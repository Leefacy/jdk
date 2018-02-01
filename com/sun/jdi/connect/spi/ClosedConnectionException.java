/*    */ package com.sun.jdi.connect.spi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class ClosedConnectionException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 3877032124297204774L;
/*    */ 
/*    */   public ClosedConnectionException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ClosedConnectionException(String paramString)
/*    */   {
/* 65 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.spi.ClosedConnectionException
 * JD-Core Version:    0.6.2
 */