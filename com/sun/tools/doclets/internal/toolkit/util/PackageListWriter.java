/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.PackageDoc;
/*    */ import com.sun.javadoc.RootDoc;
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class PackageListWriter extends PrintWriter
/*    */ {
/*    */   private Configuration configuration;
/*    */ 
/*    */   public PackageListWriter(Configuration paramConfiguration)
/*    */     throws IOException
/*    */   {
/* 55 */     super(DocFile.createFileForOutput(paramConfiguration, DocPaths.PACKAGE_LIST).openWriter());
/* 56 */     this.configuration = paramConfiguration;
/*    */   }
/*    */ 
/*    */   public static void generate(Configuration paramConfiguration)
/*    */   {
/*    */     try
/*    */     {
/* 68 */       PackageListWriter localPackageListWriter = new PackageListWriter(paramConfiguration);
/* 69 */       localPackageListWriter.generatePackageListFile(paramConfiguration.root);
/* 70 */       localPackageListWriter.close();
/*    */     } catch (IOException localIOException) {
/* 72 */       paramConfiguration.message.error("doclet.exception_encountered", new Object[] { localIOException
/* 73 */         .toString(), DocPaths.PACKAGE_LIST });
/* 74 */       throw new DocletAbortException(localIOException);
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void generatePackageListFile(RootDoc paramRootDoc) {
/* 79 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 80 */     ArrayList localArrayList = new ArrayList();
/* 81 */     for (int i = 0; i < arrayOfPackageDoc.length; i++)
/*    */     {
/* 84 */       if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[i])))
/* 85 */         localArrayList.add(arrayOfPackageDoc[i].name());
/*    */     }
/* 87 */     Collections.sort(localArrayList);
/* 88 */     for (i = 0; i < localArrayList.size(); i++)
/* 89 */       println((String)localArrayList.get(i));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.PackageListWriter
 * JD-Core Version:    0.6.2
 */