/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Plugin
/*     */ {
/*     */   public abstract String getOptionName();
/*     */ 
/*     */   public abstract String getUsage();
/*     */ 
/*     */   public int parseArgument(Options opt, String[] args, int i)
/*     */     throws BadCommandLineException, IOException
/*     */   {
/* 115 */     return 0;
/*     */   }
/*     */ 
/*     */   public List<String> getCustomizationURIs()
/*     */   {
/* 138 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   public boolean isCustomizationTagName(String nsUri, String localName)
/*     */   {
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public void onActivated(Options opts)
/*     */     throws BadCommandLineException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void postProcessModel(Model model, ErrorHandler errorHandler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract boolean run(Outline paramOutline, Options paramOptions, ErrorHandler paramErrorHandler)
/*     */     throws SAXException;
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.Plugin
 * JD-Core Version:    0.6.2
 */