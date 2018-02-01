/*    */ package com.sun.tools.javac.jvm;
/*    */ 
/*    */ import com.sun.tools.javac.code.Type;
/*    */ import com.sun.tools.javac.code.Type.DelegatedType;
/*    */ import com.sun.tools.javac.code.TypeTag;
/*    */ 
/*    */ class UninitializedType extends Type.DelegatedType
/*    */ {
/*    */   public final int offset;
/*    */ 
/*    */   public static UninitializedType uninitializedThis(Type paramType)
/*    */   {
/* 44 */     return new UninitializedType(TypeTag.UNINITIALIZED_THIS, paramType, -1);
/*    */   }
/*    */ 
/*    */   public static UninitializedType uninitializedObject(Type paramType, int paramInt) {
/* 48 */     return new UninitializedType(TypeTag.UNINITIALIZED_OBJECT, paramType, paramInt);
/*    */   }
/*    */ 
/*    */   private UninitializedType(TypeTag paramTypeTag, Type paramType, int paramInt)
/*    */   {
/* 53 */     super(paramTypeTag, paramType);
/* 54 */     this.offset = paramInt;
/*    */   }
/*    */ 
/*    */   Type initializedType() {
/* 58 */     return this.qtype;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.UninitializedType
 * JD-Core Version:    0.6.2
 */