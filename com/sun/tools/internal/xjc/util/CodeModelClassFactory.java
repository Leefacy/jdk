/*     */ package com.sun.tools.internal.xjc.util;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public final class CodeModelClassFactory
/*     */ {
/*     */   private ErrorReceiver errorReceiver;
/*  56 */   private int ticketMaster = 0;
/*     */ 
/*     */   public CodeModelClassFactory(ErrorReceiver _errorReceiver)
/*     */   {
/*  60 */     this.errorReceiver = _errorReceiver;
/*     */   }
/*     */ 
/*     */   public JDefinedClass createClass(JClassContainer parent, String name, Locator source) {
/*  64 */     return createClass(parent, 1, name, source);
/*     */   }
/*     */   public JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source) {
/*  67 */     return createClass(parent, mod, name, source, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */   public JDefinedClass createInterface(JClassContainer parent, String name, Locator source) {
/*  71 */     return createInterface(parent, 1, name, source);
/*     */   }
/*     */   public JDefinedClass createInterface(JClassContainer parent, int mod, String name, Locator source) {
/*  74 */     return createClass(parent, mod, name, source, ClassType.INTERFACE);
/*     */   }
/*     */ 
/*     */   public JDefinedClass createClass(JClassContainer parent, String name, Locator source, ClassType kind) {
/*  78 */     return createClass(parent, 1, name, source, kind);
/*     */   }
/*     */ 
/*     */   public JDefinedClass createClass(JClassContainer parent, int mod, String name, Locator source, ClassType kind)
/*     */   {
/*  83 */     if (!JJavaName.isJavaIdentifier(name))
/*     */     {
/*  85 */       this.errorReceiver.error(new SAXParseException(
/*  86 */         Messages.format("ERR_INVALID_CLASSNAME", new Object[] { name }), 
/*  86 */         source));
/*  87 */       return createDummyClass(parent);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  92 */       if ((parent.isClass()) && (kind == ClassType.CLASS)) {
/*  93 */         mod |= 16;
/*     */       }
/*  95 */       JDefinedClass r = parent._class(mod, name, kind);
/*     */ 
/*  98 */       r.metadata = source;
/*     */ 
/* 100 */       return r;
/*     */     }
/*     */     catch (JClassAlreadyExistsException e) {
/* 103 */       JDefinedClass cls = e.getExistingClass();
/*     */ 
/* 106 */       this.errorReceiver.error(new SAXParseException(
/* 107 */         Messages.format("CodeModelClassFactory.ClassNameCollision", new Object[] { cls
/* 107 */         .fullName() }), (Locator)cls.metadata));
/*     */ 
/* 109 */       this.errorReceiver.error(new SAXParseException(
/* 110 */         Messages.format("CodeModelClassFactory.ClassNameCollision.Source", new Object[] { name }), 
/* 110 */         source));
/*     */ 
/* 113 */       if (!name.equals(cls.name()))
/*     */       {
/* 115 */         this.errorReceiver.error(new SAXParseException(
/* 116 */           Messages.format("CodeModelClassFactory.CaseSensitivityCollision", new Object[] { name, cls
/* 117 */           .name() }), null));
/*     */       }
/*     */ 
/* 120 */       if (Util.equals((Locator)cls.metadata, source)) {
/* 121 */         this.errorReceiver.error(new SAXParseException(
/* 122 */           Messages.format("ERR_CHAMELEON_SCHEMA_GONE_WILD", new Object[0]), 
/* 122 */           source));
/*     */       }
/*     */     }
/*     */ 
/* 126 */     return createDummyClass(parent);
/*     */   }
/*     */ 
/*     */   private JDefinedClass createDummyClass(JClassContainer parent)
/*     */   {
/*     */     try
/*     */     {
/* 138 */       return parent._class("$$$garbage$$$" + this.ticketMaster++);
/*     */     } catch (JClassAlreadyExistsException ee) {
/* 140 */       return ee.getExistingClass();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.util.CodeModelClassFactory
 * JD-Core Version:    0.6.2
 */