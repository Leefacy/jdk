/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class SpecialClassType extends ClassType
/*     */ {
/*     */   public static SpecialClassType forSpecial(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*     */   {
/*  68 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  70 */     sun.tools.java.Type localType = paramClassDefinition.getType();
/*     */ 
/*  74 */     String str = localType.toString() + paramContextStack.getContextCodeString();
/*     */ 
/*  76 */     Type localType1 = getType(str, paramContextStack);
/*     */ 
/*  78 */     if (localType1 != null)
/*     */     {
/*  80 */       if (!(localType1 instanceof SpecialClassType)) return null;
/*     */ 
/*  84 */       return (SpecialClassType)localType1;
/*     */     }
/*     */ 
/*  89 */     int i = getTypeCode(localType, paramClassDefinition, paramContextStack);
/*     */ 
/*  91 */     if (i != 0)
/*     */     {
/*  95 */       SpecialClassType localSpecialClassType = new SpecialClassType(paramContextStack, i, paramClassDefinition);
/*  96 */       putType(str, localSpecialClassType, paramContextStack);
/*  97 */       paramContextStack.push(localSpecialClassType);
/*  98 */       paramContextStack.pop(true);
/*  99 */       return localSpecialClassType;
/*     */     }
/*     */ 
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 111 */     return "Special class";
/*     */   }
/*     */ 
/*     */   private SpecialClassType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition)
/*     */   {
/* 123 */     super(paramContextStack, paramInt | 0x10000000 | 0x4000000 | 0x2000000, paramClassDefinition);
/* 124 */     Identifier localIdentifier = paramClassDefinition.getName();
/* 125 */     String str = null;
/* 126 */     String[] arrayOfString = null;
/* 127 */     boolean bool = (paramContextStack.size() > 0) && (paramContextStack.getContext().isConstant());
/*     */ 
/* 131 */     switch (paramInt) {
/*     */     case 512:
/* 133 */       str = IDLNames.getTypeName(paramInt, bool);
/* 134 */       if (!bool)
/* 135 */         arrayOfString = IDL_CORBA_MODULE; break;
/*     */     case 1024:
/* 141 */       str = "_Object";
/* 142 */       arrayOfString = IDL_JAVA_LANG_MODULE;
/*     */     }
/*     */ 
/* 147 */     setNames(localIdentifier, arrayOfString, str);
/*     */ 
/* 151 */     if (!initParents(paramContextStack))
/*     */     {
/* 155 */       throw new CompilerError("SpecialClassType found invalid parent.");
/*     */     }
/*     */ 
/* 160 */     initialize(null, null, null, paramContextStack, false);
/*     */   }
/*     */ 
/*     */   private static int getTypeCode(sun.tools.java.Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/* 164 */     if (paramType.isType(10)) {
/* 165 */       Identifier localIdentifier = paramType.getClassName();
/* 166 */       if (localIdentifier == idJavaLangString) return 512;
/* 167 */       if (localIdentifier == idJavaLangObject) return 1024;
/*     */     }
/* 169 */     return 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.SpecialClassType
 * JD-Core Version:    0.6.2
 */