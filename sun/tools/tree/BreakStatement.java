/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class BreakStatement extends Statement
/*     */ {
/*     */   Identifier lbl;
/*     */ 
/*     */   public BreakStatement(long paramLong, Identifier paramIdentifier)
/*     */   {
/*  47 */     super(98, paramLong);
/*  48 */     this.lbl = paramIdentifier;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  55 */     reach(paramEnvironment, paramVset);
/*  56 */     checkLabel(paramEnvironment, paramContext);
/*  57 */     CheckContext localCheckContext1 = (CheckContext)new CheckContext(paramContext, this).getBreakContext(this.lbl);
/*  58 */     if (localCheckContext1 != null) {
/*  59 */       if (localCheckContext1.frameNumber != paramContext.frameNumber) {
/*  60 */         paramEnvironment.error(this.where, "branch.to.uplevel", this.lbl);
/*     */       }
/*  62 */       localCheckContext1.vsBreak = localCheckContext1.vsBreak.join(paramVset);
/*     */     }
/*  64 */     else if (this.lbl != null) {
/*  65 */       paramEnvironment.error(this.where, "label.not.found", this.lbl);
/*     */     } else {
/*  67 */       paramEnvironment.error(this.where, "invalid.break");
/*     */     }
/*     */ 
/*  70 */     CheckContext localCheckContext2 = paramContext.getTryExitContext();
/*  71 */     if (localCheckContext2 != null) {
/*  72 */       localCheckContext2.vsTryExit = localCheckContext2.vsTryExit.join(paramVset);
/*     */     }
/*  74 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  81 */     return 1;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/*  88 */     CodeContext localCodeContext1 = new CodeContext(paramContext, this);
/*  89 */     CodeContext localCodeContext2 = (CodeContext)localCodeContext1.getBreakContext(this.lbl);
/*  90 */     codeFinally(paramEnvironment, paramContext, paramAssembler, localCodeContext2, null);
/*  91 */     paramAssembler.add(this.where, 167, localCodeContext2.breakLabel);
/*  92 */     paramAssembler.add(localCodeContext1.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/*  99 */     super.print(paramPrintStream, paramInt);
/* 100 */     paramPrintStream.print("break");
/* 101 */     if (this.lbl != null) {
/* 102 */       paramPrintStream.print(" " + this.lbl);
/*     */     }
/* 104 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BreakStatement
 * JD-Core Version:    0.6.2
 */