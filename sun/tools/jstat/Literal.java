/*    */ package sun.tools.jstat;
/*    */ 
/*    */ public class Literal extends Expression
/*    */ {
/*    */   private Object value;
/*    */ 
/*    */   public Literal(Object paramObject)
/*    */   {
/* 41 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */ 
/*    */   public void setValue(Object paramObject) {
/* 49 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 53 */     return this.value.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Literal
 * JD-Core Version:    0.6.2
 */