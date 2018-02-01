/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
/*     */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAssert;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAssignOp;
/*     */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*     */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*     */ import com.sun.tools.javac.tree.JCTree.JCBreak;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCase;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCatch;
/*     */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*     */ import com.sun.tools.javac.tree.JCTree.JCContinue;
/*     */ import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
/*     */ import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
/*     */ import com.sun.tools.javac.tree.JCTree.JCErroneous;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*     */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*     */ import com.sun.tools.javac.tree.JCTree.JCForLoop;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*     */ import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
/*     */ import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
/*     */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*     */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*     */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*     */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*     */ import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*     */ import com.sun.tools.javac.tree.JCTree.JCSkip;
/*     */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*     */ import com.sun.tools.javac.tree.JCTree.JCSynchronized;
/*     */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*     */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*     */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*     */ import com.sun.tools.javac.tree.JCTree.JCWildcard;
/*     */ import com.sun.tools.javac.tree.JCTree.LetExpr;
/*     */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.ByteBuffer;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import com.sun.tools.javac.util.Position.LineMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CRTable
/*     */   implements CRTFlags
/*     */ {
/*  48 */   private final boolean crtDebug = false;
/*     */ 
/*  52 */   private ListBuffer<CRTEntry> entries = new ListBuffer();
/*     */ 
/*  56 */   private Map<Object, SourceRange> positions = new HashMap();
/*     */   private EndPosTable endPosTable;
/*     */   JCTree.JCMethodDecl methodTree;
/*     */ 
/*     */   public CRTable(JCTree.JCMethodDecl paramJCMethodDecl, EndPosTable paramEndPosTable)
/*     */   {
/*  70 */     this.methodTree = paramJCMethodDecl;
/*  71 */     this.endPosTable = paramEndPosTable;
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  82 */     this.entries.append(new CRTEntry(paramObject, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public int writeCRT(ByteBuffer paramByteBuffer, Position.LineMap paramLineMap, Log paramLog)
/*     */   {
/*  90 */     int i = 0;
/*     */ 
/*  93 */     new SourceComputer().csp(this.methodTree);
/*     */ 
/*  95 */     for (List localList = this.entries.toList(); localList.nonEmpty(); localList = localList.tail)
/*     */     {
/*  97 */       CRTEntry localCRTEntry = (CRTEntry)localList.head;
/*     */ 
/* 101 */       if (localCRTEntry.startPc != localCRTEntry.endPc)
/*     */       {
/* 104 */         SourceRange localSourceRange = (SourceRange)this.positions.get(localCRTEntry.tree);
/* 105 */         Assert.checkNonNull(localSourceRange, "CRT: tree source positions are undefined");
/* 106 */         if ((localSourceRange.startPos != -1) && (localSourceRange.endPos != -1))
/*     */         {
/* 115 */           int j = encodePosition(localSourceRange.startPos, paramLineMap, paramLog);
/* 116 */           if (j != -1)
/*     */           {
/* 124 */             int k = encodePosition(localSourceRange.endPos, paramLineMap, paramLog);
/* 125 */             if (k != -1)
/*     */             {
/* 129 */               paramByteBuffer.appendChar(localCRTEntry.startPc);
/*     */ 
/* 131 */               paramByteBuffer.appendChar(localCRTEntry.endPc - 1);
/* 132 */               paramByteBuffer.appendInt(j);
/* 133 */               paramByteBuffer.appendInt(k);
/* 134 */               paramByteBuffer.appendChar(localCRTEntry.flags);
/*     */ 
/* 136 */               i++;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 139 */     return i;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 145 */     return this.entries.length();
/*     */   }
/*     */ 
/*     */   private String getTypes(int paramInt)
/*     */   {
/* 151 */     String str = "";
/* 152 */     if ((paramInt & 0x1) != 0) str = str + " CRT_STATEMENT";
/* 153 */     if ((paramInt & 0x2) != 0) str = str + " CRT_BLOCK";
/* 154 */     if ((paramInt & 0x4) != 0) str = str + " CRT_ASSIGNMENT";
/* 155 */     if ((paramInt & 0x8) != 0) str = str + " CRT_FLOW_CONTROLLER";
/* 156 */     if ((paramInt & 0x10) != 0) str = str + " CRT_FLOW_TARGET";
/* 157 */     if ((paramInt & 0x20) != 0) str = str + " CRT_INVOKE";
/* 158 */     if ((paramInt & 0x40) != 0) str = str + " CRT_CREATE";
/* 159 */     if ((paramInt & 0x80) != 0) str = str + " CRT_BRANCH_TRUE";
/* 160 */     if ((paramInt & 0x100) != 0) str = str + " CRT_BRANCH_FALSE";
/* 161 */     return str;
/*     */   }
/*     */ 
/*     */   private int encodePosition(int paramInt, Position.LineMap paramLineMap, Log paramLog)
/*     */   {
/* 168 */     int i = paramLineMap.getLineNumber(paramInt);
/* 169 */     int j = paramLineMap.getColumnNumber(paramInt);
/* 170 */     int k = Position.encodePosition(i, j);
/*     */ 
/* 175 */     if (k == -1) {
/* 176 */       paramLog.warning(paramInt, "position.overflow", new Object[] { Integer.valueOf(i) });
/*     */     }
/* 178 */     return k;
/*     */   }
/*     */ 
/*     */   static class CRTEntry
/*     */   {
/*     */     Object tree;
/*     */     int flags;
/*     */     int startPc;
/*     */     int endPc;
/*     */ 
/*     */     CRTEntry(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 570 */       this.tree = paramObject;
/* 571 */       this.flags = paramInt1;
/* 572 */       this.startPc = paramInt2;
/* 573 */       this.endPc = paramInt3;
/*     */     }
/*     */   }
/*     */ 
/*     */   class SourceComputer extends JCTree.Visitor
/*     */   {
/*     */     CRTable.SourceRange result;
/*     */ 
/*     */     SourceComputer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public CRTable.SourceRange csp(JCTree paramJCTree)
/*     */     {
/* 198 */       if (paramJCTree == null) return null;
/* 199 */       paramJCTree.accept(this);
/* 200 */       if (this.result != null) {
/* 201 */         CRTable.this.positions.put(paramJCTree, this.result);
/*     */       }
/* 203 */       return this.result;
/*     */     }
/*     */ 
/*     */     public CRTable.SourceRange csp(List<? extends JCTree> paramList)
/*     */     {
/* 209 */       if ((paramList == null) || (!paramList.nonEmpty())) return null;
/* 210 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange();
/* 211 */       for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 212 */         localSourceRange.mergeWith(csp((JCTree)((List)localObject).head));
/*     */       }
/* 214 */       CRTable.this.positions.put(paramList, localSourceRange);
/* 215 */       return localSourceRange;
/*     */     }
/*     */ 
/*     */     public CRTable.SourceRange cspCases(List<JCTree.JCCase> paramList)
/*     */     {
/* 222 */       if ((paramList == null) || (!paramList.nonEmpty())) return null;
/* 223 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange();
/* 224 */       for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 225 */         localSourceRange.mergeWith(csp((JCTree)((List)localObject).head));
/*     */       }
/* 227 */       CRTable.this.positions.put(paramList, localSourceRange);
/* 228 */       return localSourceRange;
/*     */     }
/*     */ 
/*     */     public CRTable.SourceRange cspCatchers(List<JCTree.JCCatch> paramList)
/*     */     {
/* 235 */       if ((paramList == null) || (!paramList.nonEmpty())) return null;
/* 236 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange();
/* 237 */       for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 238 */         localSourceRange.mergeWith(csp((JCTree)((List)localObject).head));
/*     */       }
/* 240 */       CRTable.this.positions.put(paramList, localSourceRange);
/* 241 */       return localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 245 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCMethodDecl), endPos(paramJCMethodDecl));
/* 246 */       localSourceRange.mergeWith(csp(paramJCMethodDecl.body));
/* 247 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 251 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCVariableDecl), endPos(paramJCVariableDecl));
/* 252 */       csp(paramJCVariableDecl.vartype);
/* 253 */       localSourceRange.mergeWith(csp(paramJCVariableDecl.init));
/* 254 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitSkip(JCTree.JCSkip paramJCSkip)
/*     */     {
/* 259 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCSkip), startPos(paramJCSkip));
/* 260 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 264 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCBlock), endPos(paramJCBlock));
/* 265 */       csp(paramJCBlock.stats);
/* 266 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 270 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCDoWhileLoop), endPos(paramJCDoWhileLoop));
/* 271 */       localSourceRange.mergeWith(csp(paramJCDoWhileLoop.body));
/* 272 */       localSourceRange.mergeWith(csp(paramJCDoWhileLoop.cond));
/* 273 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 277 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCWhileLoop), endPos(paramJCWhileLoop));
/* 278 */       localSourceRange.mergeWith(csp(paramJCWhileLoop.cond));
/* 279 */       localSourceRange.mergeWith(csp(paramJCWhileLoop.body));
/* 280 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 284 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCForLoop), endPos(paramJCForLoop));
/* 285 */       localSourceRange.mergeWith(csp(paramJCForLoop.init));
/* 286 */       localSourceRange.mergeWith(csp(paramJCForLoop.cond));
/* 287 */       localSourceRange.mergeWith(csp(paramJCForLoop.step));
/* 288 */       localSourceRange.mergeWith(csp(paramJCForLoop.body));
/* 289 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 293 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCEnhancedForLoop), endPos(paramJCEnhancedForLoop));
/* 294 */       localSourceRange.mergeWith(csp(paramJCEnhancedForLoop.var));
/* 295 */       localSourceRange.mergeWith(csp(paramJCEnhancedForLoop.expr));
/* 296 */       localSourceRange.mergeWith(csp(paramJCEnhancedForLoop.body));
/* 297 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 301 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCLabeledStatement), endPos(paramJCLabeledStatement));
/* 302 */       localSourceRange.mergeWith(csp(paramJCLabeledStatement.body));
/* 303 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 307 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCSwitch), endPos(paramJCSwitch));
/* 308 */       localSourceRange.mergeWith(csp(paramJCSwitch.selector));
/* 309 */       localSourceRange.mergeWith(cspCases(paramJCSwitch.cases));
/* 310 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitCase(JCTree.JCCase paramJCCase) {
/* 314 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCCase), endPos(paramJCCase));
/* 315 */       localSourceRange.mergeWith(csp(paramJCCase.pat));
/* 316 */       localSourceRange.mergeWith(csp(paramJCCase.stats));
/* 317 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 321 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCSynchronized), endPos(paramJCSynchronized));
/* 322 */       localSourceRange.mergeWith(csp(paramJCSynchronized.lock));
/* 323 */       localSourceRange.mergeWith(csp(paramJCSynchronized.body));
/* 324 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTry(JCTree.JCTry paramJCTry) {
/* 328 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCTry), endPos(paramJCTry));
/* 329 */       localSourceRange.mergeWith(csp(paramJCTry.resources));
/* 330 */       localSourceRange.mergeWith(csp(paramJCTry.body));
/* 331 */       localSourceRange.mergeWith(cspCatchers(paramJCTry.catchers));
/* 332 */       localSourceRange.mergeWith(csp(paramJCTry.finalizer));
/* 333 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitCatch(JCTree.JCCatch paramJCCatch) {
/* 337 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCCatch), endPos(paramJCCatch));
/* 338 */       localSourceRange.mergeWith(csp(paramJCCatch.param));
/* 339 */       localSourceRange.mergeWith(csp(paramJCCatch.body));
/* 340 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 344 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCConditional), endPos(paramJCConditional));
/* 345 */       localSourceRange.mergeWith(csp(paramJCConditional.cond));
/* 346 */       localSourceRange.mergeWith(csp(paramJCConditional.truepart));
/* 347 */       localSourceRange.mergeWith(csp(paramJCConditional.falsepart));
/* 348 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitIf(JCTree.JCIf paramJCIf) {
/* 352 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCIf), endPos(paramJCIf));
/* 353 */       localSourceRange.mergeWith(csp(paramJCIf.cond));
/* 354 */       localSourceRange.mergeWith(csp(paramJCIf.thenpart));
/* 355 */       localSourceRange.mergeWith(csp(paramJCIf.elsepart));
/* 356 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/* 360 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCExpressionStatement), endPos(paramJCExpressionStatement));
/* 361 */       localSourceRange.mergeWith(csp(paramJCExpressionStatement.expr));
/* 362 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 366 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCBreak), endPos(paramJCBreak));
/* 367 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 371 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCContinue), endPos(paramJCContinue));
/* 372 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 376 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCReturn), endPos(paramJCReturn));
/* 377 */       localSourceRange.mergeWith(csp(paramJCReturn.expr));
/* 378 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 382 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCThrow), endPos(paramJCThrow));
/* 383 */       localSourceRange.mergeWith(csp(paramJCThrow.expr));
/* 384 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 388 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCAssert), endPos(paramJCAssert));
/* 389 */       localSourceRange.mergeWith(csp(paramJCAssert.cond));
/* 390 */       localSourceRange.mergeWith(csp(paramJCAssert.detail));
/* 391 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 395 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCMethodInvocation), endPos(paramJCMethodInvocation));
/* 396 */       localSourceRange.mergeWith(csp(paramJCMethodInvocation.meth));
/* 397 */       localSourceRange.mergeWith(csp(paramJCMethodInvocation.args));
/* 398 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 402 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCNewClass), endPos(paramJCNewClass));
/* 403 */       localSourceRange.mergeWith(csp(paramJCNewClass.encl));
/* 404 */       localSourceRange.mergeWith(csp(paramJCNewClass.clazz));
/* 405 */       localSourceRange.mergeWith(csp(paramJCNewClass.args));
/* 406 */       localSourceRange.mergeWith(csp(paramJCNewClass.def));
/* 407 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 411 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCNewArray), endPos(paramJCNewArray));
/* 412 */       localSourceRange.mergeWith(csp(paramJCNewArray.elemtype));
/* 413 */       localSourceRange.mergeWith(csp(paramJCNewArray.dims));
/* 414 */       localSourceRange.mergeWith(csp(paramJCNewArray.elems));
/* 415 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitParens(JCTree.JCParens paramJCParens) {
/* 419 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCParens), endPos(paramJCParens));
/* 420 */       localSourceRange.mergeWith(csp(paramJCParens.expr));
/* 421 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 425 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCAssign), endPos(paramJCAssign));
/* 426 */       localSourceRange.mergeWith(csp(paramJCAssign.lhs));
/* 427 */       localSourceRange.mergeWith(csp(paramJCAssign.rhs));
/* 428 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 432 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCAssignOp), endPos(paramJCAssignOp));
/* 433 */       localSourceRange.mergeWith(csp(paramJCAssignOp.lhs));
/* 434 */       localSourceRange.mergeWith(csp(paramJCAssignOp.rhs));
/* 435 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 439 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCUnary), endPos(paramJCUnary));
/* 440 */       localSourceRange.mergeWith(csp(paramJCUnary.arg));
/* 441 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 445 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCBinary), endPos(paramJCBinary));
/* 446 */       localSourceRange.mergeWith(csp(paramJCBinary.lhs));
/* 447 */       localSourceRange.mergeWith(csp(paramJCBinary.rhs));
/* 448 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 452 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCTypeCast), endPos(paramJCTypeCast));
/* 453 */       localSourceRange.mergeWith(csp(paramJCTypeCast.clazz));
/* 454 */       localSourceRange.mergeWith(csp(paramJCTypeCast.expr));
/* 455 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 459 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCInstanceOf), endPos(paramJCInstanceOf));
/* 460 */       localSourceRange.mergeWith(csp(paramJCInstanceOf.expr));
/* 461 */       localSourceRange.mergeWith(csp(paramJCInstanceOf.clazz));
/* 462 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 466 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCArrayAccess), endPos(paramJCArrayAccess));
/* 467 */       localSourceRange.mergeWith(csp(paramJCArrayAccess.indexed));
/* 468 */       localSourceRange.mergeWith(csp(paramJCArrayAccess.index));
/* 469 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 473 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCFieldAccess), endPos(paramJCFieldAccess));
/* 474 */       localSourceRange.mergeWith(csp(paramJCFieldAccess.selected));
/* 475 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 479 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCIdent), endPos(paramJCIdent));
/* 480 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/* 484 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCLiteral), endPos(paramJCLiteral));
/* 485 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/* 489 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCPrimitiveTypeTree), endPos(paramJCPrimitiveTypeTree));
/* 490 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/* 494 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCArrayTypeTree), endPos(paramJCArrayTypeTree));
/* 495 */       localSourceRange.mergeWith(csp(paramJCArrayTypeTree.elemtype));
/* 496 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/* 500 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCTypeApply), endPos(paramJCTypeApply));
/* 501 */       localSourceRange.mergeWith(csp(paramJCTypeApply.clazz));
/* 502 */       localSourceRange.mergeWith(csp(paramJCTypeApply.arguments));
/* 503 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitLetExpr(JCTree.LetExpr paramLetExpr)
/*     */     {
/* 508 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramLetExpr), endPos(paramLetExpr));
/* 509 */       localSourceRange.mergeWith(csp(paramLetExpr.defs));
/* 510 */       localSourceRange.mergeWith(csp(paramLetExpr.expr));
/* 511 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 515 */       CRTable.SourceRange localSourceRange = new CRTable.SourceRange(startPos(paramJCTypeParameter), endPos(paramJCTypeParameter));
/* 516 */       localSourceRange.mergeWith(csp(paramJCTypeParameter.bounds));
/* 517 */       this.result = localSourceRange;
/*     */     }
/*     */ 
/*     */     public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/* 521 */       this.result = null;
/*     */     }
/*     */ 
/*     */     public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/* 525 */       this.result = null;
/*     */     }
/*     */ 
/*     */     public void visitTree(JCTree paramJCTree) {
/* 529 */       Assert.error();
/*     */     }
/*     */ 
/*     */     public int startPos(JCTree paramJCTree)
/*     */     {
/* 535 */       if (paramJCTree == null) return -1;
/* 536 */       return TreeInfo.getStartPos(paramJCTree);
/*     */     }
/*     */ 
/*     */     public int endPos(JCTree paramJCTree)
/*     */     {
/* 543 */       if (paramJCTree == null) return -1;
/* 544 */       return TreeInfo.getEndPos(paramJCTree, CRTable.this.endPosTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SourceRange
/*     */   {
/*     */     int startPos;
/*     */     int endPos;
/*     */ 
/*     */     SourceRange()
/*     */     {
/* 593 */       this.startPos = -1;
/* 594 */       this.endPos = -1;
/*     */     }
/*     */ 
/*     */     SourceRange(int paramInt1, int paramInt2)
/*     */     {
/* 599 */       this.startPos = paramInt1;
/* 600 */       this.endPos = paramInt2;
/*     */     }
/*     */ 
/*     */     SourceRange mergeWith(SourceRange paramSourceRange)
/*     */     {
/* 608 */       if (paramSourceRange == null) return this;
/* 609 */       if (this.startPos == -1)
/* 610 */         this.startPos = paramSourceRange.startPos;
/* 611 */       else if (paramSourceRange.startPos != -1)
/* 612 */         this.startPos = (this.startPos < paramSourceRange.startPos ? this.startPos : paramSourceRange.startPos);
/* 613 */       if (this.endPos == -1)
/* 614 */         this.endPos = paramSourceRange.endPos;
/* 615 */       else if (paramSourceRange.endPos != -1)
/* 616 */         this.endPos = (this.endPos > paramSourceRange.endPos ? this.endPos : paramSourceRange.endPos);
/* 617 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.CRTable
 * JD-Core Version:    0.6.2
 */