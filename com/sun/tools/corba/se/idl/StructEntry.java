/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class StructEntry extends SymtabEntry
/*     */ {
/* 139 */   private Vector _members = new Vector();
/* 140 */   private Vector _contained = new Vector();
/*     */   static StructGen structGen;
/*     */ 
/*     */   protected StructEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected StructEntry(StructEntry paramStructEntry)
/*     */   {
/*  57 */     super(paramStructEntry);
/*  58 */     if (!name().equals(""))
/*     */     {
/*  60 */       module(module() + name());
/*  61 */       name("");
/*     */     }
/*  63 */     this._members = ((Vector)paramStructEntry._members.clone());
/*  64 */     this._contained = ((Vector)paramStructEntry._contained.clone());
/*     */   }
/*     */ 
/*     */   protected StructEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  69 */     super(paramSymtabEntry, paramIDLID);
/*  70 */     if (module().equals(""))
/*  71 */       module(name());
/*  72 */     else if (!name().equals(""))
/*  73 */       module(module() + "/" + name());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  78 */     return new StructEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  89 */     structGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  97 */     return structGen;
/*     */   }
/*     */ 
/*     */   public void addMember(TypedefEntry paramTypedefEntry)
/*     */   {
/* 103 */     this._members.addElement(paramTypedefEntry);
/*     */   }
/*     */ 
/*     */   public Vector members()
/*     */   {
/* 110 */     return this._members;
/*     */   }
/*     */ 
/*     */   public void addContained(SymtabEntry paramSymtabEntry)
/*     */   {
/* 115 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   public Vector contained()
/*     */   {
/* 136 */     return this._contained;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.StructEntry
 * JD-Core Version:    0.6.2
 */