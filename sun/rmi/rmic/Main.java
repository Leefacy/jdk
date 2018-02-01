/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.javac.SourceClass;
/*     */ import sun.tools.util.CommandLine;
/*     */ 
/*     */ public class Main
/*     */   implements Constants
/*     */ {
/*     */   String sourcePathArg;
/*     */   String sysClassPathArg;
/*     */   String extDirsArg;
/*     */   String classPathString;
/*     */   File destDir;
/*     */   int flags;
/*     */   long tm;
/*     */   Vector<String> classes;
/*     */   boolean nowrite;
/*     */   boolean nocompile;
/*     */   boolean keepGenerated;
/*     */   boolean status;
/*     */   String[] generatorArgs;
/*     */   Vector<Generator> generators;
/*  83 */   Class<? extends BatchEnvironment> environmentClass = BatchEnvironment.class;
/*     */ 
/*  85 */   boolean iiopGeneration = false;
/*     */   String program;
/*     */   OutputStream out;
/* 842 */   private static boolean resourcesInitialized = false;
/*     */   private static ResourceBundle resources;
/* 844 */   private static ResourceBundle resourcesExt = null;
/*     */ 
/*     */   public Main(OutputStream paramOutputStream, String paramString)
/*     */   {
/* 101 */     this.out = paramOutputStream;
/* 102 */     this.program = paramString;
/*     */   }
/*     */ 
/*     */   public void output(String paramString)
/*     */   {
/* 109 */     PrintStream localPrintStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*     */ 
/* 112 */     localPrintStream.println(paramString);
/*     */   }
/*     */ 
/*     */   public void error(String paramString)
/*     */   {
/* 120 */     output(getText(paramString));
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2) {
/* 124 */     output(getText(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2, String paramString3) {
/* 128 */     output(getText(paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */   public void usage()
/*     */   {
/* 135 */     error("rmic.usage", this.program);
/*     */   }
/*     */ 
/*     */   public synchronized boolean compile(String[] paramArrayOfString)
/*     */   {
/* 149 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 150 */       if (paramArrayOfString[i].equals("-Xnew"))
/*     */       {
/* 152 */         return new sun.rmi.rmic.newrmic.Main(this.out, this.program)
/* 152 */           .compile(paramArrayOfString);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 156 */     if (!parseArgs(paramArrayOfString)) {
/* 157 */       return false;
/*     */     }
/*     */ 
/* 160 */     if (this.classes.size() == 0) {
/* 161 */       usage();
/* 162 */       return false;
/*     */     }
/*     */ 
/* 165 */     if ((this.flags & 0x4) != 0) {
/* 166 */       for (Generator localGenerator : this.generators) {
/* 167 */         if ((localGenerator instanceof RMIGenerator)) {
/* 168 */           output(getText("rmic.jrmp.stubs.deprecated", this.program));
/* 169 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 174 */     return doCompile();
/*     */   }
/*     */ 
/*     */   public File getDestinationDir()
/*     */   {
/* 181 */     return this.destDir;
/*     */   }
/*     */ 
/*     */   public boolean parseArgs(String[] paramArrayOfString)
/*     */   {
/* 188 */     this.sourcePathArg = null;
/* 189 */     this.sysClassPathArg = null;
/* 190 */     this.extDirsArg = null;
/*     */ 
/* 192 */     this.classPathString = null;
/* 193 */     this.destDir = null;
/* 194 */     this.flags = 4;
/* 195 */     this.tm = System.currentTimeMillis();
/* 196 */     this.classes = new Vector();
/* 197 */     this.nowrite = false;
/* 198 */     this.nocompile = false;
/* 199 */     this.keepGenerated = false;
/* 200 */     this.generatorArgs = getArray("generator.args", true);
/* 201 */     if (this.generatorArgs == null) {
/* 202 */       return false;
/*     */     }
/* 204 */     this.generators = new Vector();
/*     */     try
/*     */     {
/* 208 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 210 */       error("rmic.cant.read", localFileNotFoundException.getMessage());
/* 211 */       return false;
/*     */     } catch (IOException localIOException) {
/* 213 */       localIOException.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */ 
/* 216 */       return false;
/*     */     }
/*     */ 
/* 220 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 221 */       if (paramArrayOfString[i] != null) {
/* 222 */         if (paramArrayOfString[i].equals("-g")) {
/* 223 */           this.flags &= -16385;
/* 224 */           this.flags |= 12288;
/* 225 */           paramArrayOfString[i] = null;
/* 226 */         } else if (paramArrayOfString[i].equals("-O")) {
/* 227 */           this.flags &= -4097;
/* 228 */           this.flags &= -8193;
/* 229 */           this.flags |= 16416;
/* 230 */           paramArrayOfString[i] = null;
/* 231 */         } else if (paramArrayOfString[i].equals("-nowarn")) {
/* 232 */           this.flags &= -5;
/* 233 */           paramArrayOfString[i] = null;
/* 234 */         } else if (paramArrayOfString[i].equals("-debug")) {
/* 235 */           this.flags |= 2;
/* 236 */           paramArrayOfString[i] = null;
/* 237 */         } else if (paramArrayOfString[i].equals("-depend")) {
/* 238 */           this.flags |= 32;
/* 239 */           paramArrayOfString[i] = null;
/* 240 */         } else if (paramArrayOfString[i].equals("-verbose")) {
/* 241 */           this.flags |= 1;
/* 242 */           paramArrayOfString[i] = null;
/* 243 */         } else if (paramArrayOfString[i].equals("-nowrite")) {
/* 244 */           this.nowrite = true;
/* 245 */           paramArrayOfString[i] = null;
/* 246 */         } else if (paramArrayOfString[i].equals("-Xnocompile")) {
/* 247 */           this.nocompile = true;
/* 248 */           this.keepGenerated = true;
/* 249 */           paramArrayOfString[i] = null;
/* 250 */         } else if ((paramArrayOfString[i].equals("-keep")) || 
/* 251 */           (paramArrayOfString[i]
/* 251 */           .equals("-keepgenerated")))
/*     */         {
/* 252 */           this.keepGenerated = true;
/* 253 */           paramArrayOfString[i] = null; } else {
/* 254 */           if (paramArrayOfString[i].equals("-show")) {
/* 255 */             error("rmic.option.unsupported", "-show");
/* 256 */             usage();
/* 257 */             return false;
/* 258 */           }if (paramArrayOfString[i].equals("-classpath")) {
/* 259 */             if (i + 1 < paramArrayOfString.length) {
/* 260 */               if (this.classPathString != null) {
/* 261 */                 error("rmic.option.already.seen", "-classpath");
/* 262 */                 usage();
/* 263 */                 return false;
/*     */               }
/* 265 */               paramArrayOfString[i] = null;
/* 266 */               this.classPathString = paramArrayOfString[(++i)];
/* 267 */               paramArrayOfString[i] = null;
/*     */             } else {
/* 269 */               error("rmic.option.requires.argument", "-classpath");
/* 270 */               usage();
/* 271 */               return false;
/*     */             }
/* 273 */           } else if (paramArrayOfString[i].equals("-sourcepath")) {
/* 274 */             if (i + 1 < paramArrayOfString.length) {
/* 275 */               if (this.sourcePathArg != null) {
/* 276 */                 error("rmic.option.already.seen", "-sourcepath");
/* 277 */                 usage();
/* 278 */                 return false;
/*     */               }
/* 280 */               paramArrayOfString[i] = null;
/* 281 */               this.sourcePathArg = paramArrayOfString[(++i)];
/* 282 */               paramArrayOfString[i] = null;
/*     */             } else {
/* 284 */               error("rmic.option.requires.argument", "-sourcepath");
/* 285 */               usage();
/* 286 */               return false;
/*     */             }
/* 288 */           } else if (paramArrayOfString[i].equals("-bootclasspath")) {
/* 289 */             if (i + 1 < paramArrayOfString.length) {
/* 290 */               if (this.sysClassPathArg != null) {
/* 291 */                 error("rmic.option.already.seen", "-bootclasspath");
/* 292 */                 usage();
/* 293 */                 return false;
/*     */               }
/* 295 */               paramArrayOfString[i] = null;
/* 296 */               this.sysClassPathArg = paramArrayOfString[(++i)];
/* 297 */               paramArrayOfString[i] = null;
/*     */             } else {
/* 299 */               error("rmic.option.requires.argument", "-bootclasspath");
/* 300 */               usage();
/* 301 */               return false;
/*     */             }
/* 303 */           } else if (paramArrayOfString[i].equals("-extdirs")) {
/* 304 */             if (i + 1 < paramArrayOfString.length) {
/* 305 */               if (this.extDirsArg != null) {
/* 306 */                 error("rmic.option.already.seen", "-extdirs");
/* 307 */                 usage();
/* 308 */                 return false;
/*     */               }
/* 310 */               paramArrayOfString[i] = null;
/* 311 */               this.extDirsArg = paramArrayOfString[(++i)];
/* 312 */               paramArrayOfString[i] = null;
/*     */             } else {
/* 314 */               error("rmic.option.requires.argument", "-extdirs");
/* 315 */               usage();
/* 316 */               return false;
/*     */             }
/* 318 */           } else if (paramArrayOfString[i].equals("-d")) {
/* 319 */             if (i + 1 < paramArrayOfString.length) {
/* 320 */               if (this.destDir != null) {
/* 321 */                 error("rmic.option.already.seen", "-d");
/* 322 */                 usage();
/* 323 */                 return false;
/*     */               }
/* 325 */               paramArrayOfString[i] = null;
/* 326 */               this.destDir = new File(paramArrayOfString[(++i)]);
/* 327 */               paramArrayOfString[i] = null;
/* 328 */               if (!this.destDir.exists()) {
/* 329 */                 error("rmic.no.such.directory", this.destDir.getPath());
/* 330 */                 usage();
/* 331 */                 return false;
/*     */               }
/*     */             } else {
/* 334 */               error("rmic.option.requires.argument", "-d");
/* 335 */               usage();
/* 336 */               return false;
/*     */             }
/*     */           }
/* 339 */           else if (!checkGeneratorArg(paramArrayOfString, i)) {
/* 340 */             usage();
/* 341 */             return false;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 351 */     for (i = 0; i < paramArrayOfString.length; i++) {
/* 352 */       if (paramArrayOfString[i] != null) {
/* 353 */         if (paramArrayOfString[i].startsWith("-")) {
/* 354 */           error("rmic.no.such.option", paramArrayOfString[i]);
/* 355 */           usage();
/* 356 */           return false;
/*     */         }
/* 358 */         this.classes.addElement(paramArrayOfString[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     if (this.generators.size() == 0) {
/* 367 */       addGenerator("default");
/*     */     }
/*     */ 
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean checkGeneratorArg(String[] paramArrayOfString, int paramInt)
/*     */   {
/* 379 */     boolean bool = true;
/* 380 */     if (paramArrayOfString[paramInt].startsWith("-")) {
/* 381 */       String str = paramArrayOfString[paramInt].substring(1).toLowerCase();
/* 382 */       for (int i = 0; i < this.generatorArgs.length; i++) {
/* 383 */         if (str.equalsIgnoreCase(this.generatorArgs[i]))
/*     */         {
/* 385 */           Generator localGenerator = addGenerator(str);
/* 386 */           if (localGenerator == null) {
/* 387 */             return false;
/*     */           }
/* 389 */           bool = localGenerator.parseArgs(paramArrayOfString, this);
/* 390 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 394 */     return bool;
/*     */   }
/*     */ 
/*     */   protected Generator addGenerator(String paramString)
/*     */   {
/* 407 */     String str1 = getString("generator.class." + paramString);
/* 408 */     if (str1 == null) {
/* 409 */       error("rmic.missing.property", paramString);
/* 410 */       return null;
/*     */     }
/*     */     Generator localGenerator;
/*     */     try {
/* 414 */       localGenerator = (Generator)Class.forName(str1).newInstance();
/*     */     } catch (Exception localException) {
/* 416 */       error("rmic.cannot.instantiate", str1);
/* 417 */       return null;
/*     */     }
/*     */ 
/* 420 */     this.generators.addElement(localGenerator);
/*     */ 
/* 424 */     Object localObject = BatchEnvironment.class;
/* 425 */     String str2 = getString("generator.env." + paramString);
/* 426 */     if (str2 != null) {
/*     */       try {
/* 428 */         localObject = Class.forName(str2);
/*     */ 
/* 432 */         if (this.environmentClass.isAssignableFrom((Class)localObject))
/*     */         {
/* 436 */           this.environmentClass = ((Class)localObject).asSubclass(BatchEnvironment.class);
/*     */         }
/* 443 */         else if (!((Class)localObject).isAssignableFrom(this.environmentClass))
/*     */         {
/* 447 */           error("rmic.cannot.use.both", this.environmentClass.getName(), ((Class)localObject).getName());
/* 448 */           return null;
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 452 */         error("rmic.class.not.found", str2);
/* 453 */         return null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 460 */     if (paramString.equals("iiop")) {
/* 461 */       this.iiopGeneration = true;
/*     */     }
/* 463 */     return localGenerator;
/*     */   }
/*     */ 
/*     */   protected String[] getArray(String paramString, boolean paramBoolean)
/*     */   {
/* 474 */     String[] arrayOfString = null;
/* 475 */     String str = getString(paramString);
/* 476 */     if (str == null) {
/* 477 */       if (paramBoolean) {
/* 478 */         error("rmic.resource.not.found", paramString);
/* 479 */         return null;
/*     */       }
/* 481 */       return new String[0];
/*     */     }
/*     */ 
/* 485 */     StringTokenizer localStringTokenizer = new StringTokenizer(str, ", \t\n\r", false);
/* 486 */     int i = localStringTokenizer.countTokens();
/* 487 */     arrayOfString = new String[i];
/* 488 */     for (int j = 0; j < i; j++) {
/* 489 */       arrayOfString[j] = localStringTokenizer.nextToken();
/*     */     }
/*     */ 
/* 492 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public BatchEnvironment getEnv()
/*     */   {
/* 501 */     ClassPath localClassPath = BatchEnvironment.createClassPath(this.classPathString, this.sysClassPathArg, this.extDirsArg);
/*     */ 
/* 504 */     BatchEnvironment localBatchEnvironment = null;
/*     */     try {
/* 506 */       Class[] arrayOfClass = { OutputStream.class, ClassPath.class, Main.class };
/* 507 */       Object[] arrayOfObject = { this.out, localClassPath, this };
/*     */ 
/* 509 */       Constructor localConstructor = this.environmentClass
/* 509 */         .getConstructor(arrayOfClass);
/*     */ 
/* 510 */       localBatchEnvironment = (BatchEnvironment)localConstructor.newInstance(arrayOfObject);
/* 511 */       localBatchEnvironment.reset();
/*     */     }
/*     */     catch (Exception localException) {
/* 514 */       error("rmic.cannot.instantiate", this.environmentClass.getName());
/*     */     }
/* 516 */     return localBatchEnvironment;
/*     */   }
/*     */ 
/*     */   public boolean doCompile()
/*     */   {
/* 525 */     BatchEnvironment localBatchEnvironment = getEnv();
/* 526 */     localBatchEnvironment.flags |= this.flags;
/*     */ 
/* 530 */     localBatchEnvironment.majorVersion = 45;
/* 531 */     localBatchEnvironment.minorVersion = 3;
/*     */ 
/* 535 */     String str1 = getText("rmic.no.memory");
/* 536 */     String str2 = getText("rmic.stack.overflow");
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 542 */       for (int i = this.classes.size() - 1; i >= 0; i--)
/*     */       {
/* 544 */         localObject = Identifier.lookup((String)this.classes
/* 544 */           .elementAt(i));
/*     */ 
/* 556 */         localObject = localBatchEnvironment.resolvePackageQualifiedName((Identifier)localObject);
/*     */ 
/* 566 */         localObject = Names.mangleClass((Identifier)localObject);
/*     */ 
/* 568 */         ClassDeclaration localClassDeclaration = localBatchEnvironment.getClassDeclaration((Identifier)localObject);
/*     */         try {
/* 570 */           ClassDefinition localClassDefinition = localClassDeclaration.getClassDefinition(localBatchEnvironment);
/* 571 */           for (int j = 0; j < this.generators.size(); j++) {
/* 572 */             Generator localGenerator = (Generator)this.generators.elementAt(j);
/* 573 */             localGenerator.generate(localBatchEnvironment, localClassDefinition, this.destDir);
/*     */           }
/*     */         } catch (ClassNotFound localClassNotFound) {
/* 576 */           localBatchEnvironment.error(0L, "rmic.class.not.found", localObject);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 582 */       if (!this.nocompile) {
/* 583 */         compileAllClasses(localBatchEnvironment);
/*     */       }
/*     */     }
/*     */     catch (OutOfMemoryError localOutOfMemoryError)
/*     */     {
/* 588 */       localBatchEnvironment.output(str1);
/* 589 */       return false;
/*     */     } catch (StackOverflowError localStackOverflowError) {
/* 591 */       localBatchEnvironment.output(str2);
/* 592 */       return false;
/*     */     }
/*     */     catch (Error localError)
/*     */     {
/* 597 */       if ((localBatchEnvironment.nerrors == 0) || (localBatchEnvironment.dump())) {
/* 598 */         localBatchEnvironment.error(0L, "fatal.error");
/* 599 */         localError.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 604 */       if ((localBatchEnvironment.nerrors == 0) || (localBatchEnvironment.dump())) {
/* 605 */         localBatchEnvironment.error(0L, "fatal.exception");
/* 606 */         localException.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 612 */     localBatchEnvironment.flushErrors();
/*     */ 
/* 614 */     boolean bool = true;
/* 615 */     if (localBatchEnvironment.nerrors > 0) {
/* 616 */       localObject = "";
/* 617 */       if (localBatchEnvironment.nerrors > 1)
/* 618 */         localObject = getText("rmic.errors", localBatchEnvironment.nerrors);
/*     */       else {
/* 620 */         localObject = getText("rmic.1error");
/*     */       }
/* 622 */       if (localBatchEnvironment.nwarnings > 0) {
/* 623 */         if (localBatchEnvironment.nwarnings > 1)
/* 624 */           localObject = (String)localObject + ", " + getText("rmic.warnings", localBatchEnvironment.nwarnings);
/*     */         else {
/* 626 */           localObject = (String)localObject + ", " + getText("rmic.1warning");
/*     */         }
/*     */       }
/* 629 */       output((String)localObject);
/* 630 */       bool = false;
/*     */     }
/* 632 */     else if (localBatchEnvironment.nwarnings > 0) {
/* 633 */       if (localBatchEnvironment.nwarnings > 1)
/* 634 */         output(getText("rmic.warnings", localBatchEnvironment.nwarnings));
/*     */       else {
/* 636 */         output(getText("rmic.1warning"));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 642 */     if (!this.keepGenerated) {
/* 643 */       localBatchEnvironment.deleteGeneratedFiles();
/*     */     }
/*     */ 
/* 647 */     if (localBatchEnvironment.verbose()) {
/* 648 */       this.tm = (System.currentTimeMillis() - this.tm);
/* 649 */       output(getText("rmic.done_in", Long.toString(this.tm)));
/*     */     }
/*     */ 
/* 658 */     localBatchEnvironment.shutdown();
/*     */ 
/* 660 */     this.sourcePathArg = null;
/* 661 */     this.sysClassPathArg = null;
/* 662 */     this.extDirsArg = null;
/* 663 */     this.classPathString = null;
/* 664 */     this.destDir = null;
/* 665 */     this.classes = null;
/* 666 */     this.generatorArgs = null;
/* 667 */     this.generators = null;
/* 668 */     this.environmentClass = null;
/* 669 */     this.program = null;
/* 670 */     this.out = null;
/*     */ 
/* 672 */     return bool;
/*     */   }
/*     */ 
/*     */   public void compileAllClasses(BatchEnvironment paramBatchEnvironment)
/*     */     throws ClassNotFound, IOException, InterruptedException
/*     */   {
/* 682 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4096);
/*     */     boolean bool;
/*     */     Enumeration localEnumeration;
/*     */     do
/*     */     {
/* 686 */       bool = true;
/* 687 */       for (localEnumeration = paramBatchEnvironment.getClasses(); localEnumeration.hasMoreElements(); ) {
/* 688 */         ClassDeclaration localClassDeclaration = (ClassDeclaration)localEnumeration.nextElement();
/* 689 */         bool = compileClass(localClassDeclaration, localByteArrayOutputStream, paramBatchEnvironment);
/*     */       }
/*     */     }
/* 691 */     while (!bool);
/*     */   }
/*     */ 
/*     */   public boolean compileClass(ClassDeclaration paramClassDeclaration, ByteArrayOutputStream paramByteArrayOutputStream, BatchEnvironment paramBatchEnvironment)
/*     */     throws ClassNotFound, IOException, InterruptedException
/*     */   {
/* 705 */     boolean bool = true;
/* 706 */     paramBatchEnvironment.flushErrors();
/*     */     SourceClass localSourceClass;
/* 709 */     switch (paramClassDeclaration.getStatus())
/*     */     {
/*     */     case 0:
/* 712 */       if (!paramBatchEnvironment.dependencies());
/*     */       break;
/*     */     case 3:
/* 720 */       bool = false;
/* 721 */       paramBatchEnvironment.loadDefinition(paramClassDeclaration);
/* 722 */       if (paramClassDeclaration.getStatus() != 4);
/*     */       break;
/*     */     case 4:
/* 730 */       if (!paramClassDeclaration.getClassDefinition().isInsideLocal())
/*     */       {
/* 739 */         if (this.nocompile) {
/* 740 */           throw new IOException("Compilation required, but -Xnocompile option in effect");
/*     */         }
/*     */ 
/* 743 */         bool = false;
/*     */ 
/* 745 */         localSourceClass = (SourceClass)paramClassDeclaration.getClassDefinition(paramBatchEnvironment);
/* 746 */         localSourceClass.check(paramBatchEnvironment);
/* 747 */         paramClassDeclaration.setDefinition(localSourceClass, 5);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/* 753 */       localSourceClass = (SourceClass)paramClassDeclaration.getClassDefinition(paramBatchEnvironment);
/*     */ 
/* 755 */       if (localSourceClass.getError()) {
/* 756 */         paramClassDeclaration.setDefinition(localSourceClass, 6);
/*     */       }
/*     */       else {
/* 759 */         bool = false;
/* 760 */         paramByteArrayOutputStream.reset();
/* 761 */         localSourceClass.compile(paramByteArrayOutputStream);
/* 762 */         paramClassDeclaration.setDefinition(localSourceClass, 6);
/* 763 */         localSourceClass.cleanup(paramBatchEnvironment);
/*     */ 
/* 765 */         if ((!localSourceClass.getError()) && (!this.nowrite))
/*     */         {
/* 769 */           String str1 = paramClassDeclaration.getName().getQualifier().toString().replace('.', File.separatorChar);
/* 770 */           String str2 = paramClassDeclaration.getName().getFlatName().toString().replace('.', '$') + ".class";
/*     */           File localFile;
/*     */           Object localObject;
/* 773 */           if (this.destDir != null) {
/* 774 */             if (str1.length() > 0) {
/* 775 */               localFile = new File(this.destDir, str1);
/* 776 */               if (!localFile.exists()) {
/* 777 */                 localFile.mkdirs();
/*     */               }
/* 779 */               localFile = new File(localFile, str2);
/*     */             } else {
/* 781 */               localFile = new File(this.destDir, str2);
/*     */             }
/*     */           } else {
/* 784 */             localObject = (ClassFile)localSourceClass.getSource();
/* 785 */             if (((ClassFile)localObject).isZipped()) {
/* 786 */               paramBatchEnvironment.error(0L, "cant.write", ((ClassFile)localObject).getPath());
/* 787 */               break label468;
/*     */             }
/* 789 */             localFile = new File(((ClassFile)localObject).getPath());
/* 790 */             localFile = new File(localFile.getParent(), str2);
/*     */           }
/*     */ 
/*     */           try
/*     */           {
/* 795 */             localObject = new FileOutputStream(localFile.getPath());
/* 796 */             paramByteArrayOutputStream.writeTo((OutputStream)localObject);
/* 797 */             ((FileOutputStream)localObject).close();
/* 798 */             if (paramBatchEnvironment.verbose())
/* 799 */               output(getText("rmic.wrote", localFile.getPath()));
/*     */           }
/*     */           catch (IOException localIOException) {
/* 802 */             paramBatchEnvironment.error(0L, "cant.write", localFile.getPath()); }  } 
/*     */       }break;
/*     */     case 1:
/*     */     case 2:
/* 806 */     }label468: return bool;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 813 */     Main localMain = new Main(System.out, "rmic");
/* 814 */     System.exit(localMain.compile(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ 
/*     */   public static String getString(String paramString)
/*     */   {
/* 822 */     if (!resourcesInitialized) {
/* 823 */       initResources();
/*     */     }
/*     */ 
/* 829 */     if (resourcesExt != null)
/*     */       try {
/* 831 */         return resourcesExt.getString(paramString);
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException1) {
/*     */       }
/*     */     try {
/* 836 */       return resources.getString(paramString);
/*     */     } catch (MissingResourceException localMissingResourceException2) {
/*     */     }
/* 839 */     return null;
/*     */   }
/*     */ 
/*     */   private static void initResources()
/*     */   {
/*     */     try
/*     */     {
/* 849 */       resources = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmic");
/*     */ 
/* 850 */       resourcesInitialized = true;
/*     */       try
/*     */       {
/* 853 */         resourcesExt = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmicext");
/*     */       } catch (MissingResourceException localMissingResourceException1) {
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException2) {
/* 857 */       throw new Error("fatal: missing resource bundle: " + localMissingResourceException2
/* 857 */         .getClassName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString) {
/* 862 */     String str = getString(paramString);
/* 863 */     if (str == null) {
/* 864 */       str = "no text found: \"" + paramString + "\"";
/*     */     }
/* 866 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString, int paramInt) {
/* 870 */     return getText(paramString, Integer.toString(paramInt), null, null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2) {
/* 874 */     return getText(paramString1, paramString2, null, null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2, String paramString3) {
/* 878 */     return getText(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 884 */     String str = getString(paramString1);
/* 885 */     if (str == null) {
/* 886 */       str = "no text found: key = \"" + paramString1 + "\", " + "arguments = \"{0}\", \"{1}\", \"{2}\"";
/*     */     }
/*     */ 
/* 890 */     String[] arrayOfString = new String[3];
/* 891 */     arrayOfString[0] = (paramString2 != null ? paramString2 : "null");
/* 892 */     arrayOfString[1] = (paramString3 != null ? paramString3 : "null");
/* 893 */     arrayOfString[2] = (paramString4 != null ? paramString4 : "null");
/*     */ 
/* 895 */     return MessageFormat.format(str, (Object[])arrayOfString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.Main
 * JD-Core Version:    0.6.2
 */