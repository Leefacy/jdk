/*     */ package com.sun.tools.javac.sym;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public abstract class Profiles
/*     */ {
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/*  55 */     Profiles localProfiles = read(new File(paramArrayOfString[0]));
/*  56 */     if (paramArrayOfString.length >= 2) {
/*  57 */       TreeMap localTreeMap = new TreeMap();
/*  58 */       for (int i = 1; i <= 4; i++) {
/*  59 */         localTreeMap.put(Integer.valueOf(i), new TreeSet());
/*     */       }
/*  61 */       File localFile = new File(paramArrayOfString[1]);
/*  62 */       for (Iterator localIterator = Files.readAllLines(localFile.toPath(), Charset.defaultCharset()).iterator(); localIterator.hasNext(); ) { localObject1 = (String)localIterator.next();
/*  63 */         if (((String)localObject1).endsWith(".class")) {
/*  64 */           localObject2 = ((String)localObject1).substring(0, ((String)localObject1).length() - 6);
/*  65 */           int k = localProfiles.getProfile((String)localObject2);
/*  66 */           for (int m = k; m <= 4; m++)
/*  67 */             ((Set)localTreeMap.get(Integer.valueOf(m))).add(localObject2);
/*     */         }
/*     */       }
/*     */       Object localObject1;
/*     */       Object localObject2;
/*  71 */       for (int j = 1; j <= 4; j++) {
/*  72 */         localObject1 = new BufferedWriter(new FileWriter(j + ".txt"));
/*     */         try {
/*  74 */           for (localObject2 = ((Set)localTreeMap.get(Integer.valueOf(j))).iterator(); ((Iterator)localObject2).hasNext(); ) { String str = (String)((Iterator)localObject2).next();
/*  75 */             ((BufferedWriter)localObject1).write(str);
/*  76 */             ((BufferedWriter)localObject1).newLine(); }
/*     */         }
/*     */         finally {
/*  79 */           ((BufferedWriter)localObject1).close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Profiles read(File paramFile) throws IOException {
/*  86 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*     */     try {
/*  88 */       Properties localProperties = new Properties();
/*  89 */       localProperties.load(localBufferedInputStream);
/*     */       Object localObject1;
/*  90 */       if (localProperties.containsKey("java/lang/Object")) {
/*  91 */         return new SimpleProfiles(localProperties);
/*     */       }
/*  93 */       return new MakefileProfiles(localProperties);
/*     */     } finally {
/*  95 */       localBufferedInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract int getProfileCount();
/*     */ 
/*     */   public abstract int getProfile(String paramString);
/*     */ 
/*     */   public abstract Set<String> getPackages(int paramInt);
/*     */ 
/*     */   private static class MakefileProfiles extends Profiles
/*     */   {
/* 151 */     final Map<String, Package> packages = new TreeMap();
/*     */ 
/* 153 */     final int maxProfile = 4;
/*     */ 
/*     */     MakefileProfiles(Properties paramProperties)
/*     */     {
/* 157 */       int i = 0;
/* 158 */       for (int j = 1; j <= 4; j++) {
/* 159 */         String str1 = j < 4 ? "PROFILE_" + j : "FULL_JRE";
/* 160 */         String str2 = paramProperties.getProperty(str1 + "_RTJAR_INCLUDE_PACKAGES");
/* 161 */         if (str2 == null)
/*     */           break;
/* 163 */         for (String str3 : str2.substring(1).trim().split("\\s+")) {
/* 164 */           if (str3.endsWith("/"))
/* 165 */             str3 = str3.substring(0, str3.length() - 1);
/* 166 */           if ((i == 0) && (str3.equals("java/lang")))
/* 167 */             i = 1;
/* 168 */           includePackage(j, str3);
/*     */         }
/* 170 */         ??? = paramProperties.getProperty(str1 + "_RTJAR_INCLUDE_TYPES");
/* 171 */         if (??? != null) {
/* 172 */           for (Object localObject3 : ((String)???).replace("$$", "$").split("\\s+")) {
/* 173 */             if (localObject3.endsWith(".class"))
/* 174 */               includeType(j, localObject3.substring(0, localObject3.length() - 6));
/*     */           }
/*     */         }
/* 177 */         ??? = paramProperties.getProperty(str1 + "_RTJAR_EXCLUDE_TYPES");
/* 178 */         if (??? != null) {
/* 179 */           for (String str4 : ((String)???).replace("$$", "$").split("\\s+")) {
/* 180 */             if (str4.endsWith(".class")) {
/* 181 */               excludeType(j, str4.substring(0, str4.length() - 6));
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 192 */       if (i != 0)
/* 193 */         includePackage(1, "javax/crypto");
/*     */     }
/*     */ 
/*     */     public int getProfileCount()
/*     */     {
/* 198 */       return 4;
/*     */     }
/*     */ 
/*     */     public int getProfile(String paramString)
/*     */     {
/* 203 */       int i = paramString.lastIndexOf("/");
/* 204 */       String str1 = paramString.substring(0, i);
/* 205 */       String str2 = paramString.substring(i + 1);
/*     */ 
/* 207 */       Package localPackage = getPackage(str1);
/* 208 */       return localPackage.getProfile(str2);
/*     */     }
/*     */ 
/*     */     public Set<String> getPackages(int paramInt)
/*     */     {
/* 213 */       TreeSet localTreeSet = new TreeSet();
/* 214 */       for (Package localPackage : this.packages.values())
/* 215 */         localPackage.getPackages(paramInt, localTreeSet);
/* 216 */       return localTreeSet;
/*     */     }
/*     */ 
/*     */     private void includePackage(int paramInt, String paramString)
/*     */     {
/* 221 */       Package localPackage = getPackage(paramString);
/* 222 */       Assert.check(localPackage.profile == 0);
/* 223 */       localPackage.profile = paramInt;
/*     */     }
/*     */ 
/*     */     private void includeType(int paramInt, String paramString)
/*     */     {
/* 228 */       int i = paramString.lastIndexOf("/");
/* 229 */       String str1 = paramString.substring(0, i);
/* 230 */       String str2 = paramString.substring(i + 1);
/*     */ 
/* 232 */       Package localPackage = getPackage(str1);
/* 233 */       Assert.check(!localPackage.includedTypes.containsKey(str2));
/* 234 */       localPackage.includedTypes.put(str2, Integer.valueOf(paramInt));
/*     */     }
/*     */ 
/*     */     private void excludeType(int paramInt, String paramString)
/*     */     {
/* 239 */       int i = paramString.lastIndexOf("/");
/* 240 */       String str1 = paramString.substring(0, i);
/* 241 */       String str2 = paramString.substring(i + 1);
/*     */ 
/* 243 */       Package localPackage = getPackage(str1);
/* 244 */       Assert.check(!localPackage.excludedTypes.containsKey(str2));
/* 245 */       localPackage.excludedTypes.put(str2, Integer.valueOf(paramInt));
/*     */     }
/*     */ 
/*     */     private Package getPackage(String paramString) {
/* 249 */       int i = paramString.lastIndexOf("/");
/*     */       Package localPackage1;
/*     */       Map localMap;
/*     */       String str;
/* 253 */       if (i == -1) {
/* 254 */         localPackage1 = null;
/* 255 */         localMap = this.packages;
/* 256 */         str = paramString;
/*     */       } else {
/* 258 */         localPackage1 = getPackage(paramString.substring(0, i));
/* 259 */         localMap = localPackage1.subpackages;
/* 260 */         str = paramString.substring(i + 1);
/*     */       }
/*     */ 
/* 263 */       Package localPackage2 = (Package)localMap.get(str);
/* 264 */       if (localPackage2 == null) {
/* 265 */         localMap.put(str, localPackage2 = new Package(localPackage1, str));
/*     */       }
/* 267 */       return localPackage2;
/*     */     }
/*     */ 
/*     */     static class Package
/*     */     {
/*     */       final Package parent;
/*     */       final String name;
/* 110 */       Map<String, Package> subpackages = new TreeMap();
/*     */       int profile;
/* 113 */       Map<String, Integer> includedTypes = new TreeMap();
/* 114 */       Map<String, Integer> excludedTypes = new TreeMap();
/*     */ 
/*     */       Package(Package paramPackage, String paramString) {
/* 117 */         this.parent = paramPackage;
/* 118 */         this.name = paramString;
/*     */       }
/*     */ 
/*     */       int getProfile() {
/* 122 */         return this.parent == null ? this.profile : Math.max(this.parent.getProfile(), this.profile);
/*     */       }
/*     */ 
/*     */       int getProfile(String paramString)
/*     */       {
/*     */         Integer localInteger;
/* 127 */         if ((localInteger = (Integer)this.includedTypes.get(paramString)) != null)
/* 128 */           return localInteger.intValue();
/* 129 */         if ((localInteger = (Integer)this.includedTypes.get("*")) != null)
/* 130 */           return localInteger.intValue();
/* 131 */         if ((localInteger = (Integer)this.excludedTypes.get(paramString)) != null)
/* 132 */           return localInteger.intValue() + 1;
/* 133 */         if ((localInteger = (Integer)this.excludedTypes.get("*")) != null)
/* 134 */           return localInteger.intValue() + 1;
/* 135 */         return getProfile();
/*     */       }
/*     */ 
/*     */       String getName() {
/* 139 */         return this.parent.getName() + "/" + this.name;
/*     */       }
/*     */ 
/*     */       void getPackages(int paramInt, Set<String> paramSet) {
/* 143 */         int i = getProfile();
/* 144 */         if ((i != 0) && (paramInt >= i))
/* 145 */           paramSet.add(getName());
/* 146 */         for (Package localPackage : this.subpackages.values())
/* 147 */           localPackage.getPackages(paramInt, paramSet);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SimpleProfiles extends Profiles
/*     */   {
/*     */     private final Map<String, Integer> map;
/*     */     private final int profileCount;
/*     */ 
/*     */     SimpleProfiles(Properties paramProperties)
/*     */     {
/* 276 */       int i = 0;
/* 277 */       this.map = new HashMap();
/* 278 */       for (Map.Entry localEntry : paramProperties.entrySet()) {
/* 279 */         String str = (String)localEntry.getKey();
/* 280 */         int j = Integer.valueOf((String)localEntry.getValue()).intValue();
/* 281 */         this.map.put(str, Integer.valueOf(j));
/* 282 */         i = Math.max(i, j);
/*     */       }
/* 284 */       this.profileCount = i;
/*     */     }
/*     */ 
/*     */     public int getProfileCount()
/*     */     {
/* 289 */       return this.profileCount;
/*     */     }
/*     */ 
/*     */     public int getProfile(String paramString)
/*     */     {
/* 294 */       return ((Integer)this.map.get(paramString)).intValue();
/*     */     }
/*     */ 
/*     */     public Set<String> getPackages(int paramInt)
/*     */     {
/* 299 */       TreeSet localTreeSet = new TreeSet();
/* 300 */       for (Map.Entry localEntry : this.map.entrySet()) {
/* 301 */         String str = (String)localEntry.getKey();
/* 302 */         int i = ((Integer)localEntry.getValue()).intValue();
/* 303 */         int j = str.lastIndexOf("/");
/* 304 */         if ((j > 0) && (paramInt >= i))
/* 305 */           localTreeSet.add(str);
/*     */       }
/* 307 */       return localTreeSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.sym.Profiles
 * JD-Core Version:    0.6.2
 */