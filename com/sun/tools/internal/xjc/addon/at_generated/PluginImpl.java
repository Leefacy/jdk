/*     */ package com.sun.tools.internal.xjc.addon.at_generated;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.tools.internal.xjc.Driver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.EnumOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ public class PluginImpl extends Plugin
/*     */ {
/*     */   private JClass annotation;
/*  98 */   private String date = null;
/*     */ 
/*     */   public String getOptionName()
/*     */   {
/*  52 */     return "mark-generated";
/*     */   }
/*     */ 
/*     */   public String getUsage() {
/*  56 */     return "  -mark-generated    :  mark the generated code as @javax.annotation.Generated";
/*     */   }
/*     */ 
/*     */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
/*     */   {
/*  63 */     this.annotation = model.getCodeModel().ref("javax.annotation.Generated");
/*     */ 
/*  65 */     for (ClassOutline co : model.getClasses())
/*  66 */       augument(co);
/*  67 */     for (EnumOutline eo : model.getEnums()) {
/*  68 */       augument(eo);
/*     */     }
/*     */ 
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   private void augument(EnumOutline eo) {
/*  76 */     annotate(eo.clazz);
/*     */   }
/*     */ 
/*     */   private void augument(ClassOutline co)
/*     */   {
/*  83 */     annotate(co.implClass);
/*  84 */     for (JMethod m : co.implClass.methods())
/*  85 */       annotate(m);
/*  86 */     for (JFieldVar f : co.implClass.fields().values())
/*  87 */       annotate(f);
/*     */   }
/*     */ 
/*     */   private void annotate(JAnnotatable m) {
/*  91 */     m.annotate(this.annotation)
/*  92 */       .param("value", Driver.class
/*  92 */       .getName())
/*  93 */       .param("date", 
/*  93 */       getISO8601Date())
/*  94 */       .param("comments", "JAXB RI v" + 
/*  94 */       Options.getBuildID());
/*     */   }
/*     */ 
/*     */   private String getISO8601Date()
/*     */   {
/* 105 */     if (this.date == null) {
/* 106 */       StringBuffer tstamp = new StringBuffer();
/* 107 */       tstamp.append(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ").format(new Date()));
/*     */ 
/* 110 */       tstamp.insert(tstamp.length() - 2, ':');
/* 111 */       this.date = tstamp.toString();
/*     */     }
/* 113 */     return this.date;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.addon.at_generated.PluginImpl
 * JD-Core Version:    0.6.2
 */