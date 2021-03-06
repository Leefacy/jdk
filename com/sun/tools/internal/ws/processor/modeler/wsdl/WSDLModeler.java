/*      */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*      */ 
/*      */ import com.sun.codemodel.internal.JCodeModel;
/*      */ import com.sun.codemodel.internal.JType;
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.istack.internal.SAXParseException2;
/*      */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*      */ import com.sun.tools.internal.ws.processor.generator.Names;
/*      */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*      */ import com.sun.tools.internal.ws.processor.model.AsyncOperation;
/*      */ import com.sun.tools.internal.ws.processor.model.AsyncOperationType;
/*      */ import com.sun.tools.internal.ws.processor.model.Block;
/*      */ import com.sun.tools.internal.ws.processor.model.Model;
/*      */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*      */ import com.sun.tools.internal.ws.processor.model.ModelObject;
/*      */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*      */ import com.sun.tools.internal.ws.processor.model.Request;
/*      */ import com.sun.tools.internal.ws.processor.model.Response;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaException;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaSimpleType;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureMember;
/*      */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBElementMember;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBModel;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBProperty;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBStructuredType;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*      */ import com.sun.tools.internal.ws.processor.model.jaxb.RpcLitStructure;
/*      */ import com.sun.tools.internal.ws.processor.modeler.JavaSimpleTypeCreator;
/*      */ import com.sun.tools.internal.ws.processor.util.ClassNameCollector;
/*      */ import com.sun.tools.internal.ws.resources.ModelerMessages;
/*      */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*      */ import com.sun.tools.internal.ws.wscompile.ErrorReceiverFilter;
/*      */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Binding;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingFault;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingInput;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingOutput;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Definitions;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Documentation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Input;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Kinds;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*      */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*      */ import com.sun.tools.internal.ws.wsdl.document.OperationStyle;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Output;
/*      */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*      */ import com.sun.tools.internal.ws.wsdl.document.WSDLConstants;
/*      */ import com.sun.tools.internal.ws.wsdl.document.WSDLDocument;
/*      */ import com.sun.tools.internal.ws.wsdl.document.jaxws.CustomName;
/*      */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBinding;
/*      */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEContent;
/*      */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPAddress;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBody;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPFault;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeader;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPOperation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*      */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.ParseException;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.ParserListener;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*      */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*      */ import com.sun.tools.internal.ws.wsdl.parser.WSDLParser;
/*      */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*      */ import com.sun.tools.internal.xjc.api.SchemaCompiler;
/*      */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*      */ import com.sun.tools.internal.xjc.api.XJC;
/*      */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*      */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.jws.WebParam.Mode;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.ws.Holder;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.LocatorImpl;
/*      */ 
/*      */ public class WSDLModeler extends WSDLModelerBase
/*      */ {
/*   80 */   private final Map<QName, com.sun.tools.internal.ws.processor.model.Operation> uniqueBodyBlocks = new HashMap();
/*   81 */   private final QName VOID_BODYBLOCK = new QName("");
/*      */   private final ClassNameCollector classNameCollector;
/*      */   private final String explicitDefaultPackage;
/*      */   private JAXBModelBuilder jaxbModelBuilder;
/*      */ 
/*      */   public WSDLModeler(WsimportOptions options, ErrorReceiver receiver, MetadataFinder forest)
/*      */   {
/*   86 */     super(options, receiver, forest);
/*   87 */     this.classNameCollector = new ClassNameCollector();
/*   88 */     this.explicitDefaultPackage = options.defaultPackage;
/*      */   }
/*      */ 
/*      */   public Model buildModel()
/*      */   {
/*      */     try
/*      */     {
/*  101 */       this.parser = new WSDLParser(this.options, this.errReceiver, this.forest);
/*  102 */       this.parser.addParserListener(new ParserListener()
/*      */       {
/*      */         public void ignoringExtension(Entity entity, QName name, QName parent) {
/*  105 */           if (parent.equals(WSDLConstants.QNAME_TYPES))
/*      */           {
/*  107 */             if ((name.getLocalPart().equals("schema")) && 
/*  108 */               (!name
/*  108 */               .getNamespaceURI().equals("")))
/*  109 */               WSDLModeler.this.warning(entity, ModelerMessages.WSDLMODELER_WARNING_IGNORING_UNRECOGNIZED_SCHEMA_EXTENSION(name.getNamespaceURI()));
/*      */           }
/*      */         }
/*      */ 
/*      */         public void doneParsingEntity(QName element, Entity entity)
/*      */         {
/*      */         }
/*      */       });
/*  120 */       this.document = this.parser.parse();
/*  121 */       if ((this.document == null) || (this.document.getDefinitions() == null)) {
/*  122 */         return null;
/*      */       }
/*      */ 
/*  125 */       this.document.validateLocally();
/*  126 */       Model model = internalBuildModel(this.document);
/*  127 */       if ((model == null) || (this.errReceiver.hadError())) {
/*  128 */         return null;
/*      */       }
/*      */ 
/*  131 */       this.classNameCollector.process(model);
/*  132 */       if (this.classNameCollector.getConflictingClassNames().isEmpty()) {
/*  133 */         if (this.errReceiver.hadError()) {
/*  134 */           return null;
/*      */         }
/*  136 */         return model;
/*      */       }
/*      */ 
/*  139 */       model = internalBuildModel(this.document);
/*      */ 
/*  141 */       this.classNameCollector.process(model);
/*  142 */       if (this.classNameCollector.getConflictingClassNames().isEmpty())
/*      */       {
/*  144 */         if (this.errReceiver.hadError()) {
/*  145 */           return null;
/*      */         }
/*  147 */         return model;
/*      */       }
/*      */ 
/*  150 */       StringBuilder conflictList = new StringBuilder();
/*  151 */       boolean first = true;
/*      */ 
/*  153 */       Iterator iter = this.classNameCollector
/*  153 */         .getConflictingClassNames().iterator();
/*  154 */       while (iter.hasNext())
/*      */       {
/*  156 */         if (!first)
/*  157 */           conflictList.append(", ");
/*      */         else {
/*  159 */           first = false;
/*      */         }
/*  161 */         conflictList.append((String)iter.next());
/*      */       }
/*  163 */       error(this.document.getDefinitions(), ModelerMessages.WSDLMODELER_UNSOLVABLE_NAMING_CONFLICTS(conflictList.toString()));
/*      */     } catch (ModelException e) {
/*  165 */       reportError(this.document.getDefinitions(), e.getMessage(), e);
/*      */     } catch (ParseException e) {
/*  167 */       this.errReceiver.error(e);
/*      */     } catch (ValidationException e) {
/*  169 */       this.errReceiver.error(e.getMessage(), e);
/*      */     } catch (SAXException e) {
/*  171 */       this.errReceiver.error(e);
/*      */     } catch (IOException e) {
/*  173 */       this.errReceiver.error(e);
/*      */     }
/*      */ 
/*  176 */     return null;
/*      */   }
/*      */ 
/*      */   private Model internalBuildModel(WSDLDocument document) {
/*  180 */     this.numPasses += 1;
/*      */ 
/*  183 */     buildJAXBModel(document);
/*      */ 
/*  190 */     QName modelName = new QName(document
/*  187 */       .getDefinitions().getTargetNamespaceURI(), document
/*  188 */       .getDefinitions().getName() == null ? "model" : document
/*  190 */       .getDefinitions().getName());
/*  191 */     Model model = new Model(modelName, document.getDefinitions());
/*  192 */     model.setJAXBModel(getJAXBModelBuilder().getJAXBModel());
/*      */ 
/*  198 */     model.setProperty("com.sun.xml.internal.ws.processor.model.ModelerName", "com.sun.xml.internal.ws.processor.modeler.wsdl.WSDLModeler");
/*      */ 
/*  202 */     this._javaExceptions = new HashMap();
/*  203 */     this._bindingNameToPortMap = new HashMap();
/*      */ 
/*  206 */     model.setTargetNamespaceURI(document.getDefinitions().getTargetNamespaceURI());
/*      */ 
/*  208 */     setDocumentationIfPresent(model, document
/*  209 */       .getDefinitions().getDocumentation());
/*      */ 
/*  211 */     boolean hasServices = document.getDefinitions().services().hasNext();
/*  212 */     if (hasServices) {
/*  213 */       Iterator iter = document.getDefinitions().services();
/*  214 */       while (iter.hasNext())
/*      */       {
/*  216 */         processService((com.sun.tools.internal.ws.wsdl.document.Service)iter.next(), model, document);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  221 */       warning(model.getEntity(), ModelerMessages.WSDLMODELER_WARNING_NO_SERVICE_DEFINITIONS_FOUND());
/*      */     }
/*      */ 
/*  224 */     return model;
/*      */   }
/*      */ 
/*      */   protected void processService(com.sun.tools.internal.ws.wsdl.document.Service wsdlService, Model model, WSDLDocument document)
/*      */   {
/*  231 */     QName serviceQName = getQNameOf(wsdlService);
/*  232 */     String serviceInterface = getServiceInterfaceName(serviceQName, wsdlService);
/*  233 */     if (isConflictingServiceClassName(serviceInterface)) {
/*  234 */       serviceInterface = serviceInterface + "_Service";
/*      */     }
/*  236 */     com.sun.tools.internal.ws.processor.model.Service service = new com.sun.tools.internal.ws.processor.model.Service(serviceQName, new JavaInterface(serviceInterface, serviceInterface + "Impl"), wsdlService);
/*      */ 
/*  241 */     setDocumentationIfPresent(service, wsdlService.getDocumentation());
/*  242 */     boolean hasPorts = false;
/*  243 */     for (Iterator iter = wsdlService.ports(); iter.hasNext(); )
/*      */     {
/*  245 */       boolean processed = processPort(
/*  246 */         (com.sun.tools.internal.ws.wsdl.document.Port)iter
/*  246 */         .next(), service, document);
/*      */ 
/*  249 */       hasPorts = (hasPorts) || (processed);
/*      */     }
/*  251 */     if (!hasPorts)
/*      */     {
/*  253 */       warning(wsdlService, ModelerMessages.WSDLMODELER_WARNING_NO_PORTS_IN_SERVICE(wsdlService.getName()));
/*      */     }
/*  255 */     else model.addService(service);
/*      */   }
/*      */ 
/*      */   protected boolean processPort(com.sun.tools.internal.ws.wsdl.document.Port wsdlPort, com.sun.tools.internal.ws.processor.model.Service service, WSDLDocument document)
/*      */   {
/*      */     try
/*      */     {
/*  267 */       this.uniqueBodyBlocks.clear();
/*      */ 
/*  269 */       QName portQName = getQNameOf(wsdlPort);
/*  270 */       com.sun.tools.internal.ws.processor.model.Port port = new com.sun.tools.internal.ws.processor.model.Port(portQName, wsdlPort);
/*      */ 
/*  272 */       setDocumentationIfPresent(port, wsdlPort.getDocumentation());
/*      */ 
/*  275 */       SOAPAddress soapAddress = (SOAPAddress)getExtensionOfType(wsdlPort, SOAPAddress.class);
/*      */ 
/*  276 */       if (soapAddress == null) {
/*  277 */         if (this.options.isExtensionMode()) {
/*  278 */           warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_NO_SOAP_ADDRESS(wsdlPort.getName()));
/*      */         }
/*      */         else {
/*  281 */           warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT_NO_ADDRESS(wsdlPort.getName()));
/*  282 */           return false;
/*      */         }
/*      */       }
/*  285 */       if (soapAddress != null) {
/*  286 */         port.setAddress(soapAddress.getLocation());
/*      */       }
/*  288 */       Binding binding = wsdlPort.resolveBinding(document);
/*  289 */       QName bindingName = getQNameOf(binding);
/*  290 */       PortType portType = binding.resolvePortType(document);
/*      */ 
/*  292 */       port.setProperty("com.sun.xml.internal.ws.processor.model.WSDLPortName", 
/*  294 */         getQNameOf(wsdlPort));
/*      */ 
/*  295 */       port.setProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName", 
/*  297 */         getQNameOf(portType));
/*      */ 
/*  298 */       port.setProperty("com.sun.xml.internal.ws.processor.model.WSDLBindingName", bindingName);
/*      */ 
/*  302 */       boolean isProvider = isProvider(wsdlPort);
/*  303 */       if ((this._bindingNameToPortMap.containsKey(bindingName)) && (!isProvider))
/*      */       {
/*  306 */         com.sun.tools.internal.ws.processor.model.Port existingPort = (com.sun.tools.internal.ws.processor.model.Port)this._bindingNameToPortMap
/*  306 */           .get(bindingName);
/*      */ 
/*  307 */         port.setOperations(existingPort.getOperations());
/*  308 */         port.setJavaInterface(existingPort.getJavaInterface());
/*  309 */         port.setStyle(existingPort.getStyle());
/*  310 */         port.setWrapped(existingPort.isWrapped());
/*      */       }
/*      */       else
/*      */       {
/*  314 */         SOAPBinding soapBinding = (SOAPBinding)getExtensionOfType(binding, SOAPBinding.class);
/*      */ 
/*  316 */         if (soapBinding == null)
/*      */         {
/*  318 */           soapBinding = (SOAPBinding)getExtensionOfType(binding, SOAP12Binding.class);
/*      */ 
/*  319 */           if (soapBinding == null) {
/*  320 */             if (!this.options.isExtensionMode())
/*      */             {
/*  322 */               warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT(wsdlPort.getName()));
/*  323 */               return false;
/*      */             }
/*  325 */             warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_NON_SOAP_PORT(wsdlPort.getName()));
/*      */           }
/*  329 */           else if (this.options.isExtensionMode()) {
/*  330 */             warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_PORT_SOAP_BINDING_12(wsdlPort.getName()));
/*      */           } else {
/*  332 */             warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_12(wsdlPort.getName()));
/*  333 */             return false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  338 */         if ((soapBinding != null) && ((soapBinding.getTransport() == null) || (
/*  339 */           (!soapBinding
/*  339 */           .getTransport().equals("http://schemas.xmlsoap.org/soap/http")) && 
/*  340 */           (!soapBinding
/*  340 */           .getTransport().equals("http://www.w3.org/2003/05/soap/bindings/HTTP/")))))
/*      */         {
/*  342 */           if (!this.options.isExtensionMode())
/*      */           {
/*  344 */             warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_NON_HTTP_TRANSPORT(wsdlPort.getName()));
/*  345 */             return false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  354 */         if ((soapBinding != null) && (!validateWSDLBindingStyle(binding))) {
/*  355 */           if (this.options.isExtensionMode())
/*  356 */             warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_PORT_SOAP_BINDING_MIXED_STYLE(wsdlPort.getName()));
/*      */           else {
/*  358 */             error(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_MIXED_STYLE(wsdlPort.getName()));
/*      */           }
/*      */         }
/*      */ 
/*  362 */         if (soapBinding != null) {
/*  363 */           port.setStyle(soapBinding.getStyle());
/*      */         }
/*      */ 
/*  366 */         boolean hasOverloadedOperations = false;
/*  367 */         Set operationNames = new HashSet();
/*  368 */         for (Iterator iter = portType.operations(); iter.hasNext(); )
/*      */         {
/*  370 */           com.sun.tools.internal.ws.wsdl.document.Operation operation = (com.sun.tools.internal.ws.wsdl.document.Operation)iter
/*  370 */             .next();
/*      */ 
/*  372 */           if (operationNames.contains(operation.getName())) {
/*  373 */             hasOverloadedOperations = true;
/*  374 */             break;
/*      */           }
/*  376 */           operationNames.add(operation.getName());
/*      */ 
/*  378 */           Iterator itr = binding.operations();
/*  379 */           while (iter.hasNext())
/*      */           {
/*  382 */             BindingOperation bindingOperation = (BindingOperation)itr
/*  382 */               .next();
/*      */ 
/*  385 */             if (operation
/*  384 */               .getName()
/*  385 */               .equals(bindingOperation
/*  385 */               .getName()))
/*      */               break;
/*  387 */             if (!itr.hasNext()) {
/*  388 */               error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_NOT_FOUND(operation.getName(), bindingOperation.getName()));
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  393 */         Map headers = new HashMap();
/*  394 */         boolean hasOperations = false;
/*  395 */         for (Iterator iter = binding.operations(); iter.hasNext(); )
/*      */         {
/*  397 */           BindingOperation bindingOperation = (BindingOperation)iter
/*  397 */             .next();
/*      */ 
/*  399 */           com.sun.tools.internal.ws.wsdl.document.Operation portTypeOperation = null;
/*      */ 
/*  402 */           Set operations = portType
/*  402 */             .getOperationsNamed(bindingOperation
/*  402 */             .getName());
/*  403 */           if (operations.isEmpty())
/*      */           {
/*  405 */             error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_NOT_IN_PORT_TYPE(bindingOperation.getName(), binding.getName()));
/*  406 */           } else if (operations.size() == 1)
/*      */           {
/*  410 */             portTypeOperation = (com.sun.tools.internal.ws.wsdl.document.Operation)operations
/*  409 */               .iterator()
/*  410 */               .next();
/*      */           } else {
/*  412 */             boolean found = false;
/*      */ 
/*  414 */             String expectedInputName = bindingOperation
/*  414 */               .getInput().getName();
/*      */ 
/*  416 */             String expectedOutputName = bindingOperation
/*  416 */               .getOutput().getName();
/*      */ 
/*  418 */             for (Iterator iter2 = operations.iterator(); iter2.hasNext(); )
/*      */             {
/*  421 */               com.sun.tools.internal.ws.wsdl.document.Operation candidateOperation = (com.sun.tools.internal.ws.wsdl.document.Operation)iter2
/*  421 */                 .next();
/*      */ 
/*  423 */               if (expectedInputName == null)
/*      */               {
/*  425 */                 error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_MISSING_INPUT_NAME(bindingOperation.getName()));
/*      */               }
/*  427 */               if (expectedOutputName == null)
/*      */               {
/*  429 */                 error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_MISSING_OUTPUT_NAME(bindingOperation.getName()));
/*      */               }
/*      */ 
/*  432 */               if ((expectedInputName
/*  432 */                 .equals(candidateOperation
/*  432 */                 .getInput().getName())) && 
/*  433 */                 (expectedOutputName
/*  433 */                 .equals(candidateOperation
/*  435 */                 .getOutput()
/*  436 */                 .getName()))) {
/*  437 */                 if (found)
/*      */                 {
/*  439 */                   error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_MATCHING_OPERATIONS(bindingOperation.getName(), bindingOperation.getName()));
/*      */                 }
/*      */ 
/*  442 */                 found = true;
/*  443 */                 portTypeOperation = candidateOperation;
/*      */               }
/*      */             }
/*  446 */             if (!found)
/*      */             {
/*  448 */               error(bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_NOT_FOUND(bindingOperation.getName(), binding.getName()));
/*      */             }
/*      */           }
/*  451 */           if (!isProvider) {
/*  452 */             this.info = new WSDLModelerBase.ProcessSOAPOperationInfo(port, wsdlPort, portTypeOperation, bindingOperation, soapBinding, document, hasOverloadedOperations, headers);
/*      */             com.sun.tools.internal.ws.processor.model.Operation operation;
/*      */             com.sun.tools.internal.ws.processor.model.Operation operation;
/*  465 */             if (soapBinding != null)
/*  466 */               operation = processSOAPOperation();
/*      */             else {
/*  468 */               operation = processNonSOAPOperation();
/*      */             }
/*  470 */             if (operation != null) {
/*  471 */               port.addOperation(operation);
/*  472 */               hasOperations = true;
/*      */             }
/*      */           }
/*      */         }
/*  476 */         if ((!isProvider) && (!hasOperations))
/*      */         {
/*  478 */           warning(wsdlPort, ModelerMessages.WSDLMODELER_WARNING_NO_OPERATIONS_IN_PORT(wsdlPort.getName()));
/*  479 */           return false;
/*      */         }
/*  481 */         createJavaInterfaceForPort(port, isProvider);
/*  482 */         PortType pt = binding.resolvePortType(document);
/*  483 */         String jd = pt.getDocumentation() != null ? pt.getDocumentation().getContent() : null;
/*  484 */         port.getJavaInterface().setJavaDoc(jd);
/*  485 */         this._bindingNameToPortMap.put(bindingName, port);
/*      */       }
/*      */ 
/*  488 */       service.addPort(port);
/*  489 */       applyPortMethodCustomization(port, wsdlPort);
/*  490 */       applyWrapperStyleCustomization(port, binding.resolvePortType(document));
/*      */ 
/*  492 */       return true;
/*      */     }
/*      */     catch (NoSuchEntityException e) {
/*  495 */       warning(document.getDefinitions(), e.getMessage());
/*      */     }
/*  497 */     return false;
/*      */   }
/*      */ 
/*      */   private com.sun.tools.internal.ws.processor.model.Operation processNonSOAPOperation()
/*      */   {
/*  506 */     com.sun.tools.internal.ws.processor.model.Operation operation = new com.sun.tools.internal.ws.processor.model.Operation(new QName(null, this.info.bindingOperation
/*  506 */       .getName()), this.info.bindingOperation);
/*      */ 
/*  508 */     setDocumentationIfPresent(operation, this.info.portTypeOperation
/*  510 */       .getDocumentation());
/*      */ 
/*  512 */     if (this.info.portTypeOperation.getStyle() != OperationStyle.REQUEST_RESPONSE)
/*      */     {
/*  514 */       if (this.info.portTypeOperation
/*  514 */         .getStyle() != OperationStyle.ONE_WAY) {
/*  515 */         if (this.options.isExtensionMode()) {
/*  516 */           warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_SUPPORTED_STYLE(this.info.portTypeOperation.getName()));
/*  517 */           return null;
/*      */         }
/*  519 */         error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_OPERATION_NOT_SUPPORTED_STYLE(this.info.portTypeOperation.getName(), this.info.port
/*  520 */           .resolveBinding(this.document)
/*  520 */           .resolvePortType(this.document).getName()));
/*      */       }
/*      */     }
/*      */ 
/*  524 */     boolean isRequestResponse = this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE;
/*  525 */     Message inputMessage = getInputMessage();
/*  526 */     Request request = new Request(inputMessage, this.errReceiver);
/*  527 */     request.setErrorReceiver(this.errReceiver);
/*  528 */     this.info.operation = operation;
/*  529 */     this.info.operation.setWSDLPortTypeOperation(this.info.portTypeOperation);
/*      */ 
/*  533 */     Message outputMessage = null;
/*      */     Response response;
/*      */     Response response;
/*  534 */     if (isRequestResponse) {
/*  535 */       outputMessage = getOutputMessage();
/*  536 */       response = new Response(outputMessage, this.errReceiver);
/*      */     } else {
/*  538 */       response = new Response(null, this.errReceiver);
/*      */     }
/*      */ 
/*  543 */     setNonSoapStyle(inputMessage, outputMessage);
/*      */ 
/*  546 */     List parameterList = getParameterOrder();
/*      */ 
/*  548 */     boolean unwrappable = isUnwrappable();
/*  549 */     this.info.operation.setWrapped(unwrappable);
/*  550 */     List params = getDoclitParameters(request, response, parameterList);
/*  551 */     if (!validateParameterName(params)) {
/*  552 */       return null;
/*      */     }
/*      */ 
/*  557 */     List definitiveParameterList = new ArrayList();
/*  558 */     for (Parameter param : params) {
/*  559 */       if (param.isReturn()) {
/*  560 */         this.info.operation.setProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter", param);
/*  561 */         response.addParameter(param);
/*      */       }
/*      */       else {
/*  564 */         if (param.isIN()) {
/*  565 */           request.addParameter(param);
/*  566 */         } else if (param.isOUT()) {
/*  567 */           response.addParameter(param);
/*  568 */         } else if (param.isINOUT()) {
/*  569 */           request.addParameter(param);
/*  570 */           response.addParameter(param);
/*      */         }
/*  572 */         definitiveParameterList.add(param);
/*      */       }
/*      */     }
/*  575 */     this.info.operation.setRequest(request);
/*      */ 
/*  577 */     if (isRequestResponse) {
/*  578 */       this.info.operation.setResponse(response);
/*      */     }
/*      */ 
/*  582 */     Set duplicateNames = getDuplicateFaultNames();
/*      */ 
/*  585 */     handleLiteralSOAPFault(response, duplicateNames);
/*  586 */     this.info.operation.setProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.parameterOrder", definitiveParameterList);
/*      */ 
/*  590 */     Binding binding = this.info.port.resolveBinding(this.document);
/*  591 */     PortType portType = binding.resolvePortType(this.document);
/*  592 */     if (isAsync(portType, this.info.portTypeOperation)) {
/*  593 */       warning(portType, "Can not generate Async methods for non-soap binding!");
/*      */     }
/*  595 */     return this.info.operation;
/*      */   }
/*      */ 
/*      */   private void setNonSoapStyle(Message inputMessage, Message outputMessage)
/*      */   {
/*  608 */     SOAPStyle style = SOAPStyle.DOCUMENT;
/*  609 */     for (MessagePart part : inputMessage.getParts()) {
/*  610 */       if (part.getDescriptorKind() == SchemaKinds.XSD_TYPE)
/*  611 */         style = SOAPStyle.RPC;
/*      */       else {
/*  613 */         style = SOAPStyle.DOCUMENT;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  618 */     if (outputMessage != null) {
/*  619 */       for (MessagePart part : outputMessage.getParts()) {
/*  620 */         if (part.getDescriptorKind() == SchemaKinds.XSD_TYPE)
/*  621 */           style = SOAPStyle.RPC;
/*      */         else {
/*  623 */           style = SOAPStyle.DOCUMENT;
/*      */         }
/*      */       }
/*      */     }
/*  627 */     this.info.modelPort.setStyle(style);
/*      */   }
/*      */ 
/*      */   protected com.sun.tools.internal.ws.processor.model.Operation processSOAPOperation()
/*      */   {
/*  635 */     com.sun.tools.internal.ws.processor.model.Operation operation = new com.sun.tools.internal.ws.processor.model.Operation(new QName(null, this.info.bindingOperation
/*  635 */       .getName()), this.info.bindingOperation);
/*      */ 
/*  637 */     setDocumentationIfPresent(operation, this.info.portTypeOperation
/*  639 */       .getDocumentation());
/*      */ 
/*  641 */     if (this.info.portTypeOperation.getStyle() != OperationStyle.REQUEST_RESPONSE)
/*      */     {
/*  643 */       if (this.info.portTypeOperation
/*  643 */         .getStyle() != OperationStyle.ONE_WAY) {
/*  644 */         if (this.options.isExtensionMode()) {
/*  645 */           warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_SUPPORTED_STYLE(this.info.portTypeOperation.getName()));
/*  646 */           return null;
/*      */         }
/*  648 */         error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_OPERATION_NOT_SUPPORTED_STYLE(this.info.portTypeOperation.getName(), this.info.port
/*  649 */           .resolveBinding(this.document)
/*  649 */           .resolvePortType(this.document).getName()));
/*      */       }
/*      */     }
/*      */ 
/*  653 */     SOAPStyle soapStyle = this.info.soapBinding.getStyle();
/*      */ 
/*  657 */     SOAPOperation soapOperation = (SOAPOperation)getExtensionOfType(this.info.bindingOperation, SOAPOperation.class);
/*      */ 
/*  660 */     if (soapOperation != null) {
/*  661 */       if (soapOperation.getStyle() != null) {
/*  662 */         soapStyle = soapOperation.getStyle();
/*      */       }
/*  664 */       if (soapOperation.getSOAPAction() != null) {
/*  665 */         operation.setSOAPAction(soapOperation.getSOAPAction());
/*      */       }
/*      */     }
/*      */ 
/*  669 */     operation.setStyle(soapStyle);
/*      */ 
/*  672 */     String uniqueOperationName = getUniqueName(this.info.portTypeOperation, this.info.hasOverloadedOperations);
/*      */ 
/*  673 */     if (this.info.hasOverloadedOperations) {
/*  674 */       operation.setUniqueName(uniqueOperationName);
/*      */     }
/*      */ 
/*  677 */     this.info.operation = operation;
/*      */ 
/*  680 */     SOAPBody soapRequestBody = getSOAPRequestBody();
/*  681 */     if (soapRequestBody == null)
/*      */     {
/*  683 */       error(this.info.bindingOperation, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(this.info.bindingOperation.getName()));
/*      */     }
/*      */ 
/*  686 */     if (soapStyle == SOAPStyle.RPC) {
/*  687 */       if (soapRequestBody.isEncoded()) {
/*  688 */         if (this.options.isExtensionMode()) {
/*  689 */           warning(soapRequestBody, ModelerMessages.WSDLMODELER_20_RPCENC_NOT_SUPPORTED());
/*  690 */           processNonSOAPOperation();
/*      */         } else {
/*  692 */           error(soapRequestBody, ModelerMessages.WSDLMODELER_20_RPCENC_NOT_SUPPORTED());
/*      */         }
/*      */       }
/*  695 */       return processLiteralSOAPOperation(StyleAndUse.RPC_LITERAL);
/*      */     }
/*      */ 
/*  698 */     return processLiteralSOAPOperation(StyleAndUse.DOC_LITERAL);
/*      */   }
/*      */ 
/*      */   protected com.sun.tools.internal.ws.processor.model.Operation processLiteralSOAPOperation(StyleAndUse styleAndUse)
/*      */   {
/*  703 */     if (!applyOperationNameCustomization()) {
/*  704 */       return null;
/*      */     }
/*      */ 
/*  707 */     boolean isRequestResponse = this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE;
/*  708 */     Message inputMessage = getInputMessage();
/*  709 */     Request request = new Request(inputMessage, this.errReceiver);
/*  710 */     request.setErrorReceiver(this.errReceiver);
/*  711 */     this.info.operation.setUse(SOAPUse.LITERAL);
/*  712 */     this.info.operation.setWSDLPortTypeOperation(this.info.portTypeOperation);
/*  713 */     SOAPBody soapRequestBody = getSOAPRequestBody();
/*  714 */     if ((StyleAndUse.DOC_LITERAL == styleAndUse) && (soapRequestBody.getNamespace() != null)) {
/*  715 */       warning(soapRequestBody, ModelerMessages.WSDLMODELER_WARNING_R_2716("soapbind:body", this.info.bindingOperation.getName()));
/*      */     }
/*      */ 
/*  721 */     SOAPBody soapResponseBody = null;
/*  722 */     Message outputMessage = null;
/*      */     Response response;
/*      */     Response response;
/*  723 */     if (isRequestResponse) {
/*  724 */       soapResponseBody = getSOAPResponseBody();
/*  725 */       if ((isOperationDocumentLiteral(styleAndUse)) && (soapResponseBody.getNamespace() != null)) {
/*  726 */         warning(soapResponseBody, ModelerMessages.WSDLMODELER_WARNING_R_2716("soapbind:body", this.info.bindingOperation.getName()));
/*      */       }
/*  728 */       outputMessage = getOutputMessage();
/*  729 */       response = new Response(outputMessage, this.errReceiver);
/*      */     } else {
/*  731 */       response = new Response(null, this.errReceiver);
/*      */     }
/*      */ 
/*  735 */     if ((!validateMimeParts(getMimeParts(this.info.bindingOperation.getInput()))) || 
/*  736 */       (!validateMimeParts(getMimeParts(this.info.bindingOperation
/*  736 */       .getOutput())))) {
/*  737 */       return null;
/*      */     }
/*      */ 
/*  740 */     if (!validateBodyParts(this.info.bindingOperation))
/*      */     {
/*  747 */       if (isOperationDocumentLiteral(styleAndUse)) {
/*  748 */         if (this.options.isExtensionMode())
/*  749 */           warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_TYPE_MESSAGE_PART(this.info.portTypeOperation.getName()));
/*      */         else
/*  751 */           error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_DOCLITOPERATION(this.info.portTypeOperation.getName()));
/*      */       }
/*  753 */       else if (isOperationRpcLiteral(styleAndUse)) {
/*  754 */         if (this.options.isExtensionMode())
/*  755 */           warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_ELEMENT_MESSAGE_PART(this.info.portTypeOperation.getName()));
/*      */         else {
/*  757 */           error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_RPCLITOPERATION(this.info.portTypeOperation.getName()));
/*      */         }
/*      */       }
/*  760 */       return null;
/*      */     }
/*      */ 
/*  764 */     List parameterList = getParameterOrder();
/*      */ 
/*  767 */     if (!setMessagePartsBinding(styleAndUse)) {
/*  768 */       return null;
/*      */     }
/*      */ 
/*  771 */     List params = null;
/*  772 */     boolean unwrappable = isUnwrappable();
/*  773 */     this.info.operation.setWrapped(unwrappable);
/*      */     Block reqBlock;
/*  774 */     if (isOperationDocumentLiteral(styleAndUse)) {
/*  775 */       params = getDoclitParameters(request, response, parameterList);
/*  776 */     } else if (isOperationRpcLiteral(styleAndUse)) {
/*  777 */       String operationName = this.info.bindingOperation.getName();
/*  778 */       reqBlock = null;
/*  779 */       if (inputMessage != null) {
/*  780 */         QName name = new QName(getRequestNamespaceURI(soapRequestBody), operationName);
/*  781 */         RpcLitStructure rpcStruct = new RpcLitStructure(name, getJAXBModelBuilder().getJAXBModel());
/*  782 */         rpcStruct.setJavaType(new JavaSimpleType("com.sun.xml.internal.ws.encoding.jaxb.RpcLitPayload", null));
/*  783 */         reqBlock = new Block(name, rpcStruct, inputMessage);
/*  784 */         request.addBodyBlock(reqBlock);
/*      */       }
/*      */ 
/*  787 */       Block resBlock = null;
/*  788 */       if ((isRequestResponse) && (outputMessage != null)) {
/*  789 */         QName name = new QName(getResponseNamespaceURI(soapResponseBody), operationName + "Response");
/*  790 */         RpcLitStructure rpcStruct = new RpcLitStructure(name, getJAXBModelBuilder().getJAXBModel());
/*  791 */         rpcStruct.setJavaType(new JavaSimpleType("com.sun.xml.internal.ws.encoding.jaxb.RpcLitPayload", null));
/*  792 */         resBlock = new Block(name, rpcStruct, outputMessage);
/*  793 */         response.addBodyBlock(resBlock);
/*      */       }
/*  795 */       params = getRpcLitParameters(request, response, reqBlock, resBlock, parameterList);
/*      */     }
/*      */ 
/*  799 */     if (!validateParameterName(params)) {
/*  800 */       return null;
/*      */     }
/*      */ 
/*  805 */     List definitiveParameterList = new ArrayList();
/*  806 */     for (Parameter param : params) {
/*  807 */       if (param.isReturn()) {
/*  808 */         this.info.operation.setProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter", param);
/*  809 */         response.addParameter(param);
/*      */       }
/*      */       else {
/*  812 */         if (param.isIN()) {
/*  813 */           request.addParameter(param);
/*  814 */         } else if (param.isOUT()) {
/*  815 */           response.addParameter(param);
/*  816 */         } else if (param.isINOUT()) {
/*  817 */           request.addParameter(param);
/*  818 */           response.addParameter(param);
/*      */         }
/*  820 */         definitiveParameterList.add(param);
/*      */       }
/*      */     }
/*  823 */     this.info.operation.setRequest(request);
/*      */ 
/*  825 */     if (isRequestResponse) {
/*  826 */       this.info.operation.setResponse(response);
/*      */     }
/*      */ 
/*  829 */     Iterator bb = request.getBodyBlocks();
/*      */     com.sun.tools.internal.ws.processor.model.Operation thatOp;
/*      */     QName body;
/*      */     com.sun.tools.internal.ws.processor.model.Operation thatOp;
/*  832 */     if (bb.hasNext()) {
/*  833 */       QName body = ((Block)bb.next()).getName();
/*  834 */       thatOp = (com.sun.tools.internal.ws.processor.model.Operation)this.uniqueBodyBlocks.get(body);
/*      */     }
/*      */     else {
/*  837 */       body = this.VOID_BODYBLOCK;
/*  838 */       thatOp = (com.sun.tools.internal.ws.processor.model.Operation)this.uniqueBodyBlocks.get(this.VOID_BODYBLOCK);
/*      */     }
/*      */ 
/*  841 */     if (thatOp != null) {
/*  842 */       if (this.options.isExtensionMode())
/*  843 */         warning(this.info.port, ModelerMessages.WSDLMODELER_NON_UNIQUE_BODY_WARNING(this.info.port.getName(), this.info.operation.getName(), thatOp.getName(), body));
/*      */       else
/*  845 */         error(this.info.port, ModelerMessages.WSDLMODELER_NON_UNIQUE_BODY_ERROR(this.info.port.getName(), this.info.operation.getName(), thatOp.getName(), body));
/*      */     }
/*      */     else
/*  848 */       this.uniqueBodyBlocks.put(body, this.info.operation);
/*      */     List additionalHeaders;
/*      */     MessagePart part;
/*      */     QName name;
/*  852 */     if (this.options.additionalHeaders) {
/*  853 */       additionalHeaders = new ArrayList();
/*      */       Iterator localIterator1;
/*  854 */       if (inputMessage != null) {
/*  855 */         for (localIterator1 = getAdditionHeaderParts(this.info.bindingOperation, inputMessage, true).iterator(); localIterator1.hasNext(); ) { part = (MessagePart)localIterator1.next();
/*  856 */           QName name = part.getDescriptor();
/*  857 */           JAXBType jaxbType = getJAXBType(part);
/*  858 */           Block block = new Block(name, jaxbType, part);
/*  859 */           Parameter param = ModelerUtils.createParameter(part.getName(), jaxbType, block);
/*  860 */           additionalHeaders.add(param);
/*  861 */           request.addHeaderBlock(block);
/*  862 */           request.addParameter(param);
/*  863 */           definitiveParameterList.add(param);
/*      */         }
/*      */       }
/*      */ 
/*  867 */       if ((isRequestResponse) && (outputMessage != null)) {
/*  868 */         Object outParams = new ArrayList();
/*  869 */         for (MessagePart part : getAdditionHeaderParts(this.info.bindingOperation, outputMessage, false)) {
/*  870 */           name = part.getDescriptor();
/*  871 */           JAXBType jaxbType = getJAXBType(part);
/*  872 */           Block block = new Block(name, jaxbType, part);
/*  873 */           Parameter param = ModelerUtils.createParameter(part.getName(), jaxbType, block);
/*  874 */           param.setMode(WebParam.Mode.OUT);
/*  875 */           ((List)outParams).add(param);
/*  876 */           response.addHeaderBlock(block);
/*  877 */           response.addParameter(param);
/*      */         }
/*  879 */         for (Parameter outParam : (List)outParams) {
/*  880 */           for (Parameter inParam : additionalHeaders) {
/*  881 */             if ((inParam.getName().equals(outParam.getName())) && 
/*  882 */               (inParam
/*  882 */               .getBlock().getName().equals(outParam.getBlock().getName())))
/*      */             {
/*  884 */               inParam.setMode(WebParam.Mode.INOUT);
/*  885 */               outParam.setMode(WebParam.Mode.INOUT);
/*  886 */               break;
/*      */             }
/*      */           }
/*  889 */           if (outParam.isOUT()) {
/*  890 */             definitiveParameterList.add(outParam);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  897 */     Set duplicateNames = getDuplicateFaultNames();
/*      */ 
/*  900 */     handleLiteralSOAPFault(response, duplicateNames);
/*  901 */     this.info.operation.setProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.parameterOrder", definitiveParameterList);
/*      */ 
/*  906 */     Binding binding = this.info.port.resolveBinding(this.document);
/*  907 */     PortType portType = binding.resolvePortType(this.document);
/*  908 */     if (isAsync(portType, this.info.portTypeOperation)) {
/*  909 */       addAsyncOperations(this.info.operation, styleAndUse);
/*      */     }
/*      */ 
/*  912 */     return this.info.operation;
/*      */   }
/*      */ 
/*      */   private boolean validateParameterName(List<Parameter> params)
/*      */   {
/*  917 */     if (this.options.isExtensionMode()) {
/*  918 */       return true;
/*      */     }
/*      */ 
/*  921 */     Message msg = getInputMessage();
/*  922 */     for (Iterator localIterator = params.iterator(); localIterator.hasNext(); ) { param = (Parameter)localIterator.next();
/*  923 */       if (!param.isOUT())
/*      */       {
/*  926 */         if (param.getCustomName() != null) {
/*  927 */           if (Names.isJavaReservedWord(param.getCustomName())) {
/*  928 */             error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(this.info.operation.getName(), param.getCustomName()));
/*  929 */             return false;
/*      */           }
/*  931 */           return true;
/*      */         }
/*      */ 
/*  934 */         if ((param.isEmbedded()) && (!(param.getBlock().getType() instanceof RpcLitStructure))) {
/*  935 */           if (Names.isJavaReservedWord(param.getName())) {
/*  936 */             error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(this.info.operation.getName(), param.getName(), param.getBlock().getName()));
/*  937 */             return false;
/*      */           }
/*      */ 
/*      */         }
/*  941 */         else if (Names.isJavaReservedWord(param.getName())) {
/*  942 */           error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(this.info.operation.getName(), msg.getName(), param.getName()));
/*  943 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     Parameter param;
/*  948 */     boolean isRequestResponse = this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE;
/*  949 */     if (isRequestResponse) {
/*  950 */       msg = getOutputMessage();
/*  951 */       for (Parameter param : params) {
/*  952 */         if (!param.isIN())
/*      */         {
/*  955 */           if (param.getCustomName() != null) {
/*  956 */             if (Names.isJavaReservedWord(param.getCustomName())) {
/*  957 */               error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(this.info.operation.getName(), param.getCustomName()));
/*  958 */               return false;
/*      */             }
/*  960 */             return true;
/*      */           }
/*      */ 
/*  963 */           if ((param.isEmbedded()) && (!(param.getBlock().getType() instanceof RpcLitStructure))) {
/*  964 */             if (!param.isReturn())
/*      */             {
/*  967 */               if ((!param.getName().equals("return")) && (Names.isJavaReservedWord(param.getName()))) {
/*  968 */                 error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(this.info.operation.getName(), param.getName(), param.getBlock().getName()));
/*  969 */                 return false;
/*      */               }
/*      */             }
/*  972 */           } else if (!param.isReturn())
/*      */           {
/*  977 */             if (Names.isJavaReservedWord(param.getName())) {
/*  978 */               error(param.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(this.info.operation.getName(), msg.getName(), param.getName()));
/*  979 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  985 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean enableMimeContent()
/*      */   {
/*  990 */     JAXWSBinding jaxwsCustomization = (JAXWSBinding)getExtensionOfType(this.info.bindingOperation, JAXWSBinding.class);
/*  991 */     Boolean mimeContentMapping = jaxwsCustomization != null ? jaxwsCustomization.isEnableMimeContentMapping() : null;
/*  992 */     if (mimeContentMapping != null) {
/*  993 */       return mimeContentMapping.booleanValue();
/*      */     }
/*      */ 
/*  997 */     Binding binding = this.info.port.resolveBinding(this.info.document);
/*  998 */     jaxwsCustomization = (JAXWSBinding)getExtensionOfType(binding, JAXWSBinding.class);
/*  999 */     mimeContentMapping = jaxwsCustomization != null ? jaxwsCustomization.isEnableMimeContentMapping() : null;
/* 1000 */     if (mimeContentMapping != null) {
/* 1001 */       return mimeContentMapping.booleanValue();
/*      */     }
/*      */ 
/* 1005 */     jaxwsCustomization = (JAXWSBinding)getExtensionOfType(this.info.document.getDefinitions(), JAXWSBinding.class);
/* 1006 */     mimeContentMapping = jaxwsCustomization != null ? jaxwsCustomization.isEnableMimeContentMapping() : null;
/* 1007 */     if (mimeContentMapping != null) {
/* 1008 */       return mimeContentMapping.booleanValue();
/*      */     }
/* 1010 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean applyOperationNameCustomization() {
/* 1014 */     JAXWSBinding jaxwsCustomization = (JAXWSBinding)getExtensionOfType(this.info.portTypeOperation, JAXWSBinding.class);
/* 1015 */     String operationName = jaxwsCustomization != null ? null : jaxwsCustomization.getMethodName() != null ? jaxwsCustomization.getMethodName().getName() : null;
/* 1016 */     if (operationName != null) {
/* 1017 */       if (Names.isJavaReservedWord(operationName)) {
/* 1018 */         if (this.options.isExtensionMode())
/* 1019 */           warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(this.info.operation.getName(), operationName));
/*      */         else {
/* 1021 */           error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(this.info.operation.getName(), operationName));
/*      */         }
/* 1023 */         return false;
/*      */       }
/*      */ 
/* 1026 */       this.info.operation.setCustomizedName(operationName);
/*      */     }
/*      */ 
/* 1029 */     if (Names.isJavaReservedWord(this.info.operation.getJavaMethodName())) {
/* 1030 */       if (this.options.isExtensionMode())
/* 1031 */         warning(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(this.info.operation.getName()));
/*      */       else {
/* 1033 */         error(this.info.portTypeOperation, ModelerMessages.WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(this.info.operation.getName()));
/*      */       }
/* 1035 */       return false;
/*      */     }
/* 1037 */     return true;
/*      */   }
/*      */ 
/*      */   protected String getAsyncOperationName(com.sun.tools.internal.ws.processor.model.Operation operation) {
/* 1041 */     String name = operation.getCustomizedName();
/* 1042 */     if (name == null) {
/* 1043 */       name = operation.getUniqueName();
/*      */     }
/* 1045 */     return name;
/*      */   }
/*      */ 
/*      */   private void addAsyncOperations(com.sun.tools.internal.ws.processor.model.Operation syncOperation, StyleAndUse styleAndUse)
/*      */   {
/* 1052 */     com.sun.tools.internal.ws.processor.model.Operation operation = createAsyncOperation(syncOperation, styleAndUse, AsyncOperationType.POLLING);
/* 1053 */     if (operation != null) {
/* 1054 */       this.info.modelPort.addOperation(operation);
/*      */     }
/*      */ 
/* 1057 */     operation = createAsyncOperation(syncOperation, styleAndUse, AsyncOperationType.CALLBACK);
/* 1058 */     if (operation != null)
/* 1059 */       this.info.modelPort.addOperation(operation);
/*      */   }
/*      */ 
/*      */   private com.sun.tools.internal.ws.processor.model.Operation createAsyncOperation(com.sun.tools.internal.ws.processor.model.Operation syncOperation, StyleAndUse styleAndUse, AsyncOperationType asyncType)
/*      */   {
/* 1064 */     boolean isRequestResponse = this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE;
/* 1065 */     if (!isRequestResponse) {
/* 1066 */       return null;
/*      */     }
/*      */ 
/* 1070 */     AsyncOperation operation = new AsyncOperation(this.info.operation, this.info.bindingOperation);
/*      */ 
/* 1074 */     if (asyncType.equals(AsyncOperationType.CALLBACK))
/* 1075 */       operation.setUniqueName(this.info.operation.getUniqueName() + "_async_callback");
/* 1076 */     else if (asyncType.equals(AsyncOperationType.POLLING)) {
/* 1077 */       operation.setUniqueName(this.info.operation.getUniqueName() + "_async_polling");
/*      */     }
/*      */ 
/* 1080 */     setDocumentationIfPresent(operation, this.info.portTypeOperation
/* 1082 */       .getDocumentation());
/*      */ 
/* 1084 */     operation.setAsyncType(asyncType);
/* 1085 */     operation.setSOAPAction(this.info.operation.getSOAPAction());
/* 1086 */     boolean unwrappable = this.info.operation.isWrapped();
/* 1087 */     operation.setWrapped(unwrappable);
/* 1088 */     SOAPBody soapRequestBody = getSOAPRequestBody();
/*      */ 
/* 1090 */     Message inputMessage = getInputMessage();
/* 1091 */     Request request = new Request(inputMessage, this.errReceiver);
/*      */ 
/* 1093 */     SOAPBody soapResponseBody = getSOAPResponseBody();
/* 1094 */     Message outputMessage = getOutputMessage();
/* 1095 */     Response response = new Response(outputMessage, this.errReceiver);
/*      */ 
/* 1098 */     List parameterList = getAsynParameterOrder();
/*      */ 
/* 1100 */     List inParameters = null;
/* 1101 */     if (isOperationDocumentLiteral(styleAndUse)) {
/* 1102 */       inParameters = getRequestParameters(request, parameterList);
/*      */ 
/* 1105 */       if (unwrappable) {
/* 1106 */         List unwrappedParameterList = new ArrayList();
/* 1107 */         if (inputMessage != null) {
/* 1108 */           Iterator parts = inputMessage.parts();
/* 1109 */           if (parts.hasNext()) {
/* 1110 */             MessagePart part = (MessagePart)parts.next();
/* 1111 */             JAXBType jaxbType = getJAXBType(part);
/* 1112 */             List memberList = jaxbType.getWrapperChildren();
/* 1113 */             Iterator props = memberList.iterator();
/* 1114 */             while (props.hasNext()) {
/* 1115 */               JAXBProperty prop = (JAXBProperty)props.next();
/* 1116 */               unwrappedParameterList.add(prop.getElementName().getLocalPart());
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1121 */         parameterList.clear();
/* 1122 */         parameterList.addAll(unwrappedParameterList);
/*      */       }
/* 1124 */     } else if (isOperationRpcLiteral(styleAndUse)) {
/* 1125 */       String operationName = this.info.bindingOperation.getName();
/* 1126 */       Block reqBlock = null;
/* 1127 */       if (inputMessage != null) {
/* 1128 */         QName name = new QName(getRequestNamespaceURI(soapRequestBody), operationName);
/* 1129 */         RpcLitStructure rpcStruct = new RpcLitStructure(name, getJAXBModelBuilder().getJAXBModel());
/* 1130 */         rpcStruct.setJavaType(new JavaSimpleType("com.sun.xml.internal.ws.encoding.jaxb.RpcLitPayload", null));
/* 1131 */         reqBlock = new Block(name, rpcStruct, inputMessage);
/* 1132 */         request.addBodyBlock(reqBlock);
/*      */       }
/* 1134 */       inParameters = createRpcLitRequestParameters(request, parameterList, reqBlock);
/*      */     }
/*      */ 
/* 1140 */     Iterator blocks = this.info.operation.getResponse().getBodyBlocks();
/*      */ 
/* 1142 */     while (blocks.hasNext()) {
/* 1143 */       response.addBodyBlock((Block)blocks.next());
/*      */     }
/*      */ 
/* 1146 */     blocks = this.info.operation.getResponse().getHeaderBlocks();
/* 1147 */     while (blocks.hasNext()) {
/* 1148 */       response.addHeaderBlock((Block)blocks.next());
/*      */     }
/*      */ 
/* 1151 */     blocks = this.info.operation.getResponse().getAttachmentBlocks();
/* 1152 */     while (blocks.hasNext()) {
/* 1153 */       response.addAttachmentBlock((Block)blocks.next());
/*      */     }
/*      */ 
/* 1156 */     List outputParts = outputMessage.getParts();
/*      */ 
/* 1159 */     int numOfOutMsgParts = outputParts.size();
/*      */ 
/* 1161 */     if (numOfOutMsgParts == 1) {
/* 1162 */       MessagePart part = (MessagePart)outputParts.get(0);
/* 1163 */       if (isOperationDocumentLiteral(styleAndUse)) {
/* 1164 */         JAXBType type = getJAXBType(part);
/* 1165 */         operation.setResponseBean(type);
/* 1166 */       } else if (isOperationRpcLiteral(styleAndUse)) {
/* 1167 */         String operationName = this.info.bindingOperation.getName();
/* 1168 */         Block resBlock = (Block)this.info.operation.getResponse().getBodyBlocksMap().get(new QName(getResponseNamespaceURI(soapResponseBody), operationName + "Response"));
/*      */ 
/* 1171 */         RpcLitStructure resBean = (RpcLitStructure)resBlock.getType();
/* 1172 */         List members = resBean.getRpcLitMembers();
/* 1173 */         operation.setResponseBean((AbstractType)members.get(0));
/*      */       }
/*      */     }
/*      */     else {
/* 1177 */       String nspace = "";
/* 1178 */       QName responseBeanName = new QName(nspace, getAsyncOperationName(this.info.operation) + "Response");
/* 1179 */       JAXBType responseBeanType = getJAXBModelBuilder().getJAXBType(responseBeanName);
/* 1180 */       if (responseBeanType == null) {
/* 1181 */         error(this.info.operation.getEntity(), ModelerMessages.WSDLMODELER_RESPONSEBEAN_NOTFOUND(this.info.operation.getName()));
/*      */       }
/* 1183 */       operation.setResponseBean(responseBeanType);
/*      */     }
/*      */ 
/* 1186 */     QName respBeanName = new QName(soapResponseBody.getNamespace(), getAsyncOperationName(this.info.operation) + "Response");
/* 1187 */     Block block = new Block(respBeanName, operation.getResponseBeanType(), outputMessage);
/* 1188 */     JavaType respJavaType = operation.getResponseBeanJavaType();
/* 1189 */     JAXBType respType = new JAXBType(respBeanName, respJavaType);
/* 1190 */     Parameter respParam = ModelerUtils.createParameter(this.info.operation.getName() + "Response", respType, block);
/* 1191 */     respParam.setParameterIndex(-1);
/* 1192 */     response.addParameter(respParam);
/* 1193 */     operation.setProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter", respParam.getName());
/*      */ 
/* 1196 */     int parameterOrderPosition = 0;
/* 1197 */     for (String name : parameterList) {
/* 1198 */       Parameter inParameter = ModelerUtils.getParameter(name, inParameters);
/* 1199 */       if (inParameter == null) {
/* 1200 */         if (this.options.isExtensionMode())
/* 1201 */           warning(this.info.operation.getEntity(), ModelerMessages.WSDLMODELER_WARNING_IGNORING_OPERATION_PART_NOT_FOUND(this.info.operation.getName().getLocalPart(), name));
/*      */         else {
/* 1203 */           error(this.info.operation.getEntity(), ModelerMessages.WSDLMODELER_ERROR_PART_NOT_FOUND(this.info.operation.getName().getLocalPart(), name));
/*      */         }
/* 1205 */         return null;
/*      */       }
/* 1207 */       request.addParameter(inParameter);
/* 1208 */       inParameter.setParameterIndex(parameterOrderPosition);
/* 1209 */       parameterOrderPosition++;
/*      */     }
/*      */ 
/* 1212 */     operation.setResponse(response);
/*      */ 
/* 1215 */     if (operation.getAsyncType().equals(AsyncOperationType.CALLBACK)) {
/* 1216 */       JavaType cbJavaType = operation.getCallBackType();
/* 1217 */       JAXBType callbackType = new JAXBType(respBeanName, cbJavaType);
/* 1218 */       Parameter cbParam = ModelerUtils.createParameter("asyncHandler", callbackType, block);
/* 1219 */       request.addParameter(cbParam);
/*      */     }
/*      */ 
/* 1222 */     operation.setRequest(request);
/*      */ 
/* 1224 */     return operation;
/*      */   }
/*      */ 
/*      */   protected boolean isAsync(PortType portType, com.sun.tools.internal.ws.wsdl.document.Operation wsdlOperation)
/*      */   {
/* 1229 */     JAXWSBinding jaxwsCustomization = (JAXWSBinding)getExtensionOfType(wsdlOperation, JAXWSBinding.class);
/* 1230 */     Boolean isAsync = jaxwsCustomization != null ? jaxwsCustomization.isEnableAsyncMapping() : null;
/*      */ 
/* 1232 */     if (isAsync != null) {
/* 1233 */       return isAsync.booleanValue();
/*      */     }
/*      */ 
/* 1237 */     jaxwsCustomization = (JAXWSBinding)getExtensionOfType(portType, JAXWSBinding.class);
/* 1238 */     isAsync = jaxwsCustomization != null ? jaxwsCustomization.isEnableAsyncMapping() : null;
/* 1239 */     if (isAsync != null) {
/* 1240 */       return isAsync.booleanValue();
/*      */     }
/*      */ 
/* 1244 */     jaxwsCustomization = (JAXWSBinding)getExtensionOfType(this.document.getDefinitions(), JAXWSBinding.class);
/* 1245 */     isAsync = jaxwsCustomization != null ? jaxwsCustomization.isEnableAsyncMapping() : null;
/* 1246 */     if (isAsync != null) {
/* 1247 */       return isAsync.booleanValue();
/*      */     }
/* 1249 */     return false;
/*      */   }
/*      */ 
/*      */   protected void handleLiteralSOAPHeaders(Request request, Response response, Iterator headerParts, Set duplicateNames, @NotNull List<String> definitiveParameterList, boolean processRequest)
/*      */   {
/* 1256 */     int parameterOrderPosition = definitiveParameterList.size();
/* 1257 */     while (headerParts.hasNext()) {
/* 1258 */       MessagePart part = (MessagePart)headerParts.next();
/* 1259 */       QName headerName = part.getDescriptor();
/* 1260 */       JAXBType jaxbType = getJAXBType(part);
/* 1261 */       Block headerBlock = new Block(headerName, jaxbType, part);
/*      */       TWSDLExtensible ext;
/*      */       TWSDLExtensible ext;
/* 1263 */       if (processRequest)
/* 1264 */         ext = this.info.bindingOperation.getInput();
/*      */       else {
/* 1266 */         ext = this.info.bindingOperation.getOutput();
/*      */       }
/* 1268 */       Message headerMessage = getHeaderMessage(part, ext);
/*      */ 
/* 1270 */       if (processRequest)
/* 1271 */         request.addHeaderBlock(headerBlock);
/*      */       else {
/* 1273 */         response.addHeaderBlock(headerBlock);
/*      */       }
/*      */ 
/* 1276 */       Parameter parameter = ModelerUtils.createParameter(part.getName(), jaxbType, headerBlock);
/* 1277 */       parameter.setParameterIndex(parameterOrderPosition);
/* 1278 */       setCustomizedParameterName(this.info.bindingOperation, headerMessage, part, parameter, false);
/* 1279 */       if (processRequest) {
/* 1280 */         request.addParameter(parameter);
/* 1281 */         definitiveParameterList.add(parameter.getName());
/*      */       } else {
/* 1283 */         for (String inParamName : definitiveParameterList) {
/* 1284 */           if (inParamName.equals(parameter.getName())) {
/* 1285 */             Parameter inParam = request.getParameterByName(inParamName);
/* 1286 */             parameter.setLinkedParameter(inParam);
/* 1287 */             inParam.setLinkedParameter(parameter);
/*      */ 
/* 1289 */             parameter.setParameterIndex(inParam.getParameterIndex());
/*      */           }
/*      */         }
/* 1292 */         if (!definitiveParameterList.contains(parameter.getName())) {
/* 1293 */           definitiveParameterList.add(parameter.getName());
/*      */         }
/* 1295 */         response.addParameter(parameter);
/*      */       }
/* 1297 */       parameterOrderPosition++;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void handleLiteralSOAPFault(Response response, Set duplicateNames)
/*      */   {
/* 1303 */     for (BindingFault bindingFault : this.info.bindingOperation.faults()) {
/* 1304 */       com.sun.tools.internal.ws.wsdl.document.Fault portTypeFault = null;
/* 1305 */       for (com.sun.tools.internal.ws.wsdl.document.Fault aFault : this.info.portTypeOperation.faults()) {
/* 1306 */         if (aFault.getName().equals(bindingFault.getName())) {
/* 1307 */           if (portTypeFault != null)
/*      */           {
/* 1309 */             error(portTypeFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(bindingFault.getName(), this.info.bindingOperation.getName()));
/*      */           }
/* 1311 */           portTypeFault = aFault;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1316 */       if (portTypeFault == null) {
/* 1317 */         error(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(bindingFault.getName(), this.info.bindingOperation.getName()));
/*      */       }
/*      */     }
/*      */ 
/* 1321 */     for (com.sun.tools.internal.ws.wsdl.document.Fault portTypeFault : this.info.portTypeOperation.faults())
/*      */     {
/* 1323 */       BindingFault bindingFault = null;
/* 1324 */       for (BindingFault bFault : this.info.bindingOperation.faults()) {
/* 1325 */         if (bFault.getName().equals(portTypeFault.getName())) {
/* 1326 */           bindingFault = bFault;
/*      */         }
/*      */       }
/*      */ 
/* 1330 */       if (bindingFault == null) {
/* 1331 */         warning(portTypeFault, ModelerMessages.WSDLMODELER_INVALID_PORT_TYPE_FAULT_NOT_FOUND(portTypeFault.getName(), this.info.portTypeOperation.getName()));
/*      */       }
/*      */ 
/* 1334 */       String faultName = getFaultClassName(portTypeFault);
/* 1335 */       com.sun.tools.internal.ws.processor.model.Fault fault = new com.sun.tools.internal.ws.processor.model.Fault(faultName, portTypeFault);
/* 1336 */       fault.setWsdlFaultName(portTypeFault.getName());
/* 1337 */       setDocumentationIfPresent(fault, portTypeFault.getDocumentation());
/* 1338 */       if (bindingFault != null)
/*      */       {
/* 1340 */         SOAPFault soapFault = (SOAPFault)getExtensionOfType(bindingFault, SOAPFault.class);
/*      */ 
/* 1343 */         if (soapFault == null) {
/* 1344 */           if (this.options.isExtensionMode()) {
/* 1345 */             warning(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation.getName()));
/* 1346 */             soapFault = new SOAPFault(new LocatorImpl());
/*      */           } else {
/* 1348 */             error(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation.getName()));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1353 */         if (!soapFault.isLiteral()) {
/* 1354 */           if (this.options.isExtensionMode())
/* 1355 */             warning(soapFault, ModelerMessages.WSDLMODELER_WARNING_IGNORING_FAULT_NOT_LITERAL(bindingFault.getName(), this.info.bindingOperation.getName()));
/*      */           else {
/* 1357 */             error(soapFault, ModelerMessages.WSDLMODELER_INVALID_OPERATION_FAULT_NOT_LITERAL(bindingFault.getName(), this.info.bindingOperation.getName()));
/*      */           }
/*      */ 
/*      */         }
/* 1363 */         else if (soapFault.getName() == null)
/* 1364 */           warning(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NO_SOAP_FAULT_NAME(bindingFault.getName(), this.info.bindingOperation.getName()));
/* 1365 */         else if (!soapFault.getName().equals(bindingFault.getName()))
/* 1366 */           warning(soapFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_WRONG_SOAP_FAULT_NAME(soapFault.getName(), bindingFault.getName(), this.info.bindingOperation.getName()));
/* 1367 */         else if (soapFault.getNamespace() != null) {
/* 1368 */           warning(soapFault, ModelerMessages.WSDLMODELER_WARNING_R_2716_R_2726("soapbind:fault", soapFault.getName()));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1373 */         Message faultMessage = portTypeFault.resolveMessage(this.info.document);
/* 1374 */         Iterator iter2 = faultMessage.parts();
/* 1375 */         if (!iter2.hasNext())
/*      */         {
/* 1377 */           error(faultMessage, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(portTypeFault.getName(), faultMessage.getName()));
/*      */         }
/* 1379 */         MessagePart faultPart = (MessagePart)iter2.next();
/* 1380 */         QName faultQName = faultPart.getDescriptor();
/*      */ 
/* 1383 */         if (duplicateNames.contains(faultQName)) {
/* 1384 */           warning(faultPart, ModelerMessages.WSDLMODELER_DUPLICATE_FAULT_SOAP_NAME(portTypeFault.getName(), this.info.portTypeOperation.getName(), faultPart.getName()));
/*      */         }
/*      */         else
/*      */         {
/* 1388 */           if (iter2.hasNext())
/*      */           {
/* 1390 */             error(faultMessage, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_MESSAGE_HAS_MORE_THAN_ONE_PART(portTypeFault.getName(), faultMessage.getName()));
/*      */           }
/*      */ 
/* 1393 */           if (faultPart.getDescriptorKind() != SchemaKinds.XSD_ELEMENT) {
/* 1394 */             if (this.options.isExtensionMode())
/* 1395 */               warning(faultPart, ModelerMessages.WSDLMODELER_INVALID_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(faultMessage.getName(), faultPart.getName()));
/*      */             else {
/* 1397 */               error(faultPart, ModelerMessages.WSDLMODELER_INVALID_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(faultMessage.getName(), faultPart.getName()));
/*      */             }
/*      */           }
/*      */ 
/* 1401 */           JAXBType jaxbType = getJAXBType(faultPart);
/*      */ 
/* 1403 */           fault.setElementName(faultPart.getDescriptor());
/* 1404 */           fault.setJavaMemberName(Names.getExceptionClassMemberName());
/*      */ 
/* 1406 */           Block faultBlock = new Block(faultQName, jaxbType, faultPart);
/* 1407 */           fault.setBlock(faultBlock);
/*      */ 
/* 1410 */           if (!response.getFaultBlocksMap().containsKey(faultBlock.getName())) {
/* 1411 */             response.addFaultBlock(faultBlock);
/*      */           }
/* 1413 */           this.info.operation.addFault(fault); } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 1418 */   private String getFaultClassName(com.sun.tools.internal.ws.wsdl.document.Fault portTypeFault) { JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType(portTypeFault, JAXWSBinding.class);
/* 1419 */     if (jaxwsBinding != null) {
/* 1420 */       CustomName className = jaxwsBinding.getClassName();
/* 1421 */       if (className != null) {
/* 1422 */         return makePackageQualified(className.getName());
/*      */       }
/*      */     }
/* 1425 */     return makePackageQualified(BindingHelper.mangleNameToClassName(portTypeFault.getMessage().getLocalPart())); }
/*      */ 
/*      */   protected boolean setMessagePartsBinding(StyleAndUse styleAndUse)
/*      */   {
/* 1429 */     SOAPBody inBody = getSOAPRequestBody();
/* 1430 */     Message inMessage = getInputMessage();
/* 1431 */     if (!setMessagePartsBinding(inBody, inMessage, styleAndUse, true)) {
/* 1432 */       return false;
/*      */     }
/*      */ 
/* 1435 */     if (isRequestResponse()) {
/* 1436 */       SOAPBody outBody = getSOAPResponseBody();
/* 1437 */       Message outMessage = getOutputMessage();
/* 1438 */       if (!setMessagePartsBinding(outBody, outMessage, styleAndUse, false)) {
/* 1439 */         return false;
/*      */       }
/*      */     }
/* 1442 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean setMessagePartsBinding(SOAPBody body, Message message, StyleAndUse styleAndUse, boolean isInput)
/*      */   {
/* 1451 */     List bodyParts = getBodyParts(body, message);
/*      */     List mimeParts;
/*      */     List headerParts;
/*      */     List mimeParts;
/* 1453 */     if (isInput) {
/* 1454 */       List headerParts = getHeaderPartsFromMessage(message, isInput);
/* 1455 */       mimeParts = getMimeContentParts(message, this.info.bindingOperation.getInput());
/*      */     } else {
/* 1457 */       headerParts = getHeaderPartsFromMessage(message, isInput);
/* 1458 */       mimeParts = getMimeContentParts(message, this.info.bindingOperation.getOutput());
/*      */     }
/*      */     Iterator iter;
/* 1468 */     if (bodyParts == null) {
/* 1469 */       bodyParts = new ArrayList();
/* 1470 */       for (iter = message.parts(); iter.hasNext(); ) {
/* 1471 */         MessagePart mPart = (MessagePart)iter.next();
/*      */ 
/* 1474 */         if ((mimeParts.contains(mPart)) || (headerParts.contains(mPart)) || (boundToFault(mPart.getName())))
/*      */         {
/* 1477 */           if (this.options.isExtensionMode())
/* 1478 */             warning(mPart, ModelerMessages.WSDLMODELER_WARNING_BINDING_OPERATION_MULTIPLE_PART_BINDING(this.info.bindingOperation.getName(), mPart.getName()));
/*      */           else {
/* 1480 */             error(mPart, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_PART_BINDING(this.info.bindingOperation.getName(), mPart.getName()));
/*      */           }
/*      */         }
/* 1483 */         bodyParts.add(mPart);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1488 */     for (Iterator iter = message.parts(); iter.hasNext(); ) {
/* 1489 */       MessagePart mPart = (MessagePart)iter.next();
/* 1490 */       if (mimeParts.contains(mPart))
/* 1491 */         mPart.setBindingExtensibilityElementKind(5);
/* 1492 */       else if (headerParts.contains(mPart))
/* 1493 */         mPart.setBindingExtensibilityElementKind(2);
/* 1494 */       else if (bodyParts.contains(mPart))
/* 1495 */         mPart.setBindingExtensibilityElementKind(1);
/*      */       else {
/* 1497 */         mPart.setBindingExtensibilityElementKind(-1);
/*      */       }
/*      */     }
/*      */ 
/* 1501 */     if ((isOperationDocumentLiteral(styleAndUse)) && (bodyParts.size() > 1)) {
/* 1502 */       if (this.options.isExtensionMode())
/* 1503 */         warning(message, ModelerMessages.WSDLMODELER_WARNING_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(this.info.portTypeOperation.getName()));
/*      */       else {
/* 1505 */         error(message, ModelerMessages.WSDLMODELER_INVALID_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(this.info.portTypeOperation.getName()));
/*      */       }
/* 1507 */       return false;
/*      */     }
/* 1509 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean boundToFault(String partName) {
/* 1513 */     for (BindingFault bindingFault : this.info.bindingOperation.faults()) {
/* 1514 */       if (partName.equals(bindingFault.getName())) {
/* 1515 */         return true;
/*      */       }
/*      */     }
/* 1518 */     return false;
/*      */   }
/*      */ 
/*      */   private List<MessagePart> getBodyParts(SOAPBody body, Message message)
/*      */   {
/* 1523 */     String bodyParts = body.getParts();
/* 1524 */     if (bodyParts != null) {
/* 1525 */       List partsList = new ArrayList();
/* 1526 */       StringTokenizer in = new StringTokenizer(bodyParts.trim(), " ");
/* 1527 */       while (in.hasMoreTokens()) {
/* 1528 */         String part = in.nextToken();
/* 1529 */         MessagePart mPart = message.getPart(part);
/* 1530 */         if (null == mPart) {
/* 1531 */           error(message, ModelerMessages.WSDLMODELER_ERROR_PARTS_NOT_FOUND(part, message.getName()));
/*      */         }
/* 1533 */         mPart.setBindingExtensibilityElementKind(1);
/* 1534 */         partsList.add(mPart);
/*      */       }
/* 1536 */       return partsList;
/*      */     }
/* 1538 */     return null;
/*      */   }
/*      */ 
/*      */   List<MessagePart> getAdditionHeaderParts(BindingOperation bindingOperation, Message message, boolean isInput) {
/* 1542 */     List headerParts = new ArrayList();
/* 1543 */     List parts = message.getParts();
/* 1544 */     List headers = getHeaderParts(bindingOperation, isInput);
/*      */ 
/* 1546 */     for (MessagePart part : headers)
/* 1547 */       if (!parts.contains(part))
/*      */       {
/* 1550 */         headerParts.add(part);
/*      */       }
/* 1552 */     return headerParts;
/*      */   }
/*      */ 
/*      */   private List<MessagePart> getHeaderPartsFromMessage(Message message, boolean isInput) {
/* 1556 */     List headerParts = new ArrayList();
/* 1557 */     Iterator parts = message.parts();
/* 1558 */     List headers = getHeaderParts(this.info.bindingOperation, isInput);
/* 1559 */     while (parts.hasNext()) {
/* 1560 */       MessagePart part = (MessagePart)parts.next();
/* 1561 */       if (headers.contains(part)) {
/* 1562 */         headerParts.add(part);
/*      */       }
/*      */     }
/* 1565 */     return headerParts;
/*      */   }
/*      */ 
/*      */   private Message getHeaderMessage(MessagePart part, TWSDLExtensible ext) {
/* 1569 */     Iterator headers = getHeaderExtensions(ext).iterator();
/* 1570 */     while (headers.hasNext()) {
/* 1571 */       SOAPHeader header = (SOAPHeader)headers.next();
/* 1572 */       if (header.isLiteral())
/*      */       {
/* 1575 */         Message headerMessage = findMessage(header.getMessage(), this.document);
/* 1576 */         if (headerMessage != null)
/*      */         {
/* 1580 */           MessagePart headerPart = headerMessage.getPart(header.getPart());
/* 1581 */           if (headerPart == part)
/* 1582 */             return headerMessage; 
/*      */         }
/*      */       }
/*      */     }
/* 1585 */     return null;
/*      */   }
/*      */ 
/*      */   private List<MessagePart> getHeaderParts(BindingOperation bindingOperation, boolean isInput)
/*      */   {
/*      */     TWSDLExtensible ext;
/*      */     TWSDLExtensible ext;
/* 1590 */     if (isInput)
/* 1591 */       ext = bindingOperation.getInput();
/*      */     else {
/* 1593 */       ext = bindingOperation.getOutput();
/*      */     }
/*      */ 
/* 1596 */     List parts = new ArrayList();
/* 1597 */     Iterator headers = getHeaderExtensions(ext).iterator();
/* 1598 */     while (headers.hasNext()) {
/* 1599 */       SOAPHeader header = (SOAPHeader)headers.next();
/* 1600 */       if (!header.isLiteral()) {
/* 1601 */         error(header, ModelerMessages.WSDLMODELER_INVALID_HEADER_NOT_LITERAL(header.getPart(), bindingOperation.getName()));
/*      */       }
/*      */ 
/* 1604 */       if (header.getNamespace() != null) {
/* 1605 */         warning(header, ModelerMessages.WSDLMODELER_WARNING_R_2716_R_2726("soapbind:header", bindingOperation.getName()));
/*      */       }
/* 1607 */       Message headerMessage = findMessage(header.getMessage(), this.document);
/* 1608 */       if (headerMessage == null) {
/* 1609 */         error(header, ModelerMessages.WSDLMODELER_INVALID_HEADER_CANT_RESOLVE_MESSAGE(header.getMessage(), bindingOperation.getName()));
/*      */       }
/*      */ 
/* 1612 */       MessagePart part = headerMessage.getPart(header.getPart());
/* 1613 */       if (part == null) {
/* 1614 */         error(header, ModelerMessages.WSDLMODELER_INVALID_HEADER_NOT_FOUND(header.getPart(), bindingOperation.getName()));
/*      */       }
/* 1616 */       if (part.getDescriptorKind() != SchemaKinds.XSD_ELEMENT) {
/* 1617 */         if (this.options.isExtensionMode())
/* 1618 */           warning(part, ModelerMessages.WSDLMODELER_INVALID_HEADER_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(part.getName(), bindingOperation.getName()));
/*      */         else {
/* 1620 */           error(part, ModelerMessages.WSDLMODELER_INVALID_HEADER_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(part.getName(), bindingOperation.getName()));
/*      */         }
/*      */       }
/* 1623 */       part.setBindingExtensibilityElementKind(2);
/* 1624 */       parts.add(part);
/*      */     }
/* 1626 */     return parts;
/*      */   }
/*      */ 
/*      */   private boolean isOperationDocumentLiteral(StyleAndUse styleAndUse) {
/* 1630 */     return StyleAndUse.DOC_LITERAL == styleAndUse;
/*      */   }
/*      */ 
/*      */   private boolean isOperationRpcLiteral(StyleAndUse styleAndUse) {
/* 1634 */     return StyleAndUse.RPC_LITERAL == styleAndUse;
/*      */   }
/*      */ 
/*      */   private JAXBType getJAXBType(MessagePart part)
/*      */   {
/* 1643 */     QName name = part.getDescriptor();
/*      */     JAXBType type;
/* 1644 */     if (part.getDescriptorKind().equals(SchemaKinds.XSD_ELEMENT)) {
/* 1645 */       JAXBType type = getJAXBModelBuilder().getJAXBType(name);
/* 1646 */       if (type == null)
/* 1647 */         error(part, ModelerMessages.WSDLMODELER_JAXB_JAVATYPE_NOTFOUND(name, part.getName()));
/*      */     }
/*      */     else {
/* 1650 */       S2JJAXBModel jaxbModel = getJAXBModelBuilder().getJAXBModel().getS2JJAXBModel();
/* 1651 */       TypeAndAnnotation typeAnno = jaxbModel.getJavaType(name);
/* 1652 */       if (typeAnno == null) {
/* 1653 */         error(part, ModelerMessages.WSDLMODELER_JAXB_JAVATYPE_NOTFOUND(name, part.getName()));
/*      */       }
/* 1655 */       JavaType javaType = new JavaSimpleType(new JAXBTypeAndAnnotation(typeAnno));
/* 1656 */       type = new JAXBType(new QName("", part.getName()), javaType);
/*      */     }
/* 1658 */     return type;
/*      */   }
/*      */ 
/*      */   private List<Parameter> getDoclitParameters(Request req, Response res, List<MessagePart> parameterList) {
/* 1662 */     if (parameterList.isEmpty()) {
/* 1663 */       return new ArrayList();
/*      */     }
/* 1665 */     List params = new ArrayList();
/* 1666 */     Message inMsg = getInputMessage();
/* 1667 */     Message outMsg = getOutputMessage();
/* 1668 */     boolean unwrappable = isUnwrappable();
/* 1669 */     List outParams = null;
/* 1670 */     int pIndex = 0;
/* 1671 */     for (Iterator localIterator1 = parameterList.iterator(); localIterator1.hasNext(); ) { part = (MessagePart)localIterator1.next();
/* 1672 */       QName reqBodyName = part.getDescriptor();
/* 1673 */       JAXBType jaxbType = getJAXBType(part);
/* 1674 */       Block block = new Block(reqBodyName, jaxbType, part);
/*      */       int index;
/* 1675 */       if (unwrappable)
/*      */       {
/* 1677 */         JAXBStructuredType jaxbStructType = ModelerUtils.createJAXBStructureType(jaxbType);
/* 1678 */         block = new Block(reqBodyName, jaxbStructType, part);
/* 1679 */         if (ModelerUtils.isBoundToSOAPBody(part)) {
/* 1680 */           if (part.isIN()) {
/* 1681 */             req.addBodyBlock(block);
/* 1682 */           } else if (part.isOUT()) {
/* 1683 */             res.addBodyBlock(block);
/* 1684 */           } else if (part.isINOUT()) {
/* 1685 */             req.addBodyBlock(block);
/* 1686 */             res.addBodyBlock(block);
/*      */           }
/* 1688 */         } else if (ModelerUtils.isUnbound(part))
/* 1689 */           if (part.isIN()) {
/* 1690 */             req.addUnboundBlock(block);
/* 1691 */           } else if (part.isOUT()) {
/* 1692 */             res.addUnboundBlock(block);
/* 1693 */           } else if (part.isINOUT()) {
/* 1694 */             req.addUnboundBlock(block);
/* 1695 */             res.addUnboundBlock(block);
/*      */           }
/*      */         WebParam.Mode mode;
/* 1698 */         if ((part.isIN()) || (part.isINOUT())) {
/* 1699 */           params = ModelerUtils.createUnwrappedParameters(jaxbStructType, block);
/* 1700 */           index = 0;
/* 1701 */           mode = part.isINOUT() ? WebParam.Mode.INOUT : WebParam.Mode.IN;
/* 1702 */           for (Parameter param : params) {
/* 1703 */             param.setParameterIndex(index++);
/* 1704 */             param.setMode(mode);
/* 1705 */             setCustomizedParameterName(this.info.portTypeOperation, inMsg, part, param, unwrappable);
/*      */           }
/* 1707 */         } else if (part.isOUT()) {
/* 1708 */           outParams = ModelerUtils.createUnwrappedParameters(jaxbStructType, block);
/* 1709 */           for (Parameter param : outParams) {
/* 1710 */             param.setMode(WebParam.Mode.OUT);
/* 1711 */             setCustomizedParameterName(this.info.portTypeOperation, outMsg, part, param, unwrappable);
/*      */           }
/*      */         }
/*      */       } else {
/* 1715 */         if (ModelerUtils.isBoundToSOAPBody(part)) {
/* 1716 */           if (part.isIN()) {
/* 1717 */             req.addBodyBlock(block);
/* 1718 */           } else if (part.isOUT()) {
/* 1719 */             res.addBodyBlock(block);
/* 1720 */           } else if (part.isINOUT()) {
/* 1721 */             req.addBodyBlock(block);
/* 1722 */             res.addBodyBlock(block);
/*      */           }
/* 1724 */         } else if (ModelerUtils.isBoundToSOAPHeader(part)) {
/* 1725 */           if (part.isIN()) {
/* 1726 */             req.addHeaderBlock(block);
/* 1727 */           } else if (part.isOUT()) {
/* 1728 */             res.addHeaderBlock(block);
/* 1729 */           } else if (part.isINOUT()) {
/* 1730 */             req.addHeaderBlock(block);
/* 1731 */             res.addHeaderBlock(block);
/*      */           }
/* 1733 */         } else if (ModelerUtils.isBoundToMimeContent(part))
/*      */         {
/* 1736 */           if (part.isIN()) {
/* 1737 */             List mimeContents = getMimeContents(this.info.bindingOperation.getInput(), 
/* 1738 */               getInputMessage(), part.getName());
/* 1739 */             jaxbType = getAttachmentType(mimeContents, part);
/* 1740 */             block = new Block(jaxbType.getName(), jaxbType, part);
/* 1741 */             req.addAttachmentBlock(block);
/* 1742 */           } else if (part.isOUT()) {
/* 1743 */             List mimeContents = getMimeContents(this.info.bindingOperation.getOutput(), 
/* 1744 */               getOutputMessage(), part.getName());
/* 1745 */             jaxbType = getAttachmentType(mimeContents, part);
/* 1746 */             block = new Block(jaxbType.getName(), jaxbType, part);
/* 1747 */             res.addAttachmentBlock(block);
/* 1748 */           } else if (part.isINOUT()) {
/* 1749 */             List mimeContents = getMimeContents(this.info.bindingOperation.getInput(), 
/* 1750 */               getInputMessage(), part.getName());
/* 1751 */             jaxbType = getAttachmentType(mimeContents, part);
/* 1752 */             block = new Block(jaxbType.getName(), jaxbType, part);
/* 1753 */             req.addAttachmentBlock(block);
/* 1754 */             res.addAttachmentBlock(block);
/*      */ 
/* 1756 */             mimeContents = getMimeContents(this.info.bindingOperation.getOutput(), 
/* 1757 */               getOutputMessage(), part.getName());
/* 1758 */             JAXBType outJaxbType = getAttachmentType(mimeContents, part);
/*      */ 
/* 1760 */             String inType = jaxbType.getJavaType().getType().getName();
/* 1761 */             String outType = outJaxbType.getJavaType().getType().getName();
/*      */ 
/* 1763 */             TypeAndAnnotation inTa = jaxbType.getJavaType().getType().getTypeAnn();
/* 1764 */             TypeAndAnnotation outTa = outJaxbType.getJavaType().getType().getTypeAnn();
/* 1765 */             if ((inTa != null) && (outTa != null) && (inTa.equals(outTa)) && (!inType.equals(outType))) {
/* 1766 */               String javaType = "javax.activation.DataHandler";
/*      */ 
/* 1770 */               JType jt = this.options.getCodeModel().ref(javaType);
/* 1771 */               JAXBTypeAndAnnotation jaxbTa = jaxbType.getJavaType().getType();
/* 1772 */               jaxbTa.setType(jt);
/*      */             }
/*      */           }
/* 1775 */         } else if (ModelerUtils.isUnbound(part)) {
/* 1776 */           if (part.isIN()) {
/* 1777 */             req.addUnboundBlock(block);
/* 1778 */           } else if (part.isOUT()) {
/* 1779 */             res.addUnboundBlock(block);
/* 1780 */           } else if (part.isINOUT()) {
/* 1781 */             req.addUnboundBlock(block);
/* 1782 */             res.addUnboundBlock(block);
/*      */           }
/*      */         }
/* 1785 */         Parameter param = ModelerUtils.createParameter(part.getName(), jaxbType, block);
/* 1786 */         param.setMode(part.getMode());
/* 1787 */         if (part.isReturn())
/* 1788 */           param.setParameterIndex(-1);
/*      */         else {
/* 1790 */           param.setParameterIndex(pIndex++);
/*      */         }
/*      */ 
/* 1793 */         if (part.isIN())
/* 1794 */           setCustomizedParameterName(this.info.bindingOperation, inMsg, part, param, false);
/* 1795 */         else if (outMsg != null) {
/* 1796 */           setCustomizedParameterName(this.info.bindingOperation, outMsg, part, param, false);
/*      */         }
/*      */ 
/* 1799 */         params.add(param);
/*      */       }
/*      */     }
/*      */     MessagePart part;
/*      */     int index;
/* 1802 */     if ((unwrappable) && (outParams != null)) {
/* 1803 */       index = params.size();
/* 1804 */       for (Parameter param : outParams) {
/* 1805 */         if (BindingHelper.mangleNameToVariableName(param.getName()).equals("return")) {
/* 1806 */           param.setParameterIndex(-1);
/*      */         } else {
/* 1808 */           Parameter inParam = ModelerUtils.getParameter(param.getName(), params);
/* 1809 */           if ((inParam != null) && (inParam.isIN())) {
/* 1810 */             QName inElementName = inParam.getType().getName();
/* 1811 */             QName outElementName = param.getType().getName();
/* 1812 */             String inJavaType = inParam.getTypeName();
/* 1813 */             String outJavaType = param.getTypeName();
/* 1814 */             TypeAndAnnotation inTa = inParam.getType().getJavaType().getType().getTypeAnn();
/* 1815 */             TypeAndAnnotation outTa = param.getType().getJavaType().getType().getTypeAnn();
/* 1816 */             QName inRawTypeName = ModelerUtils.getRawTypeName(inParam);
/* 1817 */             QName outRawTypeName = ModelerUtils.getRawTypeName(param);
/* 1818 */             if ((inElementName.getLocalPart().equals(outElementName.getLocalPart())) && 
/* 1819 */               (inJavaType
/* 1819 */               .equals(outJavaType)) && 
/* 1819 */               ((inTa == null) || (outTa == null) || 
/* 1820 */               (inTa
/* 1820 */               .equals(outTa))) && (
/* 1820 */               (inRawTypeName == null) || (outRawTypeName == null) || 
/* 1821 */               (inRawTypeName
/* 1821 */               .equals(outRawTypeName))))
/*      */             {
/* 1822 */               inParam.setMode(WebParam.Mode.INOUT);
/* 1823 */               continue;
/*      */             }
/*      */           }
/* 1826 */           if (outParams.size() == 1)
/* 1827 */             param.setParameterIndex(-1);
/*      */           else {
/* 1829 */             param.setParameterIndex(index++);
/*      */           }
/*      */         }
/* 1832 */         params.add(param);
/*      */       }
/*      */     }
/* 1835 */     return params;
/*      */   }
/*      */ 
/*      */   private List<Parameter> getRpcLitParameters(Request req, Response res, Block reqBlock, Block resBlock, List<MessagePart> paramList) {
/* 1839 */     List params = new ArrayList();
/* 1840 */     Message inMsg = getInputMessage();
/* 1841 */     Message outMsg = getOutputMessage();
/* 1842 */     S2JJAXBModel jaxbModel = ((RpcLitStructure)reqBlock.getType()).getJaxbModel().getS2JJAXBModel();
/* 1843 */     List inParams = ModelerUtils.createRpcLitParameters(inMsg, reqBlock, jaxbModel, this.errReceiver);
/* 1844 */     List outParams = null;
/* 1845 */     if (outMsg != null) {
/* 1846 */       outParams = ModelerUtils.createRpcLitParameters(outMsg, resBlock, jaxbModel, this.errReceiver);
/*      */     }
/*      */ 
/* 1850 */     int index = 0;
/* 1851 */     for (MessagePart part : paramList) {
/* 1852 */       Parameter param = null;
/* 1853 */       if (ModelerUtils.isBoundToSOAPBody(part)) {
/* 1854 */         if (part.isIN())
/* 1855 */           param = ModelerUtils.getParameter(part.getName(), inParams);
/* 1856 */         else if (outParams != null)
/* 1857 */           param = ModelerUtils.getParameter(part.getName(), outParams);
/*      */       }
/* 1859 */       else if (ModelerUtils.isBoundToSOAPHeader(part)) {
/* 1860 */         QName headerName = part.getDescriptor();
/* 1861 */         JAXBType jaxbType = getJAXBType(part);
/* 1862 */         Block headerBlock = new Block(headerName, jaxbType, part);
/* 1863 */         param = ModelerUtils.createParameter(part.getName(), jaxbType, headerBlock);
/* 1864 */         if (part.isIN()) {
/* 1865 */           req.addHeaderBlock(headerBlock);
/* 1866 */         } else if (part.isOUT()) {
/* 1867 */           res.addHeaderBlock(headerBlock);
/* 1868 */         } else if (part.isINOUT()) {
/* 1869 */           req.addHeaderBlock(headerBlock);
/* 1870 */           res.addHeaderBlock(headerBlock);
/*      */         }
/* 1872 */       } else if (ModelerUtils.isBoundToMimeContent(part))
/*      */       {
/*      */         List mimeContents;
/*      */         List mimeContents;
/* 1874 */         if ((part.isIN()) || (part.isINOUT()))
/* 1875 */           mimeContents = getMimeContents(this.info.bindingOperation.getInput(), 
/* 1876 */             getInputMessage(), part.getName());
/*      */         else {
/* 1878 */           mimeContents = getMimeContents(this.info.bindingOperation.getOutput(), 
/* 1879 */             getOutputMessage(), part.getName());
/*      */         }
/*      */ 
/* 1882 */         JAXBType type = getAttachmentType(mimeContents, part);
/*      */ 
/* 1885 */         Block mimeBlock = new Block(type.getName(), type, part);
/* 1886 */         param = ModelerUtils.createParameter(part.getName(), type, mimeBlock);
/* 1887 */         if (part.isIN()) {
/* 1888 */           req.addAttachmentBlock(mimeBlock);
/* 1889 */         } else if (part.isOUT()) {
/* 1890 */           res.addAttachmentBlock(mimeBlock);
/* 1891 */         } else if (part.isINOUT()) {
/* 1892 */           mimeContents = getMimeContents(this.info.bindingOperation.getOutput(), 
/* 1893 */             getOutputMessage(), part.getName());
/* 1894 */           JAXBType outJaxbType = getAttachmentType(mimeContents, part);
/*      */ 
/* 1896 */           String inType = type.getJavaType().getType().getName();
/* 1897 */           String outType = outJaxbType.getJavaType().getType().getName();
/* 1898 */           if (!inType.equals(outType)) {
/* 1899 */             String javaType = "javax.activation.DataHandler";
/* 1900 */             JType jt = this.options.getCodeModel().ref(javaType);
/* 1901 */             JAXBTypeAndAnnotation jaxbTa = type.getJavaType().getType();
/* 1902 */             jaxbTa.setType(jt);
/*      */           }
/* 1904 */           req.addAttachmentBlock(mimeBlock);
/* 1905 */           res.addAttachmentBlock(mimeBlock);
/*      */         }
/* 1907 */       } else if (ModelerUtils.isUnbound(part)) {
/* 1908 */         QName name = part.getDescriptor();
/* 1909 */         JAXBType type = getJAXBType(part);
/* 1910 */         Block unboundBlock = new Block(name, type, part);
/* 1911 */         if (part.isIN()) {
/* 1912 */           req.addUnboundBlock(unboundBlock);
/* 1913 */         } else if (part.isOUT()) {
/* 1914 */           res.addUnboundBlock(unboundBlock);
/* 1915 */         } else if (part.isINOUT()) {
/* 1916 */           req.addUnboundBlock(unboundBlock);
/* 1917 */           res.addUnboundBlock(unboundBlock);
/*      */         }
/* 1919 */         param = ModelerUtils.createParameter(part.getName(), type, unboundBlock);
/*      */       }
/* 1921 */       if (param != null) {
/* 1922 */         if (part.isReturn())
/* 1923 */           param.setParameterIndex(-1);
/*      */         else {
/* 1925 */           param.setParameterIndex(index++);
/*      */         }
/* 1927 */         param.setMode(part.getMode());
/* 1928 */         params.add(param);
/*      */       }
/*      */     }
/* 1931 */     for (Parameter param : params) {
/* 1932 */       if (param.isIN())
/* 1933 */         setCustomizedParameterName(this.info.portTypeOperation, inMsg, inMsg.getPart(param.getName()), param, false);
/* 1934 */       else if (outMsg != null) {
/* 1935 */         setCustomizedParameterName(this.info.portTypeOperation, outMsg, outMsg.getPart(param.getName()), param, false);
/*      */       }
/*      */     }
/* 1938 */     return params;
/*      */   }
/*      */ 
/*      */   private List<Parameter> getRequestParameters(Request request, List<String> parameterList) {
/* 1942 */     Message inputMessage = getInputMessage();
/*      */ 
/* 1944 */     if ((inputMessage != null) && (!inputMessage.parts().hasNext())) {
/* 1945 */       return new ArrayList();
/*      */     }
/*      */ 
/* 1948 */     List inParameters = null;
/*      */ 
/* 1952 */     boolean unwrappable = isUnwrappable();
/* 1953 */     boolean doneSOAPBody = false;
/*      */ 
/* 1955 */     for (String inParamName : parameterList) {
/* 1956 */       MessagePart part = inputMessage.getPart(inParamName);
/* 1957 */       if (part != null)
/*      */       {
/* 1960 */         QName reqBodyName = part.getDescriptor();
/* 1961 */         JAXBType jaxbReqType = getJAXBType(part);
/* 1962 */         if (unwrappable)
/*      */         {
/* 1964 */           JAXBStructuredType jaxbRequestType = ModelerUtils.createJAXBStructureType(jaxbReqType);
/* 1965 */           Block reqBlock = new Block(reqBodyName, jaxbRequestType, part);
/* 1966 */           if (ModelerUtils.isBoundToSOAPBody(part))
/* 1967 */             request.addBodyBlock(reqBlock);
/* 1968 */           else if (ModelerUtils.isUnbound(part)) {
/* 1969 */             request.addUnboundBlock(reqBlock);
/*      */           }
/* 1971 */           inParameters = ModelerUtils.createUnwrappedParameters(jaxbRequestType, reqBlock);
/* 1972 */           for (Parameter param : inParameters)
/* 1973 */             setCustomizedParameterName(this.info.portTypeOperation, inputMessage, part, param, unwrappable);
/*      */         }
/*      */         else {
/* 1976 */           Block reqBlock = new Block(reqBodyName, jaxbReqType, part);
/* 1977 */           if ((ModelerUtils.isBoundToSOAPBody(part)) && (!doneSOAPBody)) {
/* 1978 */             doneSOAPBody = true;
/* 1979 */             request.addBodyBlock(reqBlock);
/* 1980 */           } else if (ModelerUtils.isBoundToSOAPHeader(part)) {
/* 1981 */             request.addHeaderBlock(reqBlock);
/* 1982 */           } else if (ModelerUtils.isBoundToMimeContent(part)) {
/* 1983 */             List mimeContents = getMimeContents(this.info.bindingOperation.getInput(), 
/* 1984 */               getInputMessage(), part.getName());
/* 1985 */             jaxbReqType = getAttachmentType(mimeContents, part);
/*      */ 
/* 1987 */             reqBlock = new Block(jaxbReqType.getName(), jaxbReqType, part);
/* 1988 */             request.addAttachmentBlock(reqBlock);
/* 1989 */           } else if (ModelerUtils.isUnbound(part)) {
/* 1990 */             request.addUnboundBlock(reqBlock);
/*      */           }
/* 1992 */           if (inParameters == null) {
/* 1993 */             inParameters = new ArrayList();
/*      */           }
/* 1995 */           Parameter param = ModelerUtils.createParameter(part.getName(), jaxbReqType, reqBlock);
/* 1996 */           setCustomizedParameterName(this.info.portTypeOperation, inputMessage, part, param, false);
/* 1997 */           inParameters.add(param);
/*      */         }
/*      */       }
/*      */     }
/* 2000 */     return inParameters;
/*      */   }
/*      */ 
/*      */   private void setCustomizedParameterName(TWSDLExtensible extension, Message msg, MessagePart part, Parameter param, boolean wrapperStyle)
/*      */   {
/* 2009 */     JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType(extension, JAXWSBinding.class);
/* 2010 */     if (jaxwsBinding == null) {
/* 2011 */       return;
/*      */     }
/* 2013 */     String paramName = part.getName();
/* 2014 */     QName elementName = part.getDescriptor();
/* 2015 */     if (wrapperStyle) {
/* 2016 */       elementName = param.getType().getName();
/*      */     }
/* 2018 */     String customName = jaxwsBinding.getParameterName(msg.getName(), paramName, elementName, wrapperStyle);
/* 2019 */     if ((customName != null) && (!customName.equals("")))
/* 2020 */       param.setCustomName(customName);
/*      */   }
/*      */ 
/*      */   protected boolean isConflictingPortClassName(String name)
/*      */   {
/* 2026 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isUnwrappable() {
/* 2030 */     if (!getWrapperStyleCustomization()) {
/* 2031 */       return false;
/*      */     }
/*      */ 
/* 2034 */     Message inputMessage = getInputMessage();
/* 2035 */     Message outputMessage = getOutputMessage();
/*      */ 
/* 2039 */     if (((inputMessage != null) && (inputMessage.numParts() != 1)) || ((outputMessage != null) && 
/* 2040 */       (outputMessage
/* 2040 */       .numParts() != 1))) {
/* 2041 */       return false;
/*      */     }
/*      */ 
/* 2045 */     MessagePart inputPart = inputMessage != null ? 
/* 2045 */       (MessagePart)inputMessage
/* 2045 */       .parts().next() : null;
/*      */ 
/* 2047 */     MessagePart outputPart = outputMessage != null ? 
/* 2047 */       (MessagePart)outputMessage
/* 2047 */       .parts().next() : null;
/* 2048 */     String operationName = this.info.portTypeOperation.getName();
/*      */ 
/* 2053 */     if (((inputPart != null) && (!inputPart.getDescriptor().getLocalPart().equals(operationName))) || ((outputPart != null) && 
/* 2054 */       (outputPart
/* 2054 */       .getDescriptorKind() != SchemaKinds.XSD_ELEMENT))) {
/* 2055 */       return false;
/*      */     }
/*      */ 
/* 2060 */     if (((inputPart != null) && (inputPart.getBindingExtensibilityElementKind() != 1)) || ((outputPart != null) && 
/* 2061 */       (outputPart
/* 2061 */       .getBindingExtensibilityElementKind() != 1))) {
/* 2062 */       return false;
/*      */     }
/*      */ 
/* 2073 */     if (inputPart != null) {
/* 2074 */       boolean inputWrappable = false;
/* 2075 */       JAXBType inputType = getJAXBType(inputPart);
/* 2076 */       if (inputType != null) {
/* 2077 */         inputWrappable = inputType.isUnwrappable();
/*      */       }
/*      */ 
/* 2080 */       if (outputPart == null) {
/* 2081 */         return inputWrappable;
/*      */       }
/* 2083 */       JAXBType outputType = getJAXBType(outputPart);
/* 2084 */       if ((inputType != null) && (outputType != null)) {
/* 2085 */         return (inputType.isUnwrappable()) && (outputType.isUnwrappable());
/*      */       }
/*      */     }
/*      */ 
/* 2089 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean getWrapperStyleCustomization()
/*      */   {
/* 2094 */     com.sun.tools.internal.ws.wsdl.document.Operation portTypeOperation = this.info.portTypeOperation;
/* 2095 */     JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType(portTypeOperation, JAXWSBinding.class);
/* 2096 */     if (jaxwsBinding != null) {
/* 2097 */       Boolean isWrappable = jaxwsBinding.isEnableWrapperStyle();
/* 2098 */       if (isWrappable != null) {
/* 2099 */         return isWrappable.booleanValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2104 */     PortType portType = this.info.port.resolveBinding(this.document).resolvePortType(this.document);
/* 2105 */     jaxwsBinding = (JAXWSBinding)getExtensionOfType(portType, JAXWSBinding.class);
/* 2106 */     if (jaxwsBinding != null) {
/* 2107 */       Boolean isWrappable = jaxwsBinding.isEnableWrapperStyle();
/* 2108 */       if (isWrappable != null) {
/* 2109 */         return isWrappable.booleanValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2114 */     jaxwsBinding = (JAXWSBinding)getExtensionOfType(this.document.getDefinitions(), JAXWSBinding.class);
/* 2115 */     if (jaxwsBinding != null) {
/* 2116 */       Boolean isWrappable = jaxwsBinding.isEnableWrapperStyle();
/* 2117 */       if (isWrappable != null) {
/* 2118 */         return isWrappable.booleanValue();
/*      */       }
/*      */     }
/* 2121 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean isSingleInOutPart(Set inputParameterNames, MessagePart outputPart)
/*      */   {
/* 2131 */     SOAPOperation soapOperation = (SOAPOperation)getExtensionOfType(this.info.bindingOperation, SOAPOperation.class);
/*      */ 
/* 2133 */     if ((soapOperation != null) && ((soapOperation.isDocument()) || (this.info.soapBinding.isDocument()))) {
/* 2134 */       Iterator iter = getInputMessage().parts();
/* 2135 */       while (iter.hasNext()) {
/* 2136 */         MessagePart part = (MessagePart)iter.next();
/* 2137 */         if ((outputPart.getName().equals(part.getName())) && (outputPart.getDescriptor().equals(part.getDescriptor())))
/* 2138 */           return true;
/*      */       }
/*      */     }
/* 2141 */     else if (((soapOperation != null) && (soapOperation.isRPC())) || (this.info.soapBinding.isRPC())) {
/* 2142 */       Message inputMessage = getInputMessage();
/* 2143 */       if ((inputParameterNames.contains(outputPart.getName())) && 
/* 2144 */         (inputMessage.getPart(outputPart.getName()).getDescriptor().equals(outputPart.getDescriptor()))) {
/* 2145 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 2149 */     return false;
/*      */   }
/*      */ 
/*      */   private List<Parameter> createRpcLitRequestParameters(Request request, List<String> parameterList, Block block) {
/* 2153 */     Message message = getInputMessage();
/* 2154 */     S2JJAXBModel jaxbModel = ((RpcLitStructure)block.getType()).getJaxbModel().getS2JJAXBModel();
/* 2155 */     List parameters = ModelerUtils.createRpcLitParameters(message, block, jaxbModel, this.errReceiver);
/*      */ 
/* 2158 */     for (String paramName : parameterList) {
/* 2159 */       MessagePart part = message.getPart(paramName);
/* 2160 */       if (part != null)
/*      */       {
/* 2163 */         if (ModelerUtils.isBoundToSOAPHeader(part)) {
/* 2164 */           if (parameters == null) {
/* 2165 */             parameters = new ArrayList();
/*      */           }
/* 2167 */           QName headerName = part.getDescriptor();
/* 2168 */           JAXBType jaxbType = getJAXBType(part);
/* 2169 */           Block headerBlock = new Block(headerName, jaxbType, part);
/* 2170 */           request.addHeaderBlock(headerBlock);
/* 2171 */           Parameter param = ModelerUtils.createParameter(part.getName(), jaxbType, headerBlock);
/* 2172 */           if (param != null)
/* 2173 */             parameters.add(param);
/*      */         }
/* 2175 */         else if (ModelerUtils.isBoundToMimeContent(part)) {
/* 2176 */           if (parameters == null) {
/* 2177 */             parameters = new ArrayList();
/*      */           }
/* 2179 */           List mimeContents = getMimeContents(this.info.bindingOperation.getInput(), 
/* 2180 */             getInputMessage(), paramName);
/*      */ 
/* 2182 */           JAXBType type = getAttachmentType(mimeContents, part);
/*      */ 
/* 2185 */           Block mimeBlock = new Block(type.getName(), type, part);
/* 2186 */           request.addAttachmentBlock(mimeBlock);
/* 2187 */           Parameter param = ModelerUtils.createParameter(part.getName(), type, mimeBlock);
/* 2188 */           if (param != null)
/* 2189 */             parameters.add(param);
/*      */         }
/* 2191 */         else if (ModelerUtils.isUnbound(part)) {
/* 2192 */           if (parameters == null) {
/* 2193 */             parameters = new ArrayList();
/*      */           }
/* 2195 */           QName name = part.getDescriptor();
/* 2196 */           JAXBType type = getJAXBType(part);
/* 2197 */           Block unboundBlock = new Block(name, type, part);
/* 2198 */           request.addUnboundBlock(unboundBlock);
/* 2199 */           Parameter param = ModelerUtils.createParameter(part.getName(), type, unboundBlock);
/* 2200 */           if (param != null)
/* 2201 */             parameters.add(param);
/*      */         }
/*      */       }
/*      */     }
/* 2205 */     for (Parameter param : parameters) {
/* 2206 */       setCustomizedParameterName(this.info.portTypeOperation, message, message.getPart(param.getName()), param, false);
/*      */     }
/* 2208 */     return parameters;
/*      */   }
/*      */ 
/*      */   private String getJavaTypeForMimeType(String mimeType) {
/* 2212 */     if ((mimeType.equals("image/jpeg")) || (mimeType.equals("image/gif")))
/* 2213 */       return "java.awt.Image";
/* 2214 */     if ((mimeType.equals("text/xml")) || (mimeType.equals("application/xml"))) {
/* 2215 */       return "javax.xml.transform.Source";
/*      */     }
/* 2217 */     return "javax.activation.DataHandler";
/*      */   }
/*      */ 
/*      */   private JAXBType getAttachmentType(List<MIMEContent> mimeContents, MessagePart part) {
/* 2221 */     if (!enableMimeContent()) {
/* 2222 */       return getJAXBType(part);
/*      */     }
/*      */ 
/* 2225 */     List mimeTypes = getAlternateMimeTypes(mimeContents);
/*      */     String javaType;
/*      */     String javaType;
/* 2226 */     if (mimeTypes.size() > 1)
/* 2227 */       javaType = "javax.activation.DataHandler";
/*      */     else {
/* 2229 */       javaType = getJavaTypeForMimeType((String)mimeTypes.get(0));
/*      */     }
/*      */ 
/* 2232 */     S2JJAXBModel jaxbModel = getJAXBModelBuilder().getJAXBModel().getS2JJAXBModel();
/* 2233 */     JType jt = this.options.getCodeModel().ref(javaType);
/* 2234 */     QName desc = part.getDescriptor();
/* 2235 */     TypeAndAnnotation typeAnno = null;
/*      */ 
/* 2237 */     if (part.getDescriptorKind() == SchemaKinds.XSD_TYPE) {
/* 2238 */       typeAnno = jaxbModel.getJavaType(desc);
/* 2239 */       desc = new QName("", part.getName());
/* 2240 */     } else if (part.getDescriptorKind() == SchemaKinds.XSD_ELEMENT) {
/* 2241 */       typeAnno = getJAXBModelBuilder().getElementTypeAndAnn(desc);
/* 2242 */       if (typeAnno == null) {
/* 2243 */         error(part, ModelerMessages.WSDLMODELER_JAXB_JAVATYPE_NOTFOUND(part.getDescriptor(), part.getName()));
/*      */       }
/* 2245 */       for (String mimeType : mimeTypes) {
/* 2246 */         if ((!mimeType.equals("text/xml")) && (!mimeType.equals("application/xml")))
/*      */         {
/* 2255 */           warning(part, ModelerMessages.MIMEMODELER_ELEMENT_PART_INVALID_ELEMENT_MIME_TYPE(part.getName(), mimeType));
/*      */         }
/*      */       }
/*      */     }
/* 2259 */     if (typeAnno == null) {
/* 2260 */       error(part, ModelerMessages.WSDLMODELER_JAXB_JAVATYPE_NOTFOUND(desc, part.getName()));
/*      */     }
/*      */ 
/* 2263 */     return new JAXBType(desc, new JavaSimpleType(new JAXBTypeAndAnnotation(typeAnno, jt)), null, 
/* 2263 */       getJAXBModelBuilder().getJAXBModel());
/*      */   }
/*      */ 
/*      */   protected void buildJAXBModel(WSDLDocument wsdlDocument) {
/* 2267 */     JAXBModelBuilder tempJaxbModelBuilder = new JAXBModelBuilder(this.options, this.classNameCollector, this.forest, this.errReceiver);
/*      */ 
/* 2272 */     if (this.explicitDefaultPackage != null)
/* 2273 */       tempJaxbModelBuilder.getJAXBSchemaCompiler().forcePackageName(this.options.defaultPackage);
/*      */     else {
/* 2275 */       this.options.defaultPackage = getJavaPackage();
/*      */     }
/*      */ 
/* 2279 */     List schemas = PseudoSchemaBuilder.build(this, this.options, this.errReceiver);
/* 2280 */     for (InputSource schema : schemas) {
/* 2281 */       tempJaxbModelBuilder.getJAXBSchemaCompiler().parseSchema(schema);
/*      */     }
/* 2283 */     tempJaxbModelBuilder.bind();
/* 2284 */     this.jaxbModelBuilder = tempJaxbModelBuilder;
/*      */   }
/*      */ 
/*      */   protected String getJavaPackage() {
/* 2288 */     String jaxwsPackage = null;
/* 2289 */     JAXWSBinding jaxwsCustomization = (JAXWSBinding)getExtensionOfType(this.document.getDefinitions(), JAXWSBinding.class);
/* 2290 */     if ((jaxwsCustomization != null) && (jaxwsCustomization.getJaxwsPackage() != null)) {
/* 2291 */       jaxwsPackage = jaxwsCustomization.getJaxwsPackage().getName();
/*      */     }
/* 2293 */     if (jaxwsPackage != null) {
/* 2294 */       return jaxwsPackage;
/*      */     }
/* 2296 */     String wsdlUri = this.document.getDefinitions().getTargetNamespaceURI();
/* 2297 */     return XJC.getDefaultPackageName(wsdlUri);
/*      */   }
/*      */ 
/*      */   protected void createJavaInterfaceForProviderPort(com.sun.tools.internal.ws.processor.model.Port port)
/*      */   {
/* 2302 */     String interfaceName = "javax.xml.ws.Provider";
/* 2303 */     JavaInterface intf = new JavaInterface(interfaceName);
/* 2304 */     port.setJavaInterface(intf);
/*      */   }
/*      */ 
/*      */   protected void createJavaInterfaceForPort(com.sun.tools.internal.ws.processor.model.Port port, boolean isProvider) {
/* 2308 */     if (isProvider) {
/* 2309 */       createJavaInterfaceForProviderPort(port);
/* 2310 */       return;
/*      */     }
/* 2312 */     String interfaceName = getJavaNameOfSEI(port);
/*      */ 
/* 2314 */     if (isConflictingPortClassName(interfaceName)) {
/* 2315 */       interfaceName = interfaceName + "_PortType";
/*      */     }
/*      */ 
/* 2318 */     JavaInterface intf = new JavaInterface(interfaceName);
/* 2319 */     for (com.sun.tools.internal.ws.processor.model.Operation operation : port.getOperations()) {
/* 2320 */       createJavaMethodForOperation(port, operation, intf);
/*      */ 
/* 2325 */       for (JavaParameter jParam : operation.getJavaMethod().getParametersList()) {
/* 2326 */         Parameter param = jParam.getParameter();
/* 2327 */         if (param.getCustomName() != null) {
/* 2328 */           jParam.setName(param.getCustomName());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2333 */     port.setJavaInterface(intf);
/*      */   }
/*      */ 
/*      */   protected String getServiceInterfaceName(QName serviceQName, com.sun.tools.internal.ws.wsdl.document.Service wsdlService) {
/* 2337 */     String serviceName = wsdlService.getName();
/* 2338 */     JAXWSBinding jaxwsCust = (JAXWSBinding)getExtensionOfType(wsdlService, JAXWSBinding.class);
/* 2339 */     if ((jaxwsCust != null) && (jaxwsCust.getClassName() != null)) {
/* 2340 */       CustomName name = jaxwsCust.getClassName();
/* 2341 */       if ((name != null) && (!name.getName().equals(""))) {
/* 2342 */         return makePackageQualified(name.getName());
/*      */       }
/*      */     }
/* 2345 */     return makePackageQualified(BindingHelper.mangleNameToClassName(serviceName));
/*      */   }
/*      */ 
/*      */   protected String getJavaNameOfSEI(com.sun.tools.internal.ws.processor.model.Port port)
/*      */   {
/* 2350 */     QName portTypeName = (QName)port
/* 2350 */       .getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/*      */ 
/* 2352 */     PortType pt = (PortType)this.document.find(Kinds.PORT_TYPE, portTypeName);
/*      */ 
/* 2356 */     port.portTypes.put(portTypeName, pt);
/* 2357 */     JAXWSBinding jaxwsCust = (JAXWSBinding)getExtensionOfType(pt, JAXWSBinding.class);
/* 2358 */     if ((jaxwsCust != null) && (jaxwsCust.getClassName() != null)) {
/* 2359 */       CustomName name = jaxwsCust.getClassName();
/* 2360 */       if ((name != null) && (!name.getName().equals("")))
/* 2361 */         return makePackageQualified(name.getName());
/*      */     }
/*      */     String interfaceName;
/*      */     String interfaceName;
/* 2366 */     if (portTypeName != null)
/*      */     {
/* 2369 */       interfaceName = makePackageQualified(BindingHelper.mangleNameToClassName(portTypeName
/* 2369 */         .getLocalPart()));
/*      */     }
/*      */     else
/*      */     {
/* 2373 */       interfaceName = makePackageQualified(BindingHelper.mangleNameToClassName(port
/* 2373 */         .getName().getLocalPart()));
/*      */     }
/* 2375 */     return interfaceName;
/*      */   }
/*      */ 
/*      */   private void createJavaMethodForAsyncOperation(com.sun.tools.internal.ws.processor.model.Port port, com.sun.tools.internal.ws.processor.model.Operation operation, JavaInterface intf)
/*      */   {
/* 2380 */     String candidateName = getJavaNameForOperation(operation);
/* 2381 */     JavaMethod method = new JavaMethod(candidateName, this.options, this.errReceiver);
/*      */ 
/* 2383 */     assert (operation.getRequest() != null);
/* 2384 */     Response response = operation.getResponse();
/*      */ 
/* 2388 */     for (Iterator iter = operation.getRequest().getParameters(); iter.hasNext(); ) {
/* 2389 */       Parameter parameter = (Parameter)iter.next();
/*      */ 
/* 2391 */       if (parameter.getJavaParameter() != null) {
/* 2392 */         error(operation.getEntity(), ModelerMessages.WSDLMODELER_INVALID_OPERATION(operation.getName().getLocalPart()));
/*      */       }
/*      */ 
/* 2395 */       JavaType parameterType = parameter.getType().getJavaType();
/*      */ 
/* 2401 */       JavaParameter javaParameter = new JavaParameter(
/* 2398 */         BindingHelper.mangleNameToVariableName(parameter
/* 2398 */         .getName()), parameterType, parameter, parameter
/* 2401 */         .getLinkedParameter() != null);
/* 2402 */       if (javaParameter.isHolder()) {
/* 2403 */         javaParameter.setHolderName(Holder.class.getName());
/*      */       }
/* 2405 */       method.addParameter(javaParameter);
/* 2406 */       parameter.setJavaParameter(javaParameter);
/*      */     }
/*      */ 
/* 2410 */     if (response != null)
/*      */     {
/* 2412 */       String resultParameterName = (String)operation
/* 2412 */         .getProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter");
/*      */ 
/* 2414 */       Parameter resultParameter = response
/* 2414 */         .getParameterByName(resultParameterName);
/*      */ 
/* 2415 */       JavaType returnType = resultParameter.getType().getJavaType();
/* 2416 */       method.setReturnType(returnType);
/*      */     }
/*      */ 
/* 2419 */     operation.setJavaMethod(method);
/* 2420 */     intf.addMethod(method);
/*      */   }
/*      */ 
/*      */   protected void createJavaMethodForOperation(com.sun.tools.internal.ws.processor.model.Port port, com.sun.tools.internal.ws.processor.model.Operation operation, JavaInterface intf)
/*      */   {
/* 2427 */     if ((operation instanceof AsyncOperation)) {
/* 2428 */       createJavaMethodForAsyncOperation(port, operation, intf);
/* 2429 */       return;
/*      */     }
/* 2431 */     String candidateName = getJavaNameForOperation(operation);
/* 2432 */     JavaMethod method = new JavaMethod(candidateName, this.options, this.errReceiver);
/* 2433 */     Parameter returnParam = (Parameter)operation.getProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter");
/* 2434 */     if (returnParam != null) {
/* 2435 */       JavaType parameterType = returnParam.getType().getJavaType();
/* 2436 */       method.setReturnType(parameterType);
/*      */     } else {
/* 2438 */       method.setReturnType(JavaSimpleTypeCreator.VOID_JAVATYPE);
/*      */     }
/* 2440 */     List parameterOrder = (List)operation.getProperty("com.sun.xml.internal.ws.processor.modeler.wsdl.parameterOrder");
/* 2441 */     for (Parameter param : parameterOrder) {
/* 2442 */       JavaType parameterType = param.getType().getJavaType();
/* 2443 */       String name = param.getCustomName() != null ? param.getCustomName() : param.getName();
/* 2444 */       name = BindingHelper.mangleNameToVariableName(name);
/*      */ 
/* 2447 */       if (Names.isJavaReservedWord(name)) {
/* 2448 */         name = "_" + name;
/*      */       }
/*      */ 
/* 2455 */       JavaParameter javaParameter = new JavaParameter(name, parameterType, param, 
/* 2455 */         (param
/* 2455 */         .isINOUT()) || (param.isOUT()));
/* 2456 */       if (javaParameter.isHolder()) {
/* 2457 */         javaParameter.setHolderName(Holder.class.getName());
/*      */       }
/* 2459 */       method.addParameter(javaParameter);
/* 2460 */       param.setJavaParameter(javaParameter);
/*      */     }
/* 2462 */     operation.setJavaMethod(method);
/* 2463 */     intf.addMethod(method);
/*      */ 
/* 2465 */     String opName = BindingHelper.mangleNameToVariableName(operation.getName().getLocalPart());
/* 2466 */     Iterator iter = operation.getFaults();
/* 2467 */     while ((iter != null) && (iter.hasNext()))
/*      */     {
/* 2469 */       com.sun.tools.internal.ws.processor.model.Fault fault = (com.sun.tools.internal.ws.processor.model.Fault)iter.next();
/* 2470 */       createJavaExceptionFromLiteralType(fault, port, opName);
/*      */     }
/*      */ 
/* 2474 */     for (Iterator iter = operation.getFaults(); iter.hasNext(); ) {
/* 2475 */       com.sun.tools.internal.ws.processor.model.Fault fault = (com.sun.tools.internal.ws.processor.model.Fault)iter.next();
/* 2476 */       JavaException javaException = fault.getJavaException();
/* 2477 */       method.addException(javaException.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean createJavaExceptionFromLiteralType(com.sun.tools.internal.ws.processor.model.Fault fault, com.sun.tools.internal.ws.processor.model.Port port, String operationName)
/*      */   {
/* 2483 */     JAXBType faultType = (JAXBType)fault.getBlock().getType();
/*      */ 
/* 2485 */     String exceptionName = fault.getName();
/*      */ 
/* 2490 */     JAXBStructuredType jaxbStruct = new JAXBStructuredType(new QName(fault
/* 2489 */       .getBlock().getName().getNamespaceURI(), fault
/* 2490 */       .getName()));
/*      */ 
/* 2492 */     QName memberName = fault.getElementName();
/* 2493 */     JAXBElementMember jaxbMember = new JAXBElementMember(memberName, faultType);
/*      */ 
/* 2497 */     String javaMemberName = getLiteralJavaMemberName(fault);
/*      */ 
/* 2500 */     JavaStructureMember javaMember = new JavaStructureMember(javaMemberName, faultType
/* 2500 */       .getJavaType(), jaxbMember);
/*      */ 
/* 2502 */     jaxbMember.setJavaStructureMember(javaMember);
/* 2503 */     javaMember.setReadMethod(Names.getJavaMemberReadMethod(javaMember));
/* 2504 */     javaMember.setInherited(false);
/* 2505 */     jaxbMember.setJavaStructureMember(javaMember);
/* 2506 */     jaxbStruct.add(jaxbMember);
/*      */ 
/* 2508 */     if (isConflictingExceptionClassName(exceptionName)) {
/* 2509 */       exceptionName = exceptionName + "_Exception";
/*      */     }
/*      */ 
/* 2512 */     JavaException existingJavaException = (JavaException)this._javaExceptions.get(exceptionName);
/* 2513 */     if ((existingJavaException != null) && 
/* 2514 */       (existingJavaException.getName().equals(exceptionName)) && (
/* 2515 */       (((JAXBType)existingJavaException.getOwner()).getName().equals(jaxbStruct.getName())) || 
/* 2516 */       (ModelerUtils.isEquivalentLiteralStructures(jaxbStruct, 
/* 2516 */       (JAXBStructuredType)existingJavaException
/* 2516 */       .getOwner()))))
/*      */     {
/* 2518 */       if ((faultType instanceof JAXBStructuredType)) {
/* 2519 */         fault.getBlock().setType((JAXBType)existingJavaException.getOwner());
/*      */       }
/* 2521 */       fault.setJavaException(existingJavaException);
/* 2522 */       return false;
/*      */     }
/*      */ 
/* 2527 */     JavaException javaException = new JavaException(exceptionName, false, jaxbStruct);
/* 2528 */     javaException.add(javaMember);
/* 2529 */     jaxbStruct.setJavaType(javaException);
/*      */ 
/* 2531 */     this._javaExceptions.put(javaException.getName(), javaException);
/*      */ 
/* 2533 */     fault.setJavaException(javaException);
/* 2534 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean isRequestResponse() {
/* 2538 */     return this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE;
/*      */   }
/*      */ 
/*      */   protected List<String> getAsynParameterOrder()
/*      */   {
/* 2543 */     List parameterList = new ArrayList();
/* 2544 */     Message inputMessage = getInputMessage();
/* 2545 */     List inputParts = inputMessage.getParts();
/* 2546 */     for (MessagePart part : inputParts) {
/* 2547 */       parameterList.add(part.getName());
/*      */     }
/* 2549 */     return parameterList;
/*      */   }
/*      */ 
/*      */   protected List<MessagePart> getParameterOrder()
/*      */   {
/* 2554 */     List params = new ArrayList();
/* 2555 */     String parameterOrder = this.info.portTypeOperation.getParameterOrder();
/*      */ 
/* 2557 */     boolean parameterOrderPresent = false;
/*      */     List parameterList;
/* 2558 */     if ((parameterOrder != null) && (!parameterOrder.trim().equals(""))) {
/* 2559 */       List parameterList = XmlUtil.parseTokenList(parameterOrder);
/* 2560 */       parameterOrderPresent = true;
/*      */     } else {
/* 2562 */       parameterList = new ArrayList();
/*      */     }
/* 2564 */     Message inputMessage = getInputMessage();
/* 2565 */     Message outputMessage = getOutputMessage();
/* 2566 */     List outputParts = null;
/* 2567 */     List inputParts = inputMessage.getParts();
/*      */ 
/* 2569 */     for (MessagePart part : inputParts) {
/* 2570 */       part.setMode(WebParam.Mode.IN);
/* 2571 */       part.setReturn(false);
/*      */     }
/* 2573 */     if (isRequestResponse()) {
/* 2574 */       outputParts = outputMessage.getParts();
/* 2575 */       for (MessagePart part : outputParts) {
/* 2576 */         part.setMode(WebParam.Mode.OUT);
/* 2577 */         part.setReturn(false);
/*      */       }
/*      */     }
/*      */     Iterator paramOrders;
/* 2581 */     if (parameterOrderPresent) {
/* 2582 */       boolean validParameterOrder = true;
/* 2583 */       paramOrders = parameterList.iterator();
/*      */ 
/* 2586 */       while (paramOrders.hasNext()) {
/* 2587 */         String param = (String)paramOrders.next();
/* 2588 */         boolean partFound = false;
/* 2589 */         for (MessagePart part : inputParts) {
/* 2590 */           if (param.equals(part.getName())) {
/* 2591 */             partFound = true;
/* 2592 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 2596 */         if (!partFound) {
/* 2597 */           for (MessagePart part : outputParts) {
/* 2598 */             if (param.equals(part.getName())) {
/* 2599 */               partFound = true;
/* 2600 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 2604 */         if (!partFound) {
/* 2605 */           warning(this.info.operation.getEntity(), ModelerMessages.WSDLMODELER_INVALID_PARAMETERORDER_PARAMETER(param, this.info.operation.getName().getLocalPart()));
/* 2606 */           validParameterOrder = false;
/*      */         }
/*      */       }
/*      */ 
/* 2610 */       List inputUnlistedParts = new ArrayList();
/* 2611 */       List outputUnlistedParts = new ArrayList();
/*      */ 
/* 2614 */       if (validParameterOrder) {
/* 2615 */         for (String param : parameterList) {
/* 2616 */           MessagePart part = inputMessage.getPart(param);
/* 2617 */           if (part != null) {
/* 2618 */             params.add(part);
/*      */           }
/* 2621 */           else if (isRequestResponse()) {
/* 2622 */             MessagePart outPart = outputMessage.getPart(param);
/* 2623 */             if (outPart != null) {
/* 2624 */               params.add(outPart);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 2629 */         for (MessagePart part : inputParts) {
/* 2630 */           if (!parameterList.contains(part.getName())) {
/* 2631 */             inputUnlistedParts.add(part);
/*      */           }
/*      */         }
/*      */ 
/* 2635 */         if (isRequestResponse())
/*      */         {
/* 2637 */           for (MessagePart part : outputParts) {
/* 2638 */             if (!parameterList.contains(part.getName())) {
/* 2639 */               MessagePart inPart = inputMessage.getPart(part.getName());
/*      */ 
/* 2641 */               if ((inPart != null) && (inPart.getDescriptor().equals(part.getDescriptor())))
/* 2642 */                 inPart.setMode(WebParam.Mode.INOUT);
/*      */               else
/* 2644 */                 outputUnlistedParts.add(part);
/*      */             }
/*      */             else
/*      */             {
/* 2648 */               MessagePart inPart = inputMessage.getPart(part.getName());
/*      */ 
/* 2650 */               if ((inPart != null) && (inPart.getDescriptor().equals(part.getDescriptor())))
/* 2651 */                 inPart.setMode(WebParam.Mode.INOUT);
/* 2652 */               else if (!params.contains(part)) {
/* 2653 */                 params.add(part);
/*      */               }
/*      */             }
/*      */           }
/* 2657 */           if (outputUnlistedParts.size() == 1) {
/* 2658 */             resultPart = (MessagePart)outputUnlistedParts.get(0);
/* 2659 */             ((MessagePart)resultPart).setReturn(true);
/* 2660 */             params.add(resultPart);
/* 2661 */             outputUnlistedParts.clear();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2666 */         for (Object resultPart = inputUnlistedParts.iterator(); ((Iterator)resultPart).hasNext(); ) { MessagePart part = (MessagePart)((Iterator)resultPart).next();
/* 2667 */           params.add(part);
/*      */         }
/*      */ 
/* 2670 */         for (resultPart = outputUnlistedParts.iterator(); ((Iterator)resultPart).hasNext(); ) { MessagePart part = (MessagePart)((Iterator)resultPart).next();
/* 2671 */           params.add(part);
/*      */         }
/* 2673 */         return params;
/*      */       }
/*      */ 
/* 2677 */       warning(this.info.operation.getEntity(), ModelerMessages.WSDLMODELER_INVALID_PARAMETER_ORDER_INVALID_PARAMETER_ORDER(this.info.operation.getName().getLocalPart()));
/* 2678 */       parameterList.clear();
/*      */     }
/*      */ 
/* 2681 */     Object outParts = new ArrayList();
/*      */ 
/* 2684 */     for (MessagePart part : inputParts) {
/* 2685 */       params.add(part);
/*      */     }
/*      */ 
/* 2688 */     if (isRequestResponse()) {
/* 2689 */       for (MessagePart part : outputParts) {
/* 2690 */         MessagePart inPart = inputMessage.getPart(part.getName());
/* 2691 */         if ((inPart != null) && (part.getDescriptorKind() == inPart.getDescriptorKind()) && 
/* 2692 */           (part
/* 2692 */           .getDescriptor().equals(inPart.getDescriptor()))) {
/* 2693 */           inPart.setMode(WebParam.Mode.INOUT);
/*      */         }
/*      */         else {
/* 2696 */           ((List)outParts).add(part);
/*      */         }
/*      */       }
/*      */ 
/* 2700 */       for (MessagePart part : (List)outParts) {
/* 2701 */         if (((List)outParts).size() == 1) {
/* 2702 */           part.setReturn(true);
/*      */         }
/* 2704 */         params.add(part);
/*      */       }
/*      */     }
/* 2707 */     return params;
/*      */   }
/*      */ 
/*      */   protected String getClassName(com.sun.tools.internal.ws.processor.model.Port port, String suffix)
/*      */   {
/* 2716 */     String prefix = BindingHelper.mangleNameToClassName(port.getName().getLocalPart());
/* 2717 */     return this.options.defaultPackage + "." + prefix + suffix;
/*      */   }
/*      */ 
/*      */   protected boolean isConflictingServiceClassName(String name)
/*      */   {
/* 2722 */     return (conflictsWithSEIClass(name)) || (conflictsWithJAXBClass(name)) || (conflictsWithExceptionClass(name));
/*      */   }
/*      */ 
/*      */   private boolean conflictsWithSEIClass(String name) {
/* 2726 */     Set seiNames = this.classNameCollector.getSeiClassNames();
/* 2727 */     return (seiNames != null) && (seiNames.contains(name));
/*      */   }
/*      */ 
/*      */   private boolean conflictsWithJAXBClass(String name) {
/* 2731 */     Set jaxbNames = this.classNameCollector.getJaxbGeneratedClassNames();
/* 2732 */     return (jaxbNames != null) && (jaxbNames.contains(name));
/*      */   }
/*      */ 
/*      */   private boolean conflictsWithExceptionClass(String name) {
/* 2736 */     Set exceptionNames = this.classNameCollector.getExceptionClassNames();
/* 2737 */     return (exceptionNames != null) && (exceptionNames.contains(name));
/*      */   }
/*      */ 
/*      */   protected boolean isConflictingExceptionClassName(String name)
/*      */   {
/* 2742 */     return (conflictsWithSEIClass(name)) || (conflictsWithJAXBClass(name));
/*      */   }
/*      */ 
/*      */   protected JAXBModelBuilder getJAXBModelBuilder() {
/* 2746 */     return this.jaxbModelBuilder;
/*      */   }
/*      */ 
/*      */   protected boolean validateWSDLBindingStyle(Binding binding)
/*      */   {
/* 2751 */     SOAPBinding soapBinding = (SOAPBinding)getExtensionOfType(binding, SOAPBinding.class);
/*      */ 
/* 2754 */     if (soapBinding == null) {
/* 2755 */       soapBinding = (SOAPBinding)getExtensionOfType(binding, SOAP12Binding.class);
/*      */     }
/* 2757 */     if (soapBinding == null) {
/* 2758 */       return false;
/*      */     }
/*      */ 
/* 2762 */     if (soapBinding.getStyle() == null) {
/* 2763 */       soapBinding.setStyle(SOAPStyle.DOCUMENT);
/*      */     }
/*      */ 
/* 2766 */     SOAPStyle opStyle = soapBinding.getStyle();
/* 2767 */     for (Iterator iter = binding.operations(); iter.hasNext(); )
/*      */     {
/* 2769 */       BindingOperation bindingOperation = (BindingOperation)iter
/* 2769 */         .next();
/*      */ 
/* 2771 */       SOAPOperation soapOperation = (SOAPOperation)getExtensionOfType(bindingOperation, SOAPOperation.class);
/*      */ 
/* 2773 */       if (soapOperation != null) {
/* 2774 */         SOAPStyle currOpStyle = soapOperation.getStyle() != null ? soapOperation.getStyle() : soapBinding.getStyle();
/*      */ 
/* 2776 */         if (!currOpStyle.equals(opStyle)) {
/* 2777 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 2781 */     return true;
/*      */   }
/*      */ 
/*      */   private void applyWrapperStyleCustomization(com.sun.tools.internal.ws.processor.model.Port port, PortType portType)
/*      */   {
/* 2788 */     JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType(portType, JAXWSBinding.class);
/* 2789 */     Boolean wrapperStyle = jaxwsBinding != null ? jaxwsBinding.isEnableWrapperStyle() : null;
/* 2790 */     if (wrapperStyle != null)
/* 2791 */       port.setWrapped(wrapperStyle.booleanValue());
/*      */   }
/*      */ 
/*      */   protected static void setDocumentationIfPresent(ModelObject obj, Documentation documentation)
/*      */   {
/* 2798 */     if ((documentation != null) && (documentation.getContent() != null))
/* 2799 */       obj.setJavaDoc(documentation.getContent());
/*      */   }
/*      */ 
/*      */   protected String getJavaNameForOperation(com.sun.tools.internal.ws.processor.model.Operation operation)
/*      */   {
/* 2804 */     String name = operation.getJavaMethodName();
/* 2805 */     if (Names.isJavaReservedWord(name)) {
/* 2806 */       name = "_" + name;
/*      */     }
/* 2808 */     return name;
/*      */   }
/*      */ 
/*      */   private void reportError(Entity entity, String formattedMsg, Exception nestedException)
/*      */   {
/* 2813 */     Locator locator = entity == null ? null : entity.getLocator();
/*      */ 
/* 2815 */     SAXParseException e = new SAXParseException2(formattedMsg, locator, nestedException);
/*      */ 
/* 2818 */     this.errReceiver.error(e);
/*      */   }
/*      */ 
/*      */   protected static enum StyleAndUse
/*      */   {
/*   93 */     RPC_LITERAL, DOC_LITERAL;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.wsdl.WSDLModeler
 * JD-Core Version:    0.6.2
 */