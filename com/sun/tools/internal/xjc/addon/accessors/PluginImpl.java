/*    */ package com.sun.tools.internal.xjc.addon.accessors;
/*    */ 
/*    */ import com.sun.codemodel.internal.JAnnotationUse;
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.Plugin;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ 
/*    */ public class PluginImpl extends Plugin
/*    */ {
/*    */   public String getOptionName()
/*    */   {
/* 53 */     return "Xpropertyaccessors";
/*    */   }
/*    */ 
/*    */   public String getUsage() {
/* 57 */     return "  -Xpropertyaccessors :  Use XmlAccessType PROPERTY instead of FIELD for generated classes";
/*    */   }
/*    */ 
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException
/*    */   {
/* 62 */     return 0;
/*    */   }
/*    */ 
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
/*    */   {
/* 67 */     for (ClassOutline co : model.getClasses()) {
/* 68 */       Iterator ann = co.ref.annotations().iterator();
/* 69 */       while (ann.hasNext()) {
/*    */         try {
/* 71 */           JAnnotationUse a = (JAnnotationUse)ann.next();
/* 72 */           Field clazzField = a.getClass().getDeclaredField("clazz");
/* 73 */           clazzField.setAccessible(true);
/* 74 */           JClass cl = (JClass)clazzField.get(a);
/* 75 */           if (cl.equals(model.getCodeModel()._ref(XmlAccessorType.class))) {
/* 76 */             a.param("value", XmlAccessType.PROPERTY);
/* 77 */             break;
/*    */           }
/*    */         } catch (IllegalArgumentException ex) {
/* 80 */           Logger.getLogger(PluginImpl.class.getName()).log(Level.SEVERE, null, ex);
/*    */         } catch (IllegalAccessException ex) {
/* 82 */           Logger.getLogger(PluginImpl.class.getName()).log(Level.SEVERE, null, ex);
/*    */         } catch (NoSuchFieldException ex) {
/* 84 */           Logger.getLogger(PluginImpl.class.getName()).log(Level.SEVERE, null, ex);
/*    */         } catch (SecurityException ex) {
/* 86 */           Logger.getLogger(PluginImpl.class.getName()).log(Level.SEVERE, null, ex);
/*    */         }
/*    */       }
/*    */     }
/* 90 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.addon.accessors.PluginImpl
 * JD-Core Version:    0.6.2
 */