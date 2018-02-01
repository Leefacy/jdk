/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBModel;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class Model extends ModelObject
/*     */ {
/*     */   private QName name;
/*     */   private String targetNamespace;
/* 153 */   private List<Service> services = new ArrayList();
/* 154 */   private Map<QName, Service> servicesByName = new HashMap();
/* 155 */   private Set<AbstractType> extraTypes = new HashSet();
/*     */   private String source;
/* 157 */   private JAXBModel jaxBModel = null;
/*     */ 
/*     */   public Model(Entity entity)
/*     */   {
/*  43 */     super(entity);
/*     */   }
/*     */ 
/*     */   public Model(QName name, Entity entity) {
/*  47 */     super(entity);
/*  48 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  52 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(QName n) {
/*  56 */     this.name = n;
/*     */   }
/*     */ 
/*     */   public String getTargetNamespaceURI() {
/*  60 */     return this.targetNamespace;
/*     */   }
/*     */ 
/*     */   public void setTargetNamespaceURI(String s) {
/*  64 */     this.targetNamespace = s;
/*     */   }
/*     */ 
/*     */   public void addService(Service service) {
/*  68 */     if (this.servicesByName.containsKey(service.getName())) {
/*  69 */       throw new ModelException("model.uniqueness", new Object[0]);
/*     */     }
/*  71 */     this.services.add(service);
/*  72 */     this.servicesByName.put(service.getName(), service);
/*     */   }
/*     */ 
/*     */   public Service getServiceByName(QName name) {
/*  76 */     if (this.servicesByName.size() != this.services.size()) {
/*  77 */       initializeServicesByName();
/*     */     }
/*  79 */     return (Service)this.servicesByName.get(name);
/*     */   }
/*     */ 
/*     */   public List<Service> getServices()
/*     */   {
/*  84 */     return this.services;
/*     */   }
/*     */ 
/*     */   public void setServices(List<Service> l)
/*     */   {
/*  89 */     this.services = l;
/*     */   }
/*     */ 
/*     */   private void initializeServicesByName() {
/*  93 */     this.servicesByName = new HashMap();
/*  94 */     if (this.services != null)
/*  95 */       for (Service service : this.services) {
/*  96 */         if ((service.getName() != null) && 
/*  97 */           (this.servicesByName
/*  97 */           .containsKey(service
/*  97 */           .getName())))
/*     */         {
/*  99 */           throw new ModelException("model.uniqueness", new Object[0]);
/*     */         }
/* 101 */         this.servicesByName.put(service.getName(), service);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addExtraType(AbstractType type)
/*     */   {
/* 107 */     this.extraTypes.add(type);
/*     */   }
/*     */ 
/*     */   public Iterator getExtraTypes() {
/* 111 */     return this.extraTypes.iterator();
/*     */   }
/*     */ 
/*     */   public Set<AbstractType> getExtraTypesSet()
/*     */   {
/* 116 */     return this.extraTypes;
/*     */   }
/*     */ 
/*     */   public void setExtraTypesSet(Set<AbstractType> s)
/*     */   {
/* 121 */     this.extraTypes = s;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception
/*     */   {
/* 126 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   public String getSource()
/*     */   {
/* 133 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setSource(String string)
/*     */   {
/* 140 */     this.source = string;
/*     */   }
/*     */ 
/*     */   public void setJAXBModel(JAXBModel jaxBModel) {
/* 144 */     this.jaxBModel = jaxBModel;
/*     */   }
/*     */ 
/*     */   public JAXBModel getJAXBModel() {
/* 148 */     return this.jaxBModel;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Model
 * JD-Core Version:    0.6.2
 */