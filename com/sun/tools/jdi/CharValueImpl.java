/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.CharValue;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class CharValueImpl extends PrimitiveValueImpl
/*     */   implements CharValue
/*     */ {
/*     */   private char value;
/*     */ 
/*     */   CharValueImpl(VirtualMachine paramVirtualMachine, char paramChar)
/*     */   {
/*  35 */     super(paramVirtualMachine);
/*     */ 
/*  37 */     this.value = paramChar;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if ((paramObject != null) && ((paramObject instanceof CharValue)))
/*     */     {
/*  43 */       return (this.value == ((CharValue)paramObject).value()) && 
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
/*     */   public int compareTo(CharValue paramCharValue) {
/*  57 */     int i = paramCharValue.value();
/*  58 */     return value() - i;
/*     */   }
/*     */ 
/*     */   public Type type() {
/*  62 */     return this.vm.theCharType();
/*     */   }
/*     */ 
/*     */   public char value() {
/*  66 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean booleanValue() {
/*  70 */     return this.value != 0;
/*     */   }
/*     */ 
/*     */   public byte byteValue() {
/*  74 */     return (byte)this.value;
/*     */   }
/*     */ 
/*     */   public char charValue() {
/*  78 */     return this.value;
/*     */   }
/*     */ 
/*     */   public short shortValue() {
/*  82 */     return (short)this.value;
/*     */   }
/*     */ 
/*     */   public int intValue() {
/*  86 */     return this.value;
/*     */   }
/*     */ 
/*     */   public long longValue() {
/*  90 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float floatValue() {
/*  94 */     return this.value;
/*     */   }
/*     */ 
/*     */   public double doubleValue() {
/*  98 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 102 */     return "" + this.value;
/*     */   }
/*     */ 
/*     */   byte checkedByteValue() throws InvalidTypeException
/*     */   {
/* 107 */     if (this.value > '') {
/* 108 */       throw new InvalidTypeException("Can't convert " + this.value + " to byte");
/*     */     }
/* 110 */     return super.checkedByteValue();
/*     */   }
/*     */ 
/*     */   short checkedShortValue()
/*     */     throws InvalidTypeException
/*     */   {
/* 116 */     if (this.value > '翿') {
/* 117 */       throw new InvalidTypeException("Can't convert " + this.value + " to short");
/*     */     }
/* 119 */     return super.checkedShortValue();
/*     */   }
/*     */ 
/*     */   byte typeValueKey()
/*     */   {
/* 124 */     return 67;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.CharValueImpl
 * JD-Core Version:    0.6.2
 */