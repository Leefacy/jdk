/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.api.Mapping;
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public final class PropertyImpl
/*    */   implements Property
/*    */ {
/*    */   protected final FieldOutline fr;
/*    */   protected final QName elementName;
/*    */   protected final Mapping parent;
/*    */   protected final JCodeModel codeModel;
/*    */ 
/*    */   PropertyImpl(Mapping parent, FieldOutline fr, QName elementName)
/*    */   {
/* 47 */     this.parent = parent;
/* 48 */     this.fr = fr;
/* 49 */     this.elementName = elementName;
/* 50 */     this.codeModel = fr.getRawType().owner();
/*    */   }
/*    */ 
/*    */   public final String name() {
/* 54 */     return this.fr.getPropertyInfo().getName(false);
/*    */   }
/*    */ 
/*    */   public final QName rawName()
/*    */   {
/* 59 */     if ((this.fr instanceof ElementAdapter)) {
/* 60 */       CElementInfo eInfo = ((ElementAdapter)this.fr).ei;
/* 61 */       if ((eInfo != null) && (eInfo.getProperty() != null)) {
/* 62 */         return ((CTypeRef)eInfo.getProperty().getTypes().get(0)).getTypeName();
/*    */       }
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   public final QName elementName() {
/* 69 */     return this.elementName;
/*    */   }
/*    */ 
/*    */   public final JType type() {
/* 73 */     return this.fr.getRawType();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.PropertyImpl
 * JD-Core Version:    0.6.2
 */