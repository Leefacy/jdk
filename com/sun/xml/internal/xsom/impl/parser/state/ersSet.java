/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class ersSet extends NGCCHandler
/*     */ {
/*     */   private String v;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */ 
/*     */   public final NGCCRuntime getRuntime()
/*     */   {
/*  54 */     return this.$runtime;
/*     */   }
/*     */ 
/*     */   public ersSet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */ 
/*     */   public ersSet(NGCCRuntimeEx runtime) {
/*  64 */     this(null, runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException
/*     */   {
/*  69 */     this.$uri = $__uri;
/*  70 */     this.$localName = $__local;
/*  71 */     this.$qname = $__qname;
/*  72 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  75 */       revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */ 
/*  77 */       break;
/*     */     default:
/*  80 */       unexpectedEnterElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/*  88 */     this.$uri = $__uri;
/*  89 */     this.$localName = $__local;
/*  90 */     this.$qname = $__qname;
/*  91 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/*  94 */       revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/*  96 */       break;
/*     */     default:
/*  99 */       unexpectedLeaveElement($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 107 */     this.$uri = $__uri;
/* 108 */     this.$localName = $__local;
/* 109 */     this.$qname = $__qname;
/* 110 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 113 */       revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 115 */       break;
/*     */     default:
/* 118 */       unexpectedEnterAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname)
/*     */     throws SAXException
/*     */   {
/* 126 */     this.$uri = $__uri;
/* 127 */     this.$localName = $__local;
/* 128 */     this.$qname = $__qname;
/* 129 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 132 */       revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */ 
/* 134 */       break;
/*     */     default:
/* 137 */       unexpectedLeaveAttribute($__qname);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void text(String $value)
/*     */     throws SAXException
/*     */   {
/* 145 */     switch (this.$_ngcc_current_state)
/*     */     {
/*     */     case 0:
/* 148 */       revertToParentFromText(makeResult(), this._cookie, $value);
/*     */ 
/* 150 */       break;
/*     */     case 1:
/* 153 */       this.v = $value;
/* 154 */       this.$_ngcc_current_state = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean accepted()
/*     */   {
/* 166 */     return this.$_ngcc_current_state == 0;
/*     */   }
/*     */ 
/*     */   private Integer makeResult()
/*     */   {
/* 171 */     if (this.v == null) return new Integer(this.$runtime.blockDefault);
/*     */ 
/* 173 */     if (this.v.indexOf("#all") != -1) {
/* 174 */       return new Integer(7);
/*     */     }
/*     */ 
/* 177 */     int r = 0;
/*     */ 
/* 179 */     if (this.v.indexOf("extension") != -1) r |= 1;
/* 180 */     if (this.v.indexOf("restriction") != -1) r |= 2;
/* 181 */     if (this.v.indexOf("substitution") != -1) r |= 4;
/*     */ 
/* 183 */     return new Integer(r);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.ersSet
 * JD-Core Version:    0.6.2
 */