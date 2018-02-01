/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class importDecl extends NGCCHandler
/*     */ {
/*     */   private String ns;
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  55 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public importDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  59 */     super(source, parent, cookie);
/*  60 */     this.$runtime = runtime;
/*  61 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */ 
/*     */   public importDecl(NGCCRuntimeEx runtime) {
/*  65 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  70 */     if (this.ns == null) this.ns = "";
/*  71 */     this.$runtime.importSchema(this.ns, this.schemaLocation);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs)
/*     */     throws SAXException
/*     */   {
/*  77 */     this.$uri = $__uri;
/*  78 */     this.$localName = $__local;
/*  79 */     this.$qname = $__qname;
/*  80 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/*     */       int $ai;
/*  83 */       if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  84 */         this.$runtime.consumeAttribute($ai);
/*  85 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  88 */         this.$_ngcc_current_state = 2;
/*  89 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  92 */       break;
/*     */     case 8:
/*     */       int $ai;
/*  95 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  96 */         this.$runtime.consumeAttribute($ai);
/*  97 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 100 */         this.$_ngcc_current_state = 4;
/* 101 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 104 */       break;
/*     */     case 12:
/* 107 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("import"))) {
/* 108 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 109 */         this.$_ngcc_current_state = 8;
/*     */       }
/*     */       else {
/* 112 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 115 */       break;
/*     */     case 2:
/* 118 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 119 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 340, null, AnnotationContext.SCHEMA);
/* 120 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 123 */         this.$_ngcc_current_state = 1;
/* 124 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 127 */       break;
/*     */     case 0:
/* 130 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 132 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
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
/*     */     case 4:
/*     */       int $ai;
/* 149 */       if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 150 */         this.$runtime.consumeAttribute($ai);
/* 151 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 154 */         this.$_ngcc_current_state = 2;
/* 155 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 158 */       break;
/*     */     case 8:
/*     */       int $ai;
/* 161 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 162 */         this.$runtime.consumeAttribute($ai);
/* 163 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 166 */         this.$_ngcc_current_state = 4;
/* 167 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 170 */       break;
/*     */     case 2:
/* 173 */       this.$_ngcc_current_state = 1;
/* 174 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 176 */       break;
/*     */     case 1:
/* 179 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("import"))) {
/* 180 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 181 */         this.$_ngcc_current_state = 0;
/* 182 */         action0();
/*     */       }
/*     */       else {
/* 185 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 188 */       break;
/*     */     case 0:
/* 191 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 193 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
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
/*     */     case 4:
/* 210 */       if (($__uri.equals("")) && ($__local.equals("schemaLocation"))) {
/* 211 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 214 */         this.$_ngcc_current_state = 2;
/* 215 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 218 */       break;
/*     */     case 8:
/* 221 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 222 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 225 */         this.$_ngcc_current_state = 4;
/* 226 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 229 */       break;
/*     */     case 2:
/* 232 */       this.$_ngcc_current_state = 1;
/* 233 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 235 */       break;
/*     */     case 0:
/* 238 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 240 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     default:
/* 243 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 251 */     this.$uri = $__uri;
/* 252 */     this.$localName = $__local;
/* 253 */     this.$qname = $__qname;
/* 254 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 9:
/* 257 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 258 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 261 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 264 */       break;
/*     */     case 4:
/* 267 */       this.$_ngcc_current_state = 2;
/* 268 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 270 */       break;
/*     */     case 8:
/* 273 */       this.$_ngcc_current_state = 4;
/* 274 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 276 */       break;
/*     */     case 5:
/* 279 */       if (($__uri.equals("")) && ($__local.equals("schemaLocation"))) {
/* 280 */         this.$_ngcc_current_state = 2;
/*     */       }
/*     */       else {
/* 283 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 286 */       break;
/*     */     case 2:
/* 289 */       this.$_ngcc_current_state = 1;
/* 290 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 292 */       break;
/*     */     case 0:
/* 295 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 297 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     default:
/* 300 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 308 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/*     */       int $ai;
/* 311 */       if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 312 */         this.$runtime.consumeAttribute($ai);
/* 313 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 316 */         this.$_ngcc_current_state = 2;
/* 317 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 320 */       break;
/*     */     case 8:
/*     */       int $ai;
/* 323 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 324 */         this.$runtime.consumeAttribute($ai);
/* 325 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 328 */         this.$_ngcc_current_state = 4;
/* 329 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 332 */       break;
/*     */     case 10:
/* 335 */       this.ns = $value;
/* 336 */       this.$_ngcc_current_state = 9;
/*     */ 
/* 338 */       break;
/*     */     case 2:
/* 341 */       this.$_ngcc_current_state = 1;
/* 342 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 344 */       break;
/*     */     case 0:
/* 347 */       revertToParentFromText(this, this._cookie, $value);
/*     */ 
/* 349 */       break;
/*     */     case 6:
/* 352 */       this.schemaLocation = $value;
/* 353 */       this.$_ngcc_current_state = 5;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/*     */     case 9: } 
/*     */   }
/* 360 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 340:
/* 363 */       this.$_ngcc_current_state = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 370 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.importDecl
 * JD-Core Version:    0.6.2
 */