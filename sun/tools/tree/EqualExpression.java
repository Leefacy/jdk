/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class EqualExpression extends BinaryEqualityExpression
/*     */ {
/*     */   public EqualExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  43 */     super(20, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   Expression eval(int paramInt1, int paramInt2)
/*     */   {
/*  50 */     return new BooleanExpression(this.where, paramInt1 == paramInt2);
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/*  53 */     return new BooleanExpression(this.where, paramLong1 == paramLong2);
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/*  56 */     return new BooleanExpression(this.where, paramFloat1 == paramFloat2);
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/*  59 */     return new BooleanExpression(this.where, paramDouble1 == paramDouble2);
/*     */   }
/*     */   Expression eval(boolean paramBoolean1, boolean paramBoolean2) {
/*  62 */     return new BooleanExpression(this.where, paramBoolean1 == paramBoolean2);
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/*  69 */     if ((this.left.isConstant()) && (!this.right.isConstant())) {
/*  70 */       return new EqualExpression(this.where, this.right, this.left);
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean)
/*     */   {
/*  79 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  80 */     switch (this.left.type.getTypeCode()) {
/*     */     case 0:
/*     */     case 4:
/*  83 */       if (!this.right.equals(0)) { this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  85 */         paramAssembler.add(this.where, paramBoolean ? 159 : 160, paramLabel, paramBoolean);
/*     */         return; }
/*     */       break;
/*     */     case 5:
/*  90 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  91 */       paramAssembler.add(this.where, 148);
/*  92 */       break;
/*     */     case 6:
/*  94 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  95 */       paramAssembler.add(this.where, 149);
/*  96 */       break;
/*     */     case 7:
/*  98 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  99 */       paramAssembler.add(this.where, 151);
/* 100 */       break;
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 104 */       if (this.right.equals(0)) {
/* 105 */         paramAssembler.add(this.where, paramBoolean ? 198 : 199, paramLabel, paramBoolean);
/*     */       } else {
/* 107 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 108 */         paramAssembler.add(this.where, paramBoolean ? 165 : 166, paramLabel, paramBoolean);
/*     */       }
/* 110 */       return;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     default:
/* 113 */       throw new CompilerError("Unexpected Type");
/*     */     }
/* 115 */     paramAssembler.add(this.where, paramBoolean ? 153 : 154, paramLabel, paramBoolean);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.EqualExpression
 * JD-Core Version:    0.6.2
 */