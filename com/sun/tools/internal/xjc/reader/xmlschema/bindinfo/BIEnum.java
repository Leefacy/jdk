/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ @XmlRootElement(name="typesafeEnumClass")
/*     */ public final class BIEnum extends AbstractDeclarationImpl
/*     */ {
/*     */ 
/*     */   @XmlAttribute(name="map")
/*  60 */   private boolean map = true;
/*     */ 
/*     */   @XmlAttribute(name="name")
/*  64 */   public String className = null;
/*     */ 
/*     */   @XmlAttribute(name="ref")
/*     */   public String ref;
/*     */ 
/*     */   @XmlElement
/*  77 */   public final String javadoc = null;
/*     */ 
/*     */   @XmlTransient
/*  90 */   public final Map<String, BIEnumMember> members = new HashMap();
/*     */ 
/* 107 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "enum");
/*     */ 
/*     */   public boolean isMapped()
/*     */   {
/*  81 */     return this.map;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  93 */     return NAME;
/*     */   }
/*     */   public void setParent(BindInfo p) {
/*  96 */     super.setParent(p);
/*  97 */     for (BIEnumMember mem : this.members.values()) {
/*  98 */       mem.setParent(p);
/*     */     }
/*     */ 
/* 102 */     if (this.ref != null)
/* 103 */       markAsAcknowledged();
/*     */   }
/*     */ 
/*     */   @XmlElement(name="typesafeEnumMember")
/*     */   private void setMembers(BIEnumMember2[] mems)
/*     */   {
/* 113 */     for (BIEnumMember2 e : mems)
/* 114 */       this.members.put(e.value, e);
/*     */   }
/*     */ 
/*     */   static class BIEnumMember2 extends BIEnumMember
/*     */   {
/*     */ 
/*     */     @XmlAttribute(required=true)
/*     */     String value;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIEnum
 * JD-Core Version:    0.6.2
 */