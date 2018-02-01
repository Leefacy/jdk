/*      */ package com.sun.tools.javac.tree;
/*      */ 
/*      */ import com.sun.source.tree.Tree;
/*      */ import com.sun.source.tree.Tree.Kind;
/*      */ import com.sun.source.util.TreePath;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Name.Table;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ 
/*      */ public class TreeInfo
/*      */ {
/*   53 */   protected static final Context.Key<TreeInfo> treeInfoKey = new Context.Key();
/*      */ 
/*   65 */   private Name[] opname = new Name[JCTree.Tag.getNumberOfOperators()];
/*      */   public static final int notExpression = -1;
/*      */   public static final int noPrec = 0;
/*      */   public static final int assignPrec = 1;
/*      */   public static final int assignopPrec = 2;
/*      */   public static final int condPrec = 3;
/*      */   public static final int orPrec = 4;
/*      */   public static final int andPrec = 5;
/*      */   public static final int bitorPrec = 6;
/*      */   public static final int bitxorPrec = 7;
/*      */   public static final int bitandPrec = 8;
/*      */   public static final int eqPrec = 9;
/*      */   public static final int ordPrec = 10;
/*      */   public static final int shiftPrec = 11;
/*      */   public static final int addPrec = 12;
/*      */   public static final int mulPrec = 13;
/*      */   public static final int prefixPrec = 14;
/*      */   public static final int postfixPrec = 15;
/*      */   public static final int precCount = 16;
/*      */ 
/*      */   public static TreeInfo instance(Context paramContext)
/*      */   {
/*   57 */     TreeInfo localTreeInfo = (TreeInfo)paramContext.get(treeInfoKey);
/*   58 */     if (localTreeInfo == null)
/*   59 */       localTreeInfo = new TreeInfo(paramContext);
/*   60 */     return localTreeInfo;
/*      */   }
/*      */ 
/*      */   private void setOpname(JCTree.Tag paramTag, String paramString, Names paramNames)
/*      */   {
/*   68 */     setOpname(paramTag, paramNames.fromString(paramString));
/*      */   }
/*      */   private void setOpname(JCTree.Tag paramTag, Name paramName) {
/*   71 */     this.opname[paramTag.operatorIndex()] = paramName;
/*      */   }
/*      */ 
/*      */   private TreeInfo(Context paramContext) {
/*   75 */     paramContext.put(treeInfoKey, this);
/*      */ 
/*   77 */     Names localNames = Names.instance(paramContext);
/*      */ 
/*   81 */     setOpname(JCTree.Tag.POS, "+++", localNames);
/*   82 */     setOpname(JCTree.Tag.NEG, "---", localNames);
/*   83 */     setOpname(JCTree.Tag.NOT, "!", localNames);
/*   84 */     setOpname(JCTree.Tag.COMPL, "~", localNames);
/*   85 */     setOpname(JCTree.Tag.PREINC, "++", localNames);
/*   86 */     setOpname(JCTree.Tag.PREDEC, "--", localNames);
/*   87 */     setOpname(JCTree.Tag.POSTINC, "++", localNames);
/*   88 */     setOpname(JCTree.Tag.POSTDEC, "--", localNames);
/*   89 */     setOpname(JCTree.Tag.NULLCHK, "<*nullchk*>", localNames);
/*   90 */     setOpname(JCTree.Tag.OR, "||", localNames);
/*   91 */     setOpname(JCTree.Tag.AND, "&&", localNames);
/*   92 */     setOpname(JCTree.Tag.EQ, "==", localNames);
/*   93 */     setOpname(JCTree.Tag.NE, "!=", localNames);
/*   94 */     setOpname(JCTree.Tag.LT, "<", localNames);
/*   95 */     setOpname(JCTree.Tag.GT, ">", localNames);
/*   96 */     setOpname(JCTree.Tag.LE, "<=", localNames);
/*   97 */     setOpname(JCTree.Tag.GE, ">=", localNames);
/*   98 */     setOpname(JCTree.Tag.BITOR, "|", localNames);
/*   99 */     setOpname(JCTree.Tag.BITXOR, "^", localNames);
/*  100 */     setOpname(JCTree.Tag.BITAND, "&", localNames);
/*  101 */     setOpname(JCTree.Tag.SL, "<<", localNames);
/*  102 */     setOpname(JCTree.Tag.SR, ">>", localNames);
/*  103 */     setOpname(JCTree.Tag.USR, ">>>", localNames);
/*  104 */     setOpname(JCTree.Tag.PLUS, "+", localNames);
/*  105 */     setOpname(JCTree.Tag.MINUS, localNames.hyphen);
/*  106 */     setOpname(JCTree.Tag.MUL, localNames.asterisk);
/*  107 */     setOpname(JCTree.Tag.DIV, localNames.slash);
/*  108 */     setOpname(JCTree.Tag.MOD, "%", localNames);
/*      */   }
/*      */ 
/*      */   public static List<JCTree.JCExpression> args(JCTree paramJCTree) {
/*  112 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 1:
/*  114 */       return ((JCTree.JCMethodInvocation)paramJCTree).args;
/*      */     case 2:
/*  116 */       return ((JCTree.JCNewClass)paramJCTree).args;
/*      */     }
/*  118 */     return null;
/*      */   }
/*      */ 
/*      */   public Name operatorName(JCTree.Tag paramTag)
/*      */   {
/*  125 */     return this.opname[paramTag.operatorIndex()];
/*      */   }
/*      */ 
/*      */   public static boolean isConstructor(JCTree paramJCTree)
/*      */   {
/*  131 */     if (paramJCTree.hasTag(JCTree.Tag.METHODDEF)) {
/*  132 */       Name localName = ((JCTree.JCMethodDecl)paramJCTree).name;
/*  133 */       return localName == localName.table.names.init;
/*      */     }
/*  135 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isReceiverParam(JCTree paramJCTree)
/*      */   {
/*  140 */     if (paramJCTree.hasTag(JCTree.Tag.VARDEF)) {
/*  141 */       return ((JCTree.JCVariableDecl)paramJCTree).nameexpr != null;
/*      */     }
/*  143 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean hasConstructors(List<JCTree> paramList)
/*      */   {
/*  150 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  151 */       if (isConstructor((JCTree)((List)localObject).head)) return true;
/*  152 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isMultiCatch(JCTree.JCCatch paramJCCatch) {
/*  156 */     return paramJCCatch.param.vartype.hasTag(JCTree.Tag.TYPEUNION);
/*      */   }
/*      */ 
/*      */   public static boolean isSyntheticInit(JCTree paramJCTree)
/*      */   {
/*  162 */     if (paramJCTree.hasTag(JCTree.Tag.EXEC)) {
/*  163 */       JCTree.JCExpressionStatement localJCExpressionStatement = (JCTree.JCExpressionStatement)paramJCTree;
/*  164 */       if (localJCExpressionStatement.expr.hasTag(JCTree.Tag.ASSIGN)) {
/*  165 */         JCTree.JCAssign localJCAssign = (JCTree.JCAssign)localJCExpressionStatement.expr;
/*  166 */         if (localJCAssign.lhs.hasTag(JCTree.Tag.SELECT)) {
/*  167 */           JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)localJCAssign.lhs;
/*  168 */           if ((localJCFieldAccess.sym != null) && 
/*  169 */             ((localJCFieldAccess.sym
/*  169 */             .flags() & 0x1000) != 0L)) {
/*  170 */             Name localName = name(localJCFieldAccess.selected);
/*  171 */             if ((localName != null) && (localName == localName.table.names._this))
/*  172 */               return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  177 */     return false;
/*      */   }
/*      */ 
/*      */   public static Name calledMethodName(JCTree paramJCTree)
/*      */   {
/*  183 */     if (paramJCTree.hasTag(JCTree.Tag.EXEC)) {
/*  184 */       JCTree.JCExpressionStatement localJCExpressionStatement = (JCTree.JCExpressionStatement)paramJCTree;
/*  185 */       if (localJCExpressionStatement.expr.hasTag(JCTree.Tag.APPLY)) {
/*  186 */         Name localName = name(((JCTree.JCMethodInvocation)localJCExpressionStatement.expr).meth);
/*  187 */         return localName;
/*      */       }
/*      */     }
/*  190 */     return null;
/*      */   }
/*      */ 
/*      */   public static boolean isSelfCall(JCTree paramJCTree)
/*      */   {
/*  196 */     Name localName = calledMethodName(paramJCTree);
/*  197 */     if (localName != null) {
/*  198 */       Names localNames = localName.table.names;
/*  199 */       return (localName == localNames._this) || (localName == localNames._super);
/*      */     }
/*  201 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isSuperCall(JCTree paramJCTree)
/*      */   {
/*  208 */     Name localName = calledMethodName(paramJCTree);
/*  209 */     if (localName != null) {
/*  210 */       Names localNames = localName.table.names;
/*  211 */       return localName == localNames._super;
/*      */     }
/*  213 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isInitialConstructor(JCTree paramJCTree)
/*      */   {
/*  221 */     JCTree.JCMethodInvocation localJCMethodInvocation = firstConstructorCall(paramJCTree);
/*  222 */     if (localJCMethodInvocation == null) return false;
/*  223 */     Name localName = name(localJCMethodInvocation.meth);
/*  224 */     return (localName == null) || (localName != localName.table.names._this);
/*      */   }
/*      */ 
/*      */   public static JCTree.JCMethodInvocation firstConstructorCall(JCTree paramJCTree)
/*      */   {
/*  229 */     if (!paramJCTree.hasTag(JCTree.Tag.METHODDEF)) return null;
/*  230 */     JCTree.JCMethodDecl localJCMethodDecl = (JCTree.JCMethodDecl)paramJCTree;
/*  231 */     Names localNames = localJCMethodDecl.name.table.names;
/*  232 */     if (localJCMethodDecl.name != localNames.init) return null;
/*  233 */     if (localJCMethodDecl.body == null) return null;
/*  234 */     List localList = localJCMethodDecl.body.stats;
/*      */ 
/*  236 */     while ((localList.nonEmpty()) && (isSyntheticInit((JCTree)localList.head)))
/*  237 */       localList = localList.tail;
/*  238 */     if (localList.isEmpty()) return null;
/*  239 */     if (!((JCTree.JCStatement)localList.head).hasTag(JCTree.Tag.EXEC)) return null;
/*  240 */     JCTree.JCExpressionStatement localJCExpressionStatement = (JCTree.JCExpressionStatement)localList.head;
/*  241 */     if (!localJCExpressionStatement.expr.hasTag(JCTree.Tag.APPLY)) return null;
/*  242 */     return (JCTree.JCMethodInvocation)localJCExpressionStatement.expr;
/*      */   }
/*      */ 
/*      */   public static boolean isDiamond(JCTree paramJCTree)
/*      */   {
/*  247 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) { case 3:
/*  248 */       return ((JCTree.JCTypeApply)paramJCTree).getTypeArguments().isEmpty();
/*      */     case 2:
/*  249 */       return isDiamond(((JCTree.JCNewClass)paramJCTree).clazz);
/*      */     case 4:
/*  250 */       return isDiamond(((JCTree.JCAnnotatedType)paramJCTree).underlyingType); }
/*  251 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isEnumInit(JCTree paramJCTree)
/*      */   {
/*  256 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 5:
/*  258 */       return (((JCTree.JCVariableDecl)paramJCTree).mods.flags & 0x4000) != 0L;
/*      */     }
/*  260 */     return false;
/*      */   }
/*      */ 
/*      */   public static void setPolyKind(JCTree paramJCTree, JCTree.JCPolyExpression.PolyKind paramPolyKind)
/*      */   {
/*  266 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 1:
/*  268 */       ((JCTree.JCMethodInvocation)paramJCTree).polyKind = paramPolyKind;
/*  269 */       break;
/*      */     case 2:
/*  271 */       ((JCTree.JCNewClass)paramJCTree).polyKind = paramPolyKind;
/*  272 */       break;
/*      */     case 6:
/*  274 */       ((JCTree.JCMemberReference)paramJCTree).refPolyKind = paramPolyKind;
/*  275 */       break;
/*      */     default:
/*  277 */       throw new AssertionError("Unexpected tree: " + paramJCTree);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void setVarargsElement(JCTree paramJCTree, Type paramType)
/*      */   {
/*  283 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 1:
/*  285 */       ((JCTree.JCMethodInvocation)paramJCTree).varargsElement = paramType;
/*  286 */       break;
/*      */     case 2:
/*  288 */       ((JCTree.JCNewClass)paramJCTree).varargsElement = paramType;
/*  289 */       break;
/*      */     case 6:
/*  291 */       ((JCTree.JCMemberReference)paramJCTree).varargsElement = paramType;
/*  292 */       break;
/*      */     default:
/*  294 */       throw new AssertionError("Unexpected tree: " + paramJCTree);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean isExpressionStatement(JCTree.JCExpression paramJCExpression)
/*      */   {
/*  300 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCExpression.getTag().ordinal()]) { case 1:
/*      */     case 2:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*  310 */       return true;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*  312 */     case 6: } return false;
/*      */   }
/*      */ 
/*      */   public static boolean isStaticSelector(JCTree paramJCTree, Names paramNames)
/*      */   {
/*  320 */     if (paramJCTree == null)
/*  321 */       return false;
/*  322 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 24:
/*  324 */       JCTree.JCIdent localJCIdent = (JCTree.JCIdent)paramJCTree;
/*  325 */       if ((localJCIdent.name != paramNames._this) && (localJCIdent.name != paramNames._super));
/*  327 */       return isStaticSym(paramJCTree);
/*      */     case 25:
/*  330 */       return (isStaticSym(paramJCTree)) && 
/*  330 */         (isStaticSelector(((JCTree.JCFieldAccess)paramJCTree).selected, paramNames));
/*      */     case 3:
/*      */     case 26:
/*  333 */       return true;
/*      */     case 4:
/*  335 */       return isStaticSelector(((JCTree.JCAnnotatedType)paramJCTree).underlyingType, paramNames);
/*      */     }
/*  337 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isStaticSym(JCTree paramJCTree)
/*      */   {
/*  342 */     Symbol localSymbol = symbol(paramJCTree);
/*  343 */     return (localSymbol.kind == 2) || (localSymbol.kind == 1);
/*      */   }
/*      */ 
/*      */   public static boolean isNull(JCTree paramJCTree)
/*      */   {
/*  349 */     if (!paramJCTree.hasTag(JCTree.Tag.LITERAL))
/*  350 */       return false;
/*  351 */     JCTree.JCLiteral localJCLiteral = (JCTree.JCLiteral)paramJCTree;
/*  352 */     return localJCLiteral.typetag == TypeTag.BOT;
/*      */   }
/*      */ 
/*      */   public static boolean isInAnnotation(Env<?> paramEnv, JCTree paramJCTree)
/*      */   {
/*  357 */     TreePath localTreePath = TreePath.getPath(paramEnv.toplevel, paramJCTree);
/*  358 */     if (localTreePath != null) {
/*  359 */       for (Tree localTree : localTreePath) {
/*  360 */         if (localTree.getKind() == Tree.Kind.ANNOTATION)
/*  361 */           return true;
/*      */       }
/*      */     }
/*  364 */     return false;
/*      */   }
/*      */ 
/*      */   public static String getCommentText(Env<?> paramEnv, JCTree paramJCTree) {
/*  368 */     DocCommentTable localDocCommentTable = paramJCTree.hasTag(JCTree.Tag.TOPLEVEL) ? ((JCTree.JCCompilationUnit)paramJCTree).docComments : paramEnv.toplevel.docComments;
/*      */ 
/*  371 */     return localDocCommentTable == null ? null : localDocCommentTable.getCommentText(paramJCTree);
/*      */   }
/*      */ 
/*      */   public static DCTree.DCDocComment getCommentTree(Env<?> paramEnv, JCTree paramJCTree) {
/*  375 */     DocCommentTable localDocCommentTable = paramJCTree.hasTag(JCTree.Tag.TOPLEVEL) ? ((JCTree.JCCompilationUnit)paramJCTree).docComments : paramEnv.toplevel.docComments;
/*      */ 
/*  378 */     return localDocCommentTable == null ? null : localDocCommentTable.getCommentTree(paramJCTree);
/*      */   }
/*      */ 
/*      */   public static int firstStatPos(JCTree paramJCTree)
/*      */   {
/*  385 */     if ((paramJCTree.hasTag(JCTree.Tag.BLOCK)) && (((JCTree.JCBlock)paramJCTree).stats.nonEmpty())) {
/*  386 */       return ((JCTree.JCStatement)((JCTree.JCBlock)paramJCTree).stats.head).pos;
/*      */     }
/*  388 */     return paramJCTree.pos;
/*      */   }
/*      */ 
/*      */   public static int endPos(JCTree paramJCTree)
/*      */   {
/*  395 */     if ((paramJCTree.hasTag(JCTree.Tag.BLOCK)) && (((JCTree.JCBlock)paramJCTree).endpos != -1))
/*  396 */       return ((JCTree.JCBlock)paramJCTree).endpos;
/*  397 */     if (paramJCTree.hasTag(JCTree.Tag.SYNCHRONIZED))
/*  398 */       return endPos(((JCTree.JCSynchronized)paramJCTree).body);
/*  399 */     if (paramJCTree.hasTag(JCTree.Tag.TRY)) {
/*  400 */       JCTree.JCTry localJCTry = (JCTree.JCTry)paramJCTree;
/*  401 */       return endPos(localJCTry.catchers
/*  402 */         .nonEmpty() ? ((JCTree.JCCatch)localJCTry.catchers.last()).body : localJCTry.finalizer != null ? localJCTry.finalizer : 
/*  402 */         localJCTry.body);
/*      */     }
/*  404 */     return paramJCTree.pos;
/*      */   }
/*      */ 
/*      */   public static int getStartPos(JCTree paramJCTree)
/*      */   {
/*  414 */     if (paramJCTree == null)
/*  415 */       return -1;
/*      */     Object localObject;
/*  417 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 1:
/*  419 */       return getStartPos(((JCTree.JCMethodInvocation)paramJCTree).meth);
/*      */     case 11:
/*  421 */       return getStartPos(((JCTree.JCAssign)paramJCTree).lhs);
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*  426 */       return getStartPos(((JCTree.JCAssignOp)paramJCTree).lhs);
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*  434 */       return getStartPos(((JCTree.JCBinary)paramJCTree).lhs);
/*      */     case 46:
/*  436 */       localObject = (JCTree.JCClassDecl)paramJCTree;
/*  437 */       if (((JCTree.JCClassDecl)localObject).mods.pos != -1) {
/*  438 */         return ((JCTree.JCClassDecl)localObject).mods.pos;
/*      */       }
/*      */       break;
/*      */     case 47:
/*  442 */       return getStartPos(((JCTree.JCConditional)paramJCTree).cond);
/*      */     case 48:
/*  444 */       return getStartPos(((JCTree.JCExpressionStatement)paramJCTree).expr);
/*      */     case 49:
/*  446 */       return getStartPos(((JCTree.JCArrayAccess)paramJCTree).indexed);
/*      */     case 50:
/*  448 */       localObject = (JCTree.JCMethodDecl)paramJCTree;
/*  449 */       if (((JCTree.JCMethodDecl)localObject).mods.pos != -1)
/*  450 */         return ((JCTree.JCMethodDecl)localObject).mods.pos;
/*  451 */       if (((JCTree.JCMethodDecl)localObject).typarams.nonEmpty())
/*  452 */         return getStartPos((JCTree)((JCTree.JCMethodDecl)localObject).typarams.head);
/*  453 */       return ((JCTree.JCMethodDecl)localObject).restype == null ? ((JCTree.JCMethodDecl)localObject).pos : getStartPos(((JCTree.JCMethodDecl)localObject).restype);
/*      */     case 25:
/*  456 */       return getStartPos(((JCTree.JCFieldAccess)paramJCTree).selected);
/*      */     case 3:
/*  458 */       return getStartPos(((JCTree.JCTypeApply)paramJCTree).clazz);
/*      */     case 26:
/*  460 */       return getStartPos(((JCTree.JCArrayTypeTree)paramJCTree).elemtype);
/*      */     case 51:
/*  462 */       return getStartPos(((JCTree.JCInstanceOf)paramJCTree).expr);
/*      */     case 9:
/*      */     case 10:
/*  465 */       return getStartPos(((JCTree.JCUnary)paramJCTree).arg);
/*      */     case 4:
/*  467 */       localObject = (JCTree.JCAnnotatedType)paramJCTree;
/*  468 */       if (((JCTree.JCAnnotatedType)localObject).annotations.nonEmpty()) {
/*  469 */         if ((((JCTree.JCAnnotatedType)localObject).underlyingType.hasTag(JCTree.Tag.TYPEARRAY)) || 
/*  470 */           (((JCTree.JCAnnotatedType)localObject).underlyingType
/*  470 */           .hasTag(JCTree.Tag.SELECT)))
/*      */         {
/*  471 */           return getStartPos(((JCTree.JCAnnotatedType)localObject).underlyingType);
/*      */         }
/*  473 */         return getStartPos((JCTree)((JCTree.JCAnnotatedType)localObject).annotations.head);
/*      */       }
/*      */ 
/*  476 */       return getStartPos(((JCTree.JCAnnotatedType)localObject).underlyingType);
/*      */     case 2:
/*  480 */       localObject = (JCTree.JCNewClass)paramJCTree;
/*  481 */       if (((JCTree.JCNewClass)localObject).encl != null) {
/*  482 */         return getStartPos(((JCTree.JCNewClass)localObject).encl);
/*      */       }
/*      */       break;
/*      */     case 5:
/*  486 */       localObject = (JCTree.JCVariableDecl)paramJCTree;
/*  487 */       if (((JCTree.JCVariableDecl)localObject).mods.pos != -1)
/*  488 */         return ((JCTree.JCVariableDecl)localObject).mods.pos;
/*  489 */       if (((JCTree.JCVariableDecl)localObject).vartype == null)
/*      */       {
/*  492 */         return ((JCTree.JCVariableDecl)localObject).pos;
/*      */       }
/*  494 */       return getStartPos(((JCTree.JCVariableDecl)localObject).vartype);
/*      */     case 23:
/*  498 */       localObject = (JCTree.JCErroneous)paramJCTree;
/*  499 */       if ((((JCTree.JCErroneous)localObject).errs != null) && (((JCTree.JCErroneous)localObject).errs.nonEmpty()))
/*  500 */         return getStartPos((JCTree)((JCTree.JCErroneous)localObject).errs.head); break;
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*  503 */     case 24: } return paramJCTree.pos;
/*      */   }
/*      */ 
/*      */   public static int getEndPos(JCTree paramJCTree, EndPosTable paramEndPosTable)
/*      */   {
/*  509 */     if (paramJCTree == null) {
/*  510 */       return -1;
/*      */     }
/*  512 */     if (paramEndPosTable == null)
/*      */     {
/*  514 */       return endPos(paramJCTree);
/*      */     }
/*      */ 
/*  517 */     int i = paramEndPosTable.getEndPos(paramJCTree);
/*  518 */     if (i != -1)
/*  519 */       return i;
/*      */     Object localObject;
/*  521 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) { case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*  526 */       return getEndPos(((JCTree.JCAssignOp)paramJCTree).rhs, paramEndPosTable);
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*  534 */       return getEndPos(((JCTree.JCBinary)paramJCTree).rhs, paramEndPosTable);
/*      */     case 52:
/*  536 */       return getEndPos((JCTree)((JCTree.JCCase)paramJCTree).stats.last(), paramEndPosTable);
/*      */     case 53:
/*  538 */       return getEndPos(((JCTree.JCCatch)paramJCTree).body, paramEndPosTable);
/*      */     case 47:
/*  540 */       return getEndPos(((JCTree.JCConditional)paramJCTree).falsepart, paramEndPosTable);
/*      */     case 54:
/*  542 */       return getEndPos(((JCTree.JCForLoop)paramJCTree).body, paramEndPosTable);
/*      */     case 55:
/*  544 */       return getEndPos(((JCTree.JCEnhancedForLoop)paramJCTree).body, paramEndPosTable);
/*      */     case 56:
/*  546 */       localObject = (JCTree.JCIf)paramJCTree;
/*  547 */       if (((JCTree.JCIf)localObject).elsepart == null) {
/*  548 */         return getEndPos(((JCTree.JCIf)localObject).thenpart, paramEndPosTable);
/*      */       }
/*  550 */       return getEndPos(((JCTree.JCIf)localObject).elsepart, paramEndPosTable);
/*      */     case 57:
/*  554 */       return getEndPos(((JCTree.JCLabeledStatement)paramJCTree).body, paramEndPosTable);
/*      */     case 58:
/*  556 */       return getEndPos((JCTree)((JCTree.JCModifiers)paramJCTree).annotations.last(), paramEndPosTable);
/*      */     case 59:
/*  558 */       return getEndPos(((JCTree.JCSynchronized)paramJCTree).body, paramEndPosTable);
/*      */     case 60:
/*  560 */       return getEndPos((JCTree)((JCTree.JCCompilationUnit)paramJCTree).defs.last(), paramEndPosTable);
/*      */     case 61:
/*  562 */       localObject = (JCTree.JCTry)paramJCTree;
/*  563 */       if (((JCTree.JCTry)localObject).finalizer != null)
/*  564 */         return getEndPos(((JCTree.JCTry)localObject).finalizer, paramEndPosTable);
/*  565 */       if (!((JCTree.JCTry)localObject).catchers.isEmpty()) {
/*  566 */         return getEndPos((JCTree)((JCTree.JCTry)localObject).catchers.last(), paramEndPosTable);
/*      */       }
/*  568 */       return getEndPos(((JCTree.JCTry)localObject).body, paramEndPosTable);
/*      */     case 62:
/*  572 */       return getEndPos(((JCTree.JCWildcard)paramJCTree).inner, paramEndPosTable);
/*      */     case 63:
/*  574 */       return getEndPos(((JCTree.JCTypeCast)paramJCTree).expr, paramEndPosTable);
/*      */     case 51:
/*  576 */       return getEndPos(((JCTree.JCInstanceOf)paramJCTree).clazz, paramEndPosTable);
/*      */     case 7:
/*      */     case 8:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*  583 */       return getEndPos(((JCTree.JCUnary)paramJCTree).arg, paramEndPosTable);
/*      */     case 68:
/*  585 */       return getEndPos(((JCTree.JCWhileLoop)paramJCTree).body, paramEndPosTable);
/*      */     case 4:
/*  587 */       return getEndPos(((JCTree.JCAnnotatedType)paramJCTree).underlyingType, paramEndPosTable);
/*      */     case 23:
/*  589 */       localObject = (JCTree.JCErroneous)paramJCTree;
/*  590 */       if ((((JCTree.JCErroneous)localObject).errs != null) && (((JCTree.JCErroneous)localObject).errs.nonEmpty()))
/*  591 */         return getEndPos((JCTree)((JCTree.JCErroneous)localObject).errs.last(), paramEndPosTable); break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 46:
/*      */     case 48:
/*      */     case 49:
/*  594 */     case 50: } return -1;
/*      */   }
/*      */ 
/*      */   public static JCDiagnostic.DiagnosticPosition diagEndPos(JCTree paramJCTree)
/*      */   {
/*  603 */     final int i = endPos(paramJCTree);
/*  604 */     return new JCDiagnostic.DiagnosticPosition() {
/*  605 */       public JCTree getTree() { return this.val$tree; } 
/*  606 */       public int getStartPosition() { return TreeInfo.getStartPos(this.val$tree); } 
/*  607 */       public int getPreferredPosition() { return i; } 
/*      */       public int getEndPosition(EndPosTable paramAnonymousEndPosTable) {
/*  609 */         return TreeInfo.getEndPos(this.val$tree, paramAnonymousEndPosTable);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static int finalizerPos(JCTree paramJCTree)
/*      */   {
/*  617 */     if (paramJCTree.hasTag(JCTree.Tag.TRY)) {
/*  618 */       JCTree.JCTry localJCTry = (JCTree.JCTry)paramJCTree;
/*  619 */       Assert.checkNonNull(localJCTry.finalizer);
/*  620 */       return firstStatPos(localJCTry.finalizer);
/*  621 */     }if (paramJCTree.hasTag(JCTree.Tag.SYNCHRONIZED)) {
/*  622 */       return endPos(((JCTree.JCSynchronized)paramJCTree).body);
/*      */     }
/*  624 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public static int positionFor(Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/*  631 */     JCTree localJCTree = declarationFor(paramSymbol, paramJCTree);
/*  632 */     return (localJCTree != null ? localJCTree : paramJCTree).pos;
/*      */   }
/*      */ 
/*      */   public static JCDiagnostic.DiagnosticPosition diagnosticPositionFor(Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/*  638 */     JCTree localJCTree = declarationFor(paramSymbol, paramJCTree);
/*  639 */     return (localJCTree != null ? localJCTree : paramJCTree).pos();
/*      */   }
/*      */ 
/*      */   public static JCTree declarationFor(Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/*  672 */     TreeScanner local1DeclScanner = new TreeScanner()
/*      */     {
/*  646 */       JCTree result = null;
/*      */ 
/*  648 */       public void scan(JCTree paramAnonymousJCTree) { if ((paramAnonymousJCTree != null) && (this.result == null))
/*  649 */           paramAnonymousJCTree.accept(this); }
/*      */ 
/*      */       public void visitTopLevel(JCTree.JCCompilationUnit paramAnonymousJCCompilationUnit) {
/*  652 */         if (paramAnonymousJCCompilationUnit.packge == this.val$sym) this.result = paramAnonymousJCCompilationUnit; else
/*  653 */           super.visitTopLevel(paramAnonymousJCCompilationUnit); 
/*      */       }
/*      */ 
/*  656 */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) { if (paramAnonymousJCClassDecl.sym == this.val$sym) this.result = paramAnonymousJCClassDecl; else
/*  657 */           super.visitClassDef(paramAnonymousJCClassDecl); }
/*      */ 
/*      */       public void visitMethodDef(JCTree.JCMethodDecl paramAnonymousJCMethodDecl) {
/*  660 */         if (paramAnonymousJCMethodDecl.sym == this.val$sym) this.result = paramAnonymousJCMethodDecl; else
/*  661 */           super.visitMethodDef(paramAnonymousJCMethodDecl); 
/*      */       }
/*      */ 
/*  664 */       public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl) { if (paramAnonymousJCVariableDecl.sym == this.val$sym) this.result = paramAnonymousJCVariableDecl; else
/*  665 */           super.visitVarDef(paramAnonymousJCVariableDecl); }
/*      */ 
/*      */       public void visitTypeParameter(JCTree.JCTypeParameter paramAnonymousJCTypeParameter) {
/*  668 */         if ((paramAnonymousJCTypeParameter.type != null) && (paramAnonymousJCTypeParameter.type.tsym == this.val$sym)) this.result = paramAnonymousJCTypeParameter; else
/*  669 */           super.visitTypeParameter(paramAnonymousJCTypeParameter);
/*      */       }
/*      */     };
/*  673 */     paramJCTree.accept(local1DeclScanner);
/*  674 */     return local1DeclScanner.result;
/*      */   }
/*      */ 
/*      */   public static Env<AttrContext> scopeFor(JCTree paramJCTree, JCTree.JCCompilationUnit paramJCCompilationUnit) {
/*  678 */     return scopeFor(pathFor(paramJCTree, paramJCCompilationUnit));
/*      */   }
/*      */ 
/*      */   public static Env<AttrContext> scopeFor(List<JCTree> paramList)
/*      */   {
/*  683 */     throw new UnsupportedOperationException("not implemented yet");
/*      */   }
/*      */ 
/*      */   public static List<JCTree> pathFor(JCTree paramJCTree, JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */   {
/*      */     try
/*      */     {
/*  707 */       new TreeScanner()
/*      */       {
/*  695 */         List<JCTree> path = List.nil();
/*      */ 
/*  697 */         public void scan(JCTree paramAnonymousJCTree) { if (paramAnonymousJCTree != null) {
/*  698 */             this.path = this.path.prepend(paramAnonymousJCTree);
/*  699 */             if (paramAnonymousJCTree == this.val$node)
/*  700 */               throw new TreeInfo.1Result(this.path);
/*  701 */             super.scan(paramAnonymousJCTree);
/*  702 */             this.path = this.path.tail;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  707 */       .scan(paramJCCompilationUnit);
/*      */     } catch (1Result local1Result) {
/*  709 */       return local1Result.path;
/*      */     }
/*  711 */     return List.nil();
/*      */   }
/*      */ 
/*      */   public static JCTree referencedStatement(JCTree.JCLabeledStatement paramJCLabeledStatement)
/*      */   {
/*  719 */     Object localObject = paramJCLabeledStatement;
/*      */     do localObject = ((JCTree.JCLabeledStatement)localObject).body;
/*  721 */     while (((JCTree)localObject).hasTag(JCTree.Tag.LABELLED));
/*  722 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localObject.getTag().ordinal()]) { case 54:
/*      */     case 55:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*  724 */       return localObject;
/*      */     }
/*  726 */     return paramJCLabeledStatement;
/*      */   }
/*      */ 
/*      */   public static JCTree.JCExpression skipParens(JCTree.JCExpression paramJCExpression)
/*      */   {
/*  733 */     while (paramJCExpression.hasTag(JCTree.Tag.PARENS)) {
/*  734 */       paramJCExpression = ((JCTree.JCParens)paramJCExpression).expr;
/*      */     }
/*  736 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   public static JCTree skipParens(JCTree paramJCTree)
/*      */   {
/*  742 */     if (paramJCTree.hasTag(JCTree.Tag.PARENS)) {
/*  743 */       return skipParens((JCTree.JCParens)paramJCTree);
/*      */     }
/*  745 */     return paramJCTree;
/*      */   }
/*      */ 
/*      */   public static List<Type> types(List<? extends JCTree> paramList)
/*      */   {
/*  751 */     ListBuffer localListBuffer = new ListBuffer();
/*  752 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  753 */       localListBuffer.append(((JCTree)((List)localObject).head).type);
/*  754 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public static Name name(JCTree paramJCTree)
/*      */   {
/*  761 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 24:
/*  763 */       return ((JCTree.JCIdent)paramJCTree).name;
/*      */     case 25:
/*  765 */       return ((JCTree.JCFieldAccess)paramJCTree).name;
/*      */     case 3:
/*  767 */       return name(((JCTree.JCTypeApply)paramJCTree).clazz);
/*      */     }
/*  769 */     return null;
/*      */   }
/*      */ 
/*      */   public static Name fullName(JCTree paramJCTree)
/*      */   {
/*  777 */     paramJCTree = skipParens(paramJCTree);
/*  778 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 24:
/*  780 */       return ((JCTree.JCIdent)paramJCTree).name;
/*      */     case 25:
/*  782 */       Name localName = fullName(((JCTree.JCFieldAccess)paramJCTree).selected);
/*  783 */       return localName == null ? null : localName.append('.', name(paramJCTree));
/*      */     }
/*  785 */     return null;
/*      */   }
/*      */ 
/*      */   public static Symbol symbolFor(JCTree paramJCTree)
/*      */   {
/*  790 */     Symbol localSymbol = symbolForImpl(paramJCTree);
/*      */ 
/*  792 */     return localSymbol != null ? localSymbol.baseSymbol() : null;
/*      */   }
/*      */ 
/*      */   private static Symbol symbolForImpl(JCTree paramJCTree) {
/*  796 */     paramJCTree = skipParens(paramJCTree);
/*  797 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 60:
/*  799 */       return ((JCTree.JCCompilationUnit)paramJCTree).packge;
/*      */     case 46:
/*  801 */       return ((JCTree.JCClassDecl)paramJCTree).sym;
/*      */     case 50:
/*  803 */       return ((JCTree.JCMethodDecl)paramJCTree).sym;
/*      */     case 5:
/*  805 */       return ((JCTree.JCVariableDecl)paramJCTree).sym;
/*      */     case 24:
/*  807 */       return ((JCTree.JCIdent)paramJCTree).sym;
/*      */     case 25:
/*  809 */       return ((JCTree.JCFieldAccess)paramJCTree).sym;
/*      */     case 6:
/*  811 */       return ((JCTree.JCMemberReference)paramJCTree).sym;
/*      */     case 2:
/*  813 */       return ((JCTree.JCNewClass)paramJCTree).constructor;
/*      */     case 1:
/*  815 */       return symbolFor(((JCTree.JCMethodInvocation)paramJCTree).meth);
/*      */     case 3:
/*  817 */       return symbolFor(((JCTree.JCTypeApply)paramJCTree).clazz);
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*  821 */       if (paramJCTree.type != null)
/*  822 */         return paramJCTree.type.tsym;
/*  823 */       return null;
/*      */     }
/*  825 */     return null;
/*      */   }
/*      */ 
/*      */   public static boolean isDeclaration(JCTree paramJCTree)
/*      */   {
/*  830 */     paramJCTree = skipParens(paramJCTree);
/*  831 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 5:
/*      */     case 46:
/*      */     case 50:
/*  835 */       return true;
/*      */     }
/*  837 */     return false;
/*      */   }
/*      */ 
/*      */   public static Symbol symbol(JCTree paramJCTree)
/*      */   {
/*  845 */     paramJCTree = skipParens(paramJCTree);
/*  846 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 24:
/*  848 */       return ((JCTree.JCIdent)paramJCTree).sym;
/*      */     case 25:
/*  850 */       return ((JCTree.JCFieldAccess)paramJCTree).sym;
/*      */     case 3:
/*  852 */       return symbol(((JCTree.JCTypeApply)paramJCTree).clazz);
/*      */     case 4:
/*  854 */       return symbol(((JCTree.JCAnnotatedType)paramJCTree).underlyingType);
/*      */     case 6:
/*  856 */       return ((JCTree.JCMemberReference)paramJCTree).sym;
/*      */     }
/*  858 */     return null;
/*      */   }
/*      */ 
/*      */   public static boolean nonstaticSelect(JCTree paramJCTree)
/*      */   {
/*  864 */     paramJCTree = skipParens(paramJCTree);
/*  865 */     if (!paramJCTree.hasTag(JCTree.Tag.SELECT)) return false;
/*  866 */     JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCTree;
/*  867 */     Symbol localSymbol = symbol(localJCFieldAccess.selected);
/*  868 */     return (localSymbol == null) || ((localSymbol.kind != 1) && (localSymbol.kind != 2));
/*      */   }
/*      */ 
/*      */   public static void setSymbol(JCTree paramJCTree, Symbol paramSymbol)
/*      */   {
/*  874 */     paramJCTree = skipParens(paramJCTree);
/*  875 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 24:
/*  877 */       ((JCTree.JCIdent)paramJCTree).sym = paramSymbol; break;
/*      */     case 25:
/*  879 */       ((JCTree.JCFieldAccess)paramJCTree).sym = paramSymbol; break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static long flags(JCTree paramJCTree)
/*      */   {
/*  888 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 5:
/*  890 */       return ((JCTree.JCVariableDecl)paramJCTree).mods.flags;
/*      */     case 50:
/*  892 */       return ((JCTree.JCMethodDecl)paramJCTree).mods.flags;
/*      */     case 46:
/*  894 */       return ((JCTree.JCClassDecl)paramJCTree).mods.flags;
/*      */     case 74:
/*  896 */       return ((JCTree.JCBlock)paramJCTree).flags;
/*      */     }
/*  898 */     return 0L;
/*      */   }
/*      */ 
/*      */   public static long firstFlag(long paramLong)
/*      */   {
/*  906 */     long l = 1L;
/*  907 */     while ((l & paramLong & 0xFFF) == 0L)
/*  908 */       l <<= 1;
/*  909 */     return l;
/*      */   }
/*      */ 
/*      */   public static String flagNames(long paramLong)
/*      */   {
/*  915 */     return Flags.toString(paramLong & 0xFFF).trim();
/*      */   }
/*      */ 
/*      */   public static int opPrec(JCTree.Tag paramTag)
/*      */   {
/*  944 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramTag.ordinal()]) { case 7:
/*      */     case 8:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*  950 */       return 14;
/*      */     case 9:
/*      */     case 10:
/*      */     case 75:
/*  953 */       return 15;
/*      */     case 11:
/*  954 */       return 1;
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*  965 */       return 2;
/*      */     case 27:
/*  966 */       return 4;
/*      */     case 28:
/*  967 */       return 5;
/*      */     case 32:
/*      */     case 33:
/*  969 */       return 9;
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*  973 */       return 10;
/*      */     case 29:
/*  974 */       return 6;
/*      */     case 30:
/*  975 */       return 7;
/*      */     case 31:
/*  976 */       return 8;
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*  979 */       return 11;
/*      */     case 41:
/*      */     case 42:
/*  981 */       return 12;
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*  984 */       return 13;
/*      */     case 51:
/*  985 */       return 10;
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*  986 */     case 74: } throw new AssertionError();
/*      */   }
/*      */ 
/*      */   static Tree.Kind tagToKind(JCTree.Tag paramTag)
/*      */   {
/*  991 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramTag.ordinal()])
/*      */     {
/*      */     case 9:
/*  994 */       return Tree.Kind.POSTFIX_INCREMENT;
/*      */     case 10:
/*  996 */       return Tree.Kind.POSTFIX_DECREMENT;
/*      */     case 7:
/* 1000 */       return Tree.Kind.PREFIX_INCREMENT;
/*      */     case 8:
/* 1002 */       return Tree.Kind.PREFIX_DECREMENT;
/*      */     case 64:
/* 1004 */       return Tree.Kind.UNARY_PLUS;
/*      */     case 65:
/* 1006 */       return Tree.Kind.UNARY_MINUS;
/*      */     case 67:
/* 1008 */       return Tree.Kind.BITWISE_COMPLEMENT;
/*      */     case 66:
/* 1010 */       return Tree.Kind.LOGICAL_COMPLEMENT;
/*      */     case 43:
/* 1016 */       return Tree.Kind.MULTIPLY;
/*      */     case 44:
/* 1018 */       return Tree.Kind.DIVIDE;
/*      */     case 45:
/* 1020 */       return Tree.Kind.REMAINDER;
/*      */     case 41:
/* 1024 */       return Tree.Kind.PLUS;
/*      */     case 42:
/* 1026 */       return Tree.Kind.MINUS;
/*      */     case 38:
/* 1030 */       return Tree.Kind.LEFT_SHIFT;
/*      */     case 39:
/* 1032 */       return Tree.Kind.RIGHT_SHIFT;
/*      */     case 40:
/* 1034 */       return Tree.Kind.UNSIGNED_RIGHT_SHIFT;
/*      */     case 34:
/* 1038 */       return Tree.Kind.LESS_THAN;
/*      */     case 35:
/* 1040 */       return Tree.Kind.GREATER_THAN;
/*      */     case 36:
/* 1042 */       return Tree.Kind.LESS_THAN_EQUAL;
/*      */     case 37:
/* 1044 */       return Tree.Kind.GREATER_THAN_EQUAL;
/*      */     case 32:
/* 1048 */       return Tree.Kind.EQUAL_TO;
/*      */     case 33:
/* 1050 */       return Tree.Kind.NOT_EQUAL_TO;
/*      */     case 31:
/* 1054 */       return Tree.Kind.AND;
/*      */     case 30:
/* 1056 */       return Tree.Kind.XOR;
/*      */     case 29:
/* 1058 */       return Tree.Kind.OR;
/*      */     case 28:
/* 1062 */       return Tree.Kind.CONDITIONAL_AND;
/*      */     case 27:
/* 1064 */       return Tree.Kind.CONDITIONAL_OR;
/*      */     case 20:
/* 1068 */       return Tree.Kind.MULTIPLY_ASSIGNMENT;
/*      */     case 21:
/* 1070 */       return Tree.Kind.DIVIDE_ASSIGNMENT;
/*      */     case 22:
/* 1072 */       return Tree.Kind.REMAINDER_ASSIGNMENT;
/*      */     case 18:
/* 1074 */       return Tree.Kind.PLUS_ASSIGNMENT;
/*      */     case 19:
/* 1076 */       return Tree.Kind.MINUS_ASSIGNMENT;
/*      */     case 15:
/* 1078 */       return Tree.Kind.LEFT_SHIFT_ASSIGNMENT;
/*      */     case 16:
/* 1080 */       return Tree.Kind.RIGHT_SHIFT_ASSIGNMENT;
/*      */     case 17:
/* 1082 */       return Tree.Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT;
/*      */     case 14:
/* 1084 */       return Tree.Kind.AND_ASSIGNMENT;
/*      */     case 13:
/* 1086 */       return Tree.Kind.XOR_ASSIGNMENT;
/*      */     case 12:
/* 1088 */       return Tree.Kind.OR_ASSIGNMENT;
/*      */     case 75:
/* 1092 */       return Tree.Kind.OTHER;
/*      */     case 71:
/* 1095 */       return Tree.Kind.ANNOTATION;
/*      */     case 72:
/* 1097 */       return Tree.Kind.TYPE_ANNOTATION;
/*      */     case 11:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 73:
/* 1100 */     case 74: } return null;
/*      */   }
/*      */ 
/*      */   public static JCTree.JCExpression typeIn(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1109 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCExpression.getTag().ordinal()]) {
/*      */     case 4:
/* 1111 */       return ((JCTree.JCAnnotatedType)paramJCExpression).underlyingType;
/*      */     case 3:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 62:
/*      */     case 73:
/*      */     case 76:
/* 1120 */       return paramJCExpression;
/*      */     }
/* 1122 */     throw new AssertionError("Unexpected type tree: " + paramJCExpression);
/*      */   }
/*      */ 
/*      */   public static JCTree innermostType(JCTree paramJCTree)
/*      */   {
/* 1131 */     Object localObject1 = null;
/* 1132 */     Object localObject2 = paramJCTree;
/*      */     while (true) {
/* 1134 */       switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localObject2.getTag().ordinal()]) {
/*      */       case 26:
/* 1136 */         localObject1 = null;
/* 1137 */         localObject2 = ((JCTree.JCArrayTypeTree)localObject2).elemtype;
/* 1138 */         break;
/*      */       case 62:
/* 1140 */         localObject1 = null;
/* 1141 */         localObject2 = ((JCTree.JCWildcard)localObject2).inner;
/* 1142 */         break;
/*      */       case 4:
/* 1144 */         localObject1 = localObject2;
/* 1145 */         localObject2 = ((JCTree.JCAnnotatedType)localObject2).underlyingType;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1151 */     if (localObject1 != null) {
/* 1152 */       return localObject1;
/*      */     }
/* 1154 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static boolean containsTypeAnnotation(JCTree paramJCTree)
/*      */   {
/* 1174 */     TypeAnnotationFinder localTypeAnnotationFinder = new TypeAnnotationFinder(null);
/* 1175 */     localTypeAnnotationFinder.scan(paramJCTree);
/* 1176 */     return localTypeAnnotationFinder.foundTypeAnno;
/*      */   }
/*      */ 
/*      */   private static class TypeAnnotationFinder extends TreeScanner
/*      */   {
/* 1159 */     public boolean foundTypeAnno = false;
/*      */ 
/*      */     public void scan(JCTree paramJCTree)
/*      */     {
/* 1163 */       if ((this.foundTypeAnno) || (paramJCTree == null))
/* 1164 */         return;
/* 1165 */       super.scan(paramJCTree);
/*      */     }
/*      */ 
/*      */     public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 1169 */       this.foundTypeAnno = ((this.foundTypeAnno) || (paramJCAnnotation.hasTag(JCTree.Tag.TYPE_ANNOTATION)));
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.TreeInfo
 * JD-Core Version:    0.6.2
 */