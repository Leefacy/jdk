/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.ArrayData;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class NewArrayExpression extends NaryExpression
/*     */ {
/*     */   Expression init;
/*     */ 
/*     */   public NewArrayExpression(long paramLong, Expression paramExpression, Expression[] paramArrayOfExpression)
/*     */   {
/*  47 */     super(41, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*     */   }
/*     */ 
/*     */   public NewArrayExpression(long paramLong, Expression paramExpression1, Expression[] paramArrayOfExpression, Expression paramExpression2) {
/*  51 */     this(paramLong, paramExpression1, paramArrayOfExpression);
/*  52 */     this.init = paramExpression2;
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  59 */     this.type = this.right.toType(paramEnvironment, paramContext);
/*     */ 
/*  61 */     int i = this.init != null ? 1 : 0;
/*  62 */     for (int j = 0; j < this.args.length; j++) {
/*  63 */       Expression localExpression = this.args[j];
/*  64 */       if (localExpression == null) {
/*  65 */         if ((j == 0) && (i == 0)) {
/*  66 */           paramEnvironment.error(this.where, "array.dim.missing");
/*     */         }
/*  68 */         i = 1;
/*     */       } else {
/*  70 */         if (i != 0) {
/*  71 */           paramEnvironment.error(localExpression.where, "invalid.array.dim");
/*     */         }
/*  73 */         paramVset = localExpression.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  74 */         this.args[j] = convert(paramEnvironment, paramContext, Type.tInt, localExpression);
/*     */       }
/*  76 */       this.type = Type.tArray(this.type);
/*     */     }
/*  78 */     if (this.init != null) {
/*  79 */       paramVset = this.init.checkInitializer(paramEnvironment, paramContext, paramVset, this.type, paramHashtable);
/*  80 */       this.init = convert(paramEnvironment, paramContext, this.type, this.init);
/*     */     }
/*  82 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Expression copyInline(Context paramContext) {
/*  86 */     NewArrayExpression localNewArrayExpression = (NewArrayExpression)super.copyInline(paramContext);
/*  87 */     if (this.init != null) {
/*  88 */       localNewArrayExpression.init = this.init.copyInline(paramContext);
/*     */     }
/*  90 */     return localNewArrayExpression;
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  97 */     Expression localExpression = null;
/*  98 */     for (int i = 0; i < this.args.length; i++) {
/*  99 */       if (this.args[i] != null) {
/* 100 */         localExpression = localExpression != null ? new CommaExpression(this.where, localExpression, this.args[i]) : this.args[i];
/*     */       }
/*     */     }
/* 103 */     if (this.init != null)
/* 104 */       localExpression = localExpression != null ? new CommaExpression(this.where, localExpression, this.init) : this.init;
/* 105 */     return localExpression != null ? localExpression.inline(paramEnvironment, paramContext) : null;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 108 */     if (this.init != null)
/* 109 */       return this.init.inlineValue(paramEnvironment, paramContext);
/* 110 */     for (int i = 0; i < this.args.length; i++) {
/* 111 */       if (this.args[i] != null) {
/* 112 */         this.args[i] = this.args[i].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */     }
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 122 */     int i = 0;
/* 123 */     for (int j = 0; j < this.args.length; j++) {
/* 124 */       if (this.args[j] != null) {
/* 125 */         this.args[j].codeValue(paramEnvironment, paramContext, paramAssembler);
/* 126 */         i++;
/*     */       }
/*     */     }
/* 129 */     if (this.args.length > 1) {
/* 130 */       paramAssembler.add(this.where, 197, new ArrayData(this.type, i));
/* 131 */       return;
/*     */     }
/*     */ 
/* 134 */     switch (this.type.getElementType().getTypeCode()) {
/*     */     case 0:
/* 136 */       paramAssembler.add(this.where, 188, new Integer(4)); break;
/*     */     case 1:
/* 138 */       paramAssembler.add(this.where, 188, new Integer(8)); break;
/*     */     case 3:
/* 140 */       paramAssembler.add(this.where, 188, new Integer(9)); break;
/*     */     case 2:
/* 142 */       paramAssembler.add(this.where, 188, new Integer(5)); break;
/*     */     case 4:
/* 144 */       paramAssembler.add(this.where, 188, new Integer(10)); break;
/*     */     case 5:
/* 146 */       paramAssembler.add(this.where, 188, new Integer(11)); break;
/*     */     case 6:
/* 148 */       paramAssembler.add(this.where, 188, new Integer(6)); break;
/*     */     case 7:
/* 150 */       paramAssembler.add(this.where, 188, new Integer(7)); break;
/*     */     case 9:
/* 152 */       paramAssembler.add(this.where, 189, this.type.getElementType()); break;
/*     */     case 10:
/* 154 */       paramAssembler.add(this.where, 189, paramEnvironment
/* 155 */         .getClassDeclaration(this.type
/* 155 */         .getElementType()));
/* 156 */       break;
/*     */     case 8:
/*     */     default:
/* 158 */       throw new CompilerError("codeValue");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.NewArrayExpression
 * JD-Core Version:    0.6.2
 */