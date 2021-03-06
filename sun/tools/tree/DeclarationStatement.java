/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class DeclarationStatement extends Statement
/*     */ {
/*     */   int mod;
/*     */   Expression type;
/*     */   Statement[] args;
/*     */ 
/*     */   public DeclarationStatement(long paramLong, int paramInt, Expression paramExpression, Statement[] paramArrayOfStatement)
/*     */   {
/*  48 */     super(107, paramLong);
/*  49 */     this.mod = paramInt;
/*  50 */     this.type = paramExpression;
/*  51 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  59 */     paramEnvironment.error(this.where, "invalid.decl");
/*  60 */     return checkBlockStatement(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */   Vset checkBlockStatement(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  63 */     if (this.labels != null) {
/*  64 */       paramEnvironment.error(this.where, "declaration.with.label", this.labels[0]);
/*     */     }
/*  66 */     paramVset = reach(paramEnvironment, paramVset);
/*  67 */     Type localType = this.type.toType(paramEnvironment, paramContext);
/*     */ 
/*  69 */     for (int i = 0; i < this.args.length; i++) {
/*  70 */       paramVset = this.args[i].checkDeclaration(paramEnvironment, paramContext, paramVset, this.mod, localType, paramHashtable);
/*     */     }
/*     */ 
/*  73 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  80 */     int i = 0;
/*  81 */     for (int j = 0; j < this.args.length; j++) {
/*  82 */       if ((this.args[j] =  = this.args[j].inline(paramEnvironment, paramContext)) != null) {
/*  83 */         i++;
/*     */       }
/*     */     }
/*  86 */     return i == 0 ? null : this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  93 */     DeclarationStatement localDeclarationStatement = (DeclarationStatement)clone();
/*  94 */     if (this.type != null) {
/*  95 */       localDeclarationStatement.type = this.type.copyInline(paramContext);
/*     */     }
/*  97 */     localDeclarationStatement.args = new Statement[this.args.length];
/*  98 */     for (int i = 0; i < this.args.length; i++) {
/*  99 */       if (this.args[i] != null) {
/* 100 */         localDeclarationStatement.args[i] = this.args[i].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     }
/* 103 */     return localDeclarationStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 110 */     int i = 1;
/* 111 */     for (int j = 0; j < this.args.length; j++) {
/* 112 */       if (this.args[j] != null) {
/* 113 */         i += this.args[j].costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/*     */     }
/* 116 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 124 */     for (int i = 0; i < this.args.length; i++)
/* 125 */       if (this.args[i] != null)
/* 126 */         this.args[i].code(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 135 */     paramPrintStream.print("declare ");
/* 136 */     super.print(paramPrintStream, paramInt);
/* 137 */     this.type.print(paramPrintStream);
/* 138 */     paramPrintStream.print(" ");
/* 139 */     for (int i = 0; i < this.args.length; i++) {
/* 140 */       if (i > 0) {
/* 141 */         paramPrintStream.print(", ");
/*     */       }
/* 143 */       if (this.args[i] != null)
/* 144 */         this.args[i].print(paramPrintStream);
/*     */       else {
/* 146 */         paramPrintStream.print("<empty>");
/*     */       }
/*     */     }
/* 149 */     paramPrintStream.print(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.DeclarationStatement
 * JD-Core Version:    0.6.2
 */