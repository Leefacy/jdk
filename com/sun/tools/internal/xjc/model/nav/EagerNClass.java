/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class EagerNClass extends EagerNType
/*    */   implements NClass
/*    */ {
/*    */   final Class c;
/* 61 */   private static final Set<Class> boxedTypes = new HashSet();
/*    */ 
/*    */   public EagerNClass(Class type)
/*    */   {
/* 43 */     super(type);
/* 44 */     this.c = type;
/*    */   }
/*    */ 
/*    */   public boolean isBoxedType()
/*    */   {
/* 49 */     return boxedTypes.contains(this.c);
/*    */   }
/*    */ 
/*    */   public JClass toType(Outline o, Aspect aspect)
/*    */   {
/* 54 */     return o.getCodeModel().ref(this.c);
/*    */   }
/*    */ 
/*    */   public boolean isAbstract() {
/* 58 */     return Modifier.isAbstract(this.c.getModifiers());
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 64 */     boxedTypes.add(Boolean.class);
/* 65 */     boxedTypes.add(Character.class);
/* 66 */     boxedTypes.add(Byte.class);
/* 67 */     boxedTypes.add(Short.class);
/* 68 */     boxedTypes.add(Integer.class);
/* 69 */     boxedTypes.add(Long.class);
/* 70 */     boxedTypes.add(Float.class);
/* 71 */     boxedTypes.add(Double.class);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.nav.EagerNClass
 * JD-Core Version:    0.6.2
 */