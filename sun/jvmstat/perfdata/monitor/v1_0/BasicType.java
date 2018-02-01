/*    */ package sun.jvmstat.perfdata.monitor.v1_0;
/*    */ 
/*    */ public class BasicType
/*    */ {
/*    */   private final String name;
/*    */   private final int value;
/* 43 */   public static final BasicType BOOLEAN = new BasicType("boolean", 4);
/* 44 */   public static final BasicType CHAR = new BasicType("char", 5);
/* 45 */   public static final BasicType FLOAT = new BasicType("float", 6);
/* 46 */   public static final BasicType DOUBLE = new BasicType("double", 7);
/* 47 */   public static final BasicType BYTE = new BasicType("byte", 8);
/* 48 */   public static final BasicType SHORT = new BasicType("short", 9);
/* 49 */   public static final BasicType INT = new BasicType("int", 10);
/* 50 */   public static final BasicType LONG = new BasicType("long", 11);
/* 51 */   public static final BasicType OBJECT = new BasicType("object", 12);
/* 52 */   public static final BasicType ARRAY = new BasicType("array", 13);
/* 53 */   public static final BasicType VOID = new BasicType("void", 14);
/* 54 */   public static final BasicType ADDRESS = new BasicType("address", 15);
/* 55 */   public static final BasicType ILLEGAL = new BasicType("illegal", 99);
/*    */ 
/* 57 */   private static BasicType[] basicTypes = { BOOLEAN, CHAR, FLOAT, DOUBLE, BYTE, SHORT, INT, LONG, OBJECT, ARRAY, VOID, ADDRESS, ILLEGAL };
/*    */ 
/*    */   public String toString()
/*    */   {
/* 68 */     return this.name;
/*    */   }
/*    */ 
/*    */   public int intValue()
/*    */   {
/* 77 */     return this.value;
/*    */   }
/*    */ 
/*    */   public static BasicType toBasicType(int paramInt)
/*    */   {
/* 88 */     for (int i = 0; i < basicTypes.length; i++) {
/* 89 */       if (basicTypes[i].intValue() == i) {
/* 90 */         return basicTypes[i];
/*    */       }
/*    */     }
/* 93 */     return ILLEGAL;
/*    */   }
/*    */ 
/*    */   private BasicType(String paramString, int paramInt) {
/* 97 */     this.name = paramString;
/* 98 */     this.value = paramInt;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.v1_0.BasicType
 * JD-Core Version:    0.6.2
 */