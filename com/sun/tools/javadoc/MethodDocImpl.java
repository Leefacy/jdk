/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class MethodDocImpl extends ExecutableMemberDocImpl
/*     */   implements MethodDoc
/*     */ {
/*     */   private String name;
/*     */   private String qualifiedName;
/*     */ 
/*     */   public MethodDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol)
/*     */   {
/*  57 */     super(paramDocEnv, paramMethodSymbol);
/*     */   }
/*     */ 
/*     */   public MethodDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath)
/*     */   {
/*  64 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*     */   }
/*     */ 
/*     */   public boolean isMethod()
/*     */   {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/*  82 */     return (this.sym.flags() & 0x0) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean isAbstract()
/*     */   {
/*  89 */     return (Modifier.isAbstract(getModifiers())) && (!isDefault());
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type returnType()
/*     */   {
/*  99 */     return TypeMaker.getType(this.env, this.sym.type.getReturnType(), false);
/*     */   }
/*     */ 
/*     */   public ClassDoc overriddenClass()
/*     */   {
/* 112 */     com.sun.javadoc.Type localType = overriddenType();
/* 113 */     return localType != null ? localType.asClassDoc() : null;
/*     */   }
/*     */ 
/*     */   public com.sun.javadoc.Type overriddenType()
/*     */   {
/* 122 */     if ((this.sym.flags() & 0x8) != 0L) {
/* 123 */       return null;
/*     */     }
/*     */ 
/* 126 */     Symbol.ClassSymbol localClassSymbol1 = (Symbol.ClassSymbol)this.sym.owner;
/* 127 */     for (com.sun.tools.javac.code.Type localType = this.env.types.supertype(localClassSymbol1.type); 
/* 128 */       localType.hasTag(TypeTag.CLASS); 
/* 129 */       localType = this.env.types.supertype(localType)) {
/* 130 */       Symbol.ClassSymbol localClassSymbol2 = (Symbol.ClassSymbol)localType.tsym;
/* 131 */       for (Scope.Entry localEntry = membersOf(localClassSymbol2).lookup(this.sym.name); localEntry.scope != null; localEntry = localEntry.next()) {
/* 132 */         if (this.sym.overrides(localEntry.sym, localClassSymbol1, this.env.types, true)) {
/* 133 */           return TypeMaker.getType(this.env, localType);
/*     */         }
/*     */       }
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public MethodDoc overriddenMethod()
/*     */   {
/* 152 */     if ((this.sym.flags() & 0x8) != 0L) {
/* 153 */       return null;
/*     */     }
/*     */ 
/* 158 */     Symbol.ClassSymbol localClassSymbol1 = (Symbol.ClassSymbol)this.sym.owner;
/* 159 */     for (com.sun.tools.javac.code.Type localType = this.env.types.supertype(localClassSymbol1.type); 
/* 160 */       localType.hasTag(TypeTag.CLASS); 
/* 161 */       localType = this.env.types.supertype(localType)) {
/* 162 */       Symbol.ClassSymbol localClassSymbol2 = (Symbol.ClassSymbol)localType.tsym;
/* 163 */       for (Scope.Entry localEntry = membersOf(localClassSymbol2).lookup(this.sym.name); localEntry.scope != null; localEntry = localEntry.next()) {
/* 164 */         if (this.sym.overrides(localEntry.sym, localClassSymbol1, this.env.types, true)) {
/* 165 */           return this.env.getMethodDoc((Symbol.MethodSymbol)localEntry.sym);
/*     */         }
/*     */       }
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   private Scope membersOf(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/*     */     try {
/* 175 */       return paramClassSymbol.members();
/*     */     }
/*     */     catch (Symbol.CompletionFailure localCompletionFailure)
/*     */     {
/*     */     }
/*     */ 
/* 181 */     return membersOf(paramClassSymbol);
/*     */   }
/*     */ 
/*     */   public boolean overrides(MethodDoc paramMethodDoc)
/*     */   {
/* 197 */     Symbol.MethodSymbol localMethodSymbol = ((MethodDocImpl)paramMethodDoc).sym;
/* 198 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.sym.owner;
/*     */ 
/* 200 */     if ((this.sym.name == localMethodSymbol.name) && (this.sym != localMethodSymbol))
/*     */     {
/* 207 */       if (!this.sym
/* 207 */         .isStatic())
/*     */       {
/* 211 */         if (this.env.types
/* 211 */           .asSuper(localClassSymbol.type, localMethodSymbol.owner) == null);
/*     */       }
/*     */     }
/*     */ 
/* 214 */     return this.sym
/* 214 */       .overrides(localMethodSymbol, localClassSymbol, this.env.types, false);
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 219 */     if (this.name == null) {
/* 220 */       this.name = this.sym.name.toString();
/*     */     }
/* 222 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String qualifiedName()
/*     */   {
/* 228 */     if (this.qualifiedName == null) {
/* 229 */       this.qualifiedName = (this.sym.enclClass().getQualifiedName() + "." + this.sym.name);
/*     */     }
/* 231 */     return this.qualifiedName;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 244 */     return this.sym.enclClass().getQualifiedName() + "." + 
/* 244 */       typeParametersString() + name() + signature();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.MethodDocImpl
 * JD-Core Version:    0.6.2
 */