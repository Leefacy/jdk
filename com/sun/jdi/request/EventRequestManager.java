package com.sun.jdi.request;

import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Mirror;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import java.util.List;
import jdk.Exported;

@Exported
public abstract interface EventRequestManager extends Mirror
{
  public abstract ClassPrepareRequest createClassPrepareRequest();

  public abstract ClassUnloadRequest createClassUnloadRequest();

  public abstract ThreadStartRequest createThreadStartRequest();

  public abstract ThreadDeathRequest createThreadDeathRequest();

  public abstract ExceptionRequest createExceptionRequest(ReferenceType paramReferenceType, boolean paramBoolean1, boolean paramBoolean2);

  public abstract MethodEntryRequest createMethodEntryRequest();

  public abstract MethodExitRequest createMethodExitRequest();

  public abstract MonitorContendedEnterRequest createMonitorContendedEnterRequest();

  public abstract MonitorContendedEnteredRequest createMonitorContendedEnteredRequest();

  public abstract MonitorWaitRequest createMonitorWaitRequest();

  public abstract MonitorWaitedRequest createMonitorWaitedRequest();

  public abstract StepRequest createStepRequest(ThreadReference paramThreadReference, int paramInt1, int paramInt2);

  public abstract BreakpointRequest createBreakpointRequest(Location paramLocation);

  public abstract AccessWatchpointRequest createAccessWatchpointRequest(Field paramField);

  public abstract ModificationWatchpointRequest createModificationWatchpointRequest(Field paramField);

  public abstract VMDeathRequest createVMDeathRequest();

  public abstract void deleteEventRequest(EventRequest paramEventRequest);

  public abstract void deleteEventRequests(List<? extends EventRequest> paramList);

  public abstract void deleteAllBreakpoints();

  public abstract List<StepRequest> stepRequests();

  public abstract List<ClassPrepareRequest> classPrepareRequests();

  public abstract List<ClassUnloadRequest> classUnloadRequests();

  public abstract List<ThreadStartRequest> threadStartRequests();

  public abstract List<ThreadDeathRequest> threadDeathRequests();

  public abstract List<ExceptionRequest> exceptionRequests();

  public abstract List<BreakpointRequest> breakpointRequests();

  public abstract List<AccessWatchpointRequest> accessWatchpointRequests();

  public abstract List<ModificationWatchpointRequest> modificationWatchpointRequests();

  public abstract List<MethodEntryRequest> methodEntryRequests();

  public abstract List<MethodExitRequest> methodExitRequests();

  public abstract List<MonitorContendedEnterRequest> monitorContendedEnterRequests();

  public abstract List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests();

  public abstract List<MonitorWaitRequest> monitorWaitRequests();

  public abstract List<MonitorWaitedRequest> monitorWaitedRequests();

  public abstract List<VMDeathRequest> vmDeathRequests();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.EventRequestManager
 * JD-Core Version:    0.6.2
 */