/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InvalidArgument;
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Arguments extends com.sun.tools.corba.se.idl.Arguments
/*     */ {
/* 317 */   public Hashtable packages = new Hashtable();
/*     */ 
/* 319 */   public String separator = null;
/*     */   public static final int None = 0;
/*     */   public static final int Client = 1;
/*     */   public static final int Server = 2;
/*     */   public static final int All = 3;
/* 326 */   public int emit = 0;
/* 327 */   public boolean TIEServer = false;
/* 328 */   public boolean POAServer = true;
/*     */ 
/* 332 */   public boolean LocalOptimization = false;
/* 333 */   public NameModifier skeletonNameModifier = null;
/* 334 */   public NameModifier tieNameModifier = null;
/*     */ 
/* 339 */   public Hashtable packageTranslation = new Hashtable();
/*     */ 
/* 341 */   public String targetDir = "";
/*     */ 
/*     */   public Arguments()
/*     */   {
/*  65 */     this.corbaLevel = 2.4F;
/*     */   }
/*     */ 
/*     */   protected void parseOtherArgs(String[] paramArrayOfString, Properties paramProperties)
/*     */     throws InvalidArgument
/*     */   {
/*  74 */     String str1 = null;
/*  75 */     String str2 = null;
/*     */ 
/*  78 */     this.packages.put("CORBA", "org.omg");
/*  79 */     packageFromProps(paramProperties);
/*     */     try
/*     */     {
/*  86 */       Vector localVector = new Vector();
/*     */ 
/*  89 */       for (int i = 0; i < paramArrayOfString.length; i++)
/*     */       {
/*  91 */         String str3 = paramArrayOfString[i].toLowerCase();
/*     */ 
/*  93 */         if ((str3.charAt(0) != '-') && (str3.charAt(0) != '/'))
/*  94 */           throw new InvalidArgument(paramArrayOfString[i]);
/*  95 */         if (str3.charAt(0) == '-') {
/*  96 */           str3 = str3.substring(1);
/*     */         }
/*     */ 
/* 100 */         if (str3.startsWith("f"))
/*     */         {
/* 103 */           if (str3.equals("f")) {
/* 104 */             str3 = 'f' + paramArrayOfString[(++i)].toLowerCase();
/*     */           }
/*     */ 
/* 110 */           if (str3.equals("fclient"))
/*     */           {
/* 112 */             this.emit = ((this.emit == 2) || (this.emit == 3) ? 3 : 1);
/*     */           }
/* 114 */           else if (str3.equals("fserver"))
/*     */           {
/* 116 */             this.emit = ((this.emit == 1) || (this.emit == 3) ? 3 : 2);
/* 117 */             this.TIEServer = false;
/*     */           }
/* 119 */           else if (str3.equals("fall"))
/*     */           {
/* 121 */             this.emit = 3;
/* 122 */             this.TIEServer = false;
/*     */           }
/* 126 */           else if (str3.equals("fservertie"))
/*     */           {
/* 128 */             this.emit = ((this.emit == 1) || (this.emit == 3) ? 3 : 2);
/* 129 */             this.TIEServer = true;
/*     */           }
/* 131 */           else if (str3.equals("falltie"))
/*     */           {
/* 133 */             this.emit = 3;
/* 134 */             this.TIEServer = true;
/*     */           }
/*     */           else {
/* 137 */             i = collectUnknownArg(paramArrayOfString, i, localVector);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           String str4;
/*     */           String str5;
/* 139 */           if (str3.equals("pkgtranslate"))
/*     */           {
/* 141 */             if (i + 2 >= paramArrayOfString.length) {
/* 142 */               throw new InvalidArgument(paramArrayOfString[i]);
/*     */             }
/* 144 */             str4 = paramArrayOfString[(++i)];
/* 145 */             str5 = paramArrayOfString[(++i)];
/* 146 */             checkPackageNameValid(str4);
/* 147 */             checkPackageNameValid(str5);
/* 148 */             if ((str4.equals("org")) || (str4.startsWith("org.omg")))
/* 149 */               throw new InvalidArgument(paramArrayOfString[i]);
/* 150 */             str4 = str4.replace('.', '/');
/* 151 */             str5 = str5.replace('.', '/');
/* 152 */             this.packageTranslation.put(str4, str5);
/*     */           }
/* 155 */           else if (str3.equals("pkgprefix"))
/*     */           {
/* 157 */             if (i + 2 >= paramArrayOfString.length) {
/* 158 */               throw new InvalidArgument(paramArrayOfString[i]);
/*     */             }
/* 160 */             str4 = paramArrayOfString[(++i)];
/* 161 */             str5 = paramArrayOfString[(++i)];
/* 162 */             checkPackageNameValid(str4);
/* 163 */             checkPackageNameValid(str5);
/* 164 */             this.packages.put(str4, str5);
/*     */           }
/* 167 */           else if (str3.equals("td"))
/*     */           {
/* 169 */             if (i + 1 >= paramArrayOfString.length)
/* 170 */               throw new InvalidArgument(paramArrayOfString[i]);
/* 171 */             str4 = paramArrayOfString[(++i)];
/* 172 */             if (str4.charAt(0) == '-') {
/* 173 */               throw new InvalidArgument(paramArrayOfString[(i - 1)]);
/*     */             }
/*     */ 
/* 176 */             this.targetDir = str4.replace('/', File.separatorChar);
/* 177 */             if (this.targetDir.charAt(this.targetDir.length() - 1) != File.separatorChar) {
/* 178 */               this.targetDir += File.separatorChar;
/*     */             }
/*     */ 
/*     */           }
/* 182 */           else if (str3.equals("sep"))
/*     */           {
/* 184 */             if (i + 1 >= paramArrayOfString.length)
/* 185 */               throw new InvalidArgument(paramArrayOfString[i]);
/* 186 */             this.separator = paramArrayOfString[(++i)];
/*     */           }
/* 189 */           else if (str3.equals("oldimplbase")) {
/* 190 */             this.POAServer = false;
/*     */           }
/* 192 */           else if (str3.equals("skeletonname")) {
/* 193 */             if (i + 1 >= paramArrayOfString.length)
/* 194 */               throw new InvalidArgument(paramArrayOfString[i]);
/* 195 */             str1 = paramArrayOfString[(++i)];
/*     */           }
/* 197 */           else if (str3.equals("tiename")) {
/* 198 */             if (i + 1 >= paramArrayOfString.length)
/* 199 */               throw new InvalidArgument(paramArrayOfString[i]);
/* 200 */             str2 = paramArrayOfString[(++i)];
/*     */           }
/* 202 */           else if (str3.equals("localoptimization")) {
/* 203 */             this.LocalOptimization = true;
/*     */           } else {
/* 205 */             i = collectUnknownArg(paramArrayOfString, i, localVector);
/*     */           }
/*     */         }
/*     */       }
/* 209 */       if (localVector.size() > 0)
/*     */       {
/* 211 */         String[] arrayOfString = new String[localVector.size()];
/* 212 */         localVector.copyInto(arrayOfString);
/*     */ 
/* 214 */         super.parseOtherArgs(arrayOfString, paramProperties);
/*     */       }
/*     */ 
/* 217 */       setDefaultEmitter();
/* 218 */       setNameModifiers(str1, str2);
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/* 225 */       throw new InvalidArgument(paramArrayOfString[(paramArrayOfString.length - 1)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int collectUnknownArg(String[] paramArrayOfString, int paramInt, Vector paramVector)
/*     */   {
/* 234 */     paramVector.addElement(paramArrayOfString[paramInt]);
/* 235 */     paramInt++;
/* 236 */     while ((paramInt < paramArrayOfString.length) && (paramArrayOfString[paramInt].charAt(0) != '-') && (paramArrayOfString[paramInt].charAt(0) != '/'))
/* 237 */       paramVector.addElement(paramArrayOfString[(paramInt++)]);
/* 238 */     paramInt--; return paramInt;
/*     */   }
/*     */ 
/*     */   protected void packageFromProps(Properties paramProperties)
/*     */     throws InvalidArgument
/*     */   {
/* 247 */     Enumeration localEnumeration = paramProperties.propertyNames();
/* 248 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 250 */       String str1 = (String)localEnumeration.nextElement();
/* 251 */       if (str1.startsWith("PkgPrefix."))
/*     */       {
/* 253 */         String str2 = str1.substring(10);
/* 254 */         String str3 = paramProperties.getProperty(str1);
/* 255 */         checkPackageNameValid(str3);
/* 256 */         checkPackageNameValid(str2);
/* 257 */         this.packages.put(str2, str3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setDefaultEmitter()
/*     */   {
/* 267 */     if (this.emit == 0) this.emit = 1;
/*     */   }
/*     */ 
/*     */   protected void setNameModifiers(String paramString1, String paramString2)
/*     */   {
/* 272 */     if (this.emit > 1)
/*     */     {
/*     */       String str2;
/* 276 */       if (paramString1 != null)
/* 277 */         str2 = paramString1;
/* 278 */       else if (this.POAServer)
/* 279 */         str2 = "%POA";
/*     */       else
/* 281 */         str2 = "_%ImplBase";
/*     */       String str1;
/* 283 */       if (paramString2 != null)
/* 284 */         str1 = paramString2;
/* 285 */       else if (this.POAServer)
/* 286 */         str1 = "%POATie";
/*     */       else {
/* 288 */         str1 = "%_Tie";
/*     */       }
/* 290 */       this.skeletonNameModifier = new NameModifierImpl(str2);
/* 291 */       this.tieNameModifier = new NameModifierImpl(str1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkPackageNameValid(String paramString)
/*     */     throws InvalidArgument
/*     */   {
/* 300 */     if (paramString.charAt(0) == '.')
/* 301 */       throw new InvalidArgument(paramString);
/* 302 */     for (int i = 0; i < paramString.length(); i++)
/* 303 */       if (paramString.charAt(i) == '.')
/*     */       {
/* 305 */         if ((i == paramString.length() - 1) || (!Character.isJavaIdentifierStart(paramString.charAt(++i))))
/* 306 */           throw new InvalidArgument(paramString);
/*     */       }
/* 308 */       else if (!Character.isJavaIdentifierPart(paramString.charAt(i)))
/* 309 */         throw new InvalidArgument(paramString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Arguments
 * JD-Core Version:    0.6.2
 */