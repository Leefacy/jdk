/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Label;
/*    */ 
/*    */ class CodeContext extends Context
/*    */ {
/*    */   Label breakLabel;
/*    */   Label contLabel;
/*    */ 
/*    */   CodeContext(Context paramContext, Node paramNode)
/*    */   {
/* 44 */     super(paramContext, paramNode);
/* 45 */     switch (paramNode.op) {
/*    */     case 92:
/*    */     case 93:
/*    */     case 94:
/*    */     case 103:
/*    */     case 126:
/* 51 */       this.breakLabel = new Label();
/* 52 */       this.contLabel = new Label();
/* 53 */       break;
/*    */     case 95:
/*    */     case 101:
/*    */     case 150:
/*    */     case 151:
/* 58 */       this.breakLabel = new Label();
/* 59 */       break;
/*    */     default:
/* 61 */       if (((paramNode instanceof Statement)) && (((Statement)paramNode).labels != null))
/* 62 */         this.breakLabel = new Label();
/*    */       break;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.CodeContext
 * JD-Core Version:    0.6.2
 */