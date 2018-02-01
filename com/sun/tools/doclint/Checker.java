/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocRootTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.doctree.DocTree.Kind;
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
/*     */ import com.sun.source.doctree.SerialDataTree;
/*     */ import com.sun.source.doctree.SerialFieldTree;
/*     */ import com.sun.source.doctree.SinceTree;
/*     */ import com.sun.source.doctree.StartElementTree;
/*     */ import com.sun.source.doctree.TextTree;
/*     */ import com.sun.source.doctree.ThrowsTree;
/*     */ import com.sun.source.doctree.UnknownBlockTagTree;
/*     */ import com.sun.source.doctree.UnknownInlineTagTree;
/*     */ import com.sun.source.doctree.ValueTree;
/*     */ import com.sun.source.doctree.VersionTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.DocTreePath;
/*     */ import com.sun.source.util.DocTreePathScanner;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.tree.DocPretty;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Deque;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class Checker extends DocTreePathScanner<Void, Void>
/*     */ {
/*     */   final Env env;
/*  98 */   Set<Element> foundParams = new HashSet();
/*  99 */   Set<TypeMirror> foundThrows = new HashSet();
/* 100 */   Map<Element, Set<String>> foundAnchors = new HashMap();
/* 101 */   boolean foundInheritDoc = false;
/* 102 */   boolean foundReturn = false;
/*     */   private Deque<TagStackItem> tagStack;
/*     */   private HtmlTag currHeaderTag;
/*     */   private final int implicitHeaderLevel;
/* 624 */   private static final Pattern validName = Pattern.compile("[A-Za-z][A-Za-z0-9-_:.]*");
/*     */ 
/* 626 */   private static final Pattern validNumber = Pattern.compile("-?[0-9]+");
/*     */ 
/* 629 */   private static final Pattern docRoot = Pattern.compile("(?i)(\\{@docRoot *\\}/?)?(.*)");
/*     */ 
/*     */   Checker(Env paramEnv)
/*     */   {
/* 137 */     paramEnv.getClass();
/* 138 */     this.env = paramEnv;
/* 139 */     this.tagStack = new LinkedList();
/* 140 */     this.implicitHeaderLevel = paramEnv.implicitHeaderLevel;
/*     */   }
/*     */ 
/*     */   public Void scan(DocCommentTree paramDocCommentTree, TreePath paramTreePath) {
/* 144 */     this.env.setCurrent(paramTreePath, paramDocCommentTree);
/*     */ 
/* 146 */     int i = !this.env.currOverriddenMethods.isEmpty() ? 1 : 0;
/*     */     Object localObject;
/* 148 */     if (paramTreePath.getLeaf() == paramTreePath.getCompilationUnit())
/*     */     {
/* 153 */       localObject = paramTreePath.getCompilationUnit().getSourceFile();
/* 154 */       boolean bool = ((JavaFileObject)localObject).isNameCompatible("package-info", JavaFileObject.Kind.SOURCE);
/* 155 */       if (paramDocCommentTree == null) {
/* 156 */         if (bool)
/* 157 */           reportMissing("dc.missing.comment", new Object[0]);
/* 158 */         return null;
/*     */       }
/* 160 */       if (!bool) {
/* 161 */         reportReference("dc.unexpected.comment", new Object[0]);
/*     */       }
/*     */     }
/* 164 */     else if (paramDocCommentTree == null) {
/* 165 */       if ((!isSynthetic()) && (i == 0))
/* 166 */         reportMissing("dc.missing.comment", new Object[0]);
/* 167 */       return null;
/*     */     }
/*     */ 
/* 171 */     this.tagStack.clear();
/* 172 */     this.currHeaderTag = null;
/*     */ 
/* 174 */     this.foundParams.clear();
/* 175 */     this.foundThrows.clear();
/* 176 */     this.foundInheritDoc = false;
/* 177 */     this.foundReturn = false;
/*     */ 
/* 179 */     scan(new DocTreePath(paramTreePath, paramDocCommentTree), null);
/*     */ 
/* 181 */     if (i == 0) {
/* 182 */       switch (this.env.currElement.getKind()) {
/*     */       case METHOD:
/*     */       case CONSTRUCTOR:
/* 185 */         localObject = (ExecutableElement)this.env.currElement;
/* 186 */         checkParamsDocumented(((ExecutableElement)localObject).getTypeParameters());
/* 187 */         checkParamsDocumented(((ExecutableElement)localObject).getParameters());
/* 188 */         switch (localObject.getReturnType().getKind()) {
/*     */         case VOID:
/*     */         case NONE:
/* 191 */           break;
/*     */         default:
/* 193 */           if ((!this.foundReturn) && (!this.foundInheritDoc))
/*     */           {
/* 195 */             if (!this.env.types
/* 195 */               .isSameType(((ExecutableElement)localObject)
/* 195 */               .getReturnType(), this.env.java_lang_Void))
/* 196 */               reportMissing("dc.missing.return", new Object[0]); 
/*     */           }
/*     */           break;
/*     */         }
/* 199 */         checkThrowsDocumented(((ExecutableElement)localObject).getThrownTypes());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   private void reportMissing(String paramString, Object[] paramArrayOfObject) {
/* 208 */     this.env.messages.report(Messages.Group.MISSING, Diagnostic.Kind.WARNING, this.env.currPath.getLeaf(), paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private void reportReference(String paramString, Object[] paramArrayOfObject) {
/* 212 */     this.env.messages.report(Messages.Group.REFERENCE, Diagnostic.Kind.WARNING, this.env.currPath.getLeaf(), paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Void visitDocComment(DocCommentTree paramDocCommentTree, Void paramVoid)
/*     */   {
/* 217 */     super.visitDocComment(paramDocCommentTree, paramVoid);
/* 218 */     for (TagStackItem localTagStackItem : this.tagStack) {
/* 219 */       warnIfEmpty(localTagStackItem, null);
/* 220 */       if ((localTagStackItem.tree.getKind() == DocTree.Kind.START_ELEMENT) && (localTagStackItem.tag.endKind == HtmlTag.EndKind.REQUIRED))
/*     */       {
/* 222 */         StartElementTree localStartElementTree = (StartElementTree)localTagStackItem.tree;
/* 223 */         this.env.messages.error(Messages.Group.HTML, localStartElementTree, "dc.tag.not.closed", new Object[] { localStartElementTree.getName() });
/*     */       }
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitText(TextTree paramTextTree, Void paramVoid)
/*     */   {
/* 234 */     if (hasNonWhitespace(paramTextTree)) {
/* 235 */       checkAllowsText(paramTextTree);
/* 236 */       markEnclosingTag(Flag.HAS_TEXT);
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitEntity(EntityTree paramEntityTree, Void paramVoid)
/*     */   {
/* 243 */     checkAllowsText(paramEntityTree);
/* 244 */     markEnclosingTag(Flag.HAS_TEXT);
/* 245 */     String str = paramEntityTree.getName().toString();
/* 246 */     if (str.startsWith("#"))
/*     */     {
/* 249 */       int i = StringUtils.toLowerCase(str).startsWith("#x") ? 
/* 248 */         Integer.parseInt(str
/* 248 */         .substring(2), 
/* 248 */         16) : 
/* 249 */         Integer.parseInt(str
/* 249 */         .substring(1), 
/* 249 */         10);
/* 250 */       if (!Entity.isValid(i))
/* 251 */         this.env.messages.error(Messages.Group.HTML, paramEntityTree, "dc.entity.invalid", new Object[] { str });
/*     */     }
/* 253 */     else if (!Entity.isValid(str)) {
/* 254 */       this.env.messages.error(Messages.Group.HTML, paramEntityTree, "dc.entity.invalid", new Object[] { str });
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   void checkAllowsText(DocTree paramDocTree) {
/* 260 */     TagStackItem localTagStackItem = (TagStackItem)this.tagStack.peek();
/* 261 */     if ((localTagStackItem != null) && 
/* 262 */       (localTagStackItem.tree
/* 262 */       .getKind() == DocTree.Kind.START_ELEMENT) && 
/* 263 */       (!localTagStackItem.tag
/* 263 */       .acceptsText()) && 
/* 264 */       (localTagStackItem.flags.add(Flag.REPORTED_BAD_INLINE)))
/* 265 */       this.env.messages.error(Messages.Group.HTML, paramDocTree, "dc.text.not.allowed", new Object[] { ((StartElementTree)localTagStackItem.tree)
/* 266 */         .getName() });
/*     */   }
/*     */ 
/*     */   public Void visitStartElement(StartElementTree paramStartElementTree, Void paramVoid)
/*     */   {
/* 277 */     Name localName = paramStartElementTree.getName();
/* 278 */     HtmlTag localHtmlTag = HtmlTag.get(localName);
/*     */     Object localObject1;
/*     */     TagStackItem localTagStackItem2;
/* 279 */     if (localHtmlTag == null) {
/* 280 */       this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.unknown", new Object[] { localName });
/*     */     } else {
/* 282 */       int i = 0;
/* 283 */       for (localObject1 = this.tagStack.iterator(); ((Iterator)localObject1).hasNext(); ) { localTagStackItem2 = (TagStackItem)((Iterator)localObject1).next();
/* 284 */         if (localTagStackItem2.tag.accepts(localHtmlTag)) {
/* 285 */           while (this.tagStack.peek() != localTagStackItem2) {
/* 286 */             warnIfEmpty((TagStackItem)this.tagStack.peek(), null);
/* 287 */             this.tagStack.pop();
/*     */           }
/* 289 */           i = 1;
/* 290 */           break;
/* 291 */         }if (localTagStackItem2.tag.endKind != HtmlTag.EndKind.OPTIONAL) {
/* 292 */           i = 1;
/* 293 */           break;
/*     */         }
/*     */       }
/* 296 */       if ((i == 0) && (HtmlTag.BODY.accepts(localHtmlTag))) {
/* 297 */         while (!this.tagStack.isEmpty()) {
/* 298 */           warnIfEmpty((TagStackItem)this.tagStack.peek(), null);
/* 299 */           this.tagStack.pop();
/*     */         }
/*     */       }
/*     */ 
/* 303 */       markEnclosingTag(Flag.HAS_ELEMENT);
/* 304 */       checkStructure(paramStartElementTree, localHtmlTag);
/*     */ 
/* 307 */       switch (1.$SwitchMap$com$sun$tools$doclint$HtmlTag[localHtmlTag.ordinal()]) { case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 310 */         checkHeader(paramStartElementTree, localHtmlTag);
/*     */       }
/*     */ 
/* 314 */       if (localHtmlTag.flags.contains(HtmlTag.Flag.NO_NEST)) {
/* 315 */         for (localObject1 = this.tagStack.iterator(); ((Iterator)localObject1).hasNext(); ) { localTagStackItem2 = (TagStackItem)((Iterator)localObject1).next();
/* 316 */           if (localHtmlTag == localTagStackItem2.tag) {
/* 317 */             this.env.messages.warning(Messages.Group.HTML, paramStartElementTree, "dc.tag.nested.not.allowed", new Object[] { localName });
/* 318 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 325 */     if (paramStartElementTree.isSelfClosing()) {
/* 326 */       this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.self.closing", new Object[] { localName });
/*     */     }
/*     */     try
/*     */     {
/* 330 */       TagStackItem localTagStackItem1 = (TagStackItem)this.tagStack.peek();
/* 331 */       localObject1 = new TagStackItem(paramStartElementTree, localHtmlTag);
/* 332 */       this.tagStack.push(localObject1);
/*     */ 
/* 334 */       super.visitStartElement(paramStartElementTree, paramVoid);
/*     */ 
/* 337 */       if (localHtmlTag != null) {
/* 338 */         switch (localHtmlTag) {
/*     */         case CAPTION:
/* 340 */           if ((localTagStackItem1 != null) && (localTagStackItem1.tag == HtmlTag.TABLE))
/* 341 */             localTagStackItem1.flags.add(Flag.TABLE_HAS_CAPTION); break;
/*     */         case IMG:
/* 345 */           if (!((TagStackItem)localObject1).attrs.contains(HtmlTag.Attr.ALT)) {
/* 346 */             this.env.messages.error(Messages.Group.ACCESSIBILITY, paramStartElementTree, "dc.no.alt.attr.for.image", new Object[0]);
/*     */           }
/*     */           break;
/*     */         }
/*     */       }
/* 351 */       return null;
/*     */     }
/*     */     finally {
/* 354 */       if ((localHtmlTag == null) || (localHtmlTag.endKind == HtmlTag.EndKind.NONE))
/* 355 */         this.tagStack.pop();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkStructure(StartElementTree paramStartElementTree, HtmlTag paramHtmlTag) {
/* 360 */     Name localName = paramStartElementTree.getName();
/* 361 */     TagStackItem localTagStackItem = (TagStackItem)this.tagStack.peek();
/* 362 */     switch (1.$SwitchMap$com$sun$tools$doclint$HtmlTag$BlockType[paramHtmlTag.blockType.ordinal()]) {
/*     */     case 1:
/* 364 */       if ((localTagStackItem == null) || (localTagStackItem.tag.accepts(paramHtmlTag)))
/*     */         return;
/*     */       Object localObject;
/* 367 */       switch (1.$SwitchMap$com$sun$source$doctree$DocTree$Kind[localTagStackItem.tree.getKind().ordinal()]) {
/*     */       case 1:
/* 369 */         if (localTagStackItem.tag.blockType == HtmlTag.BlockType.INLINE)
/*     */         {
/* 370 */           localObject = ((StartElementTree)localTagStackItem.tree).getName();
/* 371 */           this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.not.allowed.inline.element", new Object[] { localName, localObject });
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case 2:
/*     */       case 3:
/* 380 */         localObject = localTagStackItem.tree.getKind().tagName;
/* 381 */         this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.not.allowed.inline.tag", new Object[] { localName, localObject });
/*     */ 
/* 383 */         return;
/*     */       }
/*     */ 
/* 386 */       break;
/*     */     case 2:
/* 389 */       if ((localTagStackItem == null) || (localTagStackItem.tag.accepts(paramHtmlTag))) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 3:
/*     */     case 4:
/* 395 */       if (localTagStackItem != null)
/*     */       {
/* 397 */         localTagStackItem.flags.remove(Flag.REPORTED_BAD_INLINE);
/* 398 */         if (localTagStackItem.tag.accepts(paramHtmlTag)) {
/*     */           return;
/*     */         }
/*     */       }
/*     */       break;
/*     */     case 5:
/* 404 */       this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.not.allowed", new Object[] { localName });
/* 405 */       return;
/*     */     }
/*     */ 
/* 408 */     this.env.messages.error(Messages.Group.HTML, paramStartElementTree, "dc.tag.not.allowed.here", new Object[] { localName });
/*     */   }
/*     */ 
/*     */   private void checkHeader(StartElementTree paramStartElementTree, HtmlTag paramHtmlTag)
/*     */   {
/* 413 */     if (getHeaderLevel(paramHtmlTag) > getHeaderLevel(this.currHeaderTag) + 1) {
/* 414 */       if (this.currHeaderTag == null)
/* 415 */         this.env.messages.error(Messages.Group.ACCESSIBILITY, paramStartElementTree, "dc.tag.header.sequence.1", new Object[] { paramHtmlTag });
/*     */       else {
/* 417 */         this.env.messages.error(Messages.Group.ACCESSIBILITY, paramStartElementTree, "dc.tag.header.sequence.2", new Object[] { paramHtmlTag, this.currHeaderTag });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 422 */     this.currHeaderTag = paramHtmlTag;
/*     */   }
/*     */ 
/*     */   private int getHeaderLevel(HtmlTag paramHtmlTag) {
/* 426 */     if (paramHtmlTag == null)
/* 427 */       return this.implicitHeaderLevel;
/* 428 */     switch (1.$SwitchMap$com$sun$tools$doclint$HtmlTag[paramHtmlTag.ordinal()]) { case 1:
/* 429 */       return 1;
/*     */     case 2:
/* 430 */       return 2;
/*     */     case 3:
/* 431 */       return 3;
/*     */     case 4:
/* 432 */       return 4;
/*     */     case 5:
/* 433 */       return 5;
/*     */     case 6:
/* 434 */       return 6; }
/* 435 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public Void visitEndElement(EndElementTree paramEndElementTree, Void paramVoid)
/*     */   {
/* 441 */     Name localName = paramEndElementTree.getName();
/* 442 */     HtmlTag localHtmlTag = HtmlTag.get(localName);
/* 443 */     if (localHtmlTag == null) {
/* 444 */       this.env.messages.error(Messages.Group.HTML, paramEndElementTree, "dc.tag.unknown", new Object[] { localName });
/* 445 */     } else if (localHtmlTag.endKind == HtmlTag.EndKind.NONE) {
/* 446 */       this.env.messages.error(Messages.Group.HTML, paramEndElementTree, "dc.tag.end.not.permitted", new Object[] { localName });
/*     */     } else {
/* 448 */       int i = 0;
/* 449 */       while (!this.tagStack.isEmpty()) {
/* 450 */         TagStackItem localTagStackItem1 = (TagStackItem)this.tagStack.peek();
/* 451 */         if (localHtmlTag == localTagStackItem1.tag) {
/* 452 */           switch (localHtmlTag) {
/*     */           case TABLE:
/* 454 */             if ((!localTagStackItem1.attrs.contains(HtmlTag.Attr.SUMMARY)) && 
/* 455 */               (!localTagStackItem1.flags
/* 455 */               .contains(Flag.TABLE_HAS_CAPTION)))
/*     */             {
/* 456 */               this.env.messages.error(Messages.Group.ACCESSIBILITY, paramEndElementTree, "dc.no.summary.or.caption.for.table", new Object[0]);
/*     */             }
/*     */             break;
/*     */           }
/* 460 */           warnIfEmpty(localTagStackItem1, paramEndElementTree);
/* 461 */           this.tagStack.pop();
/* 462 */           i = 1;
/* 463 */           break;
/* 464 */         }if ((localTagStackItem1.tag == null) || (localTagStackItem1.tag.endKind != HtmlTag.EndKind.REQUIRED)) {
/* 465 */           this.tagStack.pop();
/*     */         } else {
/* 467 */           int j = 0;
/* 468 */           for (TagStackItem localTagStackItem2 : this.tagStack) {
/* 469 */             if (localTagStackItem2.tag == localHtmlTag) {
/* 470 */               j = 1;
/* 471 */               break;
/*     */             }
/*     */           }
/* 474 */           if ((j != 0) && (localTagStackItem1.tree.getKind() == DocTree.Kind.START_ELEMENT)) {
/* 475 */             this.env.messages.error(Messages.Group.HTML, localTagStackItem1.tree, "dc.tag.start.unmatched", new Object[] { ((StartElementTree)localTagStackItem1.tree)
/* 476 */               .getName() });
/* 477 */             this.tagStack.pop();
/*     */           } else {
/* 479 */             this.env.messages.error(Messages.Group.HTML, paramEndElementTree, "dc.tag.end.unexpected", new Object[] { localName });
/* 480 */             i = 1;
/* 481 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 486 */       if ((i == 0) && (this.tagStack.isEmpty())) {
/* 487 */         this.env.messages.error(Messages.Group.HTML, paramEndElementTree, "dc.tag.end.unexpected", new Object[] { localName });
/*     */       }
/*     */     }
/*     */ 
/* 491 */     return (Void)super.visitEndElement(paramEndElementTree, paramVoid);
/*     */   }
/*     */ 
/*     */   void warnIfEmpty(TagStackItem paramTagStackItem, DocTree paramDocTree) {
/* 495 */     if ((paramTagStackItem.tag != null) && ((paramTagStackItem.tree instanceof StartElementTree)) && 
/* 496 */       (paramTagStackItem.tag.flags.contains(HtmlTag.Flag.EXPECT_CONTENT)) && 
/* 497 */       (!paramTagStackItem.flags
/* 497 */       .contains(Flag.HAS_TEXT)) && 
/* 498 */       (!paramTagStackItem.flags
/* 498 */       .contains(Flag.HAS_ELEMENT)) && 
/* 499 */       (!paramTagStackItem.flags
/* 499 */       .contains(Flag.HAS_INLINE_TAG)))
/*     */     {
/* 500 */       DocTree localDocTree = paramDocTree != null ? paramDocTree : paramTagStackItem.tree;
/* 501 */       Name localName = ((StartElementTree)paramTagStackItem.tree).getName();
/* 502 */       this.env.messages.warning(Messages.Group.HTML, localDocTree, "dc.tag.empty", new Object[] { localName });
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitAttribute(AttributeTree paramAttributeTree, Void paramVoid)
/*     */   {
/* 513 */     HtmlTag localHtmlTag = ((TagStackItem)this.tagStack.peek()).tag;
/* 514 */     if (localHtmlTag != null) {
/* 515 */       Name localName = paramAttributeTree.getName();
/* 516 */       HtmlTag.Attr localAttr = localHtmlTag.getAttr(localName);
/* 517 */       if (localAttr != null) {
/* 518 */         boolean bool = ((TagStackItem)this.tagStack.peek()).attrs.add(localAttr);
/* 519 */         if (!bool)
/* 520 */           this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.attr.repeated", new Object[] { localName });
/*     */       }
/* 522 */       HtmlTag.AttrKind localAttrKind = localHtmlTag.getAttrKind(localName);
/* 523 */       switch (1.$SwitchMap$com$sun$tools$doclint$HtmlTag$AttrKind[localAttrKind.ordinal()]) {
/*     */       case 1:
/* 525 */         break;
/*     */       case 2:
/* 528 */         this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.attr.unknown", new Object[] { localName });
/* 529 */         break;
/*     */       case 3:
/* 532 */         this.env.messages.warning(Messages.Group.ACCESSIBILITY, paramAttributeTree, "dc.attr.obsolete", new Object[] { localName });
/* 533 */         break;
/*     */       case 4:
/* 536 */         this.env.messages.warning(Messages.Group.ACCESSIBILITY, paramAttributeTree, "dc.attr.obsolete.use.css", new Object[] { localName });
/*     */       }
/*     */ 
/* 540 */       if (localAttr != null)
/*     */       {
/*     */         String str2;
/* 541 */         switch (1.$SwitchMap$com$sun$tools$doclint$HtmlTag$Attr[localAttr.ordinal()]) {
/*     */         case 1:
/* 543 */           if (localHtmlTag != HtmlTag.A)
/*     */           {
/*     */             break;
/*     */           }
/*     */         case 2:
/* 548 */           String str1 = getAttrValue(paramAttributeTree);
/* 549 */           if (str1 == null) {
/* 550 */             this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.anchor.value.missing", new Object[0]);
/*     */           } else {
/* 552 */             if (!validName.matcher(str1).matches()) {
/* 553 */               this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.invalid.anchor", new Object[] { str1 });
/*     */             }
/* 555 */             if (!checkAnchor(str1))
/* 556 */               this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.anchor.already.defined", new Object[] { str1 });  } break;
/*     */         case 3:
/* 562 */           if (localHtmlTag == HtmlTag.A) {
/* 563 */             str2 = getAttrValue(paramAttributeTree);
/* 564 */             if ((str2 == null) || (str2.isEmpty())) {
/* 565 */               this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.attr.lacks.value", new Object[0]);
/*     */             } else {
/* 567 */               Matcher localMatcher = docRoot.matcher(str2);
/* 568 */               if (localMatcher.matches()) {
/* 569 */                 String str3 = localMatcher.group(2);
/* 570 */                 if (!str3.isEmpty())
/* 571 */                   checkURI(paramAttributeTree, str3);
/*     */               } else {
/* 573 */                 checkURI(paramAttributeTree, str2);
/*     */               }
/*     */             }
/*     */           }
/* 576 */           break;
/*     */         case 4:
/* 580 */           if (localHtmlTag == HtmlTag.LI) {
/* 581 */             str2 = getAttrValue(paramAttributeTree);
/* 582 */             if ((str2 == null) || (str2.isEmpty()))
/* 583 */               this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.attr.lacks.value", new Object[0]);
/* 584 */             else if (!validNumber.matcher(str2).matches()) {
/* 585 */               this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.attr.not.number", new Object[0]);
/*     */             }
/*     */           }
/*     */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 595 */     return (Void)super.visitAttribute(paramAttributeTree, paramVoid);
/*     */   }
/*     */ 
/*     */   private boolean checkAnchor(String paramString) {
/* 599 */     Element localElement = getEnclosingPackageOrClass(this.env.currElement);
/* 600 */     if (localElement == null)
/* 601 */       return true;
/* 602 */     Object localObject = (Set)this.foundAnchors.get(localElement);
/* 603 */     if (localObject == null)
/* 604 */       this.foundAnchors.put(localElement, localObject = new HashSet());
/* 605 */     return ((Set)localObject).add(paramString);
/*     */   }
/*     */ 
/*     */   private Element getEnclosingPackageOrClass(Element paramElement) {
/* 609 */     while (paramElement != null) {
/* 610 */       switch (1.$SwitchMap$javax$lang$model$element$ElementKind[paramElement.getKind().ordinal()]) {
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 615 */         return paramElement;
/*     */       }
/* 617 */       paramElement = paramElement.getEnclosingElement();
/*     */     }
/*     */ 
/* 620 */     return paramElement;
/*     */   }
/*     */ 
/*     */   private String getAttrValue(AttributeTree paramAttributeTree)
/*     */   {
/* 632 */     if (paramAttributeTree.getValue() == null) {
/* 633 */       return null;
/*     */     }
/* 635 */     StringWriter localStringWriter = new StringWriter();
/*     */     try {
/* 637 */       new DocPretty(localStringWriter).print(paramAttributeTree.getValue());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 642 */     return localStringWriter.toString();
/*     */   }
/*     */ 
/*     */   private void checkURI(AttributeTree paramAttributeTree, String paramString) {
/*     */     try {
/* 647 */       URI localURI = new URI(paramString);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 649 */       this.env.messages.error(Messages.Group.HTML, paramAttributeTree, "dc.invalid.uri", new Object[] { paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitAuthor(AuthorTree paramAuthorTree, Void paramVoid)
/*     */   {
/* 658 */     warnIfEmpty(paramAuthorTree, paramAuthorTree.getName());
/* 659 */     return (Void)super.visitAuthor(paramAuthorTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitDocRoot(DocRootTree paramDocRootTree, Void paramVoid)
/*     */   {
/* 664 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 665 */     return (Void)super.visitDocRoot(paramDocRootTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitInheritDoc(InheritDocTree paramInheritDocTree, Void paramVoid)
/*     */   {
/* 670 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/*     */ 
/* 672 */     this.foundInheritDoc = true;
/* 673 */     return (Void)super.visitInheritDoc(paramInheritDocTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitLink(LinkTree paramLinkTree, Void paramVoid)
/*     */   {
/* 678 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/*     */ 
/* 680 */     HtmlTag localHtmlTag = paramLinkTree.getKind() == DocTree.Kind.LINK ? HtmlTag.CODE : HtmlTag.SPAN;
/*     */ 
/* 682 */     this.tagStack.push(new TagStackItem(paramLinkTree, localHtmlTag));
/*     */     try {
/* 684 */       return (Void)super.visitLink(paramLinkTree, paramVoid);
/*     */     } finally {
/* 686 */       this.tagStack.pop();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitLiteral(LiteralTree paramLiteralTree, Void paramVoid)
/*     */   {
/* 692 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 693 */     if (paramLiteralTree.getKind() == DocTree.Kind.CODE) {
/* 694 */       for (TagStackItem localTagStackItem : this.tagStack) {
/* 695 */         if (localTagStackItem.tag == HtmlTag.CODE) {
/* 696 */           this.env.messages.warning(Messages.Group.HTML, paramLiteralTree, "dc.tag.code.within.code", new Object[0]);
/* 697 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 701 */     return (Void)super.visitLiteral(paramLiteralTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitParam(ParamTree paramParamTree, Void paramVoid)
/*     */   {
/* 707 */     boolean bool = paramParamTree.isTypeParameter();
/* 708 */     IdentifierTree localIdentifierTree = paramParamTree.getName();
/* 709 */     Object localObject = localIdentifierTree != null ? this.env.trees.getElement(new DocTreePath(getCurrentPath(), localIdentifierTree)) : null;
/*     */ 
/* 711 */     if (localObject == null) {
/* 712 */       switch (1.$SwitchMap$javax$lang$model$element$ElementKind[this.env.currElement.getKind().ordinal()]) { case 3:
/*     */       case 5:
/* 714 */         if (!bool)
/* 715 */           this.env.messages.error(Messages.Group.REFERENCE, paramParamTree, "dc.invalid.param", new Object[0]);
/* 716 */         break;
/*     */       case 1:
/*     */       case 2:
/* 720 */         this.env.messages.error(Messages.Group.REFERENCE, localIdentifierTree, "dc.param.name.not.found", new Object[0]);
/* 721 */         break;
/*     */       case 4:
/*     */       }
/*     */ 
/* 725 */       this.env.messages.error(Messages.Group.REFERENCE, paramParamTree, "dc.invalid.param", new Object[0]);
/*     */     }
/*     */     else
/*     */     {
/* 729 */       this.foundParams.add(localObject);
/*     */     }
/*     */ 
/* 732 */     warnIfEmpty(paramParamTree, paramParamTree.getDescription());
/* 733 */     return (Void)super.visitParam(paramParamTree, paramVoid);
/*     */   }
/*     */ 
/*     */   private void checkParamsDocumented(List<? extends Element> paramList) {
/* 737 */     if (this.foundInheritDoc) {
/* 738 */       return;
/*     */     }
/* 740 */     for (Element localElement : paramList)
/* 741 */       if (!this.foundParams.contains(localElement))
/*     */       {
/* 744 */         Name localName = localElement.getKind() == ElementKind.TYPE_PARAMETER ? "<" + localElement
/* 743 */           .getSimpleName() + ">" : localElement
/* 744 */           .getSimpleName();
/* 745 */         reportMissing("dc.missing.param", new Object[] { localName });
/*     */       }
/*     */   }
/*     */ 
/*     */   public Void visitReference(ReferenceTree paramReferenceTree, Void paramVoid)
/*     */   {
/* 752 */     String str = paramReferenceTree.getSignature();
/* 753 */     if ((str.contains("<")) || (str.contains(">"))) {
/* 754 */       this.env.messages.error(Messages.Group.REFERENCE, paramReferenceTree, "dc.type.arg.not.allowed", new Object[0]);
/*     */     }
/* 756 */     Element localElement = this.env.trees.getElement(getCurrentPath());
/* 757 */     if (localElement == null)
/* 758 */       this.env.messages.error(Messages.Group.REFERENCE, paramReferenceTree, "dc.ref.not.found", new Object[0]);
/* 759 */     return (Void)super.visitReference(paramReferenceTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitReturn(ReturnTree paramReturnTree, Void paramVoid)
/*     */   {
/* 764 */     Element localElement = this.env.trees.getElement(this.env.currPath);
/* 765 */     if ((localElement.getKind() != ElementKind.METHOD) || 
/* 766 */       (((ExecutableElement)localElement)
/* 766 */       .getReturnType().getKind() == TypeKind.VOID))
/* 767 */       this.env.messages.error(Messages.Group.REFERENCE, paramReturnTree, "dc.invalid.return", new Object[0]);
/* 768 */     this.foundReturn = true;
/* 769 */     warnIfEmpty(paramReturnTree, paramReturnTree.getDescription());
/* 770 */     return (Void)super.visitReturn(paramReturnTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitSerialData(SerialDataTree paramSerialDataTree, Void paramVoid)
/*     */   {
/* 775 */     warnIfEmpty(paramSerialDataTree, paramSerialDataTree.getDescription());
/* 776 */     return (Void)super.visitSerialData(paramSerialDataTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitSerialField(SerialFieldTree paramSerialFieldTree, Void paramVoid)
/*     */   {
/* 781 */     warnIfEmpty(paramSerialFieldTree, paramSerialFieldTree.getDescription());
/* 782 */     return (Void)super.visitSerialField(paramSerialFieldTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitSince(SinceTree paramSinceTree, Void paramVoid)
/*     */   {
/* 787 */     warnIfEmpty(paramSinceTree, paramSinceTree.getBody());
/* 788 */     return (Void)super.visitSince(paramSinceTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitThrows(ThrowsTree paramThrowsTree, Void paramVoid)
/*     */   {
/* 793 */     ReferenceTree localReferenceTree = paramThrowsTree.getExceptionName();
/* 794 */     Element localElement = this.env.trees.getElement(new DocTreePath(getCurrentPath(), localReferenceTree));
/* 795 */     if (localElement == null)
/* 796 */       this.env.messages.error(Messages.Group.REFERENCE, paramThrowsTree, "dc.ref.not.found", new Object[0]);
/* 797 */     else if (isThrowable(localElement.asType()))
/* 798 */       switch (this.env.currElement.getKind()) {
/*     */       case METHOD:
/*     */       case CONSTRUCTOR:
/* 801 */         if (!isCheckedException(localElement.asType())) break;
/* 802 */         ExecutableElement localExecutableElement = (ExecutableElement)this.env.currElement;
/* 803 */         checkThrowsDeclared(localReferenceTree, localElement.asType(), localExecutableElement.getThrownTypes());
/* 804 */         break;
/*     */       default:
/* 807 */         this.env.messages.error(Messages.Group.REFERENCE, paramThrowsTree, "dc.invalid.throws", new Object[0]); break;
/*     */       }
/*     */     else {
/* 810 */       this.env.messages.error(Messages.Group.REFERENCE, paramThrowsTree, "dc.invalid.throws", new Object[0]);
/*     */     }
/* 812 */     warnIfEmpty(paramThrowsTree, paramThrowsTree.getDescription());
/* 813 */     return (Void)scan(paramThrowsTree.getDescription(), paramVoid);
/*     */   }
/*     */ 
/*     */   private boolean isThrowable(TypeMirror paramTypeMirror) {
/* 817 */     switch (paramTypeMirror.getKind()) {
/*     */     case DECLARED:
/*     */     case TYPEVAR:
/* 820 */       return this.env.types.isAssignable(paramTypeMirror, this.env.java_lang_Throwable);
/*     */     }
/* 822 */     return false;
/*     */   }
/*     */ 
/*     */   private void checkThrowsDeclared(ReferenceTree paramReferenceTree, TypeMirror paramTypeMirror, List<? extends TypeMirror> paramList) {
/* 826 */     int i = 0;
/* 827 */     for (TypeMirror localTypeMirror : paramList) {
/* 828 */       if (this.env.types.isAssignable(paramTypeMirror, localTypeMirror)) {
/* 829 */         this.foundThrows.add(localTypeMirror);
/* 830 */         i = 1;
/*     */       }
/*     */     }
/* 833 */     if (i == 0)
/* 834 */       this.env.messages.error(Messages.Group.REFERENCE, paramReferenceTree, "dc.exception.not.thrown", new Object[] { paramTypeMirror });
/*     */   }
/*     */ 
/*     */   private void checkThrowsDocumented(List<? extends TypeMirror> paramList) {
/* 838 */     if (this.foundInheritDoc) {
/* 839 */       return;
/*     */     }
/* 841 */     for (TypeMirror localTypeMirror : paramList)
/* 842 */       if ((isCheckedException(localTypeMirror)) && (!this.foundThrows.contains(localTypeMirror)))
/* 843 */         reportMissing("dc.missing.throws", new Object[] { localTypeMirror });
/*     */   }
/*     */ 
/*     */   public Void visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, Void paramVoid)
/*     */   {
/* 849 */     checkUnknownTag(paramUnknownBlockTagTree, paramUnknownBlockTagTree.getTagName());
/* 850 */     return (Void)super.visitUnknownBlockTag(paramUnknownBlockTagTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, Void paramVoid)
/*     */   {
/* 855 */     checkUnknownTag(paramUnknownInlineTagTree, paramUnknownInlineTagTree.getTagName());
/* 856 */     return (Void)super.visitUnknownInlineTag(paramUnknownInlineTagTree, paramVoid);
/*     */   }
/*     */ 
/*     */   private void checkUnknownTag(DocTree paramDocTree, String paramString) {
/* 860 */     if ((this.env.customTags != null) && (!this.env.customTags.contains(paramString)))
/* 861 */       this.env.messages.error(Messages.Group.SYNTAX, paramDocTree, "dc.tag.unknown", new Object[] { paramString });
/*     */   }
/*     */ 
/*     */   public Void visitValue(ValueTree paramValueTree, Void paramVoid)
/*     */   {
/* 866 */     ReferenceTree localReferenceTree = paramValueTree.getReference();
/* 867 */     if ((localReferenceTree == null) || (localReferenceTree.getSignature().isEmpty())) {
/* 868 */       if (!isConstant(this.env.currElement))
/* 869 */         this.env.messages.error(Messages.Group.REFERENCE, paramValueTree, "dc.value.not.allowed.here", new Object[0]);
/*     */     } else {
/* 871 */       Element localElement = this.env.trees.getElement(new DocTreePath(getCurrentPath(), localReferenceTree));
/* 872 */       if (!isConstant(localElement)) {
/* 873 */         this.env.messages.error(Messages.Group.REFERENCE, paramValueTree, "dc.value.not.a.constant", new Object[0]);
/*     */       }
/*     */     }
/* 876 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 877 */     return (Void)super.visitValue(paramValueTree, paramVoid);
/*     */   }
/*     */ 
/*     */   private boolean isConstant(Element paramElement) {
/* 881 */     if (paramElement == null) {
/* 882 */       return false;
/*     */     }
/* 884 */     switch (paramElement.getKind()) {
/*     */     case FIELD:
/* 886 */       Object localObject = ((VariableElement)paramElement).getConstantValue();
/* 887 */       return localObject != null;
/*     */     }
/* 889 */     return false;
/*     */   }
/*     */ 
/*     */   public Void visitVersion(VersionTree paramVersionTree, Void paramVoid)
/*     */   {
/* 895 */     warnIfEmpty(paramVersionTree, paramVersionTree.getBody());
/* 896 */     return (Void)super.visitVersion(paramVersionTree, paramVoid);
/*     */   }
/*     */ 
/*     */   public Void visitErroneous(ErroneousTree paramErroneousTree, Void paramVoid)
/*     */   {
/* 901 */     this.env.messages.error(Messages.Group.SYNTAX, paramErroneousTree, null, new Object[] { paramErroneousTree.getDiagnostic().getMessage(null) });
/* 902 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean isCheckedException(TypeMirror paramTypeMirror)
/*     */   {
/* 910 */     return (!this.env.types.isAssignable(paramTypeMirror, this.env.java_lang_Error)) && 
/* 910 */       (!this.env.types
/* 910 */       .isAssignable(paramTypeMirror, this.env.java_lang_RuntimeException));
/*     */   }
/*     */ 
/*     */   private boolean isSynthetic()
/*     */   {
/* 914 */     switch (this.env.currElement.getKind())
/*     */     {
/*     */     case CONSTRUCTOR:
/* 918 */       TreePath localTreePath = this.env.currPath;
/* 919 */       return this.env.getPos(localTreePath) == this.env.getPos(localTreePath.getParentPath());
/*     */     }
/* 921 */     return false;
/*     */   }
/*     */ 
/*     */   void markEnclosingTag(Flag paramFlag) {
/* 925 */     TagStackItem localTagStackItem = (TagStackItem)this.tagStack.peek();
/* 926 */     if (localTagStackItem != null)
/* 927 */       localTagStackItem.flags.add(paramFlag);
/*     */   }
/*     */ 
/*     */   String toString(TreePath paramTreePath) {
/* 931 */     StringBuilder localStringBuilder = new StringBuilder("TreePath[");
/* 932 */     toString(paramTreePath, localStringBuilder);
/* 933 */     localStringBuilder.append("]");
/* 934 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   void toString(TreePath paramTreePath, StringBuilder paramStringBuilder) {
/* 938 */     TreePath localTreePath = paramTreePath.getParentPath();
/* 939 */     if (localTreePath != null) {
/* 940 */       toString(localTreePath, paramStringBuilder);
/* 941 */       paramStringBuilder.append(",");
/*     */     }
/* 943 */     paramStringBuilder.append(paramTreePath.getLeaf().getKind()).append(":").append(this.env.getPos(paramTreePath)).append(":S").append(this.env.getStartPos(paramTreePath));
/*     */   }
/*     */ 
/*     */   void warnIfEmpty(DocTree paramDocTree, List<? extends DocTree> paramList) {
/* 947 */     for (DocTree localDocTree : paramList) {
/* 948 */       switch (localDocTree.getKind()) {
/*     */       case TEXT:
/* 950 */         if (hasNonWhitespace((TextTree)localDocTree))
/*     */           return;
/*     */         break;
/*     */       default:
/* 954 */         return;
/*     */       }
/*     */     }
/* 957 */     this.env.messages.warning(Messages.Group.SYNTAX, paramDocTree, "dc.empty", new Object[] { paramDocTree.getKind().tagName });
/*     */   }
/*     */ 
/*     */   boolean hasNonWhitespace(TextTree paramTextTree) {
/* 961 */     String str = paramTextTree.getBody();
/* 962 */     for (int i = 0; i < str.length(); i++) {
/* 963 */       if (!Character.isWhitespace(str.charAt(i)))
/* 964 */         return true;
/*     */     }
/* 966 */     return false;
/*     */   }
/*     */ 
/*     */   public static enum Flag
/*     */   {
/* 105 */     TABLE_HAS_CAPTION, 
/* 106 */     HAS_ELEMENT, 
/* 107 */     HAS_INLINE_TAG, 
/* 108 */     HAS_TEXT, 
/* 109 */     REPORTED_BAD_INLINE; } 
/*     */   static class TagStackItem { final DocTree tree;
/*     */     final HtmlTag tag;
/*     */     final Set<HtmlTag.Attr> attrs;
/*     */     final Set<Checker.Flag> flags;
/*     */ 
/* 118 */     TagStackItem(DocTree paramDocTree, HtmlTag paramHtmlTag) { this.tree = paramDocTree;
/* 119 */       this.tag = paramHtmlTag;
/* 120 */       this.attrs = EnumSet.noneOf(HtmlTag.Attr.class);
/* 121 */       this.flags = EnumSet.noneOf(Checker.Flag.class); }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 125 */       return String.valueOf(this.tag);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.Checker
 * JD-Core Version:    0.6.2
 */