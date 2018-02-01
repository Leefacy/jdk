/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ModuleEntry extends SymtabEntry
/*     */ {
/* 110 */   private Vector _contained = new Vector();
/*     */   static ModuleGen moduleGen;
/*     */ 
/*     */   protected ModuleEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ModuleEntry(ModuleEntry paramModuleEntry)
/*     */   {
/*  56 */     super(paramModuleEntry);
/*  57 */     this._contained = ((Vector)paramModuleEntry._contained.clone());
/*     */   }
/*     */ 
/*     */   protected ModuleEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  62 */     super(paramSymtabEntry, paramIDLID);
/*     */ 
/*  64 */     if (module().equals(""))
/*  65 */       module(name());
/*  66 */     else if (!name().equals(""))
/*  67 */       module(module() + "/" + name());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  72 */     return new ModuleEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  83 */     moduleGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  91 */     return moduleGen;
/*     */   }
/*     */ 
/*     */   public void addContained(SymtabEntry paramSymtabEntry)
/*     */   {
/*  99 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   public Vector contained()
/*     */   {
/* 107 */     return this._contained;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ModuleEntry
 * JD-Core Version:    0.6.2
 */