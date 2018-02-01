/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.javac.file.Locations;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import javax.tools.DocumentationTool.Location;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ public class DocletInvoker
/*     */ {
/*     */   private final Class<?> docletClass;
/*     */   private final String docletClassName;
/*     */   private final ClassLoader appClassLoader;
/*     */   private final Messager messager;
/*     */   private final boolean apiMode;
/*     */ 
/*     */   private String appendPath(String paramString1, String paramString2)
/*     */   {
/*  76 */     if ((paramString1 == null) || (paramString1.length() == 0))
/*  77 */       return paramString2 == null ? "." : paramString2;
/*  78 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/*  79 */       return paramString1;
/*     */     }
/*  81 */     return paramString1 + File.pathSeparator + paramString2;
/*     */   }
/*     */ 
/*     */   public DocletInvoker(Messager paramMessager, Class<?> paramClass, boolean paramBoolean)
/*     */   {
/*  86 */     this.messager = paramMessager;
/*  87 */     this.docletClass = paramClass;
/*  88 */     this.docletClassName = paramClass.getName();
/*  89 */     this.appClassLoader = null;
/*  90 */     this.apiMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public DocletInvoker(Messager paramMessager, JavaFileManager paramJavaFileManager, String paramString1, String paramString2, ClassLoader paramClassLoader, boolean paramBoolean)
/*     */   {
/*  97 */     this.messager = paramMessager;
/*  98 */     this.docletClassName = paramString1;
/*  99 */     this.apiMode = paramBoolean;
/*     */ 
/* 101 */     if ((paramJavaFileManager != null) && (paramJavaFileManager.hasLocation(DocumentationTool.Location.DOCLET_PATH))) {
/* 102 */       this.appClassLoader = paramJavaFileManager.getClassLoader(DocumentationTool.Location.DOCLET_PATH);
/*     */     }
/*     */     else {
/* 105 */       localObject = null;
/*     */ 
/* 108 */       localObject = appendPath(System.getProperty("env.class.path"), (String)localObject);
/* 109 */       localObject = appendPath(System.getProperty("java.class.path"), (String)localObject);
/* 110 */       localObject = appendPath(paramString2, (String)localObject);
/* 111 */       URL[] arrayOfURL = Locations.pathToURLs((String)localObject);
/* 112 */       if (paramClassLoader == null)
/* 113 */         this.appClassLoader = new URLClassLoader(arrayOfURL, getDelegationClassLoader(paramString1));
/*     */       else {
/* 115 */         this.appClassLoader = new URLClassLoader(arrayOfURL, paramClassLoader);
/*     */       }
/*     */     }
/*     */ 
/* 119 */     Object localObject = null;
/*     */     try {
/* 121 */       localObject = this.appClassLoader.loadClass(paramString1);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 123 */       paramMessager.error(Messager.NOPOS, "main.doclet_class_not_found", new Object[] { paramString1 });
/* 124 */       paramMessager.exit();
/*     */     }
/* 126 */     this.docletClass = ((Class)localObject);
/*     */   }
/*     */ 
/*     */   private ClassLoader getDelegationClassLoader(String paramString)
/*     */   {
/* 146 */     ClassLoader localClassLoader1 = Thread.currentThread().getContextClassLoader();
/* 147 */     ClassLoader localClassLoader2 = ClassLoader.getSystemClassLoader();
/* 148 */     if (localClassLoader2 == null)
/* 149 */       return localClassLoader1;
/* 150 */     if (localClassLoader1 == null) {
/* 151 */       return localClassLoader2;
/*     */     }
/*     */     try
/*     */     {
/* 155 */       localClassLoader2.loadClass(paramString);
/*     */       try {
/* 157 */         localClassLoader1.loadClass(paramString);
/*     */       } catch (ClassNotFoundException localClassNotFoundException1) {
/* 159 */         return localClassLoader2;
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException2)
/*     */     {
/*     */     }
/*     */     try {
/* 166 */       if (getClass() == localClassLoader2.loadClass(getClass().getName()))
/*     */         try {
/* 168 */           if (getClass() != localClassLoader1.loadClass(getClass().getName()))
/* 169 */             return localClassLoader2;
/*     */         } catch (ClassNotFoundException localClassNotFoundException3) {
/* 171 */           return localClassLoader2;
/*     */         }
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException4)
/*     */     {
/*     */     }
/* 177 */     return localClassLoader1;
/*     */   }
/*     */ 
/*     */   public boolean start(RootDoc paramRootDoc) {
/* 185 */     String str = "start";
/* 186 */     Class[] arrayOfClass = { RootDoc.class };
/* 187 */     Object[] arrayOfObject = { paramRootDoc };
/*     */     Object localObject;
/*     */     try {
/* 189 */       localObject = invoke(str, null, arrayOfClass, arrayOfObject);
/*     */     } catch (DocletInvokeException localDocletInvokeException) {
/* 191 */       return false;
/*     */     }
/* 193 */     if ((localObject instanceof Boolean)) {
/* 194 */       return ((Boolean)localObject).booleanValue();
/*     */     }
/* 196 */     this.messager.error(Messager.NOPOS, "main.must_return_boolean", new Object[] { this.docletClassName, str });
/*     */ 
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   public int optionLength(String paramString)
/*     */   {
/* 209 */     String str = "optionLength";
/* 210 */     Class[] arrayOfClass = { String.class };
/* 211 */     Object[] arrayOfObject = { paramString };
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 213 */       localObject = invoke(str, new Integer(0), arrayOfClass, arrayOfObject);
/*     */     } catch (DocletInvokeException localDocletInvokeException) {
/* 215 */       return -1;
/*     */     }
/* 217 */     if ((localObject instanceof Integer)) {
/* 218 */       return ((Integer)localObject).intValue();
/*     */     }
/* 220 */     this.messager.error(Messager.NOPOS, "main.must_return_int", new Object[] { this.docletClassName, str });
/*     */ 
/* 222 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean validOptions(List<String[]> paramList) {
/* 232 */     String[][] arrayOfString = (String[][])paramList.toArray(new String[paramList.length()][]);
/* 233 */     String str = "validOptions";
/* 234 */     Messager localMessager = this.messager;
/* 235 */     Class[] arrayOfClass = { [[Ljava.lang.String.class, DocErrorReporter.class };
/* 236 */     Object[] arrayOfObject = { arrayOfString, localMessager };
/*     */     Object localObject;
/*     */     try {
/* 238 */       localObject = invoke(str, Boolean.TRUE, arrayOfClass, arrayOfObject);
/*     */     } catch (DocletInvokeException localDocletInvokeException) {
/* 240 */       return false;
/*     */     }
/* 242 */     if ((localObject instanceof Boolean)) {
/* 243 */       return ((Boolean)localObject).booleanValue();
/*     */     }
/* 245 */     this.messager.error(Messager.NOPOS, "main.must_return_boolean", new Object[] { this.docletClassName, str });
/*     */ 
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   public LanguageVersion languageVersion()
/*     */   {
/*     */     try
/*     */     {
/* 258 */       String str = "languageVersion";
/* 259 */       Class[] arrayOfClass = new Class[0];
/* 260 */       Object[] arrayOfObject = new Object[0];
/*     */       Object localObject;
/*     */       try {
/* 262 */         localObject = invoke(str, LanguageVersion.JAVA_1_1, arrayOfClass, arrayOfObject);
/*     */       } catch (DocletInvokeException localDocletInvokeException) {
/* 264 */         return LanguageVersion.JAVA_1_1;
/*     */       }
/* 266 */       if ((localObject instanceof LanguageVersion)) {
/* 267 */         return (LanguageVersion)localObject;
/*     */       }
/* 269 */       this.messager.error(Messager.NOPOS, "main.must_return_languageversion", new Object[] { this.docletClassName, str });
/*     */ 
/* 271 */       return LanguageVersion.JAVA_1_1;
/*     */     } catch (NoClassDefFoundError localNoClassDefFoundError) {
/*     */     }
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */   private Object invoke(String paramString, Object paramObject, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
/*     */     throws DocletInvoker.DocletInvokeException
/*     */   {
/*     */     Method localMethod;
/*     */     try
/*     */     {
/* 286 */       localMethod = this.docletClass.getMethod(paramString, paramArrayOfClass);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 288 */       if (paramObject == null) {
/* 289 */         this.messager.error(Messager.NOPOS, "main.doclet_method_not_found", new Object[] { this.docletClassName, paramString });
/*     */ 
/* 291 */         throw new DocletInvokeException(null);
/*     */       }
/* 293 */       return paramObject;
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 296 */       this.messager.error(Messager.NOPOS, "main.doclet_method_not_accessible", new Object[] { this.docletClassName, paramString });
/*     */ 
/* 298 */       throw new DocletInvokeException(null);
/*     */     }
/* 300 */     if (!Modifier.isStatic(localMethod.getModifiers())) {
/* 301 */       this.messager.error(Messager.NOPOS, "main.doclet_method_must_be_static", new Object[] { this.docletClassName, paramString });
/*     */ 
/* 303 */       throw new DocletInvokeException(null);
/*     */     }
/*     */ 
/* 306 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 308 */       if (this.appClassLoader != null)
/* 309 */         Thread.currentThread().setContextClassLoader(this.appClassLoader);
/* 310 */       return localMethod.invoke(null, paramArrayOfObject);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 312 */       this.messager.error(Messager.NOPOS, "main.internal_error_exception_thrown", new Object[] { this.docletClassName, paramString, localIllegalArgumentException
/* 313 */         .toString() });
/* 314 */       throw new DocletInvokeException(null);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 316 */       this.messager.error(Messager.NOPOS, "main.doclet_method_not_accessible", new Object[] { this.docletClassName, paramString });
/*     */ 
/* 318 */       throw new DocletInvokeException(null);
/*     */     } catch (NullPointerException localNullPointerException) {
/* 320 */       this.messager.error(Messager.NOPOS, "main.internal_error_exception_thrown", new Object[] { this.docletClassName, paramString, localNullPointerException
/* 321 */         .toString() });
/* 322 */       throw new DocletInvokeException(null);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 324 */       Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 325 */       if (this.apiMode)
/* 326 */         throw new ClientCodeException(localThrowable);
/* 327 */       if ((localThrowable instanceof OutOfMemoryError)) {
/* 328 */         this.messager.error(Messager.NOPOS, "main.out.of.memory", new Object[0]);
/*     */       } else {
/* 330 */         this.messager.error(Messager.NOPOS, "main.exception_thrown", new Object[] { this.docletClassName, paramString, localInvocationTargetException
/* 331 */           .toString() });
/* 332 */         localInvocationTargetException.getTargetException().printStackTrace();
/*     */       }
/* 334 */       throw new DocletInvokeException(null);
/*     */     } finally {
/* 336 */       Thread.currentThread().setContextClassLoader(localClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DocletInvokeException extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.DocletInvoker
 * JD-Core Version:    0.6.2
 */