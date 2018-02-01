/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ public abstract class PerfDataBufferImpl
/*     */ {
/*     */   protected ByteBuffer buffer;
/*     */   protected Map<String, Monitor> monitors;
/*     */   protected int lvmid;
/*     */   protected Map<String, ArrayList<String>> aliasMap;
/*     */   protected Map aliasCache;
/*     */ 
/*     */   protected PerfDataBufferImpl(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  80 */     this.buffer = paramByteBuffer;
/*  81 */     this.lvmid = paramInt;
/*  82 */     this.monitors = new TreeMap();
/*  83 */     this.aliasMap = new HashMap();
/*  84 */     this.aliasCache = new HashMap();
/*     */   }
/*     */ 
/*     */   public int getLocalVmId()
/*     */   {
/*  94 */     return this.lvmid;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 106 */     ByteBuffer localByteBuffer = null;
/* 107 */     synchronized (this)
/*     */     {
/*     */       try
/*     */       {
/* 118 */         if (this.monitors.isEmpty()) {
/* 119 */           buildMonitorMap(this.monitors);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (MonitorException localMonitorException)
/*     */       {
/*     */       }
/*     */ 
/* 127 */       localByteBuffer = this.buffer.duplicate();
/*     */     }
/* 129 */     localByteBuffer.rewind();
/* 130 */     ??? = new byte[localByteBuffer.limit()];
/* 131 */     localByteBuffer.get((byte[])???);
/* 132 */     return ???;
/*     */   }
/*     */ 
/*     */   public int getCapacity()
/*     */   {
/* 141 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */   ByteBuffer getByteBuffer()
/*     */   {
/* 153 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   private void buildAliasMap()
/*     */   {
/* 162 */     assert (Thread.holdsLock(this));
/*     */ 
/* 164 */     URL localURL = null;
/* 165 */     String str = System.getProperty("sun.jvmstat.perfdata.aliasmap");
/*     */ 
/* 167 */     if (str != null) {
/* 168 */       localObject = new File(str);
/*     */       try {
/* 170 */         localURL = ((File)localObject).toURL();
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException) {
/* 173 */         throw new IllegalArgumentException(localMalformedURLException);
/*     */       }
/*     */     } else {
/* 176 */       localURL = getClass().getResource("/sun/jvmstat/perfdata/resources/aliasmap");
/*     */     }
/*     */ 
/* 180 */     assert (localURL != null);
/*     */ 
/* 182 */     Object localObject = new AliasFileParser(localURL);
/*     */     try
/*     */     {
/* 185 */       ((AliasFileParser)localObject).parse(this.aliasMap);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 188 */       System.err.println("Error processing " + str + ": " + localIOException
/* 189 */         .getMessage());
/*     */     } catch (SyntaxException localSyntaxException) {
/* 191 */       System.err.println("Syntax error parsing " + str + ": " + localSyntaxException
/* 192 */         .getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Monitor findByAlias(String paramString)
/*     */   {
/* 201 */     assert (Thread.holdsLock(this));
/*     */ 
/* 203 */     Monitor localMonitor = (Monitor)this.aliasCache.get(paramString);
/*     */     Iterator localIterator;
/* 204 */     if (localMonitor == null) {
/* 205 */       ArrayList localArrayList = (ArrayList)this.aliasMap.get(paramString);
/* 206 */       if (localArrayList != null) {
/* 207 */         for (localIterator = localArrayList.iterator(); (localIterator.hasNext()) && (localMonitor == null); ) {
/* 208 */           String str = (String)localIterator.next();
/* 209 */           localMonitor = (Monitor)this.monitors.get(str);
/*     */         }
/*     */       }
/*     */     }
/* 213 */     return localMonitor;
/*     */   }
/*     */ 
/*     */   public Monitor findByName(String paramString)
/*     */     throws MonitorException
/*     */   {
/* 237 */     Monitor localMonitor = null;
/*     */ 
/* 239 */     synchronized (this) {
/* 240 */       if (this.monitors.isEmpty()) {
/* 241 */         buildMonitorMap(this.monitors);
/* 242 */         buildAliasMap();
/*     */       }
/*     */ 
/* 246 */       localMonitor = (Monitor)this.monitors.get(paramString);
/* 247 */       if (localMonitor == null)
/*     */       {
/* 249 */         getNewMonitors(this.monitors);
/* 250 */         localMonitor = (Monitor)this.monitors.get(paramString);
/*     */       }
/* 252 */       if (localMonitor == null)
/*     */       {
/* 254 */         localMonitor = findByAlias(paramString);
/*     */       }
/*     */     }
/* 257 */     return localMonitor;
/*     */   }
/*     */ 
/*     */   public List<Monitor> findByPattern(String paramString)
/*     */     throws MonitorException, PatternSyntaxException
/*     */   {
/* 280 */     synchronized (this) {
/* 281 */       if (this.monitors.isEmpty())
/* 282 */         buildMonitorMap(this.monitors);
/*     */       else {
/* 284 */         getNewMonitors(this.monitors);
/*     */       }
/*     */     }
/*     */ 
/* 288 */     ??? = Pattern.compile(paramString);
/* 289 */     Matcher localMatcher = ((Pattern)???).matcher("");
/* 290 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 292 */     Set localSet = this.monitors.entrySet();
/*     */ 
/* 294 */     for (Iterator localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/* 295 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 296 */       String str = (String)localEntry.getKey();
/* 297 */       Monitor localMonitor = (Monitor)localEntry.getValue();
/*     */ 
/* 300 */       localMatcher.reset(str);
/*     */ 
/* 303 */       if (localMatcher.lookingAt()) {
/* 304 */         localArrayList.add((Monitor)localEntry.getValue());
/*     */       }
/*     */     }
/* 307 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public MonitorStatus getMonitorStatus()
/*     */     throws MonitorException
/*     */   {
/* 319 */     synchronized (this) {
/* 320 */       if (this.monitors.isEmpty()) {
/* 321 */         buildMonitorMap(this.monitors);
/*     */       }
/* 323 */       return getMonitorStatus(this.monitors);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract MonitorStatus getMonitorStatus(Map<String, Monitor> paramMap)
/*     */     throws MonitorException;
/*     */ 
/*     */   protected abstract void buildMonitorMap(Map<String, Monitor> paramMap)
/*     */     throws MonitorException;
/*     */ 
/*     */   protected abstract void getNewMonitors(Map<String, Monitor> paramMap)
/*     */     throws MonitorException;
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.PerfDataBufferImpl
 * JD-Core Version:    0.6.2
 */