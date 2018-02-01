package com.sun.tools.internal.ws.processor.model.jaxb;

public abstract interface JAXBTypeVisitor
{
  public abstract void visit(JAXBType paramJAXBType)
    throws Exception;

  public abstract void visit(RpcLitStructure paramRpcLitStructure)
    throws Exception;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeVisitor
 * JD-Core Version:    0.6.2
 */