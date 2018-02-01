/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import java.math.BigInteger;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class occurs extends NGCCHandler
/*     */ {
/*     */   private String v;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/* 323 */   BigInteger max = BigInteger.valueOf(1L);
/* 324 */   BigInteger min = BigInteger.valueOf(1L);
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  55 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public occurs(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  59 */     super(source, parent, cookie);
/*  60 */     this.$runtime = runtime;
/*  61 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */ 
/*     */   public occurs(NGCCRuntimeEx runtime) {
/*  65 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  69 */     this.min = new BigInteger(this.v);
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException {
/*  73 */     this.max = BigInteger.valueOf(-1L);
/*     */   }
/*     */ 
/*     */   private void action2() throws SAXException {
/*  77 */     this.max = new BigInteger(this.v);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  82 */     this.$uri = $__uri;
/*  83 */     this.$localName = $__local;
/*  84 */     this.$qname = $__qname;
/*  85 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/*  88 */       if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/*  89 */         this.$runtime.consumeAttribute($ai);
/*  90 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  93 */         this.$_ngcc_current_state = 0;
/*  94 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  97 */       break;
/*     */     case 0:
/* 100 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 102 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 105 */       if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 106 */         this.$runtime.consumeAttribute($ai);
/* 107 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 110 */         this.$_ngcc_current_state = 1;
/* 111 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 114 */       break;
/*     */     default:
/* 117 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 125 */     this.$uri = $__uri;
/* 126 */     this.$localName = $__local;
/* 127 */     this.$qname = $__qname;
/* 128 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/* 131 */       if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 132 */         this.$runtime.consumeAttribute($ai);
/* 133 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 136 */         this.$_ngcc_current_state = 0;
/* 137 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 140 */       break;
/*     */     case 0:
/* 143 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 145 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 148 */       if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 149 */         this.$runtime.consumeAttribute($ai);
/* 150 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 153 */         this.$_ngcc_current_state = 1;
/* 154 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 157 */       break;
/*     */     default:
/* 160 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 168 */     this.$uri = $__uri;
/* 169 */     this.$localName = $__local;
/* 170 */     this.$qname = $__qname;
/* 171 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/* 174 */       if (($__uri.equals("")) && ($__local.equals("minOccurs"))) {
/* 175 */         this.$_ngcc_current_state = 3;
/*     */       }
/*     */       else {
/* 178 */         this.$_ngcc_current_state = 0;
/* 179 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 182 */       break;
/*     */     case 0:
/* 185 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 187 */       break;
/*     */     case 5:
/* 190 */       if (($__uri.equals("")) && ($__local.equals("maxOccurs"))) {
/* 191 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 194 */         this.$_ngcc_current_state = 1;
/* 195 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 198 */       break;
/*     */     default:
/* 201 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 209 */     this.$uri = $__uri;
/* 210 */     this.$localName = $__local;
/* 211 */     this.$qname = $__qname;
/* 212 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/* 215 */       this.$_ngcc_current_state = 0;
/* 216 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 218 */       break;
/*     */     case 0:
/* 221 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 223 */       break;
/*     */     case 2:
/* 226 */       if (($__uri.equals("")) && ($__local.equals("minOccurs"))) {
/* 227 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 230 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 233 */       break;
/*     */     case 5:
/* 236 */       this.$_ngcc_current_state = 1;
/* 237 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 239 */       break;
/*     */     case 6:
/* 242 */       if (($__uri.equals("")) && ($__local.equals("maxOccurs"))) {
/* 243 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 246 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 249 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     default:
/* 252 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 260 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 1:
/*     */       int $ai;
/* 263 */       if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 264 */         this.$runtime.consumeAttribute($ai);
/* 265 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 268 */         this.$_ngcc_current_state = 0;
/* 269 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 272 */       break;
/*     */     case 0:
/* 275 */       revertToParentFromText(this, this._cookie, $value);
/*     */ 
/* 277 */       break;
/*     */     case 3:
/* 280 */       this.v = $value;
/* 281 */       this.$_ngcc_current_state = 2;
/* 282 */       action0();
/*     */ 
/* 284 */       break;
/*     */     case 5:
/*     */       int $ai;
/* 287 */       if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 288 */         this.$runtime.consumeAttribute($ai);
/* 289 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 292 */         this.$_ngcc_current_state = 1;
/* 293 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 296 */       break;
/*     */     case 7:
/* 299 */       if ($value.equals("unbounded")) {
/* 300 */         this.$_ngcc_current_state = 6;
/* 301 */         action1();
/*     */       }
/*     */       else {
/* 304 */         this.v = $value;
/* 305 */         this.$_ngcc_current_state = 6;
/* 306 */         action2();
/*     */       }
/*     */       break;
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */   }
/*     */ 
/*     */   public boolean accepted() {
/* 319 */     return (this.$_ngcc_current_state == 5) || (this.$_ngcc_current_state == 0) || (this.$_ngcc_current_state == 1);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.occurs
 * JD-Core Version:    0.6.2
 */