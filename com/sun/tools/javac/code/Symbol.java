/*      */ package com.sun.tools.javac.code;
/*      */ 
/*      */ import com.sun.tools.javac.comp.Annotate.AnnotateRepeatedContext;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Enter;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.jvm.Code;
/*      */ import com.sun.tools.javac.jvm.Pool;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Constants;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Name.Table;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.annotation.Inherited;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ import javax.lang.model.element.ElementVisitor;
/*      */ import javax.lang.model.element.ExecutableElement;
/*      */ import javax.lang.model.element.Modifier;
/*      */ import javax.lang.model.element.NestingKind;
/*      */ import javax.lang.model.element.PackageElement;
/*      */ import javax.lang.model.element.TypeElement;
/*      */ import javax.lang.model.element.TypeParameterElement;
/*      */ import javax.lang.model.element.VariableElement;
/*      */ import javax.tools.JavaFileObject;
/*      */ 
/*      */ public abstract class Symbol extends AnnoConstruct
/*      */   implements Element
/*      */ {
/*      */   public int kind;
/*      */   public long flags_field;
/*      */   public Name name;
/*      */   public Type type;
/*      */   public Symbol owner;
/*      */   public Completer completer;
/*      */   public Type erasure_field;
/*      */   protected SymbolMetadata metadata;
/*      */ 
/*      */   public long flags()
/*      */   {
/*   76 */     return this.flags_field;
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.Compound> getRawAttributes()
/*      */   {
/*  113 */     return this.metadata == null ? 
/*  112 */       com.sun.tools.javac.util.List.nil() : this.metadata
/*  113 */       .getDeclarationAttributes();
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.TypeCompound> getRawTypeAttributes()
/*      */   {
/*  123 */     return this.metadata == null ? 
/*  122 */       com.sun.tools.javac.util.List.nil() : this.metadata
/*  123 */       .getTypeAttributes();
/*      */   }
/*      */ 
/*      */   public Attribute.Compound attribute(Symbol paramSymbol)
/*      */   {
/*  128 */     for (Attribute.Compound localCompound : getRawAttributes()) {
/*  129 */       if (localCompound.type.tsym == paramSymbol) return localCompound;
/*      */     }
/*  131 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean annotationsPendingCompletion() {
/*  135 */     return this.metadata == null ? false : this.metadata.pendingCompletion();
/*      */   }
/*      */ 
/*      */   public void appendAttributes(com.sun.tools.javac.util.List<Attribute.Compound> paramList) {
/*  139 */     if (paramList.nonEmpty())
/*  140 */       initedMetadata().append(paramList);
/*      */   }
/*      */ 
/*      */   public void appendClassInitTypeAttributes(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList)
/*      */   {
/*  145 */     if (paramList.nonEmpty())
/*  146 */       initedMetadata().appendClassInitTypeAttributes(paramList);
/*      */   }
/*      */ 
/*      */   public void appendInitTypeAttributes(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList)
/*      */   {
/*  151 */     if (paramList.nonEmpty())
/*  152 */       initedMetadata().appendInitTypeAttributes(paramList);
/*      */   }
/*      */ 
/*      */   public void appendTypeAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.TypeCompound> paramAnnotateRepeatedContext)
/*      */   {
/*  157 */     initedMetadata().appendTypeAttributesWithCompletion(paramAnnotateRepeatedContext);
/*      */   }
/*      */ 
/*      */   public void appendUniqueTypeAttributes(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList) {
/*  161 */     if (paramList.nonEmpty())
/*  162 */       initedMetadata().appendUniqueTypes(paramList);
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.TypeCompound> getClassInitTypeAttributes()
/*      */   {
/*  169 */     return this.metadata == null ? 
/*  168 */       com.sun.tools.javac.util.List.nil() : this.metadata
/*  169 */       .getClassInitTypeAttributes();
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.TypeCompound> getInitTypeAttributes()
/*      */   {
/*  175 */     return this.metadata == null ? 
/*  174 */       com.sun.tools.javac.util.List.nil() : this.metadata
/*  175 */       .getInitTypeAttributes();
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.Compound> getDeclarationAttributes()
/*      */   {
/*  181 */     return this.metadata == null ? 
/*  180 */       com.sun.tools.javac.util.List.nil() : this.metadata
/*  181 */       .getDeclarationAttributes();
/*      */   }
/*      */ 
/*      */   public boolean hasAnnotations() {
/*  185 */     return (this.metadata != null) && (!this.metadata.isEmpty());
/*      */   }
/*      */ 
/*      */   public boolean hasTypeAnnotations() {
/*  189 */     return (this.metadata != null) && (!this.metadata.isTypesEmpty());
/*      */   }
/*      */ 
/*      */   public void prependAttributes(com.sun.tools.javac.util.List<Attribute.Compound> paramList) {
/*  193 */     if (paramList.nonEmpty())
/*  194 */       initedMetadata().prepend(paramList);
/*      */   }
/*      */ 
/*      */   public void resetAnnotations()
/*      */   {
/*  199 */     initedMetadata().reset();
/*      */   }
/*      */ 
/*      */   public void setAttributes(Symbol paramSymbol) {
/*  203 */     if ((this.metadata != null) || (paramSymbol.metadata != null))
/*  204 */       initedMetadata().setAttributes(paramSymbol.metadata);
/*      */   }
/*      */ 
/*      */   public void setDeclarationAttributes(com.sun.tools.javac.util.List<Attribute.Compound> paramList)
/*      */   {
/*  209 */     if ((this.metadata != null) || (paramList.nonEmpty()))
/*  210 */       initedMetadata().setDeclarationAttributes(paramList);
/*      */   }
/*      */ 
/*      */   public void setDeclarationAttributesWithCompletion(Annotate.AnnotateRepeatedContext<Attribute.Compound> paramAnnotateRepeatedContext)
/*      */   {
/*  215 */     initedMetadata().setDeclarationAttributesWithCompletion(paramAnnotateRepeatedContext);
/*      */   }
/*      */ 
/*      */   public void setTypeAttributes(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList) {
/*  219 */     if ((this.metadata != null) || (paramList.nonEmpty())) {
/*  220 */       if (this.metadata == null)
/*  221 */         this.metadata = new SymbolMetadata(this);
/*  222 */       this.metadata.setTypeAttributes(paramList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private SymbolMetadata initedMetadata() {
/*  227 */     if (this.metadata == null)
/*  228 */       this.metadata = new SymbolMetadata(this);
/*  229 */     return this.metadata;
/*      */   }
/*      */ 
/*      */   public SymbolMetadata getMetadata()
/*      */   {
/*  234 */     return this.metadata;
/*      */   }
/*      */ 
/*      */   public Symbol(int paramInt, long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */   {
/*  242 */     this.kind = paramInt;
/*  243 */     this.flags_field = paramLong;
/*  244 */     this.type = paramType;
/*  245 */     this.owner = paramSymbol;
/*  246 */     this.completer = null;
/*  247 */     this.erasure_field = null;
/*  248 */     this.name = paramName;
/*      */   }
/*      */ 
/*      */   public Symbol clone(Symbol paramSymbol)
/*      */   {
/*  255 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/*  259 */     return paramVisitor.visitSymbol(this, paramP);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  266 */     return this.name.toString();
/*      */   }
/*      */ 
/*      */   public Symbol location()
/*      */   {
/*  276 */     if ((this.owner.name == null) || ((this.owner.name.isEmpty()) && 
/*  277 */       ((this.owner
/*  277 */       .flags() & 0x100000) == 0L) && (this.owner.kind != 1) && (this.owner.kind != 2))) {
/*  278 */       return null;
/*      */     }
/*  280 */     return this.owner;
/*      */   }
/*      */ 
/*      */   public Symbol location(Type paramType, Types paramTypes) {
/*  284 */     if ((this.owner.name == null) || (this.owner.name.isEmpty())) {
/*  285 */       return location();
/*      */     }
/*  287 */     if (this.owner.type.hasTag(TypeTag.CLASS)) {
/*  288 */       Type localType = paramTypes.asOuterSuper(paramType, this.owner);
/*  289 */       if (localType != null) return localType.tsym;
/*      */     }
/*  291 */     return this.owner;
/*      */   }
/*      */ 
/*      */   public Symbol baseSymbol() {
/*  295 */     return this;
/*      */   }
/*      */ 
/*      */   public Type erasure(Types paramTypes)
/*      */   {
/*  301 */     if (this.erasure_field == null)
/*  302 */       this.erasure_field = paramTypes.erasure(this.type);
/*  303 */     return this.erasure_field;
/*      */   }
/*      */ 
/*      */   public Type externalType(Types paramTypes)
/*      */   {
/*  311 */     Type localType1 = erasure(paramTypes);
/*  312 */     if ((this.name == this.name.table.names.init) && (this.owner.hasOuterInstance())) {
/*  313 */       Type localType2 = paramTypes.erasure(this.owner.type.getEnclosingType());
/*      */ 
/*  316 */       return new Type.MethodType(localType1.getParameterTypes().prepend(localType2), localType1
/*  315 */         .getReturnType(), localType1
/*  316 */         .getThrownTypes(), localType1.tsym);
/*      */     }
/*      */ 
/*  319 */     return localType1;
/*      */   }
/*      */ 
/*      */   public boolean isDeprecated()
/*      */   {
/*  324 */     return (this.flags_field & 0x20000) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isStatic()
/*      */   {
/*  330 */     return ((flags() & 0x8) != 0L) || (
/*  330 */       ((this.owner
/*  330 */       .flags() & 0x200) != 0L) && (this.kind != 16) && (this.name != this.name.table.names._this));
/*      */   }
/*      */ 
/*      */   public boolean isInterface()
/*      */   {
/*  335 */     return (flags() & 0x200) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isPrivate() {
/*  339 */     return (this.flags_field & 0x7) == 2L;
/*      */   }
/*      */ 
/*      */   public boolean isEnum() {
/*  343 */     return (flags() & 0x4000) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isLocal()
/*      */   {
/*  352 */     if ((this.owner.kind & 0x14) == 0) if (this.owner.kind != 2)
/*      */         break label38;
/*  354 */     label38: return this.owner
/*  354 */       .isLocal();
/*      */   }
/*      */ 
/*      */   public boolean isAnonymous()
/*      */   {
/*  361 */     return this.name.isEmpty();
/*      */   }
/*      */ 
/*      */   public boolean isConstructor()
/*      */   {
/*  367 */     return this.name == this.name.table.names.init;
/*      */   }
/*      */ 
/*      */   public Name getQualifiedName()
/*      */   {
/*  375 */     return this.name;
/*      */   }
/*      */ 
/*      */   public Name flatName()
/*      */   {
/*  383 */     return getQualifiedName();
/*      */   }
/*      */ 
/*      */   public Scope members()
/*      */   {
/*  389 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isInner()
/*      */   {
/*  395 */     return (this.kind == 2) && (this.type.getEnclosingType().hasTag(TypeTag.CLASS));
/*      */   }
/*      */ 
/*      */   public boolean hasOuterInstance()
/*      */   {
/*  408 */     return (this.type
/*  408 */       .getEnclosingType().hasTag(TypeTag.CLASS)) && ((flags() & 0x400200) == 0L);
/*      */   }
/*      */ 
/*      */   public ClassSymbol enclClass()
/*      */   {
/*  414 */     Symbol localSymbol = this;
/*  415 */     while ((localSymbol != null) && (((localSymbol.kind & 0x2) == 0) || 
/*  416 */       (!localSymbol.type
/*  416 */       .hasTag(TypeTag.CLASS))))
/*      */     {
/*  417 */       localSymbol = localSymbol.owner;
/*      */     }
/*  419 */     return (ClassSymbol)localSymbol;
/*      */   }
/*      */ 
/*      */   public ClassSymbol outermostClass()
/*      */   {
/*  425 */     Symbol localSymbol1 = this;
/*  426 */     Symbol localSymbol2 = null;
/*  427 */     while (localSymbol1.kind != 1) {
/*  428 */       localSymbol2 = localSymbol1;
/*  429 */       localSymbol1 = localSymbol1.owner;
/*      */     }
/*  431 */     return (ClassSymbol)localSymbol2;
/*      */   }
/*      */ 
/*      */   public PackageSymbol packge()
/*      */   {
/*  437 */     Symbol localSymbol = this;
/*  438 */     while (localSymbol.kind != 1) {
/*  439 */       localSymbol = localSymbol.owner;
/*      */     }
/*  441 */     return (PackageSymbol)localSymbol;
/*      */   }
/*      */ 
/*      */   public boolean isSubClass(Symbol paramSymbol, Types paramTypes)
/*      */   {
/*  447 */     throw new AssertionError("isSubClass " + this);
/*      */   }
/*      */ 
/*      */   public boolean isMemberOf(TypeSymbol paramTypeSymbol, Types paramTypes)
/*      */   {
/*  458 */     return (this.owner == paramTypeSymbol) || (
/*  456 */       (paramTypeSymbol
/*  456 */       .isSubClass(this.owner, paramTypes)) && 
/*  457 */       (isInheritedIn(paramTypeSymbol, paramTypes)) && 
/*  458 */       (!hiddenIn((ClassSymbol)paramTypeSymbol, paramTypes)));
/*      */   }
/*      */ 
/*      */   public boolean isEnclosedBy(ClassSymbol paramClassSymbol)
/*      */   {
/*  463 */     for (Symbol localSymbol = this; localSymbol.kind != 1; localSymbol = localSymbol.owner)
/*  464 */       if (localSymbol == paramClassSymbol) return true;
/*  465 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean hiddenIn(ClassSymbol paramClassSymbol, Types paramTypes) {
/*  469 */     Symbol localSymbol = hiddenInInternal(paramClassSymbol, paramTypes);
/*  470 */     Assert.check(localSymbol != null, "the result of hiddenInInternal() can't be null");
/*      */ 
/*  473 */     return localSymbol != this;
/*      */   }
/*      */ 
/*      */   private Symbol hiddenInInternal(ClassSymbol paramClassSymbol, Types paramTypes)
/*      */   {
/*  484 */     if (paramClassSymbol == this.owner) {
/*  485 */       return this;
/*      */     }
/*  487 */     Scope.Entry localEntry = paramClassSymbol.members().lookup(this.name);
/*  488 */     while (localEntry.scope != null) {
/*  489 */       if ((localEntry.sym.kind == this.kind) && ((this.kind != 16) || (
/*  491 */         ((localEntry.sym
/*  491 */         .flags() & 0x8) != 0L) && 
/*  492 */         (paramTypes
/*  492 */         .isSubSignature(localEntry.sym.type, this.type)))))
/*      */       {
/*  493 */         return localEntry.sym;
/*      */       }
/*  495 */       localEntry = localEntry.next();
/*      */     }
/*  497 */     Object localObject = null;
/*  498 */     for (Type localType : paramTypes.interfaces(paramClassSymbol.type)
/*  499 */       .prepend(paramTypes
/*  499 */       .supertype(paramClassSymbol.type))
/*  499 */       ) {
/*  500 */       if ((localType != null) && (localType.hasTag(TypeTag.CLASS))) {
/*  501 */         Symbol localSymbol = hiddenInInternal((ClassSymbol)localType.tsym, paramTypes);
/*  502 */         if (localSymbol == this)
/*  503 */           return this;
/*  504 */         if (localSymbol != null) {
/*  505 */           localObject = localSymbol;
/*      */         }
/*      */       }
/*      */     }
/*  509 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean isInheritedIn(Symbol paramSymbol, Types paramTypes)
/*      */   {
/*  520 */     switch ((int)(this.flags_field & 0x7)) { case 1:
/*      */     case 3:
/*      */     default:
/*  523 */       return true;
/*      */     case 2:
/*  525 */       return this.owner == paramSymbol;
/*      */     case 4:
/*  528 */       return (paramSymbol.flags() & 0x200) == 0L;
/*      */     case 0: }
/*  530 */     PackageSymbol localPackageSymbol = packge();
/*  531 */     for (Object localObject = paramSymbol; 
/*  532 */       (localObject != null) && (localObject != this.owner); 
/*  533 */       localObject = paramTypes.supertype(((Symbol)localObject).type).tsym) {
/*  534 */       while (((Symbol)localObject).type.hasTag(TypeTag.TYPEVAR))
/*  535 */         localObject = ((Symbol)localObject).type.getUpperBound().tsym;
/*  536 */       if (((Symbol)localObject).type.isErroneous())
/*  537 */         return true;
/*  538 */       if ((((Symbol)localObject).flags() & 0x1000000) == 0L)
/*      */       {
/*  540 */         if (((Symbol)localObject).packge() != localPackageSymbol)
/*  541 */           return false; 
/*      */       }
/*      */     }
/*  543 */     return (paramSymbol.flags() & 0x200) == 0L;
/*      */   }
/*      */ 
/*      */   public Symbol asMemberOf(Type paramType, Types paramTypes)
/*      */   {
/*  552 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public boolean overrides(Symbol paramSymbol, TypeSymbol paramTypeSymbol, Types paramTypes, boolean paramBoolean)
/*      */   {
/*  565 */     return false;
/*      */   }
/*      */ 
/*      */   public void complete()
/*      */     throws Symbol.CompletionFailure
/*      */   {
/*  571 */     if (this.completer != null) {
/*  572 */       Completer localCompleter = this.completer;
/*  573 */       this.completer = null;
/*  574 */       localCompleter.complete(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean exists()
/*      */   {
/*  581 */     return true;
/*      */   }
/*      */ 
/*      */   public Type asType() {
/*  585 */     return this.type;
/*      */   }
/*      */ 
/*      */   public Symbol getEnclosingElement() {
/*  589 */     return this.owner;
/*      */   }
/*      */ 
/*      */   public ElementKind getKind() {
/*  593 */     return ElementKind.OTHER;
/*      */   }
/*      */ 
/*      */   public Set<Modifier> getModifiers() {
/*  597 */     return Flags.asModifierSet(flags());
/*      */   }
/*      */ 
/*      */   public Name getSimpleName() {
/*  601 */     return this.name;
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<Attribute.Compound> getAnnotationMirrors()
/*      */   {
/*  610 */     return getRawAttributes();
/*      */   }
/*      */ 
/*      */   public java.util.List<Symbol> getEnclosedElements()
/*      */   {
/*  616 */     return com.sun.tools.javac.util.List.nil();
/*      */   }
/*      */ 
/*      */   public com.sun.tools.javac.util.List<TypeVariableSymbol> getTypeParameters() {
/*  620 */     ListBuffer localListBuffer = new ListBuffer();
/*  621 */     for (Type localType : this.type.getTypeArguments()) {
/*  622 */       Assert.check(localType.tsym.getKind() == ElementKind.TYPE_PARAMETER);
/*  623 */       localListBuffer.append((TypeVariableSymbol)localType.tsym);
/*      */     }
/*  625 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public static class ClassSymbol extends Symbol.TypeSymbol
/*      */     implements TypeElement
/*      */   {
/*      */     public Scope members_field;
/*      */     public Name fullname;
/*      */     public Name flatname;
/*      */     public JavaFileObject sourcefile;
/*      */     public JavaFileObject classfile;
/*      */     public com.sun.tools.javac.util.List<ClassSymbol> trans_local;
/*      */     public Pool pool;
/*      */ 
/*      */     public ClassSymbol(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/*  948 */       super(paramLong, paramName, paramType, paramSymbol);
/*  949 */       this.members_field = null;
/*  950 */       this.fullname = formFullName(paramName, paramSymbol);
/*  951 */       this.flatname = formFlatName(paramName, paramSymbol);
/*  952 */       this.sourcefile = null;
/*  953 */       this.classfile = null;
/*  954 */       this.pool = null;
/*      */     }
/*      */ 
/*      */     public ClassSymbol(long paramLong, Name paramName, Symbol paramSymbol) {
/*  958 */       this(paramLong, paramName, new Type.ClassType(Type.noType, null, null), paramSymbol);
/*      */ 
/*  963 */       this.type.tsym = this;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  969 */       return className();
/*      */     }
/*      */ 
/*      */     public long flags() {
/*  973 */       if (this.completer != null) complete();
/*  974 */       return this.flags_field;
/*      */     }
/*      */ 
/*      */     public Scope members() {
/*  978 */       if (this.completer != null) complete();
/*  979 */       return this.members_field;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Attribute.Compound> getRawAttributes()
/*      */     {
/*  984 */       if (this.completer != null) complete();
/*  985 */       return super.getRawAttributes();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Attribute.TypeCompound> getRawTypeAttributes()
/*      */     {
/*  990 */       if (this.completer != null) complete();
/*  991 */       return super.getRawTypeAttributes();
/*      */     }
/*      */ 
/*      */     public Type erasure(Types paramTypes) {
/*  995 */       if (this.erasure_field == null)
/*  996 */         this.erasure_field = new Type.ClassType(paramTypes.erasure(this.type.getEnclosingType()), 
/*  997 */           com.sun.tools.javac.util.List.nil(), this);
/*  998 */       return this.erasure_field;
/*      */     }
/*      */ 
/*      */     public String className() {
/* 1002 */       if (this.name.isEmpty())
/*      */       {
/* 1004 */         return Log.getLocalizedString("anonymous.class", new Object[] { this.flatname });
/*      */       }
/*      */ 
/* 1006 */       return this.fullname.toString();
/*      */     }
/*      */ 
/*      */     public Name getQualifiedName() {
/* 1010 */       return this.fullname;
/*      */     }
/*      */ 
/*      */     public Name flatName() {
/* 1014 */       return this.flatname;
/*      */     }
/*      */ 
/*      */     public boolean isSubClass(Symbol paramSymbol, Types paramTypes) {
/* 1018 */       if (this == paramSymbol)
/* 1019 */         return true;
/*      */       Type localType;
/* 1020 */       if ((paramSymbol.flags() & 0x200) != 0L)
/* 1021 */         for (localType = this.type; localType.hasTag(TypeTag.CLASS); localType = paramTypes.supertype(localType))
/* 1022 */           for (com.sun.tools.javac.util.List localList = paramTypes.interfaces(localType); 
/* 1023 */             localList.nonEmpty(); 
/* 1024 */             localList = localList.tail)
/* 1025 */             if (((Type)localList.head).tsym.isSubClass(paramSymbol, paramTypes)) return true;
/*      */       else {
/* 1027 */         for (localType = this.type; localType.hasTag(TypeTag.CLASS); localType = paramTypes.supertype(localType))
/* 1028 */           if (localType.tsym == paramSymbol) return true;
/*      */       }
/* 1030 */       return false;
/*      */     }
/*      */ 
/*      */     public void complete() throws Symbol.CompletionFailure
/*      */     {
/*      */       try
/*      */       {
/* 1037 */         super.complete();
/*      */       }
/*      */       catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1040 */         this.flags_field |= 9L;
/* 1041 */         this.type = new Type.ErrorType(this, Type.noType);
/* 1042 */         throw localCompletionFailure;
/*      */       }
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getInterfaces() {
/* 1047 */       complete();
/* 1048 */       if ((this.type instanceof Type.ClassType)) {
/* 1049 */         Type.ClassType localClassType = (Type.ClassType)this.type;
/* 1050 */         if (localClassType.interfaces_field == null)
/* 1051 */           localClassType.interfaces_field = com.sun.tools.javac.util.List.nil();
/* 1052 */         if (localClassType.all_interfaces_field != null)
/* 1053 */           return Type.getModelTypes(localClassType.all_interfaces_field);
/* 1054 */         return localClassType.interfaces_field;
/*      */       }
/* 1056 */       return com.sun.tools.javac.util.List.nil();
/*      */     }
/*      */ 
/*      */     public Type getSuperclass()
/*      */     {
/* 1061 */       complete();
/* 1062 */       if ((this.type instanceof Type.ClassType)) {
/* 1063 */         Type.ClassType localClassType = (Type.ClassType)this.type;
/* 1064 */         if (localClassType.supertype_field == null) {
/* 1065 */           localClassType.supertype_field = Type.noType;
/*      */         }
/*      */ 
/* 1069 */         return localClassType.isInterface() ? Type.noType : localClassType.supertype_field
/* 1069 */           .getModelType();
/*      */       }
/* 1071 */       return Type.noType;
/*      */     }
/*      */ 
/*      */     private ClassSymbol getSuperClassToSearchForAnnotations()
/*      */     {
/* 1081 */       Type localType = getSuperclass();
/*      */ 
/* 1083 */       if ((!localType.hasTag(TypeTag.CLASS)) || (localType.isErroneous())) {
/* 1084 */         return null;
/*      */       }
/* 1086 */       return (ClassSymbol)localType.tsym;
/*      */     }
/*      */ 
/*      */     protected <A extends Annotation> A[] getInheritedAnnotations(Class<A> paramClass)
/*      */     {
/* 1093 */       ClassSymbol localClassSymbol = getSuperClassToSearchForAnnotations();
/*      */ 
/* 1096 */       return localClassSymbol == null ? super.getInheritedAnnotations(paramClass) : localClassSymbol
/* 1096 */         .getAnnotationsByType(paramClass);
/*      */     }
/*      */ 
/*      */     public ElementKind getKind()
/*      */     {
/* 1101 */       long l = flags();
/* 1102 */       if ((l & 0x2000) != 0L)
/* 1103 */         return ElementKind.ANNOTATION_TYPE;
/* 1104 */       if ((l & 0x200) != 0L)
/* 1105 */         return ElementKind.INTERFACE;
/* 1106 */       if ((l & 0x4000) != 0L) {
/* 1107 */         return ElementKind.ENUM;
/*      */       }
/* 1109 */       return ElementKind.CLASS;
/*      */     }
/*      */ 
/*      */     public Set<Modifier> getModifiers()
/*      */     {
/* 1114 */       long l = flags();
/* 1115 */       return Flags.asModifierSet(l & 0xFFFFFFFF);
/*      */     }
/*      */ 
/*      */     public NestingKind getNestingKind() {
/* 1119 */       complete();
/* 1120 */       if (this.owner.kind == 1)
/* 1121 */         return NestingKind.TOP_LEVEL;
/* 1122 */       if (this.name.isEmpty())
/* 1123 */         return NestingKind.ANONYMOUS;
/* 1124 */       if (this.owner.kind == 16) {
/* 1125 */         return NestingKind.LOCAL;
/*      */       }
/* 1127 */       return NestingKind.MEMBER;
/*      */     }
/*      */ 
/*      */     protected <A extends Annotation> Attribute.Compound getAttribute(Class<A> paramClass)
/*      */     {
/* 1134 */       Attribute.Compound localCompound = super.getAttribute(paramClass);
/*      */ 
/* 1136 */       boolean bool = paramClass.isAnnotationPresent(Inherited.class);
/* 1137 */       if ((localCompound != null) || (!bool)) {
/* 1138 */         return localCompound;
/*      */       }
/*      */ 
/* 1141 */       ClassSymbol localClassSymbol = getSuperClassToSearchForAnnotations();
/*      */ 
/* 1143 */       return localClassSymbol == null ? null : localClassSymbol
/* 1143 */         .getAttribute(paramClass);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP)
/*      */     {
/* 1150 */       return paramElementVisitor.visitType(this, paramP);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/* 1154 */       return paramVisitor.visitClassSymbol(this, paramP);
/*      */     }
/*      */ 
/*      */     public void markAbstractIfNeeded(Types paramTypes) {
/* 1158 */       if ((paramTypes.enter.getEnv(this) != null) && 
/* 1159 */         ((flags() & 0x4000) != 0L) && (paramTypes.supertype(this.type).tsym == paramTypes.syms.enumSym) && 
/* 1160 */         ((flags() & 0x410) == 0L) && 
/* 1161 */         (paramTypes.firstUnimplementedAbstract(this) != null))
/*      */       {
/* 1163 */         this.flags_field |= 1024L;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface Completer
/*      */   {
/*      */     public abstract void complete(Symbol paramSymbol)
/*      */       throws Symbol.CompletionFailure;
/*      */   }
/*      */ 
/*      */   public static class CompletionFailure extends RuntimeException
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     public Symbol sym;
/*      */     public JCDiagnostic diag;
/*      */ 
/*      */     @Deprecated
/*      */     public String errmsg;
/*      */ 
/*      */     public CompletionFailure(Symbol paramSymbol, String paramString)
/*      */     {
/* 1722 */       this.sym = paramSymbol;
/* 1723 */       this.errmsg = paramString;
/*      */     }
/*      */ 
/*      */     public CompletionFailure(Symbol paramSymbol, JCDiagnostic paramJCDiagnostic)
/*      */     {
/* 1728 */       this.sym = paramSymbol;
/* 1729 */       this.diag = paramJCDiagnostic;
/*      */     }
/*      */ 
/*      */     public JCDiagnostic getDiagnostic()
/*      */     {
/* 1734 */       return this.diag;
/*      */     }
/*      */ 
/*      */     public String getMessage()
/*      */     {
/* 1739 */       if (this.diag != null) {
/* 1740 */         return this.diag.getMessage(null);
/*      */       }
/* 1742 */       return this.errmsg;
/*      */     }
/*      */ 
/*      */     public Object getDetailValue() {
/* 1746 */       return this.diag != null ? this.diag : this.errmsg;
/*      */     }
/*      */ 
/*      */     public CompletionFailure initCause(Throwable paramThrowable)
/*      */     {
/* 1751 */       super.initCause(paramThrowable);
/* 1752 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DelegatedSymbol<T extends Symbol> extends Symbol
/*      */   {
/*      */     protected T other;
/*      */ 
/*      */     public DelegatedSymbol(T paramT)
/*      */     {
/*  631 */       super(paramT.flags_field, paramT.name, paramT.type, paramT.owner);
/*  632 */       this.other = paramT;
/*      */     }
/*  634 */     public String toString() { return this.other.toString(); } 
/*  635 */     public Symbol location() { return this.other.location(); } 
/*  636 */     public Symbol location(Type paramType, Types paramTypes) { return this.other.location(paramType, paramTypes); } 
/*  637 */     public Symbol baseSymbol() { return this.other; } 
/*  638 */     public Type erasure(Types paramTypes) { return this.other.erasure(paramTypes); } 
/*  639 */     public Type externalType(Types paramTypes) { return this.other.externalType(paramTypes); } 
/*  640 */     public boolean isLocal() { return this.other.isLocal(); } 
/*  641 */     public boolean isConstructor() { return this.other.isConstructor(); } 
/*  642 */     public Name getQualifiedName() { return this.other.getQualifiedName(); } 
/*  643 */     public Name flatName() { return this.other.flatName(); } 
/*  644 */     public Scope members() { return this.other.members(); } 
/*  645 */     public boolean isInner() { return this.other.isInner(); } 
/*  646 */     public boolean hasOuterInstance() { return this.other.hasOuterInstance(); } 
/*  647 */     public Symbol.ClassSymbol enclClass() { return this.other.enclClass(); } 
/*  648 */     public Symbol.ClassSymbol outermostClass() { return this.other.outermostClass(); } 
/*  649 */     public Symbol.PackageSymbol packge() { return this.other.packge(); } 
/*  650 */     public boolean isSubClass(Symbol paramSymbol, Types paramTypes) { return this.other.isSubClass(paramSymbol, paramTypes); } 
/*  651 */     public boolean isMemberOf(Symbol.TypeSymbol paramTypeSymbol, Types paramTypes) { return this.other.isMemberOf(paramTypeSymbol, paramTypes); } 
/*  652 */     public boolean isEnclosedBy(Symbol.ClassSymbol paramClassSymbol) { return this.other.isEnclosedBy(paramClassSymbol); } 
/*  653 */     public boolean isInheritedIn(Symbol paramSymbol, Types paramTypes) { return this.other.isInheritedIn(paramSymbol, paramTypes); } 
/*  654 */     public Symbol asMemberOf(Type paramType, Types paramTypes) { return this.other.asMemberOf(paramType, paramTypes); } 
/*  655 */     public void complete() throws Symbol.CompletionFailure { this.other.complete(); }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP) {
/*  658 */       return this.other.accept(paramElementVisitor, paramP);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/*  662 */       return paramVisitor.visitSymbol(this.other, paramP);
/*      */     }
/*      */ 
/*      */     public T getUnderlyingSymbol() {
/*  666 */       return this.other;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DynamicMethodSymbol extends Symbol.MethodSymbol
/*      */   {
/*      */     public Object[] staticArgs;
/*      */     public Symbol bsm;
/*      */     public int bsmKind;
/*      */ 
/*      */     public DynamicMethodSymbol(Name paramName, Symbol paramSymbol, int paramInt, Symbol.MethodSymbol paramMethodSymbol, Type paramType, Object[] paramArrayOfObject)
/*      */     {
/* 1673 */       super(paramName, paramType, paramSymbol);
/* 1674 */       this.bsm = paramMethodSymbol;
/* 1675 */       this.bsmKind = paramInt;
/* 1676 */       this.staticArgs = paramArrayOfObject;
/*      */     }
/*      */ 
/*      */     public boolean isDynamic()
/*      */     {
/* 1681 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class MethodSymbol extends Symbol
/*      */     implements ExecutableElement
/*      */   {
/* 1305 */     public Code code = null;
/*      */ 
/* 1308 */     public com.sun.tools.javac.util.List<Symbol.VarSymbol> extraParams = com.sun.tools.javac.util.List.nil();
/*      */ 
/* 1311 */     public com.sun.tools.javac.util.List<Symbol.VarSymbol> capturedLocals = com.sun.tools.javac.util.List.nil();
/*      */ 
/* 1314 */     public com.sun.tools.javac.util.List<Symbol.VarSymbol> params = null;
/*      */     public com.sun.tools.javac.util.List<Name> savedParameterNames;
/* 1323 */     public Attribute defaultValue = null;
/*      */ 
/* 1531 */     public static final Filter<Symbol> implementation_filter = new Filter()
/*      */     {
/*      */       public boolean accepts(Symbol paramAnonymousSymbol) {
/* 1534 */         return (paramAnonymousSymbol.kind == 16) && 
/* 1534 */           ((paramAnonymousSymbol
/* 1534 */           .flags() & 0x1000) == 0L);
/*      */       }
/* 1531 */     };
/*      */ 
/*      */     public MethodSymbol(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/* 1328 */       super(paramLong, paramName, paramType, paramSymbol);
/* 1329 */       if (paramSymbol.type.hasTag(TypeTag.TYPEVAR)) Assert.error(paramSymbol + "." + paramName);
/*      */     }
/*      */ 
/*      */     public MethodSymbol clone(Symbol paramSymbol)
/*      */     {
/* 1335 */       MethodSymbol local1 = new MethodSymbol(this.flags_field, this.name, this.type, paramSymbol)
/*      */       {
/*      */         public Symbol baseSymbol() {
/* 1338 */           return Symbol.MethodSymbol.this;
/*      */         }
/*      */       };
/* 1341 */       local1.code = this.code;
/* 1342 */       return local1;
/*      */     }
/*      */ 
/*      */     public Set<Modifier> getModifiers()
/*      */     {
/* 1347 */       long l = flags();
/* 1348 */       return Flags.asModifierSet((l & 0x0) != 0L ? l & 0xFFFFFBFF : l);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1354 */       if ((flags() & 0x100000) != 0L) {
/* 1355 */         return this.owner.name.toString();
/*      */       }
/*      */ 
/* 1359 */       String str = this.name == this.name.table.names.init ? this.owner.name
/* 1358 */         .toString() : this.name
/* 1359 */         .toString();
/* 1360 */       if (this.type != null) {
/* 1361 */         if (this.type.hasTag(TypeTag.FORALL))
/* 1362 */           str = "<" + ((Type.ForAll)this.type).getTypeArguments() + ">" + str;
/* 1363 */         str = str + "(" + this.type.argtypes((flags() & 0x0) != 0L) + ")";
/*      */       }
/* 1365 */       return str;
/*      */     }
/*      */ 
/*      */     public boolean isDynamic()
/*      */     {
/* 1370 */       return false;
/*      */     }
/*      */ 
/*      */     public Symbol implemented(Symbol.TypeSymbol paramTypeSymbol, Types paramTypes)
/*      */     {
/* 1378 */       Symbol localSymbol = null;
/* 1379 */       for (com.sun.tools.javac.util.List localList = paramTypes.interfaces(paramTypeSymbol.type); 
/* 1380 */         (localSymbol == null) && (localList.nonEmpty()); 
/* 1381 */         localList = localList.tail) {
/* 1382 */         Symbol.TypeSymbol localTypeSymbol = ((Type)localList.head).tsym;
/* 1383 */         localSymbol = implementedIn(localTypeSymbol, paramTypes);
/* 1384 */         if (localSymbol == null)
/* 1385 */           localSymbol = implemented(localTypeSymbol, paramTypes);
/*      */       }
/* 1387 */       return localSymbol;
/*      */     }
/*      */ 
/*      */     public Symbol implementedIn(Symbol.TypeSymbol paramTypeSymbol, Types paramTypes) {
/* 1391 */       Symbol localSymbol = null;
/* 1392 */       for (Scope.Entry localEntry = paramTypeSymbol.members().lookup(this.name); 
/* 1393 */         (localSymbol == null) && (localEntry.scope != null); 
/* 1394 */         localEntry = localEntry.next()) {
/* 1395 */         if (overrides(localEntry.sym, (Symbol.TypeSymbol)this.owner, paramTypes, true))
/*      */         {
/* 1398 */           if (paramTypes
/* 1398 */             .isSameType(this.type
/* 1398 */             .getReturnType(), paramTypes
/* 1399 */             .memberType(this.owner.type, localEntry.sym)
/* 1399 */             .getReturnType()))
/* 1400 */             localSymbol = localEntry.sym;
/*      */         }
/*      */       }
/* 1403 */       return localSymbol;
/*      */     }
/*      */ 
/*      */     public boolean binaryOverrides(Symbol paramSymbol, Symbol.TypeSymbol paramTypeSymbol, Types paramTypes)
/*      */     {
/* 1410 */       if ((isConstructor()) || (paramSymbol.kind != 16)) return false;
/*      */ 
/* 1412 */       if (this == paramSymbol) return true;
/* 1413 */       MethodSymbol localMethodSymbol = (MethodSymbol)paramSymbol;
/*      */ 
/* 1416 */       if ((localMethodSymbol.isOverridableIn((Symbol.TypeSymbol)this.owner)) && 
/* 1417 */         (paramTypes
/* 1417 */         .asSuper(this.owner.type, localMethodSymbol.owner) != null) && 
/* 1418 */         (paramTypes
/* 1418 */         .isSameType(erasure(paramTypes), 
/* 1418 */         localMethodSymbol.erasure(paramTypes)))) {
/* 1419 */         return true;
/*      */       }
/*      */ 
/* 1426 */       return ((flags() & 0x400) == 0L) && 
/* 1424 */         (localMethodSymbol
/* 1424 */         .isOverridableIn(paramTypeSymbol)) && 
/* 1425 */         (isMemberOf(paramTypeSymbol, paramTypes)) && 
/* 1426 */         (paramTypes
/* 1426 */         .isSameType(erasure(paramTypes), 
/* 1426 */         localMethodSymbol.erasure(paramTypes)));
/*      */     }
/*      */ 
/*      */     public MethodSymbol binaryImplementation(Symbol.ClassSymbol paramClassSymbol, Types paramTypes)
/*      */     {
/* 1435 */       for (Object localObject = paramClassSymbol; localObject != null; localObject = paramTypes.supertype(((Symbol.TypeSymbol)localObject).type).tsym) {
/* 1436 */         for (Scope.Entry localEntry = ((Symbol.TypeSymbol)localObject).members().lookup(this.name); 
/* 1437 */           localEntry.scope != null; 
/* 1438 */           localEntry = localEntry.next())
/* 1439 */           if ((localEntry.sym.kind == 16) && 
/* 1440 */             (((MethodSymbol)localEntry.sym)
/* 1440 */             .binaryOverrides(this, paramClassSymbol, paramTypes)))
/*      */           {
/* 1441 */             return (MethodSymbol)localEntry.sym;
/*      */           }
/*      */       }
/* 1444 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean overrides(Symbol paramSymbol, Symbol.TypeSymbol paramTypeSymbol, Types paramTypes, boolean paramBoolean)
/*      */     {
/* 1457 */       if ((isConstructor()) || (paramSymbol.kind != 16)) return false;
/*      */ 
/* 1459 */       if (this == paramSymbol) return true;
/* 1460 */       MethodSymbol localMethodSymbol = (MethodSymbol)paramSymbol;
/*      */ 
/* 1463 */       if ((localMethodSymbol.isOverridableIn((Symbol.TypeSymbol)this.owner)) && 
/* 1464 */         (paramTypes
/* 1464 */         .asSuper(this.owner.type, localMethodSymbol.owner) != null))
/*      */       {
/* 1465 */         localType1 = paramTypes.memberType(this.owner.type, this);
/* 1466 */         localType2 = paramTypes.memberType(this.owner.type, localMethodSymbol);
/* 1467 */         if (paramTypes.isSubSignature(localType1, localType2)) {
/* 1468 */           if (!paramBoolean)
/* 1469 */             return true;
/* 1470 */           if (paramTypes.returnTypeSubstitutable(localType1, localType2)) {
/* 1471 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1476 */       if (((flags() & 0x400) != 0L) || 
/* 1477 */         (((localMethodSymbol
/* 1477 */         .flags() & 0x400) == 0L) && ((localMethodSymbol.flags() & 0x0) == 0L)) || 
/* 1478 */         (!localMethodSymbol
/* 1478 */         .isOverridableIn(paramTypeSymbol)) || 
/* 1479 */         (!isMemberOf(paramTypeSymbol, paramTypes)))
/*      */       {
/* 1480 */         return false;
/*      */       }
/*      */ 
/* 1483 */       Type localType1 = paramTypes.memberType(paramTypeSymbol.type, this);
/* 1484 */       Type localType2 = paramTypes.memberType(paramTypeSymbol.type, localMethodSymbol);
/*      */ 
/* 1487 */       return (paramTypes
/* 1486 */         .isSubSignature(localType1, localType2)) && 
/* 1486 */         ((!paramBoolean) || 
/* 1487 */         (paramTypes
/* 1487 */         .resultSubtype(localType1, localType2, paramTypes.noWarnings)));
/*      */     }
/*      */ 
/*      */     private boolean isOverridableIn(Symbol.TypeSymbol paramTypeSymbol)
/*      */     {
/* 1492 */       switch ((int)(this.flags_field & 0x7)) {
/*      */       case 2:
/* 1494 */         return false;
/*      */       case 1:
/* 1496 */         return (!this.owner.isInterface()) || ((this.flags_field & 0x8) == 0L);
/*      */       case 4:
/* 1499 */         return (paramTypeSymbol.flags() & 0x200) == 0L;
/*      */       case 0:
/* 1505 */         return (packge() == paramTypeSymbol.packge()) && 
/* 1505 */           ((paramTypeSymbol
/* 1505 */           .flags() & 0x200) == 0L);
/*      */       case 3:
/* 1507 */       }return false;
/*      */     }
/*      */ 
/*      */     public boolean isInheritedIn(Symbol paramSymbol, Types paramTypes)
/*      */     {
/* 1513 */       switch ((int)(this.flags_field & 0x7)) {
/*      */       case 1:
/* 1515 */         return (!this.owner.isInterface()) || (paramSymbol == this.owner) || ((this.flags_field & 0x8) == 0L);
/*      */       }
/*      */ 
/* 1519 */       return super.isInheritedIn(paramSymbol, paramTypes);
/*      */     }
/*      */ 
/*      */     public MethodSymbol implementation(Symbol.TypeSymbol paramTypeSymbol, Types paramTypes, boolean paramBoolean)
/*      */     {
/* 1528 */       return implementation(paramTypeSymbol, paramTypes, paramBoolean, implementation_filter);
/*      */     }
/*      */ 
/*      */     public MethodSymbol implementation(Symbol.TypeSymbol paramTypeSymbol, Types paramTypes, boolean paramBoolean, Filter<Symbol> paramFilter)
/*      */     {
/* 1539 */       MethodSymbol localMethodSymbol = paramTypes.implementation(this, paramTypeSymbol, paramBoolean, paramFilter);
/* 1540 */       if (localMethodSymbol != null) {
/* 1541 */         return localMethodSymbol;
/*      */       }
/*      */ 
/* 1545 */       if ((paramTypes.isDerivedRaw(paramTypeSymbol.type)) && (!paramTypeSymbol.isInterface())) {
/* 1546 */         return implementation(paramTypes.supertype(paramTypeSymbol.type).tsym, paramTypes, paramBoolean);
/*      */       }
/* 1548 */       return null;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Symbol.VarSymbol> params() {
/* 1552 */       this.owner.complete();
/* 1553 */       if (this.params == null)
/*      */       {
/* 1561 */         com.sun.tools.javac.util.List localList1 = this.savedParameterNames;
/* 1562 */         this.savedParameterNames = null;
/*      */ 
/* 1564 */         if ((localList1 == null) || (localList1.size() != this.type.getParameterTypes().size())) {
/* 1565 */           localList1 = com.sun.tools.javac.util.List.nil();
/*      */         }
/* 1567 */         ListBuffer localListBuffer = new ListBuffer();
/* 1568 */         com.sun.tools.javac.util.List localList2 = localList1;
/*      */ 
/* 1571 */         int i = 0;
/* 1572 */         for (Type localType : this.type.getParameterTypes())
/*      */         {
/*      */           Name localName;
/* 1574 */           if (localList2.isEmpty())
/*      */           {
/* 1576 */             localName = createArgName(i, localList1);
/*      */           } else {
/* 1578 */             localName = (Name)localList2.head;
/* 1579 */             localList2 = localList2.tail;
/* 1580 */             if (localName.isEmpty())
/*      */             {
/* 1582 */               localName = createArgName(i, localList1);
/*      */             }
/*      */           }
/* 1585 */           localListBuffer.append(new Symbol.VarSymbol(8589934592L, localName, localType, this));
/* 1586 */           i++;
/*      */         }
/* 1588 */         this.params = localListBuffer.toList();
/*      */       }
/* 1590 */       return this.params;
/*      */     }
/*      */ 
/*      */     private Name createArgName(int paramInt, com.sun.tools.javac.util.List<Name> paramList)
/*      */     {
/* 1598 */       String str = "arg";
/*      */       while (true) {
/* 1600 */         Name localName = this.name.table.fromString(str + paramInt);
/* 1601 */         if (!paramList.contains(localName))
/* 1602 */           return localName;
/* 1603 */         str = str + "$";
/*      */       }
/*      */     }
/*      */ 
/*      */     public Symbol asMemberOf(Type paramType, Types paramTypes) {
/* 1608 */       return new MethodSymbol(this.flags_field, this.name, paramTypes.memberType(paramType, this), this.owner);
/*      */     }
/*      */ 
/*      */     public ElementKind getKind() {
/* 1612 */       if (this.name == this.name.table.names.init)
/* 1613 */         return ElementKind.CONSTRUCTOR;
/* 1614 */       if (this.name == this.name.table.names.clinit)
/* 1615 */         return ElementKind.STATIC_INIT;
/* 1616 */       if ((flags() & 0x100000) != 0L) {
/* 1617 */         return isStatic() ? ElementKind.STATIC_INIT : ElementKind.INSTANCE_INIT;
/*      */       }
/* 1619 */       return ElementKind.METHOD;
/*      */     }
/*      */ 
/*      */     public boolean isStaticOrInstanceInit()
/*      */     {
/* 1624 */       return (getKind() == ElementKind.STATIC_INIT) || 
/* 1624 */         (getKind() == ElementKind.INSTANCE_INIT);
/*      */     }
/*      */ 
/*      */     public Attribute getDefaultValue() {
/* 1628 */       return this.defaultValue;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Symbol.VarSymbol> getParameters() {
/* 1632 */       return params();
/*      */     }
/*      */ 
/*      */     public boolean isVarArgs() {
/* 1636 */       return (flags() & 0x0) != 0L;
/*      */     }
/*      */ 
/*      */     public boolean isDefault() {
/* 1640 */       return (flags() & 0x0) != 0L;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP) {
/* 1644 */       return paramElementVisitor.visitExecutable(this, paramP);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/* 1648 */       return paramVisitor.visitMethodSymbol(this, paramP);
/*      */     }
/*      */ 
/*      */     public Type getReceiverType() {
/* 1652 */       return asType().getReceiverType();
/*      */     }
/*      */ 
/*      */     public Type getReturnType() {
/* 1656 */       return asType().getReturnType();
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getThrownTypes() {
/* 1660 */       return asType().getThrownTypes();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class OperatorSymbol extends Symbol.MethodSymbol
/*      */   {
/*      */     public int opcode;
/*      */ 
/*      */     public OperatorSymbol(Name paramName, Type paramType, int paramInt, Symbol paramSymbol)
/*      */     {
/* 1692 */       super(paramName, paramType, paramSymbol);
/* 1693 */       this.opcode = paramInt;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/* 1697 */       return paramVisitor.visitOperatorSymbol(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PackageSymbol extends Symbol.TypeSymbol
/*      */     implements PackageElement
/*      */   {
/*      */     public Scope members_field;
/*      */     public Name fullname;
/*      */     public Symbol.ClassSymbol package_info;
/*      */ 
/*      */     public PackageSymbol(Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/*  835 */       super(0L, paramName, paramType, paramSymbol);
/*  836 */       this.members_field = null;
/*  837 */       this.fullname = formFullName(paramName, paramSymbol);
/*      */     }
/*      */ 
/*      */     public PackageSymbol(Name paramName, Symbol paramSymbol) {
/*  841 */       this(paramName, null, paramSymbol);
/*  842 */       this.type = new Type.PackageType(this);
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  846 */       return this.fullname.toString();
/*      */     }
/*      */ 
/*      */     public Name getQualifiedName() {
/*  850 */       return this.fullname;
/*      */     }
/*      */ 
/*      */     public boolean isUnnamed() {
/*  854 */       return (this.name.isEmpty()) && (this.owner != null);
/*      */     }
/*      */ 
/*      */     public Scope members() {
/*  858 */       if (this.completer != null) complete();
/*  859 */       return this.members_field;
/*      */     }
/*      */ 
/*      */     public long flags() {
/*  863 */       if (this.completer != null) complete();
/*  864 */       return this.flags_field;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Attribute.Compound> getRawAttributes()
/*      */     {
/*  869 */       if (this.completer != null) complete();
/*  870 */       if ((this.package_info != null) && (this.package_info.completer != null)) {
/*  871 */         this.package_info.complete();
/*  872 */         mergeAttributes();
/*      */       }
/*  874 */       return super.getRawAttributes();
/*      */     }
/*      */ 
/*      */     private void mergeAttributes() {
/*  878 */       if ((this.metadata == null) && (this.package_info.metadata != null))
/*      */       {
/*  880 */         this.metadata = new SymbolMetadata(this);
/*  881 */         this.metadata.setAttributes(this.package_info.metadata);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean exists()
/*      */     {
/*  889 */       return (this.flags_field & 0x800000) != 0L;
/*      */     }
/*      */ 
/*      */     public ElementKind getKind() {
/*  893 */       return ElementKind.PACKAGE;
/*      */     }
/*      */ 
/*      */     public Symbol getEnclosingElement() {
/*  897 */       return null;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP) {
/*  901 */       return paramElementVisitor.visitPackage(this, paramP);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/*  905 */       return paramVisitor.visitPackageSymbol(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class TypeSymbol extends Symbol
/*      */   {
/*      */     public TypeSymbol(int paramInt, long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/*  674 */       super(paramLong, paramName, paramType, paramSymbol);
/*      */     }
/*      */ 
/*      */     public static Name formFullName(Name paramName, Symbol paramSymbol)
/*      */     {
/*  679 */       if (paramSymbol == null) return paramName;
/*  680 */       if (paramSymbol.kind != 63) if ((paramSymbol.kind & 0x14) == 0) { if (paramSymbol.kind == 2) {
/*  682 */             if (!paramSymbol.type
/*  682 */               .hasTag(TypeTag.TYPEVAR));
/*      */           } } else return paramName;
/*  684 */       Name localName = paramSymbol.getQualifiedName();
/*  685 */       if ((localName == null) || (localName == localName.table.names.empty))
/*  686 */         return paramName;
/*  687 */       return localName.append('.', paramName);
/*      */     }
/*      */ 
/*      */     public static Name formFlatName(Name paramName, Symbol paramSymbol)
/*      */     {
/*  694 */       if ((paramSymbol != null) && ((paramSymbol.kind & 0x14) == 0)) { if (paramSymbol.kind == 2) {
/*  696 */           if (!paramSymbol.type
/*  696 */             .hasTag(TypeTag.TYPEVAR));
/*      */         } } else return paramName;
/*  698 */       char c = paramSymbol.kind == 2 ? '$' : '.';
/*  699 */       Name localName = paramSymbol.flatName();
/*  700 */       if ((localName == null) || (localName == localName.table.names.empty))
/*  701 */         return paramName;
/*  702 */       return localName.append(c, paramName);
/*      */     }
/*      */ 
/*      */     public final boolean precedes(TypeSymbol paramTypeSymbol, Types paramTypes)
/*      */     {
/*  712 */       if (this == paramTypeSymbol)
/*  713 */         return false;
/*  714 */       if (this.type.hasTag(paramTypeSymbol.type.getTag())) {
/*  715 */         if (this.type.hasTag(TypeTag.CLASS))
/*      */         {
/*  719 */           return (paramTypes
/*  717 */             .rank(paramTypeSymbol.type) < 
/*  717 */             paramTypes.rank(this.type)) || (
/*  718 */             (paramTypes
/*  718 */             .rank(paramTypeSymbol.type) == 
/*  718 */             paramTypes.rank(this.type)) && 
/*  719 */             (paramTypeSymbol
/*  719 */             .getQualifiedName().compareTo(getQualifiedName()) < 0));
/*  720 */         }if (this.type.hasTag(TypeTag.TYPEVAR)) {
/*  721 */           return paramTypes.isSubtype(this.type, paramTypeSymbol.type);
/*      */         }
/*      */       }
/*  724 */       return this.type.hasTag(TypeTag.TYPEVAR);
/*      */     }
/*      */ 
/*      */     public java.util.List<Symbol> getEnclosedElements()
/*      */     {
/*  729 */       com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/*  730 */       if ((this.kind == 2) && (this.type.hasTag(TypeTag.TYPEVAR))) {
/*  731 */         return localList;
/*      */       }
/*  733 */       for (Scope.Entry localEntry = members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  734 */         if ((localEntry.sym != null) && ((localEntry.sym.flags() & 0x1000) == 0L) && (localEntry.sym.owner == this))
/*  735 */           localList = localList.prepend(localEntry.sym);
/*      */       }
/*  737 */       return localList;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP)
/*      */     {
/*  742 */       return paramVisitor.visitTypeSymbol(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TypeVariableSymbol extends Symbol.TypeSymbol
/*      */     implements TypeParameterElement
/*      */   {
/*      */     public TypeVariableSymbol(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/*  753 */       super(paramLong, paramName, paramType, paramSymbol);
/*      */     }
/*      */ 
/*      */     public ElementKind getKind() {
/*  757 */       return ElementKind.TYPE_PARAMETER;
/*      */     }
/*      */ 
/*      */     public Symbol getGenericElement()
/*      */     {
/*  762 */       return this.owner;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Type> getBounds() {
/*  766 */       Type.TypeVar localTypeVar = (Type.TypeVar)this.type;
/*  767 */       Type localType = localTypeVar.getUpperBound();
/*  768 */       if (!localType.isCompound())
/*  769 */         return com.sun.tools.javac.util.List.of(localType);
/*  770 */       Type.ClassType localClassType = (Type.ClassType)localType;
/*  771 */       if (!localClassType.tsym.erasure_field.isInterface()) {
/*  772 */         return localClassType.interfaces_field.prepend(localClassType.supertype_field);
/*      */       }
/*      */ 
/*  776 */       return localClassType.interfaces_field;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<Attribute.Compound> getAnnotationMirrors()
/*      */     {
/*  784 */       com.sun.tools.javac.util.List localList1 = this.owner.getRawTypeAttributes();
/*  785 */       int i = this.owner.getTypeParameters().indexOf(this);
/*  786 */       com.sun.tools.javac.util.List localList2 = com.sun.tools.javac.util.List.nil();
/*  787 */       for (Attribute.TypeCompound localTypeCompound : localList1) {
/*  788 */         if (isCurrentSymbolsAnnotation(localTypeCompound, i)) {
/*  789 */           localList2 = localList2.prepend(localTypeCompound);
/*      */         }
/*      */       }
/*  792 */       return localList2.reverse();
/*      */     }
/*      */ 
/*      */     public <A extends Annotation> Attribute.Compound getAttribute(Class<A> paramClass)
/*      */     {
/*  798 */       String str = paramClass.getName();
/*      */ 
/*  802 */       com.sun.tools.javac.util.List localList = this.owner.getRawTypeAttributes();
/*  803 */       int i = this.owner.getTypeParameters().indexOf(this);
/*  804 */       for (Attribute.TypeCompound localTypeCompound : localList) {
/*  805 */         if ((isCurrentSymbolsAnnotation(localTypeCompound, i)) && 
/*  806 */           (str
/*  806 */           .contentEquals(localTypeCompound.type.tsym
/*  806 */           .flatName())))
/*  807 */           return localTypeCompound;
/*      */       }
/*  809 */       return null;
/*      */     }
/*      */ 
/*      */     boolean isCurrentSymbolsAnnotation(Attribute.TypeCompound paramTypeCompound, int paramInt) {
/*  813 */       return ((paramTypeCompound.position.type == TargetType.CLASS_TYPE_PARAMETER) || (paramTypeCompound.position.type == TargetType.METHOD_TYPE_PARAMETER)) && (paramTypeCompound.position.parameter_index == paramInt);
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP)
/*      */     {
/*  821 */       return paramElementVisitor.visitTypeParameter(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class VarSymbol extends Symbol
/*      */     implements VariableElement
/*      */   {
/* 1175 */     public int pos = -1;
/*      */ 
/* 1186 */     public int adr = -1;
/*      */     private Object data;
/*      */ 
/*      */     public VarSymbol(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */     {
/* 1191 */       super(paramLong, paramName, paramType, paramSymbol);
/*      */     }
/*      */ 
/*      */     public VarSymbol clone(Symbol paramSymbol)
/*      */     {
/* 1197 */       VarSymbol local1 = new VarSymbol(this.flags_field, this.name, this.type, paramSymbol)
/*      */       {
/*      */         public Symbol baseSymbol() {
/* 1200 */           return Symbol.VarSymbol.this;
/*      */         }
/*      */       };
/* 1203 */       local1.pos = this.pos;
/* 1204 */       local1.adr = this.adr;
/* 1205 */       local1.data = this.data;
/*      */ 
/* 1207 */       return local1;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1211 */       return this.name.toString();
/*      */     }
/*      */ 
/*      */     public Symbol asMemberOf(Type paramType, Types paramTypes) {
/* 1215 */       return new VarSymbol(this.flags_field, this.name, paramTypes.memberType(paramType, this), this.owner);
/*      */     }
/*      */ 
/*      */     public ElementKind getKind() {
/* 1219 */       long l = flags();
/* 1220 */       if ((l & 0x0) != 0L) {
/* 1221 */         if (isExceptionParameter()) {
/* 1222 */           return ElementKind.EXCEPTION_PARAMETER;
/*      */         }
/* 1224 */         return ElementKind.PARAMETER;
/* 1225 */       }if ((l & 0x4000) != 0L)
/* 1226 */         return ElementKind.ENUM_CONSTANT;
/* 1227 */       if ((this.owner.kind == 2) || (this.owner.kind == 63))
/* 1228 */         return ElementKind.FIELD;
/* 1229 */       if (isResourceVariable()) {
/* 1230 */         return ElementKind.RESOURCE_VARIABLE;
/*      */       }
/* 1232 */       return ElementKind.LOCAL_VARIABLE;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(ElementVisitor<R, P> paramElementVisitor, P paramP)
/*      */     {
/* 1237 */       return paramElementVisitor.visitVariable(this, paramP);
/*      */     }
/*      */ 
/*      */     public Object getConstantValue() {
/* 1241 */       return Constants.decode(getConstValue(), this.type);
/*      */     }
/*      */ 
/*      */     public void setLazyConstValue(final Env<AttrContext> paramEnv, final Attr paramAttr, final JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 1248 */       setData(new Callable() {
/*      */         public Object call() {
/* 1250 */           return paramAttr.attribLazyConstantValue(paramEnv, paramJCVariableDecl, Symbol.VarSymbol.this.type);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public boolean isExceptionParameter()
/*      */     {
/* 1264 */       return this.data == ElementKind.EXCEPTION_PARAMETER;
/*      */     }
/*      */ 
/*      */     public boolean isResourceVariable() {
/* 1268 */       return this.data == ElementKind.RESOURCE_VARIABLE;
/*      */     }
/*      */ 
/*      */     public Object getConstValue()
/*      */     {
/* 1273 */       if ((this.data == ElementKind.EXCEPTION_PARAMETER) || (this.data == ElementKind.RESOURCE_VARIABLE))
/*      */       {
/* 1275 */         return null;
/* 1276 */       }if ((this.data instanceof Callable))
/*      */       {
/* 1279 */         Callable localCallable = (Callable)this.data;
/* 1280 */         this.data = null;
/*      */         try {
/* 1282 */           this.data = localCallable.call();
/*      */         } catch (Exception localException) {
/* 1284 */           throw new AssertionError(localException);
/*      */         }
/*      */       }
/* 1287 */       return this.data;
/*      */     }
/*      */ 
/*      */     public void setData(Object paramObject) {
/* 1291 */       Assert.check(!(paramObject instanceof Env), this);
/* 1292 */       this.data = paramObject;
/*      */     }
/*      */ 
/*      */     public <R, P> R accept(Symbol.Visitor<R, P> paramVisitor, P paramP) {
/* 1296 */       return paramVisitor.visitVarSymbol(this, paramP);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface Visitor<R, P>
/*      */   {
/*      */     public abstract R visitClassSymbol(Symbol.ClassSymbol paramClassSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitMethodSymbol(Symbol.MethodSymbol paramMethodSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitPackageSymbol(Symbol.PackageSymbol paramPackageSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitOperatorSymbol(Symbol.OperatorSymbol paramOperatorSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitVarSymbol(Symbol.VarSymbol paramVarSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitTypeSymbol(Symbol.TypeSymbol paramTypeSymbol, P paramP);
/*      */ 
/*      */     public abstract R visitSymbol(Symbol paramSymbol, P paramP);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Symbol
 * JD-Core Version:    0.6.2
 */