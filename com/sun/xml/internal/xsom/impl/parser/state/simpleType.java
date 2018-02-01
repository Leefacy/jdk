/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ListSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.RestrictionSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UnionSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class simpleType extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String finalValue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private SimpleTypeImpl result;
/*     */   private Locator locator;
/*     */   private Set finalSet;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  57 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public simpleType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  61 */     super(source, parent, cookie);
/*  62 */     this.$runtime = runtime;
/*  63 */     this.$_ngcc_current_state = 19;
/*     */   }
/*     */ 
/*     */   public simpleType(NGCCRuntimeEx runtime) {
/*  67 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  71 */     this.finalSet = makeFinalSet(this.finalValue);
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException {
/*  75 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  80 */     this.$uri = $__uri;
/*  81 */     this.$localName = $__local;
/*  82 */     this.$qname = $__qname;
/*  83 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 15:
/*     */       int $ai;
/*  86 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  87 */         this.$runtime.consumeAttribute($ai);
/*  88 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  91 */         this.$_ngcc_current_state = 11;
/*  92 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  95 */       break;
/*     */     case 11:
/*     */       int $ai;
/*  98 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  99 */         this.$runtime.consumeAttribute($ai);
/* 100 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 103 */         this.$_ngcc_current_state = 10;
/* 104 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 107 */       break;
/*     */     case 19:
/* 110 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 111 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 112 */         action1();
/* 113 */         this.$_ngcc_current_state = 15;
/*     */       }
/*     */       else {
/* 116 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 119 */       break;
/*     */     case 8:
/* 122 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 123 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 89, null, AnnotationContext.SIMPLETYPE_DECL);
/* 124 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 127 */         this.$_ngcc_current_state = 7;
/* 128 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 131 */       break;
/*     */     case 10:
/* 134 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("union"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("list")))) {
/* 135 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 91, this.fa);
/* 136 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 139 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 142 */       break;
/*     */     case 7:
/* 145 */       action0();
/* 146 */       this.$_ngcc_current_state = 2;
/* 147 */       this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 149 */       break;
/*     */     case 0:
/* 152 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 154 */       break;
/*     */     case 2:
/* 157 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/* 158 */         NGCCHandler h = new SimpleType_Restriction(this, this._source, this.$runtime, 85, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 159 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/* 162 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("list"))) {
/* 163 */         NGCCHandler h = new SimpleType_List(this, this._source, this.$runtime, 86, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 164 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/* 167 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("union"))) {
/* 168 */         NGCCHandler h = new SimpleType_Union(this, this._source, this.$runtime, 80, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 169 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 172 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 177 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     default:
/* 180 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 188 */     this.$uri = $__uri;
/* 189 */     this.$localName = $__local;
/* 190 */     this.$qname = $__qname;
/* 191 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 15:
/*     */       int $ai;
/* 194 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 195 */         this.$runtime.consumeAttribute($ai);
/* 196 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 199 */         this.$_ngcc_current_state = 11;
/* 200 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 203 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 206 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 207 */         this.$runtime.consumeAttribute($ai);
/* 208 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 211 */         this.$_ngcc_current_state = 10;
/* 212 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 215 */       break;
/*     */     case 8:
/* 218 */       this.$_ngcc_current_state = 7;
/* 219 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 221 */       break;
/*     */     case 7:
/* 224 */       action0();
/* 225 */       this.$_ngcc_current_state = 2;
/* 226 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 228 */       break;
/*     */     case 1:
/* 231 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 232 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 233 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 236 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 239 */       break;
/*     */     case 0:
/* 242 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 244 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     default:
/* 247 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 255 */     this.$uri = $__uri;
/* 256 */     this.$localName = $__local;
/* 257 */     this.$qname = $__qname;
/* 258 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 15:
/* 261 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/* 262 */         this.$_ngcc_current_state = 17;
/*     */       }
/*     */       else {
/* 265 */         this.$_ngcc_current_state = 11;
/* 266 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 269 */       break;
/*     */     case 11:
/* 272 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 273 */         this.$_ngcc_current_state = 13;
/*     */       }
/*     */       else {
/* 276 */         this.$_ngcc_current_state = 10;
/* 277 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 280 */       break;
/*     */     case 8:
/* 283 */       this.$_ngcc_current_state = 7;
/* 284 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 286 */       break;
/*     */     case 7:
/* 289 */       action0();
/* 290 */       this.$_ngcc_current_state = 2;
/* 291 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 293 */       break;
/*     */     case 0:
/* 296 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 298 */       break;
/*     */     default:
/* 301 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 309 */     this.$uri = $__uri;
/* 310 */     this.$localName = $__local;
/* 311 */     this.$qname = $__qname;
/* 312 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 15:
/* 315 */       this.$_ngcc_current_state = 11;
/* 316 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 318 */       break;
/*     */     case 11:
/* 321 */       this.$_ngcc_current_state = 10;
/* 322 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 324 */       break;
/*     */     case 8:
/* 327 */       this.$_ngcc_current_state = 7;
/* 328 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 330 */       break;
/*     */     case 7:
/* 333 */       action0();
/* 334 */       this.$_ngcc_current_state = 2;
/* 335 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 337 */       break;
/*     */     case 12:
/* 340 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 341 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 344 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 347 */       break;
/*     */     case 16:
/* 350 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/* 351 */         this.$_ngcc_current_state = 11;
/*     */       }
/*     */       else {
/* 354 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 357 */       break;
/*     */     case 0:
/* 360 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 362 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     case 10:
/*     */     case 13:
/*     */     case 14:
/*     */     default:
/* 365 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 373 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 13:
/* 376 */       this.name = $value;
/* 377 */       this.$_ngcc_current_state = 12;
/*     */ 
/* 379 */       break;
/*     */     case 15:
/*     */       int $ai;
/* 382 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 383 */         this.$runtime.consumeAttribute($ai);
/* 384 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 387 */         this.$_ngcc_current_state = 11;
/* 388 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 391 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 394 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 395 */         this.$runtime.consumeAttribute($ai);
/* 396 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 399 */         this.$_ngcc_current_state = 10;
/* 400 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 403 */       break;
/*     */     case 8:
/* 406 */       this.$_ngcc_current_state = 7;
/* 407 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 409 */       break;
/*     */     case 7:
/* 412 */       action0();
/* 413 */       this.$_ngcc_current_state = 2;
/* 414 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 416 */       break;
/*     */     case 17:
/* 419 */       this.finalValue = $value;
/* 420 */       this.$_ngcc_current_state = 16;
/*     */ 
/* 422 */       break;
/*     */     case 0:
/* 425 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     case 14:
/*     */     case 16: }  } 
/* 432 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 89:
/* 435 */       this.annotation = ((AnnotationImpl)$__result__);
/* 436 */       this.$_ngcc_current_state = 7;
/*     */ 
/* 438 */       break;
/*     */     case 91:
/* 441 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 442 */       this.$_ngcc_current_state = 8;
/*     */ 
/* 444 */       break;
/*     */     case 85:
/* 447 */       this.result = ((RestrictionSimpleTypeImpl)$__result__);
/* 448 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 450 */       break;
/*     */     case 86:
/* 453 */       this.result = ((ListSimpleTypeImpl)$__result__);
/* 454 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 456 */       break;
/*     */     case 80:
/* 459 */       this.result = ((UnionSimpleTypeImpl)$__result__);
/* 460 */       this.$_ngcc_current_state = 1;
/*     */     case 81:
/*     */     case 82:
/*     */     case 83:
/*     */     case 84:
/*     */     case 87:
/*     */     case 88:
/*     */     case 90: }  } 
/* 467 */   public boolean accepted() { return this.$_ngcc_current_state == 0; }
/*     */ 
/*     */ 
/*     */   private Set makeFinalSet(String finalValue)
/*     */   {
/* 479 */     if (finalValue == null) {
/* 480 */       return Collections.EMPTY_SET;
/*     */     }
/* 482 */     Set s = new HashSet();
/* 483 */     StringTokenizer tokens = new StringTokenizer(finalValue);
/* 484 */     while (tokens.hasMoreTokens()) {
/* 485 */       String token = tokens.nextToken();
/* 486 */       if (token.equals("#all")) {
/* 487 */         s.add(XSVariety.ATOMIC);
/* 488 */         s.add(XSVariety.UNION);
/* 489 */         s.add(XSVariety.LIST);
/*     */       }
/* 491 */       if (token.equals("list")) {
/* 492 */         s.add(XSVariety.LIST);
/*     */       }
/* 494 */       if (token.equals("union")) {
/* 495 */         s.add(XSVariety.UNION);
/*     */       }
/* 497 */       if (token.equals("restriction")) {
/* 498 */         s.add(XSVariety.ATOMIC);
/*     */       }
/*     */     }
/* 501 */     return s;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.simpleType
 * JD-Core Version:    0.6.2
 */