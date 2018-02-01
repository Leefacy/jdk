/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class PragmaEntry extends SymtabEntry
/*     */ {
/*     */   static PragmaGen pragmaGen;
/* 102 */   private String _data = null;
/*     */ 
/*     */   protected PragmaEntry()
/*     */   {
/*  51 */     repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   protected PragmaEntry(SymtabEntry paramSymtabEntry)
/*     */   {
/*  56 */     super(paramSymtabEntry, new IDLID());
/*  57 */     module(paramSymtabEntry.name());
/*  58 */     name("");
/*     */   }
/*     */ 
/*     */   protected PragmaEntry(PragmaEntry paramPragmaEntry)
/*     */   {
/*  63 */     super(paramPragmaEntry);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  68 */     return new PragmaEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  79 */     pragmaGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  87 */     return pragmaGen;
/*     */   }
/*     */ 
/*     */   public String data()
/*     */   {
/*  92 */     return this._data;
/*     */   }
/*     */ 
/*     */   public void data(String paramString)
/*     */   {
/*  97 */     this._data = paramString;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.PragmaEntry
 * JD-Core Version:    0.6.2
 */