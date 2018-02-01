/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class JavaHeapObject extends JavaThing
/*     */ {
/*  63 */   private JavaThing[] referers = null;
/*  64 */   private int referersLen = 0;
/*     */ 
/*     */   public abstract JavaClass getClazz();
/*     */ 
/*     */   public abstract int getSize();
/*     */ 
/*     */   public abstract long getId();
/*     */ 
/*     */   public void resolve(Snapshot paramSnapshot)
/*     */   {
/*  75 */     StackTrace localStackTrace = paramSnapshot.getSiteTrace(this);
/*  76 */     if (localStackTrace != null)
/*  77 */       localStackTrace.resolve(paramSnapshot);
/*     */   }
/*     */ 
/*     */   void setupReferers()
/*     */   {
/*  87 */     if (this.referersLen > 1)
/*     */     {
/*  89 */       HashMap localHashMap = new HashMap();
/*  90 */       for (int i = 0; i < this.referersLen; i++) {
/*  91 */         if (localHashMap.get(this.referers[i]) == null) {
/*  92 */           localHashMap.put(this.referers[i], this.referers[i]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  97 */       this.referers = new JavaThing[localHashMap.size()];
/*  98 */       localHashMap.keySet().toArray(this.referers);
/*     */     }
/* 100 */     this.referersLen = -1;
/*     */   }
/*     */ 
/*     */   public String getIdString()
/*     */   {
/* 108 */     return Misc.toHex(getId());
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 112 */     return getClazz().getName() + "@" + getIdString();
/*     */   }
/*     */ 
/*     */   public StackTrace getAllocatedFrom()
/*     */   {
/* 120 */     return getClazz().getSiteTrace(this);
/*     */   }
/*     */ 
/*     */   public boolean isNew() {
/* 124 */     return getClazz().isNew(this);
/*     */   }
/*     */ 
/*     */   void setNew(boolean paramBoolean) {
/* 128 */     getClazz().setNew(this, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor)
/*     */   {
/* 135 */     paramJavaHeapObjectVisitor.visit(getClazz());
/*     */   }
/*     */ 
/*     */   void addReferenceFrom(JavaHeapObject paramJavaHeapObject) {
/* 139 */     if (this.referersLen == 0) {
/* 140 */       this.referers = new JavaThing[1];
/* 141 */     } else if (this.referersLen == this.referers.length) {
/* 142 */       JavaThing[] arrayOfJavaThing = new JavaThing[3 * (this.referersLen + 1) / 2];
/* 143 */       System.arraycopy(this.referers, 0, arrayOfJavaThing, 0, this.referersLen);
/* 144 */       this.referers = arrayOfJavaThing;
/*     */     }
/* 146 */     this.referers[(this.referersLen++)] = paramJavaHeapObject;
/*     */   }
/*     */ 
/*     */   void addReferenceFromRoot(Root paramRoot)
/*     */   {
/* 153 */     getClazz().addReferenceFromRoot(paramRoot, this);
/*     */   }
/*     */ 
/*     */   public Root getRoot()
/*     */   {
/* 161 */     return getClazz().getRoot(this);
/*     */   }
/*     */ 
/*     */   public Enumeration getReferers()
/*     */   {
/* 170 */     if (this.referersLen != -1) {
/* 171 */       throw new RuntimeException("not resolved: " + getIdString());
/*     */     }
/* 173 */     return new Enumeration()
/*     */     {
/* 175 */       private int num = 0;
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 178 */         return (JavaHeapObject.this.referers != null) && (this.num < JavaHeapObject.this.referers.length);
/*     */       }
/*     */ 
/*     */       public Object nextElement() {
/* 182 */         return JavaHeapObject.this.referers[(this.num++)];
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean refersOnlyWeaklyTo(Snapshot paramSnapshot, JavaThing paramJavaThing)
/*     */   {
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */   public String describeReferenceTo(JavaThing paramJavaThing, Snapshot paramSnapshot)
/*     */   {
/* 200 */     return "??";
/*     */   }
/*     */ 
/*     */   public boolean isHeapAllocated() {
/* 204 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaHeapObject
 * JD-Core Version:    0.6.2
 */