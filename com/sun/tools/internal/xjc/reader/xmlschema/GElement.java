/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.reader.gbind.Element;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ abstract class GElement extends Element
/*    */ {
/* 43 */   final Set<XSParticle> particles = new HashSet();
/*    */ 
/*    */   abstract String getPropertyNameSeed();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.GElement
 * JD-Core Version:    0.6.2
 */