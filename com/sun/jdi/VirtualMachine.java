package com.sun.jdi;

import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.EventRequestManager;
import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface VirtualMachine extends Mirror
{
  public static final int TRACE_NONE = 0;
  public static final int TRACE_SENDS = 1;
  public static final int TRACE_RECEIVES = 2;
  public static final int TRACE_EVENTS = 4;
  public static final int TRACE_REFTYPES = 8;
  public static final int TRACE_OBJREFS = 16;
  public static final int TRACE_ALL = 16777215;

  public abstract List<ReferenceType> classesByName(String paramString);

  public abstract List<ReferenceType> allClasses();

  public abstract void redefineClasses(Map<? extends ReferenceType, byte[]> paramMap);

  public abstract List<ThreadReference> allThreads();

  public abstract void suspend();

  public abstract void resume();

  public abstract List<ThreadGroupReference> topLevelThreadGroups();

  public abstract EventQueue eventQueue();

  public abstract EventRequestManager eventRequestManager();

  public abstract BooleanValue mirrorOf(boolean paramBoolean);

  public abstract ByteValue mirrorOf(byte paramByte);

  public abstract CharValue mirrorOf(char paramChar);

  public abstract ShortValue mirrorOf(short paramShort);

  public abstract IntegerValue mirrorOf(int paramInt);

  public abstract LongValue mirrorOf(long paramLong);

  public abstract FloatValue mirrorOf(float paramFloat);

  public abstract DoubleValue mirrorOf(double paramDouble);

  public abstract StringReference mirrorOf(String paramString);

  public abstract VoidValue mirrorOfVoid();

  public abstract Process process();

  public abstract void dispose();

  public abstract void exit(int paramInt);

  public abstract boolean canWatchFieldModification();

  public abstract boolean canWatchFieldAccess();

  public abstract boolean canGetBytecodes();

  public abstract boolean canGetSyntheticAttribute();

  public abstract boolean canGetOwnedMonitorInfo();

  public abstract boolean canGetCurrentContendedMonitor();

  public abstract boolean canGetMonitorInfo();

  public abstract boolean canUseInstanceFilters();

  public abstract boolean canRedefineClasses();

  public abstract boolean canAddMethod();

  public abstract boolean canUnrestrictedlyRedefineClasses();

  public abstract boolean canPopFrames();

  public abstract boolean canGetSourceDebugExtension();

  public abstract boolean canRequestVMDeathEvent();

  public abstract boolean canGetMethodReturnValues();

  public abstract boolean canGetInstanceInfo();

  public abstract boolean canUseSourceNameFilters();

  public abstract boolean canForceEarlyReturn();

  public abstract boolean canBeModified();

  public abstract boolean canRequestMonitorEvents();

  public abstract boolean canGetMonitorFrameInfo();

  public abstract boolean canGetClassFileVersion();

  public abstract boolean canGetConstantPool();

  public abstract void setDefaultStratum(String paramString);

  public abstract String getDefaultStratum();

  public abstract long[] instanceCounts(List<? extends ReferenceType> paramList);

  public abstract String description();

  public abstract String version();

  public abstract String name();

  public abstract void setDebugTraceMode(int paramInt);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VirtualMachine
 * JD-Core Version:    0.6.2
 */