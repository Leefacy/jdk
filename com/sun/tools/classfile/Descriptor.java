/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Descriptor
/*     */ {
/*     */   public final int index;
/*     */   private int count;
/*     */ 
/*     */   public Descriptor(ClassReader paramClassReader)
/*     */     throws IOException
/*     */   {
/*  67 */     this(paramClassReader.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public Descriptor(int paramInt) {
/*  71 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public String getValue(ConstantPool paramConstantPool) throws ConstantPoolException
/*     */   {
/*  76 */     return paramConstantPool.getUTF8Value(this.index);
/*     */   }
/*     */ 
/*     */   public int getParameterCount(ConstantPool paramConstantPool) throws ConstantPoolException, Descriptor.InvalidDescriptor
/*     */   {
/*  81 */     String str = getValue(paramConstantPool);
/*  82 */     int i = str.indexOf(")");
/*  83 */     if (i == -1)
/*  84 */       throw new InvalidDescriptor(str);
/*  85 */     parse(str, 0, i + 1);
/*  86 */     return this.count;
/*     */   }
/*     */ 
/*     */   public String getParameterTypes(ConstantPool paramConstantPool)
/*     */     throws ConstantPoolException, Descriptor.InvalidDescriptor
/*     */   {
/*  92 */     String str = getValue(paramConstantPool);
/*  93 */     int i = str.indexOf(")");
/*  94 */     if (i == -1)
/*  95 */       throw new InvalidDescriptor(str);
/*  96 */     return parse(str, 0, i + 1);
/*     */   }
/*     */ 
/*     */   public String getReturnType(ConstantPool paramConstantPool) throws ConstantPoolException, Descriptor.InvalidDescriptor
/*     */   {
/* 101 */     String str = getValue(paramConstantPool);
/* 102 */     int i = str.indexOf(")");
/* 103 */     if (i == -1)
/* 104 */       throw new InvalidDescriptor(str);
/* 105 */     return parse(str, i + 1, str.length());
/*     */   }
/*     */ 
/*     */   public String getFieldType(ConstantPool paramConstantPool) throws ConstantPoolException, Descriptor.InvalidDescriptor
/*     */   {
/* 110 */     String str = getValue(paramConstantPool);
/* 111 */     return parse(str, 0, str.length());
/*     */   }
/*     */ 
/*     */   private String parse(String paramString, int paramInt1, int paramInt2) throws Descriptor.InvalidDescriptor
/*     */   {
/* 116 */     int i = paramInt1;
/* 117 */     StringBuilder localStringBuilder = new StringBuilder();
/* 118 */     int j = 0;
/* 119 */     this.count = 0;
/*     */ 
/* 121 */     while (i < paramInt2)
/*     */     {
/*     */       int k;
/*     */       String str;
/* 124 */       switch (k = paramString.charAt(i++)) {
/*     */       case '(':
/* 126 */         localStringBuilder.append('(');
/* 127 */         break;
/*     */       case ')':
/* 130 */         localStringBuilder.append(')');
/* 131 */         break;
/*     */       case '[':
/* 134 */         j++;
/* 135 */         break;
/*     */       case 'B':
/* 138 */         str = "byte";
/* 139 */         break;
/*     */       case 'C':
/* 142 */         str = "char";
/* 143 */         break;
/*     */       case 'D':
/* 146 */         str = "double";
/* 147 */         break;
/*     */       case 'F':
/* 150 */         str = "float";
/* 151 */         break;
/*     */       case 'I':
/* 154 */         str = "int";
/* 155 */         break;
/*     */       case 'J':
/* 158 */         str = "long";
/* 159 */         break;
/*     */       case 'L':
/* 162 */         int m = paramString.indexOf(';', i);
/* 163 */         if (m == -1)
/* 164 */           throw new InvalidDescriptor(paramString, i - 1);
/* 165 */         str = paramString.substring(i, m).replace('/', '.');
/* 166 */         i = m + 1;
/* 167 */         break;
/*     */       case 'S':
/* 170 */         str = "short";
/* 171 */         break;
/*     */       case 'Z':
/* 174 */         str = "boolean";
/* 175 */         break;
/*     */       case 'V':
/* 178 */         str = "void";
/* 179 */         break;
/*     */       case '*':
/*     */       case '+':
/*     */       case ',':
/*     */       case '-':
/*     */       case '.':
/*     */       case '/':
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case 'A':
/*     */       case 'E':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'K':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       default:
/* 182 */         throw new InvalidDescriptor(paramString, i - 1);
/*     */ 
/* 185 */         if ((localStringBuilder.length() > 1) && (localStringBuilder.charAt(0) == '('))
/* 186 */           localStringBuilder.append(", ");
/* 187 */         localStringBuilder.append(str);
/* 188 */         for (; j > 0; j--) {
/* 189 */           localStringBuilder.append("[]");
/*     */         }
/* 191 */         this.count += 1;
/*     */       }
/*     */     }
/* 194 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static class InvalidDescriptor extends DescriptorException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public final String desc;
/*     */     public final int index;
/*     */ 
/*     */     InvalidDescriptor(String paramString)
/*     */     {
/*  43 */       this.desc = paramString;
/*  44 */       this.index = -1;
/*     */     }
/*     */ 
/*     */     InvalidDescriptor(String paramString, int paramInt) {
/*  48 */       this.desc = paramString;
/*  49 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/*  55 */       if (this.index == -1) {
/*  56 */         return "invalid descriptor \"" + this.desc + "\"";
/*     */       }
/*  58 */       return "descriptor is invalid at offset " + this.index + " in \"" + this.desc + "\"";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Descriptor
 * JD-Core Version:    0.6.2
 */