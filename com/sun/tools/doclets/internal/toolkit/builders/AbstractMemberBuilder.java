/*    */ package com.sun.tools.doclets.internal.toolkit.builders;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*    */ 
/*    */ public abstract class AbstractMemberBuilder extends AbstractBuilder
/*    */ {
/*    */   public AbstractMemberBuilder(AbstractBuilder.Context paramContext)
/*    */   {
/* 54 */     super(paramContext);
/*    */   }
/*    */ 
/*    */   public void build()
/*    */     throws DocletAbortException
/*    */   {
/* 65 */     throw new DocletAbortException("not supported");
/*    */   }
/*    */ 
/*    */   public void build(XMLNode paramXMLNode, Content paramContent)
/*    */   {
/* 77 */     if (hasMembersToDocument())
/* 78 */       super.build(paramXMLNode, paramContent);
/*    */   }
/*    */ 
/*    */   public abstract boolean hasMembersToDocument();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.AbstractMemberBuilder
 * JD-Core Version:    0.6.2
 */