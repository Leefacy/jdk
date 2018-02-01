/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class ForStatement extends Statement
/*     */ {
/*     */   Statement init;
/*     */   Expression cond;
/*     */   Expression inc;
/*     */   Statement body;
/*     */ 
/*     */   public ForStatement(long paramLong, Statement paramStatement1, Expression paramExpression1, Expression paramExpression2, Statement paramStatement2)
/*     */   {
/*  50 */     super(92, paramLong);
/*  51 */     this.init = paramStatement1;
/*  52 */     this.cond = paramExpression1;
/*  53 */     this.inc = paramExpression2;
/*  54 */     this.body = paramStatement2;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  61 */     checkLabel(paramEnvironment, paramContext);
/*  62 */     paramVset = reach(paramEnvironment, paramVset);
/*  63 */     Context localContext = new Context(paramContext, this);
/*  64 */     if (this.init != null) {
/*  65 */       paramVset = this.init.checkBlockStatement(paramEnvironment, localContext, paramVset, paramHashtable);
/*     */     }
/*  67 */     CheckContext localCheckContext = new CheckContext(localContext, this);
/*     */ 
/*  69 */     Vset localVset = paramVset.copy();
/*     */     ConditionVars localConditionVars;
/*  71 */     if (this.cond != null) {
/*  72 */       localConditionVars = this.cond.checkCondition(paramEnvironment, localCheckContext, paramVset, paramHashtable);
/*  73 */       this.cond = convert(paramEnvironment, localCheckContext, Type.tBoolean, this.cond);
/*     */     }
/*     */     else {
/*  76 */       localConditionVars = new ConditionVars();
/*  77 */       localConditionVars.vsFalse = Vset.DEAD_END;
/*  78 */       localConditionVars.vsTrue = paramVset;
/*     */     }
/*  80 */     paramVset = this.body.check(paramEnvironment, localCheckContext, localConditionVars.vsTrue, paramHashtable);
/*  81 */     paramVset = paramVset.join(localCheckContext.vsContinue);
/*  82 */     if (this.inc != null) {
/*  83 */       paramVset = this.inc.check(paramEnvironment, localCheckContext, paramVset, paramHashtable);
/*     */     }
/*     */ 
/*  88 */     localContext.checkBackBranch(paramEnvironment, this, localVset, paramVset);
/*     */ 
/*  90 */     paramVset = localCheckContext.vsBreak.join(localConditionVars.vsFalse);
/*  91 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  98 */     paramContext = new Context(paramContext, this);
/*  99 */     if (this.init != null) {
/* 100 */       Statement[] arrayOfStatement = { this.init, this };
/* 101 */       this.init = null;
/* 102 */       return new CompoundStatement(this.where, arrayOfStatement).inline(paramEnvironment, paramContext);
/*     */     }
/* 104 */     if (this.cond != null) {
/* 105 */       this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/*     */     }
/* 107 */     if (this.body != null) {
/* 108 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/* 110 */     if (this.inc != null) {
/* 111 */       this.inc = this.inc.inline(paramEnvironment, paramContext);
/*     */     }
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 120 */     ForStatement localForStatement = (ForStatement)clone();
/* 121 */     if (this.init != null) {
/* 122 */       localForStatement.init = this.init.copyInline(paramContext, paramBoolean);
/*     */     }
/* 124 */     if (this.cond != null) {
/* 125 */       localForStatement.cond = this.cond.copyInline(paramContext);
/*     */     }
/* 127 */     if (this.body != null) {
/* 128 */       localForStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 130 */     if (this.inc != null) {
/* 131 */       localForStatement.inc = this.inc.copyInline(paramContext);
/*     */     }
/* 133 */     return localForStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 140 */     int i = 2;
/* 141 */     if (this.init != null) {
/* 142 */       i += this.init.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 144 */     if (this.cond != null) {
/* 145 */       i += this.cond.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 147 */     if (this.body != null) {
/* 148 */       i += this.body.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 150 */     if (this.inc != null) {
/* 151 */       i += this.inc.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 153 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 160 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/* 161 */     if (this.init != null) {
/* 162 */       this.init.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/*     */ 
/* 165 */     Label localLabel1 = new Label();
/* 166 */     Label localLabel2 = new Label();
/*     */ 
/* 168 */     paramAssembler.add(this.where, 167, localLabel2);
/*     */ 
/* 170 */     paramAssembler.add(localLabel1);
/* 171 */     if (this.body != null) {
/* 172 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/*     */ 
/* 175 */     paramAssembler.add(localCodeContext.contLabel);
/* 176 */     if (this.inc != null) {
/* 177 */       this.inc.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/*     */ 
/* 180 */     paramAssembler.add(localLabel2);
/* 181 */     if (this.cond != null)
/* 182 */       this.cond.codeBranch(paramEnvironment, localCodeContext, paramAssembler, localLabel1, true);
/*     */     else {
/* 184 */       paramAssembler.add(this.where, 167, localLabel1);
/*     */     }
/* 186 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 193 */     super.print(paramPrintStream, paramInt);
/* 194 */     paramPrintStream.print("for (");
/* 195 */     if (this.init != null) {
/* 196 */       this.init.print(paramPrintStream, paramInt);
/* 197 */       paramPrintStream.print(" ");
/*     */     } else {
/* 199 */       paramPrintStream.print("; ");
/*     */     }
/* 201 */     if (this.cond != null) {
/* 202 */       this.cond.print(paramPrintStream);
/* 203 */       paramPrintStream.print(" ");
/*     */     }
/* 205 */     paramPrintStream.print("; ");
/* 206 */     if (this.inc != null) {
/* 207 */       this.inc.print(paramPrintStream);
/*     */     }
/* 209 */     paramPrintStream.print(") ");
/* 210 */     if (this.body != null)
/* 211 */       this.body.print(paramPrintStream, paramInt);
/*     */     else
/* 213 */       paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ForStatement
 * JD-Core Version:    0.6.2
 */