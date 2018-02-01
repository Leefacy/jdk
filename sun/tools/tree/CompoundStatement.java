/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ 
/*     */ public class CompoundStatement extends Statement
/*     */ {
/*     */   Statement[] args;
/*     */ 
/*     */   public CompoundStatement(long paramLong, Statement[] paramArrayOfStatement)
/*     */   {
/*  47 */     super(105, paramLong);
/*  48 */     this.args = paramArrayOfStatement;
/*     */ 
/*  50 */     for (int i = 0; i < paramArrayOfStatement.length; i++)
/*  51 */       if (paramArrayOfStatement[i] == null)
/*  52 */         paramArrayOfStatement[i] = new CompoundStatement(paramLong, new Statement[0]);
/*     */   }
/*     */ 
/*     */   public void insertStatement(Statement paramStatement)
/*     */   {
/*  62 */     Statement[] arrayOfStatement = new Statement[1 + this.args.length];
/*  63 */     arrayOfStatement[0] = paramStatement;
/*  64 */     for (int i = 0; i < this.args.length; i++) {
/*  65 */       arrayOfStatement[(i + 1)] = this.args[i];
/*     */     }
/*  67 */     this.args = arrayOfStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  74 */     checkLabel(paramEnvironment, paramContext);
/*  75 */     if (this.args.length > 0) {
/*  76 */       paramVset = reach(paramEnvironment, paramVset);
/*  77 */       CheckContext localCheckContext = new CheckContext(paramContext, this);
/*     */ 
/*  79 */       Environment localEnvironment = Context.newEnvironment(paramEnvironment, localCheckContext);
/*  80 */       for (int i = 0; i < this.args.length; i++) {
/*  81 */         paramVset = this.args[i].checkBlockStatement(localEnvironment, localCheckContext, paramVset, paramHashtable);
/*     */       }
/*  83 */       paramVset = paramVset.join(localCheckContext.vsBreak);
/*     */     }
/*  85 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  92 */     paramContext = new Context(paramContext, this);
/*  93 */     int i = 0;
/*  94 */     int j = 0;
/*  95 */     for (int k = 0; k < this.args.length; k++) {
/*  96 */       Statement localStatement1 = this.args[k];
/*  97 */       if (localStatement1 != null) {
/*  98 */         if ((localStatement1 = localStatement1.inline(paramEnvironment, paramContext)) != null) {
/*  99 */           if ((localStatement1.op == 105) && (localStatement1.labels == null))
/* 100 */             j += ((CompoundStatement)localStatement1).args.length;
/*     */           else {
/* 102 */             j++;
/*     */           }
/* 104 */           i = 1;
/*     */         }
/* 106 */         this.args[k] = localStatement1;
/*     */       }
/*     */     }
/* 109 */     switch (j) {
/*     */     case 0:
/* 111 */       return null;
/*     */     case 1:
/* 114 */       for (k = this.args.length; k-- > 0; ) {
/* 115 */         if (this.args[k] != null) {
/* 116 */           return eliminate(paramEnvironment, this.args[k]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 121 */     if ((i != 0) || (j != this.args.length)) {
/* 122 */       Statement[] arrayOfStatement1 = new Statement[j];
/* 123 */       for (int m = this.args.length; m-- > 0; ) {
/* 124 */         Statement localStatement2 = this.args[m];
/* 125 */         if (localStatement2 != null)
/*     */         {
/*     */           Statement[] arrayOfStatement2;
/*     */           int n;
/* 126 */           if ((localStatement2.op == 105) && (localStatement2.labels == null)) {
/* 127 */             arrayOfStatement2 = ((CompoundStatement)localStatement2).args;
/* 128 */             for (n = arrayOfStatement2.length; n-- > 0; )
/* 129 */               arrayOfStatement1[(--j)] = arrayOfStatement2[n];
/*     */           }
/*     */           else {
/* 132 */             arrayOfStatement1[(--j)] = localStatement2;
/*     */           }
/*     */         }
/*     */       }
/* 136 */       this.args = arrayOfStatement1;
/*     */     }
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 145 */     CompoundStatement localCompoundStatement = (CompoundStatement)clone();
/* 146 */     localCompoundStatement.args = new Statement[this.args.length];
/* 147 */     for (int i = 0; i < this.args.length; i++) {
/* 148 */       localCompoundStatement.args[i] = this.args[i].copyInline(paramContext, paramBoolean);
/*     */     }
/* 150 */     return localCompoundStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 157 */     int i = 0;
/* 158 */     for (int j = 0; (j < this.args.length) && (i < paramInt); j++) {
/* 159 */       i += this.args[j].costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 161 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 168 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/* 169 */     for (int i = 0; i < this.args.length; i++) {
/* 170 */       this.args[i].code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/* 172 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public Expression firstConstructor()
/*     */   {
/* 179 */     return this.args.length > 0 ? this.args[0].firstConstructor() : null;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 186 */     super.print(paramPrintStream, paramInt);
/* 187 */     paramPrintStream.print("{\n");
/* 188 */     for (int i = 0; i < this.args.length; i++) {
/* 189 */       printIndent(paramPrintStream, paramInt + 1);
/* 190 */       if (this.args[i] != null)
/* 191 */         this.args[i].print(paramPrintStream, paramInt + 1);
/*     */       else {
/* 193 */         paramPrintStream.print("<empty>");
/*     */       }
/* 195 */       paramPrintStream.print("\n");
/*     */     }
/* 197 */     printIndent(paramPrintStream, paramInt);
/* 198 */     paramPrintStream.print("}");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.CompoundStatement
 * JD-Core Version:    0.6.2
 */