/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class ConditionalExpression extends BinaryExpression
/*     */ {
/*     */   Expression cond;
/*     */ 
/*     */   public ConditionalExpression(long paramLong, Expression paramExpression1, Expression paramExpression2, Expression paramExpression3)
/*     */   {
/*  47 */     super(13, paramLong, Type.tError, paramExpression2, paramExpression3);
/*  48 */     this.cond = paramExpression1;
/*     */   }
/*     */ 
/*     */   public Expression order()
/*     */   {
/*  55 */     if (precedence() > this.cond.precedence()) {
/*  56 */       UnaryExpression localUnaryExpression = (UnaryExpression)this.cond;
/*  57 */       this.cond = localUnaryExpression.right;
/*  58 */       localUnaryExpression.right = order();
/*  59 */       return localUnaryExpression;
/*     */     }
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  68 */     ConditionVars localConditionVars = this.cond.checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  69 */     paramVset = this.left.checkValue(paramEnvironment, paramContext, localConditionVars.vsTrue, paramHashtable).join(this.right
/*  70 */       .checkValue(paramEnvironment, paramContext, localConditionVars.vsFalse, paramHashtable));
/*     */ 
/*  71 */     this.cond = convert(paramEnvironment, paramContext, Type.tBoolean, this.cond);
/*     */ 
/*  73 */     int i = this.left.type.getTypeMask() | this.right.type.getTypeMask();
/*  74 */     if ((i & 0x2000) != 0) {
/*  75 */       this.type = Type.tError;
/*  76 */       return paramVset;
/*     */     }
/*  78 */     if (this.left.type.equals(this.right.type))
/*  79 */       this.type = this.left.type;
/*  80 */     else if ((i & 0x80) != 0)
/*  81 */       this.type = Type.tDouble;
/*  82 */     else if ((i & 0x40) != 0)
/*  83 */       this.type = Type.tFloat;
/*  84 */     else if ((i & 0x20) != 0)
/*  85 */       this.type = Type.tLong;
/*  86 */     else if ((i & 0x700) != 0)
/*     */     {
/*     */       try
/*     */       {
/*  90 */         this.type = (paramEnvironment.implicitCast(this.right.type, this.left.type) ? this.left.type : this.right.type);
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound) {
/*  93 */         this.type = Type.tError;
/*     */       }
/*     */     }
/*  95 */     else if (((i & 0x4) != 0) && (this.left.fitsType(paramEnvironment, paramContext, Type.tChar)) && (this.right.fitsType(paramEnvironment, paramContext, Type.tChar)))
/*  96 */       this.type = Type.tChar;
/*  97 */     else if (((i & 0x8) != 0) && (this.left.fitsType(paramEnvironment, paramContext, Type.tShort)) && (this.right.fitsType(paramEnvironment, paramContext, Type.tShort)))
/*  98 */       this.type = Type.tShort;
/*  99 */     else if (((i & 0x2) != 0) && (this.left.fitsType(paramEnvironment, paramContext, Type.tByte)) && (this.right.fitsType(paramEnvironment, paramContext, Type.tByte)))
/* 100 */       this.type = Type.tByte;
/*     */     else {
/* 102 */       this.type = Type.tInt;
/*     */     }
/*     */ 
/* 105 */     this.left = convert(paramEnvironment, paramContext, this.type, this.left);
/* 106 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/* 107 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 111 */     paramVset = this.cond.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 112 */     this.cond = convert(paramEnvironment, paramContext, Type.tBoolean, this.cond);
/* 113 */     return this.left.check(paramEnvironment, paramContext, paramVset.copy(), paramHashtable).join(this.right.check(paramEnvironment, paramContext, paramVset, paramHashtable));
/*     */   }
/*     */ 
/*     */   public boolean isConstant()
/*     */   {
/* 120 */     return (this.cond.isConstant()) && (this.left.isConstant()) && (this.right.isConstant());
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/* 127 */     if (this.cond.equals(true)) {
/* 128 */       return this.left;
/*     */     }
/* 130 */     if (this.cond.equals(false)) {
/* 131 */       return this.right;
/*     */     }
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 140 */     this.left = this.left.inline(paramEnvironment, paramContext);
/* 141 */     this.right = this.right.inline(paramEnvironment, paramContext);
/* 142 */     if ((this.left == null) && (this.right == null)) {
/* 143 */       return this.cond.inline(paramEnvironment, paramContext);
/*     */     }
/* 145 */     if (this.left == null) {
/* 146 */       this.left = this.right;
/* 147 */       this.right = null;
/* 148 */       this.cond = new NotExpression(this.where, this.cond);
/*     */     }
/* 150 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/* 151 */     return simplify();
/*     */   }
/*     */ 
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 155 */     this.cond = this.cond.inlineValue(paramEnvironment, paramContext);
/* 156 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 157 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 158 */     return simplify();
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 172 */     return 1 + this.cond
/* 170 */       .costInline(paramInt, paramEnvironment, paramContext) + 
/* 170 */       this.left
/* 171 */       .costInline(paramInt, paramEnvironment, paramContext) + (
/* 171 */       this.right == null ? 0 : this.right
/* 172 */       .costInline(paramInt, paramEnvironment, paramContext));
/*     */   }
/*     */ 
/*     */   public Expression copyInline(Context paramContext)
/*     */   {
/* 179 */     ConditionalExpression localConditionalExpression = (ConditionalExpression)clone();
/* 180 */     localConditionalExpression.cond = this.cond.copyInline(paramContext);
/* 181 */     localConditionalExpression.left = this.left.copyInline(paramContext);
/*     */ 
/* 185 */     localConditionalExpression.right = (this.right == null ? null : this.right.copyInline(paramContext));
/*     */ 
/* 187 */     return localConditionalExpression;
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 194 */     Label localLabel1 = new Label();
/* 195 */     Label localLabel2 = new Label();
/*     */ 
/* 197 */     this.cond.codeBranch(paramEnvironment, paramContext, paramAssembler, localLabel1, false);
/* 198 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 199 */     paramAssembler.add(this.where, 167, localLabel2);
/* 200 */     paramAssembler.add(localLabel1);
/* 201 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 202 */     paramAssembler.add(localLabel2);
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 205 */     Label localLabel1 = new Label();
/* 206 */     this.cond.codeBranch(paramEnvironment, paramContext, paramAssembler, localLabel1, false);
/* 207 */     this.left.code(paramEnvironment, paramContext, paramAssembler);
/* 208 */     if (this.right != null) {
/* 209 */       Label localLabel2 = new Label();
/* 210 */       paramAssembler.add(this.where, 167, localLabel2);
/* 211 */       paramAssembler.add(localLabel1);
/* 212 */       this.right.code(paramEnvironment, paramContext, paramAssembler);
/* 213 */       paramAssembler.add(localLabel2);
/*     */     } else {
/* 215 */       paramAssembler.add(localLabel1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 223 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 224 */     this.cond.print(paramPrintStream);
/* 225 */     paramPrintStream.print(" ");
/* 226 */     this.left.print(paramPrintStream);
/* 227 */     paramPrintStream.print(" ");
/* 228 */     if (this.right != null)
/* 229 */       this.right.print(paramPrintStream);
/*     */     else {
/* 231 */       paramPrintStream.print("<null>");
/*     */     }
/* 233 */     paramPrintStream.print(")");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ConditionalExpression
 * JD-Core Version:    0.6.2
 */