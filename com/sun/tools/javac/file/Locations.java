/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class Locations
/*     */ {
/*     */   private Log log;
/*     */   private Options options;
/*     */   private Lint lint;
/*     */   private FSInfo fsInfo;
/*     */   private boolean warn;
/*  89 */   private boolean inited = false;
/*     */   Map<JavaFileManager.Location, LocationHandler> handlersForLocation;
/*     */   Map<Option, LocationHandler> handlersForOption;
/*     */ 
/*     */   public Locations()
/*     */   {
/*  92 */     initHandlers();
/*     */   }
/*     */ 
/*     */   public void update(Log paramLog, Options paramOptions, Lint paramLint, FSInfo paramFSInfo) {
/*  96 */     this.log = paramLog;
/*  97 */     this.options = paramOptions;
/*  98 */     this.lint = paramLint;
/*  99 */     this.fsInfo = paramFSInfo;
/*     */   }
/*     */ 
/*     */   public Collection<File> bootClassPath() {
/* 103 */     return getLocation(StandardLocation.PLATFORM_CLASS_PATH);
/*     */   }
/*     */ 
/*     */   public boolean isDefaultBootClassPath()
/*     */   {
/* 108 */     BootClassPathLocationHandler localBootClassPathLocationHandler = (BootClassPathLocationHandler)getHandler(StandardLocation.PLATFORM_CLASS_PATH);
/*     */ 
/* 109 */     return localBootClassPathLocationHandler.isDefault();
/*     */   }
/*     */ 
/*     */   boolean isDefaultBootClassPathRtJar(File paramFile)
/*     */   {
/* 114 */     BootClassPathLocationHandler localBootClassPathLocationHandler = (BootClassPathLocationHandler)getHandler(StandardLocation.PLATFORM_CLASS_PATH);
/*     */ 
/* 115 */     return localBootClassPathLocationHandler.isDefaultRtJar(paramFile);
/*     */   }
/*     */ 
/*     */   public Collection<File> userClassPath() {
/* 119 */     return getLocation(StandardLocation.CLASS_PATH);
/*     */   }
/*     */ 
/*     */   public Collection<File> sourcePath() {
/* 123 */     Collection localCollection = getLocation(StandardLocation.SOURCE_PATH);
/*     */ 
/* 125 */     return (localCollection == null) || (localCollection.isEmpty()) ? null : localCollection;
/*     */   }
/*     */ 
/*     */   private static Iterable<File> getPathEntries(String paramString)
/*     */   {
/* 134 */     return getPathEntries(paramString, null);
/*     */   }
/*     */ 
/*     */   private static Iterable<File> getPathEntries(String paramString, File paramFile)
/*     */   {
/* 147 */     ListBuffer localListBuffer = new ListBuffer();
/* 148 */     int i = 0;
/* 149 */     while (i <= paramString.length()) {
/* 150 */       int j = paramString.indexOf(File.pathSeparatorChar, i);
/* 151 */       if (j == -1)
/* 152 */         j = paramString.length();
/* 153 */       if (i < j)
/* 154 */         localListBuffer.add(new File(paramString.substring(i, j)));
/* 155 */       else if (paramFile != null)
/* 156 */         localListBuffer.add(paramFile);
/* 157 */       i = j + 1;
/*     */     }
/* 159 */     return localListBuffer;
/*     */   }
/*     */ 
/*     */   void initHandlers()
/*     */   {
/* 651 */     this.handlersForLocation = new HashMap();
/* 652 */     this.handlersForOption = new EnumMap(Option.class);
/*     */ 
/* 654 */     LocationHandler[] arrayOfLocationHandler1 = { new BootClassPathLocationHandler(), new ClassPathLocationHandler(), new SimpleLocationHandler(StandardLocation.SOURCE_PATH, new Option[] { Option.SOURCEPATH }), new SimpleLocationHandler(StandardLocation.ANNOTATION_PROCESSOR_PATH, new Option[] { Option.PROCESSORPATH }), new OutputLocationHandler(StandardLocation.CLASS_OUTPUT, new Option[] { Option.D }), new OutputLocationHandler(StandardLocation.SOURCE_OUTPUT, new Option[] { Option.S }), new OutputLocationHandler(StandardLocation.NATIVE_HEADER_OUTPUT, new Option[] { Option.H }) };
/*     */     LocationHandler localLocationHandler;
/* 664 */     for (localLocationHandler : arrayOfLocationHandler1) {
/* 665 */       this.handlersForLocation.put(localLocationHandler.location, localLocationHandler);
/* 666 */       for (Option localOption : localLocationHandler.options)
/* 667 */         this.handlersForOption.put(localOption, localLocationHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean handleOption(Option paramOption, String paramString) {
/* 672 */     LocationHandler localLocationHandler = (LocationHandler)this.handlersForOption.get(paramOption);
/* 673 */     return localLocationHandler == null ? false : localLocationHandler.handleOption(paramOption, paramString);
/*     */   }
/*     */ 
/*     */   Collection<File> getLocation(JavaFileManager.Location paramLocation) {
/* 677 */     LocationHandler localLocationHandler = getHandler(paramLocation);
/* 678 */     return localLocationHandler == null ? null : localLocationHandler.getLocation();
/*     */   }
/*     */ 
/*     */   File getOutputLocation(JavaFileManager.Location paramLocation) {
/* 682 */     if (!paramLocation.isOutputLocation())
/* 683 */       throw new IllegalArgumentException();
/* 684 */     LocationHandler localLocationHandler = getHandler(paramLocation);
/* 685 */     return ((OutputLocationHandler)localLocationHandler).outputDir;
/*     */   }
/*     */ 
/*     */   void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends File> paramIterable) throws IOException {
/* 689 */     Object localObject = getHandler(paramLocation);
/* 690 */     if (localObject == null) {
/* 691 */       if (paramLocation.isOutputLocation())
/* 692 */         localObject = new OutputLocationHandler(paramLocation, new Option[0]);
/*     */       else
/* 694 */         localObject = new SimpleLocationHandler(paramLocation, new Option[0]);
/* 695 */       this.handlersForLocation.put(paramLocation, localObject);
/*     */     }
/* 697 */     ((LocationHandler)localObject).setLocation(paramIterable);
/*     */   }
/*     */ 
/*     */   protected LocationHandler getHandler(JavaFileManager.Location paramLocation) {
/* 701 */     paramLocation.getClass();
/* 702 */     lazy();
/* 703 */     return (LocationHandler)this.handlersForLocation.get(paramLocation);
/*     */   }
/*     */ 
/*     */   protected void lazy()
/*     */   {
/* 708 */     if (!this.inited) {
/* 709 */       this.warn = this.lint.isEnabled(Lint.LintCategory.PATH);
/*     */ 
/* 711 */       for (LocationHandler localLocationHandler : this.handlersForLocation.values()) {
/* 712 */         localLocationHandler.update(this.options);
/*     */       }
/*     */ 
/* 715 */       this.inited = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isArchive(File paramFile)
/*     */   {
/* 721 */     String str = StringUtils.toLowerCase(paramFile.getName());
/*     */ 
/* 723 */     return (this.fsInfo.isFile(paramFile)) && (
/* 723 */       (str
/* 723 */       .endsWith(".jar")) || 
/* 723 */       (str.endsWith(".zip")));
/*     */   }
/*     */ 
/*     */   public static URL[] pathToURLs(String paramString)
/*     */   {
/* 736 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/* 737 */     URL[] arrayOfURL = new URL[localStringTokenizer.countTokens()];
/* 738 */     int i = 0;
/* 739 */     while (localStringTokenizer.hasMoreTokens()) {
/* 740 */       URL localURL = fileToURL(new File(localStringTokenizer.nextToken()));
/* 741 */       if (localURL != null) {
/* 742 */         arrayOfURL[(i++)] = localURL;
/*     */       }
/*     */     }
/* 745 */     arrayOfURL = (URL[])Arrays.copyOf(arrayOfURL, i);
/* 746 */     return arrayOfURL;
/*     */   }
/*     */ 
/*     */   private static URL fileToURL(File paramFile)
/*     */   {
/*     */     try
/*     */     {
/* 759 */       str = paramFile.getCanonicalPath();
/*     */     } catch (IOException localIOException) {
/* 761 */       str = paramFile.getAbsolutePath();
/*     */     }
/* 763 */     String str = str.replace(File.separatorChar, '/');
/* 764 */     if (!str.startsWith("/")) {
/* 765 */       str = "/" + str;
/*     */     }
/*     */ 
/* 768 */     if (!paramFile.isFile())
/* 769 */       str = str + "/";
/*     */     try
/*     */     {
/* 772 */       return new URL("file", "", str); } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 774 */     throw new IllegalArgumentException(paramFile.toString());
/*     */   }
/*     */ 
/*     */   private class BootClassPathLocationHandler extends Locations.LocationHandler
/*     */   {
/*     */     private Collection<File> searchPath;
/* 513 */     final Map<Option, String> optionValues = new EnumMap(Option.class);
/*     */ 
/* 519 */     private File defaultBootClassPathRtJar = null;
/*     */     private boolean isDefaultBootClassPath;
/*     */ 
/*     */     BootClassPathLocationHandler()
/*     */     {
/* 527 */       super(StandardLocation.PLATFORM_CLASS_PATH, new Option[] { Option.BOOTCLASSPATH, Option.XBOOTCLASSPATH, Option.XBOOTCLASSPATH_PREPEND, Option.XBOOTCLASSPATH_APPEND, Option.ENDORSEDDIRS, Option.DJAVA_ENDORSED_DIRS, Option.EXTDIRS, Option.DJAVA_EXT_DIRS });
/*     */     }
/*     */ 
/*     */     boolean isDefault()
/*     */     {
/* 536 */       lazy();
/* 537 */       return this.isDefaultBootClassPath;
/*     */     }
/*     */ 
/*     */     boolean isDefaultRtJar(File paramFile) {
/* 541 */       lazy();
/* 542 */       return paramFile.equals(this.defaultBootClassPathRtJar);
/*     */     }
/*     */ 
/*     */     boolean handleOption(Option paramOption, String paramString)
/*     */     {
/* 547 */       if (!this.options.contains(paramOption)) {
/* 548 */         return false;
/*     */       }
/* 550 */       paramOption = canonicalize(paramOption);
/* 551 */       this.optionValues.put(paramOption, paramString);
/* 552 */       if (paramOption == Option.BOOTCLASSPATH) {
/* 553 */         this.optionValues.remove(Option.XBOOTCLASSPATH_PREPEND);
/* 554 */         this.optionValues.remove(Option.XBOOTCLASSPATH_APPEND);
/*     */       }
/* 556 */       this.searchPath = null;
/* 557 */       return true;
/*     */     }
/*     */ 
/*     */     private Option canonicalize(Option paramOption)
/*     */     {
/* 563 */       switch (Locations.1.$SwitchMap$com$sun$tools$javac$main$Option[paramOption.ordinal()]) {
/*     */       case 1:
/* 565 */         return Option.BOOTCLASSPATH;
/*     */       case 2:
/* 567 */         return Option.ENDORSEDDIRS;
/*     */       case 3:
/* 569 */         return Option.EXTDIRS;
/*     */       }
/* 571 */       return paramOption;
/*     */     }
/*     */ 
/*     */     Collection<File> getLocation()
/*     */     {
/* 577 */       lazy();
/* 578 */       return this.searchPath;
/*     */     }
/*     */ 
/*     */     void setLocation(Iterable<? extends File> paramIterable)
/*     */     {
/* 583 */       if (paramIterable == null) {
/* 584 */         this.searchPath = null;
/*     */       } else {
/* 586 */         this.defaultBootClassPathRtJar = null;
/* 587 */         this.isDefaultBootClassPath = false;
/* 588 */         Locations.Path localPath = new Locations.Path(Locations.this).addFiles(paramIterable, false);
/* 589 */         this.searchPath = Collections.unmodifiableCollection(localPath);
/* 590 */         this.optionValues.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     Locations.Path computePath() {
/* 595 */       this.defaultBootClassPathRtJar = null;
/* 596 */       Locations.Path localPath = new Locations.Path(Locations.this);
/*     */ 
/* 598 */       String str1 = (String)this.optionValues.get(Option.BOOTCLASSPATH);
/* 599 */       String str2 = (String)this.optionValues.get(Option.ENDORSEDDIRS);
/* 600 */       String str3 = (String)this.optionValues.get(Option.EXTDIRS);
/* 601 */       String str4 = (String)this.optionValues.get(Option.XBOOTCLASSPATH_PREPEND);
/* 602 */       String str5 = (String)this.optionValues.get(Option.XBOOTCLASSPATH_APPEND);
/* 603 */       localPath.addFiles(str4);
/*     */ 
/* 605 */       if (str2 != null)
/* 606 */         localPath.addDirectories(str2);
/*     */       else
/* 608 */         localPath.addDirectories(System.getProperty("java.endorsed.dirs"), false);
/*     */       File localFile1;
/* 610 */       if (str1 != null) {
/* 611 */         localPath.addFiles(str1);
/*     */       }
/*     */       else {
/* 614 */         String str6 = System.getProperty("sun.boot.class.path");
/* 615 */         localPath.addFiles(str6, false);
/* 616 */         localFile1 = new File("rt.jar");
/* 617 */         for (File localFile2 : Locations.getPathEntries(str6)) {
/* 618 */           if (new File(localFile2.getName()).equals(localFile1)) {
/* 619 */             this.defaultBootClassPathRtJar = localFile2;
/*     */           }
/*     */         }
/*     */       }
/* 623 */       localPath.addFiles(str5);
/*     */ 
/* 628 */       if (str3 != null)
/* 629 */         localPath.addDirectories(str3);
/*     */       else {
/* 631 */         localPath.addDirectories(System.getProperty("java.ext.dirs"), false);
/*     */       }
/* 633 */       this.isDefaultBootClassPath = ((str4 == null) && (str1 == null) && (str5 == null));
/*     */ 
/* 638 */       return localPath;
/*     */     }
/*     */ 
/*     */     private void lazy() {
/* 642 */       if (this.searchPath == null)
/* 643 */         this.searchPath = Collections.unmodifiableCollection(computePath());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClassPathLocationHandler extends Locations.SimpleLocationHandler
/*     */   {
/*     */     ClassPathLocationHandler()
/*     */     {
/* 460 */       super(StandardLocation.CLASS_PATH, new Option[] { Option.CLASSPATH, Option.CP });
/*     */     }
/*     */ 
/*     */     Collection<File> getLocation()
/*     */     {
/* 466 */       lazy();
/* 467 */       return this.searchPath;
/*     */     }
/*     */ 
/*     */     protected Locations.Path computePath(String paramString)
/*     */     {
/* 472 */       String str = paramString;
/*     */ 
/* 475 */       if (str == null) str = System.getProperty("env.class.path");
/*     */ 
/* 479 */       if ((str == null) && (System.getProperty("application.home") == null)) {
/* 480 */         str = System.getProperty("java.class.path");
/*     */       }
/*     */ 
/* 483 */       if (str == null) str = ".";
/*     */ 
/* 485 */       return createPath().addFiles(str);
/*     */     }
/*     */ 
/*     */     protected Locations.Path createPath()
/*     */     {
/* 492 */       return new Locations.Path(Locations.this)
/* 491 */         .expandJarClassPaths(true)
/* 492 */         .emptyPathDefault(new File("."));
/*     */     }
/*     */ 
/*     */     private void lazy()
/*     */     {
/* 496 */       if (this.searchPath == null)
/* 497 */         setLocation(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract class LocationHandler
/*     */   {
/*     */     final JavaFileManager.Location location;
/*     */     final Set<Option> options;
/*     */ 
/*     */     protected LocationHandler(JavaFileManager.Location paramArrayOfOption, Option[] arg3)
/*     */     {
/* 331 */       this.location = paramArrayOfOption;
/*     */       Object[] arrayOfObject;
/* 332 */       this.options = (arrayOfObject.length == 0 ? 
/* 333 */         EnumSet.noneOf(Option.class) : 
/* 334 */         EnumSet.copyOf(Arrays.asList(arrayOfObject)));
/*     */     }
/*     */ 
/*     */     void update(Options paramOptions)
/*     */     {
/* 339 */       for (Option localOption : this.options) {
/* 340 */         String str = paramOptions.get(localOption);
/* 341 */         if (str != null)
/* 342 */           handleOption(localOption, str);
/*     */       }
/*     */     }
/*     */ 
/*     */     abstract boolean handleOption(Option paramOption, String paramString);
/*     */ 
/*     */     abstract Collection<File> getLocation();
/*     */ 
/*     */     abstract void setLocation(Iterable<? extends File> paramIterable)
/*     */       throws IOException;
/*     */   }
/*     */ 
/*     */   private class OutputLocationHandler extends Locations.LocationHandler
/*     */   {
/*     */     private File outputDir;
/*     */ 
/*     */     OutputLocationHandler(JavaFileManager.Location paramArrayOfOption, Option[] arg3)
/*     */     {
/* 365 */       super(paramArrayOfOption, arrayOfOption);
/*     */     }
/*     */ 
/*     */     boolean handleOption(Option paramOption, String paramString)
/*     */     {
/* 370 */       if (!this.options.contains(paramOption)) {
/* 371 */         return false;
/*     */       }
/*     */ 
/* 377 */       this.outputDir = new File(paramString);
/* 378 */       return true;
/*     */     }
/*     */ 
/*     */     Collection<File> getLocation()
/*     */     {
/* 383 */       return this.outputDir == null ? null : Collections.singleton(this.outputDir);
/*     */     }
/*     */ 
/*     */     void setLocation(Iterable<? extends File> paramIterable) throws IOException
/*     */     {
/* 388 */       if (paramIterable == null) {
/* 389 */         this.outputDir = null;
/*     */       } else {
/* 391 */         Iterator localIterator = paramIterable.iterator();
/* 392 */         if (!localIterator.hasNext())
/* 393 */           throw new IllegalArgumentException("empty path for directory");
/* 394 */         File localFile = (File)localIterator.next();
/* 395 */         if (localIterator.hasNext())
/* 396 */           throw new IllegalArgumentException("path too long for directory");
/* 397 */         if (!localFile.exists())
/* 398 */           throw new FileNotFoundException(localFile + ": does not exist");
/* 399 */         if (!localFile.isDirectory())
/* 400 */           throw new IOException(localFile + ": not a directory");
/* 401 */         this.outputDir = localFile;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Path extends LinkedHashSet<File>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/* 169 */     private boolean expandJarClassPaths = false;
/* 170 */     private Set<File> canonicalValues = new HashSet();
/*     */ 
/* 178 */     private File emptyPathDefault = null;
/*     */ 
/*     */     public Path expandJarClassPaths(boolean paramBoolean)
/*     */     {
/* 173 */       this.expandJarClassPaths = paramBoolean;
/* 174 */       return this;
/*     */     }
/*     */ 
/*     */     public Path emptyPathDefault(File paramFile)
/*     */     {
/* 181 */       this.emptyPathDefault = paramFile;
/* 182 */       return this;
/*     */     }
/*     */     public Path() {
/*     */     }
/*     */ 
/*     */     public Path addDirectories(String paramString, boolean paramBoolean) {
/* 188 */       boolean bool = this.expandJarClassPaths;
/* 189 */       this.expandJarClassPaths = true;
/*     */       try
/*     */       {
/*     */         Object localObject1;
/* 191 */         if (paramString != null)
/* 192 */           for (localObject1 = Locations.getPathEntries(paramString).iterator(); ((Iterator)localObject1).hasNext(); ) { File localFile = (File)((Iterator)localObject1).next();
/* 193 */             addDirectory(localFile, paramBoolean); }
/* 194 */         return this;
/*     */       } finally {
/* 196 */         this.expandJarClassPaths = bool;
/*     */       }
/*     */     }
/*     */ 
/*     */     public Path addDirectories(String paramString) {
/* 201 */       return addDirectories(paramString, Locations.this.warn);
/*     */     }
/*     */ 
/*     */     private void addDirectory(File paramFile, boolean paramBoolean) {
/* 205 */       if (!paramFile.isDirectory()) {
/* 206 */         if (paramBoolean) {
/* 207 */           Locations.this.log.warning(Lint.LintCategory.PATH, "dir.path.element.not.found", new Object[] { paramFile });
/*     */         }
/* 209 */         return;
/*     */       }
/*     */ 
/* 212 */       File[] arrayOfFile1 = paramFile.listFiles();
/* 213 */       if (arrayOfFile1 == null) {
/* 214 */         return;
/*     */       }
/* 216 */       for (File localFile : arrayOfFile1)
/* 217 */         if (Locations.this.isArchive(localFile))
/* 218 */           addFile(localFile, paramBoolean);
/*     */     }
/*     */ 
/*     */     public Path addFiles(String paramString, boolean paramBoolean)
/*     */     {
/* 223 */       if (paramString != null) {
/* 224 */         addFiles(Locations.getPathEntries(paramString, this.emptyPathDefault), paramBoolean);
/*     */       }
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */     public Path addFiles(String paramString) {
/* 230 */       return addFiles(paramString, Locations.this.warn);
/*     */     }
/*     */ 
/*     */     public Path addFiles(Iterable<? extends File> paramIterable, boolean paramBoolean) {
/* 234 */       if (paramIterable != null) {
/* 235 */         for (File localFile : paramIterable)
/* 236 */           addFile(localFile, paramBoolean);
/*     */       }
/* 238 */       return this;
/*     */     }
/*     */ 
/*     */     public Path addFiles(Iterable<? extends File> paramIterable) {
/* 242 */       return addFiles(paramIterable, Locations.this.warn);
/*     */     }
/*     */ 
/*     */     public void addFile(File paramFile, boolean paramBoolean) {
/* 246 */       if (contains(paramFile))
/*     */       {
/* 248 */         return;
/*     */       }
/*     */ 
/* 251 */       if (!Locations.this.fsInfo.exists(paramFile))
/*     */       {
/* 253 */         if (paramBoolean) {
/* 254 */           Locations.this.log.warning(Lint.LintCategory.PATH, "path.element.not.found", new Object[] { paramFile });
/*     */         }
/*     */ 
/* 257 */         super.add(paramFile);
/* 258 */         return;
/*     */       }
/*     */ 
/* 261 */       File localFile = Locations.this.fsInfo.getCanonicalFile(paramFile);
/* 262 */       if (this.canonicalValues.contains(localFile))
/*     */       {
/* 264 */         return;
/*     */       }
/*     */ 
/* 267 */       if (Locations.this.fsInfo.isFile(paramFile))
/*     */       {
/* 269 */         if (!Locations.this.isArchive(paramFile))
/*     */         {
/*     */           try
/*     */           {
/* 273 */             ZipFile localZipFile = new ZipFile(paramFile);
/* 274 */             localZipFile.close();
/* 275 */             if (paramBoolean) {
/* 276 */               Locations.this.log.warning(Lint.LintCategory.PATH, "unexpected.archive.file", new Object[] { paramFile });
/*     */             }
/*     */           }
/*     */           catch (IOException localIOException)
/*     */           {
/* 281 */             if (paramBoolean) {
/* 282 */               Locations.this.log.warning(Lint.LintCategory.PATH, "invalid.archive.file", new Object[] { paramFile });
/*     */             }
/*     */ 
/* 285 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 292 */       super.add(paramFile);
/* 293 */       this.canonicalValues.add(localFile);
/*     */ 
/* 295 */       if ((this.expandJarClassPaths) && (Locations.this.fsInfo.isFile(paramFile)))
/* 296 */         addJarClassPath(paramFile, paramBoolean);
/*     */     }
/*     */ 
/*     */     private void addJarClassPath(File paramFile, boolean paramBoolean)
/*     */     {
/*     */       try
/*     */       {
/* 305 */         for (File localFile : Locations.this.fsInfo.getJarClassPath(paramFile))
/* 306 */           addFile(localFile, paramBoolean);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 309 */         Locations.this.log.error("error.reading.file", new Object[] { paramFile, JavacFileManager.getMessage(localIOException) });
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SimpleLocationHandler extends Locations.LocationHandler
/*     */   {
/*     */     protected Collection<File> searchPath;
/*     */ 
/*     */     SimpleLocationHandler(JavaFileManager.Location paramArrayOfOption, Option[] arg3)
/*     */     {
/* 416 */       super(paramArrayOfOption, arrayOfOption);
/*     */     }
/*     */ 
/*     */     boolean handleOption(Option paramOption, String paramString)
/*     */     {
/* 421 */       if (!this.options.contains(paramOption))
/* 422 */         return false;
/* 423 */       this.searchPath = (paramString == null ? null : 
/* 424 */         Collections.unmodifiableCollection(createPath().addFiles(paramString)));
/* 425 */       return true;
/*     */     }
/*     */ 
/*     */     Collection<File> getLocation()
/*     */     {
/* 430 */       return this.searchPath;
/*     */     }
/*     */ 
/*     */     void setLocation(Iterable<? extends File> paramIterable)
/*     */     {
/*     */       Locations.Path localPath;
/* 436 */       if (paramIterable == null)
/* 437 */         localPath = computePath(null);
/*     */       else {
/* 439 */         localPath = createPath().addFiles(paramIterable);
/*     */       }
/* 441 */       this.searchPath = Collections.unmodifiableCollection(localPath);
/*     */     }
/*     */ 
/*     */     protected Locations.Path computePath(String paramString) {
/* 445 */       return createPath().addFiles(paramString);
/*     */     }
/*     */ 
/*     */     protected Locations.Path createPath() {
/* 449 */       return new Locations.Path(Locations.this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.Locations
 * JD-Core Version:    0.6.2
 */