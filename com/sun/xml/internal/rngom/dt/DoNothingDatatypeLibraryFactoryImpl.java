/*     */ package com.sun.xml.internal.rngom.dt;
/*     */ 
/*     */ import org.relaxng.datatype.Datatype;
/*     */ import org.relaxng.datatype.DatatypeBuilder;
/*     */ import org.relaxng.datatype.DatatypeException;
/*     */ import org.relaxng.datatype.DatatypeLibrary;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*     */ import org.relaxng.datatype.DatatypeStreamingValidator;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ import org.relaxng.datatype.helpers.StreamingValidatorImpl;
/*     */ 
/*     */ public final class DoNothingDatatypeLibraryFactoryImpl
/*     */   implements DatatypeLibraryFactory
/*     */ {
/*     */   public DatatypeLibrary createDatatypeLibrary(String s)
/*     */   {
/*  65 */     return new DatatypeLibrary()
/*     */     {
/*     */       public Datatype createDatatype(String s) throws DatatypeException {
/*  68 */         return createDatatypeBuilder(s).createDatatype();
/*     */       }
/*     */ 
/*     */       public DatatypeBuilder createDatatypeBuilder(String s) throws DatatypeException {
/*  72 */         return new DatatypeBuilder() {
/*     */           public void addParameter(String s, String s1, ValidationContext validationContext) throws DatatypeException {
/*     */           }
/*     */ 
/*     */           public Datatype createDatatype() throws DatatypeException {
/*  77 */             return new Datatype()
/*     */             {
/*     */               public boolean isValid(String s, ValidationContext validationContext) {
/*  80 */                 return false;
/*     */               }
/*     */ 
/*     */               public void checkValid(String s, ValidationContext validationContext) throws DatatypeException {
/*     */               }
/*     */ 
/*     */               public DatatypeStreamingValidator createStreamingValidator(ValidationContext validationContext) {
/*  87 */                 return new StreamingValidatorImpl(this, validationContext);
/*     */               }
/*     */ 
/*     */               public Object createValue(String s, ValidationContext validationContext) {
/*  91 */                 return null;
/*     */               }
/*     */ 
/*     */               public boolean sameValue(Object o, Object o1) {
/*  95 */                 return false;
/*     */               }
/*     */ 
/*     */               public int valueHashCode(Object o) {
/*  99 */                 return 0;
/*     */               }
/*     */ 
/*     */               public int getIdType() {
/* 103 */                 return 0;
/*     */               }
/*     */ 
/*     */               public boolean isContextDependent() {
/* 107 */                 return false;
/*     */               }
/*     */             };
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.DoNothingDatatypeLibraryFactoryImpl
 * JD-Core Version:    0.6.2
 */