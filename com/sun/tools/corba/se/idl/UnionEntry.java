/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class UnionEntry extends SymtabEntry
/*     */ {
/* 186 */   private Vector _branches = new Vector();
/* 187 */   private TypedefEntry _defaultBranch = null;
/* 188 */   private Vector _contained = new Vector();
/*     */   static UnionGen unionGen;
/*     */ 
/*     */   protected UnionEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected UnionEntry(UnionEntry paramUnionEntry)
/*     */   {
/*  59 */     super(paramUnionEntry);
/*  60 */     if (!name().equals(""))
/*     */     {
/*  62 */       module(module() + name());
/*  63 */       name("");
/*     */     }
/*  65 */     this._branches = ((Vector)paramUnionEntry._branches.clone());
/*  66 */     this._defaultBranch = paramUnionEntry._defaultBranch;
/*  67 */     this._contained = paramUnionEntry._contained;
/*     */   }
/*     */ 
/*     */   protected UnionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  72 */     super(paramSymtabEntry, paramIDLID);
/*  73 */     if (module().equals(""))
/*  74 */       module(name());
/*  75 */     else if (!name().equals(""))
/*  76 */       module(module() + "/" + name());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  81 */     return new UnionEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  92 */     unionGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/* 100 */     return unionGen;
/*     */   }
/*     */ 
/*     */   public void addBranch(UnionBranch paramUnionBranch)
/*     */   {
/* 105 */     this._branches.addElement(paramUnionBranch);
/*     */   }
/*     */ 
/*     */   public Vector branches()
/*     */   {
/* 111 */     return this._branches;
/*     */   }
/*     */ 
/*     */   public void defaultBranch(TypedefEntry paramTypedefEntry)
/*     */   {
/* 119 */     this._defaultBranch = paramTypedefEntry;
/*     */   }
/*     */ 
/*     */   public TypedefEntry defaultBranch()
/*     */   {
/* 127 */     return this._defaultBranch;
/*     */   }
/*     */ 
/*     */   public void addContained(SymtabEntry paramSymtabEntry)
/*     */   {
/* 132 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   public Vector contained()
/*     */   {
/* 154 */     return this._contained;
/*     */   }
/*     */ 
/*     */   boolean has(Expression paramExpression)
/*     */   {
/* 159 */     Enumeration localEnumeration1 = this._branches.elements();
/* 160 */     while (localEnumeration1.hasMoreElements())
/*     */     {
/* 162 */       Enumeration localEnumeration2 = ((UnionBranch)localEnumeration1.nextElement()).labels.elements();
/* 163 */       while (localEnumeration2.hasMoreElements())
/*     */       {
/* 165 */         Expression localExpression = (Expression)localEnumeration2.nextElement();
/* 166 */         if ((localExpression.equals(paramExpression)) || (localExpression.value().equals(paramExpression.value())))
/* 167 */           return true;
/*     */       }
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   boolean has(TypedefEntry paramTypedefEntry)
/*     */   {
/* 175 */     Enumeration localEnumeration = this._branches.elements();
/* 176 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 178 */       UnionBranch localUnionBranch = (UnionBranch)localEnumeration.nextElement();
/* 179 */       if ((!localUnionBranch.typedef.equals(paramTypedefEntry)) && (localUnionBranch.typedef.name().equals(paramTypedefEntry.name())))
/* 180 */         return true;
/*     */     }
/* 182 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.UnionEntry
 * JD-Core Version:    0.6.2
 */