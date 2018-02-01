/*     */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class JAXBStructuredType extends JAXBType
/*     */ {
/* 134 */   private List _elementMembers = new ArrayList();
/* 135 */   private Map _elementMembersByName = new HashMap();
/* 136 */   private Set _subtypes = null;
/* 137 */   private JAXBStructuredType _parentType = null;
/*     */ 
/*     */   public JAXBStructuredType(JAXBType jaxbType)
/*     */   {
/*  44 */     super(jaxbType);
/*     */   }
/*     */   public JAXBStructuredType() {
/*     */   }
/*     */ 
/*     */   public JAXBStructuredType(QName name) {
/*  50 */     this(name, null);
/*     */   }
/*     */ 
/*     */   public JAXBStructuredType(QName name, JavaStructureType javaType) {
/*  54 */     super(name, javaType);
/*     */   }
/*     */ 
/*     */   public void add(JAXBElementMember m) {
/*  58 */     if (this._elementMembersByName.containsKey(m.getName())) {
/*  59 */       throw new ModelException("model.uniqueness", new Object[0]);
/*     */     }
/*  61 */     this._elementMembers.add(m);
/*  62 */     if (m.getName() != null)
/*  63 */       this._elementMembersByName.put(m.getName().getLocalPart(), m);
/*     */   }
/*     */ 
/*     */   public Iterator getElementMembers()
/*     */   {
/*  68 */     return this._elementMembers.iterator();
/*     */   }
/*     */ 
/*     */   public int getElementMembersCount() {
/*  72 */     return this._elementMembers.size();
/*     */   }
/*     */ 
/*     */   public List getElementMembersList()
/*     */   {
/*  77 */     return this._elementMembers;
/*     */   }
/*     */ 
/*     */   public void setElementMembersList(List l)
/*     */   {
/*  82 */     this._elementMembers = l;
/*     */   }
/*     */ 
/*     */   public void addSubtype(JAXBStructuredType type) {
/*  86 */     if (this._subtypes == null) {
/*  87 */       this._subtypes = new HashSet();
/*     */     }
/*  89 */     this._subtypes.add(type);
/*  90 */     type.setParentType(this);
/*     */   }
/*     */ 
/*     */   public Iterator getSubtypes() {
/*  94 */     if (this._subtypes != null) {
/*  95 */       return this._subtypes.iterator();
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isUnwrapped()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   public Set getSubtypesSet() {
/* 108 */     return this._subtypes;
/*     */   }
/*     */ 
/*     */   public void setSubtypesSet(Set s)
/*     */   {
/* 113 */     this._subtypes = s;
/*     */   }
/*     */ 
/*     */   public void setParentType(JAXBStructuredType parent) {
/* 117 */     if ((this._parentType != null) && (parent != null))
/*     */     {
/* 119 */       if (!this._parentType
/* 119 */         .equals(parent))
/*     */       {
/* 124 */         throw new ModelException("model.parent.type.already.set", new Object[] { 
/* 122 */           getName().toString(), this._parentType
/* 123 */           .getName().toString(), parent
/* 124 */           .getName().toString() });
/*     */       }
/*     */     }
/* 126 */     this._parentType = parent;
/*     */   }
/*     */ 
/*     */   public JAXBStructuredType getParentType() {
/* 130 */     return this._parentType;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.jaxb.JAXBStructuredType
 * JD-Core Version:    0.6.2
 */