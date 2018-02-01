/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*    */ import com.sun.xml.internal.rngom.ast.util.CheckingSchemaBuilder;
/*    */ import com.sun.xml.internal.rngom.parse.Parseable;
/*    */ import com.sun.xml.internal.rngom.parse.compact.CompactParseable;
/*    */ import com.sun.xml.internal.rngom.parse.xml.SAXParseable;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 67 */     ErrorHandler eh = new DefaultHandler()
/*    */     {
/*    */       public void error(SAXParseException e) throws SAXException {
/* 70 */         throw e;
/*    */       }
/*    */     };
/*    */     Parseable p;
/*    */     Parseable p;
/* 75 */     if (args[0].endsWith(".rng"))
/* 76 */       p = new SAXParseable(new InputSource(args[0]), eh);
/*    */     else {
/* 78 */       p = new CompactParseable(new InputSource(args[0]), eh);
/*    */     }
/*    */ 
/* 84 */     SchemaBuilder sb = new CheckingSchemaBuilder(new DSchemaBuilderImpl(), eh);
/*    */     try
/*    */     {
/* 87 */       p.parse(sb);
/*    */     }
/*    */     catch (BuildException e)
/*    */     {
/* 92 */       if ((e.getCause() instanceof SAXException)) {
/* 93 */         SAXException se = (SAXException)e.getCause();
/* 94 */         if (se.getException() != null)
/* 95 */           se.getException().printStackTrace();
/*    */       }
/* 97 */       throw e;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.Main
 * JD-Core Version:    0.6.2
 */