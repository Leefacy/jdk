/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ConcreteMethodImpl extends MethodImpl
/*     */ {
/*  74 */   private Location location = null;
/*     */   private SoftReference<SoftLocationXRefs> softBaseLocationXRefsRef;
/*     */   private SoftReference<SoftLocationXRefs> softOtherLocationXRefsRef;
/*  77 */   private SoftReference<List<LocalVariable>> variablesRef = null;
/*  78 */   private boolean absentVariableInformation = false;
/*  79 */   private long firstIndex = -1L;
/*  80 */   private long lastIndex = -1L;
/*  81 */   private SoftReference<byte[]> bytecodesRef = null;
/*  82 */   private int argSlotCount = -1;
/*     */ 
/*     */   ConcreteMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt)
/*     */   {
/*  90 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   public Location location()
/*     */   {
/*  95 */     if (this.location == null) {
/*  96 */       getBaseLocations();
/*     */     }
/*  98 */     return this.location;
/*     */   }
/*     */ 
/*     */   List<Location> sourceNameFilter(List<Location> paramList, SDE.Stratum paramStratum, String paramString)
/*     */     throws AbsentInformationException
/*     */   {
/* 105 */     if (paramString == null) {
/* 106 */       return paramList;
/*     */     }
/*     */ 
/* 109 */     ArrayList localArrayList = new ArrayList();
/* 110 */     for (Location localLocation : paramList) {
/* 111 */       if (((LocationImpl)localLocation).sourceName(paramStratum).equals(paramString)) {
/* 112 */         localArrayList.add(localLocation);
/*     */       }
/*     */     }
/* 115 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   List<Location> allLineLocations(SDE.Stratum paramStratum, String paramString)
/*     */     throws AbsentInformationException
/*     */   {
/* 122 */     List localList = getLocations(paramStratum).lineLocations;
/*     */ 
/* 124 */     if (localList.size() == 0) {
/* 125 */       throw new AbsentInformationException();
/*     */     }
/*     */ 
/* 128 */     return Collections.unmodifiableList(
/* 129 */       sourceNameFilter(localList, paramStratum, paramString));
/*     */   }
/*     */ 
/*     */   List<Location> locationsOfLine(SDE.Stratum paramStratum, String paramString, int paramInt)
/*     */     throws AbsentInformationException
/*     */   {
/* 136 */     SoftLocationXRefs localSoftLocationXRefs = getLocations(paramStratum);
/*     */ 
/* 138 */     if (localSoftLocationXRefs.lineLocations.size() == 0) {
/* 139 */       throw new AbsentInformationException();
/*     */     }
/*     */ 
/* 146 */     Object localObject = (List)localSoftLocationXRefs.lineMapper.get(new Integer(paramInt));
/*     */ 
/* 148 */     if (localObject == null) {
/* 149 */       localObject = new ArrayList(0);
/*     */     }
/* 151 */     return Collections.unmodifiableList(
/* 152 */       sourceNameFilter((List)localObject, paramStratum, paramString));
/*     */   }
/*     */ 
/*     */   public Location locationOfCodeIndex(long paramLong)
/*     */   {
/* 157 */     if (this.firstIndex == -1L) {
/* 158 */       getBaseLocations();
/*     */     }
/*     */ 
/* 164 */     if ((paramLong < this.firstIndex) || (paramLong > this.lastIndex)) {
/* 165 */       return null;
/*     */     }
/*     */ 
/* 168 */     return new LocationImpl(virtualMachine(), this, paramLong);
/*     */   }
/*     */ 
/*     */   LineInfo codeIndexToLineInfo(SDE.Stratum paramStratum, long paramLong)
/*     */   {
/* 174 */     if (this.firstIndex == -1L) {
/* 175 */       getBaseLocations();
/*     */     }
/*     */ 
/* 181 */     if ((paramLong < this.firstIndex) || (paramLong > this.lastIndex)) {
/* 182 */       throw new InternalError("Location with invalid code index");
/*     */     }
/*     */ 
/* 186 */     List localList = getLocations(paramStratum).lineLocations;
/*     */ 
/* 191 */     if (localList.size() == 0) {
/* 192 */       return super.codeIndexToLineInfo(paramStratum, paramLong);
/*     */     }
/*     */ 
/* 195 */     Iterator localIterator = localList.iterator();
/*     */ 
/* 204 */     Object localObject = (LocationImpl)localIterator.next();
/* 205 */     while (localIterator.hasNext()) {
/* 206 */       LocationImpl localLocationImpl = (LocationImpl)localIterator.next();
/* 207 */       if (localLocationImpl.codeIndex() > paramLong) {
/*     */         break;
/*     */       }
/* 210 */       localObject = localLocationImpl;
/*     */     }
/* 212 */     return ((LocationImpl)localObject).getLineInfo(paramStratum);
/*     */   }
/*     */ 
/*     */   public List<LocalVariable> variables() throws AbsentInformationException
/*     */   {
/* 217 */     return getVariables();
/*     */   }
/*     */ 
/*     */   public List<LocalVariable> variablesByName(String paramString) throws AbsentInformationException {
/* 221 */     List localList = getVariables();
/*     */ 
/* 223 */     ArrayList localArrayList = new ArrayList(2);
/* 224 */     Iterator localIterator = localList.iterator();
/* 225 */     while (localIterator.hasNext()) {
/* 226 */       LocalVariable localLocalVariable = (LocalVariable)localIterator.next();
/* 227 */       if (localLocalVariable.name().equals(paramString)) {
/* 228 */         localArrayList.add(localLocalVariable);
/*     */       }
/*     */     }
/* 231 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public List<LocalVariable> arguments() throws AbsentInformationException {
/* 235 */     List localList = getVariables();
/*     */ 
/* 237 */     ArrayList localArrayList = new ArrayList(localList.size());
/* 238 */     Iterator localIterator = localList.iterator();
/* 239 */     while (localIterator.hasNext()) {
/* 240 */       LocalVariable localLocalVariable = (LocalVariable)localIterator.next();
/* 241 */       if (localLocalVariable.isArgument()) {
/* 242 */         localArrayList.add(localLocalVariable);
/*     */       }
/*     */     }
/* 245 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public byte[] bytecodes()
/*     */   {
/* 250 */     byte[] arrayOfByte = this.bytecodesRef == null ? null : 
/* 250 */       (byte[])this.bytecodesRef
/* 250 */       .get();
/* 251 */     if (arrayOfByte == null)
/*     */     {
/*     */       try {
/* 254 */         arrayOfByte = JDWP.Method.Bytecodes.process(this.vm, this.declaringType, this.ref).bytes;
/*     */       }
/*     */       catch (JDWPException localJDWPException) {
/* 256 */         throw localJDWPException.toJDIException();
/*     */       }
/* 258 */       this.bytecodesRef = new SoftReference(arrayOfByte);
/*     */     }
/*     */ 
/* 265 */     return (byte[])arrayOfByte.clone();
/*     */   }
/*     */ 
/*     */   int argSlotCount() throws AbsentInformationException {
/* 269 */     if (this.argSlotCount == -1) {
/* 270 */       getVariables();
/*     */     }
/* 272 */     return this.argSlotCount;
/*     */   }
/*     */ 
/*     */   private SoftLocationXRefs getLocations(SDE.Stratum paramStratum) {
/* 276 */     if (paramStratum.isJava()) {
/* 277 */       return getBaseLocations();
/*     */     }
/* 279 */     String str = paramStratum.id();
/*     */ 
/* 282 */     SoftLocationXRefs localSoftLocationXRefs = this.softOtherLocationXRefsRef == null ? null : 
/* 282 */       (SoftLocationXRefs)this.softOtherLocationXRefsRef
/* 282 */       .get();
/* 283 */     if ((localSoftLocationXRefs != null) && (localSoftLocationXRefs.stratumID.equals(str))) {
/* 284 */       return localSoftLocationXRefs;
/*     */     }
/*     */ 
/* 287 */     ArrayList localArrayList = new ArrayList();
/* 288 */     HashMap localHashMap = new HashMap();
/* 289 */     int i = -1;
/* 290 */     int j = -1;
/* 291 */     Object localObject1 = null;
/*     */ 
/* 293 */     SDE.Stratum localStratum = this.declaringType
/* 293 */       .stratum("Java");
/*     */ 
/* 294 */     Iterator localIterator = getBaseLocations().lineLocations.iterator();
/* 295 */     while (localIterator.hasNext()) {
/* 296 */       LocationImpl localLocationImpl = (LocationImpl)localIterator.next();
/* 297 */       int k = localLocationImpl.lineNumber(localStratum);
/*     */ 
/* 299 */       SDE.LineStratum localLineStratum = paramStratum
/* 299 */         .lineStratum(this.declaringType, k);
/*     */ 
/* 302 */       if (localLineStratum != null)
/*     */       {
/* 307 */         int m = localLineStratum.lineNumber();
/*     */ 
/* 310 */         if ((m != -1) && 
/* 311 */           (!localLineStratum
/* 311 */           .equals(localObject1)))
/*     */         {
/* 312 */           localObject1 = localLineStratum;
/*     */ 
/* 315 */           if (m > j) {
/* 316 */             j = m;
/*     */           }
/* 318 */           if ((m < i) || (i == -1)) {
/* 319 */             i = m;
/*     */           }
/*     */ 
/* 322 */           localLocationImpl.addStratumLineInfo(new StratumLineInfo(str, m, localLineStratum
/* 325 */             .sourceName(), localLineStratum
/* 326 */             .sourcePath()));
/*     */ 
/* 329 */           localArrayList.add(localLocationImpl);
/*     */ 
/* 332 */           Integer localInteger = new Integer(m);
/* 333 */           Object localObject2 = (List)localHashMap.get(localInteger);
/* 334 */           if (localObject2 == null) {
/* 335 */             localObject2 = new ArrayList(1);
/* 336 */             localHashMap.put(localInteger, localObject2);
/*     */           }
/* 338 */           ((List)localObject2).add(localLocationImpl);
/*     */         }
/*     */       }
/*     */     }
/* 342 */     localSoftLocationXRefs = new SoftLocationXRefs(str, localHashMap, localArrayList, i, j);
/*     */ 
/* 345 */     this.softOtherLocationXRefsRef = new SoftReference(localSoftLocationXRefs);
/* 346 */     return localSoftLocationXRefs;
/*     */   }
/*     */ 
/*     */   private SoftLocationXRefs getBaseLocations()
/*     */   {
/* 351 */     SoftLocationXRefs localSoftLocationXRefs = this.softBaseLocationXRefsRef == null ? null : 
/* 351 */       (SoftLocationXRefs)this.softBaseLocationXRefsRef
/* 351 */       .get();
/* 352 */     if (localSoftLocationXRefs != null) {
/* 353 */       return localSoftLocationXRefs;
/*     */     }
/*     */ 
/* 356 */     JDWP.Method.LineTable localLineTable = null;
/*     */     try {
/* 358 */       localLineTable = JDWP.Method.LineTable.process(this.vm, this.declaringType, this.ref);
/*     */     }
/*     */     catch (JDWPException localJDWPException)
/*     */     {
/* 364 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 367 */     int i = localLineTable.lines.length;
/*     */ 
/* 369 */     ArrayList localArrayList = new ArrayList(i);
/* 370 */     HashMap localHashMap = new HashMap();
/* 371 */     int j = -1;
/* 372 */     int k = -1;
/* 373 */     for (int m = 0; m < i; m++) {
/* 374 */       long l = localLineTable.lines[m].lineCodeIndex;
/* 375 */       int n = localLineTable.lines[m].lineNumber;
/*     */ 
/* 385 */       if ((m + 1 == i) || (l != localLineTable.lines[(m + 1)].lineCodeIndex))
/*     */       {
/* 387 */         if (n > k) {
/* 388 */           k = n;
/*     */         }
/* 390 */         if ((n < j) || (j == -1)) {
/* 391 */           j = n;
/*     */         }
/*     */ 
/* 394 */         LocationImpl localLocationImpl = new LocationImpl(
/* 394 */           virtualMachine(), this, l);
/* 395 */         localLocationImpl.addBaseLineInfo(new BaseLineInfo(n, this.declaringType));
/*     */ 
/* 399 */         localArrayList.add(localLocationImpl);
/*     */ 
/* 402 */         Integer localInteger = new Integer(n);
/* 403 */         Object localObject = (List)localHashMap.get(localInteger);
/* 404 */         if (localObject == null) {
/* 405 */           localObject = new ArrayList(1);
/* 406 */           localHashMap.put(localInteger, localObject);
/*     */         }
/* 408 */         ((List)localObject).add(localLocationImpl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 416 */     if (this.location == null) {
/* 417 */       this.firstIndex = localLineTable.start;
/* 418 */       this.lastIndex = localLineTable.end;
/*     */ 
/* 425 */       if (i > 0)
/* 426 */         this.location = ((Location)localArrayList.get(0));
/*     */       else {
/* 428 */         this.location = new LocationImpl(virtualMachine(), this, this.firstIndex);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 433 */     localSoftLocationXRefs = new SoftLocationXRefs("Java", localHashMap, localArrayList, j, k);
/*     */ 
/* 436 */     this.softBaseLocationXRefsRef = new SoftReference(localSoftLocationXRefs);
/* 437 */     return localSoftLocationXRefs;
/*     */   }
/*     */ 
/*     */   private List<LocalVariable> getVariables1_4() throws AbsentInformationException {
/* 441 */     JDWP.Method.VariableTable localVariableTable = null;
/*     */     try
/*     */     {
/* 444 */       localVariableTable = JDWP.Method.VariableTable.process(this.vm, this.declaringType, this.ref);
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 446 */       if (localJDWPException.errorCode() == 101) {
/* 447 */         this.absentVariableInformation = true;
/* 448 */         throw new AbsentInformationException();
/*     */       }
/* 450 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 455 */     this.argSlotCount = localVariableTable.argCnt;
/* 456 */     int i = localVariableTable.slots.length;
/* 457 */     ArrayList localArrayList = new ArrayList(i);
/* 458 */     for (int j = 0; j < i; j++) {
/* 459 */       JDWP.Method.VariableTable.SlotInfo localSlotInfo = localVariableTable.slots[j];
/*     */ 
/* 465 */       if ((!localSlotInfo.name.startsWith("this$")) && (!localSlotInfo.name.equals("this"))) {
/* 466 */         LocationImpl localLocationImpl1 = new LocationImpl(virtualMachine(), this, localSlotInfo.codeIndex);
/*     */ 
/* 469 */         LocationImpl localLocationImpl2 = new LocationImpl(
/* 469 */           virtualMachine(), this, localSlotInfo.codeIndex + localSlotInfo.length - 1L);
/*     */ 
/* 472 */         LocalVariableImpl localLocalVariableImpl = new LocalVariableImpl(
/* 472 */           virtualMachine(), this, localSlotInfo.slot, localLocationImpl1, localLocationImpl2, localSlotInfo.name, localSlotInfo.signature, null);
/*     */ 
/* 476 */         localArrayList.add(localLocalVariableImpl);
/*     */       }
/*     */     }
/* 479 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private List<LocalVariable> getVariables1() throws AbsentInformationException
/*     */   {
/* 484 */     if (!this.vm.canGet1_5LanguageFeatures()) {
/* 485 */       return getVariables1_4();
/*     */     }
/*     */ 
/* 488 */     JDWP.Method.VariableTableWithGeneric localVariableTableWithGeneric = null;
/*     */     try
/*     */     {
/* 491 */       localVariableTableWithGeneric = JDWP.Method.VariableTableWithGeneric.process(this.vm, this.declaringType, this.ref);
/*     */     }
/*     */     catch (JDWPException localJDWPException) {
/* 493 */       if (localJDWPException.errorCode() == 101) {
/* 494 */         this.absentVariableInformation = true;
/* 495 */         throw new AbsentInformationException();
/*     */       }
/* 497 */       throw localJDWPException.toJDIException();
/*     */     }
/*     */ 
/* 502 */     this.argSlotCount = localVariableTableWithGeneric.argCnt;
/* 503 */     int i = localVariableTableWithGeneric.slots.length;
/* 504 */     ArrayList localArrayList = new ArrayList(i);
/* 505 */     for (int j = 0; j < i; j++) {
/* 506 */       JDWP.Method.VariableTableWithGeneric.SlotInfo localSlotInfo = localVariableTableWithGeneric.slots[j];
/*     */ 
/* 512 */       if ((!localSlotInfo.name.startsWith("this$")) && (!localSlotInfo.name.equals("this"))) {
/* 513 */         LocationImpl localLocationImpl1 = new LocationImpl(virtualMachine(), this, localSlotInfo.codeIndex);
/*     */ 
/* 516 */         LocationImpl localLocationImpl2 = new LocationImpl(
/* 516 */           virtualMachine(), this, localSlotInfo.codeIndex + localSlotInfo.length - 1L);
/*     */ 
/* 519 */         LocalVariableImpl localLocalVariableImpl = new LocalVariableImpl(
/* 519 */           virtualMachine(), this, localSlotInfo.slot, localLocationImpl1, localLocationImpl2, localSlotInfo.name, localSlotInfo.signature, localSlotInfo.genericSignature);
/*     */ 
/* 524 */         localArrayList.add(localLocalVariableImpl);
/*     */       }
/*     */     }
/* 527 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private List<LocalVariable> getVariables() throws AbsentInformationException {
/* 531 */     if (this.absentVariableInformation) {
/* 532 */       throw new AbsentInformationException();
/*     */     }
/*     */ 
/* 536 */     List localList = this.variablesRef == null ? null : 
/* 536 */       (List)this.variablesRef
/* 536 */       .get();
/* 537 */     if (localList != null) {
/* 538 */       return localList;
/*     */     }
/* 540 */     localList = getVariables1();
/* 541 */     localList = Collections.unmodifiableList(localList);
/* 542 */     this.variablesRef = new SoftReference(localList);
/* 543 */     return localList;
/*     */   }
/*     */ 
/*     */   private static class SoftLocationXRefs
/*     */   {
/*     */     final String stratumID;
/*     */     final Map<Integer, List<Location>> lineMapper;
/*     */     final List<Location> lineLocations;
/*     */     final int lowestLine;
/*     */     final int highestLine;
/*     */ 
/*     */     SoftLocationXRefs(String paramString, Map<Integer, List<Location>> paramMap, List<Location> paramList, int paramInt1, int paramInt2)
/*     */     {
/*  65 */       this.stratumID = paramString;
/*  66 */       this.lineMapper = Collections.unmodifiableMap(paramMap);
/*  67 */       this.lineLocations = 
/*  68 */         Collections.unmodifiableList(paramList);
/*     */ 
/*  69 */       this.lowestLine = paramInt1;
/*  70 */       this.highestLine = paramInt2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ConcreteMethodImpl
 * JD-Core Version:    0.6.2
 */