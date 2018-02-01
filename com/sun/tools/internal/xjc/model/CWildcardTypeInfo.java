/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.bind.v2.model.core.WildcardTypeInfo;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public final class CWildcardTypeInfo extends AbstractCTypeInfoImpl
/*    */   implements WildcardTypeInfo<NType, NClass>
/*    */ {
/* 51 */   public static final CWildcardTypeInfo INSTANCE = new CWildcardTypeInfo();
/*    */ 
/*    */   private CWildcardTypeInfo()
/*    */   {
/* 48 */     super(null, null, null);
/*    */   }
/*    */ 
/*    */   public JType toType(Outline o, Aspect aspect)
/*    */   {
/* 54 */     return o.getCodeModel().ref(Element.class);
/*    */   }
/*    */ 
/*    */   public NType getType() {
/* 58 */     return NavigatorImpl.create(Element.class);
/*    */   }
/*    */ 
/*    */   public Locator getLocator() {
/* 62 */     return Model.EMPTY_LOCATOR;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CWildcardTypeInfo
 * JD-Core Version:    0.6.2
 */