/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ThreadGroupReferenceImpl extends ObjectReferenceImpl
/*     */   implements ThreadGroupReference, VMListener
/*     */ {
/*     */   String name;
/*     */   ThreadGroupReference parent;
/*     */   boolean triedParent;
/*     */ 
/*     */   protected ObjectReferenceImpl.Cache newCache()
/*     */   {
/*  45 */     return new Cache(null);
/*     */   }
/*     */ 
/*     */   ThreadGroupReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  49 */     super(paramVirtualMachine, paramLong);
/*  50 */     this.vm.state().addListener(this);
/*     */   }
/*     */ 
/*     */   protected String description() {
/*  54 */     return "ThreadGroupReference " + uniqueID();
/*     */   }
/*     */ 
/*     */   public String name() {
/*  58 */     if (this.name == null)
/*     */     {
/*     */       try
/*     */       {
/*  63 */         this.name = 
/*  64 */           JDWP.ThreadGroupReference.Name.process(this.vm, this).groupName;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/*  66 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */   public ThreadGroupReference parent() {
/*  73 */     if (!this.triedParent)
/*     */     {
/*     */       try
/*     */       {
/*  78 */         this.parent = 
/*  79 */           JDWP.ThreadGroupReference.Parent.process(this.vm, this).parentGroup;
/*     */ 
/*  80 */         this.triedParent = true;
/*     */       } catch (JDWPException localJDWPException) {
/*  82 */         throw localJDWPException.toJDIException();
/*     */       }
/*     */     }
/*  85 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void suspend() {
/*  89 */     for (Iterator localIterator = threads().iterator(); localIterator.hasNext(); ) { localObject = (ThreadReference)localIterator.next();
/*  90 */       ((ThreadReference)localObject).suspend();
/*     */     }
/*  93 */     Object localObject;
/*  93 */     for (localIterator = threadGroups().iterator(); localIterator.hasNext(); ) { localObject = (VMListener)localIterator.next();
/*  94 */       ((VMListener)localObject).suspend(); }
/*     */   }
/*     */ 
/*     */   public void resume()
/*     */   {
/*  99 */     for (Iterator localIterator = threads().iterator(); localIterator.hasNext(); ) { localObject = (ThreadReference)localIterator.next();
/* 100 */       ((ThreadReference)localObject).resume();
/*     */     }
/* 103 */     Object localObject;
/* 103 */     for (localIterator = threadGroups().iterator(); localIterator.hasNext(); ) { localObject = (VMListener)localIterator.next();
/* 104 */       ((VMListener)localObject).resume(); }
/*     */   }
/*     */ 
/*     */   private JDWP.ThreadGroupReference.Children kids()
/*     */   {
/* 109 */     JDWP.ThreadGroupReference.Children localChildren = null;
/*     */     try {
/* 111 */       Cache localCache = (Cache)getCache();
/*     */ 
/* 113 */       if (localCache != null) {
/* 114 */         localChildren = localCache.kids;
/*     */       }
/* 116 */       if (localChildren == null)
/*     */       {
/* 118 */         localChildren = JDWP.ThreadGroupReference.Children.process(this.vm, this);
/*     */ 
/* 119 */         if (localCache != null) {
/* 120 */           localCache.kids = localChildren;
/* 121 */           if ((this.vm.traceFlags & 0x10) != 0)
/* 122 */             this.vm.printTrace(description() + " temporarily caching children ");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JDWPException localJDWPException)
/*     */     {
/* 128 */       throw localJDWPException.toJDIException();
/*     */     }
/* 130 */     return localChildren;
/*     */   }
/*     */ 
/*     */   public List<ThreadReference> threads() {
/* 134 */     return Arrays.asList((ThreadReference[])kids().childThreads);
/*     */   }
/*     */ 
/*     */   public List<ThreadGroupReference> threadGroups() {
/* 138 */     return Arrays.asList((ThreadGroupReference[])kids().childGroups);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     return "instance of " + referenceType().name() + "(name='" + 
/* 143 */       name() + "', " + "id=" + uniqueID() + ")";
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 147 */     return 103;
/*     */   }
/*     */ 
/*     */   private static class Cache extends ObjectReferenceImpl.Cache
/*     */   {
/*  41 */     JDWP.ThreadGroupReference.Children kids = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ThreadGroupReferenceImpl
 * JD-Core Version:    0.6.2
 */