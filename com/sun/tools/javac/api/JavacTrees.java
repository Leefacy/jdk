/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.doctree.DocTree.Kind;
/*     */ import com.sun.source.doctree.IdentifierTree;
/*     */ import com.sun.source.tree.CatchTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.Tree.Kind;
/*     */ import com.sun.source.util.DocSourcePositions;
/*     */ import com.sun.source.util.DocTreePath;
/*     */ import com.sun.source.util.DocTreeScanner;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Type.ErrorType;
/*     */ import com.sun.tools.javac.code.Type.UnionClassType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.code.Types.TypeRelation;
/*     */ import com.sun.tools.javac.comp.Attr;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Enter;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.comp.MemberEnter;
/*     */ import com.sun.tools.javac.comp.Resolve;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.parser.Tokens.Comment;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.tree.DCTree;
/*     */ import com.sun.tools.javac.tree.DCTree.DCBlockTag;
/*     */ import com.sun.tools.javac.tree.DCTree.DCDocComment;
/*     */ import com.sun.tools.javac.tree.DCTree.DCEndPosTree;
/*     */ import com.sun.tools.javac.tree.DCTree.DCErroneous;
/*     */ import com.sun.tools.javac.tree.DCTree.DCIdentifier;
/*     */ import com.sun.tools.javac.tree.DCTree.DCParam;
/*     */ import com.sun.tools.javac.tree.DCTree.DCReference;
/*     */ import com.sun.tools.javac.tree.DCTree.DCText;
/*     */ import com.sun.tools.javac.tree.DocCommentTable;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCatch;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.TreeCopier;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Abort;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Log.DeferredDiagnosticHandler;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.ErrorType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.JavaCompiler.CompilationTask;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class JavacTrees extends DocTrees
/*     */ {
/*     */   private Resolve resolve;
/*     */   private Enter enter;
/*     */   private Log log;
/*     */   private MemberEnter memberEnter;
/*     */   private Attr attr;
/*     */   private TreeMaker treeMaker;
/*     */   private JavacElements elements;
/*     */   private JavacTaskImpl javacTaskImpl;
/*     */   private Names names;
/*     */   private Types types;
/* 646 */   Types.TypeRelation fuzzyMatcher = new Types.TypeRelation()
/*     */   {
/*     */     public Boolean visitType(Type paramAnonymousType1, Type paramAnonymousType2) {
/* 649 */       if (paramAnonymousType1 == paramAnonymousType2) {
/* 650 */         return Boolean.valueOf(true);
/*     */       }
/* 652 */       if (paramAnonymousType2.isPartial()) {
/* 653 */         return (Boolean)visit(paramAnonymousType2, paramAnonymousType1);
/*     */       }
/* 655 */       switch (JavacTrees.4.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramAnonymousType1.getTag().ordinal()]) { case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/* 658 */         return Boolean.valueOf(paramAnonymousType1.hasTag(paramAnonymousType2.getTag()));
/*     */       }
/* 660 */       throw new AssertionError("fuzzyMatcher " + paramAnonymousType1.getTag());
/*     */     }
/*     */ 
/*     */     public Boolean visitArrayType(Type.ArrayType paramAnonymousArrayType, Type paramAnonymousType)
/*     */     {
/* 666 */       if (paramAnonymousArrayType == paramAnonymousType) {
/* 667 */         return Boolean.valueOf(true);
/*     */       }
/* 669 */       if (paramAnonymousType.isPartial()) {
/* 670 */         return (Boolean)visit(paramAnonymousType, paramAnonymousArrayType);
/*     */       }
/* 672 */       return Boolean.valueOf((paramAnonymousType.hasTag(TypeTag.ARRAY)) && 
/* 673 */         (((Boolean)visit(paramAnonymousArrayType.elemtype, JavacTrees.this.types
/* 673 */         .elemtype(paramAnonymousType))).booleanValue()));
/*     */     }
/*     */ 
/*     */     public Boolean visitClassType(Type.ClassType paramAnonymousClassType, Type paramAnonymousType)
/*     */     {
/* 678 */       if (paramAnonymousClassType == paramAnonymousType) {
/* 679 */         return Boolean.valueOf(true);
/*     */       }
/* 681 */       if (paramAnonymousType.isPartial()) {
/* 682 */         return (Boolean)visit(paramAnonymousType, paramAnonymousClassType);
/*     */       }
/* 684 */       return Boolean.valueOf(paramAnonymousClassType.tsym == paramAnonymousType.tsym);
/*     */     }
/*     */ 
/*     */     public Boolean visitErrorType(Type.ErrorType paramAnonymousErrorType, Type paramAnonymousType)
/*     */     {
/* 689 */       return Boolean.valueOf((paramAnonymousType.hasTag(TypeTag.CLASS)) && (paramAnonymousErrorType.tsym.name == ((Type.ClassType)paramAnonymousType).tsym.name));
/*     */     }
/* 646 */   };
/*     */ 
/*     */   public static JavacTrees instance(JavaCompiler.CompilationTask paramCompilationTask)
/*     */   {
/* 135 */     if (!(paramCompilationTask instanceof BasicJavacTask))
/* 136 */       throw new IllegalArgumentException();
/* 137 */     return instance(((BasicJavacTask)paramCompilationTask).getContext());
/*     */   }
/*     */ 
/*     */   public static JavacTrees instance(ProcessingEnvironment paramProcessingEnvironment)
/*     */   {
/* 142 */     if (!(paramProcessingEnvironment instanceof JavacProcessingEnvironment))
/* 143 */       throw new IllegalArgumentException();
/* 144 */     return instance(((JavacProcessingEnvironment)paramProcessingEnvironment).getContext());
/*     */   }
/*     */ 
/*     */   public static JavacTrees instance(Context paramContext) {
/* 148 */     JavacTrees localJavacTrees = (JavacTrees)paramContext.get(JavacTrees.class);
/* 149 */     if (localJavacTrees == null)
/* 150 */       localJavacTrees = new JavacTrees(paramContext);
/* 151 */     return localJavacTrees;
/*     */   }
/*     */ 
/*     */   protected JavacTrees(Context paramContext) {
/* 155 */     paramContext.put(JavacTrees.class, this);
/* 156 */     init(paramContext);
/*     */   }
/*     */ 
/*     */   public void updateContext(Context paramContext) {
/* 160 */     init(paramContext);
/*     */   }
/*     */ 
/*     */   private void init(Context paramContext) {
/* 164 */     this.attr = Attr.instance(paramContext);
/* 165 */     this.enter = Enter.instance(paramContext);
/* 166 */     this.elements = JavacElements.instance(paramContext);
/* 167 */     this.log = Log.instance(paramContext);
/* 168 */     this.resolve = Resolve.instance(paramContext);
/* 169 */     this.treeMaker = TreeMaker.instance(paramContext);
/* 170 */     this.memberEnter = MemberEnter.instance(paramContext);
/* 171 */     this.names = Names.instance(paramContext);
/* 172 */     this.types = Types.instance(paramContext);
/*     */ 
/* 174 */     JavacTask localJavacTask = (JavacTask)paramContext.get(JavacTask.class);
/* 175 */     if ((localJavacTask instanceof JavacTaskImpl))
/* 176 */       this.javacTaskImpl = ((JavacTaskImpl)localJavacTask);
/*     */   }
/*     */ 
/*     */   public DocSourcePositions getSourcePositions() {
/* 180 */     return new DocSourcePositions() {
/*     */       public long getStartPosition(CompilationUnitTree paramAnonymousCompilationUnitTree, Tree paramAnonymousTree) {
/* 182 */         return TreeInfo.getStartPos((JCTree)paramAnonymousTree);
/*     */       }
/*     */ 
/*     */       public long getEndPosition(CompilationUnitTree paramAnonymousCompilationUnitTree, Tree paramAnonymousTree) {
/* 186 */         EndPosTable localEndPosTable = ((JCTree.JCCompilationUnit)paramAnonymousCompilationUnitTree).endPositions;
/* 187 */         return TreeInfo.getEndPos((JCTree)paramAnonymousTree, localEndPosTable);
/*     */       }
/*     */ 
/*     */       public long getStartPosition(CompilationUnitTree paramAnonymousCompilationUnitTree, DocCommentTree paramAnonymousDocCommentTree, DocTree paramAnonymousDocTree) {
/* 191 */         return ((DCTree)paramAnonymousDocTree).getSourcePosition((DCTree.DCDocComment)paramAnonymousDocCommentTree);
/*     */       }
/*     */ 
/*     */       public long getEndPosition(CompilationUnitTree paramAnonymousCompilationUnitTree, DocCommentTree paramAnonymousDocCommentTree, DocTree paramAnonymousDocTree) {
/* 195 */         DCTree.DCDocComment localDCDocComment = (DCTree.DCDocComment)paramAnonymousDocCommentTree;
/* 196 */         if ((paramAnonymousDocTree instanceof DCTree.DCEndPosTree)) {
/* 197 */           i = ((DCTree.DCEndPosTree)paramAnonymousDocTree).getEndPos(localDCDocComment);
/*     */ 
/* 199 */           if (i != -1) {
/* 200 */             return i;
/*     */           }
/*     */         }
/* 203 */         int i = 0;
/* 204 */         switch (JavacTrees.4.$SwitchMap$com$sun$source$doctree$DocTree$Kind[paramAnonymousDocTree.getKind().ordinal()]) {
/*     */         case 1:
/* 206 */           DCTree.DCText localDCText = (DCTree.DCText)paramAnonymousDocTree;
/*     */ 
/* 208 */           return localDCDocComment.comment.getSourcePos(localDCText.pos + localDCText.text.length());
/*     */         case 2:
/* 210 */           DCTree.DCErroneous localDCErroneous = (DCTree.DCErroneous)paramAnonymousDocTree;
/*     */ 
/* 212 */           return localDCDocComment.comment.getSourcePos(localDCErroneous.pos + localDCErroneous.body.length());
/*     */         case 3:
/* 214 */           DCTree.DCIdentifier localDCIdentifier = (DCTree.DCIdentifier)paramAnonymousDocTree;
/*     */ 
/* 216 */           return localDCDocComment.comment.getSourcePos(localDCIdentifier.pos + (localDCIdentifier.name != JavacTrees.this.names.error ? localDCIdentifier.name.length() : 0));
/*     */         case 4:
/* 218 */           DCTree.DCParam localDCParam = (DCTree.DCParam)paramAnonymousDocTree;
/*     */ 
/* 220 */           if ((localDCParam.isTypeParameter) && (localDCParam.getDescription().isEmpty()))
/* 221 */             i = 1; case 5:
/*     */         case 6:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/*     */         case 13:
/*     */         case 14:
/*     */         case 15:
/* 226 */           localDocTree = JavacTrees.this.getLastChild(paramAnonymousDocTree);
/*     */ 
/* 228 */           if (localDocTree != null) {
/* 229 */             return getEndPosition(paramAnonymousCompilationUnitTree, paramAnonymousDocCommentTree, localDocTree) + i;
/*     */           }
/*     */ 
/* 232 */           DCTree.DCBlockTag localDCBlockTag = (DCTree.DCBlockTag)paramAnonymousDocTree;
/*     */ 
/* 234 */           return localDCDocComment.comment.getSourcePos(localDCBlockTag.pos + localDCBlockTag.getTagName().length() + 1);
/*     */         }
/*     */ 
/* 237 */         DocTree localDocTree = JavacTrees.this.getLastChild(paramAnonymousDocTree);
/*     */ 
/* 239 */         if (localDocTree != null) {
/* 240 */           return getEndPosition(paramAnonymousCompilationUnitTree, paramAnonymousDocCommentTree, localDocTree);
/*     */         }
/*     */ 
/* 245 */         return -1L;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private DocTree getLastChild(DocTree paramDocTree) {
/* 251 */     final DocTree[] arrayOfDocTree = { null };
/*     */ 
/* 253 */     paramDocTree.accept(new DocTreeScanner() {
/*     */       public Void scan(DocTree paramAnonymousDocTree, Void paramAnonymousVoid) {
/* 255 */         if (paramAnonymousDocTree != null) arrayOfDocTree[0] = paramAnonymousDocTree;
/* 256 */         return null;
/*     */       }
/*     */     }
/*     */     , null);
/*     */ 
/* 260 */     return arrayOfDocTree[0];
/*     */   }
/*     */ 
/*     */   public JCTree.JCClassDecl getTree(TypeElement paramTypeElement) {
/* 264 */     return (JCTree.JCClassDecl)getTree(paramTypeElement);
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodDecl getTree(ExecutableElement paramExecutableElement) {
/* 268 */     return (JCTree.JCMethodDecl)getTree(paramExecutableElement);
/*     */   }
/*     */ 
/*     */   public JCTree getTree(Element paramElement) {
/* 272 */     Symbol localSymbol = (Symbol)paramElement;
/* 273 */     Symbol.ClassSymbol localClassSymbol = localSymbol.enclClass();
/* 274 */     Env localEnv = this.enter.getEnv(localClassSymbol);
/* 275 */     if (localEnv == null)
/* 276 */       return null;
/* 277 */     JCTree.JCClassDecl localJCClassDecl = localEnv.enclClass;
/* 278 */     if (localJCClassDecl != null) {
/* 279 */       if (TreeInfo.symbolFor(localJCClassDecl) == paramElement)
/* 280 */         return localJCClassDecl;
/* 281 */       for (JCTree localJCTree : localJCClassDecl.getMembers())
/* 282 */         if (TreeInfo.symbolFor(localJCTree) == paramElement)
/* 283 */           return localJCTree;
/*     */     }
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */   public JCTree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror) {
/* 289 */     return getTree(paramElement, paramAnnotationMirror, null);
/*     */   }
/*     */ 
/*     */   public JCTree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/* 293 */     Pair localPair = this.elements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue);
/* 294 */     if (localPair == null)
/* 295 */       return null;
/* 296 */     return (JCTree)localPair.fst;
/*     */   }
/*     */ 
/*     */   public TreePath getPath(CompilationUnitTree paramCompilationUnitTree, Tree paramTree) {
/* 300 */     return TreePath.getPath(paramCompilationUnitTree, paramTree);
/*     */   }
/*     */ 
/*     */   public TreePath getPath(Element paramElement) {
/* 304 */     return getPath(paramElement, null, null);
/*     */   }
/*     */ 
/*     */   public TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror) {
/* 308 */     return getPath(paramElement, paramAnnotationMirror, null);
/*     */   }
/*     */ 
/*     */   public TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/* 312 */     Pair localPair = this.elements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue);
/* 313 */     if (localPair == null)
/* 314 */       return null;
/* 315 */     return TreePath.getPath((CompilationUnitTree)localPair.snd, (Tree)localPair.fst);
/*     */   }
/*     */ 
/*     */   public Symbol getElement(TreePath paramTreePath) {
/* 319 */     JCTree localJCTree1 = (JCTree)paramTreePath.getLeaf();
/* 320 */     Symbol localSymbol = TreeInfo.symbolFor(localJCTree1);
/* 321 */     if ((localSymbol == null) && 
/* 322 */       (TreeInfo.isDeclaration(localJCTree1))) {
/* 323 */       for (TreePath localTreePath = paramTreePath; localTreePath != null; localTreePath = localTreePath.getParentPath()) {
/* 324 */         JCTree localJCTree2 = (JCTree)localTreePath.getLeaf();
/* 325 */         if (localJCTree2.hasTag(JCTree.Tag.CLASSDEF)) {
/* 326 */           JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localJCTree2;
/* 327 */           if (localJCClassDecl.sym != null) {
/* 328 */             if ((localJCClassDecl.sym.flags_field & 0x10000000) == 0L) break;
/* 329 */             this.attr.attribClass(localJCClassDecl.pos(), localJCClassDecl.sym);
/* 330 */             localSymbol = TreeInfo.symbolFor(localJCTree1); break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 338 */     return localSymbol;
/*     */   }
/*     */ 
/*     */   public Element getElement(DocTreePath paramDocTreePath)
/*     */   {
/* 343 */     DocTree localDocTree = paramDocTreePath.getLeaf();
/* 344 */     if ((localDocTree instanceof DCTree.DCReference))
/* 345 */       return attributeDocReference(paramDocTreePath.getTreePath(), (DCTree.DCReference)localDocTree);
/* 346 */     if (((localDocTree instanceof DCTree.DCIdentifier)) && 
/* 347 */       ((paramDocTreePath.getParentPath().getLeaf() instanceof DCTree.DCParam))) {
/* 348 */       return attributeParamIdentifier(paramDocTreePath.getTreePath(), (DCTree.DCParam)paramDocTreePath.getParentPath().getLeaf());
/*     */     }
/*     */ 
/* 351 */     return null;
/*     */   }
/*     */ 
/*     */   private Symbol attributeDocReference(TreePath paramTreePath, DCTree.DCReference paramDCReference) {
/* 355 */     Env localEnv = getAttrContext(paramTreePath);
/*     */ 
/* 357 */     Log.DeferredDiagnosticHandler localDeferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log);
/*     */     try
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 362 */       if (paramDCReference.qualifierExpression == null) {
/* 363 */         localObject1 = localEnv.enclClass.sym;
/* 364 */         localName = paramDCReference.memberName;
/*     */       }
/*     */       else
/*     */       {
/* 370 */         localObject2 = this.attr.attribType(paramDCReference.qualifierExpression, localEnv);
/* 371 */         if (((Type)localObject2).isErroneous()) {
/* 372 */           if (paramDCReference.memberName == null)
/*     */           {
/* 375 */             localObject3 = this.elements.getPackageElement(paramDCReference.qualifierExpression.toString());
/* 376 */             if (localObject3 != null)
/* 377 */               return localObject3;
/* 378 */             if (paramDCReference.qualifierExpression.hasTag(JCTree.Tag.IDENT))
/*     */             {
/* 381 */               localObject1 = localEnv.enclClass.sym;
/* 382 */               localName = ((JCTree.JCIdent)paramDCReference.qualifierExpression).name;
/*     */             } else {
/* 384 */               return null;
/*     */             }
/*     */           } else { return null; }
/*     */         }
/*     */         else {
/* 389 */           localObject1 = ((Type)localObject2).tsym;
/* 390 */           localName = paramDCReference.memberName;
/*     */         }
/*     */       }
/*     */ 
/* 394 */       if (localName == null)
/* 395 */         return localObject1;
/*     */       Object localObject6;
/* 398 */       if (paramDCReference.paramTypes == null) {
/* 399 */         localObject2 = null;
/*     */       } else {
/* 401 */         localObject3 = new ListBuffer();
/* 402 */         for (localObject4 = paramDCReference.paramTypes; ((List)localObject4).nonEmpty(); localObject4 = ((List)localObject4).tail) {
/* 403 */           localObject5 = (JCTree)((List)localObject4).head;
/* 404 */           localObject6 = this.attr.attribType((JCTree)localObject5, localEnv);
/* 405 */           ((ListBuffer)localObject3).add(localObject6);
/*     */         }
/* 407 */         localObject2 = ((ListBuffer)localObject3).toList();
/*     */       }
/*     */ 
/* 410 */       Object localObject3 = (Symbol.ClassSymbol)this.types.cvarUpperBound(((Symbol.TypeSymbol)localObject1).type).tsym;
/*     */ 
/* 414 */       Object localObject4 = localName == ((Symbol.ClassSymbol)localObject3).name ? 
/* 413 */         findConstructor((Symbol.ClassSymbol)localObject3, (List)localObject2) : 
/* 414 */         findMethod((Symbol.ClassSymbol)localObject3, localName, (List)localObject2);
/*     */ 
/* 415 */       if (localObject2 != null)
/*     */       {
/* 417 */         return localObject4;
/*     */       }
/*     */ 
/* 420 */       Object localObject5 = paramDCReference.paramTypes != null ? null : findField((Symbol.ClassSymbol)localObject3, localName);
/*     */ 
/* 422 */       if ((localObject5 != null) && ((localObject4 == null) || 
/* 424 */         (this.types
/* 424 */         .isSubtypeUnchecked(((Symbol.VarSymbol)localObject5)
/* 424 */         .enclClass().asType(), ((Symbol)localObject4).enclClass().asType())))) {
/* 425 */         return localObject5;
/*     */       }
/* 427 */       return localObject4;
/*     */     }
/*     */     catch (Abort localAbort)
/*     */     {
/*     */       Name localName;
/* 430 */       return null;
/*     */     } finally {
/* 432 */       this.log.popDiagnosticHandler(localDeferredDiagnosticHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Symbol attributeParamIdentifier(TreePath paramTreePath, DCTree.DCParam paramDCParam) {
/* 437 */     Symbol localSymbol1 = getElement(paramTreePath);
/* 438 */     if (localSymbol1 == null)
/* 439 */       return null;
/* 440 */     ElementKind localElementKind = localSymbol1.getKind();
/* 441 */     List localList = List.nil();
/* 442 */     if ((localElementKind == ElementKind.METHOD) || (localElementKind == ElementKind.CONSTRUCTOR)) {
/* 443 */       localObject = (Symbol.MethodSymbol)localSymbol1;
/*     */ 
/* 446 */       localList = paramDCParam.isTypeParameter() ? ((Symbol.MethodSymbol)localObject)
/* 445 */         .getTypeParameters() : ((Symbol.MethodSymbol)localObject)
/* 446 */         .getParameters();
/* 447 */     } else if ((localElementKind.isClass()) || (localElementKind.isInterface())) {
/* 448 */       localObject = (Symbol.ClassSymbol)localSymbol1;
/* 449 */       localList = ((Symbol.ClassSymbol)localObject).getTypeParameters();
/*     */     }
/*     */ 
/* 452 */     for (Object localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { Symbol localSymbol2 = (Symbol)((Iterator)localObject).next();
/* 453 */       if (localSymbol2.getSimpleName() == paramDCParam.getName().getName()) {
/* 454 */         return localSymbol2;
/*     */       }
/*     */     }
/* 457 */     return null;
/*     */   }
/*     */ 
/*     */   private Symbol.VarSymbol findField(Symbol.ClassSymbol paramClassSymbol, Name paramName)
/*     */   {
/* 462 */     return searchField(paramClassSymbol, paramName, new HashSet());
/*     */   }
/*     */ 
/*     */   private Symbol.VarSymbol searchField(Symbol.ClassSymbol paramClassSymbol, Name paramName, Set<Symbol.ClassSymbol> paramSet)
/*     */   {
/* 467 */     if (paramSet.contains(paramClassSymbol)) {
/* 468 */       return null;
/*     */     }
/* 470 */     paramSet.add(paramClassSymbol);
/*     */ 
/* 472 */     for (Object localObject1 = paramClassSymbol.members().lookup(paramName); 
/* 473 */       ((Scope.Entry)localObject1).scope != null; localObject1 = ((Scope.Entry)localObject1).next()) {
/* 474 */       if (((Scope.Entry)localObject1).sym.kind == 4) {
/* 475 */         return (Symbol.VarSymbol)((Scope.Entry)localObject1).sym;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 482 */     localObject1 = paramClassSymbol.owner.enclClass();
/* 483 */     if (localObject1 != null) {
/* 484 */       localObject2 = searchField((Symbol.ClassSymbol)localObject1, paramName, paramSet);
/* 485 */       if (localObject2 != null) {
/* 486 */         return localObject2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 491 */     Object localObject2 = paramClassSymbol.getSuperclass();
/* 492 */     if (((Type)localObject2).tsym != null) {
/* 493 */       localObject3 = searchField((Symbol.ClassSymbol)((Type)localObject2).tsym, paramName, paramSet);
/* 494 */       if (localObject3 != null) {
/* 495 */         return localObject3;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 500 */     Object localObject3 = paramClassSymbol.getInterfaces();
/* 501 */     for (Object localObject4 = localObject3; ((List)localObject4).nonEmpty(); localObject4 = ((List)localObject4).tail) {
/* 502 */       Type localType = (Type)((List)localObject4).head;
/* 503 */       if (!localType.isErroneous()) {
/* 504 */         Symbol.VarSymbol localVarSymbol = searchField((Symbol.ClassSymbol)localType.tsym, paramName, paramSet);
/* 505 */         if (localVarSymbol != null) {
/* 506 */           return localVarSymbol;
/*     */         }
/*     */       }
/*     */     }
/* 510 */     return null;
/*     */   }
/*     */ 
/*     */   Symbol.MethodSymbol findConstructor(Symbol.ClassSymbol paramClassSymbol, List<Type> paramList)
/*     */   {
/* 515 */     for (Scope.Entry localEntry = paramClassSymbol.members().lookup(this.names.init); 
/* 516 */       localEntry.scope != null; localEntry = localEntry.next()) {
/* 517 */       if ((localEntry.sym.kind == 16) && 
/* 518 */         (hasParameterTypes((Symbol.MethodSymbol)localEntry.sym, paramList))) {
/* 519 */         return (Symbol.MethodSymbol)localEntry.sym;
/*     */       }
/*     */     }
/*     */ 
/* 523 */     return null;
/*     */   }
/*     */ 
/*     */   private Symbol.MethodSymbol findMethod(Symbol.ClassSymbol paramClassSymbol, Name paramName, List<Type> paramList)
/*     */   {
/* 528 */     return searchMethod(paramClassSymbol, paramName, paramList, new HashSet());
/*     */   }
/*     */ 
/*     */   private Symbol.MethodSymbol searchMethod(Symbol.ClassSymbol paramClassSymbol, Name paramName, List<Type> paramList, Set<Symbol.ClassSymbol> paramSet)
/*     */   {
/* 537 */     if (paramName == this.names.init) {
/* 538 */       return null;
/*     */     }
/* 540 */     if (paramSet.contains(paramClassSymbol))
/* 541 */       return null;
/* 542 */     paramSet.add(paramClassSymbol);
/*     */ 
/* 545 */     Scope.Entry localEntry = paramClassSymbol.members().lookup(paramName);
/*     */ 
/* 551 */     if (paramList == null)
/*     */     {
/* 557 */       localObject1 = null;
/* 558 */       for (; localEntry.scope != null; localEntry = localEntry.next()) {
/* 559 */         if ((localEntry.sym.kind == 16) && 
/* 560 */           (localEntry.sym.name == paramName)) {
/* 561 */           localObject1 = (Symbol.MethodSymbol)localEntry.sym;
/*     */         }
/*     */       }
/*     */ 
/* 565 */       if (localObject1 != null)
/* 566 */         return localObject1;
/*     */     }
/*     */     else {
/* 569 */       for (; localEntry.scope != null; localEntry = localEntry.next()) {
/* 570 */         if ((localEntry.sym != null) && (localEntry.sym.kind == 16))
/*     */         {
/* 572 */           if (hasParameterTypes((Symbol.MethodSymbol)localEntry.sym, paramList)) {
/* 573 */             return (Symbol.MethodSymbol)localEntry.sym;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 583 */     Object localObject1 = paramClassSymbol.getSuperclass();
/* 584 */     if (((Type)localObject1).tsym != null) {
/* 585 */       localObject2 = searchMethod((Symbol.ClassSymbol)((Type)localObject1).tsym, paramName, paramList, paramSet);
/* 586 */       if (localObject2 != null) {
/* 587 */         return localObject2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 592 */     Object localObject2 = paramClassSymbol.getInterfaces();
/*     */     Object localObject4;
/* 593 */     for (Object localObject3 = localObject2; ((List)localObject3).nonEmpty(); localObject3 = ((List)localObject3).tail) {
/* 594 */       localObject4 = (Type)((List)localObject3).head;
/* 595 */       if (!((Type)localObject4).isErroneous()) {
/* 596 */         Symbol.MethodSymbol localMethodSymbol = searchMethod((Symbol.ClassSymbol)((Type)localObject4).tsym, paramName, paramList, paramSet);
/* 597 */         if (localMethodSymbol != null) {
/* 598 */           return localMethodSymbol;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 603 */     localObject3 = paramClassSymbol.owner.enclClass();
/* 604 */     if (localObject3 != null) {
/* 605 */       localObject4 = searchMethod((Symbol.ClassSymbol)localObject3, paramName, paramList, paramSet);
/* 606 */       if (localObject4 != null) {
/* 607 */         return localObject4;
/*     */       }
/*     */     }
/*     */ 
/* 611 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean hasParameterTypes(Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList)
/*     */   {
/* 616 */     if (paramList == null) {
/* 617 */       return true;
/*     */     }
/* 619 */     if (paramMethodSymbol.params().size() != paramList.size()) {
/* 620 */       return false;
/*     */     }
/* 622 */     List localList = this.types.erasureRecursive(paramMethodSymbol.asType()).getParameterTypes();
/*     */ 
/* 626 */     return Type.isErroneous(paramList) ? 
/* 625 */       fuzzyMatch(paramList, localList) : 
/* 625 */       this.types
/* 626 */       .isSameTypes(paramList, localList);
/*     */   }
/*     */ 
/*     */   boolean fuzzyMatch(List<Type> paramList1, List<Type> paramList2)
/*     */   {
/* 630 */     Object localObject1 = paramList1;
/* 631 */     Object localObject2 = paramList2;
/* 632 */     while (((List)localObject1).nonEmpty()) {
/* 633 */       if (!fuzzyMatch((Type)((List)localObject1).head, (Type)((List)localObject2).head))
/* 634 */         return false;
/* 635 */       localObject1 = ((List)localObject1).tail;
/* 636 */       localObject2 = ((List)localObject2).tail;
/*     */     }
/* 638 */     return true;
/*     */   }
/*     */ 
/*     */   boolean fuzzyMatch(Type paramType1, Type paramType2) {
/* 642 */     Boolean localBoolean = (Boolean)this.fuzzyMatcher.visit(paramType1, paramType2);
/* 643 */     return localBoolean == Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   public TypeMirror getTypeMirror(TreePath paramTreePath)
/*     */   {
/* 695 */     Tree localTree = paramTreePath.getLeaf();
/* 696 */     return ((JCTree)localTree).type;
/*     */   }
/*     */ 
/*     */   public JavacScope getScope(TreePath paramTreePath) {
/* 700 */     return new JavacScope(getAttrContext(paramTreePath));
/*     */   }
/*     */ 
/*     */   public String getDocComment(TreePath paramTreePath) {
/* 704 */     CompilationUnitTree localCompilationUnitTree = paramTreePath.getCompilationUnit();
/* 705 */     Tree localTree = paramTreePath.getLeaf();
/* 706 */     if (((localCompilationUnitTree instanceof JCTree.JCCompilationUnit)) && ((localTree instanceof JCTree))) {
/* 707 */       JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localCompilationUnitTree;
/* 708 */       if (localJCCompilationUnit.docComments != null) {
/* 709 */         return localJCCompilationUnit.docComments.getCommentText((JCTree)localTree);
/*     */       }
/*     */     }
/* 712 */     return null;
/*     */   }
/*     */ 
/*     */   public DocCommentTree getDocCommentTree(TreePath paramTreePath) {
/* 716 */     CompilationUnitTree localCompilationUnitTree = paramTreePath.getCompilationUnit();
/* 717 */     Tree localTree = paramTreePath.getLeaf();
/* 718 */     if (((localCompilationUnitTree instanceof JCTree.JCCompilationUnit)) && ((localTree instanceof JCTree))) {
/* 719 */       JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)localCompilationUnitTree;
/* 720 */       if (localJCCompilationUnit.docComments != null) {
/* 721 */         return localJCCompilationUnit.docComments.getCommentTree((JCTree)localTree);
/*     */       }
/*     */     }
/* 724 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isAccessible(com.sun.source.tree.Scope paramScope, TypeElement paramTypeElement) {
/* 728 */     if (((paramScope instanceof JavacScope)) && ((paramTypeElement instanceof Symbol.ClassSymbol))) {
/* 729 */       Env localEnv = ((JavacScope)paramScope).env;
/* 730 */       return this.resolve.isAccessible(localEnv, (Symbol.ClassSymbol)paramTypeElement, true);
/*     */     }
/* 732 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAccessible(com.sun.source.tree.Scope paramScope, Element paramElement, DeclaredType paramDeclaredType) {
/* 736 */     if (((paramScope instanceof JavacScope)) && ((paramElement instanceof Symbol)) && ((paramDeclaredType instanceof Type)))
/*     */     {
/* 739 */       Env localEnv = ((JavacScope)paramScope).env;
/* 740 */       return this.resolve.isAccessible(localEnv, (Type)paramDeclaredType, (Symbol)paramElement, true);
/*     */     }
/* 742 */     return false;
/*     */   }
/*     */ 
/*     */   private Env<AttrContext> getAttrContext(TreePath paramTreePath) {
/* 746 */     if (!(paramTreePath.getLeaf() instanceof JCTree)) {
/* 747 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 753 */     if (this.javacTaskImpl != null) {
/*     */       try {
/* 755 */         this.javacTaskImpl.enter(null);
/*     */       } catch (IOException localIOException) {
/* 757 */         throw new Error("unexpected error while entering symbols: " + localIOException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 762 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit();
/* 763 */     Copier localCopier = createCopier(this.treeMaker.forToplevel(localJCCompilationUnit));
/*     */ 
/* 765 */     Env localEnv = null;
/* 766 */     JCTree.JCMethodDecl localJCMethodDecl = null;
/* 767 */     JCTree.JCVariableDecl localJCVariableDecl = null;
/*     */ 
/* 769 */     List localList = List.nil();
/* 770 */     TreePath localTreePath = paramTreePath;
/* 771 */     while (localTreePath != null) {
/* 772 */       localList = localList.prepend(localTreePath.getLeaf());
/* 773 */       localTreePath = localTreePath.getParentPath();
/*     */     }
/*     */ 
/* 776 */     for (; localList.nonEmpty(); localList = localList.tail) {
/* 777 */       Tree localTree = (Tree)localList.head;
/*     */       Object localObject2;
/* 778 */       switch (4.$SwitchMap$com$sun$source$tree$Tree$Kind[localTree.getKind().ordinal()])
/*     */       {
/*     */       case 1:
/* 781 */         localEnv = this.enter.getTopLevelEnv((JCTree.JCCompilationUnit)localTree);
/* 782 */         break;
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 788 */         localEnv = this.enter.getClassEnv(((JCTree.JCClassDecl)localTree).sym);
/* 789 */         break;
/*     */       case 6:
/* 792 */         localJCMethodDecl = (JCTree.JCMethodDecl)localTree;
/* 793 */         localEnv = this.memberEnter.getMethodEnv(localJCMethodDecl, localEnv);
/* 794 */         break;
/*     */       case 7:
/* 797 */         localJCVariableDecl = (JCTree.JCVariableDecl)localTree;
/* 798 */         break;
/*     */       case 8:
/* 801 */         if (localJCMethodDecl != null) {
/*     */           try {
/* 803 */             Assert.check(localJCMethodDecl.body == localTree);
/* 804 */             localJCMethodDecl.body = ((JCTree.JCBlock)localCopier.copy((JCTree.JCBlock)localTree, (JCTree)paramTreePath.getLeaf()));
/* 805 */             localEnv = attribStatToTree(localJCMethodDecl.body, localEnv, localCopier.leafCopy);
/*     */           } finally {
/* 807 */             localJCMethodDecl.body = ((JCTree.JCBlock)localTree);
/*     */           }
/*     */         } else {
/* 810 */           localObject2 = (JCTree.JCBlock)localCopier.copy((JCTree.JCBlock)localTree, (JCTree)paramTreePath.getLeaf());
/* 811 */           localEnv = attribStatToTree((JCTree)localObject2, localEnv, localCopier.leafCopy);
/*     */         }
/* 813 */         return localEnv;
/*     */       default:
/* 817 */         if ((localJCVariableDecl != null) && (localJCVariableDecl.getInitializer() == localTree)) {
/* 818 */           localEnv = this.memberEnter.getInitEnv(localJCVariableDecl, localEnv);
/* 819 */           localObject2 = (JCTree.JCExpression)localCopier.copy((JCTree.JCExpression)localTree, (JCTree)paramTreePath.getLeaf());
/* 820 */           localEnv = attribExprToTree((JCTree.JCExpression)localObject2, localEnv, localCopier.leafCopy);
/* 821 */           return localEnv;
/*     */         }break;
/*     */       }
/*     */     }
/* 825 */     return localJCVariableDecl != null ? this.memberEnter.getInitEnv(localJCVariableDecl, localEnv) : localEnv;
/*     */   }
/*     */ 
/*     */   private Env<AttrContext> attribStatToTree(JCTree paramJCTree1, Env<AttrContext> paramEnv, JCTree paramJCTree2) {
/* 829 */     JavaFileObject localJavaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*     */     try {
/* 831 */       return this.attr.attribStatToTree(paramJCTree1, paramEnv, paramJCTree2);
/*     */     } finally {
/* 833 */       this.log.useSource(localJavaFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Env<AttrContext> attribExprToTree(JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv, JCTree paramJCTree) {
/* 838 */     JavaFileObject localJavaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*     */     try {
/* 840 */       return this.attr.attribExprToTree(paramJCExpression, paramEnv, paramJCTree);
/*     */     } finally {
/* 842 */       this.log.useSource(localJavaFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Copier createCopier(TreeMaker paramTreeMaker)
/*     */   {
/* 866 */     return new Copier(paramTreeMaker);
/*     */   }
/*     */ 
/*     */   public TypeMirror getOriginalType(ErrorType paramErrorType)
/*     */   {
/* 876 */     if ((paramErrorType instanceof Type.ErrorType)) {
/* 877 */       return ((Type.ErrorType)paramErrorType).getOriginalType();
/*     */     }
/*     */ 
/* 880 */     return Type.noType;
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Tree paramTree, CompilationUnitTree paramCompilationUnitTree)
/*     */   {
/* 895 */     printMessage(paramKind, paramCharSequence, ((JCTree)paramTree).pos(), paramCompilationUnitTree);
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, DocTree paramDocTree, DocCommentTree paramDocCommentTree, CompilationUnitTree paramCompilationUnitTree)
/*     */   {
/* 902 */     printMessage(paramKind, paramCharSequence, ((DCTree)paramDocTree).pos((DCTree.DCDocComment)paramDocCommentTree), paramCompilationUnitTree);
/*     */   }
/*     */ 
/*     */   private void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, CompilationUnitTree paramCompilationUnitTree)
/*     */   {
/* 908 */     JavaFileObject localJavaFileObject1 = null;
/* 909 */     JavaFileObject localJavaFileObject2 = null;
/*     */ 
/* 911 */     localJavaFileObject2 = paramCompilationUnitTree.getSourceFile();
/* 912 */     if (localJavaFileObject2 == null)
/* 913 */       paramDiagnosticPosition = null;
/*     */     else {
/* 915 */       localJavaFileObject1 = this.log.useSource(localJavaFileObject2);
/*     */     }
/*     */     try
/*     */     {
/* 919 */       switch (4.$SwitchMap$javax$tools$Diagnostic$Kind[paramKind.ordinal()]) {
/*     */       case 1:
/* 921 */         boolean bool = this.log.multipleErrors;
/*     */         try {
/* 923 */           this.log.error(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */         } finally {
/* 925 */           this.log.multipleErrors = bool;
/*     */         }
/* 927 */         break;
/*     */       case 2:
/* 930 */         this.log.warning(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/* 931 */         break;
/*     */       case 3:
/* 934 */         this.log.mandatoryWarning(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/* 935 */         break;
/*     */       default:
/* 938 */         this.log.note(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */       }
/*     */     } finally {
/* 941 */       if (localJavaFileObject1 != null)
/* 942 */         this.log.useSource(localJavaFileObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public TypeMirror getLub(CatchTree paramCatchTree)
/*     */   {
/* 948 */     JCTree.JCCatch localJCCatch = (JCTree.JCCatch)paramCatchTree;
/* 949 */     JCTree.JCVariableDecl localJCVariableDecl = localJCCatch.param;
/* 950 */     if ((localJCVariableDecl.type != null) && (localJCVariableDecl.type.getKind() == TypeKind.UNION)) {
/* 951 */       Type.UnionClassType localUnionClassType = (Type.UnionClassType)localJCVariableDecl.type;
/* 952 */       return localUnionClassType.getLub();
/*     */     }
/* 954 */     return localJCVariableDecl.type;
/*     */   }
/*     */ 
/*     */   protected static class Copier extends TreeCopier<JCTree>
/*     */   {
/* 850 */     JCTree leafCopy = null;
/*     */ 
/*     */     protected Copier(TreeMaker paramTreeMaker) {
/* 853 */       super();
/*     */     }
/*     */ 
/*     */     public <T extends JCTree> T copy(T paramT, JCTree paramJCTree)
/*     */     {
/* 858 */       JCTree localJCTree = super.copy(paramT, paramJCTree);
/* 859 */       if (paramT == paramJCTree)
/* 860 */         this.leafCopy = localJCTree;
/* 861 */       return localJCTree;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.JavacTrees
 * JD-Core Version:    0.6.2
 */