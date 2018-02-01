/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class Operation extends Entity
/*     */   implements TWSDLOperation
/*     */ {
/*     */   private TWSDLExtensible parent;
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private Input _input;
/*     */   private Output _output;
/*     */   private List<Fault> _faults;
/*     */   private OperationStyle _style;
/*     */   private String _parameterOrder;
/*     */   private String _uniqueKey;
/*     */   private ExtensibilityHelper _helper;
/* 253 */   private final Map<String, JClass> faultClassMap = new HashMap();
/* 254 */   private final Map<String, JClass> unmodifiableFaultClassMap = Collections.unmodifiableMap(this.faultClassMap);
/*     */ 
/*     */   public Operation(Locator locator)
/*     */   {
/*  48 */     super(locator);
/*  49 */     this._faults = new ArrayList();
/*  50 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  54 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(String name) {
/*  58 */     this._name = name;
/*     */   }
/*     */ 
/*     */   public String getUniqueKey() {
/*  62 */     if (this._uniqueKey == null) {
/*  63 */       StringBuffer sb = new StringBuffer();
/*  64 */       sb.append(this._name);
/*  65 */       sb.append(' ');
/*  66 */       if (this._input != null) {
/*  67 */         sb.append(this._input.getName());
/*     */       } else {
/*  69 */         sb.append(this._name);
/*  70 */         if (this._style == OperationStyle.REQUEST_RESPONSE)
/*  71 */           sb.append("Request");
/*  72 */         else if (this._style == OperationStyle.SOLICIT_RESPONSE) {
/*  73 */           sb.append("Response");
/*     */         }
/*     */       }
/*  76 */       sb.append(' ');
/*  77 */       if (this._output != null) {
/*  78 */         sb.append(this._output.getName());
/*     */       } else {
/*  80 */         sb.append(this._name);
/*  81 */         if (this._style == OperationStyle.SOLICIT_RESPONSE)
/*  82 */           sb.append("Solicit");
/*  83 */         else if (this._style == OperationStyle.REQUEST_RESPONSE) {
/*  84 */           sb.append("Response");
/*     */         }
/*     */       }
/*  87 */       this._uniqueKey = sb.toString();
/*     */     }
/*     */ 
/*  90 */     return this._uniqueKey;
/*     */   }
/*     */ 
/*     */   public OperationStyle getStyle() {
/*  94 */     return this._style;
/*     */   }
/*     */ 
/*     */   public void setStyle(OperationStyle s) {
/*  98 */     this._style = s;
/*     */   }
/*     */ 
/*     */   public Input getInput() {
/* 102 */     return this._input;
/*     */   }
/*     */ 
/*     */   public void setInput(Input i) {
/* 106 */     this._input = i;
/*     */   }
/*     */ 
/*     */   public Output getOutput() {
/* 110 */     return this._output;
/*     */   }
/*     */ 
/*     */   public void setOutput(Output o) {
/* 114 */     this._output = o;
/*     */   }
/*     */ 
/*     */   public void addFault(Fault f) {
/* 118 */     this._faults.add(f);
/*     */   }
/*     */ 
/*     */   public Iterable<Fault> faults() {
/* 122 */     return this._faults;
/*     */   }
/*     */ 
/*     */   public String getParameterOrder() {
/* 126 */     return this._parameterOrder;
/*     */   }
/*     */ 
/*     */   public void setParameterOrder(String s) {
/* 130 */     this._parameterOrder = s;
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/* 134 */     return WSDLConstants.QNAME_OPERATION;
/*     */   }
/*     */ 
/*     */   public Documentation getDocumentation() {
/* 138 */     return this._documentation;
/*     */   }
/*     */ 
/*     */   public void setDocumentation(Documentation d) {
/* 142 */     this._documentation = d;
/*     */   }
/*     */ 
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 146 */     super.withAllSubEntitiesDo(action);
/*     */ 
/* 148 */     if (this._input != null) {
/* 149 */       action.perform(this._input);
/*     */     }
/* 151 */     if (this._output != null) {
/* 152 */       action.perform(this._output);
/*     */     }
/* 154 */     for (Fault _fault : this._faults) {
/* 155 */       action.perform(_fault);
/*     */     }
/* 157 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */ 
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 161 */     visitor.preVisit(this);
/* 162 */     if (this._input != null) {
/* 163 */       this._input.accept(visitor);
/*     */     }
/* 165 */     if (this._output != null) {
/* 166 */       this._output.accept(visitor);
/*     */     }
/* 168 */     for (Fault _fault : this._faults) {
/* 169 */       _fault.accept(visitor);
/*     */     }
/* 171 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */   public void validateThis() {
/* 175 */     if (this._name == null) {
/* 176 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/* 178 */     if (this._style == null) {
/* 179 */       failValidation("validation.missingRequiredProperty", "style");
/*     */     }
/*     */ 
/* 183 */     if (this._style == OperationStyle.ONE_WAY) {
/* 184 */       if (this._input == null) {
/* 185 */         failValidation("validation.missingRequiredSubEntity", "input");
/*     */       }
/* 187 */       if (this._output != null) {
/* 188 */         failValidation("validation.invalidSubEntity", "output");
/*     */       }
/* 190 */       if ((this._faults != null) && (this._faults.size() != 0))
/* 191 */         failValidation("validation.invalidSubEntity", "fault");
/*     */     }
/* 193 */     else if ((this._style == OperationStyle.NOTIFICATION) && 
/* 194 */       (this._parameterOrder != null)) {
/* 195 */       failValidation("validation.invalidAttribute", "parameterOrder");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getNameValue()
/*     */   {
/* 201 */     return getName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 205 */     return this.parent.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public QName getWSDLElementName() {
/* 209 */     return getElementName();
/*     */   }
/*     */ 
/*     */   public void addExtension(TWSDLExtension e)
/*     */   {
/* 216 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends TWSDLExtension> extensions()
/*     */   {
/* 224 */     return this._helper.extensions();
/*     */   }
/*     */ 
/*     */   public TWSDLExtensible getParent() {
/* 228 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(TWSDLExtensible parent) {
/* 232 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public Map<String, JClass> getFaults() {
/* 236 */     return this.unmodifiableFaultClassMap;
/*     */   }
/*     */ 
/*     */   public void putFault(String faultName, JClass exception) {
/* 240 */     this.faultClassMap.put(faultName, exception);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.Operation
 * JD-Core Version:    0.6.2
 */