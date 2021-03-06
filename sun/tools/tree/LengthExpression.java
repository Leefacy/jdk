/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class LengthExpression extends UnaryExpression
/*    */ {
/*    */   public LengthExpression(long paramLong, Expression paramExpression)
/*    */   {
/* 43 */     super(148, paramLong, Type.tInt, paramExpression);
/*    */   }
/*    */ 
/*    */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*    */   {
/* 50 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 51 */     if (!this.right.type.isType(9)) {
/* 52 */       paramEnvironment.error(this.where, "invalid.length", this.right.type);
/*    */     }
/* 54 */     return paramVset;
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 61 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 62 */     paramAssembler.add(this.where, 190);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.LengthExpression
 * JD-Core Version:    0.6.2
 */