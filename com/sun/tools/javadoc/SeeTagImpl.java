/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type.CapturedType;
/*     */ import com.sun.tools.javac.util.JavacMessages;
/*     */ import com.sun.tools.javac.util.LayoutCharacters;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class SeeTagImpl extends TagImpl
/*     */   implements SeeTag, LayoutCharacters
/*     */ {
/*     */   private String where;
/*     */   private String what;
/*     */   private PackageDoc referencedPackage;
/*     */   private ClassDoc referencedClass;
/*     */   private MemberDoc referencedMember;
/*  71 */   String label = "";
/*     */   private static final boolean showRef = false;
/*     */ 
/*     */   SeeTagImpl(DocImpl paramDocImpl, String paramString1, String paramString2)
/*     */   {
/*  74 */     super(paramDocImpl, paramString1, paramString2);
/*  75 */     parseSeeString();
/*  76 */     if (this.where != null) {
/*  77 */       ClassDocImpl localClassDocImpl = null;
/*  78 */       if ((paramDocImpl instanceof MemberDoc))
/*     */       {
/*  80 */         localClassDocImpl = (ClassDocImpl)((ProgramElementDoc)paramDocImpl)
/*  80 */           .containingClass();
/*  81 */       } else if ((paramDocImpl instanceof ClassDoc)) {
/*  82 */         localClassDocImpl = (ClassDocImpl)paramDocImpl;
/*     */       }
/*  84 */       findReferenced(localClassDocImpl);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void showRef()
/*     */   {
/*     */     Object localObject;
/*  93 */     if (this.referencedMember != null) {
/*  94 */       if ((this.referencedMember instanceof MethodDocImpl))
/*  95 */         localObject = ((MethodDocImpl)this.referencedMember).sym;
/*  96 */       else if ((this.referencedMember instanceof FieldDocImpl))
/*  97 */         localObject = ((FieldDocImpl)this.referencedMember).sym;
/*     */       else
/*  99 */         localObject = ((ConstructorDocImpl)this.referencedMember).sym;
/* 100 */     } else if (this.referencedClass != null)
/* 101 */       localObject = ((ClassDocImpl)this.referencedClass).tsym;
/* 102 */     else if (this.referencedPackage != null)
/* 103 */       localObject = ((PackageDocImpl)this.referencedPackage).sym;
/*     */     else {
/* 105 */       return;
/*     */     }
/* 107 */     final JavacMessages localJavacMessages = JavacMessages.instance(docenv().context);
/* 108 */     Locale localLocale = Locale.getDefault();
/* 109 */     Printer local1 = new Printer() {
/*     */       int count;
/*     */ 
/*     */       protected String localize(Locale paramAnonymousLocale, String paramAnonymousString, Object[] paramAnonymousArrayOfObject) {
/* 113 */         return localJavacMessages.getLocalizedString(paramAnonymousLocale, paramAnonymousString, paramAnonymousArrayOfObject);
/*     */       }
/*     */ 
/*     */       protected String capturedVarId(Type.CapturedType paramAnonymousCapturedType, Locale paramAnonymousLocale) {
/* 117 */         return "CAP#" + ++this.count;
/*     */       }
/*     */     };
/* 121 */     String str1 = this.text.replaceAll("\\s+", " ");
/* 122 */     int i = str1.indexOf(" ");
/* 123 */     int j = str1.indexOf("(");
/* 124 */     int k = str1.indexOf(")");
/*     */ 
/* 127 */     String str2 = (j == -1) || (i < j) ? str1
/* 126 */       .substring(0, i) : i == -1 ? str1 : 
/* 126 */       str1
/* 127 */       .substring(0, k + 1);
/*     */ 
/* 129 */     File localFile = new File(this.holder.position().file().getAbsoluteFile().toURI().normalize());
/*     */ 
/* 131 */     StringBuilder localStringBuilder = new StringBuilder();
/* 132 */     localStringBuilder.append("+++ ").append(localFile).append(": ")
/* 133 */       .append(name()).append(" ").append(str2).append(": ");
/* 134 */     localStringBuilder.append(((Symbol)localObject).getKind()).append(" ");
/* 135 */     if ((((Symbol)localObject).kind == 16) || (((Symbol)localObject).kind == 4))
/* 136 */       localStringBuilder.append(local1.visit(((Symbol)localObject).owner, localLocale)).append(".");
/* 137 */     localStringBuilder.append(local1.visit((Symbol)localObject, localLocale));
/*     */ 
/* 139 */     System.err.println(localStringBuilder);
/*     */   }
/*     */ 
/*     */   public String referencedClassName()
/*     */   {
/* 150 */     return this.where;
/*     */   }
/*     */ 
/*     */   public PackageDoc referencedPackage()
/*     */   {
/* 160 */     return this.referencedPackage;
/*     */   }
/*     */ 
/*     */   public ClassDoc referencedClass()
/*     */   {
/* 170 */     return this.referencedClass;
/*     */   }
/*     */ 
/*     */   public String referencedMemberName()
/*     */   {
/* 182 */     return this.what;
/*     */   }
/*     */ 
/*     */   public MemberDoc referencedMember()
/*     */   {
/* 193 */     return this.referencedMember;
/*     */   }
/*     */ 
/*     */   private void parseSeeString()
/*     */   {
/* 201 */     int i = this.text.length();
/* 202 */     if (i == 0) {
/* 203 */       return;
/*     */     }
/* 205 */     switch (this.text.charAt(0)) {
/*     */     case '<':
/* 207 */       if (this.text.charAt(i - 1) != '>') {
/* 208 */         docenv().warning(this.holder, "tag.see.no_close_bracket_on_url", this.name, this.text);
/*     */       }
/*     */ 
/* 212 */       return;
/*     */     case '"':
/* 214 */       if ((i == 1) || (this.text.charAt(i - 1) != '"')) {
/* 215 */         docenv().warning(this.holder, "tag.see.no_close_quote", this.name, this.text);
/*     */       }
/*     */ 
/* 221 */       return;
/*     */     }
/*     */ 
/* 230 */     int j = 0;
/* 231 */     int k = 0;
/* 232 */     int m = 0;
/*     */     int n;
/* 234 */     for (int i1 = m; i1 < i; i1 += Character.charCount(n)) {
/* 235 */       n = this.text.codePointAt(i1);
/* 236 */       switch (n) { case 40:
/* 237 */         j++; break;
/*     */       case 41:
/* 238 */         j--; break;
/*     */       case 35:
/*     */       case 46:
/*     */       case 91:
/*     */       case 93:
/* 239 */         break;
/*     */       case 44:
/* 241 */         if (j <= 0) { docenv().warning(this.holder, "tag.see.malformed_see_tag", this.name, this.text);
/*     */           return; }
/*     */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 13:
/*     */       case 32:
/* 249 */         if (j == 0) {
/* 250 */           k = i1;
/* 251 */           i1 = i; } break;
/*     */       default:
/* 255 */         if (!Character.isJavaIdentifierPart(n)) {
/* 256 */           docenv().warning(this.holder, "tag.see.illegal_character", this.name, "" + n, this.text);
/*     */         }
/*     */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 263 */     if (j != 0) {
/* 264 */       docenv().warning(this.holder, "tag.see.malformed_see_tag", this.name, this.text);
/*     */ 
/* 267 */       return;
/*     */     }
/*     */ 
/* 270 */     String str1 = "";
/* 271 */     String str2 = "";
/*     */ 
/* 273 */     if (k > 0) {
/* 274 */       str1 = this.text.substring(m, k);
/* 275 */       str2 = this.text.substring(k + 1);
/*     */ 
/* 278 */       for (i2 = 0; i2 < str2.length(); i2++) {
/* 279 */         int i3 = str2.charAt(i2);
/* 280 */         if ((i3 != 32) && (i3 != 9) && (i3 != 10)) {
/* 281 */           this.label = str2.substring(i2);
/* 282 */           break;
/*     */         }
/*     */       }
/*     */     } else {
/* 286 */       str1 = this.text;
/* 287 */       this.label = "";
/*     */     }
/*     */ 
/* 290 */     int i2 = str1.indexOf('#');
/* 291 */     if (i2 >= 0)
/*     */     {
/* 293 */       this.where = str1.substring(0, i2);
/* 294 */       this.what = str1.substring(i2 + 1);
/*     */     }
/* 296 */     else if (str1.indexOf('(') >= 0) {
/* 297 */       docenv().warning(this.holder, "tag.see.missing_sharp", this.name, this.text);
/*     */ 
/* 300 */       this.where = "";
/* 301 */       this.what = str1;
/*     */     }
/*     */     else
/*     */     {
/* 305 */       this.where = str1;
/* 306 */       this.what = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void findReferenced(ClassDocImpl paramClassDocImpl)
/*     */   {
/* 319 */     if (this.where.length() > 0) {
/* 320 */       if (paramClassDocImpl != null)
/* 321 */         this.referencedClass = paramClassDocImpl.findClass(this.where);
/*     */       else {
/* 323 */         this.referencedClass = docenv().lookupClass(this.where);
/*     */       }
/* 325 */       if ((this.referencedClass == null) && ((holder() instanceof ProgramElementDoc))) {
/* 326 */         this.referencedClass = docenv().lookupClass(
/* 327 */           ((ProgramElementDoc)holder()).containingPackage().name() + "." + this.where);
/*     */       }
/*     */ 
/* 330 */       if (this.referencedClass == null)
/*     */       {
/* 332 */         this.referencedPackage = docenv().lookupPackage(this.where);
/*     */       }
/*     */     }
/*     */     else {
/* 336 */       if (paramClassDocImpl == null) {
/* 337 */         docenv().warning(this.holder, "tag.see.class_not_specified", this.name, this.text);
/*     */ 
/* 340 */         return;
/*     */       }
/* 342 */       this.referencedClass = paramClassDocImpl;
/*     */     }
/*     */ 
/* 345 */     this.where = this.referencedClass.qualifiedName();
/*     */ 
/* 347 */     if (this.what == null) {
/* 348 */       return;
/*     */     }
/* 350 */     int i = this.what.indexOf('(');
/* 351 */     String str = i >= 0 ? this.what.substring(0, i) : this.what;
/*     */ 
/* 353 */     if (i > 0)
/*     */     {
/* 356 */       String[] arrayOfString = new ParameterParseMachine(this.what
/* 356 */         .substring(i, this.what
/* 356 */         .length())).parseParameters();
/* 357 */       if (arrayOfString != null) {
/* 358 */         this.referencedMember = findExecutableMember(str, arrayOfString, this.referencedClass);
/*     */       }
/*     */       else
/* 361 */         this.referencedMember = null;
/*     */     }
/*     */     else
/*     */     {
/* 365 */       this.referencedMember = findExecutableMember(str, null, this.referencedClass);
/*     */ 
/* 368 */       FieldDoc localFieldDoc = ((ClassDocImpl)this.referencedClass)
/* 368 */         .findField(str);
/*     */ 
/* 370 */       if (this.referencedMember != null) { if (localFieldDoc != null) { if (!localFieldDoc
/* 372 */             .containingClass()
/* 373 */             .subclassOf(this.referencedMember
/* 373 */             .containingClass())); } } else this.referencedMember = localFieldDoc;
/*     */     }
/*     */ 
/* 377 */     if (this.referencedMember == null)
/* 378 */       docenv().warning(this.holder, "tag.see.can_not_find_member", this.name, this.what, this.where);
/*     */   }
/*     */ 
/*     */   private MemberDoc findReferencedMethod(String paramString, String[] paramArrayOfString, ClassDoc paramClassDoc)
/*     */   {
/* 387 */     MemberDoc localMemberDoc = findExecutableMember(paramString, paramArrayOfString, paramClassDoc);
/* 388 */     ClassDoc[] arrayOfClassDoc = paramClassDoc.innerClasses();
/* 389 */     if (localMemberDoc == null) {
/* 390 */       for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 391 */         localMemberDoc = findReferencedMethod(paramString, paramArrayOfString, arrayOfClassDoc[i]);
/* 392 */         if (localMemberDoc != null) {
/* 393 */           return localMemberDoc;
/*     */         }
/*     */       }
/*     */     }
/* 397 */     return null;
/*     */   }
/*     */ 
/*     */   private MemberDoc findExecutableMember(String paramString, String[] paramArrayOfString, ClassDoc paramClassDoc)
/*     */   {
/* 402 */     if (paramString.equals(paramClassDoc.name())) {
/* 403 */       return ((ClassDocImpl)paramClassDoc).findConstructor(paramString, paramArrayOfString);
/*     */     }
/*     */ 
/* 406 */     return ((ClassDocImpl)paramClassDoc).findMethod(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public String kind()
/*     */   {
/* 532 */     return "@see";
/*     */   }
/*     */ 
/*     */   public String label()
/*     */   {
/* 539 */     return this.label;
/*     */   }
/*     */ 
/*     */   class ParameterParseMachine
/*     */   {
/*     */     static final int START = 0;
/*     */     static final int TYPE = 1;
/*     */     static final int NAME = 2;
/*     */     static final int TNSPACE = 3;
/*     */     static final int ARRAYDECORATION = 4;
/*     */     static final int ARRAYSPACE = 5;
/*     */     String parameters;
/*     */     StringBuilder typeId;
/*     */     ListBuffer<String> paramList;
/*     */ 
/*     */     ParameterParseMachine(String arg2)
/*     */     {
/*     */       Object localObject;
/* 429 */       this.parameters = localObject;
/* 430 */       this.paramList = new ListBuffer();
/* 431 */       this.typeId = new StringBuilder();
/*     */     }
/*     */ 
/*     */     public String[] parseParameters() {
/* 435 */       if (this.parameters.equals("()")) {
/* 436 */         return new String[0];
/*     */       }
/* 438 */       int i = 0;
/* 439 */       int j = 0;
/* 440 */       this.parameters = this.parameters.substring(1, this.parameters.length() - 1);
/*     */       int k;
/* 442 */       for (int m = 0; m < this.parameters.length(); m += Character.charCount(k)) {
/* 443 */         k = this.parameters.codePointAt(m);
/* 444 */         switch (i) {
/*     */         case 0:
/* 446 */           if (Character.isJavaIdentifierStart(k)) {
/* 447 */             this.typeId.append(Character.toChars(k));
/* 448 */             i = 1;
/*     */           }
/* 450 */           j = 0;
/* 451 */           break;
/*     */         case 1:
/* 453 */           if ((Character.isJavaIdentifierPart(k)) || (k == 46)) {
/* 454 */             this.typeId.append(Character.toChars(k));
/* 455 */           } else if (k == 91) {
/* 456 */             this.typeId.append('[');
/* 457 */             i = 4;
/* 458 */           } else if (Character.isWhitespace(k)) {
/* 459 */             i = 3;
/* 460 */           } else if (k == 44) {
/* 461 */             addTypeToParamList();
/* 462 */             i = 0;
/*     */           }
/* 464 */           j = 1;
/* 465 */           break;
/*     */         case 3:
/* 467 */           if (Character.isJavaIdentifierStart(k)) {
/* 468 */             if (j == 4) {
/* 469 */               SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.missing_comma_space", SeeTagImpl.this.name, "(" + this.parameters + ")");
/*     */ 
/* 473 */               return (String[])null;
/*     */             }
/* 475 */             addTypeToParamList();
/* 476 */             i = 2;
/* 477 */           } else if (k == 91) {
/* 478 */             this.typeId.append('[');
/* 479 */             i = 4;
/* 480 */           } else if (k == 44) {
/* 481 */             addTypeToParamList();
/* 482 */             i = 0;
/*     */           }
/* 484 */           j = 3;
/* 485 */           break;
/*     */         case 4:
/* 487 */           if (k == 93) {
/* 488 */             this.typeId.append(']');
/* 489 */             i = 3;
/* 490 */           } else if (!Character.isWhitespace(k)) {
/* 491 */             SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.illegal_char_in_arr_dim", SeeTagImpl.this.name, "(" + this.parameters + ")");
/*     */ 
/* 495 */             return (String[])null;
/*     */           }
/* 497 */           j = 4;
/* 498 */           break;
/*     */         case 2:
/* 500 */           if (k == 44) {
/* 501 */             i = 0;
/*     */           }
/* 503 */           j = 2;
/*     */         }
/*     */       }
/*     */ 
/* 507 */       if ((i == 4) || ((i == 0) && (j == 3)))
/*     */       {
/* 509 */         SeeTagImpl.this.docenv().warning(SeeTagImpl.this.holder, "tag.illegal_see_tag", "(" + this.parameters + ")");
/*     */       }
/*     */ 
/* 513 */       if (this.typeId.length() > 0) {
/* 514 */         this.paramList.append(this.typeId.toString());
/*     */       }
/* 516 */       return (String[])this.paramList.toArray(new String[this.paramList.length()]);
/*     */     }
/*     */ 
/*     */     void addTypeToParamList() {
/* 520 */       if (this.typeId.length() > 0) {
/* 521 */         this.paramList.append(this.typeId.toString());
/* 522 */         this.typeId.setLength(0);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.SeeTagImpl
 * JD-Core Version:    0.6.2
 */