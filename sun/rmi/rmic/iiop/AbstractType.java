/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class AbstractType extends RemoteType
/*     */ {
/*     */   public static AbstractType forAbstract(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/*  65 */     int i = 0;
/*  66 */     Object localObject = null;
/*     */     try
/*     */     {
/*  72 */       sun.tools.java.Type localType = paramClassDefinition.getType();
/*  73 */       Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  75 */       if (localType1 != null)
/*     */       {
/*  77 */         if (!(localType1 instanceof AbstractType)) return null;
/*     */ 
/*  81 */         return (AbstractType)localType1;
/*     */       }
/*     */ 
/*  87 */       if (couldBeAbstract(paramContextStack, paramClassDefinition, paramBoolean))
/*     */       {
/*  91 */         AbstractType localAbstractType = new AbstractType(paramContextStack, paramClassDefinition);
/*  92 */         putType(localType, localAbstractType, paramContextStack);
/*  93 */         paramContextStack.push(localAbstractType);
/*  94 */         i = 1;
/*     */ 
/*  96 */         if (localAbstractType.initialize(paramBoolean, paramContextStack)) {
/*  97 */           paramContextStack.pop(true);
/*  98 */           localObject = localAbstractType;
/*     */         } else {
/* 100 */           removeType(localType, paramContextStack);
/* 101 */           paramContextStack.pop(false);
/*     */         }
/*     */       }
/*     */     } catch (CompilerError localCompilerError) {
/* 105 */       if (i != 0) paramContextStack.pop(false);
/*     */     }
/*     */ 
/* 108 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 115 */     return "Abstract interface";
/*     */   }
/*     */ 
/*     */   private AbstractType(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 127 */     super(paramContextStack, paramClassDefinition, 167780352);
/*     */   }
/*     */ 
/*     */   private static boolean couldBeAbstract(ContextStack paramContextStack, ClassDefinition paramClassDefinition, boolean paramBoolean)
/*     */   {
/* 140 */     boolean bool = false;
/*     */ 
/* 142 */     if (paramClassDefinition.isInterface()) {
/* 143 */       BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*     */       try
/*     */       {
/* 146 */         bool = !localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, paramClassDefinition.getClassDeclaration());
/* 147 */         if (!bool) failedConstraint(15, paramBoolean, paramContextStack, paramClassDefinition.getName()); 
/*     */       }
/* 149 */       catch (ClassNotFound localClassNotFound) { classNotFound(paramContextStack, localClassNotFound); }
/*     */     }
/*     */     else {
/* 152 */       failedConstraint(14, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */     }
/*     */ 
/* 156 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean initialize(boolean paramBoolean, ContextStack paramContextStack)
/*     */   {
/* 165 */     boolean bool = false;
/* 166 */     ClassDefinition localClassDefinition = getClassDefinition();
/*     */     try
/*     */     {
/* 172 */       Vector localVector = new Vector();
/*     */ 
/* 174 */       if (addAllMethods(localClassDefinition, localVector, true, paramBoolean, paramContextStack) != null)
/*     */       {
/* 178 */         int i = 1;
/*     */ 
/* 180 */         if (localVector.size() > 0)
/*     */         {
/* 184 */           for (int j = 0; j < localVector.size(); j++)
/*     */           {
/* 186 */             if (!isConformingRemoteMethod((CompoundType.Method)localVector.elementAt(j), true)) {
/* 187 */               i = 0;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 192 */         if (i != 0)
/*     */         {
/* 196 */           bool = initialize(null, localVector, null, paramContextStack, paramBoolean);
/*     */         }
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 200 */       classNotFound(paramContextStack, localClassNotFound);
/*     */     }
/*     */ 
/* 203 */     return bool;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.AbstractType
 * JD-Core Version:    0.6.2
 */