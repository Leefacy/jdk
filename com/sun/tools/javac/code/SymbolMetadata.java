/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.comp.Annotate.AnnotateRepeatedContext;
/*     */ import com.sun.tools.javac.comp.Annotate.Worker;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class SymbolMetadata
/*     */ {
/*  70 */   private static final List<Attribute.Compound> DECL_NOT_STARTED = List.of(null);
/*  71 */   private static final List<Attribute.Compound> DECL_IN_PROGRESS = List.of(null);
/*     */ 
/*  76 */   private List<Attribute.Compound> attributes = DECL_NOT_STARTED;
/*     */ 
/*  82 */   private List<Attribute.TypeCompound> type_attributes = List.nil();
/*     */ 
/*  88 */   private List<Attribute.TypeCompound> init_type_attributes = List.nil();
/*     */ 
/*  94 */   private List<Attribute.TypeCompound> clinit_type_attributes = List.nil();
/*     */   private final Symbol sym;
/*     */ 
/*     */   public SymbolMetadata(Symbol paramSymbol)
/*     */   {
/* 102 */     this.sym = paramSymbol;
/*     */   }
/*     */ 
/*     */   public List<Attribute.Compound> getDeclarationAttributes() {
/* 106 */     return filterDeclSentinels(this.attributes);
/*     */   }
/*     */ 
/*     */   public List<Attribute.TypeCompound> getTypeAttributes() {
/* 110 */     return this.type_attributes;
/*     */   }
/*     */ 
/*     */   public List<Attribute.TypeCompound> getInitTypeAttributes() {
/* 114 */     return this.init_type_attributes;
/*     */   }
/*     */ 
/*     */   public List<Attribute.TypeCompound> getClassInitTypeAttributes() {
/* 118 */     return this.clinit_type_attributes;
/*     */   }
/*     */ 
/*     */   public void setDeclarationAttributes(List<Attribute.Compound> paramList) {
/* 122 */     Assert.check((pendingCompletion()) || (!isStarted()));
/* 123 */     if (paramList == null) {
/* 124 */       throw new NullPointerException();
/*     */     }
/* 126 */     this.attributes = paramList;
/*     */   }
/*     */ 
/*     */   public void setTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 130 */     if (paramList == null) {
/* 131 */       throw new NullPointerException();
/*     */     }
/* 133 */     this.type_attributes = paramList;
/*     */   }
/*     */ 
/*     */   public void setInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 137 */     if (paramList == null) {
/* 138 */       throw new NullPointerException();
/*     */     }
/* 140 */     this.init_type_attributes = paramList;
/*     */   }
/*     */ 
/*     */   public void setClassInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 144 */     if (paramList == null) {
/* 145 */       throw new NullPointerException();
/*     */     }
/* 147 */     this.clinit_type_attributes = paramList;
/*     */   }
/*     */ 
/*     */   public void setAttributes(SymbolMetadata paramSymbolMetadata) {
/* 151 */     if (paramSymbolMetadata == null) {
/* 152 */       throw new NullPointerException();
/*     */     }
/* 154 */     setDeclarationAttributes(paramSymbolMetadata.getDeclarationAttributes());
/* 155 */     setTypeAttributes(paramSymbolMetadata.getTypeAttributes());
/* 156 */     setInitTypeAttributes(paramSymbolMetadata.getInitTypeAttributes());
/* 157 */     setClassInitTypeAttributes(paramSymbolMetadata.getClassInitTypeAttributes());
/*     */   }
/*     */ 
/*     */   public void setDeclarationAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.Compound> paramAnnotateRepeatedContext) {
/* 161 */     Assert.check((pendingCompletion()) || ((!isStarted()) && (this.sym.kind == 1)));
/* 162 */     setDeclarationAttributes(getAttributesForCompletion(paramAnnotateRepeatedContext));
/*     */   }
/*     */ 
/*     */   public void appendTypeAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.TypeCompound> paramAnnotateRepeatedContext) {
/* 166 */     appendUniqueTypes(getAttributesForCompletion(paramAnnotateRepeatedContext));
/*     */   }
/*     */ 
/*     */   private <T extends Attribute.Compound> List<T> getAttributesForCompletion(final Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext)
/*     */   {
/* 172 */     Map localMap = paramAnnotateRepeatedContext.annotated;
/* 173 */     int i = 0;
/* 174 */     List localList = List.nil();
/* 175 */     for (ListBuffer localListBuffer : localMap.values()) {
/* 176 */       if (localListBuffer.size() == 1) {
/* 177 */         localList = localList.prepend(localListBuffer.first());
/*     */       }
/*     */       else
/*     */       {
/* 183 */         Placeholder localPlaceholder2 = new Placeholder(paramAnnotateRepeatedContext, localListBuffer.toList(), this.sym);
/* 184 */         Placeholder localPlaceholder1 = localPlaceholder2;
/* 185 */         localList = localList.prepend(localPlaceholder1);
/* 186 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 190 */     if (i != 0)
/*     */     {
/* 208 */       paramAnnotateRepeatedContext.annotateRepeated(new Annotate.Worker()
/*     */       {
/*     */         public String toString() {
/* 211 */           return "repeated annotation pass of: " + SymbolMetadata.this.sym + " in: " + SymbolMetadata.this.sym.owner;
/*     */         }
/*     */ 
/*     */         public void run()
/*     */         {
/* 216 */           SymbolMetadata.this.complete(paramAnnotateRepeatedContext);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 221 */     return localList.reverse();
/*     */   }
/*     */ 
/*     */   public SymbolMetadata reset() {
/* 225 */     this.attributes = DECL_IN_PROGRESS;
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 232 */     return (!isStarted()) || 
/* 231 */       (pendingCompletion()) || 
/* 232 */       (this.attributes
/* 232 */       .isEmpty());
/*     */   }
/*     */ 
/*     */   public boolean isTypesEmpty() {
/* 236 */     return this.type_attributes.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean pendingCompletion() {
/* 240 */     return this.attributes == DECL_IN_PROGRESS;
/*     */   }
/*     */ 
/*     */   public SymbolMetadata append(List<Attribute.Compound> paramList) {
/* 244 */     this.attributes = filterDeclSentinels(this.attributes);
/*     */ 
/* 246 */     if (!paramList.isEmpty())
/*     */     {
/* 248 */       if (this.attributes.isEmpty())
/* 249 */         this.attributes = paramList;
/*     */       else
/* 251 */         this.attributes = this.attributes.appendList(paramList);
/*     */     }
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */   public SymbolMetadata appendUniqueTypes(List<Attribute.TypeCompound> paramList) {
/* 257 */     if (!paramList.isEmpty())
/*     */     {
/* 259 */       if (this.type_attributes.isEmpty()) {
/* 260 */         this.type_attributes = paramList;
/*     */       }
/*     */       else
/*     */       {
/* 264 */         for (Attribute.TypeCompound localTypeCompound : paramList)
/* 265 */           if (!this.type_attributes.contains(localTypeCompound))
/* 266 */             this.type_attributes = this.type_attributes.append(localTypeCompound);
/*     */       }
/*     */     }
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */   public SymbolMetadata appendInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 273 */     if (!paramList.isEmpty())
/*     */     {
/* 275 */       if (this.init_type_attributes.isEmpty())
/* 276 */         this.init_type_attributes = paramList;
/*     */       else
/* 278 */         this.init_type_attributes = this.init_type_attributes.appendList(paramList);
/*     */     }
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */   public SymbolMetadata appendClassInitTypeAttributes(List<Attribute.TypeCompound> paramList) {
/* 284 */     if (!paramList.isEmpty())
/*     */     {
/* 286 */       if (this.clinit_type_attributes.isEmpty())
/* 287 */         this.clinit_type_attributes = paramList;
/*     */       else
/* 289 */         this.clinit_type_attributes = this.clinit_type_attributes.appendList(paramList);
/*     */     }
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */   public SymbolMetadata prepend(List<Attribute.Compound> paramList) {
/* 295 */     this.attributes = filterDeclSentinels(this.attributes);
/*     */ 
/* 297 */     if (!paramList.isEmpty())
/*     */     {
/* 299 */       if (this.attributes.isEmpty())
/* 300 */         this.attributes = paramList;
/*     */       else
/* 302 */         this.attributes = this.attributes.prependList(paramList);
/*     */     }
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */   private List<Attribute.Compound> filterDeclSentinels(List<Attribute.Compound> paramList)
/*     */   {
/* 309 */     return (paramList == DECL_IN_PROGRESS) || (paramList == DECL_NOT_STARTED) ? 
/* 309 */       List.nil() : paramList;
/*     */   }
/*     */ 
/*     */   private boolean isStarted()
/*     */   {
/* 314 */     return this.attributes != DECL_NOT_STARTED;
/*     */   }
/*     */ 
/*     */   private List<Attribute.Compound> getPlaceholders() {
/* 318 */     List localList = List.nil();
/* 319 */     for (Attribute.Compound localCompound : filterDeclSentinels(this.attributes)) {
/* 320 */       if ((localCompound instanceof Placeholder)) {
/* 321 */         localList = localList.prepend(localCompound);
/*     */       }
/*     */     }
/* 324 */     return localList.reverse();
/*     */   }
/*     */ 
/*     */   private List<Attribute.TypeCompound> getTypePlaceholders() {
/* 328 */     List localList = List.nil();
/* 329 */     for (Attribute.TypeCompound localTypeCompound : this.type_attributes) {
/* 330 */       if ((localTypeCompound instanceof Placeholder)) {
/* 331 */         localList = localList.prepend(localTypeCompound);
/*     */       }
/*     */     }
/* 334 */     return localList.reverse();
/*     */   }
/*     */ 
/*     */   private <T extends Attribute.Compound> void complete(Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext)
/*     */   {
/* 341 */     Log localLog = paramAnnotateRepeatedContext.log;
/* 342 */     Env localEnv = paramAnnotateRepeatedContext.env;
/* 343 */     JavaFileObject localJavaFileObject = localLog.useSource(localEnv.toplevel.sourcefile);
/*     */     try
/*     */     {
/*     */       List localList;
/*     */       Iterator localIterator;
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 346 */       if (paramAnnotateRepeatedContext.isTypeCompound) {
/* 347 */         Assert.check(!isTypesEmpty());
/*     */ 
/* 349 */         if (isTypesEmpty()) {
/* 350 */           return;
/*     */         }
/*     */ 
/* 353 */         localList = List.nil();
/* 354 */         for (localIterator = getTypeAttributes().iterator(); localIterator.hasNext(); ) { localObject1 = (Attribute.TypeCompound)localIterator.next();
/* 355 */           if ((localObject1 instanceof Placeholder))
/*     */           {
/* 357 */             localObject2 = (Placeholder)localObject1;
/* 358 */             Attribute.TypeCompound localTypeCompound = (Attribute.TypeCompound)replaceOne((Placeholder)localObject2, ((Placeholder)localObject2).getRepeatedContext());
/*     */ 
/* 360 */             if (null != localTypeCompound)
/* 361 */               localList = localList.prepend(localTypeCompound);
/*     */           }
/*     */           else {
/* 364 */             localList = localList.prepend(localObject1);
/*     */           }
/*     */         }
/*     */ 
/* 368 */         this.type_attributes = localList.reverse();
/*     */ 
/* 370 */         Assert.check(getTypePlaceholders().isEmpty());
/*     */       } else {
/* 372 */         Assert.check(!pendingCompletion());
/*     */ 
/* 374 */         if (isEmpty()) {
/* 375 */           return;
/*     */         }
/*     */ 
/* 378 */         localList = List.nil();
/* 379 */         for (localIterator = getDeclarationAttributes().iterator(); localIterator.hasNext(); ) { localObject1 = (Attribute.Compound)localIterator.next();
/* 380 */           if ((localObject1 instanceof Placeholder))
/*     */           {
/* 382 */             localObject2 = replaceOne((Placeholder)localObject1, paramAnnotateRepeatedContext);
/*     */ 
/* 384 */             if (null != localObject2)
/* 385 */               localList = localList.prepend(localObject2);
/*     */           }
/*     */           else {
/* 388 */             localList = localList.prepend(localObject1);
/*     */           }
/*     */         }
/*     */ 
/* 392 */         this.attributes = localList.reverse();
/*     */ 
/* 394 */         Assert.check(getPlaceholders().isEmpty());
/*     */       }
/*     */     } finally {
/* 397 */       localLog.useSource(localJavaFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends Attribute.Compound> T replaceOne(Placeholder<T> paramPlaceholder, Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext) {
/* 402 */     Log localLog = paramAnnotateRepeatedContext.log;
/*     */ 
/* 405 */     Attribute.Compound localCompound = paramAnnotateRepeatedContext.processRepeatedAnnotations(paramPlaceholder.getPlaceholderFor(), this.sym);
/*     */ 
/* 407 */     if (localCompound != null)
/*     */     {
/* 411 */       ListBuffer localListBuffer = (ListBuffer)paramAnnotateRepeatedContext.annotated.get(localCompound.type.tsym);
/* 412 */       if (localListBuffer != null) {
/* 413 */         localLog.error((JCDiagnostic.DiagnosticPosition)paramAnnotateRepeatedContext.pos.get(localListBuffer.first()), "invalid.repeatable.annotation.repeated.and.container.present", new Object[] { 
/* 414 */           ((Attribute.Compound)localListBuffer
/* 414 */           .first()).type.tsym });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 419 */     return localCompound;
/*     */   }
/*     */   private static class Placeholder<T extends Attribute.Compound> extends Attribute.TypeCompound {
/*     */     private final Annotate.AnnotateRepeatedContext<T> ctx;
/*     */     private final List<T> placeholderFor;
/*     */     private final Symbol on;
/*     */ 
/*     */     public Placeholder(Annotate.AnnotateRepeatedContext<T> paramAnnotateRepeatedContext, List<T> paramList, Symbol paramSymbol) {
/* 429 */       super(List.nil(), paramAnnotateRepeatedContext.isTypeCompound ? ((Attribute.TypeCompound)paramList.head).position : new TypeAnnotationPosition());
/*     */ 
/* 433 */       this.ctx = paramAnnotateRepeatedContext;
/* 434 */       this.placeholderFor = paramList;
/* 435 */       this.on = paramSymbol;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 440 */       return "<placeholder: " + this.placeholderFor + " on: " + this.on + ">";
/*     */     }
/*     */ 
/*     */     public List<T> getPlaceholderFor() {
/* 444 */       return this.placeholderFor;
/*     */     }
/*     */ 
/*     */     public Annotate.AnnotateRepeatedContext<T> getRepeatedContext() {
/* 448 */       return this.ctx;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.SymbolMetadata
 * JD-Core Version:    0.6.2
 */