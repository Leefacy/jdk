/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ConstantPool
/*     */ {
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   private CPInfo[] pool;
/*     */ 
/*     */   ConstantPool(ClassReader paramClassReader)
/*     */     throws IOException, ConstantPool.InvalidEntry
/*     */   {
/* 167 */     int i = paramClassReader.readUnsignedShort();
/* 168 */     this.pool = new CPInfo[i];
/* 169 */     for (int j = 1; j < i; j++) {
/* 170 */       int k = paramClassReader.readUnsignedByte();
/* 171 */       switch (k) {
/*     */       case 7:
/* 173 */         this.pool[j] = new CONSTANT_Class_info(this, paramClassReader);
/* 174 */         break;
/*     */       case 6:
/* 177 */         this.pool[j] = new CONSTANT_Double_info(paramClassReader);
/* 178 */         j++;
/* 179 */         break;
/*     */       case 9:
/* 182 */         this.pool[j] = new CONSTANT_Fieldref_info(this, paramClassReader);
/* 183 */         break;
/*     */       case 4:
/* 186 */         this.pool[j] = new CONSTANT_Float_info(paramClassReader);
/* 187 */         break;
/*     */       case 3:
/* 190 */         this.pool[j] = new CONSTANT_Integer_info(paramClassReader);
/* 191 */         break;
/*     */       case 11:
/* 194 */         this.pool[j] = new CONSTANT_InterfaceMethodref_info(this, paramClassReader);
/* 195 */         break;
/*     */       case 18:
/* 198 */         this.pool[j] = new CONSTANT_InvokeDynamic_info(this, paramClassReader);
/* 199 */         break;
/*     */       case 5:
/* 202 */         this.pool[j] = new CONSTANT_Long_info(paramClassReader);
/* 203 */         j++;
/* 204 */         break;
/*     */       case 15:
/* 207 */         this.pool[j] = new CONSTANT_MethodHandle_info(this, paramClassReader);
/* 208 */         break;
/*     */       case 16:
/* 211 */         this.pool[j] = new CONSTANT_MethodType_info(this, paramClassReader);
/* 212 */         break;
/*     */       case 10:
/* 215 */         this.pool[j] = new CONSTANT_Methodref_info(this, paramClassReader);
/* 216 */         break;
/*     */       case 12:
/* 219 */         this.pool[j] = new CONSTANT_NameAndType_info(this, paramClassReader);
/* 220 */         break;
/*     */       case 8:
/* 223 */         this.pool[j] = new CONSTANT_String_info(this, paramClassReader);
/* 224 */         break;
/*     */       case 1:
/* 227 */         this.pool[j] = new CONSTANT_Utf8_info(paramClassReader);
/* 228 */         break;
/*     */       case 2:
/*     */       case 13:
/*     */       case 14:
/*     */       case 17:
/*     */       default:
/* 231 */         throw new InvalidEntry(j, k);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ConstantPool(CPInfo[] paramArrayOfCPInfo) {
/* 237 */     this.pool = paramArrayOfCPInfo;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 241 */     return this.pool.length;
/*     */   }
/*     */ 
/*     */   public int byteLength() {
/* 245 */     int i = 2;
/* 246 */     for (int j = 1; j < size(); ) {
/* 247 */       CPInfo localCPInfo = this.pool[j];
/* 248 */       i += localCPInfo.byteLength();
/* 249 */       j += localCPInfo.size();
/*     */     }
/* 251 */     return i;
/*     */   }
/*     */ 
/*     */   public CPInfo get(int paramInt) throws ConstantPool.InvalidIndex {
/* 255 */     if ((paramInt <= 0) || (paramInt >= this.pool.length))
/* 256 */       throw new InvalidIndex(paramInt);
/* 257 */     CPInfo localCPInfo = this.pool[paramInt];
/* 258 */     if (localCPInfo == null)
/*     */     {
/* 261 */       throw new InvalidIndex(paramInt);
/*     */     }
/* 263 */     return this.pool[paramInt];
/*     */   }
/*     */ 
/*     */   private CPInfo get(int paramInt1, int paramInt2) throws ConstantPool.InvalidIndex, ConstantPool.UnexpectedEntry {
/* 267 */     CPInfo localCPInfo = get(paramInt1);
/* 268 */     if (localCPInfo.getTag() != paramInt2)
/* 269 */       throw new UnexpectedEntry(paramInt1, paramInt2, localCPInfo.getTag());
/* 270 */     return localCPInfo;
/*     */   }
/*     */ 
/*     */   public CONSTANT_Utf8_info getUTF8Info(int paramInt) throws ConstantPool.InvalidIndex, ConstantPool.UnexpectedEntry {
/* 274 */     return (CONSTANT_Utf8_info)get(paramInt, 1);
/*     */   }
/*     */ 
/*     */   public CONSTANT_Class_info getClassInfo(int paramInt) throws ConstantPool.InvalidIndex, ConstantPool.UnexpectedEntry {
/* 278 */     return (CONSTANT_Class_info)get(paramInt, 7);
/*     */   }
/*     */ 
/*     */   public CONSTANT_NameAndType_info getNameAndTypeInfo(int paramInt) throws ConstantPool.InvalidIndex, ConstantPool.UnexpectedEntry {
/* 282 */     return (CONSTANT_NameAndType_info)get(paramInt, 12);
/*     */   }
/*     */ 
/*     */   public String getUTF8Value(int paramInt) throws ConstantPool.InvalidIndex, ConstantPool.UnexpectedEntry {
/* 286 */     return getUTF8Info(paramInt).value;
/*     */   }
/*     */ 
/*     */   public int getUTF8Index(String paramString) throws ConstantPool.EntryNotFound {
/* 290 */     for (int i = 1; i < this.pool.length; i++) {
/* 291 */       CPInfo localCPInfo = this.pool[i];
/* 292 */       if (((localCPInfo instanceof CONSTANT_Utf8_info)) && 
/* 293 */         (((CONSTANT_Utf8_info)localCPInfo).value
/* 293 */         .equals(paramString)))
/*     */       {
/* 294 */         return i;
/*     */       }
/*     */     }
/* 296 */     throw new EntryNotFound(paramString);
/*     */   }
/*     */ 
/*     */   public Iterable<CPInfo> entries() {
/* 300 */     return new Iterable() {
/*     */       public Iterator<ConstantPool.CPInfo> iterator() {
/* 302 */         return new Iterator()
/*     */         {
/*     */           private ConstantPool.CPInfo current;
/* 326 */           private int next = 1;
/*     */ 
/*     */           public boolean hasNext()
/*     */           {
/* 305 */             return this.next < ConstantPool.this.pool.length;
/*     */           }
/*     */ 
/*     */           public ConstantPool.CPInfo next() {
/* 309 */             this.current = ConstantPool.this.pool[this.next];
/* 310 */             switch (this.current.getTag()) {
/*     */             case 5:
/*     */             case 6:
/* 313 */               this.next += 2;
/* 314 */               break;
/*     */             default:
/* 316 */               this.next += 1;
/*     */             }
/* 318 */             return this.current;
/*     */           }
/*     */ 
/*     */           public void remove() {
/* 322 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Class_info extends ConstantPool.CPInfo
/*     */   {
/*     */     public final int name_index;
/*     */ 
/*     */     CONSTANT_Class_info(ConstantPool paramConstantPool, ClassReader paramClassReader)
/*     */       throws IOException
/*     */     {
/* 418 */       super();
/* 419 */       this.name_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public CONSTANT_Class_info(ConstantPool paramConstantPool, int paramInt) {
/* 423 */       super();
/* 424 */       this.name_index = paramInt;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 428 */       return 7;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 432 */       return 3;
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */       throws ConstantPoolException
/*     */     {
/* 442 */       return this.cp.getUTF8Value(this.name_index);
/*     */     }
/*     */ 
/*     */     public String getBaseName()
/*     */       throws ConstantPoolException
/*     */     {
/* 454 */       String str = getName();
/* 455 */       if (str.startsWith("[")) {
/* 456 */         int i = str.indexOf("[L");
/* 457 */         if (i == -1)
/* 458 */           return null;
/* 459 */         return str.substring(i + 2, str.length() - 1);
/*     */       }
/* 461 */       return str;
/*     */     }
/*     */ 
/*     */     public int getDimensionCount() throws ConstantPoolException {
/* 465 */       String str = getName();
/* 466 */       int i = 0;
/* 467 */       while (str.charAt(i) == '[')
/* 468 */         i++;
/* 469 */       return i;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 474 */       return "CONSTANT_Class_info[name_index: " + this.name_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 478 */       return paramVisitor.visitClass(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Double_info extends ConstantPool.CPInfo {
/*     */     public final double value;
/*     */ 
/*     */     CONSTANT_Double_info(ClassReader paramClassReader) throws IOException {
/* 486 */       this.value = paramClassReader.readDouble();
/*     */     }
/*     */ 
/*     */     public CONSTANT_Double_info(double paramDouble) {
/* 490 */       this.value = paramDouble;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 494 */       return 6;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 498 */       return 9;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 503 */       return 2;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 508 */       return "CONSTANT_Double_info[value: " + this.value + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 512 */       return paramVisitor.visitDouble(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Fieldref_info extends ConstantPool.CPRefInfo
/*     */   {
/*     */     CONSTANT_Fieldref_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException
/*     */     {
/* 520 */       super(paramClassReader, 9);
/*     */     }
/*     */ 
/*     */     public CONSTANT_Fieldref_info(ConstantPool paramConstantPool, int paramInt1, int paramInt2) {
/* 524 */       super(9, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 529 */       return "CONSTANT_Fieldref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 533 */       return paramVisitor.visitFieldref(this, paramD);
/*     */     }
/*     */   }
/*     */   public static class CONSTANT_Float_info extends ConstantPool.CPInfo {
/*     */     public final float value;
/*     */ 
/* 539 */     CONSTANT_Float_info(ClassReader paramClassReader) throws IOException { this.value = paramClassReader.readFloat(); }
/*     */ 
/*     */     public CONSTANT_Float_info(float paramFloat)
/*     */     {
/* 543 */       this.value = paramFloat;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 547 */       return 4;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 551 */       return 5;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 556 */       return "CONSTANT_Float_info[value: " + this.value + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 560 */       return paramVisitor.visitFloat(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Integer_info extends ConstantPool.CPInfo {
/*     */     public final int value;
/*     */ 
/*     */     CONSTANT_Integer_info(ClassReader paramClassReader) throws IOException {
/* 568 */       this.value = paramClassReader.readInt();
/*     */     }
/*     */ 
/*     */     public CONSTANT_Integer_info(int paramInt) {
/* 572 */       this.value = paramInt;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 576 */       return 3;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 580 */       return 5;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 585 */       return "CONSTANT_Integer_info[value: " + this.value + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 589 */       return paramVisitor.visitInteger(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_InterfaceMethodref_info extends ConstantPool.CPRefInfo
/*     */   {
/*     */     CONSTANT_InterfaceMethodref_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException
/*     */     {
/* 597 */       super(paramClassReader, 11);
/*     */     }
/*     */ 
/*     */     public CONSTANT_InterfaceMethodref_info(ConstantPool paramConstantPool, int paramInt1, int paramInt2) {
/* 601 */       super(11, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 606 */       return "CONSTANT_InterfaceMethodref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 610 */       return paramVisitor.visitInterfaceMethodref(this, paramD); } 
/*     */   }
/*     */   public static class CONSTANT_InvokeDynamic_info extends ConstantPool.CPInfo { public final int bootstrap_method_attr_index;
/*     */     public final int name_and_type_index;
/*     */ 
/* 616 */     CONSTANT_InvokeDynamic_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException { super();
/* 617 */       this.bootstrap_method_attr_index = paramClassReader.readUnsignedShort();
/* 618 */       this.name_and_type_index = paramClassReader.readUnsignedShort(); }
/*     */ 
/*     */     public CONSTANT_InvokeDynamic_info(ConstantPool paramConstantPool, int paramInt1, int paramInt2)
/*     */     {
/* 622 */       super();
/* 623 */       this.bootstrap_method_attr_index = paramInt1;
/* 624 */       this.name_and_type_index = paramInt2;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 628 */       return 18;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 632 */       return 5;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 637 */       return "CONSTANT_InvokeDynamic_info[bootstrap_method_index: " + this.bootstrap_method_attr_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 641 */       return paramVisitor.visitInvokeDynamic(this, paramD);
/*     */     }
/*     */ 
/*     */     public ConstantPool.CONSTANT_NameAndType_info getNameAndTypeInfo() throws ConstantPoolException {
/* 645 */       return this.cp.getNameAndTypeInfo(this.name_and_type_index);
/*     */     } }
/*     */ 
/*     */   public static class CONSTANT_Long_info extends ConstantPool.CPInfo
/*     */   {
/*     */     public final long value;
/*     */ 
/*     */     CONSTANT_Long_info(ClassReader paramClassReader) throws IOException
/*     */     {
/* 654 */       this.value = paramClassReader.readLong();
/*     */     }
/*     */ 
/*     */     public CONSTANT_Long_info(long paramLong) {
/* 658 */       this.value = paramLong;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 662 */       return 5;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 667 */       return 2;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 671 */       return 9;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 676 */       return "CONSTANT_Long_info[value: " + this.value + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 680 */       return paramVisitor.visitLong(this, paramD);
/*     */     }
/*     */   }
/*     */   public static class CONSTANT_MethodHandle_info extends ConstantPool.CPInfo {
/*     */     public final ConstantPool.RefKind reference_kind;
/*     */     public final int reference_index;
/*     */ 
/* 688 */     CONSTANT_MethodHandle_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException { super();
/* 689 */       this.reference_kind = ConstantPool.RefKind.getRefkind(paramClassReader.readUnsignedByte());
/* 690 */       this.reference_index = paramClassReader.readUnsignedShort(); }
/*     */ 
/*     */     public CONSTANT_MethodHandle_info(ConstantPool paramConstantPool, ConstantPool.RefKind paramRefKind, int paramInt)
/*     */     {
/* 694 */       super();
/* 695 */       this.reference_kind = paramRefKind;
/* 696 */       this.reference_index = paramInt;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 700 */       return 15;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 704 */       return 4;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 709 */       return "CONSTANT_MethodHandle_info[ref_kind: " + this.reference_kind + ", member_index: " + this.reference_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 713 */       return paramVisitor.visitMethodHandle(this, paramD);
/*     */     }
/*     */ 
/*     */     public ConstantPool.CPRefInfo getCPRefInfo() throws ConstantPoolException {
/* 717 */       int i = 10;
/* 718 */       int j = this.cp.get(this.reference_index).getTag();
/*     */ 
/* 720 */       switch (j) {
/*     */       case 9:
/*     */       case 11:
/* 723 */         i = j;
/*     */       }
/* 725 */       return (ConstantPool.CPRefInfo)this.cp.get(this.reference_index, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_MethodType_info extends ConstantPool.CPInfo
/*     */   {
/*     */     public final int descriptor_index;
/*     */ 
/*     */     CONSTANT_MethodType_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException {
/* 734 */       super();
/* 735 */       this.descriptor_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public CONSTANT_MethodType_info(ConstantPool paramConstantPool, int paramInt) {
/* 739 */       super();
/* 740 */       this.descriptor_index = paramInt;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 744 */       return 16;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 748 */       return 3;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 753 */       return "CONSTANT_MethodType_info[signature_index: " + this.descriptor_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 757 */       return paramVisitor.visitMethodType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getType() throws ConstantPoolException {
/* 761 */       return this.cp.getUTF8Value(this.descriptor_index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Methodref_info extends ConstantPool.CPRefInfo
/*     */   {
/*     */     CONSTANT_Methodref_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException
/*     */     {
/* 769 */       super(paramClassReader, 10);
/*     */     }
/*     */ 
/*     */     public CONSTANT_Methodref_info(ConstantPool paramConstantPool, int paramInt1, int paramInt2) {
/* 773 */       super(10, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 778 */       return "CONSTANT_Methodref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 782 */       return paramVisitor.visitMethodref(this, paramD); } 
/*     */   }
/*     */   public static class CONSTANT_NameAndType_info extends ConstantPool.CPInfo { public final int name_index;
/*     */     public final int type_index;
/*     */ 
/* 788 */     CONSTANT_NameAndType_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException { super();
/* 789 */       this.name_index = paramClassReader.readUnsignedShort();
/* 790 */       this.type_index = paramClassReader.readUnsignedShort(); }
/*     */ 
/*     */     public CONSTANT_NameAndType_info(ConstantPool paramConstantPool, int paramInt1, int paramInt2)
/*     */     {
/* 794 */       super();
/* 795 */       this.name_index = paramInt1;
/* 796 */       this.type_index = paramInt2;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 800 */       return 12;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 804 */       return 5;
/*     */     }
/*     */ 
/*     */     public String getName() throws ConstantPoolException {
/* 808 */       return this.cp.getUTF8Value(this.name_index);
/*     */     }
/*     */ 
/*     */     public String getType() throws ConstantPoolException {
/* 812 */       return this.cp.getUTF8Value(this.type_index);
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 816 */       return paramVisitor.visitNameAndType(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 821 */       return "CONSTANT_NameAndType_info[name_index: " + this.name_index + ", type_index: " + this.type_index + "]";
/*     */     } }
/*     */ 
/*     */   public static class CONSTANT_String_info extends ConstantPool.CPInfo
/*     */   {
/*     */     public final int string_index;
/*     */ 
/*     */     CONSTANT_String_info(ConstantPool paramConstantPool, ClassReader paramClassReader) throws IOException
/*     */     {
/* 830 */       super();
/* 831 */       this.string_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public CONSTANT_String_info(ConstantPool paramConstantPool, int paramInt) {
/* 835 */       super();
/* 836 */       this.string_index = paramInt;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 840 */       return 8;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 844 */       return 3;
/*     */     }
/*     */ 
/*     */     public String getString() throws ConstantPoolException {
/* 848 */       return this.cp.getUTF8Value(this.string_index);
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 852 */       return paramVisitor.visitString(this, paramD);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 857 */       return "CONSTANT_String_info[class_index: " + this.string_index + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CONSTANT_Utf8_info extends ConstantPool.CPInfo {
/*     */     public final String value;
/*     */ 
/*     */     CONSTANT_Utf8_info(ClassReader paramClassReader) throws IOException {
/* 865 */       this.value = paramClassReader.readUTF();
/*     */     }
/*     */ 
/*     */     public CONSTANT_Utf8_info(String paramString) {
/* 869 */       this.value = paramString;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 873 */       return 1;
/*     */     }
/*     */ 
/*     */     public int byteLength()
/*     */     {
/* 884 */       OutputStream local1SizeOutputStream = new OutputStream()
/*     */       {
/*     */         int size;
/*     */ 
/*     */         public void write(int paramAnonymousInt)
/*     */         {
/* 880 */           this.size += 1;
/*     */         }
/*     */       };
/* 885 */       DataOutputStream localDataOutputStream = new DataOutputStream(local1SizeOutputStream);
/*     */       try { localDataOutputStream.writeUTF(this.value); } catch (IOException localIOException) {
/* 887 */       }return 1 + local1SizeOutputStream.size;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 892 */       if ((this.value.length() < 32) && (isPrintableAscii(this.value))) {
/* 893 */         return "CONSTANT_Utf8_info[value: \"" + this.value + "\"]";
/*     */       }
/* 895 */       return "CONSTANT_Utf8_info[value: (" + this.value.length() + " chars)]";
/*     */     }
/*     */ 
/*     */     static boolean isPrintableAscii(String paramString) {
/* 899 */       for (int i = 0; i < paramString.length(); i++) {
/* 900 */         int j = paramString.charAt(i);
/* 901 */         if ((j < 32) || (j >= 127))
/* 902 */           return false;
/*     */       }
/* 904 */       return true;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD) {
/* 908 */       return paramVisitor.visitUtf8(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class CPInfo
/*     */   {
/*     */     protected final ConstantPool cp;
/*     */ 
/*     */     CPInfo()
/*     */     {
/* 354 */       this.cp = null;
/*     */     }
/*     */ 
/*     */     CPInfo(ConstantPool paramConstantPool) {
/* 358 */       this.cp = paramConstantPool;
/*     */     }
/*     */ 
/*     */     public abstract int getTag();
/*     */ 
/*     */     public int size()
/*     */     {
/* 366 */       return 1;
/*     */     }
/*     */     public abstract int byteLength();
/*     */ 
/*     */     public abstract <R, D> R accept(ConstantPool.Visitor<R, D> paramVisitor, D paramD);
/*     */   }
/*     */   public static abstract class CPRefInfo extends ConstantPool.CPInfo { public final int tag;
/*     */     public final int class_index;
/*     */     public final int name_and_type_index;
/*     */ 
/* 378 */     protected CPRefInfo(ConstantPool paramConstantPool, ClassReader paramClassReader, int paramInt) throws IOException { super();
/* 379 */       this.tag = paramInt;
/* 380 */       this.class_index = paramClassReader.readUnsignedShort();
/* 381 */       this.name_and_type_index = paramClassReader.readUnsignedShort(); }
/*     */ 
/*     */     protected CPRefInfo(ConstantPool paramConstantPool, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 385 */       super();
/* 386 */       this.tag = paramInt1;
/* 387 */       this.class_index = paramInt2;
/* 388 */       this.name_and_type_index = paramInt3;
/*     */     }
/*     */ 
/*     */     public int getTag() {
/* 392 */       return this.tag;
/*     */     }
/*     */ 
/*     */     public int byteLength() {
/* 396 */       return 5;
/*     */     }
/*     */ 
/*     */     public ConstantPool.CONSTANT_Class_info getClassInfo() throws ConstantPoolException {
/* 400 */       return this.cp.getClassInfo(this.class_index);
/*     */     }
/*     */ 
/*     */     public String getClassName() throws ConstantPoolException {
/* 404 */       return this.cp.getClassInfo(this.class_index).getName();
/*     */     }
/*     */ 
/*     */     public ConstantPool.CONSTANT_NameAndType_info getNameAndTypeInfo() throws ConstantPoolException {
/* 408 */       return this.cp.getNameAndTypeInfo(this.name_and_type_index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class EntryNotFound extends ConstantPoolException
/*     */   {
/*     */     private static final long serialVersionUID = 2885537606468581850L;
/*     */     public final Object value;
/*     */ 
/*     */     EntryNotFound(Object paramObject)
/*     */     {
/*  93 */       super();
/*  94 */       this.value = paramObject;
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/* 100 */       return "value not found: " + this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InvalidEntry extends ConstantPoolException
/*     */   {
/*     */     private static final long serialVersionUID = 1000087545585204447L;
/*     */     public final int tag;
/*     */ 
/*     */     InvalidEntry(int paramInt1, int paramInt2)
/*     */     {
/*  77 */       super();
/*  78 */       this.tag = paramInt2;
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/*  84 */       return "unexpected tag at #" + this.index + ": " + this.tag;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InvalidIndex extends ConstantPoolException
/*     */   {
/*     */     private static final long serialVersionUID = -4350294289300939730L;
/*     */ 
/*     */     InvalidIndex(int paramInt)
/*     */     {
/*  46 */       super();
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/*  52 */       return "invalid index #" + this.index;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum RefKind
/*     */   {
/* 122 */     REF_getField(1, "getfield"), 
/* 123 */     REF_getStatic(2, "getstatic"), 
/* 124 */     REF_putField(3, "putfield"), 
/* 125 */     REF_putStatic(4, "putstatic"), 
/* 126 */     REF_invokeVirtual(5, "invokevirtual"), 
/* 127 */     REF_invokeStatic(6, "invokestatic"), 
/* 128 */     REF_invokeSpecial(7, "invokespecial"), 
/* 129 */     REF_newInvokeSpecial(8, "newinvokespecial"), 
/* 130 */     REF_invokeInterface(9, "invokeinterface");
/*     */ 
/*     */     public final int tag;
/*     */     public final String name;
/*     */ 
/* 136 */     private RefKind(int paramInt, String paramString) { this.tag = paramInt;
/* 137 */       this.name = paramString; }
/*     */ 
/*     */     static RefKind getRefkind(int paramInt)
/*     */     {
/* 141 */       switch (paramInt) {
/*     */       case 1:
/* 143 */         return REF_getField;
/*     */       case 2:
/* 145 */         return REF_getStatic;
/*     */       case 3:
/* 147 */         return REF_putField;
/*     */       case 4:
/* 149 */         return REF_putStatic;
/*     */       case 5:
/* 151 */         return REF_invokeVirtual;
/*     */       case 6:
/* 153 */         return REF_invokeStatic;
/*     */       case 7:
/* 155 */         return REF_invokeSpecial;
/*     */       case 8:
/* 157 */         return REF_newInvokeSpecial;
/*     */       case 9:
/* 159 */         return REF_invokeInterface;
/*     */       }
/* 161 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UnexpectedEntry extends ConstantPoolException
/*     */   {
/*     */     private static final long serialVersionUID = 6986335935377933211L;
/*     */     public final int expected_tag;
/*     */     public final int found_tag;
/*     */ 
/*     */     UnexpectedEntry(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/*  59 */       super();
/*  60 */       this.expected_tag = paramInt2;
/*  61 */       this.found_tag = paramInt3;
/*     */     }
/*     */ 
/*     */     public String getMessage()
/*     */     {
/*  67 */       return "unexpected entry at #" + this.index + " -- expected tag " + this.expected_tag + ", found " + this.found_tag;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Visitor<R, P>
/*     */   {
/*     */     public abstract R visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, P paramP);
/*     */ 
/*     */     public abstract R visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, P paramP);
/*     */ 
/*     */     public abstract R visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, P paramP);
/*     */ 
/*     */     public abstract R visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, P paramP);
/*     */ 
/*     */     public abstract R visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, P paramP);
/*     */ 
/*     */     public abstract R visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, P paramP);
/*     */ 
/*     */     public abstract R visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, P paramP);
/*     */ 
/*     */     public abstract R visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, P paramP);
/*     */ 
/*     */     public abstract R visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, P paramP);
/*     */ 
/*     */     public abstract R visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, P paramP);
/*     */ 
/*     */     public abstract R visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, P paramP);
/*     */ 
/*     */     public abstract R visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, P paramP);
/*     */ 
/*     */     public abstract R visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, P paramP);
/*     */ 
/*     */     public abstract R visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, P paramP);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ConstantPool
 * JD-Core Version:    0.6.2
 */