/*    */ package com.sun.codemodel.internal.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ public class EncoderFactory
/*    */ {
/*    */   public static CharsetEncoder createEncoder(String encodin)
/*    */   {
/* 46 */     Charset cs = Charset.forName(System.getProperty("file.encoding"));
/* 47 */     CharsetEncoder encoder = cs.newEncoder();
/*    */ 
/* 49 */     if (cs.getClass().getName().equals("sun.nio.cs.MS1252"))
/*    */     {
/*    */       try
/*    */       {
/* 58 */         Class ms1252encoder = Class.forName("com.sun.codemodel.internal.util.MS1252Encoder");
/* 59 */         Constructor c = ms1252encoder.getConstructor(new Class[] { Charset.class });
/*    */ 
/* 62 */         return (CharsetEncoder)c.newInstance(new Object[] { cs });
/*    */       }
/*    */       catch (Throwable t)
/*    */       {
/* 67 */         return encoder;
/*    */       }
/*    */     }
/*    */ 
/* 71 */     return encoder;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.util.EncoderFactory
 * JD-Core Version:    0.6.2
 */