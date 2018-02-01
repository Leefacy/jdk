/*    */ package com.sun.tools.internal.ws;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.Version;
/*    */ 
/*    */ public abstract class ToolVersion
/*    */ {
/* 37 */   public static final Version VERSION = Version.create(ToolVersion.class.getResourceAsStream("version.properties"));
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.ToolVersion
 * JD-Core Version:    0.6.2
 */