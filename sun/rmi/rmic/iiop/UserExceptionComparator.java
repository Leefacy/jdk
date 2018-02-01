/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.util.Comparator;
/*      */ 
/*      */ class UserExceptionComparator
/*      */   implements Comparator
/*      */ {
/*      */   public int compare(Object paramObject1, Object paramObject2)
/*      */   {
/* 2362 */     ValueType localValueType1 = (ValueType)paramObject1;
/* 2363 */     ValueType localValueType2 = (ValueType)paramObject2;
/* 2364 */     int i = 0;
/* 2365 */     if (isUserException(localValueType1)) {
/* 2366 */       if (!isUserException(localValueType2))
/* 2367 */         i = -1;
/*      */     }
/* 2369 */     else if ((isUserException(localValueType2)) && 
/* 2370 */       (!isUserException(localValueType1))) {
/* 2371 */       i = 1;
/*      */     }
/*      */ 
/* 2374 */     return i;
/*      */   }
/*      */ 
/*      */   final boolean isUserException(ValueType paramValueType) {
/* 2378 */     return (paramValueType.isIDLEntityException()) && (!paramValueType.isCORBAUserException());
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.UserExceptionComparator
 * JD-Core Version:    0.6.2
 */