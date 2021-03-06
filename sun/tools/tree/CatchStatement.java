/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.LocalVariable;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class CatchStatement extends Statement
/*     */ {
/*     */   int mod;
/*     */   Expression texpr;
/*     */   Identifier id;
/*     */   Statement body;
/*     */   LocalMember field;
/*     */ 
/*     */   public CatchStatement(long paramLong, Expression paramExpression, IdentifierToken paramIdentifierToken, Statement paramStatement)
/*     */   {
/*  52 */     super(102, paramLong);
/*  53 */     this.mod = paramIdentifierToken.getModifiers();
/*  54 */     this.texpr = paramExpression;
/*  55 */     this.id = paramIdentifierToken.getName();
/*  56 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CatchStatement(long paramLong, Expression paramExpression, Identifier paramIdentifier, Statement paramStatement) {
/*  61 */     super(102, paramLong);
/*  62 */     this.texpr = paramExpression;
/*  63 */     this.id = paramIdentifier;
/*  64 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  71 */     paramVset = reach(paramEnvironment, paramVset);
/*  72 */     paramContext = new Context(paramContext, this);
/*  73 */     Type localType = this.texpr.toType(paramEnvironment, paramContext);
/*     */     try
/*     */     {
/*  76 */       if (paramContext.getLocalField(this.id) != null) {
/*  77 */         paramEnvironment.error(this.where, "local.redefined", this.id);
/*     */       }
/*     */ 
/*  80 */       if (!localType.isType(13))
/*     */       {
/*  82 */         if (!localType.isType(10)) {
/*  83 */           paramEnvironment.error(this.where, "catch.not.throwable", localType);
/*     */         } else {
/*  85 */           ClassDefinition localClassDefinition = paramEnvironment.getClassDefinition(localType);
/*  86 */           if (!localClassDefinition.subClassOf(paramEnvironment, paramEnvironment
/*  87 */             .getClassDeclaration(idJavaLangThrowable)))
/*     */           {
/*  88 */             paramEnvironment.error(this.where, "catch.not.throwable", localClassDefinition);
/*     */           }
/*     */         }
/*     */       }
/*  92 */       this.field = new LocalMember(this.where, paramContext.field.getClassDefinition(), this.mod, localType, this.id);
/*  93 */       paramContext.declare(paramEnvironment, this.field);
/*  94 */       paramVset.addVar(this.field.number);
/*     */ 
/*  96 */       return this.body.check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     } catch (ClassNotFound localClassNotFound) {
/*  98 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, opNames[this.op]);
/*  99 */     }return paramVset;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 107 */     paramContext = new Context(paramContext, this);
/* 108 */     if (this.field.isUsed()) {
/* 109 */       paramContext.declare(paramEnvironment, this.field);
/*     */     }
/* 111 */     if (this.body != null) {
/* 112 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 121 */     CatchStatement localCatchStatement = (CatchStatement)clone();
/* 122 */     if (this.body != null) {
/* 123 */       localCatchStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 125 */     if (this.field != null) {
/* 126 */       localCatchStatement.field = this.field.copyInline(paramContext);
/*     */     }
/* 128 */     return localCatchStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 135 */     int i = 1;
/* 136 */     if (this.body != null) {
/* 137 */       i += this.body.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 139 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 146 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/* 147 */     if (this.field.isUsed()) {
/* 148 */       localCodeContext.declare(paramEnvironment, this.field);
/* 149 */       paramAssembler.add(this.where, 58, new LocalVariable(this.field, this.field.number));
/*     */     } else {
/* 151 */       paramAssembler.add(this.where, 87);
/*     */     }
/* 153 */     if (this.body != null)
/* 154 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 163 */     super.print(paramPrintStream, paramInt);
/* 164 */     paramPrintStream.print("catch (");
/* 165 */     this.texpr.print(paramPrintStream);
/* 166 */     paramPrintStream.print(" " + this.id + ") ");
/* 167 */     if (this.body != null)
/* 168 */       this.body.print(paramPrintStream, paramInt);
/*     */     else
/* 170 */       paramPrintStream.print("<empty>");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.CatchStatement
 * JD-Core Version:    0.6.2
 */