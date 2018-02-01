package com.sun.tools.internal.xjc.reader.dtd.bindinfo;

import com.sun.tools.internal.xjc.model.TypeUse;

public abstract interface BIConversion
{
  public abstract String name();

  public abstract TypeUse getTransducer();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIConversion
 * JD-Core Version:    0.6.2
 */