package com.sun.xml.internal.rngom.digested;

public abstract interface DPatternVisitor<V>
{
  public abstract V onAttribute(DAttributePattern paramDAttributePattern);

  public abstract V onChoice(DChoicePattern paramDChoicePattern);

  public abstract V onData(DDataPattern paramDDataPattern);

  public abstract V onElement(DElementPattern paramDElementPattern);

  public abstract V onEmpty(DEmptyPattern paramDEmptyPattern);

  public abstract V onGrammar(DGrammarPattern paramDGrammarPattern);

  public abstract V onGroup(DGroupPattern paramDGroupPattern);

  public abstract V onInterleave(DInterleavePattern paramDInterleavePattern);

  public abstract V onList(DListPattern paramDListPattern);

  public abstract V onMixed(DMixedPattern paramDMixedPattern);

  public abstract V onNotAllowed(DNotAllowedPattern paramDNotAllowedPattern);

  public abstract V onOneOrMore(DOneOrMorePattern paramDOneOrMorePattern);

  public abstract V onOptional(DOptionalPattern paramDOptionalPattern);

  public abstract V onRef(DRefPattern paramDRefPattern);

  public abstract V onText(DTextPattern paramDTextPattern);

  public abstract V onValue(DValuePattern paramDValuePattern);

  public abstract V onZeroOrMore(DZeroOrMorePattern paramDZeroOrMorePattern);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DPatternVisitor
 * JD-Core Version:    0.6.2
 */