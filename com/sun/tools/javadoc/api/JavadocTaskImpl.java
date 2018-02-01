/*    */ package com.sun.tools.javadoc.api;
/*    */ 
/*    */ import com.sun.tools.javac.util.ClientCodeException;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javadoc.Start;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import javax.tools.DocumentationTool.DocumentationTask;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ public class JavadocTaskImpl
/*    */   implements DocumentationTool.DocumentationTask
/*    */ {
/* 48 */   private final AtomicBoolean used = new AtomicBoolean();
/*    */   private final Context context;
/*    */   private Class<?> docletClass;
/*    */   private Iterable<String> options;
/*    */   private Iterable<? extends JavaFileObject> fileObjects;
/*    */   private Locale locale;
/*    */ 
/*    */   public JavadocTaskImpl(Context paramContext, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1)
/*    */   {
/* 58 */     this.context = paramContext;
/* 59 */     this.docletClass = paramClass;
/*    */ 
/* 61 */     this.options = (paramIterable == null ? Collections.emptySet() : 
/* 62 */       nullCheck(paramIterable));
/*    */ 
/* 63 */     this.fileObjects = (paramIterable1 == null ? Collections.emptySet() : 
/* 64 */       nullCheck(paramIterable1));
/*    */ 
/* 65 */     setLocale(Locale.getDefault());
/*    */   }
/*    */ 
/*    */   public void setLocale(Locale paramLocale) {
/* 69 */     if (this.used.get())
/* 70 */       throw new IllegalStateException();
/* 71 */     this.locale = paramLocale;
/*    */   }
/*    */ 
/*    */   public Boolean call() {
/* 75 */     if (!this.used.getAndSet(true)) {
/* 76 */       initContext();
/* 77 */       Start localStart = new Start(this.context);
/*    */       try {
/* 79 */         return Boolean.valueOf(localStart.begin(this.docletClass, this.options, this.fileObjects));
/*    */       } catch (ClientCodeException localClientCodeException) {
/* 81 */         throw new RuntimeException(localClientCodeException.getCause());
/*    */       }
/*    */     }
/* 84 */     throw new IllegalStateException("multiple calls to method 'call'");
/*    */   }
/*    */ 
/*    */   private void initContext()
/*    */   {
/* 90 */     this.context.put(Locale.class, this.locale);
/*    */   }
/*    */ 
/*    */   private static <T> Iterable<T> nullCheck(Iterable<T> paramIterable) {
/* 94 */     for (Iterator localIterator = paramIterable.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 95 */       if (localObject == null)
/* 96 */         throw new NullPointerException();
/*    */     }
/* 98 */     return paramIterable;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.api.JavadocTaskImpl
 * JD-Core Version:    0.6.2
 */