/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.asm.SwitchData;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class SwitchStatement extends Statement
/*     */ {
/*     */   Expression expr;
/*     */   Statement[] args;
/*     */ 
/*     */   public SwitchStatement(long paramLong, Expression paramExpression, Statement[] paramArrayOfStatement)
/*     */   {
/*  49 */     super(95, paramLong);
/*  50 */     this.expr = paramExpression;
/*  51 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  58 */     checkLabel(paramEnvironment, paramContext);
/*  59 */     CheckContext localCheckContext = new CheckContext(paramContext, this);
/*  60 */     paramVset = this.expr.checkValue(paramEnvironment, localCheckContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  61 */     Type localType = this.expr.type;
/*     */ 
/*  63 */     this.expr = convert(paramEnvironment, localCheckContext, Type.tInt, this.expr);
/*     */ 
/*  65 */     Hashtable localHashtable = new Hashtable();
/*  66 */     int i = 0;
/*     */ 
/*  69 */     Vset localVset = DEAD_END;
/*     */ 
/*  71 */     for (int j = 0; j < this.args.length; j++) {
/*  72 */       Statement localStatement = this.args[j];
/*     */ 
/*  74 */       if (localStatement.op == 96)
/*     */       {
/*  76 */         localVset = localStatement.check(paramEnvironment, localCheckContext, localVset.join(paramVset.copy()), paramHashtable);
/*     */ 
/*  78 */         Expression localExpression = ((CaseStatement)localStatement).expr;
/*  79 */         if (localExpression != null) {
/*  80 */           if ((localExpression instanceof IntegerExpression))
/*     */           {
/*  82 */             Integer localInteger = (Integer)((IntegerExpression)localExpression)
/*  82 */               .getValue();
/*  83 */             int k = localInteger.intValue();
/*  84 */             if (localHashtable.get(localExpression) != null) {
/*  85 */               paramEnvironment.error(localStatement.where, "duplicate.label", localInteger);
/*     */             } else {
/*  87 */               localHashtable.put(localExpression, localStatement);
/*     */               int m;
/*  89 */               switch (localType.getTypeCode()) {
/*     */               case 1:
/*  91 */                 m = k != (byte)k ? 1 : 0; break;
/*     */               case 3:
/*  93 */                 m = k != (short)k ? 1 : 0; break;
/*     */               case 2:
/*  95 */                 m = k != (char)k ? 1 : 0; break;
/*     */               default:
/*  97 */                 m = 0;
/*     */               }
/*  99 */               if (m != 0) {
/* 100 */                 paramEnvironment.error(localStatement.where, "switch.overflow", localInteger, localType);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/* 120 */           else if ((!localExpression.isConstant()) || 
/* 121 */             (localExpression
/* 121 */             .getType() != Type.tInt)) {
/* 122 */             paramEnvironment.error(localStatement.where, "const.expr.required");
/*     */           }
/*     */         }
/*     */         else {
/* 126 */           if (i != 0) {
/* 127 */             paramEnvironment.error(localStatement.where, "duplicate.default");
/*     */           }
/* 129 */           i = 1;
/*     */         }
/*     */       } else {
/* 132 */         localVset = localStatement.checkBlockStatement(paramEnvironment, localCheckContext, localVset, paramHashtable);
/*     */       }
/*     */     }
/* 135 */     if (!localVset.isDeadEnd()) {
/* 136 */       localCheckContext.vsBreak = localCheckContext.vsBreak.join(localVset);
/*     */     }
/* 138 */     if (i != 0)
/* 139 */       paramVset = localCheckContext.vsBreak;
/* 140 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 147 */     paramContext = new Context(paramContext, this);
/* 148 */     this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/* 149 */     for (int i = 0; i < this.args.length; i++) {
/* 150 */       if (this.args[i] != null) {
/* 151 */         this.args[i] = this.args[i].inline(paramEnvironment, paramContext);
/*     */       }
/*     */     }
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 161 */     SwitchStatement localSwitchStatement = (SwitchStatement)clone();
/* 162 */     localSwitchStatement.expr = this.expr.copyInline(paramContext);
/* 163 */     localSwitchStatement.args = new Statement[this.args.length];
/* 164 */     for (int i = 0; i < this.args.length; i++) {
/* 165 */       if (this.args[i] != null) {
/* 166 */         localSwitchStatement.args[i] = this.args[i].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     }
/* 169 */     return localSwitchStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 176 */     int i = this.expr.costInline(paramInt, paramEnvironment, paramContext);
/* 177 */     for (int j = 0; (j < this.args.length) && (i < paramInt); j++) {
/* 178 */       if (this.args[j] != null) {
/* 179 */         i += this.args[j].costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/*     */     }
/* 182 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 189 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/*     */ 
/* 191 */     this.expr.codeValue(paramEnvironment, localCodeContext, paramAssembler);
/*     */ 
/* 193 */     SwitchData localSwitchData = new SwitchData();
/* 194 */     int i = 0;
/*     */     Statement localStatement;
/*     */     Expression localExpression;
/* 196 */     for (int j = 0; j < this.args.length; j++) {
/* 197 */       localStatement = this.args[j];
/* 198 */       if ((localStatement != null) && (localStatement.op == 96)) {
/* 199 */         localExpression = ((CaseStatement)localStatement).expr;
/* 200 */         if (localExpression != null) {
/* 201 */           localSwitchData.add(((IntegerExpression)localExpression).value, new Label());
/*     */         }
/*     */         else
/*     */         {
/* 205 */           i = 1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 212 */     if (paramEnvironment.coverage()) {
/* 213 */       localSwitchData.initTableCase();
/*     */     }
/* 215 */     paramAssembler.add(this.where, 170, localSwitchData);
/*     */ 
/* 217 */     for (j = 0; j < this.args.length; j++) {
/* 218 */       localStatement = this.args[j];
/* 219 */       if (localStatement != null) {
/* 220 */         if (localStatement.op == 96) {
/* 221 */           localExpression = ((CaseStatement)localStatement).expr;
/* 222 */           if (localExpression != null) {
/* 223 */             paramAssembler.add(localSwitchData.get(((IntegerExpression)localExpression).value));
/*     */ 
/* 225 */             localSwitchData.addTableCase(((IntegerExpression)localExpression).value, localStatement.where);
/*     */           }
/*     */           else {
/* 228 */             paramAssembler.add(localSwitchData.getDefaultLabel());
/*     */ 
/* 230 */             localSwitchData.addTableDefault(localStatement.where);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 235 */           localStatement.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 240 */     if (i == 0) {
/* 241 */       paramAssembler.add(localSwitchData.getDefaultLabel());
/*     */     }
/* 243 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 250 */     super.print(paramPrintStream, paramInt);
/* 251 */     paramPrintStream.print("switch (");
/* 252 */     this.expr.print(paramPrintStream);
/* 253 */     paramPrintStream.print(") {\n");
/* 254 */     for (int i = 0; i < this.args.length; i++) {
/* 255 */       if (this.args[i] != null) {
/* 256 */         printIndent(paramPrintStream, paramInt + 1);
/* 257 */         this.args[i].print(paramPrintStream, paramInt + 1);
/* 258 */         paramPrintStream.print("\n");
/*     */       }
/*     */     }
/* 261 */     printIndent(paramPrintStream, paramInt);
/* 262 */     paramPrintStream.print("}");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.SwitchStatement
 * JD-Core Version:    0.6.2
 */