/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.util.DocSourcePositions;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.SourcePositions;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ public class Env
/*     */ {
/*     */   final Messages messages;
/*  89 */   int implicitHeaderLevel = 0;
/*     */   Set<String> customTags;
/*     */   DocTrees trees;
/*     */   Elements elements;
/*     */   Types types;
/*     */   TypeMirror java_lang_Error;
/*     */   TypeMirror java_lang_RuntimeException;
/*     */   TypeMirror java_lang_Throwable;
/*     */   TypeMirror java_lang_Void;
/*     */   TreePath currPath;
/*     */   Element currElement;
/*     */   DocCommentTree currDocComment;
/*     */   AccessKind currAccess;
/*     */   Set<? extends ExecutableElement> currOverriddenMethods;
/*     */ 
/*     */   Env()
/*     */   {
/* 121 */     this.messages = new Messages(this);
/*     */   }
/*     */ 
/*     */   void init(JavacTask paramJavacTask) {
/* 125 */     init(DocTrees.instance(paramJavacTask), paramJavacTask.getElements(), paramJavacTask.getTypes());
/*     */   }
/*     */ 
/*     */   void init(DocTrees paramDocTrees, Elements paramElements, Types paramTypes) {
/* 129 */     this.trees = paramDocTrees;
/* 130 */     this.elements = paramElements;
/* 131 */     this.types = paramTypes;
/* 132 */     this.java_lang_Error = paramElements.getTypeElement("java.lang.Error").asType();
/* 133 */     this.java_lang_RuntimeException = paramElements.getTypeElement("java.lang.RuntimeException").asType();
/* 134 */     this.java_lang_Throwable = paramElements.getTypeElement("java.lang.Throwable").asType();
/* 135 */     this.java_lang_Void = paramElements.getTypeElement("java.lang.Void").asType();
/*     */   }
/*     */ 
/*     */   void setImplicitHeaders(int paramInt) {
/* 139 */     this.implicitHeaderLevel = paramInt;
/*     */   }
/*     */ 
/*     */   void setCustomTags(String paramString) {
/* 143 */     this.customTags = new LinkedHashSet();
/* 144 */     for (String str : paramString.split(","))
/* 145 */       if (!str.isEmpty())
/* 146 */         this.customTags.add(str);
/*     */   }
/*     */ 
/*     */   void setCurrent(TreePath paramTreePath, DocCommentTree paramDocCommentTree)
/*     */   {
/* 152 */     this.currPath = paramTreePath;
/* 153 */     this.currDocComment = paramDocCommentTree;
/* 154 */     this.currElement = this.trees.getElement(this.currPath);
/* 155 */     this.currOverriddenMethods = ((JavacTypes)this.types).getOverriddenMethods(this.currElement);
/*     */ 
/* 157 */     AccessKind localAccessKind = AccessKind.PUBLIC;
/* 158 */     for (TreePath localTreePath = paramTreePath; localTreePath != null; localTreePath = localTreePath.getParentPath()) {
/* 159 */       Element localElement = this.trees.getElement(localTreePath);
/* 160 */       if ((localElement != null) && (localElement.getKind() != ElementKind.PACKAGE)) {
/* 161 */         localAccessKind = (AccessKind)min(localAccessKind, AccessKind.of(localElement.getModifiers()));
/*     */       }
/*     */     }
/* 164 */     this.currAccess = localAccessKind;
/*     */   }
/*     */ 
/*     */   AccessKind getAccessKind() {
/* 168 */     return this.currAccess;
/*     */   }
/*     */ 
/*     */   long getPos(TreePath paramTreePath) {
/* 172 */     return ((JCTree)paramTreePath.getLeaf()).pos;
/*     */   }
/*     */ 
/*     */   long getStartPos(TreePath paramTreePath) {
/* 176 */     DocSourcePositions localDocSourcePositions = this.trees.getSourcePositions();
/* 177 */     return localDocSourcePositions.getStartPosition(paramTreePath.getCompilationUnit(), paramTreePath.getLeaf());
/*     */   }
/*     */ 
/*     */   private <T extends Comparable<T>> T min(T paramT1, T paramT2)
/*     */   {
/* 183 */     return paramT1
/* 183 */       .compareTo(paramT2) <= 0 ? 
/* 183 */       paramT1 : paramT2 == null ? paramT1 : paramT1 == null ? paramT2 : 
/* 183 */       paramT2;
/*     */   }
/*     */ 
/*     */   public static enum AccessKind
/*     */   {
/*  63 */     PRIVATE, 
/*  64 */     PACKAGE, 
/*  65 */     PROTECTED, 
/*  66 */     PUBLIC;
/*     */ 
/*     */     static boolean accepts(String paramString) {
/*  69 */       for (AccessKind localAccessKind : values())
/*  70 */         if (paramString.equals(StringUtils.toLowerCase(localAccessKind.name()))) return true;
/*  71 */       return false;
/*     */     }
/*     */ 
/*     */     static AccessKind of(Set<Modifier> paramSet) {
/*  75 */       if (paramSet.contains(Modifier.PUBLIC))
/*  76 */         return PUBLIC;
/*  77 */       if (paramSet.contains(Modifier.PROTECTED))
/*  78 */         return PROTECTED;
/*  79 */       if (paramSet.contains(Modifier.PRIVATE)) {
/*  80 */         return PRIVATE;
/*     */       }
/*  82 */       return PACKAGE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.Env
 * JD-Core Version:    0.6.2
 */