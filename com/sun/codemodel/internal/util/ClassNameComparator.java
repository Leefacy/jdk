/*    */ package com.sun.codemodel.internal.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class ClassNameComparator
/*    */   implements Comparator<JClass>
/*    */ {
/* 45 */   public static final Comparator<JClass> theInstance = new ClassNameComparator();
/*    */ 
/*    */   public int compare(JClass l, JClass r)
/*    */   {
/* 42 */     return l.fullName().compareTo(r.fullName());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.util.ClassNameComparator
 * JD-Core Version:    0.6.2
 */