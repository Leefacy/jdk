/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.AnyNameExceptNameClass;
/*    */ import com.sun.xml.internal.rngom.nc.ChoiceNameClass;
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ import com.sun.xml.internal.rngom.nc.NsNameClass;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ import com.sun.xml.internal.xsom.XSWildcard.Any;
/*    */ import com.sun.xml.internal.xsom.XSWildcard.Other;
/*    */ import com.sun.xml.internal.xsom.XSWildcard.Union;
/*    */ import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public final class WildcardNameClassBuilder
/*    */   implements XSWildcardFunction<NameClass>
/*    */ {
/* 50 */   private static final XSWildcardFunction<NameClass> theInstance = new WildcardNameClassBuilder();
/*    */ 
/*    */   public static NameClass build(XSWildcard wc)
/*    */   {
/* 54 */     return (NameClass)wc.apply(theInstance);
/*    */   }
/*    */ 
/*    */   public NameClass any(XSWildcard.Any wc) {
/* 58 */     return NameClass.ANY;
/*    */   }
/*    */ 
/*    */   public NameClass other(XSWildcard.Other wc)
/*    */   {
/* 65 */     return new AnyNameExceptNameClass(new ChoiceNameClass(new NsNameClass(""), new NsNameClass(wc
/* 65 */       .getOtherNamespace())));
/*    */   }
/*    */ 
/*    */   public NameClass union(XSWildcard.Union wc) {
/* 69 */     NameClass nc = null;
/* 70 */     for (Iterator itr = wc.iterateNamespaces(); itr.hasNext(); ) {
/* 71 */       String ns = (String)itr.next();
/*    */ 
/* 73 */       if (nc == null) nc = new NsNameClass(ns);
/*    */       else {
/* 75 */         nc = new ChoiceNameClass(nc, new NsNameClass(ns));
/*    */       }
/*    */     }
/*    */ 
/* 79 */     assert (nc != null);
/*    */ 
/* 81 */     return nc;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.WildcardNameClassBuilder
 * JD-Core Version:    0.6.2
 */