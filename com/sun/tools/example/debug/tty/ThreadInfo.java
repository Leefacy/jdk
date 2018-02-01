/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ class ThreadInfo
/*     */ {
/*  48 */   private static List<ThreadInfo> threads = Collections.synchronizedList(new ArrayList());
/*  49 */   private static boolean gotInitialThreads = false;
/*     */ 
/*  51 */   private static ThreadInfo current = null;
/*  52 */   private static ThreadGroupReference group = null;
/*     */   private final ThreadReference thread;
/*  55 */   private int currentFrameIndex = 0;
/*     */ 
/*     */   private ThreadInfo(ThreadReference paramThreadReference) {
/*  58 */     this.thread = paramThreadReference;
/*  59 */     if (paramThreadReference == null)
/*  60 */       MessageOutput.fatalError("Internal error: null ThreadInfo created");
/*     */   }
/*     */ 
/*     */   private static void initThreads()
/*     */   {
/*  65 */     if (!gotInitialThreads) {
/*  66 */       for (ThreadReference localThreadReference : Env.vm().allThreads()) {
/*  67 */         threads.add(new ThreadInfo(localThreadReference));
/*     */       }
/*  69 */       gotInitialThreads = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void addThread(ThreadReference paramThreadReference) {
/*  74 */     synchronized (threads) {
/*  75 */       initThreads();
/*  76 */       ThreadInfo localThreadInfo = new ThreadInfo(paramThreadReference);
/*     */ 
/*  80 */       if (getThreadInfo(paramThreadReference) == null)
/*  81 */         threads.add(localThreadInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void removeThread(ThreadReference paramThreadReference)
/*     */   {
/*  87 */     if (paramThreadReference.equals(current))
/*     */     {
/*     */       String str;
/*     */       try
/*     */       {
/*  95 */         str = "\"" + paramThreadReference.name() + "\"";
/*     */       } catch (Exception localException) {
/*  97 */         str = "";
/*     */       }
/*     */ 
/* 100 */       setCurrentThread(null);
/*     */ 
/* 102 */       MessageOutput.println();
/* 103 */       MessageOutput.println("Current thread died. Execution continuing...", str);
/*     */     }
/*     */ 
/* 106 */     threads.remove(getThreadInfo(paramThreadReference));
/*     */   }
/*     */ 
/*     */   static List<ThreadInfo> threads() {
/* 110 */     synchronized (threads) {
/* 111 */       initThreads();
/*     */ 
/* 113 */       return new ArrayList(threads);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void invalidateAll() {
/* 118 */     current = null;
/* 119 */     group = null;
/* 120 */     synchronized (threads) {
/* 121 */       for (ThreadInfo localThreadInfo : threads())
/* 122 */         localThreadInfo.invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setThreadGroup(ThreadGroupReference paramThreadGroupReference)
/*     */   {
/* 128 */     group = paramThreadGroupReference;
/*     */   }
/*     */ 
/*     */   static void setCurrentThread(ThreadReference paramThreadReference) {
/* 132 */     if (paramThreadReference == null) {
/* 133 */       setCurrentThreadInfo(null);
/*     */     } else {
/* 135 */       ThreadInfo localThreadInfo = getThreadInfo(paramThreadReference);
/* 136 */       setCurrentThreadInfo(localThreadInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setCurrentThreadInfo(ThreadInfo paramThreadInfo) {
/* 141 */     current = paramThreadInfo;
/* 142 */     if (current != null)
/* 143 */       current.invalidate();
/*     */   }
/*     */ 
/*     */   static ThreadInfo getCurrentThreadInfo()
/*     */   {
/* 153 */     return current;
/*     */   }
/*     */ 
/*     */   ThreadReference getThread()
/*     */   {
/* 162 */     return this.thread;
/*     */   }
/*     */ 
/*     */   static ThreadGroupReference group() {
/* 166 */     if (group == null)
/*     */     {
/* 169 */       setThreadGroup((ThreadGroupReference)Env.vm().topLevelThreadGroups().get(0));
/*     */     }
/* 171 */     return group;
/*     */   }
/*     */ 
/*     */   static ThreadInfo getThreadInfo(long paramLong) {
/* 175 */     Object localObject1 = null;
/*     */ 
/* 177 */     synchronized (threads) {
/* 178 */       for (ThreadInfo localThreadInfo : threads()) {
/* 179 */         if (localThreadInfo.thread.uniqueID() == paramLong) {
/* 180 */           localObject1 = localThreadInfo;
/* 181 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 185 */     return localObject1;
/*     */   }
/*     */ 
/*     */   static ThreadInfo getThreadInfo(ThreadReference paramThreadReference) {
/* 189 */     return getThreadInfo(paramThreadReference.uniqueID());
/*     */   }
/*     */ 
/*     */   static ThreadInfo getThreadInfo(String paramString) {
/* 193 */     ThreadInfo localThreadInfo = null;
/* 194 */     if (paramString.startsWith("t@"))
/* 195 */       paramString = paramString.substring(2);
/*     */     try
/*     */     {
/* 198 */       long l = Long.decode(paramString).longValue();
/* 199 */       localThreadInfo = getThreadInfo(l);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 201 */       localThreadInfo = null;
/*     */     }
/* 203 */     return localThreadInfo;
/*     */   }
/*     */ 
/*     */   List<StackFrame> getStack()
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 212 */     return this.thread.frames();
/*     */   }
/*     */ 
/*     */   StackFrame getCurrentFrame()
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 221 */     if (this.thread.frameCount() == 0) {
/* 222 */       return null;
/*     */     }
/* 224 */     return this.thread.frame(this.currentFrameIndex);
/*     */   }
/*     */ 
/*     */   void invalidate()
/*     */   {
/* 231 */     this.currentFrameIndex = 0;
/*     */   }
/*     */ 
/*     */   private void assureSuspended() throws IncompatibleThreadStateException
/*     */   {
/* 236 */     if (!this.thread.isSuspended())
/* 237 */       throw new IncompatibleThreadStateException();
/*     */   }
/*     */ 
/*     */   int getCurrentFrameIndex()
/*     */   {
/* 248 */     return this.currentFrameIndex;
/*     */   }
/*     */ 
/*     */   void setCurrentFrameIndex(int paramInt)
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 262 */     assureSuspended();
/* 263 */     if ((paramInt < 0) || (paramInt >= this.thread.frameCount())) {
/* 264 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 266 */     this.currentFrameIndex = paramInt;
/*     */   }
/*     */ 
/*     */   void up(int paramInt)
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 280 */     setCurrentFrameIndex(this.currentFrameIndex + paramInt);
/*     */   }
/*     */ 
/*     */   void down(int paramInt)
/*     */     throws IncompatibleThreadStateException
/*     */   {
/* 293 */     setCurrentFrameIndex(this.currentFrameIndex - paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.ThreadInfo
 * JD-Core Version:    0.6.2
 */