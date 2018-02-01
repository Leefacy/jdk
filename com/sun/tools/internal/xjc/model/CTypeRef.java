/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class CTypeRef
/*     */   implements TypeRef<NType, NClass>
/*     */ {
/*     */ 
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/*     */   private final CNonElement type;
/*     */   private final QName elementName;
/*     */ 
/*     */   @Nullable
/*     */   final QName typeName;
/*     */   private final boolean nillable;
/*     */   public final XmlString defaultValue;
/*     */ 
/*     */   public CTypeRef(CNonElement type, XSElementDecl decl)
/*     */   {
/*  70 */     this(type, BGMBuilder.getName(decl), getSimpleTypeName(decl), decl.isNillable(), decl.getDefaultValue());
/*     */   }
/*     */ 
/*     */   public QName getTypeName()
/*     */   {
/*  75 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public static QName getSimpleTypeName(XSElementDecl decl) {
/*  79 */     if ((decl == null) || (!decl.getType().isSimpleType()))
/*  80 */       return null;
/*  81 */     return resolveSimpleTypeName(decl.getType());
/*     */   }
/*     */ 
/*     */   private static QName resolveSimpleTypeName(XSType declType)
/*     */   {
/* 102 */     QName name = BGMBuilder.getName(declType);
/* 103 */     if ((name != null) && (!"http://www.w3.org/2001/XMLSchema".equals(name.getNamespaceURI()))) {
/* 104 */       return resolveSimpleTypeName(declType.getBaseType());
/*     */     }
/* 106 */     return name;
/*     */   }
/*     */ 
/*     */   public CTypeRef(CNonElement type, QName elementName, QName typeName, boolean nillable, XmlString defaultValue) {
/* 110 */     assert (type != null);
/* 111 */     assert (elementName != null);
/*     */ 
/* 113 */     this.type = type;
/* 114 */     this.elementName = elementName;
/* 115 */     this.typeName = typeName;
/* 116 */     this.nillable = nillable;
/* 117 */     this.defaultValue = defaultValue;
/*     */   }
/*     */ 
/*     */   public CNonElement getTarget() {
/* 121 */     return this.type;
/*     */   }
/*     */ 
/*     */   public QName getTagName() {
/* 125 */     return this.elementName;
/*     */   }
/*     */ 
/*     */   public boolean isNillable() {
/* 129 */     return this.nillable;
/*     */   }
/*     */ 
/*     */   public String getDefaultValue()
/*     */   {
/* 139 */     if (this.defaultValue != null) {
/* 140 */       return this.defaultValue.value;
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isLeaf()
/*     */   {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public PropertyInfo<NType, NClass> getSource()
/*     */   {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CTypeRef
 * JD-Core Version:    0.6.2
 */