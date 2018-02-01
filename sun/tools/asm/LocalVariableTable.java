/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ final class LocalVariableTable
/*     */ {
/*  43 */   LocalVariable[] locals = new LocalVariable[8];
/*     */   int len;
/*     */ 
/*     */   void define(MemberDefinition paramMemberDefinition, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  50 */     if (paramInt2 >= paramInt3) {
/*  51 */       return;
/*     */     }
/*  53 */     for (int i = 0; i < this.len; i++) {
/*  54 */       if ((this.locals[i].field == paramMemberDefinition) && (this.locals[i].slot == paramInt1) && (paramInt2 <= this.locals[i].to) && (paramInt3 >= this.locals[i].from))
/*     */       {
/*  56 */         this.locals[i].from = Math.min(this.locals[i].from, paramInt2);
/*  57 */         this.locals[i].to = Math.max(this.locals[i].to, paramInt3);
/*  58 */         return;
/*     */       }
/*     */     }
/*  61 */     if (this.len == this.locals.length) {
/*  62 */       LocalVariable[] arrayOfLocalVariable = new LocalVariable[this.len * 2];
/*  63 */       System.arraycopy(this.locals, 0, arrayOfLocalVariable, 0, this.len);
/*  64 */       this.locals = arrayOfLocalVariable;
/*     */     }
/*  66 */     this.locals[(this.len++)] = new LocalVariable(paramMemberDefinition, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   private void trim_ranges()
/*     */   {
/*  81 */     for (int i = 0; i < this.len; i++)
/*  82 */       for (int j = i + 1; j < this.len; j++)
/*  83 */         if ((this.locals[i].field.getName() == this.locals[j].field.getName()) && (this.locals[i].from <= this.locals[j].to) && (this.locals[i].to >= this.locals[j].from))
/*     */         {
/*  88 */           if (this.locals[i].slot < this.locals[j].slot) {
/*  89 */             if (this.locals[i].from < this.locals[j].from) {
/*  90 */               this.locals[i].to = Math.min(this.locals[i].to, this.locals[j].from);
/*     */             }
/*     */ 
/*     */           }
/* 100 */           else if ((this.locals[i].slot > this.locals[j].slot) && 
/* 101 */             (this.locals[i].from > this.locals[j].from))
/* 102 */             this.locals[j].to = Math.min(this.locals[j].to, this.locals[i].from);
/*     */         }
/*     */   }
/*     */ 
/*     */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*     */     throws IOException
/*     */   {
/* 121 */     trim_ranges();
/* 122 */     paramDataOutputStream.writeShort(this.len);
/* 123 */     for (int i = 0; i < this.len; i++)
/*     */     {
/* 125 */       paramDataOutputStream.writeShort(this.locals[i].from);
/* 126 */       paramDataOutputStream.writeShort(this.locals[i].to - this.locals[i].from);
/* 127 */       paramDataOutputStream.writeShort(paramConstantPool.index(this.locals[i].field.getName().toString()));
/* 128 */       paramDataOutputStream.writeShort(paramConstantPool.index(this.locals[i].field.getType().getTypeSignature()));
/* 129 */       paramDataOutputStream.writeShort(this.locals[i].slot);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.LocalVariableTable
 * JD-Core Version:    0.6.2
 */