/*     */ package com.sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public abstract class VirtualMachine
/*     */ {
/*     */   private AttachProvider provider;
/*     */   private String id;
/*     */   private volatile int hash;
/*     */ 
/*     */   protected VirtualMachine(AttachProvider paramAttachProvider, String paramString)
/*     */   {
/* 117 */     if (paramAttachProvider == null) {
/* 118 */       throw new NullPointerException("provider cannot be null");
/*     */     }
/* 120 */     if (paramString == null) {
/* 121 */       throw new NullPointerException("id cannot be null");
/*     */     }
/* 123 */     this.provider = paramAttachProvider;
/* 124 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public static List<VirtualMachineDescriptor> list()
/*     */   {
/* 143 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 145 */     List localList = AttachProvider.providers();
/* 146 */     for (AttachProvider localAttachProvider : localList) {
/* 147 */       localArrayList.addAll(localAttachProvider.listVirtualMachines());
/*     */     }
/* 149 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static VirtualMachine attach(String paramString)
/*     */     throws AttachNotSupportedException, IOException
/*     */   {
/* 198 */     if (paramString == null) {
/* 199 */       throw new NullPointerException("id cannot be null");
/*     */     }
/* 201 */     List localList = AttachProvider.providers();
/* 202 */     if (localList.size() == 0) {
/* 203 */       throw new AttachNotSupportedException("no providers installed");
/*     */     }
/* 205 */     Object localObject = null;
/* 206 */     for (AttachProvider localAttachProvider : localList) {
/*     */       try {
/* 208 */         return localAttachProvider.attachVirtualMachine(paramString);
/*     */       } catch (AttachNotSupportedException localAttachNotSupportedException) {
/* 210 */         localObject = localAttachNotSupportedException;
/*     */       }
/*     */     }
/* 213 */     throw localObject;
/*     */   }
/*     */ 
/*     */   public static VirtualMachine attach(VirtualMachineDescriptor paramVirtualMachineDescriptor)
/*     */     throws AttachNotSupportedException, IOException
/*     */   {
/* 250 */     return paramVirtualMachineDescriptor.provider().attachVirtualMachine(paramVirtualMachineDescriptor);
/*     */   }
/*     */ 
/*     */   public abstract void detach()
/*     */     throws IOException;
/*     */ 
/*     */   public final AttachProvider provider()
/*     */   {
/* 278 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final String id()
/*     */   {
/* 287 */     return this.id;
/*     */   }
/*     */ 
/*     */   public abstract void loadAgentLibrary(String paramString1, String paramString2)
/*     */     throws AgentLoadException, AgentInitializationException, IOException;
/*     */ 
/*     */   public void loadAgentLibrary(String paramString)
/*     */     throws AgentLoadException, AgentInitializationException, IOException
/*     */   {
/* 378 */     loadAgentLibrary(paramString, null);
/*     */   }
/*     */ 
/*     */   public abstract void loadAgentPath(String paramString1, String paramString2)
/*     */     throws AgentLoadException, AgentInitializationException, IOException;
/*     */ 
/*     */   public void loadAgentPath(String paramString)
/*     */     throws AgentLoadException, AgentInitializationException, IOException
/*     */   {
/* 473 */     loadAgentPath(paramString, null);
/*     */   }
/*     */ 
/*     */   public abstract void loadAgent(String paramString1, String paramString2)
/*     */     throws AgentLoadException, AgentInitializationException, IOException;
/*     */ 
/*     */   public void loadAgent(String paramString)
/*     */     throws AgentLoadException, AgentInitializationException, IOException
/*     */   {
/* 540 */     loadAgent(paramString, null);
/*     */   }
/*     */ 
/*     */   public abstract Properties getSystemProperties()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Properties getAgentProperties()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void startManagementAgent(Properties paramProperties)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract String startLocalManagementAgent()
/*     */     throws IOException;
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 678 */     if (this.hash != 0) {
/* 679 */       return this.hash;
/*     */     }
/* 681 */     this.hash = (this.provider.hashCode() * 127 + this.id.hashCode());
/* 682 */     return this.hash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 703 */     if (paramObject == this)
/* 704 */       return true;
/* 705 */     if (!(paramObject instanceof VirtualMachine))
/* 706 */       return false;
/* 707 */     VirtualMachine localVirtualMachine = (VirtualMachine)paramObject;
/* 708 */     if (localVirtualMachine.provider() != provider()) {
/* 709 */       return false;
/*     */     }
/* 711 */     if (!localVirtualMachine.id().equals(id())) {
/* 712 */       return false;
/*     */     }
/* 714 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 721 */     return this.provider.toString() + ": " + this.id;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.attach.VirtualMachine
 * JD-Core Version:    0.6.2
 */