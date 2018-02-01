/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.util.LocatorImpl;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class Annotation
/*    */   implements Annotations<ElementWrapper, LocatorImpl, CommentListImpl>
/*    */ {
/* 59 */   private final DAnnotation a = new DAnnotation();
/*    */ 
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, LocatorImpl loc) throws BuildException {
/* 62 */     this.a.attributes.put(new QName(ns, localName, prefix), new DAnnotation.Attribute(ns, localName, prefix, value, loc));
/*    */   }
/*    */ 
/*    */   public void addElement(ElementWrapper ea) throws BuildException
/*    */   {
/* 67 */     this.a.contents.add(ea.element);
/*    */   }
/*    */ 
/*    */   public void addComment(CommentListImpl comments) throws BuildException {
/*    */   }
/*    */ 
/*    */   public void addLeadingComment(CommentListImpl comments) throws BuildException {
/*    */   }
/*    */ 
/*    */   DAnnotation getResult() {
/* 77 */     return this.a;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.Annotation
 * JD-Core Version:    0.6.2
 */