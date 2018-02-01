/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.api.BasicJavacTask;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.comp.Check;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ public class DocEnv
/*     */ {
/*  65 */   protected static final Context.Key<DocEnv> docEnvKey = new Context.Key();
/*     */   private Messager messager;
/*     */   DocLocale doclocale;
/*     */   Symtab syms;
/*     */   JavadocClassReader reader;
/*     */   JavadocEnter enter;
/*     */   Names names;
/*     */   private String encoding;
/*     */   final Symbol externalizableSym;
/*     */   protected ModifierFilter showAccess;
/*     */   boolean breakiterator;
/* 105 */   boolean quiet = false;
/*     */   Check chk;
/*     */   Types types;
/*     */   JavaFileManager fileManager;
/*     */   Context context;
/*     */   DocLint doclint;
/* 113 */   WeakHashMap<JCTree, TreePath> treePaths = new WeakHashMap();
/*     */ 
/* 116 */   boolean docClasses = false;
/*     */ 
/* 119 */   protected boolean legacyDoclet = true;
/*     */ 
/* 125 */   private boolean silent = false;
/*     */   protected Source source;
/* 547 */   protected Map<Symbol.PackageSymbol, PackageDocImpl> packageMap = new HashMap();
/*     */ 
/* 574 */   protected Map<Symbol.ClassSymbol, ClassDocImpl> classMap = new HashMap();
/*     */ 
/* 616 */   protected Map<Symbol.VarSymbol, FieldDocImpl> fieldMap = new HashMap();
/*     */ 
/* 641 */   protected Map<Symbol.MethodSymbol, ExecutableMemberDocImpl> methodMap = new HashMap();
/*     */ 
/*     */   public static DocEnv instance(Context paramContext)
/*     */   {
/*  69 */     DocEnv localDocEnv = (DocEnv)paramContext.get(docEnvKey);
/*  70 */     if (localDocEnv == null)
/*  71 */       localDocEnv = new DocEnv(paramContext);
/*  72 */     return localDocEnv;
/*     */   }
/*     */ 
/*     */   protected DocEnv(Context paramContext)
/*     */   {
/* 138 */     paramContext.put(docEnvKey, this);
/* 139 */     this.context = paramContext;
/*     */ 
/* 141 */     this.messager = Messager.instance0(paramContext);
/* 142 */     this.syms = Symtab.instance(paramContext);
/* 143 */     this.reader = JavadocClassReader.instance0(paramContext);
/* 144 */     this.enter = JavadocEnter.instance0(paramContext);
/* 145 */     this.names = Names.instance(paramContext);
/* 146 */     this.externalizableSym = this.reader.enterClass(this.names.fromString("java.io.Externalizable"));
/* 147 */     this.chk = Check.instance(paramContext);
/* 148 */     this.types = Types.instance(paramContext);
/* 149 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/* 150 */     if ((this.fileManager instanceof JavacFileManager)) {
/* 151 */       ((JavacFileManager)this.fileManager).setSymbolFileEnabled(false);
/*     */     }
/*     */ 
/* 155 */     this.doclocale = new DocLocale(this, "", this.breakiterator);
/* 156 */     this.source = Source.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void setSilent(boolean paramBoolean) {
/* 160 */     this.silent = paramBoolean;
/*     */   }
/*     */ 
/*     */   public ClassDocImpl lookupClass(String paramString)
/*     */   {
/* 167 */     Symbol.ClassSymbol localClassSymbol = getClassSymbol(paramString);
/* 168 */     if (localClassSymbol != null) {
/* 169 */       return getClassDoc(localClassSymbol);
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public ClassDocImpl loadClass(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 180 */       Symbol.ClassSymbol localClassSymbol = this.reader.loadClass(this.names.fromString(paramString));
/* 181 */       return getClassDoc(localClassSymbol);
/*     */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 183 */       this.chk.completionError(null, localCompletionFailure);
/* 184 */     }return null;
/*     */   }
/*     */ 
/*     */   public PackageDocImpl lookupPackage(String paramString)
/*     */   {
/* 196 */     Symbol.PackageSymbol localPackageSymbol = (Symbol.PackageSymbol)this.syms.packages.get(this.names.fromString(paramString));
/* 197 */     Symbol.ClassSymbol localClassSymbol = getClassSymbol(paramString);
/* 198 */     if ((localPackageSymbol != null) && (localClassSymbol == null)) {
/* 199 */       return getPackageDoc(localPackageSymbol);
/*     */     }
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   Symbol.ClassSymbol getClassSymbol(String paramString)
/*     */   {
/* 211 */     int i = paramString.length();
/* 212 */     char[] arrayOfChar = paramString.toCharArray();
/* 213 */     int j = paramString.length();
/*     */     while (true) {
/* 215 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.syms.classes.get(this.names.fromChars(arrayOfChar, 0, i));
/* 216 */       if (localClassSymbol != null)
/* 217 */         return localClassSymbol;
/* 218 */       j = paramString.substring(0, j).lastIndexOf('.');
/* 219 */       if (j < 0) break;
/* 220 */       arrayOfChar[j] = '$';
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   public void setLocale(String paramString)
/*     */   {
/* 230 */     this.doclocale = new DocLocale(this, paramString, this.breakiterator);
/*     */ 
/* 232 */     this.messager.setLocale(this.doclocale.locale);
/*     */   }
/*     */ 
/*     */   public boolean shouldDocument(Symbol.VarSymbol paramVarSymbol)
/*     */   {
/* 237 */     long l = paramVarSymbol.flags();
/*     */ 
/* 239 */     if ((l & 0x1000) != 0L) {
/* 240 */       return false;
/*     */     }
/*     */ 
/* 243 */     return this.showAccess.checkModifier(translateModifiers(l));
/*     */   }
/*     */ 
/*     */   public boolean shouldDocument(Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/* 248 */     long l = paramMethodSymbol.flags();
/*     */ 
/* 250 */     if ((l & 0x1000) != 0L) {
/* 251 */       return false;
/*     */     }
/*     */ 
/* 254 */     return this.showAccess.checkModifier(translateModifiers(l));
/*     */   }
/*     */ 
/*     */   public boolean shouldDocument(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 262 */     return ((paramClassSymbol.flags_field & 0x1000) == 0L) && ((this.docClasses) || 
/* 261 */       (getClassDoc(paramClassSymbol).tree != null)) && 
/* 262 */       (isVisible(paramClassSymbol));
/*     */   }
/*     */ 
/*     */   protected boolean isVisible(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 281 */     long l = paramClassSymbol.flags_field;
/* 282 */     if (!this.showAccess.checkModifier(translateModifiers(l))) {
/* 283 */       return false;
/*     */     }
/* 285 */     Symbol.ClassSymbol localClassSymbol = paramClassSymbol.owner.enclClass();
/* 286 */     return (localClassSymbol == null) || ((l & 0x8) != 0L) || (isVisible(localClassSymbol));
/*     */   }
/*     */ 
/*     */   public void printError(String paramString)
/*     */   {
/* 297 */     if (this.silent)
/* 298 */       return;
/* 299 */     this.messager.printError(paramString);
/*     */   }
/*     */ 
/*     */   public void error(DocImpl paramDocImpl, String paramString)
/*     */   {
/* 308 */     if (this.silent)
/* 309 */       return;
/* 310 */     this.messager.error(paramDocImpl == null ? null : paramDocImpl.position(), paramString, new Object[0]);
/*     */   }
/*     */ 
/*     */   public void error(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 319 */     if (this.silent)
/* 320 */       return;
/* 321 */     this.messager.error(paramSourcePosition, paramString, new Object[0]);
/*     */   }
/*     */ 
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 330 */     if (this.silent)
/* 331 */       return;
/* 332 */     this.messager.printError(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/* 342 */     if (this.silent)
/* 343 */       return;
/* 344 */     this.messager.error(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 355 */     if (this.silent)
/* 356 */       return;
/* 357 */     this.messager.error(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 369 */     if (this.silent)
/* 370 */       return;
/* 371 */     this.messager.error(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */ 
/*     */   public void printWarning(String paramString)
/*     */   {
/* 380 */     if (this.silent)
/* 381 */       return;
/* 382 */     this.messager.printWarning(paramString);
/*     */   }
/*     */ 
/*     */   public void warning(DocImpl paramDocImpl, String paramString)
/*     */   {
/* 391 */     if (this.silent)
/* 392 */       return;
/* 393 */     this.messager.warning(paramDocImpl == null ? null : paramDocImpl.position(), paramString, new Object[0]);
/*     */   }
/*     */ 
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 402 */     if (this.silent)
/* 403 */       return;
/* 404 */     this.messager.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/* 414 */     if (this.silent) {
/* 415 */       return;
/*     */     }
/* 417 */     if ((this.doclint != null) && (paramDocImpl != null) && (paramString1.startsWith("tag")))
/* 418 */       return;
/* 419 */     this.messager.warning(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 430 */     if (this.silent)
/* 431 */       return;
/* 432 */     this.messager.warning(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 444 */     if (this.silent)
/* 445 */       return;
/* 446 */     this.messager.warning(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */ 
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */   {
/* 459 */     if (this.silent)
/* 460 */       return;
/* 461 */     this.messager.warning(paramDocImpl == null ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4, paramString5 });
/*     */   }
/*     */ 
/*     */   public void printNotice(String paramString)
/*     */   {
/* 470 */     if ((this.silent) || (this.quiet))
/* 471 */       return;
/* 472 */     this.messager.printNotice(paramString);
/*     */   }
/*     */ 
/*     */   public void notice(String paramString)
/*     */   {
/* 482 */     if ((this.silent) || (this.quiet))
/* 483 */       return;
/* 484 */     this.messager.notice(paramString, new Object[0]);
/*     */   }
/*     */ 
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 493 */     if ((this.silent) || (this.quiet))
/* 494 */       return;
/* 495 */     this.messager.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   public void notice(String paramString1, String paramString2)
/*     */   {
/* 505 */     if ((this.silent) || (this.quiet))
/* 506 */       return;
/* 507 */     this.messager.notice(paramString1, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */   public void notice(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 518 */     if ((this.silent) || (this.quiet))
/* 519 */       return;
/* 520 */     this.messager.notice(paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */ 
/*     */   public void notice(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 532 */     if ((this.silent) || (this.quiet))
/* 533 */       return;
/* 534 */     this.messager.notice(paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */ 
/*     */   public void exit()
/*     */   {
/* 544 */     this.messager.exit();
/*     */   }
/*     */ 
/*     */   public PackageDocImpl getPackageDoc(Symbol.PackageSymbol paramPackageSymbol)
/*     */   {
/* 553 */     PackageDocImpl localPackageDocImpl = (PackageDocImpl)this.packageMap.get(paramPackageSymbol);
/* 554 */     if (localPackageDocImpl != null) return localPackageDocImpl;
/* 555 */     localPackageDocImpl = new PackageDocImpl(this, paramPackageSymbol);
/* 556 */     this.packageMap.put(paramPackageSymbol, localPackageDocImpl);
/* 557 */     return localPackageDocImpl;
/*     */   }
/*     */ 
/*     */   void makePackageDoc(Symbol.PackageSymbol paramPackageSymbol, TreePath paramTreePath)
/*     */   {
/* 564 */     PackageDocImpl localPackageDocImpl = (PackageDocImpl)this.packageMap.get(paramPackageSymbol);
/* 565 */     if (localPackageDocImpl != null) {
/* 566 */       if (paramTreePath != null) localPackageDocImpl.setTreePath(paramTreePath); 
/*     */     }
/* 568 */     else { localPackageDocImpl = new PackageDocImpl(this, paramPackageSymbol, paramTreePath);
/* 569 */       this.packageMap.put(paramPackageSymbol, localPackageDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ClassDocImpl getClassDoc(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 580 */     Object localObject = (ClassDocImpl)this.classMap.get(paramClassSymbol);
/* 581 */     if (localObject != null) return localObject;
/* 582 */     if (isAnnotationType(paramClassSymbol))
/* 583 */       localObject = new AnnotationTypeDocImpl(this, paramClassSymbol);
/*     */     else {
/* 585 */       localObject = new ClassDocImpl(this, paramClassSymbol);
/*     */     }
/* 587 */     this.classMap.put(paramClassSymbol, localObject);
/* 588 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected void makeClassDoc(Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath)
/*     */   {
/* 595 */     Object localObject = (ClassDocImpl)this.classMap.get(paramClassSymbol);
/* 596 */     if (localObject != null) {
/* 597 */       if (paramTreePath != null) ((ClassDocImpl)localObject).setTreePath(paramTreePath);
/* 598 */       return;
/*     */     }
/* 600 */     if (isAnnotationType((JCTree.JCClassDecl)paramTreePath.getLeaf()))
/* 601 */       localObject = new AnnotationTypeDocImpl(this, paramClassSymbol, paramTreePath);
/*     */     else {
/* 603 */       localObject = new ClassDocImpl(this, paramClassSymbol, paramTreePath);
/*     */     }
/* 605 */     this.classMap.put(paramClassSymbol, localObject);
/*     */   }
/*     */ 
/*     */   protected static boolean isAnnotationType(Symbol.ClassSymbol paramClassSymbol) {
/* 609 */     return ClassDocImpl.isAnnotationType(paramClassSymbol);
/*     */   }
/*     */ 
/*     */   protected static boolean isAnnotationType(JCTree.JCClassDecl paramJCClassDecl) {
/* 613 */     return (paramJCClassDecl.mods.flags & 0x2000) != 0L;
/*     */   }
/*     */ 
/*     */   public FieldDocImpl getFieldDoc(Symbol.VarSymbol paramVarSymbol)
/*     */   {
/* 622 */     FieldDocImpl localFieldDocImpl = (FieldDocImpl)this.fieldMap.get(paramVarSymbol);
/* 623 */     if (localFieldDocImpl != null) return localFieldDocImpl;
/* 624 */     localFieldDocImpl = new FieldDocImpl(this, paramVarSymbol);
/* 625 */     this.fieldMap.put(paramVarSymbol, localFieldDocImpl);
/* 626 */     return localFieldDocImpl;
/*     */   }
/*     */ 
/*     */   protected void makeFieldDoc(Symbol.VarSymbol paramVarSymbol, TreePath paramTreePath)
/*     */   {
/* 632 */     FieldDocImpl localFieldDocImpl = (FieldDocImpl)this.fieldMap.get(paramVarSymbol);
/* 633 */     if (localFieldDocImpl != null) {
/* 634 */       if (paramTreePath != null) localFieldDocImpl.setTreePath(paramTreePath); 
/*     */     }
/* 636 */     else { localFieldDocImpl = new FieldDocImpl(this, paramVarSymbol, paramTreePath);
/* 637 */       this.fieldMap.put(paramVarSymbol, localFieldDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void makeMethodDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/* 648 */     MethodDocImpl localMethodDocImpl = (MethodDocImpl)this.methodMap.get(paramMethodSymbol);
/* 649 */     if (localMethodDocImpl != null) {
/* 650 */       if (paramTreePath != null) localMethodDocImpl.setTreePath(paramTreePath); 
/*     */     }
/* 652 */     else { localMethodDocImpl = new MethodDocImpl(this, paramMethodSymbol, paramTreePath);
/* 653 */       this.methodMap.put(paramMethodSymbol, localMethodDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MethodDocImpl getMethodDoc(Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/* 662 */     assert (!paramMethodSymbol.isConstructor()) : "not expecting a constructor symbol";
/* 663 */     MethodDocImpl localMethodDocImpl = (MethodDocImpl)this.methodMap.get(paramMethodSymbol);
/* 664 */     if (localMethodDocImpl != null) return localMethodDocImpl;
/* 665 */     localMethodDocImpl = new MethodDocImpl(this, paramMethodSymbol);
/* 666 */     this.methodMap.put(paramMethodSymbol, localMethodDocImpl);
/* 667 */     return localMethodDocImpl;
/*     */   }
/*     */ 
/*     */   protected void makeConstructorDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/* 675 */     ConstructorDocImpl localConstructorDocImpl = (ConstructorDocImpl)this.methodMap.get(paramMethodSymbol);
/* 676 */     if (localConstructorDocImpl != null) {
/* 677 */       if (paramTreePath != null) localConstructorDocImpl.setTreePath(paramTreePath); 
/*     */     }
/* 679 */     else { localConstructorDocImpl = new ConstructorDocImpl(this, paramMethodSymbol, paramTreePath);
/* 680 */       this.methodMap.put(paramMethodSymbol, localConstructorDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ConstructorDocImpl getConstructorDoc(Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/* 689 */     assert (paramMethodSymbol.isConstructor()) : "expecting a constructor symbol";
/* 690 */     ConstructorDocImpl localConstructorDocImpl = (ConstructorDocImpl)this.methodMap.get(paramMethodSymbol);
/* 691 */     if (localConstructorDocImpl != null) return localConstructorDocImpl;
/* 692 */     localConstructorDocImpl = new ConstructorDocImpl(this, paramMethodSymbol);
/* 693 */     this.methodMap.put(paramMethodSymbol, localConstructorDocImpl);
/* 694 */     return localConstructorDocImpl;
/*     */   }
/*     */ 
/*     */   protected void makeAnnotationTypeElementDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/* 703 */     AnnotationTypeElementDocImpl localAnnotationTypeElementDocImpl = (AnnotationTypeElementDocImpl)this.methodMap
/* 703 */       .get(paramMethodSymbol);
/*     */ 
/* 704 */     if (localAnnotationTypeElementDocImpl != null) {
/* 705 */       if (paramTreePath != null) localAnnotationTypeElementDocImpl.setTreePath(paramTreePath); 
/*     */     }
/* 707 */     else { localAnnotationTypeElementDocImpl = new AnnotationTypeElementDocImpl(this, paramMethodSymbol, paramTreePath);
/*     */ 
/* 709 */       this.methodMap.put(paramMethodSymbol, localAnnotationTypeElementDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AnnotationTypeElementDocImpl getAnnotationTypeElementDoc(Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/* 721 */     AnnotationTypeElementDocImpl localAnnotationTypeElementDocImpl = (AnnotationTypeElementDocImpl)this.methodMap
/* 721 */       .get(paramMethodSymbol);
/*     */ 
/* 722 */     if (localAnnotationTypeElementDocImpl != null) return localAnnotationTypeElementDocImpl;
/* 723 */     localAnnotationTypeElementDocImpl = new AnnotationTypeElementDocImpl(this, paramMethodSymbol);
/* 724 */     this.methodMap.put(paramMethodSymbol, localAnnotationTypeElementDocImpl);
/* 725 */     return localAnnotationTypeElementDocImpl;
/*     */   }
/*     */ 
/*     */   ParameterizedTypeImpl getParameterizedType(Type.ClassType paramClassType)
/*     */   {
/* 736 */     return new ParameterizedTypeImpl(this, paramClassType);
/*     */   }
/*     */ 
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*     */   {
/* 745 */     TreePath localTreePath = (TreePath)this.treePaths.get(paramJCCompilationUnit);
/* 746 */     if (localTreePath == null)
/* 747 */       this.treePaths.put(paramJCCompilationUnit, localTreePath = new TreePath(paramJCCompilationUnit));
/* 748 */     return localTreePath;
/*     */   }
/*     */ 
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl) {
/* 752 */     TreePath localTreePath = (TreePath)this.treePaths.get(paramJCClassDecl);
/* 753 */     if (localTreePath == null)
/* 754 */       this.treePaths.put(paramJCClassDecl, localTreePath = new TreePath(getTreePath(paramJCCompilationUnit), paramJCClassDecl));
/* 755 */     return localTreePath;
/*     */   }
/*     */ 
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl, JCTree paramJCTree) {
/* 759 */     return new TreePath(getTreePath(paramJCCompilationUnit, paramJCClassDecl), paramJCTree);
/*     */   }
/*     */ 
/*     */   public void setEncoding(String paramString)
/*     */   {
/* 766 */     this.encoding = paramString;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 773 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   static int translateModifiers(long paramLong)
/*     */   {
/* 781 */     int i = 0;
/* 782 */     if ((paramLong & 0x400) != 0L)
/* 783 */       i |= 1024;
/* 784 */     if ((paramLong & 0x10) != 0L)
/* 785 */       i |= 16;
/* 786 */     if ((paramLong & 0x200) != 0L)
/* 787 */       i |= 512;
/* 788 */     if ((paramLong & 0x100) != 0L)
/* 789 */       i |= 256;
/* 790 */     if ((paramLong & 0x2) != 0L)
/* 791 */       i |= 2;
/* 792 */     if ((paramLong & 0x4) != 0L)
/* 793 */       i |= 4;
/* 794 */     if ((paramLong & 1L) != 0L)
/* 795 */       i |= 1;
/* 796 */     if ((paramLong & 0x8) != 0L)
/* 797 */       i |= 8;
/* 798 */     if ((paramLong & 0x20) != 0L)
/* 799 */       i |= 32;
/* 800 */     if ((paramLong & 0x80) != 0L)
/* 801 */       i |= 128;
/* 802 */     if ((paramLong & 0x40) != 0L)
/* 803 */       i |= 64;
/* 804 */     return i;
/*     */   }
/*     */ 
/*     */   void initDoclint(Collection<String> paramCollection1, Collection<String> paramCollection2) {
/* 808 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 810 */     for (Object localObject1 = paramCollection1.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 811 */       localArrayList.add("-Xmsgs:" + (String)localObject2);
/*     */     }
/*     */ 
/* 814 */     if (localArrayList.isEmpty())
/* 815 */       localArrayList.add("-Xmsgs");
/* 816 */     else if ((localArrayList.size() == 1) && 
/* 817 */       (((String)localArrayList
/* 817 */       .get(0))
/* 817 */       .equals("-Xmsgs:none"))) {
/* 818 */       return;
/*     */     }
/*     */ 
/* 821 */     localObject1 = "";
/* 822 */     Object localObject2 = new StringBuilder();
/* 823 */     for (Object localObject3 = paramCollection2.iterator(); ((Iterator)localObject3).hasNext(); ) { String str = (String)((Iterator)localObject3).next();
/* 824 */       ((StringBuilder)localObject2).append((String)localObject1);
/* 825 */       ((StringBuilder)localObject2).append(str);
/* 826 */       localObject1 = ",";
/*     */     }
/* 828 */     localArrayList.add("-XcustomTags:" + ((StringBuilder)localObject2).toString());
/*     */ 
/* 830 */     localObject3 = BasicJavacTask.instance(this.context);
/* 831 */     this.doclint = new DocLint();
/*     */ 
/* 833 */     localArrayList.add("-XimplicitHeaders:2");
/* 834 */     this.doclint.init((JavacTask)localObject3, (String[])localArrayList.toArray(new String[localArrayList.size()]), false);
/*     */   }
/*     */ 
/*     */   boolean showTagMessages() {
/* 838 */     return this.doclint == null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.DocEnv
 * JD-Core Version:    0.6.2
 */