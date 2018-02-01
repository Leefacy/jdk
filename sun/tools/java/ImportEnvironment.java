/*     */ package sun.tools.java;
/*     */ 
/*     */ final class ImportEnvironment extends Environment
/*     */ {
/*     */   Imports imports;
/*     */ 
/*     */   ImportEnvironment(Environment paramEnvironment, Imports paramImports)
/*     */   {
/* 492 */     super(paramEnvironment, paramEnvironment.getSource());
/* 493 */     this.imports = paramImports;
/*     */   }
/*     */ 
/*     */   public Identifier resolve(Identifier paramIdentifier) throws ClassNotFound {
/* 497 */     return this.imports.resolve(this, paramIdentifier);
/*     */   }
/*     */ 
/*     */   public Imports getImports() {
/* 501 */     return this.imports;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ImportEnvironment
 * JD-Core Version:    0.6.2
 */