/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ 
/*     */ public class RootDocImpl extends DocImpl
/*     */   implements RootDoc
/*     */ {
/*     */   private List<ClassDocImpl> cmdLineClasses;
/*     */   private List<PackageDocImpl> cmdLinePackages;
/*     */   private List<String[]> options;
/*     */ 
/*     */   public RootDocImpl(DocEnv paramDocEnv, List<JCTree.JCClassDecl> paramList, List<String> paramList1, List<String[]> paramList2)
/*     */   {
/*  83 */     super(paramDocEnv, null);
/*  84 */     this.options = paramList2;
/*  85 */     setPackages(paramDocEnv, paramList1);
/*  86 */     setClasses(paramDocEnv, paramList);
/*     */   }
/*     */ 
/*     */   public RootDocImpl(DocEnv paramDocEnv, List<String> paramList, List<String[]> paramList1)
/*     */   {
/*  97 */     super(paramDocEnv, null);
/*  98 */     this.options = paramList1;
/*  99 */     this.cmdLinePackages = List.nil();
/* 100 */     ListBuffer localListBuffer = new ListBuffer();
/* 101 */     for (String str : paramList) {
/* 102 */       ClassDocImpl localClassDocImpl = paramDocEnv.loadClass(str);
/* 103 */       if (localClassDocImpl == null)
/* 104 */         paramDocEnv.error(null, "javadoc.class_not_found", str);
/*     */       else
/* 106 */         localListBuffer = localListBuffer.append(localClassDocImpl);
/*     */     }
/* 108 */     this.cmdLineClasses = localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   private void setClasses(DocEnv paramDocEnv, List<JCTree.JCClassDecl> paramList)
/*     */   {
/* 119 */     ListBuffer localListBuffer = new ListBuffer();
/* 120 */     for (JCTree.JCClassDecl localJCClassDecl : paramList)
/*     */     {
/* 122 */       if (paramDocEnv.shouldDocument(localJCClassDecl.sym)) {
/* 123 */         ClassDocImpl localClassDocImpl = paramDocEnv.getClassDoc(localJCClassDecl.sym);
/* 124 */         if (localClassDocImpl != null) {
/* 125 */           localClassDocImpl.isIncluded = true;
/* 126 */           localListBuffer.append(localClassDocImpl);
/*     */         }
/*     */       }
/*     */     }
/* 130 */     this.cmdLineClasses = localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   private void setPackages(DocEnv paramDocEnv, List<String> paramList)
/*     */   {
/* 140 */     ListBuffer localListBuffer = new ListBuffer();
/* 141 */     for (String str : paramList) {
/* 142 */       PackageDocImpl localPackageDocImpl = paramDocEnv.lookupPackage(str);
/* 143 */       if (localPackageDocImpl != null) {
/* 144 */         localPackageDocImpl.isIncluded = true;
/* 145 */         localListBuffer.append(localPackageDocImpl);
/*     */       } else {
/* 147 */         paramDocEnv.warning(null, "main.no_source_files_for_package", str);
/*     */       }
/*     */     }
/* 150 */     this.cmdLinePackages = localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public String[][] options()
/*     */   {
/* 171 */     return (String[][])this.options.toArray(new String[this.options.length()][]);
/*     */   }
/*     */ 
/*     */   public PackageDoc[] specifiedPackages()
/*     */   {
/* 179 */     return (PackageDoc[])this.cmdLinePackages
/* 179 */       .toArray(new PackageDocImpl[this.cmdLinePackages
/* 179 */       .length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] specifiedClasses()
/*     */   {
/* 186 */     ListBuffer localListBuffer = new ListBuffer();
/* 187 */     for (ClassDocImpl localClassDocImpl : this.cmdLineClasses) {
/* 188 */       localClassDocImpl.addAllClasses(localListBuffer, true);
/*     */     }
/* 190 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] classes()
/*     */   {
/* 198 */     ListBuffer localListBuffer = new ListBuffer();
/* 199 */     for (Iterator localIterator = this.cmdLineClasses.iterator(); localIterator.hasNext(); ) { localObject = (ClassDocImpl)localIterator.next();
/* 200 */       ((ClassDocImpl)localObject).addAllClasses(localListBuffer, true);
/*     */     }
/* 202 */     Object localObject;
/* 202 */     for (localIterator = this.cmdLinePackages.iterator(); localIterator.hasNext(); ) { localObject = (PackageDocImpl)localIterator.next();
/* 203 */       ((PackageDocImpl)localObject).addAllClassesTo(localListBuffer);
/*     */     }
/* 205 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc classNamed(String paramString)
/*     */   {
/* 218 */     return this.env.lookupClass(paramString);
/*     */   }
/*     */ 
/*     */   public PackageDoc packageNamed(String paramString)
/*     */   {
/* 230 */     return this.env.lookupPackage(paramString);
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 239 */     return "*RootDocImpl*";
/*     */   }
/*     */ 
/*     */   public String qualifiedName()
/*     */   {
/* 248 */     return "*RootDocImpl*";
/*     */   }
/*     */ 
/*     */   public boolean isIncluded()
/*     */   {
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */   public void printError(String paramString)
/*     */   {
/* 265 */     this.env.printError(paramString);
/*     */   }
/*     */ 
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 274 */     this.env.printError(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   public void printWarning(String paramString)
/*     */   {
/* 283 */     this.env.printWarning(paramString);
/*     */   }
/*     */ 
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 292 */     this.env.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   public void printNotice(String paramString)
/*     */   {
/* 301 */     this.env.printNotice(paramString);
/*     */   }
/*     */ 
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 310 */     this.env.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   private JavaFileObject getOverviewPath()
/*     */   {
/* 318 */     for (String[] arrayOfString : this.options) {
/* 319 */       if ((arrayOfString[0].equals("-overview")) && 
/* 320 */         ((this.env.fileManager instanceof StandardJavaFileManager))) {
/* 321 */         StandardJavaFileManager localStandardJavaFileManager = (StandardJavaFileManager)this.env.fileManager;
/* 322 */         return (JavaFileObject)localStandardJavaFileManager.getJavaFileObjects(new String[] { arrayOfString[1] }).iterator().next();
/*     */       }
/*     */     }
/*     */ 
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */   protected String documentation()
/*     */   {
/* 334 */     if (this.documentation == null) {
/* 335 */       JavaFileObject localJavaFileObject = getOverviewPath();
/* 336 */       if (localJavaFileObject == null)
/*     */       {
/* 338 */         this.documentation = "";
/*     */       }
/*     */       else {
/*     */         try {
/* 342 */           this.documentation = readHTMLDocumentation(localJavaFileObject
/* 343 */             .openInputStream(), localJavaFileObject);
/*     */         }
/*     */         catch (IOException localIOException) {
/* 346 */           this.documentation = "";
/* 347 */           this.env.error(null, "javadoc.File_Read_Error", localJavaFileObject.getName());
/*     */         }
/*     */       }
/*     */     }
/* 351 */     return this.documentation;
/*     */   }
/*     */ 
/*     */   public SourcePosition position()
/*     */   {
/*     */     JavaFileObject localJavaFileObject;
/* 363 */     return (localJavaFileObject = getOverviewPath()) == null ? null : 
/* 363 */       SourcePositionImpl.make(localJavaFileObject, -1, null);
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 370 */     return this.env.doclocale.locale;
/*     */   }
/*     */ 
/*     */   public JavaFileManager getFileManager()
/*     */   {
/* 377 */     return this.env.fileManager;
/*     */   }
/*     */ 
/*     */   public void initDocLint(Collection<String> paramCollection1, Collection<String> paramCollection2) {
/* 381 */     this.env.initDoclint(paramCollection1, paramCollection2);
/*     */   }
/*     */ 
/*     */   public boolean isFunctionalInterface(AnnotationDesc paramAnnotationDesc)
/*     */   {
/* 386 */     return (paramAnnotationDesc.annotationType().qualifiedName().equals(this.env.syms.functionalInterfaceType
/* 386 */       .toString())) && (this.env.source
/* 386 */       .allowLambda());
/*     */   }
/*     */ 
/*     */   public boolean showTagMessages() {
/* 390 */     return this.env.showTagMessages();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.RootDocImpl
 * JD-Core Version:    0.6.2
 */