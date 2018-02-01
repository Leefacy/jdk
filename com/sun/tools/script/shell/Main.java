/*     */ package com.sun.tools.script.shell;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   private static final int EXIT_SUCCESS = 0;
/*     */   private static final int EXIT_CMD_NO_CLASSPATH = 1;
/*     */   private static final int EXIT_CMD_NO_FILE = 2;
/*     */   private static final int EXIT_CMD_NO_SCRIPT = 3;
/*     */   private static final int EXIT_CMD_NO_LANG = 4;
/*     */   private static final int EXIT_CMD_NO_ENCODING = 5;
/*     */   private static final int EXIT_CMD_NO_PROPNAME = 6;
/*     */   private static final int EXIT_UNKNOWN_OPTION = 7;
/*     */   private static final int EXIT_ENGINE_NOT_FOUND = 8;
/*     */   private static final int EXIT_NO_ENCODING_FOUND = 9;
/*     */   private static final int EXIT_SCRIPT_ERROR = 10;
/*     */   private static final int EXIT_FILE_NOT_FOUND = 11;
/*     */   private static final int EXIT_MULTIPLE_STDIN = 12;
/*     */   private static final String DEFAULT_LANGUAGE = "js";
/* 583 */   private static List<Command> scripts = new ArrayList();
/*     */   private static ScriptEngineManager engineManager;
/* 584 */   private static Map<String, ScriptEngine> engines = new HashMap();
/* 585 */   private static ResourceBundle msgRes = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
/*     */ 
/* 579 */   private static String BUNDLE_NAME = "com.sun.tools.script.shell.messages";
/* 580 */   private static String PROGRAM_NAME = "jrunscript";
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  44 */     String[] arrayOfString = processOptions(paramArrayOfString);
/*     */ 
/*  47 */     for (Command localCommand : scripts) {
/*  48 */       localCommand.run(arrayOfString);
/*     */     }
/*     */ 
/*  51 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   private static String[] processOptions(String[] paramArrayOfString)
/*     */   {
/*  66 */     String str1 = "js";
/*     */ 
/*  68 */     String str2 = null;
/*     */ 
/*  71 */     checkClassPath(paramArrayOfString);
/*     */ 
/*  74 */     int i = 0;
/*     */ 
/*  76 */     int j = 0;
/*  77 */     for (int k = 0; k < paramArrayOfString.length; k++) {
/*  78 */       String str3 = paramArrayOfString[k];
/*  79 */       if ((str3.equals("-classpath")) || 
/*  80 */         (str3
/*  80 */         .equals("-cp")))
/*     */       {
/*  82 */         k++;
/*     */       }
/*     */       else
/*     */       {
/*     */         int n;
/*  87 */         if (!str3.startsWith("-"))
/*     */         {
/*     */           int m;
/*  90 */           if (i != 0)
/*     */           {
/*  93 */             m = paramArrayOfString.length - k;
/*  94 */             n = k;
/*     */           }
/*     */           else
/*     */           {
/*  99 */             m = paramArrayOfString.length - k - 1;
/* 100 */             n = k + 1;
/* 101 */             localObject2 = getScriptEngine(str1);
/* 102 */             addFileSource((ScriptEngine)localObject2, paramArrayOfString[k], str2);
/*     */           }
/*     */ 
/* 105 */           Object localObject2 = new String[m];
/* 106 */           System.arraycopy(paramArrayOfString, n, localObject2, 0, m);
/* 107 */           return localObject2;
/*     */         }
/*     */         Object localObject1;
/* 110 */         if (str3.startsWith("-D")) {
/* 111 */           localObject1 = str3.substring(2);
/* 112 */           n = ((String)localObject1).indexOf('=');
/* 113 */           if (n != -1) {
/* 114 */             System.setProperty(((String)localObject1).substring(0, n), ((String)localObject1)
/* 115 */               .substring(n + 1));
/*     */           }
/* 117 */           else if (!((String)localObject1).equals("")) {
/* 118 */             System.setProperty((String)localObject1, "");
/*     */           }
/*     */           else
/* 121 */             usage(6);
/*     */         }
/*     */         else
/*     */         {
/* 125 */           if ((str3.equals("-?")) || (str3.equals("-help"))) {
/* 126 */             usage(0); } else {
/* 127 */             if (str3.equals("-e")) {
/* 128 */               i = 1;
/* 129 */               k++; if (k == paramArrayOfString.length) {
/* 130 */                 usage(3);
/*     */               }
/* 132 */               localObject1 = getScriptEngine(str1);
/* 133 */               addStringSource((ScriptEngine)localObject1, paramArrayOfString[k]);
/* 134 */               continue;
/* 135 */             }if (str3.equals("-encoding")) {
/* 136 */               k++; if (k == paramArrayOfString.length)
/* 137 */                 usage(5);
/* 138 */               str2 = paramArrayOfString[k];
/* 139 */               continue;
/* 140 */             }if (str3.equals("-f")) {
/* 141 */               i = 1;
/* 142 */               k++; if (k == paramArrayOfString.length)
/* 143 */                 usage(2);
/* 144 */               localObject1 = getScriptEngine(str1);
/* 145 */               if (paramArrayOfString[k].equals("-")) {
/* 146 */                 if (j != 0)
/* 147 */                   usage(12);
/*     */                 else {
/* 149 */                   j = 1;
/*     */                 }
/* 151 */                 addInteractiveMode((ScriptEngine)localObject1); continue;
/*     */               }
/* 153 */               addFileSource((ScriptEngine)localObject1, paramArrayOfString[k], str2);
/*     */ 
/* 155 */               continue;
/* 156 */             }if (str3.equals("-l")) {
/* 157 */               k++; if (k == paramArrayOfString.length)
/* 158 */                 usage(4);
/* 159 */               str1 = paramArrayOfString[k];
/* 160 */               continue;
/* 161 */             }if (str3.equals("-q")) {
/* 162 */               listScriptEngines();
/*     */             }
/*     */           }
/* 165 */           usage(7);
/*     */         }
/*     */       }
/*     */     }
/* 168 */     if (i == 0) {
/* 169 */       ScriptEngine localScriptEngine = getScriptEngine(str1);
/* 170 */       addInteractiveMode(localScriptEngine);
/*     */     }
/* 172 */     return new String[0];
/*     */   }
/*     */ 
/*     */   private static void addInteractiveMode(ScriptEngine paramScriptEngine)
/*     */   {
/* 180 */     scripts.add(new Command() {
/*     */       public void run(String[] paramAnonymousArrayOfString) {
/* 182 */         Main.setScriptArguments(this.val$se, paramAnonymousArrayOfString);
/* 183 */         Main.processSource(this.val$se, "-", null);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static void addFileSource(ScriptEngine paramScriptEngine, final String paramString1, final String paramString2)
/*     */   {
/* 197 */     scripts.add(new Command() {
/*     */       public void run(String[] paramAnonymousArrayOfString) {
/* 199 */         Main.setScriptArguments(this.val$se, paramAnonymousArrayOfString);
/* 200 */         Main.processSource(this.val$se, paramString1, paramString2);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static void addStringSource(ScriptEngine paramScriptEngine, final String paramString)
/*     */   {
/* 212 */     scripts.add(new Command() {
/*     */       public void run(String[] paramAnonymousArrayOfString) {
/* 214 */         Main.setScriptArguments(this.val$se, paramAnonymousArrayOfString);
/* 215 */         String str = Main.setScriptFilename(this.val$se, "<string>");
/*     */         try {
/* 217 */           Main.evaluateString(this.val$se, paramString);
/*     */ 
/* 219 */           Main.setScriptFilename(this.val$se, str); } finally { Main.setScriptFilename(this.val$se, str); }
/*     */ 
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static void listScriptEngines()
/*     */   {
/* 229 */     List localList = engineManager.getEngineFactories();
/* 230 */     for (ScriptEngineFactory localScriptEngineFactory : localList) {
/* 231 */       getError().println(getMessage("engine.info", new Object[] { localScriptEngineFactory
/* 232 */         .getLanguageName(), localScriptEngineFactory
/* 233 */         .getLanguageVersion(), localScriptEngineFactory
/* 234 */         .getEngineName(), localScriptEngineFactory
/* 235 */         .getEngineVersion() }));
/*     */     }
/*     */ 
/* 238 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   private static void processSource(ScriptEngine paramScriptEngine, String paramString1, String paramString2)
/*     */   {
/*     */     Object localObject1;
/* 249 */     if (paramString1.equals("-"))
/*     */     {
/* 251 */       localObject1 = new BufferedReader(new InputStreamReader(
/* 251 */         getIn()));
/* 252 */       int i = 0;
/* 253 */       String str1 = getPrompt(paramScriptEngine);
/* 254 */       paramScriptEngine.put("javax.script.filename", "<STDIN>");
/* 255 */       while (i == 0) {
/* 256 */         getError().print(str1);
/* 257 */         String str2 = "";
/*     */         try {
/* 259 */           str2 = ((BufferedReader)localObject1).readLine();
/*     */         } catch (IOException localIOException) {
/* 261 */           getError().println(localIOException.toString());
/*     */         }
/* 263 */         if (str2 == null) {
/* 264 */           i = 1;
/* 265 */           break;
/*     */         }
/* 267 */         Object localObject2 = evaluateString(paramScriptEngine, str2, false);
/* 268 */         if (localObject2 != null) {
/* 269 */           localObject2 = localObject2.toString();
/* 270 */           if (localObject2 == null) {
/* 271 */             localObject2 = "null";
/*     */           }
/* 273 */           getError().println(localObject2);
/*     */         }
/*     */       }
/*     */     } else {
/* 277 */       localObject1 = null;
/*     */       try {
/* 279 */         localObject1 = new FileInputStream(paramString1);
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/* 281 */         getError().println(getMessage("file.not.found", new Object[] { paramString1 }));
/*     */ 
/* 283 */         System.exit(11);
/*     */       }
/* 285 */       evaluateStream(paramScriptEngine, (InputStream)localObject1, paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object evaluateString(ScriptEngine paramScriptEngine, String paramString, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 298 */       return paramScriptEngine.eval(paramString);
/*     */     } catch (ScriptException localScriptException) {
/* 300 */       getError().println(getMessage("string.script.error", new Object[] { localScriptException
/* 301 */         .getMessage() }));
/* 302 */       if (paramBoolean)
/* 303 */         System.exit(10);
/*     */     } catch (Exception localException) {
/* 305 */       localException.printStackTrace(getError());
/* 306 */       if (paramBoolean) {
/* 307 */         System.exit(10);
/*     */       }
/*     */     }
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */   private static void evaluateString(ScriptEngine paramScriptEngine, String paramString)
/*     */   {
/* 319 */     evaluateString(paramScriptEngine, paramString, true);
/*     */   }
/*     */ 
/*     */   private static Object evaluateReader(ScriptEngine paramScriptEngine, Reader paramReader, String paramString)
/*     */   {
/* 330 */     String str = setScriptFilename(paramScriptEngine, paramString);
/*     */     try {
/* 332 */       return paramScriptEngine.eval(paramReader);
/*     */     } catch (ScriptException localScriptException) {
/* 334 */       getError().println(getMessage("file.script.error", new Object[] { paramString, localScriptException
/* 335 */         .getMessage() }));
/* 336 */       System.exit(10);
/*     */     } catch (Exception localException) {
/* 338 */       localException.printStackTrace(getError());
/* 339 */       System.exit(10);
/*     */     } finally {
/* 341 */       setScriptFilename(paramScriptEngine, str);
/*     */     }
/* 343 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object evaluateStream(ScriptEngine paramScriptEngine, InputStream paramInputStream, String paramString1, String paramString2)
/*     */   {
/* 355 */     BufferedReader localBufferedReader = null;
/* 356 */     if (paramString2 != null)
/*     */       try {
/* 358 */         localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, paramString2));
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 361 */         getError().println(getMessage("encoding.unsupported", new Object[] { paramString2 }));
/*     */ 
/* 363 */         System.exit(9);
/*     */       }
/*     */     else {
/* 366 */       localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     }
/* 368 */     return evaluateReader(paramScriptEngine, localBufferedReader, paramString1);
/*     */   }
/*     */ 
/*     */   private static void usage(int paramInt)
/*     */   {
/* 376 */     getError().println(getMessage("main.usage", new Object[] { PROGRAM_NAME }));
/*     */ 
/* 378 */     System.exit(paramInt);
/*     */   }
/*     */ 
/*     */   private static String getPrompt(ScriptEngine paramScriptEngine)
/*     */   {
/* 386 */     List localList = paramScriptEngine.getFactory().getNames();
/* 387 */     return (String)localList.get(0) + "> ";
/*     */   }
/*     */ 
/*     */   private static String getMessage(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 394 */     return MessageFormat.format(msgRes.getString(paramString), paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private static InputStream getIn()
/*     */   {
/* 399 */     return System.in;
/*     */   }
/*     */ 
/*     */   private static PrintStream getError()
/*     */   {
/* 404 */     return System.err;
/*     */   }
/*     */ 
/*     */   private static ScriptEngine getScriptEngine(String paramString)
/*     */   {
/* 409 */     ScriptEngine localScriptEngine = (ScriptEngine)engines.get(paramString);
/* 410 */     if (localScriptEngine == null) {
/* 411 */       localScriptEngine = engineManager.getEngineByName(paramString);
/* 412 */       if (localScriptEngine == null) {
/* 413 */         getError().println(getMessage("engine.not.found", new Object[] { paramString }));
/*     */ 
/* 415 */         System.exit(8);
/*     */       }
/*     */ 
/* 419 */       initScriptEngine(localScriptEngine);
/*     */ 
/* 421 */       engines.put(paramString, localScriptEngine);
/*     */     }
/* 423 */     return localScriptEngine;
/*     */   }
/*     */ 
/*     */   private static void initScriptEngine(ScriptEngine paramScriptEngine)
/*     */   {
/* 429 */     paramScriptEngine.put("engine", paramScriptEngine);
/*     */ 
/* 432 */     List localList = paramScriptEngine.getFactory().getExtensions();
/* 433 */     InputStream localInputStream = null;
/* 434 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 435 */     for (String str : localList) {
/* 436 */       localInputStream = localClassLoader.getResourceAsStream("com/sun/tools/script/shell/init." + str);
/*     */ 
/* 438 */       if (localInputStream != null) break;
/*     */     }
/* 440 */     if (localInputStream != null)
/* 441 */       evaluateStream(paramScriptEngine, localInputStream, "<system-init>", null);
/*     */   }
/*     */ 
/*     */   private static void checkClassPath(String[] paramArrayOfString)
/*     */   {
/* 452 */     String str = null;
/* 453 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 454 */       if ((paramArrayOfString[i].equals("-classpath")) || 
/* 455 */         (paramArrayOfString[i]
/* 455 */         .equals("-cp")))
/*     */       {
/* 456 */         i++; if (i == paramArrayOfString.length)
/*     */         {
/* 458 */           usage(1);
/*     */         }
/* 460 */         else str = paramArrayOfString[i];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 465 */     if (str != null)
/*     */     {
/* 477 */       ClassLoader localClassLoader = Main.class.getClassLoader();
/* 478 */       URL[] arrayOfURL = pathToURLs(str);
/* 479 */       URLClassLoader localURLClassLoader = new URLClassLoader(arrayOfURL, localClassLoader);
/* 480 */       Thread.currentThread().setContextClassLoader(localURLClassLoader);
/*     */     }
/*     */ 
/* 486 */     engineManager = new ScriptEngineManager();
/*     */   }
/*     */ 
/*     */   private static URL[] pathToURLs(String paramString)
/*     */   {
/* 497 */     String[] arrayOfString = paramString.split(File.pathSeparator);
/* 498 */     Object localObject1 = new URL[arrayOfString.length];
/* 499 */     int i = 0;
/*     */     Object localObject2;
/* 500 */     while (i < arrayOfString.length) {
/* 501 */       localObject2 = fileToURL(new File(arrayOfString[i]));
/* 502 */       if (localObject2 != null) {
/* 503 */         localObject1[(i++)] = localObject2;
/*     */       }
/*     */     }
/* 506 */     if (localObject1.length != i) {
/* 507 */       localObject2 = new URL[i];
/* 508 */       System.arraycopy(localObject1, 0, localObject2, 0, i);
/* 509 */       localObject1 = localObject2;
/*     */     }
/* 511 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static URL fileToURL(File paramFile)
/*     */   {
/*     */     try
/*     */     {
/* 524 */       str = paramFile.getCanonicalPath();
/*     */     } catch (IOException localIOException) {
/* 526 */       str = paramFile.getAbsolutePath();
/*     */     }
/* 528 */     String str = str.replace(File.separatorChar, '/');
/* 529 */     if (!str.startsWith("/")) {
/* 530 */       str = "/" + str;
/*     */     }
/*     */ 
/* 533 */     if (!paramFile.isFile())
/* 534 */       str = str + "/";
/*     */     try
/*     */     {
/* 537 */       return new URL("file", "", str); } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 539 */     throw new IllegalArgumentException("file");
/*     */   }
/*     */ 
/*     */   private static void setScriptArguments(ScriptEngine paramScriptEngine, String[] paramArrayOfString)
/*     */   {
/* 544 */     paramScriptEngine.put("arguments", paramArrayOfString);
/* 545 */     paramScriptEngine.put("javax.script.argv", paramArrayOfString);
/*     */   }
/*     */ 
/*     */   private static String setScriptFilename(ScriptEngine paramScriptEngine, String paramString) {
/* 549 */     String str = (String)paramScriptEngine.get("javax.script.filename");
/* 550 */     paramScriptEngine.put("javax.script.filename", paramString);
/* 551 */     return str;
/*     */   }
/*     */ 
/*     */   private static abstract interface Command
/*     */   {
/*     */     public abstract void run(String[] paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.script.shell.Main
 * JD-Core Version:    0.6.2
 */