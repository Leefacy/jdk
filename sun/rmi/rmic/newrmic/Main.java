/*     */ package sun.rmi.rmic.newrmic;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.rmi.rmic.newrmic.jrmp.JrmpGenerator;
/*     */ import sun.tools.util.CommandLine;
/*     */ 
/*     */ public class Main
/*     */ {
/* 101 */   private static final Object batchCountLock = new Object();
/*     */ 
/* 104 */   private static long batchCount = 0L;
/*     */ 
/* 108 */   private static final Map<Long, Batch> batchTable = Collections.synchronizedMap(new HashMap())
/* 108 */     ;
/*     */   private final PrintStream out;
/*     */   private final String program;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 120 */     Main localMain = new Main(System.err, "rmic");
/* 121 */     System.exit(localMain.compile(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ 
/*     */   public Main(OutputStream paramOutputStream, String paramString)
/*     */   {
/* 129 */     this.out = ((paramOutputStream instanceof PrintStream) ? (PrintStream)paramOutputStream : new PrintStream(paramOutputStream));
/*     */ 
/* 131 */     this.program = paramString;
/*     */   }
/*     */ 
/*     */   public boolean compile(String[] paramArrayOfString)
/*     */   {
/* 144 */     long l1 = System.currentTimeMillis();
/*     */     long l2;
/* 147 */     synchronized (batchCountLock) {
/* 148 */       l2 = batchCount++;
/*     */     }
/*     */ 
/* 152 */     ??? = parseArgs(paramArrayOfString);
/* 153 */     if (??? == null) {
/* 154 */       return false;
/*     */     }
/*     */ 
/*     */     boolean bool;
/*     */     try
/*     */     {
/* 164 */       batchTable.put(Long.valueOf(l2), ???);
/* 165 */       bool = invokeJavadoc((Batch)???, l2);
/*     */     } finally {
/* 167 */       batchTable.remove(Long.valueOf(l2));
/*     */     }
/*     */ 
/* 170 */     if (((Batch)???).verbose) {
/* 171 */       long l3 = System.currentTimeMillis() - l1;
/* 172 */       output(Resources.getText("rmic.done_in", new String[] { 
/* 173 */         Long.toString(l3) }));
/*     */     }
/*     */ 
/* 176 */     return bool;
/*     */   }
/*     */ 
/*     */   public void output(String paramString)
/*     */   {
/* 184 */     this.out.println(paramString);
/*     */   }
/*     */ 
/*     */   public void error(String paramString, String[] paramArrayOfString)
/*     */   {
/* 194 */     output(Resources.getText(paramString, paramArrayOfString));
/*     */   }
/*     */ 
/*     */   public void usage()
/*     */   {
/* 205 */     error("rmic.usage", new String[] { this.program });
/*     */   }
/*     */ 
/*     */   private Batch parseArgs(String[] paramArrayOfString)
/*     */   {
/* 215 */     Batch localBatch = new Batch();
/*     */     try
/*     */     {
/* 221 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 223 */       error("rmic.cant.read", new String[] { localFileNotFoundException.getMessage() });
/* 224 */       return null;
/*     */     } catch (IOException localIOException) {
/* 226 */       localIOException.printStackTrace(this.out);
/* 227 */       return null;
/*     */     }
/*     */ 
/* 230 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/* 232 */       if (paramArrayOfString[i] != null)
/*     */       {
/* 236 */         if (paramArrayOfString[i].equals("-Xnew"))
/*     */         {
/* 238 */           paramArrayOfString[i] = null;
/*     */         } else {
/* 240 */           if (paramArrayOfString[i].equals("-show"))
/*     */           {
/* 242 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[i] });
/* 243 */             usage();
/* 244 */             return null;
/*     */           }
/* 246 */           if (paramArrayOfString[i].equals("-O"))
/*     */           {
/* 248 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[i] });
/* 249 */             paramArrayOfString[i] = null;
/*     */           }
/* 251 */           else if (paramArrayOfString[i].equals("-debug"))
/*     */           {
/* 253 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[i] });
/* 254 */             paramArrayOfString[i] = null;
/*     */           }
/* 256 */           else if (paramArrayOfString[i].equals("-depend"))
/*     */           {
/* 259 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[i] });
/* 260 */             paramArrayOfString[i] = null;
/*     */           }
/* 262 */           else if ((paramArrayOfString[i].equals("-keep")) || 
/* 263 */             (paramArrayOfString[i]
/* 263 */             .equals("-keepgenerated")))
/*     */           {
/* 265 */             localBatch.keepGenerated = true;
/* 266 */             paramArrayOfString[i] = null;
/*     */           }
/* 268 */           else if (paramArrayOfString[i].equals("-g")) {
/* 269 */             localBatch.debug = true;
/* 270 */             paramArrayOfString[i] = null;
/*     */           }
/* 272 */           else if (paramArrayOfString[i].equals("-nowarn")) {
/* 273 */             localBatch.noWarn = true;
/* 274 */             paramArrayOfString[i] = null;
/*     */           }
/* 276 */           else if (paramArrayOfString[i].equals("-nowrite")) {
/* 277 */             localBatch.noWrite = true;
/* 278 */             paramArrayOfString[i] = null;
/*     */           }
/* 280 */           else if (paramArrayOfString[i].equals("-verbose")) {
/* 281 */             localBatch.verbose = true;
/* 282 */             paramArrayOfString[i] = null;
/*     */           }
/* 284 */           else if (paramArrayOfString[i].equals("-Xnocompile")) {
/* 285 */             localBatch.noCompile = true;
/* 286 */             localBatch.keepGenerated = true;
/* 287 */             paramArrayOfString[i] = null;
/*     */           }
/* 289 */           else if (paramArrayOfString[i].equals("-bootclasspath")) {
/* 290 */             if (i + 1 >= paramArrayOfString.length) {
/* 291 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[i] });
/* 292 */               usage();
/* 293 */               return null;
/*     */             }
/* 295 */             if (localBatch.bootClassPath != null) {
/* 296 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[i] });
/* 297 */               usage();
/* 298 */               return null;
/*     */             }
/* 300 */             paramArrayOfString[i] = null;
/* 301 */             localBatch.bootClassPath = paramArrayOfString[(++i)];
/* 302 */             assert (localBatch.bootClassPath != null);
/* 303 */             paramArrayOfString[i] = null;
/*     */           }
/* 305 */           else if (paramArrayOfString[i].equals("-extdirs")) {
/* 306 */             if (i + 1 >= paramArrayOfString.length) {
/* 307 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[i] });
/* 308 */               usage();
/* 309 */               return null;
/*     */             }
/* 311 */             if (localBatch.extDirs != null) {
/* 312 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[i] });
/* 313 */               usage();
/* 314 */               return null;
/*     */             }
/* 316 */             paramArrayOfString[i] = null;
/* 317 */             localBatch.extDirs = paramArrayOfString[(++i)];
/* 318 */             assert (localBatch.extDirs != null);
/* 319 */             paramArrayOfString[i] = null;
/*     */           }
/* 321 */           else if (paramArrayOfString[i].equals("-classpath")) {
/* 322 */             if (i + 1 >= paramArrayOfString.length) {
/* 323 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[i] });
/* 324 */               usage();
/* 325 */               return null;
/*     */             }
/* 327 */             if (localBatch.classPath != null) {
/* 328 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[i] });
/* 329 */               usage();
/* 330 */               return null;
/*     */             }
/* 332 */             paramArrayOfString[i] = null;
/* 333 */             localBatch.classPath = paramArrayOfString[(++i)];
/* 334 */             assert (localBatch.classPath != null);
/* 335 */             paramArrayOfString[i] = null;
/*     */           }
/* 337 */           else if (paramArrayOfString[i].equals("-d")) {
/* 338 */             if (i + 1 >= paramArrayOfString.length) {
/* 339 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[i] });
/* 340 */               usage();
/* 341 */               return null;
/*     */             }
/* 343 */             if (localBatch.destDir != null) {
/* 344 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[i] });
/* 345 */               usage();
/* 346 */               return null;
/*     */             }
/* 348 */             paramArrayOfString[i] = null;
/* 349 */             localBatch.destDir = new File(paramArrayOfString[(++i)]);
/* 350 */             assert (localBatch.destDir != null);
/* 351 */             paramArrayOfString[i] = null;
/* 352 */             if (!localBatch.destDir.exists()) {
/* 353 */               error("rmic.no.such.directory", new String[] { localBatch.destDir.getPath() });
/* 354 */               usage();
/* 355 */               return null;
/*     */             }
/*     */           }
/* 358 */           else if ((paramArrayOfString[i].equals("-v1.1")) || 
/* 359 */             (paramArrayOfString[i]
/* 359 */             .equals("-vcompat")) || 
/* 360 */             (paramArrayOfString[i]
/* 360 */             .equals("-v1.2")))
/*     */           {
/* 362 */             JrmpGenerator localJrmpGenerator = new JrmpGenerator();
/* 363 */             localBatch.generators.add(localJrmpGenerator);
/*     */ 
/* 365 */             if (!localJrmpGenerator.parseArgs(paramArrayOfString, this))
/* 366 */               return null;
/*     */           }
/*     */           else {
/* 369 */             if (paramArrayOfString[i].equalsIgnoreCase("-iiop")) {
/* 370 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[i] });
/* 371 */               return null;
/*     */             }
/*     */ 
/* 385 */             if (paramArrayOfString[i].equalsIgnoreCase("-idl")) {
/* 386 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[i] });
/* 387 */               return null;
/*     */             }
/*     */ 
/* 391 */             if (paramArrayOfString[i].equalsIgnoreCase("-xprint")) {
/* 392 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[i] });
/* 393 */               return null;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 403 */     for (i = 0; i < paramArrayOfString.length; i++) {
/* 404 */       if (paramArrayOfString[i] != null) {
/* 405 */         if (paramArrayOfString[i].startsWith("-")) {
/* 406 */           error("rmic.no.such.option", new String[] { paramArrayOfString[i] });
/* 407 */           usage();
/* 408 */           return null;
/*     */         }
/* 410 */         localBatch.classes.add(paramArrayOfString[i]);
/*     */       }
/*     */     }
/*     */ 
/* 414 */     if (localBatch.classes.isEmpty()) {
/* 415 */       usage();
/* 416 */       return null;
/*     */     }
/*     */ 
/* 423 */     if (localBatch.generators.isEmpty()) {
/* 424 */       localBatch.generators.add(new JrmpGenerator());
/*     */     }
/* 426 */     return localBatch;
/*     */   }
/*     */ 
/*     */   public static boolean start(RootDoc paramRootDoc)
/*     */   {
/* 438 */     long l = -1L;
/* 439 */     for (localIterator1 : paramRootDoc.options()) {
/* 440 */       if (localIterator1[0].equals("-batchID")) {
/*     */         try {
/* 442 */           l = Long.parseLong(localIterator1[1]);
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 444 */           throw new AssertionError(localNumberFormatException);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 448 */     ??? = (Batch)batchTable.get(Long.valueOf(l));
/* 449 */     assert (??? != null);
/*     */     BatchEnvironment localBatchEnvironment;
/*     */     try
/*     */     {
/* 458 */       Constructor localConstructor = ((Batch)???).envClass
/* 458 */         .getConstructor(new Class[] { RootDoc.class });
/*     */ 
/* 459 */       localBatchEnvironment = (BatchEnvironment)localConstructor.newInstance(new Object[] { paramRootDoc });
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 461 */       throw new AssertionError(localNoSuchMethodException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 463 */       throw new AssertionError(localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 465 */       throw new AssertionError(localInstantiationException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 467 */       throw new AssertionError(localInvocationTargetException);
/*     */     }
/*     */ 
/* 470 */     localBatchEnvironment.setVerbose(((Batch)???).verbose);
/*     */ 
/* 478 */     File localFile1 = ((Batch)???).destDir;
/* 479 */     if (localFile1 == null) {
/* 480 */       localFile1 = new File(System.getProperty("user.dir"));
/*     */     }
/*     */ 
/* 486 */     for (Iterator localIterator1 = ((Batch)???).classes.iterator(); localIterator1.hasNext(); ) { localObject2 = (String)localIterator1.next();
/* 487 */       localObject3 = paramRootDoc.classNamed((String)localObject2);
/*     */       try {
/* 489 */         for (Generator localGenerator : ((Batch)???).generators)
/* 490 */           localGenerator.generate(localBatchEnvironment, (ClassDoc)localObject3, localFile1);
/*     */       }
/*     */       catch (NullPointerException localNullPointerException)
/*     */       {
/*     */       }
/*     */     }
/*     */     Object localObject3;
/* 509 */     boolean bool = true;
/* 510 */     Object localObject2 = localBatchEnvironment.generatedFiles();
/* 511 */     if ((!((Batch)???).noCompile) && (!((Batch)???).noWrite) && (!((List)localObject2).isEmpty())) {
/* 512 */       bool = ((Batch)???).enclosingMain().invokeJavac((Batch)???, (List)localObject2);
/*     */     }
/*     */ 
/* 518 */     if (!((Batch)???).keepGenerated) {
/* 519 */       for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { File localFile2 = (File)((Iterator)localObject3).next();
/* 520 */         localFile2.delete();
/*     */       }
/*     */     }
/*     */ 
/* 524 */     return bool;
/*     */   }
/*     */ 
/*     */   public static int optionLength(String paramString)
/*     */   {
/* 534 */     if (paramString.equals("-batchID")) {
/* 535 */       return 2;
/*     */     }
/* 537 */     return 0;
/*     */   }
/*     */ 
/*     */   private boolean invokeJavadoc(Batch paramBatch, long paramLong)
/*     */   {
/* 551 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 554 */     localArrayList.add("-private");
/*     */ 
/* 557 */     localArrayList.add("-Xclasses");
/*     */ 
/* 560 */     if (paramBatch.verbose) {
/* 561 */       localArrayList.add("-verbose");
/*     */     }
/* 563 */     if (paramBatch.bootClassPath != null) {
/* 564 */       localArrayList.add("-bootclasspath");
/* 565 */       localArrayList.add(paramBatch.bootClassPath);
/*     */     }
/* 567 */     if (paramBatch.extDirs != null) {
/* 568 */       localArrayList.add("-extdirs");
/* 569 */       localArrayList.add(paramBatch.extDirs);
/*     */     }
/* 571 */     if (paramBatch.classPath != null) {
/* 572 */       localArrayList.add("-classpath");
/* 573 */       localArrayList.add(paramBatch.classPath);
/*     */     }
/*     */ 
/* 577 */     localArrayList.add("-batchID");
/* 578 */     localArrayList.add(Long.toString(paramLong));
/*     */ 
/* 585 */     HashSet localHashSet = new HashSet();
/* 586 */     for (Iterator localIterator = paramBatch.generators.iterator(); localIterator.hasNext(); ) { localObject = (Generator)localIterator.next();
/* 587 */       localHashSet.addAll(((Generator)localObject).bootstrapClassNames());
/*     */     }
/* 590 */     Object localObject;
/* 589 */     localHashSet.addAll(paramBatch.classes);
/* 590 */     for (localIterator = localHashSet.iterator(); localIterator.hasNext(); ) { localObject = (String)localIterator.next();
/* 591 */       localArrayList.add(localObject);
/*     */     }
/*     */ 
/* 595 */     int i = com.sun.tools.javadoc.Main.execute(this.program, new PrintWriter(this.out, true), new PrintWriter(this.out, true), new PrintWriter(this.out, true), 
/* 600 */       getClass().getName(), 
/* 601 */       (String[])localArrayList
/* 601 */       .toArray(new String[localArrayList
/* 601 */       .size()]));
/* 602 */     return i == 0;
/*     */   }
/*     */ 
/*     */   private boolean invokeJavac(Batch paramBatch, List<File> paramList)
/*     */   {
/* 615 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 618 */     localArrayList.add("-nowarn");
/*     */ 
/* 621 */     if (paramBatch.debug) {
/* 622 */       localArrayList.add("-g");
/*     */     }
/* 624 */     if (paramBatch.verbose) {
/* 625 */       localArrayList.add("-verbose");
/*     */     }
/* 627 */     if (paramBatch.bootClassPath != null) {
/* 628 */       localArrayList.add("-bootclasspath");
/* 629 */       localArrayList.add(paramBatch.bootClassPath);
/*     */     }
/* 631 */     if (paramBatch.extDirs != null) {
/* 632 */       localArrayList.add("-extdirs");
/* 633 */       localArrayList.add(paramBatch.extDirs);
/*     */     }
/* 635 */     if (paramBatch.classPath != null) {
/* 636 */       localArrayList.add("-classpath");
/* 637 */       localArrayList.add(paramBatch.classPath);
/*     */     }
/*     */ 
/* 644 */     localArrayList.add("-source");
/* 645 */     localArrayList.add("1.3");
/* 646 */     localArrayList.add("-target");
/* 647 */     localArrayList.add("1.1");
/*     */ 
/* 650 */     for (File localFile : paramList) {
/* 651 */       localArrayList.add(localFile.getPath());
/*     */     }
/*     */ 
/* 655 */     int i = com.sun.tools.javac.Main.compile(
/* 656 */       (String[])localArrayList
/* 656 */       .toArray(new String[localArrayList
/* 656 */       .size()]), new PrintWriter(this.out, true));
/*     */ 
/* 658 */     return i == 0;
/*     */   }
/*     */ 
/*     */   private class Batch
/*     */   {
/* 666 */     boolean keepGenerated = false;
/* 667 */     boolean debug = false;
/* 668 */     boolean noWarn = false;
/* 669 */     boolean noWrite = false;
/* 670 */     boolean verbose = false;
/* 671 */     boolean noCompile = false;
/* 672 */     String bootClassPath = null;
/* 673 */     String extDirs = null;
/* 674 */     String classPath = null;
/* 675 */     File destDir = null;
/* 676 */     List<Generator> generators = new ArrayList();
/* 677 */     Class<? extends BatchEnvironment> envClass = BatchEnvironment.class;
/* 678 */     List<String> classes = new ArrayList();
/*     */ 
/*     */     Batch()
/*     */     {
/*     */     }
/*     */ 
/*     */     Main enclosingMain()
/*     */     {
/* 686 */       return Main.this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.Main
 * JD-Core Version:    0.6.2
 */