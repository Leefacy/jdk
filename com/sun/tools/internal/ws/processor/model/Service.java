/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class Service extends ModelObject
/*     */ {
/*     */   private QName name;
/* 117 */   private List<Port> ports = new ArrayList();
/* 118 */   private Map<QName, Port> portsByName = new HashMap();
/*     */   private JavaInterface javaInterface;
/*     */ 
/*     */   public Service(Entity entity)
/*     */   {
/*  41 */     super(entity);
/*     */   }
/*     */ 
/*     */   public Service(QName name, JavaInterface javaInterface, Entity entity) {
/*  45 */     super(entity);
/*  46 */     this.name = name;
/*  47 */     this.javaInterface = javaInterface;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  51 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(QName n) {
/*  55 */     this.name = n;
/*     */   }
/*     */ 
/*     */   public void addPort(Port port) {
/*  59 */     if (this.portsByName.containsKey(port.getName())) {
/*  60 */       throw new ModelException("model.uniqueness", new Object[0]);
/*     */     }
/*  62 */     this.ports.add(port);
/*  63 */     this.portsByName.put(port.getName(), port);
/*     */   }
/*     */ 
/*     */   public Port getPortByName(QName n)
/*     */   {
/*  68 */     if (this.portsByName.size() != this.ports.size()) {
/*  69 */       initializePortsByName();
/*     */     }
/*  71 */     return (Port)this.portsByName.get(n);
/*     */   }
/*     */ 
/*     */   public List<Port> getPorts()
/*     */   {
/*  76 */     return this.ports;
/*     */   }
/*     */ 
/*     */   public void setPorts(List<Port> m)
/*     */   {
/*  81 */     this.ports = m;
/*     */   }
/*     */ 
/*     */   private void initializePortsByName()
/*     */   {
/*  86 */     this.portsByName = new HashMap();
/*     */     Iterator iter;
/*  87 */     if (this.ports != null)
/*  88 */       for (iter = this.ports.iterator(); iter.hasNext(); ) {
/*  89 */         Port port = (Port)iter.next();
/*  90 */         if ((port.getName() != null) && 
/*  91 */           (this.portsByName
/*  91 */           .containsKey(port
/*  91 */           .getName())))
/*     */         {
/*  93 */           throw new ModelException("model.uniqueness", new Object[0]);
/*     */         }
/*  95 */         this.portsByName.put(port.getName(), port);
/*     */       }
/*     */   }
/*     */ 
/*     */   public JavaInterface getJavaIntf()
/*     */   {
/* 101 */     return getJavaInterface();
/*     */   }
/*     */ 
/*     */   public JavaInterface getJavaInterface() {
/* 105 */     return this.javaInterface;
/*     */   }
/*     */ 
/*     */   public void setJavaInterface(JavaInterface i) {
/* 109 */     this.javaInterface = i;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/* 113 */     visitor.visit(this);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Service
 * JD-Core Version:    0.6.2
 */