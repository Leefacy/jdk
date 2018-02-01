/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ class BreakpointSpec extends EventRequestSpec
/*     */ {
/*     */   String methodId;
/*     */   List<String> methodArgs;
/*     */   int lineNumber;
/*     */ 
/*     */   BreakpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, int paramInt)
/*     */   {
/*  49 */     super(paramReferenceTypeSpec);
/*  50 */     this.methodId = null;
/*  51 */     this.methodArgs = null;
/*  52 */     this.lineNumber = paramInt;
/*     */   }
/*     */ 
/*     */   BreakpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString, List<String> paramList) throws MalformedMemberNameException
/*     */   {
/*  57 */     super(paramReferenceTypeSpec);
/*  58 */     this.methodId = paramString;
/*  59 */     this.methodArgs = paramList;
/*  60 */     this.lineNumber = 0;
/*  61 */     if (!isValidMethodName(paramString))
/*  62 */       throw new MalformedMemberNameException(paramString);
/*     */   }
/*     */ 
/*     */   EventRequest resolveEventRequest(ReferenceType paramReferenceType)
/*     */     throws AmbiguousMethodException, AbsentInformationException, InvalidTypeException, NoSuchMethodException, LineNotFoundException
/*     */   {
/*  76 */     Location localLocation = location(paramReferenceType);
/*  77 */     if (localLocation == null) {
/*  78 */       throw new InvalidTypeException();
/*     */     }
/*  80 */     EventRequestManager localEventRequestManager = paramReferenceType.virtualMachine().eventRequestManager();
/*  81 */     BreakpointRequest localBreakpointRequest = localEventRequestManager.createBreakpointRequest(localLocation);
/*  82 */     localBreakpointRequest.setSuspendPolicy(this.suspendPolicy);
/*  83 */     localBreakpointRequest.enable();
/*  84 */     return localBreakpointRequest;
/*     */   }
/*     */ 
/*     */   String methodName() {
/*  88 */     return this.methodId;
/*     */   }
/*     */ 
/*     */   int lineNumber() {
/*  92 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */   List<String> methodArgs() {
/*  96 */     return this.methodArgs;
/*     */   }
/*     */ 
/*     */   boolean isMethodBreakpoint() {
/* 100 */     return this.methodId != null;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 107 */     return this.refSpec.hashCode() + this.lineNumber + (this.methodId != null ? this.methodId
/* 106 */       .hashCode() : 0) + (this.methodArgs != null ? this.methodArgs
/* 107 */       .hashCode() : 0);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 112 */     if ((paramObject instanceof BreakpointSpec)) {
/* 113 */       BreakpointSpec localBreakpointSpec = (BreakpointSpec)paramObject;
/*     */ 
/* 115 */       if ((this.methodId != null ? this.methodId
/* 116 */         .equals(localBreakpointSpec.methodId) : 
/* 116 */         this.methodId == localBreakpointSpec.methodId) && (this.methodArgs != null ? this.methodArgs
/* 119 */         .equals(localBreakpointSpec.methodArgs) : 
/* 119 */         this.methodArgs == localBreakpointSpec.methodArgs));
/* 121 */       return (this.refSpec
/* 121 */         .equals(localBreakpointSpec.refSpec)) && 
/* 121 */         (this.lineNumber == localBreakpointSpec.lineNumber);
/*     */     }
/*     */ 
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   String errorMessageFor(Exception paramException)
/*     */   {
/* 130 */     if ((paramException instanceof AmbiguousMethodException)) {
/* 131 */       return MessageOutput.format("Method is overloaded; specify arguments", 
/* 132 */         methodName());
/*     */     }
/*     */ 
/* 136 */     if ((paramException instanceof NoSuchMethodException))
/* 137 */       return MessageOutput.format("No method in", new Object[] { 
/* 138 */         methodName(), this.refSpec
/* 139 */         .toString() });
/* 140 */     if ((paramException instanceof AbsentInformationException))
/* 141 */       return MessageOutput.format("No linenumber information for", this.refSpec
/* 142 */         .toString());
/* 143 */     if ((paramException instanceof LineNotFoundException))
/* 144 */       return MessageOutput.format("No code at line", new Object[] { new Long(
/* 145 */         lineNumber()), this.refSpec
/* 146 */         .toString() });
/* 147 */     if ((paramException instanceof InvalidTypeException)) {
/* 148 */       return MessageOutput.format("Breakpoints can be located only in classes.", this.refSpec
/* 149 */         .toString());
/*     */     }
/* 151 */     return super.errorMessageFor(paramException);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 157 */     StringBuffer localStringBuffer = new StringBuffer(this.refSpec.toString());
/* 158 */     if (isMethodBreakpoint()) {
/* 159 */       localStringBuffer.append('.');
/* 160 */       localStringBuffer.append(this.methodId);
/* 161 */       if (this.methodArgs != null) {
/* 162 */         int i = 1;
/* 163 */         localStringBuffer.append('(');
/* 164 */         for (String str : this.methodArgs) {
/* 165 */           if (i == 0) {
/* 166 */             localStringBuffer.append(',');
/*     */           }
/* 168 */           localStringBuffer.append(str);
/* 169 */           i = 0;
/*     */         }
/* 171 */         localStringBuffer.append(")");
/*     */       }
/*     */     } else {
/* 174 */       localStringBuffer.append(':');
/* 175 */       localStringBuffer.append(this.lineNumber);
/*     */     }
/* 177 */     return MessageOutput.format("breakpoint", localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   private Location location(ReferenceType paramReferenceType)
/*     */     throws AmbiguousMethodException, AbsentInformationException, NoSuchMethodException, LineNotFoundException
/*     */   {
/* 185 */     Location localLocation = null;
/*     */     Object localObject;
/* 186 */     if (isMethodBreakpoint()) {
/* 187 */       localObject = findMatchingMethod(paramReferenceType);
/* 188 */       localLocation = ((Method)localObject).location();
/*     */     }
/*     */     else {
/* 191 */       localObject = paramReferenceType.locationsOfLine(lineNumber());
/* 192 */       if (((List)localObject).size() == 0) {
/* 193 */         throw new LineNotFoundException();
/*     */       }
/*     */ 
/* 196 */       localLocation = (Location)((List)localObject).get(0);
/* 197 */       if (localLocation.method() == null) {
/* 198 */         throw new LineNotFoundException();
/*     */       }
/*     */     }
/* 201 */     return localLocation;
/*     */   }
/*     */ 
/*     */   private boolean isValidMethodName(String paramString)
/*     */   {
/* 207 */     return (isJavaIdentifier(paramString)) || 
/* 206 */       (paramString
/* 206 */       .equals("<init>")) || 
/* 207 */       (paramString
/* 207 */       .equals("<clinit>"));
/*     */   }
/*     */ 
/*     */   private boolean compareArgTypes(Method paramMethod, List<String> paramList)
/*     */   {
/* 218 */     List localList = paramMethod.argumentTypeNames();
/*     */ 
/* 221 */     if (localList.size() != paramList.size()) {
/* 222 */       return false;
/*     */     }
/*     */ 
/* 226 */     int i = localList.size();
/* 227 */     for (int j = 0; j < i; j++) {
/* 228 */       String str1 = (String)localList.get(j);
/* 229 */       String str2 = (String)paramList.get(j);
/* 230 */       if (!str1.equals(str2))
/*     */       {
/* 238 */         if ((j != i - 1) || 
/* 239 */           (!paramMethod
/* 239 */           .isVarArgs()) || 
/* 240 */           (!str2
/* 240 */           .endsWith("...")))
/*     */         {
/* 241 */           return false;
/*     */         }
/*     */ 
/* 250 */         int k = str1.length();
/* 251 */         if (k + 1 != str2.length())
/*     */         {
/* 253 */           return false;
/*     */         }
/*     */ 
/* 256 */         if (!str1.regionMatches(0, str2, 0, k - 2)) {
/* 257 */           return false;
/*     */         }
/*     */ 
/* 260 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */   private String normalizeArgTypeName(String paramString)
/*     */   {
/* 277 */     int i = 0;
/* 278 */     StringBuffer localStringBuffer1 = new StringBuffer();
/* 279 */     StringBuffer localStringBuffer2 = new StringBuffer();
/* 280 */     paramString = paramString.trim();
/* 281 */     int j = paramString.length();
/*     */ 
/* 287 */     boolean bool = paramString.endsWith("...");
/* 288 */     if (bool)
/* 289 */       j -= 3;
/*     */     char c;
/* 291 */     while (i < j) {
/* 292 */       c = paramString.charAt(i);
/* 293 */       if ((Character.isWhitespace(c)) || (c == '[')) {
/*     */         break;
/*     */       }
/* 296 */       localStringBuffer1.append(c);
/* 297 */       i++;
/*     */     }
/* 299 */     while (i < j) {
/* 300 */       c = paramString.charAt(i);
/* 301 */       if ((c == '[') || (c == ']'))
/* 302 */         localStringBuffer2.append(c);
/* 303 */       else if (!Character.isWhitespace(c))
/*     */       {
/* 305 */         throw new IllegalArgumentException(
/* 305 */           MessageOutput.format("Invalid argument type name"));
/*     */       }
/*     */ 
/* 307 */       i++;
/*     */     }
/* 309 */     paramString = localStringBuffer1.toString();
/*     */ 
/* 315 */     if ((paramString.indexOf('.') == -1) || (paramString.startsWith("*."))) {
/*     */       try {
/* 317 */         ReferenceType localReferenceType = Env.getReferenceTypeFromToken(paramString);
/* 318 */         if (localReferenceType != null)
/* 319 */           paramString = localReferenceType.name();
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/*     */     }
/* 325 */     paramString = paramString + localStringBuffer2.toString();
/* 326 */     if (bool) {
/* 327 */       paramString = paramString + "...";
/*     */     }
/* 329 */     return paramString;
/*     */   }
/*     */ 
/*     */   private Method findMatchingMethod(ReferenceType paramReferenceType)
/*     */     throws AmbiguousMethodException, NoSuchMethodException
/*     */   {
/* 343 */     ArrayList localArrayList = null;
/* 344 */     if (methodArgs() != null) {
/* 345 */       localArrayList = new ArrayList(methodArgs().size());
/* 346 */       for (localObject1 = methodArgs().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 347 */         localObject2 = normalizeArgTypeName((String)localObject2);
/* 348 */         localArrayList.add(localObject2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 353 */     Object localObject1 = null;
/* 354 */     Object localObject2 = null;
/* 355 */     int i = 0;
/* 356 */     for (Object localObject3 = paramReferenceType.methods().iterator(); ((Iterator)localObject3).hasNext(); ) { Method localMethod = (Method)((Iterator)localObject3).next();
/* 357 */       if (localMethod.name().equals(methodName())) {
/* 358 */         i++;
/*     */ 
/* 361 */         if (i == 1) {
/* 362 */           localObject1 = localMethod;
/*     */         }
/*     */ 
/* 366 */         if ((localArrayList != null) && 
/* 367 */           (compareArgTypes(localMethod, localArrayList) == 
/* 367 */           true)) {
/* 368 */           localObject2 = localMethod;
/* 369 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 375 */     localObject3 = null;
/* 376 */     if (localObject2 != null)
/*     */     {
/* 378 */       localObject3 = localObject2;
/* 379 */     } else if ((localArrayList == null) && (i > 0))
/*     */     {
/* 381 */       if (i == 1)
/* 382 */         localObject3 = localObject1;
/*     */       else
/* 384 */         throw new AmbiguousMethodException();
/*     */     }
/*     */     else {
/* 387 */       throw new NoSuchMethodException(methodName());
/*     */     }
/* 389 */     return localObject3;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.BreakpointSpec
 * JD-Core Version:    0.6.2
 */