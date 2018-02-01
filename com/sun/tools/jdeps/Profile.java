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
/*     */ import com.sun.tools.classfile.RuntimeAnnotations_attribute;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */  enum Profile
/*     */ {
/*  46 */   COMPACT1("compact1", 1), 
/*  47 */   COMPACT2("compact2", 2), 
/*  48 */   COMPACT3("compact3", 3), 
/*  49 */   FULL_JRE("Full JRE", 4);
/*     */ 
/*     */   final String name;
/*     */   final int profile;
/*     */   final Set<String> packages;
/*     */   final Set<String> proprietaryPkgs;
/*     */ 
/*  57 */   private Profile(String paramString, int paramInt) { this.name = paramString;
/*  58 */     this.profile = paramInt;
/*  59 */     this.packages = new HashSet();
/*  60 */     this.proprietaryPkgs = new HashSet(); }
/*     */ 
/*     */   public String profileName()
/*     */   {
/*  64 */     return this.name;
/*     */   }
/*     */ 
/*     */   public static int getProfileCount() {
/*  68 */     return PackageToProfile.map.values().size();
/*     */   }
/*     */ 
/*     */   public static Profile getProfile(String paramString)
/*     */   {
/*  76 */     Profile localProfile = (Profile)PackageToProfile.map.get(paramString);
/*  77 */     return (localProfile != null) && (localProfile.packages.contains(paramString)) ? localProfile : null;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*     */     String str1;
/* 208 */     if (paramArrayOfString.length == 0) {
/* 209 */       if (getProfileCount() == 0)
/* 210 */         System.err.println("No profile is present in this JDK");
/*     */       String str2;
/* 212 */       for (str1 : values()) {
/* 213 */         str2 = str1.name;
/* 214 */         TreeSet localTreeSet = new TreeSet(str1.packages);
/* 215 */         for (String str3 : localTreeSet)
/*     */         {
/* 218 */           if (PackageToProfile.map.get(str3) == str1) {
/* 219 */             System.out.format("%2d: %-10s  %s%n", new Object[] { Integer.valueOf(str1.profile), str2, str3 });
/* 220 */             str2 = "";
/*     */           } else {
/* 222 */             System.err.format("Split package: %s in %s and %s %n", new Object[] { str3, 
/* 223 */               ((Profile)PackageToProfile.map
/* 223 */               .get(str3)).name, 
/* 223 */               str1.name });
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 228 */     for (str1 : paramArrayOfString)
/* 229 */       System.out.format("%s in %s%n", new Object[] { str1, getProfile(str1) });
/*     */   }
/*     */ 
/*     */   static class PackageToProfile
/*     */   {
/*  82 */     static String[] JAVAX_CRYPTO_PKGS = { "javax.crypto", "javax.crypto.interfaces", "javax.crypto.spec" };
/*     */ 
/*  87 */     static Map<String, Profile> map = initProfiles();
/*     */     private static final String PROFILE_ANNOTATION = "Ljdk/Profile+Annotation;";
/*     */     private static final String PROPRIETARY_ANNOTATION = "Lsun/Proprietary+Annotation;";
/*     */ 
/*     */     private static Map<String, Profile> initProfiles()
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/*     */       Object localObject3;
/*     */       try
/*     */       {
/*  90 */         String str = System.getProperty("jdeps.profiles");
/*  91 */         if (str != null)
/*     */         {
/*  93 */           initProfilesFromProperties(str);
/*     */         } else {
/*  95 */           localObject1 = Paths.get(System.getProperty("java.home"), new String[0]);
/*  96 */           if (((Path)localObject1).endsWith("jre")) {
/*  97 */             localObject1 = ((Path)localObject1).getParent();
/*     */           }
/*  99 */           Path localPath = ((Path)localObject1).resolve("lib").resolve("ct.sym");
/* 100 */           if (Files.exists(localPath, new LinkOption[0]))
/*     */           {
/* 102 */             JarFile localJarFile = new JarFile(localPath.toFile()); localObject2 = null;
/*     */             try { ClassFileReader localClassFileReader = ClassFileReader.newInstance(localPath, localJarFile);
/* 104 */               for (localObject3 = localClassFileReader.getClassFiles().iterator(); ((Iterator)localObject3).hasNext(); ) { ClassFile localClassFile = (ClassFile)((Iterator)localObject3).next();
/* 105 */                 findProfile(localClassFile);
/*     */               }
/*     */             }
/*     */             catch (Throwable localThrowable2)
/*     */             {
/* 102 */               localObject2 = localThrowable2; throw localThrowable2;
/*     */             }
/*     */             finally
/*     */             {
/* 107 */               if (localJarFile != null) if (localObject2 != null) try { localJarFile.close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else localJarFile.close();
/*     */             }
/*     */ 
/* 110 */             Collections.addAll(Profile.COMPACT1.packages, JAVAX_CRYPTO_PKGS);
/*     */           }
/*     */         }
/*     */       } catch (IOException|ConstantPoolException localIOException) {
/* 114 */         throw new Error(localIOException);
/*     */       }
/* 116 */       HashMap localHashMap = new HashMap();
/*     */       Iterator localIterator;
/* 117 */       for (localObject2 : Profile.values()) {
/* 118 */         for (localIterator = localObject2.packages.iterator(); localIterator.hasNext(); ) { localObject3 = (String)localIterator.next();
/* 119 */           if (!localHashMap.containsKey(localObject3))
/*     */           {
/* 121 */             localHashMap.put(localObject3, localObject2);
/*     */           }
/*     */         }
/* 124 */         for (localIterator = localObject2.proprietaryPkgs.iterator(); localIterator.hasNext(); ) { localObject3 = (String)localIterator.next();
/* 125 */           if (!localHashMap.containsKey(localObject3)) {
/* 126 */             localHashMap.put(localObject3, localObject2);
/*     */           }
/*     */         }
/*     */       }
/* 130 */       return localHashMap;
/*     */     }
/*     */ 
/*     */     private static Profile findProfile(ClassFile paramClassFile)
/*     */       throws ConstantPoolException
/*     */     {
/* 136 */       RuntimeAnnotations_attribute localRuntimeAnnotations_attribute = (RuntimeAnnotations_attribute)paramClassFile.attributes
/* 136 */         .get("RuntimeInvisibleAnnotations");
/*     */ 
/* 137 */       int i = 0;
/* 138 */       int j = 0;
/* 139 */       if (localRuntimeAnnotations_attribute != null) {
/* 140 */         for (int k = 0; k < localRuntimeAnnotations_attribute.annotations.length; k++) {
/* 141 */           localObject = localRuntimeAnnotations_attribute.annotations[k];
/* 142 */           String str = paramClassFile.constant_pool.getUTF8Value(((Annotation)localObject).type_index);
/* 143 */           if ("Ljdk/Profile+Annotation;".equals(str)) {
/* 144 */             int n = 0; if (n < ((Annotation)localObject).num_element_value_pairs) {
/* 145 */               Annotation.element_value_pair localelement_value_pair = localObject.element_value_pairs[n];
/* 146 */               Annotation.Primitive_element_value localPrimitive_element_value = (Annotation.Primitive_element_value)localelement_value_pair.value;
/*     */ 
/* 148 */               ConstantPool.CONSTANT_Integer_info localCONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)paramClassFile.constant_pool
/* 148 */                 .get(localPrimitive_element_value.const_value_index);
/*     */ 
/* 149 */               i = localCONSTANT_Integer_info.value;
/*     */             }
/*     */           }
/* 152 */           else if ("Lsun/Proprietary+Annotation;".equals(str)) {
/* 153 */             j = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 158 */       Profile localProfile = null;
/* 159 */       switch (i) {
/*     */       case 1:
/* 161 */         localProfile = Profile.COMPACT1; break;
/*     */       case 2:
/* 163 */         localProfile = Profile.COMPACT2; break;
/*     */       case 3:
/* 165 */         localProfile = Profile.COMPACT3; break;
/*     */       case 4:
/* 167 */         localProfile = Profile.FULL_JRE; break;
/*     */       default:
/* 171 */         return null;
/*     */       }
/*     */ 
/* 174 */       Object localObject = paramClassFile.getName();
/* 175 */       int m = ((String)localObject).lastIndexOf('/');
/* 176 */       localObject = m > 0 ? ((String)localObject).substring(0, m).replace('/', '.') : "";
/* 177 */       if (j != 0)
/* 178 */         localProfile.proprietaryPkgs.add(localObject);
/*     */       else {
/* 180 */         localProfile.packages.add(localObject);
/*     */       }
/* 182 */       return localProfile;
/*     */     }
/*     */ 
/*     */     private static void initProfilesFromProperties(String paramString) throws IOException {
/* 186 */       Properties localProperties = new Properties();
/* 187 */       Object localObject1 = new FileReader(paramString); Object localObject2 = null;
/*     */       try { localProperties.load((Reader)localObject1); }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 187 */         localObject2 = localThrowable2; throw localThrowable2;
/*     */       } finally {
/* 189 */         if (localObject1 != null) if (localObject2 != null) try { ((FileReader)localObject1).close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else ((FileReader)localObject1).close(); 
/*     */       }
/* 190 */       for (Object localObject4 : Profile.values()) {
/* 191 */         int k = localObject4.profile;
/* 192 */         String str1 = localProperties.getProperty("profile." + k + ".name");
/* 193 */         if (str1 == null) {
/* 194 */           throw new RuntimeException(str1 + " missing in " + paramString);
/*     */         }
/* 196 */         String str2 = localProperties.getProperty("profile." + k + ".packages");
/* 197 */         String[] arrayOfString1 = str2.split("\\s+");
/* 198 */         for (String str3 : arrayOfString1)
/* 199 */           if (!str3.isEmpty())
/* 200 */             localObject4.packages.add(str3);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.Profile
 * JD-Core Version:    0.6.2
 */