package com.sun.jdi.connect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface Connector
{
  public abstract String name();

  public abstract String description();

  public abstract Transport transport();

  public abstract Map<String, Argument> defaultArguments();

  @Exported
  public static abstract interface Argument extends Serializable
  {
    public abstract String name();

    public abstract String label();

    public abstract String description();

    public abstract String value();

    public abstract void setValue(String paramString);

    public abstract boolean isValid(String paramString);

    public abstract boolean mustSpecify();
  }

  @Exported
  public static abstract interface BooleanArgument extends Connector.Argument
  {
    public abstract void setValue(boolean paramBoolean);

    public abstract boolean isValid(String paramString);

    public abstract String stringValueOf(boolean paramBoolean);

    public abstract boolean booleanValue();
  }

  @Exported
  public static abstract interface IntegerArgument extends Connector.Argument
  {
    public abstract void setValue(int paramInt);

    public abstract boolean isValid(String paramString);

    public abstract boolean isValid(int paramInt);

    public abstract String stringValueOf(int paramInt);

    public abstract int intValue();

    public abstract int max();

    public abstract int min();
  }

  @Exported
  public static abstract interface SelectedArgument extends Connector.Argument
  {
    public abstract List<String> choices();

    public abstract boolean isValid(String paramString);
  }

  @Exported
  public static abstract interface StringArgument extends Connector.Argument
  {
    public abstract boolean isValid(String paramString);
  }
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.Connector
 * JD-Core Version:    0.6.2
 */