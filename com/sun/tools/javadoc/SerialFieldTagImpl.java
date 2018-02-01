/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ 
/*     */ class SerialFieldTagImpl extends TagImpl
/*     */   implements SerialFieldTag, Comparable<Object>
/*     */ {
/*     */   private String fieldName;
/*     */   private String fieldType;
/*     */   private String description;
/*     */   private ClassDoc containingClass;
/*     */   private ClassDoc fieldTypeDoc;
/*     */   private FieldDocImpl matchingField;
/*     */ 
/*     */   SerialFieldTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/*  71 */     super(paramDocImpl, paramString1, paramString2);
/*  72 */     parseSerialFieldString();
/*  73 */     if ((paramDocImpl instanceof MemberDoc))
/*  74 */       this.containingClass = ((MemberDocImpl)paramDocImpl).containingClass();
/*     */   }
/*     */ 
/*     */   private void parseSerialFieldString()
/*     */   {
/*  87 */     int i = this.text.length();
/*  88 */     if (i == 0) {
/*  89 */       return;
/*     */     }
/*     */ 
/*  94 */     int j = 0;
/*     */ 
/*  96 */     for (; j < i; j += Character.charCount(k)) {
/*  97 */       k = this.text.codePointAt(j);
/*  98 */       if (!Character.isWhitespace(k))
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 104 */     int m = j;
/* 105 */     int n = j;
/* 106 */     int k = this.text.codePointAt(j);
/* 107 */     if (!Character.isJavaIdentifierStart(k)) {
/* 108 */       docenv().warning(this.holder, "tag.serialField.illegal_character", new String(
/* 110 */         Character.toChars(k)), 
/* 110 */         this.text);
/* 111 */       return;
/*     */     }
/*     */ 
/* 114 */     for (j += Character.charCount(k); j < i; j += Character.charCount(k)) {
/* 115 */       k = this.text.codePointAt(j);
/* 116 */       if (!Character.isJavaIdentifierPart(k))
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 121 */     if ((j < i) && (!Character.isWhitespace(k = this.text.codePointAt(j)))) {
/* 122 */       docenv().warning(this.holder, "tag.serialField.illegal_character", new String(
/* 124 */         Character.toChars(k)), 
/* 124 */         this.text);
/* 125 */       return;
/*     */     }
/*     */ 
/* 128 */     n = j;
/* 129 */     this.fieldName = this.text.substring(m, n);
/*     */ 
/* 132 */     for (; j < i; j += Character.charCount(k)) {
/* 133 */       k = this.text.codePointAt(j);
/* 134 */       if (!Character.isWhitespace(k))
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 140 */     m = j;
/* 141 */     n = j;
/*     */ 
/* 143 */     for (; j < i; j += Character.charCount(k)) {
/* 144 */       k = this.text.codePointAt(j);
/* 145 */       if (Character.isWhitespace(k)) {
/*     */         break;
/*     */       }
/*     */     }
/* 149 */     if ((j < i) && (!Character.isWhitespace(k = this.text.codePointAt(j)))) {
/* 150 */       docenv().warning(this.holder, "tag.serialField.illegal_character", new String(
/* 152 */         Character.toChars(k)), 
/* 152 */         this.text);
/* 153 */       return;
/*     */     }
/* 155 */     n = j;
/* 156 */     this.fieldType = this.text.substring(m, n);
/*     */ 
/* 159 */     for (; j < i; j += Character.charCount(k)) {
/* 160 */       k = this.text.codePointAt(j);
/* 161 */       if (!Character.isWhitespace(k)) {
/*     */         break;
/*     */       }
/*     */     }
/* 165 */     this.description = this.text.substring(j);
/*     */   }
/*     */ 
/*     */   String key()
/*     */   {
/* 172 */     return this.fieldName;
/*     */   }
/*     */ 
/*     */   void mapToFieldDocImpl(FieldDocImpl paramFieldDocImpl)
/*     */   {
/* 181 */     this.matchingField = paramFieldDocImpl;
/*     */   }
/*     */ 
/*     */   public String fieldName()
/*     */   {
/* 188 */     return this.fieldName;
/*     */   }
/*     */ 
/*     */   public String fieldType()
/*     */   {
/* 195 */     return this.fieldType;
/*     */   }
/*     */ 
/*     */   public ClassDoc fieldTypeDoc()
/*     */   {
/* 205 */     if ((this.fieldTypeDoc == null) && (this.containingClass != null)) {
/* 206 */       this.fieldTypeDoc = this.containingClass.findClass(this.fieldType);
/*     */     }
/* 208 */     return this.fieldTypeDoc;
/*     */   }
/*     */ 
/*     */   FieldDocImpl getMatchingField()
/*     */   {
/* 217 */     return this.matchingField;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/* 225 */     if ((this.description.length() == 0) && (this.matchingField != null))
/*     */     {
/* 228 */       Comment localComment = this.matchingField.comment();
/* 229 */       if (localComment != null) {
/* 230 */         return localComment.commentText();
/*     */       }
/*     */     }
/* 233 */     return this.description;
/*     */   }
/*     */ 
/*     */   public String kind()
/*     */   {
/* 240 */     return "@serialField";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 247 */     return this.name + ":" + this.text;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 265 */     return key().compareTo(((SerialFieldTagImpl)paramObject).key());
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.SerialFieldTagImpl
 * JD-Core Version:    0.6.2
 */