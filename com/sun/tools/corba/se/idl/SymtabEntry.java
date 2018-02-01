/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SymtabEntry
/*     */ {
/* 268 */   static Stack includeStack = new Stack();
/*     */ 
/* 353 */   static boolean setEmit = true;
/* 354 */   static int maxKey = -1;
/*     */ 
/* 356 */   private SymtabEntry _container = null;
/* 357 */   private String _module = "";
/* 358 */   private String _name = "";
/* 359 */   private String _typeName = "";
/* 360 */   private SymtabEntry _type = null;
/* 361 */   private IncludeEntry _sourceFile = null;
/* 362 */   private Object _info = null;
/* 363 */   private RepositoryID _repID = new IDLID("", "", "1.0");
/* 364 */   private boolean _emit = setEmit;
/* 365 */   private Comment _comment = null;
/*     */   private Vector _dynamicVars;
/* 367 */   private boolean _isReferencable = true;
/*     */ 
/*     */   public SymtabEntry()
/*     */   {
/*  68 */     initDynamicVars();
/*     */   }
/*     */ 
/*     */   SymtabEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID)
/*     */   {
/*  73 */     this._module = paramSymtabEntry._module;
/*  74 */     this._name = paramSymtabEntry._name;
/*  75 */     this._type = paramSymtabEntry._type;
/*  76 */     this._typeName = paramSymtabEntry._typeName;
/*  77 */     this._sourceFile = paramSymtabEntry._sourceFile;
/*  78 */     this._info = paramSymtabEntry._info;
/*  79 */     this._repID = ((RepositoryID)paramIDLID.clone());
/*  80 */     ((IDLID)this._repID).appendToName(this._name);
/*  81 */     if (((paramSymtabEntry instanceof InterfaceEntry)) || ((paramSymtabEntry instanceof ModuleEntry)) || ((paramSymtabEntry instanceof StructEntry)) || ((paramSymtabEntry instanceof UnionEntry)) || (((paramSymtabEntry instanceof SequenceEntry)) && ((this instanceof SequenceEntry))))
/*  82 */       this._container = paramSymtabEntry;
/*     */     else
/*  84 */       this._container = paramSymtabEntry._container;
/*  85 */     initDynamicVars();
/*  86 */     this._comment = paramSymtabEntry._comment;
/*     */   }
/*     */ 
/*     */   SymtabEntry(SymtabEntry paramSymtabEntry)
/*     */   {
/*  92 */     this._module = paramSymtabEntry._module;
/*  93 */     this._name = paramSymtabEntry._name;
/*  94 */     this._type = paramSymtabEntry._type;
/*  95 */     this._typeName = paramSymtabEntry._typeName;
/*  96 */     this._sourceFile = paramSymtabEntry._sourceFile;
/*  97 */     this._info = paramSymtabEntry._info;
/*  98 */     this._repID = ((RepositoryID)paramSymtabEntry._repID.clone());
/*  99 */     this._container = paramSymtabEntry._container;
/*     */ 
/* 101 */     if ((this._type instanceof ForwardEntry)) {
/* 102 */       ((ForwardEntry)this._type).types.addElement(this);
/*     */     }
/* 104 */     initDynamicVars();
/*     */ 
/* 106 */     this._comment = paramSymtabEntry._comment;
/*     */   }
/*     */ 
/*     */   void initDynamicVars()
/*     */   {
/* 111 */     this._dynamicVars = new Vector(maxKey + 1);
/* 112 */     for (int i = 0; i <= maxKey; i++)
/* 113 */       this._dynamicVars.addElement(null);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 119 */     return new SymtabEntry(this);
/*     */   }
/*     */ 
/*     */   public final String fullName()
/*     */   {
/* 125 */     return this._module + '/' + this._name;
/*     */   }
/*     */ 
/*     */   public String module()
/*     */   {
/* 133 */     return this._module;
/*     */   }
/*     */ 
/*     */   public void module(String paramString)
/*     */   {
/* 140 */     if (paramString == null)
/* 141 */       this._module = "";
/*     */     else
/* 143 */       this._module = paramString;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 149 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void name(String paramString)
/*     */   {
/* 156 */     if (paramString == null)
/* 157 */       this._name = "";
/*     */     else {
/* 159 */       this._name = paramString;
/*     */     }
/*     */ 
/* 162 */     if ((this._repID instanceof IDLID))
/* 163 */       ((IDLID)this._repID).replaceName(paramString);
/*     */   }
/*     */ 
/*     */   public String typeName()
/*     */   {
/* 169 */     return this._typeName;
/*     */   }
/*     */ 
/*     */   protected void typeName(String paramString)
/*     */   {
/* 174 */     this._typeName = paramString;
/*     */   }
/*     */ 
/*     */   public SymtabEntry type()
/*     */   {
/* 180 */     return this._type;
/*     */   }
/*     */ 
/*     */   public void type(SymtabEntry paramSymtabEntry)
/*     */   {
/* 185 */     if (paramSymtabEntry == null)
/* 186 */       typeName("");
/*     */     else
/* 188 */       typeName(paramSymtabEntry.fullName());
/* 189 */     this._type = paramSymtabEntry;
/*     */ 
/* 191 */     if ((this._type instanceof ForwardEntry))
/* 192 */       ((ForwardEntry)this._type).types.addElement(this);
/*     */   }
/*     */ 
/*     */   public IncludeEntry sourceFile()
/*     */   {
/* 198 */     return this._sourceFile;
/*     */   }
/*     */ 
/*     */   public void sourceFile(IncludeEntry paramIncludeEntry)
/*     */   {
/* 204 */     this._sourceFile = paramIncludeEntry;
/*     */   }
/*     */ 
/*     */   public SymtabEntry container()
/*     */   {
/* 211 */     return this._container;
/*     */   }
/*     */ 
/*     */   public void container(SymtabEntry paramSymtabEntry)
/*     */   {
/* 218 */     if (((paramSymtabEntry instanceof InterfaceEntry)) || ((paramSymtabEntry instanceof ModuleEntry)))
/* 219 */       this._container = paramSymtabEntry;
/*     */   }
/*     */ 
/*     */   public RepositoryID repositoryID()
/*     */   {
/* 225 */     return this._repID;
/*     */   }
/*     */ 
/*     */   public void repositoryID(RepositoryID paramRepositoryID)
/*     */   {
/* 232 */     this._repID = paramRepositoryID;
/*     */   }
/*     */ 
/*     */   public boolean emit()
/*     */   {
/* 238 */     return (this._emit) && (this._isReferencable);
/*     */   }
/*     */ 
/*     */   public void emit(boolean paramBoolean)
/*     */   {
/* 243 */     this._emit = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Comment comment()
/*     */   {
/* 250 */     return this._comment;
/*     */   }
/*     */ 
/*     */   public void comment(Comment paramComment)
/*     */   {
/* 255 */     this._comment = paramComment;
/*     */   }
/*     */ 
/*     */   public boolean isReferencable()
/*     */   {
/* 260 */     return this._isReferencable;
/*     */   }
/*     */ 
/*     */   public void isReferencable(boolean paramBoolean)
/*     */   {
/* 265 */     this._isReferencable = paramBoolean;
/*     */   }
/*     */ 
/*     */   static void enteringInclude()
/*     */   {
/* 272 */     includeStack.push(new Boolean(setEmit));
/* 273 */     setEmit = false;
/*     */   }
/*     */ 
/*     */   static void exitingInclude()
/*     */   {
/* 278 */     setEmit = ((Boolean)includeStack.pop()).booleanValue();
/*     */   }
/*     */ 
/*     */   public static int getVariableKey()
/*     */   {
/* 287 */     return ++maxKey;
/*     */   }
/*     */ 
/*     */   public void dynamicVariable(int paramInt, Object paramObject)
/*     */     throws NoSuchFieldException
/*     */   {
/* 298 */     if (paramInt > maxKey) {
/* 299 */       throw new NoSuchFieldException(Integer.toString(paramInt));
/*     */     }
/*     */ 
/* 302 */     if (paramInt >= this._dynamicVars.size())
/* 303 */       growVars();
/* 304 */     this._dynamicVars.setElementAt(paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public Object dynamicVariable(int paramInt)
/*     */     throws NoSuchFieldException
/*     */   {
/* 316 */     if (paramInt > maxKey) {
/* 317 */       throw new NoSuchFieldException(Integer.toString(paramInt));
/*     */     }
/*     */ 
/* 320 */     if (paramInt >= this._dynamicVars.size())
/* 321 */       growVars();
/* 322 */     return this._dynamicVars.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   void growVars()
/*     */   {
/* 328 */     int i = maxKey - this._dynamicVars.size() + 1;
/* 329 */     for (int j = 0; j < i; j++)
/* 330 */       this._dynamicVars.addElement(null);
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Generator generator()
/*     */   {
/* 350 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.SymtabEntry
 * JD-Core Version:    0.6.2
 */