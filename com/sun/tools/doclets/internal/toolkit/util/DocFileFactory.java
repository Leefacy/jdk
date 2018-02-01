/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ import javax.tools.JavaFileManager;
/*    */ import javax.tools.JavaFileManager.Location;
/*    */ import javax.tools.StandardJavaFileManager;
/*    */ 
/*    */ abstract class DocFileFactory
/*    */ {
/* 49 */   private static final Map<Configuration, DocFileFactory> factories = new WeakHashMap();
/*    */   protected Configuration configuration;
/*    */ 
/*    */   static synchronized DocFileFactory getFactory(Configuration paramConfiguration)
/*    */   {
/* 57 */     Object localObject = (DocFileFactory)factories.get(paramConfiguration);
/* 58 */     if (localObject == null) {
/* 59 */       JavaFileManager localJavaFileManager = paramConfiguration.getFileManager();
/* 60 */       if ((localJavaFileManager instanceof StandardJavaFileManager))
/* 61 */         localObject = new StandardDocFileFactory(paramConfiguration);
/*    */       else {
/*    */         try
/*    */         {
/* 65 */           Class localClass = Class.forName("com.sun.tools.javac.nio.PathFileManager");
/*    */ 
/* 66 */           if (localClass.isAssignableFrom(localJavaFileManager.getClass()))
/* 67 */             localObject = new PathDocFileFactory(paramConfiguration);
/*    */         } catch (Throwable localThrowable) {
/* 69 */           throw new IllegalStateException(localThrowable);
/*    */         }
/*    */       }
/* 72 */       factories.put(paramConfiguration, localObject);
/*    */     }
/* 74 */     return localObject;
/*    */   }
/*    */ 
/*    */   protected DocFileFactory(Configuration paramConfiguration)
/*    */   {
/* 80 */     this.configuration = paramConfiguration;
/*    */   }
/*    */ 
/*    */   abstract DocFile createFileForDirectory(String paramString);
/*    */ 
/*    */   abstract DocFile createFileForInput(String paramString);
/*    */ 
/*    */   abstract DocFile createFileForOutput(DocPath paramDocPath);
/*    */ 
/*    */   abstract Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath);
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DocFileFactory
 * JD-Core Version:    0.6.2
 */