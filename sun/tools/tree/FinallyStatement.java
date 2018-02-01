/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
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
/*     */ public class FinallyStatement extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Statement finalbody;
/*     */   boolean finallyCanFinish;
/*     */   boolean needReturnSlot;
/*     */   Statement init;
/*     */   LocalMember tryTemp;
/*     */ 
/*     */   public FinallyStatement(long paramLong, Statement paramStatement1, Statement paramStatement2)
/*     */   {
/*  55 */     super(103, paramLong);
/*  56 */     this.body = paramStatement1;
/*  57 */     this.finalbody = paramStatement2;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  72 */     paramVset = reach(paramEnvironment, paramVset);
/*  73 */     Hashtable localHashtable = new Hashtable();
/*     */ 
/* 168 */     CheckContext localCheckContext1 = new CheckContext(paramContext, this);
/*     */ 
/* 170 */     Vset localVset1 = this.body.check(paramEnvironment, localCheckContext1, paramVset.copy(), localHashtable)
/* 170 */       .join(localCheckContext1.vsBreak);
/*     */ 
/* 172 */     CheckContext localCheckContext2 = new CheckContext(paramContext, this);
/*     */ 
/* 174 */     localCheckContext2.vsContinue = null;
/* 175 */     Vset localVset2 = this.finalbody.check(paramEnvironment, localCheckContext2, paramVset, paramHashtable);
/* 176 */     this.finallyCanFinish = (!localVset2.isDeadEnd());
/* 177 */     localVset2 = localVset2.join(localCheckContext2.vsBreak);
/*     */     Enumeration localEnumeration;
/* 183 */     if (this.finallyCanFinish)
/*     */     {
/* 185 */       for (localEnumeration = localHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 186 */         Object localObject = localEnumeration.nextElement();
/* 187 */         paramHashtable.put(localObject, localHashtable.get(localObject));
/*     */       }
/*     */     }
/* 190 */     return paramContext.removeAdditionalVars(localVset1.addDAandJoinDU(localVset2));
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 197 */     if (this.tryTemp != null) {
/* 198 */       paramContext = new Context(paramContext, this);
/* 199 */       paramContext.declare(paramEnvironment, this.tryTemp);
/*     */     }
/* 201 */     if (this.init != null) {
/* 202 */       this.init = this.init.inline(paramEnvironment, paramContext);
/*     */     }
/* 204 */     if (this.body != null) {
/* 205 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/* 207 */     if (this.finalbody != null) {
/* 208 */       this.finalbody = this.finalbody.inline(paramEnvironment, paramContext);
/*     */     }
/* 210 */     if (this.body == null) {
/* 211 */       return eliminate(paramEnvironment, this.finalbody);
/*     */     }
/* 213 */     if (this.finalbody == null) {
/* 214 */       return eliminate(paramEnvironment, this.body);
/*     */     }
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 223 */     FinallyStatement localFinallyStatement = (FinallyStatement)clone();
/* 224 */     if (this.tryTemp != null) {
/* 225 */       localFinallyStatement.tryTemp = this.tryTemp.copyInline(paramContext);
/*     */     }
/* 227 */     if (this.init != null) {
/* 228 */       localFinallyStatement.init = this.init.copyInline(paramContext, paramBoolean);
/*     */     }
/* 230 */     if (this.body != null) {
/* 231 */       localFinallyStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 233 */     if (this.finalbody != null) {
/* 234 */       localFinallyStatement.finalbody = this.finalbody.copyInline(paramContext, paramBoolean);
/*     */     }
/* 236 */     return localFinallyStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 243 */     int i = 4;
/* 244 */     if (this.init != null) {
/* 245 */       i += this.init.costInline(paramInt, paramEnvironment, paramContext);
/* 246 */       if (i >= paramInt) return i;
/*     */     }
/* 248 */     if (this.body != null) {
/* 249 */       i += this.body.costInline(paramInt, paramEnvironment, paramContext);
/* 250 */       if (i >= paramInt) return i;
/*     */     }
/* 252 */     if (this.finalbody != null) {
/* 253 */       i += this.finalbody.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 255 */     return i;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 262 */     paramContext = new Context(paramContext);
/* 263 */     Integer localInteger1 = null; Integer localInteger2 = null;
/* 264 */     Label localLabel = new Label();
/*     */ 
/* 266 */     if (this.tryTemp != null) {
/* 267 */       paramContext.declare(paramEnvironment, this.tryTemp);
/*     */     }
/* 269 */     if (this.init != null) {
/* 270 */       localObject1 = new CodeContext(paramContext, this);
/* 271 */       this.init.code(paramEnvironment, (Context)localObject1, paramAssembler);
/*     */     }
/*     */ 
/* 274 */     if (this.finallyCanFinish)
/*     */     {
/* 276 */       localObject3 = paramContext.field.getClassDefinition();
/*     */ 
/* 278 */       if (this.needReturnSlot) {
/* 279 */         Type localType = paramContext.field.getType().getReturnType();
/* 280 */         LocalMember localLocalMember = new LocalMember(0L, (ClassDefinition)localObject3, 0, localType, idFinallyReturnValue);
/*     */ 
/* 283 */         paramContext.declare(paramEnvironment, localLocalMember);
/* 284 */         Environment.debugOutput("Assigning return slot to " + localLocalMember.number);
/*     */       }
/*     */ 
/* 288 */       localObject1 = new LocalMember(this.where, (ClassDefinition)localObject3, 0, Type.tObject, null);
/* 289 */       localObject2 = new LocalMember(this.where, (ClassDefinition)localObject3, 0, Type.tInt, null);
/* 290 */       localInteger1 = new Integer(paramContext.declare(paramEnvironment, (LocalMember)localObject1));
/* 291 */       localInteger2 = new Integer(paramContext.declare(paramEnvironment, (LocalMember)localObject2));
/*     */     }
/*     */ 
/* 294 */     Object localObject1 = new TryData();
/* 295 */     ((TryData)localObject1).add(null);
/*     */ 
/* 298 */     Object localObject2 = new CodeContext(paramContext, this);
/* 299 */     paramAssembler.add(this.where, -3, localObject1);
/* 300 */     this.body.code(paramEnvironment, (Context)localObject2, paramAssembler);
/* 301 */     paramAssembler.add(((CodeContext)localObject2).breakLabel);
/* 302 */     paramAssembler.add(((TryData)localObject1).getEndLabel());
/*     */ 
/* 305 */     if (this.finallyCanFinish) {
/* 306 */       paramAssembler.add(this.where, 168, ((CodeContext)localObject2).contLabel);
/* 307 */       paramAssembler.add(this.where, 167, localLabel);
/*     */     }
/*     */     else {
/* 310 */       paramAssembler.add(this.where, 167, ((CodeContext)localObject2).contLabel);
/*     */     }
/*     */ 
/* 314 */     Object localObject3 = ((TryData)localObject1).getCatch(0);
/* 315 */     paramAssembler.add(((CatchData)localObject3).getLabel());
/* 316 */     if (this.finallyCanFinish) {
/* 317 */       paramAssembler.add(this.where, 58, localInteger1);
/* 318 */       paramAssembler.add(this.where, 168, ((CodeContext)localObject2).contLabel);
/* 319 */       paramAssembler.add(this.where, 25, localInteger1);
/* 320 */       paramAssembler.add(this.where, 191);
/*     */     }
/*     */     else {
/* 323 */       paramAssembler.add(this.where, 87);
/*     */     }
/*     */ 
/* 329 */     paramAssembler.add(((CodeContext)localObject2).contLabel);
/* 330 */     ((CodeContext)localObject2).contLabel = null;
/* 331 */     ((CodeContext)localObject2).breakLabel = localLabel;
/* 332 */     if (this.finallyCanFinish) {
/* 333 */       paramAssembler.add(this.where, 58, localInteger2);
/* 334 */       this.finalbody.code(paramEnvironment, (Context)localObject2, paramAssembler);
/* 335 */       paramAssembler.add(this.where, 169, localInteger2);
/*     */     } else {
/* 337 */       this.finalbody.code(paramEnvironment, (Context)localObject2, paramAssembler);
/*     */     }
/* 339 */     paramAssembler.add(localLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 346 */     super.print(paramPrintStream, paramInt);
/* 347 */     paramPrintStream.print("try ");
/* 348 */     if (this.body != null)
/* 349 */       this.body.print(paramPrintStream, paramInt);
/*     */     else {
/* 351 */       paramPrintStream.print("<empty>");
/*     */     }
/* 353 */     paramPrintStream.print(" finally ");
/* 354 */     if (this.finalbody != null)
/* 355 */       this.finalbody.print(paramPrintStream, paramInt);
/*     */     else
/* 357 */       paramPrintStream.print("<empty>");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.FinallyStatement
 * JD-Core Version:    0.6.2
 */