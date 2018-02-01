/*    */ package com.sun.xml.internal.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ class CompatibilityDatatypeLibrary
/*    */   implements DatatypeLibrary
/*    */ {
/*    */   private final DatatypeLibraryFactory factory;
/* 57 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */ 
/*    */   CompatibilityDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 60 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException
/*    */   {
/* 65 */     if ((type.equals("ID")) || 
/* 66 */       (type
/* 66 */       .equals("IDREF")) || 
/* 67 */       (type
/* 67 */       .equals("IDREFS")))
/*    */     {
/* 68 */       if (this.xsdDatatypeLibrary == null) {
/* 69 */         this.xsdDatatypeLibrary = this.factory
/* 70 */           .createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */ 
/* 72 */         if (this.xsdDatatypeLibrary == null)
/* 73 */           throw new DatatypeException();
/*    */       }
/* 75 */       return this.xsdDatatypeLibrary.createDatatypeBuilder(type);
/*    */     }
/* 77 */     throw new DatatypeException();
/*    */   }
/*    */ 
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 81 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.builtin.CompatibilityDatatypeLibrary
 * JD-Core Version:    0.6.2
 */