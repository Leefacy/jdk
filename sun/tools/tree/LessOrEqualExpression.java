/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class LessOrEqualExpression extends BinaryCompareExpression
/*     */ {
/*     */   public LessOrEqualExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  43 */     super(23, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   Expression eval(int paramInt1, int paramInt2)
/*     */   {
/*  50 */     return new BooleanExpression(this.where, paramInt1 <= paramInt2);
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/*  53 */     return new BooleanExpression(this.where, paramLong1 <= paramLong2);
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/*  56 */     return new BooleanExpression(this.where, paramFloat1 <= paramFloat2);
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/*  59 */     return new BooleanExpression(this.where, paramDouble1 <= paramDouble2);
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/*  66 */     if ((this.left.isConstant()) && (!this.right.isConstant())) {
/*  67 */       return new GreaterOrEqualExpression(this.where, this.right, this.left);
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean)
/*     */   {
/*  76 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  77 */     switch (this.left.type.getTypeCode()) {
/*     */     case 4:
/*  79 */       if (!this.right.equals(0)) { this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  81 */         paramAssembler.add(this.where, paramBoolean ? 164 : 163, paramLabel, paramBoolean);
/*     */         return; }
/*     */       break;
/*     */     case 5:
/*  86 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  87 */       paramAssembler.add(this.where, 148);
/*  88 */       break;
/*     */     case 6:
/*  90 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  91 */       paramAssembler.add(this.where, 150);
/*  92 */       break;
/*     */     case 7:
/*  94 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  95 */       paramAssembler.add(this.where, 152);
/*  96 */       break;
/*     */     default:
/*  98 */       throw new CompilerError("Unexpected Type");
/*     */     }
/* 100 */     paramAssembler.add(this.where, paramBoolean ? 158 : 157, paramLabel, paramBoolean);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.LessOrEqualExpression
 * JD-Core Version:    0.6.2
 */