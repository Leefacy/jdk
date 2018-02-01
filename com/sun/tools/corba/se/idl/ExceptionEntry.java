/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class ExceptionEntry extends StructEntry
/*    */ {
/*    */   static ExceptionGen exceptionGen;
/*    */ 
/*    */   protected ExceptionEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected ExceptionEntry(ExceptionEntry paramExceptionEntry)
/*    */   {
/* 58 */     super(paramExceptionEntry);
/*    */   }
/*    */ 
/*    */   protected ExceptionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*    */   {
/* 63 */     super(paramSymtabEntry, paramIDLID);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 68 */     return new ExceptionEntry(this);
/*    */   }
/*    */ 
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*    */   {
/* 79 */     exceptionGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */   public Generator generator()
/*    */   {
/* 87 */     return exceptionGen;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ExceptionEntry
 * JD-Core Version:    0.6.2
 */