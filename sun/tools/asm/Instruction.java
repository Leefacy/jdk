/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class Instruction
/*     */   implements Constants
/*     */ {
/*     */   long where;
/*     */   int pc;
/*     */   int opc;
/*     */   Object value;
/*     */   Instruction next;
/*     */   boolean flagCondInverted;
/*  50 */   boolean flagNoCovered = false;
/*     */ 
/* 116 */   public static final double SWITCHRATIO = d1;
/*     */ 
/*     */   public Instruction(long paramLong, int paramInt, Object paramObject, boolean paramBoolean)
/*     */   {
/*  58 */     this.where = paramLong;
/*  59 */     this.opc = paramInt;
/*  60 */     this.value = paramObject;
/*  61 */     this.flagCondInverted = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Instruction(boolean paramBoolean, long paramLong, int paramInt, Object paramObject)
/*     */   {
/*  68 */     this.where = paramLong;
/*  69 */     this.opc = paramInt;
/*  70 */     this.value = paramObject;
/*  71 */     this.flagNoCovered = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Instruction(long paramLong, int paramInt, boolean paramBoolean)
/*     */   {
/*  78 */     this.where = paramLong;
/*  79 */     this.opc = paramInt;
/*  80 */     this.flagNoCovered = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Instruction(long paramLong, int paramInt, Object paramObject)
/*     */   {
/*  88 */     this.where = paramLong;
/*  89 */     this.opc = paramInt;
/*  90 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public int getOpcode()
/*     */   {
/* 123 */     return this.pc;
/*     */   }
/*     */ 
/*     */   public Object getValue() {
/* 127 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject) {
/* 131 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   void optimize(Environment paramEnvironment)
/*     */   {
/*     */     Object localObject;
/* 139 */     switch (this.opc) { case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/* 144 */       if (((this.value instanceof LocalVariable)) && (!paramEnvironment.debug_vars()))
/* 145 */         this.value = new Integer(((LocalVariable)this.value).slot); break;
/*     */     case 167:
/* 150 */       localObject = (Label)this.value;
/* 151 */       this.value = (localObject = ((Label)localObject).getDestination());
/* 152 */       if (localObject == this.next)
/*     */       {
/* 154 */         this.opc = -2;
/*     */       }
/* 199 */       else if ((((Label)localObject).next != null) && (paramEnvironment.opt()))
/* 200 */         switch (((Label)localObject).next.opc) { case 172:
/*     */         case 173:
/*     */         case 174:
/*     */         case 175:
/*     */         case 176:
/*     */         case 177:
/* 204 */           this.opc = ((Label)localObject).next.opc;
/* 205 */           this.value = ((Label)localObject).next.value; }
/* 206 */       break;
/*     */     case 153:
/*     */     case 154:
/*     */     case 155:
/*     */     case 156:
/*     */     case 157:
/*     */     case 158:
/*     */     case 198:
/*     */     case 199:
/* 215 */       this.value = ((Label)this.value).getDestination();
/* 216 */       if (this.value == this.next)
/*     */       {
/* 218 */         this.opc = 87;
/*     */       }
/* 221 */       else if ((this.next.opc == 167) && (this.value == this.next.next))
/*     */       {
/* 225 */         switch (this.opc) { case 153:
/* 226 */           this.opc = 154; break;
/*     */         case 154:
/* 227 */           this.opc = 153; break;
/*     */         case 155:
/* 228 */           this.opc = 156; break;
/*     */         case 158:
/* 229 */           this.opc = 157; break;
/*     */         case 157:
/* 230 */           this.opc = 158; break;
/*     */         case 156:
/* 231 */           this.opc = 155; break;
/*     */         case 198:
/* 232 */           this.opc = 199; break;
/*     */         case 199:
/* 233 */           this.opc = 198;
/*     */         }
/*     */ 
/* 236 */         this.flagCondInverted = (!this.flagCondInverted);
/*     */ 
/* 238 */         this.value = this.next.value;
/* 239 */         this.next.opc = -2; } break;
/*     */     case 159:
/*     */     case 160:
/*     */     case 161:
/*     */     case 162:
/*     */     case 163:
/*     */     case 164:
/*     */     case 165:
/*     */     case 166:
/* 247 */       this.value = ((Label)this.value).getDestination();
/* 248 */       if (this.value == this.next)
/*     */       {
/* 250 */         this.opc = 88;
/*     */       }
/* 253 */       else if ((this.next.opc == 167) && (this.value == this.next.next))
/*     */       {
/* 255 */         switch (this.opc) { case 165:
/* 256 */           this.opc = 166; break;
/*     */         case 166:
/* 257 */           this.opc = 165; break;
/*     */         case 159:
/* 258 */           this.opc = 160; break;
/*     */         case 160:
/* 259 */           this.opc = 159; break;
/*     */         case 163:
/* 260 */           this.opc = 164; break;
/*     */         case 162:
/* 261 */           this.opc = 161; break;
/*     */         case 161:
/* 262 */           this.opc = 162; break;
/*     */         case 164:
/* 263 */           this.opc = 163;
/*     */         }
/*     */ 
/* 266 */         this.flagCondInverted = (!this.flagCondInverted);
/*     */ 
/* 268 */         this.value = this.next.value;
/* 269 */         this.next.opc = -2; } break;
/*     */     case 170:
/*     */     case 171:
/* 275 */       localObject = (SwitchData)this.value;
/* 276 */       ((SwitchData)localObject).defaultLabel = ((SwitchData)localObject).defaultLabel.getDestination();
/* 277 */       for (Enumeration localEnumeration = ((SwitchData)localObject).tab.keys(); localEnumeration.hasMoreElements(); ) {
/* 278 */         Integer localInteger = (Integer)localEnumeration.nextElement();
/* 279 */         Label localLabel = (Label)((SwitchData)localObject).tab.get(localInteger);
/* 280 */         ((SwitchData)localObject).tab.put(localInteger, localLabel.getDestination());
/*     */       }
/*     */ 
/* 286 */       long l1 = ((SwitchData)localObject).maxValue - ((SwitchData)localObject).minValue + 1L;
/* 287 */       long l2 = ((SwitchData)localObject).tab.size();
/*     */ 
/* 289 */       long l3 = 4L + l1;
/* 290 */       long l4 = 3L + 2L * l2;
/*     */ 
/* 292 */       if (l3 <= l4 * SWITCHRATIO)
/* 293 */         this.opc = 170;
/*     */       else {
/* 295 */         this.opc = 171;
/*     */       }
/* 297 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   void collect(ConstantPool paramConstantPool)
/*     */   {
/* 307 */     switch (this.opc) { case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/* 310 */       if ((this.value instanceof LocalVariable)) {
/* 311 */         MemberDefinition localMemberDefinition = ((LocalVariable)this.value).field;
/* 312 */         paramConstantPool.put(localMemberDefinition.getName().toString());
/* 313 */         paramConstantPool.put(localMemberDefinition.getType().getTypeSignature());
/*     */       }
/* 315 */       return;
/*     */     case 178:
/*     */     case 179:
/*     */     case 180:
/*     */     case 181:
/*     */     case 182:
/*     */     case 183:
/*     */     case 184:
/*     */     case 185:
/*     */     case 187:
/*     */     case 192:
/*     */     case 193:
/* 323 */       paramConstantPool.put(this.value);
/* 324 */       return;
/*     */     case 189:
/* 327 */       paramConstantPool.put(this.value);
/* 328 */       return;
/*     */     case 197:
/* 331 */       paramConstantPool.put(((ArrayData)this.value).type);
/* 332 */       return;
/*     */     case 18:
/*     */     case 19:
/* 336 */       if ((this.value instanceof Integer)) {
/* 337 */         int i = ((Integer)this.value).intValue();
/* 338 */         if ((i >= -1) && (i <= 5)) {
/* 339 */           this.opc = (3 + i);
/* 340 */           return;
/* 341 */         }if ((i >= -128) && (i < 128)) {
/* 342 */           this.opc = 16;
/* 343 */           return;
/* 344 */         }if ((i >= -32768) && (i < 32768)) {
/* 345 */           this.opc = 17;
/* 346 */           return;
/*     */         }
/* 348 */       } else if ((this.value instanceof Float)) {
/* 349 */         float f = ((Float)this.value).floatValue();
/* 350 */         if (f == 0.0F) {
/* 351 */           if (Float.floatToIntBits(f) == 0)
/* 352 */             this.opc = 11;
/*     */         }
/*     */         else {
/* 355 */           if (f == 1.0F) {
/* 356 */             this.opc = 12;
/* 357 */             return;
/* 358 */           }if (f == 2.0F) {
/* 359 */             this.opc = 13;
/* 360 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 363 */       paramConstantPool.put(this.value);
/* 364 */       return;
/*     */     case 20:
/* 367 */       if ((this.value instanceof Long)) {
/* 368 */         long l = ((Long)this.value).longValue();
/* 369 */         if (l == 0L) {
/* 370 */           this.opc = 9;
/* 371 */           return;
/* 372 */         }if (l == 1L) {
/* 373 */           this.opc = 10;
/* 374 */           return;
/*     */         }
/* 376 */       } else if ((this.value instanceof Double)) {
/* 377 */         double d = ((Double)this.value).doubleValue();
/* 378 */         if (d == 0.0D) {
/* 379 */           if (Double.doubleToLongBits(d) == 0L) {
/* 380 */             this.opc = 14;
/*     */           }
/*     */         }
/* 383 */         else if (d == 1.0D) {
/* 384 */           this.opc = 15;
/* 385 */           return;
/*     */         }
/*     */       }
/* 388 */       paramConstantPool.put(this.value);
/* 389 */       return;
/*     */     case -3:
/* 392 */       for (Enumeration localEnumeration = ((TryData)this.value).catches.elements(); localEnumeration.hasMoreElements(); ) {
/* 393 */         CatchData localCatchData = (CatchData)localEnumeration.nextElement();
/* 394 */         if (localCatchData.getType() != null) {
/* 395 */           paramConstantPool.put(localCatchData.getType());
/*     */         }
/*     */       }
/* 398 */       return;
/*     */     case 0:
/* 401 */       if ((this.value != null) && ((this.value instanceof ClassDeclaration)))
/* 402 */         paramConstantPool.put(this.value);
/* 403 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   int balance()
/*     */   {
/* 411 */     switch (this.opc) { case -3:
/*     */     case -2:
/*     */     case -1:
/*     */     case 0:
/*     */     case 47:
/*     */     case 49:
/*     */     case 95:
/*     */     case 116:
/*     */     case 117:
/*     */     case 118:
/*     */     case 119:
/*     */     case 132:
/*     */     case 134:
/*     */     case 138:
/*     */     case 139:
/*     */     case 143:
/*     */     case 145:
/*     */     case 146:
/*     */     case 147:
/*     */     case 167:
/*     */     case 168:
/*     */     case 169:
/*     */     case 177:
/*     */     case 188:
/*     */     case 189:
/*     */     case 190:
/*     */     case 192:
/*     */     case 193:
/*     */     case 200:
/*     */     case 201:
/* 422 */       return 0;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 21:
/*     */     case 23:
/*     */     case 25:
/*     */     case 89:
/*     */     case 90:
/*     */     case 91:
/*     */     case 133:
/*     */     case 135:
/*     */     case 140:
/*     */     case 141:
/*     */     case 187:
/* 433 */       return 1;
/*     */     case 9:
/*     */     case 10:
/*     */     case 14:
/*     */     case 15:
/*     */     case 20:
/*     */     case 22:
/*     */     case 24:
/*     */     case 92:
/*     */     case 93:
/*     */     case 94:
/* 439 */       return 2;
/*     */     case 46:
/*     */     case 48:
/*     */     case 50:
/*     */     case 51:
/*     */     case 52:
/*     */     case 53:
/*     */     case 54:
/*     */     case 56:
/*     */     case 58:
/*     */     case 87:
/*     */     case 96:
/*     */     case 98:
/*     */     case 100:
/*     */     case 102:
/*     */     case 104:
/*     */     case 106:
/*     */     case 108:
/*     */     case 110:
/*     */     case 112:
/*     */     case 114:
/*     */     case 120:
/*     */     case 121:
/*     */     case 122:
/*     */     case 123:
/*     */     case 124:
/*     */     case 125:
/*     */     case 126:
/*     */     case 128:
/*     */     case 130:
/*     */     case 136:
/*     */     case 137:
/*     */     case 142:
/*     */     case 144:
/*     */     case 149:
/*     */     case 150:
/*     */     case 153:
/*     */     case 154:
/*     */     case 155:
/*     */     case 156:
/*     */     case 157:
/*     */     case 158:
/*     */     case 170:
/*     */     case 171:
/*     */     case 172:
/*     */     case 174:
/*     */     case 176:
/*     */     case 191:
/*     */     case 194:
/*     */     case 195:
/*     */     case 198:
/*     */     case 199:
/* 458 */       return -1;
/*     */     case 55:
/*     */     case 57:
/*     */     case 88:
/*     */     case 97:
/*     */     case 99:
/*     */     case 101:
/*     */     case 103:
/*     */     case 105:
/*     */     case 107:
/*     */     case 109:
/*     */     case 111:
/*     */     case 113:
/*     */     case 115:
/*     */     case 127:
/*     */     case 129:
/*     */     case 131:
/*     */     case 159:
/*     */     case 160:
/*     */     case 161:
/*     */     case 162:
/*     */     case 163:
/*     */     case 164:
/*     */     case 165:
/*     */     case 166:
/*     */     case 173:
/*     */     case 175:
/* 469 */       return -2;
/*     */     case 79:
/*     */     case 81:
/*     */     case 83:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 148:
/*     */     case 151:
/*     */     case 152:
/* 474 */       return -3;
/*     */     case 80:
/*     */     case 82:
/* 477 */       return -4;
/*     */     case 197:
/* 480 */       return 1 - ((ArrayData)this.value).nargs;
/*     */     case 180:
/* 483 */       return ((MemberDefinition)this.value).getType().stackSize() - 1;
/*     */     case 181:
/* 486 */       return -1 - ((MemberDefinition)this.value).getType().stackSize();
/*     */     case 178:
/* 489 */       return ((MemberDefinition)this.value).getType().stackSize();
/*     */     case 179:
/* 492 */       return -((MemberDefinition)this.value).getType().stackSize();
/*     */     case 182:
/*     */     case 183:
/*     */     case 185:
/* 498 */       return ((MemberDefinition)this.value).getType().getReturnType().stackSize() - (((MemberDefinition)this.value)
/* 498 */         .getType().stackSize() + 1);
/*     */     case 184:
/* 502 */       return ((MemberDefinition)this.value).getType().getReturnType().stackSize() - ((MemberDefinition)this.value)
/* 502 */         .getType().stackSize();
/*     */     case 26:
/*     */     case 27:
/*     */     case 28:
/*     */     case 29:
/*     */     case 30:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 34:
/*     */     case 35:
/*     */     case 36:
/*     */     case 37:
/*     */     case 38:
/*     */     case 39:
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 44:
/*     */     case 45:
/*     */     case 59:
/*     */     case 60:
/*     */     case 61:
/*     */     case 62:
/*     */     case 63:
/*     */     case 64:
/*     */     case 65:
/*     */     case 66:
/*     */     case 67:
/*     */     case 68:
/*     */     case 69:
/*     */     case 70:
/*     */     case 71:
/*     */     case 72:
/*     */     case 73:
/*     */     case 74:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 186:
/* 504 */     case 196: } throw new CompilerError("invalid opcode: " + toString());
/*     */   }
/*     */ 
/*     */   int size(ConstantPool paramConstantPool)
/*     */   {
/*     */     int i;
/*     */     int j;
/*     */     SwitchData localSwitchData;
/* 511 */     switch (this.opc) { case -3:
/*     */     case -2:
/*     */     case -1:
/* 513 */       return 0;
/*     */     case 16:
/*     */     case 188:
/* 516 */       return 2;
/*     */     case 17:
/*     */     case 153:
/*     */     case 154:
/*     */     case 155:
/*     */     case 156:
/*     */     case 157:
/*     */     case 158:
/*     */     case 159:
/*     */     case 160:
/*     */     case 161:
/*     */     case 162:
/*     */     case 163:
/*     */     case 164:
/*     */     case 165:
/*     */     case 166:
/*     */     case 167:
/*     */     case 168:
/*     */     case 198:
/*     */     case 199:
/* 525 */       return 3;
/*     */     case 18:
/*     */     case 19:
/* 529 */       if (paramConstantPool.index(this.value) < 256) {
/* 530 */         this.opc = 18;
/* 531 */         return 2;
/*     */       }
/* 533 */       this.opc = 19;
/* 534 */       return 3;
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/* 539 */       i = ((Number)this.value).intValue();
/* 540 */       if (i < 4) {
/* 541 */         if (i < 0) {
/* 542 */           throw new CompilerError("invalid slot: " + toString() + "\nThis error possibly resulted from poorly constructed class paths.");
/*     */         }
/*     */ 
/* 545 */         this.opc = (26 + (this.opc - 21) * 4 + i);
/* 546 */         return 1;
/* 547 */       }if (i <= 255) {
/* 548 */         return 2;
/*     */       }
/* 550 */       this.opc += 256;
/* 551 */       return 4;
/*     */     case 132:
/* 556 */       i = ((int[])(int[])this.value)[0];
/* 557 */       j = ((int[])(int[])this.value)[1];
/* 558 */       if (i < 0) {
/* 559 */         throw new CompilerError("invalid slot: " + toString());
/*     */       }
/* 561 */       if ((i <= 255) && ((byte)j == j)) {
/* 562 */         return 3;
/*     */       }
/* 564 */       this.opc += 256;
/* 565 */       return 6;
/*     */     case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/* 572 */       i = (this.value instanceof Number) ? ((Number)this.value)
/* 572 */         .intValue() : ((LocalVariable)this.value).slot;
/* 573 */       if (i < 4) {
/* 574 */         if (i < 0) {
/* 575 */           throw new CompilerError("invalid slot: " + toString());
/*     */         }
/* 577 */         this.opc = (59 + (this.opc - 54) * 4 + i);
/* 578 */         return 1;
/* 579 */       }if (i <= 255) {
/* 580 */         return 2;
/*     */       }
/* 582 */       this.opc += 256;
/* 583 */       return 4;
/*     */     case 169:
/* 588 */       i = ((Number)this.value).intValue();
/* 589 */       if (i <= 255) {
/* 590 */         if (i < 0) {
/* 591 */           throw new CompilerError("invalid slot: " + toString());
/*     */         }
/* 593 */         return 2;
/*     */       }
/* 595 */       this.opc += 256;
/* 596 */       return 4;
/*     */     case 20:
/*     */     case 178:
/*     */     case 179:
/*     */     case 180:
/*     */     case 181:
/*     */     case 182:
/*     */     case 183:
/*     */     case 184:
/*     */     case 187:
/*     */     case 189:
/*     */     case 192:
/*     */     case 193:
/* 606 */       return 3;
/*     */     case 197:
/* 609 */       return 4;
/*     */     case 185:
/*     */     case 200:
/*     */     case 201:
/* 614 */       return 5;
/*     */     case 170:
/* 617 */       localSwitchData = (SwitchData)this.value;
/* 618 */       j = 1;
/* 619 */       while ((this.pc + j) % 4 != 0) j++;
/* 620 */       return j + 16 + (localSwitchData.maxValue - localSwitchData.minValue) * 4;
/*     */     case 171:
/* 624 */       localSwitchData = (SwitchData)this.value;
/* 625 */       j = 1;
/* 626 */       while ((this.pc + j) % 4 != 0) j++;
/* 627 */       return j + 8 + localSwitchData.tab.size() * 8;
/*     */     case 0:
/* 631 */       if ((this.value != null) && (!(this.value instanceof Integer))) {
/* 632 */         return 2;
/*     */       }
/* 634 */       return 1;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 26:
/*     */     case 27:
/*     */     case 28:
/*     */     case 29:
/*     */     case 30:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 34:
/*     */     case 35:
/*     */     case 36:
/*     */     case 37:
/*     */     case 38:
/*     */     case 39:
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 44:
/*     */     case 45:
/*     */     case 46:
/*     */     case 47:
/*     */     case 48:
/*     */     case 49:
/*     */     case 50:
/*     */     case 51:
/*     */     case 52:
/*     */     case 53:
/*     */     case 59:
/*     */     case 60:
/*     */     case 61:
/*     */     case 62:
/*     */     case 63:
/*     */     case 64:
/*     */     case 65:
/*     */     case 66:
/*     */     case 67:
/*     */     case 68:
/*     */     case 69:
/*     */     case 70:
/*     */     case 71:
/*     */     case 72:
/*     */     case 73:
/*     */     case 74:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 81:
/*     */     case 82:
/*     */     case 83:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/*     */     case 89:
/*     */     case 90:
/*     */     case 91:
/*     */     case 92:
/*     */     case 93:
/*     */     case 94:
/*     */     case 95:
/*     */     case 96:
/*     */     case 97:
/*     */     case 98:
/*     */     case 99:
/*     */     case 100:
/*     */     case 101:
/*     */     case 102:
/*     */     case 103:
/*     */     case 104:
/*     */     case 105:
/*     */     case 106:
/*     */     case 107:
/*     */     case 108:
/*     */     case 109:
/*     */     case 110:
/*     */     case 111:
/*     */     case 112:
/*     */     case 113:
/*     */     case 114:
/*     */     case 115:
/*     */     case 116:
/*     */     case 117:
/*     */     case 118:
/*     */     case 119:
/*     */     case 120:
/*     */     case 121:
/*     */     case 122:
/*     */     case 123:
/*     */     case 124:
/*     */     case 125:
/*     */     case 126:
/*     */     case 127:
/*     */     case 128:
/*     */     case 129:
/*     */     case 130:
/*     */     case 131:
/*     */     case 133:
/*     */     case 134:
/*     */     case 135:
/*     */     case 136:
/*     */     case 137:
/*     */     case 138:
/*     */     case 139:
/*     */     case 140:
/*     */     case 141:
/*     */     case 142:
/*     */     case 143:
/*     */     case 144:
/*     */     case 145:
/*     */     case 146:
/*     */     case 147:
/*     */     case 148:
/*     */     case 149:
/*     */     case 150:
/*     */     case 151:
/*     */     case 152:
/*     */     case 172:
/*     */     case 173:
/*     */     case 174:
/*     */     case 175:
/*     */     case 176:
/*     */     case 177:
/*     */     case 186:
/*     */     case 190:
/*     */     case 191:
/*     */     case 194:
/*     */     case 195:
/* 638 */     case 196: } return 1;
/*     */   }
/*     */ 
/*     */   void write(DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*     */     throws IOException
/*     */   {
/*     */     SwitchData localSwitchData;
/*     */     int i;
/*     */     Object localObject;
/* 646 */     switch (this.opc) { case -3:
/*     */     case -2:
/*     */     case -1:
/* 648 */       break;
/*     */     case 16:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 169:
/*     */     case 188:
/* 653 */       paramDataOutputStream.writeByte(this.opc);
/* 654 */       paramDataOutputStream.writeByte(((Number)this.value).intValue());
/* 655 */       break;
/*     */     case 277:
/*     */     case 278:
/*     */     case 279:
/*     */     case 280:
/*     */     case 281:
/*     */     case 425:
/* 660 */       paramDataOutputStream.writeByte(196);
/* 661 */       paramDataOutputStream.writeByte(this.opc - 256);
/* 662 */       paramDataOutputStream.writeShort(((Number)this.value).intValue());
/* 663 */       break;
/*     */     case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/* 667 */       paramDataOutputStream.writeByte(this.opc);
/* 668 */       paramDataOutputStream.writeByte((this.value instanceof Number) ? ((Number)this.value)
/* 669 */         .intValue() : ((LocalVariable)this.value).slot);
/* 670 */       break;
/*     */     case 310:
/*     */     case 311:
/*     */     case 312:
/*     */     case 313:
/*     */     case 314:
/* 675 */       paramDataOutputStream.writeByte(196);
/* 676 */       paramDataOutputStream.writeByte(this.opc - 256);
/* 677 */       paramDataOutputStream.writeShort((this.value instanceof Number) ? ((Number)this.value)
/* 678 */         .intValue() : ((LocalVariable)this.value).slot);
/* 679 */       break;
/*     */     case 17:
/* 682 */       paramDataOutputStream.writeByte(this.opc);
/* 683 */       paramDataOutputStream.writeShort(((Number)this.value).intValue());
/* 684 */       break;
/*     */     case 18:
/* 687 */       paramDataOutputStream.writeByte(this.opc);
/* 688 */       paramDataOutputStream.writeByte(paramConstantPool.index(this.value));
/* 689 */       break;
/*     */     case 19:
/*     */     case 20:
/*     */     case 178:
/*     */     case 179:
/*     */     case 180:
/*     */     case 181:
/*     */     case 182:
/*     */     case 183:
/*     */     case 184:
/*     */     case 187:
/*     */     case 192:
/*     */     case 193:
/* 697 */       paramDataOutputStream.writeByte(this.opc);
/* 698 */       paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/* 699 */       break;
/*     */     case 132:
/* 702 */       paramDataOutputStream.writeByte(this.opc);
/* 703 */       paramDataOutputStream.writeByte(((int[])(int[])this.value)[0]);
/* 704 */       paramDataOutputStream.writeByte(((int[])(int[])this.value)[1]);
/* 705 */       break;
/*     */     case 388:
/* 708 */       paramDataOutputStream.writeByte(196);
/* 709 */       paramDataOutputStream.writeByte(this.opc - 256);
/* 710 */       paramDataOutputStream.writeShort(((int[])(int[])this.value)[0]);
/* 711 */       paramDataOutputStream.writeShort(((int[])(int[])this.value)[1]);
/* 712 */       break;
/*     */     case 189:
/* 715 */       paramDataOutputStream.writeByte(this.opc);
/* 716 */       paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/* 717 */       break;
/*     */     case 197:
/* 720 */       paramDataOutputStream.writeByte(this.opc);
/* 721 */       paramDataOutputStream.writeShort(paramConstantPool.index(((ArrayData)this.value).type));
/* 722 */       paramDataOutputStream.writeByte(((ArrayData)this.value).nargs);
/* 723 */       break;
/*     */     case 185:
/* 726 */       paramDataOutputStream.writeByte(this.opc);
/* 727 */       paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/* 728 */       paramDataOutputStream.writeByte(((MemberDefinition)this.value).getType().stackSize() + 1);
/* 729 */       paramDataOutputStream.writeByte(0);
/* 730 */       break;
/*     */     case 153:
/*     */     case 154:
/*     */     case 155:
/*     */     case 156:
/*     */     case 157:
/*     */     case 158:
/*     */     case 159:
/*     */     case 160:
/*     */     case 161:
/*     */     case 162:
/*     */     case 163:
/*     */     case 164:
/*     */     case 165:
/*     */     case 166:
/*     */     case 167:
/*     */     case 168:
/*     */     case 198:
/*     */     case 199:
/* 738 */       paramDataOutputStream.writeByte(this.opc);
/* 739 */       paramDataOutputStream.writeShort(((Instruction)this.value).pc - this.pc);
/* 740 */       break;
/*     */     case 200:
/*     */     case 201:
/* 744 */       paramDataOutputStream.writeByte(this.opc);
/* 745 */       paramDataOutputStream.writeLong(((Instruction)this.value).pc - this.pc);
/* 746 */       break;
/*     */     case 170:
/* 749 */       localSwitchData = (SwitchData)this.value;
/* 750 */       paramDataOutputStream.writeByte(this.opc);
/* 751 */       for (i = 1; (this.pc + i) % 4 != 0; i++) {
/* 752 */         paramDataOutputStream.writeByte(0);
/*     */       }
/* 754 */       paramDataOutputStream.writeInt(localSwitchData.defaultLabel.pc - this.pc);
/* 755 */       paramDataOutputStream.writeInt(localSwitchData.minValue);
/* 756 */       paramDataOutputStream.writeInt(localSwitchData.maxValue);
/* 757 */       for (i = localSwitchData.minValue; i <= localSwitchData.maxValue; i++) {
/* 758 */         localObject = localSwitchData.get(i);
/* 759 */         int j = localObject != null ? ((Label)localObject).pc : localSwitchData.defaultLabel.pc;
/* 760 */         paramDataOutputStream.writeInt(j - this.pc);
/*     */       }
/* 762 */       break;
/*     */     case 171:
/* 766 */       localSwitchData = (SwitchData)this.value;
/* 767 */       paramDataOutputStream.writeByte(this.opc);
/* 768 */       for (i = this.pc + 1; 
/* 769 */         i % 4 != 0; i++) {
/* 770 */         paramDataOutputStream.writeByte(0);
/*     */       }
/* 772 */       paramDataOutputStream.writeInt(localSwitchData.defaultLabel.pc - this.pc);
/* 773 */       paramDataOutputStream.writeInt(localSwitchData.tab.size());
/* 774 */       for (localObject = localSwitchData.sortedKeys(); ((Enumeration)localObject).hasMoreElements(); ) {
/* 775 */         Integer localInteger = (Integer)((Enumeration)localObject).nextElement();
/* 776 */         paramDataOutputStream.writeInt(localInteger.intValue());
/* 777 */         paramDataOutputStream.writeInt(localSwitchData.get(localInteger).pc - this.pc);
/*     */       }
/* 779 */       break;
/*     */     case 0:
/* 783 */       if (this.value != null) {
/* 784 */         if ((this.value instanceof Integer))
/* 785 */           paramDataOutputStream.writeByte(((Integer)this.value).intValue());
/*     */         else {
/* 787 */           paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/*     */         }
/*     */         return;
/*     */       }
/*     */       break;
/*     */     }
/* 793 */     paramDataOutputStream.writeByte(this.opc);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 802 */     String str = (this.where >> 32) + ":\t";
/* 803 */     switch (this.opc) {
/*     */     case -3:
/* 805 */       return str + "try " + ((TryData)this.value).getEndLabel().hashCode();
/*     */     case -2:
/* 808 */       return str + "dead";
/*     */     case 132:
/* 811 */       int i = ((int[])(int[])this.value)[0];
/* 812 */       int j = ((int[])(int[])this.value)[1];
/* 813 */       return str + opcNames[this.opc] + " " + i + ", " + j;
/*     */     }
/*     */ 
/* 817 */     if (this.value != null) {
/* 818 */       if ((this.value instanceof Label))
/* 819 */         return str + opcNames[this.opc] + " " + this.value.toString();
/* 820 */       if ((this.value instanceof Instruction))
/* 821 */         return str + opcNames[this.opc] + " " + this.value.hashCode();
/* 822 */       if ((this.value instanceof String)) {
/* 823 */         return str + opcNames[this.opc] + " \"" + this.value + "\"";
/*     */       }
/* 825 */       return str + opcNames[this.opc] + " " + this.value;
/*     */     }
/*     */ 
/* 828 */     return str + opcNames[this.opc];
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 106 */     double d1 = 1.5D;
/* 107 */     String str = System.getProperty("javac.switchratio");
/* 108 */     if (str != null)
/*     */       try {
/* 110 */         double d2 = Double.valueOf(str).doubleValue();
/* 111 */         if ((!Double.isNaN(d2)) && (d2 >= 0.0D))
/* 112 */           d1 = d2;
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.Instruction
 * JD-Core Version:    0.6.2
 */