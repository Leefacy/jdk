/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.util.CommandLine;
/*     */ 
/*     */ @Deprecated
/*     */ public class Main
/*     */   implements Constants
/*     */ {
/*     */   String program;
/*     */   OutputStream out;
/*     */   public static final int EXIT_OK = 0;
/*     */   public static final int EXIT_ERROR = 1;
/*     */   public static final int EXIT_CMDERR = 2;
/*     */   public static final int EXIT_SYSERR = 3;
/*     */   public static final int EXIT_ABNORMAL = 4;
/*     */   private int exitStatus;
/*     */   private static ResourceBundle messageRB;
/* 188 */   private static final String[] releases = { "1.1", "1.2", "1.3", "1.4" };
/* 189 */   private static final short[] majorVersions = { 45, 46, 47, 48 };
/* 190 */   private static final short[] minorVersions = { 3, 0, 0, 0 };
/*     */ 
/*     */   public Main(OutputStream paramOutputStream, String paramString)
/*     */   {
/*  66 */     this.out = paramOutputStream;
/*  67 */     this.program = paramString;
/*     */   }
/*     */ 
/*     */   public int getExitStatus()
/*     */   {
/*  86 */     return this.exitStatus;
/*     */   }
/*     */ 
/*     */   public boolean compilationPerformedSuccessfully() {
/*  90 */     return (this.exitStatus == 0) || (this.exitStatus == 1);
/*     */   }
/*     */ 
/*     */   public boolean compilationReportedErrors() {
/*  94 */     return this.exitStatus != 0;
/*     */   }
/*     */ 
/*     */   private void output(String paramString)
/*     */   {
/* 101 */     PrintStream localPrintStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*     */ 
/* 104 */     localPrintStream.println(paramString);
/*     */   }
/*     */ 
/*     */   private void error(String paramString)
/*     */   {
/* 112 */     this.exitStatus = 2;
/* 113 */     output(getText(paramString));
/*     */   }
/*     */ 
/*     */   private void error(String paramString1, String paramString2) {
/* 117 */     this.exitStatus = 2;
/* 118 */     output(getText(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   private void error(String paramString1, String paramString2, String paramString3) {
/* 122 */     this.exitStatus = 2;
/* 123 */     output(getText(paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */   public void usage_error()
/*     */   {
/* 132 */     error("main.usage", this.program);
/*     */   }
/*     */ 
/*     */   static void initResource()
/*     */   {
/*     */     try
/*     */     {
/* 143 */       messageRB = ResourceBundle.getBundle("sun.tools.javac.resources.javac");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/* 145 */       throw new Error("Fatal: Resource for javac is missing");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString)
/*     */   {
/* 153 */     return getText(paramString, (String)null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString, int paramInt) {
/* 157 */     return getText(paramString, Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2) {
/* 161 */     return getText(paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2, String paramString3) {
/* 165 */     return getText(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 170 */     if (messageRB == null)
/* 171 */       initResource();
/*     */     try
/*     */     {
/* 174 */       String str1 = messageRB.getString(paramString1);
/* 175 */       return MessageFormat.format(str1, new Object[] { paramString2, paramString3, paramString4 });
/*     */     } catch (MissingResourceException localMissingResourceException) {
/* 177 */       if (paramString2 == null) paramString2 = "null";
/* 178 */       if (paramString3 == null) paramString3 = "null";
/* 179 */       if (paramString4 == null) paramString4 = "null";
/* 180 */       String str2 = "JAVAC MESSAGE FILE IS BROKEN: key={0}, arguments={1}, {2}, {3}";
/* 181 */       return MessageFormat.format(str2, new Object[] { paramString1, paramString2, paramString3, paramString4 });
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean compile(String[] paramArrayOfString)
/*     */   {
/* 196 */     String str1 = null;
/* 197 */     String str2 = null;
/* 198 */     String str3 = null;
/* 199 */     String str4 = null;
/* 200 */     int i = 0;
/*     */ 
/* 202 */     String str5 = null;
/* 203 */     short s1 = 45;
/* 204 */     short s2 = 3;
/*     */ 
/* 206 */     File localFile1 = null;
/*     */ 
/* 208 */     File localFile2 = null;
/* 209 */     String str6 = "-Xjcov";
/* 210 */     String str7 = "-Xjcov:file=";
/*     */ 
/* 212 */     int j = 266244;
/* 213 */     long l = System.currentTimeMillis();
/* 214 */     Vector localVector = new Vector();
/* 215 */     int k = 0;
/* 216 */     Object localObject1 = null;
/* 217 */     String str8 = null;
/*     */ 
/* 221 */     String str9 = null;
/* 222 */     String str10 = null;
/*     */ 
/* 224 */     this.exitStatus = 0;
/*     */     try
/*     */     {
/* 228 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/*     */     } catch (FileNotFoundException localFileNotFoundException1) {
/* 230 */       error("javac.err.cant.read", localFileNotFoundException1.getMessage());
/* 231 */       System.exit(1);
/*     */     } catch (IOException localIOException1) {
/* 233 */       localIOException1.printStackTrace();
/* 234 */       System.exit(1);
/*     */     }
/*     */ 
/* 238 */     for (int m = 0; m < paramArrayOfString.length; m++)
/* 239 */       if (paramArrayOfString[m].equals("-g")) {
/* 240 */         if ((str9 != null) && (!str9.equals("-g")))
/* 241 */           error("main.conflicting.options", str9, "-g");
/* 242 */         str9 = "-g";
/* 243 */         j |= 4096;
/* 244 */         j |= 8192;
/* 245 */         j |= 262144;
/* 246 */       } else if (paramArrayOfString[m].equals("-g:none")) {
/* 247 */         if ((str9 != null) && (!str9.equals("-g:none")))
/* 248 */           error("main.conflicting.options", str9, "-g:none");
/* 249 */         str9 = "-g:none";
/* 250 */         j &= -4097;
/* 251 */         j &= -8193;
/* 252 */         j &= -262145;
/* 253 */       } else if (paramArrayOfString[m].startsWith("-g:"))
/*     */       {
/* 259 */         if ((str9 != null) && (!str9.equals(paramArrayOfString[m])))
/* 260 */           error("main.conflicting.options", str9, paramArrayOfString[m]);
/* 261 */         str9 = paramArrayOfString[m];
/* 262 */         String str11 = paramArrayOfString[m].substring("-g:".length());
/* 263 */         j &= -4097;
/* 264 */         j &= -8193;
/* 265 */         j &= -262145;
/*     */         while (true) {
/* 267 */           if (str11.startsWith("lines")) {
/* 268 */             j |= 4096;
/* 269 */             str11 = str11.substring("lines".length());
/* 270 */           } else if (str11.startsWith("vars")) {
/* 271 */             j |= 8192;
/* 272 */             str11 = str11.substring("vars".length());
/* 273 */           } else if (str11.startsWith("source")) {
/* 274 */             j |= 262144;
/* 275 */             str11 = str11.substring("source".length());
/*     */           } else {
/* 277 */             error("main.bad.debug.option", paramArrayOfString[m]);
/* 278 */             usage_error();
/* 279 */             return false;
/*     */           }
/* 281 */           if (str11.length() == 0) break;
/* 282 */           if (str11.startsWith(","))
/* 283 */             str11 = str11.substring(",".length());
/*     */         }
/* 285 */       } else if (paramArrayOfString[m].equals("-O"))
/*     */       {
/* 289 */         if ((str10 != null) && (!str10.equals("-O")))
/* 290 */           error("main.conflicting.options", str10, "-O");
/* 291 */         str10 = "-O";
/* 292 */       } else if (paramArrayOfString[m].equals("-nowarn")) {
/* 293 */         j &= -5;
/* 294 */       } else if (paramArrayOfString[m].equals("-deprecation")) {
/* 295 */         j |= 512;
/* 296 */       } else if (paramArrayOfString[m].equals("-verbose")) {
/* 297 */         j |= 1;
/* 298 */       } else if (paramArrayOfString[m].equals("-nowrite")) {
/* 299 */         k = 1;
/* 300 */       } else if (paramArrayOfString[m].equals("-classpath")) {
/* 301 */         if (m + 1 < paramArrayOfString.length) {
/* 302 */           if (str2 != null) {
/* 303 */             error("main.option.already.seen", "-classpath");
/*     */           }
/* 305 */           str2 = paramArrayOfString[(++m)];
/*     */         } else {
/* 307 */           error("main.option.requires.argument", "-classpath");
/* 308 */           usage_error();
/* 309 */           return false;
/*     */         }
/* 311 */       } else if (paramArrayOfString[m].equals("-sourcepath")) {
/* 312 */         if (m + 1 < paramArrayOfString.length) {
/* 313 */           if (str1 != null) {
/* 314 */             error("main.option.already.seen", "-sourcepath");
/*     */           }
/* 316 */           str1 = paramArrayOfString[(++m)];
/*     */         } else {
/* 318 */           error("main.option.requires.argument", "-sourcepath");
/* 319 */           usage_error();
/* 320 */           return false;
/*     */         }
/* 322 */       } else if (paramArrayOfString[m].equals("-sysclasspath")) {
/* 323 */         if (m + 1 < paramArrayOfString.length) {
/* 324 */           if (str3 != null) {
/* 325 */             error("main.option.already.seen", "-sysclasspath");
/*     */           }
/* 327 */           str3 = paramArrayOfString[(++m)];
/*     */         } else {
/* 329 */           error("main.option.requires.argument", "-sysclasspath");
/* 330 */           usage_error();
/* 331 */           return false;
/*     */         }
/* 333 */       } else if (paramArrayOfString[m].equals("-bootclasspath")) {
/* 334 */         if (m + 1 < paramArrayOfString.length) {
/* 335 */           if (str3 != null) {
/* 336 */             error("main.option.already.seen", "-bootclasspath");
/*     */           }
/* 338 */           str3 = paramArrayOfString[(++m)];
/*     */         } else {
/* 340 */           error("main.option.requires.argument", "-bootclasspath");
/* 341 */           usage_error();
/* 342 */           return false;
/*     */         }
/* 344 */       } else if (paramArrayOfString[m].equals("-extdirs")) {
/* 345 */         if (m + 1 < paramArrayOfString.length) {
/* 346 */           if (str4 != null) {
/* 347 */             error("main.option.already.seen", "-extdirs");
/*     */           }
/* 349 */           str4 = paramArrayOfString[(++m)];
/*     */         } else {
/* 351 */           error("main.option.requires.argument", "-extdirs");
/* 352 */           usage_error();
/* 353 */           return false;
/*     */         }
/* 355 */       } else if (paramArrayOfString[m].equals("-encoding")) {
/* 356 */         if (m + 1 < paramArrayOfString.length) {
/* 357 */           if (str8 != null)
/* 358 */             error("main.option.already.seen", "-encoding");
/* 359 */           str8 = paramArrayOfString[(++m)];
/*     */         } else {
/* 361 */           error("main.option.requires.argument", "-encoding");
/* 362 */           usage_error();
/* 363 */           return false;
/*     */         }
/* 365 */       } else if (paramArrayOfString[m].equals("-target")) {
/* 366 */         if (m + 1 < paramArrayOfString.length) {
/* 367 */           if (str5 != null)
/* 368 */             error("main.option.already.seen", "-target");
/* 369 */           str5 = paramArrayOfString[(++m)];
/*     */ 
/* 371 */           for (int n = 0; n < releases.length; n++) {
/* 372 */             if (releases[n].equals(str5)) {
/* 373 */               s1 = majorVersions[n];
/* 374 */               s2 = minorVersions[n];
/* 375 */               break;
/*     */             }
/*     */           }
/* 378 */           if (n == releases.length) {
/* 379 */             error("main.unknown.release", str5);
/* 380 */             usage_error();
/* 381 */             return false;
/*     */           }
/*     */         } else {
/* 384 */           error("main.option.requires.argument", "-target");
/* 385 */           usage_error();
/* 386 */           return false;
/*     */         }
/* 388 */       } else if (paramArrayOfString[m].equals("-d")) {
/* 389 */         if (m + 1 < paramArrayOfString.length) {
/* 390 */           if (localFile1 != null)
/* 391 */             error("main.option.already.seen", "-d");
/* 392 */           localFile1 = new File(paramArrayOfString[(++m)]);
/* 393 */           if (!localFile1.exists()) {
/* 394 */             error("main.no.such.directory", localFile1.getPath());
/* 395 */             usage_error();
/* 396 */             return false;
/*     */           }
/*     */         } else {
/* 399 */           error("main.option.requires.argument", "-d");
/* 400 */           usage_error();
/* 401 */           return false;
/*     */         }
/*     */       }
/* 404 */       else if (paramArrayOfString[m].equals(str6)) {
/* 405 */         j |= 64;
/* 406 */         j &= -16385;
/* 407 */         j &= -32769;
/* 408 */       } else if ((paramArrayOfString[m].startsWith(str7)) && 
/* 409 */         (paramArrayOfString[m]
/* 409 */         .length() > str7.length())) {
/* 410 */         localFile2 = new File(paramArrayOfString[m].substring(str7.length()));
/* 411 */         j &= -16385;
/* 412 */         j &= -32769;
/* 413 */         j |= 64;
/* 414 */         j |= 128;
/*     */       }
/* 416 */       else if (paramArrayOfString[m].equals("-XO"))
/*     */       {
/* 418 */         if ((str10 != null) && (!str10.equals("-XO")))
/* 419 */           error("main.conflicting.options", str10, "-XO");
/* 420 */         str10 = "-XO";
/* 421 */         j |= 16384;
/* 422 */       } else if (paramArrayOfString[m].equals("-Xinterclass")) {
/* 423 */         if ((str10 != null) && (!str10.equals("-Xinterclass")))
/* 424 */           error("main.conflicting.options", str10, "-Xinterclass");
/* 425 */         str10 = "-Xinterclass";
/* 426 */         j |= 16384;
/* 427 */         j |= 32768;
/* 428 */         j |= 32;
/* 429 */       } else if (paramArrayOfString[m].equals("-Xdepend")) {
/* 430 */         j |= 32;
/* 431 */       } else if (paramArrayOfString[m].equals("-Xdebug")) {
/* 432 */         j |= 2;
/*     */       }
/* 436 */       else if ((paramArrayOfString[m].equals("-xdepend")) || (paramArrayOfString[m].equals("-Xjws"))) {
/* 437 */         j |= 1024;
/*     */ 
/* 439 */         if (this.out == System.err)
/* 440 */           this.out = System.out;
/*     */       }
/* 442 */       else if (paramArrayOfString[m].equals("-Xstrictdefault"))
/*     */       {
/* 444 */         j |= 131072;
/* 445 */       } else if (paramArrayOfString[m].equals("-Xverbosepath")) {
/* 446 */         i = 1;
/* 447 */       } else if (paramArrayOfString[m].equals("-Xstdout")) {
/* 448 */         this.out = System.out; } else {
/* 449 */         if (paramArrayOfString[m].equals("-X")) {
/* 450 */           error("main.unsupported.usage");
/* 451 */           return false;
/* 452 */         }if (paramArrayOfString[m].equals("-Xversion1.2"))
/*     */         {
/* 459 */           j |= 2048;
/* 460 */         } else if (paramArrayOfString[m].endsWith(".java")) {
/* 461 */           localVector.addElement(paramArrayOfString[m]);
/*     */         } else {
/* 463 */           error("main.no.such.option", paramArrayOfString[m]);
/* 464 */           usage_error();
/* 465 */           return false;
/*     */         }
/*     */       }
/* 468 */     if ((localVector.size() == 0) || (this.exitStatus == 2)) {
/* 469 */       usage_error();
/* 470 */       return false;
/*     */     }
/*     */ 
/* 474 */     BatchEnvironment localBatchEnvironment = BatchEnvironment.create(this.out, str1, str2, str3, str4);
/*     */ 
/* 479 */     if (i != 0) {
/* 480 */       output(getText("main.path.msg", localBatchEnvironment.sourcePath
/* 481 */         .toString(), localBatchEnvironment.binaryPath
/* 482 */         .toString())); } 
/*     */ localBatchEnvironment.flags |= j;
/* 486 */     localBatchEnvironment.majorVersion = s1;
/* 487 */     localBatchEnvironment.minorVersion = s2;
/*     */ 
/* 489 */     localBatchEnvironment.covFile = localFile2;
/*     */ 
/* 491 */     localBatchEnvironment.setCharacterEncoding(str8);
/*     */ 
/* 495 */     String str12 = getText("main.no.memory");
/* 496 */     String str13 = getText("main.stack.overflow");
/*     */ 
/* 498 */     localBatchEnvironment.error(0L, "warn.class.is.deprecated", "sun.tools.javac.Main");
/*     */     Object localObject4;
/*     */     try { for (Object localObject2 = localVector.elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 503 */         localObject3 = new File((String)((Enumeration)localObject2).nextElement());
/*     */         try {
/* 505 */           localBatchEnvironment.parseFile(new ClassFile((File)localObject3));
/*     */         } catch (FileNotFoundException localFileNotFoundException2) {
/* 507 */           localBatchEnvironment.error(0L, "cant.read", ((File)localObject3).getPath());
/* 508 */           this.exitStatus = 2;
/*     */         }
/*     */       }
/*     */       Object localObject3;
/* 514 */       for (localObject2 = localBatchEnvironment.getClasses(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 515 */         localObject3 = (ClassDeclaration)((Enumeration)localObject2).nextElement();
/* 516 */         if (((ClassDeclaration)localObject3).getStatus() == 4) {
/* 517 */           if (!((ClassDeclaration)localObject3).getClassDefinition().isLocal())
/*     */             try
/*     */             {
/* 520 */               ((ClassDeclaration)localObject3).getClassDefinition(localBatchEnvironment);
/*     */             }
/*     */             catch (ClassNotFound localClassNotFound)
/*     */             {
/*     */             }
/*     */         }
/*     */       }
/* 527 */       localObject2 = new ByteArrayOutputStream(4096);
/*     */       do
/*     */       {
/* 531 */         i2 = 1;
/* 532 */         localBatchEnvironment.flushErrors();
/* 533 */         for (localObject4 = localBatchEnvironment.getClasses(); ((Enumeration)localObject4).hasMoreElements(); ) {
/* 534 */           ClassDeclaration localClassDeclaration = (ClassDeclaration)((Enumeration)localObject4).nextElement();
/*     */           SourceClass localSourceClass;
/* 537 */           switch (localClassDeclaration.getStatus())
/*     */           {
/*     */           case 0:
/* 539 */             if (!localBatchEnvironment.dependencies());
/*     */             break;
/*     */           case 3:
/* 546 */             localBatchEnvironment.dtEvent("Main.compile (SOURCE): loading, " + localClassDeclaration);
/* 547 */             i2 = 0;
/* 548 */             localBatchEnvironment.loadDefinition(localClassDeclaration);
/* 549 */             if (localClassDeclaration.getStatus() != 4)
/*     */             {
/* 551 */               localBatchEnvironment.dtEvent("Main.compile (SOURCE): not parsed, " + localClassDeclaration);
/* 552 */             }break;
/*     */           case 4:
/* 557 */             if (localClassDeclaration.getClassDefinition().isInsideLocal())
/*     */             {
/* 560 */               localBatchEnvironment.dtEvent("Main.compile (PARSED): skipping local class, " + localClassDeclaration);
/*     */             }
/*     */             else {
/* 563 */               i2 = 0;
/* 564 */               localBatchEnvironment.dtEvent("Main.compile (PARSED): checking, " + localClassDeclaration);
/* 565 */               localSourceClass = (SourceClass)localClassDeclaration.getClassDefinition(localBatchEnvironment);
/* 566 */               localSourceClass.check(localBatchEnvironment);
/* 567 */               localClassDeclaration.setDefinition(localSourceClass, 5);
/*     */             }
/*     */             break;
/*     */           case 5:
/* 571 */             localSourceClass = (SourceClass)localClassDeclaration.getClassDefinition(localBatchEnvironment);
/*     */ 
/* 573 */             if (localSourceClass.getError())
/*     */             {
/* 575 */               localBatchEnvironment.dtEvent("Main.compile (CHECKED): bailing out on error, " + localClassDeclaration);
/* 576 */               localClassDeclaration.setDefinition(localSourceClass, 6);
/*     */             }
/*     */             else {
/* 579 */               i2 = 0;
/* 580 */               ((ByteArrayOutputStream)localObject2).reset();
/*     */ 
/* 582 */               localBatchEnvironment.dtEvent("Main.compile (CHECKED): compiling, " + localClassDeclaration);
/* 583 */               localSourceClass.compile((OutputStream)localObject2);
/* 584 */               localClassDeclaration.setDefinition(localSourceClass, 6);
/* 585 */               localSourceClass.cleanup(localBatchEnvironment);
/*     */ 
/* 587 */               if ((!localSourceClass.getNestError()) && (k == 0))
/*     */               {
/* 591 */                 String str14 = localClassDeclaration.getName().getQualifier().toString().replace('.', File.separatorChar);
/* 592 */                 String str15 = localClassDeclaration.getName().getFlatName().toString().replace('.', '$') + ".class";
/*     */                 File localFile3;
/*     */                 Object localObject5;
/* 595 */                 if (localFile1 != null) {
/* 596 */                   if (str14.length() > 0) {
/* 597 */                     localFile3 = new File(localFile1, str14);
/* 598 */                     if (!localFile3.exists()) {
/* 599 */                       localFile3.mkdirs();
/*     */                     }
/* 601 */                     localFile3 = new File(localFile3, str15);
/*     */                   } else {
/* 603 */                     localFile3 = new File(localFile1, str15);
/*     */                   }
/*     */                 } else {
/* 606 */                   localObject5 = (ClassFile)localSourceClass.getSource();
/* 607 */                   if (((ClassFile)localObject5).isZipped()) {
/* 608 */                     localBatchEnvironment.error(0L, "cant.write", ((ClassFile)localObject5).getPath());
/* 609 */                     this.exitStatus = 2;
/* 610 */                     continue;
/*     */                   }
/* 612 */                   localFile3 = new File(((ClassFile)localObject5).getPath());
/* 613 */                   localFile3 = new File(localFile3.getParent(), str15);
/*     */                 }
/*     */ 
/*     */                 try
/*     */                 {
/* 618 */                   localObject5 = new FileOutputStream(localFile3.getPath());
/* 619 */                   ((ByteArrayOutputStream)localObject2).writeTo((OutputStream)localObject5);
/* 620 */                   ((FileOutputStream)localObject5).close();
/*     */ 
/* 622 */                   if (localBatchEnvironment.verbose())
/* 623 */                     output(getText("main.wrote", localFile3.getPath()));
/*     */                 }
/*     */                 catch (IOException localIOException2) {
/* 626 */                   localBatchEnvironment.error(0L, "cant.write", localFile3.getPath());
/* 627 */                   this.exitStatus = 2;
/*     */                 }
/*     */ 
/* 631 */                 if (localBatchEnvironment.print_dependencies())
/* 632 */                   localSourceClass.printClassDependencies(localBatchEnvironment);  }  } break;
/*     */           case 1:
/*     */           case 2: }  } 
/*     */       }
/* 636 */       while (i2 == 0);
/*     */     }
/*     */     catch (OutOfMemoryError localOutOfMemoryError)
/*     */     {
/* 640 */       localBatchEnvironment.output(str12);
/* 641 */       this.exitStatus = 3;
/* 642 */       return false;
/*     */     } catch (StackOverflowError localStackOverflowError) {
/* 644 */       localBatchEnvironment.output(str13);
/* 645 */       this.exitStatus = 3;
/* 646 */       return false;
/*     */     }
/*     */     catch (Error localError)
/*     */     {
/* 651 */       if ((localBatchEnvironment.nerrors == 0) || (localBatchEnvironment.dump())) {
/* 652 */         localError.printStackTrace();
/* 653 */         localBatchEnvironment.error(0L, "fatal.error");
/* 654 */         this.exitStatus = 4;
/*     */       }
/*     */     } catch (Exception localException) {
/* 657 */       if ((localBatchEnvironment.nerrors == 0) || (localBatchEnvironment.dump())) {
/* 658 */         localException.printStackTrace();
/* 659 */         localBatchEnvironment.error(0L, "fatal.exception");
/* 660 */         this.exitStatus = 4;
/*     */       }
/*     */     }
/*     */ 
/* 664 */     int i1 = localBatchEnvironment.deprecationFiles.size();
/* 665 */     if ((i1 > 0) && (localBatchEnvironment.warnings())) {
/* 666 */       i2 = localBatchEnvironment.ndeprecations;
/* 667 */       localObject4 = localBatchEnvironment.deprecationFiles.elementAt(0);
/* 668 */       if (localBatchEnvironment.deprecation()) {
/* 669 */         if (i1 > 1) {
/* 670 */           localBatchEnvironment.error(0L, "warn.note.deprecations", new Integer(i1), new Integer(i2));
/*     */         }
/*     */         else {
/* 673 */           localBatchEnvironment.error(0L, "warn.note.1deprecation", localObject4, new Integer(i2));
/*     */         }
/*     */ 
/*     */       }
/* 677 */       else if (i1 > 1) {
/* 678 */         localBatchEnvironment.error(0L, "warn.note.deprecations.silent", new Integer(i1), new Integer(i2));
/*     */       }
/*     */       else {
/* 681 */         localBatchEnvironment.error(0L, "warn.note.1deprecation.silent", localObject4, new Integer(i2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 687 */     localBatchEnvironment.flushErrors();
/* 688 */     localBatchEnvironment.shutdown();
/*     */ 
/* 690 */     int i2 = 1;
/* 691 */     if (localBatchEnvironment.nerrors > 0) {
/* 692 */       localObject4 = "";
/* 693 */       if (localBatchEnvironment.nerrors > 1)
/* 694 */         localObject4 = getText("main.errors", localBatchEnvironment.nerrors);
/*     */       else {
/* 696 */         localObject4 = getText("main.1error");
/*     */       }
/* 698 */       if (localBatchEnvironment.nwarnings > 0) {
/* 699 */         if (localBatchEnvironment.nwarnings > 1)
/* 700 */           localObject4 = (String)localObject4 + ", " + getText("main.warnings", localBatchEnvironment.nwarnings);
/*     */         else {
/* 702 */           localObject4 = (String)localObject4 + ", " + getText("main.1warning");
/*     */         }
/*     */       }
/* 705 */       output((String)localObject4);
/* 706 */       if (this.exitStatus == 0)
/*     */       {
/* 708 */         this.exitStatus = 1;
/*     */       }
/* 710 */       i2 = 0;
/*     */     }
/* 712 */     else if (localBatchEnvironment.nwarnings > 0) {
/* 713 */       if (localBatchEnvironment.nwarnings > 1)
/* 714 */         output(getText("main.warnings", localBatchEnvironment.nwarnings));
/*     */       else {
/* 716 */         output(getText("main.1warning"));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 721 */     if (localBatchEnvironment.covdata()) {
/* 722 */       localObject4 = new Assembler();
/* 723 */       ((Assembler)localObject4).GenJCov(localBatchEnvironment);
/*     */     }
/*     */ 
/* 728 */     if (localBatchEnvironment.verbose()) {
/* 729 */       l = System.currentTimeMillis() - l;
/* 730 */       output(getText("main.done_in", Long.toString(l)));
/*     */     }
/*     */ 
/* 733 */     return i2;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 740 */     PrintStream localPrintStream = System.err;
/*     */ 
/* 744 */     if (Boolean.getBoolean("javac.pipe.output")) {
/* 745 */       localPrintStream = System.out;
/*     */     }
/*     */ 
/* 748 */     Main localMain = new Main(localPrintStream, "javac");
/* 749 */     System.exit(localMain.compile(paramArrayOfString) ? 0 : localMain.exitStatus);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.Main
 * JD-Core Version:    0.6.2
 */