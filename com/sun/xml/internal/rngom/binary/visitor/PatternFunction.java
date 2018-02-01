package com.sun.xml.internal.rngom.binary.visitor;

import com.sun.xml.internal.rngom.binary.AfterPattern;
import com.sun.xml.internal.rngom.binary.AttributePattern;
import com.sun.xml.internal.rngom.binary.ChoicePattern;
import com.sun.xml.internal.rngom.binary.DataExceptPattern;
import com.sun.xml.internal.rngom.binary.DataPattern;
import com.sun.xml.internal.rngom.binary.ElementPattern;
import com.sun.xml.internal.rngom.binary.EmptyPattern;
import com.sun.xml.internal.rngom.binary.ErrorPattern;
import com.sun.xml.internal.rngom.binary.GroupPattern;
import com.sun.xml.internal.rngom.binary.InterleavePattern;
import com.sun.xml.internal.rngom.binary.ListPattern;
import com.sun.xml.internal.rngom.binary.NotAllowedPattern;
import com.sun.xml.internal.rngom.binary.OneOrMorePattern;
import com.sun.xml.internal.rngom.binary.RefPattern;
import com.sun.xml.internal.rngom.binary.TextPattern;
import com.sun.xml.internal.rngom.binary.ValuePattern;

public abstract interface PatternFunction
{
  public abstract Object caseEmpty(EmptyPattern paramEmptyPattern);

  public abstract Object caseNotAllowed(NotAllowedPattern paramNotAllowedPattern);

  public abstract Object caseError(ErrorPattern paramErrorPattern);

  public abstract Object caseGroup(GroupPattern paramGroupPattern);

  public abstract Object caseInterleave(InterleavePattern paramInterleavePattern);

  public abstract Object caseChoice(ChoicePattern paramChoicePattern);

  public abstract Object caseOneOrMore(OneOrMorePattern paramOneOrMorePattern);

  public abstract Object caseElement(ElementPattern paramElementPattern);

  public abstract Object caseAttribute(AttributePattern paramAttributePattern);

  public abstract Object caseData(DataPattern paramDataPattern);

  public abstract Object caseDataExcept(DataExceptPattern paramDataExceptPattern);

  public abstract Object caseValue(ValuePattern paramValuePattern);

  public abstract Object caseText(TextPattern paramTextPattern);

  public abstract Object caseList(ListPattern paramListPattern);

  public abstract Object caseRef(RefPattern paramRefPattern);

  public abstract Object caseAfter(AfterPattern paramAfterPattern);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.visitor.PatternFunction
 * JD-Core Version:    0.6.2
 */