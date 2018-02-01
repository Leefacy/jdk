/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class RemoteType extends InterfaceType
/*     */ {
/*     */   public static RemoteType forRemote(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/*  65 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  67 */     int i = 0;
/*  68 */     Object localObject = null;
/*     */     try
/*     */     {
/*  73 */       sun.tools.java.Type localType = paramClassDefinition.getType();
/*  74 */       Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  76 */       if (localType1 != null)
/*     */       {
/*  78 */         if (!(localType1 instanceof RemoteType)) return null;
/*     */ 
/*  82 */         return (RemoteType)localType1;
/*     */       }
/*     */ 
/*  87 */       if (couldBeRemote(paramBoolean, paramContextStack, paramClassDefinition))
/*     */       {
/*  91 */         RemoteType localRemoteType = new RemoteType(paramContextStack, paramClassDefinition);
/*  92 */         putType(localType, localRemoteType, paramContextStack);
/*  93 */         paramContextStack.push(localRemoteType);
/*  94 */         i = 1;
/*     */ 
/*  96 */         if (localRemoteType.initialize(paramBoolean, paramContextStack)) {
/*  97 */           paramContextStack.pop(true);
/*  98 */           localObject = localRemoteType;
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
/* 115 */     return "Remote interface";
/*     */   }
/*     */ 
/*     */   protected RemoteType(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 127 */     super(paramContextStack, paramClassDefinition, 167776256);
/*     */   }
/*     */ 
/*     */   protected RemoteType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt)
/*     */   {
/* 135 */     super(paramContextStack, paramClassDefinition, paramInt);
/*     */   }
/*     */ 
/*     */   private static boolean couldBeRemote(boolean paramBoolean, ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 146 */     boolean bool = false;
/* 147 */     BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*     */     try
/*     */     {
/* 150 */       if (!paramClassDefinition.isInterface()) {
/* 151 */         failedConstraint(16, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       } else {
/* 153 */         bool = localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, paramClassDefinition.getClassDeclaration());
/* 154 */         if (!bool) failedConstraint(1, paramBoolean, paramContextStack, paramClassDefinition.getName()); 
/*     */       }
/*     */     }
/* 157 */     catch (ClassNotFound localClassNotFound) { classNotFound(paramContextStack, localClassNotFound); }
/*     */ 
/*     */ 
/* 160 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean initialize(boolean paramBoolean, ContextStack paramContextStack)
/*     */   {
/* 169 */     boolean bool = false;
/*     */ 
/* 173 */     Vector localVector1 = new Vector();
/* 174 */     Vector localVector2 = new Vector();
/* 175 */     Vector localVector3 = new Vector();
/*     */ 
/* 177 */     if (isConformingRemoteInterface(localVector1, localVector2, localVector3, paramBoolean, paramContextStack))
/*     */     {
/* 185 */       bool = initialize(localVector1, localVector2, localVector3, paramContextStack, paramBoolean);
/*     */     }
/*     */ 
/* 188 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean isConformingRemoteInterface(Vector paramVector1, Vector paramVector2, Vector paramVector3, boolean paramBoolean, ContextStack paramContextStack)
/*     */   {
/* 210 */     ClassDefinition localClassDefinition = getClassDefinition();
/*     */     try
/*     */     {
/* 216 */       if (addRemoteInterfaces(paramVector1, false, paramContextStack) == null) {
/* 217 */         return false;
/*     */       }
/*     */ 
/* 222 */       if (!addAllMembers(paramVector3, true, paramBoolean, paramContextStack)) {
/* 223 */         return false;
/*     */       }
/*     */ 
/* 228 */       if (addAllMethods(localClassDefinition, paramVector2, true, paramBoolean, paramContextStack) == null)
/*     */       {
/* 230 */         return false;
/*     */       }
/*     */ 
/* 235 */       int i = 1;
/* 236 */       for (int j = 0; j < paramVector2.size(); j++) {
/* 237 */         if (!isConformingRemoteMethod((CompoundType.Method)paramVector2.elementAt(j), paramBoolean)) {
/* 238 */           i = 0;
/*     */         }
/*     */       }
/* 241 */       if (i == 0)
/* 242 */         return false;
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound) {
/* 245 */       classNotFound(paramContextStack, localClassNotFound);
/* 246 */       return false;
/*     */     }
/*     */ 
/* 249 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.RemoteType
 * JD-Core Version:    0.6.2
 */