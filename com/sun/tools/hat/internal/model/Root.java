/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ 
/*     */ public class Root
/*     */ {
/*     */   private long id;
/*     */   private long refererId;
/*  52 */   private int index = -1;
/*     */   private int type;
/*     */   private String description;
/*  55 */   private JavaHeapObject referer = null;
/*  56 */   private StackTrace stackTrace = null;
/*     */   public static final int INVALID_TYPE = 0;
/*     */   public static final int UNKNOWN = 1;
/*     */   public static final int SYSTEM_CLASS = 2;
/*     */   public static final int NATIVE_LOCAL = 3;
/*     */   public static final int NATIVE_STATIC = 4;
/*     */   public static final int THREAD_BLOCK = 5;
/*     */   public static final int BUSY_MONITOR = 6;
/*     */   public static final int JAVA_LOCAL = 7;
/*     */   public static final int NATIVE_STACK = 8;
/*     */   public static final int JAVA_STATIC = 9;
/*     */ 
/*     */   public Root(long paramLong1, long paramLong2, int paramInt, String paramString)
/*     */   {
/*  74 */     this(paramLong1, paramLong2, paramInt, paramString, null);
/*     */   }
/*     */ 
/*     */   public Root(long paramLong1, long paramLong2, int paramInt, String paramString, StackTrace paramStackTrace)
/*     */   {
/*  80 */     this.id = paramLong1;
/*  81 */     this.refererId = paramLong2;
/*  82 */     this.type = paramInt;
/*  83 */     this.description = paramString;
/*  84 */     this.stackTrace = paramStackTrace;
/*     */   }
/*     */ 
/*     */   public long getId() {
/*  88 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getIdString() {
/*  92 */     return Misc.toHex(this.id);
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/*  96 */     if ("".equals(this.description)) {
/*  97 */       return getTypeName() + " Reference";
/*     */     }
/*  99 */     return this.description;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 108 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getTypeName() {
/* 112 */     switch (this.type) { case 0:
/* 113 */       return "Invalid (?!?)";
/*     */     case 1:
/* 114 */       return "Unknown";
/*     */     case 2:
/* 115 */       return "System Class";
/*     */     case 3:
/* 116 */       return "JNI Local";
/*     */     case 4:
/* 117 */       return "JNI Global";
/*     */     case 5:
/* 118 */       return "Thread Block";
/*     */     case 6:
/* 119 */       return "Busy Monitor";
/*     */     case 7:
/* 120 */       return "Java Local";
/*     */     case 8:
/* 121 */       return "Native Stack (possibly Java local)";
/*     */     case 9:
/* 122 */       return "Java Static"; }
/* 123 */     return "??";
/*     */   }
/*     */ 
/*     */   public Root mostInteresting(Root paramRoot)
/*     */   {
/* 131 */     if (paramRoot.type > this.type) {
/* 132 */       return paramRoot;
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   public JavaHeapObject getReferer()
/*     */   {
/* 143 */     return this.referer;
/*     */   }
/*     */ 
/*     */   public StackTrace getStackTrace()
/*     */   {
/* 151 */     return this.stackTrace;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 158 */     return this.index;
/*     */   }
/*     */ 
/*     */   void resolve(Snapshot paramSnapshot) {
/* 162 */     if (this.refererId != 0L) {
/* 163 */       this.referer = paramSnapshot.findThing(this.refererId);
/*     */     }
/* 165 */     if (this.stackTrace != null)
/* 166 */       this.stackTrace.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */   void setIndex(int paramInt)
/*     */   {
/* 171 */     this.index = paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.Root
 * JD-Core Version:    0.6.2
 */