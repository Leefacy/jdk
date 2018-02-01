/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ public final class ClassType
/*    */ {
/*    */   final String declarationToken;
/* 45 */   public static final ClassType CLASS = new ClassType("class");
/* 46 */   public static final ClassType INTERFACE = new ClassType("interface");
/* 47 */   public static final ClassType ANNOTATION_TYPE_DECL = new ClassType("@interface");
/* 48 */   public static final ClassType ENUM = new ClassType("enum");
/*    */ 
/*    */   private ClassType(String token)
/*    */   {
/* 42 */     this.declarationToken = token;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.ClassType
 * JD-Core Version:    0.6.2
 */