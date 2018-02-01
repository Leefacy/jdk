/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.codemodel.internal.JBlock;
/*    */ import com.sun.codemodel.internal.JConditional;
/*    */ import com.sun.codemodel.internal.JExpr;
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.codemodel.internal.JVar;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ 
/*    */ final class ElementSingleAdapter extends ElementAdapter
/*    */ {
/*    */   public ElementSingleAdapter(FieldOutline core, CElementInfo ei)
/*    */   {
/* 51 */     super(core, ei);
/*    */   }
/*    */ 
/*    */   public JType getRawType() {
/* 55 */     return this.ei.getContentInMemoryType().toType(outline(), Aspect.EXPOSED);
/*    */   }
/*    */ 
/*    */   public FieldAccessor create(JExpression targetObject) {
/* 59 */     return new FieldAccessorImpl(targetObject);
/*    */   }
/*    */ 
/*    */   final class FieldAccessorImpl extends ElementAdapter.FieldAccessorImpl {
/*    */     public FieldAccessorImpl(JExpression target) {
/* 64 */       super(target);
/*    */     }
/*    */ 
/*    */     public void toRawValue(JBlock block, JVar $var)
/*    */     {
/* 74 */       JConditional cond = block._if(this.acc.hasSetValue());
/* 75 */       JVar $v = cond._then().decl(ElementSingleAdapter.this.core.getRawType(), "v" + hashCode());
/* 76 */       this.acc.toRawValue(cond._then(), $v);
/* 77 */       cond._then().assign($var, $v.invoke("getValue"));
/* 78 */       cond._else().assign($var, JExpr._null());
/*    */     }
/*    */ 
/*    */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var)
/*    */     {
/* 85 */       this.acc.fromRawValue(block, uniqueName, createJAXBElement($var));
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.ElementSingleAdapter
 * JD-Core Version:    0.6.2
 */