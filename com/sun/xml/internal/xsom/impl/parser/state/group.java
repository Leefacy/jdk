/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class group extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ModelGroupImpl term;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ModelGroupDeclImpl result;
/*     */   private Locator loc;
/*     */   private Locator mloc;
/*     */   private String compositorName;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  57 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public group(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  61 */     super(source, parent, cookie);
/*  62 */     this.$runtime = runtime;
/*  63 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */ 
/*     */   public group(NGCCRuntimeEx runtime) {
/*  67 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  72 */     this.result = new ModelGroupDeclImpl(this.$runtime.document, this.annotation, this.loc, this.fa, this.$runtime.currentSchema
/*  74 */       .getTargetNamespace(), this.name, this.term);
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  83 */     this.mloc = this.$runtime.copyLocator();
/*  84 */     this.compositorName = this.$localName;
/*     */   }
/*     */ 
/*     */   private void action2() throws SAXException
/*     */   {
/*  89 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  94 */     this.$uri = $__uri;
/*  95 */     this.$localName = $__local;
/*  96 */     this.$qname = $__qname;
/*  97 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 10:
/*     */       int $ai;
/* 100 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 101 */         this.$runtime.consumeAttribute($ai);
/* 102 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 105 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 108 */       break;
/*     */     case 5:
/* 111 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence")))) {
/* 112 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 357, null);
/* 113 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 116 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 119 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 122 */       if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 123 */         this.$runtime.consumeAttribute($ai);
/* 124 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 127 */         this.$_ngcc_current_state = 10;
/* 128 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 131 */       break;
/*     */     case 6:
/* 134 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 135 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 359, null, AnnotationContext.MODELGROUP_DECL);
/* 136 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 139 */         this.$_ngcc_current_state = 5;
/* 140 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 143 */       break;
/*     */     case 0:
/* 146 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 148 */       break;
/*     */     case 4:
/* 151 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence")))) {
/* 152 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 153 */         this.$_ngcc_current_state = 3;
/*     */       }
/*     */       else {
/* 156 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 159 */       break;
/*     */     case 15:
/* 162 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) {
/* 163 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 164 */         action2();
/* 165 */         this.$_ngcc_current_state = 11;
/*     */       }
/*     */       else {
/* 168 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 171 */       break;
/*     */     case 3:
/* 174 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence")))) {
/* 175 */         NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 355, this.mloc, this.compositorName);
/* 176 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 179 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 182 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     default:
/* 185 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 193 */     this.$uri = $__uri;
/* 194 */     this.$localName = $__local;
/* 195 */     this.$qname = $__qname;
/* 196 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 10:
/*     */       int $ai;
/* 199 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 200 */         this.$runtime.consumeAttribute($ai);
/* 201 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 204 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 207 */       break;
/*     */     case 11:
/*     */       int $ai;
/* 210 */       if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 211 */         this.$runtime.consumeAttribute($ai);
/* 212 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 215 */         this.$_ngcc_current_state = 10;
/* 216 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 219 */       break;
/*     */     case 1:
/* 222 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) {
/* 223 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 224 */         this.$_ngcc_current_state = 0;
/* 225 */         action0();
/*     */       }
/*     */       else {
/* 228 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 231 */       break;
/*     */     case 6:
/* 234 */       this.$_ngcc_current_state = 5;
/* 235 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 237 */       break;
/*     */     case 0:
/* 240 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 242 */       break;
/*     */     case 2:
/* 245 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence")))) {
/* 246 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 247 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 250 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 253 */       break;
/*     */     case 3:
/* 256 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence")))) {
/* 257 */         NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 355, this.mloc, this.compositorName);
/* 258 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 261 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 264 */       break;
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     default:
/* 267 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 275 */     this.$uri = $__uri;
/* 276 */     this.$localName = $__local;
/* 277 */     this.$qname = $__qname;
/* 278 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 10:
/* 281 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 282 */         this.$_ngcc_current_state = 9;
/*     */       }
/*     */       else {
/* 285 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 288 */       break;
/*     */     case 11:
/* 291 */       if (($__uri.equals("")) && ($__local.equals("ID"))) {
/* 292 */         this.$_ngcc_current_state = 13;
/*     */       }
/*     */       else {
/* 295 */         this.$_ngcc_current_state = 10;
/* 296 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 299 */       break;
/*     */     case 6:
/* 302 */       this.$_ngcc_current_state = 5;
/* 303 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 305 */       break;
/*     */     case 0:
/* 308 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 310 */       break;
/*     */     default:
/* 313 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 321 */     this.$uri = $__uri;
/* 322 */     this.$localName = $__local;
/* 323 */     this.$qname = $__qname;
/* 324 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 11:
/* 327 */       this.$_ngcc_current_state = 10;
/* 328 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 330 */       break;
/*     */     case 6:
/* 333 */       this.$_ngcc_current_state = 5;
/* 334 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 336 */       break;
/*     */     case 0:
/* 339 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 341 */       break;
/*     */     case 12:
/* 344 */       if (($__uri.equals("")) && ($__local.equals("ID"))) {
/* 345 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 348 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 351 */       break;
/*     */     case 8:
/* 354 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 355 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 358 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 361 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/* 364 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 372 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 10:
/*     */       int $ai;
/* 375 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 376 */         this.$runtime.consumeAttribute($ai);
/* 377 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 11:
/*     */       int $ai;
/* 383 */       if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 384 */         this.$runtime.consumeAttribute($ai);
/* 385 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 388 */         this.$_ngcc_current_state = 10;
/* 389 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 392 */       break;
/*     */     case 6:
/* 395 */       this.$_ngcc_current_state = 5;
/* 396 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 398 */       break;
/*     */     case 0:
/* 401 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 403 */       break;
/*     */     case 9:
/* 406 */       this.name = $value;
/* 407 */       this.$_ngcc_current_state = 8;
/*     */ 
/* 409 */       break;
/*     */     case 13:
/* 412 */       this.$_ngcc_current_state = 12;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 7:
/*     */     case 8:
/*     */     case 12: }  } 
/* 419 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 357:
/* 422 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 423 */       action1();
/* 424 */       this.$_ngcc_current_state = 4;
/*     */ 
/* 426 */       break;
/*     */     case 359:
/* 429 */       this.annotation = ((AnnotationImpl)$__result__);
/* 430 */       this.$_ngcc_current_state = 5;
/*     */ 
/* 432 */       break;
/*     */     case 355:
/* 435 */       this.term = ((ModelGroupImpl)$__result__);
/* 436 */       this.$_ngcc_current_state = 2;
/*     */     case 356:
/*     */     case 358:
/*     */     } }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 443 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.group
 * JD-Core Version:    0.6.2
 */