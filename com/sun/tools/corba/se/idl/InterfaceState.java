/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ public class InterfaceState
/*    */ {
/*    */   public static final int Private = 0;
/*    */   public static final int Protected = 1;
/*    */   public static final int Public = 2;
/* 59 */   public int modifier = 2;
/* 60 */   public TypedefEntry entry = null;
/*    */ 
/*    */   public InterfaceState(int paramInt, TypedefEntry paramTypedefEntry)
/*    */   {
/* 53 */     this.modifier = paramInt;
/* 54 */     this.entry = paramTypedefEntry;
/* 55 */     if ((this.modifier < 0) || (this.modifier > 2))
/* 56 */       this.modifier = 2;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.InterfaceState
 * JD-Core Version:    0.6.2
 */