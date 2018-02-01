/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class RemoteClass
/*     */   implements RMIConstants
/*     */ {
/*     */   private BatchEnvironment env;
/*     */   private ClassDefinition implClassDef;
/*     */   private ClassDefinition[] remoteInterfaces;
/*     */   private Method[] remoteMethods;
/*     */   private long interfaceHash;
/*     */   private ClassDefinition defRemote;
/*     */   private ClassDefinition defException;
/*     */   private ClassDefinition defRemoteException;
/*     */ 
/*     */   public static RemoteClass forClass(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition)
/*     */   {
/*  69 */     RemoteClass localRemoteClass = new RemoteClass(paramBatchEnvironment, paramClassDefinition);
/*  70 */     if (localRemoteClass.initialize()) {
/*  71 */       return localRemoteClass;
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public ClassDefinition getClassDefinition()
/*     */   {
/*  81 */     return this.implClassDef;
/*     */   }
/*     */ 
/*     */   public Identifier getName()
/*     */   {
/*  88 */     return this.implClassDef.getName();
/*     */   }
/*     */ 
/*     */   public ClassDefinition[] getRemoteInterfaces()
/*     */   {
/* 106 */     return (ClassDefinition[])this.remoteInterfaces.clone();
/*     */   }
/*     */ 
/*     */   public Method[] getRemoteMethods()
/*     */   {
/* 121 */     return (Method[])this.remoteMethods.clone();
/*     */   }
/*     */ 
/*     */   public long getInterfaceHash()
/*     */   {
/* 129 */     return this.interfaceHash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     return "remote class " + this.implClassDef.getName().toString();
/*     */   }
/*     */ 
/*     */   private RemoteClass(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition)
/*     */   {
/* 165 */     this.env = paramBatchEnvironment;
/* 166 */     this.implClassDef = paramClassDefinition;
/*     */   }
/*     */ 
/*     */   private boolean initialize()
/*     */   {
/* 177 */     if (this.implClassDef.isInterface()) {
/* 178 */       this.env.error(0L, "rmic.cant.make.stubs.for.interface", this.implClassDef
/* 179 */         .getName());
/* 180 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 188 */       this.defRemote = this.env
/* 189 */         .getClassDeclaration(idRemote)
/* 189 */         .getClassDefinition(this.env);
/* 190 */       this.defException = this.env
/* 191 */         .getClassDeclaration(idJavaLangException)
/* 192 */         .getClassDefinition(this.env);
/*     */ 
/* 193 */       this.defRemoteException = this.env
/* 194 */         .getClassDeclaration(idRemoteException)
/* 195 */         .getClassDefinition(this.env);
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound1) {
/* 197 */       this.env.error(0L, "rmic.class.not.found", localClassNotFound1.name);
/* 198 */       return false;
/*     */     }
/*     */ 
/* 207 */     Vector localVector = new Vector();
/*     */ 
/* 209 */     Object localObject1 = this.implClassDef;
/*     */     ClassDefinition localClassDefinition;
/* 210 */     while (localObject1 != null) {
/*     */       try
/*     */       {
/* 213 */         ClassDeclaration[] arrayOfClassDeclaration = ((ClassDefinition)localObject1).getInterfaces();
/* 214 */         for (int j = 0; j < arrayOfClassDeclaration.length; j++)
/*     */         {
/* 216 */           localClassDefinition = arrayOfClassDeclaration[j]
/* 216 */             .getClassDefinition(this.env);
/*     */ 
/* 221 */           if ((!localVector.contains(localClassDefinition)) && 
/* 222 */             (this.defRemote
/* 222 */             .implementedBy(this.env, arrayOfClassDeclaration[j])))
/*     */           {
/* 224 */             localVector.addElement(localClassDefinition);
/*     */ 
/* 226 */             if (this.env.verbose()) {
/* 227 */               System.out.println("[found remote interface: " + localClassDefinition
/* 228 */                 .getName() + "]");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 238 */         if ((localObject1 == this.implClassDef) && (localVector.isEmpty())) {
/* 239 */           if (this.defRemote.implementedBy(this.env, this.implClassDef
/* 240 */             .getClassDeclaration()))
/*     */           {
/* 247 */             this.env.error(0L, "rmic.must.implement.remote.directly", this.implClassDef
/* 248 */               .getName());
/*     */           }
/*     */           else
/*     */           {
/* 254 */             this.env.error(0L, "rmic.must.implement.remote", this.implClassDef
/* 255 */               .getName());
/*     */           }
/* 257 */           return false;
/*     */         }
/*     */ 
/* 264 */         localObject1 = ((ClassDefinition)localObject1).getSuperClass() != null ? ((ClassDefinition)localObject1)
/* 264 */           .getSuperClass().getClassDefinition(this.env) : null;
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound2)
/*     */       {
/* 268 */         this.env.error(0L, "class.not.found", localClassNotFound2.name, ((ClassDefinition)localObject1).getName());
/* 269 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 310 */     localObject1 = new Hashtable();
/* 311 */     int i = 0;
/*     */ 
/* 313 */     Object localObject2 = localVector
/* 313 */       .elements();
/* 314 */     while (((Enumeration)localObject2).hasMoreElements())
/*     */     {
/* 316 */       localClassDefinition = (ClassDefinition)((Enumeration)localObject2).nextElement();
/* 317 */       if (!collectRemoteMethods(localClassDefinition, (Hashtable)localObject1))
/* 318 */         i = 1;
/*     */     }
/* 320 */     if (i != 0) {
/* 321 */       return false;
/*     */     }
/*     */ 
/* 327 */     this.remoteInterfaces = new ClassDefinition[localVector.size()];
/* 328 */     localVector.copyInto(this.remoteInterfaces);
/*     */ 
/* 337 */     localObject2 = new String[((Hashtable)localObject1).size()];
/* 338 */     int k = 0;
/* 339 */     Enumeration localEnumeration = ((Hashtable)localObject1).elements();
/*     */     Object localObject3;
/* 340 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 342 */       localObject3 = (Method)localEnumeration.nextElement();
/* 343 */       String str = ((Method)localObject3).getNameAndDescriptor();
/*     */ 
/* 345 */       for (int i1 = k; (i1 > 0) && 
/* 346 */         (str.compareTo(localObject2[(i1 - 1)]) < 0); i1--)
/*     */       {
/* 349 */         localObject2[i1] = localObject2[(i1 - 1)];
/*     */       }
/* 351 */       localObject2[i1] = str;
/* 352 */       k++;
/*     */     }
/* 354 */     this.remoteMethods = new Method[((Hashtable)localObject1).size()];
/* 355 */     for (int m = 0; m < this.remoteMethods.length; m++) {
/* 356 */       this.remoteMethods[m] = ((Method)((Hashtable)localObject1).get(localObject2[m]));
/*     */ 
/* 358 */       if (this.env.verbose()) {
/* 359 */         System.out.print("[found remote method <" + m + ">: " + this.remoteMethods[m]
/* 360 */           .getOperationString());
/*     */ 
/* 362 */         localObject3 = this.remoteMethods[m]
/* 362 */           .getExceptions();
/* 363 */         if (localObject3.length > 0)
/* 364 */           System.out.print(" throws ");
/* 365 */         for (int n = 0; n < localObject3.length; n++) {
/* 366 */           if (n > 0)
/* 367 */             System.out.print(", ");
/* 368 */           System.out.print(localObject3[n].getName());
/*     */         }
/* 370 */         System.out.println("]");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 379 */     this.interfaceHash = computeInterfaceHash();
/*     */ 
/* 381 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean collectRemoteMethods(ClassDefinition paramClassDefinition, Hashtable<String, Method> paramHashtable)
/*     */   {
/* 393 */     if (!paramClassDefinition.isInterface())
/*     */     {
/* 395 */       throw new Error("expected interface, not class: " + paramClassDefinition
/* 395 */         .getName());
/*     */     }
/*     */ 
/* 422 */     int i = 0;
/*     */ 
/* 428 */     label382: for (Object localObject = paramClassDefinition.getFirstMember(); 
/* 429 */       localObject != null; 
/* 430 */       localObject = ((MemberDefinition)localObject).getNextMember())
/*     */     {
/* 432 */       if ((((MemberDefinition)localObject).isMethod()) && 
/* 433 */         (!((MemberDefinition)localObject)
/* 433 */         .isConstructor()) && (!((MemberDefinition)localObject).isInitializer()))
/*     */       {
/* 438 */         ClassDeclaration[] arrayOfClassDeclaration = ((MemberDefinition)localObject).getExceptions(this.env);
/* 439 */         int k = 0;
/* 440 */         for (int m = 0; m < arrayOfClassDeclaration.length; m++)
/*     */         {
/*     */           try
/*     */           {
/* 459 */             if (this.defRemoteException.subClassOf(this.env, arrayOfClassDeclaration[m]))
/*     */             {
/* 462 */               k = 1;
/* 463 */               break;
/*     */             }
/*     */           } catch (ClassNotFound localClassNotFound3) {
/* 466 */             this.env.error(0L, "class.not.found", localClassNotFound3.name, paramClassDefinition
/* 467 */               .getName());
/* 468 */             break label382;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 476 */         if (k == 0) {
/* 477 */           this.env.error(0L, "rmic.must.throw.remoteexception", paramClassDefinition
/* 478 */             .getName(), ((MemberDefinition)localObject).toString());
/* 479 */           i = 1;
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 490 */             MemberDefinition localMemberDefinition = this.implClassDef.findMethod(this.env, ((MemberDefinition)localObject)
/* 491 */               .getName(), ((MemberDefinition)localObject).getType());
/* 492 */             if (localMemberDefinition != null) {
/* 493 */               arrayOfClassDeclaration = localMemberDefinition.getExceptions(this.env);
/* 494 */               for (int n = 0; n < arrayOfClassDeclaration.length; n++)
/* 495 */                 if (!this.defException.superClassOf(this.env, arrayOfClassDeclaration[n]))
/*     */                 {
/* 498 */                   this.env.error(0L, "rmic.must.only.throw.exception", localMemberDefinition
/* 499 */                     .toString(), arrayOfClassDeclaration[n]
/* 500 */                     .getName());
/* 501 */                   i = 1;
/* 502 */                   break label382;
/*     */                 }
/*     */             }
/*     */           }
/*     */           catch (ClassNotFound localClassNotFound2) {
/* 507 */             this.env.error(0L, "class.not.found", localClassNotFound2.name, this.implClassDef
/* 508 */               .getName());
/* 509 */             continue;
/*     */           }
/*     */ 
/* 516 */           Method localMethod1 = new Method((MemberDefinition)localObject);
/*     */ 
/* 531 */           String str = localMethod1.getNameAndDescriptor();
/* 532 */           Method localMethod2 = (Method)paramHashtable.get(str);
/* 533 */           if (localMethod2 != null) {
/* 534 */             localMethod1 = localMethod1.mergeWith(localMethod2);
/* 535 */             if (localMethod1 == null) {
/* 536 */               i = 1;
/* 537 */               continue;
/*     */             }
/*     */           }
/* 540 */           paramHashtable.put(str, localMethod1);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 548 */       localObject = paramClassDefinition.getInterfaces();
/* 549 */       for (int j = 0; j < localObject.length; j++)
/*     */       {
/* 551 */         ClassDefinition localClassDefinition = localObject[j]
/* 551 */           .getClassDefinition(this.env);
/*     */ 
/* 552 */         if (!collectRemoteMethods(localClassDefinition, paramHashtable))
/* 553 */           i = 1;
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound1) {
/* 556 */       this.env.error(0L, "class.not.found", localClassNotFound1.name, paramClassDefinition.getName());
/* 557 */       return false;
/*     */     }
/*     */ 
/* 560 */     return i == 0;
/*     */   }
/*     */ 
/*     */   private long computeInterfaceHash()
/*     */   {
/* 580 */     long l = 0L;
/* 581 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */     try {
/* 583 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 584 */       DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(localByteArrayOutputStream, localMessageDigest));
/*     */ 
/* 587 */       localDataOutputStream.writeInt(1);
/* 588 */       for (int i = 0; i < this.remoteMethods.length; i++) {
/* 589 */         MemberDefinition localMemberDefinition = this.remoteMethods[i].getMemberDefinition();
/* 590 */         Identifier localIdentifier = localMemberDefinition.getName();
/* 591 */         Type localType = localMemberDefinition.getType();
/*     */ 
/* 593 */         localDataOutputStream.writeUTF(localIdentifier.toString());
/*     */ 
/* 595 */         localDataOutputStream.writeUTF(localType.getTypeSignature());
/*     */ 
/* 597 */         ClassDeclaration[] arrayOfClassDeclaration = localMemberDefinition.getExceptions(this.env);
/* 598 */         sortClassDeclarations(arrayOfClassDeclaration);
/* 599 */         for (int k = 0; k < arrayOfClassDeclaration.length; k++) {
/* 600 */           localDataOutputStream.writeUTF(Names.mangleClass(arrayOfClassDeclaration[k]
/* 601 */             .getName()).toString());
/*     */         }
/*     */       }
/* 604 */       localDataOutputStream.flush();
/*     */ 
/* 607 */       byte[] arrayOfByte = localMessageDigest.digest();
/* 608 */       for (int j = 0; j < Math.min(8, arrayOfByte.length); j++)
/* 609 */         l += ((arrayOfByte[j] & 0xFF) << j * 8);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 612 */       throw new Error("unexpected exception computing intetrface hash: " + localIOException);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 615 */       throw new Error("unexpected exception computing intetrface hash: " + localNoSuchAlgorithmException);
/*     */     }
/*     */ 
/* 619 */     return l;
/*     */   }
/*     */ 
/*     */   private void sortClassDeclarations(ClassDeclaration[] paramArrayOfClassDeclaration)
/*     */   {
/* 629 */     for (int i = 1; i < paramArrayOfClassDeclaration.length; i++) {
/* 630 */       ClassDeclaration localClassDeclaration = paramArrayOfClassDeclaration[i];
/* 631 */       String str = Names.mangleClass(localClassDeclaration.getName()).toString();
/*     */ 
/* 633 */       for (int j = i; (j > 0) && 
/* 634 */         (str.compareTo(
/* 635 */         Names.mangleClass(paramArrayOfClassDeclaration[(j - 1)]
/* 635 */         .getName()).toString()) < 0); j--)
/*     */       {
/* 639 */         paramArrayOfClassDeclaration[j] = paramArrayOfClassDeclaration[(j - 1)];
/*     */       }
/* 641 */       paramArrayOfClassDeclaration[j] = localClassDeclaration;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class Method
/*     */     implements Cloneable
/*     */   {
/*     */     private MemberDefinition memberDef;
/*     */     private long methodHash;
/*     */     private ClassDeclaration[] exceptions;
/*     */ 
/*     */     public MemberDefinition getMemberDefinition()
/*     */     {
/* 660 */       return this.memberDef;
/*     */     }
/*     */ 
/*     */     public Identifier getName()
/*     */     {
/* 667 */       return this.memberDef.getName();
/*     */     }
/*     */ 
/*     */     public Type getType()
/*     */     {
/* 674 */       return this.memberDef.getType();
/*     */     }
/*     */ 
/*     */     public ClassDeclaration[] getExceptions()
/*     */     {
/* 687 */       return (ClassDeclaration[])this.exceptions.clone();
/*     */     }
/*     */ 
/*     */     public long getMethodHash()
/*     */     {
/* 695 */       return this.methodHash;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 702 */       return this.memberDef.toString();
/*     */     }
/*     */ 
/*     */     public String getOperationString()
/*     */     {
/* 710 */       return this.memberDef.toString();
/*     */     }
/*     */ 
/*     */     public String getNameAndDescriptor()
/*     */     {
/* 721 */       return this.memberDef.getName().toString() + this.memberDef
/* 721 */         .getType().getTypeSignature();
/*     */     }
/*     */ 
/*     */     Method(MemberDefinition arg2)
/*     */     {
/*     */       Object localObject;
/* 758 */       this.memberDef = localObject;
/* 759 */       this.exceptions = localObject.getExceptions(RemoteClass.this.env);
/* 760 */       this.methodHash = computeMethodHash();
/*     */     }
/*     */ 
/*     */     protected Object clone()
/*     */     {
/*     */       try
/*     */       {
/* 768 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 770 */       throw new Error("clone failed");
/*     */     }
/*     */ 
/*     */     private Method mergeWith(Method paramMethod)
/*     */     {
/* 784 */       if ((!getName().equals(paramMethod.getName())) || 
/* 785 */         (!getType().equals(paramMethod.getType())))
/*     */       {
/* 789 */         throw new Error("attempt to merge method \"" + paramMethod
/* 788 */           .getNameAndDescriptor() + "\" with \"" + 
/* 789 */           getNameAndDescriptor());
/*     */       }
/*     */ 
/* 792 */       Vector localVector = new Vector();
/*     */       try
/*     */       {
/* 795 */         collectCompatibleExceptions(paramMethod.exceptions, this.exceptions, localVector);
/*     */ 
/* 797 */         collectCompatibleExceptions(this.exceptions, paramMethod.exceptions, localVector);
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound) {
/* 800 */         RemoteClass.this.env.error(0L, "class.not.found", localClassNotFound.name, RemoteClass.this
/* 801 */           .getClassDefinition().getName());
/* 802 */         return null;
/*     */       }
/*     */ 
/* 805 */       Method localMethod = (Method)clone();
/* 806 */       localMethod.exceptions = new ClassDeclaration[localVector.size()];
/* 807 */       localVector.copyInto(localMethod.exceptions);
/*     */ 
/* 809 */       return localMethod;
/*     */     }
/*     */ 
/*     */     private void collectCompatibleExceptions(ClassDeclaration[] paramArrayOfClassDeclaration1, ClassDeclaration[] paramArrayOfClassDeclaration2, Vector<ClassDeclaration> paramVector)
/*     */       throws ClassNotFound
/*     */     {
/* 821 */       for (int i = 0; i < paramArrayOfClassDeclaration1.length; i++) {
/* 822 */         ClassDefinition localClassDefinition = paramArrayOfClassDeclaration1[i].getClassDefinition(RemoteClass.this.env);
/* 823 */         if (!paramVector.contains(paramArrayOfClassDeclaration1[i]))
/* 824 */           for (int j = 0; j < paramArrayOfClassDeclaration2.length; j++)
/* 825 */             if (localClassDefinition.subClassOf(RemoteClass.this.env, paramArrayOfClassDeclaration2[j])) {
/* 826 */               paramVector.addElement(paramArrayOfClassDeclaration1[i]);
/* 827 */               break;
/*     */             }
/*     */       }
/*     */     }
/*     */ 
/*     */     private long computeMethodHash()
/*     */     {
/* 843 */       long l = 0L;
/* 844 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */       try {
/* 846 */         MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 847 */         DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(localByteArrayOutputStream, localMessageDigest));
/*     */ 
/* 850 */         String str = getNameAndDescriptor();
/*     */ 
/* 852 */         if (RemoteClass.this.env.verbose()) {
/* 853 */           System.out.println("[string used for method hash: \"" + str + "\"]");
/*     */         }
/*     */ 
/* 857 */         localDataOutputStream.writeUTF(str);
/*     */ 
/* 860 */         localDataOutputStream.flush();
/* 861 */         byte[] arrayOfByte = localMessageDigest.digest();
/* 862 */         for (int i = 0; i < Math.min(8, arrayOfByte.length); i++)
/* 863 */           l += ((arrayOfByte[i] & 0xFF) << i * 8);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 866 */         throw new Error("unexpected exception computing intetrface hash: " + localIOException);
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 869 */         throw new Error("unexpected exception computing intetrface hash: " + localNoSuchAlgorithmException);
/*     */       }
/*     */ 
/* 873 */       return l;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.RemoteClass
 * JD-Core Version:    0.6.2
 */