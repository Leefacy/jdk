/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ 
/*     */ public class InlineReturnStatement extends Statement
/*     */ {
/*     */   Expression expr;
/*     */ 
/*     */   public InlineReturnStatement(long paramLong, Expression paramExpression)
/*     */   {
/*  46 */     super(149, paramLong);
/*  47 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */   Context getDestination(Context paramContext)
/*     */   {
/*  54 */     for (; paramContext != null; paramContext = paramContext.prev) {
/*  55 */       if ((paramContext.node != null) && ((paramContext.node.op == 150) || (paramContext.node.op == 151))) {
/*  56 */         return paramContext;
/*     */       }
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  66 */     if (this.expr != null) {
/*  67 */       this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  76 */     InlineReturnStatement localInlineReturnStatement = (InlineReturnStatement)clone();
/*  77 */     if (this.expr != null) {
/*  78 */       localInlineReturnStatement.expr = this.expr.copyInline(paramContext);
/*     */     }
/*  80 */     return localInlineReturnStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  87 */     return 1 + (this.expr != null ? this.expr.costInline(paramInt, paramEnvironment, paramContext) : 0);
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/*  94 */     if (this.expr != null) {
/*  95 */       this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*  97 */     CodeContext localCodeContext = (CodeContext)getDestination(paramContext);
/*  98 */     paramAssembler.add(this.where, 167, localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 105 */     super.print(paramPrintStream, paramInt);
/* 106 */     paramPrintStream.print("inline-return");
/* 107 */     if (this.expr != null) {
/* 108 */       paramPrintStream.print(" ");
/* 109 */       this.expr.print(paramPrintStream);
/*     */     }
/* 111 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.InlineReturnStatement
 * JD-Core Version:    0.6.2
 */