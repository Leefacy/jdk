/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Type.MethodType;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.CollationKey;
/*     */ import java.text.Collator;
/*     */ 
/*     */ public abstract class ExecutableMemberDocImpl extends MemberDocImpl
/*     */   implements ExecutableMemberDoc
/*     */ {
/*     */   protected final Symbol.MethodSymbol sym;
/*     */ 
/*     */   public ExecutableMemberDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/*  63 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*  64 */     this.sym = paramMethodSymbol;
/*     */   }
/*     */ 
/*     */   public ExecutableMemberDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/*  71 */     this(paramDocEnv, paramMethodSymbol, null);
/*     */   }
/*     */ 
/*     */   protected long getFlags()
/*     */   {
/*  78 */     return this.sym.flags();
/*     */   }
/*     */ 
/*     */   protected Symbol.ClassSymbol getContainingClass()
/*     */   {
/*  85 */     return this.sym.enclClass();
/*     */   }
/*     */ 
/*     */   public boolean isNative()
/*     */   {
/*  92 */     return Modifier.isNative(getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean isSynchronized()
/*     */   {
/*  99 */     return Modifier.isSynchronized(getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean isVarArgs()
/*     */   {
/* 107 */     return ((this.sym.flags() & 0x0) != 0L) && (!this.env.legacyDoclet);
/*     */   }
/*     */ 
/*     */   public boolean isSynthetic()
/*     */   {
/* 115 */     return (this.sym.flags() & 0x1000) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean isIncluded() {
/* 119 */     return (containingClass().isIncluded()) && (this.env.shouldDocument(this.sym));
/*     */   }
/*     */ 
/*     */   public ThrowsTag[] throwsTags()
/*     */   {
/* 129 */     return comment().throwsTags();
/*     */   }
/*     */ 
/*     */   public ParamTag[] paramTags()
/*     */   {
/* 139 */     return comment().paramTags();
/*     */   }
/*     */ 
/*     */   public ParamTag[] typeParamTags()
/*     */   {
/* 148 */     return this.env.legacyDoclet ? new ParamTag[0] : 
/* 148 */       comment().typeParamTags();
/*     */   }
/*     */ 
/*     */   public ClassDoc[] thrownExceptions()
/*     */   {
/* 158 */     ListBuffer localListBuffer = new ListBuffer();
/* 159 */     for (com.sun.tools.javac.code.Type localType : this.sym.type.getThrownTypes()) {
/* 160 */       localType = this.env.types.erasure(localType);
/*     */ 
/* 163 */       ClassDocImpl localClassDocImpl = this.env.getClassDoc((Symbol.ClassSymbol)localType.tsym);
/* 164 */       if (localClassDocImpl != null) localListBuffer.append(localClassDocImpl);
/*     */     }
/* 166 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type[] thrownExceptionTypes()
/*     */   {
/* 175 */     return TypeMaker.getTypes(this.env, this.sym.type.getThrownTypes());
/*     */   }
/*     */ 
/*     */   public Parameter[] parameters()
/*     */   {
/* 188 */     List localList = this.sym.params();
/* 189 */     Parameter[] arrayOfParameter = new Parameter[localList.length()];
/*     */ 
/* 191 */     int i = 0;
/* 192 */     for (Symbol.VarSymbol localVarSymbol : localList) {
/* 193 */       arrayOfParameter[(i++)] = new ParameterImpl(this.env, localVarSymbol);
/*     */     }
/* 195 */     return arrayOfParameter;
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type receiverType()
/*     */   {
/* 205 */     com.sun.tools.javac.code.Type localType = this.sym.type.asMethodType().recvtype;
/* 206 */     return localType != null ? TypeMaker.getType(this.env, localType, false, true) : null;
/*     */   }
/*     */ 
/*     */   public TypeVariable[] typeParameters()
/*     */   {
/* 214 */     if (this.env.legacyDoclet) {
/* 215 */       return new TypeVariable[0];
/*     */     }
/* 217 */     TypeVariable[] arrayOfTypeVariable = new TypeVariable[this.sym.type.getTypeArguments().length()];
/* 218 */     TypeMaker.getTypes(this.env, this.sym.type.getTypeArguments(), arrayOfTypeVariable);
/* 219 */     return arrayOfTypeVariable;
/*     */   }
/*     */ 
/*     */   public String signature()
/*     */   {
/* 228 */     return makeSignature(true);
/*     */   }
/*     */ 
/*     */   public String flatSignature()
/*     */   {
/* 239 */     return makeSignature(false);
/*     */   }
/*     */ 
/*     */   private String makeSignature(boolean paramBoolean) {
/* 243 */     StringBuilder localStringBuilder = new StringBuilder();
/* 244 */     localStringBuilder.append("(");
/* 245 */     for (List localList = this.sym.type.getParameterTypes(); localList.nonEmpty(); ) {
/* 246 */       com.sun.tools.javac.code.Type localType = (com.sun.tools.javac.code.Type)localList.head;
/* 247 */       localStringBuilder.append(TypeMaker.getTypeString(this.env, localType, paramBoolean));
/* 248 */       localList = localList.tail;
/* 249 */       if (localList.nonEmpty()) {
/* 250 */         localStringBuilder.append(", ");
/*     */       }
/*     */     }
/* 253 */     if (isVarArgs()) {
/* 254 */       int i = localStringBuilder.length();
/* 255 */       localStringBuilder.replace(i - 2, i, "...");
/*     */     }
/* 257 */     localStringBuilder.append(")");
/* 258 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected String typeParametersString() {
/* 262 */     return TypeMaker.typeParametersString(this.env, this.sym, true);
/*     */   }
/*     */ 
/*     */   CollationKey generateKey()
/*     */   {
/* 270 */     String str = name() + flatSignature() + typeParametersString();
/*     */ 
/* 272 */     str = str.replace(',', ' ').replace('&', ' ');
/*     */ 
/* 274 */     return this.env.doclocale.collator.getCollationKey(str);
/*     */   }
/*     */ 
/*     */   public SourcePosition position()
/*     */   {
/* 283 */     if (this.sym.enclClass().sourcefile == null) return null;
/* 284 */     return SourcePositionImpl.make(this.sym.enclClass().sourcefile, this.tree == null ? 0 : this.tree.pos, this.lineMap);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ExecutableMemberDocImpl
 * JD-Core Version:    0.6.2
 */