/*     */ package com.sun.xml.internal.rngom.xml.sax;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.util.Uri;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class XmlBaseHandler
/*     */ {
/*  52 */   private int depth = 0;
/*     */   private Locator loc;
/*  54 */   private Entry stack = null;
/*     */ 
/*     */   public void setLocator(Locator loc)
/*     */   {
/*  64 */     this.loc = loc;
/*     */   }
/*     */ 
/*     */   public void startElement() {
/*  68 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   public void endElement() {
/*  72 */     if ((this.stack != null) && (this.stack.depth == this.depth))
/*  73 */       this.stack = this.stack.parent;
/*  74 */     this.depth -= 1;
/*     */   }
/*     */ 
/*     */   public void xmlBaseAttribute(String value) {
/*  78 */     Entry entry = new Entry(null);
/*  79 */     entry.parent = this.stack;
/*  80 */     this.stack = entry;
/*  81 */     entry.attValue = Uri.escapeDisallowedChars(value);
/*  82 */     entry.systemId = getSystemId();
/*  83 */     entry.depth = this.depth;
/*     */   }
/*     */ 
/*     */   private String getSystemId() {
/*  87 */     return this.loc == null ? null : this.loc.getSystemId();
/*     */   }
/*     */ 
/*     */   public String getBaseUri() {
/*  91 */     return getBaseUri1(getSystemId(), this.stack);
/*     */   }
/*     */ 
/*     */   private static String getBaseUri1(String baseUri, Entry stack) {
/*  95 */     if ((stack == null) || ((baseUri != null) && 
/*  96 */       (!baseUri
/*  96 */       .equals(stack.systemId))))
/*     */     {
/*  97 */       return baseUri;
/*  98 */     }baseUri = stack.attValue;
/*  99 */     if (Uri.isAbsolute(baseUri))
/* 100 */       return baseUri;
/* 101 */     return Uri.resolve(getBaseUri1(stack.systemId, stack.parent), baseUri);
/*     */   }
/*     */ 
/*     */   private static class Entry
/*     */   {
/*     */     private Entry parent;
/*     */     private String attValue;
/*     */     private String systemId;
/*     */     private int depth;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.xml.sax.XmlBaseHandler
 * JD-Core Version:    0.6.2
 */