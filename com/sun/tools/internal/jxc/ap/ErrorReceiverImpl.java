/*    */ package com.sun.tools.internal.jxc.ap;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import javax.annotation.processing.Messager;
/*    */ import javax.annotation.processing.ProcessingEnvironment;
/*    */ import javax.tools.Diagnostic.Kind;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ final class ErrorReceiverImpl extends ErrorReceiver
/*    */ {
/*    */   private final Messager messager;
/*    */   private final boolean debug;
/*    */ 
/*    */   public ErrorReceiverImpl(Messager messager, boolean debug)
/*    */   {
/* 43 */     this.messager = messager;
/* 44 */     this.debug = debug;
/*    */   }
/*    */ 
/*    */   public ErrorReceiverImpl(Messager messager) {
/* 48 */     this(messager, false);
/*    */   }
/*    */ 
/*    */   public ErrorReceiverImpl(ProcessingEnvironment env) {
/* 52 */     this(env.getMessager());
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException exception) {
/* 56 */     this.messager.printMessage(Diagnostic.Kind.ERROR, exception.getMessage());
/* 57 */     this.messager.printMessage(Diagnostic.Kind.ERROR, getLocation(exception));
/* 58 */     printDetail(exception);
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException exception) {
/* 62 */     this.messager.printMessage(Diagnostic.Kind.ERROR, exception.getMessage());
/* 63 */     this.messager.printMessage(Diagnostic.Kind.ERROR, getLocation(exception));
/* 64 */     printDetail(exception);
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException exception) {
/* 68 */     this.messager.printMessage(Diagnostic.Kind.WARNING, exception.getMessage());
/* 69 */     this.messager.printMessage(Diagnostic.Kind.WARNING, getLocation(exception));
/* 70 */     printDetail(exception);
/*    */   }
/*    */ 
/*    */   public void info(SAXParseException exception) {
/* 74 */     printDetail(exception);
/*    */   }
/*    */ 
/*    */   private String getLocation(SAXParseException e)
/*    */   {
/* 79 */     return "";
/*    */   }
/*    */ 
/*    */   private void printDetail(SAXParseException e) {
/* 83 */     if (this.debug)
/* 84 */       e.printStackTrace(System.out);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.ErrorReceiverImpl
 * JD-Core Version:    0.6.2
 */