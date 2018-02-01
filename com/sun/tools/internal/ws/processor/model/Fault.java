/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaException;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class Fault extends ModelObject
/*     */ {
/* 152 */   private boolean wsdlException = true;
/*     */   private String name;
/*     */   private Block block;
/*     */   private JavaException javaException;
/* 156 */   private Set subfaults = new HashSet();
/* 157 */   private QName elementName = null;
/* 158 */   private String javaMemberName = null;
/*     */   private JClass exceptionClass;
/*     */   private String wsdlFaultName;
/*     */ 
/*     */   public Fault(Entity entity)
/*     */   {
/*  44 */     super(entity);
/*     */   }
/*     */ 
/*     */   public Fault(String name, Entity entity) {
/*  48 */     super(entity);
/*  49 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String s) {
/*  57 */     this.name = s;
/*     */   }
/*     */ 
/*     */   public Block getBlock() {
/*  61 */     return this.block;
/*     */   }
/*     */ 
/*     */   public void setBlock(Block b) {
/*  65 */     this.block = b;
/*     */   }
/*     */ 
/*     */   public JavaException getJavaException() {
/*  69 */     return this.javaException;
/*     */   }
/*     */ 
/*     */   public void setJavaException(JavaException e) {
/*  73 */     this.javaException = e;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/*  77 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   public Iterator getSubfaults() {
/*  81 */     if (this.subfaults.isEmpty()) {
/*  82 */       return null;
/*     */     }
/*  84 */     return this.subfaults.iterator();
/*     */   }
/*     */ 
/*     */   public Set getSubfaultsSet()
/*     */   {
/*  89 */     return this.subfaults;
/*     */   }
/*     */ 
/*     */   public void setSubfaultsSet(Set s)
/*     */   {
/*  94 */     this.subfaults = s;
/*     */   }
/*     */ 
/*     */   public Iterator getAllFaults() {
/*  98 */     Set allFaults = getAllFaultsSet();
/*  99 */     if (allFaults.isEmpty()) {
/* 100 */       return null;
/*     */     }
/* 102 */     return allFaults.iterator();
/*     */   }
/*     */ 
/*     */   public Set getAllFaultsSet() {
/* 106 */     Set transSet = new HashSet();
/* 107 */     Iterator iter = this.subfaults.iterator();
/* 108 */     while (iter.hasNext()) {
/* 109 */       transSet.addAll(((Fault)iter.next()).getAllFaultsSet());
/*     */     }
/* 111 */     transSet.addAll(this.subfaults);
/* 112 */     return transSet;
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/* 116 */     return this.elementName;
/*     */   }
/*     */ 
/*     */   public void setElementName(QName elementName) {
/* 120 */     this.elementName = elementName;
/*     */   }
/*     */ 
/*     */   public String getJavaMemberName() {
/* 124 */     return this.javaMemberName;
/*     */   }
/*     */ 
/*     */   public void setJavaMemberName(String javaMemberName) {
/* 128 */     this.javaMemberName = javaMemberName;
/*     */   }
/*     */ 
/*     */   public boolean isWsdlException()
/*     */   {
/* 135 */     return this.wsdlException;
/*     */   }
/*     */ 
/*     */   public void setWsdlException(boolean wsdlFault)
/*     */   {
/* 141 */     this.wsdlException = wsdlFault;
/*     */   }
/*     */ 
/*     */   public void setExceptionClass(JClass ex) {
/* 145 */     this.exceptionClass = ex;
/*     */   }
/*     */ 
/*     */   public JClass getExceptionClass() {
/* 149 */     return this.exceptionClass;
/*     */   }
/*     */ 
/*     */   public String getWsdlFaultName()
/*     */   {
/* 162 */     return this.wsdlFaultName;
/*     */   }
/*     */ 
/*     */   public void setWsdlFaultName(String wsdlFaultName) {
/* 166 */     this.wsdlFaultName = wsdlFaultName;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Fault
 * JD-Core Version:    0.6.2
 */