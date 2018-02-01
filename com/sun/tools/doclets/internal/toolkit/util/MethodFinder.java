/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.ClassDoc;
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ 
/*    */ public abstract class MethodFinder
/*    */ {
/*    */   abstract boolean isCorrectMethod(MethodDoc paramMethodDoc);
/*    */ 
/*    */   public MethodDoc search(ClassDoc paramClassDoc, MethodDoc paramMethodDoc)
/*    */   {
/* 46 */     MethodDoc localMethodDoc = searchInterfaces(paramClassDoc, paramMethodDoc);
/* 47 */     if (localMethodDoc != null) {
/* 48 */       return localMethodDoc;
/*    */     }
/* 50 */     ClassDoc localClassDoc = paramClassDoc.superclass();
/* 51 */     if (localClassDoc != null) {
/* 52 */       localMethodDoc = Util.findMethod(localClassDoc, paramMethodDoc);
/* 53 */       if ((localMethodDoc != null) && 
/* 54 */         (isCorrectMethod(localMethodDoc))) {
/* 55 */         return localMethodDoc;
/*    */       }
/*    */ 
/* 58 */       return search(localClassDoc, paramMethodDoc);
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   public MethodDoc searchInterfaces(ClassDoc paramClassDoc, MethodDoc paramMethodDoc) {
/* 64 */     MethodDoc[] arrayOfMethodDoc = new ImplementedMethods(paramMethodDoc, null).build();
/* 65 */     for (int i = 0; i < arrayOfMethodDoc.length; i++) {
/* 66 */       if (isCorrectMethod(arrayOfMethodDoc[i])) {
/* 67 */         return arrayOfMethodDoc[i];
/*    */       }
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.MethodFinder
 * JD-Core Version:    0.6.2
 */