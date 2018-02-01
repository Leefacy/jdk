/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.Descriptor.InvalidDescriptor;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.Signature;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class LocalVariableTypeTableWriter extends InstructionDetailWriter
/*     */ {
/*     */   private ClassWriter classWriter;
/*     */   private Code_attribute codeAttr;
/*     */   private Map<Integer, List<LocalVariableTypeTable_attribute.Entry>> pcMap;
/*     */ 
/*     */   static LocalVariableTypeTableWriter instance(Context paramContext)
/*     */   {
/*  71 */     LocalVariableTypeTableWriter localLocalVariableTypeTableWriter = (LocalVariableTypeTableWriter)paramContext.get(LocalVariableTypeTableWriter.class);
/*  72 */     if (localLocalVariableTypeTableWriter == null)
/*  73 */       localLocalVariableTypeTableWriter = new LocalVariableTypeTableWriter(paramContext);
/*  74 */     return localLocalVariableTypeTableWriter;
/*     */   }
/*     */ 
/*     */   protected LocalVariableTypeTableWriter(Context paramContext) {
/*  78 */     super(paramContext);
/*  79 */     paramContext.put(LocalVariableTypeTableWriter.class, this);
/*  80 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  84 */     this.codeAttr = paramCode_attribute;
/*  85 */     this.pcMap = new HashMap();
/*     */ 
/*  87 */     LocalVariableTypeTable_attribute localLocalVariableTypeTable_attribute = (LocalVariableTypeTable_attribute)paramCode_attribute.attributes
/*  87 */       .get("LocalVariableTypeTable");
/*     */ 
/*  88 */     if (localLocalVariableTypeTable_attribute == null) {
/*  89 */       return;
/*     */     }
/*  91 */     for (int i = 0; i < localLocalVariableTypeTable_attribute.local_variable_table.length; i++) {
/*  92 */       LocalVariableTypeTable_attribute.Entry localEntry = localLocalVariableTypeTable_attribute.local_variable_table[i];
/*  93 */       put(localEntry.start_pc, localEntry);
/*  94 */       put(localEntry.start_pc + localEntry.length, localEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  99 */     int i = paramInstruction.getPC();
/* 100 */     writeLocalVariables(i, NoteKind.END);
/* 101 */     writeLocalVariables(i, NoteKind.START);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 106 */     int i = this.codeAttr.code_length;
/* 107 */     writeLocalVariables(i, NoteKind.END);
/*     */   }
/*     */ 
/*     */   public void writeLocalVariables(int paramInt, NoteKind paramNoteKind) {
/* 111 */     ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/* 112 */     String str = space(2);
/* 113 */     List localList = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 114 */     if (localList != null)
/*     */     {
/* 116 */       ListIterator localListIterator = localList
/* 116 */         .listIterator(paramNoteKind == NoteKind.END ? localList
/* 116 */         .size() : 0);
/* 117 */       while (paramNoteKind == NoteKind.END ? localListIterator.hasPrevious() : localListIterator.hasNext())
/*     */       {
/* 119 */         LocalVariableTypeTable_attribute.Entry localEntry = paramNoteKind == NoteKind.END ? 
/* 119 */           (LocalVariableTypeTable_attribute.Entry)localListIterator
/* 119 */           .previous() : (LocalVariableTypeTable_attribute.Entry)localListIterator.next();
/* 120 */         if (paramNoteKind.match(localEntry, paramInt)) {
/* 121 */           print(str);
/* 122 */           print(paramNoteKind.text);
/* 123 */           print(" generic local ");
/* 124 */           print(Integer.valueOf(localEntry.index));
/* 125 */           print(" // ");
/* 126 */           Signature localSignature = new Signature(localEntry.signature_index);
/*     */           try {
/* 128 */             print(localSignature.getFieldType(localConstantPool).toString().replace("/", "."));
/*     */           } catch (Descriptor.InvalidDescriptor localInvalidDescriptor) {
/* 130 */             print(report(localInvalidDescriptor));
/*     */           } catch (ConstantPoolException localConstantPoolException1) {
/* 132 */             print(report(localConstantPoolException1));
/*     */           }
/* 134 */           print(" ");
/*     */           try {
/* 136 */             print(localConstantPool.getUTF8Value(localEntry.name_index));
/*     */           } catch (ConstantPoolException localConstantPoolException2) {
/* 138 */             print(report(localConstantPoolException2));
/*     */           }
/* 140 */           println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void put(int paramInt, LocalVariableTypeTable_attribute.Entry paramEntry) {
/* 147 */     Object localObject = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 148 */     if (localObject == null) {
/* 149 */       localObject = new ArrayList();
/* 150 */       this.pcMap.put(Integer.valueOf(paramInt), localObject);
/*     */     }
/* 152 */     if (!((List)localObject).contains(paramEntry))
/* 153 */       ((List)localObject).add(paramEntry);
/*     */   }
/*     */ 
/*     */   public static abstract enum NoteKind
/*     */   {
/*  53 */     START("start"), 
/*     */ 
/*  58 */     END("end");
/*     */ 
/*     */     public final String text;
/*     */ 
/*     */     private NoteKind(String paramString)
/*     */     {
/*  64 */       this.text = paramString;
/*     */     }
/*     */ 
/*     */     public abstract boolean match(LocalVariableTypeTable_attribute.Entry paramEntry, int paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.LocalVariableTypeTableWriter
 * JD-Core Version:    0.6.2
 */