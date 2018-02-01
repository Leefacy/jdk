/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ServiceLoader<S>
/*     */   implements Iterable<S>
/*     */ {
/*     */   private static final String PREFIX = "META-INF/services/";
/*     */   private Class<S> service;
/*     */   private ClassLoader loader;
/*  73 */   private LinkedHashMap<String, S> providers = new LinkedHashMap();
/*     */   private ServiceLoader<S>.LazyIterator lookupIterator;
/*     */ 
/*     */   public void reload()
/*     */   {
/*  90 */     this.providers.clear();
/*  91 */     this.lookupIterator = new LazyIterator(this.service, this.loader, null);
/*     */   }
/*     */ 
/*     */   private ServiceLoader(Class<S> paramClass, ClassLoader paramClassLoader) {
/*  95 */     this.service = ((Class)Objects.requireNonNull(paramClass, "Service interface cannot be null"));
/*  96 */     this.loader = (paramClassLoader == null ? ClassLoader.getSystemClassLoader() : paramClassLoader);
/*  97 */     reload();
/*     */   }
/*     */ 
/*     */   private static void fail(Class<?> paramClass, String paramString, Throwable paramThrowable)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 103 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   private static void fail(Class<?> paramClass, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 110 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static void fail(Class<?> paramClass, URL paramURL, int paramInt, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 116 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private int parseLine(Class<?> paramClass, URL paramURL, BufferedReader paramBufferedReader, int paramInt, List<String> paramList)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 126 */     String str = paramBufferedReader.readLine();
/* 127 */     if (str == null) {
/* 128 */       return -1;
/*     */     }
/* 130 */     int i = str.indexOf('#');
/* 131 */     if (i >= 0) str = str.substring(0, i);
/* 132 */     str = str.trim();
/* 133 */     int j = str.length();
/* 134 */     if (j != 0) {
/* 135 */       if ((str.indexOf(' ') >= 0) || (str.indexOf('\t') >= 0))
/* 136 */         fail(paramClass, paramURL, paramInt, "Illegal configuration-file syntax");
/* 137 */       int k = str.codePointAt(0);
/* 138 */       if (!Character.isJavaIdentifierStart(k))
/* 139 */         fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/* 140 */       for (int m = Character.charCount(k); m < j; m += Character.charCount(k)) {
/* 141 */         k = str.codePointAt(m);
/* 142 */         if ((!Character.isJavaIdentifierPart(k)) && (k != 46))
/* 143 */           fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/*     */       }
/* 145 */       if ((!this.providers.containsKey(str)) && (!paramList.contains(str)))
/* 146 */         paramList.add(str);
/*     */     }
/* 148 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   private Iterator<String> parse(Class<?> paramClass, URL paramURL)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 171 */     InputStream localInputStream = null;
/* 172 */     BufferedReader localBufferedReader = null;
/* 173 */     ArrayList localArrayList = new ArrayList();
/*     */     try
/*     */     {
/* 185 */       URLConnection localURLConnection = paramURL.openConnection();
/* 186 */       localURLConnection.setUseCaches(false);
/* 187 */       localInputStream = localURLConnection.getInputStream();
/*     */ 
/* 189 */       localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
/* 190 */       int i = 1;
/* 191 */       while ((i = parseLine(paramClass, paramURL, localBufferedReader, i, localArrayList)) >= 0);
/*     */     }
/*     */     catch (IOException localIOException2)
/*     */     {
/* 193 */       fail(paramClass, "Error reading configuration file", localIOException2);
/*     */     } finally {
/*     */       try {
/* 196 */         if (localBufferedReader != null) localBufferedReader.close();
/* 197 */         if (localInputStream != null) localInputStream.close(); 
/*     */       }
/* 199 */       catch (IOException localIOException4) { fail(paramClass, "Error closing configuration file", localIOException4); }
/*     */ 
/*     */     }
/* 202 */     return localArrayList.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<S> iterator()
/*     */   {
/* 323 */     return new Iterator()
/*     */     {
/* 326 */       Iterator<Map.Entry<String, S>> knownProviders = ServiceLoader.this.providers
/* 326 */         .entrySet().iterator();
/*     */ 
/*     */       public boolean hasNext() {
/* 329 */         if (this.knownProviders.hasNext())
/* 330 */           return true;
/* 331 */         return ServiceLoader.this.lookupIterator.hasNext();
/*     */       }
/*     */ 
/*     */       public S next() {
/* 335 */         if (this.knownProviders.hasNext())
/* 336 */           return ((Map.Entry)this.knownProviders.next()).getValue();
/* 337 */         return ServiceLoader.this.lookupIterator.next();
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 341 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass, ClassLoader paramClassLoader)
/*     */   {
/* 365 */     return new ServiceLoader(paramClass, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass)
/*     */   {
/* 390 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 391 */     return load(paramClass, localClassLoader);
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> loadInstalled(Class<S> paramClass)
/*     */   {
/* 419 */     ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
/* 420 */     ClassLoader localClassLoader2 = null;
/* 421 */     while (localClassLoader1 != null) {
/* 422 */       localClassLoader2 = localClassLoader1;
/* 423 */       localClassLoader1 = localClassLoader1.getParent();
/*     */     }
/* 425 */     return load(paramClass, localClassLoader2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 434 */     return "java.util.ServiceLoader[" + this.service.getName() + "]";
/*     */   }
/*     */ 
/*     */   private class LazyIterator
/*     */     implements Iterator<S>
/*     */   {
/*     */     Class<S> service;
/*     */     ClassLoader loader;
/* 213 */     Enumeration<URL> configs = null;
/* 214 */     Iterator<String> pending = null;
/* 215 */     String nextName = null;
/*     */ 
/*     */     private LazyIterator(ClassLoader arg2)
/*     */     {
/*     */       Object localObject1;
/* 218 */       this.service = localObject1;
/*     */       Object localObject2;
/* 219 */       this.loader = localObject2;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 223 */       if (this.nextName != null) {
/* 224 */         return true;
/*     */       }
/* 226 */       if (this.configs == null) {
/*     */         try {
/* 228 */           String str = "META-INF/services/" + this.service.getName();
/* 229 */           if (this.loader == null)
/* 230 */             this.configs = ClassLoader.getSystemResources(str);
/*     */           else
/* 232 */             this.configs = this.loader.getResources(str);
/*     */         } catch (IOException localIOException) {
/* 234 */           ServiceLoader.fail(this.service, "Error locating configuration files", localIOException);
/*     */         }
/*     */       }
/* 237 */       while ((this.pending == null) || (!this.pending.hasNext())) {
/* 238 */         if (!this.configs.hasMoreElements()) {
/* 239 */           return false;
/*     */         }
/* 241 */         this.pending = ServiceLoader.this.parse(this.service, (URL)this.configs.nextElement());
/*     */       }
/* 243 */       this.nextName = ((String)this.pending.next());
/* 244 */       return true;
/*     */     }
/*     */ 
/*     */     public S next() {
/* 248 */       if (!hasNext()) {
/* 249 */         throw new NoSuchElementException();
/*     */       }
/* 251 */       String str = this.nextName;
/* 252 */       this.nextName = null;
/* 253 */       Class localClass = null;
/*     */       try {
/* 255 */         localClass = Class.forName(str, false, this.loader);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 257 */         ServiceLoader.fail(this.service, "Provider " + str + " not found");
/*     */       }
/*     */ 
/* 260 */       if (!this.service.isAssignableFrom(localClass)) {
/* 261 */         ServiceLoader.fail(this.service, "Provider " + str + " not a subtype");
/*     */       }
/*     */       try
/*     */       {
/* 265 */         Object localObject = this.service.cast(localClass.newInstance());
/* 266 */         ServiceLoader.this.providers.put(str, localObject);
/* 267 */         return localObject;
/*     */       } catch (Throwable localThrowable) {
/* 269 */         ServiceLoader.fail(this.service, "Provider " + str + " could not be instantiated: " + localThrowable, localThrowable);
/*     */       }
/*     */ 
/* 273 */       throw new Error();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 277 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.ServiceLoader
 * JD-Core Version:    0.6.2
 */