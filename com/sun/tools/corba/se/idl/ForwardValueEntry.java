/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class ForwardValueEntry extends ForwardEntry
/*    */ {
/*    */   static ForwardValueGen forwardValueGen;
/*    */ 
/*    */   protected ForwardValueEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected ForwardValueEntry(ForwardValueEntry paramForwardValueEntry)
/*    */   {
/* 57 */     super(paramForwardValueEntry);
/*    */   }
/*    */ 
/*    */   protected ForwardValueEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*    */   {
/* 62 */     super(paramSymtabEntry, paramIDLID);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 67 */     return new ForwardValueEntry(this);
/*    */   }
/*    */ 
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*    */   {
/* 78 */     forwardValueGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */   public Generator generator()
/*    */   {
/* 86 */     return forwardValueGen;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ForwardValueEntry
 * JD-Core Version:    0.6.2
 */