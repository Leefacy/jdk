/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassPath;
/*     */ 
/*     */ public class ClassPathLoader extends ClassLoader
/*     */ {
/*     */   private ClassPath classPath;
/*     */ 
/*     */   public ClassPathLoader(ClassPath paramClassPath)
/*     */   {
/*  43 */     this.classPath = paramClassPath;
/*     */   }
/*     */ 
/*     */   protected Class findClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  49 */     byte[] arrayOfByte = loadClassData(paramString);
/*  50 */     return defineClass(paramString, arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   private byte[] loadClassData(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  61 */     String str = paramString.replace('.', File.separatorChar) + ".class";
/*     */ 
/*  67 */     ClassFile localClassFile = this.classPath.getFile(str);
/*     */ 
/*  69 */     if (localClassFile != null)
/*     */     {
/*  73 */       Object localObject1 = null;
/*  74 */       byte[] arrayOfByte = null;
/*     */       try
/*     */       {
/*  81 */         DataInputStream localDataInputStream = new DataInputStream(localClassFile
/*  81 */           .getInputStream());
/*     */ 
/*  88 */         arrayOfByte = new byte[(int)localClassFile.length()];
/*     */         try
/*     */         {
/*  91 */           localDataInputStream.readFully(arrayOfByte);
/*     */         }
/*     */         catch (IOException localIOException3)
/*     */         {
/*  95 */           arrayOfByte = null;
/*  96 */           localObject1 = localIOException3;
/*     */         }
/*     */         finally {
/*     */           try {
/* 100 */             localDataInputStream.close();
/*     */           } catch (IOException localIOException5) {
/*     */           }
/*     */         }
/*     */       } catch (IOException localIOException1) {
/* 105 */         localObject1 = localIOException1;
/*     */       }
/*     */ 
/* 108 */       if (arrayOfByte == null) {
/* 109 */         throw new ClassNotFoundException(paramString, localObject1);
/*     */       }
/* 111 */       return arrayOfByte;
/*     */     }
/*     */ 
/* 115 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.ClassPathLoader
 * JD-Core Version:    0.6.2
 */