/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.som.cff.FileLocator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Arguments
/*     */ {
/* 252 */   public String file = null;
/*     */ 
/* 257 */   public boolean verbose = false;
/*     */ 
/* 263 */   public boolean keepOldFiles = false;
/*     */ 
/* 268 */   public boolean emitAll = false;
/*     */ 
/* 283 */   public Vector includePaths = new Vector();
/*     */ 
/* 289 */   public Hashtable definedSymbols = new Hashtable();
/*     */ 
/* 295 */   public boolean cppModule = false;
/*     */ 
/* 300 */   public boolean versionRequest = false;
/*     */ 
/* 311 */   public float corbaLevel = 2.2F;
/*     */ 
/* 316 */   public boolean noWarn = false;
/*     */ 
/* 322 */   public boolean scannerDebugFlag = false;
/* 323 */   public boolean tokenDebugFlag = false;
/*     */ 
/*     */   protected void parseOtherArgs(String[] paramArrayOfString, Properties paramProperties)
/*     */     throws InvalidArgument
/*     */   {
/*  81 */     if (paramArrayOfString.length > 0)
/*  82 */       throw new InvalidArgument(paramArrayOfString[0]);
/*     */   }
/*     */ 
/*     */   protected void setDebugFlags(String paramString)
/*     */   {
/*  88 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/*  89 */     while (localStringTokenizer.hasMoreTokens()) {
/*  90 */       String str = localStringTokenizer.nextToken();
/*     */       try
/*     */       {
/*  95 */         Field localField = getClass().getField(str + "DebugFlag");
/*  96 */         int i = localField.getModifiers();
/*  97 */         if ((Modifier.isPublic(i)) && (!Modifier.isStatic(i)) && 
/*  98 */           (localField.getType() == Boolean.TYPE))
/*  99 */           localField.setBoolean(this, true);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void parseArgs(String[] paramArrayOfString) throws InvalidArgument
/*     */   {
/* 110 */     Vector localVector = new Vector();
/* 111 */     int i = 0;
/*     */     Object localObject;
/*     */     try {
/* 115 */       for (i = 0; i < paramArrayOfString.length - 1; i++) {
/* 116 */         String str = paramArrayOfString[i].toLowerCase();
/* 117 */         if ((str.charAt(0) != '-') && (str.charAt(0) != '/'))
/* 118 */           throw new InvalidArgument(paramArrayOfString[i]);
/* 119 */         if (str.charAt(0) == '-') {
/* 120 */           str = str.substring(1);
/*     */         }
/*     */ 
/* 124 */         if (str.equals("i")) {
/* 125 */           this.includePaths.addElement(paramArrayOfString[(++i)]);
/* 126 */         } else if (str.startsWith("i")) {
/* 127 */           this.includePaths.addElement(paramArrayOfString[i].substring(2));
/* 128 */         } else if ((str.equals("v")) || (str.equals("verbose")))
/*     */         {
/* 130 */           this.verbose = true;
/* 131 */         } else if (str.equals("d"))
/*     */         {
/* 133 */           this.definedSymbols.put(paramArrayOfString[(++i)], "");
/* 134 */         } else if (str.equals("debug"))
/*     */         {
/* 136 */           setDebugFlags(paramArrayOfString[(++i)]);
/* 137 */         } else if (str.startsWith("d")) {
/* 138 */           this.definedSymbols.put(paramArrayOfString[i].substring(2), "");
/* 139 */         } else if (str.equals("emitall"))
/*     */         {
/* 141 */           this.emitAll = true;
/* 142 */         } else if (str.equals("keep"))
/*     */         {
/* 144 */           this.keepOldFiles = true;
/* 145 */         } else if (str.equals("nowarn"))
/*     */         {
/* 147 */           this.noWarn = true;
/* 148 */         } else if (str.equals("trace"))
/*     */         {
/* 150 */           Runtime.getRuntime().traceMethodCalls(true);
/*     */         }
/* 159 */         else if (str.equals("cppmodule")) {
/* 160 */           this.cppModule = true;
/* 161 */         } else if (str.equals("version"))
/*     */         {
/* 163 */           this.versionRequest = true;
/* 164 */         } else if (str.equals("corba"))
/*     */         {
/* 166 */           if (i + 1 >= paramArrayOfString.length)
/* 167 */             throw new InvalidArgument(paramArrayOfString[i]);
/* 168 */           localObject = paramArrayOfString[(++i)];
/* 169 */           if (((String)localObject).charAt(0) == '-')
/* 170 */             throw new InvalidArgument(paramArrayOfString[(i - 1)]);
/*     */           try {
/* 172 */             this.corbaLevel = new Float((String)localObject).floatValue();
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 174 */             throw new InvalidArgument(paramArrayOfString[i]);
/*     */           }
/*     */         } else {
/* 177 */           localVector.addElement(paramArrayOfString[i]);
/* 178 */           i++;
/* 179 */           while ((i < paramArrayOfString.length - 1) && 
/* 180 */             (paramArrayOfString[i]
/* 180 */             .charAt(0) != 
/* 180 */             '-') && 
/* 181 */             (paramArrayOfString[i]
/* 181 */             .charAt(0) != 
/* 181 */             '/')) {
/* 182 */             localVector.addElement(paramArrayOfString[(i++)]);
/*     */           }
/* 184 */           i--;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/* 191 */       throw new InvalidArgument(paramArrayOfString[(paramArrayOfString.length - 1)]);
/*     */     }
/*     */ 
/* 197 */     if (i == paramArrayOfString.length - 1) {
/* 198 */       if (paramArrayOfString[i].toLowerCase().equals("-version"))
/* 199 */         this.versionRequest = true;
/*     */       else
/* 201 */         this.file = paramArrayOfString[i];
/*     */     }
/* 203 */     else throw new InvalidArgument();
/*     */ 
/* 206 */     Properties localProperties = new Properties();
/*     */     try {
/* 208 */       localObject = FileLocator.locateFileInClassPath("idl.config");
/* 209 */       localProperties.load((InputStream)localObject);
/* 210 */       addIncludePaths(localProperties);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */     String[] arrayOfString;
/* 218 */     if (localVector.size() > 0) {
/* 219 */       arrayOfString = new String[localVector.size()];
/* 220 */       localVector.copyInto(arrayOfString);
/*     */     } else {
/* 222 */       arrayOfString = new String[0];
/*     */     }
/* 224 */     parseOtherArgs(arrayOfString, localProperties);
/*     */   }
/*     */ 
/*     */   private void addIncludePaths(Properties paramProperties)
/*     */   {
/* 232 */     String str1 = paramProperties.getProperty("includes");
/* 233 */     if (str1 != null)
/*     */     {
/* 235 */       String str2 = System.getProperty("path.separator");
/* 236 */       int i = -str2.length();
/*     */       do
/*     */       {
/* 239 */         str1 = str1.substring(i + str2.length());
/* 240 */         i = str1.indexOf(str2);
/* 241 */         if (i < 0)
/* 242 */           i = str1.length();
/* 243 */         this.includePaths.addElement(str1.substring(0, i));
/*     */       }
/* 245 */       while (i != str1.length());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Arguments
 * JD-Core Version:    0.6.2
 */