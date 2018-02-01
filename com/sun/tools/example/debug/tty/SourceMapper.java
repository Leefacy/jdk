/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ class SourceMapper
/*     */ {
/*     */   private final String[] dirs;
/*     */ 
/*     */   SourceMapper(List<String> paramList)
/*     */   {
/*  53 */     ArrayList localArrayList = new ArrayList();
/*  54 */     for (String str : paramList)
/*     */     {
/*  57 */       if ((!str.endsWith(".jar")) && 
/*  58 */         (!str
/*  58 */         .endsWith(".zip")))
/*     */       {
/*  59 */         localArrayList.add(str);
/*     */       }
/*     */     }
/*  62 */     this.dirs = ((String[])localArrayList.toArray(new String[0]));
/*     */   }
/*     */ 
/*     */   SourceMapper(String paramString)
/*     */   {
/*  74 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/*     */ 
/*  76 */     ArrayList localArrayList = new ArrayList();
/*  77 */     while (localStringTokenizer.hasMoreTokens()) {
/*  78 */       String str = localStringTokenizer.nextToken();
/*     */ 
/*  81 */       if ((!str.endsWith(".jar")) && 
/*  82 */         (!str
/*  82 */         .endsWith(".zip")))
/*     */       {
/*  83 */         localArrayList.add(str);
/*     */       }
/*     */     }
/*  86 */     this.dirs = ((String[])localArrayList.toArray(new String[0]));
/*     */   }
/*     */ 
/*     */   String getSourcePath()
/*     */   {
/*  93 */     int i = 0;
/*     */ 
/*  95 */     if (this.dirs.length < 1) {
/*  96 */       return "";
/*     */     }
/*  98 */     StringBuffer localStringBuffer = new StringBuffer(this.dirs[(i++)]);
/*     */ 
/* 100 */     for (; i < this.dirs.length; i++) {
/* 101 */       localStringBuffer.append(File.pathSeparator);
/* 102 */       localStringBuffer.append(this.dirs[i]);
/*     */     }
/* 104 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   File sourceFile(Location paramLocation)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       String str1 = paramLocation.sourceName();
/* 114 */       String str2 = paramLocation.declaringType().name();
/* 115 */       int i = str2.lastIndexOf('.');
/* 116 */       String str3 = i >= 0 ? str2.substring(0, i + 1) : "";
/* 117 */       String str4 = str3.replace('.', File.separatorChar) + str1;
/* 118 */       for (int j = 0; j < this.dirs.length; j++) {
/* 119 */         File localFile = new File(this.dirs[j], str4);
/* 120 */         if (localFile.exists()) {
/* 121 */           return localFile;
/*     */         }
/*     */       }
/* 124 */       return null; } catch (AbsentInformationException localAbsentInformationException) {
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   BufferedReader sourceReader(Location paramLocation)
/*     */   {
/* 137 */     File localFile = sourceFile(paramLocation);
/* 138 */     if (localFile == null)
/* 139 */       return null;
/*     */     try
/*     */     {
/* 142 */       return new BufferedReader(new FileReader(localFile));
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 145 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.SourceMapper
 * JD-Core Version:    0.6.2
 */