/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ final class ElementWrapper
/*    */   implements ParsedElementAnnotation
/*    */ {
/*    */   final Element element;
/*    */ 
/*    */   public ElementWrapper(Element e)
/*    */   {
/* 58 */     this.element = e;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.ElementWrapper
 * JD-Core Version:    0.6.2
 */