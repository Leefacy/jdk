/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.DefaultExprFactory;
/*     */ import com.sun.tools.corba.se.idl.constExpr.ExprFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Compile
/*     */ {
/* 482 */   public Arguments arguments = null;
/*     */ 
/* 490 */   protected Hashtable overrideNames = new Hashtable();
/*     */ 
/* 496 */   protected Hashtable symbolTable = new Hashtable();
/*     */ 
/* 503 */   protected Vector includes = new Vector();
/*     */ 
/* 510 */   protected Vector includeEntries = new Vector();
/* 511 */   static Noop noop = new Noop();
/* 512 */   private GenFactory genFactory = null;
/* 513 */   private SymtabFactory symtabFactory = null;
/* 514 */   private ExprFactory exprFactory = null;
/* 515 */   private Parser parser = null;
/* 516 */   Preprocessor preprocessor = new Preprocessor();
/* 517 */   private NoPragma noPragma = new NoPragma();
/* 518 */   private Enumeration emitList = null;
/* 519 */   private String[] keywords = null;
/*     */ 
/*     */   public Compile()
/*     */   {
/* 218 */     this.noPragma.init(this.preprocessor);
/* 219 */     this.preprocessor.registerPragma(this.noPragma);
/*     */ 
/* 223 */     ParseException.detected = false;
/* 224 */     SymtabEntry.includeStack = new Stack();
/* 225 */     SymtabEntry.setEmit = true;
/*     */ 
/* 227 */     Parser.repIDStack = new Stack();
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 232 */     new Compile().start(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   protected Factories factories()
/*     */   {
/* 237 */     return new Factories();
/*     */   }
/*     */ 
/*     */   protected void registerPragma(PragmaHandler paramPragmaHandler)
/*     */   {
/* 242 */     paramPragmaHandler.init(this.preprocessor);
/* 243 */     this.preprocessor.registerPragma(paramPragmaHandler);
/*     */   }
/*     */ 
/*     */   protected void init(String[] paramArrayOfString)
/*     */     throws InvalidArgument
/*     */   {
/* 251 */     initFactories();
/* 252 */     this.arguments.parseArgs(paramArrayOfString);
/* 253 */     initGenerators();
/* 254 */     this.parser = new Parser(this.preprocessor, this.arguments, this.overrideNames, this.symbolTable, this.symtabFactory, this.exprFactory, this.keywords);
/* 255 */     this.preprocessor.init(this.parser);
/* 256 */     this.parser.includes = this.includes;
/* 257 */     this.parser.includeEntries = this.includeEntries;
/*     */   }
/*     */ 
/*     */   protected Enumeration parse()
/*     */     throws IOException
/*     */   {
/* 268 */     if (this.arguments.verbose)
/* 269 */       System.out.println(Util.getMessage("Compile.parsing", this.arguments.file));
/* 270 */     this.parser.parse(this.arguments.file);
/* 271 */     if (!ParseException.detected)
/*     */     {
/* 273 */       this.parser.forwardEntryCheck();
/*     */     }
/*     */ 
/* 278 */     if (this.arguments.verbose)
/* 279 */       System.out.println(Util.getMessage("Compile.parseDone", this.arguments.file));
/* 280 */     if (ParseException.detected)
/*     */     {
/* 282 */       this.symbolTable = null;
/* 283 */       this.emitList = null;
/*     */     }
/*     */     else
/*     */     {
/* 287 */       this.symbolTable = Parser.symbolTable;
/* 288 */       this.emitList = this.parser.emitList.elements();
/*     */     }
/* 290 */     return this.emitList;
/*     */   }
/*     */ 
/*     */   protected void generate()
/*     */     throws IOException
/*     */   {
/* 305 */     if (ParseException.detected)
/* 306 */       this.emitList = null;
/*     */     else
/* 308 */       this.emitList = this.parser.emitList.elements();
/* 309 */     if (this.emitList != null)
/*     */     {
/* 312 */       if (this.arguments.verbose)
/* 313 */         System.out.println();
/* 314 */       while (this.emitList.hasMoreElements())
/*     */       {
/* 316 */         SymtabEntry localSymtabEntry = (SymtabEntry)this.emitList.nextElement();
/* 317 */         if ((this.arguments.verbose) && 
/* 318 */           (!(localSymtabEntry.generator() instanceof Noop)))
/*     */         {
/* 320 */           if (localSymtabEntry.module().equals(""))
/* 321 */             System.out.println(Util.getMessage("Compile.generating", localSymtabEntry.name()));
/*     */           else
/* 323 */             System.out.println(Util.getMessage("Compile.generating", localSymtabEntry.module() + '/' + localSymtabEntry.name())); 
/*     */         }
/* 324 */         localSymtabEntry.generate(this.symbolTable, null);
/* 325 */         if ((this.arguments.verbose) && 
/* 326 */           (!(localSymtabEntry.generator() instanceof Noop)))
/*     */         {
/* 328 */           if (localSymtabEntry.module().equals(""))
/* 329 */             System.out.println(Util.getMessage("Compile.genDone", localSymtabEntry.name()));
/*     */           else
/* 331 */             System.out.println(Util.getMessage("Compile.genDone", localSymtabEntry.module() + '/' + localSymtabEntry.name()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void start(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/* 345 */       init(paramArrayOfString);
/* 346 */       if (this.arguments.versionRequest) {
/* 347 */         displayVersion();
/*     */       }
/*     */       else {
/* 350 */         parse();
/* 351 */         generate();
/*     */       }
/*     */     }
/*     */     catch (InvalidArgument localInvalidArgument)
/*     */     {
/* 356 */       System.err.println(localInvalidArgument);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 360 */       System.err.println(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initFactories()
/*     */   {
/* 367 */     Factories localFactories = factories();
/* 368 */     if (localFactories == null) localFactories = new Factories();
/*     */ 
/* 371 */     Arguments localArguments = localFactories.arguments();
/* 372 */     if (localArguments == null)
/* 373 */       this.arguments = new Arguments();
/*     */     else {
/* 375 */       this.arguments = localArguments;
/*     */     }
/*     */ 
/* 378 */     SymtabFactory localSymtabFactory = localFactories.symtabFactory();
/* 379 */     if (localSymtabFactory == null)
/* 380 */       this.symtabFactory = new DefaultSymtabFactory();
/*     */     else {
/* 382 */       this.symtabFactory = localSymtabFactory;
/*     */     }
/*     */ 
/* 385 */     ExprFactory localExprFactory = localFactories.exprFactory();
/* 386 */     if (localExprFactory == null)
/* 387 */       this.exprFactory = new DefaultExprFactory();
/*     */     else {
/* 389 */       this.exprFactory = localExprFactory;
/*     */     }
/*     */ 
/* 392 */     GenFactory localGenFactory = localFactories.genFactory();
/* 393 */     if (localGenFactory == null)
/* 394 */       this.genFactory = noop;
/*     */     else {
/* 396 */       this.genFactory = localGenFactory;
/*     */     }
/*     */ 
/* 399 */     this.keywords = localFactories.languageKeywords();
/* 400 */     if (this.keywords == null)
/* 401 */       this.keywords = new String[0];
/*     */   }
/*     */ 
/*     */   private void initGenerators()
/*     */   {
/* 406 */     AttributeGen localAttributeGen = this.genFactory.createAttributeGen();
/* 407 */     AttributeEntry.attributeGen = localAttributeGen == null ? noop : localAttributeGen;
/*     */ 
/* 409 */     ConstGen localConstGen = this.genFactory.createConstGen();
/* 410 */     ConstEntry.constGen = localConstGen == null ? noop : localConstGen;
/*     */ 
/* 412 */     EnumGen localEnumGen = this.genFactory.createEnumGen();
/* 413 */     EnumEntry.enumGen = localEnumGen == null ? noop : localEnumGen;
/*     */ 
/* 415 */     ExceptionGen localExceptionGen = this.genFactory.createExceptionGen();
/* 416 */     ExceptionEntry.exceptionGen = localExceptionGen == null ? noop : localExceptionGen;
/*     */ 
/* 418 */     ForwardGen localForwardGen = this.genFactory.createForwardGen();
/* 419 */     ForwardEntry.forwardGen = localForwardGen == null ? noop : localForwardGen;
/*     */ 
/* 421 */     ForwardValueGen localForwardValueGen = this.genFactory.createForwardValueGen();
/* 422 */     ForwardValueEntry.forwardValueGen = localForwardValueGen == null ? noop : localForwardValueGen;
/*     */ 
/* 424 */     IncludeGen localIncludeGen = this.genFactory.createIncludeGen();
/* 425 */     IncludeEntry.includeGen = localIncludeGen == null ? noop : localIncludeGen;
/*     */ 
/* 427 */     InterfaceGen localInterfaceGen = this.genFactory.createInterfaceGen();
/* 428 */     InterfaceEntry.interfaceGen = localInterfaceGen == null ? noop : localInterfaceGen;
/*     */ 
/* 430 */     ValueGen localValueGen = this.genFactory.createValueGen();
/* 431 */     ValueEntry.valueGen = localValueGen == null ? noop : localValueGen;
/*     */ 
/* 433 */     ValueBoxGen localValueBoxGen = this.genFactory.createValueBoxGen();
/* 434 */     ValueBoxEntry.valueBoxGen = localValueBoxGen == null ? noop : localValueBoxGen;
/*     */ 
/* 436 */     MethodGen localMethodGen = this.genFactory.createMethodGen();
/* 437 */     MethodEntry.methodGen = localMethodGen == null ? noop : localMethodGen;
/*     */ 
/* 439 */     ModuleGen localModuleGen = this.genFactory.createModuleGen();
/* 440 */     ModuleEntry.moduleGen = localModuleGen == null ? noop : localModuleGen;
/*     */ 
/* 442 */     NativeGen localNativeGen = this.genFactory.createNativeGen();
/* 443 */     NativeEntry.nativeGen = localNativeGen == null ? noop : localNativeGen;
/*     */ 
/* 445 */     ParameterGen localParameterGen = this.genFactory.createParameterGen();
/* 446 */     ParameterEntry.parameterGen = localParameterGen == null ? noop : localParameterGen;
/*     */ 
/* 448 */     PragmaGen localPragmaGen = this.genFactory.createPragmaGen();
/* 449 */     PragmaEntry.pragmaGen = localPragmaGen == null ? noop : localPragmaGen;
/*     */ 
/* 451 */     PrimitiveGen localPrimitiveGen = this.genFactory.createPrimitiveGen();
/* 452 */     PrimitiveEntry.primitiveGen = localPrimitiveGen == null ? noop : localPrimitiveGen;
/*     */ 
/* 454 */     SequenceGen localSequenceGen = this.genFactory.createSequenceGen();
/* 455 */     SequenceEntry.sequenceGen = localSequenceGen == null ? noop : localSequenceGen;
/*     */ 
/* 457 */     StringGen localStringGen = this.genFactory.createStringGen();
/* 458 */     StringEntry.stringGen = localStringGen == null ? noop : localStringGen;
/*     */ 
/* 460 */     StructGen localStructGen = this.genFactory.createStructGen();
/* 461 */     StructEntry.structGen = localStructGen == null ? noop : localStructGen;
/*     */ 
/* 463 */     TypedefGen localTypedefGen = this.genFactory.createTypedefGen();
/* 464 */     TypedefEntry.typedefGen = localTypedefGen == null ? noop : localTypedefGen;
/*     */ 
/* 466 */     UnionGen localUnionGen = this.genFactory.createUnionGen();
/* 467 */     UnionEntry.unionGen = localUnionGen == null ? noop : localUnionGen;
/*     */   }
/*     */ 
/*     */   protected void displayVersion()
/*     */   {
/* 475 */     String str = Util.getMessage("Version.product", Util.getMessage("Version.number"));
/* 476 */     System.out.println(str);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Compile
 * JD-Core Version:    0.6.2
 */