/*     */ package sun.rmi.rmic.newrmic;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class BatchEnvironment
/*     */ {
/*     */   private final RootDoc rootDoc;
/*     */   private final ClassDoc docRemote;
/*     */   private final ClassDoc docException;
/*     */   private final ClassDoc docRemoteException;
/*     */   private final ClassDoc docRuntimeException;
/*  70 */   private boolean verbose = false;
/*  71 */   private final List<File> generatedFiles = new ArrayList();
/*     */ 
/*     */   public BatchEnvironment(RootDoc paramRootDoc)
/*     */   {
/*  77 */     this.rootDoc = paramRootDoc;
/*     */ 
/*  84 */     this.docRemote = rootDoc().classNamed("java.rmi.Remote");
/*  85 */     this.docException = rootDoc().classNamed("java.lang.Exception");
/*  86 */     this.docRemoteException = rootDoc().classNamed("java.rmi.RemoteException");
/*  87 */     this.docRuntimeException = rootDoc().classNamed("java.lang.RuntimeException");
/*     */   }
/*     */ 
/*     */   public RootDoc rootDoc()
/*     */   {
/*  94 */     return this.rootDoc;
/*     */   }
/*     */   public ClassDoc docRemote() {
/*  97 */     return this.docRemote; } 
/*  98 */   public ClassDoc docException() { return this.docException; } 
/*  99 */   public ClassDoc docRemoteException() { return this.docRemoteException; } 
/* 100 */   public ClassDoc docRuntimeException() { return this.docRuntimeException; }
/*     */ 
/*     */ 
/*     */   public void setVerbose(boolean paramBoolean)
/*     */   {
/* 106 */     this.verbose = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean verbose()
/*     */   {
/* 113 */     return this.verbose;
/*     */   }
/*     */ 
/*     */   public void addGeneratedFile(File paramFile)
/*     */   {
/* 121 */     this.generatedFiles.add(paramFile);
/*     */   }
/*     */ 
/*     */   public List<File> generatedFiles()
/*     */   {
/* 128 */     return Collections.unmodifiableList(this.generatedFiles);
/*     */   }
/*     */ 
/*     */   public void output(String paramString)
/*     */   {
/* 135 */     this.rootDoc.printNotice(paramString);
/*     */   }
/*     */ 
/*     */   public void error(String paramString, String[] paramArrayOfString)
/*     */   {
/* 143 */     this.rootDoc.printError(Resources.getText(paramString, paramArrayOfString));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.BatchEnvironment
 * JD-Core Version:    0.6.2
 */