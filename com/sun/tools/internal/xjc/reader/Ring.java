/*     */ package com.sun.tools.internal.xjc.reader;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class Ring
/*     */ {
/*  66 */   private final Map<Class, Object> components = new HashMap();
/*     */ 
/*  68 */   private static final ThreadLocal<Ring> instances = new ThreadLocal();
/*     */ 
/*     */   public static <T> void add(Class<T> clazz, T instance)
/*     */   {
/*  73 */     assert (!get().components.containsKey(clazz));
/*  74 */     get().components.put(clazz, instance);
/*     */   }
/*     */ 
/*     */   public static <T> void add(T o) {
/*  78 */     add(o.getClass(), o);
/*     */   }
/*     */ 
/*     */   public static <T> T get(Class<T> key) {
/*  82 */     Object t = get().components.get(key);
/*  83 */     if (t == null) {
/*     */       try {
/*  85 */         Constructor c = key.getDeclaredConstructor(new Class[0]);
/*  86 */         c.setAccessible(true);
/*  87 */         t = c.newInstance(new Object[0]);
/*  88 */         if (!get().components.containsKey(key))
/*     */         {
/*  90 */           add(key, t);
/*     */         }
/*     */       } catch (InstantiationException e) { throw new Error(e);
/*     */       } catch (IllegalAccessException e) {
/*  94 */         throw new Error(e);
/*     */       } catch (NoSuchMethodException e) {
/*  96 */         throw new Error(e);
/*     */       } catch (InvocationTargetException e) {
/*  98 */         throw new Error(e);
/*     */       }
/*     */     }
/*     */ 
/* 102 */     assert (t != null);
/* 103 */     return t;
/*     */   }
/*     */ 
/*     */   public static Ring get()
/*     */   {
/* 110 */     return (Ring)instances.get();
/*     */   }
/*     */ 
/*     */   public static Ring begin()
/*     */   {
/* 117 */     Ring r = null;
/* 118 */     synchronized (instances) {
/* 119 */       r = (Ring)instances.get();
/* 120 */       instances.set(new Ring());
/*     */     }
/* 122 */     return r;
/*     */   }
/*     */ 
/*     */   public static void end(Ring old)
/*     */   {
/* 129 */     synchronized (instances) {
/* 130 */       instances.remove();
/* 131 */       instances.set(old);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.Ring
 * JD-Core Version:    0.6.2
 */