/*      */ package com.sun.tools.javac.jvm;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.OperatorSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Check;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.comp.Flow;
/*      */ import com.sun.tools.javac.comp.Lower;
/*      */ import com.sun.tools.javac.comp.Resolve;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.model.FilteredMemberList;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
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
/*      */ import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSkip;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSynchronized;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWildcard;
/*      */ import com.sun.tools.javac.tree.JCTree.LetExpr;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Bits;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ 
/*      */ public class Gen extends JCTree.Visitor
/*      */ {
/*   62 */   protected static final Context.Key<Gen> genKey = new Context.Key();
/*      */   private final Log log;
/*      */   private final Symtab syms;
/*      */   private final Check chk;
/*      */   private final Resolve rs;
/*      */   private final TreeMaker make;
/*      */   private final Names names;
/*      */   private final Target target;
/*      */   private final Type stringBufferType;
/*      */   private final Map<Type, Symbol> stringBufferAppend;
/*      */   private Name accessDollar;
/*      */   private final Types types;
/*      */   private final Lower lower;
/*      */   private final Flow flow;
/*      */   private final boolean allowGenerics;
/*      */   private final boolean generateIproxies;
/*      */   private final Code.StackMapFormat stackMap;
/*      */   private final Type methodType;
/*      */   private Pool pool;
/*      */   private final boolean typeAnnoAsserts;
/*      */   private final boolean lineDebugInfo;
/*      */   private final boolean varDebugInfo;
/*      */   private final boolean genCrt;
/*      */   private final boolean debugCode;
/*      */   private final boolean allowInvokedynamic;
/*      */   private final int jsrlimit;
/*      */   private boolean useJsrLocally;
/*      */   private Code code;
/*      */   private Items items;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private JCTree.JCCompilationUnit toplevel;
/*  207 */   private int nerrs = 0;
/*      */   EndPosTable endPosTable;
/*      */   Env<GenContext> env;
/*      */   Type pt;
/*      */   Items.Item result;
/*  929 */   private ClassReferenceVisitor classReferenceVisitor = new ClassReferenceVisitor();
/*      */ 
/*      */   public static Gen instance(Context paramContext)
/*      */   {
/*   94 */     Gen localGen = (Gen)paramContext.get(genKey);
/*   95 */     if (localGen == null)
/*   96 */       localGen = new Gen(paramContext);
/*   97 */     return localGen;
/*      */   }
/*      */ 
/*      */   protected Gen(Context paramContext)
/*      */   {
/*  107 */     paramContext.put(genKey, this);
/*      */ 
/*  109 */     this.names = Names.instance(paramContext);
/*  110 */     this.log = Log.instance(paramContext);
/*  111 */     this.syms = Symtab.instance(paramContext);
/*  112 */     this.chk = Check.instance(paramContext);
/*  113 */     this.rs = Resolve.instance(paramContext);
/*  114 */     this.make = TreeMaker.instance(paramContext);
/*  115 */     this.target = Target.instance(paramContext);
/*  116 */     this.types = Types.instance(paramContext);
/*  117 */     this.methodType = new Type.MethodType(null, null, null, this.syms.methodClass);
/*  118 */     this.allowGenerics = Source.instance(paramContext).allowGenerics();
/*  119 */     this.stringBufferType = (this.target.useStringBuilder() ? this.syms.stringBuilderType : this.syms.stringBufferType);
/*      */ 
/*  122 */     this.stringBufferAppend = new HashMap();
/*  123 */     this.accessDollar = this.names
/*  124 */       .fromString("access" + this.target
/*  124 */       .syntheticNameChar());
/*  125 */     this.flow = Flow.instance(paramContext);
/*  126 */     this.lower = Lower.instance(paramContext);
/*      */ 
/*  128 */     Options localOptions = Options.instance(paramContext);
/*  129 */     this.lineDebugInfo = 
/*  130 */       ((localOptions
/*  130 */       .isUnset(Option.G_CUSTOM)) || 
/*  131 */       (localOptions
/*  131 */       .isSet(Option.G_CUSTOM, "lines")));
/*      */ 
/*  132 */     this.varDebugInfo = 
/*  133 */       (localOptions
/*  133 */       .isUnset(Option.G_CUSTOM) ? 
/*  133 */       localOptions
/*  134 */       .isSet(Option.G) : 
/*  134 */       localOptions
/*  135 */       .isSet(Option.G_CUSTOM, "vars"));
/*      */ 
/*  136 */     this.genCrt = localOptions.isSet(Option.XJCOV);
/*  137 */     this.debugCode = localOptions.isSet("debugcode");
/*  138 */     this.allowInvokedynamic = ((this.target.hasInvokedynamic()) || (localOptions.isSet("invokedynamic")));
/*  139 */     this.pool = new Pool(this.types);
/*  140 */     this.typeAnnoAsserts = localOptions.isSet("TypeAnnotationAsserts");
/*      */ 
/*  142 */     this.generateIproxies = 
/*  143 */       ((this.target
/*  143 */       .requiresIproxy()) || 
/*  144 */       (localOptions
/*  144 */       .isSet("miranda")));
/*      */ 
/*  146 */     if (this.target.generateStackMapTable())
/*      */     {
/*  148 */       this.stackMap = Code.StackMapFormat.JSR202;
/*      */     }
/*  150 */     else if (this.target.generateCLDCStackmap())
/*  151 */       this.stackMap = Code.StackMapFormat.CLDC;
/*      */     else {
/*  153 */       this.stackMap = Code.StackMapFormat.NONE;
/*      */     }
/*      */ 
/*  158 */     int i = 50;
/*  159 */     String str = localOptions.get("jsrlimit");
/*  160 */     if (str != null)
/*      */       try {
/*  162 */         i = Integer.parseInt(str);
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException)
/*      */       {
/*      */       }
/*  167 */     this.jsrlimit = i;
/*  168 */     this.useJsrLocally = false;
/*      */   }
/*      */ 
/*      */   void loadIntConst(int paramInt)
/*      */   {
/*  218 */     this.items.makeImmediateItem(this.syms.intType, Integer.valueOf(paramInt)).load();
/*      */   }
/*      */ 
/*      */   public static int zero(int paramInt)
/*      */   {
/*  225 */     switch (paramInt) { case 0:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  227 */       return 3;
/*      */     case 1:
/*  229 */       return 9;
/*      */     case 2:
/*  231 */       return 11;
/*      */     case 3:
/*  233 */       return 14;
/*      */     case 4: }
/*  235 */     throw new AssertionError("zero");
/*      */   }
/*      */ 
/*      */   public static int one(int paramInt)
/*      */   {
/*  243 */     return zero(paramInt) + 1;
/*      */   }
/*      */ 
/*      */   void emitMinusOne(int paramInt)
/*      */   {
/*  250 */     if (paramInt == 1)
/*  251 */       this.items.makeImmediateItem(this.syms.longType, new Long(-1L)).load();
/*      */     else
/*  253 */       this.code.emitop0(2);
/*      */   }
/*      */ 
/*      */   Symbol binaryQualifier(Symbol paramSymbol, Type paramType)
/*      */   {
/*  271 */     if (paramType.hasTag(TypeTag.ARRAY)) {
/*  272 */       if ((paramSymbol == this.syms.lengthVar) || (paramSymbol.owner != this.syms.arrayClass))
/*      */       {
/*  274 */         return paramSymbol;
/*      */       }
/*  276 */       Symbol.TypeSymbol localTypeSymbol = this.target.arrayBinaryCompatibility() ? new Symbol.ClassSymbol(1L, paramType.tsym.name, paramType, this.syms.noSymbol) : this.syms.objectType.tsym;
/*      */ 
/*  280 */       return paramSymbol.clone(localTypeSymbol);
/*      */     }
/*      */ 
/*  283 */     if ((paramSymbol.owner == paramType.tsym) || 
/*  284 */       ((paramSymbol
/*  284 */       .flags() & 0x1008) == 4104L)) {
/*  285 */       return paramSymbol;
/*      */     }
/*  287 */     if (!this.target.obeyBinaryCompatibility())
/*      */     {
/*  290 */       return this.rs.isAccessible(this.attrEnv, (Symbol.TypeSymbol)paramSymbol.owner) ? paramSymbol : paramSymbol
/*  290 */         .clone(paramType.tsym);
/*      */     }
/*      */ 
/*  292 */     if ((!this.target.interfaceFieldsBinaryCompatibility()) && 
/*  293 */       ((paramSymbol.owner.flags() & 0x200) != 0L) && (paramSymbol.kind == 4)) {
/*  294 */       return paramSymbol;
/*      */     }
/*      */ 
/*  299 */     if (paramSymbol.owner == this.syms.objectType.tsym) {
/*  300 */       return paramSymbol;
/*      */     }
/*  302 */     if ((!this.target.interfaceObjectOverridesBinaryCompatibility()) && 
/*  303 */       ((paramSymbol.owner.flags() & 0x200) != 0L) && 
/*  304 */       (this.syms.objectType.tsym
/*  304 */       .members().lookup(paramSymbol.name).scope != null)) {
/*  305 */       return paramSymbol;
/*      */     }
/*      */ 
/*  308 */     return paramSymbol.clone(paramType.tsym);
/*      */   }
/*      */ 
/*      */   int makeRef(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  317 */     checkDimension(paramDiagnosticPosition, paramType);
/*  318 */     if (paramType.isAnnotated())
/*      */     {
/*  323 */       return this.pool.put(paramType);
/*      */     }
/*  325 */     return this.pool.put(paramType.hasTag(TypeTag.CLASS) ? paramType.tsym : paramType);
/*      */   }
/*      */ 
/*      */   private void checkDimension(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  332 */     switch (paramType.getTag()) {
/*      */     case METHOD:
/*  334 */       checkDimension(paramDiagnosticPosition, paramType.getReturnType());
/*  335 */       for (List localList = paramType.getParameterTypes(); localList.nonEmpty(); localList = localList.tail)
/*  336 */         checkDimension(paramDiagnosticPosition, (Type)localList.head);
/*  337 */       break;
/*      */     case ARRAY:
/*  339 */       if (this.types.dimensions(paramType) > 255) {
/*  340 */         this.log.error(paramDiagnosticPosition, "limit.dimensions", new Object[0]);
/*  341 */         this.nerrs += 1; } break;
/*      */     }
/*      */   }
/*      */ 
/*      */   Items.LocalItem makeTemp(Type paramType)
/*      */   {
/*  353 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(4096L, this.names.empty, paramType, this.env.enclMethod.sym);
/*      */ 
/*  357 */     this.code.newLocal(localVarSymbol);
/*  358 */     return this.items.makeLocalItem(localVarSymbol);
/*      */   }
/*      */ 
/*      */   void callMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName, List<Type> paramList, boolean paramBoolean)
/*      */   {
/*  373 */     Symbol.MethodSymbol localMethodSymbol = this.rs
/*  373 */       .resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName, paramList, null);
/*      */ 
/*  374 */     if (paramBoolean) this.items.makeStaticItem(localMethodSymbol).invoke(); else
/*  375 */       this.items.makeMemberItem(localMethodSymbol, paramName == this.names.init).invoke();
/*      */   }
/*      */ 
/*      */   private boolean isAccessSuper(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  383 */     if ((paramJCMethodDecl.mods.flags & 0x1000) != 0L);
/*  385 */     return isOddAccessName(paramJCMethodDecl.name);
/*      */   }
/*      */ 
/*      */   private boolean isOddAccessName(Name paramName)
/*      */   {
/*  393 */     return (paramName
/*  392 */       .startsWith(this.accessDollar)) && 
/*  393 */       ((paramName
/*  393 */       .getByteAt(paramName
/*  393 */       .getByteLength() - 1) & 0x1) == 1);
/*      */   }
/*      */ 
/*      */   void genFinalizer(Env<GenContext> paramEnv)
/*      */   {
/*  406 */     if ((this.code.isAlive()) && (((GenContext)paramEnv.info).finalize != null))
/*  407 */       ((GenContext)paramEnv.info).finalize.gen();
/*      */   }
/*      */ 
/*      */   Env<GenContext> unwind(JCTree paramJCTree, Env<GenContext> paramEnv)
/*      */   {
/*  417 */     Object localObject = paramEnv;
/*      */     while (true) {
/*  419 */       genFinalizer((Env)localObject);
/*  420 */       if (((Env)localObject).tree == paramJCTree) break;
/*  421 */       localObject = ((Env)localObject).next;
/*      */     }
/*  423 */     return localObject;
/*      */   }
/*      */ 
/*      */   void endFinalizerGap(Env<GenContext> paramEnv)
/*      */   {
/*  431 */     if ((((GenContext)paramEnv.info).gaps != null) && (((GenContext)paramEnv.info).gaps.length() % 2 == 1))
/*  432 */       ((GenContext)paramEnv.info).gaps.append(Integer.valueOf(this.code.curCP()));
/*      */   }
/*      */ 
/*      */   void endFinalizerGaps(Env<GenContext> paramEnv1, Env<GenContext> paramEnv2)
/*      */   {
/*  441 */     Env<GenContext> localEnv = null;
/*  442 */     while (localEnv != paramEnv2) {
/*  443 */       endFinalizerGap(paramEnv1);
/*  444 */       localEnv = paramEnv1;
/*  445 */       paramEnv1 = paramEnv1.next;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean hasFinally(JCTree paramJCTree, Env<GenContext> paramEnv)
/*      */   {
/*  455 */     while (paramEnv.tree != paramJCTree) {
/*  456 */       if ((paramEnv.tree.hasTag(JCTree.Tag.TRY)) && (((GenContext)paramEnv.info).finalize.hasFinalizer()))
/*  457 */         return true;
/*  458 */       paramEnv = paramEnv.next;
/*      */     }
/*  460 */     return false;
/*      */   }
/*      */ 
/*      */   List<JCTree> normalizeDefs(List<JCTree> paramList, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  473 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  474 */     ListBuffer localListBuffer2 = new ListBuffer();
/*  475 */     ListBuffer localListBuffer3 = new ListBuffer();
/*  476 */     ListBuffer localListBuffer4 = new ListBuffer();
/*  477 */     ListBuffer localListBuffer5 = new ListBuffer();
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Object localObject4;
/*  482 */     for (Object localObject1 = paramList; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/*  483 */       localObject2 = (JCTree)((List)localObject1).head;
/*  484 */       switch (3.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localObject2.getTag().ordinal()]) {
/*      */       case 1:
/*  486 */         localObject3 = (JCTree.JCBlock)localObject2;
/*  487 */         if ((((JCTree.JCBlock)localObject3).flags & 0x8) != 0L)
/*  488 */           localListBuffer3.append(localObject3);
/*  489 */         else if ((((JCTree.JCBlock)localObject3).flags & 0x1000) == 0L)
/*  490 */           localListBuffer1.append(localObject3); break;
/*      */       case 2:
/*  493 */         localListBuffer5.append(localObject2);
/*  494 */         break;
/*      */       case 3:
/*  496 */         localObject4 = (JCTree.JCVariableDecl)localObject2;
/*  497 */         Symbol.VarSymbol localVarSymbol = ((JCTree.JCVariableDecl)localObject4).sym;
/*  498 */         checkDimension(((JCTree.JCVariableDecl)localObject4).pos(), localVarSymbol.type);
/*  499 */         if (((JCTree.JCVariableDecl)localObject4).init != null)
/*      */         {
/*      */           JCTree.JCStatement localJCStatement;
/*  500 */           if ((localVarSymbol.flags() & 0x8) == 0L)
/*      */           {
/*  503 */             localJCStatement = this.make.at(((JCTree.JCVariableDecl)localObject4).pos())
/*  503 */               .Assignment(localVarSymbol, ((JCTree.JCVariableDecl)localObject4).init);
/*      */ 
/*  504 */             localListBuffer1.append(localJCStatement);
/*  505 */             this.endPosTable.replaceTree((JCTree)localObject4, localJCStatement);
/*  506 */             localListBuffer2.addAll(getAndRemoveNonFieldTAs(localVarSymbol));
/*  507 */           } else if (localVarSymbol.getConstValue() == null)
/*      */           {
/*  511 */             localJCStatement = this.make.at(((JCTree.JCVariableDecl)localObject4).pos)
/*  511 */               .Assignment(localVarSymbol, ((JCTree.JCVariableDecl)localObject4).init);
/*      */ 
/*  512 */             localListBuffer3.append(localJCStatement);
/*  513 */             this.endPosTable.replaceTree((JCTree)localObject4, localJCStatement);
/*  514 */             localListBuffer4.addAll(getAndRemoveNonFieldTAs(localVarSymbol));
/*      */           } else {
/*  516 */             checkStringConstant(((JCTree.JCVariableDecl)localObject4).init.pos(), localVarSymbol.getConstValue());
/*      */ 
/*  520 */             ((JCTree.JCVariableDecl)localObject4).init.accept(this.classReferenceVisitor); }  } break;
/*      */       default:
/*  525 */         Assert.error();
/*      */       }
/*      */     }
/*      */ 
/*  529 */     if (localListBuffer1.length() != 0) {
/*  530 */       localObject1 = localListBuffer1.toList();
/*  531 */       localListBuffer2.addAll(paramClassSymbol.getInitTypeAttributes());
/*  532 */       localObject2 = localListBuffer2.toList();
/*  533 */       for (localObject3 = localListBuffer5.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree)((Iterator)localObject3).next();
/*  534 */         normalizeMethod((JCTree.JCMethodDecl)localObject4, (List)localObject1, (List)localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  539 */     if (localListBuffer3.length() != 0)
/*      */     {
/*  545 */       localObject1 = new Symbol.MethodSymbol(0x8 | paramClassSymbol
/*  541 */         .flags() & 0x800, this.names.clinit, new Type.MethodType(
/*  544 */         List.nil(), this.syms.voidType, 
/*  545 */         List.nil(), this.syms.methodClass), paramClassSymbol);
/*      */ 
/*  547 */       paramClassSymbol.members().enter((Symbol)localObject1);
/*  548 */       localObject2 = localListBuffer3.toList();
/*  549 */       localObject3 = this.make.at(((JCTree.JCStatement)((List)localObject2).head).pos()).Block(0L, (List)localObject2);
/*  550 */       ((JCTree.JCBlock)localObject3).endpos = TreeInfo.endPos((JCTree)((List)localObject2).last());
/*  551 */       localListBuffer5.append(this.make.MethodDef((Symbol.MethodSymbol)localObject1, (JCTree.JCBlock)localObject3));
/*      */ 
/*  553 */       if (!localListBuffer4.isEmpty())
/*  554 */         ((Symbol.MethodSymbol)localObject1).appendUniqueTypeAttributes(localListBuffer4.toList());
/*  555 */       if (!paramClassSymbol.getClassInitTypeAttributes().isEmpty()) {
/*  556 */         ((Symbol.MethodSymbol)localObject1).appendUniqueTypeAttributes(paramClassSymbol.getClassInitTypeAttributes());
/*      */       }
/*      */     }
/*  559 */     return localListBuffer5.toList();
/*      */   }
/*      */ 
/*      */   private List<Attribute.TypeCompound> getAndRemoveNonFieldTAs(Symbol.VarSymbol paramVarSymbol) {
/*  563 */     List localList = paramVarSymbol.getRawTypeAttributes();
/*  564 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  565 */     ListBuffer localListBuffer2 = new ListBuffer();
/*  566 */     for (Attribute.TypeCompound localTypeCompound : localList) {
/*  567 */       if (localTypeCompound.getPosition().type == TargetType.FIELD) {
/*  568 */         localListBuffer1.add(localTypeCompound);
/*      */       } else {
/*  570 */         if (this.typeAnnoAsserts) {
/*  571 */           Assert.error("Type annotation does not have a valid positior");
/*      */         }
/*      */ 
/*  574 */         localListBuffer2.add(localTypeCompound);
/*      */       }
/*      */     }
/*  577 */     paramVarSymbol.setTypeAttributes(localListBuffer1.toList());
/*  578 */     return localListBuffer2.toList();
/*      */   }
/*      */ 
/*      */   private void checkStringConstant(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Object paramObject)
/*      */   {
/*  585 */     if ((this.nerrs != 0) || (paramObject == null) || (!(paramObject instanceof String)) || 
/*  588 */       (((String)paramObject)
/*  588 */       .length() < 65535))
/*  589 */       return;
/*  590 */     this.log.error(paramDiagnosticPosition, "limit.string", new Object[0]);
/*  591 */     this.nerrs += 1;
/*      */   }
/*      */ 
/*      */   void normalizeMethod(JCTree.JCMethodDecl paramJCMethodDecl, List<JCTree.JCStatement> paramList, List<Attribute.TypeCompound> paramList1)
/*      */   {
/*  601 */     if ((paramJCMethodDecl.name == this.names.init) && (TreeInfo.isInitialConstructor(paramJCMethodDecl)))
/*      */     {
/*  604 */       List localList = paramJCMethodDecl.body.stats;
/*  605 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  607 */       if (localList.nonEmpty())
/*      */       {
/*  610 */         while (TreeInfo.isSyntheticInit((JCTree)localList.head)) {
/*  611 */           localListBuffer.append(localList.head);
/*  612 */           localList = localList.tail;
/*      */         }
/*      */ 
/*  615 */         localListBuffer.append(localList.head);
/*  616 */         localList = localList.tail;
/*      */ 
/*  618 */         while ((localList.nonEmpty()) && 
/*  619 */           (TreeInfo.isSyntheticInit((JCTree)localList.head)))
/*      */         {
/*  620 */           localListBuffer.append(localList.head);
/*  621 */           localList = localList.tail;
/*      */         }
/*      */ 
/*  624 */         localListBuffer.appendList(paramList);
/*      */ 
/*  626 */         while (localList.nonEmpty()) {
/*  627 */           localListBuffer.append(localList.head);
/*  628 */           localList = localList.tail;
/*      */         }
/*      */       }
/*  631 */       paramJCMethodDecl.body.stats = localListBuffer.toList();
/*  632 */       if (paramJCMethodDecl.body.endpos == -1) {
/*  633 */         paramJCMethodDecl.body.endpos = TreeInfo.endPos((JCTree)paramJCMethodDecl.body.stats.last());
/*      */       }
/*  635 */       paramJCMethodDecl.sym.appendUniqueTypeAttributes(paramList1);
/*      */     }
/*      */   }
/*      */ 
/*      */   void implementInterfaceMethods(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  651 */     implementInterfaceMethods(paramClassSymbol, paramClassSymbol);
/*      */   }
/*      */ 
/*      */   void implementInterfaceMethods(Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2)
/*      */   {
/*  663 */     for (List localList = this.types.interfaces(paramClassSymbol1.type); localList.nonEmpty(); localList = localList.tail) {
/*  664 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((Type)localList.head).tsym;
/*  665 */       for (Scope.Entry localEntry = localClassSymbol.members().elems; 
/*  666 */         localEntry != null; 
/*  667 */         localEntry = localEntry.sibling)
/*      */       {
/*  669 */         if ((localEntry.sym.kind == 16) && ((localEntry.sym.flags() & 0x8) == 0L))
/*      */         {
/*  671 */           Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)localEntry.sym;
/*  672 */           Symbol.MethodSymbol localMethodSymbol2 = localMethodSymbol1.binaryImplementation(paramClassSymbol2, this.types);
/*  673 */           if (localMethodSymbol2 == null)
/*  674 */             addAbstractMethod(paramClassSymbol2, localMethodSymbol1);
/*  675 */           else if ((localMethodSymbol2.flags() & 0x200000) != 0L)
/*  676 */             adjustAbstractMethod(paramClassSymbol2, localMethodSymbol2, localMethodSymbol1);
/*      */         }
/*      */       }
/*  679 */       implementInterfaceMethods(localClassSymbol, paramClassSymbol2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addAbstractMethod(Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/*  697 */     Symbol.MethodSymbol localMethodSymbol = new Symbol.MethodSymbol(paramMethodSymbol
/*  697 */       .flags() | 0x200000 | 0x1000, paramMethodSymbol.name, paramMethodSymbol.type, paramClassSymbol);
/*      */ 
/*  700 */     paramClassSymbol.members().enter(localMethodSymbol);
/*      */   }
/*      */ 
/*      */   private void adjustAbstractMethod(Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2)
/*      */   {
/*  706 */     Type.MethodType localMethodType = (Type.MethodType)paramMethodSymbol1.type;
/*  707 */     Type localType = this.types.memberType(paramClassSymbol.type, paramMethodSymbol2);
/*  708 */     localMethodType.thrown = this.chk.intersect(localMethodType.getThrownTypes(), localType.getThrownTypes());
/*      */   }
/*      */ 
/*      */   public void genDef(JCTree paramJCTree, Env<GenContext> paramEnv)
/*      */   {
/*  733 */     Env localEnv = this.env;
/*      */     try {
/*  735 */       this.env = paramEnv;
/*  736 */       paramJCTree.accept(this);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  738 */       this.chk.completionError(paramJCTree.pos(), localCompletionFailure);
/*      */     } finally {
/*  740 */       this.env = localEnv;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void genStat(JCTree paramJCTree, Env<GenContext> paramEnv, int paramInt)
/*      */   {
/*  756 */     if (!this.genCrt) {
/*  757 */       genStat(paramJCTree, paramEnv);
/*  758 */       return;
/*      */     }
/*  760 */     int i = this.code.curCP();
/*  761 */     genStat(paramJCTree, paramEnv);
/*  762 */     if (paramJCTree.hasTag(JCTree.Tag.BLOCK)) paramInt |= 2;
/*  763 */     this.code.crt.put(paramJCTree, paramInt, i, this.code.curCP());
/*      */   }
/*      */ 
/*      */   public void genStat(JCTree paramJCTree, Env<GenContext> paramEnv)
/*      */   {
/*  769 */     if (this.code.isAlive()) {
/*  770 */       this.code.statBegin(paramJCTree.pos);
/*  771 */       genDef(paramJCTree, paramEnv);
/*  772 */     } else if ((((GenContext)paramEnv.info).isSwitch) && (paramJCTree.hasTag(JCTree.Tag.VARDEF)))
/*      */     {
/*  775 */       this.code.newLocal(((JCTree.JCVariableDecl)paramJCTree).sym);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void genStats(List<JCTree.JCStatement> paramList, Env<GenContext> paramEnv, int paramInt)
/*      */   {
/*  791 */     if (!this.genCrt) {
/*  792 */       genStats(paramList, paramEnv);
/*  793 */       return;
/*      */     }
/*  795 */     if (paramList.length() == 1) {
/*  796 */       genStat((JCTree)paramList.head, paramEnv, paramInt | 0x1);
/*      */     } else {
/*  798 */       int i = this.code.curCP();
/*  799 */       genStats(paramList, paramEnv);
/*  800 */       this.code.crt.put(paramList, paramInt, i, this.code.curCP());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void genStats(List<? extends JCTree> paramList, Env<GenContext> paramEnv)
/*      */   {
/*  807 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  808 */       genStat((JCTree)((List)localObject).head, paramEnv, 1);
/*      */   }
/*      */ 
/*      */   public Items.CondItem genCond(JCTree paramJCTree, int paramInt)
/*      */   {
/*  822 */     if (!this.genCrt) return genCond(paramJCTree, false);
/*  823 */     int i = this.code.curCP();
/*  824 */     Items.CondItem localCondItem = genCond(paramJCTree, (paramInt & 0x8) != 0);
/*  825 */     this.code.crt.put(paramJCTree, paramInt, i, this.code.curCP());
/*  826 */     return localCondItem;
/*      */   }
/*      */ 
/*      */   public Items.CondItem genCond(JCTree paramJCTree, boolean paramBoolean)
/*      */   {
/*  838 */     JCTree localJCTree = TreeInfo.skipParens(paramJCTree);
/*  839 */     if (localJCTree.hasTag(JCTree.Tag.CONDEXPR)) {
/*  840 */       localObject1 = (JCTree.JCConditional)localJCTree;
/*  841 */       Items.CondItem localCondItem1 = genCond(((JCTree.JCConditional)localObject1).cond, 8);
/*  842 */       if (localCondItem1.isTrue()) {
/*  843 */         this.code.resolve(localCondItem1.trueJumps);
/*  844 */         localObject2 = genCond(((JCTree.JCConditional)localObject1).truepart, 16);
/*  845 */         if (paramBoolean) ((Items.CondItem)localObject2).tree = ((JCTree.JCConditional)localObject1).truepart;
/*  846 */         return localObject2;
/*      */       }
/*  848 */       if (localCondItem1.isFalse()) {
/*  849 */         this.code.resolve(localCondItem1.falseJumps);
/*  850 */         localObject2 = genCond(((JCTree.JCConditional)localObject1).falsepart, 16);
/*  851 */         if (paramBoolean) ((Items.CondItem)localObject2).tree = ((JCTree.JCConditional)localObject1).falsepart;
/*  852 */         return localObject2;
/*      */       }
/*  854 */       Object localObject2 = localCondItem1.jumpFalse();
/*  855 */       this.code.resolve(localCondItem1.trueJumps);
/*  856 */       Items.CondItem localCondItem2 = genCond(((JCTree.JCConditional)localObject1).truepart, 16);
/*  857 */       if (paramBoolean) localCondItem2.tree = ((JCTree.JCConditional)localObject1).truepart;
/*  858 */       Code.Chain localChain1 = localCondItem2.jumpFalse();
/*  859 */       this.code.resolve(localCondItem2.trueJumps);
/*  860 */       Code.Chain localChain2 = this.code.branch(167);
/*  861 */       this.code.resolve((Code.Chain)localObject2);
/*  862 */       Items.CondItem localCondItem3 = genCond(((JCTree.JCConditional)localObject1).falsepart, 16);
/*  863 */       Items.CondItem localCondItem4 = this.items.makeCondItem(localCondItem3.opcode, 
/*  864 */         Code.mergeChains(localChain2, localCondItem3.trueJumps), 
/*  865 */         Code.mergeChains(localChain1, localCondItem3.falseJumps));
/*      */ 
/*  866 */       if (paramBoolean) localCondItem4.tree = ((JCTree.JCConditional)localObject1).falsepart;
/*  867 */       return localCondItem4;
/*      */     }
/*  869 */     Object localObject1 = genExpr(paramJCTree, this.syms.booleanType).mkCond();
/*  870 */     if (paramBoolean) ((Items.CondItem)localObject1).tree = paramJCTree;
/*  871 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public Items.Item genExpr(JCTree paramJCTree, Type paramType)
/*      */   {
/*  937 */     Type localType = this.pt;
/*      */     try {
/*  939 */       if (paramJCTree.type.constValue() != null)
/*      */       {
/*  941 */         paramJCTree.accept(this.classReferenceVisitor);
/*  942 */         checkStringConstant(paramJCTree.pos(), paramJCTree.type.constValue());
/*  943 */         this.result = this.items.makeImmediateItem(paramJCTree.type, paramJCTree.type.constValue());
/*      */       } else {
/*  945 */         this.pt = paramType;
/*  946 */         paramJCTree.accept(this);
/*      */       }
/*  948 */       return this.result.coerce(paramType);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  950 */       this.chk.completionError(paramJCTree.pos(), localCompletionFailure);
/*  951 */       this.code.state.stacksize = 1;
/*  952 */       return this.items.makeStackItem(paramType);
/*      */     } finally {
/*  954 */       this.pt = localType;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void genArgs(List<JCTree.JCExpression> paramList, List<Type> paramList1)
/*      */   {
/*  964 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  965 */       genExpr((JCTree)((List)localObject).head, (Type)paramList1.head).load();
/*  966 */       paramList1 = paramList1.tail;
/*      */     }
/*      */ 
/*  969 */     Assert.check(paramList1.isEmpty());
/*      */   }
/*      */ 
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  986 */     Env localEnv = this.env.dup(paramJCMethodDecl);
/*  987 */     localEnv.enclMethod = paramJCMethodDecl;
/*      */ 
/*  990 */     this.pt = paramJCMethodDecl.sym.erasure(this.types).getReturnType();
/*      */ 
/*  992 */     checkDimension(paramJCMethodDecl.pos(), paramJCMethodDecl.sym.erasure(this.types));
/*  993 */     genMethod(paramJCMethodDecl, localEnv, false);
/*      */   }
/*      */ 
/*      */   void genMethod(JCTree.JCMethodDecl paramJCMethodDecl, Env<GenContext> paramEnv, boolean paramBoolean)
/*      */   {
/* 1006 */     Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/* 1007 */     int i = 0;
/*      */ 
/* 1009 */     if (localMethodSymbol.isConstructor()) {
/* 1010 */       i++;
/* 1011 */       if ((localMethodSymbol.enclClass().isInner()) && 
/* 1012 */         (!localMethodSymbol
/* 1012 */         .enclClass().isStatic()))
/* 1013 */         i++;
/*      */     }
/* 1015 */     else if ((paramJCMethodDecl.mods.flags & 0x8) == 0L) {
/* 1016 */       i++;
/*      */     }
/*      */ 
/* 1019 */     if (Code.width(this.types.erasure(paramEnv.enclMethod.sym.type).getParameterTypes()) + i > 255)
/*      */     {
/* 1021 */       this.log.error(paramJCMethodDecl.pos(), "limit.parameters", new Object[0]);
/* 1022 */       this.nerrs += 1;
/*      */     }
/* 1025 */     else if (paramJCMethodDecl.body != null)
/*      */     {
/* 1027 */       int j = initCode(paramJCMethodDecl, paramEnv, paramBoolean);
/*      */       try
/*      */       {
/* 1030 */         genStat(paramJCMethodDecl.body, paramEnv);
/*      */       }
/*      */       catch (CodeSizeOverflow localCodeSizeOverflow) {
/* 1033 */         j = initCode(paramJCMethodDecl, paramEnv, paramBoolean);
/* 1034 */         genStat(paramJCMethodDecl.body, paramEnv);
/*      */       }
/*      */ 
/* 1037 */       if (this.code.state.stacksize != 0) {
/* 1038 */         this.log.error(paramJCMethodDecl.body.pos(), "stack.sim.error", new Object[] { paramJCMethodDecl });
/* 1039 */         throw new AssertionError();
/*      */       }
/*      */ 
/* 1044 */       if (this.code.isAlive()) {
/* 1045 */         this.code.statBegin(TreeInfo.endPos(paramJCMethodDecl.body));
/* 1046 */         if ((paramEnv.enclMethod == null) || 
/* 1047 */           (paramEnv.enclMethod.sym.type
/* 1047 */           .getReturnType().hasTag(TypeTag.VOID))) {
/* 1048 */           this.code.emitop0(177);
/*      */         }
/*      */         else
/*      */         {
/* 1052 */           int k = this.code.entryPoint();
/* 1053 */           Items.CondItem localCondItem = this.items.makeCondItem(167);
/* 1054 */           this.code.resolve(localCondItem.jumpTrue(), k);
/*      */         }
/*      */       }
/* 1057 */       if (this.genCrt) {
/* 1058 */         this.code.crt.put(paramJCMethodDecl.body, 2, j, this.code
/* 1061 */           .curCP());
/*      */       }
/* 1063 */       this.code.endScopes(0);
/*      */ 
/* 1066 */       if (this.code.checkLimits(paramJCMethodDecl.pos(), this.log)) {
/* 1067 */         this.nerrs += 1;
/* 1068 */         return;
/*      */       }
/*      */ 
/* 1073 */       if ((!paramBoolean) && (this.code.fatcode)) genMethod(paramJCMethodDecl, paramEnv, true);
/*      */ 
/* 1076 */       if (this.stackMap == Code.StackMapFormat.JSR202) {
/* 1077 */         this.code.lastFrame = null;
/* 1078 */         this.code.frameBeforeLast = null;
/*      */       }
/*      */ 
/* 1082 */       this.code.compressCatchTable();
/*      */ 
/* 1085 */       this.code.fillExceptionParameterPositions();
/*      */     }
/*      */   }
/*      */ 
/*      */   private int initCode(JCTree.JCMethodDecl paramJCMethodDecl, Env<GenContext> paramEnv, boolean paramBoolean) {
/* 1090 */     Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/*      */ 
/* 1093 */     localMethodSymbol.code = (this.code = new Code(localMethodSymbol, paramBoolean, this.lineDebugInfo ? this.toplevel.lineMap : null, this.varDebugInfo, this.stackMap, this.debugCode, this.genCrt ? new CRTable(paramJCMethodDecl, paramEnv.toplevel.endPositions) : null, this.syms, this.types, this.pool));
/*      */ 
/* 1104 */     this.items = new Items(this.pool, this.code, this.syms, this.types);
/* 1105 */     if (this.code.debugCode) {
/* 1106 */       System.err.println(localMethodSymbol + " for body " + paramJCMethodDecl);
/*      */     }
/*      */ 
/* 1111 */     if ((paramJCMethodDecl.mods.flags & 0x8) == 0L) {
/* 1112 */       localObject = localMethodSymbol.owner.type;
/* 1113 */       if ((localMethodSymbol.isConstructor()) && (localObject != this.syms.objectType))
/* 1114 */         localObject = UninitializedType.uninitializedThis((Type)localObject);
/* 1115 */       this.code.setDefined(this.code
/* 1116 */         .newLocal(new Symbol.VarSymbol(16L, this.names._this, (Type)localObject, localMethodSymbol.owner)));
/*      */     }
/*      */ 
/* 1122 */     for (Object localObject = paramJCMethodDecl.params; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 1123 */       checkDimension(((JCTree.JCVariableDecl)((List)localObject).head).pos(), ((JCTree.JCVariableDecl)((List)localObject).head).sym.type);
/* 1124 */       this.code.setDefined(this.code.newLocal(((JCTree.JCVariableDecl)((List)localObject).head).sym));
/*      */     }
/*      */ 
/* 1128 */     int i = this.genCrt ? this.code.curCP() : 0;
/* 1129 */     this.code.entryPoint();
/*      */ 
/* 1132 */     this.code.pendingStackMap = false;
/*      */ 
/* 1134 */     return i;
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 1138 */     Symbol.VarSymbol localVarSymbol = paramJCVariableDecl.sym;
/* 1139 */     this.code.newLocal(localVarSymbol);
/* 1140 */     if (paramJCVariableDecl.init != null) {
/* 1141 */       checkStringConstant(paramJCVariableDecl.init.pos(), localVarSymbol.getConstValue());
/* 1142 */       if ((localVarSymbol.getConstValue() == null) || (this.varDebugInfo)) {
/* 1143 */         genExpr(paramJCVariableDecl.init, localVarSymbol.erasure(this.types)).load();
/* 1144 */         this.items.makeLocalItem(localVarSymbol).store();
/*      */       }
/*      */     }
/* 1147 */     checkDimension(paramJCVariableDecl.pos(), localVarSymbol.type);
/*      */   }
/*      */ 
/*      */   public void visitSkip(JCTree.JCSkip paramJCSkip) {
/*      */   }
/*      */ 
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 1154 */     int i = this.code.nextreg;
/* 1155 */     Env localEnv = this.env.dup(paramJCBlock, new GenContext());
/* 1156 */     genStats(paramJCBlock.stats, localEnv);
/*      */ 
/* 1158 */     if (!this.env.tree.hasTag(JCTree.Tag.METHODDEF)) {
/* 1159 */       this.code.statBegin(paramJCBlock.endpos);
/* 1160 */       this.code.endScopes(i);
/* 1161 */       this.code.pendingStatPos = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 1166 */     genLoop(paramJCDoWhileLoop, paramJCDoWhileLoop.body, paramJCDoWhileLoop.cond, List.nil(), false);
/*      */   }
/*      */ 
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 1170 */     genLoop(paramJCWhileLoop, paramJCWhileLoop.body, paramJCWhileLoop.cond, List.nil(), true);
/*      */   }
/*      */ 
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 1174 */     int i = this.code.nextreg;
/* 1175 */     genStats(paramJCForLoop.init, this.env);
/* 1176 */     genLoop(paramJCForLoop, paramJCForLoop.body, paramJCForLoop.cond, paramJCForLoop.step, true);
/* 1177 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   private void genLoop(JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpressionStatement> paramList, boolean paramBoolean)
/*      */   {
/* 1193 */     Env localEnv = this.env.dup(paramJCStatement1, new GenContext());
/* 1194 */     int i = this.code.entryPoint();
/*      */     Items.CondItem localCondItem;
/* 1195 */     if (paramBoolean)
/*      */     {
/* 1197 */       if (paramJCExpression != null) {
/* 1198 */         this.code.statBegin(paramJCExpression.pos);
/* 1199 */         localCondItem = genCond(TreeInfo.skipParens(paramJCExpression), 8);
/*      */       } else {
/* 1201 */         localCondItem = this.items.makeCondItem(167);
/*      */       }
/* 1203 */       Code.Chain localChain = localCondItem.jumpFalse();
/* 1204 */       this.code.resolve(localCondItem.trueJumps);
/* 1205 */       genStat(paramJCStatement2, localEnv, 17);
/* 1206 */       this.code.resolve(((GenContext)localEnv.info).cont);
/* 1207 */       genStats(paramList, localEnv);
/* 1208 */       this.code.resolve(this.code.branch(167), i);
/* 1209 */       this.code.resolve(localChain);
/*      */     } else {
/* 1211 */       genStat(paramJCStatement2, localEnv, 17);
/* 1212 */       this.code.resolve(((GenContext)localEnv.info).cont);
/* 1213 */       genStats(paramList, localEnv);
/*      */ 
/* 1215 */       if (paramJCExpression != null) {
/* 1216 */         this.code.statBegin(paramJCExpression.pos);
/* 1217 */         localCondItem = genCond(TreeInfo.skipParens(paramJCExpression), 8);
/*      */       } else {
/* 1219 */         localCondItem = this.items.makeCondItem(167);
/*      */       }
/* 1221 */       this.code.resolve(localCondItem.jumpTrue(), i);
/* 1222 */       this.code.resolve(localCondItem.falseJumps);
/*      */     }
/* 1224 */     this.code.resolve(((GenContext)localEnv.info).exit);
/* 1225 */     if (((GenContext)localEnv.info).exit != null)
/* 1226 */       ((GenContext)localEnv.info).exit.state.defined.excludeFrom(this.code.nextreg);
/*      */   }
/*      */ 
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop)
/*      */   {
/* 1231 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 1235 */     Env localEnv = this.env.dup(paramJCLabeledStatement, new GenContext());
/* 1236 */     genStat(paramJCLabeledStatement.body, localEnv, 1);
/* 1237 */     this.code.resolve(((GenContext)localEnv.info).exit);
/*      */   }
/*      */ 
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 1241 */     int i = this.code.nextreg;
/* 1242 */     Assert.check(!paramJCSwitch.selector.type.hasTag(TypeTag.CLASS));
/* 1243 */     int j = this.genCrt ? this.code.curCP() : 0;
/* 1244 */     Items.Item localItem = genExpr(paramJCSwitch.selector, this.syms.intType);
/* 1245 */     List localList1 = paramJCSwitch.cases;
/* 1246 */     if (localList1.isEmpty())
/*      */     {
/* 1248 */       localItem.load().drop();
/* 1249 */       if (this.genCrt)
/* 1250 */         this.code.crt.put(TreeInfo.skipParens(paramJCSwitch.selector), 8, j, this.code
/* 1251 */           .curCP());
/*      */     }
/*      */     else {
/* 1254 */       localItem.load();
/* 1255 */       if (this.genCrt)
/* 1256 */         this.code.crt.put(TreeInfo.skipParens(paramJCSwitch.selector), 8, j, this.code
/* 1257 */           .curCP());
/* 1258 */       Env localEnv = this.env.dup(paramJCSwitch, new GenContext());
/* 1259 */       ((GenContext)localEnv.info).isSwitch = true;
/*      */ 
/* 1263 */       int k = 2147483647;
/* 1264 */       int m = -2147483648;
/* 1265 */       int n = 0;
/*      */ 
/* 1267 */       int[] arrayOfInt1 = new int[localList1.length()];
/* 1268 */       int i1 = -1;
/*      */ 
/* 1270 */       List localList2 = localList1;
/* 1271 */       for (int i2 = 0; i2 < arrayOfInt1.length; i2++) {
/* 1272 */         if (((JCTree.JCCase)localList2.head).pat != null) {
/* 1273 */           int i3 = ((Number)((JCTree.JCCase)localList2.head).pat.type.constValue()).intValue();
/* 1274 */           arrayOfInt1[i2] = i3;
/* 1275 */           if (i3 < k) k = i3;
/* 1276 */           if (m < i3) m = i3;
/* 1277 */           n++;
/*      */         } else {
/* 1279 */           Assert.check(i1 == -1);
/* 1280 */           i1 = i2;
/*      */         }
/* 1282 */         localList2 = localList2.tail;
/*      */       }
/*      */ 
/* 1287 */       long l1 = 4L + (m - k + 1L);
/* 1288 */       long l2 = 3L;
/* 1289 */       long l3 = 3L + 2L * n;
/* 1290 */       long l4 = n;
/* 1291 */       int i4 = (n > 0) && (l1 + 3L * l2 <= l3 + 3L * l4) ? 170 : 171;
/*      */ 
/* 1298 */       int i5 = this.code.curCP();
/* 1299 */       this.code.emitop0(i4);
/* 1300 */       this.code.align(4);
/* 1301 */       int i6 = this.code.curCP();
/* 1302 */       int[] arrayOfInt2 = null;
/* 1303 */       this.code.emit4(-1);
/* 1304 */       if (i4 == 170) {
/* 1305 */         this.code.emit4(k);
/* 1306 */         this.code.emit4(m);
/* 1307 */         for (long l5 = k; l5 <= m; l5 += 1L)
/* 1308 */           this.code.emit4(-1);
/*      */       }
/*      */       else {
/* 1311 */         this.code.emit4(n);
/* 1312 */         for (int i7 = 0; i7 < n; i7++) {
/* 1313 */           this.code.emit4(-1); this.code.emit4(-1);
/*      */         }
/* 1315 */         arrayOfInt2 = new int[arrayOfInt1.length];
/*      */       }
/* 1317 */       Code.State localState = this.code.state.dup();
/* 1318 */       this.code.markDead();
/*      */ 
/* 1321 */       localList2 = localList1;
/* 1322 */       for (int i8 = 0; i8 < arrayOfInt1.length; i8++) {
/* 1323 */         JCTree.JCCase localJCCase = (JCTree.JCCase)localList2.head;
/* 1324 */         localList2 = localList2.tail;
/*      */ 
/* 1326 */         int i10 = this.code.entryPoint(localState);
/*      */ 
/* 1329 */         if (i8 != i1) {
/* 1330 */           if (i4 == 170) {
/* 1331 */             this.code.put4(i6 + 4 * (arrayOfInt1[i8] - k + 3), i10 - i5);
/*      */           }
/*      */           else
/*      */           {
/* 1335 */             arrayOfInt2[i8] = (i10 - i5);
/*      */           }
/*      */         }
/* 1338 */         else this.code.put4(i6, i10 - i5);
/*      */ 
/* 1342 */         genStats(localJCCase.stats, localEnv, 16);
/*      */       }
/*      */ 
/* 1346 */       this.code.resolve(((GenContext)localEnv.info).exit);
/*      */ 
/* 1349 */       if (this.code.get4(i6) == -1) {
/* 1350 */         this.code.put4(i6, this.code.entryPoint(localState) - i5);
/*      */       }
/*      */ 
/* 1353 */       if (i4 == 170)
/*      */       {
/* 1355 */         i8 = this.code.get4(i6);
/* 1356 */         for (long l6 = k; l6 <= m; l6 += 1L) {
/* 1357 */           int i11 = (int)(i6 + 4L * (l6 - k + 3L));
/* 1358 */           if (this.code.get4(i11) == -1)
/* 1359 */             this.code.put4(i11, i8);
/*      */         }
/*      */       }
/*      */       else {
/* 1363 */         if (i1 >= 0)
/* 1364 */           for (i8 = i1; i8 < arrayOfInt1.length - 1; i8++) {
/* 1365 */             arrayOfInt1[i8] = arrayOfInt1[(i8 + 1)];
/* 1366 */             arrayOfInt2[i8] = arrayOfInt2[(i8 + 1)];
/*      */           }
/* 1368 */         if (n > 0)
/* 1369 */           qsort2(arrayOfInt1, arrayOfInt2, 0, n - 1);
/* 1370 */         for (i8 = 0; i8 < n; i8++) {
/* 1371 */           int i9 = i6 + 8 * (i8 + 1);
/* 1372 */           this.code.put4(i9, arrayOfInt1[i8]);
/* 1373 */           this.code.put4(i9 + 4, arrayOfInt2[i8]);
/*      */         }
/*      */       }
/*      */     }
/* 1377 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   static void qsort2(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
/*      */   {
/* 1383 */     int i = paramInt1;
/* 1384 */     int j = paramInt2;
/* 1385 */     int k = paramArrayOfInt1[((i + j) / 2)];
/*      */     do {
/* 1387 */       while (paramArrayOfInt1[i] < k) i++;
/* 1388 */       while (k < paramArrayOfInt1[j]) j--;
/* 1389 */       if (i <= j) {
/* 1390 */         int m = paramArrayOfInt1[i];
/* 1391 */         paramArrayOfInt1[i] = paramArrayOfInt1[j];
/* 1392 */         paramArrayOfInt1[j] = m;
/* 1393 */         int n = paramArrayOfInt2[i];
/* 1394 */         paramArrayOfInt2[i] = paramArrayOfInt2[j];
/* 1395 */         paramArrayOfInt2[j] = n;
/* 1396 */         i++;
/* 1397 */         j--;
/*      */       }
/*      */     }
/* 1399 */     while (i <= j);
/* 1400 */     if (paramInt1 < j) qsort2(paramArrayOfInt1, paramArrayOfInt2, paramInt1, j);
/* 1401 */     if (i < paramInt2) qsort2(paramArrayOfInt1, paramArrayOfInt2, i, paramInt2); 
/*      */   }
/*      */ 
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized)
/*      */   {
/* 1405 */     int i = this.code.nextreg;
/*      */ 
/* 1407 */     final Items.LocalItem localLocalItem = makeTemp(this.syms.objectType);
/* 1408 */     genExpr(paramJCSynchronized.lock, paramJCSynchronized.lock.type).load().duplicate();
/* 1409 */     localLocalItem.store();
/*      */ 
/* 1412 */     this.code.emitop0(194);
/* 1413 */     this.code.state.lock(localLocalItem.reg);
/*      */ 
/* 1417 */     final Env localEnv = this.env.dup(paramJCSynchronized, new GenContext());
/* 1418 */     ((GenContext)localEnv.info).finalize = new GenFinalizer(localEnv) {
/*      */       void gen() {
/* 1420 */         genLast();
/* 1421 */         Assert.check(((Gen.GenContext)localEnv.info).gaps.length() % 2 == 0);
/* 1422 */         ((Gen.GenContext)localEnv.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/*      */       }
/*      */       void genLast() {
/* 1425 */         if (Gen.this.code.isAlive()) {
/* 1426 */           localLocalItem.load();
/* 1427 */           Gen.this.code.emitop0(195);
/* 1428 */           Gen.this.code.state.unlock(localLocalItem.reg);
/*      */         }
/*      */       }
/*      */     };
/* 1432 */     ((GenContext)localEnv.info).gaps = new ListBuffer();
/* 1433 */     genTry(paramJCSynchronized.body, List.nil(), localEnv);
/* 1434 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   public void visitTry(final JCTree.JCTry paramJCTry)
/*      */   {
/* 1440 */     final Env localEnv1 = this.env.dup(paramJCTry, new GenContext());
/* 1441 */     final Env localEnv2 = this.env;
/* 1442 */     if (!this.useJsrLocally) {
/* 1443 */       if (this.stackMap == Code.StackMapFormat.NONE) if (this.jsrlimit > 0) if (this.jsrlimit >= 100)
/*      */           {
/*      */             break label74;
/*      */           }
/* 1447 */       label74: this.useJsrLocally = 
/* 1447 */         (estimateCodeComplexity(paramJCTry.finalizer) > 
/* 1447 */         this.jsrlimit);
/*      */     }
/* 1449 */     ((GenContext)localEnv1.info).finalize = new GenFinalizer(paramJCTry) {
/*      */       void gen() {
/* 1451 */         if (Gen.this.useJsrLocally) {
/* 1452 */           if (paramJCTry.finalizer != null) {
/* 1453 */             Code.State localState = Gen.this.code.state.dup();
/* 1454 */             localState.push(Code.jsrReturnValue);
/* 1455 */             ((Gen.GenContext)localEnv1.info).cont = new Code.Chain(Gen.this.code
/* 1456 */               .emitJump(168), ((Gen.GenContext)localEnv1.info).cont, localState);
/*      */           }
/*      */ 
/* 1460 */           Assert.check(((Gen.GenContext)localEnv1.info).gaps.length() % 2 == 0);
/* 1461 */           ((Gen.GenContext)localEnv1.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/*      */         } else {
/* 1463 */           Assert.check(((Gen.GenContext)localEnv1.info).gaps.length() % 2 == 0);
/* 1464 */           ((Gen.GenContext)localEnv1.info).gaps.append(Integer.valueOf(Gen.this.code.curCP()));
/* 1465 */           genLast();
/*      */         }
/*      */       }
/*      */ 
/* 1469 */       void genLast() { if (paramJCTry.finalizer != null)
/* 1470 */           Gen.this.genStat(paramJCTry.finalizer, localEnv2, 2); }
/*      */ 
/*      */       boolean hasFinalizer() {
/* 1473 */         return paramJCTry.finalizer != null;
/*      */       }
/*      */     };
/* 1476 */     ((GenContext)localEnv1.info).gaps = new ListBuffer();
/* 1477 */     genTry(paramJCTry.body, paramJCTry.catchers, localEnv1);
/*      */   }
/*      */ 
/*      */   void genTry(JCTree paramJCTree, List<JCTree.JCCatch> paramList, Env<GenContext> paramEnv)
/*      */   {
/* 1486 */     int i = this.code.nextreg;
/* 1487 */     int j = this.code.curCP();
/* 1488 */     Code.State localState = this.code.state.dup();
/* 1489 */     genStat(paramJCTree, paramEnv, 2);
/* 1490 */     int k = this.code.curCP();
/* 1491 */     if (((GenContext)paramEnv.info).finalize != null);
/* 1493 */     int m = ((GenContext)paramEnv.info).finalize
/* 1493 */       .hasFinalizer() ? 1 : 0;
/* 1494 */     List localList = ((GenContext)paramEnv.info).gaps.toList();
/* 1495 */     this.code.statBegin(TreeInfo.endPos(paramJCTree));
/* 1496 */     genFinalizer(paramEnv);
/* 1497 */     this.code.statBegin(TreeInfo.endPos(paramEnv.tree));
/* 1498 */     Code.Chain localChain = this.code.branch(167);
/* 1499 */     endFinalizerGap(paramEnv);
/* 1500 */     if (j != k) for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*      */       {
/* 1502 */         this.code.entryPoint(localState, ((JCTree.JCCatch)((List)localObject).head).param.sym.type);
/* 1503 */         genCatch((JCTree.JCCatch)((List)localObject).head, paramEnv, j, k, localList);
/* 1504 */         genFinalizer(paramEnv);
/* 1505 */         if ((m != 0) || (((List)localObject).tail.nonEmpty())) {
/* 1506 */           this.code.statBegin(TreeInfo.endPos(paramEnv.tree));
/* 1507 */           localChain = Code.mergeChains(localChain, this.code
/* 1508 */             .branch(167));
/*      */         }
/*      */ 
/* 1510 */         endFinalizerGap(paramEnv);
/*      */       }
/* 1512 */     if (m != 0)
/*      */     {
/* 1515 */       this.code.newRegSegment();
/*      */ 
/* 1520 */       int n = this.code.entryPoint(localState, this.syms.throwableType);
/*      */ 
/* 1527 */       int i1 = j;
/* 1528 */       while (((GenContext)paramEnv.info).gaps.nonEmpty()) {
/* 1529 */         int i2 = ((Integer)((GenContext)paramEnv.info).gaps.next()).intValue();
/* 1530 */         registerCatch(paramJCTree.pos(), i1, i2, n, 0);
/*      */ 
/* 1532 */         i1 = ((Integer)((GenContext)paramEnv.info).gaps.next()).intValue();
/*      */       }
/* 1534 */       this.code.statBegin(TreeInfo.finalizerPos(paramEnv.tree));
/* 1535 */       this.code.markStatBegin();
/*      */ 
/* 1537 */       Items.LocalItem localLocalItem1 = makeTemp(this.syms.throwableType);
/* 1538 */       localLocalItem1.store();
/* 1539 */       genFinalizer(paramEnv);
/* 1540 */       localLocalItem1.load();
/* 1541 */       registerCatch(paramJCTree.pos(), i1, 
/* 1542 */         ((Integer)((GenContext)paramEnv.info).gaps
/* 1542 */         .next()).intValue(), n, 0);
/*      */ 
/* 1544 */       this.code.emitop0(191);
/* 1545 */       this.code.markDead();
/*      */ 
/* 1548 */       if (((GenContext)paramEnv.info).cont != null)
/*      */       {
/* 1550 */         this.code.resolve(((GenContext)paramEnv.info).cont);
/*      */ 
/* 1553 */         this.code.statBegin(TreeInfo.finalizerPos(paramEnv.tree));
/* 1554 */         this.code.markStatBegin();
/*      */ 
/* 1557 */         Items.LocalItem localLocalItem2 = makeTemp(this.syms.throwableType);
/* 1558 */         localLocalItem2.store();
/*      */ 
/* 1561 */         ((GenContext)paramEnv.info).finalize.genLast();
/*      */ 
/* 1564 */         this.code.emitop1w(169, localLocalItem2.reg);
/* 1565 */         this.code.markDead();
/*      */       }
/*      */     }
/*      */ 
/* 1569 */     this.code.resolve(localChain);
/*      */ 
/* 1571 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   void genCatch(JCTree.JCCatch paramJCCatch, Env<GenContext> paramEnv, int paramInt1, int paramInt2, List<Integer> paramList)
/*      */   {
/* 1584 */     if (paramInt1 != paramInt2)
/*      */     {
/* 1587 */       List localList = TreeInfo.isMultiCatch(paramJCCatch) ? ((JCTree.JCTypeUnion)paramJCCatch.param.vartype).alternatives : 
/* 1587 */         List.of(paramJCCatch.param.vartype);
/*      */       JCTree.JCExpression localJCExpression;
/*      */       Object localObject2;
/* 1588 */       while (paramList.nonEmpty()) {
/* 1589 */         for (localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localJCExpression = (JCTree.JCExpression)((Iterator)localObject1).next();
/* 1590 */           j = makeRef(paramJCCatch.pos(), localJCExpression.type);
/* 1591 */           int k = ((Integer)paramList.head).intValue();
/* 1592 */           registerCatch(paramJCCatch.pos(), paramInt1, k, this.code
/* 1593 */             .curCP(), j);
/*      */ 
/* 1595 */           if (localJCExpression.type.isAnnotated())
/*      */           {
/* 1597 */             for (localObject2 = localJCExpression.type.getAnnotationMirrors().iterator(); ((Iterator)localObject2).hasNext(); ) { Attribute.TypeCompound localTypeCompound = (Attribute.TypeCompound)((Iterator)localObject2).next();
/* 1598 */               localTypeCompound.position.type_index = j;
/*      */             }
/*      */           }
/*      */         }
/* 1602 */         paramList = paramList.tail;
/* 1603 */         paramInt1 = ((Integer)paramList.head).intValue();
/* 1604 */         paramList = paramList.tail;
/*      */       }
/* 1606 */       if (paramInt1 < paramInt2)
/* 1607 */         for (localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localJCExpression = (JCTree.JCExpression)((Iterator)localObject1).next();
/* 1608 */           j = makeRef(paramJCCatch.pos(), localJCExpression.type);
/* 1609 */           registerCatch(paramJCCatch.pos(), paramInt1, paramInt2, this.code
/* 1610 */             .curCP(), j);
/*      */ 
/* 1612 */           if (localJCExpression.type.isAnnotated())
/*      */           {
/* 1614 */             for (localIterator = localJCExpression.type.getAnnotationMirrors().iterator(); localIterator.hasNext(); ) { localObject2 = (Attribute.TypeCompound)localIterator.next();
/* 1615 */               ((Attribute.TypeCompound)localObject2).position.type_index = j;
/*      */             }
/*      */           }
/*      */         }
/*      */       Iterator localIterator;
/* 1620 */       Object localObject1 = paramJCCatch.param.sym;
/* 1621 */       this.code.statBegin(paramJCCatch.pos);
/* 1622 */       this.code.markStatBegin();
/* 1623 */       int i = this.code.nextreg;
/* 1624 */       int j = this.code.newLocal((Symbol.VarSymbol)localObject1);
/* 1625 */       this.items.makeLocalItem((Symbol.VarSymbol)localObject1).store();
/* 1626 */       this.code.statBegin(TreeInfo.firstStatPos(paramJCCatch.body));
/* 1627 */       genStat(paramJCCatch.body, paramEnv, 2);
/* 1628 */       this.code.endScopes(i);
/* 1629 */       this.code.statBegin(TreeInfo.endPos(paramJCCatch.body));
/*      */     }
/*      */   }
/*      */ 
/*      */   void registerCatch(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1638 */     int i = (char)paramInt1;
/* 1639 */     int j = (char)paramInt2;
/* 1640 */     int k = (char)paramInt3;
/* 1641 */     if ((i == paramInt1) && (j == paramInt2) && (k == paramInt3))
/*      */     {
/* 1644 */       this.code.addCatch(i, j, k, (char)paramInt4);
/*      */     }
/*      */     else {
/* 1647 */       if ((!this.useJsrLocally) && (!this.target.generateStackMapTable())) {
/* 1648 */         this.useJsrLocally = true;
/* 1649 */         throw new CodeSizeOverflow();
/*      */       }
/* 1651 */       this.log.error(paramDiagnosticPosition, "limit.code.too.large.for.try.stmt", new Object[0]);
/* 1652 */       this.nerrs += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   int estimateCodeComplexity(JCTree paramJCTree)
/*      */   {
/* 1661 */     if (paramJCTree == null) return 0;
/*      */ 
/* 1739 */     TreeScanner local1ComplexityScanner = new TreeScanner()
/*      */     {
/* 1663 */       int complexity = 0;
/*      */ 
/* 1665 */       public void scan(JCTree paramAnonymousJCTree) { if (this.complexity > Gen.this.jsrlimit) return;
/* 1666 */         super.scan(paramAnonymousJCTree); } 
/*      */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl) {
/*      */       }
/*      */       public void visitDoLoop(JCTree.JCDoWhileLoop paramAnonymousJCDoWhileLoop) {
/* 1670 */         super.visitDoLoop(paramAnonymousJCDoWhileLoop); this.complexity += 1;
/*      */       }
/* 1672 */       public void visitWhileLoop(JCTree.JCWhileLoop paramAnonymousJCWhileLoop) { super.visitWhileLoop(paramAnonymousJCWhileLoop); this.complexity += 1; } 
/*      */       public void visitForLoop(JCTree.JCForLoop paramAnonymousJCForLoop) {
/* 1674 */         super.visitForLoop(paramAnonymousJCForLoop); this.complexity += 1;
/*      */       }
/* 1676 */       public void visitSwitch(JCTree.JCSwitch paramAnonymousJCSwitch) { super.visitSwitch(paramAnonymousJCSwitch); this.complexity += 5; } 
/*      */       public void visitCase(JCTree.JCCase paramAnonymousJCCase) {
/* 1678 */         super.visitCase(paramAnonymousJCCase); this.complexity += 1;
/*      */       }
/* 1680 */       public void visitSynchronized(JCTree.JCSynchronized paramAnonymousJCSynchronized) { super.visitSynchronized(paramAnonymousJCSynchronized); this.complexity += 6; } 
/*      */       public void visitTry(JCTree.JCTry paramAnonymousJCTry) {
/* 1682 */         super.visitTry(paramAnonymousJCTry);
/* 1683 */         if (paramAnonymousJCTry.finalizer != null) this.complexity += 6; 
/*      */       }
/* 1685 */       public void visitCatch(JCTree.JCCatch paramAnonymousJCCatch) { super.visitCatch(paramAnonymousJCCatch); this.complexity += 2; } 
/*      */       public void visitConditional(JCTree.JCConditional paramAnonymousJCConditional) {
/* 1687 */         super.visitConditional(paramAnonymousJCConditional); this.complexity += 2;
/*      */       }
/* 1689 */       public void visitIf(JCTree.JCIf paramAnonymousJCIf) { super.visitIf(paramAnonymousJCIf); this.complexity += 2; }
/*      */ 
/*      */       public void visitBreak(JCTree.JCBreak paramAnonymousJCBreak) {
/* 1692 */         super.visitBreak(paramAnonymousJCBreak); this.complexity += 1;
/*      */       }
/* 1694 */       public void visitContinue(JCTree.JCContinue paramAnonymousJCContinue) { super.visitContinue(paramAnonymousJCContinue); this.complexity += 1; } 
/*      */       public void visitReturn(JCTree.JCReturn paramAnonymousJCReturn) {
/* 1696 */         super.visitReturn(paramAnonymousJCReturn); this.complexity += 1;
/*      */       }
/* 1698 */       public void visitThrow(JCTree.JCThrow paramAnonymousJCThrow) { super.visitThrow(paramAnonymousJCThrow); this.complexity += 1; } 
/*      */       public void visitAssert(JCTree.JCAssert paramAnonymousJCAssert) {
/* 1700 */         super.visitAssert(paramAnonymousJCAssert); this.complexity += 5;
/*      */       }
/* 1702 */       public void visitApply(JCTree.JCMethodInvocation paramAnonymousJCMethodInvocation) { super.visitApply(paramAnonymousJCMethodInvocation); this.complexity += 2; } 
/*      */       public void visitNewClass(JCTree.JCNewClass paramAnonymousJCNewClass) {
/* 1704 */         scan(paramAnonymousJCNewClass.encl); scan(paramAnonymousJCNewClass.args); this.complexity += 2;
/*      */       }
/* 1706 */       public void visitNewArray(JCTree.JCNewArray paramAnonymousJCNewArray) { super.visitNewArray(paramAnonymousJCNewArray); this.complexity += 5; } 
/*      */       public void visitAssign(JCTree.JCAssign paramAnonymousJCAssign) {
/* 1708 */         super.visitAssign(paramAnonymousJCAssign); this.complexity += 1;
/*      */       }
/* 1710 */       public void visitAssignop(JCTree.JCAssignOp paramAnonymousJCAssignOp) { super.visitAssignop(paramAnonymousJCAssignOp); this.complexity += 2; } 
/*      */       public void visitUnary(JCTree.JCUnary paramAnonymousJCUnary) {
/* 1712 */         this.complexity += 1;
/* 1713 */         if (paramAnonymousJCUnary.type.constValue() == null) super.visitUnary(paramAnonymousJCUnary); 
/*      */       }
/* 1715 */       public void visitBinary(JCTree.JCBinary paramAnonymousJCBinary) { this.complexity += 1;
/* 1716 */         if (paramAnonymousJCBinary.type.constValue() == null) super.visitBinary(paramAnonymousJCBinary);  } 
/*      */       public void visitTypeTest(JCTree.JCInstanceOf paramAnonymousJCInstanceOf) {
/* 1718 */         super.visitTypeTest(paramAnonymousJCInstanceOf); this.complexity += 1;
/*      */       }
/* 1720 */       public void visitIndexed(JCTree.JCArrayAccess paramAnonymousJCArrayAccess) { super.visitIndexed(paramAnonymousJCArrayAccess); this.complexity += 1; } 
/*      */       public void visitSelect(JCTree.JCFieldAccess paramAnonymousJCFieldAccess) {
/* 1722 */         super.visitSelect(paramAnonymousJCFieldAccess);
/* 1723 */         if (paramAnonymousJCFieldAccess.sym.kind == 4) this.complexity += 1; 
/*      */       }
/* 1725 */       public void visitIdent(JCTree.JCIdent paramAnonymousJCIdent) { if (paramAnonymousJCIdent.sym.kind == 4) {
/* 1726 */           this.complexity += 1;
/* 1727 */           if ((paramAnonymousJCIdent.type.constValue() == null) && (paramAnonymousJCIdent.sym.owner.kind == 2))
/*      */           {
/* 1729 */             this.complexity += 1;
/*      */           }
/*      */         } } 
/*      */       public void visitLiteral(JCTree.JCLiteral paramAnonymousJCLiteral) {
/* 1733 */         this.complexity += 1;
/*      */       }
/*      */       public void visitTree(JCTree paramAnonymousJCTree) {  } 
/* 1736 */       public void visitWildcard(JCTree.JCWildcard paramAnonymousJCWildcard) { throw new AssertionError(getClass().getName()); }
/*      */ 
/*      */     };
/* 1740 */     paramJCTree.accept(local1ComplexityScanner);
/* 1741 */     return local1ComplexityScanner.complexity;
/*      */   }
/*      */ 
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 1745 */     int i = this.code.nextreg;
/* 1746 */     Code.Chain localChain1 = null;
/* 1747 */     Items.CondItem localCondItem = genCond(TreeInfo.skipParens(paramJCIf.cond), 8);
/*      */ 
/* 1749 */     Code.Chain localChain2 = localCondItem.jumpFalse();
/* 1750 */     if (!localCondItem.isFalse()) {
/* 1751 */       this.code.resolve(localCondItem.trueJumps);
/* 1752 */       genStat(paramJCIf.thenpart, this.env, 17);
/* 1753 */       localChain1 = this.code.branch(167);
/*      */     }
/* 1755 */     if (localChain2 != null) {
/* 1756 */       this.code.resolve(localChain2);
/* 1757 */       if (paramJCIf.elsepart != null) {
/* 1758 */         genStat(paramJCIf.elsepart, this.env, 17);
/*      */       }
/*      */     }
/* 1761 */     this.code.resolve(localChain1);
/* 1762 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement)
/*      */   {
/* 1767 */     JCTree.JCExpression localJCExpression = paramJCExpressionStatement.expr;
/* 1768 */     switch (localJCExpression.getTag()) {
/*      */     case POSTINC:
/* 1770 */       ((JCTree.JCUnary)localJCExpression).setTag(JCTree.Tag.PREINC);
/* 1771 */       break;
/*      */     case POSTDEC:
/* 1773 */       ((JCTree.JCUnary)localJCExpression).setTag(JCTree.Tag.PREDEC);
/*      */     }
/*      */ 
/* 1776 */     genExpr(paramJCExpressionStatement.expr, paramJCExpressionStatement.expr.type).drop();
/*      */   }
/*      */ 
/*      */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 1780 */     Env localEnv = unwind(paramJCBreak.target, this.env);
/* 1781 */     Assert.check(this.code.state.stacksize == 0);
/* 1782 */     ((GenContext)localEnv.info).addExit(this.code.branch(167));
/* 1783 */     endFinalizerGaps(this.env, localEnv);
/*      */   }
/*      */ 
/*      */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 1787 */     Env localEnv = unwind(paramJCContinue.target, this.env);
/* 1788 */     Assert.check(this.code.state.stacksize == 0);
/* 1789 */     ((GenContext)localEnv.info).addCont(this.code.branch(167));
/* 1790 */     endFinalizerGaps(this.env, localEnv);
/*      */   }
/*      */ 
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 1794 */     int i = this.code.nextreg;
/*      */     Env localEnv;
/* 1796 */     if (paramJCReturn.expr != null) {
/* 1797 */       Object localObject = genExpr(paramJCReturn.expr, this.pt).load();
/* 1798 */       if (hasFinally(this.env.enclMethod, this.env)) {
/* 1799 */         localObject = makeTemp(this.pt);
/* 1800 */         ((Items.Item)localObject).store();
/*      */       }
/* 1802 */       localEnv = unwind(this.env.enclMethod, this.env);
/* 1803 */       ((Items.Item)localObject).load();
/* 1804 */       this.code.emitop0(172 + Code.truncate(Code.typecode(this.pt)));
/*      */     }
/*      */     else
/*      */     {
/* 1813 */       int j = this.code.pendingStatPos;
/* 1814 */       localEnv = unwind(this.env.enclMethod, this.env);
/* 1815 */       this.code.pendingStatPos = j;
/* 1816 */       this.code.emitop0(177);
/*      */     }
/* 1818 */     endFinalizerGaps(this.env, localEnv);
/* 1819 */     this.code.endScopes(i);
/*      */   }
/*      */ 
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 1823 */     genExpr(paramJCThrow.expr, paramJCThrow.expr.type).load();
/* 1824 */     this.code.emitop0(191);
/*      */   }
/*      */ 
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */   {
/* 1832 */     setTypeAnnotationPositions(paramJCMethodInvocation.pos);
/*      */ 
/* 1834 */     Items.Item localItem = genExpr(paramJCMethodInvocation.meth, this.methodType);
/*      */ 
/* 1838 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)TreeInfo.symbol(paramJCMethodInvocation.meth);
/* 1839 */     genArgs(paramJCMethodInvocation.args, localMethodSymbol
/* 1840 */       .externalType(this.types)
/* 1840 */       .getParameterTypes());
/* 1841 */     if (!localMethodSymbol.isDynamic()) {
/* 1842 */       this.code.statBegin(paramJCMethodInvocation.pos);
/*      */     }
/* 1844 */     this.result = localItem.invoke();
/*      */   }
/*      */ 
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 1848 */     Code.Chain localChain1 = null;
/* 1849 */     Items.CondItem localCondItem = genCond(paramJCConditional.cond, 8);
/* 1850 */     Code.Chain localChain2 = localCondItem.jumpFalse();
/*      */     int i;
/* 1851 */     if (!localCondItem.isFalse()) {
/* 1852 */       this.code.resolve(localCondItem.trueJumps);
/* 1853 */       i = this.genCrt ? this.code.curCP() : 0;
/* 1854 */       genExpr(paramJCConditional.truepart, this.pt).load();
/* 1855 */       this.code.state.forceStackTop(paramJCConditional.type);
/* 1856 */       if (this.genCrt) this.code.crt.put(paramJCConditional.truepart, 16, i, this.code
/* 1857 */           .curCP());
/* 1858 */       localChain1 = this.code.branch(167);
/*      */     }
/* 1860 */     if (localChain2 != null) {
/* 1861 */       this.code.resolve(localChain2);
/* 1862 */       i = this.genCrt ? this.code.curCP() : 0;
/* 1863 */       genExpr(paramJCConditional.falsepart, this.pt).load();
/* 1864 */       this.code.state.forceStackTop(paramJCConditional.type);
/* 1865 */       if (this.genCrt) this.code.crt.put(paramJCConditional.falsepart, 16, i, this.code
/* 1866 */           .curCP());
/*      */     }
/* 1868 */     this.code.resolve(localChain1);
/* 1869 */     this.result = this.items.makeStackItem(this.pt);
/*      */   }
/*      */ 
/*      */   private void setTypeAnnotationPositions(int paramInt) {
/* 1873 */     Symbol.MethodSymbol localMethodSymbol = this.code.meth;
/*      */ 
/* 1875 */     int i = (this.code.meth.getKind() == ElementKind.CONSTRUCTOR) || 
/* 1875 */       (this.code.meth
/* 1875 */       .getKind() == ElementKind.STATIC_INIT) ? 1 : 0;
/*      */ 
/* 1877 */     for (Object localObject1 = localMethodSymbol.getRawTypeAttributes().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Attribute.TypeCompound)((Iterator)localObject1).next();
/* 1878 */       if (((Attribute.TypeCompound)localObject2).hasUnknownPosition()) {
/* 1879 */         ((Attribute.TypeCompound)localObject2).tryFixPosition();
/*      */       }
/* 1881 */       if (((Attribute.TypeCompound)localObject2).position.matchesPos(paramInt)) {
/* 1882 */         ((Attribute.TypeCompound)localObject2).position.updatePosOffset(this.code.cp);
/*      */       }
/*      */     }
/* 1885 */     if (i == 0) {
/* 1886 */       return;
/*      */     }
/* 1888 */     for (localObject1 = localMethodSymbol.owner.getRawTypeAttributes().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Attribute.TypeCompound)((Iterator)localObject1).next();
/* 1889 */       if (((Attribute.TypeCompound)localObject2).hasUnknownPosition()) {
/* 1890 */         ((Attribute.TypeCompound)localObject2).tryFixPosition();
/*      */       }
/* 1892 */       if (((Attribute.TypeCompound)localObject2).position.matchesPos(paramInt)) {
/* 1893 */         ((Attribute.TypeCompound)localObject2).position.updatePosOffset(this.code.cp);
/*      */       }
/*      */     }
/* 1896 */     localObject1 = localMethodSymbol.enclClass();
/* 1897 */     for (Object localObject2 = new FilteredMemberList(((Symbol.ClassSymbol)localObject1).members()).iterator(); ((Iterator)localObject2).hasNext(); ) { Symbol localSymbol = (Symbol)((Iterator)localObject2).next();
/* 1898 */       if (localSymbol.getKind().isField())
/*      */       {
/* 1901 */         for (Attribute.TypeCompound localTypeCompound : localSymbol.getRawTypeAttributes()) {
/* 1902 */           if (localTypeCompound.hasUnknownPosition()) {
/* 1903 */             localTypeCompound.tryFixPosition();
/*      */           }
/* 1905 */           if (localTypeCompound.position.matchesPos(paramInt))
/* 1906 */             localTypeCompound.position.updatePosOffset(this.code.cp);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */   {
/* 1914 */     Assert.check((paramJCNewClass.encl == null) && (paramJCNewClass.def == null));
/* 1915 */     setTypeAnnotationPositions(paramJCNewClass.pos);
/*      */ 
/* 1917 */     this.code.emitop2(187, makeRef(paramJCNewClass.pos(), paramJCNewClass.type));
/* 1918 */     this.code.emitop0(89);
/*      */ 
/* 1923 */     genArgs(paramJCNewClass.args, paramJCNewClass.constructor.externalType(this.types).getParameterTypes());
/*      */ 
/* 1925 */     this.items.makeMemberItem(paramJCNewClass.constructor, true).invoke();
/* 1926 */     this.result = this.items.makeStackItem(paramJCNewClass.type);
/*      */   }
/*      */ 
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 1930 */     setTypeAnnotationPositions(paramJCNewArray.pos);
/*      */     Object localObject;
/* 1932 */     if (paramJCNewArray.elems != null) {
/* 1933 */       localObject = this.types.elemtype(paramJCNewArray.type);
/* 1934 */       loadIntConst(paramJCNewArray.elems.length());
/* 1935 */       Items.Item localItem = makeNewArray(paramJCNewArray.pos(), paramJCNewArray.type, 1);
/* 1936 */       int i = 0;
/* 1937 */       for (List localList = paramJCNewArray.elems; localList.nonEmpty(); localList = localList.tail) {
/* 1938 */         localItem.duplicate();
/* 1939 */         loadIntConst(i);
/* 1940 */         i++;
/* 1941 */         genExpr((JCTree)localList.head, (Type)localObject).load();
/* 1942 */         this.items.makeIndexedItem((Type)localObject).store();
/*      */       }
/* 1944 */       this.result = localItem;
/*      */     } else {
/* 1946 */       for (localObject = paramJCNewArray.dims; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 1947 */         genExpr((JCTree)((List)localObject).head, this.syms.intType).load();
/*      */       }
/* 1949 */       this.result = makeNewArray(paramJCNewArray.pos(), paramJCNewArray.type, paramJCNewArray.dims.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   Items.Item makeNewArray(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, int paramInt)
/*      */   {
/* 1957 */     Type localType = this.types.elemtype(paramType);
/* 1958 */     if (this.types.dimensions(paramType) > 255) {
/* 1959 */       this.log.error(paramDiagnosticPosition, "limit.dimensions", new Object[0]);
/* 1960 */       this.nerrs += 1;
/*      */     }
/* 1962 */     int i = Code.arraycode(localType);
/* 1963 */     if ((i == 0) || ((i == 1) && (paramInt == 1)))
/* 1964 */       this.code.emitAnewarray(makeRef(paramDiagnosticPosition, localType), paramType);
/* 1965 */     else if (i == 1)
/* 1966 */       this.code.emitMultianewarray(paramInt, makeRef(paramDiagnosticPosition, paramType), paramType);
/*      */     else {
/* 1968 */       this.code.emitNewarray(i, paramType);
/*      */     }
/* 1970 */     return this.items.makeStackItem(paramType);
/*      */   }
/*      */ 
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 1974 */     this.result = genExpr(paramJCParens.expr, paramJCParens.expr.type);
/*      */   }
/*      */ 
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 1978 */     Items.Item localItem = genExpr(paramJCAssign.lhs, paramJCAssign.lhs.type);
/* 1979 */     genExpr(paramJCAssign.rhs, paramJCAssign.lhs.type).load();
/* 1980 */     this.result = this.items.makeAssignItem(localItem);
/*      */   }
/*      */ 
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 1984 */     Symbol.OperatorSymbol localOperatorSymbol = (Symbol.OperatorSymbol)paramJCAssignOp.operator;
/*      */     Items.Item localItem;
/* 1986 */     if (localOperatorSymbol.opcode == 256)
/*      */     {
/* 1988 */       makeStringBuffer(paramJCAssignOp.pos());
/*      */ 
/* 1992 */       localItem = genExpr(paramJCAssignOp.lhs, paramJCAssignOp.lhs.type);
/* 1993 */       if (localItem.width() > 0) {
/* 1994 */         this.code.emitop0(90 + 3 * (localItem.width() - 1));
/*      */       }
/*      */ 
/* 1998 */       localItem.load();
/* 1999 */       appendString(paramJCAssignOp.lhs);
/*      */ 
/* 2002 */       appendStrings(paramJCAssignOp.rhs);
/*      */ 
/* 2005 */       bufferToString(paramJCAssignOp.pos());
/*      */     }
/*      */     else {
/* 2008 */       localItem = genExpr(paramJCAssignOp.lhs, paramJCAssignOp.lhs.type);
/*      */ 
/* 2013 */       if (((paramJCAssignOp.hasTag(JCTree.Tag.PLUS_ASG)) || (paramJCAssignOp.hasTag(JCTree.Tag.MINUS_ASG))) && ((localItem instanceof Items.LocalItem)))
/*      */       {
/* 2015 */         if ((paramJCAssignOp.lhs.type
/* 2015 */           .getTag().isSubRangeOf(TypeTag.INT)) && 
/* 2016 */           (paramJCAssignOp.rhs.type
/* 2016 */           .getTag().isSubRangeOf(TypeTag.INT)) && 
/* 2017 */           (paramJCAssignOp.rhs.type
/* 2017 */           .constValue() != null)) {
/* 2018 */           int i = ((Number)paramJCAssignOp.rhs.type.constValue()).intValue();
/* 2019 */           if (paramJCAssignOp.hasTag(JCTree.Tag.MINUS_ASG)) i = -i;
/* 2020 */           ((Items.LocalItem)localItem).incr(i);
/* 2021 */           this.result = localItem;
/* 2022 */           return;
/*      */         }
/*      */       }
/*      */ 
/* 2026 */       localItem.duplicate();
/* 2027 */       localItem.coerce((Type)localOperatorSymbol.type.getParameterTypes().head).load();
/* 2028 */       completeBinop(paramJCAssignOp.lhs, paramJCAssignOp.rhs, localOperatorSymbol).coerce(paramJCAssignOp.lhs.type);
/*      */     }
/* 2030 */     this.result = this.items.makeAssignItem(localItem);
/*      */   }
/*      */ 
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 2034 */     Symbol.OperatorSymbol localOperatorSymbol = (Symbol.OperatorSymbol)paramJCUnary.operator;
/*      */     Object localObject;
/* 2035 */     if (paramJCUnary.hasTag(JCTree.Tag.NOT)) {
/* 2036 */       localObject = genCond(paramJCUnary.arg, false);
/* 2037 */       this.result = ((Items.CondItem)localObject).negate();
/*      */     } else {
/* 2039 */       localObject = genExpr(paramJCUnary.arg, (Type)localOperatorSymbol.type.getParameterTypes().head);
/* 2040 */       switch (3.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCUnary.getTag().ordinal()]) {
/*      */       case 6:
/* 2042 */         this.result = ((Items.Item)localObject).load();
/* 2043 */         break;
/*      */       case 7:
/* 2045 */         this.result = ((Items.Item)localObject).load();
/* 2046 */         this.code.emitop0(localOperatorSymbol.opcode);
/* 2047 */         break;
/*      */       case 8:
/* 2049 */         this.result = ((Items.Item)localObject).load();
/* 2050 */         emitMinusOne(((Items.Item)localObject).typecode);
/* 2051 */         this.code.emitop0(localOperatorSymbol.opcode);
/* 2052 */         break;
/*      */       case 9:
/*      */       case 10:
/* 2054 */         ((Items.Item)localObject).duplicate();
/* 2055 */         if (((localObject instanceof Items.LocalItem)) && ((localOperatorSymbol.opcode == 96) || (localOperatorSymbol.opcode == 100)))
/*      */         {
/* 2057 */           ((Items.LocalItem)localObject).incr(paramJCUnary.hasTag(JCTree.Tag.PREINC) ? 1 : -1);
/* 2058 */           this.result = ((Items.Item)localObject);
/*      */         } else {
/* 2060 */           ((Items.Item)localObject).load();
/* 2061 */           this.code.emitop0(one(((Items.Item)localObject).typecode));
/* 2062 */           this.code.emitop0(localOperatorSymbol.opcode);
/*      */ 
/* 2065 */           if ((((Items.Item)localObject).typecode != 0) && 
/* 2066 */             (Code.truncate(((Items.Item)localObject).typecode) == 0))
/*      */           {
/* 2067 */             this.code.emitop0(145 + ((Items.Item)localObject).typecode - 5);
/* 2068 */           }this.result = this.items.makeAssignItem((Items.Item)localObject);
/*      */         }
/* 2070 */         break;
/*      */       case 4:
/*      */       case 5:
/* 2072 */         ((Items.Item)localObject).duplicate();
/*      */         Items.Item localItem;
/* 2073 */         if (((localObject instanceof Items.LocalItem)) && ((localOperatorSymbol.opcode == 96) || (localOperatorSymbol.opcode == 100)))
/*      */         {
/* 2075 */           localItem = ((Items.Item)localObject).load();
/* 2076 */           ((Items.LocalItem)localObject).incr(paramJCUnary.hasTag(JCTree.Tag.POSTINC) ? 1 : -1);
/* 2077 */           this.result = localItem;
/*      */         } else {
/* 2079 */           localItem = ((Items.Item)localObject).load();
/* 2080 */           ((Items.Item)localObject).stash(((Items.Item)localObject).typecode);
/* 2081 */           this.code.emitop0(one(((Items.Item)localObject).typecode));
/* 2082 */           this.code.emitop0(localOperatorSymbol.opcode);
/*      */ 
/* 2085 */           if ((((Items.Item)localObject).typecode != 0) && 
/* 2086 */             (Code.truncate(((Items.Item)localObject).typecode) == 0))
/*      */           {
/* 2087 */             this.code.emitop0(145 + ((Items.Item)localObject).typecode - 5);
/* 2088 */           }((Items.Item)localObject).store();
/* 2089 */           this.result = localItem;
/*      */         }
/* 2091 */         break;
/*      */       case 11:
/* 2093 */         this.result = ((Items.Item)localObject).load();
/* 2094 */         this.code.emitop0(89);
/* 2095 */         genNullCheck(paramJCUnary.pos());
/* 2096 */         break;
/*      */       default:
/* 2098 */         Assert.error();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void genNullCheck(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2105 */     callMethod(paramDiagnosticPosition, this.syms.objectType, this.names.getClass, 
/* 2106 */       List.nil(), false);
/* 2107 */     this.code.emitop0(87);
/*      */   }
/*      */ 
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 2111 */     Symbol.OperatorSymbol localOperatorSymbol = (Symbol.OperatorSymbol)paramJCBinary.operator;
/* 2112 */     if (localOperatorSymbol.opcode == 256)
/*      */     {
/* 2114 */       makeStringBuffer(paramJCBinary.pos());
/*      */ 
/* 2116 */       appendStrings(paramJCBinary);
/*      */ 
/* 2118 */       bufferToString(paramJCBinary.pos());
/* 2119 */       this.result = this.items.makeStackItem(this.syms.stringType);
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject;
/*      */       Code.Chain localChain;
/*      */       Items.CondItem localCondItem;
/* 2120 */       if (paramJCBinary.hasTag(JCTree.Tag.AND)) {
/* 2121 */         localObject = genCond(paramJCBinary.lhs, 8);
/* 2122 */         if (!((Items.CondItem)localObject).isFalse()) {
/* 2123 */           localChain = ((Items.CondItem)localObject).jumpFalse();
/* 2124 */           this.code.resolve(((Items.CondItem)localObject).trueJumps);
/* 2125 */           localCondItem = genCond(paramJCBinary.rhs, 16);
/* 2126 */           this.result = this.items
/* 2127 */             .makeCondItem(localCondItem.opcode, localCondItem.trueJumps, 
/* 2129 */             Code.mergeChains(localChain, localCondItem.falseJumps));
/*      */         }
/*      */         else
/*      */         {
/* 2132 */           this.result = ((Items.Item)localObject);
/*      */         }
/* 2134 */       } else if (paramJCBinary.hasTag(JCTree.Tag.OR)) {
/* 2135 */         localObject = genCond(paramJCBinary.lhs, 8);
/* 2136 */         if (!((Items.CondItem)localObject).isTrue()) {
/* 2137 */           localChain = ((Items.CondItem)localObject).jumpTrue();
/* 2138 */           this.code.resolve(((Items.CondItem)localObject).falseJumps);
/* 2139 */           localCondItem = genCond(paramJCBinary.rhs, 16);
/* 2140 */           this.result = this.items
/* 2141 */             .makeCondItem(localCondItem.opcode, 
/* 2142 */             Code.mergeChains(localChain, localCondItem.trueJumps), 
/* 2142 */             localCondItem.falseJumps);
/*      */         }
/*      */         else {
/* 2145 */           this.result = ((Items.Item)localObject);
/*      */         }
/*      */       } else {
/* 2148 */         localObject = genExpr(paramJCBinary.lhs, (Type)localOperatorSymbol.type.getParameterTypes().head);
/* 2149 */         ((Items.Item)localObject).load();
/* 2150 */         this.result = completeBinop(paramJCBinary.lhs, paramJCBinary.rhs, localOperatorSymbol);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void makeStringBuffer(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2157 */     this.code.emitop2(187, makeRef(paramDiagnosticPosition, this.stringBufferType));
/* 2158 */     this.code.emitop0(89);
/* 2159 */     callMethod(paramDiagnosticPosition, this.stringBufferType, this.names.init, 
/* 2160 */       List.nil(), false);
/*      */   }
/*      */ 
/*      */   void appendString(JCTree paramJCTree)
/*      */   {
/* 2166 */     Type localType = paramJCTree.type.baseType();
/* 2167 */     if ((!localType.isPrimitive()) && (localType.tsym != this.syms.stringType.tsym)) {
/* 2168 */       localType = this.syms.objectType;
/*      */     }
/* 2170 */     this.items.makeMemberItem(getStringBufferAppend(paramJCTree, localType), false).invoke();
/*      */   }
/*      */   Symbol getStringBufferAppend(JCTree paramJCTree, Type paramType) {
/* 2173 */     Assert.checkNull(paramType.constValue());
/* 2174 */     Object localObject = (Symbol)this.stringBufferAppend.get(paramType);
/* 2175 */     if (localObject == null) {
/* 2176 */       localObject = this.rs.resolveInternalMethod(paramJCTree.pos(), this.attrEnv, this.stringBufferType, this.names.append, 
/* 2180 */         List.of(paramType), 
/* 2180 */         null);
/*      */ 
/* 2182 */       this.stringBufferAppend.put(paramType, localObject);
/*      */     }
/* 2184 */     return localObject;
/*      */   }
/*      */ 
/*      */   void appendStrings(JCTree paramJCTree)
/*      */   {
/* 2190 */     paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2191 */     if ((paramJCTree.hasTag(JCTree.Tag.PLUS)) && (paramJCTree.type.constValue() == null)) {
/* 2192 */       JCTree.JCBinary localJCBinary = (JCTree.JCBinary)paramJCTree;
/* 2193 */       if ((localJCBinary.operator.kind == 16) && (((Symbol.OperatorSymbol)localJCBinary.operator).opcode == 256))
/*      */       {
/* 2195 */         appendStrings(localJCBinary.lhs);
/* 2196 */         appendStrings(localJCBinary.rhs);
/* 2197 */         return;
/*      */       }
/*      */     }
/* 2200 */     genExpr(paramJCTree, paramJCTree.type).load();
/* 2201 */     appendString(paramJCTree);
/*      */   }
/*      */ 
/*      */   void bufferToString(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2207 */     callMethod(paramDiagnosticPosition, this.stringBufferType, this.names.toString, 
/* 2211 */       List.nil(), false);
/*      */   }
/*      */ 
/*      */   Items.Item completeBinop(JCTree paramJCTree1, JCTree paramJCTree2, Symbol.OperatorSymbol paramOperatorSymbol)
/*      */   {
/* 2222 */     Type.MethodType localMethodType = (Type.MethodType)paramOperatorSymbol.type;
/* 2223 */     int i = paramOperatorSymbol.opcode;
/* 2224 */     if ((i >= 159) && (i <= 164) && 
/* 2225 */       ((paramJCTree2.type
/* 2225 */       .constValue() instanceof Number)) && 
/* 2226 */       (((Number)paramJCTree2.type
/* 2226 */       .constValue()).intValue() == 0)) {
/* 2227 */       i += -6;
/* 2228 */     } else if ((i >= 165) && (i <= 166) && 
/* 2229 */       (TreeInfo.isNull(paramJCTree2)))
/*      */     {
/* 2230 */       i += 33;
/*      */     }
/*      */     else
/*      */     {
/* 2236 */       Object localObject = (Type)paramOperatorSymbol.erasure(this.types).getParameterTypes().tail.head;
/* 2237 */       if ((i >= 270) && (i <= 275)) {
/* 2238 */         i += -150;
/* 2239 */         localObject = this.syms.intType;
/*      */       }
/*      */ 
/* 2242 */       genExpr(paramJCTree2, (Type)localObject).load();
/*      */ 
/* 2245 */       if (i >= 512) {
/* 2246 */         this.code.emitop0(i >> 9);
/* 2247 */         i &= 255;
/*      */       }
/*      */     }
/* 2250 */     if (((i >= 153) && (i <= 166)) || (i == 198) || (i == 199))
/*      */     {
/* 2252 */       return this.items.makeCondItem(i);
/*      */     }
/* 2254 */     this.code.emitop0(i);
/* 2255 */     return this.items.makeStackItem(localMethodType.restype);
/*      */   }
/*      */ 
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*      */   {
/* 2260 */     setTypeAnnotationPositions(paramJCTypeCast.pos);
/* 2261 */     this.result = genExpr(paramJCTypeCast.expr, paramJCTypeCast.clazz.type).load();
/*      */ 
/* 2266 */     if ((!paramJCTypeCast.clazz.type.isPrimitive()) && 
/* 2267 */       (this.types
/* 2267 */       .asSuper(paramJCTypeCast.expr.type, paramJCTypeCast.clazz.type.tsym) == null))
/*      */     {
/* 2268 */       this.code.emitop2(192, makeRef(paramJCTypeCast.pos(), paramJCTypeCast.clazz.type));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/* 2273 */     throw new AssertionError(getClass().getName());
/*      */   }
/*      */ 
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 2277 */     setTypeAnnotationPositions(paramJCInstanceOf.pos);
/* 2278 */     genExpr(paramJCInstanceOf.expr, paramJCInstanceOf.expr.type).load();
/* 2279 */     this.code.emitop2(193, makeRef(paramJCInstanceOf.pos(), paramJCInstanceOf.clazz.type));
/* 2280 */     this.result = this.items.makeStackItem(this.syms.booleanType);
/*      */   }
/*      */ 
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 2284 */     genExpr(paramJCArrayAccess.indexed, paramJCArrayAccess.indexed.type).load();
/* 2285 */     genExpr(paramJCArrayAccess.index, this.syms.intType).load();
/* 2286 */     this.result = this.items.makeIndexedItem(paramJCArrayAccess.type);
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 2290 */     Symbol localSymbol = paramJCIdent.sym;
/* 2291 */     if ((paramJCIdent.name == this.names._this) || (paramJCIdent.name == this.names._super))
/*      */     {
/* 2294 */       Items.Item localItem = paramJCIdent.name == this.names._this ? this.items
/* 2293 */         .makeThisItem() : this.items
/* 2294 */         .makeSuperItem();
/* 2295 */       if (localSymbol.kind == 16)
/*      */       {
/* 2297 */         localItem.load();
/* 2298 */         localItem = this.items.makeMemberItem(localSymbol, true);
/*      */       }
/* 2300 */       this.result = localItem;
/* 2301 */     } else if ((localSymbol.kind == 4) && (localSymbol.owner.kind == 16)) {
/* 2302 */       this.result = this.items.makeLocalItem((Symbol.VarSymbol)localSymbol);
/* 2303 */     } else if (isInvokeDynamic(localSymbol)) {
/* 2304 */       this.result = this.items.makeDynamicItem(localSymbol);
/* 2305 */     } else if ((localSymbol.flags() & 0x8) != 0L) {
/* 2306 */       if (!isAccessSuper(this.env.enclMethod))
/* 2307 */         localSymbol = binaryQualifier(localSymbol, this.env.enclClass.type);
/* 2308 */       this.result = this.items.makeStaticItem(localSymbol);
/*      */     } else {
/* 2310 */       this.items.makeThisItem().load();
/* 2311 */       localSymbol = binaryQualifier(localSymbol, this.env.enclClass.type);
/* 2312 */       this.result = this.items.makeMemberItem(localSymbol, (localSymbol.flags() & 0x2) != 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 2317 */     Symbol localSymbol1 = paramJCFieldAccess.sym;
/*      */ 
/* 2319 */     if (paramJCFieldAccess.name == this.names._class) {
/* 2320 */       Assert.check(this.target.hasClassLiterals());
/* 2321 */       this.code.emitLdc(makeRef(paramJCFieldAccess.pos(), paramJCFieldAccess.selected.type));
/* 2322 */       this.result = this.items.makeStackItem(this.pt);
/* 2323 */       return;
/*      */     }
/*      */ 
/* 2326 */     Symbol localSymbol2 = TreeInfo.symbol(paramJCFieldAccess.selected);
/*      */ 
/* 2329 */     int i = (localSymbol2 != null) && ((localSymbol2.kind == 2) || (localSymbol2.name == this.names._super)) ? 1 : 0;
/*      */ 
/* 2334 */     boolean bool = isAccessSuper(this.env.enclMethod);
/*      */ 
/* 2338 */     Items.Item localItem = i != 0 ? this.items
/* 2337 */       .makeSuperItem() : 
/* 2338 */       genExpr(paramJCFieldAccess.selected, paramJCFieldAccess.selected.type);
/*      */ 
/* 2340 */     if ((localSymbol1.kind == 4) && (((Symbol.VarSymbol)localSymbol1).getConstValue() != null))
/*      */     {
/* 2343 */       if ((localSymbol1.flags() & 0x8) != 0L) {
/* 2344 */         if ((i == 0) && ((localSymbol2 == null) || (localSymbol2.kind != 2)))
/* 2345 */           localItem = localItem.load();
/* 2346 */         localItem.drop();
/*      */       } else {
/* 2348 */         localItem.load();
/* 2349 */         genNullCheck(paramJCFieldAccess.selected.pos());
/*      */       }
/* 2351 */       this.result = this.items
/* 2352 */         .makeImmediateItem(localSymbol1.type, ((Symbol.VarSymbol)localSymbol1)
/* 2352 */         .getConstValue());
/*      */     } else {
/* 2354 */       if (isInvokeDynamic(localSymbol1)) {
/* 2355 */         this.result = this.items.makeDynamicItem(localSymbol1);
/* 2356 */         return;
/*      */       }
/* 2358 */       localSymbol1 = binaryQualifier(localSymbol1, paramJCFieldAccess.selected.type);
/*      */ 
/* 2360 */       if ((localSymbol1.flags() & 0x8) != 0L) {
/* 2361 */         if ((i == 0) && ((localSymbol2 == null) || (localSymbol2.kind != 2)))
/* 2362 */           localItem = localItem.load();
/* 2363 */         localItem.drop();
/* 2364 */         this.result = this.items.makeStaticItem(localSymbol1);
/*      */       } else {
/* 2366 */         localItem.load();
/* 2367 */         if (localSymbol1 == this.syms.lengthVar) {
/* 2368 */           this.code.emitop0(190);
/* 2369 */           this.result = this.items.makeStackItem(this.syms.intType);
/*      */         } else {
/* 2371 */           this.result = this.items
/* 2372 */             .makeMemberItem(localSymbol1, 
/* 2373 */             ((localSymbol1
/* 2373 */             .flags() & 0x2) != 0L) || (i != 0) || (bool));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isInvokeDynamic(Symbol paramSymbol)
/*      */   {
/* 2381 */     return (paramSymbol.kind == 16) && (((Symbol.MethodSymbol)paramSymbol).isDynamic());
/*      */   }
/*      */ 
/*      */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/* 2385 */     if (paramJCLiteral.type.hasTag(TypeTag.BOT)) {
/* 2386 */       this.code.emitop0(1);
/* 2387 */       if (this.types.dimensions(this.pt) > 1) {
/* 2388 */         this.code.emitop2(192, makeRef(paramJCLiteral.pos(), this.pt));
/* 2389 */         this.result = this.items.makeStackItem(this.pt);
/*      */       } else {
/* 2391 */         this.result = this.items.makeStackItem(paramJCLiteral.type);
/*      */       }
/*      */     }
/*      */     else {
/* 2395 */       this.result = this.items.makeImmediateItem(paramJCLiteral.type, paramJCLiteral.value);
/*      */     }
/*      */   }
/*      */ 
/* 2399 */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) { int i = this.code.nextreg;
/* 2400 */     genStats(paramLetExpr.defs, this.env);
/* 2401 */     this.result = genExpr(paramLetExpr.expr, paramLetExpr.expr.type).load();
/* 2402 */     this.code.endScopes(i); }
/*      */ 
/*      */   private void generateReferencesToPrunedTree(Symbol.ClassSymbol paramClassSymbol, Pool paramPool)
/*      */   {
/* 2406 */     List localList = (List)this.lower.prunedTree.get(paramClassSymbol);
/* 2407 */     if (localList != null)
/* 2408 */       for (JCTree localJCTree : localList)
/* 2409 */         localJCTree.accept(this.classReferenceVisitor);
/*      */   }
/*      */ 
/*      */   public boolean genClass(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*      */     try
/*      */     {
/* 2427 */       this.attrEnv = paramEnv;
/* 2428 */       Symbol.ClassSymbol localClassSymbol = paramJCClassDecl.sym;
/* 2429 */       this.toplevel = paramEnv.toplevel;
/* 2430 */       this.endPosTable = this.toplevel.endPositions;
/*      */ 
/* 2433 */       if ((this.generateIproxies) && 
/* 2434 */         ((localClassSymbol
/* 2434 */         .flags() & 0x600) == 1024L) && (!this.allowGenerics))
/*      */       {
/* 2437 */         implementInterfaceMethods(localClassSymbol);
/* 2438 */       }localClassSymbol.pool = this.pool;
/* 2439 */       this.pool.reset();
/*      */ 
/* 2443 */       paramJCClassDecl.defs = normalizeDefs(paramJCClassDecl.defs, localClassSymbol);
/* 2444 */       generateReferencesToPrunedTree(localClassSymbol, this.pool);
/* 2445 */       Env localEnv = new Env(paramJCClassDecl, new GenContext());
/*      */ 
/* 2447 */       localEnv.toplevel = paramEnv.toplevel;
/* 2448 */       localEnv.enclClass = paramJCClassDecl;
/*      */ 
/* 2450 */       for (List localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 2451 */         genDef((JCTree)localList.head, localEnv);
/*      */       }
/* 2453 */       if (this.pool.numEntries() > 65535) {
/* 2454 */         this.log.error(paramJCClassDecl.pos(), "limit.pool", new Object[0]);
/* 2455 */         this.nerrs += 1;
/*      */       }
/* 2457 */       if (this.nerrs != 0)
/*      */       {
/* 2459 */         for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 2460 */           if (((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF))
/* 2461 */             ((JCTree.JCMethodDecl)localList.head).sym.code = null;
/*      */         }
/*      */       }
/* 2464 */       paramJCClassDecl.defs = List.nil();
/* 2465 */       return this.nerrs == 0;
/*      */     }
/*      */     finally {
/* 2468 */       this.attrEnv = null;
/* 2469 */       this.env = null;
/* 2470 */       this.toplevel = null;
/* 2471 */       this.endPosTable = null;
/* 2472 */       this.nerrs = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ClassReferenceVisitor extends JCTree.Visitor
/*      */   {
/*      */     ClassReferenceVisitor()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitTree(JCTree paramJCTree)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitBinary(JCTree.JCBinary paramJCBinary)
/*      */     {
/*  888 */       paramJCBinary.lhs.accept(this);
/*  889 */       paramJCBinary.rhs.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/*  894 */       if (paramJCFieldAccess.selected.type.hasTag(TypeTag.CLASS))
/*  895 */         Gen.this.makeRef(paramJCFieldAccess.selected.pos(), paramJCFieldAccess.selected.type);
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/*  901 */       if ((paramJCIdent.sym.owner instanceof Symbol.ClassSymbol))
/*  902 */         Gen.this.pool.put(paramJCIdent.sym.owner);
/*      */     }
/*      */ 
/*      */     public void visitConditional(JCTree.JCConditional paramJCConditional)
/*      */     {
/*  908 */       paramJCConditional.cond.accept(this);
/*  909 */       paramJCConditional.truepart.accept(this);
/*  910 */       paramJCConditional.falsepart.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitUnary(JCTree.JCUnary paramJCUnary)
/*      */     {
/*  915 */       paramJCUnary.arg.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitParens(JCTree.JCParens paramJCParens)
/*      */     {
/*  920 */       paramJCParens.expr.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*      */     {
/*  925 */       paramJCTypeCast.expr.accept(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CodeSizeOverflow extends RuntimeException
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */   }
/*      */ 
/*      */   static class GenContext
/*      */   {
/* 2500 */     Code.Chain exit = null;
/*      */ 
/* 2505 */     Code.Chain cont = null;
/*      */ 
/* 2510 */     Gen.GenFinalizer finalize = null;
/*      */ 
/* 2515 */     boolean isSwitch = false;
/*      */ 
/* 2520 */     ListBuffer<Integer> gaps = null;
/*      */ 
/*      */     void addExit(Code.Chain paramChain)
/*      */     {
/* 2525 */       this.exit = Code.mergeChains(paramChain, this.exit);
/*      */     }
/*      */ 
/*      */     void addCont(Code.Chain paramChain)
/*      */     {
/* 2531 */       this.cont = Code.mergeChains(paramChain, this.cont);
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class GenFinalizer
/*      */   {
/*      */     GenFinalizer()
/*      */     {
/*      */     }
/*      */ 
/*      */     abstract void gen();
/*      */ 
/*      */     abstract void genLast();
/*      */ 
/*      */     boolean hasFinalizer()
/*      */     {
/* 2490 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Gen
 * JD-Core Version:    0.6.2
 */