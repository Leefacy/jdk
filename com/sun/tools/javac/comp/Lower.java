/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.RetentionPolicy;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.OperatorSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.Type.JCPrimitiveType;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.ClassWriter;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.main.Option.PkgInfo;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssert;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssignOp;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBreak;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCase;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCatch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*      */ import com.sun.tools.javac.tree.JCTree.JCContinue;
/*      */ import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.LetExpr;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ public class Lower extends TreeTranslator
/*      */ {
/*   62 */   protected static final Context.Key<Lower> lowerKey = new Context.Key();
/*      */   private Names names;
/*      */   private Log log;
/*      */   private Symtab syms;
/*      */   private Resolve rs;
/*      */   private Check chk;
/*      */   private Attr attr;
/*      */   private TreeMaker make;
/*      */   private JCDiagnostic.DiagnosticPosition make_pos;
/*      */   private ClassWriter writer;
/*      */   private ClassReader reader;
/*      */   private ConstFold cfolder;
/*      */   private Target target;
/*      */   private Source source;
/*      */   private final TypeEnvs typeEnvs;
/*      */   private boolean allowEnums;
/*      */   private final Name dollarAssertionsDisabled;
/*      */   private final Name classDollar;
/*      */   private Types types;
/*      */   private boolean debugLower;
/*      */   private Option.PkgInfo pkginfoOpt;
/*      */   Symbol.ClassSymbol currentClass;
/*      */   ListBuffer<JCTree> translated;
/*      */   Env<AttrContext> attrEnv;
/*      */   EndPosTable endPosTable;
/*      */   Map<Symbol.ClassSymbol, JCTree.JCClassDecl> classdefs;
/*  146 */   public Map<Symbol.ClassSymbol, List<JCTree>> prunedTree = new WeakHashMap();
/*      */   Map<Symbol, Symbol> actualSymbols;
/*      */   JCTree.JCMethodDecl currentMethodDef;
/*      */   Symbol.MethodSymbol currentMethodSym;
/*      */   JCTree.JCClassDecl outermostClassDef;
/*      */   JCTree outermostMemberDef;
/*  173 */   Map<Symbol, Symbol> lambdaTranslationMap = null;
/*      */ 
/*  188 */   ClassMap classMap = new ClassMap();
/*      */   Map<Symbol.ClassSymbol, List<Symbol.VarSymbol>> freevarCache;
/*  401 */   Map<Symbol.TypeSymbol, EnumMapping> enumSwitchMap = new LinkedHashMap();
/*      */ 
/*  715 */   JCTree.Visitor conflictsChecker = new TreeScanner()
/*      */   {
/*      */     Symbol.TypeSymbol currentClass;
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramAnonymousJCMethodDecl)
/*      */     {
/*  721 */       Lower.this.chk.checkConflicts(paramAnonymousJCMethodDecl.pos(), paramAnonymousJCMethodDecl.sym, this.currentClass);
/*  722 */       super.visitMethodDef(paramAnonymousJCMethodDecl);
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl)
/*      */     {
/*  727 */       if (paramAnonymousJCVariableDecl.sym.owner.kind == 2) {
/*  728 */         Lower.this.chk.checkConflicts(paramAnonymousJCVariableDecl.pos(), paramAnonymousJCVariableDecl.sym, this.currentClass);
/*      */       }
/*  730 */       super.visitVarDef(paramAnonymousJCVariableDecl);
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl)
/*      */     {
/*  735 */       Symbol.TypeSymbol localTypeSymbol = this.currentClass;
/*  736 */       this.currentClass = paramAnonymousJCClassDecl.sym;
/*      */       try {
/*  738 */         super.visitClassDef(paramAnonymousJCClassDecl);
/*      */ 
/*  741 */         this.currentClass = localTypeSymbol; } finally { this.currentClass = localTypeSymbol; }
/*      */ 
/*      */     }
/*  715 */   };
/*      */   private static final int DEREFcode = 0;
/*      */   private static final int ASSIGNcode = 2;
/*      */   private static final int PREINCcode = 4;
/*      */   private static final int PREDECcode = 6;
/*      */   private static final int POSTINCcode = 8;
/*      */   private static final int POSTDECcode = 10;
/*      */   private static final int FIRSTASGOPcode = 12;
/*  845 */   private static final int NCODES = accessCode(275) + 2;
/*      */   private Map<Symbol, Integer> accessNums;
/*      */   private Map<Symbol, Symbol.MethodSymbol[]> accessSyms;
/*      */   private Map<Symbol, Symbol.MethodSymbol> accessConstrs;
/*      */   private List<Symbol.ClassSymbol> accessConstrTags;
/*      */   private ListBuffer<Symbol> accessed;
/*      */   Scope proxies;
/*      */   Scope twrVars;
/*      */   List<Symbol.VarSymbol> outerThisStack;
/*      */   private Symbol.ClassSymbol assertionsDisabledClassCache;
/*      */   private JCTree.JCExpression enclOp;
/*      */   private Symbol.MethodSymbol systemArraycopyMethod;
/*      */ 
/*      */   public static Lower instance(Context paramContext)
/*      */   {
/*   66 */     Lower localLower = (Lower)paramContext.get(lowerKey);
/*   67 */     if (localLower == null)
/*   68 */       localLower = new Lower(paramContext);
/*   69 */     return localLower;
/*      */   }
/*      */ 
/*      */   protected Lower(Context paramContext)
/*      */   {
/*   94 */     paramContext.put(lowerKey, this);
/*   95 */     this.names = Names.instance(paramContext);
/*   96 */     this.log = Log.instance(paramContext);
/*   97 */     this.syms = Symtab.instance(paramContext);
/*   98 */     this.rs = Resolve.instance(paramContext);
/*   99 */     this.chk = Check.instance(paramContext);
/*  100 */     this.attr = Attr.instance(paramContext);
/*  101 */     this.make = TreeMaker.instance(paramContext);
/*  102 */     this.writer = ClassWriter.instance(paramContext);
/*  103 */     this.reader = ClassReader.instance(paramContext);
/*  104 */     this.cfolder = ConstFold.instance(paramContext);
/*  105 */     this.target = Target.instance(paramContext);
/*  106 */     this.source = Source.instance(paramContext);
/*  107 */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*  108 */     this.allowEnums = this.source.allowEnums();
/*  109 */     this.dollarAssertionsDisabled = this.names
/*  110 */       .fromString(this.target
/*  110 */       .syntheticNameChar() + "assertionsDisabled");
/*  111 */     this.classDollar = this.names
/*  112 */       .fromString("class" + this.target
/*  112 */       .syntheticNameChar());
/*      */ 
/*  114 */     this.types = Types.instance(paramContext);
/*  115 */     Options localOptions = Options.instance(paramContext);
/*  116 */     this.debugLower = localOptions.isSet("debuglower");
/*  117 */     this.pkginfoOpt = Option.PkgInfo.get(localOptions);
/*      */   }
/*      */ 
/*      */   JCTree.JCClassDecl classDef(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  195 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)this.classdefs.get(paramClassSymbol);
/*  196 */     if ((localJCClassDecl == null) && (this.outermostMemberDef != null))
/*      */     {
/*  199 */       this.classMap.scan(this.outermostMemberDef);
/*  200 */       localJCClassDecl = (JCTree.JCClassDecl)this.classdefs.get(paramClassSymbol);
/*      */     }
/*  202 */     if (localJCClassDecl == null)
/*      */     {
/*  205 */       this.classMap.scan(this.outermostClassDef);
/*  206 */       localJCClassDecl = (JCTree.JCClassDecl)this.classdefs.get(paramClassSymbol);
/*      */     }
/*  208 */     return localJCClassDecl;
/*      */   }
/*      */ 
/*      */   Symbol.ClassSymbol ownerToCopyFreeVarsFrom(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  361 */     if (!paramClassSymbol.isLocal()) {
/*  362 */       return null;
/*      */     }
/*  364 */     Symbol localSymbol = paramClassSymbol.owner;
/*  365 */     while (((localSymbol.owner.kind & 0x2) != 0) && (localSymbol.isLocal())) {
/*  366 */       localSymbol = localSymbol.owner;
/*      */     }
/*  368 */     if (((localSymbol.owner.kind & 0x14) != 0) && (paramClassSymbol.isSubClass(localSymbol, this.types))) {
/*  369 */       return (Symbol.ClassSymbol)localSymbol;
/*      */     }
/*  371 */     return null;
/*      */   }
/*      */ 
/*      */   List<Symbol.VarSymbol> freevars(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  379 */     List localList = (List)this.freevarCache.get(paramClassSymbol);
/*  380 */     if (localList != null) {
/*  381 */       return localList;
/*      */     }
/*  383 */     if ((paramClassSymbol.owner.kind & 0x14) != 0) {
/*  384 */       localObject = new FreeVarCollector(paramClassSymbol);
/*  385 */       ((FreeVarCollector)localObject).scan(classDef(paramClassSymbol));
/*  386 */       localList = ((FreeVarCollector)localObject).fvs;
/*  387 */       this.freevarCache.put(paramClassSymbol, localList);
/*  388 */       return localList;
/*      */     }
/*  390 */     Object localObject = ownerToCopyFreeVarsFrom(paramClassSymbol);
/*  391 */     if (localObject != null) {
/*  392 */       localList = (List)this.freevarCache.get(localObject);
/*  393 */       this.freevarCache.put(paramClassSymbol, localList);
/*  394 */       return localList;
/*      */     }
/*  396 */     return List.nil();
/*      */   }
/*      */ 
/*      */   EnumMapping mapForEnum(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol)
/*      */   {
/*  404 */     EnumMapping localEnumMapping = (EnumMapping)this.enumSwitchMap.get(paramTypeSymbol);
/*  405 */     if (localEnumMapping == null)
/*  406 */       this.enumSwitchMap.put(paramTypeSymbol, localEnumMapping = new EnumMapping(paramDiagnosticPosition, paramTypeSymbol));
/*  407 */     return localEnumMapping;
/*      */   }
/*      */ 
/*      */   TreeMaker make_at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/*  542 */     this.make_pos = paramDiagnosticPosition;
/*  543 */     return this.make.at(paramDiagnosticPosition);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeLit(Type paramType, Object paramObject)
/*      */   {
/*  553 */     return this.make.Literal(paramType.getTag(), paramObject).setType(paramType.constType(paramObject));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeNull()
/*      */   {
/*  559 */     return makeLit(this.syms.botType, null);
/*      */   }
/*      */ 
/*      */   JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList)
/*      */   {
/*  567 */     JCTree.JCNewClass localJCNewClass = this.make.NewClass(null, null, this.make
/*  568 */       .QualIdent(paramType.tsym), 
/*  568 */       paramList, null);
/*  569 */     localJCNewClass.constructor = this.rs.resolveConstructor(this.make_pos, this.attrEnv, paramType, 
/*  570 */       TreeInfo.types(paramList), 
/*  570 */       List.nil());
/*  571 */     localJCNewClass.type = paramType;
/*  572 */     return localJCNewClass;
/*      */   }
/*      */ 
/*      */   JCTree.JCUnary makeUnary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression)
/*      */   {
/*  580 */     JCTree.JCUnary localJCUnary = this.make.Unary(paramTag, paramJCExpression);
/*  581 */     localJCUnary.operator = this.rs.resolveUnaryOperator(this.make_pos, paramTag, this.attrEnv, paramJCExpression.type);
/*      */ 
/*  583 */     localJCUnary.type = localJCUnary.operator.type.getReturnType();
/*  584 */     return localJCUnary;
/*      */   }
/*      */ 
/*      */   JCTree.JCBinary makeBinary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */   {
/*  593 */     JCTree.JCBinary localJCBinary = this.make.Binary(paramTag, paramJCExpression1, paramJCExpression2);
/*  594 */     localJCBinary.operator = this.rs.resolveBinaryOperator(this.make_pos, paramTag, this.attrEnv, paramJCExpression1.type, paramJCExpression2.type);
/*      */ 
/*  596 */     localJCBinary.type = localJCBinary.operator.type.getReturnType();
/*  597 */     return localJCBinary;
/*      */   }
/*      */ 
/*      */   JCTree.JCAssignOp makeAssignop(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2)
/*      */   {
/*  606 */     JCTree.JCAssignOp localJCAssignOp = this.make.Assignop(paramTag, paramJCTree1, paramJCTree2);
/*  607 */     localJCAssignOp.operator = this.rs.resolveBinaryOperator(this.make_pos, localJCAssignOp
/*  608 */       .getTag().noAssignOp(), this.attrEnv, paramJCTree1.type, paramJCTree2.type);
/*  609 */     localJCAssignOp.type = paramJCTree1.type;
/*  610 */     return localJCAssignOp;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeString(JCTree.JCExpression paramJCExpression)
/*      */   {
/*  617 */     if (!paramJCExpression.type.isPrimitiveOrVoid()) {
/*  618 */       return paramJCExpression;
/*      */     }
/*  620 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramJCExpression.pos(), this.names.valueOf, this.syms.stringType, 
/*  623 */       List.of(paramJCExpression.type));
/*      */ 
/*  624 */     return this.make.App(this.make.QualIdent(localMethodSymbol), List.of(paramJCExpression));
/*      */   }
/*      */ 
/*      */   JCTree.JCClassDecl makeEmptyClass(long paramLong, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  635 */     return makeEmptyClass(paramLong, paramClassSymbol, null, true);
/*      */   }
/*      */ 
/*      */   JCTree.JCClassDecl makeEmptyClass(long paramLong, Symbol.ClassSymbol paramClassSymbol, Name paramName, boolean paramBoolean)
/*      */   {
/*  641 */     Symbol.ClassSymbol localClassSymbol = this.reader.defineClass(this.names.empty, paramClassSymbol);
/*  642 */     if (paramName != null)
/*  643 */       localClassSymbol.flatname = paramName;
/*      */     else {
/*  645 */       localClassSymbol.flatname = this.chk.localClassName(localClassSymbol);
/*      */     }
/*  647 */     localClassSymbol.sourcefile = paramClassSymbol.sourcefile;
/*  648 */     localClassSymbol.completer = null;
/*  649 */     localClassSymbol.members_field = new Scope(localClassSymbol);
/*  650 */     localClassSymbol.flags_field = paramLong;
/*  651 */     Type.ClassType localClassType = (Type.ClassType)localClassSymbol.type;
/*  652 */     localClassType.supertype_field = this.syms.objectType;
/*  653 */     localClassType.interfaces_field = List.nil();
/*      */ 
/*  655 */     JCTree.JCClassDecl localJCClassDecl1 = classDef(paramClassSymbol);
/*      */ 
/*  658 */     enterSynthetic(localJCClassDecl1.pos(), localClassSymbol, paramClassSymbol.members());
/*  659 */     this.chk.compiled.put(localClassSymbol.flatname, localClassSymbol);
/*      */ 
/*  662 */     JCTree.JCClassDecl localJCClassDecl2 = this.make.ClassDef(this.make
/*  663 */       .Modifiers(paramLong), 
/*  663 */       this.names.empty, 
/*  664 */       List.nil(), null, 
/*  665 */       List.nil(), List.nil());
/*  666 */     localJCClassDecl2.sym = localClassSymbol;
/*  667 */     localJCClassDecl2.type = localClassSymbol.type;
/*      */ 
/*  670 */     if (paramBoolean) localJCClassDecl1.defs = localJCClassDecl1.defs.prepend(localJCClassDecl2);
/*  671 */     return localJCClassDecl2;
/*      */   }
/*      */ 
/*      */   private void enterSynthetic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope)
/*      */   {
/*  684 */     paramScope.enter(paramSymbol);
/*      */   }
/*      */ 
/*      */   private Name makeSyntheticName(Name paramName, Scope paramScope)
/*      */   {
/*      */     do {
/*  697 */       paramName = paramName.append(this.target
/*  698 */         .syntheticNameChar(), this.names.empty);
/*      */     }
/*  700 */     while (lookupSynthetic(paramName, paramScope) != null);
/*  701 */     return paramName;
/*      */   }
/*      */ 
/*      */   void checkConflicts(List<JCTree> paramList)
/*      */   {
/*  710 */     for (JCTree localJCTree : paramList)
/*  711 */       localJCTree.accept(this.conflictsChecker);
/*      */   }
/*      */ 
/*      */   private Symbol lookupSynthetic(Name paramName, Scope paramScope)
/*      */   {
/*  751 */     Symbol localSymbol = paramScope.lookup(paramName).sym;
/*  752 */     return (localSymbol == null) || ((localSymbol.flags() & 0x1000) == 0L) ? null : localSymbol;
/*      */   }
/*      */ 
/*      */   private Symbol.MethodSymbol lookupMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Name paramName, Type paramType, List<Type> paramList)
/*      */   {
/*  758 */     return this.rs.resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName, paramList, List.nil());
/*      */   }
/*      */ 
/*      */   private Symbol.MethodSymbol lookupConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type> paramList)
/*      */   {
/*  764 */     return this.rs.resolveInternalConstructor(paramDiagnosticPosition, this.attrEnv, paramType, paramList, null);
/*      */   }
/*      */ 
/*      */   private Symbol.VarSymbol lookupField(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName)
/*      */   {
/*  770 */     return this.rs.resolveInternalField(paramDiagnosticPosition, this.attrEnv, paramType, paramName);
/*      */   }
/*      */ 
/*      */   private void checkAccessConstructorTags()
/*      */   {
/*  781 */     for (List localList = this.accessConstrTags; localList.nonEmpty(); localList = localList.tail) {
/*  782 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localList.head;
/*  783 */       if (!isTranslatedClassAvailable(localClassSymbol))
/*      */       {
/*  786 */         JCTree.JCClassDecl localJCClassDecl = makeEmptyClass(4104L, localClassSymbol
/*  787 */           .outermostClass(), localClassSymbol.flatname, false);
/*  788 */         swapAccessConstructorTag(localClassSymbol, localJCClassDecl.sym);
/*  789 */         this.translated.append(localJCClassDecl);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  794 */   private boolean isTranslatedClassAvailable(Symbol.ClassSymbol paramClassSymbol) { for (JCTree localJCTree : this.translated) {
/*  795 */       if ((localJCTree.hasTag(JCTree.Tag.CLASSDEF)) && (((JCTree.JCClassDecl)localJCTree).sym == paramClassSymbol))
/*      */       {
/*  797 */         return true;
/*      */       }
/*      */     }
/*  800 */     return false; }
/*      */ 
/*      */   void swapAccessConstructorTag(Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2)
/*      */   {
/*  804 */     for (Symbol.MethodSymbol localMethodSymbol : this.accessConstrs.values()) {
/*  805 */       Assert.check(localMethodSymbol.type.hasTag(TypeTag.METHOD));
/*  806 */       Type.MethodType localMethodType = (Type.MethodType)localMethodSymbol.type;
/*      */ 
/*  808 */       if (((Type)localMethodType.argtypes.head).tsym == paramClassSymbol1)
/*  809 */         localMethodSymbol.type = this.types
/*  810 */           .createMethodTypeWithParameters(localMethodType, localMethodType
/*  811 */           .getParameterTypes().tail
/*  812 */           .prepend(paramClassSymbol2
/*  812 */           .erasure(this.types)));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int accessCode(int paramInt)
/*      */   {
/*  872 */     if ((96 <= paramInt) && (paramInt <= 131))
/*  873 */       return (paramInt - 96) * 2 + 12;
/*  874 */     if (paramInt == 256)
/*  875 */       return 84;
/*  876 */     if ((270 <= paramInt) && (paramInt <= 275)) {
/*  877 */       return (paramInt - 270 + 131 + 2 - 96) * 2 + 12;
/*      */     }
/*  879 */     return -1;
/*      */   }
/*      */ 
/*      */   private static int accessCode(JCTree paramJCTree1, JCTree paramJCTree2)
/*      */   {
/*  888 */     if (paramJCTree2 == null)
/*  889 */       return 0;
/*  890 */     if ((paramJCTree2.hasTag(JCTree.Tag.ASSIGN)) && 
/*  891 */       (paramJCTree1 == 
/*  891 */       TreeInfo.skipParens(((JCTree.JCAssign)paramJCTree2).lhs)))
/*      */     {
/*  892 */       return 2;
/*  893 */     }if ((paramJCTree2.getTag().isIncOrDecUnaryOp()) && 
/*  894 */       (paramJCTree1 == 
/*  894 */       TreeInfo.skipParens(((JCTree.JCUnary)paramJCTree2).arg)))
/*      */     {
/*  895 */       return mapTagToUnaryOpCode(paramJCTree2.getTag());
/*  896 */     }if ((paramJCTree2.getTag().isAssignop()) && 
/*  897 */       (paramJCTree1 == 
/*  897 */       TreeInfo.skipParens(((JCTree.JCAssignOp)paramJCTree2).lhs)))
/*      */     {
/*  898 */       return accessCode(((Symbol.OperatorSymbol)((JCTree.JCAssignOp)paramJCTree2).operator).opcode);
/*      */     }
/*  900 */     return 0;
/*      */   }
/*      */ 
/*      */   private Symbol.OperatorSymbol binaryAccessOperator(int paramInt)
/*      */   {
/*  906 */     for (Scope.Entry localEntry = this.syms.predefClass.members().elems; 
/*  907 */       localEntry != null; 
/*  908 */       localEntry = localEntry.sibling) {
/*  909 */       if ((localEntry.sym instanceof Symbol.OperatorSymbol)) {
/*  910 */         Symbol.OperatorSymbol localOperatorSymbol = (Symbol.OperatorSymbol)localEntry.sym;
/*  911 */         if (accessCode(localOperatorSymbol.opcode) == paramInt) return localOperatorSymbol;
/*      */       }
/*      */     }
/*  914 */     return null;
/*      */   }
/*      */ 
/*      */   private static JCTree.Tag treeTag(Symbol.OperatorSymbol paramOperatorSymbol)
/*      */   {
/*  921 */     switch (paramOperatorSymbol.opcode) { case 128:
/*      */     case 129:
/*  923 */       return JCTree.Tag.BITOR_ASG;
/*      */     case 130:
/*      */     case 131:
/*  925 */       return JCTree.Tag.BITXOR_ASG;
/*      */     case 126:
/*      */     case 127:
/*  927 */       return JCTree.Tag.BITAND_ASG;
/*      */     case 120:
/*      */     case 121:
/*      */     case 270:
/*      */     case 271:
/*  930 */       return JCTree.Tag.SL_ASG;
/*      */     case 122:
/*      */     case 123:
/*      */     case 272:
/*      */     case 273:
/*  933 */       return JCTree.Tag.SR_ASG;
/*      */     case 124:
/*      */     case 125:
/*      */     case 274:
/*      */     case 275:
/*  936 */       return JCTree.Tag.USR_ASG;
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 256:
/*  940 */       return JCTree.Tag.PLUS_ASG;
/*      */     case 100:
/*      */     case 101:
/*      */     case 102:
/*      */     case 103:
/*  943 */       return JCTree.Tag.MINUS_ASG;
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*  946 */       return JCTree.Tag.MUL_ASG;
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*  949 */       return JCTree.Tag.DIV_ASG;
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 115:
/*  952 */       return JCTree.Tag.MOD_ASG;
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 132:
/*      */     case 133:
/*      */     case 134:
/*      */     case 135:
/*      */     case 136:
/*      */     case 137:
/*      */     case 138:
/*      */     case 139:
/*      */     case 140:
/*      */     case 141:
/*      */     case 142:
/*      */     case 143:
/*      */     case 144:
/*      */     case 145:
/*      */     case 146:
/*      */     case 147:
/*      */     case 148:
/*      */     case 149:
/*      */     case 150:
/*      */     case 151:
/*      */     case 152:
/*      */     case 153:
/*      */     case 154:
/*      */     case 155:
/*      */     case 156:
/*      */     case 157:
/*      */     case 158:
/*      */     case 159:
/*      */     case 160:
/*      */     case 161:
/*      */     case 162:
/*      */     case 163:
/*      */     case 164:
/*      */     case 165:
/*      */     case 166:
/*      */     case 167:
/*      */     case 168:
/*      */     case 169:
/*      */     case 170:
/*      */     case 171:
/*      */     case 172:
/*      */     case 173:
/*      */     case 174:
/*      */     case 175:
/*      */     case 176:
/*      */     case 177:
/*      */     case 178:
/*      */     case 179:
/*      */     case 180:
/*      */     case 181:
/*      */     case 182:
/*      */     case 183:
/*      */     case 184:
/*      */     case 185:
/*      */     case 186:
/*      */     case 187:
/*      */     case 188:
/*      */     case 189:
/*      */     case 190:
/*      */     case 191:
/*      */     case 192:
/*      */     case 193:
/*      */     case 194:
/*      */     case 195:
/*      */     case 196:
/*      */     case 197:
/*      */     case 198:
/*      */     case 199:
/*      */     case 200:
/*      */     case 201:
/*      */     case 202:
/*      */     case 203:
/*      */     case 204:
/*      */     case 205:
/*      */     case 206:
/*      */     case 207:
/*      */     case 208:
/*      */     case 209:
/*      */     case 210:
/*      */     case 211:
/*      */     case 212:
/*      */     case 213:
/*      */     case 214:
/*      */     case 215:
/*      */     case 216:
/*      */     case 217:
/*      */     case 218:
/*      */     case 219:
/*      */     case 220:
/*      */     case 221:
/*      */     case 222:
/*      */     case 223:
/*      */     case 224:
/*      */     case 225:
/*      */     case 226:
/*      */     case 227:
/*      */     case 228:
/*      */     case 229:
/*      */     case 230:
/*      */     case 231:
/*      */     case 232:
/*      */     case 233:
/*      */     case 234:
/*      */     case 235:
/*      */     case 236:
/*      */     case 237:
/*      */     case 238:
/*      */     case 239:
/*      */     case 240:
/*      */     case 241:
/*      */     case 242:
/*      */     case 243:
/*      */     case 244:
/*      */     case 245:
/*      */     case 246:
/*      */     case 247:
/*      */     case 248:
/*      */     case 249:
/*      */     case 250:
/*      */     case 251:
/*      */     case 252:
/*      */     case 253:
/*      */     case 254:
/*      */     case 255:
/*      */     case 257:
/*      */     case 258:
/*      */     case 259:
/*      */     case 260:
/*      */     case 261:
/*      */     case 262:
/*      */     case 263:
/*      */     case 264:
/*      */     case 265:
/*      */     case 266:
/*      */     case 267:
/*      */     case 268:
/*  954 */     case 269: } throw new AssertionError();
/*      */   }
/*      */ 
/*      */   Name accessName(int paramInt1, int paramInt2)
/*      */   {
/*  961 */     return this.names.fromString("access" + this.target
/*  962 */       .syntheticNameChar() + paramInt1 + paramInt2 / 10 + paramInt2 % 10);
/*      */   }
/*      */ 
/*      */   Symbol.MethodSymbol accessSymbol(Symbol paramSymbol, JCTree paramJCTree1, JCTree paramJCTree2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  983 */     Symbol.ClassSymbol localClassSymbol = (paramBoolean2) && (paramBoolean1) ? (Symbol.ClassSymbol)((JCTree.JCFieldAccess)paramJCTree1).selected.type.tsym : 
/*  983 */       accessClass(paramSymbol, paramBoolean1, paramJCTree1);
/*      */ 
/*  985 */     Symbol localSymbol = paramSymbol;
/*  986 */     if (paramSymbol.owner != localClassSymbol) {
/*  987 */       localSymbol = paramSymbol.clone(localClassSymbol);
/*  988 */       this.actualSymbols.put(localSymbol, paramSymbol);
/*      */     }
/*      */ 
/*  992 */     Integer localInteger = (Integer)this.accessNums
/*  992 */       .get(localSymbol);
/*      */ 
/*  993 */     if (localInteger == null) {
/*  994 */       localInteger = Integer.valueOf(this.accessed.length());
/*  995 */       this.accessNums.put(localSymbol, localInteger);
/*  996 */       this.accessSyms.put(localSymbol, new Symbol.MethodSymbol[NCODES]);
/*  997 */       this.accessed.append(localSymbol);
/*      */     }
/*      */     int i;
/*      */     List localList1;
/*      */     Type localType;
/*      */     List localList2;
/* 1005 */     switch (localSymbol.kind) {
/*      */     case 4:
/* 1007 */       i = accessCode(paramJCTree1, paramJCTree2);
/* 1008 */       if (i >= 12) {
/* 1009 */         localObject = binaryAccessOperator(i);
/* 1010 */         if (((Symbol.OperatorSymbol)localObject).opcode == 256)
/* 1011 */           localList1 = List.of(this.syms.objectType);
/*      */         else
/* 1013 */           localList1 = ((Symbol.OperatorSymbol)localObject).type.getParameterTypes().tail;
/* 1014 */       } else if (i == 2) {
/* 1015 */         localList1 = List.of(localSymbol.erasure(this.types));
/*      */       } else {
/* 1017 */         localList1 = List.nil();
/* 1018 */       }localType = localSymbol.erasure(this.types);
/* 1019 */       localList2 = List.nil();
/* 1020 */       break;
/*      */     case 16:
/* 1022 */       i = 0;
/* 1023 */       localList1 = localSymbol.erasure(this.types).getParameterTypes();
/* 1024 */       localType = localSymbol.erasure(this.types).getReturnType();
/* 1025 */       localList2 = localSymbol.type.getThrownTypes();
/* 1026 */       break;
/*      */     default:
/* 1028 */       throw new AssertionError();
/*      */     }
/*      */ 
/* 1033 */     if ((paramBoolean1) && (paramBoolean2)) i++;
/*      */ 
/* 1039 */     if ((localSymbol.flags() & 0x8) == 0L) {
/* 1040 */       localList1 = localList1.prepend(localSymbol.owner.erasure(this.types));
/*      */     }
/* 1042 */     Object localObject = (Symbol.MethodSymbol[])this.accessSyms.get(localSymbol);
/* 1043 */     Symbol.MethodSymbol localMethodSymbol = localObject[i];
/* 1044 */     if (localMethodSymbol == null)
/*      */     {
/* 1047 */       localMethodSymbol = new Symbol.MethodSymbol(4104L, 
/* 1047 */         accessName(localInteger
/* 1047 */         .intValue(), i), new Type.MethodType(localList1, localType, localList2, this.syms.methodClass), localClassSymbol);
/*      */ 
/* 1050 */       enterSynthetic(paramJCTree1.pos(), localMethodSymbol, localClassSymbol.members());
/* 1051 */       localObject[i] = localMethodSymbol;
/*      */     }
/* 1053 */     return localMethodSymbol;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression accessBase(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/* 1064 */     return (paramSymbol.flags() & 0x8) != 0L ? 
/* 1063 */       access(this.make
/* 1063 */       .at(paramDiagnosticPosition
/* 1063 */       .getStartPosition()).QualIdent(paramSymbol.owner)) : 
/* 1064 */       makeOwnerThis(paramDiagnosticPosition, paramSymbol, true);
/*      */   }
/*      */ 
/*      */   boolean needsPrivateAccess(Symbol paramSymbol)
/*      */   {
/* 1070 */     if (((paramSymbol.flags() & 0x2) == 0L) || (paramSymbol.owner == this.currentClass))
/* 1071 */       return false;
/* 1072 */     if ((paramSymbol.name == this.names.init) && (paramSymbol.owner.isLocal()))
/*      */     {
/* 1074 */       paramSymbol.flags_field &= -3L;
/* 1075 */       return false;
/*      */     }
/* 1077 */     return true;
/*      */   }
/*      */ 
/*      */   boolean needsProtectedAccess(Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/* 1084 */     if (((paramSymbol.flags() & 0x4) == 0L) || (paramSymbol.owner.owner == this.currentClass.owner) || 
/* 1086 */       (paramSymbol
/* 1086 */       .packge() == this.currentClass.packge()))
/* 1087 */       return false;
/* 1088 */     if (!this.currentClass.isSubClass(paramSymbol.owner, this.types))
/* 1089 */       return true;
/* 1090 */     if (((paramSymbol.flags() & 0x8) != 0L) || 
/* 1091 */       (!paramJCTree
/* 1091 */       .hasTag(JCTree.Tag.SELECT)) || 
/* 1092 */       (TreeInfo.name(((JCTree.JCFieldAccess)paramJCTree).selected) == 
/* 1092 */       this.names._super))
/* 1093 */       return false;
/* 1094 */     return !((JCTree.JCFieldAccess)paramJCTree).selected.type.tsym.isSubClass(this.currentClass, this.types);
/*      */   }
/*      */ 
/*      */   Symbol.ClassSymbol accessClass(Symbol paramSymbol, boolean paramBoolean, JCTree paramJCTree)
/*      */   {
/* 1103 */     if (paramBoolean) {
/* 1104 */       Symbol.TypeSymbol localTypeSymbol = null;
/* 1105 */       Symbol.ClassSymbol localClassSymbol = this.currentClass;
/* 1106 */       if ((paramJCTree.hasTag(JCTree.Tag.SELECT)) && ((paramSymbol.flags() & 0x8) == 0L)) {
/* 1107 */         localTypeSymbol = ((JCTree.JCFieldAccess)paramJCTree).selected.type.tsym;
/* 1108 */         while (!localTypeSymbol.isSubClass(localClassSymbol, this.types)) {
/* 1109 */           localClassSymbol = localClassSymbol.owner.enclClass();
/*      */         }
/* 1111 */         return localClassSymbol;
/*      */       }
/* 1113 */       while (!localClassSymbol.isSubClass(paramSymbol.owner, this.types)) {
/* 1114 */         localClassSymbol = localClassSymbol.owner.enclClass();
/*      */       }
/*      */ 
/* 1117 */       return localClassSymbol;
/*      */     }
/*      */ 
/* 1120 */     return paramSymbol.owner.enclClass();
/*      */   }
/*      */ 
/*      */   private void addPrunedInfo(JCTree paramJCTree)
/*      */   {
/* 1125 */     List localList = (List)this.prunedTree.get(this.currentClass);
/* 1126 */     localList = localList == null ? List.of(paramJCTree) : localList.prepend(paramJCTree);
/* 1127 */     this.prunedTree.put(this.currentClass, localList);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression access(Symbol paramSymbol, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, boolean paramBoolean)
/*      */   {
/* 1139 */     while ((paramSymbol.kind == 4) && (paramSymbol.owner.kind == 16) && 
/* 1140 */       (paramSymbol.owner
/* 1140 */       .enclClass() != this.currentClass))
/*      */     {
/* 1142 */       localObject1 = ((Symbol.VarSymbol)paramSymbol).getConstValue();
/* 1143 */       if (localObject1 != null) {
/* 1144 */         this.make.at(paramJCExpression1.pos);
/* 1145 */         return makeLit(paramSymbol.type, localObject1);
/*      */       }
/*      */ 
/* 1148 */       paramSymbol = this.proxies.lookup(proxyName(paramSymbol.name)).sym;
/* 1149 */       Assert.check((paramSymbol != null) && ((paramSymbol.flags_field & 0x10) != 0L));
/* 1150 */       paramJCExpression1 = this.make.at(paramJCExpression1.pos).Ident(paramSymbol);
/*      */     }
/* 1152 */     Object localObject1 = paramJCExpression1.hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)paramJCExpression1).selected : null;
/* 1153 */     switch (paramSymbol.kind) {
/*      */     case 2:
/* 1155 */       if (paramSymbol.owner.kind != 1)
/*      */       {
/* 1158 */         Name localName = Convert.shortName(paramSymbol.flatName());
/* 1159 */         while ((localObject1 != null) && 
/* 1160 */           (TreeInfo.symbol((JCTree)localObject1) != null) && 
/* 1161 */           (TreeInfo.symbol((JCTree)localObject1).kind != 
/* 1161 */           1)) {
/* 1162 */           localObject1 = ((JCTree.JCExpression)localObject1).hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)localObject1).selected : null;
/*      */         }
/*      */ 
/* 1166 */         if (paramJCExpression1.hasTag(JCTree.Tag.IDENT)) {
/* 1167 */           ((JCTree.JCIdent)paramJCExpression1).name = localName;
/* 1168 */         } else if (localObject1 == null) {
/* 1169 */           paramJCExpression1 = this.make.at(paramJCExpression1.pos).Ident(paramSymbol);
/* 1170 */           ((JCTree.JCIdent)paramJCExpression1).name = localName;
/*      */         } else {
/* 1172 */           ((JCTree.JCFieldAccess)paramJCExpression1).selected = ((JCTree.JCExpression)localObject1);
/* 1173 */           ((JCTree.JCFieldAccess)paramJCExpression1).name = localName;
/*      */         }
/*      */       }
/* 1175 */       break;
/*      */     case 4:
/*      */     case 16:
/* 1178 */       if (paramSymbol.owner.kind == 2)
/*      */       {
/* 1186 */         boolean bool = ((paramBoolean) && (!needsPrivateAccess(paramSymbol))) || 
/* 1186 */           (needsProtectedAccess(paramSymbol, paramJCExpression1));
/*      */ 
/* 1187 */         int i = (bool) || (needsPrivateAccess(paramSymbol)) ? 1 : 0;
/*      */ 
/* 1191 */         if ((localObject1 == null) && (paramSymbol.owner != this.syms.predefClass));
/* 1194 */         int j = !paramSymbol
/* 1194 */           .isMemberOf(this.currentClass, this.types) ? 
/* 1194 */           1 : 0;
/*      */ 
/* 1196 */         if ((i != 0) || (j != 0)) {
/* 1197 */           this.make.at(paramJCExpression1.pos);
/*      */           Object localObject2;
/* 1200 */           if (paramSymbol.kind == 4) {
/* 1201 */             localObject2 = ((Symbol.VarSymbol)paramSymbol).getConstValue();
/* 1202 */             if (localObject2 != null) {
/* 1203 */               addPrunedInfo(paramJCExpression1);
/* 1204 */               return makeLit(paramSymbol.type, localObject2);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1210 */           if (i != 0) {
/* 1211 */             localObject2 = List.nil();
/* 1212 */             if ((paramSymbol.flags() & 0x8) == 0L)
/*      */             {
/* 1215 */               if (localObject1 == null)
/* 1216 */                 localObject1 = makeOwnerThis(paramJCExpression1.pos(), paramSymbol, true);
/* 1217 */               localObject2 = ((List)localObject2).prepend(localObject1);
/* 1218 */               localObject1 = null;
/*      */             }
/* 1220 */             Symbol.MethodSymbol localMethodSymbol = accessSymbol(paramSymbol, paramJCExpression1, paramJCExpression2, bool, paramBoolean);
/*      */ 
/* 1223 */             JCTree.JCExpression localJCExpression = this.make.Select(localObject1 != null ? localObject1 : this.make
/* 1224 */               .QualIdent(localMethodSymbol.owner), 
/* 1224 */               localMethodSymbol);
/*      */ 
/* 1226 */             return this.make.App(localJCExpression, (List)localObject2);
/*      */           }
/*      */ 
/* 1230 */           if (j != 0)
/*      */           {
/* 1232 */             return this.make.at(paramJCExpression1.pos).Select(
/* 1232 */               accessBase(paramJCExpression1
/* 1232 */               .pos(), paramSymbol), paramSymbol).setType(paramJCExpression1.type);
/*      */           }
/*      */         }
/*      */       }
/* 1235 */       else if ((paramSymbol.owner.kind == 16) && (this.lambdaTranslationMap != null))
/*      */       {
/* 1239 */         Symbol localSymbol = (Symbol)this.lambdaTranslationMap.get(paramSymbol);
/* 1240 */         if (localSymbol != null)
/* 1241 */           paramJCExpression1 = this.make.at(paramJCExpression1.pos).Ident(localSymbol);
/*      */       }
/*      */       break;
/*      */     }
/* 1245 */     return paramJCExpression1;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression access(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1252 */     Symbol localSymbol = TreeInfo.symbol(paramJCExpression);
/* 1253 */     return localSymbol == null ? paramJCExpression : access(localSymbol, paramJCExpression, null, false);
/*      */   }
/*      */ 
/*      */   Symbol accessConstructor(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/* 1262 */     if (needsPrivateAccess(paramSymbol)) {
/* 1263 */       Symbol.ClassSymbol localClassSymbol = paramSymbol.owner.enclClass();
/* 1264 */       Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)this.accessConstrs.get(paramSymbol);
/* 1265 */       if (localMethodSymbol == null) {
/* 1266 */         List localList = paramSymbol.type.getParameterTypes();
/* 1267 */         if ((localClassSymbol.flags_field & 0x4000) != 0L)
/*      */         {
/* 1270 */           localList = localList
/* 1269 */             .prepend(this.syms.intType)
/* 1270 */             .prepend(this.syms.stringType);
/*      */         }
/*      */ 
/* 1278 */         localMethodSymbol = new Symbol.MethodSymbol(4096L, this.names.init, new Type.MethodType(localList
/* 1275 */           .append(
/* 1276 */           accessConstructorTag().erasure(this.types)), paramSymbol.type
/* 1277 */           .getReturnType(), paramSymbol.type
/* 1278 */           .getThrownTypes(), this.syms.methodClass), localClassSymbol);
/*      */ 
/* 1281 */         enterSynthetic(paramDiagnosticPosition, localMethodSymbol, localClassSymbol.members());
/* 1282 */         this.accessConstrs.put(paramSymbol, localMethodSymbol);
/* 1283 */         this.accessed.append(paramSymbol);
/*      */       }
/* 1285 */       return localMethodSymbol;
/*      */     }
/* 1287 */     return paramSymbol;
/*      */   }
/*      */ 
/*      */   Symbol.ClassSymbol accessConstructorTag()
/*      */   {
/* 1294 */     Symbol.ClassSymbol localClassSymbol1 = this.currentClass.outermostClass();
/* 1295 */     Name localName = this.names.fromString("" + localClassSymbol1.getQualifiedName() + this.target
/* 1296 */       .syntheticNameChar() + "1");
/*      */ 
/* 1298 */     Symbol.ClassSymbol localClassSymbol2 = (Symbol.ClassSymbol)this.chk.compiled.get(localName);
/* 1299 */     if (localClassSymbol2 == null) {
/* 1300 */       localClassSymbol2 = makeEmptyClass(4104L, localClassSymbol1).sym;
/*      */     }
/* 1302 */     this.accessConstrTags = this.accessConstrTags.prepend(localClassSymbol2);
/* 1303 */     return localClassSymbol2;
/*      */   }
/*      */ 
/*      */   void makeAccessible(Symbol paramSymbol)
/*      */   {
/* 1310 */     JCTree.JCClassDecl localJCClassDecl = classDef(paramSymbol.owner.enclClass());
/* 1311 */     if (localJCClassDecl == null) Assert.error("class def not found: " + paramSymbol + " in " + paramSymbol.owner);
/* 1312 */     if (paramSymbol.name == this.names.init) {
/* 1313 */       localJCClassDecl.defs = localJCClassDecl.defs.prepend(
/* 1314 */         accessConstructorDef(localJCClassDecl.pos, paramSymbol, 
/* 1314 */         (Symbol.MethodSymbol)this.accessConstrs
/* 1314 */         .get(paramSymbol)));
/*      */     }
/*      */     else {
/* 1316 */       Symbol.MethodSymbol[] arrayOfMethodSymbol = (Symbol.MethodSymbol[])this.accessSyms.get(paramSymbol);
/* 1317 */       for (int i = 0; i < NCODES; i++)
/* 1318 */         if (arrayOfMethodSymbol[i] != null)
/* 1319 */           localJCClassDecl.defs = localJCClassDecl.defs.prepend(
/* 1320 */             accessDef(localJCClassDecl.pos, paramSymbol, arrayOfMethodSymbol[i], i));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static JCTree.Tag mapUnaryOpCodeToTag(int paramInt)
/*      */   {
/* 1329 */     switch (paramInt) {
/*      */     case 4:
/* 1331 */       return JCTree.Tag.PREINC;
/*      */     case 6:
/* 1333 */       return JCTree.Tag.PREDEC;
/*      */     case 8:
/* 1335 */       return JCTree.Tag.POSTINC;
/*      */     case 10:
/* 1337 */       return JCTree.Tag.POSTDEC;
/*      */     case 5:
/*      */     case 7:
/* 1339 */     case 9: } return JCTree.Tag.NO_TAG;
/*      */   }
/*      */ 
/*      */   private static int mapTagToUnaryOpCode(JCTree.Tag paramTag)
/*      */   {
/* 1347 */     switch (7.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramTag.ordinal()]) {
/*      */     case 1:
/* 1349 */       return 4;
/*      */     case 2:
/* 1351 */       return 6;
/*      */     case 3:
/* 1353 */       return 8;
/*      */     case 4:
/* 1355 */       return 10;
/*      */     }
/* 1357 */     return -1;
/*      */   }
/*      */ 
/*      */   JCTree accessDef(int paramInt1, Symbol paramSymbol, Symbol.MethodSymbol paramMethodSymbol, int paramInt2)
/*      */   {
/* 1369 */     this.currentClass = paramSymbol.owner.enclClass();
/* 1370 */     this.make.at(paramInt1);
/* 1371 */     JCTree.JCMethodDecl localJCMethodDecl = this.make.MethodDef(paramMethodSymbol, null);
/*      */ 
/* 1374 */     Symbol localSymbol = (Symbol)this.actualSymbols.get(paramSymbol);
/* 1375 */     if (localSymbol == null) localSymbol = paramSymbol;
/*      */     Object localObject1;
/*      */     List localList1;
/*      */     Object localObject2;
/* 1379 */     if ((localSymbol.flags() & 0x8) != 0L) {
/* 1380 */       localObject1 = this.make.Ident(localSymbol);
/* 1381 */       localList1 = this.make.Idents(localJCMethodDecl.params);
/*      */     } else {
/* 1383 */       localObject2 = this.make.Ident((JCTree.JCVariableDecl)localJCMethodDecl.params.head);
/* 1384 */       if (paramInt2 % 2 != 0)
/*      */       {
/* 1388 */         ((JCTree.JCExpression)localObject2).setType(this.types.erasure(this.types.supertype(paramSymbol.owner.enclClass().type)));
/*      */       }
/* 1390 */       localObject1 = this.make.Select((JCTree.JCExpression)localObject2, localSymbol);
/* 1391 */       localList1 = this.make.Idents(localJCMethodDecl.params.tail);
/*      */     }
/*      */ 
/* 1394 */     if (localSymbol.kind == 4)
/*      */     {
/* 1396 */       int i = paramInt2 - (paramInt2 & 0x1);
/*      */       Object localObject3;
/* 1399 */       switch (i) {
/*      */       case 0:
/* 1401 */         localObject3 = localObject1;
/* 1402 */         break;
/*      */       case 2:
/* 1404 */         localObject3 = this.make.Assign((JCTree.JCExpression)localObject1, (JCTree.JCExpression)localList1.head);
/* 1405 */         break;
/*      */       case 4:
/*      */       case 6:
/*      */       case 8:
/*      */       case 10:
/* 1407 */         localObject3 = makeUnary(mapUnaryOpCodeToTag(i), (JCTree.JCExpression)localObject1);
/* 1408 */         break;
/*      */       case 1:
/*      */       case 3:
/*      */       case 5:
/*      */       case 7:
/*      */       case 9:
/*      */       default:
/* 1410 */         localObject3 = this.make.Assignop(
/* 1411 */           treeTag(binaryAccessOperator(i)), 
/* 1411 */           (JCTree)localObject1, (JCTree)localList1.head);
/* 1412 */         ((JCTree.JCAssignOp)localObject3).operator = binaryAccessOperator(i);
/*      */       }
/* 1414 */       localObject2 = this.make.Return(((JCTree.JCExpression)localObject3).setType(localSymbol.type));
/*      */     } else {
/* 1416 */       localObject2 = this.make.Call(this.make.App((JCTree.JCExpression)localObject1, localList1));
/*      */     }
/* 1418 */     localJCMethodDecl.body = this.make.Block(0L, List.of(localObject2));
/*      */ 
/* 1422 */     for (List localList2 = localJCMethodDecl.params; localList2.nonEmpty(); localList2 = localList2.tail)
/* 1423 */       ((JCTree.JCVariableDecl)localList2.head).vartype = access(((JCTree.JCVariableDecl)localList2.head).vartype);
/* 1424 */     localJCMethodDecl.restype = access(localJCMethodDecl.restype);
/* 1425 */     for (localList2 = localJCMethodDecl.thrown; localList2.nonEmpty(); localList2 = localList2.tail) {
/* 1426 */       localList2.head = access((JCTree.JCExpression)localList2.head);
/*      */     }
/* 1428 */     return localJCMethodDecl;
/*      */   }
/*      */ 
/*      */   JCTree accessConstructorDef(int paramInt, Symbol paramSymbol, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 1437 */     this.make.at(paramInt);
/* 1438 */     JCTree.JCMethodDecl localJCMethodDecl = this.make.MethodDef(paramMethodSymbol, paramMethodSymbol
/* 1439 */       .externalType(this.types), 
/* 1439 */       null);
/*      */ 
/* 1441 */     JCTree.JCIdent localJCIdent = this.make.Ident(this.names._this);
/* 1442 */     localJCIdent.sym = paramSymbol;
/* 1443 */     localJCIdent.type = paramSymbol.type;
/* 1444 */     localJCMethodDecl.body = this.make
/* 1445 */       .Block(0L, 
/* 1445 */       List.of(this.make
/* 1446 */       .Call(this.make
/* 1447 */       .App(localJCIdent, this.make
/* 1449 */       .Idents(localJCMethodDecl.params
/* 1449 */       .reverse().tail.reverse())))));
/* 1450 */     return localJCMethodDecl;
/*      */   }
/*      */ 
/*      */   Name proxyName(Name paramName)
/*      */   {
/* 1482 */     return this.names.fromString("val" + this.target.syntheticNameChar() + paramName);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCVariableDecl> freevarDefs(int paramInt, List<Symbol.VarSymbol> paramList, Symbol paramSymbol)
/*      */   {
/* 1491 */     return freevarDefs(paramInt, paramList, paramSymbol, 0L);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCVariableDecl> freevarDefs(int paramInt, List<Symbol.VarSymbol> paramList, Symbol paramSymbol, long paramLong)
/*      */   {
/* 1496 */     long l = 0x1010 | paramLong;
/* 1497 */     if ((paramSymbol.kind == 2) && 
/* 1498 */       (this.target
/* 1498 */       .usePrivateSyntheticFields()))
/* 1499 */       l |= 2L;
/* 1500 */     List localList = List.nil();
/* 1501 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 1502 */       Symbol.VarSymbol localVarSymbol1 = (Symbol.VarSymbol)((List)localObject).head;
/*      */ 
/* 1504 */       Symbol.VarSymbol localVarSymbol2 = new Symbol.VarSymbol(l, 
/* 1504 */         proxyName(localVarSymbol1.name), 
/* 1504 */         localVarSymbol1.erasure(this.types), paramSymbol);
/* 1505 */       this.proxies.enter(localVarSymbol2);
/* 1506 */       JCTree.JCVariableDecl localJCVariableDecl = this.make.at(paramInt).VarDef(localVarSymbol2, null);
/* 1507 */       localJCVariableDecl.vartype = access(localJCVariableDecl.vartype);
/* 1508 */       localList = localList.prepend(localJCVariableDecl);
/*      */     }
/* 1510 */     return localList;
/*      */   }
/*      */ 
/*      */   Name outerThisName(Type paramType, Symbol paramSymbol)
/*      */   {
/* 1517 */     Type localType = paramType.getEnclosingType();
/* 1518 */     int i = 0;
/* 1519 */     while (localType.hasTag(TypeTag.CLASS)) {
/* 1520 */       localType = localType.getEnclosingType();
/* 1521 */       i++;
/*      */     }
/* 1523 */     Name localName = this.names.fromString("this" + this.target.syntheticNameChar() + i);
/* 1524 */     while ((paramSymbol.kind == 2) && (((Symbol.ClassSymbol)paramSymbol).members().lookup(localName).scope != null))
/* 1525 */       localName = this.names.fromString(localName.toString() + this.target.syntheticNameChar());
/* 1526 */     return localName;
/*      */   }
/*      */ 
/*      */   private Symbol.VarSymbol makeOuterThisVarSymbol(Symbol paramSymbol, long paramLong) {
/* 1530 */     if ((paramSymbol.kind == 2) && 
/* 1531 */       (this.target
/* 1531 */       .usePrivateSyntheticFields()))
/* 1532 */       paramLong |= 2L;
/* 1533 */     Type localType = this.types.erasure(paramSymbol.enclClass().type.getEnclosingType());
/*      */ 
/* 1535 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(paramLong, 
/* 1535 */       outerThisName(localType, paramSymbol), 
/* 1535 */       localType, paramSymbol);
/* 1536 */     this.outerThisStack = this.outerThisStack.prepend(localVarSymbol);
/* 1537 */     return localVarSymbol;
/*      */   }
/*      */ 
/*      */   private JCTree.JCVariableDecl makeOuterThisVarDecl(int paramInt, Symbol.VarSymbol paramVarSymbol) {
/* 1541 */     JCTree.JCVariableDecl localJCVariableDecl = this.make.at(paramInt).VarDef(paramVarSymbol, null);
/* 1542 */     localJCVariableDecl.vartype = access(localJCVariableDecl.vartype);
/* 1543 */     return localJCVariableDecl;
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl outerThisDef(int paramInt, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 1551 */     Symbol.ClassSymbol localClassSymbol = paramMethodSymbol.enclClass();
/*      */ 
/* 1557 */     int i = ((paramMethodSymbol
/* 1554 */       .isConstructor()) && (paramMethodSymbol.isAnonymous())) || (
/* 1556 */       (paramMethodSymbol
/* 1556 */       .isConstructor()) && (localClassSymbol.isInner()) && 
/* 1557 */       (!localClassSymbol
/* 1557 */       .isPrivate()) && (!localClassSymbol.isStatic())) ? 1 : 0;
/* 1558 */     long l = 0x10 | (i != 0 ? 32768 : 4096) | 0x0;
/*      */ 
/* 1560 */     Symbol.VarSymbol localVarSymbol = makeOuterThisVarSymbol(paramMethodSymbol, l);
/* 1561 */     paramMethodSymbol.extraParams = paramMethodSymbol.extraParams.prepend(localVarSymbol);
/* 1562 */     return makeOuterThisVarDecl(paramInt, localVarSymbol);
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl outerThisDef(int paramInt, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1570 */     Symbol.VarSymbol localVarSymbol = makeOuterThisVarSymbol(paramClassSymbol, 4112L);
/* 1571 */     return makeOuterThisVarDecl(paramInt, localVarSymbol);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> loadFreevars(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, List<Symbol.VarSymbol> paramList)
/*      */   {
/* 1580 */     List localList = List.nil();
/* 1581 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1582 */       localList = localList.prepend(loadFreevar(paramDiagnosticPosition, (Symbol.VarSymbol)((List)localObject).head));
/* 1583 */     return localList;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression loadFreevar(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol) {
/* 1587 */     return access(paramVarSymbol, this.make.at(paramDiagnosticPosition).Ident(paramVarSymbol), null, false);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol)
/*      */   {
/* 1595 */     if (this.currentClass == paramTypeSymbol)
/*      */     {
/* 1597 */       return this.make.at(paramDiagnosticPosition).This(paramTypeSymbol.erasure(this.types));
/*      */     }
/*      */ 
/* 1600 */     return makeOuterThis(paramDiagnosticPosition, paramTypeSymbol);
/*      */   }
/*      */ 
/*      */   JCTree makeTwrTry(JCTree.JCTry paramJCTry)
/*      */   {
/* 1641 */     make_at(paramJCTry.pos());
/* 1642 */     this.twrVars = this.twrVars.dup();
/* 1643 */     JCTree.JCBlock localJCBlock = makeTwrBlock(paramJCTry.resources, paramJCTry.body, paramJCTry.finallyCanCompleteNormally, 0);
/*      */ 
/* 1645 */     if ((paramJCTry.catchers.isEmpty()) && (paramJCTry.finalizer == null))
/* 1646 */       this.result = translate(localJCBlock);
/*      */     else
/* 1648 */       this.result = translate(this.make.Try(localJCBlock, paramJCTry.catchers, paramJCTry.finalizer));
/* 1649 */     this.twrVars = this.twrVars.leave();
/* 1650 */     return this.result;
/*      */   }
/*      */ 
/*      */   private JCTree.JCBlock makeTwrBlock(List<JCTree> paramList, JCTree.JCBlock paramJCBlock, boolean paramBoolean, int paramInt)
/*      */   {
/* 1655 */     if (paramList.isEmpty()) {
/* 1656 */       return paramJCBlock;
/*      */     }
/*      */ 
/* 1659 */     ListBuffer localListBuffer = new ListBuffer();
/* 1660 */     JCTree localJCTree = (JCTree)paramList.head;
/* 1661 */     Object localObject1 = null;
/* 1662 */     if ((localJCTree instanceof JCTree.JCVariableDecl)) {
/* 1663 */       localObject2 = (JCTree.JCVariableDecl)localJCTree;
/* 1664 */       localObject1 = this.make.Ident(((JCTree.JCVariableDecl)localObject2).sym).setType(localJCTree.type);
/* 1665 */       localListBuffer.add(localObject2);
/*      */     } else {
/* 1667 */       Assert.check(localJCTree instanceof JCTree.JCExpression);
/*      */ 
/* 1672 */       localObject2 = new Symbol.VarSymbol(4112L, 
/* 1670 */         makeSyntheticName(this.names
/* 1670 */         .fromString("twrVar" + paramInt), 
/* 1670 */         this.twrVars), localJCTree.type
/* 1672 */         .hasTag(TypeTag.BOT) ? 
/* 1672 */         this.syms.autoCloseableType : localJCTree.type, this.currentMethodSym);
/*      */ 
/* 1675 */       this.twrVars.enter((Symbol)localObject2);
/*      */ 
/* 1677 */       localJCVariableDecl1 = this.make
/* 1677 */         .VarDef((Symbol.VarSymbol)localObject2, (JCTree.JCExpression)localJCTree);
/*      */ 
/* 1678 */       localObject1 = this.make.Ident((Symbol)localObject2);
/* 1679 */       localListBuffer.add(localJCVariableDecl1);
/*      */     }
/*      */ 
/* 1685 */     Object localObject2 = new Symbol.VarSymbol(4096L, 
/* 1685 */       makeSyntheticName(this.names
/* 1685 */       .fromString("primaryException" + paramInt), 
/* 1685 */       this.twrVars), this.syms.throwableType, this.currentMethodSym);
/*      */ 
/* 1689 */     this.twrVars.enter((Symbol)localObject2);
/* 1690 */     JCTree.JCVariableDecl localJCVariableDecl1 = this.make.VarDef((Symbol.VarSymbol)localObject2, makeNull());
/* 1691 */     localListBuffer.add(localJCVariableDecl1);
/*      */ 
/* 1696 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(4112L, this.names
/* 1696 */       .fromString("t" + this.target
/* 1697 */       .syntheticNameChar()), this.syms.throwableType, this.currentMethodSym);
/*      */ 
/* 1700 */     JCTree.JCVariableDecl localJCVariableDecl2 = this.make.VarDef(localVarSymbol, null);
/* 1701 */     JCTree.JCStatement localJCStatement = this.make.Assignment((Symbol)localObject2, this.make.Ident(localVarSymbol));
/* 1702 */     JCTree.JCThrow localJCThrow = this.make.Throw(this.make.Ident(localVarSymbol));
/* 1703 */     JCTree.JCBlock localJCBlock1 = this.make.Block(0L, List.of(localJCStatement, localJCThrow));
/* 1704 */     JCTree.JCCatch localJCCatch = this.make.Catch(localJCVariableDecl2, localJCBlock1);
/*      */ 
/* 1706 */     int i = this.make.pos;
/* 1707 */     this.make.at(TreeInfo.endPos(paramJCBlock));
/* 1708 */     JCTree.JCBlock localJCBlock2 = makeTwrFinallyClause((Symbol)localObject2, (JCTree.JCExpression)localObject1);
/* 1709 */     this.make.at(i);
/* 1710 */     JCTree.JCTry localJCTry = this.make.Try(makeTwrBlock(paramList.tail, paramJCBlock, paramBoolean, paramInt + 1), 
/* 1712 */       List.of(localJCCatch), 
/* 1712 */       localJCBlock2);
/*      */ 
/* 1714 */     localJCTry.finallyCanCompleteNormally = paramBoolean;
/* 1715 */     localListBuffer.add(localJCTry);
/* 1716 */     JCTree.JCBlock localJCBlock3 = this.make.Block(0L, localListBuffer.toList());
/* 1717 */     return localJCBlock3;
/*      */   }
/*      */ 
/*      */   private JCTree.JCBlock makeTwrFinallyClause(Symbol paramSymbol, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1723 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(4096L, this.make
/* 1723 */       .paramName(2), 
/* 1723 */       this.syms.throwableType, this.currentMethodSym);
/*      */ 
/* 1727 */     JCTree.JCExpressionStatement localJCExpressionStatement = this.make
/* 1727 */       .Exec(makeCall(this.make
/* 1727 */       .Ident(paramSymbol), 
/* 1727 */       this.names.addSuppressed, 
/* 1729 */       List.of(this.make
/* 1729 */       .Ident(localVarSymbol))));
/*      */ 
/* 1733 */     JCTree.JCBlock localJCBlock1 = this.make
/* 1733 */       .Block(0L, 
/* 1733 */       List.of(makeResourceCloseInvocation(paramJCExpression)));
/*      */ 
/* 1734 */     JCTree.JCVariableDecl localJCVariableDecl = this.make.VarDef(localVarSymbol, null);
/* 1735 */     JCTree.JCBlock localJCBlock2 = this.make.Block(0L, List.of(localJCExpressionStatement));
/* 1736 */     List localList = List.of(this.make.Catch(localJCVariableDecl, localJCBlock2));
/* 1737 */     JCTree.JCTry localJCTry = this.make.Try(localJCBlock1, localList, null);
/* 1738 */     localJCTry.finallyCanCompleteNormally = true;
/*      */ 
/* 1741 */     JCTree.JCIf localJCIf = this.make.If(makeNonNullCheck(this.make.Ident(paramSymbol)), localJCTry, 
/* 1743 */       makeResourceCloseInvocation(paramJCExpression));
/*      */ 
/* 1746 */     return this.make.Block(0L, 
/* 1747 */       List.of(this.make
/* 1747 */       .If(makeNonNullCheck(paramJCExpression), 
/* 1747 */       localJCIf, null)));
/*      */   }
/*      */ 
/*      */   private JCTree.JCStatement makeResourceCloseInvocation(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1754 */     if (this.types.asSuper(paramJCExpression.type, this.syms.autoCloseableType.tsym) == null) {
/* 1755 */       paramJCExpression = (JCTree.JCExpression)convert(paramJCExpression, this.syms.autoCloseableType);
/*      */     }
/*      */ 
/* 1759 */     JCTree.JCMethodInvocation localJCMethodInvocation = makeCall(paramJCExpression, this.names.close, 
/* 1761 */       List.nil());
/* 1762 */     return this.make.Exec(localJCMethodInvocation);
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression makeNonNullCheck(JCTree.JCExpression paramJCExpression) {
/* 1766 */     return makeBinary(JCTree.Tag.NE, paramJCExpression, makeNull());
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeOuterThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol)
/*      */   {
/* 1775 */     List localList = this.outerThisStack;
/* 1776 */     if (localList.isEmpty()) {
/* 1777 */       this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { paramTypeSymbol });
/* 1778 */       Assert.error();
/* 1779 */       return makeNull();
/*      */     }
/* 1781 */     Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localList.head;
/* 1782 */     JCTree.JCExpression localJCExpression = access(this.make.at(paramDiagnosticPosition).Ident(localVarSymbol));
/* 1783 */     Symbol.TypeSymbol localTypeSymbol = localVarSymbol.type.tsym;
/* 1784 */     while (localTypeSymbol != paramTypeSymbol) {
/*      */       do {
/* 1786 */         localList = localList.tail;
/* 1787 */         if (localList.isEmpty()) {
/* 1788 */           this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { paramTypeSymbol });
/*      */ 
/* 1791 */           Assert.error();
/* 1792 */           return localJCExpression;
/*      */         }
/* 1794 */         localVarSymbol = (Symbol.VarSymbol)localList.head;
/* 1795 */       }while (localVarSymbol.owner != localTypeSymbol);
/* 1796 */       if ((localTypeSymbol.owner.kind != 1) && (!localTypeSymbol.hasOuterInstance())) {
/* 1797 */         this.chk.earlyRefError(paramDiagnosticPosition, paramTypeSymbol);
/* 1798 */         Assert.error();
/* 1799 */         return makeNull();
/*      */       }
/* 1801 */       localJCExpression = access(this.make.at(paramDiagnosticPosition).Select(localJCExpression, localVarSymbol));
/* 1802 */       localTypeSymbol = localVarSymbol.type.tsym;
/*      */     }
/* 1804 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeOwnerThis(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, boolean paramBoolean)
/*      */   {
/* 1817 */     Symbol localSymbol = paramSymbol.owner;
/* 1818 */     if (paramBoolean ? paramSymbol.isMemberOf(this.currentClass, this.types) : this.currentClass
/* 1819 */       .isSubClass(paramSymbol.owner, this.types))
/*      */     {
/* 1821 */       return this.make.at(paramDiagnosticPosition).This(localSymbol.erasure(this.types));
/*      */     }
/*      */ 
/* 1824 */     return makeOwnerThisN(paramDiagnosticPosition, paramSymbol, paramBoolean);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression makeOwnerThisN(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, boolean paramBoolean)
/*      */   {
/* 1832 */     Symbol localSymbol = paramSymbol.owner;
/* 1833 */     List localList = this.outerThisStack;
/* 1834 */     if (localList.isEmpty()) {
/* 1835 */       this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { localSymbol });
/* 1836 */       Assert.error();
/* 1837 */       return makeNull();
/*      */     }
/* 1839 */     Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localList.head;
/* 1840 */     JCTree.JCExpression localJCExpression = access(this.make.at(paramDiagnosticPosition).Ident(localVarSymbol));
/* 1841 */     Symbol.TypeSymbol localTypeSymbol = localVarSymbol.type.tsym;
/* 1842 */     while (paramBoolean ? !paramSymbol.isMemberOf(localTypeSymbol, this.types) : !localTypeSymbol.isSubClass(paramSymbol.owner, this.types)) {
/*      */       do {
/* 1844 */         localList = localList.tail;
/* 1845 */         if (localList.isEmpty()) {
/* 1846 */           this.log.error(paramDiagnosticPosition, "no.encl.instance.of.type.in.scope", new Object[] { localSymbol });
/*      */ 
/* 1849 */           Assert.error();
/* 1850 */           return localJCExpression;
/*      */         }
/* 1852 */         localVarSymbol = (Symbol.VarSymbol)localList.head;
/* 1853 */       }while (localVarSymbol.owner != localTypeSymbol);
/* 1854 */       localJCExpression = access(this.make.at(paramDiagnosticPosition).Select(localJCExpression, localVarSymbol));
/* 1855 */       localTypeSymbol = localVarSymbol.type.tsym;
/*      */     }
/* 1857 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCStatement initField(int paramInt, Name paramName)
/*      */   {
/* 1864 */     Scope.Entry localEntry = this.proxies.lookup(paramName);
/* 1865 */     Symbol localSymbol1 = localEntry.sym;
/* 1866 */     Assert.check(localSymbol1.owner.kind == 16);
/* 1867 */     Symbol localSymbol2 = localEntry.next().sym;
/* 1868 */     Assert.check(localSymbol1.owner.owner == localSymbol2.owner);
/* 1869 */     this.make.at(paramInt);
/*      */ 
/* 1871 */     return this.make
/* 1871 */       .Exec(this.make
/* 1872 */       .Assign(this.make
/* 1873 */       .Select(this.make
/* 1873 */       .This(localSymbol2.owner
/* 1873 */       .erasure(this.types)), 
/* 1873 */       localSymbol2), this.make
/* 1874 */       .Ident(localSymbol1))
/* 1874 */       .setType(localSymbol2
/* 1874 */       .erasure(this.types)));
/*      */   }
/*      */ 
/*      */   JCTree.JCStatement initOuterThis(int paramInt)
/*      */   {
/* 1880 */     Symbol.VarSymbol localVarSymbol1 = (Symbol.VarSymbol)this.outerThisStack.head;
/* 1881 */     Assert.check(localVarSymbol1.owner.kind == 16);
/* 1882 */     Symbol.VarSymbol localVarSymbol2 = (Symbol.VarSymbol)this.outerThisStack.tail.head;
/* 1883 */     Assert.check(localVarSymbol1.owner.owner == localVarSymbol2.owner);
/* 1884 */     this.make.at(paramInt);
/*      */ 
/* 1886 */     return this.make
/* 1886 */       .Exec(this.make
/* 1887 */       .Assign(this.make
/* 1888 */       .Select(this.make
/* 1888 */       .This(localVarSymbol2.owner
/* 1888 */       .erasure(this.types)), 
/* 1888 */       localVarSymbol2), this.make
/* 1889 */       .Ident(localVarSymbol1))
/* 1889 */       .setType(localVarSymbol2
/* 1889 */       .erasure(this.types)));
/*      */   }
/*      */ 
/*      */   private Symbol.ClassSymbol outerCacheClass()
/*      */   {
/* 1904 */     Symbol.ClassSymbol localClassSymbol = this.outermostClassDef.sym;
/* 1905 */     if (((localClassSymbol.flags() & 0x200) == 0L) && 
/* 1906 */       (!this.target
/* 1906 */       .useInnerCacheClass())) return localClassSymbol;
/* 1907 */     Scope localScope = localClassSymbol.members();
/* 1908 */     for (Scope.Entry localEntry = localScope.elems; localEntry != null; localEntry = localEntry.sibling)
/* 1909 */       if ((localEntry.sym.kind == 2) && (localEntry.sym.name == this.names.empty))
/*      */       {
/* 1911 */         if ((localEntry.sym
/* 1911 */           .flags() & 0x200) == 0L) return (Symbol.ClassSymbol)localEntry.sym; 
/*      */       }
/* 1912 */     return makeEmptyClass(4104L, localClassSymbol).sym;
/*      */   }
/*      */ 
/*      */   private Symbol.MethodSymbol classDollarSym(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 1927 */     Symbol.ClassSymbol localClassSymbol = outerCacheClass();
/*      */ 
/* 1929 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)lookupSynthetic(this.classDollar, localClassSymbol
/* 1930 */       .members());
/* 1931 */     if (localMethodSymbol == null)
/*      */     {
/* 1938 */       localMethodSymbol = new Symbol.MethodSymbol(4104L, this.classDollar, new Type.MethodType(
/* 1936 */         List.of(this.syms.stringType), 
/* 1936 */         this.types
/* 1937 */         .erasure(this.syms.classType), 
/* 1938 */         List.nil(), this.syms.methodClass), localClassSymbol);
/*      */ 
/* 1941 */       enterSynthetic(paramDiagnosticPosition, localMethodSymbol, localClassSymbol.members());
/*      */ 
/* 1943 */       JCTree.JCMethodDecl localJCMethodDecl = this.make.MethodDef(localMethodSymbol, null);
/*      */       try {
/* 1945 */         localJCMethodDecl.body = classDollarSymBody(paramDiagnosticPosition, localJCMethodDecl);
/*      */       } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1947 */         localJCMethodDecl.body = this.make.Block(0L, List.nil());
/* 1948 */         this.chk.completionError(paramDiagnosticPosition, localCompletionFailure);
/*      */       }
/* 1950 */       JCTree.JCClassDecl localJCClassDecl = classDef(localClassSymbol);
/* 1951 */       localJCClassDecl.defs = localJCClassDecl.defs.prepend(localJCMethodDecl);
/*      */     }
/* 1953 */     return localMethodSymbol;
/*      */   }
/*      */ 
/*      */   JCTree.JCBlock classDollarSymBody(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/* 1958 */     Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/* 1959 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localMethodSymbol.owner;
/*      */     Object localObject2;
/*      */     JCTree.JCBlock localJCBlock;
/* 1966 */     if (this.target.classLiteralsNoInit())
/*      */     {
/* 1969 */       localObject1 = new Symbol.VarSymbol(4104L, this.names
/* 1969 */         .fromString("cl" + this.target
/* 1969 */         .syntheticNameChar()), this.syms.classLoaderType, localClassSymbol);
/*      */ 
/* 1972 */       enterSynthetic(paramDiagnosticPosition, (Symbol)localObject1, localClassSymbol.members());
/*      */ 
/* 1975 */       localObject2 = this.make.VarDef((Symbol.VarSymbol)localObject1, null);
/* 1976 */       localObject3 = classDef(localClassSymbol);
/* 1977 */       ((JCTree.JCClassDecl)localObject3).defs = ((JCTree.JCClassDecl)localObject3).defs.prepend(localObject2);
/*      */ 
/* 1981 */       localObject4 = this.make
/* 1981 */         .NewArray(this.make
/* 1981 */         .Type(localClassSymbol.type), 
/* 1982 */         List.of(this.make
/* 1982 */         .Literal(TypeTag.INT, 
/* 1982 */         Integer.valueOf(0))
/* 1982 */         .setType(this.syms.intType)), null);
/*      */ 
/* 1984 */       ((JCTree.JCNewArray)localObject4).type = new Type.ArrayType(this.types.erasure(localClassSymbol.type), this.syms.arrayClass);
/*      */ 
/* 1989 */       localObject5 = lookupMethod(this.make_pos, this.names.forName, this.types
/* 1990 */         .erasure(this.syms.classType), 
/* 1991 */         List.of(this.syms.stringType, this.syms.booleanType, this.syms.classLoaderType));
/*      */ 
/* 2009 */       JCTree.JCExpression localJCExpression = this.make
/* 1997 */         .Conditional(
/* 1998 */         makeBinary(JCTree.Tag.EQ, this.make
/* 1998 */         .Ident((Symbol)localObject1), 
/* 1998 */         makeNull()), this.make
/* 1999 */         .Assign(this.make
/* 2000 */         .Ident((Symbol)localObject1), 
/* 2001 */         makeCall(
/* 2002 */         makeCall(makeCall((JCTree.JCExpression)localObject4, this.names.getClass, 
/* 2004 */         List.nil()), this.names.getComponentType, 
/* 2006 */         List.nil()), this.names.getClassLoader, 
/* 2008 */         List.nil())).setType(this.syms.classLoaderType), 
/* 2008 */         this.make
/* 2009 */         .Ident((Symbol)localObject1))
/* 2009 */         .setType(this.syms.classLoaderType);
/*      */ 
/* 2012 */       List localList = List.of(this.make.Ident(((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).sym), 
/* 2013 */         makeLit(this.syms.booleanType, 
/* 2013 */         Integer.valueOf(0)), 
/* 2013 */         localJCExpression);
/*      */ 
/* 2016 */       localJCBlock = this.make
/* 2016 */         .Block(0L, 
/* 2016 */         List.of(this.make
/* 2017 */         .Call(this.make
/* 2018 */         .App(this.make
/* 2019 */         .Ident((Symbol)localObject5), 
/* 2019 */         localList))));
/*      */     }
/*      */     else {
/* 2022 */       localObject1 = lookupMethod(this.make_pos, this.names.forName, this.types
/* 2024 */         .erasure(this.syms.classType), 
/* 2025 */         List.of(this.syms.stringType));
/*      */ 
/* 2028 */       localJCBlock = this.make
/* 2028 */         .Block(0L, 
/* 2028 */         List.of(this.make
/* 2029 */         .Call(this.make
/* 2030 */         .App(this.make
/* 2031 */         .QualIdent((Symbol)localObject1), 
/* 2032 */         List.of(this.make
/* 2033 */         .Ident(((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).sym))))));
/*      */     }
/*      */ 
/* 2039 */     Object localObject1 = new Symbol.VarSymbol(4096L, this.make
/* 2039 */       .paramName(1), 
/* 2039 */       this.syms.classNotFoundExceptionType, localMethodSymbol);
/*      */ 
/* 2044 */     if (this.target.hasInitCause())
/*      */     {
/* 2047 */       localObject3 = makeCall(makeNewClass(this.syms.noClassDefFoundErrorType, 
/* 2048 */         List.nil()), this.names.initCause, 
/* 2050 */         List.of(this.make
/* 2050 */         .Ident((Symbol)localObject1)));
/*      */ 
/* 2051 */       localObject2 = this.make.Throw((JCTree.JCExpression)localObject3);
/*      */     }
/*      */     else {
/* 2054 */       localObject3 = lookupMethod(this.make_pos, this.names.getMessage, this.syms.classNotFoundExceptionType, 
/* 2057 */         List.nil());
/*      */ 
/* 2060 */       localObject2 = this.make
/* 2060 */         .Throw(makeNewClass(this.syms.noClassDefFoundErrorType, 
/* 2061 */         List.of(this.make
/* 2061 */         .App(this.make
/* 2061 */         .Select(this.make
/* 2061 */         .Ident((Symbol)localObject1), 
/* 2061 */         (Symbol)localObject3), 
/* 2063 */         List.nil()))));
/*      */     }
/*      */ 
/* 2067 */     Object localObject3 = this.make.Block(0L, List.of(localObject2));
/*      */ 
/* 2070 */     Object localObject4 = this.make.Catch(this.make.VarDef((Symbol.VarSymbol)localObject1, null), (JCTree.JCBlock)localObject3);
/*      */ 
/* 2074 */     Object localObject5 = this.make.Try(localJCBlock, 
/* 2075 */       List.of(localObject4), 
/* 2075 */       null);
/*      */ 
/* 2077 */     return this.make.Block(0L, List.of(localObject5));
/*      */   }
/*      */ 
/*      */   private JCTree.JCMethodInvocation makeCall(JCTree.JCExpression paramJCExpression, Name paramName, List<JCTree.JCExpression> paramList)
/*      */   {
/* 2082 */     Assert.checkNonNull(paramJCExpression.type);
/* 2083 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(this.make_pos, paramName, paramJCExpression.type, 
/* 2084 */       TreeInfo.types(paramList));
/*      */ 
/* 2085 */     return this.make.App(this.make.Select(paramJCExpression, localMethodSymbol), paramList);
/*      */   }
/*      */ 
/*      */   private Name cacheName(String paramString)
/*      */   {
/* 2092 */     StringBuilder localStringBuilder = new StringBuilder();
/* 2093 */     if (paramString.startsWith("[")) {
/* 2094 */       localStringBuilder = localStringBuilder.append("array");
/* 2095 */       while (paramString.startsWith("[")) {
/* 2096 */         localStringBuilder = localStringBuilder.append(this.target.syntheticNameChar());
/* 2097 */         paramString = paramString.substring(1);
/*      */       }
/* 2099 */       if (paramString.startsWith("L"))
/* 2100 */         paramString = paramString.substring(0, paramString.length() - 1);
/*      */     }
/*      */     else {
/* 2103 */       localStringBuilder = localStringBuilder.append("class" + this.target.syntheticNameChar());
/*      */     }
/* 2105 */     localStringBuilder = localStringBuilder.append(paramString.replace('.', this.target.syntheticNameChar()));
/* 2106 */     return this.names.fromString(localStringBuilder.toString());
/*      */   }
/*      */ 
/*      */   private Symbol.VarSymbol cacheSym(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString)
/*      */   {
/* 2115 */     Symbol.ClassSymbol localClassSymbol = outerCacheClass();
/* 2116 */     Name localName = cacheName(paramString);
/*      */ 
/* 2118 */     Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)lookupSynthetic(localName, localClassSymbol
/* 2118 */       .members());
/* 2119 */     if (localVarSymbol == null)
/*      */     {
/* 2121 */       localVarSymbol = new Symbol.VarSymbol(4104L, localName, this.types
/* 2121 */         .erasure(this.syms.classType), 
/* 2121 */         localClassSymbol);
/* 2122 */       enterSynthetic(paramDiagnosticPosition, localVarSymbol, localClassSymbol.members());
/*      */ 
/* 2124 */       JCTree.JCVariableDecl localJCVariableDecl = this.make.VarDef(localVarSymbol, null);
/* 2125 */       JCTree.JCClassDecl localJCClassDecl = classDef(localClassSymbol);
/* 2126 */       localJCClassDecl.defs = localJCClassDecl.defs.prepend(localJCVariableDecl);
/*      */     }
/* 2128 */     return localVarSymbol;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression classOf(JCTree paramJCTree)
/*      */   {
/* 2135 */     return classOfType(paramJCTree.type, paramJCTree.pos());
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression classOfType(Type paramType, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2139 */     switch (7.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/* 2143 */       Symbol.ClassSymbol localClassSymbol = this.types.boxedClass(paramType);
/*      */ 
/* 2145 */       Symbol localSymbol = this.rs
/* 2145 */         .accessBase(this.rs
/* 2146 */         .findIdentInType(this.attrEnv, localClassSymbol.type, this.names.TYPE, 4), 
/* 2146 */         paramDiagnosticPosition, localClassSymbol.type, this.names.TYPE, true);
/*      */ 
/* 2148 */       if (localSymbol.kind == 4)
/* 2149 */         ((Symbol.VarSymbol)localSymbol).getConstValue();
/* 2150 */       return this.make.QualIdent(localSymbol);
/*      */     case 10:
/*      */     case 11:
/* 2152 */       if (this.target.hasClassLiterals()) {
/* 2153 */         localObject = new Symbol.VarSymbol(25L, this.names._class, this.syms.classType, paramType.tsym);
/*      */ 
/* 2156 */         return make_at(paramDiagnosticPosition).Select(this.make.Type(paramType), (Symbol)localObject);
/*      */       }
/*      */ 
/* 2163 */       Object localObject = this.writer
/* 2163 */         .xClassName(paramType)
/* 2163 */         .toString().replace('/', '.');
/* 2164 */       Symbol.VarSymbol localVarSymbol = cacheSym(paramDiagnosticPosition, (String)localObject);
/*      */ 
/* 2174 */       return make_at(paramDiagnosticPosition).Conditional(
/* 2166 */         makeBinary(JCTree.Tag.EQ, this.make
/* 2166 */         .Ident(localVarSymbol), 
/* 2166 */         makeNull()), this.make
/* 2167 */         .Assign(this.make
/* 2168 */         .Ident(localVarSymbol), 
/* 2168 */         this.make
/* 2169 */         .App(this.make
/* 2170 */         .Ident(classDollarSym(paramDiagnosticPosition)), 
/* 2171 */         List.of(this.make
/* 2171 */         .Literal(TypeTag.CLASS, localObject)
/* 2172 */         .setType(this.syms.stringType))))
/* 2173 */         .setType(this.types
/* 2173 */         .erasure(this.syms.classType)), 
/* 2173 */         this.make
/* 2174 */         .Ident(localVarSymbol))
/* 2174 */         .setType(this.types
/* 2174 */         .erasure(this.syms.classType));
/*      */     }
/*      */ 
/* 2176 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   private Symbol.ClassSymbol assertionsDisabledClass()
/*      */   {
/* 2189 */     if (this.assertionsDisabledClassCache != null) return this.assertionsDisabledClassCache;
/*      */ 
/* 2191 */     this.assertionsDisabledClassCache = makeEmptyClass(4104L, this.outermostClassDef.sym).sym;
/*      */ 
/* 2193 */     return this.assertionsDisabledClassCache;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression assertFlagTest(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2204 */     Symbol.ClassSymbol localClassSymbol1 = this.outermostClassDef.sym;
/*      */ 
/* 2208 */     Symbol.ClassSymbol localClassSymbol2 = !this.currentClass.isInterface() ? this.currentClass : 
/* 2208 */       assertionsDisabledClass();
/*      */ 
/* 2211 */     Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)lookupSynthetic(this.dollarAssertionsDisabled, localClassSymbol2
/* 2212 */       .members());
/* 2213 */     if (localVarSymbol == null) {
/* 2214 */       localVarSymbol = new Symbol.VarSymbol(4120L, this.dollarAssertionsDisabled, this.syms.booleanType, localClassSymbol2);
/*      */ 
/* 2219 */       enterSynthetic(paramDiagnosticPosition, localVarSymbol, localClassSymbol2.members());
/* 2220 */       Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramDiagnosticPosition, this.names.desiredAssertionStatus, this.types
/* 2222 */         .erasure(this.syms.classType), 
/* 2223 */         List.nil());
/* 2224 */       JCTree.JCClassDecl localJCClassDecl1 = classDef(localClassSymbol2);
/* 2225 */       make_at(localJCClassDecl1.pos());
/* 2226 */       JCTree.JCUnary localJCUnary = makeUnary(JCTree.Tag.NOT, this.make.App(this.make.Select(
/* 2227 */         classOfType(this.types
/* 2227 */         .erasure(localClassSymbol1.type), 
/* 2227 */         localJCClassDecl1
/* 2228 */         .pos()), localMethodSymbol)));
/*      */ 
/* 2230 */       JCTree.JCVariableDecl localJCVariableDecl = this.make.VarDef(localVarSymbol, localJCUnary);
/*      */ 
/* 2232 */       localJCClassDecl1.defs = localJCClassDecl1.defs.prepend(localJCVariableDecl);
/*      */ 
/* 2234 */       if (this.currentClass.isInterface())
/*      */       {
/* 2237 */         JCTree.JCClassDecl localJCClassDecl2 = classDef(this.currentClass);
/* 2238 */         make_at(localJCClassDecl2.pos());
/* 2239 */         JCTree.JCIf localJCIf = this.make.If(this.make.QualIdent(localVarSymbol), this.make.Skip(), null);
/* 2240 */         JCTree.JCBlock localJCBlock = this.make.Block(8L, List.of(localJCIf));
/* 2241 */         localJCClassDecl2.defs = localJCClassDecl2.defs.prepend(localJCBlock);
/*      */       }
/*      */     }
/* 2244 */     make_at(paramDiagnosticPosition);
/* 2245 */     return makeUnary(JCTree.Tag.NOT, this.make.Ident(localVarSymbol));
/*      */   }
/*      */ 
/*      */   JCTree abstractRval(JCTree paramJCTree, Type paramType, TreeBuilder paramTreeBuilder)
/*      */   {
/* 2273 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2274 */     switch (paramJCTree.getTag()) {
/*      */     case LITERAL:
/* 2276 */       return paramTreeBuilder.build(paramJCTree);
/*      */     case IDENT:
/* 2278 */       localObject = (JCTree.JCIdent)paramJCTree;
/* 2279 */       if (((((JCTree.JCIdent)localObject).sym.flags() & 0x10) != 0L) && (((JCTree.JCIdent)localObject).sym.owner.kind == 16)) {
/* 2280 */         return paramTreeBuilder.build(paramJCTree);
/*      */       }
/*      */       break;
/*      */     }
/* 2284 */     Object localObject = new Symbol.VarSymbol(4112L, this.names
/* 2284 */       .fromString(this.target
/* 2285 */       .syntheticNameChar() + "" + paramJCTree
/* 2286 */       .hashCode()), paramType, this.currentMethodSym);
/*      */ 
/* 2289 */     paramJCTree = convert(paramJCTree, paramType);
/* 2290 */     JCTree.JCVariableDecl localJCVariableDecl = this.make.VarDef((Symbol.VarSymbol)localObject, (JCTree.JCExpression)paramJCTree);
/* 2291 */     JCTree localJCTree = paramTreeBuilder.build(this.make.Ident((Symbol)localObject));
/* 2292 */     JCTree.LetExpr localLetExpr = this.make.LetExpr(localJCVariableDecl, localJCTree);
/* 2293 */     localLetExpr.type = localJCTree.type;
/* 2294 */     return localLetExpr;
/*      */   }
/*      */ 
/*      */   JCTree abstractRval(JCTree paramJCTree, TreeBuilder paramTreeBuilder)
/*      */   {
/* 2299 */     return abstractRval(paramJCTree, paramJCTree.type, paramTreeBuilder);
/*      */   }
/*      */ 
/*      */   JCTree abstractLval(JCTree paramJCTree, final TreeBuilder paramTreeBuilder)
/*      */   {
/* 2308 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/*      */     Object localObject;
/* 2309 */     switch (7.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */     case 6:
/* 2311 */       return paramTreeBuilder.build(paramJCTree);
/*      */     case 7:
/* 2313 */       localObject = (JCTree.JCFieldAccess)paramJCTree;
/* 2314 */       JCTree.JCExpression localJCExpression = TreeInfo.skipParens(((JCTree.JCFieldAccess)localObject).selected);
/* 2315 */       Symbol localSymbol = TreeInfo.symbol(((JCTree.JCFieldAccess)localObject).selected);
/* 2316 */       if ((localSymbol != null) && (localSymbol.kind == 2)) return paramTreeBuilder.build(paramJCTree);
/* 2317 */       return abstractRval(((JCTree.JCFieldAccess)localObject).selected, new TreeBuilder() {
/*      */         public JCTree build(JCTree paramAnonymousJCTree) {
/* 2319 */           return paramTreeBuilder.build(Lower.this.make.Select((JCTree.JCExpression)paramAnonymousJCTree, this.val$s.sym));
/*      */         }
/*      */ 
/*      */       });
/*      */     case 8:
/* 2324 */       localObject = (JCTree.JCArrayAccess)paramJCTree;
/* 2325 */       return abstractRval(((JCTree.JCArrayAccess)localObject).indexed, new TreeBuilder() {
/*      */         public JCTree build(final JCTree paramAnonymousJCTree) {
/* 2327 */           return Lower.this.abstractRval(this.val$i.index, Lower.this.syms.intType, new Lower.TreeBuilder() {
/*      */             public JCTree build(JCTree paramAnonymous2JCTree) {
/* 2329 */               JCTree.JCArrayAccess localJCArrayAccess = Lower.this.make.Indexed((JCTree.JCExpression)paramAnonymousJCTree, (JCTree.JCExpression)paramAnonymous2JCTree);
/*      */ 
/* 2331 */               localJCArrayAccess.setType(Lower.3.this.val$i.type);
/* 2332 */               return Lower.3.this.val$builder.build(localJCArrayAccess);
/*      */             }
/*      */           });
/*      */         }
/*      */ 
/*      */       });
/*      */     case 9:
/* 2339 */       return abstractLval(((JCTree.JCTypeCast)paramJCTree).expr, paramTreeBuilder);
/*      */     }
/*      */ 
/* 2342 */     throw new AssertionError(paramJCTree);
/*      */   }
/*      */ 
/*      */   JCTree makeComma(JCTree paramJCTree1, final JCTree paramJCTree2)
/*      */   {
/* 2347 */     return abstractRval(paramJCTree1, new TreeBuilder() {
/*      */       public JCTree build(JCTree paramAnonymousJCTree) {
/* 2349 */         return paramJCTree2;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> T translate(T paramT)
/*      */   {
/* 2367 */     if (paramT == null) {
/* 2368 */       return null;
/*      */     }
/* 2370 */     make_at(paramT.pos());
/* 2371 */     JCTree localJCTree = super.translate(paramT);
/* 2372 */     if ((this.endPosTable != null) && (localJCTree != paramT)) {
/* 2373 */       this.endPosTable.replaceTree(paramT, localJCTree);
/*      */     }
/* 2375 */     return localJCTree;
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> T translate(T paramT, Type paramType)
/*      */   {
/* 2382 */     return paramT == null ? null : boxIfNeeded(translate(paramT), paramType);
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> T translate(T paramT, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2388 */     JCTree.JCExpression localJCExpression = this.enclOp;
/* 2389 */     this.enclOp = paramJCExpression;
/* 2390 */     JCTree localJCTree = translate(paramT);
/* 2391 */     this.enclOp = localJCExpression;
/* 2392 */     return localJCTree;
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2398 */     JCTree.JCExpression localJCExpression = this.enclOp;
/* 2399 */     this.enclOp = paramJCExpression;
/* 2400 */     List localList = translate(paramList);
/* 2401 */     this.enclOp = localJCExpression;
/* 2402 */     return localList;
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, Type paramType)
/*      */   {
/* 2408 */     if (paramList == null) return null;
/* 2409 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 2410 */       ((List)localObject).head = translate((JCTree)((List)localObject).head, paramType);
/* 2411 */     return paramList;
/*      */   }
/*      */ 
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 2415 */     if (needPackageInfoClass(paramJCCompilationUnit)) {
/* 2416 */       Name localName = this.names.package_info;
/* 2417 */       long l = 1536L;
/* 2418 */       if (this.target.isPackageInfoSynthetic())
/*      */       {
/* 2420 */         l |= 4096L;
/*      */       }
/* 2422 */       JCTree.JCClassDecl localJCClassDecl = this.make
/* 2422 */         .ClassDef(this.make
/* 2422 */         .Modifiers(l, paramJCCompilationUnit.packageAnnotations), 
/* 2422 */         localName, 
/* 2424 */         List.nil(), null, 
/* 2425 */         List.nil(), List.nil());
/* 2426 */       Symbol.ClassSymbol localClassSymbol = paramJCCompilationUnit.packge.package_info;
/* 2427 */       localClassSymbol.flags_field |= l;
/* 2428 */       localClassSymbol.setAttributes(paramJCCompilationUnit.packge);
/* 2429 */       Type.ClassType localClassType = (Type.ClassType)localClassSymbol.type;
/* 2430 */       localClassType.supertype_field = this.syms.objectType;
/* 2431 */       localClassType.interfaces_field = List.nil();
/* 2432 */       localJCClassDecl.sym = localClassSymbol;
/*      */ 
/* 2434 */       this.translated.append(localJCClassDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean needPackageInfoClass(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 2439 */     switch (7.$SwitchMap$com$sun$tools$javac$main$Option$PkgInfo[this.pkginfoOpt.ordinal()]) {
/*      */     case 1:
/* 2441 */       return true;
/*      */     case 2:
/* 2443 */       return paramJCCompilationUnit.packageAnnotations.nonEmpty();
/*      */     case 3:
/* 2446 */       for (Attribute.Compound localCompound : paramJCCompilationUnit.packge.getDeclarationAttributes()) {
/* 2447 */         Attribute.RetentionPolicy localRetentionPolicy = this.types.getRetention(localCompound);
/* 2448 */         if (localRetentionPolicy != Attribute.RetentionPolicy.SOURCE)
/* 2449 */           return true;
/*      */       }
/* 2451 */       return false;
/*      */     }
/* 2453 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 2457 */     Env localEnv = this.attrEnv;
/* 2458 */     Symbol.ClassSymbol localClassSymbol = this.currentClass;
/* 2459 */     Symbol.MethodSymbol localMethodSymbol = this.currentMethodSym;
/*      */ 
/* 2461 */     this.currentClass = paramJCClassDecl.sym;
/* 2462 */     this.currentMethodSym = null;
/* 2463 */     this.attrEnv = this.typeEnvs.remove(this.currentClass);
/* 2464 */     if (this.attrEnv == null) {
/* 2465 */       this.attrEnv = localEnv;
/*      */     }
/* 2467 */     this.classdefs.put(this.currentClass, paramJCClassDecl);
/*      */ 
/* 2469 */     this.proxies = this.proxies.dup(this.currentClass);
/* 2470 */     List localList1 = this.outerThisStack;
/*      */ 
/* 2473 */     if (((paramJCClassDecl.mods.flags & 0x4000) != 0L) && 
/* 2474 */       ((this.types
/* 2474 */       .supertype(this.currentClass.type).tsym
/* 2474 */       .flags() & 0x4000) == 0L)) {
/* 2475 */       visitEnumDef(paramJCClassDecl);
/*      */     }
/*      */ 
/* 2479 */     JCTree.JCVariableDecl localJCVariableDecl = null;
/* 2480 */     if (this.currentClass.hasOuterInstance()) {
/* 2481 */       localJCVariableDecl = outerThisDef(paramJCClassDecl.pos, this.currentClass);
/*      */     }
/*      */ 
/* 2484 */     List localList2 = freevarDefs(paramJCClassDecl.pos, 
/* 2485 */       freevars(this.currentClass), 
/* 2485 */       this.currentClass);
/*      */ 
/* 2488 */     paramJCClassDecl.extending = ((JCTree.JCExpression)translate(paramJCClassDecl.extending));
/* 2489 */     paramJCClassDecl.implementing = translate(paramJCClassDecl.implementing);
/*      */ 
/* 2491 */     if (this.currentClass.isLocal()) {
/* 2492 */       localObject = this.currentClass.owner.enclClass();
/* 2493 */       if (((Symbol.ClassSymbol)localObject).trans_local == null) {
/* 2494 */         ((Symbol.ClassSymbol)localObject).trans_local = List.nil();
/*      */       }
/* 2496 */       ((Symbol.ClassSymbol)localObject).trans_local = ((Symbol.ClassSymbol)localObject).trans_local.prepend(this.currentClass);
/*      */     }
/*      */ 
/* 2502 */     Object localObject = List.nil();
/* 2503 */     while (paramJCClassDecl.defs != localObject) {
/* 2504 */       localList3 = paramJCClassDecl.defs;
/* 2505 */       for (List localList4 = localList3; (localList4.nonEmpty()) && (localList4 != localObject); localList4 = localList4.tail) {
/* 2506 */         JCTree localJCTree = this.outermostMemberDef;
/* 2507 */         if (localJCTree == null) this.outermostMemberDef = ((JCTree)localList4.head);
/* 2508 */         localList4.head = translate((JCTree)localList4.head);
/* 2509 */         this.outermostMemberDef = localJCTree;
/*      */       }
/* 2511 */       localObject = localList3;
/*      */     }
/*      */ 
/* 2515 */     if ((paramJCClassDecl.mods.flags & 0x4) != 0L) paramJCClassDecl.mods.flags |= 1L;
/* 2516 */     paramJCClassDecl.mods.flags &= 32273L;
/*      */ 
/* 2519 */     paramJCClassDecl.name = Convert.shortName(this.currentClass.flatName());
/*      */ 
/* 2523 */     for (List localList3 = localList2; localList3.nonEmpty(); localList3 = localList3.tail) {
/* 2524 */       paramJCClassDecl.defs = paramJCClassDecl.defs.prepend(localList3.head);
/* 2525 */       enterSynthetic(paramJCClassDecl.pos(), ((JCTree.JCVariableDecl)localList3.head).sym, this.currentClass.members());
/*      */     }
/* 2527 */     if (this.currentClass.hasOuterInstance()) {
/* 2528 */       paramJCClassDecl.defs = paramJCClassDecl.defs.prepend(localJCVariableDecl);
/* 2529 */       enterSynthetic(paramJCClassDecl.pos(), localJCVariableDecl.sym, this.currentClass.members());
/*      */     }
/*      */ 
/* 2532 */     this.proxies = this.proxies.leave();
/* 2533 */     this.outerThisStack = localList1;
/*      */ 
/* 2536 */     this.translated.append(paramJCClassDecl);
/*      */ 
/* 2538 */     this.attrEnv = localEnv;
/* 2539 */     this.currentClass = localClassSymbol;
/* 2540 */     this.currentMethodSym = localMethodSymbol;
/*      */ 
/* 2543 */     this.result = make_at(paramJCClassDecl.pos()).Block(4096L, List.nil());
/*      */   }
/*      */ 
/*      */   private void visitEnumDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/* 2548 */     make_at(paramJCClassDecl.pos());
/*      */ 
/* 2551 */     if (paramJCClassDecl.extending == null) {
/* 2552 */       paramJCClassDecl.extending = this.make.Type(this.types.supertype(paramJCClassDecl.type));
/*      */     }
/*      */ 
/* 2557 */     JCTree.JCExpression localJCExpression = classOfType(paramJCClassDecl.sym.type, paramJCClassDecl.pos())
/* 2557 */       .setType(this.types
/* 2557 */       .erasure(this.syms.classType));
/*      */ 
/* 2560 */     int i = 0;
/* 2561 */     ListBuffer localListBuffer1 = new ListBuffer();
/* 2562 */     ListBuffer localListBuffer2 = new ListBuffer();
/* 2563 */     ListBuffer localListBuffer3 = new ListBuffer();
/* 2564 */     for (Object localObject1 = paramJCClassDecl.defs; 
/* 2565 */       ((List)localObject1).nonEmpty(); 
/* 2566 */       localObject1 = ((List)localObject1).tail) {
/* 2567 */       if ((((JCTree)((List)localObject1).head).hasTag(JCTree.Tag.VARDEF)) && ((((JCTree.JCVariableDecl)((List)localObject1).head).mods.flags & 0x4000) != 0L)) {
/* 2568 */         localObject2 = (JCTree.JCVariableDecl)((List)localObject1).head;
/* 2569 */         visitEnumConstantDef((JCTree.JCVariableDecl)localObject2, i++);
/* 2570 */         localListBuffer1.append(this.make.QualIdent(((JCTree.JCVariableDecl)localObject2).sym));
/* 2571 */         localListBuffer2.append(localObject2);
/*      */       } else {
/* 2573 */         localListBuffer3.append(((List)localObject1).head);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2578 */     localObject1 = this.names.fromString(this.target.syntheticNameChar() + "VALUES");
/* 2579 */     while (paramJCClassDecl.sym.members().lookup((Name)localObject1).scope != null)
/* 2580 */       localObject1 = this.names.fromString(localObject1 + "" + this.target.syntheticNameChar());
/* 2581 */     Object localObject2 = new Type.ArrayType(this.types.erasure(paramJCClassDecl.type), this.syms.arrayClass);
/* 2582 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(4122L, (Name)localObject1, (Type)localObject2, paramJCClassDecl.type.tsym);
/*      */ 
/* 2586 */     JCTree.JCNewArray localJCNewArray = this.make.NewArray(this.make.Type(this.types.erasure(paramJCClassDecl.type)), 
/* 2587 */       List.nil(), localListBuffer1
/* 2588 */       .toList());
/* 2589 */     localJCNewArray.type = ((Type)localObject2);
/* 2590 */     localListBuffer2.append(this.make.VarDef(localVarSymbol, localJCNewArray));
/* 2591 */     paramJCClassDecl.sym.members().enter(localVarSymbol);
/*      */ 
/* 2593 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramJCClassDecl.pos(), this.names.values, paramJCClassDecl.type, 
/* 2594 */       List.nil());
/*      */     List localList;
/* 2596 */     if (useClone())
/*      */     {
/* 2599 */       localObject3 = this.make
/* 2599 */         .TypeCast(localMethodSymbol.type
/* 2599 */         .getReturnType(), this.make
/* 2600 */         .App(this.make
/* 2600 */         .Select(this.make
/* 2600 */         .Ident(localVarSymbol), 
/* 2600 */         this.syms.arrayCloneMethod)));
/*      */ 
/* 2602 */       localList = List.of(this.make.Return((JCTree.JCExpression)localObject3));
/*      */     }
/*      */     else {
/* 2605 */       localObject3 = this.names.fromString(this.target.syntheticNameChar() + "result");
/* 2606 */       while (paramJCClassDecl.sym.members().lookup((Name)localObject3).scope != null)
/* 2607 */         localObject3 = this.names.fromString(localObject3 + "" + this.target.syntheticNameChar());
/* 2608 */       localObject4 = new Symbol.VarSymbol(4112L, (Name)localObject3, (Type)localObject2, localMethodSymbol);
/*      */ 
/* 2612 */       localObject5 = this.make.NewArray(this.make.Type(this.types.erasure(paramJCClassDecl.type)), 
/* 2613 */         List.of(this.make
/* 2613 */         .Select(this.make
/* 2613 */         .Ident(localVarSymbol), 
/* 2613 */         this.syms.lengthVar)), null);
/*      */ 
/* 2615 */       ((JCTree.JCNewArray)localObject5).type = ((Type)localObject2);
/* 2616 */       localObject6 = this.make.VarDef((Symbol.VarSymbol)localObject4, (JCTree.JCExpression)localObject5);
/*      */ 
/* 2619 */       if (this.systemArraycopyMethod == null) {
/* 2620 */         this.systemArraycopyMethod = new Symbol.MethodSymbol(9L, this.names
/* 2622 */           .fromString("arraycopy"), 
/* 2622 */           new Type.MethodType(
/* 2623 */           List.of(this.syms.objectType, this.syms.intType, this.syms.objectType, new Type[] { this.syms.intType, this.syms.intType }), 
/* 2623 */           this.syms.voidType, 
/* 2629 */           List.nil(), this.syms.methodClass), this.syms.systemType.tsym);
/*      */       }
/*      */ 
/* 2634 */       localObject7 = this.make
/* 2634 */         .Exec(this.make
/* 2634 */         .App(this.make
/* 2634 */         .Select(this.make
/* 2634 */         .Ident(this.syms.systemType.tsym), 
/* 2634 */         this.systemArraycopyMethod), 
/* 2636 */         List.of(this.make
/* 2636 */         .Ident(localVarSymbol), 
/* 2636 */         this.make.Literal(Integer.valueOf(0)), this.make
/* 2637 */         .Ident((Symbol)localObject4), 
/* 2637 */         new JCTree.JCExpression[] { this.make.Literal(Integer.valueOf(0)), this.make
/* 2638 */         .Select(this.make
/* 2638 */         .Ident(localVarSymbol), 
/* 2638 */         this.syms.lengthVar) })));
/*      */ 
/* 2641 */       localObject8 = this.make.Return(this.make.Ident((Symbol)localObject4));
/* 2642 */       localList = List.of(localObject6, localObject7, localObject8);
/*      */     }
/*      */ 
/* 2646 */     Object localObject3 = this.make
/* 2646 */       .MethodDef((Symbol.MethodSymbol)localMethodSymbol, this.make
/* 2646 */       .Block(0L, localList));
/*      */ 
/* 2648 */     localListBuffer2.append(localObject3);
/*      */ 
/* 2650 */     if (this.debugLower) {
/* 2651 */       System.err.println(paramJCClassDecl.sym + ".valuesDef = " + localObject3);
/*      */     }
/*      */ 
/* 2661 */     Object localObject4 = lookupMethod(paramJCClassDecl.pos(), this.names.valueOf, paramJCClassDecl.sym.type, 
/* 2664 */       List.of(this.syms.stringType));
/*      */ 
/* 2665 */     Assert.check((((Symbol.MethodSymbol)localObject4).flags() & 0x8) != 0L);
/* 2666 */     Object localObject5 = (Symbol.VarSymbol)((Symbol.MethodSymbol)localObject4).params.head;
/* 2667 */     Object localObject6 = this.make.Ident((Symbol)localObject5);
/*      */ 
/* 2669 */     Object localObject7 = this.make
/* 2669 */       .Return(this.make
/* 2669 */       .TypeCast(paramJCClassDecl.sym.type, 
/* 2670 */       makeCall(this.make
/* 2670 */       .Ident(this.syms.enumSym), 
/* 2670 */       this.names.valueOf, 
/* 2672 */       List.of(localJCExpression, localObject6))));
/*      */ 
/* 2673 */     Object localObject8 = this.make.MethodDef((Symbol.MethodSymbol)localObject4, this.make
/* 2674 */       .Block(0L, 
/* 2674 */       List.of(localObject7)));
/*      */ 
/* 2675 */     ((JCTree.JCIdent)localObject6).sym = ((JCTree.JCVariableDecl)((JCTree.JCMethodDecl)localObject8).params.head).sym;
/* 2676 */     if (this.debugLower)
/* 2677 */       System.err.println(paramJCClassDecl.sym + ".valueOf = " + localObject8);
/* 2678 */     localListBuffer2.append(localObject8);
/*      */ 
/* 2680 */     localListBuffer2.appendList(localListBuffer3.toList());
/* 2681 */     paramJCClassDecl.defs = localListBuffer2.toList();
/*      */   }
/*      */ 
/*      */   private boolean useClone()
/*      */   {
/*      */     try {
/* 2687 */       Scope.Entry localEntry = this.syms.objectType.tsym.members().lookup(this.names.clone);
/* 2688 */       return localEntry.sym != null;
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*      */     }
/* 2691 */     return false;
/*      */   }
/*      */ 
/*      */   private void visitEnumConstantDef(JCTree.JCVariableDecl paramJCVariableDecl, int paramInt)
/*      */   {
/* 2697 */     JCTree.JCNewClass localJCNewClass = (JCTree.JCNewClass)paramJCVariableDecl.init;
/* 2698 */     localJCNewClass.args = localJCNewClass.args
/* 2699 */       .prepend(makeLit(this.syms.intType, 
/* 2699 */       Integer.valueOf(paramInt)))
/* 2700 */       .prepend(makeLit(this.syms.stringType, paramJCVariableDecl.name
/* 2700 */       .toString()));
/*      */   }
/*      */ 
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 2704 */     if ((paramJCMethodDecl.name == this.names.init) && ((this.currentClass.flags_field & 0x4000) != 0L))
/*      */     {
/* 2708 */       localObject1 = make_at(paramJCMethodDecl.pos())
/* 2708 */         .Param(this.names
/* 2708 */         .fromString(this.target
/* 2708 */         .syntheticNameChar() + "enum" + this.target
/* 2709 */         .syntheticNameChar() + "name"), this.syms.stringType, paramJCMethodDecl.sym);
/*      */ 
/* 2711 */       ((JCTree.JCVariableDecl)localObject1).mods.flags |= 4096L; ((JCTree.JCVariableDecl)localObject1).sym.flags_field |= 4096L;
/*      */ 
/* 2713 */       localObject2 = this.make
/* 2713 */         .Param(this.names
/* 2713 */         .fromString(this.target
/* 2713 */         .syntheticNameChar() + "enum" + this.target
/* 2714 */         .syntheticNameChar() + "ordinal"), this.syms.intType, paramJCMethodDecl.sym);
/*      */ 
/* 2717 */       ((JCTree.JCVariableDecl)localObject2).mods.flags |= 4096L; ((JCTree.JCVariableDecl)localObject2).sym.flags_field |= 4096L;
/*      */ 
/* 2719 */       paramJCMethodDecl.params = paramJCMethodDecl.params.prepend(localObject2).prepend(localObject1);
/*      */ 
/* 2721 */       Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/* 2722 */       localMethodSymbol.extraParams = localMethodSymbol.extraParams.prepend(((JCTree.JCVariableDecl)localObject2).sym);
/* 2723 */       localMethodSymbol.extraParams = localMethodSymbol.extraParams.prepend(((JCTree.JCVariableDecl)localObject1).sym);
/* 2724 */       Type localType = localMethodSymbol.erasure(this.types);
/* 2725 */       localMethodSymbol.erasure_field = new Type.MethodType(localType
/* 2726 */         .getParameterTypes().prepend(this.syms.intType).prepend(this.syms.stringType), localType
/* 2727 */         .getReturnType(), localType
/* 2728 */         .getThrownTypes(), this.syms.methodClass);
/*      */     }
/*      */ 
/* 2732 */     Object localObject1 = this.currentMethodDef;
/* 2733 */     Object localObject2 = this.currentMethodSym;
/*      */     try {
/* 2735 */       this.currentMethodDef = paramJCMethodDecl;
/* 2736 */       this.currentMethodSym = paramJCMethodDecl.sym;
/* 2737 */       visitMethodDefInternal(paramJCMethodDecl);
/*      */     } finally {
/* 2739 */       this.currentMethodDef = ((JCTree.JCMethodDecl)localObject1);
/* 2740 */       this.currentMethodSym = ((Symbol.MethodSymbol)localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void visitMethodDefInternal(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*      */     Object localObject1;
/* 2745 */     if ((paramJCMethodDecl.name == this.names.init) && (
/* 2746 */       (this.currentClass
/* 2746 */       .isInner()) || (this.currentClass.isLocal())))
/*      */     {
/* 2748 */       localObject1 = paramJCMethodDecl.sym;
/*      */ 
/* 2752 */       this.proxies = this.proxies.dup((Symbol)localObject1);
/* 2753 */       List localList1 = this.outerThisStack;
/* 2754 */       List localList2 = freevars(this.currentClass);
/* 2755 */       JCTree.JCVariableDecl localJCVariableDecl = null;
/* 2756 */       if (this.currentClass.hasOuterInstance())
/* 2757 */         localJCVariableDecl = outerThisDef(paramJCMethodDecl.pos, (Symbol.MethodSymbol)localObject1);
/* 2758 */       List localList3 = freevarDefs(paramJCMethodDecl.pos, localList2, (Symbol)localObject1, 8589934592L);
/*      */ 
/* 2761 */       paramJCMethodDecl.restype = ((JCTree.JCExpression)translate(paramJCMethodDecl.restype));
/* 2762 */       paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/* 2763 */       paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown);
/*      */ 
/* 2766 */       if (paramJCMethodDecl.body == null) {
/* 2767 */         this.result = paramJCMethodDecl;
/* 2768 */         return;
/*      */       }
/*      */ 
/* 2773 */       paramJCMethodDecl.params = paramJCMethodDecl.params.appendList(localList3);
/* 2774 */       if (this.currentClass.hasOuterInstance()) {
/* 2775 */         paramJCMethodDecl.params = paramJCMethodDecl.params.prepend(localJCVariableDecl);
/*      */       }
/*      */ 
/* 2780 */       JCTree.JCStatement localJCStatement = (JCTree.JCStatement)translate((JCTree)paramJCMethodDecl.body.stats.head);
/*      */ 
/* 2782 */       List localList4 = List.nil();
/* 2783 */       if (localList2.nonEmpty()) {
/* 2784 */         localList5 = List.nil();
/* 2785 */         for (Object localObject2 = localList2; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/* 2786 */           if (TreeInfo.isInitialConstructor(paramJCMethodDecl)) {
/* 2787 */             Name localName = proxyName(((Symbol.VarSymbol)((List)localObject2).head).name);
/* 2788 */             ((Symbol.MethodSymbol)localObject1).capturedLocals = ((Symbol.MethodSymbol)localObject1).capturedLocals
/* 2789 */               .append(
/* 2790 */               (Symbol.VarSymbol)this.proxies
/* 2790 */               .lookup(localName).sym);
/*      */ 
/* 2791 */             localList4 = localList4.prepend(
/* 2792 */               initField(paramJCMethodDecl.body.pos, localName));
/*      */           }
/*      */ 
/* 2794 */           localList5 = localList5.prepend(((Symbol.VarSymbol)((List)localObject2).head).erasure(this.types));
/*      */         }
/* 2796 */         localObject2 = ((Symbol.MethodSymbol)localObject1).erasure(this.types);
/* 2797 */         ((Symbol.MethodSymbol)localObject1).erasure_field = new Type.MethodType(((Type)localObject2)
/* 2798 */           .getParameterTypes().appendList(localList5), ((Type)localObject2)
/* 2799 */           .getReturnType(), ((Type)localObject2)
/* 2800 */           .getThrownTypes(), this.syms.methodClass);
/*      */       }
/*      */ 
/* 2803 */       if ((this.currentClass.hasOuterInstance()) && 
/* 2804 */         (TreeInfo.isInitialConstructor(paramJCMethodDecl)))
/*      */       {
/* 2806 */         localList4 = localList4.prepend(initOuterThis(paramJCMethodDecl.body.pos));
/*      */       }
/*      */ 
/* 2810 */       this.proxies = this.proxies.leave();
/*      */ 
/* 2814 */       List localList5 = translate(paramJCMethodDecl.body.stats.tail);
/* 2815 */       if (this.target.initializeFieldsBeforeSuper())
/* 2816 */         paramJCMethodDecl.body.stats = localList5.prepend(localJCStatement).prependList(localList4);
/*      */       else {
/* 2818 */         paramJCMethodDecl.body.stats = localList5.prependList(localList4).prepend(localJCStatement);
/*      */       }
/* 2820 */       this.outerThisStack = localList1;
/*      */     } else {
/* 2822 */       localObject1 = this.lambdaTranslationMap;
/*      */       try
/*      */       {
/* 2825 */         this.lambdaTranslationMap = (((paramJCMethodDecl.sym.flags() & 0x1000) != 0L) && 
/* 2826 */           (paramJCMethodDecl.sym.name
/* 2826 */           .startsWith(this.names.lambda)) ? 
/* 2827 */           makeTranslationMap(paramJCMethodDecl) : 
/* 2827 */           null);
/* 2828 */         super.visitMethodDef(paramJCMethodDecl);
/*      */       } finally {
/* 2830 */         this.lambdaTranslationMap = ((Map)localObject1);
/*      */       }
/*      */     }
/* 2833 */     this.result = paramJCMethodDecl;
/*      */   }
/*      */ 
/*      */   private Map<Symbol, Symbol> makeTranslationMap(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 2837 */     HashMap localHashMap = new HashMap();
/* 2838 */     for (JCTree.JCVariableDecl localJCVariableDecl : paramJCMethodDecl.params) {
/* 2839 */       Symbol.VarSymbol localVarSymbol = localJCVariableDecl.sym;
/* 2840 */       if (localVarSymbol != localVarSymbol.baseSymbol()) {
/* 2841 */         localHashMap.put(localVarSymbol.baseSymbol(), localVarSymbol);
/*      */       }
/*      */     }
/* 2844 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType)
/*      */   {
/* 2850 */     paramJCAnnotatedType.annotations = List.nil();
/* 2851 */     paramJCAnnotatedType.underlyingType = ((JCTree.JCExpression)translate(paramJCAnnotatedType.underlyingType));
/*      */ 
/* 2853 */     if (paramJCAnnotatedType.type.isAnnotated())
/* 2854 */       paramJCAnnotatedType.type = paramJCAnnotatedType.underlyingType.type.unannotatedType().annotatedType(paramJCAnnotatedType.type.getAnnotationMirrors());
/* 2855 */     else if (paramJCAnnotatedType.underlyingType.type.isAnnotated()) {
/* 2856 */       paramJCAnnotatedType.type = paramJCAnnotatedType.underlyingType.type;
/*      */     }
/* 2858 */     this.result = paramJCAnnotatedType;
/*      */   }
/*      */ 
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 2862 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz);
/* 2863 */     if (paramJCTypeCast.type.isPrimitive() != paramJCTypeCast.expr.type.isPrimitive())
/* 2864 */       paramJCTypeCast.expr = ((JCTree.JCExpression)translate(paramJCTypeCast.expr, paramJCTypeCast.type));
/*      */     else
/* 2866 */       paramJCTypeCast.expr = ((JCTree.JCExpression)translate(paramJCTypeCast.expr));
/* 2867 */     this.result = paramJCTypeCast;
/*      */   }
/*      */ 
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 2871 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramJCNewClass.constructor.owner;
/*      */ 
/* 2874 */     int i = (paramJCNewClass.constructor.owner.flags() & 0x4000) != 0L ? 1 : 0;
/* 2875 */     List localList = paramJCNewClass.constructor.type.getParameterTypes();
/* 2876 */     if (i != 0) localList = localList.prepend(this.syms.intType).prepend(this.syms.stringType);
/* 2877 */     paramJCNewClass.args = boxArgs(localList, paramJCNewClass.args, paramJCNewClass.varargsElement);
/* 2878 */     paramJCNewClass.varargsElement = null;
/*      */ 
/* 2882 */     if (localClassSymbol.isLocal()) {
/* 2883 */       paramJCNewClass.args = paramJCNewClass.args.appendList(loadFreevars(paramJCNewClass.pos(), freevars(localClassSymbol)));
/*      */     }
/*      */ 
/* 2887 */     Symbol localSymbol = accessConstructor(paramJCNewClass.pos(), paramJCNewClass.constructor);
/* 2888 */     if (localSymbol != paramJCNewClass.constructor) {
/* 2889 */       paramJCNewClass.args = paramJCNewClass.args.append(makeNull());
/* 2890 */       paramJCNewClass.constructor = localSymbol;
/*      */     }
/*      */ 
/* 2896 */     if (localClassSymbol.hasOuterInstance())
/*      */     {
/*      */       JCTree.JCExpression localJCExpression;
/* 2898 */       if (paramJCNewClass.encl != null) {
/* 2899 */         localJCExpression = this.attr.makeNullCheck((JCTree.JCExpression)translate(paramJCNewClass.encl));
/* 2900 */         localJCExpression.type = paramJCNewClass.encl.type;
/* 2901 */       } else if (localClassSymbol.isLocal())
/*      */       {
/* 2903 */         localJCExpression = makeThis(paramJCNewClass.pos(), localClassSymbol.type.getEnclosingType().tsym);
/*      */       }
/*      */       else {
/* 2906 */         localJCExpression = makeOwnerThis(paramJCNewClass.pos(), localClassSymbol, false);
/*      */       }
/* 2908 */       paramJCNewClass.args = paramJCNewClass.args.prepend(localJCExpression);
/*      */     }
/* 2910 */     paramJCNewClass.encl = null;
/*      */ 
/* 2914 */     if (paramJCNewClass.def != null) {
/* 2915 */       translate(paramJCNewClass.def);
/* 2916 */       paramJCNewClass.clazz = access(make_at(paramJCNewClass.clazz.pos()).Ident(paramJCNewClass.def.sym));
/* 2917 */       paramJCNewClass.def = null;
/*      */     } else {
/* 2919 */       paramJCNewClass.clazz = access(localClassSymbol, paramJCNewClass.clazz, this.enclOp, false);
/*      */     }
/* 2921 */     this.result = paramJCNewClass;
/*      */   }
/*      */ 
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional)
/*      */   {
/* 2937 */     JCTree.JCExpression localJCExpression = paramJCConditional.cond = (JCTree.JCExpression)translate(paramJCConditional.cond, this.syms.booleanType);
/* 2938 */     if (localJCExpression.type.isTrue()) {
/* 2939 */       this.result = convert(translate(paramJCConditional.truepart, paramJCConditional.type), paramJCConditional.type);
/* 2940 */       addPrunedInfo(localJCExpression);
/* 2941 */     } else if (localJCExpression.type.isFalse()) {
/* 2942 */       this.result = convert(translate(paramJCConditional.falsepart, paramJCConditional.type), paramJCConditional.type);
/* 2943 */       addPrunedInfo(localJCExpression);
/*      */     }
/*      */     else {
/* 2946 */       paramJCConditional.truepart = ((JCTree.JCExpression)translate(paramJCConditional.truepart, paramJCConditional.type));
/* 2947 */       paramJCConditional.falsepart = ((JCTree.JCExpression)translate(paramJCConditional.falsepart, paramJCConditional.type));
/* 2948 */       this.result = paramJCConditional;
/*      */     }
/*      */   }
/*      */ 
/*      */   private JCTree convert(JCTree paramJCTree, Type paramType) {
/* 2953 */     if ((paramJCTree.type == paramType) || (paramJCTree.type.hasTag(TypeTag.BOT)))
/* 2954 */       return paramJCTree;
/* 2955 */     JCTree.JCTypeCast localJCTypeCast = make_at(paramJCTree.pos()).TypeCast(this.make.Type(paramType), (JCTree.JCExpression)paramJCTree);
/* 2956 */     localJCTypeCast.type = (paramJCTree.type.constValue() != null ? this.cfolder.coerce(paramJCTree.type, paramType) : paramType);
/*      */ 
/* 2958 */     return localJCTypeCast;
/*      */   }
/*      */ 
/*      */   public void visitIf(JCTree.JCIf paramJCIf)
/*      */   {
/* 2964 */     JCTree.JCExpression localJCExpression = paramJCIf.cond = (JCTree.JCExpression)translate(paramJCIf.cond, this.syms.booleanType);
/* 2965 */     if (localJCExpression.type.isTrue()) {
/* 2966 */       this.result = translate(paramJCIf.thenpart);
/* 2967 */       addPrunedInfo(localJCExpression);
/* 2968 */     } else if (localJCExpression.type.isFalse()) {
/* 2969 */       if (paramJCIf.elsepart != null)
/* 2970 */         this.result = translate(paramJCIf.elsepart);
/*      */       else {
/* 2972 */         this.result = this.make.Skip();
/*      */       }
/* 2974 */       addPrunedInfo(localJCExpression);
/*      */     }
/*      */     else {
/* 2977 */       paramJCIf.thenpart = ((JCTree.JCStatement)translate(paramJCIf.thenpart));
/* 2978 */       paramJCIf.elsepart = ((JCTree.JCStatement)translate(paramJCIf.elsepart));
/* 2979 */       this.result = paramJCIf;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert)
/*      */   {
/* 2986 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = paramJCAssert.detail == null ? paramJCAssert.pos() : paramJCAssert.detail.pos();
/* 2987 */     paramJCAssert.cond = ((JCTree.JCExpression)translate(paramJCAssert.cond, this.syms.booleanType));
/* 2988 */     if (!paramJCAssert.cond.type.isTrue()) {
/* 2989 */       Object localObject = assertFlagTest(paramJCAssert.pos());
/*      */ 
/* 2991 */       List localList = paramJCAssert.detail == null ? 
/* 2991 */         List.nil() : List.of(translate(paramJCAssert.detail));
/* 2992 */       if (!paramJCAssert.cond.type.isFalse())
/*      */       {
/* 2994 */         localObject = makeBinary(JCTree.Tag.AND, (JCTree.JCExpression)localObject, 
/* 2996 */           makeUnary(JCTree.Tag.NOT, paramJCAssert.cond));
/*      */       }
/*      */ 
/* 2998 */       this.result = this.make
/* 2999 */         .If((JCTree.JCExpression)localObject, 
/* 3000 */         make_at(paramJCAssert)
/* 3001 */         .Throw(makeNewClass(this.syms.assertionErrorType, localList)), 
/* 3001 */         null);
/*      */     }
/*      */     else {
/* 3004 */       this.result = this.make.Skip();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 3009 */     Symbol localSymbol = TreeInfo.symbol(paramJCMethodInvocation.meth);
/* 3010 */     List localList = localSymbol.type.getParameterTypes();
/* 3011 */     if ((this.allowEnums) && (localSymbol.name == this.names.init) && (localSymbol.owner == this.syms.enumSym))
/*      */     {
/* 3014 */       localList = localList.tail.tail;
/* 3015 */     }paramJCMethodInvocation.args = boxArgs(localList, paramJCMethodInvocation.args, paramJCMethodInvocation.varargsElement);
/* 3016 */     paramJCMethodInvocation.varargsElement = null;
/* 3017 */     Name localName = TreeInfo.name(paramJCMethodInvocation.meth);
/*      */     Object localObject1;
/* 3018 */     if (localSymbol.name == this.names.init)
/*      */     {
/* 3021 */       localObject1 = accessConstructor(paramJCMethodInvocation.pos(), localSymbol);
/* 3022 */       if (localObject1 != localSymbol) {
/* 3023 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.append(makeNull());
/* 3024 */         TreeInfo.setSymbol(paramJCMethodInvocation.meth, (Symbol)localObject1);
/*      */       }
/*      */ 
/* 3029 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((Symbol)localObject1).owner;
/* 3030 */       if (localClassSymbol.isLocal())
/* 3031 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.appendList(loadFreevars(paramJCMethodInvocation.pos(), freevars(localClassSymbol)));
/*      */       Object localObject2;
/* 3036 */       if (((localClassSymbol.flags_field & 0x4000) != 0L) || (localClassSymbol.getQualifiedName() == this.names.java_lang_Enum)) {
/* 3037 */         localObject2 = this.currentMethodDef.params;
/* 3038 */         if (this.currentMethodSym.owner.hasOuterInstance())
/* 3039 */           localObject2 = ((List)localObject2).tail;
/* 3040 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args
/* 3041 */           .prepend(make_at(paramJCMethodInvocation
/* 3041 */           .pos()).Ident(((JCTree.JCVariableDecl)((List)localObject2).tail.head).sym))
/* 3042 */           .prepend(this.make
/* 3042 */           .Ident(((JCTree.JCVariableDecl)((List)localObject2).head).sym));
/*      */       }
/*      */ 
/* 3051 */       if (localClassSymbol.hasOuterInstance())
/*      */       {
/* 3053 */         if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT))
/*      */         {
/* 3055 */           localObject2 = this.attr
/* 3055 */             .makeNullCheck((JCTree.JCExpression)translate(((JCTree.JCFieldAccess)paramJCMethodInvocation.meth).selected));
/*      */ 
/* 3056 */           paramJCMethodInvocation.meth = this.make.Ident((Symbol)localObject1);
/* 3057 */           ((JCTree.JCIdent)paramJCMethodInvocation.meth).name = localName;
/* 3058 */         } else if ((localClassSymbol.isLocal()) || (localName == this.names._this))
/*      */         {
/* 3060 */           localObject2 = makeThis(paramJCMethodInvocation.meth.pos(), localClassSymbol.type.getEnclosingType().tsym);
/*      */         }
/*      */         else {
/* 3063 */           localObject2 = makeOwnerThisN(paramJCMethodInvocation.meth.pos(), localClassSymbol, false);
/*      */         }
/* 3065 */         paramJCMethodInvocation.args = paramJCMethodInvocation.args.prepend(localObject2);
/*      */       }
/*      */     }
/*      */     else {
/* 3069 */       paramJCMethodInvocation.meth = ((JCTree.JCExpression)translate(paramJCMethodInvocation.meth));
/*      */ 
/* 3074 */       if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.APPLY)) {
/* 3075 */         localObject1 = (JCTree.JCMethodInvocation)paramJCMethodInvocation.meth;
/* 3076 */         ((JCTree.JCMethodInvocation)localObject1).args = paramJCMethodInvocation.args.prependList(((JCTree.JCMethodInvocation)localObject1).args);
/* 3077 */         this.result = ((JCTree)localObject1);
/* 3078 */         return;
/*      */       }
/*      */     }
/* 3081 */     this.result = paramJCMethodInvocation;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> boxArgs(List<Type> paramList, List<JCTree.JCExpression> paramList1, Type paramType) {
/* 3085 */     Object localObject1 = paramList1;
/* 3086 */     if (paramList.isEmpty()) return localObject1;
/* 3087 */     int i = 0;
/* 3088 */     ListBuffer localListBuffer = new ListBuffer();
/* 3089 */     while (paramList.tail.nonEmpty()) {
/* 3090 */       localObject2 = (JCTree.JCExpression)translate((JCTree)((List)localObject1).head, (Type)paramList.head);
/* 3091 */       i |= (localObject2 != ((List)localObject1).head ? 1 : 0);
/* 3092 */       localListBuffer.append(localObject2);
/* 3093 */       localObject1 = ((List)localObject1).tail;
/* 3094 */       paramList = paramList.tail;
/*      */     }
/* 3096 */     Object localObject2 = (Type)paramList.head;
/*      */     Object localObject3;
/* 3097 */     if (paramType != null) {
/* 3098 */       i = 1;
/* 3099 */       localObject3 = new ListBuffer();
/* 3100 */       while (((List)localObject1).nonEmpty()) {
/* 3101 */         localObject4 = (JCTree.JCExpression)translate((JCTree)((List)localObject1).head, paramType);
/* 3102 */         ((ListBuffer)localObject3).append(localObject4);
/* 3103 */         localObject1 = ((List)localObject1).tail;
/*      */       }
/* 3105 */       Object localObject4 = this.make.NewArray(this.make.Type(paramType), 
/* 3106 */         List.nil(), ((ListBuffer)localObject3)
/* 3107 */         .toList());
/* 3108 */       ((JCTree.JCNewArray)localObject4).type = new Type.ArrayType(paramType, this.syms.arrayClass);
/* 3109 */       localListBuffer.append(localObject4);
/*      */     } else {
/* 3111 */       if (((List)localObject1).length() != 1) throw new AssertionError(localObject1);
/* 3112 */       localObject3 = (JCTree.JCExpression)translate((JCTree)((List)localObject1).head, (Type)localObject2);
/* 3113 */       i |= (localObject3 != ((List)localObject1).head ? 1 : 0);
/* 3114 */       localListBuffer.append(localObject3);
/* 3115 */       if (i == 0) return paramList1;
/*      */     }
/* 3117 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   <T extends JCTree> T boxIfNeeded(T paramT, Type paramType)
/*      */   {
/* 3123 */     boolean bool = paramT.type.isPrimitive();
/* 3124 */     if (bool == paramType.isPrimitive())
/* 3125 */       return paramT;
/* 3126 */     if (bool) {
/* 3127 */       Type localType = this.types.unboxedType(paramType);
/* 3128 */       if (!localType.hasTag(TypeTag.NONE)) {
/* 3129 */         if (!this.types.isSubtype(paramT.type, localType))
/* 3130 */           paramT.type = localType.constType(paramT.type.constValue());
/* 3131 */         return boxPrimitive((JCTree.JCExpression)paramT, paramType);
/*      */       }
/* 3133 */       paramT = boxPrimitive((JCTree.JCExpression)paramT);
/*      */     }
/*      */     else {
/* 3136 */       paramT = unbox((JCTree.JCExpression)paramT, paramType);
/*      */     }
/* 3138 */     return paramT;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression boxPrimitive(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 3143 */     return boxPrimitive(paramJCExpression, this.types.boxedClass(paramJCExpression.type).type);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression boxPrimitive(JCTree.JCExpression paramJCExpression, Type paramType)
/*      */   {
/* 3148 */     make_at(paramJCExpression.pos());
/* 3149 */     if (this.target.boxWithConstructors()) {
/* 3150 */       localMethodSymbol = lookupConstructor(paramJCExpression.pos(), paramType, 
/* 3152 */         List.nil()
/* 3153 */         .prepend(paramJCExpression.type));
/*      */ 
/* 3154 */       return this.make.Create(localMethodSymbol, List.of(paramJCExpression));
/*      */     }
/* 3156 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramJCExpression.pos(), this.names.valueOf, paramType, 
/* 3159 */       List.nil()
/* 3160 */       .prepend(paramJCExpression.type));
/*      */ 
/* 3161 */     return this.make.App(this.make.QualIdent(localMethodSymbol), List.of(paramJCExpression));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression unbox(JCTree.JCExpression paramJCExpression, Type paramType)
/*      */   {
/* 3167 */     Type localType = this.types.unboxedType(paramJCExpression.type);
/* 3168 */     if (localType.hasTag(TypeTag.NONE)) {
/* 3169 */       localType = paramType;
/* 3170 */       if (!localType.isPrimitive())
/* 3171 */         throw new AssertionError(localType);
/* 3172 */       make_at(paramJCExpression.pos());
/* 3173 */       paramJCExpression = this.make.TypeCast(this.types.boxedClass(localType).type, paramJCExpression);
/*      */     }
/* 3176 */     else if (!this.types.isSubtype(localType, paramType)) {
/* 3177 */       throw new AssertionError(paramJCExpression);
/*      */     }
/* 3179 */     make_at(paramJCExpression.pos());
/* 3180 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramJCExpression.pos(), localType.tsym.name
/* 3181 */       .append(this.names.Value), 
/* 3181 */       paramJCExpression.type, 
/* 3183 */       List.nil());
/* 3184 */     return this.make.App(this.make.Select(paramJCExpression, localMethodSymbol));
/*      */   }
/*      */ 
/*      */   public void visitParens(JCTree.JCParens paramJCParens)
/*      */   {
/* 3191 */     JCTree localJCTree = translate(paramJCParens.expr);
/* 3192 */     this.result = (localJCTree == paramJCParens.expr ? paramJCParens : localJCTree);
/*      */   }
/*      */ 
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 3196 */     paramJCArrayAccess.indexed = ((JCTree.JCExpression)translate(paramJCArrayAccess.indexed));
/* 3197 */     paramJCArrayAccess.index = ((JCTree.JCExpression)translate(paramJCArrayAccess.index, this.syms.intType));
/* 3198 */     this.result = paramJCArrayAccess;
/*      */   }
/*      */ 
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 3202 */     paramJCAssign.lhs = ((JCTree.JCExpression)translate(paramJCAssign.lhs, paramJCAssign));
/* 3203 */     paramJCAssign.rhs = ((JCTree.JCExpression)translate(paramJCAssign.rhs, paramJCAssign.lhs.type));
/*      */ 
/* 3208 */     if (paramJCAssign.lhs.hasTag(JCTree.Tag.APPLY)) {
/* 3209 */       JCTree.JCMethodInvocation localJCMethodInvocation = (JCTree.JCMethodInvocation)paramJCAssign.lhs;
/* 3210 */       localJCMethodInvocation.args = List.of(paramJCAssign.rhs).prependList(localJCMethodInvocation.args);
/* 3211 */       this.result = localJCMethodInvocation;
/*      */     } else {
/* 3213 */       this.result = paramJCAssign;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAssignop(final JCTree.JCAssignOp paramJCAssignOp) {
/* 3218 */     JCTree.JCExpression localJCExpression1 = access(TreeInfo.skipParens(paramJCAssignOp.lhs));
/*      */ 
/* 3220 */     final boolean bool = (!paramJCAssignOp.lhs.type.isPrimitive()) && 
/* 3220 */       (paramJCAssignOp.operator.type
/* 3220 */       .getReturnType().isPrimitive());
/*      */     Object localObject;
/* 3222 */     if ((bool) || (localJCExpression1.hasTag(JCTree.Tag.APPLY)))
/*      */     {
/* 3226 */       localObject = abstractLval(paramJCAssignOp.lhs, new TreeBuilder() {
/*      */         public JCTree build(JCTree paramAnonymousJCTree) {
/* 3228 */           JCTree.Tag localTag = paramJCAssignOp.getTag().noAssignOp();
/*      */ 
/* 3233 */           Symbol localSymbol = Lower.this.rs.resolveBinaryOperator(paramJCAssignOp.pos(), localTag, Lower.this.attrEnv, paramJCAssignOp.type, paramJCAssignOp.rhs.type);
/*      */ 
/* 3238 */           Object localObject = (JCTree.JCExpression)paramAnonymousJCTree;
/* 3239 */           if (((JCTree.JCExpression)localObject).type != paramJCAssignOp.type)
/* 3240 */             localObject = Lower.this.make.TypeCast(paramJCAssignOp.type, (JCTree.JCExpression)localObject);
/* 3241 */           JCTree.JCBinary localJCBinary1 = Lower.this.make.Binary(localTag, (JCTree.JCExpression)localObject, paramJCAssignOp.rhs);
/* 3242 */           localJCBinary1.operator = localSymbol;
/* 3243 */           localJCBinary1.type = localSymbol.type.getReturnType();
/*      */ 
/* 3245 */           JCTree.JCBinary localJCBinary2 = bool ? Lower.this.make
/* 3245 */             .TypeCast(Lower.this.types.unboxedType(paramJCAssignOp.type), localJCBinary1) : localJCBinary1;
/*      */ 
/* 3247 */           return Lower.this.make.Assign((JCTree.JCExpression)paramAnonymousJCTree, localJCBinary2).setType(paramJCAssignOp.type);
/*      */         }
/*      */       });
/* 3250 */       this.result = translate((JCTree)localObject);
/* 3251 */       return;
/*      */     }
/* 3253 */     paramJCAssignOp.lhs = ((JCTree.JCExpression)translate(paramJCAssignOp.lhs, paramJCAssignOp));
/* 3254 */     paramJCAssignOp.rhs = ((JCTree.JCExpression)translate(paramJCAssignOp.rhs, (Type)paramJCAssignOp.operator.type.getParameterTypes().tail.head));
/*      */ 
/* 3259 */     if (paramJCAssignOp.lhs.hasTag(JCTree.Tag.APPLY)) {
/* 3260 */       localObject = (JCTree.JCMethodInvocation)paramJCAssignOp.lhs;
/*      */ 
/* 3264 */       JCTree.JCExpression localJCExpression2 = ((Symbol.OperatorSymbol)paramJCAssignOp.operator).opcode == 256 ? 
/* 3264 */         makeString(paramJCAssignOp.rhs) : 
/* 3264 */         paramJCAssignOp.rhs;
/*      */ 
/* 3266 */       ((JCTree.JCMethodInvocation)localObject).args = List.of(localJCExpression2).prependList(((JCTree.JCMethodInvocation)localObject).args);
/* 3267 */       this.result = ((JCTree)localObject);
/*      */     } else {
/* 3269 */       this.result = paramJCAssignOp;
/*      */     }
/*      */   }
/*      */ 
/*      */   JCTree lowerBoxedPostop(final JCTree.JCUnary paramJCUnary)
/*      */   {
/* 3279 */     final boolean bool = TreeInfo.skipParens(paramJCUnary.arg).hasTag(JCTree.Tag.TYPECAST);
/* 3280 */     return abstractLval(paramJCUnary.arg, new TreeBuilder() {
/*      */       public JCTree build(final JCTree paramAnonymousJCTree) {
/* 3282 */         return Lower.this.abstractRval(paramAnonymousJCTree, paramJCUnary.arg.type, new Lower.TreeBuilder() {
/*      */           public JCTree build(JCTree paramAnonymous2JCTree) {
/* 3284 */             JCTree.Tag localTag = Lower.6.this.val$tree.hasTag(JCTree.Tag.POSTINC) ? JCTree.Tag.PLUS_ASG : JCTree.Tag.MINUS_ASG;
/*      */ 
/* 3287 */             JCTree localJCTree = Lower.6.this.val$cast ? Lower.this.make
/* 3287 */               .TypeCast(Lower.6.this.val$tree.arg.type, (JCTree.JCExpression)paramAnonymousJCTree) : paramAnonymousJCTree;
/*      */ 
/* 3289 */             JCTree.JCAssignOp localJCAssignOp = Lower.this.makeAssignop(localTag, localJCTree, Lower.this.make
/* 3291 */               .Literal(Integer.valueOf(1)));
/* 3292 */             return Lower.this.makeComma(localJCAssignOp, paramAnonymous2JCTree);
/*      */           }
/*      */         });
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 3300 */     boolean bool = paramJCUnary.getTag().isIncOrDecUnaryOp();
/* 3301 */     if ((bool) && (!paramJCUnary.arg.type.isPrimitive())) {
/* 3302 */       switch (7.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCUnary.getTag().ordinal()])
/*      */       {
/*      */       case 1:
/*      */       case 2:
/* 3308 */         JCTree.Tag localTag = paramJCUnary.hasTag(JCTree.Tag.PREINC) ? JCTree.Tag.PLUS_ASG : JCTree.Tag.MINUS_ASG;
/*      */ 
/* 3310 */         JCTree.JCAssignOp localJCAssignOp = makeAssignop(localTag, paramJCUnary.arg, this.make
/* 3312 */           .Literal(Integer.valueOf(1)));
/*      */ 
/* 3313 */         this.result = translate(localJCAssignOp, paramJCUnary.type);
/* 3314 */         return;
/*      */       case 3:
/*      */       case 4:
/* 3319 */         this.result = translate(lowerBoxedPostop(paramJCUnary), paramJCUnary.type);
/* 3320 */         return;
/*      */       }
/*      */ 
/* 3323 */       throw new AssertionError(paramJCUnary);
/*      */     }
/*      */ 
/* 3326 */     paramJCUnary.arg = ((JCTree.JCExpression)boxIfNeeded(translate(paramJCUnary.arg, paramJCUnary), paramJCUnary.type));
/*      */ 
/* 3328 */     if ((paramJCUnary.hasTag(JCTree.Tag.NOT)) && (paramJCUnary.arg.type.constValue() != null)) {
/* 3329 */       paramJCUnary.type = this.cfolder.fold1(257, paramJCUnary.arg.type);
/*      */     }
/*      */ 
/* 3335 */     if ((bool) && (paramJCUnary.arg.hasTag(JCTree.Tag.APPLY)))
/* 3336 */       this.result = paramJCUnary.arg;
/*      */     else
/* 3338 */       this.result = paramJCUnary;
/*      */   }
/*      */ 
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary)
/*      */   {
/* 3343 */     List localList = paramJCBinary.operator.type.getParameterTypes();
/* 3344 */     JCTree.JCExpression localJCExpression = paramJCBinary.lhs = (JCTree.JCExpression)translate(paramJCBinary.lhs, (Type)localList.head);
/* 3345 */     switch (paramJCBinary.getTag()) {
/*      */     case OR:
/* 3347 */       if (localJCExpression.type.isTrue()) {
/* 3348 */         this.result = localJCExpression;
/* 3349 */         return;
/*      */       }
/* 3351 */       if (localJCExpression.type.isFalse()) { this.result = translate(paramJCBinary.rhs, (Type)localList.tail.head);
/*      */         return;
/*      */       }
/*      */       break;
/*      */     case AND:
/* 3357 */       if (localJCExpression.type.isFalse()) {
/* 3358 */         this.result = localJCExpression;
/* 3359 */         return;
/*      */       }
/* 3361 */       if (localJCExpression.type.isTrue()) { this.result = translate(paramJCBinary.rhs, (Type)localList.tail.head);
/*      */         return;
/*      */       }
/*      */       break;
/*      */     }
/* 3367 */     paramJCBinary.rhs = ((JCTree.JCExpression)translate(paramJCBinary.rhs, (Type)localList.tail.head));
/* 3368 */     this.result = paramJCBinary;
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 3372 */     this.result = access(paramJCIdent.sym, paramJCIdent, this.enclOp, false);
/*      */   }
/*      */ 
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop)
/*      */   {
/* 3377 */     if (this.types.elemtype(paramJCEnhancedForLoop.expr.type) == null)
/* 3378 */       visitIterableForeachLoop(paramJCEnhancedForLoop);
/*      */     else
/* 3380 */       visitArrayForeachLoop(paramJCEnhancedForLoop);
/*      */   }
/*      */ 
/*      */   private void visitArrayForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop)
/*      */   {
/* 3405 */     make_at(paramJCEnhancedForLoop.expr.pos());
/*      */ 
/* 3407 */     Symbol.VarSymbol localVarSymbol1 = new Symbol.VarSymbol(4096L, this.names
/* 3407 */       .fromString("arr" + this.target
/* 3407 */       .syntheticNameChar()), paramJCEnhancedForLoop.expr.type, this.currentMethodSym);
/*      */ 
/* 3410 */     JCTree.JCVariableDecl localJCVariableDecl1 = this.make.VarDef(localVarSymbol1, paramJCEnhancedForLoop.expr);
/*      */ 
/* 3412 */     Symbol.VarSymbol localVarSymbol2 = new Symbol.VarSymbol(4096L, this.names
/* 3412 */       .fromString("len" + this.target
/* 3412 */       .syntheticNameChar()), this.syms.intType, this.currentMethodSym);
/*      */ 
/* 3416 */     JCTree.JCVariableDecl localJCVariableDecl2 = this.make
/* 3416 */       .VarDef(localVarSymbol2, this.make
/* 3416 */       .Select(this.make
/* 3416 */       .Ident(localVarSymbol1), 
/* 3416 */       this.syms.lengthVar));
/*      */ 
/* 3418 */     Symbol.VarSymbol localVarSymbol3 = new Symbol.VarSymbol(4096L, this.names
/* 3418 */       .fromString("i" + this.target
/* 3418 */       .syntheticNameChar()), this.syms.intType, this.currentMethodSym);
/*      */ 
/* 3422 */     JCTree.JCVariableDecl localJCVariableDecl3 = this.make.VarDef(localVarSymbol3, this.make.Literal(TypeTag.INT, Integer.valueOf(0)));
/* 3423 */     localJCVariableDecl3.init.type = (localJCVariableDecl3.type = this.syms.intType.constType(Integer.valueOf(0)));
/*      */ 
/* 3425 */     List localList = List.of(localJCVariableDecl1, localJCVariableDecl2, localJCVariableDecl3);
/* 3426 */     JCTree.JCBinary localJCBinary = makeBinary(JCTree.Tag.LT, this.make.Ident(localVarSymbol3), this.make.Ident(localVarSymbol2));
/*      */ 
/* 3428 */     JCTree.JCExpressionStatement localJCExpressionStatement = this.make.Exec(makeUnary(JCTree.Tag.PREINC, this.make.Ident(localVarSymbol3)));
/*      */ 
/* 3430 */     Type localType = this.types.elemtype(paramJCEnhancedForLoop.expr.type);
/*      */ 
/* 3432 */     JCTree.JCExpression localJCExpression = this.make.Indexed(this.make.Ident(localVarSymbol1), this.make
/* 3432 */       .Ident(localVarSymbol3))
/* 3432 */       .setType(localType);
/*      */ 
/* 3436 */     JCTree.JCVariableDecl localJCVariableDecl4 = (JCTree.JCVariableDecl)this.make.VarDef(paramJCEnhancedForLoop.var.mods, paramJCEnhancedForLoop.var.name, paramJCEnhancedForLoop.var.vartype, localJCExpression)
/* 3436 */       .setType(paramJCEnhancedForLoop.var.type);
/*      */ 
/* 3437 */     localJCVariableDecl4.sym = paramJCEnhancedForLoop.var.sym;
/*      */ 
/* 3439 */     JCTree.JCBlock localJCBlock = this.make
/* 3439 */       .Block(0L, 
/* 3439 */       List.of(localJCVariableDecl4, paramJCEnhancedForLoop.body));
/*      */ 
/* 3441 */     this.result = translate(this.make
/* 3442 */       .ForLoop(localList, localJCBinary, 
/* 3444 */       List.of(localJCExpressionStatement), 
/* 3444 */       localJCBlock));
/*      */ 
/* 3446 */     patchTargets(localJCBlock, paramJCEnhancedForLoop, this.result);
/*      */   }
/*      */ 
/*      */   private void patchTargets(JCTree paramJCTree1, final JCTree paramJCTree2, final JCTree paramJCTree3)
/*      */   {
/* 3461 */     new TreeScanner()
/*      */     {
/*      */       public void visitBreak(JCTree.JCBreak paramAnonymousJCBreak)
/*      */       {
/* 3452 */         if (paramAnonymousJCBreak.target == paramJCTree2)
/* 3453 */           paramAnonymousJCBreak.target = paramJCTree3; 
/*      */       }
/*      */ 
/* 3456 */       public void visitContinue(JCTree.JCContinue paramAnonymousJCContinue) { if (paramAnonymousJCContinue.target == paramJCTree2)
/* 3457 */           paramAnonymousJCContinue.target = paramJCTree3;  } 
/*      */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) {
/*      */       }
/*      */     }
/* 3461 */     .scan(paramJCTree1);
/*      */   }
/*      */ 
/*      */   private void visitIterableForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop)
/*      */   {
/* 3482 */     make_at(paramJCEnhancedForLoop.expr.pos());
/* 3483 */     Type localType1 = this.syms.objectType;
/* 3484 */     Type localType2 = this.types.asSuper(this.types.cvarUpperBound(paramJCEnhancedForLoop.expr.type), this.syms.iterableType.tsym);
/*      */ 
/* 3486 */     if (localType2.getTypeArguments().nonEmpty())
/* 3487 */       localType1 = this.types.erasure((Type)localType2.getTypeArguments().head);
/* 3488 */     Type localType3 = paramJCEnhancedForLoop.expr.type;
/* 3489 */     while (localType3.hasTag(TypeTag.TYPEVAR)) {
/* 3490 */       localType3 = localType3.getUpperBound();
/*      */     }
/* 3492 */     paramJCEnhancedForLoop.expr.type = this.types.erasure(localType3);
/* 3493 */     if (localType3.isCompound())
/* 3494 */       paramJCEnhancedForLoop.expr = this.make.TypeCast(this.types.erasure(localType2), paramJCEnhancedForLoop.expr);
/* 3495 */     Symbol.MethodSymbol localMethodSymbol1 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.iterator, localType3, 
/* 3498 */       List.nil());
/*      */ 
/* 3500 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(4096L, this.names.fromString("i" + this.target.syntheticNameChar()), this.types
/* 3500 */       .erasure(this.types
/* 3500 */       .asSuper(localMethodSymbol1.type
/* 3500 */       .getReturnType(), this.syms.iteratorType.tsym)), this.currentMethodSym);
/*      */ 
/* 3504 */     JCTree.JCVariableDecl localJCVariableDecl1 = this.make
/* 3504 */       .VarDef(localVarSymbol, this.make
/* 3504 */       .App(this.make
/* 3504 */       .Select(paramJCEnhancedForLoop.expr, localMethodSymbol1)
/* 3505 */       .setType(this.types
/* 3505 */       .erasure(localMethodSymbol1.type))));
/*      */ 
/* 3507 */     Symbol.MethodSymbol localMethodSymbol2 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.hasNext, localVarSymbol.type, 
/* 3510 */       List.nil());
/* 3511 */     JCTree.JCMethodInvocation localJCMethodInvocation = this.make.App(this.make.Select(this.make.Ident(localVarSymbol), localMethodSymbol2));
/* 3512 */     Symbol.MethodSymbol localMethodSymbol3 = lookupMethod(paramJCEnhancedForLoop.expr.pos(), this.names.next, localVarSymbol.type, 
/* 3515 */       List.nil());
/* 3516 */     Object localObject = this.make.App(this.make.Select(this.make.Ident(localVarSymbol), localMethodSymbol3));
/* 3517 */     if (paramJCEnhancedForLoop.var.type.isPrimitive())
/* 3518 */       localObject = this.make.TypeCast(this.types.cvarUpperBound(localType1), (JCTree.JCExpression)localObject);
/*      */     else {
/* 3520 */       localObject = this.make.TypeCast(paramJCEnhancedForLoop.var.type, (JCTree.JCExpression)localObject);
/*      */     }
/*      */ 
/* 3524 */     JCTree.JCVariableDecl localJCVariableDecl2 = (JCTree.JCVariableDecl)this.make.VarDef(paramJCEnhancedForLoop.var.mods, paramJCEnhancedForLoop.var.name, paramJCEnhancedForLoop.var.vartype, (JCTree.JCExpression)localObject)
/* 3524 */       .setType(paramJCEnhancedForLoop.var.type);
/*      */ 
/* 3525 */     localJCVariableDecl2.sym = paramJCEnhancedForLoop.var.sym;
/* 3526 */     JCTree.JCBlock localJCBlock = this.make.Block(0L, List.of(localJCVariableDecl2, paramJCEnhancedForLoop.body));
/* 3527 */     localJCBlock.endpos = TreeInfo.endPos(paramJCEnhancedForLoop.body);
/* 3528 */     this.result = translate(this.make
/* 3529 */       .ForLoop(List.of(localJCVariableDecl1), 
/* 3529 */       localJCMethodInvocation, 
/* 3531 */       List.nil(), localJCBlock));
/*      */ 
/* 3533 */     patchTargets(localJCBlock, paramJCEnhancedForLoop, this.result);
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 3537 */     Symbol.MethodSymbol localMethodSymbol = this.currentMethodSym;
/* 3538 */     paramJCVariableDecl.mods = ((JCTree.JCModifiers)translate(paramJCVariableDecl.mods));
/* 3539 */     paramJCVariableDecl.vartype = ((JCTree.JCExpression)translate(paramJCVariableDecl.vartype));
/* 3540 */     if (this.currentMethodSym == null)
/*      */     {
/* 3542 */       this.currentMethodSym = new Symbol.MethodSymbol(paramJCVariableDecl.mods.flags & 0x8 | 0x100000, this.names.empty, null, this.currentClass);
/*      */     }
/*      */ 
/* 3547 */     if (paramJCVariableDecl.init != null) paramJCVariableDecl.init = ((JCTree.JCExpression)translate(paramJCVariableDecl.init, paramJCVariableDecl.type));
/* 3548 */     this.result = paramJCVariableDecl;
/* 3549 */     this.currentMethodSym = localMethodSymbol;
/*      */   }
/*      */ 
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 3553 */     Symbol.MethodSymbol localMethodSymbol = this.currentMethodSym;
/* 3554 */     if (this.currentMethodSym == null)
/*      */     {
/* 3556 */       this.currentMethodSym = new Symbol.MethodSymbol(paramJCBlock.flags | 0x100000, this.names.empty, null, this.currentClass);
/*      */     }
/*      */ 
/* 3561 */     super.visitBlock(paramJCBlock);
/* 3562 */     this.currentMethodSym = localMethodSymbol;
/*      */   }
/*      */ 
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 3566 */     paramJCDoWhileLoop.body = ((JCTree.JCStatement)translate(paramJCDoWhileLoop.body));
/* 3567 */     paramJCDoWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCDoWhileLoop.cond, this.syms.booleanType));
/* 3568 */     this.result = paramJCDoWhileLoop;
/*      */   }
/*      */ 
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 3572 */     paramJCWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCWhileLoop.cond, this.syms.booleanType));
/* 3573 */     paramJCWhileLoop.body = ((JCTree.JCStatement)translate(paramJCWhileLoop.body));
/* 3574 */     this.result = paramJCWhileLoop;
/*      */   }
/*      */ 
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 3578 */     paramJCForLoop.init = translate(paramJCForLoop.init);
/* 3579 */     if (paramJCForLoop.cond != null)
/* 3580 */       paramJCForLoop.cond = ((JCTree.JCExpression)translate(paramJCForLoop.cond, this.syms.booleanType));
/* 3581 */     paramJCForLoop.step = translate(paramJCForLoop.step);
/* 3582 */     paramJCForLoop.body = ((JCTree.JCStatement)translate(paramJCForLoop.body));
/* 3583 */     this.result = paramJCForLoop;
/*      */   }
/*      */ 
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 3587 */     if (paramJCReturn.expr != null) {
/* 3588 */       paramJCReturn.expr = ((JCTree.JCExpression)translate(paramJCReturn.expr, this.types
/* 3589 */         .erasure(this.currentMethodDef.restype.type)));
/*      */     }
/*      */ 
/* 3591 */     this.result = paramJCReturn;
/*      */   }
/*      */ 
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 3595 */     Type localType = this.types.supertype(paramJCSwitch.selector.type);
/*      */ 
/* 3597 */     int i = (localType != null) && 
/* 3597 */       ((paramJCSwitch.selector.type.tsym
/* 3597 */       .flags() & 0x4000) != 0L) ? 1 : 0;
/*      */ 
/* 3599 */     int j = (localType != null) && 
/* 3599 */       (this.types
/* 3599 */       .isSameType(paramJCSwitch.selector.type, this.syms.stringType)) ? 
/* 3599 */       1 : 0;
/* 3600 */     Type.JCPrimitiveType localJCPrimitiveType = j != 0 ? this.syms.stringType : i != 0 ? paramJCSwitch.selector.type : this.syms.intType;
/*      */ 
/* 3602 */     paramJCSwitch.selector = ((JCTree.JCExpression)translate(paramJCSwitch.selector, localJCPrimitiveType));
/* 3603 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/* 3604 */     if (i != 0)
/* 3605 */       this.result = visitEnumSwitch(paramJCSwitch);
/* 3606 */     else if (j != 0)
/* 3607 */       this.result = visitStringSwitch(paramJCSwitch);
/*      */     else
/* 3609 */       this.result = paramJCSwitch;
/*      */   }
/*      */ 
/*      */   public JCTree visitEnumSwitch(JCTree.JCSwitch paramJCSwitch)
/*      */   {
/* 3614 */     Symbol.TypeSymbol localTypeSymbol = paramJCSwitch.selector.type.tsym;
/* 3615 */     EnumMapping localEnumMapping = mapForEnum(paramJCSwitch.pos(), localTypeSymbol);
/* 3616 */     make_at(paramJCSwitch.pos());
/* 3617 */     Symbol.MethodSymbol localMethodSymbol = lookupMethod(paramJCSwitch.pos(), this.names.ordinal, paramJCSwitch.selector.type, 
/* 3620 */       List.nil());
/* 3621 */     JCTree.JCArrayAccess localJCArrayAccess = this.make.Indexed(localEnumMapping.mapVar, this.make
/* 3622 */       .App(this.make
/* 3622 */       .Select(paramJCSwitch.selector, localMethodSymbol)));
/*      */ 
/* 3624 */     ListBuffer localListBuffer = new ListBuffer();
/* 3625 */     for (Object localObject = paramJCSwitch.cases.iterator(); ((Iterator)localObject).hasNext(); ) { JCTree.JCCase localJCCase = (JCTree.JCCase)((Iterator)localObject).next();
/* 3626 */       if (localJCCase.pat != null) {
/* 3627 */         Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)TreeInfo.symbol(localJCCase.pat);
/* 3628 */         JCTree.JCLiteral localJCLiteral = localEnumMapping.forConstant(localVarSymbol);
/* 3629 */         localListBuffer.append(this.make.Case(localJCLiteral, localJCCase.stats));
/*      */       } else {
/* 3631 */         localListBuffer.append(localJCCase);
/*      */       }
/*      */     }
/* 3634 */     localObject = this.make.Switch(localJCArrayAccess, localListBuffer.toList());
/* 3635 */     patchTargets((JCTree)localObject, paramJCSwitch, (JCTree)localObject);
/* 3636 */     return localObject;
/*      */   }
/*      */ 
/*      */   public JCTree visitStringSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 3640 */     List localList = paramJCSwitch.getCases();
/* 3641 */     int i = localList.size();
/*      */ 
/* 3643 */     if (i == 0) {
/* 3644 */       return this.make.at(paramJCSwitch.pos()).Exec(this.attr.makeNullCheck(paramJCSwitch.getExpression()));
/*      */     }
/*      */ 
/* 3687 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 3691 */     LinkedHashMap localLinkedHashMap1 = new LinkedHashMap(i + 1, 1.0F);
/*      */ 
/* 3695 */     LinkedHashMap localLinkedHashMap2 = new LinkedHashMap(i + 1, 1.0F);
/*      */ 
/* 3698 */     int j = 0;
/* 3699 */     for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCCase)((Iterator)localObject1).next();
/* 3700 */       localObject3 = ((JCTree.JCCase)localObject2).getExpression();
/*      */ 
/* 3702 */       if (localObject3 != null) {
/* 3703 */         localObject4 = (String)((JCTree.JCExpression)localObject3).type.constValue();
/* 3704 */         localObject5 = (Integer)localLinkedHashMap1.put(localObject4, Integer.valueOf(j));
/* 3705 */         Assert.checkNull(localObject5);
/* 3706 */         int k = ((String)localObject4).hashCode();
/*      */ 
/* 3708 */         localObject6 = (Set)localLinkedHashMap2.get(Integer.valueOf(k));
/* 3709 */         if (localObject6 == null) {
/* 3710 */           localObject6 = new LinkedHashSet(1, 1.0F);
/* 3711 */           ((Set)localObject6).add(localObject4);
/* 3712 */           localLinkedHashMap2.put(Integer.valueOf(k), localObject6);
/*      */         } else {
/* 3714 */           boolean bool = ((Set)localObject6).add(localObject4);
/* 3715 */           Assert.check(bool);
/*      */         }
/*      */       }
/* 3718 */       j++;
/*      */     }
/*      */ 
/* 3744 */     localObject1 = new Symbol.VarSymbol(4112L, this.names
/* 3744 */       .fromString("s" + paramJCSwitch.pos + this.target
/* 3744 */       .syntheticNameChar()), this.syms.stringType, this.currentMethodSym);
/*      */ 
/* 3747 */     localListBuffer.append(this.make.at(paramJCSwitch.pos()).VarDef((Symbol.VarSymbol)localObject1, paramJCSwitch.getExpression()).setType(((Symbol.VarSymbol)localObject1).type));
/*      */ 
/* 3750 */     Object localObject2 = new Symbol.VarSymbol(4096L, this.names
/* 3750 */       .fromString("tmp" + paramJCSwitch.pos + this.target
/* 3750 */       .syntheticNameChar()), this.syms.intType, this.currentMethodSym);
/*      */ 
/* 3754 */     Object localObject3 = (JCTree.JCVariableDecl)this.make
/* 3754 */       .VarDef((Symbol.VarSymbol)localObject2, this.make
/* 3754 */       .Literal(TypeTag.INT, 
/* 3754 */       Integer.valueOf(-1)))
/* 3754 */       .setType(((Symbol.VarSymbol)localObject2).type);
/* 3755 */     ((JCTree.JCVariableDecl)localObject3).init.type = (((Symbol.VarSymbol)localObject2).type = this.syms.intType);
/* 3756 */     localListBuffer.append(localObject3);
/* 3757 */     Object localObject4 = new ListBuffer();
/*      */ 
/* 3761 */     Object localObject5 = makeCall(this.make.Ident((Symbol)localObject1), this.names.hashCode, 
/* 3761 */       List.nil()).setType(this.syms.intType);
/*      */ 
/* 3762 */     JCTree.JCSwitch localJCSwitch = this.make.Switch((JCTree.JCExpression)localObject5, ((ListBuffer)localObject4)
/* 3763 */       .toList());
/* 3764 */     for (Object localObject6 = localLinkedHashMap2.entrySet().iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (Map.Entry)((Iterator)localObject6).next();
/* 3765 */       int m = ((Integer)((Map.Entry)localObject7).getKey()).intValue();
/* 3766 */       localObject8 = (Set)((Map.Entry)localObject7).getValue();
/* 3767 */       Assert.check(((Set)localObject8).size() >= 1);
/*      */ 
/* 3769 */       Object localObject9 = null;
/* 3770 */       for (localObject10 = ((Set)localObject8).iterator(); ((Iterator)localObject10).hasNext(); ) { localObject11 = (String)((Iterator)localObject10).next();
/* 3771 */         JCTree.JCMethodInvocation localJCMethodInvocation = makeCall(this.make.Ident((Symbol)localObject1), this.names.equals, 
/* 3773 */           List.of(this.make
/* 3773 */           .Literal(localObject11)));
/*      */ 
/* 3774 */         localObject9 = this.make.If(localJCMethodInvocation, this.make
/* 3775 */           .Exec(this.make
/* 3775 */           .Assign(this.make
/* 3775 */           .Ident((Symbol)localObject2), 
/* 3775 */           this.make
/* 3776 */           .Literal(localLinkedHashMap1
/* 3776 */           .get(localObject11)))
/* 3777 */           .setType(((Symbol.VarSymbol)localObject2).type)), (JCTree.JCStatement)localObject9);
/*      */       }
/*      */ 
/* 3781 */       localObject10 = new ListBuffer();
/* 3782 */       Object localObject11 = this.make.Break(null);
/* 3783 */       ((JCTree.JCBreak)localObject11).target = localJCSwitch;
/* 3784 */       ((ListBuffer)localObject10).append(localObject9).append(localObject11);
/*      */ 
/* 3786 */       ((ListBuffer)localObject4).append(this.make.Case(this.make.Literal(Integer.valueOf(m)), ((ListBuffer)localObject10).toList()));
/*      */     }
/* 3798 */     Object localObject8;
/*      */     Object localObject10;
/* 3789 */     localJCSwitch.cases = ((ListBuffer)localObject4).toList();
/* 3790 */     localListBuffer.append(localJCSwitch);
/*      */ 
/* 3796 */     localObject6 = new ListBuffer();
/* 3797 */     Object localObject7 = this.make.Switch(this.make.Ident((Symbol)localObject2), ((ListBuffer)localObject6).toList());
/* 3798 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject8 = (JCTree.JCCase)localIterator.next();
/*      */ 
/* 3801 */       patchTargets((JCTree)localObject8, paramJCSwitch, (JCTree)localObject7);
/*      */ 
/* 3803 */       int n = ((JCTree.JCCase)localObject8).getExpression() == null ? 1 : 0;
/*      */ 
/* 3805 */       if (n != 0)
/* 3806 */         localObject10 = null;
/*      */       else {
/* 3808 */         localObject10 = this.make.Literal(localLinkedHashMap1.get(
/* 3810 */           (String)TreeInfo.skipParens(((JCTree.JCCase)localObject8)
/* 3809 */           .getExpression()).type
/* 3810 */           .constValue()));
/*      */       }
/*      */ 
/* 3813 */       ((ListBuffer)localObject6).append(this.make.Case((JCTree.JCExpression)localObject10, ((JCTree.JCCase)localObject8)
/* 3814 */         .getStatements()));
/*      */     }
/*      */ 
/* 3817 */     ((JCTree.JCSwitch)localObject7).cases = ((ListBuffer)localObject6).toList();
/* 3818 */     localListBuffer.append(localObject7);
/*      */ 
/* 3820 */     return this.make.Block(0L, localListBuffer.toList());
/*      */   }
/*      */ 
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray)
/*      */   {
/* 3825 */     paramJCNewArray.elemtype = ((JCTree.JCExpression)translate(paramJCNewArray.elemtype));
/* 3826 */     for (List localList = paramJCNewArray.dims; localList.tail != null; localList = localList.tail)
/* 3827 */       if (localList.head != null) localList.head = translate((JCTree)localList.head, this.syms.intType);
/* 3828 */     paramJCNewArray.elems = translate(paramJCNewArray.elems, this.types.elemtype(paramJCNewArray.type));
/* 3829 */     this.result = paramJCNewArray;
/*      */   }
/*      */ 
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */   {
/* 3839 */     boolean bool = (paramJCFieldAccess.selected
/* 3837 */       .hasTag(JCTree.Tag.SELECT)) && 
/* 3838 */       (TreeInfo.name(paramJCFieldAccess.selected) == 
/* 3838 */       this.names._super) && 
/* 3839 */       (!this.types
/* 3839 */       .isDirectSuperInterface(((JCTree.JCFieldAccess)paramJCFieldAccess.selected).selected.type.tsym, this.currentClass));
/*      */ 
/* 3840 */     paramJCFieldAccess.selected = ((JCTree.JCExpression)translate(paramJCFieldAccess.selected));
/* 3841 */     if (paramJCFieldAccess.name == this.names._class) {
/* 3842 */       this.result = classOf(paramJCFieldAccess.selected);
/*      */     }
/* 3844 */     else if ((paramJCFieldAccess.name == this.names._super) && 
/* 3845 */       (this.types
/* 3845 */       .isDirectSuperInterface(paramJCFieldAccess.selected.type.tsym, this.currentClass)))
/*      */     {
/* 3847 */       Symbol.TypeSymbol localTypeSymbol = paramJCFieldAccess.selected.type.tsym;
/* 3848 */       Assert.checkNonNull(this.types.asSuper(this.currentClass.type, localTypeSymbol));
/* 3849 */       this.result = paramJCFieldAccess;
/*      */     }
/* 3851 */     else if ((paramJCFieldAccess.name == this.names._this) || (paramJCFieldAccess.name == this.names._super)) {
/* 3852 */       this.result = makeThis(paramJCFieldAccess.pos(), paramJCFieldAccess.selected.type.tsym);
/*      */     }
/*      */     else {
/* 3855 */       this.result = access(paramJCFieldAccess.sym, paramJCFieldAccess, this.enclOp, bool);
/*      */     }
/*      */   }
/*      */ 
/* 3859 */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) { paramLetExpr.defs = translateVarDefs(paramLetExpr.defs);
/* 3860 */     paramLetExpr.expr = translate(paramLetExpr.expr, paramLetExpr.type);
/* 3861 */     this.result = paramLetExpr;
/*      */   }
/*      */ 
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation)
/*      */   {
/* 3867 */     this.result = paramJCAnnotation;
/*      */   }
/*      */ 
/*      */   public void visitTry(JCTree.JCTry paramJCTry)
/*      */   {
/* 3872 */     if (paramJCTry.resources.nonEmpty()) {
/* 3873 */       this.result = makeTwrTry(paramJCTry);
/* 3874 */       return;
/*      */     }
/*      */ 
/* 3877 */     boolean bool1 = paramJCTry.body.getStatements().nonEmpty();
/* 3878 */     boolean bool2 = paramJCTry.catchers.nonEmpty();
/*      */ 
/* 3880 */     int i = (paramJCTry.finalizer != null) && 
/* 3880 */       (paramJCTry.finalizer
/* 3880 */       .getStatements().nonEmpty()) ? 1 : 0;
/*      */ 
/* 3882 */     if ((!bool2) && (i == 0)) {
/* 3883 */       this.result = translate(paramJCTry.body);
/* 3884 */       return;
/*      */     }
/*      */ 
/* 3887 */     if (!bool1) {
/* 3888 */       if (i != 0)
/* 3889 */         this.result = translate(paramJCTry.finalizer);
/*      */       else {
/* 3891 */         this.result = translate(paramJCTry.body);
/*      */       }
/* 3893 */       return;
/*      */     }
/*      */ 
/* 3897 */     super.visitTry(paramJCTry);
/*      */   }
/*      */ 
/*      */   public List<JCTree> translateTopLevelClass(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker)
/*      */   {
/* 3911 */     ListBuffer localListBuffer = null;
/*      */     try {
/* 3913 */       this.attrEnv = paramEnv;
/* 3914 */       this.make = paramTreeMaker;
/* 3915 */       this.endPosTable = paramEnv.toplevel.endPositions;
/* 3916 */       this.currentClass = null;
/* 3917 */       this.currentMethodDef = null;
/* 3918 */       this.outermostClassDef = (paramJCTree.hasTag(JCTree.Tag.CLASSDEF) ? (JCTree.JCClassDecl)paramJCTree : null);
/* 3919 */       this.outermostMemberDef = null;
/* 3920 */       this.translated = new ListBuffer();
/* 3921 */       this.classdefs = new HashMap();
/* 3922 */       this.actualSymbols = new HashMap();
/* 3923 */       this.freevarCache = new HashMap();
/* 3924 */       this.proxies = new Scope(this.syms.noSymbol);
/* 3925 */       this.twrVars = new Scope(this.syms.noSymbol);
/* 3926 */       this.outerThisStack = List.nil();
/* 3927 */       this.accessNums = new HashMap();
/* 3928 */       this.accessSyms = new HashMap();
/* 3929 */       this.accessConstrs = new HashMap();
/* 3930 */       this.accessConstrTags = List.nil();
/* 3931 */       this.accessed = new ListBuffer();
/* 3932 */       translate(paramJCTree, (JCTree.JCExpression)null);
/* 3933 */       for (Object localObject1 = this.accessed.toList(); ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail)
/* 3934 */         makeAccessible((Symbol)((List)localObject1).head);
/* 3935 */       for (localObject1 = this.enumSwitchMap.values().iterator(); ((Iterator)localObject1).hasNext(); ) { EnumMapping localEnumMapping = (EnumMapping)((Iterator)localObject1).next();
/* 3936 */         localEnumMapping.translate(); }
/* 3937 */       checkConflicts(this.translated.toList());
/* 3938 */       checkAccessConstructorTags();
/* 3939 */       localListBuffer = this.translated;
/*      */     }
/*      */     finally {
/* 3942 */       this.attrEnv = null;
/* 3943 */       this.make = null;
/* 3944 */       this.endPosTable = null;
/* 3945 */       this.currentClass = null;
/* 3946 */       this.currentMethodDef = null;
/* 3947 */       this.outermostClassDef = null;
/* 3948 */       this.outermostMemberDef = null;
/* 3949 */       this.translated = null;
/* 3950 */       this.classdefs = null;
/* 3951 */       this.actualSymbols = null;
/* 3952 */       this.freevarCache = null;
/* 3953 */       this.proxies = null;
/* 3954 */       this.outerThisStack = null;
/* 3955 */       this.accessNums = null;
/* 3956 */       this.accessSyms = null;
/* 3957 */       this.accessConstrs = null;
/* 3958 */       this.accessConstrTags = null;
/* 3959 */       this.accessed = null;
/* 3960 */       this.enumSwitchMap.clear();
/* 3961 */       this.assertionsDisabledClassCache = null;
/*      */     }
/* 3963 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   abstract class BasicFreeVarCollector extends TreeScanner
/*      */   {
/*      */     BasicFreeVarCollector()
/*      */     {
/*      */     }
/*      */ 
/*      */     abstract void addFreeVars(Symbol.ClassSymbol paramClassSymbol);
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/*  234 */       visitSymbol(paramJCIdent.sym);
/*      */     }
/*      */ 
/*      */     abstract void visitSymbol(Symbol paramSymbol);
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/*  243 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramJCNewClass.constructor.owner;
/*  244 */       addFreeVars(localClassSymbol);
/*  245 */       super.visitNewClass(paramJCNewClass);
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */     {
/*  252 */       if (TreeInfo.name(paramJCMethodInvocation.meth) == Lower.this.names._super) {
/*  253 */         addFreeVars((Symbol.ClassSymbol)TreeInfo.symbol(paramJCMethodInvocation.meth).owner);
/*      */       }
/*  255 */       super.visitApply(paramJCMethodInvocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   class ClassMap extends TreeScanner
/*      */   {
/*      */     ClassMap()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*  184 */       Lower.this.classdefs.put(paramJCClassDecl.sym, paramJCClassDecl);
/*  185 */       super.visitClassDef(paramJCClassDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   class EnumMapping
/*      */   {
/*  463 */     JCDiagnostic.DiagnosticPosition pos = null;
/*      */ 
/*  466 */     int next = 1;
/*      */     final Symbol.TypeSymbol forEnum;
/*      */     final Symbol.VarSymbol mapVar;
/*      */     final Map<Symbol.VarSymbol, Integer> values;
/*      */ 
/*      */     EnumMapping(JCDiagnostic.DiagnosticPosition paramTypeSymbol, Symbol.TypeSymbol arg3)
/*      */     {
/*      */       Object localObject;
/*  445 */       this.forEnum = localObject;
/*  446 */       this.values = new LinkedHashMap();
/*  447 */       this.pos = paramTypeSymbol;
/*      */ 
/*  449 */       Name localName = Lower.this.names
/*  449 */         .fromString(Lower.this.target
/*  449 */         .syntheticNameChar() + "SwitchMap" + Lower.this.target
/*  451 */         .syntheticNameChar() + Lower.this.writer
/*  452 */         .xClassName(localObject.type).toString()
/*  453 */         .replace('/', '.')
/*  454 */         .replace('.', Lower.this.target
/*  454 */         .syntheticNameChar()));
/*  455 */       Symbol.ClassSymbol localClassSymbol = Lower.this.outerCacheClass();
/*  456 */       this.mapVar = new Symbol.VarSymbol(4120L, localName, new Type.ArrayType(Lower.this.syms.intType, 
/*  458 */         Lower.this.syms.arrayClass), localClassSymbol);
/*      */ 
/*  460 */       Lower.this.enterSynthetic(paramTypeSymbol, this.mapVar, localClassSymbol.members());
/*      */     }
/*      */ 
/*      */     JCTree.JCLiteral forConstant(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/*  478 */       Integer localInteger = (Integer)this.values.get(paramVarSymbol);
/*  479 */       if (localInteger == null)
/*  480 */         this.values.put(paramVarSymbol, localInteger = Integer.valueOf(this.next++));
/*  481 */       return Lower.this.make.Literal(localInteger);
/*      */     }
/*      */ 
/*      */     void translate()
/*      */     {
/*  486 */       Lower.this.make.at(this.pos.getStartPosition());
/*  487 */       JCTree.JCClassDecl localJCClassDecl = Lower.this.classDef((Symbol.ClassSymbol)this.mapVar.owner);
/*      */ 
/*  490 */       Symbol.MethodSymbol localMethodSymbol1 = Lower.this.lookupMethod(this.pos, Lower.this.names.values, 
/*  491 */         this.forEnum.type, 
/*  493 */         List.nil());
/*      */ 
/*  495 */       JCTree.JCExpression localJCExpression1 = Lower.this.make
/*  495 */         .Select(Lower.this.make
/*  495 */         .App(Lower.this.make.QualIdent(localMethodSymbol1)), Lower.this.syms.lengthVar);
/*      */ 
/*  499 */       JCTree.JCExpression localJCExpression2 = Lower.this.make
/*  498 */         .NewArray(Lower.this.make
/*  498 */         .Type(Lower.this.syms.intType), List.of(localJCExpression1), null)
/*  499 */         .setType(new Type.ArrayType(Lower.this.syms.intType, 
/*  499 */         Lower.this.syms.arrayClass));
/*      */ 
/*  502 */       ListBuffer localListBuffer = new ListBuffer();
/*  503 */       Symbol.MethodSymbol localMethodSymbol2 = Lower.this.lookupMethod(this.pos, Lower.this.names.ordinal, 
/*  504 */         this.forEnum.type, 
/*  506 */         List.nil());
/*      */ 
/*  508 */       List localList = List.nil()
/*  508 */         .prepend(Lower.this.make
/*  508 */         .Catch(Lower.this.make.VarDef(new Symbol.VarSymbol(8589934592L, Lower.this.names.ex, Lower.this.syms.noSuchFieldErrorType, 
/*  509 */         Lower.this.syms.noSymbol), 
/*  510 */         null), Lower.this.make
/*  512 */         .Block(0L, List.nil())));
/*  513 */       for (Map.Entry localEntry : this.values.entrySet()) {
/*  514 */         Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.getKey();
/*  515 */         Integer localInteger = (Integer)localEntry.getValue();
/*      */ 
/*  521 */         JCTree.JCExpression localJCExpression3 = Lower.this.make
/*  517 */           .Assign(Lower.this.make
/*  517 */           .Indexed(this.mapVar, Lower.this.make
/*  518 */           .App(Lower.this.make.Select(Lower.this.make.QualIdent(localVarSymbol), localMethodSymbol2))), Lower.this.make
/*  520 */           .Literal(localInteger))
/*  521 */           .setType(Lower.this.syms.intType);
/*      */ 
/*  522 */         JCTree.JCExpressionStatement localJCExpressionStatement = Lower.this.make.Exec(localJCExpression3);
/*  523 */         JCTree.JCTry localJCTry = Lower.this.make.Try(Lower.this.make.Block(0L, List.of(localJCExpressionStatement)), localList, null);
/*  524 */         localListBuffer.append(localJCTry);
/*      */       }
/*      */ 
/*  527 */       localJCClassDecl.defs = localJCClassDecl.defs
/*  528 */         .prepend(Lower.this.make
/*  528 */         .Block(8L, localListBuffer.toList()))
/*  529 */         .prepend(Lower.this.make
/*  529 */         .VarDef(this.mapVar, localJCExpression2));
/*      */     }
/*      */   }
/*      */ 
/*      */   class FreeVarCollector extends Lower.BasicFreeVarCollector
/*      */   {
/*      */     Symbol owner;
/*      */     Symbol.ClassSymbol clazz;
/*      */     List<Symbol.VarSymbol> fvs;
/*      */ 
/*      */     FreeVarCollector(Symbol.ClassSymbol arg2)
/*      */     {
/*  277 */       super();
/*      */       Object localObject;
/*  278 */       this.clazz = localObject;
/*  279 */       this.owner = localObject.owner;
/*  280 */       this.fvs = List.nil();
/*      */     }
/*      */ 
/*      */     private void addFreeVar(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/*  286 */       for (List localList = this.fvs; localList.nonEmpty(); localList = localList.tail)
/*  287 */         if (localList.head == paramVarSymbol) return;
/*  288 */       this.fvs = this.fvs.prepend(paramVarSymbol);
/*      */     }
/*      */ 
/*      */     void addFreeVars(Symbol.ClassSymbol paramClassSymbol)
/*      */     {
/*  293 */       List localList1 = (List)Lower.this.freevarCache.get(paramClassSymbol);
/*  294 */       if (localList1 != null)
/*  295 */         for (List localList2 = localList1; localList2.nonEmpty(); localList2 = localList2.tail)
/*  296 */           addFreeVar((Symbol.VarSymbol)localList2.head);
/*      */     }
/*      */ 
/*      */     void visitSymbol(Symbol paramSymbol)
/*      */     {
/*  303 */       Symbol localSymbol = paramSymbol;
/*  304 */       if ((localSymbol.kind == 4) || (localSymbol.kind == 16)) {
/*  305 */         while ((localSymbol != null) && (localSymbol.owner != this.owner))
/*  306 */           localSymbol = Lower.this.proxies.lookup(Lower.this.proxyName(localSymbol.name)).sym;
/*  307 */         if ((localSymbol != null) && (localSymbol.owner == this.owner)) {
/*  308 */           Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localSymbol;
/*  309 */           if (localVarSymbol.getConstValue() == null) {
/*  310 */             addFreeVar(localVarSymbol);
/*      */           }
/*      */         }
/*  313 */         else if ((Lower.this.outerThisStack.head != null) && (Lower.this.outerThisStack.head != paramSymbol))
/*      */         {
/*  315 */           visitSymbol((Symbol)Lower.this.outerThisStack.head);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/*  324 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramJCNewClass.constructor.owner;
/*  325 */       if ((paramJCNewClass.encl == null) && 
/*  326 */         (localClassSymbol
/*  326 */         .hasOuterInstance()) && (Lower.this.outerThisStack.head != null))
/*      */       {
/*  328 */         visitSymbol((Symbol)Lower.this.outerThisStack.head);
/*  329 */       }super.visitNewClass(paramJCNewClass);
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/*  337 */       if (((paramJCFieldAccess.name == Lower.this.names._this) || (paramJCFieldAccess.name == Lower.this.names._super)) && (paramJCFieldAccess.selected.type.tsym != this.clazz) && (Lower.this.outerThisStack.head != null))
/*      */       {
/*  340 */         visitSymbol((Symbol)Lower.this.outerThisStack.head);
/*  341 */       }super.visitSelect(paramJCFieldAccess);
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */     {
/*  348 */       if (TreeInfo.name(paramJCMethodInvocation.meth) == Lower.this.names._super) {
/*  349 */         Symbol localSymbol = TreeInfo.symbol(paramJCMethodInvocation.meth);
/*  350 */         Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localSymbol.owner;
/*  351 */         if ((localClassSymbol.hasOuterInstance()) && 
/*  352 */           (!paramJCMethodInvocation.meth
/*  352 */           .hasTag(JCTree.Tag.SELECT)) && 
/*  352 */           (Lower.this.outerThisStack.head != null))
/*      */         {
/*  354 */           visitSymbol((Symbol)Lower.this.outerThisStack.head);
/*      */         }
/*      */       }
/*  356 */       super.visitApply(paramJCMethodInvocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface TreeBuilder
/*      */   {
/*      */     public abstract JCTree build(JCTree paramJCTree);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Lower
 * JD-Core Version:    0.6.2
 */