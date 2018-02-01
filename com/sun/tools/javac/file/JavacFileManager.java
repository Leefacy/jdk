/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Factory;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class JavacFileManager extends BaseFileManager
/*     */   implements StandardJavaFileManager
/*     */ {
/*     */   private FSInfo fsInfo;
/*     */   private boolean contextUseOptimizedZip;
/*     */   private ZipFileIndexCache zipFileIndexCache;
/*  90 */   private final Set<JavaFileObject.Kind> sourceOrClass = EnumSet.of(JavaFileObject.Kind.SOURCE, JavaFileObject.Kind.CLASS)
/*  90 */     ;
/*     */   protected boolean mmappedIO;
/*     */   protected boolean symbolFileEnabled;
/*     */   protected SortFiles sortFiles;
/* 367 */   private static final boolean fileSystemIsCaseSensitive = File.separatorChar == '/';
/*     */ 
/* 448 */   Map<File, Archive> archives = new HashMap();
/*     */ 
/* 450 */   private static final String[] symbolFileLocation = { "lib", "ct.sym" };
/* 451 */   private static final RelativePath.RelativeDirectory symbolFilePrefix = new RelativePath.RelativeDirectory("META-INF/sym/rt.jar/");
/*     */   private String defaultEncodingName;
/*     */ 
/*     */   public static char[] toArray(CharBuffer paramCharBuffer)
/*     */   {
/*  78 */     if (paramCharBuffer.hasArray()) {
/*  79 */       return ((CharBuffer)paramCharBuffer.compact().flip()).array();
/*     */     }
/*  81 */     return paramCharBuffer.toString().toCharArray();
/*     */   }
/*     */ 
/*     */   public static void preRegister(Context paramContext)
/*     */   {
/* 113 */     paramContext.put(JavaFileManager.class, new Context.Factory() {
/*     */       public JavaFileManager make(Context paramAnonymousContext) {
/* 115 */         return new JavacFileManager(paramAnonymousContext, true, null);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public JavacFileManager(Context paramContext, boolean paramBoolean, Charset paramCharset)
/*     */   {
/* 125 */     super(paramCharset);
/* 126 */     if (paramBoolean)
/* 127 */       paramContext.put(JavaFileManager.class, this);
/* 128 */     setContext(paramContext);
/*     */   }
/*     */ 
/*     */   public void setContext(Context paramContext)
/*     */   {
/* 136 */     super.setContext(paramContext);
/*     */ 
/* 138 */     this.fsInfo = FSInfo.instance(paramContext);
/*     */ 
/* 140 */     this.contextUseOptimizedZip = this.options.getBoolean("useOptimizedZip", true);
/* 141 */     if (this.contextUseOptimizedZip) {
/* 142 */       this.zipFileIndexCache = ZipFileIndexCache.getSharedInstance();
/*     */     }
/* 144 */     this.mmappedIO = this.options.isSet("mmappedIO");
/* 145 */     this.symbolFileEnabled = (!this.options.isSet("ignore.symbol.file"));
/*     */ 
/* 147 */     String str = this.options.get("sortFiles");
/* 148 */     if (str != null)
/* 149 */       this.sortFiles = (str.equals("reverse") ? SortFiles.REVERSE : SortFiles.FORWARD);
/*     */   }
/*     */ 
/*     */   public void setSymbolFileEnabled(boolean paramBoolean)
/*     */   {
/* 157 */     this.symbolFileEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isDefaultBootClassPath()
/*     */   {
/* 162 */     return this.locations.isDefaultBootClassPath();
/*     */   }
/*     */ 
/*     */   public JavaFileObject getFileForInput(String paramString) {
/* 166 */     return getRegularFile(new File(paramString));
/*     */   }
/*     */ 
/*     */   public JavaFileObject getRegularFile(File paramFile) {
/* 170 */     return new RegularFileObject(this, paramFile);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getFileForOutput(String paramString, JavaFileObject.Kind paramKind, JavaFileObject paramJavaFileObject)
/*     */     throws IOException
/*     */   {
/* 178 */     return getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramString, paramKind, paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> paramIterable) {
/* 182 */     ListBuffer localListBuffer = new ListBuffer();
/* 183 */     for (String str : paramIterable)
/* 184 */       localListBuffer.append(new File((String)nullCheck(str)));
/* 185 */     return getJavaFileObjectsFromFiles(localListBuffer.toList());
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjects(String[] paramArrayOfString) {
/* 189 */     return getJavaFileObjectsFromStrings(Arrays.asList((Object[])nullCheck(paramArrayOfString)));
/*     */   }
/*     */ 
/*     */   private static boolean isValidName(String paramString)
/*     */   {
/* 198 */     for (String str : paramString.split("\\.", -1)) {
/* 199 */       if (!SourceVersion.isIdentifier(str))
/* 200 */         return false;
/*     */     }
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   private static void validateClassName(String paramString) {
/* 206 */     if (!isValidName(paramString))
/* 207 */       throw new IllegalArgumentException("Invalid class name: " + paramString);
/*     */   }
/*     */ 
/*     */   private static void validatePackageName(String paramString) {
/* 211 */     if ((paramString.length() > 0) && (!isValidName(paramString)))
/* 212 */       throw new IllegalArgumentException("Invalid packageName name: " + paramString);
/*     */   }
/*     */ 
/*     */   public static void testName(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*     */     try
/*     */     {
/* 220 */       validatePackageName(paramString);
/* 221 */       if (!paramBoolean1)
/* 222 */         throw new AssertionError("Invalid package name accepted: " + paramString);
/* 223 */       printAscii("Valid package name: \"%s\"", new Object[] { paramString });
/*     */     } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 225 */       if (paramBoolean1)
/* 226 */         throw new AssertionError("Valid package name rejected: " + paramString);
/* 227 */       printAscii("Invalid package name: \"%s\"", new Object[] { paramString });
/*     */     }
/*     */     try {
/* 230 */       validateClassName(paramString);
/* 231 */       if (!paramBoolean2)
/* 232 */         throw new AssertionError("Invalid class name accepted: " + paramString);
/* 233 */       printAscii("Valid class name: \"%s\"", new Object[] { paramString });
/*     */     } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 235 */       if (paramBoolean2)
/* 236 */         throw new AssertionError("Valid class name rejected: " + paramString);
/* 237 */       printAscii("Invalid class name: \"%s\"", new Object[] { paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void printAscii(String paramString, Object[] paramArrayOfObject)
/*     */   {
/*     */     String str;
/*     */     try {
/* 245 */       str = new String(String.format(null, paramString, paramArrayOfObject).getBytes("US-ASCII"), "US-ASCII");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 247 */       throw new AssertionError(localUnsupportedEncodingException);
/*     */     }
/* 249 */     System.out.println(str);
/*     */   }
/*     */ 
/*     */   private void listDirectory(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer)
/*     */   {
/* 262 */     File localFile1 = paramRelativeDirectory.getFile(paramFile);
/* 263 */     if (!caseMapCheck(localFile1, paramRelativeDirectory)) {
/* 264 */       return;
/*     */     }
/* 266 */     File[] arrayOfFile1 = localFile1.listFiles();
/* 267 */     if (arrayOfFile1 == null) {
/* 268 */       return;
/*     */     }
/* 270 */     if (this.sortFiles != null) {
/* 271 */       Arrays.sort(arrayOfFile1, this.sortFiles);
/*     */     }
/* 273 */     for (File localFile2 : arrayOfFile1) {
/* 274 */       String str = localFile2.getName();
/* 275 */       if (localFile2.isDirectory()) {
/* 276 */         if ((paramBoolean) && (SourceVersion.isIdentifier(str))) {
/* 277 */           listDirectory(paramFile, new RelativePath.RelativeDirectory(paramRelativeDirectory, str), paramSet, paramBoolean, paramListBuffer);
/*     */         }
/*     */ 
/*     */       }
/* 284 */       else if (isValidFile(str, paramSet)) {
/* 285 */         RegularFileObject localRegularFileObject = new RegularFileObject(this, str, new File(localFile1, str));
/*     */ 
/* 287 */         paramListBuffer.append(localRegularFileObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void listArchive(Archive paramArchive, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer)
/*     */   {
/* 303 */     List localList = paramArchive.getFiles(paramRelativeDirectory);
/*     */     Object localObject;
/* 304 */     if (localList != null) {
/* 305 */       for (; !localList.isEmpty(); localList = localList.tail) {
/* 306 */         localObject = (String)localList.head;
/* 307 */         if (isValidFile((String)localObject, paramSet)) {
/* 308 */           paramListBuffer.append(paramArchive.getFileObject(paramRelativeDirectory, (String)localObject));
/*     */         }
/*     */       }
/*     */     }
/* 312 */     if (paramBoolean)
/* 313 */       for (localObject = paramArchive.getSubdirectories().iterator(); ((Iterator)localObject).hasNext(); ) { RelativePath.RelativeDirectory localRelativeDirectory = (RelativePath.RelativeDirectory)((Iterator)localObject).next();
/* 314 */         if (paramRelativeDirectory.contains(localRelativeDirectory))
/*     */         {
/* 318 */           listArchive(paramArchive, localRelativeDirectory, paramSet, false, paramListBuffer);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void listContainer(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer)
/*     */   {
/* 334 */     Archive localArchive = (Archive)this.archives.get(paramFile);
/* 335 */     if (localArchive == null)
/*     */     {
/* 337 */       if (this.fsInfo.isDirectory(paramFile)) {
/* 338 */         listDirectory(paramFile, paramRelativeDirectory, paramSet, paramBoolean, paramListBuffer);
/*     */ 
/* 343 */         return;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 348 */         localArchive = openArchive(paramFile);
/*     */       } catch (IOException localIOException) {
/* 350 */         this.log.error("error.reading.file", new Object[] { paramFile, 
/* 351 */           getMessage(localIOException) });
/*     */ 
/* 352 */         return;
/*     */       }
/*     */     }
/* 355 */     listArchive(localArchive, paramRelativeDirectory, paramSet, paramBoolean, paramListBuffer);
/*     */   }
/*     */ 
/*     */   private boolean isValidFile(String paramString, Set<JavaFileObject.Kind> paramSet)
/*     */   {
/* 363 */     JavaFileObject.Kind localKind = getKind(paramString);
/* 364 */     return paramSet.contains(localKind);
/*     */   }
/*     */ 
/*     */   private boolean caseMapCheck(File paramFile, RelativePath paramRelativePath)
/*     */   {
/* 375 */     if (fileSystemIsCaseSensitive) return true;
/*     */ 
/*     */     String str;
/*     */     try
/*     */     {
/* 380 */       str = paramFile.getCanonicalPath();
/*     */     } catch (IOException localIOException) {
/* 382 */       return false;
/*     */     }
/* 384 */     char[] arrayOfChar1 = str.toCharArray();
/* 385 */     char[] arrayOfChar2 = paramRelativePath.path.toCharArray();
/* 386 */     int i = arrayOfChar1.length - 1;
/* 387 */     int j = arrayOfChar2.length - 1;
/* 388 */     while ((i >= 0) && (j >= 0)) {
/* 389 */       while ((i >= 0) && (arrayOfChar1[i] == File.separatorChar)) i--;
/* 390 */       while ((j >= 0) && (arrayOfChar2[j] == '/')) j--;
/* 391 */       if ((i >= 0) && (j >= 0)) {
/* 392 */         if (arrayOfChar1[i] != arrayOfChar2[j]) return false;
/* 393 */         i--;
/* 394 */         j--;
/*     */       }
/*     */     }
/* 397 */     return j < 0;
/*     */   }
/*     */ 
/*     */   protected Archive openArchive(File paramFile)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 462 */       return openArchive(paramFile, this.contextUseOptimizedZip);
/*     */     } catch (IOException localIOException) {
/* 464 */       if ((localIOException instanceof ZipFileIndex.ZipFormatException)) {
/* 465 */         return openArchive(paramFile, false);
/*     */       }
/* 467 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Archive openArchive(File paramFile, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 475 */     File localFile1 = paramFile;
/*     */     Object localObject1;
/*     */     String str;
/* 476 */     if ((this.symbolFileEnabled) && (this.locations.isDefaultBootClassPathRtJar(paramFile))) {
/* 477 */       localObject1 = paramFile.getParentFile().getParentFile();
/* 478 */       if (new File(((File)localObject1).getName()).equals(new File("jre"))) {
/* 479 */         localObject1 = ((File)localObject1).getParentFile();
/*     */       }
/* 481 */       for (str : symbolFileLocation) {
/* 482 */         localObject1 = new File((File)localObject1, str);
/*     */       }
/* 484 */       if (((File)localObject1).exists()) {
/* 485 */         paramFile = (File)localObject1;
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 491 */       ??? = null;
/*     */ 
/* 493 */       ??? = 0;
/* 494 */       Object localObject3 = null;
/*     */       boolean bool;
/* 496 */       if (!paramBoolean) {
/* 497 */         ??? = new ZipFile(paramFile);
/*     */       } else {
/* 499 */         bool = this.options.isSet("usezipindex");
/* 500 */         localObject3 = this.options.get("java.io.tmpdir");
/* 501 */         str = this.options.get("cachezipindexdir");
/*     */ 
/* 503 */         if ((str != null) && (str.length() != 0)) {
/* 504 */           if (str.startsWith("\"")) {
/* 505 */             if (str.endsWith("\"")) {
/* 506 */               str = str.substring(1, str.length() - 1);
/*     */             }
/*     */             else {
/* 509 */               str = str.substring(1);
/*     */             }
/*     */           }
/*     */ 
/* 513 */           File localFile2 = new File(str);
/* 514 */           if ((localFile2.exists()) && (localFile2.canWrite())) {
/* 515 */             localObject3 = str;
/* 516 */             if ((!((String)localObject3).endsWith("/")) && 
/* 517 */               (!((String)localObject3)
/* 517 */               .endsWith(File.separator)))
/*     */             {
/* 518 */               localObject3 = (String)localObject3 + File.separator;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 524 */       if (localFile1 == paramFile) {
/* 525 */         if (!paramBoolean) {
/* 526 */           localObject1 = new ZipArchive(this, (ZipFile)???);
/*     */         }
/*     */         else {
/* 529 */           localObject1 = new ZipFileIndexArchive(this, this.zipFileIndexCache
/* 529 */             .getZipFileIndex(paramFile, null, bool, (String)localObject3, this.options
/* 533 */             .isSet("writezipindexfiles")));
/*     */         }
/*     */ 
/*     */       }
/* 536 */       else if (!paramBoolean) {
/* 537 */         localObject1 = new SymbolArchive(this, localFile1, (ZipFile)???, symbolFilePrefix);
/*     */       }
/*     */       else {
/* 540 */         localObject1 = new ZipFileIndexArchive(this, this.zipFileIndexCache
/* 540 */           .getZipFileIndex(paramFile, symbolFilePrefix, bool, (String)localObject3, this.options
/* 544 */           .isSet("writezipindexfiles")));
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException)
/*     */     {
/* 548 */       localObject1 = new MissingArchive(paramFile);
/*     */     } catch (ZipFileIndex.ZipFormatException localZipFormatException) {
/* 550 */       throw localZipFormatException;
/*     */     } catch (IOException localIOException) {
/* 552 */       if (paramFile.exists())
/* 553 */         this.log.error("error.reading.file", new Object[] { paramFile, getMessage(localIOException) });
/* 554 */       localObject1 = new MissingArchive(paramFile);
/*     */     }
/*     */ 
/* 557 */     this.archives.put(localFile1, localObject1);
/* 558 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 564 */     this.contentCache.clear();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 571 */     for (Iterator localIterator = this.archives.values().iterator(); localIterator.hasNext(); ) {
/* 572 */       Archive localArchive = (Archive)localIterator.next();
/* 573 */       localIterator.remove();
/*     */       try {
/* 575 */         localArchive.close();
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getDefaultEncodingName() {
/* 583 */     if (this.defaultEncodingName == null) {
/* 584 */       this.defaultEncodingName = new OutputStreamWriter(new ByteArrayOutputStream())
/* 585 */         .getEncoding();
/*     */     }
/* 587 */     return this.defaultEncodingName;
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader(JavaFileManager.Location paramLocation) {
/* 591 */     nullCheck(paramLocation);
/* 592 */     Iterable localIterable = getLocation(paramLocation);
/* 593 */     if (localIterable == null)
/* 594 */       return null;
/* 595 */     ListBuffer localListBuffer = new ListBuffer();
/* 596 */     for (File localFile : localIterable) {
/*     */       try {
/* 598 */         localListBuffer.append(localFile.toURI().toURL());
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 600 */         throw new AssertionError(localMalformedURLException);
/*     */       }
/*     */     }
/*     */ 
/* 604 */     return getClassLoader((URL[])localListBuffer.toArray(new URL[localListBuffer.size()]));
/*     */   }
/*     */ 
/*     */   public Iterable<JavaFileObject> list(JavaFileManager.Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 614 */     nullCheck(paramString);
/* 615 */     nullCheck(paramSet);
/*     */ 
/* 617 */     Iterable localIterable = getLocation(paramLocation);
/* 618 */     if (localIterable == null)
/* 619 */       return List.nil();
/* 620 */     RelativePath.RelativeDirectory localRelativeDirectory = RelativePath.RelativeDirectory.forPackage(paramString);
/* 621 */     ListBuffer localListBuffer = new ListBuffer();
/*     */ 
/* 623 */     for (File localFile : localIterable)
/* 624 */       listContainer(localFile, localRelativeDirectory, paramSet, paramBoolean, localListBuffer);
/* 625 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public String inferBinaryName(JavaFileManager.Location paramLocation, JavaFileObject paramJavaFileObject) {
/* 629 */     paramJavaFileObject.getClass();
/* 630 */     paramLocation.getClass();
/*     */ 
/* 632 */     Iterable localIterable = getLocation(paramLocation);
/* 633 */     if (localIterable == null) {
/* 634 */       return null;
/*     */     }
/*     */ 
/* 637 */     if ((paramJavaFileObject instanceof BaseFileObject)) {
/* 638 */       return ((BaseFileObject)paramJavaFileObject).inferBinaryName(localIterable);
/*     */     }
/* 640 */     throw new IllegalArgumentException(paramJavaFileObject.getClass().getName());
/*     */   }
/*     */ 
/*     */   public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2) {
/* 644 */     nullCheck(paramFileObject1);
/* 645 */     nullCheck(paramFileObject2);
/* 646 */     if (!(paramFileObject1 instanceof BaseFileObject))
/* 647 */       throw new IllegalArgumentException("Not supported: " + paramFileObject1);
/* 648 */     if (!(paramFileObject2 instanceof BaseFileObject))
/* 649 */       throw new IllegalArgumentException("Not supported: " + paramFileObject2);
/* 650 */     return paramFileObject1.equals(paramFileObject2);
/*     */   }
/*     */ 
/*     */   public boolean hasLocation(JavaFileManager.Location paramLocation) {
/* 654 */     return getLocation(paramLocation) != null;
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForInput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind)
/*     */     throws IOException
/*     */   {
/* 662 */     nullCheck(paramLocation);
/*     */ 
/* 664 */     nullCheck(paramString);
/* 665 */     nullCheck(paramKind);
/* 666 */     if (!this.sourceOrClass.contains(paramKind))
/* 667 */       throw new IllegalArgumentException("Invalid kind: " + paramKind);
/* 668 */     return getFileForInput(paramLocation, RelativePath.RelativeFile.forClass(paramString, paramKind));
/*     */   }
/*     */ 
/*     */   public FileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 676 */     nullCheck(paramLocation);
/*     */ 
/* 678 */     nullCheck(paramString1);
/* 679 */     if (!isRelativeUri(paramString2)) {
/* 680 */       throw new IllegalArgumentException("Invalid relative name: " + paramString2);
/*     */     }
/*     */ 
/* 683 */     RelativePath.RelativeFile localRelativeFile = paramString1.length() == 0 ? new RelativePath.RelativeFile(paramString2) : new RelativePath.RelativeFile(
/* 683 */       RelativePath.RelativeDirectory.forPackage(paramString1), 
/* 683 */       paramString2);
/* 684 */     return getFileForInput(paramLocation, localRelativeFile);
/*     */   }
/*     */ 
/*     */   private JavaFileObject getFileForInput(JavaFileManager.Location paramLocation, RelativePath.RelativeFile paramRelativeFile) throws IOException {
/* 688 */     Iterable localIterable = getLocation(paramLocation);
/* 689 */     if (localIterable == null) {
/* 690 */       return null;
/*     */     }
/* 692 */     for (File localFile1 : localIterable) {
/* 693 */       Archive localArchive = (Archive)this.archives.get(localFile1);
/* 694 */       if (localArchive == null) {
/* 695 */         if (this.fsInfo.isDirectory(localFile1)) {
/* 696 */           File localFile2 = paramRelativeFile.getFile(localFile1);
/* 697 */           if (localFile2.exists())
/* 698 */             return new RegularFileObject(this, localFile2);
/*     */         }
/*     */         else
/*     */         {
/* 702 */           localArchive = openArchive(localFile1);
/*     */         }
/*     */       }
/* 705 */       else if (localArchive.contains(paramRelativeFile)) {
/* 706 */         return localArchive.getFileObject(paramRelativeFile.dirname(), paramRelativeFile.basename());
/*     */       }
/*     */     }
/* 709 */     return null;
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForOutput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 718 */     nullCheck(paramLocation);
/*     */ 
/* 720 */     nullCheck(paramString);
/* 721 */     nullCheck(paramKind);
/* 722 */     if (!this.sourceOrClass.contains(paramKind))
/* 723 */       throw new IllegalArgumentException("Invalid kind: " + paramKind);
/* 724 */     return getFileForOutput(paramLocation, RelativePath.RelativeFile.forClass(paramString, paramKind), paramFileObject);
/*     */   }
/*     */ 
/*     */   public FileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 733 */     nullCheck(paramLocation);
/*     */ 
/* 735 */     nullCheck(paramString1);
/* 736 */     if (!isRelativeUri(paramString2)) {
/* 737 */       throw new IllegalArgumentException("Invalid relative name: " + paramString2);
/*     */     }
/*     */ 
/* 740 */     RelativePath.RelativeFile localRelativeFile = paramString1.length() == 0 ? new RelativePath.RelativeFile(paramString2) : new RelativePath.RelativeFile(
/* 740 */       RelativePath.RelativeDirectory.forPackage(paramString1), 
/* 740 */       paramString2);
/* 741 */     return getFileForOutput(paramLocation, localRelativeFile, paramFileObject);
/*     */   }
/*     */ 
/*     */   private JavaFileObject getFileForOutput(JavaFileManager.Location paramLocation, RelativePath.RelativeFile paramRelativeFile, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject1;
/* 750 */     if (paramLocation == StandardLocation.CLASS_OUTPUT) {
/* 751 */       if (getClassOutDir() != null) {
/* 752 */         localObject1 = getClassOutDir();
/*     */       } else {
/* 754 */         localObject2 = null;
/* 755 */         if ((paramFileObject != null) && ((paramFileObject instanceof RegularFileObject))) {
/* 756 */           localObject2 = ((RegularFileObject)paramFileObject).file.getParentFile();
/*     */         }
/* 758 */         return new RegularFileObject(this, new File((File)localObject2, paramRelativeFile.basename()));
/*     */       }
/* 760 */     } else if (paramLocation == StandardLocation.SOURCE_OUTPUT) {
/* 761 */       localObject1 = getSourceOutDir() != null ? getSourceOutDir() : getClassOutDir();
/*     */     } else {
/* 763 */       localObject2 = this.locations.getLocation(paramLocation);
/* 764 */       localObject1 = null;
/* 765 */       Iterator localIterator = ((Iterable)localObject2).iterator(); if (localIterator.hasNext()) { File localFile = (File)localIterator.next();
/* 766 */         localObject1 = localFile;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 771 */     Object localObject2 = paramRelativeFile.getFile((File)localObject1);
/* 772 */     return new RegularFileObject(this, (File)localObject2);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> paramIterable)
/*     */   {
/*     */     ArrayList localArrayList;
/* 780 */     if ((paramIterable instanceof Collection))
/* 781 */       localArrayList = new ArrayList(((Collection)paramIterable).size());
/*     */     else
/* 783 */       localArrayList = new ArrayList();
/* 784 */     for (File localFile : paramIterable)
/* 785 */       localArrayList.add(new RegularFileObject(this, (File)nullCheck(localFile)));
/* 786 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjects(File[] paramArrayOfFile) {
/* 790 */     return getJavaFileObjectsFromFiles(Arrays.asList((Object[])nullCheck(paramArrayOfFile)));
/*     */   }
/*     */ 
/*     */   public void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends File> paramIterable)
/*     */     throws IOException
/*     */   {
/* 797 */     nullCheck(paramLocation);
/* 798 */     this.locations.setLocation(paramLocation, paramIterable);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends File> getLocation(JavaFileManager.Location paramLocation) {
/* 802 */     nullCheck(paramLocation);
/* 803 */     return this.locations.getLocation(paramLocation);
/*     */   }
/*     */ 
/*     */   private File getClassOutDir() {
/* 807 */     return this.locations.getOutputLocation(StandardLocation.CLASS_OUTPUT);
/*     */   }
/*     */ 
/*     */   private File getSourceOutDir() {
/* 811 */     return this.locations.getOutputLocation(StandardLocation.SOURCE_OUTPUT);
/*     */   }
/*     */ 
/*     */   protected static boolean isRelativeUri(URI paramURI)
/*     */   {
/* 822 */     if (paramURI.isAbsolute())
/* 823 */       return false;
/* 824 */     String str = paramURI.normalize().getPath();
/* 825 */     if (str.length() == 0)
/* 826 */       return false;
/* 827 */     if (!str.equals(paramURI.getPath()))
/* 828 */       return false;
/* 829 */     if ((str.startsWith("/")) || (str.startsWith("./")) || (str.startsWith("../")))
/* 830 */       return false;
/* 831 */     return true;
/*     */   }
/*     */ 
/*     */   protected static boolean isRelativeUri(String paramString)
/*     */   {
/*     */     try {
/* 837 */       return isRelativeUri(new URI(paramString)); } catch (URISyntaxException localURISyntaxException) {
/*     */     }
/* 839 */     return false;
/*     */   }
/*     */ 
/*     */   public static String getRelativeName(File paramFile)
/*     */   {
/* 855 */     if (!paramFile.isAbsolute()) {
/* 856 */       String str = paramFile.getPath().replace(File.separatorChar, '/');
/* 857 */       if (isRelativeUri(str))
/* 858 */         return str;
/*     */     }
/* 860 */     throw new IllegalArgumentException("Invalid relative path: " + paramFile);
/*     */   }
/*     */ 
/*     */   public static String getMessage(IOException paramIOException)
/*     */   {
/* 873 */     String str = paramIOException.getLocalizedMessage();
/* 874 */     if (str != null)
/* 875 */       return str;
/* 876 */     str = paramIOException.getMessage();
/* 877 */     if (str != null)
/* 878 */       return str;
/* 879 */     return paramIOException.toString();
/*     */   }
/*     */ 
/*     */   public static abstract interface Archive
/*     */   {
/*     */     public abstract void close()
/*     */       throws IOException;
/*     */ 
/*     */     public abstract boolean contains(RelativePath paramRelativePath);
/*     */ 
/*     */     public abstract JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString);
/*     */ 
/*     */     public abstract List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory);
/*     */ 
/*     */     public abstract Set<RelativePath.RelativeDirectory> getSubdirectories();
/*     */   }
/*     */ 
/*     */   public class MissingArchive
/*     */     implements JavacFileManager.Archive
/*     */   {
/*     */     final File zipFileName;
/*     */ 
/*     */     public MissingArchive(File arg2)
/*     */     {
/*     */       Object localObject;
/* 419 */       this.zipFileName = localObject;
/*     */     }
/*     */     public boolean contains(RelativePath paramRelativePath) {
/* 422 */       return false;
/*     */     }
/*     */ 
/*     */     public void close() {
/*     */     }
/*     */ 
/*     */     public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/* 429 */       return null;
/*     */     }
/*     */ 
/*     */     public List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/* 433 */       return List.nil();
/*     */     }
/*     */ 
/*     */     public Set<RelativePath.RelativeDirectory> getSubdirectories() {
/* 437 */       return Collections.emptySet();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 442 */       return "MissingArchive[" + this.zipFileName + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static abstract enum SortFiles
/*     */     implements Comparator<File>
/*     */   {
/*  96 */     FORWARD, 
/*     */ 
/* 101 */     REVERSE;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.JavacFileManager
 * JD-Core Version:    0.6.2
 */