/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.ForeignAttributes;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.AttributesImpl;
/*    */ 
/*    */ public final class ForeignAttributesImpl extends AttributesImpl
/*    */   implements ForeignAttributes
/*    */ {
/*    */   private final ValidationContext context;
/*    */   private final Locator locator;
/*    */   final ForeignAttributesImpl next;
/*    */ 
/*    */   public ForeignAttributesImpl(ValidationContext context, Locator locator, ForeignAttributesImpl next)
/*    */   {
/* 47 */     this.context = context;
/* 48 */     this.locator = locator;
/* 49 */     this.next = next;
/*    */   }
/*    */ 
/*    */   public ValidationContext getContext() {
/* 53 */     return this.context;
/*    */   }
/*    */ 
/*    */   public Locator getLocator() {
/* 57 */     return this.locator;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ForeignAttributesImpl
 * JD-Core Version:    0.6.2
 */