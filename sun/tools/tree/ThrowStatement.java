/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class ThrowStatement extends Statement
/*     */ {
/*     */   Expression expr;
/*     */ 
/*     */   public ThrowStatement(long paramLong, Expression paramExpression)
/*     */   {
/*  46 */     super(104, paramLong);
/*  47 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  54 */     checkLabel(paramEnvironment, paramContext);
/*     */     try {
/*  56 */       paramVset = reach(paramEnvironment, paramVset);
/*  57 */       this.expr.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  58 */       if (this.expr.type.isType(10)) {
/*  59 */         ClassDeclaration localClassDeclaration1 = paramEnvironment.getClassDeclaration(this.expr.type);
/*  60 */         if (paramHashtable.get(localClassDeclaration1) == null) {
/*  61 */           paramHashtable.put(localClassDeclaration1, this);
/*     */         }
/*  63 */         ClassDefinition localClassDefinition = localClassDeclaration1.getClassDefinition(paramEnvironment);
/*     */ 
/*  65 */         ClassDeclaration localClassDeclaration2 = paramEnvironment
/*  65 */           .getClassDeclaration(idJavaLangThrowable);
/*     */ 
/*  66 */         if (!localClassDefinition.subClassOf(paramEnvironment, localClassDeclaration2)) {
/*  67 */           paramEnvironment.error(this.where, "throw.not.throwable", localClassDefinition);
/*     */         }
/*  69 */         this.expr = convert(paramEnvironment, paramContext, Type.tObject, this.expr);
/*  70 */       } else if (!this.expr.type.isType(13)) {
/*  71 */         paramEnvironment.error(this.expr.where, "throw.not.throwable", this.expr.type);
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound) {
/*  74 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, opNames[this.op]);
/*     */     }
/*  76 */     CheckContext localCheckContext = paramContext.getTryExitContext();
/*  77 */     if (localCheckContext != null) {
/*  78 */       localCheckContext.vsTryExit = localCheckContext.vsTryExit.join(paramVset);
/*     */     }
/*  80 */     return DEAD_END;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  87 */     this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  95 */     ThrowStatement localThrowStatement = (ThrowStatement)clone();
/*  96 */     localThrowStatement.expr = this.expr.copyInline(paramContext);
/*  97 */     return localThrowStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 104 */     return 1 + this.expr.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 111 */     this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 112 */     paramAssembler.add(this.where, 191);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 119 */     super.print(paramPrintStream, paramInt);
/* 120 */     paramPrintStream.print("throw ");
/* 121 */     this.expr.print(paramPrintStream);
/* 122 */     paramPrintStream.print(":");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ThrowStatement
 * JD-Core Version:    0.6.2
 */