/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.AttGroup;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.Attribute;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.ComplexType;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.Element;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.IdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.SimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.Term;
/*     */ import com.sun.xml.internal.xsom.impl.Ref.Type;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class DelayedRef
/*     */   implements Patch
/*     */ {
/*     */   protected final XSSchemaSet schema;
/*     */   private PatcherManager manager;
/*     */   private UName name;
/*     */   private Locator source;
/*  86 */   private Object ref = null;
/*     */ 
/*     */   DelayedRef(PatcherManager _manager, Locator _source, SchemaImpl _schema, UName _name)
/*     */   {
/*  54 */     this.schema = _schema.getRoot();
/*  55 */     this.manager = _manager;
/*  56 */     this.name = _name;
/*  57 */     this.source = _source;
/*     */ 
/*  59 */     if (this.name == null) throw new InternalError();
/*     */ 
/*  61 */     this.manager.addPatcher(this);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */     throws SAXException
/*     */   {
/*  69 */     if (this.ref == null)
/*  70 */       resolve();
/*  71 */     this.manager = null;
/*  72 */     this.name = null;
/*  73 */     this.source = null;
/*     */   }
/*     */ 
/*     */   protected abstract Object resolveReference(UName paramUName);
/*     */ 
/*     */   protected abstract String getErrorProperty();
/*     */ 
/*     */   protected final Object _get()
/*     */   {
/*  88 */     if (this.ref == null) throw new InternalError("unresolved reference");
/*  89 */     return this.ref;
/*     */   }
/*     */ 
/*     */   private void resolve() throws SAXException {
/*  93 */     this.ref = resolveReference(this.name);
/*  94 */     if (this.ref == null)
/*  95 */       this.manager.reportError(
/*  96 */         Messages.format(getErrorProperty(), new Object[] { this.name.getQualifiedName() }), this.source);
/*     */   }
/*     */ 
/*     */   public void redefine(XSDeclaration d)
/*     */   {
/* 105 */     if ((!d.getTargetNamespace().equals(this.name.getNamespaceURI())) || 
/* 106 */       (!d
/* 106 */       .getName().equals(this.name.getName()))) {
/* 107 */       return;
/*     */     }
/* 109 */     this.ref = d;
/* 110 */     this.manager = null;
/* 111 */     this.name = null;
/* 112 */     this.source = null;
/*     */   }
/*     */ 
/*     */   public static class AttGroup extends DelayedRef
/*     */     implements Ref.AttGroup
/*     */   {
/*     */     public AttGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 208 */       super(loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 211 */       return this.schema.getAttGroupDecl(name
/* 212 */         .getNamespaceURI(), name
/* 213 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 217 */       return "UndefinedAttributeGroup";
/*     */     }
/*     */     public XSAttGroupDecl get() {
/* 220 */       return (XSAttGroupDecl)super._get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Attribute extends DelayedRef implements Ref.Attribute {
/* 225 */     public Attribute(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) { super(loc, schema, name); }
/*     */ 
/*     */     protected Object resolveReference(UName name) {
/* 228 */       return this.schema.getAttributeDecl(name
/* 229 */         .getNamespaceURI(), name
/* 230 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 234 */       return "UndefinedAttribute";
/*     */     }
/*     */     public XSAttributeDecl getAttribute() {
/* 237 */       return (XSAttributeDecl)super._get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ComplexType extends DelayedRef
/*     */     implements Ref.ComplexType
/*     */   {
/*     */     public ComplexType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 155 */       super(loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 158 */       return this.schema.getComplexType(name
/* 159 */         .getNamespaceURI(), name
/* 160 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 164 */       return "UndefinedCompplexType";
/*     */     }
/*     */     public XSComplexType getType() {
/* 167 */       return (XSComplexType)super._get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Element extends DelayedRef implements Ref.Element {
/* 172 */     public Element(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) { super(loc, schema, name); }
/*     */ 
/*     */     protected Object resolveReference(UName name) {
/* 175 */       return this.schema.getElementDecl(name
/* 176 */         .getNamespaceURI(), name
/* 177 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 181 */       return "UndefinedElement";
/*     */     }
/*     */     public XSElementDecl get() {
/* 184 */       return (XSElementDecl)super._get(); } 
/* 185 */     public XSTerm getTerm() { return get(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static class IdentityConstraint extends DelayedRef
/*     */     implements Ref.IdentityConstraint
/*     */   {
/*     */     public IdentityConstraint(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 242 */       super(loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 245 */       return this.schema.getIdentityConstraint(name
/* 246 */         .getNamespaceURI(), name
/* 247 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 251 */       return "UndefinedIdentityConstraint";
/*     */     }
/*     */     public XSIdentityConstraint get() {
/* 254 */       return (XSIdentityConstraint)super._get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ModelGroup extends DelayedRef
/*     */     implements Ref.Term
/*     */   {
/*     */     public ModelGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 190 */       super(loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 193 */       return this.schema.getModelGroupDecl(name
/* 194 */         .getNamespaceURI(), name
/* 195 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 199 */       return "UndefinedModelGroup";
/*     */     }
/*     */     public XSModelGroupDecl get() {
/* 202 */       return (XSModelGroupDecl)super._get(); } 
/* 203 */     public XSTerm getTerm() { return get(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static class SimpleType extends DelayedRef
/*     */     implements Ref.SimpleType
/*     */   {
/*     */     public SimpleType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 138 */       super(loc, schema, name);
/*     */     }
/* 140 */     public XSSimpleType getType() { return (XSSimpleType)_get(); }
/*     */ 
/*     */     protected Object resolveReference(UName name) {
/* 143 */       return this.schema.getSimpleType(name
/* 144 */         .getNamespaceURI(), name
/* 145 */         .getName());
/*     */     }
/*     */ 
/*     */     protected String getErrorProperty() {
/* 149 */       return "UndefinedSimpleType";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Type extends DelayedRef
/*     */     implements Ref.Type
/*     */   {
/*     */     public Type(PatcherManager manager, Locator loc, SchemaImpl schema, UName name)
/*     */     {
/* 118 */       super(loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 121 */       Object o = this.schema.getSimpleType(name
/* 122 */         .getNamespaceURI(), name.getName());
/* 123 */       if (o != null) return o;
/*     */ 
/* 125 */       return this.schema.getComplexType(name
/* 126 */         .getNamespaceURI(), name
/* 127 */         .getName());
/*     */     }
/*     */     protected String getErrorProperty() {
/* 130 */       return "UndefinedType";
/*     */     }
/*     */     public XSType getType() {
/* 133 */       return (XSType)super._get();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.DelayedRef
 * JD-Core Version:    0.6.2
 */