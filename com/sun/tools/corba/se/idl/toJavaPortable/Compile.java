/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.IncludeEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.InvalidArgument;
/*     */ import com.sun.tools.corba.se.idl.ModuleEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabFactory;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.UnionBranch;
/*     */ import com.sun.tools.corba.se.idl.UnionEntry;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Compile extends com.sun.tools.corba.se.idl.Compile
/*     */ {
/* 204 */   public Factories _factories = new Factories();
/*     */   ModuleEntry org;
/*     */   ModuleEntry omg;
/*     */   ModuleEntry corba;
/*     */   InterfaceEntry object;
/* 425 */   public Vector importTypes = new Vector();
/*     */   public SymtabFactory factory;
/*     */   public static int typedefInfo;
/* 428 */   public Hashtable list = new Hashtable();
/* 429 */   public static Compile compiler = null;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 145 */     compiler = new Compile();
/* 146 */     compiler.start(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void start(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       Util.registerMessageFile("com/sun/tools/corba/se/idl/toJavaPortable/toJavaPortable.prp");
/* 159 */       init(paramArrayOfString);
/* 160 */       if (this.arguments.versionRequest) {
/* 161 */         displayVersion();
/*     */       }
/*     */       else {
/* 164 */         preParse();
/* 165 */         Enumeration localEnumeration = parse();
/* 166 */         if (localEnumeration != null)
/*     */         {
/* 168 */           preEmit(localEnumeration);
/* 169 */           generate();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (InvalidArgument localInvalidArgument)
/*     */     {
/* 178 */       System.err.println(localInvalidArgument);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 182 */       System.err.println(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Compile()
/*     */   {
/* 191 */     this.factory = factories().symtabFactory();
/*     */   }
/*     */ 
/*     */   protected com.sun.tools.corba.se.idl.Factories factories()
/*     */   {
/* 207 */     return this._factories;
/*     */   }
/*     */ 
/*     */   protected void preParse()
/*     */   {
/* 221 */     Util.setSymbolTable(this.symbolTable);
/* 222 */     Util.setPackageTranslation(((Arguments)this.arguments).packageTranslation);
/*     */ 
/* 225 */     this.org = this.factory.moduleEntry();
/*     */ 
/* 230 */     this.org.emit(false);
/* 231 */     this.org.name("org");
/* 232 */     this.org.container(null);
/* 233 */     this.omg = this.factory.moduleEntry();
/* 234 */     this.omg.emit(false);
/* 235 */     this.omg.name("omg");
/* 236 */     this.omg.module("org");
/* 237 */     this.omg.container(this.org);
/* 238 */     this.org.addContained(this.omg);
/* 239 */     this.corba = this.factory.moduleEntry();
/* 240 */     this.corba.emit(false);
/* 241 */     this.corba.name("CORBA");
/* 242 */     this.corba.module("org/omg");
/* 243 */     this.corba.container(this.omg);
/* 244 */     this.omg.addContained(this.corba);
/* 245 */     this.symbolTable.put("org", this.org);
/* 246 */     this.symbolTable.put("org/omg", this.omg);
/* 247 */     this.symbolTable.put("org/omg/CORBA", this.corba);
/*     */ 
/* 250 */     this.object = ((InterfaceEntry)this.symbolTable.get("Object"));
/* 251 */     this.object.module("org/omg/CORBA");
/* 252 */     this.object.container(this.corba);
/* 253 */     this.symbolTable.put("org/omg/CORBA/Object", this.object);
/*     */ 
/* 256 */     PrimitiveEntry localPrimitiveEntry = this.factory.primitiveEntry();
/* 257 */     localPrimitiveEntry.name("TypeCode");
/* 258 */     localPrimitiveEntry.module("org/omg/CORBA");
/* 259 */     localPrimitiveEntry.container(this.corba);
/* 260 */     this.symbolTable.put("org/omg/CORBA/TypeCode", localPrimitiveEntry);
/* 261 */     this.symbolTable.put("CORBA/TypeCode", localPrimitiveEntry);
/* 262 */     this.overrideNames.put("CORBA/TypeCode", "org/omg/CORBA/TypeCode");
/* 263 */     this.overrideNames.put("org/omg/CORBA/TypeCode", "CORBA/TypeCode");
/*     */ 
/* 270 */     localPrimitiveEntry = this.factory.primitiveEntry();
/* 271 */     localPrimitiveEntry.name("Principal");
/* 272 */     localPrimitiveEntry.module("org/omg/CORBA");
/* 273 */     localPrimitiveEntry.container(this.corba);
/* 274 */     this.symbolTable.put("org/omg/CORBA/Principle", localPrimitiveEntry);
/* 275 */     this.symbolTable.put("CORBA/Principal", localPrimitiveEntry);
/* 276 */     this.overrideNames.put("CORBA/Principal", "org/omg/CORBA/Principal");
/* 277 */     this.overrideNames.put("org/omg/CORBA/Principal", "CORBA/Principal");
/*     */ 
/* 289 */     this.overrideNames.put("TRUE", "true");
/* 290 */     this.overrideNames.put("FALSE", "false");
/*     */ 
/* 294 */     this.symbolTable.put("CORBA", this.corba);
/* 295 */     this.overrideNames.put("CORBA", "org/omg/CORBA");
/* 296 */     this.overrideNames.put("org/omg/CORBA", "CORBA");
/*     */   }
/*     */ 
/*     */   protected void preEmit(Enumeration paramEnumeration)
/*     */   {
/* 302 */     typedefInfo = SymtabEntry.getVariableKey();
/* 303 */     Hashtable localHashtable = (Hashtable)this.symbolTable.clone();
/*     */ 
/* 305 */     for (Enumeration localEnumeration1 = localHashtable.elements(); localEnumeration1.hasMoreElements(); )
/*     */     {
/* 307 */       localSymtabEntry = (SymtabEntry)localEnumeration1.nextElement();
/*     */ 
/* 310 */       preEmitSTElement(localSymtabEntry);
/*     */     }
/*     */     SymtabEntry localSymtabEntry;
/* 315 */     localEnumeration1 = this.symbolTable.elements();
/* 316 */     while (localEnumeration1.hasMoreElements())
/*     */     {
/* 321 */       localSymtabEntry = (SymtabEntry)localEnumeration1.nextElement();
/* 322 */       if (((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry))) {
/* 323 */         Util.fillInfo(localSymtabEntry);
/*     */       }
/*     */       else
/*     */       {
/*     */         Enumeration localEnumeration2;
/* 332 */         if ((localSymtabEntry instanceof StructEntry))
/*     */         {
/* 334 */           localEnumeration2 = ((StructEntry)localSymtabEntry).members().elements();
/* 335 */           while (localEnumeration2.hasMoreElements())
/* 336 */             Util.fillInfo((SymtabEntry)localEnumeration2.nextElement());
/*     */         }
/* 338 */         else if (((localSymtabEntry instanceof InterfaceEntry)) && (((InterfaceEntry)localSymtabEntry).state() != null))
/*     */         {
/* 340 */           localEnumeration2 = ((InterfaceEntry)localSymtabEntry).state().elements();
/* 341 */           while (localEnumeration2.hasMoreElements())
/* 342 */             Util.fillInfo(((InterfaceState)localEnumeration2.nextElement()).entry);
/*     */         }
/* 344 */         else if ((localSymtabEntry instanceof UnionEntry))
/*     */         {
/* 346 */           localEnumeration2 = ((UnionEntry)localSymtabEntry).branches().elements();
/* 347 */           while (localEnumeration2.hasMoreElements()) {
/* 348 */             Util.fillInfo(((UnionBranch)localEnumeration2.nextElement()).typedef);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 355 */       if ((localSymtabEntry.module().equals("")) && (!(localSymtabEntry instanceof ModuleEntry)) && (!(localSymtabEntry instanceof IncludeEntry)) && (!(localSymtabEntry instanceof PrimitiveEntry))) {
/* 356 */         this.importTypes.addElement(localSymtabEntry);
/*     */       }
/*     */     }
/* 359 */     while (paramEnumeration.hasMoreElements())
/*     */     {
/* 361 */       localSymtabEntry = (SymtabEntry)paramEnumeration.nextElement();
/*     */ 
/* 364 */       preEmitELElement(localSymtabEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void preEmitSTElement(SymtabEntry paramSymtabEntry)
/*     */   {
/* 376 */     Hashtable localHashtable = ((Arguments)this.arguments).packages;
/* 377 */     if (localHashtable.size() > 0)
/*     */     {
/* 379 */       String str1 = (String)localHashtable.get(paramSymtabEntry.fullName());
/* 380 */       if (str1 != null)
/*     */       {
/* 382 */         String str2 = null;
/* 383 */         ModuleEntry localModuleEntry = null;
/* 384 */         Object localObject = null;
/* 385 */         while (str1 != null)
/*     */         {
/* 387 */           int i = str1.indexOf('.');
/* 388 */           if (i < 0)
/*     */           {
/* 390 */             str2 = str1;
/* 391 */             str1 = null;
/*     */           }
/*     */           else
/*     */           {
/* 395 */             str2 = str1.substring(0, i);
/* 396 */             str1 = str1.substring(i + 1);
/*     */           }
/*     */ 
/* 399 */           String str3 = ((ModuleEntry)localObject).fullName() + '/' + str2;
/* 400 */           localModuleEntry = (ModuleEntry)this.symbolTable.get(str3);
/* 401 */           if (localModuleEntry == null)
/*     */           {
/* 403 */             localModuleEntry = this.factory.moduleEntry();
/* 404 */             localModuleEntry.name(str2);
/* 405 */             localModuleEntry.container((SymtabEntry)localObject);
/* 406 */             if (localObject != null) localModuleEntry.module(((ModuleEntry)localObject).fullName());
/* 407 */             this.symbolTable.put(str2, localModuleEntry);
/*     */           }
/* 409 */           localObject = localModuleEntry;
/*     */         }
/* 411 */         paramSymtabEntry.module(localModuleEntry.fullName());
/* 412 */         paramSymtabEntry.container(localModuleEntry);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void preEmitELElement(SymtabEntry paramSymtabEntry)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Compile
 * JD-Core Version:    0.6.2
 */