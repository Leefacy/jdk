/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Snapshot
/*     */ {
/*  53 */   public static long SMALL_ID_MASK = 4294967295L;
/*  54 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*  56 */   private static final JavaField[] EMPTY_FIELD_ARRAY = new JavaField[0];
/*  57 */   private static final JavaStatic[] EMPTY_STATIC_ARRAY = new JavaStatic[0];
/*     */ 
/*  60 */   private Hashtable<Number, JavaHeapObject> heapObjects = new Hashtable();
/*     */ 
/*  63 */   private Hashtable<Number, JavaClass> fakeClasses = new Hashtable();
/*     */ 
/*  67 */   private Vector<Root> roots = new Vector();
/*     */ 
/*  70 */   private Map<String, JavaClass> classes = new TreeMap();
/*     */   private volatile Map<JavaHeapObject, Boolean> newObjects;
/*     */   private volatile Map<JavaHeapObject, StackTrace> siteTraces;
/*  80 */   private Map<JavaHeapObject, Root> rootsMap = new HashMap();
/*     */   private SoftReference<Vector> finalizablesCache;
/*     */   private JavaThing nullThing;
/*     */   private JavaClass weakReferenceClass;
/*     */   private int referentFieldIndex;
/*     */   private JavaClass javaLangClass;
/*     */   private JavaClass javaLangString;
/*     */   private JavaClass javaLangClassLoader;
/*     */   private volatile JavaClass otherArrayType;
/*     */   private ReachableExcludes reachableExcludes;
/*     */   private ReadBuffer readBuf;
/*     */   private boolean hasNewSet;
/*     */   private boolean unresolvedObjectsOK;
/*     */   private boolean newStyleArrayClass;
/* 117 */   private int identifierSize = 4;
/*     */   private int minimumObjectSize;
/*     */   private static final int DOT_LIMIT = 5000;
/*     */ 
/*     */   public Snapshot(ReadBuffer paramReadBuffer)
/*     */   {
/* 125 */     this.nullThing = new HackJavaValue("<null>", 0);
/* 126 */     this.readBuf = paramReadBuffer;
/*     */   }
/*     */ 
/*     */   public void setSiteTrace(JavaHeapObject paramJavaHeapObject, StackTrace paramStackTrace) {
/* 130 */     if ((paramStackTrace != null) && (paramStackTrace.getFrames().length != 0)) {
/* 131 */       initSiteTraces();
/* 132 */       this.siteTraces.put(paramJavaHeapObject, paramStackTrace);
/*     */     }
/*     */   }
/*     */ 
/*     */   public StackTrace getSiteTrace(JavaHeapObject paramJavaHeapObject) {
/* 137 */     if (this.siteTraces != null) {
/* 138 */       return (StackTrace)this.siteTraces.get(paramJavaHeapObject);
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public void setNewStyleArrayClass(boolean paramBoolean)
/*     */   {
/* 145 */     this.newStyleArrayClass = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isNewStyleArrayClass() {
/* 149 */     return this.newStyleArrayClass;
/*     */   }
/*     */ 
/*     */   public void setIdentifierSize(int paramInt) {
/* 153 */     this.identifierSize = paramInt;
/* 154 */     this.minimumObjectSize = (2 * paramInt);
/*     */   }
/*     */ 
/*     */   public int getIdentifierSize() {
/* 158 */     return this.identifierSize;
/*     */   }
/*     */ 
/*     */   public int getMinimumObjectSize() {
/* 162 */     return this.minimumObjectSize;
/*     */   }
/*     */ 
/*     */   public void addHeapObject(long paramLong, JavaHeapObject paramJavaHeapObject) {
/* 166 */     this.heapObjects.put(makeId(paramLong), paramJavaHeapObject);
/*     */   }
/*     */ 
/*     */   public void addRoot(Root paramRoot) {
/* 170 */     paramRoot.setIndex(this.roots.size());
/* 171 */     this.roots.addElement(paramRoot);
/*     */   }
/*     */ 
/*     */   public void addClass(long paramLong, JavaClass paramJavaClass) {
/* 175 */     addHeapObject(paramLong, paramJavaClass);
/* 176 */     putInClassesMap(paramJavaClass);
/*     */   }
/*     */ 
/*     */   JavaClass addFakeInstanceClass(long paramLong, int paramInt)
/*     */   {
/* 181 */     String str = "unknown-class<@" + Misc.toHex(paramLong) + ">";
/*     */ 
/* 186 */     int i = paramInt / 4;
/* 187 */     int j = paramInt % 4;
/* 188 */     JavaField[] arrayOfJavaField = new JavaField[i + j];
/*     */ 
/* 190 */     for (int k = 0; k < i; k++) {
/* 191 */       arrayOfJavaField[k] = new JavaField("unknown-field-" + k, "I");
/*     */     }
/* 193 */     for (k = 0; k < j; k++) {
/* 194 */       arrayOfJavaField[(k + i)] = new JavaField("unknown-field-" + k + i, "B");
/*     */     }
/*     */ 
/* 199 */     JavaClass localJavaClass = new JavaClass(str, 0L, 0L, 0L, 0L, arrayOfJavaField, EMPTY_STATIC_ARRAY, paramInt);
/*     */ 
/* 202 */     addFakeClass(makeId(paramLong), localJavaClass);
/* 203 */     return localJavaClass;
/*     */   }
/*     */ 
/*     */   public boolean getHasNewSet()
/*     */   {
/* 214 */     return this.hasNewSet;
/*     */   }
/*     */ 
/*     */   public void resolve(boolean paramBoolean)
/*     */   {
/* 234 */     System.out.println("Resolving " + this.heapObjects.size() + " objects...");
/*     */ 
/* 239 */     this.javaLangClass = findClass("java.lang.Class");
/* 240 */     if (this.javaLangClass == null) {
/* 241 */       System.out.println("WARNING:  hprof file does not include java.lang.Class!");
/* 242 */       this.javaLangClass = new JavaClass("java.lang.Class", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */ 
/* 244 */       addFakeClass(this.javaLangClass);
/*     */     }
/* 246 */     this.javaLangString = findClass("java.lang.String");
/* 247 */     if (this.javaLangString == null) {
/* 248 */       System.out.println("WARNING:  hprof file does not include java.lang.String!");
/* 249 */       this.javaLangString = new JavaClass("java.lang.String", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */ 
/* 251 */       addFakeClass(this.javaLangString);
/*     */     }
/* 253 */     this.javaLangClassLoader = findClass("java.lang.ClassLoader");
/* 254 */     if (this.javaLangClassLoader == null) {
/* 255 */       System.out.println("WARNING:  hprof file does not include java.lang.ClassLoader!");
/* 256 */       this.javaLangClassLoader = new JavaClass("java.lang.ClassLoader", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */ 
/* 258 */       addFakeClass(this.javaLangClassLoader);
/*     */     }
/*     */ 
/* 261 */     for (Object localObject = this.heapObjects.values().iterator(); ((Iterator)localObject).hasNext(); ) { localJavaHeapObject1 = (JavaHeapObject)((Iterator)localObject).next();
/* 262 */       if ((localJavaHeapObject1 instanceof JavaClass))
/* 263 */         localJavaHeapObject1.resolve(this);
/*     */     }
/* 268 */     JavaHeapObject localJavaHeapObject1;
/* 268 */     for (localObject = this.heapObjects.values().iterator(); ((Iterator)localObject).hasNext(); ) { localJavaHeapObject1 = (JavaHeapObject)((Iterator)localObject).next();
/* 269 */       if (!(localJavaHeapObject1 instanceof JavaClass)) {
/* 270 */         localJavaHeapObject1.resolve(this);
/*     */       }
/*     */     }
/*     */ 
/* 274 */     this.heapObjects.putAll(this.fakeClasses);
/* 275 */     this.fakeClasses.clear();
/*     */ 
/* 277 */     this.weakReferenceClass = findClass("java.lang.ref.Reference");
/* 278 */     if (this.weakReferenceClass == null) {
/* 279 */       this.weakReferenceClass = findClass("sun.misc.Ref");
/* 280 */       this.referentFieldIndex = 0;
/*     */     } else {
/* 282 */       localObject = this.weakReferenceClass.getFieldsForInstance();
/* 283 */       for (int j = 0; j < localObject.length; j++) {
/* 284 */         if ("referent".equals(localObject[j].getName())) {
/* 285 */           this.referentFieldIndex = j;
/* 286 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 291 */     if (paramBoolean) {
/* 292 */       calculateReferencesToObjects();
/* 293 */       System.out.print("Eliminating duplicate references");
/* 294 */       System.out.flush();
/*     */     }
/*     */ 
/* 297 */     int i = 0;
/* 298 */     for (JavaHeapObject localJavaHeapObject2 : this.heapObjects.values()) {
/* 299 */       localJavaHeapObject2.setupReferers();
/* 300 */       i++;
/* 301 */       if ((paramBoolean) && (i % 5000 == 0)) {
/* 302 */         System.out.print(".");
/* 303 */         System.out.flush();
/*     */       }
/*     */     }
/* 306 */     if (paramBoolean) {
/* 307 */       System.out.println("");
/*     */     }
/*     */ 
/* 312 */     this.classes = Collections.unmodifiableMap(this.classes);
/*     */   }
/*     */ 
/*     */   private void calculateReferencesToObjects() {
/* 316 */     System.out.print("Chasing references, expect " + this.heapObjects
/* 317 */       .size() / 5000 + " dots");
/* 318 */     System.out.flush();
/* 319 */     int i = 0;
/* 320 */     MyVisitor localMyVisitor = new MyVisitor(null);
/* 321 */     for (Iterator localIterator = this.heapObjects.values().iterator(); localIterator.hasNext(); ) { localObject = (JavaHeapObject)localIterator.next();
/* 322 */       localMyVisitor.t = ((JavaHeapObject)localObject);
/*     */ 
/* 324 */       ((JavaHeapObject)localObject).visitReferencedObjects(localMyVisitor);
/* 325 */       i++;
/* 326 */       if (i % 5000 == 0) {
/* 327 */         System.out.print(".");
/* 328 */         System.out.flush();
/*     */       }
/*     */     }
/* 332 */     Object localObject;
/* 331 */     System.out.println();
/* 332 */     for (localIterator = this.roots.iterator(); localIterator.hasNext(); ) { localObject = (Root)localIterator.next();
/* 333 */       ((Root)localObject).resolve(this);
/* 334 */       JavaHeapObject localJavaHeapObject = findThing(((Root)localObject).getId());
/* 335 */       if (localJavaHeapObject != null)
/* 336 */         localJavaHeapObject.addReferenceFromRoot((Root)localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void markNewRelativeTo(Snapshot paramSnapshot)
/*     */   {
/* 342 */     this.hasNewSet = true;
/* 343 */     for (JavaHeapObject localJavaHeapObject1 : this.heapObjects.values())
/*     */     {
/* 345 */       long l = localJavaHeapObject1.getId();
/*     */       boolean bool;
/* 346 */       if ((l == 0L) || (l == -1L)) {
/* 347 */         bool = false;
/*     */       } else {
/* 349 */         JavaHeapObject localJavaHeapObject2 = paramSnapshot.findThing(localJavaHeapObject1.getId());
/* 350 */         if (localJavaHeapObject2 == null)
/* 351 */           bool = true;
/*     */         else {
/* 353 */           bool = !localJavaHeapObject1.isSameTypeAs(localJavaHeapObject2);
/*     */         }
/*     */       }
/* 356 */       localJavaHeapObject1.setNew(bool);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<JavaHeapObject> getThings() {
/* 361 */     return this.heapObjects.elements();
/*     */   }
/*     */ 
/*     */   public JavaHeapObject findThing(long paramLong)
/*     */   {
/* 366 */     Number localNumber = makeId(paramLong);
/* 367 */     JavaHeapObject localJavaHeapObject = (JavaHeapObject)this.heapObjects.get(localNumber);
/* 368 */     return localJavaHeapObject != null ? localJavaHeapObject : (JavaHeapObject)this.fakeClasses.get(localNumber);
/*     */   }
/*     */ 
/*     */   public JavaHeapObject findThing(String paramString) {
/* 372 */     return findThing(Misc.parseHex(paramString));
/*     */   }
/*     */ 
/*     */   public JavaClass findClass(String paramString) {
/* 376 */     if (paramString.startsWith("0x")) {
/* 377 */       return (JavaClass)findThing(paramString);
/*     */     }
/* 379 */     return (JavaClass)this.classes.get(paramString);
/*     */   }
/*     */ 
/*     */   public Iterator getClasses()
/*     */   {
/* 389 */     return this.classes.values().iterator();
/*     */   }
/*     */ 
/*     */   public JavaClass[] getClassesArray() {
/* 393 */     JavaClass[] arrayOfJavaClass = new JavaClass[this.classes.size()];
/* 394 */     this.classes.values().toArray(arrayOfJavaClass);
/* 395 */     return arrayOfJavaClass;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration getFinalizerObjects()
/*     */   {
/*     */     Vector localVector1;
/* 400 */     if ((this.finalizablesCache != null) && 
/* 401 */       ((localVector1 = (Vector)this.finalizablesCache
/* 401 */       .get()) != null)) {
/* 402 */       return localVector1.elements();
/*     */     }
/*     */ 
/* 405 */     JavaClass localJavaClass = findClass("java.lang.ref.Finalizer");
/* 406 */     JavaObject localJavaObject1 = (JavaObject)localJavaClass.getStaticField("queue");
/* 407 */     JavaThing localJavaThing1 = localJavaObject1.getField("head");
/* 408 */     Vector localVector2 = new Vector();
/* 409 */     if (localJavaThing1 != getNullThing()) {
/* 410 */       JavaObject localJavaObject2 = (JavaObject)localJavaThing1;
/*     */       while (true) {
/* 412 */         JavaHeapObject localJavaHeapObject = (JavaHeapObject)localJavaObject2.getField("referent");
/* 413 */         JavaThing localJavaThing2 = localJavaObject2.getField("next");
/* 414 */         if ((localJavaThing2 == getNullThing()) || (localJavaThing2.equals(localJavaObject2))) {
/*     */           break;
/*     */         }
/* 417 */         localJavaObject2 = (JavaObject)localJavaThing2;
/* 418 */         localVector2.add(localJavaHeapObject);
/*     */       }
/*     */     }
/* 421 */     this.finalizablesCache = new SoftReference(localVector2);
/* 422 */     return localVector2.elements();
/*     */   }
/*     */ 
/*     */   public Enumeration<Root> getRoots() {
/* 426 */     return this.roots.elements();
/*     */   }
/*     */ 
/*     */   public Root[] getRootsArray() {
/* 430 */     Root[] arrayOfRoot = new Root[this.roots.size()];
/* 431 */     this.roots.toArray(arrayOfRoot);
/* 432 */     return arrayOfRoot;
/*     */   }
/*     */ 
/*     */   public Root getRootAt(int paramInt) {
/* 436 */     return (Root)this.roots.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public ReferenceChain[] rootsetReferencesTo(JavaHeapObject paramJavaHeapObject, boolean paramBoolean)
/*     */   {
/* 441 */     Vector localVector1 = new Vector();
/*     */ 
/* 443 */     Hashtable localHashtable = new Hashtable();
/*     */ 
/* 445 */     Vector localVector2 = new Vector();
/* 446 */     localHashtable.put(paramJavaHeapObject, paramJavaHeapObject);
/* 447 */     localVector1.addElement(new ReferenceChain(paramJavaHeapObject, null));
/*     */ 
/* 449 */     while (localVector1.size() > 0) {
/* 450 */       localObject = (ReferenceChain)localVector1.elementAt(0);
/* 451 */       localVector1.removeElementAt(0);
/* 452 */       JavaHeapObject localJavaHeapObject1 = ((ReferenceChain)localObject).getObj();
/* 453 */       if (localJavaHeapObject1.getRoot() != null) {
/* 454 */         localVector2.addElement(localObject);
/*     */       }
/*     */ 
/* 458 */       Enumeration localEnumeration = localJavaHeapObject1.getReferers();
/* 459 */       while (localEnumeration.hasMoreElements()) {
/* 460 */         JavaHeapObject localJavaHeapObject2 = (JavaHeapObject)localEnumeration.nextElement();
/* 461 */         if ((localJavaHeapObject2 != null) && (!localHashtable.containsKey(localJavaHeapObject2)) && (
/* 462 */           (paramBoolean) || (!localJavaHeapObject2.refersOnlyWeaklyTo(this, localJavaHeapObject1)))) {
/* 463 */           localHashtable.put(localJavaHeapObject2, localJavaHeapObject2);
/* 464 */           localVector1.addElement(new ReferenceChain(localJavaHeapObject2, (ReferenceChain)localObject));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 470 */     Object localObject = new ReferenceChain[localVector2.size()];
/* 471 */     for (int i = 0; i < localVector2.size(); i++) {
/* 472 */       localObject[i] = ((ReferenceChain)localVector2.elementAt(i));
/*     */     }
/* 474 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean getUnresolvedObjectsOK() {
/* 478 */     return this.unresolvedObjectsOK;
/*     */   }
/*     */ 
/*     */   public void setUnresolvedObjectsOK(boolean paramBoolean) {
/* 482 */     this.unresolvedObjectsOK = paramBoolean;
/*     */   }
/*     */ 
/*     */   public JavaClass getWeakReferenceClass() {
/* 486 */     return this.weakReferenceClass;
/*     */   }
/*     */ 
/*     */   public int getReferentFieldIndex() {
/* 490 */     return this.referentFieldIndex;
/*     */   }
/*     */ 
/*     */   public JavaThing getNullThing() {
/* 494 */     return this.nullThing;
/*     */   }
/*     */ 
/*     */   public void setReachableExcludes(ReachableExcludes paramReachableExcludes) {
/* 498 */     this.reachableExcludes = paramReachableExcludes;
/*     */   }
/*     */ 
/*     */   public ReachableExcludes getReachableExcludes() {
/* 502 */     return this.reachableExcludes;
/*     */   }
/*     */ 
/*     */   void addReferenceFromRoot(Root paramRoot, JavaHeapObject paramJavaHeapObject)
/*     */   {
/* 507 */     Root localRoot = (Root)this.rootsMap.get(paramJavaHeapObject);
/* 508 */     if (localRoot == null)
/* 509 */       this.rootsMap.put(paramJavaHeapObject, paramRoot);
/*     */     else
/* 511 */       this.rootsMap.put(paramJavaHeapObject, localRoot.mostInteresting(paramRoot));
/*     */   }
/*     */ 
/*     */   Root getRoot(JavaHeapObject paramJavaHeapObject)
/*     */   {
/* 516 */     return (Root)this.rootsMap.get(paramJavaHeapObject);
/*     */   }
/*     */ 
/*     */   JavaClass getJavaLangClass() {
/* 520 */     return this.javaLangClass;
/*     */   }
/*     */ 
/*     */   JavaClass getJavaLangString() {
/* 524 */     return this.javaLangString;
/*     */   }
/*     */ 
/*     */   JavaClass getJavaLangClassLoader() {
/* 528 */     return this.javaLangClassLoader;
/*     */   }
/*     */ 
/*     */   JavaClass getOtherArrayType() {
/* 532 */     if (this.otherArrayType == null) {
/* 533 */       synchronized (this) {
/* 534 */         if (this.otherArrayType == null) {
/* 535 */           addFakeClass(new JavaClass("[<other>", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0));
/*     */ 
/* 538 */           this.otherArrayType = findClass("[<other>");
/*     */         }
/*     */       }
/*     */     }
/* 542 */     return this.otherArrayType;
/*     */   }
/*     */ 
/*     */   JavaClass getArrayClass(String paramString)
/*     */   {
/*     */     JavaClass localJavaClass;
/* 547 */     synchronized (this.classes) {
/* 548 */       localJavaClass = findClass("[" + paramString);
/* 549 */       if (localJavaClass == null) {
/* 550 */         localJavaClass = new JavaClass("[" + paramString, 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */ 
/* 552 */         addFakeClass(localJavaClass);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 559 */     return localJavaClass;
/*     */   }
/*     */ 
/*     */   ReadBuffer getReadBuffer() {
/* 563 */     return this.readBuf;
/*     */   }
/*     */ 
/*     */   void setNew(JavaHeapObject paramJavaHeapObject, boolean paramBoolean) {
/* 567 */     initNewObjects();
/* 568 */     if (paramBoolean)
/* 569 */       this.newObjects.put(paramJavaHeapObject, Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   boolean isNew(JavaHeapObject paramJavaHeapObject)
/*     */   {
/* 574 */     if (this.newObjects != null) {
/* 575 */       return this.newObjects.get(paramJavaHeapObject) != null;
/*     */     }
/* 577 */     return false;
/*     */   }
/*     */ 
/*     */   private Number makeId(long paramLong)
/*     */   {
/* 583 */     if (this.identifierSize == 4) {
/* 584 */       return new Integer((int)paramLong);
/*     */     }
/* 586 */     return new Long(paramLong);
/*     */   }
/*     */ 
/*     */   private void putInClassesMap(JavaClass paramJavaClass)
/*     */   {
/* 591 */     String str = paramJavaClass.getName();
/* 592 */     if (this.classes.containsKey(str))
/*     */     {
/* 596 */       str = str + "-" + paramJavaClass.getIdString();
/*     */     }
/* 598 */     this.classes.put(paramJavaClass.getName(), paramJavaClass);
/*     */   }
/*     */ 
/*     */   private void addFakeClass(JavaClass paramJavaClass) {
/* 602 */     putInClassesMap(paramJavaClass);
/* 603 */     paramJavaClass.resolve(this);
/*     */   }
/*     */ 
/*     */   private void addFakeClass(Number paramNumber, JavaClass paramJavaClass) {
/* 607 */     this.fakeClasses.put(paramNumber, paramJavaClass);
/* 608 */     addFakeClass(paramJavaClass);
/*     */   }
/*     */ 
/*     */   private synchronized void initNewObjects() {
/* 612 */     if (this.newObjects == null)
/* 613 */       synchronized (this) {
/* 614 */         if (this.newObjects == null)
/* 615 */           this.newObjects = new HashMap();
/*     */       }
/*     */   }
/*     */ 
/*     */   private synchronized void initSiteTraces()
/*     */   {
/* 622 */     if (this.siteTraces == null)
/* 623 */       synchronized (this) {
/* 624 */         if (this.siteTraces == null)
/* 625 */           this.siteTraces = new HashMap();
/*     */       }
/*     */   }
/*     */ 
/*     */   private static class MyVisitor extends AbstractJavaHeapObjectVisitor
/*     */   {
/*     */     JavaHeapObject t;
/*     */ 
/*     */     public void visit(JavaHeapObject paramJavaHeapObject)
/*     */     {
/* 223 */       paramJavaHeapObject.addReferenceFrom(this.t);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.Snapshot
 * JD-Core Version:    0.6.2
 */