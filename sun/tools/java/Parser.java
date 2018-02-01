/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Vector;
/*      */ import sun.tools.tree.AddExpression;
/*      */ import sun.tools.tree.AndExpression;
/*      */ import sun.tools.tree.ArrayAccessExpression;
/*      */ import sun.tools.tree.ArrayExpression;
/*      */ import sun.tools.tree.AssignAddExpression;
/*      */ import sun.tools.tree.AssignBitAndExpression;
/*      */ import sun.tools.tree.AssignBitOrExpression;
/*      */ import sun.tools.tree.AssignBitXorExpression;
/*      */ import sun.tools.tree.AssignDivideExpression;
/*      */ import sun.tools.tree.AssignExpression;
/*      */ import sun.tools.tree.AssignMultiplyExpression;
/*      */ import sun.tools.tree.AssignOpExpression;
/*      */ import sun.tools.tree.AssignRemainderExpression;
/*      */ import sun.tools.tree.AssignShiftLeftExpression;
/*      */ import sun.tools.tree.AssignShiftRightExpression;
/*      */ import sun.tools.tree.AssignSubtractExpression;
/*      */ import sun.tools.tree.AssignUnsignedShiftRightExpression;
/*      */ import sun.tools.tree.BitAndExpression;
/*      */ import sun.tools.tree.BitNotExpression;
/*      */ import sun.tools.tree.BitOrExpression;
/*      */ import sun.tools.tree.BitXorExpression;
/*      */ import sun.tools.tree.BooleanExpression;
/*      */ import sun.tools.tree.BreakStatement;
/*      */ import sun.tools.tree.CaseStatement;
/*      */ import sun.tools.tree.CastExpression;
/*      */ import sun.tools.tree.CatchStatement;
/*      */ import sun.tools.tree.CharExpression;
/*      */ import sun.tools.tree.CommaExpression;
/*      */ import sun.tools.tree.CompoundStatement;
/*      */ import sun.tools.tree.ConditionalExpression;
/*      */ import sun.tools.tree.ContinueStatement;
/*      */ import sun.tools.tree.DeclarationStatement;
/*      */ import sun.tools.tree.DivideExpression;
/*      */ import sun.tools.tree.DoStatement;
/*      */ import sun.tools.tree.DoubleExpression;
/*      */ import sun.tools.tree.EqualExpression;
/*      */ import sun.tools.tree.ExprExpression;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.ExpressionStatement;
/*      */ import sun.tools.tree.FieldExpression;
/*      */ import sun.tools.tree.FinallyStatement;
/*      */ import sun.tools.tree.FloatExpression;
/*      */ import sun.tools.tree.ForStatement;
/*      */ import sun.tools.tree.GreaterExpression;
/*      */ import sun.tools.tree.GreaterOrEqualExpression;
/*      */ import sun.tools.tree.IdentifierExpression;
/*      */ import sun.tools.tree.IfStatement;
/*      */ import sun.tools.tree.InstanceOfExpression;
/*      */ import sun.tools.tree.IntExpression;
/*      */ import sun.tools.tree.LessExpression;
/*      */ import sun.tools.tree.LessOrEqualExpression;
/*      */ import sun.tools.tree.LocalMember;
/*      */ import sun.tools.tree.LongExpression;
/*      */ import sun.tools.tree.MethodExpression;
/*      */ import sun.tools.tree.MultiplyExpression;
/*      */ import sun.tools.tree.NegativeExpression;
/*      */ import sun.tools.tree.NewArrayExpression;
/*      */ import sun.tools.tree.NewInstanceExpression;
/*      */ import sun.tools.tree.Node;
/*      */ import sun.tools.tree.NotEqualExpression;
/*      */ import sun.tools.tree.NotExpression;
/*      */ import sun.tools.tree.NullExpression;
/*      */ import sun.tools.tree.OrExpression;
/*      */ import sun.tools.tree.PositiveExpression;
/*      */ import sun.tools.tree.PostDecExpression;
/*      */ import sun.tools.tree.PostIncExpression;
/*      */ import sun.tools.tree.PreDecExpression;
/*      */ import sun.tools.tree.PreIncExpression;
/*      */ import sun.tools.tree.RemainderExpression;
/*      */ import sun.tools.tree.ReturnStatement;
/*      */ import sun.tools.tree.ShiftLeftExpression;
/*      */ import sun.tools.tree.ShiftRightExpression;
/*      */ import sun.tools.tree.Statement;
/*      */ import sun.tools.tree.StringExpression;
/*      */ import sun.tools.tree.SubtractExpression;
/*      */ import sun.tools.tree.SuperExpression;
/*      */ import sun.tools.tree.SwitchStatement;
/*      */ import sun.tools.tree.SynchronizedStatement;
/*      */ import sun.tools.tree.ThisExpression;
/*      */ import sun.tools.tree.ThrowStatement;
/*      */ import sun.tools.tree.TryStatement;
/*      */ import sun.tools.tree.TypeExpression;
/*      */ import sun.tools.tree.UnsignedShiftRightExpression;
/*      */ import sun.tools.tree.VarDeclarationStatement;
/*      */ import sun.tools.tree.WhileStatement;
/*      */ 
/*      */ public class Parser extends Scanner
/*      */   implements ParserActions, Constants
/*      */ {
/*      */   ParserActions actions;
/*  274 */   private Node[] args = new Node[32];
/*  275 */   protected int argIndex = 0;
/*      */ 
/* 1505 */   private int aCount = 0;
/* 1506 */   private Type[] aTypes = new Type[8];
/* 1507 */   private IdentifierToken[] aNames = new IdentifierToken[this.aTypes.length];
/*      */   private ClassDefinition curClass;
/* 1842 */   private int FPstate = 0;
/*      */   protected Scanner scanner;
/*      */ 
/*      */   protected Parser(Environment paramEnvironment, InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*   73 */     super(paramEnvironment, paramInputStream);
/*   74 */     this.scanner = this;
/*   75 */     this.actions = this;
/*      */   }
/*      */ 
/*      */   protected Parser(Scanner paramScanner)
/*      */     throws IOException
/*      */   {
/*   82 */     super(paramScanner.env);
/*   83 */     this.scanner = paramScanner;
/*   84 */     this.env = paramScanner.env;
/*   85 */     this.token = paramScanner.token;
/*   86 */     this.pos = paramScanner.pos;
/*   87 */     this.actions = this;
/*      */   }
/*      */ 
/*      */   public Parser(Scanner paramScanner, ParserActions paramParserActions)
/*      */     throws IOException
/*      */   {
/*   94 */     this(paramScanner);
/*   95 */     this.actions = paramParserActions;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken)
/*      */   {
/*  127 */     packageDeclaration(paramLong, paramIdentifierToken.id);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void packageDeclaration(long paramLong, Identifier paramIdentifier)
/*      */   {
/*  134 */     throw new RuntimeException("beginClass method is abstract");
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void importClass(long paramLong, IdentifierToken paramIdentifierToken)
/*      */   {
/*  145 */     importClass(paramLong, paramIdentifierToken.id);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void importClass(long paramLong, Identifier paramIdentifier)
/*      */   {
/*  152 */     throw new RuntimeException("importClass method is abstract");
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void importPackage(long paramLong, IdentifierToken paramIdentifierToken)
/*      */   {
/*  163 */     importPackage(paramLong, paramIdentifierToken.id);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void importPackage(long paramLong, Identifier paramIdentifier)
/*      */   {
/*  170 */     throw new RuntimeException("importPackage method is abstract");
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken)
/*      */   {
/*  184 */     Identifier localIdentifier = paramIdentifierToken2 == null ? null : paramIdentifierToken2.id;
/*  185 */     Identifier[] arrayOfIdentifier = null;
/*  186 */     if (paramArrayOfIdentifierToken != null) {
/*  187 */       arrayOfIdentifier = new Identifier[paramArrayOfIdentifierToken.length];
/*  188 */       for (int i = 0; i < paramArrayOfIdentifierToken.length; i++) {
/*  189 */         arrayOfIdentifier[i] = paramArrayOfIdentifierToken[i].id;
/*      */       }
/*      */     }
/*  192 */     beginClass(paramLong, paramString, paramInt, paramIdentifierToken1.id, localIdentifier, arrayOfIdentifier);
/*  193 */     return getCurrentClass();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void beginClass(long paramLong, String paramString, int paramInt, Identifier paramIdentifier1, Identifier paramIdentifier2, Identifier[] paramArrayOfIdentifier)
/*      */   {
/*  201 */     throw new RuntimeException("beginClass method is abstract");
/*      */   }
/*      */ 
/*      */   protected ClassDefinition getCurrentClass()
/*      */   {
/*  210 */     return null;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void endClass(long paramLong, ClassDefinition paramClassDefinition)
/*      */   {
/*  221 */     endClass(paramLong, paramClassDefinition.getName().getFlatName().getName());
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void endClass(long paramLong, Identifier paramIdentifier)
/*      */   {
/*  228 */     throw new RuntimeException("endClass method is abstract");
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode)
/*      */   {
/*  242 */     Identifier[] arrayOfIdentifier1 = null;
/*  243 */     Identifier[] arrayOfIdentifier2 = null;
/*      */     int i;
/*  244 */     if (paramArrayOfIdentifierToken1 != null) {
/*  245 */       arrayOfIdentifier1 = new Identifier[paramArrayOfIdentifierToken1.length];
/*  246 */       for (i = 0; i < paramArrayOfIdentifierToken1.length; i++) {
/*  247 */         arrayOfIdentifier1[i] = paramArrayOfIdentifierToken1[i].id;
/*      */       }
/*      */     }
/*  250 */     if (paramArrayOfIdentifierToken2 != null) {
/*  251 */       arrayOfIdentifier2 = new Identifier[paramArrayOfIdentifierToken2.length];
/*  252 */       for (i = 0; i < paramArrayOfIdentifierToken2.length; i++) {
/*  253 */         arrayOfIdentifier2[i] = paramArrayOfIdentifierToken2[i].id;
/*      */       }
/*      */     }
/*  256 */     defineField(paramLong, paramString, paramInt, paramType, paramIdentifierToken.id, arrayOfIdentifier1, arrayOfIdentifier2, paramNode);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void defineField(long paramLong, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, Identifier[] paramArrayOfIdentifier1, Identifier[] paramArrayOfIdentifier2, Node paramNode)
/*      */   {
/*  266 */     throw new RuntimeException("defineField method is abstract");
/*      */   }
/*      */ 
/*      */   protected final void addArgument(Node paramNode)
/*      */   {
/*  278 */     if (this.argIndex == this.args.length) {
/*  279 */       Node[] arrayOfNode = new Node[this.args.length * 2];
/*  280 */       System.arraycopy(this.args, 0, arrayOfNode, 0, this.args.length);
/*  281 */       this.args = arrayOfNode;
/*      */     }
/*  283 */     this.args[(this.argIndex++)] = paramNode;
/*      */   }
/*      */   protected final Expression[] exprArgs(int paramInt) {
/*  286 */     Expression[] arrayOfExpression = new Expression[this.argIndex - paramInt];
/*  287 */     System.arraycopy(this.args, paramInt, arrayOfExpression, 0, this.argIndex - paramInt);
/*  288 */     this.argIndex = paramInt;
/*  289 */     return arrayOfExpression;
/*      */   }
/*      */   protected final Statement[] statArgs(int paramInt) {
/*  292 */     Statement[] arrayOfStatement = new Statement[this.argIndex - paramInt];
/*  293 */     System.arraycopy(this.args, paramInt, arrayOfStatement, 0, this.argIndex - paramInt);
/*  294 */     this.argIndex = paramInt;
/*  295 */     return arrayOfStatement;
/*      */   }
/*      */ 
/*      */   protected void expect(int paramInt)
/*      */     throws SyntaxError, IOException
/*      */   {
/*  303 */     if (this.token != paramInt) {
/*  304 */       switch (paramInt) {
/*      */       case 60:
/*  306 */         this.env.error(this.scanner.prevPos, "identifier.expected");
/*  307 */         break;
/*      */       default:
/*  309 */         this.env.error(this.scanner.prevPos, "token.expected", opNames[paramInt]);
/*      */       }
/*      */ 
/*  312 */       throw new SyntaxError();
/*      */     }
/*  314 */     scan();
/*      */   }
/*      */ 
/*      */   protected Expression parseTypeExpression()
/*      */     throws SyntaxError, IOException
/*      */   {
/*  321 */     switch (this.token) {
/*      */     case 77:
/*  323 */       return new TypeExpression(scan(), Type.tVoid);
/*      */     case 78:
/*  325 */       return new TypeExpression(scan(), Type.tBoolean);
/*      */     case 70:
/*  327 */       return new TypeExpression(scan(), Type.tByte);
/*      */     case 71:
/*  329 */       return new TypeExpression(scan(), Type.tChar);
/*      */     case 72:
/*  331 */       return new TypeExpression(scan(), Type.tShort);
/*      */     case 73:
/*  333 */       return new TypeExpression(scan(), Type.tInt);
/*      */     case 74:
/*  335 */       return new TypeExpression(scan(), Type.tLong);
/*      */     case 75:
/*  337 */       return new TypeExpression(scan(), Type.tFloat);
/*      */     case 76:
/*  339 */       return new TypeExpression(scan(), Type.tDouble);
/*      */     case 60:
/*  341 */       Object localObject = new IdentifierExpression(this.pos, this.scanner.idValue);
/*  342 */       scan();
/*  343 */       while (this.token == 46) {
/*  344 */         localObject = new FieldExpression(scan(), (Expression)localObject, this.scanner.idValue);
/*  345 */         expect(60);
/*      */       }
/*  347 */       return localObject;
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*  350 */     case 69: } this.env.error(this.pos, "type.expected");
/*  351 */     throw new SyntaxError();
/*      */   }
/*      */ 
/*      */   protected Expression parseMethodExpression(Expression paramExpression, Identifier paramIdentifier)
/*      */     throws SyntaxError, IOException
/*      */   {
/*  359 */     long l = scan();
/*  360 */     int i = this.argIndex;
/*  361 */     if (this.token != 141) {
/*  362 */       addArgument(parseExpression());
/*  363 */       while (this.token == 0) {
/*  364 */         scan();
/*  365 */         addArgument(parseExpression());
/*      */       }
/*      */     }
/*  368 */     expect(141);
/*  369 */     return new MethodExpression(l, paramExpression, paramIdentifier, exprArgs(i));
/*      */   }
/*      */ 
/*      */   protected Expression parseNewInstanceExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*      */     throws SyntaxError, IOException
/*      */   {
/*  377 */     int i = this.argIndex;
/*  378 */     expect(140);
/*  379 */     if (this.token != 141) {
/*  380 */       addArgument(parseExpression());
/*  381 */       while (this.token == 0) {
/*  382 */         scan();
/*  383 */         addArgument(parseExpression());
/*      */       }
/*      */     }
/*  386 */     expect(141);
/*  387 */     ClassDefinition localClassDefinition = null;
/*  388 */     if ((this.token == 138) && (!(paramExpression2 instanceof TypeExpression))) {
/*  389 */       long l = this.pos;
/*      */ 
/*  391 */       Identifier localIdentifier = FieldExpression.toIdentifier(paramExpression2);
/*  392 */       if (localIdentifier == null) {
/*  393 */         this.env.error(paramExpression2.getWhere(), "type.expected");
/*      */       }
/*  395 */       Vector localVector1 = new Vector(1);
/*  396 */       Vector localVector2 = new Vector(0);
/*  397 */       localVector1.addElement(new IdentifierToken(idNull));
/*  398 */       if ((this.token == 113) || (this.token == 112)) {
/*  399 */         this.env.error(this.pos, "anonymous.extends");
/*  400 */         parseInheritance(localVector1, localVector2);
/*      */       }
/*  402 */       localClassDefinition = parseClassBody(new IdentifierToken(l, idNull), 196608, 56, null, localVector1, localVector2, paramExpression2
/*  404 */         .getWhere());
/*      */     }
/*  406 */     if ((paramExpression1 == null) && (localClassDefinition == null)) {
/*  407 */       return new NewInstanceExpression(paramLong, paramExpression2, exprArgs(i));
/*      */     }
/*  409 */     return new NewInstanceExpression(paramLong, paramExpression2, exprArgs(i), paramExpression1, localClassDefinition);
/*      */   }
/*      */ 
/*      */   protected Expression parseTerm()
/*      */     throws SyntaxError, IOException
/*      */   {
/*      */     long l3;
/*      */     Object localObject;
/*      */     long l2;
/*      */     int m;
/*  416 */     switch (this.token) {
/*      */     case 63:
/*  418 */       char c = this.scanner.charValue;
/*  419 */       return new CharExpression(scan(), c);
/*      */     case 65:
/*  422 */       int i = this.scanner.intValue;
/*  423 */       l3 = scan();
/*  424 */       if ((i < 0) && (this.radix == 10)) this.env.error(l3, "overflow.int.dec");
/*  425 */       return new IntExpression(l3, i);
/*      */     case 66:
/*  428 */       long l1 = this.scanner.longValue;
/*  429 */       long l4 = scan();
/*  430 */       if ((l1 < 0L) && (this.radix == 10)) this.env.error(l4, "overflow.long.dec");
/*  431 */       return new LongExpression(l4, l1);
/*      */     case 67:
/*  434 */       float f1 = this.scanner.floatValue;
/*  435 */       return new FloatExpression(scan(), f1);
/*      */     case 68:
/*  438 */       double d1 = this.scanner.doubleValue;
/*  439 */       return new DoubleExpression(scan(), d1);
/*      */     case 69:
/*  442 */       localObject = this.scanner.stringValue;
/*  443 */       return new StringExpression(scan(), (String)localObject);
/*      */     case 60:
/*  446 */       localObject = this.scanner.idValue;
/*  447 */       l3 = scan();
/*      */ 
/*  449 */       return this.token == 140 ? 
/*  449 */         parseMethodExpression(null, (Identifier)localObject) : 
/*  449 */         new IdentifierExpression(l3, (Identifier)localObject);
/*      */     case 80:
/*  453 */       return new BooleanExpression(scan(), true);
/*      */     case 81:
/*  455 */       return new BooleanExpression(scan(), false);
/*      */     case 84:
/*  457 */       return new NullExpression(scan());
/*      */     case 82:
/*  460 */       localObject = new ThisExpression(scan());
/*  461 */       return this.token == 140 ? parseMethodExpression((Expression)localObject, idInit) : localObject;
/*      */     case 83:
/*  464 */       localObject = new SuperExpression(scan());
/*  465 */       return this.token == 140 ? parseMethodExpression((Expression)localObject, idInit) : localObject;
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*  477 */       return parseTypeExpression();
/*      */     case 29:
/*  480 */       l2 = scan();
/*  481 */       switch (this.token) {
/*      */       case 65:
/*  483 */         int j = this.scanner.intValue;
/*  484 */         long l7 = scan();
/*  485 */         if ((j < 0) && (this.radix == 10)) this.env.error(l7, "overflow.int.dec");
/*  486 */         return new IntExpression(l7, j);
/*      */       case 66:
/*  489 */         long l5 = this.scanner.longValue;
/*  490 */         long l8 = scan();
/*  491 */         if ((l5 < 0L) && (this.radix == 10)) this.env.error(l8, "overflow.long.dec");
/*  492 */         return new LongExpression(l8, l5);
/*      */       case 67:
/*  495 */         float f2 = this.scanner.floatValue;
/*  496 */         return new FloatExpression(scan(), f2);
/*      */       case 68:
/*  499 */         double d2 = this.scanner.doubleValue;
/*  500 */         return new DoubleExpression(scan(), d2);
/*      */       }
/*      */ 
/*  503 */       return new PositiveExpression(l2, parseTerm());
/*      */     case 30:
/*  506 */       l2 = scan();
/*  507 */       switch (this.token) {
/*      */       case 65:
/*  509 */         int k = -this.scanner.intValue;
/*  510 */         return new IntExpression(scan(), k);
/*      */       case 66:
/*  513 */         long l6 = -this.scanner.longValue;
/*  514 */         return new LongExpression(scan(), l6);
/*      */       case 67:
/*  517 */         float f3 = -this.scanner.floatValue;
/*  518 */         return new FloatExpression(scan(), f3);
/*      */       case 68:
/*  521 */         double d3 = -this.scanner.doubleValue;
/*  522 */         return new DoubleExpression(scan(), d3);
/*      */       }
/*      */ 
/*  525 */       return new NegativeExpression(l2, parseTerm());
/*      */     case 37:
/*  528 */       return new NotExpression(scan(), parseTerm());
/*      */     case 38:
/*  530 */       return new BitNotExpression(scan(), parseTerm());
/*      */     case 50:
/*  532 */       return new PreIncExpression(scan(), parseTerm());
/*      */     case 51:
/*  534 */       return new PreDecExpression(scan(), parseTerm());
/*      */     case 140:
/*  538 */       l2 = scan();
/*  539 */       Expression localExpression1 = parseExpression();
/*  540 */       expect(141);
/*      */ 
/*  542 */       if (localExpression1.getOp() == 147)
/*      */       {
/*  544 */         return new CastExpression(l2, localExpression1, parseTerm());
/*      */       }
/*      */ 
/*  547 */       switch (this.token)
/*      */       {
/*      */       case 50:
/*  555 */         return new PostIncExpression(scan(), localExpression1);
/*      */       case 51:
/*  559 */         return new PostDecExpression(scan(), localExpression1);
/*      */       case 37:
/*      */       case 38:
/*      */       case 49:
/*      */       case 60:
/*      */       case 63:
/*      */       case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 140:
/*  578 */         return new CastExpression(l2, localExpression1, parseTerm());
/*      */       }
/*  580 */       return new ExprExpression(l2, localExpression1);
/*      */     case 138:
/*  585 */       l2 = scan();
/*  586 */       m = this.argIndex;
/*  587 */       if (this.token != 139) {
/*  588 */         addArgument(parseExpression());
/*  589 */         while (this.token == 0) {
/*  590 */           scan();
/*  591 */           if (this.token == 139) {
/*      */             break;
/*      */           }
/*  594 */           addArgument(parseExpression());
/*      */         }
/*      */       }
/*  597 */       expect(139);
/*  598 */       return new ArrayExpression(l2, exprArgs(m));
/*      */     case 49:
/*  602 */       l2 = scan();
/*  603 */       m = this.argIndex;
/*      */ 
/*  605 */       if (this.token == 140) {
/*  606 */         scan();
/*  607 */         localExpression2 = parseExpression();
/*  608 */         expect(141);
/*  609 */         this.env.error(l2, "not.supported", "new(...)");
/*  610 */         return new NullExpression(l2);
/*      */       }
/*      */ 
/*  613 */       Expression localExpression2 = parseTypeExpression();
/*      */ 
/*  615 */       if (this.token == 142) {
/*  616 */         while (this.token == 142) {
/*  617 */           scan();
/*  618 */           addArgument(this.token != 143 ? parseExpression() : null);
/*  619 */           expect(143);
/*      */         }
/*  621 */         Expression[] arrayOfExpression = exprArgs(m);
/*  622 */         if (this.token == 138) {
/*  623 */           return new NewArrayExpression(l2, localExpression2, arrayOfExpression, parseTerm());
/*      */         }
/*  625 */         return new NewArrayExpression(l2, localExpression2, arrayOfExpression);
/*      */       }
/*  627 */       return parseNewInstanceExpression(l2, null, localExpression2);
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 59:
/*      */     case 61:
/*      */     case 62:
/*      */     case 64:
/*      */     case 79:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 89:
/*      */     case 90:
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 100:
/*      */     case 101:
/*      */     case 102:
/*      */     case 103:
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 115:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 120:
/*      */     case 121:
/*      */     case 122:
/*      */     case 123:
/*      */     case 124:
/*      */     case 125:
/*      */     case 126:
/*      */     case 127:
/*      */     case 128:
/*      */     case 129:
/*      */     case 130:
/*      */     case 131:
/*      */     case 132:
/*      */     case 133:
/*      */     case 134:
/*      */     case 135:
/*      */     case 136:
/*      */     case 137:
/*  633 */     case 139: } this.env.error(this.scanner.prevPos, "missing.term");
/*  634 */     return new IntExpression(this.pos, 0);
/*      */   }
/*      */ 
/*      */   protected Expression parseExpression()
/*      */     throws SyntaxError, IOException
/*      */   {
/*  641 */     for (Object localObject = parseTerm(); localObject != null; localObject = ((Expression)localObject).order()) {
/*  642 */       Expression localExpression = parseBinaryExpression((Expression)localObject);
/*  643 */       if (localExpression == null)
/*  644 */         return localObject;
/*  645 */       localObject = localExpression;
/*      */     }
/*      */ 
/*  648 */     return null;
/*      */   }
/*      */ 
/*      */   protected Expression parseBinaryExpression(Expression paramExpression)
/*      */     throws SyntaxError, IOException
/*      */   {
/*  655 */     if (paramExpression != null)
/*      */     {
/*      */       long l1;
/*      */       Object localObject;
/*  656 */       switch (this.token)
/*      */       {
/*      */       case 142:
/*  659 */         l1 = scan();
/*  660 */         Expression localExpression1 = this.token != 143 ? parseExpression() : null;
/*  661 */         expect(143);
/*  662 */         paramExpression = new ArrayAccessExpression(l1, paramExpression, localExpression1);
/*  663 */         break;
/*      */       case 50:
/*  667 */         paramExpression = new PostIncExpression(scan(), paramExpression);
/*  668 */         break;
/*      */       case 51:
/*  670 */         paramExpression = new PostDecExpression(scan(), paramExpression);
/*  671 */         break;
/*      */       case 46:
/*  673 */         l1 = scan();
/*      */         long l2;
/*  674 */         if (this.token == 82)
/*      */         {
/*  677 */           l2 = scan();
/*  678 */           if (this.token == 140) {
/*  679 */             paramExpression = new ThisExpression(l2, paramExpression);
/*  680 */             paramExpression = parseMethodExpression(paramExpression, idInit);
/*      */           } else {
/*  682 */             paramExpression = new FieldExpression(l1, paramExpression, idThis);
/*      */           }
/*      */ 
/*      */         }
/*  686 */         else if (this.token == 83)
/*      */         {
/*  691 */           l2 = scan();
/*  692 */           if (this.token == 140) {
/*  693 */             paramExpression = new SuperExpression(l2, paramExpression);
/*  694 */             paramExpression = parseMethodExpression(paramExpression, idInit);
/*      */           }
/*      */           else
/*      */           {
/*  698 */             paramExpression = new FieldExpression(l1, paramExpression, idSuper);
/*      */           }
/*      */ 
/*      */         }
/*  702 */         else if (this.token == 49)
/*      */         {
/*  704 */           scan();
/*  705 */           if (this.token != 60)
/*  706 */             expect(60);
/*  707 */           paramExpression = parseNewInstanceExpression(l1, paramExpression, parseTypeExpression());
/*      */         }
/*  710 */         else if (this.token == 111)
/*      */         {
/*  713 */           scan();
/*  714 */           paramExpression = new FieldExpression(l1, paramExpression, idClass);
/*      */         }
/*      */         else {
/*  717 */           localObject = this.scanner.idValue;
/*  718 */           expect(60);
/*  719 */           if (this.token == 140)
/*  720 */             paramExpression = parseMethodExpression(paramExpression, (Identifier)localObject);
/*      */           else
/*  722 */             paramExpression = new FieldExpression(l1, paramExpression, (Identifier)localObject);
/*      */         }
/*  724 */         break;
/*      */       case 25:
/*  727 */         paramExpression = new InstanceOfExpression(scan(), paramExpression, parseTerm());
/*  728 */         break;
/*      */       case 29:
/*  730 */         paramExpression = new AddExpression(scan(), paramExpression, parseTerm());
/*  731 */         break;
/*      */       case 30:
/*  733 */         paramExpression = new SubtractExpression(scan(), paramExpression, parseTerm());
/*  734 */         break;
/*      */       case 33:
/*  736 */         paramExpression = new MultiplyExpression(scan(), paramExpression, parseTerm());
/*  737 */         break;
/*      */       case 31:
/*  739 */         paramExpression = new DivideExpression(scan(), paramExpression, parseTerm());
/*  740 */         break;
/*      */       case 32:
/*  742 */         paramExpression = new RemainderExpression(scan(), paramExpression, parseTerm());
/*  743 */         break;
/*      */       case 26:
/*  745 */         paramExpression = new ShiftLeftExpression(scan(), paramExpression, parseTerm());
/*  746 */         break;
/*      */       case 27:
/*  748 */         paramExpression = new ShiftRightExpression(scan(), paramExpression, parseTerm());
/*  749 */         break;
/*      */       case 28:
/*  751 */         paramExpression = new UnsignedShiftRightExpression(scan(), paramExpression, parseTerm());
/*  752 */         break;
/*      */       case 24:
/*  754 */         paramExpression = new LessExpression(scan(), paramExpression, parseTerm());
/*  755 */         break;
/*      */       case 23:
/*  757 */         paramExpression = new LessOrEqualExpression(scan(), paramExpression, parseTerm());
/*  758 */         break;
/*      */       case 22:
/*  760 */         paramExpression = new GreaterExpression(scan(), paramExpression, parseTerm());
/*  761 */         break;
/*      */       case 21:
/*  763 */         paramExpression = new GreaterOrEqualExpression(scan(), paramExpression, parseTerm());
/*  764 */         break;
/*      */       case 20:
/*  766 */         paramExpression = new EqualExpression(scan(), paramExpression, parseTerm());
/*  767 */         break;
/*      */       case 19:
/*  769 */         paramExpression = new NotEqualExpression(scan(), paramExpression, parseTerm());
/*  770 */         break;
/*      */       case 18:
/*  772 */         paramExpression = new BitAndExpression(scan(), paramExpression, parseTerm());
/*  773 */         break;
/*      */       case 17:
/*  775 */         paramExpression = new BitXorExpression(scan(), paramExpression, parseTerm());
/*  776 */         break;
/*      */       case 16:
/*  778 */         paramExpression = new BitOrExpression(scan(), paramExpression, parseTerm());
/*  779 */         break;
/*      */       case 15:
/*  781 */         paramExpression = new AndExpression(scan(), paramExpression, parseTerm());
/*  782 */         break;
/*      */       case 14:
/*  784 */         paramExpression = new OrExpression(scan(), paramExpression, parseTerm());
/*  785 */         break;
/*      */       case 1:
/*  787 */         paramExpression = new AssignExpression(scan(), paramExpression, parseTerm());
/*  788 */         break;
/*      */       case 2:
/*  790 */         paramExpression = new AssignMultiplyExpression(scan(), paramExpression, parseTerm());
/*  791 */         break;
/*      */       case 3:
/*  793 */         paramExpression = new AssignDivideExpression(scan(), paramExpression, parseTerm());
/*  794 */         break;
/*      */       case 4:
/*  796 */         paramExpression = new AssignRemainderExpression(scan(), paramExpression, parseTerm());
/*  797 */         break;
/*      */       case 5:
/*  799 */         paramExpression = new AssignAddExpression(scan(), paramExpression, parseTerm());
/*  800 */         break;
/*      */       case 6:
/*  802 */         paramExpression = new AssignSubtractExpression(scan(), paramExpression, parseTerm());
/*  803 */         break;
/*      */       case 7:
/*  805 */         paramExpression = new AssignShiftLeftExpression(scan(), paramExpression, parseTerm());
/*  806 */         break;
/*      */       case 8:
/*  808 */         paramExpression = new AssignShiftRightExpression(scan(), paramExpression, parseTerm());
/*  809 */         break;
/*      */       case 9:
/*  811 */         paramExpression = new AssignUnsignedShiftRightExpression(scan(), paramExpression, parseTerm());
/*  812 */         break;
/*      */       case 10:
/*  814 */         paramExpression = new AssignBitAndExpression(scan(), paramExpression, parseTerm());
/*  815 */         break;
/*      */       case 11:
/*  817 */         paramExpression = new AssignBitOrExpression(scan(), paramExpression, parseTerm());
/*  818 */         break;
/*      */       case 12:
/*  820 */         paramExpression = new AssignBitXorExpression(scan(), paramExpression, parseTerm());
/*  821 */         break;
/*      */       case 137:
/*  823 */         l1 = scan();
/*  824 */         localObject = parseExpression();
/*  825 */         expect(136);
/*  826 */         Expression localExpression2 = parseExpression();
/*      */ 
/*  833 */         if (((localExpression2 instanceof AssignExpression)) || ((localExpression2 instanceof AssignOpExpression)))
/*      */         {
/*  835 */           this.env.error(localExpression2.getWhere(), "assign.in.conditionalexpr");
/*      */         }
/*      */ 
/*  838 */         paramExpression = new ConditionalExpression(l1, paramExpression, (Expression)localObject, localExpression2);
/*  839 */         break;
/*      */       case 13:
/*      */       case 34:
/*      */       case 35:
/*      */       case 36:
/*      */       case 37:
/*      */       case 38:
/*      */       case 39:
/*      */       case 40:
/*      */       case 41:
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 47:
/*      */       case 48:
/*      */       case 49:
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
/*      */       case 80:
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
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*      */       case 127:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 131:
/*      */       case 132:
/*      */       case 133:
/*      */       case 134:
/*      */       case 135:
/*      */       case 136:
/*      */       case 138:
/*      */       case 139:
/*      */       case 140:
/*      */       case 141:
/*      */       default:
/*  843 */         return null;
/*      */       }
/*      */     }
/*  846 */     return paramExpression;
/*      */   }
/*      */ 
/*      */   protected boolean recoverStatement()
/*      */     throws SyntaxError, IOException
/*      */   {
/*      */     while (true)
/*      */     {
/*  856 */       switch (this.token)
/*      */       {
/*      */       case -1:
/*      */       case 90:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*      */       case 98:
/*      */       case 99:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 138:
/*      */       case 139:
/*  871 */         return true;
/*      */       case 77:
/*      */       case 111:
/*      */       case 114:
/*      */       case 120:
/*      */       case 121:
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*  882 */         expect(139);
/*  883 */         return false;
/*      */       case 140:
/*  886 */         match(140, 141);
/*  887 */         scan();
/*  888 */         break;
/*      */       case 142:
/*  891 */         match(142, 143);
/*  892 */         scan();
/*  893 */         break;
/*      */       }
/*      */ 
/*  897 */       scan();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Statement parseDeclaration(long paramLong, int paramInt, Expression paramExpression)
/*      */     throws SyntaxError, IOException
/*      */   {
/*  908 */     int i = this.argIndex;
/*  909 */     if (this.token == 60) {
/*  910 */       addArgument(new VarDeclarationStatement(this.pos, parseExpression()));
/*  911 */       while (this.token == 0) {
/*  912 */         scan();
/*  913 */         addArgument(new VarDeclarationStatement(this.pos, parseExpression()));
/*      */       }
/*      */     }
/*  916 */     return new DeclarationStatement(paramLong, paramInt, paramExpression, statArgs(i));
/*      */   }
/*      */ 
/*      */   protected void topLevelExpression(Expression paramExpression)
/*      */   {
/*  924 */     switch (paramExpression.getOp()) {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 39:
/*      */     case 40:
/*      */     case 42:
/*      */     case 44:
/*      */     case 45:
/*      */     case 47:
/*  943 */       return;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 41:
/*      */     case 43:
/*  945 */     case 46: } this.env.error(paramExpression.getWhere(), "invalid.expr");
/*      */   }
/*      */ 
/*      */   protected Statement parseStatement()
/*      */     throws SyntaxError, IOException
/*      */   {
/*      */     long l1;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     int m;
/*      */     Expression localExpression1;
/*      */     Object localObject3;
/*  952 */     switch (this.token) {
/*      */     case 135:
/*  954 */       return new CompoundStatement(scan(), new Statement[0]);
/*      */     case 138:
/*  957 */       return parseBlockStatement();
/*      */     case 90:
/*  962 */       l1 = scan();
/*      */ 
/*  964 */       expect(140);
/*  965 */       localObject1 = parseExpression();
/*  966 */       expect(141);
/*  967 */       localObject2 = parseStatement();
/*  968 */       if (this.token == 91) {
/*  969 */         scan();
/*  970 */         return new IfStatement(l1, (Expression)localObject1, (Statement)localObject2, parseStatement());
/*      */       }
/*  972 */       return new IfStatement(l1, (Expression)localObject1, (Statement)localObject2, null);
/*      */     case 91:
/*  978 */       this.env.error(scan(), "else.without.if");
/*  979 */       return parseStatement();
/*      */     case 92:
/*  984 */       l1 = scan();
/*  985 */       localObject1 = null;
/*  986 */       localObject2 = null; Object localObject4 = null;
/*      */ 
/*  988 */       expect(140);
/*      */       long l3;
/*  989 */       if (this.token != 135) {
/*  990 */         l3 = this.pos;
/*  991 */         int n = parseModifiers(16);
/*  992 */         Object localObject6 = parseExpression();
/*      */ 
/*  994 */         if (this.token == 60) {
/*  995 */           localObject1 = parseDeclaration(l3, n, (Expression)localObject6);
/*      */         } else {
/*  997 */           if (n != 0) {
/*  998 */             expect(60);
/*      */           }
/* 1000 */           topLevelExpression((Expression)localObject6);
/* 1001 */           while (this.token == 0) {
/* 1002 */             long l5 = scan();
/* 1003 */             Expression localExpression5 = parseExpression();
/* 1004 */             topLevelExpression(localExpression5);
/* 1005 */             localObject6 = new CommaExpression(l5, (Expression)localObject6, localExpression5);
/*      */           }
/* 1007 */           localObject1 = new ExpressionStatement(l3, (Expression)localObject6);
/*      */         }
/*      */       }
/* 1010 */       expect(135);
/* 1011 */       if (this.token != 135) {
/* 1012 */         localObject2 = parseExpression();
/*      */       }
/* 1014 */       expect(135);
/* 1015 */       if (this.token != 141) {
/* 1016 */         localObject4 = parseExpression();
/* 1017 */         topLevelExpression((Expression)localObject4);
/* 1018 */         while (this.token == 0) {
/* 1019 */           l3 = scan();
/* 1020 */           Expression localExpression3 = parseExpression();
/* 1021 */           topLevelExpression(localExpression3);
/* 1022 */           localObject4 = new CommaExpression(l3, (Expression)localObject4, localExpression3);
/*      */         }
/*      */       }
/* 1025 */       expect(141);
/* 1026 */       return new ForStatement(l1, (Statement)localObject1, (Expression)localObject2, (Expression)localObject4, parseStatement());
/*      */     case 93:
/* 1031 */       l1 = scan();
/*      */ 
/* 1033 */       expect(140);
/* 1034 */       localObject1 = parseExpression();
/* 1035 */       expect(141);
/* 1036 */       return new WhileStatement(l1, (Expression)localObject1, parseStatement());
/*      */     case 94:
/* 1041 */       l1 = scan();
/*      */ 
/* 1043 */       localObject1 = parseStatement();
/* 1044 */       expect(93);
/* 1045 */       expect(140);
/* 1046 */       localObject2 = parseExpression();
/* 1047 */       expect(141);
/* 1048 */       expect(135);
/* 1049 */       return new DoStatement(l1, (Statement)localObject1, (Expression)localObject2);
/*      */     case 98:
/* 1054 */       l1 = scan();
/* 1055 */       localObject1 = null;
/*      */ 
/* 1057 */       if (this.token == 60) {
/* 1058 */         localObject1 = this.scanner.idValue;
/* 1059 */         scan();
/*      */       }
/* 1061 */       expect(135);
/* 1062 */       return new BreakStatement(l1, (Identifier)localObject1);
/*      */     case 99:
/* 1067 */       l1 = scan();
/* 1068 */       localObject1 = null;
/*      */ 
/* 1070 */       if (this.token == 60) {
/* 1071 */         localObject1 = this.scanner.idValue;
/* 1072 */         scan();
/*      */       }
/* 1074 */       expect(135);
/* 1075 */       return new ContinueStatement(l1, (Identifier)localObject1);
/*      */     case 100:
/* 1081 */       l1 = scan();
/* 1082 */       localObject1 = null;
/*      */ 
/* 1084 */       if (this.token != 135) {
/* 1085 */         localObject1 = parseExpression();
/*      */       }
/* 1087 */       expect(135);
/* 1088 */       return new ReturnStatement(l1, (Expression)localObject1);
/*      */     case 95:
/* 1093 */       l1 = scan();
/* 1094 */       int i = this.argIndex;
/*      */ 
/* 1096 */       expect(140);
/* 1097 */       localObject2 = parseExpression();
/* 1098 */       expect(141);
/* 1099 */       expect(138);
/*      */ 
/* 1101 */       while ((this.token != -1) && (this.token != 139)) {
/* 1102 */         m = this.argIndex;
/*      */         try {
/* 1104 */           switch (this.token)
/*      */           {
/*      */           case 96:
/* 1107 */             addArgument(new CaseStatement(scan(), parseExpression()));
/* 1108 */             expect(136);
/* 1109 */             break;
/*      */           case 97:
/* 1113 */             addArgument(new CaseStatement(scan(), null));
/* 1114 */             expect(136);
/* 1115 */             break;
/*      */           default:
/* 1118 */             addArgument(parseStatement());
/*      */           }
/*      */         }
/*      */         catch (SyntaxError localSyntaxError) {
/* 1122 */           this.argIndex = m;
/* 1123 */           if (!recoverStatement()) {
/* 1124 */             throw localSyntaxError;
/*      */           }
/*      */         }
/*      */       }
/* 1128 */       expect(139);
/* 1129 */       return new SwitchStatement(l1, (Expression)localObject2, statArgs(i));
/*      */     case 96:
/* 1134 */       this.env.error(this.pos, "case.without.switch");
/* 1135 */       while (this.token == 96) {
/* 1136 */         scan();
/* 1137 */         parseExpression();
/* 1138 */         expect(136);
/*      */       }
/* 1140 */       return parseStatement();
/*      */     case 97:
/* 1145 */       this.env.error(this.pos, "default.without.switch");
/* 1146 */       scan();
/* 1147 */       expect(136);
/* 1148 */       return parseStatement();
/*      */     case 101:
/* 1153 */       l1 = scan();
/* 1154 */       localExpression1 = null;
/* 1155 */       int k = this.argIndex;
/* 1156 */       m = 0;
/*      */ 
/* 1176 */       Object localObject5 = parseBlockStatement();
/*      */ 
/* 1178 */       if (localExpression1 != null);
/* 1182 */       while (this.token == 102) {
/* 1183 */         long l4 = this.pos;
/* 1184 */         expect(102);
/* 1185 */         expect(140);
/* 1186 */         int i1 = parseModifiers(16);
/* 1187 */         Expression localExpression4 = parseExpression();
/* 1188 */         IdentifierToken localIdentifierToken = this.scanner.getIdToken();
/* 1189 */         expect(60);
/* 1190 */         localIdentifierToken.modifiers = i1;
/*      */ 
/* 1196 */         expect(141);
/* 1197 */         addArgument(new CatchStatement(l4, localExpression4, localIdentifierToken, parseBlockStatement()));
/* 1198 */         m = 1;
/*      */       }
/*      */ 
/* 1201 */       if (m != 0) {
/* 1202 */         localObject5 = new TryStatement(l1, (Statement)localObject5, statArgs(k));
/*      */       }
/* 1204 */       if (this.token == 103) {
/* 1205 */         scan();
/* 1206 */         return new FinallyStatement(l1, (Statement)localObject5, parseBlockStatement());
/* 1207 */       }if ((m != 0) || (localExpression1 != null)) {
/* 1208 */         return localObject5;
/*      */       }
/* 1210 */       this.env.error(this.pos, "try.without.catch.finally");
/* 1211 */       return new TryStatement(l1, (Statement)localObject5, null);
/*      */     case 102:
/* 1217 */       this.env.error(this.pos, "catch.without.try");
/*      */       Statement localStatement1;
/*      */       do
/*      */       {
/* 1221 */         scan();
/* 1222 */         expect(140);
/* 1223 */         parseModifiers(16);
/* 1224 */         parseExpression();
/* 1225 */         expect(60);
/* 1226 */         expect(141);
/* 1227 */         localStatement1 = parseBlockStatement();
/* 1228 */       }while (this.token == 102);
/*      */ 
/* 1230 */       if (this.token == 103) {
/* 1231 */         scan();
/* 1232 */         localStatement1 = parseBlockStatement();
/*      */       }
/* 1234 */       return localStatement1;
/*      */     case 103:
/* 1239 */       this.env.error(this.pos, "finally.without.try");
/* 1240 */       scan();
/* 1241 */       return parseBlockStatement();
/*      */     case 104:
/* 1246 */       l2 = scan();
/* 1247 */       localExpression1 = parseExpression();
/* 1248 */       expect(135);
/* 1249 */       return new ThrowStatement(l2, localExpression1);
/*      */     case 58:
/* 1253 */       l2 = scan();
/* 1254 */       expect(60);
/* 1255 */       expect(135);
/* 1256 */       this.env.error(l2, "not.supported", "goto");
/* 1257 */       return new CompoundStatement(l2, new Statement[0]);
/*      */     case 126:
/* 1262 */       l2 = scan();
/* 1263 */       expect(140);
/* 1264 */       localExpression1 = parseExpression();
/* 1265 */       expect(141);
/* 1266 */       return new SynchronizedStatement(l2, localExpression1, parseBlockStatement());
/*      */     case 111:
/*      */     case 114:
/* 1272 */       return parseLocalClass(0);
/*      */     case 123:
/*      */     case 128:
/*      */     case 130:
/*      */     case 131:
/* 1279 */       l2 = this.pos;
/*      */ 
/* 1288 */       int j = parseModifiers(2098192);
/*      */ 
/* 1291 */       switch (this.token) {
/*      */       case 111:
/*      */       case 114:
/* 1294 */         return parseLocalClass(j);
/*      */       case 60:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 78:
/* 1305 */         if ((j & 0x200400) != 0) {
/* 1306 */           j &= -2098177;
/* 1307 */           expect(111);
/*      */         }
/* 1309 */         localObject3 = parseExpression();
/* 1310 */         if (this.token != 60) {
/* 1311 */           expect(60);
/*      */         }
/*      */ 
/* 1314 */         Statement localStatement2 = parseDeclaration(l2, j, (Expression)localObject3);
/* 1315 */         expect(135);
/* 1316 */         return localStatement2;
/*      */       }
/*      */ 
/* 1320 */       this.env.error(this.pos, "type.expected");
/* 1321 */       throw new SyntaxError();
/*      */     case 77:
/*      */     case 120:
/*      */     case 121:
/*      */     case 124:
/*      */     case 125:
/* 1331 */       this.env.error(this.pos, "statement.expected");
/* 1332 */       throw new SyntaxError();
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 89:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 112:
/*      */     case 113:
/*      */     case 115:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 122:
/*      */     case 127:
/*      */     case 129:
/*      */     case 132:
/*      */     case 133:
/*      */     case 134:
/*      */     case 136:
/* 1335 */     case 137: } long l2 = this.pos;
/* 1336 */     Expression localExpression2 = parseExpression();
/*      */ 
/* 1338 */     if (this.token == 60)
/*      */     {
/* 1340 */       localObject3 = parseDeclaration(l2, 0, localExpression2);
/* 1341 */       expect(135);
/* 1342 */       return localObject3;
/*      */     }
/* 1344 */     if (this.token == 136)
/*      */     {
/* 1346 */       scan();
/* 1347 */       localObject3 = parseStatement();
/* 1348 */       ((Statement)localObject3).setLabel(this.env, localExpression2);
/* 1349 */       return localObject3;
/*      */     }
/*      */ 
/* 1353 */     topLevelExpression(localExpression2);
/* 1354 */     expect(135);
/* 1355 */     return new ExpressionStatement(l2, localExpression2);
/*      */   }
/*      */ 
/*      */   protected Statement parseBlockStatement() throws SyntaxError, IOException
/*      */   {
/* 1360 */     if (this.token != 138)
/*      */     {
/* 1363 */       this.env.error(this.scanner.prevPos, "token.expected", opNames['']);
/* 1364 */       return parseStatement();
/*      */     }
/* 1366 */     long l = scan();
/* 1367 */     int i = this.argIndex;
/* 1368 */     while ((this.token != -1) && (this.token != 139)) {
/* 1369 */       int j = this.argIndex;
/*      */       try {
/* 1371 */         addArgument(parseStatement());
/*      */       } catch (SyntaxError localSyntaxError) {
/* 1373 */         this.argIndex = j;
/* 1374 */         if (!recoverStatement()) {
/* 1375 */           throw localSyntaxError;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1380 */     expect(139);
/* 1381 */     return new CompoundStatement(l, statArgs(i));
/*      */   }
/*      */ 
/*      */   protected IdentifierToken parseName(boolean paramBoolean)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1391 */     IdentifierToken localIdentifierToken = this.scanner.getIdToken();
/* 1392 */     expect(60);
/*      */ 
/* 1394 */     if (this.token != 46) {
/* 1395 */       return localIdentifierToken;
/*      */     }
/*      */ 
/* 1398 */     StringBuffer localStringBuffer = new StringBuffer(localIdentifierToken.id.toString());
/*      */ 
/* 1400 */     while (this.token == 46) {
/* 1401 */       scan();
/* 1402 */       if ((this.token == 33) && (paramBoolean)) {
/* 1403 */         scan();
/* 1404 */         localStringBuffer.append(".*");
/* 1405 */         break;
/*      */       }
/*      */ 
/* 1408 */       localStringBuffer.append('.');
/* 1409 */       if (this.token == 60) {
/* 1410 */         localStringBuffer.append(this.scanner.idValue);
/*      */       }
/* 1412 */       expect(60);
/*      */     }
/*      */ 
/* 1415 */     localIdentifierToken.id = Identifier.lookup(localStringBuffer.toString());
/* 1416 */     return localIdentifierToken;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected Identifier parseIdentifier(boolean paramBoolean)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1424 */     return parseName(paramBoolean).id;
/*      */   }
/*      */ 
/*      */   protected Type parseType()
/*      */     throws SyntaxError, IOException
/*      */   {
/*      */     Type localType;
/* 1434 */     switch (this.token) {
/*      */     case 60:
/* 1436 */       localType = Type.tClass(parseName(false).id);
/* 1437 */       break;
/*      */     case 77:
/* 1439 */       scan();
/* 1440 */       localType = Type.tVoid;
/* 1441 */       break;
/*      */     case 78:
/* 1443 */       scan();
/* 1444 */       localType = Type.tBoolean;
/* 1445 */       break;
/*      */     case 70:
/* 1447 */       scan();
/* 1448 */       localType = Type.tByte;
/* 1449 */       break;
/*      */     case 71:
/* 1451 */       scan();
/* 1452 */       localType = Type.tChar;
/* 1453 */       break;
/*      */     case 72:
/* 1455 */       scan();
/* 1456 */       localType = Type.tShort;
/* 1457 */       break;
/*      */     case 73:
/* 1459 */       scan();
/* 1460 */       localType = Type.tInt;
/* 1461 */       break;
/*      */     case 75:
/* 1463 */       scan();
/* 1464 */       localType = Type.tFloat;
/* 1465 */       break;
/*      */     case 74:
/* 1467 */       scan();
/* 1468 */       localType = Type.tLong;
/* 1469 */       break;
/*      */     case 76:
/* 1471 */       scan();
/* 1472 */       localType = Type.tDouble;
/* 1473 */       break;
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     default:
/* 1475 */       this.env.error(this.pos, "type.expected");
/* 1476 */       throw new SyntaxError();
/*      */     }
/* 1478 */     return parseArrayBrackets(localType);
/*      */   }
/*      */ 
/*      */   protected Type parseArrayBrackets(Type paramType)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1488 */     while (this.token == 142) {
/* 1489 */       scan();
/* 1490 */       if (this.token != 143) {
/* 1491 */         this.env.error(this.pos, "array.dim.in.decl");
/* 1492 */         parseExpression();
/*      */       }
/* 1494 */       expect(143);
/* 1495 */       paramType = Type.tArray(paramType);
/*      */     }
/* 1497 */     return paramType;
/*      */   }
/*      */ 
/*      */   private void addArgument(int paramInt, Type paramType, IdentifierToken paramIdentifierToken)
/*      */   {
/* 1510 */     paramIdentifierToken.modifiers = paramInt;
/* 1511 */     if (this.aCount >= this.aTypes.length) {
/* 1512 */       Type[] arrayOfType = new Type[this.aCount * 2];
/* 1513 */       System.arraycopy(this.aTypes, 0, arrayOfType, 0, this.aCount);
/* 1514 */       this.aTypes = arrayOfType;
/* 1515 */       IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[this.aCount * 2];
/* 1516 */       System.arraycopy(this.aNames, 0, arrayOfIdentifierToken, 0, this.aCount);
/* 1517 */       this.aNames = arrayOfIdentifierToken;
/*      */     }
/* 1519 */     this.aTypes[this.aCount] = paramType;
/* 1520 */     this.aNames[(this.aCount++)] = paramIdentifierToken;
/*      */   }
/*      */ 
/*      */   protected int parseModifiers(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1531 */     int i = 0;
/*      */     while (true) {
/* 1533 */       if (this.token == 123)
/*      */       {
/* 1535 */         this.env.error(this.pos, "not.supported", "const");
/* 1536 */         scan();
/*      */       }
/* 1538 */       int j = 0;
/* 1539 */       switch (this.token) { case 120:
/* 1540 */         j = 2; break;
/*      */       case 121:
/* 1541 */         j = 1; break;
/*      */       case 122:
/* 1542 */         j = 4; break;
/*      */       case 124:
/* 1543 */         j = 8; break;
/*      */       case 125:
/* 1544 */         j = 128; break;
/*      */       case 128:
/* 1545 */         j = 16; break;
/*      */       case 130:
/* 1546 */         j = 1024; break;
/*      */       case 127:
/* 1547 */         j = 256; break;
/*      */       case 129:
/* 1548 */         j = 64; break;
/*      */       case 126:
/* 1549 */         j = 32; break;
/*      */       case 131:
/* 1550 */         j = 2097152;
/*      */       case 123: }
/* 1552 */       if ((j & paramInt) == 0) {
/*      */         break;
/*      */       }
/* 1555 */       if ((j & i) != 0) {
/* 1556 */         this.env.error(this.pos, "repeated.modifier");
/*      */       }
/* 1558 */       i |= j;
/* 1559 */       scan();
/*      */     }
/* 1561 */     return i;
/*      */   }
/*      */ 
/*      */   protected void parseField()
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1574 */     if (this.token == 135)
/*      */     {
/* 1576 */       scan();
/* 1577 */       return;
/*      */     }
/*      */ 
/* 1581 */     String str = this.scanner.docComment;
/*      */ 
/* 1584 */     long l = this.pos;
/*      */ 
/* 1587 */     int i = parseModifiers(2098687);
/*      */ 
/* 1592 */     if ((i == (i & 0x8)) && (this.token == 138))
/*      */     {
/* 1594 */       this.actions.defineField(l, this.curClass, str, i, 
/* 1595 */         Type.tMethod(Type.tVoid), 
/* 1595 */         new IdentifierToken(idClassInit), null, null, 
/* 1597 */         parseStatement());
/* 1598 */       return;
/*      */     }
/*      */ 
/* 1602 */     if ((this.token == 111) || (this.token == 114)) {
/* 1603 */       parseNamedClass(i, 111, str);
/* 1604 */       return;
/*      */     }
/*      */ 
/* 1608 */     l = this.pos;
/* 1609 */     Type localType = parseType();
/* 1610 */     IdentifierToken localIdentifierToken = null;
/*      */ 
/* 1615 */     switch (this.token) {
/*      */     case 60:
/* 1617 */       localIdentifierToken = this.scanner.getIdToken();
/* 1618 */       l = scan();
/* 1619 */       break;
/*      */     case 140:
/* 1623 */       localIdentifierToken = new IdentifierToken(idInit);
/* 1624 */       if ((i & 0x200000) != 0)
/* 1625 */         this.env.error(this.pos, "bad.constructor.modifier"); break;
/*      */     default:
/* 1629 */       expect(60);
/*      */     }
/*      */     Object localObject2;
/*      */     Object localObject1;
/* 1635 */     if (this.token == 140)
/*      */     {
/* 1637 */       scan();
/* 1638 */       this.aCount = 0;
/*      */ 
/* 1640 */       if (this.token != 141)
/*      */       {
/* 1643 */         int j = parseModifiers(16);
/* 1644 */         localObject2 = parseType();
/* 1645 */         localObject3 = this.scanner.getIdToken();
/* 1646 */         expect(60);
/*      */ 
/* 1649 */         localObject2 = parseArrayBrackets((Type)localObject2);
/* 1650 */         addArgument(j, (Type)localObject2, (IdentifierToken)localObject3);
/*      */ 
/* 1654 */         while (this.token == 0)
/*      */         {
/* 1656 */           scan();
/* 1657 */           j = parseModifiers(16);
/* 1658 */           localObject2 = parseType();
/* 1659 */           localObject3 = this.scanner.getIdToken();
/* 1660 */           expect(60);
/*      */ 
/* 1663 */           localObject2 = parseArrayBrackets((Type)localObject2);
/* 1664 */           addArgument(j, (Type)localObject2, (IdentifierToken)localObject3);
/*      */         }
/*      */       }
/* 1667 */       expect(141);
/*      */ 
/* 1670 */       localType = parseArrayBrackets(localType);
/*      */ 
/* 1673 */       localObject1 = new Type[this.aCount];
/* 1674 */       System.arraycopy(this.aTypes, 0, localObject1, 0, this.aCount);
/*      */ 
/* 1676 */       localObject2 = new IdentifierToken[this.aCount];
/* 1677 */       System.arraycopy(this.aNames, 0, localObject2, 0, this.aCount);
/*      */ 
/* 1680 */       localType = Type.tMethod(localType, (Type[])localObject1);
/*      */ 
/* 1683 */       Object localObject3 = null;
/* 1684 */       if (this.token == 144) {
/* 1685 */         Vector localVector = new Vector();
/* 1686 */         scan();
/* 1687 */         localVector.addElement(parseName(false));
/* 1688 */         while (this.token == 0) {
/* 1689 */           scan();
/* 1690 */           localVector.addElement(parseName(false));
/*      */         }
/*      */ 
/* 1693 */         localObject3 = new IdentifierToken[localVector.size()];
/* 1694 */         localVector.copyInto((Object[])localObject3);
/*      */       }
/*      */ 
/* 1699 */       switch (this.token)
/*      */       {
/*      */       case 138:
/* 1703 */         int k = this.FPstate;
/* 1704 */         if ((i & 0x200000) != 0)
/* 1705 */           this.FPstate = 2097152;
/*      */         else {
/* 1707 */           i |= this.FPstate & 0x200000;
/*      */         }
/*      */ 
/* 1710 */         this.actions.defineField(l, this.curClass, str, i, localType, localIdentifierToken, (IdentifierToken[])localObject2, (IdentifierToken[])localObject3, 
/* 1711 */           parseStatement());
/*      */ 
/* 1713 */         this.FPstate = k;
/*      */ 
/* 1715 */         break;
/*      */       case 135:
/* 1718 */         scan();
/* 1719 */         this.actions.defineField(l, this.curClass, str, i, localType, localIdentifierToken, (IdentifierToken[])localObject2, (IdentifierToken[])localObject3, null);
/*      */ 
/* 1721 */         break;
/*      */       default:
/* 1725 */         if ((i & 0x500) == 0)
/* 1726 */           expect(138);
/*      */         else
/* 1728 */           expect(135);
/*      */         break;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       while (true)
/*      */       {
/* 1736 */         l = this.pos;
/*      */ 
/* 1739 */         localObject1 = parseArrayBrackets(localType);
/*      */ 
/* 1742 */         localObject2 = null;
/* 1743 */         if (this.token == 1) {
/* 1744 */           scan();
/* 1745 */           localObject2 = parseExpression();
/*      */         }
/*      */ 
/* 1749 */         this.actions.defineField(l, this.curClass, str, i, (Type)localObject1, localIdentifierToken, null, null, (Node)localObject2);
/*      */ 
/* 1753 */         if (this.token != 0) {
/* 1754 */           expect(135);
/* 1755 */           return;
/*      */         }
/* 1757 */         scan();
/*      */ 
/* 1760 */         localIdentifierToken = this.scanner.getIdToken();
/* 1761 */         expect(60);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void recoverField(ClassDefinition paramClassDefinition)
/*      */     throws SyntaxError, IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1772 */       switch (this.token)
/*      */       {
/*      */       case -1:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 120:
/*      */       case 121:
/*      */       case 124:
/*      */       case 125:
/*      */       case 126:
/*      */       case 128:
/* 1791 */         return;
/*      */       case 138:
/* 1794 */         match(138, 139);
/* 1795 */         scan();
/* 1796 */         break;
/*      */       case 140:
/* 1799 */         match(140, 141);
/* 1800 */         scan();
/* 1801 */         break;
/*      */       case 142:
/* 1804 */         match(142, 143);
/* 1805 */         scan();
/* 1806 */         break;
/*      */       case 110:
/*      */       case 111:
/*      */       case 114:
/*      */       case 115:
/*      */       case 139:
/* 1814 */         this.actions.endClass(this.pos, paramClassDefinition);
/* 1815 */         throw new SyntaxError();
/*      */       }
/*      */ 
/* 1819 */       scan();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void parseClass()
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1829 */     String str = this.scanner.docComment;
/*      */ 
/* 1832 */     int i = parseModifiers(2098719);
/*      */ 
/* 1834 */     parseNamedClass(i, 115, str);
/*      */   }
/*      */ 
/*      */   protected Statement parseLocalClass(int paramInt)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1848 */     long l = this.pos;
/* 1849 */     ClassDefinition localClassDefinition = parseNamedClass(0x20000 | paramInt, 105, null);
/* 1850 */     Statement[] arrayOfStatement = { new VarDeclarationStatement(l, new LocalMember(localClassDefinition), null) };
/*      */ 
/* 1853 */     TypeExpression localTypeExpression = new TypeExpression(l, localClassDefinition.getType());
/* 1854 */     return new DeclarationStatement(l, 0, localTypeExpression, arrayOfStatement);
/*      */   }
/*      */ 
/*      */   protected ClassDefinition parseNamedClass(int paramInt1, int paramInt2, String paramString)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1864 */     switch (this.token) {
/*      */     case 114:
/* 1866 */       scan();
/* 1867 */       paramInt1 |= 512;
/* 1868 */       break;
/*      */     case 111:
/* 1871 */       scan();
/* 1872 */       break;
/*      */     default:
/* 1875 */       this.env.error(this.pos, "class.expected");
/*      */     }
/*      */ 
/* 1879 */     int i = this.FPstate;
/* 1880 */     if ((paramInt1 & 0x200000) != 0) {
/* 1881 */       this.FPstate = 2097152;
/*      */     }
/*      */     else
/*      */     {
/* 1885 */       paramInt1 |= this.FPstate & 0x200000;
/*      */     }
/*      */ 
/* 1889 */     IdentifierToken localIdentifierToken = this.scanner.getIdToken();
/* 1890 */     long l = this.pos;
/* 1891 */     expect(60);
/*      */ 
/* 1893 */     Vector localVector1 = new Vector();
/* 1894 */     Vector localVector2 = new Vector();
/* 1895 */     parseInheritance(localVector1, localVector2);
/*      */ 
/* 1897 */     ClassDefinition localClassDefinition = parseClassBody(localIdentifierToken, paramInt1, paramInt2, paramString, localVector1, localVector2, l);
/*      */ 
/* 1899 */     this.FPstate = i;
/*      */ 
/* 1901 */     return localClassDefinition;
/*      */   }
/*      */ 
/*      */   protected void parseInheritance(Vector paramVector1, Vector paramVector2) throws SyntaxError, IOException
/*      */   {
/* 1906 */     if (this.token == 112) {
/* 1907 */       scan();
/* 1908 */       paramVector1.addElement(parseName(false));
/* 1909 */       while (this.token == 0) {
/* 1910 */         scan();
/* 1911 */         paramVector1.addElement(parseName(false));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1916 */     if (this.token == 113) {
/* 1917 */       scan();
/* 1918 */       paramVector2.addElement(parseName(false));
/* 1919 */       while (this.token == 0) {
/* 1920 */         scan();
/* 1921 */         paramVector2.addElement(parseName(false));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ClassDefinition parseClassBody(IdentifierToken paramIdentifierToken, int paramInt1, int paramInt2, String paramString, Vector paramVector1, Vector paramVector2, long paramLong)
/*      */     throws SyntaxError, IOException
/*      */   {
/* 1935 */     IdentifierToken localIdentifierToken = null;
/* 1936 */     if ((paramInt1 & 0x200) != 0) {
/* 1937 */       if (paramVector2.size() > 0) {
/* 1938 */         this.env.error(((IdentifierToken)paramVector2.elementAt(0)).getWhere(), "intf.impl.intf");
/*      */       }
/*      */ 
/* 1941 */       paramVector2 = paramVector1;
/*      */     }
/* 1943 */     else if (paramVector1.size() > 0) {
/* 1944 */       if (paramVector1.size() > 1) {
/* 1945 */         this.env.error(((IdentifierToken)paramVector1.elementAt(1)).getWhere(), "multiple.inherit");
/*      */       }
/*      */ 
/* 1948 */       localIdentifierToken = (IdentifierToken)paramVector1.elementAt(0);
/*      */     }
/*      */ 
/* 1952 */     ClassDefinition localClassDefinition1 = this.curClass;
/*      */ 
/* 1955 */     IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[paramVector2.size()];
/* 1956 */     paramVector2.copyInto(arrayOfIdentifierToken);
/*      */ 
/* 1958 */     ClassDefinition localClassDefinition2 = this.actions
/* 1958 */       .beginClass(paramLong, paramString, paramInt1, paramIdentifierToken, localIdentifierToken, arrayOfIdentifierToken);
/*      */ 
/* 1961 */     expect(138);
/* 1962 */     while ((this.token != -1) && (this.token != 139)) {
/*      */       try {
/* 1964 */         this.curClass = localClassDefinition2;
/* 1965 */         parseField();
/*      */       } catch (SyntaxError localSyntaxError) {
/* 1967 */         recoverField(localClassDefinition2);
/*      */       } finally {
/* 1969 */         this.curClass = localClassDefinition1;
/*      */       }
/*      */     }
/* 1972 */     expect(139);
/*      */ 
/* 1975 */     this.actions.endClass(this.scanner.prevPos, localClassDefinition2);
/* 1976 */     return localClassDefinition2;
/*      */   }
/*      */ 
/*      */   protected void recoverFile()
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1986 */       switch (this.token)
/*      */       {
/*      */       case 111:
/*      */       case 114:
/* 1990 */         return;
/*      */       case 138:
/* 1993 */         match(138, 139);
/* 1994 */         scan();
/* 1995 */         break;
/*      */       case 140:
/* 1998 */         match(140, 141);
/* 1999 */         scan();
/* 2000 */         break;
/*      */       case 142:
/* 2003 */         match(142, 143);
/* 2004 */         scan();
/* 2005 */         break;
/*      */       case -1:
/* 2008 */         return;
/*      */       }
/*      */ 
/* 2012 */       scan();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseFile()
/*      */   {
/*      */     try
/*      */     {
/*      */       IdentifierToken localIdentifierToken;
/*      */       try
/*      */       {
/* 2024 */         if (this.token == 115)
/*      */         {
/* 2026 */           long l1 = scan();
/* 2027 */           localIdentifierToken = parseName(false);
/* 2028 */           expect(135);
/* 2029 */           this.actions.packageDeclaration(l1, localIdentifierToken);
/*      */         }
/*      */       } catch (SyntaxError localSyntaxError1) {
/* 2032 */         recoverFile();
/*      */       }
/* 2034 */       while (this.token == 110) {
/*      */         try
/*      */         {
/* 2037 */           long l2 = scan();
/* 2038 */           localIdentifierToken = parseName(true);
/* 2039 */           expect(135);
/* 2040 */           if (localIdentifierToken.id.getName().equals(idStar)) {
/* 2041 */             localIdentifierToken.id = localIdentifierToken.id.getQualifier();
/* 2042 */             this.actions.importPackage(l2, localIdentifierToken);
/*      */           } else {
/* 2044 */             this.actions.importClass(l2, localIdentifierToken);
/*      */           }
/*      */         } catch (SyntaxError localSyntaxError2) {
/* 2047 */           recoverFile();
/*      */         }
/*      */       }
/*      */ 
/* 2051 */       while (this.token != -1)
/*      */         try {
/* 2053 */           switch (this.token)
/*      */           {
/*      */           case 111:
/*      */           case 114:
/*      */           case 120:
/*      */           case 121:
/*      */           case 128:
/*      */           case 130:
/*      */           case 131:
/* 2062 */             parseClass();
/* 2063 */             break;
/*      */           case 135:
/* 2072 */             scan();
/* 2073 */             break;
/*      */           case -1:
/* 2077 */             return;
/*      */           default:
/* 2081 */             this.env.error(this.pos, "toplevel.expected");
/* 2082 */             throw new SyntaxError();
/*      */           }
/*      */         } catch (SyntaxError localSyntaxError3) {
/* 2085 */           recoverFile();
/*      */         }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 2089 */       this.env.error(this.pos, "io.exception", this.env.getSource());
/* 2090 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public long scan()
/*      */     throws IOException
/*      */   {
/* 2112 */     if ((this.scanner != this) && (this.scanner != null)) {
/* 2113 */       long l = this.scanner.scan();
/* 2114 */       this.token = this.scanner.token;
/* 2115 */       this.pos = this.scanner.pos;
/* 2116 */       return l;
/*      */     }
/* 2118 */     return super.scan();
/*      */   }
/*      */ 
/*      */   public void match(int paramInt1, int paramInt2) throws IOException {
/* 2122 */     if (this.scanner != this) {
/* 2123 */       this.scanner.match(paramInt1, paramInt2);
/* 2124 */       this.token = this.scanner.token;
/* 2125 */       this.pos = this.scanner.pos;
/* 2126 */       return;
/*      */     }
/* 2128 */     super.match(paramInt1, paramInt2);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Parser
 * JD-Core Version:    0.6.2
 */