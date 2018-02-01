/*    */ package com.sun.tools.javac.comp;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Key;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ class TypeEnvs
/*    */ {
/*    */   private static final long serialVersionUID = 571524752489954631L;
/* 44 */   protected static final Context.Key<TypeEnvs> typeEnvsKey = new Context.Key();
/*    */   private HashMap<Symbol.TypeSymbol, Env<AttrContext>> map;
/*    */ 
/*    */   public static TypeEnvs instance(Context paramContext)
/*    */   {
/* 46 */     TypeEnvs localTypeEnvs = (TypeEnvs)paramContext.get(typeEnvsKey);
/* 47 */     if (localTypeEnvs == null)
/* 48 */       localTypeEnvs = new TypeEnvs(paramContext);
/* 49 */     return localTypeEnvs;
/*    */   }
/*    */ 
/*    */   protected TypeEnvs(Context paramContext)
/*    */   {
/* 54 */     this.map = new HashMap();
/* 55 */     paramContext.put(typeEnvsKey, this);
/*    */   }
/*    */   Env<AttrContext> get(Symbol.TypeSymbol paramTypeSymbol) {
/* 58 */     return (Env)this.map.get(paramTypeSymbol); } 
/* 59 */   Env<AttrContext> put(Symbol.TypeSymbol paramTypeSymbol, Env<AttrContext> paramEnv) { return (Env)this.map.put(paramTypeSymbol, paramEnv); } 
/* 60 */   Env<AttrContext> remove(Symbol.TypeSymbol paramTypeSymbol) { return (Env)this.map.remove(paramTypeSymbol); } 
/* 61 */   Collection<Env<AttrContext>> values() { return this.map.values(); } 
/* 62 */   void clear() { this.map.clear(); }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.TypeEnvs
 * JD-Core Version:    0.6.2
 */