/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ 
/*    */ public final class NamespaceContextAdapter
/*    */   implements NamespaceContext
/*    */ {
/*    */   private XmlString xstr;
/*    */ 
/*    */   public NamespaceContextAdapter(XmlString xstr)
/*    */   {
/* 46 */     this.xstr = xstr;
/*    */   }
/*    */ 
/*    */   public String getNamespaceURI(String prefix) {
/* 50 */     return this.xstr.resolvePrefix(prefix);
/*    */   }
/*    */ 
/*    */   public String getPrefix(String namespaceURI) {
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   public Iterator getPrefixes(String namespaceURI) {
/* 58 */     return Collections.EMPTY_LIST.iterator();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.util.NamespaceContextAdapter
 * JD-Core Version:    0.6.2
 */