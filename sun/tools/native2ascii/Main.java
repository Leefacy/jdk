/*     */ package sun.tools.native2ascii;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class Main
/*     */ {
/*  86 */   String inputFileName = null;
/*  87 */   String outputFileName = null;
/*  88 */   File tempFile = null;
/*  89 */   boolean reverse = false;
/*  90 */   static String encodingString = null;
/*  91 */   static String defaultEncoding = null;
/*  92 */   static CharsetEncoder encoder = null;
/*     */   private static ResourceBundle rsrc;
/*     */ 
/*     */   public synchronized boolean convert(String[] paramArrayOfString)
/*     */   {
/*  98 */     ArrayList localArrayList = new ArrayList(2);
/*  99 */     File localFile = null;
/* 100 */     int i = 0;
/*     */ 
/* 103 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 104 */       if (paramArrayOfString[j].equals("-encoding")) {
/* 105 */         if (j + 1 < paramArrayOfString.length) {
/* 106 */           encodingString = paramArrayOfString[(++j)];
/*     */         } else {
/* 108 */           error(getMsg("err.bad.arg"));
/* 109 */           usage();
/* 110 */           return false;
/*     */         }
/* 112 */       } else if (paramArrayOfString[j].equals("-reverse")) {
/* 113 */         this.reverse = true;
/*     */       } else {
/* 115 */         if (localArrayList.size() > 1) {
/* 116 */           usage();
/* 117 */           return false;
/*     */         }
/* 119 */         localArrayList.add(paramArrayOfString[j]);
/*     */       }
/*     */     }
/* 122 */     if (encodingString == null) {
/* 123 */       defaultEncoding = Charset.defaultCharset().name();
/*     */     }
/* 125 */     char[] arrayOfChar = System.getProperty("line.separator").toCharArray();
/*     */     try {
/* 127 */       initializeConverter();
/*     */ 
/* 129 */       if (localArrayList.size() == 1) {
/* 130 */         this.inputFileName = ((String)localArrayList.get(0));
/*     */       }
/* 132 */       if (localArrayList.size() == 2) {
/* 133 */         this.inputFileName = ((String)localArrayList.get(0));
/* 134 */         this.outputFileName = ((String)localArrayList.get(1));
/* 135 */         i = 1;
/*     */       }
/*     */ 
/* 138 */       if (i != 0) {
/* 139 */         localFile = new File(this.outputFileName);
/* 140 */         if ((localFile.exists()) && (!localFile.canWrite()))
/* 141 */           throw new Exception(formatMsg("err.cannot.write", this.outputFileName));
/*     */       }
/*     */       Object localObject1;
/*     */       Object localObject2;
/*     */       Object localObject3;
/* 145 */       if (this.reverse) {
/* 146 */         localObject1 = getA2NInput(this.inputFileName);
/* 147 */         localObject2 = getA2NOutput(this.outputFileName);
/*     */ 
/* 150 */         while ((localObject3 = ((BufferedReader)localObject1).readLine()) != null) {
/* 151 */           ((Writer)localObject2).write(((String)localObject3).toCharArray());
/* 152 */           ((Writer)localObject2).write(arrayOfChar);
/* 153 */           if (this.outputFileName == null) {
/* 154 */             ((Writer)localObject2).flush();
/*     */           }
/*     */         }
/* 157 */         ((BufferedReader)localObject1).close();
/* 158 */         ((Writer)localObject2).close();
/*     */       }
/*     */       else
/*     */       {
/* 162 */         localObject2 = getN2AInput(this.inputFileName);
/* 163 */         localObject3 = getN2AOutput(this.outputFileName);
/*     */ 
/* 165 */         while ((localObject1 = ((BufferedReader)localObject2).readLine()) != null) {
/* 166 */           ((BufferedWriter)localObject3).write(((String)localObject1).toCharArray());
/* 167 */           ((BufferedWriter)localObject3).write(arrayOfChar);
/* 168 */           if (this.outputFileName == null) {
/* 169 */             ((BufferedWriter)localObject3).flush();
/*     */           }
/*     */         }
/* 172 */         ((BufferedWriter)localObject3).close();
/*     */       }
/*     */ 
/* 175 */       if (i != 0) {
/* 176 */         if (localFile.exists())
/*     */         {
/* 181 */           localFile.delete();
/*     */         }
/* 183 */         this.tempFile.renameTo(localFile);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 187 */       error(localException.toString());
/* 188 */       return false;
/*     */     }
/*     */ 
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   private void error(String paramString) {
/* 195 */     System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   private void usage() {
/* 199 */     System.out.println(getMsg("usage"));
/*     */   }
/*     */ 
/*     */   private BufferedReader getN2AInput(String paramString)
/*     */     throws Exception
/*     */   {
/*     */     Object localObject1;
/* 206 */     if (paramString == null) {
/* 207 */       localObject1 = System.in;
/*     */     } else {
/* 209 */       localObject2 = new File(paramString);
/* 210 */       if (!((File)localObject2).canRead()) {
/* 211 */         throw new Exception(formatMsg("err.cannot.read", ((File)localObject2).getName()));
/*     */       }
/*     */       try
/*     */       {
/* 215 */         localObject1 = new FileInputStream(paramString);
/*     */       } catch (IOException localIOException) {
/* 217 */         throw new Exception(formatMsg("err.cannot.read", ((File)localObject2).getName()));
/*     */       }
/*     */     }
/*     */ 
/* 221 */     Object localObject2 = encodingString != null ? new BufferedReader(new InputStreamReader((InputStream)localObject1, encodingString)) : new BufferedReader(new InputStreamReader((InputStream)localObject1));
/*     */ 
/* 225 */     return localObject2;
/*     */   }
/*     */ 
/*     */   private BufferedWriter getN2AOutput(String paramString)
/*     */     throws Exception
/*     */   {
/*     */     Object localObject;
/* 233 */     if (paramString == null) {
/* 234 */       localObject = new OutputStreamWriter(System.out, "US-ASCII");
/*     */     }
/*     */     else {
/* 237 */       File localFile1 = new File(paramString);
/*     */ 
/* 239 */       File localFile2 = localFile1.getParentFile();
/*     */ 
/* 241 */       if (localFile2 == null) {
/* 242 */         localFile2 = new File(System.getProperty("user.dir"));
/*     */       }
/* 244 */       this.tempFile = File.createTempFile("_N2A", ".TMP", localFile2);
/*     */ 
/* 247 */       this.tempFile.deleteOnExit();
/*     */       try
/*     */       {
/* 250 */         localObject = new FileWriter(this.tempFile);
/*     */       } catch (IOException localIOException) {
/* 252 */         throw new Exception(formatMsg("err.cannot.write", this.tempFile.getName()));
/*     */       }
/*     */     }
/*     */ 
/* 256 */     BufferedWriter localBufferedWriter = new BufferedWriter(new N2AFilter((Writer)localObject));
/* 257 */     return localBufferedWriter;
/*     */   }
/*     */ 
/*     */   private BufferedReader getA2NInput(String paramString)
/*     */     throws Exception
/*     */   {
/*     */     Object localObject;
/* 264 */     if (paramString == null) {
/* 265 */       localObject = new InputStreamReader(System.in, "US-ASCII");
/*     */     } else {
/* 267 */       File localFile = new File(paramString);
/* 268 */       if (!localFile.canRead()) {
/* 269 */         throw new Exception(formatMsg("err.cannot.read", localFile.getName()));
/*     */       }
/*     */       try
/*     */       {
/* 273 */         localObject = new FileReader(paramString);
/*     */       } catch (Exception localException) {
/* 275 */         throw new Exception(formatMsg("err.cannot.read", localFile.getName()));
/*     */       }
/*     */     }
/*     */ 
/* 279 */     BufferedReader localBufferedReader = new BufferedReader(new A2NFilter((Reader)localObject));
/* 280 */     return localBufferedReader;
/*     */   }
/*     */ 
/*     */   private Writer getA2NOutput(String paramString) throws Exception
/*     */   {
/* 285 */     OutputStreamWriter localOutputStreamWriter = null;
/* 286 */     Object localObject = null;
/*     */ 
/* 288 */     if (paramString == null) {
/* 289 */       localObject = System.out;
/*     */     } else {
/* 291 */       File localFile1 = new File(paramString);
/*     */ 
/* 293 */       File localFile2 = localFile1.getParentFile();
/* 294 */       if (localFile2 == null)
/* 295 */         localFile2 = new File(System.getProperty("user.dir"));
/* 296 */       this.tempFile = File.createTempFile("_N2A", ".TMP", localFile2);
/*     */ 
/* 299 */       this.tempFile.deleteOnExit();
/*     */       try
/*     */       {
/* 302 */         localObject = new FileOutputStream(this.tempFile);
/*     */       } catch (IOException localIOException) {
/* 304 */         throw new Exception(formatMsg("err.cannot.write", this.tempFile.getName()));
/*     */       }
/*     */     }
/*     */ 
/* 308 */     localOutputStreamWriter = encodingString != null ? new OutputStreamWriter((OutputStream)localObject, encodingString) : new OutputStreamWriter((OutputStream)localObject);
/*     */ 
/* 312 */     return localOutputStreamWriter;
/*     */   }
/*     */ 
/*     */   private static Charset lookupCharset(String paramString) {
/* 316 */     if (Charset.isSupported(paramString)) {
/*     */       try {
/* 318 */         return Charset.forName(paramString);
/*     */       } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/* 320 */         throw new Error(localUnsupportedCharsetException);
/*     */       }
/*     */     }
/* 323 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean canConvert(char paramChar) {
/* 327 */     return (encoder != null) && (encoder.canEncode(paramChar));
/*     */   }
/*     */ 
/*     */   private static void initializeConverter() throws UnsupportedEncodingException {
/* 331 */     Charset localCharset = null;
/*     */     try
/*     */     {
/* 336 */       localCharset = encodingString == null ? 
/* 335 */         lookupCharset(defaultEncoding) : 
/* 336 */         lookupCharset(encodingString);
/*     */ 
/* 339 */       encoder = localCharset != null ? localCharset
/* 339 */         .newEncoder() : null;
/*     */     }
/*     */     catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 342 */       throw new Error(localIllegalCharsetNameException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getMsg(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 359 */       return rsrc.getString(paramString); } catch (MissingResourceException localMissingResourceException) {
/*     */     }
/* 361 */     throw new Error("Error in  message file format.");
/*     */   }
/*     */ 
/*     */   private String formatMsg(String paramString1, String paramString2)
/*     */   {
/* 366 */     String str = getMsg(paramString1);
/* 367 */     return MessageFormat.format(str, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 375 */     Main localMain = new Main();
/* 376 */     System.exit(localMain.convert(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 350 */       rsrc = ResourceBundle.getBundle("sun.tools.native2ascii.resources.MsgNative2ascii");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/* 353 */       throw new Error("Missing message file.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.native2ascii.Main
 * JD-Core Version:    0.6.2
 */