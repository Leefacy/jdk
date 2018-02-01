/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ public class ImplementationType extends ClassType
/*     */ {
/*     */   public static ImplementationType forImplementation(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/*  66 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  68 */     int i = 0;
/*  69 */     Object localObject = null;
/*     */     try
/*     */     {
/*  74 */       sun.tools.java.Type localType = paramClassDefinition.getType();
/*  75 */       Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  77 */       if (localType1 != null)
/*     */       {
/*  79 */         if (!(localType1 instanceof ImplementationType)) return null;
/*     */ 
/*  83 */         return (ImplementationType)localType1;
/*     */       }
/*     */ 
/*  89 */       if (couldBeImplementation(paramBoolean, paramContextStack, paramClassDefinition))
/*     */       {
/*  93 */         ImplementationType localImplementationType = new ImplementationType(paramContextStack, paramClassDefinition);
/*  94 */         putType(localType, localImplementationType, paramContextStack);
/*  95 */         paramContextStack.push(localImplementationType);
/*  96 */         i = 1;
/*     */ 
/*  98 */         if (localImplementationType.initialize(paramContextStack, paramBoolean)) {
/*  99 */           paramContextStack.pop(true);
/* 100 */           localObject = localImplementationType;
/*     */         } else {
/* 102 */           removeType(localType, paramContextStack);
/* 103 */           paramContextStack.pop(false);
/*     */         }
/*     */       }
/*     */     } catch (CompilerError localCompilerError) {
/* 107 */       if (i != 0) paramContextStack.pop(false);
/*     */     }
/*     */ 
/* 110 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 117 */     return "Implementation";
/*     */   }
/*     */ 
/*     */   private ImplementationType(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 130 */     super(100728832, paramClassDefinition, paramContextStack);
/*     */   }
/*     */ 
/*     */   private static boolean couldBeImplementation(boolean paramBoolean, ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 136 */     boolean bool = false;
/* 137 */     BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*     */     try
/*     */     {
/* 140 */       if (!paramClassDefinition.isClass()) {
/* 141 */         failedConstraint(17, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       } else {
/* 143 */         bool = localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, paramClassDefinition.getClassDeclaration());
/* 144 */         if (!bool) failedConstraint(8, paramBoolean, paramContextStack, paramClassDefinition.getName()); 
/*     */       }
/*     */     }
/* 147 */     catch (ClassNotFound localClassNotFound) { classNotFound(paramContextStack, localClassNotFound); }
/*     */ 
/*     */ 
/* 150 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean initialize(ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/* 159 */     boolean bool = false;
/* 160 */     ClassDefinition localClassDefinition = getClassDefinition();
/*     */ 
/* 162 */     if (initParents(paramContextStack))
/*     */     {
/* 166 */       Vector localVector1 = new Vector();
/* 167 */       Vector localVector2 = new Vector();
/*     */       try
/*     */       {
/* 172 */         if (addRemoteInterfaces(localVector1, true, paramContextStack) != null)
/*     */         {
/* 174 */           int i = 0;
/*     */ 
/* 178 */           for (int j = 0; j < localVector1.size(); j++) {
/* 179 */             InterfaceType localInterfaceType = (InterfaceType)localVector1.elementAt(j);
/* 180 */             if ((localInterfaceType.isType(4096)) || 
/* 181 */               (localInterfaceType
/* 181 */               .isType(524288)))
/*     */             {
/* 182 */               i = 1;
/*     */             }
/*     */ 
/* 185 */             copyRemoteMethods(localInterfaceType, localVector2);
/*     */           }
/*     */ 
/* 190 */           if (i == 0) {
/* 191 */             failedConstraint(8, paramBoolean, paramContextStack, getQualifiedName());
/* 192 */             return false;
/*     */           }
/*     */ 
/* 198 */           if (checkMethods(localClassDefinition, localVector2, paramContextStack, paramBoolean))
/*     */           {
/* 202 */             bool = initialize(localVector1, localVector2, null, paramContextStack, paramBoolean);
/*     */           }
/*     */         }
/*     */       } catch (ClassNotFound localClassNotFound) {
/* 206 */         classNotFound(paramContextStack, localClassNotFound);
/*     */       }
/*     */     }
/*     */ 
/* 210 */     return bool;
/*     */   }
/*     */ 
/*     */   private static void copyRemoteMethods(InterfaceType paramInterfaceType, Vector paramVector)
/*     */   {
/* 215 */     if (paramInterfaceType.isType(4096))
/*     */     {
/* 219 */       CompoundType.Method[] arrayOfMethod = paramInterfaceType.getMethods();
/*     */ 
/* 221 */       for (int i = 0; i < arrayOfMethod.length; i++) {
/* 222 */         CompoundType.Method localMethod = arrayOfMethod[i];
/*     */ 
/* 224 */         if (!paramVector.contains(localMethod)) {
/* 225 */           paramVector.addElement(localMethod);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 231 */       InterfaceType[] arrayOfInterfaceType = paramInterfaceType.getInterfaces();
/*     */ 
/* 233 */       for (int j = 0; j < arrayOfInterfaceType.length; j++)
/* 234 */         copyRemoteMethods(arrayOfInterfaceType[j], paramVector);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean checkMethods(ClassDefinition paramClassDefinition, Vector paramVector, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/* 247 */     CompoundType.Method[] arrayOfMethod = new CompoundType.Method[paramVector.size()];
/* 248 */     paramVector.copyInto(arrayOfMethod);
/*     */ 
/* 250 */     for (MemberDefinition localMemberDefinition = paramClassDefinition.getFirstMember(); 
/* 251 */       localMemberDefinition != null; 
/* 252 */       localMemberDefinition = localMemberDefinition.getNextMember())
/*     */     {
/* 254 */       if ((localMemberDefinition.isMethod()) && (!localMemberDefinition.isConstructor()) && 
/* 255 */         (!localMemberDefinition
/* 255 */         .isInitializer()))
/*     */       {
/* 259 */         if (!updateExceptions(localMemberDefinition, arrayOfMethod, paramContextStack, paramBoolean)) {
/* 260 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean updateExceptions(MemberDefinition paramMemberDefinition, CompoundType.Method[] paramArrayOfMethod, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/* 269 */     int i = paramArrayOfMethod.length;
/* 270 */     String str = paramMemberDefinition.toString();
/*     */ 
/* 272 */     for (int j = 0; j < i; j++) {
/* 273 */       CompoundType.Method localMethod = paramArrayOfMethod[j];
/* 274 */       MemberDefinition localMemberDefinition = localMethod.getMemberDefinition();
/*     */ 
/* 278 */       if (str.equals(localMemberDefinition.toString()))
/*     */       {
/*     */         try
/*     */         {
/* 283 */           ValueType[] arrayOfValueType = getMethodExceptions(paramMemberDefinition, paramBoolean, paramContextStack);
/* 284 */           localMethod.setImplExceptions(arrayOfValueType);
/*     */         } catch (Exception localException) {
/* 286 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 290 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.ImplementationType
 * JD-Core Version:    0.6.2
 */