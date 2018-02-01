/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AttributeTree.ValueKind;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.CommentTree;
/*     */ import com.sun.source.doctree.DeprecatedTree;
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocRootTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.doctree.DocTree.Kind;
/*     */ import com.sun.source.doctree.DocTreeVisitor;
/*     */ import com.sun.source.doctree.EndElementTree;
/*     */ import com.sun.source.doctree.EntityTree;
/*     */ import com.sun.source.doctree.ErroneousTree;
/*     */ import com.sun.source.doctree.IdentifierTree;
/*     */ import com.sun.source.doctree.InheritDocTree;
/*     */ import com.sun.source.doctree.LinkTree;
/*     */ import com.sun.source.doctree.LiteralTree;
/*     */ import com.sun.source.doctree.ParamTree;
/*     */ import com.sun.source.doctree.ReferenceTree;
/*     */ import com.sun.source.doctree.ReturnTree;
/*     */ import com.sun.source.doctree.SeeTree;
/*     */ import com.sun.source.doctree.SerialDataTree;
/*     */ import com.sun.source.doctree.SerialFieldTree;
/*     */ import com.sun.source.doctree.SerialTree;
/*     */ import com.sun.source.doctree.SinceTree;
/*     */ import com.sun.source.doctree.StartElementTree;
/*     */ import com.sun.source.doctree.TextTree;
/*     */ import com.sun.source.doctree.ThrowsTree;
/*     */ import com.sun.source.doctree.UnknownBlockTagTree;
/*     */ import com.sun.source.doctree.UnknownInlineTagTree;
/*     */ import com.sun.source.doctree.ValueTree;
/*     */ import com.sun.source.doctree.VersionTree;
/*     */ import com.sun.tools.javac.util.Convert;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DocPretty
/*     */   implements DocTreeVisitor<Void, Void>
/*     */ {
/*     */   final Writer out;
/*  54 */   int lmargin = 0;
/*     */ 
/* 116 */   final String lineSep = System.getProperty("line.separator");
/*     */ 
/*     */   public DocPretty(Writer paramWriter)
/*     */   {
/*  57 */     this.out = paramWriter;
/*     */   }
/*     */ 
/*     */   public void print(DocTree paramDocTree) throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  64 */       if (paramDocTree == null)
/*  65 */         print("/*missing*/");
/*     */       else
/*  67 */         paramDocTree.accept(this, null);
/*     */     }
/*     */     catch (UncheckedIOException localUncheckedIOException) {
/*  70 */       throw new IOException(localUncheckedIOException.getMessage(), localUncheckedIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void print(Object paramObject)
/*     */     throws IOException
/*     */   {
/*  78 */     this.out.write(Convert.escapeUnicode(paramObject.toString()));
/*     */   }
/*     */ 
/*     */   public void print(List<? extends DocTree> paramList)
/*     */     throws IOException
/*     */   {
/*  85 */     for (DocTree localDocTree : paramList)
/*  86 */       print(localDocTree);
/*     */   }
/*     */ 
/*     */   protected void print(List<? extends DocTree> paramList, String paramString)
/*     */     throws IOException
/*     */   {
/*  94 */     if (paramList.isEmpty())
/*  95 */       return;
/*  96 */     int i = 1;
/*  97 */     for (DocTree localDocTree : paramList) {
/*  98 */       if (i == 0)
/*  99 */         print(paramString);
/* 100 */       print(localDocTree);
/* 101 */       i = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void println()
/*     */     throws IOException
/*     */   {
/* 108 */     this.out.write(this.lineSep);
/*     */   }
/*     */ 
/*     */   protected void printTagName(DocTree paramDocTree) throws IOException {
/* 112 */     this.out.write("@");
/* 113 */     this.out.write(paramDocTree.getKind().tagName);
/*     */   }
/*     */ 
/*     */   public Void visitAttribute(AttributeTree paramAttributeTree, Void paramVoid)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       print(paramAttributeTree.getName());
/*     */       String str;
/* 135 */       switch (1.$SwitchMap$com$sun$source$doctree$AttributeTree$ValueKind[paramAttributeTree.getValueKind().ordinal()]) {
/*     */       case 1:
/* 137 */         str = null;
/* 138 */         break;
/*     */       case 2:
/* 140 */         str = "";
/* 141 */         break;
/*     */       case 3:
/* 143 */         str = "'";
/* 144 */         break;
/*     */       case 4:
/* 146 */         str = "\"";
/* 147 */         break;
/*     */       default:
/* 149 */         throw new AssertionError();
/*     */       }
/* 151 */       if (str != null) {
/* 152 */         print("=" + str);
/* 153 */         print(paramAttributeTree.getValue());
/* 154 */         print(str);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 157 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitAuthor(AuthorTree paramAuthorTree, Void paramVoid) {
/*     */     try {
/* 164 */       printTagName(paramAuthorTree);
/* 165 */       print(" ");
/* 166 */       print(paramAuthorTree.getName());
/*     */     } catch (IOException localIOException) {
/* 168 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitComment(CommentTree paramCommentTree, Void paramVoid) {
/*     */     try {
/* 175 */       print(paramCommentTree.getBody());
/*     */     } catch (IOException localIOException) {
/* 177 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitDeprecated(DeprecatedTree paramDeprecatedTree, Void paramVoid) {
/*     */     try {
/* 184 */       printTagName(paramDeprecatedTree);
/* 185 */       if (!paramDeprecatedTree.getBody().isEmpty()) {
/* 186 */         print(" ");
/* 187 */         print(paramDeprecatedTree.getBody());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 190 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitDocComment(DocCommentTree paramDocCommentTree, Void paramVoid) {
/*     */     try {
/* 197 */       List localList1 = paramDocCommentTree.getFirstSentence();
/* 198 */       List localList2 = paramDocCommentTree.getBody();
/* 199 */       List localList3 = paramDocCommentTree.getBlockTags();
/* 200 */       print(localList1);
/* 201 */       if ((!localList1.isEmpty()) && (!localList2.isEmpty()))
/* 202 */         print(" ");
/* 203 */       print(localList2);
/* 204 */       if (((!localList1.isEmpty()) || (!localList2.isEmpty())) && (!localList3.isEmpty()))
/* 205 */         print("\n");
/* 206 */       print(localList3, "\n");
/*     */     } catch (IOException localIOException) {
/* 208 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitDocRoot(DocRootTree paramDocRootTree, Void paramVoid) {
/*     */     try {
/* 215 */       print("{");
/* 216 */       printTagName(paramDocRootTree);
/* 217 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 219 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitEndElement(EndElementTree paramEndElementTree, Void paramVoid) {
/*     */     try {
/* 226 */       print("</");
/* 227 */       print(paramEndElementTree.getName());
/* 228 */       print(">");
/*     */     } catch (IOException localIOException) {
/* 230 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitEntity(EntityTree paramEntityTree, Void paramVoid) {
/*     */     try {
/* 237 */       print("&");
/* 238 */       print(paramEntityTree.getName());
/* 239 */       print(";");
/*     */     } catch (IOException localIOException) {
/* 241 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitErroneous(ErroneousTree paramErroneousTree, Void paramVoid) {
/*     */     try {
/* 248 */       print(paramErroneousTree.getBody());
/*     */     } catch (IOException localIOException) {
/* 250 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitIdentifier(IdentifierTree paramIdentifierTree, Void paramVoid) {
/*     */     try {
/* 257 */       print(paramIdentifierTree.getName());
/*     */     } catch (IOException localIOException) {
/* 259 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitInheritDoc(InheritDocTree paramInheritDocTree, Void paramVoid) {
/*     */     try {
/* 266 */       print("{");
/* 267 */       printTagName(paramInheritDocTree);
/* 268 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 270 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitLink(LinkTree paramLinkTree, Void paramVoid) {
/*     */     try {
/* 277 */       print("{");
/* 278 */       printTagName(paramLinkTree);
/* 279 */       print(" ");
/* 280 */       print(paramLinkTree.getReference());
/* 281 */       if (!paramLinkTree.getLabel().isEmpty()) {
/* 282 */         print(" ");
/* 283 */         print(paramLinkTree.getLabel());
/*     */       }
/* 285 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 287 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitLiteral(LiteralTree paramLiteralTree, Void paramVoid) {
/*     */     try {
/* 294 */       print("{");
/* 295 */       printTagName(paramLiteralTree);
/* 296 */       print(" ");
/* 297 */       print(paramLiteralTree.getBody());
/* 298 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 300 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitParam(ParamTree paramParamTree, Void paramVoid) {
/*     */     try {
/* 307 */       printTagName(paramParamTree);
/* 308 */       print(" ");
/* 309 */       if (paramParamTree.isTypeParameter()) print("<");
/* 310 */       print(paramParamTree.getName());
/* 311 */       if (paramParamTree.isTypeParameter()) print(">");
/* 312 */       if (!paramParamTree.getDescription().isEmpty()) {
/* 313 */         print(" ");
/* 314 */         print(paramParamTree.getDescription());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 317 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 319 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitReference(ReferenceTree paramReferenceTree, Void paramVoid) {
/*     */     try {
/* 324 */       print(paramReferenceTree.getSignature());
/*     */     } catch (IOException localIOException) {
/* 326 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 328 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitReturn(ReturnTree paramReturnTree, Void paramVoid) {
/*     */     try {
/* 333 */       printTagName(paramReturnTree);
/* 334 */       print(" ");
/* 335 */       print(paramReturnTree.getDescription());
/*     */     } catch (IOException localIOException) {
/* 337 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 339 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSee(SeeTree paramSeeTree, Void paramVoid) {
/*     */     try {
/* 344 */       printTagName(paramSeeTree);
/* 345 */       i = 1;
/* 346 */       j = 1;
/* 347 */       for (DocTree localDocTree : paramSeeTree.getReference()) {
/* 348 */         if (j != 0) print(" ");
/* 349 */         j = (i != 0) && ((localDocTree instanceof ReferenceTree)) ? 1 : 0;
/* 350 */         i = 0;
/* 351 */         print(localDocTree);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */       int i;
/*     */       int j;
/* 354 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 356 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSerial(SerialTree paramSerialTree, Void paramVoid) {
/*     */     try {
/* 361 */       printTagName(paramSerialTree);
/* 362 */       if (!paramSerialTree.getDescription().isEmpty()) {
/* 363 */         print(" ");
/* 364 */         print(paramSerialTree.getDescription());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 367 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 369 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSerialData(SerialDataTree paramSerialDataTree, Void paramVoid) {
/*     */     try {
/* 374 */       printTagName(paramSerialDataTree);
/* 375 */       if (!paramSerialDataTree.getDescription().isEmpty()) {
/* 376 */         print(" ");
/* 377 */         print(paramSerialDataTree.getDescription());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 380 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 382 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSerialField(SerialFieldTree paramSerialFieldTree, Void paramVoid) {
/*     */     try {
/* 387 */       printTagName(paramSerialFieldTree);
/* 388 */       print(" ");
/* 389 */       print(paramSerialFieldTree.getName());
/* 390 */       print(" ");
/* 391 */       print(paramSerialFieldTree.getType());
/* 392 */       if (!paramSerialFieldTree.getDescription().isEmpty()) {
/* 393 */         print(" ");
/* 394 */         print(paramSerialFieldTree.getDescription());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 397 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 399 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSince(SinceTree paramSinceTree, Void paramVoid) {
/*     */     try {
/* 404 */       printTagName(paramSinceTree);
/* 405 */       print(" ");
/* 406 */       print(paramSinceTree.getBody());
/*     */     } catch (IOException localIOException) {
/* 408 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitStartElement(StartElementTree paramStartElementTree, Void paramVoid) {
/*     */     try {
/* 415 */       print("<");
/* 416 */       print(paramStartElementTree.getName());
/* 417 */       List localList = paramStartElementTree.getAttributes();
/* 418 */       if (!localList.isEmpty()) {
/* 419 */         print(" ");
/* 420 */         print(localList);
/* 421 */         DocTree localDocTree = (DocTree)paramStartElementTree.getAttributes().get(localList.size() - 1);
/* 422 */         if ((paramStartElementTree.isSelfClosing()) && ((localDocTree instanceof AttributeTree)) && 
/* 423 */           (((AttributeTree)localDocTree)
/* 423 */           .getValueKind() == AttributeTree.ValueKind.UNQUOTED))
/* 424 */           print(" ");
/*     */       }
/* 426 */       if (paramStartElementTree.isSelfClosing())
/* 427 */         print("/");
/* 428 */       print(">");
/*     */     } catch (IOException localIOException) {
/* 430 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 432 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitText(TextTree paramTextTree, Void paramVoid) {
/*     */     try {
/* 437 */       print(paramTextTree.getBody());
/*     */     } catch (IOException localIOException) {
/* 439 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 441 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitThrows(ThrowsTree paramThrowsTree, Void paramVoid) {
/*     */     try {
/* 446 */       printTagName(paramThrowsTree);
/* 447 */       print(" ");
/* 448 */       print(paramThrowsTree.getExceptionName());
/* 449 */       if (!paramThrowsTree.getDescription().isEmpty()) {
/* 450 */         print(" ");
/* 451 */         print(paramThrowsTree.getDescription());
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 454 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 456 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, Void paramVoid) {
/*     */     try {
/* 461 */       print("@");
/* 462 */       print(paramUnknownBlockTagTree.getTagName());
/* 463 */       print(" ");
/* 464 */       print(paramUnknownBlockTagTree.getContent());
/*     */     } catch (IOException localIOException) {
/* 466 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 468 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, Void paramVoid) {
/*     */     try {
/* 473 */       print("{");
/* 474 */       print("@");
/* 475 */       print(paramUnknownInlineTagTree.getTagName());
/* 476 */       print(" ");
/* 477 */       print(paramUnknownInlineTagTree.getContent());
/* 478 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 480 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 482 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitValue(ValueTree paramValueTree, Void paramVoid) {
/*     */     try {
/* 487 */       print("{");
/* 488 */       printTagName(paramValueTree);
/* 489 */       if (paramValueTree.getReference() != null) {
/* 490 */         print(" ");
/* 491 */         print(paramValueTree.getReference());
/*     */       }
/* 493 */       print("}");
/*     */     } catch (IOException localIOException) {
/* 495 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 497 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitVersion(VersionTree paramVersionTree, Void paramVoid) {
/*     */     try {
/* 502 */       printTagName(paramVersionTree);
/* 503 */       print(" ");
/* 504 */       print(paramVersionTree.getBody());
/*     */     } catch (IOException localIOException) {
/* 506 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitOther(DocTree paramDocTree, Void paramVoid) {
/*     */     try {
/* 513 */       print("(UNKNOWN: " + paramDocTree + ")");
/* 514 */       println();
/*     */     } catch (IOException localIOException) {
/* 516 */       throw new UncheckedIOException(localIOException);
/*     */     }
/* 518 */     return null;
/*     */   }
/*     */ 
/*     */   private static class UncheckedIOException extends Error
/*     */   {
/*     */     static final long serialVersionUID = -4032692679158424751L;
/*     */ 
/*     */     UncheckedIOException(IOException paramIOException)
/*     */     {
/* 126 */       super(paramIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.DocPretty
 * JD-Core Version:    0.6.2
 */