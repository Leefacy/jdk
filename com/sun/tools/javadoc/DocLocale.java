/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.text.Collator;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class DocLocale
/*     */ {
/*     */   final String localeName;
/*     */   final Locale locale;
/*     */   final Collator collator;
/*     */   private final DocEnv docenv;
/*     */   private final BreakIterator sentenceBreaker;
/*  77 */   private boolean useBreakIterator = false;
/*     */ 
/*  82 */   static final String[] sentenceTerminators = { "<p>", "</p>", "<h1>", "<h2>", "<h3>", "<h4>", "<h5>", "<h6>", "</h1>", "</h2>", "</h3>", "</h4>", "</h5>", "</h6>", "<hr>", "<pre>", "</pre>" };
/*     */ 
/*     */   DocLocale(DocEnv paramDocEnv, String paramString, boolean paramBoolean)
/*     */   {
/*  94 */     this.docenv = paramDocEnv;
/*  95 */     this.localeName = paramString;
/*  96 */     this.useBreakIterator = paramBoolean;
/*  97 */     this.locale = getLocale();
/*  98 */     if (this.locale == null)
/*  99 */       paramDocEnv.exit();
/*     */     else {
/* 101 */       Locale.setDefault(this.locale);
/*     */     }
/* 103 */     this.collator = Collator.getInstance(this.locale);
/* 104 */     this.sentenceBreaker = BreakIterator.getSentenceInstance(this.locale);
/*     */   }
/*     */ 
/*     */   private Locale getLocale()
/*     */   {
/* 113 */     Locale localLocale = null;
/* 114 */     if (this.localeName.length() > 0) {
/* 115 */       int i = this.localeName.indexOf('_');
/* 116 */       int j = -1;
/* 117 */       String str1 = null;
/* 118 */       String str2 = null;
/* 119 */       String str3 = null;
/* 120 */       if (i == 2) {
/* 121 */         str1 = this.localeName.substring(0, i);
/* 122 */         j = this.localeName.indexOf('_', i + 1);
/* 123 */         if (j > 0) {
/* 124 */           if ((j != i + 3) || 
/* 125 */             (this.localeName
/* 125 */             .length() <= j + 1)) {
/* 126 */             this.docenv.error(null, "main.malformed_locale_name", this.localeName);
/* 127 */             return null;
/*     */           }
/* 129 */           str2 = this.localeName.substring(i + 1, j);
/*     */ 
/* 131 */           str3 = this.localeName.substring(j + 1);
/* 132 */         } else if (this.localeName.length() == i + 3) {
/* 133 */           str2 = this.localeName.substring(i + 1);
/*     */         } else {
/* 135 */           this.docenv.error(null, "main.malformed_locale_name", this.localeName);
/* 136 */           return null;
/*     */         }
/* 138 */       } else if ((i == -1) && (this.localeName.length() == 2)) {
/* 139 */         str1 = this.localeName;
/*     */       } else {
/* 141 */         this.docenv.error(null, "main.malformed_locale_name", this.localeName);
/* 142 */         return null;
/*     */       }
/* 144 */       localLocale = searchLocale(str1, str2, str3);
/* 145 */       if (localLocale == null) {
/* 146 */         this.docenv.error(null, "main.illegal_locale_name", this.localeName);
/* 147 */         return null;
/*     */       }
/* 149 */       return localLocale;
/*     */     }
/*     */ 
/* 152 */     return Locale.getDefault();
/*     */   }
/*     */ 
/*     */   private Locale searchLocale(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 162 */     Locale[] arrayOfLocale = Locale.getAvailableLocales();
/* 163 */     for (int i = 0; i < arrayOfLocale.length; i++) {
/* 164 */       if ((arrayOfLocale[i].getLanguage().equals(paramString1)) && ((paramString2 == null) || 
/* 165 */         (arrayOfLocale[i]
/* 165 */         .getCountry().equals(paramString2))) && (
/* 165 */         (paramString3 == null) || 
/* 166 */         (arrayOfLocale[i]
/* 166 */         .getVariant().equals(paramString3)))) {
/* 167 */         return arrayOfLocale[i];
/*     */       }
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   String localeSpecificFirstSentence(DocImpl paramDocImpl, String paramString) {
/* 174 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 175 */       return "";
/*     */     }
/* 177 */     int i = paramString.indexOf("-->");
/* 178 */     if ((paramString.trim().startsWith("<!--")) && (i != -1)) {
/* 179 */       return localeSpecificFirstSentence(paramDocImpl, paramString.substring(i + 3, paramString.length()));
/*     */     }
/* 181 */     if ((this.useBreakIterator) || (!this.locale.getLanguage().equals("en"))) {
/* 182 */       this.sentenceBreaker.setText(paramString.replace('\n', ' '));
/* 183 */       int j = this.sentenceBreaker.first();
/* 184 */       int k = this.sentenceBreaker.next();
/* 185 */       return paramString.substring(j, k).trim();
/*     */     }
/* 187 */     return englishLanguageFirstSentence(paramString).trim();
/*     */   }
/*     */ 
/*     */   private String englishLanguageFirstSentence(String paramString)
/*     */   {
/* 196 */     if (paramString == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     int i = paramString.length();
/* 200 */     int j = 0;
/* 201 */     for (int k = 0; k < i; k++) {
/* 202 */       switch (paramString.charAt(k)) {
/*     */       case '.':
/* 204 */         j = 1;
/* 205 */         break;
/*     */       case '\t':
/*     */       case '\n':
/*     */       case '\f':
/*     */       case '\r':
/*     */       case ' ':
/* 211 */         if (j != 0) {
/* 212 */           return paramString.substring(0, k);
/*     */         }
/*     */         break;
/*     */       case '<':
/* 216 */         if ((k > 0) && 
/* 217 */           (htmlSentenceTerminatorFound(paramString, k))) {
/* 218 */           return paramString.substring(0, k);
/*     */         }
/*     */ 
/*     */         break;
/*     */       default:
/* 223 */         j = 0;
/*     */       }
/*     */     }
/* 226 */     return paramString;
/*     */   }
/*     */ 
/*     */   private boolean htmlSentenceTerminatorFound(String paramString, int paramInt)
/*     */   {
/* 234 */     for (int i = 0; i < sentenceTerminators.length; i++) {
/* 235 */       String str = sentenceTerminators[i];
/* 236 */       if (paramString.regionMatches(true, paramInt, str, 0, str
/* 237 */         .length())) {
/* 238 */         return true;
/*     */       }
/*     */     }
/* 241 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.DocLocale
 * JD-Core Version:    0.6.2
 */