/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.classfile.Instruction;
/*    */ 
/*    */ public abstract class InstructionDetailWriter extends BasicWriter
/*    */ {
/*    */   InstructionDetailWriter(Context paramContext)
/*    */   {
/* 56 */     super(paramContext);
/*    */   }
/*    */ 
/*    */   abstract void writeDetails(Instruction paramInstruction);
/*    */ 
/*    */   void flush()
/*    */   {
/*    */   }
/*    */ 
/*    */   public static enum Kind
/*    */   {
/* 41 */     LOCAL_VARS("localVariables"), 
/* 42 */     LOCAL_VAR_TYPES("localVariableTypes"), 
/* 43 */     SOURCE("source"), 
/* 44 */     STACKMAPS("stackMaps"), 
/* 45 */     TRY_BLOCKS("tryBlocks"), 
/* 46 */     TYPE_ANNOS("typeAnnotations");
/*    */ 
/*    */     final String option;
/*    */ 
/* 49 */     private Kind(String paramString) { this.option = paramString; }
/*    */ 
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.InstructionDetailWriter
 * JD-Core Version:    0.6.2
 */