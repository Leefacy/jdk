/*    */ package com.sun.tools.doclets.standard;
/*    */ 
/*    */ import com.sun.javadoc.DocErrorReporter;
/*    */ import com.sun.javadoc.LanguageVersion;
/*    */ import com.sun.javadoc.RootDoc;
/*    */ import com.sun.tools.doclets.formats.html.HtmlDoclet;
/*    */ 
/*    */ public class Standard
/*    */ {
/*    */   public static int optionLength(String paramString)
/*    */   {
/* 35 */     return HtmlDoclet.optionLength(paramString);
/*    */   }
/*    */ 
/*    */   public static boolean start(RootDoc paramRootDoc) {
/* 39 */     return HtmlDoclet.start(paramRootDoc);
/*    */   }
/*    */ 
/*    */   public static boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter)
/*    */   {
/* 44 */     return HtmlDoclet.validOptions(paramArrayOfString, paramDocErrorReporter);
/*    */   }
/*    */ 
/*    */   public static LanguageVersion languageVersion() {
/* 48 */     return HtmlDoclet.languageVersion();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.standard.Standard
 * JD-Core Version:    0.6.2
 */