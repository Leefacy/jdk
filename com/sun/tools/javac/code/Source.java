/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.SourceVersion;
/*     */ 
/*     */ public enum Source
/*     */ {
/*  52 */   JDK1_2("1.2"), 
/*     */ 
/*  55 */   JDK1_3("1.3"), 
/*     */ 
/*  58 */   JDK1_4("1.4"), 
/*     */ 
/*  62 */   JDK1_5("1.5"), 
/*     */ 
/*  65 */   JDK1_6("1.6"), 
/*     */ 
/*  68 */   JDK1_7("1.7"), 
/*     */ 
/*  71 */   JDK1_8("1.8");
/*     */ 
/*     */   private static final Context.Key<Source> sourceKey;
/*     */   public final String name;
/*     */   private static final Map<String, Source> tab;
/* 105 */   public static final Source DEFAULT = JDK1_8;
/*     */ 
/*     */   public static Source instance(Context paramContext)
/*     */   {
/*  77 */     Source localSource = (Source)paramContext.get(sourceKey);
/*  78 */     if (localSource == null) {
/*  79 */       Options localOptions = Options.instance(paramContext);
/*  80 */       String str = localOptions.get(Option.SOURCE);
/*  81 */       if (str != null) localSource = lookup(str);
/*  82 */       if (localSource == null) localSource = DEFAULT;
/*  83 */       paramContext.put(sourceKey, localSource);
/*     */     }
/*  85 */     return localSource;
/*     */   }
/*     */ 
/*     */   private Source(String paramString)
/*     */   {
/* 102 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public static Source lookup(String paramString)
/*     */   {
/* 108 */     return (Source)tab.get(paramString);
/*     */   }
/*     */ 
/*     */   public Target requiredTarget() {
/* 112 */     if (compareTo(JDK1_8) >= 0) return Target.JDK1_8;
/* 113 */     if (compareTo(JDK1_7) >= 0) return Target.JDK1_7;
/* 114 */     if (compareTo(JDK1_6) >= 0) return Target.JDK1_6;
/* 115 */     if (compareTo(JDK1_5) >= 0) return Target.JDK1_5;
/* 116 */     if (compareTo(JDK1_4) >= 0) return Target.JDK1_4;
/* 117 */     return Target.JDK1_1;
/*     */   }
/*     */ 
/*     */   public boolean allowEncodingErrors()
/*     */   {
/* 122 */     return compareTo(JDK1_6) < 0;
/*     */   }
/*     */   public boolean allowAsserts() {
/* 125 */     return compareTo(JDK1_4) >= 0;
/*     */   }
/*     */   public boolean allowCovariantReturns() {
/* 128 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowGenerics() {
/* 131 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowDiamond() {
/* 134 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowMulticatch() {
/* 137 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowImprovedRethrowAnalysis() {
/* 140 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowImprovedCatchAnalysis() {
/* 143 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowEnums() {
/* 146 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowForeach() {
/* 149 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowStaticImport() {
/* 152 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowBoxing() {
/* 155 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowVarargs() {
/* 158 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowAnnotations() {
/* 161 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean allowHexFloats() {
/* 165 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowAnonOuterThis() {
/* 168 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean addBridges() {
/* 171 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean enforceMandatoryWarnings() {
/* 174 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean allowTryWithResources() {
/* 177 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowBinaryLiterals() {
/* 180 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowUnderscoresInLiterals() {
/* 183 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowStringsInSwitch() {
/* 186 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowSimplifiedVarargs() {
/* 189 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowObjectToPrimitiveCast() {
/* 192 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean enforceThisDotInit() {
/* 195 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */   public boolean allowPoly() {
/* 198 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowLambda() {
/* 201 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowMethodReferences() {
/* 204 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowDefaultMethods() {
/* 207 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowStaticInterfaceMethods() {
/* 210 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowStrictMethodClashCheck() {
/* 213 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowEffectivelyFinalInInnerClasses() {
/* 216 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowTypeAnnotations() {
/* 219 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowAnnotationsAfterTypeParams() {
/* 222 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowRepeatedAnnotations() {
/* 225 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowIntersectionTypesInCast() {
/* 228 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowGraphInference() {
/* 231 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowFunctionalInterfaceMostSpecific() {
/* 234 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public boolean allowPostApplicabilityVarargsAccessCheck() {
/* 237 */     return compareTo(JDK1_8) >= 0;
/*     */   }
/*     */   public static SourceVersion toSourceVersion(Source paramSource) {
/* 240 */     switch (1.$SwitchMap$com$sun$tools$javac$code$Source[paramSource.ordinal()]) {
/*     */     case 1:
/* 242 */       return SourceVersion.RELEASE_2;
/*     */     case 2:
/* 244 */       return SourceVersion.RELEASE_3;
/*     */     case 3:
/* 246 */       return SourceVersion.RELEASE_4;
/*     */     case 4:
/* 248 */       return SourceVersion.RELEASE_5;
/*     */     case 5:
/* 250 */       return SourceVersion.RELEASE_6;
/*     */     case 6:
/* 252 */       return SourceVersion.RELEASE_7;
/*     */     case 7:
/* 254 */       return SourceVersion.RELEASE_8;
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  73 */     sourceKey = new Context.Key();
/*     */ 
/*  90 */     tab = new HashMap();
/*     */ 
/*  92 */     for (Source localSource : values()) {
/*  93 */       tab.put(localSource.name, localSource);
/*     */     }
/*  95 */     tab.put("5", JDK1_5);
/*  96 */     tab.put("6", JDK1_6);
/*  97 */     tab.put("7", JDK1_7);
/*  98 */     tab.put("8", JDK1_8);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Source
 * JD-Core Version:    0.6.2
 */