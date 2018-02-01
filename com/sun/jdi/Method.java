/*     */ package com.sun.jdi;
/*     */ 
/*     */ import java.util.List;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public abstract interface Method extends TypeComponent, Locatable, Comparable<Method>
/*     */ {
/*     */   public abstract String returnTypeName();
/*     */ 
/*     */   public abstract Type returnType()
/*     */     throws ClassNotLoadedException;
/*     */ 
/*     */   public abstract List<String> argumentTypeNames();
/*     */ 
/*     */   public abstract List<Type> argumentTypes()
/*     */     throws ClassNotLoadedException;
/*     */ 
/*     */   public abstract boolean isAbstract();
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public abstract boolean isSynchronized();
/*     */ 
/*     */   public abstract boolean isNative();
/*     */ 
/*     */   public abstract boolean isVarArgs();
/*     */ 
/*     */   public abstract boolean isBridge();
/*     */ 
/*     */   public abstract boolean isConstructor();
/*     */ 
/*     */   public abstract boolean isStaticInitializer();
/*     */ 
/*     */   public abstract boolean isObsolete();
/*     */ 
/*     */   public abstract List<Location> allLineLocations()
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract List<Location> allLineLocations(String paramString1, String paramString2)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract List<Location> locationsOfLine(int paramInt)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract Location locationOfCodeIndex(long paramLong);
/*     */ 
/*     */   public abstract List<LocalVariable> variables()
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract List<LocalVariable> variablesByName(String paramString)
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract List<LocalVariable> arguments()
/*     */     throws AbsentInformationException;
/*     */ 
/*     */   public abstract byte[] bytecodes();
/*     */ 
/*     */   public abstract Location location();
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract int hashCode();
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Method
 * JD-Core Version:    0.6.2
 */