/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.ClassPrepareRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ class PatternReferenceTypeSpec
/*     */   implements ReferenceTypeSpec
/*     */ {
/*     */   final String classId;
/*     */   String stem;
/*     */ 
/*     */   PatternReferenceTypeSpec(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  47 */     this.classId = paramString;
/*  48 */     this.stem = paramString;
/*  49 */     if (paramString.startsWith("*"))
/*  50 */       this.stem = this.stem.substring(1);
/*  51 */     else if (paramString.endsWith("*")) {
/*  52 */       this.stem = this.stem.substring(0, paramString.length() - 1);
/*     */     }
/*  54 */     checkClassName(this.stem);
/*     */   }
/*     */ 
/*     */   public boolean isUnique()
/*     */   {
/*  61 */     return this.classId.equals(this.stem);
/*     */   }
/*     */ 
/*     */   public boolean matches(ReferenceType paramReferenceType)
/*     */   {
/*  69 */     if (this.classId.startsWith("*"))
/*  70 */       return paramReferenceType.name().endsWith(this.stem);
/*  71 */     if (this.classId.endsWith("*")) {
/*  72 */       return paramReferenceType.name().startsWith(this.stem);
/*     */     }
/*  74 */     return paramReferenceType.name().equals(this.classId);
/*     */   }
/*     */ 
/*     */   public ClassPrepareRequest createPrepareRequest()
/*     */   {
/*  81 */     ClassPrepareRequest localClassPrepareRequest = Env.vm().eventRequestManager().createClassPrepareRequest();
/*  82 */     localClassPrepareRequest.addClassFilter(this.classId);
/*  83 */     localClassPrepareRequest.addCountFilter(1);
/*  84 */     return localClassPrepareRequest;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  89 */     return this.classId.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  94 */     if ((paramObject instanceof PatternReferenceTypeSpec)) {
/*  95 */       PatternReferenceTypeSpec localPatternReferenceTypeSpec = (PatternReferenceTypeSpec)paramObject;
/*     */ 
/*  97 */       return this.classId.equals(localPatternReferenceTypeSpec.classId);
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   private void checkClassName(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 108 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ".");
/* 109 */     while (localStringTokenizer.hasMoreTokens()) {
/* 110 */       String str = localStringTokenizer.nextToken();
/*     */ 
/* 115 */       if (!isJavaIdentifier(str))
/* 116 */         throw new ClassNotFoundException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isJavaIdentifier(String paramString)
/*     */   {
/* 122 */     if (paramString.length() == 0) {
/* 123 */       return false;
/*     */     }
/*     */ 
/* 126 */     int i = paramString.codePointAt(0);
/* 127 */     if (!Character.isJavaIdentifierStart(i)) {
/* 128 */       return false;
/*     */     }
/*     */ 
/* 131 */     for (int j = Character.charCount(i); j < paramString.length(); j += Character.charCount(i)) {
/* 132 */       i = paramString.codePointAt(j);
/* 133 */       if (!Character.isJavaIdentifierPart(i)) {
/* 134 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     return this.classId;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.PatternReferenceTypeSpec
 * JD-Core Version:    0.6.2
 */