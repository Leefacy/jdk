/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class PrimitiveEntry extends SymtabEntry
/*    */ {
/*    */   static PrimitiveGen primitiveGen;
/*    */ 
/*    */   protected PrimitiveEntry()
/*    */   {
/* 52 */     repositoryID(Util.emptyID);
/*    */   }
/*    */ 
/*    */   protected PrimitiveEntry(String paramString)
/*    */   {
/* 57 */     name(paramString);
/* 58 */     module("");
/* 59 */     repositoryID(Util.emptyID);
/*    */   }
/*    */ 
/*    */   protected PrimitiveEntry(PrimitiveEntry paramPrimitiveEntry)
/*    */   {
/* 64 */     super(paramPrimitiveEntry);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 69 */     return new PrimitiveEntry(this);
/*    */   }
/*    */ 
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*    */   {
/* 80 */     primitiveGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */   public Generator generator()
/*    */   {
/* 88 */     return primitiveGen;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.PrimitiveEntry
 * JD-Core Version:    0.6.2
 */