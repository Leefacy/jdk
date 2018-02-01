/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ public class ValueType extends ClassType
/*     */ {
/*     */   private boolean isCustom;
/*     */ 
/*     */   public static ValueType forValue(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/*  72 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  76 */     sun.tools.java.Type localType = paramClassDefinition.getType();
/*  77 */     String str = localType.toString();
/*  78 */     Type localType1 = getType(str, paramContextStack);
/*     */ 
/*  80 */     if (localType1 != null)
/*     */     {
/*  82 */       if (!(localType1 instanceof ValueType)) return null;
/*     */ 
/*  86 */       return (ValueType)localType1;
/*     */     }
/*     */ 
/*  91 */     boolean bool = false;
/*     */     Object localObject;
/*  93 */     if (paramClassDefinition.getClassDeclaration().getName() == idJavaLangClass)
/*     */     {
/*  98 */       bool = true;
/*  99 */       localObject = paramContextStack.getEnv();
/* 100 */       ClassDeclaration localClassDeclaration = ((BatchEnvironment)localObject).getClassDeclaration(idClassDesc);
/* 101 */       ClassDefinition localClassDefinition = null;
/*     */       try
/*     */       {
/* 104 */         localClassDefinition = localClassDeclaration.getClassDefinition((Environment)localObject);
/*     */       } catch (ClassNotFound localClassNotFound) {
/* 106 */         classNotFound(paramContextStack, localClassNotFound);
/* 107 */         return null;
/*     */       }
/*     */ 
/* 110 */       paramClassDefinition = localClassDefinition;
/*     */     }
/*     */ 
/* 115 */     if (couldBeValue(paramContextStack, paramClassDefinition))
/*     */     {
/* 119 */       localObject = new ValueType(paramClassDefinition, paramContextStack, bool);
/* 120 */       putType(str, (Type)localObject, paramContextStack);
/* 121 */       paramContextStack.push((ContextElement)localObject);
/*     */ 
/* 123 */       if (((ValueType)localObject).initialize(paramContextStack, paramBoolean)) {
/* 124 */         paramContextStack.pop(true);
/* 125 */         return localObject;
/*     */       }
/* 127 */       removeType(str, paramContextStack);
/* 128 */       paramContextStack.pop(false);
/* 129 */       return null;
/*     */     }
/*     */ 
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 141 */     String str = addExceptionDescription("Value");
/* 142 */     if (this.isCustom) {
/* 143 */       str = "Custom " + str;
/*     */     }
/* 145 */     if (this.isIDLEntity) {
/* 146 */       str = str + " [IDLEntity]";
/*     */     }
/* 148 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean isCustom()
/*     */   {
/* 160 */     return this.isCustom;
/*     */   }
/*     */ 
/*     */   private ValueType(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/* 175 */     super(paramContextStack, paramClassDefinition, 100696064);
/* 176 */     this.isCustom = false;
/*     */ 
/* 181 */     if (paramBoolean)
/* 182 */       setNames(idJavaLangClass, IDL_CLASS_MODULE, "ClassDesc");
/*     */   }
/*     */ 
/*     */   private static boolean couldBeValue(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 196 */     boolean bool = false;
/* 197 */     ClassDeclaration localClassDeclaration = paramClassDefinition.getClassDeclaration();
/* 198 */     BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*     */     try
/*     */     {
/* 203 */       if (localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, localClassDeclaration)) {
/* 204 */         failedConstraint(10, false, paramContextStack, paramClassDefinition.getName());
/*     */       }
/* 209 */       else if (!localBatchEnvironment.defSerializable.implementedBy(localBatchEnvironment, localClassDeclaration))
/* 210 */         failedConstraint(11, false, paramContextStack, paramClassDefinition.getName());
/*     */       else
/* 212 */         bool = true;
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound)
/*     */     {
/* 216 */       classNotFound(paramContextStack, localClassNotFound);
/*     */     }
/*     */ 
/* 219 */     return bool;
/*     */   }
/*     */ 
/*     */   private boolean initialize(ContextStack paramContextStack, boolean paramBoolean)
/*     */   {
/* 227 */     ClassDefinition localClassDefinition = getClassDefinition();
/* 228 */     ClassDeclaration localClassDeclaration = getClassDeclaration();
/*     */     try
/*     */     {
/* 234 */       if (!initParents(paramContextStack)) {
/* 235 */         failedConstraint(12, paramBoolean, paramContextStack, getQualifiedName());
/* 236 */         return false;
/*     */       }
/*     */ 
/* 242 */       Vector localVector1 = new Vector();
/* 243 */       Vector localVector2 = new Vector();
/* 244 */       Vector localVector3 = new Vector();
/*     */ 
/* 248 */       if (addNonRemoteInterfaces(localVector1, paramContextStack) != null)
/*     */       {
/* 252 */         if (addAllMethods(localClassDefinition, localVector2, false, false, paramContextStack) != null)
/*     */         {
/* 255 */           if (updateParentClassMethods(localClassDefinition, localVector2, false, paramContextStack) != null)
/*     */           {
/* 259 */             if (addAllMembers(localVector3, false, false, paramContextStack))
/*     */             {
/* 263 */               if (!initialize(localVector1, localVector2, localVector3, paramContextStack, paramBoolean)) {
/* 264 */                 return false;
/*     */               }
/*     */ 
/* 269 */               int i = 0;
/* 270 */               if (!this.env.defExternalizable.implementedBy(this.env, localClassDeclaration))
/*     */               {
/* 275 */                 if (!checkPersistentFields(getClassInstance(), paramBoolean)) {
/* 276 */                   return false;
/*     */                 }
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/* 282 */                 i = 1;
/*     */               }
/*     */ 
/* 292 */               if (i != 0)
/* 293 */                 this.isCustom = true;
/*     */               else {
/* 295 */                 for (MemberDefinition localMemberDefinition = localClassDefinition.getFirstMember(); 
/* 296 */                   localMemberDefinition != null; 
/* 297 */                   localMemberDefinition = localMemberDefinition.getNextMember())
/*     */                 {
/* 299 */                   if ((localMemberDefinition.isMethod()) && 
/* 300 */                     (!localMemberDefinition
/* 300 */                     .isInitializer()) && 
/* 301 */                     (localMemberDefinition
/* 301 */                     .isPrivate()) && 
/* 302 */                     (localMemberDefinition
/* 302 */                     .getName().toString().equals("writeObject")))
/*     */                   {
/* 306 */                     sun.tools.java.Type localType1 = localMemberDefinition.getType();
/* 307 */                     sun.tools.java.Type localType2 = localType1.getReturnType();
/*     */ 
/* 309 */                     if (localType2 == sun.tools.java.Type.tVoid)
/*     */                     {
/* 313 */                       sun.tools.java.Type[] arrayOfType = localType1.getArgumentTypes();
/* 314 */                       if ((arrayOfType.length == 1) && 
/* 315 */                         (arrayOfType[0]
/* 315 */                         .getTypeSignature().equals("Ljava/io/ObjectOutputStream;")))
/*     */                       {
/* 320 */                         this.isCustom = true;
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/* 328 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 333 */       classNotFound(paramContextStack, localClassNotFound);
/*     */     }
/*     */ 
/* 336 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean checkPersistentFields(Class paramClass, boolean paramBoolean)
/*     */   {
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 344 */     for (int i = 0; i < this.methods.length; i++) {
/* 345 */       if ((this.methods[i].getName().equals("writeObject")) && 
/* 346 */         (this.methods[i]
/* 346 */         .getArguments().length == 1))
/*     */       {
/* 348 */         Type localType = this.methods[i].getReturnType();
/* 349 */         localObject1 = this.methods[i].getArguments()[0];
/* 350 */         localObject2 = ((Type)localObject1).getQualifiedName();
/*     */ 
/* 352 */         if ((localType.isType(1)) && 
/* 353 */           (((String)localObject2)
/* 353 */           .equals("java.io.ObjectOutputStream")))
/*     */         {
/* 357 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 364 */     MemberDefinition localMemberDefinition = null;
/*     */     Object localObject3;
/* 366 */     for (int j = 0; j < this.members.length; j++) {
/* 367 */       if (this.members[j].getName().equals("serialPersistentFields"))
/*     */       {
/* 369 */         localObject1 = this.members[j];
/* 370 */         localObject2 = ((CompoundType.Member)localObject1).getType();
/* 371 */         localObject3 = ((Type)localObject2).getElementType();
/*     */ 
/* 376 */         if ((localObject3 != null) && 
/* 377 */           (((Type)localObject3)
/* 377 */           .getQualifiedName().equals("java.io.ObjectStreamField")))
/*     */         {
/* 381 */           if ((((CompoundType.Member)localObject1).isStatic()) && 
/* 382 */             (((CompoundType.Member)localObject1)
/* 382 */             .isFinal()) && 
/* 383 */             (((CompoundType.Member)localObject1)
/* 383 */             .isPrivate()))
/*     */           {
/* 387 */             localMemberDefinition = ((CompoundType.Member)localObject1).getMemberDefinition();
/*     */           }
/*     */           else
/*     */           {
/* 393 */             failedConstraint(4, paramBoolean, this.stack, getQualifiedName());
/* 394 */             return false;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 403 */     if (localMemberDefinition == null) {
/* 404 */       return true;
/*     */     }
/*     */ 
/* 410 */     Hashtable localHashtable = getPersistentFields(paramClass);
/* 411 */     boolean bool = true;
/*     */ 
/* 413 */     for (int k = 0; k < this.members.length; k++) {
/* 414 */       localObject3 = this.members[k].getName();
/* 415 */       String str1 = this.members[k].getType().getSignature();
/*     */ 
/* 419 */       String str2 = (String)localHashtable.get(localObject3);
/*     */ 
/* 421 */       if (str2 == null)
/*     */       {
/* 425 */         this.members[k].setTransient();
/*     */       }
/* 431 */       else if (str2.equals(str1))
/*     */       {
/* 435 */         localHashtable.remove(localObject3);
/*     */       }
/*     */       else
/*     */       {
/* 441 */         bool = false;
/* 442 */         failedConstraint(2, paramBoolean, this.stack, localObject3, getQualifiedName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 450 */     if ((bool) && (localHashtable.size() > 0))
/*     */     {
/* 452 */       bool = false;
/* 453 */       failedConstraint(9, paramBoolean, this.stack, getQualifiedName());
/*     */     }
/*     */ 
/* 458 */     return bool;
/*     */   }
/*     */ 
/*     */   private Hashtable getPersistentFields(Class paramClass)
/*     */   {
/* 465 */     Hashtable localHashtable = new Hashtable();
/* 466 */     ObjectStreamClass localObjectStreamClass = ObjectStreamClass.lookup(paramClass);
/* 467 */     if (localObjectStreamClass != null) {
/* 468 */       ObjectStreamField[] arrayOfObjectStreamField = localObjectStreamClass.getFields();
/* 469 */       for (int i = 0; i < arrayOfObjectStreamField.length; i++)
/*     */       {
/* 471 */         String str2 = String.valueOf(arrayOfObjectStreamField[i].getTypeCode());
/*     */         String str1;
/* 472 */         if (arrayOfObjectStreamField[i].isPrimitive()) {
/* 473 */           str1 = str2;
/*     */         } else {
/* 475 */           if (arrayOfObjectStreamField[i].getTypeCode() == '[') {
/* 476 */             str2 = "";
/*     */           }
/* 478 */           str1 = str2 + arrayOfObjectStreamField[i].getType().getName().replace('.', '/');
/* 479 */           if (str1.endsWith(";")) {
/* 480 */             str1 = str1.substring(0, str1.length() - 1);
/*     */           }
/*     */         }
/* 483 */         localHashtable.put(arrayOfObjectStreamField[i].getName(), str1);
/*     */       }
/*     */     }
/* 486 */     return localHashtable;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.ValueType
 * JD-Core Version:    0.6.2
 */