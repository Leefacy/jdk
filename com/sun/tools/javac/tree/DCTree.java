/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AttributeTree.ValueKind;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.BlockTagTree;
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
/*     */ import com.sun.source.doctree.InlineTagTree;
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
/*     */ import com.sun.tools.javac.parser.Tokens.Comment;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.DiagnosticSource;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.SimpleDiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public abstract class DCTree
/*     */   implements DocTree
/*     */ {
/*     */   public int pos;
/*     */ 
/*     */   public long getSourcePosition(DCDocComment paramDCDocComment)
/*     */   {
/*  64 */     return paramDCDocComment.comment.getSourcePos(this.pos);
/*     */   }
/*     */ 
/*     */   public JCDiagnostic.DiagnosticPosition pos(DCDocComment paramDCDocComment) {
/*  68 */     return new JCDiagnostic.SimpleDiagnosticPosition(paramDCDocComment.comment.getSourcePos(this.pos));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  74 */     StringWriter localStringWriter = new StringWriter();
/*     */     try {
/*  76 */       new DocPretty(localStringWriter).print(this);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  81 */       throw new AssertionError(localIOException);
/*     */     }
/*  83 */     return localStringWriter.toString();
/*     */   }
/*     */ 
/*     */   public static class DCAttribute extends DCTree
/*     */     implements AttributeTree
/*     */   {
/*     */     public final Name name;
/*     */     public final AttributeTree.ValueKind vkind;
/*     */     public final List<DCTree> value;
/*     */ 
/*     */     DCAttribute(Name paramName, AttributeTree.ValueKind paramValueKind, List<DCTree> paramList)
/*     */     {
/* 157 */       Assert.check(paramList == null);
/* 158 */       this.name = paramName;
/* 159 */       this.vkind = paramValueKind;
/* 160 */       this.value = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 165 */       return DocTree.Kind.ATTRIBUTE;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 170 */       return paramDocTreeVisitor.visitAttribute(this, paramD);
/*     */     }
/*     */ 
/*     */     public Name getName()
/*     */     {
/* 175 */       return this.name;
/*     */     }
/*     */ 
/*     */     public AttributeTree.ValueKind getValueKind()
/*     */     {
/* 180 */       return this.vkind;
/*     */     }
/*     */ 
/*     */     public List<DCTree> getValue()
/*     */     {
/* 185 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCAuthor extends DCTree.DCBlockTag implements AuthorTree {
/*     */     public final List<DCTree> name;
/*     */ 
/*     */     DCAuthor(List<DCTree> paramList) {
/* 193 */       this.name = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 198 */       return DocTree.Kind.AUTHOR;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 203 */       return paramDocTreeVisitor.visitAuthor(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getName()
/*     */     {
/* 208 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class DCBlockTag extends DCTree
/*     */     implements BlockTagTree
/*     */   {
/*     */     public String getTagName()
/*     */     {
/* 141 */       return getKind().tagName;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCComment extends DCTree
/*     */     implements CommentTree
/*     */   {
/*     */     public final String body;
/*     */ 
/*     */     DCComment(String paramString)
/*     */     {
/* 216 */       this.body = paramString;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 221 */       return DocTree.Kind.COMMENT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 226 */       return paramDocTreeVisitor.visitComment(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getBody()
/*     */     {
/* 231 */       return this.body;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCDeprecated extends DCTree.DCBlockTag implements DeprecatedTree {
/*     */     public final List<DCTree> body;
/*     */ 
/*     */     DCDeprecated(List<DCTree> paramList) {
/* 239 */       this.body = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 244 */       return DocTree.Kind.DEPRECATED;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 249 */       return paramDocTreeVisitor.visitDeprecated(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getBody()
/*     */     {
/* 254 */       return this.body;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCDocComment extends DCTree
/*     */     implements DocCommentTree
/*     */   {
/*     */     public final Tokens.Comment comment;
/*     */     public final List<DCTree> firstSentence;
/*     */     public final List<DCTree> body;
/*     */     public final List<DCTree> tags;
/*     */ 
/*     */     public DCDocComment(Tokens.Comment paramComment, List<DCTree> paramList1, List<DCTree> paramList2, List<DCTree> paramList3)
/*     */     {
/* 111 */       this.comment = paramComment;
/* 112 */       this.firstSentence = paramList1;
/* 113 */       this.body = paramList2;
/* 114 */       this.tags = paramList3;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind() {
/* 118 */       return DocTree.Kind.DOC_COMMENT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD) {
/* 122 */       return paramDocTreeVisitor.visitDocComment(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getFirstSentence() {
/* 126 */       return this.firstSentence;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getBody() {
/* 130 */       return this.body;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getBlockTags() {
/* 134 */       return this.tags;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCDocRoot extends DCTree.DCInlineTag
/*     */     implements DocRootTree
/*     */   {
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 262 */       return DocTree.Kind.DOC_ROOT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 267 */       return paramDocTreeVisitor.visitDocRoot(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCEndElement extends DCTree implements EndElementTree {
/*     */     public final Name name;
/*     */ 
/*     */     DCEndElement(Name paramName) {
/* 275 */       this.name = paramName;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 280 */       return DocTree.Kind.END_ELEMENT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 285 */       return paramDocTreeVisitor.visitEndElement(this, paramD);
/*     */     }
/*     */ 
/*     */     public Name getName()
/*     */     {
/* 290 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class DCEndPosTree<T extends DCEndPosTree<T>> extends DCTree
/*     */   {
/*  88 */     private int endPos = -1;
/*     */ 
/*     */     public int getEndPos(DCTree.DCDocComment paramDCDocComment) {
/*  91 */       return paramDCDocComment.comment.getSourcePos(this.endPos);
/*     */     }
/*     */ 
/*     */     public T setEndPos(int paramInt)
/*     */     {
/*  96 */       this.endPos = paramInt;
/*  97 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCEntity extends DCTree
/*     */     implements EntityTree
/*     */   {
/*     */     public final Name name;
/*     */ 
/*     */     DCEntity(Name paramName)
/*     */     {
/* 298 */       this.name = paramName;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 303 */       return DocTree.Kind.ENTITY;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 308 */       return paramDocTreeVisitor.visitEntity(this, paramD);
/*     */     }
/*     */ 
/*     */     public Name getName()
/*     */     {
/* 313 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCErroneous extends DCTree implements ErroneousTree, JCDiagnostic.DiagnosticPosition {
/*     */     public final String body;
/*     */     public final JCDiagnostic diag;
/*     */ 
/* 322 */     DCErroneous(String paramString1, JCDiagnostic.Factory paramFactory, DiagnosticSource paramDiagnosticSource, String paramString2, Object[] paramArrayOfObject) { this.body = paramString1;
/* 323 */       this.diag = paramFactory.error(paramDiagnosticSource, this, paramString2, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 328 */       return DocTree.Kind.ERRONEOUS;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 333 */       return paramDocTreeVisitor.visitErroneous(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getBody()
/*     */     {
/* 338 */       return this.body;
/*     */     }
/*     */ 
/*     */     public Diagnostic<JavaFileObject> getDiagnostic()
/*     */     {
/* 343 */       return this.diag;
/*     */     }
/*     */ 
/*     */     public JCTree getTree()
/*     */     {
/* 348 */       return null;
/*     */     }
/*     */ 
/*     */     public int getStartPosition()
/*     */     {
/* 353 */       return this.pos;
/*     */     }
/*     */ 
/*     */     public int getPreferredPosition()
/*     */     {
/* 358 */       return this.pos + this.body.length() - 1;
/*     */     }
/*     */ 
/*     */     public int getEndPosition(EndPosTable paramEndPosTable)
/*     */     {
/* 363 */       return this.pos + this.body.length();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCIdentifier extends DCTree implements IdentifierTree
/*     */   {
/*     */     public final Name name;
/*     */ 
/*     */     DCIdentifier(Name paramName) {
/* 372 */       this.name = paramName;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 377 */       return DocTree.Kind.IDENTIFIER;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 382 */       return paramDocTreeVisitor.visitIdentifier(this, paramD);
/*     */     }
/*     */ 
/*     */     public Name getName()
/*     */     {
/* 387 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCInheritDoc extends DCTree.DCInlineTag implements InheritDocTree
/*     */   {
/*     */     public DocTree.Kind getKind() {
/* 394 */       return DocTree.Kind.INHERIT_DOC;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 399 */       return paramDocTreeVisitor.visitInheritDoc(this, paramD);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class DCInlineTag extends DCTree.DCEndPosTree<DCInlineTag>
/*     */     implements InlineTagTree
/*     */   {
/*     */     public String getTagName()
/*     */     {
/* 147 */       return getKind().tagName;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCLink extends DCTree.DCInlineTag
/*     */     implements LinkTree
/*     */   {
/*     */     public final DocTree.Kind kind;
/*     */     public final DCTree.DCReference ref;
/*     */     public final List<DCTree> label;
/*     */ 
/*     */     DCLink(DocTree.Kind paramKind, DCTree.DCReference paramDCReference, List<DCTree> paramList)
/*     */     {
/* 409 */       Assert.check((paramKind == DocTree.Kind.LINK) || (paramKind == DocTree.Kind.LINK_PLAIN));
/* 410 */       this.kind = paramKind;
/* 411 */       this.ref = paramDCReference;
/* 412 */       this.label = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 417 */       return this.kind;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 422 */       return paramDocTreeVisitor.visitLink(this, paramD);
/*     */     }
/*     */ 
/*     */     public ReferenceTree getReference()
/*     */     {
/* 427 */       return this.ref;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getLabel()
/*     */     {
/* 432 */       return this.label;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCLiteral extends DCTree.DCInlineTag implements LiteralTree {
/*     */     public final DocTree.Kind kind;
/*     */     public final DCTree.DCText body;
/*     */ 
/* 441 */     DCLiteral(DocTree.Kind paramKind, DCTree.DCText paramDCText) { Assert.check((paramKind == DocTree.Kind.CODE) || (paramKind == DocTree.Kind.LITERAL));
/* 442 */       this.kind = paramKind;
/* 443 */       this.body = paramDCText;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 448 */       return this.kind;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 453 */       return paramDocTreeVisitor.visitLiteral(this, paramD);
/*     */     }
/*     */ 
/*     */     public DCTree.DCText getBody()
/*     */     {
/* 458 */       return this.body;
/*     */     }
/*     */   }
/*     */   public static class DCParam extends DCTree.DCBlockTag implements ParamTree {
/*     */     public final boolean isTypeParameter;
/*     */     public final DCTree.DCIdentifier name;
/*     */     public final List<DCTree> description;
/*     */ 
/* 468 */     DCParam(boolean paramBoolean, DCTree.DCIdentifier paramDCIdentifier, List<DCTree> paramList) { this.isTypeParameter = paramBoolean;
/* 469 */       this.name = paramDCIdentifier;
/* 470 */       this.description = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 475 */       return DocTree.Kind.PARAM;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 480 */       return paramDocTreeVisitor.visitParam(this, paramD);
/*     */     }
/*     */ 
/*     */     public boolean isTypeParameter()
/*     */     {
/* 485 */       return this.isTypeParameter;
/*     */     }
/*     */ 
/*     */     public IdentifierTree getName()
/*     */     {
/* 490 */       return this.name;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 495 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCReference extends DCTree.DCEndPosTree<DCReference> implements ReferenceTree
/*     */   {
/*     */     public final String signature;
/*     */     public final JCTree qualifierExpression;
/*     */     public final Name memberName;
/*     */     public final List<JCTree> paramTypes;
/*     */ 
/*     */     DCReference(String paramString, JCTree paramJCTree, Name paramName, List<JCTree> paramList) {
/* 510 */       this.signature = paramString;
/* 511 */       this.qualifierExpression = paramJCTree;
/* 512 */       this.memberName = paramName;
/* 513 */       this.paramTypes = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 518 */       return DocTree.Kind.REFERENCE;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 523 */       return paramDocTreeVisitor.visitReference(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getSignature()
/*     */     {
/* 528 */       return this.signature;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCReturn extends DCTree.DCBlockTag implements ReturnTree {
/*     */     public final List<DCTree> description;
/*     */ 
/*     */     DCReturn(List<DCTree> paramList) {
/* 536 */       this.description = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 541 */       return DocTree.Kind.RETURN;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 546 */       return paramDocTreeVisitor.visitReturn(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 551 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCSee extends DCTree.DCBlockTag implements SeeTree {
/*     */     public final List<DCTree> reference;
/*     */ 
/*     */     DCSee(List<DCTree> paramList) {
/* 559 */       this.reference = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 564 */       return DocTree.Kind.SEE;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 569 */       return paramDocTreeVisitor.visitSee(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getReference()
/*     */     {
/* 574 */       return this.reference;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCSerial extends DCTree.DCBlockTag implements SerialTree {
/*     */     public final List<DCTree> description;
/*     */ 
/*     */     DCSerial(List<DCTree> paramList) {
/* 582 */       this.description = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 587 */       return DocTree.Kind.SERIAL;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 592 */       return paramDocTreeVisitor.visitSerial(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 597 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCSerialData extends DCTree.DCBlockTag implements SerialDataTree {
/*     */     public final List<DCTree> description;
/*     */ 
/*     */     DCSerialData(List<DCTree> paramList) {
/* 605 */       this.description = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 610 */       return DocTree.Kind.SERIAL_DATA;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 615 */       return paramDocTreeVisitor.visitSerialData(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 620 */       return this.description;
/*     */     }
/*     */   }
/*     */   public static class DCSerialField extends DCTree.DCBlockTag implements SerialFieldTree {
/*     */     public final DCTree.DCIdentifier name;
/*     */     public final DCTree.DCReference type;
/*     */     public final List<DCTree> description;
/*     */ 
/* 630 */     DCSerialField(DCTree.DCIdentifier paramDCIdentifier, DCTree.DCReference paramDCReference, List<DCTree> paramList) { this.description = paramList;
/* 631 */       this.name = paramDCIdentifier;
/* 632 */       this.type = paramDCReference;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 637 */       return DocTree.Kind.SERIAL_FIELD;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 642 */       return paramDocTreeVisitor.visitSerialField(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 647 */       return this.description;
/*     */     }
/*     */ 
/*     */     public IdentifierTree getName()
/*     */     {
/* 652 */       return this.name;
/*     */     }
/*     */ 
/*     */     public ReferenceTree getType()
/*     */     {
/* 657 */       return this.type;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCSince extends DCTree.DCBlockTag implements SinceTree {
/*     */     public final List<DCTree> body;
/*     */ 
/*     */     DCSince(List<DCTree> paramList) {
/* 665 */       this.body = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 670 */       return DocTree.Kind.SINCE;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 675 */       return paramDocTreeVisitor.visitSince(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getBody()
/*     */     {
/* 680 */       return this.body;
/*     */     }
/*     */   }
/*     */   public static class DCStartElement extends DCTree.DCEndPosTree<DCStartElement> implements StartElementTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> attrs;
/*     */     public final boolean selfClosing;
/*     */ 
/* 690 */     DCStartElement(Name paramName, List<DCTree> paramList, boolean paramBoolean) { this.name = paramName;
/* 691 */       this.attrs = paramList;
/* 692 */       this.selfClosing = paramBoolean;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 697 */       return DocTree.Kind.START_ELEMENT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 702 */       return paramDocTreeVisitor.visitStartElement(this, paramD);
/*     */     }
/*     */ 
/*     */     public Name getName()
/*     */     {
/* 707 */       return this.name;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getAttributes()
/*     */     {
/* 712 */       return this.attrs;
/*     */     }
/*     */ 
/*     */     public boolean isSelfClosing()
/*     */     {
/* 717 */       return this.selfClosing;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCText extends DCTree implements TextTree {
/*     */     public final String text;
/*     */ 
/*     */     DCText(String paramString) {
/* 725 */       this.text = paramString;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 730 */       return DocTree.Kind.TEXT;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 735 */       return paramDocTreeVisitor.visitText(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getBody()
/*     */     {
/* 740 */       return this.text;
/*     */     }
/*     */   }
/*     */   public static class DCThrows extends DCTree.DCBlockTag implements ThrowsTree {
/*     */     public final DocTree.Kind kind;
/*     */     public final DCTree.DCReference name;
/*     */     public final List<DCTree> description;
/*     */ 
/* 750 */     DCThrows(DocTree.Kind paramKind, DCTree.DCReference paramDCReference, List<DCTree> paramList) { Assert.check((paramKind == DocTree.Kind.EXCEPTION) || (paramKind == DocTree.Kind.THROWS));
/* 751 */       this.kind = paramKind;
/* 752 */       this.name = paramDCReference;
/* 753 */       this.description = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 758 */       return this.kind;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 763 */       return paramDocTreeVisitor.visitThrows(this, paramD);
/*     */     }
/*     */ 
/*     */     public ReferenceTree getExceptionName()
/*     */     {
/* 768 */       return this.name;
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getDescription()
/*     */     {
/* 773 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCUnknownBlockTag extends DCTree.DCBlockTag implements UnknownBlockTagTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> content;
/*     */ 
/* 782 */     DCUnknownBlockTag(Name paramName, List<DCTree> paramList) { this.name = paramName;
/* 783 */       this.content = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 788 */       return DocTree.Kind.UNKNOWN_BLOCK_TAG;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 793 */       return paramDocTreeVisitor.visitUnknownBlockTag(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getTagName()
/*     */     {
/* 798 */       return this.name.toString();
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getContent()
/*     */     {
/* 803 */       return this.content;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCUnknownInlineTag extends DCTree.DCInlineTag implements UnknownInlineTagTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> content;
/*     */ 
/* 812 */     DCUnknownInlineTag(Name paramName, List<DCTree> paramList) { this.name = paramName;
/* 813 */       this.content = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 818 */       return DocTree.Kind.UNKNOWN_INLINE_TAG;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 823 */       return paramDocTreeVisitor.visitUnknownInlineTag(this, paramD);
/*     */     }
/*     */ 
/*     */     public String getTagName()
/*     */     {
/* 828 */       return this.name.toString();
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getContent()
/*     */     {
/* 833 */       return this.content;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCValue extends DCTree.DCInlineTag implements ValueTree {
/*     */     public final DCTree.DCReference ref;
/*     */ 
/*     */     DCValue(DCTree.DCReference paramDCReference) {
/* 841 */       this.ref = paramDCReference;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 846 */       return DocTree.Kind.VALUE;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 851 */       return paramDocTreeVisitor.visitValue(this, paramD);
/*     */     }
/*     */ 
/*     */     public ReferenceTree getReference()
/*     */     {
/* 856 */       return this.ref;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DCVersion extends DCTree.DCBlockTag implements VersionTree {
/*     */     public final List<DCTree> body;
/*     */ 
/*     */     DCVersion(List<DCTree> paramList) {
/* 864 */       this.body = paramList;
/*     */     }
/*     */ 
/*     */     public DocTree.Kind getKind()
/*     */     {
/* 869 */       return DocTree.Kind.VERSION;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> paramDocTreeVisitor, D paramD)
/*     */     {
/* 874 */       return paramDocTreeVisitor.visitVersion(this, paramD);
/*     */     }
/*     */ 
/*     */     public List<? extends DocTree> getBody()
/*     */     {
/* 879 */       return this.body;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.DCTree
 * JD-Core Version:    0.6.2
 */