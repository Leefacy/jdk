/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Context
/*    */ {
/*    */   Map<Class<?>, Object> map;
/*    */ 
/*    */   public Context()
/*    */   {
/* 41 */     this.map = new HashMap();
/*    */   }
/*    */ 
/*    */   public <T> T get(Class<T> paramClass)
/*    */   {
/* 46 */     return this.map.get(paramClass);
/*    */   }
/*    */ 
/*    */   public <T> T put(Class<T> paramClass, T paramT)
/*    */   {
/* 51 */     return this.map.put(paramClass, paramT);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.Context
 * JD-Core Version:    0.6.2
 */