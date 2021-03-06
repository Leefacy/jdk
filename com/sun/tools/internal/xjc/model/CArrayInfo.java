/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public final class CArrayInfo extends AbstractCTypeInfoImpl
/*    */   implements ArrayInfo<NType, NClass>, CNonElement, NType
/*    */ {
/*    */   private final CNonElement itemType;
/*    */   private final QName typeName;
/*    */ 
/*    */   public CArrayInfo(Model model, CNonElement itemType, XSComponent source, CCustomizations customizations)
/*    */   {
/* 57 */     super(model, source, customizations);
/* 58 */     this.itemType = itemType;
/* 59 */     assert (itemType.getTypeName() != null);
/* 60 */     this.typeName = ArrayInfoUtil.calcArrayTypeName(itemType.getTypeName());
/*    */   }
/*    */ 
/*    */   public CNonElement getItemType() {
/* 64 */     return this.itemType;
/*    */   }
/*    */ 
/*    */   public QName getTypeName() {
/* 68 */     return this.typeName;
/*    */   }
/*    */ 
/*    */   public boolean isSimpleType() {
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   @Deprecated
/*    */   public CNonElement getInfo() {
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */   public JType toType(Outline o, Aspect aspect) {
/* 81 */     return this.itemType.toType(o, aspect).array();
/*    */   }
/*    */ 
/*    */   public NType getType() {
/* 85 */     return this;
/*    */   }
/*    */ 
/*    */   public boolean isBoxedType() {
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   public String fullName() {
/* 93 */     return ((NType)this.itemType.getType()).fullName() + "[]";
/*    */   }
/*    */ 
/*    */   public Locator getLocator() {
/* 97 */     return Model.EMPTY_LOCATOR;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CArrayInfo
 * JD-Core Version:    0.6.2
 */