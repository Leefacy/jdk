/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class ContextStack
/*     */ {
/*     */   public static final int TOP = 1;
/*     */   public static final int METHOD = 2;
/*     */   public static final int METHOD_RETURN = 3;
/*     */   public static final int METHOD_ARGUMENT = 4;
/*     */   public static final int METHOD_EXCEPTION = 5;
/*     */   public static final int MEMBER = 6;
/*     */   public static final int MEMBER_CONSTANT = 7;
/*     */   public static final int MEMBER_STATIC = 8;
/*     */   public static final int MEMBER_TRANSIENT = 9;
/*     */   public static final int IMPLEMENTS = 10;
/*     */   public static final int EXTENDS = 11;
/*  63 */   private static final String[] CODE_NAMES = { "UNKNOWN ", "Top level type ", "Method ", "Return parameter ", "Parameter ", "Exception ", "Member ", "Constant member ", "Static member ", "Transient member ", "Implements ", "Extends " };
/*     */ 
/*  79 */   private int currentIndex = -1;
/*  80 */   private int maxIndex = 100;
/*  81 */   private TypeContext[] stack = new TypeContext[this.maxIndex];
/*  82 */   private int newCode = 1;
/*  83 */   private BatchEnvironment env = null;
/*  84 */   private boolean trace = false;
/*  85 */   private TypeContext tempContext = new TypeContext();
/*     */   private static final String TRACE_INDENT = "   ";
/*     */ 
/*     */   public ContextStack(BatchEnvironment paramBatchEnvironment)
/*     */   {
/*  93 */     this.env = paramBatchEnvironment;
/*  94 */     paramBatchEnvironment.contextStack = this;
/*     */   }
/*     */ 
/*     */   public boolean anyErrors()
/*     */   {
/* 101 */     return this.env.nerrors > 0;
/*     */   }
/*     */ 
/*     */   public void setTrace(boolean paramBoolean)
/*     */   {
/* 108 */     this.trace = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isTraceOn()
/*     */   {
/* 115 */     return this.trace;
/*     */   }
/*     */ 
/*     */   public BatchEnvironment getEnv()
/*     */   {
/* 122 */     return this.env;
/*     */   }
/*     */ 
/*     */   public void setNewContextCode(int paramInt)
/*     */   {
/* 129 */     this.newCode = paramInt;
/*     */   }
/*     */ 
/*     */   public int getCurrentContextCode()
/*     */   {
/* 136 */     return this.newCode;
/*     */   }
/*     */ 
/*     */   final void traceCallStack()
/*     */   {
/* 145 */     if (this.trace) dumpCallStack(); 
/*     */   }
/*     */ 
/*     */   public static final void dumpCallStack()
/*     */   {
/* 149 */     new Error().printStackTrace(System.out);
/*     */   }
/*     */ 
/*     */   private final void tracePrint(String paramString, boolean paramBoolean)
/*     */   {
/* 156 */     int i = paramString.length() + this.currentIndex * "   ".length();
/* 157 */     StringBuffer localStringBuffer = new StringBuffer(i);
/* 158 */     for (int j = 0; j < this.currentIndex; j++) {
/* 159 */       localStringBuffer.append("   ");
/*     */     }
/* 161 */     localStringBuffer.append(paramString);
/* 162 */     if (paramBoolean) {
/* 163 */       localStringBuffer.append("\n");
/*     */     }
/* 165 */     System.out.print(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   final void trace(String paramString)
/*     */   {
/* 172 */     if (this.trace)
/* 173 */       tracePrint(paramString, false);
/*     */   }
/*     */ 
/*     */   final void traceln(String paramString)
/*     */   {
/* 181 */     if (this.trace)
/* 182 */       tracePrint(paramString, true);
/*     */   }
/*     */ 
/*     */   final void traceExistingType(Type paramType)
/*     */   {
/* 190 */     if (this.trace) {
/* 191 */       this.tempContext.set(this.newCode, paramType);
/* 192 */       traceln(toResultString(this.tempContext, true, true));
/*     */     }
/*     */   }
/*     */ 
/*     */   public TypeContext push(ContextElement paramContextElement)
/*     */   {
/* 202 */     this.currentIndex += 1;
/*     */ 
/* 206 */     if (this.currentIndex == this.maxIndex) {
/* 207 */       int i = this.maxIndex * 2;
/* 208 */       TypeContext[] arrayOfTypeContext = new TypeContext[i];
/* 209 */       System.arraycopy(this.stack, 0, arrayOfTypeContext, 0, this.maxIndex);
/* 210 */       this.maxIndex = i;
/* 211 */       this.stack = arrayOfTypeContext;
/*     */     }
/*     */ 
/* 216 */     TypeContext localTypeContext = this.stack[this.currentIndex];
/*     */ 
/* 218 */     if (localTypeContext == null) {
/* 219 */       localTypeContext = new TypeContext();
/* 220 */       this.stack[this.currentIndex] = localTypeContext;
/*     */     }
/*     */ 
/* 225 */     localTypeContext.set(this.newCode, paramContextElement);
/*     */ 
/* 229 */     traceln(toTrialString(localTypeContext));
/*     */ 
/* 233 */     return localTypeContext;
/*     */   }
/*     */ 
/*     */   public TypeContext pop(boolean paramBoolean)
/*     */   {
/* 242 */     if (this.currentIndex < 0) {
/* 243 */       throw new CompilerError("Nothing on stack!");
/*     */     }
/*     */ 
/* 246 */     this.newCode = this.stack[this.currentIndex].getCode();
/* 247 */     traceln(toResultString(this.stack[this.currentIndex], paramBoolean, false));
/*     */ 
/* 249 */     Type localType = this.stack[this.currentIndex].getCandidateType();
/* 250 */     if (localType != null)
/*     */     {
/* 254 */       if (paramBoolean)
/* 255 */         localType.setStatus(1);
/*     */       else {
/* 257 */         localType.setStatus(2);
/*     */       }
/*     */     }
/*     */ 
/* 261 */     this.currentIndex -= 1;
/*     */ 
/* 263 */     if (this.currentIndex < 0)
/*     */     {
/* 268 */       if (paramBoolean) {
/* 269 */         Type.updateAllInvalidTypes(this);
/*     */       }
/* 271 */       return null;
/*     */     }
/* 273 */     return this.stack[this.currentIndex];
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 281 */     return this.currentIndex + 1;
/*     */   }
/*     */ 
/*     */   public TypeContext getContext(int paramInt)
/*     */   {
/* 289 */     if (this.currentIndex < paramInt) {
/* 290 */       throw new Error("Index out of range");
/*     */     }
/* 292 */     return this.stack[paramInt];
/*     */   }
/*     */ 
/*     */   public TypeContext getContext()
/*     */   {
/* 300 */     if (this.currentIndex < 0) {
/* 301 */       throw new Error("Nothing on stack!");
/*     */     }
/* 303 */     return this.stack[this.currentIndex];
/*     */   }
/*     */ 
/*     */   public boolean isParentAValue()
/*     */   {
/* 311 */     if (this.currentIndex > 0) {
/* 312 */       return this.stack[(this.currentIndex - 1)].isValue();
/*     */     }
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */   public TypeContext getParentContext()
/*     */   {
/* 323 */     if (this.currentIndex > 0) {
/* 324 */       return this.stack[(this.currentIndex - 1)];
/*     */     }
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */   public String getContextCodeString()
/*     */   {
/* 335 */     if (this.currentIndex >= 0) {
/* 336 */       return CODE_NAMES[this.newCode];
/*     */     }
/* 338 */     return CODE_NAMES[0];
/*     */   }
/*     */ 
/*     */   public static String getContextCodeString(int paramInt)
/*     */   {
/* 346 */     return CODE_NAMES[paramInt];
/*     */   }
/*     */ 
/*     */   private String toTrialString(TypeContext paramTypeContext) {
/* 350 */     int i = paramTypeContext.getCode();
/* 351 */     if ((i != 2) && (i != 6)) {
/* 352 */       return paramTypeContext.toString() + " (trying " + paramTypeContext.getTypeDescription() + ")";
/*     */     }
/* 354 */     return paramTypeContext.toString();
/*     */   }
/*     */ 
/*     */   private String toResultString(TypeContext paramTypeContext, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 359 */     int i = paramTypeContext.getCode();
/* 360 */     if ((i != 2) && (i != 6)) {
/* 361 */       if (paramBoolean1) {
/* 362 */         String str = paramTypeContext.toString() + " --> " + paramTypeContext.getTypeDescription();
/* 363 */         if (paramBoolean2) {
/* 364 */           return str + " [Previously mapped]";
/*     */         }
/* 366 */         return str;
/*     */       }
/*     */ 
/*     */     }
/* 370 */     else if (paramBoolean1) {
/* 371 */       return paramTypeContext.toString() + " --> [Mapped]";
/*     */     }
/*     */ 
/* 374 */     return paramTypeContext.toString() + " [Did not map]";
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 378 */     for (int i = 0; i < this.stack.length; i++)
/* 379 */       if (this.stack[i] != null) this.stack[i].destroy();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.ContextStack
 * JD-Core Version:    0.6.2
 */