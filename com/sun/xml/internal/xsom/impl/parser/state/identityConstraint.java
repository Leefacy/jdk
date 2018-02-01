/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.IdentityConstraintImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.XPathImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.IdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class identityConstraint extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private UName ref;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl ann;
/*     */   private XPathImpl field;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private short category;
/* 585 */   private List fields = new ArrayList();
/*     */   private XPathImpl selector;
/* 587 */   private DelayedRef.IdentityConstraint refer = null;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  58 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public identityConstraint(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  62 */     super(source, parent, cookie);
/*  63 */     this.$runtime = runtime;
/*  64 */     this.$_ngcc_current_state = 18;
/*     */   }
/*     */ 
/*     */   public identityConstraint(NGCCRuntimeEx runtime) {
/*  68 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  72 */     this.fields.add(this.field);
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException
/*     */   {
/*  77 */     this.refer = new DelayedRef.IdentityConstraint(this.$runtime, this.$runtime
/*  78 */       .copyLocator(), this.$runtime.currentSchema, this.ref);
/*     */   }
/*     */ 
/*     */   private void action2() throws SAXException
/*     */   {
/*  83 */     if (this.$localName.equals("key")) {
/*  84 */       this.category = 0;
/*     */     }
/*  86 */     else if (this.$localName.equals("keyref")) {
/*  87 */       this.category = 1;
/*     */     }
/*  89 */     else if (this.$localName.equals("unique"))
/*  90 */       this.category = 2;
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  95 */     this.$uri = $__uri;
/*  96 */     this.$localName = $__local;
/*  97 */     this.$qname = $__qname;
/*  98 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 16:
/*     */       int $ai;
/* 101 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 102 */         this.$runtime.consumeAttribute($ai);
/* 103 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 106 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 109 */       break;
/*     */     case 1:
/* 112 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("field"))) {
/* 113 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 114 */         this.$_ngcc_current_state = 3;
/*     */       }
/*     */       else {
/* 117 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 120 */       break;
/*     */     case 0:
/* 123 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 125 */       break;
/*     */     case 17:
/*     */       int $ai;
/* 128 */       if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("selector"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) {
/* 129 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 287, null);
/* 130 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 133 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 136 */       break;
/*     */     case 7:
/* 139 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("selector"))) {
/* 140 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 141 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 144 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 147 */       break;
/*     */     case 18:
/* 150 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique")))) {
/* 151 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 152 */         action2();
/* 153 */         this.$_ngcc_current_state = 17;
/*     */       }
/*     */       else {
/* 156 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 159 */       break;
/*     */     case 3:
/*     */       int $ai;
/* 162 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 163 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 270);
/* 164 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 167 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 170 */       break;
/*     */     case 4:
/* 173 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("field"))) {
/* 174 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 175 */         this.$_ngcc_current_state = 3;
/*     */       }
/*     */       else {
/* 178 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 181 */       break;
/*     */     case 8:
/* 184 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 185 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 277, null, AnnotationContext.IDENTITY_CONSTRAINT);
/* 186 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 189 */         this.$_ngcc_current_state = 7;
/* 190 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 193 */       break;
/*     */     case 10:
/*     */       int $ai;
/* 196 */       if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/* 197 */         this.$runtime.consumeAttribute($ai);
/* 198 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 201 */         this.$_ngcc_current_state = 8;
/* 202 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 205 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 208 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 209 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 274);
/* 210 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 213 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 216 */       break;
/*     */     case 2:
/*     */     case 5:
/*     */     case 9:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     default:
/* 219 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 227 */     this.$uri = $__uri;
/* 228 */     this.$localName = $__local;
/* 229 */     this.$qname = $__qname;
/* 230 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 5:
/* 233 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("selector"))) {
/* 234 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 235 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 238 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 241 */       break;
/*     */     case 16:
/*     */       int $ai;
/* 244 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 245 */         this.$runtime.consumeAttribute($ai);
/* 246 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 249 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 252 */       break;
/*     */     case 1:
/* 255 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique")))) {
/* 256 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 257 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 260 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 263 */       break;
/*     */     case 2:
/* 266 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("field"))) {
/* 267 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 268 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 271 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 274 */       break;
/*     */     case 0:
/* 277 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 279 */       break;
/*     */     case 17:
/*     */       int $ai;
/* 282 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 283 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 287, null);
/* 284 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 287 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 290 */       break;
/*     */     case 3:
/*     */       int $ai;
/* 293 */       if ((($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("field"))) {
/* 294 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 270);
/* 295 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 298 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 301 */       break;
/*     */     case 8:
/* 304 */       this.$_ngcc_current_state = 7;
/* 305 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 307 */       break;
/*     */     case 10:
/*     */       int $ai;
/* 310 */       if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/* 311 */         this.$runtime.consumeAttribute($ai);
/* 312 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 315 */         this.$_ngcc_current_state = 8;
/* 316 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 319 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 322 */       if ((($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("selector"))) {
/* 323 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 274);
/* 324 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 327 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 330 */       break;
/*     */     case 4:
/*     */     case 7:
/*     */     case 9:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     default:
/* 333 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 341 */     this.$uri = $__uri;
/* 342 */     this.$localName = $__local;
/* 343 */     this.$qname = $__qname;
/* 344 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 16:
/* 347 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 348 */         this.$_ngcc_current_state = 15;
/*     */       }
/*     */       else {
/* 351 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 354 */       break;
/*     */     case 0:
/* 357 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 359 */       break;
/*     */     case 17:
/* 362 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 363 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 287, null);
/* 364 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 367 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 370 */       break;
/*     */     case 3:
/* 373 */       if (($__uri.equals("")) && ($__local.equals("xpath"))) {
/* 374 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 270);
/* 375 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 378 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 381 */       break;
/*     */     case 8:
/* 384 */       this.$_ngcc_current_state = 7;
/* 385 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 387 */       break;
/*     */     case 10:
/* 390 */       if (($__uri.equals("")) && ($__local.equals("refer"))) {
/* 391 */         this.$_ngcc_current_state = 12;
/*     */       }
/*     */       else {
/* 394 */         this.$_ngcc_current_state = 8;
/* 395 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 398 */       break;
/*     */     case 6:
/* 401 */       if (($__uri.equals("")) && ($__local.equals("xpath"))) {
/* 402 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 274);
/* 403 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 406 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 409 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 9:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     default:
/* 412 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 420 */     this.$uri = $__uri;
/* 421 */     this.$localName = $__local;
/* 422 */     this.$qname = $__qname;
/* 423 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 426 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 428 */       break;
/*     */     case 14:
/* 431 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 432 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 435 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 438 */       break;
/*     */     case 8:
/* 441 */       this.$_ngcc_current_state = 7;
/* 442 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 444 */       break;
/*     */     case 10:
/* 447 */       this.$_ngcc_current_state = 8;
/* 448 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 450 */       break;
/*     */     case 11:
/* 453 */       if (($__uri.equals("")) && ($__local.equals("refer"))) {
/* 454 */         this.$_ngcc_current_state = 8;
/*     */       }
/*     */       else {
/* 457 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 460 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 12:
/*     */     case 13:
/*     */     default:
/* 463 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 471 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 15:
/* 474 */       this.name = $value;
/* 475 */       this.$_ngcc_current_state = 14;
/*     */ 
/* 477 */       break;
/*     */     case 16:
/*     */       int $ai;
/* 480 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 481 */         this.$runtime.consumeAttribute($ai);
/* 482 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 0:
/* 488 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */ 
/* 490 */       break;
/*     */     case 12:
/* 493 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 280);
/* 494 */       spawnChildFromText(h, $value);
/*     */ 
/* 496 */       break;
/*     */     case 17:
/*     */       int $ai;
/* 499 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 500 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 287, null);
/* 501 */         spawnChildFromText(h, $value);
/* 502 */       }break;
/*     */     case 3:
/*     */       int $ai;
/* 507 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 508 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 270);
/* 509 */         spawnChildFromText(h, $value);
/* 510 */       }break;
/*     */     case 8:
/* 515 */       this.$_ngcc_current_state = 7;
/* 516 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 518 */       break;
/*     */     case 10:
/*     */       int $ai;
/* 521 */       if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/* 522 */         this.$runtime.consumeAttribute($ai);
/* 523 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 526 */         this.$_ngcc_current_state = 8;
/* 527 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 530 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 533 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 534 */         NGCCHandler h = new xpath(this, this._source, this.$runtime, 274);
/* 535 */         spawnChildFromText(h, $value); } break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 9:
/*     */     case 11:
/*     */     case 13:
/*     */     case 14: }  } 
/* 543 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 270:
/* 546 */       this.field = ((XPathImpl)$__result__);
/* 547 */       action0();
/* 548 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 550 */       break;
/*     */     case 287:
/* 553 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 554 */       this.$_ngcc_current_state = 16;
/*     */ 
/* 556 */       break;
/*     */     case 280:
/* 559 */       this.ref = ((UName)$__result__);
/* 560 */       action1();
/* 561 */       this.$_ngcc_current_state = 11;
/*     */ 
/* 563 */       break;
/*     */     case 277:
/* 566 */       this.ann = ((AnnotationImpl)$__result__);
/* 567 */       this.$_ngcc_current_state = 7;
/*     */ 
/* 569 */       break;
/*     */     case 274:
/* 572 */       this.selector = ((XPathImpl)$__result__);
/* 573 */       this.$_ngcc_current_state = 5;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 580 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   private IdentityConstraintImpl makeResult()
/*     */   {
/* 590 */     return new IdentityConstraintImpl(this.$runtime.document, this.ann, this.$runtime.copyLocator(), this.fa, this.category, this.name, this.selector, this.fields, this.refer);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.identityConstraint
 * JD-Core Version:    0.6.2
 */