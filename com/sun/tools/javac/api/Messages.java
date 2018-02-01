package com.sun.tools.javac.api;

import java.util.Locale;
import java.util.MissingResourceException;

public abstract interface Messages
{
  public abstract void add(String paramString)
    throws MissingResourceException;

  public abstract String getLocalizedString(Locale paramLocale, String paramString, Object[] paramArrayOfObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.Messages
 * JD-Core Version:    0.6.2
 */