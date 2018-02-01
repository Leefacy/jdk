/*    */ package sun.tools.jstat;
/*    */ 
/*    */ public class Identifier extends Expression
/*    */ {
/*    */   private String name;
/*    */   private Object value;
/*    */ 
/*    */   public Identifier(String paramString)
/*    */   {
/* 42 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setValue(Object paramObject) {
/* 50 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 54 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean isResolved() {
/* 58 */     return this.value != null;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 62 */     return this.name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Identifier
 * JD-Core Version:    0.6.2
 */