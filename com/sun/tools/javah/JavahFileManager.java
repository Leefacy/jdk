/*    */ package com.sun.tools.javah;
/*    */ 
/*    */ import com.sun.tools.javac.file.JavacFileManager;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import java.io.PrintWriter;
/*    */ import java.nio.charset.Charset;
/*    */ import javax.tools.DiagnosticListener;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ class JavahFileManager extends JavacFileManager
/*    */ {
/*    */   private JavahFileManager(Context paramContext, Charset paramCharset)
/*    */   {
/* 46 */     super(paramContext, true, paramCharset);
/* 47 */     setSymbolFileEnabled(false);
/*    */   }
/*    */ 
/*    */   static JavahFileManager create(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, PrintWriter paramPrintWriter) {
/* 51 */     Context localContext = new Context();
/*    */ 
/* 53 */     if (paramDiagnosticListener != null)
/* 54 */       localContext.put(DiagnosticListener.class, paramDiagnosticListener);
/* 55 */     localContext.put(Log.outKey, paramPrintWriter);
/*    */ 
/* 57 */     return new JavahFileManager(localContext, null);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.JavahFileManager
 * JD-Core Version:    0.6.2
 */