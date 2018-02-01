/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.tools.FileObject;
/*     */ 
/*     */ public class PackageDocImpl extends DocImpl
/*     */   implements PackageDoc
/*     */ {
/*     */   protected Symbol.PackageSymbol sym;
/*  66 */   private JCTree.JCCompilationUnit tree = null;
/*     */ 
/*  68 */   public FileObject docPath = null;
/*     */   private boolean foundDoc;
/*  72 */   boolean isIncluded = false;
/*  73 */   public boolean setDocPath = false;
/*     */ 
/* 129 */   private List<ClassDocImpl> allClassesFiltered = null;
/*     */ 
/* 135 */   private List<ClassDocImpl> allClasses = null;
/*     */   private String qualifiedName;
/* 362 */   private boolean checkDocWarningEmitted = false;
/*     */ 
/*     */   public PackageDocImpl(DocEnv paramDocEnv, Symbol.PackageSymbol paramPackageSymbol)
/*     */   {
/*  79 */     this(paramDocEnv, paramPackageSymbol, null);
/*     */   }
/*     */ 
/*     */   public PackageDocImpl(DocEnv paramDocEnv, Symbol.PackageSymbol paramPackageSymbol, TreePath paramTreePath)
/*     */   {
/*  86 */     super(paramDocEnv, paramTreePath);
/*  87 */     this.sym = paramPackageSymbol;
/*  88 */     this.tree = (paramTreePath == null ? null : (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit());
/*  89 */     this.foundDoc = (this.documentation != null);
/*     */   }
/*     */ 
/*     */   void setTree(JCTree paramJCTree) {
/*  93 */     this.tree = ((JCTree.JCCompilationUnit)paramJCTree);
/*     */   }
/*     */ 
/*     */   public void setTreePath(TreePath paramTreePath) {
/*  97 */     super.setTreePath(paramTreePath);
/*  98 */     checkDoc();
/*     */   }
/*     */ 
/*     */   protected String documentation()
/*     */   {
/* 105 */     if (this.documentation != null)
/* 106 */       return this.documentation;
/* 107 */     if (this.docPath != null) {
/*     */       try
/*     */       {
/* 110 */         InputStream localInputStream = this.docPath.openInputStream();
/* 111 */         this.documentation = readHTMLDocumentation(localInputStream, this.docPath);
/*     */       } catch (IOException localIOException) {
/* 113 */         this.documentation = "";
/* 114 */         this.env.error(null, "javadoc.File_Read_Error", this.docPath.getName());
/*     */       }
/*     */     }
/*     */     else {
/* 118 */       this.documentation = "";
/*     */     }
/* 120 */     return this.documentation;
/*     */   }
/*     */ 
/*     */   private List<ClassDocImpl> getClasses(boolean paramBoolean)
/*     */   {
/* 142 */     if ((this.allClasses != null) && (!paramBoolean)) {
/* 143 */       return this.allClasses;
/*     */     }
/* 145 */     if ((this.allClassesFiltered != null) && (paramBoolean)) {
/* 146 */       return this.allClassesFiltered;
/*     */     }
/* 148 */     ListBuffer localListBuffer = new ListBuffer();
/* 149 */     for (Scope.Entry localEntry = this.sym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/* 150 */       if (localEntry.sym != null) {
/* 151 */         Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localEntry.sym;
/* 152 */         ClassDocImpl localClassDocImpl = this.env.getClassDoc(localClassSymbol);
/* 153 */         if ((localClassDocImpl != null) && (!localClassDocImpl.isSynthetic()))
/* 154 */           localClassDocImpl.addAllClasses(localListBuffer, paramBoolean);
/*     */       }
/*     */     }
/* 157 */     if (paramBoolean) {
/* 158 */       return this.allClassesFiltered = localListBuffer.toList();
/*     */     }
/* 160 */     return this.allClasses = localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public void addAllClassesTo(ListBuffer<ClassDocImpl> paramListBuffer)
/*     */   {
/* 168 */     paramListBuffer.appendList(getClasses(true));
/*     */   }
/*     */ 
/*     */   public ClassDoc[] allClasses(boolean paramBoolean)
/*     */   {
/* 180 */     List localList = getClasses(paramBoolean);
/* 181 */     return (ClassDoc[])localList.toArray(new ClassDocImpl[localList.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] allClasses()
/*     */   {
/* 191 */     return allClasses(true);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] ordinaryClasses()
/*     */   {
/* 201 */     ListBuffer localListBuffer = new ListBuffer();
/* 202 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 203 */       if (localClassDocImpl.isOrdinaryClass()) {
/* 204 */         localListBuffer.append(localClassDocImpl);
/*     */       }
/*     */     }
/* 207 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] exceptions()
/*     */   {
/* 216 */     ListBuffer localListBuffer = new ListBuffer();
/* 217 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 218 */       if (localClassDocImpl.isException()) {
/* 219 */         localListBuffer.append(localClassDocImpl);
/*     */       }
/*     */     }
/* 222 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] errors()
/*     */   {
/* 231 */     ListBuffer localListBuffer = new ListBuffer();
/* 232 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 233 */       if (localClassDocImpl.isError()) {
/* 234 */         localListBuffer.append(localClassDocImpl);
/*     */       }
/*     */     }
/* 237 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] enums()
/*     */   {
/* 246 */     ListBuffer localListBuffer = new ListBuffer();
/* 247 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 248 */       if (localClassDocImpl.isEnum()) {
/* 249 */         localListBuffer.append(localClassDocImpl);
/*     */       }
/*     */     }
/* 252 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public ClassDoc[] interfaces()
/*     */   {
/* 261 */     ListBuffer localListBuffer = new ListBuffer();
/* 262 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 263 */       if (localClassDocImpl.isInterface()) {
/* 264 */         localListBuffer.append(localClassDocImpl);
/*     */       }
/*     */     }
/* 267 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public AnnotationTypeDoc[] annotationTypes()
/*     */   {
/* 276 */     ListBuffer localListBuffer = new ListBuffer();
/*     */ 
/* 278 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 279 */       if (localClassDocImpl.isAnnotationType()) {
/* 280 */         localListBuffer.append((AnnotationTypeDocImpl)localClassDocImpl);
/*     */       }
/*     */     }
/* 283 */     return (AnnotationTypeDoc[])localListBuffer.toArray(new AnnotationTypeDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public AnnotationDesc[] annotations()
/*     */   {
/* 291 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[this.sym.getRawAttributes().length()];
/* 292 */     int i = 0;
/* 293 */     for (Attribute.Compound localCompound : this.sym.getRawAttributes()) {
/* 294 */       arrayOfAnnotationDesc[(i++)] = new AnnotationDescImpl(this.env, localCompound);
/*     */     }
/* 296 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ 
/*     */   public ClassDoc findClass(String paramString)
/*     */   {
/* 307 */     for (ClassDocImpl localClassDocImpl : getClasses(true)) {
/* 308 */       if (localClassDocImpl.name().equals(paramString)) {
/* 309 */         return localClassDocImpl;
/*     */       }
/*     */     }
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isIncluded()
/*     */   {
/* 319 */     return this.isIncluded;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 330 */     return qualifiedName();
/*     */   }
/*     */ 
/*     */   public String qualifiedName()
/*     */   {
/* 337 */     if (this.qualifiedName == null) {
/* 338 */       Name localName = this.sym.getQualifiedName();
/*     */ 
/* 341 */       this.qualifiedName = (localName.isEmpty() ? "" : localName.toString());
/*     */     }
/* 343 */     return this.qualifiedName;
/*     */   }
/*     */ 
/*     */   public void setDocPath(FileObject paramFileObject)
/*     */   {
/* 352 */     this.setDocPath = true;
/* 353 */     if (paramFileObject == null)
/* 354 */       return;
/* 355 */     if (!paramFileObject.equals(this.docPath)) {
/* 356 */       this.docPath = paramFileObject;
/* 357 */       checkDoc();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkDoc()
/*     */   {
/* 369 */     if (this.foundDoc) {
/* 370 */       if (!this.checkDocWarningEmitted) {
/* 371 */         this.env.warning(null, "javadoc.Multiple_package_comments", name());
/* 372 */         this.checkDocWarningEmitted = true;
/*     */       }
/*     */     }
/* 375 */     else this.foundDoc = true;
/*     */   }
/*     */ 
/*     */   public SourcePosition position()
/*     */   {
/* 386 */     return this.tree != null ? 
/* 385 */       SourcePositionImpl.make(this.tree.sourcefile, this.tree.pos, this.tree.lineMap) : 
/* 386 */       SourcePositionImpl.make(this.docPath, -1, null);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.PackageDocImpl
 * JD-Core Version:    0.6.2
 */