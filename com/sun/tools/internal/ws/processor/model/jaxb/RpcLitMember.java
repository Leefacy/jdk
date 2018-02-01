/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class RpcLitMember extends AbstractType
/*    */ {
/*    */   private String javaTypeName;
/*    */   private QName schemaTypeName;
/*    */ 
/*    */   public RpcLitMember()
/*    */   {
/*    */   }
/*    */ 
/*    */   public RpcLitMember(QName name, String javaTypeName)
/*    */   {
/* 51 */     setName(name);
/* 52 */     this.javaTypeName = javaTypeName;
/*    */   }
/*    */   public RpcLitMember(QName name, String javaTypeName, QName schemaTypeName) {
/* 55 */     setName(name);
/* 56 */     this.javaTypeName = javaTypeName;
/* 57 */     this.schemaTypeName = schemaTypeName;
/*    */   }
/*    */ 
/*    */   public String getJavaTypeName()
/*    */   {
/* 64 */     return this.javaTypeName;
/*    */   }
/*    */ 
/*    */   public void setJavaTypeName(String type)
/*    */   {
/* 70 */     this.javaTypeName = type;
/*    */   }
/*    */ 
/*    */   public QName getSchemaTypeName()
/*    */   {
/* 77 */     return this.schemaTypeName;
/*    */   }
/*    */ 
/*    */   public void setSchemaTypeName(QName type)
/*    */   {
/* 83 */     this.schemaTypeName = type;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.RpcLitMember
 * JD-Core Version:    0.6.2
 */