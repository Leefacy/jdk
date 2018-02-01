/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class AndExpression extends BinaryLogicalExpression
/*     */ {
/*     */   public AndExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  44 */     super(15, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars)
/*     */   {
/*  59 */     this.left.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, paramConditionVars);
/*  60 */     this.left = convert(paramEnvironment, paramContext, Type.tBoolean, this.left);
/*  61 */     Vset localVset1 = paramConditionVars.vsTrue.copy();
/*  62 */     Vset localVset2 = paramConditionVars.vsFalse.copy();
/*     */ 
/*  65 */     this.right.checkCondition(paramEnvironment, paramContext, localVset1, paramHashtable, paramConditionVars);
/*  66 */     this.right = convert(paramEnvironment, paramContext, Type.tBoolean, this.right);
/*     */ 
/*  71 */     paramConditionVars.vsFalse = paramConditionVars.vsFalse.join(localVset2);
/*     */   }
/*     */ 
/*     */   Expression eval(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  78 */     return new BooleanExpression(this.where, (paramBoolean1) && (paramBoolean2));
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/*  85 */     if (this.left.equals(true)) {
/*  86 */       return this.right;
/*     */     }
/*  88 */     if (this.right.equals(false))
/*     */     {
/*  90 */       return new CommaExpression(this.where, this.left, this.right).simplify();
/*     */     }
/*  92 */     if (this.right.equals(true)) {
/*  93 */       return this.left;
/*     */     }
/*  95 */     if (this.left.equals(false)) {
/*  96 */       return this.left;
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean)
/*     */   {
/* 105 */     if (paramBoolean) {
/* 106 */       Label localLabel = new Label();
/* 107 */       this.left.codeBranch(paramEnvironment, paramContext, paramAssembler, localLabel, false);
/* 108 */       this.right.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, true);
/* 109 */       paramAssembler.add(localLabel);
/*     */     } else {
/* 111 */       this.left.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, false);
/* 112 */       this.right.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, false);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.AndExpression
 * JD-Core Version:    0.6.2
 */