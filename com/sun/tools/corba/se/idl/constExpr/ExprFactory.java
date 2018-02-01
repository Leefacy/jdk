package com.sun.tools.corba.se.idl.constExpr;

import com.sun.tools.corba.se.idl.ConstEntry;
import java.math.BigInteger;

public abstract interface ExprFactory
{
  public abstract And and(Expression paramExpression1, Expression paramExpression2);

  public abstract BooleanAnd booleanAnd(Expression paramExpression1, Expression paramExpression2);

  public abstract BooleanNot booleanNot(Expression paramExpression);

  public abstract BooleanOr booleanOr(Expression paramExpression1, Expression paramExpression2);

  public abstract Divide divide(Expression paramExpression1, Expression paramExpression2);

  public abstract Equal equal(Expression paramExpression1, Expression paramExpression2);

  public abstract GreaterEqual greaterEqual(Expression paramExpression1, Expression paramExpression2);

  public abstract GreaterThan greaterThan(Expression paramExpression1, Expression paramExpression2);

  public abstract LessEqual lessEqual(Expression paramExpression1, Expression paramExpression2);

  public abstract LessThan lessThan(Expression paramExpression1, Expression paramExpression2);

  public abstract Minus minus(Expression paramExpression1, Expression paramExpression2);

  public abstract Modulo modulo(Expression paramExpression1, Expression paramExpression2);

  public abstract Negative negative(Expression paramExpression);

  public abstract Not not(Expression paramExpression);

  public abstract NotEqual notEqual(Expression paramExpression1, Expression paramExpression2);

  public abstract Or or(Expression paramExpression1, Expression paramExpression2);

  public abstract Plus plus(Expression paramExpression1, Expression paramExpression2);

  public abstract Positive positive(Expression paramExpression);

  public abstract ShiftLeft shiftLeft(Expression paramExpression1, Expression paramExpression2);

  public abstract ShiftRight shiftRight(Expression paramExpression1, Expression paramExpression2);

  public abstract Terminal terminal(String paramString, Character paramCharacter, boolean paramBoolean);

  public abstract Terminal terminal(String paramString, Boolean paramBoolean);

  public abstract Terminal terminal(String paramString, Double paramDouble);

  public abstract Terminal terminal(String paramString, BigInteger paramBigInteger);

  public abstract Terminal terminal(String paramString, boolean paramBoolean);

  public abstract Terminal terminal(ConstEntry paramConstEntry);

  public abstract Times times(Expression paramExpression1, Expression paramExpression2);

  public abstract Xor xor(Expression paramExpression1, Expression paramExpression2);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.constExpr.ExprFactory
 * JD-Core Version:    0.6.2
 */