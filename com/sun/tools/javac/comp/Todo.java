/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class Todo extends AbstractQueue<Env<AttrContext>>
/*     */ {
/*  46 */   protected static final Context.Key<Todo> todoKey = new Context.Key();
/*     */ 
/* 135 */   LinkedList<Env<AttrContext>> contents = new LinkedList();
/*     */   LinkedList<Queue<Env<AttrContext>>> contentsByFile;
/*     */   Map<JavaFileObject, FileQueue> fileMap;
/*     */ 
/*     */   public static Todo instance(Context paramContext)
/*     */   {
/*  51 */     Todo localTodo = (Todo)paramContext.get(todoKey);
/*  52 */     if (localTodo == null)
/*  53 */       localTodo = new Todo(paramContext);
/*  54 */     return localTodo;
/*     */   }
/*     */ 
/*     */   protected Todo(Context paramContext)
/*     */   {
/*  59 */     paramContext.put(todoKey, this);
/*     */   }
/*     */ 
/*     */   public void append(Env<AttrContext> paramEnv) {
/*  63 */     add(paramEnv);
/*     */   }
/*     */ 
/*     */   public Iterator<Env<AttrContext>> iterator()
/*     */   {
/*  68 */     return this.contents.iterator();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  73 */     return this.contents.size();
/*     */   }
/*     */ 
/*     */   public boolean offer(Env<AttrContext> paramEnv) {
/*  77 */     if (this.contents.add(paramEnv)) {
/*  78 */       if (this.contentsByFile != null)
/*  79 */         addByFile(paramEnv);
/*  80 */       return true;
/*     */     }
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> poll()
/*     */   {
/*  87 */     if (size() == 0)
/*  88 */       return null;
/*  89 */     Env localEnv = (Env)this.contents.remove(0);
/*  90 */     if (this.contentsByFile != null)
/*  91 */       removeByFile(localEnv);
/*  92 */     return localEnv;
/*     */   }
/*     */ 
/*     */   public Env<AttrContext> peek() {
/*  96 */     return size() == 0 ? null : (Env)this.contents.get(0);
/*     */   }
/*     */ 
/*     */   public Queue<Queue<Env<AttrContext>>> groupByFile() {
/* 100 */     if (this.contentsByFile == null) {
/* 101 */       this.contentsByFile = new LinkedList();
/* 102 */       for (Env localEnv : this.contents) {
/* 103 */         addByFile(localEnv);
/*     */       }
/*     */     }
/* 106 */     return this.contentsByFile;
/*     */   }
/*     */ 
/*     */   private void addByFile(Env<AttrContext> paramEnv) {
/* 110 */     JavaFileObject localJavaFileObject = paramEnv.toplevel.sourcefile;
/* 111 */     if (this.fileMap == null)
/* 112 */       this.fileMap = new HashMap();
/* 113 */     FileQueue localFileQueue = (FileQueue)this.fileMap.get(localJavaFileObject);
/* 114 */     if (localFileQueue == null) {
/* 115 */       localFileQueue = new FileQueue();
/* 116 */       this.fileMap.put(localJavaFileObject, localFileQueue);
/* 117 */       this.contentsByFile.add(localFileQueue);
/*     */     }
/* 119 */     localFileQueue.fileContents.add(paramEnv);
/*     */   }
/*     */ 
/*     */   private void removeByFile(Env<AttrContext> paramEnv) {
/* 123 */     JavaFileObject localJavaFileObject = paramEnv.toplevel.sourcefile;
/* 124 */     FileQueue localFileQueue = (FileQueue)this.fileMap.get(localJavaFileObject);
/* 125 */     if (localFileQueue == null)
/* 126 */       return;
/* 127 */     if ((localFileQueue.fileContents.remove(paramEnv)) && 
/* 128 */       (localFileQueue.isEmpty())) {
/* 129 */       this.fileMap.remove(localJavaFileObject);
/* 130 */       this.contentsByFile.remove(localFileQueue);
/*     */     }
/*     */   }
/*     */ 
/*     */   class FileQueue extends AbstractQueue<Env<AttrContext>>
/*     */   {
/* 170 */     LinkedList<Env<AttrContext>> fileContents = new LinkedList();
/*     */ 
/*     */     FileQueue()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<Env<AttrContext>> iterator()
/*     */     {
/* 142 */       return this.fileContents.iterator();
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 147 */       return this.fileContents.size();
/*     */     }
/*     */ 
/*     */     public boolean offer(Env<AttrContext> paramEnv) {
/* 151 */       if (this.fileContents.offer(paramEnv)) {
/* 152 */         Todo.this.contents.add(paramEnv);
/* 153 */         return true;
/*     */       }
/* 155 */       return false;
/*     */     }
/*     */ 
/*     */     public Env<AttrContext> poll() {
/* 159 */       if (this.fileContents.size() == 0)
/* 160 */         return null;
/* 161 */       Env localEnv = (Env)this.fileContents.remove(0);
/* 162 */       Todo.this.contents.remove(localEnv);
/* 163 */       return localEnv;
/*     */     }
/*     */ 
/*     */     public Env<AttrContext> peek() {
/* 167 */       return this.fileContents.size() == 0 ? null : (Env)this.fileContents.get(0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Todo
 * JD-Core Version:    0.6.2
 */