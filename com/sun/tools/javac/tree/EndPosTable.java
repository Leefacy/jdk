package com.sun.tools.javac.tree;

public abstract interface EndPosTable
{
  public abstract int getEndPos(JCTree paramJCTree);

  public abstract void storeEnd(JCTree paramJCTree, int paramInt);

  public abstract int replaceTree(JCTree paramJCTree1, JCTree paramJCTree2);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.EndPosTable
 * JD-Core Version:    0.6.2
 */