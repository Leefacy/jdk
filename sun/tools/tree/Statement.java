/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class Statement extends Node
/*     */ {
/*  41 */   public static final Vset DEAD_END = Vset.DEAD_END;
/*  42 */   Identifier[] labels = null;
/*     */ 
/*  54 */   public static final Statement empty = new Statement(105, 0L);
/*     */ 
/*  61 */   public static final int MAXINLINECOST = Integer.getInteger("javac.maxinlinecost", 30)
/*  61 */     .intValue();
/*     */ 
/*     */   Statement(int paramInt, long paramLong)
/*     */   {
/*  48 */     super(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   public static Statement insertStatement(Statement paramStatement1, Statement paramStatement2)
/*     */   {
/*  68 */     if (paramStatement2 == null) {
/*  69 */       paramStatement2 = paramStatement1;
/*  70 */     } else if ((paramStatement2 instanceof CompoundStatement))
/*     */     {
/*  72 */       ((CompoundStatement)paramStatement2).insertStatement(paramStatement1);
/*     */     } else {
/*  74 */       Statement[] arrayOfStatement = { paramStatement1, paramStatement2 };
/*  75 */       paramStatement2 = new CompoundStatement(paramStatement1.getWhere(), arrayOfStatement);
/*     */     }
/*  77 */     return paramStatement2;
/*     */   }
/*     */ 
/*     */   public void setLabel(Environment paramEnvironment, Expression paramExpression)
/*     */   {
/*  84 */     if (paramExpression.op == 60) {
/*  85 */       if (this.labels == null) {
/*  86 */         this.labels = new Identifier[1];
/*     */       }
/*     */       else
/*     */       {
/*  90 */         Identifier[] arrayOfIdentifier = new Identifier[this.labels.length + 1];
/*  91 */         System.arraycopy(this.labels, 0, arrayOfIdentifier, 1, this.labels.length);
/*  92 */         this.labels = arrayOfIdentifier;
/*     */       }
/*  94 */       this.labels[0] = ((IdentifierExpression)paramExpression).id;
/*     */     } else {
/*  96 */       paramEnvironment.error(paramExpression.where, "invalid.label");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Vset checkMethod(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 105 */     CheckContext localCheckContext = new CheckContext(paramContext, new Statement(47, 0L));
/* 106 */     paramContext = localCheckContext;
/*     */ 
/* 108 */     paramVset = check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */ 
/* 111 */     if (!paramContext.field.getType().getReturnType().isType(11))
/*     */     {
/* 123 */       if (!paramVset.isDeadEnd()) {
/* 124 */         paramEnvironment.error(paramContext.field.getWhere(), "return.required.at.end", paramContext.field);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 129 */     paramVset = paramVset.join(localCheckContext.vsBreak);
/*     */ 
/* 131 */     return paramVset;
/*     */   }
/*     */   Vset checkDeclaration(Environment paramEnvironment, Context paramContext, Vset paramVset, int paramInt, Type paramType, Hashtable paramHashtable) {
/* 134 */     throw new CompilerError("checkDeclaration");
/*     */   }
/*     */ 
/*     */   protected void checkLabel(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 143 */     if (this.labels != null)
/* 144 */       label122: for (int i = 0; i < this.labels.length; i++)
/*     */       {
/* 146 */         for (int j = i + 1; j < this.labels.length; j++) {
/* 147 */           if (this.labels[i] == this.labels[j]) {
/* 148 */             paramEnvironment.error(this.where, "nested.duplicate.label", this.labels[i]);
/* 149 */             break label122;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 155 */         CheckContext localCheckContext = (CheckContext)paramContext
/* 155 */           .getLabelContext(this.labels[i]);
/*     */ 
/* 157 */         if (localCheckContext != null)
/*     */         {
/* 159 */           if (localCheckContext.frameNumber == paramContext.frameNumber)
/* 160 */             paramEnvironment.error(this.where, "nested.duplicate.label", this.labels[i]);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 168 */     throw new CompilerError("check");
/*     */   }
/*     */ 
/*     */   Vset checkBlockStatement(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 173 */     return check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */   Vset reach(Environment paramEnvironment, Vset paramVset) {
/* 177 */     if (paramVset.isDeadEnd()) {
/* 178 */       paramEnvironment.error(this.where, "stat.not.reached");
/* 179 */       paramVset = paramVset.clearDeadEnd();
/*     */     }
/* 181 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement eliminate(Environment paramEnvironment, Statement paramStatement)
/*     */   {
/* 195 */     if ((paramStatement != null) && (this.labels != null)) {
/* 196 */       Statement[] arrayOfStatement = { paramStatement };
/* 197 */       paramStatement = new CompoundStatement(this.where, arrayOfStatement);
/* 198 */       paramStatement.labels = this.labels;
/*     */     }
/* 200 */     return paramStatement;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 208 */     throw new CompilerError("code");
/*     */   }
/*     */ 
/*     */   void codeFinally(Environment paramEnvironment, Context paramContext1, Assembler paramAssembler, Context paramContext2, Type paramType)
/*     */   {
/* 219 */     Integer localInteger = null;
/* 220 */     int i = 0;
/* 221 */     int j = 0;
/*     */     Object localObject2;
/* 223 */     for (Object localObject1 = paramContext1; (localObject1 != null) && (localObject1 != paramContext2); localObject1 = ((Context)localObject1).prev)
/* 224 */       if (((Context)localObject1).node != null)
/*     */       {
/* 226 */         if (((Context)localObject1).node.op == 126) {
/* 227 */           i = 1;
/* 228 */         } else if ((((Context)localObject1).node.op == 103) && (((CodeContext)localObject1).contLabel != null))
/*     */         {
/* 231 */           i = 1;
/* 232 */           localObject2 = (FinallyStatement)((Context)localObject1).node;
/* 233 */           if (!((FinallyStatement)localObject2).finallyCanFinish) {
/* 234 */             j = 1;
/*     */ 
/* 237 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 241 */     if (i == 0)
/*     */     {
/* 243 */       return;
/*     */     }
/* 245 */     if (paramType != null)
/*     */     {
/* 247 */       localObject1 = paramContext1.field.getClassDefinition();
/* 248 */       if (j == 0)
/*     */       {
/* 251 */         localObject2 = paramContext1.getLocalField(idFinallyReturnValue);
/* 252 */         localInteger = new Integer(((LocalMember)localObject2).number);
/* 253 */         paramAssembler.add(this.where, 54 + paramType.getTypeCodeOffset(), localInteger);
/*     */       }
/*     */       else {
/* 256 */         switch (paramContext1.field.getType().getReturnType().getTypeCode()) {
/*     */         case 11:
/* 258 */           break;
/*     */         case 5:
/*     */         case 7:
/* 260 */           paramAssembler.add(this.where, 88); break;
/*     */         default:
/* 262 */           paramAssembler.add(this.where, 87);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 267 */     for (localObject1 = paramContext1; (localObject1 != null) && (localObject1 != paramContext2); localObject1 = ((Context)localObject1).prev) {
/* 268 */       if (((Context)localObject1).node != null)
/*     */       {
/* 270 */         if (((Context)localObject1).node.op == 126) {
/* 271 */           paramAssembler.add(this.where, 168, ((CodeContext)localObject1).contLabel);
/* 272 */         } else if ((((Context)localObject1).node.op == 103) && (((CodeContext)localObject1).contLabel != null))
/*     */         {
/* 274 */           localObject2 = (FinallyStatement)((Context)localObject1).node;
/* 275 */           Label localLabel = ((CodeContext)localObject1).contLabel;
/* 276 */           if (((FinallyStatement)localObject2).finallyCanFinish) {
/* 277 */             paramAssembler.add(this.where, 168, localLabel);
/*     */           }
/*     */           else {
/* 280 */             paramAssembler.add(this.where, 167, localLabel);
/* 281 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 286 */     if (localInteger != null)
/* 287 */       paramAssembler.add(this.where, 21 + paramType.getTypeCodeOffset(), localInteger);
/*     */   }
/*     */ 
/*     */   public boolean hasLabel(Identifier paramIdentifier)
/*     */   {
/* 295 */     Identifier[] arrayOfIdentifier = this.labels;
/* 296 */     if (arrayOfIdentifier != null) {
/* 297 */       int i = arrayOfIdentifier.length;
/*     */       do { i--; if (i < 0) break; }
/* 298 */       while (!arrayOfIdentifier[i].equals(paramIdentifier));
/* 299 */       return true;
/*     */     }
/*     */ 
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   public Expression firstConstructor()
/*     */   {
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 317 */     return (Statement)clone();
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 321 */     return paramInt;
/*     */   }
/*     */ 
/*     */   void printIndent(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 329 */     for (int i = 0; i < paramInt; i++)
/* 330 */       paramPrintStream.print("    ");
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 334 */     if (this.labels != null) {
/* 335 */       int i = this.labels.length;
/*     */       while (true) { i--; if (i < 0) break;
/* 336 */         paramPrintStream.print(this.labels[i] + ": "); } 
/*     */     }
/*     */   }
/*     */ 
/* 340 */   public void print(PrintStream paramPrintStream) { print(paramPrintStream, 0); }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.Statement
 * JD-Core Version:    0.6.2
 */