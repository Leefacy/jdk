/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.parse.Context;
/*    */ 
/*    */ public class DValuePattern extends DPattern
/*    */ {
/*    */   private String datatypeLibrary;
/*    */   private String type;
/*    */   private String value;
/*    */   private Context context;
/*    */   private String ns;
/*    */ 
/*    */   public DValuePattern(String datatypeLibrary, String type, String value, Context context, String ns)
/*    */   {
/* 61 */     this.datatypeLibrary = datatypeLibrary;
/* 62 */     this.type = type;
/* 63 */     this.value = value;
/* 64 */     this.context = context;
/* 65 */     this.ns = ns;
/*    */   }
/*    */ 
/*    */   public String getDatatypeLibrary() {
/* 69 */     return this.datatypeLibrary;
/*    */   }
/*    */ 
/*    */   public String getType() {
/* 73 */     return this.type;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 77 */     return this.value;
/*    */   }
/*    */ 
/*    */   public Context getContext() {
/* 81 */     return this.context;
/*    */   }
/*    */ 
/*    */   public String getNs() {
/* 85 */     return this.ns;
/*    */   }
/*    */ 
/*    */   public boolean isNullable() {
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   public Object accept(DPatternVisitor visitor) {
/* 93 */     return visitor.onValue(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DValuePattern
 * JD-Core Version:    0.6.2
 */