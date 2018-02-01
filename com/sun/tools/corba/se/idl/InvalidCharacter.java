/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InvalidCharacter extends IOException
/*    */ {
/* 64 */   private String message = null;
/*    */ 
/*    */   public InvalidCharacter(String paramString1, String paramString2, int paramInt1, int paramInt2, char paramChar)
/*    */   {
/* 47 */     String str = "^";
/* 48 */     if (paramInt2 > 1)
/*    */     {
/* 50 */       localObject = new byte[paramInt2 - 1];
/* 51 */       for (int i = 0; i < paramInt2 - 1; i++)
/* 52 */         localObject[i] = 32;
/* 53 */       str = new String((byte[])localObject) + str;
/*    */     }
/* 55 */     Object localObject = { paramString1, Integer.toString(paramInt1), "" + paramChar, Integer.toString(paramChar), paramString2, str };
/* 56 */     this.message = Util.getMessage("InvalidCharacter.1", (String[])localObject);
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 61 */     return this.message;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.InvalidCharacter
 * JD-Core Version:    0.6.2
 */