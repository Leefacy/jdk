package com.sun.codemodel.internal;

public abstract interface JAssignmentTarget extends JGenerable, JExpression
{
  public abstract JExpression assign(JExpression paramJExpression);

  public abstract JExpression assignPlus(JExpression paramJExpression);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAssignmentTarget
 * JD-Core Version:    0.6.2
 */