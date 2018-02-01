/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ReferenceFinder
/*     */ {
/*     */   private final Filter filter;
/*     */   private final Visitor visitor;
/* 132 */   private ConstantPool.Visitor<Boolean, ConstantPool> cpVisitor = new ConstantPool.Visitor()
/*     */   {
/*     */     public Boolean visitClass(ConstantPool.CONSTANT_Class_info paramAnonymousCONSTANT_Class_info, ConstantPool paramAnonymousConstantPool)
/*     */     {
/* 136 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramAnonymousCONSTANT_InterfaceMethodref_info, ConstantPool paramAnonymousConstantPool) {
/* 140 */       return Boolean.valueOf(ReferenceFinder.this.filter.accept(paramAnonymousConstantPool, paramAnonymousCONSTANT_InterfaceMethodref_info));
/*     */     }
/*     */ 
/*     */     public Boolean visitMethodref(ConstantPool.CONSTANT_Methodref_info paramAnonymousCONSTANT_Methodref_info, ConstantPool paramAnonymousConstantPool) {
/* 144 */       return Boolean.valueOf(ReferenceFinder.this.filter.accept(paramAnonymousConstantPool, paramAnonymousCONSTANT_Methodref_info));
/*     */     }
/*     */ 
/*     */     public Boolean visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramAnonymousCONSTANT_Fieldref_info, ConstantPool paramAnonymousConstantPool) {
/* 148 */       return Boolean.valueOf(ReferenceFinder.this.filter.accept(paramAnonymousConstantPool, paramAnonymousCONSTANT_Fieldref_info));
/*     */     }
/*     */ 
/*     */     public Boolean visitDouble(ConstantPool.CONSTANT_Double_info paramAnonymousCONSTANT_Double_info, ConstantPool paramAnonymousConstantPool) {
/* 152 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitFloat(ConstantPool.CONSTANT_Float_info paramAnonymousCONSTANT_Float_info, ConstantPool paramAnonymousConstantPool) {
/* 156 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitInteger(ConstantPool.CONSTANT_Integer_info paramAnonymousCONSTANT_Integer_info, ConstantPool paramAnonymousConstantPool) {
/* 160 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramAnonymousCONSTANT_InvokeDynamic_info, ConstantPool paramAnonymousConstantPool) {
/* 164 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitLong(ConstantPool.CONSTANT_Long_info paramAnonymousCONSTANT_Long_info, ConstantPool paramAnonymousConstantPool) {
/* 168 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramAnonymousCONSTANT_NameAndType_info, ConstantPool paramAnonymousConstantPool) {
/* 172 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramAnonymousCONSTANT_MethodHandle_info, ConstantPool paramAnonymousConstantPool) {
/* 176 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitMethodType(ConstantPool.CONSTANT_MethodType_info paramAnonymousCONSTANT_MethodType_info, ConstantPool paramAnonymousConstantPool) {
/* 180 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitString(ConstantPool.CONSTANT_String_info paramAnonymousCONSTANT_String_info, ConstantPool paramAnonymousConstantPool) {
/* 184 */       return Boolean.valueOf(false);
/*     */     }
/*     */ 
/*     */     public Boolean visitUtf8(ConstantPool.CONSTANT_Utf8_info paramAnonymousCONSTANT_Utf8_info, ConstantPool paramAnonymousConstantPool) {
/* 188 */       return Boolean.valueOf(false);
/*     */     }
/* 132 */   };
/*     */ 
/* 192 */   private Instruction.KindVisitor<Integer, List<Integer>> codeVisitor = new Instruction.KindVisitor()
/*     */   {
/*     */     public Integer visitNoOperands(Instruction paramAnonymousInstruction, List<Integer> paramAnonymousList)
/*     */     {
/* 196 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitArrayType(Instruction paramAnonymousInstruction, Instruction.TypeKind paramAnonymousTypeKind, List<Integer> paramAnonymousList) {
/* 200 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitBranch(Instruction paramAnonymousInstruction, int paramAnonymousInt, List<Integer> paramAnonymousList) {
/* 204 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitConstantPoolRef(Instruction paramAnonymousInstruction, int paramAnonymousInt, List<Integer> paramAnonymousList) {
/* 208 */       return Integer.valueOf(paramAnonymousList.contains(Integer.valueOf(paramAnonymousInt)) ? paramAnonymousInt : 0);
/*     */     }
/*     */ 
/*     */     public Integer visitConstantPoolRefAndValue(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, List<Integer> paramAnonymousList) {
/* 212 */       return Integer.valueOf(paramAnonymousList.contains(Integer.valueOf(paramAnonymousInt1)) ? paramAnonymousInt1 : 0);
/*     */     }
/*     */ 
/*     */     public Integer visitLocal(Instruction paramAnonymousInstruction, int paramAnonymousInt, List<Integer> paramAnonymousList) {
/* 216 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitLocalAndValue(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, List<Integer> paramAnonymousList) {
/* 220 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitLookupSwitch(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, int[] paramAnonymousArrayOfInt1, int[] paramAnonymousArrayOfInt2, List<Integer> paramAnonymousList) {
/* 224 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitTableSwitch(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int[] paramAnonymousArrayOfInt, List<Integer> paramAnonymousList) {
/* 228 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitValue(Instruction paramAnonymousInstruction, int paramAnonymousInt, List<Integer> paramAnonymousList) {
/* 232 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Integer visitUnknown(Instruction paramAnonymousInstruction, List<Integer> paramAnonymousList) {
/* 236 */       return Integer.valueOf(0);
/*     */     }
/* 192 */   };
/*     */ 
/*     */   public ReferenceFinder(Filter paramFilter, Visitor paramVisitor)
/*     */   {
/*  80 */     this.filter = ((Filter)Objects.requireNonNull(paramFilter));
/*  81 */     this.visitor = ((Visitor)Objects.requireNonNull(paramVisitor));
/*     */   }
/*     */ 
/*     */   public boolean parse(ClassFile paramClassFile)
/*     */     throws ConstantPoolException
/*     */   {
/*  97 */     ArrayList localArrayList = new ArrayList();
/*  98 */     int i = 1;
/*  99 */     for (Object localObject1 = paramClassFile.constant_pool.entries().iterator(); ((Iterator)localObject1).hasNext(); ) { ConstantPool.CPInfo localCPInfo = (ConstantPool.CPInfo)((Iterator)localObject1).next();
/* 100 */       if (((Boolean)localCPInfo.accept(this.cpVisitor, paramClassFile.constant_pool)).booleanValue()) {
/* 101 */         localArrayList.add(Integer.valueOf(i));
/*     */       }
/* 103 */       i += localCPInfo.size();
/*     */     }
/*     */ 
/* 106 */     if (localArrayList.isEmpty()) {
/* 107 */       return false;
/*     */     }
/*     */ 
/* 110 */     for (Method localMethod : paramClassFile.methods) {
/* 111 */       HashSet localHashSet = new HashSet();
/* 112 */       Code_attribute localCode_attribute = (Code_attribute)localMethod.attributes.get("Code");
/*     */       Object localObject2;
/* 113 */       if (localCode_attribute != null)
/* 114 */         for (localObject2 = localCode_attribute.getInstructions().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Instruction)((Iterator)localObject2).next();
/* 115 */           m = ((Integer)((Instruction)localObject3).accept(this.codeVisitor, localArrayList)).intValue();
/* 116 */           if (m > 0)
/* 117 */             localHashSet.add(Integer.valueOf(m));
/*     */         }
/*     */       Object localObject3;
/*     */       int m;
/* 121 */       if (localHashSet.size() > 0) {
/* 122 */         localObject2 = new ArrayList(localHashSet.size());
/* 123 */         for (localObject3 = localHashSet.iterator(); ((Iterator)localObject3).hasNext(); ) { m = ((Integer)((Iterator)localObject3).next()).intValue();
/* 124 */           ((List)localObject2).add(ConstantPool.CPRefInfo.class.cast(paramClassFile.constant_pool.get(m)));
/*     */         }
/* 126 */         this.visitor.visit(paramClassFile, localMethod, (List)localObject2);
/*     */       }
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   public static abstract interface Filter
/*     */   {
/*     */     public abstract boolean accept(ConstantPool paramConstantPool, ConstantPool.CPRefInfo paramCPRefInfo);
/*     */   }
/*     */ 
/*     */   public static abstract interface Visitor
/*     */   {
/*     */     public abstract void visit(ClassFile paramClassFile, Method paramMethod, List<ConstantPool.CPRefInfo> paramList);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ReferenceFinder
 * JD-Core Version:    0.6.2
 */