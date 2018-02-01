/*      */ package com.sun.tools.corba.se.idl;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.constExpr.And;
/*      */ import com.sun.tools.corba.se.idl.constExpr.BinaryExpr;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Divide;
/*      */ import com.sun.tools.corba.se.idl.constExpr.EvaluationException;
/*      */ import com.sun.tools.corba.se.idl.constExpr.ExprFactory;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Minus;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Modulo;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Negative;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Not;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Or;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Plus;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Positive;
/*      */ import com.sun.tools.corba.se.idl.constExpr.ShiftLeft;
/*      */ import com.sun.tools.corba.se.idl.constExpr.ShiftRight;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Terminal;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Times;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Xor;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class Parser
/*      */ {
/*  556 */   private boolean _isModuleLegalType = false;
/*      */   private static final int MAX_SHORT = 32767;
/*      */   private static final int MIN_SHORT = -32768;
/*      */   private static final int MAX_USHORT = 65535;
/* 2298 */   UnionBranch defaultBranch = null;
/*      */   public static final String unknownNamePrefix = "uN__";
/*      */   static Hashtable symbolTable;
/* 3851 */   Hashtable lcSymbolTable = new Hashtable();
/*      */   static Hashtable overrideNames;
/* 3853 */   Vector emitList = new Vector();
/*      */   boolean emitAll;
/*      */   boolean cppModule;
/*      */   boolean noWarn;
/*      */   Scanner scanner;
/*      */   Hashtable symbols;
/* 3863 */   Vector macros = new Vector();
/*      */   Vector paths;
/* 3867 */   SymtabEntry currentModule = null;
/*      */ 
/* 3872 */   static Stack repIDStack = new Stack();
/*      */ 
/* 3881 */   private static int ftlKey = SymtabEntry.getVariableKey();
/*      */ 
/* 3883 */   int sequence = 0;
/*      */   Vector includes;
/*      */   Vector includeEntries;
/* 3888 */   boolean parsingConditionalExpr = false;
/*      */   Token token;
/*      */   ModuleEntry topLevelModule;
/*      */   private Preprocessor prep;
/*      */   private boolean verbose;
/*      */   SymtabFactory stFactory;
/*      */   ExprFactory exprFactory;
/*      */   private String[] keywords;
/* 3901 */   private TokenBuffer tokenHistory = new TokenBuffer();
/*      */   protected float corbaLevel;
/*      */   private Arguments arguments;
/*      */ 
/*      */   Parser(Preprocessor paramPreprocessor, Arguments paramArguments, Hashtable paramHashtable1, Hashtable paramHashtable2, SymtabFactory paramSymtabFactory, ExprFactory paramExprFactory, String[] paramArrayOfString)
/*      */   {
/*   90 */     this.arguments = paramArguments;
/*   91 */     this.noWarn = paramArguments.noWarn;
/*   92 */     this.corbaLevel = paramArguments.corbaLevel;
/*   93 */     this.paths = paramArguments.includePaths;
/*   94 */     this.symbols = paramArguments.definedSymbols;
/*   95 */     this.verbose = paramArguments.verbose;
/*   96 */     this.emitAll = paramArguments.emitAll;
/*      */ 
/*   98 */     this.cppModule = paramArguments.cppModule;
/*      */ 
/*  101 */     overrideNames = paramHashtable1 == null ? new Hashtable() : paramHashtable1;
/*  102 */     symbolTable = paramHashtable2 == null ? new Hashtable() : paramHashtable2;
/*  103 */     this.keywords = (paramArrayOfString == null ? new String[0] : paramArrayOfString);
/*  104 */     this.stFactory = paramSymtabFactory;
/*  105 */     this.exprFactory = paramExprFactory;
/*  106 */     this.currentModule = (this.topLevelModule = new ModuleEntry());
/*  107 */     this.prep = paramPreprocessor;
/*  108 */     repIDStack.push(new IDLID());
/*  109 */     addPrimEntries();
/*      */   }
/*      */ 
/*      */   void parse(String paramString)
/*      */     throws IOException
/*      */   {
/*  117 */     IncludeEntry localIncludeEntry = this.stFactory.includeEntry();
/*  118 */     localIncludeEntry.name('"' + paramString + '"');
/*      */     try
/*      */     {
/*  122 */       localIncludeEntry.absFilename(Util.getAbsolutePath(paramString, this.paths));
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */ 
/*  131 */     this.scanner = new Scanner(localIncludeEntry, this.keywords, this.verbose, this.emitAll, this.corbaLevel, this.arguments.scannerDebugFlag);
/*      */ 
/*  133 */     this.topLevelModule.sourceFile(localIncludeEntry);
/*      */ 
/*  140 */     this.token = new Token(0);
/*  141 */     this.tokenHistory.insert(this.token);
/*      */     try
/*      */     {
/*  144 */       match(0);
/*  145 */       if (this.token.equals(999))
/*  146 */         ParseException.nothing(paramString);
/*      */       else
/*  148 */         specification(this.topLevelModule);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*      */     }
/*      */     catch (EOFException localEOFException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addPrimEntries()
/*      */   {
/*  165 */     symbolTable.put("short", this.stFactory.primitiveEntry("short"));
/*  166 */     symbolTable.put("long", this.stFactory.primitiveEntry("long"));
/*  167 */     symbolTable.put("long long", this.stFactory.primitiveEntry("long long"));
/*  168 */     symbolTable.put("unsigned short", this.stFactory.primitiveEntry("unsigned short"));
/*  169 */     symbolTable.put("unsigned long", this.stFactory.primitiveEntry("unsigned long"));
/*  170 */     symbolTable.put("unsigned long long", this.stFactory.primitiveEntry("unsigned long long"));
/*  171 */     symbolTable.put("char", this.stFactory.primitiveEntry("char"));
/*  172 */     symbolTable.put("wchar", this.stFactory.primitiveEntry("wchar"));
/*  173 */     symbolTable.put("float", this.stFactory.primitiveEntry("float"));
/*      */ 
/*  175 */     symbolTable.put("double", this.stFactory.primitiveEntry("double"));
/*  176 */     symbolTable.put("boolean", this.stFactory.primitiveEntry("boolean"));
/*  177 */     symbolTable.put("octet", this.stFactory.primitiveEntry("octet"));
/*  178 */     symbolTable.put("any", this.stFactory.primitiveEntry("any"));
/*      */ 
/*  180 */     InterfaceEntry localInterfaceEntry = this.stFactory.interfaceEntry();
/*  181 */     localInterfaceEntry.name("Object");
/*  182 */     symbolTable.put("Object", localInterfaceEntry);
/*      */ 
/*  184 */     ValueEntry localValueEntry = this.stFactory.valueEntry();
/*  185 */     localValueEntry.name("ValueBase");
/*  186 */     symbolTable.put("ValueBase", localValueEntry);
/*      */ 
/*  189 */     this.lcSymbolTable.put("short", this.stFactory.primitiveEntry("short"));
/*  190 */     this.lcSymbolTable.put("long", this.stFactory.primitiveEntry("long"));
/*  191 */     this.lcSymbolTable.put("long long", this.stFactory.primitiveEntry("long long"));
/*  192 */     this.lcSymbolTable.put("unsigned short", this.stFactory.primitiveEntry("unsigned short"));
/*  193 */     this.lcSymbolTable.put("unsigned long", this.stFactory.primitiveEntry("unsigned long"));
/*  194 */     this.lcSymbolTable.put("unsigned long long", this.stFactory.primitiveEntry("unsigned long long"));
/*  195 */     this.lcSymbolTable.put("char", this.stFactory.primitiveEntry("char"));
/*  196 */     this.lcSymbolTable.put("wchar", this.stFactory.primitiveEntry("wchar"));
/*  197 */     this.lcSymbolTable.put("float", this.stFactory.primitiveEntry("float"));
/*      */ 
/*  199 */     this.lcSymbolTable.put("double", this.stFactory.primitiveEntry("double"));
/*  200 */     this.lcSymbolTable.put("boolean", this.stFactory.primitiveEntry("boolean"));
/*  201 */     this.lcSymbolTable.put("octet", this.stFactory.primitiveEntry("octet"));
/*  202 */     this.lcSymbolTable.put("any", this.stFactory.primitiveEntry("any"));
/*  203 */     this.lcSymbolTable.put("object", localInterfaceEntry);
/*  204 */     this.lcSymbolTable.put("valuebase", localValueEntry);
/*      */   }
/*      */ 
/*      */   private void specification(ModuleEntry paramModuleEntry)
/*      */     throws IOException
/*      */   {
/*  212 */     while (!this.token.equals(999))
/*      */     {
/*  214 */       definition(paramModuleEntry);
/*  215 */       addToEmitList(paramModuleEntry);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addToEmitList(ModuleEntry paramModuleEntry)
/*      */   {
/*  225 */     for (Enumeration localEnumeration = paramModuleEntry.contained().elements(); localEnumeration.hasMoreElements(); )
/*      */     {
/*  227 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/*  228 */       if (localSymtabEntry.emit())
/*      */       {
/*  230 */         this.emitList.addElement(localSymtabEntry);
/*      */ 
/*  239 */         if ((localSymtabEntry instanceof ModuleEntry))
/*  240 */           checkContained((ModuleEntry)localSymtabEntry);
/*  241 */         if ((localSymtabEntry instanceof IncludeEntry))
/*      */         {
/*  243 */           this.includes.addElement(localSymtabEntry.name());
/*  244 */           this.includeEntries.addElement(localSymtabEntry);
/*      */         }
/*      */ 
/*      */       }
/*  250 */       else if ((localSymtabEntry instanceof ModuleEntry)) {
/*  251 */         checkContained((ModuleEntry)localSymtabEntry);
/*      */       }
/*      */     }
/*  253 */     paramModuleEntry.contained().removeAllElements();
/*      */   }
/*      */ 
/*      */   private void checkContained(ModuleEntry paramModuleEntry)
/*      */   {
/*  263 */     for (Enumeration localEnumeration = paramModuleEntry.contained().elements(); localEnumeration.hasMoreElements(); )
/*      */     {
/*  265 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/*  266 */       if ((localSymtabEntry instanceof ModuleEntry))
/*  267 */         checkContained((ModuleEntry)localSymtabEntry);
/*  268 */       if (localSymtabEntry.emit())
/*      */       {
/*  270 */         if (!this.emitList.contains(paramModuleEntry))
/*  271 */           this.emitList.addElement(paramModuleEntry);
/*  272 */         paramModuleEntry.emit(true);
/*  273 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void definition(ModuleEntry paramModuleEntry)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  285 */       switch (this.token.type)
/*      */       {
/*      */       case 9:
/*      */       case 29:
/*      */       case 32:
/*      */       case 34:
/*  291 */         typeDcl(paramModuleEntry);
/*  292 */         break;
/*      */       case 5:
/*  294 */         constDcl(paramModuleEntry);
/*  295 */         break;
/*      */       case 19:
/*  297 */         nativeDcl(paramModuleEntry);
/*  298 */         break;
/*      */       case 10:
/*  300 */         exceptDcl(paramModuleEntry);
/*  301 */         break;
/*      */       case 16:
/*  303 */         interfaceProd(paramModuleEntry, 0);
/*  304 */         break;
/*      */       case 55:
/*  306 */         match(55);
/*  307 */         if (this.token.type == 16)
/*  308 */           interfaceProd(paramModuleEntry, 2);
/*      */         else {
/*  310 */           throw ParseException.syntaxError(this.scanner, new int[] { 16 }, this.token.type);
/*      */         }
/*      */         break;
/*      */       case 18:
/*  314 */         module(paramModuleEntry);
/*  315 */         break;
/*      */       case 39:
/*  317 */         match(39);
/*  318 */         if (this.token.type == 16)
/*  319 */           interfaceProd(paramModuleEntry, 1);
/*  320 */         else if (this.token.type == 46)
/*  321 */           valueProd(paramModuleEntry, true);
/*      */         else {
/*  323 */           throw ParseException.syntaxError(this.scanner, new int[] { 16, 46 }, this.token.type);
/*      */         }
/*      */         break;
/*      */       case 40:
/*      */       case 46:
/*  328 */         valueProd(paramModuleEntry, false);
/*  329 */         break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 17:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 30:
/*      */       case 31:
/*      */       case 33:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 38:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 47:
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       default:
/*  331 */         throw ParseException.syntaxError(this.scanner, new int[] { 32, 29, 34, 9, 5, 10, 16, 46, 18 }, this.token.type);
/*      */       }
/*      */ 
/*  336 */       match(100);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*  340 */       skipToSemicolon();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void module(ModuleEntry paramModuleEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  349 */     match(18);
/*  350 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/*  351 */     ModuleEntry localModuleEntry = newModule(paramModuleEntry);
/*  352 */     ((IDLID)repIDStack.peek()).appendToName(localModuleEntry.name());
/*      */ 
/*  354 */     localModuleEntry.comment(this.tokenHistory.lookBack(1).comment);
/*  355 */     this.currentModule = localModuleEntry;
/*  356 */     match(80);
/*  357 */     this.prep.openScope(localModuleEntry);
/*  358 */     match(101);
/*  359 */     definition(localModuleEntry);
/*  360 */     while ((!this.token.equals(999)) && (!this.token.equals(102)))
/*  361 */       definition(localModuleEntry);
/*  362 */     this.prep.closeScope(localModuleEntry);
/*  363 */     match(102);
/*  364 */     this.currentModule = paramModuleEntry;
/*  365 */     repIDStack.pop();
/*      */   }
/*      */ 
/*      */   private void interfaceProd(ModuleEntry paramModuleEntry, int paramInt)
/*      */     throws IOException, ParseException
/*      */   {
/*  374 */     match(16);
/*  375 */     String str = this.token.name;
/*  376 */     match(80);
/*  377 */     interface2(paramModuleEntry, str, paramInt);
/*      */   }
/*      */ 
/*      */   private void interface2(ModuleEntry paramModuleEntry, String paramString, int paramInt)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/*  386 */     if ((this.token.type == 103) || (this.token.type == 101)) {
/*  387 */       repIDStack.push(((IDLID)repIDStack.peek()).clone());
/*  388 */       localObject = this.stFactory.interfaceEntry(paramModuleEntry, 
/*  389 */         (IDLID)repIDStack
/*  389 */         .peek());
/*  390 */       ((InterfaceEntry)localObject).sourceFile(this.scanner.fileEntry());
/*  391 */       ((InterfaceEntry)localObject).name(paramString);
/*  392 */       ((InterfaceEntry)localObject).setInterfaceType(paramInt);
/*      */ 
/*  394 */       ((InterfaceEntry)localObject).comment(this.tokenHistory.lookBack(((InterfaceEntry)localObject)
/*  395 */         .getInterfaceType() == 0 ? 2 : 3).comment);
/*      */ 
/*  397 */       if (!ForwardEntry.replaceForwardDecl((InterfaceEntry)localObject))
/*  398 */         ParseException.badAbstract(this.scanner, ((InterfaceEntry)localObject).fullName());
/*  399 */       pigeonhole(paramModuleEntry, (SymtabEntry)localObject);
/*  400 */       ((IDLID)repIDStack.peek()).appendToName(paramString);
/*  401 */       this.currentModule = ((SymtabEntry)localObject);
/*  402 */       interfaceDcl((InterfaceEntry)localObject);
/*  403 */       this.currentModule = paramModuleEntry;
/*  404 */       repIDStack.pop();
/*      */     } else {
/*  406 */       localObject = this.stFactory.forwardEntry(paramModuleEntry, (IDLID)repIDStack.peek());
/*  407 */       ((ForwardEntry)localObject).sourceFile(this.scanner.fileEntry());
/*  408 */       ((ForwardEntry)localObject).name(paramString);
/*  409 */       ((ForwardEntry)localObject).setInterfaceType(paramInt);
/*      */ 
/*  411 */       ((ForwardEntry)localObject).comment(this.tokenHistory.lookBack(((ForwardEntry)localObject)
/*  412 */         .getInterfaceType() == 0 ? 2 : 3).comment);
/*  413 */       pigeonhole(paramModuleEntry, (SymtabEntry)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void interfaceDcl(InterfaceEntry paramInterfaceEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  422 */     if (this.token.type != 101) {
/*  423 */       inheritanceSpec(paramInterfaceEntry);
/*  424 */     } else if (!paramInterfaceEntry.isAbstract()) {
/*  425 */       SymtabEntry localSymtabEntry1 = qualifiedEntry("Object");
/*  426 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/*  427 */       if (localSymtabEntry1 != null)
/*      */       {
/*  429 */         if (!isInterface(localSymtabEntry2))
/*  430 */           ParseException.wrongType(this.scanner, overrideName("Object"), "interface", localSymtabEntry1
/*  431 */             .typeName());
/*      */         else
/*  433 */           paramInterfaceEntry.derivedFromAddElement(localSymtabEntry2, this.scanner);
/*      */       }
/*      */     }
/*  436 */     this.prep.openScope(paramInterfaceEntry);
/*  437 */     match(101);
/*  438 */     while (this.token.type != 102)
/*  439 */       export(paramInterfaceEntry);
/*  440 */     this.prep.closeScope(paramInterfaceEntry);
/*  441 */     match(102);
/*      */   }
/*      */ 
/*      */   private void export(InterfaceEntry paramInterfaceEntry)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  451 */       switch (this.token.type)
/*      */       {
/*      */       case 9:
/*      */       case 29:
/*      */       case 32:
/*      */       case 34:
/*  457 */         typeDcl(paramInterfaceEntry);
/*  458 */         break;
/*      */       case 5:
/*  460 */         constDcl(paramInterfaceEntry);
/*  461 */         break;
/*      */       case 19:
/*  463 */         nativeDcl(paramInterfaceEntry);
/*  464 */         break;
/*      */       case 10:
/*  466 */         exceptDcl(paramInterfaceEntry);
/*  467 */         break;
/*      */       case 1:
/*      */       case 25:
/*  470 */         attrDcl(paramInterfaceEntry);
/*  471 */         break;
/*      */       case 0:
/*      */       case 2:
/*      */       case 4:
/*      */       case 8:
/*      */       case 13:
/*      */       case 17:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 27:
/*      */       case 28:
/*      */       case 33:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 45:
/*      */       case 80:
/*      */       case 124:
/*  491 */         opDcl(paramInterfaceEntry);
/*  492 */         break;
/*      */       case 3:
/*      */       case 6:
/*      */       case 7:
/*      */       case 11:
/*      */       case 12:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 18:
/*      */       case 23:
/*      */       case 24:
/*      */       case 26:
/*      */       case 30:
/*      */       case 31:
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 46:
/*      */       case 47:
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 63:
/*      */       case 64:
/*      */       case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 95:
/*      */       case 96:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/*      */       default:
/*  498 */         throw ParseException.syntaxError(this.scanner, new int[] { 32, 29, 34, 9, 5, 10, 25, 1, 22, 13, 8, 17, 27, 33, 4, 36, 2, 21, 0, 28, 37, 80, 124, 35, 45 }, this.token.type);
/*      */       }
/*      */ 
/*  507 */       match(100);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*  511 */       skipToSemicolon();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void inheritanceSpec(InterfaceEntry paramInterfaceEntry) throws IOException, ParseException
/*      */   {
/*  517 */     for (match(103); ; match(104)) {
/*  518 */       SymtabEntry localSymtabEntry1 = scopedName(paramInterfaceEntry.container(), this.stFactory
/*  519 */         .interfaceEntry());
/*  520 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/*      */ 
/*  522 */       if (isInterfaceOnly(localSymtabEntry2)) {
/*  523 */         boolean bool = localSymtabEntry2 instanceof InterfaceEntry;
/*  524 */         if (paramInterfaceEntry.derivedFrom().contains(localSymtabEntry2))
/*  525 */           ParseException.alreadyDerived(this.scanner, localSymtabEntry2.fullName(), paramInterfaceEntry.fullName());
/*  526 */         else if ((!paramInterfaceEntry.isAbstract()) || 
/*  527 */           (((InterfaceType)localSymtabEntry2)
/*  527 */           .getInterfaceType() == 1))
/*  528 */           paramInterfaceEntry.derivedFromAddElement(localSymtabEntry2, this.scanner);
/*      */         else
/*  530 */           ParseException.nonAbstractParent(this.scanner, paramInterfaceEntry.fullName(), localSymtabEntry1.fullName());
/*  531 */       } else if (isForward(localSymtabEntry2)) {
/*  532 */         ParseException.illegalForwardInheritance(this.scanner, paramInterfaceEntry
/*  533 */           .fullName(), localSymtabEntry1.fullName());
/*      */       } else {
/*  535 */         ParseException.wrongType(this.scanner, localSymtabEntry1.fullName(), "interface", entryName(localSymtabEntry1));
/*      */       }
/*  537 */       if (((localSymtabEntry1 instanceof InterfaceEntry)) && (((InterfaceEntry)localSymtabEntry1).state() != null)) {
/*  538 */         if (paramInterfaceEntry.state() == null)
/*  539 */           paramInterfaceEntry.initState();
/*      */         else
/*  541 */           throw ParseException.badState(this.scanner, paramInterfaceEntry.fullName());
/*      */       }
/*  543 */       if (this.token.type != 104)
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isModuleLegalType()
/*      */   {
/*  563 */     return this._isModuleLegalType;
/*      */   }
/*      */ 
/*      */   public void isModuleLegalType(boolean paramBoolean)
/*      */   {
/*  571 */     this._isModuleLegalType = paramBoolean;
/*      */   }
/*      */ 
/*      */   SymtabEntry scopedName(SymtabEntry paramSymtabEntry1, SymtabEntry paramSymtabEntry2)
/*      */     throws IOException, ParseException
/*      */   {
/*  580 */     return scopedName(paramSymtabEntry1, paramSymtabEntry2, true);
/*      */   }
/*      */ 
/*      */   SymtabEntry scopedName(SymtabEntry paramSymtabEntry1, SymtabEntry paramSymtabEntry2, boolean paramBoolean)
/*      */     throws IOException, ParseException
/*      */   {
/*  586 */     int i = 0;
/*  587 */     int j = 0;
/*  588 */     String str = null;
/*  589 */     if (this.token.type == 124) {
/*  590 */       i = 1;
/*      */     }
/*  593 */     else if (this.token.type == 20)
/*      */     {
/*  595 */       str = "Object";
/*  596 */       match(20);
/*      */     }
/*  598 */     else if (this.token.type == 45)
/*      */     {
/*  600 */       str = "ValueBase";
/*  601 */       match(45);
/*      */     }
/*      */     else
/*      */     {
/*  605 */       str = this.token.name;
/*  606 */       match(80);
/*      */     }
/*      */ 
/*  609 */     while (this.token.type == 124)
/*      */     {
/*  611 */       match(124);
/*  612 */       j = 1;
/*  613 */       if (str != null)
/*  614 */         str = str + '/' + this.token.name;
/*  615 */       else str = this.token.name;
/*  616 */       match(80);
/*      */     }
/*  618 */     SymtabEntry localSymtabEntry = null;
/*  619 */     if (i != 0)
/*  620 */       localSymtabEntry = qualifiedEntry(str);
/*  621 */     else if (j != 0)
/*  622 */       localSymtabEntry = partlyQualifiedEntry(str, paramSymtabEntry1);
/*      */     else {
/*  624 */       localSymtabEntry = unqualifiedEntry(str, paramSymtabEntry1);
/*      */     }
/*  626 */     if (localSymtabEntry == null)
/*      */     {
/*  631 */       (localSymtabEntry = paramSymtabEntry2).name(str);
/*  632 */     } else if ((!localSymtabEntry.isReferencable()) && (paramBoolean)) {
/*  633 */       throw ParseException.illegalIncompleteTypeReference(this.scanner, str);
/*      */     }
/*  635 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   private void valueProd(ModuleEntry paramModuleEntry, boolean paramBoolean) throws IOException, ParseException
/*      */   {
/*  640 */     boolean bool = this.token.type == 40;
/*  641 */     if (bool)
/*  642 */       match(40);
/*  643 */     match(46);
/*  644 */     String str = this.token.name;
/*  645 */     match(80);
/*      */ 
/*  647 */     switch (this.token.type)
/*      */     {
/*      */     case 43:
/*      */     case 101:
/*      */     case 103:
/*  652 */       value2(paramModuleEntry, str, paramBoolean, bool);
/*  653 */       return;
/*      */     case 100:
/*  655 */       if (!bool) {
/*  657 */         valueForwardDcl(paramModuleEntry, str, paramBoolean);
/*      */         return;
/*      */       }break;
/*  660 */     }if (bool)
/*  661 */       throw ParseException.badCustom(this.scanner);
/*  662 */     if (paramBoolean)
/*  663 */       throw ParseException.abstractValueBox(this.scanner);
/*  664 */     valueBox(paramModuleEntry, str);
/*      */   }
/*      */ 
/*      */   private void value2(ModuleEntry paramModuleEntry, String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IOException, ParseException
/*      */   {
/*  673 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/*      */ 
/*  677 */     ValueEntry localValueEntry = this.stFactory.valueEntry(paramModuleEntry, (IDLID)repIDStack.peek());
/*  678 */     localValueEntry.sourceFile(this.scanner.fileEntry());
/*  679 */     localValueEntry.name(paramString);
/*  680 */     localValueEntry.setInterfaceType(paramBoolean1 ? 1 : 0);
/*  681 */     localValueEntry.setCustom(paramBoolean2);
/*      */ 
/*  683 */     localValueEntry.comment(this.tokenHistory.lookBack((paramBoolean1) || (paramBoolean2) ? 3 : 2).comment);
/*      */ 
/*  687 */     if (!ForwardEntry.replaceForwardDecl(localValueEntry))
/*  688 */       ParseException.badAbstract(this.scanner, localValueEntry.fullName());
/*  689 */     pigeonhole(paramModuleEntry, localValueEntry);
/*  690 */     ((IDLID)repIDStack.peek()).appendToName(paramString);
/*  691 */     this.currentModule = localValueEntry;
/*  692 */     valueDcl(localValueEntry);
/*  693 */     localValueEntry.tagMethods();
/*  694 */     this.currentModule = paramModuleEntry;
/*  695 */     repIDStack.pop();
/*      */   }
/*      */ 
/*      */   private void valueDcl(ValueEntry paramValueEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  703 */     if (this.token.type == 103) {
/*  704 */       valueInheritanceSpec(paramValueEntry);
/*  705 */     } else if (!paramValueEntry.isAbstract())
/*      */     {
/*  707 */       SymtabEntry localSymtabEntry1 = qualifiedEntry("ValueBase");
/*  708 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/*  709 */       if (localSymtabEntry1 != null)
/*      */       {
/*  711 */         if (!isValue(localSymtabEntry2))
/*  712 */           ParseException.wrongType(this.scanner, overrideName("ValueBase"), "value", localSymtabEntry1.typeName());
/*      */         else
/*  714 */           paramValueEntry.derivedFromAddElement(localSymtabEntry2, false, this.scanner); 
/*      */       }
/*      */     }
/*  716 */     if (this.token.type == 43)
/*  717 */       valueSupportsSpec(paramValueEntry);
/*  718 */     this.prep.openScope(paramValueEntry);
/*  719 */     match(101);
/*  720 */     while (this.token.type != 102)
/*      */     {
/*  722 */       valueElement(paramValueEntry);
/*      */     }
/*  724 */     this.prep.closeScope(paramValueEntry);
/*  725 */     match(102);
/*      */   }
/*      */ 
/*      */   private void valueInheritanceSpec(ValueEntry paramValueEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  733 */     match(103);
/*  734 */     boolean bool = this.token.type == 44;
/*  735 */     if (bool)
/*  736 */       match(44);
/*      */     while (true) {
/*  738 */       SymtabEntry localSymtabEntry1 = scopedName(paramValueEntry.container(), this.stFactory
/*  739 */         .valueEntry());
/*  740 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/*  741 */       if ((isValue(localSymtabEntry2)) && (!(localSymtabEntry2 instanceof ValueBoxEntry))) {
/*  742 */         paramValueEntry.derivedFromAddElement(localSymtabEntry2, bool, this.scanner);
/*      */       }
/*  744 */       else if (isForward(localSymtabEntry2))
/*  745 */         ParseException.illegalForwardInheritance(this.scanner, paramValueEntry
/*  746 */           .fullName(), localSymtabEntry1.fullName());
/*      */       else
/*  748 */         ParseException.wrongType(this.scanner, localSymtabEntry1
/*  749 */           .fullName(), "value", entryName(localSymtabEntry1));
/*  750 */       if (this.token.type != 104)
/*      */         break;
/*  737 */       match(104); bool = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void valueSupportsSpec(ValueEntry paramValueEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  760 */     match(43);
/*  761 */     for (; ; match(104)) {
/*  762 */       SymtabEntry localSymtabEntry1 = scopedName(paramValueEntry.container(), this.stFactory.interfaceEntry());
/*  763 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/*  764 */       if (isInterface(localSymtabEntry2))
/*  765 */         paramValueEntry.derivedFromAddElement(localSymtabEntry2, this.scanner);
/*      */       else {
/*  767 */         ParseException.wrongType(this.scanner, localSymtabEntry1.fullName(), "interface", 
/*  768 */           entryName(localSymtabEntry1));
/*      */       }
/*      */ 
/*  770 */       if (this.token.type != 104)
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void valueElement(ValueEntry paramValueEntry) throws IOException, ParseException
/*      */   {
/*  777 */     if (paramValueEntry.isAbstract())
/*  778 */       export(paramValueEntry);
/*      */     else
/*  780 */       switch (this.token.type)
/*      */       {
/*      */       case 41:
/*      */       case 42:
/*  784 */         valueStateMember(paramValueEntry);
/*  785 */         break;
/*      */       case 38:
/*      */       case 47:
/*  788 */         initDcl(paramValueEntry);
/*  789 */         break;
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 4:
/*      */       case 5:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 13:
/*      */       case 17:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 25:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 32:
/*      */       case 33:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 45:
/*      */       case 80:
/*      */       case 124:
/*  817 */         export(paramValueEntry);
/*  818 */         break;
/*      */       case 3:
/*      */       case 6:
/*      */       case 7:
/*      */       case 11:
/*      */       case 12:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 18:
/*      */       case 23:
/*      */       case 24:
/*      */       case 26:
/*      */       case 30:
/*      */       case 31:
/*      */       case 39:
/*      */       case 40:
/*      */       case 43:
/*      */       case 44:
/*      */       case 46:
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 63:
/*      */       case 64:
/*      */       case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 95:
/*      */       case 96:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 123:
/*      */       default:
/*  820 */         throw ParseException.syntaxError(this.scanner, new int[] { 41, 42, 38, 45, 32, 29, 34, 9, 5, 10, 25, 1, 22, 13, 8, 17, 27, 33, 4, 36, 2, 21, 0, 28, 37, 80, 124, 35 }, this.token.type);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void valueStateMember(ValueEntry paramValueEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  839 */     TypedefEntry localTypedefEntry = this.stFactory
/*  839 */       .typedefEntry(paramValueEntry, 
/*  839 */       (IDLID)repIDStack
/*  839 */       .peek());
/*  840 */     localTypedefEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/*  842 */     localTypedefEntry.comment(this.token.comment);
/*  843 */     boolean bool = this.token.type == 42;
/*  844 */     if (bool)
/*  845 */       match(42);
/*      */     else {
/*  847 */       match(41);
/*      */     }
/*      */ 
/*  850 */     int i = (this.token.type == 29) || (this.token.type == 34) || (this.token.type == 9) ? 1 : 0;
/*      */ 
/*  854 */     localTypedefEntry.name("");
/*  855 */     localTypedefEntry.type(typeSpec(localTypedefEntry));
/*  856 */     addDeclarators(paramValueEntry, localTypedefEntry, bool);
/*      */ 
/*  858 */     if (i != 0)
/*  859 */       paramValueEntry.addContained(localTypedefEntry);
/*  860 */     match(100);
/*      */   }
/*      */ 
/*      */   private void addDeclarators(ValueEntry paramValueEntry, TypedefEntry paramTypedefEntry, boolean paramBoolean)
/*      */     throws IOException, ParseException
/*      */   {
/*  867 */     int i = paramBoolean ? 2 : 0;
/*      */     try
/*      */     {
/*  870 */       Vector localVector = new Vector();
/*  871 */       declarators(paramTypedefEntry, localVector);
/*  872 */       for (localEnumeration = localVector.elements(); localEnumeration.hasMoreElements(); )
/*  873 */         paramValueEntry.addStateElement(new InterfaceState(i, 
/*  874 */           (TypedefEntry)localEnumeration
/*  874 */           .nextElement()), this.scanner);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*      */       Enumeration localEnumeration;
/*  878 */       skipToSemicolon();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initDcl(ValueEntry paramValueEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  887 */     MethodEntry localMethodEntry = this.stFactory.methodEntry(paramValueEntry, (IDLID)repIDStack.peek());
/*  888 */     localMethodEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/*  890 */     localMethodEntry.comment(this.token.comment);
/*  891 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/*  892 */     ((IDLID)repIDStack.peek()).appendToName(this.token.name);
/*      */ 
/*  895 */     if (this.token.type == 38)
/*      */     {
/*  897 */       localMethodEntry.name("init");
/*  898 */       match(38);
/*  899 */       match(108);
/*      */     }
/*      */     else
/*      */     {
/*  903 */       match(47);
/*  904 */       localMethodEntry.name(this.token.name);
/*  905 */       if (this.token.type == 81) {
/*  906 */         match(81);
/*      */       }
/*      */       else {
/*  909 */         match(80);
/*  910 */         match(108);
/*      */       }
/*      */     }
/*      */ 
/*  914 */     if (this.token.type != 109)
/*      */       while (true)
/*      */       {
/*  917 */         initParamDcl(localMethodEntry);
/*  918 */         if (this.token.type == 109)
/*      */           break;
/*  920 */         match(104);
/*      */       }
/*  922 */     paramValueEntry.initializersAddElement(localMethodEntry, this.scanner);
/*  923 */     match(109);
/*  924 */     match(100);
/*  925 */     repIDStack.pop();
/*      */   }
/*      */ 
/*      */   private void initParamDcl(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  933 */     ParameterEntry localParameterEntry = this.stFactory.parameterEntry(paramMethodEntry, (IDLID)repIDStack.peek());
/*  934 */     localParameterEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/*  936 */     localParameterEntry.comment(this.token.comment);
/*  937 */     match(14);
/*  938 */     localParameterEntry.passType(0);
/*  939 */     localParameterEntry.type(paramTypeSpec(paramMethodEntry));
/*  940 */     localParameterEntry.name(this.token.name);
/*  941 */     match(80);
/*  942 */     if (isntInList(paramMethodEntry.parameters(), localParameterEntry.name()))
/*  943 */       paramMethodEntry.addParameter(localParameterEntry);
/*      */   }
/*      */ 
/*      */   private void valueBox(ModuleEntry paramModuleEntry, String paramString)
/*      */     throws IOException, ParseException
/*      */   {
/*  951 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/*      */ 
/*  955 */     ValueBoxEntry localValueBoxEntry = this.stFactory.valueBoxEntry(paramModuleEntry, (IDLID)repIDStack.peek());
/*  956 */     localValueBoxEntry.sourceFile(this.scanner.fileEntry());
/*  957 */     localValueBoxEntry.name(paramString);
/*      */ 
/*  959 */     localValueBoxEntry.comment(this.tokenHistory.lookBack(2).comment);
/*      */ 
/*  966 */     SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(localValueBoxEntry.fullName());
/*  967 */     if ((localSymtabEntry != null) && ((localSymtabEntry instanceof ForwardEntry)))
/*  968 */       ParseException.forwardedValueBox(this.scanner, localValueBoxEntry.fullName());
/*  969 */     pigeonhole(paramModuleEntry, localValueBoxEntry);
/*  970 */     ((IDLID)repIDStack.peek()).appendToName(paramString);
/*  971 */     this.currentModule = localValueBoxEntry;
/*  972 */     TypedefEntry localTypedefEntry = this.stFactory.typedefEntry(localValueBoxEntry, (IDLID)repIDStack.peek());
/*  973 */     localTypedefEntry.sourceFile(this.scanner.fileEntry());
/*  974 */     localTypedefEntry.comment(this.token.comment);
/*      */ 
/*  979 */     int i = (this.token.type == 29) || (this.token.type == 34) || (this.token.type == 9) ? 1 : 0;
/*      */ 
/*  983 */     localTypedefEntry.name("");
/*  984 */     localTypedefEntry.type(typeSpec(localTypedefEntry));
/*      */ 
/*  986 */     if ((localTypedefEntry.type() instanceof ValueBoxEntry)) {
/*  987 */       ParseException.nestedValueBox(this.scanner);
/*      */     }
/*  989 */     localValueBoxEntry.addStateElement(new InterfaceState(2, localTypedefEntry), this.scanner);
/*  990 */     if (i != 0)
/*  991 */       localValueBoxEntry.addContained(localTypedefEntry);
/*  992 */     this.currentModule = paramModuleEntry;
/*  993 */     repIDStack.pop();
/*      */   }
/*      */ 
/*      */   private void valueForwardDcl(ModuleEntry paramModuleEntry, String paramString, boolean paramBoolean)
/*      */     throws IOException, ParseException
/*      */   {
/* 1002 */     ForwardValueEntry localForwardValueEntry = this.stFactory.forwardValueEntry(paramModuleEntry, (IDLID)repIDStack.peek());
/* 1003 */     localForwardValueEntry.sourceFile(this.scanner.fileEntry());
/* 1004 */     localForwardValueEntry.name(paramString);
/* 1005 */     localForwardValueEntry.setInterfaceType(paramBoolean ? 1 : 0);
/*      */ 
/* 1007 */     localForwardValueEntry.comment(this.tokenHistory.lookBack(paramBoolean ? 3 : 2).comment);
/* 1008 */     pigeonhole(paramModuleEntry, localForwardValueEntry);
/*      */   }
/*      */ 
/*      */   private void nativeDcl(SymtabEntry paramSymtabEntry) throws IOException, ParseException
/*      */   {
/* 1013 */     match(19);
/* 1014 */     NativeEntry localNativeEntry = this.stFactory.nativeEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 1015 */     localNativeEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 1017 */     localNativeEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 1018 */     localNativeEntry.name(this.token.name);
/* 1019 */     match(80);
/* 1020 */     pigeonhole(paramSymtabEntry, localNativeEntry);
/*      */   }
/*      */ 
/*      */   private void constDcl(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1027 */     match(5);
/* 1028 */     ConstEntry localConstEntry = this.stFactory.constEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 1029 */     localConstEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 1031 */     localConstEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 1032 */     constType(localConstEntry);
/* 1033 */     localConstEntry.name(this.token.name);
/* 1034 */     match(80);
/* 1035 */     match(105);
/* 1036 */     localConstEntry.value(constExp(localConstEntry));
/* 1037 */     verifyConstType(localConstEntry.value(), typeOf(localConstEntry.type()));
/* 1038 */     pigeonhole(paramSymtabEntry, localConstEntry);
/*      */   }
/*      */ 
/*      */   private void constType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1046 */     switch (this.token.type)
/*      */     {
/*      */     case 21:
/* 1049 */       paramSymtabEntry.type(octetType());
/* 1050 */       break;
/*      */     case 17:
/*      */     case 27:
/*      */     case 33:
/* 1054 */       paramSymtabEntry.type(integerType(paramSymtabEntry));
/* 1055 */       break;
/*      */     case 4:
/*      */     case 36:
/* 1058 */       paramSymtabEntry.type(charType());
/* 1059 */       break;
/*      */     case 2:
/* 1061 */       paramSymtabEntry.type(booleanType());
/* 1062 */       break;
/*      */     case 8:
/*      */     case 13:
/* 1065 */       paramSymtabEntry.type(floatingPtType());
/* 1066 */       break;
/*      */     case 28:
/*      */     case 37:
/* 1069 */       paramSymtabEntry.type(stringType(paramSymtabEntry));
/* 1070 */       break;
/*      */     case 80:
/*      */     case 124:
/* 1073 */       paramSymtabEntry.type(scopedName(paramSymtabEntry.container(), this.stFactory.primitiveEntry()));
/* 1074 */       if (hasArrayInfo(paramSymtabEntry.type()))
/* 1075 */         ParseException.illegalArray(this.scanner, "const");
/* 1076 */       SymtabEntry localSymtabEntry = typeOf(paramSymtabEntry.type());
/* 1077 */       if ((!(localSymtabEntry instanceof PrimitiveEntry)) && (!(localSymtabEntry instanceof StringEntry)))
/*      */       {
/* 1079 */         ParseException.wrongType(this.scanner, paramSymtabEntry.fullName(), "primitive or string", entryName(paramSymtabEntry.type()));
/* 1080 */         paramSymtabEntry.type(qualifiedEntry("long"));
/*      */       }
/* 1082 */       else if ((localSymtabEntry instanceof PrimitiveEntry))
/*      */       {
/* 1084 */         String str = overrideName("any");
/* 1085 */         if (localSymtabEntry.name().equals(str))
/*      */         {
/* 1087 */           ParseException.wrongType(this.scanner, paramSymtabEntry.fullName(), "primitive or string (except " + str + ')', str);
/* 1088 */           paramSymtabEntry.type(qualifiedEntry("long"));
/*      */         }
/*      */       }
/* 1090 */       break;
/*      */     default:
/* 1093 */       throw ParseException.syntaxError(this.scanner, new int[] { 17, 27, 33, 4, 36, 2, 13, 8, 28, 37, 80, 124 }, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean hasArrayInfo(SymtabEntry paramSymtabEntry)
/*      */   {
/* 1106 */     while ((paramSymtabEntry instanceof TypedefEntry))
/*      */     {
/* 1108 */       if (((TypedefEntry)paramSymtabEntry).arrayInfo().size() != 0)
/* 1109 */         return true;
/* 1110 */       paramSymtabEntry = paramSymtabEntry.type();
/*      */     }
/* 1112 */     return false;
/*      */   }
/*      */ 
/*      */   public static String overrideName(String paramString)
/*      */   {
/* 1120 */     String str = (String)overrideNames.get(paramString);
/* 1121 */     return str == null ? paramString : str;
/*      */   }
/*      */ 
/*      */   private void verifyConstType(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1134 */     Object localObject = paramExpression.value();
/* 1135 */     if ((localObject instanceof BigInteger))
/* 1136 */       verifyIntegral((Number)localObject, paramSymtabEntry);
/* 1137 */     else if ((localObject instanceof String))
/* 1138 */       verifyString(paramExpression, paramSymtabEntry);
/* 1139 */     else if ((localObject instanceof Boolean))
/* 1140 */       verifyBoolean(paramSymtabEntry);
/* 1141 */     else if ((localObject instanceof Character))
/* 1142 */       verifyCharacter(paramExpression, paramSymtabEntry);
/* 1143 */     else if (((localObject instanceof Float)) || ((localObject instanceof Double)))
/* 1144 */       verifyFloat((Number)localObject, paramSymtabEntry);
/* 1145 */     else if ((localObject instanceof ConstEntry))
/* 1146 */       verifyConstType(((ConstEntry)localObject).value(), paramSymtabEntry);
/*      */     else
/* 1148 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.fullName(), localObject == null ? "" : localObject
/* 1149 */         .toString());
/*      */   }
/*      */ 
/*      */   private void verifyIntegral(Number paramNumber, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1161 */     int i = 0;
/*      */ 
/* 1165 */     if (paramSymtabEntry == qualifiedEntry("octet")) {
/* 1166 */       if ((paramNumber.longValue() > 255L) || (paramNumber.longValue() < 0L))
/* 1167 */         i = 1;
/* 1168 */     } else if (paramSymtabEntry == qualifiedEntry("long")) {
/* 1169 */       if ((paramNumber.longValue() > 2147483647L) || (paramNumber.longValue() < -2147483648L))
/* 1170 */         i = 1;
/* 1171 */     } else if (paramSymtabEntry == qualifiedEntry("short")) {
/* 1172 */       if ((paramNumber.intValue() > 32767) || (paramNumber.intValue() < -32768))
/* 1173 */         i = 1;
/* 1174 */     } else if (paramSymtabEntry == qualifiedEntry("unsigned long")) {
/* 1175 */       if ((paramNumber.longValue() > 4294967295L) || (paramNumber.longValue() < 0L))
/* 1176 */         i = 1;
/* 1177 */     } else if (paramSymtabEntry == qualifiedEntry("unsigned short")) {
/* 1178 */       if ((paramNumber.intValue() > 65535) || (paramNumber.intValue() < 0))
/* 1179 */         i = 1;
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject;
/*      */       BigInteger localBigInteger;
/* 1180 */       if (paramSymtabEntry == qualifiedEntry("long long"))
/*      */       {
/* 1183 */         localObject = BigInteger.valueOf(9223372036854775807L);
/* 1184 */         localBigInteger = BigInteger.valueOf(-9223372036854775808L);
/* 1185 */         if ((((BigInteger)paramNumber).compareTo((BigInteger)localObject) > 0) || 
/* 1186 */           (((BigInteger)paramNumber)
/* 1186 */           .compareTo(localBigInteger) < 0))
/*      */         {
/* 1187 */           i = 1;
/*      */         } } else if (paramSymtabEntry == qualifiedEntry("unsigned long long"))
/*      */       {
/* 1191 */         localObject = BigInteger.valueOf(9223372036854775807L)
/* 1190 */           .multiply(BigInteger.valueOf(2L))
/* 1191 */           .add(BigInteger.valueOf(1L));
/*      */ 
/* 1192 */         localBigInteger = BigInteger.valueOf(0L);
/* 1193 */         if ((((BigInteger)paramNumber).compareTo((BigInteger)localObject) > 0) || 
/* 1194 */           (((BigInteger)paramNumber)
/* 1194 */           .compareTo(localBigInteger) < 0))
/*      */         {
/* 1195 */           i = 1;
/*      */         }
/*      */       } else { localObject = null;
/*      */ 
/* 1207 */         localObject = "long";
/* 1208 */         ParseException.wrongExprType(this.scanner, paramSymtabEntry.fullName(), (String)localObject);
/*      */       }
/*      */     }
/* 1211 */     if (i != 0)
/* 1212 */       ParseException.outOfRange(this.scanner, paramNumber.toString(), paramSymtabEntry.fullName());
/*      */   }
/*      */ 
/*      */   private void verifyString(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1220 */     String str = (String)paramExpression.value();
/* 1221 */     if (!(paramSymtabEntry instanceof StringEntry)) {
/* 1222 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.fullName(), paramExpression.type());
/* 1223 */     } else if (((StringEntry)paramSymtabEntry).maxSize() != null) {
/* 1224 */       Expression localExpression = ((StringEntry)paramSymtabEntry).maxSize();
/*      */       try {
/* 1226 */         Number localNumber = (Number)localExpression.value();
/* 1227 */         if (str.length() > localNumber.intValue()) {
/* 1228 */           ParseException.stringTooLong(this.scanner, str, localNumber.toString());
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1236 */     if (!paramExpression.type().equals(paramSymtabEntry.name()))
/*      */     {
/* 1238 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.name(), paramExpression.type());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void verifyBoolean(SymtabEntry paramSymtabEntry)
/*      */   {
/* 1247 */     if (!paramSymtabEntry.name().equals(overrideName("boolean")))
/* 1248 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.name(), "boolean");
/*      */   }
/*      */ 
/*      */   private void verifyCharacter(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1258 */     if (((!paramSymtabEntry.name().equals(overrideName("char"))) && 
/* 1259 */       (!paramSymtabEntry
/* 1259 */       .name().equals(overrideName("wchar")))) || 
/* 1260 */       (!paramSymtabEntry
/* 1260 */       .name().equals(paramExpression.type())))
/* 1261 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.fullName(), paramExpression.type());
/*      */   }
/*      */ 
/*      */   private void verifyFloat(Number paramNumber, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1276 */     int i = 0;
/* 1277 */     if (paramSymtabEntry.name().equals(overrideName("float")))
/*      */     {
/* 1280 */       double d = paramNumber.doubleValue() < 0.0D ? paramNumber
/* 1280 */         .doubleValue() * -1.0D : paramNumber.doubleValue();
/* 1281 */       if ((d != 0.0D) && ((d > 3.402823466385289E+038D) || (d < 1.401298464324817E-045D)))
/*      */       {
/* 1283 */         i = 1;
/*      */       }
/* 1285 */     } else if (!paramSymtabEntry.name().equals(overrideName("double")))
/*      */     {
/* 1293 */       ParseException.wrongExprType(this.scanner, paramSymtabEntry.fullName(), (paramNumber instanceof Float) ? "float" : "double");
/*      */     }
/*      */ 
/* 1296 */     if (i != 0)
/* 1297 */       ParseException.outOfRange(this.scanner, paramNumber.toString(), paramSymtabEntry.fullName());
/*      */   }
/*      */ 
/*      */   Expression constExp(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1306 */     Expression localExpression = orExpr(null, paramSymtabEntry);
/*      */ 
/* 1309 */     if (localExpression.type() == null) {
/* 1310 */       localExpression.type(paramSymtabEntry.typeName());
/*      */     }
/*      */     try
/*      */     {
/* 1314 */       localExpression.evaluate();
/*      */ 
/* 1318 */       if (((localExpression instanceof Terminal)) && 
/* 1319 */         ((localExpression
/* 1319 */         .value() instanceof BigInteger)) && (
/* 1320 */         (overrideName(localExpression
/* 1320 */         .type()).equals("float")) || 
/* 1321 */         (overrideName(localExpression
/* 1321 */         .type()).indexOf("double") >= 0)))
/*      */       {
/* 1323 */         localExpression.value(new Double(((BigInteger)localExpression.value()).doubleValue()));
/*      */       }
/*      */     }
/*      */     catch (EvaluationException localEvaluationException)
/*      */     {
/* 1328 */       ParseException.evaluationError(this.scanner, localEvaluationException.toString());
/*      */     }
/* 1330 */     return localExpression;
/*      */   }
/*      */ 
/*      */   private Expression orExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1338 */     if (paramExpression == null) {
/* 1339 */       paramExpression = xorExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1342 */       localObject = (BinaryExpr)paramExpression;
/* 1343 */       ((BinaryExpr)localObject).right(xorExpr(null, paramSymtabEntry));
/* 1344 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1346 */     if (this.token.equals(117))
/*      */     {
/* 1348 */       match(this.token.type);
/* 1349 */       localObject = this.exprFactory.or(paramExpression, null);
/* 1350 */       ((Or)localObject).type(paramSymtabEntry.typeName());
/* 1351 */       ((Or)localObject).rep(paramExpression.rep() + " | ");
/* 1352 */       return orExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1354 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression xorExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1362 */     if (paramExpression == null) {
/* 1363 */       paramExpression = andExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1366 */       localObject = (BinaryExpr)paramExpression;
/* 1367 */       ((BinaryExpr)localObject).right(andExpr(null, paramSymtabEntry));
/* 1368 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1370 */     if (this.token.equals(118))
/*      */     {
/* 1372 */       match(this.token.type);
/* 1373 */       localObject = this.exprFactory.xor(paramExpression, null);
/* 1374 */       ((Xor)localObject).rep(paramExpression.rep() + " ^ ");
/* 1375 */       ((Xor)localObject).type(paramSymtabEntry.typeName());
/* 1376 */       return xorExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1378 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression andExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1386 */     if (paramExpression == null) {
/* 1387 */       paramExpression = shiftExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1390 */       localObject = (BinaryExpr)paramExpression;
/* 1391 */       ((BinaryExpr)localObject).right(shiftExpr(null, paramSymtabEntry));
/* 1392 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1394 */     if (this.token.equals(119))
/*      */     {
/* 1396 */       match(this.token.type);
/* 1397 */       localObject = this.exprFactory.and(paramExpression, null);
/* 1398 */       ((And)localObject).rep(paramExpression.rep() + " & ");
/* 1399 */       ((And)localObject).type(paramSymtabEntry.typeName());
/* 1400 */       return andExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1402 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression shiftExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1410 */     if (paramExpression == null) {
/* 1411 */       paramExpression = addExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1414 */       localObject = (BinaryExpr)paramExpression;
/* 1415 */       ((BinaryExpr)localObject).right(addExpr(null, paramSymtabEntry));
/* 1416 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1418 */     if (this.token.equals(125))
/*      */     {
/* 1420 */       match(this.token.type);
/* 1421 */       localObject = this.exprFactory.shiftLeft(paramExpression, null);
/* 1422 */       ((ShiftLeft)localObject).type(paramSymtabEntry.typeName());
/* 1423 */       ((ShiftLeft)localObject).rep(paramExpression.rep() + " << ");
/* 1424 */       return shiftExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1426 */     if (this.token.equals(126))
/*      */     {
/* 1428 */       match(this.token.type);
/* 1429 */       localObject = this.exprFactory.shiftRight(paramExpression, null);
/* 1430 */       ((ShiftRight)localObject).type(paramSymtabEntry.typeName());
/* 1431 */       ((ShiftRight)localObject).rep(paramExpression.rep() + " >> ");
/* 1432 */       return shiftExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1434 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression addExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1442 */     if (paramExpression == null) {
/* 1443 */       paramExpression = multExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1446 */       localObject = (BinaryExpr)paramExpression;
/* 1447 */       ((BinaryExpr)localObject).right(multExpr(null, paramSymtabEntry));
/* 1448 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1450 */     if (this.token.equals(106))
/*      */     {
/* 1452 */       match(this.token.type);
/* 1453 */       localObject = this.exprFactory.plus(paramExpression, null);
/* 1454 */       ((Plus)localObject).type(paramSymtabEntry.typeName());
/* 1455 */       ((Plus)localObject).rep(paramExpression.rep() + " + ");
/* 1456 */       return addExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1458 */     if (this.token.equals(107))
/*      */     {
/* 1460 */       match(this.token.type);
/* 1461 */       localObject = this.exprFactory.minus(paramExpression, null);
/* 1462 */       ((Minus)localObject).type(paramSymtabEntry.typeName());
/* 1463 */       ((Minus)localObject).rep(paramExpression.rep() + " - ");
/* 1464 */       return addExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1466 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression multExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/* 1474 */     if (paramExpression == null) {
/* 1475 */       paramExpression = unaryExpr(paramSymtabEntry);
/*      */     }
/*      */     else {
/* 1478 */       localObject = (BinaryExpr)paramExpression;
/* 1479 */       ((BinaryExpr)localObject).right(unaryExpr(paramSymtabEntry));
/* 1480 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/* 1482 */     if (this.token.equals(120))
/*      */     {
/* 1484 */       match(this.token.type);
/* 1485 */       localObject = this.exprFactory.times(paramExpression, null);
/* 1486 */       ((Times)localObject).type(paramSymtabEntry.typeName());
/* 1487 */       ((Times)localObject).rep(paramExpression.rep() + " * ");
/* 1488 */       return multExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1490 */     if (this.token.equals(121))
/*      */     {
/* 1492 */       match(this.token.type);
/* 1493 */       localObject = this.exprFactory.divide(paramExpression, null);
/* 1494 */       ((Divide)localObject).type(paramSymtabEntry.typeName());
/* 1495 */       ((Divide)localObject).rep(paramExpression.rep() + " / ");
/* 1496 */       return multExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1498 */     if (this.token.equals(122))
/*      */     {
/* 1500 */       match(this.token.type);
/* 1501 */       localObject = this.exprFactory.modulo(paramExpression, null);
/* 1502 */       ((Modulo)localObject).type(paramSymtabEntry.typeName());
/* 1503 */       ((Modulo)localObject).rep(paramExpression.rep() + " % ");
/* 1504 */       return multExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/* 1506 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression unaryExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Expression localExpression;
/*      */     Object localObject;
/* 1514 */     if (this.token.equals(106))
/*      */     {
/* 1516 */       match(this.token.type);
/* 1517 */       localExpression = primaryExpr(paramSymtabEntry);
/* 1518 */       localObject = this.exprFactory.positive(localExpression);
/* 1519 */       ((Positive)localObject).type(paramSymtabEntry.typeName());
/* 1520 */       ((Positive)localObject).rep('+' + localExpression.rep());
/* 1521 */       return localObject;
/*      */     }
/* 1523 */     if (this.token.equals(107))
/*      */     {
/* 1525 */       match(this.token.type);
/* 1526 */       localExpression = primaryExpr(paramSymtabEntry);
/* 1527 */       localObject = this.exprFactory.negative(localExpression);
/* 1528 */       ((Negative)localObject).type(paramSymtabEntry.typeName());
/* 1529 */       ((Negative)localObject).rep('-' + localExpression.rep());
/* 1530 */       return localObject;
/*      */     }
/* 1532 */     if (this.token.equals(123))
/*      */     {
/* 1534 */       match(this.token.type);
/* 1535 */       localExpression = primaryExpr(paramSymtabEntry);
/* 1536 */       localObject = this.exprFactory.not(localExpression);
/* 1537 */       ((Not)localObject).type(paramSymtabEntry.typeName());
/* 1538 */       ((Not)localObject).rep('~' + localExpression.rep());
/* 1539 */       return localObject;
/*      */     }
/* 1541 */     return primaryExpr(paramSymtabEntry);
/*      */   }
/*      */ 
/*      */   private Expression primaryExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1550 */     Object localObject = null;
/* 1551 */     if (this.parsingConditionalExpr)
/*      */     {
/* 1553 */       this.prep.token = this.token;
/* 1554 */       localObject = this.prep.primaryExpr(paramSymtabEntry);
/* 1555 */       this.token = this.prep.token;
/*      */     }
/*      */     else {
/* 1558 */       switch (this.token.type)
/*      */       {
/*      */       case 80:
/*      */       case 124:
/* 1562 */         ConstEntry localConstEntry = this.stFactory.constEntry();
/* 1563 */         localConstEntry.value(this.exprFactory.terminal("1", BigInteger.valueOf(1L)));
/* 1564 */         SymtabEntry localSymtabEntry = scopedName(paramSymtabEntry.container(), localConstEntry);
/* 1565 */         if (!(localSymtabEntry instanceof ConstEntry))
/*      */         {
/* 1567 */           ParseException.invalidConst(this.scanner, localSymtabEntry.fullName());
/*      */ 
/* 1570 */           localObject = this.exprFactory.terminal("1", BigInteger.valueOf(1L));
/*      */         }
/*      */         else {
/* 1573 */           localObject = this.exprFactory.terminal((ConstEntry)localSymtabEntry);
/* 1574 */         }break;
/*      */       case 200:
/*      */       case 201:
/*      */       case 202:
/*      */       case 203:
/*      */       case 204:
/* 1580 */         localObject = literal(paramSymtabEntry);
/* 1581 */         break;
/*      */       case 108:
/* 1583 */         match(108);
/* 1584 */         localObject = constExp(paramSymtabEntry);
/* 1585 */         match(109);
/* 1586 */         ((Expression)localObject).rep('(' + ((Expression)localObject).rep() + ')');
/* 1587 */         break;
/*      */       default:
/* 1589 */         throw ParseException.syntaxError(this.scanner, new int[] { 80, 124, 205, 108 }, this.token.type);
/*      */       }
/*      */     }
/*      */ 
/* 1593 */     return localObject;
/*      */   }
/*      */ 
/*      */   Expression literal(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1601 */     String str = this.token.name;
/* 1602 */     Object localObject = null;
/* 1603 */     switch (this.token.type)
/*      */     {
/*      */     case 202:
/* 1606 */       match(202);
/*      */       try
/*      */       {
/* 1609 */         localObject = this.exprFactory.terminal(str, parseString(str));
/* 1610 */         ((Expression)localObject).type(paramSymtabEntry.typeName());
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException1)
/*      */       {
/* 1614 */         ParseException.notANumber(this.scanner, str);
/* 1615 */         localObject = this.exprFactory.terminal("0", BigInteger.valueOf(0L));
/*      */       }
/*      */ 
/*      */     case 201:
/* 1619 */       boolean bool = this.token.isWide();
/* 1620 */       match(201);
/* 1621 */       localObject = this.exprFactory.terminal("'" + str.substring(1) + "'", new Character(str
/* 1622 */         .charAt(0)), 
/* 1622 */         bool);
/* 1623 */       break;
/*      */     case 203:
/* 1625 */       match(203);
/*      */       try
/*      */       {
/* 1628 */         localObject = this.exprFactory.terminal(str, new Double(str));
/* 1629 */         ((Expression)localObject).type(paramSymtabEntry.typeName());
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException2)
/*      */       {
/* 1633 */         ParseException.notANumber(this.scanner, str);
/*      */       }
/*      */ 
/*      */     case 200:
/* 1637 */       localObject = booleanLiteral();
/* 1638 */       break;
/*      */     case 204:
/* 1640 */       localObject = stringLiteral();
/* 1641 */       break;
/*      */     default:
/* 1643 */       throw ParseException.syntaxError(this.scanner, 205, this.token.type);
/*      */     }
/* 1645 */     return localObject;
/*      */   }
/*      */ 
/*      */   private BigInteger parseString(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/* 1653 */     int i = 10;
/* 1654 */     if ((paramString.length() > 1) && 
/* 1655 */       (paramString.charAt(0) == '0'))
/* 1656 */       if ((paramString.charAt(1) == 'x') || (paramString.charAt(1) == 'X'))
/*      */       {
/* 1658 */         paramString = paramString.substring(2);
/* 1659 */         i = 16;
/*      */       }
/*      */       else {
/* 1662 */         i = 8;
/*      */       }
/* 1663 */     return new BigInteger(paramString, i);
/*      */   }
/*      */ 
/*      */   private Terminal booleanLiteral()
/*      */     throws IOException, ParseException
/*      */   {
/* 1671 */     Boolean localBoolean = null;
/* 1672 */     if (this.token.name.equals("TRUE")) {
/* 1673 */       localBoolean = new Boolean(true);
/* 1674 */     } else if (this.token.name.equals("FALSE")) {
/* 1675 */       localBoolean = new Boolean(false);
/*      */     }
/*      */     else {
/* 1678 */       ParseException.invalidConst(this.scanner, this.token.name);
/* 1679 */       localBoolean = new Boolean(false);
/*      */     }
/* 1681 */     String str = this.token.name;
/* 1682 */     match(200);
/* 1683 */     return this.exprFactory.terminal(str, localBoolean);
/*      */   }
/*      */ 
/*      */   private Expression stringLiteral()
/*      */     throws IOException, ParseException
/*      */   {
/* 1695 */     boolean bool = this.token.isWide();
/* 1696 */     String str = "";
/*      */     do
/*      */     {
/* 1699 */       str = str + this.token.name;
/* 1700 */       match(204);
/* 1701 */     }while (this.token.equals(204));
/* 1702 */     Terminal localTerminal = this.exprFactory.terminal(str, bool);
/* 1703 */     localTerminal.rep('"' + str + '"');
/* 1704 */     return localTerminal;
/*      */   }
/*      */ 
/*      */   private Expression positiveIntConst(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1712 */     Object localObject1 = constExp(paramSymtabEntry);
/* 1713 */     Object localObject2 = ((Expression)localObject1).value();
/* 1714 */     while ((localObject2 instanceof ConstEntry))
/* 1715 */       localObject2 = ((ConstEntry)localObject2).value().value();
/* 1716 */     if ((!(localObject2 instanceof Number)) || ((localObject2 instanceof Float)) || ((localObject2 instanceof Double)))
/*      */     {
/* 1718 */       ParseException.notPositiveInt(this.scanner, ((Expression)localObject1).rep());
/*      */ 
/* 1720 */       localObject1 = this.exprFactory.terminal("1", BigInteger.valueOf(1L));
/*      */     }
/* 1725 */     else if (((BigInteger)localObject2).compareTo(BigInteger.valueOf(0L)) <= 0)
/*      */     {
/* 1727 */       ParseException.notPositiveInt(this.scanner, localObject2.toString());
/*      */ 
/* 1729 */       localObject1 = this.exprFactory.terminal("1", BigInteger.valueOf(1L));
/*      */     }
/* 1731 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private SymtabEntry typeDcl(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1739 */     switch (this.token.type)
/*      */     {
/*      */     case 32:
/* 1742 */       match(32);
/* 1743 */       return typeDeclarator(paramSymtabEntry);
/*      */     case 29:
/* 1745 */       return structType(paramSymtabEntry);
/*      */     case 34:
/* 1747 */       return unionType(paramSymtabEntry);
/*      */     case 9:
/* 1749 */       return enumType(paramSymtabEntry);
/*      */     }
/* 1751 */     throw ParseException.syntaxError(this.scanner, new int[] { 32, 29, 34, 9 }, this.token.type);
/*      */   }
/*      */ 
/*      */   private TypedefEntry typeDeclarator(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1761 */     TypedefEntry localTypedefEntry = this.stFactory.typedefEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 1762 */     localTypedefEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 1764 */     localTypedefEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 1765 */     localTypedefEntry.type(typeSpec(paramSymtabEntry));
/* 1766 */     Vector localVector = new Vector();
/* 1767 */     declarators(localTypedefEntry, localVector);
/* 1768 */     for (Enumeration localEnumeration = localVector.elements(); localEnumeration.hasMoreElements(); )
/* 1769 */       pigeonhole(paramSymtabEntry, (SymtabEntry)localEnumeration.nextElement());
/* 1770 */     return localTypedefEntry;
/*      */   }
/*      */ 
/*      */   private SymtabEntry typeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1782 */     return (this.token.type == 29) || (this.token.type == 34) || (this.token.type == 9) ? 
/* 1781 */       constrTypeSpec(paramSymtabEntry) : 
/* 1782 */       simpleTypeSpec(paramSymtabEntry, true);
/*      */   }
/*      */ 
/*      */   private SymtabEntry simpleTypeSpec(SymtabEntry paramSymtabEntry, boolean paramBoolean)
/*      */     throws IOException, ParseException
/*      */   {
/* 1795 */     if ((this.token.type == 80) || (this.token.type == 124) || (this.token.type == 20) || (this.token.type == 45))
/*      */     {
/* 1805 */       SymtabEntry localSymtabEntry = ((paramSymtabEntry instanceof InterfaceEntry)) || ((paramSymtabEntry instanceof ModuleEntry)) || ((paramSymtabEntry instanceof StructEntry)) || ((paramSymtabEntry instanceof UnionEntry)) ? paramSymtabEntry : paramSymtabEntry
/* 1805 */         .container();
/* 1806 */       return scopedName(localSymtabEntry, this.stFactory.primitiveEntry(), paramBoolean);
/*      */     }
/*      */ 
/* 1813 */     return (this.token.type == 26) || (this.token.type == 28) || (this.token.type == 37) ? 
/* 1812 */       templateTypeSpec(paramSymtabEntry) : 
/* 1813 */       baseTypeSpec(paramSymtabEntry);
/*      */   }
/*      */ 
/*      */   private SymtabEntry baseTypeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1821 */     switch (this.token.type)
/*      */     {
/*      */     case 8:
/*      */     case 13:
/* 1825 */       return floatingPtType();
/*      */     case 17:
/*      */     case 27:
/*      */     case 33:
/* 1829 */       return integerType(paramSymtabEntry);
/*      */     case 4:
/*      */     case 36:
/* 1832 */       return charType();
/*      */     case 2:
/* 1834 */       return booleanType();
/*      */     case 21:
/* 1836 */       return octetType();
/*      */     case 0:
/* 1838 */       return anyType();
/*      */     case 1:
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 34:
/* 1844 */     case 35: } throw ParseException.syntaxError(this.scanner, new int[] { 13, 8, 17, 27, 33, 4, 36, 2, 21, 0 }, this.token.type);
/*      */   }
/*      */ 
/*      */   private SymtabEntry templateTypeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1856 */     switch (this.token.type)
/*      */     {
/*      */     case 26:
/* 1859 */       return sequenceType(paramSymtabEntry);
/*      */     case 28:
/*      */     case 37:
/* 1862 */       return stringType(paramSymtabEntry);
/*      */     }
/* 1864 */     throw ParseException.syntaxError(this.scanner, new int[] { 26, 28, 37 }, this.token.type);
/*      */   }
/*      */ 
/*      */   private SymtabEntry constrTypeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1872 */     switch (this.token.type)
/*      */     {
/*      */     case 29:
/* 1875 */       return structType(paramSymtabEntry);
/*      */     case 34:
/* 1877 */       return unionType(paramSymtabEntry);
/*      */     case 9:
/* 1879 */       return enumType(paramSymtabEntry);
/*      */     }
/* 1881 */     throw ParseException.syntaxError(this.scanner, new int[] { 29, 34, 9 }, this.token.type);
/*      */   }
/*      */ 
/*      */   private void declarators(TypedefEntry paramTypedefEntry, Vector paramVector)
/*      */     throws IOException, ParseException
/*      */   {
/* 1889 */     for (; ; match(104))
/*      */     {
/* 1891 */       TypedefEntry localTypedefEntry = (TypedefEntry)paramTypedefEntry.clone();
/* 1892 */       declarator(localTypedefEntry);
/* 1893 */       if (isntInList(paramVector, localTypedefEntry.name()))
/* 1894 */         paramVector.addElement(localTypedefEntry);
/* 1895 */       if (this.token.type != 104)
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void declarator(TypedefEntry paramTypedefEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1905 */     paramTypedefEntry.name(this.token.name);
/*      */ 
/* 1908 */     if (!this.token.comment.text().equals(""))
/* 1909 */       paramTypedefEntry.comment(this.token.comment);
/* 1910 */     match(80);
/* 1911 */     while (this.token.type == 112)
/* 1912 */       fixedArraySize(paramTypedefEntry);
/*      */   }
/*      */ 
/*      */   private PrimitiveEntry floatingPtType()
/*      */     throws IOException, ParseException
/*      */   {
/* 1920 */     String str = "double";
/* 1921 */     if (this.token.type == 13)
/*      */     {
/* 1923 */       match(13);
/* 1924 */       str = "float";
/*      */     }
/* 1926 */     else if (this.token.type == 8) {
/* 1927 */       match(8);
/*      */     }
/*      */     else {
/* 1930 */       localObject = new int[] { 13, 8 };
/* 1931 */       ParseException.syntaxError(this.scanner, new int[] { 13, 8 }, this.token.type);
/*      */     }
/* 1933 */     Object localObject = null;
/*      */     try
/*      */     {
/* 1936 */       localObject = (PrimitiveEntry)qualifiedEntry(str);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 1940 */       ParseException.undeclaredType(this.scanner, str);
/*      */     }
/* 1942 */     return localObject;
/*      */   }
/*      */ 
/*      */   private PrimitiveEntry integerType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 1950 */     String str = "";
/* 1951 */     if (this.token.type == 33)
/*      */     {
/* 1953 */       match(33);
/* 1954 */       str = "unsigned ";
/*      */     }
/* 1956 */     str = str + signedInt();
/* 1957 */     PrimitiveEntry localPrimitiveEntry = null;
/*      */     try
/*      */     {
/* 1960 */       localPrimitiveEntry = (PrimitiveEntry)qualifiedEntry(str);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 1964 */       ParseException.undeclaredType(this.scanner, str);
/*      */     }
/* 1966 */     return localPrimitiveEntry;
/*      */   }
/*      */ 
/*      */   private String signedInt()
/*      */     throws IOException, ParseException
/*      */   {
/* 1974 */     String str = "long";
/* 1975 */     if (this.token.type == 17)
/*      */     {
/* 1977 */       match(17);
/*      */ 
/* 1979 */       if (this.token.type == 17)
/*      */       {
/* 1981 */         str = "long long";
/* 1982 */         match(17);
/*      */       }
/*      */     }
/* 1985 */     else if (this.token.type == 27)
/*      */     {
/* 1987 */       str = "short";
/* 1988 */       match(27);
/*      */     }
/*      */     else {
/* 1991 */       ParseException.syntaxError(this.scanner, new int[] { 17, 27 }, this.token.type);
/* 1992 */     }return str;
/*      */   }
/*      */ 
/*      */   private PrimitiveEntry charType()
/*      */     throws IOException, ParseException
/*      */   {
/*      */     String str;
/* 2001 */     if (this.token.type == 4)
/*      */     {
/* 2003 */       match(4);
/* 2004 */       str = "char";
/*      */     }
/*      */     else
/*      */     {
/* 2008 */       match(36);
/* 2009 */       str = "wchar";
/*      */     }
/* 2011 */     PrimitiveEntry localPrimitiveEntry = null;
/*      */     try
/*      */     {
/* 2014 */       localPrimitiveEntry = (PrimitiveEntry)qualifiedEntry(str);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 2018 */       ParseException.undeclaredType(this.scanner, overrideName(str));
/*      */     }
/* 2020 */     return localPrimitiveEntry;
/*      */   }
/*      */ 
/*      */   private PrimitiveEntry booleanType()
/*      */     throws IOException, ParseException
/*      */   {
/* 2028 */     PrimitiveEntry localPrimitiveEntry = null;
/* 2029 */     match(2);
/*      */     try
/*      */     {
/* 2032 */       localPrimitiveEntry = (PrimitiveEntry)qualifiedEntry("boolean");
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 2036 */       ParseException.undeclaredType(this.scanner, overrideName("boolean"));
/*      */     }
/* 2038 */     return localPrimitiveEntry;
/*      */   }
/*      */ 
/*      */   private PrimitiveEntry octetType()
/*      */     throws IOException, ParseException
/*      */   {
/* 2046 */     PrimitiveEntry localPrimitiveEntry = null;
/* 2047 */     match(21);
/*      */     try
/*      */     {
/* 2050 */       localPrimitiveEntry = (PrimitiveEntry)qualifiedEntry("octet");
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 2054 */       ParseException.undeclaredType(this.scanner, overrideName("octet"));
/*      */     }
/* 2056 */     return localPrimitiveEntry;
/*      */   }
/*      */ 
/*      */   private SymtabEntry anyType()
/*      */     throws IOException, ParseException
/*      */   {
/* 2064 */     match(0);
/*      */     try
/*      */     {
/* 2067 */       return qualifiedEntry("any");
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 2071 */       ParseException.undeclaredType(this.scanner, overrideName("any"));
/* 2072 */     }return null;
/*      */   }
/*      */ 
/*      */   private StructEntry structType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2082 */     match(29);
/* 2083 */     String str = this.token.name;
/* 2084 */     match(80);
/* 2085 */     StructEntry localStructEntry = null;
/*      */ 
/* 2087 */     if (this.token.type == 101) {
/* 2088 */       repIDStack.push(((IDLID)repIDStack.peek()).clone());
/* 2089 */       localStructEntry = makeStructEntry(str, paramSymtabEntry, false);
/* 2090 */       ((IDLID)repIDStack.peek()).appendToName(str);
/* 2091 */       this.prep.openScope(localStructEntry);
/* 2092 */       match(101);
/* 2093 */       member(localStructEntry);
/* 2094 */       memberList2(localStructEntry);
/* 2095 */       this.prep.closeScope(localStructEntry);
/* 2096 */       match(102);
/* 2097 */       repIDStack.pop();
/* 2098 */     } else if (this.token.equals(100)) {
/* 2099 */       localStructEntry = makeStructEntry(str, paramSymtabEntry, true);
/*      */     } else {
/* 2101 */       throw ParseException.syntaxError(this.scanner, new int[] { 100, 101 }, this.token.type);
/*      */     }
/*      */ 
/* 2104 */     return localStructEntry;
/*      */   }
/*      */ 
/*      */   private StructEntry makeStructEntry(String paramString, SymtabEntry paramSymtabEntry, boolean paramBoolean)
/*      */   {
/* 2110 */     StructEntry localStructEntry = this.stFactory.structEntry(paramSymtabEntry, 
/* 2111 */       (IDLID)repIDStack
/* 2111 */       .peek());
/* 2112 */     localStructEntry.isReferencable(!paramBoolean);
/* 2113 */     localStructEntry.sourceFile(this.scanner.fileEntry());
/* 2114 */     localStructEntry.name(paramString);
/*      */ 
/* 2116 */     localStructEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 2117 */     pigeonhole(paramSymtabEntry, localStructEntry);
/* 2118 */     return localStructEntry;
/*      */   }
/*      */ 
/*      */   private void memberList2(StructEntry paramStructEntry)
/*      */     throws IOException
/*      */   {
/* 2126 */     while (this.token.type != 102)
/* 2127 */       member(paramStructEntry);
/*      */   }
/*      */ 
/*      */   private void member(StructEntry paramStructEntry)
/*      */     throws IOException
/*      */   {
/* 2135 */     TypedefEntry localTypedefEntry = this.stFactory.typedefEntry(paramStructEntry, (IDLID)repIDStack.peek());
/* 2136 */     localTypedefEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2138 */     localTypedefEntry.comment(this.token.comment);
/*      */     try
/*      */     {
/* 2141 */       localTypedefEntry.type(typeSpec(paramStructEntry));
/* 2142 */       if (localTypedefEntry.type() == paramStructEntry) {
/* 2143 */         throw ParseException.recursive(this.scanner, paramStructEntry.fullName(), this.token.name == null ? "" : this.token.name);
/*      */       }
/*      */ 
/* 2146 */       if ((typeOf(localTypedefEntry) instanceof ExceptionEntry))
/* 2147 */         throw ParseException.illegalException(this.scanner, entryName(paramStructEntry));
/* 2148 */       declarators(localTypedefEntry, paramStructEntry.members());
/* 2149 */       match(100);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/* 2153 */       skipToSemicolon();
/*      */     }
/*      */   }
/*      */ 
/*      */   private final boolean isConstTypeSpec(Token paramToken)
/*      */   {
/* 2162 */     return (paramToken.type == 29) || (paramToken.type == 34) || (paramToken.type == 9);
/*      */   }
/*      */ 
/*      */   private UnionEntry unionType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2170 */     match(34);
/* 2171 */     String str = this.token.name;
/* 2172 */     match(80);
/* 2173 */     UnionEntry localUnionEntry = null;
/*      */ 
/* 2175 */     if (this.token.type == 30) {
/* 2176 */       repIDStack.push(((IDLID)repIDStack.peek()).clone());
/* 2177 */       localUnionEntry = makeUnionEntry(str, paramSymtabEntry, false);
/* 2178 */       ((IDLID)repIDStack.peek()).appendToName(str);
/* 2179 */       match(30);
/* 2180 */       match(108);
/* 2181 */       localUnionEntry.type(switchTypeSpec(localUnionEntry));
/* 2182 */       match(109);
/* 2183 */       this.prep.openScope(localUnionEntry);
/* 2184 */       match(101);
/* 2185 */       switchBody(localUnionEntry);
/* 2186 */       verifyUnion(localUnionEntry);
/* 2187 */       this.prep.closeScope(localUnionEntry);
/* 2188 */       match(102);
/* 2189 */       repIDStack.pop();
/* 2190 */     } else if (this.token.equals(100)) {
/* 2191 */       localUnionEntry = makeUnionEntry(str, paramSymtabEntry, true);
/*      */     } else {
/* 2193 */       throw ParseException.syntaxError(this.scanner, new int[] { 100, 30 }, this.token.type);
/*      */     }
/*      */ 
/* 2197 */     return localUnionEntry;
/*      */   }
/*      */ 
/*      */   private UnionEntry makeUnionEntry(String paramString, SymtabEntry paramSymtabEntry, boolean paramBoolean)
/*      */   {
/* 2203 */     UnionEntry localUnionEntry = this.stFactory.unionEntry(paramSymtabEntry, 
/* 2204 */       (IDLID)repIDStack
/* 2204 */       .peek());
/* 2205 */     localUnionEntry.isReferencable(!paramBoolean);
/* 2206 */     localUnionEntry.sourceFile(this.scanner.fileEntry());
/* 2207 */     localUnionEntry.name(paramString);
/*      */ 
/* 2209 */     localUnionEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 2210 */     pigeonhole(paramSymtabEntry, localUnionEntry);
/* 2211 */     return localUnionEntry;
/*      */   }
/*      */ 
/*      */   private void verifyUnion(UnionEntry paramUnionEntry)
/*      */   {
/* 2219 */     if (paramUnionEntry.typeName().equals(overrideName("boolean")))
/*      */     {
/* 2221 */       if (caseCount(paramUnionEntry) > 2L)
/* 2222 */         ParseException.noDefault(this.scanner);
/*      */     }
/* 2224 */     else if ((paramUnionEntry.type() instanceof EnumEntry))
/*      */     {
/* 2226 */       if (caseCount(paramUnionEntry) > ((EnumEntry)paramUnionEntry.type()).elements().size())
/* 2227 */         ParseException.noDefault(this.scanner);
/*      */     }
/*      */   }
/*      */ 
/*      */   private long caseCount(UnionEntry paramUnionEntry)
/*      */   {
/* 2236 */     long l = 0L;
/* 2237 */     Enumeration localEnumeration = paramUnionEntry.branches().elements();
/* 2238 */     while (localEnumeration.hasMoreElements())
/*      */     {
/* 2240 */       UnionBranch localUnionBranch = (UnionBranch)localEnumeration.nextElement();
/* 2241 */       l += localUnionBranch.labels.size();
/* 2242 */       if (localUnionBranch.isDefault)
/* 2243 */         l += 1L;
/*      */     }
/* 2245 */     return l;
/*      */   }
/*      */ 
/*      */   private SymtabEntry switchTypeSpec(UnionEntry paramUnionEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2253 */     SymtabEntry localSymtabEntry1 = null;
/* 2254 */     switch (this.token.type)
/*      */     {
/*      */     case 17:
/*      */     case 27:
/*      */     case 33:
/* 2259 */       return integerType(paramUnionEntry);
/*      */     case 4:
/*      */     case 36:
/* 2262 */       return charType();
/*      */     case 2:
/* 2264 */       return booleanType();
/*      */     case 9:
/* 2266 */       return enumType(paramUnionEntry);
/*      */     case 80:
/*      */     case 124:
/* 2269 */       localSymtabEntry1 = scopedName(paramUnionEntry, this.stFactory.primitiveEntry());
/* 2270 */       if (hasArrayInfo(paramUnionEntry.type()))
/* 2271 */         ParseException.illegalArray(this.scanner, "switch");
/* 2272 */       SymtabEntry localSymtabEntry2 = typeOf(localSymtabEntry1);
/* 2273 */       if ((!(localSymtabEntry2 instanceof EnumEntry)) && (!(localSymtabEntry2 instanceof PrimitiveEntry))) {
/* 2274 */         ParseException.wrongType(this.scanner, localSymtabEntry1.fullName(), "long, unsigned long, short, unsigned short, char, boolean, enum", 
/* 2276 */           entryName(localSymtabEntry1
/* 2276 */           .type()));
/* 2277 */       } else if ((localSymtabEntry1 instanceof PrimitiveEntry))
/*      */       {
/* 2279 */         SymtabEntry localSymtabEntry3 = qualifiedEntry("octet");
/* 2280 */         SymtabEntry localSymtabEntry4 = qualifiedEntry("float");
/* 2281 */         SymtabEntry localSymtabEntry5 = qualifiedEntry("double");
/* 2282 */         if ((localSymtabEntry2 == localSymtabEntry3) || (localSymtabEntry2 == localSymtabEntry4) || (localSymtabEntry2 == localSymtabEntry5))
/* 2283 */           ParseException.wrongType(this.scanner, localSymtabEntry1.fullName(), "long, unsigned long, short, unsigned short, char, boolean, enum", 
/* 2285 */             entryName(localSymtabEntry1
/* 2285 */             .type())); 
/*      */       }
/* 2286 */       break;
/*      */     default:
/* 2289 */       throw ParseException.syntaxError(this.scanner, new int[] { 17, 27, 33, 4, 2, 9, 80, 124 }, this.token.type);
/*      */     }
/*      */ 
/* 2294 */     return localSymtabEntry1;
/*      */   }
/*      */ 
/*      */   private void switchBody(UnionEntry paramUnionEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2305 */     caseProd(paramUnionEntry);
/* 2306 */     while (!this.token.equals(102))
/* 2307 */       caseProd(paramUnionEntry);
/* 2308 */     paramUnionEntry.defaultBranch(this.defaultBranch == null ? null : this.defaultBranch.typedef);
/* 2309 */     this.defaultBranch = null;
/*      */   }
/*      */ 
/*      */   private void caseProd(UnionEntry paramUnionEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2317 */     UnionBranch localUnionBranch = new UnionBranch();
/* 2318 */     paramUnionEntry.addBranch(localUnionBranch);
/* 2319 */     caseLabel(paramUnionEntry, localUnionBranch);
/* 2320 */     while ((this.token.equals(3)) || (this.token.equals(7)))
/* 2321 */       caseLabel(paramUnionEntry, localUnionBranch);
/* 2322 */     elementSpec(paramUnionEntry, localUnionBranch);
/* 2323 */     match(100);
/*      */   }
/*      */ 
/*      */   private void caseLabel(UnionEntry paramUnionEntry, UnionBranch paramUnionBranch)
/*      */     throws IOException, ParseException
/*      */   {
/* 2331 */     if (this.token.type == 3)
/*      */     {
/* 2333 */       match(3);
/* 2334 */       ConstEntry localConstEntry = this.stFactory.constEntry(paramUnionEntry, (IDLID)repIDStack.peek());
/* 2335 */       localConstEntry.sourceFile(this.scanner.fileEntry());
/* 2336 */       localConstEntry.type(paramUnionEntry);
/*      */ 
/* 2339 */       SymtabEntry localSymtabEntry = typeOf(paramUnionEntry.type());
/*      */       Expression localExpression;
/* 2340 */       if ((localSymtabEntry instanceof EnumEntry)) {
/* 2341 */         localExpression = matchEnum((EnumEntry)localSymtabEntry);
/*      */       }
/*      */       else {
/* 2344 */         localExpression = constExp(localConstEntry);
/* 2345 */         verifyConstType(localExpression, localSymtabEntry);
/*      */       }
/* 2347 */       if (paramUnionEntry.has(localExpression))
/* 2348 */         ParseException.branchLabel(this.scanner, localExpression.rep());
/* 2349 */       paramUnionBranch.labels.addElement(localExpression);
/* 2350 */       match(103);
/*      */     }
/* 2352 */     else if (this.token.type == 7)
/*      */     {
/* 2354 */       match(7);
/* 2355 */       match(103);
/* 2356 */       if (paramUnionEntry.defaultBranch() != null)
/* 2357 */         ParseException.alreadyDefaulted(this.scanner);
/* 2358 */       paramUnionBranch.isDefault = true;
/* 2359 */       this.defaultBranch = paramUnionBranch;
/*      */     }
/*      */     else {
/* 2362 */       throw ParseException.syntaxError(this.scanner, new int[] { 3, 7 }, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Expression matchEnum(EnumEntry paramEnumEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2378 */     SymtabEntry localSymtabEntry = scopedName(paramEnumEntry.container(), new SymtabEntry());
/* 2379 */     return this.exprFactory.terminal(localSymtabEntry.name(), false);
/*      */   }
/*      */ 
/*      */   private void elementSpec(UnionEntry paramUnionEntry, UnionBranch paramUnionBranch)
/*      */     throws IOException, ParseException
/*      */   {
/* 2387 */     TypedefEntry localTypedefEntry = this.stFactory.typedefEntry(paramUnionEntry, (IDLID)repIDStack.peek());
/* 2388 */     localTypedefEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2390 */     localTypedefEntry.comment(this.token.comment);
/* 2391 */     localTypedefEntry.type(typeSpec(paramUnionEntry));
/* 2392 */     if (localTypedefEntry.type() == paramUnionEntry) {
/* 2393 */       throw ParseException.recursive(this.scanner, paramUnionEntry.fullName(), this.token.name == null ? "" : this.token.name);
/*      */     }
/* 2395 */     if ((typeOf(localTypedefEntry) instanceof ExceptionEntry))
/* 2396 */       throw ParseException.illegalException(this.scanner, entryName(paramUnionEntry));
/* 2397 */     declarator(localTypedefEntry);
/* 2398 */     paramUnionBranch.typedef = localTypedefEntry;
/*      */ 
/* 2400 */     if (paramUnionEntry.has(localTypedefEntry))
/* 2401 */       ParseException.branchName(this.scanner, localTypedefEntry.name());
/*      */   }
/*      */ 
/*      */   private EnumEntry enumType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2409 */     match(9);
/* 2410 */     EnumEntry localEnumEntry = newEnumEntry(paramSymtabEntry);
/*      */ 
/* 2412 */     localEnumEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 2413 */     localEnumEntry.name(this.token.name);
/* 2414 */     match(80);
/* 2415 */     this.prep.openScope(localEnumEntry);
/* 2416 */     match(101);
/* 2417 */     if (isntInStringList(localEnumEntry.elements(), this.token.name))
/*      */     {
/* 2419 */       localEnumEntry.addElement(this.token.name);
/* 2420 */       SymtabEntry localSymtabEntry = new SymtabEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/*      */ 
/* 2422 */       if (localSymtabEntry.module().equals(""))
/* 2423 */         localSymtabEntry.module(localSymtabEntry.name());
/* 2424 */       else if (!localSymtabEntry.name().equals(""))
/* 2425 */         localSymtabEntry.module(localSymtabEntry.module() + "/" + localSymtabEntry.name());
/* 2426 */       localSymtabEntry.name(this.token.name);
/*      */ 
/* 2432 */       pigeonhole(localEnumEntry.container(), localSymtabEntry);
/*      */     }
/* 2434 */     match(80);
/* 2435 */     enumType2(localEnumEntry);
/* 2436 */     this.prep.closeScope(localEnumEntry);
/* 2437 */     match(102);
/* 2438 */     return localEnumEntry;
/*      */   }
/*      */ 
/*      */   private void enumType2(EnumEntry paramEnumEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2446 */     while (this.token.type == 104)
/*      */     {
/* 2448 */       match(104);
/* 2449 */       String str = this.token.name;
/* 2450 */       match(80);
/* 2451 */       if (isntInStringList(paramEnumEntry.elements(), str))
/*      */       {
/* 2453 */         paramEnumEntry.addElement(str);
/* 2454 */         SymtabEntry localSymtabEntry = new SymtabEntry(paramEnumEntry.container(), (IDLID)repIDStack.peek());
/*      */ 
/* 2456 */         if (localSymtabEntry.module().equals(""))
/* 2457 */           localSymtabEntry.module(localSymtabEntry.name());
/* 2458 */         else if (!localSymtabEntry.name().equals(""))
/* 2459 */           localSymtabEntry.module(localSymtabEntry.module() + "/" + localSymtabEntry.name());
/* 2460 */         localSymtabEntry.name(str);
/* 2461 */         pigeonhole(paramEnumEntry.container(), localSymtabEntry);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private SequenceEntry sequenceType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2471 */     match(26);
/* 2472 */     match(110);
/*      */ 
/* 2474 */     SequenceEntry localSequenceEntry = newSequenceEntry(paramSymtabEntry);
/* 2475 */     SymtabEntry localSymtabEntry = simpleTypeSpec(localSequenceEntry, false);
/* 2476 */     localSequenceEntry.type(localSymtabEntry);
/* 2477 */     if (!localSymtabEntry.isReferencable())
/*      */     {
/*      */       try
/*      */       {
/* 2483 */         Object localObject = (List)localSymtabEntry.dynamicVariable(ftlKey);
/* 2484 */         if (localObject == null) {
/* 2485 */           localObject = new ArrayList();
/* 2486 */           localSymtabEntry.dynamicVariable(ftlKey, localObject);
/*      */         }
/* 2488 */         ((List)localObject).add(localSequenceEntry);
/*      */       } catch (NoSuchFieldException localNoSuchFieldException) {
/* 2490 */         throw new IllegalStateException();
/*      */       }
/*      */     }
/*      */ 
/* 2494 */     if (this.token.type == 104)
/*      */     {
/* 2496 */       match(104);
/* 2497 */       ConstEntry localConstEntry = this.stFactory.constEntry(localSequenceEntry, (IDLID)repIDStack.peek());
/* 2498 */       localConstEntry.sourceFile(this.scanner.fileEntry());
/* 2499 */       localConstEntry.type(qualifiedEntry("long"));
/* 2500 */       localSequenceEntry.maxSize(positiveIntConst(localConstEntry));
/* 2501 */       verifyConstType(localSequenceEntry.maxSize(), qualifiedEntry("long"));
/*      */     }
/* 2503 */     match(111);
/* 2504 */     return localSequenceEntry;
/*      */   }
/*      */ 
/*      */   private StringEntry stringType(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2512 */     StringEntry localStringEntry = this.stFactory.stringEntry();
/* 2513 */     if (this.token.type == 28)
/*      */     {
/* 2515 */       localStringEntry.name(overrideName("string"));
/* 2516 */       match(28);
/*      */     }
/*      */     else
/*      */     {
/* 2520 */       localStringEntry.name(overrideName("wstring"));
/* 2521 */       match(37);
/*      */     }
/* 2523 */     localStringEntry.maxSize(stringType2(paramSymtabEntry));
/* 2524 */     return localStringEntry;
/*      */   }
/*      */ 
/*      */   private Expression stringType2(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2532 */     if (this.token.type == 110)
/*      */     {
/* 2534 */       match(110);
/*      */ 
/* 2539 */       ConstEntry localConstEntry = this.stFactory.constEntry(paramSymtabEntry, 
/* 2540 */         (IDLID)repIDStack
/* 2540 */         .peek());
/* 2541 */       localConstEntry.sourceFile(this.scanner.fileEntry());
/* 2542 */       localConstEntry.type(qualifiedEntry("long"));
/* 2543 */       Expression localExpression = positiveIntConst(localConstEntry);
/*      */ 
/* 2547 */       verifyConstType(localExpression, qualifiedEntry("long"));
/* 2548 */       match(111);
/* 2549 */       return localExpression;
/*      */     }
/* 2551 */     return null;
/*      */   }
/*      */ 
/*      */   private void fixedArraySize(TypedefEntry paramTypedefEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2559 */     match(112);
/* 2560 */     ConstEntry localConstEntry = this.stFactory.constEntry(paramTypedefEntry, (IDLID)repIDStack.peek());
/* 2561 */     localConstEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2565 */     localConstEntry.type(qualifiedEntry("long"));
/* 2566 */     Expression localExpression = positiveIntConst(localConstEntry);
/* 2567 */     paramTypedefEntry.addArrayInfo(localExpression);
/* 2568 */     verifyConstType(localExpression, qualifiedEntry("long"));
/* 2569 */     match(113);
/*      */   }
/*      */ 
/*      */   private void attrDcl(InterfaceEntry paramInterfaceEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2577 */     AttributeEntry localAttributeEntry1 = this.stFactory.attributeEntry(paramInterfaceEntry, (IDLID)repIDStack.peek());
/* 2578 */     localAttributeEntry1.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2581 */     localAttributeEntry1.comment(this.token.comment);
/* 2582 */     Comment localComment = localAttributeEntry1.comment();
/*      */ 
/* 2584 */     if (this.token.type == 25)
/*      */     {
/* 2586 */       match(25);
/* 2587 */       localAttributeEntry1.readOnly(true);
/*      */     }
/* 2589 */     match(1);
/* 2590 */     localAttributeEntry1.type(paramTypeSpec(localAttributeEntry1));
/* 2591 */     localAttributeEntry1.name(this.token.name);
/*      */ 
/* 2593 */     if (!this.token.comment.text().equals(""))
/* 2594 */       localAttributeEntry1.comment(this.token.comment);
/* 2595 */     paramInterfaceEntry.methodsAddElement(localAttributeEntry1, this.scanner);
/* 2596 */     pigeonholeMethod(paramInterfaceEntry, localAttributeEntry1);
/*      */ 
/* 2598 */     if (!this.token.comment.text().equals(""))
/*      */     {
/* 2602 */       AttributeEntry localAttributeEntry2 = (AttributeEntry)localAttributeEntry1.clone();
/* 2603 */       localAttributeEntry2.comment(localComment);
/*      */ 
/* 2605 */       match(80);
/* 2606 */       attrDcl2(paramInterfaceEntry, localAttributeEntry2);
/*      */     }
/*      */     else
/*      */     {
/* 2610 */       match(80);
/* 2611 */       attrDcl2(paramInterfaceEntry, localAttributeEntry1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void attrDcl2(InterfaceEntry paramInterfaceEntry, AttributeEntry paramAttributeEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2623 */     while (this.token.type == 104)
/*      */     {
/* 2625 */       match(104);
/* 2626 */       AttributeEntry localAttributeEntry = (AttributeEntry)paramAttributeEntry.clone();
/* 2627 */       localAttributeEntry.name(this.token.name);
/*      */ 
/* 2630 */       if (!this.token.comment.text().equals(""))
/* 2631 */         localAttributeEntry.comment(this.token.comment);
/* 2632 */       paramInterfaceEntry.methodsAddElement(localAttributeEntry, this.scanner);
/* 2633 */       pigeonholeMethod(paramInterfaceEntry, localAttributeEntry);
/* 2634 */       match(80);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void exceptDcl(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2643 */     match(10);
/* 2644 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/* 2645 */     ExceptionEntry localExceptionEntry = this.stFactory.exceptionEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 2646 */     ((IDLID)repIDStack.peek()).appendToName(this.token.name);
/* 2647 */     localExceptionEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2649 */     localExceptionEntry.comment(this.tokenHistory.lookBack(1).comment);
/* 2650 */     localExceptionEntry.name(this.token.name);
/* 2651 */     match(80);
/* 2652 */     pigeonhole(paramSymtabEntry, localExceptionEntry);
/* 2653 */     if (this.token.equals(101))
/*      */     {
/* 2655 */       this.prep.openScope(localExceptionEntry);
/* 2656 */       match(101);
/* 2657 */       memberList2(localExceptionEntry);
/* 2658 */       this.prep.closeScope(localExceptionEntry);
/* 2659 */       match(102);
/* 2660 */       repIDStack.pop();
/*      */     }
/*      */     else {
/* 2663 */       throw ParseException.syntaxError(this.scanner, 101, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void opDcl(InterfaceEntry paramInterfaceEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2671 */     MethodEntry localMethodEntry = this.stFactory.methodEntry(paramInterfaceEntry, (IDLID)repIDStack.peek());
/* 2672 */     localMethodEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2674 */     localMethodEntry.comment(this.token.comment);
/* 2675 */     if (this.token.type == 22)
/*      */     {
/* 2677 */       match(22);
/* 2678 */       localMethodEntry.oneway(true);
/*      */     }
/* 2680 */     localMethodEntry.type(opTypeSpec(localMethodEntry));
/* 2681 */     repIDStack.push(((IDLID)repIDStack.peek()).clone());
/* 2682 */     ((IDLID)repIDStack.peek()).appendToName(this.token.name);
/* 2683 */     localMethodEntry.name(this.token.name);
/* 2684 */     paramInterfaceEntry.methodsAddElement(localMethodEntry, this.scanner);
/* 2685 */     pigeonholeMethod(paramInterfaceEntry, localMethodEntry);
/* 2686 */     opDcl2(localMethodEntry);
/* 2687 */     if (localMethodEntry.oneway())
/* 2688 */       checkIfOpLegalForOneway(localMethodEntry);
/* 2689 */     repIDStack.pop();
/*      */   }
/*      */ 
/*      */   private void checkIfOpLegalForOneway(MethodEntry paramMethodEntry)
/*      */   {
/* 2697 */     int i = 0;
/*      */     Enumeration localEnumeration;
/* 2698 */     if ((paramMethodEntry.type() != null) || 
/* 2699 */       (paramMethodEntry
/* 2699 */       .exceptions().size() != 0)) i = 1;
/*      */     else
/*      */     {
/* 2702 */       for (localEnumeration = paramMethodEntry.parameters().elements(); localEnumeration.hasMoreElements(); )
/*      */       {
/* 2704 */         if (((ParameterEntry)localEnumeration.nextElement()).passType() != 0)
/*      */         {
/* 2706 */           i = 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2711 */     if (i != 0)
/* 2712 */       ParseException.oneway(this.scanner, paramMethodEntry.name());
/*      */   }
/*      */ 
/*      */   private void opDcl2(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2720 */     if (this.token.equals(81))
/*      */     {
/* 2722 */       match(81);
/* 2723 */       parameterDcls2(paramMethodEntry);
/*      */     }
/*      */     else
/*      */     {
/* 2727 */       match(80);
/* 2728 */       parameterDcls(paramMethodEntry);
/*      */     }
/* 2730 */     opDcl3(paramMethodEntry);
/*      */   }
/*      */ 
/*      */   private void opDcl3(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2738 */     if (this.token.type != 100)
/*      */     {
/* 2740 */       if ((!this.token.equals(24)) && (!this.token.equals(6))) {
/* 2741 */         throw ParseException.syntaxError(this.scanner, new int[] { 24, 6, 100 }, this.token.type);
/*      */       }
/* 2743 */       if (this.token.type == 24)
/* 2744 */         raisesExpr(paramMethodEntry);
/* 2745 */       if (this.token.type == 6)
/* 2746 */         contextExpr(paramMethodEntry);
/*      */     }
/*      */   }
/*      */ 
/*      */   private SymtabEntry opTypeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2755 */     SymtabEntry localSymtabEntry = null;
/* 2756 */     if (this.token.type == 35)
/* 2757 */       match(35);
/*      */     else
/* 2759 */       localSymtabEntry = paramTypeSpec(paramSymtabEntry);
/* 2760 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   private void parameterDcls(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2768 */     match(108);
/* 2769 */     parameterDcls2(paramMethodEntry);
/*      */   }
/*      */ 
/*      */   private void parameterDcls2(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2777 */     if (this.token.type == 109) {
/* 2778 */       match(109);
/*      */     }
/*      */     else {
/* 2781 */       paramDcl(paramMethodEntry);
/* 2782 */       while (this.token.type == 104)
/*      */       {
/* 2784 */         match(104);
/* 2785 */         paramDcl(paramMethodEntry);
/*      */       }
/* 2787 */       match(109);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paramDcl(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2796 */     ParameterEntry localParameterEntry = this.stFactory.parameterEntry(paramMethodEntry, (IDLID)repIDStack.peek());
/* 2797 */     localParameterEntry.sourceFile(this.scanner.fileEntry());
/*      */ 
/* 2799 */     localParameterEntry.comment(this.token.comment);
/* 2800 */     paramAttribute(localParameterEntry);
/* 2801 */     localParameterEntry.type(paramTypeSpec(paramMethodEntry));
/* 2802 */     localParameterEntry.name(this.token.name);
/* 2803 */     match(80);
/* 2804 */     if (isntInList(paramMethodEntry.parameters(), localParameterEntry.name()))
/* 2805 */       paramMethodEntry.addParameter(localParameterEntry);
/*      */   }
/*      */ 
/*      */   private void paramAttribute(ParameterEntry paramParameterEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2813 */     if (this.token.type == 14)
/*      */     {
/* 2815 */       paramParameterEntry.passType(0);
/* 2816 */       match(14);
/*      */     }
/* 2818 */     else if (this.token.type == 23)
/*      */     {
/* 2820 */       paramParameterEntry.passType(2);
/* 2821 */       match(23);
/*      */     }
/* 2823 */     else if (this.token.type == 15)
/*      */     {
/* 2825 */       paramParameterEntry.passType(1);
/* 2826 */       match(15);
/*      */     }
/*      */     else {
/* 2829 */       throw ParseException.syntaxError(this.scanner, new int[] { 14, 23, 15 }, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void raisesExpr(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2838 */     match(24);
/* 2839 */     match(108);
/*      */ 
/* 2841 */     Comment localComment = this.token.comment;
/* 2842 */     SymtabEntry localSymtabEntry = scopedName(paramMethodEntry.container(), this.stFactory.exceptionEntry());
/* 2843 */     if ((typeOf(localSymtabEntry) instanceof ExceptionEntry))
/*      */     {
/* 2846 */       localSymtabEntry.comment(localComment);
/* 2847 */       if (isntInList(paramMethodEntry.exceptions(), localSymtabEntry))
/* 2848 */         paramMethodEntry.exceptionsAddElement((ExceptionEntry)localSymtabEntry);
/*      */     }
/*      */     else {
/* 2851 */       ParseException.wrongType(this.scanner, localSymtabEntry.fullName(), "exception", 
/* 2852 */         entryName(localSymtabEntry
/* 2852 */         .type()));
/* 2853 */     }raisesExpr2(paramMethodEntry);
/* 2854 */     match(109);
/*      */   }
/*      */ 
/*      */   private void raisesExpr2(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2862 */     while (this.token.type == 104)
/*      */     {
/* 2864 */       match(104);
/*      */ 
/* 2866 */       Comment localComment = this.token.comment;
/* 2867 */       SymtabEntry localSymtabEntry = scopedName(paramMethodEntry.container(), this.stFactory.exceptionEntry());
/* 2868 */       if ((typeOf(localSymtabEntry) instanceof ExceptionEntry))
/*      */       {
/* 2871 */         localSymtabEntry.comment(localComment);
/* 2872 */         if (isntInList(paramMethodEntry.exceptions(), localSymtabEntry))
/* 2873 */           paramMethodEntry.addException((ExceptionEntry)localSymtabEntry);
/*      */       }
/*      */       else {
/* 2876 */         ParseException.wrongType(this.scanner, localSymtabEntry.fullName(), "exception", 
/* 2877 */           entryName(localSymtabEntry
/* 2877 */           .type()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void contextExpr(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2886 */     match(6);
/* 2887 */     match(108);
/* 2888 */     String str = (String)stringLiteral().value();
/* 2889 */     if (isntInStringList(paramMethodEntry.contexts(), str))
/* 2890 */       paramMethodEntry.addContext(str);
/* 2891 */     contextExpr2(paramMethodEntry);
/* 2892 */     match(109);
/*      */   }
/*      */ 
/*      */   private void contextExpr2(MethodEntry paramMethodEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2898 */     while (this.token.type == 104)
/*      */     {
/* 2900 */       match(104);
/* 2901 */       String str = (String)stringLiteral().value();
/* 2902 */       if (isntInStringList(paramMethodEntry.contexts(), str))
/* 2903 */         paramMethodEntry.addContext(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   private SymtabEntry paramTypeSpec(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/* 2912 */     SymtabEntry localSymtabEntry = null;
/* 2913 */     switch (this.token.type)
/*      */     {
/*      */     case 0:
/*      */     case 2:
/*      */     case 4:
/*      */     case 8:
/*      */     case 13:
/*      */     case 17:
/*      */     case 21:
/*      */     case 27:
/*      */     case 33:
/*      */     case 36:
/* 2925 */       return baseTypeSpec(paramSymtabEntry);
/*      */     case 28:
/*      */     case 37:
/* 2928 */       return stringType(paramSymtabEntry);
/*      */     case 20:
/*      */     case 45:
/*      */     case 80:
/*      */     case 124:
/* 2934 */       localSymtabEntry = scopedName(paramSymtabEntry.container(), this.stFactory.primitiveEntry());
/* 2935 */       if ((typeOf(localSymtabEntry) instanceof AttributeEntry))
/*      */       {
/* 2937 */         ParseException.attributeNotType(this.scanner, localSymtabEntry.name());
/*      */       }
/* 2939 */       else if ((typeOf(localSymtabEntry) instanceof MethodEntry))
/* 2940 */         ParseException.operationNotType(this.scanner, localSymtabEntry.name()); break;
/*      */     default:
/* 2948 */       throw ParseException.syntaxError(this.scanner, new int[] { 13, 8, 17, 27, 33, 4, 36, 2, 21, 0, 28, 37, 80, 124, 45 }, this.token.type);
/*      */     }
/*      */ 
/* 2954 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   private void match(int paramInt)
/*      */     throws IOException, ParseException
/*      */   {
/* 2962 */     ParseException localParseException = null;
/* 2963 */     if (!this.token.equals(paramInt))
/*      */     {
/* 2965 */       localParseException = ParseException.syntaxError(this.scanner, paramInt, this.token.type);
/*      */ 
/* 2969 */       if (paramInt == 100) {
/* 2970 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2980 */     this.token = this.scanner.getToken();
/*      */ 
/* 2983 */     issueTokenWarnings();
/*      */ 
/* 2986 */     this.tokenHistory.insert(this.token);
/*      */ 
/* 3001 */     while (this.token.isDirective()) {
/* 3002 */       this.token = this.prep.process(this.token);
/*      */     }
/*      */ 
/* 3006 */     if ((this.token.equals(80)) || (this.token.equals(81)))
/*      */     {
/* 3008 */       String str = (String)this.symbols.get(this.token.name);
/* 3009 */       if ((str != null) && (!str.equals("")))
/*      */       {
/* 3012 */         if (this.macros.contains(this.token.name))
/*      */         {
/* 3014 */           this.scanner.scanString(this.prep.expandMacro(str, this.token));
/* 3015 */           match(this.token.type);
/*      */         }
/*      */         else
/*      */         {
/* 3019 */           this.scanner.scanString(str);
/* 3020 */           match(this.token.type);
/*      */         }
/*      */       }
/*      */     }
/* 3024 */     if (localParseException != null)
/* 3025 */       throw localParseException;
/*      */   }
/*      */ 
/*      */   private void issueTokenWarnings()
/*      */   {
/* 3034 */     if (this.noWarn) {
/* 3035 */       return;
/*      */     }
/* 3037 */     if (((this.token.equals(80)) || (this.token.equals(81))) && 
/* 3038 */       (!this.token
/* 3038 */       .isEscaped()))
/*      */     {
/* 3042 */       if (this.token.collidesWithKeyword()) {
/* 3043 */         ParseException.warning(this.scanner, Util.getMessage("Migration.keywordCollision", this.token.name));
/*      */       }
/*      */     }
/* 3046 */     if ((this.token.isKeyword()) && (this.token.isDeprecated()))
/* 3047 */       ParseException.warning(this.scanner, Util.getMessage("Deprecated.keyword", this.token.toString()));
/*      */   }
/*      */ 
/*      */   private ModuleEntry newModule(ModuleEntry paramModuleEntry)
/*      */   {
/* 3055 */     ModuleEntry localModuleEntry = this.stFactory.moduleEntry(paramModuleEntry, (IDLID)repIDStack.peek());
/* 3056 */     localModuleEntry.sourceFile(this.scanner.fileEntry());
/* 3057 */     localModuleEntry.name(this.token.name);
/*      */ 
/* 3060 */     SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(localModuleEntry.fullName());
/* 3061 */     if ((!this.cppModule) && (localSymtabEntry != null) && ((localSymtabEntry instanceof ModuleEntry)))
/*      */     {
/* 3064 */       localModuleEntry = (ModuleEntry)localSymtabEntry;
/* 3065 */       if (paramModuleEntry == this.topLevelModule)
/*      */       {
/* 3068 */         if (!localModuleEntry.emit())
/*      */         {
/* 3072 */           addToContainer(paramModuleEntry, localModuleEntry);
/* 3073 */         } else if (!paramModuleEntry.contained().contains(localModuleEntry))
/*      */         {
/* 3079 */           addToContainer(paramModuleEntry, localModuleEntry);
/*      */         }
/*      */       }
/*      */     } else {
/* 3083 */       pigeonhole(paramModuleEntry, localModuleEntry);
/* 3084 */     }return localModuleEntry;
/*      */   }
/*      */ 
/*      */   private EnumEntry newEnumEntry(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3092 */     EnumEntry localEnumEntry = this.stFactory.enumEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 3093 */     localEnumEntry.sourceFile(this.scanner.fileEntry());
/* 3094 */     localEnumEntry.name(this.token.name);
/* 3095 */     pigeonhole(paramSymtabEntry, localEnumEntry);
/* 3096 */     return localEnumEntry;
/*      */   }
/*      */ 
/*      */   private SequenceEntry newSequenceEntry(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3104 */     SequenceEntry localSequenceEntry = this.stFactory.sequenceEntry(paramSymtabEntry, (IDLID)repIDStack.peek());
/* 3105 */     localSequenceEntry.sourceFile(this.scanner.fileEntry());
/* 3106 */     localSequenceEntry.name("");
/* 3107 */     pigeonhole(paramSymtabEntry, localSequenceEntry);
/* 3108 */     return localSequenceEntry;
/*      */   }
/*      */ 
/*      */   private void updateSymbolTable(String paramString, SymtabEntry paramSymtabEntry, boolean paramBoolean)
/*      */   {
/* 3114 */     String str1 = paramString.toLowerCase();
/* 3115 */     if ((paramBoolean) && 
/* 3116 */       (this.lcSymbolTable.get(str1) != null)) {
/* 3117 */       ParseException.alreadyDeclared(this.scanner, paramString);
/*      */     }
/* 3119 */     symbolTable.put(paramString, paramSymtabEntry);
/* 3120 */     this.lcSymbolTable.put(str1, paramSymtabEntry);
/*      */ 
/* 3123 */     String str2 = "org/omg/CORBA";
/* 3124 */     if (paramString.startsWith(str2))
/* 3125 */       overrideNames.put("CORBA" + paramString
/* 3126 */         .substring(str2
/* 3126 */         .length()), paramString);
/*      */   }
/*      */ 
/*      */   private void pigeonhole(SymtabEntry paramSymtabEntry1, SymtabEntry paramSymtabEntry2)
/*      */   {
/* 3132 */     if (paramSymtabEntry2.name().equals("")) {
/* 3133 */       paramSymtabEntry2.name("uN__" + ++this.sequence);
/*      */     }
/*      */ 
/* 3138 */     String str1 = paramSymtabEntry2.fullName();
/* 3139 */     if (overrideNames.get(str1) == null) {
/* 3140 */       addToContainer(paramSymtabEntry1, paramSymtabEntry2);
/*      */ 
/* 3145 */       SymtabEntry localSymtabEntry1 = (SymtabEntry)symbolTable.get(str1);
/*      */ 
/* 3147 */       if (localSymtabEntry1 == null) {
/* 3148 */         updateSymbolTable(str1, paramSymtabEntry2, true);
/*      */       }
/*      */       else
/*      */       {
/*      */         String str2;
/*      */         String str3;
/* 3149 */         if (((localSymtabEntry1 instanceof ForwardEntry)) && ((paramSymtabEntry2 instanceof InterfaceEntry)))
/*      */         {
/* 3152 */           str2 = ((IDLID)paramSymtabEntry2.repositoryID()).prefix();
/* 3153 */           str3 = ((IDLID)localSymtabEntry1.repositoryID()).prefix();
/* 3154 */           if (str2.equals(str3))
/* 3155 */             updateSymbolTable(str1, paramSymtabEntry2, false);
/*      */           else {
/* 3157 */             ParseException.badRepIDPrefix(this.scanner, str1, str3, str2);
/*      */           }
/*      */         }
/* 3160 */         else if (((paramSymtabEntry2 instanceof ForwardEntry)) && (((localSymtabEntry1 instanceof InterfaceEntry)) || ((localSymtabEntry1 instanceof ForwardEntry))))
/*      */         {
/* 3163 */           if (((localSymtabEntry1 instanceof ForwardEntry)) && 
/* 3164 */             ((paramSymtabEntry2
/* 3164 */             .repositoryID() instanceof IDLID)) && 
/* 3165 */             ((localSymtabEntry1
/* 3165 */             .repositoryID() instanceof IDLID)))
/*      */           {
/* 3168 */             str2 = ((IDLID)paramSymtabEntry2
/* 3168 */               .repositoryID()).prefix();
/*      */ 
/* 3170 */             str3 = ((IDLID)localSymtabEntry1
/* 3170 */               .repositoryID()).prefix();
/*      */ 
/* 3172 */             if (!str2.equals(str3))
/*      */             {
/* 3175 */               ParseException.badRepIDPrefix(this.scanner, str1, str3, str2);
/*      */             }
/*      */           }
/*      */         }
/* 3179 */         else if ((!this.cppModule) || (!(paramSymtabEntry2 instanceof ModuleEntry)) || (!(localSymtabEntry1 instanceof ModuleEntry)))
/*      */         {
/* 3183 */           if ((!str1.startsWith("org/omg/CORBA")) && 
/* 3184 */             (!str1
/* 3184 */             .startsWith("CORBA")))
/*      */           {
/* 3186 */             if (isForwardable(localSymtabEntry1, paramSymtabEntry2))
/*      */             {
/* 3195 */               if ((localSymtabEntry1.isReferencable()) && (paramSymtabEntry2.isReferencable())) {
/* 3196 */                 ParseException.alreadyDeclared(this.scanner, str1);
/*      */               }
/* 3198 */               if (paramSymtabEntry2.isReferencable())
/*      */               {
/* 3200 */                 str2 = localSymtabEntry1
/* 3200 */                   .sourceFile().absFilename();
/*      */ 
/* 3202 */                 str3 = paramSymtabEntry2
/* 3202 */                   .sourceFile().absFilename();
/* 3203 */                 if (!str2.equals(str3)) {
/* 3204 */                   ParseException.declNotInSameFile(this.scanner, str1, str2);
/*      */                 }
/*      */                 else {
/* 3207 */                   updateSymbolTable(str1, paramSymtabEntry2, false);
/*      */                   List localList;
/*      */                   try
/*      */                   {
/* 3212 */                     localList = (List)localSymtabEntry1.dynamicVariable(ftlKey);
/*      */                   }
/*      */                   catch (NoSuchFieldException localNoSuchFieldException) {
/* 3215 */                     throw new IllegalStateException();
/*      */                   }
/*      */ 
/* 3218 */                   if (localList != null)
/*      */                   {
/* 3220 */                     Iterator localIterator = localList.iterator();
/* 3221 */                     while (localIterator.hasNext()) {
/* 3222 */                       SymtabEntry localSymtabEntry2 = (SymtabEntry)localIterator.next();
/* 3223 */                       localSymtabEntry2.type(paramSymtabEntry2);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             } else {
/* 3229 */               ParseException.alreadyDeclared(this.scanner, str1);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 3237 */   private boolean isForwardable(SymtabEntry paramSymtabEntry1, SymtabEntry paramSymtabEntry2) { return (((paramSymtabEntry1 instanceof StructEntry)) && ((paramSymtabEntry2 instanceof StructEntry))) || (((paramSymtabEntry1 instanceof UnionEntry)) && ((paramSymtabEntry2 instanceof UnionEntry))); }
/*      */ 
/*      */ 
/*      */   private void pigeonholeMethod(InterfaceEntry paramInterfaceEntry, MethodEntry paramMethodEntry)
/*      */   {
/* 3254 */     if (paramMethodEntry.name().equals("")) {
/* 3255 */       paramMethodEntry.name("uN__" + ++this.sequence);
/*      */     }
/*      */ 
/* 3260 */     String str1 = paramMethodEntry.fullName();
/* 3261 */     if (overrideNames.get(str1) == null)
/*      */     {
/* 3263 */       addToContainer(paramInterfaceEntry, paramMethodEntry);
/* 3264 */       String str2 = str1.toLowerCase();
/* 3265 */       symbolTable.put(str1, paramMethodEntry);
/* 3266 */       this.lcSymbolTable.put(str2, paramMethodEntry);
/*      */ 
/* 3269 */       if (str1.startsWith("org/omg/CORBA"))
/* 3270 */         overrideNames.put("CORBA" + str1.substring(13), str1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addToContainer(SymtabEntry paramSymtabEntry1, SymtabEntry paramSymtabEntry2)
/*      */   {
/* 3279 */     if ((paramSymtabEntry1 instanceof ModuleEntry))
/* 3280 */       ((ModuleEntry)paramSymtabEntry1).addContained(paramSymtabEntry2);
/* 3281 */     else if ((paramSymtabEntry1 instanceof InterfaceEntry))
/* 3282 */       ((InterfaceEntry)paramSymtabEntry1).addContained(paramSymtabEntry2);
/* 3283 */     else if ((paramSymtabEntry1 instanceof StructEntry))
/* 3284 */       ((StructEntry)paramSymtabEntry1).addContained(paramSymtabEntry2);
/* 3285 */     else if ((paramSymtabEntry1 instanceof UnionEntry))
/* 3286 */       ((UnionEntry)paramSymtabEntry1).addContained(paramSymtabEntry2);
/* 3287 */     else if ((paramSymtabEntry1 instanceof SequenceEntry))
/* 3288 */       ((SequenceEntry)paramSymtabEntry1).addContained(paramSymtabEntry2);
/*      */   }
/*      */ 
/*      */   SymtabEntry qualifiedEntry(String paramString)
/*      */   {
/* 3308 */     SymtabEntry localSymtabEntry = recursiveQualifiedEntry(paramString);
/* 3309 */     if (localSymtabEntry == null)
/*      */     {
/* 3311 */       ParseException.undeclaredType(this.scanner, paramString);
/*      */     }
/* 3315 */     else if (((localSymtabEntry instanceof ModuleEntry)) && (!this._isModuleLegalType))
/*      */     {
/* 3318 */       ParseException.moduleNotType(this.scanner, paramString);
/* 3319 */       localSymtabEntry = null;
/*      */     }
/* 3321 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry recursiveQualifiedEntry(String paramString)
/*      */   {
/* 3329 */     SymtabEntry localSymtabEntry = null;
/* 3330 */     if ((paramString != null) && (!paramString.equals("void")))
/*      */     {
/* 3332 */       int i = paramString.lastIndexOf('/');
/* 3333 */       if (i >= 0)
/*      */       {
/* 3336 */         localSymtabEntry = recursiveQualifiedEntry(paramString.substring(0, i));
/* 3337 */         if (localSymtabEntry == null)
/* 3338 */           return null;
/* 3339 */         if ((localSymtabEntry instanceof TypedefEntry)) {
/* 3340 */           paramString = typeOf(localSymtabEntry).fullName() + paramString.substring(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3345 */       localSymtabEntry = searchOverrideNames(paramString);
/* 3346 */       if (localSymtabEntry == null)
/* 3347 */         localSymtabEntry = (SymtabEntry)symbolTable.get(paramString);
/* 3348 */       if (localSymtabEntry == null)
/* 3349 */         localSymtabEntry = searchGlobalInheritanceScope(paramString);
/*      */     }
/* 3351 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry partlyQualifiedEntry(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3369 */     SymtabEntry localSymtabEntry = null;
/* 3370 */     if (paramString != null)
/*      */     {
/* 3372 */       int i = paramString.lastIndexOf('/');
/*      */ 
/* 3376 */       localSymtabEntry = recursivePQEntry(paramString.substring(0, i), paramSymtabEntry);
/* 3377 */       if ((localSymtabEntry instanceof TypedefEntry)) {
/* 3378 */         paramString = typeOf(localSymtabEntry).fullName() + paramString.substring(i);
/*      */       }
/*      */ 
/* 3383 */       if (paramSymtabEntry != null)
/* 3384 */         localSymtabEntry = searchModuleScope(paramString.substring(0, paramString.lastIndexOf('/')), paramSymtabEntry);
/* 3385 */       if (localSymtabEntry == null)
/* 3386 */         localSymtabEntry = qualifiedEntry(paramString);
/*      */       else
/* 3388 */         localSymtabEntry = qualifiedEntry(localSymtabEntry.fullName() + paramString.substring(paramString.lastIndexOf('/')));
/*      */     }
/* 3390 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry recursivePQEntry(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3405 */     SymtabEntry localSymtabEntry = null;
/* 3406 */     if (paramString != null)
/*      */     {
/* 3408 */       int i = paramString.lastIndexOf('/');
/* 3409 */       if (i < 0) {
/* 3410 */         localSymtabEntry = searchModuleScope(paramString, paramSymtabEntry);
/*      */       }
/*      */       else
/*      */       {
/* 3415 */         localSymtabEntry = recursivePQEntry(paramString.substring(0, i), paramSymtabEntry);
/* 3416 */         if (localSymtabEntry == null)
/* 3417 */           return null;
/* 3418 */         if ((localSymtabEntry instanceof TypedefEntry)) {
/* 3419 */           paramString = typeOf(localSymtabEntry).fullName() + paramString.substring(i);
/*      */         }
/*      */ 
/* 3423 */         if (paramSymtabEntry != null)
/* 3424 */           localSymtabEntry = searchModuleScope(paramString.substring(0, paramString.lastIndexOf('/')), paramSymtabEntry);
/* 3425 */         if (localSymtabEntry == null)
/* 3426 */           recursiveQualifiedEntry(paramString);
/*      */         else
/* 3428 */           localSymtabEntry = recursiveQualifiedEntry(localSymtabEntry.fullName() + paramString.substring(paramString.lastIndexOf('/')));
/*      */       }
/*      */     }
/* 3431 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry unqualifiedEntry(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3445 */     SymtabEntry localSymtabEntry = unqualifiedEntryWMod(paramString, paramSymtabEntry);
/*      */ 
/* 3450 */     if (((localSymtabEntry instanceof ModuleEntry)) && (!this._isModuleLegalType))
/*      */     {
/* 3453 */       ParseException.moduleNotType(this.scanner, paramString);
/* 3454 */       localSymtabEntry = null;
/*      */     }
/* 3456 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry unqualifiedEntryWMod(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3464 */     SymtabEntry localSymtabEntry = null;
/* 3465 */     if ((paramString != null) && (!paramString.equals("void")))
/*      */     {
/* 3468 */       localSymtabEntry = (SymtabEntry)symbolTable.get(paramSymtabEntry.fullName() + '/' + paramString);
/* 3469 */       if (localSymtabEntry == null)
/* 3470 */         localSymtabEntry = searchLocalInheritanceScope(paramString, paramSymtabEntry);
/* 3471 */       if (localSymtabEntry == null)
/* 3472 */         localSymtabEntry = searchOverrideNames(paramString);
/* 3473 */       if ((localSymtabEntry == null) && (paramSymtabEntry != null))
/* 3474 */         localSymtabEntry = searchModuleScope(paramString, paramSymtabEntry);
/* 3475 */       if (localSymtabEntry == null)
/* 3476 */         localSymtabEntry = searchParentInheritanceScope(paramString, paramSymtabEntry);
/*      */     }
/* 3478 */     if (localSymtabEntry == null)
/*      */     {
/* 3480 */       ParseException.undeclaredType(this.scanner, paramString);
/* 3481 */     }return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry searchParentInheritanceScope(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3493 */     String str1 = paramSymtabEntry.fullName();
/*      */ 
/* 3495 */     while ((paramSymtabEntry != null) && (!str1.equals("")) && (!(paramSymtabEntry instanceof InterfaceEntry)))
/*      */     {
/* 3497 */       int i = str1.lastIndexOf('/');
/* 3498 */       if (i < 0) {
/* 3499 */         str1 = "";
/*      */       } else {
/* 3501 */         str1 = str1.substring(0, i);
/* 3502 */         paramSymtabEntry = (SymtabEntry)symbolTable.get(str1);
/*      */       }
/*      */     }
/*      */ 
/* 3506 */     if ((paramSymtabEntry == null) || (!(paramSymtabEntry instanceof InterfaceEntry))) {
/* 3507 */       return null;
/*      */     }
/*      */ 
/* 3511 */     String str2 = paramSymtabEntry.fullName() + '/' + paramString;
/* 3512 */     SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(str2);
/* 3513 */     if (localSymtabEntry != null) {
/* 3514 */       return localSymtabEntry;
/*      */     }
/*      */ 
/* 3518 */     return searchLocalInheritanceScope(paramString, paramSymtabEntry);
/*      */   }
/*      */ 
/*      */   SymtabEntry searchGlobalInheritanceScope(String paramString)
/*      */   {
/* 3527 */     int i = paramString.lastIndexOf('/');
/* 3528 */     SymtabEntry localSymtabEntry = null;
/* 3529 */     if (i >= 0)
/*      */     {
/* 3531 */       String str = paramString.substring(0, i);
/* 3532 */       localSymtabEntry = (SymtabEntry)symbolTable.get(str);
/*      */ 
/* 3535 */       localSymtabEntry = (localSymtabEntry instanceof InterfaceEntry) ? 
/* 3535 */         searchLocalInheritanceScope(paramString
/* 3535 */         .substring(i + 1), 
/* 3535 */         localSymtabEntry) : null;
/*      */     }
/*      */ 
/* 3538 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry searchLocalInheritanceScope(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3547 */     return (paramSymtabEntry instanceof InterfaceEntry) ? 
/* 3547 */       searchDerivedFrom(paramString, (InterfaceEntry)paramSymtabEntry) : 
/* 3547 */       null;
/*      */   }
/*      */ 
/*      */   SymtabEntry searchOverrideNames(String paramString)
/*      */   {
/* 3556 */     String str = (String)overrideNames.get(paramString);
/*      */ 
/* 3558 */     return str != null ? 
/* 3558 */       (SymtabEntry)symbolTable
/* 3558 */       .get(str) : 
/* 3558 */       null;
/*      */   }
/*      */ 
/*      */   SymtabEntry searchModuleScope(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3567 */     String str1 = paramSymtabEntry.fullName();
/* 3568 */     String str2 = str1 + '/' + paramString;
/* 3569 */     SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(str2);
/* 3570 */     while ((localSymtabEntry == null) && (!str1.equals("")))
/*      */     {
/* 3572 */       int i = str1.lastIndexOf('/');
/* 3573 */       if (i < 0) {
/* 3574 */         str1 = "";
/*      */       }
/*      */       else {
/* 3577 */         str1 = str1.substring(0, i);
/* 3578 */         str2 = str1 + '/' + paramString;
/* 3579 */         localSymtabEntry = (SymtabEntry)symbolTable.get(str2);
/*      */       }
/*      */     }
/* 3582 */     return localSymtabEntry == null ? (SymtabEntry)symbolTable.get(paramString) : localSymtabEntry;
/*      */   }
/*      */ 
/*      */   SymtabEntry searchDerivedFrom(String paramString, InterfaceEntry paramInterfaceEntry)
/*      */   {
/* 3590 */     for (Enumeration localEnumeration = paramInterfaceEntry.derivedFrom().elements(); localEnumeration.hasMoreElements(); )
/*      */     {
/* 3592 */       SymtabEntry localSymtabEntry1 = (SymtabEntry)localEnumeration.nextElement();
/* 3593 */       if ((localSymtabEntry1 instanceof InterfaceEntry))
/*      */       {
/* 3595 */         InterfaceEntry localInterfaceEntry = (InterfaceEntry)localSymtabEntry1;
/* 3596 */         String str = localInterfaceEntry.fullName() + '/' + paramString;
/* 3597 */         SymtabEntry localSymtabEntry2 = (SymtabEntry)symbolTable.get(str);
/* 3598 */         if (localSymtabEntry2 != null)
/* 3599 */           return localSymtabEntry2;
/* 3600 */         localSymtabEntry2 = searchDerivedFrom(paramString, localInterfaceEntry);
/* 3601 */         if (localSymtabEntry2 != null) {
/* 3602 */           return localSymtabEntry2;
/*      */         }
/*      */       }
/*      */     }
/* 3606 */     return null;
/*      */   }
/*      */ 
/*      */   String entryName(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3614 */     if ((paramSymtabEntry instanceof AttributeEntry))
/* 3615 */       return "attribute";
/* 3616 */     if ((paramSymtabEntry instanceof ConstEntry))
/* 3617 */       return "constant";
/* 3618 */     if ((paramSymtabEntry instanceof EnumEntry))
/* 3619 */       return "enumeration";
/* 3620 */     if ((paramSymtabEntry instanceof ExceptionEntry))
/* 3621 */       return "exception";
/* 3622 */     if ((paramSymtabEntry instanceof ValueBoxEntry))
/* 3623 */       return "value box";
/* 3624 */     if (((paramSymtabEntry instanceof ForwardValueEntry)) || ((paramSymtabEntry instanceof ValueEntry)))
/* 3625 */       return "value";
/* 3626 */     if (((paramSymtabEntry instanceof ForwardEntry)) || ((paramSymtabEntry instanceof InterfaceEntry)))
/* 3627 */       return "interface";
/* 3628 */     if ((paramSymtabEntry instanceof MethodEntry))
/* 3629 */       return "method";
/* 3630 */     if ((paramSymtabEntry instanceof ModuleEntry))
/* 3631 */       return "module";
/* 3632 */     if ((paramSymtabEntry instanceof ParameterEntry))
/* 3633 */       return "parameter";
/* 3634 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/* 3635 */       return "primitive";
/* 3636 */     if ((paramSymtabEntry instanceof SequenceEntry))
/* 3637 */       return "sequence";
/* 3638 */     if ((paramSymtabEntry instanceof StringEntry))
/* 3639 */       return "string";
/* 3640 */     if ((paramSymtabEntry instanceof StructEntry))
/* 3641 */       return "struct";
/* 3642 */     if ((paramSymtabEntry instanceof TypedefEntry))
/* 3643 */       return "typedef";
/* 3644 */     if ((paramSymtabEntry instanceof UnionEntry))
/* 3645 */       return "union";
/* 3646 */     return "void";
/*      */   }
/*      */ 
/*      */   private boolean isInterface(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3654 */     return ((paramSymtabEntry instanceof InterfaceEntry)) || (((paramSymtabEntry instanceof ForwardEntry)) && (!(paramSymtabEntry instanceof ForwardValueEntry)));
/*      */   }
/*      */ 
/*      */   private boolean isValue(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3660 */     return paramSymtabEntry instanceof ValueEntry;
/*      */   }
/*      */ 
/*      */   private boolean isInterfaceOnly(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3665 */     return paramSymtabEntry instanceof InterfaceEntry;
/*      */   }
/*      */ 
/*      */   private boolean isForward(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3670 */     return paramSymtabEntry instanceof ForwardEntry;
/*      */   }
/*      */ 
/*      */   private boolean isntInStringList(Vector paramVector, String paramString)
/*      */   {
/* 3679 */     boolean bool = true;
/* 3680 */     Enumeration localEnumeration = paramVector.elements();
/* 3681 */     while (localEnumeration.hasMoreElements()) {
/* 3682 */       if (paramString.equals((String)localEnumeration.nextElement()))
/*      */       {
/* 3684 */         ParseException.alreadyDeclared(this.scanner, paramString);
/* 3685 */         bool = false;
/*      */       }
/*      */     }
/* 3688 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean isntInList(Vector paramVector, String paramString)
/*      */   {
/* 3697 */     boolean bool = true;
/* 3698 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); ) {
/* 3699 */       if (paramString.equals(((SymtabEntry)localEnumeration.nextElement()).name()))
/*      */       {
/* 3701 */         ParseException.alreadyDeclared(this.scanner, paramString);
/* 3702 */         bool = false;
/*      */       }
/*      */     }
/* 3705 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean isntInList(Vector paramVector, SymtabEntry paramSymtabEntry)
/*      */   {
/* 3714 */     boolean bool = true;
/* 3715 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); )
/*      */     {
/* 3717 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/* 3718 */       if (paramSymtabEntry == localSymtabEntry)
/*      */       {
/* 3720 */         ParseException.alreadyDeclared(this.scanner, paramSymtabEntry.fullName());
/* 3721 */         bool = false;
/* 3722 */         break;
/*      */       }
/*      */     }
/* 3725 */     return bool;
/*      */   }
/*      */ 
/*      */   public static SymtabEntry typeOf(SymtabEntry paramSymtabEntry)
/*      */   {
/* 3733 */     while ((paramSymtabEntry instanceof TypedefEntry))
/* 3734 */       paramSymtabEntry = paramSymtabEntry.type();
/* 3735 */     return paramSymtabEntry;
/*      */   }
/*      */ 
/*      */   void forwardEntryCheck()
/*      */   {
/* 3743 */     for (Enumeration localEnumeration = symbolTable.elements(); localEnumeration.hasMoreElements(); )
/*      */     {
/* 3745 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/* 3746 */       if ((localSymtabEntry instanceof ForwardEntry))
/* 3747 */         ParseException.forwardEntry(this.scanner, localSymtabEntry.fullName());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void skipToSemicolon()
/*      */     throws IOException
/*      */   {
/* 3772 */     while ((!this.token.equals(999)) && (!this.token.equals(100)))
/*      */     {
/* 3774 */       if (this.token.equals(101))
/* 3775 */         skipToRightBrace();
/*      */       try
/*      */       {
/* 3778 */         match(this.token.type);
/*      */       }
/*      */       catch (ParseException localParseException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 3785 */     if (this.token.equals(999))
/* 3786 */       throw new EOFException();
/*      */     try
/*      */     {
/* 3789 */       match(100);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void skipToRightBrace()
/*      */     throws IOException
/*      */   {
/* 3801 */     int i = 1;
/* 3802 */     while ((!this.token.equals(999)) && (!this.token.equals(102)))
/*      */     {
/* 3804 */       if (i != 0)
/* 3805 */         i = 0;
/* 3806 */       else if (this.token.equals(101))
/* 3807 */         skipToRightBrace();
/*      */       try
/*      */       {
/* 3810 */         match(this.token.type);
/*      */       }
/*      */       catch (ParseException localParseException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 3817 */     if (this.token.equals(999))
/* 3818 */       throw new EOFException();
/*      */   }
/*      */ 
/*      */   public static void enteringInclude()
/*      */   {
/* 3836 */     repIDStack.push(new IDLID());
/*      */   }
/*      */ 
/*      */   public static void exitingInclude()
/*      */   {
/* 3844 */     repIDStack.pop();
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Parser
 * JD-Core Version:    0.6.2
 */