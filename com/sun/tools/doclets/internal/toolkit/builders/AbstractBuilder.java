/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractBuilder
/*     */ {
/*     */   protected final Configuration configuration;
/*     */   protected final Set<String> containingPackagesSeen;
/*     */   protected final LayoutParser layoutParser;
/*     */   protected static final boolean DEBUG = false;
/*     */ 
/*     */   public AbstractBuilder(Context paramContext)
/*     */   {
/* 107 */     this.configuration = paramContext.configuration;
/* 108 */     this.containingPackagesSeen = paramContext.containingPackagesSeen;
/* 109 */     this.layoutParser = paramContext.layoutParser;
/*     */   }
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract void build()
/*     */     throws IOException;
/*     */ 
/*     */   protected void build(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 133 */     String str = paramXMLNode.name;
/*     */     try {
/* 135 */       invokeMethod("build" + str, new Class[] { XMLNode.class, Content.class }, new Object[] { paramXMLNode, paramContent });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/* 139 */       localNoSuchMethodException.printStackTrace();
/* 140 */       this.configuration.root.printError("Unknown element: " + str);
/* 141 */       throw new DocletAbortException(localNoSuchMethodException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 143 */       throw new DocletAbortException(localInvocationTargetException.getCause());
/*     */     } catch (Exception localException) {
/* 145 */       localException.printStackTrace();
/* 146 */       this.configuration.root.printError("Exception " + localException
/* 147 */         .getClass().getName() + " thrown while processing element: " + str);
/*     */ 
/* 149 */       throw new DocletAbortException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void buildChildren(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 160 */     for (XMLNode localXMLNode : paramXMLNode.children)
/* 161 */       build(localXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   protected void invokeMethod(String paramString, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/* 179 */     Method localMethod = getClass().getMethod(paramString, paramArrayOfClass);
/* 180 */     localMethod.invoke(this, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static class Context
/*     */   {
/*     */     final Configuration configuration;
/*     */     final Set<String> containingPackagesSeen;
/*     */     final LayoutParser layoutParser;
/*     */ 
/*     */     Context(Configuration paramConfiguration, Set<String> paramSet, LayoutParser paramLayoutParser)
/*     */     {
/*  76 */       this.configuration = paramConfiguration;
/*  77 */       this.containingPackagesSeen = paramSet;
/*  78 */       this.layoutParser = paramLayoutParser;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder
 * JD-Core Version:    0.6.2
 */