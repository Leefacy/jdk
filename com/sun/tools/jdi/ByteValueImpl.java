/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ByteValue;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ public class ByteValueImpl extends PrimitiveValueImpl
/*     */   implements ByteValue
/*     */ {
/*     */   private byte value;
/*     */ 
/*     */   ByteValueImpl(VirtualMachine paramVirtualMachine, byte paramByte)
/*     */   {
/*  35 */     super(paramVirtualMachine);
/*     */ 
/*  37 */     this.value = paramByte;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if ((paramObject != null) && ((paramObject instanceof ByteValue)))
/*     */     {
/*  43 */       return (this.value == ((ByteValue)paramObject).value()) && 
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
/*     */   public int compareTo(ByteValue paramByteValue) {
/*  57 */     int i = paramByteValue.value();
/*  58 */     return value() - i;
/*     */   }
/*     */ 
/*     */   public Type type()
/*     */   {
/*  63 */     return this.vm.theByteType();
/*     */   }
/*     */ 
/*     */   public byte value() {
/*  67 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean booleanValue() {
/*  71 */     return this.value != 0;
/*     */   }
/*     */ 
/*     */   public byte byteValue() {
/*  75 */     return this.value;
/*     */   }
/*     */ 
/*     */   public char charValue() {
/*  79 */     return (char)this.value;
/*     */   }
/*     */ 
/*     */   public short shortValue() {
/*  83 */     return (short)this.value;
/*     */   }
/*     */ 
/*     */   public int intValue() {
/*  87 */     return this.value;
/*     */   }
/*     */ 
/*     */   public long longValue() {
/*  91 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float floatValue() {
/*  95 */     return this.value;
/*     */   }
/*     */ 
/*     */   public double doubleValue() {
/*  99 */     return this.value;
/*     */   }
/*     */ 
/*     */   char checkedCharValue() throws InvalidTypeException {
/* 103 */     if ((this.value > 65535) || (this.value < 0)) {
/* 104 */       throw new InvalidTypeException("Can't convert " + this.value + " to char");
/*     */     }
/* 106 */     return super.checkedCharValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 111 */     return "" + this.value;
/*     */   }
/*     */ 
/*     */   byte typeValueKey() {
/* 115 */     return 66;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ByteValueImpl
 * JD-Core Version:    0.6.2
 */