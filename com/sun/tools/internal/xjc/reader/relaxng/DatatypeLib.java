/*    */ package com.sun.tools.internal.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class DatatypeLib
/*    */ {
/*    */   public final String nsUri;
/* 48 */   private final Map<String, TypeUse> types = new HashMap();
/*    */ 
/* 64 */   public static final DatatypeLib BUILTIN = new DatatypeLib("");
/*    */ 
/* 69 */   public static final DatatypeLib XMLSCHEMA = new DatatypeLib("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */ 
/*    */   public DatatypeLib(String nsUri)
/*    */   {
/* 51 */     this.nsUri = nsUri;
/*    */   }
/*    */ 
/*    */   TypeUse get(String name)
/*    */   {
/* 58 */     return (TypeUse)this.types.get(name);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 72 */     BUILTIN.types.put("token", CBuiltinLeafInfo.TOKEN);
/* 73 */     BUILTIN.types.put("string", CBuiltinLeafInfo.STRING);
/* 74 */     XMLSCHEMA.types.putAll(SimpleTypeBuilder.builtinConversions);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.relaxng.DatatypeLib
 * JD-Core Version:    0.6.2
 */