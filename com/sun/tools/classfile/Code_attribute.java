/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class Code_attribute extends Attribute
/*     */ {
/*     */   public final int max_stack;
/*     */   public final int max_locals;
/*     */   public final int code_length;
/*     */   public final byte[] code;
/*     */   public final int exception_table_length;
/*     */   public final Exception_data[] exception_table;
/*     */   public final Attributes attributes;
/*     */ 
/*     */   Code_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*     */     throws IOException, ConstantPoolException
/*     */   {
/*  58 */     super(paramInt1, paramInt2);
/*  59 */     this.max_stack = paramClassReader.readUnsignedShort();
/*  60 */     this.max_locals = paramClassReader.readUnsignedShort();
/*  61 */     this.code_length = paramClassReader.readInt();
/*  62 */     this.code = new byte[this.code_length];
/*  63 */     paramClassReader.readFully(this.code);
/*  64 */     this.exception_table_length = paramClassReader.readUnsignedShort();
/*  65 */     this.exception_table = new Exception_data[this.exception_table_length];
/*  66 */     for (int i = 0; i < this.exception_table_length; i++)
/*  67 */       this.exception_table[i] = new Exception_data(paramClassReader);
/*  68 */     this.attributes = new Attributes(paramClassReader);
/*     */   }
/*     */ 
/*     */   public int getByte(int paramInt) throws Code_attribute.InvalidIndex {
/*  72 */     if ((paramInt < 0) || (paramInt >= this.code.length))
/*  73 */       throw new InvalidIndex(paramInt);
/*  74 */     return this.code[paramInt];
/*     */   }
/*     */ 
/*     */   public int getUnsignedByte(int paramInt) throws Code_attribute.InvalidIndex {
/*  78 */     if ((paramInt < 0) || (paramInt >= this.code.length))
/*  79 */       throw new InvalidIndex(paramInt);
/*  80 */     return this.code[paramInt] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getShort(int paramInt) throws Code_attribute.InvalidIndex {
/*  84 */     if ((paramInt < 0) || (paramInt + 1 >= this.code.length))
/*  85 */       throw new InvalidIndex(paramInt);
/*  86 */     return this.code[paramInt] << 8 | this.code[(paramInt + 1)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getUnsignedShort(int paramInt) throws Code_attribute.InvalidIndex {
/*  90 */     if ((paramInt < 0) || (paramInt + 1 >= this.code.length))
/*  91 */       throw new InvalidIndex(paramInt);
/*  92 */     return (this.code[paramInt] << 8 | this.code[(paramInt + 1)] & 0xFF) & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt) throws Code_attribute.InvalidIndex {
/*  96 */     if ((paramInt < 0) || (paramInt + 3 >= this.code.length))
/*  97 */       throw new InvalidIndex(paramInt);
/*  98 */     return getShort(paramInt) << 16 | getShort(paramInt + 2) & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 102 */     return paramVisitor.visitCode(this, paramD);
/*     */   }
/*     */ 
/*     */   public Iterable<Instruction> getInstructions() {
/* 106 */     return new Iterable() {
/*     */       public Iterator<Instruction> iterator() {
/* 108 */         return new Iterator()
/*     */         {
/* 128 */           Instruction current = null;
/* 129 */           int pc = 0;
/* 130 */           Instruction next = new Instruction(Code_attribute.this.code, this.pc);
/*     */ 
/*     */           public boolean hasNext()
/*     */           {
/* 111 */             return this.next != null;
/*     */           }
/*     */ 
/*     */           public Instruction next() {
/* 115 */             if (this.next == null) {
/* 116 */               throw new NoSuchElementException();
/*     */             }
/* 118 */             this.current = this.next;
/* 119 */             this.pc += this.current.length();
/* 120 */             this.next = (this.pc < Code_attribute.this.code.length ? new Instruction(Code_attribute.this.code, this.pc) : null);
/* 121 */             return this.current;
/*     */           }
/*     */ 
/*     */           public void remove() {
/* 125 */             throw new UnsupportedOperationException("Not supported.");
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static class Exception_data
/*     */   {
/*     */     public final int start_pc;
/*     */     public final int end_pc;
/*     */     public final int handler_pc;
/*     */     public final int catch_type;
/*     */ 
/*     */     Exception_data(ClassReader paramClassReader)
/*     */       throws IOException
/*     */     {
/* 148 */       this.start_pc = paramClassReader.readUnsignedShort();
/* 149 */       this.end_pc = paramClassReader.readUnsignedShort();
/* 150 */       this.handler_pc = paramClassReader.readUnsignedShort();
/* 151 */       this.catch_type = paramClassReader.readUnsignedShort();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InvalidIndex extends AttributeException
/*     */   {
/*     */     private static final long serialVersionUID = -8904527774589382802L;
/*     */     public final int index;
/*     */ 
/*     */     InvalidIndex(int paramInt)
/*     */     {
/*  44 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/*  50 */       return "invalid index " + this.index + " in Code attribute";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Code_attribute
 * JD-Core Version:    0.6.2
 */