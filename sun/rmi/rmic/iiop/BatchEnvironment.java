/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.ClassPath;
/*     */ 
/*     */ public class BatchEnvironment extends sun.rmi.rmic.BatchEnvironment
/*     */   implements Constants
/*     */ {
/*  58 */   private boolean parseNonConforming = false;
/*     */   private boolean standardPackage;
/*  68 */   HashSet alreadyChecked = new HashSet();
/*  69 */   Hashtable allTypes = new Hashtable(3001, 0.5F);
/*  70 */   Hashtable invalidTypes = new Hashtable(256, 0.5F);
/*  71 */   DirectoryLoader loader = null;
/*  72 */   ClassPathLoader classPathLoader = null;
/*  73 */   Hashtable nameContexts = null;
/*  74 */   Hashtable namesCache = new Hashtable();
/*  75 */   NameContext modulesContext = new NameContext(false);
/*     */ 
/*  77 */   ClassDefinition defRemote = null;
/*  78 */   ClassDefinition defError = null;
/*  79 */   ClassDefinition defException = null;
/*  80 */   ClassDefinition defRemoteException = null;
/*  81 */   ClassDefinition defCorbaObject = null;
/*  82 */   ClassDefinition defSerializable = null;
/*  83 */   ClassDefinition defExternalizable = null;
/*  84 */   ClassDefinition defThrowable = null;
/*  85 */   ClassDefinition defRuntimeException = null;
/*  86 */   ClassDefinition defIDLEntity = null;
/*  87 */   ClassDefinition defValueBase = null;
/*     */ 
/*  89 */   sun.tools.java.Type typeRemoteException = null;
/*  90 */   sun.tools.java.Type typeIOException = null;
/*  91 */   sun.tools.java.Type typeException = null;
/*  92 */   sun.tools.java.Type typeThrowable = null;
/*     */ 
/*  94 */   ContextStack contextStack = null;
/*     */ 
/*     */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, Main paramMain)
/*     */   {
/* 102 */     super(paramOutputStream, paramClassPath, paramMain);
/*     */     try
/*     */     {
/* 107 */       this.defRemote = 
/* 108 */         getClassDeclaration(idRemote)
/* 108 */         .getClassDefinition(this);
/* 109 */       this.defError = 
/* 110 */         getClassDeclaration(idJavaLangError)
/* 110 */         .getClassDefinition(this);
/* 111 */       this.defException = 
/* 112 */         getClassDeclaration(idJavaLangException)
/* 112 */         .getClassDefinition(this);
/* 113 */       this.defRemoteException = 
/* 114 */         getClassDeclaration(idRemoteException)
/* 114 */         .getClassDefinition(this);
/* 115 */       this.defCorbaObject = 
/* 116 */         getClassDeclaration(idCorbaObject)
/* 116 */         .getClassDefinition(this);
/* 117 */       this.defSerializable = 
/* 118 */         getClassDeclaration(idJavaIoSerializable)
/* 118 */         .getClassDefinition(this);
/* 119 */       this.defRuntimeException = 
/* 120 */         getClassDeclaration(idJavaLangRuntimeException)
/* 120 */         .getClassDefinition(this);
/* 121 */       this.defExternalizable = 
/* 122 */         getClassDeclaration(idJavaIoExternalizable)
/* 122 */         .getClassDefinition(this);
/* 123 */       this.defThrowable = 
/* 124 */         getClassDeclaration(idJavaLangThrowable)
/* 124 */         .getClassDefinition(this);
/* 125 */       this.defIDLEntity = 
/* 126 */         getClassDeclaration(idIDLEntity)
/* 126 */         .getClassDefinition(this);
/* 127 */       this.defValueBase = 
/* 128 */         getClassDeclaration(idValueBase)
/* 128 */         .getClassDefinition(this);
/* 129 */       this.typeRemoteException = this.defRemoteException.getClassDeclaration().getType();
/* 130 */       this.typeException = this.defException.getClassDeclaration().getType();
/* 131 */       this.typeIOException = getClassDeclaration(idJavaIoIOException).getType();
/* 132 */       this.typeThrowable = getClassDeclaration(idJavaLangThrowable).getType();
/*     */ 
/* 134 */       this.classPathLoader = new ClassPathLoader(paramClassPath);
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound) {
/* 137 */       error(0L, "rmic.class.not.found", localClassNotFound.name);
/* 138 */       throw new Error();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getParseNonConforming()
/*     */   {
/* 146 */     return this.parseNonConforming;
/*     */   }
/*     */ 
/*     */   public void setParseNonConforming(boolean paramBoolean)
/*     */   {
/* 158 */     if ((paramBoolean) && (!this.parseNonConforming)) {
/* 159 */       reset();
/*     */     }
/*     */ 
/* 162 */     this.parseNonConforming = paramBoolean;
/*     */   }
/*     */ 
/*     */   void setStandardPackage(boolean paramBoolean) {
/* 166 */     this.standardPackage = paramBoolean;
/*     */   }
/*     */ 
/*     */   boolean getStandardPackage() {
/* 170 */     return this.standardPackage;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 181 */     for (Object localObject1 = this.allTypes.elements(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 182 */       localObject2 = (Type)((Enumeration)localObject1).nextElement();
/* 183 */       ((Type)localObject2).destroy();
/*     */     }
/*     */     Object localObject2;
/* 186 */     for (localObject1 = this.invalidTypes.keys(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 187 */       localObject2 = (Type)((Enumeration)localObject1).nextElement();
/* 188 */       ((Type)localObject2).destroy();
/*     */     }
/*     */ 
/* 191 */     for (localObject1 = this.alreadyChecked.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 192 */       localObject2 = (Type)((Iterator)localObject1).next();
/* 193 */       ((Type)localObject2).destroy();
/*     */     }
/*     */ 
/* 196 */     if (this.contextStack != null) this.contextStack.clear();
/*     */ 
/* 201 */     if (this.nameContexts != null) {
/* 202 */       for (localObject1 = this.nameContexts.elements(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 203 */         localObject2 = (NameContext)((Enumeration)localObject1).nextElement();
/* 204 */         ((NameContext)localObject2).clear();
/*     */       }
/* 206 */       this.nameContexts.clear();
/*     */     }
/*     */ 
/* 211 */     this.allTypes.clear();
/* 212 */     this.invalidTypes.clear();
/* 213 */     this.alreadyChecked.clear();
/* 214 */     this.namesCache.clear();
/* 215 */     this.modulesContext.clear();
/*     */ 
/* 218 */     this.loader = null;
/* 219 */     this.parseNonConforming = false;
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 228 */     if (this.alreadyChecked != null)
/*     */     {
/* 232 */       reset();
/*     */ 
/* 235 */       this.alreadyChecked = null;
/* 236 */       this.allTypes = null;
/* 237 */       this.invalidTypes = null;
/* 238 */       this.nameContexts = null;
/* 239 */       this.namesCache = null;
/* 240 */       this.modulesContext = null;
/* 241 */       this.defRemote = null;
/* 242 */       this.defError = null;
/* 243 */       this.defException = null;
/* 244 */       this.defRemoteException = null;
/* 245 */       this.defCorbaObject = null;
/* 246 */       this.defSerializable = null;
/* 247 */       this.defExternalizable = null;
/* 248 */       this.defThrowable = null;
/* 249 */       this.defRuntimeException = null;
/* 250 */       this.defIDLEntity = null;
/* 251 */       this.defValueBase = null;
/* 252 */       this.typeRemoteException = null;
/* 253 */       this.typeIOException = null;
/* 254 */       this.typeException = null;
/* 255 */       this.typeThrowable = null;
/*     */ 
/* 257 */       super.shutdown();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.BatchEnvironment
 * JD-Core Version:    0.6.2
 */