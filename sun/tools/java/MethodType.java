/*     */ package sun.tools.java;
/*     */ 
/*     */ public final class MethodType extends Type
/*     */ {
/*     */   Type returnType;
/*     */   Type[] argTypes;
/*     */ 
/*     */   MethodType(String paramString, Type paramType, Type[] paramArrayOfType)
/*     */   {
/*  56 */     super(12, paramString);
/*  57 */     this.returnType = paramType;
/*  58 */     this.argTypes = paramArrayOfType;
/*     */   }
/*     */ 
/*     */   public Type getReturnType() {
/*  62 */     return this.returnType;
/*     */   }
/*     */ 
/*     */   public Type[] getArgumentTypes() {
/*  66 */     return this.argTypes;
/*     */   }
/*     */ 
/*     */   public boolean equalArguments(Type paramType) {
/*  70 */     if (paramType.typeCode != 12) {
/*  71 */       return false;
/*     */     }
/*  73 */     MethodType localMethodType = (MethodType)paramType;
/*  74 */     if (this.argTypes.length != localMethodType.argTypes.length) {
/*  75 */       return false;
/*     */     }
/*  77 */     for (int i = this.argTypes.length - 1; i >= 0; i--) {
/*  78 */       if (this.argTypes[i] != localMethodType.argTypes[i]) {
/*  79 */         return false;
/*     */       }
/*     */     }
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */   public int stackSize() {
/*  86 */     int i = 0;
/*  87 */     for (int j = 0; j < this.argTypes.length; j++) {
/*  88 */       i += this.argTypes[j].stackSize();
/*     */     }
/*  90 */     return i;
/*     */   }
/*     */ 
/*     */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/*  94 */     StringBuffer localStringBuffer = new StringBuffer();
/*  95 */     localStringBuffer.append(paramString);
/*  96 */     localStringBuffer.append('(');
/*  97 */     for (int i = 0; i < this.argTypes.length; i++) {
/*  98 */       if (i > 0) {
/*  99 */         localStringBuffer.append(", ");
/*     */       }
/* 101 */       localStringBuffer.append(this.argTypes[i].typeString("", paramBoolean1, paramBoolean2));
/*     */     }
/* 103 */     localStringBuffer.append(')');
/*     */ 
/* 105 */     return paramBoolean2 ? getReturnType().typeString(localStringBuffer.toString(), paramBoolean1, paramBoolean2) : localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.MethodType
 * JD-Core Version:    0.6.2
 */