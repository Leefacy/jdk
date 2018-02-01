/*      */ package com.sun.xml.internal.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ElementDecl;
/*      */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.internal.xsom.impl.IdentityConstraintImpl;
/*      */ import com.sun.xml.internal.xsom.impl.Ref.Type;
/*      */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.UName;
/*      */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.Element;
/*      */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.Type;
/*      */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.internal.xsom.impl.parser.ParserContext;
/*      */ import com.sun.xml.internal.xsom.impl.parser.SubstGroupBaseTypeRef;
/*      */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ class elementDeclBody extends NGCCHandler
/*      */ {
/*      */   private Integer finalValue;
/*      */   private String name;
/*      */   private String nillable;
/*      */   private String abstractValue;
/*      */   private Integer blockValue;
/*      */   private ForeignAttributesImpl fa;
/*      */   private AnnotationImpl annotation;
/*      */   private Locator locator;
/*      */   private String defaultValue;
/*      */   private IdentityConstraintImpl idc;
/*      */   private boolean isGlobal;
/*      */   private String fixedValue;
/*      */   private UName typeName;
/*      */   private UName substRef;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   private int $_ngcc_current_state;
/*      */   protected String $uri;
/*      */   protected String $localName;
/*      */   protected String $qname;
/*      */   private boolean form;
/*      */   private boolean formSpecified;
/*      */   private Ref.Type type;
/* 1127 */   private List idcs = new ArrayList();
/*      */   private DelayedRef.Element substHeadRef;
/*      */ 
/*      */   public final NGCCRuntime getRuntime()
/*      */   {
/*   67 */     return this.$runtime;
/*      */   }
/*      */ 
/*      */   public elementDeclBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, boolean _isGlobal) {
/*   71 */     super(source, parent, cookie);
/*   72 */     this.$runtime = runtime;
/*   73 */     this.locator = _locator;
/*   74 */     this.isGlobal = _isGlobal;
/*   75 */     this.$_ngcc_current_state = 48;
/*      */   }
/*      */ 
/*      */   public elementDeclBody(NGCCRuntimeEx runtime, Locator _locator, boolean _isGlobal) {
/*   79 */     this(null, runtime, runtime, -1, _locator, _isGlobal);
/*      */   }
/*      */ 
/*      */   private void action0() throws SAXException {
/*   83 */     this.idcs.add(this.idc);
/*      */   }
/*      */ 
/*      */   private void action1() throws SAXException
/*      */   {
/*   88 */     this.type = new DelayedRef.Type(this.$runtime, this.locator, this.$runtime.currentSchema, this.typeName);
/*      */   }
/*      */ 
/*      */   private void action2()
/*      */     throws SAXException
/*      */   {
/*   95 */     this.substHeadRef = new DelayedRef.Element(this.$runtime, this.locator, this.$runtime.currentSchema, this.substRef);
/*      */   }
/*      */ 
/*      */   private void action3()
/*      */     throws SAXException
/*      */   {
/*  101 */     this.formSpecified = true;
/*      */   }
/*      */ 
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*      */   {
/*  106 */     this.$uri = $__uri;
/*  107 */     this.$localName = $__local;
/*  108 */     this.$qname = $__qname;
/*  109 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 17:
/*      */       int $ai;
/*  112 */       if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  113 */         this.$runtime.consumeAttribute($ai);
/*  114 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  117 */         this.$_ngcc_current_state = 13;
/*  118 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  121 */       break;
/*      */     case 28:
/*      */       int $ai;
/*  124 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  125 */         this.$runtime.consumeAttribute($ai);
/*  126 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  129 */         this.$_ngcc_current_state = 24;
/*  130 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  133 */       break;
/*      */     case 0:
/*  136 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique")))) {
/*  137 */         NGCCHandler h = new identityConstraint(this, this._source, this.$runtime, 6);
/*  138 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  141 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  144 */       break;
/*      */     case 32:
/*      */       int $ai;
/*  147 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  148 */         this.$runtime.consumeAttribute($ai);
/*  149 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  152 */         this.$_ngcc_current_state = 28;
/*  153 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  156 */       break;
/*      */     case 24:
/*      */       int $ai;
/*  159 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  160 */         this.$runtime.consumeAttribute($ai);
/*  161 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  164 */         this.$_ngcc_current_state = 23;
/*  165 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  168 */       break;
/*      */     case 11:
/*  171 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  172 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 24, null, AnnotationContext.ELEMENT_DECL);
/*  173 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  176 */         this.$_ngcc_current_state = 3;
/*  177 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  180 */       break;
/*      */     case 23:
/*      */       int $ai;
/*  183 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  184 */         this.$runtime.consumeAttribute($ai);
/*  185 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  188 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  191 */       break;
/*      */     case 44:
/*      */       int $ai;
/*  194 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  195 */         this.$runtime.consumeAttribute($ai);
/*  196 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  199 */         this.$_ngcc_current_state = 40;
/*  200 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  203 */       break;
/*      */     case 40:
/*      */       int $ai;
/*  206 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  207 */         this.$runtime.consumeAttribute($ai);
/*  208 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  211 */         this.$_ngcc_current_state = 36;
/*  212 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  215 */       break;
/*      */     case 48:
/*      */       int $ai;
/*  218 */       if (((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))))) || ((($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) && ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation")))))) {
/*  219 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  220 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  223 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  226 */       break;
/*      */     case 1:
/*  229 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("key"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("keyref"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("unique")))) {
/*  230 */         NGCCHandler h = new identityConstraint(this, this._source, this.$runtime, 7);
/*  231 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  234 */         this.$_ngcc_current_state = 0;
/*  235 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  238 */       break;
/*      */     case 3:
/*  241 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/*  242 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 19);
/*  243 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  246 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  247 */         NGCCHandler h = new complexType(this, this._source, this.$runtime, 20);
/*  248 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else
/*      */       {
/*      */         int $ai;
/*  251 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  252 */           this.$runtime.consumeAttribute($ai);
/*  253 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         }
/*      */         else {
/*  256 */           this.$_ngcc_current_state = 1;
/*  257 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  262 */       break;
/*      */     case 13:
/*      */       int $ai;
/*  265 */       if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  266 */         this.$runtime.consumeAttribute($ai);
/*  267 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  270 */         this.$_ngcc_current_state = 11;
/*  271 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  274 */       break;
/*      */     case 36:
/*      */       int $ai;
/*  277 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  278 */         this.$runtime.consumeAttribute($ai);
/*  279 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  282 */         this.$_ngcc_current_state = 32;
/*  283 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  286 */       break;
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     default:
/*  289 */       unexpectedEnterElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  297 */     this.$uri = $__uri;
/*  298 */     this.$localName = $__local;
/*  299 */     this.$qname = $__qname;
/*  300 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 17:
/*      */       int $ai;
/*  303 */       if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  304 */         this.$runtime.consumeAttribute($ai);
/*  305 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  308 */         this.$_ngcc_current_state = 13;
/*  309 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  312 */       break;
/*      */     case 28:
/*      */       int $ai;
/*  315 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  316 */         this.$runtime.consumeAttribute($ai);
/*  317 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  320 */         this.$_ngcc_current_state = 24;
/*  321 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  324 */       break;
/*      */     case 0:
/*  327 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  329 */       break;
/*      */     case 32:
/*      */       int $ai;
/*  332 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  333 */         this.$runtime.consumeAttribute($ai);
/*  334 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  337 */         this.$_ngcc_current_state = 28;
/*  338 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  341 */       break;
/*      */     case 24:
/*      */       int $ai;
/*  344 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  345 */         this.$runtime.consumeAttribute($ai);
/*  346 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  349 */         this.$_ngcc_current_state = 23;
/*  350 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  353 */       break;
/*      */     case 11:
/*  356 */       this.$_ngcc_current_state = 3;
/*  357 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  359 */       break;
/*      */     case 23:
/*      */       int $ai;
/*  362 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  363 */         this.$runtime.consumeAttribute($ai);
/*  364 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  367 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  370 */       break;
/*      */     case 44:
/*      */       int $ai;
/*  373 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  374 */         this.$runtime.consumeAttribute($ai);
/*  375 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  378 */         this.$_ngcc_current_state = 40;
/*  379 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  382 */       break;
/*      */     case 40:
/*      */       int $ai;
/*  385 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  386 */         this.$runtime.consumeAttribute($ai);
/*  387 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  390 */         this.$_ngcc_current_state = 36;
/*  391 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  394 */       break;
/*      */     case 48:
/*      */       int $ai;
/*  397 */       if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0)) {
/*  398 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  399 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  402 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  405 */       break;
/*      */     case 1:
/*  408 */       this.$_ngcc_current_state = 0;
/*  409 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  411 */       break;
/*      */     case 3:
/*      */       int $ai;
/*  414 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  415 */         this.$runtime.consumeAttribute($ai);
/*  416 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  419 */         this.$_ngcc_current_state = 1;
/*  420 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  423 */       break;
/*      */     case 13:
/*      */       int $ai;
/*  426 */       if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  427 */         this.$runtime.consumeAttribute($ai);
/*  428 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  431 */         this.$_ngcc_current_state = 11;
/*  432 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  435 */       break;
/*      */     case 36:
/*      */       int $ai;
/*  438 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  439 */         this.$runtime.consumeAttribute($ai);
/*  440 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  443 */         this.$_ngcc_current_state = 32;
/*  444 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  447 */       break;
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     default:
/*  450 */       unexpectedLeaveElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  458 */     this.$uri = $__uri;
/*  459 */     this.$localName = $__local;
/*  460 */     this.$qname = $__qname;
/*  461 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 17:
/*  464 */       if (($__uri.equals("")) && ($__local.equals("nillable"))) {
/*  465 */         this.$_ngcc_current_state = 19;
/*      */       }
/*      */       else {
/*  468 */         this.$_ngcc_current_state = 13;
/*  469 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  472 */       break;
/*      */     case 28:
/*  475 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/*  476 */         this.$_ngcc_current_state = 30;
/*      */       }
/*      */       else {
/*  479 */         this.$_ngcc_current_state = 24;
/*  480 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  483 */       break;
/*      */     case 0:
/*  486 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  488 */       break;
/*      */     case 32:
/*  491 */       if (($__uri.equals("")) && ($__local.equals("default"))) {
/*  492 */         this.$_ngcc_current_state = 34;
/*      */       }
/*      */       else {
/*  495 */         this.$_ngcc_current_state = 28;
/*  496 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  499 */       break;
/*      */     case 24:
/*  502 */       if (($__uri.equals("")) && ($__local.equals("form"))) {
/*  503 */         this.$_ngcc_current_state = 26;
/*      */       }
/*      */       else {
/*  506 */         this.$_ngcc_current_state = 23;
/*  507 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  510 */       break;
/*      */     case 11:
/*  513 */       this.$_ngcc_current_state = 3;
/*  514 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  516 */       break;
/*      */     case 23:
/*  519 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/*  520 */         this.$_ngcc_current_state = 22;
/*      */       }
/*      */       else {
/*  523 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/*  526 */       break;
/*      */     case 44:
/*  529 */       if (($__uri.equals("")) && ($__local.equals("abstract"))) {
/*  530 */         this.$_ngcc_current_state = 46;
/*      */       }
/*      */       else {
/*  533 */         this.$_ngcc_current_state = 40;
/*  534 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  537 */       break;
/*      */     case 40:
/*  540 */       if (($__uri.equals("")) && ($__local.equals("block"))) {
/*  541 */         this.$_ngcc_current_state = 42;
/*      */       }
/*      */       else {
/*  544 */         this.$_ngcc_current_state = 36;
/*  545 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  548 */       break;
/*      */     case 48:
/*  551 */       if ((($__uri.equals("")) && ($__local.equals("default"))) || (($__uri.equals("")) && ($__local.equals("fixed"))) || (($__uri.equals("")) && ($__local.equals("form"))) || (($__uri.equals("")) && ($__local.equals("block"))) || (($__uri.equals("")) && ($__local.equals("final"))) || (($__uri.equals("")) && ($__local.equals("name"))) || (($__uri.equals("")) && ($__local.equals("abstract")))) {
/*  552 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  553 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  556 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/*  559 */       break;
/*      */     case 1:
/*  562 */       this.$_ngcc_current_state = 0;
/*  563 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  565 */       break;
/*      */     case 3:
/*  568 */       if (($__uri.equals("")) && ($__local.equals("type"))) {
/*  569 */         this.$_ngcc_current_state = 6;
/*      */       }
/*      */       else {
/*  572 */         this.$_ngcc_current_state = 1;
/*  573 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  576 */       break;
/*      */     case 13:
/*  579 */       if (($__uri.equals("")) && ($__local.equals("substitutionGroup"))) {
/*  580 */         this.$_ngcc_current_state = 15;
/*      */       }
/*      */       else {
/*  583 */         this.$_ngcc_current_state = 11;
/*  584 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  587 */       break;
/*      */     case 36:
/*  590 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/*  591 */         this.$_ngcc_current_state = 38;
/*      */       }
/*      */       else {
/*  594 */         this.$_ngcc_current_state = 32;
/*  595 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  598 */       break;
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     default:
/*  601 */       unexpectedEnterAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  609 */     this.$uri = $__uri;
/*  610 */     this.$localName = $__local;
/*  611 */     this.$qname = $__qname;
/*  612 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 21:
/*  615 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/*  616 */         this.$_ngcc_current_state = 17;
/*      */       }
/*      */       else {
/*  619 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  622 */       break;
/*      */     case 17:
/*  625 */       this.$_ngcc_current_state = 13;
/*  626 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  628 */       break;
/*      */     case 18:
/*  631 */       if (($__uri.equals("")) && ($__local.equals("nillable"))) {
/*  632 */         this.$_ngcc_current_state = 13;
/*      */       }
/*      */       else {
/*  635 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  638 */       break;
/*      */     case 25:
/*  641 */       if (($__uri.equals("")) && ($__local.equals("form"))) {
/*  642 */         this.$_ngcc_current_state = 23;
/*      */       }
/*      */       else {
/*  645 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  648 */       break;
/*      */     case 41:
/*  651 */       if (($__uri.equals("")) && ($__local.equals("block"))) {
/*  652 */         this.$_ngcc_current_state = 36;
/*      */       }
/*      */       else {
/*  655 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  658 */       break;
/*      */     case 28:
/*  661 */       this.$_ngcc_current_state = 24;
/*  662 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  664 */       break;
/*      */     case 32:
/*  667 */       this.$_ngcc_current_state = 28;
/*  668 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  670 */       break;
/*      */     case 0:
/*  673 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  675 */       break;
/*      */     case 24:
/*  678 */       this.$_ngcc_current_state = 23;
/*  679 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  681 */       break;
/*      */     case 11:
/*  684 */       this.$_ngcc_current_state = 3;
/*  685 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  687 */       break;
/*      */     case 33:
/*  690 */       if (($__uri.equals("")) && ($__local.equals("default"))) {
/*  691 */         this.$_ngcc_current_state = 28;
/*      */       }
/*      */       else {
/*  694 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  697 */       break;
/*      */     case 37:
/*  700 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/*  701 */         this.$_ngcc_current_state = 32;
/*      */       }
/*      */       else {
/*  704 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  707 */       break;
/*      */     case 44:
/*  710 */       this.$_ngcc_current_state = 40;
/*  711 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  713 */       break;
/*      */     case 14:
/*  716 */       if (($__uri.equals("")) && ($__local.equals("substitutionGroup"))) {
/*  717 */         this.$_ngcc_current_state = 11;
/*      */       }
/*      */       else {
/*  720 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  723 */       break;
/*      */     case 40:
/*  726 */       this.$_ngcc_current_state = 36;
/*  727 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  729 */       break;
/*      */     case 45:
/*  732 */       if (($__uri.equals("")) && ($__local.equals("abstract"))) {
/*  733 */         this.$_ngcc_current_state = 40;
/*      */       }
/*      */       else {
/*  736 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  739 */       break;
/*      */     case 1:
/*  742 */       this.$_ngcc_current_state = 0;
/*  743 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  745 */       break;
/*      */     case 3:
/*  748 */       this.$_ngcc_current_state = 1;
/*  749 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  751 */       break;
/*      */     case 13:
/*  754 */       this.$_ngcc_current_state = 11;
/*  755 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  757 */       break;
/*      */     case 36:
/*  760 */       this.$_ngcc_current_state = 32;
/*  761 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  763 */       break;
/*      */     case 5:
/*  766 */       if (($__uri.equals("")) && ($__local.equals("type"))) {
/*  767 */         this.$_ngcc_current_state = 1;
/*  768 */         action1();
/*      */       }
/*      */       else {
/*  771 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  774 */       break;
/*      */     case 29:
/*  777 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/*  778 */         this.$_ngcc_current_state = 24;
/*      */       }
/*      */       else {
/*  781 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  784 */       break;
/*      */     case 2:
/*      */     case 4:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/*      */     case 15:
/*      */     case 16:
/*      */     case 19:
/*      */     case 20:
/*      */     case 22:
/*      */     case 23:
/*      */     case 26:
/*      */     case 27:
/*      */     case 30:
/*      */     case 31:
/*      */     case 34:
/*      */     case 35:
/*      */     case 38:
/*      */     case 39:
/*      */     case 42:
/*      */     case 43:
/*      */     default:
/*  787 */       unexpectedLeaveAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void text(String $value)
/*      */     throws SAXException
/*      */   {
/*  795 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 17:
/*      */       int $ai;
/*  798 */       if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  799 */         this.$runtime.consumeAttribute($ai);
/*  800 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  803 */         this.$_ngcc_current_state = 13;
/*  804 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  807 */       break;
/*      */     case 34:
/*  810 */       this.defaultValue = $value;
/*  811 */       this.$_ngcc_current_state = 33;
/*      */ 
/*  813 */       break;
/*      */     case 22:
/*  816 */       this.name = $value;
/*  817 */       this.$_ngcc_current_state = 21;
/*      */ 
/*  819 */       break;
/*      */     case 28:
/*      */       int $ai;
/*  822 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  823 */         this.$runtime.consumeAttribute($ai);
/*  824 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  827 */         this.$_ngcc_current_state = 24;
/*  828 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  831 */       break;
/*      */     case 32:
/*      */       int $ai;
/*  834 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  835 */         this.$runtime.consumeAttribute($ai);
/*  836 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  839 */         this.$_ngcc_current_state = 28;
/*  840 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  843 */       break;
/*      */     case 0:
/*  846 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*      */ 
/*  848 */       break;
/*      */     case 6:
/*  851 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 10);
/*  852 */       spawnChildFromText(h, $value);
/*      */ 
/*  854 */       break;
/*      */     case 24:
/*      */       int $ai;
/*  857 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  858 */         this.$runtime.consumeAttribute($ai);
/*  859 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  862 */         this.$_ngcc_current_state = 23;
/*  863 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  866 */       break;
/*      */     case 11:
/*  869 */       this.$_ngcc_current_state = 3;
/*  870 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/*  872 */       break;
/*      */     case 23:
/*      */       int $ai;
/*  875 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  876 */         this.$runtime.consumeAttribute($ai);
/*  877 */         this.$runtime.sendText(this._cookie, $value); } break;
/*      */     case 44:
/*      */       int $ai;
/*  883 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  884 */         this.$runtime.consumeAttribute($ai);
/*  885 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  888 */         this.$_ngcc_current_state = 40;
/*  889 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  892 */       break;
/*      */     case 46:
/*  895 */       this.abstractValue = $value;
/*  896 */       this.$_ngcc_current_state = 45;
/*      */ 
/*  898 */       break;
/*      */     case 19:
/*  901 */       this.nillable = $value;
/*  902 */       this.$_ngcc_current_state = 18;
/*      */ 
/*  904 */       break;
/*      */     case 40:
/*      */       int $ai;
/*  907 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  908 */         this.$runtime.consumeAttribute($ai);
/*  909 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  912 */         this.$_ngcc_current_state = 36;
/*  913 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  916 */       break;
/*      */     case 48:
/*      */       int $ai;
/*  919 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  920 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  921 */         spawnChildFromText(h, $value);
/*      */       }
/*  924 */       else if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  925 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  926 */         spawnChildFromText(h, $value);
/*      */       }
/*  929 */       else if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  930 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  931 */         spawnChildFromText(h, $value);
/*      */       }
/*  934 */       else if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  935 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  936 */         spawnChildFromText(h, $value);
/*      */       }
/*  939 */       else if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  940 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  941 */         spawnChildFromText(h, $value);
/*      */       }
/*  944 */       else if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  945 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  946 */         spawnChildFromText(h, $value);
/*      */       }
/*  949 */       else if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  950 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 69, this.fa);
/*  951 */         spawnChildFromText(h, $value);
/*  952 */       }break;
/*      */     case 30:
/*  963 */       this.fixedValue = $value;
/*  964 */       this.$_ngcc_current_state = 29;
/*      */ 
/*  966 */       break;
/*      */     case 1:
/*  969 */       this.$_ngcc_current_state = 0;
/*  970 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/*  972 */       break;
/*      */     case 3:
/*      */       int $ai;
/*  975 */       if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  976 */         this.$runtime.consumeAttribute($ai);
/*  977 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  980 */         this.$_ngcc_current_state = 1;
/*  981 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  984 */       break;
/*      */     case 13:
/*      */       int $ai;
/*  987 */       if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  988 */         this.$runtime.consumeAttribute($ai);
/*  989 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  992 */         this.$_ngcc_current_state = 11;
/*  993 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  996 */       break;
/*      */     case 38:
/*  999 */       NGCCHandler h = new erSet(this, this._source, this.$runtime, 55);
/* 1000 */       spawnChildFromText(h, $value);
/*      */ 
/* 1002 */       break;
/*      */     case 15:
/* 1005 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 27);
/* 1006 */       spawnChildFromText(h, $value);
/*      */ 
/* 1008 */       break;
/*      */     case 26:
/* 1011 */       if ($value.equals("unqualified")) {
/* 1012 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 40);
/* 1013 */         spawnChildFromText(h, $value);
/*      */       }
/* 1016 */       else if ($value.equals("qualified")) {
/* 1017 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 40);
/* 1018 */         spawnChildFromText(h, $value);
/* 1019 */       }break;
/*      */     case 36:
/*      */       int $ai;
/* 1025 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1026 */         this.$runtime.consumeAttribute($ai);
/* 1027 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1030 */         this.$_ngcc_current_state = 32;
/* 1031 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1034 */       break;
/*      */     case 42:
/* 1037 */       NGCCHandler h = new ersSet(this, this._source, this.$runtime, 60);
/* 1038 */       spawnChildFromText(h, $value);
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 12:
/*      */     case 14:
/*      */     case 16:
/*      */     case 18:
/*      */     case 20:
/*      */     case 21:
/*      */     case 25:
/*      */     case 27:
/*      */     case 29:
/*      */     case 31:
/*      */     case 33:
/*      */     case 35:
/*      */     case 37:
/*      */     case 39:
/*      */     case 41:
/*      */     case 43:
/*      */     case 45:
/*      */     case 47: }  } 
/* 1045 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*      */     {
/*      */     case 24:
/* 1048 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1049 */       this.$_ngcc_current_state = 3;
/*      */ 
/* 1051 */       break;
/*      */     case 27:
/* 1054 */       this.substRef = ((UName)$__result__);
/* 1055 */       action2();
/* 1056 */       this.$_ngcc_current_state = 14;
/*      */ 
/* 1058 */       break;
/*      */     case 10:
/* 1061 */       this.typeName = ((UName)$__result__);
/* 1062 */       this.$_ngcc_current_state = 5;
/*      */ 
/* 1064 */       break;
/*      */     case 60:
/* 1067 */       this.blockValue = ((Integer)$__result__);
/* 1068 */       this.$_ngcc_current_state = 41;
/*      */ 
/* 1070 */       break;
/*      */     case 69:
/* 1073 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1074 */       this.$_ngcc_current_state = 44;
/*      */ 
/* 1076 */       break;
/*      */     case 19:
/* 1079 */       this.type = ((SimpleTypeImpl)$__result__);
/* 1080 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1082 */       break;
/*      */     case 20:
/* 1085 */       this.type = ((ComplexTypeImpl)$__result__);
/* 1086 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1088 */       break;
/*      */     case 40:
/* 1091 */       this.form = ((Boolean)$__result__).booleanValue();
/* 1092 */       action3();
/* 1093 */       this.$_ngcc_current_state = 25;
/*      */ 
/* 1095 */       break;
/*      */     case 6:
/* 1098 */       this.idc = ((IdentityConstraintImpl)$__result__);
/* 1099 */       action0();
/* 1100 */       this.$_ngcc_current_state = 0;
/*      */ 
/* 1102 */       break;
/*      */     case 7:
/* 1105 */       this.idc = ((IdentityConstraintImpl)$__result__);
/* 1106 */       action0();
/* 1107 */       this.$_ngcc_current_state = 0;
/*      */ 
/* 1109 */       break;
/*      */     case 55:
/* 1112 */       this.finalValue = ((Integer)$__result__);
/* 1113 */       this.$_ngcc_current_state = 37;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean accepted()
/*      */   {
/* 1120 */     return (this.$_ngcc_current_state == 1) || (this.$_ngcc_current_state == 0) || (this.$_ngcc_current_state == 3) || (this.$_ngcc_current_state == 17) || (this.$_ngcc_current_state == 13) || (this.$_ngcc_current_state == 11);
/*      */   }
/*      */ 
/*      */   private ElementDecl makeResult()
/*      */   {
/* 1132 */     if (this.finalValue == null)
/* 1133 */       this.finalValue = new Integer(this.$runtime.finalDefault);
/* 1134 */     if (this.blockValue == null) {
/* 1135 */       this.blockValue = new Integer(this.$runtime.blockDefault);
/*      */     }
/* 1137 */     if (!this.formSpecified)
/* 1138 */       this.form = this.$runtime.elementFormDefault;
/* 1139 */     if (this.isGlobal)
/* 1140 */       this.form = true;
/* 1143 */     String tns;
/*      */     String tns;
/* 1143 */     if (this.form) tns = this.$runtime.currentSchema.getTargetNamespace(); else {
/* 1144 */       tns = "";
/*      */     }
/* 1146 */     if (this.type == null) {
/* 1147 */       if (this.substHeadRef != null)
/* 1148 */         this.type = new SubstGroupBaseTypeRef(this.substHeadRef);
/*      */       else {
/* 1150 */         this.type = this.$runtime.parser.schemaSet.anyType;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1170 */     ElementDecl ed = new ElementDecl(this.$runtime, this.$runtime.document, this.annotation, this.locator, this.fa, tns, this.name, !this.isGlobal, this.$runtime
/* 1162 */       .createXmlString(this.defaultValue), 
/* 1162 */       this.$runtime
/* 1163 */       .createXmlString(this.fixedValue), 
/* 1163 */       this.$runtime
/* 1164 */       .parseBoolean(this.nillable), 
/* 1164 */       this.$runtime
/* 1165 */       .parseBoolean(this.abstractValue), 
/* 1165 */       this.formSpecified ? 
/* 1166 */       Boolean.valueOf(this.form) : 
/* 1166 */       null, this.type, this.substHeadRef, this.blockValue
/* 1169 */       .intValue(), this.finalValue
/* 1170 */       .intValue(), this.idcs);
/*      */ 
/* 1174 */     if ((this.type instanceof ComplexTypeImpl))
/* 1175 */       ((ComplexTypeImpl)this.type).setScope(ed);
/* 1176 */     return ed;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.elementDeclBody
 * JD-Core Version:    0.6.2
 */