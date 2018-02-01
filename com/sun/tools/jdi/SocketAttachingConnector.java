/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import com.sun.jdi.connect.Connector.Argument;
/*    */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*    */ import com.sun.jdi.connect.Transport;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SocketAttachingConnector extends GenericAttachingConnector
/*    */ {
/*    */   static final String ARG_PORT = "port";
/*    */   static final String ARG_HOST = "hostname";
/*    */ 
/*    */   public SocketAttachingConnector()
/*    */   {
/* 45 */     super(new SocketTransportService());
/*    */     String str;
/*    */     try
/*    */     {
/* 49 */       str = InetAddress.getLocalHost().getHostName();
/*    */     } catch (UnknownHostException localUnknownHostException) {
/* 51 */       str = "";
/*    */     }
/*    */ 
/* 54 */     addStringArgument("hostname", 
/* 56 */       getString("socket_attaching.host.label"), 
/* 57 */       getString("socket_attaching.host"), 
/* 57 */       str, false);
/*    */ 
/* 61 */     addIntegerArgument("port", 
/* 63 */       getString("socket_attaching.port.label"), 
/* 64 */       getString("socket_attaching.port"), 
/* 64 */       "", true, 0, 2147483647);
/*    */ 
/* 69 */     this.transport = new Transport() {
/*    */       public String name() {
/* 71 */         return "dt_socket";
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public VirtualMachine attach(Map<String, ? extends Connector.Argument> paramMap)
/*    */     throws IOException, IllegalConnectorArgumentsException
/*    */   {
/* 85 */     String str1 = argument("hostname", paramMap).value();
/* 86 */     if (str1.length() > 0) {
/* 87 */       str1 = str1 + ":";
/*    */     }
/* 89 */     String str2 = str1 + argument("port", paramMap).value();
/* 90 */     return super.attach(str2, paramMap);
/*    */   }
/*    */ 
/*    */   public String name() {
/* 94 */     return "com.sun.jdi.SocketAttach";
/*    */   }
/*    */ 
/*    */   public String description() {
/* 98 */     return getString("socket_attaching.description");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.SocketAttachingConnector
 * JD-Core Version:    0.6.2
 */