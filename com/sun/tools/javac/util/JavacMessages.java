/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class JavacMessages
/*     */   implements Messages
/*     */ {
/*  47 */   public static final Context.Key<JavacMessages> messagesKey = new Context.Key();
/*     */   private Map<Locale, SoftReference<List<ResourceBundle>>> bundleCache;
/*     */   private List<String> bundleNames;
/*     */   private Locale currentLocale;
/*     */   private List<ResourceBundle> currentBundles;
/*     */   private static final String defaultBundleName = "com.sun.tools.javac.resources.compiler";
/*     */   private static ResourceBundle defaultBundle;
/*     */   private static JavacMessages defaultMessages;
/*     */ 
/*     */   public static JavacMessages instance(Context paramContext)
/*     */   {
/*  52 */     JavacMessages localJavacMessages = (JavacMessages)paramContext.get(messagesKey);
/*  53 */     if (localJavacMessages == null)
/*  54 */       localJavacMessages = new JavacMessages(paramContext);
/*  55 */     return localJavacMessages;
/*     */   }
/*     */ 
/*     */   public Locale getCurrentLocale()
/*     */   {
/*  66 */     return this.currentLocale;
/*     */   }
/*     */ 
/*     */   public void setCurrentLocale(Locale paramLocale) {
/*  70 */     if (paramLocale == null) {
/*  71 */       paramLocale = Locale.getDefault();
/*     */     }
/*  73 */     this.currentBundles = getBundles(paramLocale);
/*  74 */     this.currentLocale = paramLocale;
/*     */   }
/*     */ 
/*     */   public JavacMessages(Context paramContext)
/*     */   {
/*  80 */     this("com.sun.tools.javac.resources.compiler", (Locale)paramContext.get(Locale.class));
/*  81 */     paramContext.put(messagesKey, this);
/*     */   }
/*     */ 
/*     */   public JavacMessages(String paramString)
/*     */     throws MissingResourceException
/*     */   {
/*  88 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */   public JavacMessages(String paramString, Locale paramLocale)
/*     */     throws MissingResourceException
/*     */   {
/*  95 */     this.bundleNames = List.nil();
/*  96 */     this.bundleCache = new HashMap();
/*  97 */     add(paramString);
/*  98 */     setCurrentLocale(paramLocale);
/*     */   }
/*     */ 
/*     */   public JavacMessages() throws MissingResourceException {
/* 102 */     this("com.sun.tools.javac.resources.compiler");
/*     */   }
/*     */ 
/*     */   public void add(String paramString) throws MissingResourceException {
/* 106 */     this.bundleNames = this.bundleNames.prepend(paramString);
/* 107 */     if (!this.bundleCache.isEmpty())
/* 108 */       this.bundleCache.clear();
/* 109 */     this.currentBundles = null;
/*     */   }
/*     */ 
/*     */   public List<ResourceBundle> getBundles(Locale paramLocale) {
/* 113 */     if ((paramLocale == this.currentLocale) && (this.currentBundles != null))
/* 114 */       return this.currentBundles;
/* 115 */     SoftReference localSoftReference = (SoftReference)this.bundleCache.get(paramLocale);
/* 116 */     List localList = localSoftReference == null ? null : (List)localSoftReference.get();
/* 117 */     if (localList == null) {
/* 118 */       localList = List.nil();
/* 119 */       for (String str : this.bundleNames) {
/*     */         try {
/* 121 */           ResourceBundle localResourceBundle = ResourceBundle.getBundle(str, paramLocale);
/* 122 */           localList = localList.prepend(localResourceBundle);
/*     */         } catch (MissingResourceException localMissingResourceException) {
/* 124 */           throw new InternalError("Cannot find javac resource bundle for locale " + paramLocale);
/*     */         }
/*     */       }
/* 127 */       this.bundleCache.put(paramLocale, new SoftReference(localList));
/*     */     }
/* 129 */     return localList;
/*     */   }
/*     */ 
/*     */   public String getLocalizedString(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 135 */     return getLocalizedString(this.currentLocale, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public String getLocalizedString(Locale paramLocale, String paramString, Object[] paramArrayOfObject) {
/* 139 */     if (paramLocale == null)
/* 140 */       paramLocale = getCurrentLocale();
/* 141 */     return getLocalizedString(getBundles(paramLocale), paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   static String getDefaultLocalizedString(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 161 */     return getLocalizedString(List.of(getDefaultBundle()), paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   static JavacMessages getDefaultMessages()
/*     */   {
/* 167 */     if (defaultMessages == null)
/* 168 */       defaultMessages = new JavacMessages("com.sun.tools.javac.resources.compiler");
/* 169 */     return defaultMessages;
/*     */   }
/*     */ 
/*     */   public static ResourceBundle getDefaultBundle() {
/*     */     try {
/* 174 */       if (defaultBundle == null)
/* 175 */         defaultBundle = ResourceBundle.getBundle("com.sun.tools.javac.resources.compiler");
/* 176 */       return defaultBundle;
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/* 179 */       throw new Error("Fatal: Resource for compiler is missing", localMissingResourceException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getLocalizedString(List<ResourceBundle> paramList, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 186 */     String str = null;
/* 187 */     for (Object localObject = paramList; (((List)localObject).nonEmpty()) && (str == null); localObject = ((List)localObject).tail) {
/* 188 */       ResourceBundle localResourceBundle = (ResourceBundle)((List)localObject).head;
/*     */       try {
/* 190 */         str = localResourceBundle.getString(paramString);
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException)
/*     */       {
/*     */       }
/*     */     }
/* 196 */     if (str == null) {
/* 197 */       str = "compiler message file broken: key=" + paramString + " arguments={0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}";
/*     */     }
/*     */ 
/* 200 */     return MessageFormat.format(str, paramArrayOfObject);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.JavacMessages
 * JD-Core Version:    0.6.2
 */