/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class IncludeEntry extends SymtabEntry
/*     */ {
/*     */   static IncludeGen includeGen;
/* 121 */   private Vector includeList = new Vector();
/*     */ 
/* 124 */   private String _absFilename = null;
/*     */ 
/*     */   protected IncludeEntry()
/*     */   {
/*  52 */     repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   protected IncludeEntry(SymtabEntry paramSymtabEntry)
/*     */   {
/*  57 */     super(paramSymtabEntry, new IDLID());
/*  58 */     module(paramSymtabEntry.name());
/*  59 */     name("");
/*     */   }
/*     */ 
/*     */   protected IncludeEntry(IncludeEntry paramIncludeEntry)
/*     */   {
/*  64 */     super(paramIncludeEntry);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  69 */     return new IncludeEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  80 */     includeGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  88 */     return includeGen;
/*     */   }
/*     */ 
/*     */   public void absFilename(String paramString)
/*     */   {
/*  95 */     this._absFilename = paramString;
/*     */   }
/*     */ 
/*     */   public String absFilename()
/*     */   {
/* 103 */     return this._absFilename;
/*     */   }
/*     */ 
/*     */   public void addInclude(IncludeEntry paramIncludeEntry)
/*     */   {
/* 110 */     this.includeList.addElement(paramIncludeEntry);
/*     */   }
/*     */ 
/*     */   public Vector includes()
/*     */   {
/* 116 */     return this.includeList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.IncludeEntry
 * JD-Core Version:    0.6.2
 */