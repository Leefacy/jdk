package com.sun.tools.corba.se.idl;

public abstract interface GenFactory
{
  public abstract AttributeGen createAttributeGen();

  public abstract ConstGen createConstGen();

  public abstract EnumGen createEnumGen();

  public abstract ExceptionGen createExceptionGen();

  public abstract ForwardGen createForwardGen();

  public abstract ForwardValueGen createForwardValueGen();

  public abstract IncludeGen createIncludeGen();

  public abstract InterfaceGen createInterfaceGen();

  public abstract ValueGen createValueGen();

  public abstract ValueBoxGen createValueBoxGen();

  public abstract MethodGen createMethodGen();

  public abstract ModuleGen createModuleGen();

  public abstract NativeGen createNativeGen();

  public abstract ParameterGen createParameterGen();

  public abstract PragmaGen createPragmaGen();

  public abstract PrimitiveGen createPrimitiveGen();

  public abstract SequenceGen createSequenceGen();

  public abstract StringGen createStringGen();

  public abstract StructGen createStructGen();

  public abstract TypedefGen createTypedefGen();

  public abstract UnionGen createUnionGen();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.GenFactory
 * JD-Core Version:    0.6.2
 */