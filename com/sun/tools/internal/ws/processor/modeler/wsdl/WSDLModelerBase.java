/*     */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.processor.generator.Names;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaException;
/*     */ import com.sun.tools.internal.ws.processor.modeler.Modeler;
/*     */ import com.sun.tools.internal.ws.resources.ModelerMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingInput;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingOutput;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Input;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Kinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.OperationStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Output;
/*     */ import com.sun.tools.internal.ws.wsdl.document.WSDLDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.CustomName;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEContent;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEMultipartRelated;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEPart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBody;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeader;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GloballyKnown;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.WSDLParser;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public abstract class WSDLModelerBase
/*     */   implements Modeler
/*     */ {
/*     */   protected final ErrorReceiverFilter errReceiver;
/*     */   protected final WsimportOptions options;
/*     */   protected MetadataFinder forest;
/* 712 */   int numPasses = 0;
/*     */   protected static final String OPERATION_HAS_VOID_RETURN_TYPE = "com.sun.xml.internal.ws.processor.modeler.wsdl.operationHasVoidReturnType";
/*     */   protected static final String WSDL_PARAMETER_ORDER = "com.sun.xml.internal.ws.processor.modeler.wsdl.parameterOrder";
/*     */   public static final String WSDL_RESULT_PARAMETER = "com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter";
/*     */   public static final String MESSAGE_HAS_MIME_MULTIPART_RELATED_BINDING = "com.sun.xml.internal.ws.processor.modeler.wsdl.mimeMultipartRelatedBinding";
/*     */   protected ProcessSOAPOperationInfo info;
/*     */   private Set _conflictingClassNames;
/*     */   protected Map<String, JavaException> _javaExceptions;
/*     */   protected Map _faultTypeToStructureMap;
/*     */   protected Map<QName, com.sun.tools.internal.ws.processor.model.Port> _bindingNameToPortMap;
/* 752 */   private final Set<String> reqResNames = new HashSet();
/*     */   protected WSDLParser parser;
/*     */   protected WSDLDocument document;
/* 790 */   protected static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
/*     */ 
/*     */   public WSDLModelerBase(WsimportOptions options, ErrorReceiver receiver, MetadataFinder forest)
/*     */   {
/*  73 */     this.options = options;
/*  74 */     this.errReceiver = new ErrorReceiverFilter(receiver);
/*  75 */     this.forest = forest;
/*     */   }
/*     */ 
/*     */   protected void applyPortMethodCustomization(com.sun.tools.internal.ws.processor.model.Port port, com.sun.tools.internal.ws.wsdl.document.Port wsdlPort)
/*     */   {
/*  84 */     if (isProvider(wsdlPort)) {
/*  85 */       return;
/*     */     }
/*  87 */     JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType(wsdlPort, JAXWSBinding.class);
/*     */ 
/*  89 */     String portMethodName = jaxwsBinding != null ? null : jaxwsBinding.getMethodName() != null ? jaxwsBinding.getMethodName().getName() : null;
/*  90 */     if (portMethodName != null) {
/*  91 */       port.setPortGetter(portMethodName);
/*     */     } else {
/*  93 */       portMethodName = Names.getPortName(port);
/*  94 */       portMethodName = BindingHelper.mangleNameToClassName(portMethodName);
/*  95 */       port.setPortGetter("get" + portMethodName);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isProvider(com.sun.tools.internal.ws.wsdl.document.Port wsdlPort)
/*     */   {
/* 101 */     JAXWSBinding portCustomization = (JAXWSBinding)getExtensionOfType(wsdlPort, JAXWSBinding.class);
/* 102 */     Boolean isProvider = portCustomization != null ? portCustomization.isProvider() : null;
/* 103 */     if (isProvider != null) {
/* 104 */       return isProvider.booleanValue();
/*     */     }
/*     */ 
/* 107 */     JAXWSBinding jaxwsGlobalCustomization = (JAXWSBinding)getExtensionOfType(this.document.getDefinitions(), JAXWSBinding.class);
/* 108 */     isProvider = jaxwsGlobalCustomization != null ? jaxwsGlobalCustomization.isProvider() : null;
/* 109 */     if (isProvider != null) {
/* 110 */       return isProvider.booleanValue();
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   protected SOAPBody getSOAPRequestBody()
/*     */   {
/* 117 */     SOAPBody requestBody = (SOAPBody)getAnyExtensionOfType(this.info.bindingOperation
/* 117 */       .getInput(), SOAPBody.class);
/*     */ 
/* 119 */     if (requestBody == null)
/*     */     {
/* 121 */       error(this.info.bindingOperation.getInput(), ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(this.info.bindingOperation.getName()));
/*     */     }
/* 123 */     return requestBody;
/*     */   }
/*     */ 
/*     */   protected boolean isRequestMimeMultipart() {
/* 127 */     for (TWSDLExtension extension : this.info.bindingOperation.getInput().extensions()) {
/* 128 */       if (extension.getClass().equals(MIMEMultipartRelated.class)) {
/* 129 */         return true;
/*     */       }
/*     */     }
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isResponseMimeMultipart() {
/* 136 */     for (TWSDLExtension extension : this.info.bindingOperation.getOutput().extensions()) {
/* 137 */       if (extension.getClass().equals(MIMEMultipartRelated.class)) {
/* 138 */         return true;
/*     */       }
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   protected SOAPBody getSOAPResponseBody()
/*     */   {
/* 149 */     SOAPBody responseBody = (SOAPBody)getAnyExtensionOfType(this.info.bindingOperation
/* 149 */       .getOutput(), SOAPBody.class);
/*     */ 
/* 151 */     if (responseBody == null)
/*     */     {
/* 153 */       error(this.info.bindingOperation.getOutput(), ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_MISSING_SOAP_BODY(this.info.bindingOperation.getName()));
/*     */     }
/* 155 */     return responseBody;
/*     */   }
/*     */ 
/*     */   protected Message getOutputMessage() {
/* 159 */     if (this.info.portTypeOperation.getOutput() == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     return this.info.portTypeOperation.getOutput().resolveMessage(this.info.document);
/*     */   }
/*     */ 
/*     */   protected Message getInputMessage() {
/* 166 */     return this.info.portTypeOperation.getInput().resolveMessage(this.info.document);
/*     */   }
/*     */ 
/*     */   protected List<MessagePart> getMessageParts(SOAPBody body, Message message, boolean isInput)
/*     */   {
/* 177 */     String bodyParts = body.getParts();
/* 178 */     ArrayList partsList = new ArrayList();
/* 179 */     List parts = new ArrayList();
/*     */     List mimeParts;
/*     */     List mimeParts;
/* 183 */     if (isInput)
/* 184 */       mimeParts = getMimeContentParts(message, this.info.bindingOperation.getInput());
/*     */     else
/* 186 */       mimeParts = getMimeContentParts(message, this.info.bindingOperation.getOutput());
/*     */     StringTokenizer in;
/* 189 */     if (bodyParts != null) {
/* 190 */       in = new StringTokenizer(bodyParts.trim(), " ");
/* 191 */       while (in.hasMoreTokens()) {
/* 192 */         String part = in.nextToken();
/* 193 */         MessagePart mPart = message.getPart(part);
/* 194 */         if (null == mPart) {
/* 195 */           error(message, ModelerMessages.WSDLMODELER_ERROR_PARTS_NOT_FOUND(part, message.getName()));
/*     */         }
/* 197 */         mPart.setBindingExtensibilityElementKind(1);
/* 198 */         partsList.add(mPart);
/*     */       }
/*     */     } else {
/* 201 */       for (MessagePart mPart : message.getParts()) {
/* 202 */         if (!mimeParts.contains(mPart)) {
/* 203 */           mPart.setBindingExtensibilityElementKind(1);
/*     */         }
/* 205 */         partsList.add(mPart);
/*     */       }
/*     */     }
/*     */ 
/* 209 */     for (MessagePart mPart : message.getParts()) {
/* 210 */       if (mimeParts.contains(mPart)) {
/* 211 */         mPart.setBindingExtensibilityElementKind(5);
/* 212 */         parts.add(mPart);
/* 213 */       } else if (partsList.contains(mPart)) {
/* 214 */         mPart.setBindingExtensibilityElementKind(1);
/* 215 */         parts.add(mPart);
/*     */       }
/*     */     }
/*     */ 
/* 219 */     return parts;
/*     */   }
/*     */ 
/*     */   protected List<MessagePart> getMimeContentParts(Message message, TWSDLExtensible ext)
/*     */   {
/* 227 */     ArrayList mimeContentParts = new ArrayList();
/*     */ 
/* 229 */     for (MIMEPart mimePart : getMimeParts(ext)) {
/* 230 */       MessagePart part = getMimeContentPart(message, mimePart);
/* 231 */       if (part != null) {
/* 232 */         mimeContentParts.add(part);
/*     */       }
/*     */     }
/* 235 */     return mimeContentParts;
/*     */   }
/*     */ 
/*     */   protected boolean validateMimeParts(Iterable<MIMEPart> mimeParts)
/*     */   {
/* 242 */     boolean gotRootPart = false;
/* 243 */     List mimeContents = new ArrayList();
/* 244 */     for (MIMEPart mPart : mimeParts) {
/* 245 */       for (TWSDLExtension obj : mPart.extensions()) {
/* 246 */         if ((obj instanceof SOAPBody)) {
/* 247 */           if (gotRootPart) {
/* 248 */             warning(mPart, ModelerMessages.MIMEMODELER_INVALID_MIME_PART_MORE_THAN_ONE_SOAP_BODY(this.info.operation.getName().getLocalPart()));
/* 249 */             return false;
/*     */           }
/* 251 */           gotRootPart = true;
/* 252 */         } else if ((obj instanceof MIMEContent)) {
/* 253 */           mimeContents.add((MIMEContent)obj);
/*     */         }
/*     */       }
/* 256 */       if (!validateMimeContentPartNames(mimeContents)) {
/* 257 */         return false;
/*     */       }
/* 259 */       if (mPart.getName() != null) {
/* 260 */         warning(mPart, ModelerMessages.MIMEMODELER_INVALID_MIME_PART_NAME_NOT_ALLOWED(this.info.portTypeOperation.getName()));
/*     */       }
/*     */     }
/* 263 */     return true;
/*     */   }
/*     */ 
/*     */   private MessagePart getMimeContentPart(Message message, MIMEPart part)
/*     */   {
/* 268 */     Iterator localIterator = getMimeContents(part).iterator(); if (localIterator.hasNext()) { MIMEContent mimeContent = (MIMEContent)localIterator.next();
/* 269 */       String mimeContentPartName = mimeContent.getPart();
/* 270 */       MessagePart mPart = message.getPart(mimeContentPartName);
/*     */ 
/* 272 */       if (null == mPart) {
/* 273 */         error(mimeContent, ModelerMessages.WSDLMODELER_ERROR_PARTS_NOT_FOUND(mimeContentPartName, message.getName()));
/*     */       }
/* 275 */       mPart.setBindingExtensibilityElementKind(5);
/* 276 */       return mPart;
/*     */     }
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */   protected List<String> getAlternateMimeTypes(List<MIMEContent> mimeContents)
/*     */   {
/* 283 */     List mimeTypes = new ArrayList();
/*     */ 
/* 286 */     for (MIMEContent mimeContent : mimeContents) {
/* 287 */       String mimeType = getMimeContentType(mimeContent);
/* 288 */       if (!mimeTypes.contains(mimeType)) {
/* 289 */         mimeTypes.add(mimeType);
/*     */       }
/*     */     }
/* 292 */     return mimeTypes;
/*     */   }
/*     */ 
/*     */   private boolean validateMimeContentPartNames(List<MIMEContent> mimeContents)
/*     */   {
/* 297 */     for (MIMEContent mimeContent : mimeContents)
/*     */     {
/* 299 */       String mimeContnetPart = getMimeContentPartName(mimeContent);
/* 300 */       if (mimeContnetPart == null) {
/* 301 */         warning(mimeContent, ModelerMessages.MIMEMODELER_INVALID_MIME_CONTENT_MISSING_PART_ATTRIBUTE(this.info.operation.getName().getLocalPart()));
/* 302 */         return false;
/*     */       }
/*     */     }
/* 305 */     return true;
/*     */   }
/*     */ 
/*     */   protected Iterable<MIMEPart> getMimeParts(TWSDLExtensible ext)
/*     */   {
/* 310 */     MIMEMultipartRelated multiPartRelated = (MIMEMultipartRelated)getAnyExtensionOfType(ext, MIMEMultipartRelated.class);
/*     */ 
/* 312 */     if (multiPartRelated == null) {
/* 313 */       return Collections.emptyList();
/*     */     }
/* 315 */     return multiPartRelated.getParts();
/*     */   }
/*     */ 
/*     */   protected List<MIMEContent> getMimeContents(MIMEPart part)
/*     */   {
/* 320 */     List mimeContents = new ArrayList();
/* 321 */     for (TWSDLExtension mimeContent : part.extensions()) {
/* 322 */       if ((mimeContent instanceof MIMEContent)) {
/* 323 */         mimeContents.add((MIMEContent)mimeContent);
/*     */       }
/*     */     }
/*     */ 
/* 327 */     return mimeContents;
/*     */   }
/*     */ 
/*     */   private String getMimeContentPartName(MIMEContent mimeContent)
/*     */   {
/* 337 */     return mimeContent.getPart();
/*     */   }
/*     */ 
/*     */   private String getMimeContentType(MIMEContent mimeContent) {
/* 341 */     String mimeType = mimeContent.getType();
/* 342 */     if (mimeType == null) {
/* 343 */       error(mimeContent, ModelerMessages.MIMEMODELER_INVALID_MIME_CONTENT_MISSING_TYPE_ATTRIBUTE(this.info.operation.getName().getLocalPart()));
/*     */     }
/* 345 */     return mimeType;
/*     */   }
/*     */ 
/*     */   protected boolean isStyleAndPartMatch(SOAPOperation soapOperation, MessagePart part)
/*     */   {
/* 360 */     if ((soapOperation != null) && (soapOperation.getStyle() != null)) {
/* 361 */       if (((soapOperation.isDocument()) && 
/* 362 */         (part
/* 362 */         .getDescriptorKind() != SchemaKinds.XSD_ELEMENT)) || (
/* 363 */         (soapOperation
/* 363 */         .isRPC()) && 
/* 364 */         (part
/* 364 */         .getDescriptorKind() != SchemaKinds.XSD_TYPE))) {
/* 365 */         return false;
/*     */       }
/*     */     }
/* 368 */     else if (((this.info.soapBinding.isDocument()) && 
/* 369 */       (part
/* 369 */       .getDescriptorKind() != SchemaKinds.XSD_ELEMENT)) || (
/* 370 */       (this.info.soapBinding
/* 370 */       .isRPC()) && 
/* 371 */       (part
/* 371 */       .getDescriptorKind() != SchemaKinds.XSD_TYPE))) {
/* 372 */       return false;
/*     */     }
/*     */ 
/* 376 */     return true;
/*     */   }
/*     */ 
/*     */   protected String getRequestNamespaceURI(SOAPBody body)
/*     */   {
/* 382 */     String namespaceURI = body.getNamespace();
/* 383 */     if (namespaceURI == null) {
/* 384 */       if (this.options.isExtensionMode()) {
/* 385 */         return this.info.modelPort.getName().getNamespaceURI();
/*     */       }
/*     */ 
/* 389 */       error(body, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_SOAP_BODY_MISSING_NAMESPACE(this.info.bindingOperation.getName()));
/*     */     }
/* 391 */     return namespaceURI;
/*     */   }
/*     */ 
/*     */   protected String getResponseNamespaceURI(SOAPBody body) {
/* 395 */     String namespaceURI = body.getNamespace();
/* 396 */     if (namespaceURI == null) {
/* 397 */       if (this.options.isExtensionMode()) {
/* 398 */         return this.info.modelPort.getName().getNamespaceURI();
/*     */       }
/*     */ 
/* 402 */       error(body, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_SOAP_BODY_MISSING_NAMESPACE(this.info.bindingOperation.getName()));
/*     */     }
/* 404 */     return namespaceURI;
/*     */   }
/*     */ 
/*     */   protected List<SOAPHeader> getHeaderExtensions(TWSDLExtensible extensible)
/*     */   {
/* 411 */     List headerList = new ArrayList();
/* 412 */     for (TWSDLExtension extension : extensible.extensions())
/*     */     {
/*     */       boolean isRootPart;
/* 413 */       if (extension.getClass() == MIMEMultipartRelated.class) {
/* 414 */         for (MIMEPart part : ((MIMEMultipartRelated)extension).getParts()) {
/* 415 */           isRootPart = isRootPart(part);
/* 416 */           for (TWSDLExtension obj : part.extensions())
/* 417 */             if ((obj instanceof SOAPHeader))
/*     */             {
/* 419 */               if (!isRootPart) {
/* 420 */                 warning((Entity)obj, ModelerMessages.MIMEMODELER_WARNING_IGNORINGINVALID_HEADER_PART_NOT_DECLARED_IN_ROOT_PART(this.info.bindingOperation.getName()));
/* 421 */                 return new ArrayList();
/*     */               }
/* 423 */               headerList.add((SOAPHeader)obj);
/*     */             }
/*     */         }
/*     */       }
/* 427 */       else if ((extension instanceof SOAPHeader)) {
/* 428 */         headerList.add((SOAPHeader)extension);
/*     */       }
/*     */     }
/* 431 */     return headerList;
/*     */   }
/*     */ 
/*     */   private boolean isRootPart(MIMEPart part)
/*     */   {
/* 439 */     for (TWSDLExtension twsdlExtension : part.extensions()) {
/* 440 */       if ((twsdlExtension instanceof SOAPBody)) {
/* 441 */         return true;
/*     */       }
/*     */     }
/* 444 */     return false;
/*     */   }
/*     */ 
/*     */   protected Set getDuplicateFaultNames()
/*     */   {
/* 449 */     Set faultNames = new HashSet();
/* 450 */     Set duplicateNames = new HashSet();
/* 451 */     for (BindingFault bindingFault : this.info.bindingOperation.faults()) {
/* 452 */       com.sun.tools.internal.ws.wsdl.document.Fault portTypeFault = null;
/* 453 */       for (com.sun.tools.internal.ws.wsdl.document.Fault aFault : this.info.portTypeOperation.faults()) {
/* 454 */         if (aFault.getName().equals(bindingFault.getName())) {
/* 455 */           if (portTypeFault != null)
/*     */           {
/* 457 */             error(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(bindingFault.getName(), this.info.bindingOperation
/* 458 */               .getName()));
/*     */           }
/* 460 */           else portTypeFault = aFault;
/*     */         }
/*     */       }
/*     */ 
/* 464 */       if (portTypeFault == null)
/*     */       {
/* 466 */         error(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(bindingFault.getName(), this.info.bindingOperation
/* 467 */           .getName()));
/*     */       }
/*     */ 
/* 470 */       SOAPFault soapFault = (SOAPFault)getExtensionOfType(bindingFault, SOAPFault.class);
/*     */ 
/* 471 */       if (soapFault == null)
/*     */       {
/* 473 */         if (this.options.isExtensionMode())
/* 474 */           warning(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation
/* 475 */             .getName()));
/*     */         else {
/* 477 */           error(bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation
/* 478 */             .getName()));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 483 */       Message faultMessage = portTypeFault
/* 483 */         .resolveMessage(this.info.document);
/*     */ 
/* 484 */       if (faultMessage.getParts().isEmpty())
/*     */       {
/* 486 */         error(faultMessage, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(bindingFault.getName(), faultMessage
/* 487 */           .getName()));
/*     */       }
/*     */ 
/* 490 */       if ((!this.options.isExtensionMode()) && (soapFault != null) && (soapFault.getNamespace() != null)) {
/* 491 */         warning(soapFault, ModelerMessages.WSDLMODELER_WARNING_R_2716_R_2726("soapbind:fault", soapFault.getName()));
/*     */       }
/* 493 */       String faultNamespaceURI = (soapFault != null) && (soapFault.getNamespace() != null) ? soapFault.getNamespace() : portTypeFault.getMessage().getNamespaceURI();
/* 494 */       String faultName = faultMessage.getName();
/* 495 */       QName faultQName = new QName(faultNamespaceURI, faultName);
/* 496 */       if (faultNames.contains(faultQName))
/* 497 */         duplicateNames.add(faultQName);
/*     */       else {
/* 499 */         faultNames.add(faultQName);
/*     */       }
/*     */     }
/* 502 */     return duplicateNames;
/*     */   }
/*     */ 
/*     */   protected boolean validateBodyParts(BindingOperation operation)
/*     */   {
/* 512 */     boolean isRequestResponse = this.info.portTypeOperation
/* 512 */       .getStyle() == OperationStyle.REQUEST_RESPONSE;
/*     */ 
/* 514 */     List inputParts = getMessageParts(getSOAPRequestBody(), getInputMessage(), true);
/* 515 */     if (!validateStyleAndPart(operation, inputParts)) {
/* 516 */       return false;
/*     */     }
/*     */ 
/* 519 */     if (isRequestResponse) {
/* 520 */       List outputParts = getMessageParts(getSOAPResponseBody(), getOutputMessage(), false);
/* 521 */       if (!validateStyleAndPart(operation, outputParts)) {
/* 522 */         return false;
/*     */       }
/*     */     }
/* 525 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean validateStyleAndPart(BindingOperation operation, List<MessagePart> parts)
/*     */   {
/* 534 */     SOAPOperation soapOperation = (SOAPOperation)getExtensionOfType(operation, SOAPOperation.class);
/*     */ 
/* 535 */     for (MessagePart part : parts) {
/* 536 */       if ((part.getBindingExtensibilityElementKind() == 1) && 
/* 537 */         (!isStyleAndPartMatch(soapOperation, part))) {
/* 538 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 542 */     return true;
/*     */   }
/*     */ 
/*     */   protected String getLiteralJavaMemberName(com.sun.tools.internal.ws.processor.model.Fault fault)
/*     */   {
/* 548 */     QName memberName = fault.getElementName();
/* 549 */     String javaMemberName = fault.getJavaMemberName();
/* 550 */     if (javaMemberName == null) {
/* 551 */       javaMemberName = memberName.getLocalPart();
/*     */     }
/* 553 */     return javaMemberName;
/*     */   }
/*     */ 
/*     */   protected List<MIMEContent> getMimeContents(TWSDLExtensible ext, Message message, String name)
/*     */   {
/* 563 */     for (MIMEPart mimePart : getMimeParts(ext)) {
/* 564 */       mimeContents = getMimeContents(mimePart);
/* 565 */       for (MIMEContent mimeContent : mimeContents)
/* 566 */         if (mimeContent.getPart().equals(name))
/* 567 */           return mimeContents;
/*     */     }
/*     */     List mimeContents;
/* 571 */     return null;
/*     */   }
/*     */ 
/*     */   protected String makePackageQualified(String s) {
/* 575 */     if (s.indexOf(".") != -1)
/*     */     {
/* 577 */       return s;
/* 578 */     }if ((this.options.defaultPackage != null) && 
/* 579 */       (!this.options.defaultPackage
/* 579 */       .equals("")))
/*     */     {
/* 580 */       return this.options.defaultPackage + "." + s;
/*     */     }
/* 582 */     return s;
/*     */   }
/*     */ 
/*     */   protected String getUniqueName(com.sun.tools.internal.ws.wsdl.document.Operation operation, boolean hasOverloadedOperations)
/*     */   {
/* 591 */     if (hasOverloadedOperations) {
/* 592 */       return operation.getUniqueKey().replace(' ', '_');
/*     */     }
/* 594 */     return operation.getName();
/*     */   }
/*     */ 
/*     */   protected static QName getQNameOf(GloballyKnown entity)
/*     */   {
/* 601 */     return new QName(entity
/* 600 */       .getDefining().getTargetNamespaceURI(), entity
/* 601 */       .getName());
/*     */   }
/*     */ 
/*     */   protected static TWSDLExtension getExtensionOfType(TWSDLExtensible extensible, Class type)
/*     */   {
/* 607 */     for (TWSDLExtension extension : extensible.extensions()) {
/* 608 */       if (extension.getClass().equals(type)) {
/* 609 */         return extension;
/*     */       }
/*     */     }
/*     */ 
/* 613 */     return null;
/*     */   }
/*     */ 
/*     */   protected TWSDLExtension getAnyExtensionOfType(TWSDLExtensible extensible, Class type)
/*     */   {
/* 619 */     if (extensible == null) {
/* 620 */       return null;
/*     */     }
/* 622 */     for (TWSDLExtension extension : extensible.extensions()) {
/* 623 */       if (extension.getClass().equals(type))
/* 624 */         return extension;
/* 625 */       if ((extension.getClass().equals(MIMEMultipartRelated.class)) && (
/* 626 */         (type
/* 626 */         .equals(SOAPBody.class)) || 
/* 626 */         (type.equals(MIMEContent.class)) || 
/* 627 */         (type
/* 627 */         .equals(MIMEPart.class))))
/*     */       {
/* 628 */         for (MIMEPart part : ((MIMEMultipartRelated)extension).getParts())
/*     */         {
/* 630 */           TWSDLExtension extn = getExtensionOfType(part, type);
/* 631 */           if (extn != null) {
/* 632 */             return extn;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 638 */     return null;
/*     */   }
/*     */ 
/*     */   protected static Message findMessage(QName messageName, WSDLDocument document)
/*     */   {
/* 645 */     Message message = null;
/*     */     try
/*     */     {
/* 648 */       message = (Message)document
/* 648 */         .find(Kinds.MESSAGE, messageName);
/*     */     }
/*     */     catch (NoSuchEntityException localNoSuchEntityException)
/*     */     {
/*     */     }
/*     */ 
/* 653 */     return message;
/*     */   }
/*     */ 
/*     */   protected static boolean tokenListContains(String tokenList, String target)
/*     */   {
/* 659 */     if (tokenList == null) {
/* 660 */       return false;
/*     */     }
/*     */ 
/* 663 */     StringTokenizer tokenizer = new StringTokenizer(tokenList, " ");
/* 664 */     while (tokenizer.hasMoreTokens()) {
/* 665 */       String s = tokenizer.nextToken();
/* 666 */       if (target.equals(s)) {
/* 667 */         return true;
/*     */       }
/*     */     }
/* 670 */     return false;
/*     */   }
/*     */ 
/*     */   protected String getUniqueClassName(String className) {
/* 674 */     int cnt = 2;
/* 675 */     String uniqueName = className;
/* 676 */     while (this.reqResNames.contains(uniqueName.toLowerCase(Locale.ENGLISH))) {
/* 677 */       uniqueName = className + cnt;
/* 678 */       cnt++;
/*     */     }
/* 680 */     this.reqResNames.add(uniqueName.toLowerCase(Locale.ENGLISH));
/* 681 */     return uniqueName;
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingClassName(String name) {
/* 685 */     if (this._conflictingClassNames == null) {
/* 686 */       return false;
/*     */     }
/*     */ 
/* 689 */     return this._conflictingClassNames.contains(name);
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingServiceClassName(String name) {
/* 693 */     return isConflictingClassName(name);
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingStubClassName(String name) {
/* 697 */     return isConflictingClassName(name);
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingTieClassName(String name) {
/* 701 */     return isConflictingClassName(name);
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingPortClassName(String name) {
/* 705 */     return isConflictingClassName(name);
/*     */   }
/*     */ 
/*     */   protected boolean isConflictingExceptionClassName(String name) {
/* 709 */     return isConflictingClassName(name);
/*     */   }
/*     */ 
/*     */   protected void warning(Entity entity, String message)
/*     */   {
/* 716 */     if (this.numPasses > 1) {
/* 717 */       return;
/*     */     }
/* 719 */     if (entity == null)
/* 720 */       this.errReceiver.warning(null, message);
/*     */     else
/* 722 */       this.errReceiver.warning(entity.getLocator(), message);
/*     */   }
/*     */ 
/*     */   protected void error(Entity entity, String message)
/*     */   {
/* 727 */     if (entity == null)
/* 728 */       this.errReceiver.error(null, message);
/*     */     else {
/* 730 */       this.errReceiver.error(entity.getLocator(), message);
/*     */     }
/* 732 */     throw new AbortException();
/*     */   }
/*     */ 
/*     */   public static class ProcessSOAPOperationInfo
/*     */   {
/*     */     public com.sun.tools.internal.ws.processor.model.Port modelPort;
/*     */     public com.sun.tools.internal.ws.wsdl.document.Port port;
/*     */     public com.sun.tools.internal.ws.wsdl.document.Operation portTypeOperation;
/*     */     public BindingOperation bindingOperation;
/*     */     public SOAPBinding soapBinding;
/*     */     public WSDLDocument document;
/*     */     public boolean hasOverloadedOperations;
/*     */     public Map headers;
/*     */     public com.sun.tools.internal.ws.processor.model.Operation operation;
/*     */ 
/*     */     public ProcessSOAPOperationInfo(com.sun.tools.internal.ws.processor.model.Port modelPort, com.sun.tools.internal.ws.wsdl.document.Port port, com.sun.tools.internal.ws.wsdl.document.Operation portTypeOperation, BindingOperation bindingOperation, SOAPBinding soapBinding, WSDLDocument document, boolean hasOverloadedOperations, Map headers)
/*     */     {
/* 765 */       this.modelPort = modelPort;
/* 766 */       this.port = port;
/* 767 */       this.portTypeOperation = portTypeOperation;
/* 768 */       this.bindingOperation = bindingOperation;
/* 769 */       this.soapBinding = soapBinding;
/* 770 */       this.document = document;
/* 771 */       this.hasOverloadedOperations = hasOverloadedOperations;
/* 772 */       this.headers = headers;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.wsdl.WSDLModelerBase
 * JD-Core Version:    0.6.2
 */