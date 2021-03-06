/*     */ package com.sun.tools.internal.jxc.ap;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.ConfigReader;
/*     */ import com.sun.tools.internal.jxc.api.JXC;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.JavaCompiler;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedOptions;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ @SupportedAnnotationTypes({"javax.xml.bind.annotation.*"})
/*     */ @SupportedOptions({"jaxb.config"})
/*     */ public final class AnnotationParser extends AbstractProcessor
/*     */ {
/*     */   private ErrorReceiver errorListener;
/*     */ 
/*     */   public void init(ProcessingEnvironment processingEnv)
/*     */   {
/*  73 */     super.init(processingEnv);
/*  74 */     this.processingEnv = processingEnv;
/*  75 */     this.errorListener = new ErrorReceiverImpl(processingEnv
/*  76 */       .getMessager(), processingEnv
/*  77 */       .getOptions().containsKey(Const.DEBUG_OPTION.getValue()));
/*     */   }
/*     */ 
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
/*     */   {
/*  83 */     if (this.processingEnv.getOptions().containsKey(Const.CONFIG_FILE_OPTION.getValue())) {
/*  84 */       String value = (String)this.processingEnv.getOptions().get(Const.CONFIG_FILE_OPTION.getValue());
/*     */ 
/*  88 */       StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
/*  89 */       if (!st.hasMoreTokens()) {
/*  90 */         this.errorListener.error(null, Messages.OPERAND_MISSING.format(new Object[] { Const.CONFIG_FILE_OPTION.getValue() }));
/*  91 */         return true;
/*     */       }
/*     */ 
/*  94 */       while (st.hasMoreTokens()) {
/*  95 */         File configFile = new File(st.nextToken());
/*  96 */         if (!configFile.exists()) {
/*  97 */           this.errorListener.error(null, Messages.NON_EXISTENT_FILE.format(new Object[0]));
/*     */         }
/*     */         else
/*     */           try
/*     */           {
/* 102 */             Collection rootElements = new ArrayList();
/* 103 */             filterClass(rootElements, roundEnv.getRootElements());
/* 104 */             ConfigReader configReader = new ConfigReader(this.processingEnv, rootElements, configFile, this.errorListener);
/*     */ 
/* 111 */             Collection classesToBeIncluded = configReader.getClassesToBeIncluded();
/* 112 */             J2SJAXBModel model = JXC.createJavaCompiler().bind(classesToBeIncluded, 
/* 113 */               Collections.emptyMap(), null, this.processingEnv);
/*     */ 
/* 115 */             SchemaOutputResolver schemaOutputResolver = configReader.getSchemaOutputResolver();
/*     */ 
/* 117 */             model.generateSchema(schemaOutputResolver, this.errorListener);
/*     */           } catch (IOException e) {
/* 119 */             this.errorListener.error(e.getMessage(), e);
/*     */           }
/*     */           catch (SAXException localSAXException) {
/*     */           }
/*     */       }
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   private void filterClass(Collection<TypeElement> rootElements, Collection<? extends Element> elements) {
/* 129 */     for (Element element : elements)
/* 130 */       if ((element.getKind().equals(ElementKind.CLASS)) || (element.getKind().equals(ElementKind.INTERFACE)) || 
/* 131 */         (element
/* 131 */         .getKind().equals(ElementKind.ENUM))) {
/* 132 */         rootElements.add((TypeElement)element);
/* 133 */         filterClass(rootElements, ElementFilter.typesIn(element.getEnclosedElements()));
/*     */       }
/*     */   }
/*     */ 
/*     */   public SourceVersion getSupportedSourceVersion()
/*     */   {
/* 140 */     if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_6) > 0) {
/* 141 */       return SourceVersion.valueOf("RELEASE_7");
/*     */     }
/* 143 */     return SourceVersion.RELEASE_6;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.AnnotationParser
 * JD-Core Version:    0.6.2
 */