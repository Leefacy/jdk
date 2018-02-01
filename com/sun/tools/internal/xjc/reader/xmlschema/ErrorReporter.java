/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public final class ErrorReporter extends BindingComponent
/*    */ {
/* 60 */   private final ErrorReceiver errorReceiver = (ErrorReceiver)Ring.get(ErrorReceiver.class);
/*    */ 
/*    */   void error(Locator loc, String prop, Object[] args)
/*    */   {
/* 69 */     this.errorReceiver.error(loc, Messages.format(prop, args));
/*    */   }
/*    */ 
/*    */   void warning(Locator loc, String prop, Object[] args) {
/* 73 */     this.errorReceiver.warning(new SAXParseException(
/* 74 */       Messages.format(prop, args), 
/* 74 */       loc));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ErrorReporter
 * JD-Core Version:    0.6.2
 */