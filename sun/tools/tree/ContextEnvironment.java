/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ final class ContextEnvironment extends Environment
/*     */ {
/*     */   Context ctx;
/*     */   Environment innerEnv;
/*     */ 
/*     */   ContextEnvironment(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 853 */     super(paramEnvironment, paramEnvironment.getSource());
/* 854 */     this.ctx = paramContext;
/* 855 */     this.innerEnv = paramEnvironment;
/*     */   }
/*     */ 
/*     */   public Identifier resolveName(Identifier paramIdentifier) {
/* 859 */     return this.ctx.resolveName(this.innerEnv, paramIdentifier);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ContextEnvironment
 * JD-Core Version:    0.6.2
 */