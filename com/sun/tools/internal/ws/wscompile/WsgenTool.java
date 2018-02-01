/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */ 
/*     */ import com.oracle.webservices.internal.api.databinding.WSDLResolver;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import com.sun.tools.internal.ws.ToolVersion;
/*     */ import com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceAp;
/*     */ import com.sun.tools.internal.ws.processor.modeler.wsdl.ConsoleErrorReporter;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.xjc.util.NullStream;
/*     */ import com.sun.xml.internal.txw2.TXW;
/*     */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlAttribute;
/*     */ import com.sun.xml.internal.txw2.annotation.XmlElement;
/*     */ import com.sun.xml.internal.txw2.output.StreamSerializer;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
/*     */ import com.sun.xml.internal.ws.api.databinding.DatabindingFactory;
/*     */ import com.sun.xml.internal.ws.api.databinding.MappingInfo;
/*     */ import com.sun.xml.internal.ws.api.databinding.WSDLGenInfo;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.db.DatabindingImpl;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.ExternalMetadataReader;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.Version;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.tools.DiagnosticCollector;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaCompiler.CompilationTask;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.ToolProvider;
/*     */ import javax.xml.bind.annotation.XmlSeeAlso;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.Holder;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class WsgenTool
/*     */ {
/*     */   private final PrintStream out;
/*  87 */   private final WsgenOptions options = new WsgenOptions();
/*     */   private final Container container;
/*     */ 
/*     */   public WsgenTool(OutputStream out, Container container)
/*     */   {
/*  91 */     this.out = ((out instanceof PrintStream) ? (PrintStream)out : new PrintStream(out));
/*  92 */     this.container = container;
/*     */   }
/*     */ 
/*     */   public WsgenTool(OutputStream out)
/*     */   {
/*  97 */     this(out, null);
/*     */   }
/*     */ 
/*     */   public boolean run(String[] args) {
/* 101 */     Listener listener = new Listener();
/* 102 */     for (String arg : args) {
/* 103 */       if (arg.equals("-version")) {
/* 104 */         listener.message(
/* 105 */           WscompileMessages.WSGEN_VERSION(ToolVersion.VERSION.MAJOR_VERSION));
/*     */ 
/* 106 */         return true;
/*     */       }
/* 108 */       if (arg.equals("-fullversion")) {
/* 109 */         listener.message(
/* 110 */           WscompileMessages.WSGEN_FULLVERSION(ToolVersion.VERSION
/* 110 */           .toString()));
/* 111 */         return true;
/*     */       }
/*     */     }
/*     */     try {
/* 115 */       this.options.parseArguments(args);
/* 116 */       this.options.validate();
/* 117 */       if (!buildModel(this.options.endpoint.getName(), listener))
/* 118 */         return false;
/*     */     }
/*     */     catch (Options.WeAreDone done) {
/* 121 */       usage(done.getOptions());
/*     */     } catch (BadCommandLineException e) {
/* 123 */       if (e.getMessage() != null) {
/* 124 */         System.out.println(e.getMessage());
/* 125 */         System.out.println();
/*     */       }
/* 127 */       usage(e.getOptions());
/* 128 */       return 0;
/*     */     } catch (AbortException localAbortException1) {
/*     */     }
/*     */     finally {
/* 132 */       if (!this.options.keep) {
/* 133 */         this.options.removeGeneratedFiles();
/*     */       }
/*     */     }
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean useBootClasspath(Class clazz)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       ParallelWorldClassLoader.toJarUrl(clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class"));
/* 147 */       return true; } catch (Exception e) {
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean buildModel(String endpoint, Listener listener)
/*     */     throws BadCommandLineException
/*     */   {
/* 161 */     final ErrorReceiverFilter errReceiver = new ErrorReceiverFilter(listener);
/*     */ 
/* 163 */     boolean bootCP = (useBootClasspath(EndpointReference.class)) || (useBootClasspath(XmlSeeAlso.class));
/* 164 */     List args = new ArrayList(6 + (bootCP ? 1 : 0) + (this.options.nocompile ? 1 : 0) + (this.options.encoding != null ? 2 : 0));
/*     */ 
/* 166 */     args.add("-d");
/* 167 */     args.add(this.options.destDir.getAbsolutePath());
/* 168 */     args.add("-classpath");
/* 169 */     args.add(this.options.classpath);
/* 170 */     args.add("-s");
/* 171 */     args.add(this.options.sourceDir.getAbsolutePath());
/* 172 */     if (this.options.nocompile) {
/* 173 */       args.add("-proc:only");
/*     */     }
/* 175 */     if (this.options.encoding != null) {
/* 176 */       args.add("-encoding");
/* 177 */       args.add(this.options.encoding);
/*     */     }
/* 179 */     if (bootCP) {
/* 180 */       args.add("-Xbootclasspath/p:" + 
/* 182 */         JavaCompilerHelper.getJarFile(EndpointReference.class) + 
/* 182 */         File.pathSeparator + 
/* 184 */         JavaCompilerHelper.getJarFile(XmlSeeAlso.class));
/*     */     }
/*     */ 
/* 186 */     if (this.options.javacOptions != null) {
/* 187 */       args.addAll(this.options.getJavacOptions(args, listener));
/*     */     }
/*     */ 
/* 190 */     JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
/* 191 */     DiagnosticCollector diagnostics = new DiagnosticCollector();
/* 192 */     StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
/* 193 */     JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, args, 
/* 198 */       Collections.singleton(endpoint
/* 198 */       .replaceAll("\\$", ".")), 
/* 198 */       null);
/*     */ 
/* 200 */     task.setProcessors(Collections.singleton(new WebServiceAp(this.options, this.out)));
/* 201 */     boolean result = task.call().booleanValue();
/*     */ 
/* 203 */     if (!result) {
/* 204 */       this.out.println(WscompileMessages.WSCOMPILE_ERROR(WscompileMessages.WSCOMPILE_COMPILATION_FAILED()));
/* 205 */       return false;
/*     */     }
/* 207 */     if (this.options.genWsdl) {
/* 208 */       DatabindingConfig config = new DatabindingConfig();
/*     */ 
/* 210 */       List externalMetadataFileNames = this.options.externalMetadataFiles;
/* 211 */       boolean disableXmlSecurity = this.options.disableXmlSecurity;
/* 212 */       if ((externalMetadataFileNames != null) && (externalMetadataFileNames.size() > 0)) {
/* 213 */         config.setMetadataReader(new ExternalMetadataReader(getExternalFiles(externalMetadataFileNames), null, null, true, disableXmlSecurity));
/*     */       }
/*     */ 
/* 216 */       String tmpPath = this.options.destDir.getAbsolutePath() + File.pathSeparator + this.options.classpath;
/*     */ 
/* 218 */       ClassLoader classLoader = new URLClassLoader(Options.pathToURLs(tmpPath), 
/* 218 */         getClass().getClassLoader());
/*     */       try
/*     */       {
/* 221 */         endpointClass = classLoader.loadClass(endpoint);
/*     */       }
/*     */       catch (ClassNotFoundException e)
/*     */       {
/*     */         Class endpointClass;
/* 223 */         throw new BadCommandLineException(WscompileMessages.WSGEN_CLASS_NOT_FOUND(endpoint));
/*     */       }
/*     */       Class endpointClass;
/* 226 */       BindingID bindingID = this.options.getBindingID(this.options.protocol);
/* 227 */       if (!this.options.protocolSet) {
/* 228 */         bindingID = BindingID.parse(endpointClass);
/*     */       }
/* 230 */       WebServiceFeatureList wsfeatures = new WebServiceFeatureList(endpointClass);
/*     */ 
/* 233 */       if (this.options.portName != null) {
/* 234 */         config.getMappingInfo().setPortName(this.options.portName);
/*     */       }
/*     */ 
/* 237 */       DatabindingFactory fac = DatabindingFactory.newInstance();
/* 238 */       config.setEndpointClass(endpointClass);
/* 239 */       config.getMappingInfo().setServiceName(this.options.serviceName);
/* 240 */       config.setFeatures(wsfeatures.toArray());
/* 241 */       config.setClassLoader(classLoader);
/* 242 */       config.getMappingInfo().setBindingID(bindingID);
/* 243 */       DatabindingImpl rt = (DatabindingImpl)fac.createRuntime(config);
/*     */ 
/* 245 */       final File[] wsdlFileName = new File[1];
/* 246 */       final Map schemaFiles = new HashMap();
/*     */ 
/* 248 */       WSDLGenInfo wsdlGenInfo = new WSDLGenInfo();
/* 249 */       wsdlGenInfo.setSecureXmlProcessingDisabled(disableXmlSecurity);
/*     */ 
/* 251 */       wsdlGenInfo.setWsdlResolver(new WSDLResolver()
/*     */       {
/*     */         private File toFile(String suggestedFilename) {
/* 254 */           return new File(WsgenTool.this.options.nonclassDestDir, suggestedFilename);
/*     */         }
/*     */ 
/*     */         private Result toResult(File file)
/*     */         {
/*     */           try {
/* 260 */             Result result = new StreamResult(new FileOutputStream(file));
/* 261 */             result.setSystemId(file.getPath().replace('\\', '/'));
/*     */           } catch (FileNotFoundException e) {
/* 263 */             errReceiver.error(e);
/* 264 */             return null;
/*     */           }
/*     */           Result result;
/* 266 */           return result;
/*     */         }
/*     */ 
/*     */         public Result getWSDL(String suggestedFilename)
/*     */         {
/* 271 */           File f = toFile(suggestedFilename);
/* 272 */           wsdlFileName[0] = f;
/* 273 */           return toResult(f);
/*     */         }
/*     */ 
/*     */         public Result getSchemaOutput(String namespace, String suggestedFilename) {
/* 277 */           if (namespace == null)
/* 278 */             return null;
/* 279 */           File f = toFile(suggestedFilename);
/* 280 */           schemaFiles.put(namespace, f);
/* 281 */           return toResult(f);
/*     */         }
/*     */ 
/*     */         public Result getAbstractWSDL(Holder<String> filename)
/*     */         {
/* 286 */           return toResult(toFile((String)filename.value));
/*     */         }
/*     */ 
/*     */         public Result getSchemaOutput(String namespace, Holder<String> filename)
/*     */         {
/* 291 */           return getSchemaOutput(namespace, (String)filename.value);
/*     */         }
/*     */       });
/* 296 */       wsdlGenInfo.setContainer(this.container);
/* 297 */       wsdlGenInfo.setExtensions((WSDLGeneratorExtension[])ServiceFinder.find(WSDLGeneratorExtension.class).toArray());
/* 298 */       wsdlGenInfo.setInlineSchemas(this.options.inlineSchemas);
/* 299 */       rt.generateWSDL(wsdlGenInfo);
/*     */ 
/* 302 */       if (this.options.wsgenReport != null)
/* 303 */         generateWsgenReport(endpointClass, (AbstractSEIModelImpl)rt.getModel(), wsdlFileName[0], schemaFiles);
/*     */     }
/* 305 */     return true;
/*     */   }
/*     */ 
/*     */   private List<File> getExternalFiles(List<String> exts) {
/* 309 */     List files = new ArrayList();
/* 310 */     for (String ext : exts)
/*     */     {
/* 312 */       File file = new File(ext);
/* 313 */       if (!file.exists())
/*     */       {
/* 315 */         file = new File(this.options.sourceDir.getAbsolutePath() + File.separator + ext);
/*     */       }
/* 317 */       files.add(file);
/*     */     }
/* 319 */     return files;
/*     */   }
/*     */ 
/*     */   private void generateWsgenReport(Class<?> endpointClass, AbstractSEIModelImpl rtModel, File wsdlFile, Map<String, File> schemaFiles)
/*     */   {
/*     */     try
/*     */     {
/* 328 */       WsgenTool.ReportOutput.Report report = (WsgenTool.ReportOutput.Report)TXW.create(WsgenTool.ReportOutput.Report.class, new StreamSerializer(new BufferedOutputStream(new FileOutputStream(this.options.wsgenReport))));
/*     */ 
/* 331 */       report.wsdl(wsdlFile.getAbsolutePath());
/* 332 */       ReportOutput.writeQName(rtModel.getServiceQName(), report.service());
/* 333 */       ReportOutput.writeQName(rtModel.getPortName(), report.port());
/* 334 */       ReportOutput.writeQName(rtModel.getPortTypeName(), report.portType());
/*     */ 
/* 336 */       report.implClass(endpointClass.getName());
/*     */ 
/* 338 */       for (Map.Entry e : schemaFiles.entrySet()) {
/* 339 */         WsgenTool.ReportOutput.Schema s = report.schema();
/* 340 */         s.ns((String)e.getKey());
/* 341 */         s.location(((File)e.getValue()).getAbsolutePath());
/*     */       }
/*     */ 
/* 344 */       report.commit();
/*     */     }
/*     */     catch (IOException e) {
/* 347 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void usage(Options options)
/*     */   {
/* 404 */     if (options == null)
/* 405 */       options = this.options;
/* 406 */     if ((options instanceof WsgenOptions)) {
/* 407 */       System.out.println(WscompileMessages.WSGEN_HELP("WSGEN", ((WsgenOptions)options).protocols, ((WsgenOptions)options).nonstdProtocols
/* 409 */         .keySet()));
/* 410 */       System.out.println(WscompileMessages.WSGEN_USAGE_EXTENSIONS());
/* 411 */       System.out.println(WscompileMessages.WSGEN_USAGE_EXAMPLES());
/*     */     }
/*     */   }
/*     */ 
/*     */   class Listener extends WsimportListener {
/* 416 */     ConsoleErrorReporter cer = new ConsoleErrorReporter(WsgenTool.this.out == null ? new PrintStream(new NullStream()) : WsgenTool.this.out);
/*     */ 
/*     */     Listener() {
/*     */     }
/* 420 */     public void generatedFile(String fileName) { message(fileName); }
/*     */ 
/*     */ 
/*     */     public void message(String msg)
/*     */     {
/* 425 */       WsgenTool.this.out.println(msg);
/*     */     }
/*     */ 
/*     */     public void error(SAXParseException exception)
/*     */     {
/* 430 */       this.cer.error(exception);
/*     */     }
/*     */ 
/*     */     public void fatalError(SAXParseException exception)
/*     */     {
/* 435 */       this.cer.fatalError(exception);
/*     */     }
/*     */ 
/*     */     public void warning(SAXParseException exception)
/*     */     {
/* 440 */       this.cer.warning(exception);
/*     */     }
/*     */ 
/*     */     public void info(SAXParseException exception)
/*     */     {
/* 445 */       this.cer.info(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ReportOutput
/*     */   {
/*     */     private static void writeQName(QName n, QualifiedName w)
/*     */     {
/* 396 */       w.uri(n.getNamespaceURI());
/* 397 */       w.localName(n.getLocalPart());
/*     */     }
/*     */ 
/*     */     static abstract interface QualifiedName extends TypedXmlWriter
/*     */     {
/*     */       @XmlAttribute
/*     */       public abstract void uri(String paramString);
/*     */ 
/*     */       @XmlAttribute
/*     */       public abstract void localName(String paramString);
/*     */     }
/*     */ 
/*     */     @XmlElement("report")
/*     */     static abstract interface Report extends TypedXmlWriter
/*     */     {
/*     */       @XmlElement
/*     */       public abstract void wsdl(String paramString);
/*     */ 
/*     */       @XmlElement
/*     */       public abstract WsgenTool.ReportOutput.QualifiedName portType();
/*     */ 
/*     */       @XmlElement
/*     */       public abstract WsgenTool.ReportOutput.QualifiedName service();
/*     */ 
/*     */       @XmlElement
/*     */       public abstract WsgenTool.ReportOutput.QualifiedName port();
/*     */ 
/*     */       @XmlElement
/*     */       public abstract void implClass(String paramString);
/*     */ 
/*     */       @XmlElement
/*     */       public abstract WsgenTool.ReportOutput.Schema schema();
/*     */     }
/*     */ 
/*     */     static abstract interface Schema extends TypedXmlWriter
/*     */     {
/*     */       @XmlAttribute
/*     */       public abstract void ns(String paramString);
/*     */ 
/*     */       @XmlAttribute
/*     */       public abstract void location(String paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wscompile.WsgenTool
 * JD-Core Version:    0.6.2
 */