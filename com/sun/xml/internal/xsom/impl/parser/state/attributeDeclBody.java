/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttributeDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.impl.parser.ParserContext;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class attributeDeclBody extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl annotation;
/*     */   private Locator locator;
/*     */   private boolean isLocal;
/*     */   private String defaultValue;
/*     */   private UName typeName;
/*     */   private String fixedValue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private boolean form;
/* 517 */   private boolean formSpecified = false;
/*     */   private Ref.SimpleType type;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  61 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public attributeDeclBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, boolean _isLocal, String _defaultValue, String _fixedValue) {
/*  65 */     super(source, parent, cookie);
/*  66 */     this.$runtime = runtime;
/*  67 */     this.locator = _locator;
/*  68 */     this.isLocal = _isLocal;
/*  69 */     this.defaultValue = _defaultValue;
/*  70 */     this.fixedValue = _fixedValue;
/*  71 */     this.$_ngcc_current_state = 13;
/*     */   }
/*     */ 
/*     */   public attributeDeclBody(NGCCRuntimeEx runtime, Locator _locator, boolean _isLocal, String _defaultValue, String _fixedValue) {
/*  75 */     this(null, runtime, runtime, -1, _locator, _isLocal, _defaultValue, _fixedValue);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  80 */     this.type = new DelayedRef.SimpleType(this.$runtime, this.locator, this.$runtime.currentSchema, this.typeName);
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  86 */     this.formSpecified = true;
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  91 */     this.$uri = $__uri;
/*  92 */     this.$localName = $__local;
/*  93 */     this.$qname = $__qname;
/*  94 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  97 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/*  99 */       break;
/*     */     case 12:
/*     */       int $ai;
/* 102 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 103 */         this.$runtime.consumeAttribute($ai);
/* 104 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 107 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 110 */       break;
/*     */     case 7:
/* 113 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 114 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 388, null, AnnotationContext.ATTRIBUTE_DECL);
/* 115 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 118 */         this.$_ngcc_current_state = 1;
/* 119 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 122 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 125 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || ((($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType")))) {
/* 126 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 127 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else
/*     */       {
/*     */         int $ai;
/* 130 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 131 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 134 */       break;
/*     */     case 13:
/*     */       int $ai;
/* 137 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 138 */         this.$runtime.consumeAttribute($ai);
/* 139 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 142 */         this.$_ngcc_current_state = 12;
/* 143 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 146 */       break;
/*     */     case 1:
/*     */       int $ai;
/* 149 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 150 */         this.$runtime.consumeAttribute($ai);
/* 151 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/* 154 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 155 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 379);
/* 156 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 159 */         this.$_ngcc_current_state = 0;
/* 160 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 164 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 10:
/*     */     case 11:
/*     */     default:
/* 167 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 175 */     this.$uri = $__uri;
/* 176 */     this.$localName = $__local;
/* 177 */     this.$qname = $__qname;
/* 178 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 181 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 183 */       break;
/*     */     case 12:
/*     */       int $ai;
/* 186 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 187 */         this.$runtime.consumeAttribute($ai);
/* 188 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 191 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 194 */       break;
/*     */     case 7:
/* 197 */       this.$_ngcc_current_state = 1;
/* 198 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 200 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 203 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 204 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 205 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 208 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 209 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 212 */       break;
/*     */     case 13:
/*     */       int $ai;
/* 215 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 216 */         this.$runtime.consumeAttribute($ai);
/* 217 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 220 */         this.$_ngcc_current_state = 12;
/* 221 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 224 */       break;
/*     */     case 1:
/*     */       int $ai;
/* 227 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 228 */         this.$runtime.consumeAttribute($ai);
/* 229 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 232 */         this.$_ngcc_current_state = 0;
/* 233 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 236 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 10:
/*     */     case 11:
/*     */     default:
/* 239 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 247 */     this.$uri = $__uri;
/* 248 */     this.$localName = $__local;
/* 249 */     this.$qname = $__qname;
/* 250 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 253 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 255 */       break;
/*     */     case 12:
/* 258 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 259 */         this.$_ngcc_current_state = 11;
/*     */       }
/*     */       else {
/* 262 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 265 */       break;
/*     */     case 7:
/* 268 */       this.$_ngcc_current_state = 1;
/* 269 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 271 */       break;
/*     */     case 9:
/* 274 */       if (($__uri.equals("")) && ($__local.equals("type"))) {
/* 275 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 276 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 279 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 280 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 283 */       break;
/*     */     case 13:
/* 286 */       if (($__uri.equals("")) && ($__local.equals("form"))) {
/* 287 */         this.$_ngcc_current_state = 15;
/*     */       }
/*     */       else {
/* 290 */         this.$_ngcc_current_state = 12;
/* 291 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 294 */       break;
/*     */     case 1:
/* 297 */       if (($__uri.equals("")) && ($__local.equals("type"))) {
/* 298 */         this.$_ngcc_current_state = 5;
/*     */       }
/*     */       else {
/* 301 */         this.$_ngcc_current_state = 0;
/* 302 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 305 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 10:
/*     */     case 11:
/*     */     default:
/* 308 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 316 */     this.$uri = $__uri;
/* 317 */     this.$localName = $__local;
/* 318 */     this.$qname = $__qname;
/* 319 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 322 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 324 */       break;
/*     */     case 14:
/* 327 */       if (($__uri.equals("")) && ($__local.equals("form"))) {
/* 328 */         this.$_ngcc_current_state = 12;
/*     */       }
/*     */       else {
/* 331 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 334 */       break;
/*     */     case 7:
/* 337 */       this.$_ngcc_current_state = 1;
/* 338 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 340 */       break;
/*     */     case 10:
/* 343 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 344 */         this.$_ngcc_current_state = 9;
/*     */       }
/*     */       else {
/* 347 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 350 */       break;
/*     */     case 9:
/* 353 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 354 */       spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */ 
/* 356 */       break;
/*     */     case 13:
/* 359 */       this.$_ngcc_current_state = 12;
/* 360 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 362 */       break;
/*     */     case 1:
/* 365 */       this.$_ngcc_current_state = 0;
/* 366 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 368 */       break;
/*     */     case 4:
/* 371 */       if (($__uri.equals("")) && ($__local.equals("type"))) {
/* 372 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 375 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 378 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 11:
/*     */     case 12:
/*     */     default:
/* 381 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 389 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 392 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */ 
/* 394 */       break;
/*     */     case 12:
/*     */       int $ai;
/* 397 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 398 */         this.$runtime.consumeAttribute($ai);
/* 399 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 7:
/* 405 */       this.$_ngcc_current_state = 1;
/* 406 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 408 */       break;
/*     */     case 9:
/*     */       int $ai;
/* 411 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 412 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 413 */         spawnChildFromText(h, $value);
/*     */       }
/*     */       else {
/* 416 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 390, this.fa);
/* 417 */         spawnChildFromText(h, $value);
/*     */       }
/*     */ 
/* 420 */       break;
/*     */     case 13:
/*     */       int $ai;
/* 423 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 424 */         this.$runtime.consumeAttribute($ai);
/* 425 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 428 */         this.$_ngcc_current_state = 12;
/* 429 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 432 */       break;
/*     */     case 15:
/* 435 */       if ($value.equals("unqualified")) {
/* 436 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 395);
/* 437 */         spawnChildFromText(h, $value);
/*     */       }
/* 440 */       else if ($value.equals("qualified")) {
/* 441 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 395);
/* 442 */         spawnChildFromText(h, $value);
/* 443 */       }break;
/*     */     case 1:
/*     */       int $ai;
/* 449 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 450 */         this.$runtime.consumeAttribute($ai);
/* 451 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 454 */         this.$_ngcc_current_state = 0;
/* 455 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 458 */       break;
/*     */     case 11:
/* 461 */       this.name = $value;
/* 462 */       this.$_ngcc_current_state = 10;
/*     */ 
/* 464 */       break;
/*     */     case 5:
/* 467 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 381);
/* 468 */       spawnChildFromText(h, $value);
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 8:
/*     */     case 10:
/*     */     case 14: }  } 
/* 475 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 388:
/* 478 */       this.annotation = ((AnnotationImpl)$__result__);
/* 479 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 481 */       break;
/*     */     case 379:
/* 484 */       this.type = ((SimpleTypeImpl)$__result__);
/* 485 */       this.$_ngcc_current_state = 0;
/*     */ 
/* 487 */       break;
/*     */     case 390:
/* 490 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 491 */       this.$_ngcc_current_state = 7;
/*     */ 
/* 493 */       break;
/*     */     case 395:
/* 496 */       this.form = ((Boolean)$__result__).booleanValue();
/* 497 */       action1();
/* 498 */       this.$_ngcc_current_state = 14;
/*     */ 
/* 500 */       break;
/*     */     case 381:
/* 503 */       this.typeName = ((UName)$__result__);
/* 504 */       action0();
/* 505 */       this.$_ngcc_current_state = 4;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 512 */     return (this.$_ngcc_current_state == 0) || (this.$_ngcc_current_state == 1) || (this.$_ngcc_current_state == 7);
/*     */   }
/*     */ 
/*     */   private AttributeDeclImpl makeResult()
/*     */   {
/* 521 */     if (this.type == null)
/*     */     {
/* 523 */       this.type = this.$runtime.parser.schemaSet.anySimpleType;
/*     */     }
/* 525 */     if (!this.formSpecified) this.form = this.$runtime.attributeFormDefault;
/*     */ 
/* 527 */     if (!this.isLocal) this.form = true;
/* 530 */     String tns;
/*     */     String tns;
/* 530 */     if (this.form == true) tns = this.$runtime.currentSchema.getTargetNamespace(); else {
/* 531 */       tns = "";
/*     */     }
/*     */ 
/* 537 */     return new AttributeDeclImpl(this.$runtime.document, tns, this.name, this.annotation, this.locator, this.fa, this.isLocal, this.$runtime
/* 536 */       .createXmlString(this.defaultValue), 
/* 536 */       this.$runtime
/* 537 */       .createXmlString(this.fixedValue), 
/* 537 */       this.type);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.attributeDeclBody
 * JD-Core Version:    0.6.2
 */