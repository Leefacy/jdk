/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ 
/*     */ public class ExpressionResolver
/*     */   implements ExpressionEvaluator
/*     */ {
/*  40 */   private static boolean debug = Boolean.getBoolean("ExpressionResolver.debug");
/*     */   private MonitoredVm vm;
/*     */ 
/*     */   ExpressionResolver(MonitoredVm paramMonitoredVm)
/*     */   {
/*  44 */     this.vm = paramMonitoredVm;
/*     */   }
/*     */ 
/*     */   public Object evaluate(Expression paramExpression)
/*     */     throws MonitorException
/*     */   {
/*  53 */     if (paramExpression == null) {
/*  54 */       return null;
/*     */     }
/*     */ 
/*  57 */     if (debug) {
/*  58 */       System.out.println("Resolving Expression:" + paramExpression);
/*     */     }
/*     */ 
/*  61 */     if ((paramExpression instanceof Identifier)) {
/*  62 */       localObject1 = (Identifier)paramExpression;
/*     */ 
/*  65 */       if (((Identifier)localObject1).isResolved()) {
/*  66 */         return localObject1;
/*     */       }
/*     */ 
/*  70 */       localObject2 = this.vm.findByName(((Identifier)localObject1).getName());
/*  71 */       if (localObject2 == null) {
/*  72 */         System.err.println("Warning: Unresolved Symbol: " + ((Identifier)localObject1)
/*  73 */           .getName() + " substituted NaN");
/*  74 */         return new Literal(new Double((0.0D / 0.0D)));
/*     */       }
/*  76 */       if (((Monitor)localObject2).getVariability() == Variability.CONSTANT) {
/*  77 */         if (debug) {
/*  78 */           System.out.println("Converting constant " + ((Identifier)localObject1).getName() + " to literal with value " + ((Monitor)localObject2)
/*  80 */             .getValue());
/*     */         }
/*  82 */         return new Literal(((Monitor)localObject2).getValue());
/*     */       }
/*  84 */       ((Identifier)localObject1).setValue(localObject2);
/*  85 */       return localObject1;
/*     */     }
/*     */ 
/*  88 */     if ((paramExpression instanceof Literal)) {
/*  89 */       return paramExpression;
/*     */     }
/*     */ 
/*  92 */     Object localObject1 = null;
/*  93 */     Object localObject2 = null;
/*     */ 
/*  95 */     if (paramExpression.getLeft() != null) {
/*  96 */       localObject1 = (Expression)evaluate(paramExpression.getLeft());
/*     */     }
/*  98 */     if (paramExpression.getRight() != null) {
/*  99 */       localObject2 = (Expression)evaluate(paramExpression.getRight());
/*     */     }
/*     */ 
/* 102 */     if ((localObject1 != null) && (localObject2 != null) && 
/* 103 */       ((localObject1 instanceof Literal)) && ((localObject2 instanceof Literal))) {
/* 104 */       Literal localLiteral1 = (Literal)localObject1;
/* 105 */       Literal localLiteral2 = (Literal)localObject2;
/* 106 */       int i = 0;
/*     */ 
/* 108 */       Double localDouble = new Double((0.0D / 0.0D));
/* 109 */       if ((localLiteral1.getValue() instanceof String)) {
/* 110 */         i = 1; localLiteral1.setValue(localDouble);
/*     */       }
/* 112 */       if ((localLiteral2.getValue() instanceof String)) {
/* 113 */         i = 1; localLiteral2.setValue(localDouble);
/*     */       }
/* 115 */       if ((debug) && (i != 0)) {
/* 116 */         System.out.println("Warning: String literal in numerical expression: substitutied NaN");
/*     */       }
/*     */ 
/* 122 */       Number localNumber1 = (Number)localLiteral1.getValue();
/* 123 */       Number localNumber2 = (Number)localLiteral2.getValue();
/* 124 */       double d = paramExpression.getOperator().eval(localNumber1.doubleValue(), localNumber2
/* 125 */         .doubleValue());
/* 126 */       if (debug) {
/* 127 */         System.out.println("Converting expression " + paramExpression + " (left = " + localNumber1
/* 128 */           .doubleValue() + ")" + " (right = " + localNumber2
/* 129 */           .doubleValue() + ")" + " to literal value " + d);
/*     */       }
/*     */ 
/* 132 */       return new Literal(new Double(d));
/*     */     }
/*     */ 
/* 136 */     if ((localObject1 != null) && (localObject2 == null)) {
/* 137 */       return localObject1;
/*     */     }
/*     */ 
/* 140 */     paramExpression.setLeft((Expression)localObject1);
/* 141 */     paramExpression.setRight((Expression)localObject2);
/*     */ 
/* 143 */     return paramExpression;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.ExpressionResolver
 * JD-Core Version:    0.6.2
 */