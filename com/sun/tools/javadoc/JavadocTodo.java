/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.comp.AttrContext;
/*    */ import com.sun.tools.javac.comp.Env;
/*    */ import com.sun.tools.javac.comp.Todo;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Factory;
/*    */ 
/*    */ public class JavadocTodo extends Todo
/*    */ {
/*    */   public static void preRegister(Context paramContext)
/*    */   {
/* 44 */     paramContext.put(todoKey, new Context.Factory() {
/*    */       public Todo make(Context paramAnonymousContext) {
/* 46 */         return new JavadocTodo(paramAnonymousContext);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   protected JavadocTodo(Context paramContext) {
/* 52 */     super(paramContext);
/*    */   }
/*    */ 
/*    */   public void append(Env<AttrContext> paramEnv)
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean offer(Env<AttrContext> paramEnv)
/*    */   {
/* 62 */     return false;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.JavadocTodo
 * JD-Core Version:    0.6.2
 */