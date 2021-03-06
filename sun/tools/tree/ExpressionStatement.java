/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ 
/*     */ public class ExpressionStatement extends Statement
/*     */ {
/*     */   Expression expr;
/*     */ 
/*     */   public ExpressionStatement(long paramLong, Expression paramExpression)
/*     */   {
/*  46 */     super(106, paramLong);
/*  47 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  54 */     checkLabel(paramEnvironment, paramContext);
/*  55 */     return this.expr.check(paramEnvironment, paramContext, reach(paramEnvironment, paramVset), paramHashtable);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  62 */     if (this.expr != null) {
/*  63 */       this.expr = this.expr.inline(paramEnvironment, paramContext);
/*  64 */       return this.expr == null ? null : this;
/*     */     }
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  73 */     ExpressionStatement localExpressionStatement = (ExpressionStatement)clone();
/*  74 */     localExpressionStatement.expr = this.expr.copyInline(paramContext);
/*  75 */     return localExpressionStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  82 */     return this.expr.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/*  89 */     this.expr.code(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */   public Expression firstConstructor()
/*     */   {
/*  96 */     return this.expr.firstConstructor();
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 103 */     super.print(paramPrintStream, paramInt);
/* 104 */     if (this.expr != null)
/* 105 */       this.expr.print(paramPrintStream);
/*     */     else {
/* 107 */       paramPrintStream.print("<empty>");
/*     */     }
/* 109 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ExpressionStatement
 * JD-Core Version:    0.6.2
 */