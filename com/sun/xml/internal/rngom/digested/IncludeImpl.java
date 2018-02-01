/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection.Combine;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*     */ import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
/*     */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class IncludeImpl extends GrammarBuilderImpl
/*     */   implements Include
/*     */ {
/*  66 */   private Set overridenPatterns = new HashSet();
/*  67 */   private boolean startOverriden = false;
/*     */ 
/*     */   public IncludeImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/*  70 */     super(p, parent, sb);
/*     */   }
/*     */ 
/*     */   public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException
/*     */   {
/*  75 */     super.define(name, combine, pattern, loc, anno);
/*  76 */     if (name == "")
/*  77 */       this.startOverriden = true;
/*     */     else
/*  79 */       this.overridenPatterns.add(name);
/*     */   }
/*     */ 
/*     */   public void endInclude(Parseable current, String uri, String ns, Location loc, Annotations anno) throws BuildException, IllegalSchemaException {
/*  83 */     current.parseInclude(uri, this.sb, new IncludedGrammarImpl(this.grammar, this.parent, this.sb), ns);
/*     */   }
/*     */ 
/*     */   private class IncludedGrammarImpl extends GrammarBuilderImpl implements IncludedGrammar {
/*     */     public IncludedGrammarImpl(DGrammarPattern p, Scope parent, DSchemaBuilderImpl sb) {
/*  88 */       super(parent, sb);
/*     */     }
/*     */ 
/*     */     public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno)
/*     */       throws BuildException
/*     */     {
/*  94 */       if (name == "")
/*     */       {
/*  95 */         if (!IncludeImpl.this.startOverriden);
/*     */       }
/*  98 */       else if (IncludeImpl.this.overridenPatterns.contains(name)) {
/*  99 */         return;
/*     */       }
/*     */ 
/* 102 */       super.define(name, combine, pattern, loc, anno);
/*     */     }
/*     */ 
/*     */     public ParsedPattern endIncludedGrammar(Location loc, Annotations anno) throws BuildException {
/* 106 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.IncludeImpl
 * JD-Core Version:    0.6.2
 */