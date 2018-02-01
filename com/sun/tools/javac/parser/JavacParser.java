/*      */ package com.sun.tools.javac.parser;
/*      */ 
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.tree.DocCommentTable;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssert;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBreak;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCase;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCatch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCContinue;
/*      */ import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCErroneous;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.TypeBoundKind;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.IntHashTable;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.SimpleDiagnosticPosition;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.ArrayList;
/*      */ 
/*      */ public class JavacParser
/*      */   implements Parser
/*      */ {
/*      */   private static final int infixPrecedenceLevels = 10;
/*      */   protected Lexer S;
/*      */   protected TreeMaker F;
/*      */   private Log log;
/*      */   private Source source;
/*      */   private Names names;
/*      */   private final AbstractEndPosTable endPosTable;
/*  115 */   private List<JCTree.JCAnnotation> typeAnnotationsPushedBack = List.nil();
/*      */ 
/*  123 */   private boolean permitTypeAnnotationsPushBack = false;
/*      */   boolean allowGenerics;
/*      */   boolean allowDiamond;
/*      */   boolean allowMulticatch;
/*      */   boolean allowVarargs;
/*      */   boolean allowAsserts;
/*      */   boolean allowEnums;
/*      */   boolean allowForeach;
/*      */   boolean allowStaticImport;
/*      */   boolean allowAnnotations;
/*      */   boolean allowTWR;
/*      */   boolean allowStringFolding;
/*      */   boolean allowLambda;
/*      */   boolean allowMethodReferences;
/*      */   boolean allowDefaultMethods;
/*      */   boolean allowStaticInterfaceMethods;
/*      */   boolean allowIntersectionTypesInCast;
/*      */   boolean keepDocComments;
/*      */   boolean keepLineMap;
/*      */   boolean allowTypeAnnotations;
/*      */   boolean allowAnnotationsAfterTypeParams;
/*      */   boolean allowThisIdent;
/*      */   JCTree.JCVariableDecl receiverParam;
/*      */   static final int EXPR = 1;
/*      */   static final int TYPE = 2;
/*      */   static final int NOPARAMS = 4;
/*      */   static final int TYPEARG = 8;
/*      */   static final int DIAMOND = 16;
/*  286 */   private int mode = 0;
/*      */ 
/*  290 */   private int lastmode = 0;
/*      */   protected Tokens.Token token;
/*      */   private JCTree.JCErroneous errorTree;
/*  439 */   private int errorPos = -1;
/*      */   private final DocCommentTable docComments;
/* 1018 */   ArrayList<JCTree.JCExpression[]> odStackSupply = new ArrayList();
/* 1019 */   ArrayList<Tokens.Token[]> opStackSupply = new ArrayList();
/*      */ 
/* 1694 */   Filter<Tokens.TokenKind> LAX_IDENTIFIER = new Filter() {
/*      */     public boolean accepts(Tokens.TokenKind paramAnonymousTokenKind) {
/* 1696 */       return (paramAnonymousTokenKind == Tokens.TokenKind.IDENTIFIER) || (paramAnonymousTokenKind == Tokens.TokenKind.UNDERSCORE) || (paramAnonymousTokenKind == Tokens.TokenKind.ASSERT) || (paramAnonymousTokenKind == Tokens.TokenKind.ENUM);
/*      */     }
/* 1694 */   };
/*      */ 
/*      */   protected JavacParser(ParserFactory paramParserFactory, Lexer paramLexer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/*  141 */     this.S = paramLexer;
/*  142 */     nextToken();
/*  143 */     this.F = paramParserFactory.F;
/*  144 */     this.log = paramParserFactory.log;
/*  145 */     this.names = paramParserFactory.names;
/*  146 */     this.source = paramParserFactory.source;
/*  147 */     this.allowGenerics = this.source.allowGenerics();
/*  148 */     this.allowVarargs = this.source.allowVarargs();
/*  149 */     this.allowAsserts = this.source.allowAsserts();
/*  150 */     this.allowEnums = this.source.allowEnums();
/*  151 */     this.allowForeach = this.source.allowForeach();
/*  152 */     this.allowStaticImport = this.source.allowStaticImport();
/*  153 */     this.allowAnnotations = this.source.allowAnnotations();
/*  154 */     this.allowTWR = this.source.allowTryWithResources();
/*  155 */     this.allowDiamond = this.source.allowDiamond();
/*  156 */     this.allowMulticatch = this.source.allowMulticatch();
/*  157 */     this.allowStringFolding = paramParserFactory.options.getBoolean("allowStringFolding", true);
/*  158 */     this.allowLambda = this.source.allowLambda();
/*  159 */     this.allowMethodReferences = this.source.allowMethodReferences();
/*  160 */     this.allowDefaultMethods = this.source.allowDefaultMethods();
/*  161 */     this.allowStaticInterfaceMethods = this.source.allowStaticInterfaceMethods();
/*  162 */     this.allowIntersectionTypesInCast = this.source.allowIntersectionTypesInCast();
/*  163 */     this.allowTypeAnnotations = this.source.allowTypeAnnotations();
/*  164 */     this.allowAnnotationsAfterTypeParams = this.source.allowAnnotationsAfterTypeParams();
/*  165 */     this.keepDocComments = paramBoolean1;
/*  166 */     this.docComments = newDocCommentTable(paramBoolean1, paramParserFactory);
/*  167 */     this.keepLineMap = paramBoolean2;
/*  168 */     this.errorTree = this.F.Erroneous();
/*  169 */     this.endPosTable = newEndPosTable(paramBoolean3);
/*      */   }
/*      */ 
/*      */   protected AbstractEndPosTable newEndPosTable(boolean paramBoolean) {
/*  173 */     return paramBoolean ? new SimpleEndPosTable(this) : new EmptyEndPosTable(this);
/*      */   }
/*      */ 
/*      */   protected DocCommentTable newDocCommentTable(boolean paramBoolean, ParserFactory paramParserFactory)
/*      */   {
/*  179 */     return paramBoolean ? new LazyDocCommentTable(paramParserFactory) : null;
/*      */   }
/*      */ 
/*      */   public Tokens.Token token()
/*      */   {
/*  297 */     return this.token;
/*      */   }
/*      */ 
/*      */   public void nextToken() {
/*  301 */     this.S.nextToken();
/*  302 */     this.token = this.S.token();
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter) {
/*  306 */     return peekToken(0, paramFilter);
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter) {
/*  310 */     return paramFilter.accepts(this.S.token(paramInt + 1).kind);
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2) {
/*  314 */     return peekToken(0, paramFilter1, paramFilter2);
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2)
/*      */   {
/*  319 */     return (paramFilter1.accepts(this.S.token(paramInt + 1).kind)) && 
/*  319 */       (paramFilter2
/*  319 */       .accepts(this.S
/*  319 */       .token(paramInt + 2).kind));
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2, Filter<Tokens.TokenKind> paramFilter3)
/*      */   {
/*  323 */     return peekToken(0, paramFilter1, paramFilter2, paramFilter3);
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2, Filter<Tokens.TokenKind> paramFilter3)
/*      */   {
/*  329 */     return (paramFilter1.accepts(this.S.token(paramInt + 1).kind)) && 
/*  328 */       (paramFilter2
/*  328 */       .accepts(this.S
/*  328 */       .token(paramInt + 2).kind)) && 
/*  329 */       (paramFilter3
/*  329 */       .accepts(this.S
/*  329 */       .token(paramInt + 3).kind));
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(Filter<Tokens.TokenKind>[] paramArrayOfFilter)
/*      */   {
/*  334 */     return peekToken(0, paramArrayOfFilter);
/*      */   }
/*      */ 
/*      */   protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind>[] paramArrayOfFilter)
/*      */   {
/*  339 */     for (; paramInt < paramArrayOfFilter.length; paramInt++) {
/*  340 */       if (!paramArrayOfFilter[paramInt].accepts(this.S.token(paramInt + 1).kind)) {
/*  341 */         return false;
/*      */       }
/*      */     }
/*  344 */     return true;
/*      */   }
/*      */ 
/*      */   private void skip(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*      */   {
/*      */     while (true)
/*      */     {
/*  355 */       switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) {
/*      */       case 1:
/*  357 */         nextToken();
/*  358 */         return;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*  367 */         return;
/*      */       case 10:
/*  369 */         if (paramBoolean1)
/*  370 */           return;
/*      */         break;
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*  392 */         if (paramBoolean2)
/*  393 */           return;
/*      */         break;
/*      */       case 31:
/*      */       case 32:
/*  397 */         if (paramBoolean3)
/*  398 */           return;
/*      */         break;
/*      */       case 33:
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
/*      */       case 46:
/*      */       case 47:
/*  415 */         if (paramBoolean4)
/*  416 */           return;
/*      */         break;
/*      */       }
/*  419 */       nextToken();
/*      */     }
/*      */   }
/*      */ 
/*      */   private JCTree.JCErroneous syntaxError(int paramInt, String paramString, Tokens.TokenKind[] paramArrayOfTokenKind) {
/*  424 */     return syntaxError(paramInt, List.nil(), paramString, paramArrayOfTokenKind);
/*      */   }
/*      */ 
/*      */   private JCTree.JCErroneous syntaxError(int paramInt, List<JCTree> paramList, String paramString, Tokens.TokenKind[] paramArrayOfTokenKind) {
/*  428 */     setErrorEndPos(paramInt);
/*  429 */     JCTree.JCErroneous localJCErroneous = this.F.at(paramInt).Erroneous(paramList);
/*  430 */     reportSyntaxError(localJCErroneous, paramString, (Object[])paramArrayOfTokenKind);
/*  431 */     if (paramList != null) {
/*  432 */       JCTree localJCTree = (JCTree)paramList.last();
/*  433 */       if (localJCTree != null)
/*  434 */         storeEnd(localJCTree, paramInt);
/*      */     }
/*  436 */     return (JCTree.JCErroneous)toP(localJCErroneous);
/*      */   }
/*      */ 
/*      */   private void reportSyntaxError(int paramInt, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  446 */     JCDiagnostic.SimpleDiagnosticPosition localSimpleDiagnosticPosition = new JCDiagnostic.SimpleDiagnosticPosition(paramInt);
/*  447 */     reportSyntaxError(localSimpleDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   private void reportSyntaxError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  455 */     int i = paramDiagnosticPosition.getPreferredPosition();
/*  456 */     if ((i > this.S.errPos()) || (i == -1)) {
/*  457 */       if (this.token.kind == Tokens.TokenKind.EOF)
/*  458 */         error(paramDiagnosticPosition, "premature.eof", new Object[0]);
/*      */       else {
/*  460 */         error(paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */       }
/*      */     }
/*  463 */     this.S.errPos(i);
/*  464 */     if (this.token.pos == this.errorPos)
/*  465 */       nextToken();
/*  466 */     this.errorPos = this.token.pos;
/*      */   }
/*      */ 
/*      */   private JCTree.JCErroneous syntaxError(String paramString)
/*      */   {
/*  474 */     return syntaxError(this.token.pos, paramString, new Tokens.TokenKind[0]);
/*      */   }
/*      */ 
/*      */   private JCTree.JCErroneous syntaxError(String paramString, Tokens.TokenKind paramTokenKind)
/*      */   {
/*  481 */     return syntaxError(this.token.pos, paramString, new Tokens.TokenKind[] { paramTokenKind });
/*      */   }
/*      */ 
/*      */   public void accept(Tokens.TokenKind paramTokenKind)
/*      */   {
/*  488 */     if (this.token.kind == paramTokenKind) {
/*  489 */       nextToken();
/*      */     } else {
/*  491 */       setErrorEndPos(this.token.pos);
/*  492 */       reportSyntaxError(this.S.prevToken().endPos, "expected", new Object[] { paramTokenKind });
/*      */     }
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression illegal(int paramInt)
/*      */   {
/*  499 */     setErrorEndPos(paramInt);
/*  500 */     if ((this.mode & 0x1) != 0) {
/*  501 */       return syntaxError(paramInt, "illegal.start.of.expr", new Tokens.TokenKind[0]);
/*      */     }
/*  503 */     return syntaxError(paramInt, "illegal.start.of.type", new Tokens.TokenKind[0]);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression illegal()
/*      */   {
/*  510 */     return illegal(this.token.pos);
/*      */   }
/*      */ 
/*      */   void checkNoMods(long paramLong)
/*      */   {
/*  515 */     if (paramLong != 0L) {
/*  516 */       long l = paramLong & -paramLong;
/*  517 */       error(this.token.pos, "mod.not.allowed.here", new Object[] { 
/*  518 */         Flags.asFlagSet(l) });
/*      */     }
/*      */   }
/*      */ 
/*      */   void attach(JCTree paramJCTree, Tokens.Comment paramComment)
/*      */   {
/*  536 */     if ((this.keepDocComments) && (paramComment != null))
/*      */     {
/*  538 */       this.docComments.putComment(paramJCTree, paramComment);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setErrorEndPos(int paramInt)
/*      */   {
/*  545 */     this.endPosTable.setErrorEndPos(paramInt);
/*      */   }
/*      */ 
/*      */   private void storeEnd(JCTree paramJCTree, int paramInt) {
/*  549 */     this.endPosTable.storeEnd(paramJCTree, paramInt);
/*      */   }
/*      */ 
/*      */   private <T extends JCTree> T to(T paramT) {
/*  553 */     return this.endPosTable.to(paramT);
/*      */   }
/*      */ 
/*      */   private <T extends JCTree> T toP(T paramT) {
/*  557 */     return this.endPosTable.toP(paramT);
/*      */   }
/*      */ 
/*      */   public int getStartPos(JCTree paramJCTree)
/*      */   {
/*  566 */     return TreeInfo.getStartPos(paramJCTree);
/*      */   }
/*      */ 
/*      */   public int getEndPos(JCTree paramJCTree)
/*      */   {
/*  578 */     return this.endPosTable.getEndPos(paramJCTree);
/*      */   }
/*      */ 
/*      */   Name ident()
/*      */   {
/*      */     Name localName;
/*  589 */     if (this.token.kind == Tokens.TokenKind.IDENTIFIER) {
/*  590 */       localName = this.token.name();
/*  591 */       nextToken();
/*  592 */       return localName;
/*  593 */     }if (this.token.kind == Tokens.TokenKind.ASSERT) {
/*  594 */       if (this.allowAsserts) {
/*  595 */         error(this.token.pos, "assert.as.identifier", new Object[0]);
/*  596 */         nextToken();
/*  597 */         return this.names.error;
/*      */       }
/*  599 */       warning(this.token.pos, "assert.as.identifier", new Object[0]);
/*  600 */       localName = this.token.name();
/*  601 */       nextToken();
/*  602 */       return localName;
/*      */     }
/*  604 */     if (this.token.kind == Tokens.TokenKind.ENUM) {
/*  605 */       if (this.allowEnums) {
/*  606 */         error(this.token.pos, "enum.as.identifier", new Object[0]);
/*  607 */         nextToken();
/*  608 */         return this.names.error;
/*      */       }
/*  610 */       warning(this.token.pos, "enum.as.identifier", new Object[0]);
/*  611 */       localName = this.token.name();
/*  612 */       nextToken();
/*  613 */       return localName;
/*      */     }
/*  615 */     if (this.token.kind == Tokens.TokenKind.THIS) {
/*  616 */       if (this.allowThisIdent)
/*      */       {
/*  618 */         checkTypeAnnotations();
/*  619 */         localName = this.token.name();
/*  620 */         nextToken();
/*  621 */         return localName;
/*      */       }
/*  623 */       error(this.token.pos, "this.as.identifier", new Object[0]);
/*  624 */       nextToken();
/*  625 */       return this.names.error;
/*      */     }
/*  627 */     if (this.token.kind == Tokens.TokenKind.UNDERSCORE) {
/*  628 */       warning(this.token.pos, "underscore.as.identifier", new Object[0]);
/*  629 */       localName = this.token.name();
/*  630 */       nextToken();
/*  631 */       return localName;
/*      */     }
/*  633 */     accept(Tokens.TokenKind.IDENTIFIER);
/*  634 */     return this.names.error;
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression qualident(boolean paramBoolean)
/*      */   {
/*  642 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*  643 */     while (this.token.kind == Tokens.TokenKind.DOT) {
/*  644 */       int i = this.token.pos;
/*  645 */       nextToken();
/*  646 */       List localList = null;
/*  647 */       if (paramBoolean) {
/*  648 */         localList = typeAnnotationsOpt();
/*      */       }
/*  650 */       localJCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(localJCExpression, ident()));
/*  651 */       if ((localList != null) && (localList.nonEmpty())) {
/*  652 */         localJCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList.head).pos).AnnotatedType(localList, localJCExpression));
/*      */       }
/*      */     }
/*  655 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression literal(Name paramName) {
/*  659 */     return literal(paramName, this.token.pos);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression literal(Name paramName, int paramInt)
/*      */   {
/*  675 */     Object localObject1 = this.errorTree;
/*      */     String str;
/*      */     Object localObject2;
/*  676 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) {
/*      */     case 48:
/*      */       try {
/*  679 */         localObject1 = this.F.at(paramInt).Literal(TypeTag.INT, 
/*  681 */           Integer.valueOf(Convert.string2int(strval(paramName), 
/*  681 */           this.token.radix())));
/*      */       } catch (NumberFormatException localNumberFormatException1) {
/*  683 */         error(this.token.pos, "int.number.too.large", new Object[] { strval(paramName) });
/*      */       }
/*      */     case 49:
/*      */       try
/*      */       {
/*  688 */         localObject1 = this.F.at(paramInt).Literal(TypeTag.LONG, new Long(
/*  690 */           Convert.string2long(strval(paramName), 
/*  690 */           this.token.radix())));
/*      */       } catch (NumberFormatException localNumberFormatException2) {
/*  692 */         error(this.token.pos, "int.number.too.large", new Object[] { strval(paramName) });
/*      */       }
/*      */ 
/*      */     case 50:
/*  698 */       str = this.token.radix() == 16 ? "0x" + this.token
/*  697 */         .stringVal() : this.token
/*  698 */         .stringVal();
/*      */       try
/*      */       {
/*  701 */         localObject2 = Float.valueOf(str);
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException3) {
/*  704 */         localObject2 = Float.valueOf((0.0F / 0.0F));
/*      */       }
/*  706 */       if ((((Float)localObject2).floatValue() == 0.0F) && (!isZero(str)))
/*  707 */         error(this.token.pos, "fp.number.too.small", new Object[0]);
/*  708 */       else if (((Float)localObject2).floatValue() == (1.0F / 1.0F))
/*  709 */         error(this.token.pos, "fp.number.too.large", new Object[0]);
/*      */       else
/*  711 */         localObject1 = this.F.at(paramInt).Literal(TypeTag.FLOAT, localObject2);
/*  712 */       break;
/*      */     case 51:
/*  717 */       str = this.token.radix() == 16 ? "0x" + this.token
/*  716 */         .stringVal() : this.token
/*  717 */         .stringVal();
/*      */       try
/*      */       {
/*  720 */         localObject2 = Double.valueOf(str);
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException4) {
/*  723 */         localObject2 = Double.valueOf((0.0D / 0.0D));
/*      */       }
/*  725 */       if ((((Double)localObject2).doubleValue() == 0.0D) && (!isZero(str)))
/*  726 */         error(this.token.pos, "fp.number.too.small", new Object[0]);
/*  727 */       else if (((Double)localObject2).doubleValue() == (1.0D / 0.0D))
/*  728 */         error(this.token.pos, "fp.number.too.large", new Object[0]);
/*      */       else
/*  730 */         localObject1 = this.F.at(paramInt).Literal(TypeTag.DOUBLE, localObject2);
/*  731 */       break;
/*      */     case 52:
/*  734 */       localObject1 = this.F.at(paramInt).Literal(TypeTag.CHAR, 
/*  736 */         Integer.valueOf(this.token
/*  736 */         .stringVal().charAt(0) + '\000'));
/*  737 */       break;
/*      */     case 53:
/*  739 */       localObject1 = this.F.at(paramInt).Literal(TypeTag.CLASS, this.token
/*  741 */         .stringVal());
/*  742 */       break;
/*      */     case 54:
/*      */     case 55:
/*  744 */       localObject1 = this.F.at(paramInt).Literal(TypeTag.BOOLEAN, 
/*  746 */         Integer.valueOf(this.token.kind == Tokens.TokenKind.TRUE ? 1 : 0));
/*      */ 
/*  747 */       break;
/*      */     case 56:
/*  749 */       localObject1 = this.F.at(paramInt).Literal(TypeTag.BOT, null);
/*      */ 
/*  752 */       break;
/*      */     default:
/*  754 */       Assert.error();
/*      */     }
/*  756 */     if (localObject1 == this.errorTree)
/*  757 */       localObject1 = this.F.at(paramInt).Erroneous();
/*  758 */     storeEnd((JCTree)localObject1, this.token.endPos);
/*  759 */     nextToken();
/*  760 */     return localObject1;
/*      */   }
/*      */ 
/*      */   boolean isZero(String paramString) {
/*  764 */     char[] arrayOfChar = paramString.toCharArray();
/*  765 */     int i = (arrayOfChar.length > 1) && (Character.toLowerCase(arrayOfChar[1]) == 'x') ? 16 : 10;
/*  766 */     int j = i == 16 ? 2 : 0;
/*  767 */     while ((j < arrayOfChar.length) && ((arrayOfChar[j] == '0') || (arrayOfChar[j] == '.'))) j++;
/*  768 */     return (j >= arrayOfChar.length) || (Character.digit(arrayOfChar[j], i) <= 0);
/*      */   }
/*      */ 
/*      */   String strval(Name paramName) {
/*  772 */     String str = this.token.stringVal();
/*  773 */     return paramName + str;
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression parseExpression()
/*      */   {
/*  779 */     return term(1);
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression parseType()
/*      */   {
/*  795 */     List localList = typeAnnotationsOpt();
/*  796 */     return parseType(localList);
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression parseType(List<JCTree.JCAnnotation> paramList) {
/*  800 */     JCTree.JCExpression localJCExpression = unannotatedType();
/*      */ 
/*  802 */     if (paramList.nonEmpty()) {
/*  803 */       localJCExpression = insertAnnotationsToMostInner(localJCExpression, paramList, false);
/*      */     }
/*      */ 
/*  806 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression unannotatedType() {
/*  810 */     return term(2);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term(int paramInt) {
/*  814 */     int i = this.mode;
/*  815 */     this.mode = paramInt;
/*  816 */     JCTree.JCExpression localJCExpression = term();
/*  817 */     this.lastmode = this.mode;
/*  818 */     this.mode = i;
/*  819 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term()
/*      */   {
/*  836 */     JCTree.JCExpression localJCExpression = term1();
/*  837 */     if ((((this.mode & 0x1) != 0) && (this.token.kind == Tokens.TokenKind.EQ)) || (
/*  838 */       (Tokens.TokenKind.PLUSEQ
/*  838 */       .compareTo(this.token.kind) <= 0) && 
/*  838 */       (this.token.kind.compareTo(Tokens.TokenKind.GTGTGTEQ) <= 0))) {
/*  839 */       return termRest(localJCExpression);
/*      */     }
/*  841 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression termRest(JCTree.JCExpression paramJCExpression)
/*      */   {
/*      */     int i;
/*      */     Object localObject;
/*  845 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) {
/*      */     case 57:
/*  847 */       i = this.token.pos;
/*  848 */       nextToken();
/*  849 */       this.mode = 1;
/*  850 */       localObject = term();
/*  851 */       return (JCTree.JCExpression)toP(this.F.at(i).Assign(paramJCExpression, (JCTree.JCExpression)localObject));
/*      */     case 58:
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
/*  864 */       i = this.token.pos;
/*  865 */       localObject = this.token.kind;
/*  866 */       nextToken();
/*  867 */       this.mode = 1;
/*  868 */       JCTree.JCExpression localJCExpression = term();
/*  869 */       return this.F.at(i).Assignop(optag((Tokens.TokenKind)localObject), paramJCExpression, localJCExpression);
/*      */     }
/*  871 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term1()
/*      */   {
/*  880 */     JCTree.JCExpression localJCExpression = term2();
/*  881 */     if (((this.mode & 0x1) != 0) && (this.token.kind == Tokens.TokenKind.QUES)) {
/*  882 */       this.mode = 1;
/*  883 */       return term1Rest(localJCExpression);
/*      */     }
/*  885 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term1Rest(JCTree.JCExpression paramJCExpression)
/*      */   {
/*  892 */     if (this.token.kind == Tokens.TokenKind.QUES) {
/*  893 */       int i = this.token.pos;
/*  894 */       nextToken();
/*  895 */       JCTree.JCExpression localJCExpression1 = term();
/*  896 */       accept(Tokens.TokenKind.COLON);
/*  897 */       JCTree.JCExpression localJCExpression2 = term1();
/*  898 */       return this.F.at(i).Conditional(paramJCExpression, localJCExpression1, localJCExpression2);
/*      */     }
/*  900 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term2()
/*      */   {
/*  909 */     JCTree.JCExpression localJCExpression = term3();
/*  910 */     if (((this.mode & 0x1) != 0) && (prec(this.token.kind) >= 4)) {
/*  911 */       this.mode = 1;
/*  912 */       return term2Rest(localJCExpression, 4);
/*      */     }
/*  914 */     return localJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term2Rest(JCTree.JCExpression paramJCExpression, int paramInt)
/*      */   {
/*  932 */     JCTree.JCExpression[] arrayOfJCExpression = newOdStack();
/*  933 */     Tokens.Token[] arrayOfToken = newOpStack();
/*      */ 
/*  936 */     int i = 0;
/*  937 */     arrayOfJCExpression[0] = paramJCExpression;
/*  938 */     int j = this.token.pos;
/*  939 */     Tokens.Token localToken = Tokens.DUMMY;
/*      */ 
/*  950 */     for (; prec(this.token.kind) >= paramInt; 
/*  950 */       goto 92)
/*      */     {
/*  941 */       arrayOfToken[i] = localToken;
/*  942 */       i++;
/*  943 */       localToken = this.token;
/*  944 */       nextToken();
/*  945 */       arrayOfJCExpression[i] = (localToken.kind == Tokens.TokenKind.INSTANCEOF ? parseType() : term3());
/*  946 */       if ((i > 0) && (prec(localToken.kind) >= prec(this.token.kind))) {
/*  947 */         arrayOfJCExpression[(i - 1)] = makeOp(localToken.pos, localToken.kind, arrayOfJCExpression[(i - 1)], arrayOfJCExpression[i]);
/*      */ 
/*  949 */         i--;
/*  950 */         localToken = arrayOfToken[i];
/*      */       }
/*      */     }
/*  953 */     Assert.check(i == 0);
/*  954 */     paramJCExpression = arrayOfJCExpression[0];
/*      */ 
/*  956 */     if (paramJCExpression.hasTag(JCTree.Tag.PLUS)) {
/*  957 */       StringBuilder localStringBuilder = foldStrings(paramJCExpression);
/*  958 */       if (localStringBuilder != null) {
/*  959 */         paramJCExpression = (JCTree.JCExpression)toP(this.F.at(j).Literal(TypeTag.CLASS, localStringBuilder.toString()));
/*      */       }
/*      */     }
/*      */ 
/*  963 */     this.odStackSupply.add(arrayOfJCExpression);
/*  964 */     this.opStackSupply.add(arrayOfToken);
/*  965 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression makeOp(int paramInt, Tokens.TokenKind paramTokenKind, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */   {
/*  975 */     if (paramTokenKind == Tokens.TokenKind.INSTANCEOF) {
/*  976 */       return this.F.at(paramInt).TypeTest(paramJCExpression1, paramJCExpression2);
/*      */     }
/*  978 */     return this.F.at(paramInt).Binary(optag(paramTokenKind), paramJCExpression1, paramJCExpression2);
/*      */   }
/*      */ 
/*      */   protected StringBuilder foldStrings(JCTree paramJCTree)
/*      */   {
/*  985 */     if (!this.allowStringFolding)
/*  986 */       return null;
/*  987 */     List localList = List.nil();
/*      */     while (true) {
/*  989 */       if (paramJCTree.hasTag(JCTree.Tag.LITERAL)) {
/*  990 */         localObject1 = (JCTree.JCLiteral)paramJCTree;
/*  991 */         if (((JCTree.JCLiteral)localObject1).typetag == TypeTag.CLASS) {
/*  992 */           localObject2 = new StringBuilder((String)((JCTree.JCLiteral)localObject1).value);
/*      */ 
/*  994 */           while (localList.nonEmpty()) {
/*  995 */             ((StringBuilder)localObject2).append((String)localList.head);
/*  996 */             localList = localList.tail;
/*      */           }
/*  998 */           return localObject2;
/*      */         }
/* 1000 */         break; } if (!paramJCTree.hasTag(JCTree.Tag.PLUS)) break;
/* 1001 */       Object localObject1 = (JCTree.JCBinary)paramJCTree;
/* 1002 */       if (!((JCTree.JCBinary)localObject1).rhs.hasTag(JCTree.Tag.LITERAL)) break;
/* 1003 */       Object localObject2 = (JCTree.JCLiteral)((JCTree.JCBinary)localObject1).rhs;
/* 1004 */       if (((JCTree.JCLiteral)localObject2).typetag != TypeTag.CLASS) break;
/* 1005 */       localList = localList.prepend((String)((JCTree.JCLiteral)localObject2).value);
/* 1006 */       paramJCTree = ((JCTree.JCBinary)localObject1).lhs;
/*      */     }
/*      */ 
/* 1011 */     return null;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression[] newOdStack()
/*      */   {
/* 1022 */     if (this.odStackSupply.isEmpty())
/* 1023 */       return new JCTree.JCExpression[11];
/* 1024 */     return (JCTree.JCExpression[])this.odStackSupply.remove(this.odStackSupply.size() - 1);
/*      */   }
/*      */ 
/*      */   private Tokens.Token[] newOpStack() {
/* 1028 */     if (this.opStackSupply.isEmpty())
/* 1029 */       return new Tokens.Token[11];
/* 1030 */     return (Tokens.Token[])this.opStackSupply.remove(this.opStackSupply.size() - 1);
/*      */   }
/*      */ 
/*      */   protected JCTree.JCExpression term3()
/*      */   {
/* 1069 */     int i = this.token.pos;
/*      */ 
/* 1071 */     List localList1 = typeArgumentsOpt(1);
/*      */     Object localObject2;
/*      */     Object localObject1;
/*      */     label517: Object localObject3;
/*      */     JCTree.JCExpression localJCExpression2;
/* 1072 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) {
/*      */     case 76:
/* 1074 */       if (((this.mode & 0x2) != 0) && ((this.mode & 0xC) == 8)) {
/* 1075 */         this.mode = 2;
/* 1076 */         return typeArgument();
/*      */       }
/* 1078 */       return illegal();
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/* 1080 */       if ((localList1 == null) && ((this.mode & 0x1) != 0)) {
/* 1081 */         localObject2 = this.token.kind;
/* 1082 */         nextToken();
/* 1083 */         this.mode = 1;
/* 1084 */         if ((localObject2 == Tokens.TokenKind.SUB) && ((this.token.kind == Tokens.TokenKind.INTLITERAL) || (this.token.kind == Tokens.TokenKind.LONGLITERAL)))
/*      */         {
/* 1086 */           if (this.token
/* 1086 */             .radix() == 10) {
/* 1087 */             this.mode = 1;
/* 1088 */             localObject1 = literal(this.names.hyphen, i); break label517;
/*      */           }
/*      */         }
/* 1090 */         localObject1 = term3();
/* 1091 */         return this.F.at(i).Unary(unoptag((Tokens.TokenKind)localObject2), (JCTree.JCExpression)localObject1);
/*      */       } else {
/* 1093 */         return illegal();
/*      */       }break;
/*      */     case 73:
/* 1096 */       if ((localList1 == null) && ((this.mode & 0x1) != 0)) {
/* 1097 */         localObject2 = analyzeParens();
/* 1098 */         switch (2.$SwitchMap$com$sun$tools$javac$parser$JavacParser$ParensResult[localObject2.ordinal()]) {
/*      */         case 1:
/* 1100 */           accept(Tokens.TokenKind.LPAREN);
/* 1101 */           this.mode = 2;
/* 1102 */           int j = i;
/* 1103 */           localObject3 = List.of(localObject1 = term3());
/* 1104 */           while (this.token.kind == Tokens.TokenKind.AMP) {
/* 1105 */             checkIntersectionTypesInCast();
/* 1106 */             accept(Tokens.TokenKind.AMP);
/* 1107 */             localObject3 = ((List)localObject3).prepend(term3());
/*      */           }
/* 1109 */           if (((List)localObject3).length() > 1) {
/* 1110 */             localObject1 = (JCTree.JCExpression)toP(this.F.at(j).TypeIntersection(((List)localObject3).reverse()));
/*      */           }
/* 1112 */           accept(Tokens.TokenKind.RPAREN);
/* 1113 */           this.mode = 1;
/* 1114 */           localJCExpression2 = term3();
/* 1115 */           return this.F.at(i).TypeCast((JCTree)localObject1, localJCExpression2);
/*      */         case 2:
/*      */         case 3:
/* 1118 */           localObject1 = lambdaExpressionOrStatement(true, localObject2 == ParensResult.EXPLICIT_LAMBDA, i);
/* 1119 */           break;
/*      */         default:
/* 1121 */           accept(Tokens.TokenKind.LPAREN);
/* 1122 */           this.mode = 1;
/* 1123 */           localObject1 = termRest(term1Rest(term2Rest(term3(), 4)));
/* 1124 */           accept(Tokens.TokenKind.RPAREN);
/* 1125 */           localObject1 = (JCTree.JCExpression)toP(this.F.at(i).Parens((JCTree.JCExpression)localObject1));
/*      */         }
/*      */       }
/*      */       else {
/* 1129 */         return illegal();
/*      */       }
/*      */       break;
/*      */     case 69:
/* 1133 */       if ((this.mode & 0x1) != 0) {
/* 1134 */         this.mode = 1;
/* 1135 */         localObject1 = (JCTree.JCExpression)to(this.F.at(i).Ident(this.names._this));
/* 1136 */         nextToken();
/* 1137 */         if (localList1 == null)
/* 1138 */           localObject1 = argumentsOpt(null, (JCTree.JCExpression)localObject1);
/*      */         else
/* 1140 */           localObject1 = arguments(localList1, (JCTree.JCExpression)localObject1);
/* 1141 */         localList1 = null; } else {
/* 1142 */         return illegal();
/*      */       }break;
/*      */     case 70:
/* 1145 */       if ((this.mode & 0x1) != 0) {
/* 1146 */         this.mode = 1;
/* 1147 */         localObject1 = (JCTree.JCExpression)to(this.F.at(i).Ident(this.names._super));
/* 1148 */         localObject1 = superSuffix(localList1, (JCTree.JCExpression)localObject1);
/* 1149 */         localList1 = null; } else {
/* 1150 */         return illegal(); } break;
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/* 1155 */       if ((localList1 == null) && ((this.mode & 0x1) != 0)) {
/* 1156 */         this.mode = 1;
/* 1157 */         localObject1 = literal(this.names.empty); } else {
/* 1158 */         return illegal();
/*      */       }break;
/*      */     case 71:
/* 1161 */       if (localList1 != null) return illegal();
/* 1162 */       if ((this.mode & 0x1) != 0) {
/* 1163 */         this.mode = 1;
/* 1164 */         nextToken();
/* 1165 */         if (this.token.kind == Tokens.TokenKind.LT) localList1 = typeArguments(false);
/* 1166 */         localObject1 = creator(i, localList1);
/* 1167 */         localList1 = null; } else {
/* 1168 */         return illegal();
/*      */       }
/*      */       break;
/*      */     case 5:
/* 1172 */       localObject2 = typeAnnotationsOpt();
/* 1173 */       if (((List)localObject2).isEmpty())
/*      */       {
/* 1175 */         throw new AssertionError("Expected type annotations, but found none!");
/*      */       }
/*      */ 
/* 1178 */       JCTree.JCExpression localJCExpression1 = term3();
/*      */ 
/* 1180 */       if ((this.mode & 0x2) == 0)
/*      */       {
/* 1182 */         switch (localJCExpression1.getTag()) {
/*      */         case REFERENCE:
/* 1184 */           localObject3 = (JCTree.JCMemberReference)localJCExpression1;
/* 1185 */           ((JCTree.JCMemberReference)localObject3).expr = ((JCTree.JCExpression)toP(this.F.at(i).AnnotatedType((List)localObject2, ((JCTree.JCMemberReference)localObject3).expr)));
/* 1186 */           localObject1 = localObject3;
/* 1187 */           break;
/*      */         case SELECT:
/* 1190 */           localObject3 = (JCTree.JCFieldAccess)localJCExpression1;
/*      */ 
/* 1192 */           if (((JCTree.JCFieldAccess)localObject3).name != this.names._class) {
/* 1193 */             return illegal();
/*      */           }
/* 1195 */           this.log.error(this.token.pos, "no.annotations.on.dot.class", new Object[0]);
/* 1196 */           return localJCExpression1;
/*      */         default:
/* 1200 */           return illegal(((JCTree.JCAnnotation)((List)localObject2).head).pos);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1205 */         localObject1 = insertAnnotationsToMostInner(localJCExpression1, (List)localObject2, false);
/*      */       }
/* 1207 */       break;
/*      */     case 9:
/*      */     case 31:
/*      */     case 32:
/*      */     case 83:
/* 1209 */       if (localList1 != null) return illegal();
/* 1210 */       if (((this.mode & 0x1) != 0) && (peekToken(Tokens.TokenKind.ARROW))) {
/* 1211 */         localObject1 = lambdaExpressionOrStatement(false, false, i);
/*      */       } else {
/* 1213 */         localObject1 = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*      */         while (true) {
/* 1215 */           i = this.token.pos;
/* 1216 */           localObject3 = typeAnnotationsOpt();
/*      */ 
/* 1220 */           if ((!((List)localObject3).isEmpty()) && (this.token.kind != Tokens.TokenKind.LBRACKET) && (this.token.kind != Tokens.TokenKind.ELLIPSIS)) {
/* 1221 */             return illegal(((JCTree.JCAnnotation)((List)localObject3).head).pos);
/*      */           }
/* 1223 */           switch (this.token.kind) {
/*      */           case LBRACKET:
/* 1225 */             nextToken();
/* 1226 */             if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 1227 */               nextToken();
/* 1228 */               localObject1 = bracketsOpt((JCTree.JCExpression)localObject1);
/* 1229 */               localObject1 = (JCTree.JCExpression)toP(this.F.at(i).TypeArray((JCTree.JCExpression)localObject1));
/* 1230 */               if (((List)localObject3).nonEmpty()) {
/* 1231 */                 localObject1 = (JCTree.JCExpression)toP(this.F.at(i).AnnotatedType((List)localObject3, (JCTree.JCExpression)localObject1));
/*      */               }
/*      */ 
/* 1234 */               localJCExpression2 = bracketsSuffix((JCTree.JCExpression)localObject1);
/* 1235 */               if ((localJCExpression2 != localObject1) && ((((List)localObject3).nonEmpty()) || (TreeInfo.containsTypeAnnotation((JCTree)localObject1))))
/*      */               {
/* 1239 */                 syntaxError("no.annotations.on.dot.class");
/*      */               }
/* 1241 */               localObject1 = localJCExpression2;
/*      */             } else {
/* 1243 */               if ((this.mode & 0x1) != 0) {
/* 1244 */                 this.mode = 1;
/* 1245 */                 localJCExpression2 = term();
/* 1246 */                 if (!((List)localObject3).isEmpty()) localObject1 = illegal(((JCTree.JCAnnotation)((List)localObject3).head).pos);
/* 1247 */                 localObject1 = (JCTree.JCExpression)to(this.F.at(i).Indexed((JCTree.JCExpression)localObject1, localJCExpression2));
/*      */               }
/* 1249 */               accept(Tokens.TokenKind.RBRACKET);
/*      */             }
/* 1251 */             break;
/*      */           case LPAREN:
/* 1253 */             if ((this.mode & 0x1) == 0) break label2343;
/* 1254 */             this.mode = 1;
/* 1255 */             localObject1 = arguments(localList1, (JCTree.JCExpression)localObject1);
/* 1256 */             if (!((List)localObject3).isEmpty()) localObject1 = illegal(((JCTree.JCAnnotation)((List)localObject3).head).pos);
/* 1257 */             localList1 = null; break;
/*      */           case DOT:
/* 1261 */             nextToken();
/* 1262 */             int k = this.mode;
/* 1263 */             this.mode &= -5;
/* 1264 */             localList1 = typeArgumentsOpt(1);
/* 1265 */             this.mode = k;
/* 1266 */             if ((this.mode & 0x1) != 0) {
/* 1267 */               switch (this.token.kind) {
/*      */               case CLASS:
/* 1269 */                 if (localList1 != null) return illegal();
/* 1270 */                 this.mode = 1;
/* 1271 */                 localObject1 = (JCTree.JCExpression)to(this.F.at(i).Select((JCTree.JCExpression)localObject1, this.names._class));
/* 1272 */                 nextToken();
/* 1273 */                 break;
/*      */               case THIS:
/* 1275 */                 if (localList1 != null) return illegal();
/* 1276 */                 this.mode = 1;
/* 1277 */                 localObject1 = (JCTree.JCExpression)to(this.F.at(i).Select((JCTree.JCExpression)localObject1, this.names._this));
/* 1278 */                 nextToken();
/* 1279 */                 break;
/*      */               case SUPER:
/* 1281 */                 this.mode = 1;
/* 1282 */                 localObject1 = (JCTree.JCExpression)to(this.F.at(i).Select((JCTree.JCExpression)localObject1, this.names._super));
/* 1283 */                 localObject1 = superSuffix(localList1, (JCTree.JCExpression)localObject1);
/* 1284 */                 localList1 = null;
/* 1285 */                 break;
/*      */               case NEW:
/* 1287 */                 if (localList1 != null) return illegal();
/* 1288 */                 this.mode = 1;
/* 1289 */                 int m = this.token.pos;
/* 1290 */                 nextToken();
/* 1291 */                 if (this.token.kind == Tokens.TokenKind.LT) localList1 = typeArguments(false);
/* 1292 */                 localObject1 = innerCreator(m, localList1, (JCTree.JCExpression)localObject1);
/* 1293 */                 localList1 = null;
/* 1294 */                 break;
/*      */               }
/*      */             }
/*      */ 
/* 1298 */             List localList2 = null;
/* 1299 */             if (((this.mode & 0x2) != 0) && (this.token.kind == Tokens.TokenKind.MONKEYS_AT)) {
/* 1300 */               localList2 = typeAnnotationsOpt();
/*      */             }
/*      */ 
/* 1303 */             localObject1 = (JCTree.JCExpression)toP(this.F.at(i).Select((JCTree.JCExpression)localObject1, ident()));
/* 1304 */             if ((localList2 != null) && (localList2.nonEmpty()))
/* 1305 */               localObject1 = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList2.head).pos).AnnotatedType(localList2, (JCTree.JCExpression)localObject1)); break;
/*      */           case ELLIPSIS:
/* 1309 */             if (this.permitTypeAnnotationsPushBack)
/* 1310 */               this.typeAnnotationsPushedBack = ((List)localObject3);
/* 1311 */             else if (((List)localObject3).nonEmpty())
/*      */             {
/* 1313 */               illegal(((JCTree.JCAnnotation)((List)localObject3).head).pos); } break;
/*      */           case LT:
/* 1317 */             if (((this.mode & 0x2) != 0) || (!isUnboundMemberRef())) {
/*      */               break label2343;
/*      */             }
/* 1320 */             int n = this.token.pos;
/* 1321 */             accept(Tokens.TokenKind.LT);
/* 1322 */             ListBuffer localListBuffer = new ListBuffer();
/* 1323 */             localListBuffer.append(typeArgument());
/* 1324 */             while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 1325 */               nextToken();
/* 1326 */               localListBuffer.append(typeArgument());
/*      */             }
/* 1328 */             accept(Tokens.TokenKind.GT);
/* 1329 */             localObject1 = (JCTree.JCExpression)toP(this.F.at(n).TypeApply((JCTree.JCExpression)localObject1, localListBuffer.toList()));
/* 1330 */             checkGenerics();
/* 1331 */             while (this.token.kind == Tokens.TokenKind.DOT) {
/* 1332 */               nextToken();
/* 1333 */               this.mode = 2;
/* 1334 */               localObject1 = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Select((JCTree.JCExpression)localObject1, ident()));
/* 1335 */               localObject1 = typeArgumentsOpt((JCTree.JCExpression)localObject1);
/*      */             }
/* 1337 */             localObject1 = bracketsOpt((JCTree.JCExpression)localObject1);
/* 1338 */             if (this.token.kind != Tokens.TokenKind.COLCOL)
/*      */             {
/* 1340 */               localObject1 = illegal();
/*      */             }
/* 1342 */             this.mode = 1;
/* 1343 */             return term3Rest((JCTree.JCExpression)localObject1, localList1);
/*      */           default:
/* 1347 */             break label2343;
/*      */           }
/*      */         }
/*      */       }
/* 1351 */       if (localList1 != null) illegal();
/* 1352 */       localObject1 = typeArgumentsOpt((JCTree.JCExpression)localObject1);
/* 1353 */       break;
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/* 1356 */       if (localList1 != null) illegal();
/* 1357 */       localObject1 = bracketsSuffix(bracketsOpt(basicType()));
/* 1358 */       break;
/*      */     case 30:
/* 1360 */       if (localList1 != null) illegal();
/* 1361 */       if ((this.mode & 0x1) != 0) {
/* 1362 */         nextToken();
/* 1363 */         if (this.token.kind == Tokens.TokenKind.DOT) {
/* 1364 */           localObject3 = (JCTree.JCPrimitiveTypeTree)toP(this.F.at(i).TypeIdent(TypeTag.VOID));
/* 1365 */           localObject1 = bracketsSuffix((JCTree.JCExpression)localObject3);
/*      */         } else {
/* 1367 */           return illegal(i);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1373 */         localObject3 = (JCTree.JCPrimitiveTypeTree)to(this.F.at(i).TypeIdent(TypeTag.VOID));
/* 1374 */         nextToken();
/* 1375 */         return localObject3; } break;
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 57:
/*      */     case 58:
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
/*      */     case 72:
/*      */     case 74:
/*      */     case 75:
/*      */     default:
/* 1380 */       label2343: return illegal();
/*      */     }
/* 1382 */     return term3Rest((JCTree.JCExpression)localObject1, localList1);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression term3Rest(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) {
/* 1386 */     if (paramList != null) illegal(); while (true)
/*      */     {
/* 1388 */       int i = this.token.pos;
/* 1389 */       List localList1 = typeAnnotationsOpt();
/*      */ 
/* 1391 */       if (this.token.kind == Tokens.TokenKind.LBRACKET) {
/* 1392 */         nextToken();
/* 1393 */         if ((this.mode & 0x2) != 0) {
/* 1394 */           int j = this.mode;
/* 1395 */           this.mode = 2;
/* 1396 */           if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 1397 */             nextToken();
/* 1398 */             paramJCExpression = bracketsOpt(paramJCExpression);
/* 1399 */             paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).TypeArray(paramJCExpression));
/* 1400 */             if (this.token.kind == Tokens.TokenKind.COLCOL) {
/* 1401 */               this.mode = 1;
/*      */             }
/*      */             else {
/* 1404 */               if (localList1.nonEmpty()) {
/* 1405 */                 paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).AnnotatedType(localList1, paramJCExpression));
/*      */               }
/* 1407 */               return paramJCExpression;
/*      */             }
/*      */           } else { this.mode = j; }
/*      */         } else {
/* 1411 */           if ((this.mode & 0x1) != 0) {
/* 1412 */             this.mode = 1;
/* 1413 */             JCTree.JCExpression localJCExpression = term();
/* 1414 */             paramJCExpression = (JCTree.JCExpression)to(this.F.at(i).Indexed(paramJCExpression, localJCExpression));
/*      */           }
/* 1416 */           accept(Tokens.TokenKind.RBRACKET);
/*      */         } } else if (this.token.kind == Tokens.TokenKind.DOT) {
/* 1418 */         nextToken();
/* 1419 */         paramList = typeArgumentsOpt(1);
/* 1420 */         if ((this.token.kind == Tokens.TokenKind.SUPER) && ((this.mode & 0x1) != 0)) {
/* 1421 */           this.mode = 1;
/* 1422 */           paramJCExpression = (JCTree.JCExpression)to(this.F.at(i).Select(paramJCExpression, this.names._super));
/* 1423 */           nextToken();
/* 1424 */           paramJCExpression = arguments(paramList, paramJCExpression);
/* 1425 */           paramList = null;
/* 1426 */         } else if ((this.token.kind == Tokens.TokenKind.NEW) && ((this.mode & 0x1) != 0)) {
/* 1427 */           if (paramList != null) return illegal();
/* 1428 */           this.mode = 1;
/* 1429 */           int k = this.token.pos;
/* 1430 */           nextToken();
/* 1431 */           if (this.token.kind == Tokens.TokenKind.LT) paramList = typeArguments(false);
/* 1432 */           paramJCExpression = innerCreator(k, paramList, paramJCExpression);
/* 1433 */           paramList = null;
/*      */         } else {
/* 1435 */           List localList2 = null;
/* 1436 */           if (((this.mode & 0x2) != 0) && (this.token.kind == Tokens.TokenKind.MONKEYS_AT))
/*      */           {
/* 1438 */             localList2 = typeAnnotationsOpt();
/*      */           }
/* 1440 */           paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(paramJCExpression, ident()));
/* 1441 */           if ((localList2 != null) && (localList2.nonEmpty())) {
/* 1442 */             paramJCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList2.head).pos).AnnotatedType(localList2, paramJCExpression));
/*      */           }
/* 1444 */           paramJCExpression = argumentsOpt(paramList, typeArgumentsOpt(paramJCExpression));
/* 1445 */           paramList = null;
/*      */         }
/* 1447 */       } else if (((this.mode & 0x1) != 0) && (this.token.kind == Tokens.TokenKind.COLCOL)) {
/* 1448 */         this.mode = 1;
/* 1449 */         if (paramList != null) return illegal();
/* 1450 */         accept(Tokens.TokenKind.COLCOL);
/* 1451 */         paramJCExpression = memberReferenceSuffix(i, paramJCExpression);
/*      */       } else {
/* 1453 */         if (localList1.isEmpty()) break;
/* 1454 */         if (this.permitTypeAnnotationsPushBack) {
/* 1455 */           this.typeAnnotationsPushedBack = localList1; break;
/*      */         }
/* 1457 */         return illegal(((JCTree.JCAnnotation)localList1.head).pos);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1462 */     while (((this.token.kind == Tokens.TokenKind.PLUSPLUS) || (this.token.kind == Tokens.TokenKind.SUBSUB)) && ((this.mode & 0x1) != 0)) {
/* 1463 */       this.mode = 1;
/* 1464 */       paramJCExpression = (JCTree.JCExpression)to(this.F.at(this.token.pos).Unary(this.token.kind == Tokens.TokenKind.PLUSPLUS ? JCTree.Tag.POSTINC : JCTree.Tag.POSTDEC, paramJCExpression));
/*      */ 
/* 1466 */       nextToken();
/*      */     }
/* 1468 */     return (JCTree.JCExpression)toP(paramJCExpression);
/*      */   }
/*      */ 
/*      */   boolean isUnboundMemberRef()
/*      */   {
/* 1478 */     int i = 0; int j = 0;
/* 1479 */     label541: for (Tokens.Token localToken = this.S.token(i); ; localToken = this.S.token(++i))
/*      */     {
/*      */       Tokens.TokenKind localTokenKind;
/* 1480 */       switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[localToken.kind.ordinal()]) { case 5:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 31:
/*      */       case 32:
/*      */       case 70:
/*      */       case 72:
/*      */       case 74:
/*      */       case 76:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/* 1486 */         break;
/*      */       case 73:
/* 1490 */         int k = 0;
/* 1491 */         for (; ; i++) {
/* 1492 */           localTokenKind = this.S.token(i).kind;
/* 1493 */           switch (localTokenKind) {
/*      */           case EOF:
/* 1495 */             return false;
/*      */           case LPAREN:
/* 1497 */             k++;
/* 1498 */             break;
/*      */           case RPAREN:
/* 1500 */             k--;
/* 1501 */             if (k == 0)
/*      */             {
/*      */               break label541;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       case 21:
/* 1509 */         j++; break;
/*      */       case 88:
/* 1511 */         j--;
/*      */       case 89:
/* 1513 */         j--;
/*      */       case 90:
/* 1515 */         j--;
/* 1516 */         if (j == 0) {
/* 1517 */           localTokenKind = this.S.token(i + 1).kind;
/* 1518 */           return (localTokenKind == Tokens.TokenKind.DOT) || (localTokenKind == Tokens.TokenKind.LBRACKET) || (localTokenKind == Tokens.TokenKind.COLCOL); } break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 30:
/*      */       case 33:
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
/*      */       case 71:
/*      */       case 75:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       default:
/* 1525 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   ParensResult analyzeParens()
/*      */   {
/* 1537 */     int i = 0;
/* 1538 */     int j = 0;
/* 1539 */     label1191: for (int k = 0; ; k++) {
/* 1540 */       Tokens.TokenKind localTokenKind1 = this.S.token(k).kind;
/* 1541 */       switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[localTokenKind1.ordinal()]) {
/*      */       case 87:
/* 1543 */         j = 1;
/*      */       case 70:
/*      */       case 74:
/*      */       case 85:
/*      */       case 91:
/* 1546 */         break;
/*      */       case 76:
/* 1548 */         if ((peekToken(k, Tokens.TokenKind.EXTENDS)) || 
/* 1549 */           (peekToken(k, Tokens.TokenKind.SUPER)))
/*      */         {
/* 1551 */           j = 1; } break;
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/* 1556 */         if (peekToken(k, Tokens.TokenKind.RPAREN))
/*      */         {
/* 1558 */           return ParensResult.CAST;
/* 1559 */         }if (peekToken(k, this.LAX_IDENTIFIER))
/*      */         {
/* 1561 */           return ParensResult.EXPLICIT_LAMBDA;
/*      */         }
/*      */         break;
/*      */       case 73:
/* 1565 */         if (k != 0)
/*      */         {
/* 1567 */           return ParensResult.PARENS;
/* 1568 */         }if (peekToken(k, Tokens.TokenKind.RPAREN))
/*      */         {
/* 1570 */           return ParensResult.EXPLICIT_LAMBDA;
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 84:
/* 1576 */         if (j != 0) return ParensResult.CAST;
/*      */ 
/* 1579 */         switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.S.token(k + 1).kind.ordinal()]) { case 9:
/*      */         case 22:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 48:
/*      */         case 49:
/*      */         case 50:
/*      */         case 51:
/*      */         case 52:
/*      */         case 53:
/*      */         case 54:
/*      */         case 55:
/*      */         case 56:
/*      */         case 69:
/*      */         case 70:
/*      */         case 71:
/*      */         case 73:
/*      */         case 79:
/*      */         case 80:
/*      */         case 83:
/* 1589 */           return ParensResult.CAST;
/*      */         case 10:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 20:
/*      */         case 21:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */         case 38:
/*      */         case 39:
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 46:
/*      */         case 47:
/*      */         case 57:
/*      */         case 58:
/*      */         case 59:
/*      */         case 60:
/*      */         case 61:
/*      */         case 62:
/*      */         case 63:
/*      */         case 64:
/*      */         case 65:
/*      */         case 66:
/*      */         case 67:
/*      */         case 68:
/*      */         case 72:
/*      */         case 74:
/*      */         case 75:
/*      */         case 76:
/*      */         case 77:
/*      */         case 78:
/*      */         case 81:
/* 1591 */         case 82: } return ParensResult.PARENS;
/*      */       case 9:
/*      */       case 31:
/*      */       case 32:
/*      */       case 83:
/* 1597 */         if (peekToken(k, this.LAX_IDENTIFIER))
/*      */         {
/* 1599 */           return ParensResult.EXPLICIT_LAMBDA;
/* 1600 */         }if (peekToken(k, Tokens.TokenKind.RPAREN, Tokens.TokenKind.ARROW))
/*      */         {
/* 1602 */           return ParensResult.IMPLICIT_LAMBDA;
/*      */         }
/* 1604 */         j = 0;
/* 1605 */         break;
/*      */       case 3:
/*      */       case 75:
/* 1609 */         return ParensResult.EXPLICIT_LAMBDA;
/*      */       case 5:
/* 1611 */         j = 1;
/* 1612 */         k++;
/* 1613 */         while (peekToken(k, Tokens.TokenKind.DOT)) {
/* 1614 */           k += 2;
/*      */         }
/* 1616 */         if (peekToken(k, Tokens.TokenKind.LPAREN)) {
/* 1617 */           k++;
/*      */ 
/* 1619 */           int m = 0;
/* 1620 */           for (; ; k++) {
/* 1621 */             Tokens.TokenKind localTokenKind2 = this.S.token(k).kind;
/* 1622 */             switch (localTokenKind2) {
/*      */             case EOF:
/* 1624 */               return ParensResult.PARENS;
/*      */             case LPAREN:
/* 1626 */               m++;
/* 1627 */               break;
/*      */             case RPAREN:
/* 1629 */               m--;
/* 1630 */               if (m == 0)
/*      */               {
/*      */                 break label1191;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         break;
/*      */       case 72:
/* 1639 */         if (peekToken(k, Tokens.TokenKind.RBRACKET, this.LAX_IDENTIFIER))
/*      */         {
/* 1641 */           return ParensResult.EXPLICIT_LAMBDA;
/* 1642 */         }if ((peekToken(k, Tokens.TokenKind.RBRACKET, Tokens.TokenKind.RPAREN)) || 
/* 1643 */           (peekToken(k, Tokens.TokenKind.RBRACKET, Tokens.TokenKind.AMP)))
/*      */         {
/* 1646 */           return ParensResult.CAST;
/* 1647 */         }if (peekToken(k, Tokens.TokenKind.RBRACKET))
/*      */         {
/* 1649 */           j = 1;
/* 1650 */           k++;
/*      */         }
/*      */         else {
/* 1653 */           return ParensResult.PARENS;
/*      */         }break;
/*      */       case 21:
/* 1656 */         i++; break;
/*      */       case 88:
/* 1658 */         i--;
/*      */       case 89:
/* 1660 */         i--;
/*      */       case 90:
/* 1662 */         i--;
/* 1663 */         if (i == 0) {
/* 1664 */           if ((peekToken(k, Tokens.TokenKind.RPAREN)) || 
/* 1665 */             (peekToken(k, Tokens.TokenKind.AMP)))
/*      */           {
/* 1668 */             return ParensResult.CAST;
/* 1669 */           }if ((peekToken(k, this.LAX_IDENTIFIER, Tokens.TokenKind.COMMA)) || 
/* 1670 */             (peekToken(k, this.LAX_IDENTIFIER, Tokens.TokenKind.RPAREN, Tokens.TokenKind.ARROW)) || 
/* 1671 */             (peekToken(k, Tokens.TokenKind.ELLIPSIS)))
/*      */           {
/* 1675 */             return ParensResult.EXPLICIT_LAMBDA;
/*      */           }
/*      */ 
/* 1679 */           j = 1;
/*      */         }
/* 1681 */         else if (i < 0)
/*      */         {
/* 1683 */           return ParensResult.PARENS; } break;
/*      */       case 4:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 30:
/*      */       case 33:
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
/*      */       case 71:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 86:
/*      */       default:
/* 1688 */         return ParensResult.PARENS;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression lambdaExpressionOrStatement(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
/*      */   {
/* 1710 */     List localList = paramBoolean2 ? 
/* 1709 */       formalParameters(true) : 
/* 1710 */       implicitParameters(paramBoolean1);
/*      */ 
/* 1712 */     return lambdaExpressionOrStatementRest(localList, paramInt);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression lambdaExpressionOrStatementRest(List<JCTree.JCVariableDecl> paramList, int paramInt) {
/* 1716 */     checkLambda();
/* 1717 */     accept(Tokens.TokenKind.ARROW);
/*      */ 
/* 1721 */     return this.token.kind == Tokens.TokenKind.LBRACE ? 
/* 1720 */       lambdaStatement(paramList, paramInt, paramInt) : 
/* 1721 */       lambdaExpression(paramList, paramInt);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression lambdaStatement(List<JCTree.JCVariableDecl> paramList, int paramInt1, int paramInt2)
/*      */   {
/* 1725 */     JCTree.JCBlock localJCBlock = block(paramInt2, 0L);
/* 1726 */     return (JCTree.JCExpression)toP(this.F.at(paramInt1).Lambda(paramList, localJCBlock));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression lambdaExpression(List<JCTree.JCVariableDecl> paramList, int paramInt) {
/* 1730 */     JCTree.JCExpression localJCExpression = parseExpression();
/* 1731 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).Lambda(paramList, localJCExpression));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression superSuffix(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1737 */     nextToken();
/* 1738 */     if ((this.token.kind == Tokens.TokenKind.LPAREN) || (paramList != null)) {
/* 1739 */       paramJCExpression = arguments(paramList, paramJCExpression);
/* 1740 */     } else if (this.token.kind == Tokens.TokenKind.COLCOL) {
/* 1741 */       if (paramList != null) return illegal();
/* 1742 */       paramJCExpression = memberReferenceSuffix(paramJCExpression);
/*      */     } else {
/* 1744 */       int i = this.token.pos;
/* 1745 */       accept(Tokens.TokenKind.DOT);
/* 1746 */       paramList = this.token.kind == Tokens.TokenKind.LT ? typeArguments(false) : null;
/* 1747 */       paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(paramJCExpression, ident()));
/* 1748 */       paramJCExpression = argumentsOpt(paramList, paramJCExpression);
/*      */     }
/* 1750 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCPrimitiveTypeTree basicType()
/*      */   {
/* 1756 */     JCTree.JCPrimitiveTypeTree localJCPrimitiveTypeTree = (JCTree.JCPrimitiveTypeTree)to(this.F.at(this.token.pos).TypeIdent(typetag(this.token.kind)));
/* 1757 */     nextToken();
/* 1758 */     return localJCPrimitiveTypeTree;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression argumentsOpt(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1764 */     if ((((this.mode & 0x1) != 0) && (this.token.kind == Tokens.TokenKind.LPAREN)) || (paramList != null)) {
/* 1765 */       this.mode = 1;
/* 1766 */       return arguments(paramList, paramJCExpression);
/*      */     }
/* 1768 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> arguments()
/*      */   {
/* 1775 */     ListBuffer localListBuffer = new ListBuffer();
/* 1776 */     if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 1777 */       nextToken();
/* 1778 */       if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 1779 */         localListBuffer.append(parseExpression());
/* 1780 */         while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 1781 */           nextToken();
/* 1782 */           localListBuffer.append(parseExpression());
/*      */         }
/*      */       }
/* 1785 */       accept(Tokens.TokenKind.RPAREN);
/*      */     } else {
/* 1787 */       syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LPAREN });
/*      */     }
/* 1789 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   JCTree.JCMethodInvocation arguments(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression) {
/* 1793 */     int i = this.token.pos;
/* 1794 */     List localList = arguments();
/* 1795 */     return (JCTree.JCMethodInvocation)toP(this.F.at(i).Apply(paramList, paramJCExpression, localList));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression typeArgumentsOpt(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1801 */     if ((this.token.kind == Tokens.TokenKind.LT) && ((this.mode & 0x2) != 0) && ((this.mode & 0x4) == 0))
/*      */     {
/* 1804 */       this.mode = 2;
/* 1805 */       checkGenerics();
/* 1806 */       return typeArguments(paramJCExpression, false);
/*      */     }
/* 1808 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> typeArgumentsOpt() {
/* 1812 */     return typeArgumentsOpt(2);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> typeArgumentsOpt(int paramInt) {
/* 1816 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 1817 */       checkGenerics();
/* 1818 */       if (((this.mode & paramInt) == 0) || ((this.mode & 0x4) != 0))
/*      */       {
/* 1820 */         illegal();
/*      */       }
/* 1822 */       this.mode = paramInt;
/* 1823 */       return typeArguments(false);
/*      */     }
/* 1825 */     return null;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> typeArguments(boolean paramBoolean)
/*      */   {
/* 1834 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 1835 */       nextToken();
/* 1836 */       if ((this.token.kind == Tokens.TokenKind.GT) && (paramBoolean)) {
/* 1837 */         checkDiamond();
/* 1838 */         this.mode |= 16;
/* 1839 */         nextToken();
/* 1840 */         return List.nil();
/*      */       }
/* 1842 */       ListBuffer localListBuffer = new ListBuffer();
/* 1843 */       localListBuffer.append((this.mode & 0x1) == 0 ? typeArgument() : parseType());
/* 1844 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 1845 */         nextToken();
/* 1846 */         localListBuffer.append((this.mode & 0x1) == 0 ? typeArgument() : parseType());
/*      */       }
/* 1848 */       switch (this.token.kind) { case GTGTEQ:
/*      */       case GTGTGTEQ:
/*      */       case GTGTGT:
/*      */       case GTGT:
/*      */       case GTEQ:
/* 1852 */         this.token = this.S.split();
/* 1853 */         break;
/*      */       case GT:
/* 1855 */         nextToken();
/* 1856 */         break;
/*      */       default:
/* 1858 */         localListBuffer.append(syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.GT }));
/*      */       }
/*      */ 
/* 1861 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/* 1864 */     return List.of(syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LT }));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression typeArgument()
/*      */   {
/* 1877 */     List localList = typeAnnotationsOpt();
/* 1878 */     if (this.token.kind != Tokens.TokenKind.QUES) return parseType(localList);
/* 1879 */     int i = this.token.pos;
/* 1880 */     nextToken();
/*      */     JCTree.TypeBoundKind localTypeBoundKind;
/*      */     JCTree.JCExpression localJCExpression;
/*      */     Object localObject;
/* 1882 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 1883 */       localTypeBoundKind = (JCTree.TypeBoundKind)to(this.F.at(i).TypeBoundKind(BoundKind.EXTENDS));
/* 1884 */       nextToken();
/* 1885 */       localJCExpression = parseType();
/* 1886 */       localObject = this.F.at(i).Wildcard(localTypeBoundKind, localJCExpression);
/* 1887 */     } else if (this.token.kind == Tokens.TokenKind.SUPER) {
/* 1888 */       localTypeBoundKind = (JCTree.TypeBoundKind)to(this.F.at(i).TypeBoundKind(BoundKind.SUPER));
/* 1889 */       nextToken();
/* 1890 */       localJCExpression = parseType();
/* 1891 */       localObject = this.F.at(i).Wildcard(localTypeBoundKind, localJCExpression);
/* 1892 */     } else if (this.LAX_IDENTIFIER.accepts(this.token.kind))
/*      */     {
/* 1894 */       localTypeBoundKind = this.F.at(-1).TypeBoundKind(BoundKind.UNBOUND);
/* 1895 */       localJCExpression = (JCTree.JCExpression)toP(this.F.at(i).Wildcard(localTypeBoundKind, null));
/* 1896 */       JCTree.JCIdent localJCIdent = (JCTree.JCIdent)toP(this.F.at(this.token.pos).Ident(ident()));
/* 1897 */       JCTree.JCErroneous localJCErroneous = this.F.at(i).Erroneous(List.of(localJCExpression, localJCIdent));
/* 1898 */       reportSyntaxError(localJCErroneous, "expected3", new Object[] { Tokens.TokenKind.GT, Tokens.TokenKind.EXTENDS, Tokens.TokenKind.SUPER });
/* 1899 */       localObject = localJCErroneous;
/*      */     } else {
/* 1901 */       localTypeBoundKind = (JCTree.TypeBoundKind)toP(this.F.at(i).TypeBoundKind(BoundKind.UNBOUND));
/* 1902 */       localObject = (JCTree.JCExpression)toP(this.F.at(i).Wildcard(localTypeBoundKind, null));
/*      */     }
/* 1904 */     if (!localList.isEmpty()) {
/* 1905 */       localObject = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList.head).pos).AnnotatedType(localList, (JCTree.JCExpression)localObject));
/*      */     }
/* 1907 */     return localObject;
/*      */   }
/*      */ 
/*      */   JCTree.JCTypeApply typeArguments(JCTree.JCExpression paramJCExpression, boolean paramBoolean) {
/* 1911 */     int i = this.token.pos;
/* 1912 */     List localList = typeArguments(paramBoolean);
/* 1913 */     return (JCTree.JCTypeApply)toP(this.F.at(i).TypeApply(paramJCExpression, localList));
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression bracketsOpt(JCTree.JCExpression paramJCExpression, List<JCTree.JCAnnotation> paramList)
/*      */   {
/* 1926 */     List localList = typeAnnotationsOpt();
/*      */ 
/* 1928 */     if (this.token.kind == Tokens.TokenKind.LBRACKET) {
/* 1929 */       int i = this.token.pos;
/* 1930 */       nextToken();
/* 1931 */       paramJCExpression = bracketsOptCont(paramJCExpression, i, localList);
/* 1932 */     } else if (!localList.isEmpty()) {
/* 1933 */       if (this.permitTypeAnnotationsPushBack)
/* 1934 */         this.typeAnnotationsPushedBack = localList;
/*      */       else {
/* 1936 */         return illegal(((JCTree.JCAnnotation)localList.head).pos);
/*      */       }
/*      */     }
/*      */ 
/* 1940 */     if (!paramList.isEmpty()) {
/* 1941 */       paramJCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).AnnotatedType(paramList, paramJCExpression));
/*      */     }
/* 1943 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression bracketsOpt(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1949 */     return bracketsOpt(paramJCExpression, List.nil());
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression bracketsOptCont(JCTree.JCExpression paramJCExpression, int paramInt, List<JCTree.JCAnnotation> paramList)
/*      */   {
/* 1954 */     accept(Tokens.TokenKind.RBRACKET);
/* 1955 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 1956 */     paramJCExpression = (JCTree.JCExpression)toP(this.F.at(paramInt).TypeArray(paramJCExpression));
/* 1957 */     if (paramList.nonEmpty()) {
/* 1958 */       paramJCExpression = (JCTree.JCExpression)toP(this.F.at(paramInt).AnnotatedType(paramList, paramJCExpression));
/*      */     }
/* 1960 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression bracketsSuffix(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1967 */     if (((this.mode & 0x1) != 0) && (this.token.kind == Tokens.TokenKind.DOT)) {
/* 1968 */       this.mode = 1;
/* 1969 */       int i = this.token.pos;
/* 1970 */       nextToken();
/* 1971 */       accept(Tokens.TokenKind.CLASS);
/* 1972 */       if (this.token.pos == this.endPosTable.errorEndPos)
/*      */       {
/*      */         Name localName;
/* 1975 */         if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 1976 */           localName = this.token.name();
/* 1977 */           nextToken();
/*      */         } else {
/* 1979 */           localName = this.names.error;
/*      */         }
/* 1981 */         paramJCExpression = this.F.at(i).Erroneous(List.of(toP(this.F.at(i).Select(paramJCExpression, localName))));
/*      */       } else {
/* 1983 */         paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(paramJCExpression, this.names._class));
/*      */       }
/* 1985 */     } else if ((this.mode & 0x2) != 0) {
/* 1986 */       if (this.token.kind != Tokens.TokenKind.COLCOL)
/* 1987 */         this.mode = 2;
/*      */     }
/* 1989 */     else if (this.token.kind != Tokens.TokenKind.COLCOL) {
/* 1990 */       syntaxError(this.token.pos, "dot.class.expected", new Tokens.TokenKind[0]);
/*      */     }
/* 1992 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression memberReferenceSuffix(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2000 */     int i = this.token.pos;
/* 2001 */     accept(Tokens.TokenKind.COLCOL);
/* 2002 */     return memberReferenceSuffix(i, paramJCExpression);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression memberReferenceSuffix(int paramInt, JCTree.JCExpression paramJCExpression) {
/* 2006 */     checkMethodReferences();
/* 2007 */     this.mode = 1;
/* 2008 */     List localList = null;
/* 2009 */     if (this.token.kind == Tokens.TokenKind.LT)
/* 2010 */       localList = typeArguments(false);
/*      */     MemberReferenceTree.ReferenceMode localReferenceMode;
/*      */     Name localName;
/* 2014 */     if (this.token.kind == Tokens.TokenKind.NEW) {
/* 2015 */       localReferenceMode = MemberReferenceTree.ReferenceMode.NEW;
/* 2016 */       localName = this.names.init;
/* 2017 */       nextToken();
/*      */     } else {
/* 2019 */       localReferenceMode = MemberReferenceTree.ReferenceMode.INVOKE;
/* 2020 */       localName = ident();
/*      */     }
/* 2022 */     return (JCTree.JCExpression)toP(this.F.at(paramJCExpression.getStartPosition()).Reference(localReferenceMode, localName, paramJCExpression, localList));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression creator(int paramInt, List<JCTree.JCExpression> paramList)
/*      */   {
/* 2028 */     List localList1 = typeAnnotationsOpt();
/*      */ 
/* 2030 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) { case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/* 2033 */       if (paramList == null) {
/* 2034 */         if (localList1.isEmpty()) {
/* 2035 */           return arrayCreatorRest(paramInt, basicType());
/*      */         }
/* 2037 */         return arrayCreatorRest(paramInt, (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList1.head).pos).AnnotatedType(localList1, basicType())));
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/* 2043 */     Object localObject1 = qualident(true);
/*      */ 
/* 2045 */     int i = this.mode;
/* 2046 */     this.mode = 2;
/* 2047 */     int j = 0;
/* 2048 */     int k = -1;
/* 2049 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 2050 */       checkGenerics();
/* 2051 */       k = this.token.pos;
/* 2052 */       localObject1 = typeArguments((JCTree.JCExpression)localObject1, true);
/* 2053 */       j = (this.mode & 0x10) != 0 ? 1 : 0;
/*      */     }
/* 2055 */     while (this.token.kind == Tokens.TokenKind.DOT) {
/* 2056 */       if (j != 0)
/*      */       {
/* 2058 */         illegal();
/*      */       }
/* 2060 */       int m = this.token.pos;
/* 2061 */       nextToken();
/* 2062 */       List localList2 = typeAnnotationsOpt();
/* 2063 */       localObject1 = (JCTree.JCExpression)toP(this.F.at(m).Select((JCTree.JCExpression)localObject1, ident()));
/*      */ 
/* 2065 */       if ((localList2 != null) && (localList2.nonEmpty())) {
/* 2066 */         localObject1 = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList2.head).pos).AnnotatedType(localList2, (JCTree.JCExpression)localObject1));
/*      */       }
/*      */ 
/* 2069 */       if (this.token.kind == Tokens.TokenKind.LT) {
/* 2070 */         k = this.token.pos;
/* 2071 */         checkGenerics();
/* 2072 */         localObject1 = typeArguments((JCTree.JCExpression)localObject1, true);
/* 2073 */         j = (this.mode & 0x10) != 0 ? 1 : 0;
/*      */       }
/*      */     }
/* 2076 */     this.mode = i;
/*      */     Object localObject2;
/* 2077 */     if ((this.token.kind == Tokens.TokenKind.LBRACKET) || (this.token.kind == Tokens.TokenKind.MONKEYS_AT))
/*      */     {
/* 2079 */       if (localList1.nonEmpty()) {
/* 2080 */         localObject1 = insertAnnotationsToMostInner((JCTree.JCExpression)localObject1, localList1, false);
/*      */       }
/*      */ 
/* 2083 */       localObject2 = arrayCreatorRest(paramInt, (JCTree.JCExpression)localObject1);
/* 2084 */       if (j != 0) {
/* 2085 */         reportSyntaxError(k, "cannot.create.array.with.diamond", new Object[0]);
/* 2086 */         return (JCTree.JCExpression)toP(this.F.at(paramInt).Erroneous(List.of(localObject2)));
/*      */       }
/* 2088 */       if (paramList != null) {
/* 2089 */         int n = paramInt;
/* 2090 */         if ((!paramList.isEmpty()) && (((JCTree.JCExpression)paramList.head).pos != -1))
/*      */         {
/* 2094 */           n = ((JCTree.JCExpression)paramList.head).pos;
/*      */         }
/* 2096 */         setErrorEndPos(this.S.prevToken().endPos);
/* 2097 */         JCTree.JCErroneous localJCErroneous = this.F.at(n).Erroneous(paramList.prepend(localObject2));
/* 2098 */         reportSyntaxError(localJCErroneous, "cannot.create.array.with.type.arguments", new Object[0]);
/* 2099 */         return (JCTree.JCExpression)toP(localJCErroneous);
/*      */       }
/* 2101 */       return localObject2;
/* 2102 */     }if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 2103 */       localObject2 = classCreatorRest(paramInt, null, paramList, (JCTree.JCExpression)localObject1);
/* 2104 */       if (((JCTree.JCNewClass)localObject2).def != null) {
/* 2105 */         assert (((JCTree.JCNewClass)localObject2).def.mods.annotations.isEmpty());
/* 2106 */         if (localList1.nonEmpty())
/*      */         {
/* 2111 */           ((JCTree.JCNewClass)localObject2).def.mods.pos = earlier(((JCTree.JCNewClass)localObject2).def.mods.pos, ((JCTree.JCAnnotation)localList1.head).pos);
/* 2112 */           ((JCTree.JCNewClass)localObject2).def.mods.annotations = localList1;
/*      */         }
/*      */ 
/*      */       }
/* 2116 */       else if (localList1.nonEmpty()) {
/* 2117 */         localObject1 = insertAnnotationsToMostInner((JCTree.JCExpression)localObject1, localList1, false);
/* 2118 */         ((JCTree.JCNewClass)localObject2).clazz = ((JCTree.JCExpression)localObject1);
/*      */       }
/*      */ 
/* 2121 */       return localObject2;
/*      */     }
/* 2123 */     setErrorEndPos(this.token.pos);
/* 2124 */     reportSyntaxError(this.token.pos, "expected2", new Object[] { Tokens.TokenKind.LPAREN, Tokens.TokenKind.LBRACKET });
/* 2125 */     localObject1 = (JCTree.JCExpression)toP(this.F.at(paramInt).NewClass(null, paramList, (JCTree.JCExpression)localObject1, List.nil(), null));
/* 2126 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).Erroneous(List.of(localObject1)));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression innerCreator(int paramInt, List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2133 */     List localList = typeAnnotationsOpt();
/*      */ 
/* 2135 */     Object localObject = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*      */ 
/* 2137 */     if (localList.nonEmpty()) {
/* 2138 */       localObject = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)localList.head).pos).AnnotatedType(localList, (JCTree.JCExpression)localObject));
/*      */     }
/*      */ 
/* 2141 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 2142 */       int i = this.mode;
/* 2143 */       checkGenerics();
/* 2144 */       localObject = typeArguments((JCTree.JCExpression)localObject, true);
/* 2145 */       this.mode = i;
/*      */     }
/* 2147 */     return classCreatorRest(paramInt, paramJCExpression, paramList, (JCTree.JCExpression)localObject);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression arrayCreatorRest(int paramInt, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2154 */     List localList = typeAnnotationsOpt();
/*      */ 
/* 2156 */     accept(Tokens.TokenKind.LBRACKET);
/* 2157 */     if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2158 */       accept(Tokens.TokenKind.RBRACKET);
/* 2159 */       paramJCExpression = bracketsOpt(paramJCExpression, localList);
/* 2160 */       if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 2161 */         localObject1 = (JCTree.JCNewArray)arrayInitializer(paramInt, paramJCExpression);
/* 2162 */         if (localList.nonEmpty())
/*      */         {
/* 2169 */           localObject2 = (JCTree.JCAnnotatedType)paramJCExpression;
/* 2170 */           assert (((JCTree.JCAnnotatedType)localObject2).annotations == localList);
/* 2171 */           ((JCTree.JCNewArray)localObject1).annotations = ((JCTree.JCAnnotatedType)localObject2).annotations;
/* 2172 */           ((JCTree.JCNewArray)localObject1).elemtype = ((JCTree.JCAnnotatedType)localObject2).underlyingType;
/*      */         }
/* 2174 */         return localObject1;
/*      */       }
/* 2176 */       localObject1 = (JCTree.JCExpression)toP(this.F.at(paramInt).NewArray(paramJCExpression, List.nil(), null));
/* 2177 */       return syntaxError(this.token.pos, List.of(localObject1), "array.dimension.missing", new Tokens.TokenKind[0]);
/*      */     }
/*      */ 
/* 2180 */     Object localObject1 = new ListBuffer();
/*      */ 
/* 2183 */     Object localObject2 = new ListBuffer();
/* 2184 */     ((ListBuffer)localObject2).append(localList);
/*      */ 
/* 2186 */     ((ListBuffer)localObject1).append(parseExpression());
/* 2187 */     accept(Tokens.TokenKind.RBRACKET);
/* 2188 */     while ((this.token.kind == Tokens.TokenKind.LBRACKET) || (this.token.kind == Tokens.TokenKind.MONKEYS_AT))
/*      */     {
/* 2190 */       localObject3 = typeAnnotationsOpt();
/* 2191 */       int i = this.token.pos;
/* 2192 */       nextToken();
/* 2193 */       if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2194 */         paramJCExpression = bracketsOptCont(paramJCExpression, i, (List)localObject3);
/*      */       }
/* 2196 */       else if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2197 */         paramJCExpression = bracketsOptCont(paramJCExpression, i, (List)localObject3);
/*      */       } else {
/* 2199 */         ((ListBuffer)localObject2).append(localObject3);
/* 2200 */         ((ListBuffer)localObject1).append(parseExpression());
/* 2201 */         accept(Tokens.TokenKind.RBRACKET);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2206 */     Object localObject3 = (JCTree.JCNewArray)toP(this.F.at(paramInt).NewArray(paramJCExpression, ((ListBuffer)localObject1).toList(), null));
/* 2207 */     ((JCTree.JCNewArray)localObject3).dimAnnotations = ((ListBuffer)localObject2).toList();
/* 2208 */     return localObject3;
/*      */   }
/*      */ 
/*      */   JCTree.JCNewClass classCreatorRest(int paramInt, JCTree.JCExpression paramJCExpression1, List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression2)
/*      */   {
/* 2219 */     List localList1 = arguments();
/* 2220 */     JCTree.JCClassDecl localJCClassDecl = null;
/* 2221 */     if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 2222 */       int i = this.token.pos;
/* 2223 */       List localList2 = classOrInterfaceBody(this.names.empty, false);
/* 2224 */       JCTree.JCModifiers localJCModifiers = this.F.at(-1).Modifiers(0L);
/* 2225 */       localJCClassDecl = (JCTree.JCClassDecl)toP(this.F.at(i).AnonymousClassDef(localJCModifiers, localList2));
/*      */     }
/* 2227 */     return (JCTree.JCNewClass)toP(this.F.at(paramInt).NewClass(paramJCExpression1, paramList, paramJCExpression2, localList1, localJCClassDecl));
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression arrayInitializer(int paramInt, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2233 */     accept(Tokens.TokenKind.LBRACE);
/* 2234 */     ListBuffer localListBuffer = new ListBuffer();
/* 2235 */     if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2236 */       nextToken();
/* 2237 */     } else if (this.token.kind != Tokens.TokenKind.RBRACE) {
/* 2238 */       localListBuffer.append(variableInitializer());
/* 2239 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2240 */         nextToken();
/* 2241 */         if (this.token.kind == Tokens.TokenKind.RBRACE) break;
/* 2242 */         localListBuffer.append(variableInitializer());
/*      */       }
/*      */     }
/* 2245 */     accept(Tokens.TokenKind.RBRACE);
/* 2246 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).NewArray(paramJCExpression, List.nil(), localListBuffer.toList()));
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression variableInitializer()
/*      */   {
/* 2252 */     return this.token.kind == Tokens.TokenKind.LBRACE ? arrayInitializer(this.token.pos, null) : parseExpression();
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression parExpression()
/*      */   {
/* 2258 */     int i = this.token.pos;
/* 2259 */     accept(Tokens.TokenKind.LPAREN);
/* 2260 */     JCTree.JCExpression localJCExpression = parseExpression();
/* 2261 */     accept(Tokens.TokenKind.RPAREN);
/* 2262 */     return (JCTree.JCExpression)toP(this.F.at(i).Parens(localJCExpression));
/*      */   }
/*      */ 
/*      */   JCTree.JCBlock block(int paramInt, long paramLong)
/*      */   {
/* 2268 */     accept(Tokens.TokenKind.LBRACE);
/* 2269 */     List localList = blockStatements();
/* 2270 */     JCTree.JCBlock localJCBlock = this.F.at(paramInt).Block(paramLong, localList);
/* 2271 */     while ((this.token.kind == Tokens.TokenKind.CASE) || (this.token.kind == Tokens.TokenKind.DEFAULT)) {
/* 2272 */       syntaxError("orphaned", this.token.kind);
/* 2273 */       switchBlockStatementGroups();
/*      */     }
/*      */ 
/* 2277 */     localJCBlock.endpos = this.token.pos;
/* 2278 */     accept(Tokens.TokenKind.RBRACE);
/* 2279 */     return (JCTree.JCBlock)toP(localJCBlock);
/*      */   }
/*      */ 
/*      */   public JCTree.JCBlock block() {
/* 2283 */     return block(this.token.pos, 0L);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCStatement> blockStatements()
/*      */   {
/* 2296 */     ListBuffer localListBuffer = new ListBuffer();
/*      */     while (true) {
/* 2298 */       List localList = blockStatement();
/* 2299 */       if (localList.isEmpty()) {
/* 2300 */         return localListBuffer.toList();
/*      */       }
/* 2302 */       if (this.token.pos <= this.endPosTable.errorEndPos) {
/* 2303 */         skip(false, true, true, true);
/*      */       }
/* 2305 */       localListBuffer.addAll(localList);
/*      */     }
/*      */   }
/*      */ 
/*      */   JCTree.JCStatement parseStatementAsBlock()
/*      */   {
/* 2316 */     int i = this.token.pos;
/* 2317 */     List localList1 = blockStatement();
/* 2318 */     if (localList1.isEmpty()) {
/* 2319 */       localObject = this.F.at(i).Erroneous();
/* 2320 */       error((JCDiagnostic.DiagnosticPosition)localObject, "illegal.start.of.stmt", new Object[0]);
/* 2321 */       return this.F.at(i).Exec((JCTree.JCExpression)localObject);
/*      */     }
/* 2323 */     Object localObject = (JCTree.JCStatement)localList1.head;
/* 2324 */     String str = null;
/* 2325 */     switch (localObject.getTag()) {
/*      */     case CLASSDEF:
/* 2327 */       str = "class.not.allowed";
/* 2328 */       break;
/*      */     case VARDEF:
/* 2330 */       str = "variable.not.allowed";
/*      */     }
/*      */ 
/* 2333 */     if (str != null) {
/* 2334 */       error((JCDiagnostic.DiagnosticPosition)localObject, str, new Object[0]);
/* 2335 */       List localList2 = List.of(this.F.at(((JCTree.JCStatement)localObject).pos).Block(0L, localList1));
/* 2336 */       return (JCTree.JCStatement)toP(this.F.at(i).Exec(this.F.at(((JCTree.JCStatement)localObject).pos).Erroneous(localList2)));
/*      */     }
/* 2338 */     return localObject;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCStatement> blockStatement()
/*      */   {
/* 2345 */     int i = this.token.pos;
/*      */     Tokens.Comment localComment;
/* 2346 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[this.token.kind.ordinal()]) { case 6:
/*      */     case 12:
/*      */     case 33:
/*      */     case 34:
/* 2348 */       return List.nil();
/*      */     case 1:
/*      */     case 11:
/*      */     case 19:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/* 2352 */       return List.of(parseStatement());
/*      */     case 3:
/*      */     case 5:
/* 2355 */       localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2356 */       localObject1 = modifiersOpt();
/* 2357 */       if ((this.token.kind == Tokens.TokenKind.INTERFACE) || (this.token.kind == Tokens.TokenKind.CLASS) || ((this.allowEnums) && (this.token.kind == Tokens.TokenKind.ENUM)))
/*      */       {
/* 2360 */         return List.of(classOrInterfaceOrEnumDeclaration((JCTree.JCModifiers)localObject1, localComment));
/*      */       }
/* 2362 */       localJCExpression = parseType();
/*      */ 
/* 2364 */       localObject2 = variableDeclarators((JCTree.JCModifiers)localObject1, localJCExpression, new ListBuffer());
/*      */ 
/* 2366 */       storeEnd((JCTree)((ListBuffer)localObject2).last(), this.token.endPos);
/* 2367 */       accept(Tokens.TokenKind.SEMI);
/* 2368 */       return ((ListBuffer)localObject2).toList();
/*      */     case 4:
/*      */     case 20:
/* 2372 */       localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2373 */       localObject1 = modifiersOpt();
/* 2374 */       return List.of(classOrInterfaceOrEnumDeclaration((JCTree.JCModifiers)localObject1, localComment));
/*      */     case 7:
/*      */     case 8:
/* 2378 */       localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2379 */       return List.of(classOrInterfaceOrEnumDeclaration(modifiersOpt(), localComment));
/*      */     case 9:
/*      */     case 83:
/* 2382 */       if ((this.allowEnums) && (this.token.kind == Tokens.TokenKind.ENUM)) {
/* 2383 */         error(this.token.pos, "local.enum", new Object[0]);
/* 2384 */         localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2385 */         return List.of(classOrInterfaceOrEnumDeclaration(modifiersOpt(), localComment));
/* 2386 */       }if ((this.allowAsserts) && (this.token.kind == Tokens.TokenKind.ASSERT))
/* 2387 */         return List.of(parseStatement()); break;
/*      */     case 2:
/*      */     case 10:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
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
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
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
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/* 2391 */     case 82: } Object localObject1 = this.token;
/* 2392 */     JCTree.JCExpression localJCExpression = term(3);
/* 2393 */     if ((this.token.kind == Tokens.TokenKind.COLON) && (localJCExpression.hasTag(JCTree.Tag.IDENT))) {
/* 2394 */       nextToken();
/* 2395 */       localObject2 = parseStatement();
/* 2396 */       return List.of(this.F.at(i).Labelled(((Tokens.Token)localObject1).name(), (JCTree.JCStatement)localObject2));
/* 2397 */     }if (((this.lastmode & 0x2) != 0) && (this.LAX_IDENTIFIER.accepts(this.token.kind))) {
/* 2398 */       i = this.token.pos;
/* 2399 */       localObject2 = this.F.at(-1).Modifiers(0L);
/* 2400 */       this.F.at(i);
/*      */ 
/* 2402 */       ListBuffer localListBuffer = variableDeclarators((JCTree.JCModifiers)localObject2, localJCExpression, new ListBuffer());
/*      */ 
/* 2404 */       storeEnd((JCTree)localListBuffer.last(), this.token.endPos);
/* 2405 */       accept(Tokens.TokenKind.SEMI);
/* 2406 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/* 2409 */     Object localObject2 = (JCTree.JCExpressionStatement)to(this.F.at(i).Exec(checkExprStat(localJCExpression)));
/* 2410 */     accept(Tokens.TokenKind.SEMI);
/* 2411 */     return List.of(localObject2);
/*      */   }
/*      */ 
/*      */   public JCTree.JCStatement parseStatement()
/*      */   {
/* 2438 */     int i = this.token.pos;
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2439 */     switch (this.token.kind) {
/*      */     case LBRACE:
/* 2441 */       return block();
/*      */     case IF:
/* 2443 */       nextToken();
/* 2444 */       localObject1 = parExpression();
/* 2445 */       localObject2 = parseStatementAsBlock();
/* 2446 */       localObject3 = null;
/* 2447 */       if (this.token.kind == Tokens.TokenKind.ELSE) {
/* 2448 */         nextToken();
/* 2449 */         localObject3 = parseStatementAsBlock();
/*      */       }
/* 2451 */       return this.F.at(i).If((JCTree.JCExpression)localObject1, (JCTree.JCStatement)localObject2, (JCTree.JCStatement)localObject3);
/*      */     case FOR:
/* 2454 */       nextToken();
/* 2455 */       accept(Tokens.TokenKind.LPAREN);
/* 2456 */       localObject1 = this.token.kind == Tokens.TokenKind.SEMI ? List.nil() : forInit();
/* 2457 */       if ((((List)localObject1).length() == 1) && 
/* 2458 */         (((JCTree.JCStatement)((List)localObject1).head)
/* 2458 */         .hasTag(JCTree.Tag.VARDEF)) && 
/* 2458 */         (((JCTree.JCVariableDecl)((List)localObject1).head).init == null) && (this.token.kind == Tokens.TokenKind.COLON))
/*      */       {
/* 2461 */         checkForeach();
/* 2462 */         localObject2 = (JCTree.JCVariableDecl)((List)localObject1).head;
/* 2463 */         accept(Tokens.TokenKind.COLON);
/* 2464 */         localObject3 = parseExpression();
/* 2465 */         accept(Tokens.TokenKind.RPAREN);
/* 2466 */         localObject4 = parseStatementAsBlock();
/* 2467 */         return this.F.at(i).ForeachLoop((JCTree.JCVariableDecl)localObject2, (JCTree.JCExpression)localObject3, (JCTree.JCStatement)localObject4);
/*      */       }
/* 2469 */       accept(Tokens.TokenKind.SEMI);
/* 2470 */       localObject2 = this.token.kind == Tokens.TokenKind.SEMI ? null : parseExpression();
/* 2471 */       accept(Tokens.TokenKind.SEMI);
/* 2472 */       localObject3 = this.token.kind == Tokens.TokenKind.RPAREN ? List.nil() : forUpdate();
/* 2473 */       accept(Tokens.TokenKind.RPAREN);
/* 2474 */       localObject4 = parseStatementAsBlock();
/* 2475 */       return this.F.at(i).ForLoop((List)localObject1, (JCTree.JCExpression)localObject2, (List)localObject3, (JCTree.JCStatement)localObject4);
/*      */     case WHILE:
/* 2479 */       nextToken();
/* 2480 */       localObject1 = parExpression();
/* 2481 */       localObject2 = parseStatementAsBlock();
/* 2482 */       return this.F.at(i).WhileLoop((JCTree.JCExpression)localObject1, (JCTree.JCStatement)localObject2);
/*      */     case DO:
/* 2485 */       nextToken();
/* 2486 */       localObject1 = parseStatementAsBlock();
/* 2487 */       accept(Tokens.TokenKind.WHILE);
/* 2488 */       localObject2 = parExpression();
/* 2489 */       localObject3 = (JCTree.JCDoWhileLoop)to(this.F.at(i).DoLoop((JCTree.JCStatement)localObject1, (JCTree.JCExpression)localObject2));
/* 2490 */       accept(Tokens.TokenKind.SEMI);
/* 2491 */       return localObject3;
/*      */     case TRY:
/* 2494 */       nextToken();
/* 2495 */       localObject1 = List.nil();
/* 2496 */       if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 2497 */         checkTryWithResources();
/* 2498 */         nextToken();
/* 2499 */         localObject1 = resources();
/* 2500 */         accept(Tokens.TokenKind.RPAREN);
/*      */       }
/* 2502 */       localObject2 = block();
/* 2503 */       localObject3 = new ListBuffer();
/* 2504 */       localObject4 = null;
/* 2505 */       if ((this.token.kind == Tokens.TokenKind.CATCH) || (this.token.kind == Tokens.TokenKind.FINALLY)) {
/* 2506 */         while (this.token.kind == Tokens.TokenKind.CATCH) ((ListBuffer)localObject3).append(catchClause());
/* 2507 */         if (this.token.kind == Tokens.TokenKind.FINALLY) {
/* 2508 */           nextToken();
/* 2509 */           localObject4 = block();
/*      */         }
/*      */       }
/* 2512 */       else if (this.allowTWR) {
/* 2513 */         if (((List)localObject1).isEmpty())
/* 2514 */           error(i, "try.without.catch.finally.or.resource.decls", new Object[0]);
/*      */       } else {
/* 2516 */         error(i, "try.without.catch.or.finally", new Object[0]);
/*      */       }
/* 2518 */       return this.F.at(i).Try((List)localObject1, (JCTree.JCBlock)localObject2, ((ListBuffer)localObject3).toList(), (JCTree.JCBlock)localObject4);
/*      */     case SWITCH:
/* 2521 */       nextToken();
/* 2522 */       localObject1 = parExpression();
/* 2523 */       accept(Tokens.TokenKind.LBRACE);
/* 2524 */       localObject2 = switchBlockStatementGroups();
/* 2525 */       localObject3 = (JCTree.JCSwitch)to(this.F.at(i).Switch((JCTree.JCExpression)localObject1, (List)localObject2));
/* 2526 */       accept(Tokens.TokenKind.RBRACE);
/* 2527 */       return localObject3;
/*      */     case SYNCHRONIZED:
/* 2530 */       nextToken();
/* 2531 */       localObject1 = parExpression();
/* 2532 */       localObject2 = block();
/* 2533 */       return this.F.at(i).Synchronized((JCTree.JCExpression)localObject1, (JCTree.JCBlock)localObject2);
/*      */     case RETURN:
/* 2536 */       nextToken();
/* 2537 */       localObject1 = this.token.kind == Tokens.TokenKind.SEMI ? null : parseExpression();
/* 2538 */       localObject2 = (JCTree.JCReturn)to(this.F.at(i).Return((JCTree.JCExpression)localObject1));
/* 2539 */       accept(Tokens.TokenKind.SEMI);
/* 2540 */       return localObject2;
/*      */     case THROW:
/* 2543 */       nextToken();
/* 2544 */       localObject1 = parseExpression();
/* 2545 */       localObject2 = (JCTree.JCThrow)to(this.F.at(i).Throw((JCTree.JCExpression)localObject1));
/* 2546 */       accept(Tokens.TokenKind.SEMI);
/* 2547 */       return localObject2;
/*      */     case BREAK:
/* 2550 */       nextToken();
/* 2551 */       localObject1 = this.LAX_IDENTIFIER.accepts(this.token.kind) ? ident() : null;
/* 2552 */       localObject2 = (JCTree.JCBreak)to(this.F.at(i).Break((Name)localObject1));
/* 2553 */       accept(Tokens.TokenKind.SEMI);
/* 2554 */       return localObject2;
/*      */     case CONTINUE:
/* 2557 */       nextToken();
/* 2558 */       localObject1 = this.LAX_IDENTIFIER.accepts(this.token.kind) ? ident() : null;
/* 2559 */       localObject2 = (JCTree.JCContinue)to(this.F.at(i).Continue((Name)localObject1));
/* 2560 */       accept(Tokens.TokenKind.SEMI);
/* 2561 */       return localObject2;
/*      */     case SEMI:
/* 2564 */       nextToken();
/* 2565 */       return (JCTree.JCStatement)toP(this.F.at(i).Skip());
/*      */     case ELSE:
/* 2567 */       int j = this.token.pos;
/* 2568 */       nextToken();
/* 2569 */       return doRecover(j, BasicErrorRecoveryAction.BLOCK_STMT, "else.without.if");
/*      */     case FINALLY:
/* 2571 */       int k = this.token.pos;
/* 2572 */       nextToken();
/* 2573 */       return doRecover(k, BasicErrorRecoveryAction.BLOCK_STMT, "finally.without.try");
/*      */     case CATCH:
/* 2575 */       return doRecover(this.token.pos, BasicErrorRecoveryAction.CATCH_CLAUSE, "catch.without.try");
/*      */     case ASSERT:
/* 2577 */       if ((this.allowAsserts) && (this.token.kind == Tokens.TokenKind.ASSERT)) {
/* 2578 */         nextToken();
/* 2579 */         localObject3 = parseExpression();
/* 2580 */         localObject4 = null;
/* 2581 */         if (this.token.kind == Tokens.TokenKind.COLON) {
/* 2582 */           nextToken();
/* 2583 */           localObject4 = parseExpression();
/*      */         }
/* 2585 */         localObject5 = (JCTree.JCAssert)to(this.F.at(i).Assert((JCTree.JCExpression)localObject3, (JCTree.JCExpression)localObject4));
/* 2586 */         accept(Tokens.TokenKind.SEMI);
/* 2587 */         return localObject5;
/*      */       }
/*      */       break;
/*      */     case ENUM:
/*      */     }
/*      */ 
/* 2593 */     Object localObject3 = this.token;
/* 2594 */     Object localObject4 = parseExpression();
/* 2595 */     if ((this.token.kind == Tokens.TokenKind.COLON) && (((JCTree.JCExpression)localObject4).hasTag(JCTree.Tag.IDENT))) {
/* 2596 */       nextToken();
/* 2597 */       localObject5 = parseStatement();
/* 2598 */       return this.F.at(i).Labelled(((Tokens.Token)localObject3).name(), (JCTree.JCStatement)localObject5);
/*      */     }
/*      */ 
/* 2601 */     Object localObject5 = (JCTree.JCExpressionStatement)to(this.F.at(i).Exec(checkExprStat((JCTree.JCExpression)localObject4)));
/* 2602 */     accept(Tokens.TokenKind.SEMI);
/* 2603 */     return localObject5;
/*      */   }
/*      */ 
/*      */   private JCTree.JCStatement doRecover(int paramInt, ErrorRecoveryAction paramErrorRecoveryAction, String paramString)
/*      */   {
/* 2609 */     int i = this.S.errPos();
/* 2610 */     JCTree localJCTree = paramErrorRecoveryAction.doRecover(this);
/* 2611 */     this.S.errPos(i);
/* 2612 */     return (JCTree.JCStatement)toP(this.F.Exec(syntaxError(paramInt, List.of(localJCTree), paramString, new Tokens.TokenKind[0])));
/*      */   }
/*      */ 
/*      */   protected JCTree.JCCatch catchClause()
/*      */   {
/* 2619 */     int i = this.token.pos;
/* 2620 */     accept(Tokens.TokenKind.CATCH);
/* 2621 */     accept(Tokens.TokenKind.LPAREN);
/* 2622 */     JCTree.JCModifiers localJCModifiers = optFinal(8589934592L);
/* 2623 */     List localList = catchTypes();
/*      */ 
/* 2625 */     JCTree.JCExpression localJCExpression = localList.size() > 1 ? 
/* 2625 */       (JCTree.JCExpression)toP(this.F
/* 2625 */       .at(((JCTree.JCExpression)localList.head)
/* 2625 */       .getStartPosition()).TypeUnion(localList)) : (JCTree.JCExpression)localList.head;
/*      */ 
/* 2627 */     JCTree.JCVariableDecl localJCVariableDecl = variableDeclaratorId(localJCModifiers, localJCExpression);
/* 2628 */     accept(Tokens.TokenKind.RPAREN);
/* 2629 */     JCTree.JCBlock localJCBlock = block();
/* 2630 */     return this.F.at(i).Catch(localJCVariableDecl, localJCBlock);
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> catchTypes() {
/* 2634 */     ListBuffer localListBuffer = new ListBuffer();
/* 2635 */     localListBuffer.add(parseType());
/* 2636 */     while (this.token.kind == Tokens.TokenKind.BAR) {
/* 2637 */       checkMulticatch();
/* 2638 */       nextToken();
/*      */ 
/* 2641 */       localListBuffer.add(parseType());
/*      */     }
/* 2643 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCCase> switchBlockStatementGroups()
/*      */   {
/* 2651 */     ListBuffer localListBuffer = new ListBuffer();
/*      */     while (true) {
/* 2653 */       int i = this.token.pos;
/* 2654 */       switch (this.token.kind) {
/*      */       case CASE:
/*      */       case DEFAULT:
/* 2657 */         localListBuffer.append(switchBlockStatementGroup());
/* 2658 */         break;
/*      */       case EOF:
/*      */       case RBRACE:
/* 2660 */         return localListBuffer.toList();
/*      */       default:
/* 2662 */         nextToken();
/* 2663 */         syntaxError(i, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.CASE, Tokens.TokenKind.DEFAULT, Tokens.TokenKind.RBRACE });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected JCTree.JCCase switchBlockStatementGroup()
/*      */   {
/* 2670 */     int i = this.token.pos;
/*      */     List localList;
/*      */     JCTree.JCCase localJCCase;
/* 2673 */     switch (this.token.kind) {
/*      */     case CASE:
/* 2675 */       nextToken();
/* 2676 */       JCTree.JCExpression localJCExpression = parseExpression();
/* 2677 */       accept(Tokens.TokenKind.COLON);
/* 2678 */       localList = blockStatements();
/* 2679 */       localJCCase = this.F.at(i).Case(localJCExpression, localList);
/* 2680 */       if (localList.isEmpty())
/* 2681 */         storeEnd(localJCCase, this.S.prevToken().endPos);
/* 2682 */       return localJCCase;
/*      */     case DEFAULT:
/* 2684 */       nextToken();
/* 2685 */       accept(Tokens.TokenKind.COLON);
/* 2686 */       localList = blockStatements();
/* 2687 */       localJCCase = this.F.at(i).Case(null, localList);
/* 2688 */       if (localList.isEmpty())
/* 2689 */         storeEnd(localJCCase, this.S.prevToken().endPos);
/* 2690 */       return localJCCase;
/*      */     }
/* 2692 */     throw new AssertionError("should not reach here");
/*      */   }
/*      */ 
/*      */   <T extends ListBuffer<? super JCTree.JCExpressionStatement>> T moreStatementExpressions(int paramInt, JCTree.JCExpression paramJCExpression, T paramT)
/*      */   {
/* 2701 */     paramT.append(toP(this.F.at(paramInt).Exec(checkExprStat(paramJCExpression))));
/* 2702 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2703 */       nextToken();
/* 2704 */       paramInt = this.token.pos;
/* 2705 */       JCTree.JCExpression localJCExpression = parseExpression();
/*      */ 
/* 2707 */       paramT.append(toP(this.F.at(paramInt).Exec(checkExprStat(localJCExpression))));
/*      */     }
/* 2709 */     return paramT;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCStatement> forInit()
/*      */   {
/* 2716 */     ListBuffer localListBuffer = new ListBuffer();
/* 2717 */     int i = this.token.pos;
/* 2718 */     if ((this.token.kind == Tokens.TokenKind.FINAL) || (this.token.kind == Tokens.TokenKind.MONKEYS_AT)) {
/* 2719 */       return variableDeclarators(optFinal(0L), parseType(), localListBuffer).toList();
/*      */     }
/* 2721 */     JCTree.JCExpression localJCExpression = term(3);
/* 2722 */     if (((this.lastmode & 0x2) != 0) && (this.LAX_IDENTIFIER.accepts(this.token.kind)))
/* 2723 */       return variableDeclarators(mods(i, 0L, List.nil()), localJCExpression, localListBuffer).toList();
/* 2724 */     if (((this.lastmode & 0x2) != 0) && (this.token.kind == Tokens.TokenKind.COLON)) {
/* 2725 */       error(i, "bad.initializer", new Object[] { "for-loop" });
/* 2726 */       return List.of(this.F.at(i).VarDef(null, null, localJCExpression, null));
/*      */     }
/* 2728 */     return moreStatementExpressions(i, localJCExpression, localListBuffer).toList();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpressionStatement> forUpdate()
/*      */   {
/* 2738 */     return moreStatementExpressions(this.token.pos, 
/* 2737 */       parseExpression(), new ListBuffer())
/* 2738 */       .toList();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCAnnotation> annotationsOpt(JCTree.Tag paramTag)
/*      */   {
/* 2746 */     if (this.token.kind != Tokens.TokenKind.MONKEYS_AT) return List.nil();
/* 2747 */     ListBuffer localListBuffer = new ListBuffer();
/* 2748 */     int i = this.mode;
/* 2749 */     while (this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/* 2750 */       int j = this.token.pos;
/* 2751 */       nextToken();
/* 2752 */       localListBuffer.append(annotation(j, paramTag));
/*      */     }
/* 2754 */     this.lastmode = this.mode;
/* 2755 */     this.mode = i;
/* 2756 */     List localList = localListBuffer.toList();
/*      */ 
/* 2758 */     return localList;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCAnnotation> typeAnnotationsOpt() {
/* 2762 */     List localList = annotationsOpt(JCTree.Tag.TYPE_ANNOTATION);
/* 2763 */     return localList;
/*      */   }
/*      */ 
/*      */   JCTree.JCModifiers modifiersOpt()
/*      */   {
/* 2772 */     return modifiersOpt(null);
/*      */   }
/*      */ 
/*      */   protected JCTree.JCModifiers modifiersOpt(JCTree.JCModifiers paramJCModifiers) {
/* 2776 */     ListBuffer localListBuffer = new ListBuffer();
/*      */     long l1;
/*      */     int i;
/* 2778 */     if (paramJCModifiers == null) {
/* 2779 */       l1 = 0L;
/* 2780 */       i = this.token.pos;
/*      */     } else {
/* 2782 */       l1 = paramJCModifiers.flags;
/* 2783 */       localListBuffer.appendList(paramJCModifiers.annotations);
/* 2784 */       i = paramJCModifiers.pos;
/*      */     }
/* 2786 */     if (this.token.deprecatedFlag())
/* 2787 */       l1 |= 131072L;
/*      */     while (true)
/*      */     {
/*      */       long l2;
/* 2793 */       switch (this.token.kind) { case PRIVATE:
/* 2794 */         l2 = 2L; break;
/*      */       case PROTECTED:
/* 2795 */         l2 = 4L; break;
/*      */       case PUBLIC:
/* 2796 */         l2 = 1L; break;
/*      */       case STATIC:
/* 2797 */         l2 = 8L; break;
/*      */       case TRANSIENT:
/* 2798 */         l2 = 128L; break;
/*      */       case FINAL:
/* 2799 */         l2 = 16L; break;
/*      */       case ABSTRACT:
/* 2800 */         l2 = 1024L; break;
/*      */       case NATIVE:
/* 2801 */         l2 = 256L; break;
/*      */       case VOLATILE:
/* 2802 */         l2 = 64L; break;
/*      */       case SYNCHRONIZED:
/* 2803 */         l2 = 32L; break;
/*      */       case STRICTFP:
/* 2804 */         l2 = 2048L; break;
/*      */       case MONKEYS_AT:
/* 2805 */         l2 = 8192L; break;
/*      */       case DEFAULT:
/* 2806 */         checkDefaultMethods(); l2 = 8796093022208L; break;
/*      */       case ERROR:
/* 2807 */         l2 = 0L; nextToken(); break;
/*      */       default:
/* 2808 */         break;
/*      */       }
/* 2810 */       if ((l1 & l2) != 0L) error(this.token.pos, "repeated.modifier", new Object[0]);
/* 2811 */       int j = this.token.pos;
/* 2812 */       nextToken();
/* 2813 */       if (l2 == 8192L) {
/* 2814 */         checkAnnotations();
/* 2815 */         if (this.token.kind != Tokens.TokenKind.INTERFACE) {
/* 2816 */           JCTree.JCAnnotation localJCAnnotation = annotation(j, JCTree.Tag.ANNOTATION);
/*      */ 
/* 2818 */           if ((l1 == 0L) && (localListBuffer.isEmpty()))
/* 2819 */             i = localJCAnnotation.pos;
/* 2820 */           localListBuffer.append(localJCAnnotation);
/* 2821 */           l2 = 0L;
/*      */         }
/*      */       }
/* 2824 */       l1 |= l2;
/*      */     }
/* 2826 */     switch (this.token.kind) { case ENUM:
/* 2827 */       l1 |= 16384L; break;
/*      */     case INTERFACE:
/* 2828 */       l1 |= 512L; break;
/*      */     }
/*      */ 
/* 2832 */     return mods(i, l1, localListBuffer.toList());
/*      */   }
/*      */ 
/*      */   JCTree.JCModifiers mods(int paramInt, long paramLong, List<JCTree.JCAnnotation> paramList)
/*      */   {
/* 2838 */     if (((paramLong & 0x2DFF) == 0L) && (paramList.isEmpty())) {
/* 2839 */       paramInt = -1;
/*      */     }
/* 2841 */     JCTree.JCModifiers localJCModifiers = this.F.at(paramInt).Modifiers(paramLong, paramList);
/* 2842 */     if (paramInt != -1)
/* 2843 */       storeEnd(localJCModifiers, this.S.prevToken().endPos);
/* 2844 */     return localJCModifiers;
/*      */   }
/*      */ 
/*      */   JCTree.JCAnnotation annotation(int paramInt, JCTree.Tag paramTag)
/*      */   {
/* 2854 */     checkAnnotations();
/* 2855 */     if (paramTag == JCTree.Tag.TYPE_ANNOTATION) {
/* 2856 */       checkTypeAnnotations();
/*      */     }
/* 2858 */     JCTree.JCExpression localJCExpression = qualident(false);
/* 2859 */     List localList = annotationFieldValuesOpt();
/*      */     JCTree.JCAnnotation localJCAnnotation;
/* 2861 */     if (paramTag == JCTree.Tag.ANNOTATION)
/* 2862 */       localJCAnnotation = this.F.at(paramInt).Annotation(localJCExpression, localList);
/* 2863 */     else if (paramTag == JCTree.Tag.TYPE_ANNOTATION)
/* 2864 */       localJCAnnotation = this.F.at(paramInt).TypeAnnotation(localJCExpression, localList);
/*      */     else {
/* 2866 */       throw new AssertionError("Unhandled annotation kind: " + paramTag);
/*      */     }
/*      */ 
/* 2869 */     storeEnd(localJCAnnotation, this.S.prevToken().endPos);
/* 2870 */     return localJCAnnotation;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> annotationFieldValuesOpt() {
/* 2874 */     return this.token.kind == Tokens.TokenKind.LPAREN ? annotationFieldValues() : List.nil();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> annotationFieldValues()
/*      */   {
/* 2879 */     accept(Tokens.TokenKind.LPAREN);
/* 2880 */     ListBuffer localListBuffer = new ListBuffer();
/* 2881 */     if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 2882 */       localListBuffer.append(annotationFieldValue());
/* 2883 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2884 */         nextToken();
/* 2885 */         localListBuffer.append(annotationFieldValue());
/*      */       }
/*      */     }
/* 2888 */     accept(Tokens.TokenKind.RPAREN);
/* 2889 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression annotationFieldValue()
/*      */   {
/* 2896 */     if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 2897 */       this.mode = 1;
/* 2898 */       JCTree.JCExpression localJCExpression1 = term1();
/* 2899 */       if ((localJCExpression1.hasTag(JCTree.Tag.IDENT)) && (this.token.kind == Tokens.TokenKind.EQ)) {
/* 2900 */         int i = this.token.pos;
/* 2901 */         accept(Tokens.TokenKind.EQ);
/* 2902 */         JCTree.JCExpression localJCExpression2 = annotationValue();
/* 2903 */         return (JCTree.JCExpression)toP(this.F.at(i).Assign(localJCExpression1, localJCExpression2));
/*      */       }
/* 2905 */       return localJCExpression1;
/*      */     }
/*      */ 
/* 2908 */     return annotationValue();
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression annotationValue()
/*      */   {
/*      */     int i;
/* 2917 */     switch (this.token.kind) {
/*      */     case MONKEYS_AT:
/* 2919 */       i = this.token.pos;
/* 2920 */       nextToken();
/* 2921 */       return annotation(i, JCTree.Tag.ANNOTATION);
/*      */     case LBRACE:
/* 2923 */       i = this.token.pos;
/* 2924 */       accept(Tokens.TokenKind.LBRACE);
/* 2925 */       ListBuffer localListBuffer = new ListBuffer();
/* 2926 */       if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2927 */         nextToken();
/* 2928 */       } else if (this.token.kind != Tokens.TokenKind.RBRACE) {
/* 2929 */         localListBuffer.append(annotationValue());
/* 2930 */         while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2931 */           nextToken();
/* 2932 */           if (this.token.kind == Tokens.TokenKind.RBRACE) break;
/* 2933 */           localListBuffer.append(annotationValue());
/*      */         }
/*      */       }
/* 2936 */       accept(Tokens.TokenKind.RBRACE);
/* 2937 */       return (JCTree.JCExpression)toP(this.F.at(i).NewArray(null, List.nil(), localListBuffer.toList()));
/*      */     }
/* 2939 */     this.mode = 1;
/* 2940 */     return term1();
/*      */   }
/*      */ 
/*      */   public <T extends ListBuffer<? super JCTree.JCVariableDecl>> T variableDeclarators(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, T paramT)
/*      */   {
/* 2950 */     return variableDeclaratorsRest(this.token.pos, paramJCModifiers, paramJCExpression, ident(), false, null, paramT);
/*      */   }
/*      */ 
/*      */   <T extends ListBuffer<? super JCTree.JCVariableDecl>> T variableDeclaratorsRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, boolean paramBoolean, Tokens.Comment paramComment, T paramT)
/*      */   {
/* 2967 */     paramT.append(variableDeclaratorRest(paramInt, paramJCModifiers, paramJCExpression, paramName, paramBoolean, paramComment));
/* 2968 */     while (this.token.kind == Tokens.TokenKind.COMMA)
/*      */     {
/* 2970 */       storeEnd((JCTree)paramT.last(), this.token.endPos);
/* 2971 */       nextToken();
/* 2972 */       paramT.append(variableDeclarator(paramJCModifiers, paramJCExpression, paramBoolean, paramComment));
/*      */     }
/* 2974 */     return paramT;
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl variableDeclarator(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, boolean paramBoolean, Tokens.Comment paramComment)
/*      */   {
/* 2981 */     return variableDeclaratorRest(this.token.pos, paramJCModifiers, paramJCExpression, ident(), paramBoolean, paramComment);
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl variableDeclaratorRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, boolean paramBoolean, Tokens.Comment paramComment)
/*      */   {
/* 2992 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 2993 */     JCTree.JCExpression localJCExpression = null;
/* 2994 */     if (this.token.kind == Tokens.TokenKind.EQ) {
/* 2995 */       nextToken();
/* 2996 */       localJCExpression = variableInitializer();
/*      */     }
/* 2998 */     else if (paramBoolean) { syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.EQ }); }
/*      */ 
/* 3000 */     JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)toP(this.F
/* 3000 */       .at(paramInt)
/* 3000 */       .VarDef(paramJCModifiers, paramName, paramJCExpression, localJCExpression));
/* 3001 */     attach(localJCVariableDecl, paramComment);
/* 3002 */     return localJCVariableDecl;
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl variableDeclaratorId(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 3008 */     return variableDeclaratorId(paramJCModifiers, paramJCExpression, false);
/*      */   }
/*      */ 
/*      */   JCTree.JCVariableDecl variableDeclaratorId(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, boolean paramBoolean) {
/* 3012 */     int i = this.token.pos;
/*      */     Name localName;
/* 3014 */     if ((paramBoolean) && (this.token.kind == Tokens.TokenKind.UNDERSCORE)) {
/* 3015 */       this.log.error(i, "underscore.as.identifier.in.lambda", new Object[0]);
/* 3016 */       localName = this.token.name();
/* 3017 */       nextToken();
/*      */     }
/* 3019 */     else if (this.allowThisIdent) {
/* 3020 */       JCTree.JCExpression localJCExpression = qualident(false);
/* 3021 */       if ((localJCExpression.hasTag(JCTree.Tag.IDENT)) && (((JCTree.JCIdent)localJCExpression).name != this.names._this)) {
/* 3022 */         localName = ((JCTree.JCIdent)localJCExpression).name;
/*      */       } else {
/* 3024 */         if ((paramJCModifiers.flags & 0x0) != 0L) {
/* 3025 */           this.log.error(this.token.pos, "varargs.and.receiver", new Object[0]);
/*      */         }
/* 3027 */         if (this.token.kind == Tokens.TokenKind.LBRACKET) {
/* 3028 */           this.log.error(this.token.pos, "array.and.receiver", new Object[0]);
/*      */         }
/* 3030 */         return (JCTree.JCVariableDecl)toP(this.F.at(i).ReceiverVarDef(paramJCModifiers, localJCExpression, paramJCExpression));
/*      */       }
/*      */     } else {
/* 3033 */       localName = ident();
/*      */     }
/*      */ 
/* 3036 */     if (((paramJCModifiers.flags & 0x0) != 0L) && (this.token.kind == Tokens.TokenKind.LBRACKET))
/*      */     {
/* 3038 */       this.log.error(this.token.pos, "varargs.and.old.array.syntax", new Object[0]);
/*      */     }
/* 3040 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 3041 */     return (JCTree.JCVariableDecl)toP(this.F.at(i).VarDef(paramJCModifiers, localName, paramJCExpression, null));
/*      */   }
/*      */ 
/*      */   List<JCTree> resources()
/*      */   {
/* 3047 */     ListBuffer localListBuffer = new ListBuffer();
/* 3048 */     localListBuffer.append(resource());
/* 3049 */     while (this.token.kind == Tokens.TokenKind.SEMI)
/*      */     {
/* 3051 */       storeEnd((JCTree)localListBuffer.last(), this.token.endPos);
/* 3052 */       int i = this.token.pos;
/* 3053 */       nextToken();
/* 3054 */       if (this.token.kind == Tokens.TokenKind.RPAREN)
/*      */       {
/*      */         break;
/*      */       }
/* 3058 */       localListBuffer.append(resource());
/*      */     }
/* 3060 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected JCTree resource()
/*      */   {
/* 3066 */     JCTree.JCModifiers localJCModifiers = optFinal(16L);
/* 3067 */     JCTree.JCExpression localJCExpression = parseType();
/* 3068 */     int i = this.token.pos;
/* 3069 */     Name localName = ident();
/* 3070 */     return variableDeclaratorRest(i, localJCModifiers, localJCExpression, localName, true, null);
/*      */   }
/*      */ 
/*      */   public JCTree.JCCompilationUnit parseCompilationUnit()
/*      */   {
/* 3076 */     Tokens.Token localToken = this.token;
/* 3077 */     JCTree.JCExpression localJCExpression = null;
/* 3078 */     JCTree.JCModifiers localJCModifiers = null;
/* 3079 */     int i = 0;
/* 3080 */     int j = 0;
/* 3081 */     int k = 0;
/* 3082 */     List localList = List.nil();
/* 3083 */     if (this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/* 3084 */       localJCModifiers = modifiersOpt();
/*      */     }
/* 3086 */     if (this.token.kind == Tokens.TokenKind.PACKAGE) {
/* 3087 */       k = 1;
/* 3088 */       if (localJCModifiers != null) {
/* 3089 */         checkNoMods(localJCModifiers.flags);
/* 3090 */         localList = localJCModifiers.annotations;
/* 3091 */         localJCModifiers = null;
/*      */       }
/* 3093 */       nextToken();
/* 3094 */       localJCExpression = qualident(false);
/* 3095 */       accept(Tokens.TokenKind.SEMI);
/*      */     }
/* 3097 */     ListBuffer localListBuffer = new ListBuffer();
/* 3098 */     boolean bool = true;
/* 3099 */     int m = 1;
/* 3100 */     while (this.token.kind != Tokens.TokenKind.EOF) {
/* 3101 */       if ((this.token.pos > 0) && (this.token.pos <= this.endPosTable.errorEndPos))
/*      */       {
/* 3103 */         skip(bool, false, false, false);
/* 3104 */         if (this.token.kind == Tokens.TokenKind.EOF)
/*      */           break;
/*      */       }
/* 3107 */       if ((bool) && (localJCModifiers == null) && (this.token.kind == Tokens.TokenKind.IMPORT)) {
/* 3108 */         j = 1;
/* 3109 */         localListBuffer.append(importDeclaration());
/*      */       } else {
/* 3111 */         localObject1 = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3112 */         if ((m != 0) && (j == 0) && (k == 0)) {
/* 3113 */           localObject1 = localToken.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3114 */           i = 1;
/*      */         }
/* 3116 */         Object localObject2 = typeDeclaration(localJCModifiers, (Tokens.Comment)localObject1);
/* 3117 */         if ((localObject2 instanceof JCTree.JCExpressionStatement))
/* 3118 */           localObject2 = ((JCTree.JCExpressionStatement)localObject2).expr;
/* 3119 */         localListBuffer.append(localObject2);
/* 3120 */         if ((localObject2 instanceof JCTree.JCClassDecl))
/* 3121 */           bool = false;
/* 3122 */         localJCModifiers = null;
/* 3123 */         m = 0;
/*      */       }
/*      */     }
/* 3126 */     Object localObject1 = this.F.at(localToken.pos).TopLevel(localList, localJCExpression, localListBuffer.toList());
/* 3127 */     if (i == 0)
/* 3128 */       attach((JCTree)localObject1, localToken.comment(Tokens.Comment.CommentStyle.JAVADOC));
/* 3129 */     if (localListBuffer.isEmpty())
/* 3130 */       storeEnd((JCTree)localObject1, this.S.prevToken().endPos);
/* 3131 */     if (this.keepDocComments)
/* 3132 */       ((JCTree.JCCompilationUnit)localObject1).docComments = this.docComments;
/* 3133 */     if (this.keepLineMap)
/* 3134 */       ((JCTree.JCCompilationUnit)localObject1).lineMap = this.S.getLineMap();
/* 3135 */     this.endPosTable.setParser(null);
/* 3136 */     ((JCTree.JCCompilationUnit)localObject1).endPositions = this.endPosTable;
/* 3137 */     return localObject1;
/*      */   }
/*      */ 
/*      */   JCTree importDeclaration()
/*      */   {
/* 3143 */     int i = this.token.pos;
/* 3144 */     nextToken();
/* 3145 */     boolean bool = false;
/* 3146 */     if (this.token.kind == Tokens.TokenKind.STATIC) {
/* 3147 */       checkStaticImports();
/* 3148 */       bool = true;
/* 3149 */       nextToken();
/*      */     }
/* 3151 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*      */     do {
/* 3153 */       int j = this.token.pos;
/* 3154 */       accept(Tokens.TokenKind.DOT);
/* 3155 */       if (this.token.kind == Tokens.TokenKind.STAR) {
/* 3156 */         localJCExpression = (JCTree.JCExpression)to(this.F.at(j).Select(localJCExpression, this.names.asterisk));
/* 3157 */         nextToken();
/* 3158 */         break;
/*      */       }
/* 3160 */       localJCExpression = (JCTree.JCExpression)toP(this.F.at(j).Select(localJCExpression, ident()));
/*      */     }
/* 3162 */     while (this.token.kind == Tokens.TokenKind.DOT);
/* 3163 */     accept(Tokens.TokenKind.SEMI);
/* 3164 */     return toP(this.F.at(i).Import(localJCExpression, bool));
/*      */   }
/*      */ 
/*      */   JCTree typeDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment)
/*      */   {
/* 3171 */     int i = this.token.pos;
/* 3172 */     if ((paramJCModifiers == null) && (this.token.kind == Tokens.TokenKind.SEMI)) {
/* 3173 */       nextToken();
/* 3174 */       return toP(this.F.at(i).Skip());
/*      */     }
/* 3176 */     return classOrInterfaceOrEnumDeclaration(modifiersOpt(paramJCModifiers), paramComment);
/*      */   }
/*      */ 
/*      */   JCTree.JCStatement classOrInterfaceOrEnumDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment)
/*      */   {
/* 3186 */     if (this.token.kind == Tokens.TokenKind.CLASS)
/* 3187 */       return classDeclaration(paramJCModifiers, paramComment);
/* 3188 */     if (this.token.kind == Tokens.TokenKind.INTERFACE)
/* 3189 */       return interfaceDeclaration(paramJCModifiers, paramComment);
/*      */     List localList;
/* 3190 */     if (this.allowEnums) {
/* 3191 */       if (this.token.kind == Tokens.TokenKind.ENUM) {
/* 3192 */         return enumDeclaration(paramJCModifiers, paramComment);
/*      */       }
/* 3194 */       i = this.token.pos;
/*      */ 
/* 3196 */       if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 3197 */         localList = List.of(paramJCModifiers, toP(this.F.at(i).Ident(ident())));
/* 3198 */         setErrorEndPos(this.token.pos);
/*      */       } else {
/* 3200 */         localList = List.of(paramJCModifiers);
/*      */       }
/* 3202 */       return (JCTree.JCStatement)toP(this.F.Exec(syntaxError(i, localList, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.CLASS, Tokens.TokenKind.INTERFACE, Tokens.TokenKind.ENUM })));
/*      */     }
/*      */ 
/* 3206 */     if (this.token.kind == Tokens.TokenKind.ENUM) {
/* 3207 */       error(this.token.pos, "enums.not.supported.in.source", new Object[] { this.source.name });
/* 3208 */       this.allowEnums = true;
/* 3209 */       return enumDeclaration(paramJCModifiers, paramComment);
/*      */     }
/* 3211 */     int i = this.token.pos;
/*      */ 
/* 3213 */     if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 3214 */       localList = List.of(paramJCModifiers, toP(this.F.at(i).Ident(ident())));
/* 3215 */       setErrorEndPos(this.token.pos);
/*      */     } else {
/* 3217 */       localList = List.of(paramJCModifiers);
/*      */     }
/* 3219 */     return (JCTree.JCStatement)toP(this.F.Exec(syntaxError(i, localList, "expected2", new Tokens.TokenKind[] { Tokens.TokenKind.CLASS, Tokens.TokenKind.INTERFACE })));
/*      */   }
/*      */ 
/*      */   protected JCTree.JCClassDecl classDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment)
/*      */   {
/* 3230 */     int i = this.token.pos;
/* 3231 */     accept(Tokens.TokenKind.CLASS);
/* 3232 */     Name localName = ident();
/*      */ 
/* 3234 */     List localList1 = typeParametersOpt();
/*      */ 
/* 3236 */     JCTree.JCExpression localJCExpression = null;
/* 3237 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3238 */       nextToken();
/* 3239 */       localJCExpression = parseType();
/*      */     }
/* 3241 */     List localList2 = List.nil();
/* 3242 */     if (this.token.kind == Tokens.TokenKind.IMPLEMENTS) {
/* 3243 */       nextToken();
/* 3244 */       localList2 = typeList();
/*      */     }
/* 3246 */     List localList3 = classOrInterfaceBody(localName, false);
/* 3247 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)toP(this.F.at(i).ClassDef(paramJCModifiers, localName, localList1, localJCExpression, localList2, localList3));
/*      */ 
/* 3249 */     attach(localJCClassDecl, paramComment);
/* 3250 */     return localJCClassDecl;
/*      */   }
/*      */ 
/*      */   protected JCTree.JCClassDecl interfaceDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment)
/*      */   {
/* 3259 */     int i = this.token.pos;
/* 3260 */     accept(Tokens.TokenKind.INTERFACE);
/* 3261 */     Name localName = ident();
/*      */ 
/* 3263 */     List localList1 = typeParametersOpt();
/*      */ 
/* 3265 */     List localList2 = List.nil();
/* 3266 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3267 */       nextToken();
/* 3268 */       localList2 = typeList();
/*      */     }
/* 3270 */     List localList3 = classOrInterfaceBody(localName, true);
/* 3271 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)toP(this.F.at(i).ClassDef(paramJCModifiers, localName, localList1, null, localList2, localList3));
/*      */ 
/* 3273 */     attach(localJCClassDecl, paramComment);
/* 3274 */     return localJCClassDecl;
/*      */   }
/*      */ 
/*      */   protected JCTree.JCClassDecl enumDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment)
/*      */   {
/* 3282 */     int i = this.token.pos;
/* 3283 */     accept(Tokens.TokenKind.ENUM);
/* 3284 */     Name localName = ident();
/*      */ 
/* 3286 */     List localList1 = List.nil();
/* 3287 */     if (this.token.kind == Tokens.TokenKind.IMPLEMENTS) {
/* 3288 */       nextToken();
/* 3289 */       localList1 = typeList();
/*      */     }
/*      */ 
/* 3292 */     List localList2 = enumBody(localName);
/* 3293 */     paramJCModifiers.flags |= 16384L;
/* 3294 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)toP(this.F.at(i)
/* 3295 */       .ClassDef(paramJCModifiers, localName, 
/* 3295 */       List.nil(), null, localList1, localList2));
/*      */ 
/* 3297 */     attach(localJCClassDecl, paramComment);
/* 3298 */     return localJCClassDecl;
/*      */   }
/*      */ 
/*      */   List<JCTree> enumBody(Name paramName)
/*      */   {
/* 3305 */     accept(Tokens.TokenKind.LBRACE);
/* 3306 */     ListBuffer localListBuffer = new ListBuffer();
/* 3307 */     if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3308 */       nextToken();
/* 3309 */     } else if ((this.token.kind != Tokens.TokenKind.RBRACE) && (this.token.kind != Tokens.TokenKind.SEMI)) {
/* 3310 */       localListBuffer.append(enumeratorDeclaration(paramName));
/* 3311 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3312 */         nextToken();
/* 3313 */         if ((this.token.kind == Tokens.TokenKind.RBRACE) || (this.token.kind == Tokens.TokenKind.SEMI)) break;
/* 3314 */         localListBuffer.append(enumeratorDeclaration(paramName));
/*      */       }
/* 3316 */       if ((this.token.kind != Tokens.TokenKind.SEMI) && (this.token.kind != Tokens.TokenKind.RBRACE)) {
/* 3317 */         localListBuffer.append(syntaxError(this.token.pos, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.COMMA, Tokens.TokenKind.RBRACE, Tokens.TokenKind.SEMI }));
/*      */ 
/* 3319 */         nextToken();
/*      */       }
/*      */     }
/* 3322 */     if (this.token.kind == Tokens.TokenKind.SEMI) {
/* 3323 */       nextToken();
/* 3324 */       while ((this.token.kind != Tokens.TokenKind.RBRACE) && (this.token.kind != Tokens.TokenKind.EOF)) {
/* 3325 */         localListBuffer.appendList(classOrInterfaceBodyDeclaration(paramName, false));
/*      */ 
/* 3327 */         if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */         {
/* 3329 */           skip(false, true, true, false);
/*      */         }
/*      */       }
/*      */     }
/* 3333 */     accept(Tokens.TokenKind.RBRACE);
/* 3334 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   JCTree enumeratorDeclaration(Name paramName)
/*      */   {
/* 3340 */     Tokens.Comment localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3341 */     int i = 16409;
/* 3342 */     if (this.token.deprecatedFlag()) {
/* 3343 */       i |= 131072;
/*      */     }
/* 3345 */     int j = this.token.pos;
/* 3346 */     List localList1 = annotationsOpt(JCTree.Tag.ANNOTATION);
/* 3347 */     JCTree.JCModifiers localJCModifiers = this.F.at(localList1.isEmpty() ? -1 : j).Modifiers(i, localList1);
/* 3348 */     List localList2 = typeArgumentsOpt();
/* 3349 */     int k = this.token.pos;
/* 3350 */     Name localName = ident();
/* 3351 */     int m = this.token.pos;
/*      */ 
/* 3353 */     List localList3 = this.token.kind == Tokens.TokenKind.LPAREN ? 
/* 3353 */       arguments() : List.nil();
/* 3354 */     JCTree.JCClassDecl localJCClassDecl = null;
/* 3355 */     if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3356 */       localObject1 = this.F.at(-1).Modifiers(16392L);
/* 3357 */       localObject2 = classOrInterfaceBody(this.names.empty, false);
/* 3358 */       localJCClassDecl = (JCTree.JCClassDecl)toP(this.F.at(k).AnonymousClassDef((JCTree.JCModifiers)localObject1, (List)localObject2));
/*      */     }
/* 3360 */     if ((localList3.isEmpty()) && (localJCClassDecl == null))
/* 3361 */       m = k;
/* 3362 */     Object localObject1 = this.F.at(k).Ident(paramName);
/* 3363 */     Object localObject2 = this.F.at(m).NewClass(null, localList2, (JCTree.JCExpression)localObject1, localList3, localJCClassDecl);
/* 3364 */     if (m != k)
/* 3365 */       storeEnd((JCTree)localObject2, this.S.prevToken().endPos);
/* 3366 */     localObject1 = this.F.at(k).Ident(paramName);
/* 3367 */     JCTree localJCTree = toP(this.F.at(j).VarDef(localJCModifiers, localName, (JCTree.JCExpression)localObject1, (JCTree.JCExpression)localObject2));
/* 3368 */     attach(localJCTree, localComment);
/* 3369 */     return localJCTree;
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> typeList()
/*      */   {
/* 3375 */     ListBuffer localListBuffer = new ListBuffer();
/* 3376 */     localListBuffer.append(parseType());
/* 3377 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3378 */       nextToken();
/* 3379 */       localListBuffer.append(parseType());
/*      */     }
/* 3381 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   List<JCTree> classOrInterfaceBody(Name paramName, boolean paramBoolean)
/*      */   {
/* 3388 */     accept(Tokens.TokenKind.LBRACE);
/* 3389 */     if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */     {
/* 3391 */       skip(false, true, false, false);
/* 3392 */       if (this.token.kind == Tokens.TokenKind.LBRACE)
/* 3393 */         nextToken();
/*      */     }
/* 3395 */     ListBuffer localListBuffer = new ListBuffer();
/* 3396 */     while ((this.token.kind != Tokens.TokenKind.RBRACE) && (this.token.kind != Tokens.TokenKind.EOF)) {
/* 3397 */       localListBuffer.appendList(classOrInterfaceBodyDeclaration(paramName, paramBoolean));
/* 3398 */       if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */       {
/* 3400 */         skip(false, true, true, false);
/*      */       }
/*      */     }
/* 3403 */     accept(Tokens.TokenKind.RBRACE);
/* 3404 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   protected List<JCTree> classOrInterfaceBodyDeclaration(Name paramName, boolean paramBoolean)
/*      */   {
/* 3437 */     if (this.token.kind == Tokens.TokenKind.SEMI) {
/* 3438 */       nextToken();
/* 3439 */       return List.nil();
/*      */     }
/* 3441 */     Tokens.Comment localComment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3442 */     int i = this.token.pos;
/* 3443 */     JCTree.JCModifiers localJCModifiers = modifiersOpt();
/* 3444 */     if ((this.token.kind == Tokens.TokenKind.CLASS) || (this.token.kind == Tokens.TokenKind.INTERFACE) || ((this.allowEnums) && (this.token.kind == Tokens.TokenKind.ENUM)))
/*      */     {
/* 3447 */       return List.of(classOrInterfaceOrEnumDeclaration(localJCModifiers, localComment));
/* 3448 */     }if ((this.token.kind == Tokens.TokenKind.LBRACE) && (!paramBoolean) && ((localJCModifiers.flags & 0xFFF & 0xFFFFFFF7) == 0L))
/*      */     {
/* 3450 */       if (localJCModifiers.annotations
/* 3450 */         .isEmpty())
/* 3451 */         return List.of(block(i, localJCModifiers.flags));
/*      */     }
/* 3453 */     i = this.token.pos;
/* 3454 */     List localList1 = typeParametersOpt();
/*      */ 
/* 3457 */     if ((localList1.nonEmpty()) && (localJCModifiers.pos == -1)) {
/* 3458 */       localJCModifiers.pos = i;
/* 3459 */       storeEnd(localJCModifiers, i);
/*      */     }
/* 3461 */     List localList2 = annotationsOpt(JCTree.Tag.ANNOTATION);
/*      */ 
/* 3463 */     if (localList2.nonEmpty()) {
/* 3464 */       checkAnnotationsAfterTypeParams(((JCTree.JCAnnotation)localList2.head).pos);
/* 3465 */       localJCModifiers.annotations = localJCModifiers.annotations.appendList(localList2);
/* 3466 */       if (localJCModifiers.pos == -1) {
/* 3467 */         localJCModifiers.pos = ((JCTree.JCAnnotation)localJCModifiers.annotations.head).pos;
/*      */       }
/*      */     }
/* 3470 */     Tokens.Token localToken = this.token;
/* 3471 */     i = this.token.pos;
/*      */ 
/* 3473 */     boolean bool = this.token.kind == Tokens.TokenKind.VOID;
/*      */     JCTree.JCExpression localJCExpression;
/* 3474 */     if (bool) {
/* 3475 */       localJCExpression = (JCTree.JCExpression)to(this.F.at(i).TypeIdent(TypeTag.VOID));
/* 3476 */       nextToken();
/*      */     }
/*      */     else {
/* 3479 */       localJCExpression = unannotatedType();
/*      */     }
/* 3481 */     if ((this.token.kind == Tokens.TokenKind.LPAREN) && (!paramBoolean) && (localJCExpression.hasTag(JCTree.Tag.IDENT))) {
/* 3482 */       if ((paramBoolean) || (localToken.name() != paramName))
/* 3483 */         error(i, "invalid.meth.decl.ret.type.req", new Object[0]);
/* 3484 */       else if (localList2.nonEmpty())
/* 3485 */         illegal(((JCTree.JCAnnotation)localList2.head).pos);
/* 3486 */       return List.of(methodDeclaratorRest(i, localJCModifiers, null, this.names.init, localList1, paramBoolean, true, localComment));
/*      */     }
/*      */ 
/* 3490 */     i = this.token.pos;
/* 3491 */     Name localName = ident();
/* 3492 */     if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 3493 */       return List.of(methodDeclaratorRest(i, localJCModifiers, localJCExpression, localName, localList1, paramBoolean, bool, localComment));
/*      */     }
/*      */ 
/* 3496 */     if ((!bool) && (localList1.isEmpty()))
/*      */     {
/* 3499 */       localList3 = variableDeclaratorsRest(i, localJCModifiers, localJCExpression, localName, paramBoolean, localComment, new ListBuffer())
/* 3499 */         .toList();
/* 3500 */       storeEnd((JCTree)localList3.last(), this.token.endPos);
/* 3501 */       accept(Tokens.TokenKind.SEMI);
/* 3502 */       return localList3;
/*      */     }
/* 3504 */     i = this.token.pos;
/*      */ 
/* 3506 */     List localList3 = bool ? 
/* 3506 */       List.of(toP(this.F
/* 3506 */       .at(i)
/* 3506 */       .MethodDef(localJCModifiers, localName, localJCExpression, localList1, 
/* 3507 */       List.nil(), List.nil(), null, null))) : null;
/*      */ 
/* 3509 */     return List.of(syntaxError(this.token.pos, localList3, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LPAREN }));
/*      */   }
/*      */ 
/*      */   protected JCTree methodDeclaratorRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, List<JCTree.JCTypeParameter> paramList, boolean paramBoolean1, boolean paramBoolean2, Tokens.Comment paramComment)
/*      */   {
/* 3530 */     if ((paramBoolean1) && ((paramJCModifiers.flags & 0x8) != 0L)) {
/* 3531 */       checkStaticInterfaceMethods();
/*      */     }
/* 3533 */     JCTree.JCVariableDecl localJCVariableDecl = this.receiverParam;
/*      */     try {
/* 3535 */       this.receiverParam = null;
/*      */ 
/* 3537 */       List localList1 = formalParameters();
/* 3538 */       if (!paramBoolean2) paramJCExpression = bracketsOpt(paramJCExpression);
/* 3539 */       List localList2 = List.nil();
/* 3540 */       if (this.token.kind == Tokens.TokenKind.THROWS) {
/* 3541 */         nextToken();
/* 3542 */         localList2 = qualidentList();
/*      */       }
/* 3544 */       JCTree.JCBlock localJCBlock = null;
/*      */       JCTree.JCExpression localJCExpression;
/* 3546 */       if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3547 */         localJCBlock = block();
/* 3548 */         localJCExpression = null;
/*      */       } else {
/* 3550 */         if (this.token.kind == Tokens.TokenKind.DEFAULT) {
/* 3551 */           accept(Tokens.TokenKind.DEFAULT);
/* 3552 */           localJCExpression = annotationValue();
/*      */         } else {
/* 3554 */           localJCExpression = null;
/*      */         }
/* 3556 */         accept(Tokens.TokenKind.SEMI);
/* 3557 */         if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */         {
/* 3559 */           skip(false, true, false, false);
/* 3560 */           if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3561 */             localJCBlock = block();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3567 */       JCTree.JCMethodDecl localJCMethodDecl1 = (JCTree.JCMethodDecl)toP(this.F
/* 3567 */         .at(paramInt)
/* 3567 */         .MethodDef(paramJCModifiers, paramName, paramJCExpression, paramList, this.receiverParam, localList1, localList2, localJCBlock, localJCExpression));
/*      */ 
/* 3570 */       attach(localJCMethodDecl1, paramComment);
/* 3571 */       return localJCMethodDecl1;
/*      */     } finally {
/* 3573 */       this.receiverParam = localJCVariableDecl;
/*      */     }
/*      */   }
/*      */ 
/*      */   List<JCTree.JCExpression> qualidentList()
/*      */   {
/* 3580 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 3582 */     List localList = typeAnnotationsOpt();
/* 3583 */     JCTree.JCExpression localJCExpression1 = qualident(true);
/*      */     JCTree.JCExpression localJCExpression2;
/* 3584 */     if (!localList.isEmpty()) {
/* 3585 */       localJCExpression2 = insertAnnotationsToMostInner(localJCExpression1, localList, false);
/* 3586 */       localListBuffer.append(localJCExpression2);
/*      */     } else {
/* 3588 */       localListBuffer.append(localJCExpression1);
/*      */     }
/* 3590 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3591 */       nextToken();
/*      */ 
/* 3593 */       localList = typeAnnotationsOpt();
/* 3594 */       localJCExpression1 = qualident(true);
/* 3595 */       if (!localList.isEmpty()) {
/* 3596 */         localJCExpression2 = insertAnnotationsToMostInner(localJCExpression1, localList, false);
/* 3597 */         localListBuffer.append(localJCExpression2);
/*      */       } else {
/* 3599 */         localListBuffer.append(localJCExpression1);
/*      */       }
/*      */     }
/* 3602 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCTypeParameter> typeParametersOpt()
/*      */   {
/* 3611 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 3612 */       checkGenerics();
/* 3613 */       ListBuffer localListBuffer = new ListBuffer();
/* 3614 */       nextToken();
/* 3615 */       localListBuffer.append(typeParameter());
/* 3616 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3617 */         nextToken();
/* 3618 */         localListBuffer.append(typeParameter());
/*      */       }
/* 3620 */       accept(Tokens.TokenKind.GT);
/* 3621 */       return localListBuffer.toList();
/*      */     }
/* 3623 */     return List.nil();
/*      */   }
/*      */ 
/*      */   JCTree.JCTypeParameter typeParameter()
/*      */   {
/* 3635 */     int i = this.token.pos;
/* 3636 */     List localList = typeAnnotationsOpt();
/* 3637 */     Name localName = ident();
/* 3638 */     ListBuffer localListBuffer = new ListBuffer();
/* 3639 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3640 */       nextToken();
/* 3641 */       localListBuffer.append(parseType());
/* 3642 */       while (this.token.kind == Tokens.TokenKind.AMP) {
/* 3643 */         nextToken();
/* 3644 */         localListBuffer.append(parseType());
/*      */       }
/*      */     }
/* 3647 */     return (JCTree.JCTypeParameter)toP(this.F.at(i).TypeParameter(localName, localListBuffer.toList(), localList));
/*      */   }
/*      */ 
/*      */   List<JCTree.JCVariableDecl> formalParameters()
/*      */   {
/* 3655 */     return formalParameters(false);
/*      */   }
/*      */   List<JCTree.JCVariableDecl> formalParameters(boolean paramBoolean) {
/* 3658 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 3660 */     accept(Tokens.TokenKind.LPAREN);
/* 3661 */     if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 3662 */       this.allowThisIdent = true;
/* 3663 */       JCTree.JCVariableDecl localJCVariableDecl = formalParameter(paramBoolean);
/* 3664 */       if (localJCVariableDecl.nameexpr != null)
/* 3665 */         this.receiverParam = localJCVariableDecl;
/*      */       else {
/* 3667 */         localListBuffer.append(localJCVariableDecl);
/*      */       }
/* 3669 */       this.allowThisIdent = false;
/* 3670 */       while (((localJCVariableDecl.mods.flags & 0x0) == 0L) && (this.token.kind == Tokens.TokenKind.COMMA)) {
/* 3671 */         nextToken();
/* 3672 */         localListBuffer.append(localJCVariableDecl = formalParameter(paramBoolean));
/*      */       }
/*      */     }
/* 3675 */     accept(Tokens.TokenKind.RPAREN);
/* 3676 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   List<JCTree.JCVariableDecl> implicitParameters(boolean paramBoolean) {
/* 3680 */     if (paramBoolean) {
/* 3681 */       accept(Tokens.TokenKind.LPAREN);
/*      */     }
/* 3683 */     ListBuffer localListBuffer = new ListBuffer();
/* 3684 */     if ((this.token.kind != Tokens.TokenKind.RPAREN) && (this.token.kind != Tokens.TokenKind.ARROW)) {
/* 3685 */       localListBuffer.append(implicitParameter());
/* 3686 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3687 */         nextToken();
/* 3688 */         localListBuffer.append(implicitParameter());
/*      */       }
/*      */     }
/* 3691 */     if (paramBoolean) {
/* 3692 */       accept(Tokens.TokenKind.RPAREN);
/*      */     }
/* 3694 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   JCTree.JCModifiers optFinal(long paramLong) {
/* 3698 */     JCTree.JCModifiers localJCModifiers = modifiersOpt();
/* 3699 */     checkNoMods(localJCModifiers.flags & 0xFFFDFFEF);
/* 3700 */     localJCModifiers.flags |= paramLong;
/* 3701 */     return localJCModifiers;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression insertAnnotationsToMostInner(JCTree.JCExpression paramJCExpression, List<JCTree.JCAnnotation> paramList, boolean paramBoolean)
/*      */   {
/* 3726 */     int i = getEndPos(paramJCExpression);
/* 3727 */     Object localObject1 = paramJCExpression;
/* 3728 */     JCTree.JCArrayTypeTree localJCArrayTypeTree = null;
/* 3729 */     while (TreeInfo.typeIn((JCTree.JCExpression)localObject1).hasTag(JCTree.Tag.TYPEARRAY)) {
/* 3730 */       localJCArrayTypeTree = (JCTree.JCArrayTypeTree)TreeInfo.typeIn((JCTree.JCExpression)localObject1);
/* 3731 */       localObject1 = localJCArrayTypeTree.elemtype;
/*      */     }
/*      */ 
/* 3734 */     if (paramBoolean) {
/* 3735 */       localObject1 = (JCTree.JCExpression)to(this.F.at(this.token.pos).TypeArray((JCTree.JCExpression)localObject1));
/*      */     }
/*      */ 
/* 3738 */     Object localObject2 = localObject1;
/* 3739 */     if (paramList.nonEmpty()) {
/* 3740 */       Object localObject3 = localObject1;
/*      */ 
/* 3742 */       while ((TreeInfo.typeIn((JCTree.JCExpression)localObject1).hasTag(JCTree.Tag.SELECT)) || 
/* 3743 */         (TreeInfo.typeIn((JCTree.JCExpression)localObject1)
/* 3743 */         .hasTag(JCTree.Tag.TYPEAPPLY))) {
/* 3744 */         while (TreeInfo.typeIn((JCTree.JCExpression)localObject1).hasTag(JCTree.Tag.SELECT)) {
/* 3745 */           localObject3 = localObject1;
/* 3746 */           localObject1 = ((JCTree.JCFieldAccess)TreeInfo.typeIn((JCTree.JCExpression)localObject1)).getExpression();
/*      */         }
/* 3748 */         while (TreeInfo.typeIn((JCTree.JCExpression)localObject1).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 3749 */           localObject3 = localObject1;
/* 3750 */           localObject1 = ((JCTree.JCTypeApply)TreeInfo.typeIn((JCTree.JCExpression)localObject1)).clazz;
/*      */         }
/*      */       }
/*      */ 
/* 3754 */       localObject1 = this.F.at(((JCTree.JCAnnotation)paramList.head).pos).AnnotatedType(paramList, (JCTree.JCExpression)localObject1);
/*      */ 
/* 3756 */       if (TreeInfo.typeIn(localObject3).hasTag(JCTree.Tag.TYPEAPPLY))
/* 3757 */         ((JCTree.JCTypeApply)TreeInfo.typeIn(localObject3)).clazz = ((JCTree.JCExpression)localObject1);
/* 3758 */       else if (TreeInfo.typeIn(localObject3).hasTag(JCTree.Tag.SELECT)) {
/* 3759 */         ((JCTree.JCFieldAccess)TreeInfo.typeIn(localObject3)).selected = ((JCTree.JCExpression)localObject1);
/*      */       }
/*      */       else {
/* 3762 */         localObject2 = localObject1;
/*      */       }
/*      */     }
/*      */ 
/* 3766 */     if (localJCArrayTypeTree == null) {
/* 3767 */       return localObject2;
/*      */     }
/* 3769 */     localJCArrayTypeTree.elemtype = localObject2;
/* 3770 */     storeEnd(paramJCExpression, i);
/* 3771 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   protected JCTree.JCVariableDecl formalParameter()
/*      */   {
/* 3779 */     return formalParameter(false);
/*      */   }
/*      */   protected JCTree.JCVariableDecl formalParameter(boolean paramBoolean) {
/* 3782 */     JCTree.JCModifiers localJCModifiers = optFinal(8589934592L);
/*      */ 
/* 3785 */     this.permitTypeAnnotationsPushBack = true;
/* 3786 */     JCTree.JCExpression localJCExpression = parseType();
/* 3787 */     this.permitTypeAnnotationsPushBack = false;
/*      */ 
/* 3789 */     if (this.token.kind == Tokens.TokenKind.ELLIPSIS) {
/* 3790 */       List localList = this.typeAnnotationsPushedBack;
/* 3791 */       this.typeAnnotationsPushedBack = List.nil();
/* 3792 */       checkVarargs();
/* 3793 */       localJCModifiers.flags |= 17179869184L;
/*      */ 
/* 3795 */       localJCExpression = insertAnnotationsToMostInner(localJCExpression, localList, true);
/* 3796 */       nextToken();
/*      */     }
/*      */     else {
/* 3799 */       if (this.typeAnnotationsPushedBack.nonEmpty()) {
/* 3800 */         reportSyntaxError(((JCTree.JCAnnotation)this.typeAnnotationsPushedBack.head).pos, "illegal.start.of.type", new Object[0]);
/*      */       }
/*      */ 
/* 3803 */       this.typeAnnotationsPushedBack = List.nil();
/*      */     }
/* 3805 */     return variableDeclaratorId(localJCModifiers, localJCExpression, paramBoolean);
/*      */   }
/*      */ 
/*      */   protected JCTree.JCVariableDecl implicitParameter() {
/* 3809 */     JCTree.JCModifiers localJCModifiers = this.F.at(this.token.pos).Modifiers(8589934592L);
/* 3810 */     return variableDeclaratorId(localJCModifiers, null, true);
/*      */   }
/*      */ 
/*      */   void error(int paramInt, String paramString, Object[] paramArrayOfObject)
/*      */   {
/* 3816 */     this.log.error(JCDiagnostic.DiagnosticFlag.SYNTAX, paramInt, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   void error(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject) {
/* 3820 */     this.log.error(JCDiagnostic.DiagnosticFlag.SYNTAX, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   void warning(int paramInt, String paramString, Object[] paramArrayOfObject) {
/* 3824 */     this.log.warning(paramInt, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   protected JCTree.JCExpression checkExprStat(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 3830 */     if (!TreeInfo.isExpressionStatement(paramJCExpression)) {
/* 3831 */       JCTree.JCErroneous localJCErroneous = this.F.at(paramJCExpression.pos).Erroneous(List.of(paramJCExpression));
/* 3832 */       error(localJCErroneous, "not.stmt", new Object[0]);
/* 3833 */       return localJCErroneous;
/*      */     }
/* 3835 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   static int prec(Tokens.TokenKind paramTokenKind)
/*      */   {
/* 3843 */     JCTree.Tag localTag = optag(paramTokenKind);
/* 3844 */     return localTag != JCTree.Tag.NO_TAG ? TreeInfo.opPrec(localTag) : -1;
/*      */   }
/*      */ 
/*      */   static int earlier(int paramInt1, int paramInt2)
/*      */   {
/* 3852 */     if (paramInt1 == -1)
/* 3853 */       return paramInt2;
/* 3854 */     if (paramInt2 == -1)
/* 3855 */       return paramInt1;
/* 3856 */     return paramInt1 < paramInt2 ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */   static JCTree.Tag optag(Tokens.TokenKind paramTokenKind)
/*      */   {
/* 3863 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[paramTokenKind.ordinal()]) {
/*      */     case 94:
/* 3865 */       return JCTree.Tag.OR;
/*      */     case 95:
/* 3867 */       return JCTree.Tag.AND;
/*      */     case 96:
/* 3869 */       return JCTree.Tag.BITOR;
/*      */     case 64:
/* 3871 */       return JCTree.Tag.BITOR_ASG;
/*      */     case 97:
/* 3873 */       return JCTree.Tag.BITXOR;
/*      */     case 65:
/* 3875 */       return JCTree.Tag.BITXOR_ASG;
/*      */     case 91:
/* 3877 */       return JCTree.Tag.BITAND;
/*      */     case 63:
/* 3879 */       return JCTree.Tag.BITAND_ASG;
/*      */     case 98:
/* 3881 */       return JCTree.Tag.EQ;
/*      */     case 99:
/* 3883 */       return JCTree.Tag.NE;
/*      */     case 21:
/* 3885 */       return JCTree.Tag.LT;
/*      */     case 90:
/* 3887 */       return JCTree.Tag.GT;
/*      */     case 100:
/* 3889 */       return JCTree.Tag.LE;
/*      */     case 92:
/* 3891 */       return JCTree.Tag.GE;
/*      */     case 101:
/* 3893 */       return JCTree.Tag.SL;
/*      */     case 66:
/* 3895 */       return JCTree.Tag.SL_ASG;
/*      */     case 89:
/* 3897 */       return JCTree.Tag.SR;
/*      */     case 67:
/* 3899 */       return JCTree.Tag.SR_ASG;
/*      */     case 88:
/* 3901 */       return JCTree.Tag.USR;
/*      */     case 68:
/* 3903 */       return JCTree.Tag.USR_ASG;
/*      */     case 81:
/* 3905 */       return JCTree.Tag.PLUS;
/*      */     case 58:
/* 3907 */       return JCTree.Tag.PLUS_ASG;
/*      */     case 82:
/* 3909 */       return JCTree.Tag.MINUS;
/*      */     case 59:
/* 3911 */       return JCTree.Tag.MINUS_ASG;
/*      */     case 102:
/* 3913 */       return JCTree.Tag.MUL;
/*      */     case 60:
/* 3915 */       return JCTree.Tag.MUL_ASG;
/*      */     case 103:
/* 3917 */       return JCTree.Tag.DIV;
/*      */     case 61:
/* 3919 */       return JCTree.Tag.DIV_ASG;
/*      */     case 104:
/* 3921 */       return JCTree.Tag.MOD;
/*      */     case 62:
/* 3923 */       return JCTree.Tag.MOD_ASG;
/*      */     case 105:
/* 3925 */       return JCTree.Tag.TYPETEST;
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
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/* 3927 */     case 93: } return JCTree.Tag.NO_TAG;
/*      */   }
/*      */ 
/*      */   static JCTree.Tag unoptag(Tokens.TokenKind paramTokenKind)
/*      */   {
/* 3935 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[paramTokenKind.ordinal()]) {
/*      */     case 81:
/* 3937 */       return JCTree.Tag.POS;
/*      */     case 82:
/* 3939 */       return JCTree.Tag.NEG;
/*      */     case 79:
/* 3941 */       return JCTree.Tag.NOT;
/*      */     case 80:
/* 3943 */       return JCTree.Tag.COMPL;
/*      */     case 77:
/* 3945 */       return JCTree.Tag.PREINC;
/*      */     case 78:
/* 3947 */       return JCTree.Tag.PREDEC;
/*      */     }
/* 3949 */     return JCTree.Tag.NO_TAG;
/*      */   }
/*      */ 
/*      */   static TypeTag typetag(Tokens.TokenKind paramTokenKind)
/*      */   {
/* 3957 */     switch (2.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[paramTokenKind.ordinal()]) {
/*      */     case 22:
/* 3959 */       return TypeTag.BYTE;
/*      */     case 24:
/* 3961 */       return TypeTag.CHAR;
/*      */     case 23:
/* 3963 */       return TypeTag.SHORT;
/*      */     case 25:
/* 3965 */       return TypeTag.INT;
/*      */     case 26:
/* 3967 */       return TypeTag.LONG;
/*      */     case 27:
/* 3969 */       return TypeTag.FLOAT;
/*      */     case 28:
/* 3971 */       return TypeTag.DOUBLE;
/*      */     case 29:
/* 3973 */       return TypeTag.BOOLEAN;
/*      */     }
/* 3975 */     return TypeTag.NONE;
/*      */   }
/*      */ 
/*      */   void checkGenerics()
/*      */   {
/* 3980 */     if (!this.allowGenerics) {
/* 3981 */       error(this.token.pos, "generics.not.supported.in.source", new Object[] { this.source.name });
/* 3982 */       this.allowGenerics = true;
/*      */     }
/*      */   }
/*      */ 
/* 3986 */   void checkVarargs() { if (!this.allowVarargs) {
/* 3987 */       error(this.token.pos, "varargs.not.supported.in.source", new Object[] { this.source.name });
/* 3988 */       this.allowVarargs = true;
/*      */     } }
/*      */ 
/*      */   void checkForeach() {
/* 3992 */     if (!this.allowForeach) {
/* 3993 */       error(this.token.pos, "foreach.not.supported.in.source", new Object[] { this.source.name });
/* 3994 */       this.allowForeach = true;
/*      */     }
/*      */   }
/*      */ 
/* 3998 */   void checkStaticImports() { if (!this.allowStaticImport) {
/* 3999 */       error(this.token.pos, "static.import.not.supported.in.source", new Object[] { this.source.name });
/* 4000 */       this.allowStaticImport = true;
/*      */     } }
/*      */ 
/*      */   void checkAnnotations() {
/* 4004 */     if (!this.allowAnnotations) {
/* 4005 */       error(this.token.pos, "annotations.not.supported.in.source", new Object[] { this.source.name });
/* 4006 */       this.allowAnnotations = true;
/*      */     }
/*      */   }
/*      */ 
/* 4010 */   void checkDiamond() { if (!this.allowDiamond) {
/* 4011 */       error(this.token.pos, "diamond.not.supported.in.source", new Object[] { this.source.name });
/* 4012 */       this.allowDiamond = true;
/*      */     } }
/*      */ 
/*      */   void checkMulticatch() {
/* 4016 */     if (!this.allowMulticatch) {
/* 4017 */       error(this.token.pos, "multicatch.not.supported.in.source", new Object[] { this.source.name });
/* 4018 */       this.allowMulticatch = true;
/*      */     }
/*      */   }
/*      */ 
/* 4022 */   void checkTryWithResources() { if (!this.allowTWR) {
/* 4023 */       error(this.token.pos, "try.with.resources.not.supported.in.source", new Object[] { this.source.name });
/* 4024 */       this.allowTWR = true;
/*      */     } }
/*      */ 
/*      */   void checkLambda() {
/* 4028 */     if (!this.allowLambda) {
/* 4029 */       this.log.error(this.token.pos, "lambda.not.supported.in.source", new Object[] { this.source.name });
/* 4030 */       this.allowLambda = true;
/*      */     }
/*      */   }
/*      */ 
/* 4034 */   void checkMethodReferences() { if (!this.allowMethodReferences) {
/* 4035 */       this.log.error(this.token.pos, "method.references.not.supported.in.source", new Object[] { this.source.name });
/* 4036 */       this.allowMethodReferences = true;
/*      */     } }
/*      */ 
/*      */   void checkDefaultMethods() {
/* 4040 */     if (!this.allowDefaultMethods) {
/* 4041 */       this.log.error(this.token.pos, "default.methods.not.supported.in.source", new Object[] { this.source.name });
/* 4042 */       this.allowDefaultMethods = true;
/*      */     }
/*      */   }
/*      */ 
/* 4046 */   void checkIntersectionTypesInCast() { if (!this.allowIntersectionTypesInCast) {
/* 4047 */       this.log.error(this.token.pos, "intersection.types.in.cast.not.supported.in.source", new Object[] { this.source.name });
/* 4048 */       this.allowIntersectionTypesInCast = true;
/*      */     } }
/*      */ 
/*      */   void checkStaticInterfaceMethods() {
/* 4052 */     if (!this.allowStaticInterfaceMethods) {
/* 4053 */       this.log.error(this.token.pos, "static.intf.methods.not.supported.in.source", new Object[] { this.source.name });
/* 4054 */       this.allowStaticInterfaceMethods = true;
/*      */     }
/*      */   }
/*      */ 
/* 4058 */   void checkTypeAnnotations() { if (!this.allowTypeAnnotations) {
/* 4059 */       this.log.error(this.token.pos, "type.annotations.not.supported.in.source", new Object[] { this.source.name });
/* 4060 */       this.allowTypeAnnotations = true;
/*      */     } }
/*      */ 
/*      */   void checkAnnotationsAfterTypeParams(int paramInt) {
/* 4064 */     if (!this.allowAnnotationsAfterTypeParams) {
/* 4065 */       this.log.error(paramInt, "annotations.after.type.params.not.supported.in.source", new Object[] { this.source.name });
/* 4066 */       this.allowAnnotationsAfterTypeParams = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static abstract class AbstractEndPosTable
/*      */     implements EndPosTable
/*      */   {
/*      */     protected JavacParser parser;
/*      */     protected int errorEndPos;
/*      */ 
/*      */     public AbstractEndPosTable(JavacParser paramJavacParser)
/*      */     {
/* 4154 */       this.parser = paramJavacParser;
/*      */     }
/*      */ 
/*      */     protected abstract <T extends JCTree> T to(T paramT);
/*      */ 
/*      */     protected abstract <T extends JCTree> T toP(T paramT);
/*      */ 
/*      */     protected void setErrorEndPos(int paramInt)
/*      */     {
/* 4179 */       if (paramInt > this.errorEndPos)
/* 4180 */         this.errorEndPos = paramInt;
/*      */     }
/*      */ 
/*      */     protected void setParser(JavacParser paramJavacParser)
/*      */     {
/* 4185 */       this.parser = paramJavacParser;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract enum BasicErrorRecoveryAction
/*      */     implements JavacParser.ErrorRecoveryAction
/*      */   {
/*  130 */     BLOCK_STMT, 
/*  131 */     CATCH_CLAUSE;
/*      */   }
/*      */ 
/*      */   protected static class EmptyEndPosTable extends JavacParser.AbstractEndPosTable
/*      */   {
/*      */     EmptyEndPosTable(JavacParser paramJavacParser)
/*      */     {
/* 4119 */       super();
/*      */     }
/*      */     public void storeEnd(JCTree paramJCTree, int paramInt) {
/*      */     }
/*      */ 
/*      */     protected <T extends JCTree> T to(T paramT) {
/* 4125 */       return paramT;
/*      */     }
/*      */ 
/*      */     protected <T extends JCTree> T toP(T paramT) {
/* 4129 */       return paramT;
/*      */     }
/*      */ 
/*      */     public int getEndPos(JCTree paramJCTree) {
/* 4133 */       return -1;
/*      */     }
/*      */ 
/*      */     public int replaceTree(JCTree paramJCTree1, JCTree paramJCTree2) {
/* 4137 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface ErrorRecoveryAction
/*      */   {
/*      */     public abstract JCTree doRecover(JavacParser paramJavacParser);
/*      */   }
/*      */ 
/*      */   static enum ParensResult
/*      */   {
/* 1701 */     CAST, 
/* 1702 */     EXPLICIT_LAMBDA, 
/* 1703 */     IMPLICIT_LAMBDA, 
/* 1704 */     PARENS;
/*      */   }
/*      */ 
/*      */   protected static class SimpleEndPosTable extends JavacParser.AbstractEndPosTable
/*      */   {
/*      */     private final IntHashTable endPosMap;
/*      */ 
/*      */     SimpleEndPosTable(JavacParser paramJavacParser)
/*      */     {
/* 4078 */       super();
/* 4079 */       this.endPosMap = new IntHashTable();
/*      */     }
/*      */ 
/*      */     public void storeEnd(JCTree paramJCTree, int paramInt) {
/* 4083 */       this.endPosMap.putAtIndex(paramJCTree, this.errorEndPos > paramInt ? this.errorEndPos : paramInt, this.endPosMap
/* 4084 */         .lookup(paramJCTree));
/*      */     }
/*      */ 
/*      */     protected <T extends JCTree> T to(T paramT)
/*      */     {
/* 4088 */       storeEnd(paramT, this.parser.token.endPos);
/* 4089 */       return paramT;
/*      */     }
/*      */ 
/*      */     protected <T extends JCTree> T toP(T paramT) {
/* 4093 */       storeEnd(paramT, this.parser.S.prevToken().endPos);
/* 4094 */       return paramT;
/*      */     }
/*      */ 
/*      */     public int getEndPos(JCTree paramJCTree) {
/* 4098 */       int i = this.endPosMap.getFromIndex(this.endPosMap.lookup(paramJCTree));
/*      */ 
/* 4100 */       return i == -1 ? -1 : i;
/*      */     }
/*      */ 
/*      */     public int replaceTree(JCTree paramJCTree1, JCTree paramJCTree2) {
/* 4104 */       int i = this.endPosMap.remove(paramJCTree1);
/* 4105 */       if (i != -1) {
/* 4106 */         storeEnd(paramJCTree2, i);
/* 4107 */         return i;
/*      */       }
/* 4109 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.JavacParser
 * JD-Core Version:    0.6.2
 */