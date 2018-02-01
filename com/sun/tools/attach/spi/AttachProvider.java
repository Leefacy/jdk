/*     */ package com.sun.tools.attach.spi;
/*     */ 
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachPermission;
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import com.sun.tools.attach.VirtualMachineDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public abstract class AttachProvider
/*     */ {
/*  80 */   private static final Object lock = new Object();
/*  81 */   private static List<AttachProvider> providers = null;
/*     */ 
/*     */   protected AttachProvider()
/*     */   {
/*  92 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  93 */     if (localSecurityManager != null)
/*  94 */       localSecurityManager.checkPermission(new AttachPermission("createAttachProvider"));
/*     */   }
/*     */ 
/*     */   public abstract String name();
/*     */ 
/*     */   public abstract String type();
/*     */ 
/*     */   public abstract VirtualMachine attachVirtualMachine(String paramString)
/*     */     throws AttachNotSupportedException, IOException;
/*     */ 
/*     */   public VirtualMachine attachVirtualMachine(VirtualMachineDescriptor paramVirtualMachineDescriptor)
/*     */     throws AttachNotSupportedException, IOException
/*     */   {
/* 191 */     if (paramVirtualMachineDescriptor.provider() != this) {
/* 192 */       throw new AttachNotSupportedException("provider mismatch");
/*     */     }
/* 194 */     return attachVirtualMachine(paramVirtualMachineDescriptor.id());
/*     */   }
/*     */ 
/*     */   public abstract List<VirtualMachineDescriptor> listVirtualMachines();
/*     */ 
/*     */   public static List<AttachProvider> providers()
/*     */   {
/* 248 */     synchronized (lock) {
/* 249 */       if (providers == null) {
/* 250 */         providers = new ArrayList();
/*     */ 
/* 253 */         ServiceLoader localServiceLoader = ServiceLoader.load(AttachProvider.class, AttachProvider.class
/* 254 */           .getClassLoader());
/*     */ 
/* 256 */         Iterator localIterator = localServiceLoader.iterator();
/*     */ 
/* 258 */         while (localIterator.hasNext()) {
/*     */           try {
/* 260 */             providers.add(localIterator.next());
/*     */           } catch (Throwable localThrowable) {
/* 262 */             if ((localThrowable instanceof ThreadDeath)) {
/* 263 */               ThreadDeath localThreadDeath = (ThreadDeath)localThrowable;
/* 264 */               throw localThreadDeath;
/*     */             }
/*     */ 
/* 267 */             System.err.println(localThrowable);
/*     */           }
/*     */         }
/*     */       }
/* 271 */       return Collections.unmodifiableList(providers);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.spi.AttachProvider
 * JD-Core Version:    0.6.2
 */