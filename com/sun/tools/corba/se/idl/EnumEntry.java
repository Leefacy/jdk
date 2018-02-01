/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class EnumEntry extends SymtabEntry
/*     */ {
/*     */   static EnumGen enumGen;
/* 107 */   private Vector _elements = new Vector();
/*     */ 
/*     */   protected EnumEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected EnumEntry(EnumEntry paramEnumEntry)
/*     */   {
/*  56 */     super(paramEnumEntry);
/*  57 */     this._elements = ((Vector)paramEnumEntry._elements.clone());
/*     */   }
/*     */ 
/*     */   protected EnumEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
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
/*  72 */     return new EnumEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  83 */     enumGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  91 */     return enumGen;
/*     */   }
/*     */ 
/*     */   public void addElement(String paramString)
/*     */   {
/*  97 */     this._elements.addElement(paramString);
/*     */   }
/*     */ 
/*     */   public Vector elements()
/*     */   {
/* 103 */     return this._elements;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.EnumEntry
 * JD-Core Version:    0.6.2
 */