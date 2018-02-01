/*    */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*    */ import com.sun.tools.corba.se.idl.MethodEntry;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class AttributeGen24 extends MethodGenClone24
/*    */ {
/*    */   protected void abstractMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*    */   {
/* 71 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*    */ 
/* 74 */     super.abstractMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/*    */ 
/* 77 */     if (!localAttributeEntry.readOnly())
/*    */     {
/* 79 */       setupForSetMethod();
/* 80 */       super.abstractMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/* 81 */       clear();
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*    */   {
/* 90 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*    */ 
/* 93 */     super.interfaceMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/*    */ 
/* 96 */     if (!localAttributeEntry.readOnly())
/*    */     {
/* 98 */       setupForSetMethod();
/* 99 */       super.interfaceMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/* 100 */       clear();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.AttributeGen24
 * JD-Core Version:    0.6.2
 */