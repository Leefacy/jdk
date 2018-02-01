/*    */ package com.sun.tools.javah;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Arrays;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ import javax.lang.model.SourceVersion;
/*    */ import javax.tools.DiagnosticListener;
/*    */ import javax.tools.JavaFileManager;
/*    */ import javax.tools.JavaFileObject;
/*    */ import javax.tools.StandardJavaFileManager;
/*    */ 
/*    */ public class JavahTool
/*    */   implements NativeHeaderTool
/*    */ {
/*    */   public NativeHeaderTool.NativeHeaderTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2)
/*    */   {
/* 55 */     return new JavahTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramIterable1, paramIterable2);
/*    */   }
/*    */ 
/*    */   public StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset) {
/* 59 */     return JavahTask.getDefaultFileManager(paramDiagnosticListener, null);
/*    */   }
/*    */ 
/*    */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String[] paramArrayOfString)
/*    */   {
/* 67 */     JavahTask localJavahTask = new JavahTask(
/* 64 */       JavahTask.getPrintWriterForStream(paramOutputStream1), 
/* 64 */       null, null, 
/* 67 */       Arrays.asList(paramArrayOfString), 
/* 67 */       null);
/*    */ 
/* 69 */     return localJavahTask.run() ? 0 : 1;
/*    */   }
/*    */ 
/*    */   public Set<SourceVersion> getSourceVersions() {
/* 73 */     return EnumSet.allOf(SourceVersion.class);
/*    */   }
/*    */ 
/*    */   public int isSupportedOption(String paramString) {
/* 77 */     JavahTask.Option[] arrayOfOption = JavahTask.recognizedOptions;
/* 78 */     for (int i = 0; i < arrayOfOption.length; i++) {
/* 79 */       if (arrayOfOption[i].matches(paramString))
/* 80 */         return arrayOfOption[i].hasArg ? 1 : 0;
/*    */     }
/* 82 */     return -1;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.JavahTool
 * JD-Core Version:    0.6.2
 */