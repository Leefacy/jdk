/*    */ package com.sun.jdi.connect;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class TransportTimeoutException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 4107035242623365074L;
/*    */ 
/*    */   public TransportTimeoutException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TransportTimeoutException(String paramString)
/*    */   {
/* 75 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.TransportTimeoutException
 * JD-Core Version:    0.6.2
 */