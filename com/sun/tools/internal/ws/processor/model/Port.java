/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Provider;
/*     */ 
/*     */ public class Port extends ModelObject
/*     */ {
/* 168 */   private SOAPStyle _style = null;
/* 169 */   private boolean _isWrapped = true;
/*     */   private String portGetter;
/*     */   private QName _name;
/* 173 */   private List<Operation> _operations = new ArrayList();
/*     */   private JavaInterface _javaInterface;
/*     */   private String _address;
/*     */   private String _serviceImplName;
/* 177 */   private Map<String, Operation> operationsByName = new HashMap();
/* 178 */   public Map<QName, PortType> portTypes = new HashMap();
/*     */ 
/*     */   public Port(Entity entity)
/*     */   {
/*  45 */     super(entity);
/*     */   }
/*     */ 
/*     */   public Port(QName name, Entity entity) {
/*  49 */     super(entity);
/*  50 */     this._name = name;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  54 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(QName n) {
/*  58 */     this._name = n;
/*     */   }
/*     */ 
/*     */   public void addOperation(Operation operation) {
/*  62 */     this._operations.add(operation);
/*  63 */     this.operationsByName.put(operation.getUniqueName(), operation);
/*     */   }
/*     */ 
/*     */   public Operation getOperationByUniqueName(String name) {
/*  67 */     if (this.operationsByName.size() != this._operations.size()) {
/*  68 */       initializeOperationsByName();
/*     */     }
/*  70 */     return (Operation)this.operationsByName.get(name);
/*     */   }
/*     */ 
/*     */   private void initializeOperationsByName() {
/*  74 */     this.operationsByName = new HashMap();
/*  75 */     if (this._operations != null)
/*  76 */       for (Operation operation : this._operations) {
/*  77 */         if ((operation.getUniqueName() != null) && 
/*  78 */           (this.operationsByName
/*  78 */           .containsKey(operation
/*  78 */           .getUniqueName())))
/*     */         {
/*  80 */           throw new ModelException("model.uniqueness", new Object[0]);
/*     */         }
/*  82 */         this.operationsByName.put(operation.getUniqueName(), operation);
/*     */       }
/*     */   }
/*     */ 
/*     */   public List<Operation> getOperations()
/*     */   {
/*  89 */     return this._operations;
/*     */   }
/*     */ 
/*     */   public void setOperations(List<Operation> l)
/*     */   {
/*  94 */     this._operations = l;
/*     */   }
/*     */ 
/*     */   public JavaInterface getJavaInterface() {
/*  98 */     return this._javaInterface;
/*     */   }
/*     */ 
/*     */   public void setJavaInterface(JavaInterface i) {
/* 102 */     this._javaInterface = i;
/*     */   }
/*     */ 
/*     */   public String getAddress() {
/* 106 */     return this._address;
/*     */   }
/*     */ 
/*     */   public void setAddress(String s) {
/* 110 */     this._address = s;
/*     */   }
/*     */ 
/*     */   public String getServiceImplName() {
/* 114 */     return this._serviceImplName;
/*     */   }
/*     */ 
/*     */   public void setServiceImplName(String name) {
/* 118 */     this._serviceImplName = name;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/* 122 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   public boolean isProvider() {
/* 126 */     JavaInterface intf = getJavaInterface();
/* 127 */     if (intf != null) {
/* 128 */       String sei = intf.getName();
/* 129 */       if (sei.equals(Provider.class.getName())) {
/* 130 */         return true;
/*     */       }
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public String getPortGetter()
/*     */   {
/* 142 */     return this.portGetter;
/*     */   }
/*     */ 
/*     */   public void setPortGetter(String portGetterName)
/*     */   {
/* 149 */     this.portGetter = portGetterName;
/*     */   }
/*     */ 
/*     */   public SOAPStyle getStyle() {
/* 153 */     return this._style;
/*     */   }
/*     */ 
/*     */   public void setStyle(SOAPStyle s) {
/* 157 */     this._style = s;
/*     */   }
/*     */ 
/*     */   public boolean isWrapped() {
/* 161 */     return this._isWrapped;
/*     */   }
/*     */ 
/*     */   public void setWrapped(boolean isWrapped) {
/* 165 */     this._isWrapped = isWrapped;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Port
 * JD-Core Version:    0.6.2
 */