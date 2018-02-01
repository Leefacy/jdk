/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class Lint
/*     */ {
/*  47 */   protected static final Context.Key<Lint> lintKey = new Context.Key();
/*     */   private final AugmentVisitor augmentor;
/*     */   private final EnumSet<LintCategory> values;
/*     */   private final EnumSet<LintCategory> suppressedValues;
/*  86 */   private static final Map<String, LintCategory> map = new ConcurrentHashMap(20);
/*     */ 
/*     */   public static Lint instance(Context paramContext)
/*     */   {
/*  51 */     Lint localLint = (Lint)paramContext.get(lintKey);
/*  52 */     if (localLint == null)
/*  53 */       localLint = new Lint(paramContext);
/*  54 */     return localLint;
/*     */   }
/*     */ 
/*     */   public Lint augment(Attribute.Compound paramCompound)
/*     */   {
/*  62 */     return this.augmentor.augment(this, paramCompound);
/*     */   }
/*     */ 
/*     */   public Lint augment(Symbol paramSymbol)
/*     */   {
/*  71 */     Lint localLint = this.augmentor.augment(this, paramSymbol.getDeclarationAttributes());
/*  72 */     if (paramSymbol.isDeprecated()) {
/*  73 */       if (localLint == this)
/*  74 */         localLint = new Lint(this);
/*  75 */       localLint.values.remove(LintCategory.DEPRECATION);
/*  76 */       localLint.suppressedValues.add(LintCategory.DEPRECATION);
/*     */     }
/*  78 */     return localLint;
/*     */   }
/*     */ 
/*     */   protected Lint(Context paramContext)
/*     */   {
/*  91 */     Options localOptions = Options.instance(paramContext);
/*  92 */     this.values = EnumSet.noneOf(LintCategory.class);
/*  93 */     for (Map.Entry localEntry : map.entrySet()) {
/*  94 */       if (localOptions.lint((String)localEntry.getKey())) {
/*  95 */         this.values.add(localEntry.getValue());
/*     */       }
/*     */     }
/*  98 */     this.suppressedValues = EnumSet.noneOf(LintCategory.class);
/*     */ 
/* 100 */     paramContext.put(lintKey, this);
/* 101 */     this.augmentor = new AugmentVisitor(paramContext);
/*     */   }
/*     */ 
/*     */   protected Lint(Lint paramLint) {
/* 105 */     this.augmentor = paramLint.augmentor;
/* 106 */     this.values = paramLint.values.clone();
/* 107 */     this.suppressedValues = paramLint.suppressedValues.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return "Lint:[values" + this.values + " suppressedValues" + this.suppressedValues + "]";
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(LintCategory paramLintCategory)
/*     */   {
/* 253 */     return this.values.contains(paramLintCategory);
/*     */   }
/*     */ 
/*     */   public boolean isSuppressed(LintCategory paramLintCategory)
/*     */   {
/* 263 */     return this.suppressedValues.contains(paramLintCategory);
/*     */   }
/*     */   protected static class AugmentVisitor implements Attribute.Visitor {
/*     */     private final Context context;
/*     */     private Symtab syms;
/*     */     private Lint parent;
/*     */     private Lint lint;
/*     */ 
/*     */     AugmentVisitor(Context paramContext) {
/* 275 */       this.context = paramContext;
/*     */     }
/*     */ 
/*     */     Lint augment(Lint paramLint, Attribute.Compound paramCompound) {
/* 279 */       initSyms();
/* 280 */       this.parent = paramLint;
/* 281 */       this.lint = null;
/* 282 */       paramCompound.accept(this);
/* 283 */       return this.lint == null ? paramLint : this.lint;
/*     */     }
/*     */ 
/*     */     Lint augment(Lint paramLint, List<Attribute.Compound> paramList) {
/* 287 */       initSyms();
/* 288 */       this.parent = paramLint;
/* 289 */       this.lint = null;
/* 290 */       for (Attribute.Compound localCompound : paramList) {
/* 291 */         localCompound.accept(this);
/*     */       }
/* 293 */       return this.lint == null ? paramLint : this.lint;
/*     */     }
/*     */ 
/*     */     private void initSyms() {
/* 297 */       if (this.syms == null)
/* 298 */         this.syms = Symtab.instance(this.context);
/*     */     }
/*     */ 
/*     */     private void suppress(Lint.LintCategory paramLintCategory) {
/* 302 */       if (this.lint == null)
/* 303 */         this.lint = new Lint(this.parent);
/* 304 */       this.lint.suppressedValues.add(paramLintCategory);
/* 305 */       this.lint.values.remove(paramLintCategory);
/*     */     }
/*     */ 
/*     */     public void visitConstant(Attribute.Constant paramConstant) {
/* 309 */       if (paramConstant.type.tsym == this.syms.stringType.tsym) {
/* 310 */         Lint.LintCategory localLintCategory = Lint.LintCategory.get((String)paramConstant.value);
/* 311 */         if (localLintCategory != null)
/* 312 */           suppress(localLintCategory);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitClass(Attribute.Class paramClass)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitCompound(Attribute.Compound paramCompound)
/*     */     {
/* 323 */       if (paramCompound.type.tsym == this.syms.suppressWarningsType.tsym)
/* 324 */         for (List localList = paramCompound.values; 
/* 325 */           localList.nonEmpty(); localList = localList.tail) {
/* 326 */           Pair localPair = (Pair)localList.head;
/* 327 */           if (((Symbol.MethodSymbol)localPair.fst).name.toString().equals("value"))
/* 328 */             ((Attribute)localPair.snd).accept(this);
/*     */         }
/*     */     }
/*     */ 
/*     */     public void visitArray(Attribute.Array paramArray)
/*     */     {
/* 335 */       for (Attribute localAttribute : paramArray.values)
/* 336 */         localAttribute.accept(this);
/*     */     }
/*     */ 
/*     */     public void visitEnum(Attribute.Enum paramEnum)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitError(Attribute.Error paramError)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum LintCategory
/*     */   {
/* 124 */     AUXILIARYCLASS("auxiliaryclass"), 
/*     */ 
/* 129 */     CAST("cast"), 
/*     */ 
/* 134 */     CLASSFILE("classfile"), 
/*     */ 
/* 139 */     DEPRECATION("deprecation"), 
/*     */ 
/* 145 */     DEP_ANN("dep-ann"), 
/*     */ 
/* 150 */     DIVZERO("divzero"), 
/*     */ 
/* 155 */     EMPTY("empty"), 
/*     */ 
/* 160 */     FALLTHROUGH("fallthrough"), 
/*     */ 
/* 165 */     FINALLY("finally"), 
/*     */ 
/* 170 */     OPTIONS("options"), 
/*     */ 
/* 175 */     OVERLOADS("overloads"), 
/*     */ 
/* 180 */     OVERRIDES("overrides"), 
/*     */ 
/* 187 */     PATH("path"), 
/*     */ 
/* 192 */     PROCESSING("processing"), 
/*     */ 
/* 197 */     RAW("rawtypes"), 
/*     */ 
/* 202 */     SERIAL("serial"), 
/*     */ 
/* 207 */     STATIC("static"), 
/*     */ 
/* 212 */     SUNAPI("sunapi", true), 
/*     */ 
/* 217 */     TRY("try"), 
/*     */ 
/* 222 */     UNCHECKED("unchecked"), 
/*     */ 
/* 227 */     VARARGS("varargs");
/*     */ 
/*     */     public final String option;
/*     */     public final boolean hidden;
/*     */ 
/* 230 */     private LintCategory(String paramString) { this(paramString, false); }
/*     */ 
/*     */     private LintCategory(String paramString, boolean paramBoolean)
/*     */     {
/* 234 */       this.option = paramString;
/* 235 */       this.hidden = paramBoolean;
/* 236 */       Lint.map.put(paramString, this);
/*     */     }
/*     */ 
/*     */     static LintCategory get(String paramString) {
/* 240 */       return (LintCategory)Lint.map.get(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Lint
 * JD-Core Version:    0.6.2
 */