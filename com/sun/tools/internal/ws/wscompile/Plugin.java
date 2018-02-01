/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.Model;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class Plugin
/*    */ {
/*    */   public abstract String getOptionName();
/*    */ 
/*    */   public abstract String getUsage();
/*    */ 
/*    */   public int parseArgument(Options opt, String[] args, int i)
/*    */     throws BadCommandLineException, IOException
/*    */   {
/* 89 */     return 0;
/*    */   }
/*    */ 
/*    */   public void onActivated(Options opts)
/*    */     throws BadCommandLineException
/*    */   {
/*    */   }
/*    */ 
/*    */   public abstract boolean run(Model paramModel, WsimportOptions paramWsimportOptions, ErrorReceiver paramErrorReceiver)
/*    */     throws SAXException;
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wscompile.Plugin
 * JD-Core Version:    0.6.2
 */