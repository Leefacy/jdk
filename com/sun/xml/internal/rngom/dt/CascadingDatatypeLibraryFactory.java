/*    */ package com.sun.xml.internal.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ public class CascadingDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private final DatatypeLibraryFactory factory1;
/*    */   private final DatatypeLibraryFactory factory2;
/*    */ 
/*    */   public CascadingDatatypeLibraryFactory(DatatypeLibraryFactory factory1, DatatypeLibraryFactory factory2)
/*    */   {
/* 62 */     this.factory1 = factory1;
/* 63 */     this.factory2 = factory2;
/*    */   }
/*    */ 
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 67 */     DatatypeLibrary lib = this.factory1.createDatatypeLibrary(namespaceURI);
/* 68 */     if (lib == null)
/* 69 */       lib = this.factory2.createDatatypeLibrary(namespaceURI);
/* 70 */     return lib;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.CascadingDatatypeLibraryFactory
 * JD-Core Version:    0.6.2
 */