/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlEnum;
/*    */ import javax.xml.bind.annotation.XmlEnumValue;
/*    */ 
/*    */ @XmlEnum
/*    */ public enum EnumMemberMode
/*    */ {
/* 38 */   SKIP, 
/*    */ 
/* 40 */   ERROR, 
/*    */ 
/* 42 */   GENERATE;
/*    */ 
/*    */   public EnumMemberMode getModeWithEnum()
/*    */   {
/* 51 */     if (this == SKIP) return ERROR;
/* 52 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.EnumMemberMode
 * JD-Core Version:    0.6.2
 */