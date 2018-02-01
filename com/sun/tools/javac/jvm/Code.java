/*      */ package com.sun.tools.javac.jvm;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.JCPrimitiveType;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.UniqueType;
/*      */ import com.sun.tools.javac.util.ArrayUtils;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Bits;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Position.LineMap;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ public class Code
/*      */ {
/*      */   public final boolean debugCode;
/*      */   public final boolean needStackMap;
/*      */   final Types types;
/*      */   final Symtab syms;
/*   79 */   public int max_stack = 0;
/*      */ 
/*   83 */   public int max_locals = 0;
/*      */ 
/*   87 */   public byte[] code = new byte[64];
/*      */ 
/*   91 */   public int cp = 0;
/*      */ 
/*  115 */   ListBuffer<char[]> catchInfo = new ListBuffer();
/*      */ 
/*  120 */   com.sun.tools.javac.util.List<char[]> lineInfo = com.sun.tools.javac.util.List.nil();
/*      */   public CRTable crt;
/*      */   public boolean fatcode;
/*  134 */   private boolean alive = true;
/*      */   State state;
/*  143 */   private boolean fixedPc = false;
/*      */ 
/*  147 */   public int nextreg = 0;
/*      */ 
/*  152 */   Chain pendingJumps = null;
/*      */ 
/*  159 */   int pendingStatPos = -1;
/*      */ 
/*  162 */   boolean pendingStackMap = false;
/*      */   StackMapFormat stackMap;
/*      */   boolean varDebugInfo;
/*      */   boolean lineDebugInfo;
/*      */   Position.LineMap lineMap;
/*      */   final Pool pool;
/*      */   final Symbol.MethodSymbol meth;
/* 1230 */   StackMapFrame[] stackMapBuffer = null;
/*      */ 
/* 1233 */   ClassWriter.StackMapTableFrame[] stackMapTableBuffer = null;
/* 1234 */   int stackMapBufferSize = 0;
/*      */ 
/* 1237 */   int lastStackMapPC = -1;
/*      */ 
/* 1240 */   StackMapFrame lastFrame = null;
/*      */ 
/* 1243 */   StackMapFrame frameBeforeLast = null;
/*      */ 
/* 1871 */   static final Type jsrReturnValue = new Type.JCPrimitiveType(TypeTag.INT, null);
/*      */   LocalVar[] lvar;
/*      */   LocalVar[] varBuffer;
/*      */   int varBufferSize;
/*      */ 
/*      */   public boolean checkLimits(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Log paramLog)
/*      */   {
/*   97 */     if (this.cp > 65535) {
/*   98 */       paramLog.error(paramDiagnosticPosition, "limit.code", new Object[0]);
/*   99 */       return true;
/*      */     }
/*  101 */     if (this.max_locals > 65535) {
/*  102 */       paramLog.error(paramDiagnosticPosition, "limit.locals", new Object[0]);
/*  103 */       return true;
/*      */     }
/*  105 */     if (this.max_stack > 65535) {
/*  106 */       paramLog.error(paramDiagnosticPosition, "limit.stack", new Object[0]);
/*  107 */       return true;
/*      */     }
/*  109 */     return false;
/*      */   }
/*      */ 
/*      */   public Code(Symbol.MethodSymbol paramMethodSymbol, boolean paramBoolean1, Position.LineMap paramLineMap, boolean paramBoolean2, StackMapFormat paramStackMapFormat, boolean paramBoolean3, CRTable paramCRTable, Symtab paramSymtab, Types paramTypes, Pool paramPool)
/*      */   {
/*  198 */     this.meth = paramMethodSymbol;
/*  199 */     this.fatcode = paramBoolean1;
/*  200 */     this.lineMap = paramLineMap;
/*  201 */     this.lineDebugInfo = (paramLineMap != null);
/*  202 */     this.varDebugInfo = paramBoolean2;
/*  203 */     this.crt = paramCRTable;
/*  204 */     this.syms = paramSymtab;
/*  205 */     this.types = paramTypes;
/*  206 */     this.debugCode = paramBoolean3;
/*  207 */     this.stackMap = paramStackMapFormat;
/*  208 */     switch (paramStackMapFormat) {
/*      */     case CLDC:
/*      */     case JSR202:
/*  211 */       this.needStackMap = true;
/*  212 */       break;
/*      */     default:
/*  214 */       this.needStackMap = false;
/*      */     }
/*  216 */     this.state = new State();
/*  217 */     this.lvar = new LocalVar[20];
/*  218 */     this.pool = paramPool;
/*      */   }
/*      */ 
/*      */   public static int typecode(Type paramType)
/*      */   {
/*  230 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 1:
/*  231 */       return 5;
/*      */     case 2:
/*  232 */       return 7;
/*      */     case 3:
/*  233 */       return 6;
/*      */     case 4:
/*  234 */       return 0;
/*      */     case 5:
/*  235 */       return 1;
/*      */     case 6:
/*  236 */       return 2;
/*      */     case 7:
/*  237 */       return 3;
/*      */     case 8:
/*  238 */       return 5;
/*      */     case 9:
/*  239 */       return 8;
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*  247 */       return 4; }
/*  248 */     throw new AssertionError("typecode " + paramType.getTag());
/*      */   }
/*      */ 
/*      */   public static int truncate(int paramInt)
/*      */   {
/*  255 */     switch (paramInt) { case 5:
/*      */     case 6:
/*      */     case 7:
/*  256 */       return 0; }
/*  257 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static int width(int paramInt)
/*      */   {
/*  264 */     switch (paramInt) { case 1:
/*      */     case 3:
/*  265 */       return 2;
/*      */     case 8:
/*  266 */       return 0; }
/*  267 */     return 1;
/*      */   }
/*      */ 
/*      */   public static int width(Type paramType)
/*      */   {
/*  272 */     return paramType == null ? 1 : width(typecode(paramType));
/*      */   }
/*      */ 
/*      */   public static int width(com.sun.tools.javac.util.List<Type> paramList)
/*      */   {
/*  278 */     int i = 0;
/*  279 */     for (Object localObject = paramList; ((com.sun.tools.javac.util.List)localObject).nonEmpty(); localObject = ((com.sun.tools.javac.util.List)localObject).tail)
/*  280 */       i += width((Type)((com.sun.tools.javac.util.List)localObject).head);
/*  281 */     return i;
/*      */   }
/*      */ 
/*      */   public static int arraycode(Type paramType)
/*      */   {
/*  287 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 1:
/*  288 */       return 8;
/*      */     case 8:
/*  289 */       return 4;
/*      */     case 2:
/*  290 */       return 9;
/*      */     case 3:
/*  291 */       return 5;
/*      */     case 4:
/*  292 */       return 10;
/*      */     case 5:
/*  293 */       return 11;
/*      */     case 6:
/*  294 */       return 6;
/*      */     case 7:
/*  295 */       return 7;
/*      */     case 10:
/*  296 */       return 0;
/*      */     case 11:
/*  297 */       return 1;
/*  298 */     case 9: } throw new AssertionError("arraycode " + paramType);
/*      */   }
/*      */ 
/*      */   public int curCP()
/*      */   {
/*  316 */     if (this.pendingJumps != null) {
/*  317 */       resolvePending();
/*      */     }
/*  319 */     if (this.pendingStatPos != -1) {
/*  320 */       markStatBegin();
/*      */     }
/*  322 */     this.fixedPc = true;
/*  323 */     return this.cp;
/*      */   }
/*      */ 
/*      */   private void emit1(int paramInt)
/*      */   {
/*  329 */     if (!this.alive) return;
/*  330 */     this.code = ArrayUtils.ensureCapacity(this.code, this.cp);
/*  331 */     this.code[(this.cp++)] = ((byte)paramInt);
/*      */   }
/*      */ 
/*      */   private void emit2(int paramInt)
/*      */   {
/*  337 */     if (!this.alive) return;
/*  338 */     if (this.cp + 2 > this.code.length) {
/*  339 */       emit1(paramInt >> 8);
/*  340 */       emit1(paramInt);
/*      */     } else {
/*  342 */       this.code[(this.cp++)] = ((byte)(paramInt >> 8));
/*  343 */       this.code[(this.cp++)] = ((byte)paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emit4(int paramInt)
/*      */   {
/*  350 */     if (!this.alive) return;
/*  351 */     if (this.cp + 4 > this.code.length) {
/*  352 */       emit1(paramInt >> 24);
/*  353 */       emit1(paramInt >> 16);
/*  354 */       emit1(paramInt >> 8);
/*  355 */       emit1(paramInt);
/*      */     } else {
/*  357 */       this.code[(this.cp++)] = ((byte)(paramInt >> 24));
/*  358 */       this.code[(this.cp++)] = ((byte)(paramInt >> 16));
/*  359 */       this.code[(this.cp++)] = ((byte)(paramInt >> 8));
/*  360 */       this.code[(this.cp++)] = ((byte)paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void emitop(int paramInt)
/*      */   {
/*  367 */     if (this.pendingJumps != null) resolvePending();
/*  368 */     if (this.alive) {
/*  369 */       if (this.pendingStatPos != -1)
/*  370 */         markStatBegin();
/*  371 */       if (this.pendingStackMap) {
/*  372 */         this.pendingStackMap = false;
/*  373 */         emitStackMap();
/*      */       }
/*  375 */       if (this.debugCode) {
/*  376 */         System.err.println("emit@" + this.cp + " stack=" + this.state.stacksize + ": " + 
/*  378 */           mnem(paramInt));
/*      */       }
/*      */ 
/*  379 */       emit1(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   void postop() {
/*  384 */     Assert.check((this.alive) || (this.state.stacksize == 0));
/*      */   }
/*      */ 
/*      */   public void emitLdc(int paramInt)
/*      */   {
/*  390 */     if (paramInt <= 255) {
/*  391 */       emitop1(18, paramInt);
/*      */     }
/*      */     else
/*  394 */       emitop2(19, paramInt);
/*      */   }
/*      */ 
/*      */   public void emitMultianewarray(int paramInt1, int paramInt2, Type paramType)
/*      */   {
/*  401 */     emitop(197);
/*  402 */     if (!this.alive) return;
/*  403 */     emit2(paramInt2);
/*  404 */     emit1(paramInt1);
/*  405 */     this.state.pop(paramInt1);
/*  406 */     this.state.push(paramType);
/*      */   }
/*      */ 
/*      */   public void emitNewarray(int paramInt, Type paramType)
/*      */   {
/*  412 */     emitop(188);
/*  413 */     if (!this.alive) return;
/*  414 */     emit1(paramInt);
/*  415 */     this.state.pop(1);
/*  416 */     this.state.push(paramType);
/*      */   }
/*      */ 
/*      */   public void emitAnewarray(int paramInt, Type paramType)
/*      */   {
/*  422 */     emitop(189);
/*  423 */     if (!this.alive) return;
/*  424 */     emit2(paramInt);
/*  425 */     this.state.pop(1);
/*  426 */     this.state.push(paramType);
/*      */   }
/*      */ 
/*      */   public void emitInvokeinterface(int paramInt, Type paramType)
/*      */   {
/*  432 */     int i = width(paramType.getParameterTypes());
/*  433 */     emitop(185);
/*  434 */     if (!this.alive) return;
/*  435 */     emit2(paramInt);
/*  436 */     emit1(i + 1);
/*  437 */     emit1(0);
/*  438 */     this.state.pop(i + 1);
/*  439 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */ 
/*      */   public void emitInvokespecial(int paramInt, Type paramType)
/*      */   {
/*  445 */     int i = width(paramType.getParameterTypes());
/*  446 */     emitop(183);
/*  447 */     if (!this.alive) return;
/*  448 */     emit2(paramInt);
/*  449 */     Symbol localSymbol = (Symbol)this.pool.pool[paramInt];
/*  450 */     this.state.pop(i);
/*  451 */     if (localSymbol.isConstructor())
/*  452 */       this.state.markInitialized((UninitializedType)this.state.peek());
/*  453 */     this.state.pop(1);
/*  454 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */ 
/*      */   public void emitInvokestatic(int paramInt, Type paramType)
/*      */   {
/*  460 */     int i = width(paramType.getParameterTypes());
/*  461 */     emitop(184);
/*  462 */     if (!this.alive) return;
/*  463 */     emit2(paramInt);
/*  464 */     this.state.pop(i);
/*  465 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */ 
/*      */   public void emitInvokevirtual(int paramInt, Type paramType)
/*      */   {
/*  471 */     int i = width(paramType.getParameterTypes());
/*  472 */     emitop(182);
/*  473 */     if (!this.alive) return;
/*  474 */     emit2(paramInt);
/*  475 */     this.state.pop(i + 1);
/*  476 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */ 
/*      */   public void emitInvokedynamic(int paramInt, Type paramType)
/*      */   {
/*  482 */     int i = width(paramType.getParameterTypes());
/*  483 */     emitop(186);
/*  484 */     if (!this.alive) return;
/*  485 */     emit2(paramInt);
/*  486 */     emit2(0);
/*  487 */     this.state.pop(i);
/*  488 */     this.state.push(paramType.getReturnType());
/*      */   }
/*      */ 
/*      */   public void emitop0(int paramInt)
/*      */   {
/*  494 */     emitop(paramInt);
/*  495 */     if (!this.alive)
/*      */       return;
/*      */     Type localType1;
/*      */     Type localType2;
/*      */     Type localType3;
/*  496 */     switch (paramInt) {
/*      */     case 50:
/*  498 */       this.state.pop(1);
/*  499 */       localType1 = this.state.stack[(this.state.stacksize - 1)];
/*  500 */       this.state.pop(1);
/*      */ 
/*  505 */       localType2 = localType1.hasTag(TypeTag.BOT) ? this.syms.objectType : this.types
/*  505 */         .erasure(this.types
/*  505 */         .elemtype(localType1));
/*      */ 
/*  506 */       this.state.push(localType2);
/*  507 */       break;
/*      */     case 167:
/*  509 */       markDead();
/*  510 */       break;
/*      */     case 0:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*  516 */       break;
/*      */     case 1:
/*  518 */       this.state.push(this.syms.botType);
/*  519 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*  531 */       this.state.push(this.syms.intType);
/*  532 */       break;
/*      */     case 9:
/*      */     case 10:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*  539 */       this.state.push(this.syms.longType);
/*  540 */       break;
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*  548 */       this.state.push(this.syms.floatType);
/*  549 */       break;
/*      */     case 14:
/*      */     case 15:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*  556 */       this.state.push(this.syms.doubleType);
/*  557 */       break;
/*      */     case 42:
/*  559 */       this.state.push(this.lvar[0].sym.type);
/*  560 */       break;
/*      */     case 43:
/*  562 */       this.state.push(this.lvar[1].sym.type);
/*  563 */       break;
/*      */     case 44:
/*  565 */       this.state.push(this.lvar[2].sym.type);
/*  566 */       break;
/*      */     case 45:
/*  568 */       this.state.push(this.lvar[3].sym.type);
/*  569 */       break;
/*      */     case 46:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*  574 */       this.state.pop(2);
/*  575 */       this.state.push(this.syms.intType);
/*  576 */       break;
/*      */     case 47:
/*  578 */       this.state.pop(2);
/*  579 */       this.state.push(this.syms.longType);
/*  580 */       break;
/*      */     case 48:
/*  582 */       this.state.pop(2);
/*  583 */       this.state.push(this.syms.floatType);
/*  584 */       break;
/*      */     case 49:
/*  586 */       this.state.pop(2);
/*  587 */       this.state.push(this.syms.doubleType);
/*  588 */       break;
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 87:
/*      */     case 121:
/*      */     case 123:
/*      */     case 125:
/*  605 */       this.state.pop(1);
/*  606 */       break;
/*      */     case 172:
/*      */     case 174:
/*      */     case 176:
/*  610 */       Assert.check(this.state.nlocks == 0);
/*  611 */       this.state.pop(1);
/*  612 */       markDead();
/*  613 */       break;
/*      */     case 191:
/*  615 */       this.state.pop(1);
/*  616 */       markDead();
/*  617 */       break;
/*      */     case 63:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 88:
/*  627 */       this.state.pop(2);
/*  628 */       break;
/*      */     case 173:
/*      */     case 175:
/*  631 */       Assert.check(this.state.nlocks == 0);
/*  632 */       this.state.pop(2);
/*  633 */       markDead();
/*  634 */       break;
/*      */     case 89:
/*  636 */       this.state.push(this.state.stack[(this.state.stacksize - 1)]);
/*  637 */       break;
/*      */     case 177:
/*  639 */       Assert.check(this.state.nlocks == 0);
/*  640 */       markDead();
/*  641 */       break;
/*      */     case 190:
/*  643 */       this.state.pop(1);
/*  644 */       this.state.push(this.syms.intType);
/*  645 */       break;
/*      */     case 96:
/*      */     case 100:
/*      */     case 104:
/*      */     case 108:
/*      */     case 112:
/*      */     case 120:
/*      */     case 122:
/*      */     case 124:
/*      */     case 126:
/*      */     case 128:
/*      */     case 130:
/*  657 */       this.state.pop(1);
/*      */ 
/*  660 */       break;
/*      */     case 83:
/*  662 */       this.state.pop(3);
/*  663 */       break;
/*      */     case 97:
/*      */     case 101:
/*      */     case 105:
/*      */     case 109:
/*      */     case 113:
/*      */     case 127:
/*      */     case 129:
/*      */     case 131:
/*  672 */       this.state.pop(2);
/*  673 */       break;
/*      */     case 148:
/*  675 */       this.state.pop(4);
/*  676 */       this.state.push(this.syms.intType);
/*  677 */       break;
/*      */     case 136:
/*  679 */       this.state.pop(2);
/*  680 */       this.state.push(this.syms.intType);
/*  681 */       break;
/*      */     case 133:
/*  683 */       this.state.pop(1);
/*  684 */       this.state.push(this.syms.longType);
/*  685 */       break;
/*      */     case 134:
/*  687 */       this.state.pop(1);
/*  688 */       this.state.push(this.syms.floatType);
/*  689 */       break;
/*      */     case 135:
/*  691 */       this.state.pop(1);
/*  692 */       this.state.push(this.syms.doubleType);
/*  693 */       break;
/*      */     case 137:
/*  695 */       this.state.pop(2);
/*  696 */       this.state.push(this.syms.floatType);
/*  697 */       break;
/*      */     case 138:
/*  699 */       this.state.pop(2);
/*  700 */       this.state.push(this.syms.doubleType);
/*  701 */       break;
/*      */     case 139:
/*  703 */       this.state.pop(1);
/*  704 */       this.state.push(this.syms.intType);
/*  705 */       break;
/*      */     case 140:
/*  707 */       this.state.pop(1);
/*  708 */       this.state.push(this.syms.longType);
/*  709 */       break;
/*      */     case 141:
/*  711 */       this.state.pop(1);
/*  712 */       this.state.push(this.syms.doubleType);
/*  713 */       break;
/*      */     case 142:
/*  715 */       this.state.pop(2);
/*  716 */       this.state.push(this.syms.intType);
/*  717 */       break;
/*      */     case 143:
/*  719 */       this.state.pop(2);
/*  720 */       this.state.push(this.syms.longType);
/*  721 */       break;
/*      */     case 144:
/*  723 */       this.state.pop(2);
/*  724 */       this.state.push(this.syms.floatType);
/*  725 */       break;
/*      */     case 170:
/*      */     case 171:
/*  728 */       this.state.pop(1);
/*      */ 
/*  730 */       break;
/*      */     case 90:
/*  732 */       localType1 = this.state.pop1();
/*  733 */       localType2 = this.state.pop1();
/*  734 */       this.state.push(localType1);
/*  735 */       this.state.push(localType2);
/*  736 */       this.state.push(localType1);
/*  737 */       break;
/*      */     case 84:
/*  740 */       this.state.pop(3);
/*  741 */       break;
/*      */     case 145:
/*      */     case 146:
/*      */     case 147:
/*  745 */       break;
/*      */     case 98:
/*      */     case 102:
/*      */     case 106:
/*      */     case 110:
/*      */     case 114:
/*  751 */       this.state.pop(1);
/*  752 */       break;
/*      */     case 79:
/*      */     case 81:
/*      */     case 85:
/*      */     case 86:
/*  757 */       this.state.pop(3);
/*  758 */       break;
/*      */     case 80:
/*      */     case 82:
/*  761 */       this.state.pop(4);
/*  762 */       break;
/*      */     case 92:
/*  764 */       if (this.state.stack[(this.state.stacksize - 1)] != null) {
/*  765 */         localType1 = this.state.pop1();
/*  766 */         localType2 = this.state.pop1();
/*  767 */         this.state.push(localType2);
/*  768 */         this.state.push(localType1);
/*  769 */         this.state.push(localType2);
/*  770 */         this.state.push(localType1);
/*      */       } else {
/*  772 */         localType1 = this.state.pop2();
/*  773 */         this.state.push(localType1);
/*  774 */         this.state.push(localType1);
/*      */       }
/*  776 */       break;
/*      */     case 93:
/*  778 */       if (this.state.stack[(this.state.stacksize - 1)] != null) {
/*  779 */         localType1 = this.state.pop1();
/*  780 */         localType2 = this.state.pop1();
/*  781 */         localType3 = this.state.pop1();
/*  782 */         this.state.push(localType2);
/*  783 */         this.state.push(localType1);
/*  784 */         this.state.push(localType3);
/*  785 */         this.state.push(localType2);
/*  786 */         this.state.push(localType1);
/*      */       } else {
/*  788 */         localType1 = this.state.pop2();
/*  789 */         localType2 = this.state.pop1();
/*  790 */         this.state.push(localType1);
/*  791 */         this.state.push(localType2);
/*  792 */         this.state.push(localType1);
/*      */       }
/*  794 */       break;
/*      */     case 94:
/*  796 */       if (this.state.stack[(this.state.stacksize - 1)] != null) {
/*  797 */         localType1 = this.state.pop1();
/*  798 */         localType2 = this.state.pop1();
/*  799 */         if (this.state.stack[(this.state.stacksize - 1)] != null)
/*      */         {
/*  801 */           localType3 = this.state.pop1();
/*  802 */           Type localType4 = this.state.pop1();
/*  803 */           this.state.push(localType2);
/*  804 */           this.state.push(localType1);
/*  805 */           this.state.push(localType4);
/*  806 */           this.state.push(localType3);
/*  807 */           this.state.push(localType2);
/*  808 */           this.state.push(localType1);
/*      */         }
/*      */         else {
/*  811 */           localType3 = this.state.pop2();
/*  812 */           this.state.push(localType2);
/*  813 */           this.state.push(localType1);
/*  814 */           this.state.push(localType3);
/*  815 */           this.state.push(localType2);
/*  816 */           this.state.push(localType1);
/*      */         }
/*      */       } else {
/*  819 */         localType1 = this.state.pop2();
/*  820 */         if (this.state.stack[(this.state.stacksize - 1)] != null)
/*      */         {
/*  822 */           localType2 = this.state.pop1();
/*  823 */           localType3 = this.state.pop1();
/*  824 */           this.state.push(localType1);
/*  825 */           this.state.push(localType3);
/*  826 */           this.state.push(localType2);
/*  827 */           this.state.push(localType1);
/*      */         }
/*      */         else {
/*  830 */           localType2 = this.state.pop2();
/*  831 */           this.state.push(localType1);
/*  832 */           this.state.push(localType2);
/*  833 */           this.state.push(localType1);
/*      */         }
/*      */       }
/*  836 */       break;
/*      */     case 91:
/*  838 */       localType1 = this.state.pop1();
/*  839 */       if (this.state.stack[(this.state.stacksize - 1)] != null)
/*      */       {
/*  841 */         localType2 = this.state.pop1();
/*  842 */         localType3 = this.state.pop1();
/*  843 */         this.state.push(localType1);
/*  844 */         this.state.push(localType3);
/*  845 */         this.state.push(localType2);
/*  846 */         this.state.push(localType1);
/*      */       }
/*      */       else {
/*  849 */         localType2 = this.state.pop2();
/*  850 */         this.state.push(localType1);
/*  851 */         this.state.push(localType2);
/*  852 */         this.state.push(localType1);
/*      */       }
/*      */ 
/*  855 */       break;
/*      */     case 149:
/*      */     case 150:
/*  858 */       this.state.pop(2);
/*  859 */       this.state.push(this.syms.intType);
/*  860 */       break;
/*      */     case 151:
/*      */     case 152:
/*  863 */       this.state.pop(4);
/*  864 */       this.state.push(this.syms.intType);
/*  865 */       break;
/*      */     case 95:
/*  867 */       localType1 = this.state.pop1();
/*  868 */       localType2 = this.state.pop1();
/*  869 */       this.state.push(localType1);
/*  870 */       this.state.push(localType2);
/*  871 */       break;
/*      */     case 99:
/*      */     case 103:
/*      */     case 107:
/*      */     case 111:
/*      */     case 115:
/*  878 */       this.state.pop(2);
/*  879 */       break;
/*      */     case 169:
/*  881 */       markDead();
/*  882 */       break;
/*      */     case 196:
/*  885 */       return;
/*      */     case 194:
/*      */     case 195:
/*  888 */       this.state.pop(1);
/*  889 */       break;
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
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 132:
/*      */     case 153:
/*      */     case 154:
/*      */     case 155:
/*      */     case 156:
/*      */     case 157:
/*      */     case 158:
/*      */     case 159:
/*      */     case 160:
/*      */     case 161:
/*      */     case 162:
/*      */     case 163:
/*      */     case 164:
/*      */     case 165:
/*      */     case 166:
/*      */     case 168:
/*      */     case 178:
/*      */     case 179:
/*      */     case 180:
/*      */     case 181:
/*      */     case 182:
/*      */     case 183:
/*      */     case 184:
/*      */     case 185:
/*      */     case 186:
/*      */     case 187:
/*      */     case 188:
/*      */     case 189:
/*      */     case 192:
/*      */     case 193:
/*      */     default:
/*  892 */       throw new AssertionError(mnem(paramInt));
/*      */     }
/*  894 */     postop();
/*      */   }
/*      */ 
/*      */   public void emitop1(int paramInt1, int paramInt2)
/*      */   {
/*  900 */     emitop(paramInt1);
/*  901 */     if (!this.alive) return;
/*  902 */     emit1(paramInt2);
/*  903 */     switch (paramInt1) {
/*      */     case 16:
/*  905 */       this.state.push(this.syms.intType);
/*  906 */       break;
/*      */     case 18:
/*  908 */       this.state.push(typeForPool(this.pool.pool[paramInt2]));
/*  909 */       break;
/*      */     default:
/*  911 */       throw new AssertionError(mnem(paramInt1));
/*      */     }
/*  913 */     postop();
/*      */   }
/*      */ 
/*      */   private Type typeForPool(Object paramObject)
/*      */   {
/*  918 */     if ((paramObject instanceof Integer)) return this.syms.intType;
/*  919 */     if ((paramObject instanceof Float)) return this.syms.floatType;
/*  920 */     if ((paramObject instanceof String)) return this.syms.stringType;
/*  921 */     if ((paramObject instanceof Long)) return this.syms.longType;
/*  922 */     if ((paramObject instanceof Double)) return this.syms.doubleType;
/*  923 */     if ((paramObject instanceof Symbol.ClassSymbol)) return this.syms.classType;
/*  924 */     if ((paramObject instanceof Pool.MethodHandle)) return this.syms.methodHandleType;
/*  925 */     if ((paramObject instanceof Types.UniqueType)) return typeForPool(((Types.UniqueType)paramObject).type);
/*  926 */     if ((paramObject instanceof Type)) {
/*  927 */       Type localType = ((Type)paramObject).unannotatedType();
/*      */ 
/*  929 */       if ((localType instanceof Type.ArrayType)) return this.syms.classType;
/*  930 */       if ((localType instanceof Type.MethodType)) return this.syms.methodTypeType;
/*      */     }
/*  932 */     throw new AssertionError("Invalid type of constant pool entry: " + paramObject.getClass());
/*      */   }
/*      */ 
/*      */   public void emitop1w(int paramInt1, int paramInt2)
/*      */   {
/*  939 */     if (paramInt2 > 255) {
/*  940 */       emitop(196);
/*  941 */       emitop(paramInt1);
/*  942 */       emit2(paramInt2);
/*      */     } else {
/*  944 */       emitop(paramInt1);
/*  945 */       emit1(paramInt2);
/*      */     }
/*  947 */     if (!this.alive) return;
/*  948 */     switch (paramInt1) {
/*      */     case 21:
/*  950 */       this.state.push(this.syms.intType);
/*  951 */       break;
/*      */     case 22:
/*  953 */       this.state.push(this.syms.longType);
/*  954 */       break;
/*      */     case 23:
/*  956 */       this.state.push(this.syms.floatType);
/*  957 */       break;
/*      */     case 24:
/*  959 */       this.state.push(this.syms.doubleType);
/*  960 */       break;
/*      */     case 25:
/*  962 */       this.state.push(this.lvar[paramInt2].sym.type);
/*  963 */       break;
/*      */     case 55:
/*      */     case 57:
/*  966 */       this.state.pop(2);
/*  967 */       break;
/*      */     case 54:
/*      */     case 56:
/*      */     case 58:
/*  971 */       this.state.pop(1);
/*  972 */       break;
/*      */     case 169:
/*  974 */       markDead();
/*  975 */       break;
/*      */     default:
/*  977 */       throw new AssertionError(mnem(paramInt1));
/*      */     }
/*  979 */     postop();
/*      */   }
/*      */ 
/*      */   public void emitop1w(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  986 */     if ((paramInt2 > 255) || (paramInt3 < -128) || (paramInt3 > 127)) {
/*  987 */       emitop(196);
/*  988 */       emitop(paramInt1);
/*  989 */       emit2(paramInt2);
/*  990 */       emit2(paramInt3);
/*      */     } else {
/*  992 */       emitop(paramInt1);
/*  993 */       emit1(paramInt2);
/*  994 */       emit1(paramInt3);
/*      */     }
/*  996 */     if (!this.alive) return;
/*  997 */     switch (paramInt1) {
/*      */     case 132:
/*  999 */       break;
/*      */     default:
/* 1001 */       throw new AssertionError(mnem(paramInt1));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emitop2(int paramInt1, int paramInt2)
/*      */   {
/* 1008 */     emitop(paramInt1);
/* 1009 */     if (!this.alive) return;
/* 1010 */     emit2(paramInt2);
/* 1011 */     switch (paramInt1) {
/*      */     case 178:
/* 1013 */       this.state.push(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/* 1014 */       break;
/*      */     case 179:
/* 1016 */       this.state.pop(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/* 1017 */       break;
/*      */     case 187:
/*      */       Object localObject1;
/* 1020 */       if ((this.pool.pool[paramInt2] instanceof Types.UniqueType))
/*      */       {
/* 1024 */         localObject1 = ((Types.UniqueType)this.pool.pool[paramInt2]).type.tsym;
/*      */       }
/* 1026 */       else localObject1 = (Symbol)this.pool.pool[paramInt2];
/*      */ 
/* 1028 */       this.state.push(UninitializedType.uninitializedObject(((Symbol)localObject1).erasure(this.types), this.cp - 3));
/* 1029 */       break;
/*      */     case 17:
/* 1031 */       this.state.push(this.syms.intType);
/* 1032 */       break;
/*      */     case 153:
/*      */     case 154:
/*      */     case 155:
/*      */     case 156:
/*      */     case 157:
/*      */     case 158:
/*      */     case 198:
/*      */     case 199:
/* 1041 */       this.state.pop(1);
/* 1042 */       break;
/*      */     case 159:
/*      */     case 160:
/*      */     case 161:
/*      */     case 162:
/*      */     case 163:
/*      */     case 164:
/*      */     case 165:
/*      */     case 166:
/* 1051 */       this.state.pop(2);
/* 1052 */       break;
/*      */     case 167:
/* 1054 */       markDead();
/* 1055 */       break;
/*      */     case 181:
/* 1057 */       this.state.pop(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/* 1058 */       this.state.pop(1);
/* 1059 */       break;
/*      */     case 180:
/* 1061 */       this.state.pop(1);
/* 1062 */       this.state.push(((Symbol)this.pool.pool[paramInt2]).erasure(this.types));
/* 1063 */       break;
/*      */     case 192:
/* 1065 */       this.state.pop(1);
/* 1066 */       Object localObject2 = this.pool.pool[paramInt2];
/*      */ 
/* 1069 */       Type localType = (localObject2 instanceof Symbol) ? ((Symbol)localObject2)
/* 1068 */         .erasure(this.types) : 
/* 1068 */         this.types
/* 1069 */         .erasure(((Types.UniqueType)localObject2).type);
/*      */ 
/* 1070 */       this.state.push(localType);
/* 1071 */       break;
/*      */     case 20:
/* 1073 */       this.state.push(typeForPool(this.pool.pool[paramInt2]));
/* 1074 */       break;
/*      */     case 193:
/* 1076 */       this.state.pop(1);
/* 1077 */       this.state.push(this.syms.intType);
/* 1078 */       break;
/*      */     case 19:
/* 1080 */       this.state.push(typeForPool(this.pool.pool[paramInt2]));
/* 1081 */       break;
/*      */     case 168:
/* 1083 */       break;
/*      */     default:
/* 1085 */       throw new AssertionError(mnem(paramInt1));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emitop4(int paramInt1, int paramInt2)
/*      */   {
/* 1093 */     emitop(paramInt1);
/* 1094 */     if (!this.alive) return;
/* 1095 */     emit4(paramInt2);
/* 1096 */     switch (paramInt1) {
/*      */     case 200:
/* 1098 */       markDead();
/* 1099 */       break;
/*      */     case 201:
/* 1101 */       break;
/*      */     default:
/* 1103 */       throw new AssertionError(mnem(paramInt1));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void align(int paramInt)
/*      */   {
/* 1111 */     if (this.alive)
/* 1112 */       while (this.cp % paramInt != 0) emitop0(0);
/*      */   }
/*      */ 
/*      */   private void put1(int paramInt1, int paramInt2)
/*      */   {
/* 1119 */     this.code[paramInt1] = ((byte)paramInt2);
/*      */   }
/*      */ 
/*      */   private void put2(int paramInt1, int paramInt2)
/*      */   {
/* 1127 */     put1(paramInt1, paramInt2 >> 8);
/* 1128 */     put1(paramInt1 + 1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void put4(int paramInt1, int paramInt2)
/*      */   {
/* 1136 */     put1(paramInt1, paramInt2 >> 24);
/* 1137 */     put1(paramInt1 + 1, paramInt2 >> 16);
/* 1138 */     put1(paramInt1 + 2, paramInt2 >> 8);
/* 1139 */     put1(paramInt1 + 3, paramInt2);
/*      */   }
/*      */ 
/*      */   private int get1(int paramInt)
/*      */   {
/* 1145 */     return this.code[paramInt] & 0xFF;
/*      */   }
/*      */ 
/*      */   private int get2(int paramInt)
/*      */   {
/* 1151 */     return get1(paramInt) << 8 | get1(paramInt + 1);
/*      */   }
/*      */ 
/*      */   public int get4(int paramInt)
/*      */   {
/* 1162 */     return get1(paramInt) << 
/* 1159 */       24 | 
/* 1160 */       get1(paramInt + 1) << 
/* 1160 */       16 | 
/* 1161 */       get1(paramInt + 2) << 
/* 1161 */       8 | 
/* 1162 */       get1(paramInt + 3);
/*      */   }
/*      */ 
/*      */   public boolean isAlive()
/*      */   {
/* 1168 */     return (this.alive) || (this.pendingJumps != null);
/*      */   }
/*      */ 
/*      */   public void markDead()
/*      */   {
/* 1174 */     this.alive = false;
/*      */   }
/*      */ 
/*      */   public int entryPoint()
/*      */   {
/* 1180 */     int i = curCP();
/* 1181 */     this.alive = true;
/* 1182 */     this.pendingStackMap = this.needStackMap;
/* 1183 */     return i;
/*      */   }
/*      */ 
/*      */   public int entryPoint(State paramState)
/*      */   {
/* 1190 */     int i = curCP();
/* 1191 */     this.alive = true;
/* 1192 */     State localState = paramState.dup();
/* 1193 */     setDefined(localState.defined);
/* 1194 */     this.state = localState;
/* 1195 */     Assert.check(paramState.stacksize <= this.max_stack);
/* 1196 */     if (this.debugCode) System.err.println("entry point " + paramState);
/* 1197 */     this.pendingStackMap = this.needStackMap;
/* 1198 */     return i;
/*      */   }
/*      */ 
/*      */   public int entryPoint(State paramState, Type paramType)
/*      */   {
/* 1205 */     int i = curCP();
/* 1206 */     this.alive = true;
/* 1207 */     State localState = paramState.dup();
/* 1208 */     setDefined(localState.defined);
/* 1209 */     this.state = localState;
/* 1210 */     Assert.check(paramState.stacksize <= this.max_stack);
/* 1211 */     this.state.push(paramType);
/* 1212 */     if (this.debugCode) System.err.println("entry point " + paramState);
/* 1213 */     this.pendingStackMap = this.needStackMap;
/* 1214 */     return i;
/*      */   }
/*      */ 
/*      */   public void emitStackMap()
/*      */   {
/* 1247 */     int i = curCP();
/* 1248 */     if (!this.needStackMap) return;
/*      */ 
/* 1252 */     switch (this.stackMap) {
/*      */     case CLDC:
/* 1254 */       emitCLDCStackMap(i, getLocalsSize());
/* 1255 */       break;
/*      */     case JSR202:
/* 1257 */       emitStackMapFrame(i, getLocalsSize());
/* 1258 */       break;
/*      */     default:
/* 1260 */       throw new AssertionError("Should have chosen a stackmap format");
/*      */     }
/*      */ 
/* 1263 */     if (this.debugCode) this.state.dump(i); 
/*      */   }
/*      */ 
/*      */   private int getLocalsSize()
/*      */   {
/* 1267 */     int i = 0;
/* 1268 */     for (int j = this.max_locals - 1; j >= 0; j--) {
/* 1269 */       if ((this.state.defined.isMember(j)) && (this.lvar[j] != null)) {
/* 1270 */         i = j + width(this.lvar[j].sym.erasure(this.types));
/* 1271 */         break;
/*      */       }
/*      */     }
/* 1274 */     return i;
/*      */   }
/*      */ 
/*      */   void emitCLDCStackMap(int paramInt1, int paramInt2)
/*      */   {
/* 1279 */     if (this.lastStackMapPC == paramInt1)
/*      */     {
/* 1281 */       this.stackMapBuffer[(--this.stackMapBufferSize)] = null;
/*      */     }
/* 1283 */     this.lastStackMapPC = paramInt1;
/*      */ 
/* 1285 */     if (this.stackMapBuffer == null)
/* 1286 */       this.stackMapBuffer = new StackMapFrame[20];
/*      */     else {
/* 1288 */       this.stackMapBuffer = ((StackMapFrame[])ArrayUtils.ensureCapacity(this.stackMapBuffer, this.stackMapBufferSize));
/*      */     }
/* 1290 */     StackMapFrame localStackMapFrame = this.stackMapBuffer[(this.stackMapBufferSize++)] =  = new StackMapFrame();
/*      */ 
/* 1292 */     localStackMapFrame.pc = paramInt1;
/*      */ 
/* 1294 */     localStackMapFrame.locals = new Type[paramInt2];
/* 1295 */     for (int i = 0; i < paramInt2; i++) {
/* 1296 */       if ((this.state.defined.isMember(i)) && (this.lvar[i] != null)) {
/* 1297 */         Type localType = this.lvar[i].sym.type;
/* 1298 */         if (!(localType instanceof UninitializedType))
/* 1299 */           localType = this.types.erasure(localType);
/* 1300 */         localStackMapFrame.locals[i] = localType;
/*      */       }
/*      */     }
/* 1303 */     localStackMapFrame.stack = new Type[this.state.stacksize];
/* 1304 */     for (i = 0; i < this.state.stacksize; i++)
/* 1305 */       localStackMapFrame.stack[i] = this.state.stack[i];
/*      */   }
/*      */ 
/*      */   void emitStackMapFrame(int paramInt1, int paramInt2) {
/* 1309 */     if (this.lastFrame == null)
/*      */     {
/* 1311 */       this.lastFrame = getInitialFrame();
/* 1312 */     } else if (this.lastFrame.pc == paramInt1)
/*      */     {
/* 1314 */       this.stackMapTableBuffer[(--this.stackMapBufferSize)] = null;
/* 1315 */       this.lastFrame = this.frameBeforeLast;
/* 1316 */       this.frameBeforeLast = null;
/*      */     }
/*      */ 
/* 1319 */     StackMapFrame localStackMapFrame = new StackMapFrame();
/* 1320 */     localStackMapFrame.pc = paramInt1;
/*      */ 
/* 1322 */     int i = 0;
/* 1323 */     Type[] arrayOfType = new Type[paramInt2];
/* 1324 */     for (int j = 0; j < paramInt2; i++) {
/* 1325 */       if ((this.state.defined.isMember(j)) && (this.lvar[j] != null)) {
/* 1326 */         Type localType = this.lvar[j].sym.type;
/* 1327 */         if (!(localType instanceof UninitializedType))
/* 1328 */           localType = this.types.erasure(localType);
/* 1329 */         arrayOfType[j] = localType;
/* 1330 */         if (width(localType) > 1) j++;
/*      */       }
/* 1324 */       j++;
/*      */     }
/*      */ 
/* 1333 */     localStackMapFrame.locals = new Type[i];
/* 1334 */     j = 0; for (int k = 0; j < paramInt2; k++) {
/* 1335 */       Assert.check(k < i);
/* 1336 */       localStackMapFrame.locals[k] = arrayOfType[j];
/* 1337 */       if (width(arrayOfType[j]) > 1) j++;
/* 1334 */       j++;
/*      */     }
/*      */ 
/* 1340 */     j = 0;
/* 1341 */     for (k = 0; k < this.state.stacksize; k++) {
/* 1342 */       if (this.state.stack[k] != null) {
/* 1343 */         j++;
/*      */       }
/*      */     }
/* 1346 */     localStackMapFrame.stack = new Type[j];
/* 1347 */     j = 0;
/* 1348 */     for (k = 0; k < this.state.stacksize; k++) {
/* 1349 */       if (this.state.stack[k] != null) {
/* 1350 */         localStackMapFrame.stack[(j++)] = this.types.erasure(this.state.stack[k]);
/*      */       }
/*      */     }
/*      */ 
/* 1354 */     if (this.stackMapTableBuffer == null)
/* 1355 */       this.stackMapTableBuffer = new ClassWriter.StackMapTableFrame[20];
/*      */     else {
/* 1357 */       this.stackMapTableBuffer = ((ClassWriter.StackMapTableFrame[])ArrayUtils.ensureCapacity(this.stackMapTableBuffer, this.stackMapBufferSize));
/*      */     }
/*      */ 
/* 1361 */     this.stackMapTableBuffer[(this.stackMapBufferSize++)] = 
/* 1362 */       ClassWriter.StackMapTableFrame.getInstance(localStackMapFrame, this.lastFrame.pc, this.lastFrame.locals, this.types);
/*      */ 
/* 1364 */     this.frameBeforeLast = this.lastFrame;
/* 1365 */     this.lastFrame = localStackMapFrame;
/*      */   }
/*      */ 
/*      */   StackMapFrame getInitialFrame() {
/* 1369 */     StackMapFrame localStackMapFrame = new StackMapFrame();
/* 1370 */     com.sun.tools.javac.util.List localList = ((Type.MethodType)this.meth.externalType(this.types)).argtypes;
/* 1371 */     int i = localList.length();
/* 1372 */     int j = 0;
/* 1373 */     if (!this.meth.isStatic()) {
/* 1374 */       localObject = this.meth.owner.type;
/* 1375 */       localStackMapFrame.locals = new Type[i + 1];
/* 1376 */       if ((this.meth.isConstructor()) && (localObject != this.syms.objectType))
/* 1377 */         localStackMapFrame.locals[(j++)] = UninitializedType.uninitializedThis((Type)localObject);
/*      */       else
/* 1379 */         localStackMapFrame.locals[(j++)] = this.types.erasure((Type)localObject);
/*      */     }
/*      */     else {
/* 1382 */       localStackMapFrame.locals = new Type[i];
/*      */     }
/* 1384 */     for (Object localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { Type localType = (Type)((Iterator)localObject).next();
/* 1385 */       localStackMapFrame.locals[(j++)] = this.types.erasure(localType);
/*      */     }
/* 1387 */     localStackMapFrame.pc = -1;
/* 1388 */     localStackMapFrame.stack = null;
/* 1389 */     return localStackMapFrame;
/*      */   }
/*      */ 
/*      */   public static int negate(int paramInt)
/*      */   {
/* 1429 */     if (paramInt == 198) return 199;
/* 1430 */     if (paramInt == 199) return 198;
/* 1431 */     return (paramInt + 1 ^ 0x1) - 1;
/*      */   }
/*      */ 
/*      */   public int emitJump(int paramInt)
/*      */   {
/* 1438 */     if (this.fatcode) {
/* 1439 */       if ((paramInt == 167) || (paramInt == 168)) {
/* 1440 */         emitop4(paramInt + 200 - 167, 0);
/*      */       } else {
/* 1442 */         emitop2(negate(paramInt), 8);
/* 1443 */         emitop4(200, 0);
/* 1444 */         this.alive = true;
/* 1445 */         this.pendingStackMap = this.needStackMap;
/*      */       }
/* 1447 */       return this.cp - 5;
/*      */     }
/* 1449 */     emitop2(paramInt, 0);
/* 1450 */     return this.cp - 3;
/*      */   }
/*      */ 
/*      */   public Chain branch(int paramInt)
/*      */   {
/* 1458 */     Chain localChain = null;
/* 1459 */     if (paramInt == 167) {
/* 1460 */       localChain = this.pendingJumps;
/* 1461 */       this.pendingJumps = null;
/*      */     }
/* 1463 */     if ((paramInt != 168) && (isAlive()))
/*      */     {
/* 1466 */       localChain = new Chain(emitJump(paramInt), localChain, this.state
/* 1466 */         .dup());
/* 1467 */       this.fixedPc = this.fatcode;
/* 1468 */       if (paramInt == 167) this.alive = false;
/*      */     }
/* 1470 */     return localChain;
/*      */   }
/*      */ 
/*      */   public void resolve(Chain paramChain, int paramInt)
/*      */   {
/* 1476 */     int i = 0;
/* 1477 */     State localState = this.state;
/* 1478 */     for (; paramChain != null; paramChain = paramChain.next) {
/* 1479 */       Assert.check((this.state != paramChain.state) && ((paramInt > paramChain.pc) || (this.state.stacksize == 0)));
/*      */ 
/* 1481 */       if (paramInt >= this.cp)
/* 1482 */         paramInt = this.cp;
/* 1483 */       else if (get1(paramInt) == 167) {
/* 1484 */         if (this.fatcode) paramInt += get4(paramInt + 1); else
/* 1485 */           paramInt += get2(paramInt + 1);
/*      */       }
/* 1487 */       if ((get1(paramChain.pc) == 167) && (paramChain.pc + 3 == paramInt) && (paramInt == this.cp) && (!this.fixedPc))
/*      */       {
/* 1491 */         if (this.varDebugInfo) {
/* 1492 */           adjustAliveRanges(this.cp, -3);
/*      */         }
/* 1494 */         this.cp -= 3;
/* 1495 */         paramInt -= 3;
/* 1496 */         if (paramChain.next == null)
/*      */         {
/* 1500 */           this.alive = true;
/* 1501 */           break;
/*      */         }
/*      */       } else {
/* 1504 */         if (this.fatcode)
/* 1505 */           put4(paramChain.pc + 1, paramInt - paramChain.pc);
/* 1506 */         else if ((paramInt - paramChain.pc < -32768) || (paramInt - paramChain.pc > 32767))
/*      */         {
/* 1508 */           this.fatcode = true;
/*      */         }
/* 1510 */         else put2(paramChain.pc + 1, paramInt - paramChain.pc);
/* 1511 */         Assert.check((!this.alive) || ((paramChain.state.stacksize == localState.stacksize) && (paramChain.state.nlocks == localState.nlocks)));
/*      */       }
/*      */ 
/* 1515 */       this.fixedPc = true;
/* 1516 */       if (this.cp == paramInt) {
/* 1517 */         i = 1;
/* 1518 */         if (this.debugCode)
/* 1519 */           System.err.println("resolving chain state=" + paramChain.state);
/* 1520 */         if (this.alive) {
/* 1521 */           localState = paramChain.state.join(localState);
/*      */         } else {
/* 1523 */           localState = paramChain.state;
/* 1524 */           this.alive = true;
/*      */         }
/*      */       }
/*      */     }
/* 1528 */     Assert.check((i == 0) || (this.state != localState));
/* 1529 */     if (this.state != localState) {
/* 1530 */       setDefined(localState.defined);
/* 1531 */       this.state = localState;
/* 1532 */       this.pendingStackMap = this.needStackMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resolve(Chain paramChain)
/*      */   {
/* 1539 */     Assert.check((!this.alive) || (paramChain == null) || ((this.state.stacksize == paramChain.state.stacksize) && (this.state.nlocks == paramChain.state.nlocks)));
/*      */ 
/* 1544 */     this.pendingJumps = mergeChains(paramChain, this.pendingJumps);
/*      */   }
/*      */ 
/*      */   public void resolvePending()
/*      */   {
/* 1550 */     Chain localChain = this.pendingJumps;
/* 1551 */     this.pendingJumps = null;
/* 1552 */     resolve(localChain, this.cp);
/*      */   }
/*      */ 
/*      */   public static Chain mergeChains(Chain paramChain1, Chain paramChain2)
/*      */   {
/* 1559 */     if (paramChain2 == null) return paramChain1;
/* 1560 */     if (paramChain1 == null) return paramChain2;
/* 1561 */     Assert.check((paramChain1.state.stacksize == paramChain2.state.stacksize) && (paramChain1.state.nlocks == paramChain2.state.nlocks));
/*      */ 
/* 1564 */     if (paramChain1.pc < paramChain2.pc)
/*      */     {
/* 1567 */       return new Chain(paramChain2.pc, 
/* 1567 */         mergeChains(paramChain1, paramChain2.next), 
/* 1567 */         paramChain2.state);
/*      */     }
/*      */ 
/* 1571 */     return new Chain(paramChain1.pc, 
/* 1571 */       mergeChains(paramChain1.next, paramChain2), 
/* 1571 */       paramChain1.state);
/*      */   }
/*      */ 
/*      */   public void addCatch(char paramChar1, char paramChar2, char paramChar3, char paramChar4)
/*      */   {
/* 1584 */     this.catchInfo.append(new char[] { paramChar1, paramChar2, paramChar3, paramChar4 });
/*      */   }
/*      */ 
/*      */   public void compressCatchTable()
/*      */   {
/* 1589 */     ListBuffer localListBuffer = new ListBuffer();
/* 1590 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1591 */     for (Iterator localIterator = this.catchInfo.iterator(); localIterator.hasNext(); ) { arrayOfChar = (char[])localIterator.next();
/* 1592 */       localList = localList.prepend(Integer.valueOf(arrayOfChar[2]));
/*      */     }
/* 1594 */     char[] arrayOfChar;
/* 1594 */     for (localIterator = this.catchInfo.iterator(); localIterator.hasNext(); ) { arrayOfChar = (char[])localIterator.next();
/* 1595 */       int i = arrayOfChar[0];
/* 1596 */       int j = arrayOfChar[1];
/* 1597 */       if ((i != j) && ((i != j - 1) || 
/* 1599 */         (!localList
/* 1599 */         .contains(Integer.valueOf(i)))))
/*      */       {
/* 1602 */         localListBuffer.append(arrayOfChar);
/*      */       }
/*      */     }
/* 1605 */     this.catchInfo = localListBuffer;
/*      */   }
/*      */ 
/*      */   public void addLineNumber(char paramChar1, char paramChar2)
/*      */   {
/* 1616 */     if (this.lineDebugInfo) {
/* 1617 */       if ((this.lineInfo.nonEmpty()) && (((char[])this.lineInfo.head)[0] == paramChar1))
/* 1618 */         this.lineInfo = this.lineInfo.tail;
/* 1619 */       if ((this.lineInfo.isEmpty()) || (((char[])this.lineInfo.head)[1] != paramChar2))
/* 1620 */         this.lineInfo = this.lineInfo.prepend(new char[] { paramChar1, paramChar2 });
/*      */     }
/*      */   }
/*      */ 
/*      */   public void statBegin(int paramInt)
/*      */   {
/* 1627 */     if (paramInt != -1)
/* 1628 */       this.pendingStatPos = paramInt;
/*      */   }
/*      */ 
/*      */   public void markStatBegin()
/*      */   {
/* 1635 */     if ((this.alive) && (this.lineDebugInfo)) {
/* 1636 */       int i = this.lineMap.getLineNumber(this.pendingStatPos);
/* 1637 */       int j = (char)this.cp;
/* 1638 */       int k = (char)i;
/* 1639 */       if ((j == this.cp) && (k == i))
/* 1640 */         addLineNumber(j, k);
/*      */     }
/* 1642 */     this.pendingStatPos = -1;
/*      */   }
/*      */ 
/*      */   private void addLocalVar(Symbol.VarSymbol paramVarSymbol)
/*      */   {
/* 2001 */     int i = paramVarSymbol.adr;
/* 2002 */     this.lvar = ((LocalVar[])ArrayUtils.ensureCapacity(this.lvar, i + 1));
/* 2003 */     Assert.checkNull(this.lvar[i]);
/* 2004 */     if (this.pendingJumps != null) {
/* 2005 */       resolvePending();
/*      */     }
/* 2007 */     this.lvar[i] = new LocalVar(paramVarSymbol);
/* 2008 */     this.state.defined.excl(i); } 
/*      */   void adjustAliveRanges(int paramInt1, int paramInt2) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 713	com/sun/tools/javac/jvm/Code:lvar	[Lcom/sun/tools/javac/jvm/Code$LocalVar;
/*      */     //   4: astore_3
/*      */     //   5: aload_3
/*      */     //   6: arraylength
/*      */     //   7: istore 4
/*      */     //   9: iconst_0
/*      */     //   10: istore 5
/*      */     //   12: iload 5
/*      */     //   14: iload 4
/*      */     //   16: if_icmpge +92 -> 108
/*      */     //   19: aload_3
/*      */     //   20: iload 5
/*      */     //   22: aaload
/*      */     //   23: astore 6
/*      */     //   25: aload 6
/*      */     //   27: ifnull +75 -> 102
/*      */     //   30: aload 6
/*      */     //   32: getfield 731	com/sun/tools/javac/jvm/Code$LocalVar:aliveRanges	Ljava/util/List;
/*      */     //   35: invokeinterface 876 1 0
/*      */     //   40: astore 7
/*      */     //   42: aload 7
/*      */     //   44: invokeinterface 873 1 0
/*      */     //   49: ifeq +53 -> 102
/*      */     //   52: aload 7
/*      */     //   54: invokeinterface 874 1 0
/*      */     //   59: checkcast 352	com/sun/tools/javac/jvm/Code$LocalVar$Range
/*      */     //   62: astore 8
/*      */     //   64: aload 8
/*      */     //   66: invokevirtual 822	com/sun/tools/javac/jvm/Code$LocalVar$Range:closed	()Z
/*      */     //   69: ifeq +30 -> 99
/*      */     //   72: aload 8
/*      */     //   74: getfield 733	com/sun/tools/javac/jvm/Code$LocalVar$Range:start_pc	C
/*      */     //   77: aload 8
/*      */     //   79: getfield 732	com/sun/tools/javac/jvm/Code$LocalVar$Range:length	C
/*      */     //   82: iadd
/*      */     //   83: iload_1
/*      */     //   84: if_icmplt +15 -> 99
/*      */     //   87: aload 8
/*      */     //   89: dup
/*      */     //   90: getfield 732	com/sun/tools/javac/jvm/Code$LocalVar$Range:length	C
/*      */     //   93: iload_2
/*      */     //   94: iadd
/*      */     //   95: i2c
/*      */     //   96: putfield 732	com/sun/tools/javac/jvm/Code$LocalVar$Range:length	C
/*      */     //   99: goto -57 -> 42
/*      */     //   102: iinc 5 1
/*      */     //   105: goto -93 -> 12
/*      */     //   108: return } 
/* 2027 */   public int getLVTSize() { int i = this.varBufferSize;
/* 2028 */     for (int j = 0; j < this.varBufferSize; j++) {
/* 2029 */       LocalVar localLocalVar = this.varBuffer[j];
/* 2030 */       i += localLocalVar.aliveRanges.size() - 1;
/*      */     }
/* 2032 */     return i;
/*      */   }
/*      */ 
/*      */   public void setDefined(Bits paramBits)
/*      */   {
/* 2037 */     if ((this.alive) && (paramBits != this.state.defined)) {
/* 2038 */       Bits localBits = new Bits(this.state.defined).xorSet(paramBits);
/* 2039 */       for (int i = localBits.nextBit(0); 
/* 2040 */         i >= 0; 
/* 2041 */         i = localBits.nextBit(i + 1))
/* 2042 */         if (i >= this.nextreg)
/* 2043 */           this.state.defined.excl(i);
/* 2044 */         else if (this.state.defined.isMember(i))
/* 2045 */           setUndefined(i);
/*      */         else
/* 2047 */           setDefined(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDefined(int paramInt)
/*      */   {
/* 2054 */     LocalVar localLocalVar = this.lvar[paramInt];
/* 2055 */     if (localLocalVar == null) {
/* 2056 */       this.state.defined.excl(paramInt);
/*      */     } else {
/* 2058 */       this.state.defined.incl(paramInt);
/* 2059 */       if (this.cp < 65535)
/* 2060 */         localLocalVar.openRange((char)this.cp);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setUndefined(int paramInt)
/*      */   {
/* 2067 */     this.state.defined.excl(paramInt);
/* 2068 */     if ((paramInt < this.lvar.length) && (this.lvar[paramInt] != null))
/*      */     {
/* 2070 */       if (this.lvar[paramInt]
/* 2070 */         .isLastRangeInitialized()) {
/* 2071 */         LocalVar localLocalVar = this.lvar[paramInt];
/* 2072 */         int i = (char)(curCP() - localLocalVar.lastRange().start_pc);
/* 2073 */         if (i < 65535) {
/* 2074 */           this.lvar[paramInt] = localLocalVar.dup();
/* 2075 */           localLocalVar.closeRange(i);
/* 2076 */           putVar(localLocalVar);
/*      */         } else {
/* 2078 */           localLocalVar.removeLastRange();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void endScope(int paramInt) {
/* 2085 */     LocalVar localLocalVar = this.lvar[paramInt];
/* 2086 */     if (localLocalVar != null) {
/* 2087 */       if (localLocalVar.isLastRangeInitialized()) {
/* 2088 */         int i = (char)(curCP() - localLocalVar.lastRange().start_pc);
/* 2089 */         if (i < 65535) {
/* 2090 */           localLocalVar.closeRange(i);
/* 2091 */           putVar(localLocalVar);
/* 2092 */           fillLocalVarPosition(localLocalVar);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2100 */       this.lvar[paramInt] = null;
/*      */     }
/* 2102 */     this.state.defined.excl(paramInt);
/*      */   }
/*      */ 
/*      */   private void fillLocalVarPosition(LocalVar paramLocalVar) {
/* 2106 */     if ((paramLocalVar == null) || (paramLocalVar.sym == null) || (!paramLocalVar.sym.hasTypeAnnotations()))
/* 2107 */       return;
/* 2108 */     for (Attribute.TypeCompound localTypeCompound : paramLocalVar.sym.getRawTypeAttributes()) {
/* 2109 */       TypeAnnotationPosition localTypeAnnotationPosition = localTypeCompound.position;
/* 2110 */       Code.LocalVar.Range localRange = paramLocalVar.getWidestRange();
/* 2111 */       localTypeAnnotationPosition.lvarOffset = new int[] { localRange.start_pc };
/* 2112 */       localTypeAnnotationPosition.lvarLength = new int[] { localRange.length };
/* 2113 */       localTypeAnnotationPosition.lvarIndex = new int[] { paramLocalVar.reg };
/* 2114 */       localTypeAnnotationPosition.isValidOffset = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillExceptionParameterPositions()
/*      */   {
/* 2122 */     for (int i = 0; i < this.varBufferSize; i++) {
/* 2123 */       LocalVar localLocalVar = this.varBuffer[i];
/* 2124 */       if ((localLocalVar != null) && (localLocalVar.sym != null) && 
/* 2125 */         (localLocalVar.sym
/* 2125 */         .hasTypeAnnotations()) && 
/* 2126 */         (localLocalVar.sym
/* 2126 */         .isExceptionParameter()))
/*      */       {
/* 2129 */         for (Attribute.TypeCompound localTypeCompound : localLocalVar.sym.getRawTypeAttributes()) {
/* 2130 */           TypeAnnotationPosition localTypeAnnotationPosition = localTypeCompound.position;
/*      */ 
/* 2137 */           if (localTypeAnnotationPosition.type_index != -666) {
/* 2138 */             localTypeAnnotationPosition.exception_index = findExceptionIndex(localTypeAnnotationPosition.type_index);
/* 2139 */             localTypeAnnotationPosition.type_index = -666;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 2146 */   private int findExceptionIndex(int paramInt) { if (paramInt == -2147483648)
/*      */     {
/* 2150 */       return -1;
/*      */     }
/* 2152 */     com.sun.tools.javac.util.List localList = this.catchInfo.toList();
/* 2153 */     int i = this.catchInfo.length();
/* 2154 */     for (int j = 0; j < i; j++) {
/* 2155 */       char[] arrayOfChar = (char[])localList.head;
/* 2156 */       localList = localList.tail;
/* 2157 */       int k = arrayOfChar[3];
/* 2158 */       if (paramInt == k) {
/* 2159 */         return j;
/*      */       }
/*      */     }
/* 2162 */     return -1;
/*      */   }
/*      */ 
/*      */   void putVar(LocalVar paramLocalVar)
/*      */   {
/* 2173 */     int i = (this.varDebugInfo) || (
/* 2173 */       (paramLocalVar.sym
/* 2173 */       .isExceptionParameter()) && (paramLocalVar.sym.hasTypeAnnotations())) ? 1 : 0;
/* 2174 */     if (i == 0) return;
/*      */ 
/* 2178 */     int j = ((paramLocalVar.sym.flags() & 0x1000) != 0L) && (
/* 2177 */       ((paramLocalVar.sym.owner
/* 2177 */       .flags() & 0x0) == 0L) || 
/* 2178 */       ((paramLocalVar.sym
/* 2178 */       .flags() & 0x0) == 0L)) ? 1 : 0;
/* 2179 */     if (j != 0) return;
/* 2180 */     if (this.varBuffer == null)
/* 2181 */       this.varBuffer = new LocalVar[20];
/*      */     else
/* 2183 */       this.varBuffer = ((LocalVar[])ArrayUtils.ensureCapacity(this.varBuffer, this.varBufferSize));
/* 2184 */     this.varBuffer[(this.varBufferSize++)] = paramLocalVar;
/*      */   }
/*      */ 
/*      */   private int newLocal(int paramInt)
/*      */   {
/* 2194 */     int i = this.nextreg;
/* 2195 */     int j = width(paramInt);
/* 2196 */     this.nextreg = (i + j);
/* 2197 */     if (this.nextreg > this.max_locals) this.max_locals = this.nextreg;
/* 2198 */     return i;
/*      */   }
/*      */ 
/*      */   private int newLocal(Type paramType) {
/* 2202 */     return newLocal(typecode(paramType));
/*      */   }
/*      */ 
/*      */   public int newLocal(Symbol.VarSymbol paramVarSymbol) {
/* 2206 */     int i = paramVarSymbol.adr = newLocal(paramVarSymbol.erasure(this.types));
/* 2207 */     addLocalVar(paramVarSymbol);
/* 2208 */     return i;
/*      */   }
/*      */ 
/*      */   public void newRegSegment()
/*      */   {
/* 2214 */     this.nextreg = this.max_locals;
/*      */   }
/*      */ 
/*      */   public void endScopes(int paramInt)
/*      */   {
/* 2220 */     int i = this.nextreg;
/* 2221 */     this.nextreg = paramInt;
/* 2222 */     for (int j = this.nextreg; j < i; j++) endScope(j);
/*      */   }
/*      */ 
/*      */   public static String mnem(int paramInt)
/*      */   {
/* 2230 */     return Mneumonics.mnem[paramInt];
/*      */   }
/*      */ 
/*      */   public static class Chain
/*      */   {
/*      */     public final int pc;
/*      */     Code.State state;
/*      */     public final Chain next;
/*      */ 
/*      */     public Chain(int paramInt, Chain paramChain, Code.State paramState)
/*      */     {
/* 1420 */       this.pc = paramInt;
/* 1421 */       this.next = paramChain;
/* 1422 */       this.state = paramState;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LocalVar
/*      */   {
/*      */     final Symbol.VarSymbol sym;
/*      */     final char reg;
/* 1910 */     java.util.List<Range> aliveRanges = new ArrayList();
/*      */ 
/*      */     LocalVar(Symbol.VarSymbol paramVarSymbol) {
/* 1913 */       this.sym = paramVarSymbol;
/* 1914 */       this.reg = ((char)paramVarSymbol.adr);
/*      */     }
/*      */     public LocalVar dup() {
/* 1917 */       return new LocalVar(this.sym);
/*      */     }
/*      */ 
/*      */     Range firstRange() {
/* 1921 */       return this.aliveRanges.isEmpty() ? null : (Range)this.aliveRanges.get(0);
/*      */     }
/*      */ 
/*      */     Range lastRange() {
/* 1925 */       return this.aliveRanges.isEmpty() ? null : (Range)this.aliveRanges.get(this.aliveRanges.size() - 1);
/*      */     }
/*      */ 
/*      */     void removeLastRange() {
/* 1929 */       Range localRange = lastRange();
/* 1930 */       if (localRange != null)
/* 1931 */         this.aliveRanges.remove(localRange);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1937 */       if (this.aliveRanges == null) {
/* 1938 */         return "empty local var";
/*      */       }
/*      */ 
/* 1941 */       StringBuilder localStringBuilder = new StringBuilder().append(this.sym)
/* 1941 */         .append(" in register ")
/* 1941 */         .append(this.reg).append(" \n");
/* 1942 */       for (Range localRange : this.aliveRanges) {
/* 1943 */         localStringBuilder.append(" starts at pc=").append(Integer.toString(localRange.start_pc))
/* 1944 */           .append(" length=")
/* 1944 */           .append(Integer.toString(localRange.length))
/* 1945 */           .append("\n");
/*      */       }
/*      */ 
/* 1947 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     public void openRange(char paramChar) {
/* 1951 */       if (!hasOpenRange())
/* 1952 */         this.aliveRanges.add(new Range(paramChar));
/*      */     }
/*      */ 
/*      */     public void closeRange(char paramChar)
/*      */     {
/* 1957 */       if ((isLastRangeInitialized()) && (paramChar > 0)) {
/* 1958 */         Range localRange = lastRange();
/* 1959 */         if ((localRange != null) && 
/* 1960 */           (localRange.length == 65535))
/* 1961 */           localRange.length = paramChar;
/*      */       }
/*      */       else
/*      */       {
/* 1965 */         removeLastRange();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean hasOpenRange() {
/* 1970 */       if (this.aliveRanges.isEmpty()) {
/* 1971 */         return false;
/*      */       }
/* 1973 */       return lastRange().length == 65535;
/*      */     }
/*      */ 
/*      */     public boolean isLastRangeInitialized() {
/* 1977 */       if (this.aliveRanges.isEmpty()) {
/* 1978 */         return false;
/*      */       }
/* 1980 */       return lastRange().start_pc != 65535;
/*      */     }
/*      */ 
/*      */     public Range getWidestRange() {
/* 1984 */       if (this.aliveRanges.isEmpty()) {
/* 1985 */         return new Range();
/*      */       }
/* 1987 */       Range localRange1 = firstRange();
/* 1988 */       Range localRange2 = lastRange();
/* 1989 */       char c = (char)(localRange2.length + (localRange2.start_pc - localRange1.start_pc));
/* 1990 */       return new Range(localRange1.start_pc, c);
/*      */     }
/*      */ 
/*      */     class Range
/*      */     {
/* 1884 */       char start_pc = 65535;
/* 1885 */       char length = 65535;
/*      */ 
/*      */       Range()
/*      */       {
/*      */       }
/*      */ 
/*      */       Range(char arg2)
/*      */       {
/*      */         char c;
/* 1890 */         this.start_pc = c;
/*      */       }
/*      */ 
/*      */       Range(char paramChar1, char arg3) {
/* 1894 */         this.start_pc = paramChar1;
/*      */         char c;
/* 1895 */         this.length = c;
/*      */       }
/*      */ 
/*      */       boolean closed() {
/* 1899 */         return (this.start_pc != 65535) && (this.length != 65535);
/*      */       }
/*      */ 
/*      */       public String toString()
/*      */       {
/* 1904 */         int i = this.start_pc;
/* 1905 */         int j = this.length;
/* 1906 */         return "startpc = " + i + " length " + j;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Mneumonics
/*      */   {
/* 2234 */     private static final String[] mnem = new String['Ë'];
/*      */ 
/* 2236 */     static { mnem[0] = "nop";
/* 2237 */       mnem[1] = "aconst_null";
/* 2238 */       mnem[2] = "iconst_m1";
/* 2239 */       mnem[3] = "iconst_0";
/* 2240 */       mnem[4] = "iconst_1";
/* 2241 */       mnem[5] = "iconst_2";
/* 2242 */       mnem[6] = "iconst_3";
/* 2243 */       mnem[7] = "iconst_4";
/* 2244 */       mnem[8] = "iconst_5";
/* 2245 */       mnem[9] = "lconst_0";
/* 2246 */       mnem[10] = "lconst_1";
/* 2247 */       mnem[11] = "fconst_0";
/* 2248 */       mnem[12] = "fconst_1";
/* 2249 */       mnem[13] = "fconst_2";
/* 2250 */       mnem[14] = "dconst_0";
/* 2251 */       mnem[15] = "dconst_1";
/* 2252 */       mnem[16] = "bipush";
/* 2253 */       mnem[17] = "sipush";
/* 2254 */       mnem[18] = "ldc1";
/* 2255 */       mnem[19] = "ldc2";
/* 2256 */       mnem[20] = "ldc2w";
/* 2257 */       mnem[21] = "iload";
/* 2258 */       mnem[22] = "lload";
/* 2259 */       mnem[23] = "fload";
/* 2260 */       mnem[24] = "dload";
/* 2261 */       mnem[25] = "aload";
/* 2262 */       mnem[26] = "iload_0";
/* 2263 */       mnem[30] = "lload_0";
/* 2264 */       mnem[34] = "fload_0";
/* 2265 */       mnem[38] = "dload_0";
/* 2266 */       mnem[42] = "aload_0";
/* 2267 */       mnem[27] = "iload_1";
/* 2268 */       mnem[31] = "lload_1";
/* 2269 */       mnem[35] = "fload_1";
/* 2270 */       mnem[39] = "dload_1";
/* 2271 */       mnem[43] = "aload_1";
/* 2272 */       mnem[28] = "iload_2";
/* 2273 */       mnem[32] = "lload_2";
/* 2274 */       mnem[36] = "fload_2";
/* 2275 */       mnem[40] = "dload_2";
/* 2276 */       mnem[44] = "aload_2";
/* 2277 */       mnem[29] = "iload_3";
/* 2278 */       mnem[33] = "lload_3";
/* 2279 */       mnem[37] = "fload_3";
/* 2280 */       mnem[41] = "dload_3";
/* 2281 */       mnem[45] = "aload_3";
/* 2282 */       mnem[46] = "iaload";
/* 2283 */       mnem[47] = "laload";
/* 2284 */       mnem[48] = "faload";
/* 2285 */       mnem[49] = "daload";
/* 2286 */       mnem[50] = "aaload";
/* 2287 */       mnem[51] = "baload";
/* 2288 */       mnem[52] = "caload";
/* 2289 */       mnem[53] = "saload";
/* 2290 */       mnem[54] = "istore";
/* 2291 */       mnem[55] = "lstore";
/* 2292 */       mnem[56] = "fstore";
/* 2293 */       mnem[57] = "dstore";
/* 2294 */       mnem[58] = "astore";
/* 2295 */       mnem[59] = "istore_0";
/* 2296 */       mnem[63] = "lstore_0";
/* 2297 */       mnem[67] = "fstore_0";
/* 2298 */       mnem[71] = "dstore_0";
/* 2299 */       mnem[75] = "astore_0";
/* 2300 */       mnem[60] = "istore_1";
/* 2301 */       mnem[64] = "lstore_1";
/* 2302 */       mnem[68] = "fstore_1";
/* 2303 */       mnem[72] = "dstore_1";
/* 2304 */       mnem[76] = "astore_1";
/* 2305 */       mnem[61] = "istore_2";
/* 2306 */       mnem[65] = "lstore_2";
/* 2307 */       mnem[69] = "fstore_2";
/* 2308 */       mnem[73] = "dstore_2";
/* 2309 */       mnem[77] = "astore_2";
/* 2310 */       mnem[62] = "istore_3";
/* 2311 */       mnem[66] = "lstore_3";
/* 2312 */       mnem[70] = "fstore_3";
/* 2313 */       mnem[74] = "dstore_3";
/* 2314 */       mnem[78] = "astore_3";
/* 2315 */       mnem[79] = "iastore";
/* 2316 */       mnem[80] = "lastore";
/* 2317 */       mnem[81] = "fastore";
/* 2318 */       mnem[82] = "dastore";
/* 2319 */       mnem[83] = "aastore";
/* 2320 */       mnem[84] = "bastore";
/* 2321 */       mnem[85] = "castore";
/* 2322 */       mnem[86] = "sastore";
/* 2323 */       mnem[87] = "pop";
/* 2324 */       mnem[88] = "pop2";
/* 2325 */       mnem[89] = "dup";
/* 2326 */       mnem[90] = "dup_x1";
/* 2327 */       mnem[91] = "dup_x2";
/* 2328 */       mnem[92] = "dup2";
/* 2329 */       mnem[93] = "dup2_x1";
/* 2330 */       mnem[94] = "dup2_x2";
/* 2331 */       mnem[95] = "swap";
/* 2332 */       mnem[96] = "iadd";
/* 2333 */       mnem[97] = "ladd";
/* 2334 */       mnem[98] = "fadd";
/* 2335 */       mnem[99] = "dadd";
/* 2336 */       mnem[100] = "isub";
/* 2337 */       mnem[101] = "lsub";
/* 2338 */       mnem[102] = "fsub";
/* 2339 */       mnem[103] = "dsub";
/* 2340 */       mnem[104] = "imul";
/* 2341 */       mnem[105] = "lmul";
/* 2342 */       mnem[106] = "fmul";
/* 2343 */       mnem[107] = "dmul";
/* 2344 */       mnem[108] = "idiv";
/* 2345 */       mnem[109] = "ldiv";
/* 2346 */       mnem[110] = "fdiv";
/* 2347 */       mnem[111] = "ddiv";
/* 2348 */       mnem[112] = "imod";
/* 2349 */       mnem[113] = "lmod";
/* 2350 */       mnem[114] = "fmod";
/* 2351 */       mnem[115] = "dmod";
/* 2352 */       mnem[116] = "ineg";
/* 2353 */       mnem[117] = "lneg";
/* 2354 */       mnem[118] = "fneg";
/* 2355 */       mnem[119] = "dneg";
/* 2356 */       mnem[120] = "ishl";
/* 2357 */       mnem[121] = "lshl";
/* 2358 */       mnem[122] = "ishr";
/* 2359 */       mnem[123] = "lshr";
/* 2360 */       mnem[124] = "iushr";
/* 2361 */       mnem[125] = "lushr";
/* 2362 */       mnem[126] = "iand";
/* 2363 */       mnem[127] = "land";
/* 2364 */       mnem[''] = "ior";
/* 2365 */       mnem[''] = "lor";
/* 2366 */       mnem[''] = "ixor";
/* 2367 */       mnem[''] = "lxor";
/* 2368 */       mnem[''] = "iinc";
/* 2369 */       mnem[''] = "i2l";
/* 2370 */       mnem[''] = "i2f";
/* 2371 */       mnem[''] = "i2d";
/* 2372 */       mnem[''] = "l2i";
/* 2373 */       mnem[''] = "l2f";
/* 2374 */       mnem[''] = "l2d";
/* 2375 */       mnem[''] = "f2i";
/* 2376 */       mnem[''] = "f2l";
/* 2377 */       mnem[''] = "f2d";
/* 2378 */       mnem[''] = "d2i";
/* 2379 */       mnem[''] = "d2l";
/* 2380 */       mnem[''] = "d2f";
/* 2381 */       mnem[''] = "int2byte";
/* 2382 */       mnem[''] = "int2char";
/* 2383 */       mnem[''] = "int2short";
/* 2384 */       mnem[''] = "lcmp";
/* 2385 */       mnem[''] = "fcmpl";
/* 2386 */       mnem[''] = "fcmpg";
/* 2387 */       mnem[''] = "dcmpl";
/* 2388 */       mnem[''] = "dcmpg";
/* 2389 */       mnem[''] = "ifeq";
/* 2390 */       mnem[''] = "ifne";
/* 2391 */       mnem[''] = "iflt";
/* 2392 */       mnem[''] = "ifge";
/* 2393 */       mnem[''] = "ifgt";
/* 2394 */       mnem[''] = "ifle";
/* 2395 */       mnem[''] = "if_icmpeq";
/* 2396 */       mnem[' '] = "if_icmpne";
/* 2397 */       mnem['¡'] = "if_icmplt";
/* 2398 */       mnem['¢'] = "if_icmpge";
/* 2399 */       mnem['£'] = "if_icmpgt";
/* 2400 */       mnem['¤'] = "if_icmple";
/* 2401 */       mnem['¥'] = "if_acmpeq";
/* 2402 */       mnem['¦'] = "if_acmpne";
/* 2403 */       mnem['§'] = "goto_";
/* 2404 */       mnem['¨'] = "jsr";
/* 2405 */       mnem['©'] = "ret";
/* 2406 */       mnem['ª'] = "tableswitch";
/* 2407 */       mnem['«'] = "lookupswitch";
/* 2408 */       mnem['¬'] = "ireturn";
/* 2409 */       mnem['­'] = "lreturn";
/* 2410 */       mnem['®'] = "freturn";
/* 2411 */       mnem['¯'] = "dreturn";
/* 2412 */       mnem['°'] = "areturn";
/* 2413 */       mnem['±'] = "return_";
/* 2414 */       mnem['²'] = "getstatic";
/* 2415 */       mnem['³'] = "putstatic";
/* 2416 */       mnem['´'] = "getfield";
/* 2417 */       mnem['µ'] = "putfield";
/* 2418 */       mnem['¶'] = "invokevirtual";
/* 2419 */       mnem['·'] = "invokespecial";
/* 2420 */       mnem['¸'] = "invokestatic";
/* 2421 */       mnem['¹'] = "invokeinterface";
/* 2422 */       mnem['º'] = "invokedynamic";
/* 2423 */       mnem['»'] = "new_";
/* 2424 */       mnem['¼'] = "newarray";
/* 2425 */       mnem['½'] = "anewarray";
/* 2426 */       mnem['¾'] = "arraylength";
/* 2427 */       mnem['¿'] = "athrow";
/* 2428 */       mnem['À'] = "checkcast";
/* 2429 */       mnem['Á'] = "instanceof_";
/* 2430 */       mnem['Â'] = "monitorenter";
/* 2431 */       mnem['Ã'] = "monitorexit";
/* 2432 */       mnem['Ä'] = "wide";
/* 2433 */       mnem['Å'] = "multianewarray";
/* 2434 */       mnem['Æ'] = "if_acmp_null";
/* 2435 */       mnem['Ç'] = "if_acmp_nonnull";
/* 2436 */       mnem['È'] = "goto_w";
/* 2437 */       mnem['É'] = "jsr_w";
/* 2438 */       mnem['Ê'] = "breakpoint";
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum StackMapFormat
/*      */   {
/*   56 */     NONE, 
/*   57 */     CLDC, 
/*      */ 
/*   62 */     JSR202;
/*      */ 
/*      */     Name getAttributeName(Names paramNames)
/*      */     {
/*   68 */       return paramNames.empty;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StackMapFrame
/*      */   {
/*      */     int pc;
/*      */     Type[] locals;
/*      */     Type[] stack;
/*      */   }
/*      */ 
/*      */   class State
/*      */     implements Cloneable
/*      */   {
/*      */     Bits defined;
/*      */     Type[] stack;
/*      */     int stacksize;
/*      */     int[] locks;
/*      */     int nlocks;
/*      */ 
/*      */     State()
/*      */     {
/* 1665 */       this.defined = new Bits();
/* 1666 */       this.stack = new Type[16];
/*      */     }
/*      */ 
/*      */     State dup() {
/*      */       try {
/* 1671 */         State localState = (State)super.clone();
/* 1672 */         localState.defined = new Bits(this.defined);
/* 1673 */         localState.stack = ((Type[])this.stack.clone());
/* 1674 */         if (this.locks != null) localState.locks = ((int[])this.locks.clone());
/* 1675 */         if (Code.this.debugCode) {
/* 1676 */           System.err.println("duping state " + this);
/* 1677 */           dump();
/*      */         }
/* 1679 */         return localState;
/*      */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 1681 */         throw new AssertionError(localCloneNotSupportedException);
/*      */       }
/*      */     }
/*      */ 
/*      */     void lock(int paramInt) {
/* 1686 */       if (this.locks == null)
/* 1687 */         this.locks = new int[20];
/*      */       else {
/* 1689 */         this.locks = ArrayUtils.ensureCapacity(this.locks, this.nlocks);
/*      */       }
/* 1691 */       this.locks[this.nlocks] = paramInt;
/* 1692 */       this.nlocks += 1;
/*      */     }
/*      */ 
/*      */     void unlock(int paramInt) {
/* 1696 */       this.nlocks -= 1;
/* 1697 */       Assert.check(this.locks[this.nlocks] == paramInt);
/* 1698 */       this.locks[this.nlocks] = -1;
/*      */     }
/*      */ 
/*      */     void push(Type paramType) {
/* 1702 */       if (Code.this.debugCode) System.err.println("   pushing " + paramType);
/* 1703 */       switch (Code.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*      */       case 9:
/* 1705 */         return;
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 8:
/* 1710 */         paramType = Code.this.syms.intType;
/* 1711 */         break;
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/* 1715 */       case 7: } this.stack = ((Type[])ArrayUtils.ensureCapacity(this.stack, this.stacksize + 2));
/* 1716 */       this.stack[(this.stacksize++)] = paramType;
/* 1717 */       switch (Code.width(paramType)) {
/*      */       case 1:
/* 1719 */         break;
/*      */       case 2:
/* 1721 */         this.stack[(this.stacksize++)] = null;
/* 1722 */         break;
/*      */       default:
/* 1724 */         throw new AssertionError(paramType);
/*      */       }
/* 1726 */       if (this.stacksize > Code.this.max_stack)
/* 1727 */         Code.this.max_stack = this.stacksize;
/*      */     }
/*      */ 
/*      */     Type pop1() {
/* 1731 */       if (Code.this.debugCode) System.err.println("   popping 1");
/* 1732 */       this.stacksize -= 1;
/* 1733 */       Type localType = this.stack[this.stacksize];
/* 1734 */       this.stack[this.stacksize] = null;
/* 1735 */       Assert.check((localType != null) && (Code.width(localType) == 1));
/* 1736 */       return localType;
/*      */     }
/*      */ 
/*      */     Type peek() {
/* 1740 */       return this.stack[(this.stacksize - 1)];
/*      */     }
/*      */ 
/*      */     Type pop2() {
/* 1744 */       if (Code.this.debugCode) System.err.println("   popping 2");
/* 1745 */       this.stacksize -= 2;
/* 1746 */       Type localType = this.stack[this.stacksize];
/* 1747 */       this.stack[this.stacksize] = null;
/* 1748 */       Assert.check((this.stack[(this.stacksize + 1)] == null) && (localType != null) && 
/* 1749 */         (Code.width(localType) == 
/* 1749 */         2));
/* 1750 */       return localType;
/*      */     }
/*      */ 
/*      */     void pop(int paramInt) {
/* 1754 */       if (Code.this.debugCode) System.err.println("   popping " + paramInt);
/* 1755 */       while (paramInt > 0) {
/* 1756 */         this.stack[(--this.stacksize)] = null;
/* 1757 */         paramInt--;
/*      */       }
/*      */     }
/*      */ 
/*      */     void pop(Type paramType) {
/* 1762 */       pop(Code.width(paramType));
/*      */     }
/*      */ 
/*      */     void forceStackTop(Type paramType)
/*      */     {
/* 1768 */       if (!Code.this.alive) return;
/* 1769 */       switch (Code.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*      */       case 10:
/*      */       case 11:
/* 1772 */         int i = Code.width(paramType);
/* 1773 */         Type localType = this.stack[(this.stacksize - i)];
/* 1774 */         Assert.check(Code.this.types.isSubtype(Code.this.types.erasure(localType), Code.this.types
/* 1775 */           .erasure(paramType)));
/*      */ 
/* 1776 */         this.stack[(this.stacksize - i)] = paramType;
/* 1777 */         break;
/*      */       }
/*      */     }
/*      */ 
/*      */     void markInitialized(UninitializedType paramUninitializedType)
/*      */     {
/* 1783 */       Type localType = paramUninitializedType.initializedType();
/* 1784 */       for (int i = 0; i < this.stacksize; i++) {
/* 1785 */         if (this.stack[i] == paramUninitializedType) this.stack[i] = localType;
/*      */       }
/* 1787 */       for (i = 0; i < Code.this.lvar.length; i++) {
/* 1788 */         Code.LocalVar localLocalVar1 = Code.this.lvar[i];
/* 1789 */         if ((localLocalVar1 != null) && (localLocalVar1.sym.type == paramUninitializedType)) {
/* 1790 */           Symbol.VarSymbol localVarSymbol = localLocalVar1.sym;
/* 1791 */           localVarSymbol = localVarSymbol.clone(localVarSymbol.owner);
/* 1792 */           localVarSymbol.type = localType;
/* 1793 */           Code.LocalVar localLocalVar2 = Code.this.lvar[i] =  = new Code.LocalVar(localVarSymbol);
/* 1794 */           localLocalVar2.aliveRanges = localLocalVar1.aliveRanges;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     State join(State paramState) {
/* 1800 */       this.defined.andSet(paramState.defined);
/* 1801 */       Assert.check((this.stacksize == paramState.stacksize) && (this.nlocks == paramState.nlocks));
/*      */ 
/* 1803 */       for (int i = 0; i < this.stacksize; ) {
/* 1804 */         Type localType1 = this.stack[i];
/* 1805 */         Type localType2 = paramState.stack[i];
/*      */ 
/* 1810 */         Type localType3 = Code.this.types
/* 1809 */           .isSubtype(localType2, localType1) ? 
/* 1809 */           localType1 : Code.this.types
/* 1808 */           .isSubtype(localType1, localType2) ? 
/* 1808 */           localType2 : localType1 == localType2 ? localType1 : 
/* 1810 */           error();
/* 1811 */         int j = Code.width(localType3);
/* 1812 */         this.stack[i] = localType3;
/* 1813 */         if (j == 2) Assert.checkNull(this.stack[(i + 1)]);
/* 1814 */         i += j;
/*      */       }
/* 1816 */       return this;
/*      */     }
/*      */ 
/*      */     Type error() {
/* 1820 */       throw new AssertionError("inconsistent stack types at join point");
/*      */     }
/*      */ 
/*      */     void dump() {
/* 1824 */       dump(-1);
/*      */     }
/*      */ 
/*      */     void dump(int paramInt) {
/* 1828 */       System.err.print("stackMap for " + Code.this.meth.owner + "." + Code.this.meth);
/* 1829 */       if (paramInt == -1)
/* 1830 */         System.out.println();
/*      */       else
/* 1832 */         System.out.println(" at " + paramInt);
/* 1833 */       System.err.println(" stack (from bottom):");
/* 1834 */       for (int i = 0; i < this.stacksize; i++) {
/* 1835 */         System.err.println("  " + i + ": " + this.stack[i]);
/*      */       }
/* 1837 */       i = 0;
/* 1838 */       for (int j = Code.this.max_locals - 1; j >= 0; j--) {
/* 1839 */         if (this.defined.isMember(j)) {
/* 1840 */           i = j;
/* 1841 */           break;
/*      */         }
/*      */       }
/* 1844 */       if (i >= 0)
/* 1845 */         System.err.println(" locals:");
/* 1846 */       for (j = 0; j <= i; j++) {
/* 1847 */         System.err.print("  " + j + ": ");
/* 1848 */         if (this.defined.isMember(j)) {
/* 1849 */           Code.LocalVar localLocalVar = Code.this.lvar[j];
/* 1850 */           if (localLocalVar == null)
/* 1851 */             System.err.println("(none)");
/* 1852 */           else if (localLocalVar.sym == null)
/* 1853 */             System.err.println("UNKNOWN!");
/*      */           else
/* 1855 */             System.err.println("" + localLocalVar.sym + " of type " + localLocalVar.sym
/* 1856 */               .erasure(Code.this.types));
/*      */         }
/*      */         else {
/* 1858 */           System.err.println("undefined");
/*      */         }
/*      */       }
/* 1861 */       if (this.nlocks != 0) {
/* 1862 */         System.err.print(" locks:");
/* 1863 */         for (j = 0; j < this.nlocks; j++) {
/* 1864 */           System.err.print(" " + this.locks[j]);
/*      */         }
/* 1866 */         System.err.println();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Code
 * JD-Core Version:    0.6.2
 */