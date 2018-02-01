/*    */ package com.sun.tools.internal.xjc.addon.sync;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.codemodel.internal.JMods;
/*    */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.Plugin;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ 
/*    */ public class SynchronizedMethodAddOn extends Plugin
/*    */ {
/*    */   public String getOptionName()
/*    */   {
/* 48 */     return "Xsync-methods";
/*    */   }
/*    */ 
/*    */   public String getUsage() {
/* 52 */     return "  -Xsync-methods     :  generate accessor methods with the 'synchronized' keyword";
/*    */   }
/*    */ 
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 56 */     return 0;
/*    */   }
/*    */ 
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
/*    */   {
/* 61 */     for (ClassOutline co : model.getClasses()) {
/* 62 */       augument(co);
/*    */     }
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   private void augument(ClassOutline co)
/*    */   {
/* 71 */     for (JMethod m : co.implClass.methods())
/* 72 */       m.getMods().setSynchronized(true);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.addon.sync.SynchronizedMethodAddOn
 * JD-Core Version:    0.6.2
 */