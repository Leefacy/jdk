/*     */ package com.sun.tools.internal.jxc.gen.config;
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
/*  92 */   private int lockCount = 0;
/*     */ 
/* 188 */   private boolean isJoining = false;
/*     */ 
/*     */   protected NGCCInterleaveFilter(NGCCHandler parent, int cookie)
/*     */   {
/*  42 */     this._parent = parent;
/*  43 */     this._cookie = cookie;
/*     */   }
/*     */ 
/*     */   protected void setHandlers(NGCCEventReceiver[] receivers) {
/*  47 */     this._receivers = receivers;
/*     */   }
/*     */ 
/*     */   public int replace(NGCCEventReceiver oldHandler, NGCCEventReceiver newHandler)
/*     */   {
/*  54 */     for (int i = 0; i < this._receivers.length; i++)
/*  55 */       if (this._receivers[i] == oldHandler) {
/*  56 */         this._receivers[i] = newHandler;
/*  57 */         return i;
/*     */       }
/*  59 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public void enterElement(String uri, String localName, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  97 */     if (this.isJoining) return;
/*     */ 
/*  99 */     if (this.lockCount++ == 0) {
/* 100 */       this.lockedReceiver = findReceiverOfElement(uri, localName);
/* 101 */       if (this.lockedReceiver == -1)
/*     */       {
/* 103 */         joinByEnterElement(null, uri, localName, qname, atts);
/* 104 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 108 */     this._receivers[this.lockedReceiver].enterElement(uri, localName, qname, atts);
/*     */   }
/*     */   public void leaveElement(String uri, String localName, String qname) throws SAXException {
/* 111 */     if (this.isJoining) return;
/*     */ 
/* 113 */     if (this.lockCount-- == 0)
/* 114 */       joinByLeaveElement(null, uri, localName, qname);
/*     */     else
/* 116 */       this._receivers[this.lockedReceiver].leaveElement(uri, localName, qname); 
/*     */   }
/*     */ 
/* 119 */   public void enterAttribute(String uri, String localName, String qname) throws SAXException { if (this.isJoining) return;
/*     */ 
/* 121 */     if (this.lockCount++ == 0) {
/* 122 */       this.lockedReceiver = findReceiverOfAttribute(uri, localName);
/* 123 */       if (this.lockedReceiver == -1)
/*     */       {
/* 125 */         joinByEnterAttribute(null, uri, localName, qname);
/* 126 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 130 */     this._receivers[this.lockedReceiver].enterAttribute(uri, localName, qname); }
/*     */ 
/*     */   public void leaveAttribute(String uri, String localName, String qname) throws SAXException {
/* 133 */     if (this.isJoining) return;
/*     */ 
/* 135 */     if (this.lockCount-- == 0)
/* 136 */       joinByLeaveAttribute(null, uri, localName, qname);
/*     */     else
/* 138 */       this._receivers[this.lockedReceiver].leaveAttribute(uri, localName, qname); 
/*     */   }
/*     */ 
/* 141 */   public void text(String value) throws SAXException { if (this.isJoining) return;
/*     */ 
/* 143 */     if (this.lockCount != 0) {
/* 144 */       this._receivers[this.lockedReceiver].text(value);
/*     */     } else {
/* 146 */       int receiver = findReceiverOfText();
/* 147 */       if (receiver != -1) this._receivers[receiver].text(value); else
/* 148 */         joinByText(null, value);
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
/* 210 */     if (this.isJoining) return;
/* 211 */     this.isJoining = true;
/*     */ 
/* 218 */     for (int i = 0; i < this._receivers.length; i++) {
/* 219 */       if (this._receivers[i] != source) {
/* 220 */         this._receivers[i].enterElement(uri, local, qname, atts);
/*     */       }
/*     */     }
/* 223 */     this._parent._source.replace(this, this._parent);
/* 224 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 226 */     this._parent.enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void joinByLeaveElement(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 232 */     if (this.isJoining) return;
/* 233 */     this.isJoining = true;
/*     */ 
/* 240 */     for (int i = 0; i < this._receivers.length; i++) {
/* 241 */       if (this._receivers[i] != source) {
/* 242 */         this._receivers[i].leaveElement(uri, local, qname);
/*     */       }
/*     */     }
/* 245 */     this._parent._source.replace(this, this._parent);
/* 246 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 248 */     this._parent.leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByEnterAttribute(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 254 */     if (this.isJoining) return;
/* 255 */     this.isJoining = true;
/*     */ 
/* 262 */     for (int i = 0; i < this._receivers.length; i++) {
/* 263 */       if (this._receivers[i] != source) {
/* 264 */         this._receivers[i].enterAttribute(uri, local, qname);
/*     */       }
/*     */     }
/* 267 */     this._parent._source.replace(this, this._parent);
/* 268 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 270 */     this._parent.enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByLeaveAttribute(NGCCEventReceiver source, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 276 */     if (this.isJoining) return;
/* 277 */     this.isJoining = true;
/*     */ 
/* 284 */     for (int i = 0; i < this._receivers.length; i++) {
/* 285 */       if (this._receivers[i] != source) {
/* 286 */         this._receivers[i].leaveAttribute(uri, local, qname);
/*     */       }
/*     */     }
/* 289 */     this._parent._source.replace(this, this._parent);
/* 290 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 292 */     this._parent.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void joinByText(NGCCEventReceiver source, String value)
/*     */     throws SAXException
/*     */   {
/* 298 */     if (this.isJoining) return;
/* 299 */     this.isJoining = true;
/*     */ 
/* 306 */     for (int i = 0; i < this._receivers.length; i++) {
/* 307 */       if (this._receivers[i] != source) {
/* 308 */         this._receivers[i].text(value);
/*     */       }
/*     */     }
/* 311 */     this._parent._source.replace(this, this._parent);
/* 312 */     this._parent.onChildCompleted(null, this._cookie, true);
/*     */ 
/* 314 */     this._parent.text(value);
/*     */   }
/*     */ 
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 328 */     this._receivers[threadId].enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 334 */     this._receivers[threadId].enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 340 */     this._receivers[threadId].leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname)
/*     */     throws SAXException
/*     */   {
/* 346 */     this._receivers[threadId].leaveElement(uri, local, qname);
/*     */   }
/*     */ 
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 350 */     this._receivers[threadId].text(value);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.NGCCInterleaveFilter
 * JD-Core Version:    0.6.2
 */