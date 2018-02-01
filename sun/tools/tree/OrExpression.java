/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class OrExpression extends BinaryLogicalExpression
/*     */ {
/*     */   public OrExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  44 */     super(14, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars)
/*     */   {
/*  59 */     this.left.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, paramConditionVars);
/*  60 */     this.left = convert(paramEnvironment, paramContext, Type.tBoolean, this.left);
/*  61 */     Vset localVset1 = paramConditionVars.vsTrue.copy();
/*  62 */     Vset localVset2 = paramConditionVars.vsFalse.copy();
/*     */ 
/*  65 */     this.right.checkCondition(paramEnvironment, paramContext, localVset2, paramHashtable, paramConditionVars);
/*  66 */     this.right = convert(paramEnvironment, paramContext, Type.tBoolean, this.right);
/*     */ 
/*  71 */     paramConditionVars.vsTrue = paramConditionVars.vsTrue.join(localVset1);
/*     */   }
/*     */ 
/*     */   Expression eval(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  78 */     return new BooleanExpression(this.where, (paramBoolean1) || (paramBoolean2));
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/*  85 */     if (this.right.equals(false)) {
/*  86 */       return this.left;
/*     */     }
/*  88 */     if (this.left.equals(true)) {
/*  89 */       return this.left;
/*     */     }
/*  91 */     if (this.left.equals(false)) {
/*  92 */       return this.right;
/*     */     }
/*  94 */     if (this.right.equals(true))
/*     */     {
/*  96 */       return new CommaExpression(this.where, this.left, this.right).simplify();
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean)
/*     */   {
/* 105 */     if (paramBoolean) {
/* 106 */       this.left.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, true);
/* 107 */       this.right.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, true);
/*     */     } else {
/* 109 */       Label localLabel = new Label();
/* 110 */       this.left.codeBranch(paramEnvironment, paramContext, paramAssembler, localLabel, true);
/* 111 */       this.right.codeBranch(paramEnvironment, paramContext, paramAssembler, paramLabel, false);
/* 112 */       paramAssembler.add(localLabel);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.OrExpression
 * JD-Core Version:    0.6.2
 */