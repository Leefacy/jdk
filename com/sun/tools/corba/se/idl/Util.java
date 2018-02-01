/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.som.cff.FileLocator;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Util
/*     */ {
/* 307 */   private static Properties messages = null;
/* 308 */   private static String defaultKey = "default";
/* 309 */   private static Vector msgFiles = new Vector();
/*     */ 
/* 378 */   static RepositoryID emptyID = new RepositoryID();
/*     */ 
/*     */   public static String getVersion()
/*     */   {
/*  67 */     return getVersion("com/sun/tools/corba/se/idl/idl.prp");
/*     */   }
/*     */ 
/*     */   public static String getVersion(String paramString)
/*     */   {
/*  80 */     String str = "";
/*  81 */     if (messages == null)
/*     */     {
/*  83 */       Vector localVector = msgFiles;
/*  84 */       if ((paramString == null) || (paramString.equals("")))
/*  85 */         paramString = "com/sun/tools/corba/se/idl/idl.prp";
/*  86 */       paramString = paramString.replace('/', File.separatorChar);
/*  87 */       registerMessageFile(paramString);
/*  88 */       str = getMessage("Version.product", getMessage("Version.number"));
/*  89 */       msgFiles = localVector;
/*  90 */       messages = null;
/*     */     }
/*     */     else
/*     */     {
/*  94 */       str = getMessage("Version.product", getMessage("Version.number"));
/*     */     }
/*  96 */     return str;
/*     */   }
/*     */ 
/*     */   public static boolean isAttribute(String paramString, Hashtable paramHashtable)
/*     */   {
/* 101 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 102 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof AttributeEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isConst(String paramString, Hashtable paramHashtable)
/*     */   {
/* 107 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 108 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof ConstEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isEnum(String paramString, Hashtable paramHashtable)
/*     */   {
/* 113 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 114 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof EnumEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isException(String paramString, Hashtable paramHashtable)
/*     */   {
/* 119 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 120 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof ExceptionEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isInterface(String paramString, Hashtable paramHashtable)
/*     */   {
/* 125 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 126 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof InterfaceEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isMethod(String paramString, Hashtable paramHashtable)
/*     */   {
/* 131 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 132 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof MethodEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isModule(String paramString, Hashtable paramHashtable)
/*     */   {
/* 137 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 138 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof ModuleEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isParameter(String paramString, Hashtable paramHashtable)
/*     */   {
/* 143 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 144 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof ParameterEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isPrimitive(String paramString, Hashtable paramHashtable)
/*     */   {
/* 151 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 152 */     if (localSymtabEntry == null)
/*     */     {
/* 157 */       int i = paramString.indexOf('(');
/* 158 */       if (i >= 0)
/* 159 */         localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString.substring(0, i));
/*     */     }
/* 161 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof PrimitiveEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isSequence(String paramString, Hashtable paramHashtable)
/*     */   {
/* 166 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 167 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof SequenceEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isStruct(String paramString, Hashtable paramHashtable)
/*     */   {
/* 172 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 173 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof StructEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isString(String paramString, Hashtable paramHashtable)
/*     */   {
/* 178 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 179 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof StringEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isTypedef(String paramString, Hashtable paramHashtable)
/*     */   {
/* 184 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 185 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof TypedefEntry;
/*     */   }
/*     */ 
/*     */   public static boolean isUnion(String paramString, Hashtable paramHashtable)
/*     */   {
/* 190 */     SymtabEntry localSymtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 191 */     return localSymtabEntry == null ? false : localSymtabEntry instanceof UnionEntry;
/*     */   }
/*     */ 
/*     */   public static String getMessage(String paramString)
/*     */   {
/* 199 */     if (messages == null)
/* 200 */       readMessages();
/* 201 */     String str = messages.getProperty(paramString);
/* 202 */     if (str == null)
/* 203 */       str = getDefaultMessage(paramString);
/* 204 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getMessage(String paramString1, String paramString2)
/*     */   {
/* 209 */     if (messages == null)
/* 210 */       readMessages();
/* 211 */     String str = messages.getProperty(paramString1);
/* 212 */     if (str == null) {
/* 213 */       str = getDefaultMessage(paramString1);
/*     */     }
/*     */     else {
/* 216 */       int i = str.indexOf("%0");
/* 217 */       if (i >= 0)
/* 218 */         str = str.substring(0, i) + paramString2 + str.substring(i + 2);
/*     */     }
/* 220 */     return str;
/*     */   }
/*     */ 
/*     */   public static String getMessage(String paramString, String[] paramArrayOfString)
/*     */   {
/* 225 */     if (messages == null)
/* 226 */       readMessages();
/* 227 */     String str = messages.getProperty(paramString);
/* 228 */     if (str == null)
/* 229 */       str = getDefaultMessage(paramString);
/*     */     else
/* 231 */       for (int i = 0; i < paramArrayOfString.length; i++)
/*     */       {
/* 233 */         int j = str.indexOf("%" + i);
/* 234 */         if (j >= 0)
/* 235 */           str = str.substring(0, j) + paramArrayOfString[i] + str.substring(j + 2);
/*     */       }
/* 237 */     return str;
/*     */   }
/*     */ 
/*     */   private static String getDefaultMessage(String paramString)
/*     */   {
/* 242 */     String str = messages.getProperty(defaultKey);
/* 243 */     int i = str.indexOf("%0");
/* 244 */     if (i > 0)
/* 245 */       str = str.substring(0, i) + paramString;
/* 246 */     return str;
/*     */   }
/*     */ 
/*     */   private static void readMessages()
/*     */   {
/* 273 */     messages = new Properties();
/* 274 */     Enumeration localEnumeration = msgFiles.elements();
/*     */ 
/* 276 */     while (localEnumeration.hasMoreElements())
/*     */       try
/*     */       {
/* 279 */         DataInputStream localDataInputStream = FileLocator.locateLocaleSpecificFileInClassPath((String)localEnumeration.nextElement());
/* 280 */         messages.load(localDataInputStream);
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/* 285 */     if (messages.size() == 0)
/* 286 */       messages.put(defaultKey, "Error reading Messages File.");
/*     */   }
/*     */ 
/*     */   public static void registerMessageFile(String paramString)
/*     */   {
/* 293 */     if (paramString != null)
/* 294 */       if (messages == null)
/* 295 */         msgFiles.addElement(paramString);
/*     */       else
/*     */         try
/*     */         {
/* 299 */           DataInputStream localDataInputStream = FileLocator.locateLocaleSpecificFileInClassPath(paramString);
/* 300 */           messages.load(localDataInputStream);
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */   }
/*     */ 
/*     */   public static String capitalize(String paramString)
/*     */   {
/* 320 */     String str = new String(paramString.substring(0, 1));
/* 321 */     str = str.toUpperCase();
/* 322 */     return str + paramString.substring(1);
/*     */   }
/*     */ 
/*     */   public static String getAbsolutePath(String paramString, Vector paramVector)
/*     */     throws FileNotFoundException
/*     */   {
/* 336 */     String str1 = null;
/* 337 */     File localFile = new File(paramString);
/* 338 */     if (localFile.canRead()) {
/* 339 */       str1 = localFile.getAbsolutePath();
/*     */     }
/*     */     else {
/* 342 */       String str2 = null;
/* 343 */       Enumeration localEnumeration = paramVector.elements();
/* 344 */       while ((!localFile.canRead()) && (localEnumeration.hasMoreElements()))
/*     */       {
/* 346 */         str2 = (String)localEnumeration.nextElement() + File.separatorChar + paramString;
/* 347 */         localFile = new File(str2);
/*     */       }
/* 349 */       if (localFile.canRead())
/* 350 */         str1 = localFile.getPath();
/*     */       else
/* 352 */         throw new FileNotFoundException(paramString);
/*     */     }
/* 354 */     return str1;
/*     */   }
/*     */ 
/*     */   public static float absDelta(float paramFloat1, float paramFloat2)
/*     */   {
/* 371 */     double d = paramFloat1 - paramFloat2;
/* 372 */     return (float)(d < 0.0D ? d * -1.0D : d);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 312 */     msgFiles.addElement("com/sun/tools/corba/se/idl/idl.prp");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.Util
 * JD-Core Version:    0.6.2
 */