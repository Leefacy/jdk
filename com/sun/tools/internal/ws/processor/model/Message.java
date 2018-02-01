/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.resources.ModelMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class Message extends ModelObject
/*     */ {
/* 228 */   private Map<QName, Block> _attachmentBlocks = new HashMap();
/* 229 */   private Map<QName, Block> _bodyBlocks = new HashMap();
/* 230 */   private Map<QName, Block> _headerBlocks = new HashMap();
/* 231 */   private Map<QName, Block> _unboundBlocks = new HashMap();
/* 232 */   private List<Parameter> _parameters = new ArrayList();
/* 233 */   private Map<String, Parameter> _parametersByName = new HashMap();
/*     */ 
/*     */   protected Message(com.sun.tools.internal.ws.wsdl.document.Message entity, ErrorReceiver receiver)
/*     */   {
/*  42 */     super(entity);
/*  43 */     setErrorReceiver(receiver);
/*     */   }
/*     */ 
/*     */   public void addBodyBlock(Block b) {
/*  47 */     if (this._bodyBlocks.containsKey(b.getName())) {
/*  48 */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/*  49 */       throw new AbortException();
/*     */     }
/*  51 */     this._bodyBlocks.put(b.getName(), b);
/*  52 */     b.setLocation(1);
/*     */   }
/*     */ 
/*     */   public Iterator<Block> getBodyBlocks() {
/*  56 */     return this._bodyBlocks.values().iterator();
/*     */   }
/*     */ 
/*     */   public int getBodyBlockCount() {
/*  60 */     return this._bodyBlocks.size();
/*     */   }
/*     */ 
/*     */   public Map<QName, Block> getBodyBlocksMap()
/*     */   {
/*  65 */     return this._bodyBlocks;
/*     */   }
/*     */ 
/*     */   public void setBodyBlocksMap(Map<QName, Block> m)
/*     */   {
/*  70 */     this._bodyBlocks = m;
/*     */   }
/*     */ 
/*     */   public boolean isBodyEmpty() {
/*  74 */     return getBodyBlocks().hasNext();
/*     */   }
/*     */ 
/*     */   public boolean isBodyEncoded() {
/*  78 */     boolean isEncoded = false;
/*  79 */     for (Iterator iter = getBodyBlocks(); iter.hasNext(); ) {
/*  80 */       Block bodyBlock = (Block)iter.next();
/*  81 */       if (bodyBlock.getType().isSOAPType()) {
/*  82 */         isEncoded = true;
/*     */       }
/*     */     }
/*  85 */     return isEncoded;
/*     */   }
/*     */ 
/*     */   public void addHeaderBlock(Block b) {
/*  89 */     if (this._headerBlocks.containsKey(b.getName())) {
/*  90 */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/*  91 */       throw new AbortException();
/*     */     }
/*  93 */     this._headerBlocks.put(b.getName(), b);
/*  94 */     b.setLocation(2);
/*     */   }
/*     */ 
/*     */   public Iterator<Block> getHeaderBlocks() {
/*  98 */     return this._headerBlocks.values().iterator();
/*     */   }
/*     */ 
/*     */   public Collection<Block> getHeaderBlockCollection() {
/* 102 */     return this._headerBlocks.values();
/*     */   }
/*     */ 
/*     */   public int getHeaderBlockCount() {
/* 106 */     return this._headerBlocks.size();
/*     */   }
/*     */ 
/*     */   public Map<QName, Block> getHeaderBlocksMap()
/*     */   {
/* 111 */     return this._headerBlocks;
/*     */   }
/*     */ 
/*     */   public void setHeaderBlocksMap(Map<QName, Block> m)
/*     */   {
/* 116 */     this._headerBlocks = m;
/*     */   }
/*     */ 
/*     */   public void addAttachmentBlock(Block b)
/*     */   {
/* 121 */     if (this._attachmentBlocks.containsKey(b.getName())) {
/* 122 */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PART_NOT_UNIQUE(((com.sun.tools.internal.ws.wsdl.document.Message)getEntity()).getName(), b.getName()));
/* 123 */       throw new AbortException();
/*     */     }
/* 125 */     this._attachmentBlocks.put(b.getName(), b);
/* 126 */     b.setLocation(3);
/*     */   }
/*     */ 
/*     */   public void addUnboundBlock(Block b) {
/* 130 */     if (this._unboundBlocks.containsKey(b.getName())) {
/* 131 */       return;
/*     */     }
/* 133 */     this._unboundBlocks.put(b.getName(), b);
/* 134 */     b.setLocation(0);
/*     */   }
/*     */ 
/*     */   public Iterator<Block> getUnboundBlocks() {
/* 138 */     return this._unboundBlocks.values().iterator();
/*     */   }
/*     */ 
/*     */   public Map<QName, Block> getUnboundBlocksMap()
/*     */   {
/* 143 */     return this._unboundBlocks;
/*     */   }
/*     */ 
/*     */   public int getUnboundBlocksCount() {
/* 147 */     return this._unboundBlocks.size();
/*     */   }
/*     */ 
/*     */   public void setUnboundBlocksMap(Map<QName, Block> m)
/*     */   {
/* 152 */     this._unboundBlocks = m;
/*     */   }
/*     */ 
/*     */   public Iterator<Block> getAttachmentBlocks()
/*     */   {
/* 157 */     return this._attachmentBlocks.values().iterator();
/*     */   }
/*     */ 
/*     */   public int getAttachmentBlockCount() {
/* 161 */     return this._attachmentBlocks.size();
/*     */   }
/*     */ 
/*     */   public Map<QName, Block> getAttachmentBlocksMap()
/*     */   {
/* 166 */     return this._attachmentBlocks;
/*     */   }
/*     */ 
/*     */   public void setAttachmentBlocksMap(Map<QName, Block> m)
/*     */   {
/* 171 */     this._attachmentBlocks = m;
/*     */   }
/*     */ 
/*     */   public void addParameter(Parameter p) {
/* 175 */     if (this._parametersByName.containsKey(p.getName())) {
/* 176 */       this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(p.getName(), p.getName()));
/* 177 */       throw new AbortException();
/*     */     }
/* 179 */     this._parameters.add(p);
/* 180 */     String name = p.getCustomName() != null ? p.getCustomName() : p.getName();
/* 181 */     this._parametersByName.put(name, p);
/*     */   }
/*     */ 
/*     */   public Parameter getParameterByName(String name) {
/* 185 */     if (this._parametersByName.size() != this._parameters.size()) {
/* 186 */       initializeParametersByName();
/*     */     }
/* 188 */     return (Parameter)this._parametersByName.get(name);
/*     */   }
/*     */ 
/*     */   public Iterator<Parameter> getParameters() {
/* 192 */     return this._parameters.iterator();
/*     */   }
/*     */ 
/*     */   public List<Parameter> getParametersList()
/*     */   {
/* 197 */     return this._parameters;
/*     */   }
/*     */ 
/*     */   public void setParametersList(List<Parameter> l)
/*     */   {
/* 202 */     this._parameters = l;
/*     */   }
/*     */ 
/*     */   private void initializeParametersByName() {
/* 206 */     this._parametersByName = new HashMap();
/*     */     Iterator iter;
/* 207 */     if (this._parameters != null)
/* 208 */       for (iter = this._parameters.iterator(); iter.hasNext(); ) {
/* 209 */         Parameter param = (Parameter)iter.next();
/* 210 */         if ((param.getName() != null) && 
/* 211 */           (this._parametersByName
/* 211 */           .containsKey(param
/* 211 */           .getName()))) {
/* 212 */           this.errorReceiver.error(getEntity().getLocator(), ModelMessages.MODEL_PARAMETER_NOTUNIQUE(param.getName(), param.getName()));
/* 213 */           throw new AbortException();
/*     */         }
/* 215 */         this._parametersByName.put(param.getName(), param);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Set<Block> getAllBlocks()
/*     */   {
/* 221 */     Set blocks = new HashSet();
/* 222 */     blocks.addAll(this._bodyBlocks.values());
/* 223 */     blocks.addAll(this._headerBlocks.values());
/* 224 */     blocks.addAll(this._attachmentBlocks.values());
/* 225 */     return blocks;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Message
 * JD-Core Version:    0.6.2
 */