/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Dependencies.ClassFileError;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class ClassFileReader
/*     */ {
/*     */   protected final Path path;
/*     */   protected final String baseFileName;
/*  71 */   protected final List<String> skippedEntries = new ArrayList();
/*     */ 
/*     */   public static ClassFileReader newInstance(Path paramPath)
/*     */     throws IOException
/*     */   {
/*  49 */     if (!Files.exists(paramPath, new LinkOption[0])) {
/*  50 */       throw new FileNotFoundException(paramPath.toString());
/*     */     }
/*     */ 
/*  53 */     if (Files.isDirectory(paramPath, new LinkOption[0]))
/*  54 */       return new DirectoryReader(paramPath);
/*  55 */     if (paramPath.getFileName().toString().endsWith(".jar")) {
/*  56 */       return new JarFileReader(paramPath);
/*     */     }
/*  58 */     return new ClassFileReader(paramPath);
/*     */   }
/*     */ 
/*     */   public static ClassFileReader newInstance(Path paramPath, JarFile paramJarFile)
/*     */     throws IOException
/*     */   {
/*  66 */     return new JarFileReader(paramPath, paramJarFile);
/*     */   }
/*     */ 
/*     */   protected ClassFileReader(Path paramPath)
/*     */   {
/*  73 */     this.path = paramPath;
/*  74 */     this.baseFileName = (paramPath.getFileName() != null ? paramPath
/*  75 */       .getFileName().toString() : paramPath
/*  76 */       .toString());
/*     */   }
/*     */ 
/*     */   public String getFileName() {
/*  80 */     return this.baseFileName;
/*     */   }
/*     */ 
/*     */   public List<String> skippedEntries() {
/*  84 */     return this.skippedEntries;
/*     */   }
/*     */ 
/*     */   public ClassFile getClassFile(String paramString)
/*     */     throws IOException
/*     */   {
/*  92 */     if (paramString.indexOf('.') > 0) {
/*  93 */       int i = paramString.lastIndexOf('.');
/*  94 */       String str = paramString.replace('.', File.separatorChar) + ".class";
/*  95 */       if ((this.baseFileName.equals(str)) || 
/*  96 */         (this.baseFileName
/*  96 */         .equals(str
/*  96 */         .substring(0, i) + 
/*  96 */         "$" + str
/*  97 */         .substring(i + 1, str
/*  97 */         .length())))) {
/*  98 */         return readClassFile(this.path);
/*     */       }
/*     */     }
/* 101 */     else if (this.baseFileName.equals(paramString.replace('/', File.separatorChar) + ".class")) {
/* 102 */       return readClassFile(this.path);
/*     */     }
/*     */ 
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterable<ClassFile> getClassFiles() throws IOException {
/* 109 */     return new Iterable() {
/*     */       public Iterator<ClassFile> iterator() {
/* 111 */         return new ClassFileReader.FileIterator(ClassFileReader.this);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected ClassFile readClassFile(Path paramPath) throws IOException {
/* 117 */     InputStream localInputStream = null;
/*     */     try {
/* 119 */       localInputStream = Files.newInputStream(paramPath, new OpenOption[0]);
/* 120 */       return ClassFile.read(localInputStream);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 122 */       throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */     } finally {
/* 124 */       if (localInputStream != null)
/* 125 */         localInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 158 */     return this.path.toString();
/*     */   }
/*     */ 
/*     */   private static class DirectoryReader extends ClassFileReader {
/*     */     DirectoryReader(Path paramPath) throws IOException {
/* 163 */       super();
/*     */     }
/*     */ 
/*     */     public ClassFile getClassFile(String paramString) throws IOException {
/* 167 */       if (paramString.indexOf('.') > 0) {
/* 168 */         int i = paramString.lastIndexOf('.');
/* 169 */         String str = paramString.replace('.', File.separatorChar) + ".class";
/* 170 */         Path localPath2 = this.path.resolve(str);
/* 171 */         if (!Files.exists(localPath2, new LinkOption[0])) {
/* 172 */           localPath2 = this.path.resolve(str.substring(0, i) + "$" + str
/* 173 */             .substring(i + 1, str
/* 173 */             .length()));
/*     */         }
/* 175 */         if (Files.exists(localPath2, new LinkOption[0]))
/* 176 */           return readClassFile(localPath2);
/*     */       }
/*     */       else {
/* 179 */         Path localPath1 = this.path.resolve(paramString + ".class");
/* 180 */         if (Files.exists(localPath1, new LinkOption[0])) {
/* 181 */           return readClassFile(localPath1);
/*     */         }
/*     */       }
/* 184 */       return null;
/*     */     }
/*     */ 
/*     */     public Iterable<ClassFile> getClassFiles() throws IOException {
/* 188 */       final DirectoryIterator localDirectoryIterator = new DirectoryIterator();
/* 189 */       return new Iterable() {
/*     */         public Iterator<ClassFile> iterator() {
/* 191 */           return localDirectoryIterator;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     private List<Path> walkTree(Path paramPath) throws IOException {
/* 197 */       final ArrayList localArrayList = new ArrayList();
/* 198 */       Files.walkFileTree(paramPath, new SimpleFileVisitor()
/*     */       {
/*     */         public FileVisitResult visitFile(Path paramAnonymousPath, BasicFileAttributes paramAnonymousBasicFileAttributes) throws IOException {
/* 201 */           if (paramAnonymousPath.getFileName().toString().endsWith(".class")) {
/* 202 */             localArrayList.add(paramAnonymousPath);
/*     */           }
/* 204 */           return FileVisitResult.CONTINUE;
/*     */         }
/*     */       });
/* 207 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     class DirectoryIterator
/*     */       implements Iterator<ClassFile>
/*     */     {
/*     */       private List<Path> entries;
/* 212 */       private int index = 0;
/*     */ 
/* 214 */       DirectoryIterator() throws IOException { this.entries = ClassFileReader.DirectoryReader.this.walkTree(ClassFileReader.DirectoryReader.this.path);
/* 215 */         this.index = 0; }
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 219 */         return this.index != this.entries.size();
/*     */       }
/*     */ 
/*     */       public ClassFile next() {
/* 223 */         if (!hasNext()) {
/* 224 */           throw new NoSuchElementException();
/*     */         }
/* 226 */         Path localPath = (Path)this.entries.get(this.index++);
/*     */         try {
/* 228 */           return ClassFileReader.DirectoryReader.this.readClassFile(localPath);
/*     */         } catch (IOException localIOException) {
/* 230 */           throw new Dependencies.ClassFileError(localIOException);
/*     */         }
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 235 */         throw new UnsupportedOperationException("Not supported yet.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class FileIterator
/*     */     implements Iterator<ClassFile>
/*     */   {
/*     */     int count;
/*     */ 
/*     */     FileIterator()
/*     */     {
/* 133 */       this.count = 0;
/*     */     }
/*     */     public boolean hasNext() {
/* 136 */       return (this.count == 0) && (ClassFileReader.this.baseFileName.endsWith(".class"));
/*     */     }
/*     */ 
/*     */     public ClassFile next() {
/* 140 */       if (!hasNext())
/* 141 */         throw new NoSuchElementException();
/*     */       try
/*     */       {
/* 144 */         ClassFile localClassFile = ClassFileReader.this.readClassFile(ClassFileReader.this.path);
/* 145 */         this.count += 1;
/* 146 */         return localClassFile;
/*     */       } catch (IOException localIOException) {
/* 148 */         throw new Dependencies.ClassFileError(localIOException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 153 */       throw new UnsupportedOperationException("Not supported yet.");
/*     */     }
/*     */   }
/*     */ 
/*     */   class JarFileIterator
/*     */     implements Iterator<ClassFile>
/*     */   {
/*     */     protected final ClassFileReader.JarFileReader reader;
/*     */     protected Enumeration<JarEntry> entries;
/*     */     protected JarFile jf;
/*     */     protected JarEntry nextEntry;
/*     */     protected ClassFile cf;
/*     */ 
/*     */     JarFileIterator(ClassFileReader.JarFileReader arg2)
/*     */     {
/* 302 */       this(localJarFileReader, null);
/*     */     }
/*     */     JarFileIterator(ClassFileReader.JarFileReader paramJarFile, JarFile arg3) {
/* 305 */       this.reader = paramJarFile;
/*     */       JarFile localJarFile;
/* 306 */       setJarFile(localJarFile);
/*     */     }
/*     */ 
/*     */     void setJarFile(JarFile paramJarFile) {
/* 310 */       if (paramJarFile == null) return;
/*     */ 
/* 312 */       this.jf = paramJarFile;
/* 313 */       this.entries = this.jf.entries();
/* 314 */       this.nextEntry = nextEntry();
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 318 */       if ((this.nextEntry != null) && (this.cf != null)) {
/* 319 */         return true;
/*     */       }
/* 321 */       while (this.nextEntry != null) {
/*     */         try {
/* 323 */           this.cf = this.reader.readClassFile(this.jf, this.nextEntry);
/* 324 */           return true;
/*     */         } catch (Dependencies.ClassFileError|IOException localClassFileError) {
/* 326 */           ClassFileReader.this.skippedEntries.add(this.nextEntry.getName());
/*     */         }
/* 328 */         this.nextEntry = nextEntry();
/*     */       }
/* 330 */       return false;
/*     */     }
/*     */ 
/*     */     public ClassFile next() {
/* 334 */       if (!hasNext()) {
/* 335 */         throw new NoSuchElementException();
/*     */       }
/* 337 */       ClassFile localClassFile = this.cf;
/* 338 */       this.cf = null;
/* 339 */       this.nextEntry = nextEntry();
/* 340 */       return localClassFile;
/*     */     }
/*     */ 
/*     */     protected JarEntry nextEntry() {
/* 344 */       while (this.entries.hasMoreElements()) {
/* 345 */         JarEntry localJarEntry = (JarEntry)this.entries.nextElement();
/* 346 */         String str = localJarEntry.getName();
/* 347 */         if (str.endsWith(".class")) {
/* 348 */           return localJarEntry;
/*     */         }
/*     */       }
/* 351 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 355 */       throw new UnsupportedOperationException("Not supported yet.");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class JarFileReader extends ClassFileReader
/*     */   {
/*     */     private final JarFile jarfile;
/*     */ 
/*     */     JarFileReader(Path paramPath)
/*     */       throws IOException
/*     */     {
/* 243 */       this(paramPath, new JarFile(paramPath.toFile(), false));
/*     */     }
/*     */ 
/*     */     JarFileReader(Path paramPath, JarFile paramJarFile) throws IOException {
/* 247 */       super();
/* 248 */       this.jarfile = paramJarFile;
/*     */     }
/*     */ 
/*     */     public ClassFile getClassFile(String paramString) throws IOException {
/* 252 */       if (paramString.indexOf('.') > 0) {
/* 253 */         int i = paramString.lastIndexOf('.');
/* 254 */         String str = paramString.replace('.', '/') + ".class";
/* 255 */         JarEntry localJarEntry2 = this.jarfile.getJarEntry(str);
/* 256 */         if (localJarEntry2 == null) {
/* 257 */           localJarEntry2 = this.jarfile.getJarEntry(str.substring(0, i) + "$" + str
/* 258 */             .substring(i + 1, str
/* 258 */             .length()));
/*     */         }
/* 260 */         if (localJarEntry2 != null)
/* 261 */           return readClassFile(this.jarfile, localJarEntry2);
/*     */       }
/*     */       else {
/* 264 */         JarEntry localJarEntry1 = this.jarfile.getJarEntry(paramString + ".class");
/* 265 */         if (localJarEntry1 != null) {
/* 266 */           return readClassFile(this.jarfile, localJarEntry1);
/*     */         }
/*     */       }
/* 269 */       return null;
/*     */     }
/*     */ 
/*     */     protected ClassFile readClassFile(JarFile paramJarFile, JarEntry paramJarEntry) throws IOException {
/* 273 */       InputStream localInputStream = null;
/*     */       try {
/* 275 */         localInputStream = paramJarFile.getInputStream(paramJarEntry);
/* 276 */         return ClassFile.read(localInputStream);
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 278 */         throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */       } finally {
/* 280 */         if (localInputStream != null)
/* 281 */           localInputStream.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     public Iterable<ClassFile> getClassFiles() throws IOException {
/* 286 */       final ClassFileReader.JarFileIterator localJarFileIterator = new ClassFileReader.JarFileIterator(this, this, this.jarfile);
/* 287 */       return new Iterable() {
/*     */         public Iterator<ClassFile> iterator() {
/* 289 */           return localJarFileIterator;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.ClassFileReader
 * JD-Core Version:    0.6.2
 */