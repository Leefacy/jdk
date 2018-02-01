package com.sun.xml.internal.rngom.binary.visitor;

import com.sun.xml.internal.rngom.binary.Pattern;
import com.sun.xml.internal.rngom.nc.NameClass;
import org.relaxng.datatype.Datatype;

public abstract interface PatternVisitor
{
  public abstract void visitEmpty();

  public abstract void visitNotAllowed();

  public abstract void visitError();

  public abstract void visitAfter(Pattern paramPattern1, Pattern paramPattern2);

  public abstract void visitGroup(Pattern paramPattern1, Pattern paramPattern2);

  public abstract void visitInterleave(Pattern paramPattern1, Pattern paramPattern2);

  public abstract void visitChoice(Pattern paramPattern1, Pattern paramPattern2);

  public abstract void visitOneOrMore(Pattern paramPattern);

  public abstract void visitElement(NameClass paramNameClass, Pattern paramPattern);

  public abstract void visitAttribute(NameClass paramNameClass, Pattern paramPattern);

  public abstract void visitData(Datatype paramDatatype);

  public abstract void visitDataExcept(Datatype paramDatatype, Pattern paramPattern);

  public abstract void visitValue(Datatype paramDatatype, Object paramObject);

  public abstract void visitText();

  public abstract void visitList(Pattern paramPattern);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.visitor.PatternVisitor
 * JD-Core Version:    0.6.2
 */