/*     */ package com.sun.tools.doclets.internal.toolkit;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.HtmlDoclet;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.PackageListWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ 
/*     */ public abstract class AbstractDoclet
/*     */ {
/*     */   public Configuration configuration;
/*  53 */   private static final String TOOLKIT_DOCLET_NAME = HtmlDoclet.class
/*  53 */     .getName();
/*     */ 
/*     */   private boolean isValidDoclet(AbstractDoclet paramAbstractDoclet)
/*     */   {
/*  60 */     if (!paramAbstractDoclet.getClass().getName().equals(TOOLKIT_DOCLET_NAME)) {
/*  61 */       this.configuration.message.error("doclet.Toolkit_Usage_Violation", new Object[] { TOOLKIT_DOCLET_NAME });
/*     */ 
/*  63 */       return false;
/*     */     }
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean start(AbstractDoclet paramAbstractDoclet, RootDoc paramRootDoc)
/*     */   {
/*  76 */     this.configuration = configuration();
/*  77 */     this.configuration.root = paramRootDoc;
/*  78 */     if (!isValidDoclet(paramAbstractDoclet))
/*  79 */       return false;
/*     */     try
/*     */     {
/*  82 */       paramAbstractDoclet.startGeneration(paramRootDoc);
/*     */     } catch (Configuration.Fault localFault) {
/*  84 */       paramRootDoc.printError(localFault.getMessage());
/*  85 */       return false;
/*     */     } catch (DocletAbortException localDocletAbortException) {
/*  87 */       Throwable localThrowable = localDocletAbortException.getCause();
/*  88 */       if (localThrowable != null) {
/*  89 */         if (localThrowable.getLocalizedMessage() != null)
/*  90 */           paramRootDoc.printError(localThrowable.getLocalizedMessage());
/*     */         else {
/*  92 */           paramRootDoc.printError(localThrowable.toString());
/*     */         }
/*     */       }
/*  95 */       return false;
/*     */     } catch (Exception localException) {
/*  97 */       localException.printStackTrace();
/*  98 */       return false;
/*     */     }
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   public static LanguageVersion languageVersion()
/*     */   {
/* 108 */     return LanguageVersion.JAVA_1_5;
/*     */   }
/*     */ 
/*     */   public abstract Configuration configuration();
/*     */ 
/*     */   private void startGeneration(RootDoc paramRootDoc)
/*     */     throws Configuration.Fault, Exception
/*     */   {
/* 127 */     if (paramRootDoc.classes().length == 0) {
/* 128 */       this.configuration.message
/* 129 */         .error("doclet.No_Public_Classes_To_Document", new Object[0]);
/*     */ 
/* 130 */       return;
/*     */     }
/* 132 */     this.configuration.setOptions();
/* 133 */     this.configuration.getDocletSpecificMsg().notice("doclet.build_version", new Object[] { this.configuration
/* 134 */       .getDocletSpecificBuildDate() });
/* 135 */     ClassTree localClassTree = new ClassTree(this.configuration, this.configuration.nodeprecated);
/*     */ 
/* 137 */     generateClassFiles(paramRootDoc, localClassTree);
/* 138 */     Util.copyDocFiles(this.configuration, DocPaths.DOC_FILES);
/*     */ 
/* 140 */     PackageListWriter.generate(this.configuration);
/* 141 */     generatePackageFiles(localClassTree);
/* 142 */     generateProfileFiles();
/*     */ 
/* 144 */     generateOtherFiles(paramRootDoc, localClassTree);
/* 145 */     this.configuration.tagletManager.printReport();
/*     */   }
/*     */ 
/*     */   protected void generateOtherFiles(RootDoc paramRootDoc, ClassTree paramClassTree)
/*     */     throws Exception
/*     */   {
/* 155 */     BuilderFactory localBuilderFactory = this.configuration.getBuilderFactory();
/* 156 */     AbstractBuilder localAbstractBuilder1 = localBuilderFactory.getConstantsSummaryBuider();
/* 157 */     localAbstractBuilder1.build();
/* 158 */     AbstractBuilder localAbstractBuilder2 = localBuilderFactory.getSerializedFormBuilder();
/* 159 */     localAbstractBuilder2.build();
/*     */   }
/*     */ 
/*     */   protected abstract void generateProfileFiles()
/*     */     throws Exception;
/*     */ 
/*     */   protected abstract void generatePackageFiles(ClassTree paramClassTree)
/*     */     throws Exception;
/*     */ 
/*     */   protected abstract void generateClassFiles(ClassDoc[] paramArrayOfClassDoc, ClassTree paramClassTree);
/*     */ 
/*     */   protected void generateClassFiles(RootDoc paramRootDoc, ClassTree paramClassTree)
/*     */   {
/* 189 */     generateClassFiles(paramClassTree);
/* 190 */     PackageDoc[] arrayOfPackageDoc = paramRootDoc.specifiedPackages();
/* 191 */     for (int i = 0; i < arrayOfPackageDoc.length; i++)
/* 192 */       generateClassFiles(arrayOfPackageDoc[i].allClasses(), paramClassTree);
/*     */   }
/*     */ 
/*     */   private void generateClassFiles(ClassTree paramClassTree)
/*     */   {
/* 202 */     String[] arrayOfString = this.configuration.classDocCatalog.packageNames();
/* 203 */     for (int i = 0; i < arrayOfString.length; 
/* 204 */       i++)
/* 205 */       generateClassFiles(this.configuration.classDocCatalog.allClasses(arrayOfString[i]), paramClassTree);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.AbstractDoclet
 * JD-Core Version:    0.6.2
 */