/*      */ package com.sun.tools.javac.code;
/*      */ 
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.source.tree.Tree.Kind;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Annotate.Worker;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeIntersection;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.Iterator;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.tools.JavaFileObject;
/*      */ 
/*      */ public class TypeAnnotations
/*      */ {
/*   85 */   protected static final Context.Key<TypeAnnotations> typeAnnosKey = new Context.Key();
/*      */   final Log log;
/*      */   final Names names;
/*      */   final Symtab syms;
/*      */   final Annotate annotate;
/*      */   final Attr attr;
/*      */ 
/*      */   public static TypeAnnotations instance(Context paramContext)
/*      */   {
/*   89 */     TypeAnnotations localTypeAnnotations = (TypeAnnotations)paramContext.get(typeAnnosKey);
/*   90 */     if (localTypeAnnotations == null)
/*   91 */       localTypeAnnotations = new TypeAnnotations(paramContext);
/*   92 */     return localTypeAnnotations;
/*      */   }
/*      */ 
/*      */   protected TypeAnnotations(Context paramContext)
/*      */   {
/*  102 */     paramContext.put(typeAnnosKey, this);
/*  103 */     this.names = Names.instance(paramContext);
/*  104 */     this.log = Log.instance(paramContext);
/*  105 */     this.syms = Symtab.instance(paramContext);
/*  106 */     this.annotate = Annotate.instance(paramContext);
/*  107 */     this.attr = Attr.instance(paramContext);
/*  108 */     Options localOptions = Options.instance(paramContext);
/*      */   }
/*      */ 
/*      */   public void organizeTypeAnnotationsSignatures(final Env<AttrContext> paramEnv, final JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*  121 */     this.annotate.afterRepeated(new Annotate.Worker()
/*      */     {
/*      */       public void run() {
/*  124 */         JavaFileObject localJavaFileObject = TypeAnnotations.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try
/*      */         {
/*  127 */           new TypeAnnotations.TypeAnnotationPositions(TypeAnnotations.this, true).scan(paramJCClassDecl);
/*      */ 
/*  129 */           TypeAnnotations.this.log.useSource(localJavaFileObject); } finally { TypeAnnotations.this.log.useSource(localJavaFileObject); }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void validateTypeAnnotationsSignatures(final Env<AttrContext> paramEnv, final JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*  136 */     this.annotate.validate(new Annotate.Worker()
/*      */     {
/*      */       public void run() {
/*  139 */         JavaFileObject localJavaFileObject = TypeAnnotations.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try
/*      */         {
/*  142 */           TypeAnnotations.this.attr.validateTypeAnnotations(paramJCClassDecl, true);
/*      */ 
/*  144 */           TypeAnnotations.this.log.useSource(localJavaFileObject); } finally { TypeAnnotations.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void organizeTypeAnnotationsBodies(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*  155 */     new TypeAnnotationPositions(false).scan(paramJCClassDecl);
/*      */   }
/*      */ 
/*      */   public AnnotationType annotationType(Attribute.Compound paramCompound, Symbol paramSymbol)
/*      */   {
/*  166 */     Attribute.Compound localCompound = paramCompound.type.tsym
/*  166 */       .attribute(this.syms.annotationTargetType.tsym);
/*      */ 
/*  167 */     if (localCompound == null) {
/*  168 */       return inferTargetMetaInfo(paramCompound, paramSymbol);
/*      */     }
/*  170 */     Attribute localAttribute1 = localCompound.member(this.names.value);
/*  171 */     if (!(localAttribute1 instanceof Attribute.Array)) {
/*  172 */       Assert.error("annotationType(): bad @Target argument " + localAttribute1 + " (" + localAttribute1
/*  173 */         .getClass() + ")");
/*  174 */       return AnnotationType.DECLARATION;
/*      */     }
/*  176 */     Attribute.Array localArray = (Attribute.Array)localAttribute1;
/*  177 */     int i = 0; int j = 0;
/*  178 */     for (Attribute localAttribute2 : localArray.values)
/*  179 */       if (!(localAttribute2 instanceof Attribute.Enum)) {
/*  180 */         Assert.error("annotationType(): unrecognized Attribute kind " + localAttribute2 + " (" + localAttribute2
/*  181 */           .getClass() + ")");
/*  182 */         i = 1;
/*      */       }
/*      */       else {
/*  185 */         Attribute.Enum localEnum = (Attribute.Enum)localAttribute2;
/*  186 */         if (localEnum.value.name == this.names.TYPE) {
/*  187 */           if (paramSymbol.kind == 2)
/*  188 */             i = 1;
/*  189 */         } else if (localEnum.value.name == this.names.FIELD) {
/*  190 */           if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind != 16))
/*      */           {
/*  192 */             i = 1;
/*      */           } } else if (localEnum.value.name == this.names.METHOD) {
/*  194 */           if ((paramSymbol.kind == 16) && 
/*  195 */             (!paramSymbol
/*  195 */             .isConstructor()))
/*  196 */             i = 1;
/*  197 */         } else if (localEnum.value.name == this.names.PARAMETER) {
/*  198 */           if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind == 16))
/*      */           {
/*  200 */             if ((paramSymbol
/*  200 */               .flags() & 0x0) != 0L)
/*  201 */               i = 1; 
/*      */           } } else if (localEnum.value.name == this.names.CONSTRUCTOR) {
/*  203 */           if ((paramSymbol.kind == 16) && 
/*  204 */             (paramSymbol
/*  204 */             .isConstructor()))
/*  205 */             i = 1;
/*  206 */         } else if (localEnum.value.name == this.names.LOCAL_VARIABLE) {
/*  207 */           if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind == 16))
/*      */           {
/*  209 */             if ((paramSymbol
/*  209 */               .flags() & 0x0) == 0L)
/*  210 */               i = 1; 
/*      */           } } else if (localEnum.value.name == this.names.ANNOTATION_TYPE) {
/*  212 */           if ((paramSymbol.kind == 2) && 
/*  213 */             ((paramSymbol
/*  213 */             .flags() & 0x2000) != 0L))
/*  214 */             i = 1;
/*  215 */         } else if (localEnum.value.name == this.names.PACKAGE) {
/*  216 */           if (paramSymbol.kind == 1)
/*  217 */             i = 1;
/*  218 */         } else if (localEnum.value.name == this.names.TYPE_USE) {
/*  219 */           if ((paramSymbol.kind == 2) || (paramSymbol.kind == 4) || ((paramSymbol.kind == 16) && 
/*  221 */             (!paramSymbol
/*  221 */             .isConstructor()) && 
/*  222 */             (!paramSymbol.type
/*  222 */             .getReturnType().hasTag(TypeTag.VOID))) || (
/*  222 */             (paramSymbol.kind == 16) && 
/*  223 */             (paramSymbol
/*  223 */             .isConstructor())))
/*  224 */             j = 1;
/*  225 */         } else if (localEnum.value.name != this.names.TYPE_PARAMETER)
/*      */         {
/*  231 */           Assert.error("annotationType(): unrecognized Attribute name " + localEnum.value.name + " (" + localEnum.value.name
/*  232 */             .getClass() + ")");
/*  233 */           i = 1;
/*      */         }
/*      */       }
/*  236 */     if ((i != 0) && (j != 0))
/*  237 */       return AnnotationType.BOTH;
/*  238 */     if (j != 0) {
/*  239 */       return AnnotationType.TYPE;
/*      */     }
/*  241 */     return AnnotationType.DECLARATION;
/*      */   }
/*      */ 
/*      */   private static AnnotationType inferTargetMetaInfo(Attribute.Compound paramCompound, Symbol paramSymbol)
/*      */   {
/*  249 */     return AnnotationType.DECLARATION;
/*      */   }
/*      */ 
/*      */   public static enum AnnotationType
/*      */   {
/*  158 */     DECLARATION, TYPE, BOTH;
/*      */   }
/*      */ 
/*      */   private class TypeAnnotationPositions extends TreeScanner
/*      */   {
/*      */     private final boolean sigOnly;
/*  265 */     private ListBuffer<JCTree> frames = new ListBuffer();
/*      */ 
/* 1028 */     private boolean isInClass = false;
/*      */ 
/* 1115 */     private JCTree.JCLambda currentLambda = null;
/*      */ 
/*      */     TypeAnnotationPositions(boolean arg2)
/*      */     {
/*      */       boolean bool;
/*  258 */       this.sigOnly = bool;
/*      */     }
/*      */ 
/*      */     protected void push(JCTree paramJCTree)
/*      */     {
/*  267 */       this.frames = this.frames.prepend(paramJCTree); } 
/*  268 */     protected JCTree pop() { return (JCTree)this.frames.next(); } 
/*      */     private JCTree peek2() {
/*  270 */       return (JCTree)this.frames.toList().tail.head;
/*      */     }
/*      */ 
/*      */     public void scan(JCTree paramJCTree) {
/*  274 */       push(paramJCTree);
/*  275 */       super.scan(paramJCTree);
/*  276 */       pop();
/*      */     }
/*      */ 
/*      */     private void separateAnnotationsKinds(JCTree paramJCTree, Type paramType, Symbol paramSymbol, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/*  288 */       List localList1 = paramSymbol.getRawAttributes();
/*  289 */       ListBuffer localListBuffer1 = new ListBuffer();
/*  290 */       ListBuffer localListBuffer2 = new ListBuffer();
/*  291 */       ListBuffer localListBuffer3 = new ListBuffer();
/*      */ 
/*  293 */       for (Object localObject1 = localList1.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Attribute.Compound)((Iterator)localObject1).next();
/*  294 */         switch (TypeAnnotations.3.$SwitchMap$com$sun$tools$javac$code$TypeAnnotations$AnnotationType[TypeAnnotations.this.annotationType(localObject2, paramSymbol).ordinal()]) {
/*      */         case 1:
/*  296 */           localListBuffer1.append(localObject2);
/*  297 */           break;
/*      */         case 2:
/*  299 */           localListBuffer1.append(localObject2);
/*  300 */           localObject3 = toTypeCompound((Attribute.Compound)localObject2, paramTypeAnnotationPosition);
/*  301 */           localListBuffer2.append(localObject3);
/*  302 */           break;
/*      */         case 3:
/*  305 */           localObject3 = toTypeCompound((Attribute.Compound)localObject2, paramTypeAnnotationPosition);
/*  306 */           localListBuffer2.append(localObject3);
/*      */ 
/*  308 */           localListBuffer3.append(localObject3);
/*      */         }
/*      */       }
/*      */       Object localObject2;
/*      */       Object localObject3;
/*  314 */       paramSymbol.resetAnnotations();
/*  315 */       paramSymbol.setDeclarationAttributes(localListBuffer1.toList());
/*      */ 
/*  317 */       if (localListBuffer2.isEmpty()) {
/*  318 */         return;
/*      */       }
/*      */ 
/*  321 */       localObject1 = localListBuffer2.toList();
/*      */ 
/*  323 */       if (paramType == null)
/*      */       {
/*  327 */         paramType = paramSymbol.getEnclosingElement().asType();
/*      */ 
/*  331 */         paramType = typeWithAnnotations(paramJCTree, paramType, (List)localObject1, (List)localObject1);
/*      */ 
/*  336 */         paramSymbol.appendUniqueTypeAttributes((List)localObject1);
/*  337 */         return;
/*      */       }
/*      */ 
/*  341 */       paramType = typeWithAnnotations(paramJCTree, paramType, (List)localObject1, localListBuffer3.toList());
/*      */ 
/*  343 */       if (paramSymbol.getKind() == ElementKind.METHOD) {
/*  344 */         paramSymbol.type.asMethodType().restype = paramType;
/*  345 */       } else if (paramSymbol.getKind() == ElementKind.PARAMETER) {
/*  346 */         paramSymbol.type = paramType;
/*  347 */         if (paramSymbol.getQualifiedName().equals(TypeAnnotations.this.names._this)) {
/*  348 */           paramSymbol.owner.type.asMethodType().recvtype = paramType;
/*      */         }
/*      */         else {
/*  351 */           localObject2 = paramSymbol.owner.type.asMethodType();
/*  352 */           localObject3 = ((Symbol.MethodSymbol)paramSymbol.owner).params;
/*  353 */           List localList2 = ((Type.MethodType)localObject2).argtypes;
/*  354 */           ListBuffer localListBuffer4 = new ListBuffer();
/*  355 */           while (((List)localObject3).nonEmpty()) {
/*  356 */             if (((List)localObject3).head == paramSymbol)
/*  357 */               localListBuffer4.add(paramType);
/*      */             else {
/*  359 */               localListBuffer4.add(localList2.head);
/*      */             }
/*  361 */             localList2 = localList2.tail;
/*  362 */             localObject3 = ((List)localObject3).tail;
/*      */           }
/*  364 */           ((Type.MethodType)localObject2).argtypes = localListBuffer4.toList();
/*      */         }
/*      */       } else {
/*  367 */         paramSymbol.type = paramType;
/*      */       }
/*      */ 
/*  370 */       paramSymbol.appendUniqueTypeAttributes((List)localObject1);
/*      */ 
/*  372 */       if ((paramSymbol.getKind() == ElementKind.PARAMETER) || 
/*  373 */         (paramSymbol
/*  373 */         .getKind() == ElementKind.LOCAL_VARIABLE) || 
/*  374 */         (paramSymbol
/*  374 */         .getKind() == ElementKind.RESOURCE_VARIABLE) || 
/*  375 */         (paramSymbol
/*  375 */         .getKind() == ElementKind.EXCEPTION_PARAMETER))
/*      */       {
/*  378 */         paramSymbol.owner.appendUniqueTypeAttributes(paramSymbol.getRawTypeAttributes());
/*      */       }
/*      */     }
/*      */ 
/*      */     private Type typeWithAnnotations(JCTree paramJCTree, Type paramType, List<Attribute.TypeCompound> paramList1, List<Attribute.TypeCompound> paramList2)
/*      */     {
/*  397 */       if (paramList1.isEmpty())
/*  398 */         return paramType;
/*      */       Object localObject7;
/*  400 */       if (paramType.hasTag(TypeTag.ARRAY)) {
/*  401 */         localObject1 = (Type.ArrayType)paramType.unannotatedType();
/*  402 */         localObject2 = new Type.ArrayType(null, ((Type.ArrayType)localObject1).tsym);
/*      */ 
/*  404 */         if (paramType.isAnnotated())
/*  405 */           localObject3 = ((Type.ArrayType)localObject2).annotatedType(paramType.getAnnotationMirrors());
/*      */         else {
/*  407 */           localObject3 = localObject2;
/*      */         }
/*      */ 
/*  410 */         localObject4 = arrayTypeTree(paramJCTree);
/*      */ 
/*  412 */         localObject5 = new ListBuffer();
/*  413 */         localObject5 = ((ListBuffer)localObject5).append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/*  414 */         while (((Type.ArrayType)localObject1).elemtype.hasTag(TypeTag.ARRAY)) {
/*  415 */           if (((Type.ArrayType)localObject1).elemtype.isAnnotated()) {
/*  416 */             localObject6 = ((Type.ArrayType)localObject1).elemtype;
/*  417 */             localObject1 = (Type.ArrayType)((Type)localObject6).unannotatedType();
/*  418 */             localObject7 = localObject2;
/*  419 */             localObject2 = new Type.ArrayType(null, ((Type.ArrayType)localObject1).tsym);
/*  420 */             ((Type.ArrayType)localObject7).elemtype = ((Type.ArrayType)localObject2).annotatedType(((Type.ArrayType)localObject1).elemtype.getAnnotationMirrors());
/*      */           } else {
/*  422 */             localObject1 = (Type.ArrayType)((Type.ArrayType)localObject1).elemtype;
/*  423 */             ((Type.ArrayType)localObject2).elemtype = new Type.ArrayType(null, ((Type.ArrayType)localObject1).tsym);
/*  424 */             localObject2 = (Type.ArrayType)((Type.ArrayType)localObject2).elemtype;
/*      */           }
/*  426 */           localObject4 = arrayTypeTree(((JCTree.JCArrayTypeTree)localObject4).elemtype);
/*  427 */           localObject5 = ((ListBuffer)localObject5).append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/*      */         }
/*  429 */         localObject6 = typeWithAnnotations(((JCTree.JCArrayTypeTree)localObject4).elemtype, ((Type.ArrayType)localObject1).elemtype, paramList1, paramList2);
/*  430 */         ((Type.ArrayType)localObject2).elemtype = ((Type)localObject6);
/*      */ 
/*  433 */         localObject7 = (Attribute.TypeCompound)paramList1.get(0);
/*  434 */         TypeAnnotationPosition localTypeAnnotationPosition = ((Attribute.TypeCompound)localObject7).position;
/*  435 */         localTypeAnnotationPosition.location = localTypeAnnotationPosition.location.prependList(((ListBuffer)localObject5).toList());
/*      */ 
/*  437 */         paramJCTree.type = ((Type)localObject3);
/*  438 */         return localObject3;
/*  439 */       }if (paramType.hasTag(TypeTag.TYPEVAR))
/*      */       {
/*  441 */         return paramType;
/*  442 */       }if (paramType.getKind() == TypeKind.UNION)
/*      */       {
/*  444 */         localObject1 = (JCTree.JCTypeUnion)paramJCTree;
/*  445 */         localObject2 = (JCTree.JCExpression)((JCTree.JCTypeUnion)localObject1).alternatives.get(0);
/*  446 */         localObject3 = typeWithAnnotations((JCTree)localObject2, ((JCTree.JCExpression)localObject2).type, paramList1, paramList2);
/*  447 */         ((JCTree.JCExpression)localObject2).type = ((Type)localObject3);
/*      */ 
/*  451 */         return paramType;
/*      */       }
/*  453 */       Object localObject1 = paramType;
/*  454 */       Object localObject2 = paramType.asElement();
/*  455 */       Object localObject3 = paramJCTree;
/*      */ 
/*  457 */       while ((localObject2 != null) && 
/*  458 */         (((Element)localObject2)
/*  458 */         .getKind() != ElementKind.PACKAGE) && (localObject1 != null))
/*      */       {
/*  460 */         if ((((Type)localObject1)
/*  460 */           .getKind() == TypeKind.NONE) || 
/*  461 */           (((Type)localObject1)
/*  461 */           .getKind() == TypeKind.ERROR) || (
/*  462 */           (((JCTree)localObject3)
/*  462 */           .getKind() != Tree.Kind.MEMBER_SELECT) && 
/*  463 */           (((JCTree)localObject3)
/*  463 */           .getKind() != Tree.Kind.PARAMETERIZED_TYPE) && 
/*  464 */           (((JCTree)localObject3)
/*  464 */           .getKind() != Tree.Kind.ANNOTATED_TYPE)))
/*      */         {
/*      */           break;
/*      */         }
/*  468 */         if (((JCTree)localObject3).getKind() == Tree.Kind.MEMBER_SELECT)
/*      */         {
/*  470 */           localObject1 = ((Type)localObject1).getEnclosingType();
/*  471 */           localObject2 = ((Element)localObject2).getEnclosingElement();
/*  472 */           localObject3 = ((JCTree.JCFieldAccess)localObject3).getExpression();
/*  473 */         } else if (((JCTree)localObject3).getKind() == Tree.Kind.PARAMETERIZED_TYPE) {
/*  474 */           localObject3 = ((JCTree.JCTypeApply)localObject3).getType();
/*      */         }
/*      */         else {
/*  477 */           localObject3 = ((JCTree.JCAnnotatedType)localObject3).getUnderlyingType();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  484 */       if ((localObject1 != null) && 
/*  485 */         (((Type)localObject1)
/*  485 */         .hasTag(TypeTag.NONE)))
/*      */       {
/*  486 */         switch (paramList2.size())
/*      */         {
/*      */         case 0:
/*  493 */           break;
/*      */         case 1:
/*  495 */           TypeAnnotations.this.log.error(paramJCTree.pos(), "cant.type.annotate.scoping.1", new Object[] { paramList2 });
/*      */ 
/*  497 */           break;
/*      */         default:
/*  499 */           TypeAnnotations.this.log.error(paramJCTree.pos(), "cant.type.annotate.scoping", new Object[] { paramList2 });
/*      */         }
/*      */ 
/*  502 */         return paramType;
/*      */       }
/*      */ 
/*  511 */       Object localObject4 = new ListBuffer();
/*      */ 
/*  513 */       Object localObject5 = localObject1;
/*  514 */       while ((localObject2 != null) && 
/*  515 */         (((Element)localObject2)
/*  515 */         .getKind() != ElementKind.PACKAGE) && (localObject5 != null))
/*      */       {
/*  517 */         if ((((Type)localObject5)
/*  517 */           .getKind() == TypeKind.NONE) || 
/*  518 */           (((Type)localObject5)
/*  518 */           .getKind() == TypeKind.ERROR)) break;
/*  519 */         localObject5 = ((Type)localObject5).getEnclosingType();
/*  520 */         localObject2 = ((Element)localObject2).getEnclosingElement();
/*      */ 
/*  522 */         if ((localObject5 != null) && (((Type)localObject5).getKind() != TypeKind.NONE))
/*      */         {
/*  524 */           localObject4 = ((ListBuffer)localObject4).append(TypeAnnotationPosition.TypePathEntry.INNER_TYPE);
/*      */         }
/*      */       }
/*      */ 
/*  528 */       if (((ListBuffer)localObject4).nonEmpty())
/*      */       {
/*  532 */         localObject6 = (Attribute.TypeCompound)paramList1.get(0);
/*  533 */         localObject7 = ((Attribute.TypeCompound)localObject6).position;
/*  534 */         ((TypeAnnotationPosition)localObject7).location = ((TypeAnnotationPosition)localObject7).location.appendList(((ListBuffer)localObject4).toList());
/*      */       }
/*      */ 
/*  537 */       Object localObject6 = typeWithAnnotations(paramType, (Type)localObject1, paramList1);
/*  538 */       paramJCTree.type = ((Type)localObject6);
/*  539 */       return localObject6;
/*      */     }
/*      */ 
/*      */     private JCTree.JCArrayTypeTree arrayTypeTree(JCTree paramJCTree)
/*      */     {
/*  544 */       if (paramJCTree.getKind() == Tree.Kind.ARRAY_TYPE)
/*  545 */         return (JCTree.JCArrayTypeTree)paramJCTree;
/*  546 */       if (paramJCTree.getKind() == Tree.Kind.ANNOTATED_TYPE) {
/*  547 */         return (JCTree.JCArrayTypeTree)((JCTree.JCAnnotatedType)paramJCTree).underlyingType;
/*      */       }
/*  549 */       Assert.error("Could not determine array type from type tree: " + paramJCTree);
/*  550 */       return null;
/*      */     }
/*      */ 
/*      */     private Type typeWithAnnotations(Type paramType1, final Type paramType2, List<Attribute.TypeCompound> paramList)
/*      */     {
/*  573 */       Type.Visitor local1 = new Type.Visitor()
/*      */       {
/*      */         public Type visitClassType(Type.ClassType paramAnonymousClassType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  578 */           if ((paramAnonymousClassType == paramType2) || 
/*  579 */             (paramAnonymousClassType
/*  579 */             .getEnclosingType() == Type.noType)) {
/*  580 */             return paramAnonymousClassType.annotatedType(paramAnonymousList);
/*      */           }
/*  582 */           Type.ClassType localClassType = new Type.ClassType((Type)paramAnonymousClassType.getEnclosingType().accept(this, paramAnonymousList), paramAnonymousClassType.typarams_field, paramAnonymousClassType.tsym);
/*      */ 
/*  584 */           localClassType.all_interfaces_field = paramAnonymousClassType.all_interfaces_field;
/*  585 */           localClassType.allparams_field = paramAnonymousClassType.allparams_field;
/*  586 */           localClassType.interfaces_field = paramAnonymousClassType.interfaces_field;
/*  587 */           localClassType.rank_field = paramAnonymousClassType.rank_field;
/*  588 */           localClassType.supertype_field = paramAnonymousClassType.supertype_field;
/*  589 */           return localClassType;
/*      */         }
/*      */ 
/*      */         public Type visitAnnotatedType(Type.AnnotatedType paramAnonymousAnnotatedType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  595 */           return ((Type)paramAnonymousAnnotatedType.unannotatedType().accept(this, paramAnonymousList)).annotatedType(paramAnonymousAnnotatedType.getAnnotationMirrors());
/*      */         }
/*      */ 
/*      */         public Type visitWildcardType(Type.WildcardType paramAnonymousWildcardType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  600 */           return paramAnonymousWildcardType.annotatedType(paramAnonymousList);
/*      */         }
/*      */ 
/*      */         public Type visitArrayType(Type.ArrayType paramAnonymousArrayType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  605 */           Type.ArrayType localArrayType = new Type.ArrayType((Type)paramAnonymousArrayType.elemtype.accept(this, paramAnonymousList), paramAnonymousArrayType.tsym);
/*  606 */           return localArrayType;
/*      */         }
/*      */ 
/*      */         public Type visitMethodType(Type.MethodType paramAnonymousMethodType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  612 */           return paramAnonymousMethodType;
/*      */         }
/*      */ 
/*      */         public Type visitPackageType(Type.PackageType paramAnonymousPackageType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  618 */           return paramAnonymousPackageType;
/*      */         }
/*      */ 
/*      */         public Type visitTypeVar(Type.TypeVar paramAnonymousTypeVar, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  623 */           return paramAnonymousTypeVar.annotatedType(paramAnonymousList);
/*      */         }
/*      */ 
/*      */         public Type visitCapturedType(Type.CapturedType paramAnonymousCapturedType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  628 */           return paramAnonymousCapturedType.annotatedType(paramAnonymousList);
/*      */         }
/*      */ 
/*      */         public Type visitForAll(Type.ForAll paramAnonymousForAll, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  634 */           return paramAnonymousForAll;
/*      */         }
/*      */ 
/*      */         public Type visitUndetVar(Type.UndetVar paramAnonymousUndetVar, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  640 */           return paramAnonymousUndetVar;
/*      */         }
/*      */ 
/*      */         public Type visitErrorType(Type.ErrorType paramAnonymousErrorType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  645 */           return paramAnonymousErrorType.annotatedType(paramAnonymousList);
/*      */         }
/*      */ 
/*      */         public Type visitType(Type paramAnonymousType, List<Attribute.TypeCompound> paramAnonymousList)
/*      */         {
/*  650 */           return paramAnonymousType.annotatedType(paramAnonymousList);
/*      */         }
/*      */       };
/*  654 */       return (Type)paramType1.accept(local1, paramList);
/*      */     }
/*      */ 
/*      */     private Attribute.TypeCompound toTypeCompound(Attribute.Compound paramCompound, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/*  659 */       return new Attribute.TypeCompound(paramCompound, paramTypeAnnotationPosition);
/*      */     }
/*      */ 
/*      */     private void resolveFrame(JCTree paramJCTree1, JCTree paramJCTree2, List<JCTree> paramList, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject4;
/*      */       Object localObject2;
/*      */       Object localObject3;
/*  677 */       switch (TypeAnnotations.3.$SwitchMap$com$sun$source$tree$Tree$Kind[paramJCTree2.getKind().ordinal()]) {
/*      */       case 1:
/*  679 */         JCTree.JCTypeCast localJCTypeCast = (JCTree.JCTypeCast)paramJCTree2;
/*  680 */         paramTypeAnnotationPosition.type = TargetType.CAST;
/*  681 */         if (!localJCTypeCast.clazz.hasTag(JCTree.Tag.TYPEINTERSECTION))
/*      */         {
/*  684 */           paramTypeAnnotationPosition.type_index = 0;
/*      */         }
/*  686 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  687 */         return;
/*      */       case 2:
/*  690 */         paramTypeAnnotationPosition.type = TargetType.INSTANCEOF;
/*  691 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  692 */         return;
/*      */       case 3:
/*  695 */         JCTree.JCNewClass localJCNewClass = (JCTree.JCNewClass)paramJCTree2;
/*  696 */         if (localJCNewClass.def != null)
/*      */         {
/*  698 */           localObject1 = localJCNewClass.def;
/*  699 */           if (((JCTree.JCClassDecl)localObject1).extending == paramJCTree1) {
/*  700 */             paramTypeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/*  701 */             paramTypeAnnotationPosition.type_index = -1;
/*  702 */           } else if (((JCTree.JCClassDecl)localObject1).implementing.contains(paramJCTree1)) {
/*  703 */             paramTypeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/*  704 */             paramTypeAnnotationPosition.type_index = ((JCTree.JCClassDecl)localObject1).implementing.indexOf(paramJCTree1);
/*      */           }
/*      */           else {
/*  707 */             Assert.error("Could not determine position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */           }
/*      */         }
/*  710 */         else if (localJCNewClass.typeargs.contains(paramJCTree1)) {
/*  711 */           paramTypeAnnotationPosition.type = TargetType.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT;
/*  712 */           paramTypeAnnotationPosition.type_index = localJCNewClass.typeargs.indexOf(paramJCTree1);
/*      */         } else {
/*  714 */           paramTypeAnnotationPosition.type = TargetType.NEW;
/*      */         }
/*  716 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  717 */         return;
/*      */       case 4:
/*  720 */         paramTypeAnnotationPosition.type = TargetType.NEW;
/*  721 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  722 */         return;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*  728 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  729 */         if (((JCTree.JCClassDecl)paramJCTree2).extending == paramJCTree1) {
/*  730 */           paramTypeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/*  731 */           paramTypeAnnotationPosition.type_index = -1;
/*  732 */         } else if (((JCTree.JCClassDecl)paramJCTree2).implementing.contains(paramJCTree1)) {
/*  733 */           paramTypeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/*  734 */           paramTypeAnnotationPosition.type_index = ((JCTree.JCClassDecl)paramJCTree2).implementing.indexOf(paramJCTree1);
/*  735 */         } else if (((JCTree.JCClassDecl)paramJCTree2).typarams.contains(paramJCTree1)) {
/*  736 */           paramTypeAnnotationPosition.type = TargetType.CLASS_TYPE_PARAMETER;
/*  737 */           paramTypeAnnotationPosition.parameter_index = ((JCTree.JCClassDecl)paramJCTree2).typarams.indexOf(paramJCTree1);
/*      */         } else {
/*  739 */           Assert.error("Could not determine position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */         }
/*      */ 
/*  742 */         return;
/*      */       case 9:
/*  745 */         localObject1 = (JCTree.JCMethodDecl)paramJCTree2;
/*  746 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  747 */         if (((JCTree.JCMethodDecl)localObject1).thrown.contains(paramJCTree1)) {
/*  748 */           paramTypeAnnotationPosition.type = TargetType.THROWS;
/*  749 */           paramTypeAnnotationPosition.type_index = ((JCTree.JCMethodDecl)localObject1).thrown.indexOf(paramJCTree1);
/*  750 */         } else if (((JCTree.JCMethodDecl)localObject1).restype == paramJCTree1) {
/*  751 */           paramTypeAnnotationPosition.type = TargetType.METHOD_RETURN;
/*  752 */         } else if (((JCTree.JCMethodDecl)localObject1).typarams.contains(paramJCTree1)) {
/*  753 */           paramTypeAnnotationPosition.type = TargetType.METHOD_TYPE_PARAMETER;
/*  754 */           paramTypeAnnotationPosition.parameter_index = ((JCTree.JCMethodDecl)localObject1).typarams.indexOf(paramJCTree1);
/*      */         } else {
/*  756 */           Assert.error("Could not determine position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */         }
/*      */ 
/*  759 */         return;
/*      */       case 10:
/*  763 */         localObject1 = paramList.tail;
/*      */ 
/*  765 */         if (((JCTree.JCTypeApply)paramJCTree2).clazz != paramJCTree1)
/*      */         {
/*  767 */           if (((JCTree.JCTypeApply)paramJCTree2).arguments.contains(paramJCTree1)) {
/*  768 */             JCTree.JCTypeApply localJCTypeApply = (JCTree.JCTypeApply)paramJCTree2;
/*  769 */             int j = localJCTypeApply.arguments.indexOf(paramJCTree1);
/*  770 */             paramTypeAnnotationPosition.location = paramTypeAnnotationPosition.location.prepend(new TypeAnnotationPosition.TypePathEntry(TypeAnnotationPosition.TypePathEntryKind.TYPE_ARGUMENT, j));
/*      */ 
/*  773 */             if ((((List)localObject1).tail != null) && (((JCTree)((List)localObject1).tail.head).hasTag(JCTree.Tag.NEWCLASS)))
/*      */             {
/*  776 */               localObject4 = ((JCTree)((List)localObject1).tail.head).type;
/*      */             }
/*  778 */             else localObject4 = localJCTypeApply.type;
/*      */ 
/*  781 */             locateNestedTypes((Type)localObject4, paramTypeAnnotationPosition);
/*      */           } else {
/*  783 */             Assert.error("Could not determine type argument position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */           }
/*      */         }
/*      */ 
/*  787 */         resolveFrame((JCTree)((List)localObject1).head, (JCTree)((List)localObject1).tail.head, (List)localObject1, paramTypeAnnotationPosition);
/*  788 */         return;
/*      */       case 11:
/*  792 */         localObject1 = (JCTree.JCMemberReference)paramJCTree2;
/*      */ 
/*  794 */         if (((JCTree.JCMemberReference)localObject1).expr == paramJCTree1) {
/*  795 */           switch (TypeAnnotations.3.$SwitchMap$com$sun$source$tree$MemberReferenceTree$ReferenceMode[localObject1.mode.ordinal()]) {
/*      */           case 1:
/*  797 */             paramTypeAnnotationPosition.type = TargetType.METHOD_REFERENCE;
/*  798 */             break;
/*      */           case 2:
/*  800 */             paramTypeAnnotationPosition.type = TargetType.CONSTRUCTOR_REFERENCE;
/*  801 */             break;
/*      */           default:
/*  803 */             Assert.error("Unknown method reference mode " + ((JCTree.JCMemberReference)localObject1).mode + " for tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */           }
/*      */ 
/*  806 */           paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  807 */         } else if ((((JCTree.JCMemberReference)localObject1).typeargs != null) && 
/*  808 */           (((JCTree.JCMemberReference)localObject1).typeargs
/*  808 */           .contains(paramJCTree1)))
/*      */         {
/*  809 */           int i = ((JCTree.JCMemberReference)localObject1).typeargs.indexOf(paramJCTree1);
/*  810 */           paramTypeAnnotationPosition.type_index = i;
/*  811 */           switch (TypeAnnotations.3.$SwitchMap$com$sun$source$tree$MemberReferenceTree$ReferenceMode[localObject1.mode.ordinal()]) {
/*      */           case 1:
/*  813 */             paramTypeAnnotationPosition.type = TargetType.METHOD_REFERENCE_TYPE_ARGUMENT;
/*  814 */             break;
/*      */           case 2:
/*  816 */             paramTypeAnnotationPosition.type = TargetType.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT;
/*  817 */             break;
/*      */           default:
/*  819 */             Assert.error("Unknown method reference mode " + ((JCTree.JCMemberReference)localObject1).mode + " for tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */           }
/*      */ 
/*  822 */           paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*      */         } else {
/*  824 */           Assert.error("Could not determine type argument position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */         }
/*      */ 
/*  827 */         return;
/*      */       case 12:
/*  831 */         localObject1 = new ListBuffer();
/*  832 */         localObject1 = ((ListBuffer)localObject1).append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/*  833 */         localObject2 = paramList.tail;
/*      */         while (true) {
/*  835 */           localObject3 = (JCTree)((List)localObject2).tail.head;
/*  836 */           if (((JCTree)localObject3).hasTag(JCTree.Tag.TYPEARRAY)) {
/*  837 */             localObject2 = ((List)localObject2).tail;
/*  838 */             localObject1 = ((ListBuffer)localObject1).append(TypeAnnotationPosition.TypePathEntry.ARRAY); } else {
/*  839 */             if (!((JCTree)localObject3).hasTag(JCTree.Tag.ANNOTATED_TYPE)) break;
/*  840 */             localObject2 = ((List)localObject2).tail;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  845 */         paramTypeAnnotationPosition.location = paramTypeAnnotationPosition.location.prependList(((ListBuffer)localObject1).toList());
/*  846 */         resolveFrame((JCTree)((List)localObject2).head, (JCTree)((List)localObject2).tail.head, (List)localObject2, paramTypeAnnotationPosition);
/*  847 */         return;
/*      */       case 13:
/*  851 */         if (((JCTree)paramList.tail.tail.head).hasTag(JCTree.Tag.CLASSDEF)) {
/*  852 */           localObject1 = (JCTree.JCClassDecl)paramList.tail.tail.head;
/*  853 */           paramTypeAnnotationPosition.type = TargetType.CLASS_TYPE_PARAMETER_BOUND;
/*  854 */           paramTypeAnnotationPosition.parameter_index = ((JCTree.JCClassDecl)localObject1).typarams.indexOf(paramList.tail.head);
/*  855 */           paramTypeAnnotationPosition.bound_index = ((JCTree.JCTypeParameter)paramJCTree2).bounds.indexOf(paramJCTree1);
/*  856 */           if (((JCTree.JCExpression)((JCTree.JCTypeParameter)paramJCTree2).bounds.get(0)).type.isInterface())
/*      */           {
/*  858 */             paramTypeAnnotationPosition.bound_index += 1;
/*      */           }
/*  860 */         } else if (((JCTree)paramList.tail.tail.head).hasTag(JCTree.Tag.METHODDEF)) {
/*  861 */           localObject1 = (JCTree.JCMethodDecl)paramList.tail.tail.head;
/*  862 */           paramTypeAnnotationPosition.type = TargetType.METHOD_TYPE_PARAMETER_BOUND;
/*  863 */           paramTypeAnnotationPosition.parameter_index = ((JCTree.JCMethodDecl)localObject1).typarams.indexOf(paramList.tail.head);
/*  864 */           paramTypeAnnotationPosition.bound_index = ((JCTree.JCTypeParameter)paramJCTree2).bounds.indexOf(paramJCTree1);
/*  865 */           if (((JCTree.JCExpression)((JCTree.JCTypeParameter)paramJCTree2).bounds.get(0)).type.isInterface())
/*      */           {
/*  867 */             paramTypeAnnotationPosition.bound_index += 1;
/*      */           }
/*      */         } else {
/*  870 */           Assert.error("Could not determine position of tree " + paramJCTree1 + " within frame " + paramJCTree2);
/*      */         }
/*      */ 
/*  873 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  874 */         return;
/*      */       case 14:
/*  877 */         localObject1 = ((JCTree.JCVariableDecl)paramJCTree2).sym;
/*  878 */         paramTypeAnnotationPosition.pos = paramJCTree2.pos;
/*  879 */         switch (TypeAnnotations.3.$SwitchMap$javax$lang$model$element$ElementKind[localObject1.getKind().ordinal()]) {
/*      */         case 1:
/*  881 */           paramTypeAnnotationPosition.type = TargetType.LOCAL_VARIABLE;
/*  882 */           break;
/*      */         case 2:
/*  884 */           paramTypeAnnotationPosition.type = TargetType.FIELD;
/*  885 */           break;
/*      */         case 3:
/*  887 */           if (((Symbol.VarSymbol)localObject1).getQualifiedName().equals(TypeAnnotations.this.names._this))
/*      */           {
/*  889 */             paramTypeAnnotationPosition.type = TargetType.METHOD_RECEIVER;
/*      */           } else {
/*  891 */             paramTypeAnnotationPosition.type = TargetType.METHOD_FORMAL_PARAMETER;
/*  892 */             paramTypeAnnotationPosition.parameter_index = methodParamIndex(paramList, paramJCTree2);
/*      */           }
/*  894 */           break;
/*      */         case 4:
/*  896 */           paramTypeAnnotationPosition.type = TargetType.EXCEPTION_PARAMETER;
/*  897 */           break;
/*      */         case 5:
/*  899 */           paramTypeAnnotationPosition.type = TargetType.RESOURCE_VARIABLE;
/*  900 */           break;
/*      */         default:
/*  902 */           Assert.error("Found unexpected type annotation for variable: " + localObject1 + " with kind: " + ((Symbol.VarSymbol)localObject1).getKind());
/*      */         }
/*  904 */         if (((Symbol.VarSymbol)localObject1).getKind() != ElementKind.FIELD) {
/*  905 */           ((Symbol.VarSymbol)localObject1).owner.appendUniqueTypeAttributes(((Symbol.VarSymbol)localObject1).getRawTypeAttributes());
/*      */         }
/*  907 */         return;
/*      */       case 15:
/*  910 */         if (paramJCTree2 == paramJCTree1)
/*      */         {
/*  914 */           localObject2 = (JCTree.JCAnnotatedType)paramJCTree2;
/*  915 */           localObject3 = ((JCTree.JCAnnotatedType)localObject2).underlyingType.type;
/*  916 */           if (localObject3 == null)
/*      */           {
/*  919 */             return;
/*      */           }
/*  921 */           localObject4 = ((Type)localObject3).tsym;
/*  922 */           if ((!((Symbol)localObject4).getKind().equals(ElementKind.TYPE_PARAMETER)) && 
/*  923 */             (!((Type)localObject3)
/*  923 */             .getKind().equals(TypeKind.WILDCARD)) && 
/*  924 */             (!((Type)localObject3)
/*  924 */             .getKind().equals(TypeKind.ARRAY)))
/*      */           {
/*  929 */             locateNestedTypes((Type)localObject3, paramTypeAnnotationPosition);
/*      */           }
/*      */         }
/*  932 */         localObject2 = paramList.tail;
/*  933 */         resolveFrame((JCTree)((List)localObject2).head, (JCTree)((List)localObject2).tail.head, (List)localObject2, paramTypeAnnotationPosition);
/*  934 */         return;
/*      */       case 16:
/*  938 */         localObject2 = paramList.tail;
/*  939 */         resolveFrame((JCTree)((List)localObject2).head, (JCTree)((List)localObject2).tail.head, (List)localObject2, paramTypeAnnotationPosition);
/*  940 */         return;
/*      */       case 17:
/*  944 */         localObject2 = (JCTree.JCTypeIntersection)paramJCTree2;
/*  945 */         paramTypeAnnotationPosition.type_index = ((JCTree.JCTypeIntersection)localObject2).bounds.indexOf(paramJCTree1);
/*  946 */         localObject3 = paramList.tail;
/*  947 */         resolveFrame((JCTree)((List)localObject3).head, (JCTree)((List)localObject3).tail.head, (List)localObject3, paramTypeAnnotationPosition);
/*  948 */         return;
/*      */       case 18:
/*  952 */         localObject2 = (JCTree.JCMethodInvocation)paramJCTree2;
/*  953 */         if (!((JCTree.JCMethodInvocation)localObject2).typeargs.contains(paramJCTree1)) {
/*  954 */           Assert.error("{" + paramJCTree1 + "} is not an argument in the invocation: " + localObject2);
/*      */         }
/*  956 */         localObject3 = (Symbol.MethodSymbol)TreeInfo.symbol(((JCTree.JCMethodInvocation)localObject2).getMethodSelect());
/*  957 */         if (localObject3 == null)
/*  958 */           Assert.error("could not determine symbol for {" + localObject2 + "}");
/*  959 */         else if (((Symbol.MethodSymbol)localObject3).isConstructor())
/*  960 */           paramTypeAnnotationPosition.type = TargetType.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT;
/*      */         else {
/*  962 */           paramTypeAnnotationPosition.type = TargetType.METHOD_INVOCATION_TYPE_ARGUMENT;
/*      */         }
/*  964 */         paramTypeAnnotationPosition.pos = ((JCTree.JCMethodInvocation)localObject2).pos;
/*  965 */         paramTypeAnnotationPosition.type_index = ((JCTree.JCMethodInvocation)localObject2).typeargs.indexOf(paramJCTree1);
/*  966 */         return;
/*      */       case 19:
/*      */       case 20:
/*  972 */         paramTypeAnnotationPosition.location = paramTypeAnnotationPosition.location.prepend(TypeAnnotationPosition.TypePathEntry.WILDCARD);
/*  973 */         localObject2 = paramList.tail;
/*  974 */         resolveFrame((JCTree)((List)localObject2).head, (JCTree)((List)localObject2).tail.head, (List)localObject2, paramTypeAnnotationPosition);
/*  975 */         return;
/*      */       case 21:
/*  979 */         localObject2 = paramList.tail;
/*  980 */         resolveFrame((JCTree)((List)localObject2).head, (JCTree)((List)localObject2).tail.head, (List)localObject2, paramTypeAnnotationPosition);
/*  981 */         return;
/*      */       }
/*      */ 
/*  985 */       Assert.error("Unresolved frame: " + paramJCTree2 + " of kind: " + paramJCTree2.getKind() + "\n    Looking for tree: " + paramJCTree1);
/*      */     }
/*      */ 
/*      */     private void locateNestedTypes(Type paramType, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/*  994 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  996 */       Type localType = paramType.getEnclosingType();
/*  997 */       while ((localType != null) && 
/*  998 */         (localType
/*  998 */         .getKind() != TypeKind.NONE) && 
/*  999 */         (localType
/*  999 */         .getKind() != TypeKind.ERROR)) {
/* 1000 */         localListBuffer = localListBuffer.append(TypeAnnotationPosition.TypePathEntry.INNER_TYPE);
/* 1001 */         localType = localType.getEnclosingType();
/*      */       }
/* 1003 */       if (localListBuffer.nonEmpty())
/* 1004 */         paramTypeAnnotationPosition.location = paramTypeAnnotationPosition.location.prependList(localListBuffer.toList());
/*      */     }
/*      */ 
/*      */     private int methodParamIndex(List<JCTree> paramList, JCTree paramJCTree)
/*      */     {
/* 1009 */       Object localObject1 = paramList;
/* 1010 */       while ((((JCTree)((List)localObject1).head).getTag() != JCTree.Tag.METHODDEF) && 
/* 1011 */         (((JCTree)((List)localObject1).head)
/* 1011 */         .getTag() != JCTree.Tag.LAMBDA))
/* 1012 */         localObject1 = ((List)localObject1).tail;
/*      */       Object localObject2;
/* 1014 */       if (((JCTree)((List)localObject1).head).getTag() == JCTree.Tag.METHODDEF) {
/* 1015 */         localObject2 = (JCTree.JCMethodDecl)((List)localObject1).head;
/* 1016 */         return ((JCTree.JCMethodDecl)localObject2).params.indexOf(paramJCTree);
/* 1017 */       }if (((JCTree)((List)localObject1).head).getTag() == JCTree.Tag.LAMBDA) {
/* 1018 */         localObject2 = (JCTree.JCLambda)((List)localObject1).head;
/* 1019 */         return ((JCTree.JCLambda)localObject2).params.indexOf(paramJCTree);
/*      */       }
/* 1021 */       Assert.error("methodParamIndex expected to find method or lambda for param: " + paramJCTree);
/* 1022 */       return -1;
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 1032 */       if (this.isInClass)
/* 1033 */         return;
/* 1034 */       this.isInClass = true;
/*      */ 
/* 1036 */       if (this.sigOnly) {
/* 1037 */         scan(paramJCClassDecl.mods);
/* 1038 */         scan(paramJCClassDecl.typarams);
/* 1039 */         scan(paramJCClassDecl.extending);
/* 1040 */         scan(paramJCClassDecl.implementing);
/*      */       }
/* 1042 */       scan(paramJCClassDecl.defs);
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 1051 */       if (paramJCMethodDecl.sym == null)
/* 1052 */         Assert.error("Visiting tree node before memberEnter");
/*      */       int i;
/* 1054 */       if (this.sigOnly)
/*      */       {
/*      */         TypeAnnotationPosition localTypeAnnotationPosition1;
/* 1055 */         if (!paramJCMethodDecl.mods.annotations.isEmpty())
/*      */         {
/* 1058 */           localTypeAnnotationPosition1 = new TypeAnnotationPosition();
/* 1059 */           localTypeAnnotationPosition1.type = TargetType.METHOD_RETURN;
/* 1060 */           if (paramJCMethodDecl.sym.isConstructor()) {
/* 1061 */             localTypeAnnotationPosition1.pos = paramJCMethodDecl.pos;
/*      */ 
/* 1063 */             separateAnnotationsKinds(paramJCMethodDecl, null, paramJCMethodDecl.sym, localTypeAnnotationPosition1);
/*      */           } else {
/* 1065 */             localTypeAnnotationPosition1.pos = paramJCMethodDecl.restype.pos;
/* 1066 */             separateAnnotationsKinds(paramJCMethodDecl.restype, paramJCMethodDecl.sym.type.getReturnType(), paramJCMethodDecl.sym, localTypeAnnotationPosition1);
/*      */           }
/*      */         }
/*      */ 
/* 1070 */         if ((paramJCMethodDecl.recvparam != null) && (paramJCMethodDecl.recvparam.sym != null) && 
/* 1071 */           (!paramJCMethodDecl.recvparam.mods.annotations
/* 1071 */           .isEmpty()))
/*      */         {
/* 1075 */           localTypeAnnotationPosition1 = new TypeAnnotationPosition();
/* 1076 */           localTypeAnnotationPosition1.type = TargetType.METHOD_RECEIVER;
/* 1077 */           localTypeAnnotationPosition1.pos = paramJCMethodDecl.recvparam.vartype.pos;
/* 1078 */           separateAnnotationsKinds(paramJCMethodDecl.recvparam.vartype, paramJCMethodDecl.recvparam.sym.type, paramJCMethodDecl.recvparam.sym, localTypeAnnotationPosition1);
/*      */         }
/*      */ 
/* 1081 */         i = 0;
/* 1082 */         for (JCTree.JCVariableDecl localJCVariableDecl : paramJCMethodDecl.params) {
/* 1083 */           if (!localJCVariableDecl.mods.annotations.isEmpty())
/*      */           {
/* 1086 */             TypeAnnotationPosition localTypeAnnotationPosition2 = new TypeAnnotationPosition();
/* 1087 */             localTypeAnnotationPosition2.type = TargetType.METHOD_FORMAL_PARAMETER;
/* 1088 */             localTypeAnnotationPosition2.parameter_index = i;
/* 1089 */             localTypeAnnotationPosition2.pos = localJCVariableDecl.vartype.pos;
/* 1090 */             separateAnnotationsKinds(localJCVariableDecl.vartype, localJCVariableDecl.sym.type, localJCVariableDecl.sym, localTypeAnnotationPosition2);
/*      */           }
/* 1092 */           i++;
/*      */         }
/*      */       }
/*      */ 
/* 1096 */       push(paramJCMethodDecl);
/*      */ 
/* 1098 */       if (this.sigOnly) {
/* 1099 */         scan(paramJCMethodDecl.mods);
/* 1100 */         scan(paramJCMethodDecl.restype);
/* 1101 */         scan(paramJCMethodDecl.typarams);
/* 1102 */         scan(paramJCMethodDecl.recvparam);
/* 1103 */         scan(paramJCMethodDecl.params);
/* 1104 */         scan(paramJCMethodDecl.thrown);
/*      */       } else {
/* 1106 */         scan(paramJCMethodDecl.defaultValue);
/* 1107 */         scan(paramJCMethodDecl.body);
/*      */       }
/* 1109 */       pop();
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1118 */       JCTree.JCLambda localJCLambda = this.currentLambda;
/*      */       try {
/* 1120 */         this.currentLambda = paramJCLambda;
/*      */ 
/* 1122 */         int i = 0;
/* 1123 */         for (JCTree.JCVariableDecl localJCVariableDecl : paramJCLambda.params) {
/* 1124 */           if (!localJCVariableDecl.mods.annotations.isEmpty())
/*      */           {
/* 1127 */             TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1128 */             localTypeAnnotationPosition.type = TargetType.METHOD_FORMAL_PARAMETER;
/* 1129 */             localTypeAnnotationPosition.parameter_index = i;
/* 1130 */             localTypeAnnotationPosition.pos = localJCVariableDecl.vartype.pos;
/* 1131 */             localTypeAnnotationPosition.onLambda = paramJCLambda;
/* 1132 */             separateAnnotationsKinds(localJCVariableDecl.vartype, localJCVariableDecl.sym.type, localJCVariableDecl.sym, localTypeAnnotationPosition);
/*      */           }
/* 1134 */           i++;
/*      */         }
/*      */ 
/* 1137 */         push(paramJCLambda);
/* 1138 */         scan(paramJCLambda.body);
/* 1139 */         scan(paramJCLambda.params);
/* 1140 */         pop();
/*      */       } finally {
/* 1142 */         this.currentLambda = localJCLambda;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 1152 */       if (!paramJCVariableDecl.mods.annotations.isEmpty())
/*      */       {
/* 1155 */         if (paramJCVariableDecl.sym == null) {
/* 1156 */           Assert.error("Visiting tree node before memberEnter");
/* 1157 */         } else if (paramJCVariableDecl.sym.getKind() != ElementKind.PARAMETER)
/*      */         {
/*      */           TypeAnnotationPosition localTypeAnnotationPosition;
/* 1159 */           if (paramJCVariableDecl.sym.getKind() == ElementKind.FIELD) {
/* 1160 */             if (this.sigOnly) {
/* 1161 */               localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1162 */               localTypeAnnotationPosition.type = TargetType.FIELD;
/* 1163 */               localTypeAnnotationPosition.pos = paramJCVariableDecl.pos;
/* 1164 */               separateAnnotationsKinds(paramJCVariableDecl.vartype, paramJCVariableDecl.sym.type, paramJCVariableDecl.sym, localTypeAnnotationPosition);
/*      */             }
/* 1166 */           } else if (paramJCVariableDecl.sym.getKind() == ElementKind.LOCAL_VARIABLE) {
/* 1167 */             localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1168 */             localTypeAnnotationPosition.type = TargetType.LOCAL_VARIABLE;
/* 1169 */             localTypeAnnotationPosition.pos = paramJCVariableDecl.pos;
/* 1170 */             localTypeAnnotationPosition.onLambda = this.currentLambda;
/* 1171 */             separateAnnotationsKinds(paramJCVariableDecl.vartype, paramJCVariableDecl.sym.type, paramJCVariableDecl.sym, localTypeAnnotationPosition);
/* 1172 */           } else if (paramJCVariableDecl.sym.getKind() == ElementKind.EXCEPTION_PARAMETER) {
/* 1173 */             localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1174 */             localTypeAnnotationPosition.type = TargetType.EXCEPTION_PARAMETER;
/* 1175 */             localTypeAnnotationPosition.pos = paramJCVariableDecl.pos;
/* 1176 */             localTypeAnnotationPosition.onLambda = this.currentLambda;
/* 1177 */             separateAnnotationsKinds(paramJCVariableDecl.vartype, paramJCVariableDecl.sym.type, paramJCVariableDecl.sym, localTypeAnnotationPosition);
/* 1178 */           } else if (paramJCVariableDecl.sym.getKind() == ElementKind.RESOURCE_VARIABLE) {
/* 1179 */             localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1180 */             localTypeAnnotationPosition.type = TargetType.RESOURCE_VARIABLE;
/* 1181 */             localTypeAnnotationPosition.pos = paramJCVariableDecl.pos;
/* 1182 */             localTypeAnnotationPosition.onLambda = this.currentLambda;
/* 1183 */             separateAnnotationsKinds(paramJCVariableDecl.vartype, paramJCVariableDecl.sym.type, paramJCVariableDecl.sym, localTypeAnnotationPosition);
/* 1184 */           } else if (paramJCVariableDecl.sym.getKind() != ElementKind.ENUM_CONSTANT)
/*      */           {
/* 1188 */             Assert.error("Unhandled variable kind: " + paramJCVariableDecl + " of kind: " + paramJCVariableDecl.sym.getKind());
/*      */           }
/*      */         }
/*      */       }
/* 1191 */       push(paramJCVariableDecl);
/*      */ 
/* 1193 */       scan(paramJCVariableDecl.mods);
/* 1194 */       scan(paramJCVariableDecl.vartype);
/* 1195 */       if (!this.sigOnly) {
/* 1196 */         scan(paramJCVariableDecl.init);
/*      */       }
/* 1198 */       pop();
/*      */     }
/*      */ 
/*      */     public void visitBlock(JCTree.JCBlock paramJCBlock)
/*      */     {
/* 1205 */       if (!this.sigOnly)
/* 1206 */         scan(paramJCBlock.stats);
/*      */     }
/*      */ 
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType)
/*      */     {
/* 1212 */       push(paramJCAnnotatedType);
/* 1213 */       findPosition(paramJCAnnotatedType, paramJCAnnotatedType, paramJCAnnotatedType.annotations);
/* 1214 */       pop();
/* 1215 */       super.visitAnnotatedType(paramJCAnnotatedType);
/*      */     }
/*      */ 
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter)
/*      */     {
/* 1220 */       findPosition(paramJCTypeParameter, peek2(), paramJCTypeParameter.annotations);
/* 1221 */       super.visitTypeParameter(paramJCTypeParameter);
/*      */     }
/*      */ 
/*      */     private void copyNewClassAnnotationsToOwner(JCTree.JCNewClass paramJCNewClass) {
/* 1225 */       Symbol.ClassSymbol localClassSymbol = paramJCNewClass.def.sym;
/* 1226 */       TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1227 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1230 */       for (Attribute.TypeCompound localTypeCompound : localClassSymbol.getRawTypeAttributes()) {
/* 1231 */         localListBuffer.append(new Attribute.TypeCompound(localTypeCompound.type, localTypeCompound.values, localTypeAnnotationPosition));
/*      */       }
/*      */ 
/* 1235 */       localTypeAnnotationPosition.type = TargetType.NEW;
/* 1236 */       localTypeAnnotationPosition.pos = paramJCNewClass.pos;
/* 1237 */       localClassSymbol.owner.appendUniqueTypeAttributes(localListBuffer.toList());
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 1242 */       if ((paramJCNewClass.def != null) && 
/* 1243 */         (!paramJCNewClass.def.mods.annotations
/* 1243 */         .isEmpty())) {
/* 1244 */         JCTree.JCClassDecl localJCClassDecl = paramJCNewClass.def;
/* 1245 */         TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1246 */         localTypeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/* 1247 */         localTypeAnnotationPosition.pos = paramJCNewClass.pos;
/* 1248 */         if (localJCClassDecl.extending == paramJCNewClass.clazz)
/* 1249 */           localTypeAnnotationPosition.type_index = -1;
/* 1250 */         else if (localJCClassDecl.implementing.contains(paramJCNewClass.clazz)) {
/* 1251 */           localTypeAnnotationPosition.type_index = localJCClassDecl.implementing.indexOf(paramJCNewClass.clazz);
/*      */         }
/*      */         else {
/* 1254 */           Assert.error("Could not determine position of tree " + paramJCNewClass);
/*      */         }
/* 1256 */         Type localType = localJCClassDecl.sym.type;
/* 1257 */         separateAnnotationsKinds(localJCClassDecl, paramJCNewClass.clazz.type, localJCClassDecl.sym, localTypeAnnotationPosition);
/* 1258 */         copyNewClassAnnotationsToOwner(paramJCNewClass);
/*      */ 
/* 1262 */         localJCClassDecl.sym.type = localType;
/*      */       }
/*      */ 
/* 1265 */       scan(paramJCNewClass.encl);
/* 1266 */       scan(paramJCNewClass.typeargs);
/* 1267 */       scan(paramJCNewClass.clazz);
/* 1268 */       scan(paramJCNewClass.args);
/*      */     }
/*      */ 
/*      */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray)
/*      */     {
/* 1276 */       findPosition(paramJCNewArray, paramJCNewArray, paramJCNewArray.annotations);
/* 1277 */       int i = paramJCNewArray.dimAnnotations.size();
/* 1278 */       ListBuffer localListBuffer = new ListBuffer();
/*      */       Object localObject;
/* 1281 */       for (int j = 0; j < i; j++) {
/* 1282 */         localObject = new TypeAnnotationPosition();
/* 1283 */         ((TypeAnnotationPosition)localObject).pos = paramJCNewArray.pos;
/* 1284 */         ((TypeAnnotationPosition)localObject).onLambda = this.currentLambda;
/* 1285 */         ((TypeAnnotationPosition)localObject).type = TargetType.NEW;
/* 1286 */         if (j != 0) {
/* 1287 */           localListBuffer = localListBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1288 */           ((TypeAnnotationPosition)localObject).location = ((TypeAnnotationPosition)localObject).location.appendList(localListBuffer.toList());
/*      */         }
/*      */ 
/* 1291 */         setTypeAnnotationPos((List)paramJCNewArray.dimAnnotations.get(j), (TypeAnnotationPosition)localObject);
/*      */       }
/*      */ 
/* 1297 */       JCTree.JCExpression localJCExpression = paramJCNewArray.elemtype;
/* 1298 */       localListBuffer = localListBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1299 */       while (localJCExpression != null) {
/* 1300 */         if (localJCExpression.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1301 */           localObject = (JCTree.JCAnnotatedType)localJCExpression;
/* 1302 */           TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1303 */           localTypeAnnotationPosition.type = TargetType.NEW;
/* 1304 */           localTypeAnnotationPosition.pos = paramJCNewArray.pos;
/* 1305 */           localTypeAnnotationPosition.onLambda = this.currentLambda;
/* 1306 */           locateNestedTypes(localJCExpression.type, localTypeAnnotationPosition);
/* 1307 */           localTypeAnnotationPosition.location = localTypeAnnotationPosition.location.prependList(localListBuffer.toList());
/* 1308 */           setTypeAnnotationPos(((JCTree.JCAnnotatedType)localObject).annotations, localTypeAnnotationPosition);
/* 1309 */           localJCExpression = ((JCTree.JCAnnotatedType)localObject).underlyingType;
/* 1310 */         } else if (localJCExpression.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1311 */           localListBuffer = localListBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1312 */           localJCExpression = ((JCTree.JCArrayTypeTree)localJCExpression).elemtype; } else {
/* 1313 */           if (!localJCExpression.hasTag(JCTree.Tag.SELECT)) break;
/* 1314 */           localJCExpression = ((JCTree.JCFieldAccess)localJCExpression).selected;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1319 */       scan(paramJCNewArray.elems);
/*      */     }
/*      */ 
/*      */     private void findPosition(JCTree paramJCTree1, JCTree paramJCTree2, List<JCTree.JCAnnotation> paramList) {
/* 1323 */       if (!paramList.isEmpty())
/*      */       {
/* 1329 */         TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1330 */         localTypeAnnotationPosition.onLambda = this.currentLambda;
/* 1331 */         resolveFrame(paramJCTree1, paramJCTree2, this.frames.toList(), localTypeAnnotationPosition);
/* 1332 */         setTypeAnnotationPos(paramList, localTypeAnnotationPosition);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void setTypeAnnotationPos(List<JCTree.JCAnnotation> paramList, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/* 1338 */       for (JCTree.JCAnnotation localJCAnnotation : paramList)
/*      */       {
/* 1341 */         if (localJCAnnotation.attribute != null)
/* 1342 */           ((Attribute.TypeCompound)localJCAnnotation.attribute).position = paramTypeAnnotationPosition;
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1349 */       return super.toString() + ": sigOnly: " + this.sigOnly;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.TypeAnnotations
 * JD-Core Version:    0.6.2
 */