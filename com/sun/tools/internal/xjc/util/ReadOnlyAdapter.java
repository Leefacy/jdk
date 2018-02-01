/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ 
/*    */ public abstract class ReadOnlyAdapter<OnTheWire, InMemory> extends XmlAdapter<OnTheWire, InMemory>
/*    */ {
/*    */   public final OnTheWire marshal(InMemory onTheWire)
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.util.ReadOnlyAdapter
 * JD-Core Version:    0.6.2
 */