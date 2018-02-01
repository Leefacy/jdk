/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BootstrapMethods_attribute extends Attribute
/*    */ {
/*    */   public final BootstrapMethodSpecifier[] bootstrap_method_specifiers;
/*    */ 
/*    */   BootstrapMethods_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, AttributeException
/*    */   {
/* 44 */     super(paramInt1, paramInt2);
/* 45 */     int i = paramClassReader.readUnsignedShort();
/* 46 */     this.bootstrap_method_specifiers = new BootstrapMethodSpecifier[i];
/* 47 */     for (int j = 0; j < this.bootstrap_method_specifiers.length; j++)
/* 48 */       this.bootstrap_method_specifiers[j] = new BootstrapMethodSpecifier(paramClassReader);
/*    */   }
/*    */ 
/*    */   public BootstrapMethods_attribute(int paramInt, BootstrapMethodSpecifier[] paramArrayOfBootstrapMethodSpecifier) {
/* 52 */     super(paramInt, length(paramArrayOfBootstrapMethodSpecifier));
/* 53 */     this.bootstrap_method_specifiers = paramArrayOfBootstrapMethodSpecifier;
/*    */   }
/*    */ 
/*    */   public static int length(BootstrapMethodSpecifier[] paramArrayOfBootstrapMethodSpecifier) {
/* 57 */     int i = 2;
/* 58 */     for (BootstrapMethodSpecifier localBootstrapMethodSpecifier : paramArrayOfBootstrapMethodSpecifier)
/* 59 */       i += localBootstrapMethodSpecifier.length();
/* 60 */     return i;
/*    */   }
/*    */ 
/*    */   public <R, P> R accept(Attribute.Visitor<R, P> paramVisitor, P paramP)
/*    */   {
/* 65 */     return paramVisitor.visitBootstrapMethods(this, paramP);
/*    */   }
/*    */   public static class BootstrapMethodSpecifier {
/*    */     public int bootstrap_method_ref;
/*    */     public int[] bootstrap_arguments;
/*    */ 
/*    */     public BootstrapMethodSpecifier(int paramInt, int[] paramArrayOfInt) {
/* 73 */       this.bootstrap_method_ref = paramInt;
/* 74 */       this.bootstrap_arguments = paramArrayOfInt;
/*    */     }
/*    */     BootstrapMethodSpecifier(ClassReader paramClassReader) throws IOException {
/* 77 */       this.bootstrap_method_ref = paramClassReader.readUnsignedShort();
/* 78 */       int i = paramClassReader.readUnsignedShort();
/* 79 */       this.bootstrap_arguments = new int[i];
/* 80 */       for (int j = 0; j < this.bootstrap_arguments.length; j++)
/* 81 */         this.bootstrap_arguments[j] = paramClassReader.readUnsignedShort();
/*    */     }
/*    */ 
/*    */     int length()
/*    */     {
/* 87 */       return 4 + this.bootstrap_arguments.length * 2;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.BootstrapMethods_attribute
 * JD-Core Version:    0.6.2
 */