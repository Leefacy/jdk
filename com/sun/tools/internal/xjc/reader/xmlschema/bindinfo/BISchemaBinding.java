/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ @XmlRootElement(name="schemaBindings")
/*     */ public final class BISchemaBinding extends AbstractDeclarationImpl
/*     */ {
/*     */ 
/*     */   @XmlElement
/*  68 */   private NameRules nameXmlTransform = new NameRules(null);
/*     */ 
/*     */   @XmlElement(name="package")
/*  78 */   private PackageInfo packageInfo = new PackageInfo(null);
/*     */ 
/*     */   @XmlAttribute(name="map")
/*  86 */   public boolean map = true;
/*     */ 
/*  92 */   private static final NamingRule defaultNamingRule = new NamingRule("", "");
/*     */ 
/* 156 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "schemaBinding");
/*     */ 
/*     */   public String mangleClassName(String name, XSComponent cmp)
/*     */   {
/* 133 */     if ((cmp instanceof XSType))
/* 134 */       return this.nameXmlTransform.typeName.mangle(name);
/* 135 */     if ((cmp instanceof XSElementDecl))
/* 136 */       return this.nameXmlTransform.elementName.mangle(name);
/* 137 */     if ((cmp instanceof XSAttributeDecl))
/* 138 */       return this.nameXmlTransform.attributeName.mangle(name);
/* 139 */     if (((cmp instanceof XSModelGroup)) || ((cmp instanceof XSModelGroupDecl))) {
/* 140 */       return this.nameXmlTransform.modelGroupName.mangle(name);
/*     */     }
/*     */ 
/* 143 */     return name;
/*     */   }
/*     */ 
/*     */   public String mangleAnonymousTypeClassName(String name) {
/* 147 */     return this.nameXmlTransform.anonymousTypeName.mangle(name);
/*     */   }
/*     */ 
/*     */   public String getPackageName() {
/* 151 */     return this.packageInfo.name;
/*     */   }
/* 153 */   public String getJavadoc() { return this.packageInfo.javadoc; } 
/*     */   public QName getName() {
/* 155 */     return NAME;
/*     */   }
/*     */ 
/*     */   @XmlType(propOrder={})
/*     */   private static final class NameRules
/*     */   {
/*     */ 
/*     */     @XmlElement
/*     */     BISchemaBinding.NamingRule typeName;
/*     */ 
/*     */     @XmlElement
/*     */     BISchemaBinding.NamingRule elementName;
/*     */ 
/*     */     @XmlElement
/*     */     BISchemaBinding.NamingRule attributeName;
/*     */ 
/*     */     @XmlElement
/*     */     BISchemaBinding.NamingRule modelGroupName;
/*     */ 
/*     */     @XmlElement
/*  65 */     BISchemaBinding.NamingRule anonymousTypeName = BISchemaBinding.defaultNamingRule;
/*     */ 
/*     */     private NameRules()
/*     */     {
/*  56 */       this.typeName = 
/*  57 */         BISchemaBinding.defaultNamingRule;
/*  58 */       this.elementName = 
/*  59 */         BISchemaBinding.defaultNamingRule;
/*  60 */       this.attributeName = 
/*  61 */         BISchemaBinding.defaultNamingRule;
/*  62 */       this.modelGroupName = 
/*  63 */         BISchemaBinding.defaultNamingRule;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class NamingRule
/*     */   {
/*     */ 
/*     */     @XmlAttribute
/* 103 */     private String prefix = "";
/*     */ 
/*     */     @XmlAttribute
/* 105 */     private String suffix = "";
/*     */ 
/*     */     public NamingRule(String _prefix, String _suffix)
/*     */     {
/* 109 */       this.prefix = _prefix;
/* 110 */       this.suffix = _suffix;
/*     */     }
/*     */ 
/*     */     public NamingRule()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String mangle(String originalName) {
/* 118 */       return this.prefix + originalName + this.suffix;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PackageInfo
/*     */   {
/*     */ 
/*     */     @XmlAttribute
/*     */     String name;
/*     */ 
/*     */     @XmlElement
/*     */     String javadoc;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BISchemaBinding
 * JD-Core Version:    0.6.2
 */