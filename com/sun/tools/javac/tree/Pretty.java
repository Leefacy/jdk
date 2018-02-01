/*      */ package com.sun.tools.javac.tree;
/*      */ 
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Name.Table;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ 
/*      */ public class Pretty extends JCTree.Visitor
/*      */ {
/*      */   private final boolean sourceOutput;
/*      */   Writer out;
/*   66 */   public int width = 4;
/*      */ 
/*   70 */   int lmargin = 0;
/*      */   Name enclClassName;
/*   79 */   DocCommentTable docComments = null;
/*      */   private static final String trimSequence = "[...]";
/*      */   private static final int PREFERRED_LENGTH = 20;
/*  166 */   String lineSep = System.getProperty("line.separator");
/*      */   int prec;
/*      */ 
/*      */   public Pretty(Writer paramWriter, boolean paramBoolean)
/*      */   {
/*   49 */     this.out = paramWriter;
/*   50 */     this.sourceOutput = paramBoolean;
/*      */   }
/*      */ 
/*      */   void align()
/*      */     throws IOException
/*      */   {
/*   95 */     for (int i = 0; i < this.lmargin; i++) this.out.write(" ");
/*      */   }
/*      */ 
/*      */   void indent()
/*      */   {
/*  101 */     this.lmargin += this.width;
/*      */   }
/*      */ 
/*      */   void undent()
/*      */   {
/*  107 */     this.lmargin -= this.width;
/*      */   }
/*      */ 
/*      */   void open(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  116 */     if (paramInt2 < paramInt1) this.out.write("(");
/*      */   }
/*      */ 
/*      */   void close(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  125 */     if (paramInt2 < paramInt1) this.out.write(")");
/*      */   }
/*      */ 
/*      */   public void print(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  131 */     this.out.write(Convert.escapeUnicode(paramObject.toString()));
/*      */   }
/*      */ 
/*      */   public void println()
/*      */     throws IOException
/*      */   {
/*  137 */     this.out.write(this.lineSep);
/*      */   }
/*      */ 
/*      */   public static String toSimpleString(JCTree paramJCTree) {
/*  141 */     return toSimpleString(paramJCTree, 20);
/*      */   }
/*      */ 
/*      */   public static String toSimpleString(JCTree paramJCTree, int paramInt) {
/*  145 */     StringWriter localStringWriter = new StringWriter();
/*      */     try {
/*  147 */       new Pretty(localStringWriter, false).printExpr(paramJCTree);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  152 */       throw new AssertionError(localIOException);
/*      */     }
/*      */ 
/*  156 */     String str = localStringWriter.toString().trim().replaceAll("\\s+", " ").replaceAll("/\\*missing\\*/", "");
/*  157 */     if (str.length() < paramInt) {
/*  158 */       return str;
/*      */     }
/*  160 */     int i = (paramInt - "[...]".length()) * 2 / 3;
/*  161 */     int j = paramInt - "[...]".length() - i;
/*  162 */     return str.substring(0, i) + "[...]" + str.substring(str.length() - j);
/*      */   }
/*      */ 
/*      */   public void printExpr(JCTree paramJCTree, int paramInt)
/*      */     throws IOException
/*      */   {
/*  188 */     int i = this.prec;
/*      */     try {
/*  190 */       this.prec = paramInt;
/*  191 */       if (paramJCTree == null) print("/*missing*/");
/*      */       else
/*  193 */         paramJCTree.accept(this);
/*      */     }
/*      */     catch (UncheckedIOException localUncheckedIOException) {
/*  196 */       IOException localIOException = new IOException(localUncheckedIOException.getMessage());
/*  197 */       localIOException.initCause(localUncheckedIOException);
/*  198 */       throw localIOException;
/*      */     } finally {
/*  200 */       this.prec = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printExpr(JCTree paramJCTree)
/*      */     throws IOException
/*      */   {
/*  208 */     printExpr(paramJCTree, 0);
/*      */   }
/*      */ 
/*      */   public void printStat(JCTree paramJCTree)
/*      */     throws IOException
/*      */   {
/*  214 */     printExpr(paramJCTree, -1);
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> void printExprs(List<T> paramList, String paramString)
/*      */     throws IOException
/*      */   {
/*  221 */     if (paramList.nonEmpty()) {
/*  222 */       printExpr((JCTree)paramList.head);
/*  223 */       for (List localList = paramList.tail; localList.nonEmpty(); localList = localList.tail) {
/*  224 */         print(paramString);
/*  225 */         printExpr((JCTree)localList.head);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> void printExprs(List<T> paramList)
/*      */     throws IOException
/*      */   {
/*  233 */     printExprs(paramList, ", ");
/*      */   }
/*      */ 
/*      */   public void printStats(List<? extends JCTree> paramList)
/*      */     throws IOException
/*      */   {
/*  239 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  240 */       align();
/*  241 */       printStat((JCTree)((List)localObject).head);
/*  242 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printFlags(long paramLong)
/*      */     throws IOException
/*      */   {
/*  249 */     if ((paramLong & 0x1000) != 0L) print("/*synthetic*/ ");
/*  250 */     print(TreeInfo.flagNames(paramLong));
/*  251 */     if ((paramLong & 0xFFF) != 0L) print(" ");
/*  252 */     if ((paramLong & 0x2000) != 0L) print("@"); 
/*      */   }
/*      */ 
/*      */   public void printAnnotations(List<JCTree.JCAnnotation> paramList) throws IOException {
/*  256 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  257 */       printStat((JCTree)((List)localObject).head);
/*  258 */       println();
/*  259 */       align();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printTypeAnnotations(List<JCTree.JCAnnotation> paramList) throws IOException {
/*  264 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  265 */       printExpr((JCTree)((List)localObject).head);
/*  266 */       print(" ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printDocComment(JCTree paramJCTree)
/*      */     throws IOException
/*      */   {
/*  274 */     if (this.docComments != null) {
/*  275 */       String str = this.docComments.getCommentText(paramJCTree);
/*  276 */       if (str != null) {
/*  277 */         print("/**"); println();
/*  278 */         int i = 0;
/*  279 */         int j = lineEndPos(str, i);
/*  280 */         while (i < str.length()) {
/*  281 */           align();
/*  282 */           print(" *");
/*  283 */           if ((i < str.length()) && (str.charAt(i) > ' ')) print(" ");
/*  284 */           print(str.substring(i, j)); println();
/*  285 */           i = j + 1;
/*  286 */           j = lineEndPos(str, i);
/*      */         }
/*  288 */         align(); print(" */"); println();
/*  289 */         align();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static int lineEndPos(String paramString, int paramInt) {
/*  295 */     int i = paramString.indexOf('\n', paramInt);
/*  296 */     if (i < 0) i = paramString.length();
/*  297 */     return i;
/*      */   }
/*      */ 
/*      */   public void printTypeParameters(List<JCTree.JCTypeParameter> paramList)
/*      */     throws IOException
/*      */   {
/*  304 */     if (paramList.nonEmpty()) {
/*  305 */       print("<");
/*  306 */       printExprs(paramList);
/*  307 */       print(">");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printBlock(List<? extends JCTree> paramList)
/*      */     throws IOException
/*      */   {
/*  314 */     print("{");
/*  315 */     println();
/*  316 */     indent();
/*  317 */     printStats(paramList);
/*  318 */     undent();
/*  319 */     align();
/*  320 */     print("}");
/*      */   }
/*      */ 
/*      */   public void printEnumBody(List<JCTree> paramList)
/*      */     throws IOException
/*      */   {
/*  326 */     print("{");
/*  327 */     println();
/*  328 */     indent();
/*  329 */     int i = 1;
/*  330 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  331 */       if (isEnumerator((JCTree)((List)localObject).head)) {
/*  332 */         if (i == 0) {
/*  333 */           print(",");
/*  334 */           println();
/*      */         }
/*  336 */         align();
/*  337 */         printStat((JCTree)((List)localObject).head);
/*  338 */         i = 0;
/*      */       }
/*      */     }
/*  341 */     print(";");
/*  342 */     println();
/*  343 */     for (localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  344 */       if (!isEnumerator((JCTree)((List)localObject).head)) {
/*  345 */         align();
/*  346 */         printStat((JCTree)((List)localObject).head);
/*  347 */         println();
/*      */       }
/*      */     }
/*  350 */     undent();
/*  351 */     align();
/*  352 */     print("}");
/*      */   }
/*      */ 
/*      */   boolean isEnumerator(JCTree paramJCTree)
/*      */   {
/*  357 */     return (paramJCTree.hasTag(JCTree.Tag.VARDEF)) && ((((JCTree.JCVariableDecl)paramJCTree).mods.flags & 0x4000) != 0L);
/*      */   }
/*      */ 
/*      */   public void printUnit(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl)
/*      */     throws IOException
/*      */   {
/*  368 */     this.docComments = paramJCCompilationUnit.docComments;
/*  369 */     printDocComment(paramJCCompilationUnit);
/*  370 */     if (paramJCCompilationUnit.pid != null) {
/*  371 */       print("package ");
/*  372 */       printExpr(paramJCCompilationUnit.pid);
/*  373 */       print(";");
/*  374 */       println();
/*      */     }
/*  376 */     int i = 1;
/*  377 */     for (List localList = paramJCCompilationUnit.defs; 
/*  378 */       (localList.nonEmpty()) && ((paramJCClassDecl == null) || (((JCTree)localList.head).hasTag(JCTree.Tag.IMPORT))); 
/*  379 */       localList = localList.tail) {
/*  380 */       if (((JCTree)localList.head).hasTag(JCTree.Tag.IMPORT)) {
/*  381 */         JCTree.JCImport localJCImport = (JCTree.JCImport)localList.head;
/*  382 */         Name localName = TreeInfo.name(localJCImport.qualid);
/*  383 */         if ((localName == localName.table.names.asterisk) || (paramJCClassDecl == null) || 
/*  385 */           (isUsed(TreeInfo.symbol(localJCImport.qualid), 
/*  385 */           paramJCClassDecl))) {
/*  386 */           if (i != 0) {
/*  387 */             i = 0;
/*  388 */             println();
/*      */           }
/*  390 */           printStat(localJCImport);
/*      */         }
/*      */       } else {
/*  393 */         printStat((JCTree)localList.head);
/*      */       }
/*      */     }
/*  396 */     if (paramJCClassDecl != null) {
/*  397 */       printStat(paramJCClassDecl);
/*  398 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isUsed(final Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/*  412 */     TreeScanner local1UsedVisitor = new TreeScanner()
/*      */     {
/*  407 */       boolean result = false;
/*      */ 
/*      */       public void scan(JCTree paramAnonymousJCTree)
/*      */       {
/*  405 */         if ((paramAnonymousJCTree != null) && (!this.result)) paramAnonymousJCTree.accept(this); 
/*      */       }
/*      */ 
/*      */       public void visitIdent(JCTree.JCIdent paramAnonymousJCIdent)
/*      */       {
/*  409 */         if (paramAnonymousJCIdent.sym == paramSymbol) this.result = true;
/*      */       }
/*      */     };
/*  413 */     local1UsedVisitor.scan(paramJCTree);
/*  414 */     return local1UsedVisitor.result;
/*      */   }
/*      */ 
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */   {
/*      */     try
/*      */     {
/*  423 */       printUnit(paramJCCompilationUnit, null);
/*      */     } catch (IOException localIOException) {
/*  425 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitImport(JCTree.JCImport paramJCImport) {
/*      */     try {
/*  431 */       print("import ");
/*  432 */       if (paramJCImport.staticImport) print("static ");
/*  433 */       printExpr(paramJCImport.qualid);
/*  434 */       print(";");
/*  435 */       println();
/*      */     } catch (IOException localIOException) {
/*  437 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*      */     try {
/*  443 */       println(); align();
/*  444 */       printDocComment(paramJCClassDecl);
/*  445 */       printAnnotations(paramJCClassDecl.mods.annotations);
/*  446 */       printFlags(paramJCClassDecl.mods.flags & 0xFFFFFDFF);
/*  447 */       Name localName = this.enclClassName;
/*  448 */       this.enclClassName = paramJCClassDecl.name;
/*  449 */       if ((paramJCClassDecl.mods.flags & 0x200) != 0L) {
/*  450 */         print("interface " + paramJCClassDecl.name);
/*  451 */         printTypeParameters(paramJCClassDecl.typarams);
/*  452 */         if (paramJCClassDecl.implementing.nonEmpty()) {
/*  453 */           print(" extends ");
/*  454 */           printExprs(paramJCClassDecl.implementing);
/*      */         }
/*      */       } else {
/*  457 */         if ((paramJCClassDecl.mods.flags & 0x4000) != 0L)
/*  458 */           print("enum " + paramJCClassDecl.name);
/*      */         else
/*  460 */           print("class " + paramJCClassDecl.name);
/*  461 */         printTypeParameters(paramJCClassDecl.typarams);
/*  462 */         if (paramJCClassDecl.extending != null) {
/*  463 */           print(" extends ");
/*  464 */           printExpr(paramJCClassDecl.extending);
/*      */         }
/*  466 */         if (paramJCClassDecl.implementing.nonEmpty()) {
/*  467 */           print(" implements ");
/*  468 */           printExprs(paramJCClassDecl.implementing);
/*      */         }
/*      */       }
/*  471 */       print(" ");
/*  472 */       if ((paramJCClassDecl.mods.flags & 0x4000) != 0L)
/*  473 */         printEnumBody(paramJCClassDecl.defs);
/*      */       else {
/*  475 */         printBlock(paramJCClassDecl.defs);
/*      */       }
/*  477 */       this.enclClassName = localName;
/*      */     } catch (IOException localIOException) {
/*  479 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*      */     try {
/*  486 */       if ((paramJCMethodDecl.name == paramJCMethodDecl.name.table.names.init) && (this.enclClassName == null) && (this.sourceOutput))
/*      */       {
/*  488 */         return;
/*  489 */       }println(); align();
/*  490 */       printDocComment(paramJCMethodDecl);
/*  491 */       printExpr(paramJCMethodDecl.mods);
/*  492 */       printTypeParameters(paramJCMethodDecl.typarams);
/*  493 */       if (paramJCMethodDecl.name == paramJCMethodDecl.name.table.names.init) {
/*  494 */         print(this.enclClassName != null ? this.enclClassName : paramJCMethodDecl.name);
/*      */       } else {
/*  496 */         printExpr(paramJCMethodDecl.restype);
/*  497 */         print(" " + paramJCMethodDecl.name);
/*      */       }
/*  499 */       print("(");
/*  500 */       if (paramJCMethodDecl.recvparam != null) {
/*  501 */         printExpr(paramJCMethodDecl.recvparam);
/*  502 */         if (paramJCMethodDecl.params.size() > 0) {
/*  503 */           print(", ");
/*      */         }
/*      */       }
/*  506 */       printExprs(paramJCMethodDecl.params);
/*  507 */       print(")");
/*  508 */       if (paramJCMethodDecl.thrown.nonEmpty()) {
/*  509 */         print(" throws ");
/*  510 */         printExprs(paramJCMethodDecl.thrown);
/*      */       }
/*  512 */       if (paramJCMethodDecl.defaultValue != null) {
/*  513 */         print(" default ");
/*  514 */         printExpr(paramJCMethodDecl.defaultValue);
/*      */       }
/*  516 */       if (paramJCMethodDecl.body != null) {
/*  517 */         print(" ");
/*  518 */         printStat(paramJCMethodDecl.body);
/*      */       } else {
/*  520 */         print(";");
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  523 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*      */     try {
/*  529 */       if ((this.docComments != null) && (this.docComments.hasComment(paramJCVariableDecl))) {
/*  530 */         println(); align();
/*      */       }
/*  532 */       printDocComment(paramJCVariableDecl);
/*      */       Object localObject;
/*  533 */       if ((paramJCVariableDecl.mods.flags & 0x4000) != 0L) {
/*  534 */         print("/*public static final*/ ");
/*  535 */         print(paramJCVariableDecl.name);
/*  536 */         if (paramJCVariableDecl.init != null) {
/*  537 */           if ((this.sourceOutput) && (paramJCVariableDecl.init.hasTag(JCTree.Tag.NEWCLASS))) {
/*  538 */             print(" /*enum*/ ");
/*  539 */             localObject = (JCTree.JCNewClass)paramJCVariableDecl.init;
/*  540 */             if ((((JCTree.JCNewClass)localObject).args != null) && (((JCTree.JCNewClass)localObject).args.nonEmpty())) {
/*  541 */               print("(");
/*  542 */               print(((JCTree.JCNewClass)localObject).args);
/*  543 */               print(")");
/*      */             }
/*  545 */             if ((((JCTree.JCNewClass)localObject).def != null) && (((JCTree.JCNewClass)localObject).def.defs != null)) {
/*  546 */               print(" ");
/*  547 */               printBlock(((JCTree.JCNewClass)localObject).def.defs);
/*      */             }
/*  549 */             return;
/*      */           }
/*  551 */           print(" /* = ");
/*  552 */           printExpr(paramJCVariableDecl.init);
/*  553 */           print(" */");
/*      */         }
/*      */       } else {
/*  556 */         printExpr(paramJCVariableDecl.mods);
/*  557 */         if ((paramJCVariableDecl.mods.flags & 0x0) != 0L) {
/*  558 */           localObject = paramJCVariableDecl.vartype;
/*  559 */           List localList = null;
/*  560 */           if ((localObject instanceof JCTree.JCAnnotatedType)) {
/*  561 */             localList = ((JCTree.JCAnnotatedType)localObject).annotations;
/*  562 */             localObject = ((JCTree.JCAnnotatedType)localObject).underlyingType;
/*      */           }
/*  564 */           printExpr(((JCTree.JCArrayTypeTree)localObject).elemtype);
/*  565 */           if (localList != null) {
/*  566 */             print(Character.valueOf(' '));
/*  567 */             printTypeAnnotations(localList);
/*      */           }
/*  569 */           print("... " + paramJCVariableDecl.name);
/*      */         } else {
/*  571 */           printExpr(paramJCVariableDecl.vartype);
/*  572 */           print(" " + paramJCVariableDecl.name);
/*      */         }
/*  574 */         if (paramJCVariableDecl.init != null) {
/*  575 */           print(" = ");
/*  576 */           printExpr(paramJCVariableDecl.init);
/*      */         }
/*  578 */         if (this.prec == -1) print(";"); 
/*      */       }
/*      */     }
/*  581 */     catch (IOException localIOException) { throw new UncheckedIOException(localIOException); }
/*      */   }
/*      */ 
/*      */   public void visitSkip(JCTree.JCSkip paramJCSkip)
/*      */   {
/*      */     try {
/*  587 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  589 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/*      */     try {
/*  595 */       printFlags(paramJCBlock.flags);
/*  596 */       printBlock(paramJCBlock.stats);
/*      */     } catch (IOException localIOException) {
/*  598 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/*      */     try {
/*  604 */       print("do ");
/*  605 */       printStat(paramJCDoWhileLoop.body);
/*  606 */       align();
/*  607 */       print(" while ");
/*  608 */       if (paramJCDoWhileLoop.cond.hasTag(JCTree.Tag.PARENS)) {
/*  609 */         printExpr(paramJCDoWhileLoop.cond);
/*      */       } else {
/*  611 */         print("(");
/*  612 */         printExpr(paramJCDoWhileLoop.cond);
/*  613 */         print(")");
/*      */       }
/*  615 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  617 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/*      */     try {
/*  623 */       print("while ");
/*  624 */       if (paramJCWhileLoop.cond.hasTag(JCTree.Tag.PARENS)) {
/*  625 */         printExpr(paramJCWhileLoop.cond);
/*      */       } else {
/*  627 */         print("(");
/*  628 */         printExpr(paramJCWhileLoop.cond);
/*  629 */         print(")");
/*      */       }
/*  631 */       print(" ");
/*  632 */       printStat(paramJCWhileLoop.body);
/*      */     } catch (IOException localIOException) {
/*  634 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/*      */     try {
/*  640 */       print("for (");
/*  641 */       if (paramJCForLoop.init.nonEmpty()) {
/*  642 */         if (((JCTree.JCStatement)paramJCForLoop.init.head).hasTag(JCTree.Tag.VARDEF)) {
/*  643 */           printExpr((JCTree)paramJCForLoop.init.head);
/*  644 */           for (List localList = paramJCForLoop.init.tail; localList.nonEmpty(); localList = localList.tail) {
/*  645 */             JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)localList.head;
/*  646 */             print(", " + localJCVariableDecl.name + " = ");
/*  647 */             printExpr(localJCVariableDecl.init);
/*      */           }
/*      */         } else {
/*  650 */           printExprs(paramJCForLoop.init);
/*      */         }
/*      */       }
/*  653 */       print("; ");
/*  654 */       if (paramJCForLoop.cond != null) printExpr(paramJCForLoop.cond);
/*  655 */       print("; ");
/*  656 */       printExprs(paramJCForLoop.step);
/*  657 */       print(") ");
/*  658 */       printStat(paramJCForLoop.body);
/*      */     } catch (IOException localIOException) {
/*  660 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*      */     try {
/*  666 */       print("for (");
/*  667 */       printExpr(paramJCEnhancedForLoop.var);
/*  668 */       print(" : ");
/*  669 */       printExpr(paramJCEnhancedForLoop.expr);
/*  670 */       print(") ");
/*  671 */       printStat(paramJCEnhancedForLoop.body);
/*      */     } catch (IOException localIOException) {
/*  673 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/*      */     try {
/*  679 */       print(paramJCLabeledStatement.label + ": ");
/*  680 */       printStat(paramJCLabeledStatement.body);
/*      */     } catch (IOException localIOException) {
/*  682 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/*      */     try {
/*  688 */       print("switch ");
/*  689 */       if (paramJCSwitch.selector.hasTag(JCTree.Tag.PARENS)) {
/*  690 */         printExpr(paramJCSwitch.selector);
/*      */       } else {
/*  692 */         print("(");
/*  693 */         printExpr(paramJCSwitch.selector);
/*  694 */         print(")");
/*      */       }
/*  696 */       print(" {");
/*  697 */       println();
/*  698 */       printStats(paramJCSwitch.cases);
/*  699 */       align();
/*  700 */       print("}");
/*      */     } catch (IOException localIOException) {
/*  702 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitCase(JCTree.JCCase paramJCCase) {
/*      */     try {
/*  708 */       if (paramJCCase.pat == null) {
/*  709 */         print("default");
/*      */       } else {
/*  711 */         print("case ");
/*  712 */         printExpr(paramJCCase.pat);
/*      */       }
/*  714 */       print(": ");
/*  715 */       println();
/*  716 */       indent();
/*  717 */       printStats(paramJCCase.stats);
/*  718 */       undent();
/*  719 */       align();
/*      */     } catch (IOException localIOException) {
/*  721 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/*      */     try {
/*  727 */       print("synchronized ");
/*  728 */       if (paramJCSynchronized.lock.hasTag(JCTree.Tag.PARENS)) {
/*  729 */         printExpr(paramJCSynchronized.lock);
/*      */       } else {
/*  731 */         print("(");
/*  732 */         printExpr(paramJCSynchronized.lock);
/*  733 */         print(")");
/*      */       }
/*  735 */       print(" ");
/*  736 */       printStat(paramJCSynchronized.body);
/*      */     } catch (IOException localIOException) {
/*  738 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTry(JCTree.JCTry paramJCTry) {
/*      */     try {
/*  744 */       print("try ");
/*  745 */       if (paramJCTry.resources.nonEmpty()) {
/*  746 */         print("(");
/*  747 */         int i = 1;
/*  748 */         for (JCTree localJCTree : paramJCTry.resources) {
/*  749 */           if (i == 0) {
/*  750 */             println();
/*  751 */             indent();
/*      */           }
/*  753 */           printStat(localJCTree);
/*  754 */           i = 0;
/*      */         }
/*  756 */         print(") ");
/*      */       }
/*  758 */       printStat(paramJCTry.body);
/*  759 */       for (List localList = paramJCTry.catchers; localList.nonEmpty(); localList = localList.tail) {
/*  760 */         printStat((JCTree)localList.head);
/*      */       }
/*  762 */       if (paramJCTry.finalizer != null) {
/*  763 */         print(" finally ");
/*  764 */         printStat(paramJCTry.finalizer);
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  767 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitCatch(JCTree.JCCatch paramJCCatch) {
/*      */     try {
/*  773 */       print(" catch (");
/*  774 */       printExpr(paramJCCatch.param);
/*  775 */       print(") ");
/*  776 */       printStat(paramJCCatch.body);
/*      */     } catch (IOException localIOException) {
/*  778 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/*      */     try {
/*  784 */       open(this.prec, 3);
/*  785 */       printExpr(paramJCConditional.cond, 4);
/*  786 */       print(" ? ");
/*  787 */       printExpr(paramJCConditional.truepart);
/*  788 */       print(" : ");
/*  789 */       printExpr(paramJCConditional.falsepart, 3);
/*  790 */       close(this.prec, 3);
/*      */     } catch (IOException localIOException) {
/*  792 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/*      */     try {
/*  798 */       print("if ");
/*  799 */       if (paramJCIf.cond.hasTag(JCTree.Tag.PARENS)) {
/*  800 */         printExpr(paramJCIf.cond);
/*      */       } else {
/*  802 */         print("(");
/*  803 */         printExpr(paramJCIf.cond);
/*  804 */         print(")");
/*      */       }
/*  806 */       print(" ");
/*  807 */       printStat(paramJCIf.thenpart);
/*  808 */       if (paramJCIf.elsepart != null) {
/*  809 */         print(" else ");
/*  810 */         printStat(paramJCIf.elsepart);
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  813 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/*      */     try {
/*  819 */       printExpr(paramJCExpressionStatement.expr);
/*  820 */       if (this.prec == -1) print(";"); 
/*      */     }
/*  822 */     catch (IOException localIOException) { throw new UncheckedIOException(localIOException); }
/*      */   }
/*      */ 
/*      */   public void visitBreak(JCTree.JCBreak paramJCBreak)
/*      */   {
/*      */     try {
/*  828 */       print("break");
/*  829 */       if (paramJCBreak.label != null) print(" " + paramJCBreak.label);
/*  830 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  832 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/*      */     try {
/*  838 */       print("continue");
/*  839 */       if (paramJCContinue.label != null) print(" " + paramJCContinue.label);
/*  840 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  842 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*      */     try {
/*  848 */       print("return");
/*  849 */       if (paramJCReturn.expr != null) {
/*  850 */         print(" ");
/*  851 */         printExpr(paramJCReturn.expr);
/*      */       }
/*  853 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  855 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/*      */     try {
/*  861 */       print("throw ");
/*  862 */       printExpr(paramJCThrow.expr);
/*  863 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  865 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/*      */     try {
/*  871 */       print("assert ");
/*  872 */       printExpr(paramJCAssert.cond);
/*  873 */       if (paramJCAssert.detail != null) {
/*  874 */         print(" : ");
/*  875 */         printExpr(paramJCAssert.detail);
/*      */       }
/*  877 */       print(";");
/*      */     } catch (IOException localIOException) {
/*  879 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/*      */     try {
/*  885 */       if (!paramJCMethodInvocation.typeargs.isEmpty()) {
/*  886 */         if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT)) {
/*  887 */           JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCMethodInvocation.meth;
/*  888 */           printExpr(localJCFieldAccess.selected);
/*  889 */           print(".<");
/*  890 */           printExprs(paramJCMethodInvocation.typeargs);
/*  891 */           print(">" + localJCFieldAccess.name);
/*      */         } else {
/*  893 */           print("<");
/*  894 */           printExprs(paramJCMethodInvocation.typeargs);
/*  895 */           print(">");
/*  896 */           printExpr(paramJCMethodInvocation.meth);
/*      */         }
/*      */       }
/*  899 */       else printExpr(paramJCMethodInvocation.meth);
/*      */ 
/*  901 */       print("(");
/*  902 */       printExprs(paramJCMethodInvocation.args);
/*  903 */       print(")");
/*      */     } catch (IOException localIOException) {
/*  905 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*      */     try {
/*  911 */       if (paramJCNewClass.encl != null) {
/*  912 */         printExpr(paramJCNewClass.encl);
/*  913 */         print(".");
/*      */       }
/*  915 */       print("new ");
/*  916 */       if (!paramJCNewClass.typeargs.isEmpty()) {
/*  917 */         print("<");
/*  918 */         printExprs(paramJCNewClass.typeargs);
/*  919 */         print(">");
/*      */       }
/*  921 */       if ((paramJCNewClass.def != null) && (paramJCNewClass.def.mods.annotations.nonEmpty())) {
/*  922 */         printTypeAnnotations(paramJCNewClass.def.mods.annotations);
/*      */       }
/*  924 */       printExpr(paramJCNewClass.clazz);
/*  925 */       print("(");
/*  926 */       printExprs(paramJCNewClass.args);
/*  927 */       print(")");
/*  928 */       if (paramJCNewClass.def != null) {
/*  929 */         Name localName = this.enclClassName;
/*  930 */         this.enclClassName = ((paramJCNewClass.type != null) && (paramJCNewClass.type.tsym.name != paramJCNewClass.type.tsym.name.table.names.empty) ? paramJCNewClass.type.tsym.name : paramJCNewClass.def.name != null ? paramJCNewClass.def.name : null);
/*      */ 
/*  934 */         if ((paramJCNewClass.def.mods.flags & 0x4000) != 0L) print("/*enum*/");
/*  935 */         printBlock(paramJCNewClass.def.defs);
/*  936 */         this.enclClassName = localName;
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  939 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/*      */     try {
/*  945 */       if (paramJCNewArray.elemtype != null) {
/*  946 */         print("new ");
/*  947 */         JCTree.JCExpression localJCExpression = paramJCNewArray.elemtype;
/*  948 */         printBaseElementType(localJCExpression);
/*      */ 
/*  950 */         if (!paramJCNewArray.annotations.isEmpty()) {
/*  951 */           print(Character.valueOf(' '));
/*  952 */           printTypeAnnotations(paramJCNewArray.annotations);
/*      */         }
/*  954 */         if (paramJCNewArray.elems != null) {
/*  955 */           print("[]");
/*      */         }
/*      */ 
/*  958 */         int i = 0;
/*  959 */         List localList1 = paramJCNewArray.dimAnnotations;
/*  960 */         for (List localList2 = paramJCNewArray.dims; localList2.nonEmpty(); localList2 = localList2.tail) {
/*  961 */           if ((localList1.size() > i) && (!((List)localList1.get(i)).isEmpty())) {
/*  962 */             print(Character.valueOf(' '));
/*  963 */             printTypeAnnotations((List)localList1.get(i));
/*      */           }
/*  965 */           print("[");
/*  966 */           i++;
/*  967 */           printExpr((JCTree)localList2.head);
/*  968 */           print("]");
/*      */         }
/*  970 */         printBrackets(localJCExpression);
/*      */       }
/*  972 */       if (paramJCNewArray.elems != null) {
/*  973 */         print("{");
/*  974 */         printExprs(paramJCNewArray.elems);
/*  975 */         print("}");
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  978 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/*      */     try {
/*  984 */       print("(");
/*      */       String str;
/*  985 */       if (paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.EXPLICIT) {
/*  986 */         printExprs(paramJCLambda.params);
/*      */       } else {
/*  988 */         str = "";
/*  989 */         for (JCTree.JCVariableDecl localJCVariableDecl : paramJCLambda.params) {
/*  990 */           print(str);
/*  991 */           print(localJCVariableDecl.name);
/*  992 */           str = ",";
/*      */         }
/*      */       }
/*  995 */       print(")->");
/*  996 */       printExpr(paramJCLambda.body);
/*      */     } catch (IOException localIOException) {
/*  998 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/*      */     try {
/* 1004 */       print("(");
/* 1005 */       printExpr(paramJCParens.expr);
/* 1006 */       print(")");
/*      */     } catch (IOException localIOException) {
/* 1008 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/*      */     try {
/* 1014 */       open(this.prec, 1);
/* 1015 */       printExpr(paramJCAssign.lhs, 2);
/* 1016 */       print(" = ");
/* 1017 */       printExpr(paramJCAssign.rhs, 1);
/* 1018 */       close(this.prec, 1);
/*      */     } catch (IOException localIOException) {
/* 1020 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String operatorName(JCTree.Tag paramTag) {
/* 1025 */     switch (1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramTag.ordinal()]) { case 1:
/* 1026 */       return "+";
/*      */     case 2:
/* 1027 */       return "-";
/*      */     case 3:
/* 1028 */       return "!";
/*      */     case 4:
/* 1029 */       return "~";
/*      */     case 5:
/* 1030 */       return "++";
/*      */     case 6:
/* 1031 */       return "--";
/*      */     case 7:
/* 1032 */       return "++";
/*      */     case 8:
/* 1033 */       return "--";
/*      */     case 9:
/* 1034 */       return "<*nullchk*>";
/*      */     case 10:
/* 1035 */       return "||";
/*      */     case 11:
/* 1036 */       return "&&";
/*      */     case 12:
/* 1037 */       return "==";
/*      */     case 13:
/* 1038 */       return "!=";
/*      */     case 14:
/* 1039 */       return "<";
/*      */     case 15:
/* 1040 */       return ">";
/*      */     case 16:
/* 1041 */       return "<=";
/*      */     case 17:
/* 1042 */       return ">=";
/*      */     case 18:
/* 1043 */       return "|";
/*      */     case 19:
/* 1044 */       return "^";
/*      */     case 20:
/* 1045 */       return "&";
/*      */     case 21:
/* 1046 */       return "<<";
/*      */     case 22:
/* 1047 */       return ">>";
/*      */     case 23:
/* 1048 */       return ">>>";
/*      */     case 24:
/* 1049 */       return "+";
/*      */     case 25:
/* 1050 */       return "-";
/*      */     case 26:
/* 1051 */       return "*";
/*      */     case 27:
/* 1052 */       return "/";
/*      */     case 28:
/* 1053 */       return "%"; }
/* 1054 */     throw new Error();
/*      */   }
/*      */ 
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp)
/*      */   {
/*      */     try {
/* 1060 */       open(this.prec, 2);
/* 1061 */       printExpr(paramJCAssignOp.lhs, 3);
/* 1062 */       print(" " + operatorName(paramJCAssignOp.getTag().noAssignOp()) + "= ");
/* 1063 */       printExpr(paramJCAssignOp.rhs, 2);
/* 1064 */       close(this.prec, 2);
/*      */     } catch (IOException localIOException) {
/* 1066 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/*      */     try {
/* 1072 */       int i = TreeInfo.opPrec(paramJCUnary.getTag());
/* 1073 */       String str = operatorName(paramJCUnary.getTag());
/* 1074 */       open(this.prec, i);
/* 1075 */       if (!paramJCUnary.getTag().isPostUnaryOp()) {
/* 1076 */         print(str);
/* 1077 */         printExpr(paramJCUnary.arg, i);
/*      */       } else {
/* 1079 */         printExpr(paramJCUnary.arg, i);
/* 1080 */         print(str);
/*      */       }
/* 1082 */       close(this.prec, i);
/*      */     } catch (IOException localIOException) {
/* 1084 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/*      */     try {
/* 1090 */       int i = TreeInfo.opPrec(paramJCBinary.getTag());
/* 1091 */       String str = operatorName(paramJCBinary.getTag());
/* 1092 */       open(this.prec, i);
/* 1093 */       printExpr(paramJCBinary.lhs, i);
/* 1094 */       print(" " + str + " ");
/* 1095 */       printExpr(paramJCBinary.rhs, i + 1);
/* 1096 */       close(this.prec, i);
/*      */     } catch (IOException localIOException) {
/* 1098 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/*      */     try {
/* 1104 */       open(this.prec, 14);
/* 1105 */       print("(");
/* 1106 */       printExpr(paramJCTypeCast.clazz);
/* 1107 */       print(")");
/* 1108 */       printExpr(paramJCTypeCast.expr, 14);
/* 1109 */       close(this.prec, 14);
/*      */     } catch (IOException localIOException) {
/* 1111 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/*      */     try {
/* 1117 */       open(this.prec, 10);
/* 1118 */       printExpr(paramJCInstanceOf.expr, 10);
/* 1119 */       print(" instanceof ");
/* 1120 */       printExpr(paramJCInstanceOf.clazz, 11);
/* 1121 */       close(this.prec, 10);
/*      */     } catch (IOException localIOException) {
/* 1123 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/*      */     try {
/* 1129 */       printExpr(paramJCArrayAccess.indexed, 15);
/* 1130 */       print("[");
/* 1131 */       printExpr(paramJCArrayAccess.index);
/* 1132 */       print("]");
/*      */     } catch (IOException localIOException) {
/* 1134 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/*      */     try {
/* 1140 */       printExpr(paramJCFieldAccess.selected, 15);
/* 1141 */       print("." + paramJCFieldAccess.name);
/*      */     } catch (IOException localIOException) {
/* 1143 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/*      */     try {
/* 1149 */       printExpr(paramJCMemberReference.expr);
/* 1150 */       print("::");
/* 1151 */       if (paramJCMemberReference.typeargs != null) {
/* 1152 */         print("<");
/* 1153 */         printExprs(paramJCMemberReference.typeargs);
/* 1154 */         print(">");
/*      */       }
/* 1156 */       print(paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.INVOKE ? paramJCMemberReference.name : "new");
/*      */     } catch (IOException localIOException) {
/* 1158 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/*      */     try {
/* 1164 */       print(paramJCIdent.name);
/*      */     } catch (IOException localIOException) {
/* 1166 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/*      */     try {
/* 1172 */       switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramJCLiteral.typetag.ordinal()]) {
/*      */       case 1:
/* 1174 */         print(paramJCLiteral.value.toString());
/* 1175 */         break;
/*      */       case 2:
/* 1177 */         print(paramJCLiteral.value + "L");
/* 1178 */         break;
/*      */       case 3:
/* 1180 */         print(paramJCLiteral.value + "F");
/* 1181 */         break;
/*      */       case 4:
/* 1183 */         print(paramJCLiteral.value.toString());
/* 1184 */         break;
/*      */       case 5:
/* 1186 */         print("'" + 
/* 1187 */           Convert.quote(
/* 1188 */           String.valueOf((char)((Number)paramJCLiteral.value)
/* 1188 */           .intValue())) + "'");
/*      */ 
/* 1190 */         break;
/*      */       case 6:
/* 1192 */         print(((Number)paramJCLiteral.value).intValue() == 1 ? "true" : "false");
/* 1193 */         break;
/*      */       case 7:
/* 1195 */         print("null");
/* 1196 */         break;
/*      */       default:
/* 1198 */         print("\"" + Convert.quote(paramJCLiteral.value.toString()) + "\"");
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1202 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/*      */     try {
/* 1208 */       switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramJCPrimitiveTypeTree.typetag.ordinal()]) {
/*      */       case 8:
/* 1210 */         print("byte");
/* 1211 */         break;
/*      */       case 5:
/* 1213 */         print("char");
/* 1214 */         break;
/*      */       case 9:
/* 1216 */         print("short");
/* 1217 */         break;
/*      */       case 1:
/* 1219 */         print("int");
/* 1220 */         break;
/*      */       case 2:
/* 1222 */         print("long");
/* 1223 */         break;
/*      */       case 3:
/* 1225 */         print("float");
/* 1226 */         break;
/*      */       case 4:
/* 1228 */         print("double");
/* 1229 */         break;
/*      */       case 6:
/* 1231 */         print("boolean");
/* 1232 */         break;
/*      */       case 10:
/* 1234 */         print("void");
/* 1235 */         break;
/*      */       case 7:
/*      */       default:
/* 1237 */         print("error");
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1241 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/*      */     try {
/* 1247 */       printBaseElementType(paramJCArrayTypeTree);
/* 1248 */       printBrackets(paramJCArrayTypeTree);
/*      */     } catch (IOException localIOException) {
/* 1250 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printBaseElementType(JCTree paramJCTree) throws IOException
/*      */   {
/* 1256 */     printExpr(TreeInfo.innermostType(paramJCTree));
/*      */   }
/*      */ 
/*      */   private void printBrackets(JCTree paramJCTree)
/*      */     throws IOException
/*      */   {
/* 1262 */     Object localObject = paramJCTree;
/*      */     while (true) {
/* 1264 */       if (((JCTree)localObject).hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1265 */         JCTree.JCAnnotatedType localJCAnnotatedType = (JCTree.JCAnnotatedType)localObject;
/* 1266 */         localObject = localJCAnnotatedType.underlyingType;
/* 1267 */         if (((JCTree)localObject).hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1268 */           print(Character.valueOf(' '));
/* 1269 */           printTypeAnnotations(localJCAnnotatedType.annotations);
/*      */         }
/*      */       }
/* 1272 */       if (!((JCTree)localObject).hasTag(JCTree.Tag.TYPEARRAY)) break;
/* 1273 */       print("[]");
/* 1274 */       localObject = ((JCTree.JCArrayTypeTree)localObject).elemtype;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */   {
/*      */     try
/*      */     {
/* 1283 */       printExpr(paramJCTypeApply.clazz);
/* 1284 */       print("<");
/* 1285 */       printExprs(paramJCTypeApply.arguments);
/* 1286 */       print(">");
/*      */     } catch (IOException localIOException) {
/* 1288 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/*      */     try {
/* 1294 */       printExprs(paramJCTypeUnion.alternatives, " | ");
/*      */     } catch (IOException localIOException) {
/* 1296 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/*      */     try {
/* 1302 */       printExprs(paramJCTypeIntersection.bounds, " & ");
/*      */     } catch (IOException localIOException) {
/* 1304 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/*      */     try {
/* 1310 */       if (paramJCTypeParameter.annotations.nonEmpty()) {
/* 1311 */         printTypeAnnotations(paramJCTypeParameter.annotations);
/*      */       }
/* 1313 */       print(paramJCTypeParameter.name);
/* 1314 */       if (paramJCTypeParameter.bounds.nonEmpty()) {
/* 1315 */         print(" extends ");
/* 1316 */         printExprs(paramJCTypeParameter.bounds, " & ");
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 1319 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard)
/*      */   {
/*      */     try {
/* 1326 */       print(paramJCWildcard.kind);
/* 1327 */       if (paramJCWildcard.kind.kind != BoundKind.UNBOUND)
/* 1328 */         printExpr(paramJCWildcard.inner);
/*      */     } catch (IOException localIOException) {
/* 1330 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind)
/*      */   {
/*      */     try {
/* 1337 */       print(String.valueOf(paramTypeBoundKind.kind));
/*      */     } catch (IOException localIOException) {
/* 1339 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/*      */     try {
/* 1345 */       print("(ERROR)");
/*      */     } catch (IOException localIOException) {
/* 1347 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/*      */     try {
/* 1353 */       print("(let " + paramLetExpr.defs + " in " + paramLetExpr.expr + ")");
/*      */     } catch (IOException localIOException) {
/* 1355 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitModifiers(JCTree.JCModifiers paramJCModifiers) {
/*      */     try {
/* 1361 */       printAnnotations(paramJCModifiers.annotations);
/* 1362 */       printFlags(paramJCModifiers.flags);
/*      */     } catch (IOException localIOException) {
/* 1364 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/*      */     try {
/* 1370 */       print("@");
/* 1371 */       printExpr(paramJCAnnotation.annotationType);
/* 1372 */       print("(");
/* 1373 */       printExprs(paramJCAnnotation.args);
/* 1374 */       print(")");
/*      */     } catch (IOException localIOException) {
/* 1376 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/*      */     try {
/* 1382 */       if (paramJCAnnotatedType.underlyingType.hasTag(JCTree.Tag.SELECT)) {
/* 1383 */         JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCAnnotatedType.underlyingType;
/* 1384 */         printExpr(localJCFieldAccess.selected, 15);
/* 1385 */         print(".");
/* 1386 */         printTypeAnnotations(paramJCAnnotatedType.annotations);
/* 1387 */         print(localJCFieldAccess.name);
/* 1388 */       } else if (paramJCAnnotatedType.underlyingType.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1389 */         printBaseElementType(paramJCAnnotatedType);
/* 1390 */         printBrackets(paramJCAnnotatedType);
/*      */       } else {
/* 1392 */         printTypeAnnotations(paramJCAnnotatedType.annotations);
/* 1393 */         printExpr(paramJCAnnotatedType.underlyingType);
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 1396 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTree(JCTree paramJCTree) {
/*      */     try {
/* 1402 */       print("(UNKNOWN: " + paramJCTree + ")");
/* 1403 */       println();
/*      */     } catch (IOException localIOException) {
/* 1405 */       throw new UncheckedIOException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class UncheckedIOException extends Error
/*      */   {
/*      */     static final long serialVersionUID = -4032692679158424751L;
/*      */ 
/*      */     UncheckedIOException(IOException paramIOException)
/*      */     {
/*  176 */       super(paramIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.Pretty
 * JD-Core Version:    0.6.2
 */