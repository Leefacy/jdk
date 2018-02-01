/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.UnionSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class SimpleType_Union extends NGCCHandler
/*     */ {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String __text;
/*     */   private UName memberTypeName;
/*     */   private String name;
/*     */   private Set finalSet;
/*     */   private ForeignAttributesImpl fa;
/*     */   private SimpleTypeImpl anonymousMemberType;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private UnionSimpleTypeImpl result;
/* 460 */   private final Vector members = new Vector();
/*     */   private Locator uloc;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  62 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public SimpleType_Union(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  66 */     super(source, parent, cookie);
/*  67 */     this.$runtime = runtime;
/*  68 */     this.annotation = _annotation;
/*  69 */     this.locator = _locator;
/*  70 */     this.fa = _fa;
/*  71 */     this.name = _name;
/*  72 */     this.finalSet = _finalSet;
/*  73 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */ 
/*     */   public SimpleType_Union(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  77 */     this(null, runtime, runtime, -1, _annotation, _locator, _fa, _name, _finalSet);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  82 */     this.result = new UnionSimpleTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, this.name == null, this.finalSet, 
/*  84 */       (Ref.SimpleType[])this.members
/*  84 */       .toArray(new Ref.SimpleType[this.members
/*  84 */       .size()]));
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  90 */     this.members.add(this.anonymousMemberType);
/*     */   }
/*     */ 
/*     */   private void action2()
/*     */     throws SAXException
/*     */   {
/*  96 */     this.members.add(new DelayedRef.SimpleType(this.$runtime, this.uloc, this.$runtime.currentSchema, this.memberTypeName));
/*     */   }
/*     */ 
/*     */   private void action3()
/*     */     throws SAXException
/*     */   {
/* 102 */     this.$runtime.processList(this.__text);
/*     */   }
/*     */   private void action4() throws SAXException {
/* 105 */     this.uloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/* 110 */     this.$uri = $__uri;
/* 111 */     this.$localName = $__local;
/* 112 */     this.$qname = $__qname;
/* 113 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 116 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 117 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 183, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/* 118 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 121 */         this.$_ngcc_current_state = 2;
/* 122 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 125 */       break;
/*     */     case 0:
/* 128 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 130 */       break;
/*     */     case 1:
/* 133 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 134 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 179);
/* 135 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 138 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 141 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 144 */       if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/* 145 */         this.$runtime.consumeAttribute($ai);
/* 146 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 149 */         this.$_ngcc_current_state = 6;
/* 150 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 153 */       break;
/*     */     case 12:
/* 156 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("union"))) {
/* 157 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 158 */         action4();
/* 159 */         this.$_ngcc_current_state = 7;
/*     */       }
/*     */       else {
/* 162 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 165 */       break;
/*     */     case 2:
/* 168 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 169 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 180);
/* 170 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 173 */         this.$_ngcc_current_state = 1;
/* 174 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 177 */       break;
/*     */     case 6:
/* 180 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType")))) {
/* 181 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 185, this.fa);
/* 182 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 185 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 188 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     default:
/* 191 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 199 */     this.$uri = $__uri;
/* 200 */     this.$localName = $__local;
/* 201 */     this.$qname = $__qname;
/* 202 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 205 */       this.$_ngcc_current_state = 2;
/* 206 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 208 */       break;
/*     */     case 0:
/* 211 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 213 */       break;
/*     */     case 1:
/* 216 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("union"))) {
/* 217 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 218 */         this.$_ngcc_current_state = 0;
/* 219 */         action0();
/*     */       }
/*     */       else {
/* 222 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 225 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 228 */       if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/* 229 */         this.$runtime.consumeAttribute($ai);
/* 230 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 233 */         this.$_ngcc_current_state = 6;
/* 234 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 237 */       break;
/*     */     case 2:
/* 240 */       this.$_ngcc_current_state = 1;
/* 241 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 243 */       break;
/*     */     case 6:
/* 246 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("union"))) {
/* 247 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 185, this.fa);
/* 248 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 251 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 254 */       break;
/*     */     case 3:
/*     */     case 5:
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
/*     */     case 4:
/* 271 */       this.$_ngcc_current_state = 2;
/* 272 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 274 */       break;
/*     */     case 0:
/* 277 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 279 */       break;
/*     */     case 7:
/* 282 */       if (($__uri.equals("")) && ($__local.equals("memberTypes"))) {
/* 283 */         this.$_ngcc_current_state = 10;
/*     */       }
/*     */       else {
/* 286 */         this.$_ngcc_current_state = 6;
/* 287 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */ 
/* 290 */       break;
/*     */     case 2:
/* 293 */       this.$_ngcc_current_state = 1;
/* 294 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 296 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     default:
/* 299 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 307 */     this.$uri = $__uri;
/* 308 */     this.$localName = $__local;
/* 309 */     this.$qname = $__qname;
/* 310 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 313 */       this.$_ngcc_current_state = 2;
/* 314 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 316 */       break;
/*     */     case 0:
/* 319 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 321 */       break;
/*     */     case 7:
/* 324 */       this.$_ngcc_current_state = 6;
/* 325 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 327 */       break;
/*     */     case 8:
/* 330 */       if (($__uri.equals("")) && ($__local.equals("memberTypes"))) {
/* 331 */         this.$_ngcc_current_state = 6;
/*     */       }
/*     */       else {
/* 334 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 337 */       break;
/*     */     case 2:
/* 340 */       this.$_ngcc_current_state = 1;
/* 341 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 343 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     default:
/* 346 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 354 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 4:
/* 357 */       this.$_ngcc_current_state = 2;
/* 358 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 360 */       break;
/*     */     case 9:
/* 363 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 187);
/* 364 */       spawnChildFromText(h, $value);
/*     */ 
/* 366 */       break;
/*     */     case 10:
/* 369 */       this.__text = $value;
/* 370 */       this.$_ngcc_current_state = 9;
/* 371 */       action3();
/*     */ 
/* 373 */       break;
/*     */     case 0:
/* 376 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 378 */       break;
/*     */     case 7:
/*     */       int $ai;
/* 381 */       if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/* 382 */         this.$runtime.consumeAttribute($ai);
/* 383 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */       else {
/* 386 */         this.$_ngcc_current_state = 6;
/* 387 */         this.$runtime.sendText(this._cookie, $value);
/*     */       }
/*     */ 
/* 390 */       break;
/*     */     case 8:
/* 393 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 188);
/* 394 */       spawnChildFromText(h, $value);
/*     */ 
/* 396 */       break;
/*     */     case 2:
/* 399 */       this.$_ngcc_current_state = 1;
/* 400 */       this.$runtime.sendText(this._cookie, $value);
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     }
/*     */   }
/* 407 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 183:
/* 410 */       this.annotation = ((AnnotationImpl)$__result__);
/* 411 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 413 */       break;
/*     */     case 187:
/* 416 */       this.memberTypeName = ((UName)$__result__);
/* 417 */       action2();
/* 418 */       this.$_ngcc_current_state = 8;
/*     */ 
/* 420 */       break;
/*     */     case 179:
/* 423 */       this.anonymousMemberType = ((SimpleTypeImpl)$__result__);
/* 424 */       action1();
/* 425 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 427 */       break;
/*     */     case 188:
/* 430 */       this.memberTypeName = ((UName)$__result__);
/* 431 */       action2();
/* 432 */       this.$_ngcc_current_state = 8;
/*     */ 
/* 434 */       break;
/*     */     case 185:
/* 437 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 438 */       this.$_ngcc_current_state = 4;
/*     */ 
/* 440 */       break;
/*     */     case 180:
/* 443 */       this.anonymousMemberType = ((SimpleTypeImpl)$__result__);
/* 444 */       action1();
/* 445 */       this.$_ngcc_current_state = 1;
/*     */     case 181:
/*     */     case 182:
/*     */     case 184:
/*     */     case 186:
/*     */     } } 
/*     */   public boolean accepted() {
/* 452 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.SimpleType_Union
 * JD-Core Version:    0.6.2
 */