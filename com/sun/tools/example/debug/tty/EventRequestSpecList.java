/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ class EventRequestSpecList
/*     */ {
/*     */   private static final int statusResolved = 1;
/*     */   private static final int statusUnresolved = 2;
/*     */   private static final int statusError = 3;
/*  51 */   private List<EventRequestSpec> eventRequestSpecs = Collections.synchronizedList(new ArrayList());
/*     */ 
/*     */   boolean resolve(ClassPrepareEvent paramClassPrepareEvent)
/*     */   {
/*  62 */     int i = 0;
/*  63 */     synchronized (this.eventRequestSpecs) {
/*  64 */       for (EventRequestSpec localEventRequestSpec : this.eventRequestSpecs) {
/*  65 */         if (!localEventRequestSpec.isResolved()) {
/*     */           try {
/*  67 */             EventRequest localEventRequest = localEventRequestSpec.resolve(paramClassPrepareEvent);
/*  68 */             if (localEventRequest != null)
/*  69 */               MessageOutput.println("Set deferred", localEventRequestSpec.toString());
/*     */           }
/*     */           catch (Exception localException) {
/*  72 */             MessageOutput.println("Unable to set deferred", new Object[] { localEventRequestSpec
/*  73 */               .toString(), localEventRequestSpec
/*  74 */               .errorMessageFor(localException) });
/*     */ 
/*  75 */             i = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  80 */     return i == 0;
/*     */   }
/*     */ 
/*     */   void resolveAll() {
/*  84 */     for (EventRequestSpec localEventRequestSpec : this.eventRequestSpecs)
/*     */       try {
/*  86 */         EventRequest localEventRequest = localEventRequestSpec.resolveEagerly();
/*  87 */         if (localEventRequest != null)
/*  88 */           MessageOutput.println("Set deferred", localEventRequestSpec.toString());
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   boolean addEagerlyResolve(EventRequestSpec paramEventRequestSpec) {
/*     */     try {
/*  97 */       this.eventRequestSpecs.add(paramEventRequestSpec);
/*  98 */       EventRequest localEventRequest = paramEventRequestSpec.resolveEagerly();
/*  99 */       if (localEventRequest != null) {
/* 100 */         MessageOutput.println("Set", paramEventRequestSpec.toString());
/*     */       }
/* 102 */       return true;
/*     */     } catch (Exception localException) {
/* 104 */       MessageOutput.println("Unable to set", new Object[] { paramEventRequestSpec
/* 105 */         .toString(), paramEventRequestSpec
/* 106 */         .errorMessageFor(localException) });
/*     */     }
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   BreakpointSpec createBreakpoint(String paramString, int paramInt)
/*     */     throws ClassNotFoundException
/*     */   {
/* 113 */     PatternReferenceTypeSpec localPatternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString);
/*     */ 
/* 115 */     return new BreakpointSpec(localPatternReferenceTypeSpec, paramInt);
/*     */   }
/*     */ 
/*     */   BreakpointSpec createBreakpoint(String paramString1, String paramString2, List<String> paramList)
/*     */     throws MalformedMemberNameException, ClassNotFoundException
/*     */   {
/* 123 */     PatternReferenceTypeSpec localPatternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */ 
/* 125 */     return new BreakpointSpec(localPatternReferenceTypeSpec, paramString2, paramList);
/*     */   }
/*     */ 
/*     */   EventRequestSpec createExceptionCatch(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws ClassNotFoundException
/*     */   {
/* 132 */     PatternReferenceTypeSpec localPatternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString);
/*     */ 
/* 134 */     return new ExceptionSpec(localPatternReferenceTypeSpec, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   WatchpointSpec createAccessWatchpoint(String paramString1, String paramString2)
/*     */     throws MalformedMemberNameException, ClassNotFoundException
/*     */   {
/* 141 */     PatternReferenceTypeSpec localPatternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */ 
/* 143 */     return new AccessWatchpointSpec(localPatternReferenceTypeSpec, paramString2);
/*     */   }
/*     */ 
/*     */   WatchpointSpec createModificationWatchpoint(String paramString1, String paramString2)
/*     */     throws MalformedMemberNameException, ClassNotFoundException
/*     */   {
/* 150 */     PatternReferenceTypeSpec localPatternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */ 
/* 152 */     return new ModificationWatchpointSpec(localPatternReferenceTypeSpec, paramString2);
/*     */   }
/*     */ 
/*     */   boolean delete(EventRequestSpec paramEventRequestSpec) {
/* 156 */     synchronized (this.eventRequestSpecs) {
/* 157 */       int i = this.eventRequestSpecs.indexOf(paramEventRequestSpec);
/* 158 */       if (i != -1) {
/* 159 */         EventRequestSpec localEventRequestSpec = (EventRequestSpec)this.eventRequestSpecs.get(i);
/* 160 */         localEventRequestSpec.remove();
/* 161 */         this.eventRequestSpecs.remove(i);
/* 162 */         return true;
/*     */       }
/* 164 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   List<EventRequestSpec> eventRequestSpecs()
/*     */   {
/* 171 */     synchronized (this.eventRequestSpecs) {
/* 172 */       return new ArrayList(this.eventRequestSpecs);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.EventRequestSpecList
 * JD-Core Version:    0.6.2
 */