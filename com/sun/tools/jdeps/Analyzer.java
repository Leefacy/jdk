/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.Dependency.Location;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class Analyzer
/*     */ {
/*     */   private final Type type;
/*     */   private final Filter filter;
/*  66 */   private final Map<Archive, ArchiveDeps> results = new HashMap();
/*  67 */   private final Map<Dependency.Location, Archive> map = new HashMap();
/*  68 */   private final Archive NOT_FOUND = new Archive(
/*  69 */     JdepsTask.getMessage("artifact.not.found", new Object[0]));
/*     */ 
/*     */   public Analyzer(Type paramType, Filter paramFilter)
/*     */   {
/*  78 */     this.type = paramType;
/*  79 */     this.filter = paramFilter;
/*     */   }
/*     */ 
/*     */   public void run(List<Archive> paramList)
/*     */   {
/*  87 */     buildLocationArchiveMap(paramList);
/*     */ 
/*  90 */     for (Archive localArchive : paramList) {
/*  91 */       ArchiveDeps localArchiveDeps = new ArchiveDeps(localArchive, this.type);
/*  92 */       localArchive.visitDependences(localArchiveDeps);
/*  93 */       this.results.put(localArchive, localArchiveDeps);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildLocationArchiveMap(List<Archive> paramList)
/*     */   {
/*  99 */     for (Iterator localIterator1 = paramList.iterator(); localIterator1.hasNext(); ) { localArchive = (Archive)localIterator1.next();
/* 100 */       for (Dependency.Location localLocation : localArchive.getClasses())
/* 101 */         if (!this.map.containsKey(localLocation))
/* 102 */           this.map.put(localLocation, localArchive);
/*     */     }
/*     */     Archive localArchive;
/*     */   }
/*     */ 
/*     */   public boolean hasDependences(Archive paramArchive)
/*     */   {
/* 111 */     if (this.results.containsKey(paramArchive)) {
/* 112 */       return ((ArchiveDeps)this.results.get(paramArchive)).dependencies().size() > 0;
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> dependences(Archive paramArchive) {
/* 118 */     ArchiveDeps localArchiveDeps = (ArchiveDeps)this.results.get(paramArchive);
/* 119 */     return localArchiveDeps.targetDependences();
/*     */   }
/*     */ 
/*     */   public void visitDependences(Archive paramArchive, Visitor paramVisitor, Type paramType)
/*     */   {
/*     */     ArchiveDeps localArchiveDeps;
/*     */     Object localObject1;
/*     */     Iterator localIterator;
/*     */     Object localObject2;
/* 137 */     if (paramType == Type.SUMMARY) {
/* 138 */       localArchiveDeps = (ArchiveDeps)this.results.get(paramArchive);
/* 139 */       localObject1 = new TreeMap();
/* 140 */       for (localIterator = localArchiveDeps.requires().iterator(); localIterator.hasNext(); ) { localObject2 = (Archive)localIterator.next();
/* 141 */         ((SortedMap)localObject1).put(((Archive)localObject2).getName(), localObject2);
/*     */       }
/* 143 */       for (localIterator = ((SortedMap)localObject1).values().iterator(); localIterator.hasNext(); ) { localObject2 = (Archive)localIterator.next();
/* 144 */         Profile localProfile = localArchiveDeps.getTargetProfile((Archive)localObject2);
/* 145 */         paramVisitor.visitDependence(paramArchive.getName(), paramArchive, localProfile != null ? localProfile
/* 146 */           .profileName() : ((Archive)localObject2).getName(), (Archive)localObject2); }
/*     */     }
/*     */     else {
/* 149 */       localArchiveDeps = (ArchiveDeps)this.results.get(paramArchive);
/* 150 */       if (paramType != this.type)
/*     */       {
/* 152 */         localArchiveDeps = new ArchiveDeps(paramArchive, paramType);
/* 153 */         paramArchive.visitDependences(localArchiveDeps);
/*     */       }
/* 155 */       localObject1 = new TreeSet(localArchiveDeps.dependencies());
/* 156 */       for (localIterator = ((SortedSet)localObject1).iterator(); localIterator.hasNext(); ) { localObject2 = (Dep)localIterator.next();
/* 157 */         paramVisitor.visitDependence(((Dep)localObject2).origin(), ((Dep)localObject2).originArchive(), ((Dep)localObject2).target(), ((Dep)localObject2).targetArchive()); }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitDependences(Archive paramArchive, Visitor paramVisitor)
/*     */   {
/* 163 */     visitDependences(paramArchive, paramVisitor, this.type);
/*     */   }
/*     */   class ArchiveDeps implements Archive.Visitor { protected final Archive archive;
/*     */     protected final Set<Archive> requires;
/*     */     protected final Set<Analyzer.Dep> deps;
/*     */     protected final Analyzer.Type level;
/*     */     private Profile profile;
/*     */     private Analyzer.Dep curDep;
/*     */ 
/* 177 */     ArchiveDeps(Archive paramType, Analyzer.Type arg3) { this.archive = paramType;
/* 178 */       this.deps = new HashSet();
/* 179 */       this.requires = new HashSet();
/*     */       Object localObject;
/* 180 */       this.level = localObject; }
/*     */ 
/*     */     Set<Analyzer.Dep> dependencies()
/*     */     {
/* 184 */       return this.deps;
/*     */     }
/*     */ 
/*     */     Set<String> targetDependences() {
/* 188 */       HashSet localHashSet = new HashSet();
/* 189 */       for (Analyzer.Dep localDep : this.deps) {
/* 190 */         localHashSet.add(localDep.target());
/*     */       }
/* 192 */       return localHashSet;
/*     */     }
/*     */ 
/*     */     Set<Archive> requires() {
/* 196 */       return this.requires;
/*     */     }
/*     */ 
/*     */     Profile getTargetProfile(Archive paramArchive) {
/* 200 */       return PlatformClassPath.JDKArchive.isProfileArchive(paramArchive) ? this.profile : null;
/*     */     }
/*     */ 
/*     */     Archive findArchive(Dependency.Location paramLocation) {
/* 204 */       Archive localArchive = this.archive.getClasses().contains(paramLocation) ? this.archive : (Archive)Analyzer.this.map.get(paramLocation);
/* 205 */       if (localArchive == null) {
/* 206 */         Analyzer.this.map.put(paramLocation, localArchive = Analyzer.this.NOT_FOUND);
/*     */       }
/* 208 */       return localArchive;
/*     */     }
/*     */ 
/*     */     private String getLocationName(Dependency.Location paramLocation)
/*     */     {
/* 213 */       if ((this.level == Analyzer.Type.CLASS) || (this.level == Analyzer.Type.VERBOSE)) {
/* 214 */         return paramLocation.getClassName();
/*     */       }
/* 216 */       String str = paramLocation.getPackageName();
/* 217 */       return str.isEmpty() ? "<unnamed>" : str;
/*     */     }
/*     */ 
/*     */     public void visit(Dependency.Location paramLocation1, Dependency.Location paramLocation2)
/*     */     {
/* 223 */       Archive localArchive = findArchive(paramLocation2);
/* 224 */       if (Analyzer.this.filter.accepts(paramLocation1, this.archive, paramLocation2, localArchive)) {
/* 225 */         addDep(paramLocation1, paramLocation2);
/* 226 */         if ((this.archive != localArchive) && (!this.requires.contains(localArchive))) {
/* 227 */           this.requires.add(localArchive);
/*     */         }
/*     */       }
/* 230 */       if ((localArchive instanceof PlatformClassPath.JDKArchive)) {
/* 231 */         Profile localProfile = Profile.getProfile(paramLocation2.getPackageName());
/* 232 */         if ((this.profile == null) || ((localProfile != null) && (localProfile.compareTo(this.profile) > 0)))
/* 233 */           this.profile = localProfile;
/*     */       }
/*     */     }
/*     */ 
/*     */     protected Analyzer.Dep addDep(Dependency.Location paramLocation1, Dependency.Location paramLocation2)
/*     */     {
/* 240 */       String str1 = getLocationName(paramLocation1);
/* 241 */       String str2 = getLocationName(paramLocation2);
/* 242 */       Archive localArchive = findArchive(paramLocation2);
/* 243 */       if ((this.curDep != null) && 
/* 244 */         (this.curDep
/* 244 */         .origin().equals(str1)) && 
/* 245 */         (this.curDep
/* 245 */         .originArchive() == this.archive) && 
/* 246 */         (this.curDep
/* 246 */         .target().equals(str2)) && 
/* 247 */         (this.curDep
/* 247 */         .targetArchive() == localArchive)) {
/* 248 */         return this.curDep;
/*     */       }
/*     */ 
/* 251 */       Analyzer.Dep localDep1 = new Analyzer.Dep(Analyzer.this, str1, this.archive, str2, localArchive);
/* 252 */       if (this.deps.contains(localDep1)) {
/* 253 */         for (Analyzer.Dep localDep2 : this.deps)
/* 254 */           if (localDep1.equals(localDep2))
/* 255 */             this.curDep = localDep2;
/*     */       }
/*     */       else
/*     */       {
/* 259 */         this.deps.add(localDep1);
/* 260 */         this.curDep = localDep1;
/*     */       }
/* 262 */       return this.curDep;
/*     */     } }
/*     */ 
/*     */   class Dep implements Comparable<Dep>
/*     */   {
/*     */     final String origin;
/*     */     final Archive originArchive;
/*     */     final String target;
/*     */     final Archive targetArchive;
/*     */ 
/*     */     Dep(String paramArchive1, Archive paramString1, String paramArchive2, Archive arg5) {
/* 276 */       this.origin = paramArchive1;
/* 277 */       this.originArchive = paramString1;
/* 278 */       this.target = paramArchive2;
/*     */       Object localObject;
/* 279 */       this.targetArchive = localObject;
/*     */     }
/*     */ 
/*     */     String origin() {
/* 283 */       return this.origin;
/*     */     }
/*     */ 
/*     */     Archive originArchive() {
/* 287 */       return this.originArchive;
/*     */     }
/*     */ 
/*     */     String target() {
/* 291 */       return this.target;
/*     */     }
/*     */ 
/*     */     Archive targetArchive() {
/* 295 */       return this.targetArchive;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 301 */       if ((paramObject instanceof Dep)) {
/* 302 */         Dep localDep = (Dep)paramObject;
/* 303 */         if ((this.origin.equals(localDep.origin)) && (this.originArchive == localDep.originArchive));
/* 305 */         return (this.target
/* 305 */           .equals(localDep.target)) && 
/* 305 */           (this.targetArchive == localDep.targetArchive);
/*     */       }
/*     */ 
/* 308 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 313 */       int i = 7;
/*     */ 
/* 317 */       i = 67 * i + Objects.hashCode(this.origin) + 
/* 315 */         Objects.hashCode(this.originArchive) + 
/* 316 */         Objects.hashCode(this.target) + 
/* 317 */         Objects.hashCode(this.targetArchive);
/*     */ 
/* 318 */       return i;
/*     */     }
/*     */ 
/*     */     public int compareTo(Dep paramDep)
/*     */     {
/* 323 */       if (this.origin.equals(paramDep.origin)) {
/* 324 */         if (this.target.equals(paramDep.target)) {
/* 325 */           if ((this.originArchive == paramDep.originArchive) && (this.targetArchive == paramDep.targetArchive))
/*     */           {
/* 327 */             return 0;
/* 328 */           }if (this.originArchive == paramDep.originArchive) {
/* 329 */             return this.targetArchive.getPathName().compareTo(paramDep.targetArchive.getPathName());
/*     */           }
/* 331 */           return this.originArchive.getPathName().compareTo(paramDep.originArchive.getPathName());
/*     */         }
/*     */ 
/* 334 */         return this.target.compareTo(paramDep.target);
/*     */       }
/*     */ 
/* 337 */       return this.origin.compareTo(paramDep.origin);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface Filter
/*     */   {
/*     */     public abstract boolean accepts(Dependency.Location paramLocation1, Archive paramArchive1, Dependency.Location paramLocation2, Archive paramArchive2);
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  50 */     SUMMARY, 
/*  51 */     PACKAGE, 
/*  52 */     CLASS, 
/*  53 */     VERBOSE;
/*     */   }
/*     */ 
/*     */   public static abstract interface Visitor
/*     */   {
/*     */     public abstract void visitDependence(String paramString1, Archive paramArchive1, String paramString2, Archive paramArchive2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdeps.Analyzer
 * JD-Core Version:    0.6.2
 */