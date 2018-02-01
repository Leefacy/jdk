/*     */ package com.sun.xml.internal.xsom;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.internal.xsom.impl.scd.SCDImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.SCDParser;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Step;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Token;
/*     */ import com.sun.xml.internal.xsom.impl.scd.TokenMgrError;
/*     */ import com.sun.xml.internal.xsom.util.DeferedCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ 
/*     */ public abstract class SCD
/*     */ {
/*     */   public static SCD create(String path, NamespaceContext nsContext)
/*     */     throws java.text.ParseException
/*     */   {
/*     */     try
/*     */     {
/*  75 */       SCDParser p = new SCDParser(path, nsContext);
/*  76 */       List list = p.RelativeSchemaComponentPath();
/*  77 */       return new SCDImpl(path, (Step[])list.toArray(new Step[list.size()]));
/*     */     } catch (TokenMgrError e) {
/*  79 */       throw setCause(new java.text.ParseException(e.getMessage(), -1), e);
/*     */     } catch (com.sun.xml.internal.xsom.impl.scd.ParseException e) {
/*  81 */       throw setCause(new java.text.ParseException(e.getMessage(), e.currentToken.beginColumn), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static java.text.ParseException setCause(java.text.ParseException e, Throwable x) {
/*  86 */     e.initCause(x);
/*  87 */     return e;
/*     */   }
/*     */ 
/*     */   public final Collection<XSComponent> select(XSComponent contextNode)
/*     */   {
/*  98 */     return new DeferedCollection(select(Iterators.singleton(contextNode)));
/*     */   }
/*     */ 
/*     */   public final Collection<XSComponent> select(XSSchemaSet contextNode)
/*     */   {
/* 113 */     return select(contextNode.getSchemas());
/*     */   }
/*     */ 
/*     */   public final XSComponent selectSingle(XSComponent contextNode)
/*     */   {
/* 125 */     Iterator r = select(Iterators.singleton(contextNode));
/* 126 */     if (r.hasNext()) return (XSComponent)r.next();
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public final XSComponent selectSingle(XSSchemaSet contextNode)
/*     */   {
/* 139 */     Iterator r = select(contextNode.iterateSchema());
/* 140 */     if (r.hasNext()) return (XSComponent)r.next();
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract Iterator<XSComponent> select(Iterator<? extends XSComponent> paramIterator);
/*     */ 
/*     */   public final Collection<XSComponent> select(Collection<? extends XSComponent> contextNodes)
/*     */   {
/* 169 */     return new DeferedCollection(select(contextNodes.iterator()));
/*     */   }
/*     */ 
/*     */   public abstract String toString();
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.SCD
 * JD-Core Version:    0.6.2
 */