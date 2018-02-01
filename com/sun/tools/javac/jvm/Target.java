/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public enum Target
/*     */ {
/*  44 */   JDK1_1("1.1", 45, 3), 
/*  45 */   JDK1_2("1.2", 46, 0), 
/*  46 */   JDK1_3("1.3", 47, 0), 
/*     */ 
/*  49 */   JDK1_4("1.4", 48, 0), 
/*     */ 
/*  52 */   JDK1_5("1.5", 49, 0), 
/*     */ 
/*  55 */   JDK1_6("1.6", 50, 0), 
/*     */ 
/*  58 */   JDK1_7("1.7", 51, 0), 
/*     */ 
/*  61 */   JDK1_8("1.8", 52, 0);
/*     */ 
/*     */   private static final Context.Key<Target> targetKey;
/*     */   private static final Target MIN;
/*     */   private static final Target MAX;
/*     */   private static final Map<String, Target> tab;
/*     */   public final String name;
/*     */   public final int majorVersion;
/*     */   public final int minorVersion;
/* 104 */   public static final Target DEFAULT = JDK1_8;
/*     */ 
/*     */   public static Target instance(Context paramContext)
/*     */   {
/*  67 */     Target localTarget = (Target)paramContext.get(targetKey);
/*  68 */     if (localTarget == null) {
/*  69 */       Options localOptions = Options.instance(paramContext);
/*  70 */       String str = localOptions.get(Option.TARGET);
/*  71 */       if (str != null) localTarget = lookup(str);
/*  72 */       if (localTarget == null) localTarget = DEFAULT;
/*  73 */       paramContext.put(targetKey, localTarget);
/*     */     }
/*  75 */     return localTarget;
/*     */   }
/*     */ 
/*     */   public static Target MIN() {
/*  79 */     return MIN;
/*     */   }
/*     */   public static Target MAX() {
/*  82 */     return MAX;
/*     */   }
/*     */ 
/*     */   private Target(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*  99 */     this.name = paramString;
/* 100 */     this.majorVersion = paramInt1;
/* 101 */     this.minorVersion = paramInt2;
/*     */   }
/*     */ 
/*     */   public static Target lookup(String paramString)
/*     */   {
/* 107 */     return (Target)tab.get(paramString);
/*     */   }
/*     */ 
/*     */   public boolean requiresIproxy()
/*     */   {
/* 115 */     return compareTo(JDK1_1) <= 0;
/*     */   }
/*     */ 
/*     */   public boolean initializeFieldsBeforeSuper()
/*     */   {
/* 125 */     return compareTo(JDK1_4) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean obeyBinaryCompatibility()
/*     */   {
/* 136 */     return compareTo(JDK1_2) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean arrayBinaryCompatibility()
/*     */   {
/* 145 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean interfaceFieldsBinaryCompatibility()
/*     */   {
/* 154 */     return compareTo(JDK1_2) > 0;
/*     */   }
/*     */ 
/*     */   public boolean interfaceObjectOverridesBinaryCompatibility()
/*     */   {
/* 164 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean usePrivateSyntheticFields()
/*     */   {
/* 174 */     return compareTo(JDK1_5) < 0;
/*     */   }
/*     */ 
/*     */   public boolean useInnerCacheClass()
/*     */   {
/* 183 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean generateCLDCStackmap()
/*     */   {
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean generateStackMapTable()
/*     */   {
/* 194 */     return compareTo(JDK1_6) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean isPackageInfoSynthetic()
/*     */   {
/* 200 */     return compareTo(JDK1_6) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean generateEmptyAfterBig()
/*     */   {
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean useStringBuilder()
/*     */   {
/* 214 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean useSyntheticFlag()
/*     */   {
/* 221 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean useEnumFlag() {
/* 224 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean useAnnotationFlag() {
/* 227 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean useVarargsFlag() {
/* 230 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */   public boolean useBridgeFlag() {
/* 233 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public char syntheticNameChar()
/*     */   {
/* 240 */     return '$';
/*     */   }
/*     */ 
/*     */   public boolean hasClassLiterals()
/*     */   {
/* 246 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean hasInvokedynamic()
/*     */   {
/* 252 */     return compareTo(JDK1_7) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean hasMethodHandles()
/*     */   {
/* 260 */     return hasInvokedynamic();
/*     */   }
/*     */ 
/*     */   public boolean classLiteralsNoInit()
/*     */   {
/* 268 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean hasInitCause()
/*     */   {
/* 275 */     return compareTo(JDK1_4) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean boxWithConstructors()
/*     */   {
/* 282 */     return compareTo(JDK1_5) < 0;
/*     */   }
/*     */ 
/*     */   public boolean hasIterable()
/*     */   {
/* 289 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean hasEnclosingMethodAttribute()
/*     */   {
/* 296 */     return compareTo(JDK1_5) >= 0;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  63 */     targetKey = new Context.Key();
/*     */ 
/*  78 */     MIN = values()[0];
/*     */ 
/*  81 */     MAX = values()[(values().length - 1)];
/*     */ 
/*  84 */     tab = new HashMap();
/*     */ 
/*  86 */     for (Target localTarget : values()) {
/*  87 */       tab.put(localTarget.name, localTarget);
/*     */     }
/*  89 */     tab.put("5", JDK1_5);
/*  90 */     tab.put("6", JDK1_6);
/*  91 */     tab.put("7", JDK1_7);
/*  92 */     tab.put("8", JDK1_8);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Target
 * JD-Core Version:    0.6.2
 */