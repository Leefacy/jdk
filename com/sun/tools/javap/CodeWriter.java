/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.Code_attribute.Exception_data;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Instruction.KindVisitor;
/*     */ import com.sun.tools.classfile.Instruction.TypeKind;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CodeWriter extends BasicWriter
/*     */ {
/* 130 */   Instruction.KindVisitor<Void, Integer> instructionPrinter = new Instruction.KindVisitor()
/*     */   {
/*     */     public Void visitNoOperands(Instruction paramAnonymousInstruction, Integer paramAnonymousInteger)
/*     */     {
/* 134 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitArrayType(Instruction paramAnonymousInstruction, Instruction.TypeKind paramAnonymousTypeKind, Integer paramAnonymousInteger) {
/* 138 */       CodeWriter.this.print(" " + paramAnonymousTypeKind.name);
/* 139 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitBranch(Instruction paramAnonymousInstruction, int paramAnonymousInt, Integer paramAnonymousInteger) {
/* 143 */       CodeWriter.this.print(Integer.valueOf(paramAnonymousInstruction.getPC() + paramAnonymousInt));
/* 144 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitConstantPoolRef(Instruction paramAnonymousInstruction, int paramAnonymousInt, Integer paramAnonymousInteger) {
/* 148 */       CodeWriter.this.print("#" + paramAnonymousInt);
/* 149 */       CodeWriter.this.tab();
/* 150 */       CodeWriter.this.print("// ");
/* 151 */       CodeWriter.this.printConstant(paramAnonymousInt);
/* 152 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitConstantPoolRefAndValue(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, Integer paramAnonymousInteger) {
/* 156 */       CodeWriter.this.print("#" + paramAnonymousInt1 + ",  " + paramAnonymousInt2);
/* 157 */       CodeWriter.this.tab();
/* 158 */       CodeWriter.this.print("// ");
/* 159 */       CodeWriter.this.printConstant(paramAnonymousInt1);
/* 160 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitLocal(Instruction paramAnonymousInstruction, int paramAnonymousInt, Integer paramAnonymousInteger) {
/* 164 */       CodeWriter.this.print(Integer.valueOf(paramAnonymousInt));
/* 165 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitLocalAndValue(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, Integer paramAnonymousInteger) {
/* 169 */       CodeWriter.this.print(paramAnonymousInt1 + ", " + paramAnonymousInt2);
/* 170 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitLookupSwitch(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, int[] paramAnonymousArrayOfInt1, int[] paramAnonymousArrayOfInt2, Integer paramAnonymousInteger)
/*     */     {
/* 175 */       int i = paramAnonymousInstruction.getPC();
/* 176 */       CodeWriter.this.print("{ // " + paramAnonymousInt2);
/* 177 */       CodeWriter.this.indent(paramAnonymousInteger.intValue());
/* 178 */       for (int j = 0; j < paramAnonymousInt2; j++) {
/* 179 */         CodeWriter.this.print(String.format("%n%12d: %d", new Object[] { Integer.valueOf(paramAnonymousArrayOfInt1[j]), Integer.valueOf(i + paramAnonymousArrayOfInt2[j]) }));
/*     */       }
/* 181 */       CodeWriter.this.print("\n     default: " + (i + paramAnonymousInt1) + "\n}");
/* 182 */       CodeWriter.this.indent(-paramAnonymousInteger.intValue());
/* 183 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitTableSwitch(Instruction paramAnonymousInstruction, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int[] paramAnonymousArrayOfInt, Integer paramAnonymousInteger)
/*     */     {
/* 188 */       int i = paramAnonymousInstruction.getPC();
/* 189 */       CodeWriter.this.print("{ // " + paramAnonymousInt2 + " to " + paramAnonymousInt3);
/* 190 */       CodeWriter.this.indent(paramAnonymousInteger.intValue());
/* 191 */       for (int j = 0; j < paramAnonymousArrayOfInt.length; j++) {
/* 192 */         CodeWriter.this.print(String.format("%n%12d: %d", new Object[] { Integer.valueOf(paramAnonymousInt2 + j), Integer.valueOf(i + paramAnonymousArrayOfInt[j]) }));
/*     */       }
/* 194 */       CodeWriter.this.print("\n     default: " + (i + paramAnonymousInt1) + "\n}");
/* 195 */       CodeWriter.this.indent(-paramAnonymousInteger.intValue());
/* 196 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitValue(Instruction paramAnonymousInstruction, int paramAnonymousInt, Integer paramAnonymousInteger) {
/* 200 */       CodeWriter.this.print(Integer.valueOf(paramAnonymousInt));
/* 201 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitUnknown(Instruction paramAnonymousInstruction, Integer paramAnonymousInteger) {
/* 205 */       return null;
/*     */     }
/* 130 */   };
/*     */   private AttributeWriter attrWriter;
/*     */   private ClassWriter classWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private LocalVariableTableWriter localVariableTableWriter;
/*     */   private LocalVariableTypeTableWriter localVariableTypeTableWriter;
/*     */   private TypeAnnotationWriter typeAnnotationWriter;
/*     */   private SourceWriter sourceWriter;
/*     */   private StackMapWriter stackMapWriter;
/*     */   private TryBlockWriter tryBlockWriter;
/*     */   private Options options;
/*     */ 
/*     */   public static CodeWriter instance(Context paramContext)
/*     */   {
/*  50 */     CodeWriter localCodeWriter = (CodeWriter)paramContext.get(CodeWriter.class);
/*  51 */     if (localCodeWriter == null)
/*  52 */       localCodeWriter = new CodeWriter(paramContext);
/*  53 */     return localCodeWriter;
/*     */   }
/*     */ 
/*     */   protected CodeWriter(Context paramContext) {
/*  57 */     super(paramContext);
/*  58 */     paramContext.put(CodeWriter.class, this);
/*  59 */     this.attrWriter = AttributeWriter.instance(paramContext);
/*  60 */     this.classWriter = ClassWriter.instance(paramContext);
/*  61 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*  62 */     this.sourceWriter = SourceWriter.instance(paramContext);
/*  63 */     this.tryBlockWriter = TryBlockWriter.instance(paramContext);
/*  64 */     this.stackMapWriter = StackMapWriter.instance(paramContext);
/*  65 */     this.localVariableTableWriter = LocalVariableTableWriter.instance(paramContext);
/*  66 */     this.localVariableTypeTableWriter = LocalVariableTypeTableWriter.instance(paramContext);
/*  67 */     this.typeAnnotationWriter = TypeAnnotationWriter.instance(paramContext);
/*  68 */     this.options = Options.instance(paramContext);
/*     */   }
/*     */ 
/*     */   void write(Code_attribute paramCode_attribute, ConstantPool paramConstantPool) {
/*  72 */     println("Code:");
/*  73 */     indent(1);
/*  74 */     writeVerboseHeader(paramCode_attribute, paramConstantPool);
/*  75 */     writeInstrs(paramCode_attribute);
/*  76 */     writeExceptionTable(paramCode_attribute);
/*  77 */     this.attrWriter.write(paramCode_attribute, paramCode_attribute.attributes, paramConstantPool);
/*  78 */     indent(-1);
/*     */   }
/*     */ 
/*  82 */   public void writeVerboseHeader(Code_attribute paramCode_attribute, ConstantPool paramConstantPool) { Method localMethod = this.classWriter.getMethod();
/*     */     String str;
/*     */     try {
/*  85 */       int i = localMethod.descriptor.getParameterCount(paramConstantPool);
/*  86 */       if (!localMethod.access_flags.is(8))
/*  87 */         i++;
/*  88 */       str = Integer.toString(i);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/*  90 */       str = report(localConstantPoolException);
/*     */     } catch (DescriptorException localDescriptorException) {
/*  92 */       str = report(localDescriptorException);
/*     */     }
/*     */ 
/*  95 */     println("stack=" + paramCode_attribute.max_stack + ", locals=" + paramCode_attribute.max_locals + ", args_size=" + str);
/*     */   }
/*     */ 
/*     */   public void writeInstrs(Code_attribute paramCode_attribute)
/*     */   {
/* 102 */     List localList = getDetailWriters(paramCode_attribute);
/*     */ 
/* 104 */     for (Iterator localIterator1 = paramCode_attribute.getInstructions().iterator(); localIterator1.hasNext(); ) { localObject = (Instruction)localIterator1.next();
/*     */       try {
/* 106 */         for (InstructionDetailWriter localInstructionDetailWriter : localList)
/* 107 */           localInstructionDetailWriter.writeDetails((Instruction)localObject);
/* 108 */         writeInstr((Instruction)localObject);
/*     */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 110 */         println(report("error at or after byte " + ((Instruction)localObject).getPC()));
/* 111 */         break;
/*     */       }
/*     */     }
/* 115 */     Object localObject;
/* 115 */     for (localIterator1 = localList.iterator(); localIterator1.hasNext(); ) { localObject = (InstructionDetailWriter)localIterator1.next();
/* 116 */       ((InstructionDetailWriter)localObject).flush(); }
/*     */   }
/*     */ 
/*     */   public void writeInstr(Instruction paramInstruction) {
/* 120 */     print(String.format("%4d: %-13s ", new Object[] { Integer.valueOf(paramInstruction.getPC()), paramInstruction.getMnemonic() }));
/*     */ 
/* 124 */     int i = this.options.indentWidth;
/* 125 */     int j = (6 + i - 1) / i;
/* 126 */     paramInstruction.accept(this.instructionPrinter, Integer.valueOf(j));
/* 127 */     println();
/*     */   }
/*     */ 
/*     */   public void writeExceptionTable(Code_attribute paramCode_attribute)
/*     */   {
/* 211 */     if (paramCode_attribute.exception_table_length > 0) {
/* 212 */       println("Exception table:");
/* 213 */       indent(1);
/* 214 */       println(" from    to  target type");
/* 215 */       for (int i = 0; i < paramCode_attribute.exception_table.length; i++) {
/* 216 */         Code_attribute.Exception_data localException_data = paramCode_attribute.exception_table[i];
/* 217 */         print(String.format(" %5d %5d %5d", new Object[] { 
/* 218 */           Integer.valueOf(localException_data.start_pc), 
/* 218 */           Integer.valueOf(localException_data.end_pc), Integer.valueOf(localException_data.handler_pc) }));
/* 219 */         print("   ");
/* 220 */         int j = localException_data.catch_type;
/* 221 */         if (j == 0) {
/* 222 */           println("any");
/*     */         } else {
/* 224 */           print("Class ");
/* 225 */           println(this.constantWriter.stringValue(j));
/*     */         }
/*     */       }
/* 228 */       indent(-1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printConstant(int paramInt)
/*     */   {
/* 234 */     this.constantWriter.write(paramInt);
/*     */   }
/*     */ 
/*     */   private List<InstructionDetailWriter> getDetailWriters(Code_attribute paramCode_attribute) {
/* 238 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 240 */     if (this.options.details.contains(InstructionDetailWriter.Kind.SOURCE)) {
/* 241 */       this.sourceWriter.reset(this.classWriter.getClassFile(), paramCode_attribute);
/* 242 */       if (this.sourceWriter.hasSource())
/* 243 */         localArrayList.add(this.sourceWriter);
/*     */       else {
/* 245 */         println("(Source code not available)");
/*     */       }
/*     */     }
/* 248 */     if (this.options.details.contains(InstructionDetailWriter.Kind.LOCAL_VARS)) {
/* 249 */       this.localVariableTableWriter.reset(paramCode_attribute);
/* 250 */       localArrayList.add(this.localVariableTableWriter);
/*     */     }
/*     */ 
/* 253 */     if (this.options.details.contains(InstructionDetailWriter.Kind.LOCAL_VAR_TYPES)) {
/* 254 */       this.localVariableTypeTableWriter.reset(paramCode_attribute);
/* 255 */       localArrayList.add(this.localVariableTypeTableWriter);
/*     */     }
/*     */ 
/* 258 */     if (this.options.details.contains(InstructionDetailWriter.Kind.STACKMAPS)) {
/* 259 */       this.stackMapWriter.reset(paramCode_attribute);
/* 260 */       this.stackMapWriter.writeInitialDetails();
/* 261 */       localArrayList.add(this.stackMapWriter);
/*     */     }
/*     */ 
/* 264 */     if (this.options.details.contains(InstructionDetailWriter.Kind.TRY_BLOCKS)) {
/* 265 */       this.tryBlockWriter.reset(paramCode_attribute);
/* 266 */       localArrayList.add(this.tryBlockWriter);
/*     */     }
/*     */ 
/* 269 */     if (this.options.details.contains(InstructionDetailWriter.Kind.TYPE_ANNOS)) {
/* 270 */       this.typeAnnotationWriter.reset(paramCode_attribute);
/* 271 */       localArrayList.add(this.typeAnnotationWriter);
/*     */     }
/*     */ 
/* 274 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.CodeWriter
 * JD-Core Version:    0.6.2
 */