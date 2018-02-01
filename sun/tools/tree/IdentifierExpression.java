/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.LocalVariable;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class IdentifierExpression extends Expression
/*     */ {
/*     */   Identifier id;
/*     */   MemberDefinition field;
/*     */   Expression implementation;
/*     */ 
/*     */   public IdentifierExpression(long paramLong, Identifier paramIdentifier)
/*     */   {
/*  49 */     super(60, paramLong, Type.tError);
/*  50 */     this.id = paramIdentifier;
/*     */   }
/*     */   public IdentifierExpression(IdentifierToken paramIdentifierToken) {
/*  53 */     this(paramIdentifierToken.getWhere(), paramIdentifierToken.getName());
/*     */   }
/*     */   public IdentifierExpression(long paramLong, MemberDefinition paramMemberDefinition) {
/*  56 */     super(60, paramLong, paramMemberDefinition.getType());
/*  57 */     this.id = paramMemberDefinition.getName();
/*  58 */     this.field = paramMemberDefinition;
/*     */   }
/*     */ 
/*     */   public Expression getImplementation() {
/*  62 */     if (this.implementation != null)
/*  63 */       return this.implementation;
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean equals(Identifier paramIdentifier)
/*     */   {
/*  71 */     return this.id.equals(paramIdentifier);
/*     */   }
/*     */ 
/*     */   private Vset assign(Environment paramEnvironment, Context paramContext, Vset paramVset)
/*     */   {
/*  79 */     if (this.field.isLocal()) {
/*  80 */       LocalMember localLocalMember = (LocalMember)this.field;
/*  81 */       if (localLocalMember.scopeNumber < paramContext.frameNumber) {
/*  82 */         paramEnvironment.error(this.where, "assign.to.uplevel", this.id);
/*     */       }
/*  84 */       if (localLocalMember.isFinal())
/*     */       {
/*  86 */         if (!localLocalMember.isBlankFinal())
/*  87 */           paramEnvironment.error(this.where, "assign.to.final", this.id);
/*  88 */         else if (!paramVset.testVarUnassigned(localLocalMember.number)) {
/*  89 */           paramEnvironment.error(this.where, "assign.to.blank.final", this.id);
/*     */         }
/*     */       }
/*  92 */       paramVset.addVar(localLocalMember.number);
/*  93 */       localLocalMember.writecount += 1;
/*  94 */     } else if (this.field.isFinal()) {
/*  95 */       paramVset = FieldExpression.checkFinalAssign(paramEnvironment, paramContext, paramVset, this.where, this.field);
/*     */     }
/*     */ 
/*  98 */     return paramVset;
/*     */   }
/*     */ 
/*     */   private Vset get(Environment paramEnvironment, Context paramContext, Vset paramVset)
/*     */   {
/* 105 */     if (this.field.isLocal()) {
/* 106 */       LocalMember localLocalMember = (LocalMember)this.field;
/* 107 */       if ((localLocalMember.scopeNumber < paramContext.frameNumber) && (!localLocalMember.isFinal())) {
/* 108 */         paramEnvironment.error(this.where, "invalid.uplevel", this.id);
/*     */       }
/* 110 */       if (!paramVset.testVar(localLocalMember.number)) {
/* 111 */         paramEnvironment.error(this.where, "var.not.initialized", this.id);
/* 112 */         paramVset.addVar(localLocalMember.number);
/*     */       }
/* 114 */       localLocalMember.readcount += 1;
/*     */     } else {
/* 116 */       if ((!this.field.isStatic()) && 
/* 117 */         (!paramVset.testVar(paramContext.getThisNumber()))) {
/* 118 */         paramEnvironment.error(this.where, "access.inst.before.super", this.id);
/* 119 */         this.implementation = null;
/*     */       }
/*     */ 
/* 122 */       if (this.field.isBlankFinal()) {
/* 123 */         int i = paramContext.getFieldNumber(this.field);
/* 124 */         if ((i >= 0) && (!paramVset.testVar(i))) {
/* 125 */           paramEnvironment.error(this.where, "var.not.initialized", this.id);
/*     */         }
/*     */       }
/*     */     }
/* 129 */     return paramVset;
/*     */   }
/*     */ 
/*     */   boolean bind(Environment paramEnvironment, Context paramContext)
/*     */   {
/*     */     try
/*     */     {
/* 137 */       this.field = paramContext.getField(paramEnvironment, this.id);
/*     */       Object localObject1;
/* 138 */       if (this.field == null) {
/* 139 */         for (localObject1 = paramContext.field.getClassDefinition(); 
/* 140 */           localObject1 != null; localObject1 = ((ClassDefinition)localObject1).getOuterClass()) {
/* 141 */           if (((ClassDefinition)localObject1).findAnyMethod(paramEnvironment, this.id) != null) {
/* 142 */             paramEnvironment.error(this.where, "invalid.var", this.id, paramContext.field
/* 143 */               .getClassDeclaration());
/* 144 */             return false;
/*     */           }
/*     */         }
/* 147 */         paramEnvironment.error(this.where, "undef.var", this.id);
/* 148 */         return false;
/*     */       }
/*     */ 
/* 151 */       this.type = this.field.getType();
/*     */ 
/* 154 */       if (!paramContext.field.getClassDefinition().canAccess(paramEnvironment, this.field)) {
/* 155 */         paramEnvironment.error(this.where, "no.field.access", this.id, this.field
/* 156 */           .getClassDeclaration(), paramContext.field
/* 157 */           .getClassDeclaration());
/* 158 */         return false;
/*     */       }
/*     */ 
/* 162 */       if (this.field.isLocal()) {
/* 163 */         localObject1 = (LocalMember)this.field;
/* 164 */         if (((LocalMember)localObject1).scopeNumber < paramContext.frameNumber)
/*     */         {
/* 166 */           this.implementation = paramContext.makeReference(paramEnvironment, (LocalMember)localObject1);
/*     */         }
/*     */       } else {
/* 169 */         localObject1 = this.field;
/*     */ 
/* 171 */         if (((MemberDefinition)localObject1).reportDeprecated(paramEnvironment)) {
/* 172 */           paramEnvironment.error(this.where, "warn.field.is.deprecated", this.id, ((MemberDefinition)localObject1)
/* 173 */             .getClassDefinition());
/*     */         }
/*     */ 
/* 176 */         ClassDefinition localClassDefinition1 = ((MemberDefinition)localObject1).getClassDefinition();
/*     */         Object localObject2;
/* 177 */         if (localClassDefinition1 != paramContext.field.getClassDefinition())
/*     */         {
/* 179 */           localObject2 = paramContext.getApparentField(paramEnvironment, this.id);
/* 180 */           if ((localObject2 != null) && (localObject2 != localObject1)) {
/* 181 */             ClassDefinition localClassDefinition2 = paramContext.findScope(paramEnvironment, localClassDefinition1);
/* 182 */             if (localClassDefinition2 == null) localClassDefinition2 = ((MemberDefinition)localObject1).getClassDefinition();
/* 183 */             if (((MemberDefinition)localObject2).isLocal())
/* 184 */               paramEnvironment.error(this.where, "inherited.hides.local", this.id, localClassDefinition2
/* 185 */                 .getClassDeclaration());
/*     */             else {
/* 187 */               paramEnvironment.error(this.where, "inherited.hides.field", this.id, localClassDefinition2
/* 188 */                 .getClassDeclaration(), ((MemberDefinition)localObject2)
/* 189 */                 .getClassDeclaration());
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 202 */         if (((MemberDefinition)localObject1).isStatic())
/*     */         {
/* 204 */           localObject2 = new TypeExpression(this.where, ((MemberDefinition)localObject1)
/* 204 */             .getClassDeclaration().getType());
/* 205 */           this.implementation = new FieldExpression(this.where, null, (MemberDefinition)localObject1);
/*     */         } else {
/* 207 */           localObject2 = paramContext.findOuterLink(paramEnvironment, this.where, (MemberDefinition)localObject1);
/* 208 */           if (localObject2 != null) {
/* 209 */             this.implementation = new FieldExpression(this.where, (Expression)localObject2, (MemberDefinition)localObject1);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 215 */       if (!paramContext.canReach(paramEnvironment, this.field)) {
/* 216 */         paramEnvironment.error(this.where, "forward.ref", this.id, this.field
/* 217 */           .getClassDeclaration());
/* 218 */         return false;
/*     */       }
/* 220 */       return true;
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 222 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, paramContext.field);
/*     */     } catch (AmbiguousMember localAmbiguousMember) {
/* 224 */       paramEnvironment.error(this.where, "ambig.field", this.id, localAmbiguousMember.field1
/* 225 */         .getClassDeclaration(), localAmbiguousMember.field2
/* 226 */         .getClassDeclaration());
/*     */     }
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 235 */     if (this.field != null)
/*     */     {
/* 238 */       return paramVset;
/*     */     }
/* 240 */     if (bind(paramEnvironment, paramContext)) {
/* 241 */       paramVset = get(paramEnvironment, paramContext, paramVset);
/* 242 */       paramContext.field.getClassDefinition().addDependency(this.field.getClassDeclaration());
/* 243 */       if (this.implementation != null)
/* 244 */         paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     }
/* 246 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 254 */     if (!bind(paramEnvironment, paramContext))
/* 255 */       return paramVset;
/* 256 */     paramVset = assign(paramEnvironment, paramContext, paramVset);
/* 257 */     if (this.implementation != null)
/* 258 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 259 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression)
/*     */   {
/* 267 */     if (!bind(paramEnvironment, paramContext))
/* 268 */       return paramVset;
/* 269 */     paramVset = assign(paramEnvironment, paramContext, get(paramEnvironment, paramContext, paramVset));
/* 270 */     if (this.implementation != null)
/* 271 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 272 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 279 */     if (this.implementation != null)
/* 280 */       return this.implementation.getAssigner(paramEnvironment, paramContext);
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 288 */     if (this.implementation != null)
/* 289 */       return this.implementation.getUpdater(paramEnvironment, paramContext);
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression)
/*     */   {
/*     */     try
/*     */     {
/* 299 */       if (paramContext.getField(paramEnvironment, this.id) != null)
/*     */       {
/* 301 */         return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound) {
/*     */     }
/*     */     catch (AmbiguousMember localAmbiguousMember) {
/*     */     }
/* 307 */     ClassDefinition localClassDefinition = toResolvedType(paramEnvironment, paramContext, true);
/*     */ 
/* 309 */     if (localClassDefinition != null) {
/* 310 */       paramUnaryExpression.right = new TypeExpression(this.where, localClassDefinition.getType());
/* 311 */       return paramVset;
/*     */     }
/*     */ 
/* 314 */     this.type = Type.tPackage;
/* 315 */     return paramVset;
/*     */   }
/*     */ 
/*     */   private ClassDefinition toResolvedType(Environment paramEnvironment, Context paramContext, boolean paramBoolean)
/*     */   {
/* 323 */     Identifier localIdentifier1 = paramContext.resolveName(paramEnvironment, this.id);
/* 324 */     Type localType = Type.tClass(localIdentifier1);
/* 325 */     if ((paramBoolean) && (!paramEnvironment.classExists(localType))) {
/* 326 */       return null;
/*     */     }
/* 328 */     if (paramEnvironment.resolve(this.where, paramContext.field.getClassDefinition(), localType))
/*     */       try {
/* 330 */         ClassDefinition localClassDefinition1 = paramEnvironment.getClassDefinition(localType);
/*     */ 
/* 333 */         if (localClassDefinition1.isMember()) {
/* 334 */           ClassDefinition localClassDefinition2 = paramContext.findScope(paramEnvironment, localClassDefinition1.getOuterClass());
/* 335 */           if (localClassDefinition2 != localClassDefinition1.getOuterClass()) {
/* 336 */             Identifier localIdentifier2 = paramContext.getApparentClassName(paramEnvironment, this.id);
/* 337 */             if ((!localIdentifier2.equals(idNull)) && (!localIdentifier2.equals(localIdentifier1))) {
/* 338 */               paramEnvironment.error(this.where, "inherited.hides.type", this.id, localClassDefinition2
/* 339 */                 .getClassDeclaration());
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 344 */         if (!localClassDefinition1.getLocalName().equals(this.id.getFlatName().getName())) {
/* 345 */           paramEnvironment.error(this.where, "illegal.mangled.name", this.id, localClassDefinition1);
/*     */         }
/*     */ 
/* 348 */         return localClassDefinition1;
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound) {
/*     */       }
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */   Type toType(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 360 */     ClassDefinition localClassDefinition = toResolvedType(paramEnvironment, paramContext, false);
/* 361 */     if (localClassDefinition != null) {
/* 362 */       return localClassDefinition.getType();
/*     */     }
/* 364 */     return Type.tError;
/*     */   }
/*     */ 
/*     */   public boolean isConstant()
/*     */   {
/* 391 */     if (this.implementation != null)
/* 392 */       return this.implementation.isConstant();
/* 393 */     if (this.field != null) {
/* 394 */       return this.field.isConstant();
/*     */     }
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 403 */     return null;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 406 */     if (this.implementation != null)
/* 407 */       return this.implementation.inlineValue(paramEnvironment, paramContext);
/* 408 */     if (this.field == null)
/* 409 */       return this;
/*     */     try
/*     */     {
/* 412 */       if (this.field.isLocal()) {
/* 413 */         if (this.field.isInlineable(paramEnvironment, false)) {
/* 414 */           Expression localExpression = (Expression)this.field.getValue(paramEnvironment);
/* 415 */           return localExpression == null ? this : localExpression.inlineValue(paramEnvironment, paramContext);
/*     */         }
/* 417 */         return this;
/*     */       }
/* 419 */       return this;
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 421 */       throw new CompilerError(localClassNotFound);
/*     */     }
/*     */   }
/*     */ 
/* 425 */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) { if (this.implementation != null)
/* 426 */       return this.implementation.inlineLHS(paramEnvironment, paramContext);
/* 427 */     return this; }
/*     */ 
/*     */   public Expression copyInline(Context paramContext)
/*     */   {
/* 431 */     if (this.implementation != null) {
/* 432 */       return this.implementation.copyInline(paramContext);
/*     */     }
/* 434 */     IdentifierExpression localIdentifierExpression = (IdentifierExpression)super
/* 434 */       .copyInline(paramContext);
/*     */ 
/* 435 */     if ((this.field != null) && (this.field.isLocal())) {
/* 436 */       localIdentifierExpression.field = ((LocalMember)this.field).getCurrentInlineCopy(paramContext);
/*     */     }
/* 438 */     return localIdentifierExpression;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 442 */     if (this.implementation != null)
/* 443 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext);
/* 444 */     return super.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 451 */     return 0;
/*     */   }
/*     */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 454 */     paramAssembler.add(this.where, 21 + this.type.getTypeCodeOffset(), new Integer(((LocalMember)this.field).number));
/*     */   }
/*     */ 
/*     */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 458 */     LocalMember localLocalMember = (LocalMember)this.field;
/* 459 */     paramAssembler.add(this.where, 54 + this.type.getTypeCodeOffset(), new LocalVariable(localLocalMember, localLocalMember.number));
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 463 */     codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 464 */     codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 471 */     paramPrintStream.print(this.id + "#" + (this.field != null ? this.field.hashCode() : 0));
/* 472 */     if (this.implementation != null) {
/* 473 */       paramPrintStream.print("/IMPL=");
/* 474 */       this.implementation.print(paramPrintStream);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.IdentifierExpression
 * JD-Core Version:    0.6.2
 */