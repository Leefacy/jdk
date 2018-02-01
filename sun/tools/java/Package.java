/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class Package
/*     */ {
/*     */   ClassPath sourcePath;
/*     */   ClassPath binaryPath;
/*     */   String pkg;
/*     */ 
/*     */   public Package(ClassPath paramClassPath, Identifier paramIdentifier)
/*     */     throws IOException
/*     */   {
/*  60 */     this(paramClassPath, paramClassPath, paramIdentifier);
/*     */   }
/*     */ 
/*     */   public Package(ClassPath paramClassPath1, ClassPath paramClassPath2, Identifier paramIdentifier)
/*     */     throws IOException
/*     */   {
/*  71 */     if (paramIdentifier.isInner())
/*  72 */       paramIdentifier = Identifier.lookup(paramIdentifier.getQualifier(), paramIdentifier.getFlatName());
/*  73 */     this.sourcePath = paramClassPath1;
/*  74 */     this.binaryPath = paramClassPath2;
/*  75 */     this.pkg = paramIdentifier.toString().replace('.', File.separatorChar);
/*     */   }
/*     */ 
/*     */   public boolean classExists(Identifier paramIdentifier)
/*     */   {
/*  86 */     return (getBinaryFile(paramIdentifier) != null) || (
/*  85 */       (!paramIdentifier
/*  85 */       .isInner()) && 
/*  86 */       (getSourceFile(paramIdentifier) != null));
/*     */   }
/*     */ 
/*     */   public boolean exists()
/*     */   {
/*  94 */     ClassFile localClassFile = this.binaryPath.getDirectory(this.pkg);
/*  95 */     if ((localClassFile != null) && (localClassFile.isDirectory())) {
/*  96 */       return true;
/*     */     }
/*     */ 
/*  99 */     if (this.sourcePath != this.binaryPath)
/*     */     {
/* 101 */       localClassFile = this.sourcePath.getDirectory(this.pkg);
/* 102 */       if ((localClassFile != null) && (localClassFile.isDirectory())) {
/* 103 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     String str = this.pkg + File.separator;
/*     */ 
/* 114 */     return (this.binaryPath.getFiles(str, ".class").hasMoreElements()) || 
/* 114 */       (this.sourcePath
/* 114 */       .getFiles(str, ".java")
/* 114 */       .hasMoreElements());
/*     */   }
/*     */ 
/*     */   private String makeName(String paramString) {
/* 118 */     return this.pkg + File.separator + paramString;
/*     */   }
/*     */ 
/*     */   public ClassFile getBinaryFile(Identifier paramIdentifier)
/*     */   {
/* 125 */     paramIdentifier = Type.mangleInnerType(paramIdentifier);
/* 126 */     String str = paramIdentifier.toString() + ".class";
/* 127 */     return this.binaryPath.getFile(makeName(str));
/*     */   }
/*     */ 
/*     */   public ClassFile getSourceFile(Identifier paramIdentifier)
/*     */   {
/* 135 */     paramIdentifier = paramIdentifier.getTopName();
/* 136 */     String str = paramIdentifier.toString() + ".java";
/* 137 */     return this.sourcePath.getFile(makeName(str));
/*     */   }
/*     */ 
/*     */   public ClassFile getSourceFile(String paramString) {
/* 141 */     if (paramString.endsWith(".java")) {
/* 142 */       return this.sourcePath.getFile(makeName(paramString));
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration getSourceFiles() {
/* 148 */     return this.sourcePath.getFiles(this.pkg, ".java");
/*     */   }
/*     */ 
/*     */   public Enumeration getBinaryFiles() {
/* 152 */     return this.binaryPath.getFiles(this.pkg, ".class");
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 156 */     if (this.pkg.equals("")) {
/* 157 */       return "unnamed package";
/*     */     }
/* 159 */     return "package " + this.pkg;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Package
 * JD-Core Version:    0.6.2
 */