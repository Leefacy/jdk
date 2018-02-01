/*     */ package com.sun.tools.internal.ws;
/*     */ 
/*     */ import com.sun.istack.internal.tools.MaskingClassLoader;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.Options.Target;
/*     */ import com.sun.tools.internal.xjc.api.util.ToolsJarNotFoundException;
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public final class Invoker
/*     */ {
/*     */   static final String[] maskedPackages;
/*     */   public static final boolean noSystemProxies;
/*     */ 
/*     */   static int invoke(String mainClass, String[] args)
/*     */     throws Throwable
/*     */   {
/* 100 */     if (!noSystemProxies) {
/*     */       try {
/* 102 */         System.setProperty("java.net.useSystemProxies", "true");
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */     }
/* 108 */     ClassLoader oldcc = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 110 */       ClassLoader cl = Invoker.class.getClassLoader();
/* 111 */       if (Arrays.asList(args).contains("-Xendorsed")) {
/* 112 */         cl = createClassLoader(cl);
/*     */       } else {
/* 114 */         int targetArgIndex = Arrays.asList(args).indexOf("-target");
/*     */         Options.Target targetVersion;
/*     */         Options.Target targetVersion;
/* 116 */         if (targetArgIndex != -1)
/* 117 */           targetVersion = Options.Target.parse(args[(targetArgIndex + 1)]);
/*     */         else {
/* 119 */           targetVersion = Options.Target.getDefault();
/*     */         }
/* 121 */         Options.Target loadedVersion = Options.Target.getLoadedAPIVersion();
/*     */ 
/* 124 */         if (!loadedVersion.isLaterThan(targetVersion)) {
/* 125 */           if (Service.class.getClassLoader() == null)
/* 126 */             System.err.println(WscompileMessages.INVOKER_NEED_ENDORSED(loadedVersion.getVersion(), targetVersion.getVersion()));
/*     */           else {
/* 128 */             System.err.println(WscompileMessages.WRAPPER_TASK_LOADING_INCORRECT_API(loadedVersion.getVersion(), Which.which(Service.class), targetVersion.getVersion()));
/*     */           }
/* 130 */           return -1;
/*     */         }
/*     */ 
/* 134 */         Object urls = new ArrayList();
/* 135 */         findToolsJar(cl, (List)urls);
/*     */ 
/* 137 */         if (((List)urls).size() > 0) {
/* 138 */           List mask = new ArrayList(Arrays.asList(maskedPackages));
/*     */ 
/* 142 */           cl = new MaskingClassLoader(cl, mask);
/*     */ 
/* 145 */           cl = new URLClassLoader((URL[])((List)urls).toArray(new URL[((List)urls).size()]), cl);
/*     */ 
/* 148 */           cl = new ParallelWorldClassLoader(cl, "");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 153 */       Thread.currentThread().setContextClassLoader(cl);
/*     */ 
/* 155 */       Class compileTool = cl.loadClass(mainClass);
/* 156 */       Constructor ctor = compileTool.getConstructor(new Class[] { OutputStream.class });
/* 157 */       Object tool = ctor.newInstance(new Object[] { System.out });
/* 158 */       Method runMethod = compileTool.getMethod("run", new Class[] { [Ljava.lang.String.class });
/* 159 */       boolean r = ((Boolean)runMethod.invoke(tool, new Object[] { args })).booleanValue();
/* 160 */       return r ? 0 : 1;
/*     */     } catch (ToolsJarNotFoundException e) {
/* 162 */       System.err.println(e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 164 */       throw e.getCause();
/*     */     } catch (ClassNotFoundException e) {
/* 166 */       throw e;
/*     */     } finally {
/* 168 */       Thread.currentThread().setContextClassLoader(oldcc);
/*     */     }
/*     */ 
/* 171 */     return -1;
/*     */   }
/*     */ 
/*     */   public static boolean checkIfLoading21API()
/*     */   {
/*     */     try
/*     */     {
/* 179 */       Service.class.getMethod("getPort", new Class[] { Class.class, [Ljavax.xml.ws.WebServiceFeature.class });
/*     */ 
/* 181 */       return true;
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/*     */     catch (LinkageError localLinkageError) {
/*     */     }
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean checkIfLoading22API()
/*     */   {
/*     */     try
/*     */     {
/* 194 */       Service.class.getMethod("create", new Class[] { URL.class, QName.class, [Ljavax.xml.ws.WebServiceFeature.class });
/*     */ 
/* 196 */       return true;
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/*     */     catch (LinkageError localLinkageError) {
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public static ClassLoader createClassLoader(ClassLoader cl)
/*     */     throws ClassNotFoundException, IOException, ToolsJarNotFoundException
/*     */   {
/* 211 */     URL[] urls = findIstack22APIs(cl);
/* 212 */     if (urls.length == 0) {
/* 213 */       return cl;
/*     */     }
/* 215 */     List mask = new ArrayList(Arrays.asList(maskedPackages));
/* 216 */     if (urls.length > 1)
/*     */     {
/* 218 */       mask.add("javax.xml.bind.");
/* 219 */       mask.add("javax.xml.ws.");
/*     */     }
/*     */ 
/* 224 */     cl = new MaskingClassLoader(cl, mask);
/*     */ 
/* 227 */     cl = new URLClassLoader(urls, cl);
/*     */ 
/* 230 */     cl = new ParallelWorldClassLoader(cl, "");
/*     */ 
/* 232 */     return cl;
/*     */   }
/*     */ 
/*     */   private static URL[] findIstack22APIs(ClassLoader cl)
/*     */     throws ClassNotFoundException, IOException, ToolsJarNotFoundException
/*     */   {
/* 239 */     List urls = new ArrayList();
/*     */ 
/* 241 */     if (Service.class.getClassLoader() == null)
/*     */     {
/* 243 */       URL res = cl.getResource("javax/xml/ws/EndpointContext.class");
/* 244 */       if (res == null)
/* 245 */         throw new ClassNotFoundException("There's no JAX-WS 2.2 API in the classpath");
/* 246 */       urls.add(ParallelWorldClassLoader.toJarUrl(res));
/* 247 */       res = cl.getResource("javax/xml/bind/JAXBPermission.class");
/* 248 */       if (res == null)
/* 249 */         throw new ClassNotFoundException("There's no JAXB 2.2 API in the classpath");
/* 250 */       urls.add(ParallelWorldClassLoader.toJarUrl(res));
/*     */     }
/*     */ 
/* 253 */     findToolsJar(cl, urls);
/*     */ 
/* 255 */     return (URL[])urls.toArray(new URL[urls.size()]);
/*     */   }
/*     */ 
/*     */   private static void findToolsJar(ClassLoader cl, List<URL> urls) throws ToolsJarNotFoundException, MalformedURLException {
/*     */     try {
/* 260 */       Class.forName("com.sun.tools.javac.Main", false, cl);
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/* 267 */       File jreHome = new File(System.getProperty("java.home"));
/* 268 */       File toolsJar = new File(jreHome.getParent(), "lib/tools.jar");
/*     */ 
/* 270 */       if (!toolsJar.exists()) {
/* 271 */         throw new ToolsJarNotFoundException(toolsJar);
/*     */       }
/* 273 */       urls.add(toolsJar.toURL());
/*     */     }
/*     */   }
/*     */ 
/*     */   // ERROR //
/*     */   static
/*     */   {
/*     */     // Byte code:
/*     */     //   0: bipush 13
/*     */     //   2: anewarray 212	java/lang/String
/*     */     //   5: dup
/*     */     //   6: iconst_0
/*     */     //   7: ldc 10
/*     */     //   9: aastore
/*     */     //   10: dup
/*     */     //   11: iconst_1
/*     */     //   12: ldc 12
/*     */     //   14: aastore
/*     */     //   15: dup
/*     */     //   16: iconst_2
/*     */     //   17: ldc 14
/*     */     //   19: aastore
/*     */     //   20: dup
/*     */     //   21: iconst_3
/*     */     //   22: ldc 13
/*     */     //   24: aastore
/*     */     //   25: dup
/*     */     //   26: iconst_4
/*     */     //   27: ldc 9
/*     */     //   29: aastore
/*     */     //   30: dup
/*     */     //   31: iconst_5
/*     */     //   32: ldc 11
/*     */     //   34: aastore
/*     */     //   35: dup
/*     */     //   36: bipush 6
/*     */     //   38: ldc 19
/*     */     //   40: aastore
/*     */     //   41: dup
/*     */     //   42: bipush 7
/*     */     //   44: ldc 16
/*     */     //   46: aastore
/*     */     //   47: dup
/*     */     //   48: bipush 8
/*     */     //   50: ldc 7
/*     */     //   52: aastore
/*     */     //   53: dup
/*     */     //   54: bipush 9
/*     */     //   56: ldc 29
/*     */     //   58: aastore
/*     */     //   59: dup
/*     */     //   60: bipush 10
/*     */     //   62: ldc 17
/*     */     //   64: aastore
/*     */     //   65: dup
/*     */     //   66: bipush 11
/*     */     //   68: ldc 18
/*     */     //   70: aastore
/*     */     //   71: dup
/*     */     //   72: bipush 12
/*     */     //   74: ldc 8
/*     */     //   76: aastore
/*     */     //   77: putstatic 337	com/sun/tools/internal/ws/Invoker:maskedPackages	[Ljava/lang/String;
/*     */     //   80: iconst_0
/*     */     //   81: istore_0
/*     */     //   82: new 213	java/lang/StringBuilder
/*     */     //   85: dup
/*     */     //   86: invokespecial 373	java/lang/StringBuilder:<init>	()V
/*     */     //   89: ldc 34
/*     */     //   91: invokevirtual 365	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   94: invokevirtual 375	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   97: ldc 4
/*     */     //   99: invokevirtual 375	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   102: invokevirtual 374	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   105: invokestatic 363	java/lang/Boolean:getBoolean	(Ljava/lang/String;)Z
/*     */     //   108: istore_0
/*     */     //   109: iload_0
/*     */     //   110: putstatic 336	com/sun/tools/internal/ws/Invoker:noSystemProxies	Z
/*     */     //   113: goto +18 -> 131
/*     */     //   116: astore_1
/*     */     //   117: iload_0
/*     */     //   118: putstatic 336	com/sun/tools/internal/ws/Invoker:noSystemProxies	Z
/*     */     //   121: goto +10 -> 131
/*     */     //   124: astore_2
/*     */     //   125: iload_0
/*     */     //   126: putstatic 336	com/sun/tools/internal/ws/Invoker:noSystemProxies	Z
/*     */     //   129: aload_2
/*     */     //   130: athrow
/*     */     //   131: return
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   82	109	116	java/lang/SecurityException
/*     */     //   82	109	124	finally
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.Invoker
 * JD-Core Version:    0.6.2
 */