/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.codemodel.internal.CodeWriter;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.writer.ZipCodeWriter;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.tools.DefaultAuthenticator;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Graph;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ExpressionBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.xjc.util.NullStream;
/*     */ import com.sun.tools.internal.xjc.util.Util;
/*     */ import com.sun.tools.internal.xjc.writer.SignatureWriter;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class Driver
/*     */ {
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  71 */       System.setProperty("java.net.useSystemProxies", "true");
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */     }
/*  76 */     if (Util.getSystemProperty(Driver.class, "noThreadSwap") != null) {
/*  77 */       _main(args);
/*     */     }
/*     */ 
/*  82 */     final Throwable[] ex = new Throwable[1];
/*     */ 
/*  84 */     Thread th = new Thread()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  88 */           Driver._main(this.val$args);
/*     */         } catch (Throwable e) {
/*  90 */           ex[0] = e;
/*     */         }
/*     */       }
/*     */     };
/*  94 */     th.start();
/*  95 */     th.join();
/*     */ 
/*  97 */     if (ex[0] != null)
/*     */     {
/*  99 */       if ((ex[0] instanceof Exception)) {
/* 100 */         throw ((Exception)ex[0]);
/*     */       }
/* 102 */       throw ((Error)ex[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void _main(String[] args) throws Exception {
/*     */     try {
/* 108 */       System.exit(run(args, System.out, System.out));
/*     */     }
/*     */     catch (BadCommandLineException e)
/*     */     {
/* 112 */       if (e.getMessage() != null) {
/* 113 */         System.out.println(e.getMessage());
/* 114 */         System.out.println();
/*     */       }
/*     */ 
/* 117 */       usage(e.getOptions(), false);
/* 118 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int run(String[] args, final PrintStream status, PrintStream out)
/*     */     throws Exception
/*     */   {
/* 185 */     return run(args, new XJCListener()
/*     */     {
/* 156 */       ConsoleErrorReporter cer = new ConsoleErrorReporter(this.val$out == null ? new PrintStream(new NullStream()) : this.val$out);
/*     */ 
/*     */       public void generatedFile(String fileName, int count, int total)
/*     */       {
/* 160 */         message(fileName);
/*     */       }
/*     */ 
/*     */       public void message(String msg) {
/* 164 */         if (status != null)
/* 165 */           status.println(msg);
/*     */       }
/*     */ 
/*     */       public void error(SAXParseException exception) {
/* 169 */         this.cer.error(exception);
/*     */       }
/*     */ 
/*     */       public void fatalError(SAXParseException exception) {
/* 173 */         this.cer.fatalError(exception);
/*     */       }
/*     */ 
/*     */       public void warning(SAXParseException exception) {
/* 177 */         this.cer.warning(exception);
/*     */       }
/*     */ 
/*     */       public void info(SAXParseException exception) {
/* 181 */         this.cer.info(exception);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static int run(String[] args, @NotNull final XJCListener listener)
/*     */     throws BadCommandLineException
/*     */   {
/* 211 */     for (String arg : args) {
/* 212 */       if (arg.equals("-version")) {
/* 213 */         listener.message(Messages.format("Driver.Version", new Object[0]));
/* 214 */         return -1;
/*     */       }
/* 216 */       if (arg.equals("-fullversion")) {
/* 217 */         listener.message(Messages.format("Driver.FullVersion", new Object[0]));
/* 218 */         return -1;
/*     */       }
/*     */     }
/*     */ 
/* 222 */     final OptionsEx opt = new OptionsEx();
/* 223 */     opt.setSchemaLanguage(Language.XMLSCHEMA);
/*     */     try {
/* 225 */       opt.parseArguments(args);
/*     */     } catch (WeAreDone e) {
/* 227 */       if (opt.proxyAuth != null) {
/* 228 */         DefaultAuthenticator.reset();
/*     */       }
/* 230 */       return -1;
/*     */     } catch (BadCommandLineException e) {
/* 232 */       if (opt.proxyAuth != null) {
/* 233 */         DefaultAuthenticator.reset();
/*     */       }
/* 235 */       e.initOptions(opt);
/* 236 */       throw e;
/*     */     }
/*     */ 
/* 241 */     if ((opt.defaultPackage != null) && (opt.defaultPackage.length() == 0)) {
/* 242 */       listener.message(Messages.format("Driver.WarningMessage", new Object[] { Messages.format("Driver.DefaultPackageWarning", new Object[0]) }));
/*     */     }
/*     */ 
/* 248 */     ClassLoader contextClassLoader = SecureLoader.getContextClassLoader();
/* 249 */     SecureLoader.setContextClassLoader(opt.getUserClassLoader(contextClassLoader));
/*     */     try
/*     */     {
/* 254 */       if (!opt.quiet) {
/* 255 */         listener.message(Messages.format("Driver.ParsingSchema", new Object[0]));
/*     */       }
/*     */ 
/* 258 */       final boolean[] hadWarning = new boolean[1];
/*     */ 
/* 260 */       receiver = new ErrorReceiverFilter(listener)
/*     */       {
/*     */         public void info(SAXParseException exception) {
/* 263 */           if (opt.verbose)
/* 264 */             super.info(exception);
/*     */         }
/*     */ 
/*     */         public void warning(SAXParseException exception) {
/* 268 */           hadWarning[0] = true;
/* 269 */           if (!opt.quiet)
/* 270 */             super.warning(exception);
/*     */         }
/*     */ 
/*     */         public void pollAbort() throws AbortException {
/* 274 */           if (listener.isCanceled())
/* 275 */             throw new AbortException();
/*     */         }
/*     */       };
/* 279 */       if (opt.mode == Mode.FOREST)
/*     */       {
/* 281 */         ModelLoader loader = new ModelLoader(opt, new JCodeModel(), receiver);
/*     */         try {
/* 283 */           DOMForest forest = loader.buildDOMForest(new XMLSchemaInternalizationLogic());
/* 284 */           forest.dump(System.out);
/* 285 */           return 0;
/*     */         } catch (SAXException localSAXException) {
/*     */         }
/*     */         catch (IOException e) {
/* 289 */           receiver.error(e);
/*     */         }
/*     */ 
/* 292 */         return -1;
/*     */       }
/*     */       Iterator it;
/*     */       XSComplexType ct;
/*     */       int m;
/* 295 */       if (opt.mode == Mode.GBIND) {
/*     */         try {
/* 297 */           XSSchemaSet xss = new ModelLoader(opt, new JCodeModel(), receiver).loadXMLSchema();
/* 298 */           it = xss.iterateComplexTypes();
/* 299 */           while (it.hasNext()) {
/* 300 */             ct = (XSComplexType)it.next();
/* 301 */             XSParticle p = ct.getContentType().asParticle();
/* 302 */             if (p != null)
/*     */             {
/* 304 */               Expression tree = ExpressionBuilder.createTree(p);
/* 305 */               System.out.println("Graph for " + ct.getName());
/* 306 */               System.out.println(tree.toString());
/* 307 */               Graph g = new Graph(tree);
/* 308 */               System.out.println(g.toString());
/* 309 */               System.out.println();
/*     */             }
/*     */           }
/* 311 */           return 0;
/*     */         }
/*     */         catch (SAXException localSAXException1)
/*     */         {
/* 315 */           return -1;
/*     */         }
/*     */       }
/* 318 */       Model model = ModelLoader.load(opt, new JCodeModel(), receiver);
/*     */ 
/* 320 */       if (model == null) {
/* 321 */         listener.message(Messages.format("Driver.ParseFailed", new Object[0]));
/* 322 */         return -1;
/*     */       }
/*     */ 
/* 325 */       if (!opt.quiet)
/* 326 */         listener.message(Messages.format("Driver.CompilingSchema", new Object[0]));
/*     */       Object os;
/*     */       int n;
/* 329 */       switch (3.$SwitchMap$com$sun$tools$internal$xjc$Driver$Mode[opt.mode.ordinal()]) {
/*     */       case 1:
/*     */         try {
/* 332 */           SignatureWriter.write(
/* 333 */             BeanGenerator.generate(model, receiver), 
/* 333 */             new OutputStreamWriter(System.out));
/*     */ 
/* 335 */           return 0;
/*     */         } catch (IOException e) {
/* 337 */           receiver.error(e);
/* 338 */           return -1;
/*     */         }
/*     */ 
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 346 */         receiver.debug("generating code");
/*     */ 
/* 348 */         Outline outline = model.generateCode(opt, receiver);
/* 349 */         if (outline == null) {
/* 350 */           listener.message(
/* 351 */             Messages.format("Driver.FailedToGenerateCode", new Object[0]));
/*     */ 
/* 352 */           return -1;
/*     */         }
/*     */ 
/* 355 */         listener.compiled(outline);
/*     */ 
/* 358 */         if (opt.mode == Mode.DRYRUN)
/*     */           break label908;
/*     */         try
/*     */         {
/*     */           CodeWriter cw;
/*     */           CodeWriter cw;
/* 364 */           if (opt.mode == Mode.ZIP)
/*     */           {
/*     */             Object os;
/* 366 */             if (opt.targetDir.getPath().equals("."))
/* 367 */               os = System.out;
/*     */             else {
/* 369 */               os = new FileOutputStream(opt.targetDir);
/*     */             }
/* 371 */             cw = opt.createCodeWriter(new ZipCodeWriter((OutputStream)os));
/*     */           } else {
/* 373 */             cw = opt.createCodeWriter();
/*     */           }
/* 375 */           if (!opt.quiet) {
/* 376 */             cw = new ProgressCodeWriter(cw, listener, model.codeModel.countArtifacts());
/*     */           }
/* 378 */           model.codeModel.build(cw);
/*     */         } catch (IOException e) {
/* 380 */           receiver.error(e);
/* 381 */           return -1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 387 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/* 390 */       label908: if (opt.debugMode) {
/*     */         try {
/* 392 */           new FileOutputStream(new File(opt.targetDir, hadWarning[0] != 0 ? "hadWarning" : "noWarning")).close();
/*     */         } catch (IOException e) {
/* 394 */           receiver.error(e);
/* 395 */           return -1;
/*     */         }
/*     */       }
/*     */ 
/* 399 */       return 0;
/*     */     }
/*     */     catch (StackOverflowError e)
/*     */     {
/*     */       ErrorReceiver receiver;
/* 401 */       if (opt.verbose)
/*     */       {
/* 404 */         throw e;
/*     */       }
/*     */ 
/* 408 */       listener.message(Messages.format("Driver.StackOverflow", new Object[0]));
/* 409 */       return -1;
/*     */     }
/*     */     finally {
/* 412 */       if (opt.proxyAuth != null)
/* 413 */         DefaultAuthenticator.reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getBuildID()
/*     */   {
/* 419 */     return Messages.format("Driver.BuildID", new Object[0]);
/*     */   }
/*     */ 
/*     */   public static void usage(@Nullable Options opts, boolean privateUsage)
/*     */   {
/* 514 */     System.out.println(Messages.format("Driver.Public.Usage", new Object[0]));
/* 515 */     if (privateUsage) {
/* 516 */       System.out.println(Messages.format("Driver.Private.Usage", new Object[0]));
/*     */     }
/*     */ 
/* 519 */     if ((opts != null) && (!opts.getAllPlugins().isEmpty())) {
/* 520 */       System.out.println(Messages.format("Driver.AddonUsage", new Object[0]));
/* 521 */       for (Plugin p : opts.getAllPlugins())
/* 522 */         System.out.println(p.getUsage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum Mode
/*     */   {
/* 428 */     CODE, 
/*     */ 
/* 431 */     SIGNATURE, 
/*     */ 
/* 434 */     FOREST, 
/*     */ 
/* 437 */     DRYRUN, 
/*     */ 
/* 440 */     ZIP, 
/*     */ 
/* 443 */     GBIND;
/*     */   }
/*     */ 
/*     */   static class OptionsEx extends Options
/*     */   {
/* 457 */     protected Driver.Mode mode = Driver.Mode.CODE;
/*     */ 
/* 460 */     public boolean noNS = false;
/*     */ 
/*     */     public int parseArgument(String[] args, int i)
/*     */       throws BadCommandLineException
/*     */     {
/* 465 */       if (args[i].equals("-noNS")) {
/* 466 */         this.noNS = true;
/* 467 */         return 1;
/*     */       }
/* 469 */       if (args[i].equals("-mode")) {
/* 470 */         i++;
/* 471 */         if (i == args.length)
/*     */         {
/* 473 */           throw new BadCommandLineException(
/* 473 */             Messages.format("Driver.MissingModeOperand", new Object[0]));
/*     */         }
/*     */ 
/* 475 */         String mstr = args[i].toLowerCase();
/*     */ 
/* 477 */         for (Driver.Mode m : Driver.Mode.values()) {
/* 478 */           if ((m.name().toLowerCase().startsWith(mstr)) && (mstr.length() > 2)) {
/* 479 */             this.mode = m;
/* 480 */             return 2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 485 */         throw new BadCommandLineException(
/* 485 */           Messages.format("Driver.UnrecognizedMode", new Object[] { args[i] }));
/*     */       }
/*     */ 
/* 487 */       if (args[i].equals("-help")) {
/* 488 */         Driver.usage(this, false);
/* 489 */         throw new Driver.WeAreDone(null);
/*     */       }
/* 491 */       if (args[i].equals("-private")) {
/* 492 */         Driver.usage(this, true);
/* 493 */         throw new Driver.WeAreDone(null);
/*     */       }
/*     */ 
/* 496 */       return super.parseArgument(args, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class WeAreDone extends BadCommandLineException
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.Driver
 * JD-Core Version:    0.6.2
 */