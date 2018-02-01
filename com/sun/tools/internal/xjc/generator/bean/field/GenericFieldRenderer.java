/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ public final class GenericFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private Constructor constructor;
/*    */ 
/*    */   public GenericFieldRenderer(Class fieldClass)
/*    */   {
/*    */     try
/*    */     {
/* 46 */       this.constructor = fieldClass.getDeclaredConstructor(new Class[] { ClassOutlineImpl.class, CPropertyInfo.class });
/*    */     } catch (NoSuchMethodException e) {
/* 48 */       throw new NoSuchMethodError(e.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/*    */     try {
/* 54 */       return (FieldOutline)this.constructor.newInstance(new Object[] { context, prop });
/*    */     } catch (InstantiationException e) {
/* 56 */       throw new InstantiationError(e.getMessage());
/*    */     } catch (IllegalAccessException e) {
/* 58 */       throw new IllegalAccessError(e.getMessage());
/*    */     } catch (InvocationTargetException e) {
/* 60 */       Throwable t = e.getTargetException();
/* 61 */       if ((t instanceof RuntimeException))
/* 62 */         throw ((RuntimeException)t);
/* 63 */       if ((t instanceof Error)) {
/* 64 */         throw ((Error)t);
/*    */       }
/*    */ 
/* 67 */       throw new AssertionError(t);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.GenericFieldRenderer
 * JD-Core Version:    0.6.2
 */