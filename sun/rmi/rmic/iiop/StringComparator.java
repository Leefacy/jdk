/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.util.Comparator;
/*      */ 
/*      */ class StringComparator
/*      */   implements Comparator
/*      */ {
/*      */   public int compare(Object paramObject1, Object paramObject2)
/*      */   {
/* 2353 */     String str1 = (String)paramObject1;
/* 2354 */     String str2 = (String)paramObject2;
/* 2355 */     return str1.compareTo(str2);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.StringComparator
 * JD-Core Version:    0.6.2
 */