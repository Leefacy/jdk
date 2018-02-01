/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAnyElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAttributeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefsWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementsWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlValue;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ abstract class AbstractField
/*     */   implements FieldOutline
/*     */ {
/*     */   protected final ClassOutlineImpl outline;
/*     */   protected final CPropertyInfo prop;
/*     */   protected final JCodeModel codeModel;
/*     */   protected final JType implType;
/*     */   protected final JType exposedType;
/* 329 */   private XmlElementsWriter xesw = null;
/*     */ 
/*     */   protected AbstractField(ClassOutlineImpl outline, CPropertyInfo prop)
/*     */   {
/* 107 */     this.outline = outline;
/* 108 */     this.prop = prop;
/* 109 */     this.codeModel = outline.parent().getCodeModel();
/* 110 */     this.implType = getType(Aspect.IMPLEMENTATION);
/* 111 */     this.exposedType = getType(Aspect.EXPOSED);
/*     */   }
/*     */ 
/*     */   public final ClassOutline parent() {
/* 115 */     return this.outline;
/*     */   }
/*     */ 
/*     */   public final CPropertyInfo getPropertyInfo() {
/* 119 */     return this.prop;
/*     */   }
/*     */ 
/*     */   protected void annotate(JAnnotatable field)
/*     */   {
/* 128 */     assert (field != null);
/*     */ 
/* 139 */     if ((this.prop instanceof CAttributePropertyInfo))
/* 140 */       annotateAttribute(field);
/* 141 */     else if ((this.prop instanceof CElementPropertyInfo))
/* 142 */       annotateElement(field);
/* 143 */     else if ((this.prop instanceof CValuePropertyInfo))
/* 144 */       field.annotate(XmlValue.class);
/* 145 */     else if ((this.prop instanceof CReferencePropertyInfo)) {
/* 146 */       annotateReference(field);
/*     */     }
/*     */ 
/* 149 */     this.outline.parent().generateAdapterIfNecessary(this.prop, field);
/*     */ 
/* 151 */     QName st = this.prop.getSchemaType();
/* 152 */     if (st != null) {
/* 153 */       ((XmlSchemaTypeWriter)field.annotate2(XmlSchemaTypeWriter.class))
/* 154 */         .name(st
/* 154 */         .getLocalPart())
/* 155 */         .namespace(st
/* 155 */         .getNamespaceURI());
/*     */     }
/* 157 */     if (this.prop.inlineBinaryData())
/* 158 */       field.annotate(XmlInlineBinaryData.class);
/*     */   }
/*     */ 
/*     */   private void annotateReference(JAnnotatable field) {
/* 162 */     CReferencePropertyInfo rp = (CReferencePropertyInfo)this.prop;
/*     */ 
/* 164 */     TODO.prototype();
/*     */ 
/* 167 */     Collection elements = rp.getElements();
/*     */     XmlElementRefsWriter refsw;
/* 170 */     if (elements.size() == 1) {
/* 171 */       XmlElementRefWriter refw = (XmlElementRefWriter)field.annotate2(XmlElementRefWriter.class);
/* 172 */       CElement e = (CElement)elements.iterator().next();
/* 173 */       refw.name(e.getElementName().getLocalPart())
/* 174 */         .namespace(e
/* 174 */         .getElementName().getNamespaceURI())
/* 175 */         .type(((NType)e
/* 175 */         .getType()).toType(this.outline.parent(), Aspect.IMPLEMENTATION));
/* 176 */       if (getOptions().target.isLaterThan(SpecVersion.V2_2))
/* 177 */         refw.required(rp.isRequired());
/*     */     }
/* 179 */     else if (elements.size() > 1) {
/* 180 */       refsw = (XmlElementRefsWriter)field.annotate2(XmlElementRefsWriter.class);
/* 181 */       for (CElement e : elements) {
/* 182 */         XmlElementRefWriter refw = refsw.value();
/* 183 */         refw.name(e.getElementName().getLocalPart())
/* 184 */           .namespace(e
/* 184 */           .getElementName().getNamespaceURI())
/* 185 */           .type(((NType)e
/* 185 */           .getType()).toType(this.outline.parent(), Aspect.IMPLEMENTATION));
/* 186 */         if (getOptions().target.isLaterThan(SpecVersion.V2_2)) {
/* 187 */           refw.required(rp.isRequired());
/*     */         }
/*     */       }
/*     */     }
/* 191 */     if (rp.isMixed()) {
/* 192 */       field.annotate(XmlMixed.class);
/*     */     }
/* 194 */     NClass dh = rp.getDOMHandler();
/* 195 */     if (dh != null) {
/* 196 */       XmlAnyElementWriter xaew = (XmlAnyElementWriter)field.annotate2(XmlAnyElementWriter.class);
/* 197 */       xaew.lax(rp.getWildcard().allowTypedObject);
/*     */ 
/* 199 */       JClass value = dh.toType(this.outline.parent(), Aspect.IMPLEMENTATION);
/* 200 */       if (!value.equals(this.codeModel.ref(W3CDomHandler.class)))
/* 201 */         xaew.value(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void annotateElement(JAnnotatable field)
/*     */   {
/* 211 */     CElementPropertyInfo ep = (CElementPropertyInfo)this.prop;
/* 212 */     List types = ep.getTypes();
/*     */ 
/* 214 */     if (ep.isValueList()) {
/* 215 */       field.annotate(XmlList.class);
/*     */     }
/*     */ 
/* 218 */     assert (ep.getXmlName() == null);
/*     */     CTypeRef t;
/* 225 */     if (types.size() == 1) {
/* 226 */       t = (CTypeRef)types.get(0);
/* 227 */       writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), false);
/*     */     } else {
/* 229 */       for (CTypeRef t : types)
/*     */       {
/* 231 */         writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), true);
/*     */       }
/* 233 */       this.xesw = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeXmlElementAnnotation(JAnnotatable field, CTypeRef ctype, JType jtype, boolean checkWrapper)
/*     */   {
/* 255 */     XmlElementWriter xew = null;
/*     */ 
/* 258 */     XmlNsForm formDefault = parent()._package().getElementFormDefault();
/* 259 */     String propName = this.prop.getName(false);
/*     */     String enclosingTypeNS;
/*     */     String enclosingTypeNS;
/* 263 */     if (parent().target.getTypeName() == null)
/* 264 */       enclosingTypeNS = parent()._package().getMostUsedNamespaceURI();
/*     */     else {
/* 266 */       enclosingTypeNS = parent().target.getTypeName().getNamespaceURI();
/*     */     }
/*     */ 
/* 269 */     String generatedName = ctype.getTagName().getLocalPart();
/* 270 */     if (!generatedName.equals(propName)) {
/* 271 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 272 */       xew.name(generatedName);
/*     */     }
/*     */ 
/* 276 */     String generatedNS = ctype.getTagName().getNamespaceURI();
/* 277 */     if (((formDefault == XmlNsForm.QUALIFIED) && (!generatedNS.equals(enclosingTypeNS))) || ((formDefault == XmlNsForm.UNQUALIFIED) && 
/* 278 */       (!generatedNS
/* 278 */       .equals(""))))
/*     */     {
/* 279 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 280 */       xew.namespace(generatedNS);
/*     */     }
/*     */ 
/* 284 */     CElementPropertyInfo ep = (CElementPropertyInfo)this.prop;
/* 285 */     if ((ep.isRequired()) && (this.exposedType.isReference())) {
/* 286 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 287 */       xew.required(true);
/*     */     }
/*     */ 
/* 296 */     if ((ep.isRequired()) && (!this.prop.isCollection())) {
/* 297 */       jtype = jtype.unboxify();
/*     */     }
/*     */ 
/* 302 */     if ((!jtype.equals(this.exposedType)) || ((getOptions().runtime14) && (this.prop.isCollection()))) {
/* 303 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 304 */       xew.type(jtype);
/*     */     }
/*     */ 
/* 308 */     String defaultValue = ctype.getDefaultValue();
/* 309 */     if (defaultValue != null) {
/* 310 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 311 */       xew.defaultValue(defaultValue);
/*     */     }
/*     */ 
/* 315 */     if (ctype.isNillable()) {
/* 316 */       if (xew == null) xew = getXew(checkWrapper, field);
/* 317 */       xew.nillable(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final Options getOptions()
/*     */   {
/* 325 */     return parent().parent().getModel().options;
/*     */   }
/*     */ 
/*     */   private XmlElementWriter getXew(boolean checkWrapper, JAnnotatable field)
/*     */   {
/*     */     XmlElementWriter xew;
/*     */     XmlElementWriter xew;
/* 333 */     if (checkWrapper) {
/* 334 */       if (this.xesw == null) {
/* 335 */         this.xesw = ((XmlElementsWriter)field.annotate2(XmlElementsWriter.class));
/*     */       }
/* 337 */       xew = this.xesw.value();
/*     */     } else {
/* 339 */       xew = (XmlElementWriter)field.annotate2(XmlElementWriter.class);
/*     */     }
/* 341 */     return xew;
/*     */   }
/*     */ 
/*     */   private void annotateAttribute(JAnnotatable field)
/*     */   {
/* 348 */     CAttributePropertyInfo ap = (CAttributePropertyInfo)this.prop;
/* 349 */     QName attName = ap.getXmlName();
/*     */ 
/* 353 */     XmlAttributeWriter xaw = (XmlAttributeWriter)field.annotate2(XmlAttributeWriter.class);
/*     */ 
/* 355 */     String generatedName = attName.getLocalPart();
/* 356 */     String generatedNS = attName.getNamespaceURI();
/*     */ 
/* 360 */     if ((!generatedName.equals(ap.getName(false))) || (!generatedName.equals(ap.getName(true))) || (this.outline.parent().getModel().getNameConverter() != NameConverter.standard)) {
/* 361 */       xaw.name(generatedName);
/*     */     }
/*     */ 
/* 365 */     if (!generatedNS.equals("")) {
/* 366 */       xaw.namespace(generatedNS);
/*     */     }
/*     */ 
/* 370 */     if (ap.isRequired())
/* 371 */       xaw.required(true);
/*     */   }
/*     */ 
/*     */   protected final JFieldVar generateField(JType type)
/*     */   {
/* 409 */     return this.outline.implClass.field(2, type, this.prop.getName(false));
/*     */   }
/*     */ 
/*     */   protected final JExpression castToImplType(JExpression exp)
/*     */   {
/* 416 */     if (this.implType == this.exposedType) {
/* 417 */       return exp;
/*     */     }
/* 419 */     return JExpr.cast(this.implType, exp);
/*     */   }
/*     */ 
/*     */   protected JType getType(final Aspect aspect)
/*     */   {
/* 427 */     if (this.prop.getAdapter() != null) {
/* 428 */       return ((NType)this.prop.getAdapter().customType).toType(this.outline.parent(), aspect);
/*     */     }
/*     */ 
/* 447 */     ArrayList r = new ArrayList()
/*     */     {
/*     */       void add(CTypeInfo t)
/*     */       {
/* 432 */         add(((NType)t.getType()).toType(this.this$0.outline.parent(), aspect));
/* 433 */         if ((t instanceof CElementInfo))
/*     */         {
/* 438 */           add(((CElementInfo)t).getSubstitutionMembers());
/*     */         }
/*     */       }
/*     */ 
/*     */       void add(Collection<? extends CTypeInfo> col) {
/* 443 */         for (CTypeInfo typeInfo : col)
/* 444 */           add(typeInfo);
/*     */       }
/*     */     };
/* 448 */     r.add(this.prop.ref());
/*     */     JType t;
/*     */     JType t;
/* 451 */     if (this.prop.baseType != null)
/* 452 */       t = this.prop.baseType;
/*     */     else {
/* 454 */       t = TypeUtil.getCommonBaseType(this.codeModel, r);
/*     */     }
/*     */ 
/* 461 */     if (this.prop.isUnboxable())
/* 462 */       t = t.unboxify();
/* 463 */     return t;
/*     */   }
/*     */ 
/*     */   protected final List<Object> listPossibleTypes(CPropertyInfo prop)
/*     */   {
/* 470 */     List r = new ArrayList();
/* 471 */     for (CTypeInfo tt : prop.ref()) {
/* 472 */       JType t = ((NType)tt.getType()).toType(this.outline.parent(), Aspect.EXPOSED);
/* 473 */       if ((t.isPrimitive()) || (t.isArray())) {
/* 474 */         r.add(t.fullName());
/*     */       } else {
/* 476 */         r.add(t);
/* 477 */         r.add("\n");
/*     */       }
/*     */     }
/*     */ 
/* 481 */     return r;
/*     */   }
/*     */ 
/*     */   private JType resolve(CTypeRef typeRef, Aspect a)
/*     */   {
/* 488 */     return this.outline.parent().resolve(typeRef, a);
/*     */   }
/*     */ 
/*     */   protected abstract class Accessor
/*     */     implements FieldAccessor
/*     */   {
/*     */     protected final JExpression $target;
/*     */ 
/*     */     protected Accessor(JExpression $target)
/*     */     {
/* 386 */       this.$target = $target;
/*     */     }
/*     */ 
/*     */     public final FieldOutline owner() {
/* 390 */       return AbstractField.this;
/*     */     }
/*     */ 
/*     */     public final CPropertyInfo getPropertyInfo() {
/* 394 */       return AbstractField.this.prop;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.AbstractField
 * JD-Core Version:    0.6.2
 */