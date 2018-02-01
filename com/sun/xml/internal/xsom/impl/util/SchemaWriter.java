/*     */ package com.sun.xml.internal.xsom.impl.util;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Any;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Other;
/*     */ import com.sun.xml.internal.xsom.XSWildcard.Union;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.math.BigInteger;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class SchemaWriter
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   private final Writer out;
/*     */   private int indent;
/* 107 */   private boolean hadError = false;
/*     */ 
/* 573 */   private static final XSWildcardFunction<String> WILDCARD_NS = new XSWildcardFunction() {
/*     */     public String any(XSWildcard.Any wc) {
/* 575 */       return "";
/*     */     }
/*     */ 
/*     */     public String other(XSWildcard.Other wc) {
/* 579 */       return " namespace='##other'";
/*     */     }
/*     */ 
/*     */     public String union(XSWildcard.Union wc) {
/* 583 */       StringBuilder buf = new StringBuilder(" namespace='");
/* 584 */       boolean first = true;
/* 585 */       for (String s : wc.getNamespaces()) {
/* 586 */         if (first) first = false; else
/* 587 */           buf.append(' ');
/* 588 */         buf.append(s);
/*     */       }
/* 590 */       return '\'';
/*     */     }
/* 573 */   };
/*     */ 
/*     */   public SchemaWriter(Writer _out)
/*     */   {
/*  83 */     this.out = _out;
/*     */   }
/*     */ 
/*     */   private void println(String s)
/*     */   {
/*     */     try
/*     */     {
/*  94 */       for (int i = 0; i < this.indent; i++) this.out.write("  ");
/*  95 */       this.out.write(s);
/*  96 */       this.out.write(10);
/*     */ 
/*  98 */       this.out.flush();
/*     */     }
/*     */     catch (IOException e) {
/* 101 */       this.hadError = true;
/*     */     }
/*     */   }
/* 104 */   private void println() { println(""); }
/*     */ 
/*     */ 
/*     */   public boolean checkError()
/*     */   {
/*     */     try
/*     */     {
/* 112 */       this.out.flush();
/*     */     } catch (IOException e) {
/* 114 */       this.hadError = true;
/*     */     }
/* 116 */     return this.hadError;
/*     */   }
/*     */ 
/*     */   public void visit(XSSchemaSet s) {
/* 120 */     Iterator itr = s.iterateSchema();
/* 121 */     while (itr.hasNext()) {
/* 122 */       schema((XSSchema)itr.next());
/* 123 */       println();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void schema(XSSchema s)
/*     */   {
/* 130 */     if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/* 131 */       return;
/*     */     }
/* 133 */     println(MessageFormat.format("<schema targetNamespace=\"{0}\">", new Object[] { s.getTargetNamespace() }));
/* 134 */     this.indent += 1;
/*     */ 
/* 138 */     Iterator itr = s.iterateAttGroupDecls();
/* 139 */     while (itr.hasNext()) {
/* 140 */       attGroupDecl((XSAttGroupDecl)itr.next());
/*     */     }
/* 142 */     itr = s.iterateAttributeDecls();
/* 143 */     while (itr.hasNext()) {
/* 144 */       attributeDecl((XSAttributeDecl)itr.next());
/*     */     }
/* 146 */     itr = s.iterateComplexTypes();
/* 147 */     while (itr.hasNext()) {
/* 148 */       complexType((XSComplexType)itr.next());
/*     */     }
/* 150 */     itr = s.iterateElementDecls();
/* 151 */     while (itr.hasNext()) {
/* 152 */       elementDecl((XSElementDecl)itr.next());
/*     */     }
/* 154 */     itr = s.iterateModelGroupDecls();
/* 155 */     while (itr.hasNext()) {
/* 156 */       modelGroupDecl((XSModelGroupDecl)itr.next());
/*     */     }
/* 158 */     itr = s.iterateSimpleTypes();
/* 159 */     while (itr.hasNext()) {
/* 160 */       simpleType((XSSimpleType)itr.next());
/*     */     }
/* 162 */     this.indent -= 1;
/* 163 */     println("</schema>");
/*     */   }
/*     */ 
/*     */   public void attGroupDecl(XSAttGroupDecl decl)
/*     */   {
/* 169 */     println(MessageFormat.format("<attGroup name=\"{0}\">", new Object[] { decl.getName() }));
/* 170 */     this.indent += 1;
/*     */ 
/* 174 */     Iterator itr = decl.iterateAttGroups();
/* 175 */     while (itr.hasNext()) {
/* 176 */       dumpRef((XSAttGroupDecl)itr.next());
/*     */     }
/* 178 */     itr = decl.iterateDeclaredAttributeUses();
/* 179 */     while (itr.hasNext()) {
/* 180 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/* 182 */     this.indent -= 1;
/* 183 */     println("</attGroup>");
/*     */   }
/*     */ 
/*     */   public void dumpRef(XSAttGroupDecl decl) {
/* 187 */     println(MessageFormat.format("<attGroup ref=\"'{'{0}'}'{1}\"/>", new Object[] { decl.getTargetNamespace(), decl.getName() }));
/*     */   }
/*     */ 
/*     */   public void attributeUse(XSAttributeUse use) {
/* 191 */     XSAttributeDecl decl = use.getDecl();
/*     */ 
/* 193 */     String additionalAtts = "";
/*     */ 
/* 195 */     if (use.isRequired())
/* 196 */       additionalAtts = additionalAtts + " use=\"required\"";
/* 197 */     if ((use.getFixedValue() != null) && (use.getDecl().getFixedValue() == null))
/* 198 */       additionalAtts = additionalAtts + " fixed=\"" + use.getFixedValue() + '"';
/* 199 */     if ((use.getDefaultValue() != null) && (use.getDecl().getDefaultValue() == null)) {
/* 200 */       additionalAtts = additionalAtts + " default=\"" + use.getDefaultValue() + '"';
/*     */     }
/* 202 */     if (decl.isLocal())
/*     */     {
/* 204 */       dump(decl, additionalAtts);
/*     */     }
/*     */     else
/* 207 */       println(MessageFormat.format("<attribute ref=\"'{'{0}'}'{1}{2}\"/>", new Object[] { decl
/* 208 */         .getTargetNamespace(), decl.getName(), additionalAtts }));
/*     */   }
/*     */ 
/*     */   public void attributeDecl(XSAttributeDecl decl)
/*     */   {
/* 213 */     dump(decl, "");
/*     */   }
/*     */ 
/*     */   private void dump(XSAttributeDecl decl, String additionalAtts) {
/* 217 */     XSSimpleType type = decl.getType();
/*     */ 
/* 219 */     println(MessageFormat.format("<attribute name=\"{0}\"{1}{2}{3}{4}{5}>", new Object[] { decl
/* 220 */       .getName(), additionalAtts, type
/* 222 */       .isLocal() ? "" : 
/* 223 */       MessageFormat.format(" type=\"'{'{0}'}'{1}\"", new Object[] { type
/* 223 */       .getTargetNamespace(), type.getName() }), 
/* 224 */       " fixed=\"" + decl
/* 225 */       .getFixedValue() + '"', 
/* 226 */       " default=\"" + decl
/* 227 */       .getDefaultValue() + '"', type
/* 228 */       .isLocal() ? "" : " /" }));
/*     */ 
/* 230 */     if (type.isLocal()) {
/* 231 */       this.indent += 1;
/* 232 */       simpleType(type);
/* 233 */       this.indent -= 1;
/* 234 */       println("</attribute>");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void simpleType(XSSimpleType type) {
/* 239 */     println(MessageFormat.format("<simpleType{0}>", new Object[] { " name=\"" + type.getName() + '"' }));
/* 240 */     this.indent += 1;
/*     */ 
/* 242 */     type.visit(this);
/*     */ 
/* 244 */     this.indent -= 1;
/* 245 */     println("</simpleType>");
/*     */   }
/*     */ 
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 249 */     XSSimpleType itemType = type.getItemType();
/*     */ 
/* 251 */     if (itemType.isLocal()) {
/* 252 */       println("<list>");
/* 253 */       this.indent += 1;
/* 254 */       simpleType(itemType);
/* 255 */       this.indent -= 1;
/* 256 */       println("</list>");
/*     */     }
/*     */     else {
/* 259 */       println(MessageFormat.format("<list itemType=\"'{'{0}'}'{1}\" />", new Object[] { itemType
/* 260 */         .getTargetNamespace(), itemType.getName() }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 265 */     int len = type.getMemberSize();
/* 266 */     StringBuffer ref = new StringBuffer();
/*     */ 
/* 268 */     for (int i = 0; i < len; i++) {
/* 269 */       XSSimpleType member = type.getMember(i);
/* 270 */       if (member.isGlobal()) {
/* 271 */         ref.append(MessageFormat.format(" '{'{0}'}'{1}", new Object[] { member.getTargetNamespace(), member.getName() }));
/*     */       }
/*     */     }
/* 274 */     if (ref.length() == 0)
/* 275 */       println("<union>");
/*     */     else
/* 277 */       println("<union memberTypes=\"" + ref + "\">");
/* 278 */     this.indent += 1;
/*     */ 
/* 280 */     for (int i = 0; i < len; i++) {
/* 281 */       XSSimpleType member = type.getMember(i);
/* 282 */       if (member.isLocal())
/* 283 */         simpleType(member);
/*     */     }
/* 285 */     this.indent -= 1;
/* 286 */     println("</union>");
/*     */   }
/*     */ 
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type)
/*     */   {
/* 291 */     if (type.getBaseType() == null)
/*     */     {
/* 293 */       if (!type.getName().equals("anySimpleType"))
/* 294 */         throw new InternalError();
/* 295 */       if (!"http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace()))
/* 296 */         throw new InternalError();
/* 297 */       return;
/*     */     }
/*     */ 
/* 300 */     XSSimpleType baseType = type.getSimpleBaseType();
/*     */ 
/* 302 */     println(MessageFormat.format("<restriction{0}>", new Object[] { 
/* 303 */       " base=\"{" + baseType
/* 304 */       .getTargetNamespace() + '}' + baseType
/* 305 */       .getName() + '"' }));
/* 306 */     this.indent += 1;
/*     */ 
/* 308 */     if (baseType.isLocal()) {
/* 309 */       simpleType(baseType);
/*     */     }
/* 311 */     Iterator itr = type.iterateDeclaredFacets();
/* 312 */     while (itr.hasNext()) {
/* 313 */       facet((XSFacet)itr.next());
/*     */     }
/* 315 */     this.indent -= 1;
/* 316 */     println("</restriction>");
/*     */   }
/*     */ 
/*     */   public void facet(XSFacet facet) {
/* 320 */     println(MessageFormat.format("<{0} value=\"{1}\"/>", new Object[] { facet
/* 321 */       .getName(), facet.getValue() }));
/*     */   }
/*     */ 
/*     */   public void notation(XSNotation notation) {
/* 325 */     println(MessageFormat.format("<notation name='\"0}\" public =\"{1}\" system=\"{2}\" />", new Object[] { notation
/* 326 */       .getName(), notation.getPublicId(), notation.getSystemId() }));
/*     */   }
/*     */ 
/*     */   public void complexType(XSComplexType type)
/*     */   {
/* 332 */     println(MessageFormat.format("<complexType{0}>", new Object[] { 
/* 333 */       " name=\"" + type.getName() + '"' }));
/* 334 */     this.indent += 1;
/*     */ 
/* 338 */     if (type.getContentType().asSimpleType() != null)
/*     */     {
/* 340 */       println("<simpleContent>");
/* 341 */       this.indent += 1;
/*     */ 
/* 343 */       XSType baseType = type.getBaseType();
/*     */ 
/* 345 */       if (type.getDerivationMethod() == 2)
/*     */       {
/* 347 */         println(MessageFormat.format("<restriction base=\"<{0}>{1}\">", new Object[] { baseType
/* 348 */           .getTargetNamespace(), baseType.getName() }));
/* 349 */         this.indent += 1;
/*     */ 
/* 351 */         dumpComplexTypeAttribute(type);
/*     */ 
/* 353 */         this.indent -= 1;
/* 354 */         println("</restriction>");
/*     */       }
/*     */       else {
/* 357 */         println(MessageFormat.format("<extension base=\"<{0}>{1}\">", new Object[] { baseType
/* 358 */           .getTargetNamespace(), baseType.getName() }));
/*     */ 
/* 361 */         if ((type.isGlobal()) && 
/* 362 */           (type
/* 362 */           .getTargetNamespace().equals(baseType.getTargetNamespace())) && 
/* 363 */           (type
/* 363 */           .getName().equals(baseType.getName()))) {
/* 364 */           this.indent += 1;
/* 365 */           println("<redefine>");
/* 366 */           this.indent += 1;
/* 367 */           baseType.visit(this);
/* 368 */           this.indent -= 1;
/* 369 */           println("</redefine>");
/* 370 */           this.indent -= 1;
/*     */         }
/*     */ 
/* 373 */         this.indent += 1;
/*     */ 
/* 375 */         dumpComplexTypeAttribute(type);
/*     */ 
/* 377 */         this.indent -= 1;
/* 378 */         println("</extension>");
/*     */       }
/*     */ 
/* 381 */       this.indent -= 1;
/* 382 */       println("</simpleContent>");
/*     */     }
/*     */     else {
/* 385 */       println("<complexContent>");
/* 386 */       this.indent += 1;
/*     */ 
/* 388 */       XSComplexType baseType = type.getBaseType().asComplexType();
/*     */ 
/* 390 */       if (type.getDerivationMethod() == 2)
/*     */       {
/* 392 */         println(MessageFormat.format("<restriction base=\"'{'{0}'}'{1}\">", new Object[] { baseType
/* 393 */           .getTargetNamespace(), baseType.getName() }));
/* 394 */         this.indent += 1;
/*     */ 
/* 396 */         type.getContentType().visit(this);
/* 397 */         dumpComplexTypeAttribute(type);
/*     */ 
/* 399 */         this.indent -= 1;
/* 400 */         println("</restriction>");
/*     */       }
/*     */       else {
/* 403 */         println(MessageFormat.format("<extension base=\"'{'{0}'}'{1}\">", new Object[] { baseType
/* 404 */           .getTargetNamespace(), baseType.getName() }));
/*     */ 
/* 407 */         if ((type.isGlobal()) && 
/* 408 */           (type
/* 408 */           .getTargetNamespace().equals(baseType.getTargetNamespace())) && 
/* 409 */           (type
/* 409 */           .getName().equals(baseType.getName()))) {
/* 410 */           this.indent += 1;
/* 411 */           println("<redefine>");
/* 412 */           this.indent += 1;
/* 413 */           baseType.visit(this);
/* 414 */           this.indent -= 1;
/* 415 */           println("</redefine>");
/* 416 */           this.indent -= 1;
/*     */         }
/*     */ 
/* 419 */         this.indent += 1;
/*     */ 
/* 421 */         type.getExplicitContent().visit(this);
/* 422 */         dumpComplexTypeAttribute(type);
/*     */ 
/* 424 */         this.indent -= 1;
/* 425 */         println("</extension>");
/*     */       }
/*     */ 
/* 428 */       this.indent -= 1;
/* 429 */       println("</complexContent>");
/*     */     }
/*     */ 
/* 432 */     this.indent -= 1;
/* 433 */     println("</complexType>");
/*     */   }
/*     */ 
/*     */   private void dumpComplexTypeAttribute(XSComplexType type)
/*     */   {
/* 439 */     Iterator itr = type.iterateAttGroups();
/* 440 */     while (itr.hasNext()) {
/* 441 */       dumpRef((XSAttGroupDecl)itr.next());
/*     */     }
/* 443 */     itr = type.iterateDeclaredAttributeUses();
/* 444 */     while (itr.hasNext()) {
/* 445 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/* 447 */     XSWildcard awc = type.getAttributeWildcard();
/* 448 */     if (awc != null)
/* 449 */       wildcard("anyAttribute", awc, "");
/*     */   }
/*     */ 
/*     */   public void elementDecl(XSElementDecl decl) {
/* 453 */     elementDecl(decl, "");
/*     */   }
/*     */   private void elementDecl(XSElementDecl decl, String extraAtts) {
/* 456 */     XSType type = decl.getType();
/*     */ 
/* 461 */     if (decl.getForm() != null) {
/* 462 */       extraAtts = extraAtts + " form=\"" + (decl.getForm().booleanValue() ? "qualified" : "unqualified") + "\"";
/*     */     }
/*     */ 
/* 465 */     println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>", new Object[] { decl
/* 466 */       .getName(), 
/* 467 */       " type=\"{" + type
/* 468 */       .getTargetNamespace() + '}' + type
/* 469 */       .getName() + '"', extraAtts, type
/* 471 */       .isLocal() ? "" : "/" }));
/*     */ 
/* 473 */     if (type.isLocal()) {
/* 474 */       this.indent += 1;
/*     */ 
/* 476 */       if (type.isLocal()) type.visit(this);
/*     */ 
/* 478 */       this.indent -= 1;
/* 479 */       println("</element>");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 484 */     println(MessageFormat.format("<group name=\"{0}\">", new Object[] { decl.getName() }));
/* 485 */     this.indent += 1;
/*     */ 
/* 487 */     modelGroup(decl.getModelGroup());
/*     */ 
/* 489 */     this.indent -= 1;
/* 490 */     println("</group>");
/*     */   }
/*     */ 
/*     */   public void modelGroup(XSModelGroup group) {
/* 494 */     modelGroup(group, "");
/*     */   }
/*     */   private void modelGroup(XSModelGroup group, String extraAtts) {
/* 497 */     println(MessageFormat.format("<{0}{1}>", new Object[] { group.getCompositor(), extraAtts }));
/* 498 */     this.indent += 1;
/*     */ 
/* 500 */     int len = group.getSize();
/* 501 */     for (int i = 0; i < len; i++) {
/* 502 */       particle(group.getChild(i));
/*     */     }
/* 504 */     this.indent -= 1;
/* 505 */     println(MessageFormat.format("</{0}>", new Object[] { group.getCompositor() }));
/*     */   }
/*     */ 
/*     */   public void particle(XSParticle part)
/*     */   {
/* 511 */     StringBuilder buf = new StringBuilder();
/*     */ 
/* 513 */     BigInteger i = part.getMaxOccurs();
/* 514 */     if (i.equals(BigInteger.valueOf(-1L)))
/* 515 */       buf.append(" maxOccurs=\"unbounded\"");
/* 516 */     else if (!i.equals(BigInteger.ONE)) {
/* 517 */       buf.append(" maxOccurs=\"").append(i).append('"');
/*     */     }
/* 519 */     i = part.getMinOccurs();
/* 520 */     if (!i.equals(BigInteger.ONE)) {
/* 521 */       buf.append(" minOccurs=\"").append(i).append('"');
/*     */     }
/* 523 */     final String extraAtts = buf.toString();
/*     */ 
/* 525 */     part.getTerm().visit(new XSTermVisitor() {
/*     */       public void elementDecl(XSElementDecl decl) {
/* 527 */         if (decl.isLocal()) {
/* 528 */           SchemaWriter.this.elementDecl(decl, extraAtts);
/*     */         }
/*     */         else
/* 531 */           SchemaWriter.this.println(MessageFormat.format("<element ref=\"'{'{0}'}'{1}\"{2}/>", new Object[] { decl
/* 532 */             .getTargetNamespace(), decl
/* 533 */             .getName(), extraAtts }));
/*     */       }
/*     */ 
/*     */       public void modelGroupDecl(XSModelGroupDecl decl)
/*     */       {
/* 539 */         SchemaWriter.this.println(MessageFormat.format("<group ref=\"'{'{0}'}'{1}\"{2}/>", new Object[] { decl
/* 540 */           .getTargetNamespace(), decl
/* 541 */           .getName(), extraAtts }));
/*     */       }
/*     */ 
/*     */       public void modelGroup(XSModelGroup group) {
/* 545 */         SchemaWriter.this.modelGroup(group, extraAtts);
/*     */       }
/*     */       public void wildcard(XSWildcard wc) {
/* 548 */         SchemaWriter.this.wildcard("any", wc, extraAtts);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void wildcard(XSWildcard wc) {
/* 554 */     wildcard("any", wc, "");
/*     */   }
/*     */ 
/*     */   private void wildcard(String tagName, XSWildcard wc, String extraAtts)
/*     */   {
/*     */     String proessContents;
/*     */     String proessContents;
/*     */     String proessContents;
/* 559 */     switch (wc.getMode()) {
/*     */     case 1:
/* 561 */       proessContents = " processContents='lax'"; break;
/*     */     case 2:
/* 563 */       proessContents = ""; break;
/*     */     case 3:
/* 565 */       proessContents = " processContents='skip'"; break;
/*     */     default:
/* 567 */       throw new AssertionError();
/*     */     }
/*     */     String proessContents;
/* 570 */     println(MessageFormat.format("<{0}{1}{2}{3}/>", new Object[] { tagName, proessContents, wc.apply(WILDCARD_NS), extraAtts }));
/*     */   }
/*     */ 
/*     */   public void annotation(XSAnnotation ann)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void identityConstraint(XSIdentityConstraint decl)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void xpath(XSXPath xp)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void empty(XSContentType t)
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.util.SchemaWriter
 * JD-Core Version:    0.6.2
 */