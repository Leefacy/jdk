/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Expression
/*    */ {
/*    */   private static int nextOrdinal;
/* 38 */   private boolean debug = Boolean.getBoolean("Expression.debug");
/*    */   private Expression left;
/*    */   private Expression right;
/*    */   private Operator operator;
/* 42 */   private int ordinal = nextOrdinal++;
/*    */ 
/*    */   Expression() {
/* 45 */     if (this.debug)
/* 46 */       System.out.println("Expression " + this.ordinal + " created");
/*    */   }
/*    */ 
/*    */   void setLeft(Expression paramExpression)
/*    */   {
/* 51 */     if (this.debug) {
/* 52 */       System.out.println("Setting left on " + this.ordinal + " to " + paramExpression);
/*    */     }
/* 54 */     this.left = paramExpression;
/*    */   }
/*    */ 
/*    */   Expression getLeft() {
/* 58 */     return this.left;
/*    */   }
/*    */ 
/*    */   void setRight(Expression paramExpression) {
/* 62 */     if (this.debug) {
/* 63 */       System.out.println("Setting right on " + this.ordinal + " to " + paramExpression);
/*    */     }
/* 65 */     this.right = paramExpression;
/*    */   }
/*    */ 
/*    */   Expression getRight() {
/* 69 */     return this.right;
/*    */   }
/*    */ 
/*    */   void setOperator(Operator paramOperator) {
/* 73 */     if (this.debug) {
/* 74 */       System.out.println("Setting operator on " + this.ordinal + " to " + paramOperator);
/*    */     }
/* 76 */     this.operator = paramOperator;
/*    */   }
/*    */ 
/*    */   Operator getOperator() {
/* 80 */     return this.operator;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 84 */     StringBuilder localStringBuilder = new StringBuilder();
/* 85 */     localStringBuilder.append('(');
/* 86 */     if (this.left != null) {
/* 87 */       localStringBuilder.append(this.left.toString());
/*    */     }
/* 89 */     if (this.operator != null) {
/* 90 */       localStringBuilder.append(this.operator.toString());
/* 91 */       if (this.right != null) {
/* 92 */         localStringBuilder.append(this.right.toString());
/*    */       }
/*    */     }
/* 95 */     localStringBuilder.append(')');
/* 96 */     return localStringBuilder.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Expression
 * JD-Core Version:    0.6.2
 */