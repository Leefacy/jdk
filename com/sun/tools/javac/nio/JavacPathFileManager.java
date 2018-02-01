/*     */ package com.sun.tools.javac.nio;
/*     */ 
/*     */ import com.sun.tools.javac.file.Locations;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class JavacPathFileManager extends BaseFileManager
/*     */   implements PathFileManager
/*     */ {
/*     */   protected FileSystem defaultFileSystem;
/* 269 */   private boolean inited = false;
/*     */   private Map<JavaFileManager.Location, PathsForLocation> pathsForLocation;
/*     */   private Map<Path, FileSystem> fileSystems;
/*     */ 
/*     */   public JavacPathFileManager(Context paramContext, boolean paramBoolean, Charset paramCharset)
/*     */   {
/* 113 */     super(paramCharset);
/* 114 */     if (paramBoolean)
/* 115 */       paramContext.put(JavaFileManager.class, this);
/* 116 */     this.pathsForLocation = new HashMap();
/* 117 */     this.fileSystems = new HashMap();
/* 118 */     setContext(paramContext);
/*     */   }
/*     */ 
/*     */   public void setContext(Context paramContext)
/*     */   {
/* 126 */     super.setContext(paramContext);
/*     */   }
/*     */ 
/*     */   public FileSystem getDefaultFileSystem()
/*     */   {
/* 131 */     if (this.defaultFileSystem == null)
/* 132 */       this.defaultFileSystem = FileSystems.getDefault();
/* 133 */     return this.defaultFileSystem;
/*     */   }
/*     */ 
/*     */   public void setDefaultFileSystem(FileSystem paramFileSystem)
/*     */   {
/* 138 */     this.defaultFileSystem = paramFileSystem;
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException
/*     */   {
/* 143 */     this.contentCache.clear();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 148 */     for (FileSystem localFileSystem : this.fileSystems.values())
/* 149 */       localFileSystem.close();
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader(JavaFileManager.Location paramLocation)
/*     */   {
/* 154 */     nullCheck(paramLocation);
/* 155 */     Iterable localIterable = getLocation(paramLocation);
/* 156 */     if (localIterable == null)
/* 157 */       return null;
/* 158 */     ListBuffer localListBuffer = new ListBuffer();
/* 159 */     for (Path localPath : localIterable) {
/*     */       try {
/* 161 */         localListBuffer.append(localPath.toUri().toURL());
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 163 */         throw new AssertionError(localMalformedURLException);
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return getClassLoader((URL[])localListBuffer.toArray(new URL[localListBuffer.size()]));
/*     */   }
/*     */ 
/*     */   public boolean isDefaultBootClassPath()
/*     */   {
/* 172 */     return this.locations.isDefaultBootClassPath();
/*     */   }
/*     */ 
/*     */   public boolean hasLocation(JavaFileManager.Location paramLocation)
/*     */   {
/* 178 */     return getLocation(paramLocation) != null;
/*     */   }
/*     */ 
/*     */   public Iterable<? extends Path> getLocation(JavaFileManager.Location paramLocation) {
/* 182 */     nullCheck(paramLocation);
/* 183 */     lazyInitSearchPaths();
/* 184 */     PathsForLocation localPathsForLocation = (PathsForLocation)this.pathsForLocation.get(paramLocation);
/* 185 */     if ((localPathsForLocation == null) && (!this.pathsForLocation.containsKey(paramLocation))) {
/* 186 */       setDefaultForLocation(paramLocation);
/* 187 */       localPathsForLocation = (PathsForLocation)this.pathsForLocation.get(paramLocation);
/*     */     }
/* 189 */     return localPathsForLocation;
/*     */   }
/*     */ 
/*     */   private Path getOutputLocation(JavaFileManager.Location paramLocation) {
/* 193 */     Iterable localIterable = getLocation(paramLocation);
/* 194 */     return localIterable == null ? null : (Path)localIterable.iterator().next();
/*     */   }
/*     */ 
/*     */   public void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends Path> paramIterable)
/*     */     throws IOException
/*     */   {
/* 200 */     nullCheck(paramLocation);
/* 201 */     lazyInitSearchPaths();
/* 202 */     if (paramIterable == null) {
/* 203 */       setDefaultForLocation(paramLocation);
/*     */     } else {
/* 205 */       if (paramLocation.isOutputLocation())
/* 206 */         checkOutputPath(paramIterable);
/* 207 */       PathsForLocation localPathsForLocation = new PathsForLocation(null);
/* 208 */       for (Path localPath : paramIterable)
/* 209 */         localPathsForLocation.add(localPath);
/* 210 */       this.pathsForLocation.put(paramLocation, localPathsForLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkOutputPath(Iterable<? extends Path> paramIterable) throws IOException {
/* 215 */     Iterator localIterator = paramIterable.iterator();
/* 216 */     if (!localIterator.hasNext())
/* 217 */       throw new IllegalArgumentException("empty path for directory");
/* 218 */     Path localPath = (Path)localIterator.next();
/* 219 */     if (localIterator.hasNext())
/* 220 */       throw new IllegalArgumentException("path too long for directory");
/* 221 */     if (!isDirectory(localPath))
/* 222 */       throw new IOException(localPath + ": not a directory");
/*     */   }
/*     */ 
/*     */   private void setDefaultForLocation(JavaFileManager.Location paramLocation) {
/* 226 */     Object localObject1 = null;
/* 227 */     if ((paramLocation instanceof StandardLocation)) {
/* 228 */       switch (2.$SwitchMap$javax$tools$StandardLocation[((StandardLocation)paramLocation).ordinal()]) {
/*     */       case 1:
/* 230 */         localObject1 = this.locations.userClassPath();
/* 231 */         break;
/*     */       case 2:
/* 233 */         localObject1 = this.locations.bootClassPath();
/* 234 */         break;
/*     */       case 3:
/* 236 */         localObject1 = this.locations.sourcePath();
/* 237 */         break;
/*     */       case 4:
/* 239 */         localObject2 = this.options.get(Option.D);
/* 240 */         localObject1 = localObject2 == null ? null : Collections.singleton(new File((String)localObject2));
/* 241 */         break;
/*     */       case 5:
/* 244 */         localObject2 = this.options.get(Option.S);
/* 245 */         localObject1 = localObject2 == null ? null : Collections.singleton(new File((String)localObject2));
/* 246 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 251 */     Object localObject2 = new PathsForLocation(null);
/* 252 */     if (localObject1 != null) {
/* 253 */       for (File localFile : (Collection)localObject1)
/* 254 */         ((PathsForLocation)localObject2).add(localFile.toPath());
/*     */     }
/* 256 */     if (!((PathsForLocation)localObject2).isEmpty())
/* 257 */       this.pathsForLocation.put(paramLocation, localObject2);
/*     */   }
/*     */ 
/*     */   private void lazyInitSearchPaths() {
/* 261 */     if (!this.inited) {
/* 262 */       setDefaultForLocation(StandardLocation.PLATFORM_CLASS_PATH);
/* 263 */       setDefaultForLocation(StandardLocation.CLASS_PATH);
/* 264 */       setDefaultForLocation(StandardLocation.SOURCE_PATH);
/* 265 */       this.inited = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Path getPath(FileObject paramFileObject)
/*     */   {
/* 283 */     nullCheck(paramFileObject);
/* 284 */     if (!(paramFileObject instanceof PathFileObject))
/* 285 */       throw new IllegalArgumentException();
/* 286 */     return ((PathFileObject)paramFileObject).getPath();
/*     */   }
/*     */ 
/*     */   public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2)
/*     */   {
/* 291 */     nullCheck(paramFileObject1);
/* 292 */     nullCheck(paramFileObject2);
/* 293 */     if (!(paramFileObject1 instanceof PathFileObject))
/* 294 */       throw new IllegalArgumentException("Not supported: " + paramFileObject1);
/* 295 */     if (!(paramFileObject2 instanceof PathFileObject))
/* 296 */       throw new IllegalArgumentException("Not supported: " + paramFileObject2);
/* 297 */     return ((PathFileObject)paramFileObject1).isSameFile((PathFileObject)paramFileObject2);
/*     */   }
/*     */ 
/*     */   public Iterable<JavaFileObject> list(JavaFileManager.Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 305 */     nullCheck(paramString);
/* 306 */     nullCheck(paramSet);
/*     */ 
/* 308 */     Iterable localIterable = getLocation(paramLocation);
/* 309 */     if (localIterable == null)
/* 310 */       return List.nil();
/* 311 */     ListBuffer localListBuffer = new ListBuffer();
/*     */ 
/* 313 */     for (Path localPath : localIterable) {
/* 314 */       list(localPath, paramString, paramSet, paramBoolean, localListBuffer);
/*     */     }
/* 316 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   private void list(Path paramPath, String paramString, final Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, final ListBuffer<JavaFileObject> paramListBuffer)
/*     */     throws IOException
/*     */   {
/* 322 */     if (!Files.exists(paramPath, new LinkOption[0]))
/*     */       return;
/*     */     final Path localPath1;
/* 326 */     if (isDirectory(paramPath)) {
/* 327 */       localPath1 = paramPath;
/*     */     } else {
/* 329 */       localObject = getFileSystem(paramPath);
/* 330 */       if (localObject == null)
/* 331 */         return;
/* 332 */       localPath1 = (Path)((FileSystem)localObject).getRootDirectories().iterator().next();
/*     */     }
/* 334 */     Object localObject = paramPath.getFileSystem().getSeparator();
/*     */ 
/* 336 */     Path localPath2 = paramString.isEmpty() ? localPath1 : localPath1
/* 336 */       .resolve(paramString
/* 336 */       .replace(".", (CharSequence)localObject));
/*     */ 
/* 337 */     if (!Files.exists(localPath2, new LinkOption[0])) {
/* 338 */       return;
/*     */     }
/*     */ 
/* 366 */     int i = paramBoolean ? 2147483647 : 1;
/* 367 */     EnumSet localEnumSet = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
/* 368 */     Files.walkFileTree(localPath2, localEnumSet, i, new SimpleFileVisitor()
/*     */     {
/*     */       public FileVisitResult preVisitDirectory(Path paramAnonymousPath, BasicFileAttributes paramAnonymousBasicFileAttributes)
/*     */       {
/* 372 */         Path localPath = paramAnonymousPath.getFileName();
/* 373 */         if ((localPath == null) || (SourceVersion.isIdentifier(localPath.toString()))) {
/* 374 */           return FileVisitResult.CONTINUE;
/*     */         }
/* 376 */         return FileVisitResult.SKIP_SUBTREE;
/*     */       }
/*     */ 
/*     */       public FileVisitResult visitFile(Path paramAnonymousPath, BasicFileAttributes paramAnonymousBasicFileAttributes)
/*     */       {
/* 381 */         if ((paramAnonymousBasicFileAttributes.isRegularFile()) && (paramSet.contains(BaseFileManager.getKind(paramAnonymousPath.getFileName().toString()))))
/*     */         {
/* 383 */           PathFileObject localPathFileObject = PathFileObject.createDirectoryPathFileObject(JavacPathFileManager.this, paramAnonymousPath, localPath1);
/*     */ 
/* 385 */           paramListBuffer.append(localPathFileObject);
/*     */         }
/* 387 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromPaths(Iterable<? extends Path> paramIterable)
/*     */   {
/*     */     ArrayList localArrayList;
/* 396 */     if ((paramIterable instanceof Collection))
/* 397 */       localArrayList = new ArrayList(((Collection)paramIterable).size());
/*     */     else
/* 399 */       localArrayList = new ArrayList();
/* 400 */     for (Path localPath : paramIterable)
/* 401 */       localArrayList.add(PathFileObject.createSimplePathFileObject(this, (Path)nullCheck(localPath)));
/* 402 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjects(Path[] paramArrayOfPath)
/*     */   {
/* 407 */     return getJavaFileObjectsFromPaths(Arrays.asList((Object[])nullCheck(paramArrayOfPath)));
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForInput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind)
/*     */     throws IOException
/*     */   {
/* 413 */     return getFileForInput(paramLocation, getRelativePath(paramString, paramKind));
/*     */   }
/*     */ 
/*     */   public FileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 419 */     return getFileForInput(paramLocation, getRelativePath(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   private JavaFileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString) throws IOException
/*     */   {
/* 424 */     for (Path localPath1 : getLocation(paramLocation))
/*     */     {
/*     */       Object localObject;
/* 425 */       if (isDirectory(localPath1)) {
/* 426 */         localObject = resolve(localPath1, paramString);
/* 427 */         if (Files.exists((Path)localObject, new LinkOption[0]))
/* 428 */           return PathFileObject.createDirectoryPathFileObject(this, (Path)localObject, localPath1);
/*     */       } else {
/* 430 */         localObject = getFileSystem(localPath1);
/* 431 */         if (localObject != null) {
/* 432 */           Path localPath2 = getPath((FileSystem)localObject, paramString);
/* 433 */           if (Files.exists(localPath2, new LinkOption[0]))
/* 434 */             return PathFileObject.createJarPathFileObject(this, localPath2);
/*     */         }
/*     */       }
/*     */     }
/* 438 */     return null;
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForOutput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 444 */     return getFileForOutput(paramLocation, getRelativePath(paramString, paramKind), paramFileObject);
/*     */   }
/*     */ 
/*     */   public FileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 451 */     return getFileForOutput(paramLocation, getRelativePath(paramString1, paramString2), paramFileObject);
/*     */   }
/*     */ 
/*     */   private JavaFileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString, FileObject paramFileObject)
/*     */   {
/* 456 */     Path localPath1 = getOutputLocation(paramLocation);
/* 457 */     if (localPath1 == null) {
/* 458 */       if (paramLocation == StandardLocation.CLASS_OUTPUT) {
/* 459 */         localPath2 = null;
/* 460 */         if ((paramFileObject != null) && ((paramFileObject instanceof PathFileObject))) {
/* 461 */           localPath2 = ((PathFileObject)paramFileObject).getPath().getParent();
/*     */         }
/* 463 */         return PathFileObject.createSiblingPathFileObject(this, localPath2
/* 464 */           .resolve(getBaseName(paramString)), 
/* 464 */           paramString);
/*     */       }
/* 466 */       if (paramLocation == StandardLocation.SOURCE_OUTPUT) {
/* 467 */         localPath1 = getOutputLocation(StandardLocation.CLASS_OUTPUT);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 472 */     if (localPath1 != null) {
/* 473 */       localPath2 = resolve(localPath1, paramString);
/* 474 */       return PathFileObject.createDirectoryPathFileObject(this, localPath2, localPath1);
/*     */     }
/* 476 */     Path localPath2 = getPath(getDefaultFileSystem(), paramString);
/* 477 */     return PathFileObject.createSimplePathFileObject(this, localPath2);
/*     */   }
/*     */ 
/*     */   public String inferBinaryName(JavaFileManager.Location paramLocation, JavaFileObject paramJavaFileObject)
/*     */   {
/* 484 */     nullCheck(paramJavaFileObject);
/*     */ 
/* 486 */     Iterable localIterable = getLocation(paramLocation);
/* 487 */     if (localIterable == null) {
/* 488 */       return null;
/*     */     }
/*     */ 
/* 491 */     if (!(paramJavaFileObject instanceof PathFileObject)) {
/* 492 */       throw new IllegalArgumentException(paramJavaFileObject.getClass().getName());
/*     */     }
/* 494 */     return ((PathFileObject)paramJavaFileObject).inferBinaryName(localIterable);
/*     */   }
/*     */ 
/*     */   private FileSystem getFileSystem(Path paramPath) throws IOException {
/* 498 */     FileSystem localFileSystem = (FileSystem)this.fileSystems.get(paramPath);
/* 499 */     if (localFileSystem == null) {
/* 500 */       localFileSystem = FileSystems.newFileSystem(paramPath, null);
/* 501 */       this.fileSystems.put(paramPath, localFileSystem);
/*     */     }
/* 503 */     return localFileSystem;
/*     */   }
/*     */ 
/*     */   private static String getRelativePath(String paramString, JavaFileObject.Kind paramKind)
/*     */   {
/* 513 */     return paramString.replace(".", "/") + paramKind.extension;
/*     */   }
/*     */ 
/*     */   private static String getRelativePath(String paramString1, String paramString2)
/*     */   {
/* 518 */     return paramString1
/* 518 */       .replace(".", "/") + 
/* 518 */       "/" + paramString2;
/*     */   }
/*     */ 
/*     */   private static String getBaseName(String paramString) {
/* 522 */     int i = paramString.lastIndexOf("/");
/* 523 */     return paramString.substring(i + 1);
/*     */   }
/*     */ 
/*     */   private static boolean isDirectory(Path paramPath) throws IOException {
/* 527 */     BasicFileAttributes localBasicFileAttributes = Files.readAttributes(paramPath, BasicFileAttributes.class, new LinkOption[0]);
/* 528 */     return localBasicFileAttributes.isDirectory();
/*     */   }
/*     */ 
/*     */   private static Path getPath(FileSystem paramFileSystem, String paramString) {
/* 532 */     return paramFileSystem.getPath(paramString.replace("/", paramFileSystem.getSeparator()), new String[0]);
/*     */   }
/*     */ 
/*     */   private static Path resolve(Path paramPath, String paramString) {
/* 536 */     FileSystem localFileSystem = paramPath.getFileSystem();
/* 537 */     Path localPath = localFileSystem.getPath(paramString.replace("/", localFileSystem.getSeparator()), new String[0]);
/* 538 */     return paramPath.resolve(localPath);
/*     */   }
/*     */ 
/*     */   private static class PathsForLocation extends LinkedHashSet<Path>
/*     */   {
/*     */     private static final long serialVersionUID = 6788510222394486733L;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.nio.JavacPathFileManager
 * JD-Core Version:    0.6.2
 */