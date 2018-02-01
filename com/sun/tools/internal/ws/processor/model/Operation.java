/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class Operation extends ModelObject
/*     */ {
/*     */   private String customizedName;
/* 244 */   private boolean _isWrapped = true;
/*     */   private QName _name;
/*     */   private String _uniqueName;
/*     */   private Request _request;
/*     */   private Response _response;
/*     */   private JavaMethod _javaMethod;
/*     */   private String _soapAction;
/* 251 */   private SOAPStyle _style = SOAPStyle.DOCUMENT;
/* 252 */   private SOAPUse _use = SOAPUse.LITERAL;
/*     */   private Set<String> _faultNames;
/*     */   private Set<Fault> _faults;
/*     */   private com.sun.tools.internal.ws.wsdl.document.Operation wsdlOperation;
/*     */ 
/*     */   public Operation(Entity entity)
/*     */   {
/*  46 */     super(entity);
/*     */   }
/*     */ 
/*     */   public Operation(Operation operation, Entity entity) {
/*  50 */     this(operation._name, entity);
/*  51 */     this._style = operation._style;
/*  52 */     this._use = operation._use;
/*  53 */     this.customizedName = operation.customizedName;
/*     */   }
/*     */   public Operation(QName name, Entity entity) {
/*  56 */     super(entity);
/*  57 */     this._name = name;
/*  58 */     this._uniqueName = name.getLocalPart();
/*  59 */     this._faultNames = new HashSet();
/*  60 */     this._faults = new HashSet();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  64 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(QName n) {
/*  68 */     this._name = n;
/*     */   }
/*     */ 
/*     */   public String getUniqueName() {
/*  72 */     return this._uniqueName;
/*     */   }
/*     */ 
/*     */   public void setUniqueName(String s) {
/*  76 */     this._uniqueName = s;
/*     */   }
/*     */ 
/*     */   public Request getRequest() {
/*  80 */     return this._request;
/*     */   }
/*     */ 
/*     */   public void setRequest(Request r) {
/*  84 */     this._request = r;
/*     */   }
/*     */ 
/*     */   public Response getResponse() {
/*  88 */     return this._response;
/*     */   }
/*     */ 
/*     */   public void setResponse(Response r) {
/*  92 */     this._response = r;
/*     */   }
/*     */ 
/*     */   public boolean isOverloaded() {
/*  96 */     return !this._name.getLocalPart().equals(this._uniqueName);
/*     */   }
/*     */ 
/*     */   public void addFault(Fault f) {
/* 100 */     if (this._faultNames.contains(f.getName())) {
/* 101 */       throw new ModelException("model.uniqueness", new Object[0]);
/*     */     }
/* 103 */     this._faultNames.add(f.getName());
/* 104 */     this._faults.add(f);
/*     */   }
/*     */ 
/*     */   public Iterator<Fault> getFaults() {
/* 108 */     return this._faults.iterator();
/*     */   }
/*     */ 
/*     */   public Set<Fault> getFaultsSet() {
/* 112 */     return this._faults;
/*     */   }
/*     */ 
/*     */   public void setFaultsSet(Set<Fault> s)
/*     */   {
/* 117 */     this._faults = s;
/* 118 */     initializeFaultNames();
/*     */   }
/*     */ 
/*     */   private void initializeFaultNames() {
/* 122 */     this._faultNames = new HashSet();
/*     */     Iterator iter;
/* 123 */     if (this._faults != null)
/* 124 */       for (iter = this._faults.iterator(); iter.hasNext(); ) {
/* 125 */         Fault f = (Fault)iter.next();
/* 126 */         if ((f.getName() != null) && (this._faultNames.contains(f.getName()))) {
/* 127 */           throw new ModelException("model.uniqueness", new Object[0]);
/*     */         }
/* 129 */         this._faultNames.add(f.getName());
/*     */       }
/*     */   }
/*     */ 
/*     */   public Iterator<Fault> getAllFaults()
/*     */   {
/* 135 */     Set allFaults = getAllFaultsSet();
/* 136 */     return allFaults.iterator();
/*     */   }
/*     */ 
/*     */   public Set<Fault> getAllFaultsSet() {
/* 140 */     Set transSet = new HashSet();
/* 141 */     transSet.addAll(this._faults);
/* 142 */     Iterator iter = this._faults.iterator();
/*     */ 
/* 145 */     while (iter.hasNext()) {
/* 146 */       Set tmpSet = ((Fault)iter.next()).getAllFaultsSet();
/* 147 */       transSet.addAll(tmpSet);
/*     */     }
/* 149 */     return transSet;
/*     */   }
/*     */ 
/*     */   public int getFaultCount() {
/* 153 */     return this._faults.size();
/*     */   }
/*     */ 
/*     */   public Set<Block> getAllFaultBlocks() {
/* 157 */     Set blocks = new HashSet();
/* 158 */     Iterator faults = this._faults.iterator();
/* 159 */     while (faults.hasNext()) {
/* 160 */       Fault f = (Fault)faults.next();
/* 161 */       blocks.add(f.getBlock());
/*     */     }
/* 163 */     return blocks;
/*     */   }
/*     */ 
/*     */   public JavaMethod getJavaMethod() {
/* 167 */     return this._javaMethod;
/*     */   }
/*     */ 
/*     */   public void setJavaMethod(JavaMethod i) {
/* 171 */     this._javaMethod = i;
/*     */   }
/*     */ 
/*     */   public String getSOAPAction() {
/* 175 */     return this._soapAction;
/*     */   }
/*     */ 
/*     */   public void setSOAPAction(String s) {
/* 179 */     this._soapAction = s;
/*     */   }
/*     */ 
/*     */   public SOAPStyle getStyle() {
/* 183 */     return this._style;
/*     */   }
/*     */ 
/*     */   public void setStyle(SOAPStyle s) {
/* 187 */     this._style = s;
/*     */   }
/*     */ 
/*     */   public SOAPUse getUse() {
/* 191 */     return this._use;
/*     */   }
/*     */ 
/*     */   public void setUse(SOAPUse u) {
/* 195 */     this._use = u;
/*     */   }
/*     */ 
/*     */   public boolean isWrapped() {
/* 199 */     return this._isWrapped;
/*     */   }
/*     */ 
/*     */   public void setWrapped(boolean isWrapped) {
/* 203 */     this._isWrapped = isWrapped;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception
/*     */   {
/* 208 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   public void setCustomizedName(String name) {
/* 212 */     this.customizedName = name;
/*     */   }
/*     */ 
/*     */   public String getCustomizedName() {
/* 216 */     return this.customizedName;
/*     */   }
/*     */ 
/*     */   public String getJavaMethodName()
/*     */   {
/* 221 */     if (this._javaMethod != null) {
/* 222 */       return this._javaMethod.getName();
/*     */     }
/*     */ 
/* 226 */     if (this.customizedName != null) {
/* 227 */       return this.customizedName;
/*     */     }
/*     */ 
/* 230 */     return BindingHelper.mangleNameToVariableName(this._name.getLocalPart());
/*     */   }
/*     */ 
/*     */   public com.sun.tools.internal.ws.wsdl.document.Operation getWSDLPortTypeOperation() {
/* 234 */     return this.wsdlOperation;
/*     */   }
/*     */ 
/*     */   public void setWSDLPortTypeOperation(com.sun.tools.internal.ws.wsdl.document.Operation wsdlOperation) {
/* 238 */     this.wsdlOperation = wsdlOperation;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Operation
 * JD-Core Version:    0.6.2
 */