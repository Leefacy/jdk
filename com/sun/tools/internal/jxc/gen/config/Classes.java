/*     */ package com.sun.tools.internal.jxc.gen.config;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.NGCCRuntimeEx;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Classes extends NGCCHandler
/*     */ {
/*     */   private String __text;
/*     */   private String exclude_content;
/*     */   private String include_content;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/* 333 */   private List includes = new ArrayList();
/*     */ 
/* 335 */   private List excludes = new ArrayList();
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  52 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public Classes(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  56 */     super(source, parent, cookie);
/*  57 */     this.$runtime = runtime;
/*  58 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */ 
/*     */   public Classes(NGCCRuntimeEx runtime) {
/*  62 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException {
/*  66 */     this.excludes.add(this.exclude_content);
/*     */   }
/*     */ 
/*     */   private void action1() throws SAXException {
/*  70 */     this.$runtime.processList(this.__text);
/*     */   }
/*     */   private void action2() throws SAXException {
/*  73 */     this.includes.add(this.include_content);
/*     */   }
/*     */ 
/*     */   private void action3() throws SAXException {
/*  77 */     this.$runtime.processList(this.__text);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  81 */     this.$uri = $__uri;
/*  82 */     this.$localName = $__local;
/*  83 */     this.$qname = $__qname;
/*  84 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 12:
/*  87 */       if (($__uri.equals("")) && ($__local.equals("classes"))) {
/*  88 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  89 */         this.$_ngcc_current_state = 11;
/*     */       }
/*     */       else {
/*  92 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/*  95 */       break;
/*     */     case 2:
/*  98 */       if (($__uri.equals("")) && ($__local.equals("excludes"))) {
/*  99 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 100 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 103 */         this.$_ngcc_current_state = 1;
/* 104 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 107 */       break;
/*     */     case 4:
/* 110 */       this.$_ngcc_current_state = 3;
/* 111 */       this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 113 */       break;
/*     */     case 11:
/* 116 */       if (($__uri.equals("")) && ($__local.equals("includes"))) {
/* 117 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 118 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 121 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 124 */       break;
/*     */     case 0:
/* 127 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 129 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/* 132 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 140 */     this.$uri = $__uri;
/* 141 */     this.$localName = $__local;
/* 142 */     this.$qname = $__qname;
/* 143 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 146 */       this.$_ngcc_current_state = 1;
/* 147 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 149 */       break;
/*     */     case 4:
/* 152 */       this.$_ngcc_current_state = 3;
/* 153 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 155 */       break;
/*     */     case 1:
/* 158 */       if (($__uri.equals("")) && ($__local.equals("classes"))) {
/* 159 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 160 */         this.$_ngcc_current_state = 0;
/*     */       }
/*     */       else {
/* 163 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 166 */       break;
/*     */     case 3:
/* 169 */       if (($__uri.equals("")) && ($__local.equals("excludes"))) {
/* 170 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 171 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 174 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 177 */       break;
/*     */     case 0:
/* 180 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 182 */       break;
/*     */     case 8:
/* 185 */       if (($__uri.equals("")) && ($__local.equals("includes"))) {
/* 186 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 187 */         this.$_ngcc_current_state = 2;
/*     */       }
/*     */       else {
/* 190 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 193 */       break;
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
/*     */     case 2:
/* 210 */       this.$_ngcc_current_state = 1;
/* 211 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 213 */       break;
/*     */     case 4:
/* 216 */       this.$_ngcc_current_state = 3;
/* 217 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 219 */       break;
/*     */     case 0:
/* 222 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 224 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     default:
/* 227 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 235 */     this.$uri = $__uri;
/* 236 */     this.$localName = $__local;
/* 237 */     this.$qname = $__qname;
/* 238 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 241 */       this.$_ngcc_current_state = 1;
/* 242 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 244 */       break;
/*     */     case 4:
/* 247 */       this.$_ngcc_current_state = 3;
/* 248 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 250 */       break;
/*     */     case 0:
/* 253 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 255 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     default:
/* 258 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 266 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 2:
/* 269 */       this.$_ngcc_current_state = 1;
/* 270 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 272 */       break;
/*     */     case 4:
/* 275 */       this.exclude_content = $value;
/* 276 */       this.$_ngcc_current_state = 3;
/* 277 */       action0();
/*     */ 
/* 279 */       break;
/*     */     case 10:
/* 282 */       this.__text = $value;
/* 283 */       this.$_ngcc_current_state = 9;
/* 284 */       action3();
/*     */ 
/* 286 */       break;
/*     */     case 3:
/* 289 */       this.exclude_content = $value;
/* 290 */       this.$_ngcc_current_state = 3;
/* 291 */       action0();
/*     */ 
/* 293 */       break;
/*     */     case 6:
/* 296 */       this.__text = $value;
/* 297 */       this.$_ngcc_current_state = 4;
/* 298 */       action1();
/*     */ 
/* 300 */       break;
/*     */     case 0:
/* 303 */       revertToParentFromText(this, this._cookie, $value);
/*     */ 
/* 305 */       break;
/*     */     case 9:
/* 308 */       this.include_content = $value;
/* 309 */       this.$_ngcc_current_state = 8;
/* 310 */       action2();
/*     */ 
/* 312 */       break;
/*     */     case 8:
/* 315 */       this.include_content = $value;
/* 316 */       this.$_ngcc_current_state = 8;
/* 317 */       action2();
/*     */     case 1:
/*     */     case 5:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object result, int cookie, boolean needAttCheck) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean accepted() {
/* 329 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   public List getIncludes()
/*     */   {
/* 334 */     return this.$runtime.getIncludePatterns(this.includes);
/*     */   }
/* 336 */   public List getExcludes() { return this.$runtime.getExcludePatterns(this.excludes); }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.Classes
 * JD-Core Version:    0.6.2
 */