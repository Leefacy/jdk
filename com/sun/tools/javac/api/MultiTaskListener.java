/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class MultiTaskListener
/*     */   implements TaskListener
/*     */ {
/*  45 */   public static final Context.Key<MultiTaskListener> taskListenerKey = new Context.Key();
/*     */ 
/*  65 */   TaskListener[] listeners = new TaskListener[0];
/*     */   ClientCodeWrapper ccw;
/*     */ 
/*     */   public static MultiTaskListener instance(Context paramContext)
/*     */   {
/*  50 */     MultiTaskListener localMultiTaskListener = (MultiTaskListener)paramContext.get(taskListenerKey);
/*  51 */     if (localMultiTaskListener == null)
/*  52 */       localMultiTaskListener = new MultiTaskListener(paramContext);
/*  53 */     return localMultiTaskListener;
/*     */   }
/*     */ 
/*     */   protected MultiTaskListener(Context paramContext) {
/*  57 */     paramContext.put(taskListenerKey, this);
/*  58 */     this.ccw = ClientCodeWrapper.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public Collection<TaskListener> getTaskListeners()
/*     */   {
/*  70 */     return Arrays.asList(this.listeners);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  74 */     return this.listeners.length == 0;
/*     */   }
/*     */ 
/*     */   public void add(TaskListener paramTaskListener) {
/*  78 */     for (TaskListener localTaskListener : this.listeners) {
/*  79 */       if (this.ccw.unwrap(localTaskListener) == paramTaskListener)
/*  80 */         throw new IllegalStateException();
/*     */     }
/*  82 */     this.listeners = ((TaskListener[])Arrays.copyOf(this.listeners, this.listeners.length + 1));
/*  83 */     this.listeners[(this.listeners.length - 1)] = this.ccw.wrap(paramTaskListener);
/*     */   }
/*     */ 
/*     */   public void remove(TaskListener paramTaskListener) {
/*  87 */     for (int i = 0; i < this.listeners.length; i++)
/*  88 */       if (this.ccw.unwrap(this.listeners[i]) == paramTaskListener) {
/*  89 */         TaskListener[] arrayOfTaskListener = new TaskListener[this.listeners.length - 1];
/*  90 */         System.arraycopy(this.listeners, 0, arrayOfTaskListener, 0, i);
/*  91 */         System.arraycopy(this.listeners, i + 1, arrayOfTaskListener, i, arrayOfTaskListener.length - i);
/*  92 */         this.listeners = arrayOfTaskListener;
/*  93 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void started(TaskEvent paramTaskEvent)
/*     */   {
/* 101 */     TaskListener[] arrayOfTaskListener1 = this.listeners;
/* 102 */     for (TaskListener localTaskListener : arrayOfTaskListener1)
/* 103 */       localTaskListener.started(paramTaskEvent);
/*     */   }
/*     */ 
/*     */   public void finished(TaskEvent paramTaskEvent)
/*     */   {
/* 109 */     TaskListener[] arrayOfTaskListener1 = this.listeners;
/* 110 */     for (TaskListener localTaskListener : arrayOfTaskListener1)
/* 111 */       localTaskListener.finished(paramTaskEvent);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     return Arrays.toString(this.listeners);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.MultiTaskListener
 * JD-Core Version:    0.6.2
 */