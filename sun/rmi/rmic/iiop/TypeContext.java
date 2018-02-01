/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ class TypeContext
/*     */ {
/* 444 */   private int code = 0;
/* 445 */   private ContextElement element = null;
/* 446 */   private boolean isValue = false;
/*     */ 
/*     */   public void set(int paramInt, ContextElement paramContextElement)
/*     */   {
/* 388 */     this.code = paramInt;
/* 389 */     this.element = paramContextElement;
/* 390 */     if ((paramContextElement instanceof ValueType))
/* 391 */       this.isValue = true;
/*     */     else
/* 393 */       this.isValue = false;
/*     */   }
/*     */ 
/*     */   public int getCode()
/*     */   {
/* 398 */     return this.code;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 402 */     return this.element.getElementName();
/*     */   }
/*     */ 
/*     */   public Type getCandidateType() {
/* 406 */     if ((this.element instanceof Type)) {
/* 407 */       return (Type)this.element;
/*     */     }
/* 409 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 414 */     if ((this.element instanceof Type)) {
/* 415 */       return ((Type)this.element).getTypeDescription();
/*     */     }
/* 417 */     return "[unknown type]";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 422 */     if (this.element != null) {
/* 423 */       return ContextStack.getContextCodeString(this.code) + this.element.getElementName();
/*     */     }
/* 425 */     return ContextStack.getContextCodeString(this.code) + "null";
/*     */   }
/*     */ 
/*     */   public boolean isValue()
/*     */   {
/* 430 */     return this.isValue;
/*     */   }
/*     */ 
/*     */   public boolean isConstant() {
/* 434 */     return this.code == 7;
/*     */   }
/*     */ 
/*     */   public void destroy() {
/* 438 */     if ((this.element instanceof Type)) {
/* 439 */       ((Type)this.element).destroy();
/*     */     }
/* 441 */     this.element = null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.TypeContext
 * JD-Core Version:    0.6.2
 */