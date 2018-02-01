/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Kinds;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.Type.CapturedType;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Type.ErrorType;
/*     */ import com.sun.tools.javac.code.Type.ForAll;
/*     */ import com.sun.tools.javac.code.Type.MethodType;
/*     */ import com.sun.tools.javac.code.Type.TypeVar;
/*     */ import com.sun.tools.javac.code.Type.WildcardType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.code.Types.DefaultSymbolVisitor;
/*     */ import com.sun.tools.javac.code.Types.UnaryVisitor;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class RichDiagnosticFormatter extends ForwardingDiagnosticFormatter<JCDiagnostic, AbstractDiagnosticFormatter>
/*     */ {
/*     */   final Symtab syms;
/*     */   final Types types;
/*     */   final JCDiagnostic.Factory diags;
/*     */   final JavacMessages messages;
/*     */   protected ClassNameSimplifier nameSimplifier;
/*     */   private RichPrinter printer;
/*     */   Map<WhereClauseKind, Map<Type, JCDiagnostic>> whereClauses;
/* 452 */   protected Types.UnaryVisitor<Void> typePreprocessor = new Types.UnaryVisitor()
/*     */   {
/*     */     public Void visit(List<Type> paramAnonymousList)
/*     */     {
/* 456 */       for (Type localType : paramAnonymousList)
/* 457 */         visit(localType);
/* 458 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitForAll(Type.ForAll paramAnonymousForAll, Void paramAnonymousVoid)
/*     */     {
/* 463 */       visit(paramAnonymousForAll.tvars);
/* 464 */       visit(paramAnonymousForAll.qtype);
/* 465 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitMethodType(Type.MethodType paramAnonymousMethodType, Void paramAnonymousVoid)
/*     */     {
/* 470 */       visit(paramAnonymousMethodType.argtypes);
/* 471 */       visit(paramAnonymousMethodType.restype);
/* 472 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitErrorType(Type.ErrorType paramAnonymousErrorType, Void paramAnonymousVoid)
/*     */     {
/* 477 */       Type localType = paramAnonymousErrorType.getOriginalType();
/* 478 */       if (localType != null)
/* 479 */         visit(localType);
/* 480 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitArrayType(Type.ArrayType paramAnonymousArrayType, Void paramAnonymousVoid)
/*     */     {
/* 485 */       visit(paramAnonymousArrayType.elemtype);
/* 486 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitWildcardType(Type.WildcardType paramAnonymousWildcardType, Void paramAnonymousVoid)
/*     */     {
/* 491 */       visit(paramAnonymousWildcardType.type);
/* 492 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitType(Type paramAnonymousType, Void paramAnonymousVoid) {
/* 496 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitCapturedType(Type.CapturedType paramAnonymousCapturedType, Void paramAnonymousVoid)
/*     */     {
/* 501 */       if (RichDiagnosticFormatter.this.indexOf(paramAnonymousCapturedType, RichDiagnosticFormatter.WhereClauseKind.CAPTURED) == -1) {
/* 502 */         String str = paramAnonymousCapturedType.lower == RichDiagnosticFormatter.this.syms.botType ? ".1" : "";
/* 503 */         JCDiagnostic localJCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.captured" + str, new Object[] { paramAnonymousCapturedType, paramAnonymousCapturedType.bound, paramAnonymousCapturedType.lower, paramAnonymousCapturedType.wildcard });
/* 504 */         ((Map)RichDiagnosticFormatter.this.whereClauses.get(RichDiagnosticFormatter.WhereClauseKind.CAPTURED)).put(paramAnonymousCapturedType, localJCDiagnostic);
/* 505 */         visit(paramAnonymousCapturedType.wildcard);
/* 506 */         visit(paramAnonymousCapturedType.lower);
/* 507 */         visit(paramAnonymousCapturedType.bound);
/*     */       }
/* 509 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitClassType(Type.ClassType paramAnonymousClassType, Void paramAnonymousVoid)
/*     */     {
/*     */       Object localObject;
/* 514 */       if (paramAnonymousClassType.isCompound()) {
/* 515 */         if (RichDiagnosticFormatter.this.indexOf(paramAnonymousClassType, RichDiagnosticFormatter.WhereClauseKind.INTERSECTION) == -1) {
/* 516 */           localObject = RichDiagnosticFormatter.this.types.supertype(paramAnonymousClassType);
/* 517 */           List localList = RichDiagnosticFormatter.this.types.interfaces(paramAnonymousClassType);
/* 518 */           JCDiagnostic localJCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.intersection", new Object[] { paramAnonymousClassType, localList.prepend(localObject) });
/* 519 */           ((Map)RichDiagnosticFormatter.this.whereClauses.get(RichDiagnosticFormatter.WhereClauseKind.INTERSECTION)).put(paramAnonymousClassType, localJCDiagnostic);
/* 520 */           visit((Type)localObject);
/* 521 */           visit(localList);
/*     */         }
/* 523 */       } else if (paramAnonymousClassType.tsym.name.isEmpty())
/*     */       {
/* 525 */         localObject = (Type.ClassType)paramAnonymousClassType.tsym.type;
/* 526 */         if (localObject != null) {
/* 527 */           if ((((Type.ClassType)localObject).interfaces_field != null) && (((Type.ClassType)localObject).interfaces_field.nonEmpty()))
/* 528 */             visit((Type)((Type.ClassType)localObject).interfaces_field.head);
/*     */           else {
/* 530 */             visit(((Type.ClassType)localObject).supertype_field);
/*     */           }
/*     */         }
/*     */       }
/* 534 */       RichDiagnosticFormatter.this.nameSimplifier.addUsage(paramAnonymousClassType.tsym);
/* 535 */       visit(paramAnonymousClassType.getTypeArguments());
/* 536 */       if (paramAnonymousClassType.getEnclosingType() != Type.noType)
/* 537 */         visit(paramAnonymousClassType.getEnclosingType());
/* 538 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitTypeVar(Type.TypeVar paramAnonymousTypeVar, Void paramAnonymousVoid)
/*     */     {
/* 543 */       if (RichDiagnosticFormatter.this.indexOf(paramAnonymousTypeVar, RichDiagnosticFormatter.WhereClauseKind.TYPEVAR) == -1)
/*     */       {
/* 545 */         Type localType = paramAnonymousTypeVar.bound;
/* 546 */         while ((localType instanceof Type.ErrorType)) {
/* 547 */           localType = ((Type.ErrorType)localType).getOriginalType();
/*     */         }
/*     */ 
/* 553 */         List localList = (localType != null) && (
/* 551 */           (localType
/* 551 */           .hasTag(TypeTag.CLASS)) || 
/* 551 */           (localType.hasTag(TypeTag.TYPEVAR))) ? RichDiagnosticFormatter.this.types
/* 552 */           .getBounds(paramAnonymousTypeVar) : 
/* 553 */           List.nil();
/*     */ 
/* 555 */         RichDiagnosticFormatter.this.nameSimplifier.addUsage(paramAnonymousTypeVar.tsym);
/*     */ 
/* 559 */         int i = (localList.head == null) || 
/* 558 */           (((Type)localList.head)
/* 558 */           .hasTag(TypeTag.NONE)) || 
/* 559 */           (((Type)localList.head)
/* 559 */           .hasTag(TypeTag.ERROR)) ? 
/* 559 */           1 : 0;
/*     */         JCDiagnostic localJCDiagnostic;
/* 561 */         if ((paramAnonymousTypeVar.tsym.flags() & 0x1000) == 0L)
/*     */         {
/* 563 */           localJCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.typevar" + (i != 0 ? ".1" : ""), new Object[] { paramAnonymousTypeVar, localList, 
/* 565 */             Kinds.kindName(paramAnonymousTypeVar.tsym
/* 565 */             .location()), paramAnonymousTypeVar.tsym.location() });
/* 566 */           ((Map)RichDiagnosticFormatter.this.whereClauses.get(RichDiagnosticFormatter.WhereClauseKind.TYPEVAR)).put(paramAnonymousTypeVar, localJCDiagnostic);
/* 567 */           RichDiagnosticFormatter.this.symbolPreprocessor.visit(paramAnonymousTypeVar.tsym.location(), null);
/* 568 */           visit(localList);
/*     */         } else {
/* 570 */           Assert.check(i == 0);
/*     */ 
/* 572 */           localJCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.fresh.typevar", new Object[] { paramAnonymousTypeVar, localList });
/* 573 */           ((Map)RichDiagnosticFormatter.this.whereClauses.get(RichDiagnosticFormatter.WhereClauseKind.TYPEVAR)).put(paramAnonymousTypeVar, localJCDiagnostic);
/* 574 */           visit(localList);
/*     */         }
/*     */       }
/*     */ 
/* 578 */       return null;
/*     */     }
/* 452 */   };
/*     */ 
/* 592 */   protected Types.DefaultSymbolVisitor<Void, Void> symbolPreprocessor = new Types.DefaultSymbolVisitor()
/*     */   {
/*     */     public Void visitClassSymbol(Symbol.ClassSymbol paramAnonymousClassSymbol, Void paramAnonymousVoid)
/*     */     {
/* 597 */       if (paramAnonymousClassSymbol.type.isCompound())
/* 598 */         RichDiagnosticFormatter.this.typePreprocessor.visit(paramAnonymousClassSymbol.type);
/*     */       else {
/* 600 */         RichDiagnosticFormatter.this.nameSimplifier.addUsage(paramAnonymousClassSymbol);
/*     */       }
/* 602 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSymbol(Symbol paramAnonymousSymbol, Void paramAnonymousVoid)
/*     */     {
/* 607 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitMethodSymbol(Symbol.MethodSymbol paramAnonymousMethodSymbol, Void paramAnonymousVoid)
/*     */     {
/* 612 */       visit(paramAnonymousMethodSymbol.owner, null);
/* 613 */       if (paramAnonymousMethodSymbol.type != null)
/* 614 */         RichDiagnosticFormatter.this.typePreprocessor.visit(paramAnonymousMethodSymbol.type);
/* 615 */       return null;
/*     */     }
/* 592 */   };
/*     */ 
/*     */   public static RichDiagnosticFormatter instance(Context paramContext)
/*     */   {
/*  87 */     RichDiagnosticFormatter localRichDiagnosticFormatter = (RichDiagnosticFormatter)paramContext.get(RichDiagnosticFormatter.class);
/*  88 */     if (localRichDiagnosticFormatter == null)
/*  89 */       localRichDiagnosticFormatter = new RichDiagnosticFormatter(paramContext);
/*  90 */     return localRichDiagnosticFormatter;
/*     */   }
/*     */ 
/*     */   protected RichDiagnosticFormatter(Context paramContext) {
/*  94 */     super((AbstractDiagnosticFormatter)Log.instance(paramContext).getDiagnosticFormatter());
/*  95 */     setRichPrinter(new RichPrinter());
/*  96 */     this.syms = Symtab.instance(paramContext);
/*  97 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  98 */     this.types = Types.instance(paramContext);
/*  99 */     this.messages = JavacMessages.instance(paramContext);
/* 100 */     this.whereClauses = new EnumMap(WhereClauseKind.class);
/* 101 */     this.configuration = new RichConfiguration(Options.instance(paramContext), (AbstractDiagnosticFormatter)this.formatter);
/* 102 */     for (WhereClauseKind localWhereClauseKind : WhereClauseKind.values())
/* 103 */       this.whereClauses.put(localWhereClauseKind, new LinkedHashMap());
/*     */   }
/*     */ 
/*     */   public String format(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 108 */     StringBuilder localStringBuilder = new StringBuilder();
/* 109 */     this.nameSimplifier = new ClassNameSimplifier();
/*     */     Object localObject2;
/* 110 */     for (localObject2 : WhereClauseKind.values())
/* 111 */       ((Map)this.whereClauses.get(localObject2)).clear();
/* 112 */     preprocessDiagnostic(paramJCDiagnostic);
/* 113 */     localStringBuilder.append(((AbstractDiagnosticFormatter)this.formatter).format(paramJCDiagnostic, paramLocale));
/*     */     String str1;
/*     */     Iterator localIterator;
/* 114 */     if (getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.WHERE_CLAUSES)) {
/* 115 */       ??? = getWhereClauses();
/*     */ 
/* 117 */       str1 = ((AbstractDiagnosticFormatter)this.formatter).isRaw() ? "" : ((AbstractDiagnosticFormatter)this.formatter)
/* 117 */         .indentString(2);
/*     */ 
/* 118 */       for (localIterator = ((List)???).iterator(); localIterator.hasNext(); ) { localObject2 = (JCDiagnostic)localIterator.next();
/* 119 */         String str2 = ((AbstractDiagnosticFormatter)this.formatter).format((JCDiagnostic)localObject2, paramLocale);
/* 120 */         if (str2.length() > 0) {
/* 121 */           localStringBuilder.append('\n' + str1 + str2);
/*     */         }
/*     */       }
/*     */     }
/* 125 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 130 */     this.nameSimplifier = new ClassNameSimplifier();
/* 131 */     preprocessDiagnostic(paramJCDiagnostic);
/* 132 */     return super.formatMessage(paramJCDiagnostic, paramLocale);
/*     */   }
/*     */ 
/*     */   protected void setRichPrinter(RichPrinter paramRichPrinter)
/*     */   {
/* 140 */     this.printer = paramRichPrinter;
/* 141 */     ((AbstractDiagnosticFormatter)this.formatter).setPrinter(paramRichPrinter);
/*     */   }
/*     */ 
/*     */   protected RichPrinter getRichPrinter()
/*     */   {
/* 149 */     return this.printer;
/*     */   }
/*     */ 
/*     */   protected void preprocessDiagnostic(JCDiagnostic paramJCDiagnostic)
/*     */   {
/* 161 */     for (Object localObject2 : paramJCDiagnostic.getArgs()) {
/* 162 */       if (localObject2 != null) {
/* 163 */         preprocessArgument(localObject2);
/*     */       }
/*     */     }
/* 166 */     if (paramJCDiagnostic.isMultiline())
/* 167 */       for (??? = paramJCDiagnostic.getSubdiagnostics().iterator(); ((Iterator)???).hasNext(); ) { JCDiagnostic localJCDiagnostic = (JCDiagnostic)((Iterator)???).next();
/* 168 */         preprocessDiagnostic(localJCDiagnostic);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void preprocessArgument(Object paramObject)
/*     */   {
/*     */     Iterator localIterator;
/* 179 */     if ((paramObject instanceof Type)) {
/* 180 */       preprocessType((Type)paramObject);
/*     */     }
/* 182 */     else if ((paramObject instanceof Symbol)) {
/* 183 */       preprocessSymbol((Symbol)paramObject);
/*     */     }
/* 185 */     else if ((paramObject instanceof JCDiagnostic)) {
/* 186 */       preprocessDiagnostic((JCDiagnostic)paramObject);
/*     */     }
/* 188 */     else if ((paramObject instanceof Iterable))
/* 189 */       for (localIterator = ((Iterable)paramObject).iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 190 */         preprocessArgument(localObject);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected List<JCDiagnostic> getWhereClauses()
/*     */   {
/* 202 */     List localList1 = List.nil();
/* 203 */     for (WhereClauseKind localWhereClauseKind : WhereClauseKind.values()) {
/* 204 */       List localList2 = List.nil();
/* 205 */       for (Object localObject1 = ((Map)this.whereClauses.get(localWhereClauseKind)).entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/* 206 */         localList2 = localList2.prepend(((Map.Entry)localObject2).getValue());
/*     */       }
/*     */       Object localObject2;
/* 208 */       if (!localList2.isEmpty()) {
/* 209 */         localObject1 = localWhereClauseKind.key();
/* 210 */         if (localList2.size() > 1)
/* 211 */           localObject1 = (String)localObject1 + ".1";
/* 212 */         localObject2 = this.diags.fragment((String)localObject1, new Object[] { ((Map)this.whereClauses.get(localWhereClauseKind)).keySet() });
/* 213 */         localObject2 = new JCDiagnostic.MultilineDiagnostic((JCDiagnostic)localObject2, localList2.reverse());
/* 214 */         localList1 = localList1.prepend(localObject2);
/*     */       }
/*     */     }
/* 217 */     return localList1.reverse();
/*     */   }
/*     */ 
/*     */   private int indexOf(Type paramType, WhereClauseKind paramWhereClauseKind) {
/* 221 */     int i = 1;
/* 222 */     for (Type localType : ((Map)this.whereClauses.get(paramWhereClauseKind)).keySet()) {
/* 223 */       if (localType.tsym == paramType.tsym) {
/* 224 */         return i;
/*     */       }
/* 226 */       if ((paramWhereClauseKind != WhereClauseKind.TYPEVAR) || 
/* 227 */         (localType
/* 227 */         .toString().equals(paramType.toString()))) {
/* 228 */         i++;
/*     */       }
/*     */     }
/* 231 */     return -1;
/*     */   }
/*     */ 
/*     */   private boolean unique(Type.TypeVar paramTypeVar) {
/* 235 */     int i = 0;
/* 236 */     for (Type localType : ((Map)this.whereClauses.get(WhereClauseKind.TYPEVAR)).keySet()) {
/* 237 */       if (localType.toString().equals(paramTypeVar.toString())) {
/* 238 */         i++;
/*     */       }
/*     */     }
/* 241 */     if (i < 1)
/* 242 */       throw new AssertionError("Missing type variable in where clause " + paramTypeVar);
/* 243 */     return i == 1;
/*     */   }
/*     */ 
/*     */   protected void preprocessType(Type paramType)
/*     */   {
/* 449 */     this.typePreprocessor.visit(paramType);
/*     */   }
/*     */ 
/*     */   protected void preprocessSymbol(Symbol paramSymbol)
/*     */   {
/* 589 */     this.symbolPreprocessor.visit(paramSymbol, null);
/*     */   }
/*     */ 
/*     */   public RichConfiguration getConfiguration()
/*     */   {
/* 623 */     return (RichConfiguration)this.configuration;
/*     */   }
/*     */ 
/*     */   protected class ClassNameSimplifier
/*     */   {
/* 282 */     Map<Name, List<Symbol>> nameClashes = new HashMap();
/*     */ 
/*     */     protected ClassNameSimplifier() {
/*     */     }
/*     */ 
/*     */     protected void addUsage(Symbol paramSymbol) {
/* 288 */       Name localName = paramSymbol.getSimpleName();
/* 289 */       List localList = (List)this.nameClashes.get(localName);
/* 290 */       if (localList == null) {
/* 291 */         localList = List.nil();
/*     */       }
/* 293 */       if (!localList.contains(paramSymbol))
/* 294 */         this.nameClashes.put(localName, localList.append(paramSymbol));
/*     */     }
/*     */ 
/*     */     public String simplify(Symbol paramSymbol) {
/* 298 */       String str1 = paramSymbol.getQualifiedName().toString();
/* 299 */       if ((!paramSymbol.type.isCompound()) && (!paramSymbol.type.isPrimitive())) {
/* 300 */         List localList1 = (List)this.nameClashes.get(paramSymbol.getSimpleName());
/* 301 */         if ((localList1 == null) || (
/* 302 */           (localList1
/* 302 */           .size() == 1) && 
/* 303 */           (localList1
/* 303 */           .contains(paramSymbol))))
/*     */         {
/* 304 */           List localList2 = List.nil();
/* 305 */           Symbol localSymbol = paramSymbol;
/* 306 */           while ((localSymbol.type.hasTag(TypeTag.CLASS)) && 
/* 307 */             (localSymbol.type
/* 307 */             .getEnclosingType().hasTag(TypeTag.CLASS)) && (localSymbol.owner.kind == 2))
/*     */           {
/* 309 */             localList2 = localList2.prepend(localSymbol.getSimpleName());
/* 310 */             localSymbol = localSymbol.owner;
/*     */           }
/* 312 */           localList2 = localList2.prepend(localSymbol.getSimpleName());
/* 313 */           StringBuilder localStringBuilder = new StringBuilder();
/* 314 */           String str2 = "";
/* 315 */           for (Name localName : localList2) {
/* 316 */             localStringBuilder.append(str2);
/* 317 */             localStringBuilder.append(localName);
/* 318 */             str2 = ".";
/*     */           }
/* 320 */           str1 = localStringBuilder.toString();
/*     */         }
/*     */       }
/* 323 */       return str1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class RichConfiguration extends ForwardingDiagnosticFormatter.ForwardingConfiguration
/*     */   {
/*     */     protected EnumSet<RichFormatterFeature> features;
/*     */ 
/*     */     public RichConfiguration(Options paramOptions, AbstractDiagnosticFormatter paramAbstractDiagnosticFormatter)
/*     */     {
/* 636 */       super();
/* 637 */       this.features = (paramAbstractDiagnosticFormatter.isRaw() ? EnumSet.noneOf(RichFormatterFeature.class) : 
/* 638 */         EnumSet.of(RichFormatterFeature.SIMPLE_NAMES, RichFormatterFeature.WHERE_CLAUSES, RichFormatterFeature.UNIQUE_TYPEVAR_NAMES));
/*     */ 
/* 641 */       String str1 = paramOptions.get("diags");
/* 642 */       if (str1 != null)
/* 643 */         for (String str2 : str1.split(",")) {
/* 644 */           if (str2.equals("-where")) {
/* 645 */             this.features.remove(RichFormatterFeature.WHERE_CLAUSES);
/*     */           }
/* 647 */           else if (str2.equals("where")) {
/* 648 */             this.features.add(RichFormatterFeature.WHERE_CLAUSES);
/*     */           }
/* 650 */           if (str2.equals("-simpleNames")) {
/* 651 */             this.features.remove(RichFormatterFeature.SIMPLE_NAMES);
/*     */           }
/* 653 */           else if (str2.equals("simpleNames")) {
/* 654 */             this.features.add(RichFormatterFeature.SIMPLE_NAMES);
/*     */           }
/* 656 */           if (str2.equals("-disambiguateTvars")) {
/* 657 */             this.features.remove(RichFormatterFeature.UNIQUE_TYPEVAR_NAMES);
/*     */           }
/* 659 */           else if (str2.equals("disambiguateTvars"))
/* 660 */             this.features.add(RichFormatterFeature.UNIQUE_TYPEVAR_NAMES);
/*     */         }
/*     */     }
/*     */ 
/*     */     public RichFormatterFeature[] getAvailableFeatures()
/*     */     {
/* 671 */       return RichFormatterFeature.values();
/*     */     }
/*     */ 
/*     */     public void enable(RichFormatterFeature paramRichFormatterFeature)
/*     */     {
/* 679 */       this.features.add(paramRichFormatterFeature);
/*     */     }
/*     */ 
/*     */     public void disable(RichFormatterFeature paramRichFormatterFeature)
/*     */     {
/* 687 */       this.features.remove(paramRichFormatterFeature);
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(RichFormatterFeature paramRichFormatterFeature)
/*     */     {
/* 695 */       return this.features.contains(paramRichFormatterFeature);
/*     */     }
/*     */ 
/*     */     public static enum RichFormatterFeature
/*     */     {
/* 703 */       WHERE_CLAUSES, 
/*     */ 
/* 705 */       SIMPLE_NAMES, 
/*     */ 
/* 707 */       UNIQUE_TYPEVAR_NAMES;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class RichPrinter extends Printer
/*     */   {
/*     */     protected RichPrinter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String localize(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 339 */       return ((AbstractDiagnosticFormatter)RichDiagnosticFormatter.this.formatter).localize(paramLocale, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public String capturedVarId(Type.CapturedType paramCapturedType, Locale paramLocale)
/*     */     {
/* 344 */       return RichDiagnosticFormatter.this.indexOf(paramCapturedType, RichDiagnosticFormatter.WhereClauseKind.CAPTURED) + "";
/*     */     }
/*     */ 
/*     */     public String visitType(Type paramType, Locale paramLocale)
/*     */     {
/* 349 */       String str = super.visitType(paramType, paramLocale);
/* 350 */       if (paramType == RichDiagnosticFormatter.this.syms.botType)
/* 351 */         str = localize(paramLocale, "compiler.misc.type.null", new Object[0]);
/* 352 */       return str;
/*     */     }
/*     */ 
/*     */     public String visitCapturedType(Type.CapturedType paramCapturedType, Locale paramLocale)
/*     */     {
/* 357 */       if (RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.WHERE_CLAUSES)) {
/* 358 */         return localize(paramLocale, "compiler.misc.captured.type", new Object[] { 
/* 360 */           Integer.valueOf(RichDiagnosticFormatter.this
/* 360 */           .indexOf(paramCapturedType, RichDiagnosticFormatter.WhereClauseKind.CAPTURED)) });
/*     */       }
/*     */ 
/* 363 */       return super.visitCapturedType(paramCapturedType, paramLocale);
/*     */     }
/*     */ 
/*     */     public String visitClassType(Type.ClassType paramClassType, Locale paramLocale)
/*     */     {
/* 368 */       if ((paramClassType.isCompound()) && 
/* 369 */         (RichDiagnosticFormatter.this
/* 369 */         .getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.WHERE_CLAUSES))) {
/* 370 */         return localize(paramLocale, "compiler.misc.intersection.type", new Object[] { 
/* 372 */           Integer.valueOf(RichDiagnosticFormatter.this
/* 372 */           .indexOf(paramClassType, RichDiagnosticFormatter.WhereClauseKind.INTERSECTION)) });
/*     */       }
/*     */ 
/* 375 */       return super.visitClassType(paramClassType, paramLocale);
/*     */     }
/*     */ 
/*     */     protected String className(Type.ClassType paramClassType, boolean paramBoolean, Locale paramLocale)
/*     */     {
/* 380 */       Symbol.TypeSymbol localTypeSymbol = paramClassType.tsym;
/* 381 */       if ((localTypeSymbol.name.length() == 0) || 
/* 382 */         (!RichDiagnosticFormatter.this
/* 382 */         .getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.SIMPLE_NAMES))) {
/* 383 */         return super.className(paramClassType, paramBoolean, paramLocale);
/*     */       }
/* 385 */       if (paramBoolean) {
/* 386 */         return RichDiagnosticFormatter.this.nameSimplifier.simplify(localTypeSymbol).toString();
/*     */       }
/* 388 */       return localTypeSymbol.name.toString();
/*     */     }
/*     */ 
/*     */     public String visitTypeVar(Type.TypeVar paramTypeVar, Locale paramLocale)
/*     */     {
/* 393 */       if ((RichDiagnosticFormatter.this.unique(paramTypeVar)) || 
/* 394 */         (!RichDiagnosticFormatter.this
/* 394 */         .getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.UNIQUE_TYPEVAR_NAMES))) {
/* 395 */         return paramTypeVar.toString();
/*     */       }
/*     */ 
/* 398 */       return localize(paramLocale, "compiler.misc.type.var", new Object[] { paramTypeVar
/* 400 */         .toString(), Integer.valueOf(RichDiagnosticFormatter.this.indexOf(paramTypeVar, RichDiagnosticFormatter.WhereClauseKind.TYPEVAR)) });
/*     */     }
/*     */ 
/*     */     public String visitClassSymbol(Symbol.ClassSymbol paramClassSymbol, Locale paramLocale)
/*     */     {
/* 406 */       if (paramClassSymbol.type.isCompound()) {
/* 407 */         return visit(paramClassSymbol.type, paramLocale);
/*     */       }
/* 409 */       String str = RichDiagnosticFormatter.this.nameSimplifier.simplify(paramClassSymbol);
/* 410 */       if ((str.length() == 0) || 
/* 411 */         (!RichDiagnosticFormatter.this
/* 411 */         .getConfiguration().isEnabled(RichDiagnosticFormatter.RichConfiguration.RichFormatterFeature.SIMPLE_NAMES))) {
/* 412 */         return super.visitClassSymbol(paramClassSymbol, paramLocale);
/*     */       }
/*     */ 
/* 415 */       return str;
/*     */     }
/*     */ 
/*     */     public String visitMethodSymbol(Symbol.MethodSymbol paramMethodSymbol, Locale paramLocale)
/*     */     {
/* 421 */       String str1 = visit(paramMethodSymbol.owner, paramLocale);
/* 422 */       if (paramMethodSymbol.isStaticOrInstanceInit()) {
/* 423 */         return str1;
/*     */       }
/*     */ 
/* 427 */       String str2 = paramMethodSymbol.name == paramMethodSymbol.name.table.names.init ? str1 : paramMethodSymbol.name
/* 427 */         .toString();
/* 428 */       if (paramMethodSymbol.type != null) {
/* 429 */         if (paramMethodSymbol.type.hasTag(TypeTag.FORALL)) {
/* 430 */           str2 = "<" + visitTypes(paramMethodSymbol.type.getTypeArguments(), paramLocale) + ">" + str2;
/*     */         }
/* 432 */         str2 = str2 + "(" + printMethodArgs(paramMethodSymbol.type
/* 433 */           .getParameterTypes(), 
/* 434 */           (paramMethodSymbol
/* 434 */           .flags() & 0x0) != 0L, paramLocale) + ")";
/*     */       }
/*     */ 
/* 437 */       return str2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum WhereClauseKind
/*     */   {
/* 253 */     TYPEVAR("where.description.typevar"), 
/*     */ 
/* 255 */     CAPTURED("where.description.captured"), 
/*     */ 
/* 257 */     INTERSECTION("where.description.intersection");
/*     */ 
/*     */     private final String key;
/*     */ 
/*     */     private WhereClauseKind(String paramString)
/*     */     {
/* 263 */       this.key = paramString;
/*     */     }
/*     */ 
/*     */     String key() {
/* 267 */       return this.key;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.RichDiagnosticFormatter
 * JD-Core Version:    0.6.2
 */