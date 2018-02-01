/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Imports
/*     */   implements Constants
/*     */ {
/*  59 */   Identifier currentPackage = idNull;
/*     */ 
/*  65 */   long currentPackageWhere = 0L;
/*     */ 
/*  70 */   Hashtable classes = new Hashtable();
/*     */ 
/*  77 */   Vector packages = new Vector();
/*     */ 
/*  83 */   Vector singles = new Vector();
/*     */   protected int checked;
/*     */ 
/*     */   public Imports(Environment paramEnvironment)
/*     */   {
/*  94 */     addPackage(idJavaLang);
/*     */   }
/*     */ 
/*     */   public synchronized void resolve(Environment paramEnvironment)
/*     */   {
/* 101 */     if (this.checked != 0) {
/* 102 */       return;
/*     */     }
/* 104 */     this.checked = -1;
/*     */ 
/* 137 */     Vector localVector = new Vector();
/* 138 */     for (Enumeration localEnumeration = this.packages.elements(); localEnumeration.hasMoreElements(); ) {
/* 139 */       localIdentifierToken = (IdentifierToken)localEnumeration.nextElement();
/* 140 */       localObject1 = localIdentifierToken.getName();
/* 141 */       l = localIdentifierToken.getWhere();
/*     */ 
/* 147 */       if (paramEnvironment.isExemptPackage((Identifier)localObject1)) {
/* 148 */         localVector.addElement(localIdentifierToken);
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/* 154 */           Identifier localIdentifier1 = paramEnvironment.resolvePackageQualifiedName((Identifier)localObject1);
/* 155 */           if (importable(localIdentifier1, paramEnvironment))
/*     */           {
/* 157 */             if (paramEnvironment.getPackage(localIdentifier1.getTopName()).exists()) {
/* 158 */               paramEnvironment.error(l, "class.and.package", localIdentifier1
/* 159 */                 .getTopName());
/*     */             }
/*     */ 
/* 162 */             if (!localIdentifier1.isInner())
/* 163 */               localIdentifier1 = Identifier.lookupInner(localIdentifier1, idNull);
/* 164 */             localObject1 = localIdentifier1;
/* 165 */           } else if (!paramEnvironment.getPackage((Identifier)localObject1).exists()) {
/* 166 */             paramEnvironment.error(l, "package.not.found", localObject1, "import");
/* 167 */           } else if (localIdentifier1.isInner())
/*     */           {
/* 169 */             paramEnvironment.error(l, "class.and.package", localIdentifier1.getTopName());
/*     */           }
/* 171 */           localVector.addElement(new IdentifierToken(l, (Identifier)localObject1));
/*     */         } catch (IOException localIOException) {
/* 173 */           paramEnvironment.error(l, "io.exception", "import");
/*     */         }
/*     */       }
/*     */     }
/*     */     IdentifierToken localIdentifierToken;
/*     */     Object localObject1;
/*     */     long l;
/* 176 */     this.packages = localVector;
/*     */ 
/* 178 */     for (localEnumeration = this.singles.elements(); localEnumeration.hasMoreElements(); ) {
/* 179 */       localIdentifierToken = (IdentifierToken)localEnumeration.nextElement();
/* 180 */       localObject1 = localIdentifierToken.getName();
/* 181 */       l = localIdentifierToken.getWhere();
/* 182 */       Identifier localIdentifier2 = ((Identifier)localObject1).getQualifier();
/*     */ 
/* 185 */       localObject1 = paramEnvironment.resolvePackageQualifiedName((Identifier)localObject1);
/* 186 */       if (!paramEnvironment.classExists(((Identifier)localObject1).getTopName())) {
/* 187 */         paramEnvironment.error(l, "class.not.found", localObject1, "import");
/*     */       }
/*     */ 
/* 191 */       Identifier localIdentifier3 = ((Identifier)localObject1).getFlatName().getName();
/*     */ 
/* 194 */       Identifier localIdentifier4 = (Identifier)this.classes.get(localIdentifier3);
/*     */       Object localObject2;
/*     */       Object localObject3;
/* 195 */       if (localIdentifier4 != null) {
/* 196 */         localObject2 = Identifier.lookup(localIdentifier4.getQualifier(), localIdentifier4
/* 197 */           .getFlatName());
/* 198 */         localObject3 = Identifier.lookup(((Identifier)localObject1).getQualifier(), ((Identifier)localObject1)
/* 199 */           .getFlatName());
/* 200 */         if (!localObject2.equals(localObject3)) {
/* 201 */           paramEnvironment.error(l, "ambig.class", localObject1, localIdentifier4);
/*     */         }
/*     */       }
/* 204 */       this.classes.put(localIdentifier3, localObject1);
/*     */       try
/*     */       {
/* 224 */         localObject2 = paramEnvironment.getClassDeclaration((Identifier)localObject1);
/*     */ 
/* 227 */         localObject3 = ((ClassDeclaration)localObject2).getClassDefinitionNoCheck(paramEnvironment);
/*     */ 
/* 232 */         Identifier localIdentifier5 = ((ClassDefinition)localObject3).getName().getQualifier();
/*     */ 
/* 236 */         for (; localObject3 != null; localObject3 = ((ClassDefinition)localObject3).getOuterClass())
/* 237 */           if ((((ClassDefinition)localObject3).isPrivate()) || (
/* 238 */             (!((ClassDefinition)localObject3)
/* 238 */             .isPublic()) && 
/* 239 */             (!localIdentifier5
/* 239 */             .equals(this.currentPackage))))
/*     */           {
/* 240 */             paramEnvironment.error(l, "cant.access.class", localObject3);
/* 241 */             break;
/*     */           }
/*     */       }
/*     */       catch (AmbiguousClass localAmbiguousClass) {
/* 245 */         paramEnvironment.error(l, "ambig.class", localAmbiguousClass.name1, localAmbiguousClass.name2);
/*     */       } catch (ClassNotFound localClassNotFound) {
/* 247 */         paramEnvironment.error(l, "class.not.found", localClassNotFound.name, "import");
/*     */       }
/*     */     }
/* 250 */     this.checked = 1;
/*     */   }
/*     */ 
/*     */   public synchronized Identifier resolve(Environment paramEnvironment, Identifier paramIdentifier)
/*     */     throws ClassNotFound
/*     */   {
/* 261 */     paramEnvironment.dtEnter("Imports.resolve: " + paramIdentifier);
/*     */ 
/* 267 */     if (paramIdentifier.hasAmbigPrefix()) {
/* 268 */       paramIdentifier = paramIdentifier.removeAmbigPrefix();
/*     */     }
/*     */ 
/* 271 */     if (paramIdentifier.isQualified())
/*     */     {
/* 273 */       paramEnvironment.dtExit("Imports.resolve: QUALIFIED " + paramIdentifier);
/* 274 */       return paramIdentifier;
/*     */     }
/*     */ 
/* 277 */     if (this.checked <= 0) {
/* 278 */       this.checked = 0;
/* 279 */       resolve(paramEnvironment);
/*     */     }
/*     */ 
/* 283 */     Object localObject = (Identifier)this.classes.get(paramIdentifier);
/* 284 */     if (localObject != null) {
/* 285 */       paramEnvironment.dtExit("Imports.resolve: PREVIOUSLY IMPORTED " + paramIdentifier);
/* 286 */       return localObject;
/*     */     }
/*     */ 
/* 300 */     Identifier localIdentifier = Identifier.lookup(this.currentPackage, paramIdentifier);
/* 301 */     if (importable(localIdentifier, paramEnvironment)) {
/* 302 */       localObject = localIdentifier;
/*     */     }
/*     */     else
/*     */     {
/* 306 */       Enumeration localEnumeration = this.packages.elements();
/* 307 */       while (localEnumeration.hasMoreElements()) {
/* 308 */         IdentifierToken localIdentifierToken = (IdentifierToken)localEnumeration.nextElement();
/* 309 */         localIdentifier = Identifier.lookup(localIdentifierToken.getName(), paramIdentifier);
/*     */ 
/* 311 */         if (importable(localIdentifier, paramEnvironment)) {
/* 312 */           if (localObject == null)
/*     */           {
/* 316 */             localObject = localIdentifier;
/*     */           }
/*     */           else {
/* 319 */             paramEnvironment.dtExit("Imports.resolve: AMBIGUOUS " + paramIdentifier);
/*     */ 
/* 322 */             throw new AmbiguousClass((Identifier)localObject, localIdentifier);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 329 */     if (localObject == null) {
/* 330 */       paramEnvironment.dtExit("Imports.resolve: NOT FOUND " + paramIdentifier);
/* 331 */       throw new ClassNotFound(paramIdentifier);
/*     */     }
/*     */ 
/* 335 */     this.classes.put(paramIdentifier, localObject);
/* 336 */     paramEnvironment.dtExit("Imports.resolve: FIRST IMPORT " + paramIdentifier);
/* 337 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static boolean importable(Identifier paramIdentifier, Environment paramEnvironment)
/*     */   {
/* 345 */     if (!paramIdentifier.isInner())
/* 346 */       return paramEnvironment.classExists(paramIdentifier);
/* 347 */     if (!paramEnvironment.classExists(paramIdentifier.getTopName())) {
/* 348 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 368 */       ClassDeclaration localClassDeclaration = paramEnvironment
/* 368 */         .getClassDeclaration(paramIdentifier
/* 368 */         .getTopName());
/*     */ 
/* 370 */       ClassDefinition localClassDefinition = localClassDeclaration
/* 370 */         .getClassDefinitionNoCheck(paramEnvironment);
/*     */ 
/* 372 */       return localClassDefinition.innerClassExists(paramIdentifier.getFlatName().getTail()); } catch (ClassNotFound localClassNotFound) {
/*     */     }
/* 374 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized Identifier forceResolve(Environment paramEnvironment, Identifier paramIdentifier)
/*     */   {
/* 386 */     if (paramIdentifier.isQualified()) {
/* 387 */       return paramIdentifier;
/*     */     }
/* 389 */     Identifier localIdentifier = (Identifier)this.classes.get(paramIdentifier);
/* 390 */     if (localIdentifier != null) {
/* 391 */       return localIdentifier;
/*     */     }
/*     */ 
/* 394 */     localIdentifier = Identifier.lookup(this.currentPackage, paramIdentifier);
/*     */ 
/* 396 */     this.classes.put(paramIdentifier, localIdentifier);
/* 397 */     return localIdentifier;
/*     */   }
/*     */ 
/*     */   public synchronized void addClass(IdentifierToken paramIdentifierToken)
/*     */   {
/* 404 */     this.singles.addElement(paramIdentifierToken);
/*     */   }
/*     */ 
/*     */   public void addClass(Identifier paramIdentifier) throws AmbiguousClass {
/* 408 */     addClass(new IdentifierToken(paramIdentifier));
/*     */   }
/*     */ 
/*     */   public synchronized void addPackage(IdentifierToken paramIdentifierToken)
/*     */   {
/* 416 */     Identifier localIdentifier = paramIdentifierToken.getName();
/*     */ 
/* 420 */     if (localIdentifier == this.currentPackage) {
/* 421 */       return;
/*     */     }
/*     */ 
/* 426 */     int i = this.packages.size();
/* 427 */     for (int j = 0; j < i; j++) {
/* 428 */       if (localIdentifier == ((IdentifierToken)this.packages.elementAt(j)).getName()) {
/* 429 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 434 */     this.packages.addElement(paramIdentifierToken);
/*     */   }
/*     */ 
/*     */   public void addPackage(Identifier paramIdentifier) {
/* 438 */     addPackage(new IdentifierToken(paramIdentifier));
/*     */   }
/*     */ 
/*     */   public synchronized void setCurrentPackage(IdentifierToken paramIdentifierToken)
/*     */   {
/* 445 */     this.currentPackage = paramIdentifierToken.getName();
/* 446 */     this.currentPackageWhere = paramIdentifierToken.getWhere();
/*     */   }
/*     */ 
/*     */   public synchronized void setCurrentPackage(Identifier paramIdentifier)
/*     */   {
/* 453 */     this.currentPackage = paramIdentifier;
/*     */   }
/*     */ 
/*     */   public Identifier getCurrentPackage()
/*     */   {
/* 460 */     return this.currentPackage;
/*     */   }
/*     */ 
/*     */   public List getImportedPackages()
/*     */   {
/* 468 */     return Collections.unmodifiableList(this.packages);
/*     */   }
/*     */ 
/*     */   public List getImportedClasses()
/*     */   {
/* 476 */     return Collections.unmodifiableList(this.singles);
/*     */   }
/*     */ 
/*     */   public Environment newEnvironment(Environment paramEnvironment)
/*     */   {
/* 483 */     return new ImportEnvironment(paramEnvironment, this);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Imports
 * JD-Core Version:    0.6.2
 */