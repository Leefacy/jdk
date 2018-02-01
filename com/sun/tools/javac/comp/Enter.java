/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.ImportScope;
/*     */ import com.sun.tools.javac.code.Scope.StarImportScope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Type.TypeVar;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.jvm.ClassReader;
/*     */ import com.sun.tools.javac.main.Option.PkgInfo;
/*     */ import com.sun.tools.javac.tree.DocCommentTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class Enter extends JCTree.Visitor
/*     */ {
/*  93 */   protected static final Context.Key<Enter> enterKey = new Context.Key();
/*     */   Log log;
/*     */   Symtab syms;
/*     */   Check chk;
/*     */   TreeMaker make;
/*     */   ClassReader reader;
/*     */   Annotate annotate;
/*     */   MemberEnter memberEnter;
/*     */   Types types;
/*     */   Lint lint;
/*     */   Names names;
/*     */   JavaFileManager fileManager;
/*     */   Option.PkgInfo pkginfoOpt;
/*     */   TypeEnvs typeEnvs;
/*     */   private final Todo todo;
/*     */   ListBuffer<Symbol.ClassSymbol> uncompleted;
/*     */   private JCTree.JCClassDecl predefClassDef;
/*     */   protected Env<AttrContext> env;
/*     */   Type result;
/*     */ 
/*     */   public static Enter instance(Context paramContext)
/*     */   {
/* 113 */     Enter localEnter = (Enter)paramContext.get(enterKey);
/* 114 */     if (localEnter == null)
/* 115 */       localEnter = new Enter(paramContext);
/* 116 */     return localEnter;
/*     */   }
/*     */ 
/*     */   protected Enter(Context paramContext) {
/* 120 */     paramContext.put(enterKey, this);
/*     */ 
/* 122 */     this.log = Log.instance(paramContext);
/* 123 */     this.reader = ClassReader.instance(paramContext);
/* 124 */     this.make = TreeMaker.instance(paramContext);
/* 125 */     this.syms = Symtab.instance(paramContext);
/* 126 */     this.chk = Check.instance(paramContext);
/* 127 */     this.memberEnter = MemberEnter.instance(paramContext);
/* 128 */     this.types = Types.instance(paramContext);
/* 129 */     this.annotate = Annotate.instance(paramContext);
/* 130 */     this.lint = Lint.instance(paramContext);
/* 131 */     this.names = Names.instance(paramContext);
/*     */ 
/* 133 */     this.predefClassDef = this.make.ClassDef(this.make
/* 134 */       .Modifiers(1L), 
/* 134 */       this.syms.predefClass.name, 
/* 136 */       List.nil(), null, 
/* 138 */       List.nil(), 
/* 139 */       List.nil());
/* 140 */     this.predefClassDef.sym = this.syms.predefClass;
/* 141 */     this.todo = Todo.instance(paramContext);
/* 142 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*     */ 
/* 144 */     Options localOptions = Options.instance(paramContext);
/* 145 */     this.pkginfoOpt = Option.PkgInfo.get(localOptions);
/* 146 */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> getEnv(Symbol.TypeSymbol paramTypeSymbol)
/*     */   {
/* 152 */     return this.typeEnvs.get(paramTypeSymbol);
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> getClassEnv(Symbol.TypeSymbol paramTypeSymbol) {
/* 156 */     Env localEnv1 = getEnv(paramTypeSymbol);
/* 157 */     Env localEnv2 = localEnv1;
/* 158 */     while (((AttrContext)localEnv2.info).lint == null)
/* 159 */       localEnv2 = localEnv2.next;
/* 160 */     ((AttrContext)localEnv1.info).lint = ((AttrContext)localEnv2.info).lint.augment(paramTypeSymbol);
/* 161 */     return localEnv1;
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> classEnv(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv)
/*     */   {
/* 193 */     Env localEnv = paramEnv
/* 193 */       .dup(paramJCClassDecl, ((AttrContext)paramEnv.info)
/* 193 */       .dup(new Scope(paramJCClassDecl.sym)));
/*     */ 
/* 194 */     localEnv.enclClass = paramJCClassDecl;
/* 195 */     localEnv.outer = paramEnv;
/* 196 */     ((AttrContext)localEnv.info).isSelfCall = false;
/* 197 */     ((AttrContext)localEnv.info).lint = null;
/*     */ 
/* 199 */     return localEnv;
/*     */   }
/*     */ 
/*     */   Env<AttrContext> topLevelEnv(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*     */   {
/* 206 */     Env localEnv = new Env(paramJCCompilationUnit, new AttrContext());
/* 207 */     localEnv.toplevel = paramJCCompilationUnit;
/* 208 */     localEnv.enclClass = this.predefClassDef;
/* 209 */     paramJCCompilationUnit.namedImportScope = new Scope.ImportScope(paramJCCompilationUnit.packge);
/* 210 */     paramJCCompilationUnit.starImportScope = new Scope.StarImportScope(paramJCCompilationUnit.packge);
/* 211 */     ((AttrContext)localEnv.info).scope = paramJCCompilationUnit.namedImportScope;
/* 212 */     ((AttrContext)localEnv.info).lint = this.lint;
/* 213 */     return localEnv;
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> getTopLevelEnv(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 217 */     Env localEnv = new Env(paramJCCompilationUnit, new AttrContext());
/* 218 */     localEnv.toplevel = paramJCCompilationUnit;
/* 219 */     localEnv.enclClass = this.predefClassDef;
/* 220 */     ((AttrContext)localEnv.info).scope = paramJCCompilationUnit.namedImportScope;
/* 221 */     ((AttrContext)localEnv.info).lint = this.lint;
/* 222 */     return localEnv;
/*     */   }
/*     */ 
/*     */   Scope enterScope(Env<AttrContext> paramEnv)
/*     */   {
/* 231 */     return paramEnv.tree.hasTag(JCTree.Tag.CLASSDEF) ? ((JCTree.JCClassDecl)paramEnv.tree).sym.members_field : ((AttrContext)paramEnv.info).scope;
/*     */   }
/*     */ 
/*     */   Type classEnter(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*     */   {
/* 255 */     Env localEnv = this.env;
/*     */     try {
/* 257 */       this.env = paramEnv;
/* 258 */       paramJCTree.accept(this);
/* 259 */       return this.result;
/*     */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 261 */       return this.chk.completionError(paramJCTree.pos(), localCompletionFailure);
/*     */     } finally {
/* 263 */       this.env = localEnv;
/*     */     }
/*     */   }
/*     */ 
/*     */   <T extends JCTree> List<Type> classEnter(List<T> paramList, Env<AttrContext> paramEnv)
/*     */   {
/* 270 */     ListBuffer localListBuffer = new ListBuffer();
/* 271 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 272 */       Type localType = classEnter((JCTree)((List)localObject).head, paramEnv);
/* 273 */       if (localType != null)
/* 274 */         localListBuffer.append(localType);
/*     */     }
/* 276 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*     */   {
/* 281 */     JavaFileObject localJavaFileObject = this.log.useSource(paramJCCompilationUnit.sourcefile);
/* 282 */     int i = 0;
/* 283 */     boolean bool = paramJCCompilationUnit.sourcefile.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE);
/*     */ 
/* 285 */     if (paramJCCompilationUnit.pid != null) {
/* 286 */       paramJCCompilationUnit.packge = this.reader.enterPackage(TreeInfo.fullName(paramJCCompilationUnit.pid));
/* 287 */       if ((paramJCCompilationUnit.packageAnnotations.nonEmpty()) || (this.pkginfoOpt == Option.PkgInfo.ALWAYS) || (paramJCCompilationUnit.docComments != null))
/*     */       {
/* 290 */         if (bool)
/* 291 */           i = 1;
/* 292 */         else if (paramJCCompilationUnit.packageAnnotations.nonEmpty())
/* 293 */           this.log.error(((JCTree.JCAnnotation)paramJCCompilationUnit.packageAnnotations.head).pos(), "pkg.annotations.sb.in.package-info.java", new Object[0]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 298 */       paramJCCompilationUnit.packge = this.syms.unnamedPackage;
/*     */     }
/* 300 */     paramJCCompilationUnit.packge.complete();
/* 301 */     Env localEnv1 = topLevelEnv(paramJCCompilationUnit);
/*     */ 
/* 304 */     if (bool) {
/* 305 */       Env localEnv2 = this.typeEnvs.get(paramJCCompilationUnit.packge);
/* 306 */       if (localEnv2 == null) {
/* 307 */         this.typeEnvs.put(paramJCCompilationUnit.packge, localEnv1);
/*     */       } else {
/* 309 */         localObject = localEnv2.toplevel;
/* 310 */         if (!this.fileManager.isSameFile(paramJCCompilationUnit.sourcefile, ((JCTree.JCCompilationUnit)localObject).sourcefile)) {
/* 311 */           this.log.warning(paramJCCompilationUnit.pid != null ? paramJCCompilationUnit.pid.pos() : null, "pkg-info.already.seen", new Object[] { paramJCCompilationUnit.packge });
/*     */ 
/* 315 */           if (i == 0) { if ((((JCTree.JCCompilationUnit)localObject).packageAnnotations.isEmpty()) && (paramJCCompilationUnit.docComments != null)) {
/* 317 */               if (!paramJCCompilationUnit.docComments
/* 317 */                 .hasComment(paramJCCompilationUnit));
/*     */             } } else this.typeEnvs.put(paramJCCompilationUnit.packge, localEnv1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 323 */       for (Object localObject = paramJCCompilationUnit.packge; (localObject != null) && (((Symbol)localObject).kind == 1); localObject = ((Symbol)localObject).owner) {
/* 324 */         localObject.flags_field |= 8388608L;
/*     */       }
/* 326 */       localObject = this.names.package_info;
/* 327 */       Symbol.ClassSymbol localClassSymbol = this.reader.enterClass((Name)localObject, paramJCCompilationUnit.packge);
/* 328 */       localClassSymbol.flatname = this.names.fromString(paramJCCompilationUnit.packge + "." + localObject);
/* 329 */       localClassSymbol.sourcefile = paramJCCompilationUnit.sourcefile;
/* 330 */       localClassSymbol.completer = null;
/* 331 */       localClassSymbol.members_field = new Scope(localClassSymbol);
/* 332 */       paramJCCompilationUnit.packge.package_info = localClassSymbol;
/*     */     }
/* 334 */     classEnter(paramJCCompilationUnit.defs, localEnv1);
/* 335 */     if (i != 0) {
/* 336 */       this.todo.append(localEnv1);
/*     */     }
/* 338 */     this.log.useSource(localJavaFileObject);
/* 339 */     this.result = null;
/*     */   }
/*     */ 
/*     */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*     */   {
/* 344 */     Symbol localSymbol1 = ((AttrContext)this.env.info).scope.owner;
/* 345 */     Scope localScope = enterScope(this.env);
/*     */     Symbol.ClassSymbol localClassSymbol;
/* 347 */     if (localSymbol1.kind == 1)
/*     */     {
/* 349 */       localObject1 = (Symbol.PackageSymbol)localSymbol1;
/* 350 */       for (localObject2 = localObject1; (localObject2 != null) && (((Symbol)localObject2).kind == 1); localObject2 = ((Symbol)localObject2).owner)
/* 351 */         localObject2.flags_field |= 8388608L;
/* 352 */       localClassSymbol = this.reader.enterClass(paramJCClassDecl.name, (Symbol.TypeSymbol)localObject1);
/* 353 */       ((Symbol.PackageSymbol)localObject1).members().enterIfAbsent(localClassSymbol);
/* 354 */       if (((paramJCClassDecl.mods.flags & 1L) != 0L) && (!classNameMatchesFileName(localClassSymbol, this.env)))
/* 355 */         this.log.error(paramJCClassDecl.pos(), "class.public.should.be.in.file", new Object[] { paramJCClassDecl.name });
/*     */     }
/*     */     else
/*     */     {
/* 359 */       if ((!paramJCClassDecl.name.isEmpty()) && 
/* 360 */         (!this.chk
/* 360 */         .checkUniqueClassName(paramJCClassDecl
/* 360 */         .pos(), paramJCClassDecl.name, localScope))) {
/* 361 */         this.result = null;
/* 362 */         return;
/*     */       }
/* 364 */       if (localSymbol1.kind == 2)
/*     */       {
/* 366 */         localClassSymbol = this.reader.enterClass(paramJCClassDecl.name, (Symbol.TypeSymbol)localSymbol1);
/* 367 */         if ((localSymbol1.flags_field & 0x200) != 0L)
/* 368 */           paramJCClassDecl.mods.flags |= 9L;
/*     */       }
/*     */       else
/*     */       {
/* 372 */         localClassSymbol = this.reader.defineClass(paramJCClassDecl.name, localSymbol1);
/* 373 */         localClassSymbol.flatname = this.chk.localClassName(localClassSymbol);
/* 374 */         if (!localClassSymbol.name.isEmpty())
/* 375 */           this.chk.checkTransparentClass(paramJCClassDecl.pos(), localClassSymbol, ((AttrContext)this.env.info).scope);
/*     */       }
/*     */     }
/* 378 */     paramJCClassDecl.sym = localClassSymbol;
/*     */ 
/* 381 */     if (this.chk.compiled.get(localClassSymbol.flatname) != null) {
/* 382 */       duplicateClass(paramJCClassDecl.pos(), localClassSymbol);
/* 383 */       this.result = this.types.createErrorType(paramJCClassDecl.name, (Symbol.TypeSymbol)localSymbol1, Type.noType);
/* 384 */       paramJCClassDecl.sym = ((Symbol.ClassSymbol)this.result.tsym);
/* 385 */       return;
/*     */     }
/* 387 */     this.chk.compiled.put(localClassSymbol.flatname, localClassSymbol);
/* 388 */     localScope.enter(localClassSymbol);
/*     */ 
/* 392 */     Object localObject1 = classEnv(paramJCClassDecl, this.env);
/* 393 */     this.typeEnvs.put(localClassSymbol, (Env)localObject1);
/*     */ 
/* 396 */     localClassSymbol.completer = this.memberEnter;
/* 397 */     localClassSymbol.flags_field = this.chk.checkFlags(paramJCClassDecl.pos(), paramJCClassDecl.mods.flags, localClassSymbol, paramJCClassDecl);
/* 398 */     localClassSymbol.sourcefile = this.env.toplevel.sourcefile;
/* 399 */     localClassSymbol.members_field = new Scope(localClassSymbol);
/*     */ 
/* 401 */     Object localObject2 = (Type.ClassType)localClassSymbol.type;
/* 402 */     if ((localSymbol1.kind != 1) && ((localClassSymbol.flags_field & 0x8) == 0L))
/*     */     {
/* 407 */       Symbol localSymbol2 = localSymbol1;
/* 408 */       while (((localSymbol2.kind & 0x14) != 0) && ((localSymbol2.flags_field & 0x8) == 0L))
/*     */       {
/* 410 */         localSymbol2 = localSymbol2.owner;
/*     */       }
/* 412 */       if (localSymbol2.kind == 2) {
/* 413 */         ((Type.ClassType)localObject2).setEnclosingType(localSymbol2.type);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 418 */     ((Type.ClassType)localObject2).typarams_field = classEnter(paramJCClassDecl.typarams, (Env)localObject1);
/*     */ 
/* 422 */     if ((!localClassSymbol.isLocal()) && (this.uncompleted != null)) this.uncompleted.append(localClassSymbol);
/*     */ 
/* 426 */     classEnter(paramJCClassDecl.defs, (Env)localObject1);
/*     */ 
/* 428 */     this.result = localClassSymbol.type;
/*     */   }
/*     */ 
/*     */   private static boolean classNameMatchesFileName(Symbol.ClassSymbol paramClassSymbol, Env<AttrContext> paramEnv)
/*     */   {
/* 435 */     return paramEnv.toplevel.sourcefile.isNameCompatible(paramClassSymbol.name.toString(), JavaFileObject.Kind.SOURCE);
/*     */   }
/*     */ 
/*     */   protected void duplicateClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 441 */     this.log.error(paramDiagnosticPosition, "duplicate.class", new Object[] { paramClassSymbol.fullname });
/*     */   }
/*     */ 
/*     */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter)
/*     */   {
/* 450 */     Type.TypeVar localTypeVar = paramJCTypeParameter.type != null ? (Type.TypeVar)paramJCTypeParameter.type : new Type.TypeVar(paramJCTypeParameter.name, ((AttrContext)this.env.info).scope.owner, this.syms.botType);
/*     */ 
/* 453 */     paramJCTypeParameter.type = localTypeVar;
/* 454 */     if (this.chk.checkUnique(paramJCTypeParameter.pos(), localTypeVar.tsym, ((AttrContext)this.env.info).scope)) {
/* 455 */       ((AttrContext)this.env.info).scope.enter(localTypeVar.tsym);
/*     */     }
/* 457 */     this.result = localTypeVar;
/*     */   }
/*     */ 
/*     */   public void visitTree(JCTree paramJCTree)
/*     */   {
/* 464 */     this.result = null;
/*     */   }
/*     */ 
/*     */   public void main(List<JCTree.JCCompilationUnit> paramList)
/*     */   {
/* 471 */     complete(paramList, null);
/*     */   }
/*     */ 
/*     */   public void complete(List<JCTree.JCCompilationUnit> paramList, Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 480 */     this.annotate.enterStart();
/* 481 */     ListBuffer localListBuffer = this.uncompleted;
/* 482 */     if (this.memberEnter.completionEnabled) this.uncompleted = new ListBuffer();
/*     */ 
/*     */     try
/*     */     {
/* 486 */       classEnter(paramList, null);
/*     */ 
/* 489 */       if (this.memberEnter.completionEnabled) {
/* 490 */         while (this.uncompleted.nonEmpty()) {
/* 491 */           localObject1 = (Symbol.ClassSymbol)this.uncompleted.next();
/* 492 */           if ((paramClassSymbol == null) || (paramClassSymbol == localObject1) || (localListBuffer == null)) {
/* 493 */             ((Symbol.ClassSymbol)localObject1).complete();
/*     */           }
/*     */           else {
/* 496 */             localListBuffer.append(localObject1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 501 */         for (localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)((Iterator)localObject1).next();
/* 502 */           if (localJCCompilationUnit.starImportScope.elems == null) {
/* 503 */             JavaFileObject localJavaFileObject = this.log.useSource(localJCCompilationUnit.sourcefile);
/* 504 */             Env localEnv = topLevelEnv(localJCCompilationUnit);
/* 505 */             this.memberEnter.memberEnter(localJCCompilationUnit, localEnv);
/* 506 */             this.log.useSource(localJavaFileObject);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       Object localObject1;
/* 511 */       this.uncompleted = localListBuffer;
/* 512 */       this.annotate.enterDone();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Enter
 * JD-Core Version:    0.6.2
 */