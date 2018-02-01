/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.Imports;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Parser;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.Node;
/*     */ 
/*     */ @Deprecated
/*     */ public class BatchParser extends Parser
/*     */ {
/*     */   protected Identifier pkg;
/*     */   protected Imports imports;
/*     */   protected Vector classes;
/*     */   protected SourceClass sourceClass;
/*     */   protected Environment toplevelEnv;
/*     */ 
/*     */   public BatchParser(Environment paramEnvironment, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  76 */     super(paramEnvironment, paramInputStream);
/*     */ 
/*  78 */     this.imports = new Imports(paramEnvironment);
/*  79 */     this.classes = new Vector();
/*  80 */     this.toplevelEnv = this.imports.newEnvironment(paramEnvironment);
/*     */   }
/*     */ 
/*     */   public void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken)
/*     */   {
/*  87 */     Identifier localIdentifier = paramIdentifierToken.getName();
/*     */ 
/*  89 */     if (this.pkg == null)
/*     */     {
/*  93 */       this.pkg = paramIdentifierToken.getName();
/*  94 */       this.imports.setCurrentPackage(paramIdentifierToken);
/*     */     } else {
/*  96 */       this.env.error(paramLong, "package.repeated");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void importClass(long paramLong, IdentifierToken paramIdentifierToken)
/*     */   {
/* 105 */     this.imports.addClass(paramIdentifierToken);
/*     */   }
/*     */ 
/*     */   public void importPackage(long paramLong, IdentifierToken paramIdentifierToken)
/*     */   {
/* 113 */     this.imports.addPackage(paramIdentifierToken);
/*     */   }
/*     */ 
/*     */   public ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken)
/*     */   {
/* 135 */     this.toplevelEnv.dtEnter("beginClass: " + this.sourceClass);
/*     */ 
/* 137 */     SourceClass localSourceClass = this.sourceClass;
/*     */ 
/* 139 */     if ((localSourceClass == null) && (this.pkg != null))
/*     */     {
/* 141 */       paramIdentifierToken1 = new IdentifierToken(paramIdentifierToken1.getWhere(), 
/* 141 */         Identifier.lookup(this.pkg, paramIdentifierToken1
/* 141 */         .getName()));
/*     */     }
/*     */ 
/* 146 */     if ((paramInt & 0x10000) != 0) {
/* 147 */       paramInt |= 18;
/*     */     }
/* 149 */     if ((paramInt & 0x20000) != 0) {
/* 150 */       paramInt |= 2;
/*     */     }
/*     */ 
/* 164 */     if ((paramInt & 0x200) != 0)
/*     */     {
/* 166 */       paramInt |= 1024;
/* 167 */       if (localSourceClass != null)
/*     */       {
/* 169 */         paramInt |= 8;
/*     */       }
/*     */     }
/*     */ 
/* 173 */     if ((localSourceClass != null) && (localSourceClass.isInterface()))
/*     */     {
/* 180 */       if ((paramInt & 0x6) == 0) {
/* 181 */         paramInt |= 1;
/*     */       }
/* 183 */       paramInt |= 8;
/*     */     }
/*     */ 
/* 196 */     this.sourceClass = 
/* 197 */       ((SourceClass)this.toplevelEnv
/* 197 */       .makeClassDefinition(this.toplevelEnv, paramLong, paramIdentifierToken1, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, localSourceClass));
/*     */ 
/* 201 */     this.sourceClass.getClassDeclaration().setDefinition(this.sourceClass, 4);
/* 202 */     this.env = new Environment(this.toplevelEnv, this.sourceClass);
/*     */ 
/* 204 */     this.toplevelEnv.dtEvent("beginClass: SETTING UP DEPENDENCIES");
/*     */ 
/* 210 */     this.toplevelEnv.dtEvent("beginClass: ADDING TO CLASS LIST");
/*     */ 
/* 212 */     this.classes.addElement(this.sourceClass);
/*     */ 
/* 214 */     this.toplevelEnv.dtExit("beginClass: " + this.sourceClass);
/*     */ 
/* 216 */     return this.sourceClass;
/*     */   }
/*     */ 
/*     */   public ClassDefinition getCurrentClass()
/*     */   {
/* 223 */     return this.sourceClass;
/*     */   }
/*     */ 
/*     */   public void endClass(long paramLong, ClassDefinition paramClassDefinition)
/*     */   {
/* 231 */     this.toplevelEnv.dtEnter("endClass: " + this.sourceClass);
/*     */ 
/* 234 */     this.sourceClass.setEndPosition(paramLong);
/* 235 */     SourceClass localSourceClass = (SourceClass)this.sourceClass.getOuterClass();
/* 236 */     this.sourceClass = localSourceClass;
/* 237 */     this.env = this.toplevelEnv;
/* 238 */     if (this.sourceClass != null) {
/* 239 */       this.env = new Environment(this.env, this.sourceClass);
/*     */     }
/* 241 */     this.toplevelEnv.dtExit("endClass: " + this.sourceClass);
/*     */   }
/*     */ 
/*     */   public void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode)
/*     */   {
/* 252 */     Identifier localIdentifier1 = paramIdentifierToken.getName();
/*     */ 
/* 255 */     if (this.sourceClass.isInterface())
/*     */     {
/* 257 */       if ((paramInt & 0x6) == 0)
/*     */       {
/* 262 */         paramInt |= 1;
/*     */       }
/*     */ 
/* 265 */       if (paramType.isType(12))
/* 266 */         paramInt |= 1024;
/*     */       else {
/* 268 */         paramInt |= 24;
/*     */       }
/*     */     }
/* 271 */     if (localIdentifier1.equals(idInit))
/*     */     {
/* 275 */       localObject = paramType.getReturnType();
/*     */ 
/* 277 */       Identifier localIdentifier2 = !((Type)localObject).isType(10) ? idStar : ((Type)localObject)
/* 277 */         .getClassName();
/* 278 */       Identifier localIdentifier3 = this.sourceClass.getLocalName();
/* 279 */       if (localIdentifier3.equals(localIdentifier2)) {
/* 280 */         paramType = Type.tMethod(Type.tVoid, paramType.getArgumentTypes());
/* 281 */       } else if (localIdentifier3.equals(localIdentifier2.getFlatName().getName()))
/*     */       {
/* 283 */         paramType = Type.tMethod(Type.tVoid, paramType.getArgumentTypes());
/* 284 */         this.env.error(paramLong, "invalid.method.decl.qual"); } else {
/* 285 */         if ((localIdentifier2.isQualified()) || (localIdentifier2.equals(idStar)))
/*     */         {
/* 287 */           this.env.error(paramLong, "invalid.method.decl.name");
/* 288 */           return;
/*     */         }
/*     */ 
/* 293 */         this.env.error(paramLong, "invalid.method.decl");
/* 294 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 298 */     if ((paramArrayOfIdentifierToken1 == null) && (paramType.isType(12))) {
/* 299 */       paramArrayOfIdentifierToken1 = new IdentifierToken[0];
/*     */     }
/*     */ 
/* 302 */     if ((paramArrayOfIdentifierToken2 == null) && (paramType.isType(12))) {
/* 303 */       paramArrayOfIdentifierToken2 = new IdentifierToken[0];
/*     */     }
/*     */ 
/* 306 */     Object localObject = this.env.makeMemberDefinition(this.env, paramLong, this.sourceClass, paramString, paramInt, paramType, localIdentifier1, paramArrayOfIdentifierToken1, paramArrayOfIdentifierToken2, paramNode);
/*     */ 
/* 309 */     if (this.env.dump())
/* 310 */       ((MemberDefinition)localObject).print(System.out);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.BatchParser
 * JD-Core Version:    0.6.2
 */