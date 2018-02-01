/*    */ package com.sun.xml.internal.rngom.dt.builtin;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.util.Localizer;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ 
/*    */ class BuiltinDatatypeBuilder
/*    */   implements DatatypeBuilder
/*    */ {
/*    */   private final Datatype dt;
/* 58 */   private static final Localizer localizer = new Localizer(BuiltinDatatypeBuilder.class);
/*    */ 
/*    */   BuiltinDatatypeBuilder(Datatype dt) {
/* 61 */     this.dt = dt;
/*    */   }
/*    */ 
/*    */   public void addParameter(String name, String value, ValidationContext context)
/*    */     throws DatatypeException
/*    */   {
/* 67 */     throw new DatatypeException(localizer.message("builtin_param"));
/*    */   }
/*    */ 
/*    */   public Datatype createDatatype() {
/* 71 */     return this.dt;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.dt.builtin.BuiltinDatatypeBuilder
 * JD-Core Version:    0.6.2
 */