/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.CatchData;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.asm.TryData;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class SynchronizedStatement extends Statement
/*     */ {
/*     */   Expression expr;
/*     */   Statement body;
/*     */   boolean needReturnSlot;
/*     */ 
/*     */   public SynchronizedStatement(long paramLong, Expression paramExpression, Statement paramStatement)
/*     */   {
/*  51 */     super(126, paramLong);
/*  52 */     this.expr = paramExpression;
/*  53 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  60 */     checkLabel(paramEnvironment, paramContext);
/*  61 */     CheckContext localCheckContext = new CheckContext(paramContext, this);
/*  62 */     paramVset = reach(paramEnvironment, paramVset);
/*  63 */     paramVset = this.expr.checkValue(paramEnvironment, localCheckContext, paramVset, paramHashtable);
/*  64 */     if (this.expr.type.equals(Type.tNull)) {
/*  65 */       paramEnvironment.error(this.expr.where, "synchronized.null");
/*     */     }
/*  67 */     this.expr = convert(paramEnvironment, localCheckContext, Type.tClass(idJavaLangObject), this.expr);
/*  68 */     paramVset = this.body.check(paramEnvironment, localCheckContext, paramVset, paramHashtable);
/*  69 */     return paramContext.removeAdditionalVars(paramVset.join(localCheckContext.vsBreak));
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  76 */     if (this.body != null) {
/*  77 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/*  79 */     this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/*  87 */     SynchronizedStatement localSynchronizedStatement = (SynchronizedStatement)clone();
/*  88 */     localSynchronizedStatement.expr = this.expr.copyInline(paramContext);
/*  89 */     if (this.body != null) {
/*  90 */       localSynchronizedStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/*  92 */     return localSynchronizedStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  99 */     int i = 1;
/* 100 */     if (this.expr != null) {
/* 101 */       i += this.expr.costInline(paramInt, paramEnvironment, paramContext);
/* 102 */       if (i >= paramInt) return i;
/*     */     }
/* 104 */     if (this.body != null) {
/* 105 */       i += this.body.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 114 */     ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/* 115 */     this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 116 */     paramContext = new Context(paramContext);
/*     */ 
/* 118 */     if (this.needReturnSlot) {
/* 119 */       localObject = paramContext.field.getType().getReturnType();
/* 120 */       localLocalMember = new LocalMember(0L, localClassDefinition, 0, (Type)localObject, idFinallyReturnValue);
/*     */ 
/* 122 */       paramContext.declare(paramEnvironment, localLocalMember);
/* 123 */       Environment.debugOutput("Assigning return slot to " + localLocalMember.number);
/*     */     }
/*     */ 
/* 126 */     Object localObject = new LocalMember(this.where, localClassDefinition, 0, Type.tObject, null);
/* 127 */     LocalMember localLocalMember = new LocalMember(this.where, localClassDefinition, 0, Type.tInt, null);
/* 128 */     Integer localInteger1 = new Integer(paramContext.declare(paramEnvironment, (LocalMember)localObject));
/* 129 */     Integer localInteger2 = new Integer(paramContext.declare(paramEnvironment, localLocalMember));
/*     */ 
/* 131 */     Label localLabel = new Label();
/*     */ 
/* 133 */     TryData localTryData = new TryData();
/* 134 */     localTryData.add(null);
/*     */ 
/* 137 */     paramAssembler.add(this.where, 58, localInteger1);
/* 138 */     paramAssembler.add(this.where, 25, localInteger1);
/* 139 */     paramAssembler.add(this.where, 194);
/*     */ 
/* 142 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/* 143 */     paramAssembler.add(this.where, -3, localTryData);
/* 144 */     if (this.body != null)
/* 145 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     else {
/* 147 */       paramAssembler.add(this.where, 0);
/*     */     }
/* 149 */     paramAssembler.add(localCodeContext.breakLabel);
/* 150 */     paramAssembler.add(localTryData.getEndLabel());
/*     */ 
/* 153 */     paramAssembler.add(this.where, 25, localInteger1);
/* 154 */     paramAssembler.add(this.where, 195);
/* 155 */     paramAssembler.add(this.where, 167, localLabel);
/*     */ 
/* 158 */     CatchData localCatchData = localTryData.getCatch(0);
/* 159 */     paramAssembler.add(localCatchData.getLabel());
/* 160 */     paramAssembler.add(this.where, 25, localInteger1);
/* 161 */     paramAssembler.add(this.where, 195);
/* 162 */     paramAssembler.add(this.where, 191);
/*     */ 
/* 165 */     paramAssembler.add(localCodeContext.contLabel);
/* 166 */     paramAssembler.add(this.where, 58, localInteger2);
/* 167 */     paramAssembler.add(this.where, 25, localInteger1);
/* 168 */     paramAssembler.add(this.where, 195);
/* 169 */     paramAssembler.add(this.where, 169, localInteger2);
/*     */ 
/* 171 */     paramAssembler.add(localLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 178 */     super.print(paramPrintStream, paramInt);
/* 179 */     paramPrintStream.print("synchronized ");
/* 180 */     this.expr.print(paramPrintStream);
/* 181 */     paramPrintStream.print(" ");
/* 182 */     if (this.body != null)
/* 183 */       this.body.print(paramPrintStream, paramInt);
/*     */     else
/* 185 */       paramPrintStream.print("{}");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.SynchronizedStatement
 * JD-Core Version:    0.6.2
 */