/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import com.sun.codemodel.internal.writer.FileCodeWriter;
/*     */ import com.sun.codemodel.internal.writer.ProgressCodeWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public final class JCodeModel
/*     */ {
/*  83 */   private HashMap<String, JPackage> packages = new HashMap();
/*     */ 
/*  86 */   private final HashMap<Class<?>, JReferencedClass> refClasses = new HashMap();
/*     */ 
/*  90 */   public final JNullType NULL = new JNullType(this);
/*     */ 
/*  92 */   public final JPrimitiveType VOID = new JPrimitiveType(this, "void", Void.class);
/*  93 */   public final JPrimitiveType BOOLEAN = new JPrimitiveType(this, "boolean", Boolean.class);
/*  94 */   public final JPrimitiveType BYTE = new JPrimitiveType(this, "byte", Byte.class);
/*  95 */   public final JPrimitiveType SHORT = new JPrimitiveType(this, "short", Short.class);
/*  96 */   public final JPrimitiveType CHAR = new JPrimitiveType(this, "char", Character.class);
/*  97 */   public final JPrimitiveType INT = new JPrimitiveType(this, "int", Integer.class);
/*  98 */   public final JPrimitiveType FLOAT = new JPrimitiveType(this, "float", Float.class);
/*  99 */   public final JPrimitiveType LONG = new JPrimitiveType(this, "long", Long.class);
/* 100 */   public final JPrimitiveType DOUBLE = new JPrimitiveType(this, "double", Double.class);
/*     */ 
/* 106 */   protected static final boolean isCaseSensitiveFileSystem = getFileSystemCaseSensitivity();
/*     */   private JClass wildcard;
/* 664 */   public static final Map<Class<?>, Class<?>> primitiveToBox = Collections.unmodifiableMap(m2);
/*     */   public static final Map<Class<?>, Class<?>> boxToPrimitive;
/*     */ 
/*     */   private static boolean getFileSystemCaseSensitivity()
/*     */   {
/*     */     try
/*     */     {
/* 112 */       if (System.getProperty("com.sun.codemodel.internal.FileSystemCaseSensitive") != null)
/* 113 */         return true;
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 117 */     return File.separatorChar == '/';
/*     */   }
/*     */ 
/*     */   public JPackage _package(String name)
/*     */   {
/* 132 */     JPackage p = (JPackage)this.packages.get(name);
/* 133 */     if (p == null) {
/* 134 */       p = new JPackage(name, this);
/* 135 */       this.packages.put(name, p);
/*     */     }
/* 137 */     return p;
/*     */   }
/*     */ 
/*     */   public final JPackage rootPackage() {
/* 141 */     return _package("");
/*     */   }
/*     */ 
/*     */   public Iterator<JPackage> packages()
/*     */   {
/* 149 */     return this.packages.values().iterator();
/*     */   }
/*     */ 
/*     */   public JDefinedClass _class(String fullyqualifiedName)
/*     */     throws JClassAlreadyExistsException
/*     */   {
/* 159 */     return _class(fullyqualifiedName, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */   public JClass directClass(String name)
/*     */   {
/* 170 */     return new JDirectClass(this, name);
/*     */   }
/*     */ 
/*     */   public JDefinedClass _class(int mods, String fullyqualifiedName, ClassType t)
/*     */     throws JClassAlreadyExistsException
/*     */   {
/* 180 */     int idx = fullyqualifiedName.lastIndexOf('.');
/* 181 */     if (idx < 0) return rootPackage()._class(fullyqualifiedName);
/*     */ 
/* 184 */     return _package(fullyqualifiedName.substring(0, idx))
/* 184 */       ._class(mods, fullyqualifiedName
/* 184 */       .substring(idx + 1), 
/* 184 */       t);
/*     */   }
/*     */ 
/*     */   public JDefinedClass _class(String fullyqualifiedName, ClassType t)
/*     */     throws JClassAlreadyExistsException
/*     */   {
/* 194 */     return _class(1, fullyqualifiedName, t);
/*     */   }
/*     */ 
/*     */   public JDefinedClass _getClass(String fullyQualifiedName)
/*     */   {
/* 205 */     int idx = fullyQualifiedName.lastIndexOf('.');
/* 206 */     if (idx < 0) return rootPackage()._getClass(fullyQualifiedName);
/*     */ 
/* 209 */     return _package(fullyQualifiedName.substring(0, idx))
/* 209 */       ._getClass(fullyQualifiedName
/* 209 */       .substring(idx + 1));
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public JDefinedClass newAnonymousClass(JClass baseType)
/*     */   {
/* 220 */     return new JAnonymousClass(baseType);
/*     */   }
/*     */ 
/*     */   public JDefinedClass anonymousClass(JClass baseType)
/*     */   {
/* 227 */     return new JAnonymousClass(baseType);
/*     */   }
/*     */ 
/*     */   public JDefinedClass anonymousClass(Class<?> baseType) {
/* 231 */     return anonymousClass(ref(baseType));
/*     */   }
/*     */ 
/*     */   public void build(File destDir, PrintStream status)
/*     */     throws IOException
/*     */   {
/* 244 */     build(destDir, destDir, status);
/*     */   }
/*     */ 
/*     */   public void build(File srcDir, File resourceDir, PrintStream status)
/*     */     throws IOException
/*     */   {
/* 259 */     CodeWriter src = new FileCodeWriter(srcDir);
/* 260 */     CodeWriter res = new FileCodeWriter(resourceDir);
/* 261 */     if (status != null) {
/* 262 */       src = new ProgressCodeWriter(src, status);
/* 263 */       res = new ProgressCodeWriter(res, status);
/*     */     }
/* 265 */     build(src, res);
/*     */   }
/*     */ 
/*     */   public void build(File destDir)
/*     */     throws IOException
/*     */   {
/* 272 */     build(destDir, System.out);
/*     */   }
/*     */ 
/*     */   public void build(File srcDir, File resourceDir)
/*     */     throws IOException
/*     */   {
/* 279 */     build(srcDir, resourceDir, System.out);
/*     */   }
/*     */ 
/*     */   public void build(CodeWriter out)
/*     */     throws IOException
/*     */   {
/* 286 */     build(out, out);
/*     */   }
/*     */ 
/*     */   public void build(CodeWriter source, CodeWriter resource)
/*     */     throws IOException
/*     */   {
/* 293 */     JPackage[] pkgs = (JPackage[])this.packages.values().toArray(new JPackage[this.packages.size()]);
/*     */ 
/* 295 */     for (JPackage pkg : pkgs)
/* 296 */       pkg.build(source, resource);
/* 297 */     source.close();
/* 298 */     resource.close();
/*     */   }
/*     */ 
/*     */   public int countArtifacts()
/*     */   {
/* 306 */     int r = 0;
/* 307 */     JPackage[] pkgs = (JPackage[])this.packages.values().toArray(new JPackage[this.packages.size()]);
/*     */ 
/* 309 */     for (JPackage pkg : pkgs)
/* 310 */       r += pkg.countArtifacts();
/* 311 */     return r;
/*     */   }
/*     */ 
/*     */   public JClass ref(Class<?> clazz)
/*     */   {
/* 324 */     JReferencedClass jrc = (JReferencedClass)this.refClasses.get(clazz);
/* 325 */     if (jrc == null) {
/* 326 */       if (clazz.isPrimitive())
/* 327 */         throw new IllegalArgumentException(clazz + " is a primitive");
/* 328 */       if (clazz.isArray()) {
/* 329 */         return new JArrayClass(this, _ref(clazz.getComponentType()));
/*     */       }
/* 331 */       jrc = new JReferencedClass(clazz);
/* 332 */       this.refClasses.put(clazz, jrc);
/*     */     }
/*     */ 
/* 335 */     return jrc;
/*     */   }
/*     */ 
/*     */   public JType _ref(Class<?> c) {
/* 339 */     if (c.isPrimitive()) {
/* 340 */       return JType.parse(this, c.getName());
/*     */     }
/* 342 */     return ref(c);
/*     */   }
/*     */ 
/*     */   public JClass ref(String fullyQualifiedClassName)
/*     */   {
/*     */     try
/*     */     {
/* 357 */       return ref(SecureLoader.getContextClassLoader().loadClass(fullyQualifiedClassName));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */       try
/*     */       {
/* 363 */         return ref(Class.forName(fullyQualifiedClassName));
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException1)
/*     */       {
/*     */       }
/*     */     }
/* 369 */     return new JDirectClass(this, fullyQualifiedClassName);
/*     */   }
/*     */ 
/*     */   public JClass wildcard()
/*     */   {
/* 382 */     if (this.wildcard == null)
/* 383 */       this.wildcard = ref(Object.class).wildcard();
/* 384 */     return this.wildcard;
/*     */   }
/*     */ 
/*     */   public JType parseType(String name)
/*     */     throws ClassNotFoundException
/*     */   {
/* 398 */     if (name.endsWith("[]")) {
/* 399 */       return parseType(name.substring(0, name.length() - 2)).array();
/*     */     }
/*     */     try
/*     */     {
/* 403 */       return JType.parse(this, name);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/*     */     }
/*     */ 
/* 409 */     return new TypeNameParser(name).parseTypeName();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 647 */     Map m1 = new HashMap();
/* 648 */     Map m2 = new HashMap();
/*     */ 
/* 650 */     m1.put(Boolean.class, Boolean.TYPE);
/* 651 */     m1.put(Byte.class, Byte.TYPE);
/* 652 */     m1.put(Character.class, Character.TYPE);
/* 653 */     m1.put(Double.class, Double.TYPE);
/* 654 */     m1.put(Float.class, Float.TYPE);
/* 655 */     m1.put(Integer.class, Integer.TYPE);
/* 656 */     m1.put(Long.class, Long.TYPE);
/* 657 */     m1.put(Short.class, Short.TYPE);
/* 658 */     m1.put(Void.class, Void.TYPE);
/*     */ 
/* 660 */     for (Map.Entry e : m1.entrySet()) {
/* 661 */       m2.put(e.getValue(), e.getKey());
/*     */     }
/* 663 */     boxToPrimitive = Collections.unmodifiableMap(m1);
/*     */   }
/*     */ 
/*     */   private class JReferencedClass extends JClass
/*     */     implements JDeclaration
/*     */   {
/*     */     private final Class<?> _class;
/*     */ 
/*     */     JReferencedClass()
/*     */     {
/* 538 */       super();
/* 539 */       this._class = _clazz;
/* 540 */       assert (!this._class.isArray());
/*     */     }
/*     */ 
/*     */     public String name() {
/* 544 */       return this._class.getSimpleName().replace('$', '.');
/*     */     }
/*     */ 
/*     */     public String fullName() {
/* 548 */       return this._class.getName().replace('$', '.');
/*     */     }
/*     */ 
/*     */     public String binaryName() {
/* 552 */       return this._class.getName();
/*     */     }
/*     */ 
/*     */     public JClass outer() {
/* 556 */       Class p = this._class.getDeclaringClass();
/* 557 */       if (p == null) return null;
/* 558 */       return JCodeModel.this.ref(p);
/*     */     }
/*     */ 
/*     */     public JPackage _package() {
/* 562 */       String name = fullName();
/*     */ 
/* 565 */       if (name.indexOf('[') != -1) {
/* 566 */         return JCodeModel.this._package("");
/*     */       }
/*     */ 
/* 569 */       int idx = name.lastIndexOf('.');
/* 570 */       if (idx < 0) {
/* 571 */         return JCodeModel.this._package("");
/*     */       }
/* 573 */       return JCodeModel.this._package(name.substring(0, idx));
/*     */     }
/*     */ 
/*     */     public JClass _extends() {
/* 577 */       Class sp = this._class.getSuperclass();
/* 578 */       if (sp == null) {
/* 579 */         if (isInterface())
/* 580 */           return owner().ref(Object.class);
/* 581 */         return null;
/*     */       }
/* 583 */       return JCodeModel.this.ref(sp);
/*     */     }
/*     */ 
/*     */     public Iterator<JClass> _implements() {
/* 587 */       final Class[] interfaces = this._class.getInterfaces();
/* 588 */       return new Iterator() {
/* 589 */         private int idx = 0;
/*     */ 
/* 591 */         public boolean hasNext() { return this.idx < interfaces.length; }
/*     */ 
/*     */         public JClass next() {
/* 594 */           return JCodeModel.this.ref(interfaces[(this.idx++)]);
/*     */         }
/*     */         public void remove() {
/* 597 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public boolean isInterface() {
/* 603 */       return this._class.isInterface();
/*     */     }
/*     */ 
/*     */     public boolean isAbstract() {
/* 607 */       return Modifier.isAbstract(this._class.getModifiers());
/*     */     }
/*     */ 
/*     */     public JPrimitiveType getPrimitiveType() {
/* 611 */       Class v = (Class)JCodeModel.boxToPrimitive.get(this._class);
/* 612 */       if (v != null) {
/* 613 */         return JType.parse(JCodeModel.this, v.getName());
/*     */       }
/* 615 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean isArray() {
/* 619 */       return false;
/*     */     }
/*     */ 
/*     */     public void declare(JFormatter f)
/*     */     {
/*     */     }
/*     */ 
/*     */     public JTypeVar[] typeParams() {
/* 627 */       return super.typeParams();
/*     */     }
/*     */ 
/*     */     protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings)
/*     */     {
/* 632 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class TypeNameParser
/*     */   {
/*     */     private final String s;
/*     */     private int idx;
/*     */ 
/*     */     public TypeNameParser(String s)
/*     */     {
/* 417 */       this.s = s;
/*     */     }
/*     */ 
/*     */     JClass parseTypeName()
/*     */       throws ClassNotFoundException
/*     */     {
/* 427 */       int start = this.idx;
/*     */ 
/* 429 */       if (this.s.charAt(this.idx) == '?')
/*     */       {
/* 431 */         this.idx += 1;
/* 432 */         ws();
/* 433 */         String head = this.s.substring(this.idx);
/* 434 */         if (head.startsWith("extends")) {
/* 435 */           this.idx += 7;
/* 436 */           ws();
/* 437 */           return parseTypeName().wildcard();
/*     */         }
/* 439 */         if (head.startsWith("super")) {
/* 440 */           throw new UnsupportedOperationException("? super T not implemented");
/*     */         }
/*     */ 
/* 443 */         throw new IllegalArgumentException("only extends/super can follow ?, but found " + this.s.substring(this.idx));
/*     */       }
/*     */ 
/* 447 */       while (this.idx < this.s.length()) {
/* 448 */         char ch = this.s.charAt(this.idx);
/* 449 */         if ((!Character.isJavaIdentifierStart(ch)) && 
/* 450 */           (!Character.isJavaIdentifierPart(ch)) && 
/* 450 */           (ch != '.'))
/*     */           break;
/* 452 */         this.idx += 1;
/*     */       }
/*     */ 
/* 457 */       JClass clazz = JCodeModel.this.ref(this.s.substring(start, this.idx));
/*     */ 
/* 459 */       return parseSuffix(clazz);
/*     */     }
/*     */ 
/*     */     private JClass parseSuffix(JClass clazz)
/*     */       throws ClassNotFoundException
/*     */     {
/* 467 */       if (this.idx == this.s.length()) {
/* 468 */         return clazz;
/*     */       }
/* 470 */       char ch = this.s.charAt(this.idx);
/*     */ 
/* 472 */       if (ch == '<') {
/* 473 */         return parseSuffix(parseArguments(clazz));
/*     */       }
/* 475 */       if (ch == '[') {
/* 476 */         if (this.s.charAt(this.idx + 1) == ']') {
/* 477 */           this.idx += 2;
/* 478 */           return parseSuffix(clazz.array());
/*     */         }
/* 480 */         throw new IllegalArgumentException("Expected ']' but found " + this.s.substring(this.idx + 1));
/*     */       }
/*     */ 
/* 483 */       return clazz;
/*     */     }
/*     */ 
/*     */     private void ws()
/*     */     {
/* 490 */       while ((Character.isWhitespace(this.s.charAt(this.idx))) && (this.idx < this.s.length()))
/* 491 */         this.idx += 1;
/*     */     }
/*     */ 
/*     */     private JClass parseArguments(JClass rawType)
/*     */       throws ClassNotFoundException
/*     */     {
/* 500 */       if (this.s.charAt(this.idx) != '<')
/* 501 */         throw new IllegalArgumentException();
/* 502 */       this.idx += 1;
/*     */ 
/* 504 */       List args = new ArrayList();
/*     */       while (true)
/*     */       {
/* 507 */         args.add(parseTypeName());
/* 508 */         if (this.idx == this.s.length())
/* 509 */           throw new IllegalArgumentException("Missing '>' in " + this.s);
/* 510 */         char ch = this.s.charAt(this.idx);
/* 511 */         if (ch == '>') {
/* 512 */           return rawType.narrow((JClass[])args.toArray(new JClass[args.size()]));
/*     */         }
/* 514 */         if (ch != ',')
/* 515 */           throw new IllegalArgumentException(this.s);
/* 516 */         this.idx += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JCodeModel
 * JD-Core Version:    0.6.2
 */