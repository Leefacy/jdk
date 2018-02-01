/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class TypeExpression extends Expression
/*    */ {
/*    */   public TypeExpression(long paramLong, Type paramType)
/*    */   {
/* 43 */     super(147, paramLong, paramType);
/*    */   }
/*    */ 
/*    */   Type toType(Environment paramEnvironment, Context paramContext)
/*    */   {
/* 50 */     return this.type;
/*    */   }
/*    */ 
/*    */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*    */   {
/* 57 */     paramEnvironment.error(this.where, "invalid.term");
/* 58 */     this.type = Type.tError;
/* 59 */     return paramVset;
/*    */   }
/*    */ 
/*    */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression)
/*    */   {
/* 64 */     return paramVset;
/*    */   }
/*    */ 
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 75 */     paramPrintStream.print(this.type.toString());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.TypeExpression
 * JD-Core Version:    0.6.2
 */