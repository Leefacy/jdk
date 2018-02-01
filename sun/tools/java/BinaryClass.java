/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import sun.tools.javac.Main;
/*     */ 
/*     */ public final class BinaryClass extends ClassDefinition
/*     */   implements Constants
/*     */ {
/*     */   BinaryConstantPool cpool;
/*     */   BinaryAttribute atts;
/*     */   Vector dependencies;
/*  47 */   private boolean haveLoadedNested = false;
/*     */ 
/*  65 */   private boolean basicCheckDone = false;
/*  66 */   private boolean basicChecking = false;
/*     */ 
/*     */   public BinaryClass(Object paramObject, ClassDeclaration paramClassDeclaration1, int paramInt, ClassDeclaration paramClassDeclaration2, ClassDeclaration[] paramArrayOfClassDeclaration, Vector paramVector)
/*     */   {
/*  55 */     super(paramObject, 0L, paramClassDeclaration1, paramInt, null, null);
/*  56 */     this.dependencies = paramVector;
/*  57 */     this.superClass = paramClassDeclaration2;
/*  58 */     this.interfaces = paramArrayOfClassDeclaration;
/*     */   }
/*     */ 
/*     */   protected void basicCheck(Environment paramEnvironment)
/*     */     throws ClassNotFound
/*     */   {
/*  76 */     paramEnvironment.dtEnter("BinaryClass.basicCheck: " + getName());
/*     */ 
/*  83 */     if ((this.basicChecking) || (this.basicCheckDone)) {
/*  84 */       paramEnvironment.dtExit("BinaryClass.basicCheck: OK " + getName());
/*  85 */       return;
/*     */     }
/*     */ 
/*  88 */     paramEnvironment.dtEvent("BinaryClass.basicCheck: CHECKING " + getName());
/*  89 */     this.basicChecking = true;
/*     */ 
/*  91 */     super.basicCheck(paramEnvironment);
/*     */ 
/*  94 */     if (doInheritanceChecks) {
/*  95 */       collectInheritedMethods(paramEnvironment);
/*     */     }
/*     */ 
/*  98 */     this.basicCheckDone = true;
/*  99 */     this.basicChecking = false;
/* 100 */     paramEnvironment.dtExit("BinaryClass.basicCheck: " + getName());
/*     */   }
/*     */ 
/*     */   public static BinaryClass load(Environment paramEnvironment, DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 107 */     return load(paramEnvironment, paramDataInputStream, -7);
/*     */   }
/*     */ 
/*     */   public static BinaryClass load(Environment paramEnvironment, DataInputStream paramDataInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 113 */     int i = paramDataInputStream.readInt();
/* 114 */     if (i != -889275714) {
/* 115 */       throw new ClassFormatError("wrong magic: " + i + ", expected " + -889275714);
/*     */     }
/* 117 */     int j = paramDataInputStream.readUnsignedShort();
/* 118 */     int k = paramDataInputStream.readUnsignedShort();
/* 119 */     if (k < 45)
/*     */     {
/* 121 */       throw new ClassFormatError(
/* 121 */         Main.getText("javac.err.version.too.old", 
/* 123 */         String.valueOf(k)));
/*     */     }
/*     */ 
/* 124 */     if ((k > 52) || ((k == 52) && (j > 0)))
/*     */     {
/* 128 */       throw new ClassFormatError(
/* 128 */         Main.getText("javac.err.version.too.recent", k + "." + j));
/*     */     }
/*     */ 
/* 134 */     BinaryConstantPool localBinaryConstantPool = new BinaryConstantPool(paramDataInputStream);
/*     */ 
/* 137 */     Vector localVector = localBinaryConstantPool.getDependencies(paramEnvironment);
/*     */ 
/* 140 */     int m = paramDataInputStream.readUnsignedShort() & 0xE31;
/*     */ 
/* 143 */     ClassDeclaration localClassDeclaration1 = localBinaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */ 
/* 146 */     ClassDeclaration localClassDeclaration2 = localBinaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */ 
/* 149 */     ClassDeclaration[] arrayOfClassDeclaration = new ClassDeclaration[paramDataInputStream.readUnsignedShort()];
/* 150 */     for (int n = 0; n < arrayOfClassDeclaration.length; n++)
/*     */     {
/* 152 */       arrayOfClassDeclaration[n] = localBinaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */     }
/*     */ 
/* 156 */     BinaryClass localBinaryClass = new BinaryClass(null, localClassDeclaration1, m, localClassDeclaration2, arrayOfClassDeclaration, localVector);
/*     */ 
/* 158 */     localBinaryClass.cpool = localBinaryConstantPool;
/*     */ 
/* 161 */     localBinaryClass.addDependency(localClassDeclaration2);
/*     */ 
/* 164 */     int i1 = paramDataInputStream.readUnsignedShort();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 165 */     for (int i2 = 0; i2 < i1; i2++)
/*     */     {
/* 167 */       i3 = paramDataInputStream.readUnsignedShort() & 0xDF;
/*     */ 
/* 169 */       Identifier localIdentifier = localBinaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */ 
/* 171 */       localObject1 = localBinaryConstantPool.getType(paramDataInputStream.readUnsignedShort());
/* 172 */       localObject2 = BinaryAttribute.load(paramDataInputStream, localBinaryConstantPool, paramInt);
/* 173 */       localBinaryClass.addMember(new BinaryMember(localBinaryClass, i3, (Type)localObject1, localIdentifier, (BinaryAttribute)localObject2));
/*     */     }
/*     */ 
/* 177 */     i2 = paramDataInputStream.readUnsignedShort();
/* 178 */     for (int i3 = 0; i3 < i2; i3++)
/*     */     {
/* 180 */       int i4 = paramDataInputStream.readUnsignedShort() & 0xD3F;
/*     */ 
/* 182 */       localObject1 = localBinaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */ 
/* 184 */       localObject2 = localBinaryConstantPool.getType(paramDataInputStream.readUnsignedShort());
/* 185 */       BinaryAttribute localBinaryAttribute = BinaryAttribute.load(paramDataInputStream, localBinaryConstantPool, paramInt);
/* 186 */       localBinaryClass.addMember(new BinaryMember(localBinaryClass, i4, (Type)localObject2, (Identifier)localObject1, localBinaryAttribute));
/*     */     }
/*     */ 
/* 190 */     localBinaryClass.atts = BinaryAttribute.load(paramDataInputStream, localBinaryConstantPool, paramInt);
/*     */ 
/* 193 */     byte[] arrayOfByte = localBinaryClass.getAttribute(idSourceFile);
/* 194 */     if (arrayOfByte != null) {
/* 195 */       DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
/*     */ 
/* 197 */       localBinaryClass.source = localBinaryConstantPool.getString(localDataInputStream.readUnsignedShort());
/*     */     }
/*     */ 
/* 201 */     arrayOfByte = localBinaryClass.getAttribute(idDocumentation);
/* 202 */     if (arrayOfByte != null) {
/* 203 */       localBinaryClass.documentation = new DataInputStream(new ByteArrayInputStream(arrayOfByte)).readUTF();
/*     */     }
/*     */ 
/* 207 */     if (localBinaryClass.getAttribute(idDeprecated) != null) {
/* 208 */       localBinaryClass.modifiers |= 262144;
/*     */     }
/*     */ 
/* 212 */     if (localBinaryClass.getAttribute(idSynthetic) != null) {
/* 213 */       localBinaryClass.modifiers |= 524288;
/*     */     }
/*     */ 
/* 216 */     return localBinaryClass;
/*     */   }
/*     */ 
/*     */   public void loadNested(Environment paramEnvironment)
/*     */   {
/* 225 */     loadNested(paramEnvironment, 0);
/*     */   }
/*     */ 
/*     */   public void loadNested(Environment paramEnvironment, int paramInt)
/*     */   {
/* 230 */     if (this.haveLoadedNested)
/*     */     {
/* 234 */       paramEnvironment.dtEvent("loadNested: DUPLICATE CALL SKIPPED");
/* 235 */       return;
/*     */     }
/* 237 */     this.haveLoadedNested = true;
/*     */     try
/*     */     {
/* 241 */       byte[] arrayOfByte = getAttribute(idInnerClasses);
/* 242 */       if (arrayOfByte != null) {
/* 243 */         initInnerClasses(paramEnvironment, arrayOfByte, paramInt);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 249 */       paramEnvironment.error(0L, "malformed.attribute", getClassDeclaration(), idInnerClasses);
/*     */ 
/* 252 */       paramEnvironment.dtEvent("loadNested: MALFORMED ATTRIBUTE (InnerClasses)");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initInnerClasses(Environment paramEnvironment, byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 259 */     DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
/* 260 */     int i = localDataInputStream.readUnsignedShort();
/* 261 */     for (int j = 0; j < i; j++)
/*     */     {
/* 287 */       int k = localDataInputStream.readUnsignedShort();
/*     */ 
/* 289 */       ClassDeclaration localClassDeclaration1 = this.cpool.getDeclaration(paramEnvironment, k);
/*     */ 
/* 293 */       ClassDeclaration localClassDeclaration2 = null;
/*     */ 
/* 295 */       int m = localDataInputStream.readUnsignedShort();
/* 296 */       if (m != 0) {
/* 297 */         localClassDeclaration2 = this.cpool.getDeclaration(paramEnvironment, m);
/*     */       }
/*     */ 
/* 304 */       Identifier localIdentifier1 = idNull;
/*     */ 
/* 306 */       int n = localDataInputStream.readUnsignedShort();
/* 307 */       if (n != 0) {
/* 308 */         localIdentifier1 = Identifier.lookup(this.cpool.getString(n));
/*     */       }
/*     */ 
/* 313 */       int i1 = localDataInputStream.readUnsignedShort();
/*     */ 
/* 338 */       if (localClassDeclaration2 != null);
/* 340 */       int i2 = (!localIdentifier1
/* 340 */         .equals(idNull)) && 
/* 340 */         (((i1 & 0x2) == 0) || ((paramInt & 0x4) != 0)) ? 1 : 0;
/*     */ 
/* 356 */       if (i2 != 0)
/*     */       {
/* 358 */         Identifier localIdentifier2 = Identifier.lookupInner(localClassDeclaration2
/* 358 */           .getName(), localIdentifier1);
/*     */ 
/* 361 */         Type.tClass(localIdentifier2);
/*     */ 
/* 363 */         if (localClassDeclaration1.equals(getClassDeclaration()))
/*     */           try
/*     */           {
/* 366 */             ClassDefinition localClassDefinition1 = localClassDeclaration2.getClassDefinition(paramEnvironment);
/* 367 */             initInner(localClassDefinition1, i1);
/*     */           }
/*     */           catch (ClassNotFound localClassNotFound1) {
/*     */           }
/* 371 */         else if (localClassDeclaration2.equals(getClassDeclaration()))
/*     */         {
/*     */           try
/*     */           {
/* 375 */             ClassDefinition localClassDefinition2 = localClassDeclaration1
/* 375 */               .getClassDefinition(paramEnvironment);
/*     */ 
/* 376 */             initOuter(localClassDefinition2, i1);
/*     */           }
/*     */           catch (ClassNotFound localClassNotFound2) {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initInner(ClassDefinition paramClassDefinition, int paramInt) {
/* 386 */     if (getOuterClass() != null) {
/* 387 */       return;
/*     */     }
/*     */ 
/* 401 */     if ((paramInt & 0x2) != 0)
/*     */     {
/* 403 */       paramInt &= -6;
/* 404 */     } else if ((paramInt & 0x4) != 0)
/*     */     {
/* 406 */       paramInt &= -2;
/*     */     }
/* 408 */     if ((paramInt & 0x200) != 0)
/*     */     {
/* 411 */       paramInt |= 1032;
/*     */     }
/* 413 */     if (paramClassDefinition.isInterface())
/*     */     {
/* 416 */       paramInt |= 9;
/* 417 */       paramInt &= -7;
/*     */     }
/* 419 */     this.modifiers = paramInt;
/*     */ 
/* 421 */     setOuterClass(paramClassDefinition);
/*     */ 
/* 423 */     for (MemberDefinition localMemberDefinition = getFirstMember(); 
/* 424 */       localMemberDefinition != null; 
/* 425 */       localMemberDefinition = localMemberDefinition.getNextMember())
/* 426 */       if ((localMemberDefinition.isUplevelValue()) && 
/* 427 */         (paramClassDefinition
/* 427 */         .getType().equals(localMemberDefinition.getType())) && 
/* 428 */         (localMemberDefinition
/* 428 */         .getName().toString().startsWith("this$")))
/* 429 */         setOuterMember(localMemberDefinition);
/*     */   }
/*     */ 
/*     */   private void initOuter(ClassDefinition paramClassDefinition, int paramInt)
/*     */   {
/* 435 */     if ((paramClassDefinition instanceof BinaryClass))
/* 436 */       ((BinaryClass)paramClassDefinition).initInner(this, paramInt);
/* 437 */     addMember(new BinaryMember(paramClassDefinition));
/*     */   }
/*     */ 
/*     */   public void write(Environment paramEnvironment, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 444 */     DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
/*     */ 
/* 447 */     localDataOutputStream.writeInt(-889275714);
/* 448 */     localDataOutputStream.writeShort(paramEnvironment.getMinorVersion());
/* 449 */     localDataOutputStream.writeShort(paramEnvironment.getMajorVersion());
/*     */ 
/* 452 */     this.cpool.write(localDataOutputStream, paramEnvironment);
/*     */ 
/* 455 */     localDataOutputStream.writeShort(getModifiers() & 0xE31);
/* 456 */     localDataOutputStream.writeShort(this.cpool.indexObject(getClassDeclaration(), paramEnvironment));
/* 457 */     localDataOutputStream.writeShort(getSuperClass() != null ? this.cpool
/* 458 */       .indexObject(getSuperClass(), paramEnvironment) : 0);
/* 459 */     localDataOutputStream.writeShort(this.interfaces.length);
/* 460 */     for (int i = 0; i < this.interfaces.length; i++) {
/* 461 */       localDataOutputStream.writeShort(this.cpool.indexObject(this.interfaces[i], paramEnvironment));
/*     */     }
/*     */ 
/* 465 */     i = 0; int j = 0;
/* 466 */     for (MemberDefinition localMemberDefinition = this.firstMember; localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 467 */       if (localMemberDefinition.isMethod()) j++; else i++;
/*     */     }
/*     */ 
/* 470 */     localDataOutputStream.writeShort(i);
/*     */     String str1;
/*     */     String str2;
/* 471 */     for (localMemberDefinition = this.firstMember; localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 472 */       if (!localMemberDefinition.isMethod()) {
/* 473 */         localDataOutputStream.writeShort(localMemberDefinition.getModifiers() & 0xDF);
/* 474 */         str1 = localMemberDefinition.getName().toString();
/* 475 */         str2 = localMemberDefinition.getType().getTypeSignature();
/* 476 */         localDataOutputStream.writeShort(this.cpool.indexString(str1, paramEnvironment));
/* 477 */         localDataOutputStream.writeShort(this.cpool.indexString(str2, paramEnvironment));
/* 478 */         BinaryAttribute.write(((BinaryMember)localMemberDefinition).atts, localDataOutputStream, this.cpool, paramEnvironment);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 483 */     localDataOutputStream.writeShort(j);
/* 484 */     for (localMemberDefinition = this.firstMember; localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 485 */       if (localMemberDefinition.isMethod()) {
/* 486 */         localDataOutputStream.writeShort(localMemberDefinition.getModifiers() & 0xD3F);
/* 487 */         str1 = localMemberDefinition.getName().toString();
/* 488 */         str2 = localMemberDefinition.getType().getTypeSignature();
/* 489 */         localDataOutputStream.writeShort(this.cpool.indexString(str1, paramEnvironment));
/* 490 */         localDataOutputStream.writeShort(this.cpool.indexString(str2, paramEnvironment));
/* 491 */         BinaryAttribute.write(((BinaryMember)localMemberDefinition).atts, localDataOutputStream, this.cpool, paramEnvironment);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 496 */     BinaryAttribute.write(this.atts, localDataOutputStream, this.cpool, paramEnvironment);
/* 497 */     localDataOutputStream.flush();
/*     */   }
/*     */ 
/*     */   public Enumeration getDependencies()
/*     */   {
/* 504 */     return this.dependencies.elements();
/*     */   }
/*     */ 
/*     */   public void addDependency(ClassDeclaration paramClassDeclaration)
/*     */   {
/* 511 */     if ((paramClassDeclaration != null) && (!this.dependencies.contains(paramClassDeclaration)))
/* 512 */       this.dependencies.addElement(paramClassDeclaration);
/*     */   }
/*     */ 
/*     */   public BinaryConstantPool getConstants()
/*     */   {
/* 520 */     return this.cpool;
/*     */   }
/*     */ 
/*     */   public byte[] getAttribute(Identifier paramIdentifier)
/*     */   {
/* 527 */     for (BinaryAttribute localBinaryAttribute = this.atts; localBinaryAttribute != null; localBinaryAttribute = localBinaryAttribute.next) {
/* 528 */       if (localBinaryAttribute.name.equals(paramIdentifier)) {
/* 529 */         return localBinaryAttribute.data;
/*     */       }
/*     */     }
/* 532 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryClass
 * JD-Core Version:    0.6.2
 */