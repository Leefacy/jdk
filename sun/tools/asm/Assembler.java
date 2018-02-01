/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.javac.SourceClass;
/*     */ 
/*     */ public final class Assembler
/*     */   implements Constants
/*     */ {
/*     */   static final int NOTREACHED = 0;
/*     */   static final int REACHED = 1;
/*     */   static final int NEEDED = 2;
/*  60 */   Label first = new Label();
/*  61 */   Instruction last = this.first;
/*     */   int maxdepth;
/*     */   int maxvar;
/*     */   int maxpc;
/*  94 */   static Vector<String> SourceClassList = new Vector();
/*     */ 
/*  96 */   static Vector<String> TmpCovTable = new Vector();
/*     */ 
/*  98 */   static int[] JcovClassCountArray = new int[9];
/*     */ 
/* 100 */   static String JcovMagicLine = "JCOV-DATA-FILE-VERSION: 2.0";
/* 101 */   static String JcovClassLine = "CLASS: ";
/* 102 */   static String JcovSrcfileLine = "SRCFILE: ";
/* 103 */   static String JcovTimestampLine = "TIMESTAMP: ";
/* 104 */   static String JcovDataLine = "DATA: ";
/* 105 */   static String JcovHeadingLine = "#kind\tcount";
/*     */ 
/* 107 */   static int[] arrayModifiers = { 1, 2, 4, 1024, 16, 512 };
/*     */ 
/* 109 */   static int[] arrayModifiersOpc = { 121, 120, 122, 130, 128, 114 };
/*     */ 
/*     */   public void add(Instruction paramInstruction)
/*     */   {
/*  70 */     if (paramInstruction != null) {
/*  71 */       this.last.next = paramInstruction;
/*  72 */       this.last = paramInstruction;
/*     */     }
/*     */   }
/*     */ 
/*  76 */   public void add(long paramLong, int paramInt) { add(new Instruction(paramLong, paramInt, null)); }
/*     */ 
/*     */   public void add(long paramLong, int paramInt, Object paramObject) {
/*  79 */     add(new Instruction(paramLong, paramInt, paramObject));
/*     */   }
/*     */ 
/*     */   public void add(long paramLong, int paramInt, Object paramObject, boolean paramBoolean) {
/*  83 */     add(new Instruction(paramLong, paramInt, paramObject, paramBoolean));
/*     */   }
/*     */ 
/*     */   public void add(boolean paramBoolean, long paramLong, int paramInt, Object paramObject) {
/*  87 */     add(new Instruction(paramBoolean, paramLong, paramInt, paramObject));
/*     */   }
/*     */ 
/*     */   public void add(long paramLong, int paramInt, boolean paramBoolean) {
/*  91 */     add(new Instruction(paramLong, paramInt, paramBoolean));
/*     */   }
/*     */ 
/*     */   void optimize(Environment paramEnvironment, Label paramLabel)
/*     */   {
/* 117 */     paramLabel.pc = 1;
/*     */ 
/* 119 */     for (Instruction localInstruction = paramLabel.next; localInstruction != null; localInstruction = localInstruction.next) {
/* 120 */       switch (localInstruction.pc) {
/*     */       case 0:
/* 122 */         localInstruction.optimize(paramEnvironment);
/* 123 */         localInstruction.pc = 1;
/* 124 */         break;
/*     */       case 1:
/*     */         return;
/*     */       case 2:
/*     */       }
/*     */       Object localObject;
/*     */       Enumeration localEnumeration;
/* 131 */       switch (localInstruction.opc) {
/*     */       case -2:
/*     */       case -1:
/* 134 */         if (localInstruction.pc == 1)
/* 135 */           localInstruction.pc = 0; break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 198:
/*     */       case 199:
/* 155 */         optimize(paramEnvironment, (Label)localInstruction.value);
/* 156 */         break;
/*     */       case 167:
/* 159 */         optimize(paramEnvironment, (Label)localInstruction.value);
/* 160 */         return;
/*     */       case 168:
/* 163 */         optimize(paramEnvironment, (Label)localInstruction.value);
/* 164 */         break;
/*     */       case 169:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
/*     */       case 191:
/* 174 */         return;
/*     */       case 170:
/*     */       case 171:
/* 178 */         localObject = (SwitchData)localInstruction.value;
/* 179 */         optimize(paramEnvironment, ((SwitchData)localObject).defaultLabel);
/* 180 */         for (localEnumeration = ((SwitchData)localObject).tab.elements(); localEnumeration.hasMoreElements(); ) {
/* 181 */           optimize(paramEnvironment, (Label)localEnumeration.nextElement());
/*     */         }
/* 183 */         return;
/*     */       case -3:
/* 187 */         localObject = (TryData)localInstruction.value;
/* 188 */         ((TryData)localObject).getEndLabel().pc = 2;
/* 189 */         for (localEnumeration = ((TryData)localObject).catches.elements(); localEnumeration.hasMoreElements(); ) {
/* 190 */           CatchData localCatchData = (CatchData)localEnumeration.nextElement();
/* 191 */           optimize(paramEnvironment, localCatchData.getLabel());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean eliminate()
/*     */   {
/* 203 */     boolean bool = false;
/* 204 */     Object localObject = this.first;
/*     */ 
/* 206 */     for (Instruction localInstruction = this.first.next; localInstruction != null; localInstruction = localInstruction.next) {
/* 207 */       if (localInstruction.pc != 0) {
/* 208 */         ((Instruction)localObject).next = localInstruction;
/* 209 */         localObject = localInstruction;
/* 210 */         localInstruction.pc = 0;
/*     */       } else {
/* 212 */         bool = true;
/*     */       }
/*     */     }
/* 215 */     this.first.pc = 0;
/* 216 */     ((Instruction)localObject).next = null;
/* 217 */     return bool;
/*     */   }
/*     */ 
/*     */   public void optimize(Environment paramEnvironment)
/*     */   {
/*     */     do {
/* 227 */       optimize(paramEnvironment, this.first);
/*     */     }
/*     */ 
/* 230 */     while ((eliminate()) && (paramEnvironment.opt()));
/*     */   }
/*     */ 
/*     */   public void collect(Environment paramEnvironment, MemberDefinition paramMemberDefinition, ConstantPool paramConstantPool)
/*     */   {
/*     */     Enumeration localEnumeration;
/* 239 */     if ((paramMemberDefinition != null) && (paramEnvironment.debug_vars()))
/*     */     {
/* 241 */       localObject = paramMemberDefinition.getArguments();
/* 242 */       if (localObject != null) {
/* 243 */         for (localEnumeration = ((Vector)localObject).elements(); localEnumeration.hasMoreElements(); ) {
/* 244 */           MemberDefinition localMemberDefinition = (MemberDefinition)localEnumeration.nextElement();
/* 245 */           paramConstantPool.put(localMemberDefinition.getName().toString());
/* 246 */           paramConstantPool.put(localMemberDefinition.getType().getTypeSignature());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 252 */     for (Object localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next)
/* 253 */       ((Instruction)localObject).collect(paramConstantPool);
/*     */   }
/*     */ 
/*     */   void balance(Label paramLabel, int paramInt)
/*     */   {
/* 261 */     for (Object localObject1 = paramLabel; localObject1 != null; localObject1 = ((Instruction)localObject1).next)
/*     */     {
/* 264 */       paramInt += ((Instruction)localObject1).balance();
/* 265 */       if (paramInt < 0) {
/* 266 */         throw new CompilerError("stack under flow: " + ((Instruction)localObject1).toString() + " = " + paramInt);
/*     */       }
/* 268 */       if (paramInt > this.maxdepth)
/* 269 */         this.maxdepth = paramInt;
/*     */       int i;
/*     */       Object localObject2;
/*     */       Enumeration localEnumeration;
/* 271 */       switch (((Instruction)localObject1).opc) {
/*     */       case -1:
/* 273 */         paramLabel = (Label)localObject1;
/* 274 */         if (((Instruction)localObject1).pc == 1) {
/* 275 */           if (paramLabel.depth != paramInt)
/*     */           {
/* 278 */             throw new CompilerError("stack depth error " + paramInt + "/" + paramLabel.depth + ": " + ((Instruction)localObject1)
/* 278 */               .toString());
/*     */           }
/* 280 */           return;
/*     */         }
/* 282 */         paramLabel.pc = 1;
/* 283 */         paramLabel.depth = paramInt;
/* 284 */         break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 198:
/*     */       case 199:
/* 302 */         balance((Label)((Instruction)localObject1).value, paramInt);
/* 303 */         break;
/*     */       case 167:
/* 306 */         balance((Label)((Instruction)localObject1).value, paramInt);
/* 307 */         return;
/*     */       case 168:
/* 310 */         balance((Label)((Instruction)localObject1).value, paramInt + 1);
/* 311 */         break;
/*     */       case 169:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
/*     */       case 191:
/* 321 */         return;
/*     */       case 21:
/*     */       case 23:
/*     */       case 25:
/*     */       case 54:
/*     */       case 56:
/*     */       case 58:
/* 330 */         i = ((((Instruction)localObject1).value instanceof Number) ? ((Number)((Instruction)localObject1).value)
/* 330 */           .intValue() : ((LocalVariable)((Instruction)localObject1).value).slot) + 1;
/*     */ 
/* 332 */         if (i > this.maxvar)
/* 333 */           this.maxvar = i; break;
/*     */       case 22:
/*     */       case 24:
/*     */       case 55:
/*     */       case 57:
/* 342 */         i = ((((Instruction)localObject1).value instanceof Number) ? ((Number)((Instruction)localObject1).value)
/* 342 */           .intValue() : ((LocalVariable)((Instruction)localObject1).value).slot) + 2;
/*     */ 
/* 344 */         if (i > this.maxvar)
/* 345 */           this.maxvar = i; break;
/*     */       case 132:
/* 350 */         i = ((int[])(int[])localObject1.value)[0] + 1;
/* 351 */         if (i > this.maxvar)
/* 352 */           this.maxvar = (i + 1); break;
/*     */       case 170:
/*     */       case 171:
/* 358 */         localObject2 = (SwitchData)((Instruction)localObject1).value;
/* 359 */         balance(((SwitchData)localObject2).defaultLabel, paramInt);
/* 360 */         for (localEnumeration = ((SwitchData)localObject2).tab.elements(); localEnumeration.hasMoreElements(); ) {
/* 361 */           balance((Label)localEnumeration.nextElement(), paramInt);
/*     */         }
/* 363 */         return;
/*     */       case -3:
/* 367 */         localObject2 = (TryData)((Instruction)localObject1).value;
/* 368 */         for (localEnumeration = ((TryData)localObject2).catches.elements(); localEnumeration.hasMoreElements(); ) {
/* 369 */           CatchData localCatchData = (CatchData)localEnumeration.nextElement();
/* 370 */           balance(localCatchData.getLabel(), paramInt + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, MemberDefinition paramMemberDefinition, ConstantPool paramConstantPool)
/*     */     throws IOException
/*     */   {
/* 386 */     if ((paramMemberDefinition != null) && (paramMemberDefinition.getArguments() != null)) {
/* 387 */       int i = 0;
/*     */ 
/* 389 */       Vector localVector = paramMemberDefinition.getArguments();
/* 390 */       for (localObject = localVector.elements(); ((Enumeration)localObject).hasMoreElements(); ) {
/* 391 */         MemberDefinition localMemberDefinition = (MemberDefinition)((Enumeration)localObject).nextElement();
/* 392 */         i += localMemberDefinition.getType().stackSize();
/*     */       }
/* 394 */       this.maxvar = i;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 399 */       balance(this.first, 0);
/*     */     } catch (CompilerError localCompilerError) {
/* 401 */       System.out.println("ERROR: " + localCompilerError);
/* 402 */       listing(System.out);
/* 403 */       throw localCompilerError;
/*     */     }
/*     */ 
/* 407 */     int j = 0; int k = 0;
/* 408 */     for (Object localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next) {
/* 409 */       ((Instruction)localObject).pc = j;
/* 410 */       int m = ((Instruction)localObject).size(paramConstantPool);
/* 411 */       if ((j < 65536) && (j + m >= 65536)) {
/* 412 */         paramEnvironment.error(((Instruction)localObject).where, "warn.method.too.long");
/*     */       }
/* 414 */       j += m;
/*     */ 
/* 416 */       if (((Instruction)localObject).opc == -3) {
/* 417 */         k += ((TryData)((Instruction)localObject).value).catches.size();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 422 */     paramDataOutputStream.writeShort(this.maxdepth);
/* 423 */     paramDataOutputStream.writeShort(this.maxvar);
/* 424 */     paramDataOutputStream.writeInt(this.maxpc = j);
/*     */ 
/* 427 */     for (localObject = this.first.next; localObject != null; localObject = ((Instruction)localObject).next) {
/* 428 */       ((Instruction)localObject).write(paramDataOutputStream, paramConstantPool);
/*     */     }
/*     */ 
/* 432 */     paramDataOutputStream.writeShort(k);
/* 433 */     if (k > 0)
/*     */     {
/* 435 */       writeExceptions(paramEnvironment, paramDataOutputStream, paramConstantPool, this.first, this.last);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeExceptions(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool, Instruction paramInstruction1, Instruction paramInstruction2)
/*     */     throws IOException
/*     */   {
/* 443 */     for (Object localObject = paramInstruction1; localObject != paramInstruction2.next; localObject = ((Instruction)localObject).next)
/* 444 */       if (((Instruction)localObject).opc == -3) {
/* 445 */         TryData localTryData = (TryData)((Instruction)localObject).value;
/* 446 */         writeExceptions(paramEnvironment, paramDataOutputStream, paramConstantPool, ((Instruction)localObject).next, localTryData.getEndLabel());
/* 447 */         for (Enumeration localEnumeration = localTryData.catches.elements(); localEnumeration.hasMoreElements(); ) {
/* 448 */           CatchData localCatchData = (CatchData)localEnumeration.nextElement();
/*     */ 
/* 450 */           paramDataOutputStream.writeShort(((Instruction)localObject).pc);
/* 451 */           paramDataOutputStream.writeShort(localTryData.getEndLabel().pc);
/* 452 */           paramDataOutputStream.writeShort(localCatchData.getLabel().pc);
/* 453 */           if (localCatchData.getType() != null)
/* 454 */             paramDataOutputStream.writeShort(paramConstantPool.index(localCatchData.getType()));
/*     */           else {
/* 456 */             paramDataOutputStream.writeShort(0);
/*     */           }
/*     */         }
/* 459 */         localObject = localTryData.getEndLabel();
/*     */       }
/*     */   }
/*     */ 
/*     */   public void writeCoverageTable(Environment paramEnvironment, ClassDefinition paramClassDefinition, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool, long paramLong)
/*     */     throws IOException
/*     */   {
/* 469 */     Vector localVector1 = new Vector();
/* 470 */     int i = 0;
/* 471 */     int j = 0;
/*     */ 
/* 473 */     long l1 = ((SourceClass)paramClassDefinition).getWhere();
/* 474 */     Vector localVector2 = new Vector();
/* 475 */     int k = 0;
/* 476 */     int m = 0;
/*     */     long l2;
/* 478 */     for (Object localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next) {
/* 479 */       l2 = ((Instruction)localObject).where >> 32;
/* 480 */       if ((l2 > 0L) && (((Instruction)localObject).opc != -1)) {
/* 481 */         if (j == 0) {
/* 482 */           if (l1 == ((Instruction)localObject).where)
/* 483 */             localVector1.addElement(new Cover(2, paramLong, ((Instruction)localObject).pc));
/*     */           else
/* 485 */             localVector1.addElement(new Cover(1, paramLong, ((Instruction)localObject).pc));
/* 486 */           m++;
/* 487 */           j = 1;
/*     */         }
/* 489 */         if ((i == 0) && (!((Instruction)localObject).flagNoCovered)) {
/* 490 */           int n = 0;
/* 491 */           for (Enumeration localEnumeration1 = localVector2.elements(); localEnumeration1.hasMoreElements(); ) {
/* 492 */             if (((Long)localEnumeration1.nextElement()).longValue() == ((Instruction)localObject).where) {
/* 493 */               n = 1;
/*     */             }
/*     */           }
/*     */ 
/* 497 */           if (n == 0) {
/* 498 */             localVector1.addElement(new Cover(3, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/* 499 */             m++;
/* 500 */             i = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */       SwitchData localSwitchData;
/* 504 */       switch (((Instruction)localObject).opc) {
/*     */       case -1:
/* 506 */         i = 0;
/* 507 */         break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 198:
/*     */       case 199:
/* 524 */         if (((Instruction)localObject).flagCondInverted) {
/* 525 */           localVector1.addElement(new Cover(7, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/* 526 */           localVector1.addElement(new Cover(8, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/*     */         } else {
/* 528 */           localVector1.addElement(new Cover(8, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/* 529 */           localVector1.addElement(new Cover(7, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/*     */         }
/* 531 */         m += 2;
/* 532 */         i = 0;
/* 533 */         break;
/*     */       case 167:
/* 537 */         i = 0;
/* 538 */         break;
/*     */       case 169:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
/*     */       case 191:
/* 549 */         break;
/*     */       case -3:
/* 553 */         localVector2.addElement(Long.valueOf(((Instruction)localObject).where));
/* 554 */         i = 0;
/* 555 */         break;
/*     */       case 170:
/* 559 */         localSwitchData = (SwitchData)((Instruction)localObject).value;
/* 560 */         for (int i1 = localSwitchData.minValue; i1 <= localSwitchData.maxValue; i1++) {
/* 561 */           localVector1.addElement(new Cover(5, localSwitchData.whereCase(new Integer(i1)), ((Instruction)localObject).pc));
/* 562 */           m++;
/*     */         }
/* 564 */         if (!localSwitchData.getDefault()) {
/* 565 */           localVector1.addElement(new Cover(6, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/* 566 */           m++;
/*     */         } else {
/* 568 */           localVector1.addElement(new Cover(5, localSwitchData.whereCase("default"), ((Instruction)localObject).pc));
/* 569 */           m++;
/*     */         }
/* 571 */         i = 0;
/* 572 */         break;
/*     */       case 171:
/* 575 */         localSwitchData = (SwitchData)((Instruction)localObject).value;
/* 576 */         for (Enumeration localEnumeration2 = localSwitchData.sortedKeys(); localEnumeration2.hasMoreElements(); ) {
/* 577 */           Integer localInteger = (Integer)localEnumeration2.nextElement();
/* 578 */           localVector1.addElement(new Cover(5, localSwitchData.whereCase(localInteger), ((Instruction)localObject).pc));
/* 579 */           m++;
/*     */         }
/* 581 */         if (!localSwitchData.getDefault()) {
/* 582 */           localVector1.addElement(new Cover(6, ((Instruction)localObject).where, ((Instruction)localObject).pc));
/* 583 */           m++;
/*     */         } else {
/* 585 */           localVector1.addElement(new Cover(5, localSwitchData.whereCase("default"), ((Instruction)localObject).pc));
/* 586 */           m++;
/*     */         }
/* 588 */         i = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 596 */     paramDataOutputStream.writeShort(m);
/* 597 */     for (int i2 = 0; i2 < m; i2++) {
/* 598 */       localObject = (Cover)localVector1.elementAt(i2);
/* 599 */       l2 = ((Cover)localObject).Addr >> 32;
/* 600 */       long l3 = ((Cover)localObject).Addr << 32 >> 32;
/* 601 */       paramDataOutputStream.writeShort(((Cover)localObject).NumCommand);
/* 602 */       paramDataOutputStream.writeShort(((Cover)localObject).Type);
/* 603 */       paramDataOutputStream.writeInt((int)l2);
/* 604 */       paramDataOutputStream.writeInt((int)l3);
/*     */ 
/* 606 */       if ((((Cover)localObject).Type != 5) || (((Cover)localObject).Addr != 0L))
/* 607 */         JcovClassCountArray[((Cover)localObject).Type] += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNativeToJcovTab(Environment paramEnvironment, ClassDefinition paramClassDefinition)
/*     */   {
/* 618 */     JcovClassCountArray[1] += 1;
/*     */   }
/*     */ 
/*     */   private String createClassJcovElement(Environment paramEnvironment, ClassDefinition paramClassDefinition)
/*     */   {
/* 626 */     String str1 = Type.mangleInnerType(paramClassDefinition.getClassDeclaration().getName()).toString();
/*     */ 
/* 630 */     SourceClassList.addElement(str1);
/* 631 */     String str2 = str1.replace('.', '/');
/* 632 */     String str3 = JcovClassLine + str2;
/*     */ 
/* 634 */     str3 = str3 + " [";
/* 635 */     String str4 = "";
/*     */ 
/* 637 */     for (int i = 0; i < arrayModifiers.length; i++) {
/* 638 */       if ((paramClassDefinition.getModifiers() & arrayModifiers[i]) != 0) {
/* 639 */         str3 = str3 + str4 + opNames[arrayModifiersOpc[i]];
/* 640 */         str4 = " ";
/*     */       }
/*     */     }
/* 643 */     str3 = str3 + "]";
/*     */ 
/* 645 */     return str3;
/*     */   }
/*     */ 
/*     */   public void GenVecJCov(Environment paramEnvironment, ClassDefinition paramClassDefinition, long paramLong)
/*     */   {
/* 654 */     String str = ((SourceClass)paramClassDefinition).getAbsoluteName();
/*     */ 
/* 656 */     TmpCovTable.addElement(createClassJcovElement(paramEnvironment, paramClassDefinition));
/* 657 */     TmpCovTable.addElement(JcovSrcfileLine + str);
/* 658 */     TmpCovTable.addElement(JcovTimestampLine + paramLong);
/* 659 */     TmpCovTable.addElement(JcovDataLine + "A");
/* 660 */     TmpCovTable.addElement(JcovHeadingLine);
/*     */ 
/* 662 */     for (int i = 1; i <= 8; i++)
/* 663 */       if (JcovClassCountArray[i] != 0) {
/* 664 */         TmpCovTable.addElement(new String(i + "\t" + JcovClassCountArray[i]));
/* 665 */         JcovClassCountArray[i] = 0;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void GenJCov(Environment paramEnvironment)
/*     */   {
/*     */     try
/*     */     {
/* 679 */       File localFile = paramEnvironment.getcovFile();
/* 680 */       if (localFile.exists()) {
/* 681 */         localObject1 = new DataInputStream(new BufferedInputStream(new FileInputStream(localFile)));
/*     */ 
/* 684 */         localObject2 = null;
/* 685 */         int i = 1;
/*     */ 
/* 688 */         localObject2 = ((DataInputStream)localObject1).readLine();
/* 689 */         if ((localObject2 != null) && (((String)localObject2).startsWith(JcovMagicLine)))
/*     */         {
/* 692 */           while ((localObject2 = ((DataInputStream)localObject1).readLine()) != null)
/*     */           {
/*     */             Enumeration localEnumeration;
/* 693 */             if (((String)localObject2).startsWith(JcovClassLine)) {
/* 694 */               i = 1;
/* 695 */               for (localEnumeration = SourceClassList.elements(); localEnumeration.hasMoreElements(); ) {
/* 696 */                 String str2 = ((String)localObject2).substring(JcovClassLine.length());
/* 697 */                 int j = str2.indexOf(' ');
/*     */ 
/* 699 */                 if (j != -1) {
/* 700 */                   str2 = str2.substring(0, j);
/*     */                 }
/* 702 */                 String str1 = (String)localEnumeration.nextElement();
/* 703 */                 if (str1.compareTo(str2) == 0) {
/* 704 */                   i = 0;
/* 705 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/* 709 */             if (i != 0)
/* 710 */               TmpCovTable.addElement(localObject2);
/*     */           }
/*     */         }
/* 713 */         ((DataInputStream)localObject1).close();
/*     */       }
/* 715 */       Object localObject1 = new PrintStream(new DataOutputStream(new FileOutputStream(localFile)));
/* 716 */       ((PrintStream)localObject1).println(JcovMagicLine);
/* 717 */       for (Object localObject2 = TmpCovTable.elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 718 */         ((PrintStream)localObject1).println((String)((Enumeration)localObject2).nextElement());
/*     */       }
/* 720 */       ((PrintStream)localObject1).close();
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException) {
/* 723 */       System.out.println("ERROR: " + localFileNotFoundException);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 726 */       System.out.println("ERROR: " + localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeLineNumberTable(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*     */     throws IOException
/*     */   {
/* 736 */     long l1 = -1L;
/* 737 */     int i = 0;
/*     */     long l2;
/* 739 */     for (Object localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next) {
/* 740 */       l2 = ((Instruction)localObject).where >> 32;
/* 741 */       if ((l2 > 0L) && (l1 != l2)) {
/* 742 */         l1 = l2;
/* 743 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 747 */     l1 = -1L;
/* 748 */     paramDataOutputStream.writeShort(i);
/* 749 */     for (localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next) {
/* 750 */       l2 = ((Instruction)localObject).where >> 32;
/* 751 */       if ((l2 > 0L) && (l1 != l2)) {
/* 752 */         l1 = l2;
/* 753 */         paramDataOutputStream.writeShort(((Instruction)localObject).pc);
/* 754 */         paramDataOutputStream.writeShort((int)l1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void flowFields(Environment paramEnvironment, Label paramLabel, MemberDefinition[] paramArrayOfMemberDefinition)
/*     */   {
/* 766 */     if (paramLabel.locals != null)
/*     */     {
/* 768 */       arrayOfMemberDefinition = paramLabel.locals;
/* 769 */       for (int i = 0; i < this.maxvar; i++) {
/* 770 */         if (arrayOfMemberDefinition[i] != paramArrayOfMemberDefinition[i]) {
/* 771 */           arrayOfMemberDefinition[i] = null;
/*     */         }
/*     */       }
/* 774 */       return;
/*     */     }
/*     */ 
/* 778 */     paramLabel.locals = new MemberDefinition[this.maxvar];
/* 779 */     System.arraycopy(paramArrayOfMemberDefinition, 0, paramLabel.locals, 0, this.maxvar);
/*     */ 
/* 781 */     MemberDefinition[] arrayOfMemberDefinition = new MemberDefinition[this.maxvar];
/* 782 */     System.arraycopy(paramArrayOfMemberDefinition, 0, arrayOfMemberDefinition, 0, this.maxvar);
/* 783 */     paramArrayOfMemberDefinition = arrayOfMemberDefinition;
/*     */ 
/* 785 */     for (Instruction localInstruction = paramLabel.next; localInstruction != null; localInstruction = localInstruction.next)
/*     */     {
/*     */       Object localObject;
/*     */       Enumeration localEnumeration;
/* 786 */       switch (localInstruction.opc) { case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 65:
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/* 797 */         if ((localInstruction.value instanceof LocalVariable)) {
/* 798 */           localObject = (LocalVariable)localInstruction.value;
/* 799 */           paramArrayOfMemberDefinition[localObject.slot] = ((LocalVariable)localObject).field;
/* 800 */         }break;
/*     */       case -1:
/* 804 */         flowFields(paramEnvironment, (Label)localInstruction, paramArrayOfMemberDefinition);
/* 805 */         return;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 168:
/*     */       case 198:
/*     */       case 199:
/* 814 */         flowFields(paramEnvironment, (Label)localInstruction.value, paramArrayOfMemberDefinition);
/* 815 */         break;
/*     */       case 167:
/* 818 */         flowFields(paramEnvironment, (Label)localInstruction.value, paramArrayOfMemberDefinition);
/* 819 */         return;
/*     */       case 169:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
/*     */       case 191:
/* 824 */         return;
/*     */       case 170:
/*     */       case 171:
/* 828 */         localObject = (SwitchData)localInstruction.value;
/* 829 */         flowFields(paramEnvironment, ((SwitchData)localObject).defaultLabel, paramArrayOfMemberDefinition);
/* 830 */         for (localEnumeration = ((SwitchData)localObject).tab.elements(); localEnumeration.hasMoreElements(); ) {
/* 831 */           flowFields(paramEnvironment, (Label)localEnumeration.nextElement(), paramArrayOfMemberDefinition);
/*     */         }
/* 833 */         return;
/*     */       case -3:
/* 837 */         localObject = ((TryData)localInstruction.value).catches;
/* 838 */         for (localEnumeration = ((Vector)localObject).elements(); localEnumeration.hasMoreElements(); ) {
/* 839 */           CatchData localCatchData = (CatchData)localEnumeration.nextElement();
/* 840 */           flowFields(paramEnvironment, localCatchData.getLabel(), paramArrayOfMemberDefinition); } case -2:
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/*     */       case 34:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 42:
/*     */       case 43:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 47:
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 52:
/*     */       case 53:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 83:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89:
/*     */       case 90:
/*     */       case 91:
/*     */       case 92:
/*     */       case 93:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/*     */       case 97:
/*     */       case 98:
/*     */       case 99:
/*     */       case 100:
/*     */       case 101:
/*     */       case 102:
/*     */       case 103:
/*     */       case 104:
/*     */       case 105:
/*     */       case 106:
/*     */       case 107:
/*     */       case 108:
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 115:
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 120:
/*     */       case 121:
/*     */       case 122:
/*     */       case 123:
/*     */       case 124:
/*     */       case 125:
/*     */       case 126:
/*     */       case 127:
/*     */       case 128:
/*     */       case 129:
/*     */       case 130:
/*     */       case 131:
/*     */       case 132:
/*     */       case 133:
/*     */       case 134:
/*     */       case 135:
/*     */       case 136:
/*     */       case 137:
/*     */       case 138:
/*     */       case 139:
/*     */       case 140:
/*     */       case 141:
/*     */       case 142:
/*     */       case 143:
/*     */       case 144:
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/*     */       case 148:
/*     */       case 149:
/*     */       case 150:
/*     */       case 151:
/*     */       case 152:
/*     */       case 178:
/*     */       case 179:
/*     */       case 180:
/*     */       case 181:
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 185:
/*     */       case 186:
/*     */       case 187:
/*     */       case 188:
/*     */       case 189:
/*     */       case 190:
/*     */       case 192:
/*     */       case 193:
/*     */       case 194:
/*     */       case 195:
/*     */       case 196:
/*     */       case 197: }  }  } 
/* 854 */   public void writeLocalVariableTable(Environment paramEnvironment, MemberDefinition paramMemberDefinition, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException { MemberDefinition[] arrayOfMemberDefinition1 = new MemberDefinition[this.maxvar];
/* 855 */     int i = 0;
/*     */     int j;
/* 858 */     if ((paramMemberDefinition != null) && (paramMemberDefinition.getArguments() != null)) {
/* 859 */       j = 0;
/*     */ 
/* 861 */       Vector localVector = paramMemberDefinition.getArguments();
/* 862 */       for (localObject1 = localVector.elements(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 863 */         localObject2 = (MemberDefinition)((Enumeration)localObject1).nextElement();
/* 864 */         arrayOfMemberDefinition1[j] = localObject2;
/* 865 */         j += ((MemberDefinition)localObject2).getType().stackSize();
/*     */       }
/*     */     }
/*     */     Object localObject2;
/* 869 */     flowFields(paramEnvironment, this.first, arrayOfMemberDefinition1);
/* 870 */     LocalVariableTable localLocalVariableTable = new LocalVariableTable();
/*     */ 
/* 873 */     for (i = 0; i < this.maxvar; i++)
/* 874 */       arrayOfMemberDefinition1[i] = null;
/*     */     int k;
/* 875 */     if ((paramMemberDefinition != null) && (paramMemberDefinition.getArguments() != null)) {
/* 876 */       k = 0;
/*     */ 
/* 878 */       localObject1 = paramMemberDefinition.getArguments();
/* 879 */       for (localObject2 = ((Vector)localObject1).elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 880 */         MemberDefinition localMemberDefinition = (MemberDefinition)((Enumeration)localObject2).nextElement();
/* 881 */         arrayOfMemberDefinition1[k] = localMemberDefinition;
/* 882 */         localLocalVariableTable.define(localMemberDefinition, k, 0, this.maxpc);
/* 883 */         k += localMemberDefinition.getType().stackSize();
/*     */       }
/*     */     }
/*     */ 
/* 887 */     int[] arrayOfInt = new int[this.maxvar];
/*     */ 
/* 889 */     for (Object localObject1 = this.first; localObject1 != null; localObject1 = ((Instruction)localObject1).next)
/* 890 */       switch (((Instruction)localObject1).opc) { case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 65:
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/* 901 */         if ((((Instruction)localObject1).value instanceof LocalVariable)) {
/* 902 */           localObject2 = (LocalVariable)((Instruction)localObject1).value;
/* 903 */           int n = ((Instruction)localObject1).next != null ? ((Instruction)localObject1).next.pc : ((Instruction)localObject1).pc;
/* 904 */           if (arrayOfMemberDefinition1[localObject2.slot] != null) {
/* 905 */             localLocalVariableTable.define(arrayOfMemberDefinition1[localObject2.slot], ((LocalVariable)localObject2).slot, arrayOfInt[localObject2.slot], n);
/*     */           }
/* 907 */           arrayOfInt[localObject2.slot] = n;
/* 908 */           arrayOfMemberDefinition1[localObject2.slot] = ((LocalVariable)localObject2).field;
/* 909 */         }break;
/*     */       case -1:
/* 914 */         for (i = 0; i < this.maxvar; i++) {
/* 915 */           if (arrayOfMemberDefinition1[i] != null) {
/* 916 */             localLocalVariableTable.define(arrayOfMemberDefinition1[i], i, arrayOfInt[i], ((Instruction)localObject1).pc);
/*     */           }
/*     */         }
/*     */ 
/* 920 */         int m = ((Instruction)localObject1).pc;
/* 921 */         MemberDefinition[] arrayOfMemberDefinition2 = ((Label)localObject1).locals;
/* 922 */         if (arrayOfMemberDefinition2 == null) {
/* 923 */           for (i = 0; i < this.maxvar; i++)
/* 924 */             arrayOfMemberDefinition1[i] = null;
/*     */         }
/* 926 */         System.arraycopy(arrayOfMemberDefinition2, 0, arrayOfMemberDefinition1, 0, this.maxvar);
/*     */ 
/* 928 */         for (i = 0; i < this.maxvar; i++)
/* 929 */           arrayOfInt[i] = m; case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/*     */       case 34:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 42:
/*     */       case 43:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 47:
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 52:
/* 937 */       case 53: }  for (i = 0; i < this.maxvar; i++) {
/* 938 */       if (arrayOfMemberDefinition1[i] != null) {
/* 939 */         localLocalVariableTable.define(arrayOfMemberDefinition1[i], i, arrayOfInt[i], this.maxpc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 944 */     localLocalVariableTable.write(paramEnvironment, paramDataOutputStream, paramConstantPool);
/*     */   }
/*     */ 
/*     */   public boolean empty()
/*     */   {
/* 951 */     return this.first == this.last;
/*     */   }
/*     */ 
/*     */   public void listing(PrintStream paramPrintStream)
/*     */   {
/* 958 */     paramPrintStream.println("-- listing --");
/* 959 */     for (Object localObject = this.first; localObject != null; localObject = ((Instruction)localObject).next)
/* 960 */       paramPrintStream.println(((Instruction)localObject).toString());
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.Assembler
 * JD-Core Version:    0.6.2
 */