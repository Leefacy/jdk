/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public enum Profile
/*     */ {
/*  42 */   COMPACT1("compact1", 1, Target.JDK1_8, new Target[0]), 
/*  43 */   COMPACT2("compact2", 2, Target.JDK1_8, new Target[0]), 
/*  44 */   COMPACT3("compact3", 3, Target.JDK1_8, new Target[0]), 
/*     */ 
/*  46 */   DEFAULT;
/*     */ 
/*  53 */   private static final Context.Key<Profile> profileKey = new Context.Key();
/*     */   public final String name;
/*     */   public final int value;
/*     */   final Set<Target> targets;
/*     */ 
/*     */   public static Profile instance(Context paramContext) {
/*  57 */     Profile localProfile = (Profile)paramContext.get(profileKey);
/*  58 */     if (localProfile == null) {
/*  59 */       Options localOptions = Options.instance(paramContext);
/*  60 */       String str = localOptions.get(Option.PROFILE);
/*  61 */       if (str != null) localProfile = lookup(str);
/*  62 */       if (localProfile == null) localProfile = DEFAULT;
/*  63 */       paramContext.put(profileKey, localProfile);
/*     */     }
/*  65 */     return localProfile;
/*     */   }
/*     */ 
/*     */   private Profile()
/*     */   {
/*  73 */     this.name = null;
/*  74 */     this.value = 2147483647;
/*  75 */     this.targets = null;
/*     */   }
/*     */ 
/*     */   private Profile(String paramString, int paramInt, Target paramTarget, Target[] paramArrayOfTarget) {
/*  79 */     this.name = paramString;
/*  80 */     this.value = paramInt;
/*  81 */     this.targets = EnumSet.of(paramTarget, paramArrayOfTarget);
/*     */   }
/*     */ 
/*     */   public static Profile lookup(String paramString)
/*     */   {
/*  86 */     for (Profile localProfile : values()) {
/*  87 */       if (paramString.equals(localProfile.name))
/*  88 */         return localProfile;
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public static Profile lookup(int paramInt)
/*     */   {
/*  95 */     for (Profile localProfile : values()) {
/*  96 */       if (paramInt == localProfile.value)
/*  97 */         return localProfile;
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isValid(Target paramTarget) {
/* 103 */     return this.targets.contains(paramTarget);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Profile
 * JD-Core Version:    0.6.2
 */