/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*    */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ 
/*    */ final class CollectionTypeAttribute
/*    */ {
/*    */ 
/*    */   @XmlValue
/* 41 */   String collectionType = null;
/*    */ 
/*    */   @XmlTransient
/*    */   private FieldRenderer fr;
/*    */ 
/*    */   FieldRenderer get(Model m)
/*    */   {
/* 51 */     if (this.fr == null)
/* 52 */       this.fr = calcFr(m);
/* 53 */     return this.fr;
/*    */   }
/*    */ 
/*    */   private FieldRenderer calcFr(Model m) {
/* 57 */     FieldRendererFactory frf = m.options.getFieldRendererFactory();
/* 58 */     if (this.collectionType == null) {
/* 59 */       return frf.getDefault();
/*    */     }
/* 61 */     if (this.collectionType.equals("indexed")) {
/* 62 */       return frf.getArray();
/*    */     }
/* 64 */     return frf.getList(m.codeModel.ref(this.collectionType));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.CollectionTypeAttribute
 * JD-Core Version:    0.6.2
 */