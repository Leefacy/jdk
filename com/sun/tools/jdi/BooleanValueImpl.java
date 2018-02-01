/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.BooleanValue;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class BooleanValueImpl extends PrimitiveValueImpl
/*     */   implements BooleanValue
/*     */ {
/*     */   private boolean value;
/*     */ 
/*     */   BooleanValueImpl(VirtualMachine paramVirtualMachine, boolean paramBoolean)
/*     */   {
/*  35 */     super(paramVirtualMachine);
/*     */ 
/*  37 */     this.value = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if ((paramObject != null) && ((paramObject instanceof BooleanValue)))
/*     */     {
/*  43 */       return (this.value == ((BooleanValue)paramObject).value()) && 
/*  43 */         (super
/*  43 */         .equals(paramObject));
/*     */     }
/*     */ 
/*  45 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  53 */     return intValue();
/*     */   }
/*     */ 
/*     */   public Type type() {
/*  57 */     return this.vm.theBooleanType();
/*     */   }
/*     */ 
/*     */   public boolean value() {
/*  61 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean booleanValue() {
/*  65 */     return this.value;
/*     */   }
/*     */ 
/*     */   public byte byteValue() {
/*  69 */     return (byte)(this.value ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public char charValue() {
/*  73 */     return (char)(this.value ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public short shortValue() {
/*  77 */     return (short)(this.value ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public int intValue() {
/*  81 */     return this.value ? 1 : 0;
/*     */   }
/*     */ 
/*     */   public long longValue() {
/*  85 */     return this.value ? 1 : 0;
/*     */   }
/*     */ 
/*     */   public float floatValue() {
/*  89 */     return (float)(this.value ? 1.0D : 0.0D);
/*     */   }
/*     */ 
/*     */   public double doubleValue() {
/*  93 */     return this.value ? 1.0D : 0.0D;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  97 */     return "" + this.value;
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 101 */     return 90;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.BooleanValueImpl
 * JD-Core Version:    0.6.2
 */