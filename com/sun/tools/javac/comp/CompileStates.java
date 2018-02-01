/*    */ package com.sun.tools.javac.comp;
/*    */ 
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Key;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class CompileStates extends HashMap<Env<AttrContext>, CompileState>
/*    */ {
/* 42 */   protected static final Context.Key<CompileStates> compileStatesKey = new Context.Key();
/*    */   private static final long serialVersionUID = 1812267524140424433L;
/*    */   protected Context context;
/*    */ 
/*    */   public static CompileStates instance(Context paramContext)
/*    */   {
/* 47 */     CompileStates localCompileStates = (CompileStates)paramContext.get(compileStatesKey);
/* 48 */     if (localCompileStates == null) {
/* 49 */       localCompileStates = new CompileStates(paramContext);
/*    */     }
/* 51 */     return localCompileStates;
/*    */   }
/*    */ 
/*    */   public CompileStates(Context paramContext)
/*    */   {
/* 84 */     this.context = paramContext;
/* 85 */     paramContext.put(compileStatesKey, this);
/*    */   }
/*    */ 
/*    */   public boolean isDone(Env<AttrContext> paramEnv, CompileState paramCompileState) {
/* 89 */     CompileState localCompileState = (CompileState)get(paramEnv);
/* 90 */     return (localCompileState != null) && (!paramCompileState.isAfter(localCompileState));
/*    */   }
/*    */ 
/*    */   public static enum CompileState
/*    */   {
/* 56 */     INIT(0), 
/* 57 */     PARSE(1), 
/* 58 */     ENTER(2), 
/* 59 */     PROCESS(3), 
/* 60 */     ATTR(4), 
/* 61 */     FLOW(5), 
/* 62 */     TRANSTYPES(6), 
/* 63 */     UNLAMBDA(7), 
/* 64 */     LOWER(8), 
/* 65 */     GENERATE(9);
/*    */ 
/*    */     private final int value;
/*    */ 
/* 68 */     private CompileState(int paramInt) { this.value = paramInt; }
/*    */ 
/*    */     public boolean isAfter(CompileState paramCompileState) {
/* 71 */       return this.value > paramCompileState.value;
/*    */     }
/*    */     public static CompileState max(CompileState paramCompileState1, CompileState paramCompileState2) {
/* 74 */       return paramCompileState1.value > paramCompileState2.value ? paramCompileState1 : paramCompileState2;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.CompileStates
 * JD-Core Version:    0.6.2
 */