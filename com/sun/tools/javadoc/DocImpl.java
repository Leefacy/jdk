/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.tree.DocCommentTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.CollationKey;
/*     */ import java.text.Collator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.tools.FileObject;
/*     */ 
/*     */ public abstract class DocImpl
/*     */   implements Doc, Comparable<Object>
/*     */ {
/*     */   protected final DocEnv env;
/*     */   protected TreePath treePath;
/*     */   private Comment comment;
/*  80 */   private CollationKey collationkey = null;
/*     */   protected String documentation;
/*     */   private Tag[] firstSentence;
/*     */   private Tag[] inlineTags;
/*     */ 
/*     */   DocImpl(DocEnv paramDocEnv, TreePath paramTreePath)
/*     */   {
/* 101 */     this.treePath = paramTreePath;
/* 102 */     this.documentation = getCommentText(paramTreePath);
/* 103 */     this.env = paramDocEnv;
/*     */   }
/*     */ 
/*     */   private static String getCommentText(TreePath paramTreePath) {
/* 107 */     if (paramTreePath == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit();
/* 111 */     JCTree localJCTree = (JCTree)paramTreePath.getLeaf();
/* 112 */     return localJCCompilationUnit.docComments.getCommentText(localJCTree);
/*     */   }
/*     */ 
/*     */   protected String documentation()
/*     */   {
/* 120 */     if (this.documentation == null) this.documentation = "";
/* 121 */     return this.documentation;
/*     */   }
/*     */ 
/*     */   Comment comment()
/*     */   {
/* 128 */     if (this.comment == null) {
/* 129 */       String str = documentation();
/* 130 */       if ((this.env.doclint != null) && (this.treePath != null))
/*     */       {
/* 132 */         if (str
/* 132 */           .equals(getCommentText(this.treePath)))
/*     */         {
/* 133 */           this.env.doclint.scan(this.treePath);
/*     */         }
/*     */       }
/* 135 */       this.comment = new Comment(this, str);
/*     */     }
/* 137 */     return this.comment;
/*     */   }
/*     */ 
/*     */   public String commentText()
/*     */   {
/* 145 */     return comment().commentText();
/*     */   }
/*     */ 
/*     */   public Tag[] tags()
/*     */   {
/* 154 */     return comment().tags();
/*     */   }
/*     */ 
/*     */   public Tag[] tags(String paramString)
/*     */   {
/* 165 */     return comment().tags(paramString);
/*     */   }
/*     */ 
/*     */   public SeeTag[] seeTags()
/*     */   {
/* 174 */     return comment().seeTags();
/*     */   }
/*     */ 
/*     */   public Tag[] inlineTags() {
/* 178 */     if (this.inlineTags == null) {
/* 179 */       this.inlineTags = Comment.getInlineTags(this, commentText());
/*     */     }
/* 181 */     return this.inlineTags;
/*     */   }
/*     */ 
/*     */   public Tag[] firstSentenceTags() {
/* 185 */     if (this.firstSentence == null)
/*     */     {
/* 187 */       inlineTags();
/*     */       try {
/* 189 */         this.env.setSilent(true);
/* 190 */         this.firstSentence = Comment.firstSentenceTags(this, commentText());
/*     */ 
/* 192 */         this.env.setSilent(false); } finally { this.env.setSilent(false); }
/*     */ 
/*     */     }
/* 195 */     return this.firstSentence;
/*     */   }
/*     */ 
/*     */   String readHTMLDocumentation(InputStream paramInputStream, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 202 */     byte[] arrayOfByte = new byte[paramInputStream.available()];
/*     */     try {
/* 204 */       localObject1 = new DataInputStream(paramInputStream);
/* 205 */       ((DataInputStream)localObject1).readFully(arrayOfByte);
/*     */     } finally {
/* 207 */       paramInputStream.close();
/*     */     }
/* 209 */     Object localObject1 = this.env.getEncoding();
/* 210 */     String str1 = localObject1 != null ? new String(arrayOfByte, (String)localObject1) : new String(arrayOfByte);
/*     */ 
/* 213 */     Pattern localPattern = Pattern.compile("(?is).*<body\\b[^>]*>(.*)</body\\b.*");
/* 214 */     Matcher localMatcher = localPattern.matcher(str1);
/* 215 */     if (localMatcher.matches()) {
/* 216 */       return localMatcher.group(1);
/*     */     }
/* 218 */     String str2 = str1.matches("(?is).*<body\\b.*") ? "javadoc.End_body_missing_from_html_file" : "javadoc.Body_missing_from_html_file";
/*     */ 
/* 221 */     this.env.error(SourcePositionImpl.make(paramFileObject, -1, null), str2);
/* 222 */     return "";
/*     */   }
/*     */ 
/*     */   public String getRawCommentText()
/*     */   {
/* 232 */     return documentation();
/*     */   }
/*     */ 
/*     */   public void setRawCommentText(String paramString)
/*     */   {
/* 241 */     this.treePath = null;
/* 242 */     this.documentation = paramString;
/* 243 */     this.comment = null;
/*     */   }
/*     */ 
/*     */   void setTreePath(TreePath paramTreePath)
/*     */   {
/* 250 */     this.treePath = paramTreePath;
/* 251 */     this.documentation = getCommentText(paramTreePath);
/* 252 */     this.comment = null;
/*     */   }
/*     */ 
/*     */   CollationKey key()
/*     */   {
/* 259 */     if (this.collationkey == null) {
/* 260 */       this.collationkey = generateKey();
/*     */     }
/* 262 */     return this.collationkey;
/*     */   }
/*     */ 
/*     */   CollationKey generateKey()
/*     */   {
/* 271 */     String str = name();
/*     */ 
/* 273 */     return this.env.doclocale.collator.getCollationKey(str);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 281 */     return qualifiedName();
/*     */   }
/*     */ 
/*     */   public abstract String name();
/*     */ 
/*     */   public abstract String qualifiedName();
/*     */ 
/*     */   public int compareTo(Object paramObject)
/*     */   {
/* 313 */     return key().compareTo(((DocImpl)paramObject).key());
/*     */   }
/*     */ 
/*     */   public boolean isField()
/*     */   {
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEnumConstant()
/*     */   {
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isConstructor()
/*     */   {
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMethod()
/*     */   {
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAnnotationTypeElement()
/*     */   {
/* 361 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/* 371 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isException()
/*     */   {
/* 380 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isError()
/*     */   {
/* 389 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEnum()
/*     */   {
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAnnotationType()
/*     */   {
/* 407 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isOrdinaryClass()
/*     */   {
/* 418 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isClass()
/*     */   {
/* 430 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract boolean isIncluded();
/*     */ 
/*     */   public SourcePosition position()
/*     */   {
/* 442 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.DocImpl
 * JD-Core Version:    0.6.2
 */