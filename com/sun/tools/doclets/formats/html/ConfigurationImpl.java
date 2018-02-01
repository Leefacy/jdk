/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import com.sun.tools.javadoc.RootDocImpl;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ public class ConfigurationImpl extends Configuration
/*     */ {
/*  72 */   public static final String BUILD_DATE = System.getProperty("java.version");
/*     */ 
/*  77 */   public String header = "";
/*     */ 
/*  82 */   public String packagesheader = "";
/*     */ 
/*  87 */   public String footer = "";
/*     */ 
/*  92 */   public String doctitle = "";
/*     */ 
/*  97 */   public String windowtitle = "";
/*     */ 
/* 102 */   public String top = "";
/*     */ 
/* 107 */   public String bottom = "";
/*     */ 
/* 112 */   public String helpfile = "";
/*     */ 
/* 117 */   public String stylesheetfile = "";
/*     */ 
/* 122 */   public String docrootparent = "";
/*     */ 
/* 127 */   public boolean nohelp = false;
/*     */ 
/* 133 */   public boolean splitindex = false;
/*     */ 
/* 138 */   public boolean createindex = true;
/*     */ 
/* 143 */   public boolean classuse = false;
/*     */ 
/* 148 */   public boolean createtree = true;
/*     */ 
/* 154 */   public boolean nodeprecatedlist = false;
/*     */ 
/* 159 */   public boolean nonavbar = false;
/*     */ 
/* 165 */   private boolean nooverview = false;
/*     */ 
/* 170 */   public boolean overview = false;
/*     */ 
/* 176 */   public boolean createoverview = false;
/*     */ 
/* 181 */   public Set<String> doclintOpts = new LinkedHashSet();
/*     */   public final MessageRetriever standardmessage;
/* 192 */   public DocPath topFile = DocPath.empty;
/*     */ 
/* 197 */   public ClassDoc currentcd = null;
/*     */ 
/* 208 */   private final String versionRBName = "com.sun.tools.javadoc.resources.version";
/*     */   private ResourceBundle versionRB;
/*     */   private JavaFileManager fileManager;
/*     */ 
/*     */   public ConfigurationImpl()
/*     */   {
/* 204 */     this.standardmessage = new MessageRetriever(this, "com.sun.tools.doclets.formats.html.resources.standard");
/*     */   }
/*     */ 
/*     */   public String getDocletSpecificBuildDate()
/*     */   {
/* 216 */     if (this.versionRB == null) {
/*     */       try {
/* 218 */         this.versionRB = ResourceBundle.getBundle("com.sun.tools.javadoc.resources.version");
/*     */       } catch (MissingResourceException localMissingResourceException1) {
/* 220 */         return BUILD_DATE;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 225 */       return this.versionRB.getString("release"); } catch (MissingResourceException localMissingResourceException2) {
/*     */     }
/* 227 */     return BUILD_DATE;
/*     */   }
/*     */ 
/*     */   public void setSpecificDocletOptions(String[][] paramArrayOfString)
/*     */   {
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 239 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 240 */       localObject1 = paramArrayOfString[i];
/* 241 */       localObject2 = StringUtils.toLowerCase(localObject1[0]);
/* 242 */       if (((String)localObject2).equals("-footer"))
/* 243 */         this.footer = localObject1[1];
/* 244 */       else if (((String)localObject2).equals("-header"))
/* 245 */         this.header = localObject1[1];
/* 246 */       else if (((String)localObject2).equals("-packagesheader"))
/* 247 */         this.packagesheader = localObject1[1];
/* 248 */       else if (((String)localObject2).equals("-doctitle"))
/* 249 */         this.doctitle = localObject1[1];
/* 250 */       else if (((String)localObject2).equals("-windowtitle"))
/* 251 */         this.windowtitle = localObject1[1].replaceAll("\\<.*?>", "");
/* 252 */       else if (((String)localObject2).equals("-top"))
/* 253 */         this.top = localObject1[1];
/* 254 */       else if (((String)localObject2).equals("-bottom"))
/* 255 */         this.bottom = localObject1[1];
/* 256 */       else if (((String)localObject2).equals("-helpfile"))
/* 257 */         this.helpfile = localObject1[1];
/* 258 */       else if (((String)localObject2).equals("-stylesheetfile"))
/* 259 */         this.stylesheetfile = localObject1[1];
/* 260 */       else if (((String)localObject2).equals("-charset"))
/* 261 */         this.charset = localObject1[1];
/* 262 */       else if (((String)localObject2).equals("-xdocrootparent"))
/* 263 */         this.docrootparent = localObject1[1];
/* 264 */       else if (((String)localObject2).equals("-nohelp"))
/* 265 */         this.nohelp = true;
/* 266 */       else if (((String)localObject2).equals("-splitindex"))
/* 267 */         this.splitindex = true;
/* 268 */       else if (((String)localObject2).equals("-noindex"))
/* 269 */         this.createindex = false;
/* 270 */       else if (((String)localObject2).equals("-use"))
/* 271 */         this.classuse = true;
/* 272 */       else if (((String)localObject2).equals("-notree"))
/* 273 */         this.createtree = false;
/* 274 */       else if (((String)localObject2).equals("-nodeprecatedlist"))
/* 275 */         this.nodeprecatedlist = true;
/* 276 */       else if (((String)localObject2).equals("-nonavbar"))
/* 277 */         this.nonavbar = true;
/* 278 */       else if (((String)localObject2).equals("-nooverview"))
/* 279 */         this.nooverview = true;
/* 280 */       else if (((String)localObject2).equals("-overview"))
/* 281 */         this.overview = true;
/* 282 */       else if (((String)localObject2).equals("-xdoclint"))
/* 283 */         this.doclintOpts.add(null);
/* 284 */       else if (((String)localObject2).startsWith("-xdoclint:")) {
/* 285 */         this.doclintOpts.add(((String)localObject2).substring(((String)localObject2).indexOf(":") + 1));
/*     */       }
/*     */     }
/* 288 */     if (this.root.specifiedClasses().length > 0) {
/* 289 */       HashMap localHashMap = new HashMap();
/*     */ 
/* 291 */       localObject2 = this.root.classes();
/* 292 */       for (int j = 0; j < localObject2.length; j++) {
/* 293 */         localObject1 = localObject2[j].containingPackage();
/* 294 */         if (!localHashMap.containsKey(((PackageDoc)localObject1).name())) {
/* 295 */           localHashMap.put(((PackageDoc)localObject1).name(), localObject1);
/*     */         }
/*     */       }
/*     */     }
/* 299 */     setCreateOverview();
/* 300 */     setTopFile(this.root);
/*     */ 
/* 302 */     if ((this.root instanceof RootDocImpl))
/* 303 */       ((RootDocImpl)this.root).initDocLint(this.doclintOpts, this.tagletManager.getCustomTagNames());
/*     */   }
/*     */ 
/*     */   public int optionLength(String paramString)
/*     */   {
/* 324 */     int i = -1;
/* 325 */     if ((i = super.optionLength(paramString)) > 0) {
/* 326 */       return i;
/*     */     }
/*     */ 
/* 329 */     paramString = StringUtils.toLowerCase(paramString);
/* 330 */     if ((paramString.equals("-nodeprecatedlist")) || 
/* 331 */       (paramString
/* 331 */       .equals("-noindex")) || 
/* 332 */       (paramString
/* 332 */       .equals("-notree")) || 
/* 333 */       (paramString
/* 333 */       .equals("-nohelp")) || 
/* 334 */       (paramString
/* 334 */       .equals("-splitindex")) || 
/* 335 */       (paramString
/* 335 */       .equals("-serialwarn")) || 
/* 336 */       (paramString
/* 336 */       .equals("-use")) || 
/* 337 */       (paramString
/* 337 */       .equals("-nonavbar")) || 
/* 338 */       (paramString
/* 338 */       .equals("-nooverview")) || 
/* 339 */       (paramString
/* 339 */       .equals("-xdoclint")) || 
/* 340 */       (paramString
/* 340 */       .startsWith("-xdoclint:")))
/*     */     {
/* 341 */       return 1;
/* 342 */     }if (paramString.equals("-help"))
/*     */     {
/* 347 */       System.out.println(getText("doclet.usage"));
/* 348 */       return 1;
/* 349 */     }if (paramString.equals("-x"))
/*     */     {
/* 354 */       System.out.println(getText("doclet.X.usage"));
/* 355 */       return 1;
/* 356 */     }if ((paramString.equals("-footer")) || 
/* 357 */       (paramString
/* 357 */       .equals("-header")) || 
/* 358 */       (paramString
/* 358 */       .equals("-packagesheader")) || 
/* 359 */       (paramString
/* 359 */       .equals("-doctitle")) || 
/* 360 */       (paramString
/* 360 */       .equals("-windowtitle")) || 
/* 361 */       (paramString
/* 361 */       .equals("-top")) || 
/* 362 */       (paramString
/* 362 */       .equals("-bottom")) || 
/* 363 */       (paramString
/* 363 */       .equals("-helpfile")) || 
/* 364 */       (paramString
/* 364 */       .equals("-stylesheetfile")) || 
/* 365 */       (paramString
/* 365 */       .equals("-charset")) || 
/* 366 */       (paramString
/* 366 */       .equals("-overview")) || 
/* 367 */       (paramString
/* 367 */       .equals("-xdocrootparent")))
/*     */     {
/* 368 */       return 2;
/*     */     }
/* 370 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter)
/*     */   {
/* 380 */     int i = 0;
/* 381 */     int j = 0;
/* 382 */     int k = 0;
/* 383 */     int m = 0;
/* 384 */     int n = 0;
/* 385 */     int i1 = 0;
/*     */ 
/* 387 */     if (!generalValidOptions(paramArrayOfString, paramDocErrorReporter)) {
/* 388 */       return false;
/*     */     }
/*     */ 
/* 391 */     for (int i2 = 0; i2 < paramArrayOfString.length; i2++) {
/* 392 */       String[] arrayOfString = paramArrayOfString[i2];
/* 393 */       String str = StringUtils.toLowerCase(arrayOfString[0]);
/* 394 */       if (str.equals("-helpfile")) {
/* 395 */         if (j == 1) {
/* 396 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-helpfile", "-nohelp"));
/*     */ 
/* 398 */           return false;
/*     */         }
/* 400 */         if (i == 1) {
/* 401 */           paramDocErrorReporter.printError(getText("doclet.Option_reuse", "-helpfile"));
/*     */ 
/* 403 */           return false;
/*     */         }
/* 405 */         DocFile localDocFile = DocFile.createFileForInput(this, arrayOfString[1]);
/* 406 */         if (!localDocFile.exists()) {
/* 407 */           paramDocErrorReporter.printError(getText("doclet.File_not_found", arrayOfString[1]));
/* 408 */           return false;
/*     */         }
/* 410 */         i = 1;
/* 411 */       } else if (str.equals("-nohelp")) {
/* 412 */         if (i == 1) {
/* 413 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-nohelp", "-helpfile"));
/*     */ 
/* 415 */           return false;
/*     */         }
/* 417 */         j = 1;
/* 418 */       } else if (str.equals("-xdocrootparent")) {
/*     */         try {
/* 420 */           new URL(arrayOfString[1]);
/*     */         } catch (MalformedURLException localMalformedURLException) {
/* 422 */           paramDocErrorReporter.printError(getText("doclet.MalformedURL", arrayOfString[1]));
/* 423 */           return false;
/*     */         }
/* 425 */       } else if (str.equals("-overview")) {
/* 426 */         if (m == 1) {
/* 427 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-overview", "-nooverview"));
/*     */ 
/* 429 */           return false;
/*     */         }
/* 431 */         if (k == 1) {
/* 432 */           paramDocErrorReporter.printError(getText("doclet.Option_reuse", "-overview"));
/*     */ 
/* 434 */           return false;
/*     */         }
/* 436 */         k = 1;
/* 437 */       } else if (str.equals("-nooverview")) {
/* 438 */         if (k == 1) {
/* 439 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-nooverview", "-overview"));
/*     */ 
/* 441 */           return false;
/*     */         }
/* 443 */         m = 1;
/* 444 */       } else if (str.equals("-splitindex")) {
/* 445 */         if (i1 == 1) {
/* 446 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-splitindex", "-noindex"));
/*     */ 
/* 448 */           return false;
/*     */         }
/* 450 */         n = 1;
/* 451 */       } else if (str.equals("-noindex")) {
/* 452 */         if (n == 1) {
/* 453 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-noindex", "-splitindex"));
/*     */ 
/* 455 */           return false;
/*     */         }
/* 457 */         i1 = 1;
/* 458 */       } else if (str.startsWith("-xdoclint:")) {
/* 459 */         if (str.contains("/")) {
/* 460 */           paramDocErrorReporter.printError(getText("doclet.Option_doclint_no_qualifiers"));
/* 461 */           return false;
/*     */         }
/* 463 */         if (!DocLint.isValidOption(str
/* 464 */           .replace("-xdoclint:", "-Xmsgs:")))
/*     */         {
/* 465 */           paramDocErrorReporter.printError(getText("doclet.Option_doclint_invalid_arg"));
/* 466 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   public MessageRetriever getDocletSpecificMsg()
/*     */   {
/* 478 */     return this.standardmessage;
/*     */   }
/*     */ 
/*     */   protected void setTopFile(RootDoc paramRootDoc)
/*     */   {
/* 492 */     if (!checkForDeprecation(paramRootDoc)) {
/* 493 */       return;
/*     */     }
/* 495 */     if (this.createoverview) {
/* 496 */       this.topFile = DocPaths.OVERVIEW_SUMMARY;
/*     */     }
/* 498 */     else if ((this.packages.length == 1) && (this.packages[0].name().equals(""))) {
/* 499 */       if (paramRootDoc.classes().length > 0) {
/* 500 */         ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 501 */         Arrays.sort(arrayOfClassDoc);
/* 502 */         ClassDoc localClassDoc = getValidClass(arrayOfClassDoc);
/* 503 */         this.topFile = DocPath.forClass(localClassDoc);
/*     */       }
/*     */     }
/* 506 */     else this.topFile = DocPath.forPackage(this.packages[0]).resolve(DocPaths.PACKAGE_SUMMARY);
/*     */   }
/*     */ 
/*     */   protected ClassDoc getValidClass(ClassDoc[] paramArrayOfClassDoc)
/*     */   {
/* 512 */     if (!this.nodeprecated) {
/* 513 */       return paramArrayOfClassDoc[0];
/*     */     }
/* 515 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++) {
/* 516 */       if (paramArrayOfClassDoc[i].tags("deprecated").length == 0) {
/* 517 */         return paramArrayOfClassDoc[i];
/*     */       }
/*     */     }
/* 520 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean checkForDeprecation(RootDoc paramRootDoc) {
/* 524 */     ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 525 */     for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 526 */       if (isGeneratedDoc(arrayOfClassDoc[i])) {
/* 527 */         return true;
/*     */       }
/*     */     }
/* 530 */     return false;
/*     */   }
/*     */ 
/*     */   protected void setCreateOverview()
/*     */   {
/* 538 */     if (((this.overview) || (this.packages.length > 1)) && (!this.nooverview))
/* 539 */       this.createoverview = true;
/*     */   }
/*     */ 
/*     */   public WriterFactory getWriterFactory()
/*     */   {
/* 548 */     return new WriterFactoryImpl(this);
/*     */   }
/*     */ 
/*     */   public Comparator<ProgramElementDoc> getMemberComparator()
/*     */   {
/* 556 */     return null;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 564 */     if ((this.root instanceof RootDocImpl)) {
/* 565 */       return ((RootDocImpl)this.root).getLocale();
/*     */     }
/* 567 */     return Locale.getDefault();
/*     */   }
/*     */ 
/*     */   public JavaFileManager getFileManager()
/*     */   {
/* 575 */     if (this.fileManager == null) {
/* 576 */       if ((this.root instanceof RootDocImpl))
/* 577 */         this.fileManager = ((RootDocImpl)this.root).getFileManager();
/*     */       else
/* 579 */         this.fileManager = new JavacFileManager(new Context(), false, null);
/*     */     }
/* 581 */     return this.fileManager;
/*     */   }
/*     */ 
/*     */   public boolean showMessage(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 588 */     if ((this.root instanceof RootDocImpl)) {
/* 589 */       return (paramSourcePosition == null) || (((RootDocImpl)this.root).showTagMessages());
/*     */     }
/* 591 */     return true;
/*     */   }
/*     */ 
/*     */   public Content newContent()
/*     */   {
/* 596 */     return new ContentBuilder();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.ConfigurationImpl
 * JD-Core Version:    0.6.2
 */