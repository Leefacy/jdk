/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ class NoPragma extends PragmaHandler
/*    */ {
/*    */   public boolean process(String paramString1, String paramString2)
/*    */     throws IOException
/*    */   {
/* 46 */     parseException(Util.getMessage("Preprocessor.unknownPragma", paramString1));
/* 47 */     skipToEOL();
/* 48 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.NoPragma
 * JD-Core Version:    0.6.2
 */