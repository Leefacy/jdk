/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SequenceEntry extends SymtabEntry
/*     */ {
/*     */   static SequenceGen sequenceGen;
/* 139 */   private Expression _maxSize = null;
/* 140 */   private Vector _contained = new Vector();
/*     */ 
/*     */   protected SequenceEntry()
/*     */   {
/*  54 */     repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   protected SequenceEntry(SequenceEntry paramSequenceEntry)
/*     */   {
/*  59 */     super(paramSequenceEntry);
/*  60 */     this._maxSize = paramSequenceEntry._maxSize;
/*     */   }
/*     */ 
/*     */   protected SequenceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  65 */     super(paramSymtabEntry, paramIDLID);
/*  66 */     if (!(paramSymtabEntry instanceof SequenceEntry))
/*     */     {
/*  68 */       if (module().equals(""))
/*  69 */         module(name());
/*  70 */       else if (!name().equals(""))
/*  71 */         module(module() + "/" + name()); 
/*     */     }
/*  72 */     repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  77 */     return new SequenceEntry(this);
/*     */   }
/*     */ 
/*     */   public boolean isReferencable()
/*     */   {
/*  84 */     return type().isReferencable();
/*     */   }
/*     */ 
/*     */   public void isReferencable(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/* 100 */     sequenceGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/* 108 */     return sequenceGen;
/*     */   }
/*     */ 
/*     */   public void maxSize(Expression paramExpression)
/*     */   {
/* 115 */     this._maxSize = paramExpression;
/*     */   }
/*     */ 
/*     */   public Expression maxSize()
/*     */   {
/* 122 */     return this._maxSize;
/*     */   }
/*     */ 
/*     */   public void addContained(SymtabEntry paramSymtabEntry)
/*     */   {
/* 128 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   public Vector contained()
/*     */   {
/* 134 */     return this._contained;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.SequenceEntry
 * JD-Core Version:    0.6.2
 */