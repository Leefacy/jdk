/*    */ package com.sun.xml.internal.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ public class BuiltinDatatypeLibrary
/*    */   implements DatatypeLibrary
/*    */ {
/*    */   private final DatatypeLibraryFactory factory;
/* 58 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */ 
/*    */   BuiltinDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 61 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException
/*    */   {
/* 66 */     this.xsdDatatypeLibrary = this.factory
/* 67 */       .createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */ 
/* 69 */     if (this.xsdDatatypeLibrary == null) {
/* 70 */       throw new DatatypeException();
/*    */     }
/* 72 */     if ((type.equals("string")) || (type.equals("token")))
/*    */     {
/* 74 */       return new BuiltinDatatypeBuilder(this.xsdDatatypeLibrary
/* 74 */         .createDatatype(type));
/*    */     }
/*    */ 
/* 76 */     throw new DatatypeException();
/*    */   }
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 79 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.builtin.BuiltinDatatypeLibrary
 * JD-Core Version:    0.6.2
 */