/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import com.sun.jdi.request.MethodExitRequest;
/*     */ import com.sun.jdi.request.StepRequest;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ class Env
/*     */ {
/*  47 */   static EventRequestSpecList specList = new EventRequestSpecList();
/*     */   private static VMConnection connection;
/*  51 */   private static SourceMapper sourceMapper = new SourceMapper("");
/*     */   private static List<String> excludes;
/*     */   private static final int SOURCE_CACHE_SIZE = 5;
/*  55 */   private static List<SourceCode> sourceCache = new LinkedList();
/*     */ 
/*  57 */   private static HashMap<String, Value> savedValues = new HashMap();
/*     */   private static Method atExitMethod;
/*     */ 
/*     */   static void init(String paramString, boolean paramBoolean, int paramInt)
/*     */   {
/*  61 */     connection = new VMConnection(paramString, paramInt);
/*  62 */     if ((!connection.isLaunch()) || (paramBoolean))
/*  63 */       connection.open();
/*     */   }
/*     */ 
/*     */   static VMConnection connection()
/*     */   {
/*  68 */     return connection;
/*     */   }
/*     */ 
/*     */   static VirtualMachine vm() {
/*  72 */     return connection.vm();
/*     */   }
/*     */ 
/*     */   static void shutdown() {
/*  76 */     shutdown(null);
/*     */   }
/*     */ 
/*     */   static void shutdown(String paramString) {
/*  80 */     if (connection != null) {
/*     */       try {
/*  82 */         connection.disposeVM();
/*     */       }
/*     */       catch (VMDisconnectedException localVMDisconnectedException)
/*     */       {
/*     */       }
/*     */     }
/*  88 */     if (paramString != null) {
/*  89 */       MessageOutput.lnprint(paramString);
/*  90 */       MessageOutput.println();
/*     */     }
/*  92 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   static void setSourcePath(String paramString) {
/*  96 */     sourceMapper = new SourceMapper(paramString);
/*  97 */     sourceCache.clear();
/*     */   }
/*     */ 
/*     */   static void setSourcePath(List<String> paramList) {
/* 101 */     sourceMapper = new SourceMapper(paramList);
/* 102 */     sourceCache.clear();
/*     */   }
/*     */ 
/*     */   static String getSourcePath() {
/* 106 */     return sourceMapper.getSourcePath();
/*     */   }
/*     */ 
/*     */   private static List<String> excludes() {
/* 110 */     if (excludes == null) {
/* 111 */       setExcludes("java.*, javax.*, sun.*, com.sun.*");
/*     */     }
/* 113 */     return excludes;
/*     */   }
/*     */ 
/*     */   static String excludesString() {
/* 117 */     StringBuffer localStringBuffer = new StringBuffer();
/* 118 */     for (String str : excludes()) {
/* 119 */       localStringBuffer.append(str);
/* 120 */       localStringBuffer.append(",");
/*     */     }
/* 122 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static void addExcludes(StepRequest paramStepRequest) {
/* 126 */     for (String str : excludes())
/* 127 */       paramStepRequest.addClassExclusionFilter(str);
/*     */   }
/*     */ 
/*     */   static void addExcludes(MethodEntryRequest paramMethodEntryRequest)
/*     */   {
/* 132 */     for (String str : excludes())
/* 133 */       paramMethodEntryRequest.addClassExclusionFilter(str);
/*     */   }
/*     */ 
/*     */   static void addExcludes(MethodExitRequest paramMethodExitRequest)
/*     */   {
/* 138 */     for (String str : excludes())
/* 139 */       paramMethodExitRequest.addClassExclusionFilter(str);
/*     */   }
/*     */ 
/*     */   static void setExcludes(String paramString)
/*     */   {
/* 144 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ,;");
/* 145 */     ArrayList localArrayList = new ArrayList();
/* 146 */     while (localStringTokenizer.hasMoreTokens()) {
/* 147 */       localArrayList.add(localStringTokenizer.nextToken());
/*     */     }
/* 149 */     excludes = localArrayList;
/*     */   }
/*     */ 
/*     */   static Method atExitMethod() {
/* 153 */     return atExitMethod;
/*     */   }
/*     */ 
/*     */   static void setAtExitMethod(Method paramMethod) {
/* 157 */     atExitMethod = paramMethod;
/*     */   }
/*     */ 
/*     */   static BufferedReader sourceReader(Location paramLocation)
/*     */   {
/* 166 */     return sourceMapper.sourceReader(paramLocation);
/*     */   }
/*     */ 
/*     */   static synchronized String sourceLine(Location paramLocation, int paramInt) throws IOException
/*     */   {
/* 171 */     if (paramInt == -1) {
/* 172 */       throw new IllegalArgumentException();
/*     */     }
/*     */     try
/*     */     {
/* 176 */       String str = paramLocation.sourceName();
/*     */ 
/* 178 */       Iterator localIterator = sourceCache.iterator();
/* 179 */       Object localObject1 = null;
/*     */       Object localObject2;
/* 180 */       while (localIterator.hasNext()) {
/* 181 */         localObject2 = (SourceCode)localIterator.next();
/* 182 */         if (((SourceCode)localObject2).fileName().equals(str)) {
/* 183 */           localObject1 = localObject2;
/* 184 */           localIterator.remove();
/* 185 */           break;
/*     */         }
/*     */       }
/* 188 */       if (localObject1 == null) {
/* 189 */         localObject2 = sourceReader(paramLocation);
/* 190 */         if (localObject2 == null) {
/* 191 */           throw new FileNotFoundException(str);
/*     */         }
/* 193 */         localObject1 = new SourceCode(str, (BufferedReader)localObject2);
/* 194 */         if (sourceCache.size() == 5) {
/* 195 */           sourceCache.remove(sourceCache.size() - 1);
/*     */         }
/*     */       }
/* 198 */       sourceCache.add(0, localObject1);
/* 199 */       return ((SourceCode)localObject1).sourceLine(paramInt); } catch (AbsentInformationException localAbsentInformationException) {
/*     */     }
/* 201 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   static String description(ObjectReference paramObjectReference)
/*     */   {
/* 207 */     ReferenceType localReferenceType = paramObjectReference.referenceType();
/* 208 */     long l = paramObjectReference.uniqueID();
/* 209 */     if (localReferenceType == null) {
/* 210 */       return toHex(l);
/*     */     }
/* 212 */     return MessageOutput.format("object description and hex id", new Object[] { localReferenceType
/* 213 */       .name(), 
/* 214 */       toHex(l) });
/*     */   }
/*     */ 
/*     */   static String toHex(long paramLong)
/*     */   {
/* 220 */     char[] arrayOfChar1 = new char[16];
/* 221 */     char[] arrayOfChar2 = new char[18];
/*     */ 
/* 224 */     int i = 0;
/*     */     do {
/* 226 */       long l = paramLong & 0xF;
/* 227 */       arrayOfChar1[(i++)] = ((char)(int)(l < 10L ? 48L + l : 97L + l - 10L));
/* 228 */     }while (paramLong >>>= 4 > 0L);
/*     */ 
/* 231 */     arrayOfChar2[0] = '0';
/* 232 */     arrayOfChar2[1] = 'x';
/* 233 */     int j = 2;
/*     */     while (true) { i--; if (i < 0) break;
/* 235 */       arrayOfChar2[(j++)] = arrayOfChar1[i];
/*     */     }
/* 237 */     return new String(arrayOfChar2, 0, j);
/*     */   }
/*     */ 
/*     */   static long fromHex(String paramString)
/*     */   {
/* 243 */     String str = paramString.startsWith("0x") ? paramString
/* 243 */       .substring(2)
/* 243 */       .toLowerCase() : paramString.toLowerCase();
/* 244 */     if (paramString.length() == 0) {
/* 245 */       throw new NumberFormatException();
/*     */     }
/*     */ 
/* 248 */     long l = 0L;
/* 249 */     for (int i = 0; i < str.length(); i++) {
/* 250 */       int j = str.charAt(i);
/* 251 */       if ((j >= 48) && (j <= 57))
/* 252 */         l = l * 16L + (j - 48);
/* 253 */       else if ((j >= 97) && (j <= 102))
/* 254 */         l = l * 16L + (j - 97 + 10);
/*     */       else {
/* 256 */         throw new NumberFormatException();
/*     */       }
/*     */     }
/* 259 */     return l;
/*     */   }
/*     */ 
/*     */   static ReferenceType getReferenceTypeFromToken(String paramString) {
/* 263 */     Object localObject1 = null;
/* 264 */     if (Character.isDigit(paramString.charAt(0))) {
/* 265 */       localObject1 = null;
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject2;
/* 266 */       if (paramString.startsWith("*."))
/*     */       {
/* 271 */         paramString = paramString.substring(1);
/* 272 */         for (localObject2 = vm().allClasses().iterator(); ((Iterator)localObject2).hasNext(); ) { ReferenceType localReferenceType = (ReferenceType)((Iterator)localObject2).next();
/* 273 */           if (localReferenceType.name().endsWith(paramString)) {
/* 274 */             localObject1 = localReferenceType;
/* 275 */             break;
/*     */           } }
/*     */       }
/*     */       else
/*     */       {
/* 280 */         localObject2 = vm().classesByName(paramString);
/* 281 */         if (((List)localObject2).size() > 0)
/*     */         {
/* 283 */           localObject1 = (ReferenceType)((List)localObject2).get(0);
/*     */         }
/*     */       }
/*     */     }
/* 286 */     return localObject1;
/*     */   }
/*     */ 
/*     */   static Set<String> getSaveKeys() {
/* 290 */     return savedValues.keySet();
/*     */   }
/*     */ 
/*     */   static Value getSavedValue(String paramString) {
/* 294 */     return (Value)savedValues.get(paramString);
/*     */   }
/*     */ 
/*     */   static void setSavedValue(String paramString, Value paramValue) {
/* 298 */     savedValues.put(paramString, paramValue);
/*     */   }
/*     */ 
/*     */   static class SourceCode
/*     */   {
/*     */     private String fileName;
/* 303 */     private List<String> sourceLines = new ArrayList();
/*     */ 
/*     */     SourceCode(String paramString, BufferedReader paramBufferedReader) throws IOException {
/* 306 */       this.fileName = paramString;
/*     */       try {
/* 308 */         String str = paramBufferedReader.readLine();
/* 309 */         while (str != null) {
/* 310 */           this.sourceLines.add(str);
/* 311 */           str = paramBufferedReader.readLine();
/*     */         }
/*     */       } finally {
/* 314 */         paramBufferedReader.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     String fileName() {
/* 319 */       return this.fileName;
/*     */     }
/*     */ 
/*     */     String sourceLine(int paramInt) {
/* 323 */       int i = paramInt - 1;
/* 324 */       if (i >= this.sourceLines.size()) {
/* 325 */         return null;
/*     */       }
/* 327 */       return (String)this.sourceLines.get(i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.Env
 * JD-Core Version:    0.6.2
 */