/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ class Comment
/*     */ {
/*  53 */   private final ListBuffer<Tag> tagList = new ListBuffer();
/*     */   private String text;
/*     */   private final DocEnv docenv;
/* 376 */   private static final Pattern prePat = Pattern.compile("(?i)<(/?)pre>");
/*     */ 
/*     */   Comment(final DocImpl paramDocImpl, final String paramString)
/*     */   {
/*  69 */     this.docenv = paramDocImpl.env;
/*     */ 
/* 190 */     new Object()
/*     */     {
/*     */       void parseCommentStateMachine()
/*     */       {
/*  90 */         int i = 2;
/*  91 */         int j = 1;
/*  92 */         String str = null;
/*  93 */         int k = 0;
/*  94 */         int m = 0;
/*  95 */         int n = -1;
/*  96 */         int i1 = paramString.length();
/*  97 */         for (int i2 = 0; i2 < i1; i2++) {
/*  98 */           char c = paramString.charAt(i2);
/*  99 */           boolean bool = Character.isWhitespace(c);
/* 100 */           switch (i) {
/*     */           case 3:
/* 102 */             if (bool) {
/* 103 */               str = paramString.substring(k, i2);
/* 104 */               i = 2; } break;
/*     */           case 2:
/* 108 */             if (!bool)
/*     */             {
/* 111 */               m = i2;
/* 112 */               i = 1;
/*     */             }break;
/*     */           case 1:
/* 115 */             if ((j != 0) && (c == '@')) {
/* 116 */               parseCommentComponent(str, m, n + 1);
/*     */ 
/* 118 */               k = i2;
/* 119 */               i = 3;
/*     */             }
/*     */             break;
/*     */           }
/* 123 */           if (c == '\n') {
/* 124 */             j = 1;
/* 125 */           } else if (!bool) {
/* 126 */             n = i2;
/* 127 */             j = 0;
/*     */           }
/*     */         }
/*     */ 
/* 131 */         switch (i) {
/*     */         case 3:
/* 133 */           str = paramString.substring(k, i1);
/*     */         case 2:
/* 136 */           m = i1;
/*     */         case 1:
/* 139 */           parseCommentComponent(str, m, n + 1);
/*     */         }
/*     */       }
/*     */ 
/*     */       void parseCommentComponent(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */       {
/* 149 */         String str = paramAnonymousInt2 <= paramAnonymousInt1 ? "" : paramString.substring(paramAnonymousInt1, paramAnonymousInt2);
/* 150 */         if (paramAnonymousString == null) {
/* 151 */           this.this$0.text = str;
/*     */         }
/*     */         else
/*     */         {
/*     */           Object localObject;
/* 154 */           if ((paramAnonymousString.equals("@exception")) || (paramAnonymousString.equals("@throws"))) {
/* 155 */             warnIfEmpty(paramAnonymousString, str);
/* 156 */             localObject = new ThrowsTagImpl(paramDocImpl, paramAnonymousString, str);
/* 157 */           } else if (paramAnonymousString.equals("@param")) {
/* 158 */             warnIfEmpty(paramAnonymousString, str);
/* 159 */             localObject = new ParamTagImpl(paramDocImpl, paramAnonymousString, str);
/* 160 */           } else if (paramAnonymousString.equals("@see")) {
/* 161 */             warnIfEmpty(paramAnonymousString, str);
/* 162 */             localObject = new SeeTagImpl(paramDocImpl, paramAnonymousString, str);
/* 163 */           } else if (paramAnonymousString.equals("@serialField")) {
/* 164 */             warnIfEmpty(paramAnonymousString, str);
/* 165 */             localObject = new SerialFieldTagImpl(paramDocImpl, paramAnonymousString, str);
/* 166 */           } else if (paramAnonymousString.equals("@return")) {
/* 167 */             warnIfEmpty(paramAnonymousString, str);
/* 168 */             localObject = new TagImpl(paramDocImpl, paramAnonymousString, str);
/* 169 */           } else if (paramAnonymousString.equals("@author")) {
/* 170 */             warnIfEmpty(paramAnonymousString, str);
/* 171 */             localObject = new TagImpl(paramDocImpl, paramAnonymousString, str);
/* 172 */           } else if (paramAnonymousString.equals("@version")) {
/* 173 */             warnIfEmpty(paramAnonymousString, str);
/* 174 */             localObject = new TagImpl(paramDocImpl, paramAnonymousString, str);
/*     */           } else {
/* 176 */             localObject = new TagImpl(paramDocImpl, paramAnonymousString, str);
/*     */           }
/* 178 */           this.this$0.tagList.append(localObject);
/*     */         }
/*     */       }
/*     */ 
/*     */       void warnIfEmpty(String paramAnonymousString1, String paramAnonymousString2) {
/* 183 */         if (paramAnonymousString2.length() == 0) {
/* 184 */           this.this$0.docenv.warning(paramDocImpl, "tag.tag_has_no_arguments", paramAnonymousString1);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     .parseCommentStateMachine();
/*     */   }
/*     */ 
/*     */   String commentText()
/*     */   {
/* 197 */     return this.text;
/*     */   }
/*     */ 
/*     */   Tag[] tags()
/*     */   {
/* 204 */     return (Tag[])this.tagList.toArray(new Tag[this.tagList.length()]);
/*     */   }
/*     */ 
/*     */   Tag[] tags(String paramString)
/*     */   {
/* 211 */     ListBuffer localListBuffer = new ListBuffer();
/* 212 */     String str = paramString;
/* 213 */     if (str.charAt(0) != '@') {
/* 214 */       str = "@" + str;
/*     */     }
/* 216 */     for (Tag localTag : this.tagList) {
/* 217 */       if (localTag.kind().equals(str)) {
/* 218 */         localListBuffer.append(localTag);
/*     */       }
/*     */     }
/* 221 */     return (Tag[])localListBuffer.toArray(new Tag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   ThrowsTag[] throwsTags()
/*     */   {
/* 228 */     ListBuffer localListBuffer = new ListBuffer();
/* 229 */     for (Tag localTag : this.tagList) {
/* 230 */       if ((localTag instanceof ThrowsTag)) {
/* 231 */         localListBuffer.append((ThrowsTag)localTag);
/*     */       }
/*     */     }
/* 234 */     return (ThrowsTag[])localListBuffer.toArray(new ThrowsTag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   ParamTag[] paramTags()
/*     */   {
/* 241 */     return paramTags(false);
/*     */   }
/*     */ 
/*     */   ParamTag[] typeParamTags()
/*     */   {
/* 248 */     return paramTags(true);
/*     */   }
/*     */ 
/*     */   private ParamTag[] paramTags(boolean paramBoolean)
/*     */   {
/* 257 */     ListBuffer localListBuffer = new ListBuffer();
/* 258 */     for (Tag localTag : this.tagList) {
/* 259 */       if ((localTag instanceof ParamTag)) {
/* 260 */         ParamTag localParamTag = (ParamTag)localTag;
/* 261 */         if (paramBoolean == localParamTag.isTypeParameter()) {
/* 262 */           localListBuffer.append(localParamTag);
/*     */         }
/*     */       }
/*     */     }
/* 266 */     return (ParamTag[])localListBuffer.toArray(new ParamTag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   SeeTag[] seeTags()
/*     */   {
/* 273 */     ListBuffer localListBuffer = new ListBuffer();
/* 274 */     for (Tag localTag : this.tagList) {
/* 275 */       if ((localTag instanceof SeeTag)) {
/* 276 */         localListBuffer.append((SeeTag)localTag);
/*     */       }
/*     */     }
/* 279 */     return (SeeTag[])localListBuffer.toArray(new SeeTag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   SerialFieldTag[] serialFieldTags()
/*     */   {
/* 286 */     ListBuffer localListBuffer = new ListBuffer();
/* 287 */     for (Tag localTag : this.tagList) {
/* 288 */       if ((localTag instanceof SerialFieldTag)) {
/* 289 */         localListBuffer.append((SerialFieldTag)localTag);
/*     */       }
/*     */     }
/* 292 */     return (SerialFieldTag[])localListBuffer.toArray(new SerialFieldTag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   static Tag[] getInlineTags(DocImpl paramDocImpl, String paramString)
/*     */   {
/* 299 */     ListBuffer localListBuffer = new ListBuffer();
/* 300 */     int i = 0; int j = 0; int k = paramString.length();
/* 301 */     boolean bool = false;
/* 302 */     DocEnv localDocEnv = paramDocImpl.env;
/*     */ 
/* 304 */     if (k == 0)
/* 305 */       return (Tag[])localListBuffer.toArray(new Tag[localListBuffer.length()]);
/*     */     while (true)
/*     */     {
/*     */       int m;
/* 309 */       if ((m = inlineTagFound(paramDocImpl, paramString, j)) == -1)
/*     */       {
/* 311 */         localListBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 312 */           .substring(j)));
/*     */       }
/*     */       else
/*     */       {
/* 315 */         bool = scanForPre(paramString, j, m, bool);
/* 316 */         int n = m;
/* 317 */         for (int i1 = m; i1 < paramString.length(); i1++) {
/* 318 */           char c = paramString.charAt(i1);
/* 319 */           if ((Character.isWhitespace(c)) || (c == '}'))
/*     */           {
/* 321 */             n = i1;
/* 322 */             break;
/*     */           }
/*     */         }
/* 325 */         String str = paramString.substring(m + 2, n);
/* 326 */         if ((!bool) || ((!str.equals("code")) && (!str.equals("literal"))))
/*     */         {
/* 328 */           while (Character.isWhitespace(paramString
/* 329 */             .charAt(n)))
/*     */           {
/* 330 */             if (paramString.length() <= n) {
/* 331 */               localListBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 332 */                 .substring(j, n)));
/*     */ 
/* 333 */               localDocEnv.warning(paramDocImpl, "tag.Improper_Use_Of_Link_Tag", paramString);
/*     */ 
/* 336 */               return (Tag[])localListBuffer.toArray(new Tag[localListBuffer.length()]);
/*     */             }
/* 338 */             n++;
/*     */           }
/*     */         }
/*     */ 
/* 342 */         localListBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 343 */           .substring(j, m)));
/*     */ 
/* 344 */         j = n;
/* 345 */         if ((i = findInlineTagDelim(paramString, j)) == -1)
/*     */         {
/* 348 */           localListBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 349 */             .substring(j)));
/*     */ 
/* 350 */           localDocEnv.warning(paramDocImpl, "tag.End_delimiter_missing_for_possible_SeeTag", paramString);
/*     */ 
/* 353 */           return (Tag[])localListBuffer.toArray(new Tag[localListBuffer.length()]);
/*     */         }
/*     */ 
/* 356 */         if ((str.equals("see")) || 
/* 357 */           (str
/* 357 */           .equals("link")) || 
/* 358 */           (str
/* 358 */           .equals("linkplain")))
/*     */         {
/* 359 */           localListBuffer.append(new SeeTagImpl(paramDocImpl, "@" + str, paramString
/* 360 */             .substring(j, i)));
/*     */         }
/*     */         else {
/* 362 */           localListBuffer.append(new TagImpl(paramDocImpl, "@" + str, paramString
/* 363 */             .substring(j, i)));
/*     */         }
/*     */ 
/* 365 */         j = i + 1;
/*     */ 
/* 368 */         if (j == paramString.length())
/*     */           break;
/*     */       }
/*     */     }
/* 372 */     return (Tag[])localListBuffer.toArray(new Tag[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   private static boolean scanForPre(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 379 */     Matcher localMatcher = prePat.matcher(paramString).region(paramInt1, paramInt2);
/* 380 */     while (localMatcher.find()) {
/* 381 */       paramBoolean = localMatcher.group(1).isEmpty();
/*     */     }
/* 383 */     return paramBoolean;
/*     */   }
/*     */ 
/*     */   private static int findInlineTagDelim(String paramString, int paramInt)
/*     */   {
/*     */     int i;
/* 396 */     if ((i = paramString.indexOf("}", paramInt)) == -1)
/* 397 */       return -1;
/*     */     int j;
/* 398 */     if (((j = paramString.indexOf("{", paramInt)) != -1) && (j < i))
/*     */     {
/* 401 */       int k = findInlineTagDelim(paramString, j + 1);
/*     */ 
/* 403 */       return k != -1 ? 
/* 403 */         findInlineTagDelim(paramString, k + 1) : 
/* 403 */         -1;
/*     */     }
/*     */ 
/* 406 */     return i;
/*     */   }
/*     */ 
/*     */   private static int inlineTagFound(DocImpl paramDocImpl, String paramString, int paramInt)
/*     */   {
/* 419 */     DocEnv localDocEnv = paramDocImpl.env;
/* 420 */     int i = paramString.indexOf("{@", paramInt);
/* 421 */     if ((paramInt == paramString.length()) || (i == -1))
/* 422 */       return -1;
/* 423 */     if (paramString.indexOf('}', i) == -1)
/*     */     {
/* 425 */       localDocEnv.warning(paramDocImpl, "tag.Improper_Use_Of_Link_Tag", paramString
/* 426 */         .substring(i, paramString
/* 426 */         .length()));
/* 427 */       return -1;
/*     */     }
/* 429 */     return i;
/*     */   }
/*     */ 
/*     */   static Tag[] firstSentenceTags(DocImpl paramDocImpl, String paramString)
/*     */   {
/* 438 */     DocLocale localDocLocale = paramDocImpl.env.doclocale;
/* 439 */     return getInlineTags(paramDocImpl, localDocLocale
/* 440 */       .localeSpecificFirstSentence(paramDocImpl, paramString));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 448 */     return this.text;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.Comment
 * JD-Core Version:    0.6.2
 */