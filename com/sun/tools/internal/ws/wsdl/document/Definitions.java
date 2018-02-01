/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class Definitions extends Entity
/*     */   implements Defining, TWSDLExtensible
/*     */ {
/*     */   private AbstractDocument _document;
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private String _targetNsURI;
/*     */   private Types _types;
/*     */   private List _messages;
/*     */   private List _portTypes;
/*     */   private List _bindings;
/*     */   private List<Service> _services;
/*     */   private List _imports;
/*     */   private Set _importedNamespaces;
/*     */ 
/*     */   public Definitions(AbstractDocument document, Locator locator)
/*     */   {
/*  45 */     super(locator);
/*  46 */     this._document = document;
/*  47 */     this._bindings = new ArrayList();
/*  48 */     this._imports = new ArrayList();
/*  49 */     this._messages = new ArrayList();
/*  50 */     this._portTypes = new ArrayList();
/*  51 */     this._services = new ArrayList();
/*  52 */     this._importedNamespaces = new HashSet();
/*  53 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  57 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(String s) {
/*  61 */     this._name = s;
/*     */   }
/*     */ 
/*     */   public String getTargetNamespaceURI() {
/*  65 */     return this._targetNsURI;
/*     */   }
/*     */ 
/*     */   public void setTargetNamespaceURI(String s) {
/*  69 */     this._targetNsURI = s;
/*     */   }
/*     */ 
/*     */   public void setTypes(Types t) {
/*  73 */     this._types = t;
/*     */   }
/*     */ 
/*     */   public Types getTypes() {
/*  77 */     return this._types;
/*     */   }
/*     */ 
/*     */   public void add(Message m) {
/*  81 */     this._document.define(m);
/*  82 */     this._messages.add(m);
/*     */   }
/*     */ 
/*     */   public void add(PortType p) {
/*  86 */     this._document.define(p);
/*  87 */     this._portTypes.add(p);
/*     */   }
/*     */ 
/*     */   public void add(Binding b) {
/*  91 */     this._document.define(b);
/*  92 */     this._bindings.add(b);
/*     */   }
/*     */ 
/*     */   public void add(Service s) {
/*  96 */     this._document.define(s);
/*  97 */     this._services.add(s);
/*     */   }
/*     */ 
/*     */   public void addServiceOveride(Service s) {
/* 101 */     this._services.add(s);
/*     */   }
/*     */ 
/*     */   public void add(Import i) {
/* 105 */     this._imports.add(i);
/* 106 */     this._importedNamespaces.add(i.getNamespace());
/*     */   }
/*     */ 
/*     */   public Iterator imports() {
/* 110 */     return this._imports.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator messages() {
/* 114 */     return this._messages.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator portTypes() {
/* 118 */     return this._portTypes.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator bindings() {
/* 122 */     return this._bindings.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<Service> services() {
/* 126 */     return this._services.iterator();
/*     */   }
/*     */ 
/*     */   public String getNameValue() {
/* 130 */     return getName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 134 */     return getTargetNamespaceURI();
/*     */   }
/*     */ 
/*     */   public QName getWSDLElementName() {
/* 138 */     return WSDLConstants.QNAME_DEFINITIONS;
/*     */   }
/*     */ 
/*     */   public Documentation getDocumentation() {
/* 142 */     return this._documentation;
/*     */   }
/*     */ 
/*     */   public void setDocumentation(Documentation d) {
/* 146 */     this._documentation = d;
/*     */   }
/*     */ 
/*     */   public void addExtension(TWSDLExtension e) {
/* 150 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 154 */     return this._helper.extensions();
/*     */   }
/*     */ 
/*     */   public TWSDLExtensible getParent()
/*     */   {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 165 */     if (this._types != null) {
/* 166 */       action.perform(this._types);
/*     */     }
/* 168 */     for (Iterator iter = this._messages.iterator(); iter.hasNext(); ) {
/* 169 */       action.perform((Entity)iter.next());
/*     */     }
/* 171 */     for (Iterator iter = this._portTypes.iterator(); iter.hasNext(); ) {
/* 172 */       action.perform((Entity)iter.next());
/*     */     }
/* 174 */     for (Iterator iter = this._bindings.iterator(); iter.hasNext(); ) {
/* 175 */       action.perform((Entity)iter.next());
/*     */     }
/* 177 */     for (Iterator iter = this._services.iterator(); iter.hasNext(); ) {
/* 178 */       action.perform((Entity)iter.next());
/*     */     }
/* 180 */     for (Iterator iter = this._imports.iterator(); iter.hasNext(); ) {
/* 181 */       action.perform((Entity)iter.next());
/*     */     }
/* 183 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */ 
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 187 */     visitor.preVisit(this);
/*     */ 
/* 189 */     for (Iterator iter = this._imports.iterator(); iter.hasNext(); ) {
/* 190 */       ((Import)iter.next()).accept(visitor);
/*     */     }
/*     */ 
/* 193 */     if (this._types != null) {
/* 194 */       this._types.accept(visitor);
/*     */     }
/*     */ 
/* 197 */     for (Iterator iter = this._messages.iterator(); iter.hasNext(); ) {
/* 198 */       ((Message)iter.next()).accept(visitor);
/*     */     }
/* 200 */     for (Iterator iter = this._portTypes.iterator(); iter.hasNext(); ) {
/* 201 */       ((PortType)iter.next()).accept(visitor);
/*     */     }
/* 203 */     for (Iterator iter = this._bindings.iterator(); iter.hasNext(); ) {
/* 204 */       ((Binding)iter.next()).accept(visitor);
/*     */     }
/* 206 */     for (Iterator iter = this._services.iterator(); iter.hasNext(); ) {
/* 207 */       ((Service)iter.next()).accept(visitor);
/*     */     }
/*     */ 
/* 210 */     this._helper.accept(visitor);
/* 211 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */   public void validateThis() {
/*     */   }
/*     */ 
/*     */   public Map resolveBindings() {
/* 218 */     return this._document.getMap(Kinds.BINDING);
/*     */   }
/*     */ 
/*     */   public QName getElementName()
/*     */   {
/* 235 */     return getWSDLElementName();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.Definitions
 * JD-Core Version:    0.6.2
 */