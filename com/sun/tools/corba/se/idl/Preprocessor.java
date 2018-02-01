/*      */ package com.sun.tools.corba.se.idl;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.constExpr.BinaryExpr;
/*      */ import com.sun.tools.corba.se.idl.constExpr.BooleanAnd;
/*      */ import com.sun.tools.corba.se.idl.constExpr.BooleanNot;
/*      */ import com.sun.tools.corba.se.idl.constExpr.BooleanOr;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Equal;
/*      */ import com.sun.tools.corba.se.idl.constExpr.EvaluationException;
/*      */ import com.sun.tools.corba.se.idl.constExpr.ExprFactory;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import com.sun.tools.corba.se.idl.constExpr.GreaterEqual;
/*      */ import com.sun.tools.corba.se.idl.constExpr.GreaterThan;
/*      */ import com.sun.tools.corba.se.idl.constExpr.LessEqual;
/*      */ import com.sun.tools.corba.se.idl.constExpr.LessThan;
/*      */ import com.sun.tools.corba.se.idl.constExpr.NotEqual;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class Preprocessor
/*      */ {
/*  691 */   private Vector PragmaIDs = new Vector();
/*      */ 
/*  850 */   private Vector pragmaHandlers = new Vector();
/*      */ 
/*  945 */   private boolean lastWasMacroID = false;
/*      */   private Parser parser;
/*      */   private Scanner scanner;
/*      */   private Hashtable symbols;
/*      */   private Vector macros;
/* 1427 */   private Stack alreadyProcessedABranch = new Stack();
/*      */   Token token;
/* 1430 */   private static String indent = "";
/*      */ 
/*      */   void init(Parser paramParser)
/*      */   {
/*   82 */     this.parser = paramParser;
/*   83 */     this.symbols = paramParser.symbols;
/*   84 */     this.macros = paramParser.macros;
/*      */   }
/*      */ 
/*      */   protected Object clone()
/*      */   {
/*   92 */     return new Preprocessor();
/*      */   }
/*      */ 
/*      */   Token process(Token paramToken)
/*      */     throws IOException, ParseException
/*      */   {
/*  100 */     this.token = paramToken;
/*  101 */     this.scanner = this.parser.scanner;
/*      */ 
/*  105 */     this.scanner.escapedOK = false;
/*      */     try
/*      */     {
/*  108 */       switch (this.token.type)
/*      */       {
/*      */       case 307:
/*  111 */         include();
/*  112 */         break;
/*      */       case 302:
/*  114 */         ifClause();
/*  115 */         break;
/*      */       case 303:
/*  117 */         ifdef(false);
/*  118 */         break;
/*      */       case 304:
/*  120 */         ifdef(true);
/*  121 */         break;
/*      */       case 305:
/*  123 */         if (this.alreadyProcessedABranch.empty())
/*  124 */           throw ParseException.elseNoIf(this.scanner);
/*  125 */         if (((Boolean)this.alreadyProcessedABranch.peek()).booleanValue()) {
/*  126 */           skipToEndif();
/*      */         }
/*      */         else {
/*  129 */           this.alreadyProcessedABranch.pop();
/*  130 */           this.alreadyProcessedABranch.push(new Boolean(true));
/*  131 */           this.token = this.scanner.getToken();
/*      */         }
/*  133 */         break;
/*      */       case 306:
/*  135 */         elif();
/*  136 */         break;
/*      */       case 308:
/*  138 */         if (this.alreadyProcessedABranch.empty()) {
/*  139 */           throw ParseException.endNoIf(this.scanner);
/*      */         }
/*      */ 
/*  142 */         this.alreadyProcessedABranch.pop();
/*  143 */         this.token = this.scanner.getToken();
/*  144 */         break;
/*      */       case 300:
/*  147 */         define();
/*  148 */         break;
/*      */       case 301:
/*  150 */         undefine();
/*  151 */         break;
/*      */       case 311:
/*  153 */         pragma();
/*  154 */         break;
/*      */       case 313:
/*  156 */         if (!this.parser.noWarn)
/*  157 */           ParseException.warning(this.scanner, Util.getMessage("Preprocessor.unknown", this.token.name));
/*      */         break;
/*      */       case 309:
/*      */       case 310:
/*      */       case 312:
/*      */       }
/*  163 */       this.scanner.skipLineComment();
/*  164 */       this.token = this.scanner.getToken();
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  173 */       this.scanner.escapedOK = true;
/*  174 */       throw localIOException;
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/*  180 */       this.scanner.escapedOK = true;
/*  181 */       throw localParseException;
/*      */     }
/*      */ 
/*  185 */     this.scanner.escapedOK = true;
/*  186 */     return this.token;
/*      */   }
/*      */ 
/*      */   private void include()
/*      */     throws IOException, ParseException
/*      */   {
/*  194 */     match(307);
/*  195 */     IncludeEntry localIncludeEntry = this.parser.stFactory.includeEntry(this.parser.currentModule);
/*  196 */     localIncludeEntry.sourceFile(this.scanner.fileEntry());
/*  197 */     this.scanner.fileEntry().addInclude(localIncludeEntry);
/*  198 */     if (this.token.type == 204) {
/*  199 */       include2(localIncludeEntry);
/*  200 */     } else if (this.token.type == 110) {
/*  201 */       include3(localIncludeEntry);
/*      */     }
/*      */     else {
/*  204 */       int[] arrayOfInt = { 204, 110 };
/*  205 */       throw ParseException.syntaxError(this.scanner, arrayOfInt, this.token.type);
/*      */     }
/*  207 */     if ((this.parser.currentModule instanceof ModuleEntry))
/*  208 */       ((ModuleEntry)this.parser.currentModule).addContained(localIncludeEntry);
/*  209 */     else if ((this.parser.currentModule instanceof InterfaceEntry))
/*  210 */       ((InterfaceEntry)this.parser.currentModule).addContained(localIncludeEntry);
/*      */   }
/*      */ 
/*      */   private void include2(IncludeEntry paramIncludeEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  218 */     paramIncludeEntry.name('"' + this.token.name + '"');
/*  219 */     include4(paramIncludeEntry, this.token.name);
/*  220 */     match(204);
/*      */   }
/*      */ 
/*      */   private void include3(IncludeEntry paramIncludeEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  228 */     if (this.token.type != 110)
/*      */     {
/*  230 */       match(110);
/*      */     }
/*      */     else
/*      */       try
/*      */       {
/*  235 */         String str = getUntil('>');
/*  236 */         this.token = this.scanner.getToken();
/*  237 */         paramIncludeEntry.name('<' + str + '>');
/*  238 */         include4(paramIncludeEntry, str);
/*  239 */         match(111);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  243 */         throw ParseException.syntaxError(this.scanner, ">", "EOF");
/*      */       }
/*      */   }
/*      */ 
/*      */   private void include4(IncludeEntry paramIncludeEntry, String paramString)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     try
/*      */     {
/*  258 */       boolean bool = this.parser.currentModule == this.parser.topLevelModule;
/*      */ 
/*  260 */       paramIncludeEntry.absFilename(Util.getAbsolutePath(paramString, this.parser.paths));
/*  261 */       this.scanner.scanIncludedFile(paramIncludeEntry, getFilename(paramString), bool);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  265 */       ParseException.generic(this.scanner, localIOException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void define()
/*      */     throws IOException, ParseException
/*      */   {
/*  274 */     match(300);
/*  275 */     if (this.token.equals(80))
/*      */     {
/*  277 */       String str = this.scanner.getStringToEOL();
/*  278 */       this.symbols.put(this.token.name, str.trim());
/*  279 */       match(80);
/*      */     }
/*  281 */     else if (this.token.equals(81))
/*      */     {
/*  283 */       this.symbols.put(this.token.name, '(' + this.scanner.getStringToEOL().trim());
/*  284 */       this.macros.addElement(this.token.name);
/*  285 */       match(81);
/*      */     }
/*      */     else {
/*  288 */       throw ParseException.syntaxError(this.scanner, 80, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void undefine()
/*      */     throws IOException, ParseException
/*      */   {
/*  296 */     match(301);
/*  297 */     if (this.token.equals(80))
/*      */     {
/*  299 */       this.symbols.remove(this.token.name);
/*  300 */       this.macros.removeElement(this.token.name);
/*  301 */       match(80);
/*      */     }
/*      */     else {
/*  304 */       throw ParseException.syntaxError(this.scanner, 80, this.token.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ifClause()
/*      */     throws IOException, ParseException
/*      */   {
/*  312 */     match(302);
/*  313 */     constExpr();
/*      */   }
/*      */ 
/*      */   private void constExpr()
/*      */     throws IOException, ParseException
/*      */   {
/*  321 */     SymtabEntry localSymtabEntry = new SymtabEntry(this.parser.currentModule);
/*  322 */     localSymtabEntry.container(this.parser.currentModule);
/*  323 */     this.parser.parsingConditionalExpr = true;
/*  324 */     Expression localExpression = booleanConstExpr(localSymtabEntry);
/*  325 */     this.parser.parsingConditionalExpr = false;
/*      */     boolean bool;
/*  327 */     if ((localExpression.value() instanceof Boolean))
/*  328 */       bool = ((Boolean)localExpression.value()).booleanValue();
/*      */     else
/*  330 */       bool = ((Number)localExpression.value()).longValue() != 0L;
/*  331 */     this.alreadyProcessedABranch.push(new Boolean(bool));
/*  332 */     if (!bool)
/*  333 */       skipToEndiforElse();
/*      */   }
/*      */ 
/*      */   Expression booleanConstExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  341 */     Expression localExpression = orExpr(null, paramSymtabEntry);
/*      */     try
/*      */     {
/*  344 */       localExpression.evaluate();
/*      */     }
/*      */     catch (EvaluationException localEvaluationException)
/*      */     {
/*  348 */       ParseException.evaluationError(this.scanner, localEvaluationException.toString());
/*      */     }
/*  350 */     return localExpression;
/*      */   }
/*      */ 
/*      */   private Expression orExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/*  358 */     if (paramExpression == null) {
/*  359 */       paramExpression = andExpr(null, paramSymtabEntry);
/*      */     }
/*      */     else {
/*  362 */       localObject = (BinaryExpr)paramExpression;
/*  363 */       ((BinaryExpr)localObject).right(andExpr(null, paramSymtabEntry));
/*  364 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/*  366 */     if (this.token.equals(134))
/*      */     {
/*  368 */       match(this.token.type);
/*  369 */       localObject = this.parser.exprFactory.booleanOr(paramExpression, null);
/*  370 */       ((BooleanOr)localObject).rep(paramExpression.rep() + " || ");
/*  371 */       return orExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*      */ 
/*  374 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression andExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/*  382 */     if (paramExpression == null) {
/*  383 */       paramExpression = notExpr(paramSymtabEntry);
/*      */     }
/*      */     else {
/*  386 */       localObject = (BinaryExpr)paramExpression;
/*  387 */       ((BinaryExpr)localObject).right(notExpr(paramSymtabEntry));
/*  388 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/*  390 */     if (this.token.equals(135))
/*      */     {
/*  392 */       match(this.token.type);
/*  393 */       localObject = this.parser.exprFactory.booleanAnd(paramExpression, null);
/*  394 */       ((BooleanAnd)localObject).rep(paramExpression.rep() + " && ");
/*  395 */       return andExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*      */ 
/*  398 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   private Expression notExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/*  407 */     if (this.token.equals(129))
/*      */     {
/*  409 */       match(129);
/*  410 */       localObject = this.parser.exprFactory.booleanNot(definedExpr(paramSymtabEntry));
/*  411 */       ((Expression)localObject).rep("!" + ((BooleanNot)localObject).operand().rep());
/*      */     }
/*      */     else {
/*  414 */       localObject = definedExpr(paramSymtabEntry);
/*  415 */     }return localObject;
/*      */   }
/*      */ 
/*      */   private Expression definedExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  423 */     if ((this.token.equals(80)) && (this.token.name.equals("defined")))
/*  424 */       match(80);
/*  425 */     return equalityExpr(null, paramSymtabEntry);
/*      */   }
/*      */ 
/*      */   private Expression equalityExpr(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*      */     Object localObject;
/*  433 */     if (paramExpression == null)
/*      */     {
/*  435 */       this.parser.token = this.token;
/*  436 */       paramExpression = this.parser.constExp(paramSymtabEntry);
/*  437 */       this.token = this.parser.token;
/*      */     }
/*      */     else
/*      */     {
/*  441 */       localObject = (BinaryExpr)paramExpression;
/*  442 */       this.parser.token = this.token;
/*  443 */       Expression localExpression = this.parser.constExp(paramSymtabEntry);
/*  444 */       this.token = this.parser.token;
/*  445 */       ((BinaryExpr)localObject).right(localExpression);
/*  446 */       paramExpression.rep(paramExpression.rep() + ((BinaryExpr)localObject).right().rep());
/*      */     }
/*  448 */     if (this.token.equals(130))
/*      */     {
/*  450 */       match(this.token.type);
/*  451 */       localObject = this.parser.exprFactory.equal(paramExpression, null);
/*  452 */       ((Equal)localObject).rep(paramExpression.rep() + " == ");
/*  453 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*  455 */     if (this.token.equals(131))
/*      */     {
/*  457 */       match(this.token.type);
/*  458 */       localObject = this.parser.exprFactory.notEqual(paramExpression, null);
/*  459 */       ((NotEqual)localObject).rep(paramExpression.rep() + " != ");
/*  460 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*  462 */     if (this.token.equals(111))
/*      */     {
/*  464 */       match(this.token.type);
/*  465 */       localObject = this.parser.exprFactory.greaterThan(paramExpression, null);
/*  466 */       ((GreaterThan)localObject).rep(paramExpression.rep() + " > ");
/*  467 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*  469 */     if (this.token.equals(132))
/*      */     {
/*  471 */       match(this.token.type);
/*  472 */       localObject = this.parser.exprFactory.greaterEqual(paramExpression, null);
/*  473 */       ((GreaterEqual)localObject).rep(paramExpression.rep() + " >= ");
/*  474 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*  476 */     if (this.token.equals(110))
/*      */     {
/*  478 */       match(this.token.type);
/*  479 */       localObject = this.parser.exprFactory.lessThan(paramExpression, null);
/*  480 */       ((LessThan)localObject).rep(paramExpression.rep() + " < ");
/*  481 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*  483 */     if (this.token.equals(133))
/*      */     {
/*  485 */       match(this.token.type);
/*  486 */       localObject = this.parser.exprFactory.lessEqual(paramExpression, null);
/*  487 */       ((LessEqual)localObject).rep(paramExpression.rep() + " <= ");
/*  488 */       return equalityExpr((Expression)localObject, paramSymtabEntry);
/*      */     }
/*      */ 
/*  491 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   Expression primaryExpr(SymtabEntry paramSymtabEntry)
/*      */     throws IOException, ParseException
/*      */   {
/*  499 */     Object localObject = null;
/*  500 */     switch (this.token.type)
/*      */     {
/*      */     case 80:
/*  507 */       localObject = this.parser.exprFactory.terminal("0", BigInteger.valueOf(0L));
/*  508 */       this.token = this.scanner.getToken();
/*  509 */       break;
/*      */     case 200:
/*      */     case 201:
/*      */     case 202:
/*      */     case 203:
/*      */     case 204:
/*  516 */       localObject = this.parser.literal(paramSymtabEntry);
/*  517 */       this.token = this.parser.token;
/*  518 */       break;
/*      */     case 108:
/*  520 */       match(108);
/*  521 */       localObject = booleanConstExpr(paramSymtabEntry);
/*  522 */       match(109);
/*  523 */       ((Expression)localObject).rep('(' + ((Expression)localObject).rep() + ')');
/*  524 */       break;
/*      */     default:
/*  526 */       int[] arrayOfInt = { 205, 108 };
/*  527 */       throw ParseException.syntaxError(this.scanner, arrayOfInt, this.token.type);
/*      */     }
/*  529 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void ifDefine(boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IOException, ParseException
/*      */   {
/*  537 */     if (this.token.equals(80)) {
/*  538 */       if (((paramBoolean2) && (this.symbols.containsKey(this.token.name))) || ((!paramBoolean2) && (!this.symbols.containsKey(this.token.name))))
/*      */       {
/*  540 */         this.alreadyProcessedABranch.push(new Boolean(false));
/*  541 */         skipToEndiforElse();
/*      */       }
/*      */       else
/*      */       {
/*  545 */         this.alreadyProcessedABranch.push(new Boolean(true));
/*  546 */         match(80);
/*  547 */         if (paramBoolean1)
/*  548 */           match(109);
/*      */       }
/*      */     }
/*  551 */     else throw ParseException.syntaxError(this.scanner, 80, this.token.type);
/*      */   }
/*      */ 
/*      */   private void ifdef(boolean paramBoolean)
/*      */     throws IOException, ParseException
/*      */   {
/*  559 */     if (paramBoolean)
/*  560 */       match(304);
/*      */     else
/*  562 */       match(303);
/*  563 */     if (this.token.equals(80)) {
/*  564 */       if (((paramBoolean) && (this.symbols.containsKey(this.token.name))) || ((!paramBoolean) && (!this.symbols.containsKey(this.token.name))))
/*      */       {
/*  566 */         this.alreadyProcessedABranch.push(new Boolean(false));
/*  567 */         skipToEndiforElse();
/*      */       }
/*      */       else
/*      */       {
/*  571 */         this.alreadyProcessedABranch.push(new Boolean(true));
/*  572 */         match(80);
/*      */       }
/*      */     }
/*  575 */     else throw ParseException.syntaxError(this.scanner, 80, this.token.type);
/*      */   }
/*      */ 
/*      */   private void elif()
/*      */     throws IOException, ParseException
/*      */   {
/*  583 */     if (this.alreadyProcessedABranch.empty())
/*  584 */       throw ParseException.elseNoIf(this.scanner);
/*  585 */     if (((Boolean)this.alreadyProcessedABranch.peek()).booleanValue()) {
/*  586 */       skipToEndif();
/*      */     }
/*      */     else {
/*  589 */       match(306);
/*  590 */       constExpr();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void skipToEndiforElse()
/*      */     throws IOException, ParseException
/*      */   {
/*  599 */     while ((!this.token.equals(308)) && (!this.token.equals(305)) && (!this.token.equals(306)))
/*      */     {
/*  601 */       if ((this.token.equals(303)) || (this.token.equals(304)))
/*      */       {
/*  603 */         this.alreadyProcessedABranch.push(new Boolean(true));
/*  604 */         skipToEndif();
/*      */       }
/*      */       else {
/*  607 */         this.token = this.scanner.skipUntil('#');
/*      */       }
/*      */     }
/*  609 */     process(this.token);
/*      */   }
/*      */ 
/*      */   private void skipToEndif()
/*      */     throws IOException, ParseException
/*      */   {
/*  617 */     while (!this.token.equals(308))
/*      */     {
/*  619 */       this.token = this.scanner.skipUntil('#');
/*  620 */       if ((this.token.equals(303)) || (this.token.equals(304)))
/*      */       {
/*  622 */         this.alreadyProcessedABranch.push(new Boolean(true));
/*  623 */         skipToEndif();
/*      */       }
/*      */     }
/*  626 */     this.alreadyProcessedABranch.pop();
/*  627 */     match(308);
/*      */   }
/*      */ 
/*      */   private void pragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  638 */     match(311);
/*  639 */     String str = this.token.name;
/*      */ 
/*  643 */     this.scanner.escapedOK = true;
/*  644 */     match(80);
/*      */ 
/*  647 */     PragmaEntry localPragmaEntry = this.parser.stFactory.pragmaEntry(this.parser.currentModule);
/*  648 */     localPragmaEntry.name(str);
/*  649 */     localPragmaEntry.sourceFile(this.scanner.fileEntry());
/*  650 */     localPragmaEntry.data(this.scanner.currentLine());
/*  651 */     if ((this.parser.currentModule instanceof ModuleEntry))
/*  652 */       ((ModuleEntry)this.parser.currentModule).addContained(localPragmaEntry);
/*  653 */     else if ((this.parser.currentModule instanceof InterfaceEntry)) {
/*  654 */       ((InterfaceEntry)this.parser.currentModule).addContained(localPragmaEntry);
/*      */     }
/*      */ 
/*  657 */     if (str.equals("ID")) {
/*  658 */       idPragma();
/*  659 */     } else if (str.equals("prefix")) {
/*  660 */       prefixPragma();
/*  661 */     } else if (str.equals("version")) {
/*  662 */       versionPragma();
/*      */     }
/*  675 */     else if (str.equals("sun_local")) {
/*  676 */       localPragma();
/*  677 */     } else if (str.equals("sun_localservant")) {
/*  678 */       localServantPragma();
/*      */     }
/*      */     else {
/*  681 */       otherPragmas(str, tokenToString());
/*  682 */       this.token = this.scanner.getToken();
/*      */     }
/*      */ 
/*  685 */     this.scanner.escapedOK = false;
/*      */   }
/*      */ 
/*      */   private void localPragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  696 */     this.parser.token = this.token;
/*      */ 
/*  699 */     SymtabEntry localSymtabEntry1 = new SymtabEntry();
/*  700 */     SymtabEntry localSymtabEntry2 = this.parser.scopedName(this.parser.currentModule, localSymtabEntry1);
/*      */ 
/*  702 */     if (localSymtabEntry2 == localSymtabEntry1)
/*      */     {
/*  704 */       System.out.println("Error occured ");
/*      */ 
/*  706 */       this.scanner.skipLineComment();
/*  707 */       this.token = this.scanner.getToken();
/*      */     }
/*      */     else
/*      */     {
/*  713 */       if ((localSymtabEntry2 instanceof InterfaceEntry)) {
/*  714 */         localObject = (InterfaceEntry)localSymtabEntry2;
/*  715 */         ((InterfaceEntry)localObject).setInterfaceType(4);
/*      */       }
/*  717 */       this.token = this.parser.token;
/*  718 */       Object localObject = this.token.name;
/*  719 */       match(204);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void localServantPragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  727 */     this.parser.token = this.token;
/*      */ 
/*  730 */     SymtabEntry localSymtabEntry1 = new SymtabEntry();
/*  731 */     SymtabEntry localSymtabEntry2 = this.parser.scopedName(this.parser.currentModule, localSymtabEntry1);
/*      */ 
/*  734 */     if (localSymtabEntry2 == localSymtabEntry1)
/*      */     {
/*  737 */       this.scanner.skipLineComment();
/*  738 */       this.token = this.scanner.getToken();
/*  739 */       System.out.println("Error occured ");
/*      */     }
/*      */     else
/*      */     {
/*  745 */       if ((localSymtabEntry2 instanceof InterfaceEntry)) {
/*  746 */         localObject = (InterfaceEntry)localSymtabEntry2;
/*  747 */         ((InterfaceEntry)localObject).setInterfaceType(3);
/*      */       }
/*  749 */       this.token = this.parser.token;
/*  750 */       Object localObject = this.token.name;
/*  751 */       match(204);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void idPragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  763 */     this.parser.token = this.token;
/*      */ 
/*  767 */     this.parser.isModuleLegalType(true);
/*  768 */     SymtabEntry localSymtabEntry1 = new SymtabEntry();
/*  769 */     SymtabEntry localSymtabEntry2 = this.parser.scopedName(this.parser.currentModule, localSymtabEntry1);
/*  770 */     this.parser.isModuleLegalType(false);
/*      */ 
/*  773 */     if (localSymtabEntry2 == localSymtabEntry1)
/*      */     {
/*  776 */       this.scanner.skipLineComment();
/*  777 */       this.token = this.scanner.getToken();
/*      */     }
/*      */     else
/*      */     {
/*  788 */       this.token = this.parser.token;
/*  789 */       String str = this.token.name;
/*      */ 
/*  792 */       if (this.PragmaIDs.contains(localSymtabEntry2))
/*      */       {
/*  794 */         ParseException.badRepIDAlreadyAssigned(this.scanner, localSymtabEntry2.name());
/*      */       }
/*  796 */       else if (!RepositoryID.hasValidForm(str))
/*      */       {
/*  798 */         ParseException.badRepIDForm(this.scanner, str);
/*      */       }
/*      */       else
/*      */       {
/*  802 */         localSymtabEntry2.repositoryID(new RepositoryID(str));
/*  803 */         this.PragmaIDs.addElement(localSymtabEntry2);
/*      */       }
/*  805 */       match(204);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void prefixPragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  814 */     String str = this.token.name;
/*  815 */     match(204);
/*  816 */     ((IDLID)Parser.repIDStack.peek()).prefix(str);
/*  817 */     ((IDLID)Parser.repIDStack.peek()).name("");
/*      */   }
/*      */ 
/*      */   private void versionPragma()
/*      */     throws IOException, ParseException
/*      */   {
/*  826 */     this.parser.token = this.token;
/*      */ 
/*  829 */     this.parser.isModuleLegalType(true);
/*  830 */     SymtabEntry localSymtabEntry1 = new SymtabEntry();
/*  831 */     SymtabEntry localSymtabEntry2 = this.parser.scopedName(this.parser.currentModule, localSymtabEntry1);
/*      */ 
/*  833 */     this.parser.isModuleLegalType(false);
/*  834 */     if (localSymtabEntry2 == localSymtabEntry1)
/*      */     {
/*  837 */       this.scanner.skipLineComment();
/*  838 */       this.token = this.scanner.getToken();
/*      */     }
/*      */     else
/*      */     {
/*  842 */       this.token = this.parser.token;
/*  843 */       String str = this.token.name;
/*  844 */       match(203);
/*  845 */       if ((localSymtabEntry2.repositoryID() instanceof IDLID))
/*  846 */         ((IDLID)localSymtabEntry2.repositoryID()).version(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   void registerPragma(PragmaHandler paramPragmaHandler)
/*      */   {
/*  857 */     this.pragmaHandlers.addElement(paramPragmaHandler);
/*      */   }
/*      */ 
/*      */   private void otherPragmas(String paramString1, String paramString2)
/*      */     throws IOException
/*      */   {
/*  865 */     for (int i = this.pragmaHandlers.size() - 1; i >= 0; i--)
/*      */     {
/*  867 */       PragmaHandler localPragmaHandler = (PragmaHandler)this.pragmaHandlers.elementAt(i);
/*  868 */       if (localPragmaHandler.process(paramString1, paramString2))
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   String currentToken()
/*      */   {
/*  883 */     return tokenToString();
/*      */   }
/*      */ 
/*      */   SymtabEntry getEntryForName(String paramString)
/*      */   {
/*  894 */     int i = 0;
/*  895 */     int j = 0;
/*      */ 
/*  898 */     if (paramString.startsWith("::"))
/*      */     {
/*  900 */       j = 1;
/*  901 */       paramString = paramString.substring(2);
/*      */     }
/*  903 */     int k = paramString.indexOf("::");
/*  904 */     while (k >= 0)
/*      */     {
/*  906 */       i = 1;
/*  907 */       paramString = paramString.substring(0, k) + '/' + paramString.substring(k + 2);
/*  908 */       k = paramString.indexOf("::");
/*      */     }
/*      */ 
/*  912 */     SymtabEntry localSymtabEntry = null;
/*  913 */     if (j != 0)
/*  914 */       localSymtabEntry = this.parser.recursiveQualifiedEntry(paramString);
/*  915 */     else if (i != 0)
/*  916 */       localSymtabEntry = this.parser.recursivePQEntry(paramString, this.parser.currentModule);
/*      */     else
/*  918 */       localSymtabEntry = this.parser.unqualifiedEntryWMod(paramString, this.parser.currentModule);
/*  919 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   String getStringToEOL()
/*      */     throws IOException
/*      */   {
/*  929 */     return this.scanner.getStringToEOL();
/*      */   }
/*      */ 
/*      */   String getUntil(char paramChar)
/*      */     throws IOException
/*      */   {
/*  942 */     return this.scanner.getUntil(paramChar);
/*      */   }
/*      */ 
/*      */   private String tokenToString()
/*      */   {
/*  952 */     if (this.token.equals(81))
/*      */     {
/*  954 */       this.lastWasMacroID = true;
/*  955 */       return this.token.name;
/*      */     }
/*  957 */     if (this.token.equals(80)) {
/*  958 */       return this.token.name;
/*      */     }
/*  960 */     return this.token.toString();
/*      */   }
/*      */ 
/*      */   String nextToken()
/*      */     throws IOException
/*      */   {
/*  968 */     if (this.lastWasMacroID)
/*      */     {
/*  970 */       this.lastWasMacroID = false;
/*  971 */       return "(";
/*      */     }
/*      */ 
/*  975 */     this.token = this.scanner.getToken();
/*  976 */     return tokenToString();
/*      */   }
/*      */ 
/*      */   SymtabEntry scopedName()
/*      */     throws IOException
/*      */   {
/*  988 */     int i = 0;
/*  989 */     int j = 0;
/*  990 */     String str = null;
/*  991 */     SymtabEntry localSymtabEntry = null;
/*      */     try
/*      */     {
/*  994 */       if (this.token.equals(124)) {
/*  995 */         i = 1;
/*      */       }
/*  998 */       else if (this.token.equals(20))
/*      */       {
/* 1000 */         str = "Object";
/* 1001 */         match(20);
/*      */       }
/* 1003 */       else if (this.token.type == 45)
/*      */       {
/* 1005 */         str = "ValueBase";
/* 1006 */         match(45);
/*      */       }
/*      */       else
/*      */       {
/* 1010 */         str = this.token.name;
/* 1011 */         match(80);
/*      */       }
/*      */ 
/* 1014 */       while (this.token.equals(124))
/*      */       {
/* 1016 */         match(124);
/* 1017 */         j = 1;
/* 1018 */         if (str != null)
/* 1019 */           str = str + '/' + this.token.name;
/*      */         else
/* 1021 */           str = this.token.name;
/* 1022 */         match(80);
/*      */       }
/* 1024 */       if (i != 0)
/* 1025 */         localSymtabEntry = this.parser.recursiveQualifiedEntry(str);
/* 1026 */       else if (j != 0)
/* 1027 */         localSymtabEntry = this.parser.recursivePQEntry(str, this.parser.currentModule);
/*      */       else
/* 1029 */         localSymtabEntry = this.parser.unqualifiedEntryWMod(str, this.parser.currentModule);
/*      */     }
/*      */     catch (ParseException localParseException)
/*      */     {
/* 1033 */       localSymtabEntry = null;
/*      */     }
/* 1035 */     return localSymtabEntry;
/*      */   }
/*      */ 
/*      */   void skipToEOL()
/*      */     throws IOException
/*      */   {
/* 1043 */     this.scanner.skipLineComment();
/*      */   }
/*      */ 
/*      */   String skipUntil(char paramChar)
/*      */     throws IOException
/*      */   {
/* 1052 */     if ((!this.lastWasMacroID) || (paramChar != '('))
/* 1053 */       this.token = this.scanner.skipUntil(paramChar);
/* 1054 */     return tokenToString();
/*      */   }
/*      */ 
/*      */   void parseException(String paramString)
/*      */   {
/* 1064 */     if (!this.parser.noWarn)
/* 1065 */       ParseException.warning(this.scanner, paramString);
/*      */   }
/*      */ 
/*      */   String expandMacro(String paramString, Token paramToken)
/*      */     throws IOException, ParseException
/*      */   {
/* 1077 */     this.token = paramToken;
/*      */ 
/* 1079 */     Vector localVector1 = getParmValues();
/*      */ 
/* 1085 */     this.scanner.scanString(paramString + '\n');
/* 1086 */     Vector localVector2 = new Vector();
/* 1087 */     macro(localVector2);
/*      */ 
/* 1089 */     if (localVector1.size() < localVector2.size())
/* 1090 */       throw ParseException.syntaxError(this.scanner, 104, 109);
/* 1091 */     if (localVector1.size() > localVector2.size()) {
/* 1092 */       throw ParseException.syntaxError(this.scanner, 109, 104);
/*      */     }
/* 1094 */     paramString = this.scanner.getStringToEOL();
/* 1095 */     for (int i = 0; i < localVector2.size(); i++)
/* 1096 */       paramString = replaceAll(paramString, (String)localVector2.elementAt(i), (String)localVector1.elementAt(i));
/* 1097 */     return removeDoublePound(paramString);
/*      */   }
/*      */ 
/*      */   private void miniMatch(int paramInt)
/*      */     throws ParseException
/*      */   {
/* 1110 */     if (!this.token.equals(paramInt))
/* 1111 */       throw ParseException.syntaxError(this.scanner, paramInt, this.token.type);
/*      */   }
/*      */ 
/*      */   private Vector getParmValues()
/*      */     throws IOException, ParseException
/*      */   {
/* 1119 */     Vector localVector = new Vector();
/* 1120 */     if (this.token.equals(80))
/*      */     {
/* 1122 */       match(80);
/* 1123 */       miniMatch(108);
/*      */     }
/* 1125 */     else if (!this.token.equals(81)) {
/* 1126 */       throw ParseException.syntaxError(this.scanner, 80, this.token.type);
/*      */     }
/* 1128 */     if (!this.token.equals(109))
/*      */     {
/* 1130 */       localVector.addElement(this.scanner.getUntil(',', ')').trim());
/* 1131 */       this.token = this.scanner.getToken();
/* 1132 */       macroParmValues(localVector);
/*      */     }
/* 1134 */     return localVector;
/*      */   }
/*      */ 
/*      */   private void macroParmValues(Vector paramVector)
/*      */     throws IOException, ParseException
/*      */   {
/* 1142 */     while (!this.token.equals(109))
/*      */     {
/* 1144 */       miniMatch(104);
/* 1145 */       paramVector.addElement(this.scanner.getUntil(',', ')').trim());
/* 1146 */       this.token = this.scanner.getToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void macro(Vector paramVector)
/*      */     throws IOException, ParseException
/*      */   {
/* 1155 */     match(this.token.type);
/* 1156 */     match(108);
/* 1157 */     macroParms(paramVector);
/* 1158 */     miniMatch(109);
/*      */   }
/*      */ 
/*      */   private void macroParms(Vector paramVector)
/*      */     throws IOException, ParseException
/*      */   {
/* 1166 */     if (!this.token.equals(109))
/*      */     {
/* 1168 */       paramVector.addElement(this.token.name);
/* 1169 */       match(80);
/* 1170 */       macroParms2(paramVector);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void macroParms2(Vector paramVector)
/*      */     throws IOException, ParseException
/*      */   {
/* 1179 */     while (!this.token.equals(109))
/*      */     {
/* 1181 */       match(104);
/* 1182 */       paramVector.addElement(this.token.name);
/* 1183 */       match(80);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String replaceAll(String paramString1, String paramString2, String paramString3)
/*      */   {
/* 1192 */     int i = 0;
/* 1193 */     while (i != -1)
/*      */     {
/* 1195 */       i = paramString1.indexOf(paramString2, i);
/* 1196 */       if (i != -1)
/*      */       {
/* 1198 */         if (!embedded(paramString1, i, i + paramString2.length()))
/* 1199 */           if ((i > 0) && (paramString1.charAt(i) == '#'))
/* 1200 */             paramString1 = paramString1.substring(0, i) + '"' + paramString3 + '"' + paramString1.substring(i + paramString2.length());
/*      */           else
/* 1202 */             paramString1 = paramString1.substring(0, i) + paramString3 + paramString1.substring(i + paramString2.length());
/* 1203 */         i += paramString3.length();
/*      */       }
/*      */     }
/* 1206 */     return paramString1;
/*      */   }
/*      */ 
/*      */   private boolean embedded(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 1216 */     boolean bool = false;
/* 1217 */     int i = paramInt1 == 0 ? ' ' : paramString.charAt(paramInt1 - 1);
/* 1218 */     int j = paramInt2 >= paramString.length() - 1 ? ' ' : paramString.charAt(paramInt2);
/* 1219 */     if (((i >= 97) && (i <= 122)) || ((i >= 65) && (i <= 90)))
/* 1220 */       bool = true;
/* 1221 */     else if (((j >= 97) && (j <= 122)) || ((j >= 65) && (j <= 90)) || ((j >= 48) && (j <= 57)) || (j == 95))
/* 1222 */       bool = true;
/*      */     else
/* 1224 */       bool = inQuotes(paramString, paramInt1);
/* 1225 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean inQuotes(String paramString, int paramInt)
/*      */   {
/* 1233 */     int i = 0;
/* 1234 */     for (int j = 0; j < paramInt; j++) {
/* 1235 */       if (paramString.charAt(j) == '"') i++;
/*      */     }
/*      */ 
/* 1238 */     return i % 2 != 0;
/*      */   }
/*      */ 
/*      */   private String removeDoublePound(String paramString)
/*      */   {
/* 1246 */     int i = 0;
/* 1247 */     while (i != -1)
/*      */     {
/* 1249 */       i = paramString.indexOf("##", i);
/* 1250 */       if (i != -1)
/*      */       {
/* 1252 */         int j = i - 1;
/* 1253 */         int k = i + 2;
/* 1254 */         if (j < 0)
/* 1255 */           j = 0;
/* 1256 */         if (k >= paramString.length())
/* 1257 */           k = paramString.length() - 1;
/* 1258 */         while ((j > 0) && (
/* 1259 */           (paramString
/* 1259 */           .charAt(j) == 
/* 1259 */           ' ') || 
/* 1260 */           (paramString
/* 1260 */           .charAt(j) == 
/* 1260 */           '\t')))
/* 1261 */           j--;
/* 1262 */         while ((k < paramString.length() - 1) && (
/* 1263 */           (paramString
/* 1263 */           .charAt(k) == 
/* 1263 */           ' ') || 
/* 1264 */           (paramString
/* 1264 */           .charAt(k) == 
/* 1264 */           '\t')))
/* 1265 */           k++;
/* 1266 */         paramString = paramString.substring(0, j + 1) + paramString.substring(k);
/*      */       }
/*      */     }
/* 1269 */     return paramString;
/*      */   }
/*      */ 
/*      */   private String getFilename(String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/* 1280 */     String str = null;
/* 1281 */     File localFile = new File(paramString);
/* 1282 */     if (localFile.canRead()) {
/* 1283 */       str = paramString;
/*      */     }
/*      */     else {
/* 1286 */       Enumeration localEnumeration = this.parser.paths.elements();
/* 1287 */       while ((!localFile.canRead()) && (localEnumeration.hasMoreElements()))
/*      */       {
/* 1289 */         str = (String)localEnumeration.nextElement() + File.separatorChar + paramString;
/* 1290 */         localFile = new File(str);
/*      */       }
/* 1292 */       if (!localFile.canRead())
/* 1293 */         throw new FileNotFoundException(paramString);
/*      */     }
/* 1295 */     return str;
/*      */   }
/*      */ 
/*      */   private void match(int paramInt)
/*      */     throws IOException, ParseException
/*      */   {
/* 1303 */     if (!this.token.equals(paramInt))
/* 1304 */       throw ParseException.syntaxError(this.scanner, paramInt, this.token.type);
/* 1305 */     this.token = this.scanner.getToken();
/*      */ 
/* 1318 */     if ((this.token.equals(80)) || (this.token.equals(81)))
/*      */     {
/* 1320 */       String str = (String)this.symbols.get(this.token.name);
/* 1321 */       if ((str != null) && (!str.equals("")))
/*      */       {
/* 1323 */         if (this.macros.contains(this.token.name))
/*      */         {
/* 1325 */           this.scanner.scanString(expandMacro(str, this.token));
/* 1326 */           this.token = this.scanner.getToken();
/*      */         }
/*      */         else
/*      */         {
/* 1331 */           this.scanner.scanString(str);
/* 1332 */           this.token = this.scanner.getToken();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void issueTokenWarnings()
/*      */   {
/* 1343 */     if (this.parser.noWarn);
/*      */   }
/*      */ 
/*      */   void openScope(SymtabEntry paramSymtabEntry)
/*      */   {
/* 1374 */     for (int i = this.pragmaHandlers.size() - 1; i >= 0; i--)
/*      */     {
/* 1376 */       PragmaHandler localPragmaHandler = (PragmaHandler)this.pragmaHandlers.elementAt(i);
/* 1377 */       localPragmaHandler.openScope(paramSymtabEntry);
/*      */     }
/*      */   }
/*      */ 
/*      */   void closeScope(SymtabEntry paramSymtabEntry)
/*      */   {
/* 1392 */     for (int i = this.pragmaHandlers.size() - 1; i >= 0; i--)
/*      */     {
/* 1394 */       PragmaHandler localPragmaHandler = (PragmaHandler)this.pragmaHandlers.elementAt(i);
/* 1395 */       localPragmaHandler.closeScope(paramSymtabEntry);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Preprocessor
 * JD-Core Version:    0.6.2
 */