/*    */ package com.sun.tools.javac.file;
/*    */ 
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.Attributes.Name;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ public class FSInfo
/*    */ {
/*    */   public static FSInfo instance(Context paramContext)
/*    */   {
/* 32 */     FSInfo localFSInfo = (FSInfo)paramContext.get(FSInfo.class);
/* 33 */     if (localFSInfo == null)
/* 34 */       localFSInfo = new FSInfo();
/* 35 */     return localFSInfo;
/*    */   }
/*    */ 
/*    */   protected FSInfo() {
/*    */   }
/*    */ 
/*    */   protected FSInfo(Context paramContext) {
/* 42 */     paramContext.put(FSInfo.class, this);
/*    */   }
/*    */ 
/*    */   public File getCanonicalFile(File paramFile) {
/*    */     try {
/* 47 */       return paramFile.getCanonicalFile(); } catch (IOException localIOException) {
/*    */     }
/* 49 */     return paramFile.getAbsoluteFile();
/*    */   }
/*    */ 
/*    */   public boolean exists(File paramFile)
/*    */   {
/* 54 */     return paramFile.exists();
/*    */   }
/*    */ 
/*    */   public boolean isDirectory(File paramFile) {
/* 58 */     return paramFile.isDirectory();
/*    */   }
/*    */ 
/*    */   public boolean isFile(File paramFile) {
/* 62 */     return paramFile.isFile();
/*    */   }
/*    */ 
/*    */   public List<File> getJarClassPath(File paramFile) throws IOException {
/* 66 */     String str1 = paramFile.getParent();
/* 67 */     JarFile localJarFile = new JarFile(paramFile);
/*    */     try {
/* 69 */       Manifest localManifest = localJarFile.getManifest();
/* 70 */       if (localManifest == null) {
/* 71 */         return Collections.emptyList();
/*    */       }
/* 73 */       Object localObject1 = localManifest.getMainAttributes();
/* 74 */       if (localObject1 == null) {
/* 75 */         return Collections.emptyList();
/*    */       }
/* 77 */       Object localObject2 = ((Attributes)localObject1).getValue(Attributes.Name.CLASS_PATH);
/* 78 */       if (localObject2 == null) {
/* 79 */         return Collections.emptyList();
/*    */       }
/* 81 */       Object localObject3 = new ArrayList();
/*    */ 
/* 83 */       for (Object localObject4 = new StringTokenizer((String)localObject2); ((StringTokenizer)localObject4).hasMoreTokens(); ) {
/* 84 */         String str2 = ((StringTokenizer)localObject4).nextToken();
/* 85 */         File localFile = str1 == null ? new File(str2) : new File(str1, str2);
/* 86 */         ((List)localObject3).add(localFile);
/*    */       }
/*    */ 
/* 89 */       return localObject3;
/*    */     } finally {
/* 91 */       localJarFile.close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.FSInfo
 * JD-Core Version:    0.6.2
 */