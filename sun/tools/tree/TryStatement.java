/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.CatchData;
/*     */ import sun.tools.asm.TryData;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class TryStatement extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Statement[] args;
/*     */   long arrayCloneWhere;
/*     */ 
/*     */   public TryStatement(long paramLong, Statement paramStatement, Statement[] paramArrayOfStatement)
/*     */   {
/*  52 */     super(101, paramLong);
/*  53 */     this.body = paramStatement;
/*  54 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  61 */     checkLabel(paramEnvironment, paramContext);
/*     */     try {
/*  63 */       paramVset = reach(paramEnvironment, paramVset);
/*  64 */       Hashtable localHashtable = new Hashtable();
/*  65 */       CheckContext localCheckContext = new CheckContext(paramContext, this);
/*     */ 
/*  69 */       Vset localVset1 = this.body.check(paramEnvironment, localCheckContext, paramVset.copy(), localHashtable);
/*     */ 
/*  77 */       Vset localVset2 = Vset.firstDAandSecondDU(paramVset, localVset1.copy().join(localCheckContext.vsTryExit));
/*     */ 
/*  79 */       for (int i = 0; i < this.args.length; i++)
/*     */       {
/*  82 */         localVset1 = localVset1.join(this.args[i].check(paramEnvironment, localCheckContext, localVset2.copy(), paramHashtable));
/*     */       }
/*     */       Object localObject2;
/*     */       Object localObject4;
/*     */       Object localObject5;
/*  86 */       for (i = 1; i < this.args.length; i++) {
/*  87 */         localObject1 = (CatchStatement)this.args[i];
/*  88 */         if (((CatchStatement)localObject1).field != null)
/*     */         {
/*  91 */           Type localType1 = ((CatchStatement)localObject1).field.getType();
/*  92 */           localObject2 = paramEnvironment.getClassDefinition(localType1);
/*     */ 
/*  94 */           for (int k = 0; k < i; k++) {
/*  95 */             localObject4 = (CatchStatement)this.args[k];
/*  96 */             if (((CatchStatement)localObject4).field != null)
/*     */             {
/*  99 */               Type localType2 = ((CatchStatement)localObject4).field.getType();
/* 100 */               localObject5 = paramEnvironment.getClassDeclaration(localType2);
/* 101 */               if (((ClassDefinition)localObject2).subClassOf(paramEnvironment, (ClassDeclaration)localObject5)) {
/* 102 */                 paramEnvironment.error(this.args[i].where, "catch.not.reached");
/* 103 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 108 */       ClassDeclaration localClassDeclaration = paramEnvironment.getClassDeclaration(idJavaLangError);
/* 109 */       Object localObject1 = paramEnvironment.getClassDeclaration(idJavaLangRuntimeException);
/*     */       Object localObject3;
/*     */       int n;
/*     */       Object localObject6;
/* 112 */       for (int j = 0; j < this.args.length; j++) {
/* 113 */         localObject2 = (CatchStatement)this.args[j];
/* 114 */         if (((CatchStatement)localObject2).field != null)
/*     */         {
/* 117 */           localObject3 = ((CatchStatement)localObject2).field.getType();
/* 118 */           if (((Type)localObject3).isType(10))
/*     */           {
/* 124 */             localObject4 = paramEnvironment.getClassDefinition((Type)localObject3);
/*     */ 
/* 127 */             if ((!((ClassDefinition)localObject4).subClassOf(paramEnvironment, localClassDeclaration)) && (!((ClassDefinition)localObject4).superClassOf(paramEnvironment, localClassDeclaration)) && 
/* 128 */               (!((ClassDefinition)localObject4)
/* 128 */               .subClassOf(paramEnvironment, (ClassDeclaration)localObject1)) && 
/* 128 */               (!((ClassDefinition)localObject4).superClassOf(paramEnvironment, (ClassDeclaration)localObject1)))
/*     */             {
/* 133 */               n = 0;
/* 134 */               for (localObject5 = localHashtable.keys(); ((Enumeration)localObject5).hasMoreElements(); ) {
/* 135 */                 localObject6 = (ClassDeclaration)((Enumeration)localObject5).nextElement();
/* 136 */                 if ((((ClassDefinition)localObject4).superClassOf(paramEnvironment, (ClassDeclaration)localObject6)) || (((ClassDefinition)localObject4).subClassOf(paramEnvironment, (ClassDeclaration)localObject6))) {
/* 137 */                   n = 1;
/* 138 */                   break;
/*     */                 }
/*     */               }
/* 141 */               if ((n == 0) && (this.arrayCloneWhere != 0L) && 
/* 142 */                 (((ClassDefinition)localObject4)
/* 142 */                 .getName().toString().equals("java.lang.CloneNotSupportedException"))) {
/* 143 */                 paramEnvironment.error(this.arrayCloneWhere, "warn.array.clone.supported", ((ClassDefinition)localObject4).getName());
/*     */               }
/*     */ 
/* 146 */               if (n == 0)
/* 147 */                 paramEnvironment.error(((CatchStatement)localObject2).where, "catch.not.thrown", ((ClassDefinition)localObject4).getName());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 152 */       for (Enumeration localEnumeration = localHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 153 */         localObject2 = (ClassDeclaration)localEnumeration.nextElement();
/* 154 */         localObject3 = ((ClassDeclaration)localObject2).getClassDefinition(paramEnvironment);
/* 155 */         int m = 1;
/* 156 */         for (n = 0; n < this.args.length; n++) {
/* 157 */           localObject5 = (CatchStatement)this.args[n];
/* 158 */           if (((CatchStatement)localObject5).field != null)
/*     */           {
/* 161 */             localObject6 = ((CatchStatement)localObject5).field.getType();
/* 162 */             if (!((Type)localObject6).isType(13))
/*     */             {
/* 164 */               if (((ClassDefinition)localObject3).subClassOf(paramEnvironment, paramEnvironment.getClassDeclaration((Type)localObject6))) {
/* 165 */                 m = 0;
/* 166 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 169 */         if (m != 0) {
/* 170 */           paramHashtable.put(localObject2, localHashtable.get(localObject2));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 181 */       return paramContext.removeAdditionalVars(localVset1.join(localCheckContext.vsBreak));
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 183 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, opNames[this.op]);
/* 184 */     }return paramVset;
/*     */   }
/*     */ 
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 192 */     if (this.body != null) {
/* 193 */       this.body = this.body.inline(paramEnvironment, new Context(paramContext, this));
/*     */     }
/* 195 */     if (this.body == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     for (int i = 0; i < this.args.length; i++) {
/* 199 */       if (this.args[i] != null) {
/* 200 */         this.args[i] = this.args[i].inline(paramEnvironment, new Context(paramContext, this));
/*     */       }
/*     */     }
/* 203 */     return this.args.length == 0 ? eliminate(paramEnvironment, this.body) : this;
/*     */   }
/*     */ 
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean)
/*     */   {
/* 210 */     TryStatement localTryStatement = (TryStatement)clone();
/* 211 */     if (this.body != null) {
/* 212 */       localTryStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 214 */     localTryStatement.args = new Statement[this.args.length];
/* 215 */     for (int i = 0; i < this.args.length; i++) {
/* 216 */       if (this.args[i] != null) {
/* 217 */         localTryStatement.args[i] = this.args[i].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     }
/* 220 */     return localTryStatement;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/* 265 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 272 */     CodeContext localCodeContext = new CodeContext(paramContext, this);
/*     */ 
/* 274 */     TryData localTryData = new TryData();
/*     */     Object localObject;
/* 275 */     for (int i = 0; i < this.args.length; i++) {
/* 276 */       localObject = ((CatchStatement)this.args[i]).field.getType();
/* 277 */       if (((Type)localObject).isType(10))
/* 278 */         localTryData.add(paramEnvironment.getClassDeclaration((Type)localObject));
/*     */       else {
/* 280 */         localTryData.add(localObject);
/*     */       }
/*     */     }
/* 283 */     paramAssembler.add(this.where, -3, localTryData);
/* 284 */     if (this.body != null) {
/* 285 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*     */     }
/*     */ 
/* 288 */     paramAssembler.add(localTryData.getEndLabel());
/* 289 */     paramAssembler.add(this.where, 167, localCodeContext.breakLabel);
/*     */ 
/* 291 */     for (i = 0; i < this.args.length; i++) {
/* 292 */       localObject = localTryData.getCatch(i);
/* 293 */       paramAssembler.add(((CatchData)localObject).getLabel());
/* 294 */       this.args[i].code(paramEnvironment, localCodeContext, paramAssembler);
/* 295 */       paramAssembler.add(this.where, 167, localCodeContext.breakLabel);
/*     */     }
/*     */ 
/* 298 */     paramAssembler.add(localCodeContext.breakLabel);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream, int paramInt)
/*     */   {
/* 305 */     super.print(paramPrintStream, paramInt);
/* 306 */     paramPrintStream.print("try ");
/* 307 */     if (this.body != null)
/* 308 */       this.body.print(paramPrintStream, paramInt);
/*     */     else {
/* 310 */       paramPrintStream.print("<empty>");
/*     */     }
/* 312 */     for (int i = 0; i < this.args.length; i++) {
/* 313 */       paramPrintStream.print(" ");
/* 314 */       this.args[i].print(paramPrintStream, paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.TryStatement
 * JD-Core Version:    0.6.2
 */