/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Type;
/*     */ 
/*     */ public class Constants
/*     */ {
/*     */   public static Object decode(Object paramObject, Type paramType)
/*     */   {
/*  47 */     if ((paramObject instanceof Integer)) {
/*  48 */       int i = ((Integer)paramObject).intValue();
/*  49 */       switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 1:
/*  50 */         return Boolean.valueOf(i != 0);
/*     */       case 2:
/*  51 */         return Character.valueOf((char)i);
/*     */       case 3:
/*  52 */         return Byte.valueOf((byte)i);
/*     */       case 4:
/*  53 */         return Short.valueOf((short)i);
/*     */       }
/*     */     }
/*  56 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public static String format(Object paramObject, Type paramType)
/*     */   {
/*  64 */     paramObject = decode(paramObject, paramType);
/*  65 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 3:
/*  66 */       return formatByte(((Byte)paramObject).byteValue());
/*     */     case 5:
/*  67 */       return formatLong(((Long)paramObject).longValue());
/*     */     case 6:
/*  68 */       return formatFloat(((Float)paramObject).floatValue());
/*     */     case 7:
/*  69 */       return formatDouble(((Double)paramObject).doubleValue());
/*     */     case 2:
/*  70 */       return formatChar(((Character)paramObject).charValue());
/*     */     case 4: }
/*  72 */     if ((paramObject instanceof String))
/*  73 */       return formatString((String)paramObject);
/*  74 */     return paramObject + "";
/*     */   }
/*     */ 
/*     */   public static String format(Object paramObject)
/*     */   {
/*  83 */     if ((paramObject instanceof Byte)) return formatByte(((Byte)paramObject).byteValue());
/*  84 */     if ((paramObject instanceof Short)) return formatShort(((Short)paramObject).shortValue());
/*  85 */     if ((paramObject instanceof Long)) return formatLong(((Long)paramObject).longValue());
/*  86 */     if ((paramObject instanceof Float)) return formatFloat(((Float)paramObject).floatValue());
/*  87 */     if ((paramObject instanceof Double)) return formatDouble(((Double)paramObject).doubleValue());
/*  88 */     if ((paramObject instanceof Character)) return formatChar(((Character)paramObject).charValue());
/*  89 */     if ((paramObject instanceof String)) return formatString((String)paramObject);
/*  90 */     if (((paramObject instanceof Integer)) || ((paramObject instanceof Boolean))) {
/*  91 */       return paramObject.toString();
/*     */     }
/*     */ 
/*  97 */     throw new IllegalArgumentException("Argument is not a primitive type or a string; it " + (paramObject == null ? "is a null value." : new StringBuilder().append("has class ")
/*  97 */       .append(paramObject
/*  97 */       .getClass().getName()).toString()) + ".");
/*     */   }
/*     */ 
/*     */   private static String formatByte(byte paramByte) {
/* 101 */     return String.format("(byte)0x%02x", new Object[] { Byte.valueOf(paramByte) });
/*     */   }
/*     */ 
/*     */   private static String formatShort(short paramShort) {
/* 105 */     return String.format("(short)%d", new Object[] { Short.valueOf(paramShort) });
/*     */   }
/*     */ 
/*     */   private static String formatLong(long paramLong) {
/* 109 */     return paramLong + "L";
/*     */   }
/*     */ 
/*     */   private static String formatFloat(float paramFloat) {
/* 113 */     if (Float.isNaN(paramFloat))
/* 114 */       return "0.0f/0.0f";
/* 115 */     if (Float.isInfinite(paramFloat)) {
/* 116 */       return paramFloat < 0.0F ? "-1.0f/0.0f" : "1.0f/0.0f";
/*     */     }
/* 118 */     return paramFloat + "f";
/*     */   }
/*     */ 
/*     */   private static String formatDouble(double paramDouble) {
/* 122 */     if (Double.isNaN(paramDouble))
/* 123 */       return "0.0/0.0";
/* 124 */     if (Double.isInfinite(paramDouble)) {
/* 125 */       return paramDouble < 0.0D ? "-1.0/0.0" : "1.0/0.0";
/*     */     }
/* 127 */     return paramDouble + "";
/*     */   }
/*     */ 
/*     */   private static String formatChar(char paramChar) {
/* 131 */     return '\'' + Convert.quote(paramChar) + '\'';
/*     */   }
/*     */ 
/*     */   private static String formatString(String paramString) {
/* 135 */     return '"' + Convert.quote(paramString) + '"';
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Constants
 * JD-Core Version:    0.6.2
 */