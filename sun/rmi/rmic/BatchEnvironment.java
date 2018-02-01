/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.tools.java.ClassPath;
/*     */ 
/*     */ public class BatchEnvironment extends sun.tools.javac.BatchEnvironment
/*     */ {
/*     */   private Main main;
/* 163 */   private Vector<File> generatedFiles = new Vector();
/*     */ 
/*     */   public static ClassPath createClassPath(String paramString)
/*     */   {
/*  73 */     ClassPath[] arrayOfClassPath = classPaths(null, paramString, null, null);
/*  74 */     return arrayOfClassPath[1];
/*     */   }
/*     */ 
/*     */   public static ClassPath createClassPath(String paramString1, String paramString2, String paramString3)
/*     */   {
/*  96 */     Path localPath = new Path();
/*     */ 
/*  98 */     if (paramString2 == null) {
/*  99 */       paramString2 = System.getProperty("sun.boot.class.path");
/*     */     }
/* 101 */     if (paramString2 != null) {
/* 102 */       localPath.addFiles(paramString2);
/*     */     }
/*     */ 
/* 109 */     localPath.expandJarClassPaths(true);
/*     */ 
/* 111 */     if (paramString3 == null) {
/* 112 */       paramString3 = System.getProperty("java.ext.dirs");
/*     */     }
/* 114 */     if (paramString3 != null) {
/* 115 */       localPath.addDirectories(paramString3);
/*     */     }
/*     */ 
/* 122 */     localPath.emptyPathDefault(".");
/*     */ 
/* 124 */     if (paramString1 == null)
/*     */     {
/* 128 */       paramString1 = System.getProperty("env.class.path");
/* 129 */       if (paramString1 == null) {
/* 130 */         paramString1 = ".";
/*     */       }
/*     */     }
/* 133 */     localPath.addFiles(paramString1);
/*     */ 
/* 135 */     return new ClassPath((String[])localPath.toArray(new String[localPath.size()]));
/*     */   }
/*     */ 
/*     */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, Main paramMain)
/*     */   {
/* 143 */     super(paramOutputStream, new ClassPath(""), paramClassPath);
/*     */ 
/* 145 */     this.main = paramMain;
/*     */   }
/*     */ 
/*     */   public Main getMain()
/*     */   {
/* 152 */     return this.main;
/*     */   }
/*     */ 
/*     */   public ClassPath getClassPath()
/*     */   {
/* 159 */     return this.binaryPath;
/*     */   }
/*     */ 
/*     */   public void addGeneratedFile(File paramFile)
/*     */   {
/* 170 */     this.generatedFiles.addElement(paramFile);
/*     */   }
/*     */ 
/*     */   public void deleteGeneratedFiles()
/*     */   {
/* 179 */     synchronized (this.generatedFiles) {
/* 180 */       Enumeration localEnumeration = this.generatedFiles.elements();
/* 181 */       while (localEnumeration.hasMoreElements()) {
/* 182 */         File localFile = (File)localEnumeration.nextElement();
/* 183 */         localFile.delete();
/*     */       }
/* 185 */       this.generatedFiles.removeAllElements();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 193 */     this.main = null;
/* 194 */     this.generatedFiles = null;
/* 195 */     super.shutdown();
/*     */   }
/*     */ 
/*     */   public String errorString(String paramString, Object paramObject1, Object paramObject2, Object paramObject3)
/*     */   {
/* 207 */     if ((paramString.startsWith("rmic.")) || (paramString.startsWith("warn.rmic."))) {
/* 208 */       String str = Main.getText(paramString, paramObject1 != null ? paramObject1
/* 209 */         .toString() : null, paramObject2 != null ? paramObject2
/* 210 */         .toString() : null, paramObject3 != null ? paramObject3
/* 211 */         .toString() : null);
/*     */ 
/* 213 */       if (paramString.startsWith("warn.")) {
/* 214 */         str = "warning: " + str;
/*     */       }
/* 216 */       return str;
/*     */     }
/* 218 */     return super.errorString(paramString, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class Path extends LinkedHashSet<String>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     private static final boolean warn = false;
/* 318 */     private boolean expandJarClassPaths = false;
/*     */ 
/* 326 */     private String emptyPathDefault = null;
/*     */ 
/*     */     private static boolean isZip(String paramString)
/*     */     {
/* 315 */       return new File(paramString).isFile();
/*     */     }
/*     */ 
/*     */     public Path expandJarClassPaths(boolean paramBoolean)
/*     */     {
/* 321 */       this.expandJarClassPaths = paramBoolean;
/* 322 */       return this;
/*     */     }
/*     */ 
/*     */     public Path emptyPathDefault(String paramString)
/*     */     {
/* 329 */       this.emptyPathDefault = paramString;
/* 330 */       return this;
/*     */     }
/*     */ 
/*     */     public Path addDirectories(String paramString, boolean paramBoolean)
/*     */     {
/* 336 */       if (paramString != null)
/* 337 */         for (String str : new PathIterator(paramString))
/* 338 */           addDirectory(str, paramBoolean);
/* 339 */       return this;
/*     */     }
/*     */ 
/*     */     public Path addDirectories(String paramString) {
/* 343 */       return addDirectories(paramString, false);
/*     */     }
/*     */ 
/*     */     private void addDirectory(String paramString, boolean paramBoolean) {
/* 347 */       if (!new File(paramString).isDirectory())
/*     */       {
/* 351 */         return;
/*     */       }
/*     */ 
/* 354 */       for (String str1 : new File(paramString).list()) {
/* 355 */         String str2 = str1.toLowerCase();
/* 356 */         if ((str2.endsWith(".jar")) || 
/* 357 */           (str2
/* 357 */           .endsWith(".zip")))
/*     */         {
/* 358 */           addFile(paramString + File.separator + str1, paramBoolean);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 363 */     public Path addFiles(String paramString, boolean paramBoolean) { if (paramString != null)
/* 364 */         for (String str : new PathIterator(paramString, this.emptyPathDefault))
/* 365 */           addFile(str, paramBoolean);
/* 366 */       return this; }
/*     */ 
/*     */     public Path addFiles(String paramString)
/*     */     {
/* 370 */       return addFiles(paramString, false);
/*     */     }
/*     */ 
/*     */     private void addFile(String paramString, boolean paramBoolean) {
/* 374 */       if (contains(paramString))
/*     */       {
/* 376 */         return;
/*     */       }
/*     */ 
/* 379 */       File localFile = new File(paramString);
/* 380 */       if (!localFile.exists())
/*     */       {
/* 382 */         if (paramBoolean)
/*     */         {
/* 385 */           return;
/*     */         }
/*     */       }
/* 388 */       if (localFile.isFile())
/*     */       {
/* 390 */         String str = paramString.toLowerCase();
/* 391 */         if ((!str.endsWith(".zip")) && 
/* 392 */           (!str
/* 392 */           .endsWith(".jar")))
/*     */         {
/* 397 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 404 */       super.add(paramString);
/* 405 */       if ((this.expandJarClassPaths) && (isZip(paramString)))
/* 406 */         addJarClassPath(paramString, paramBoolean);
/*     */     }
/*     */ 
/*     */     private void addJarClassPath(String paramString, boolean paramBoolean)
/*     */     {
/*     */       try
/*     */       {
/* 415 */         String str1 = new File(paramString).getParent();
/* 416 */         JarFile localJarFile = new JarFile(paramString);
/*     */         try
/*     */         {
/* 419 */           Manifest localManifest = localJarFile.getManifest();
/* 420 */           if (localManifest == null) return;
/*     */ 
/* 422 */           Attributes localAttributes = localManifest.getMainAttributes();
/* 423 */           if (localAttributes == null) return;
/*     */ 
/* 425 */           String str2 = localAttributes.getValue(Attributes.Name.CLASS_PATH);
/* 426 */           if (str2 == null) return;
/*     */ 
/* 428 */           StringTokenizer localStringTokenizer = new StringTokenizer(str2);
/* 429 */           while (localStringTokenizer.hasMoreTokens()) {
/* 430 */             String str3 = localStringTokenizer.nextToken();
/* 431 */             if (str1 != null)
/* 432 */               str3 = new File(str1, str3).getCanonicalPath();
/* 433 */             addFile(str3, paramBoolean);
/*     */           }
/*     */         } finally {
/* 436 */           localJarFile.close();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     private static class PathIterator
/*     */       implements Collection<String>
/*     */     {
/* 236 */       private int pos = 0;
/*     */       private final String path;
/*     */       private final String emptyPathDefault;
/*     */ 
/*     */       public PathIterator(String paramString1, String paramString2)
/*     */       {
/* 241 */         this.path = paramString1;
/* 242 */         this.emptyPathDefault = paramString2;
/*     */       }
/* 244 */       public PathIterator(String paramString) { this(paramString, null); } 
/*     */       public Iterator<String> iterator() {
/* 246 */         return new Iterator() {
/*     */           public boolean hasNext() {
/* 248 */             return BatchEnvironment.Path.PathIterator.this.pos <= BatchEnvironment.Path.PathIterator.this.path.length();
/*     */           }
/*     */           public String next() {
/* 251 */             int i = BatchEnvironment.Path.PathIterator.this.pos;
/* 252 */             int j = BatchEnvironment.Path.PathIterator.this.path.indexOf(File.pathSeparator, i);
/* 253 */             if (j == -1)
/* 254 */               j = BatchEnvironment.Path.PathIterator.this.path.length();
/* 255 */             BatchEnvironment.Path.PathIterator.this.pos = (j + 1);
/*     */ 
/* 257 */             if ((i == j) && (BatchEnvironment.Path.PathIterator.this.emptyPathDefault != null)) {
/* 258 */               return BatchEnvironment.Path.PathIterator.this.emptyPathDefault;
/*     */             }
/* 260 */             return BatchEnvironment.Path.PathIterator.this.path.substring(i, j);
/*     */           }
/*     */           public void remove() {
/* 263 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */ 
/*     */       public int size()
/*     */       {
/* 270 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean isEmpty() {
/* 273 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean contains(Object paramObject) {
/* 276 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public Object[] toArray() {
/* 279 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public <T> T[] toArray(T[] paramArrayOfT) {
/* 282 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean add(String paramString) {
/* 285 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean remove(Object paramObject) {
/* 288 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean containsAll(Collection<?> paramCollection) {
/* 291 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean addAll(Collection<? extends String> paramCollection) {
/* 294 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean removeAll(Collection<?> paramCollection) {
/* 297 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean retainAll(Collection<?> paramCollection) {
/* 300 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public void clear() {
/* 303 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean equals(Object paramObject) {
/* 306 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public int hashCode() {
/* 309 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.BatchEnvironment
 * JD-Core Version:    0.6.2
 */