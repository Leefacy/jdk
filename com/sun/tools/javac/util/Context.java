/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Context
/*     */ {
/* 122 */   private Map<Key<?>, Object> ht = new HashMap();
/*     */ 
/* 169 */   private Map<Key<?>, Factory<?>> ft = new HashMap();
/*     */ 
/* 180 */   private Map<Class<?>, Key<?>> kt = new HashMap();
/*     */ 
/*     */   public <T> void put(Key<T> paramKey, Factory<T> paramFactory)
/*     */   {
/* 126 */     checkState(this.ht);
/* 127 */     Object localObject = this.ht.put(paramKey, paramFactory);
/* 128 */     if (localObject != null)
/* 129 */       throw new AssertionError("duplicate context value");
/* 130 */     checkState(this.ft);
/* 131 */     this.ft.put(paramKey, paramFactory);
/*     */   }
/*     */ 
/*     */   public <T> void put(Key<T> paramKey, T paramT)
/*     */   {
/* 136 */     if ((paramT instanceof Factory))
/* 137 */       throw new AssertionError("T extends Context.Factory");
/* 138 */     checkState(this.ht);
/* 139 */     Object localObject = this.ht.put(paramKey, paramT);
/* 140 */     if ((localObject != null) && (!(localObject instanceof Factory)) && (localObject != paramT) && (paramT != null))
/* 141 */       throw new AssertionError("duplicate context value");
/*     */   }
/*     */ 
/*     */   public <T> T get(Key<T> paramKey)
/*     */   {
/* 146 */     checkState(this.ht);
/* 147 */     Object localObject = this.ht.get(paramKey);
/* 148 */     if ((localObject instanceof Factory)) {
/* 149 */       Factory localFactory = (Factory)localObject;
/* 150 */       localObject = localFactory.make(this);
/* 151 */       if ((localObject instanceof Factory))
/* 152 */         throw new AssertionError("T extends Context.Factory");
/* 153 */       Assert.check(this.ht.get(paramKey) == localObject);
/*     */     }
/*     */ 
/* 161 */     return uncheckedCast(localObject);
/*     */   }
/*     */ 
/*     */   public Context()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Context(Context paramContext)
/*     */   {
/* 172 */     this.kt.putAll(paramContext.kt);
/* 173 */     this.ft.putAll(paramContext.ft);
/* 174 */     this.ht.putAll(paramContext.ft);
/*     */   }
/*     */ 
/*     */   private <T> Key<T> key(Class<T> paramClass)
/*     */   {
/* 183 */     checkState(this.kt);
/* 184 */     Key localKey = (Key)uncheckedCast(this.kt.get(paramClass));
/* 185 */     if (localKey == null) {
/* 186 */       localKey = new Key();
/* 187 */       this.kt.put(paramClass, localKey);
/*     */     }
/* 189 */     return localKey;
/*     */   }
/*     */ 
/*     */   public <T> T get(Class<T> paramClass) {
/* 193 */     return get(key(paramClass));
/*     */   }
/*     */ 
/*     */   public <T> void put(Class<T> paramClass, T paramT) {
/* 197 */     put(key(paramClass), paramT);
/*     */   }
/*     */   public <T> void put(Class<T> paramClass, Factory<T> paramFactory) {
/* 200 */     put(key(paramClass), paramFactory);
/*     */   }
/*     */ 
/*     */   private static <T> T uncheckedCast(Object paramObject)
/*     */   {
/* 209 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public void dump() {
/* 213 */     for (Iterator localIterator = this.ht.values().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 214 */       System.err.println(localObject == null ? null : localObject.getClass()); }
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 218 */     this.ht = null;
/* 219 */     this.kt = null;
/* 220 */     this.ft = null;
/*     */   }
/*     */ 
/*     */   private static void checkState(Map<?, ?> paramMap) {
/* 224 */     if (paramMap == null)
/* 225 */       throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public static abstract interface Factory<T>
/*     */   {
/*     */     public abstract T make(Context paramContext);
/*     */   }
/*     */ 
/*     */   public static class Key<T>
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Context
 * JD-Core Version:    0.6.2
 */