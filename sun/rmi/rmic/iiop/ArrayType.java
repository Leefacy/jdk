/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.HashSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ArrayType extends Type
/*     */ {
/*     */   private Type type;
/*     */   private int arrayDimension;
/*     */   private String brackets;
/*     */   private String bracketsSig;
/*     */ 
/*     */   public static ArrayType forArray(sun.tools.java.Type paramType, ContextStack paramContextStack)
/*     */   {
/*  71 */     ArrayType localArrayType = null;
/*  72 */     sun.tools.java.Type localType = paramType;
/*     */ 
/*  74 */     if (localType.getTypeCode() == 9)
/*     */     {
/*  78 */       while (localType.getTypeCode() == 9) {
/*  79 */         localType = localType.getElementType();
/*     */       }
/*     */ 
/*  84 */       Type localType1 = getType(paramType, paramContextStack);
/*  85 */       if (localType1 != null)
/*     */       {
/*  87 */         if (!(localType1 instanceof ArrayType)) return null;
/*     */ 
/*  91 */         return (ArrayType)localType1;
/*     */       }
/*     */ 
/*  96 */       Type localType2 = CompoundType.makeType(localType, null, paramContextStack);
/*     */ 
/*  98 */       if (localType2 != null)
/*     */       {
/* 102 */         localArrayType = new ArrayType(paramContextStack, localType2, paramType.getArrayDimension());
/*     */ 
/* 106 */         putType(paramType, localArrayType, paramContextStack);
/*     */ 
/* 110 */         paramContextStack.push(localArrayType);
/* 111 */         paramContextStack.pop(true);
/*     */       }
/*     */     }
/*     */ 
/* 115 */     return localArrayType;
/*     */   }
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 123 */     return this.bracketsSig + this.type.getSignature();
/*     */   }
/*     */ 
/*     */   public Type getElementType()
/*     */   {
/* 130 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getArrayDimension()
/*     */   {
/* 137 */     return this.arrayDimension;
/*     */   }
/*     */ 
/*     */   public String getArrayBrackets()
/*     */   {
/* 144 */     return this.brackets;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     return getQualifiedName() + this.brackets;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 158 */     return "Array of " + this.type.getTypeDescription();
/*     */   }
/*     */ 
/*     */   public String getTypeName(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 171 */     if (paramBoolean2) {
/* 172 */       return super.getTypeName(paramBoolean1, paramBoolean2, paramBoolean3);
/*     */     }
/* 174 */     return super.getTypeName(paramBoolean1, paramBoolean2, paramBoolean3) + this.brackets;
/*     */   }
/*     */ 
/*     */   protected void swapInvalidTypes()
/*     */   {
/* 187 */     if (this.type.getStatus() != 1)
/* 188 */       this.type = getValidType(this.type);
/*     */   }
/*     */ 
/*     */   protected boolean addTypes(int paramInt, HashSet paramHashSet, Vector paramVector)
/*     */   {
/* 202 */     boolean bool = super.addTypes(paramInt, paramHashSet, paramVector);
/*     */ 
/* 206 */     if (bool)
/*     */     {
/* 210 */       getElementType().addTypes(paramInt, paramHashSet, paramVector);
/*     */     }
/*     */ 
/* 213 */     return bool;
/*     */   }
/*     */ 
/*     */   private ArrayType(ContextStack paramContextStack, Type paramType, int paramInt)
/*     */   {
/* 221 */     super(paramContextStack, 262144);
/* 222 */     this.type = paramType;
/* 223 */     this.arrayDimension = paramInt;
/*     */ 
/* 227 */     this.brackets = "";
/* 228 */     this.bracketsSig = "";
/* 229 */     for (int i = 0; i < paramInt; i++) {
/* 230 */       this.brackets += "[]";
/* 231 */       this.bracketsSig += "[";
/*     */     }
/*     */ 
/* 236 */     String str = IDLNames.getArrayName(paramType, paramInt);
/* 237 */     String[] arrayOfString = IDLNames.getArrayModuleNames(paramType);
/* 238 */     setNames(paramType.getIdentifier(), arrayOfString, str);
/*     */ 
/* 242 */     setRepositoryID();
/*     */   }
/*     */ 
/*     */   protected Class loadClass()
/*     */   {
/* 250 */     Class localClass1 = null;
/* 251 */     Class localClass2 = this.type.getClassInstance();
/* 252 */     if (localClass2 != null) {
/* 253 */       localClass1 = Array.newInstance(localClass2, new int[this.arrayDimension]).getClass();
/*     */     }
/* 255 */     return localClass1;
/*     */   }
/*     */ 
/*     */   protected void destroy()
/*     */   {
/* 262 */     super.destroy();
/* 263 */     if (this.type != null) {
/* 264 */       this.type.destroy();
/* 265 */       this.type = null;
/*     */     }
/* 267 */     this.brackets = null;
/* 268 */     this.bracketsSig = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.ArrayType
 * JD-Core Version:    0.6.2
 */