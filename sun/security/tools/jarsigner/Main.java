/*      */ package sun.security.tools.jarsigner;
/*      */ 
/*      */ import com.sun.jarsigner.ContentSigner;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.security.CodeSigner;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.Key;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.Provider;
/*      */ import java.security.Security;
/*      */ import java.security.Timestamp;
/*      */ import java.security.UnrecoverableKeyException;
/*      */ import java.security.cert.CertPath;
/*      */ import java.security.cert.CertPathValidator;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateExpiredException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.CertificateNotYetValidException;
/*      */ import java.security.cert.CertificateParsingException;
/*      */ import java.security.cert.PKIXParameters;
/*      */ import java.security.cert.TrustAnchor;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.Collator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Base64;
/*      */ import java.util.Base64.Encoder;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import sun.security.tools.KeyStoreUtil;
/*      */ import sun.security.tools.PathList;
/*      */ import sun.security.util.BitArray;
/*      */ import sun.security.util.DerInputStream;
/*      */ import sun.security.util.DerValue;
/*      */ import sun.security.util.ManifestDigester;
/*      */ import sun.security.util.Password;
/*      */ import sun.security.util.SignatureFileVerifier;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ import sun.security.x509.NetscapeCertTypeExtension;
/*      */ 
/*      */ public class Main
/*      */ {
/*   84 */   private static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.tools.jarsigner.Resources")
/*   84 */     ;
/*   85 */   private static final Collator collator = Collator.getInstance();
/*      */   private static final String META_INF = "META-INF/";
/*   93 */   private static final Class<?>[] PARAM_STRING = { String.class };
/*      */   private static final String NONE = "NONE";
/*      */   private static final String P11KEYSTORE = "PKCS11";
/*      */   private static final long SIX_MONTHS = 15552000000L;
/*      */   static final String VERSION = "1.0";
/*      */   static final int IN_KEYSTORE = 1;
/*      */   static final int IN_SCOPE = 2;
/*      */   static final int NOT_ALIAS = 4;
/*      */   static final int SIGNED_BY_ALIAS = 8;
/*      */   X509Certificate[] certChain;
/*      */   PrivateKey privateKey;
/*      */   KeyStore store;
/*      */   String keystore;
/*  121 */   boolean nullStream = false;
/*  122 */   boolean token = false;
/*      */   String jarfile;
/*      */   String alias;
/*  125 */   List<String> ckaliases = new ArrayList();
/*      */   char[] storepass;
/*      */   boolean protectedPath;
/*      */   String storetype;
/*      */   String providerName;
/*  130 */   Vector<String> providers = null;
/*      */ 
/*  132 */   HashMap<String, String> providerArgs = new HashMap();
/*      */   char[] keypass;
/*      */   String sigfile;
/*      */   String sigalg;
/*  136 */   String digestalg = "SHA-256";
/*      */   String signedjar;
/*      */   String tsaUrl;
/*      */   String tsaAlias;
/*      */   String altCertChain;
/*      */   String tSAPolicyID;
/*  142 */   boolean verify = false;
/*  143 */   String verbose = null;
/*  144 */   boolean showcerts = false;
/*  145 */   boolean debug = false;
/*  146 */   boolean signManifest = true;
/*  147 */   boolean externalSF = true;
/*  148 */   boolean strict = false;
/*      */ 
/*  151 */   private ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
/*  152 */   private byte[] buffer = new byte[8192];
/*  153 */   private ContentSigner signingMechanism = null;
/*  154 */   private String altSignerClass = null;
/*  155 */   private String altSignerClasspath = null;
/*  156 */   private ZipFile zipFile = null;
/*      */ 
/*  159 */   private boolean hasExpiringCert = false;
/*  160 */   private boolean noTimestamp = false;
/*  161 */   private Date expireDate = new Date(0L);
/*      */ 
/*  164 */   private boolean hasExpiredCert = false;
/*  165 */   private boolean notYetValidCert = false;
/*  166 */   private boolean chainNotValidated = false;
/*  167 */   private boolean notSignedByAlias = false;
/*  168 */   private boolean aliasNotInStore = false;
/*  169 */   private boolean hasUnsignedEntry = false;
/*  170 */   private boolean badKeyUsage = false;
/*  171 */   private boolean badExtendedKeyUsage = false;
/*  172 */   private boolean badNetscapeCertType = false;
/*      */   CertificateFactory certificateFactory;
/*      */   CertPathValidator validator;
/*      */   PKIXParameters pkixParameters;
/*  857 */   private static MessageFormat validityTimeForm = null;
/*  858 */   private static MessageFormat notYetTimeForm = null;
/*  859 */   private static MessageFormat expiredTimeForm = null;
/*  860 */   private static MessageFormat expiringTimeForm = null;
/*      */ 
/*  971 */   private static MessageFormat signTimeForm = null;
/*      */ 
/*  985 */   private Map<CodeSigner, Integer> cacheForInKS = new IdentityHashMap();
/*      */ 
/* 1028 */   Hashtable<Certificate, String> storeHash = new Hashtable();
/*      */ 
/* 1525 */   Map<CodeSigner, String> cacheForSignerInfo = new IdentityHashMap();
/*      */ 
/*      */   public static void main(String[] paramArrayOfString)
/*      */     throws Exception
/*      */   {
/*  103 */     Main localMain = new Main();
/*  104 */     localMain.run(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   public void run(String[] paramArrayOfString)
/*      */   {
/*      */     try
/*      */     {
/*  180 */       parseArgs(paramArrayOfString);
/*      */ 
/*  183 */       if (this.providers != null) {
/*  184 */         ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*  185 */         Enumeration localEnumeration = this.providers.elements();
/*  186 */         while (localEnumeration.hasMoreElements()) {
/*  187 */           String str1 = (String)localEnumeration.nextElement();
/*      */           Class localClass;
/*  189 */           if (localClassLoader != null)
/*  190 */             localClass = localClassLoader.loadClass(str1);
/*      */           else {
/*  192 */             localClass = Class.forName(str1);
/*      */           }
/*      */ 
/*  195 */           String str2 = (String)this.providerArgs.get(str1);
/*      */           Object localObject1;
/*      */           Object localObject2;
/*  197 */           if (str2 == null) {
/*  198 */             localObject1 = localClass.newInstance();
/*      */           }
/*      */           else {
/*  201 */             localObject2 = localClass
/*  201 */               .getConstructor(PARAM_STRING);
/*      */ 
/*  202 */             localObject1 = ((Constructor)localObject2).newInstance(new Object[] { str2 });
/*      */           }
/*      */ 
/*  205 */           if (!(localObject1 instanceof Provider))
/*      */           {
/*  207 */             localObject2 = new MessageFormat(rb
/*  207 */               .getString("provName.not.a.provider"));
/*      */ 
/*  208 */             Object[] arrayOfObject = { str1 };
/*  209 */             throw new Exception(((MessageFormat)localObject2).format(arrayOfObject));
/*      */           }
/*  211 */           Security.addProvider((Provider)localObject1);
/*      */         }
/*      */       }
/*      */ 
/*  215 */       if (this.verify) {
/*      */         try {
/*  217 */           loadKeyStore(this.keystore, false);
/*      */         } catch (Exception localException1) {
/*  219 */           if ((this.keystore != null) || (this.storepass != null)) {
/*  220 */             System.out.println(rb.getString("jarsigner.error.") + localException1
/*  221 */               .getMessage());
/*  222 */             System.exit(1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  230 */         verifyJar(this.jarfile);
/*      */       } else {
/*  232 */         loadKeyStore(this.keystore, true);
/*  233 */         getAliasInfo(this.alias);
/*      */ 
/*  236 */         if (this.altSignerClass != null) {
/*  237 */           this.signingMechanism = loadSigningMechanism(this.altSignerClass, this.altSignerClasspath);
/*      */         }
/*      */ 
/*  240 */         signJar(this.jarfile, this.alias, paramArrayOfString);
/*      */       }
/*      */     } catch (Exception localException2) {
/*  243 */       System.out.println(rb.getString("jarsigner.error.") + localException2);
/*  244 */       if (this.debug) {
/*  245 */         localException2.printStackTrace();
/*      */       }
/*  247 */       System.exit(1);
/*      */     }
/*      */     finally {
/*  250 */       if (this.keypass != null) {
/*  251 */         Arrays.fill(this.keypass, ' ');
/*  252 */         this.keypass = null;
/*      */       }
/*      */ 
/*  255 */       if (this.storepass != null) {
/*  256 */         Arrays.fill(this.storepass, ' ');
/*  257 */         this.storepass = null;
/*      */       }
/*      */     }
/*      */ 
/*  261 */     if (this.strict) {
/*  262 */       int i = 0;
/*  263 */       if ((this.chainNotValidated) || (this.hasExpiredCert) || (this.notYetValidCert)) {
/*  264 */         i |= 4;
/*      */       }
/*  266 */       if ((this.badKeyUsage) || (this.badExtendedKeyUsage) || (this.badNetscapeCertType)) {
/*  267 */         i |= 8;
/*      */       }
/*  269 */       if (this.hasUnsignedEntry) {
/*  270 */         i |= 16;
/*      */       }
/*  272 */       if ((this.notSignedByAlias) || (this.aliasNotInStore)) {
/*  273 */         i |= 32;
/*      */       }
/*  275 */       if (i != 0)
/*  276 */         System.exit(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseArgs(String[] paramArrayOfString)
/*      */   {
/*  286 */     int i = 0;
/*      */ 
/*  288 */     if (paramArrayOfString.length == 0) fullusage();
/*  289 */     for (i = 0; i < paramArrayOfString.length; i++)
/*      */     {
/*  291 */       String str1 = paramArrayOfString[i];
/*  292 */       String str2 = null;
/*      */ 
/*  294 */       if (str1.startsWith("-")) {
/*  295 */         int j = str1.indexOf(':');
/*  296 */         if (j > 0) {
/*  297 */           str2 = str1.substring(j + 1);
/*  298 */           str1 = str1.substring(0, j);
/*      */         }
/*      */       }
/*      */ 
/*  302 */       if (!str1.startsWith("-")) {
/*  303 */         if (this.jarfile == null) {
/*  304 */           this.jarfile = str1;
/*      */         } else {
/*  306 */           this.alias = str1;
/*  307 */           this.ckaliases.add(this.alias);
/*      */         }
/*  309 */       } else if (collator.compare(str1, "-keystore") == 0) {
/*  310 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  311 */         this.keystore = paramArrayOfString[i];
/*  312 */       } else if (collator.compare(str1, "-storepass") == 0) {
/*  313 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  314 */         this.storepass = getPass(str2, paramArrayOfString[i]);
/*  315 */       } else if (collator.compare(str1, "-storetype") == 0) {
/*  316 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  317 */         this.storetype = paramArrayOfString[i];
/*  318 */       } else if (collator.compare(str1, "-providerName") == 0) {
/*  319 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  320 */         this.providerName = paramArrayOfString[i];
/*  321 */       } else if ((collator.compare(str1, "-provider") == 0) || 
/*  322 */         (collator
/*  322 */         .compare(str1, "-providerClass") == 0))
/*      */       {
/*  323 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  324 */         if (this.providers == null) {
/*  325 */           this.providers = new Vector(3);
/*      */         }
/*  327 */         this.providers.add(paramArrayOfString[i]);
/*      */ 
/*  329 */         if (paramArrayOfString.length > i + 1) {
/*  330 */           str1 = paramArrayOfString[(i + 1)];
/*  331 */           if (collator.compare(str1, "-providerArg") == 0) {
/*  332 */             if (paramArrayOfString.length == i + 2) usageNoArg();
/*  333 */             this.providerArgs.put(paramArrayOfString[i], paramArrayOfString[(i + 2)]);
/*  334 */             i += 2;
/*      */           }
/*      */         }
/*  337 */       } else if (collator.compare(str1, "-protected") == 0) {
/*  338 */         this.protectedPath = true;
/*  339 */       } else if (collator.compare(str1, "-certchain") == 0) {
/*  340 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  341 */         this.altCertChain = paramArrayOfString[i];
/*  342 */       } else if (collator.compare(str1, "-tsapolicyid") == 0) {
/*  343 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  344 */         this.tSAPolicyID = paramArrayOfString[i];
/*  345 */       } else if (collator.compare(str1, "-debug") == 0) {
/*  346 */         this.debug = true;
/*  347 */       } else if (collator.compare(str1, "-keypass") == 0) {
/*  348 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  349 */         this.keypass = getPass(str2, paramArrayOfString[i]);
/*  350 */       } else if (collator.compare(str1, "-sigfile") == 0) {
/*  351 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  352 */         this.sigfile = paramArrayOfString[i];
/*  353 */       } else if (collator.compare(str1, "-signedjar") == 0) {
/*  354 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  355 */         this.signedjar = paramArrayOfString[i];
/*  356 */       } else if (collator.compare(str1, "-tsa") == 0) {
/*  357 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  358 */         this.tsaUrl = paramArrayOfString[i];
/*  359 */       } else if (collator.compare(str1, "-tsacert") == 0) {
/*  360 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  361 */         this.tsaAlias = paramArrayOfString[i];
/*  362 */       } else if (collator.compare(str1, "-altsigner") == 0) {
/*  363 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  364 */         this.altSignerClass = paramArrayOfString[i];
/*  365 */       } else if (collator.compare(str1, "-altsignerpath") == 0) {
/*  366 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  367 */         this.altSignerClasspath = paramArrayOfString[i];
/*  368 */       } else if (collator.compare(str1, "-sectionsonly") == 0) {
/*  369 */         this.signManifest = false;
/*  370 */       } else if (collator.compare(str1, "-internalsf") == 0) {
/*  371 */         this.externalSF = false;
/*  372 */       } else if (collator.compare(str1, "-verify") == 0) {
/*  373 */         this.verify = true;
/*  374 */       } else if (collator.compare(str1, "-verbose") == 0) {
/*  375 */         this.verbose = (str2 != null ? str2 : "all");
/*  376 */       } else if (collator.compare(str1, "-sigalg") == 0) {
/*  377 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  378 */         this.sigalg = paramArrayOfString[i];
/*  379 */       } else if (collator.compare(str1, "-digestalg") == 0) {
/*  380 */         i++; if (i == paramArrayOfString.length) usageNoArg();
/*  381 */         this.digestalg = paramArrayOfString[i];
/*  382 */       } else if (collator.compare(str1, "-certs") == 0) {
/*  383 */         this.showcerts = true;
/*  384 */       } else if (collator.compare(str1, "-strict") == 0) {
/*  385 */         this.strict = true;
/*  386 */       } else if ((collator.compare(str1, "-h") == 0) || 
/*  387 */         (collator
/*  387 */         .compare(str1, "-help") == 0))
/*      */       {
/*  388 */         fullusage();
/*      */       } else {
/*  390 */         System.err.println(rb
/*  391 */           .getString("Illegal.option.") + 
/*  391 */           str1);
/*  392 */         usage();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  397 */     if (this.verbose == null) this.showcerts = false;
/*      */ 
/*  399 */     if (this.jarfile == null) {
/*  400 */       System.err.println(rb.getString("Please.specify.jarfile.name"));
/*  401 */       usage();
/*      */     }
/*  403 */     if ((!this.verify) && (this.alias == null)) {
/*  404 */       System.err.println(rb.getString("Please.specify.alias.name"));
/*  405 */       usage();
/*      */     }
/*  407 */     if ((!this.verify) && (this.ckaliases.size() > 1)) {
/*  408 */       System.err.println(rb.getString("Only.one.alias.can.be.specified"));
/*  409 */       usage();
/*      */     }
/*      */ 
/*  412 */     if (this.storetype == null) {
/*  413 */       this.storetype = KeyStore.getDefaultType();
/*      */     }
/*  415 */     this.storetype = KeyStoreUtil.niceStoreTypeName(this.storetype);
/*      */     try
/*      */     {
/*  418 */       if ((this.signedjar != null) && (new File(this.signedjar).getCanonicalPath().equals(new File(this.jarfile)
/*  419 */         .getCanonicalPath()))) {
/*  420 */         this.signedjar = null;
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */ 
/*  427 */     if (("PKCS11".equalsIgnoreCase(this.storetype)) || 
/*  428 */       (KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */     {
/*  429 */       this.token = true;
/*  430 */       if (this.keystore == null) {
/*  431 */         this.keystore = "NONE";
/*      */       }
/*      */     }
/*      */ 
/*  435 */     if ("NONE".equals(this.keystore)) {
/*  436 */       this.nullStream = true;
/*      */     }
/*      */ 
/*  439 */     if ((this.token) && (!this.nullStream)) {
/*  440 */       System.err.println(MessageFormat.format(rb
/*  441 */         .getString(".keystore.must.be.NONE.if.storetype.is.{0}"), 
/*  441 */         new Object[] { this.storetype }));
/*  442 */       usage();
/*      */     }
/*      */ 
/*  445 */     if ((this.token) && (this.keypass != null)) {
/*  446 */       System.err.println(MessageFormat.format(rb
/*  447 */         .getString(".keypass.can.not.be.specified.if.storetype.is.{0}"), 
/*  447 */         new Object[] { this.storetype }));
/*  448 */       usage();
/*      */     }
/*      */ 
/*  451 */     if ((this.protectedPath) && (
/*  452 */       (this.storepass != null) || (this.keypass != null))) {
/*  453 */       System.err.println(rb
/*  454 */         .getString("If.protected.is.specified.then.storepass.and.keypass.must.not.be.specified"));
/*      */ 
/*  455 */       usage();
/*      */     }
/*      */ 
/*  458 */     if ((KeyStoreUtil.isWindowsKeyStore(this.storetype)) && (
/*  459 */       (this.storepass != null) || (this.keypass != null))) {
/*  460 */       System.err.println(rb
/*  461 */         .getString("If.keystore.is.not.password.protected.then.storepass.and.keypass.must.not.be.specified"));
/*      */ 
/*  462 */       usage();
/*      */     }
/*      */   }
/*      */ 
/*      */   static char[] getPass(String paramString1, String paramString2)
/*      */   {
/*  468 */     char[] arrayOfChar = KeyStoreUtil.getPassWithModifier(paramString1, paramString2, rb);
/*  469 */     if (arrayOfChar != null) return arrayOfChar;
/*  470 */     usage();
/*  471 */     return null;
/*      */   }
/*      */ 
/*      */   static void usageNoArg() {
/*  475 */     System.out.println(rb.getString("Option.lacks.argument"));
/*  476 */     usage();
/*      */   }
/*      */ 
/*      */   static void usage() {
/*  480 */     System.out.println();
/*  481 */     System.out.println(rb.getString("Please.type.jarsigner.help.for.usage"));
/*  482 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   static void fullusage() {
/*  486 */     System.out.println(rb
/*  487 */       .getString("Usage.jarsigner.options.jar.file.alias"));
/*      */ 
/*  488 */     System.out.println(rb
/*  489 */       .getString(".jarsigner.verify.options.jar.file.alias."));
/*      */ 
/*  490 */     System.out.println();
/*  491 */     System.out.println(rb
/*  492 */       .getString(".keystore.url.keystore.location"));
/*      */ 
/*  493 */     System.out.println();
/*  494 */     System.out.println(rb
/*  495 */       .getString(".storepass.password.password.for.keystore.integrity"));
/*      */ 
/*  496 */     System.out.println();
/*  497 */     System.out.println(rb
/*  498 */       .getString(".storetype.type.keystore.type"));
/*      */ 
/*  499 */     System.out.println();
/*  500 */     System.out.println(rb
/*  501 */       .getString(".keypass.password.password.for.private.key.if.different."));
/*      */ 
/*  502 */     System.out.println();
/*  503 */     System.out.println(rb
/*  504 */       .getString(".certchain.file.name.of.alternative.certchain.file"));
/*      */ 
/*  505 */     System.out.println();
/*  506 */     System.out.println(rb
/*  507 */       .getString(".sigfile.file.name.of.SF.DSA.file"));
/*      */ 
/*  508 */     System.out.println();
/*  509 */     System.out.println(rb
/*  510 */       .getString(".signedjar.file.name.of.signed.JAR.file"));
/*      */ 
/*  511 */     System.out.println();
/*  512 */     System.out.println(rb
/*  513 */       .getString(".digestalg.algorithm.name.of.digest.algorithm"));
/*      */ 
/*  514 */     System.out.println();
/*  515 */     System.out.println(rb
/*  516 */       .getString(".sigalg.algorithm.name.of.signature.algorithm"));
/*      */ 
/*  517 */     System.out.println();
/*  518 */     System.out.println(rb
/*  519 */       .getString(".verify.verify.a.signed.JAR.file"));
/*      */ 
/*  520 */     System.out.println();
/*  521 */     System.out.println(rb
/*  522 */       .getString(".verbose.suboptions.verbose.output.when.signing.verifying."));
/*      */ 
/*  523 */     System.out.println(rb
/*  524 */       .getString(".suboptions.can.be.all.grouped.or.summary"));
/*      */ 
/*  525 */     System.out.println();
/*  526 */     System.out.println(rb
/*  527 */       .getString(".certs.display.certificates.when.verbose.and.verifying"));
/*      */ 
/*  528 */     System.out.println();
/*  529 */     System.out.println(rb
/*  530 */       .getString(".tsa.url.location.of.the.Timestamping.Authority"));
/*      */ 
/*  531 */     System.out.println();
/*  532 */     System.out.println(rb
/*  533 */       .getString(".tsacert.alias.public.key.certificate.for.Timestamping.Authority"));
/*      */ 
/*  534 */     System.out.println();
/*  535 */     System.out.println(rb
/*  536 */       .getString(".tsapolicyid.tsapolicyid.for.Timestamping.Authority"));
/*      */ 
/*  537 */     System.out.println();
/*  538 */     System.out.println(rb
/*  539 */       .getString(".altsigner.class.class.name.of.an.alternative.signing.mechanism"));
/*      */ 
/*  540 */     System.out.println();
/*  541 */     System.out.println(rb
/*  542 */       .getString(".altsignerpath.pathlist.location.of.an.alternative.signing.mechanism"));
/*      */ 
/*  543 */     System.out.println();
/*  544 */     System.out.println(rb
/*  545 */       .getString(".internalsf.include.the.SF.file.inside.the.signature.block"));
/*      */ 
/*  546 */     System.out.println();
/*  547 */     System.out.println(rb
/*  548 */       .getString(".sectionsonly.don.t.compute.hash.of.entire.manifest"));
/*      */ 
/*  549 */     System.out.println();
/*  550 */     System.out.println(rb
/*  551 */       .getString(".protected.keystore.has.protected.authentication.path"));
/*      */ 
/*  552 */     System.out.println();
/*  553 */     System.out.println(rb
/*  554 */       .getString(".providerName.name.provider.name"));
/*      */ 
/*  555 */     System.out.println();
/*  556 */     System.out.println(rb
/*  557 */       .getString(".providerClass.class.name.of.cryptographic.service.provider.s"));
/*      */ 
/*  558 */     System.out.println(rb
/*  559 */       .getString(".providerArg.arg.master.class.file.and.constructor.argument"));
/*      */ 
/*  560 */     System.out.println();
/*  561 */     System.out.println(rb
/*  562 */       .getString(".strict.treat.warnings.as.errors"));
/*      */ 
/*  563 */     System.out.println();
/*      */ 
/*  565 */     System.exit(0);
/*      */   }
/*      */ 
/*      */   void verifyJar(String paramString)
/*      */     throws Exception
/*      */   {
/*  571 */     int i = 0;
/*  572 */     JarFile localJarFile = null;
/*      */     try
/*      */     {
/*  575 */       localJarFile = new JarFile(paramString, true);
/*  576 */       Vector localVector = new Vector();
/*  577 */       byte[] arrayOfByte = new byte[8192];
/*      */ 
/*  579 */       Enumeration localEnumeration = localJarFile.entries();
/*  580 */       while (localEnumeration.hasMoreElements()) {
/*  581 */         localObject1 = (JarEntry)localEnumeration.nextElement();
/*  582 */         localVector.addElement(localObject1);
/*  583 */         localObject2 = null;
/*      */         try {
/*  585 */           localObject2 = localJarFile.getInputStream((ZipEntry)localObject1);
/*      */           int j;
/*  587 */           while ((j = ((InputStream)localObject2).read(arrayOfByte, 0, arrayOfByte.length)) != -1);
/*      */         }
/*      */         finally {
/*  592 */           if (localObject2 != null) {
/*  593 */             ((InputStream)localObject2).close();
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  598 */       Object localObject1 = localJarFile.getManifest();
/*      */ 
/*  603 */       Object localObject2 = new LinkedHashMap();
/*      */       Object localObject3;
/*      */       Object localObject5;
/*      */       Object localObject6;
/*      */       String str1;
/*  605 */       if (localObject1 != null) {
/*  606 */         if (this.verbose != null) System.out.println();
/*  607 */         localObject3 = localVector.elements();
/*      */ 
/*  609 */         localObject5 = rb.getString("6SPACE");
/*      */ 
/*  611 */         while (((Enumeration)localObject3).hasMoreElements()) {
/*  612 */           localObject6 = (JarEntry)((Enumeration)localObject3).nextElement();
/*  613 */           str1 = ((JarEntry)localObject6).getName();
/*  614 */           CodeSigner[] arrayOfCodeSigner = ((JarEntry)localObject6).getCodeSigners();
/*  615 */           int i1 = arrayOfCodeSigner != null ? 1 : 0;
/*  616 */           i |= i1;
/*  617 */           this.hasUnsignedEntry = 
/*  618 */             (this.hasUnsignedEntry | ((!((JarEntry)localObject6).isDirectory()) && (i1 == 0) && 
/*  618 */             (!signatureRelated(str1))));
/*      */ 
/*  620 */           int i2 = inKeyStore(arrayOfCodeSigner);
/*      */ 
/*  622 */           int i3 = (i2 & 0x1) != 0 ? 1 : 0;
/*  623 */           int i4 = (i2 & 0x2) != 0 ? 1 : 0;
/*      */ 
/*  625 */           this.notSignedByAlias |= (i2 & 0x4) != 0;
/*  626 */           if (this.keystore != null) {
/*  627 */             this.aliasNotInStore |= ((i1 != 0) && (i3 == 0) && (i4 == 0));
/*      */           }
/*      */ 
/*  631 */           StringBuffer localStringBuffer1 = null;
/*  632 */           if (this.verbose != null) {
/*  633 */             localStringBuffer1 = new StringBuffer();
/*      */ 
/*  637 */             int i5 = (((Manifest)localObject1)
/*  635 */               .getAttributes(str1) != null) || 
/*  636 */               (((Manifest)localObject1)
/*  636 */               .getAttributes("./" + str1) != null) || 
/*  637 */               (((Manifest)localObject1)
/*  637 */               .getAttributes("/" + str1) != null) ? 
/*  637 */               1 : 0;
/*  638 */             localStringBuffer1.append((i1 != 0 ? rb
/*  639 */               .getString("s") : 
/*  639 */               rb.getString("SPACE")) + (i5 != 0 ? rb
/*  640 */               .getString("m") : 
/*  640 */               rb.getString("SPACE")) + (i3 != 0 ? rb
/*  641 */               .getString("k") : 
/*  641 */               rb.getString("SPACE")) + (i4 != 0 ? rb
/*  642 */               .getString("i") : 
/*  642 */               rb.getString("SPACE")) + ((i2 & 0x4) != 0 ? "X" : " ") + rb
/*  644 */               .getString("SPACE"));
/*      */ 
/*  645 */             localStringBuffer1.append("|");
/*      */           }
/*      */ 
/*  650 */           if (i1 != 0) {
/*  651 */             if (this.showcerts) localStringBuffer1.append('\n');
/*  652 */             for (CodeSigner localCodeSigner : arrayOfCodeSigner)
/*      */             {
/*  656 */               String str4 = signerInfo(localCodeSigner, (String)localObject5);
/*  657 */               if (this.showcerts) {
/*  658 */                 localStringBuffer1.append(str4);
/*  659 */                 localStringBuffer1.append('\n');
/*      */               }
/*      */             }
/*  662 */           } else if ((this.showcerts) && (!this.verbose.equals("all")))
/*      */           {
/*  665 */             if (signatureRelated(str1)) {
/*  666 */               localStringBuffer1.append("\n" + (String)localObject5 + rb.getString(".Signature.related.entries.") + "\n\n");
/*      */             }
/*      */             else {
/*  669 */               localStringBuffer1.append("\n" + (String)localObject5 + rb.getString(".Unsigned.entries.") + "\n\n");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  674 */           if (this.verbose != null) {
/*  675 */             ??? = localStringBuffer1.toString();
/*  676 */             if (signatureRelated(str1))
/*      */             {
/*  679 */               ??? = "-" + (String)???;
/*      */             }
/*      */ 
/*  686 */             if (!((Map)localObject2).containsKey(???)) {
/*  687 */               ((Map)localObject2).put(???, new ArrayList());
/*      */             }
/*      */ 
/*  690 */             StringBuffer localStringBuffer2 = new StringBuffer();
/*  691 */             String str3 = Long.toString(((JarEntry)localObject6).getSize());
/*  692 */             for (int i8 = 6 - str3.length(); i8 > 0; i8--) {
/*  693 */               localStringBuffer2.append(' ');
/*      */             }
/*  695 */             localStringBuffer2.append(str3).append(' ')
/*  696 */               .append(new Date(((JarEntry)localObject6)
/*  696 */               .getTime()).toString());
/*  697 */             localStringBuffer2.append(' ').append(str1);
/*      */ 
/*  699 */             ((List)((Map)localObject2).get(???)).add(localStringBuffer2.toString());
/*      */           }
/*      */         }
/*      */       }
/*  703 */       if (this.verbose != null) {
/*  704 */         for (localObject3 = ((Map)localObject2).entrySet().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject5 = (Map.Entry)((Iterator)localObject3).next();
/*  705 */           localObject6 = (List)((Map.Entry)localObject5).getValue();
/*  706 */           str1 = (String)((Map.Entry)localObject5).getKey();
/*  707 */           if (str1.charAt(0) == '-') {
/*  708 */             str1 = str1.substring(1);
/*      */           }
/*  710 */           int n = str1.indexOf('|');
/*      */           Iterator localIterator;
/*      */           String str2;
/*  711 */           if (this.verbose.equals("all")) {
/*  712 */             for (localIterator = ((List)localObject6).iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/*  713 */               System.out.println(str1.substring(0, n) + str2);
/*  714 */               System.out.printf(str1.substring(n + 1), new Object[0]); }
/*      */           }
/*      */           else {
/*  717 */             if (this.verbose.equals("grouped")) {
/*  718 */               for (localIterator = ((List)localObject6).iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/*  719 */                 System.out.println(str1.substring(0, n) + str2); }
/*      */             }
/*  721 */             else if (this.verbose.equals("summary")) {
/*  722 */               System.out.print(str1.substring(0, n));
/*  723 */               if (((List)localObject6).size() > 1)
/*  724 */                 System.out.println((String)((List)localObject6).get(0) + " " + 
/*  725 */                   String.format(rb
/*  725 */                   .getString(".and.d.more."), 
/*  725 */                   new Object[] { 
/*  726 */                   Integer.valueOf(((List)localObject6)
/*  726 */                   .size() - 1) }));
/*      */               else {
/*  728 */                 System.out.println((String)((List)localObject6).get(0));
/*      */               }
/*      */             }
/*  731 */             System.out.printf(str1.substring(n + 1), new Object[0]);
/*      */           }
/*      */         }
/*  734 */         System.out.println();
/*  735 */         System.out.println(rb.getString(".s.signature.was.verified."));
/*      */ 
/*  737 */         System.out.println(rb.getString(".m.entry.is.listed.in.manifest"));
/*      */ 
/*  739 */         System.out.println(rb.getString(".k.at.least.one.certificate.was.found.in.keystore"));
/*      */ 
/*  741 */         System.out.println(rb.getString(".i.at.least.one.certificate.was.found.in.identity.scope"));
/*      */ 
/*  743 */         if (this.ckaliases.size() > 0) {
/*  744 */           System.out.println(rb.getString(".X.not.signed.by.specified.alias.es."));
/*      */         }
/*      */ 
/*  747 */         System.out.println();
/*      */       }
/*  749 */       if (localObject1 == null) {
/*  750 */         System.out.println(rb.getString("no.manifest."));
/*      */       }
/*  752 */       if (i == 0) {
/*  753 */         System.out.println(rb.getString("jar.is.unsigned.signatures.missing.or.not.parsable."));
/*      */       }
/*      */       else {
/*  756 */         int k = 0;
/*  757 */         int m = 0;
/*  758 */         if ((this.badKeyUsage) || (this.badExtendedKeyUsage) || (this.badNetscapeCertType) || (this.notYetValidCert) || (this.chainNotValidated) || (this.hasExpiredCert) || (this.hasUnsignedEntry) || (this.aliasNotInStore) || (this.notSignedByAlias))
/*      */         {
/*  763 */           if (this.strict) {
/*  764 */             System.out.println(rb.getString("jar.verified.with.signer.errors."));
/*  765 */             System.out.println();
/*  766 */             System.out.println(rb.getString("Error."));
/*  767 */             m = 1;
/*      */           } else {
/*  769 */             System.out.println(rb.getString("jar.verified."));
/*  770 */             System.out.println();
/*  771 */             System.out.println(rb.getString("Warning."));
/*  772 */             k = 1;
/*      */           }
/*      */ 
/*  775 */           if (this.badKeyUsage) {
/*  776 */             System.out.println(rb
/*  777 */               .getString("This.jar.contains.entries.whose.signer.certificate.s.KeyUsage.extension.doesn.t.allow.code.signing."));
/*      */           }
/*      */ 
/*  780 */           if (this.badExtendedKeyUsage) {
/*  781 */             System.out.println(rb
/*  782 */               .getString("This.jar.contains.entries.whose.signer.certificate.s.ExtendedKeyUsage.extension.doesn.t.allow.code.signing."));
/*      */           }
/*      */ 
/*  785 */           if (this.badNetscapeCertType) {
/*  786 */             System.out.println(rb
/*  787 */               .getString("This.jar.contains.entries.whose.signer.certificate.s.NetscapeCertType.extension.doesn.t.allow.code.signing."));
/*      */           }
/*      */ 
/*  790 */           if (this.hasUnsignedEntry) {
/*  791 */             System.out.println(rb.getString("This.jar.contains.unsigned.entries.which.have.not.been.integrity.checked."));
/*      */           }
/*      */ 
/*  794 */           if (this.hasExpiredCert) {
/*  795 */             System.out.println(rb.getString("This.jar.contains.entries.whose.signer.certificate.has.expired."));
/*      */           }
/*      */ 
/*  798 */           if (this.notYetValidCert) {
/*  799 */             System.out.println(rb.getString("This.jar.contains.entries.whose.signer.certificate.is.not.yet.valid."));
/*      */           }
/*      */ 
/*  803 */           if (this.chainNotValidated) {
/*  804 */             System.out.println(rb
/*  805 */               .getString("This.jar.contains.entries.whose.certificate.chain.is.not.validated."));
/*      */           }
/*      */ 
/*  808 */           if (this.notSignedByAlias) {
/*  809 */             System.out.println(rb
/*  810 */               .getString("This.jar.contains.signed.entries.which.is.not.signed.by.the.specified.alias.es."));
/*      */           }
/*      */ 
/*  813 */           if (this.aliasNotInStore)
/*  814 */             System.out.println(rb.getString("This.jar.contains.signed.entries.that.s.not.signed.by.alias.in.this.keystore."));
/*      */         }
/*      */         else {
/*  817 */           System.out.println(rb.getString("jar.verified."));
/*      */         }
/*  819 */         if ((this.hasExpiringCert) || (this.noTimestamp)) {
/*  820 */           if (k == 0) {
/*  821 */             System.out.println();
/*  822 */             System.out.println(rb.getString("Warning."));
/*  823 */             k = 1;
/*      */           }
/*  825 */           if (this.hasExpiringCert) {
/*  826 */             System.out.println(rb.getString("This.jar.contains.entries.whose.signer.certificate.will.expire.within.six.months."));
/*      */           }
/*      */ 
/*  829 */           if (this.noTimestamp) {
/*  830 */             System.out.println(
/*  831 */               String.format(rb
/*  831 */               .getString("no.timestamp.verifying"), 
/*  831 */               new Object[] { this.expireDate }));
/*      */           }
/*      */         }
/*  834 */         if (((k != 0) || (m != 0)) && (
/*  835 */           (this.verbose == null) || (!this.showcerts))) {
/*  836 */           System.out.println();
/*  837 */           System.out.println(rb.getString("Re.run.with.the.verbose.and.certs.options.for.more.details."));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  842 */       return;
/*      */     } catch (Exception localException) {
/*  844 */       System.out.println(rb.getString("jarsigner.") + localException);
/*  845 */       if (this.debug)
/*  846 */         localException.printStackTrace();
/*      */     }
/*      */     finally {
/*  849 */       if (localJarFile != null) {
/*  850 */         localJarFile.close();
/*      */       }
/*      */     }
/*      */ 
/*  854 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   String printCert(String paramString, Certificate paramCertificate, boolean paramBoolean1, Date paramDate, boolean paramBoolean2)
/*      */   {
/*  873 */     StringBuilder localStringBuilder = new StringBuilder();
/*  874 */     String str1 = rb.getString("SPACE");
/*  875 */     X509Certificate localX509Certificate = null;
/*      */ 
/*  877 */     if ((paramCertificate instanceof X509Certificate)) {
/*  878 */       localX509Certificate = (X509Certificate)paramCertificate;
/*  879 */       localStringBuilder.append(paramString).append(localX509Certificate.getType())
/*  880 */         .append(rb
/*  880 */         .getString("COMMA"))
/*  881 */         .append(localX509Certificate
/*  881 */         .getSubjectDN().getName());
/*      */     } else {
/*  883 */       localStringBuilder.append(paramString).append(paramCertificate.getType());
/*      */     }
/*      */ 
/*  886 */     String str2 = (String)this.storeHash.get(paramCertificate);
/*  887 */     if (str2 != null) {
/*  888 */       localStringBuilder.append(str1).append(str2);
/*      */     }
/*      */ 
/*  891 */     if ((paramBoolean1) && (localX509Certificate != null)) { localStringBuilder.append("\n").append(paramString).append("[");
/*  894 */       Date localDate = localX509Certificate.getNotAfter();
/*      */       Object localObject;
/*      */       try { int i = 1;
/*  897 */         if (paramDate == null) {
/*  898 */           if ((this.expireDate.getTime() == 0L) || (this.expireDate.after(localDate))) {
/*  899 */             this.expireDate = localDate;
/*      */           }
/*  901 */           localX509Certificate.checkValidity();
/*      */ 
/*  903 */           if (localDate.getTime() < System.currentTimeMillis() + 15552000000L) {
/*  904 */             this.hasExpiringCert = true;
/*  905 */             if (expiringTimeForm == null)
/*      */             {
/*  907 */               expiringTimeForm = new MessageFormat(rb
/*  907 */                 .getString("certificate.will.expire.on"));
/*      */             }
/*      */ 
/*  909 */             localObject = new Object[] { localDate };
/*  910 */             localStringBuilder.append(expiringTimeForm.format(localObject));
/*  911 */             i = 0;
/*      */           }
/*      */         } else {
/*  914 */           localX509Certificate.checkValidity(paramDate);
/*      */         }
/*  916 */         if (i != 0) {
/*  917 */           if (validityTimeForm == null)
/*      */           {
/*  919 */             validityTimeForm = new MessageFormat(rb
/*  919 */               .getString("certificate.is.valid.from"));
/*      */           }
/*      */ 
/*  921 */           localObject = new Object[] { localX509Certificate.getNotBefore(), localDate };
/*  922 */           localStringBuilder.append(validityTimeForm.format(localObject));
/*      */         }
/*      */       } catch (CertificateExpiredException localCertificateExpiredException) {
/*  925 */         this.hasExpiredCert = true;
/*      */ 
/*  927 */         if (expiredTimeForm == null)
/*      */         {
/*  929 */           expiredTimeForm = new MessageFormat(rb
/*  929 */             .getString("certificate.expired.on"));
/*      */         }
/*      */ 
/*  931 */         localObject = new Object[] { localDate };
/*  932 */         localStringBuilder.append(expiredTimeForm.format(localObject));
/*      */       }
/*      */       catch (CertificateNotYetValidException localCertificateNotYetValidException) {
/*  935 */         this.notYetValidCert = true;
/*      */ 
/*  937 */         if (notYetTimeForm == null)
/*      */         {
/*  939 */           notYetTimeForm = new MessageFormat(rb
/*  939 */             .getString("certificate.is.not.valid.until"));
/*      */         }
/*      */ 
/*  941 */         localObject = new Object[] { localX509Certificate.getNotBefore() };
/*  942 */         localStringBuilder.append(notYetTimeForm.format(localObject));
/*      */       }
/*  944 */       localStringBuilder.append("]");
/*      */ 
/*  946 */       if (paramBoolean2) {
/*  947 */         boolean[] arrayOfBoolean = new boolean[3];
/*  948 */         checkCertUsage(localX509Certificate, arrayOfBoolean);
/*  949 */         if ((arrayOfBoolean[0] != 0) || (arrayOfBoolean[1] != 0) || (arrayOfBoolean[2] != 0)) {
/*  950 */           localObject = "";
/*  951 */           if (arrayOfBoolean[0] != 0) {
/*  952 */             localObject = "KeyUsage";
/*      */           }
/*  954 */           if (arrayOfBoolean[1] != 0) {
/*  955 */             if (((String)localObject).length() > 0) localObject = (String)localObject + ", ";
/*  956 */             localObject = (String)localObject + "ExtendedKeyUsage";
/*      */           }
/*  958 */           if (arrayOfBoolean[2] != 0) {
/*  959 */             if (((String)localObject).length() > 0) localObject = (String)localObject + ", ";
/*  960 */             localObject = (String)localObject + "NetscapeCertType";
/*      */           }
/*  962 */           localStringBuilder.append("\n").append(paramString)
/*  963 */             .append(MessageFormat.format(rb
/*  963 */             .getString(".{0}.extension.does.not.support.code.signing."), 
/*  963 */             new Object[] { localObject }));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  968 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private String printTimestamp(String paramString, Timestamp paramTimestamp)
/*      */   {
/*  975 */     if (signTimeForm == null)
/*      */     {
/*  977 */       signTimeForm = new MessageFormat(rb
/*  977 */         .getString("entry.was.signed.on"));
/*      */     }
/*      */ 
/*  979 */     Object[] arrayOfObject = { paramTimestamp.getTimestamp() };
/*      */ 
/*  982 */     return paramString + "[" + signTimeForm
/*  982 */       .format(arrayOfObject) + 
/*  982 */       "]";
/*      */   }
/*      */ 
/*      */   private int inKeyStoreForOneSigner(CodeSigner paramCodeSigner)
/*      */   {
/*  988 */     if (this.cacheForInKS.containsKey(paramCodeSigner)) {
/*  989 */       return ((Integer)this.cacheForInKS.get(paramCodeSigner)).intValue();
/*      */     }
/*      */ 
/*  992 */     int i = 0;
/*  993 */     int j = 0;
/*  994 */     List localList = paramCodeSigner.getSignerCertPath().getCertificates();
/*  995 */     for (Certificate localCertificate : localList) {
/*  996 */       String str = (String)this.storeHash.get(localCertificate);
/*  997 */       if (str != null) {
/*  998 */         if (str.startsWith("("))
/*  999 */           j |= 1;
/* 1000 */         else if (str.startsWith("[")) {
/* 1001 */           j |= 2;
/*      */         }
/* 1003 */         if (this.ckaliases.contains(str.substring(1, str.length() - 1)))
/* 1004 */           j |= 8;
/*      */       }
/*      */       else {
/* 1007 */         if (this.store != null) {
/*      */           try {
/* 1009 */             str = this.store.getCertificateAlias(localCertificate);
/*      */           }
/*      */           catch (KeyStoreException localKeyStoreException) {
/*      */           }
/* 1013 */           if (str != null) {
/* 1014 */             this.storeHash.put(localCertificate, "(" + str + ")");
/* 1015 */             i = 1;
/* 1016 */             j |= 1;
/*      */           }
/*      */         }
/* 1019 */         if (this.ckaliases.contains(str)) {
/* 1020 */           j |= 8;
/*      */         }
/*      */       }
/*      */     }
/* 1024 */     this.cacheForInKS.put(paramCodeSigner, Integer.valueOf(j));
/* 1025 */     return j;
/*      */   }
/*      */ 
/*      */   int inKeyStore(CodeSigner[] paramArrayOfCodeSigner)
/*      */   {
/* 1032 */     if (paramArrayOfCodeSigner == null) {
/* 1033 */       return 0;
/*      */     }
/* 1035 */     int i = 0;
/*      */ 
/* 1037 */     for (CodeSigner localCodeSigner : paramArrayOfCodeSigner) {
/* 1038 */       int m = inKeyStoreForOneSigner(localCodeSigner);
/* 1039 */       i |= m;
/*      */     }
/* 1041 */     if ((this.ckaliases.size() > 0) && ((i & 0x8) == 0)) {
/* 1042 */       i |= 4;
/*      */     }
/* 1044 */     return i;
/*      */   }
/*      */ 
/*      */   void signJar(String paramString1, String paramString2, String[] paramArrayOfString) throws Exception
/*      */   {
/* 1049 */     int i = 0;
/* 1050 */     X509Certificate localX509Certificate = null;
/*      */ 
/* 1052 */     if (this.sigfile == null) {
/* 1053 */       this.sigfile = paramString2;
/* 1054 */       i = 1;
/*      */     }
/*      */ 
/* 1057 */     if (this.sigfile.length() > 8)
/* 1058 */       this.sigfile = this.sigfile.substring(0, 8).toUpperCase(Locale.ENGLISH);
/*      */     else {
/* 1060 */       this.sigfile = this.sigfile.toUpperCase(Locale.ENGLISH);
/*      */     }
/*      */ 
/* 1063 */     StringBuilder localStringBuilder = new StringBuilder(this.sigfile.length());
/* 1064 */     for (int j = 0; j < this.sigfile.length(); j++) {
/* 1065 */       char c = this.sigfile.charAt(j);
/* 1066 */       if (((c < 'A') || (c > 'Z')) && ((c < '0') || (c > '9')) && (c != '-') && (c != '_'))
/*      */       {
/* 1071 */         if (i != 0)
/*      */         {
/* 1073 */           c = '_';
/*      */         }
/*      */         else
/*      */         {
/* 1077 */           throw new RuntimeException(rb
/* 1077 */             .getString("signature.filename.must.consist.of.the.following.characters.A.Z.0.9.or."));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1080 */       localStringBuilder.append(c);
/*      */     }
/*      */ 
/* 1083 */     this.sigfile = localStringBuilder.toString();
/*      */     String str1;
/* 1086 */     if (this.signedjar == null) str1 = paramString1 + ".sig"; else {
/* 1087 */       str1 = this.signedjar;
/*      */     }
/* 1089 */     File localFile1 = new File(paramString1);
/* 1090 */     File localFile2 = new File(str1);
/*      */     try
/*      */     {
/* 1094 */       this.zipFile = new ZipFile(paramString1);
/*      */     } catch (IOException localIOException1) {
/* 1096 */       error(rb.getString("unable.to.open.jar.file.") + paramString1, localIOException1);
/*      */     }
/*      */ 
/* 1099 */     FileOutputStream localFileOutputStream = null;
/*      */     try {
/* 1101 */       localFileOutputStream = new FileOutputStream(localFile2);
/*      */     } catch (IOException localIOException2) {
/* 1103 */       error(rb.getString("unable.to.create.") + str1, localIOException2); } 
/*      */ PrintStream localPrintStream = new PrintStream(localFileOutputStream);
/* 1107 */     ZipOutputStream localZipOutputStream = new ZipOutputStream(localPrintStream);
/*      */ 
/* 1110 */     String str2 = ("META-INF/" + this.sigfile + ".SF").toUpperCase(Locale.ENGLISH);
/* 1111 */     String str3 = ("META-INF/" + this.sigfile + ".DSA").toUpperCase(Locale.ENGLISH);
/*      */ 
/* 1113 */     Manifest localManifest = new Manifest();
/* 1114 */     Map localMap = localManifest.getEntries();
/*      */ 
/* 1117 */     Attributes localAttributes1 = null;
/*      */ 
/* 1119 */     int k = 0;
/* 1120 */     int m = 0;
/* 1121 */     Object localObject1 = null;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     try { MessageDigest[] arrayOfMessageDigest = { MessageDigest.getInstance(this.digestalg) };
/*      */ 
/* 1128 */       if ((localObject2 = getManifestFile(this.zipFile)) != null)
/*      */       {
/* 1130 */         localObject1 = getBytes(this.zipFile, (ZipEntry)localObject2);
/* 1131 */         localManifest.read(new ByteArrayInputStream((byte[])localObject1));
/* 1132 */         localAttributes1 = (Attributes)localManifest.getMainAttributes().clone();
/*      */       }
/*      */       else {
/* 1135 */         localObject3 = localManifest.getMainAttributes();
/* 1136 */         ((Attributes)localObject3).putValue(Attributes.Name.MANIFEST_VERSION.toString(), "1.0");
/*      */ 
/* 1138 */         String str4 = System.getProperty("java.vendor");
/* 1139 */         localObject4 = System.getProperty("java.version");
/* 1140 */         ((Attributes)localObject3).putValue("Created-By", (String)localObject4 + " (" + str4 + ")");
/*      */ 
/* 1142 */         localObject2 = new ZipEntry("META-INF/MANIFEST.MF");
/* 1143 */         m = 1;
/*      */       }
/*      */ 
/* 1157 */       localObject3 = new Vector();
/*      */ 
/* 1159 */       int i1 = 0;
/*      */ 
/* 1161 */       Object localObject4 = this.zipFile.entries();
/* 1162 */       while (((Enumeration)localObject4).hasMoreElements()) {
/* 1163 */         localObject5 = (ZipEntry)((Enumeration)localObject4).nextElement();
/*      */ 
/* 1165 */         if (((ZipEntry)localObject5).getName().startsWith("META-INF/"))
/*      */         {
/* 1168 */           ((Vector)localObject3).addElement(localObject5);
/*      */ 
/* 1170 */           if (SignatureFileVerifier.isBlockOrSF(((ZipEntry)localObject5)
/* 1171 */             .getName().toUpperCase(Locale.ENGLISH))) {
/* 1172 */             i1 = 1;
/*      */           }
/*      */ 
/* 1175 */           if (signatureRelated(((ZipEntry)localObject5).getName()));
/*      */         }
/* 1181 */         else if (localManifest.getAttributes(((ZipEntry)localObject5).getName()) != null)
/*      */         {
/* 1184 */           if (updateDigests((ZipEntry)localObject5, this.zipFile, arrayOfMessageDigest, localManifest) == true)
/*      */           {
/* 1186 */             k = 1;
/*      */           }
/* 1188 */         } else if (!((ZipEntry)localObject5).isDirectory())
/*      */         {
/* 1190 */           Attributes localAttributes2 = getDigestAttributes((ZipEntry)localObject5, this.zipFile, arrayOfMessageDigest);
/*      */ 
/* 1192 */           localMap.put(((ZipEntry)localObject5).getName(), localAttributes2);
/* 1193 */           k = 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1198 */       if (k != 0) {
/* 1199 */         localObject4 = new ByteArrayOutputStream();
/* 1200 */         localManifest.write((OutputStream)localObject4);
/* 1201 */         if (i1 != 0) {
/* 1202 */           localObject5 = ((ByteArrayOutputStream)localObject4).toByteArray();
/* 1203 */           if ((localObject1 != null) && 
/* 1204 */             (localAttributes1
/* 1204 */             .equals(localManifest
/* 1204 */             .getMainAttributes())))
/*      */           {
/* 1217 */             int i2 = findHeaderEnd((byte[])localObject5);
/* 1218 */             int i3 = findHeaderEnd((byte[])localObject1);
/*      */ 
/* 1220 */             if (i2 == i3) {
/* 1221 */               System.arraycopy(localObject1, 0, localObject5, 0, i3);
/*      */             }
/*      */             else {
/* 1224 */               localObject6 = new byte[i3 + localObject5.length - i2];
/*      */ 
/* 1226 */               System.arraycopy(localObject1, 0, localObject6, 0, i3);
/* 1227 */               System.arraycopy(localObject5, i2, localObject6, i3, localObject5.length - i2);
/*      */ 
/* 1229 */               localObject5 = localObject6;
/*      */             }
/*      */           }
/* 1232 */           localObject1 = localObject5;
/*      */         } else {
/* 1234 */           localObject1 = ((ByteArrayOutputStream)localObject4).toByteArray();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1239 */       if (k != 0)
/*      */       {
/* 1241 */         localObject2 = new ZipEntry("META-INF/MANIFEST.MF");
/*      */       }
/* 1243 */       if (this.verbose != null) {
/* 1244 */         if (m != 0)
/* 1245 */           System.out.println(rb.getString(".adding.") + ((ZipEntry)localObject2)
/* 1246 */             .getName());
/* 1247 */         else if (k != 0) {
/* 1248 */           System.out.println(rb.getString(".updating.") + ((ZipEntry)localObject2)
/* 1249 */             .getName());
/*      */         }
/*      */       }
/* 1252 */       localZipOutputStream.putNextEntry((ZipEntry)localObject2);
/* 1253 */       localZipOutputStream.write((byte[])localObject1);
/*      */ 
/* 1256 */       localObject4 = new ManifestDigester((byte[])localObject1);
/* 1257 */       Object localObject5 = new SignatureFile(arrayOfMessageDigest, localManifest, (ManifestDigester)localObject4, this.sigfile, this.signManifest);
/*      */ 
/* 1260 */       if (this.tsaAlias != null) {
/* 1261 */         localX509Certificate = getTsaCert(this.tsaAlias);
/*      */       }
/*      */ 
/* 1264 */       if ((this.tsaUrl == null) && (localX509Certificate == null)) {
/* 1265 */         this.noTimestamp = true;
/*      */       }
/*      */ 
/* 1268 */       SignatureFile.Block localBlock = null;
/*      */       try
/*      */       {
/* 1272 */         localBlock = ((SignatureFile)localObject5)
/* 1272 */           .generateBlock(this.privateKey, this.sigalg, this.certChain, this.externalSF, this.tsaUrl, localX509Certificate, this.tSAPolicyID, this.signingMechanism, paramArrayOfString, this.zipFile);
/*      */       }
/*      */       catch (SocketTimeoutException localSocketTimeoutException)
/*      */       {
/* 1277 */         error(rb.getString("unable.to.sign.jar.") + rb
/* 1278 */           .getString("no.response.from.the.Timestamping.Authority.") + 
/* 1278 */           "\n  -J-Dhttp.proxyHost=<hostname>" + "\n  -J-Dhttp.proxyPort=<portnumber>\n" + rb
/* 1281 */           .getString("or") + 
/* 1281 */           "\n  -J-Dhttps.proxyHost=<hostname> " + "\n  -J-Dhttps.proxyPort=<portnumber> ", localSocketTimeoutException);
/*      */       }
/*      */ 
/* 1286 */       str2 = ((SignatureFile)localObject5).getMetaName();
/* 1287 */       str3 = localBlock.getMetaName();
/*      */ 
/* 1289 */       ZipEntry localZipEntry1 = new ZipEntry(str2);
/* 1290 */       Object localObject6 = new ZipEntry(str3);
/*      */ 
/* 1292 */       long l = System.currentTimeMillis();
/* 1293 */       localZipEntry1.setTime(l);
/* 1294 */       ((ZipEntry)localObject6).setTime(l);
/*      */ 
/* 1297 */       localZipOutputStream.putNextEntry(localZipEntry1);
/* 1298 */       ((SignatureFile)localObject5).write(localZipOutputStream);
/* 1299 */       if (this.verbose != null) {
/* 1300 */         if (this.zipFile.getEntry(str2) != null) {
/* 1301 */           System.out.println(rb.getString(".updating.") + str2);
/*      */         }
/*      */         else {
/* 1304 */           System.out.println(rb.getString(".adding.") + str2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1309 */       if (this.verbose != null) {
/* 1310 */         if ((this.tsaUrl != null) || (localX509Certificate != null)) {
/* 1311 */           System.out.println(rb
/* 1312 */             .getString("requesting.a.signature.timestamp"));
/*      */         }
/*      */ 
/* 1314 */         if (this.tsaUrl != null) {
/* 1315 */           System.out.println(rb.getString("TSA.location.") + this.tsaUrl);
/*      */         }
/* 1317 */         if (localX509Certificate != null) {
/* 1318 */           URI localURI = TimestampedSigner.getTimestampingURI(localX509Certificate);
/* 1319 */           if (localURI != null) {
/* 1320 */             System.out.println(rb.getString("TSA.location.") + localURI);
/*      */           }
/*      */ 
/* 1323 */           System.out.println(rb.getString("TSA.certificate.") + 
/* 1324 */             printCert("", localX509Certificate, false, null, false));
/*      */         }
/*      */ 
/* 1326 */         if (this.signingMechanism != null) {
/* 1327 */           System.out.println(rb
/* 1328 */             .getString("using.an.alternative.signing.mechanism"));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1333 */       localZipOutputStream.putNextEntry((ZipEntry)localObject6);
/* 1334 */       localBlock.write(localZipOutputStream);
/* 1335 */       if (this.verbose != null)
/* 1336 */         if (this.zipFile.getEntry(str3) != null) {
/* 1337 */           System.out.println(rb.getString(".updating.") + str3);
/*      */         }
/*      */         else
/* 1340 */           System.out.println(rb.getString(".adding.") + str3);
/*      */       ZipEntry localZipEntry2;
/* 1347 */       for (int i4 = 0; i4 < ((Vector)localObject3).size(); i4++) {
/* 1348 */         localZipEntry2 = (ZipEntry)((Vector)localObject3).elementAt(i4);
/* 1349 */         if ((!localZipEntry2.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")) && 
/* 1350 */           (!localZipEntry2
/* 1350 */           .getName().equalsIgnoreCase(str2)) && 
/* 1351 */           (!localZipEntry2
/* 1351 */           .getName().equalsIgnoreCase(str3))) {
/* 1352 */           writeEntry(this.zipFile, localZipOutputStream, localZipEntry2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1357 */       Enumeration localEnumeration = this.zipFile.entries();
/* 1358 */       while (localEnumeration.hasMoreElements()) {
/* 1359 */         localZipEntry2 = (ZipEntry)localEnumeration.nextElement();
/*      */ 
/* 1361 */         if (!localZipEntry2.getName().startsWith("META-INF/")) {
/* 1362 */           if (this.verbose != null) {
/* 1363 */             if (localManifest.getAttributes(localZipEntry2.getName()) != null)
/* 1364 */               System.out.println(rb.getString(".signing.") + localZipEntry2
/* 1365 */                 .getName());
/*      */             else
/* 1367 */               System.out.println(rb.getString(".adding.") + localZipEntry2
/* 1368 */                 .getName());
/*      */           }
/* 1370 */           writeEntry(this.zipFile, localZipOutputStream, localZipEntry2);
/*      */         }
/*      */       }
/*      */     } catch (IOException localIOException3) {
/* 1374 */       error(rb.getString("unable.to.sign.jar.") + localIOException3, localIOException3);
/*      */     }
/*      */     finally {
/* 1377 */       if (this.zipFile != null) {
/* 1378 */         this.zipFile.close();
/* 1379 */         this.zipFile = null;
/*      */       }
/*      */ 
/* 1382 */       if (localZipOutputStream != null) {
/* 1383 */         localZipOutputStream.close();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1390 */     if (this.signedjar == null)
/*      */     {
/* 1394 */       if (!localFile2.renameTo(localFile1)) {
/* 1395 */         File localFile3 = new File(paramString1 + ".orig");
/*      */ 
/* 1397 */         if (localFile1.renameTo(localFile3)) {
/* 1398 */           if (localFile2.renameTo(localFile1)) {
/* 1399 */             localFile3.delete();
/*      */           }
/*      */           else {
/* 1402 */             localObject2 = new MessageFormat(rb
/* 1402 */               .getString("attempt.to.rename.signedJarFile.to.jarFile.failed"));
/*      */ 
/* 1403 */             localObject3 = new Object[] { localFile2, localFile1 };
/* 1404 */             error(((MessageFormat)localObject2).format(localObject3));
/*      */           }
/*      */         }
/*      */         else {
/* 1408 */           localObject2 = new MessageFormat(rb
/* 1408 */             .getString("attempt.to.rename.jarFile.to.origJar.failed"));
/*      */ 
/* 1409 */           localObject3 = new Object[] { localFile1, localFile3 };
/* 1410 */           error(((MessageFormat)localObject2).format(localObject3));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1415 */     int n = 0;
/* 1416 */     if ((this.badKeyUsage) || (this.badExtendedKeyUsage) || (this.badNetscapeCertType) || (this.notYetValidCert) || (this.chainNotValidated) || (this.hasExpiredCert))
/*      */     {
/* 1418 */       if (this.strict) {
/* 1419 */         System.out.println(rb.getString("jar.signed.with.signer.errors."));
/* 1420 */         System.out.println();
/* 1421 */         System.out.println(rb.getString("Error."));
/*      */       } else {
/* 1423 */         System.out.println(rb.getString("jar.signed."));
/* 1424 */         System.out.println();
/* 1425 */         System.out.println(rb.getString("Warning."));
/* 1426 */         n = 1;
/*      */       }
/*      */ 
/* 1429 */       if (this.badKeyUsage) {
/* 1430 */         System.out.println(rb
/* 1431 */           .getString("The.signer.certificate.s.KeyUsage.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/* 1434 */       if (this.badExtendedKeyUsage) {
/* 1435 */         System.out.println(rb
/* 1436 */           .getString("The.signer.certificate.s.ExtendedKeyUsage.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/* 1439 */       if (this.badNetscapeCertType) {
/* 1440 */         System.out.println(rb
/* 1441 */           .getString("The.signer.certificate.s.NetscapeCertType.extension.doesn.t.allow.code.signing."));
/*      */       }
/*      */ 
/* 1444 */       if (this.hasExpiredCert) {
/* 1445 */         System.out.println(rb
/* 1446 */           .getString("The.signer.certificate.has.expired."));
/*      */       }
/* 1447 */       else if (this.notYetValidCert) {
/* 1448 */         System.out.println(rb
/* 1449 */           .getString("The.signer.certificate.is.not.yet.valid."));
/*      */       }
/*      */ 
/* 1452 */       if (this.chainNotValidated)
/* 1453 */         System.out.println(rb
/* 1454 */           .getString("The.signer.s.certificate.chain.is.not.validated."));
/*      */     }
/*      */     else
/*      */     {
/* 1457 */       System.out.println(rb.getString("jar.signed."));
/*      */     }
/* 1459 */     if ((this.hasExpiringCert) || (this.noTimestamp)) {
/* 1460 */       if (n == 0) {
/* 1461 */         System.out.println();
/* 1462 */         System.out.println(rb.getString("Warning."));
/*      */       }
/*      */ 
/* 1465 */       if (this.hasExpiringCert) {
/* 1466 */         System.out.println(rb
/* 1467 */           .getString("The.signer.certificate.will.expire.within.six.months."));
/*      */       }
/*      */ 
/* 1470 */       if (this.noTimestamp)
/* 1471 */         System.out.println(
/* 1472 */           String.format(rb
/* 1472 */           .getString("no.timestamp.signing"), 
/* 1472 */           new Object[] { this.expireDate }));
/*      */     }
/*      */   }
/*      */ 
/*      */   private int findHeaderEnd(byte[] paramArrayOfByte)
/*      */   {
/* 1491 */     int i = 1;
/* 1492 */     int j = paramArrayOfByte.length;
/* 1493 */     for (int k = 0; k < j; k++) {
/* 1494 */       switch (paramArrayOfByte[k]) {
/*      */       case 13:
/* 1496 */         if ((k < j - 1) && (paramArrayOfByte[(k + 1)] == 10)) k++;
/*      */ 
/*      */       case 10:
/* 1499 */         if (i != 0) return k + 1;
/* 1500 */         i = 1;
/* 1501 */         break;
/*      */       }
/* 1503 */       i = 0;
/*      */     }
/*      */ 
/* 1509 */     return j;
/*      */   }
/*      */ 
/*      */   private boolean signatureRelated(String paramString)
/*      */   {
/* 1522 */     return SignatureFileVerifier.isSigningRelated(paramString);
/*      */   }
/*      */ 
/*      */   private String signerInfo(CodeSigner paramCodeSigner, String paramString)
/*      */   {
/* 1531 */     if (this.cacheForSignerInfo.containsKey(paramCodeSigner)) {
/* 1532 */       return (String)this.cacheForSignerInfo.get(paramCodeSigner);
/*      */     }
/* 1534 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1535 */     List localList = paramCodeSigner.getSignerCertPath().getCertificates();
/*      */ 
/* 1538 */     Timestamp localTimestamp = paramCodeSigner.getTimestamp();
/*      */     Date localDate;
/* 1539 */     if (localTimestamp != null) {
/* 1540 */       localStringBuffer.append(printTimestamp(paramString, localTimestamp));
/* 1541 */       localStringBuffer.append('\n');
/* 1542 */       localDate = localTimestamp.getTimestamp();
/*      */     } else {
/* 1544 */       localDate = null;
/* 1545 */       this.noTimestamp = true;
/*      */     }
/*      */ 
/* 1549 */     boolean bool = true;
/* 1550 */     for (Certificate localCertificate : localList) {
/* 1551 */       localStringBuffer.append(printCert(paramString, localCertificate, true, localDate, bool));
/* 1552 */       localStringBuffer.append('\n');
/* 1553 */       bool = false;
/*      */     }
/*      */     try {
/* 1556 */       validateCertChain(localList);
/*      */     } catch (Exception localException) {
/* 1558 */       if (this.debug) {
/* 1559 */         localException.printStackTrace();
/*      */       }
/* 1561 */       if ((localException.getCause() == null) || (
/* 1562 */         (!(localException
/* 1562 */         .getCause() instanceof CertificateExpiredException)) && 
/* 1563 */         (!(localException
/* 1563 */         .getCause() instanceof CertificateNotYetValidException))))
/*      */       {
/* 1566 */         this.chainNotValidated = true;
/* 1567 */         localStringBuffer.append(paramString + rb.getString(".CertPath.not.validated.") + localException
/* 1568 */           .getLocalizedMessage() + "]\n");
/*      */       }
/*      */     }
/* 1571 */     String str = localStringBuffer.toString();
/* 1572 */     this.cacheForSignerInfo.put(paramCodeSigner, str);
/* 1573 */     return str;
/*      */   }
/*      */ 
/*      */   private void writeEntry(ZipFile paramZipFile, ZipOutputStream paramZipOutputStream, ZipEntry paramZipEntry)
/*      */     throws IOException
/*      */   {
/* 1579 */     ZipEntry localZipEntry = new ZipEntry(paramZipEntry.getName());
/* 1580 */     localZipEntry.setMethod(paramZipEntry.getMethod());
/* 1581 */     localZipEntry.setTime(paramZipEntry.getTime());
/* 1582 */     localZipEntry.setComment(paramZipEntry.getComment());
/* 1583 */     localZipEntry.setExtra(paramZipEntry.getExtra());
/* 1584 */     if (paramZipEntry.getMethod() == 0) {
/* 1585 */       localZipEntry.setSize(paramZipEntry.getSize());
/* 1586 */       localZipEntry.setCrc(paramZipEntry.getCrc());
/*      */     }
/* 1588 */     paramZipOutputStream.putNextEntry(localZipEntry);
/* 1589 */     writeBytes(paramZipFile, paramZipEntry, paramZipOutputStream);
/*      */   }
/*      */ 
/*      */   private synchronized void writeBytes(ZipFile paramZipFile, ZipEntry paramZipEntry, ZipOutputStream paramZipOutputStream)
/*      */     throws IOException
/*      */   {
/* 1599 */     InputStream localInputStream = null;
/*      */     try {
/* 1601 */       localInputStream = paramZipFile.getInputStream(paramZipEntry);
/* 1602 */       long l = paramZipEntry.getSize();
/*      */       int i;
/* 1604 */       while ((l > 0L) && ((i = localInputStream.read(this.buffer, 0, this.buffer.length)) != -1)) {
/* 1605 */         paramZipOutputStream.write(this.buffer, 0, i);
/* 1606 */         l -= i;
/*      */       }
/*      */     } finally {
/* 1609 */       if (localInputStream != null)
/* 1610 */         localInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   void loadKeyStore(String paramString, boolean paramBoolean)
/*      */   {
/* 1617 */     if ((!this.nullStream) && (paramString == null)) {
/* 1618 */       paramString = System.getProperty("user.home") + File.separator + ".keystore";
/*      */     }
/*      */ try {
/* 1624 */       this.certificateFactory = CertificateFactory.getInstance("X.509");
/* 1625 */       this.validator = CertPathValidator.getInstance("PKIX");
/* 1626 */       HashSet localHashSet = new HashSet();
/*      */       Object localObject3;
/*      */       try { KeyStore localKeyStore = KeyStoreUtil.getCacertsKeyStore();
/* 1629 */         if (localKeyStore != null) {
/* 1630 */           Enumeration localEnumeration = localKeyStore.aliases();
/* 1631 */           while (localEnumeration.hasMoreElements()) {
/* 1632 */             localObject3 = (String)localEnumeration.nextElement();
/*      */             try {
/* 1634 */               localHashSet.add(new TrustAnchor((X509Certificate)localKeyStore.getCertificate((String)localObject3), null));
/*      */             }
/*      */             catch (Exception localException3)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       } catch (Exception localException1)
/*      */       {
/*      */       }
/* 1644 */       if (this.providerName == null)
/* 1645 */         this.store = KeyStore.getInstance(this.storetype);
/*      */       else {
/* 1647 */         this.store = KeyStore.getInstance(this.storetype, this.providerName);
/*      */       }
/*      */ 
/* 1653 */       if ((this.token) && (this.storepass == null) && (!this.protectedPath) && 
/* 1654 */         (!KeyStoreUtil.isWindowsKeyStore(this.storetype)))
/*      */       {
/* 1655 */         this.storepass = 
/* 1656 */           getPass(rb
/* 1656 */           .getString("Enter.Passphrase.for.keystore."));
/*      */       }
/* 1657 */       else if ((!this.token) && (this.storepass == null) && (paramBoolean))
/* 1658 */         this.storepass = 
/* 1659 */           getPass(rb
/* 1659 */           .getString("Enter.Passphrase.for.keystore."));
/*      */       try
/*      */       {
/*      */         Object localObject2;
/* 1663 */         if (this.nullStream) {
/* 1664 */           this.store.load(null, this.storepass);
/*      */         } else {
/* 1666 */           paramString = paramString.replace(File.separatorChar, '/');
/* 1667 */           localObject1 = null;
/*      */           try {
/* 1669 */             localObject1 = new URL(paramString);
/*      */           }
/*      */           catch (MalformedURLException localMalformedURLException) {
/* 1672 */             localObject1 = new File(paramString).toURI().toURL();
/*      */           }
/* 1674 */           localObject2 = null;
/*      */           try {
/* 1676 */             localObject2 = ((URL)localObject1).openStream();
/* 1677 */             this.store.load((InputStream)localObject2, this.storepass);
/*      */           } finally {
/* 1679 */             if (localObject2 != null) {
/* 1680 */               ((InputStream)localObject2).close();
/*      */             }
/*      */           }
/*      */         }
/* 1684 */         Object localObject1 = this.store.aliases();
/* 1685 */         while (((Enumeration)localObject1).hasMoreElements()) {
/* 1686 */           localObject2 = (String)((Enumeration)localObject1).nextElement();
/*      */           try {
/* 1688 */             localObject3 = (X509Certificate)this.store.getCertificate((String)localObject2);
/*      */ 
/* 1691 */             if ((this.store.isCertificateEntry((String)localObject2)) || 
/* 1692 */               (((X509Certificate)localObject3)
/* 1692 */               .getSubjectDN().equals(((X509Certificate)localObject3).getIssuerDN())))
/* 1693 */               localHashSet.add(new TrustAnchor((X509Certificate)localObject3, null));
/*      */           }
/*      */           catch (Exception localException2) {
/*      */           }
/*      */         }
/*      */       }
/*      */       finally {
/*      */         try {
/* 1701 */           this.pkixParameters = new PKIXParameters(localHashSet);
/* 1702 */           this.pkixParameters.setRevocationEnabled(false);
/*      */         }
/*      */         catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException2) {
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1709 */       throw new RuntimeException(rb.getString("keystore.load.") + localIOException
/* 1709 */         .getMessage());
/*      */     }
/*      */     catch (CertificateException localCertificateException) {
/* 1712 */       throw new RuntimeException(rb.getString("certificate.exception.") + localCertificateException
/* 1712 */         .getMessage());
/*      */     }
/*      */     catch (NoSuchProviderException localNoSuchProviderException) {
/* 1715 */       throw new RuntimeException(rb.getString("keystore.load.") + localNoSuchProviderException
/* 1715 */         .getMessage());
/*      */     }
/*      */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1718 */       throw new RuntimeException(rb.getString("keystore.load.") + localNoSuchAlgorithmException
/* 1718 */         .getMessage());
/*      */     }
/*      */     catch (KeyStoreException localKeyStoreException)
/*      */     {
/* 1722 */       throw new RuntimeException(rb
/* 1721 */         .getString("unable.to.instantiate.keystore.class.") + 
/* 1721 */         localKeyStoreException
/* 1722 */         .getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   X509Certificate getTsaCert(String paramString)
/*      */   {
/* 1728 */     Certificate localCertificate = null;
/*      */     try
/*      */     {
/* 1731 */       localCertificate = this.store.getCertificate(paramString);
/*      */     }
/*      */     catch (KeyStoreException localKeyStoreException) {
/*      */     }
/* 1735 */     if ((localCertificate == null) || (!(localCertificate instanceof X509Certificate)))
/*      */     {
/* 1737 */       MessageFormat localMessageFormat = new MessageFormat(rb
/* 1737 */         .getString("Certificate.not.found.for.alias.alias.must.reference.a.valid.KeyStore.entry.containing.an.X.509.public.key.certificate.for.the"));
/*      */ 
/* 1738 */       Object[] arrayOfObject = { paramString, paramString };
/* 1739 */       error(localMessageFormat.format(arrayOfObject));
/*      */     }
/* 1741 */     return (X509Certificate)localCertificate;
/*      */   }
/*      */ 
/*      */   void checkCertUsage(X509Certificate paramX509Certificate, boolean[] paramArrayOfBoolean)
/*      */   {
/* 1761 */     if (paramArrayOfBoolean != null)
/*      */     {
/*      */       int tmp13_12 = (paramArrayOfBoolean[2] = 0); paramArrayOfBoolean[1] = tmp13_12; paramArrayOfBoolean[0] = tmp13_12;
/*      */     }
/*      */ 
/* 1765 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 1766 */     if (arrayOfBoolean != null) {
/* 1767 */       arrayOfBoolean = Arrays.copyOf(arrayOfBoolean, 9);
/* 1768 */       if ((arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] == 0) && 
/* 1769 */         (paramArrayOfBoolean != null)) {
/* 1770 */         paramArrayOfBoolean[0] = true;
/* 1771 */         this.badKeyUsage = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1777 */       List localList = paramX509Certificate.getExtendedKeyUsage();
/* 1778 */       if ((localList != null) && 
/* 1779 */         (!localList.contains("2.5.29.37.0")) && 
/* 1780 */         (!localList
/* 1780 */         .contains("1.3.6.1.5.5.7.3.3")) && 
/* 1781 */         (paramArrayOfBoolean != null)) {
/* 1782 */         paramArrayOfBoolean[1] = true;
/* 1783 */         this.badExtendedKeyUsage = true;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (CertificateParsingException localCertificateParsingException)
/*      */     {
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1794 */       byte[] arrayOfByte1 = paramX509Certificate
/* 1794 */         .getExtensionValue("2.16.840.1.113730.1.1");
/*      */ 
/* 1795 */       if (arrayOfByte1 != null) {
/* 1796 */         DerInputStream localDerInputStream = new DerInputStream(arrayOfByte1);
/* 1797 */         byte[] arrayOfByte2 = localDerInputStream.getOctetString();
/*      */ 
/* 1799 */         arrayOfByte2 = new DerValue(arrayOfByte2).getUnalignedBitString()
/* 1799 */           .toByteArray();
/*      */ 
/* 1801 */         NetscapeCertTypeExtension localNetscapeCertTypeExtension = new NetscapeCertTypeExtension(arrayOfByte2);
/*      */ 
/* 1804 */         Boolean localBoolean = localNetscapeCertTypeExtension.get("object_signing");
/* 1805 */         if ((!localBoolean.booleanValue()) && 
/* 1806 */           (paramArrayOfBoolean != null)) {
/* 1807 */           paramArrayOfBoolean[2] = true;
/* 1808 */           this.badNetscapeCertType = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   void getAliasInfo(String paramString)
/*      */   {
/* 1819 */     Key localKey = null;
/*      */     try
/*      */     {
/* 1822 */       Certificate[] arrayOfCertificate = null;
/*      */       Object localObject1;
/* 1823 */       if (this.altCertChain != null) try {
/* 1824 */           FileInputStream localFileInputStream = new FileInputStream(this.altCertChain); localObject1 = null;
/*      */           try
/*      */           {
/* 1827 */             arrayOfCertificate = (Certificate[])CertificateFactory.getInstance("X.509")
/* 1826 */               .generateCertificates(localFileInputStream)
/* 1827 */               .toArray(new Certificate[0]);
/*      */           }
/*      */           catch (Throwable localThrowable2)
/*      */           {
/* 1824 */             localObject1 = localThrowable2; throw localThrowable2;
/*      */           }
/*      */           finally
/*      */           {
/* 1828 */             if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable3) { ((Throwable)localObject1).addSuppressed(localThrowable3); } else localFileInputStream.close();  
/*      */           } } catch (FileNotFoundException localFileNotFoundException) { error(rb.getString("File.specified.by.certchain.does.not.exist"));
/*      */         } catch (CertificateException|IOException localCertificateException) {
/* 1831 */           error(rb.getString("Cannot.restore.certchain.from.file.specified"));
/*      */         } else
/*      */         try
/*      */         {
/* 1835 */           arrayOfCertificate = this.store.getCertificateChain(paramString);
/*      */         }
/*      */         catch (KeyStoreException localKeyStoreException2)
/*      */         {
/*      */         }
/* 1840 */       if ((arrayOfCertificate == null) || (arrayOfCertificate.length == 0)) {
/* 1841 */         if (this.altCertChain != null) {
/* 1842 */           error(rb
/* 1843 */             .getString("Certificate.chain.not.found.in.the.file.specified."));
/*      */         }
/*      */         else
/*      */         {
/* 1846 */           MessageFormat localMessageFormat2 = new MessageFormat(rb
/* 1846 */             .getString("Certificate.chain.not.found.for.alias.alias.must.reference.a.valid.KeyStore.key.entry.containing.a.private.key.and"));
/*      */ 
/* 1847 */           localObject1 = new Object[] { paramString, paramString };
/* 1848 */           error(localMessageFormat2.format(localObject1));
/*      */         }
/*      */       }
/*      */ 
/* 1852 */       this.certChain = new X509Certificate[arrayOfCertificate.length];
/* 1853 */       for (int i = 0; i < arrayOfCertificate.length; i++) {
/* 1854 */         if (!(arrayOfCertificate[i] instanceof X509Certificate)) {
/* 1855 */           error(rb
/* 1856 */             .getString("found.non.X.509.certificate.in.signer.s.chain"));
/*      */         }
/*      */ 
/* 1858 */         this.certChain[i] = ((X509Certificate)arrayOfCertificate[i]);
/*      */       }
/*      */ 
/* 1863 */       printCert("", this.certChain[0], true, null, true);
/*      */       try
/*      */       {
/* 1866 */         validateCertChain(Arrays.asList(this.certChain));
/*      */       } catch (Exception localException) {
/* 1868 */         if (this.debug) {
/* 1869 */           localException.printStackTrace();
/*      */         }
/* 1871 */         if ((localException.getCause() == null) || (
/* 1872 */           (!(localException
/* 1872 */           .getCause() instanceof CertificateExpiredException)) && 
/* 1873 */           (!(localException
/* 1873 */           .getCause() instanceof CertificateNotYetValidException))))
/*      */         {
/* 1876 */           this.chainNotValidated = true;
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1881 */         if ((!this.token) && (this.keypass == null))
/* 1882 */           localKey = this.store.getKey(paramString, this.storepass);
/*      */         else
/* 1884 */           localKey = this.store.getKey(paramString, this.keypass);
/*      */       } catch (UnrecoverableKeyException localUnrecoverableKeyException2) {
/* 1886 */         if (this.token)
/* 1887 */           throw localUnrecoverableKeyException2;
/* 1888 */         if (this.keypass == null)
/*      */         {
/* 1891 */           localObject1 = new MessageFormat(rb
/* 1891 */             .getString("Enter.key.password.for.alias."));
/*      */ 
/* 1892 */           Object[] arrayOfObject2 = { paramString };
/* 1893 */           this.keypass = getPass(((MessageFormat)localObject1).format(arrayOfObject2));
/* 1894 */           localKey = this.store.getKey(paramString, this.keypass);
/*      */         }
/*      */       }
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1898 */       error(localNoSuchAlgorithmException.getMessage());
/*      */     } catch (UnrecoverableKeyException localUnrecoverableKeyException1) {
/* 1900 */       error(rb.getString("unable.to.recover.key.from.keystore"));
/*      */     }
/*      */     catch (KeyStoreException localKeyStoreException1)
/*      */     {
/*      */     }
/* 1905 */     if (!(localKey instanceof PrivateKey))
/*      */     {
/* 1907 */       MessageFormat localMessageFormat1 = new MessageFormat(rb
/* 1907 */         .getString("key.associated.with.alias.not.a.private.key"));
/*      */ 
/* 1908 */       Object[] arrayOfObject1 = { paramString };
/* 1909 */       error(localMessageFormat1.format(arrayOfObject1));
/*      */     } else {
/* 1911 */       this.privateKey = ((PrivateKey)localKey);
/*      */     }
/*      */   }
/*      */ 
/*      */   void error(String paramString)
/*      */   {
/* 1917 */     System.out.println(rb.getString("jarsigner.") + paramString);
/* 1918 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   void error(String paramString, Exception paramException)
/*      */   {
/* 1924 */     System.out.println(rb.getString("jarsigner.") + paramString);
/* 1925 */     if (this.debug) {
/* 1926 */       paramException.printStackTrace();
/*      */     }
/* 1928 */     System.exit(1);
/*      */   }
/*      */ 
/*      */   void validateCertChain(List<? extends Certificate> paramList)
/*      */     throws Exception
/*      */   {
/*      */     Object localObject;
/* 1932 */     for (int i = 0; 
/* 1933 */       i < paramList.size(); i++) {
/* 1934 */       for (localObject = this.pkixParameters.getTrustAnchors().iterator(); ((Iterator)localObject).hasNext(); ) { TrustAnchor localTrustAnchor = (TrustAnchor)((Iterator)localObject).next();
/* 1935 */         if (localTrustAnchor.getTrustedCert().equals(paramList.get(i))) {
/*      */           break label75;
/*      */         }
/*      */       }
/*      */     }
/* 1940 */     label75: if (i > 0) {
/* 1941 */       localObject = this.certificateFactory.generateCertPath(i == paramList
/* 1942 */         .size() ? paramList : paramList.subList(0, i));
/* 1943 */       this.validator.validate((CertPath)localObject, this.pkixParameters);
/*      */     }
/*      */   }
/*      */ 
/*      */   char[] getPass(String paramString)
/*      */   {
/* 1949 */     System.err.print(paramString);
/* 1950 */     System.err.flush();
/*      */     try {
/* 1952 */       char[] arrayOfChar = Password.readPassword(System.in);
/*      */ 
/* 1954 */       if (arrayOfChar == null)
/* 1955 */         error(rb.getString("you.must.enter.key.password"));
/*      */       else
/* 1957 */         return arrayOfChar;
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1960 */       error(rb.getString("unable.to.read.password.") + localIOException.getMessage());
/*      */     }
/*      */ 
/* 1963 */     return null;
/*      */   }
/*      */ 
/*      */   private synchronized byte[] getBytes(ZipFile paramZipFile, ZipEntry paramZipEntry)
/*      */     throws IOException
/*      */   {
/* 1973 */     InputStream localInputStream = null;
/*      */     try {
/* 1975 */       localInputStream = paramZipFile.getInputStream(paramZipEntry);
/* 1976 */       this.baos.reset();
/* 1977 */       long l = paramZipEntry.getSize();
/*      */       int i;
/* 1979 */       while ((l > 0L) && ((i = localInputStream.read(this.buffer, 0, this.buffer.length)) != -1)) {
/* 1980 */         this.baos.write(this.buffer, 0, i);
/* 1981 */         l -= i;
/*      */       }
/*      */     } finally {
/* 1984 */       if (localInputStream != null) {
/* 1985 */         localInputStream.close();
/*      */       }
/*      */     }
/*      */ 
/* 1989 */     return this.baos.toByteArray();
/*      */   }
/*      */ 
/*      */   private ZipEntry getManifestFile(ZipFile paramZipFile)
/*      */   {
/* 1997 */     ZipEntry localZipEntry = paramZipFile.getEntry("META-INF/MANIFEST.MF");
/* 1998 */     if (localZipEntry == null)
/*      */     {
/* 2000 */       Enumeration localEnumeration = paramZipFile.entries();
/* 2001 */       while ((localEnumeration.hasMoreElements()) && (localZipEntry == null)) {
/* 2002 */         localZipEntry = (ZipEntry)localEnumeration.nextElement();
/*      */ 
/* 2004 */         if (!"META-INF/MANIFEST.MF"
/* 2004 */           .equalsIgnoreCase(localZipEntry
/* 2004 */           .getName())) {
/* 2005 */           localZipEntry = null;
/*      */         }
/*      */       }
/*      */     }
/* 2009 */     return localZipEntry;
/*      */   }
/*      */ 
/*      */   private synchronized String[] getDigests(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest)
/*      */     throws IOException
/*      */   {
/* 2021 */     InputStream localInputStream = null;
/*      */     try {
/* 2023 */       localInputStream = paramZipFile.getInputStream(paramZipEntry);
/* 2024 */       long l = paramZipEntry.getSize();
/*      */       int i;
/* 2025 */       while ((l > 0L) && 
/* 2026 */         ((i = localInputStream
/* 2026 */         .read(this.buffer, 0, this.buffer.length)) != 
/* 2026 */         -1)) {
/* 2027 */         for (j = 0; j < paramArrayOfMessageDigest.length; j++) {
/* 2028 */           paramArrayOfMessageDigest[j].update(this.buffer, 0, i);
/*      */         }
/* 2030 */         l -= i;
/*      */       }
/*      */     } finally {
/* 2033 */       if (localInputStream != null) {
/* 2034 */         localInputStream.close();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2039 */     String[] arrayOfString = new String[paramArrayOfMessageDigest.length];
/* 2040 */     for (int j = 0; j < paramArrayOfMessageDigest.length; j++) {
/* 2041 */       arrayOfString[j] = Base64.getEncoder().encodeToString(paramArrayOfMessageDigest[j].digest());
/*      */     }
/* 2043 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private Attributes getDigestAttributes(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest)
/*      */     throws IOException
/*      */   {
/* 2054 */     String[] arrayOfString = getDigests(paramZipEntry, paramZipFile, paramArrayOfMessageDigest);
/* 2055 */     Attributes localAttributes = new Attributes();
/*      */ 
/* 2057 */     for (int i = 0; i < paramArrayOfMessageDigest.length; i++) {
/* 2058 */       localAttributes.putValue(paramArrayOfMessageDigest[i].getAlgorithm() + "-Digest", arrayOfString[i]);
/*      */     }
/*      */ 
/* 2061 */     return localAttributes;
/*      */   }
/*      */ 
/*      */   private boolean updateDigests(ZipEntry paramZipEntry, ZipFile paramZipFile, MessageDigest[] paramArrayOfMessageDigest, Manifest paramManifest)
/*      */     throws IOException
/*      */   {
/* 2077 */     boolean bool = false;
/*      */ 
/* 2079 */     Attributes localAttributes = paramManifest.getAttributes(paramZipEntry.getName());
/* 2080 */     String[] arrayOfString = getDigests(paramZipEntry, paramZipFile, paramArrayOfMessageDigest);
/*      */ 
/* 2082 */     for (int i = 0; i < paramArrayOfMessageDigest.length; i++) { Object localObject1 = null;
/*      */       AlgorithmId localAlgorithmId;
/*      */       Iterator localIterator;
/*      */       try { localAlgorithmId = AlgorithmId.get(paramArrayOfMessageDigest[i].getAlgorithm());
/* 2088 */         for (localIterator = localAttributes.keySet().iterator(); localIterator.hasNext(); ) { Object localObject2 = localIterator.next();
/* 2089 */           if ((localObject2 instanceof Attributes.Name)) {
/* 2090 */             String str2 = ((Attributes.Name)localObject2).toString();
/* 2091 */             if (str2.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST")) {
/* 2092 */               String str3 = str2.substring(0, str2.length() - 7);
/* 2093 */               if (AlgorithmId.get(str3).equals(localAlgorithmId)) {
/* 2094 */                 localObject1 = str2;
/* 2095 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*      */       {
/*      */       }
/* 2104 */       if (localObject1 == null) {
/* 2105 */         localObject1 = paramArrayOfMessageDigest[i].getAlgorithm() + "-Digest";
/* 2106 */         localAttributes.putValue((String)localObject1, arrayOfString[i]);
/* 2107 */         bool = true;
/*      */       }
/*      */       else
/*      */       {
/* 2111 */         String str1 = localAttributes.getValue((String)localObject1);
/* 2112 */         if (!str1.equalsIgnoreCase(arrayOfString[i])) {
/* 2113 */           localAttributes.putValue((String)localObject1, arrayOfString[i]);
/* 2114 */           bool = true;
/*      */         }
/*      */       }
/*      */     }
/* 2118 */     return bool;
/*      */   }
/*      */ 
/*      */   private ContentSigner loadSigningMechanism(String paramString1, String paramString2)
/*      */     throws Exception
/*      */   {
/* 2129 */     String str = null;
/*      */ 
/* 2132 */     str = PathList.appendPath(System.getProperty("env.class.path"), str);
/* 2133 */     str = PathList.appendPath(System.getProperty("java.class.path"), str);
/* 2134 */     str = PathList.appendPath(paramString2, str);
/* 2135 */     URL[] arrayOfURL = PathList.pathToURLs(str);
/* 2136 */     URLClassLoader localURLClassLoader = new URLClassLoader(arrayOfURL);
/*      */ 
/* 2139 */     Class localClass = localURLClassLoader.loadClass(paramString1);
/*      */ 
/* 2142 */     Object localObject = localClass.newInstance();
/* 2143 */     if (!(localObject instanceof ContentSigner))
/*      */     {
/* 2145 */       MessageFormat localMessageFormat = new MessageFormat(rb
/* 2145 */         .getString("signerClass.is.not.a.signing.mechanism"));
/*      */ 
/* 2146 */       Object[] arrayOfObject = { localClass.getName() };
/* 2147 */       throw new IllegalArgumentException(localMessageFormat.format(arrayOfObject));
/*      */     }
/* 2149 */     return (ContentSigner)localObject;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   88 */     collator.setStrength(0);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.security.tools.jarsigner.Main
 * JD-Core Version:    0.6.2
 */