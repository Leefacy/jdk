/*    */ package com.sun.xml.internal.dtdparser;
/*    */ 
/*    */ final class InternalEntity extends EntityDecl
/*    */ {
/*    */   char[] buf;
/*    */ 
/*    */   InternalEntity(String name, char[] value)
/*    */   {
/* 31 */     this.name = name;
/* 32 */     this.buf = value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.dtdparser.InternalEntity
 * JD-Core Version:    0.6.2
 */