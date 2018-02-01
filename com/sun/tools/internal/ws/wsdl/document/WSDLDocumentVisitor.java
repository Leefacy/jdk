package com.sun.tools.internal.ws.wsdl.document;

import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;

public abstract interface WSDLDocumentVisitor extends ExtensionVisitor
{
  public abstract void preVisit(Definitions paramDefinitions)
    throws Exception;

  public abstract void postVisit(Definitions paramDefinitions)
    throws Exception;

  public abstract void visit(Import paramImport)
    throws Exception;

  public abstract void preVisit(Types paramTypes)
    throws Exception;

  public abstract void postVisit(Types paramTypes)
    throws Exception;

  public abstract void preVisit(Message paramMessage)
    throws Exception;

  public abstract void postVisit(Message paramMessage)
    throws Exception;

  public abstract void visit(MessagePart paramMessagePart)
    throws Exception;

  public abstract void preVisit(PortType paramPortType)
    throws Exception;

  public abstract void postVisit(PortType paramPortType)
    throws Exception;

  public abstract void preVisit(Operation paramOperation)
    throws Exception;

  public abstract void postVisit(Operation paramOperation)
    throws Exception;

  public abstract void preVisit(Input paramInput)
    throws Exception;

  public abstract void postVisit(Input paramInput)
    throws Exception;

  public abstract void preVisit(Output paramOutput)
    throws Exception;

  public abstract void postVisit(Output paramOutput)
    throws Exception;

  public abstract void preVisit(Fault paramFault)
    throws Exception;

  public abstract void postVisit(Fault paramFault)
    throws Exception;

  public abstract void preVisit(Binding paramBinding)
    throws Exception;

  public abstract void postVisit(Binding paramBinding)
    throws Exception;

  public abstract void preVisit(BindingOperation paramBindingOperation)
    throws Exception;

  public abstract void postVisit(BindingOperation paramBindingOperation)
    throws Exception;

  public abstract void preVisit(BindingInput paramBindingInput)
    throws Exception;

  public abstract void postVisit(BindingInput paramBindingInput)
    throws Exception;

  public abstract void preVisit(BindingOutput paramBindingOutput)
    throws Exception;

  public abstract void postVisit(BindingOutput paramBindingOutput)
    throws Exception;

  public abstract void preVisit(BindingFault paramBindingFault)
    throws Exception;

  public abstract void postVisit(BindingFault paramBindingFault)
    throws Exception;

  public abstract void preVisit(Service paramService)
    throws Exception;

  public abstract void postVisit(Service paramService)
    throws Exception;

  public abstract void preVisit(Port paramPort)
    throws Exception;

  public abstract void postVisit(Port paramPort)
    throws Exception;

  public abstract void visit(Documentation paramDocumentation)
    throws Exception;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.WSDLDocumentVisitor
 * JD-Core Version:    0.6.2
 */