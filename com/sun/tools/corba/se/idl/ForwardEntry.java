/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ForwardEntry extends SymtabEntry
/*     */   implements InterfaceType
/*     */ {
/*     */   static ForwardGen forwardGen;
/* 139 */   Vector derivers = new Vector();
/* 140 */   Vector types = new Vector();
/* 141 */   private int _type = 0;
/*     */ 
/*     */   protected ForwardEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ForwardEntry(ForwardEntry paramForwardEntry)
/*     */   {
/*  57 */     super(paramForwardEntry);
/*     */   }
/*     */ 
/*     */   protected ForwardEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  62 */     super(paramSymtabEntry, paramIDLID);
/*  63 */     if (module().equals(""))
/*  64 */       module(name());
/*  65 */     else if (!name().equals(""))
/*  66 */       module(module() + "/" + name());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  71 */     return new ForwardEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  82 */     forwardGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  90 */     return forwardGen;
/*     */   }
/*     */ 
/*     */   static boolean replaceForwardDecl(InterfaceEntry paramInterfaceEntry)
/*     */   {
/*  95 */     boolean bool = true;
/*     */     try
/*     */     {
/*  99 */       ForwardEntry localForwardEntry = (ForwardEntry)Parser.symbolTable
/*  99 */         .get(paramInterfaceEntry
/*  99 */         .fullName());
/* 100 */       if (localForwardEntry != null)
/*     */       {
/* 103 */         bool = paramInterfaceEntry.getInterfaceType() == localForwardEntry
/* 103 */           .getInterfaceType();
/* 104 */         localForwardEntry.type(paramInterfaceEntry);
/*     */ 
/* 109 */         paramInterfaceEntry.forwardedDerivers = localForwardEntry.derivers;
/* 110 */         Enumeration localEnumeration = localForwardEntry.derivers.elements();
/* 111 */         while (localEnumeration.hasMoreElements()) {
/* 112 */           ((InterfaceEntry)localEnumeration.nextElement()).replaceForwardDecl(localForwardEntry, paramInterfaceEntry);
/*     */         }
/*     */ 
/* 115 */         localEnumeration = localForwardEntry.types.elements();
/* 116 */         while (localEnumeration.hasMoreElements())
/* 117 */           ((SymtabEntry)localEnumeration.nextElement()).type(paramInterfaceEntry);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 122 */     return bool;
/*     */   }
/*     */ 
/*     */   public int getInterfaceType()
/*     */   {
/* 130 */     return this._type;
/*     */   }
/*     */ 
/*     */   public void setInterfaceType(int paramInt)
/*     */   {
/* 135 */     this._type = paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ForwardEntry
 * JD-Core Version:    0.6.2
 */