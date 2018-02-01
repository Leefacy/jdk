/*    */ package com.sun.xml.internal.rngom.ast.util;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class LocatorImpl
/*    */   implements Locator, Location
/*    */ {
/*    */   private final String systemId;
/*    */   private final int lineNumber;
/*    */   private final int columnNumber;
/*    */ 
/*    */   public LocatorImpl(String systemId, int lineNumber, int columnNumber)
/*    */   {
/* 60 */     this.systemId = systemId;
/* 61 */     this.lineNumber = lineNumber;
/* 62 */     this.columnNumber = columnNumber;
/*    */   }
/*    */ 
/*    */   public String getPublicId() {
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */   public String getSystemId() {
/* 70 */     return this.systemId;
/*    */   }
/*    */ 
/*    */   public int getLineNumber() {
/* 74 */     return this.lineNumber;
/*    */   }
/*    */ 
/*    */   public int getColumnNumber() {
/* 78 */     return this.columnNumber;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.util.LocatorImpl
 * JD-Core Version:    0.6.2
 */