/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class NaryExpression extends UnaryExpression
/*    */ {
/*    */   Expression[] args;
/*    */ 
/*    */   NaryExpression(int paramInt, long paramLong, Type paramType, Expression paramExpression, Expression[] paramArrayOfExpression)
/*    */   {
/* 44 */     super(paramInt, paramLong, paramType, paramExpression);
/* 45 */     this.args = paramArrayOfExpression;
/*    */   }
/*    */ 
/*    */   public Expression copyInline(Context paramContext)
/*    */   {
/* 52 */     NaryExpression localNaryExpression = (NaryExpression)clone();
/* 53 */     if (this.right != null) {
/* 54 */       localNaryExpression.right = this.right.copyInline(paramContext);
/*    */     }
/* 56 */     localNaryExpression.args = new Expression[this.args.length];
/* 57 */     for (int i = 0; i < this.args.length; i++) {
/* 58 */       if (this.args[i] != null) {
/* 59 */         localNaryExpression.args[i] = this.args[i].copyInline(paramContext);
/*    */       }
/*    */     }
/* 62 */     return localNaryExpression;
/*    */   }
/*    */ 
/*    */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*    */   {
/* 69 */     int i = 3;
/* 70 */     if (this.right != null)
/* 71 */       i += this.right.costInline(paramInt, paramEnvironment, paramContext);
/* 72 */     for (int j = 0; (j < this.args.length) && (i < paramInt); j++) {
/* 73 */       if (this.args[j] != null) {
/* 74 */         i += this.args[j].costInline(paramInt, paramEnvironment, paramContext);
/*    */       }
/*    */     }
/* 77 */     return i;
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 84 */     paramPrintStream.print("(" + opNames[this.op] + "#" + hashCode());
/* 85 */     if (this.right != null) {
/* 86 */       paramPrintStream.print(" ");
/* 87 */       this.right.print(paramPrintStream);
/*    */     }
/* 89 */     for (int i = 0; i < this.args.length; i++) {
/* 90 */       paramPrintStream.print(" ");
/* 91 */       if (this.args[i] != null)
/* 92 */         this.args[i].print(paramPrintStream);
/*    */       else {
/* 94 */         paramPrintStream.print("<null>");
/*    */       }
/*    */     }
/* 97 */     paramPrintStream.print(")");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.NaryExpression
 * JD-Core Version:    0.6.2
 */