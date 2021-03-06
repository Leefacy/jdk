/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class AutoClassNameAllocator
/*    */   implements ClassNameAllocator
/*    */ {
/*    */   private final ClassNameAllocator core;
/* 43 */   private final Map<String, Set<String>> names = new HashMap();
/*    */ 
/*    */   public AutoClassNameAllocator(ClassNameAllocator core) {
/* 46 */     this.core = core;
/*    */   }
/*    */ 
/*    */   public String assignClassName(String packageName, String className) {
/* 50 */     className = determineName(packageName, className);
/* 51 */     if (this.core != null)
/* 52 */       className = this.core.assignClassName(packageName, className);
/* 53 */     return className;
/*    */   }
/*    */ 
/*    */   private String determineName(String packageName, String className) {
/* 57 */     Set s = (Set)this.names.get(packageName);
/* 58 */     if (s == null) {
/* 59 */       s = new HashSet();
/* 60 */       this.names.put(packageName, s);
/*    */     }
/*    */ 
/* 63 */     if (s.add(className)) {
/* 64 */       return className;
/*    */     }
/* 66 */     for (int i = 2; ; i++)
/* 67 */       if (s.add(className + i))
/* 68 */         return className + i;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.AutoClassNameAllocator
 * JD-Core Version:    0.6.2
 */