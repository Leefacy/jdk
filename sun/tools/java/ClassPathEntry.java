/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Hashtable;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ class ClassPathEntry
/*     */ {
/*     */   File dir;
/*     */   ZipFile zip;
/* 290 */   Hashtable subdirs = new Hashtable(29);
/*     */ 
/* 292 */   String[] getFiles(String paramString) { Object localObject = (String[])this.subdirs.get(paramString);
/* 293 */     if (localObject == null)
/*     */     {
/* 295 */       File localFile = new File(this.dir.getPath(), paramString);
/* 296 */       if (localFile.isDirectory()) {
/* 297 */         localObject = localFile.list();
/* 298 */         if (localObject == null)
/*     */         {
/* 300 */           localObject = new String[0];
/*     */         }
/* 302 */         if (localObject.length == 0) {
/* 303 */           String[] arrayOfString = { "" };
/* 304 */           localObject = arrayOfString;
/*     */         }
/*     */       } else {
/* 307 */         localObject = new String[0];
/*     */       }
/* 309 */       this.subdirs.put(paramString, localObject);
/*     */     }
/* 311 */     return localObject;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassPathEntry
 * JD-Core Version:    0.6.2
 */