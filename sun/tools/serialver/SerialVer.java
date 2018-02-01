/*     */ package sun.tools.serialver;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.Button;
/*     */ import java.awt.Event;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Label;
/*     */ import java.awt.TextField;
/*     */ import java.awt.Window;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class SerialVer extends Applet
/*     */ {
/*     */   GridBagLayout gb;
/*     */   TextField classname_t;
/*     */   Button show_b;
/*     */   TextField serialversion_t;
/*     */   Label footer_l;
/*     */   private static final long serialVersionUID = 7666909783837760853L;
/* 147 */   static URLClassLoader loader = null;
/*     */ 
/*     */   public synchronized void init()
/*     */   {
/*  52 */     this.gb = new GridBagLayout();
/*  53 */     setLayout(this.gb);
/*     */ 
/*  55 */     GridBagConstraints localGridBagConstraints = new GridBagConstraints();
/*  56 */     localGridBagConstraints.fill = 1;
/*     */ 
/*  58 */     Label localLabel1 = new Label(Res.getText("FullClassName"));
/*  59 */     localLabel1.setAlignment(2);
/*  60 */     this.gb.setConstraints(localLabel1, localGridBagConstraints);
/*  61 */     add(localLabel1);
/*     */ 
/*  63 */     this.classname_t = new TextField(20);
/*  64 */     localGridBagConstraints.gridwidth = -1;
/*  65 */     localGridBagConstraints.weightx = 1.0D;
/*  66 */     this.gb.setConstraints(this.classname_t, localGridBagConstraints);
/*  67 */     add(this.classname_t);
/*     */ 
/*  69 */     this.show_b = new Button(Res.getText("Show"));
/*  70 */     localGridBagConstraints.gridwidth = 0;
/*  71 */     localGridBagConstraints.weightx = 0.0D;
/*  72 */     this.gb.setConstraints(this.show_b, localGridBagConstraints);
/*  73 */     add(this.show_b);
/*     */ 
/*  75 */     Label localLabel2 = new Label(Res.getText("SerialVersion"));
/*  76 */     localLabel2.setAlignment(2);
/*  77 */     localGridBagConstraints.gridwidth = 1;
/*  78 */     this.gb.setConstraints(localLabel2, localGridBagConstraints);
/*  79 */     add(localLabel2);
/*     */ 
/*  81 */     this.serialversion_t = new TextField(50);
/*  82 */     this.serialversion_t.setEditable(false);
/*  83 */     localGridBagConstraints.gridwidth = 0;
/*  84 */     this.gb.setConstraints(this.serialversion_t, localGridBagConstraints);
/*  85 */     add(this.serialversion_t);
/*     */ 
/*  87 */     this.footer_l = new Label();
/*  88 */     localGridBagConstraints.gridwidth = 0;
/*  89 */     this.gb.setConstraints(this.footer_l, localGridBagConstraints);
/*  90 */     add(this.footer_l);
/*     */ 
/*  93 */     this.classname_t.requestFocus();
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  98 */     this.classname_t.requestFocus();
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject)
/*     */   {
/* 103 */     if (paramEvent.target == this.classname_t) {
/* 104 */       show((String)paramEvent.arg);
/* 105 */       return true;
/* 106 */     }if (paramEvent.target == this.show_b) {
/* 107 */       show(this.classname_t.getText());
/* 108 */       return true;
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event paramEvent)
/*     */   {
/* 116 */     boolean bool = super.handleEvent(paramEvent);
/* 117 */     return bool;
/*     */   }
/*     */ 
/*     */   void show(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 125 */       this.footer_l.setText("");
/* 126 */       this.serialversion_t.setText("");
/*     */ 
/* 128 */       if (paramString.equals("")) {
/* 129 */         return;
/*     */       }
/*     */ 
/* 132 */       String str = serialSyntax(paramString);
/* 133 */       if (str != null)
/* 134 */         this.serialversion_t.setText(str);
/*     */       else
/* 136 */         this.footer_l.setText(Res.getText("NotSerializable", paramString));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 139 */       this.footer_l.setText(Res.getText("ClassNotFound", paramString));
/*     */     }
/*     */   }
/*     */ 
/*     */   static void initializeLoader(String paramString)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 156 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/* 157 */     int i = localStringTokenizer.countTokens();
/* 158 */     URL[] arrayOfURL = new URL[i];
/* 159 */     for (int j = 0; j < i; j++) {
/* 160 */       arrayOfURL[j] = ParseUtil.fileToEncodedURL(new File(new File(localStringTokenizer
/* 161 */         .nextToken()).getCanonicalPath()));
/*     */     }
/* 163 */     loader = new URLClassLoader(arrayOfURL);
/*     */   }
/*     */ 
/*     */   static String serialSyntax(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 171 */     String str1 = null;
/* 172 */     int i = 0;
/*     */ 
/* 175 */     if (paramString.indexOf('$') != -1) {
/* 176 */       str1 = resolveClass(paramString);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 183 */         str1 = resolveClass(paramString);
/* 184 */         i = 1;
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException1) {
/*     */       }
/* 188 */       if (i == 0) {
/* 189 */         StringBuffer localStringBuffer = new StringBuffer(paramString);
/* 190 */         String str2 = localStringBuffer.toString();
/*     */         int j;
/* 192 */         while (((j = str2.lastIndexOf('.')) != -1) && (i == 0)) {
/* 193 */           localStringBuffer.setCharAt(j, '$');
/*     */           try {
/* 195 */             str2 = localStringBuffer.toString();
/* 196 */             str1 = resolveClass(str2);
/* 197 */             i = 1;
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException2) {
/*     */           }
/*     */         }
/*     */       }
/* 203 */       if (i == 0) {
/* 204 */         throw new ClassNotFoundException();
/*     */       }
/*     */     }
/* 207 */     return str1;
/*     */   }
/*     */ 
/*     */   static String resolveClass(String paramString) throws ClassNotFoundException {
/* 211 */     Class localClass = Class.forName(paramString, false, loader);
/* 212 */     ObjectStreamClass localObjectStreamClass = ObjectStreamClass.lookup(localClass);
/* 213 */     if (localObjectStreamClass != null)
/*     */     {
/* 215 */       return "    private static final long serialVersionUID = " + localObjectStreamClass
/* 215 */         .getSerialVersionUID() + "L;";
/*     */     }
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */   private static void showWindow(Window paramWindow)
/*     */   {
/* 223 */     paramWindow.show();
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString) {
/* 227 */     int i = 0;
/* 228 */     String str1 = null;
/* 229 */     int j = 0;
/*     */ 
/* 231 */     if (paramArrayOfString.length == 0) {
/* 232 */       usage();
/* 233 */       System.exit(1);
/*     */     }
/*     */ 
/* 236 */     for (j = 0; j < paramArrayOfString.length; j++) {
/* 237 */       if (paramArrayOfString[j].equals("-show")) {
/* 238 */         i = 1;
/* 239 */       } else if (paramArrayOfString[j].equals("-classpath")) {
/* 240 */         if ((j + 1 == paramArrayOfString.length) || (paramArrayOfString[(j + 1)].startsWith("-"))) {
/* 241 */           System.err.println(Res.getText("error.missing.classpath"));
/* 242 */           usage();
/* 243 */           System.exit(1);
/*     */         }
/* 245 */         str1 = new String(paramArrayOfString[(j + 1)]);
/* 246 */         j++; } else {
/* 247 */         if (!paramArrayOfString[j].startsWith("-")) break;
/* 248 */         System.err.println(Res.getText("invalid.flag", paramArrayOfString[j]));
/* 249 */         usage();
/* 250 */         System.exit(1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 261 */     if (str1 == null) {
/* 262 */       str1 = System.getProperty("env.class.path");
/*     */ 
/* 266 */       if (str1 == null) {
/* 267 */         str1 = ".";
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 272 */       initializeLoader(str1);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 274 */       System.err.println(Res.getText("error.parsing.classpath", str1));
/* 275 */       System.exit(2);
/*     */     } catch (IOException localIOException) {
/* 277 */       System.err.println(Res.getText("error.parsing.classpath", str1));
/* 278 */       System.exit(3);
/*     */     }
/*     */ 
/* 281 */     if (i == 0)
/*     */     {
/* 286 */       if (j == paramArrayOfString.length) {
/* 287 */         usage();
/* 288 */         System.exit(1);
/*     */       }
/*     */ 
/* 294 */       int k = 0;
/* 295 */       for (j = j; j < paramArrayOfString.length; j++) {
/*     */         try {
/* 297 */           String str2 = serialSyntax(paramArrayOfString[j]);
/* 298 */           if (str2 != null) {
/* 299 */             System.out.println(paramArrayOfString[j] + ":" + str2);
/*     */           } else {
/* 301 */             System.err.println(Res.getText("NotSerializable", paramArrayOfString[j]));
/*     */ 
/* 303 */             k = 1;
/*     */           }
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 306 */           System.err.println(Res.getText("ClassNotFound", paramArrayOfString[j]));
/* 307 */           k = 1;
/*     */         }
/*     */       }
/* 310 */       if (k != 0)
/* 311 */         System.exit(1);
/*     */     }
/*     */     else {
/* 314 */       if (j < paramArrayOfString.length) {
/* 315 */         System.err.println(Res.getText("ignoring.classes"));
/* 316 */         System.exit(1);
/*     */       }
/* 318 */       SerialVerFrame localSerialVerFrame = new SerialVerFrame();
/*     */ 
/* 320 */       SerialVer localSerialVer = new SerialVer();
/* 321 */       localSerialVer.init();
/*     */ 
/* 323 */       localSerialVerFrame.add("Center", localSerialVer);
/* 324 */       localSerialVerFrame.pack();
/* 325 */       showWindow(localSerialVerFrame);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void usage()
/*     */   {
/* 334 */     System.err.println(Res.getText("usage"));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.serialver.SerialVer
 * JD-Core Version:    0.6.2
 */