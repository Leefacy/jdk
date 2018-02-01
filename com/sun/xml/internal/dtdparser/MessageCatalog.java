/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public abstract class MessageCatalog
/*     */ {
/*     */   private String bundleName;
/* 433 */   private Hashtable cache = new Hashtable(5);
/*     */ 
/*     */   protected MessageCatalog(Class packageMember)
/*     */   {
/* 152 */     this(packageMember, "Messages");
/*     */   }
/*     */ 
/*     */   private MessageCatalog(Class packageMember, String bundle)
/*     */   {
/* 167 */     this.bundleName = packageMember.getName();
/* 168 */     int index = this.bundleName.lastIndexOf('.');
/* 169 */     if (index == -1)
/* 170 */       this.bundleName = "";
/*     */     else
/* 172 */       this.bundleName = (this.bundleName.substring(0, index) + ".");
/* 173 */     this.bundleName = (this.bundleName + "resources." + bundle);
/*     */   }
/*     */ 
/*     */   public String getMessage(Locale locale, String messageId)
/*     */   {
/* 196 */     if (locale == null)
/* 197 */       locale = Locale.getDefault();
/*     */     ResourceBundle bundle;
/*     */     try {
/* 200 */       bundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*     */       ResourceBundle bundle;
/* 202 */       bundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     }
/* 204 */     return bundle.getString(messageId);
/*     */   }
/*     */ 
/*     */   public String getMessage(Locale locale, String messageId, Object[] parameters)
/*     */   {
/* 229 */     if (parameters == null) {
/* 230 */       return getMessage(locale, messageId);
/*     */     }
/*     */ 
/* 235 */     for (int i = 0; i < parameters.length; i++) {
/* 236 */       if ((!(parameters[i] instanceof String)) && (!(parameters[i] instanceof Number)) && (!(parameters[i] instanceof Date)))
/*     */       {
/* 239 */         if (parameters[i] == null)
/* 240 */           parameters[i] = "(null)";
/*     */         else {
/* 242 */           parameters[i] = parameters[i].toString();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 247 */     if (locale == null) {
/* 248 */       locale = Locale.getDefault();
/*     */     }
/*     */ 
/*     */     ResourceBundle bundle;
/*     */     try
/*     */     {
/* 255 */       bundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*     */       ResourceBundle bundle;
/* 257 */       bundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     }
/*     */ 
/* 267 */     MessageFormat format = new MessageFormat(bundle.getString(messageId));
/* 268 */     format.setLocale(locale);
/*     */ 
/* 271 */     StringBuffer result = new StringBuffer();
/*     */ 
/* 273 */     result = format.format(parameters, result, new FieldPosition(0));
/* 274 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public Locale chooseLocale(String[] languages)
/*     */   {
/* 297 */     if ((languages = canonicalize(languages)) != null) {
/* 298 */       for (int i = 0; i < languages.length; i++)
/* 299 */         if (isLocaleSupported(languages[i]))
/* 300 */           return getLocale(languages[i]);
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   private String[] canonicalize(String[] languages)
/*     */   {
/* 314 */     boolean didClone = false;
/* 315 */     int trimCount = 0;
/*     */ 
/* 317 */     if (languages == null) {
/* 318 */       return languages;
/*     */     }
/* 320 */     for (int i = 0; i < languages.length; i++) {
/* 321 */       String lang = languages[i];
/* 322 */       int len = lang.length();
/*     */ 
/* 326 */       if ((len != 2) && (len != 5)) {
/* 327 */         if (!didClone) {
/* 328 */           languages = (String[])languages.clone();
/* 329 */           didClone = true;
/*     */         }
/* 331 */         languages[i] = null;
/* 332 */         trimCount++;
/*     */       }
/* 337 */       else if (len == 2) {
/* 338 */         lang = lang.toLowerCase();
/* 339 */         if (lang != languages[i]) {
/* 340 */           if (!didClone) {
/* 341 */             languages = (String[])languages.clone();
/* 342 */             didClone = true;
/*     */           }
/* 344 */           languages[i] = lang;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 350 */         char[] buf = new char[5];
/*     */ 
/* 352 */         buf[0] = Character.toLowerCase(lang.charAt(0));
/* 353 */         buf[1] = Character.toLowerCase(lang.charAt(1));
/* 354 */         buf[2] = '_';
/* 355 */         buf[3] = Character.toUpperCase(lang.charAt(3));
/* 356 */         buf[4] = Character.toUpperCase(lang.charAt(4));
/* 357 */         if (!didClone) {
/* 358 */           languages = (String[])languages.clone();
/* 359 */           didClone = true;
/*     */         }
/* 361 */         languages[i] = new String(buf);
/*     */       }
/*     */     }
/*     */ 
/* 365 */     if (trimCount != 0) {
/* 366 */       String[] temp = new String[languages.length - trimCount];
/*     */ 
/* 369 */       int i = 0; for (trimCount = 0; i < temp.length; i++) {
/* 370 */         while (languages[(i + trimCount)] == null)
/* 371 */           trimCount++;
/* 372 */         temp[i] = languages[(i + trimCount)];
/*     */       }
/* 374 */       languages = temp;
/*     */     }
/* 376 */     return languages;
/*     */   }
/*     */ 
/*     */   private Locale getLocale(String localeName)
/*     */   {
/* 389 */     int index = localeName.indexOf('_');
/*     */     String country;
/*     */     String language;
/*     */     String country;
/* 390 */     if (index == -1)
/*     */     {
/* 394 */       if (localeName.equals("de"))
/* 395 */         return Locale.GERMAN;
/* 396 */       if (localeName.equals("en"))
/* 397 */         return Locale.ENGLISH;
/* 398 */       if (localeName.equals("fr"))
/* 399 */         return Locale.FRENCH;
/* 400 */       if (localeName.equals("it"))
/* 401 */         return Locale.ITALIAN;
/* 402 */       if (localeName.equals("ja"))
/* 403 */         return Locale.JAPANESE;
/* 404 */       if (localeName.equals("ko"))
/* 405 */         return Locale.KOREAN;
/* 406 */       if (localeName.equals("zh")) {
/* 407 */         return Locale.CHINESE;
/*     */       }
/* 409 */       String language = localeName;
/* 410 */       country = "";
/*     */     } else {
/* 412 */       if (localeName.equals("zh_CN"))
/* 413 */         return Locale.SIMPLIFIED_CHINESE;
/* 414 */       if (localeName.equals("zh_TW")) {
/* 415 */         return Locale.TRADITIONAL_CHINESE;
/*     */       }
/*     */ 
/* 421 */       language = localeName.substring(0, index);
/* 422 */       country = localeName.substring(index + 1);
/*     */     }
/*     */ 
/* 425 */     return new Locale(language, country);
/*     */   }
/*     */ 
/*     */   public boolean isLocaleSupported(String localeName)
/*     */   {
/* 462 */     Boolean value = (Boolean)this.cache.get(localeName);
/*     */ 
/* 464 */     if (value != null) {
/* 465 */       return value.booleanValue();
/*     */     }
/*     */ 
/* 472 */     ClassLoader loader = null;
/*     */     while (true)
/*     */     {
/* 475 */       String name = this.bundleName + "_" + localeName;
/*     */       try
/*     */       {
/* 479 */         Class.forName(name);
/* 480 */         this.cache.put(localeName, Boolean.TRUE);
/* 481 */         return true;
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 488 */         if (loader == null) {
/* 489 */           loader = getClass().getClassLoader();
/*     */         }
/* 491 */         name = name.replace('.', '/');
/* 492 */         name = name + ".properties";
/*     */         InputStream in;
/*     */         InputStream in;
/* 493 */         if (loader == null)
/* 494 */           in = ClassLoader.getSystemResourceAsStream(name);
/*     */         else
/* 496 */           in = loader.getResourceAsStream(name);
/* 497 */         if (in != null) {
/* 498 */           this.cache.put(localeName, Boolean.TRUE);
/* 499 */           return true;
/*     */         }
/*     */ 
/* 502 */         int index = localeName.indexOf('_');
/*     */ 
/* 504 */         if (index > 0) {
/* 505 */           localeName = localeName.substring(0, index);
/*     */         }
/*     */         else
/*     */         {
/* 513 */           this.cache.put(localeName, Boolean.FALSE); }  } 
/* 514 */     }return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.dtdparser.MessageCatalog
 * JD-Core Version:    0.6.2
 */