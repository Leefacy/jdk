/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class ConvertExpression extends UnaryExpression
/*     */ {
/*     */   public ConvertExpression(long paramLong, Type paramType, Expression paramExpression)
/*     */   {
/*  44 */     super(55, paramLong, paramType, paramExpression);
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  51 */     return this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */   Expression simplify()
/*     */   {
/*  58 */     switch (this.right.op) {
/*     */     case 62:
/*     */     case 63:
/*     */     case 64:
/*     */     case 65:
/*  63 */       int i = ((IntegerExpression)this.right).value;
/*  64 */       switch (this.type.getTypeCode()) { case 1:
/*  65 */         return new ByteExpression(this.right.where, (byte)i);
/*     */       case 2:
/*  66 */         return new CharExpression(this.right.where, (char)i);
/*     */       case 3:
/*  67 */         return new ShortExpression(this.right.where, (short)i);
/*     */       case 4:
/*  68 */         return new IntExpression(this.right.where, i);
/*     */       case 5:
/*  69 */         return new LongExpression(this.right.where, i);
/*     */       case 6:
/*  70 */         return new FloatExpression(this.right.where, i);
/*     */       case 7:
/*  71 */         return new DoubleExpression(this.right.where, i);
/*     */       }
/*  73 */       break;
/*     */     case 66:
/*  76 */       long l = ((LongExpression)this.right).value;
/*  77 */       switch (this.type.getTypeCode()) { case 1:
/*  78 */         return new ByteExpression(this.right.where, (byte)(int)l);
/*     */       case 2:
/*  79 */         return new CharExpression(this.right.where, (char)(int)l);
/*     */       case 3:
/*  80 */         return new ShortExpression(this.right.where, (short)(int)l);
/*     */       case 4:
/*  81 */         return new IntExpression(this.right.where, (int)l);
/*     */       case 6:
/*  82 */         return new FloatExpression(this.right.where, (float)l);
/*     */       case 7:
/*  83 */         return new DoubleExpression(this.right.where, l);
/*     */       case 5: }
/*  85 */       break;
/*     */     case 67:
/*  88 */       float f = ((FloatExpression)this.right).value;
/*  89 */       switch (this.type.getTypeCode()) { case 1:
/*  90 */         return new ByteExpression(this.right.where, (byte)(int)f);
/*     */       case 2:
/*  91 */         return new CharExpression(this.right.where, (char)(int)f);
/*     */       case 3:
/*  92 */         return new ShortExpression(this.right.where, (short)(int)f);
/*     */       case 4:
/*  93 */         return new IntExpression(this.right.where, (int)f);
/*     */       case 5:
/*  94 */         return new LongExpression(this.right.where, ()f);
/*     */       case 7:
/*  95 */         return new DoubleExpression(this.right.where, f);
/*     */       case 6: }
/*  97 */       break;
/*     */     case 68:
/* 100 */       double d = ((DoubleExpression)this.right).value;
/* 101 */       switch (this.type.getTypeCode()) { case 1:
/* 102 */         return new ByteExpression(this.right.where, (byte)(int)d);
/*     */       case 2:
/* 103 */         return new CharExpression(this.right.where, (char)(int)d);
/*     */       case 3:
/* 104 */         return new ShortExpression(this.right.where, (short)(int)d);
/*     */       case 4:
/* 105 */         return new IntExpression(this.right.where, (int)d);
/*     */       case 5:
/* 106 */         return new LongExpression(this.right.where, ()d);
/*     */       case 6:
/* 107 */         return new FloatExpression(this.right.where, (float)d);
/*     */       }
/* 109 */       break;
/*     */     }
/*     */ 
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean equals(int paramInt)
/*     */   {
/* 119 */     return this.right.equals(paramInt);
/*     */   }
/*     */   public boolean equals(boolean paramBoolean) {
/* 122 */     return this.right.equals(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 131 */     if ((this.right.type.inMask(1792)) && (this.type.inMask(1792))) {
/*     */       try {
/* 133 */         if (!paramEnvironment.implicitCast(this.right.type, this.type))
/* 134 */           return inlineValue(paramEnvironment, paramContext);
/*     */       } catch (ClassNotFound localClassNotFound) {
/* 136 */         throw new CompilerError(localClassNotFound);
/*     */       }
/*     */     }
/* 139 */     return super.inline(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 146 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 147 */     codeConversion(paramEnvironment, paramContext, paramAssembler, this.right.type, this.type);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 154 */     paramPrintStream.print("(" + opNames[this.op] + " " + this.type.toString() + " ");
/* 155 */     this.right.print(paramPrintStream);
/* 156 */     paramPrintStream.print(")");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ConvertExpression
 * JD-Core Version:    0.6.2
 */