/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.rmi.rmic.newrmic.BatchEnvironment;
/*     */ 
/*     */ final class RemoteClass
/*     */ {
/*     */   private final BatchEnvironment env;
/*     */   private final ClassDoc implClass;
/*     */   private ClassDoc[] remoteInterfaces;
/*     */   private Method[] remoteMethods;
/*     */   private long interfaceHash;
/*     */ 
/*     */   static RemoteClass forClass(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc)
/*     */   {
/*  86 */     RemoteClass localRemoteClass = new RemoteClass(paramBatchEnvironment, paramClassDoc);
/*  87 */     if (localRemoteClass.init()) {
/*  88 */       return localRemoteClass;
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   private RemoteClass(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc)
/*     */   {
/*  99 */     this.env = paramBatchEnvironment;
/* 100 */     this.implClass = paramClassDoc;
/*     */   }
/*     */ 
/*     */   ClassDoc classDoc()
/*     */   {
/* 107 */     return this.implClass;
/*     */   }
/*     */ 
/*     */   ClassDoc[] remoteInterfaces()
/*     */   {
/* 124 */     return (ClassDoc[])this.remoteInterfaces.clone();
/*     */   }
/*     */ 
/*     */   Method[] remoteMethods()
/*     */   {
/* 139 */     return (Method[])this.remoteMethods.clone();
/*     */   }
/*     */ 
/*     */   long interfaceHash()
/*     */   {
/* 148 */     return this.interfaceHash;
/*     */   }
/*     */ 
/*     */   private boolean init()
/*     */   {
/* 160 */     if (this.implClass.isInterface()) {
/* 161 */       this.env.error("rmic.cant.make.stubs.for.interface", new String[] { this.implClass
/* 162 */         .qualifiedName() });
/* 163 */       return false;
/*     */     }
/*     */ 
/* 172 */     ArrayList localArrayList = new ArrayList();
/*     */     Object localObject3;
/* 173 */     for (Object localObject1 = this.implClass; localObject1 != null; localObject1 = ((ClassDoc)localObject1).superclass()) {
/* 174 */       for (localObject3 : ((ClassDoc)localObject1).interfaces())
/*     */       {
/* 179 */         if ((!localArrayList.contains(localObject3)) && 
/* 180 */           (((ClassDoc)localObject3)
/* 180 */           .subclassOf(this.env
/* 180 */           .docRemote())))
/*     */         {
/* 182 */           localArrayList.add(localObject3);
/* 183 */           if (this.env.verbose()) {
/* 184 */             this.env.output("[found remote interface: " + ((ClassDoc)localObject3)
/* 185 */               .qualifiedName() + "]");
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 194 */       if ((localObject1 == this.implClass) && (localArrayList.isEmpty())) {
/* 195 */         if (this.implClass.subclassOf(this.env.docRemote()))
/*     */         {
/* 201 */           this.env.error("rmic.must.implement.remote.directly", new String[] { this.implClass
/* 202 */             .qualifiedName() });
/*     */         }
/*     */         else
/*     */         {
/* 208 */           this.env.error("rmic.must.implement.remote", new String[] { this.implClass
/* 209 */             .qualifiedName() });
/*     */         }
/* 211 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 219 */     this.remoteInterfaces = 
/* 220 */       ((ClassDoc[])localArrayList
/* 220 */       .toArray(
/* 221 */       new ClassDoc[localArrayList
/* 221 */       .size()]));
/*     */ 
/* 228 */     localObject1 = new HashMap();
/* 229 */     int i = 0;
/* 230 */     for (Object localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) { ClassDoc localClassDoc = (ClassDoc)((Iterator)localObject2).next();
/* 231 */       if (!collectRemoteMethods(localClassDoc, (Map)localObject1))
/*     */       {
/* 236 */         i = 1;
/*     */       }
/*     */     }
/* 239 */     if (i != 0) {
/* 240 */       return false;
/*     */     }
/*     */ 
/* 251 */     localObject2 = (String[])((Map)localObject1)
/* 251 */       .keySet().toArray(new String[((Map)localObject1).size()]);
/* 252 */     Arrays.sort((Object[])localObject2);
/* 253 */     this.remoteMethods = new Method[((Map)localObject1).size()];
/* 254 */     for (int m = 0; m < this.remoteMethods.length; m++) {
/* 255 */       this.remoteMethods[m] = ((Method)((Map)localObject1).get(localObject2[m]));
/* 256 */       if (this.env.verbose())
/*     */       {
/* 258 */         localObject3 = "[found remote method <" + m + ">: " + this.remoteMethods[m]
/* 258 */           .operationString();
/* 259 */         ClassDoc[] arrayOfClassDoc2 = this.remoteMethods[m].exceptionTypes();
/* 260 */         if (arrayOfClassDoc2.length > 0) {
/* 261 */           localObject3 = (String)localObject3 + " throws ";
/* 262 */           for (int n = 0; n < arrayOfClassDoc2.length; n++) {
/* 263 */             if (n > 0) {
/* 264 */               localObject3 = (String)localObject3 + ", ";
/*     */             }
/* 266 */             localObject3 = (String)localObject3 + arrayOfClassDoc2[n].qualifiedName();
/*     */           }
/*     */         }
/*     */ 
/* 270 */         localObject3 = (String)localObject3 + "\n\tname and descriptor = \"" + this.remoteMethods[m]
/* 270 */           .nameAndDescriptor();
/*     */ 
/* 272 */         localObject3 = (String)localObject3 + "\n\tmethod hash = " + this.remoteMethods[m]
/* 272 */           .methodHash() + "]";
/* 273 */         this.env.output((String)localObject3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 282 */     this.interfaceHash = computeInterfaceHash();
/*     */ 
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean collectRemoteMethods(ClassDoc paramClassDoc, Map<String, Method> paramMap)
/*     */   {
/* 296 */     if (!paramClassDoc.isInterface())
/*     */     {
/* 298 */       throw new AssertionError(paramClassDoc
/* 298 */         .qualifiedName() + " not an interface");
/*     */     }
/*     */ 
/* 301 */     int i = 0;
/*     */     MethodDoc localMethodDoc;
/* 307 */     label386: for (localMethodDoc : paramClassDoc.methods())
/*     */     {
/* 313 */       int m = 0;
/* 314 */       for (ClassDoc localClassDoc : localMethodDoc.thrownExceptions()) {
/* 315 */         if (this.env.docRemoteException().subclassOf(localClassDoc)) {
/* 316 */           m = 1;
/* 317 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 326 */       if (m == 0) {
/* 327 */         this.env.error("rmic.must.throw.remoteexception", new String[] { paramClassDoc
/* 328 */           .qualifiedName(), localMethodDoc
/* 329 */           .name() + localMethodDoc.signature() });
/* 330 */         i = 1;
/*     */       }
/*     */       else
/*     */       {
/* 340 */         ??? = findImplMethod(localMethodDoc);
/* 341 */         if (??? != null) {
/* 342 */           for (Object localObject4 : ((MethodDoc)???).thrownExceptions()) {
/* 343 */             if (!localObject4.subclassOf(this.env.docException())) {
/* 344 */               this.env.error("rmic.must.only.throw.exception", new String[] { ((MethodDoc)???)
/* 345 */                 .name() + ((MethodDoc)???).signature(), localObject4
/* 346 */                 .qualifiedName() });
/* 347 */               i = 1;
/* 348 */               break label386;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 357 */         ??? = new Method(localMethodDoc);
/*     */ 
/* 372 */         String str = ((Method)???).nameAndDescriptor();
/* 373 */         Method localMethod = (Method)paramMap.get(str);
/* 374 */         if (localMethod != null) {
/* 375 */           ??? = ((Method)???).mergeWith(localMethod);
/*     */         }
/* 377 */         paramMap.put(str, ???);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 383 */     for (localMethodDoc : paramClassDoc.interfaces()) {
/* 384 */       if (!collectRemoteMethods(localMethodDoc, paramMap)) {
/* 385 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 389 */     return i == 0;
/*     */   }
/*     */ 
/*     */   private MethodDoc findImplMethod(MethodDoc paramMethodDoc)
/*     */   {
/* 399 */     String str1 = paramMethodDoc.name();
/* 400 */     String str2 = Util.methodDescriptorOf(paramMethodDoc);
/* 401 */     for (MethodDoc localMethodDoc : this.implClass.methods()) {
/* 402 */       if ((str1.equals(localMethodDoc.name())) && 
/* 403 */         (str2
/* 403 */         .equals(Util.methodDescriptorOf(localMethodDoc))))
/*     */       {
/* 405 */         return localMethodDoc;
/*     */       }
/*     */     }
/* 408 */     return null;
/*     */   }
/*     */ 
/*     */   private long computeInterfaceHash()
/*     */   {
/* 430 */     long l = 0L;
/* 431 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */     try {
/* 433 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 434 */       DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(localByteArrayOutputStream, localMessageDigest));
/*     */ 
/* 437 */       localDataOutputStream.writeInt(1);
/*     */ 
/* 439 */       for (Object localObject2 : this.remoteMethods) {
/* 440 */         MethodDoc localMethodDoc = localObject2.methodDoc();
/*     */ 
/* 442 */         localDataOutputStream.writeUTF(localMethodDoc.name());
/* 443 */         localDataOutputStream.writeUTF(Util.methodDescriptorOf(localMethodDoc));
/*     */ 
/* 446 */         ClassDoc[] arrayOfClassDoc1 = localMethodDoc.thrownExceptions();
/* 447 */         Arrays.sort(arrayOfClassDoc1, new ClassDocComparator(null));
/* 448 */         for (ClassDoc localClassDoc : arrayOfClassDoc1) {
/* 449 */           localDataOutputStream.writeUTF(Util.binaryNameOf(localClassDoc));
/*     */         }
/*     */       }
/* 452 */       localDataOutputStream.flush();
/*     */ 
/* 455 */       ??? = localMessageDigest.digest();
/* 456 */       for (??? = 0; ??? < Math.min(8, ???.length); ???++)
/* 457 */         l += ((???[???] & 0xFF) << ??? * 8);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 460 */       throw new AssertionError(localIOException);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 462 */       throw new AssertionError(localNoSuchAlgorithmException);
/*     */     }
/*     */ 
/* 465 */     return l;
/*     */   }
/*     */ 
/*     */   private static class ClassDocComparator
/*     */     implements Comparator<ClassDoc>
/*     */   {
/*     */     public int compare(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2)
/*     */     {
/* 474 */       return Util.binaryNameOf(paramClassDoc1).compareTo(Util.binaryNameOf(paramClassDoc2));
/*     */     }
/*     */   }
/*     */ 
/*     */   final class Method
/*     */     implements Cloneable
/*     */   {
/*     */     private final MethodDoc methodDoc;
/*     */     private final String operationString;
/*     */     private final String nameAndDescriptor;
/*     */     private final long methodHash;
/*     */     private ClassDoc[] exceptionTypes;
/*     */ 
/*     */     Method(MethodDoc arg2)
/*     */     {
/*     */       MethodDoc localMethodDoc;
/* 519 */       this.methodDoc = localMethodDoc;
/* 520 */       this.exceptionTypes = localMethodDoc.thrownExceptions();
/*     */ 
/* 525 */       Arrays.sort(this.exceptionTypes, new RemoteClass.ClassDocComparator(null));
/* 526 */       this.operationString = computeOperationString();
/* 527 */       this.nameAndDescriptor = 
/* 528 */         (localMethodDoc
/* 528 */         .name() + Util.methodDescriptorOf(localMethodDoc));
/* 529 */       this.methodHash = computeMethodHash();
/*     */     }
/*     */ 
/*     */     MethodDoc methodDoc()
/*     */     {
/* 537 */       return this.methodDoc;
/*     */     }
/*     */ 
/*     */     Type[] parameterTypes()
/*     */     {
/* 544 */       Parameter[] arrayOfParameter = this.methodDoc.parameters();
/* 545 */       Type[] arrayOfType = new Type[arrayOfParameter.length];
/* 546 */       for (int i = 0; i < arrayOfType.length; i++) {
/* 547 */         arrayOfType[i] = arrayOfParameter[i].type();
/*     */       }
/* 549 */       return arrayOfType;
/*     */     }
/*     */ 
/*     */     ClassDoc[] exceptionTypes()
/*     */     {
/* 562 */       return (ClassDoc[])this.exceptionTypes.clone();
/*     */     }
/*     */ 
/*     */     long methodHash()
/*     */     {
/* 570 */       return this.methodHash;
/*     */     }
/*     */ 
/*     */     String operationString()
/*     */     {
/* 579 */       return this.operationString;
/*     */     }
/*     */ 
/*     */     String nameAndDescriptor()
/*     */     {
/* 587 */       return this.nameAndDescriptor;
/*     */     }
/*     */ 
/*     */     Method mergeWith(Method paramMethod)
/*     */     {
/* 600 */       if (!nameAndDescriptor().equals(paramMethod.nameAndDescriptor()))
/*     */       {
/* 604 */         throw new AssertionError("attempt to merge method \"" + paramMethod
/* 603 */           .nameAndDescriptor() + "\" with \"" + 
/* 604 */           nameAndDescriptor());
/*     */       }
/*     */ 
/* 607 */       ArrayList localArrayList = new ArrayList();
/* 608 */       collectCompatibleExceptions(paramMethod.exceptionTypes, this.exceptionTypes, localArrayList);
/*     */ 
/* 610 */       collectCompatibleExceptions(this.exceptionTypes, paramMethod.exceptionTypes, localArrayList);
/*     */ 
/* 613 */       Method localMethod = clone();
/* 614 */       localMethod.exceptionTypes = 
/* 615 */         ((ClassDoc[])localArrayList
/* 615 */         .toArray(new ClassDoc[localArrayList
/* 615 */         .size()]));
/*     */ 
/* 617 */       return localMethod;
/*     */     }
/*     */ 
/*     */     protected Method clone()
/*     */     {
/*     */       try
/*     */       {
/* 626 */         return (Method)super.clone();
/*     */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 628 */         throw new AssertionError(localCloneNotSupportedException);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void collectCompatibleExceptions(ClassDoc[] paramArrayOfClassDoc1, ClassDoc[] paramArrayOfClassDoc2, List<ClassDoc> paramList)
/*     */     {
/* 641 */       for (ClassDoc localClassDoc1 : paramArrayOfClassDoc1)
/* 642 */         if (!paramList.contains(localClassDoc1))
/* 643 */           for (ClassDoc localClassDoc2 : paramArrayOfClassDoc2)
/* 644 */             if (localClassDoc1.subclassOf(localClassDoc2)) {
/* 645 */               paramList.add(localClassDoc1);
/* 646 */               break;
/*     */             }
/*     */     }
/*     */ 
/*     */     private long computeMethodHash()
/*     */     {
/* 660 */       long l = 0L;
/* 661 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */       try {
/* 663 */         MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 664 */         DataOutputStream localDataOutputStream = new DataOutputStream(new DigestOutputStream(localByteArrayOutputStream, localMessageDigest));
/*     */ 
/* 667 */         String str = nameAndDescriptor();
/* 668 */         localDataOutputStream.writeUTF(str);
/*     */ 
/* 671 */         localDataOutputStream.flush();
/* 672 */         byte[] arrayOfByte = localMessageDigest.digest();
/* 673 */         for (int i = 0; i < Math.min(8, arrayOfByte.length); i++)
/* 674 */           l += ((arrayOfByte[i] & 0xFF) << i * 8);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 677 */         throw new AssertionError(localIOException);
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 679 */         throw new AssertionError(localNoSuchAlgorithmException);
/*     */       }
/*     */ 
/* 682 */       return l;
/*     */     }
/*     */ 
/*     */     private String computeOperationString()
/*     */     {
/* 696 */       Type localType = this.methodDoc.returnType();
/*     */ 
/* 698 */       String str = localType.qualifiedTypeName() + " " + this.methodDoc
/* 698 */         .name() + "(";
/* 699 */       Parameter[] arrayOfParameter = this.methodDoc.parameters();
/* 700 */       for (int i = 0; i < arrayOfParameter.length; i++) {
/* 701 */         if (i > 0) {
/* 702 */           str = str + ", ";
/*     */         }
/* 704 */         str = str + arrayOfParameter[i].type().toString();
/*     */       }
/* 706 */       str = str + ")" + localType.dimension();
/* 707 */       return str;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.jrmp.RemoteClass
 * JD-Core Version:    0.6.2
 */