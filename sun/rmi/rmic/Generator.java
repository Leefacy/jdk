package sun.rmi.rmic;

import java.io.File;
import sun.tools.java.ClassDefinition;

public abstract interface Generator
{
  public abstract boolean parseArgs(String[] paramArrayOfString, Main paramMain);

  public abstract void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.Generator
 * JD-Core Version:    0.6.2
 */