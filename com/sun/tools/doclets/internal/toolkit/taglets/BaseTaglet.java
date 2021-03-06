/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ public abstract class BaseTaglet
/*     */   implements Taglet
/*     */ {
/*  44 */   protected String name = "Default";
/*     */ 
/*     */   public boolean inConstructor()
/*     */   {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inField()
/*     */   {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inMethod()
/*     */   {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inOverview()
/*     */   {
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inPackage()
/*     */   {
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inType()
/*     */   {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isInlineTag()
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 127 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*     */   {
/* 135 */     throw new IllegalArgumentException("Method not supported in taglet " + getName() + ".");
/*     */   }
/*     */ 
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*     */   {
/* 143 */     throw new IllegalArgumentException("Method not supported in taglet " + getName() + ".");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.BaseTaglet
 * JD-Core Version:    0.6.2
 */