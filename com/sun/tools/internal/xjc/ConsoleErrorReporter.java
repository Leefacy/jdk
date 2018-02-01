/*    */ package com.sun.tools.internal.xjc;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class ConsoleErrorReporter extends ErrorReceiver
/*    */ {
/*    */   private PrintStream output;
/* 46 */   private boolean hadError = false;
/*    */ 
/*    */   public ConsoleErrorReporter(PrintStream out) {
/* 49 */     this.output = out;
/*    */   }
/*    */   public ConsoleErrorReporter(OutputStream out) {
/* 52 */     this(new PrintStream(out));
/*    */   }
/* 54 */   public ConsoleErrorReporter() { this(System.out); }
/*    */ 
/*    */   public void warning(SAXParseException e) {
/* 57 */     print("Driver.WarningMessage", e);
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException e) {
/* 61 */     this.hadError = true;
/* 62 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException e) {
/* 66 */     this.hadError = true;
/* 67 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */ 
/*    */   public void info(SAXParseException e) {
/* 71 */     print("Driver.InfoMessage", e);
/*    */   }
/*    */ 
/*    */   public boolean hadError() {
/* 75 */     return this.hadError;
/*    */   }
/*    */ 
/*    */   private void print(String resource, SAXParseException e) {
/* 79 */     this.output.println(Messages.format(resource, new Object[] { e.getMessage() }));
/* 80 */     this.output.println(getLocationString(e));
/* 81 */     this.output.println();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.ConsoleErrorReporter
 * JD-Core Version:    0.6.2
 */