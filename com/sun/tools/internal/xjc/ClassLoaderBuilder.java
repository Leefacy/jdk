/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.istack.internal.tools.MaskingClassLoader;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ 
/*     */ class ClassLoaderBuilder
/*     */ {
/* 102 */   private static String[] maskedPackages = { "com.sun.tools.", "com.sun.codemodel.internal.", "com.sun.relaxng.", "com.sun.xml.internal.xsom.", "com.sun.xml.internal.bind." };
/*     */ 
/* 111 */   private static String[] toolPackages = { "com.sun.tools.", "com.sun.codemodel.internal.", "com.sun.relaxng.", "com.sun.xml.internal.xsom." };
/*     */ 
/* 121 */   public static final boolean noHack = Boolean.getBoolean(XJCFacade.class.getName() + ".nohack");
/*     */ 
/*     */   protected static ClassLoader createProtectiveClassLoader(ClassLoader cl, String v)
/*     */     throws ClassNotFoundException, MalformedURLException
/*     */   {
/*  56 */     if (noHack) return cl;
/*     */ 
/*  58 */     boolean mustang = false;
/*     */ 
/*  60 */     if (SecureLoader.getClassClassLoader(JAXBContext.class) == null)
/*     */     {
/*  62 */       mustang = true;
/*     */ 
/*  64 */       List mask = new ArrayList(Arrays.asList(maskedPackages));
/*  65 */       mask.add("javax.xml.bind.");
/*     */ 
/*  67 */       cl = new MaskingClassLoader(cl, mask);
/*     */ 
/*  69 */       URL apiUrl = cl.getResource("javax/xml/bind/JAXBPermission.class");
/*  70 */       if (apiUrl == null) {
/*  71 */         throw new ClassNotFoundException("There's no JAXB 2.2 API in the classpath");
/*     */       }
/*  73 */       cl = new URLClassLoader(new URL[] { ParallelWorldClassLoader.toJarUrl(apiUrl) }, cl);
/*     */     }
/*     */ 
/*  82 */     if ("1.0".equals(v)) {
/*  83 */       if (!mustang)
/*     */       {
/*  85 */         cl = new MaskingClassLoader(cl, toolPackages);
/*  86 */       }cl = new ParallelWorldClassLoader(cl, "1.0/");
/*     */     }
/*  88 */     else if (mustang)
/*     */     {
/*  90 */       cl = new ParallelWorldClassLoader(cl, "");
/*     */     }
/*     */ 
/*  93 */     return cl;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.ClassLoaderBuilder
 * JD-Core Version:    0.6.2
 */