/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ public abstract class AbstractPerfDataBuffer
/*     */ {
/*     */   protected PerfDataBufferImpl impl;
/*     */ 
/*     */   public int getLocalVmId()
/*     */   {
/*  59 */     return this.impl.getLocalVmId();
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/*  71 */     return this.impl.getBytes();
/*     */   }
/*     */ 
/*     */   public int getCapacity()
/*     */   {
/*  80 */     return this.impl.getCapacity();
/*     */   }
/*     */ 
/*     */   public Monitor findByName(String paramString)
/*     */     throws MonitorException
/*     */   {
/* 100 */     return this.impl.findByName(paramString);
/*     */   }
/*     */ 
/*     */   public List<Monitor> findByPattern(String paramString)
/*     */     throws MonitorException
/*     */   {
/* 121 */     return this.impl.findByPattern(paramString);
/*     */   }
/*     */ 
/*     */   public MonitorStatus getMonitorStatus()
/*     */     throws MonitorException
/*     */   {
/* 133 */     return this.impl.getMonitorStatus();
/*     */   }
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 143 */     return this.impl.getByteBuffer();
/*     */   }
/*     */ 
/*     */   protected void createPerfDataBuffer(ByteBuffer paramByteBuffer, int paramInt)
/*     */     throws MonitorException
/*     */   {
/* 162 */     int i = AbstractPerfDataBufferPrologue.getMajorVersion(paramByteBuffer);
/* 163 */     int j = AbstractPerfDataBufferPrologue.getMinorVersion(paramByteBuffer);
/*     */ 
/* 166 */     String str = "sun.jvmstat.perfdata.monitor.v" + i + "_" + j + ".PerfDataBuffer";
/*     */     try
/*     */     {
/* 171 */       Class localClass = Class.forName(str);
/* 172 */       localObject = localClass.getConstructor(new Class[] { 
/* 173 */         Class.forName("java.nio.ByteBuffer"), 
/* 173 */         Integer.TYPE });
/*     */ 
/* 177 */       this.impl = ((PerfDataBufferImpl)((Constructor)localObject).newInstance(new Object[] { paramByteBuffer, new Integer(paramInt) }));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 184 */       throw new IllegalArgumentException("Could not find " + str + ": " + localClassNotFoundException
/* 184 */         .getMessage(), localClassNotFoundException);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/* 190 */       throw new IllegalArgumentException("Expected constructor missing in " + str + ": " + localNoSuchMethodException
/* 190 */         .getMessage(), localNoSuchMethodException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 196 */       throw new IllegalArgumentException("Unexpected constructor access in " + str + ": " + localIllegalAccessException
/* 196 */         .getMessage(), localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException)
/*     */     {
/* 200 */       throw new IllegalArgumentException(str + "is abstract: " + localInstantiationException
/* 200 */         .getMessage(), localInstantiationException);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 203 */       Object localObject = localInvocationTargetException.getCause();
/* 204 */       if ((localObject instanceof MonitorException)) {
/* 205 */         throw ((MonitorException)localObject);
/*     */       }
/*     */ 
/* 208 */       throw new RuntimeException("Unexpected exception: " + localInvocationTargetException
/* 208 */         .getMessage(), localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer
 * JD-Core Version:    0.6.2
 */