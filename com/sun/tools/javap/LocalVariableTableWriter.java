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
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute.Entry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class LocalVariableTableWriter extends InstructionDetailWriter
/*     */ {
/*     */   private ClassWriter classWriter;
/*     */   private Code_attribute codeAttr;
/*     */   private Map<Integer, List<LocalVariableTable_attribute.Entry>> pcMap;
/*     */ 
/*     */   static LocalVariableTableWriter instance(Context paramContext)
/*     */   {
/*  70 */     LocalVariableTableWriter localLocalVariableTableWriter = (LocalVariableTableWriter)paramContext.get(LocalVariableTableWriter.class);
/*  71 */     if (localLocalVariableTableWriter == null)
/*  72 */       localLocalVariableTableWriter = new LocalVariableTableWriter(paramContext);
/*  73 */     return localLocalVariableTableWriter;
/*     */   }
/*     */ 
/*     */   protected LocalVariableTableWriter(Context paramContext) {
/*  77 */     super(paramContext);
/*  78 */     paramContext.put(LocalVariableTableWriter.class, this);
/*  79 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  83 */     this.codeAttr = paramCode_attribute;
/*  84 */     this.pcMap = new HashMap();
/*     */ 
/*  86 */     LocalVariableTable_attribute localLocalVariableTable_attribute = (LocalVariableTable_attribute)paramCode_attribute.attributes
/*  86 */       .get("LocalVariableTable");
/*     */ 
/*  87 */     if (localLocalVariableTable_attribute == null) {
/*  88 */       return;
/*     */     }
/*  90 */     for (int i = 0; i < localLocalVariableTable_attribute.local_variable_table.length; i++) {
/*  91 */       LocalVariableTable_attribute.Entry localEntry = localLocalVariableTable_attribute.local_variable_table[i];
/*  92 */       put(localEntry.start_pc, localEntry);
/*  93 */       put(localEntry.start_pc + localEntry.length, localEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  98 */     int i = paramInstruction.getPC();
/*  99 */     writeLocalVariables(i, NoteKind.END);
/* 100 */     writeLocalVariables(i, NoteKind.START);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 105 */     int i = this.codeAttr.code_length;
/* 106 */     writeLocalVariables(i, NoteKind.END);
/*     */   }
/*     */ 
/*     */   public void writeLocalVariables(int paramInt, NoteKind paramNoteKind) {
/* 110 */     ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/* 111 */     String str = space(2);
/* 112 */     List localList = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 113 */     if (localList != null)
/*     */     {
/* 115 */       ListIterator localListIterator = localList
/* 115 */         .listIterator(paramNoteKind == NoteKind.END ? localList
/* 115 */         .size() : 0);
/* 116 */       while (paramNoteKind == NoteKind.END ? localListIterator.hasPrevious() : localListIterator.hasNext())
/*     */       {
/* 118 */         LocalVariableTable_attribute.Entry localEntry = paramNoteKind == NoteKind.END ? 
/* 118 */           (LocalVariableTable_attribute.Entry)localListIterator
/* 118 */           .previous() : (LocalVariableTable_attribute.Entry)localListIterator.next();
/* 119 */         if (paramNoteKind.match(localEntry, paramInt)) {
/* 120 */           print(str);
/* 121 */           print(paramNoteKind.text);
/* 122 */           print(" local ");
/* 123 */           print(Integer.valueOf(localEntry.index));
/* 124 */           print(" // ");
/* 125 */           Descriptor localDescriptor = new Descriptor(localEntry.descriptor_index);
/*     */           try {
/* 127 */             print(localDescriptor.getFieldType(localConstantPool));
/*     */           } catch (Descriptor.InvalidDescriptor localInvalidDescriptor) {
/* 129 */             print(report(localInvalidDescriptor));
/*     */           } catch (ConstantPoolException localConstantPoolException1) {
/* 131 */             print(report(localConstantPoolException1));
/*     */           }
/* 133 */           print(" ");
/*     */           try {
/* 135 */             print(localConstantPool.getUTF8Value(localEntry.name_index));
/*     */           } catch (ConstantPoolException localConstantPoolException2) {
/* 137 */             print(report(localConstantPoolException2));
/*     */           }
/* 139 */           println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void put(int paramInt, LocalVariableTable_attribute.Entry paramEntry) {
/* 146 */     Object localObject = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 147 */     if (localObject == null) {
/* 148 */       localObject = new ArrayList();
/* 149 */       this.pcMap.put(Integer.valueOf(paramInt), localObject);
/*     */     }
/* 151 */     if (!((List)localObject).contains(paramEntry))
/* 152 */       ((List)localObject).add(paramEntry);
/*     */   }
/*     */ 
/*     */   public static abstract enum NoteKind
/*     */   {
/*  52 */     START("start"), 
/*     */ 
/*  57 */     END("end");
/*     */ 
/*     */     public final String text;
/*     */ 
/*     */     private NoteKind(String paramString)
/*     */     {
/*  63 */       this.text = paramString;
/*     */     }
/*     */ 
/*     */     public abstract boolean match(LocalVariableTable_attribute.Entry paramEntry, int paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.LocalVariableTableWriter
 * JD-Core Version:    0.6.2
 */