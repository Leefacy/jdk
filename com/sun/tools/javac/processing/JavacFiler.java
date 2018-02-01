/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.Filer;
/*     */ import javax.annotation.processing.FilerException;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.ForwardingFileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class JavacFiler
/*     */   implements Filer, Closeable
/*     */ {
/*     */   private static final String ALREADY_OPENED = "Output stream or writer has already been opened.";
/*     */   private static final String NOT_FOR_READING = "FileObject was not opened for reading.";
/*     */   private static final String NOT_FOR_WRITING = "FileObject was not opened for writing.";
/*     */   JavaFileManager fileManager;
/*     */   Log log;
/*     */   Context context;
/*     */   boolean lastRound;
/*     */   private final boolean lint;
/*     */   private final Set<FileObject> fileObjectHistory;
/*     */   private final Set<String> openTypeNames;
/*     */   private Set<String> generatedSourceNames;
/*     */   private final Map<String, JavaFileObject> generatedClasses;
/*     */   private Set<JavaFileObject> generatedSourceFileObjects;
/*     */   private final Set<String> aggregateGeneratedSourceNames;
/*     */   private final Set<String> aggregateGeneratedClassNames;
/*     */ 
/*     */   JavacFiler(Context paramContext)
/*     */   {
/* 357 */     this.context = paramContext;
/* 358 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*     */ 
/* 360 */     this.log = Log.instance(paramContext);
/*     */ 
/* 362 */     this.fileObjectHistory = Collections.synchronizedSet(new LinkedHashSet());
/* 363 */     this.generatedSourceNames = Collections.synchronizedSet(new LinkedHashSet());
/* 364 */     this.generatedSourceFileObjects = Collections.synchronizedSet(new LinkedHashSet());
/*     */ 
/* 366 */     this.generatedClasses = Collections.synchronizedMap(new LinkedHashMap());
/*     */ 
/* 368 */     this.openTypeNames = Collections.synchronizedSet(new LinkedHashSet());
/*     */ 
/* 370 */     this.aggregateGeneratedSourceNames = new LinkedHashSet();
/* 371 */     this.aggregateGeneratedClassNames = new LinkedHashSet();
/*     */ 
/* 373 */     this.lint = Lint.instance(paramContext).isEnabled(Lint.LintCategory.PROCESSING);
/*     */   }
/*     */ 
/*     */   public JavaFileObject createSourceFile(CharSequence paramCharSequence, Element[] paramArrayOfElement) throws IOException
/*     */   {
/* 378 */     return createSourceOrClassFile(true, paramCharSequence.toString());
/*     */   }
/*     */ 
/*     */   public JavaFileObject createClassFile(CharSequence paramCharSequence, Element[] paramArrayOfElement) throws IOException
/*     */   {
/* 383 */     return createSourceOrClassFile(false, paramCharSequence.toString());
/*     */   }
/*     */ 
/*     */   private JavaFileObject createSourceOrClassFile(boolean paramBoolean, String paramString) throws IOException {
/* 387 */     if (this.lint) {
/* 388 */       int i = paramString.lastIndexOf(".");
/* 389 */       if (i != -1) {
/* 390 */         localObject1 = paramString.substring(i);
/* 391 */         localObject2 = paramBoolean ? ".java" : ".class";
/* 392 */         if (((String)localObject1).equals(localObject2))
/* 393 */           this.log.warning("proc.suspicious.class.name", new Object[] { paramString, localObject2 });
/*     */       }
/*     */     }
/* 396 */     checkNameAndExistence(paramString, paramBoolean);
/* 397 */     StandardLocation localStandardLocation = paramBoolean ? StandardLocation.SOURCE_OUTPUT : StandardLocation.CLASS_OUTPUT;
/* 398 */     Object localObject1 = paramBoolean ? JavaFileObject.Kind.SOURCE : JavaFileObject.Kind.CLASS;
/*     */ 
/* 403 */     Object localObject2 = this.fileManager
/* 403 */       .getJavaFileForOutput(localStandardLocation, paramString, (JavaFileObject.Kind)localObject1, null);
/*     */ 
/* 404 */     checkFileReopening((FileObject)localObject2, true);
/*     */ 
/* 406 */     if (this.lastRound) {
/* 407 */       this.log.warning("proc.file.create.last.round", new Object[] { paramString });
/*     */     }
/* 409 */     if (paramBoolean)
/* 410 */       this.aggregateGeneratedSourceNames.add(paramString);
/*     */     else
/* 412 */       this.aggregateGeneratedClassNames.add(paramString);
/* 413 */     this.openTypeNames.add(paramString);
/*     */ 
/* 415 */     return new FilerOutputJavaFileObject(paramString, (JavaFileObject)localObject2);
/*     */   }
/*     */ 
/*     */   public FileObject createResource(JavaFileManager.Location paramLocation, CharSequence paramCharSequence1, CharSequence paramCharSequence2, Element[] paramArrayOfElement)
/*     */     throws IOException
/*     */   {
/* 422 */     locationCheck(paramLocation);
/*     */ 
/* 424 */     String str = paramCharSequence1.toString();
/* 425 */     if (str.length() > 0) {
/* 426 */       checkName(str);
/*     */     }
/*     */ 
/* 429 */     FileObject localFileObject = this.fileManager
/* 429 */       .getFileForOutput(paramLocation, str, paramCharSequence2
/* 430 */       .toString(), null);
/* 431 */     checkFileReopening(localFileObject, true);
/*     */ 
/* 433 */     if ((localFileObject instanceof JavaFileObject)) {
/* 434 */       return new FilerOutputJavaFileObject(null, (JavaFileObject)localFileObject);
/*     */     }
/* 436 */     return new FilerOutputFileObject(null, localFileObject);
/*     */   }
/*     */ 
/*     */   private void locationCheck(JavaFileManager.Location paramLocation) {
/* 440 */     if ((paramLocation instanceof StandardLocation)) {
/* 441 */       StandardLocation localStandardLocation = (StandardLocation)paramLocation;
/* 442 */       if (!localStandardLocation.isOutputLocation())
/* 443 */         throw new IllegalArgumentException("Resource creation not supported in location " + localStandardLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FileObject getResource(JavaFileManager.Location paramLocation, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
/*     */     throws IOException
/*     */   {
/* 451 */     String str1 = paramCharSequence1.toString();
/* 452 */     if (str1.length() > 0)
/* 453 */       checkName(str1);
/*     */     FileObject localFileObject;
/* 466 */     if (paramLocation.isOutputLocation()) {
/* 467 */       localFileObject = this.fileManager.getFileForOutput(paramLocation, paramCharSequence1
/* 468 */         .toString(), paramCharSequence2
/* 469 */         .toString(), null);
/*     */     }
/*     */     else {
/* 472 */       localFileObject = this.fileManager.getFileForInput(paramLocation, paramCharSequence1
/* 473 */         .toString(), paramCharSequence2
/* 474 */         .toString());
/*     */     }
/* 476 */     if (localFileObject == null)
/*     */     {
/* 478 */       String str2 = paramCharSequence1 + "/" + paramCharSequence2;
/* 479 */       throw new FileNotFoundException(str2);
/*     */     }
/*     */ 
/* 483 */     checkFileReopening(localFileObject, false);
/* 484 */     return new FilerInputFileObject(localFileObject);
/*     */   }
/*     */ 
/*     */   private void checkName(String paramString) throws FilerException {
/* 488 */     checkName(paramString, false);
/*     */   }
/*     */ 
/*     */   private void checkName(String paramString, boolean paramBoolean) throws FilerException {
/* 492 */     if ((!SourceVersion.isName(paramString)) && (!isPackageInfo(paramString, paramBoolean))) {
/* 493 */       if (this.lint)
/* 494 */         this.log.warning("proc.illegal.file.name", new Object[] { paramString });
/* 495 */       throw new FilerException("Illegal name " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isPackageInfo(String paramString, boolean paramBoolean)
/*     */   {
/* 503 */     int i = paramString.lastIndexOf(".");
/* 504 */     if (i == -1) {
/* 505 */       return paramBoolean ? paramString.equals("package-info") : false;
/*     */     }
/*     */ 
/* 508 */     String str1 = paramString.substring(0, i);
/* 509 */     String str2 = paramString.substring(i + 1);
/* 510 */     return (SourceVersion.isName(str1)) && (str2.equals("package-info"));
/*     */   }
/*     */ 
/*     */   private void checkNameAndExistence(String paramString, boolean paramBoolean)
/*     */     throws FilerException
/*     */   {
/* 517 */     checkName(paramString, paramBoolean);
/* 518 */     if ((this.aggregateGeneratedSourceNames.contains(paramString)) || 
/* 519 */       (this.aggregateGeneratedClassNames
/* 519 */       .contains(paramString)))
/*     */     {
/* 520 */       if (this.lint)
/* 521 */         this.log.warning("proc.type.recreate", new Object[] { paramString });
/* 522 */       throw new FilerException("Attempt to recreate a file for type " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkFileReopening(FileObject paramFileObject, boolean paramBoolean)
/*     */     throws FilerException
/*     */   {
/* 531 */     for (FileObject localFileObject : this.fileObjectHistory) {
/* 532 */       if (this.fileManager.isSameFile(localFileObject, paramFileObject)) {
/* 533 */         if (this.lint)
/* 534 */           this.log.warning("proc.file.reopening", new Object[] { paramFileObject.getName() });
/* 535 */         throw new FilerException("Attempt to reopen a file for path " + paramFileObject.getName());
/*     */       }
/*     */     }
/* 538 */     if (paramBoolean)
/* 539 */       this.fileObjectHistory.add(paramFileObject);
/*     */   }
/*     */ 
/*     */   public boolean newFiles()
/*     */   {
/* 544 */     return (!this.generatedSourceNames.isEmpty()) || 
/* 544 */       (!this.generatedClasses
/* 544 */       .isEmpty());
/*     */   }
/*     */ 
/*     */   public Set<String> getGeneratedSourceNames() {
/* 548 */     return this.generatedSourceNames;
/*     */   }
/*     */ 
/*     */   public Set<JavaFileObject> getGeneratedSourceFileObjects() {
/* 552 */     return this.generatedSourceFileObjects;
/*     */   }
/*     */ 
/*     */   public Map<String, JavaFileObject> getGeneratedClasses() {
/* 556 */     return this.generatedClasses;
/*     */   }
/*     */ 
/*     */   public void warnIfUnclosedFiles() {
/* 560 */     if (!this.openTypeNames.isEmpty())
/* 561 */       this.log.warning("proc.unclosed.type.files", new Object[] { this.openTypeNames.toString() });
/*     */   }
/*     */ 
/*     */   public void newRound(Context paramContext)
/*     */   {
/* 568 */     this.context = paramContext;
/* 569 */     this.log = Log.instance(paramContext);
/* 570 */     clearRoundState();
/*     */   }
/*     */ 
/*     */   void setLastRound(boolean paramBoolean) {
/* 574 */     this.lastRound = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 578 */     clearRoundState();
/*     */ 
/* 580 */     this.fileObjectHistory.clear();
/* 581 */     this.openTypeNames.clear();
/* 582 */     this.aggregateGeneratedSourceNames.clear();
/* 583 */     this.aggregateGeneratedClassNames.clear();
/*     */   }
/*     */ 
/*     */   private void clearRoundState() {
/* 587 */     this.generatedSourceNames.clear();
/* 588 */     this.generatedSourceFileObjects.clear();
/* 589 */     this.generatedClasses.clear();
/*     */   }
/*     */ 
/*     */   public void displayState()
/*     */   {
/* 596 */     PrintWriter localPrintWriter = (PrintWriter)this.context.get(Log.outKey);
/* 597 */     localPrintWriter.println("File Object History : " + this.fileObjectHistory);
/* 598 */     localPrintWriter.println("Open Type Names     : " + this.openTypeNames);
/* 599 */     localPrintWriter.println("Gen. Src Names      : " + this.generatedSourceNames);
/* 600 */     localPrintWriter.println("Gen. Cls Names      : " + this.generatedClasses.keySet());
/* 601 */     localPrintWriter.println("Agg. Gen. Src Names : " + this.aggregateGeneratedSourceNames);
/* 602 */     localPrintWriter.println("Agg. Gen. Cls Names : " + this.aggregateGeneratedClassNames);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 606 */     return "javac Filer";
/*     */   }
/*     */ 
/*     */   private void closeFileObject(String paramString, FileObject paramFileObject)
/*     */   {
/* 620 */     if (paramString != null) {
/* 621 */       if (!(paramFileObject instanceof JavaFileObject))
/* 622 */         throw new AssertionError("JavaFileOject not found for " + paramFileObject);
/* 623 */       JavaFileObject localJavaFileObject = (JavaFileObject)paramFileObject;
/* 624 */       switch (1.$SwitchMap$javax$tools$JavaFileObject$Kind[localJavaFileObject.getKind().ordinal()]) {
/*     */       case 1:
/* 626 */         this.generatedSourceNames.add(paramString);
/* 627 */         this.generatedSourceFileObjects.add(localJavaFileObject);
/* 628 */         this.openTypeNames.remove(paramString);
/* 629 */         break;
/*     */       case 2:
/* 632 */         this.generatedClasses.put(paramString, localJavaFileObject);
/* 633 */         this.openTypeNames.remove(paramString);
/* 634 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerInputFileObject extends ForwardingFileObject<FileObject>
/*     */   {
/*     */     FilerInputFileObject(FileObject arg2)
/*     */     {
/* 191 */       super();
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream() throws IOException
/*     */     {
/* 196 */       throw new IllegalStateException("FileObject was not opened for writing.");
/*     */     }
/*     */ 
/*     */     public Writer openWriter() throws IOException
/*     */     {
/* 201 */       throw new IllegalStateException("FileObject was not opened for writing.");
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/* 206 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerInputJavaFileObject extends JavacFiler.FilerInputFileObject implements JavaFileObject {
/*     */     private final JavaFileObject javaFileObject;
/*     */ 
/* 213 */     FilerInputJavaFileObject(JavaFileObject arg2) { super(localFileObject);
/* 214 */       this.javaFileObject = localFileObject; }
/*     */ 
/*     */     public JavaFileObject.Kind getKind()
/*     */     {
/* 218 */       return this.javaFileObject.getKind();
/*     */     }
/*     */ 
/*     */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */     {
/* 223 */       return this.javaFileObject.isNameCompatible(paramString, paramKind);
/*     */     }
/*     */ 
/*     */     public NestingKind getNestingKind() {
/* 227 */       return this.javaFileObject.getNestingKind();
/*     */     }
/*     */ 
/*     */     public Modifier getAccessLevel() {
/* 231 */       return this.javaFileObject.getAccessLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerOutputFileObject extends ForwardingFileObject<FileObject>
/*     */   {
/* 115 */     private boolean opened = false;
/*     */     private String name;
/*     */ 
/*     */     FilerOutputFileObject(String paramFileObject, FileObject arg3)
/*     */     {
/* 119 */       super();
/* 120 */       this.name = paramFileObject;
/*     */     }
/*     */ 
/*     */     public synchronized OutputStream openOutputStream() throws IOException
/*     */     {
/* 125 */       if (this.opened)
/* 126 */         throw new IOException("Output stream or writer has already been opened.");
/* 127 */       this.opened = true;
/* 128 */       return new JavacFiler.FilerOutputStream(JavacFiler.this, this.name, this.fileObject);
/*     */     }
/*     */ 
/*     */     public synchronized Writer openWriter() throws IOException
/*     */     {
/* 133 */       if (this.opened)
/* 134 */         throw new IOException("Output stream or writer has already been opened.");
/* 135 */       this.opened = true;
/* 136 */       return new JavacFiler.FilerWriter(JavacFiler.this, this.name, this.fileObject);
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream()
/*     */       throws IOException
/*     */     {
/* 142 */       throw new IllegalStateException("FileObject was not opened for reading.");
/*     */     }
/*     */ 
/*     */     public Reader openReader(boolean paramBoolean) throws IOException
/*     */     {
/* 147 */       throw new IllegalStateException("FileObject was not opened for reading.");
/*     */     }
/*     */ 
/*     */     public CharSequence getCharContent(boolean paramBoolean) throws IOException
/*     */     {
/* 152 */       throw new IllegalStateException("FileObject was not opened for reading.");
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/* 157 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerOutputJavaFileObject extends JavacFiler.FilerOutputFileObject implements JavaFileObject {
/*     */     private final JavaFileObject javaFileObject;
/*     */ 
/* 164 */     FilerOutputJavaFileObject(String paramJavaFileObject, JavaFileObject arg3) { super(paramJavaFileObject, localFileObject);
/* 165 */       this.javaFileObject = localFileObject; }
/*     */ 
/*     */     public JavaFileObject.Kind getKind()
/*     */     {
/* 169 */       return this.javaFileObject.getKind();
/*     */     }
/*     */ 
/*     */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */     {
/* 174 */       return this.javaFileObject.isNameCompatible(paramString, paramKind);
/*     */     }
/*     */ 
/*     */     public NestingKind getNestingKind() {
/* 178 */       return this.javaFileObject.getNestingKind();
/*     */     }
/*     */ 
/*     */     public Modifier getAccessLevel() {
/* 182 */       return this.javaFileObject.getAccessLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerOutputStream extends FilterOutputStream
/*     */   {
/*     */     String typeName;
/*     */     FileObject fileObject;
/* 244 */     boolean closed = false;
/*     */ 
/*     */     FilerOutputStream(String paramFileObject, FileObject arg3)
/*     */       throws IOException
/*     */     {
/* 251 */       super();
/* 252 */       this.typeName = paramFileObject;
/* 253 */       this.fileObject = localObject;
/*     */     }
/*     */ 
/*     */     public synchronized void close() throws IOException {
/* 257 */       if (!this.closed) {
/* 258 */         this.closed = true;
/*     */ 
/* 264 */         JavacFiler.this.closeFileObject(this.typeName, this.fileObject);
/* 265 */         this.out.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class FilerWriter extends FilterWriter
/*     */   {
/*     */     String typeName;
/*     */     FileObject fileObject;
/* 278 */     boolean closed = false;
/*     */ 
/*     */     FilerWriter(String paramFileObject, FileObject arg3)
/*     */       throws IOException
/*     */     {
/* 286 */       super();
/* 287 */       this.typeName = paramFileObject;
/* 288 */       this.fileObject = localObject;
/*     */     }
/*     */ 
/*     */     public synchronized void close() throws IOException {
/* 292 */       if (!this.closed) {
/* 293 */         this.closed = true;
/*     */ 
/* 299 */         JavacFiler.this.closeFileObject(this.typeName, this.fileObject);
/* 300 */         this.out.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.JavacFiler
 * JD-Core Version:    0.6.2
 */