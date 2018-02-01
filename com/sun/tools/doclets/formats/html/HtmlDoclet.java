/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.internal.toolkit.AbstractDoclet;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class HtmlDoclet extends AbstractDoclet
/*     */ {
/*  52 */   private static HtmlDoclet docletToStart = null;
/*     */   public final ConfigurationImpl configuration;
/* 280 */   public static final ConfigurationImpl sharedInstanceForOptions = new ConfigurationImpl();
/*     */ 
/*     */   public HtmlDoclet()
/*     */   {
/*  55 */     this.configuration = new ConfigurationImpl();
/*     */   }
/*     */ 
/*     */   public static boolean start(RootDoc paramRootDoc)
/*     */   {
/*     */     HtmlDoclet localHtmlDoclet;
/*  74 */     if (docletToStart != null) {
/*  75 */       localHtmlDoclet = docletToStart;
/*  76 */       docletToStart = null;
/*     */     } else {
/*  78 */       localHtmlDoclet = new HtmlDoclet();
/*     */     }
/*  80 */     return localHtmlDoclet.start(localHtmlDoclet, paramRootDoc);
/*     */   }
/*     */ 
/*     */   public Configuration configuration()
/*     */   {
/*  89 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   protected void generateOtherFiles(RootDoc paramRootDoc, ClassTree paramClassTree)
/*     */     throws Exception
/*     */   {
/* 104 */     super.generateOtherFiles(paramRootDoc, paramClassTree);
/* 105 */     if (this.configuration.linksource) {
/* 106 */       SourceToHTMLConverter.convertRoot(this.configuration, paramRootDoc, DocPaths.SOURCE_OUTPUT);
/*     */     }
/*     */ 
/* 110 */     if (this.configuration.topFile.isEmpty()) {
/* 111 */       this.configuration.standardmessage
/* 112 */         .error("doclet.No_Non_Deprecated_Classes_To_Document", new Object[0]);
/*     */ 
/* 113 */       return;
/*     */     }
/* 115 */     boolean bool = this.configuration.nodeprecated;
/* 116 */     performCopy(this.configuration.helpfile);
/* 117 */     performCopy(this.configuration.stylesheetfile);
/*     */ 
/* 119 */     if (this.configuration.classuse) {
/* 120 */       ClassUseWriter.generate(this.configuration, paramClassTree);
/*     */     }
/* 122 */     IndexBuilder localIndexBuilder = new IndexBuilder(this.configuration, bool);
/*     */ 
/* 124 */     if (this.configuration.createtree) {
/* 125 */       TreeWriter.generate(this.configuration, paramClassTree);
/*     */     }
/* 127 */     if (this.configuration.createindex) {
/* 128 */       if (this.configuration.splitindex)
/* 129 */         SplitIndexWriter.generate(this.configuration, localIndexBuilder);
/*     */       else {
/* 131 */         SingleIndexWriter.generate(this.configuration, localIndexBuilder);
/*     */       }
/*     */     }
/*     */ 
/* 135 */     if ((!this.configuration.nodeprecatedlist) && (!bool)) {
/* 136 */       DeprecatedListWriter.generate(this.configuration);
/*     */     }
/*     */ 
/* 139 */     AllClassesFrameWriter.generate(this.configuration, new IndexBuilder(this.configuration, bool, true));
/*     */ 
/* 142 */     FrameOutputWriter.generate(this.configuration);
/*     */ 
/* 144 */     if (this.configuration.createoverview) {
/* 145 */       PackageIndexWriter.generate(this.configuration);
/*     */     }
/* 147 */     if ((this.configuration.helpfile.length() == 0) && (!this.configuration.nohelp))
/*     */     {
/* 149 */       HelpWriter.generate(this.configuration);
/*     */     }
/*     */ 
/* 154 */     if (this.configuration.stylesheetfile.length() == 0) {
/* 155 */       localDocFile = DocFile.createFileForOutput(this.configuration, DocPaths.STYLESHEET);
/* 156 */       localDocFile.copyResource(DocPaths.RESOURCES.resolve(DocPaths.STYLESHEET), false, true);
/*     */     }
/* 158 */     DocFile localDocFile = DocFile.createFileForOutput(this.configuration, DocPaths.JAVASCRIPT);
/* 159 */     localDocFile.copyResource(DocPaths.RESOURCES.resolve(DocPaths.JAVASCRIPT), true, true);
/*     */   }
/*     */ 
/*     */   protected void generateClassFiles(ClassDoc[] paramArrayOfClassDoc, ClassTree paramClassTree)
/*     */   {
/* 166 */     Arrays.sort(paramArrayOfClassDoc);
/* 167 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++)
/* 168 */       if ((this.configuration.isGeneratedDoc(paramArrayOfClassDoc[i])) && (paramArrayOfClassDoc[i].isIncluded()))
/*     */       {
/* 171 */         ClassDoc localClassDoc1 = i == 0 ? null : paramArrayOfClassDoc[(i - 1)];
/*     */ 
/* 174 */         ClassDoc localClassDoc2 = paramArrayOfClassDoc[i];
/* 175 */         ClassDoc localClassDoc3 = i + 1 == paramArrayOfClassDoc.length ? null : paramArrayOfClassDoc[(i + 1)];
/*     */         try
/*     */         {
/*     */           AbstractBuilder localAbstractBuilder;
/* 179 */           if (localClassDoc2.isAnnotationType())
/*     */           {
/* 182 */             localAbstractBuilder = this.configuration
/* 181 */               .getBuilderFactory()
/* 182 */               .getAnnotationTypeBuilder((AnnotationTypeDoc)localClassDoc2, localClassDoc1, localClassDoc3);
/*     */ 
/* 184 */             localAbstractBuilder.build();
/*     */           }
/*     */           else
/*     */           {
/* 188 */             localAbstractBuilder = this.configuration
/* 187 */               .getBuilderFactory()
/* 188 */               .getClassBuilder(localClassDoc2, localClassDoc1, localClassDoc3, paramClassTree);
/*     */ 
/* 189 */             localAbstractBuilder.build();
/*     */           }
/*     */         } catch (IOException localIOException) {
/* 192 */           throw new DocletAbortException(localIOException);
/*     */         } catch (DocletAbortException localDocletAbortException) {
/* 194 */           throw localDocletAbortException;
/*     */         } catch (Exception localException) {
/* 196 */           localException.printStackTrace();
/* 197 */           throw new DocletAbortException(localException);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void generateProfileFiles()
/*     */     throws Exception
/*     */   {
/* 206 */     if ((this.configuration.showProfiles) && (this.configuration.profilePackages.size() > 0)) {
/* 207 */       ProfileIndexFrameWriter.generate(this.configuration);
/* 208 */       Profile localProfile1 = null;
/*     */ 
/* 210 */       for (int i = 1; i < this.configuration.profiles.getProfileCount(); i++) {
/* 211 */         String str = Profile.lookup(i).name;
/*     */ 
/* 215 */         if (this.configuration.shouldDocumentProfile(str))
/*     */         {
/* 217 */           ProfilePackageIndexFrameWriter.generate(this.configuration, str);
/* 218 */           PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(str);
/*     */ 
/* 220 */           PackageDoc localPackageDoc1 = null;
/* 221 */           for (int j = 0; j < arrayOfPackageDoc.length; j++)
/*     */           {
/* 225 */             if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[j]))) {
/* 226 */               ProfilePackageFrameWriter.generate(this.configuration, arrayOfPackageDoc[j], i);
/*     */ 
/* 228 */               PackageDoc localPackageDoc2 = (j + 1 < arrayOfPackageDoc.length) && 
/* 228 */                 (arrayOfPackageDoc[(j + 1)]
/* 228 */                 .name().length() > 0) ? arrayOfPackageDoc[(j + 1)] : null;
/*     */ 
/* 230 */               AbstractBuilder localAbstractBuilder2 = this.configuration
/* 230 */                 .getBuilderFactory().getProfilePackageSummaryBuilder(arrayOfPackageDoc[j], localPackageDoc1, localPackageDoc2, 
/* 231 */                 Profile.lookup(i));
/*     */ 
/* 232 */               localAbstractBuilder2.build();
/* 233 */               localPackageDoc1 = arrayOfPackageDoc[j];
/*     */             }
/*     */           }
/*     */ 
/* 237 */           Profile localProfile2 = i + 1 < this.configuration.profiles.getProfileCount() ? 
/* 237 */             Profile.lookup(i + 1) : 
/* 237 */             null;
/*     */ 
/* 239 */           AbstractBuilder localAbstractBuilder1 = this.configuration
/* 239 */             .getBuilderFactory().getProfileSummaryBuilder(
/* 240 */             Profile.lookup(i), 
/* 240 */             localProfile1, localProfile2);
/* 241 */           localAbstractBuilder1.build();
/* 242 */           localProfile1 = Profile.lookup(i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generatePackageFiles(ClassTree paramClassTree)
/*     */     throws Exception
/*     */   {
/* 251 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 252 */     if (arrayOfPackageDoc.length > 1) {
/* 253 */       PackageIndexFrameWriter.generate(this.configuration);
/*     */     }
/* 255 */     PackageDoc localPackageDoc = null;
/* 256 */     for (int i = 0; i < arrayOfPackageDoc.length; i++)
/*     */     {
/* 260 */       if ((!this.configuration.nodeprecated) || (!Util.isDeprecated(arrayOfPackageDoc[i]))) {
/* 261 */         PackageFrameWriter.generate(this.configuration, arrayOfPackageDoc[i]);
/*     */ 
/* 263 */         Object localObject = (i + 1 < arrayOfPackageDoc.length) && 
/* 263 */           (arrayOfPackageDoc[(i + 1)]
/* 263 */           .name().length() > 0) ? arrayOfPackageDoc[(i + 1)] : null;
/*     */ 
/* 265 */         localObject = (i + 2 < arrayOfPackageDoc.length) && (localObject == null) ? arrayOfPackageDoc[(i + 2)] : localObject;
/*     */ 
/* 267 */         AbstractBuilder localAbstractBuilder = this.configuration
/* 267 */           .getBuilderFactory().getPackageSummaryBuilder(arrayOfPackageDoc[i], localPackageDoc, localObject);
/*     */ 
/* 269 */         localAbstractBuilder.build();
/* 270 */         if (this.configuration.createtree) {
/* 271 */           PackageTreeWriter.generate(this.configuration, arrayOfPackageDoc[i], localPackageDoc, localObject, this.configuration.nodeprecated);
/*     */         }
/*     */ 
/* 275 */         localPackageDoc = arrayOfPackageDoc[i];
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int optionLength(String paramString)
/*     */   {
/* 291 */     return sharedInstanceForOptions.optionLength(paramString);
/*     */   }
/*     */ 
/*     */   public static boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter)
/*     */   {
/* 307 */     docletToStart = new HtmlDoclet();
/* 308 */     return docletToStart.configuration.validOptions(paramArrayOfString, paramDocErrorReporter);
/*     */   }
/*     */ 
/*     */   private void performCopy(String paramString) {
/* 312 */     if (paramString.isEmpty())
/* 313 */       return;
/*     */     try
/*     */     {
/* 316 */       DocFile localDocFile1 = DocFile.createFileForInput(this.configuration, paramString);
/* 317 */       DocPath localDocPath = DocPath.create(localDocFile1.getName());
/* 318 */       DocFile localDocFile2 = DocFile.createFileForOutput(this.configuration, localDocPath);
/* 319 */       if (localDocFile2.isSameFile(localDocFile1)) {
/* 320 */         return;
/*     */       }
/* 322 */       this.configuration.message.notice((SourcePosition)null, "doclet.Copying_File_0_To_File_1", new Object[] { localDocFile1
/* 324 */         .toString(), localDocPath.getPath() });
/* 325 */       localDocFile2.copyFile(localDocFile1);
/*     */     } catch (IOException localIOException) {
/* 327 */       this.configuration.message.error((SourcePosition)null, "doclet.perform_copy_exception_encountered", new Object[] { localIOException
/* 329 */         .toString() });
/* 330 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.HtmlDoclet
 * JD-Core Version:    0.6.2
 */