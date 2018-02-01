/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class DoStatement extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Expression cond;
/*     */ 
/*     */   public DoStatement(long paramLong, Statement paramStatement, Expression paramExpression)
/*     */   {
/*  48 */     super(94, paramLong);
/*  49 */     this.body = paramStatement;
/*  50 */     this.cond = paramExpression;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  57 */     checkLabel(paramEnvironment, paramContext);
/*  58 */     CheckContext localCheckContext = new CheckContext(paramContext, this);
/*     */ 
/*  60 */     Vset localVset = paramVset.copy();
/*  61 */     paramVset = this.body.check(paramEnvironment, localCheckContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  62 */     paramVset = paramVset.join(localCheckContext.vsContinue);
/*     */ 
/*  66 */     ConditionVars localConditionVars = this.cond
/*  66 */       .checkCondition(paramEnvironment, localCheckContext, paramVset, paramHashtable);
/*     */ 
/*  67 */     this.cond = convert(paramEnvironment, localCheckContext, Type.tBoolean, this.cond);
/*     */ 
/*  69 */     paramContext.checkBackBranch(paramEnvironment, this, localVset, localConditionVars.vsTrue);
/*     */ 
/*  71 */     paramVset = localCheckContext.vsBreak.join(localConditionVars.vsFalse);
/*  72 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  79 */     paramContext = new Context(paramContext, this);
/*  80 */     if (this.body != null) {
/*  81 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/*  83 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  91 */     DoStatement localDoStatement = (DoStatement)clone();
/*  92 */     localDoStatement.cond = this.cond.copyInline(paramContext);
/*  93 */     if (this.body != null) {
/*  94 */       localDoStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/*  96 */     return localDoStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 104 */     return 1 + this.cond.costInline(paramInt, paramEnvironment, paramContext) + (this.body != null ? this.body
/* 104 */       .costInline(paramInt, paramEnvironment, paramContext) : 
/* 104 */       0);
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 111 */     Label localLabel = new Label();
/* 112 */     paramAssembler.add(localLabel);
/*     */ 
/* 114 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/*     */ 
/* 116 */     if (this.body != null) {
/* 117 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/* 119 */     paramAssembler.add(localCodeContext.contLabel);
/* 120 */     this.cond.codeBranch(paramEnvironment, localCodeContext, paramAssembler, localLabel, true);
/* 121 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 128 */     super.print(paramPrintStream, paramInt);
/* 129 */     paramPrintStream.print("do ");
/* 130 */     this.body.print(paramPrintStream, paramInt);
/* 131 */     paramPrintStream.print(" while ");
/* 132 */     this.cond.print(paramPrintStream);
/* 133 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.DoStatement
 * JD-Core Version:    0.6.2
 */