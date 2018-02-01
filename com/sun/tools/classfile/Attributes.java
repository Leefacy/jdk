/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Attributes
/*     */   implements Iterable<Attribute>
/*     */ {
/*     */   public final Attribute[] attrs;
/*     */   public final Map<String, Attribute> map;
/*     */ 
/*     */   Attributes(ClassReader paramClassReader)
/*     */     throws IOException
/*     */   {
/*  42 */     this.map = new HashMap();
/*  43 */     int i = paramClassReader.readUnsignedShort();
/*  44 */     this.attrs = new Attribute[i];
/*  45 */     for (int j = 0; j < i; j++) {
/*  46 */       Attribute localAttribute = Attribute.read(paramClassReader);
/*  47 */       this.attrs[j] = localAttribute;
/*     */       try {
/*  49 */         this.map.put(localAttribute.getName(paramClassReader.getConstantPool()), localAttribute);
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attributes(ConstantPool paramConstantPool, Attribute[] paramArrayOfAttribute) {
/*  57 */     this.attrs = paramArrayOfAttribute;
/*  58 */     this.map = new HashMap();
/*  59 */     for (int i = 0; i < paramArrayOfAttribute.length; i++) {
/*  60 */       Attribute localAttribute = paramArrayOfAttribute[i];
/*     */       try {
/*  62 */         this.map.put(localAttribute.getName(paramConstantPool), localAttribute);
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<Attribute> iterator() {
/*  70 */     return Arrays.asList(this.attrs).iterator();
/*     */   }
/*     */ 
/*     */   public Attribute get(int paramInt) {
/*  74 */     return this.attrs[paramInt];
/*     */   }
/*     */ 
/*     */   public Attribute get(String paramString) {
/*  78 */     return (Attribute)this.map.get(paramString);
/*     */   }
/*     */ 
/*     */   public int getIndex(ConstantPool paramConstantPool, String paramString) {
/*  82 */     for (int i = 0; i < this.attrs.length; i++) {
/*  83 */       Attribute localAttribute = this.attrs[i];
/*     */       try {
/*  85 */         if ((localAttribute != null) && (localAttribute.getName(paramConstantPool).equals(paramString)))
/*  86 */           return i;
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/*     */       }
/*     */     }
/*  91 */     return -1;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  95 */     return this.attrs.length;
/*     */   }
/*     */ 
/*     */   public int byteLength() {
/*  99 */     int i = 2;
/* 100 */     for (Attribute localAttribute : this.attrs)
/* 101 */       i += localAttribute.byteLength();
/* 102 */     return i;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Attributes
 * JD-Core Version:    0.6.2
 */