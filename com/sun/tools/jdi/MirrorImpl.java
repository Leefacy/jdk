/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.VMMismatchException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ abstract class MirrorImpl
/*     */   implements Mirror
/*     */ {
/*     */   protected VirtualMachineImpl vm;
/*     */ 
/*     */   MirrorImpl(VirtualMachine paramVirtualMachine)
/*     */   {
/*  42 */     this.vm = ((VirtualMachineImpl)paramVirtualMachine);
/*     */   }
/*     */ 
/*     */   public VirtualMachine virtualMachine() {
/*  46 */     return this.vm;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  50 */     if ((paramObject != null) && ((paramObject instanceof Mirror))) {
/*  51 */       Mirror localMirror = (Mirror)paramObject;
/*  52 */       return this.vm.equals(localMirror.virtualMachine());
/*     */     }
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  59 */     return this.vm.hashCode();
/*     */   }
/*     */ 
/*     */   void validateMirror(Mirror paramMirror)
/*     */   {
/*  67 */     if (!this.vm.equals(paramMirror.virtualMachine()))
/*  68 */       throw new VMMismatchException(paramMirror.toString());
/*     */   }
/*     */ 
/*     */   void validateMirrorOrNull(Mirror paramMirror)
/*     */   {
/*  77 */     if ((paramMirror != null) && (!this.vm.equals(paramMirror.virtualMachine())))
/*  78 */       throw new VMMismatchException(paramMirror.toString());
/*     */   }
/*     */ 
/*     */   void validateMirrors(Collection<? extends Mirror> paramCollection)
/*     */   {
/*  87 */     Iterator localIterator = paramCollection.iterator();
/*  88 */     while (localIterator.hasNext()) {
/*  89 */       MirrorImpl localMirrorImpl = (MirrorImpl)localIterator.next();
/*  90 */       if (!this.vm.equals(localMirrorImpl.vm))
/*  91 */         throw new VMMismatchException(localMirrorImpl.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   void validateMirrorsOrNulls(Collection<? extends Mirror> paramCollection)
/*     */   {
/* 100 */     Iterator localIterator = paramCollection.iterator();
/* 101 */     while (localIterator.hasNext()) {
/* 102 */       MirrorImpl localMirrorImpl = (MirrorImpl)localIterator.next();
/* 103 */       if ((localMirrorImpl != null) && (!this.vm.equals(localMirrorImpl.vm)))
/* 104 */         throw new VMMismatchException(localMirrorImpl.toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.MirrorImpl
 * JD-Core Version:    0.6.2
 */