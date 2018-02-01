/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class DirectoryLoader extends ClassLoader
/*     */ {
/*     */   private Hashtable cache;
/*     */   private File root;
/*     */ 
/*     */   public DirectoryLoader(File paramFile)
/*     */   {
/*  54 */     this.cache = new Hashtable();
/*  55 */     if ((paramFile == null) || (!paramFile.isDirectory())) {
/*  56 */       throw new IllegalArgumentException();
/*     */     }
/*  58 */     this.root = paramFile;
/*     */   }
/*     */ 
/*     */   private DirectoryLoader()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Class loadClass(String paramString) throws ClassNotFoundException
/*     */   {
/*  67 */     return loadClass(paramString, true);
/*     */   }
/*     */ 
/*     */   public synchronized Class loadClass(String paramString, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/*  82 */     Class localClass = (Class)this.cache.get(paramString);
/*     */ 
/*  84 */     if (localClass == null)
/*     */     {
/*     */       try
/*     */       {
/*  90 */         localClass = super.findSystemClass(paramString);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException)
/*     */       {
/*  96 */         byte[] arrayOfByte = getClassFileData(paramString);
/*     */ 
/*  98 */         if (arrayOfByte == null) {
/*  99 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/* 104 */         localClass = defineClass(arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/* 106 */         if (localClass == null) {
/* 107 */           throw new ClassFormatError();
/*     */         }
/*     */ 
/* 112 */         if (paramBoolean) resolveClass(localClass);
/*     */ 
/* 116 */         this.cache.put(paramString, localClass);
/*     */       }
/*     */     }
/*     */ 
/* 120 */     return localClass;
/*     */   }
/*     */ 
/*     */   private byte[] getClassFileData(String paramString)
/*     */   {
/* 129 */     byte[] arrayOfByte = null;
/* 130 */     FileInputStream localFileInputStream = null;
/*     */ 
/* 134 */     File localFile = new File(this.root, paramString.replace('.', File.separatorChar) + ".class");
/*     */     try
/*     */     {
/* 139 */       localFileInputStream = new FileInputStream(localFile);
/* 140 */       arrayOfByte = new byte[localFileInputStream.available()];
/* 141 */       localFileInputStream.read(arrayOfByte);
/*     */     } catch (ThreadDeath localThreadDeath2) {
/* 143 */       throw localThreadDeath2;
/*     */     }
/*     */     catch (Throwable localThrowable2) {
/*     */     }
/*     */     finally {
/* 148 */       if (localFileInputStream != null)
/*     */         try {
/* 150 */           localFileInputStream.close();
/*     */         } catch (ThreadDeath localThreadDeath4) {
/* 152 */           throw localThreadDeath4;
/*     */         }
/*     */         catch (Throwable localThrowable4)
/*     */         {
/*     */         }
/*     */     }
/* 158 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.DirectoryLoader
 * JD-Core Version:    0.6.2
 */