package com.sun.tools.internal.ws.processor.model;

public abstract interface ModelVisitor
{
  public abstract void visit(Model paramModel)
    throws Exception;

  public abstract void visit(Service paramService)
    throws Exception;

  public abstract void visit(Port paramPort)
    throws Exception;

  public abstract void visit(Operation paramOperation)
    throws Exception;

  public abstract void visit(Request paramRequest)
    throws Exception;

  public abstract void visit(Response paramResponse)
    throws Exception;

  public abstract void visit(Fault paramFault)
    throws Exception;

  public abstract void visit(Block paramBlock)
    throws Exception;

  public abstract void visit(Parameter paramParameter)
    throws Exception;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.ModelVisitor
 * JD-Core Version:    0.6.2
 */