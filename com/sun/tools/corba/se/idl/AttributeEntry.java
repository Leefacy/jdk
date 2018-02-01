/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class AttributeEntry extends MethodEntry
/*     */ {
/*     */   static AttributeGen attributeGen;
/* 104 */   public boolean _readOnly = false;
/*     */ 
/*     */   protected AttributeEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AttributeEntry(AttributeEntry paramAttributeEntry)
/*     */   {
/*  57 */     super(paramAttributeEntry);
/*  58 */     this._readOnly = paramAttributeEntry._readOnly;
/*     */   }
/*     */ 
/*     */   protected AttributeEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID)
/*     */   {
/*  63 */     super(paramInterfaceEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  68 */     return new AttributeEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  79 */     attributeGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  87 */     return attributeGen;
/*     */   }
/*     */ 
/*     */   public boolean readOnly()
/*     */   {
/*  93 */     return this._readOnly;
/*     */   }
/*     */ 
/*     */   public void readOnly(boolean paramBoolean)
/*     */   {
/*  99 */     this._readOnly = paramBoolean;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.AttributeEntry
 * JD-Core Version:    0.6.2
 */