/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class ContinueStatement extends Statement
/*     */ {
/*     */   Identifier lbl;
/*     */ 
/*     */   public ContinueStatement(long paramLong, Identifier paramIdentifier)
/*     */   {
/*  47 */     super(99, paramLong);
/*  48 */     this.lbl = paramIdentifier;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  56 */     checkLabel(paramEnvironment, paramContext);
/*  57 */     reach(paramEnvironment, paramVset);
/*     */ 
/*  61 */     CheckContext localCheckContext1 = (CheckContext)new CheckContext(paramContext, this).getContinueContext(this.lbl);
/*  62 */     if (localCheckContext1 != null) {
/*  63 */       switch (localCheckContext1.node.op) {
/*     */       case 92:
/*     */       case 93:
/*     */       case 94:
/*  67 */         if (localCheckContext1.frameNumber != paramContext.frameNumber) {
/*  68 */           paramEnvironment.error(this.where, "branch.to.uplevel", this.lbl);
/*     */         }
/*  70 */         localCheckContext1.vsContinue = localCheckContext1.vsContinue.join(paramVset);
/*  71 */         break;
/*     */       default:
/*  73 */         paramEnvironment.error(this.where, "invalid.continue"); break;
/*     */       }
/*     */     }
/*  76 */     else if (this.lbl != null)
/*  77 */       paramEnvironment.error(this.where, "label.not.found", this.lbl);
/*     */     else {
/*  79 */       paramEnvironment.error(this.where, "invalid.continue");
/*     */     }
/*     */ 
/*  82 */     CheckContext localCheckContext2 = paramContext.getTryExitContext();
/*  83 */     if (localCheckContext2 != null) {
/*  84 */       localCheckContext2.vsTryExit = localCheckContext2.vsTryExit.join(paramVset);
/*     */     }
/*  86 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  93 */     return 1;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 100 */     CodeContext localCodeContext = (CodeContext)paramContext.getContinueContext(this.lbl);
/* 101 */     codeFinally(paramEnvironment, paramContext, paramAssembler, localCodeContext, null);
/* 102 */     paramAssembler.add(this.where, 167, localCodeContext.contLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 109 */     super.print(paramPrintStream, paramInt);
/* 110 */     paramPrintStream.print("continue");
/* 111 */     if (this.lbl != null) {
/* 112 */       paramPrintStream.print(" " + this.lbl);
/*     */     }
/* 114 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ContinueStatement
 * JD-Core Version:    0.6.2
 */