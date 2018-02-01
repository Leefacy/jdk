/*    */ package com.sun.tools.internal.xjc.api;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.impl.s2j.SchemaCompilerImpl;
/*    */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*    */ 
/*    */ public final class XJC
/*    */ {
/*    */   public static SchemaCompiler createSchemaCompiler()
/*    */   {
/* 47 */     return new SchemaCompilerImpl();
/*    */   }
/*    */ 
/*    */   public static String getDefaultPackageName(String namespaceUri)
/*    */   {
/* 63 */     if (namespaceUri == null) throw new IllegalArgumentException();
/* 64 */     return NameConverter.standard.toPackageName(namespaceUri);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.XJC
 * JD-Core Version:    0.6.2
 */