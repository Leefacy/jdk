/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.Dependency.Location;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class Archive
/*     */ {
/*     */   private final Path path;
/*     */   private final String filename;
/*     */   private final ClassFileReader reader;
/*  47 */   protected Map<Dependency.Location, Set<Dependency.Location>> deps = new ConcurrentHashMap();
/*     */ 
/*     */   public static Archive getInstance(Path paramPath)
/*     */     throws IOException
/*     */   {
/*  41 */     return new Archive(paramPath, ClassFileReader.newInstance(paramPath));
/*     */   }
/*     */ 
/*     */   protected Archive(String paramString)
/*     */   {
/*  50 */     this.path = null;
/*  51 */     this.filename = paramString;
/*  52 */     this.reader = null;
/*     */   }
/*     */ 
/*     */   protected Archive(Path paramPath, ClassFileReader paramClassFileReader) {
/*  56 */     this.path = paramPath;
/*  57 */     this.filename = this.path.getFileName().toString();
/*  58 */     this.reader = paramClassFileReader;
/*     */   }
/*     */ 
/*     */   public ClassFileReader reader() {
/*  62 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  66 */     return this.filename;
/*     */   }
/*     */ 
/*     */   public void addClass(Dependency.Location paramLocation) {
/*  70 */     Object localObject = (Set)this.deps.get(paramLocation);
/*  71 */     if (localObject == null) {
/*  72 */       localObject = new HashSet();
/*  73 */       this.deps.put(paramLocation, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addClass(Dependency.Location paramLocation1, Dependency.Location paramLocation2) {
/*  78 */     Object localObject = (Set)this.deps.get(paramLocation1);
/*  79 */     if (localObject == null) {
/*  80 */       localObject = new HashSet();
/*  81 */       this.deps.put(paramLocation1, localObject);
/*     */     }
/*  83 */     ((Set)localObject).add(paramLocation2);
/*     */   }
/*     */ 
/*     */   public Set<Dependency.Location> getClasses() {
/*  87 */     return this.deps.keySet();
/*     */   }
/*     */ 
/*     */   public void visitDependences(Visitor paramVisitor) {
/*  91 */     for (Iterator localIterator1 = this.deps.entrySet().iterator(); localIterator1.hasNext(); ) { localEntry = (Map.Entry)localIterator1.next();
/*  92 */       for (Dependency.Location localLocation : (Set)localEntry.getValue())
/*  93 */         paramVisitor.visit((Dependency.Location)localEntry.getKey(), localLocation); }
/*     */     Map.Entry localEntry;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  99 */     return getClasses().isEmpty();
/*     */   }
/*     */ 
/*     */   public String getPathName() {
/* 103 */     return this.path != null ? this.path.toString() : this.filename;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 107 */     return this.filename;
/*     */   }
/*     */ 
/*     */   static abstract interface Visitor
/*     */   {
/*     */     public abstract void visit(Dependency.Location paramLocation1, Dependency.Location paramLocation2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.Archive
 * JD-Core Version:    0.6.2
 */