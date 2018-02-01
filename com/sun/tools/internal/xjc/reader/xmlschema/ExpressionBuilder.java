/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Choice;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Element;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.OneOrMore;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Sequence;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup.Compositor;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class ExpressionBuilder
/*     */   implements XSTermFunction<Expression>
/*     */ {
/*  63 */   private GWildcardElement wildcard = null;
/*     */ 
/*  65 */   private final Map<QName, GElementImpl> decls = new HashMap();
/*     */   private XSParticle current;
/*     */ 
/*     */   public static Expression createTree(XSParticle p)
/*     */   {
/*  54 */     return new ExpressionBuilder().particle(p);
/*     */   }
/*     */ 
/*     */   public Expression wildcard(XSWildcard wc)
/*     */   {
/*  74 */     if (this.wildcard == null)
/*  75 */       this.wildcard = new GWildcardElement();
/*  76 */     this.wildcard.merge(wc);
/*  77 */     this.wildcard.particles.add(this.current);
/*  78 */     return this.wildcard;
/*     */   }
/*     */ 
/*     */   public Expression modelGroupDecl(XSModelGroupDecl decl) {
/*  82 */     return modelGroup(decl.getModelGroup());
/*     */   }
/*     */ 
/*     */   public Expression modelGroup(XSModelGroup group) {
/*  86 */     XSModelGroup.Compositor comp = group.getCompositor();
/*  87 */     if (comp == XSModelGroup.CHOICE)
/*     */     {
/*  92 */       Expression e = Expression.EPSILON;
/*  93 */       for (XSParticle p : group.getChildren()) {
/*  94 */         if (e == null) e = particle(p); else
/*  95 */           e = new Choice(e, particle(p));
/*     */       }
/*  97 */       return e;
/*     */     }
/*  99 */     Expression e = Expression.EPSILON;
/* 100 */     for (XSParticle p : group.getChildren()) {
/* 101 */       if (e == null) e = particle(p); else
/* 102 */         e = new Sequence(e, particle(p));
/*     */     }
/* 104 */     return e;
/*     */   }
/*     */ 
/*     */   public Element elementDecl(XSElementDecl decl)
/*     */   {
/* 109 */     QName n = BGMBuilder.getName(decl);
/*     */ 
/* 111 */     GElementImpl e = (GElementImpl)this.decls.get(n);
/* 112 */     if (e == null) {
/* 113 */       this.decls.put(n, e = new GElementImpl(n, decl));
/*     */     }
/* 115 */     e.particles.add(this.current);
/* 116 */     assert (this.current.getTerm() == decl);
/*     */ 
/* 118 */     return e;
/*     */   }
/*     */ 
/*     */   public Expression particle(XSParticle p) {
/* 122 */     this.current = p;
/* 123 */     Expression e = (Expression)p.getTerm().apply(this);
/*     */ 
/* 125 */     if (p.isRepeated()) {
/* 126 */       e = new OneOrMore(e);
/*     */     }
/* 128 */     if (BigInteger.ZERO.equals(p.getMinOccurs())) {
/* 129 */       e = new Choice(e, Expression.EPSILON);
/*     */     }
/* 131 */     return e;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ExpressionBuilder
 * JD-Core Version:    0.6.2
 */