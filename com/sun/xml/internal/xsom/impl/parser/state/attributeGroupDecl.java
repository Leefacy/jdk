/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class attributeGroupDecl extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private AttGroupDeclImpl result;
/*     */   private Locator locator;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  56 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public attributeGroupDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  60 */     super(source, parent, cookie);
/*  61 */     this.$runtime = runtime;
/*  62 */     this.$_ngcc_current_state = 14;
/*     */   }
/*     */ 
/*     */   public attributeGroupDecl(NGCCRuntimeEx runtime) {
/*  66 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  71 */     this.result = new AttGroupDeclImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name);
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  77 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  82 */     this.$uri = $__uri;
/*  83 */     this.$localName = $__local;
/*  84 */     this.$qname = $__qname;
/*  85 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 6:
/*  88 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  89 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 246, this.fa);
/*  90 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  93 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/*  96 */       break;
/*     */     case 13:
/*     */       int $ai;
/*  99 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 100 */         this.$runtime.consumeAttribute($ai);
/* 101 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 104 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 107 */       break;
/*     */     case 0:
/* 110 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 112 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 115 */       if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 116 */         this.$runtime.consumeAttribute($ai);
/* 117 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 120 */         this.$_ngcc_current_state = 6;
/* 121 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 124 */       break;
/*     */     case 3:
/* 127 */       action0();
/* 128 */       this.$_ngcc_current_state = 2;
/* 129 */       this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 131 */       break;
/*     */     case 2:
/* 134 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/* 135 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 241, this.result);
/* 136 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 139 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 142 */       break;
/*     */     case 14:
/* 145 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/* 146 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 147 */         action1();
/* 148 */         this.$_ngcc_current_state = 13;
/*     */       }
/*     */       else {
/* 151 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 154 */       break;
/*     */     case 4:
/* 157 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 158 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 244, null, AnnotationContext.ATTRIBUTE_GROUP);
/* 159 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 162 */         this.$_ngcc_current_state = 3;
/* 163 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 166 */       break;
/*     */     case 1:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     default:
/* 169 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 177 */     this.$uri = $__uri;
/* 178 */     this.$localName = $__local;
/* 179 */     this.$qname = $__qname;
/* 180 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 6:
/* 183 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/* 184 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 246, this.fa);
/* 185 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 188 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 191 */       break;
/*     */     case 1:
/* 194 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/* 195 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 196 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 199 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 202 */       break;
/*     */     case 13:
/*     */       int $ai;
/* 205 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 206 */         this.$runtime.consumeAttribute($ai);
/* 207 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 210 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 213 */       break;
/*     */     case 0:
/* 216 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 218 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 221 */       if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 222 */         this.$runtime.consumeAttribute($ai);
/* 223 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 226 */         this.$_ngcc_current_state = 6;
/* 227 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 230 */       break;
/*     */     case 3:
/* 233 */       action0();
/* 234 */       this.$_ngcc_current_state = 2;
/* 235 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 237 */       break;
/*     */     case 2:
/* 240 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/* 241 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 241, this.result);
/* 242 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 245 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 248 */       break;
/*     */     case 4:
/* 251 */       this.$_ngcc_current_state = 3;
/* 252 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 254 */       break;
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     default:
/* 257 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 265 */     this.$uri = $__uri;
/* 266 */     this.$localName = $__local;
/* 267 */     this.$qname = $__qname;
/* 268 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 13:
/* 271 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 272 */         this.$_ngcc_current_state = 12;
/*     */       }
/*     */       else {
/* 275 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 278 */       break;
/*     */     case 0:
/* 281 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 283 */       break;
/*     */     case 7:
/* 286 */       if (($__uri.equals("")) && ($__local.equals("id"))) {
/* 287 */         this.$_ngcc_current_state = 9;
/*     */       }
/*     */       else {
/* 290 */         this.$_ngcc_current_state = 6;
/* 291 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 294 */       break;
/*     */     case 3:
/* 297 */       action0();
/* 298 */       this.$_ngcc_current_state = 2;
/* 299 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 301 */       break;
/*     */     case 4:
/* 304 */       this.$_ngcc_current_state = 3;
/* 305 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 307 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     default:
/* 310 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 318 */     this.$uri = $__uri;
/* 319 */     this.$localName = $__local;
/* 320 */     this.$qname = $__qname;
/* 321 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 324 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 326 */       break;
/*     */     case 7:
/* 329 */       this.$_ngcc_current_state = 6;
/* 330 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 332 */       break;
/*     */     case 3:
/* 335 */       action0();
/* 336 */       this.$_ngcc_current_state = 2;
/* 337 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 339 */       break;
/*     */     case 8:
/* 342 */       if (($__uri.equals("")) && ($__local.equals("id"))) {
/* 343 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 346 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 349 */       break;
/*     */     case 11:
/* 352 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 353 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 356 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 359 */       break;
/*     */     case 4:
/* 362 */       this.$_ngcc_current_state = 3;
/* 363 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 365 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/* 368 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 376 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 13:
/*     */       int $ai;
/* 379 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 380 */         this.$runtime.consumeAttribute($ai);
/* 381 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 0:
/* 387 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 389 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 392 */       if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 393 */         this.$runtime.consumeAttribute($ai);
/* 394 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 397 */         this.$_ngcc_current_state = 6;
/* 398 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 401 */       break;
/*     */     case 12:
/* 404 */       this.name = $value;
/* 405 */       this.$_ngcc_current_state = 11;
/*     */ 
/* 407 */       break;
/*     */     case 9:
/* 410 */       this.$_ngcc_current_state = 8;
/*     */ 
/* 412 */       break;
/*     */     case 3:
/* 415 */       action0();
/* 416 */       this.$_ngcc_current_state = 2;
/* 417 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 419 */       break;
/*     */     case 4:
/* 422 */       this.$_ngcc_current_state = 3;
/* 423 */       this.$runtime.sendText(this._cookie, $value);
/*     */     case 1:
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     case 10:
/*     */     case 11: }  } 
/* 430 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 241:
/* 433 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 435 */       break;
/*     */     case 246:
/* 438 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 439 */       this.$_ngcc_current_state = 4;
/*     */ 
/* 441 */       break;
/*     */     case 244:
/* 444 */       this.annotation = ((AnnotationImpl)$__result__);
/* 445 */       this.$_ngcc_current_state = 3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 452 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.attributeGroupDecl
 * JD-Core Version:    0.6.2
 */