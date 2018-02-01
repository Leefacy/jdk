/*    */ package com.sun.tools.internal.xjc.api.util;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public final class ToolsJarNotFoundException extends Exception
/*    */ {
/*    */   public final File toolsJar;
/*    */ 
/*    */   public ToolsJarNotFoundException(File toolsJar)
/*    */   {
/* 44 */     super(calcMessage(toolsJar));
/* 45 */     this.toolsJar = toolsJar;
/*    */   }
/*    */ 
/*    */   private static String calcMessage(File toolsJar) {
/* 49 */     return Messages.TOOLS_JAR_NOT_FOUND.format(new Object[] { toolsJar.getPath() });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.util.ToolsJarNotFoundException
 * JD-Core Version:    0.6.2
 */