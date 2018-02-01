/*      */ package com.sun.xml.internal.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.internal.xsom.XSComplexType;
/*      */ import com.sun.xml.internal.xsom.XSContentType;
/*      */ import com.sun.xml.internal.xsom.XSFacet;
/*      */ import com.sun.xml.internal.xsom.XSModelGroup;
/*      */ import com.sun.xml.internal.xsom.XSSimpleType;
/*      */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ContentTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ParticleImpl;
/*      */ import com.sun.xml.internal.xsom.impl.Ref.ContentType;
/*      */ import com.sun.xml.internal.xsom.impl.Ref.SimpleType;
/*      */ import com.sun.xml.internal.xsom.impl.Ref.Type;
/*      */ import com.sun.xml.internal.xsom.impl.RestrictionSimpleTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.UName;
/*      */ import com.sun.xml.internal.xsom.impl.parser.BaseContentRef;
/*      */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef.Type;
/*      */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.internal.xsom.impl.parser.ParserContext;
/*      */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*      */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*      */ import java.util.Collections;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ class complexType extends NGCCHandler
/*      */ {
/*      */   private Integer finalValue;
/*      */   private String name;
/*      */   private String abstractValue;
/*      */   private Integer blockValue;
/*      */   private XSFacet facet;
/*      */   private ForeignAttributesImpl fa;
/*      */   private AnnotationImpl annotation;
/*      */   private ContentTypeImpl explicitContent;
/*      */   private UName baseTypeName;
/*      */   private String mixedValue;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   private int $_ngcc_current_state;
/*      */   protected String $uri;
/*      */   protected String $localName;
/*      */   protected String $qname;
/*      */   private ComplexTypeImpl result;
/*      */   private Ref.Type baseType;
/*      */   private Ref.ContentType contentType;
/*      */   private Ref.SimpleType baseContentType;
/*      */   private RestrictionSimpleTypeImpl contentSimpleType;
/*      */   private Locator locator;
/*      */   private Locator locator2;
/*      */ 
/*      */   public final NGCCRuntime getRuntime()
/*      */   {
/*   63 */     return this.$runtime;
/*      */   }
/*      */ 
/*      */   public complexType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*   67 */     super(source, parent, cookie);
/*   68 */     this.$runtime = runtime;
/*   69 */     this.$_ngcc_current_state = 88;
/*      */   }
/*      */ 
/*      */   public complexType(NGCCRuntimeEx runtime) {
/*   73 */     this(null, runtime, runtime, -1);
/*      */   }
/*      */ 
/*      */   private void action0() throws SAXException
/*      */   {
/*   78 */     this.result.setContentType(this.explicitContent);
/*      */   }
/*      */ 
/*      */   private void action1()
/*      */     throws SAXException
/*      */   {
/*   84 */     this.baseType = this.$runtime.parser.schemaSet.anyType;
/*   85 */     makeResult(2);
/*      */   }
/*      */ 
/*      */   private void action2()
/*      */     throws SAXException
/*      */   {
/*   91 */     this.result.setExplicitContent(this.explicitContent);
/*   92 */     this.result.setContentType(
/*   93 */       buildComplexExtensionContentModel(this.explicitContent));
/*      */   }
/*      */ 
/*      */   private void action3()
/*      */     throws SAXException
/*      */   {
/*   99 */     this.baseType = new DelayedRef.Type(this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */ 
/*  101 */     makeResult(1);
/*      */   }
/*      */ 
/*      */   private void action4() throws SAXException
/*      */   {
/*  106 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   private void action5() throws SAXException
/*      */   {
/*  111 */     this.result.setContentType(this.explicitContent);
/*      */   }
/*      */ 
/*      */   private void action6()
/*      */     throws SAXException
/*      */   {
/*  117 */     this.baseType = new DelayedRef.Type(this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */ 
/*  119 */     makeResult(2);
/*      */   }
/*      */ 
/*      */   private void action7() throws SAXException
/*      */   {
/*  124 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   private void action8() throws SAXException
/*      */   {
/*  129 */     this.contentType = new BaseContentRef(this.$runtime, this.baseType);
/*  130 */     makeResult(1);
/*  131 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */   private void action9()
/*      */     throws SAXException
/*      */   {
/*  137 */     this.baseType = new DelayedRef.Type(this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */   private void action10()
/*      */     throws SAXException
/*      */   {
/*  143 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   private void action11() throws SAXException
/*      */   {
/*  148 */     makeResult(2);
/*  149 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */   private void action12() throws SAXException
/*      */   {
/*  154 */     this.contentSimpleType.addFacet(this.facet);
/*      */   }
/*      */ 
/*      */   private void action13() throws SAXException
/*      */   {
/*  159 */     if (this.baseContentType == null)
/*      */     {
/*  161 */       this.baseContentType = new BaseContentSimpleTypeRef(this.baseType, null);
/*      */     }
/*      */ 
/*  164 */     this.contentSimpleType = new RestrictionSimpleTypeImpl(this.$runtime.document, null, this.locator2, null, null, true, Collections.EMPTY_SET, this.baseContentType);
/*      */ 
/*  167 */     this.contentType = this.contentSimpleType;
/*      */   }
/*      */ 
/*      */   private void action14()
/*      */     throws SAXException
/*      */   {
/*  173 */     this.baseType = new DelayedRef.Type(this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */   private void action15()
/*      */     throws SAXException
/*      */   {
/*  179 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   private void action16() throws SAXException {
/*  183 */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*      */   {
/*  188 */     this.$uri = $__uri;
/*  189 */     this.$localName = $__local;
/*  190 */     this.$qname = $__qname;
/*  191 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 54:
/*  194 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  195 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 617, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  196 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  199 */         this.$_ngcc_current_state = 52;
/*  200 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  203 */       break;
/*      */     case 76:
/*      */       int $ai;
/*  206 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  207 */         this.$runtime.consumeAttribute($ai);
/*  208 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  211 */         this.$_ngcc_current_state = 72;
/*  212 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  215 */       break;
/*      */     case 49:
/*  218 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern")))) {
/*  219 */         NGCCHandler h = new facet(this, this._source, this.$runtime, 610);
/*  220 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  223 */         this.$_ngcc_current_state = 48;
/*  224 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  227 */       break;
/*      */     case 7:
/*  230 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  231 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  232 */         action7();
/*  233 */         this.$_ngcc_current_state = 24;
/*      */       }
/*  236 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  237 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  238 */         action4();
/*  239 */         this.$_ngcc_current_state = 15;
/*      */       }
/*      */       else {
/*  242 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  246 */       break;
/*      */     case 61:
/*  249 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  250 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 626, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  251 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  254 */         this.$_ngcc_current_state = 35;
/*  255 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  258 */       break;
/*      */     case 18:
/*  261 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  262 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 571, this.result);
/*  263 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  266 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  269 */       break;
/*      */     case 12:
/*  272 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  273 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 564, this.fa);
/*  274 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  277 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  280 */       break;
/*      */     case 26:
/*  283 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  284 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 582, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  285 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  288 */         this.$_ngcc_current_state = 7;
/*  289 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  292 */       break;
/*      */     case 38:
/*  295 */       action8();
/*  296 */       this.$_ngcc_current_state = 37;
/*  297 */       this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */ 
/*  299 */       break;
/*      */     case 44:
/*      */       int $ai;
/*  302 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  303 */         this.$runtime.consumeAttribute($ai);
/*  304 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  307 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  310 */       break;
/*      */     case 68:
/*      */       int $ai;
/*  313 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  314 */         this.$runtime.consumeAttribute($ai);
/*  315 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  318 */         this.$_ngcc_current_state = 67;
/*  319 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  322 */       break;
/*      */     case 35:
/*  325 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  326 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  327 */         action15();
/*  328 */         this.$_ngcc_current_state = 59;
/*      */       }
/*  331 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  332 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  333 */         action10();
/*  334 */         this.$_ngcc_current_state = 44;
/*      */       }
/*      */       else {
/*  337 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  341 */       break;
/*      */     case 80:
/*      */       int $ai;
/*  344 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  345 */         this.$runtime.consumeAttribute($ai);
/*  346 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  349 */         this.$_ngcc_current_state = 76;
/*  350 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  353 */       break;
/*      */     case 63:
/*  356 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension")))) {
/*  357 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 628, this.fa);
/*  358 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  361 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  364 */       break;
/*      */     case 88:
/*  367 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  368 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  369 */         action16();
/*  370 */         this.$_ngcc_current_state = 84;
/*      */       }
/*      */       else {
/*  373 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  376 */       break;
/*      */     case 84:
/*      */       int $ai;
/*  379 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  380 */         this.$runtime.consumeAttribute($ai);
/*  381 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  384 */         this.$_ngcc_current_state = 80;
/*  385 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  388 */       break;
/*      */     case 37:
/*  391 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  392 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 594, this.result);
/*  393 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  396 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  399 */       break;
/*      */     case 9:
/*  402 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  403 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 560, this.result);
/*  404 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  407 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  410 */       break;
/*      */     case 19:
/*  413 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  414 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 573, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  415 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  418 */         this.$_ngcc_current_state = 18;
/*  419 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  422 */       break;
/*      */     case 15:
/*      */       int $ai;
/*  425 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  426 */         this.$runtime.consumeAttribute($ai);
/*  427 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  430 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  433 */       break;
/*      */     case 48:
/*  436 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern")))) {
/*  437 */         NGCCHandler h = new facet(this, this._source, this.$runtime, 609);
/*  438 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  441 */         action11();
/*  442 */         this.$_ngcc_current_state = 47;
/*  443 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  446 */       break;
/*      */     case 47:
/*  449 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  450 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 606, this.result);
/*  451 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  454 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  457 */       break;
/*      */     case 24:
/*      */       int $ai;
/*  460 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  461 */         this.$runtime.consumeAttribute($ai);
/*  462 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  465 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  468 */       break;
/*      */     case 28:
/*  471 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction")))) {
/*  472 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 584, this.fa);
/*  473 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  476 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  479 */       break;
/*      */     case 29:
/*      */       int $ai;
/*  482 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  483 */         this.$runtime.consumeAttribute($ai);
/*  484 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  487 */         this.$_ngcc_current_state = 28;
/*  488 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  491 */       break;
/*      */     case 67:
/*  494 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleContent"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexContent"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  495 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 636, this.fa);
/*  496 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  499 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  502 */       break;
/*      */     case 10:
/*  505 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  506 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 562, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  507 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  510 */         this.$_ngcc_current_state = 9;
/*  511 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  514 */       break;
/*      */     case 41:
/*  517 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  518 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 599, this.fa);
/*  519 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  522 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  525 */       break;
/*      */     case 2:
/*  528 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleContent"))) {
/*  529 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  530 */         this.$_ngcc_current_state = 63;
/*      */       }
/*  533 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexContent"))) {
/*  534 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  535 */         this.$_ngcc_current_state = 29;
/*      */       }
/*  538 */       else if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  539 */         action1();
/*  540 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 557, this.result);
/*  541 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  544 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  549 */       break;
/*      */     case 21:
/*  552 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  553 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 575, this.fa);
/*  554 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  557 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  560 */       break;
/*      */     case 72:
/*      */       int $ai;
/*  563 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  564 */         this.$runtime.consumeAttribute($ai);
/*  565 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  568 */         this.$_ngcc_current_state = 68;
/*  569 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  572 */       break;
/*      */     case 56:
/*  575 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxExclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxInclusive"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("totalDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("fractionDigits"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("length"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("maxLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("minLength"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("enumeration"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("whiteSpace"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("pattern"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("anyAttribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  576 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 619, this.fa);
/*  577 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  580 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  583 */       break;
/*      */     case 39:
/*  586 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  587 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 597, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  588 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  591 */         this.$_ngcc_current_state = 38;
/*  592 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  595 */       break;
/*      */     case 59:
/*      */       int $ai;
/*  598 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  599 */         this.$runtime.consumeAttribute($ai);
/*  600 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  603 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  606 */       break;
/*      */     case 52:
/*  609 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/*  610 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 614);
/*  611 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  614 */         this.$_ngcc_current_state = 51;
/*  615 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  618 */       break;
/*      */     case 0:
/*  621 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */ 
/*  623 */       break;
/*      */     case 51:
/*  626 */       action13();
/*  627 */       this.$_ngcc_current_state = 49;
/*  628 */       this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */ 
/*  630 */       break;
/*      */     case 65:
/*  633 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  634 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 634, null, AnnotationContext.COMPLEXTYPE_DECL);
/*  635 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  638 */         this.$_ngcc_current_state = 2;
/*  639 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  642 */       break;
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/*      */     case 11:
/*      */     case 13:
/*      */     case 14:
/*      */     case 16:
/*      */     case 17:
/*      */     case 20:
/*      */     case 22:
/*      */     case 23:
/*      */     case 25:
/*      */     case 27:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 36:
/*      */     case 40:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 46:
/*      */     case 50:
/*      */     case 53:
/*      */     case 55:
/*      */     case 57:
/*      */     case 58:
/*      */     case 60:
/*      */     case 62:
/*      */     case 64:
/*      */     case 66:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     default:
/*  645 */       unexpectedEnterElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  653 */     this.$uri = $__uri;
/*  654 */     this.$localName = $__local;
/*  655 */     this.$qname = $__qname;
/*  656 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 54:
/*  659 */       this.$_ngcc_current_state = 52;
/*  660 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  662 */       break;
/*      */     case 76:
/*      */       int $ai;
/*  665 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  666 */         this.$runtime.consumeAttribute($ai);
/*  667 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  670 */         this.$_ngcc_current_state = 72;
/*  671 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  674 */       break;
/*      */     case 49:
/*  677 */       this.$_ngcc_current_state = 48;
/*  678 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  680 */       break;
/*      */     case 6:
/*  683 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexContent"))) {
/*  684 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  685 */         this.$_ngcc_current_state = 1;
/*      */       }
/*      */       else {
/*  688 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  691 */       break;
/*      */     case 61:
/*  694 */       this.$_ngcc_current_state = 35;
/*  695 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  697 */       break;
/*      */     case 46:
/*  700 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  701 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  702 */         this.$_ngcc_current_state = 34;
/*      */       }
/*      */       else {
/*  705 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  708 */       break;
/*      */     case 36:
/*  711 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  712 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  713 */         this.$_ngcc_current_state = 34;
/*      */       }
/*      */       else {
/*  716 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  719 */       break;
/*      */     case 18:
/*  722 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  723 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 571, this.result);
/*  724 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  727 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  730 */       break;
/*      */     case 12:
/*  733 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  734 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 564, this.fa);
/*  735 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  738 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  741 */       break;
/*      */     case 26:
/*  744 */       this.$_ngcc_current_state = 7;
/*  745 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  747 */       break;
/*      */     case 34:
/*  750 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleContent"))) {
/*  751 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  752 */         this.$_ngcc_current_state = 1;
/*      */       }
/*      */       else {
/*  755 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  758 */       break;
/*      */     case 38:
/*  761 */       action8();
/*  762 */       this.$_ngcc_current_state = 37;
/*  763 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  765 */       break;
/*      */     case 44:
/*      */       int $ai;
/*  768 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  769 */         this.$runtime.consumeAttribute($ai);
/*  770 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  773 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  776 */       break;
/*      */     case 68:
/*      */       int $ai;
/*  779 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  780 */         this.$runtime.consumeAttribute($ai);
/*  781 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  784 */         this.$_ngcc_current_state = 67;
/*  785 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  788 */       break;
/*      */     case 1:
/*  791 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  792 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  793 */         this.$_ngcc_current_state = 0;
/*      */       }
/*      */       else {
/*  796 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  799 */       break;
/*      */     case 80:
/*      */       int $ai;
/*  802 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  803 */         this.$runtime.consumeAttribute($ai);
/*  804 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  807 */         this.$_ngcc_current_state = 76;
/*  808 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  811 */       break;
/*      */     case 37:
/*  814 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  815 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 594, this.result);
/*  816 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  819 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  822 */       break;
/*      */     case 84:
/*      */       int $ai;
/*  825 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  826 */         this.$runtime.consumeAttribute($ai);
/*  827 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  830 */         this.$_ngcc_current_state = 80;
/*  831 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  834 */       break;
/*      */     case 9:
/*  837 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  838 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 560, this.result);
/*  839 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  842 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  845 */       break;
/*      */     case 19:
/*  848 */       this.$_ngcc_current_state = 18;
/*  849 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  851 */       break;
/*      */     case 15:
/*      */       int $ai;
/*  854 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  855 */         this.$runtime.consumeAttribute($ai);
/*  856 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  859 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  862 */       break;
/*      */     case 48:
/*  865 */       action11();
/*  866 */       this.$_ngcc_current_state = 47;
/*  867 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  869 */       break;
/*      */     case 47:
/*  872 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  873 */         NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 606, this.result);
/*  874 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  877 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  880 */       break;
/*      */     case 8:
/*  883 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  884 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  885 */         this.$_ngcc_current_state = 6;
/*      */       }
/*      */       else {
/*  888 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  891 */       break;
/*      */     case 24:
/*      */       int $ai;
/*  894 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  895 */         this.$runtime.consumeAttribute($ai);
/*  896 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  899 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  902 */       break;
/*      */     case 29:
/*      */       int $ai;
/*  905 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  906 */         this.$runtime.consumeAttribute($ai);
/*  907 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  910 */         this.$_ngcc_current_state = 28;
/*  911 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  914 */       break;
/*      */     case 67:
/*  917 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  918 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 636, this.fa);
/*  919 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  922 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  925 */       break;
/*      */     case 10:
/*  928 */       this.$_ngcc_current_state = 9;
/*  929 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  931 */       break;
/*      */     case 41:
/*  934 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("extension"))) {
/*  935 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 599, this.fa);
/*  936 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  939 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  942 */       break;
/*      */     case 2:
/*  945 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  946 */         action1();
/*  947 */         NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 557, this.result);
/*  948 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  951 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  954 */       break;
/*      */     case 21:
/*  957 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  958 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 575, this.fa);
/*  959 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  962 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  965 */       break;
/*      */     case 72:
/*      */       int $ai;
/*  968 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  969 */         this.$runtime.consumeAttribute($ai);
/*  970 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  973 */         this.$_ngcc_current_state = 68;
/*  974 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  977 */       break;
/*      */     case 56:
/*  980 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/*  981 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 619, this.fa);
/*  982 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  985 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  988 */       break;
/*      */     case 39:
/*  991 */       this.$_ngcc_current_state = 38;
/*  992 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  994 */       break;
/*      */     case 59:
/*      */       int $ai;
/*  997 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  998 */         this.$runtime.consumeAttribute($ai);
/*  999 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/* 1002 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/* 1005 */       break;
/*      */     case 52:
/* 1008 */       this.$_ngcc_current_state = 51;
/* 1009 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1011 */       break;
/*      */     case 17:
/* 1014 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("restriction"))) {
/* 1015 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 1016 */         this.$_ngcc_current_state = 6;
/*      */       }
/*      */       else {
/* 1019 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/* 1022 */       break;
/*      */     case 0:
/* 1025 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1027 */       break;
/*      */     case 51:
/* 1030 */       action13();
/* 1031 */       this.$_ngcc_current_state = 49;
/* 1032 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1034 */       break;
/*      */     case 65:
/* 1037 */       this.$_ngcc_current_state = 2;
/* 1038 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1040 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 11:
/*      */     case 13:
/*      */     case 14:
/*      */     case 16:
/*      */     case 20:
/*      */     case 22:
/*      */     case 23:
/*      */     case 25:
/*      */     case 27:
/*      */     case 28:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 35:
/*      */     case 40:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 50:
/*      */     case 53:
/*      */     case 55:
/*      */     case 57:
/*      */     case 58:
/*      */     case 60:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 66:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     default:
/* 1043 */       unexpectedLeaveElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/* 1051 */     this.$uri = $__uri;
/* 1052 */     this.$localName = $__local;
/* 1053 */     this.$qname = $__qname;
/* 1054 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 29:
/* 1057 */       if (($__uri.equals("")) && ($__local.equals("mixed"))) {
/* 1058 */         this.$_ngcc_current_state = 31;
/*      */       }
/*      */       else {
/* 1061 */         this.$_ngcc_current_state = 28;
/* 1062 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1065 */       break;
/*      */     case 54:
/* 1068 */       this.$_ngcc_current_state = 52;
/* 1069 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1071 */       break;
/*      */     case 10:
/* 1074 */       this.$_ngcc_current_state = 9;
/* 1075 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1077 */       break;
/*      */     case 76:
/* 1080 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/* 1081 */         this.$_ngcc_current_state = 78;
/*      */       }
/*      */       else {
/* 1084 */         this.$_ngcc_current_state = 72;
/* 1085 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1088 */       break;
/*      */     case 49:
/* 1091 */       this.$_ngcc_current_state = 48;
/* 1092 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1094 */       break;
/*      */     case 61:
/* 1097 */       this.$_ngcc_current_state = 35;
/* 1098 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1100 */       break;
/*      */     case 72:
/* 1103 */       if (($__uri.equals("")) && ($__local.equals("mixed"))) {
/* 1104 */         this.$_ngcc_current_state = 74;
/*      */       }
/*      */       else {
/* 1107 */         this.$_ngcc_current_state = 68;
/* 1108 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1111 */       break;
/*      */     case 39:
/* 1114 */       this.$_ngcc_current_state = 38;
/* 1115 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1117 */       break;
/*      */     case 59:
/* 1120 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1121 */         this.$_ngcc_current_state = 58;
/*      */       }
/*      */       else {
/* 1124 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/* 1127 */       break;
/*      */     case 26:
/* 1130 */       this.$_ngcc_current_state = 7;
/* 1131 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1133 */       break;
/*      */     case 38:
/* 1136 */       action8();
/* 1137 */       this.$_ngcc_current_state = 37;
/* 1138 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1140 */       break;
/*      */     case 44:
/* 1143 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1144 */         this.$_ngcc_current_state = 43;
/*      */       }
/*      */       else {
/* 1147 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/* 1150 */       break;
/*      */     case 68:
/* 1153 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 1154 */         this.$_ngcc_current_state = 70;
/*      */       }
/*      */       else {
/* 1157 */         this.$_ngcc_current_state = 67;
/* 1158 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1161 */       break;
/*      */     case 52:
/* 1164 */       this.$_ngcc_current_state = 51;
/* 1165 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1167 */       break;
/*      */     case 0:
/* 1170 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1172 */       break;
/*      */     case 51:
/* 1175 */       action13();
/* 1176 */       this.$_ngcc_current_state = 49;
/* 1177 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1179 */       break;
/*      */     case 80:
/* 1182 */       if (($__uri.equals("")) && ($__local.equals("block"))) {
/* 1183 */         this.$_ngcc_current_state = 82;
/*      */       }
/*      */       else {
/* 1186 */         this.$_ngcc_current_state = 76;
/* 1187 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1190 */       break;
/*      */     case 84:
/* 1193 */       if (($__uri.equals("")) && ($__local.equals("abstract"))) {
/* 1194 */         this.$_ngcc_current_state = 86;
/*      */       }
/*      */       else {
/* 1197 */         this.$_ngcc_current_state = 80;
/* 1198 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/* 1201 */       break;
/*      */     case 19:
/* 1204 */       this.$_ngcc_current_state = 18;
/* 1205 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1207 */       break;
/*      */     case 15:
/* 1210 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1211 */         this.$_ngcc_current_state = 14;
/*      */       }
/*      */       else {
/* 1214 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/* 1217 */       break;
/*      */     case 65:
/* 1220 */       this.$_ngcc_current_state = 2;
/* 1221 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1223 */       break;
/*      */     case 48:
/* 1226 */       action11();
/* 1227 */       this.$_ngcc_current_state = 47;
/* 1228 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1230 */       break;
/*      */     case 24:
/* 1233 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1234 */         this.$_ngcc_current_state = 23;
/*      */       }
/*      */       else {
/* 1237 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/* 1240 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 25:
/*      */     case 27:
/*      */     case 28:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 50:
/*      */     case 53:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 60:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 66:
/*      */     case 67:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     default:
/* 1243 */       unexpectedEnterAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/* 1251 */     this.$uri = $__uri;
/* 1252 */     this.$localName = $__local;
/* 1253 */     this.$qname = $__qname;
/* 1254 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 54:
/* 1257 */       this.$_ngcc_current_state = 52;
/* 1258 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1260 */       break;
/*      */     case 76:
/* 1263 */       this.$_ngcc_current_state = 72;
/* 1264 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1266 */       break;
/*      */     case 49:
/* 1269 */       this.$_ngcc_current_state = 48;
/* 1270 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1272 */       break;
/*      */     case 30:
/* 1275 */       if (($__uri.equals("")) && ($__local.equals("mixed"))) {
/* 1276 */         this.$_ngcc_current_state = 28;
/*      */       }
/*      */       else {
/* 1279 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1282 */       break;
/*      */     case 73:
/* 1285 */       if (($__uri.equals("")) && ($__local.equals("mixed"))) {
/* 1286 */         this.$_ngcc_current_state = 68;
/*      */       }
/*      */       else {
/* 1289 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1292 */       break;
/*      */     case 61:
/* 1295 */       this.$_ngcc_current_state = 35;
/* 1296 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1298 */       break;
/*      */     case 26:
/* 1301 */       this.$_ngcc_current_state = 7;
/* 1302 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1304 */       break;
/*      */     case 38:
/* 1307 */       action8();
/* 1308 */       this.$_ngcc_current_state = 37;
/* 1309 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1311 */       break;
/*      */     case 68:
/* 1314 */       this.$_ngcc_current_state = 67;
/* 1315 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1317 */       break;
/*      */     case 13:
/* 1320 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1321 */         this.$_ngcc_current_state = 12;
/*      */       }
/*      */       else {
/* 1324 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1327 */       break;
/*      */     case 85:
/* 1330 */       if (($__uri.equals("")) && ($__local.equals("abstract"))) {
/* 1331 */         this.$_ngcc_current_state = 80;
/*      */       }
/*      */       else {
/* 1334 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1337 */       break;
/*      */     case 80:
/* 1340 */       this.$_ngcc_current_state = 76;
/* 1341 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1343 */       break;
/*      */     case 84:
/* 1346 */       this.$_ngcc_current_state = 80;
/* 1347 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1349 */       break;
/*      */     case 19:
/* 1352 */       this.$_ngcc_current_state = 18;
/* 1353 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1355 */       break;
/*      */     case 48:
/* 1358 */       action11();
/* 1359 */       this.$_ngcc_current_state = 47;
/* 1360 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1362 */       break;
/*      */     case 29:
/* 1365 */       this.$_ngcc_current_state = 28;
/* 1366 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1368 */       break;
/*      */     case 10:
/* 1371 */       this.$_ngcc_current_state = 9;
/* 1372 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1374 */       break;
/*      */     case 77:
/* 1377 */       if (($__uri.equals("")) && ($__local.equals("final"))) {
/* 1378 */         this.$_ngcc_current_state = 72;
/*      */       }
/*      */       else {
/* 1381 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1384 */       break;
/*      */     case 72:
/* 1387 */       this.$_ngcc_current_state = 68;
/* 1388 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1390 */       break;
/*      */     case 69:
/* 1393 */       if (($__uri.equals("")) && ($__local.equals("name"))) {
/* 1394 */         this.$_ngcc_current_state = 67;
/*      */       }
/*      */       else {
/* 1397 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1400 */       break;
/*      */     case 39:
/* 1403 */       this.$_ngcc_current_state = 38;
/* 1404 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1406 */       break;
/*      */     case 22:
/* 1409 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1410 */         this.$_ngcc_current_state = 21;
/*      */       }
/*      */       else {
/* 1413 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1416 */       break;
/*      */     case 81:
/* 1419 */       if (($__uri.equals("")) && ($__local.equals("block"))) {
/* 1420 */         this.$_ngcc_current_state = 76;
/*      */       }
/*      */       else {
/* 1423 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1426 */       break;
/*      */     case 42:
/* 1429 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1430 */         this.$_ngcc_current_state = 41;
/*      */       }
/*      */       else {
/* 1433 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1436 */       break;
/*      */     case 52:
/* 1439 */       this.$_ngcc_current_state = 51;
/* 1440 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1442 */       break;
/*      */     case 0:
/* 1445 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1447 */       break;
/*      */     case 51:
/* 1450 */       action13();
/* 1451 */       this.$_ngcc_current_state = 49;
/* 1452 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1454 */       break;
/*      */     case 57:
/* 1457 */       if (($__uri.equals("")) && ($__local.equals("base"))) {
/* 1458 */         this.$_ngcc_current_state = 56;
/*      */       }
/*      */       else {
/* 1461 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/* 1464 */       break;
/*      */     case 65:
/* 1467 */       this.$_ngcc_current_state = 2;
/* 1468 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/* 1470 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*      */     case 12:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 20:
/*      */     case 21:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 27:
/*      */     case 28:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 40:
/*      */     case 41:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 50:
/*      */     case 53:
/*      */     case 55:
/*      */     case 56:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 66:
/*      */     case 67:
/*      */     case 70:
/*      */     case 71:
/*      */     case 74:
/*      */     case 75:
/*      */     case 78:
/*      */     case 79:
/*      */     case 82:
/*      */     case 83:
/*      */     default:
/* 1473 */       unexpectedLeaveAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void text(String $value)
/*      */     throws SAXException
/*      */   {
/* 1481 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 58:
/* 1484 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 621);
/* 1485 */       spawnChildFromText(h, $value);
/*      */ 
/* 1487 */       break;
/*      */     case 54:
/* 1490 */       this.$_ngcc_current_state = 52;
/* 1491 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1493 */       break;
/*      */     case 31:
/* 1496 */       this.mixedValue = $value;
/* 1497 */       this.$_ngcc_current_state = 30;
/*      */ 
/* 1499 */       break;
/*      */     case 76:
/*      */       int $ai;
/* 1502 */       if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1503 */         this.$runtime.consumeAttribute($ai);
/* 1504 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1507 */         this.$_ngcc_current_state = 72;
/* 1508 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1511 */       break;
/*      */     case 49:
/* 1514 */       this.$_ngcc_current_state = 48;
/* 1515 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1517 */       break;
/*      */     case 61:
/* 1520 */       this.$_ngcc_current_state = 35;
/* 1521 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1523 */       break;
/*      */     case 26:
/* 1526 */       this.$_ngcc_current_state = 7;
/* 1527 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1529 */       break;
/*      */     case 38:
/* 1532 */       action8();
/* 1533 */       this.$_ngcc_current_state = 37;
/* 1534 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1536 */       break;
/*      */     case 44:
/*      */       int $ai;
/* 1539 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1540 */         this.$runtime.consumeAttribute($ai);
/* 1541 */         this.$runtime.sendText(this._cookie, $value); } break;
/*      */     case 68:
/*      */       int $ai;
/* 1547 */       if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1548 */         this.$runtime.consumeAttribute($ai);
/* 1549 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1552 */         this.$_ngcc_current_state = 67;
/* 1553 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1556 */       break;
/*      */     case 80:
/*      */       int $ai;
/* 1559 */       if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 1560 */         this.$runtime.consumeAttribute($ai);
/* 1561 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1564 */         this.$_ngcc_current_state = 76;
/* 1565 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1568 */       break;
/*      */     case 84:
/*      */       int $ai;
/* 1571 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 1572 */         this.$runtime.consumeAttribute($ai);
/* 1573 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1576 */         this.$_ngcc_current_state = 80;
/* 1577 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1580 */       break;
/*      */     case 19:
/* 1583 */       this.$_ngcc_current_state = 18;
/* 1584 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1586 */       break;
/*      */     case 15:
/*      */       int $ai;
/* 1589 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1590 */         this.$runtime.consumeAttribute($ai);
/* 1591 */         this.$runtime.sendText(this._cookie, $value); } break;
/*      */     case 86:
/* 1597 */       this.abstractValue = $value;
/* 1598 */       this.$_ngcc_current_state = 85;
/*      */ 
/* 1600 */       break;
/*      */     case 48:
/* 1603 */       action11();
/* 1604 */       this.$_ngcc_current_state = 47;
/* 1605 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1607 */       break;
/*      */     case 24:
/*      */       int $ai;
/* 1610 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1611 */         this.$runtime.consumeAttribute($ai);
/* 1612 */         this.$runtime.sendText(this._cookie, $value); } break;
/*      */     case 29:
/*      */       int $ai;
/* 1618 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1619 */         this.$runtime.consumeAttribute($ai);
/* 1620 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1623 */         this.$_ngcc_current_state = 28;
/* 1624 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1627 */       break;
/*      */     case 10:
/* 1630 */       this.$_ngcc_current_state = 9;
/* 1631 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1633 */       break;
/*      */     case 72:
/*      */       int $ai;
/* 1636 */       if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1637 */         this.$runtime.consumeAttribute($ai);
/* 1638 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1641 */         this.$_ngcc_current_state = 68;
/* 1642 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1645 */       break;
/*      */     case 43:
/* 1648 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 601);
/* 1649 */       spawnChildFromText(h, $value);
/*      */ 
/* 1651 */       break;
/*      */     case 39:
/* 1654 */       this.$_ngcc_current_state = 38;
/* 1655 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1657 */       break;
/*      */     case 59:
/*      */       int $ai;
/* 1660 */       if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1661 */         this.$runtime.consumeAttribute($ai);
/* 1662 */         this.$runtime.sendText(this._cookie, $value); } break;
/*      */     case 23:
/* 1668 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 577);
/* 1669 */       spawnChildFromText(h, $value);
/*      */ 
/* 1671 */       break;
/*      */     case 52:
/* 1674 */       this.$_ngcc_current_state = 51;
/* 1675 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1677 */       break;
/*      */     case 78:
/* 1680 */       NGCCHandler h = new erSet(this, this._source, this.$runtime, 648);
/* 1681 */       spawnChildFromText(h, $value);
/*      */ 
/* 1683 */       break;
/*      */     case 70:
/* 1686 */       this.name = $value;
/* 1687 */       this.$_ngcc_current_state = 69;
/*      */ 
/* 1689 */       break;
/*      */     case 82:
/* 1692 */       NGCCHandler h = new erSet(this, this._source, this.$runtime, 653);
/* 1693 */       spawnChildFromText(h, $value);
/*      */ 
/* 1695 */       break;
/*      */     case 0:
/* 1698 */       revertToParentFromText(this.result, this._cookie, $value);
/*      */ 
/* 1700 */       break;
/*      */     case 51:
/* 1703 */       action13();
/* 1704 */       this.$_ngcc_current_state = 49;
/* 1705 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1707 */       break;
/*      */     case 65:
/* 1710 */       this.$_ngcc_current_state = 2;
/* 1711 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1713 */       break;
/*      */     case 74:
/* 1716 */       this.mixedValue = $value;
/* 1717 */       this.$_ngcc_current_state = 73;
/*      */ 
/* 1719 */       break;
/*      */     case 14:
/* 1722 */       NGCCHandler h = new qname(this, this._source, this.$runtime, 566);
/* 1723 */       spawnChildFromText(h, $value);
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 25:
/*      */     case 27:
/*      */     case 28:
/*      */     case 30:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 50:
/*      */     case 53:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 60:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 66:
/*      */     case 67:
/*      */     case 69:
/*      */     case 71:
/*      */     case 73:
/*      */     case 75:
/*      */     case 77:
/*      */     case 79:
/*      */     case 81:
/*      */     case 83:
/*      */     case 85: }  } 
/* 1730 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*      */     {
/*      */     case 573:
/* 1733 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1734 */       this.$_ngcc_current_state = 18;
/*      */ 
/* 1736 */       break;
/*      */     case 636:
/* 1739 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1740 */       this.$_ngcc_current_state = 65;
/*      */ 
/* 1742 */       break;
/*      */     case 562:
/* 1745 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1746 */       this.$_ngcc_current_state = 9;
/*      */ 
/* 1748 */       break;
/*      */     case 577:
/* 1751 */       this.baseTypeName = ((UName)$__result__);
/* 1752 */       action6();
/* 1753 */       this.$_ngcc_current_state = 22;
/*      */ 
/* 1755 */       break;
/*      */     case 648:
/* 1758 */       this.finalValue = ((Integer)$__result__);
/* 1759 */       this.$_ngcc_current_state = 77;
/*      */ 
/* 1761 */       break;
/*      */     case 614:
/* 1764 */       this.baseContentType = ((SimpleTypeImpl)$__result__);
/* 1765 */       this.$_ngcc_current_state = 51;
/*      */ 
/* 1767 */       break;
/*      */     case 653:
/* 1770 */       this.blockValue = ((Integer)$__result__);
/* 1771 */       this.$_ngcc_current_state = 81;
/*      */ 
/* 1773 */       break;
/*      */     case 566:
/* 1776 */       this.baseTypeName = ((UName)$__result__);
/* 1777 */       action3();
/* 1778 */       this.$_ngcc_current_state = 13;
/*      */ 
/* 1780 */       break;
/*      */     case 621:
/* 1783 */       this.baseTypeName = ((UName)$__result__);
/* 1784 */       action14();
/* 1785 */       this.$_ngcc_current_state = 57;
/*      */ 
/* 1787 */       break;
/*      */     case 617:
/* 1790 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1791 */       this.$_ngcc_current_state = 52;
/*      */ 
/* 1793 */       break;
/*      */     case 610:
/* 1796 */       this.facet = ((XSFacet)$__result__);
/* 1797 */       action12();
/* 1798 */       this.$_ngcc_current_state = 48;
/*      */ 
/* 1800 */       break;
/*      */     case 626:
/* 1803 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1804 */       this.$_ngcc_current_state = 35;
/*      */ 
/* 1806 */       break;
/*      */     case 571:
/* 1809 */       this.explicitContent = ((ContentTypeImpl)$__result__);
/* 1810 */       action5();
/* 1811 */       this.$_ngcc_current_state = 17;
/*      */ 
/* 1813 */       break;
/*      */     case 564:
/* 1816 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1817 */       this.$_ngcc_current_state = 10;
/*      */ 
/* 1819 */       break;
/*      */     case 582:
/* 1822 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1823 */       this.$_ngcc_current_state = 7;
/*      */ 
/* 1825 */       break;
/*      */     case 628:
/* 1828 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1829 */       this.$_ngcc_current_state = 61;
/*      */ 
/* 1831 */       break;
/*      */     case 594:
/* 1834 */       this.$_ngcc_current_state = 36;
/*      */ 
/* 1836 */       break;
/*      */     case 560:
/* 1839 */       this.explicitContent = ((ContentTypeImpl)$__result__);
/* 1840 */       action2();
/* 1841 */       this.$_ngcc_current_state = 8;
/*      */ 
/* 1843 */       break;
/*      */     case 606:
/* 1846 */       this.$_ngcc_current_state = 46;
/*      */ 
/* 1848 */       break;
/*      */     case 609:
/* 1851 */       this.facet = ((XSFacet)$__result__);
/* 1852 */       action12();
/* 1853 */       this.$_ngcc_current_state = 48;
/*      */ 
/* 1855 */       break;
/*      */     case 584:
/* 1858 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1859 */       this.$_ngcc_current_state = 26;
/*      */ 
/* 1861 */       break;
/*      */     case 599:
/* 1864 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1865 */       this.$_ngcc_current_state = 39;
/*      */ 
/* 1867 */       break;
/*      */     case 557:
/* 1870 */       this.explicitContent = ((ContentTypeImpl)$__result__);
/* 1871 */       action0();
/* 1872 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1874 */       break;
/*      */     case 575:
/* 1877 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1878 */       this.$_ngcc_current_state = 19;
/*      */ 
/* 1880 */       break;
/*      */     case 601:
/* 1883 */       this.baseTypeName = ((UName)$__result__);
/* 1884 */       action9();
/* 1885 */       this.$_ngcc_current_state = 42;
/*      */ 
/* 1887 */       break;
/*      */     case 619:
/* 1890 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1891 */       this.$_ngcc_current_state = 54;
/*      */ 
/* 1893 */       break;
/*      */     case 597:
/* 1896 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1897 */       this.$_ngcc_current_state = 38;
/*      */ 
/* 1899 */       break;
/*      */     case 634:
/* 1902 */       this.annotation = ((AnnotationImpl)$__result__);
/* 1903 */       this.$_ngcc_current_state = 2;
/*      */     case 558:
/*      */     case 559:
/*      */     case 561:
/*      */     case 563:
/*      */     case 565:
/*      */     case 567:
/*      */     case 568:
/*      */     case 569:
/*      */     case 570:
/*      */     case 572:
/*      */     case 574:
/*      */     case 576:
/*      */     case 578:
/*      */     case 579:
/*      */     case 580:
/*      */     case 581:
/*      */     case 583:
/*      */     case 585:
/*      */     case 586:
/*      */     case 587:
/*      */     case 588:
/*      */     case 589:
/*      */     case 590:
/*      */     case 591:
/*      */     case 592:
/*      */     case 593:
/*      */     case 595:
/*      */     case 596:
/*      */     case 598:
/*      */     case 600:
/*      */     case 602:
/*      */     case 603:
/*      */     case 604:
/*      */     case 605:
/*      */     case 607:
/*      */     case 608:
/*      */     case 611:
/*      */     case 612:
/*      */     case 613:
/*      */     case 615:
/*      */     case 616:
/*      */     case 618:
/*      */     case 620:
/*      */     case 622:
/*      */     case 623:
/*      */     case 624:
/*      */     case 625:
/*      */     case 627:
/*      */     case 629:
/*      */     case 630:
/*      */     case 631:
/*      */     case 632:
/*      */     case 633:
/*      */     case 635:
/*      */     case 637:
/*      */     case 638:
/*      */     case 639:
/*      */     case 640:
/*      */     case 641:
/*      */     case 642:
/*      */     case 643:
/*      */     case 644:
/*      */     case 645:
/*      */     case 646:
/*      */     case 647:
/*      */     case 649:
/*      */     case 650:
/*      */     case 651:
/*      */     case 652: }  } 
/* 1910 */   public boolean accepted() { return this.$_ngcc_current_state == 0; }
/*      */ 
/*      */ 
/*      */   private void makeResult(int derivationMethod)
/*      */   {
/* 1936 */     if (this.finalValue == null)
/* 1937 */       this.finalValue = Integer.valueOf(this.$runtime.finalDefault);
/* 1938 */     if (this.blockValue == null) {
/* 1939 */       this.blockValue = Integer.valueOf(this.$runtime.blockDefault);
/*      */     }
/* 1941 */     this.result = new ComplexTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, this.name == null, this.$runtime
/* 1948 */       .parseBoolean(this.abstractValue), 
/* 1948 */       derivationMethod, this.baseType, this.finalValue
/* 1951 */       .intValue(), this.blockValue
/* 1952 */       .intValue(), this.$runtime
/* 1953 */       .parseBoolean(this.mixedValue));
/*      */   }
/*      */ 
/*      */   private Ref.ContentType buildComplexExtensionContentModel(XSContentType explicitContent)
/*      */   {
/* 1997 */     if (explicitContent == this.$runtime.parser.schemaSet.empty) {
/* 1998 */       return new BaseComplexTypeContentRef(this.baseType, null);
/*      */     }
/* 2000 */     return new InheritBaseContentTypeRef(this.baseType, explicitContent, this.$runtime, null);
/*      */   }
/*      */ 
/*      */   private static class BaseComplexTypeContentRef
/*      */     implements Ref.ContentType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */ 
/*      */     private BaseComplexTypeContentRef(Ref.Type _baseType)
/*      */     {
/* 1962 */       this.baseType = _baseType;
/*      */     }
/* 1964 */     public XSContentType getContentType() { return ((XSComplexType)this.baseType.getType()).getContentType(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class BaseContentSimpleTypeRef
/*      */     implements Ref.SimpleType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */ 
/*      */     private BaseContentSimpleTypeRef(Ref.Type _baseType)
/*      */     {
/* 1926 */       this.baseType = _baseType;
/*      */     }
/* 1928 */     public XSSimpleType getType() { return (XSSimpleType)((XSComplexType)this.baseType.getType()).getContentType(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class InheritBaseContentTypeRef
/*      */     implements Ref.ContentType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */     private final XSContentType empty;
/*      */     private final XSContentType expContent;
/*      */     private final SchemaDocumentImpl currentDocument;
/*      */ 
/*      */     private InheritBaseContentTypeRef(Ref.Type _baseType, XSContentType _explicitContent, NGCCRuntimeEx $runtime)
/*      */     {
/* 1975 */       this.baseType = _baseType;
/* 1976 */       this.currentDocument = $runtime.document;
/* 1977 */       this.expContent = _explicitContent;
/* 1978 */       this.empty = $runtime.parser.schemaSet.empty;
/*      */     }
/*      */ 
/*      */     public XSContentType getContentType() {
/* 1982 */       XSContentType baseContentType = ((XSComplexType)this.baseType
/* 1982 */         .getType()).getContentType();
/* 1983 */       if (baseContentType == this.empty) {
/* 1984 */         return this.expContent;
/*      */       }
/* 1986 */       return new ParticleImpl(this.currentDocument, null, new ModelGroupImpl(this.currentDocument, null, null, null, XSModelGroup.SEQUENCE, new ParticleImpl[] { (ParticleImpl)baseContentType, (ParticleImpl)this.expContent }), null);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.complexType
 * JD-Core Version:    0.6.2
 */