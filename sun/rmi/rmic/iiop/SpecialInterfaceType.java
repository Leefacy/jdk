/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class SpecialInterfaceType extends InterfaceType
/*     */ {
/*     */   public static SpecialInterfaceType forSpecial(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*     */   {
/*  75 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  79 */     sun.tools.java.Type localType = paramClassDefinition.getType();
/*  80 */     Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  82 */     if (localType1 != null)
/*     */     {
/*  84 */       if (!(localType1 instanceof SpecialInterfaceType)) return null;
/*     */ 
/*  88 */       return (SpecialInterfaceType)localType1;
/*     */     }
/*     */ 
/*  93 */     if (isSpecial(localType, paramClassDefinition, paramContextStack))
/*     */     {
/*  97 */       SpecialInterfaceType localSpecialInterfaceType = new SpecialInterfaceType(paramContextStack, 0, paramClassDefinition);
/*  98 */       putType(localType, localSpecialInterfaceType, paramContextStack);
/*  99 */       paramContextStack.push(localSpecialInterfaceType);
/*     */ 
/* 101 */       if (localSpecialInterfaceType.initialize(localType, paramContextStack)) {
/* 102 */         paramContextStack.pop(true);
/* 103 */         return localSpecialInterfaceType;
/*     */       }
/* 105 */       removeType(localType, paramContextStack);
/* 106 */       paramContextStack.pop(false);
/* 107 */       return null;
/*     */     }
/*     */ 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 117 */     return "Special interface";
/*     */   }
/*     */ 
/*     */   private SpecialInterfaceType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition)
/*     */   {
/* 129 */     super(paramContextStack, paramInt | 0x20000000 | 0x8000000 | 0x2000000, paramClassDefinition);
/* 130 */     setNames(paramClassDefinition.getName(), null, null);
/*     */   }
/*     */ 
/*     */   private static boolean isSpecial(sun.tools.java.Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*     */   {
/* 136 */     if (paramType.isType(10)) {
/* 137 */       Identifier localIdentifier = paramType.getClassName();
/*     */ 
/* 139 */       if (localIdentifier.equals(idRemote)) return true;
/* 140 */       if (localIdentifier == idJavaIoSerializable) return true;
/* 141 */       if (localIdentifier == idJavaIoExternalizable) return true;
/* 142 */       if (localIdentifier == idCorbaObject) return true;
/* 143 */       if (localIdentifier == idIDLEntity) return true;
/* 144 */       BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*     */       try {
/* 146 */         if (localBatchEnvironment.defCorbaObject.implementedBy(localBatchEnvironment, paramClassDefinition.getClassDeclaration())) return true; 
/*     */       }
/* 148 */       catch (ClassNotFound localClassNotFound) { classNotFound(paramContextStack, localClassNotFound); }
/*     */ 
/*     */     }
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean initialize(sun.tools.java.Type paramType, ContextStack paramContextStack)
/*     */   {
/* 156 */     int i = 0;
/* 157 */     Identifier localIdentifier = null;
/* 158 */     String str = null;
/* 159 */     String[] arrayOfString = null;
/* 160 */     boolean bool = (paramContextStack.size() > 0) && (paramContextStack.getContext().isConstant());
/*     */ 
/* 162 */     if (paramType.isType(10)) {
/* 163 */       localIdentifier = paramType.getClassName();
/*     */ 
/* 165 */       if (localIdentifier.equals(idRemote)) {
/* 166 */         i = 524288;
/* 167 */         str = "Remote";
/* 168 */         arrayOfString = IDL_JAVA_RMI_MODULE;
/* 169 */       } else if (localIdentifier == idJavaIoSerializable) {
/* 170 */         i = 1024;
/* 171 */         str = "Serializable";
/* 172 */         arrayOfString = IDL_JAVA_IO_MODULE;
/* 173 */       } else if (localIdentifier == idJavaIoExternalizable) {
/* 174 */         i = 1024;
/* 175 */         str = "Externalizable";
/* 176 */         arrayOfString = IDL_JAVA_IO_MODULE;
/* 177 */       } else if (localIdentifier == idIDLEntity) {
/* 178 */         i = 1024;
/* 179 */         str = "IDLEntity";
/* 180 */         arrayOfString = IDL_ORG_OMG_CORBA_PORTABLE_MODULE;
/*     */       }
/*     */       else {
/* 183 */         i = 2048;
/*     */ 
/* 187 */         if (localIdentifier == idCorbaObject)
/*     */         {
/* 191 */           str = IDLNames.getTypeName(i, bool);
/* 192 */           arrayOfString = null;
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 202 */             str = IDLNames.getClassOrInterfaceName(localIdentifier, this.env);
/* 203 */             arrayOfString = IDLNames.getModuleNames(localIdentifier, isBoxed(), this.env);
/*     */           }
/*     */           catch (Exception localException) {
/* 206 */             failedConstraint(7, false, paramContextStack, localIdentifier.toString(), localException.getMessage());
/* 207 */             throw new CompilerError("");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 213 */     if (i == 0) {
/* 214 */       return false;
/*     */     }
/*     */ 
/* 219 */     setTypeCode(i | 0x20000000 | 0x8000000 | 0x2000000);
/*     */ 
/* 223 */     if (str == null) {
/* 224 */       throw new CompilerError("Not a special type");
/*     */     }
/*     */ 
/* 227 */     setNames(localIdentifier, arrayOfString, str);
/*     */ 
/* 231 */     return initialize(null, null, null, paramContextStack, false);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.SpecialInterfaceType
 * JD-Core Version:    0.6.2
 */