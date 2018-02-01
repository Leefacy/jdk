package com.sun.tools.hat.internal.model;

public abstract interface JavaHeapObjectVisitor
{
  public abstract void visit(JavaHeapObject paramJavaHeapObject);

  public abstract boolean exclude(JavaClass paramJavaClass, JavaField paramJavaField);

  public abstract boolean mightExclude();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaHeapObjectVisitor
 * JD-Core Version:    0.6.2
 */