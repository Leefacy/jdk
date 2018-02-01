/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class LocationImpl extends MirrorImpl
/*     */   implements Location
/*     */ {
/*     */   private final ReferenceTypeImpl declaringType;
/*     */   private Method method;
/*     */   private long methodRef;
/*     */   private long codeIndex;
/*  37 */   private LineInfo baseLineInfo = null;
/*  38 */   private LineInfo otherLineInfo = null;
/*     */ 
/*     */   LocationImpl(VirtualMachine paramVirtualMachine, Method paramMethod, long paramLong)
/*     */   {
/*  42 */     super(paramVirtualMachine);
/*     */ 
/*  44 */     this.method = paramMethod;
/*  45 */     this.codeIndex = (paramMethod.isNative() ? -1L : paramLong);
/*  46 */     this.declaringType = ((ReferenceTypeImpl)paramMethod.declaringType());
/*     */   }
/*     */ 
/*     */   LocationImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong1, long paramLong2)
/*     */   {
/*  56 */     super(paramVirtualMachine);
/*     */ 
/*  58 */     this.method = null;
/*  59 */     this.codeIndex = paramLong2;
/*  60 */     this.declaringType = paramReferenceTypeImpl;
/*  61 */     this.methodRef = paramLong1;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  65 */     if ((paramObject != null) && ((paramObject instanceof Location))) {
/*  66 */       Location localLocation = (Location)paramObject;
/*     */ 
/*  69 */       return (method().equals(localLocation.method())) && 
/*  68 */         (codeIndex() == localLocation.codeIndex()) && 
/*  69 */         (super
/*  69 */         .equals(paramObject));
/*     */     }
/*     */ 
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  79 */     return method().hashCode() + (int)codeIndex();
/*     */   }
/*     */ 
/*     */   public int compareTo(Location paramLocation) {
/*  83 */     LocationImpl localLocationImpl = (LocationImpl)paramLocation;
/*  84 */     int i = method().compareTo(localLocationImpl.method());
/*  85 */     if (i == 0) {
/*  86 */       long l = codeIndex() - localLocationImpl.codeIndex();
/*  87 */       if (l < 0L)
/*  88 */         return -1;
/*  89 */       if (l > 0L) {
/*  90 */         return 1;
/*     */       }
/*  92 */       return 0;
/*     */     }
/*  94 */     return i;
/*     */   }
/*     */ 
/*     */   public ReferenceType declaringType() {
/*  98 */     return this.declaringType;
/*     */   }
/*     */ 
/*     */   public Method method() {
/* 102 */     if (this.method == null) {
/* 103 */       this.method = this.declaringType.getMethodMirror(this.methodRef);
/* 104 */       if (this.method.isNative()) {
/* 105 */         this.codeIndex = -1L;
/*     */       }
/*     */     }
/* 108 */     return this.method;
/*     */   }
/*     */ 
/*     */   public long codeIndex() {
/* 112 */     method();
/* 113 */     return this.codeIndex;
/*     */   }
/*     */ 
/*     */   LineInfo getBaseLineInfo(SDE.Stratum paramStratum)
/*     */   {
/* 120 */     if (this.baseLineInfo != null) {
/* 121 */       return this.baseLineInfo;
/*     */     }
/*     */ 
/* 125 */     MethodImpl localMethodImpl = (MethodImpl)method();
/* 126 */     LineInfo localLineInfo = localMethodImpl.codeIndexToLineInfo(paramStratum, 
/* 127 */       codeIndex());
/*     */ 
/* 130 */     addBaseLineInfo(localLineInfo);
/*     */ 
/* 132 */     return localLineInfo;
/*     */   }
/*     */ 
/*     */   LineInfo getLineInfo(SDE.Stratum paramStratum)
/*     */   {
/* 139 */     if (paramStratum.isJava()) {
/* 140 */       return getBaseLineInfo(paramStratum);
/*     */     }
/*     */ 
/* 144 */     Object localObject = this.otherLineInfo;
/* 145 */     if ((localObject != null) && 
/* 146 */       (paramStratum
/* 146 */       .id().equals(((LineInfo)localObject).liStratum()))) {
/* 147 */       return localObject;
/*     */     }
/*     */ 
/* 150 */     int i = lineNumber("Java");
/*     */ 
/* 152 */     SDE.LineStratum localLineStratum = paramStratum
/* 152 */       .lineStratum(this.declaringType, i);
/*     */ 
/* 154 */     if ((localLineStratum != null) && (localLineStratum.lineNumber() != -1))
/*     */     {
/* 158 */       localObject = new StratumLineInfo(paramStratum.id(), localLineStratum
/* 156 */         .lineNumber(), localLineStratum
/* 157 */         .sourceName(), localLineStratum
/* 158 */         .sourcePath());
/*     */     }
/*     */     else {
/* 161 */       MethodImpl localMethodImpl = (MethodImpl)method();
/* 162 */       localObject = localMethodImpl.codeIndexToLineInfo(paramStratum, 
/* 163 */         codeIndex());
/*     */     }
/*     */ 
/* 167 */     addStratumLineInfo((LineInfo)localObject);
/*     */ 
/* 169 */     return localObject;
/*     */   }
/*     */ 
/*     */   void addStratumLineInfo(LineInfo paramLineInfo) {
/* 173 */     this.otherLineInfo = paramLineInfo;
/*     */   }
/*     */ 
/*     */   void addBaseLineInfo(LineInfo paramLineInfo) {
/* 177 */     this.baseLineInfo = paramLineInfo;
/*     */   }
/*     */ 
/*     */   public String sourceName() throws AbsentInformationException {
/* 181 */     return sourceName(this.vm.getDefaultStratum());
/*     */   }
/*     */ 
/*     */   public String sourceName(String paramString) throws AbsentInformationException
/*     */   {
/* 186 */     return sourceName(this.declaringType.stratum(paramString));
/*     */   }
/*     */ 
/*     */   String sourceName(SDE.Stratum paramStratum) throws AbsentInformationException
/*     */   {
/* 191 */     return getLineInfo(paramStratum).liSourceName();
/*     */   }
/*     */ 
/*     */   public String sourcePath() throws AbsentInformationException {
/* 195 */     return sourcePath(this.vm.getDefaultStratum());
/*     */   }
/*     */ 
/*     */   public String sourcePath(String paramString) throws AbsentInformationException
/*     */   {
/* 200 */     return sourcePath(this.declaringType.stratum(paramString));
/*     */   }
/*     */ 
/*     */   String sourcePath(SDE.Stratum paramStratum) throws AbsentInformationException
/*     */   {
/* 205 */     return getLineInfo(paramStratum).liSourcePath();
/*     */   }
/*     */ 
/*     */   public int lineNumber() {
/* 209 */     return lineNumber(this.vm.getDefaultStratum());
/*     */   }
/*     */ 
/*     */   public int lineNumber(String paramString) {
/* 213 */     return lineNumber(this.declaringType.stratum(paramString));
/*     */   }
/*     */ 
/*     */   int lineNumber(SDE.Stratum paramStratum) {
/* 217 */     return getLineInfo(paramStratum).liLineNumber();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 221 */     if (lineNumber() == -1) {
/* 222 */       return method().toString() + "+" + codeIndex();
/*     */     }
/* 224 */     return declaringType().name() + ":" + lineNumber();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.LocationImpl
 * JD-Core Version:    0.6.2
 */