/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ListSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class SimpleType_List extends NGCCHandler
/*     */ {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName itemTypeName;
/*     */   private Set finalSet;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ListSimpleTypeImpl result;
/*     */   private Ref.SimpleType itemType;
/*     */   private Locator lloc;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  59 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public SimpleType_List(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  63 */     super(source, parent, cookie);
/*  64 */     this.$runtime = runtime;
/*  65 */     this.annotation = _annotation;
/*  66 */     this.locator = _locator;
/*  67 */     this.fa = _fa;
/*  68 */     this.name = _name;
/*  69 */     this.finalSet = _finalSet;
/*  70 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */ 
/*     */   public SimpleType_List(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  74 */     this(null, runtime, runtime, -1, _annotation, _locator, _fa, _name, _finalSet);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  79 */     this.result = new ListSimpleTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, this.name == null, this.finalSet, this.itemType);
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  87 */     this.itemType = new DelayedRef.SimpleType(this.$runtime, this.lloc, this.$runtime.currentSchema, this.itemTypeName);
/*     */   }
/*     */ 
/*     */   private void action2()
/*     */     throws SAXException
/*     */   {
/*  93 */     this.lloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  98 */     this.$uri = $__uri;
/*  99 */     this.$localName = $__local;
/* 100 */     this.$qname = $__qname;
/* 101 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 9:
/*     */       int $ai;
/* 104 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || ((($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType")))) {
/* 105 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 266, this.fa);
/* 106 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else
/*     */       {
/*     */         int $ai;
/* 109 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 112 */       break;
/*     */     case 7:
/* 115 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 116 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 264, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/* 117 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 120 */         this.$_ngcc_current_state = 2;
/* 121 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 124 */       break;
/*     */     case 10:
/* 127 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("list"))) {
/* 128 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 129 */         action2();
/* 130 */         this.$_ngcc_current_state = 9;
/*     */       }
/*     */       else {
/* 133 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 136 */       break;
/*     */     case 2:
/*     */       int $ai;
/* 139 */       if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 140 */         this.$runtime.consumeAttribute($ai);
/* 141 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/* 144 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/* 145 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 258);
/* 146 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 149 */         unexpectedEnterElement($__qname);
/*     */       }
/*     */ 
/* 153 */       break;
/*     */     case 0:
/* 156 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 158 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     default:
/* 161 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 169 */     this.$uri = $__uri;
/* 170 */     this.$localName = $__local;
/* 171 */     this.$qname = $__qname;
/* 172 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 9:
/*     */       int $ai;
/* 175 */       if ((($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("list"))) {
/* 176 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 266, this.fa);
/* 177 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 180 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 183 */       break;
/*     */     case 7:
/* 186 */       this.$_ngcc_current_state = 2;
/* 187 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 189 */       break;
/*     */     case 2:
/*     */       int $ai;
/* 192 */       if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 193 */         this.$runtime.consumeAttribute($ai);
/* 194 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 197 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 200 */       break;
/*     */     case 0:
/* 203 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 205 */       break;
/*     */     case 1:
/* 208 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("list"))) {
/* 209 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 210 */         this.$_ngcc_current_state = 0;
/* 211 */         action0();
/*     */       }
/*     */       else {
/* 214 */         unexpectedLeaveElement($__qname);
/*     */       }
/*     */ 
/* 217 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     default:
/* 220 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 228 */     this.$uri = $__uri;
/* 229 */     this.$localName = $__local;
/* 230 */     this.$qname = $__qname;
/* 231 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 9:
/* 234 */       if (($__uri.equals("")) && ($__local.equals("itemType"))) {
/* 235 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 266, this.fa);
/* 236 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */       }
/*     */       else {
/* 239 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 242 */       break;
/*     */     case 7:
/* 245 */       this.$_ngcc_current_state = 2;
/* 246 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 248 */       break;
/*     */     case 2:
/* 251 */       if (($__uri.equals("")) && ($__local.equals("itemType"))) {
/* 252 */         this.$_ngcc_current_state = 5;
/*     */       }
/*     */       else {
/* 255 */         unexpectedEnterAttribute($__qname);
/*     */       }
/*     */ 
/* 258 */       break;
/*     */     case 0:
/* 261 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 263 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 8:
/*     */     default:
/* 266 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 274 */     this.$uri = $__uri;
/* 275 */     this.$localName = $__local;
/* 276 */     this.$qname = $__qname;
/* 277 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 7:
/* 280 */       this.$_ngcc_current_state = 2;
/* 281 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 283 */       break;
/*     */     case 0:
/* 286 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 288 */       break;
/*     */     case 4:
/* 291 */       if (($__uri.equals("")) && ($__local.equals("itemType"))) {
/* 292 */         this.$_ngcc_current_state = 1;
/*     */       }
/*     */       else {
/* 295 */         unexpectedLeaveAttribute($__qname);
/*     */       }
/*     */ 
/* 298 */       break;
/*     */     default:
/* 301 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 309 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 9:
/*     */       int $ai;
/* 312 */       if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 313 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 266, this.fa);
/* 314 */         spawnChildFromText(h, $value);
/* 315 */       }break;
/*     */     case 7:
/* 320 */       this.$_ngcc_current_state = 2;
/* 321 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 323 */       break;
/*     */     case 2:
/*     */       int $ai;
/* 326 */       if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 327 */         this.$runtime.consumeAttribute($ai);
/* 328 */         this.$runtime.sendText(this._cookie, $value); } break;
/*     */     case 0:
/* 334 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 336 */       break;
/*     */     case 5:
/* 339 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 260);
/* 340 */       spawnChildFromText(h, $value);
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 8: } 
/*     */   }
/* 347 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*     */     {
/*     */     case 266:
/* 350 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 351 */       this.$_ngcc_current_state = 7;
/*     */ 
/* 353 */       break;
/*     */     case 264:
/* 356 */       this.annotation = ((AnnotationImpl)$__result__);
/* 357 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 359 */       break;
/*     */     case 258:
/* 362 */       this.itemType = ((SimpleTypeImpl)$__result__);
/* 363 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 365 */       break;
/*     */     case 260:
/* 368 */       this.itemTypeName = ((UName)$__result__);
/* 369 */       action1();
/* 370 */       this.$_ngcc_current_state = 4;
/*     */     case 259:
/*     */     case 261:
/*     */     case 262:
/*     */     case 263:
/*     */     case 265:
/*     */     } } 
/* 377 */   public boolean accepted() { return this.$_ngcc_current_state == 0; }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.SimpleType_List
 * JD-Core Version:    0.6.2
 */