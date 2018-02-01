/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class IndexBuilder
/*     */ {
/*  54 */   private Map<Character, List<Doc>> indexmap = new HashMap();
/*     */   private boolean noDeprecated;
/*     */   private boolean classesOnly;
/*     */   private boolean javafx;
/*     */   protected final Object[] elements;
/*     */ 
/*     */   public IndexBuilder(Configuration paramConfiguration, boolean paramBoolean)
/*     */   {
/* 103 */     this(paramConfiguration, paramBoolean, false);
/*     */   }
/*     */ 
/*     */   public IndexBuilder(Configuration paramConfiguration, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 116 */     if (paramBoolean2)
/* 117 */       paramConfiguration.message.notice("doclet.Building_Index_For_All_Classes", new Object[0]);
/*     */     else {
/* 119 */       paramConfiguration.message.notice("doclet.Building_Index", new Object[0]);
/*     */     }
/* 121 */     this.noDeprecated = paramBoolean1;
/* 122 */     this.classesOnly = paramBoolean2;
/* 123 */     this.javafx = paramConfiguration.javafx;
/* 124 */     buildIndexMap(paramConfiguration.root);
/* 125 */     Set localSet = this.indexmap.keySet();
/* 126 */     this.elements = localSet.toArray();
/* 127 */     Arrays.sort(this.elements);
/*     */   }
/*     */ 
/*     */   protected void sortIndexMap()
/*     */   {
/* 135 */     for (Iterator localIterator = this.indexmap.values().iterator(); localIterator.hasNext(); )
/* 136 */       Collections.sort((List)localIterator.next(), new DocComparator(null));
/*     */   }
/*     */ 
/*     */   protected void buildIndexMap(RootDoc paramRootDoc)
/*     */   {
/* 148 */     PackageDoc[] arrayOfPackageDoc = paramRootDoc.specifiedPackages();
/* 149 */     ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 150 */     if (!this.classesOnly) {
/* 151 */       if (arrayOfPackageDoc.length == 0) {
/* 152 */         HashSet localHashSet = new HashSet();
/*     */ 
/* 154 */         for (int j = 0; j < arrayOfClassDoc.length; j++) {
/* 155 */           PackageDoc localPackageDoc = arrayOfClassDoc[j].containingPackage();
/* 156 */           if ((localPackageDoc != null) && (localPackageDoc.name().length() > 0)) {
/* 157 */             localHashSet.add(localPackageDoc);
/*     */           }
/*     */         }
/* 160 */         adjustIndexMap((Doc[])localHashSet.toArray(arrayOfPackageDoc));
/*     */       } else {
/* 162 */         adjustIndexMap(arrayOfPackageDoc);
/*     */       }
/*     */     }
/* 165 */     adjustIndexMap(arrayOfClassDoc);
/* 166 */     if (!this.classesOnly) {
/* 167 */       for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 168 */         if (shouldAddToIndexMap(arrayOfClassDoc[i])) {
/* 169 */           putMembersInIndexMap(arrayOfClassDoc[i]);
/*     */         }
/*     */       }
/*     */     }
/* 173 */     sortIndexMap();
/*     */   }
/*     */ 
/*     */   protected void putMembersInIndexMap(ClassDoc paramClassDoc)
/*     */   {
/* 183 */     adjustIndexMap(paramClassDoc.fields());
/* 184 */     adjustIndexMap(paramClassDoc.methods());
/* 185 */     adjustIndexMap(paramClassDoc.constructors());
/*     */   }
/*     */ 
/*     */   protected void adjustIndexMap(Doc[] paramArrayOfDoc)
/*     */   {
/* 197 */     for (int i = 0; i < paramArrayOfDoc.length; i++)
/* 198 */       if (shouldAddToIndexMap(paramArrayOfDoc[i])) {
/* 199 */         String str = paramArrayOfDoc[i].name();
/*     */ 
/* 202 */         char c = str.length() == 0 ? '*' : 
/* 202 */           Character.toUpperCase(str
/* 202 */           .charAt(0));
/*     */ 
/* 203 */         Character localCharacter = new Character(c);
/* 204 */         Object localObject = (List)this.indexmap.get(localCharacter);
/* 205 */         if (localObject == null) {
/* 206 */           localObject = new ArrayList();
/* 207 */           this.indexmap.put(localCharacter, localObject);
/*     */         }
/* 209 */         ((List)localObject).add(paramArrayOfDoc[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected boolean shouldAddToIndexMap(Doc paramDoc)
/*     */   {
/* 218 */     if ((this.javafx) && 
/* 219 */       (paramDoc.tags("treatAsPrivate").length > 0)) {
/* 220 */       return false;
/*     */     }
/*     */ 
/* 224 */     if ((paramDoc instanceof PackageDoc))
/*     */     {
/* 227 */       return (!this.noDeprecated) || (!Util.isDeprecated(paramDoc));
/*     */     }
/*     */ 
/* 234 */     return (!this.noDeprecated) || (
/* 233 */       (!Util.isDeprecated(paramDoc)) && 
/* 234 */       (!Util.isDeprecated(((ProgramElementDoc)paramDoc)
/* 234 */       .containingPackage())));
/*     */   }
/*     */ 
/*     */   public Map<Character, List<Doc>> getIndexMap()
/*     */   {
/* 243 */     return this.indexmap;
/*     */   }
/*     */ 
/*     */   public List<Doc> getMemberList(Character paramCharacter)
/*     */   {
/* 253 */     return (List)this.indexmap.get(paramCharacter);
/*     */   }
/*     */ 
/*     */   public Object[] elements()
/*     */   {
/* 260 */     return this.elements;
/*     */   }
/*     */ 
/*     */   private class DocComparator
/*     */     implements Comparator<Doc>
/*     */   {
/*     */     private DocComparator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(Doc paramDoc1, Doc paramDoc2)
/*     */     {
/*  80 */       String str1 = paramDoc1.name();
/*  81 */       String str2 = paramDoc2.name();
/*     */       int i;
/*  83 */       if ((i = str1.compareToIgnoreCase(str2)) != 0)
/*  84 */         return i;
/*  85 */       if (((paramDoc1 instanceof ProgramElementDoc)) && ((paramDoc2 instanceof ProgramElementDoc))) {
/*  86 */         str1 = ((ProgramElementDoc)paramDoc1).qualifiedName();
/*  87 */         str2 = ((ProgramElementDoc)paramDoc2).qualifiedName();
/*  88 */         return str1.compareToIgnoreCase(str2);
/*     */       }
/*  90 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.IndexBuilder
 * JD-Core Version:    0.6.2
 */