/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.XPathImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class xpath extends NGCCHandler
/*     */ {
/*     */   private String xpath;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  56 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public xpath(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  60 */     super(source, parent, cookie);
/*  61 */     this.$runtime = runtime;
/*  62 */     this.$_ngcc_current_state = 6;
/*     */   }
/*     */ 
/*     */   public xpath(NGCCRuntimeEx runtime) {
/*  66 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  71 */     this.$uri = $__uri;
/*  72 */     this.$localName = $__local;
/*  73 */     this.$qname = $__qname;
/*  74 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  77 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/*  79 */       break;
/*     */     case 6:
/*     */       int $ai;
/*  82 */       if ((($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  83 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 77, null);
/*  84 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  87 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/*  90 */       break;
/*     */     case 1:
/*  93 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  94 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 72, null, AnnotationContext.XPATH);
/*  95 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  98 */         this.$_ngcc_current_state = 0;
/*  99 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 102 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 105 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 106 */         this.$runtime.consumeAttribute($ai);
/* 107 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 110 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 113 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     default:
/* 116 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 124 */     this.$uri = $__uri;
/* 125 */     this.$localName = $__local;
/* 126 */     this.$qname = $__qname;
/* 127 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 130 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 132 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 135 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 136 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 77, null);
/* 137 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 140 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 143 */       break;
/*     */     case 1:
/* 146 */       this.$_ngcc_current_state = 0;
/* 147 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 149 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 152 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 153 */         this.$runtime.consumeAttribute($ai);
/* 154 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 157 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 160 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     default:
/* 163 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 171 */     this.$uri = $__uri;
/* 172 */     this.$localName = $__local;
/* 173 */     this.$qname = $__qname;
/* 174 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 177 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 179 */       break;
/*     */     case 6:
/* 182 */       if (($__uri.equals("")) && ($__local.equals("xpath"))) {
/* 183 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 77, null);
/* 184 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 187 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 190 */       break;
/*     */     case 1:
/* 193 */       this.$_ngcc_current_state = 0;
/* 194 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 196 */       break;
/*     */     case 5:
/* 199 */       if (($__uri.equals("")) && ($__local.equals("xpath"))) {
/* 200 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 203 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 206 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     default:
/* 209 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 217 */     this.$uri = $__uri;
/* 218 */     this.$localName = $__local;
/* 219 */     this.$qname = $__qname;
/* 220 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 223 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 225 */       break;
/*     */     case 1:
/* 228 */       this.$_ngcc_current_state = 0;
/* 229 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 231 */       break;
/*     */     case 3:
/* 234 */       if (($__uri.equals("")) && ($__local.equals("xpath"))) {
/* 235 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 238 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 241 */       break;
/*     */     case 2:
/*     */     default:
/* 244 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 252 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 255 */       this.xpath = $value;
/* 256 */       this.$_ngcc_current_state = 3;
/*     */ 
/* 258 */       break;
/*     */     case 0:
/* 261 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */ 
/* 263 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 266 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 267 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 77, null);
/* 268 */         spawnChildFromText(h, $value);
/* 269 */       }break;
/*     */     case 1:
/* 274 */       this.$_ngcc_current_state = 0;
/* 275 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 277 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 280 */       if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 281 */         this.$runtime.consumeAttribute($ai);
/* 282 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }break;
/*     */     case 2:
/*     */     case 3:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 290 */     switch ($__cookie__)
/*     */     {
/*     */     case 77:
/* 293 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 294 */       this.$_ngcc_current_state = 5;
/*     */ 
/* 296 */       break;
/*     */     case 72:
/* 299 */       this.ann = ((AnnotationImpl)$__result__);
/* 300 */       this.$_ngcc_current_state = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 307 */     return (this.$_ngcc_current_state == 1) || (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   private XPathImpl makeResult()
/*     */   {
/* 313 */     return new XPathImpl(this.$runtime.document, this.ann, this.$runtime.copyLocator(), this.fa, this.$runtime
/* 313 */       .createXmlString(this.xpath));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.xpath
 * JD-Core Version:    0.6.2
 */