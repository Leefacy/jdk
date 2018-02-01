/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.javac.main.CommandLine;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class Start extends ToolOption.Helper
/*     */ {
/*     */   private final Context context;
/*     */   private final String defaultDocletClassName;
/*     */   private final ClassLoader docletParentClassLoader;
/*     */   private static final String javadocName = "javadoc";
/*     */   private static final String standardDocletClassName = "com.sun.tools.doclets.standard.Standard";
/*  74 */   private long defaultFilter = 5L;
/*     */   private final Messager messager;
/*     */   private DocletInvoker docletInvoker;
/*     */   private boolean apiMode;
/*     */ 
/*     */   Start(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2)
/*     */   {
/*  91 */     this(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2, null);
/*     */   }
/*     */ 
/*     */   Start(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, ClassLoader paramClassLoader)
/*     */   {
/* 100 */     this.context = new Context();
/* 101 */     this.messager = new Messager(this.context, paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3);
/* 102 */     this.defaultDocletClassName = paramString2;
/* 103 */     this.docletParentClassLoader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   Start(String paramString1, String paramString2) {
/* 107 */     this(paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   Start(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*     */   {
/* 112 */     this.context = new Context();
/* 113 */     this.messager = new Messager(this.context, paramString1);
/* 114 */     this.defaultDocletClassName = paramString2;
/* 115 */     this.docletParentClassLoader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   Start(String paramString, ClassLoader paramClassLoader) {
/* 119 */     this(paramString, "com.sun.tools.doclets.standard.Standard", paramClassLoader);
/*     */   }
/*     */ 
/*     */   Start(String paramString) {
/* 123 */     this(paramString, "com.sun.tools.doclets.standard.Standard");
/*     */   }
/*     */ 
/*     */   Start(ClassLoader paramClassLoader) {
/* 127 */     this("javadoc", paramClassLoader);
/*     */   }
/*     */ 
/*     */   Start() {
/* 131 */     this("javadoc");
/*     */   }
/*     */ 
/*     */   public Start(Context paramContext) {
/* 135 */     paramContext.getClass();
/* 136 */     this.context = paramContext;
/* 137 */     this.apiMode = true;
/* 138 */     this.defaultDocletClassName = "com.sun.tools.doclets.standard.Standard";
/* 139 */     this.docletParentClassLoader = null;
/*     */ 
/* 141 */     Log localLog = (Log)paramContext.get(Log.logKey);
/* 142 */     if ((localLog instanceof Messager)) {
/* 143 */       this.messager = ((Messager)localLog);
/*     */     } else {
/* 145 */       PrintWriter localPrintWriter = (PrintWriter)paramContext.get(Log.outKey);
/* 146 */       this.messager = (localPrintWriter == null ? new Messager(paramContext, "javadoc") : new Messager(paramContext, "javadoc", localPrintWriter, localPrintWriter, localPrintWriter));
/*     */     }
/*     */   }
/*     */ 
/*     */   void usage()
/*     */   {
/* 156 */     usage(true);
/*     */   }
/*     */ 
/*     */   void usage(boolean paramBoolean) {
/* 160 */     usage("main.usage", "-help", null, paramBoolean);
/*     */   }
/*     */ 
/*     */   void Xusage()
/*     */   {
/* 165 */     Xusage(true);
/*     */   }
/*     */ 
/*     */   void Xusage(boolean paramBoolean) {
/* 169 */     usage("main.Xusage", "-X", "main.Xusage.foot", paramBoolean);
/*     */   }
/*     */ 
/*     */   private void usage(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
/*     */   {
/* 175 */     this.messager.notice(paramString1, new Object[0]);
/*     */ 
/* 178 */     if (this.docletInvoker != null)
/*     */     {
/* 184 */       this.docletInvoker.optionLength(paramString2);
/*     */     }
/*     */ 
/* 187 */     if (paramString3 != null) {
/* 188 */       this.messager.notice(paramString3, new Object[0]);
/*     */     }
/* 190 */     if (paramBoolean) exit();
/*     */   }
/*     */ 
/*     */   private void exit()
/*     */   {
/* 197 */     this.messager.exit();
/*     */   }
/*     */ 
/*     */   int begin(String[] paramArrayOfString)
/*     */   {
/* 205 */     boolean bool = begin(null, paramArrayOfString, Collections.emptySet());
/* 206 */     return bool ? 0 : 1;
/*     */   }
/*     */ 
/*     */   public boolean begin(Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1) {
/* 210 */     ArrayList localArrayList = new ArrayList();
/*     */     String str;
/* 211 */     for (Iterator localIterator = paramIterable.iterator(); localIterator.hasNext(); localArrayList.add(str)) str = (String)localIterator.next();
/* 212 */     return begin(paramClass, (String[])localArrayList.toArray(new String[localArrayList.size()]), paramIterable1);
/*     */   }
/*     */ 
/*     */   private boolean begin(Class<?> paramClass, String[] paramArrayOfString, Iterable<? extends JavaFileObject> paramIterable) {
/* 216 */     int i = 0;
/*     */     try
/*     */     {
/* 219 */       i = !parseAndExecute(paramClass, paramArrayOfString, paramIterable) ? 1 : 0;
/*     */     } catch (Messager.ExitJavadoc localExitJavadoc) {
/*     */     }
/*     */     catch (OutOfMemoryError localOutOfMemoryError) {
/* 223 */       this.messager.error(Messager.NOPOS, "main.out.of.memory", new Object[0]);
/* 224 */       i = 1;
/*     */     }
/*     */     catch (ClientCodeException localClientCodeException) {
/* 227 */       throw localClientCodeException;
/*     */     } catch (Error localError) {
/* 229 */       localError.printStackTrace(System.err);
/* 230 */       this.messager.error(Messager.NOPOS, "main.fatal.error", new Object[0]);
/* 231 */       i = 1;
/*     */     } catch (Exception localException) {
/* 233 */       localException.printStackTrace(System.err);
/* 234 */       this.messager.error(Messager.NOPOS, "main.fatal.exception", new Object[0]);
/* 235 */       i = 1;
/*     */     } finally {
/* 237 */       this.messager.exitNotice();
/* 238 */       this.messager.flush();
/*     */     }
/* 240 */     i |= (this.messager.nerrors() > 0 ? 1 : 0);
/* 241 */     i |= ((this.rejectWarnings) && (this.messager.nwarnings() > 0) ? 1 : 0);
/* 242 */     return i == 0;
/*     */   }
/*     */ 
/*     */   private boolean parseAndExecute(Class<?> paramClass, String[] paramArrayOfString, Iterable<? extends JavaFileObject> paramIterable)
/*     */     throws IOException
/*     */   {
/* 252 */     long l = System.currentTimeMillis();
/*     */ 
/* 254 */     ListBuffer localListBuffer1 = new ListBuffer();
/*     */     try
/*     */     {
/* 258 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 260 */       this.messager.error(Messager.NOPOS, "main.cant.read", new Object[] { localFileNotFoundException.getMessage() });
/* 261 */       exit();
/*     */     } catch (IOException localIOException) {
/* 263 */       localIOException.printStackTrace(System.err);
/* 264 */       exit();
/*     */     }
/*     */ 
/* 268 */     JavaFileManager localJavaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/* 269 */     setDocletInvoker(paramClass, localJavaFileManager, paramArrayOfString);
/*     */ 
/* 271 */     this.compOpts = Options.instance(this.context);
/*     */ 
/* 273 */     this.compOpts.put("-Xlint:-options", "-Xlint:-options");
/*     */ 
/* 276 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 277 */       localObject1 = paramArrayOfString[i];
/*     */ 
/* 279 */       localObject2 = ToolOption.get((String)localObject1);
/* 280 */       if (localObject2 != null)
/*     */       {
/* 282 */         if ((localObject2 == ToolOption.LOCALE) && (i > 0)) {
/* 283 */           usageError("main.locale_first", new Object[0]);
/*     */         }
/* 285 */         if (((ToolOption)localObject2).hasArg) {
/* 286 */           oneArg(paramArrayOfString, i++);
/* 287 */           ((ToolOption)localObject2).process(this, paramArrayOfString[i]);
/*     */         } else {
/* 289 */           setOption((String)localObject1);
/* 290 */           ((ToolOption)localObject2).process(this);
/*     */         }
/*     */       }
/* 293 */       else if (((String)localObject1).startsWith("-XD"))
/*     */       {
/* 295 */         String str1 = ((String)localObject1).substring("-XD".length());
/* 296 */         int k = str1.indexOf('=');
/* 297 */         String str2 = k < 0 ? str1 : str1.substring(0, k);
/* 298 */         String str3 = k < 0 ? str1 : str1.substring(k + 1);
/* 299 */         this.compOpts.put(str2, str3);
/*     */       }
/* 303 */       else if (((String)localObject1).startsWith("-"))
/*     */       {
/* 305 */         j = this.docletInvoker.optionLength((String)localObject1);
/* 306 */         if (j < 0)
/*     */         {
/* 308 */           exit();
/* 309 */         } else if (j == 0)
/*     */         {
/* 311 */           usageError("main.invalid_flag", new Object[] { localObject1 });
/*     */         }
/*     */         else {
/* 314 */           if (i + j > paramArrayOfString.length) {
/* 315 */             usageError("main.requires_argument", new Object[] { localObject1 });
/*     */           }
/* 317 */           ListBuffer localListBuffer2 = new ListBuffer();
/* 318 */           for (int m = 0; m < j - 1; m++) {
/* 319 */             localListBuffer2.append(paramArrayOfString[(++i)]);
/*     */           }
/* 321 */           setOption((String)localObject1, localListBuffer2.toList());
/*     */         }
/*     */       } else {
/* 324 */         localListBuffer1.append(localObject1);
/*     */       }
/*     */     }
/* 327 */     this.compOpts.notifyListeners();
/*     */ 
/* 329 */     if ((localListBuffer1.isEmpty()) && (this.subPackages.isEmpty()) && (isEmpty(paramIterable))) {
/* 330 */       usageError("main.No_packages_or_classes_specified", new Object[0]);
/*     */     }
/*     */ 
/* 333 */     if (!this.docletInvoker.validOptions(this.options.toList()))
/*     */     {
/* 335 */       exit();
/*     */     }
/*     */ 
/* 338 */     JavadocTool localJavadocTool = JavadocTool.make0(this.context);
/* 339 */     if (localJavadocTool == null) return false;
/*     */ 
/* 341 */     if (this.showAccess == null) {
/* 342 */       setFilter(this.defaultFilter);
/*     */     }
/*     */ 
/* 345 */     Object localObject1 = this.docletInvoker.languageVersion();
/* 346 */     Object localObject2 = localJavadocTool.getRootDocImpl(this.docLocale, this.encoding, this.showAccess, localListBuffer1
/* 350 */       .toList(), this.options
/* 351 */       .toList(), paramIterable, this.breakiterator, this.subPackages
/* 354 */       .toList(), this.excludedPackages
/* 355 */       .toList(), this.docClasses, (localObject1 == null) || (localObject1 == LanguageVersion.JAVA_1_1), this.quiet);
/*     */ 
/* 362 */     localJavadocTool = null;
/*     */ 
/* 365 */     int j = localObject2 != null ? 1 : 0;
/*     */     boolean bool;
/* 366 */     if (j != 0) bool = this.docletInvoker.start((RootDoc)localObject2);
/*     */ 
/* 369 */     if (this.compOpts.get("-verbose") != null) {
/* 370 */       l = System.currentTimeMillis() - l;
/* 371 */       this.messager.notice("main.done_in", new Object[] { Long.toString(l) });
/*     */     }
/*     */ 
/* 374 */     return bool;
/*     */   }
/*     */ 
/*     */   private <T> boolean isEmpty(Iterable<T> paramIterable) {
/* 378 */     return !paramIterable.iterator().hasNext();
/*     */   }
/*     */ 
/*     */   private void setDocletInvoker(Class<?> paramClass, JavaFileManager paramJavaFileManager, String[] paramArrayOfString)
/*     */   {
/* 394 */     if (paramClass != null) {
/* 395 */       this.docletInvoker = new DocletInvoker(this.messager, paramClass, this.apiMode);
/*     */ 
/* 397 */       return;
/*     */     }
/*     */ 
/* 400 */     String str1 = null;
/* 401 */     String str2 = null;
/*     */ 
/* 404 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 405 */       String str3 = paramArrayOfString[i];
/* 406 */       if (str3.equals(ToolOption.DOCLET.opt)) {
/* 407 */         oneArg(paramArrayOfString, i++);
/* 408 */         if (str1 != null) {
/* 409 */           usageError("main.more_than_one_doclet_specified_0_and_1", new Object[] { str1, paramArrayOfString[i] });
/*     */         }
/*     */ 
/* 412 */         str1 = paramArrayOfString[i];
/* 413 */       } else if (str3.equals(ToolOption.DOCLETPATH.opt)) {
/* 414 */         oneArg(paramArrayOfString, i++);
/* 415 */         if (str2 == null)
/* 416 */           str2 = paramArrayOfString[i];
/*     */         else {
/* 418 */           str2 = str2 + File.pathSeparator + paramArrayOfString[i];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 423 */     if (str1 == null) {
/* 424 */       str1 = this.defaultDocletClassName;
/*     */     }
/*     */ 
/* 428 */     this.docletInvoker = new DocletInvoker(this.messager, paramJavaFileManager, str1, str2, this.docletParentClassLoader, this.apiMode);
/*     */   }
/*     */ 
/*     */   private void oneArg(String[] paramArrayOfString, int paramInt)
/*     */   {
/* 439 */     if (paramInt + 1 < paramArrayOfString.length)
/* 440 */       setOption(paramArrayOfString[paramInt], paramArrayOfString[(paramInt + 1)]);
/*     */     else
/* 442 */       usageError("main.requires_argument", new Object[] { paramArrayOfString[paramInt] });
/*     */   }
/*     */ 
/*     */   void usageError(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 448 */     this.messager.error(Messager.NOPOS, paramString, paramArrayOfObject);
/* 449 */     usage(true);
/*     */   }
/*     */ 
/*     */   private void setOption(String paramString)
/*     */   {
/* 456 */     String[] arrayOfString = { paramString };
/* 457 */     this.options.append(arrayOfString);
/*     */   }
/*     */ 
/*     */   private void setOption(String paramString1, String paramString2)
/*     */   {
/* 464 */     String[] arrayOfString = { paramString1, paramString2 };
/* 465 */     this.options.append(arrayOfString);
/*     */   }
/*     */ 
/*     */   private void setOption(String paramString, List<String> paramList)
/*     */   {
/* 472 */     String[] arrayOfString = new String[paramList.length() + 1];
/* 473 */     int i = 0;
/* 474 */     arrayOfString[(i++)] = paramString;
/* 475 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 476 */       arrayOfString[(i++)] = ((String)((List)localObject).head);
/*     */     }
/* 478 */     this.options.append(arrayOfString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.Start
 * JD-Core Version:    0.6.2
 */