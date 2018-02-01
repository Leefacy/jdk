/*     */ package sun.tools.jstatd;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.ConnectException;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.RMISecurityManager;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ 
/*     */ public class Jstatd
/*     */ {
/*     */   private static Registry registry;
/*  46 */   private static int port = -1;
/*  47 */   private static boolean startRegistry = true;
/*     */ 
/*     */   private static void printUsage() {
/*  50 */     System.err.println("usage: jstatd [-nr] [-p port] [-n rminame]");
/*     */   }
/*     */ 
/*     */   static void bind(String paramString, RemoteHostImpl paramRemoteHostImpl) throws RemoteException, MalformedURLException, Exception
/*     */   {
/*     */     try
/*     */     {
/*  57 */       Naming.rebind(paramString, paramRemoteHostImpl);
/*     */     }
/*     */     catch (ConnectException localConnectException)
/*     */     {
/*  63 */       if ((startRegistry) && (registry == null)) {
/*  64 */         int i = port < 0 ? 1099 : port;
/*  65 */         registry = LocateRegistry.createRegistry(i);
/*  66 */         bind(paramString, paramRemoteHostImpl);
/*     */       }
/*     */       else {
/*  69 */         System.out.println("Could not contact registry\n" + localConnectException
/*  70 */           .getMessage());
/*  71 */         localConnectException.printStackTrace();
/*     */       }
/*     */     } catch (RemoteException localRemoteException) {
/*  74 */       System.err.println("Could not bind " + paramString + " to RMI Registry");
/*  75 */       localRemoteException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString) {
/*  80 */     String str = null;
/*  81 */     for (int i = 0; 
/*  83 */       (i < paramArrayOfString.length) && (paramArrayOfString[i].startsWith("-")); i++) {
/*  84 */       localObject = paramArrayOfString[i];
/*     */ 
/*  86 */       if (((String)localObject).compareTo("-nr") == 0) {
/*  87 */         startRegistry = false;
/*  88 */       } else if (((String)localObject).startsWith("-p")) {
/*  89 */         if (((String)localObject).compareTo("-p") != 0) {
/*  90 */           port = Integer.parseInt(((String)localObject).substring(2));
/*     */         } else {
/*  92 */           i++;
/*  93 */           if (i >= paramArrayOfString.length) {
/*  94 */             printUsage();
/*  95 */             System.exit(1);
/*     */           }
/*  97 */           port = Integer.parseInt(paramArrayOfString[i]);
/*     */         }
/*  99 */       } else if (((String)localObject).startsWith("-n")) {
/* 100 */         if (((String)localObject).compareTo("-n") != 0) {
/* 101 */           str = ((String)localObject).substring(2);
/*     */         } else {
/* 103 */           i++;
/* 104 */           if (i >= paramArrayOfString.length) {
/* 105 */             printUsage();
/* 106 */             System.exit(1);
/*     */           }
/* 108 */           str = paramArrayOfString[i];
/*     */         }
/*     */       } else {
/* 111 */         printUsage();
/* 112 */         System.exit(1);
/*     */       }
/*     */     }
/*     */ 
/* 116 */     if (i < paramArrayOfString.length) {
/* 117 */       printUsage();
/* 118 */       System.exit(1);
/*     */     }
/*     */ 
/* 121 */     if (System.getSecurityManager() == null) {
/* 122 */       System.setSecurityManager(new RMISecurityManager());
/*     */     }
/*     */ 
/* 125 */     Object localObject = new StringBuilder();
/*     */ 
/* 127 */     if (port >= 0) {
/* 128 */       ((StringBuilder)localObject).append("//:").append(port);
/*     */     }
/*     */ 
/* 131 */     if (str == null) {
/* 132 */       str = "JStatRemoteHost";
/*     */     }
/*     */ 
/* 135 */     ((StringBuilder)localObject).append("/").append(str);
/*     */     try
/*     */     {
/* 139 */       System.setProperty("java.rmi.server.ignoreSubClasses", "true");
/* 140 */       RemoteHostImpl localRemoteHostImpl = new RemoteHostImpl();
/* 141 */       RemoteHost localRemoteHost = (RemoteHost)UnicastRemoteObject.exportObject(localRemoteHostImpl, 0);
/*     */ 
/* 143 */       bind(((StringBuilder)localObject).toString(), localRemoteHostImpl);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 145 */       if (str != null)
/* 146 */         System.out.println("Bad RMI server name: " + str);
/*     */       else {
/* 148 */         System.out.println("Bad RMI URL: " + localObject + " : " + localMalformedURLException
/* 149 */           .getMessage());
/*     */       }
/* 151 */       System.exit(1);
/*     */     }
/*     */     catch (ConnectException localConnectException) {
/* 154 */       System.out.println("Could not contact RMI registry\n" + localConnectException
/* 155 */         .getMessage());
/* 156 */       System.exit(1);
/*     */     } catch (Exception localException) {
/* 158 */       System.out.println("Could not create remote object\n" + localException
/* 159 */         .getMessage());
/* 160 */       localException.printStackTrace();
/* 161 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstatd.Jstatd
 * JD-Core Version:    0.6.2
 */