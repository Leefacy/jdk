/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.ClassNotFound;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class BinaryEqualityExpression extends BinaryExpression
/*    */ {
/*    */   public BinaryEqualityExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 41 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt)
/*    */   {
/* 49 */     if ((paramInt & 0x2000) != 0)
/*    */     {
/* 51 */       return;
/* 52 */     }if ((paramInt & 0x700) != 0) {
/*    */       try {
/* 54 */         if ((paramEnvironment.explicitCast(this.left.type, this.right.type)) || 
/* 55 */           (paramEnvironment
/* 55 */           .explicitCast(this.right.type, this.left.type)))
/*    */         {
/* 56 */           return;
/*    */         }
/* 58 */         paramEnvironment.error(this.where, "incompatible.type", this.left.type, this.left.type, this.right.type);
/*    */       }
/*    */       catch (ClassNotFound localClassNotFound) {
/* 61 */         paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, opNames[this.op]);
/*    */       }
/*    */       return;
/*    */     }
/*    */     Type localType;
/* 64 */     if ((paramInt & 0x80) != 0)
/* 65 */       localType = Type.tDouble;
/* 66 */     else if ((paramInt & 0x40) != 0)
/* 67 */       localType = Type.tFloat;
/* 68 */     else if ((paramInt & 0x20) != 0)
/* 69 */       localType = Type.tLong;
/* 70 */     else if ((paramInt & 0x1) != 0)
/* 71 */       localType = Type.tBoolean;
/*    */     else {
/* 73 */       localType = Type.tInt;
/*    */     }
/* 75 */     this.left = convert(paramEnvironment, paramContext, localType, this.left);
/* 76 */     this.right = convert(paramEnvironment, paramContext, localType, this.right);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BinaryEqualityExpression
 * JD-Core Version:    0.6.2
 */