/*     */ package com.sun.tools.internal.jxc;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.gen.config.Classes;
/*     */ import com.sun.tools.internal.jxc.gen.config.Config;
/*     */ import com.sun.tools.internal.jxc.gen.config.Schema;
/*     */ import com.sun.tools.internal.xjc.SchemaCache;
/*     */ import com.sun.tools.internal.xjc.api.Reference;
/*     */ import com.sun.tools.internal.xjc.util.ForkContentHandler;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public final class ConfigReader
/*     */ {
/*  76 */   private final Set<Reference> classesToBeIncluded = new HashSet();
/*     */   private final SchemaOutputResolver schemaOutputResolver;
/*     */   private final ProcessingEnvironment env;
/* 184 */   private static SchemaCache configSchema = new SchemaCache(Config.class.getResource("config.xsd"));
/*     */ 
/*     */   public ConfigReader(ProcessingEnvironment env, Collection<? extends TypeElement> classes, File xmlFile, ErrorHandler errorHandler)
/*     */     throws SAXException, IOException
/*     */   {
/*  98 */     this.env = env;
/*  99 */     Config config = parseAndGetConfig(xmlFile, errorHandler, env.getOptions().containsKey("-disableXmlSecurity"));
/* 100 */     checkAllClasses(config, classes);
/* 101 */     String path = xmlFile.getAbsolutePath();
/* 102 */     String xmlPath = path.substring(0, path.lastIndexOf(File.separatorChar));
/* 103 */     this.schemaOutputResolver = createSchemaOutputResolver(config, xmlPath);
/*     */   }
/*     */ 
/*     */   public Collection<Reference> getClassesToBeIncluded()
/*     */   {
/* 117 */     return this.classesToBeIncluded;
/*     */   }
/*     */ 
/*     */   private void checkAllClasses(Config config, Collection<? extends TypeElement> rootClasses)
/*     */   {
/* 122 */     List includeRegexList = config.getClasses().getIncludes();
/* 123 */     List excludeRegexList = config.getClasses().getExcludes();
/*     */ 
/* 126 */     for (Iterator localIterator1 = rootClasses.iterator(); localIterator1.hasNext(); ) { typeDecl = (TypeElement)localIterator1.next();
/*     */ 
/* 128 */       qualifiedName = typeDecl.getQualifiedName().toString();
/*     */ 
/* 130 */       localIterator2 = excludeRegexList.iterator();
/*     */       while (true) { if (!localIterator2.hasNext()) break label111; Pattern pattern = (Pattern)localIterator2.next();
/* 131 */         boolean match = checkPatternMatch(qualifiedName, pattern);
/* 132 */         if (match) {
/*     */           break;
/*     */         }
/*     */       }
/* 136 */       for (Pattern pattern : includeRegexList) {
/* 137 */         boolean match = checkPatternMatch(qualifiedName, pattern);
/* 138 */         if (match) {
/* 139 */           this.classesToBeIncluded.add(new Reference(typeDecl, this.env));
/* 140 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     TypeElement typeDecl;
/*     */     String qualifiedName;
/*     */     Iterator localIterator2;
/* 144 */     label111:
/*     */   }
/*     */ 
/*     */   public SchemaOutputResolver getSchemaOutputResolver()
/*     */   {
/* 150 */     return this.schemaOutputResolver;
/*     */   }
/*     */ 
/*     */   private SchemaOutputResolver createSchemaOutputResolver(Config config, String xmlpath) {
/* 154 */     File baseDir = new File(xmlpath, config.getBaseDir().getPath());
/* 155 */     SchemaOutputResolverImpl outResolver = new SchemaOutputResolverImpl(baseDir);
/*     */ 
/* 157 */     for (Schema schema : config.getSchema()) {
/* 158 */       String namespace = schema.getNamespace();
/* 159 */       File location = schema.getLocation();
/* 160 */       outResolver.addSchemaInfo(namespace, location);
/*     */     }
/* 162 */     return outResolver;
/*     */   }
/*     */ 
/*     */   private boolean checkPatternMatch(String qualifiedName, Pattern pattern)
/*     */   {
/* 175 */     Matcher matcher = pattern.matcher(qualifiedName);
/* 176 */     return matcher.matches();
/*     */   }
/*     */ 
/*     */   private Config parseAndGetConfig(File xmlFile, ErrorHandler errorHandler, boolean disableSecureProcessing)
/*     */     throws SAXException, IOException
/*     */   {
/*     */     try
/*     */     {
/* 198 */       SAXParserFactory factory = XmlFactory.createParserFactory(disableSecureProcessing);
/* 199 */       reader = factory.newSAXParser().getXMLReader();
/*     */     }
/*     */     catch (ParserConfigurationException e)
/*     */     {
/*     */       XMLReader reader;
/* 202 */       throw new Error(e);
/*     */     }
/*     */     XMLReader reader;
/* 204 */     NGCCRuntimeEx runtime = new NGCCRuntimeEx(errorHandler);
/*     */ 
/* 207 */     ValidatorHandler validator = configSchema.newValidator();
/* 208 */     validator.setErrorHandler(errorHandler);
/*     */ 
/* 211 */     reader.setContentHandler(new ForkContentHandler(validator, runtime));
/*     */ 
/* 213 */     reader.setErrorHandler(errorHandler);
/* 214 */     Config config = new Config(runtime);
/* 215 */     runtime.setRootHandler(config);
/* 216 */     reader.parse(new InputSource(xmlFile.toURL().toExternalForm()));
/* 217 */     runtime.reset();
/*     */ 
/* 219 */     return config;
/*     */   }
/*     */ 
/*     */   private static final class SchemaOutputResolverImpl extends SchemaOutputResolver
/*     */   {
/*     */     private final File baseDir;
/* 239 */     private final Map<String, File> schemas = new HashMap();
/*     */ 
/*     */     public Result createOutput(String namespaceUri, String suggestedFileName)
/*     */     {
/* 250 */       if (this.schemas.containsKey(namespaceUri)) {
/* 251 */         File loc = (File)this.schemas.get(namespaceUri);
/* 252 */         if (loc == null) return null;
/*     */ 
/* 256 */         loc.getParentFile().mkdirs();
/*     */ 
/* 258 */         return new StreamResult(loc);
/*     */       }
/*     */ 
/* 264 */       File schemaFile = new File(this.baseDir, suggestedFileName);
/*     */ 
/* 266 */       return new StreamResult(schemaFile);
/*     */     }
/*     */ 
/*     */     public SchemaOutputResolverImpl(File baseDir)
/*     */     {
/* 271 */       assert (baseDir != null);
/* 272 */       this.baseDir = baseDir;
/*     */     }
/*     */ 
/*     */     public void addSchemaInfo(String namespaceUri, File location) {
/* 276 */       if (namespaceUri == null)
/*     */       {
/* 278 */         namespaceUri = "";
/* 279 */       }this.schemas.put(namespaceUri, location);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ConfigReader
 * JD-Core Version:    0.6.2
 */