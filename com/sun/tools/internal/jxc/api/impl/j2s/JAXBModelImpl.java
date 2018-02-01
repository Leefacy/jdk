/*     */ package com.sun.tools.internal.jxc.api.impl.j2s;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.Reference;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator;
/*     */ import com.sun.xml.internal.txw2.output.ResultFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ 
/*     */ final class JAXBModelImpl
/*     */   implements J2SJAXBModel
/*     */ {
/*     */   private final Map<QName, Reference> additionalElementDecls;
/*  69 */   private final List<String> classList = new ArrayList();
/*     */   private final TypeInfoSet<TypeMirror, TypeElement, VariableElement, ExecutableElement> types;
/*     */   private final AnnotationReader<TypeMirror, TypeElement, VariableElement, ExecutableElement> reader;
/*     */   private XmlSchemaGenerator<TypeMirror, TypeElement, VariableElement, ExecutableElement> xsdgen;
/*  84 */   private final Map<Reference, NonElement<TypeMirror, TypeElement>> refMap = new HashMap();
/*     */ 
/*     */   public JAXBModelImpl(TypeInfoSet<TypeMirror, TypeElement, VariableElement, ExecutableElement> types, AnnotationReader<TypeMirror, TypeElement, VariableElement, ExecutableElement> reader, Collection<Reference> rootClasses, Map<QName, Reference> additionalElementDecls)
/*     */   {
/*  90 */     this.types = types;
/*  91 */     this.reader = reader;
/*  92 */     this.additionalElementDecls = additionalElementDecls;
/*     */ 
/*  94 */     Navigator navigator = types.getNavigator();
/*     */ 
/*  96 */     for (ClassInfo i : types.beans().values()) {
/*  97 */       this.classList.add(i.getName());
/*     */     }
/*     */ 
/* 100 */     for (ArrayInfo a : types.arrays().values()) {
/* 101 */       String javaName = navigator.getTypeName(a.getType());
/* 102 */       this.classList.add(javaName);
/*     */     }
/*     */ 
/* 105 */     for (EnumLeafInfo l : types.enums().values()) {
/* 106 */       QName tn = l.getTypeName();
/* 107 */       if (tn != null) {
/* 108 */         String javaName = navigator.getTypeName(l.getType());
/* 109 */         this.classList.add(javaName);
/*     */       }
/*     */     }
/*     */ 
/* 113 */     for (Reference ref : rootClasses) {
/* 114 */       this.refMap.put(ref, getXmlType(ref));
/*     */     }
/*     */ 
/* 118 */     Iterator itr = additionalElementDecls.entrySet().iterator();
/* 119 */     while (itr.hasNext()) {
/* 120 */       Map.Entry entry = (Map.Entry)itr.next();
/* 121 */       if (entry.getValue() != null)
/*     */       {
/* 123 */         NonElement xt = getXmlType((Reference)entry.getValue());
/*     */ 
/* 125 */         assert (xt != null);
/* 126 */         this.refMap.put(entry.getValue(), xt);
/* 127 */         if ((xt instanceof ClassInfo)) {
/* 128 */           ClassInfo xct = (ClassInfo)xt;
/* 129 */           com.sun.xml.internal.bind.v2.model.core.Element elem = xct.asElement();
/* 130 */           if ((elem != null) && (elem.getElementName().equals(entry.getKey())))
/* 131 */             itr.remove();
/*     */         }
/*     */         else
/*     */         {
/* 135 */           ElementInfo ei = types.getElementInfo(null, (QName)entry.getKey());
/* 136 */           if ((ei != null) && (ei.getContentType() == xt))
/* 137 */             itr.remove();  } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 142 */   public List<String> getClassList() { return this.classList; }
/*     */ 
/*     */   public QName getXmlTypeName(Reference javaType)
/*     */   {
/* 146 */     NonElement ti = (NonElement)this.refMap.get(javaType);
/*     */ 
/* 148 */     if (ti != null) {
/* 149 */       return ti.getTypeName();
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   private NonElement<TypeMirror, TypeElement> getXmlType(Reference r) {
/* 155 */     if (r == null) {
/* 156 */       throw new IllegalArgumentException();
/*     */     }
/* 158 */     XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)r.annotations.getAnnotation(XmlJavaTypeAdapter.class);
/* 159 */     XmlList xl = (XmlList)r.annotations.getAnnotation(XmlList.class);
/*     */ 
/* 162 */     Ref ref = new Ref(this.reader, this.types
/* 162 */       .getNavigator(), r.type, xjta, xl);
/*     */ 
/* 164 */     return this.types.getTypeInfo(ref);
/*     */   }
/*     */ 
/*     */   public void generateSchema(SchemaOutputResolver outputResolver, ErrorListener errorListener) throws IOException {
/* 168 */     getSchemaGenerator().write(outputResolver, errorListener);
/*     */   }
/*     */ 
/*     */   public void generateEpisodeFile(Result output) {
/* 172 */     getSchemaGenerator().writeEpisodeFile(ResultFactory.createSerializer(output));
/*     */   }
/*     */ 
/*     */   private synchronized XmlSchemaGenerator<TypeMirror, TypeElement, VariableElement, ExecutableElement> getSchemaGenerator() {
/* 176 */     if (this.xsdgen == null) {
/* 177 */       this.xsdgen = new XmlSchemaGenerator(this.types.getNavigator(), this.types);
/*     */ 
/* 179 */       for (Map.Entry e : this.additionalElementDecls.entrySet()) {
/* 180 */         Reference value = (Reference)e.getValue();
/* 181 */         if (value != null) {
/* 182 */           NonElement typeInfo = (NonElement)this.refMap.get(value);
/* 183 */           if (typeInfo == null)
/* 184 */             throw new IllegalArgumentException(e.getValue() + " was not specified to JavaCompiler.bind");
/* 185 */           TypeMirror type = value.type;
/* 186 */           this.xsdgen.add((QName)e.getKey(), (type == null) || (!type.getKind().isPrimitive()), typeInfo);
/*     */         } else {
/* 188 */           this.xsdgen.add((QName)e.getKey(), false, null);
/*     */         }
/*     */       }
/*     */     }
/* 192 */     return this.xsdgen;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.api.impl.j2s.JAXBModelImpl
 * JD-Core Version:    0.6.2
 */