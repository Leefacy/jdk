/*     */ package com.sun.tools.doclets.internal.toolkit;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Extern;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Group;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ public abstract class Configuration
/*     */ {
/*     */   protected BuilderFactory builderFactory;
/*     */   public TagletManager tagletManager;
/*     */   public String builderXMLPath;
/*     */   private static final String DEFAULT_BUILDER_XML = "resources/doclet.xml";
/*  97 */   public String tagletpath = "";
/*     */ 
/* 103 */   public boolean serialwarn = false;
/*     */   public int sourcetab;
/*     */   public String tabSpaces;
/* 115 */   public boolean linksource = false;
/*     */ 
/* 121 */   public boolean nosince = false;
/*     */ 
/* 126 */   public boolean copydocfilesubdirs = false;
/*     */ 
/* 131 */   public String charset = "";
/*     */ 
/* 138 */   public boolean keywords = false;
/*     */ 
/* 143 */   public final MetaKeywords metakeywords = new MetaKeywords(this);
/*     */   protected Set<String> excludedDocFileDirs;
/*     */   protected Set<String> excludedQualifiers;
/*     */   public RootDoc root;
/* 164 */   public String destDirName = "";
/*     */ 
/* 169 */   public String docFileDestDirName = "";
/*     */ 
/* 175 */   public String docencoding = null;
/*     */ 
/* 180 */   public boolean nocomment = false;
/*     */ 
/* 186 */   public String encoding = null;
/*     */ 
/* 194 */   public boolean showauthor = false;
/*     */ 
/* 200 */   public boolean javafx = false;
/*     */ 
/* 208 */   public boolean showversion = false;
/*     */ 
/* 214 */   public String sourcepath = "";
/*     */ 
/* 219 */   public String profilespath = "";
/*     */ 
/* 225 */   public boolean showProfiles = false;
/*     */ 
/* 233 */   public boolean nodeprecated = false;
/*     */   public ClassDocCatalog classDocCatalog;
/* 247 */   public MessageRetriever message = null;
/*     */ 
/* 253 */   public boolean notimestamp = false;
/*     */ 
/* 258 */   public final Group group = new Group(this);
/*     */ 
/* 263 */   public final Extern extern = new Extern(this);
/*     */   public Profiles profiles;
/*     */   public Map<String, PackageDoc[]> profilePackages;
/*     */   public PackageDoc[] packages;
/*     */ 
/*     */   public abstract String getDocletSpecificBuildDate();
/*     */ 
/*     */   public abstract void setSpecificDocletOptions(String[][] paramArrayOfString)
/*     */     throws Configuration.Fault;
/*     */ 
/*     */   public abstract MessageRetriever getDocletSpecificMsg();
/*     */ 
/*     */   public Configuration()
/*     */   {
/* 307 */     this.message = new MessageRetriever(this, "com.sun.tools.doclets.internal.toolkit.resources.doclets");
/*     */ 
/* 310 */     this.excludedDocFileDirs = new HashSet();
/* 311 */     this.excludedQualifiers = new HashSet();
/* 312 */     setTabWidth(8);
/*     */   }
/*     */ 
/*     */   public BuilderFactory getBuilderFactory()
/*     */   {
/* 321 */     if (this.builderFactory == null) {
/* 322 */       this.builderFactory = new BuilderFactory(this);
/*     */     }
/* 324 */     return this.builderFactory;
/*     */   }
/*     */ 
/*     */   public int optionLength(String paramString)
/*     */   {
/* 341 */     paramString = StringUtils.toLowerCase(paramString);
/* 342 */     if ((paramString.equals("-author")) || 
/* 343 */       (paramString
/* 343 */       .equals("-docfilessubdirs")) || 
/* 344 */       (paramString
/* 344 */       .equals("-javafx")) || 
/* 345 */       (paramString
/* 345 */       .equals("-keywords")) || 
/* 346 */       (paramString
/* 346 */       .equals("-linksource")) || 
/* 347 */       (paramString
/* 347 */       .equals("-nocomment")) || 
/* 348 */       (paramString
/* 348 */       .equals("-nodeprecated")) || 
/* 349 */       (paramString
/* 349 */       .equals("-nosince")) || 
/* 350 */       (paramString
/* 350 */       .equals("-notimestamp")) || 
/* 351 */       (paramString
/* 351 */       .equals("-quiet")) || 
/* 352 */       (paramString
/* 352 */       .equals("-xnodate")) || 
/* 353 */       (paramString
/* 353 */       .equals("-version")))
/*     */     {
/* 354 */       return 1;
/* 355 */     }if ((paramString.equals("-d")) || 
/* 356 */       (paramString
/* 356 */       .equals("-docencoding")) || 
/* 357 */       (paramString
/* 357 */       .equals("-encoding")) || 
/* 358 */       (paramString
/* 358 */       .equals("-excludedocfilessubdir")) || 
/* 359 */       (paramString
/* 359 */       .equals("-link")) || 
/* 360 */       (paramString
/* 360 */       .equals("-sourcetab")) || 
/* 361 */       (paramString
/* 361 */       .equals("-noqualifier")) || 
/* 362 */       (paramString
/* 362 */       .equals("-output")) || 
/* 363 */       (paramString
/* 363 */       .equals("-sourcepath")) || 
/* 364 */       (paramString
/* 364 */       .equals("-tag")) || 
/* 365 */       (paramString
/* 365 */       .equals("-taglet")) || 
/* 366 */       (paramString
/* 366 */       .equals("-tagletpath")) || 
/* 367 */       (paramString
/* 367 */       .equals("-xprofilespath")))
/*     */     {
/* 368 */       return 2;
/* 369 */     }if ((paramString.equals("-group")) || 
/* 370 */       (paramString
/* 370 */       .equals("-linkoffline")))
/*     */     {
/* 371 */       return 3;
/*     */     }
/* 373 */     return -1;
/*     */   }
/*     */ 
/*     */   public abstract boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter);
/*     */ 
/*     */   private void initProfiles()
/*     */     throws IOException
/*     */   {
/* 387 */     if (this.profilespath.isEmpty()) {
/* 388 */       return;
/*     */     }
/* 390 */     this.profiles = Profiles.read(new File(this.profilespath));
/*     */ 
/* 394 */     EnumMap localEnumMap = new EnumMap(Profile.class);
/*     */     Object localObject2;
/* 396 */     for (localObject2 : Profile.values())
/* 397 */       localEnumMap.put(localObject2, new ArrayList());
/*     */     Object localObject3;
/* 399 */     for (localObject2 : this.packages) {
/* 400 */       if ((!this.nodeprecated) || (!Util.isDeprecated((Doc)localObject2)))
/*     */       {
/* 405 */         int k = this.profiles.getProfile(((PackageDoc)localObject2).name().replace(".", "/") + "/*");
/* 406 */         localObject3 = Profile.lookup(k);
/* 407 */         if (localObject3 != null) {
/* 408 */           List localList = (List)localEnumMap.get(localObject3);
/* 409 */           localList.add(localObject2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 414 */     this.profilePackages = new HashMap();
/* 415 */     ??? = Collections.emptyList();
/*     */ 
/* 417 */     for (Iterator localIterator = localEnumMap.entrySet().iterator(); localIterator.hasNext(); ) { localObject2 = (Map.Entry)localIterator.next();
/* 418 */       Profile localProfile = (Profile)((Map.Entry)localObject2).getKey();
/* 419 */       localObject3 = (List)((Map.Entry)localObject2).getValue();
/* 420 */       ((List)localObject3).addAll((Collection)???);
/* 421 */       Collections.sort((List)localObject3);
/* 422 */       ??? = ((List)localObject3).size();
/*     */ 
/* 425 */       if (??? > 0)
/* 426 */         this.profilePackages.put(localProfile.name, ((List)localObject3).toArray(new PackageDoc[((List)localObject3).size()]));
/* 427 */       ??? = localObject3;
/*     */     }
/*     */ 
/* 432 */     this.showProfiles = (!((List)???).isEmpty());
/*     */   }
/*     */ 
/*     */   private void initPackageArray() {
/* 436 */     HashSet localHashSet = new HashSet(Arrays.asList(this.root.specifiedPackages()));
/* 437 */     ClassDoc[] arrayOfClassDoc = this.root.specifiedClasses();
/* 438 */     for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 439 */       localHashSet.add(arrayOfClassDoc[i].containingPackage());
/*     */     }
/* 441 */     ArrayList localArrayList = new ArrayList(localHashSet);
/* 442 */     Collections.sort(localArrayList);
/* 443 */     this.packages = ((PackageDoc[])localArrayList.toArray(new PackageDoc[0]));
/*     */   }
/*     */ 
/*     */   public void setOptions(String[][] paramArrayOfString)
/*     */     throws Configuration.Fault
/*     */   {
/* 452 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/*     */     String[] arrayOfString;
/*     */     String str1;
/* 456 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 457 */       arrayOfString = paramArrayOfString[i];
/* 458 */       str1 = StringUtils.toLowerCase(arrayOfString[0]);
/* 459 */       if (str1.equals("-d")) {
/* 460 */         this.destDirName = addTrailingFileSep(arrayOfString[1]);
/* 461 */         this.docFileDestDirName = this.destDirName;
/* 462 */         ensureOutputDirExists();
/* 463 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 467 */     for (i = 0; i < paramArrayOfString.length; i++) {
/* 468 */       arrayOfString = paramArrayOfString[i];
/* 469 */       str1 = StringUtils.toLowerCase(arrayOfString[0]);
/* 470 */       if (str1.equals("-docfilessubdirs")) {
/* 471 */         this.copydocfilesubdirs = true;
/* 472 */       } else if (str1.equals("-docencoding")) {
/* 473 */         this.docencoding = arrayOfString[1];
/* 474 */       } else if (str1.equals("-encoding")) {
/* 475 */         this.encoding = arrayOfString[1];
/* 476 */       } else if (str1.equals("-author")) {
/* 477 */         this.showauthor = true;
/* 478 */       } else if (str1.equals("-javafx")) {
/* 479 */         this.javafx = true;
/* 480 */       } else if (str1.equals("-nosince")) {
/* 481 */         this.nosince = true;
/* 482 */       } else if (str1.equals("-version")) {
/* 483 */         this.showversion = true;
/* 484 */       } else if (str1.equals("-nodeprecated")) {
/* 485 */         this.nodeprecated = true;
/* 486 */       } else if (str1.equals("-sourcepath")) {
/* 487 */         this.sourcepath = arrayOfString[1];
/* 488 */       } else if (((str1.equals("-classpath")) || (str1.equals("-cp"))) && 
/* 489 */         (this.sourcepath
/* 489 */         .length() == 0)) {
/* 490 */         this.sourcepath = arrayOfString[1];
/* 491 */       } else if (str1.equals("-excludedocfilessubdir")) {
/* 492 */         addToSet(this.excludedDocFileDirs, arrayOfString[1]);
/* 493 */       } else if (str1.equals("-noqualifier")) {
/* 494 */         addToSet(this.excludedQualifiers, arrayOfString[1]);
/* 495 */       } else if (str1.equals("-linksource")) {
/* 496 */         this.linksource = true;
/* 497 */       } else if (str1.equals("-sourcetab")) {
/* 498 */         this.linksource = true;
/*     */         try {
/* 500 */           setTabWidth(Integer.parseInt(arrayOfString[1]));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException)
/*     */         {
/* 504 */           this.sourcetab = -1;
/*     */         }
/* 506 */         if (this.sourcetab <= 0) {
/* 507 */           this.message.warning("doclet.sourcetab_warning", new Object[0]);
/* 508 */           setTabWidth(8);
/*     */         }
/* 510 */       } else if (str1.equals("-notimestamp")) {
/* 511 */         this.notimestamp = true;
/* 512 */       } else if (str1.equals("-nocomment")) {
/* 513 */         this.nocomment = true;
/* 514 */       } else if ((str1.equals("-tag")) || (str1.equals("-taglet"))) {
/* 515 */         localLinkedHashSet.add(arrayOfString);
/* 516 */       } else if (str1.equals("-tagletpath")) {
/* 517 */         this.tagletpath = arrayOfString[1];
/* 518 */       } else if (str1.equals("-xprofilespath")) {
/* 519 */         this.profilespath = arrayOfString[1];
/* 520 */       } else if (str1.equals("-keywords")) {
/* 521 */         this.keywords = true;
/* 522 */       } else if (str1.equals("-serialwarn")) {
/* 523 */         this.serialwarn = true;
/* 524 */       } else if (str1.equals("-group")) {
/* 525 */         this.group.checkPackageGroups(arrayOfString[1], arrayOfString[2]);
/*     */       }
/*     */       else
/*     */       {
/*     */         String str2;
/* 526 */         if (str1.equals("-link")) {
/* 527 */           str2 = arrayOfString[1];
/* 528 */           this.extern.link(str2, str2, this.root, false);
/* 529 */         } else if (str1.equals("-linkoffline")) {
/* 530 */           str2 = arrayOfString[1];
/* 531 */           String str3 = arrayOfString[2];
/* 532 */           this.extern.link(str2, str3, this.root, true);
/*     */         }
/*     */       }
/*     */     }
/* 535 */     if (this.sourcepath.length() == 0) {
/* 536 */       this.sourcepath = (System.getProperty("env.class.path") == null ? "" : 
/* 537 */         System.getProperty("env.class.path"));
/*     */     }
/*     */ 
/* 539 */     if (this.docencoding == null) {
/* 540 */       this.docencoding = this.encoding;
/*     */     }
/*     */ 
/* 543 */     this.classDocCatalog = new ClassDocCatalog(this.root.specifiedClasses(), this);
/* 544 */     initTagletManager(localLinkedHashSet);
/*     */   }
/*     */ 
/*     */   public void setOptions()
/*     */     throws Configuration.Fault
/*     */   {
/* 553 */     initPackageArray();
/* 554 */     setOptions(this.root.options());
/*     */     try {
/* 556 */       initProfiles();
/*     */     } catch (Exception localException) {
/* 558 */       throw new DocletAbortException(localException);
/*     */     }
/* 560 */     setSpecificDocletOptions(this.root.options());
/*     */   }
/*     */ 
/*     */   private void ensureOutputDirExists() throws Configuration.Fault {
/* 564 */     DocFile localDocFile = DocFile.createFileForDirectory(this, this.destDirName);
/* 565 */     if (!localDocFile.exists())
/*     */     {
/* 567 */       this.root.printNotice(getText("doclet.dest_dir_create", this.destDirName));
/* 568 */       localDocFile.mkdirs(); } else {
/* 569 */       if (!localDocFile.isDirectory())
/* 570 */         throw new Fault(getText("doclet.destination_directory_not_directory_0", localDocFile
/* 572 */           .getPath()));
/* 573 */       if (!localDocFile.canWrite())
/* 574 */         throw new Fault(getText("doclet.destination_directory_not_writable_0", localDocFile
/* 576 */           .getPath()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initTagletManager(Set<String[]> paramSet)
/*     */   {
/* 588 */     this.tagletManager = (this.tagletManager == null ? new TagletManager(this.nosince, this.showversion, this.showauthor, this.javafx, this.message) : this.tagletManager);
/*     */ 
/* 592 */     for (Iterator localIterator = paramSet.iterator(); localIterator.hasNext(); ) {
/* 593 */       String[] arrayOfString1 = (String[])localIterator.next();
/* 594 */       if (arrayOfString1[0].equals("-taglet")) {
/* 595 */         this.tagletManager.addCustomTag(arrayOfString1[1], getFileManager(), this.tagletpath);
/*     */       }
/*     */       else {
/* 598 */         String[] arrayOfString2 = tokenize(arrayOfString1[1], ':', 3);
/*     */ 
/* 600 */         if (arrayOfString2.length == 1) {
/* 601 */           String str = arrayOfString1[1];
/* 602 */           if (this.tagletManager.isKnownCustomTag(str))
/*     */           {
/* 604 */             this.tagletManager.addNewSimpleCustomTag(str, null, "");
/*     */           }
/*     */           else {
/* 607 */             StringBuilder localStringBuilder = new StringBuilder(str + ":");
/* 608 */             localStringBuilder.setCharAt(0, Character.toUpperCase(str.charAt(0)));
/* 609 */             this.tagletManager.addNewSimpleCustomTag(str, localStringBuilder.toString(), "a");
/*     */           }
/* 611 */         } else if (arrayOfString2.length == 2)
/*     */         {
/* 613 */           this.tagletManager.addNewSimpleCustomTag(arrayOfString2[0], arrayOfString2[1], "");
/* 614 */         } else if (arrayOfString2.length >= 3) {
/* 615 */           this.tagletManager.addNewSimpleCustomTag(arrayOfString2[0], arrayOfString2[2], arrayOfString2[1]);
/*     */         } else {
/* 617 */           this.message.error("doclet.Error_invalid_custom_tag_argument", new Object[] { arrayOfString1[1] });
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String[] tokenize(String paramString, char paramChar, int paramInt)
/*     */   {
/* 636 */     ArrayList localArrayList = new ArrayList();
/* 637 */     StringBuilder localStringBuilder = new StringBuilder();
/* 638 */     int i = 0;
/* 639 */     for (int j = 0; j < paramString.length(); j += Character.charCount(j)) {
/* 640 */       char c = paramString.codePointAt(j);
/* 641 */       if (i != 0)
/*     */       {
/* 643 */         localStringBuilder.appendCodePoint(c);
/* 644 */         i = 0;
/* 645 */       } else if ((c == paramChar) && (localArrayList.size() < paramInt - 1))
/*     */       {
/* 647 */         localArrayList.add(localStringBuilder.toString());
/* 648 */         localStringBuilder = new StringBuilder();
/* 649 */       } else if (c == '\\')
/*     */       {
/* 651 */         i = 1;
/*     */       }
/*     */       else {
/* 654 */         localStringBuilder.appendCodePoint(c);
/*     */       }
/*     */     }
/* 657 */     if (localStringBuilder.length() > 0) {
/* 658 */       localArrayList.add(localStringBuilder.toString());
/*     */     }
/* 660 */     return (String[])localArrayList.toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   private void addToSet(Set<String> paramSet, String paramString) {
/* 664 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
/*     */ 
/* 666 */     while (localStringTokenizer.hasMoreTokens()) {
/* 667 */       String str = localStringTokenizer.nextToken();
/* 668 */       paramSet.add(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String addTrailingFileSep(String paramString)
/*     */   {
/* 681 */     String str1 = System.getProperty("file.separator");
/* 682 */     String str2 = str1 + str1;
/*     */     int i;
/* 684 */     while ((i = paramString.indexOf(str2, 1)) >= 0)
/*     */     {
/* 686 */       paramString = paramString.substring(0, i) + paramString
/* 686 */         .substring(i + str1
/* 686 */         .length());
/*     */     }
/* 688 */     if (!paramString.endsWith(str1))
/* 689 */       paramString = paramString + str1;
/* 690 */     return paramString;
/*     */   }
/*     */ 
/*     */   public boolean generalValidOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter)
/*     */   {
/* 708 */     int i = 0;
/* 709 */     String str1 = "";
/* 710 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 711 */       String[] arrayOfString = paramArrayOfString[j];
/* 712 */       String str2 = StringUtils.toLowerCase(arrayOfString[0]);
/* 713 */       if (str2.equals("-docencoding")) {
/* 714 */         i = 1;
/* 715 */         if (!checkOutputFileEncoding(arrayOfString[1], paramDocErrorReporter))
/* 716 */           return false;
/*     */       }
/* 718 */       else if (str2.equals("-encoding")) {
/* 719 */         str1 = arrayOfString[1];
/*     */       }
/*     */     }
/* 722 */     if ((i == 0) && (str1.length() > 0) && 
/* 723 */       (!checkOutputFileEncoding(str1, paramDocErrorReporter))) {
/* 724 */       return false;
/*     */     }
/*     */ 
/* 727 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean shouldDocumentProfile(String paramString)
/*     */   {
/* 738 */     return this.profilePackages.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   private boolean checkOutputFileEncoding(String paramString, DocErrorReporter paramDocErrorReporter)
/*     */   {
/* 750 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 751 */     OutputStreamWriter localOutputStreamWriter = null;
/*     */     try {
/* 753 */       localOutputStreamWriter = new OutputStreamWriter(localByteArrayOutputStream, paramString);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 755 */       paramDocErrorReporter.printError(getText("doclet.Encoding_not_supported", paramString));
/*     */ 
/* 757 */       return false;
/*     */     } finally {
/*     */       try {
/* 760 */         if (localOutputStreamWriter != null)
/* 761 */           localOutputStreamWriter.close();
/*     */       }
/*     */       catch (IOException localIOException3) {
/*     */       }
/*     */     }
/* 766 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean shouldExcludeDocFileDir(String paramString)
/*     */   {
/* 775 */     if (this.excludedDocFileDirs.contains(paramString)) {
/* 776 */       return true;
/*     */     }
/* 778 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean shouldExcludeQualifier(String paramString)
/*     */   {
/* 787 */     if ((this.excludedQualifiers.contains("all")) || 
/* 788 */       (this.excludedQualifiers
/* 788 */       .contains(paramString)) || 
/* 789 */       (this.excludedQualifiers
/* 789 */       .contains(paramString + ".*")))
/*     */     {
/* 790 */       return true;
/*     */     }
/* 792 */     int i = -1;
/* 793 */     while ((i = paramString.indexOf(".", i + 1)) != -1) {
/* 794 */       if (this.excludedQualifiers.contains(paramString.substring(0, i + 1) + "*")) {
/* 795 */         return true;
/*     */       }
/*     */     }
/* 798 */     return false;
/*     */   }
/*     */ 
/*     */   public String getClassName(ClassDoc paramClassDoc)
/*     */   {
/* 808 */     PackageDoc localPackageDoc = paramClassDoc.containingPackage();
/* 809 */     if ((localPackageDoc != null) && (shouldExcludeQualifier(paramClassDoc.containingPackage().name()))) {
/* 810 */       return paramClassDoc.name();
/*     */     }
/* 812 */     return paramClassDoc.qualifiedName();
/*     */   }
/*     */ 
/*     */   public String getText(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 819 */       return getDocletSpecificMsg().getText(paramString, new Object[0]);
/*     */     } catch (Exception localException) {
/*     */     }
/* 822 */     return this.message.getText(paramString, new Object[0]); } 
/*     */   public String getText(String paramString1, String paramString2) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 663	com/sun/tools/doclets/internal/toolkit/Configuration:getDocletSpecificMsg	()Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   4: aload_1
/*     */     //   5: iconst_1
/*     */     //   6: anewarray 365	java/lang/Object
/*     */     //   9: dup
/*     */     //   10: iconst_0
/*     */     //   11: aload_2
/*     */     //   12: aastore
/*     */     //   13: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   16: areturn
/*     */     //   17: astore_3
/*     */     //   18: aload_0
/*     */     //   19: getfield 641	com/sun/tools/doclets/internal/toolkit/Configuration:message	Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   22: aload_1
/*     */     //   23: iconst_1
/*     */     //   24: anewarray 365	java/lang/Object
/*     */     //   27: dup
/*     */     //   28: iconst_0
/*     */     //   29: aload_2
/*     */     //   30: aastore
/*     */     //   31: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   34: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	16	17	java/lang/Exception } 
/*     */   public String getText(String paramString1, String paramString2, String paramString3) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 663	com/sun/tools/doclets/internal/toolkit/Configuration:getDocletSpecificMsg	()Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   4: aload_1
/*     */     //   5: iconst_2
/*     */     //   6: anewarray 365	java/lang/Object
/*     */     //   9: dup
/*     */     //   10: iconst_0
/*     */     //   11: aload_2
/*     */     //   12: aastore
/*     */     //   13: dup
/*     */     //   14: iconst_1
/*     */     //   15: aload_3
/*     */     //   16: aastore
/*     */     //   17: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   20: areturn
/*     */     //   21: astore 4
/*     */     //   23: aload_0
/*     */     //   24: getfield 641	com/sun/tools/doclets/internal/toolkit/Configuration:message	Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   27: aload_1
/*     */     //   28: iconst_2
/*     */     //   29: anewarray 365	java/lang/Object
/*     */     //   32: dup
/*     */     //   33: iconst_0
/*     */     //   34: aload_2
/*     */     //   35: aastore
/*     */     //   36: dup
/*     */     //   37: iconst_1
/*     */     //   38: aload_3
/*     */     //   39: aastore
/*     */     //   40: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   43: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	20	21	java/lang/Exception } 
/*     */   public String getText(String paramString1, String paramString2, String paramString3, String paramString4) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 663	com/sun/tools/doclets/internal/toolkit/Configuration:getDocletSpecificMsg	()Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   4: aload_1
/*     */     //   5: iconst_3
/*     */     //   6: anewarray 365	java/lang/Object
/*     */     //   9: dup
/*     */     //   10: iconst_0
/*     */     //   11: aload_2
/*     */     //   12: aastore
/*     */     //   13: dup
/*     */     //   14: iconst_1
/*     */     //   15: aload_3
/*     */     //   16: aastore
/*     */     //   17: dup
/*     */     //   18: iconst_2
/*     */     //   19: aload 4
/*     */     //   21: aastore
/*     */     //   22: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   25: areturn
/*     */     //   26: astore 5
/*     */     //   28: aload_0
/*     */     //   29: getfield 641	com/sun/tools/doclets/internal/toolkit/Configuration:message	Lcom/sun/tools/doclets/internal/toolkit/util/MessageRetriever;
/*     */     //   32: aload_1
/*     */     //   33: iconst_3
/*     */     //   34: anewarray 365	java/lang/Object
/*     */     //   37: dup
/*     */     //   38: iconst_0
/*     */     //   39: aload_2
/*     */     //   40: aastore
/*     */     //   41: dup
/*     */     //   42: iconst_1
/*     */     //   43: aload_3
/*     */     //   44: aastore
/*     */     //   45: dup
/*     */     //   46: iconst_2
/*     */     //   47: aload 4
/*     */     //   49: aastore
/*     */     //   50: invokevirtual 701	com/sun/tools/doclets/internal/toolkit/util/MessageRetriever:getText	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   53: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	25	26	java/lang/Exception } 
/*     */   public abstract Content newContent();
/*     */ 
/* 865 */   public Content getResource(String paramString) { Content localContent = newContent();
/* 866 */     localContent.addContent(getText(paramString));
/* 867 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString, Object paramObject)
/*     */   {
/* 878 */     return getResource(paramString, paramObject, null, null);
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 889 */     return getResource(paramString, paramObject1, paramObject2, null);
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2, Object paramObject3)
/*     */   {
/* 901 */     Content localContent = newContent();
/* 902 */     Pattern localPattern = Pattern.compile("\\{([012])\\}");
/* 903 */     String str = getText(paramString);
/* 904 */     Matcher localMatcher = localPattern.matcher(str);
/* 905 */     int i = 0;
/* 906 */     while (localMatcher.find(i)) {
/* 907 */       localContent.addContent(str.substring(i, localMatcher.start()));
/*     */ 
/* 909 */       Object localObject = null;
/* 910 */       switch (localMatcher.group(1).charAt(0)) { case '0':
/* 911 */         localObject = paramObject1; break;
/*     */       case '1':
/* 912 */         localObject = paramObject2; break;
/*     */       case '2':
/* 913 */         localObject = paramObject3;
/*     */       }
/*     */ 
/* 916 */       if (localObject == null)
/* 917 */         localContent.addContent("{" + localMatcher.group(1) + "}");
/* 918 */       else if ((localObject instanceof String))
/* 919 */         localContent.addContent((String)localObject);
/* 920 */       else if ((localObject instanceof Content)) {
/* 921 */         localContent.addContent((Content)localObject);
/*     */       }
/*     */ 
/* 924 */       i = localMatcher.end();
/*     */     }
/*     */ 
/* 927 */     localContent.addContent(str.substring(i));
/* 928 */     return localContent;
/*     */   }
/*     */ 
/*     */   public boolean isGeneratedDoc(ClassDoc paramClassDoc)
/*     */   {
/* 941 */     if (!this.nodeprecated) {
/* 942 */       return true;
/*     */     }
/* 944 */     return (!Util.isDeprecated(paramClassDoc)) && (!Util.isDeprecated(paramClassDoc.containingPackage()));
/*     */   }
/*     */ 
/*     */   public abstract WriterFactory getWriterFactory();
/*     */ 
/*     */   public InputStream getBuilderXML()
/*     */     throws IOException
/*     */   {
/* 962 */     return this.builderXMLPath == null ? Configuration.class
/* 961 */       .getResourceAsStream("resources/doclet.xml") : 
/* 962 */       DocFile.createFileForInput(this, this.builderXMLPath)
/* 962 */       .openInputStream();
/*     */   }
/*     */ 
/*     */   public abstract Locale getLocale();
/*     */ 
/*     */   public abstract JavaFileManager getFileManager();
/*     */ 
/*     */   public abstract Comparator<ProgramElementDoc> getMemberComparator();
/*     */ 
/*     */   private void setTabWidth(int paramInt)
/*     */   {
/* 984 */     this.sourcetab = paramInt;
/* 985 */     this.tabSpaces = String.format("%" + paramInt + "s", new Object[] { "" });
/*     */   }
/*     */ 
/*     */   public abstract boolean showMessage(SourcePosition paramSourcePosition, String paramString);
/*     */ 
/*     */   public static class Fault extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     Fault(String paramString)
/*     */     {
/*  66 */       super();
/*     */     }
/*     */ 
/*     */     Fault(String paramString, Exception paramException) {
/*  70 */       super(paramException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.Configuration
 * JD-Core Version:    0.6.2
 */