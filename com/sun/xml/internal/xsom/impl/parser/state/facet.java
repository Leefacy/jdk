/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.FacetImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class facet extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String fixed;
/*     */   private String value;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private FacetImpl result;
/*     */   private Locator locator;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  57 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public facet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  61 */     super(source, parent, cookie);
/*  62 */     this.$runtime = runtime;
/*  63 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */ 
/*     */   public facet(NGCCRuntimeEx runtime) {
/*  67 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  72 */     this.result = new FacetImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.$localName, this.$runtime
/*  74 */       .createXmlString(this.value), 
/*  74 */       this.$runtime.parseBoolean(this.fixed));
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException
/*     */   {
/*  79 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  84 */     this.$uri = $__uri;
/*  85 */     this.$localName = $__local;
/*  86 */     this.$qname = $__qname;
/*  87 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 12:
/*  90 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern")))) {
/*  91 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  92 */         action1();
/*  93 */         this.$_ngcc_current_state = 11;
/*     */       }
/*     */       else {
/*  96 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/*  99 */       break;
/*     */     case 4:
/* 102 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 103 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 230, this.fa);
/* 104 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 107 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 110 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 113 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 114 */         this.$runtime.consumeAttribute($ai);
/* 115 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 118 */         this.$_ngcc_current_state = 4;
/* 119 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 122 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 125 */       if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 126 */         this.$runtime.consumeAttribute($ai);
/* 127 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 130 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 133 */       break;
/*     */     case 0:
/* 136 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 138 */       break;
/*     */     case 2:
/* 141 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 142 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 228, null, AnnotationContext.SIMPLETYPE_DECL);
/* 143 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 146 */         this.$_ngcc_current_state = 1;
/* 147 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 150 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/* 153 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 161 */     this.$uri = $__uri;
/* 162 */     this.$localName = $__local;
/* 163 */     this.$qname = $__qname;
/* 164 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 167 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern")))) {
/* 168 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 230, this.fa);
/* 169 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 172 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 175 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 178 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 179 */         this.$runtime.consumeAttribute($ai);
/* 180 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 183 */         this.$_ngcc_current_state = 4;
/* 184 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 187 */       break;
/*     */     case 1:
/* 190 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern")))) {
/* 191 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 192 */         this.$_ngcc_current_state = 0;
/* 193 */         action0();
/*     */       }
/*     */       else {
/* 196 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 199 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 202 */       if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 203 */         this.$runtime.consumeAttribute($ai);
/* 204 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 207 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 210 */       break;
/*     */     case 0:
/* 213 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 215 */       break;
/*     */     case 2:
/* 218 */       this.$_ngcc_current_state = 1;
/* 219 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 221 */       break;
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/* 224 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 232 */     this.$uri = $__uri;
/* 233 */     this.$localName = $__local;
/* 234 */     this.$qname = $__qname;
/* 235 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 5:
/* 238 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/* 239 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 242 */         this.$_ngcc_current_state = 4;
/* 243 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 246 */       break;
/*     */     case 11:
/* 249 */       if (($__uri.equals("")) && ($__local.equals("value"))) {
/* 250 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 253 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 256 */       break;
/*     */     case 0:
/* 259 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 261 */       break;
/*     */     case 2:
/* 264 */       this.$_ngcc_current_state = 1;
/* 265 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 267 */       break;
/*     */     default:
/* 270 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 278 */     this.$uri = $__uri;
/* 279 */     this.$localName = $__local;
/* 280 */     this.$qname = $__qname;
/* 281 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 5:
/* 284 */       this.$_ngcc_current_state = 4;
/* 285 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 287 */       break;
/*     */     case 9:
/* 290 */       if (($__uri.equals("")) && ($__local.equals("value"))) {
/* 291 */         this.$_ngcc_current_state = 5;
/*     */       }
/*     */       else {
/* 294 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 297 */       break;
/*     */     case 0:
/* 300 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 302 */       break;
/*     */     case 6:
/* 305 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/* 306 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 309 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 312 */       break;
/*     */     case 2:
/* 315 */       this.$_ngcc_current_state = 1;
/* 316 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 318 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 7:
/*     */     case 8:
/*     */     default:
/* 321 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 329 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 5:
/*     */       int $ai;
/* 332 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 333 */         this.$runtime.consumeAttribute($ai);
/* 334 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 337 */         this.$_ngcc_current_state = 4;
/* 338 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 341 */       break;
/*     */     case 7:
/* 344 */       this.fixed = $value;
/* 345 */       this.$_ngcc_current_state = 6;
/*     */ 
/* 347 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 350 */       if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 351 */         this.$runtime.consumeAttribute($ai);
/* 352 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 10:
/* 358 */       this.value = $value;
/* 359 */       this.$_ngcc_current_state = 9;
/*     */ 
/* 361 */       break;
/*     */     case 0:
/* 364 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 366 */       break;
/*     */     case 2:
/* 369 */       this.$_ngcc_current_state = 1;
/* 370 */       this.$runtime.sendText(this._cookie, $value);
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 8:
/*     */     case 9: }  } 
/* 377 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 230:
/* 380 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 381 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 383 */       break;
/*     */     case 228:
/* 386 */       this.annotation = ((AnnotationImpl)$__result__);
/* 387 */       this.$_ngcc_current_state = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 394 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.facet
 * JD-Core Version:    0.6.2
 */