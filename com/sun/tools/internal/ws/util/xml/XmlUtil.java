/*    */ package com.sun.tools.internal.ws.util.xml;
/*    */ 
/*    */ import com.sun.tools.internal.ws.util.WSDLParseException;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class XmlUtil extends com.sun.xml.internal.ws.util.xml.XmlUtil
/*    */ {
/*    */   public static boolean matchesTagNS(Element e, String tag, String nsURI)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokeinterface 58 1 0
/*    */     //   6: aload_1
/*    */     //   7: invokevirtual 55	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   10: ifeq +20 -> 30
/*    */     //   13: aload_0
/*    */     //   14: invokeinterface 59 1 0
/*    */     //   19: aload_2
/*    */     //   20: invokevirtual 55	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   23: ifeq +7 -> 30
/*    */     //   26: iconst_1
/*    */     //   27: goto +4 -> 31
/*    */     //   30: iconst_0
/*    */     //   31: ireturn
/*    */     //   32: astore_3
/*    */     //   33: new 29	com/sun/tools/internal/ws/util/WSDLParseException
/*    */     //   36: dup
/*    */     //   37: ldc 1
/*    */     //   39: iconst_1
/*    */     //   40: anewarray 33	java/lang/Object
/*    */     //   43: dup
/*    */     //   44: iconst_0
/*    */     //   45: aload_0
/*    */     //   46: invokeinterface 58 1 0
/*    */     //   51: aastore
/*    */     //   52: invokespecial 53	com/sun/tools/internal/ws/util/WSDLParseException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   55: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	31	32	java/lang/NullPointerException
/*    */   }
/*    */ 
/*    */   public static boolean matchesTagNS(Element e, QName name)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokeinterface 58 1 0
/*    */     //   6: aload_1
/*    */     //   7: invokevirtual 56	javax/xml/namespace/QName:getLocalPart	()Ljava/lang/String;
/*    */     //   10: invokevirtual 55	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   13: ifeq +23 -> 36
/*    */     //   16: aload_0
/*    */     //   17: invokeinterface 59 1 0
/*    */     //   22: aload_1
/*    */     //   23: invokevirtual 57	javax/xml/namespace/QName:getNamespaceURI	()Ljava/lang/String;
/*    */     //   26: invokevirtual 55	java/lang/String:equals	(Ljava/lang/Object;)Z
/*    */     //   29: ifeq +7 -> 36
/*    */     //   32: iconst_1
/*    */     //   33: goto +4 -> 37
/*    */     //   36: iconst_0
/*    */     //   37: ireturn
/*    */     //   38: astore_2
/*    */     //   39: new 29	com/sun/tools/internal/ws/util/WSDLParseException
/*    */     //   42: dup
/*    */     //   43: ldc 1
/*    */     //   45: iconst_1
/*    */     //   46: anewarray 33	java/lang/Object
/*    */     //   49: dup
/*    */     //   50: iconst_0
/*    */     //   51: aload_0
/*    */     //   52: invokeinterface 58 1 0
/*    */     //   57: aastore
/*    */     //   58: invokespecial 53	com/sun/tools/internal/ws/util/WSDLParseException:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
/*    */     //   61: athrow
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	37	38	java/lang/NullPointerException
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.util.xml.XmlUtil
 * JD-Core Version:    0.6.2
 */