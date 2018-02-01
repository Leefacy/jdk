package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface ReferenceType extends Type, Comparable<ReferenceType>, Accessible
{
  public abstract String name();

  public abstract String genericSignature();

  public abstract ClassLoaderReference classLoader();

  public abstract String sourceName()
    throws AbsentInformationException;

  public abstract List<String> sourceNames(String paramString)
    throws AbsentInformationException;

  public abstract List<String> sourcePaths(String paramString)
    throws AbsentInformationException;

  public abstract String sourceDebugExtension()
    throws AbsentInformationException;

  public abstract boolean isStatic();

  public abstract boolean isAbstract();

  public abstract boolean isFinal();

  public abstract boolean isPrepared();

  public abstract boolean isVerified();

  public abstract boolean isInitialized();

  public abstract boolean failedToInitialize();

  public abstract List<Field> fields();

  public abstract List<Field> visibleFields();

  public abstract List<Field> allFields();

  public abstract Field fieldByName(String paramString);

  public abstract List<Method> methods();

  public abstract List<Method> visibleMethods();

  public abstract List<Method> allMethods();

  public abstract List<Method> methodsByName(String paramString);

  public abstract List<Method> methodsByName(String paramString1, String paramString2);

  public abstract List<ReferenceType> nestedTypes();

  public abstract Value getValue(Field paramField);

  public abstract Map<Field, Value> getValues(List<? extends Field> paramList);

  public abstract ClassObjectReference classObject();

  public abstract List<Location> allLineLocations()
    throws AbsentInformationException;

  public abstract List<Location> allLineLocations(String paramString1, String paramString2)
    throws AbsentInformationException;

  public abstract List<Location> locationsOfLine(int paramInt)
    throws AbsentInformationException;

  public abstract List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt)
    throws AbsentInformationException;

  public abstract List<String> availableStrata();

  public abstract String defaultStratum();

  public abstract List<ObjectReference> instances(long paramLong);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract int majorVersion();

  public abstract int minorVersion();

  public abstract int constantPoolCount();

  public abstract byte[] constantPool();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ReferenceType
 * JD-Core Version:    0.6.2
 */