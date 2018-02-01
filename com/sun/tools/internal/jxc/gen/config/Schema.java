/*     */ package com.sun.tools.internal.jxc.gen.config;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.NGCCRuntimeEx;
/*     */ import java.io.File;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Schema extends NGCCHandler
/*     */ {
/*     */   private File baseDir;
/*     */   private String loc;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private File location;
/*     */   private String namespace;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  50 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public Schema(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, File _baseDir) {
/*  54 */     super(source, parent, cookie);
/*  55 */     this.$runtime = runtime;
/*  56 */     this.baseDir = _baseDir;
/*  57 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */ 
/*     */   public Schema(NGCCRuntimeEx runtime, File _baseDir) {
/*  61 */     this(null, runtime, runtime, -1, _baseDir);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  65 */     this.location = new File(this.baseDir, this.loc);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  70 */     this.$uri = $__uri;
/*  71 */     this.$localName = $__local;
/*  72 */     this.$qname = $__qname;
/*  73 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  76 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/*  78 */       break;
/*     */     case 2:
/*     */       int $ai;
/*  81 */       if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/*  82 */         this.$runtime.consumeAttribute($ai);
/*  83 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  86 */         this.$_ngcc_current_state = 1;
/*  87 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/*  90 */       break;
/*     */     case 6:
/*     */       int $ai;
/*  93 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  94 */         this.$runtime.consumeAttribute($ai);
/*  95 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  98 */         this.$_ngcc_current_state = 2;
/*  99 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 102 */       break;
/*     */     case 10:
/* 105 */       if (($__uri.equals("")) && ($__local.equals("schema"))) {
/* 106 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 107 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 110 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 113 */       break;
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
/*     */     case 1:
/* 130 */       if (($__uri.equals("")) && ($__local.equals("schema"))) {
/* 131 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 132 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 135 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 138 */       break;
/*     */     case 0:
/* 141 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 143 */       break;
/*     */     case 2:
/*     */       int $ai;
/* 146 */       if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/* 147 */         this.$runtime.consumeAttribute($ai);
/* 148 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 151 */         this.$_ngcc_current_state = 1;
/* 152 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 155 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 158 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 159 */         this.$runtime.consumeAttribute($ai);
/* 160 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 163 */         this.$_ngcc_current_state = 2;
/* 164 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 167 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     default:
/* 170 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 178 */     this.$uri = $__uri;
/* 179 */     this.$localName = $__local;
/* 180 */     this.$qname = $__qname;
/* 181 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 184 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 186 */       break;
/*     */     case 2:
/* 189 */       if (($__uri.equals("")) && ($__local.equals("location"))) {
/* 190 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 193 */         this.$_ngcc_current_state = 1;
/* 194 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 197 */       break;
/*     */     case 6:
/* 200 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 201 */         this.$_ngcc_current_state = 8;
/*     */       }
/*     */       else {
/* 204 */         this.$_ngcc_current_state = 2;
/* 205 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 208 */       break;
/*     */     default:
/* 211 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 219 */     this.$uri = $__uri;
/* 220 */     this.$localName = $__local;
/* 221 */     this.$qname = $__qname;
/* 222 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 225 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 227 */       break;
/*     */     case 3:
/* 230 */       if (($__uri.equals("")) && ($__local.equals("location"))) {
/* 231 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 234 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 237 */       break;
/*     */     case 7:
/* 240 */       if (($__uri.equals("")) && ($__local.equals("namespace"))) {
/* 241 */         this.$_ngcc_current_state = 2;
/*     */       }
/*     */       else {
/* 244 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 247 */       break;
/*     */     case 2:
/* 250 */       this.$_ngcc_current_state = 1;
/* 251 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 253 */       break;
/*     */     case 6:
/* 256 */       this.$_ngcc_current_state = 2;
/* 257 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 259 */       break;
/*     */     case 1:
/*     */     case 4:
/*     */     case 5:
/*     */     default:
/* 262 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 270 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 8:
/* 273 */       this.namespace = $value;
/* 274 */       this.$_ngcc_current_state = 7;
/*     */ 
/* 276 */       break;
/*     */     case 4:
/* 279 */       this.loc = $value;
/* 280 */       this.$_ngcc_current_state = 3;
/* 281 */       action0();
/*     */ 
/* 283 */       break;
/*     */     case 0:
/* 286 */       revertToParentFromText(this, this._cookie, $value);
/*     */ 
/* 288 */       break;
/*     */     case 2:
/*     */       int $ai;
/* 291 */       if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/* 292 */         this.$runtime.consumeAttribute($ai);
/* 293 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 296 */         this.$_ngcc_current_state = 1;
/* 297 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 300 */       break;
/*     */     case 6:
/*     */       int $ai;
/* 303 */       if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 304 */         this.$runtime.consumeAttribute($ai);
/* 305 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 308 */         this.$_ngcc_current_state = 2;
/* 309 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object result, int cookie, boolean needAttCheck) throws SAXException {
/*     */   }
/*     */ 
/*     */   public boolean accepted() {
/* 322 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 328 */     return this.namespace; } 
/* 329 */   public File getLocation() { return this.location; }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.Schema
 * JD-Core Version:    0.6.2
 */