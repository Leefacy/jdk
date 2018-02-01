/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ 
/*     */ public abstract class MonitoredHost
/*     */ {
/*  51 */   private static Map<HostIdentifier, MonitoredHost> monitoredHosts = new HashMap();
/*     */   private static final String IMPL_OVERRIDE_PROP_NAME = "sun.jvmstat.monitor.MonitoredHost";
/*     */   private static final String IMPL_PKG_PROP_NAME = "sun.jvmstat.monitor.package";
/*  77 */   private static final String IMPL_PACKAGE = System.getProperty("sun.jvmstat.monitor.package", "sun.jvmstat.perfdata")
/*  77 */     ;
/*     */   private static final String LOCAL_PROTOCOL_PROP_NAME = "sun.jvmstat.monitor.local";
/*  89 */   private static final String LOCAL_PROTOCOL = System.getProperty("sun.jvmstat.monitor.local", "local")
/*  89 */     ;
/*     */   private static final String REMOTE_PROTOCOL_PROP_NAME = "sun.jvmstat.monitor.remote";
/* 101 */   private static final String REMOTE_PROTOCOL = System.getProperty("sun.jvmstat.monitor.remote", "rmi")
/* 101 */     ;
/*     */   private static final String MONITORED_HOST_CLASS = "MonitoredHostProvider";
/*     */   protected HostIdentifier hostId;
/*     */   protected int interval;
/*     */   protected Exception lastException;
/*     */ 
/*     */   public static MonitoredHost getMonitoredHost(String paramString)
/*     */     throws MonitorException, URISyntaxException
/*     */   {
/* 142 */     HostIdentifier localHostIdentifier = new HostIdentifier(paramString);
/* 143 */     return getMonitoredHost(localHostIdentifier);
/*     */   }
/*     */ 
/*     */   public static MonitoredHost getMonitoredHost(VmIdentifier paramVmIdentifier)
/*     */     throws MonitorException
/*     */   {
/* 164 */     HostIdentifier localHostIdentifier = new HostIdentifier(paramVmIdentifier);
/* 165 */     return getMonitoredHost(localHostIdentifier);
/*     */   }
/*     */ 
/*     */   public static MonitoredHost getMonitoredHost(HostIdentifier paramHostIdentifier)
/*     */     throws MonitorException
/*     */   {
/* 184 */     String str = System.getProperty("sun.jvmstat.monitor.MonitoredHost");
/* 185 */     MonitoredHost localMonitoredHost = null;
/*     */ 
/* 187 */     synchronized (monitoredHosts) {
/* 188 */       localMonitoredHost = (MonitoredHost)monitoredHosts.get(paramHostIdentifier);
/* 189 */       if (localMonitoredHost != null) {
/* 190 */         if (localMonitoredHost.isErrored())
/* 191 */           monitoredHosts.remove(paramHostIdentifier);
/*     */         else {
/* 193 */           return localMonitoredHost;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 198 */     paramHostIdentifier = resolveHostId(paramHostIdentifier);
/*     */ 
/* 200 */     if (str == null)
/*     */     {
/* 203 */       str = IMPL_PACKAGE + ".monitor.protocol." + paramHostIdentifier
/* 203 */         .getScheme() + "." + "MonitoredHostProvider";
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 208 */       ??? = Class.forName(str);
/*     */ 
/* 210 */       localObject2 = ((Class)???).getConstructor(new Class[] { paramHostIdentifier
/* 211 */         .getClass() });
/*     */ 
/* 214 */       localMonitoredHost = (MonitoredHost)((Constructor)localObject2).newInstance(new Object[] { paramHostIdentifier });
/*     */ 
/* 216 */       synchronized (monitoredHosts) {
/* 217 */         monitoredHosts.put(localMonitoredHost.hostId, localMonitoredHost);
/*     */       }
/* 219 */       return localMonitoredHost;
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 223 */       throw new IllegalArgumentException("Could not find " + str + ": " + localClassNotFoundException
/* 223 */         .getMessage(), localClassNotFoundException);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/* 228 */       throw new IllegalArgumentException("Expected constructor missing in " + str + ": " + localNoSuchMethodException
/* 228 */         .getMessage(), localNoSuchMethodException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 233 */       throw new IllegalArgumentException("Unexpected constructor access in " + str + ": " + localIllegalAccessException
/* 233 */         .getMessage(), localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 236 */       throw new IllegalArgumentException(str + "is abstract: " + localInstantiationException
/* 236 */         .getMessage(), localInstantiationException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 238 */       Object localObject2 = localInvocationTargetException.getCause();
/* 239 */       if ((localObject2 instanceof MonitorException)) {
/* 240 */         throw ((MonitorException)localObject2);
/*     */       }
/* 242 */       throw new RuntimeException("Unexpected exception", localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static HostIdentifier resolveHostId(HostIdentifier paramHostIdentifier)
/*     */     throws MonitorException
/*     */   {
/* 258 */     String str1 = paramHostIdentifier.getHost();
/* 259 */     String str2 = paramHostIdentifier.getScheme();
/* 260 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 262 */     assert (str1 != null);
/*     */ 
/* 264 */     if (str2 == null) {
/* 265 */       if (str1.compareTo("localhost") == 0)
/* 266 */         str2 = LOCAL_PROTOCOL;
/*     */       else {
/* 268 */         str2 = REMOTE_PROTOCOL;
/*     */       }
/*     */     }
/*     */ 
/* 272 */     localStringBuffer.append(str2).append(":").append(paramHostIdentifier.getSchemeSpecificPart());
/*     */ 
/* 274 */     String str3 = paramHostIdentifier.getFragment();
/* 275 */     if (str3 != null) {
/* 276 */       localStringBuffer.append("#").append(str3);
/*     */     }
/*     */     try
/*     */     {
/* 280 */       return new HostIdentifier(localStringBuffer.toString());
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException) {
/* 283 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 285 */     throw new IllegalArgumentException("Malformed URI created: " + localStringBuffer
/* 285 */       .toString());
/*     */   }
/*     */ 
/*     */   public HostIdentifier getHostIdentifier()
/*     */   {
/* 295 */     return this.hostId;
/*     */   }
/*     */ 
/*     */   public void setInterval(int paramInt)
/*     */   {
/* 306 */     this.interval = paramInt;
/*     */   }
/*     */ 
/*     */   public int getInterval()
/*     */   {
/* 315 */     return this.interval;
/*     */   }
/*     */ 
/*     */   public void setLastException(Exception paramException)
/*     */   {
/* 324 */     this.lastException = paramException;
/*     */   }
/*     */ 
/*     */   public Exception getLastException()
/*     */   {
/* 335 */     return this.lastException;
/*     */   }
/*     */ 
/*     */   public void clearLastException()
/*     */   {
/* 342 */     this.lastException = null;
/*     */   }
/*     */ 
/*     */   public boolean isErrored()
/*     */   {
/* 355 */     return this.lastException != null;
/*     */   }
/*     */ 
/*     */   public abstract MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier)
/*     */     throws MonitorException;
/*     */ 
/*     */   public abstract MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt)
/*     */     throws MonitorException;
/*     */ 
/*     */   public abstract void detach(MonitoredVm paramMonitoredVm)
/*     */     throws MonitorException;
/*     */ 
/*     */   public abstract void addHostListener(HostListener paramHostListener)
/*     */     throws MonitorException;
/*     */ 
/*     */   public abstract void removeHostListener(HostListener paramHostListener)
/*     */     throws MonitorException;
/*     */ 
/*     */   public abstract Set<Integer> activeVms()
/*     */     throws MonitorException;
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.MonitoredHost
 * JD-Core Version:    0.6.2
 */