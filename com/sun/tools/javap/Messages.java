package com.sun.tools.javap;

import java.util.Locale;

public abstract interface Messages
{
  public abstract String getMessage(String paramString, Object[] paramArrayOfObject);

  public abstract String getMessage(Locale paramLocale, String paramString, Object[] paramArrayOfObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.Messages
 * JD-Core Version:    0.6.2
 */