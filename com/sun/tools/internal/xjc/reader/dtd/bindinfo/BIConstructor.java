/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class BIConstructor
/*    */ {
/*    */   private final Element dom;
/*    */   private final String[] properties;
/*    */ 
/*    */   BIConstructor(Element _node)
/*    */   {
/* 53 */     this.dom = _node;
/*    */ 
/* 56 */     StringTokenizer tokens = new StringTokenizer(
/* 56 */       DOMUtil.getAttribute(_node, "properties"));
/*    */ 
/* 58 */     List vec = new ArrayList();
/* 59 */     while (tokens.hasMoreTokens())
/* 60 */       vec.add(tokens.nextToken());
/* 61 */     this.properties = ((String[])vec.toArray(new String[0]));
/*    */ 
/* 63 */     if (this.properties.length == 0)
/* 64 */       throw new AssertionError("this error should be catched by the validator");
/*    */   }
/*    */ 
/*    */   public void createDeclaration(CClassInfo cls)
/*    */   {
/* 81 */     cls.addConstructor(this.properties);
/*    */   }
/*    */ 
/*    */   public Locator getSourceLocation()
/*    */   {
/* 86 */     return DOMLocator.getLocationInfo(this.dom);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIConstructor
 * JD-Core Version:    0.6.2
 */