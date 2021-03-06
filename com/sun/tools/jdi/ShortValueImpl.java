/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.ShortValue;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class ShortValueImpl extends PrimitiveValueImpl
/*     */   implements ShortValue
/*     */ {
/*     */   private short value;
/*     */ 
/*     */   ShortValueImpl(VirtualMachine paramVirtualMachine, short paramShort)
/*     */   {
/*  35 */     super(paramVirtualMachine);
/*     */ 
/*  37 */     this.value = paramShort;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if ((paramObject != null) && ((paramObject instanceof ShortValue)))
/*     */     {
/*  43 */       return (this.value == ((ShortValue)paramObject).value()) && 
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
/*     */   public int compareTo(ShortValue paramShortValue) {
/*  57 */     int i = paramShortValue.value();
/*  58 */     return value() - i;
/*     */   }
/*     */ 
/*     */   public Type type() {
/*  62 */     return this.vm.theShortType();
/*     */   }
/*     */ 
/*     */   public short value() {
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
/*  78 */     return (char)this.value;
/*     */   }
/*     */ 
/*     */   public short shortValue() {
/*  82 */     return this.value;
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
/*     */   byte checkedByteValue() throws InvalidTypeException {
/* 102 */     if ((this.value > 127) || (this.value < -128)) {
/* 103 */       throw new InvalidTypeException("Can't convert " + this.value + " to byte");
/*     */     }
/* 105 */     return super.checkedByteValue();
/*     */   }
/*     */ 
/*     */   char checkedCharValue() throws InvalidTypeException
/*     */   {
/* 110 */     if ((this.value > 65535) || (this.value < 0)) {
/* 111 */       throw new InvalidTypeException("Can't convert " + this.value + " to char");
/*     */     }
/* 113 */     return super.checkedCharValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return "" + this.value;
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 122 */     return 83;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ShortValueImpl
 * JD-Core Version:    0.6.2
 */