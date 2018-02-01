/*    */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.ModuleEntry;
/*    */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class ModuleGen
/*    */   implements com.sun.tools.corba.se.idl.ModuleGen
/*    */ {
/*    */   public void generate(Hashtable paramHashtable, ModuleEntry paramModuleEntry, PrintWriter paramPrintWriter)
/*    */   {
/* 66 */     String str = Util.containerFullName(paramModuleEntry);
/* 67 */     Util.mkdir(str);
/*    */ 
/* 70 */     Enumeration localEnumeration = paramModuleEntry.contained().elements();
/* 71 */     while (localEnumeration.hasMoreElements())
/*    */     {
/* 73 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/* 74 */       if (localSymtabEntry.emit())
/* 75 */         localSymtabEntry.generate(paramHashtable, paramPrintWriter);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ModuleGen
 * JD-Core Version:    0.6.2
 */