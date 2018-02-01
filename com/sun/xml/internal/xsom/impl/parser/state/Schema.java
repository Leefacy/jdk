/*      */ package com.sun.xml.internal.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.internal.xsom.XSNotation;
/*      */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.internal.xsom.impl.AttGroupDeclImpl;
/*      */ import com.sun.xml.internal.xsom.impl.AttributeDeclImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ElementDecl;
/*      */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ModelGroupDeclImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*      */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.parser.Messages;
/*      */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.internal.xsom.impl.parser.ParserContext;
/*      */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ public class Schema extends NGCCHandler
/*      */ {
/*      */   private Integer finalDefault;
/*      */   private boolean efd;
/*      */   private boolean afd;
/*      */   private Integer blockDefault;
/*      */   private ForeignAttributesImpl fa;
/*      */   private boolean includeMode;
/*      */   private AnnotationImpl anno;
/*      */   private ComplexTypeImpl ct;
/*      */   private ElementDecl e;
/*      */   private String defaultValue;
/*      */   private XSNotation notation;
/*      */   private AttGroupDeclImpl ag;
/*      */   private String fixedValue;
/*      */   private ModelGroupDeclImpl group;
/*      */   private AttributeDeclImpl ad;
/*      */   private SimpleTypeImpl st;
/*      */   private String expectedNamespace;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   private int $_ngcc_current_state;
/*      */   protected String $uri;
/*      */   protected String $localName;
/*      */   protected String $qname;
/* 1312 */   private String tns = null;
/*      */   private Locator locator;
/*      */ 
/*      */   public final NGCCRuntime getRuntime()
/*      */   {
/*   72 */     return this.$runtime;
/*      */   }
/*      */ 
/*      */   public Schema(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, boolean _includeMode, String _expectedNamespace) {
/*   76 */     super(source, parent, cookie);
/*   77 */     this.$runtime = runtime;
/*   78 */     this.includeMode = _includeMode;
/*   79 */     this.expectedNamespace = _expectedNamespace;
/*   80 */     this.$_ngcc_current_state = 57;
/*      */   }
/*      */ 
/*      */   public Schema(NGCCRuntimeEx runtime, boolean _includeMode, String _expectedNamespace) {
/*   84 */     this(null, runtime, runtime, -1, _includeMode, _expectedNamespace);
/*      */   }
/*      */ 
/*      */   private void action0() throws SAXException {
/*   88 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getAttGroupDecl(this.ag.getName()));
/*   89 */     this.$runtime.currentSchema.addAttGroupDecl(this.ag, false);
/*      */   }
/*      */ 
/*      */   private void action1() throws SAXException {
/*   93 */     this.$runtime.currentSchema.addNotation(this.notation);
/*      */   }
/*      */ 
/*      */   private void action2() throws SAXException {
/*   97 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getModelGroupDecl(this.group.getName()));
/*   98 */     this.$runtime.currentSchema.addModelGroupDecl(this.group, false);
/*      */   }
/*      */ 
/*      */   private void action3() throws SAXException {
/*  102 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getAttributeDecl(this.ad.getName()));
/*  103 */     this.$runtime.currentSchema.addAttributeDecl(this.ad);
/*      */   }
/*      */ 
/*      */   private void action4() throws SAXException {
/*  107 */     this.locator = this.$runtime.copyLocator();
/*  108 */     this.defaultValue = null;
/*  109 */     this.fixedValue = null;
/*      */   }
/*      */ 
/*      */   private void action5() throws SAXException {
/*  113 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getType(this.ct.getName()));
/*  114 */     this.$runtime.currentSchema.addComplexType(this.ct, false);
/*      */   }
/*      */ 
/*      */   private void action6() throws SAXException {
/*  118 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getType(this.st.getName()));
/*  119 */     this.$runtime.currentSchema.addSimpleType(this.st, false);
/*      */   }
/*      */ 
/*      */   private void action7() throws SAXException {
/*  123 */     this.$runtime.checkDoubleDefError(this.$runtime.currentSchema.getElementDecl(this.e.getName()));
/*  124 */     this.$runtime.currentSchema.addElementDecl(this.e);
/*      */   }
/*      */ 
/*      */   private void action8() throws SAXException {
/*  128 */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   private void action9() throws SAXException {
/*  132 */     this.$runtime.currentSchema.setAnnotation(this.anno);
/*      */   }
/*      */ 
/*      */   private void action10() throws SAXException {
/*  136 */     this.$runtime.currentSchema.addForeignAttributes(this.fa);
/*      */   }
/*      */ 
/*      */   private void action11() throws SAXException {
/*  140 */     this.$runtime.finalDefault = this.finalDefault.intValue();
/*      */   }
/*      */ 
/*      */   private void action12() throws SAXException {
/*  144 */     this.$runtime.blockDefault = this.blockDefault.intValue();
/*      */   }
/*      */ 
/*      */   private void action13() throws SAXException {
/*  148 */     this.$runtime.elementFormDefault = this.efd;
/*      */   }
/*      */ 
/*      */   private void action14() throws SAXException {
/*  152 */     this.$runtime.attributeFormDefault = this.afd;
/*      */   }
/*      */ 
/*      */   private void action15() throws SAXException {
/*  156 */     Attributes test = this.$runtime.getCurrentAttributes();
/*  157 */     String tns = test.getValue("targetNamespace");
/*      */ 
/*  159 */     if (!this.includeMode)
/*      */     {
/*  161 */       if (tns == null) tns = "";
/*  162 */       this.$runtime.currentSchema = this.$runtime.parser.schemaSet.createSchema(tns, this.$runtime.copyLocator());
/*  163 */       if ((this.expectedNamespace != null) && (!this.expectedNamespace.equals(tns))) {
/*  164 */         this.$runtime.reportError(
/*  165 */           Messages.format("UnexpectedTargetnamespace.Import", new Object[] { tns, this.expectedNamespace, tns }), 
/*  165 */           this.$runtime
/*  166 */           .getLocator());
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  173 */       if ((tns != null) && (this.expectedNamespace != null) && (!this.expectedNamespace.equals(tns))) {
/*  174 */         this.$runtime.reportError(
/*  175 */           Messages.format("UnexpectedTargetnamespace.Include", new Object[] { tns, this.expectedNamespace, tns }));
/*      */       }
/*      */ 
/*  177 */       this.$runtime.chameleonMode = true;
/*      */     }
/*      */ 
/*  181 */     if (this.$runtime.hasAlreadyBeenRead())
/*      */     {
/*  183 */       this.$runtime.redirectSubtree(new DefaultHandler(), "", "", "");
/*  184 */       return;
/*      */     }
/*      */ 
/*  187 */     this.anno = ((AnnotationImpl)this.$runtime.currentSchema.getAnnotation());
/*  188 */     this.$runtime.blockDefault = 0;
/*  189 */     this.$runtime.finalDefault = 0;
/*      */   }
/*      */ 
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*      */   {
/*  194 */     this.$uri = $__uri;
/*  195 */     this.$localName = $__local;
/*  196 */     this.$qname = $__qname;
/*  197 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 49:
/*      */       int $ai;
/*  200 */       if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*  201 */         this.$runtime.consumeAttribute($ai);
/*  202 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  205 */         this.$_ngcc_current_state = 45;
/*  206 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  209 */       break;
/*      */     case 36:
/*  212 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("include"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("redefine"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("import")))) {
/*  213 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 527, null);
/*  214 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  217 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  220 */       break;
/*      */     case 0:
/*  223 */       revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */ 
/*  225 */       break;
/*      */     case 16:
/*      */       int $ai;
/*  228 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  229 */         this.$runtime.consumeAttribute($ai);
/*  230 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  233 */         this.$_ngcc_current_state = 12;
/*  234 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  237 */       break;
/*      */     case 53:
/*      */       int $ai;
/*  240 */       if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*  241 */         this.$runtime.consumeAttribute($ai);
/*  242 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  245 */         this.$_ngcc_current_state = 49;
/*  246 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  249 */       break;
/*      */     case 37:
/*      */       int $ai;
/*  252 */       if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*  253 */         this.$runtime.consumeAttribute($ai);
/*  254 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  257 */         this.$_ngcc_current_state = 36;
/*  258 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  261 */       break;
/*      */     case 12:
/*      */       int $ai;
/*  264 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  265 */         this.$runtime.consumeAttribute($ai);
/*  266 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  269 */         this.$_ngcc_current_state = 11;
/*  270 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  273 */       break;
/*      */     case 45:
/*      */       int $ai;
/*  276 */       if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*  277 */         this.$runtime.consumeAttribute($ai);
/*  278 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  281 */         this.$_ngcc_current_state = 41;
/*  282 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  285 */       break;
/*      */     case 41:
/*      */       int $ai;
/*  288 */       if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*  289 */         this.$runtime.consumeAttribute($ai);
/*  290 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  293 */         this.$_ngcc_current_state = 37;
/*  294 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  297 */       break;
/*      */     case 2:
/*  300 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  301 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 515, this.anno, AnnotationContext.SCHEMA);
/*  302 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  305 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("include"))) {
/*  306 */         NGCCHandler h = new includeDecl(this, this._source, this.$runtime, 516);
/*  307 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  310 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("import"))) {
/*  311 */         NGCCHandler h = new importDecl(this, this._source, this.$runtime, 517);
/*  312 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  315 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("redefine"))) {
/*  316 */         NGCCHandler h = new redefine(this, this._source, this.$runtime, 518);
/*  317 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  320 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) {
/*  321 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  322 */         action8();
/*  323 */         this.$_ngcc_current_state = 27;
/*      */       }
/*  326 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/*  327 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 520);
/*  328 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  331 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  332 */         NGCCHandler h = new complexType(this, this._source, this.$runtime, 521);
/*  333 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  336 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute"))) {
/*  337 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  338 */         action4();
/*  339 */         this.$_ngcc_current_state = 16;
/*      */       }
/*  342 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) {
/*  343 */         NGCCHandler h = new group(this, this._source, this.$runtime, 523);
/*  344 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  347 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) {
/*  348 */         NGCCHandler h = new notation(this, this._source, this.$runtime, 524);
/*  349 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  352 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/*  353 */         NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 525);
/*  354 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  357 */         this.$_ngcc_current_state = 1;
/*  358 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */ 
/*  371 */       break;
/*      */     case 27:
/*      */       int $ai;
/*  374 */       if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0)) {
/*  375 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/*  376 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  379 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  382 */       break;
/*      */     case 57:
/*  385 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("schema"))) {
/*  386 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  387 */         action15();
/*  388 */         this.$_ngcc_current_state = 53;
/*      */       }
/*      */       else {
/*  391 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  394 */       break;
/*      */     case 11:
/*      */       int $ai;
/*  397 */       if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0)) {
/*  398 */         NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 421, this.locator, false, this.defaultValue, this.fixedValue);
/*  399 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  402 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  405 */       break;
/*      */     case 1:
/*  408 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/*  409 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 504, this.anno, AnnotationContext.SCHEMA);
/*  410 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  413 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("include"))) {
/*  414 */         NGCCHandler h = new includeDecl(this, this._source, this.$runtime, 505);
/*  415 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  418 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("import"))) {
/*  419 */         NGCCHandler h = new importDecl(this, this._source, this.$runtime, 506);
/*  420 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  423 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("redefine"))) {
/*  424 */         NGCCHandler h = new redefine(this, this._source, this.$runtime, 507);
/*  425 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  428 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) {
/*  429 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  430 */         action8();
/*  431 */         this.$_ngcc_current_state = 27;
/*      */       }
/*  434 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("simpleType"))) {
/*  435 */         NGCCHandler h = new simpleType(this, this._source, this.$runtime, 509);
/*  436 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  439 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("complexType"))) {
/*  440 */         NGCCHandler h = new complexType(this, this._source, this.$runtime, 510);
/*  441 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  444 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute"))) {
/*  445 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  446 */         action4();
/*  447 */         this.$_ngcc_current_state = 16;
/*      */       }
/*  450 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) {
/*  451 */         NGCCHandler h = new group(this, this._source, this.$runtime, 512);
/*  452 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  455 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("notation"))) {
/*  456 */         NGCCHandler h = new notation(this, this._source, this.$runtime, 513);
/*  457 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*  460 */       else if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attributeGroup"))) {
/*  461 */         NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 514);
/*  462 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */       }
/*      */       else {
/*  465 */         unexpectedEnterElement($__qname);
/*      */       }
/*      */ 
/*  478 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     default:
/*  481 */       unexpectedEnterElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  489 */     this.$uri = $__uri;
/*  490 */     this.$localName = $__local;
/*  491 */     this.$qname = $__qname;
/*  492 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 49:
/*      */       int $ai;
/*  495 */       if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*  496 */         this.$runtime.consumeAttribute($ai);
/*  497 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  500 */         this.$_ngcc_current_state = 45;
/*  501 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  504 */       break;
/*      */     case 36:
/*  507 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("schema"))) {
/*  508 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 527, null);
/*  509 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  512 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  515 */       break;
/*      */     case 0:
/*  518 */       revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  520 */       break;
/*      */     case 10:
/*  523 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute"))) {
/*  524 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  525 */         this.$_ngcc_current_state = 1;
/*      */       }
/*      */       else {
/*  528 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  531 */       break;
/*      */     case 16:
/*      */       int $ai;
/*  534 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  535 */         this.$runtime.consumeAttribute($ai);
/*  536 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  539 */         this.$_ngcc_current_state = 12;
/*  540 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  543 */       break;
/*      */     case 53:
/*      */       int $ai;
/*  546 */       if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*  547 */         this.$runtime.consumeAttribute($ai);
/*  548 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  551 */         this.$_ngcc_current_state = 49;
/*  552 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  555 */       break;
/*      */     case 26:
/*  558 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) {
/*  559 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  560 */         this.$_ngcc_current_state = 1;
/*      */       }
/*      */       else {
/*  563 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  566 */       break;
/*      */     case 37:
/*      */       int $ai;
/*  569 */       if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*  570 */         this.$runtime.consumeAttribute($ai);
/*  571 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  574 */         this.$_ngcc_current_state = 36;
/*  575 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  578 */       break;
/*      */     case 12:
/*      */       int $ai;
/*  581 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  582 */         this.$runtime.consumeAttribute($ai);
/*  583 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  586 */         this.$_ngcc_current_state = 11;
/*  587 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  590 */       break;
/*      */     case 45:
/*      */       int $ai;
/*  593 */       if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*  594 */         this.$runtime.consumeAttribute($ai);
/*  595 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  598 */         this.$_ngcc_current_state = 41;
/*  599 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  602 */       break;
/*      */     case 41:
/*      */       int $ai;
/*  605 */       if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*  606 */         this.$runtime.consumeAttribute($ai);
/*  607 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  610 */         this.$_ngcc_current_state = 37;
/*  611 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  614 */       break;
/*      */     case 2:
/*  617 */       this.$_ngcc_current_state = 1;
/*  618 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  620 */       break;
/*      */     case 27:
/*      */       int $ai;
/*  623 */       if (((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || ((($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element")))) {
/*  624 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/*  625 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  628 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  631 */       break;
/*      */     case 11:
/*      */       int $ai;
/*  634 */       if (((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute"))) || ((($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) && ($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("attribute")))) {
/*  635 */         NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 421, this.locator, false, this.defaultValue, this.fixedValue);
/*  636 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  639 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  642 */       break;
/*      */     case 1:
/*  645 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("schema"))) {
/*  646 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  647 */         this.$_ngcc_current_state = 0;
/*      */       }
/*      */       else {
/*  650 */         unexpectedLeaveElement($__qname);
/*      */       }
/*      */ 
/*  653 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     default:
/*  656 */       unexpectedLeaveElement($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  664 */     this.$uri = $__uri;
/*  665 */     this.$localName = $__local;
/*  666 */     this.$qname = $__qname;
/*  667 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 49:
/*  670 */       if (($__uri.equals("")) && ($__local.equals("attributeFormDefault"))) {
/*  671 */         this.$_ngcc_current_state = 51;
/*      */       }
/*      */       else {
/*  674 */         this.$_ngcc_current_state = 45;
/*  675 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  678 */       break;
/*      */     case 45:
/*  681 */       if (($__uri.equals("")) && ($__local.equals("elementFormDefault"))) {
/*  682 */         this.$_ngcc_current_state = 47;
/*      */       }
/*      */       else {
/*  685 */         this.$_ngcc_current_state = 41;
/*  686 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  689 */       break;
/*      */     case 41:
/*  692 */       if (($__uri.equals("")) && ($__local.equals("blockDefault"))) {
/*  693 */         this.$_ngcc_current_state = 43;
/*      */       }
/*      */       else {
/*  696 */         this.$_ngcc_current_state = 37;
/*  697 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  700 */       break;
/*      */     case 2:
/*  703 */       this.$_ngcc_current_state = 1;
/*  704 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  706 */       break;
/*      */     case 27:
/*  709 */       if ((($__uri.equals("")) && ($__local.equals("default"))) || (($__uri.equals("")) && ($__local.equals("fixed"))) || (($__uri.equals("")) && ($__local.equals("form"))) || (($__uri.equals("")) && ($__local.equals("final"))) || (($__uri.equals("")) && ($__local.equals("block"))) || (($__uri.equals("")) && ($__local.equals("name"))) || (($__uri.equals("")) && ($__local.equals("abstract")))) {
/*  710 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/*  711 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  714 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/*  717 */       break;
/*      */     case 0:
/*  720 */       revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  722 */       break;
/*      */     case 16:
/*  725 */       if (($__uri.equals("")) && ($__local.equals("default"))) {
/*  726 */         this.$_ngcc_current_state = 18;
/*      */       }
/*      */       else {
/*  729 */         this.$_ngcc_current_state = 12;
/*  730 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  733 */       break;
/*      */     case 37:
/*  736 */       if (($__uri.equals("")) && ($__local.equals("finalDefault"))) {
/*  737 */         this.$_ngcc_current_state = 39;
/*      */       }
/*      */       else {
/*  740 */         this.$_ngcc_current_state = 36;
/*  741 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  744 */       break;
/*      */     case 53:
/*  747 */       if (($__uri.equals("")) && ($__local.equals("targetNamespace"))) {
/*  748 */         this.$_ngcc_current_state = 55;
/*      */       }
/*      */       else {
/*  751 */         this.$_ngcc_current_state = 49;
/*  752 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  755 */       break;
/*      */     case 11:
/*  758 */       if ((($__uri.equals("")) && ($__local.equals("name"))) || (($__uri.equals("")) && ($__local.equals("form")))) {
/*  759 */         NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 421, this.locator, false, this.defaultValue, this.fixedValue);
/*  760 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */       }
/*      */       else {
/*  763 */         unexpectedEnterAttribute($__qname);
/*      */       }
/*      */ 
/*  766 */       break;
/*      */     case 12:
/*  769 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/*  770 */         this.$_ngcc_current_state = 14;
/*      */       }
/*      */       else {
/*  773 */         this.$_ngcc_current_state = 11;
/*  774 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */       }
/*      */ 
/*  777 */       break;
/*      */     default:
/*  780 */       unexpectedEnterAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*      */     throws SAXException
/*      */   {
/*  788 */     this.$uri = $__uri;
/*  789 */     this.$localName = $__local;
/*  790 */     this.$qname = $__qname;
/*  791 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 49:
/*  794 */       this.$_ngcc_current_state = 45;
/*  795 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  797 */       break;
/*      */     case 38:
/*  800 */       if (($__uri.equals("")) && ($__local.equals("finalDefault"))) {
/*  801 */         this.$_ngcc_current_state = 36;
/*      */       }
/*      */       else {
/*  804 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  807 */       break;
/*      */     case 0:
/*  810 */       revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  812 */       break;
/*      */     case 16:
/*  815 */       this.$_ngcc_current_state = 12;
/*  816 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  818 */       break;
/*      */     case 13:
/*  821 */       if (($__uri.equals("")) && ($__local.equals("fixed"))) {
/*  822 */         this.$_ngcc_current_state = 11;
/*      */       }
/*      */       else {
/*  825 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  828 */       break;
/*      */     case 53:
/*  831 */       this.$_ngcc_current_state = 49;
/*  832 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  834 */       break;
/*      */     case 37:
/*  837 */       this.$_ngcc_current_state = 36;
/*  838 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  840 */       break;
/*      */     case 17:
/*  843 */       if (($__uri.equals("")) && ($__local.equals("default"))) {
/*  844 */         this.$_ngcc_current_state = 12;
/*      */       }
/*      */       else {
/*  847 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  850 */       break;
/*      */     case 12:
/*  853 */       this.$_ngcc_current_state = 11;
/*  854 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  856 */       break;
/*      */     case 50:
/*  859 */       if (($__uri.equals("")) && ($__local.equals("attributeFormDefault"))) {
/*  860 */         this.$_ngcc_current_state = 45;
/*      */       }
/*      */       else {
/*  863 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  866 */       break;
/*      */     case 42:
/*  869 */       if (($__uri.equals("")) && ($__local.equals("blockDefault"))) {
/*  870 */         this.$_ngcc_current_state = 37;
/*      */       }
/*      */       else {
/*  873 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  876 */       break;
/*      */     case 45:
/*  879 */       this.$_ngcc_current_state = 41;
/*  880 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  882 */       break;
/*      */     case 41:
/*  885 */       this.$_ngcc_current_state = 37;
/*  886 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  888 */       break;
/*      */     case 2:
/*  891 */       this.$_ngcc_current_state = 1;
/*  892 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */ 
/*  894 */       break;
/*      */     case 54:
/*  897 */       if (($__uri.equals("")) && ($__local.equals("targetNamespace"))) {
/*  898 */         this.$_ngcc_current_state = 49;
/*      */       }
/*      */       else {
/*  901 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  904 */       break;
/*      */     case 46:
/*  907 */       if (($__uri.equals("")) && ($__local.equals("elementFormDefault"))) {
/*  908 */         this.$_ngcc_current_state = 41;
/*      */       }
/*      */       else {
/*  911 */         unexpectedLeaveAttribute($__qname);
/*      */       }
/*      */ 
/*  914 */       break;
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 14:
/*      */     case 15:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 39:
/*      */     case 40:
/*      */     case 43:
/*      */     case 44:
/*      */     case 47:
/*      */     case 48:
/*      */     case 51:
/*      */     case 52:
/*      */     default:
/*  917 */       unexpectedLeaveAttribute($__qname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void text(String $value)
/*      */     throws SAXException
/*      */   {
/*  925 */     switch (this.$_ngcc_current_state)
/*      */     {
/*      */     case 49:
/*      */       int $ai;
/*  928 */       if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*  929 */         this.$runtime.consumeAttribute($ai);
/*  930 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  933 */         this.$_ngcc_current_state = 45;
/*  934 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  937 */       break;
/*      */     case 0:
/*  940 */       revertToParentFromText(this, this._cookie, $value);
/*      */ 
/*  942 */       break;
/*      */     case 47:
/*  945 */       if ($value.equals("unqualified")) {
/*  946 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 539);
/*  947 */         spawnChildFromText(h, $value);
/*      */       }
/*  950 */       else if ($value.equals("qualified")) {
/*  951 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 539);
/*  952 */         spawnChildFromText(h, $value);
/*  953 */       }break;
/*      */     case 43:
/*  959 */       NGCCHandler h = new ersSet(this, this._source, this.$runtime, 534);
/*  960 */       spawnChildFromText(h, $value);
/*      */ 
/*  962 */       break;
/*      */     case 16:
/*      */       int $ai;
/*  965 */       if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  966 */         this.$runtime.consumeAttribute($ai);
/*  967 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  970 */         this.$_ngcc_current_state = 12;
/*  971 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  974 */       break;
/*      */     case 53:
/*      */       int $ai;
/*  977 */       if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*  978 */         this.$runtime.consumeAttribute($ai);
/*  979 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  982 */         this.$_ngcc_current_state = 49;
/*  983 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  986 */       break;
/*      */     case 37:
/*      */       int $ai;
/*  989 */       if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*  990 */         this.$runtime.consumeAttribute($ai);
/*  991 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/*  994 */         this.$_ngcc_current_state = 36;
/*  995 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/*  998 */       break;
/*      */     case 12:
/*      */       int $ai;
/* 1001 */       if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 1002 */         this.$runtime.consumeAttribute($ai);
/* 1003 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1006 */         this.$_ngcc_current_state = 11;
/* 1007 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1010 */       break;
/*      */     case 14:
/* 1013 */       this.fixedValue = $value;
/* 1014 */       this.$_ngcc_current_state = 13;
/*      */ 
/* 1016 */       break;
/*      */     case 45:
/*      */       int $ai;
/* 1019 */       if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/* 1020 */         this.$runtime.consumeAttribute($ai);
/* 1021 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1024 */         this.$_ngcc_current_state = 41;
/* 1025 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1028 */       break;
/*      */     case 41:
/*      */       int $ai;
/* 1031 */       if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/* 1032 */         this.$runtime.consumeAttribute($ai);
/* 1033 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */       else {
/* 1036 */         this.$_ngcc_current_state = 37;
/* 1037 */         this.$runtime.sendText(this._cookie, $value);
/*      */       }
/*      */ 
/* 1040 */       break;
/*      */     case 55:
/* 1043 */       this.$_ngcc_current_state = 54;
/*      */ 
/* 1045 */       break;
/*      */     case 2:
/* 1048 */       this.$_ngcc_current_state = 1;
/* 1049 */       this.$runtime.sendText(this._cookie, $value);
/*      */ 
/* 1051 */       break;
/*      */     case 27:
/*      */       int $ai;
/* 1054 */       if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 1055 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1056 */         spawnChildFromText(h, $value);
/*      */       }
/* 1059 */       else if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1060 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1061 */         spawnChildFromText(h, $value);
/*      */       }
/* 1064 */       else if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 1065 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1066 */         spawnChildFromText(h, $value);
/*      */       }
/* 1069 */       else if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1070 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1071 */         spawnChildFromText(h, $value);
/*      */       }
/* 1074 */       else if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 1075 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1076 */         spawnChildFromText(h, $value);
/*      */       }
/* 1079 */       else if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 1080 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1081 */         spawnChildFromText(h, $value);
/*      */       }
/* 1084 */       else if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 1085 */         NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 439, this.locator, true);
/* 1086 */         spawnChildFromText(h, $value);
/* 1087 */       }break;
/*      */     case 39:
/* 1098 */       NGCCHandler h = new erSet(this, this._source, this.$runtime, 529);
/* 1099 */       spawnChildFromText(h, $value);
/*      */ 
/* 1101 */       break;
/*      */     case 51:
/* 1104 */       if ($value.equals("unqualified")) {
/* 1105 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 544);
/* 1106 */         spawnChildFromText(h, $value);
/*      */       }
/* 1109 */       else if ($value.equals("qualified")) {
/* 1110 */         NGCCHandler h = new qualification(this, this._source, this.$runtime, 544);
/* 1111 */         spawnChildFromText(h, $value);
/* 1112 */       }break;
/*      */     case 18:
/* 1118 */       this.defaultValue = $value;
/* 1119 */       this.$_ngcc_current_state = 17;
/*      */ 
/* 1121 */       break;
/*      */     case 11:
/*      */       int $ai;
/* 1124 */       if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 1125 */         NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 421, this.locator, false, this.defaultValue, this.fixedValue);
/* 1126 */         spawnChildFromText(h, $value);
/*      */       }
/* 1129 */       else if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1130 */         NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 421, this.locator, false, this.defaultValue, this.fixedValue);
/* 1131 */         spawnChildFromText(h, $value); } break;
/*      */     case 1:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 13:
/*      */     case 15:
/*      */     case 17:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 38:
/*      */     case 40:
/*      */     case 42:
/*      */     case 44:
/*      */     case 46:
/*      */     case 48:
/*      */     case 50:
/*      */     case 52:
/*      */     case 54: }  } 
/* 1140 */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException { switch ($__cookie__)
/*      */     {
/*      */     case 527:
/* 1143 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 1144 */       action10();
/* 1145 */       this.$_ngcc_current_state = 2;
/*      */ 
/* 1147 */       break;
/*      */     case 534:
/* 1150 */       this.blockDefault = ((Integer)$__result__);
/* 1151 */       action12();
/* 1152 */       this.$_ngcc_current_state = 42;
/*      */ 
/* 1154 */       break;
/*      */     case 439:
/* 1157 */       this.e = ((ElementDecl)$__result__);
/* 1158 */       action7();
/* 1159 */       this.$_ngcc_current_state = 26;
/*      */ 
/* 1161 */       break;
/*      */     case 544:
/* 1164 */       this.afd = ((Boolean)$__result__).booleanValue();
/* 1165 */       action14();
/* 1166 */       this.$_ngcc_current_state = 50;
/*      */ 
/* 1168 */       break;
/*      */     case 421:
/* 1171 */       this.ad = ((AttributeDeclImpl)$__result__);
/* 1172 */       action3();
/* 1173 */       this.$_ngcc_current_state = 10;
/*      */ 
/* 1175 */       break;
/*      */     case 504:
/* 1178 */       this.anno = ((AnnotationImpl)$__result__);
/* 1179 */       action9();
/* 1180 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1182 */       break;
/*      */     case 505:
/* 1185 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1187 */       break;
/*      */     case 506:
/* 1190 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1192 */       break;
/*      */     case 507:
/* 1195 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1197 */       break;
/*      */     case 509:
/* 1200 */       this.st = ((SimpleTypeImpl)$__result__);
/* 1201 */       action6();
/* 1202 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1204 */       break;
/*      */     case 510:
/* 1207 */       this.ct = ((ComplexTypeImpl)$__result__);
/* 1208 */       action5();
/* 1209 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1211 */       break;
/*      */     case 512:
/* 1214 */       this.group = ((ModelGroupDeclImpl)$__result__);
/* 1215 */       action2();
/* 1216 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1218 */       break;
/*      */     case 513:
/* 1221 */       this.notation = ((XSNotation)$__result__);
/* 1222 */       action1();
/* 1223 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1225 */       break;
/*      */     case 514:
/* 1228 */       this.ag = ((AttGroupDeclImpl)$__result__);
/* 1229 */       action0();
/* 1230 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1232 */       break;
/*      */     case 539:
/* 1235 */       this.efd = ((Boolean)$__result__).booleanValue();
/* 1236 */       action13();
/* 1237 */       this.$_ngcc_current_state = 46;
/*      */ 
/* 1239 */       break;
/*      */     case 515:
/* 1242 */       this.anno = ((AnnotationImpl)$__result__);
/* 1243 */       action9();
/* 1244 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1246 */       break;
/*      */     case 516:
/* 1249 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1251 */       break;
/*      */     case 517:
/* 1254 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1256 */       break;
/*      */     case 518:
/* 1259 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1261 */       break;
/*      */     case 520:
/* 1264 */       this.st = ((SimpleTypeImpl)$__result__);
/* 1265 */       action6();
/* 1266 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1268 */       break;
/*      */     case 521:
/* 1271 */       this.ct = ((ComplexTypeImpl)$__result__);
/* 1272 */       action5();
/* 1273 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1275 */       break;
/*      */     case 523:
/* 1278 */       this.group = ((ModelGroupDeclImpl)$__result__);
/* 1279 */       action2();
/* 1280 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1282 */       break;
/*      */     case 524:
/* 1285 */       this.notation = ((XSNotation)$__result__);
/* 1286 */       action1();
/* 1287 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1289 */       break;
/*      */     case 525:
/* 1292 */       this.ag = ((AttGroupDeclImpl)$__result__);
/* 1293 */       action0();
/* 1294 */       this.$_ngcc_current_state = 1;
/*      */ 
/* 1296 */       break;
/*      */     case 529:
/* 1299 */       this.finalDefault = ((Integer)$__result__);
/* 1300 */       action11();
/* 1301 */       this.$_ngcc_current_state = 38;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean accepted()
/*      */   {
/* 1308 */     return this.$_ngcc_current_state == 0;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.Schema
 * JD-Core Version:    0.6.2
 */