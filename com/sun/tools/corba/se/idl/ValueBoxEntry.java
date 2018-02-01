/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class ValueBoxEntry extends ValueEntry
/*    */ {
/*    */   static ValueBoxGen valueBoxGen;
/*    */ 
/*    */   protected ValueBoxEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected ValueBoxEntry(ValueBoxEntry paramValueBoxEntry)
/*    */   {
/* 55 */     super(paramValueBoxEntry);
/*    */   }
/*    */ 
/*    */   protected ValueBoxEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*    */   {
/* 60 */     super(paramSymtabEntry, paramIDLID);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 65 */     return new ValueBoxEntry(this);
/*    */   }
/*    */ 
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*    */   {
/* 76 */     valueBoxGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */   public Generator generator()
/*    */   {
/* 84 */     return valueBoxGen;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ValueBoxEntry
 * JD-Core Version:    0.6.2
 */