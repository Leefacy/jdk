/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class AssignAddExpression extends AssignOpExpression
/*     */ {
/*     */   public AssignAddExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*     */   {
/*  42 */     super(5, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*     */   {
/*  50 */     return this.type.isType(10) ? 25 : super.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean)
/*     */   {
/*  57 */     if (this.itype.isType(10))
/*     */     {
/*     */       try
/*     */       {
/*  61 */         Type[] arrayOfType = { Type.tString };
/*     */ 
/*  63 */         ClassDeclaration localClassDeclaration = paramEnvironment
/*  63 */           .getClassDeclaration(idJavaLangStringBuffer);
/*     */         Object localObject;
/*  65 */         if (this.updater == null)
/*     */         {
/*  69 */           paramAssembler.add(this.where, 187, localClassDeclaration);
/*  70 */           paramAssembler.add(this.where, 89);
/*     */ 
/*  72 */           int i = this.left.codeLValue(paramEnvironment, paramContext, paramAssembler);
/*  73 */           codeDup(paramEnvironment, paramContext, paramAssembler, i, 2);
/*     */ 
/*  78 */           this.left.codeLoad(paramEnvironment, paramContext, paramAssembler);
/*  79 */           this.left.ensureString(paramEnvironment, paramContext, paramAssembler);
/*     */ 
/*  82 */           localObject = paramContext.field.getClassDefinition();
/*     */ 
/*  84 */           MemberDefinition localMemberDefinition = localClassDeclaration.getClassDefinition(paramEnvironment)
/*  84 */             .matchMethod(paramEnvironment, (ClassDefinition)localObject, idInit, arrayOfType);
/*     */ 
/*  86 */           paramAssembler.add(this.where, 183, localMemberDefinition);
/*     */ 
/*  89 */           this.right.codeAppend(paramEnvironment, paramContext, paramAssembler, localClassDeclaration, false);
/*     */ 
/*  91 */           localMemberDefinition = localClassDeclaration.getClassDefinition(paramEnvironment)
/*  91 */             .matchMethod(paramEnvironment, (ClassDefinition)localObject, idToString);
/*     */ 
/*  92 */           paramAssembler.add(this.where, 182, localMemberDefinition);
/*     */ 
/*  95 */           if (paramBoolean) {
/*  96 */             codeDup(paramEnvironment, paramContext, paramAssembler, Type.tString.stackSize(), i);
/*     */           }
/*     */ 
/* 100 */           this.left.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */         }
/*     */         else
/*     */         {
/* 107 */           this.updater.startUpdate(paramEnvironment, paramContext, paramAssembler, false);
/*     */ 
/* 109 */           this.left.ensureString(paramEnvironment, paramContext, paramAssembler);
/* 110 */           paramAssembler.add(this.where, 187, localClassDeclaration);
/*     */ 
/* 112 */           paramAssembler.add(this.where, 90);
/*     */ 
/* 114 */           paramAssembler.add(this.where, 95);
/*     */ 
/* 117 */           ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/*     */ 
/* 119 */           localObject = localClassDeclaration.getClassDefinition(paramEnvironment)
/* 119 */             .matchMethod(paramEnvironment, localClassDefinition, idInit, arrayOfType);
/*     */ 
/* 121 */           paramAssembler.add(this.where, 183, localObject);
/*     */ 
/* 124 */           this.right.codeAppend(paramEnvironment, paramContext, paramAssembler, localClassDeclaration, false);
/*     */ 
/* 126 */           localObject = localClassDeclaration.getClassDefinition(paramEnvironment)
/* 126 */             .matchMethod(paramEnvironment, localClassDefinition, idToString);
/*     */ 
/* 127 */           paramAssembler.add(this.where, 182, localObject);
/*     */ 
/* 129 */           this.updater.finishUpdate(paramEnvironment, paramContext, paramAssembler, paramBoolean);
/*     */         }
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound)
/*     */       {
/* 134 */         throw new CompilerError(localClassNotFound);
/*     */       } catch (AmbiguousMember localAmbiguousMember) {
/* 136 */         throw new CompilerError(localAmbiguousMember);
/*     */       }
/*     */     }
/* 139 */     else super.code(paramEnvironment, paramContext, paramAssembler, paramBoolean);
/*     */   }
/*     */ 
/*     */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 147 */     paramAssembler.add(this.where, 96 + this.itype.getTypeCodeOffset());
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.AssignAddExpression
 * JD-Core Version:    0.6.2
 */