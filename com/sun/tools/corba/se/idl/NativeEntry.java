/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class NativeEntry extends SymtabEntry
/*    */ {
/*    */   static NativeGen nativeGen;
/*    */ 
/*    */   protected NativeEntry()
/*    */   {
/* 41 */     repositoryID(Util.emptyID);
/*    */   }
/*    */ 
/*    */   protected NativeEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*    */   {
/* 46 */     super(paramSymtabEntry, paramIDLID);
/* 47 */     if (module().equals(""))
/* 48 */       module(name());
/* 49 */     else if (!name().equals(""))
/* 50 */       module(module() + "/" + name());
/*    */   }
/*    */ 
/*    */   protected NativeEntry(NativeEntry paramNativeEntry)
/*    */   {
/* 55 */     super(paramNativeEntry);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 61 */     return new NativeEntry(this);
/*    */   }
/*    */ 
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*    */   {
/* 72 */     nativeGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */   public Generator generator()
/*    */   {
/* 80 */     return nativeGen;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.NativeEntry
 * JD-Core Version:    0.6.2
 */