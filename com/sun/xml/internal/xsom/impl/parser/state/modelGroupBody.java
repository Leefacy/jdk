/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup.Compositor;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ParticleImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class modelGroupBody extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String compositorName;
/*     */   private Locator locator;
/*     */   private ParticleImpl childParticle;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ModelGroupImpl result;
/* 368 */   private final List particles = new ArrayList();
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  59 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public modelGroupBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, String _compositorName) {
/*  63 */     super(source, parent, cookie);
/*  64 */     this.$runtime = runtime;
/*  65 */     this.locator = _locator;
/*  66 */     this.compositorName = _compositorName;
/*  67 */     this.$_ngcc_current_state = 6;
/*     */   }
/*     */ 
/*     */   public modelGroupBody(NGCCRuntimeEx runtime, Locator _locator, String _compositorName) {
/*  71 */     this(null, runtime, runtime, -1, _locator, _compositorName);
/*     */   }
/*     */ 
/*     */   private void action0() throws SAXException
/*     */   {
/*  76 */     XSModelGroup.Compositor compositor = null;
/*  77 */     if (this.compositorName.equals("all")) compositor = XSModelGroup.ALL;
/*  78 */     if (this.compositorName.equals("sequence")) compositor = XSModelGroup.SEQUENCE;
/*  79 */     if (this.compositorName.equals("choice")) compositor = XSModelGroup.CHOICE;
/*  80 */     if (compositor == null) {
/*  81 */       throw new InternalError("unable to process " + this.compositorName);
/*     */     }
/*  83 */     this.result = new ModelGroupImpl(this.$runtime.document, this.annotation, this.locator, this.fa, compositor, 
/*  84 */       (ParticleImpl[])this.particles
/*  84 */       .toArray(new ParticleImpl[0]));
/*     */   }
/*     */ 
/*     */   private void action1()
/*     */     throws SAXException
/*     */   {
/*  90 */     this.particles.add(this.childParticle);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs)
/*     */     throws SAXException
/*     */   {
/*  96 */     this.$uri = $__uri;
/*  97 */     this.$localName = $__local;
/*  98 */     this.$qname = $__qname;
/*  99 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 102 */       revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/* 104 */       break;
/*     */     case 4:
/* 107 */       if (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) {
/* 108 */         NGCCHandler h = new annotation(this, this._source, this.$runtime, 174, null, AnnotationContext.MODELGROUP);
/* 109 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 112 */         this.$_ngcc_current_state = 2;
/* 113 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 116 */       break;
/*     */     case 2:
/* 119 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any")))) {
/* 120 */         NGCCHandler h = new particle(this, this._source, this.$runtime, 171);
/* 121 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 124 */         this.$_ngcc_current_state = 1;
/* 125 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 128 */       break;
/*     */     case 6:
/* 131 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("annotation"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any")))) {
/* 132 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 133 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 136 */         NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 137 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 140 */       break;
/*     */     case 1:
/* 143 */       if ((($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("group"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("element"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("all"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("choice"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("sequence"))) || (($__uri.equals("http://www.w3.org/2001/XMLSchema")) && ($__local.equals("any")))) {
/* 144 */         NGCCHandler h = new particle(this, this._source, this.$runtime, 170);
/* 145 */         spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */       else {
/* 148 */         action0();
/* 149 */         this.$_ngcc_current_state = 0;
/* 150 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */       }
/*     */ 
/* 153 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     default:
/* 156 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 164 */     this.$uri = $__uri;
/* 165 */     this.$localName = $__local;
/* 166 */     this.$qname = $__qname;
/* 167 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 170 */       revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 172 */       break;
/*     */     case 4:
/* 175 */       this.$_ngcc_current_state = 2;
/* 176 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 178 */       break;
/*     */     case 2:
/* 181 */       this.$_ngcc_current_state = 1;
/* 182 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 184 */       break;
/*     */     case 6:
/* 187 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 188 */       spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */ 
/* 190 */       break;
/*     */     case 1:
/* 193 */       action0();
/* 194 */       this.$_ngcc_current_state = 0;
/* 195 */       this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 197 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     default:
/* 200 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 208 */     this.$uri = $__uri;
/* 209 */     this.$localName = $__local;
/* 210 */     this.$qname = $__qname;
/* 211 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 214 */       revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 216 */       break;
/*     */     case 4:
/* 219 */       this.$_ngcc_current_state = 2;
/* 220 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 222 */       break;
/*     */     case 2:
/* 225 */       this.$_ngcc_current_state = 1;
/* 226 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 228 */       break;
/*     */     case 6:
/* 231 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 232 */       spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */ 
/* 234 */       break;
/*     */     case 1:
/* 237 */       action0();
/* 238 */       this.$_ngcc_current_state = 0;
/* 239 */       this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 241 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     default:
/* 244 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 252 */     this.$uri = $__uri;
/* 253 */     this.$localName = $__local;
/* 254 */     this.$qname = $__qname;
/* 255 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 258 */       revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 260 */       break;
/*     */     case 4:
/* 263 */       this.$_ngcc_current_state = 2;
/* 264 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 266 */       break;
/*     */     case 2:
/* 269 */       this.$_ngcc_current_state = 1;
/* 270 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 272 */       break;
/*     */     case 6:
/* 275 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 276 */       spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */ 
/* 278 */       break;
/*     */     case 1:
/* 281 */       action0();
/* 282 */       this.$_ngcc_current_state = 0;
/* 283 */       this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 285 */       break;
/*     */     case 3:
/*     */     case 5:
/*     */     default:
/* 288 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 296 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 299 */       revertToParentFromText(this.result, this._cookie, $value);
/*     */ 
/* 301 */       break;
/*     */     case 4:
/* 304 */       this.$_ngcc_current_state = 2;
/* 305 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 307 */       break;
/*     */     case 2:
/* 310 */       this.$_ngcc_current_state = 1;
/* 311 */       this.$runtime.sendText(this._cookie, $value);
/*     */ 
/* 313 */       break;
/*     */     case 6:
/* 316 */       NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 176, null);
/* 317 */       spawnChildFromText(h, $value);
/*     */ 
/* 319 */       break;
/*     */     case 1:
/* 322 */       action0();
/* 323 */       this.$_ngcc_current_state = 0;
/* 324 */       this.$runtime.sendText(this._cookie, $value);
/*     */     case 3:
/*     */     case 5:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 331 */     switch ($__cookie__)
/*     */     {
/*     */     case 174:
/* 334 */       this.annotation = ((AnnotationImpl)$__result__);
/* 335 */       this.$_ngcc_current_state = 2;
/*     */ 
/* 337 */       break;
/*     */     case 176:
/* 340 */       this.fa = ((ForeignAttributesImpl)$__result__);
/* 341 */       this.$_ngcc_current_state = 4;
/*     */ 
/* 343 */       break;
/*     */     case 171:
/* 346 */       this.childParticle = ((ParticleImpl)$__result__);
/* 347 */       action1();
/* 348 */       this.$_ngcc_current_state = 1;
/*     */ 
/* 350 */       break;
/*     */     case 170:
/* 353 */       this.childParticle = ((ParticleImpl)$__result__);
/* 354 */       action1();
/* 355 */       this.$_ngcc_current_state = 1;
/*     */     case 172:
/*     */     case 173:
/*     */     case 175:
/*     */     }
/*     */   }
/*     */ 
/* 362 */   public boolean accepted() { return (this.$_ngcc_current_state == 1) || (this.$_ngcc_current_state == 2) || (this.$_ngcc_current_state == 4) || (this.$_ngcc_current_state == 0); }
/*     */ 
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.modelGroupBody
 * JD-Core Version:    0.6.2
 */