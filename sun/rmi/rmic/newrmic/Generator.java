package sun.rmi.rmic.newrmic;

import com.sun.javadoc.ClassDoc;
import java.io.File;
import java.util.Set;

public abstract interface Generator
{
  public abstract boolean parseArgs(String[] paramArrayOfString, Main paramMain);

  public abstract Class<? extends BatchEnvironment> envClass();

  public abstract Set<String> bootstrapClassNames();

  public abstract void generate(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc, File paramFile);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.Generator
 * JD-Core Version:    0.6.2
 */