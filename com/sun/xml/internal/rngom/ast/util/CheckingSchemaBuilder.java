/*     */ package com.sun.xml.internal.rngom.ast.util;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.binary.SchemaBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.binary.SchemaPatternBuilder;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.host.ParsedPatternHost;
/*     */ import com.sun.xml.internal.rngom.parse.host.SchemaBuilderHost;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ public class CheckingSchemaBuilder extends SchemaBuilderHost
/*     */ {
/*     */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh)
/*     */   {
/*  90 */     super(new SchemaBuilderImpl(eh), sb);
/*     */   }
/*     */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh, DatatypeLibraryFactory dlf) {
/*  93 */     super(new SchemaBuilderImpl(eh, dlf, new SchemaPatternBuilder()), sb);
/*     */   }
/*     */ 
/*     */   public ParsedPattern expandPattern(ParsedPattern p)
/*     */     throws BuildException, IllegalSchemaException
/*     */   {
/* 100 */     ParsedPatternHost r = (ParsedPatternHost)super.expandPattern(p);
/* 101 */     return r.rhs;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.util.CheckingSchemaBuilder
 * JD-Core Version:    0.6.2
 */