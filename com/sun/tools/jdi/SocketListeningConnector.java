/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.connect.Connector.Argument;
/*    */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*    */ import com.sun.jdi.connect.Transport;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SocketListeningConnector extends GenericListeningConnector
/*    */ {
/*    */   static final String ARG_PORT = "port";
/*    */   static final String ARG_LOCALADDR = "localAddress";
/*    */ 
/*    */   public SocketListeningConnector()
/*    */   {
/* 42 */     super(new SocketTransportService());
/*    */ 
/* 44 */     addIntegerArgument("port", 
/* 46 */       getString("socket_listening.port.label"), 
/* 47 */       getString("socket_listening.port"), 
/* 47 */       "", false, 0, 2147483647);
/*    */ 
/* 52 */     addStringArgument("localAddress", 
/* 54 */       getString("socket_listening.localaddr.label"), 
/* 55 */       getString("socket_listening.localaddr"), 
/* 55 */       "", false);
/*    */ 
/* 59 */     this.transport = new Transport() {
/*    */       public String name() {
/* 61 */         return "dt_socket";
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public String startListening(Map<String, ? extends Connector.Argument> paramMap)
/*    */     throws IOException, IllegalConnectorArgumentsException
/*    */   {
/* 71 */     String str1 = argument("port", paramMap).value();
/* 72 */     String str2 = argument("localAddress", paramMap).value();
/*    */ 
/* 75 */     if (str1.length() == 0) {
/* 76 */       str1 = "0";
/*    */     }
/*    */ 
/* 79 */     if (str2.length() > 0)
/* 80 */       str2 = str2 + ":" + str1;
/*    */     else {
/* 82 */       str2 = str1;
/*    */     }
/*    */ 
/* 85 */     return super.startListening(str2, paramMap);
/*    */   }
/*    */ 
/*    */   public String name() {
/* 89 */     return "com.sun.jdi.SocketListen";
/*    */   }
/*    */ 
/*    */   public String description() {
/* 93 */     return getString("socket_listening.description");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SocketListeningConnector
 * JD-Core Version:    0.6.2
 */