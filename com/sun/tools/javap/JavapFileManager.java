/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.javac.file.JavacFileManager;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import java.io.PrintWriter;
/*    */ import java.nio.charset.Charset;
/*    */ import javax.tools.DiagnosticListener;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ public class JavapFileManager extends JavacFileManager
/*    */ {
/*    */   private JavapFileManager(Context paramContext, Charset paramCharset)
/*    */   {
/* 46 */     super(paramContext, true, paramCharset);
/* 47 */     setSymbolFileEnabled(false);
/*    */   }
/*    */ 
/*    */   public static JavapFileManager create(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, PrintWriter paramPrintWriter) {
/* 51 */     Context localContext = new Context();
/*    */ 
/* 53 */     if (paramDiagnosticListener != null)
/* 54 */       localContext.put(DiagnosticListener.class, paramDiagnosticListener);
/* 55 */     localContext.put(Log.outKey, paramPrintWriter);
/*    */ 
/* 57 */     return new JavapFileManager(localContext, null);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.JavapFileManager
 * JD-Core Version:    0.6.2
 */