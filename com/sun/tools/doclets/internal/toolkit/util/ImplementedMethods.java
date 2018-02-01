/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ImplementedMethods
/*     */ {
/*  46 */   private Map<MethodDoc, Type> interfaces = new HashMap();
/*  47 */   private List<MethodDoc> methlist = new ArrayList();
/*     */   private Configuration configuration;
/*     */   private final ClassDoc classdoc;
/*     */   private final MethodDoc method;
/*     */ 
/*     */   public ImplementedMethods(MethodDoc paramMethodDoc, Configuration paramConfiguration)
/*     */   {
/*  53 */     this.method = paramMethodDoc;
/*  54 */     this.configuration = paramConfiguration;
/*  55 */     this.classdoc = paramMethodDoc.containingClass();
/*     */   }
/*     */ 
/*     */   public MethodDoc[] build(boolean paramBoolean)
/*     */   {
/*  71 */     buildImplementedMethodList(paramBoolean);
/*  72 */     return (MethodDoc[])this.methlist.toArray(new MethodDoc[this.methlist.size()]);
/*     */   }
/*     */ 
/*     */   public MethodDoc[] build() {
/*  76 */     return build(true);
/*     */   }
/*     */ 
/*     */   public Type getMethodHolder(MethodDoc paramMethodDoc) {
/*  80 */     return (Type)this.interfaces.get(paramMethodDoc);
/*     */   }
/*     */ 
/*     */   private void buildImplementedMethodList(boolean paramBoolean)
/*     */   {
/*  91 */     List localList = Util.getAllInterfaces(this.classdoc, this.configuration, paramBoolean);
/*  92 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/*  93 */       Type localType = (Type)localIterator.next();
/*  94 */       MethodDoc localMethodDoc = Util.findMethod(localType.asClassDoc(), this.method);
/*  95 */       if (localMethodDoc != null) {
/*  96 */         removeOverriddenMethod(localMethodDoc);
/*  97 */         if (!overridingMethodFound(localMethodDoc)) {
/*  98 */           this.methlist.add(localMethodDoc);
/*  99 */           this.interfaces.put(localMethodDoc, localType);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeOverriddenMethod(MethodDoc paramMethodDoc)
/*     */   {
/* 113 */     ClassDoc localClassDoc1 = paramMethodDoc.overriddenClass();
/* 114 */     if (localClassDoc1 != null)
/* 115 */       for (int i = 0; i < this.methlist.size(); i++) {
/* 116 */         ClassDoc localClassDoc2 = ((MethodDoc)this.methlist.get(i)).containingClass();
/* 117 */         if ((localClassDoc2 == localClassDoc1) || (localClassDoc1.subclassOf(localClassDoc2))) {
/* 118 */           this.methlist.remove(i);
/* 119 */           return;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean overridingMethodFound(MethodDoc paramMethodDoc)
/*     */   {
/* 134 */     ClassDoc localClassDoc1 = paramMethodDoc.containingClass();
/* 135 */     for (int i = 0; i < this.methlist.size(); i++) {
/* 136 */       MethodDoc localMethodDoc = (MethodDoc)this.methlist.get(i);
/* 137 */       if (localClassDoc1 == localMethodDoc.containingClass())
/*     */       {
/* 139 */         return true;
/*     */       }
/* 141 */       ClassDoc localClassDoc2 = localMethodDoc.overriddenClass();
/* 142 */       if (localClassDoc2 != null)
/*     */       {
/* 145 */         if ((localClassDoc2 == localClassDoc1) || (localClassDoc2.subclassOf(localClassDoc1)))
/* 146 */           return true;
/*     */       }
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods
 * JD-Core Version:    0.6.2
 */