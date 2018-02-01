/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class ClassPath
/*     */ {
/*  44 */   static final char dirSeparator = File.pathSeparatorChar;
/*     */   String pathstr;
/*     */   private ClassPathEntry[] path;
/* 188 */   private final String fileSeparatorChar = "" + File.separatorChar;
/*     */ 
/*     */   public ClassPath(String paramString)
/*     */   {
/*  60 */     init(paramString);
/*     */   }
/*     */ 
/*     */   public ClassPath(String[] paramArrayOfString)
/*     */   {
/*  75 */     init(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public ClassPath()
/*     */   {
/*  84 */     String str1 = System.getProperty("sun.boot.class.path");
/*  85 */     String str2 = System.getProperty("env.class.path");
/*  86 */     if (str2 == null) str2 = ".";
/*  87 */     String str3 = str1 + File.pathSeparator + str2;
/*  88 */     init(str3);
/*     */   }
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/*  94 */     this.pathstr = paramString;
/*     */ 
/*  96 */     if (paramString.length() == 0)
/*  97 */       this.path = new ClassPathEntry[0];
/* 103 */     int k;
/* 101 */     for (int i = k = 0; 
/* 102 */       (i = paramString.indexOf(dirSeparator, i)) != -1; 
/* 103 */       i++) k++;
/*     */ 
/* 106 */     ClassPathEntry[] arrayOfClassPathEntry = new ClassPathEntry[k + 1];
/* 107 */     int m = paramString.length();
/*     */     int j;
/* 108 */     for (i = k = 0; i < m; i = j + 1) {
/* 109 */       if ((j = paramString.indexOf(dirSeparator, i)) == -1) {
/* 110 */         j = m;
/*     */       }
/* 112 */       if (i == j) {
/* 113 */         arrayOfClassPathEntry[k] = new ClassPathEntry();
/* 114 */         arrayOfClassPathEntry[(k++)].dir = new File(".");
/*     */       } else {
/* 116 */         File localFile = new File(paramString.substring(i, j));
/* 117 */         if (localFile.isFile()) {
/*     */           try {
/* 119 */             ZipFile localZipFile = new ZipFile(localFile);
/* 120 */             arrayOfClassPathEntry[k] = new ClassPathEntry();
/* 121 */             arrayOfClassPathEntry[(k++)].zip = localZipFile;
/*     */           } catch (ZipException localZipException) {
/*     */           } catch (IOException localIOException) {
/*     */           }
/*     */         }
/*     */         else {
/* 127 */           arrayOfClassPathEntry[k] = new ClassPathEntry();
/* 128 */           arrayOfClassPathEntry[(k++)].dir = localFile;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 133 */     this.path = new ClassPathEntry[k];
/* 134 */     System.arraycopy(arrayOfClassPathEntry, 0, this.path, 0, k);
/*     */   }
/*     */ 
/*     */   private void init(String[] paramArrayOfString)
/*     */   {
/* 139 */     if (paramArrayOfString.length == 0) {
/* 140 */       this.pathstr = "";
/*     */     } else {
/* 142 */       localObject = new StringBuilder(paramArrayOfString[0]);
/* 143 */       for (i = 1; i < paramArrayOfString.length; i++) {
/* 144 */         ((StringBuilder)localObject).append(File.pathSeparatorChar);
/* 145 */         ((StringBuilder)localObject).append(paramArrayOfString[i]);
/*     */       }
/* 147 */       this.pathstr = ((StringBuilder)localObject).toString();
/*     */     }
/*     */ 
/* 151 */     Object localObject = new ClassPathEntry[paramArrayOfString.length];
/* 152 */     int i = 0;
/* 153 */     for (String str : paramArrayOfString) {
/* 154 */       File localFile = new File(str);
/* 155 */       if (localFile.isFile()) {
/*     */         try {
/* 157 */           ZipFile localZipFile = new ZipFile(localFile);
/* 158 */           localObject[i] = new ClassPathEntry();
/* 159 */           localObject[(i++)].zip = localZipFile;
/*     */         } catch (ZipException localZipException) {
/*     */         } catch (IOException localIOException) {
/*     */         }
/*     */       }
/*     */       else {
/* 165 */         localObject[i] = new ClassPathEntry();
/* 166 */         localObject[(i++)].dir = localFile;
/*     */       }
/*     */     }
/*     */ 
/* 170 */     this.path = new ClassPathEntry[i];
/* 171 */     System.arraycopy(localObject, 0, this.path, 0, i);
/*     */   }
/*     */ 
/*     */   public ClassFile getDirectory(String paramString)
/*     */   {
/* 178 */     return getFile(paramString, true);
/*     */   }
/*     */ 
/*     */   public ClassFile getFile(String paramString)
/*     */   {
/* 185 */     return getFile(paramString, false);
/*     */   }
/*     */ 
/*     */   private ClassFile getFile(String paramString, boolean paramBoolean)
/*     */   {
/* 191 */     String str1 = paramString;
/* 192 */     String str2 = "";
/* 193 */     if (!paramBoolean) {
/* 194 */       i = paramString.lastIndexOf(File.separatorChar);
/* 195 */       str1 = paramString.substring(0, i + 1);
/* 196 */       str2 = paramString.substring(i + 1);
/* 197 */     } else if ((!str1.equals("")) && 
/* 198 */       (!str1
/* 198 */       .endsWith(this.fileSeparatorChar)))
/*     */     {
/* 201 */       str1 = str1 + File.separatorChar;
/* 202 */       paramString = str1;
/*     */     }
/* 204 */     for (int i = 0; i < this.path.length; i++)
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 205 */       if (this.path[i].zip != null) {
/* 206 */         localObject1 = paramString.replace(File.separatorChar, '/');
/* 207 */         localObject2 = this.path[i].zip.getEntry((String)localObject1);
/* 208 */         if (localObject2 != null)
/* 209 */           return new ClassFile(this.path[i].zip, (ZipEntry)localObject2);
/*     */       }
/*     */       else {
/* 212 */         localObject1 = new File(this.path[i].dir.getPath(), paramString);
/* 213 */         localObject2 = this.path[i].getFiles(str1);
/* 214 */         if (paramBoolean) {
/* 215 */           if (localObject2.length > 0)
/* 216 */             return new ClassFile((File)localObject1);
/*     */         }
/*     */         else {
/* 219 */           for (int j = 0; j < localObject2.length; j++) {
/* 220 */             if (str2.equals(localObject2[j]))
/*     */             {
/* 224 */               return new ClassFile((File)localObject1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration getFiles(String paramString1, String paramString2)
/*     */   {
/* 237 */     Hashtable localHashtable = new Hashtable();
/* 238 */     int i = this.path.length;
/*     */     while (true) { i--; if (i < 0)
/*     */         break;
/*     */       Object localObject;
/*     */       String str;
/* 239 */       if (this.path[i].zip != null) {
/* 240 */         localObject = this.path[i].zip.entries();
/* 241 */         while (((Enumeration)localObject).hasMoreElements()) {
/* 242 */           ZipEntry localZipEntry = (ZipEntry)((Enumeration)localObject).nextElement();
/* 243 */           str = localZipEntry.getName();
/* 244 */           str = str.replace('/', File.separatorChar);
/* 245 */           if ((str.startsWith(paramString1)) && (str.endsWith(paramString2)))
/* 246 */             localHashtable.put(str, new ClassFile(this.path[i].zip, localZipEntry));
/*     */         }
/*     */       }
/*     */       else {
/* 250 */         localObject = this.path[i].getFiles(paramString1);
/* 251 */         for (int j = 0; j < localObject.length; j++) {
/* 252 */           str = localObject[j];
/* 253 */           if (str.endsWith(paramString2)) {
/* 254 */             str = paramString1 + File.separatorChar + str;
/* 255 */             File localFile = new File(this.path[i].dir.getPath(), str);
/* 256 */             localHashtable.put(str, new ClassFile(localFile));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 261 */     return localHashtable.elements();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 268 */     int i = this.path.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 269 */       if (this.path[i].zip != null)
/* 270 */         this.path[i].zip.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 279 */     return this.pathstr;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassPath
 * JD-Core Version:    0.6.2
 */