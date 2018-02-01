/*     */ package com.sun.tools.attach;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public final class AttachPermission extends BasicPermission
/*     */ {
/*     */   static final long serialVersionUID = -4619447669752976181L;
/*     */ 
/*     */   public AttachPermission(String paramString)
/*     */   {
/*  98 */     super(paramString);
/*  99 */     if ((!paramString.equals("attachVirtualMachine")) && (!paramString.equals("createAttachProvider")))
/* 100 */       throw new IllegalArgumentException("name: " + paramString);
/*     */   }
/*     */ 
/*     */   public AttachPermission(String paramString1, String paramString2)
/*     */   {
/* 117 */     super(paramString1);
/* 118 */     if ((!paramString1.equals("attachVirtualMachine")) && (!paramString1.equals("createAttachProvider"))) {
/* 119 */       throw new IllegalArgumentException("name: " + paramString1);
/*     */     }
/* 121 */     if ((paramString2 != null) && (paramString2.length() > 0))
/* 122 */       throw new IllegalArgumentException("actions: " + paramString2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.AttachPermission
 * JD-Core Version:    0.6.2
 */