/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.internal.xsom.impl.WildcardImpl.Any;
/*     */ import com.sun.xml.internal.xsom.impl.WildcardImpl.Finite;
/*     */ import com.sun.xml.internal.xsom.impl.WildcardImpl.Other;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.HashSet;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class wildcardBody extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private Locator locator;
/*     */   private String modeValue;
/*     */   private String ns;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  60 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public wildcardBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator) {
/*  64 */     super(source, parent, cookie);
/*  65 */     this.$runtime = runtime;
/*  66 */     this.locator = _locator;
/*  67 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */ 
/*     */   public wildcardBody(NGCCRuntimeEx runtime, Locator _locator) {
/*  71 */     this(null, runtime, runtime, -1, _locator);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  76 */     this.$uri = $__uri;
/*  77 */     this.$localName = $__local;
/*  78 */     this.$qname = $__qname;
/*  79 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/*  82 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  83 */         this.$runtime.consumeAttribute($ai);
/*  84 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  87 */         this.$_ngcc_current_state = 0;
/*  88 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  91 */       break;
/*     */     case 5:
/*     */       int $ai;
/*  94 */       if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  95 */         this.$runtime.consumeAttribute($ai);
/*  96 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  99 */         this.$_ngcc_current_state = 1;
/* 100 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 103 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 106 */       if ((($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0)) {
/* 107 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 108 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 111 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 112 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 115 */       break;
/*     */     case 10:
/* 118 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 119 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 411, null, AnnotationContext.WILDCARD);
/* 120 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 123 */         this.$_ngcc_current_state = 9;
/* 124 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 127 */       break;
/*     */     case 0:
/* 130 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 132 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     default:
/* 135 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 143 */     this.$uri = $__uri;
/* 144 */     this.$localName = $__local;
/* 145 */     this.$qname = $__qname;
/* 146 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/* 149 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 150 */         this.$runtime.consumeAttribute($ai);
/* 151 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 154 */         this.$_ngcc_current_state = 0;
/* 155 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 158 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 161 */       if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 162 */         this.$runtime.consumeAttribute($ai);
/* 163 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 166 */         this.$_ngcc_current_state = 1;
/* 167 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 170 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 173 */       if ((($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0)) {
/* 174 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 175 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 178 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 179 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 182 */       break;
/*     */     case 10:
/* 185 */       this.$_ngcc_current_state = 9;
/* 186 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 188 */       break;
/*     */     case 0:
/* 191 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 193 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     default:
/* 196 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 204 */     this.$uri = $__uri;
/* 205 */     this.$localName = $__local;
/* 206 */     this.$qname = $__qname;
/* 207 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/* 210 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 211 */         this.$_ngcc_current_state = 3;
/*     */       }
/*     */       else {
/* 214 */         this.$_ngcc_current_state = 0;
/* 215 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 218 */       break;
/*     */     case 5:
/* 221 */       if (($__uri.equals("")) && ($__local.equals("processContents"))) {
/* 222 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 225 */         this.$_ngcc_current_state = 1;
/* 226 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 229 */       break;
/*     */     case 9:
/* 232 */       if ((($__uri.equals("")) && ($__local.equals("namespace"))) || (($__uri.equals("")) && ($__local.equals("processContents")))) {
/* 233 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 234 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 237 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 238 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 241 */       break;
/*     */     case 10:
/* 244 */       this.$_ngcc_current_state = 9;
/* 245 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 247 */       break;
/*     */     case 0:
/* 250 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 252 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     default:
/* 255 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 263 */     this.$uri = $__uri;
/* 264 */     this.$localName = $__local;
/* 265 */     this.$qname = $__qname;
/* 266 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/* 269 */       this.$_ngcc_current_state = 0;
/* 270 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 272 */       break;
/*     */     case 5:
/* 275 */       this.$_ngcc_current_state = 1;
/* 276 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 278 */       break;
/*     */     case 6:
/* 281 */       if (($__uri.equals("")) && ($__local.equals("processContents"))) {
/* 282 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 285 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 288 */       break;
/*     */     case 9:
/* 291 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 292 */       spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */ 
/* 294 */       break;
/*     */     case 10:
/* 297 */       this.$_ngcc_current_state = 9;
/* 298 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 300 */       break;
/*     */     case 0:
/* 303 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 305 */       break;
/*     */     case 2:
/* 308 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 309 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 312 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 315 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 7:
/*     */     case 8:
/*     */     default:
/* 318 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 326 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/* 329 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 330 */         this.$runtime.consumeAttribute($ai);
/* 331 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 334 */         this.$_ngcc_current_state = 0;
/* 335 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 338 */       break;
/*     */     case 3:
/* 341 */       this.ns = $value;
/* 342 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 344 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 347 */       if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 348 */         this.$runtime.consumeAttribute($ai);
/* 349 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 352 */         this.$_ngcc_current_state = 1;
/* 353 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 356 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 359 */       if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 360 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 361 */         spawnChildFromText(h, $value);
/*     */       }
/* 364 */       else if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 365 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 366 */         spawnChildFromText(h, $value);
/*     */       }
/*     */       else {
/* 369 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 409, null);
/* 370 */         spawnChildFromText(h, $value);
/*     */       }
/*     */ 
/* 374 */       break;
/*     */     case 10:
/* 377 */       this.$_ngcc_current_state = 9;
/* 378 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 380 */       break;
/*     */     case 0:
/* 383 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */ 
/* 385 */       break;
/*     */     case 7:
/* 388 */       this.modeValue = $value;
/* 389 */       this.$_ngcc_current_state = 6;
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     case 8:
/*     */     }
/*     */   }
/* 396 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 409:
/* 399 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 400 */       this.$_ngcc_current_state = 5;
/*     */ 
/* 402 */       break;
/*     */     case 411:
/* 405 */       this.annotation = ((AnnotationImpl)$__result__);
/* 406 */       this.$_ngcc_current_state = 9;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 413 */     return (this.$_ngcc_current_state == 0) || (this.$_ngcc_current_state == 5) || (this.$_ngcc_current_state == 1);
/*     */   }
/*     */ 
/*     */   private WildcardImpl makeResult()
/*     */   {
/* 418 */     if (this.modeValue == null) this.modeValue = "strict";
/*     */ 
/* 420 */     int mode = -1;
/* 421 */     if (this.modeValue.equals("strict")) mode = 2;
/* 422 */     if (this.modeValue.equals("lax")) mode = 1;
/* 423 */     if (this.modeValue.equals("skip")) mode = 3;
/* 424 */     if (mode == -1) throw new InternalError();
/*     */ 
/* 426 */     if ((this.ns == null) || (this.ns.equals("##any"))) {
/* 427 */       return new WildcardImpl.Any(this.$runtime.document, this.annotation, this.locator, this.fa, mode);
/*     */     }
/* 429 */     if (this.ns.equals("##other"))
/*     */     {
/* 432 */       return new WildcardImpl.Other(this.$runtime.document, this.annotation, this.locator, this.fa, this.$runtime.currentSchema
/* 432 */         .getTargetNamespace(), mode);
/*     */     }
/* 434 */     StringTokenizer tokens = new StringTokenizer(this.ns);
/* 435 */     HashSet s = new HashSet();
/* 436 */     while (tokens.hasMoreTokens()) {
/* 437 */       String ns = tokens.nextToken();
/* 438 */       if (ns.equals("##local")) ns = "";
/* 439 */       if (ns.equals("##targetNamespace")) ns = this.$runtime.currentSchema.getTargetNamespace();
/* 440 */       s.add(ns);
/*     */     }
/*     */ 
/* 443 */     return new WildcardImpl.Finite(this.$runtime.document, this.annotation, this.locator, this.fa, s, mode);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.wildcardBody
 * JD-Core Version:    0.6.2
 */