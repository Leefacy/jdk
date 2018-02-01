/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.tools.DocumentationTool.Location;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ public class TagletManager
/*     */ {
/*     */   public static final char SIMPLE_TAGLET_OPT_SEPARATOR = ':';
/*     */   public static final String ALT_SIMPLE_TAGLET_OPT_SEPARATOR = "-";
/*     */   private LinkedHashMap<String, Taglet> customTags;
/*     */   private Taglet[] packageTags;
/*     */   private Taglet[] typeTags;
/*     */   private Taglet[] fieldTags;
/*     */   private Taglet[] constructorTags;
/*     */   private Taglet[] methodTags;
/*     */   private Taglet[] overviewTags;
/*     */   private Taglet[] inlineTags;
/*     */   private Taglet[] serializedFormTags;
/*     */   private MessageRetriever message;
/*     */   private Set<String> standardTags;
/*     */   private Set<String> standardTagsLowercase;
/*     */   private Set<String> overridenStandardTags;
/*     */   private Set<String> potentiallyConflictingTags;
/*     */   private Set<String> unseenCustomTags;
/*     */   private boolean nosince;
/*     */   private boolean showversion;
/*     */   private boolean showauthor;
/*     */   private boolean javafx;
/*     */ 
/*     */   public TagletManager(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, MessageRetriever paramMessageRetriever)
/*     */   {
/* 176 */     this.overridenStandardTags = new HashSet();
/* 177 */     this.potentiallyConflictingTags = new HashSet();
/* 178 */     this.standardTags = new HashSet();
/* 179 */     this.standardTagsLowercase = new HashSet();
/* 180 */     this.unseenCustomTags = new HashSet();
/* 181 */     this.customTags = new LinkedHashMap();
/* 182 */     this.nosince = paramBoolean1;
/* 183 */     this.showversion = paramBoolean2;
/* 184 */     this.showauthor = paramBoolean3;
/* 185 */     this.javafx = paramBoolean4;
/* 186 */     this.message = paramMessageRetriever;
/* 187 */     initStandardTaglets();
/* 188 */     initStandardTagsLowercase();
/*     */   }
/*     */ 
/*     */   public void addCustomTag(Taglet paramTaglet)
/*     */   {
/* 199 */     if (paramTaglet != null) {
/* 200 */       String str = paramTaglet.getName();
/* 201 */       if (this.customTags.containsKey(str)) {
/* 202 */         this.customTags.remove(str);
/*     */       }
/* 204 */       this.customTags.put(str, paramTaglet);
/* 205 */       checkTagName(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<String> getCustomTagNames() {
/* 210 */     return this.customTags.keySet();
/*     */   }
/*     */ 
/*     */   public void addCustomTag(String paramString1, JavaFileManager paramJavaFileManager, String paramString2)
/*     */   {
/*     */     try
/*     */     {
/* 221 */       Class localClass = null;
/*     */ 
/* 223 */       String str = null;
/*     */       Object localObject1;
/* 226 */       if ((paramJavaFileManager != null) && (paramJavaFileManager.hasLocation(DocumentationTool.Location.TAGLET_PATH))) {
/* 227 */         localObject1 = paramJavaFileManager.getClassLoader(DocumentationTool.Location.TAGLET_PATH);
/*     */       }
/*     */       else {
/* 230 */         str = appendPath(System.getProperty("env.class.path"), str);
/* 231 */         str = appendPath(System.getProperty("java.class.path"), str);
/* 232 */         str = appendPath(paramString2, str);
/* 233 */         localObject1 = new URLClassLoader(pathToURLs(str));
/*     */       }
/*     */ 
/* 236 */       localClass = ((ClassLoader)localObject1).loadClass(paramString1);
/* 237 */       Method localMethod = localClass.getMethod("register", new Class[] { Map.class });
/*     */ 
/* 239 */       Object[] arrayOfObject = this.customTags.values().toArray();
/* 240 */       Object localObject2 = (arrayOfObject != null) && (arrayOfObject.length > 0) ? (Taglet)arrayOfObject[(arrayOfObject.length - 1)] : null;
/*     */ 
/* 242 */       localMethod.invoke(null, new Object[] { this.customTags });
/* 243 */       arrayOfObject = this.customTags.values().toArray();
/* 244 */       Object localObject3 = (arrayOfObject != null) && (arrayOfObject.length > 0) ? arrayOfObject[(arrayOfObject.length - 1)] : null;
/*     */ 
/* 246 */       if (localObject2 != localObject3)
/*     */       {
/* 250 */         this.message.notice("doclet.Notice_taglet_registered", new Object[] { paramString1 });
/* 251 */         if (localObject3 != null)
/* 252 */           checkTaglet(localObject3);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 256 */       this.message.error("doclet.Error_taglet_not_registered", new Object[] { localException.getClass().getName(), paramString1 });
/*     */     }
/*     */   }
/*     */ 
/*     */   private String appendPath(String paramString1, String paramString2)
/*     */   {
/* 262 */     if ((paramString1 == null) || (paramString1.length() == 0))
/* 263 */       return paramString2 == null ? "." : paramString2;
/* 264 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/* 265 */       return paramString1;
/*     */     }
/* 267 */     return paramString1 + File.pathSeparator + paramString2;
/*     */   }
/*     */ 
/*     */   private URL[] pathToURLs(String paramString)
/*     */   {
/* 279 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 280 */     for (String str : paramString.split(File.pathSeparator)) {
/* 281 */       if (!str.isEmpty())
/*     */         try {
/* 283 */           localLinkedHashSet.add(new File(str).getAbsoluteFile().toURI().toURL());
/*     */         } catch (MalformedURLException localMalformedURLException) {
/* 285 */           this.message.error("doclet.MalformedURL", new Object[] { str });
/*     */         }
/*     */     }
/* 288 */     return (URL[])localLinkedHashSet.toArray(new URL[localLinkedHashSet.size()]);
/*     */   }
/*     */ 
/*     */   public void addNewSimpleCustomTag(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 304 */     if ((paramString1 == null) || (paramString3 == null)) {
/* 305 */       return;
/*     */     }
/* 307 */     Taglet localTaglet = (Taglet)this.customTags.get(paramString1);
/* 308 */     paramString3 = StringUtils.toLowerCase(paramString3);
/* 309 */     if ((localTaglet == null) || (paramString2 != null)) {
/* 310 */       this.customTags.remove(paramString1);
/* 311 */       this.customTags.put(paramString1, new SimpleTaglet(paramString1, paramString2, paramString3));
/* 312 */       if ((paramString3 != null) && (paramString3.indexOf('x') == -1))
/* 313 */         checkTagName(paramString1);
/*     */     }
/*     */     else
/*     */     {
/* 317 */       this.customTags.remove(paramString1);
/* 318 */       this.customTags.put(paramString1, localTaglet);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkTagName(String paramString)
/*     */   {
/* 326 */     if (this.standardTags.contains(paramString)) {
/* 327 */       this.overridenStandardTags.add(paramString);
/*     */     } else {
/* 329 */       if (paramString.indexOf('.') == -1) {
/* 330 */         this.potentiallyConflictingTags.add(paramString);
/*     */       }
/* 332 */       this.unseenCustomTags.add(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkTaglet(Object paramObject)
/*     */   {
/* 341 */     if ((paramObject instanceof Taglet)) {
/* 342 */       checkTagName(((Taglet)paramObject).getName());
/* 343 */     } else if ((paramObject instanceof com.sun.tools.doclets.Taglet)) {
/* 344 */       com.sun.tools.doclets.Taglet localTaglet = (com.sun.tools.doclets.Taglet)paramObject;
/* 345 */       this.customTags.remove(localTaglet.getName());
/* 346 */       this.customTags.put(localTaglet.getName(), new LegacyTaglet(localTaglet));
/* 347 */       checkTagName(localTaglet.getName());
/*     */     } else {
/* 349 */       throw new IllegalArgumentException("Given object is not a taglet.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void seenCustomTag(String paramString)
/*     */   {
/* 359 */     this.unseenCustomTags.remove(paramString);
/*     */   }
/*     */ 
/*     */   public void checkTags(Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean)
/*     */   {
/* 369 */     if (paramArrayOfTag == null) {
/* 370 */       return;
/*     */     }
/*     */ 
/* 373 */     for (int i = 0; i < paramArrayOfTag.length; i++) {
/* 374 */       String str = paramArrayOfTag[i].name();
/* 375 */       if ((str.length() > 0) && (str.charAt(0) == '@')) {
/* 376 */         str = str.substring(1, str.length());
/*     */       }
/* 378 */       if ((!this.standardTags.contains(str)) && (!this.customTags.containsKey(str))) {
/* 379 */         if (this.standardTagsLowercase.contains(StringUtils.toLowerCase(str))) {
/* 380 */           this.message.warning(paramArrayOfTag[i].position(), "doclet.UnknownTagLowercase", new Object[] { paramArrayOfTag[i].name() });
/*     */         }
/*     */         else
/* 383 */           this.message.warning(paramArrayOfTag[i].position(), "doclet.UnknownTag", new Object[] { paramArrayOfTag[i].name() });
/*     */       }
/*     */       else
/*     */       {
/*     */         Taglet localTaglet;
/* 388 */         if ((localTaglet = (Taglet)this.customTags.get(str)) != null) {
/* 389 */           if ((paramBoolean) && (!localTaglet.isInlineTag())) {
/* 390 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "inline");
/*     */           }
/* 392 */           if (((paramDoc instanceof RootDoc)) && (!localTaglet.inOverview()))
/* 393 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "overview");
/* 394 */           else if (((paramDoc instanceof PackageDoc)) && (!localTaglet.inPackage()))
/* 395 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "package");
/* 396 */           else if (((paramDoc instanceof ClassDoc)) && (!localTaglet.inType()))
/* 397 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "class");
/* 398 */           else if (((paramDoc instanceof ConstructorDoc)) && (!localTaglet.inConstructor()))
/* 399 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "constructor");
/* 400 */           else if (((paramDoc instanceof FieldDoc)) && (!localTaglet.inField()))
/* 401 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "field");
/* 402 */           else if (((paramDoc instanceof MethodDoc)) && (!localTaglet.inMethod()))
/* 403 */             printTagMisuseWarn(localTaglet, paramArrayOfTag[i], "method");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printTagMisuseWarn(Taglet paramTaglet, Tag paramTag, String paramString)
/*     */   {
/* 417 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 418 */     if (paramTaglet.inOverview()) {
/* 419 */       localLinkedHashSet.add("overview");
/*     */     }
/* 421 */     if (paramTaglet.inPackage()) {
/* 422 */       localLinkedHashSet.add("package");
/*     */     }
/* 424 */     if (paramTaglet.inType()) {
/* 425 */       localLinkedHashSet.add("class/interface");
/*     */     }
/* 427 */     if (paramTaglet.inConstructor()) {
/* 428 */       localLinkedHashSet.add("constructor");
/*     */     }
/* 430 */     if (paramTaglet.inField()) {
/* 431 */       localLinkedHashSet.add("field");
/*     */     }
/* 433 */     if (paramTaglet.inMethod()) {
/* 434 */       localLinkedHashSet.add("method");
/*     */     }
/* 436 */     if (paramTaglet.isInlineTag()) {
/* 437 */       localLinkedHashSet.add("inline text");
/*     */     }
/* 439 */     String[] arrayOfString = (String[])localLinkedHashSet.toArray(new String[0]);
/* 440 */     if ((arrayOfString == null) || (arrayOfString.length == 0))
/*     */     {
/* 442 */       return;
/*     */     }
/* 444 */     StringBuilder localStringBuilder = new StringBuilder();
/* 445 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 446 */       if (i > 0) {
/* 447 */         localStringBuilder.append(", ");
/*     */       }
/* 449 */       localStringBuilder.append(arrayOfString[i]);
/*     */     }
/* 451 */     this.message.warning(paramTag.position(), "doclet.tag_misuse", new Object[] { "@" + paramTaglet
/* 452 */       .getName(), paramString, localStringBuilder.toString() });
/*     */   }
/*     */ 
/*     */   public Taglet[] getPackageCustomTaglets()
/*     */   {
/* 462 */     if (this.packageTags == null) {
/* 463 */       initCustomTagletArrays();
/*     */     }
/* 465 */     return this.packageTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getTypeCustomTaglets()
/*     */   {
/* 475 */     if (this.typeTags == null) {
/* 476 */       initCustomTagletArrays();
/*     */     }
/* 478 */     return this.typeTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getInlineCustomTaglets()
/*     */   {
/* 488 */     if (this.inlineTags == null) {
/* 489 */       initCustomTagletArrays();
/*     */     }
/* 491 */     return this.inlineTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getFieldCustomTaglets()
/*     */   {
/* 501 */     if (this.fieldTags == null) {
/* 502 */       initCustomTagletArrays();
/*     */     }
/* 504 */     return this.fieldTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getSerializedFormTaglets()
/*     */   {
/* 514 */     if (this.serializedFormTags == null) {
/* 515 */       initCustomTagletArrays();
/*     */     }
/* 517 */     return this.serializedFormTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getCustomTaglets(Doc paramDoc)
/*     */   {
/* 525 */     if ((paramDoc instanceof ConstructorDoc))
/* 526 */       return getConstructorCustomTaglets();
/* 527 */     if ((paramDoc instanceof MethodDoc))
/* 528 */       return getMethodCustomTaglets();
/* 529 */     if ((paramDoc instanceof FieldDoc))
/* 530 */       return getFieldCustomTaglets();
/* 531 */     if ((paramDoc instanceof ClassDoc))
/* 532 */       return getTypeCustomTaglets();
/* 533 */     if ((paramDoc instanceof PackageDoc))
/* 534 */       return getPackageCustomTaglets();
/* 535 */     if ((paramDoc instanceof RootDoc)) {
/* 536 */       return getOverviewCustomTaglets();
/*     */     }
/* 538 */     return null;
/*     */   }
/*     */ 
/*     */   public Taglet[] getConstructorCustomTaglets()
/*     */   {
/* 548 */     if (this.constructorTags == null) {
/* 549 */       initCustomTagletArrays();
/*     */     }
/* 551 */     return this.constructorTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getMethodCustomTaglets()
/*     */   {
/* 561 */     if (this.methodTags == null) {
/* 562 */       initCustomTagletArrays();
/*     */     }
/* 564 */     return this.methodTags;
/*     */   }
/*     */ 
/*     */   public Taglet[] getOverviewCustomTaglets()
/*     */   {
/* 574 */     if (this.overviewTags == null) {
/* 575 */       initCustomTagletArrays();
/*     */     }
/* 577 */     return this.overviewTags;
/*     */   }
/*     */ 
/*     */   private void initCustomTagletArrays()
/*     */   {
/* 584 */     Iterator localIterator = this.customTags.values().iterator();
/* 585 */     ArrayList localArrayList1 = new ArrayList(this.customTags.size());
/* 586 */     ArrayList localArrayList2 = new ArrayList(this.customTags.size());
/* 587 */     ArrayList localArrayList3 = new ArrayList(this.customTags.size());
/* 588 */     ArrayList localArrayList4 = new ArrayList(this.customTags.size());
/* 589 */     ArrayList localArrayList5 = new ArrayList(this.customTags.size());
/* 590 */     ArrayList localArrayList6 = new ArrayList(this.customTags.size());
/* 591 */     ArrayList localArrayList7 = new ArrayList(this.customTags.size());
/* 592 */     ArrayList localArrayList8 = new ArrayList();
/*     */ 
/* 594 */     while (localIterator.hasNext()) {
/* 595 */       Taglet localTaglet = (Taglet)localIterator.next();
/* 596 */       if ((localTaglet.inPackage()) && (!localTaglet.isInlineTag())) {
/* 597 */         localArrayList1.add(localTaglet);
/*     */       }
/* 599 */       if ((localTaglet.inType()) && (!localTaglet.isInlineTag())) {
/* 600 */         localArrayList2.add(localTaglet);
/*     */       }
/* 602 */       if ((localTaglet.inField()) && (!localTaglet.isInlineTag())) {
/* 603 */         localArrayList3.add(localTaglet);
/*     */       }
/* 605 */       if ((localTaglet.inConstructor()) && (!localTaglet.isInlineTag())) {
/* 606 */         localArrayList4.add(localTaglet);
/*     */       }
/* 608 */       if ((localTaglet.inMethod()) && (!localTaglet.isInlineTag())) {
/* 609 */         localArrayList5.add(localTaglet);
/*     */       }
/* 611 */       if (localTaglet.isInlineTag()) {
/* 612 */         localArrayList6.add(localTaglet);
/*     */       }
/* 614 */       if ((localTaglet.inOverview()) && (!localTaglet.isInlineTag())) {
/* 615 */         localArrayList7.add(localTaglet);
/*     */       }
/*     */     }
/* 618 */     this.packageTags = ((Taglet[])localArrayList1.toArray(new Taglet[0]));
/* 619 */     this.typeTags = ((Taglet[])localArrayList2.toArray(new Taglet[0]));
/* 620 */     this.fieldTags = ((Taglet[])localArrayList3.toArray(new Taglet[0]));
/* 621 */     this.constructorTags = ((Taglet[])localArrayList4.toArray(new Taglet[0]));
/* 622 */     this.methodTags = ((Taglet[])localArrayList5.toArray(new Taglet[0]));
/* 623 */     this.overviewTags = ((Taglet[])localArrayList7.toArray(new Taglet[0]));
/* 624 */     this.inlineTags = ((Taglet[])localArrayList6.toArray(new Taglet[0]));
/*     */ 
/* 627 */     localArrayList8.add(this.customTags.get("serialData"));
/* 628 */     localArrayList8.add(this.customTags.get("throws"));
/* 629 */     if (!this.nosince)
/* 630 */       localArrayList8.add(this.customTags.get("since"));
/* 631 */     localArrayList8.add(this.customTags.get("see"));
/* 632 */     this.serializedFormTags = ((Taglet[])localArrayList8.toArray(new Taglet[0]));
/*     */   }
/*     */ 
/*     */   private void initStandardTaglets()
/*     */   {
/* 639 */     if (this.javafx) {
/* 640 */       initJavaFXTaglets();
/*     */     }
/*     */ 
/* 644 */     addStandardTaglet(new ParamTaglet());
/* 645 */     addStandardTaglet(new ReturnTaglet());
/* 646 */     addStandardTaglet(new ThrowsTaglet());
/* 647 */     addStandardTaglet(new SimpleTaglet("exception", null, "mc"));
/*     */ 
/* 649 */     addStandardTaglet(!this.nosince, new SimpleTaglet("since", this.message.getText("doclet.Since", new Object[0]), "a"));
/*     */ 
/* 651 */     addStandardTaglet(this.showversion, new SimpleTaglet("version", this.message.getText("doclet.Version", new Object[0]), "pto"));
/*     */ 
/* 653 */     addStandardTaglet(this.showauthor, new SimpleTaglet("author", this.message.getText("doclet.Author", new Object[0]), "pto"));
/*     */ 
/* 655 */     addStandardTaglet(new SimpleTaglet("serialData", this.message.getText("doclet.SerialData", new Object[0]), "x"));
/*     */     SimpleTaglet localSimpleTaglet;
/* 657 */     this.customTags.put((localSimpleTaglet = new SimpleTaglet("factory", this.message.getText("doclet.Factory", new Object[0]), "m"))
/* 658 */       .getName(), localSimpleTaglet);
/* 659 */     addStandardTaglet(new SeeTaglet());
/*     */ 
/* 661 */     addStandardTaglet(new DocRootTaglet());
/* 662 */     addStandardTaglet(new InheritDocTaglet());
/* 663 */     addStandardTaglet(new ValueTaglet());
/* 664 */     addStandardTaglet(new LiteralTaglet());
/* 665 */     addStandardTaglet(new CodeTaglet());
/*     */ 
/* 670 */     this.standardTags.add("deprecated");
/* 671 */     this.standardTags.add("link");
/* 672 */     this.standardTags.add("linkplain");
/* 673 */     this.standardTags.add("serial");
/* 674 */     this.standardTags.add("serialField");
/* 675 */     this.standardTags.add("Text");
/*     */   }
/*     */ 
/*     */   private void initJavaFXTaglets()
/*     */   {
/* 682 */     addStandardTaglet(new PropertyGetterTaglet());
/* 683 */     addStandardTaglet(new PropertySetterTaglet());
/* 684 */     addStandardTaglet(new SimpleTaglet("propertyDescription", this.message
/* 685 */       .getText("doclet.PropertyDescription", new Object[0]), 
/* 685 */       "fm"));
/*     */ 
/* 687 */     addStandardTaglet(new SimpleTaglet("defaultValue", this.message.getText("doclet.DefaultValue", new Object[0]), "fm"));
/*     */ 
/* 689 */     addStandardTaglet(new SimpleTaglet("treatAsPrivate", null, "fmt"));
/*     */   }
/*     */ 
/*     */   void addStandardTaglet(Taglet paramTaglet)
/*     */   {
/* 694 */     String str = paramTaglet.getName();
/* 695 */     this.customTags.put(str, paramTaglet);
/* 696 */     this.standardTags.add(str);
/*     */   }
/*     */ 
/*     */   void addStandardTaglet(boolean paramBoolean, Taglet paramTaglet) {
/* 700 */     String str = paramTaglet.getName();
/* 701 */     if (paramBoolean)
/* 702 */       this.customTags.put(str, paramTaglet);
/* 703 */     this.standardTags.add(str);
/*     */   }
/*     */ 
/*     */   private void initStandardTagsLowercase()
/*     */   {
/* 710 */     Iterator localIterator = this.standardTags.iterator();
/* 711 */     while (localIterator.hasNext())
/* 712 */       this.standardTagsLowercase.add(StringUtils.toLowerCase((String)localIterator.next()));
/*     */   }
/*     */ 
/*     */   public boolean isKnownCustomTag(String paramString)
/*     */   {
/* 717 */     return this.customTags.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public void printReport()
/*     */   {
/* 726 */     printReportHelper("doclet.Notice_taglet_conflict_warn", this.potentiallyConflictingTags);
/* 727 */     printReportHelper("doclet.Notice_taglet_overriden", this.overridenStandardTags);
/* 728 */     printReportHelper("doclet.Notice_taglet_unseen", this.unseenCustomTags);
/*     */   }
/*     */ 
/*     */   private void printReportHelper(String paramString, Set<String> paramSet) {
/* 732 */     if (paramSet.size() > 0) {
/* 733 */       String[] arrayOfString = (String[])paramSet.toArray(new String[0]);
/* 734 */       String str = " ";
/* 735 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 736 */         str = str + "@" + arrayOfString[i];
/* 737 */         if (i + 1 < arrayOfString.length) {
/* 738 */           str = str + ", ";
/*     */         }
/*     */       }
/* 741 */       this.message.notice(paramString, new Object[] { str });
/*     */     }
/*     */   }
/*     */ 
/*     */   public Taglet getTaglet(String paramString)
/*     */   {
/* 754 */     if (paramString.indexOf("@") == 0) {
/* 755 */       return (Taglet)this.customTags.get(paramString.substring(1));
/*     */     }
/* 757 */     return (Taglet)this.customTags.get(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.TagletManager
 * JD-Core Version:    0.6.2
 */