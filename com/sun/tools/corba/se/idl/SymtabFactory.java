package com.sun.tools.corba.se.idl;

public abstract interface SymtabFactory
{
  public abstract AttributeEntry attributeEntry();

  public abstract AttributeEntry attributeEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID);

  public abstract ConstEntry constEntry();

  public abstract ConstEntry constEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract NativeEntry nativeEntry();

  public abstract NativeEntry nativeEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract EnumEntry enumEntry();

  public abstract EnumEntry enumEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract ExceptionEntry exceptionEntry();

  public abstract ExceptionEntry exceptionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract ForwardEntry forwardEntry();

  public abstract ForwardEntry forwardEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract ForwardValueEntry forwardValueEntry();

  public abstract ForwardValueEntry forwardValueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract IncludeEntry includeEntry();

  public abstract IncludeEntry includeEntry(SymtabEntry paramSymtabEntry);

  public abstract InterfaceEntry interfaceEntry();

  public abstract InterfaceEntry interfaceEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract ValueEntry valueEntry();

  public abstract ValueEntry valueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract ValueBoxEntry valueBoxEntry();

  public abstract ValueBoxEntry valueBoxEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract MethodEntry methodEntry();

  public abstract MethodEntry methodEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID);

  public abstract ModuleEntry moduleEntry();

  public abstract ModuleEntry moduleEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);

  public abstract ParameterEntry parameterEntry();

  public abstract ParameterEntry parameterEntry(MethodEntry paramMethodEntry, IDLID paramIDLID);

  public abstract PragmaEntry pragmaEntry();

  public abstract PragmaEntry pragmaEntry(SymtabEntry paramSymtabEntry);

  public abstract PrimitiveEntry primitiveEntry();

  public abstract PrimitiveEntry primitiveEntry(String paramString);

  public abstract SequenceEntry sequenceEntry();

  public abstract SequenceEntry sequenceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract StringEntry stringEntry();

  public abstract StructEntry structEntry();

  public abstract StructEntry structEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract TypedefEntry typedefEntry();

  public abstract TypedefEntry typedefEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);

  public abstract UnionEntry unionEntry();

  public abstract UnionEntry unionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.SymtabFactory
 * JD-Core Version:    0.6.2
 */