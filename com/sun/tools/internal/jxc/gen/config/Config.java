/*     */ package com.sun.tools.internal.jxc.gen.config;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.NGCCRuntimeEx;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Config extends NGCCHandler
/*     */ {
/*     */   private String bd;
/*     */   private Schema _schema;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private File baseDir;
/*     */   private Classes classes;
/* 331 */   private List schema = new ArrayList();
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  54 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public Config(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.$_ngcc_current_state = 8;
/*     */   }
/*     */ 
/*     */   public Config(NGCCRuntimeEx runtime) {
/*  64 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  68 */     this.schema.add(this._schema);
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException {
/*  72 */     this.baseDir = this.$runtime.getBaseDir(this.bd);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  77 */     this.$uri = $__uri;
/*  78 */     this.$localName = $__local;
/*  79 */     this.$qname = $__qname;
/*  80 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  83 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/*  85 */       break;
/*     */     case 1:
/*  88 */       if (($__uri.equals("")) && ($__local.equals("schema"))) {
/*  89 */         NGCCHandler h = new Schema(this, this._source, this.$runtime, 19, this.baseDir);
/*  90 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/*  93 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/*  96 */       break;
/*     */     case 8:
/*  99 */       if (($__uri.equals("")) && ($__local.equals("config"))) {
/* 100 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 101 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 104 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 107 */       break;
/*     */     case 4:
/* 110 */       if (($__uri.equals("")) && ($__local.equals("classes"))) {
/* 111 */         NGCCHandler h = new Classes(this, this._source, this.$runtime, 22);
/* 112 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 115 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 118 */       break;
/*     */     case 2:
/* 121 */       if (($__uri.equals("")) && ($__local.equals("schema"))) {
/* 122 */         NGCCHandler h = new Schema(this, this._source, this.$runtime, 20, this.baseDir);
/* 123 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 126 */         this.$_ngcc_current_state = 1;
/* 127 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 130 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 133 */       if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) {
/* 134 */         this.$runtime.consumeAttribute($ai);
/* 135 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 138 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 141 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     default:
/* 144 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 152 */     this.$uri = $__uri;
/* 153 */     this.$localName = $__local;
/* 154 */     this.$qname = $__qname;
/* 155 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 158 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 160 */       break;
/*     */     case 1:
/* 163 */       if (($__uri.equals("")) && ($__local.equals("config"))) {
/* 164 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 165 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 168 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 171 */       break;
/*     */     case 2:
/* 174 */       this.$_ngcc_current_state = 1;
/* 175 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 177 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 180 */       if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) {
/* 181 */         this.$runtime.consumeAttribute($ai);
/* 182 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 185 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 188 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     default:
/* 191 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 199 */     this.$uri = $__uri;
/* 200 */     this.$localName = $__local;
/* 201 */     this.$qname = $__qname;
/* 202 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 205 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 207 */       break;
/*     */     case 2:
/* 210 */       this.$_ngcc_current_state = 1;
/* 211 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 213 */       break;
/*     */     case 7:
/* 216 */       if (($__uri.equals("")) && ($__local.equals("baseDir"))) {
/* 217 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 220 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 223 */       break;
/*     */     default:
/* 226 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 234 */     this.$uri = $__uri;
/* 235 */     this.$localName = $__local;
/* 236 */     this.$qname = $__qname;
/* 237 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 240 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 242 */       break;
/*     */     case 5:
/* 245 */       if (($__uri.equals("")) && ($__local.equals("baseDir"))) {
/* 246 */         this.$_ngcc_current_state = 4;
/*     */       }
/*     */       else {
/* 249 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 252 */       break;
/*     */     case 2:
/* 255 */       this.$_ngcc_current_state = 1;
/* 256 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 258 */       break;
/*     */     default:
/* 261 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 269 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 272 */       revertToParentFromText(this, this._cookie, $value);
/*     */ 
/* 274 */       break;
/*     */     case 6:
/* 277 */       this.bd = $value;
/* 278 */       this.$_ngcc_current_state = 5;
/* 279 */       action1();
/*     */ 
/* 281 */       break;
/*     */     case 2:
/* 284 */       this.$_ngcc_current_state = 1;
/* 285 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 287 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 290 */       if (($ai = this.$runtime.getAttributeIndex("", "baseDir")) >= 0) {
/* 291 */         this.$runtime.consumeAttribute($ai);
/* 292 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     }
/*     */   }
/* 300 */   public void onChildCompleted(Object result, int cookie, boolean needAttCheck) throws SAXException { switch (cookie)
/*     */     {
/*     */     case 19:
/* 303 */       this._schema = ((Schema)result);
/* 304 */       action0();
/* 305 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 307 */       break;
/*     */     case 22:
/* 310 */       this.classes = ((Classes)result);
/* 311 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 313 */       break;
/*     */     case 20:
/* 316 */       this._schema = ((Schema)result);
/* 317 */       action0();
/* 318 */       this.$_ngcc_current_state = 1;
/*     */     case 21:
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 325 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   public Classes getClasses()
/*     */   {
/* 332 */     return this.classes; } 
/* 333 */   public File getBaseDir() { return this.baseDir; } 
/* 334 */   public List getSchema() { return this.schema; }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.Config
 * JD-Core Version:    0.6.2
 */