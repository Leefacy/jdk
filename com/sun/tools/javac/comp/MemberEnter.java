/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler;
/*      */ import com.sun.tools.javac.code.Kinds;
/*      */ import com.sun.tools.javac.code.Kinds.KindName;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Scope.ErrorScope;
/*      */ import com.sun.tools.javac.code.Scope.ImportScope;
/*      */ import com.sun.tools.javac.code.Scope.StarImportScope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.Completer;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.Type.ErrorType;
/*      */ import com.sun.tools.javac.code.Type.ForAll;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.TypeAnnotations;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*      */ import com.sun.tools.javac.tree.JCTree.JCErroneous;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCImport;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.FatalError;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.JavaFileObject.Kind;
/*      */ 
/*      */ public class MemberEnter extends JCTree.Visitor
/*      */   implements Symbol.Completer
/*      */ {
/*   65 */   protected static final Context.Key<MemberEnter> memberEnterKey = new Context.Key();
/*      */   static final boolean checkClash = true;
/*      */   private final Names names;
/*      */   private final Enter enter;
/*      */   private final Log log;
/*      */   private final Check chk;
/*      */   private final Attr attr;
/*      */   private final Symtab syms;
/*      */   private final TreeMaker make;
/*      */   private final ClassReader reader;
/*      */   private final Todo todo;
/*      */   private final Annotate annotate;
/*      */   private final TypeAnnotations typeAnnotations;
/*      */   private final Types types;
/*      */   private final JCDiagnostic.Factory diags;
/*      */   private final Source source;
/*      */   private final Target target;
/*      */   private final DeferredLintHandler deferredLintHandler;
/*      */   private final Lint lint;
/*      */   private final TypeEnvs typeEnvs;
/*      */   boolean allowTypeAnnos;
/*      */   boolean allowRepeatedAnnos;
/*  131 */   ListBuffer<Env<AttrContext>> halfcompleted = new ListBuffer();
/*      */ 
/*  136 */   boolean isFirst = true;
/*      */ 
/*  142 */   boolean completionEnabled = true;
/*      */   protected Env<AttrContext> env;
/*      */ 
/*      */   public static MemberEnter instance(Context paramContext)
/*      */   {
/*   92 */     MemberEnter localMemberEnter = (MemberEnter)paramContext.get(memberEnterKey);
/*   93 */     if (localMemberEnter == null)
/*   94 */       localMemberEnter = new MemberEnter(paramContext);
/*   95 */     return localMemberEnter;
/*      */   }
/*      */ 
/*      */   protected MemberEnter(Context paramContext) {
/*   99 */     paramContext.put(memberEnterKey, this);
/*  100 */     this.names = Names.instance(paramContext);
/*  101 */     this.enter = Enter.instance(paramContext);
/*  102 */     this.log = Log.instance(paramContext);
/*  103 */     this.chk = Check.instance(paramContext);
/*  104 */     this.attr = Attr.instance(paramContext);
/*  105 */     this.syms = Symtab.instance(paramContext);
/*  106 */     this.make = TreeMaker.instance(paramContext);
/*  107 */     this.reader = ClassReader.instance(paramContext);
/*  108 */     this.todo = Todo.instance(paramContext);
/*  109 */     this.annotate = Annotate.instance(paramContext);
/*  110 */     this.typeAnnotations = TypeAnnotations.instance(paramContext);
/*  111 */     this.types = Types.instance(paramContext);
/*  112 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  113 */     this.source = Source.instance(paramContext);
/*  114 */     this.target = Target.instance(paramContext);
/*  115 */     this.deferredLintHandler = DeferredLintHandler.instance(paramContext);
/*  116 */     this.lint = Lint.instance(paramContext);
/*  117 */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*  118 */     this.allowTypeAnnos = this.source.allowTypeAnnotations();
/*  119 */     this.allowRepeatedAnnos = this.source.allowRepeatedAnnotations();
/*      */   }
/*      */ 
/*      */   private void importAll(int paramInt, Symbol.TypeSymbol paramTypeSymbol, Env<AttrContext> paramEnv)
/*      */   {
/*  156 */     if ((paramTypeSymbol.kind == 1) && (paramTypeSymbol.members().elems == null) && (!paramTypeSymbol.exists()))
/*      */     {
/*  158 */       if (((Symbol.PackageSymbol)paramTypeSymbol).fullname.equals(this.names.java_lang)) {
/*  159 */         JCDiagnostic localJCDiagnostic = this.diags.fragment("fatal.err.no.java.lang", new Object[0]);
/*  160 */         throw new FatalError(localJCDiagnostic);
/*      */       }
/*  162 */       this.log.error(JCDiagnostic.DiagnosticFlag.RESOLVE_ERROR, paramInt, "doesnt.exist", new Object[] { paramTypeSymbol });
/*      */     }
/*      */ 
/*  165 */     paramEnv.toplevel.starImportScope.importAll(paramTypeSymbol.members());
/*      */   }
/*      */ 
/*      */   private void importStaticAll(int paramInt, final Symbol.TypeSymbol paramTypeSymbol, Env<AttrContext> paramEnv)
/*      */   {
/*  176 */     final JavaFileObject localJavaFileObject = paramEnv.toplevel.sourcefile;
/*  177 */     final Scope.StarImportScope localStarImportScope = paramEnv.toplevel.starImportScope;
/*  178 */     final Symbol.PackageSymbol localPackageSymbol = paramEnv.toplevel.packge;
/*  179 */     final Symbol.TypeSymbol localTypeSymbol = paramTypeSymbol;
/*      */ 
/*  182 */     new Object() {
/*  183 */       Set<Symbol> processed = new HashSet();
/*      */ 
/*  185 */       void importFrom(Symbol.TypeSymbol paramAnonymousTypeSymbol) { if ((paramAnonymousTypeSymbol == null) || (!this.processed.add(paramAnonymousTypeSymbol))) {
/*  186 */           return;
/*      */         }
/*      */ 
/*  189 */         importFrom(MemberEnter.this.types.supertype(paramAnonymousTypeSymbol.type).tsym);
/*  190 */         for (Object localObject1 = MemberEnter.this.types.interfaces(paramAnonymousTypeSymbol.type).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  191 */           importFrom(((Type)localObject2).tsym);
/*      */         }
/*  193 */         localObject1 = paramAnonymousTypeSymbol.members();
/*  194 */         for (Object localObject2 = ((Scope)localObject1).elems; localObject2 != null; localObject2 = ((Scope.Entry)localObject2).sibling) {
/*  195 */           Symbol localSymbol = ((Scope.Entry)localObject2).sym;
/*  196 */           if ((localSymbol.kind == 2) && 
/*  197 */             ((localSymbol
/*  197 */             .flags() & 0x8) != 0L) && 
/*  198 */             (MemberEnter.this
/*  198 */             .staticImportAccessible(localSymbol, localPackageSymbol)) && 
/*  199 */             (localSymbol
/*  199 */             .isMemberOf(localTypeSymbol, MemberEnter.this.types)) && 
/*  200 */             (!localStarImportScope
/*  200 */             .includes(localSymbol)))
/*      */           {
/*  201 */             localStarImportScope.enter(localSymbol, (Scope)localObject1, localTypeSymbol.members(), true);
/*      */           }
/*      */         } } 
/*      */     }
/*  204 */     .importFrom(paramTypeSymbol);
/*      */ 
/*  207 */     this.annotate.earlier(new Annotate.Worker() {
/*  208 */       Set<Symbol> processed = new HashSet();
/*      */ 
/*      */       public String toString() {
/*  211 */         return "import static " + paramTypeSymbol + ".*" + " in " + localJavaFileObject;
/*      */       }
/*      */       void importFrom(Symbol.TypeSymbol paramAnonymousTypeSymbol) {
/*  214 */         if ((paramAnonymousTypeSymbol == null) || (!this.processed.add(paramAnonymousTypeSymbol))) {
/*  215 */           return;
/*      */         }
/*      */ 
/*  218 */         importFrom(MemberEnter.this.types.supertype(paramAnonymousTypeSymbol.type).tsym);
/*  219 */         for (Object localObject1 = MemberEnter.this.types.interfaces(paramAnonymousTypeSymbol.type).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  220 */           importFrom(((Type)localObject2).tsym);
/*      */         }
/*  222 */         localObject1 = paramAnonymousTypeSymbol.members();
/*  223 */         for (Object localObject2 = ((Scope)localObject1).elems; localObject2 != null; localObject2 = ((Scope.Entry)localObject2).sibling) {
/*  224 */           Symbol localSymbol = ((Scope.Entry)localObject2).sym;
/*  225 */           if ((localSymbol.isStatic()) && (localSymbol.kind != 2) && 
/*  226 */             (MemberEnter.this
/*  226 */             .staticImportAccessible(localSymbol, localPackageSymbol)) && 
/*  227 */             (!localStarImportScope
/*  227 */             .includes(localSymbol)) && 
/*  228 */             (localSymbol
/*  228 */             .isMemberOf(localTypeSymbol, MemberEnter.this.types)))
/*      */           {
/*  229 */             localStarImportScope.enter(localSymbol, (Scope)localObject1, localTypeSymbol.members(), true);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  234 */       public void run() { importFrom(paramTypeSymbol); }
/*      */ 
/*      */     });
/*      */   }
/*      */ 
/*      */   boolean staticImportAccessible(Symbol paramSymbol, Symbol.PackageSymbol paramPackageSymbol)
/*      */   {
/*  241 */     int i = (int)(paramSymbol.flags() & 0x7);
/*  242 */     switch (i) { case 1:
/*      */     case 3:
/*      */     default:
/*  245 */       return true;
/*      */     case 2:
/*  247 */       return false;
/*      */     case 0:
/*      */     case 4: }
/*  250 */     return paramSymbol.packge() == paramPackageSymbol;
/*      */   }
/*      */ 
/*      */   private void importNamedStatic(final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, final Symbol.TypeSymbol paramTypeSymbol, final Name paramName, final Env<AttrContext> paramEnv)
/*      */   {
/*  265 */     if (paramTypeSymbol.kind != 2) {
/*  266 */       this.log.error(JCDiagnostic.DiagnosticFlag.RECOVERABLE, paramDiagnosticPosition, "static.imp.only.classes.and.interfaces", new Object[0]);
/*  267 */       return;
/*      */     }
/*      */ 
/*  270 */     final Scope.ImportScope localImportScope = paramEnv.toplevel.namedImportScope;
/*  271 */     final Symbol.PackageSymbol localPackageSymbol = paramEnv.toplevel.packge;
/*  272 */     final Symbol.TypeSymbol localTypeSymbol = paramTypeSymbol;
/*      */ 
/*  275 */     new Object() {
/*  276 */       Set<Symbol> processed = new HashSet();
/*      */ 
/*  278 */       void importFrom(Symbol.TypeSymbol paramAnonymousTypeSymbol) { if ((paramAnonymousTypeSymbol == null) || (!this.processed.add(paramAnonymousTypeSymbol))) {
/*  279 */           return;
/*      */         }
/*      */ 
/*  282 */         importFrom(MemberEnter.this.types.supertype(paramAnonymousTypeSymbol.type).tsym);
/*  283 */         for (Object localObject1 = MemberEnter.this.types.interfaces(paramAnonymousTypeSymbol.type).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  284 */           importFrom(((Type)localObject2).tsym);
/*      */         }
/*      */         Object localObject2;
/*  286 */         for (localObject1 = paramAnonymousTypeSymbol.members().lookup(paramName); 
/*  287 */           ((Scope.Entry)localObject1).scope != null; 
/*  288 */           localObject1 = ((Scope.Entry)localObject1).next()) {
/*  289 */           localObject2 = ((Scope.Entry)localObject1).sym;
/*  290 */           if ((((Symbol)localObject2).isStatic()) && (((Symbol)localObject2).kind == 2))
/*      */           {
/*  292 */             if ((MemberEnter.this
/*  292 */               .staticImportAccessible((Symbol)localObject2, localPackageSymbol)) && 
/*  293 */               (((Symbol)localObject2)
/*  293 */               .isMemberOf(localTypeSymbol, MemberEnter.this.types)) && 
/*  294 */               (MemberEnter.this.chk
/*  294 */               .checkUniqueStaticImport(paramDiagnosticPosition, (Symbol)localObject2, localImportScope)))
/*  295 */               localImportScope.enter((Symbol)localObject2, ((Symbol)localObject2).owner.members(), localTypeSymbol.members(), true); 
/*      */           }
/*      */         } } 
/*      */     }
/*  298 */     .importFrom(paramTypeSymbol);
/*      */ 
/*  301 */     this.annotate.earlier(new Annotate.Worker() {
/*  302 */       Set<Symbol> processed = new HashSet();
/*  303 */       boolean found = false;
/*      */ 
/*      */       public String toString() {
/*  306 */         return "import static " + paramTypeSymbol + "." + paramName;
/*      */       }
/*      */       void importFrom(Symbol.TypeSymbol paramAnonymousTypeSymbol) {
/*  309 */         if ((paramAnonymousTypeSymbol == null) || (!this.processed.add(paramAnonymousTypeSymbol))) {
/*  310 */           return;
/*      */         }
/*      */ 
/*  313 */         importFrom(MemberEnter.this.types.supertype(paramAnonymousTypeSymbol.type).tsym);
/*  314 */         for (Object localObject1 = MemberEnter.this.types.interfaces(paramAnonymousTypeSymbol.type).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  315 */           importFrom(((Type)localObject2).tsym);
/*      */         }
/*      */         Object localObject2;
/*  317 */         for (localObject1 = paramAnonymousTypeSymbol.members().lookup(paramName); 
/*  318 */           ((Scope.Entry)localObject1).scope != null; 
/*  319 */           localObject1 = ((Scope.Entry)localObject1).next()) {
/*  320 */           localObject2 = ((Scope.Entry)localObject1).sym;
/*  321 */           if ((((Symbol)localObject2).isStatic()) && 
/*  322 */             (MemberEnter.this
/*  322 */             .staticImportAccessible((Symbol)localObject2, localPackageSymbol)) && 
/*  323 */             (((Symbol)localObject2)
/*  323 */             .isMemberOf(localTypeSymbol, MemberEnter.this.types)))
/*      */           {
/*  324 */             this.found = true;
/*  325 */             if (((Symbol)localObject2).kind != 2)
/*  326 */               localImportScope.enter((Symbol)localObject2, ((Symbol)localObject2).owner.members(), localTypeSymbol.members(), true);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public void run() {
/*  332 */         JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try {
/*  334 */           importFrom(paramTypeSymbol);
/*  335 */           if (!this.found) {
/*  336 */             MemberEnter.this.log.error(paramDiagnosticPosition, "cant.resolve.location", new Object[] { Kinds.KindName.STATIC, paramName, 
/*  338 */               List.nil(), List.nil(), 
/*  339 */               Kinds.typeKindName(paramTypeSymbol.type), 
/*  339 */               paramTypeSymbol.type });
/*      */           }
/*      */ 
/*  343 */           MemberEnter.this.log.useSource(localJavaFileObject); } finally { MemberEnter.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void importNamed(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Env<AttrContext> paramEnv)
/*      */   {
/*  355 */     if ((paramSymbol.kind == 2) && 
/*  356 */       (this.chk
/*  356 */       .checkUniqueImport(paramDiagnosticPosition, paramSymbol, paramEnv.toplevel.namedImportScope)))
/*      */     {
/*  357 */       paramEnv.toplevel.namedImportScope.enter(paramSymbol, paramSymbol.owner.members());
/*      */     }
/*      */   }
/*      */ 
/*      */   Type signature(Symbol.MethodSymbol paramMethodSymbol, List<JCTree.JCTypeParameter> paramList, List<JCTree.JCVariableDecl> paramList1, JCTree paramJCTree, JCTree.JCVariableDecl paramJCVariableDecl, List<JCTree.JCExpression> paramList2, Env<AttrContext> paramEnv)
/*      */   {
/*  379 */     List localList = this.enter.classEnter(paramList, paramEnv);
/*  380 */     this.attr.attribTypeVariables(paramList, paramEnv);
/*      */ 
/*  383 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  384 */     for (Object localObject1 = paramList1; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/*  385 */       memberEnter((JCTree)((List)localObject1).head, paramEnv);
/*  386 */       localListBuffer1.append(((JCTree.JCVariableDecl)((List)localObject1).head).vartype.type);
/*      */     }
/*      */ 
/*  390 */     localObject1 = paramJCTree == null ? this.syms.voidType : this.attr.attribType(paramJCTree, paramEnv);
/*      */     Type localType1;
/*  394 */     if (paramJCVariableDecl != null) {
/*  395 */       memberEnter(paramJCVariableDecl, paramEnv);
/*  396 */       localType1 = paramJCVariableDecl.vartype.type;
/*      */     } else {
/*  398 */       localType1 = null;
/*      */     }
/*      */ 
/*  402 */     ListBuffer localListBuffer2 = new ListBuffer();
/*  403 */     for (Object localObject2 = paramList2; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/*  404 */       Type localType2 = this.attr.attribType((JCTree)((List)localObject2).head, paramEnv);
/*  405 */       if (!localType2.hasTag(TypeTag.TYPEVAR))
/*  406 */         localType2 = this.chk.checkClassType(((JCTree.JCExpression)((List)localObject2).head).pos(), localType2);
/*  407 */       else if (localType2.tsym.owner == paramMethodSymbol)
/*      */       {
/*  409 */         localType2.tsym.flags_field |= 140737488355328L;
/*      */       }
/*  411 */       localListBuffer2.append(localType2);
/*      */     }
/*      */ 
/*  415 */     localObject2 = new Type.MethodType(localListBuffer1.toList(), (Type)localObject1, localListBuffer2
/*  415 */       .toList(), this.syms.methodClass);
/*      */ 
/*  417 */     ((Type.MethodType)localObject2).recvtype = localType1;
/*      */ 
/*  419 */     return localList.isEmpty() ? localObject2 : new Type.ForAll(localList, (Type)localObject2);
/*      */   }
/*      */ 
/*      */   protected void memberEnter(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  434 */     Env localEnv = this.env;
/*      */     try {
/*  436 */       this.env = paramEnv;
/*  437 */       paramJCTree.accept(this);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  439 */       this.chk.completionError(paramJCTree.pos(), localCompletionFailure);
/*      */     } finally {
/*  441 */       this.env = localEnv;
/*      */     }
/*      */   }
/*      */ 
/*      */   void memberEnter(List<? extends JCTree> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  448 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  449 */       memberEnter((JCTree)((List)localObject).head, paramEnv);
/*      */   }
/*      */ 
/*      */   void finishClass(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv)
/*      */   {
/*  455 */     if (((paramJCClassDecl.mods.flags & 0x4000) != 0L) && 
/*  456 */       ((this.types
/*  456 */       .supertype(paramJCClassDecl.sym.type).tsym
/*  456 */       .flags() & 0x4000) == 0L)) {
/*  457 */       addEnumMembers(paramJCClassDecl, paramEnv);
/*      */     }
/*  459 */     memberEnter(paramJCClassDecl.defs, paramEnv);
/*      */   }
/*      */ 
/*      */   private void addEnumMembers(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv)
/*      */   {
/*  466 */     JCTree.JCExpression localJCExpression = this.make.Type(new Type.ArrayType(paramJCClassDecl.sym.type, this.syms.arrayClass));
/*      */ 
/*  470 */     JCTree.JCMethodDecl localJCMethodDecl1 = this.make
/*  470 */       .MethodDef(this.make
/*  470 */       .Modifiers(9L), 
/*  470 */       this.names.values, localJCExpression, 
/*  473 */       List.nil(), 
/*  474 */       List.nil(), 
/*  475 */       List.nil(), null, null);
/*      */ 
/*  478 */     memberEnter(localJCMethodDecl1, paramEnv);
/*      */ 
/*  482 */     JCTree.JCMethodDecl localJCMethodDecl2 = this.make
/*  482 */       .MethodDef(this.make
/*  482 */       .Modifiers(9L), 
/*  482 */       this.names.valueOf, this.make
/*  484 */       .Type(paramJCClassDecl.sym.type), 
/*  485 */       List.nil(), 
/*  486 */       List.of(this.make
/*  486 */       .VarDef(this.make
/*  486 */       .Modifiers(8589967360L), 
/*  486 */       this.names
/*  488 */       .fromString("name"), 
/*  488 */       this.make
/*  489 */       .Type(this.syms.stringType), 
/*  489 */       null)), 
/*  490 */       List.nil(), null, null);
/*      */ 
/*  493 */     memberEnter(localJCMethodDecl2, paramEnv);
/*      */   }
/*      */ 
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/*  497 */     if (paramJCCompilationUnit.starImportScope.elems != null)
/*      */     {
/*  499 */       return;
/*      */     }
/*      */ 
/*  504 */     if (paramJCCompilationUnit.pid != null) {
/*  505 */       localObject1 = paramJCCompilationUnit.packge;
/*  506 */       while (((Symbol)localObject1).owner != this.syms.rootPackage) {
/*  507 */         ((Symbol)localObject1).owner.complete();
/*  508 */         if (this.syms.classes.get(((Symbol)localObject1).getQualifiedName()) != null) {
/*  509 */           this.log.error(paramJCCompilationUnit.pos, "pkg.clashes.with.class.of.same.name", new Object[] { localObject1 });
/*      */         }
/*      */ 
/*  513 */         localObject1 = ((Symbol)localObject1).owner;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  518 */     annotateLater(paramJCCompilationUnit.packageAnnotations, this.env, paramJCCompilationUnit.packge, null);
/*      */ 
/*  520 */     Object localObject1 = this.deferredLintHandler.immediate();
/*  521 */     Lint localLint = this.chk.setLint(this.lint);
/*      */     try
/*      */     {
/*  525 */       importAll(paramJCCompilationUnit.pos, this.reader.enterPackage(this.names.java_lang), this.env);
/*      */ 
/*  528 */       memberEnter(paramJCCompilationUnit.defs, this.env);
/*      */     } finally {
/*  530 */       this.chk.setLint(localLint);
/*  531 */       this.deferredLintHandler.setPos((JCDiagnostic.DiagnosticPosition)localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitImport(JCTree.JCImport paramJCImport)
/*      */   {
/*  537 */     JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCImport.qualid;
/*  538 */     Name localName = TreeInfo.name(localJCFieldAccess);
/*      */ 
/*  542 */     Env localEnv = this.env.dup(paramJCImport);
/*      */ 
/*  544 */     Symbol.TypeSymbol localTypeSymbol1 = this.attr.attribImportQualifier(paramJCImport, localEnv).tsym;
/*  545 */     if (localName == this.names.asterisk)
/*      */     {
/*  547 */       this.chk.checkCanonical(localJCFieldAccess.selected);
/*  548 */       if (paramJCImport.staticImport)
/*  549 */         importStaticAll(paramJCImport.pos, localTypeSymbol1, this.env);
/*      */       else {
/*  551 */         importAll(paramJCImport.pos, localTypeSymbol1, this.env);
/*      */       }
/*      */     }
/*  554 */     else if (paramJCImport.staticImport) {
/*  555 */       importNamedStatic(paramJCImport.pos(), localTypeSymbol1, localName, localEnv);
/*  556 */       this.chk.checkCanonical(localJCFieldAccess.selected);
/*      */     } else {
/*  558 */       Symbol.TypeSymbol localTypeSymbol2 = attribImportType(localJCFieldAccess, localEnv).tsym;
/*  559 */       this.chk.checkCanonical(localJCFieldAccess);
/*  560 */       importNamed(paramJCImport.pos(), localTypeSymbol2, this.env);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  566 */     Scope localScope = this.enter.enterScope(this.env);
/*  567 */     Symbol.MethodSymbol localMethodSymbol = new Symbol.MethodSymbol(0L, paramJCMethodDecl.name, null, localScope.owner);
/*  568 */     localMethodSymbol.flags_field = this.chk.checkFlags(paramJCMethodDecl.pos(), paramJCMethodDecl.mods.flags, localMethodSymbol, paramJCMethodDecl);
/*  569 */     paramJCMethodDecl.sym = localMethodSymbol;
/*      */ 
/*  572 */     if ((paramJCMethodDecl.mods.flags & 0x0) != 0L) {
/*  573 */       localMethodSymbol.enclClass().flags_field |= 8796093022208L;
/*      */     }
/*      */ 
/*  576 */     Env localEnv = methodEnv(paramJCMethodDecl, this.env);
/*      */ 
/*  578 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferredLintHandler.setPos(paramJCMethodDecl.pos());
/*      */     try
/*      */     {
/*  581 */       localMethodSymbol.type = signature(localMethodSymbol, paramJCMethodDecl.typarams, paramJCMethodDecl.params, paramJCMethodDecl.restype, paramJCMethodDecl.recvparam, paramJCMethodDecl.thrown, localEnv);
/*      */     }
/*      */     finally
/*      */     {
/*  586 */       this.deferredLintHandler.setPos(localDiagnosticPosition);
/*      */     }
/*      */ 
/*  589 */     if (this.types.isSignaturePolymorphic(localMethodSymbol)) {
/*  590 */       localMethodSymbol.flags_field |= 70368744177664L;
/*      */     }
/*      */ 
/*  594 */     ListBuffer localListBuffer = new ListBuffer();
/*  595 */     JCTree.JCVariableDecl localJCVariableDecl1 = null;
/*  596 */     for (List localList = paramJCMethodDecl.params; localList.nonEmpty(); localList = localList.tail) {
/*  597 */       JCTree.JCVariableDecl localJCVariableDecl2 = localJCVariableDecl1 = (JCTree.JCVariableDecl)localList.head;
/*  598 */       localListBuffer.append(Assert.checkNonNull(localJCVariableDecl2.sym));
/*      */     }
/*  600 */     localMethodSymbol.params = localListBuffer.toList();
/*      */ 
/*  603 */     if ((localJCVariableDecl1 != null) && ((localJCVariableDecl1.mods.flags & 0x0) != 0L)) {
/*  604 */       localMethodSymbol.flags_field |= 17179869184L;
/*      */     }
/*  606 */     ((AttrContext)localEnv.info).scope.leave();
/*  607 */     if (this.chk.checkUnique(paramJCMethodDecl.pos(), localMethodSymbol, localScope)) {
/*  608 */       localScope.enter(localMethodSymbol);
/*      */     }
/*      */ 
/*  611 */     annotateLater(paramJCMethodDecl.mods.annotations, localEnv, localMethodSymbol, paramJCMethodDecl.pos());
/*      */ 
/*  614 */     typeAnnotate(paramJCMethodDecl, localEnv, localMethodSymbol, paramJCMethodDecl.pos());
/*      */ 
/*  616 */     if (paramJCMethodDecl.defaultValue != null)
/*  617 */       annotateDefaultValueLater(paramJCMethodDecl.defaultValue, localEnv, localMethodSymbol);
/*      */   }
/*      */ 
/*      */   Env<AttrContext> methodEnv(JCTree.JCMethodDecl paramJCMethodDecl, Env<AttrContext> paramEnv)
/*      */   {
/*  626 */     Env localEnv = paramEnv
/*  626 */       .dup(paramJCMethodDecl, ((AttrContext)paramEnv.info)
/*  626 */       .dup(((AttrContext)paramEnv.info).scope
/*  626 */       .dupUnshared()));
/*  627 */     localEnv.enclMethod = paramJCMethodDecl;
/*  628 */     ((AttrContext)localEnv.info).scope.owner = paramJCMethodDecl.sym;
/*  629 */     if (paramJCMethodDecl.sym.type != null)
/*      */     {
/*      */       Attr tmp76_73 = this.attr; tmp76_73.getClass(); ((AttrContext)localEnv.info).returnResult = new Attr.ResultInfo(tmp76_73, 12, paramJCMethodDecl.sym.type.getReturnType());
/*      */     }
/*  633 */     if ((paramJCMethodDecl.mods.flags & 0x8) != 0L) ((AttrContext)localEnv.info).staticLevel += 1;
/*  634 */     return localEnv;
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  638 */     Env localEnv1 = this.env;
/*  639 */     if (((paramJCVariableDecl.mods.flags & 0x8) != 0L) || 
/*  640 */       ((((AttrContext)this.env.info).scope.owner
/*  640 */       .flags() & 0x200) != 0L)) {
/*  641 */       localEnv1 = this.env.dup(paramJCVariableDecl, ((AttrContext)this.env.info).dup());
/*  642 */       ((AttrContext)localEnv1.info).staticLevel += 1;
/*      */     }
/*  644 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferredLintHandler.setPos(paramJCVariableDecl.pos());
/*      */     try {
/*  646 */       if (TreeInfo.isEnumInit(paramJCVariableDecl)) {
/*  647 */         this.attr.attribIdentAsEnumType(localEnv1, (JCTree.JCIdent)paramJCVariableDecl.vartype);
/*      */       } else {
/*  649 */         this.attr.attribType(paramJCVariableDecl.vartype, localEnv1);
/*  650 */         if (TreeInfo.isReceiverParam(paramJCVariableDecl))
/*  651 */           checkReceiver(paramJCVariableDecl, localEnv1);
/*      */       }
/*      */     } finally {
/*  654 */       this.deferredLintHandler.setPos(localDiagnosticPosition);
/*      */     }
/*      */ 
/*  657 */     if ((paramJCVariableDecl.mods.flags & 0x0) != 0L)
/*      */     {
/*  664 */       localObject2 = (Type.ArrayType)paramJCVariableDecl.vartype.type.unannotatedType();
/*  665 */       paramJCVariableDecl.vartype.type = ((Type.ArrayType)localObject2).makeVarargs();
/*      */     }
/*  667 */     Object localObject2 = this.enter.enterScope(this.env);
/*  668 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(0L, paramJCVariableDecl.name, paramJCVariableDecl.vartype.type, ((Scope)localObject2).owner);
/*      */ 
/*  670 */     localVarSymbol.flags_field = this.chk.checkFlags(paramJCVariableDecl.pos(), paramJCVariableDecl.mods.flags, localVarSymbol, paramJCVariableDecl);
/*  671 */     paramJCVariableDecl.sym = localVarSymbol;
/*  672 */     if (paramJCVariableDecl.init != null) {
/*  673 */       localVarSymbol.flags_field |= 262144L;
/*  674 */       if (((localVarSymbol.flags_field & 0x10) != 0L) && 
/*  675 */         (needsLazyConstValue(paramJCVariableDecl.init)))
/*      */       {
/*  676 */         Env localEnv2 = getInitEnv(paramJCVariableDecl, this.env);
/*  677 */         ((AttrContext)localEnv2.info).enclVar = localVarSymbol;
/*  678 */         localVarSymbol.setLazyConstValue(initEnv(paramJCVariableDecl, localEnv2), this.attr, paramJCVariableDecl);
/*      */       }
/*      */     }
/*  681 */     if (this.chk.checkUnique(paramJCVariableDecl.pos(), localVarSymbol, (Scope)localObject2)) {
/*  682 */       this.chk.checkTransparentVar(paramJCVariableDecl.pos(), localVarSymbol, (Scope)localObject2);
/*  683 */       ((Scope)localObject2).enter(localVarSymbol);
/*      */     }
/*  685 */     annotateLater(paramJCVariableDecl.mods.annotations, localEnv1, localVarSymbol, paramJCVariableDecl.pos());
/*  686 */     typeAnnotate(paramJCVariableDecl.vartype, this.env, localVarSymbol, paramJCVariableDecl.pos());
/*  687 */     localVarSymbol.pos = paramJCVariableDecl.pos;
/*      */   }
/*      */ 
/*      */   void checkType(JCTree paramJCTree, Type paramType, String paramString) {
/*  691 */     if ((!paramJCTree.type.isErroneous()) && (!this.types.isSameType(paramJCTree.type, paramType)))
/*  692 */       this.log.error(paramJCTree, paramString, new Object[] { paramType, paramJCTree.type });
/*      */   }
/*      */ 
/*      */   void checkReceiver(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv) {
/*  696 */     this.attr.attribExpr(paramJCVariableDecl.nameexpr, paramEnv);
/*  697 */     Symbol.MethodSymbol localMethodSymbol = paramEnv.enclMethod.sym;
/*  698 */     if (localMethodSymbol.isConstructor()) {
/*  699 */       Type localType = localMethodSymbol.owner.owner.type;
/*  700 */       if (localType.hasTag(TypeTag.METHOD))
/*      */       {
/*  702 */         localType = localMethodSymbol.owner.owner.owner.type;
/*      */       }
/*  704 */       if (localType.hasTag(TypeTag.CLASS)) {
/*  705 */         checkType(paramJCVariableDecl.vartype, localType, "incorrect.constructor.receiver.type");
/*  706 */         checkType(paramJCVariableDecl.nameexpr, localType, "incorrect.constructor.receiver.name");
/*      */       } else {
/*  708 */         this.log.error(paramJCVariableDecl, "receiver.parameter.not.applicable.constructor.toplevel.class", new Object[0]);
/*      */       }
/*      */     } else {
/*  711 */       checkType(paramJCVariableDecl.vartype, localMethodSymbol.owner.type, "incorrect.receiver.type");
/*  712 */       checkType(paramJCVariableDecl.nameexpr, localMethodSymbol.owner.type, "incorrect.receiver.name");
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean needsLazyConstValue(JCTree paramJCTree) {
/*  717 */     InitTreeVisitor localInitTreeVisitor = new InitTreeVisitor();
/*  718 */     paramJCTree.accept(localInitTreeVisitor);
/*  719 */     return localInitTreeVisitor.result;
/*      */   }
/*      */ 
/*      */   Env<AttrContext> initEnv(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv)
/*      */   {
/*  788 */     Env localEnv = paramEnv.dupto(new AttrContextEnv(paramJCVariableDecl, ((AttrContext)paramEnv.info).dup()));
/*  789 */     if (paramJCVariableDecl.sym.owner.kind == 2) {
/*  790 */       ((AttrContext)localEnv.info).scope = ((AttrContext)paramEnv.info).scope.dupUnshared();
/*  791 */       ((AttrContext)localEnv.info).scope.owner = paramJCVariableDecl.sym;
/*      */     }
/*  793 */     if (((paramJCVariableDecl.mods.flags & 0x8) != 0L) || (
/*  794 */       ((paramEnv.enclClass.sym
/*  794 */       .flags() & 0x200) != 0L) && (paramEnv.enclMethod == null)))
/*  795 */       ((AttrContext)localEnv.info).staticLevel += 1;
/*  796 */     return localEnv;
/*      */   }
/*      */ 
/*      */   public void visitTree(JCTree paramJCTree)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous)
/*      */   {
/*  805 */     if (paramJCErroneous.errs != null)
/*  806 */       memberEnter(paramJCErroneous.errs, this.env);
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> getMethodEnv(JCTree.JCMethodDecl paramJCMethodDecl, Env<AttrContext> paramEnv) {
/*  810 */     Env localEnv = methodEnv(paramJCMethodDecl, paramEnv);
/*  811 */     ((AttrContext)localEnv.info).lint = ((AttrContext)localEnv.info).lint.augment(paramJCMethodDecl.sym);
/*  812 */     for (List localList = paramJCMethodDecl.typarams; localList.nonEmpty(); localList = localList.tail)
/*  813 */       ((AttrContext)localEnv.info).scope.enterIfAbsent(((JCTree.JCTypeParameter)localList.head).type.tsym);
/*  814 */     for (localList = paramJCMethodDecl.params; localList.nonEmpty(); localList = localList.tail)
/*  815 */       ((AttrContext)localEnv.info).scope.enterIfAbsent(((JCTree.JCVariableDecl)localList.head).sym);
/*  816 */     return localEnv;
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> getInitEnv(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv) {
/*  820 */     Env localEnv = initEnv(paramJCVariableDecl, paramEnv);
/*  821 */     return localEnv;
/*      */   }
/*      */ 
/*      */   Type attribImportType(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  829 */     Assert.check(this.completionEnabled);
/*      */     try
/*      */     {
/*  833 */       this.completionEnabled = false;
/*  834 */       return this.attr.attribType(paramJCTree, paramEnv);
/*      */     } finally {
/*  836 */       this.completionEnabled = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   void annotateLater(final List<JCTree.JCAnnotation> paramList, final Env<AttrContext> paramEnv, final Symbol paramSymbol, final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/*  849 */     if (paramList.isEmpty()) {
/*  850 */       return;
/*      */     }
/*  852 */     if (paramSymbol.kind != 1) {
/*  853 */       paramSymbol.resetAnnotations();
/*      */     }
/*  855 */     this.annotate.normal(new Annotate.Worker()
/*      */     {
/*      */       public String toString() {
/*  858 */         return "annotate " + paramList + " onto " + paramSymbol + " in " + paramSymbol.owner;
/*      */       }
/*      */ 
/*      */       public void run()
/*      */       {
/*  863 */         Assert.check((paramSymbol.kind == 1) || (paramSymbol.annotationsPendingCompletion()));
/*  864 */         JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */ 
/*  868 */         JCDiagnostic.DiagnosticPosition localDiagnosticPosition = paramDiagnosticPosition != null ? MemberEnter.this.deferredLintHandler
/*  867 */           .setPos(paramDiagnosticPosition) : MemberEnter.this.deferredLintHandler
/*  868 */           .immediate();
/*  869 */         Lint localLint = paramDiagnosticPosition != null ? null : MemberEnter.this.chk.setLint(MemberEnter.this.lint);
/*      */         try {
/*  871 */           if ((paramSymbol.hasAnnotations()) && 
/*  872 */             (paramList
/*  872 */             .nonEmpty()))
/*  873 */             MemberEnter.this.log.error(((JCTree.JCAnnotation)paramList.head).pos, "already.annotated", new Object[] { 
/*  875 */               Kinds.kindName(paramSymbol), 
/*  875 */               paramSymbol });
/*  876 */           MemberEnter.this.actualEnterAnnotations(paramList, paramEnv, paramSymbol);
/*      */         } finally {
/*  878 */           if (localLint != null)
/*  879 */             MemberEnter.this.chk.setLint(localLint);
/*  880 */           MemberEnter.this.deferredLintHandler.setPos(localDiagnosticPosition);
/*  881 */           MemberEnter.this.log.useSource(localJavaFileObject);
/*      */         }
/*      */       }
/*      */     });
/*  886 */     this.annotate.validate(new Annotate.Worker()
/*      */     {
/*      */       public void run() {
/*  889 */         JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try {
/*  891 */           MemberEnter.this.chk.validateAnnotations(paramList, paramSymbol);
/*      */ 
/*  893 */           MemberEnter.this.log.useSource(localJavaFileObject); } finally { MemberEnter.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private boolean hasDeprecatedAnnotation(List<JCTree.JCAnnotation> paramList)
/*      */   {
/*  904 */     for (Object localObject = paramList; !((List)localObject).isEmpty(); localObject = ((List)localObject).tail) {
/*  905 */       JCTree.JCAnnotation localJCAnnotation = (JCTree.JCAnnotation)((List)localObject).head;
/*  906 */       if ((localJCAnnotation.annotationType.type == this.syms.deprecatedType) && (localJCAnnotation.args.isEmpty()))
/*  907 */         return true;
/*      */     }
/*  909 */     return false;
/*      */   }
/*      */ 
/*      */   private void actualEnterAnnotations(List<JCTree.JCAnnotation> paramList, Env<AttrContext> paramEnv, Symbol paramSymbol)
/*      */   {
/*  916 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*      */ 
/*  918 */     HashMap localHashMap = new HashMap();
/*      */ 
/*  921 */     for (Object localObject = paramList; !((List)localObject).isEmpty(); localObject = ((List)localObject).tail) {
/*  922 */       JCTree.JCAnnotation localJCAnnotation = (JCTree.JCAnnotation)((List)localObject).head;
/*  923 */       Attribute.Compound localCompound = this.annotate.enterAnnotation(localJCAnnotation, this.syms.annotationType, paramEnv);
/*      */ 
/*  926 */       if (localCompound != null)
/*      */       {
/*  930 */         if (localLinkedHashMap.containsKey(localJCAnnotation.type.tsym)) {
/*  931 */           if (!this.allowRepeatedAnnos) {
/*  932 */             this.log.error(localJCAnnotation.pos(), "repeatable.annotations.not.supported.in.source", new Object[0]);
/*  933 */             this.allowRepeatedAnnos = true;
/*      */           }
/*  935 */           ListBuffer localListBuffer = (ListBuffer)localLinkedHashMap.get(localJCAnnotation.type.tsym);
/*  936 */           localListBuffer = localListBuffer.append(localCompound);
/*  937 */           localLinkedHashMap.put(localJCAnnotation.type.tsym, localListBuffer);
/*  938 */           localHashMap.put(localCompound, localJCAnnotation.pos());
/*      */         } else {
/*  940 */           localLinkedHashMap.put(localJCAnnotation.type.tsym, ListBuffer.of(localCompound));
/*  941 */           localHashMap.put(localCompound, localJCAnnotation.pos());
/*      */         }
/*      */ 
/*  945 */         if ((!localCompound.type.isErroneous()) && (paramSymbol.owner.kind != 16))
/*      */         {
/*  947 */           if (this.types
/*  947 */             .isSameType(localCompound.type, this.syms.deprecatedType))
/*      */           {
/*  948 */             paramSymbol.flags_field |= 131072L;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     Annotate tmp292_289 = this.annotate; tmp292_289.getClass(); paramSymbol.setDeclarationAttributesWithCompletion(new Annotate.AnnotateRepeatedContext(tmp292_289, paramEnv, localLinkedHashMap, localHashMap, this.log, false));
/*      */   }
/*      */ 
/*      */   void annotateDefaultValueLater(final JCTree.JCExpression paramJCExpression, final Env<AttrContext> paramEnv, final Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/*  960 */     this.annotate.normal(new Annotate.Worker()
/*      */     {
/*      */       public String toString() {
/*  963 */         return "annotate " + paramMethodSymbol.owner + "." + paramMethodSymbol + " default " + paramJCExpression;
/*      */       }
/*      */ 
/*      */       public void run()
/*      */       {
/*  969 */         JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try {
/*  971 */           MemberEnter.this.enterDefaultValue(paramJCExpression, paramEnv, paramMethodSymbol);
/*      */ 
/*  973 */           MemberEnter.this.log.useSource(localJavaFileObject); } finally { MemberEnter.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */       }
/*      */     });
/*  977 */     this.annotate.validate(new Annotate.Worker()
/*      */     {
/*      */       public void run() {
/*  980 */         JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */         try
/*      */         {
/*  984 */           MemberEnter.this.chk.validateAnnotationTree(paramJCExpression);
/*      */ 
/*  986 */           MemberEnter.this.log.useSource(localJavaFileObject); } finally { MemberEnter.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void enterDefaultValue(JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/*  996 */     paramMethodSymbol.defaultValue = this.annotate.enterAttributeValue(paramMethodSymbol.type.getReturnType(), paramJCExpression, paramEnv);
/*      */   }
/*      */ 
/*      */   public void complete(Symbol paramSymbol)
/*      */     throws Symbol.CompletionFailure
/*      */   {
/* 1010 */     if (!this.completionEnabled)
/*      */     {
/* 1012 */       Assert.check((paramSymbol.flags() & 0x1000000) == 0L);
/* 1013 */       paramSymbol.completer = this;
/* 1014 */       return;
/*      */     }
/*      */ 
/* 1017 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramSymbol;
/* 1018 */     Type.ClassType localClassType = (Type.ClassType)localClassSymbol.type;
/* 1019 */     Env localEnv1 = this.typeEnvs.get(localClassSymbol);
/* 1020 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localEnv1.tree;
/* 1021 */     boolean bool1 = this.isFirst;
/* 1022 */     this.isFirst = false;
/*      */     try {
/* 1024 */       this.annotate.enterStart();
/*      */ 
/* 1026 */       JavaFileObject localJavaFileObject = this.log.useSource(localEnv1.toplevel.sourcefile);
/* 1027 */       JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferredLintHandler.setPos(localJCClassDecl.pos());
/*      */       try
/*      */       {
/* 1030 */         this.halfcompleted.append(localEnv1);
/*      */ 
/* 1033 */         localClassSymbol.flags_field |= 268435456L;
/*      */ 
/* 1037 */         if (localClassSymbol.owner.kind == 1) {
/* 1038 */           memberEnter(localEnv1.toplevel, localEnv1.enclosing(JCTree.Tag.TOPLEVEL));
/* 1039 */           this.todo.append(localEnv1);
/*      */         }
/*      */ 
/* 1042 */         if (localClassSymbol.owner.kind == 2) {
/* 1043 */           localClassSymbol.owner.complete();
/*      */         }
/*      */ 
/* 1046 */         Env localEnv2 = baseEnv(localJCClassDecl, localEnv1);
/*      */ 
/* 1048 */         if (localJCClassDecl.extending != null)
/* 1049 */           typeAnnotate(localJCClassDecl.extending, localEnv2, paramSymbol, localJCClassDecl.pos());
/* 1050 */         for (Object localObject1 = localJCClassDecl.implementing.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCExpression)((Iterator)localObject1).next();
/* 1051 */           typeAnnotate((JCTree)localObject2, localEnv2, paramSymbol, localJCClassDecl.pos()); }
/* 1052 */         this.annotate.flush();
/*      */ 
/* 1059 */         localObject1 = localClassSymbol.fullname == this.names.java_lang_Object ? Type.noType : (localJCClassDecl.mods.flags & 0x4000) != 0L ? this.attr
/* 1059 */           .attribBase(enumBase(localJCClassDecl.pos, localClassSymbol), 
/* 1059 */           localEnv2, true, false, false) : localJCClassDecl.extending != null ? this.attr
/* 1057 */           .attribBase(localJCClassDecl.extending, localEnv2, true, false, true) : 
/* 1059 */           this.syms.objectType;
/*      */ 
/* 1064 */         localClassType.supertype_field = modelMissingTypes((Type)localObject1, localJCClassDecl.extending, false);
/*      */ 
/* 1067 */         Object localObject2 = new ListBuffer();
/* 1068 */         ListBuffer localListBuffer = null;
/* 1069 */         HashSet localHashSet = new HashSet();
/* 1070 */         List localList = localJCClassDecl.implementing;
/* 1071 */         for (Object localObject3 = localList.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree.JCExpression)((Iterator)localObject3).next();
/* 1072 */           localObject5 = this.attr.attribBase((JCTree)localObject4, localEnv2, false, true, true);
/* 1073 */           if (((Type)localObject5).hasTag(TypeTag.CLASS)) {
/* 1074 */             ((ListBuffer)localObject2).append(localObject5);
/* 1075 */             if (localListBuffer != null) localListBuffer.append(localObject5);
/* 1076 */             this.chk.checkNotRepeated(((JCTree.JCExpression)localObject4).pos(), this.types.erasure((Type)localObject5), localHashSet);
/*      */           } else {
/* 1078 */             if (localListBuffer == null)
/* 1079 */               localListBuffer = new ListBuffer().appendList((ListBuffer)localObject2);
/* 1080 */             localListBuffer.append(modelMissingTypes((Type)localObject5, (JCTree.JCExpression)localObject4, true));
/*      */           }
/*      */         }
/*      */         Object localObject4;
/*      */         Object localObject5;
/* 1083 */         if ((localClassSymbol.flags_field & 0x2000) != 0L) {
/* 1084 */           localClassType.interfaces_field = List.of(this.syms.annotationType);
/* 1085 */           localClassType.all_interfaces_field = localClassType.interfaces_field;
/*      */         } else {
/* 1087 */           localClassType.interfaces_field = ((ListBuffer)localObject2).toList();
/* 1088 */           localClassType.all_interfaces_field = (localListBuffer == null ? localClassType.interfaces_field : localListBuffer
/* 1089 */             .toList());
/*      */         }
/*      */ 
/* 1092 */         if (localClassSymbol.fullname == this.names.java_lang_Object) {
/* 1093 */           if (localJCClassDecl.extending != null) {
/* 1094 */             this.chk.checkNonCyclic(localJCClassDecl.extending.pos(), (Type)localObject1);
/*      */ 
/* 1096 */             localClassType.supertype_field = Type.noType;
/*      */           }
/* 1098 */           else if (localJCClassDecl.implementing.nonEmpty()) {
/* 1099 */             this.chk.checkNonCyclic(((JCTree.JCExpression)localJCClassDecl.implementing.head).pos(), (Type)localClassType.interfaces_field.head);
/*      */ 
/* 1101 */             localClassType.interfaces_field = List.nil();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1109 */         this.attr.attribAnnotationTypes(localJCClassDecl.mods.annotations, localEnv2);
/* 1110 */         if (hasDeprecatedAnnotation(localJCClassDecl.mods.annotations))
/* 1111 */           localClassSymbol.flags_field |= 131072L;
/* 1112 */         annotateLater(localJCClassDecl.mods.annotations, localEnv2, localClassSymbol, localJCClassDecl.pos());
/*      */ 
/* 1115 */         this.chk.checkNonCyclicDecl(localJCClassDecl);
/*      */ 
/* 1117 */         this.attr.attribTypeVariables(localJCClassDecl.typarams, localEnv2);
/*      */ 
/* 1119 */         for (localObject3 = localJCClassDecl.typarams.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree.JCTypeParameter)((Iterator)localObject3).next();
/* 1120 */           typeAnnotate((JCTree)localObject4, localEnv2, paramSymbol, localJCClassDecl.pos());
/*      */         }
/*      */ 
/* 1123 */         if (((localClassSymbol.flags() & 0x200) == 0L) && 
/* 1124 */           (!TreeInfo.hasConstructors(localJCClassDecl.defs)))
/*      */         {
/* 1125 */           localObject3 = List.nil();
/* 1126 */           localObject4 = List.nil();
/* 1127 */           localObject5 = List.nil();
/* 1128 */           long l = 0L;
/* 1129 */           boolean bool2 = false;
/* 1130 */           int i = 1;
/* 1131 */           JCTree.JCNewClass localJCNewClass = null;
/*      */           Type localType;
/* 1132 */           if (localClassSymbol.name.isEmpty()) {
/* 1133 */             localJCNewClass = (JCTree.JCNewClass)localEnv1.next.tree;
/* 1134 */             if (localJCNewClass.constructor != null) {
/* 1135 */               i = localJCNewClass.constructor.kind != 63 ? 1 : 0;
/* 1136 */               localType = this.types.memberType(localClassSymbol.type, localJCNewClass.constructor);
/*      */ 
/* 1138 */               localObject3 = localType.getParameterTypes();
/* 1139 */               localObject4 = localType.getTypeArguments();
/* 1140 */               l = localJCNewClass.constructor.flags() & 0x0;
/* 1141 */               if (localJCNewClass.encl != null) {
/* 1142 */                 localObject3 = ((List)localObject3).prepend(localJCNewClass.encl.type);
/* 1143 */                 bool2 = true;
/*      */               }
/* 1145 */               localObject5 = localType.getThrownTypes();
/*      */             }
/*      */           }
/* 1148 */           if (i != 0) {
/* 1149 */             localType = localJCNewClass != null ? (Symbol.MethodSymbol)localJCNewClass.constructor : null;
/*      */ 
/* 1151 */             JCTree localJCTree = DefaultConstructor(this.make.at(localJCClassDecl.pos), localClassSymbol, localType, (List)localObject4, (List)localObject3, (List)localObject5, l, bool2);
/*      */ 
/* 1155 */             localJCClassDecl.defs = localJCClassDecl.defs.prepend(localJCTree);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1160 */         localObject3 = new Symbol.VarSymbol(262160L, this.names._this, localClassSymbol.type, localClassSymbol);
/*      */ 
/* 1162 */         ((Symbol.VarSymbol)localObject3).pos = 0;
/* 1163 */         ((AttrContext)localEnv1.info).scope.enter((Symbol)localObject3);
/*      */ 
/* 1165 */         if (((localClassSymbol.flags_field & 0x200) == 0L) && 
/* 1166 */           (localClassType.supertype_field
/* 1166 */           .hasTag(TypeTag.CLASS)))
/*      */         {
/* 1167 */           localObject4 = new Symbol.VarSymbol(262160L, this.names._super, localClassType.supertype_field, localClassSymbol);
/*      */ 
/* 1170 */           ((Symbol.VarSymbol)localObject4).pos = 0;
/* 1171 */           ((AttrContext)localEnv1.info).scope.enter((Symbol)localObject4);
/*      */         }
/*      */ 
/* 1177 */         if ((localClassSymbol.owner.kind == 1) && (localClassSymbol.owner != this.syms.unnamedPackage))
/*      */         {
/* 1179 */           if (this.reader
/* 1179 */             .packageExists(localClassSymbol.fullname))
/*      */           {
/* 1180 */             this.log.error(localJCClassDecl.pos, "clash.with.pkg.of.same.name", new Object[] { Kinds.kindName(paramSymbol), localClassSymbol });
/*      */           }
/*      */         }
/* 1182 */         if ((localClassSymbol.owner.kind == 1) && ((localClassSymbol.flags_field & 1L) == 0L) && 
/* 1183 */           (!localEnv1.toplevel.sourcefile
/* 1183 */           .isNameCompatible(localClassSymbol.name
/* 1183 */           .toString(), JavaFileObject.Kind.SOURCE)))
/* 1184 */           localClassSymbol.flags_field |= 17592186044416L;
/*      */       }
/*      */       catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1187 */         this.chk.completionError(localJCClassDecl.pos(), localCompletionFailure);
/*      */       } finally {
/* 1189 */         this.deferredLintHandler.setPos(localDiagnosticPosition);
/* 1190 */         this.log.useSource(localJavaFileObject);
/*      */       }
/*      */ 
/* 1195 */       if (bool1)
/*      */         try {
/* 1197 */           while (this.halfcompleted.nonEmpty()) {
/* 1198 */             Env localEnv3 = (Env)this.halfcompleted.next();
/* 1199 */             finish(localEnv3);
/* 1200 */             if (this.allowTypeAnnos) {
/* 1201 */               this.typeAnnotations.organizeTypeAnnotationsSignatures(localEnv3, (JCTree.JCClassDecl)localEnv3.tree);
/* 1202 */               this.typeAnnotations.validateTypeAnnotationsSignatures(localEnv3, (JCTree.JCClassDecl)localEnv3.tree);
/*      */             }
/*      */           }
/*      */         } finally {
/* 1206 */           this.isFirst = true;
/*      */         }
/*      */     }
/*      */     finally {
/* 1210 */       this.annotate.enterDone();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void actualEnterTypeAnnotations(List<JCTree.JCAnnotation> paramList, Env<AttrContext> paramEnv, Symbol paramSymbol)
/*      */   {
/* 1220 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*      */ 
/* 1222 */     HashMap localHashMap = new HashMap();
/*      */ 
/* 1225 */     for (Object localObject = paramList; !((List)localObject).isEmpty(); localObject = ((List)localObject).tail) {
/* 1226 */       JCTree.JCAnnotation localJCAnnotation = (JCTree.JCAnnotation)((List)localObject).head;
/* 1227 */       Attribute.TypeCompound localTypeCompound = this.annotate.enterTypeAnnotation(localJCAnnotation, this.syms.annotationType, paramEnv);
/*      */ 
/* 1230 */       if (localTypeCompound != null)
/*      */       {
/* 1234 */         if (localLinkedHashMap.containsKey(localJCAnnotation.type.tsym)) {
/* 1235 */           if (this.source.allowRepeatedAnnotations()) {
/* 1236 */             ListBuffer localListBuffer = (ListBuffer)localLinkedHashMap.get(localJCAnnotation.type.tsym);
/* 1237 */             localListBuffer = localListBuffer.append(localTypeCompound);
/* 1238 */             localLinkedHashMap.put(localJCAnnotation.type.tsym, localListBuffer);
/* 1239 */             localHashMap.put(localTypeCompound, localJCAnnotation.pos());
/*      */           } else {
/* 1241 */             this.log.error(localJCAnnotation.pos(), "repeatable.annotations.not.supported.in.source", new Object[0]);
/*      */           }
/*      */         } else {
/* 1244 */           localLinkedHashMap.put(localJCAnnotation.type.tsym, ListBuffer.of(localTypeCompound));
/* 1245 */           localHashMap.put(localTypeCompound, localJCAnnotation.pos());
/*      */         }
/*      */       }
/*      */     }
/* 1249 */     if (paramSymbol != null)
/*      */     {
/*      */       Annotate tmp240_237 = this.annotate; tmp240_237.getClass(); paramSymbol.appendTypeAttributesWithCompletion(new Annotate.AnnotateRepeatedContext(tmp240_237, paramEnv, localLinkedHashMap, localHashMap, this.log, true));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void typeAnnotate(JCTree paramJCTree, Env<AttrContext> paramEnv, Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 1256 */     if (this.allowTypeAnnos)
/* 1257 */       paramJCTree.accept(new TypeAnnotate(paramEnv, paramSymbol, paramDiagnosticPosition));
/*      */   }
/*      */ 
/*      */   private Env<AttrContext> baseEnv(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv)
/*      */   {
/* 1376 */     Scope localScope = new Scope(paramJCClassDecl.sym);
/*      */ 
/* 1378 */     for (Object localObject = ((AttrContext)paramEnv.outer.info).scope.elems; localObject != null; localObject = ((Scope.Entry)localObject).sibling) {
/* 1379 */       if (((Scope.Entry)localObject).sym.isLocal()) {
/* 1380 */         localScope.enter(((Scope.Entry)localObject).sym);
/*      */       }
/*      */     }
/*      */ 
/* 1384 */     if (paramJCClassDecl.typarams != null)
/* 1385 */       for (localObject = paramJCClassDecl.typarams; 
/* 1386 */         ((List)localObject).nonEmpty(); 
/* 1387 */         localObject = ((List)localObject).tail)
/* 1388 */         localScope.enter(((JCTree.JCTypeParameter)((List)localObject).head).type.tsym);
/* 1389 */     localObject = paramEnv.outer;
/* 1390 */     Env localEnv = ((Env)localObject).dup(paramJCClassDecl, ((AttrContext)((Env)localObject).info).dup(localScope));
/* 1391 */     localEnv.baseClause = true;
/* 1392 */     localEnv.outer = ((Env)localObject);
/* 1393 */     ((AttrContext)localEnv.info).isSelfCall = false;
/* 1394 */     return localEnv;
/*      */   }
/*      */ 
/*      */   private void finish(Env<AttrContext> paramEnv)
/*      */   {
/* 1401 */     JavaFileObject localJavaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */     try {
/* 1403 */       JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)paramEnv.tree;
/* 1404 */       finishClass(localJCClassDecl, paramEnv);
/*      */     } finally {
/* 1406 */       this.log.useSource(localJavaFileObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression enumBase(int paramInt, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1416 */     JCTree.JCTypeApply localJCTypeApply = this.make.at(paramInt)
/* 1416 */       .TypeApply(this.make
/* 1416 */       .QualIdent(this.syms.enumSym), 
/* 1417 */       List.of(this.make
/* 1417 */       .Type(paramClassSymbol.type)));
/*      */ 
/* 1418 */     return localJCTypeApply;
/*      */   }
/*      */ 
/*      */   Type modelMissingTypes(Type paramType, final JCTree.JCExpression paramJCExpression, final boolean paramBoolean) {
/* 1422 */     if (!paramType.hasTag(TypeTag.ERROR)) {
/* 1423 */       return paramType;
/*      */     }
/* 1425 */     return new Type.ErrorType(paramType.getOriginalType(), paramType.tsym)
/*      */     {
/*      */       private Type modelType;
/*      */ 
/*      */       public Type getModelType() {
/* 1430 */         if (this.modelType == null)
/* 1431 */           this.modelType = new MemberEnter.Synthesizer(MemberEnter.this, getOriginalType(), paramBoolean).visit(paramJCExpression);
/* 1432 */         return this.modelType;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   JCTree DefaultConstructor(TreeMaker paramTreeMaker, Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList1, List<Type> paramList2, List<Type> paramList3, long paramLong, boolean paramBoolean)
/*      */   {
/* 1572 */     if (((paramClassSymbol.flags() & 0x4000) != 0L) && 
/* 1573 */       (this.types
/* 1573 */       .supertype(paramClassSymbol.type).tsym == 
/* 1573 */       this.syms.enumSym))
/*      */     {
/* 1575 */       paramLong = paramLong & 0xFFFFFFF8 | 0x2 | 0x0;
/*      */     }
/* 1577 */     else paramLong |= paramClassSymbol.flags() & 0x7 | 0x0;
/* 1578 */     if (paramClassSymbol.name.isEmpty()) {
/* 1579 */       paramLong |= 536870912L;
/*      */     }
/* 1581 */     Type.MethodType localMethodType1 = new Type.MethodType(paramList2, null, paramList3, paramClassSymbol);
/* 1582 */     Type.MethodType localMethodType2 = paramList1.nonEmpty() ? new Type.ForAll(paramList1, localMethodType1) : localMethodType1;
/*      */ 
/* 1585 */     Symbol.MethodSymbol localMethodSymbol = new Symbol.MethodSymbol(paramLong, this.names.init, localMethodType2, paramClassSymbol);
/*      */ 
/* 1587 */     localMethodSymbol.params = createDefaultConstructorParams(paramTreeMaker, paramMethodSymbol, localMethodSymbol, paramList2, paramBoolean);
/*      */ 
/* 1589 */     List localList1 = paramTreeMaker.Params(paramList2, localMethodSymbol);
/* 1590 */     List localList2 = List.nil();
/* 1591 */     if (paramClassSymbol.type != this.syms.objectType) {
/* 1592 */       localList2 = localList2.prepend(SuperCall(paramTreeMaker, paramList1, localList1, paramBoolean));
/*      */     }
/* 1594 */     JCTree.JCMethodDecl localJCMethodDecl = paramTreeMaker.MethodDef(localMethodSymbol, paramTreeMaker.Block(0L, localList2));
/* 1595 */     return localJCMethodDecl;
/*      */   }
/*      */ 
/*      */   private List<Symbol.VarSymbol> createDefaultConstructorParams(TreeMaker paramTreeMaker, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, List<Type> paramList, boolean paramBoolean)
/*      */   {
/* 1604 */     List localList = null;
/* 1605 */     Object localObject1 = paramList;
/*      */     Object localObject2;
/* 1606 */     if (paramBoolean)
/*      */     {
/* 1617 */       localList = List.nil();
/* 1618 */       localObject2 = new Symbol.VarSymbol(8589934592L, paramTreeMaker.paramName(0), (Type)paramList.head, paramMethodSymbol2);
/* 1619 */       localList = localList.append(localObject2);
/* 1620 */       localObject1 = ((List)localObject1).tail;
/*      */     }
/* 1622 */     if ((paramMethodSymbol1 != null) && (paramMethodSymbol1.params != null) && 
/* 1623 */       (paramMethodSymbol1.params
/* 1623 */       .nonEmpty()) && (((List)localObject1).nonEmpty())) {
/* 1624 */       localList = localList == null ? List.nil() : localList;
/* 1625 */       localObject2 = paramMethodSymbol1.params;
/* 1626 */       while ((((List)localObject2).nonEmpty()) && (((List)localObject1).nonEmpty())) {
/* 1627 */         Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(((Symbol.VarSymbol)((List)localObject2).head).flags() | 0x0, ((Symbol.VarSymbol)((List)localObject2).head).name, (Type)((List)localObject1).head, paramMethodSymbol2);
/*      */ 
/* 1629 */         localList = localList.append(localVarSymbol);
/* 1630 */         localObject2 = ((List)localObject2).tail;
/* 1631 */         localObject1 = ((List)localObject1).tail;
/*      */       }
/*      */     }
/* 1634 */     return localList;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpressionStatement SuperCall(TreeMaker paramTreeMaker, List<Type> paramList, List<JCTree.JCVariableDecl> paramList1, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/* 1657 */     if (paramBoolean) {
/* 1658 */       localObject = paramTreeMaker.Select(paramTreeMaker.Ident((JCTree.JCVariableDecl)paramList1.head), this.names._super);
/* 1659 */       paramList1 = paramList1.tail;
/*      */     } else {
/* 1661 */       localObject = paramTreeMaker.Ident(this.names._super);
/*      */     }
/* 1663 */     List localList = paramList.nonEmpty() ? paramTreeMaker.Types(paramList) : null;
/* 1664 */     return paramTreeMaker.Exec(paramTreeMaker.Apply(localList, (JCTree.JCExpression)localObject, paramTreeMaker.Idents(paramList1)));
/*      */   }
/*      */ 
/*      */   static class InitTreeVisitor extends JCTree.Visitor
/*      */   {
/*  726 */     private boolean result = true;
/*      */ 
/*      */     public void visitTree(JCTree paramJCTree)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*  733 */       this.result = false;
/*      */     }
/*      */ 
/*      */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray)
/*      */     {
/*  738 */       this.result = false;
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/*  743 */       this.result = false;
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/*  748 */       this.result = false;
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */     {
/*  753 */       this.result = false;
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/*  758 */       paramJCFieldAccess.selected.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitConditional(JCTree.JCConditional paramJCConditional)
/*      */     {
/*  763 */       paramJCConditional.cond.accept(this);
/*  764 */       paramJCConditional.truepart.accept(this);
/*  765 */       paramJCConditional.falsepart.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitParens(JCTree.JCParens paramJCParens)
/*      */     {
/*  770 */       paramJCParens.expr.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*      */     {
/*  775 */       paramJCTypeCast.expr.accept(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Synthesizer extends JCTree.Visitor
/*      */   {
/*      */     Type originalType;
/*      */     boolean interfaceExpected;
/* 1440 */     List<Symbol.ClassSymbol> synthesizedSymbols = List.nil();
/*      */     Type result;
/*      */ 
/*      */     Synthesizer(Type paramBoolean, boolean arg3)
/*      */     {
/* 1444 */       this.originalType = paramBoolean;
/*      */       boolean bool;
/* 1445 */       this.interfaceExpected = bool;
/*      */     }
/*      */ 
/*      */     Type visit(JCTree paramJCTree) {
/* 1449 */       paramJCTree.accept(this);
/* 1450 */       return this.result;
/*      */     }
/*      */ 
/*      */     List<Type> visit(List<? extends JCTree> paramList) {
/* 1454 */       ListBuffer localListBuffer = new ListBuffer();
/* 1455 */       for (JCTree localJCTree : paramList)
/* 1456 */         localListBuffer.append(visit(localJCTree));
/* 1457 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     public void visitTree(JCTree paramJCTree)
/*      */     {
/* 1462 */       this.result = MemberEnter.this.syms.errType;
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/* 1467 */       if (!paramJCIdent.type.hasTag(TypeTag.ERROR))
/* 1468 */         this.result = paramJCIdent.type;
/*      */       else
/* 1470 */         this.result = synthesizeClass(paramJCIdent.name, MemberEnter.this.syms.unnamedPackage).type;
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 1476 */       if (!paramJCFieldAccess.type.hasTag(TypeTag.ERROR)) {
/* 1477 */         this.result = paramJCFieldAccess.type;
/*      */       } else { boolean bool = this.interfaceExpected;
/*      */         Type localType;
/*      */         try {
/* 1482 */           this.interfaceExpected = false;
/* 1483 */           localType = visit(paramJCFieldAccess.selected);
/*      */         } finally {
/* 1485 */           this.interfaceExpected = bool;
/*      */         }
/* 1487 */         Symbol.ClassSymbol localClassSymbol = synthesizeClass(paramJCFieldAccess.name, localType.tsym);
/* 1488 */         this.result = localClassSymbol.type;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */     {
/* 1494 */       if (!paramJCTypeApply.type.hasTag(TypeTag.ERROR)) {
/* 1495 */         this.result = paramJCTypeApply.type;
/*      */       } else {
/* 1497 */         Type.ClassType localClassType = (Type.ClassType)visit(paramJCTypeApply.clazz);
/* 1498 */         if (this.synthesizedSymbols.contains(localClassType.tsym))
/* 1499 */           synthesizeTyparams((Symbol.ClassSymbol)localClassType.tsym, paramJCTypeApply.arguments.size());
/* 1500 */         final List localList = visit(paramJCTypeApply.arguments);
/* 1501 */         this.result = new Type.ErrorType(paramJCTypeApply.type, localClassType.tsym)
/*      */         {
/*      */           public List<Type> getTypeArguments() {
/* 1504 */             return localList;
/*      */           }
/*      */         };
/*      */       }
/*      */     }
/*      */ 
/*      */     Symbol.ClassSymbol synthesizeClass(Name paramName, Symbol paramSymbol) {
/* 1511 */       int i = this.interfaceExpected ? 512 : 0;
/* 1512 */       Symbol.ClassSymbol localClassSymbol = new Symbol.ClassSymbol(i, paramName, paramSymbol);
/* 1513 */       localClassSymbol.members_field = new Scope.ErrorScope(localClassSymbol);
/* 1514 */       localClassSymbol.type = new Type.ErrorType(this.originalType, localClassSymbol)
/*      */       {
/*      */         public List<Type> getTypeArguments() {
/* 1517 */           return this.typarams_field;
/*      */         }
/*      */       };
/* 1520 */       this.synthesizedSymbols = this.synthesizedSymbols.prepend(localClassSymbol);
/* 1521 */       return localClassSymbol;
/*      */     }
/*      */ 
/*      */     void synthesizeTyparams(Symbol.ClassSymbol paramClassSymbol, int paramInt) {
/* 1525 */       Type.ClassType localClassType = (Type.ClassType)paramClassSymbol.type;
/* 1526 */       Assert.check(localClassType.typarams_field.isEmpty());
/* 1527 */       if (paramInt == 1) {
/* 1528 */         Type.TypeVar localTypeVar1 = new Type.TypeVar(MemberEnter.this.names.fromString("T"), paramClassSymbol, MemberEnter.this.syms.botType);
/* 1529 */         localClassType.typarams_field = localClassType.typarams_field.prepend(localTypeVar1);
/*      */       } else {
/* 1531 */         for (int i = paramInt; i > 0; i--) {
/* 1532 */           Type.TypeVar localTypeVar2 = new Type.TypeVar(MemberEnter.this.names.fromString("T" + i), paramClassSymbol, MemberEnter.this.syms.botType);
/* 1533 */           localClassType.typarams_field = localClassType.typarams_field.prepend(localTypeVar2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TypeAnnotate extends TreeScanner
/*      */   {
/*      */     private Env<AttrContext> env;
/*      */     private Symbol sym;
/*      */     private JCDiagnostic.DiagnosticPosition deferPos;
/*      */ 
/*      */     public TypeAnnotate(Symbol paramDiagnosticPosition, JCDiagnostic.DiagnosticPosition arg3)
/*      */     {
/* 1271 */       this.env = paramDiagnosticPosition;
/*      */       Object localObject1;
/* 1272 */       this.sym = localObject1;
/*      */       Object localObject2;
/* 1273 */       this.deferPos = localObject2;
/*      */     }
/*      */ 
/*      */     void annotateTypeLater(final List<JCTree.JCAnnotation> paramList) {
/* 1277 */       if (paramList.isEmpty()) {
/* 1278 */         return;
/*      */       }
/*      */ 
/* 1281 */       final JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferPos;
/*      */ 
/* 1283 */       MemberEnter.this.annotate.normal(new Annotate.Worker()
/*      */       {
/*      */         public String toString() {
/* 1286 */           return "type annotate " + paramList + " onto " + MemberEnter.TypeAnnotate.this.sym + " in " + MemberEnter.TypeAnnotate.this.sym.owner;
/*      */         }
/*      */ 
/*      */         public void run() {
/* 1290 */           JavaFileObject localJavaFileObject = MemberEnter.this.log.useSource(MemberEnter.TypeAnnotate.this.env.toplevel.sourcefile);
/* 1291 */           JCDiagnostic.DiagnosticPosition localDiagnosticPosition = null;
/*      */ 
/* 1293 */           if (localDiagnosticPosition != null)
/* 1294 */             localDiagnosticPosition = MemberEnter.this.deferredLintHandler.setPos(localDiagnosticPosition);
/*      */           try
/*      */           {
/* 1297 */             MemberEnter.this.actualEnterTypeAnnotations(paramList, MemberEnter.TypeAnnotate.this.env, MemberEnter.TypeAnnotate.this.sym);
/*      */ 
/* 1299 */             if (localDiagnosticPosition != null)
/* 1300 */               MemberEnter.this.deferredLintHandler.setPos(localDiagnosticPosition);
/* 1301 */             MemberEnter.this.log.useSource(localJavaFileObject);
/*      */           }
/*      */           finally
/*      */           {
/* 1299 */             if (localDiagnosticPosition != null)
/* 1300 */               MemberEnter.this.deferredLintHandler.setPos(localDiagnosticPosition);
/* 1301 */             MemberEnter.this.log.useSource(localJavaFileObject);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType)
/*      */     {
/* 1309 */       annotateTypeLater(paramJCAnnotatedType.annotations);
/* 1310 */       super.visitAnnotatedType(paramJCAnnotatedType);
/*      */     }
/*      */ 
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter)
/*      */     {
/* 1315 */       annotateTypeLater(paramJCTypeParameter.annotations);
/* 1316 */       super.visitTypeParameter(paramJCTypeParameter);
/*      */     }
/*      */ 
/*      */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray)
/*      */     {
/* 1321 */       annotateTypeLater(paramJCNewArray.annotations);
/* 1322 */       for (List localList : paramJCNewArray.dimAnnotations)
/* 1323 */         annotateTypeLater(localList);
/* 1324 */       super.visitNewArray(paramJCNewArray);
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 1329 */       scan(paramJCMethodDecl.mods);
/* 1330 */       scan(paramJCMethodDecl.restype);
/* 1331 */       scan(paramJCMethodDecl.typarams);
/* 1332 */       scan(paramJCMethodDecl.recvparam);
/* 1333 */       scan(paramJCMethodDecl.params);
/* 1334 */       scan(paramJCMethodDecl.thrown);
/* 1335 */       scan(paramJCMethodDecl.defaultValue);
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 1342 */       JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferPos;
/* 1343 */       this.deferPos = paramJCVariableDecl.pos();
/*      */       try {
/* 1345 */         if ((this.sym != null) && (this.sym.kind == 4))
/*      */         {
/* 1348 */           scan(paramJCVariableDecl.mods);
/* 1349 */           scan(paramJCVariableDecl.vartype);
/*      */         }
/* 1351 */         scan(paramJCVariableDecl.init);
/*      */ 
/* 1353 */         this.deferPos = localDiagnosticPosition; } finally { this.deferPos = localDiagnosticPosition; }
/*      */ 
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 1366 */       if (paramJCNewClass.def == null)
/*      */       {
/* 1369 */         super.visitNewClass(paramJCNewClass);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.MemberEnter
 * JD-Core Version:    0.6.2
 */