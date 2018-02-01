/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Array;
/*     */ import com.sun.tools.javac.code.Attribute.Class;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Attribute.Enum;
/*     */ import com.sun.tools.javac.code.Attribute.Error;
/*     */ import com.sun.tools.javac.code.Attribute.Visitor;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Enter;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.processing.PrintingProcessor.PrintingElementVisitor;
/*     */ import com.sun.tools.javac.tree.DocCommentTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*     */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*     */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeScanner;
/*     */ import com.sun.tools.javac.util.Constants;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class JavacElements
/*     */   implements Elements
/*     */ {
/*     */   private JavaCompiler javaCompiler;
/*     */   private Symtab syms;
/*     */   private Names names;
/*     */   private Types types;
/*     */   private Enter enter;
/*     */ 
/*     */   public static JavacElements instance(Context paramContext)
/*     */   {
/*  70 */     JavacElements localJavacElements = (JavacElements)paramContext.get(JavacElements.class);
/*  71 */     if (localJavacElements == null)
/*  72 */       localJavacElements = new JavacElements(paramContext);
/*  73 */     return localJavacElements;
/*     */   }
/*     */ 
/*     */   protected JavacElements(Context paramContext)
/*     */   {
/*  80 */     setContext(paramContext);
/*     */   }
/*     */ 
/*     */   public void setContext(Context paramContext)
/*     */   {
/*  88 */     paramContext.put(JavacElements.class, this);
/*  89 */     this.javaCompiler = JavaCompiler.instance(paramContext);
/*  90 */     this.syms = Symtab.instance(paramContext);
/*  91 */     this.names = Names.instance(paramContext);
/*  92 */     this.types = Types.instance(paramContext);
/*  93 */     this.enter = Enter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public Symbol.PackageSymbol getPackageElement(CharSequence paramCharSequence) {
/*  97 */     String str = paramCharSequence.toString();
/*  98 */     if (str.equals("")) {
/*  99 */       return this.syms.unnamedPackage;
/*     */     }
/* 101 */     return SourceVersion.isName(str) ? 
/* 101 */       (Symbol.PackageSymbol)nameToSymbol(str, Symbol.PackageSymbol.class) : 
/* 101 */       null;
/*     */   }
/*     */ 
/*     */   public Symbol.ClassSymbol getTypeElement(CharSequence paramCharSequence)
/*     */   {
/* 106 */     String str = paramCharSequence.toString();
/*     */ 
/* 108 */     return SourceVersion.isName(str) ? 
/* 108 */       (Symbol.ClassSymbol)nameToSymbol(str, Symbol.ClassSymbol.class) : 
/* 108 */       null;
/*     */   }
/*     */ 
/*     */   private <S extends Symbol> S nameToSymbol(String paramString, Class<S> paramClass)
/*     */   {
/* 117 */     Name localName = this.names.fromString(paramString);
/*     */ 
/* 121 */     Symbol localSymbol = paramClass == Symbol.ClassSymbol.class ? 
/* 120 */       (Symbol)this.syms.classes
/* 120 */       .get(localName) : 
/* 121 */       (Symbol)this.syms.packages
/* 121 */       .get(localName);
/*     */     try
/*     */     {
/* 124 */       if (localSymbol == null) {
/* 125 */         localSymbol = this.javaCompiler.resolveIdent(paramString);
/*     */       }
/* 127 */       localSymbol.complete();
/*     */ 
/* 133 */       return (localSymbol.kind != 63) && 
/* 130 */         (localSymbol
/* 130 */         .exists()) && 
/* 131 */         (paramClass
/* 131 */         .isInstance(localSymbol)) && 
/* 132 */         (localName
/* 132 */         .equals(localSymbol
/* 132 */         .getQualifiedName())) ? 
/* 133 */         (Symbol)paramClass
/* 133 */         .cast(localSymbol) : 
/* 133 */         null;
/*     */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement)
/*     */   {
/* 141 */     Pair localPair = getTreeAndTopLevel(paramElement);
/* 142 */     if (localPair == null)
/* 143 */       return null;
/* 144 */     JCTree localJCTree = (JCTree)localPair.fst;
/* 145 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localPair.snd;
/* 146 */     JavaFileObject localJavaFileObject = localJCCompilationUnit.sourcefile;
/* 147 */     if (localJavaFileObject == null)
/* 148 */       return null;
/* 149 */     return new JavacSourcePosition(localJavaFileObject, localJCTree.pos, localJCCompilationUnit.lineMap);
/*     */   }
/*     */ 
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement, AnnotationMirror paramAnnotationMirror) {
/* 153 */     Pair localPair = getTreeAndTopLevel(paramElement);
/* 154 */     if (localPair == null)
/* 155 */       return null;
/* 156 */     JCTree localJCTree1 = (JCTree)localPair.fst;
/* 157 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localPair.snd;
/* 158 */     JavaFileObject localJavaFileObject = localJCCompilationUnit.sourcefile;
/* 159 */     if (localJavaFileObject == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     JCTree localJCTree2 = matchAnnoToTree(paramAnnotationMirror, paramElement, localJCTree1);
/* 163 */     if (localJCTree2 == null)
/* 164 */       return null;
/* 165 */     return new JavacSourcePosition(localJavaFileObject, localJCTree2.pos, localJCCompilationUnit.lineMap);
/*     */   }
/*     */ 
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue)
/*     */   {
/* 172 */     return getSourcePosition(paramElement, paramAnnotationMirror);
/*     */   }
/*     */ 
/*     */   private JCTree matchAnnoToTree(AnnotationMirror paramAnnotationMirror, Element paramElement, JCTree paramJCTree)
/*     */   {
/* 181 */     Symbol localSymbol = (Symbol)cast(Symbol.class, paramElement);
/*     */ 
/* 197 */     JCTree.Visitor local1Vis = new JCTree.Visitor()
/*     */     {
/* 183 */       List<JCTree.JCAnnotation> result = null;
/*     */ 
/* 185 */       public void visitTopLevel(JCTree.JCCompilationUnit paramAnonymousJCCompilationUnit) { this.result = paramAnonymousJCCompilationUnit.packageAnnotations; }
/*     */ 
/*     */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) {
/* 188 */         this.result = paramAnonymousJCClassDecl.mods.annotations;
/*     */       }
/*     */       public void visitMethodDef(JCTree.JCMethodDecl paramAnonymousJCMethodDecl) {
/* 191 */         this.result = paramAnonymousJCMethodDecl.mods.annotations;
/*     */       }
/*     */       public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl) {
/* 194 */         this.result = paramAnonymousJCVariableDecl.mods.annotations;
/*     */       }
/*     */     };
/* 198 */     paramJCTree.accept(local1Vis);
/* 199 */     if (local1Vis.result == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     List localList = localSymbol.getRawAttributes();
/* 203 */     return matchAnnoToTree((Attribute.Compound)cast(Attribute.Compound.class, paramAnnotationMirror), localList, local1Vis.result);
/*     */   }
/*     */ 
/*     */   private JCTree matchAnnoToTree(Attribute.Compound paramCompound, List<Attribute.Compound> paramList, List<JCTree.JCAnnotation> paramList1)
/*     */   {
/* 216 */     for (Iterator localIterator1 = paramList.iterator(); localIterator1.hasNext(); ) { localCompound = (Attribute.Compound)localIterator1.next();
/* 217 */       for (JCTree.JCAnnotation localJCAnnotation : paramList1) {
/* 218 */         JCTree localJCTree = matchAnnoToTree(paramCompound, localCompound, localJCAnnotation);
/* 219 */         if (localJCTree != null)
/* 220 */           return localJCTree;
/*     */       }
/*     */     }
/*     */     Attribute.Compound localCompound;
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */   private JCTree matchAnnoToTree(final Attribute.Compound paramCompound, Attribute paramAttribute, final JCTree paramJCTree)
/*     */   {
/* 234 */     if (paramAttribute == paramCompound) {
/* 235 */       return paramJCTree.type.tsym == paramCompound.type.tsym ? paramJCTree : null;
/*     */     }
/*     */ 
/* 273 */     Attribute.Visitor local2Vis = new Attribute.Visitor()
/*     */     {
/* 238 */       JCTree result = null;
/*     */ 
/*     */       public void visitConstant(Attribute.Constant paramAnonymousConstant) {
/*     */       }
/*     */       public void visitClass(Attribute.Class paramAnonymousClass) {
/*     */       }
/* 244 */       public void visitCompound(Attribute.Compound paramAnonymousCompound) { for (Pair localPair : paramAnonymousCompound.values) {
/* 245 */           JCTree.JCExpression localJCExpression = this.this$0.scanForAssign((Symbol.MethodSymbol)localPair.fst, paramJCTree);
/* 246 */           if (localJCExpression != null) {
/* 247 */             JCTree localJCTree = this.this$0.matchAnnoToTree(paramCompound, (Attribute)localPair.snd, localJCExpression);
/* 248 */             if (localJCTree != null) {
/* 249 */               this.result = localJCTree;
/* 250 */               return;
/*     */             }
/*     */           }
/*     */         } }
/*     */ 
/*     */       public void visitArray(Attribute.Array paramAnonymousArray) {
/* 256 */         if ((paramJCTree.hasTag(JCTree.Tag.NEWARRAY)) && 
/* 257 */           (this.this$0.types
/* 257 */           .elemtype(paramAnonymousArray.type).tsym == paramCompound.type.tsym)) {
/* 258 */           List localList = ((JCTree.JCNewArray)paramJCTree).elems;
/* 259 */           for (Attribute localAttribute : paramAnonymousArray.values) {
/* 260 */             if (localAttribute == paramCompound) {
/* 261 */               this.result = ((JCTree)localList.head);
/* 262 */               return;
/*     */             }
/* 264 */             localList = localList.tail;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public void visitEnum(Attribute.Enum paramAnonymousEnum)
/*     */       {
/*     */       }
/*     */ 
/*     */       public void visitError(Attribute.Error paramAnonymousError)
/*     */       {
/*     */       }
/*     */     };
/* 274 */     paramAttribute.accept(local2Vis);
/* 275 */     return local2Vis.result;
/*     */   }
/*     */ 
/*     */   private JCTree.JCExpression scanForAssign(final Symbol.MethodSymbol paramMethodSymbol, final JCTree paramJCTree)
/*     */   {
/* 302 */     TreeScanner local1TS = new TreeScanner()
/*     */     {
/* 285 */       JCTree.JCExpression result = null;
/*     */ 
/* 287 */       public void scan(JCTree paramAnonymousJCTree) { if ((paramAnonymousJCTree != null) && (this.result == null))
/* 288 */           paramAnonymousJCTree.accept(this); }
/*     */ 
/*     */       public void visitAnnotation(JCTree.JCAnnotation paramAnonymousJCAnnotation) {
/* 291 */         if (paramAnonymousJCAnnotation == paramJCTree)
/* 292 */           scan(paramAnonymousJCAnnotation.args); 
/*     */       }
/*     */ 
/* 295 */       public void visitAssign(JCTree.JCAssign paramAnonymousJCAssign) { if (paramAnonymousJCAssign.lhs.hasTag(JCTree.Tag.IDENT)) {
/* 296 */           JCTree.JCIdent localJCIdent = (JCTree.JCIdent)paramAnonymousJCAssign.lhs;
/* 297 */           if (localJCIdent.sym == paramMethodSymbol)
/* 298 */             this.result = paramAnonymousJCAssign.rhs;
/*     */         }
/*     */       }
/*     */     };
/* 303 */     paramJCTree.accept(local1TS);
/* 304 */     return local1TS.result;
/*     */   }
/*     */ 
/*     */   public JCTree getTree(Element paramElement)
/*     */   {
/* 312 */     Pair localPair = getTreeAndTopLevel(paramElement);
/* 313 */     return localPair != null ? (JCTree)localPair.fst : null;
/*     */   }
/*     */ 
/*     */   public String getDocComment(Element paramElement)
/*     */   {
/* 321 */     Pair localPair = getTreeAndTopLevel(paramElement);
/* 322 */     if (localPair == null)
/* 323 */       return null;
/* 324 */     JCTree localJCTree = (JCTree)localPair.fst;
/* 325 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localPair.snd;
/* 326 */     if (localJCCompilationUnit.docComments == null)
/* 327 */       return null;
/* 328 */     return localJCCompilationUnit.docComments.getCommentText(localJCTree);
/*     */   }
/*     */ 
/*     */   public PackageElement getPackageOf(Element paramElement) {
/* 332 */     return ((Symbol)cast(Symbol.class, paramElement)).packge();
/*     */   }
/*     */ 
/*     */   public boolean isDeprecated(Element paramElement) {
/* 336 */     Symbol localSymbol = (Symbol)cast(Symbol.class, paramElement);
/* 337 */     return (localSymbol.flags() & 0x20000) != 0L;
/*     */   }
/*     */ 
/*     */   public Name getBinaryName(TypeElement paramTypeElement) {
/* 341 */     return ((Symbol.TypeSymbol)cast(Symbol.TypeSymbol.class, paramTypeElement)).flatName();
/*     */   }
/*     */ 
/*     */   public Map<Symbol.MethodSymbol, Attribute> getElementValuesWithDefaults(AnnotationMirror paramAnnotationMirror)
/*     */   {
/* 346 */     Attribute.Compound localCompound = (Attribute.Compound)cast(Attribute.Compound.class, paramAnnotationMirror);
/* 347 */     DeclaredType localDeclaredType = paramAnnotationMirror.getAnnotationType();
/* 348 */     Map localMap = localCompound.getElementValues();
/*     */ 
/* 351 */     for (ExecutableElement localExecutableElement : ElementFilter.methodsIn(localDeclaredType.asElement().getEnclosedElements())) {
/* 352 */       Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localExecutableElement;
/* 353 */       Attribute localAttribute = localMethodSymbol.getDefaultValue();
/* 354 */       if ((localAttribute != null) && (!localMap.containsKey(localMethodSymbol))) {
/* 355 */         localMap.put(localMethodSymbol, localAttribute);
/*     */       }
/*     */     }
/* 358 */     return localMap;
/*     */   }
/*     */ 
/*     */   public FilteredMemberList getAllMembers(TypeElement paramTypeElement)
/*     */   {
/* 365 */     Symbol localSymbol = (Symbol)cast(Symbol.class, paramTypeElement);
/* 366 */     Scope localScope = localSymbol.members().dupUnshared();
/* 367 */     List localList = this.types.closure(localSymbol.asType());
/* 368 */     for (Type localType : localList)
/* 369 */       addMembers(localScope, localType);
/* 370 */     return new FilteredMemberList(localScope);
/*     */   }
/*     */ 
/*     */   private void addMembers(Scope paramScope, Type paramType)
/*     */   {
/* 375 */     label224: for (Scope.Entry localEntry1 = paramType.asElement().members().elems; localEntry1 != null; localEntry1 = localEntry1.sibling) {
/* 376 */       Scope.Entry localEntry2 = paramScope.lookup(localEntry1.sym.getSimpleName());
/* 377 */       while (localEntry2.scope != null) {
/* 378 */         if ((localEntry2.sym.kind == localEntry1.sym.kind) && 
/* 379 */           ((localEntry2.sym
/* 379 */           .flags() & 0x1000) == 0L) && 
/* 381 */           (localEntry2.sym.getKind() == ElementKind.METHOD) && 
/* 382 */           (overrides((ExecutableElement)localEntry2.sym, (ExecutableElement)localEntry1.sym, 
/* 382 */           (TypeElement)paramType
/* 382 */           .asElement())))
/*     */         {
/*     */           break label224;
/*     */         }
/* 386 */         localEntry2 = localEntry2.next();
/*     */       }
/* 388 */       int i = localEntry1.sym.getEnclosingElement() != paramScope.owner ? 1 : 0;
/* 389 */       ElementKind localElementKind = localEntry1.sym.getKind();
/* 390 */       int j = (localElementKind == ElementKind.CONSTRUCTOR) || (localElementKind == ElementKind.INSTANCE_INIT) || (localElementKind == ElementKind.STATIC_INIT) ? 1 : 0;
/*     */ 
/* 393 */       if ((i == 0) || ((j == 0) && (localEntry1.sym.isInheritedIn(paramScope.owner, this.types))))
/* 394 */         paramScope.enter(localEntry1.sym);
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<Attribute.Compound> getAllAnnotationMirrors(Element paramElement)
/*     */   {
/* 407 */     Object localObject = (Symbol)cast(Symbol.class, paramElement);
/* 408 */     List localList1 = ((Symbol)localObject).getAnnotationMirrors();
/*     */     List localList2;
/* 409 */     while (((Symbol)localObject).getKind() == ElementKind.CLASS) {
/* 410 */       Type localType = ((Symbol.ClassSymbol)localObject).getSuperclass();
/* 411 */       if ((!localType.hasTag(TypeTag.CLASS)) || (localType.isErroneous()) || (localType.tsym == this.syms.objectType.tsym))
/*     */       {
/*     */         break;
/*     */       }
/* 415 */       localObject = localType.tsym;
/* 416 */       localList2 = localList1;
/* 417 */       List localList3 = ((Symbol)localObject).getAnnotationMirrors();
/* 418 */       for (Attribute.Compound localCompound : localList3) {
/* 419 */         if ((isInherited(localCompound.type)) && 
/* 420 */           (!containsAnnoOfType(localList2, localCompound.type)))
/*     */         {
/* 421 */           localList1 = localList1.prepend(localCompound);
/*     */         }
/*     */       }
/*     */     }
/* 425 */     return localList1;
/*     */   }
/*     */ 
/*     */   private boolean isInherited(Type paramType)
/*     */   {
/* 432 */     return paramType.tsym.attribute(this.syms.inheritedType.tsym) != null;
/*     */   }
/*     */ 
/*     */   private static boolean containsAnnoOfType(List<Attribute.Compound> paramList, Type paramType)
/*     */   {
/* 441 */     for (Attribute.Compound localCompound : paramList) {
/* 442 */       if (localCompound.type.tsym == paramType.tsym)
/* 443 */         return true;
/*     */     }
/* 445 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hides(Element paramElement1, Element paramElement2) {
/* 449 */     Symbol localSymbol1 = (Symbol)cast(Symbol.class, paramElement1);
/* 450 */     Symbol localSymbol2 = (Symbol)cast(Symbol.class, paramElement2);
/*     */ 
/* 454 */     if ((localSymbol1 == localSymbol2) || (localSymbol1.kind != localSymbol2.kind) || (localSymbol1.name != localSymbol2.name))
/*     */     {
/* 457 */       return false;
/*     */     }
/*     */ 
/* 462 */     if ((localSymbol1.kind == 16) && (
/* 463 */       (!localSymbol1.isStatic()) || 
/* 464 */       (!this.types
/* 464 */       .isSubSignature(localSymbol1.type, localSymbol2.type))))
/*     */     {
/* 465 */       return false;
/*     */     }
/*     */ 
/* 472 */     Symbol.ClassSymbol localClassSymbol1 = localSymbol1.owner.enclClass();
/* 473 */     Symbol.ClassSymbol localClassSymbol2 = localSymbol2.owner.enclClass();
/* 474 */     if ((localClassSymbol1 == null) || (localClassSymbol2 == null) || 
/* 475 */       (!localClassSymbol1
/* 475 */       .isSubClass(localClassSymbol2, this.types)))
/*     */     {
/* 476 */       return false;
/*     */     }
/*     */ 
/* 481 */     return localSymbol2.isInheritedIn(localClassSymbol1, this.types);
/*     */   }
/*     */ 
/*     */   public boolean overrides(ExecutableElement paramExecutableElement1, ExecutableElement paramExecutableElement2, TypeElement paramTypeElement)
/*     */   {
/* 486 */     Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)cast(Symbol.MethodSymbol.class, paramExecutableElement1);
/* 487 */     Symbol.MethodSymbol localMethodSymbol2 = (Symbol.MethodSymbol)cast(Symbol.MethodSymbol.class, paramExecutableElement2);
/* 488 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)cast(Symbol.ClassSymbol.class, paramTypeElement);
/*     */ 
/* 490 */     if ((localMethodSymbol1.name == localMethodSymbol2.name) && (localMethodSymbol1 != localMethodSymbol2))
/*     */     {
/* 497 */       if (!localMethodSymbol1
/* 497 */         .isStatic())
/*     */       {
/* 500 */         if (!localMethodSymbol2
/* 500 */           .isMemberOf(localClassSymbol, this.types));
/*     */       }
/*     */     }
/* 503 */     return localMethodSymbol1
/* 503 */       .overrides(localMethodSymbol2, localClassSymbol, this.types, false);
/*     */   }
/*     */ 
/*     */   public String getConstantExpression(Object paramObject)
/*     */   {
/* 507 */     return Constants.format(paramObject);
/*     */   }
/*     */ 
/*     */   public void printElements(Writer paramWriter, Element[] paramArrayOfElement)
/*     */   {
/* 520 */     for (Element localElement : paramArrayOfElement)
/* 521 */       ((PrintingProcessor.PrintingElementVisitor)new PrintingProcessor.PrintingElementVisitor(paramWriter, this).visit(localElement)).flush();
/*     */   }
/*     */ 
/*     */   public Name getName(CharSequence paramCharSequence) {
/* 525 */     return this.names.fromString(paramCharSequence.toString());
/*     */   }
/*     */ 
/*     */   public boolean isFunctionalInterface(TypeElement paramTypeElement)
/*     */   {
/* 530 */     if (paramTypeElement.getKind() != ElementKind.INTERFACE) {
/* 531 */       return false;
/*     */     }
/* 533 */     Symbol.TypeSymbol localTypeSymbol = (Symbol.TypeSymbol)cast(Symbol.TypeSymbol.class, paramTypeElement);
/* 534 */     return this.types.isFunctionalInterface(localTypeSymbol);
/*     */   }
/*     */ 
/*     */   private Pair<JCTree, JCTree.JCCompilationUnit> getTreeAndTopLevel(Element paramElement)
/*     */   {
/* 543 */     Symbol localSymbol = (Symbol)cast(Symbol.class, paramElement);
/* 544 */     Env localEnv = getEnterEnv(localSymbol);
/* 545 */     if (localEnv == null)
/* 546 */       return null;
/* 547 */     JCTree localJCTree = TreeInfo.declarationFor(localSymbol, localEnv.tree);
/* 548 */     if ((localJCTree == null) || (localEnv.toplevel == null))
/* 549 */       return null;
/* 550 */     return new Pair(localJCTree, localEnv.toplevel);
/*     */   }
/*     */ 
/*     */   public Pair<JCTree, JCTree.JCCompilationUnit> getTreeAndTopLevel(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue)
/*     */   {
/* 564 */     if (paramElement == null) {
/* 565 */       return null;
/*     */     }
/* 567 */     Pair localPair = getTreeAndTopLevel(paramElement);
/* 568 */     if (localPair == null) {
/* 569 */       return null;
/*     */     }
/* 571 */     if (paramAnnotationMirror == null) {
/* 572 */       return localPair;
/*     */     }
/* 574 */     JCTree localJCTree = matchAnnoToTree(paramAnnotationMirror, paramElement, (JCTree)localPair.fst);
/* 575 */     if (localJCTree == null) {
/* 576 */       return localPair;
/*     */     }
/*     */ 
/* 581 */     return new Pair(localJCTree, localPair.snd);
/*     */   }
/*     */ 
/*     */   private Env<AttrContext> getEnterEnv(Symbol paramSymbol)
/*     */   {
/* 591 */     Symbol.PackageSymbol localPackageSymbol = paramSymbol.kind != 1 ? paramSymbol
/* 591 */       .enclClass() : (Symbol.PackageSymbol)paramSymbol;
/*     */ 
/* 594 */     return localPackageSymbol != null ? this.enter
/* 594 */       .getEnv(localPackageSymbol) : 
/* 594 */       null;
/*     */   }
/*     */ 
/*     */   private static <T> T cast(Class<T> paramClass, Object paramObject)
/*     */   {
/* 604 */     if (!paramClass.isInstance(paramObject))
/* 605 */       throw new IllegalArgumentException(paramObject.toString());
/* 606 */     return paramClass.cast(paramObject);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.model.JavacElements
 * JD-Core Version:    0.6.2
 */