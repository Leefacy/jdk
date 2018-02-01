package sun.tools.java;

import sun.tools.tree.Node;

public abstract interface ParserActions
{
  public abstract void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken);

  public abstract void importClass(long paramLong, IdentifierToken paramIdentifierToken);

  public abstract void importPackage(long paramLong, IdentifierToken paramIdentifierToken);

  public abstract ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken);

  public abstract void endClass(long paramLong, ClassDefinition paramClassDefinition);

  public abstract void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ParserActions
 * JD-Core Version:    0.6.2
 */