/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class WhileStatement extends Statement
/*     */ {
/*     */   Expression cond;
/*     */   Statement body;
/*     */ 
/*     */   public WhileStatement(long paramLong, Expression paramExpression, Statement paramStatement)
/*     */   {
/*  48 */     super(93, paramLong);
/*  49 */     this.cond = paramExpression;
/*  50 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  57 */     checkLabel(paramEnvironment, paramContext);
/*  58 */     CheckContext localCheckContext = new CheckContext(paramContext, this);
/*     */ 
/*  60 */     Vset localVset = paramVset.copy();
/*     */ 
/*  64 */     ConditionVars localConditionVars = this.cond
/*  64 */       .checkCondition(paramEnvironment, localCheckContext, 
/*  64 */       reach(paramEnvironment, paramVset), 
/*  64 */       paramHashtable);
/*  65 */     this.cond = convert(paramEnvironment, localCheckContext, Type.tBoolean, this.cond);
/*     */ 
/*  67 */     paramVset = this.body.check(paramEnvironment, localCheckContext, localConditionVars.vsTrue, paramHashtable);
/*  68 */     paramVset = paramVset.join(localCheckContext.vsContinue);
/*     */ 
/*  70 */     paramContext.checkBackBranch(paramEnvironment, this, localVset, paramVset);
/*     */ 
/*  72 */     paramVset = localCheckContext.vsBreak.join(localConditionVars.vsFalse);
/*  73 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  80 */     paramContext = new Context(paramContext, this);
/*  81 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*  82 */     if (this.body != null) {
/*  83 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  93 */     return 1 + this.cond.costInline(paramInt, paramEnvironment, paramContext) + (this.body != null ? this.body
/*  93 */       .costInline(paramInt, paramEnvironment, paramContext) : 
/*  93 */       0);
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 100 */     WhileStatement localWhileStatement = (WhileStatement)clone();
/* 101 */     localWhileStatement.cond = this.cond.copyInline(paramContext);
/* 102 */     if (this.body != null) {
/* 103 */       localWhileStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 105 */     return localWhileStatement;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 112 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/*     */ 
/* 114 */     paramAssembler.add(this.where, 167, localCodeContext.contLabel);
/*     */ 
/* 116 */     Label localLabel = new Label();
/* 117 */     paramAssembler.add(localLabel);
/*     */ 
/* 119 */     if (this.body != null) {
/* 120 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/*     */ 
/* 123 */     paramAssembler.add(localCodeContext.contLabel);
/* 124 */     this.cond.codeBranch(paramEnvironment, localCodeContext, paramAssembler, localLabel, true);
/* 125 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 132 */     super.print(paramPrintStream, paramInt);
/* 133 */     paramPrintStream.print("while ");
/* 134 */     this.cond.print(paramPrintStream);
/* 135 */     if (this.body != null) {
/* 136 */       paramPrintStream.print(" ");
/* 137 */       this.body.print(paramPrintStream, paramInt);
/*     */     } else {
/* 139 */       paramPrintStream.print(";");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.WhileStatement
 * JD-Core Version:    0.6.2
 */