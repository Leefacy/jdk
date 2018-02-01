/*    */ package com.sun.xml.internal.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ public class CachedDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private String lastUri;
/*    */   private DatatypeLibrary lastLib;
/*    */   private final DatatypeLibraryFactory core;
/*    */ 
/*    */   public CachedDatatypeLibraryFactory(DatatypeLibraryFactory core)
/*    */   {
/* 64 */     this.core = core;
/*    */   }
/*    */ 
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 68 */     if (this.lastUri == namespaceURI) {
/* 69 */       return this.lastLib;
/*    */     }
/* 71 */     this.lastUri = namespaceURI;
/* 72 */     this.lastLib = this.core.createDatatypeLibrary(namespaceURI);
/* 73 */     return this.lastLib;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.CachedDatatypeLibraryFactory
 * JD-Core Version:    0.6.2
 */