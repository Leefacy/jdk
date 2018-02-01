/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.Annotation;
/*     */ import com.sun.tools.classfile.Annotation.Primitive_element_value;
/*     */ import com.sun.tools.classfile.Annotation.element_value_pair;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Integer_info;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Dependencies.ClassFileError;
/*     */ import com.sun.tools.classfile.RuntimeAnnotations_attribute;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ class PlatformClassPath
/*     */ {
/*  49 */   private static final List<String> NON_PLATFORM_JARFILES = Arrays.asList(new String[] { "alt-rt.jar", "ant-javafx.jar", "javafx-mx.jar" })
/*  49 */     ;
/*  50 */   private static final List<Archive> javaHomeArchives = init();
/*     */ 
/*     */   static List<Archive> getArchives() {
/*  53 */     return javaHomeArchives;
/*     */   }
/*     */ 
/*     */   private static List<Archive> init() {
/*  57 */     ArrayList localArrayList = new ArrayList();
/*  58 */     Path localPath1 = Paths.get(System.getProperty("java.home"), new String[0]);
/*     */     try
/*     */     {
/*     */       Path localPath2;
/*  60 */       if (localPath1.endsWith("jre"))
/*     */       {
/*  62 */         localArrayList.addAll(addJarFiles(localPath1.resolve("lib")));
/*  63 */         if (localPath1.getParent() != null)
/*     */         {
/*  65 */           localPath2 = localPath1.getParent().resolve("lib");
/*  66 */           if (Files.exists(localPath2, new LinkOption[0]))
/*  67 */             localArrayList.addAll(addJarFiles(localPath2));
/*     */         }
/*     */       }
/*  70 */       else if (Files.exists(localPath1.resolve("lib"), new LinkOption[0]))
/*     */       {
/*  72 */         localPath2 = localPath1.resolve("classes");
/*  73 */         if (Files.isDirectory(localPath2, new LinkOption[0]))
/*     */         {
/*  75 */           localArrayList.add(new JDKArchive(localPath2));
/*     */         }
/*     */ 
/*  78 */         localArrayList.addAll(addJarFiles(localPath1.resolve("lib")));
/*     */       } else {
/*  80 */         throw new RuntimeException("\"" + localPath1 + "\" not a JDK home");
/*     */       }
/*  82 */       return localArrayList;
/*     */     } catch (IOException localIOException) {
/*  84 */       throw new Error(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static List<Archive> addJarFiles(Path paramPath) throws IOException {
/*  89 */     final ArrayList localArrayList = new ArrayList();
/*  90 */     final Path localPath = paramPath.resolve("ext");
/*  91 */     Files.walkFileTree(paramPath, new SimpleFileVisitor()
/*     */     {
/*     */       public FileVisitResult preVisitDirectory(Path paramAnonymousPath, BasicFileAttributes paramAnonymousBasicFileAttributes)
/*     */         throws IOException
/*     */       {
/*  96 */         if ((paramAnonymousPath.equals(this.val$root)) || (paramAnonymousPath.equals(localPath))) {
/*  97 */           return FileVisitResult.CONTINUE;
/*     */         }
/*     */ 
/* 100 */         return FileVisitResult.SKIP_SUBTREE;
/*     */       }
/*     */ 
/*     */       public FileVisitResult visitFile(Path paramAnonymousPath, BasicFileAttributes paramAnonymousBasicFileAttributes)
/*     */         throws IOException
/*     */       {
/* 107 */         String str = paramAnonymousPath.getFileName().toString();
/* 108 */         if (str.endsWith(".jar"))
/*     */         {
/* 111 */           localArrayList.add(PlatformClassPath.NON_PLATFORM_JARFILES.contains(str) ? 
/* 112 */             Archive.getInstance(paramAnonymousPath) : 
/* 112 */             new PlatformClassPath.JDKArchive(paramAnonymousPath));
/*     */         }
/*     */ 
/* 115 */         return FileVisitResult.CONTINUE;
/*     */       }
/*     */     });
/* 118 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static class JDKArchive extends Archive
/*     */   {
/* 126 */     private static List<String> PROFILE_JARS = Arrays.asList(new String[] { "rt.jar", "jce.jar" });
/*     */ 
/* 128 */     private static List<String> EXPORTED_PACKAGES = Arrays.asList(new String[] { "javax.jnlp", "org.w3c.dom.css", "org.w3c.dom.html", "org.w3c.dom.stylesheets", "org.w3c.dom.xpath" });
/*     */ 
/* 142 */     private final Map<String, Boolean> exportedPackages = new HashMap();
/* 143 */     private final Map<String, Boolean> exportedTypes = new HashMap();
/*     */     private static final String JDK_EXPORTED_ANNOTATION = "Ljdk/Exported;";
/*     */ 
/*     */     public static boolean isProfileArchive(Archive paramArchive) {
/* 136 */       if ((paramArchive instanceof JDKArchive)) {
/* 137 */         return PROFILE_JARS.contains(paramArchive.getName());
/*     */       }
/* 139 */       return false;
/*     */     }
/*     */ 
/*     */     JDKArchive(Path paramPath)
/*     */       throws IOException
/*     */     {
/* 145 */       super(ClassFileReader.newInstance(paramPath));
/*     */     }
/*     */ 
/*     */     public boolean isExported(String paramString)
/*     */     {
/* 152 */       int i = paramString.lastIndexOf('.');
/* 153 */       String str = i > 0 ? paramString.substring(0, i) : "";
/*     */ 
/* 155 */       boolean bool = isExportedPackage(str);
/* 156 */       if (this.exportedTypes.containsKey(paramString)) {
/* 157 */         return ((Boolean)this.exportedTypes.get(paramString)).booleanValue();
/*     */       }
/* 159 */       return bool;
/*     */     }
/*     */ 
/*     */     public boolean isExportedPackage(String paramString)
/*     */     {
/* 166 */       if (Profile.getProfile(paramString) != null) {
/* 167 */         return true;
/*     */       }
/*     */ 
/* 170 */       if ((EXPORTED_PACKAGES.contains(paramString)) || (paramString.startsWith("javafx."))) {
/* 171 */         return true;
/*     */       }
/* 173 */       return this.exportedPackages.containsKey(paramString) ? ((Boolean)this.exportedPackages.get(paramString)).booleanValue() : false;
/*     */     }
/*     */ 
/*     */     private Boolean isJdkExported(ClassFile paramClassFile)
/*     */       throws ConstantPoolException
/*     */     {
/* 179 */       RuntimeAnnotations_attribute localRuntimeAnnotations_attribute = (RuntimeAnnotations_attribute)paramClassFile.attributes
/* 179 */         .get("RuntimeVisibleAnnotations");
/*     */ 
/* 180 */       if (localRuntimeAnnotations_attribute != null) {
/* 181 */         for (int i = 0; i < localRuntimeAnnotations_attribute.annotations.length; i++) {
/* 182 */           Annotation localAnnotation = localRuntimeAnnotations_attribute.annotations[i];
/* 183 */           String str = paramClassFile.constant_pool.getUTF8Value(localAnnotation.type_index);
/* 184 */           if ("Ljdk/Exported;".equals(str)) {
/* 185 */             boolean bool = true;
/* 186 */             for (int j = 0; j < localAnnotation.num_element_value_pairs; j++) {
/* 187 */               Annotation.element_value_pair localelement_value_pair = localAnnotation.element_value_pairs[j];
/* 188 */               Annotation.Primitive_element_value localPrimitive_element_value = (Annotation.Primitive_element_value)localelement_value_pair.value;
/*     */ 
/* 190 */               ConstantPool.CONSTANT_Integer_info localCONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)paramClassFile.constant_pool
/* 190 */                 .get(localPrimitive_element_value.const_value_index);
/*     */ 
/* 191 */               bool = localCONSTANT_Integer_info.value != 0;
/*     */             }
/* 193 */             return Boolean.valueOf(bool);
/*     */           }
/*     */         }
/*     */       }
/* 197 */       return null;
/*     */     }
/*     */ 
/*     */     void processJdkExported(ClassFile paramClassFile) throws IOException {
/*     */       try {
/* 202 */         String str1 = paramClassFile.getName();
/* 203 */         String str2 = str1.substring(0, str1.lastIndexOf('/')).replace('/', '.');
/*     */ 
/* 205 */         Boolean localBoolean1 = isJdkExported(paramClassFile);
/* 206 */         if (localBoolean1 != null) {
/* 207 */           this.exportedTypes.put(str1.replace('/', '.'), localBoolean1);
/*     */         }
/* 209 */         if (!this.exportedPackages.containsKey(str2))
/*     */         {
/* 211 */           Boolean localBoolean2 = null;
/* 212 */           ClassFile localClassFile = reader().getClassFile(str1.substring(0, str1.lastIndexOf('/') + 1) + "package-info");
/* 213 */           if (localClassFile != null) {
/* 214 */             localBoolean2 = isJdkExported(localClassFile);
/*     */           }
/* 216 */           if (localBoolean2 != null)
/* 217 */             this.exportedPackages.put(str2, localBoolean2);
/*     */         }
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/* 221 */         throw new Dependencies.ClassFileError(localConstantPoolException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.PlatformClassPath
 * JD-Core Version:    0.6.2
 */