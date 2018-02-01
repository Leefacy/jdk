/*    */ package com.sun.tools.internal.ws.spi;
/*    */ 
/*    */ import com.sun.tools.internal.ws.util.WSToolsObjectFactoryImpl;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public abstract class WSToolsObjectFactory
/*    */ {
/* 41 */   private static final WSToolsObjectFactory factory = new WSToolsObjectFactoryImpl();
/*    */ 
/*    */   public static WSToolsObjectFactory newInstance()
/*    */   {
/* 48 */     return factory;
/*    */   }
/*    */ 
/*    */   public abstract boolean wsimport(OutputStream paramOutputStream, Container paramContainer, String[] paramArrayOfString);
/*    */ 
/*    */   public boolean wsimport(OutputStream logStream, String[] args)
/*    */   {
/* 73 */     return wsimport(logStream, Container.NONE, args);
/*    */   }
/*    */ 
/*    */   public abstract boolean wsgen(OutputStream paramOutputStream, Container paramContainer, String[] paramArrayOfString);
/*    */ 
/*    */   public boolean wsgen(OutputStream logStream, String[] args)
/*    */   {
/* 97 */     return wsgen(logStream, Container.NONE, args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.spi.WSToolsObjectFactory
 * JD-Core Version:    0.6.2
 */