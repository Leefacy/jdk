/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ public class ArrayUtils
/*    */ {
/*    */   private static int calculateNewLength(int paramInt1, int paramInt2)
/*    */   {
/* 38 */     while (paramInt1 < paramInt2 + 1)
/* 39 */       paramInt1 *= 2;
/* 40 */     return paramInt1;
/*    */   }
/*    */ 
/*    */   public static <T> T[] ensureCapacity(T[] paramArrayOfT, int paramInt) {
/* 44 */     if (paramInt < paramArrayOfT.length) {
/* 45 */       return paramArrayOfT;
/*    */     }
/* 47 */     int i = calculateNewLength(paramArrayOfT.length, paramInt);
/*    */ 
/* 49 */     Object[] arrayOfObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/* 50 */     System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
/* 51 */     return arrayOfObject;
/*    */   }
/*    */ 
/*    */   public static byte[] ensureCapacity(byte[] paramArrayOfByte, int paramInt)
/*    */   {
/* 56 */     if (paramInt < paramArrayOfByte.length) {
/* 57 */       return paramArrayOfByte;
/*    */     }
/* 59 */     int i = calculateNewLength(paramArrayOfByte.length, paramInt);
/* 60 */     byte[] arrayOfByte = new byte[i];
/* 61 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
/* 62 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static char[] ensureCapacity(char[] paramArrayOfChar, int paramInt)
/*    */   {
/* 67 */     if (paramInt < paramArrayOfChar.length) {
/* 68 */       return paramArrayOfChar;
/*    */     }
/* 70 */     int i = calculateNewLength(paramArrayOfChar.length, paramInt);
/* 71 */     char[] arrayOfChar = new char[i];
/* 72 */     System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
/* 73 */     return arrayOfChar;
/*    */   }
/*    */ 
/*    */   public static int[] ensureCapacity(int[] paramArrayOfInt, int paramInt)
/*    */   {
/* 78 */     if (paramInt < paramArrayOfInt.length) {
/* 79 */       return paramArrayOfInt;
/*    */     }
/* 81 */     int i = calculateNewLength(paramArrayOfInt.length, paramInt);
/* 82 */     int[] arrayOfInt = new int[i];
/* 83 */     System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
/* 84 */     return arrayOfInt;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.ArrayUtils
 * JD-Core Version:    0.6.2
 */