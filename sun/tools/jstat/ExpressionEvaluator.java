package sun.tools.jstat;

import sun.jvmstat.monitor.MonitorException;

abstract interface ExpressionEvaluator
{
  public abstract Object evaluate(Expression paramExpression)
    throws MonitorException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.ExpressionEvaluator
 * JD-Core Version:    0.6.2
 */