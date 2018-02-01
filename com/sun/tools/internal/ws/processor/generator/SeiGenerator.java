/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotationArrayMember;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*     */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*     */ import com.sun.tools.internal.ws.processor.model.AsyncOperation;
/*     */ import com.sun.tools.internal.ws.processor.model.Block;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Message;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Request;
/*     */ import com.sun.tools.internal.ws.processor.model.Response;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBModel;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ import com.sun.tools.internal.ws.resources.GeneratorMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.Options.Target;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jws.Oneway;
/*     */ import javax.jws.WebMethod;
/*     */ import javax.jws.WebParam;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.jws.WebResult;
/*     */ import javax.jws.WebService;
/*     */ import javax.jws.soap.SOAPBinding;
/*     */ import javax.jws.soap.SOAPBinding.ParameterStyle;
/*     */ import javax.jws.soap.SOAPBinding.Style;
/*     */ import javax.xml.bind.annotation.XmlSeeAlso;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.RequestWrapper;
/*     */ import javax.xml.ws.ResponseWrapper;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class SeiGenerator extends GeneratorBase
/*     */ {
/*     */   private TJavaGeneratorExtension extension;
/*     */   private List<TJavaGeneratorExtension> extensionHandlers;
/* 419 */   private boolean isDocStyle = true;
/* 420 */   private boolean sameParamStyle = true;
/*     */ 
/*     */   public static void generate(Model model, WsimportOptions options, ErrorReceiver receiver, TJavaGeneratorExtension[] extensions)
/*     */   {
/*  60 */     SeiGenerator seiGenerator = new SeiGenerator();
/*  61 */     seiGenerator.init(model, options, receiver, extensions);
/*  62 */     seiGenerator.doGeneration();
/*     */   }
/*     */ 
/*     */   public void init(Model model, WsimportOptions options, ErrorReceiver receiver, TJavaGeneratorExtension[] extensions) {
/*  66 */     init(model, options, receiver);
/*  67 */     this.extensionHandlers = new ArrayList();
/*     */ 
/*  72 */     if (options.target.isLaterThan(Options.Target.V2_2)) {
/*  73 */       register(new W3CAddressingJavaGeneratorExtension());
/*     */     }
/*     */ 
/*  76 */     for (TJavaGeneratorExtension j : extensions) {
/*  77 */       register(j);
/*     */     }
/*     */ 
/*  80 */     this.extension = new JavaGeneratorExtensionFacade((TJavaGeneratorExtension[])this.extensionHandlers.toArray(new TJavaGeneratorExtension[this.extensionHandlers.size()]));
/*     */   }
/*     */ 
/*     */   private void write(Port port) {
/*  84 */     JavaInterface intf = port.getJavaInterface();
/*  85 */     String className = Names.customJavaTypeClassName(intf);
/*     */ 
/*  87 */     if ((this.donotOverride) && (GeneratorUtil.classExists(this.options, className))) {
/*  88 */       log("Class " + className + " exists. Not overriding.");
/*  89 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  95 */       cls = getClass(className, ClassType.INTERFACE);
/*     */     }
/*     */     catch (JClassAlreadyExistsException e)
/*     */     {
/*     */       JDefinedClass cls;
/*  98 */       QName portTypeName = (QName)port
/*  98 */         .getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/*     */ 
/* 100 */       loc = null;
/* 101 */       if (portTypeName != null) {
/* 102 */         PortType pt = (PortType)port.portTypes.get(portTypeName);
/* 103 */         if (pt != null) {
/* 104 */           loc = pt.getLocator();
/*     */         }
/*     */       }
/* 107 */       this.receiver.error(loc, GeneratorMessages.GENERATOR_SEI_CLASS_ALREADY_EXIST(intf.getName(), portTypeName));
/*     */       return;
/*     */     }
/*     */     JDefinedClass cls;
/* 112 */     if (!cls.methods().isEmpty()) {
/* 113 */       return;
/*     */     }
/*     */ 
/* 117 */     JDocComment comment = cls.javadoc();
/*     */ 
/* 119 */     String ptDoc = intf.getJavaDoc();
/* 120 */     if (ptDoc != null) {
/* 121 */       comment.add(ptDoc);
/* 122 */       comment.add("\n\n");
/*     */     }
/*     */ 
/* 125 */     for (Locator loc = getJAXWSClassComment().iterator(); loc.hasNext(); ) { doc = (String)loc.next();
/* 126 */       comment.add(doc);
/*     */     }
/*     */     String doc;
/* 131 */     JAnnotationUse webServiceAnn = cls.annotate(this.cm.ref(WebService.class));
/* 132 */     writeWebServiceAnnotation(port, webServiceAnn);
/*     */ 
/* 135 */     writeHandlerConfig(Names.customJavaTypeClassName(port.getJavaInterface()), cls, this.options);
/*     */ 
/* 138 */     writeSOAPBinding(port, cls);
/*     */ 
/* 141 */     if (this.options.target.isLaterThan(Options.Target.V2_1)) {
/* 142 */       writeXmlSeeAlso(cls);
/*     */     }
/*     */ 
/* 145 */     for (com.sun.tools.internal.ws.processor.model.Operation operation : port.getOperations()) {
/* 146 */       JavaMethod method = operation.getJavaMethod();
/*     */ 
/* 151 */       String methodJavaDoc = operation.getJavaDoc();
/*     */       JDocComment methodDoc;
/*     */       JMethod m;
/*     */       JDocComment methodDoc;
/* 152 */       if (method.getReturnType().getName().equals("void")) {
/* 153 */         JMethod m = cls.method(1, Void.TYPE, method.getName());
/* 154 */         methodDoc = m.javadoc();
/*     */       } else {
/* 156 */         JAXBTypeAndAnnotation retType = method.getReturnType().getType();
/* 157 */         m = cls.method(1, retType.getType(), method.getName());
/* 158 */         retType.annotate(m);
/* 159 */         methodDoc = m.javadoc();
/* 160 */         ret = methodDoc.addReturn();
/* 161 */         ret.add("returns " + retType.getName());
/*     */       }
/* 163 */       if (methodJavaDoc != null) {
/* 164 */         methodDoc.add(methodJavaDoc);
/*     */       }
/*     */ 
/* 167 */       writeWebMethod(operation, m);
/* 168 */       JClass holder = this.cm.ref(Holder.class);
/* 169 */       for (JCommentPart ret = method.getParametersList().iterator(); ret.hasNext(); ) { parameter = (JavaParameter)ret.next();
/*     */ 
/* 171 */         JAXBTypeAndAnnotation paramType = parameter.getType().getType();
/*     */         JVar var;
/*     */         JVar var;
/* 172 */         if (parameter.isHolder())
/* 173 */           var = m.param(holder.narrow(paramType.getType().boxify()), parameter.getName());
/*     */         else {
/* 175 */           var = m.param(paramType.getType(), parameter.getName());
/*     */         }
/*     */ 
/* 179 */         paramType.annotate(var);
/* 180 */         methodDoc.addParam(var);
/* 181 */         JAnnotationUse paramAnn = var.annotate(this.cm.ref(WebParam.class));
/* 182 */         writeWebParam(operation, parameter, paramAnn);
/*     */       }
/*     */       JavaParameter parameter;
/* 184 */       com.sun.tools.internal.ws.wsdl.document.Operation wsdlOp = operation.getWSDLPortTypeOperation();
/* 185 */       for (Fault fault : operation.getFaultsSet()) {
/* 186 */         m._throws(fault.getExceptionClass());
/* 187 */         methodDoc.addThrows(fault.getExceptionClass());
/* 188 */         wsdlOp.putFault(fault.getWsdlFaultName(), fault.getExceptionClass());
/*     */       }
/*     */ 
/* 192 */       this.extension.writeMethodAnnotations(wsdlOp, m);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeXmlSeeAlso(JDefinedClass cls)
/*     */   {
/*     */     JAnnotationArrayMember paramArray;
/* 197 */     if (this.model.getJAXBModel().getS2JJAXBModel() != null) {
/* 198 */       List objectFactories = this.model.getJAXBModel().getS2JJAXBModel().getAllObjectFactories();
/*     */ 
/* 201 */       if (objectFactories.isEmpty()) {
/* 202 */         return;
/*     */       }
/*     */ 
/* 205 */       JAnnotationUse xmlSeeAlso = cls.annotate(this.cm.ref(XmlSeeAlso.class));
/* 206 */       paramArray = xmlSeeAlso.paramArray("value");
/* 207 */       for (JClass of : objectFactories)
/* 208 */         paramArray = paramArray.param(of);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeWebMethod(com.sun.tools.internal.ws.processor.model.Operation operation, JMethod m)
/*     */   {
/* 215 */     Response response = operation.getResponse();
/* 216 */     JAnnotationUse webMethodAnn = m.annotate(this.cm.ref(WebMethod.class));
/*     */ 
/* 219 */     String operationName = (operation instanceof AsyncOperation) ? ((AsyncOperation)operation)
/* 218 */       .getNormalOperation().getName().getLocalPart() : operation
/* 219 */       .getName().getLocalPart();
/*     */ 
/* 221 */     if (!m.name().equals(operationName)) {
/* 222 */       webMethodAnn.param("operationName", operationName);
/*     */     }
/*     */ 
/* 225 */     if ((operation.getSOAPAction() != null) && (operation.getSOAPAction().length() > 0))
/* 226 */       webMethodAnn.param("action", operation.getSOAPAction());
/*     */     String resultName;
/*     */     String nsURI;
/* 229 */     if (operation.getResponse() == null) {
/* 230 */       m.annotate(Oneway.class);
/* 231 */     } else if ((!operation.getJavaMethod().getReturnType().getName().equals("void")) && 
/* 232 */       (operation
/* 232 */       .getResponse().getParametersList().size() > 0))
/*     */     {
/* 234 */       resultName = null;
/* 235 */       nsURI = null;
/* 236 */       if (operation.getResponse().getBodyBlocks().hasNext()) {
/* 237 */         Block block = (Block)operation.getResponse().getBodyBlocks().next();
/* 238 */         resultName = block.getName().getLocalPart();
/* 239 */         if ((this.isDocStyle) || (block.getLocation() == 2)) {
/* 240 */           nsURI = block.getName().getNamespaceURI();
/*     */         }
/*     */       }
/*     */ 
/* 244 */       for (Parameter parameter : operation.getResponse().getParametersList()) {
/* 245 */         if (parameter.getParameterIndex() == -1) {
/* 246 */           if ((operation.isWrapped()) || (!this.isDocStyle)) {
/* 247 */             if (parameter.getBlock().getLocation() == 2)
/* 248 */               resultName = parameter.getBlock().getName().getLocalPart();
/*     */             else {
/* 250 */               resultName = parameter.getName();
/*     */             }
/* 252 */             if ((this.isDocStyle) || (parameter.getBlock().getLocation() == 2))
/* 253 */               nsURI = parameter.getType().getName().getNamespaceURI();
/*     */           }
/* 255 */           else if (this.isDocStyle) {
/* 256 */             JAXBType t = (JAXBType)parameter.getType();
/* 257 */             resultName = t.getName().getLocalPart();
/* 258 */             nsURI = t.getName().getNamespaceURI();
/*     */           }
/* 260 */           if (!(operation instanceof AsyncOperation)) {
/* 261 */             JAnnotationUse wr = null;
/*     */ 
/* 263 */             if (!resultName.equals("return")) {
/* 264 */               wr = m.annotate(WebResult.class);
/* 265 */               wr.param("name", resultName);
/*     */             }
/* 267 */             if ((nsURI != null) || ((this.isDocStyle) && (operation.isWrapped()))) {
/* 268 */               if (wr == null) {
/* 269 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 271 */               wr.param("targetNamespace", nsURI);
/*     */             }
/*     */ 
/* 274 */             if ((!this.isDocStyle) || (!operation.isWrapped()) || 
/* 275 */               (parameter
/* 275 */               .getBlock().getLocation() == 2)) {
/* 276 */               if (wr == null) {
/* 277 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 279 */               wr.param("partName", parameter.getName());
/*     */             }
/* 281 */             if (parameter.getBlock().getLocation() == 2) {
/* 282 */               if (wr == null) {
/* 283 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 285 */               wr.param("header", true);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 294 */     if ((!this.sameParamStyle) && 
/* 295 */       (!operation.isWrapped())) {
/* 296 */       JAnnotationUse sb = m.annotate(SOAPBinding.class);
/* 297 */       sb.param("parameterStyle", SOAPBinding.ParameterStyle.BARE);
/*     */     }
/*     */ 
/* 301 */     if ((operation.isWrapped()) && (operation.getStyle().equals(SOAPStyle.DOCUMENT))) {
/* 302 */       Block reqBlock = (Block)operation.getRequest().getBodyBlocks().next();
/* 303 */       JAnnotationUse reqW = m.annotate(RequestWrapper.class);
/* 304 */       reqW.param("localName", reqBlock.getName().getLocalPart());
/* 305 */       reqW.param("targetNamespace", reqBlock.getName().getNamespaceURI());
/* 306 */       reqW.param("className", reqBlock.getType().getJavaType().getName());
/*     */ 
/* 308 */       if (response != null) {
/* 309 */         JAnnotationUse resW = m.annotate(ResponseWrapper.class);
/* 310 */         Block resBlock = (Block)response.getBodyBlocks().next();
/* 311 */         resW.param("localName", resBlock.getName().getLocalPart());
/* 312 */         resW.param("targetNamespace", resBlock.getName().getNamespaceURI());
/* 313 */         resW.param("className", resBlock.getType().getJavaType().getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isMessageParam(Parameter param, Message message) {
/* 319 */     Block block = param.getBlock();
/*     */ 
/* 323 */     return ((message.getBodyBlockCount() > 0) && (block.equals(message.getBodyBlocks().next()))) || (
/* 322 */       (message
/* 322 */       .getHeaderBlockCount() > 0) && 
/* 323 */       (block
/* 323 */       .equals(message
/* 323 */       .getHeaderBlocks().next())));
/*     */   }
/*     */ 
/*     */   private boolean isHeaderParam(Parameter param, Message message) {
/* 327 */     if (message.getHeaderBlockCount() == 0) {
/* 328 */       return false;
/*     */     }
/*     */ 
/* 331 */     for (Block headerBlock : message.getHeaderBlocksMap().values()) {
/* 332 */       if (param.getBlock().equals(headerBlock)) {
/* 333 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 337 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isAttachmentParam(Parameter param, Message message) {
/* 341 */     if (message.getAttachmentBlockCount() == 0) {
/* 342 */       return false;
/*     */     }
/*     */ 
/* 345 */     for (Block attBlock : message.getAttachmentBlocksMap().values()) {
/* 346 */       if (param.getBlock().equals(attBlock)) {
/* 347 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isUnboundParam(Parameter param, Message message) {
/* 355 */     if (message.getUnboundBlocksCount() == 0) {
/* 356 */       return false;
/*     */     }
/*     */ 
/* 359 */     for (Block unboundBlock : message.getUnboundBlocksMap().values()) {
/* 360 */       if (param.getBlock().equals(unboundBlock)) {
/* 361 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */   private void writeWebParam(com.sun.tools.internal.ws.processor.model.Operation operation, JavaParameter javaParameter, JAnnotationUse paramAnno) {
/* 369 */     Parameter param = javaParameter.getParameter();
/* 370 */     Request req = operation.getRequest();
/* 371 */     Response res = operation.getResponse();
/*     */ 
/* 374 */     boolean header = (isHeaderParam(param, req)) || ((res != null) && 
/* 374 */       (isHeaderParam(param, res)));
/*     */ 
/* 377 */     boolean isWrapped = operation.isWrapped();
/*     */     String name;
/*     */     String name;
/* 379 */     if ((param.getBlock().getLocation() == 2) || ((this.isDocStyle) && (!isWrapped)))
/* 380 */       name = param.getBlock().getName().getLocalPart();
/*     */     else {
/* 382 */       name = param.getName();
/*     */     }
/*     */ 
/* 385 */     paramAnno.param("name", name);
/*     */ 
/* 387 */     String ns = null;
/*     */ 
/* 389 */     if (this.isDocStyle) {
/* 390 */       ns = param.getBlock().getName().getNamespaceURI();
/* 391 */       if (isWrapped)
/* 392 */         ns = param.getType().getName().getNamespaceURI();
/*     */     }
/* 394 */     else if (header) {
/* 395 */       ns = param.getBlock().getName().getNamespaceURI();
/*     */     }
/*     */ 
/* 398 */     if ((ns != null) || ((this.isDocStyle) && (isWrapped))) {
/* 399 */       paramAnno.param("targetNamespace", ns);
/*     */     }
/*     */ 
/* 402 */     if (header) {
/* 403 */       paramAnno.param("header", true);
/*     */     }
/*     */ 
/* 406 */     if (param.isINOUT())
/* 407 */       paramAnno.param("mode", WebParam.Mode.INOUT);
/* 408 */     else if ((res != null) && ((isMessageParam(param, res)) || (isHeaderParam(param, res)) || (isAttachmentParam(param, res)) || 
/* 409 */       (isUnboundParam(param, res)) || 
/* 409 */       (param.isOUT()))) {
/* 410 */       paramAnno.param("mode", WebParam.Mode.OUT);
/*     */     }
/*     */ 
/* 414 */     if ((!this.isDocStyle) || (!isWrapped) || (header))
/* 415 */       paramAnno.param("partName", javaParameter.getParameter().getName());
/*     */   }
/*     */ 
/*     */   private void writeSOAPBinding(Port port, JDefinedClass cls)
/*     */   {
/* 422 */     JAnnotationUse soapBindingAnn = null;
/* 423 */     this.isDocStyle = ((port.getStyle() == null) || (port.getStyle().equals(SOAPStyle.DOCUMENT)));
/* 424 */     if (!this.isDocStyle) {
/* 425 */       soapBindingAnn = cls.annotate(SOAPBinding.class);
/* 426 */       soapBindingAnn.param("style", SOAPBinding.Style.RPC);
/* 427 */       port.setWrapped(true);
/*     */     }
/* 429 */     if (this.isDocStyle) {
/* 430 */       boolean first = true;
/* 431 */       boolean isWrapper = true;
/* 432 */       for (com.sun.tools.internal.ws.processor.model.Operation operation : port.getOperations()) {
/* 433 */         if (first) {
/* 434 */           isWrapper = operation.isWrapped();
/* 435 */           first = false;
/*     */         }
/*     */         else {
/* 438 */           this.sameParamStyle = (isWrapper == operation.isWrapped());
/* 439 */           if (!this.sameParamStyle)
/*     */             break;
/*     */         }
/*     */       }
/* 443 */       if (this.sameParamStyle) {
/* 444 */         port.setWrapped(isWrapper);
/*     */       }
/*     */     }
/* 447 */     if ((this.sameParamStyle) && (!port.isWrapped())) {
/* 448 */       if (soapBindingAnn == null) {
/* 449 */         soapBindingAnn = cls.annotate(SOAPBinding.class);
/*     */       }
/* 451 */       soapBindingAnn.param("parameterStyle", SOAPBinding.ParameterStyle.BARE);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeWebServiceAnnotation(Port port, JAnnotationUse wsa) {
/* 456 */     QName name = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/* 457 */     wsa.param("name", name.getLocalPart());
/* 458 */     wsa.param("targetNamespace", name.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   public void visit(Model model) throws Exception
/*     */   {
/* 463 */     for (Service s : model.getServices())
/* 464 */       s.accept(this);
/*     */   }
/*     */ 
/*     */   public void visit(Service service)
/*     */     throws Exception
/*     */   {
/* 470 */     String jd = this.model.getJavaDoc();
/*     */     JPackage pkg;
/* 471 */     if (jd != null) {
/* 472 */       pkg = this.cm._package(this.options.defaultPackage);
/* 473 */       pkg.javadoc().add(jd);
/*     */     }
/*     */ 
/* 476 */     for (Port p : service.getPorts())
/* 477 */       visitPort(service, p);
/*     */   }
/*     */ 
/*     */   private void visitPort(Service service, Port port)
/*     */   {
/* 482 */     if (port.isProvider()) {
/* 483 */       return;
/*     */     }
/* 485 */     write(port);
/*     */   }
/*     */ 
/*     */   private void register(TJavaGeneratorExtension h) {
/* 489 */     this.extensionHandlers.add(h);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.generator.SeiGenerator
 * JD-Core Version:    0.6.2
 */