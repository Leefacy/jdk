/*    */ package com.sun.tools.doclets.internal.toolkit.builders;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class XMLNode
/*    */ {
/*    */   final XMLNode parent;
/*    */   final String name;
/*    */   final Map<String, String> attrs;
/*    */   final List<XMLNode> children;
/*    */ 
/*    */   XMLNode(XMLNode paramXMLNode, String paramString)
/*    */   {
/* 43 */     this.parent = paramXMLNode;
/* 44 */     this.name = paramString;
/* 45 */     this.attrs = new HashMap();
/* 46 */     this.children = new ArrayList();
/*    */ 
/* 48 */     if (paramXMLNode != null)
/* 49 */       paramXMLNode.children.add(this);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     StringBuilder localStringBuilder = new StringBuilder();
/* 55 */     localStringBuilder.append("<");
/* 56 */     localStringBuilder.append(this.name);
/* 57 */     for (Iterator localIterator = this.attrs.entrySet().iterator(); localIterator.hasNext(); ) { localObject = (Map.Entry)localIterator.next();
/* 58 */       localStringBuilder.append(" " + (String)((Map.Entry)localObject).getKey() + "=\"" + (String)((Map.Entry)localObject).getValue() + "\"");
/*    */     }
/*    */     Object localObject;
/* 59 */     if (this.children.size() == 0) {
/* 60 */       localStringBuilder.append("/>");
/*    */     } else {
/* 62 */       localStringBuilder.append(">");
/* 63 */       for (localIterator = this.children.iterator(); localIterator.hasNext(); ) { localObject = (XMLNode)localIterator.next();
/* 64 */         localStringBuilder.append(((XMLNode)localObject).toString()); }
/* 65 */       localStringBuilder.append("</" + this.name + ">");
/*    */     }
/* 67 */     return localStringBuilder.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.XMLNode
 * JD-Core Version:    0.6.2
 */