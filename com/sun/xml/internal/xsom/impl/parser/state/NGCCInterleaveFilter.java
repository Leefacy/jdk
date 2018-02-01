/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class NGCCInterleaveFilter
/*     */   implements NGCCEventSource, NGCCEventReceiver
/*     */ {
/*     */   protected NGCCEventReceiver[] _receivers;
/*     */   private final NGCCHandler _parent;
/*     */   private final int _cookie;
/*     */   private int lockedReceiver;
/*  89 */   private int lockCount = 0;
/*     */ 
/* 185 */   private boolean isJoining = false;
/*     */ 
/*     */   protected NGCCInterleaveFilter(NGCCHandler parent, int cookie)
/*     */   {
/*  39 */     this._parent = parent;
/*  40 */     this._cookie = cookie;
/*     */   }
/*     */ 
/*     */   protected void setHandlers(NGCCEventReceiver[] receivers) {
/*  44 */     this._receivers = receivers;
/*     */   }
/*     */ 
/*     */   public int replace(NGCCEventReceiver oldHandler, NGCCEventReceiver newHandler)
/*     */   {
/*  51 */     for (int i = 0; i < this._receivers.length; i++)
/*  52 */       if (this._receivers[i] == oldHandler) {
/*  53 */         this._receivers[i] = newHandler;
/*  54 */         return i;
/*     */       }
/*  56 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public void enterElement(String uri, String localName, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  94 */     if (this.isJoining) return;
/*     */ 
/*  96 */     if (this.lockCount++ == 0) {
/*  97 */       this.lockedReceiver = findReceiverOfElement(uri, localName);
/*  98 */       if (this.lockedReceiver == -1)
/*     */       {
/* 100 */         joinByEnterElement(null, uri, localName, qname, atts);
/* 101 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 105 */     this._receivers[this.lockedReceiver].enterElement(uri, localName, qname, atts);
/*     */   }
/*     */   public void leaveElement(String uri, String localName, String qname) throws SAXException {
/* 108 */     if (this.isJoining) return;
/*     */ 
/* 110 */     if (this.lockCount-- == 0)
/* 111 */       joinByLeaveElement(null, uri, localName, qname);
/*     */     else
/* 113 */       this._receivers[this.lockedReceiver].leaveElement(uri, localName, qname); 
/*     */   }
/*     */ 
/* 116 */   public void enterAttribute(String uri, String localName, String qname) throws SAXException { if (this.isJoining) return;
/*     */ 
/* 118 */     if (this.lockCount++ == 0) {
/* 119 */       this.lockedReceiver = findReceiverOfAttribute(uri, localName);
/* 120 */       if (this.lockedReceiver == -1)
/*     */       {
/* 122 */         joinByEnterAttribute(null, uri, localName, qname);
/* 123 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 127 */     this._receivers[this.lockedReceiver].enterAttribute(uri, localName, qname); }
/*     */ 
/*     */   public void leaveAttribute(String uri, String localName, String qname) throws SAXException {
/* 130 */     if (this.isJoining) return;
/*     */ 
/* 132 */     if (this.lockCount-- == 0)
/* 133 */       joinByLeaveAttribute(null, uri, localName, qname);
/*     */     else
/* 135 */       this._receivers[this.lockedReceiver].leaveAttribute(uri, localName, qname); 
/*     */   }
/*     */ 
/* 138 */   public void text(String value) throws SAXException { if (this.isJoining) return;
/*     */ 
/* 140 */     if (this.lockCount != 0) {
/* 141 */       this._receivers[this.lockedReceiver].text(value);
/*     */     } else {
/* 143 */       int receiver = findReceiverOfText();
/* 144 */       if (receiver != -1) this._receivers[receiver].text(value); else
/* 145 */         joinByText(null, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract int findReceiverOfElement(String paramString1, String paramString2);
/*     */ 
/*     */   protected abstract int findReceiverOfAttribute(String paramString1, String paramString2);
/*     */ 
/*     */   protected abstract int findReceiverOfText();
/*     */ 
/*     */   public void joinByEnterElement(NGCCEventReceiver source, String uri, String local, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 207 */     if (this.isJoining) return;
/* 208 */     this.isJoining = true;
/*     */ 
/* 215 */     for (int i = 0; i < this._receivers.length; i++) {
/* 216 */       if (this._receivers[i] != source) {
/* 217 */         this._receivers[i].enterElement(uri, local, qname, atts);
/*     */       }
/*     */     }
/* 220 */     this._parent._source.replace(this, this._parent);
/* 221 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 223 */     this._parent.enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void joinByLeaveElement(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 229 */     if (this.isJoining) return;
/* 230 */     this.isJoining = true;
/*     */ 
/* 237 */     for (int i = 0; i < this._receivers.length; i++) {
/* 238 */       if (this._receivers[i] != source) {
/* 239 */         this._receivers[i].leaveElement(uri, local, qname);
/*     */       }
/*     */     }
/* 242 */     this._parent._source.replace(this, this._parent);
/* 243 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 245 */     this._parent.leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByEnterAttribute(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 251 */     if (this.isJoining) return;
/* 252 */     this.isJoining = true;
/*     */ 
/* 259 */     for (int i = 0; i < this._receivers.length; i++) {
/* 260 */       if (this._receivers[i] != source) {
/* 261 */         this._receivers[i].enterAttribute(uri, local, qname);
/*     */       }
/*     */     }
/* 264 */     this._parent._source.replace(this, this._parent);
/* 265 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 267 */     this._parent.enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByLeaveAttribute(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 273 */     if (this.isJoining) return;
/* 274 */     this.isJoining = true;
/*     */ 
/* 281 */     for (int i = 0; i < this._receivers.length; i++) {
/* 282 */       if (this._receivers[i] != source) {
/* 283 */         this._receivers[i].leaveAttribute(uri, local, qname);
/*     */       }
/*     */     }
/* 286 */     this._parent._source.replace(this, this._parent);
/* 287 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 289 */     this._parent.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByText(NGCCEventReceiver source, String value)
/*     */     throws SAXException
/*     */   {
/* 295 */     if (this.isJoining) return;
/* 296 */     this.isJoining = true;
/*     */ 
/* 303 */     for (int i = 0; i < this._receivers.length; i++) {
/* 304 */       if (this._receivers[i] != source) {
/* 305 */         this._receivers[i].text(value);
/*     */       }
/*     */     }
/* 308 */     this._parent._source.replace(this, this._parent);
/* 309 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 311 */     this._parent.text(value);
/*     */   }
/*     */ 
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 325 */     this._receivers[threadId].enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 331 */     this._receivers[threadId].enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 337 */     this._receivers[threadId].leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 343 */     this._receivers[threadId].leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 347 */     this._receivers[threadId].text(value);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.state.NGCCInterleaveFilter
 * JD-Core Version:    0.6.2
 */