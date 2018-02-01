/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Options
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*  45 */   public static final Context.Key<Options> optionsKey = new Context.Key();
/*     */   private LinkedHashMap<String, String> values;
/* 162 */   private List<Runnable> listeners = List.nil();
/*     */ 
/*     */   public static Options instance(Context paramContext)
/*     */   {
/*  52 */     Options localOptions = (Options)paramContext.get(optionsKey);
/*  53 */     if (localOptions == null)
/*  54 */       localOptions = new Options(paramContext);
/*  55 */     return localOptions;
/*     */   }
/*     */ 
/*     */   protected Options(Context paramContext)
/*     */   {
/*  60 */     this.values = new LinkedHashMap();
/*  61 */     paramContext.put(optionsKey, this);
/*     */   }
/*     */ 
/*     */   public String get(String paramString)
/*     */   {
/*  68 */     return (String)this.values.get(paramString);
/*     */   }
/*     */ 
/*     */   public String get(Option paramOption)
/*     */   {
/*  75 */     return (String)this.values.get(paramOption.text);
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(String paramString)
/*     */   {
/*  83 */     return getBoolean(paramString, false);
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(String paramString, boolean paramBoolean)
/*     */   {
/*  90 */     String str = get(paramString);
/*  91 */     return str == null ? paramBoolean : Boolean.parseBoolean(str);
/*     */   }
/*     */ 
/*     */   public boolean isSet(String paramString)
/*     */   {
/*  98 */     return this.values.get(paramString) != null;
/*     */   }
/*     */ 
/*     */   public boolean isSet(Option paramOption)
/*     */   {
/* 105 */     return this.values.get(paramOption.text) != null;
/*     */   }
/*     */ 
/*     */   public boolean isSet(Option paramOption, String paramString)
/*     */   {
/* 112 */     return this.values.get(paramOption.text + paramString) != null;
/*     */   }
/*     */ 
/*     */   public boolean isUnset(String paramString)
/*     */   {
/* 119 */     return this.values.get(paramString) == null;
/*     */   }
/*     */ 
/*     */   public boolean isUnset(Option paramOption)
/*     */   {
/* 126 */     return this.values.get(paramOption.text) == null;
/*     */   }
/*     */ 
/*     */   public boolean isUnset(Option paramOption, String paramString)
/*     */   {
/* 133 */     return this.values.get(paramOption.text + paramString) == null;
/*     */   }
/*     */ 
/*     */   public void put(String paramString1, String paramString2) {
/* 137 */     this.values.put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void put(Option paramOption, String paramString) {
/* 141 */     this.values.put(paramOption.text, paramString);
/*     */   }
/*     */ 
/*     */   public void putAll(Options paramOptions) {
/* 145 */     this.values.putAll(paramOptions.values);
/*     */   }
/*     */ 
/*     */   public void remove(String paramString) {
/* 149 */     this.values.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Set<String> keySet() {
/* 153 */     return this.values.keySet();
/*     */   }
/*     */ 
/*     */   public int size() {
/* 157 */     return this.values.size();
/*     */   }
/*     */ 
/*     */   public void addListener(Runnable paramRunnable)
/*     */   {
/* 165 */     this.listeners = this.listeners.prepend(paramRunnable);
/*     */   }
/*     */ 
/*     */   public void notifyListeners() {
/* 169 */     for (Runnable localRunnable : this.listeners)
/* 170 */       localRunnable.run();
/*     */   }
/*     */ 
/*     */   public boolean lint(String paramString)
/*     */   {
/* 181 */     return (isSet(Option.XLINT_CUSTOM, paramString)) || 
/* 179 */       (
/* 180 */       ((isSet(Option.XLINT)) || 
/* 180 */       (isSet(Option.XLINT_CUSTOM, "all"))) && 
/* 181 */       (isUnset(Option.XLINT_CUSTOM, "-" + paramString)));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Options
 * JD-Core Version:    0.6.2
 */