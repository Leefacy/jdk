/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class CReferencePropertyInfo extends CPropertyInfo
/*     */   implements ReferencePropertyInfo<NType, NClass>
/*     */ {
/*     */   private final boolean required;
/*  63 */   private final Set<CElement> elements = new HashSet();
/*     */   private final boolean isMixed;
/*     */   private WildcardMode wildcard;
/*     */   private boolean dummy;
/*     */   private boolean content;
/*  69 */   private boolean isMixedExtendedCust = false;
/*     */ 
/*     */   public CReferencePropertyInfo(String name, boolean collection, boolean required, boolean isMixed, XSComponent source, CCustomizations customizations, Locator locator, boolean dummy, boolean content, boolean isMixedExtended)
/*     */   {
/*  73 */     super(name, ((collection) || (isMixed)) && (!dummy), source, customizations, locator);
/*  74 */     this.isMixed = isMixed;
/*  75 */     this.required = required;
/*  76 */     this.dummy = dummy;
/*  77 */     this.content = content;
/*  78 */     this.isMixedExtendedCust = isMixedExtended; } 
/*     */   public Set<? extends CTypeInfo> ref() { // Byte code:
/*     */     //   0: new 103	com/sun/tools/internal/xjc/model/CReferencePropertyInfo$1RefList
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 217	com/sun/tools/internal/xjc/model/CReferencePropertyInfo$1RefList:<init>	(Lcom/sun/tools/internal/xjc/model/CReferencePropertyInfo;)V
/*     */     //   8: astore_1
/*     */     //   9: aload_0
/*     */     //   10: getfield 204	com/sun/tools/internal/xjc/model/CReferencePropertyInfo:wildcard	Lcom/sun/xml/internal/bind/v2/model/core/WildcardMode;
/*     */     //   13: ifnull +39 -> 52
/*     */     //   16: aload_0
/*     */     //   17: getfield 204	com/sun/tools/internal/xjc/model/CReferencePropertyInfo:wildcard	Lcom/sun/xml/internal/bind/v2/model/core/WildcardMode;
/*     */     //   20: getfield 209	com/sun/xml/internal/bind/v2/model/core/WildcardMode:allowDom	Z
/*     */     //   23: ifeq +11 -> 34
/*     */     //   26: aload_1
/*     */     //   27: getstatic 206	com/sun/tools/internal/xjc/model/CWildcardTypeInfo:INSTANCE	Lcom/sun/tools/internal/xjc/model/CWildcardTypeInfo;
/*     */     //   30: invokevirtual 218	com/sun/tools/internal/xjc/model/CReferencePropertyInfo$1RefList:add	(Ljava/lang/Object;)Z
/*     */     //   33: pop
/*     */     //   34: aload_0
/*     */     //   35: getfield 204	com/sun/tools/internal/xjc/model/CReferencePropertyInfo:wildcard	Lcom/sun/xml/internal/bind/v2/model/core/WildcardMode;
/*     */     //   38: getfield 210	com/sun/xml/internal/bind/v2/model/core/WildcardMode:allowTypedObject	Z
/*     */     //   41: ifeq +11 -> 52
/*     */     //   44: aload_1
/*     */     //   45: getstatic 197	com/sun/tools/internal/xjc/model/CBuiltinLeafInfo:ANYTYPE	Lcom/sun/tools/internal/xjc/model/CBuiltinLeafInfo;
/*     */     //   48: invokevirtual 218	com/sun/tools/internal/xjc/model/CReferencePropertyInfo$1RefList:add	(Ljava/lang/Object;)Z
/*     */     //   51: pop
/*     */     //   52: aload_0
/*     */     //   53: invokevirtual 212	com/sun/tools/internal/xjc/model/CReferencePropertyInfo:isMixed	()Z
/*     */     //   56: ifeq +11 -> 67
/*     */     //   59: aload_1
/*     */     //   60: getstatic 198	com/sun/tools/internal/xjc/model/CBuiltinLeafInfo:STRING	Lcom/sun/tools/internal/xjc/model/CBuiltinLeafInfo;
/*     */     //   63: invokevirtual 218	com/sun/tools/internal/xjc/model/CReferencePropertyInfo$1RefList:add	(Ljava/lang/Object;)Z
/*     */     //   66: pop
/*     */     //   67: aload_1
/*     */     //   68: areturn } 
/* 128 */   public Set<CElement> getElements() { return this.elements; }
/*     */ 
/*     */   public boolean isMixed()
/*     */   {
/* 132 */     return this.isMixed;
/*     */   }
/*     */ 
/*     */   public boolean isDummy() {
/* 136 */     return this.dummy;
/*     */   }
/*     */ 
/*     */   public boolean isContent() {
/* 140 */     return this.content;
/*     */   }
/*     */ 
/*     */   public boolean isMixedExtendedCust() {
/* 144 */     return this.isMixedExtendedCust;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public QName getXmlName()
/*     */   {
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isUnboxable()
/*     */   {
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isOptionalPrimitive()
/*     */   {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 171 */     return visitor.onReference(this);
/*     */   }
/*     */ 
/*     */   public CAdapter getAdapter() {
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   public final PropertyKind kind() {
/* 179 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */   public ID id()
/*     */   {
/* 187 */     return ID.NONE;
/*     */   }
/*     */ 
/*     */   public WildcardMode getWildcard() {
/* 191 */     return this.wildcard;
/*     */   }
/*     */ 
/*     */   public void setWildcard(WildcardMode mode) {
/* 195 */     this.wildcard = mode;
/*     */   }
/*     */ 
/*     */   public NClass getDOMHandler()
/*     */   {
/* 200 */     if (getWildcard() != null) {
/* 201 */       return NavigatorImpl.create(W3CDomHandler.class);
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public MimeType getExpectedMimeType() {
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isCollectionNillable()
/*     */   {
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCollectionRequired()
/*     */   {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   public QName getSchemaType()
/*     */   {
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isRequired() {
/* 226 */     return this.required;
/*     */   }
/*     */ 
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table)
/*     */   {
/* 231 */     for (CElement e : this.elements) {
/* 232 */       QName n = e.getElementName();
/* 233 */       if (table.containsKey(n))
/* 234 */         return n;
/* 235 */       table.put(n, this);
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CReferencePropertyInfo
 * JD-Core Version:    0.6.2
 */