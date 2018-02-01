/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ public class UplevelReference
/*     */   implements Constants
/*     */ {
/*     */   ClassDefinition client;
/*     */   LocalMember target;
/*     */   LocalMember localArgument;
/*     */   MemberDefinition localField;
/*     */   UplevelReference next;
/*     */ 
/*     */   public UplevelReference(ClassDefinition paramClassDefinition, LocalMember paramLocalMember)
/*     */   {
/*  88 */     this.client = paramClassDefinition;
/*  89 */     this.target = paramLocalMember;
/*     */     Identifier localIdentifier;
/*  93 */     if (paramLocalMember.getName().equals(idThis)) {
/*  94 */       localObject1 = paramLocalMember.getClassDefinition();
/*     */ 
/* 102 */       i = 0;
/* 103 */       for (Object localObject2 = localObject1; !((ClassDefinition)localObject2).isTopLevel(); localObject2 = ((ClassDefinition)localObject2).getOuterClass())
/*     */       {
/* 108 */         i++;
/*     */       }
/*     */ 
/* 133 */       localIdentifier = Identifier.lookup("this$" + i);
/*     */     } else {
/* 135 */       localIdentifier = Identifier.lookup("val$" + paramLocalMember.getName());
/*     */     }
/*     */ 
/* 141 */     Object localObject1 = localIdentifier;
/* 142 */     int i = 0;
/*     */     while (true) {
/* 144 */       int j = paramClassDefinition.getFirstMatch(localIdentifier) != null ? 1 : 0;
/* 145 */       for (UplevelReference localUplevelReference = paramClassDefinition.getReferences(); 
/* 146 */         localUplevelReference != null; localUplevelReference = localUplevelReference.next) {
/* 147 */         if (localUplevelReference.target.getName().equals(localIdentifier)) {
/* 148 */           j = 1;
/*     */         }
/*     */       }
/* 151 */       if (j == 0)
/*     */       {
/*     */         break;
/*     */       }
/* 155 */       localIdentifier = Identifier.lookup(localObject1 + "$" + ++i);
/*     */     }
/*     */ 
/* 160 */     this.localArgument = new LocalMember(paramLocalMember.getWhere(), paramClassDefinition, 524304, paramLocalMember
/* 163 */       .getType(), localIdentifier);
/*     */   }
/*     */ 
/*     */   public UplevelReference insertInto(UplevelReference paramUplevelReference)
/*     */   {
/* 175 */     if ((paramUplevelReference == null) || (isEarlierThan(paramUplevelReference))) {
/* 176 */       this.next = paramUplevelReference;
/* 177 */       return this;
/*     */     }
/* 179 */     UplevelReference localUplevelReference = paramUplevelReference;
/* 180 */     while ((localUplevelReference.next != null) && (!isEarlierThan(localUplevelReference.next))) {
/* 181 */       localUplevelReference = localUplevelReference.next;
/*     */     }
/* 183 */     this.next = localUplevelReference.next;
/* 184 */     localUplevelReference.next = this;
/* 185 */     return paramUplevelReference;
/*     */   }
/*     */ 
/*     */   public final boolean isEarlierThan(UplevelReference paramUplevelReference)
/*     */   {
/* 194 */     if (isClientOuterField())
/* 195 */       return true;
/* 196 */     if (paramUplevelReference.isClientOuterField()) {
/* 197 */       return false;
/*     */     }
/*     */ 
/* 201 */     LocalMember localLocalMember = paramUplevelReference.target;
/* 202 */     Identifier localIdentifier1 = this.target.getName();
/* 203 */     Identifier localIdentifier2 = localLocalMember.getName();
/* 204 */     int i = localIdentifier1.toString().compareTo(localIdentifier2.toString());
/* 205 */     if (i != 0) {
/* 206 */       return i < 0;
/*     */     }
/* 208 */     Identifier localIdentifier3 = this.target.getClassDefinition().getName();
/* 209 */     Identifier localIdentifier4 = localLocalMember.getClassDefinition().getName();
/* 210 */     int j = localIdentifier3.toString().compareTo(localIdentifier4.toString());
/* 211 */     return j < 0;
/*     */   }
/*     */ 
/*     */   public final LocalMember getTarget()
/*     */   {
/* 218 */     return this.target;
/*     */   }
/*     */ 
/*     */   public final LocalMember getLocalArgument()
/*     */   {
/* 225 */     return this.localArgument;
/*     */   }
/*     */ 
/*     */   public final MemberDefinition getLocalField()
/*     */   {
/* 232 */     return this.localField;
/*     */   }
/*     */ 
/*     */   public final MemberDefinition getLocalField(Environment paramEnvironment)
/*     */   {
/* 240 */     if (this.localField == null) {
/* 241 */       makeLocalField(paramEnvironment);
/*     */     }
/* 243 */     return this.localField;
/*     */   }
/*     */ 
/*     */   public final ClassDefinition getClient()
/*     */   {
/* 250 */     return this.client;
/*     */   }
/*     */ 
/*     */   public final UplevelReference getNext()
/*     */   {
/* 257 */     return this.next;
/*     */   }
/*     */ 
/*     */   public boolean isClientOuterField()
/*     */   {
/* 267 */     MemberDefinition localMemberDefinition = this.client.findOuterMember();
/* 268 */     return (localMemberDefinition != null) && (this.localField == localMemberDefinition);
/*     */   }
/*     */ 
/*     */   public boolean localArgumentAvailable(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 279 */     MemberDefinition localMemberDefinition = paramContext.field;
/* 280 */     if (localMemberDefinition.getClassDefinition() != this.client) {
/* 281 */       throw new CompilerError("localArgumentAvailable");
/*     */     }
/*     */ 
/* 285 */     return (localMemberDefinition.isConstructor()) || 
/* 284 */       (localMemberDefinition
/* 284 */       .isVariable()) || 
/* 285 */       (localMemberDefinition
/* 285 */       .isInitializer());
/*     */   }
/*     */ 
/*     */   public void noteReference(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 295 */     if ((this.localField == null) && (!localArgumentAvailable(paramEnvironment, paramContext)))
/*     */     {
/* 297 */       makeLocalField(paramEnvironment);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void makeLocalField(Environment paramEnvironment)
/*     */   {
/* 303 */     this.client.referencesMustNotBeFrozen();
/* 304 */     int i = 524306;
/* 305 */     this.localField = paramEnvironment.makeMemberDefinition(paramEnvironment, this.localArgument
/* 306 */       .getWhere(), this.client, null, i, this.localArgument
/* 309 */       .getType(), this.localArgument
/* 310 */       .getName(), null, null, null);
/*     */   }
/*     */ 
/*     */   public Expression makeLocalReference(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 322 */     if (paramContext.field.getClassDefinition() != this.client) {
/* 323 */       throw new CompilerError("makeLocalReference");
/*     */     }
/* 325 */     if (localArgumentAvailable(paramEnvironment, paramContext)) {
/* 326 */       return new IdentifierExpression(0L, this.localArgument);
/*     */     }
/* 328 */     return makeFieldReference(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public Expression makeFieldReference(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 337 */     Expression localExpression = paramContext.findOuterLink(paramEnvironment, 0L, this.localField);
/* 338 */     return new FieldExpression(0L, localExpression, this.localField);
/*     */   }
/*     */ 
/*     */   public void willCodeArguments(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 351 */     if (!isClientOuterField()) {
/* 352 */       paramContext.noteReference(paramEnvironment, this.target);
/*     */     }
/*     */ 
/* 355 */     if (this.next != null)
/* 356 */       this.next.willCodeArguments(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public void codeArguments(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, long paramLong, MemberDefinition paramMemberDefinition)
/*     */   {
/* 366 */     if (!isClientOuterField()) {
/* 367 */       Expression localExpression = paramContext.makeReference(paramEnvironment, this.target);
/* 368 */       localExpression.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */ 
/* 371 */     if (this.next != null)
/* 372 */       this.next.codeArguments(paramEnvironment, paramContext, paramAssembler, paramLong, paramMemberDefinition);
/*     */   }
/*     */ 
/*     */   public void codeInitialization(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, long paramLong, MemberDefinition paramMemberDefinition)
/*     */   {
/* 385 */     if ((this.localField != null) && (!isClientOuterField())) {
/* 386 */       Object localObject = paramContext.makeReference(paramEnvironment, this.target);
/* 387 */       Expression localExpression = makeFieldReference(paramEnvironment, paramContext);
/* 388 */       localObject = new AssignExpression(((Expression)localObject).getWhere(), localExpression, (Expression)localObject);
/* 389 */       ((Expression)localObject).type = this.localField.getType();
/* 390 */       ((Expression)localObject).code(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */ 
/* 393 */     if (this.next != null)
/* 394 */       this.next.codeInitialization(paramEnvironment, paramContext, paramAssembler, paramLong, paramMemberDefinition);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 399 */     return "[" + this.localArgument + " in " + this.client + "]";
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.UplevelReference
 * JD-Core Version:    0.6.2
 */