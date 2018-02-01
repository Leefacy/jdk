/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.Code_attribute.Exception_data;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TryBlockWriter extends InstructionDetailWriter
/*     */ {
/*     */   private Map<Integer, List<Code_attribute.Exception_data>> pcMap;
/*     */   private Map<Code_attribute.Exception_data, Integer> indexMap;
/*     */   private ConstantWriter constantWriter;
/*     */ 
/*     */   static TryBlockWriter instance(Context paramContext)
/*     */   {
/*  70 */     TryBlockWriter localTryBlockWriter = (TryBlockWriter)paramContext.get(TryBlockWriter.class);
/*  71 */     if (localTryBlockWriter == null)
/*  72 */       localTryBlockWriter = new TryBlockWriter(paramContext);
/*  73 */     return localTryBlockWriter;
/*     */   }
/*     */ 
/*     */   protected TryBlockWriter(Context paramContext) {
/*  77 */     super(paramContext);
/*  78 */     paramContext.put(TryBlockWriter.class, this);
/*  79 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  83 */     this.indexMap = new HashMap();
/*  84 */     this.pcMap = new HashMap();
/*  85 */     for (int i = 0; i < paramCode_attribute.exception_table.length; i++) {
/*  86 */       Code_attribute.Exception_data localException_data = paramCode_attribute.exception_table[i];
/*  87 */       this.indexMap.put(localException_data, Integer.valueOf(i));
/*  88 */       put(localException_data.start_pc, localException_data);
/*  89 */       put(localException_data.end_pc, localException_data);
/*  90 */       put(localException_data.handler_pc, localException_data);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  95 */     writeTrys(paramInstruction, NoteKind.END);
/*  96 */     writeTrys(paramInstruction, NoteKind.START);
/*  97 */     writeTrys(paramInstruction, NoteKind.HANDLER);
/*     */   }
/*     */ 
/*     */   public void writeTrys(Instruction paramInstruction, NoteKind paramNoteKind) {
/* 101 */     String str = space(2);
/* 102 */     int i = paramInstruction.getPC();
/* 103 */     List localList = (List)this.pcMap.get(Integer.valueOf(i));
/* 104 */     if (localList != null)
/*     */     {
/* 106 */       ListIterator localListIterator = localList
/* 106 */         .listIterator(paramNoteKind == NoteKind.END ? localList
/* 106 */         .size() : 0);
/* 107 */       while (paramNoteKind == NoteKind.END ? localListIterator.hasPrevious() : localListIterator.hasNext())
/*     */       {
/* 109 */         Code_attribute.Exception_data localException_data = paramNoteKind == NoteKind.END ? 
/* 109 */           (Code_attribute.Exception_data)localListIterator
/* 109 */           .previous() : (Code_attribute.Exception_data)localListIterator.next();
/* 110 */         if (paramNoteKind.match(localException_data, i)) {
/* 111 */           print(str);
/* 112 */           print(paramNoteKind.text);
/* 113 */           print("[");
/* 114 */           print(this.indexMap.get(localException_data));
/* 115 */           print("] ");
/* 116 */           if (localException_data.catch_type == 0) {
/* 117 */             print("finally");
/*     */           } else {
/* 119 */             print("#" + localException_data.catch_type);
/* 120 */             print(" // ");
/* 121 */             this.constantWriter.write(localException_data.catch_type);
/*     */           }
/* 123 */           println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void put(int paramInt, Code_attribute.Exception_data paramException_data) {
/* 130 */     Object localObject = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 131 */     if (localObject == null) {
/* 132 */       localObject = new ArrayList();
/* 133 */       this.pcMap.put(Integer.valueOf(paramInt), localObject);
/*     */     }
/* 135 */     if (!((List)localObject).contains(paramException_data))
/* 136 */       ((List)localObject).add(paramException_data);
/*     */   }
/*     */ 
/*     */   public static abstract enum NoteKind
/*     */   {
/*  47 */     START("try"), 
/*     */ 
/*  52 */     END("end try"), 
/*     */ 
/*  57 */     HANDLER("catch");
/*     */ 
/*     */     public final String text;
/*     */ 
/*     */     private NoteKind(String paramString)
/*     */     {
/*  63 */       this.text = paramString;
/*     */     }
/*     */ 
/*     */     public abstract boolean match(Code_attribute.Exception_data paramException_data, int paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.TryBlockWriter
 * JD-Core Version:    0.6.2
 */