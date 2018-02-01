/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.MethodType;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ 
/*     */ public class Items
/*     */ {
/*     */   Pool pool;
/*     */   Code code;
/*     */   Symtab syms;
/*     */   Types types;
/*     */   private final Item voidItem;
/*     */   private final Item thisItem;
/*     */   private final Item superItem;
/*  73 */   private final Item[] stackItem = new Item[9];
/*     */ 
/*     */   public Items(Pool paramPool, Code paramCode, Symtab paramSymtab, Types paramTypes) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 149	java/lang/Object:<init>	()V
/*     */     //   4: aload_0
/*     */     //   5: bipush 9
/*     */     //   7: anewarray 67	com/sun/tools/javac/jvm/Items$Item
/*     */     //   10: putfield 133	com/sun/tools/javac/jvm/Items:stackItem	[Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   13: aload_0
/*     */     //   14: aload_2
/*     */     //   15: putfield 129	com/sun/tools/javac/jvm/Items:code	Lcom/sun/tools/javac/jvm/Code;
/*     */     //   18: aload_0
/*     */     //   19: aload_1
/*     */     //   20: putfield 134	com/sun/tools/javac/jvm/Items:pool	Lcom/sun/tools/javac/jvm/Pool;
/*     */     //   23: aload_0
/*     */     //   24: aload 4
/*     */     //   26: putfield 128	com/sun/tools/javac/jvm/Items:types	Lcom/sun/tools/javac/code/Types;
/*     */     //   29: aload_0
/*     */     //   30: new 61	com/sun/tools/javac/jvm/Items$1
/*     */     //   33: dup
/*     */     //   34: aload_0
/*     */     //   35: bipush 8
/*     */     //   37: invokespecial 138	com/sun/tools/javac/jvm/Items$1:<init>	(Lcom/sun/tools/javac/jvm/Items;I)V
/*     */     //   40: putfield 132	com/sun/tools/javac/jvm/Items:voidItem	Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   43: aload_0
/*     */     //   44: new 70	com/sun/tools/javac/jvm/Items$SelfItem
/*     */     //   47: dup
/*     */     //   48: aload_0
/*     */     //   49: iconst_0
/*     */     //   50: invokespecial 146	com/sun/tools/javac/jvm/Items$SelfItem:<init>	(Lcom/sun/tools/javac/jvm/Items;Z)V
/*     */     //   53: putfield 131	com/sun/tools/javac/jvm/Items:thisItem	Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   56: aload_0
/*     */     //   57: new 70	com/sun/tools/javac/jvm/Items$SelfItem
/*     */     //   60: dup
/*     */     //   61: aload_0
/*     */     //   62: iconst_1
/*     */     //   63: invokespecial 146	com/sun/tools/javac/jvm/Items$SelfItem:<init>	(Lcom/sun/tools/javac/jvm/Items;Z)V
/*     */     //   66: putfield 130	com/sun/tools/javac/jvm/Items:superItem	Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   69: iconst_0
/*     */     //   70: istore 5
/*     */     //   72: iload 5
/*     */     //   74: bipush 8
/*     */     //   76: if_icmpge +26 -> 102
/*     */     //   79: aload_0
/*     */     //   80: getfield 133	com/sun/tools/javac/jvm/Items:stackItem	[Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   83: iload 5
/*     */     //   85: new 71	com/sun/tools/javac/jvm/Items$StackItem
/*     */     //   88: dup
/*     */     //   89: aload_0
/*     */     //   90: iload 5
/*     */     //   92: invokespecial 147	com/sun/tools/javac/jvm/Items$StackItem:<init>	(Lcom/sun/tools/javac/jvm/Items;I)V
/*     */     //   95: aastore
/*     */     //   96: iinc 5 1
/*     */     //   99: goto -27 -> 72
/*     */     //   102: aload_0
/*     */     //   103: getfield 133	com/sun/tools/javac/jvm/Items:stackItem	[Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   106: bipush 8
/*     */     //   108: aload_0
/*     */     //   109: getfield 132	com/sun/tools/javac/jvm/Items:voidItem	Lcom/sun/tools/javac/jvm/Items$Item;
/*     */     //   112: aastore
/*     */     //   113: aload_0
/*     */     //   114: aload_3
/*     */     //   115: putfield 127	com/sun/tools/javac/jvm/Items:syms	Lcom/sun/tools/javac/code/Symtab;
/*     */     //   118: return } 
/*  92 */   Item makeVoidItem() { return this.voidItem; }
/*     */ 
/*     */ 
/*     */   Item makeThisItem()
/*     */   {
/*  97 */     return this.thisItem;
/*     */   }
/*     */ 
/*     */   Item makeSuperItem()
/*     */   {
/* 103 */     return this.superItem;
/*     */   }
/*     */ 
/*     */   Item makeStackItem(Type paramType)
/*     */   {
/* 110 */     return this.stackItem[Code.typecode(paramType)];
/*     */   }
/*     */ 
/*     */   Item makeDynamicItem(Symbol paramSymbol)
/*     */   {
/* 117 */     return new DynamicItem(paramSymbol);
/*     */   }
/*     */ 
/*     */   Item makeIndexedItem(Type paramType)
/*     */   {
/* 124 */     return new IndexedItem(paramType);
/*     */   }
/*     */ 
/*     */   LocalItem makeLocalItem(Symbol.VarSymbol paramVarSymbol)
/*     */   {
/* 131 */     return new LocalItem(paramVarSymbol.erasure(this.types), paramVarSymbol.adr);
/*     */   }
/*     */ 
/*     */   private LocalItem makeLocalItem(Type paramType, int paramInt)
/*     */   {
/* 139 */     return new LocalItem(paramType, paramInt);
/*     */   }
/*     */ 
/*     */   Item makeStaticItem(Symbol paramSymbol)
/*     */   {
/* 146 */     return new StaticItem(paramSymbol);
/*     */   }
/*     */ 
/*     */   Item makeMemberItem(Symbol paramSymbol, boolean paramBoolean)
/*     */   {
/* 155 */     return new MemberItem(paramSymbol, paramBoolean);
/*     */   }
/*     */ 
/*     */   Item makeImmediateItem(Type paramType, Object paramObject)
/*     */   {
/* 163 */     return new ImmediateItem(paramType, paramObject);
/*     */   }
/*     */ 
/*     */   Item makeAssignItem(Item paramItem)
/*     */   {
/* 170 */     return new AssignItem(paramItem);
/*     */   }
/*     */ 
/*     */   CondItem makeCondItem(int paramInt, Code.Chain paramChain1, Code.Chain paramChain2)
/*     */   {
/* 181 */     return new CondItem(paramInt, paramChain1, paramChain2);
/*     */   }
/*     */ 
/*     */   CondItem makeCondItem(int paramInt)
/*     */   {
/* 188 */     return makeCondItem(paramInt, null, null);
/*     */   }
/*     */ 
/*     */   class AssignItem extends Items.Item
/*     */   {
/*     */     Items.Item lhs;
/*     */ 
/*     */     AssignItem(Items.Item arg2)
/*     */     {
/* 695 */       super(localObject.typecode);
/* 696 */       this.lhs = localObject;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 700 */       this.lhs.stash(this.typecode);
/* 701 */       this.lhs.store();
/* 702 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void duplicate() {
/* 706 */       load().duplicate();
/*     */     }
/*     */ 
/*     */     void drop() {
/* 710 */       this.lhs.store();
/*     */     }
/*     */ 
/*     */     void stash(int paramInt) {
/* 714 */       Assert.error();
/*     */     }
/*     */ 
/*     */     int width() {
/* 718 */       return this.lhs.width() + Code.width(this.typecode);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 722 */       return "assign(lhs = " + this.lhs + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class CondItem extends Items.Item
/*     */   {
/*     */     Code.Chain trueJumps;
/*     */     Code.Chain falseJumps;
/*     */     int opcode;
/*     */     JCTree tree;
/*     */ 
/*     */     CondItem(int paramChain1, Code.Chain paramChain2, Code.Chain arg4)
/*     */     {
/* 751 */       super(5);
/* 752 */       this.opcode = paramChain1;
/* 753 */       this.trueJumps = paramChain2;
/*     */       Object localObject;
/* 754 */       this.falseJumps = localObject;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 758 */       Code.Chain localChain1 = null;
/* 759 */       Code.Chain localChain2 = jumpFalse();
/* 760 */       if (!isFalse()) {
/* 761 */         Items.this.code.resolve(this.trueJumps);
/* 762 */         Items.this.code.emitop0(4);
/* 763 */         localChain1 = Items.this.code.branch(167);
/*     */       }
/* 765 */       if (localChain2 != null) {
/* 766 */         Items.this.code.resolve(localChain2);
/* 767 */         Items.this.code.emitop0(3);
/*     */       }
/* 769 */       Items.this.code.resolve(localChain1);
/* 770 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void duplicate() {
/* 774 */       load().duplicate();
/*     */     }
/*     */ 
/*     */     void drop() {
/* 778 */       load().drop();
/*     */     }
/*     */ 
/*     */     void stash(int paramInt) {
/* 782 */       Assert.error();
/*     */     }
/*     */ 
/*     */     CondItem mkCond() {
/* 786 */       return this;
/*     */     }
/*     */ 
/*     */     Code.Chain jumpTrue() {
/* 790 */       if (this.tree == null) return Code.mergeChains(this.trueJumps, Items.this.code.branch(this.opcode));
/*     */ 
/* 792 */       int i = Items.this.code.curCP();
/* 793 */       Code.Chain localChain = Code.mergeChains(this.trueJumps, Items.this.code.branch(this.opcode));
/* 794 */       Items.this.code.crt.put(this.tree, 128, i, Items.this.code.curCP());
/* 795 */       return localChain;
/*     */     }
/*     */ 
/*     */     Code.Chain jumpFalse() {
/* 799 */       if (this.tree == null) return Code.mergeChains(this.falseJumps, Items.this.code.branch(Code.negate(this.opcode)));
/*     */ 
/* 801 */       int i = Items.this.code.curCP();
/* 802 */       Code.Chain localChain = Code.mergeChains(this.falseJumps, Items.this.code.branch(Code.negate(this.opcode)));
/* 803 */       Items.this.code.crt.put(this.tree, 256, i, Items.this.code.curCP());
/* 804 */       return localChain;
/*     */     }
/*     */ 
/*     */     CondItem negate() {
/* 808 */       CondItem localCondItem = new CondItem(Items.this, Code.negate(this.opcode), this.falseJumps, this.trueJumps);
/* 809 */       localCondItem.tree = this.tree;
/* 810 */       return localCondItem;
/*     */     }
/*     */ 
/*     */     int width()
/*     */     {
/* 815 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     boolean isTrue() {
/* 819 */       return (this.falseJumps == null) && (this.opcode == 167);
/*     */     }
/*     */ 
/*     */     boolean isFalse() {
/* 823 */       return (this.trueJumps == null) && (this.opcode == 168);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 827 */       return "cond(" + Code.mnem(this.opcode) + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class DynamicItem extends Items.StaticItem
/*     */   {
/*     */     DynamicItem(Symbol arg2)
/*     */     {
/* 471 */       super(localSymbol);
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 475 */       if (!$assertionsDisabled) throw new AssertionError();
/* 476 */       return null;
/*     */     }
/*     */ 
/*     */     void store() {
/* 480 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/*     */ 
/*     */     Items.Item invoke()
/*     */     {
/* 485 */       Type.MethodType localMethodType = (Type.MethodType)this.member.erasure(Items.this.types);
/* 486 */       int i = Code.typecode(localMethodType.restype);
/* 487 */       Items.this.code.emitInvokedynamic(Items.this.pool.put(this.member), localMethodType);
/* 488 */       return Items.this.stackItem[i];
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 492 */       return "dynamic(" + this.member + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class ImmediateItem extends Items.Item
/*     */   {
/*     */     Object value;
/*     */ 
/*     */     ImmediateItem(Type paramObject, Object arg3)
/*     */     {
/* 566 */       super(Code.typecode(paramObject));
/*     */       Object localObject;
/* 567 */       this.value = localObject;
/*     */     }
/*     */ 
/*     */     private void ldc() {
/* 571 */       int i = Items.this.pool.put(this.value);
/* 572 */       if ((this.typecode == 1) || (this.typecode == 3))
/* 573 */         Items.this.code.emitop2(20, i);
/*     */       else
/* 575 */         Items.this.code.emitLdc(i);
/*     */     }
/*     */ 
/*     */     Items.Item load()
/*     */     {
/* 580 */       switch (this.typecode) { case 0:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 582 */         int i = ((Number)this.value).intValue();
/* 583 */         if ((-1 <= i) && (i <= 5))
/* 584 */           Items.this.code.emitop0(3 + i);
/* 585 */         else if ((-128 <= i) && (i <= 127))
/* 586 */           Items.this.code.emitop1(16, i);
/* 587 */         else if ((-32768 <= i) && (i <= 32767))
/* 588 */           Items.this.code.emitop2(17, i);
/*     */         else
/* 590 */           ldc();
/* 591 */         break;
/*     */       case 1:
/* 593 */         long l = ((Number)this.value).longValue();
/* 594 */         if ((l == 0L) || (l == 1L))
/* 595 */           Items.this.code.emitop0(9 + (int)l);
/*     */         else
/* 597 */           ldc();
/* 598 */         break;
/*     */       case 2:
/* 600 */         float f = ((Number)this.value).floatValue();
/* 601 */         if ((isPosZero(f)) || (f == 1.0D) || (f == 2.0D))
/* 602 */           Items.this.code.emitop0(11 + (int)f);
/*     */         else {
/* 604 */           ldc();
/*     */         }
/* 606 */         break;
/*     */       case 3:
/* 608 */         double d = ((Number)this.value).doubleValue();
/* 609 */         if ((isPosZero(d)) || (d == 1.0D))
/* 610 */           Items.this.code.emitop0(14 + (int)d);
/*     */         else
/* 612 */           ldc();
/* 613 */         break;
/*     */       case 4:
/* 615 */         ldc();
/* 616 */         break;
/*     */       default:
/* 618 */         Assert.error();
/*     */       }
/* 620 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     private boolean isPosZero(float paramFloat)
/*     */     {
/* 626 */       return (paramFloat == 0.0F) && (1.0F / paramFloat > 0.0F);
/*     */     }
/*     */ 
/*     */     private boolean isPosZero(double paramDouble)
/*     */     {
/* 631 */       return (paramDouble == 0.0D) && (1.0D / paramDouble > 0.0D);
/*     */     }
/*     */ 
/*     */     Items.CondItem mkCond() {
/* 635 */       int i = ((Number)this.value).intValue();
/* 636 */       return Items.this.makeCondItem(i != 0 ? 167 : 168);
/*     */     }
/*     */ 
/*     */     Items.Item coerce(int paramInt) {
/* 640 */       if (this.typecode == paramInt) {
/* 641 */         return this;
/*     */       }
/* 643 */       switch (paramInt) {
/*     */       case 0:
/* 645 */         if (Code.truncate(this.typecode) == 0) {
/* 646 */           return this;
/*     */         }
/*     */ 
/* 650 */         return new ImmediateItem(Items.this, Items.this.syms.intType, 
/* 650 */           Integer.valueOf(((Number)this.value)
/* 650 */           .intValue()));
/*     */       case 1:
/* 654 */         return new ImmediateItem(Items.this, Items.this.syms.longType, 
/* 654 */           Long.valueOf(((Number)this.value)
/* 654 */           .longValue()));
/*     */       case 2:
/* 658 */         return new ImmediateItem(Items.this, Items.this.syms.floatType, 
/* 658 */           Float.valueOf(((Number)this.value)
/* 658 */           .floatValue()));
/*     */       case 3:
/* 662 */         return new ImmediateItem(Items.this, Items.this.syms.doubleType, 
/* 662 */           Double.valueOf(((Number)this.value)
/* 662 */           .doubleValue()));
/*     */       case 5:
/* 666 */         return new ImmediateItem(Items.this, Items.this.syms.byteType, 
/* 666 */           Integer.valueOf((byte)((Number)this.value)
/* 666 */           .intValue()));
/*     */       case 6:
/* 670 */         return new ImmediateItem(Items.this, Items.this.syms.charType, 
/* 670 */           Integer.valueOf((char)((Number)this.value)
/* 670 */           .intValue()));
/*     */       case 7:
/* 674 */         return new ImmediateItem(Items.this, Items.this.syms.shortType, 
/* 674 */           Integer.valueOf((short)((Number)this.value)
/* 674 */           .intValue()));
/*     */       case 4:
/* 676 */       }return super.coerce(paramInt);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 682 */       return "immediate(" + this.value + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class IndexedItem extends Items.Item
/*     */   {
/*     */     IndexedItem(Type arg2)
/*     */     {
/* 320 */       super(Code.typecode(localType));
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 324 */       Items.this.code.emitop0(46 + this.typecode);
/* 325 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void store() {
/* 329 */       Items.this.code.emitop0(79 + this.typecode);
/*     */     }
/*     */ 
/*     */     void duplicate() {
/* 333 */       Items.this.code.emitop0(92);
/*     */     }
/*     */ 
/*     */     void drop() {
/* 337 */       Items.this.code.emitop0(88);
/*     */     }
/*     */ 
/*     */     void stash(int paramInt) {
/* 341 */       Items.this.code.emitop0(91 + 3 * (Code.width(paramInt) - 1));
/*     */     }
/*     */ 
/*     */     int width() {
/* 345 */       return 2;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 349 */       return "indexed(" + ByteCodes.typecodeNames[this.typecode] + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract class Item
/*     */   {
/*     */     int typecode;
/*     */ 
/*     */     Item(int arg2)
/*     */     {
/*     */       int i;
/* 200 */       this.typecode = i;
/*     */     }
/*     */ 
/*     */     Item load()
/*     */     {
/* 206 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     void store()
/*     */     {
/* 212 */       throw new AssertionError("store unsupported: " + this);
/*     */     }
/*     */ 
/*     */     Item invoke()
/*     */     {
/* 218 */       throw new AssertionError(this);
/*     */     }
/*     */ 
/*     */     void duplicate()
/*     */     {
/*     */     }
/*     */ 
/*     */     void drop()
/*     */     {
/*     */     }
/*     */ 
/*     */     void stash(int paramInt)
/*     */     {
/* 233 */       Items.this.stackItem[paramInt].duplicate();
/*     */     }
/*     */ 
/*     */     Items.CondItem mkCond()
/*     */     {
/* 239 */       load();
/* 240 */       return Items.this.makeCondItem(154);
/*     */     }
/*     */ 
/*     */     Item coerce(int paramInt)
/*     */     {
/* 247 */       if (this.typecode == paramInt) {
/* 248 */         return this;
/*     */       }
/* 250 */       load();
/* 251 */       int i = Code.truncate(this.typecode);
/* 252 */       int j = Code.truncate(paramInt);
/* 253 */       if (i != j) {
/* 254 */         int k = j > i ? j - 1 : j;
/*     */ 
/* 256 */         Items.this.code.emitop0(133 + i * 3 + k);
/*     */       }
/* 258 */       if (paramInt != j) {
/* 259 */         Items.this.code.emitop0(145 + paramInt - 5);
/*     */       }
/* 261 */       return Items.this.stackItem[paramInt];
/*     */     }
/*     */ 
/*     */     Item coerce(Type paramType)
/*     */     {
/* 269 */       return coerce(Code.typecode(paramType));
/*     */     }
/*     */ 
/*     */     int width()
/*     */     {
/* 275 */       return 0;
/*     */     }
/*     */ 
/*     */     public abstract String toString();
/*     */   }
/*     */ 
/*     */   class LocalItem extends Items.Item
/*     */   {
/*     */     int reg;
/*     */     Type type;
/*     */ 
/*     */     LocalItem(Type paramInt, int arg3)
/*     */     {
/* 389 */       super(Code.typecode(paramInt));
/*     */       int i;
/* 390 */       Assert.check(i >= 0);
/* 391 */       this.type = paramInt;
/* 392 */       this.reg = i;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 396 */       if (this.reg <= 3)
/* 397 */         Items.this.code.emitop0(26 + Code.truncate(this.typecode) * 4 + this.reg);
/*     */       else
/* 399 */         Items.this.code.emitop1w(21 + Code.truncate(this.typecode), this.reg);
/* 400 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void store() {
/* 404 */       if (this.reg <= 3)
/* 405 */         Items.this.code.emitop0(59 + Code.truncate(this.typecode) * 4 + this.reg);
/*     */       else
/* 407 */         Items.this.code.emitop1w(54 + Code.truncate(this.typecode), this.reg);
/* 408 */       Items.this.code.setDefined(this.reg);
/*     */     }
/*     */ 
/*     */     void incr(int paramInt) {
/* 412 */       if ((this.typecode == 0) && (paramInt >= -32768) && (paramInt <= 32767)) {
/* 413 */         Items.this.code.emitop1w(132, this.reg, paramInt);
/*     */       } else {
/* 415 */         load();
/* 416 */         if (paramInt >= 0) {
/* 417 */           Items.this.makeImmediateItem(Items.this.syms.intType, Integer.valueOf(paramInt)).load();
/* 418 */           Items.this.code.emitop0(96);
/*     */         } else {
/* 420 */           Items.this.makeImmediateItem(Items.this.syms.intType, Integer.valueOf(-paramInt)).load();
/* 421 */           Items.this.code.emitop0(100);
/*     */         }
/* 423 */         Items.this.makeStackItem(Items.this.syms.intType).coerce(this.typecode);
/* 424 */         store();
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 429 */       return "localItem(type=" + this.type + "; reg=" + this.reg + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class MemberItem extends Items.Item
/*     */   {
/*     */     Symbol member;
/*     */     boolean nonvirtual;
/*     */ 
/*     */     MemberItem(Symbol paramBoolean, boolean arg3)
/*     */     {
/* 509 */       super(Code.typecode(paramBoolean.erasure(Items.this.types)));
/* 510 */       this.member = paramBoolean;
/*     */       boolean bool;
/* 511 */       this.nonvirtual = bool;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 515 */       Items.this.code.emitop2(180, Items.this.pool.put(this.member));
/* 516 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void store() {
/* 520 */       Items.this.code.emitop2(181, Items.this.pool.put(this.member));
/*     */     }
/*     */ 
/*     */     Items.Item invoke() {
/* 524 */       Type.MethodType localMethodType = (Type.MethodType)this.member.externalType(Items.this.types);
/* 525 */       int i = Code.typecode(localMethodType.restype);
/* 526 */       if (((this.member.owner.flags() & 0x200) != 0L) && (!this.nonvirtual))
/* 527 */         Items.this.code.emitInvokeinterface(Items.this.pool.put(this.member), localMethodType);
/* 528 */       else if (this.nonvirtual)
/* 529 */         Items.this.code.emitInvokespecial(Items.this.pool.put(this.member), localMethodType);
/*     */       else {
/* 531 */         Items.this.code.emitInvokevirtual(Items.this.pool.put(this.member), localMethodType);
/*     */       }
/* 533 */       return Items.this.stackItem[i];
/*     */     }
/*     */ 
/*     */     void duplicate() {
/* 537 */       Items.this.stackItem[4].duplicate();
/*     */     }
/*     */ 
/*     */     void drop() {
/* 541 */       Items.this.stackItem[4].drop();
/*     */     }
/*     */ 
/*     */     void stash(int paramInt) {
/* 545 */       Items.this.stackItem[4].stash(paramInt);
/*     */     }
/*     */ 
/*     */     int width() {
/* 549 */       return 1;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 553 */       return "member(" + this.member + (this.nonvirtual ? " nonvirtual)" : ")");
/*     */     }
/*     */   }
/*     */ 
/*     */   class SelfItem extends Items.Item
/*     */   {
/*     */     boolean isSuper;
/*     */ 
/*     */     SelfItem(boolean arg2)
/*     */     {
/* 362 */       super(4);
/*     */       boolean bool;
/* 363 */       this.isSuper = bool;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 367 */       Items.this.code.emitop0(42);
/* 368 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 372 */       return this.isSuper ? "super" : "this";
/*     */     }
/*     */   }
/*     */ 
/*     */   class StackItem extends Items.Item
/*     */   {
/*     */     StackItem(int arg2)
/*     */     {
/* 286 */       super(i);
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 290 */       return this;
/*     */     }
/*     */ 
/*     */     void duplicate() {
/* 294 */       Items.this.code.emitop0(width() == 2 ? 92 : 89);
/*     */     }
/*     */ 
/*     */     void drop() {
/* 298 */       Items.this.code.emitop0(width() == 2 ? 88 : 87);
/*     */     }
/*     */ 
/*     */     void stash(int paramInt) {
/* 302 */       Items.this.code.emitop0(
/* 303 */         (width() == 2 ? 91 : 90) + 3 * (Code.width(paramInt) - 1));
/*     */     }
/*     */ 
/*     */     int width() {
/* 307 */       return Code.width(this.typecode);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 311 */       return "stack(" + ByteCodes.typecodeNames[this.typecode] + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   class StaticItem extends Items.Item
/*     */   {
/*     */     Symbol member;
/*     */ 
/*     */     StaticItem(Symbol arg2)
/*     */     {
/* 442 */       super(Code.typecode(localObject.erasure(Items.this.types)));
/* 443 */       this.member = localObject;
/*     */     }
/*     */ 
/*     */     Items.Item load() {
/* 447 */       Items.this.code.emitop2(178, Items.this.pool.put(this.member));
/* 448 */       return Items.this.stackItem[this.typecode];
/*     */     }
/*     */ 
/*     */     void store() {
/* 452 */       Items.this.code.emitop2(179, Items.this.pool.put(this.member));
/*     */     }
/*     */ 
/*     */     Items.Item invoke() {
/* 456 */       Type.MethodType localMethodType = (Type.MethodType)this.member.erasure(Items.this.types);
/* 457 */       int i = Code.typecode(localMethodType.restype);
/* 458 */       Items.this.code.emitInvokestatic(Items.this.pool.put(this.member), localMethodType);
/* 459 */       return Items.this.stackItem[i];
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 463 */       return "static(" + this.member + ")";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Items
 * JD-Core Version:    0.6.2
 */