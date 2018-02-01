/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class ArrayExpression extends NaryExpression
/*     */ {
/*     */   public ArrayExpression(long paramLong, Expression[] paramArrayOfExpression)
/*     */   {
/*  44 */     super(57, paramLong, Type.tError, null, paramArrayOfExpression);
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  51 */     paramEnvironment.error(this.where, "invalid.array.expr");
/*  52 */     return paramVset;
/*     */   }
/*     */   public Vset checkInitializer(Environment paramEnvironment, Context paramContext, Vset paramVset, Type paramType, Hashtable paramHashtable) {
/*  55 */     if (!paramType.isType(9)) {
/*  56 */       if (!paramType.isType(13)) {
/*  57 */         paramEnvironment.error(this.where, "invalid.array.init", paramType);
/*     */       }
/*  59 */       return paramVset;
/*     */     }
/*  61 */     this.type = paramType;
/*  62 */     paramType = paramType.getElementType();
/*  63 */     for (int i = 0; i < this.args.length; i++) {
/*  64 */       paramVset = this.args[i].checkInitializer(paramEnvironment, paramContext, paramVset, paramType, paramHashtable);
/*  65 */       this.args[i] = convert(paramEnvironment, paramContext, paramType, this.args[i]);
/*     */     }
/*  67 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  74 */     Object localObject = null;
/*  75 */     for (int i = 0; i < this.args.length; i++) {
/*  76 */       this.args[i] = this.args[i].inline(paramEnvironment, paramContext);
/*  77 */       if (this.args[i] != null) {
/*  78 */         localObject = localObject == null ? this.args[i] : new CommaExpression(this.where, (Expression)localObject, this.args[i]);
/*     */       }
/*     */     }
/*  81 */     return localObject;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  84 */     for (int i = 0; i < this.args.length; i++) {
/*  85 */       this.args[i] = this.args[i].inlineValue(paramEnvironment, paramContext);
/*     */     }
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/*  94 */     int i = 0;
/*  95 */     paramAssembler.add(this.where, 18, new Integer(this.args.length));
/*  96 */     switch (this.type.getElementType().getTypeCode()) { case 0:
/*  97 */       paramAssembler.add(this.where, 188, new Integer(4)); break;
/*     */     case 1:
/*  98 */       paramAssembler.add(this.where, 188, new Integer(8)); break;
/*     */     case 3:
/*  99 */       paramAssembler.add(this.where, 188, new Integer(9)); break;
/*     */     case 2:
/* 100 */       paramAssembler.add(this.where, 188, new Integer(5)); break;
/*     */     case 4:
/* 101 */       paramAssembler.add(this.where, 188, new Integer(10)); break;
/*     */     case 5:
/* 102 */       paramAssembler.add(this.where, 188, new Integer(11)); break;
/*     */     case 6:
/* 103 */       paramAssembler.add(this.where, 188, new Integer(6)); break;
/*     */     case 7:
/* 104 */       paramAssembler.add(this.where, 188, new Integer(7)); break;
/*     */     case 9:
/* 107 */       paramAssembler.add(this.where, 189, this.type.getElementType());
/* 108 */       break;
/*     */     case 10:
/* 111 */       paramAssembler.add(this.where, 189, paramEnvironment.getClassDeclaration(this.type.getElementType()));
/* 112 */       break;
/*     */     case 8:
/*     */     default:
/* 115 */       throw new CompilerError("codeValue");
/*     */     }
/*     */ 
/* 118 */     for (int j = 0; j < this.args.length; j++)
/*     */     {
/* 122 */       if (!this.args[j].equalsDefault())
/*     */       {
/* 124 */         paramAssembler.add(this.where, 89);
/* 125 */         paramAssembler.add(this.where, 18, new Integer(j));
/* 126 */         this.args[j].codeValue(paramEnvironment, paramContext, paramAssembler);
/* 127 */         switch (this.type.getElementType().getTypeCode()) {
/*     */         case 0:
/*     */         case 1:
/* 130 */           paramAssembler.add(this.where, 84);
/* 131 */           break;
/*     */         case 2:
/* 133 */           paramAssembler.add(this.where, 85);
/* 134 */           break;
/*     */         case 3:
/* 136 */           paramAssembler.add(this.where, 86);
/* 137 */           break;
/*     */         default:
/* 139 */           paramAssembler.add(this.where, 79 + this.type.getElementType().getTypeCodeOffset());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ArrayExpression
 * JD-Core Version:    0.6.2
 */