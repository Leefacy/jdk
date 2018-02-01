/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree.ValueKind;
/*     */ import com.sun.source.doctree.DocTree.Kind;
/*     */ import com.sun.tools.javac.parser.Tokens.Comment;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.DiagnosticSource;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ public class DocTreeMaker
/*     */ {
/*  51 */   protected static final Context.Key<DocTreeMaker> treeMakerKey = new Context.Key();
/*     */ 
/*  64 */   public int pos = -1;
/*     */   private final JCDiagnostic.Factory diags;
/*     */ 
/*     */   public static DocTreeMaker instance(Context paramContext)
/*     */   {
/*  56 */     DocTreeMaker localDocTreeMaker = (DocTreeMaker)paramContext.get(treeMakerKey);
/*  57 */     if (localDocTreeMaker == null)
/*  58 */       localDocTreeMaker = new DocTreeMaker(paramContext);
/*  59 */     return localDocTreeMaker;
/*     */   }
/*     */ 
/*     */   protected DocTreeMaker(Context paramContext)
/*     */   {
/*  72 */     paramContext.put(treeMakerKey, this);
/*  73 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  74 */     this.pos = -1;
/*     */   }
/*     */ 
/*     */   public DocTreeMaker at(int paramInt)
/*     */   {
/*  80 */     this.pos = paramInt;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   public DocTreeMaker at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*     */   {
/*  87 */     this.pos = (paramDiagnosticPosition == null ? -1 : paramDiagnosticPosition.getStartPosition());
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   public DCTree.DCAttribute Attribute(Name paramName, AttributeTree.ValueKind paramValueKind, List<DCTree> paramList) {
/*  92 */     DCTree.DCAttribute localDCAttribute = new DCTree.DCAttribute(paramName, paramValueKind, paramList);
/*  93 */     localDCAttribute.pos = this.pos;
/*  94 */     return localDCAttribute;
/*     */   }
/*     */ 
/*     */   public DCTree.DCAuthor Author(List<DCTree> paramList) {
/*  98 */     DCTree.DCAuthor localDCAuthor = new DCTree.DCAuthor(paramList);
/*  99 */     localDCAuthor.pos = this.pos;
/* 100 */     return localDCAuthor;
/*     */   }
/*     */ 
/*     */   public DCTree.DCLiteral Code(DCTree.DCText paramDCText) {
/* 104 */     DCTree.DCLiteral localDCLiteral = new DCTree.DCLiteral(DocTree.Kind.CODE, paramDCText);
/* 105 */     localDCLiteral.pos = this.pos;
/* 106 */     return localDCLiteral;
/*     */   }
/*     */ 
/*     */   public DCTree.DCComment Comment(String paramString) {
/* 110 */     DCTree.DCComment localDCComment = new DCTree.DCComment(paramString);
/* 111 */     localDCComment.pos = this.pos;
/* 112 */     return localDCComment;
/*     */   }
/*     */ 
/*     */   public DCTree.DCDeprecated Deprecated(List<DCTree> paramList) {
/* 116 */     DCTree.DCDeprecated localDCDeprecated = new DCTree.DCDeprecated(paramList);
/* 117 */     localDCDeprecated.pos = this.pos;
/* 118 */     return localDCDeprecated;
/*     */   }
/*     */ 
/*     */   public DCTree.DCDocComment DocComment(Tokens.Comment paramComment, List<DCTree> paramList1, List<DCTree> paramList2, List<DCTree> paramList3) {
/* 122 */     DCTree.DCDocComment localDCDocComment = new DCTree.DCDocComment(paramComment, paramList1, paramList2, paramList3);
/* 123 */     localDCDocComment.pos = this.pos;
/* 124 */     return localDCDocComment;
/*     */   }
/*     */ 
/*     */   public DCTree.DCDocRoot DocRoot() {
/* 128 */     DCTree.DCDocRoot localDCDocRoot = new DCTree.DCDocRoot();
/* 129 */     localDCDocRoot.pos = this.pos;
/* 130 */     return localDCDocRoot;
/*     */   }
/*     */ 
/*     */   public DCTree.DCEndElement EndElement(Name paramName) {
/* 134 */     DCTree.DCEndElement localDCEndElement = new DCTree.DCEndElement(paramName);
/* 135 */     localDCEndElement.pos = this.pos;
/* 136 */     return localDCEndElement;
/*     */   }
/*     */ 
/*     */   public DCTree.DCEntity Entity(Name paramName) {
/* 140 */     DCTree.DCEntity localDCEntity = new DCTree.DCEntity(paramName);
/* 141 */     localDCEntity.pos = this.pos;
/* 142 */     return localDCEntity;
/*     */   }
/*     */ 
/*     */   public DCTree.DCErroneous Erroneous(String paramString1, DiagnosticSource paramDiagnosticSource, String paramString2, Object[] paramArrayOfObject) {
/* 146 */     DCTree.DCErroneous localDCErroneous = new DCTree.DCErroneous(paramString1, this.diags, paramDiagnosticSource, paramString2, paramArrayOfObject);
/* 147 */     localDCErroneous.pos = this.pos;
/* 148 */     return localDCErroneous;
/*     */   }
/*     */ 
/*     */   public DCTree.DCThrows Exception(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 152 */     DCTree.DCThrows localDCThrows = new DCTree.DCThrows(DocTree.Kind.EXCEPTION, paramDCReference, paramList);
/* 153 */     localDCThrows.pos = this.pos;
/* 154 */     return localDCThrows;
/*     */   }
/*     */ 
/*     */   public DCTree.DCIdentifier Identifier(Name paramName) {
/* 158 */     DCTree.DCIdentifier localDCIdentifier = new DCTree.DCIdentifier(paramName);
/* 159 */     localDCIdentifier.pos = this.pos;
/* 160 */     return localDCIdentifier;
/*     */   }
/*     */ 
/*     */   public DCTree.DCInheritDoc InheritDoc() {
/* 164 */     DCTree.DCInheritDoc localDCInheritDoc = new DCTree.DCInheritDoc();
/* 165 */     localDCInheritDoc.pos = this.pos;
/* 166 */     return localDCInheritDoc;
/*     */   }
/*     */ 
/*     */   public DCTree.DCLink Link(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 170 */     DCTree.DCLink localDCLink = new DCTree.DCLink(DocTree.Kind.LINK, paramDCReference, paramList);
/* 171 */     localDCLink.pos = this.pos;
/* 172 */     return localDCLink;
/*     */   }
/*     */ 
/*     */   public DCTree.DCLink LinkPlain(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 176 */     DCTree.DCLink localDCLink = new DCTree.DCLink(DocTree.Kind.LINK_PLAIN, paramDCReference, paramList);
/* 177 */     localDCLink.pos = this.pos;
/* 178 */     return localDCLink;
/*     */   }
/*     */ 
/*     */   public DCTree.DCLiteral Literal(DCTree.DCText paramDCText) {
/* 182 */     DCTree.DCLiteral localDCLiteral = new DCTree.DCLiteral(DocTree.Kind.LITERAL, paramDCText);
/* 183 */     localDCLiteral.pos = this.pos;
/* 184 */     return localDCLiteral;
/*     */   }
/*     */ 
/*     */   public DCTree.DCParam Param(boolean paramBoolean, DCTree.DCIdentifier paramDCIdentifier, List<DCTree> paramList) {
/* 188 */     DCTree.DCParam localDCParam = new DCTree.DCParam(paramBoolean, paramDCIdentifier, paramList);
/* 189 */     localDCParam.pos = this.pos;
/* 190 */     return localDCParam;
/*     */   }
/*     */ 
/*     */   public DCTree.DCReference Reference(String paramString, JCTree paramJCTree, Name paramName, List<JCTree> paramList)
/*     */   {
/* 195 */     DCTree.DCReference localDCReference = new DCTree.DCReference(paramString, paramJCTree, paramName, paramList);
/* 196 */     localDCReference.pos = this.pos;
/* 197 */     return localDCReference;
/*     */   }
/*     */ 
/*     */   public DCTree.DCReturn Return(List<DCTree> paramList) {
/* 201 */     DCTree.DCReturn localDCReturn = new DCTree.DCReturn(paramList);
/* 202 */     localDCReturn.pos = this.pos;
/* 203 */     return localDCReturn;
/*     */   }
/*     */ 
/*     */   public DCTree.DCSee See(List<DCTree> paramList) {
/* 207 */     DCTree.DCSee localDCSee = new DCTree.DCSee(paramList);
/* 208 */     localDCSee.pos = this.pos;
/* 209 */     return localDCSee;
/*     */   }
/*     */ 
/*     */   public DCTree.DCSerial Serial(List<DCTree> paramList) {
/* 213 */     DCTree.DCSerial localDCSerial = new DCTree.DCSerial(paramList);
/* 214 */     localDCSerial.pos = this.pos;
/* 215 */     return localDCSerial;
/*     */   }
/*     */ 
/*     */   public DCTree.DCSerialData SerialData(List<DCTree> paramList) {
/* 219 */     DCTree.DCSerialData localDCSerialData = new DCTree.DCSerialData(paramList);
/* 220 */     localDCSerialData.pos = this.pos;
/* 221 */     return localDCSerialData;
/*     */   }
/*     */ 
/*     */   public DCTree.DCSerialField SerialField(DCTree.DCIdentifier paramDCIdentifier, DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 225 */     DCTree.DCSerialField localDCSerialField = new DCTree.DCSerialField(paramDCIdentifier, paramDCReference, paramList);
/* 226 */     localDCSerialField.pos = this.pos;
/* 227 */     return localDCSerialField;
/*     */   }
/*     */ 
/*     */   public DCTree.DCSince Since(List<DCTree> paramList) {
/* 231 */     DCTree.DCSince localDCSince = new DCTree.DCSince(paramList);
/* 232 */     localDCSince.pos = this.pos;
/* 233 */     return localDCSince;
/*     */   }
/*     */ 
/*     */   public DCTree.DCStartElement StartElement(Name paramName, List<DCTree> paramList, boolean paramBoolean) {
/* 237 */     DCTree.DCStartElement localDCStartElement = new DCTree.DCStartElement(paramName, paramList, paramBoolean);
/* 238 */     localDCStartElement.pos = this.pos;
/* 239 */     return localDCStartElement;
/*     */   }
/*     */ 
/*     */   public DCTree.DCText Text(String paramString) {
/* 243 */     DCTree.DCText localDCText = new DCTree.DCText(paramString);
/* 244 */     localDCText.pos = this.pos;
/* 245 */     return localDCText;
/*     */   }
/*     */ 
/*     */   public DCTree.DCThrows Throws(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 249 */     DCTree.DCThrows localDCThrows = new DCTree.DCThrows(DocTree.Kind.THROWS, paramDCReference, paramList);
/* 250 */     localDCThrows.pos = this.pos;
/* 251 */     return localDCThrows;
/*     */   }
/*     */ 
/*     */   public DCTree.DCUnknownBlockTag UnknownBlockTag(Name paramName, List<DCTree> paramList) {
/* 255 */     DCTree.DCUnknownBlockTag localDCUnknownBlockTag = new DCTree.DCUnknownBlockTag(paramName, paramList);
/* 256 */     localDCUnknownBlockTag.pos = this.pos;
/* 257 */     return localDCUnknownBlockTag;
/*     */   }
/*     */ 
/*     */   public DCTree.DCUnknownInlineTag UnknownInlineTag(Name paramName, List<DCTree> paramList) {
/* 261 */     DCTree.DCUnknownInlineTag localDCUnknownInlineTag = new DCTree.DCUnknownInlineTag(paramName, paramList);
/* 262 */     localDCUnknownInlineTag.pos = this.pos;
/* 263 */     return localDCUnknownInlineTag;
/*     */   }
/*     */ 
/*     */   public DCTree.DCValue Value(DCTree.DCReference paramDCReference) {
/* 267 */     DCTree.DCValue localDCValue = new DCTree.DCValue(paramDCReference);
/* 268 */     localDCValue.pos = this.pos;
/* 269 */     return localDCValue;
/*     */   }
/*     */ 
/*     */   public DCTree.DCVersion Version(List<DCTree> paramList) {
/* 273 */     DCTree.DCVersion localDCVersion = new DCTree.DCVersion(paramList);
/* 274 */     localDCVersion.pos = this.pos;
/* 275 */     return localDCVersion;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.DocTreeMaker
 * JD-Core Version:    0.6.2
 */