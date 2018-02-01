/*    */ package com.sun.tools.internal.xjc.reader.internalizer;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ 
/*    */ public final class LocatorTable
/*    */ {
/* 43 */   private final Map startLocations = new HashMap();
/*    */ 
/* 46 */   private final Map endLocations = new HashMap();
/*    */ 
/*    */   public void storeStartLocation(Element e, Locator loc) {
/* 49 */     this.startLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */ 
/*    */   public void storeEndLocation(Element e, Locator loc) {
/* 53 */     this.endLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */ 
/*    */   public Locator getStartLocation(Element e) {
/* 57 */     return (Locator)this.startLocations.get(e);
/*    */   }
/*    */ 
/*    */   public Locator getEndLocation(Element e) {
/* 61 */     return (Locator)this.endLocations.get(e);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.internalizer.LocatorTable
 * JD-Core Version:    0.6.2
 */