package com.sun.tools.internal.ws.wsdl.parser;

import org.w3c.dom.Element;
import org.xml.sax.helpers.XMLFilterImpl;

public abstract interface InternalizationLogic
{
  public abstract XMLFilterImpl createExternalReferenceFinder(DOMForest paramDOMForest);

  public abstract boolean checkIfValidTargetNode(DOMForest paramDOMForest, Element paramElement1, Element paramElement2);

  public abstract Element refineSchemaTarget(Element paramElement);

  public abstract Element refineWSDLTarget(Element paramElement);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.parser.InternalizationLogic
 * JD-Core Version:    0.6.2
 */