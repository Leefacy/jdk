/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ValueEntry extends InterfaceEntry
/*     */ {
/* 407 */   private Vector _supportsNames = new Vector();
/* 408 */   private Vector _supports = new Vector();
/* 409 */   private Vector _initializers = new Vector();
/* 410 */   private boolean _custom = false;
/* 411 */   private boolean _isSafe = false;
/*     */   static ValueGen valueGen;
/*     */ 
/*     */   protected ValueEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected ValueEntry(ValueEntry paramValueEntry)
/*     */   {
/*  59 */     super(paramValueEntry);
/*  60 */     this._supportsNames = ((Vector)paramValueEntry._supportsNames.clone());
/*  61 */     this._supports = ((Vector)paramValueEntry._supports.clone());
/*  62 */     this._initializers = ((Vector)paramValueEntry._initializers.clone());
/*  63 */     this._custom = paramValueEntry._custom;
/*  64 */     this._isSafe = paramValueEntry._isSafe;
/*     */   }
/*     */ 
/*     */   protected ValueEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  69 */     super(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  74 */     return new ValueEntry(this);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*  85 */     valueGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/*  93 */     return valueGen;
/*     */   }
/*     */ 
/*     */   public void addSupport(SymtabEntry paramSymtabEntry)
/*     */   {
/* 102 */     this._supports.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   public Vector supports()
/*     */   {
/* 108 */     return this._supports;
/*     */   }
/*     */ 
/*     */   public void addSupportName(String paramString)
/*     */   {
/* 114 */     this._supportsNames.addElement(paramString);
/*     */   }
/*     */ 
/*     */   public Vector supportsNames()
/*     */   {
/* 123 */     return this._supportsNames;
/*     */   }
/*     */ 
/*     */   void derivedFromAddElement(SymtabEntry paramSymtabEntry, boolean paramBoolean, Scanner paramScanner)
/*     */   {
/* 137 */     if (((InterfaceType)paramSymtabEntry).getInterfaceType() != 1) {
/* 138 */       if (isAbstract())
/* 139 */         ParseException.nonAbstractParent2(paramScanner, fullName(), paramSymtabEntry.fullName());
/* 140 */       else if (derivedFrom().size() > 0) {
/* 141 */         ParseException.nonAbstractParent3(paramScanner, fullName(), paramSymtabEntry.fullName());
/*     */       }
/*     */     }
/* 144 */     if (derivedFrom().contains(paramSymtabEntry)) {
/* 145 */       ParseException.alreadyDerived(paramScanner, paramSymtabEntry.fullName(), fullName());
/*     */     }
/* 147 */     if (paramBoolean) {
/* 148 */       this._isSafe = true;
/*     */     }
/* 150 */     addDerivedFrom(paramSymtabEntry);
/* 151 */     addDerivedFromName(paramSymtabEntry.fullName());
/* 152 */     addParentType(paramSymtabEntry, paramScanner);
/*     */   }
/*     */ 
/*     */   void derivedFromAddElement(SymtabEntry paramSymtabEntry, Scanner paramScanner)
/*     */   {
/* 158 */     addSupport(paramSymtabEntry);
/* 159 */     addSupportName(paramSymtabEntry.fullName());
/* 160 */     addParentType(paramSymtabEntry, paramScanner);
/*     */   }
/*     */ 
/*     */   public boolean replaceForwardDecl(ForwardEntry paramForwardEntry, InterfaceEntry paramInterfaceEntry)
/*     */   {
/* 165 */     if (super.replaceForwardDecl(paramForwardEntry, paramInterfaceEntry))
/* 166 */       return true;
/* 167 */     int i = this._supports.indexOf(paramForwardEntry);
/* 168 */     if (i >= 0)
/* 169 */       this._supports.setElementAt(paramInterfaceEntry, i);
/* 170 */     return i >= 0;
/*     */   }
/*     */ 
/*     */   void initializersAddElement(MethodEntry paramMethodEntry, Scanner paramScanner)
/*     */   {
/* 176 */     Vector localVector1 = paramMethodEntry.parameters();
/* 177 */     int i = localVector1.size();
/* 178 */     for (Enumeration localEnumeration = this._initializers.elements(); localEnumeration.hasMoreElements(); )
/*     */     {
/* 180 */       Vector localVector2 = ((MethodEntry)localEnumeration.nextElement()).parameters();
/* 181 */       if (i == localVector2.size())
/*     */       {
/* 183 */         int j = 0;
/* 184 */         while ((j < i) && 
/* 185 */           (((ParameterEntry)localVector1.elementAt(j)).type().equals(
/* 186 */           ((ParameterEntry)localVector2
/* 186 */           .elementAt(j))
/* 186 */           .type()))) {
/* 184 */           j++;
/*     */         }
/*     */ 
/* 188 */         if (j >= i)
/* 189 */           ParseException.duplicateInit(paramScanner);
/*     */       }
/*     */     }
/* 192 */     this._initializers.addElement(paramMethodEntry);
/*     */   }
/*     */ 
/*     */   public Vector initializers()
/*     */   {
/* 197 */     return this._initializers;
/*     */   }
/*     */ 
/*     */   public void tagMethods()
/*     */   {
/* 205 */     for (Enumeration localEnumeration = methods().elements(); localEnumeration.hasMoreElements(); )
/* 206 */       ((MethodEntry)localEnumeration.nextElement()).valueMethod(true);
/*     */   }
/*     */ 
/*     */   public boolean isCustom()
/*     */   {
/* 391 */     return this._custom;
/*     */   }
/*     */ 
/*     */   public void setCustom(boolean paramBoolean)
/*     */   {
/* 397 */     this._custom = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isSafe()
/*     */   {
/* 404 */     return this._isSafe;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ValueEntry
 * JD-Core Version:    0.6.2
 */