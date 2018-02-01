/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.Plugin;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.api.BasicJavacTask;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.file.CacheFSInfo;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.processing.AnnotationProcessingError;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.FatalError;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Log.PrefixKind;
/*     */ import com.sun.tools.javac.util.Log.WriterKind;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.PropagatedException;
/*     */ import com.sun.tools.javac.util.ServiceLoader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.Processor;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   String ownName;
/*     */   PrintWriter out;
/*     */   public Log log;
/*     */   boolean apiMode;
/* 110 */   private Option[] recognizedOptions = (Option[])Option.getJavaCompilerOptions().toArray(new Option[0]);
/*     */ 
/* 112 */   private OptionHelper optionHelper = new OptionHelper()
/*     */   {
/*     */     public String get(Option paramAnonymousOption) {
/* 115 */       return Main.this.options.get(paramAnonymousOption);
/*     */     }
/*     */ 
/*     */     public void put(String paramAnonymousString1, String paramAnonymousString2)
/*     */     {
/* 120 */       Main.this.options.put(paramAnonymousString1, paramAnonymousString2);
/*     */     }
/*     */ 
/*     */     public void remove(String paramAnonymousString)
/*     */     {
/* 125 */       Main.this.options.remove(paramAnonymousString);
/*     */     }
/*     */ 
/*     */     public Log getLog()
/*     */     {
/* 130 */       return Main.this.log;
/*     */     }
/*     */ 
/*     */     public String getOwnName()
/*     */     {
/* 135 */       return Main.this.ownName;
/*     */     }
/*     */ 
/*     */     public void error(String paramAnonymousString, Object[] paramAnonymousArrayOfObject)
/*     */     {
/* 140 */       Main.this.error(paramAnonymousString, paramAnonymousArrayOfObject);
/*     */     }
/*     */ 
/*     */     public void addFile(File paramAnonymousFile)
/*     */     {
/* 145 */       Main.this.filenames.add(paramAnonymousFile);
/*     */     }
/*     */ 
/*     */     public void addClassName(String paramAnonymousString)
/*     */     {
/* 150 */       Main.this.classnames.append(paramAnonymousString); }  } ;
/*     */ 
/* 171 */   private Options options = null;
/*     */ 
/* 175 */   public Set<File> filenames = null;
/*     */ 
/* 179 */   public ListBuffer<String> classnames = null;
/*     */   private JavaFileManager fileManager;
/*     */   public static final String javacBundleName = "com.sun.tools.javac.resources.javac";
/*     */ 
/* 159 */   public Main(String paramString) { this(paramString, new PrintWriter(System.err, true)); }
/*     */ 
/*     */ 
/*     */   public Main(String paramString, PrintWriter paramPrintWriter)
/*     */   {
/* 166 */     this.ownName = paramString;
/* 167 */     this.out = paramPrintWriter;
/*     */   }
/*     */ 
/*     */   void error(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 184 */     if (this.apiMode) {
/* 185 */       String str = this.log.localize(Log.PrefixKind.JAVAC, paramString, paramArrayOfObject);
/* 186 */       throw new PropagatedException(new IllegalStateException(str));
/*     */     }
/* 188 */     warning(paramString, paramArrayOfObject);
/* 189 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.usage", new Object[] { this.ownName });
/*     */   }
/*     */ 
/*     */   void warning(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 195 */     this.log.printRawLines(this.ownName + ": " + this.log.localize(Log.PrefixKind.JAVAC, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public Option getOption(String paramString) {
/* 199 */     for (Option localOption : this.recognizedOptions) {
/* 200 */       if (localOption.matches(paramString))
/* 201 */         return localOption;
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public void setOptions(Options paramOptions) {
/* 207 */     if (paramOptions == null)
/* 208 */       throw new NullPointerException();
/* 209 */     this.options = paramOptions;
/*     */   }
/*     */ 
/*     */   public void setAPIMode(boolean paramBoolean) {
/* 213 */     this.apiMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Collection<File> processArgs(String[] paramArrayOfString)
/*     */   {
/* 221 */     return processArgs(paramArrayOfString, null);
/*     */   }
/*     */ 
/*     */   public Collection<File> processArgs(String[] paramArrayOfString1, String[] paramArrayOfString2) {
/* 225 */     int i = 0;
/* 226 */     while (i < paramArrayOfString1.length) {
/* 227 */       str1 = paramArrayOfString1[i];
/* 228 */       i++;
/*     */ 
/* 230 */       localObject1 = null;
/*     */ 
/* 232 */       if (str1.length() > 0)
/*     */       {
/* 236 */         int j = str1.charAt(0) == '-' ? 0 : this.recognizedOptions.length - 1;
/* 237 */         for (int k = j; k < this.recognizedOptions.length; k++) {
/* 238 */           if (this.recognizedOptions[k].matches(str1)) {
/* 239 */             localObject1 = this.recognizedOptions[k];
/* 240 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 245 */       if (localObject1 == null) {
/* 246 */         error("err.invalid.flag", new Object[] { str1 });
/* 247 */         return null;
/*     */       }
/*     */ 
/* 250 */       if (((Option)localObject1).hasArg()) {
/* 251 */         if (i == paramArrayOfString1.length) {
/* 252 */           error("err.req.arg", new Object[] { str1 });
/* 253 */           return null;
/*     */         }
/* 255 */         str2 = paramArrayOfString1[i];
/* 256 */         i++;
/* 257 */         if (((Option)localObject1).process(this.optionHelper, str1, str2))
/* 258 */           return null;
/*     */       }
/* 260 */       else if (((Option)localObject1).process(this.optionHelper, str1)) {
/* 261 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 265 */     if ((this.options.get(Option.PROFILE) != null) && (this.options.get(Option.BOOTCLASSPATH) != null)) {
/* 266 */       error("err.profile.bootclasspath.conflict", new Object[0]);
/* 267 */       return null;
/*     */     }
/*     */ 
/* 270 */     if ((this.classnames != null) && (paramArrayOfString2 != null)) {
/* 271 */       this.classnames.addAll(Arrays.asList(paramArrayOfString2));
/*     */     }
/*     */ 
/* 274 */     if (!checkDirectory(Option.D))
/* 275 */       return null;
/* 276 */     if (!checkDirectory(Option.S)) {
/* 277 */       return null;
/*     */     }
/* 279 */     String str1 = this.options.get(Option.SOURCE);
/*     */ 
/* 281 */     Object localObject1 = str1 != null ? 
/* 281 */       Source.lookup(str1) : 
/* 281 */       Source.DEFAULT;
/*     */ 
/* 283 */     String str2 = this.options.get(Option.TARGET);
/*     */ 
/* 285 */     Target localTarget = str2 != null ? 
/* 285 */       Target.lookup(str2) : 
/* 285 */       Target.DEFAULT;
/*     */ 
/* 292 */     if (Character.isDigit(localTarget.name.charAt(0))) {
/* 293 */       if (localTarget.compareTo(((Source)localObject1).requiredTarget()) < 0) {
/* 294 */         if (str2 != null) {
/* 295 */           if (str1 == null)
/* 296 */             warning("warn.target.default.source.conflict", new Object[] { str2, ((Source)localObject1)
/* 298 */               .requiredTarget().name });
/*     */           else {
/* 300 */             warning("warn.source.target.conflict", new Object[] { str1, ((Source)localObject1)
/* 302 */               .requiredTarget().name });
/*     */           }
/* 304 */           return null;
/*     */         }
/* 306 */         localTarget = ((Source)localObject1).requiredTarget();
/* 307 */         this.options.put("-target", localTarget.name);
/*     */       }
/* 310 */       else if ((str2 == null) && (!((Source)localObject1).allowGenerics())) {
/* 311 */         localTarget = Target.JDK1_4;
/* 312 */         this.options.put("-target", localTarget.name);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 317 */     String str3 = this.options.get(Option.PROFILE);
/* 318 */     if (str3 != null) {
/* 319 */       localObject2 = Profile.lookup(str3);
/* 320 */       if (!((Profile)localObject2).isValid(localTarget)) {
/* 321 */         warning("warn.profile.target.conflict", new Object[] { str3, localTarget.name });
/* 322 */         return null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 327 */     Object localObject2 = this.options.get("showClass");
/* 328 */     if (localObject2 != null) {
/* 329 */       if (((String)localObject2).equals("showClass"))
/* 330 */         localObject2 = "com.sun.tools.javac.Main";
/* 331 */       showClass((String)localObject2);
/*     */     }
/*     */ 
/* 334 */     this.options.notifyListeners();
/*     */ 
/* 336 */     return this.filenames;
/*     */   }
/*     */ 
/*     */   private boolean checkDirectory(Option paramOption) {
/* 340 */     String str = this.options.get(paramOption);
/* 341 */     if (str == null)
/* 342 */       return true;
/* 343 */     File localFile = new File(str);
/* 344 */     if (!localFile.exists()) {
/* 345 */       error("err.dir.not.found", new Object[] { str });
/* 346 */       return false;
/*     */     }
/* 348 */     if (!localFile.isDirectory()) {
/* 349 */       error("err.file.not.directory", new Object[] { str });
/* 350 */       return false;
/*     */     }
/* 352 */     return true;
/*     */   }
/*     */ 
/*     */   public Result compile(String[] paramArrayOfString)
/*     */   {
/* 359 */     Context localContext = new Context();
/* 360 */     JavacFileManager.preRegister(localContext);
/* 361 */     Result localResult = compile(paramArrayOfString, localContext);
/* 362 */     if ((this.fileManager instanceof JavacFileManager))
/*     */     {
/* 364 */       ((JavacFileManager)this.fileManager).close();
/*     */     }
/* 366 */     return localResult;
/*     */   }
/*     */ 
/*     */   public Result compile(String[] paramArrayOfString, Context paramContext) {
/* 370 */     return compile(paramArrayOfString, paramContext, List.nil(), null);
/*     */   }
/*     */ 
/*     */   public Result compile(String[] paramArrayOfString, Context paramContext, List<JavaFileObject> paramList, Iterable<? extends Processor> paramIterable)
/*     */   {
/* 381 */     return compile(paramArrayOfString, null, paramContext, paramList, paramIterable);
/*     */   }
/*     */ 
/*     */   public Result compile(String[] paramArrayOfString1, String[] paramArrayOfString2, Context paramContext, List<JavaFileObject> paramList, Iterable<? extends Processor> paramIterable)
/*     */   {
/* 390 */     paramContext.put(Log.outKey, this.out);
/* 391 */     this.log = Log.instance(paramContext);
/*     */ 
/* 393 */     if (this.options == null) {
/* 394 */       this.options = Options.instance(paramContext);
/*     */     }
/* 396 */     this.filenames = new LinkedHashSet();
/* 397 */     this.classnames = new ListBuffer();
/* 398 */     JavaCompiler localJavaCompiler = null;
/*     */     try
/*     */     {
/*     */       Object localObject1;
/* 405 */       if ((paramArrayOfString1.length == 0) && ((paramArrayOfString2 == null) || (paramArrayOfString2.length == 0)))
/*     */       {
/* 407 */         if (paramList
/* 407 */           .isEmpty()) {
/* 408 */           Option.HELP.process(this.optionHelper, "-help");
/* 409 */           return Result.CMDERR;
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 414 */         localObject1 = processArgs(CommandLine.parse(paramArrayOfString1), paramArrayOfString2);
/*     */         Result localResult1;
/* 415 */         if (localObject1 == null)
/*     */         {
/* 417 */           return Result.CMDERR;
/* 418 */         }if ((((Collection)localObject1).isEmpty()) && (paramList.isEmpty()) && (this.classnames.isEmpty()))
/*     */         {
/* 420 */           if ((this.options.isSet(Option.HELP)) || 
/* 421 */             (this.options
/* 421 */             .isSet(Option.X)) || 
/* 422 */             (this.options
/* 422 */             .isSet(Option.VERSION)) || 
/* 423 */             (this.options
/* 423 */             .isSet(Option.FULLVERSION)))
/*     */           {
/* 424 */             return Result.OK;
/* 425 */           }if (JavaCompiler.explicitAnnotationProcessingRequested(this.options))
/* 426 */             error("err.no.source.files.classes", new Object[0]);
/*     */           else {
/* 428 */             error("err.no.source.files", new Object[0]);
/*     */           }
/* 430 */           return Result.CMDERR;
/*     */         }
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/* 433 */         warning("err.file.not.found", new Object[] { localFileNotFoundException.getMessage() });
/* 434 */         return Result.SYSERR;
/*     */       }
/*     */ 
/* 437 */       boolean bool = this.options.isSet("stdout");
/* 438 */       if (bool) {
/* 439 */         this.log.flush();
/* 440 */         this.log.setWriters(new PrintWriter(System.out, true));
/*     */       }
/*     */ 
/* 445 */       int i = (this.options.isUnset("nonBatchMode")) && 
/* 445 */         (System.getProperty("nonBatchMode") == null) ? 
/* 445 */         1 : 0;
/* 446 */       if (i != 0) {
/* 447 */         CacheFSInfo.preRegister(paramContext);
/*     */       }
/*     */ 
/* 451 */       String str = this.options.get(Option.PLUGIN);
/*     */       Object localObject4;
/*     */       Object localObject5;
/*     */       Object localObject8;
/*     */       Object localObject7;
/* 452 */       if (str != null) {
/* 453 */         localObject2 = JavacProcessingEnvironment.instance(paramContext);
/* 454 */         localObject3 = ((JavacProcessingEnvironment)localObject2).getProcessorClassLoader();
/* 455 */         localObject4 = ServiceLoader.load(Plugin.class, (ClassLoader)localObject3);
/* 456 */         localObject5 = new LinkedHashSet();
/* 457 */         for (localObject8 : str.split("\\x00")) {
/* 458 */           ((Set)localObject5).add(List.from(((String)localObject8).split("\\s+")));
/*     */         }
/* 460 */         ??? = null;
/* 461 */         Iterator localIterator2 = ((ServiceLoader)localObject4).iterator();
/* 462 */         while (localIterator2.hasNext()) {
/* 463 */           localObject7 = (Plugin)localIterator2.next();
/* 464 */           for (localObject8 = ((Set)localObject5).iterator(); ((Iterator)localObject8).hasNext(); ) { List localList = (List)((Iterator)localObject8).next();
/* 465 */             if (((Plugin)localObject7).getName().equals(localList.head)) {
/* 466 */               ((Set)localObject5).remove(localList);
/*     */               try {
/* 468 */                 if (??? == null)
/* 469 */                   ??? = JavacTask.instance((ProcessingEnvironment)localObject2);
/* 470 */                 ((Plugin)localObject7).init((JavacTask)???, (String[])localList.tail.toArray(new String[localList.tail.size()]));
/*     */               } catch (Throwable localThrowable2) {
/* 472 */                 if (this.apiMode)
/* 473 */                   throw new RuntimeException(localThrowable2);
/* 474 */                 pluginMessage(localThrowable2);
/* 475 */                 return Result.SYSERR;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 480 */         for (localObject7 = ((Set)localObject5).iterator(); ((Iterator)localObject7).hasNext(); ) { localObject8 = (List)((Iterator)localObject7).next();
/* 481 */           this.log.printLines(Log.PrefixKind.JAVAC, "msg.plugin.not.found", new Object[] { ((List)localObject8).head });
/*     */         }
/*     */       }
/*     */ 
/* 485 */       localJavaCompiler = JavaCompiler.instance(paramContext);
/*     */ 
/* 488 */       Object localObject2 = this.options.get(Option.XDOCLINT);
/* 489 */       Object localObject3 = this.options.get(Option.XDOCLINT_CUSTOM);
/* 490 */       if ((localObject2 != null) || (localObject3 != null)) {
/* 491 */         localObject4 = new LinkedHashSet();
/* 492 */         if (localObject2 != null)
/* 493 */           ((Set)localObject4).add("-Xmsgs");
/* 494 */         if (localObject3 != null) {
/* 495 */           for (localObject7 : ((String)localObject3).split("\\s+"))
/* 496 */             if (!((String)localObject7).isEmpty())
/*     */             {
/* 498 */               ((Set)localObject4).add(((String)localObject7).replace(Option.XDOCLINT_CUSTOM.text, "-Xmsgs:"));
/*     */             }
/*     */         }
/* 501 */         if ((((Set)localObject4).size() != 1) || 
/* 502 */           (!((String)((Set)localObject4)
/* 502 */           .iterator().next()).equals("-Xmsgs:none"))) {
/* 503 */           localObject5 = BasicJavacTask.instance(paramContext);
/*     */ 
/* 505 */           ((Set)localObject4).add("-XimplicitHeaders:2");
/* 506 */           new DocLint().init((JavacTask)localObject5, (String[])((Set)localObject4).toArray(new String[((Set)localObject4).size()]));
/* 507 */           localJavaCompiler.keepComments = true;
/*     */         }
/*     */       }
/*     */ 
/* 511 */       this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*     */       Iterator localIterator1;
/*     */       JavaFileObject localJavaFileObject;
/* 513 */       if (!((Collection)localObject1).isEmpty())
/*     */       {
/* 515 */         localJavaCompiler = JavaCompiler.instance(paramContext);
/* 516 */         localObject4 = List.nil();
/* 517 */         localObject5 = (JavacFileManager)this.fileManager;
/* 518 */         for (localIterator1 = ((JavacFileManager)localObject5).getJavaFileObjectsFromFiles((Iterable)localObject1).iterator(); localIterator1.hasNext(); ) { localJavaFileObject = (JavaFileObject)localIterator1.next();
/* 519 */           localObject4 = ((List)localObject4).prepend(localJavaFileObject); }
/* 520 */         for (localIterator1 = ((List)localObject4).iterator(); localIterator1.hasNext(); ) { localJavaFileObject = (JavaFileObject)localIterator1.next();
/* 521 */           paramList = paramList.prepend(localJavaFileObject); }
/*     */       }
/* 523 */       localJavaCompiler.compile(paramList, this.classnames
/* 524 */         .toList(), paramIterable);
/*     */ 
/* 527 */       if (this.log.expectDiagKeys != null) {
/* 528 */         if (this.log.expectDiagKeys.isEmpty()) {
/* 529 */           this.log.printRawLines("all expected diagnostics found");
/* 530 */           return Result.OK;
/*     */         }
/* 532 */         this.log.printRawLines("expected diagnostic keys not found: " + this.log.expectDiagKeys);
/* 533 */         return Result.ERROR;
/*     */       }
/*     */ 
/* 537 */       if (localJavaCompiler.errorCount() != 0)
/* 538 */         return Result.ERROR;
/*     */     } catch (IOException localIOException) {
/* 540 */       ioMessage(localIOException);
/* 541 */       return Result.SYSERR;
/*     */     } catch (OutOfMemoryError localOutOfMemoryError) {
/* 543 */       resourceMessage(localOutOfMemoryError);
/* 544 */       return Result.SYSERR;
/*     */     } catch (StackOverflowError localStackOverflowError) {
/* 546 */       resourceMessage(localStackOverflowError);
/* 547 */       return Result.SYSERR;
/*     */     } catch (FatalError localFatalError) {
/* 549 */       feMessage(localFatalError);
/* 550 */       return Result.SYSERR;
/*     */     } catch (AnnotationProcessingError localAnnotationProcessingError) {
/* 552 */       if (this.apiMode)
/* 553 */         throw new RuntimeException(localAnnotationProcessingError.getCause());
/* 554 */       apMessage(localAnnotationProcessingError);
/* 555 */       return Result.SYSERR;
/*     */     }
/*     */     catch (ClientCodeException localClientCodeException2)
/*     */     {
/* 559 */       throw new RuntimeException(localClientCodeException2.getCause());
/*     */     } catch (PropagatedException localPropagatedException) {
/* 561 */       throw localPropagatedException.getCause();
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/*     */       Result localResult2;
/* 566 */       if ((localJavaCompiler == null) || (localJavaCompiler.errorCount() == 0) || (this.options == null) || 
/* 567 */         (this.options
/* 567 */         .isSet("dev")))
/*     */       {
/* 568 */         bugMessage(localThrowable1);
/* 569 */       }return Result.ABNORMAL;
/*     */     } finally {
/* 571 */       if (localJavaCompiler != null) {
/*     */         try {
/* 573 */           localJavaCompiler.close();
/*     */         } catch (ClientCodeException localClientCodeException18) {
/* 575 */           throw new RuntimeException(localClientCodeException18.getCause());
/*     */         }
/*     */       }
/* 578 */       this.filenames = null;
/* 579 */       this.options = null;
/*     */     }
/* 581 */     return Result.OK;
/*     */   }
/*     */ 
/*     */   void bugMessage(Throwable paramThrowable)
/*     */   {
/* 587 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.bug", new Object[] { JavaCompiler.version() });
/* 588 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void feMessage(Throwable paramThrowable)
/*     */   {
/* 594 */     this.log.printRawLines(paramThrowable.getMessage());
/* 595 */     if ((paramThrowable.getCause() != null) && (this.options.isSet("dev")))
/* 596 */       paramThrowable.getCause().printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void ioMessage(Throwable paramThrowable)
/*     */   {
/* 603 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.io", new Object[0]);
/* 604 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void resourceMessage(Throwable paramThrowable)
/*     */   {
/* 610 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.resource", new Object[0]);
/* 611 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void apMessage(AnnotationProcessingError paramAnnotationProcessingError)
/*     */   {
/* 618 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.proc.annotation.uncaught.exception", new Object[0]);
/* 619 */     paramAnnotationProcessingError.getCause().printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void pluginMessage(Throwable paramThrowable)
/*     */   {
/* 626 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.plugin.uncaught.exception", new Object[0]);
/* 627 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   void showClass(String paramString)
/*     */   {
/* 632 */     PrintWriter localPrintWriter = this.log.getWriter(Log.WriterKind.NOTICE);
/* 633 */     localPrintWriter.println("javac: show class: " + paramString);
/* 634 */     URL localURL = getClass().getResource('/' + paramString.replace('.', '/') + ".class");
/* 635 */     if (localURL == null) {
/* 636 */       localPrintWriter.println("  class not found");
/*     */     } else {
/* 638 */       localPrintWriter.println("  " + localURL);
/*     */       try { MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/* 643 */         DigestInputStream localDigestInputStream = new DigestInputStream(localURL.openStream(), localMessageDigest);
/*     */         byte[] arrayOfByte;
/*     */         try { localObject1 = new byte[8192];
/*     */           int i;
/*     */           do i = localDigestInputStream.read((byte[])localObject1); while (i > 0);
/* 648 */           arrayOfByte = localMessageDigest.digest();
/*     */         } finally {
/* 650 */           localDigestInputStream.close();
/*     */         }
/* 652 */         Object localObject1 = new StringBuilder();
/* 653 */         for (byte b : arrayOfByte)
/* 654 */           ((StringBuilder)localObject1).append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
/* 655 */         localPrintWriter.println("  MD5 checksum: " + localObject1);
/*     */       } catch (Exception localException) {
/* 657 */         localPrintWriter.println("  cannot compute digest: " + localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Result
/*     */   {
/*  92 */     OK(0), 
/*  93 */     ERROR(1), 
/*  94 */     CMDERR(2), 
/*  95 */     SYSERR(3), 
/*  96 */     ABNORMAL(4);
/*     */ 
/*     */     public final int exitCode;
/*     */ 
/*  99 */     private Result(int paramInt) { this.exitCode = paramInt; }
/*     */ 
/*     */     public boolean isOK()
/*     */     {
/* 103 */       return this.exitCode == 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.main.Main
 * JD-Core Version:    0.6.2
 */