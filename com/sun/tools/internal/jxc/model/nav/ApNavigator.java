/*     */ package com.sun.tools.internal.jxc.model.nav;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.LineMap;
/*     */ import com.sun.source.util.SourcePositions;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.source.util.Trees;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.type.WildcardType;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor6;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public final class ApNavigator
/*     */   implements Navigator<TypeMirror, TypeElement, VariableElement, ExecutableElement>
/*     */ {
/*     */   private final ProcessingEnvironment env;
/*     */   private final PrimitiveType primitiveByte;
/*     */   private static final Map<Class, TypeKind> primitives;
/* 367 */   private static final TypeMirror DUMMY = new TypeMirror()
/*     */   {
/*     */     public <R, P> R accept(TypeVisitor<R, P> v, P p) {
/* 370 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public TypeKind getKind()
/*     */     {
/* 375 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public List<? extends AnnotationMirror> getAnnotationMirrors()
/*     */     {
/* 380 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> annotationType)
/*     */     {
/* 385 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType)
/*     */     {
/* 390 */       throw new IllegalStateException();
/*     */     }
/* 367 */   };
/*     */ 
/* 463 */   private final SimpleTypeVisitor6<TypeMirror, TypeElement> baseClassFinder = new SimpleTypeVisitor6()
/*     */   {
/*     */     public TypeMirror visitDeclared(DeclaredType t, TypeElement sup) {
/* 466 */       if (t.asElement().equals(sup)) {
/* 467 */         return t;
/*     */       }
/* 469 */       for (TypeMirror i : ApNavigator.this.env.getTypeUtils().directSupertypes(t)) {
/* 470 */         TypeMirror r = visitDeclared((DeclaredType)i, sup);
/* 471 */         if (r != null) {
/* 472 */           return r;
/*     */         }
/*     */       }
/*     */ 
/* 476 */       TypeMirror superclass = ((TypeElement)t.asElement()).getSuperclass();
/* 477 */       if (!superclass.getKind().equals(TypeKind.NONE)) {
/* 478 */         TypeMirror r = visitDeclared((DeclaredType)superclass, sup);
/* 479 */         if (r != null)
/* 480 */           return r;
/*     */       }
/* 482 */       return null;
/*     */     }
/*     */ 
/*     */     public TypeMirror visitTypeVariable(TypeVariable t, TypeElement typeElement)
/*     */     {
/* 489 */       for (TypeMirror typeMirror : ((TypeParameterElement)t.asElement()).getBounds()) {
/* 490 */         TypeMirror m = (TypeMirror)visit(typeMirror, typeElement);
/* 491 */         if (m != null)
/* 492 */           return m;
/*     */       }
/* 494 */       return null;
/*     */     }
/*     */ 
/*     */     public TypeMirror visitArray(ArrayType t, TypeElement typeElement)
/*     */     {
/* 502 */       return null;
/*     */     }
/*     */ 
/*     */     public TypeMirror visitWildcard(WildcardType t, TypeElement typeElement)
/*     */     {
/* 509 */       return (TypeMirror)visit(t.getExtendsBound(), typeElement);
/*     */     }
/*     */ 
/*     */     protected TypeMirror defaultAction(TypeMirror e, TypeElement typeElement)
/*     */     {
/* 514 */       return e;
/*     */     }
/* 463 */   };
/*     */ 
/*     */   public ApNavigator(ProcessingEnvironment env)
/*     */   {
/*  74 */     this.env = env;
/*  75 */     this.primitiveByte = env.getTypeUtils().getPrimitiveType(TypeKind.BYTE);
/*     */   }
/*     */ 
/*     */   public TypeElement getSuperClass(TypeElement typeElement) {
/*  79 */     if (typeElement.getKind().equals(ElementKind.CLASS)) {
/*  80 */       TypeMirror sup = typeElement.getSuperclass();
/*  81 */       if (!sup.getKind().equals(TypeKind.NONE)) {
/*  82 */         return (TypeElement)((DeclaredType)sup).asElement();
/*     */       }
/*  84 */       return null;
/*     */     }
/*  86 */     return this.env.getElementUtils().getTypeElement(Object.class.getName());
/*     */   }
/*     */ 
/*     */   public TypeMirror getBaseClass(TypeMirror type, TypeElement sup) {
/*  90 */     return (TypeMirror)this.baseClassFinder.visit(type, sup);
/*     */   }
/*     */ 
/*     */   public String getClassName(TypeElement t) {
/*  94 */     return t.getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   public String getTypeName(TypeMirror typeMirror) {
/*  98 */     return typeMirror.toString();
/*     */   }
/*     */ 
/*     */   public String getClassShortName(TypeElement t) {
/* 102 */     return t.getSimpleName().toString();
/*     */   }
/*     */ 
/*     */   public Collection<VariableElement> getDeclaredFields(TypeElement typeElement) {
/* 106 */     return ElementFilter.fieldsIn(typeElement.getEnclosedElements());
/*     */   }
/*     */ 
/*     */   public VariableElement getDeclaredField(TypeElement clazz, String fieldName) {
/* 110 */     for (VariableElement fd : ElementFilter.fieldsIn(clazz.getEnclosedElements())) {
/* 111 */       if (fd.getSimpleName().toString().equals(fieldName))
/* 112 */         return fd;
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   public Collection<ExecutableElement> getDeclaredMethods(TypeElement typeElement) {
/* 118 */     return ElementFilter.methodsIn(typeElement.getEnclosedElements());
/*     */   }
/*     */ 
/*     */   public TypeElement getDeclaringClassForField(VariableElement f) {
/* 122 */     return (TypeElement)f.getEnclosingElement();
/*     */   }
/*     */ 
/*     */   public TypeElement getDeclaringClassForMethod(ExecutableElement m) {
/* 126 */     return (TypeElement)m.getEnclosingElement();
/*     */   }
/*     */ 
/*     */   public TypeMirror getFieldType(VariableElement f) {
/* 130 */     return f.asType();
/*     */   }
/*     */ 
/*     */   public String getFieldName(VariableElement f) {
/* 134 */     return f.getSimpleName().toString();
/*     */   }
/*     */ 
/*     */   public String getMethodName(ExecutableElement m) {
/* 138 */     return m.getSimpleName().toString();
/*     */   }
/*     */ 
/*     */   public TypeMirror getReturnType(ExecutableElement m) {
/* 142 */     return m.getReturnType();
/*     */   }
/*     */ 
/*     */   public TypeMirror[] getMethodParameters(ExecutableElement m) {
/* 146 */     Collection ps = m.getParameters();
/* 147 */     TypeMirror[] r = new TypeMirror[ps.size()];
/* 148 */     int i = 0;
/* 149 */     for (VariableElement p : ps)
/* 150 */       r[(i++)] = p.asType();
/* 151 */     return r;
/*     */   }
/*     */ 
/*     */   public boolean isStaticMethod(ExecutableElement m) {
/* 155 */     return hasModifier(m, Modifier.STATIC);
/*     */   }
/*     */ 
/*     */   public boolean isFinalMethod(ExecutableElement m) {
/* 159 */     return hasModifier(m, Modifier.FINAL);
/*     */   }
/*     */ 
/*     */   private boolean hasModifier(Element d, Modifier mod) {
/* 163 */     return d.getModifiers().contains(mod);
/*     */   }
/*     */ 
/*     */   public boolean isSubClassOf(TypeMirror sub, TypeMirror sup) {
/* 167 */     if (sup == DUMMY)
/*     */     {
/* 171 */       return false;
/* 172 */     }return this.env.getTypeUtils().isSubtype(sub, sup);
/*     */   }
/*     */ 
/*     */   private String getSourceClassName(Class clazz) {
/* 176 */     Class d = clazz.getDeclaringClass();
/* 177 */     if (d == null) {
/* 178 */       return clazz.getName();
/*     */     }
/* 180 */     String shortName = clazz.getName().substring(d.getName().length() + 1);
/* 181 */     return getSourceClassName(d) + '.' + shortName;
/*     */   }
/*     */ 
/*     */   public TypeMirror ref(Class c)
/*     */   {
/* 186 */     if (c.isArray())
/* 187 */       return this.env.getTypeUtils().getArrayType(ref(c.getComponentType()));
/* 188 */     if (c.isPrimitive())
/* 189 */       return getPrimitive(c);
/* 190 */     TypeElement t = this.env.getElementUtils().getTypeElement(getSourceClassName(c));
/*     */ 
/* 199 */     if (t == null)
/* 200 */       return DUMMY;
/* 201 */     return this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]);
/*     */   }
/*     */ 
/*     */   public TypeMirror use(TypeElement t) {
/* 205 */     assert (t != null);
/* 206 */     return this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]);
/*     */   }
/*     */ 
/*     */   public TypeElement asDecl(TypeMirror m) {
/* 210 */     m = this.env.getTypeUtils().erasure(m);
/* 211 */     if (m.getKind().equals(TypeKind.DECLARED)) {
/* 212 */       DeclaredType d = (DeclaredType)m;
/* 213 */       return (TypeElement)d.asElement();
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */ 
/*     */   public TypeElement asDecl(Class c) {
/* 219 */     return this.env.getElementUtils().getTypeElement(getSourceClassName(c));
/*     */   }
/*     */ 
/*     */   public TypeMirror erasure(TypeMirror t) {
/* 223 */     Types tu = this.env.getTypeUtils();
/* 224 */     t = tu.erasure(t);
/* 225 */     if (t.getKind().equals(TypeKind.DECLARED)) {
/* 226 */       DeclaredType dt = (DeclaredType)t;
/* 227 */       if (!dt.getTypeArguments().isEmpty())
/* 228 */         return tu.getDeclaredType((TypeElement)dt.asElement(), new TypeMirror[0]);
/*     */     }
/* 230 */     return t;
/*     */   }
/*     */ 
/*     */   public boolean isAbstract(TypeElement clazz) {
/* 234 */     return hasModifier(clazz, Modifier.ABSTRACT);
/*     */   }
/*     */ 
/*     */   public boolean isFinal(TypeElement clazz) {
/* 238 */     return hasModifier(clazz, Modifier.FINAL);
/*     */   }
/*     */ 
/*     */   public VariableElement[] getEnumConstants(TypeElement clazz) {
/* 242 */     List elements = this.env.getElementUtils().getAllMembers(clazz);
/* 243 */     Collection constants = new ArrayList();
/* 244 */     for (Element element : elements) {
/* 245 */       if (element.getKind().equals(ElementKind.ENUM_CONSTANT)) {
/* 246 */         constants.add((VariableElement)element);
/*     */       }
/*     */     }
/* 249 */     return (VariableElement[])constants.toArray(new VariableElement[constants.size()]);
/*     */   }
/*     */ 
/*     */   public TypeMirror getVoidType() {
/* 253 */     return this.env.getTypeUtils().getNoType(TypeKind.VOID);
/*     */   }
/*     */ 
/*     */   public String getPackageName(TypeElement clazz) {
/* 257 */     return this.env.getElementUtils().getPackageOf(clazz).getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   public TypeElement loadObjectFactory(TypeElement referencePoint, String packageName)
/*     */   {
/* 262 */     return this.env.getElementUtils().getTypeElement(packageName + ".ObjectFactory");
/*     */   }
/*     */ 
/*     */   public boolean isBridgeMethod(ExecutableElement method) {
/* 266 */     return method.getModifiers().contains(Modifier.VOLATILE);
/*     */   }
/*     */ 
/*     */   public boolean isOverriding(ExecutableElement method, TypeElement base) {
/* 270 */     Elements elements = this.env.getElementUtils();
/*     */     while (true)
/*     */     {
/* 273 */       for (ExecutableElement m : ElementFilter.methodsIn(elements.getAllMembers(base))) {
/* 274 */         if (elements.overrides(method, m, base)) {
/* 275 */           return true;
/*     */         }
/*     */       }
/* 278 */       if (base.getSuperclass().getKind().equals(TypeKind.NONE))
/* 279 */         return false;
/* 280 */       base = (TypeElement)this.env.getTypeUtils().asElement(base.getSuperclass());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isInterface(TypeElement clazz) {
/* 285 */     return clazz.getKind().isInterface();
/*     */   }
/*     */ 
/*     */   public boolean isTransient(VariableElement f) {
/* 289 */     return f.getModifiers().contains(Modifier.TRANSIENT);
/*     */   }
/*     */ 
/*     */   public boolean isInnerClass(TypeElement clazz) {
/* 293 */     return (clazz.getEnclosingElement() != null) && (!clazz.getModifiers().contains(Modifier.STATIC));
/*     */   }
/*     */ 
/*     */   public boolean isSameType(TypeMirror t1, TypeMirror t2)
/*     */   {
/* 298 */     return this.env.getTypeUtils().isSameType(t1, t2);
/*     */   }
/*     */ 
/*     */   public boolean isArray(TypeMirror type) {
/* 302 */     return (type != null) && (type.getKind().equals(TypeKind.ARRAY));
/*     */   }
/*     */ 
/*     */   public boolean isArrayButNotByteArray(TypeMirror t) {
/* 306 */     if (!isArray(t)) {
/* 307 */       return false;
/*     */     }
/* 309 */     ArrayType at = (ArrayType)t;
/* 310 */     TypeMirror ct = at.getComponentType();
/*     */ 
/* 312 */     return !ct.equals(this.primitiveByte);
/*     */   }
/*     */ 
/*     */   public TypeMirror getComponentType(TypeMirror t) {
/* 316 */     if (isArray(t)) {
/* 317 */       ArrayType at = (ArrayType)t;
/* 318 */       return at.getComponentType();
/*     */     }
/*     */ 
/* 321 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public TypeMirror getTypeArgument(TypeMirror typeMirror, int i) {
/* 325 */     if ((typeMirror != null) && (typeMirror.getKind().equals(TypeKind.DECLARED))) {
/* 326 */       DeclaredType declaredType = (DeclaredType)typeMirror;
/* 327 */       TypeMirror[] args = (TypeMirror[])declaredType.getTypeArguments().toArray(new TypeMirror[declaredType.getTypeArguments().size()]);
/* 328 */       return args[i];
/* 329 */     }throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public boolean isParameterizedType(TypeMirror typeMirror) {
/* 333 */     if ((typeMirror != null) && (typeMirror.getKind().equals(TypeKind.DECLARED))) {
/* 334 */       DeclaredType d = (DeclaredType)typeMirror;
/* 335 */       return !d.getTypeArguments().isEmpty();
/*     */     }
/* 337 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive(TypeMirror t) {
/* 341 */     return t.getKind().isPrimitive();
/*     */   }
/*     */ 
/*     */   public TypeMirror getPrimitive(Class primitiveType)
/*     */   {
/* 358 */     assert (primitiveType.isPrimitive());
/* 359 */     if (primitiveType == Void.TYPE)
/* 360 */       return getVoidType();
/* 361 */     return this.env.getTypeUtils().getPrimitiveType((TypeKind)primitives.get(primitiveType));
/*     */   }
/*     */ 
/*     */   public Location getClassLocation(TypeElement typeElement)
/*     */   {
/* 395 */     Trees trees = Trees.instance(this.env);
/* 396 */     return getLocation(typeElement.getQualifiedName().toString(), trees.getPath(typeElement));
/*     */   }
/*     */ 
/*     */   public Location getFieldLocation(VariableElement variableElement) {
/* 400 */     return getLocation(variableElement);
/*     */   }
/*     */ 
/*     */   public Location getMethodLocation(ExecutableElement executableElement) {
/* 404 */     return getLocation(executableElement);
/*     */   }
/*     */ 
/*     */   public boolean hasDefaultConstructor(TypeElement t) {
/* 408 */     if ((t == null) || (!t.getKind().equals(ElementKind.CLASS))) {
/* 409 */       return false;
/*     */     }
/* 411 */     for (ExecutableElement init : ElementFilter.constructorsIn(this.env.getElementUtils().getAllMembers(t))) {
/* 412 */       if (init.getParameters().isEmpty())
/* 413 */         return true;
/*     */     }
/* 415 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isStaticField(VariableElement f) {
/* 419 */     return hasModifier(f, Modifier.STATIC);
/*     */   }
/*     */ 
/*     */   public boolean isPublicMethod(ExecutableElement m) {
/* 423 */     return hasModifier(m, Modifier.PUBLIC);
/*     */   }
/*     */ 
/*     */   public boolean isPublicField(VariableElement f) {
/* 427 */     return hasModifier(f, Modifier.PUBLIC);
/*     */   }
/*     */ 
/*     */   public boolean isEnum(TypeElement t) {
/* 431 */     return (t != null) && (t.getKind().equals(ElementKind.ENUM));
/*     */   }
/*     */ 
/*     */   private Location getLocation(Element element) {
/* 435 */     Trees trees = Trees.instance(this.env);
/* 436 */     return getLocation(
/* 437 */       ((TypeElement)element
/* 437 */       .getEnclosingElement()).getQualifiedName() + "." + element.getSimpleName(), trees
/* 438 */       .getPath(element));
/*     */   }
/*     */ 
/*     */   private Location getLocation(final String name, final TreePath treePath)
/*     */   {
/* 443 */     return new Location() {
/*     */       public String toString() {
/* 445 */         if (treePath == null) {
/* 446 */           return name + " (Unknown Source)";
/*     */         }
/*     */ 
/* 450 */         CompilationUnitTree compilationUnit = treePath.getCompilationUnit();
/* 451 */         Trees trees = Trees.instance(ApNavigator.this.env);
/* 452 */         long startPosition = trees.getSourcePositions().getStartPosition(compilationUnit, treePath.getLeaf());
/*     */ 
/* 454 */         return name + "(" + compilationUnit
/* 454 */           .getSourceFile().getName() + ":" + compilationUnit.getLineMap().getLineNumber(startPosition) + ")";
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 344 */     primitives = new HashMap();
/*     */ 
/* 347 */     primitives.put(Integer.TYPE, TypeKind.INT);
/* 348 */     primitives.put(Byte.TYPE, TypeKind.BYTE);
/* 349 */     primitives.put(Float.TYPE, TypeKind.FLOAT);
/* 350 */     primitives.put(Boolean.TYPE, TypeKind.BOOLEAN);
/* 351 */     primitives.put(Short.TYPE, TypeKind.SHORT);
/* 352 */     primitives.put(Long.TYPE, TypeKind.LONG);
/* 353 */     primitives.put(Double.TYPE, TypeKind.DOUBLE);
/* 354 */     primitives.put(Character.TYPE, TypeKind.CHAR);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.model.nav.ApNavigator
 * JD-Core Version:    0.6.2
 */