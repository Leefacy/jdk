/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ class Util
/*    */ {
/*    */   static final String MAGIC = "=:";
/*    */   static final String MAGIC0 = "=:0";
/*    */   static final String MAGIC1 = "=:1";
/*    */   static final String MAGIC2 = "=:2";
/*    */ 
/*    */   static String replace(String macro, String[] args)
/*    */   {
/* 36 */     int len = macro.length();
/* 37 */     StringBuilder buf = new StringBuilder(len);
/* 38 */     for (int i = 0; i < len; i++) {
/* 39 */       char ch = macro.charAt(i);
/* 40 */       if ((ch == '=') && (i + 2 < len)) {
/* 41 */         char tail = macro.charAt(i + 1);
/* 42 */         char ch2 = macro.charAt(i + 2);
/* 43 */         if (('0' <= ch2) && (ch2 <= '9') && (tail == ':')) {
/* 44 */           buf.append(args[(ch2 - '0')]);
/* 45 */           i += 2;
/* 46 */           continue;
/*    */         }
/*    */       }
/* 49 */       buf.append(ch);
/*    */     }
/* 51 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   static String createMacroTemplate(String s)
/*    */   {
/* 58 */     return s;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.Util
 * JD-Core Version:    0.6.2
 */