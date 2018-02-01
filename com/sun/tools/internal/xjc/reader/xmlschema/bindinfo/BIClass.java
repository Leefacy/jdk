/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ @XmlRootElement(name="class")
/*     */ public final class BIClass extends AbstractDeclarationImpl
/*     */ {
/*     */ 
/*     */   @XmlAttribute(name="name")
/*     */   private String className;
/*     */ 
/*     */   @XmlAttribute(name="implClass")
/*     */   private String userSpecifiedImplClass;
/*     */ 
/*     */   @XmlAttribute(name="ref")
/*     */   private String ref;
/*     */ 
/*     */   @XmlAttribute(name="recursive", namespace="http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   private String recursive;
/*     */ 
/*     */   @XmlElement
/*     */   private String javadoc;
/* 130 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "class");
/*     */ 
/*     */   @Nullable
/*     */   public String getClassName()
/*     */   {
/*  67 */     if (this.className == null) return null;
/*     */ 
/*  69 */     BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*  70 */     NameConverter nc = getBuilder().model.getNameConverter();
/*     */ 
/*  72 */     if (gb.isJavaNamingConventionEnabled()) return nc.toClassName(this.className);
/*     */ 
/*  75 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getUserSpecifiedImplClass()
/*     */   {
/*  87 */     return this.userSpecifiedImplClass;
/*     */   }
/*     */ 
/*     */   public String getExistingClassRef()
/*     */   {
/* 104 */     return this.ref;
/*     */   }
/*     */ 
/*     */   public String getRecursive() {
/* 108 */     return this.recursive;
/*     */   }
/*     */ 
/*     */   public String getJavadoc()
/*     */   {
/* 117 */     return this.javadoc;
/*     */   }
/* 119 */   public QName getName() { return NAME; }
/*     */ 
/*     */   public void setParent(BindInfo p) {
/* 122 */     super.setParent(p);
/*     */ 
/* 125 */     if (this.ref != null)
/* 126 */       markAsAcknowledged();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIClass
 * JD-Core Version:    0.6.2
 */