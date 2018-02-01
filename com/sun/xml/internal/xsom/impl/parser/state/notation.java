/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.NotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class notation extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private String pub;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String sys;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  58 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public notation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  62 */     super(source, parent, cookie);
/*  63 */     this.$runtime = runtime;
/*  64 */     this.$_ngcc_current_state = 16;
/*     */   }
/*     */ 
/*     */   public notation(NGCCRuntimeEx runtime) {
/*  68 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  72 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  77 */     this.$uri = $__uri;
/*  78 */     this.$localName = $__local;
/*  79 */     this.$qname = $__qname;
/*  80 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/*  83 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  84 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 209, null, AnnotationContext.NOTATION);
/*  85 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  88 */         this.$_ngcc_current_state = 1;
/*  89 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  92 */       break;
/*     */     case 16:
/*  95 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) {
/*  96 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  97 */         action0();
/*  98 */         this.$_ngcc_current_state = 15;
/*     */       }
/*     */       else {
/* 101 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 104 */       break;
/*     */     case 14:
/*     */       int $ai;
/* 107 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 108 */         this.$runtime.consumeAttribute($ai);
/* 109 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 112 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 115 */       break;
/*     */     case 15:
/*     */       int $ai;
/* 118 */       if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 119 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 120 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 123 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 126 */       break;
/*     */     case 4:
/*     */       int $ai;
/* 129 */       if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 130 */         this.$runtime.consumeAttribute($ai);
/* 131 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 134 */         this.$_ngcc_current_state = 2;
/* 135 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 138 */       break;
/*     */     case 8:
/*     */       int $ai;
/* 141 */       if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 142 */         this.$runtime.consumeAttribute($ai);
/* 143 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 146 */         this.$_ngcc_current_state = 4;
/* 147 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 150 */       break;
/*     */     case 0:
/* 153 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 155 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     default:
/* 158 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 166 */     this.$uri = $__uri;
/* 167 */     this.$localName = $__local;
/* 168 */     this.$qname = $__qname;
/* 169 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 172 */       this.$_ngcc_current_state = 1;
/* 173 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 175 */       break;
/*     */     case 14:
/*     */       int $ai;
/* 178 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 179 */         this.$runtime.consumeAttribute($ai);
/* 180 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 183 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 186 */       break;
/*     */     case 15:
/*     */       int $ai;
/* 189 */       if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) {
/* 190 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 191 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 194 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 197 */       break;
/*     */     case 4:
/*     */       int $ai;
/* 200 */       if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 201 */         this.$runtime.consumeAttribute($ai);
/* 202 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 205 */         this.$_ngcc_current_state = 2;
/* 206 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 209 */       break;
/*     */     case 1:
/* 212 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) {
/* 213 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 214 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 217 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 220 */       break;
/*     */     case 8:
/*     */       int $ai;
/* 223 */       if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 224 */         this.$runtime.consumeAttribute($ai);
/* 225 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 228 */         this.$_ngcc_current_state = 4;
/* 229 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 232 */       break;
/*     */     case 0:
/* 235 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 237 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     default:
/* 240 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 248 */     this.$uri = $__uri;
/* 249 */     this.$localName = $__local;
/* 250 */     this.$qname = $__qname;
/* 251 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 254 */       this.$_ngcc_current_state = 1;
/* 255 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 257 */       break;
/*     */     case 14:
/* 260 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 261 */         this.$_ngcc_current_state = 13;
/*     */       }
/*     */       else {
/* 264 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 267 */       break;
/*     */     case 15:
/* 270 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 271 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 272 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 275 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 278 */       break;
/*     */     case 4:
/* 281 */       if (($__uri.equals("")) && ($__local.equals("system"))) {
/* 282 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 285 */         this.$_ngcc_current_state = 2;
/* 286 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 289 */       break;
/*     */     case 8:
/* 292 */       if (($__uri.equals("")) && ($__local.equals("public"))) {
/* 293 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 296 */         this.$_ngcc_current_state = 4;
/* 297 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 300 */       break;
/*     */     case 0:
/* 303 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 305 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
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
/*     */     case 2:
/* 322 */       this.$_ngcc_current_state = 1;
/* 323 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 325 */       break;
/*     */     case 9:
/* 328 */       if (($__uri.equals("")) && ($__local.equals("public"))) {
/* 329 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 332 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 335 */       break;
/*     */     case 12:
/* 338 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 339 */         this.$_ngcc_current_state = 8;
/*     */       }
/*     */       else {
/* 342 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 345 */       break;
/*     */     case 5:
/* 348 */       if (($__uri.equals("")) && ($__local.equals("system"))) {
/* 349 */         this.$_ngcc_current_state = 2;
/*     */       }
/*     */       else {
/* 352 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 355 */       break;
/*     */     case 4:
/* 358 */       this.$_ngcc_current_state = 2;
/* 359 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 361 */       break;
/*     */     case 8:
/* 364 */       this.$_ngcc_current_state = 4;
/* 365 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 367 */       break;
/*     */     case 0:
/* 370 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 372 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 10:
/*     */     case 11:
/*     */     default:
/* 375 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 383 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 386 */       this.$_ngcc_current_state = 1;
/* 387 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 389 */       break;
/*     */     case 10:
/* 392 */       this.pub = $value;
/* 393 */       this.$_ngcc_current_state = 9;
/*     */ 
/* 395 */       break;
/*     */     case 14:
/*     */       int $ai;
/* 398 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 399 */         this.$runtime.consumeAttribute($ai);
/* 400 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 15:
/*     */       int $ai;
/* 406 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 407 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 408 */         spawnChildFromText(h, $value);
/* 409 */       }break;
/*     */     case 4:
/*     */       int $ai;
/* 414 */       if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 415 */         this.$runtime.consumeAttribute($ai);
/* 416 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 419 */         this.$_ngcc_current_state = 2;
/* 420 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 423 */       break;
/*     */     case 8:
/*     */       int $ai;
/* 426 */       if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 427 */         this.$runtime.consumeAttribute($ai);
/* 428 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 431 */         this.$_ngcc_current_state = 4;
/* 432 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 435 */       break;
/*     */     case 13:
/* 438 */       this.name = $value;
/* 439 */       this.$_ngcc_current_state = 12;
/*     */ 
/* 441 */       break;
/*     */     case 6:
/* 444 */       this.sys = $value;
/* 445 */       this.$_ngcc_current_state = 5;
/*     */ 
/* 447 */       break;
/*     */     case 0:
/* 450 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/*     */     case 9:
/*     */     case 11:
/*     */     case 12: }  } 
/* 457 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 209:
/* 460 */       this.ann = ((AnnotationImpl)$__result__);
/* 461 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 463 */       break;
/*     */     case 224:
/* 466 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 467 */       this.$_ngcc_current_state = 14;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 474 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   private XSNotation makeResult()
/*     */   {
/* 480 */     return new NotationImpl(this.$runtime.document, this.ann, this.loc, this.fa, this.name, this.pub, this.sys);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.notation
 * JD-Core Version:    0.6.2
 */