/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.JCPrimitiveType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ class ConstFold
/*     */ {
/*  45 */   protected static final Context.Key<ConstFold> constFoldKey = new Context.Key();
/*     */   private Symtab syms;
/*  63 */   static final Integer minusOne = Integer.valueOf(-1);
/*  64 */   static final Integer zero = Integer.valueOf(0);
/*  65 */   static final Integer one = Integer.valueOf(1);
/*     */ 
/*     */   public static strictfp ConstFold instance(Context paramContext)
/*     */   {
/*  51 */     ConstFold localConstFold = (ConstFold)paramContext.get(constFoldKey);
/*  52 */     if (localConstFold == null)
/*  53 */       localConstFold = new ConstFold(paramContext);
/*  54 */     return localConstFold;
/*     */   }
/*     */ 
/*     */   private strictfp ConstFold(Context paramContext) {
/*  58 */     paramContext.put(constFoldKey, this);
/*     */ 
/*  60 */     this.syms = Symtab.instance(paramContext);
/*     */   }
/*     */ 
/*     */   private static strictfp Integer b2i(boolean paramBoolean)
/*     */   {
/*  70 */     return paramBoolean ? one : zero;
/*     */   }
/*  72 */   private static strictfp int intValue(Object paramObject) { return ((Number)paramObject).intValue(); } 
/*  73 */   private static strictfp long longValue(Object paramObject) { return ((Number)paramObject).longValue(); } 
/*  74 */   private static strictfp float floatValue(Object paramObject) { return ((Number)paramObject).floatValue(); } 
/*  75 */   private static strictfp double doubleValue(Object paramObject) { return ((Number)paramObject).doubleValue(); }
/*     */ 
/*     */ 
/*     */   strictfp Type fold(int paramInt, List<Type> paramList)
/*     */   {
/*  86 */     int i = paramList.length();
/*  87 */     if (i == 1)
/*  88 */       return fold1(paramInt, (Type)paramList.head);
/*  89 */     if (i == 2) {
/*  90 */       return fold2(paramInt, (Type)paramList.head, (Type)paramList.tail.head);
/*     */     }
/*  92 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   strictfp Type fold1(int paramInt, Type paramType)
/*     */   {
/*     */     try
/*     */     {
/* 105 */       Object localObject = paramType.constValue();
/* 106 */       switch (paramInt) {
/*     */       case 0:
/* 108 */         return paramType;
/*     */       case 116:
/* 110 */         return this.syms.intType.constType(Integer.valueOf(-intValue(localObject)));
/*     */       case 130:
/* 112 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject) ^ 0xFFFFFFFF));
/*     */       case 257:
/* 114 */         return this.syms.booleanType.constType(b2i(intValue(localObject) == 0));
/*     */       case 153:
/* 116 */         return this.syms.booleanType.constType(b2i(intValue(localObject) == 0));
/*     */       case 154:
/* 118 */         return this.syms.booleanType.constType(b2i(intValue(localObject) != 0));
/*     */       case 155:
/* 120 */         return this.syms.booleanType.constType(b2i(intValue(localObject) < 0));
/*     */       case 157:
/* 122 */         return this.syms.booleanType.constType(b2i(intValue(localObject) > 0));
/*     */       case 158:
/* 124 */         return this.syms.booleanType.constType(b2i(intValue(localObject) <= 0));
/*     */       case 156:
/* 126 */         return this.syms.booleanType.constType(b2i(intValue(localObject) >= 0));
/*     */       case 117:
/* 129 */         return this.syms.longType.constType(new Long(-longValue(localObject)));
/*     */       case 131:
/* 131 */         return this.syms.longType.constType(new Long(longValue(localObject) ^ 0xFFFFFFFF));
/*     */       case 118:
/* 134 */         return this.syms.floatType.constType(new Float(-floatValue(localObject)));
/*     */       case 119:
/* 137 */         return this.syms.doubleType.constType(new Double(-doubleValue(localObject)));
/*     */       }
/*     */ 
/* 140 */       return null;
/*     */     } catch (ArithmeticException localArithmeticException) {
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   strictfp Type fold2(int paramInt, Type paramType1, Type paramType2)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       if (paramInt > 511)
/*     */       {
/* 160 */         localObject1 = fold2(paramInt >> 9, paramType1, paramType2);
/*     */ 
/* 162 */         return ((Type)localObject1).constValue() == null ? localObject1 : 
/* 162 */           fold1(paramInt & 0x1FF, (Type)localObject1);
/*     */       }
/*     */ 
/* 164 */       Object localObject1 = paramType1.constValue();
/* 165 */       Object localObject2 = paramType2.constValue();
/* 166 */       switch (paramInt) {
/*     */       case 96:
/* 168 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) + intValue(localObject2)));
/*     */       case 100:
/* 170 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) - intValue(localObject2)));
/*     */       case 104:
/* 172 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) * intValue(localObject2)));
/*     */       case 108:
/* 174 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) / intValue(localObject2)));
/*     */       case 112:
/* 176 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) % intValue(localObject2)));
/*     */       case 126:
/* 180 */         return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/* 180 */           .constType(Integer.valueOf(intValue(localObject1) & 
/* 180 */           intValue(localObject2)));
/*     */       case 258:
/* 182 */         return this.syms.booleanType.constType(b2i((intValue(localObject1) & intValue(localObject2)) != 0));
/*     */       case 128:
/* 186 */         return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/* 186 */           .constType(Integer.valueOf(intValue(localObject1) | 
/* 186 */           intValue(localObject2)));
/*     */       case 259:
/* 188 */         return this.syms.booleanType.constType(b2i((intValue(localObject1) | intValue(localObject2)) != 0));
/*     */       case 130:
/* 192 */         return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/* 192 */           .constType(Integer.valueOf(intValue(localObject1) ^ 
/* 192 */           intValue(localObject2)));
/*     */       case 120:
/*     */       case 270:
/* 194 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) << intValue(localObject2)));
/*     */       case 122:
/*     */       case 272:
/* 196 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) >> intValue(localObject2)));
/*     */       case 124:
/*     */       case 274:
/* 198 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject1) >>> intValue(localObject2)));
/*     */       case 159:
/* 200 */         return this.syms.booleanType.constType(
/* 201 */           b2i(intValue(localObject1) == 
/* 201 */           intValue(localObject2)));
/*     */       case 160:
/* 203 */         return this.syms.booleanType.constType(
/* 204 */           b2i(intValue(localObject1) != 
/* 204 */           intValue(localObject2)));
/*     */       case 161:
/* 206 */         return this.syms.booleanType.constType(
/* 207 */           b2i(intValue(localObject1) < 
/* 207 */           intValue(localObject2)));
/*     */       case 163:
/* 209 */         return this.syms.booleanType.constType(
/* 210 */           b2i(intValue(localObject1) > 
/* 210 */           intValue(localObject2)));
/*     */       case 164:
/* 212 */         return this.syms.booleanType.constType(
/* 213 */           b2i(intValue(localObject1) <= 
/* 213 */           intValue(localObject2)));
/*     */       case 162:
/* 215 */         return this.syms.booleanType.constType(
/* 216 */           b2i(intValue(localObject1) >= 
/* 216 */           intValue(localObject2)));
/*     */       case 97:
/* 219 */         return this.syms.longType.constType(new Long(
/* 220 */           longValue(localObject1) + 
/* 220 */           longValue(localObject2)));
/*     */       case 101:
/* 222 */         return this.syms.longType.constType(new Long(
/* 223 */           longValue(localObject1) - 
/* 223 */           longValue(localObject2)));
/*     */       case 105:
/* 225 */         return this.syms.longType.constType(new Long(
/* 226 */           longValue(localObject1) * 
/* 226 */           longValue(localObject2)));
/*     */       case 109:
/* 228 */         return this.syms.longType.constType(new Long(
/* 229 */           longValue(localObject1) / 
/* 229 */           longValue(localObject2)));
/*     */       case 113:
/* 231 */         return this.syms.longType.constType(new Long(
/* 232 */           longValue(localObject1) % 
/* 232 */           longValue(localObject2)));
/*     */       case 127:
/* 234 */         return this.syms.longType.constType(new Long(
/* 235 */           longValue(localObject1) & 
/* 235 */           longValue(localObject2)));
/*     */       case 129:
/* 237 */         return this.syms.longType.constType(new Long(
/* 238 */           longValue(localObject1) | 
/* 238 */           longValue(localObject2)));
/*     */       case 131:
/* 240 */         return this.syms.longType.constType(new Long(
/* 241 */           longValue(localObject1) ^ 
/* 241 */           longValue(localObject2)));
/*     */       case 121:
/*     */       case 271:
/* 243 */         return this.syms.longType.constType(new Long(
/* 244 */           longValue(localObject1) << 
/* 244 */           intValue(localObject2)));
/*     */       case 123:
/*     */       case 273:
/* 246 */         return this.syms.longType.constType(new Long(
/* 247 */           longValue(localObject1) >> 
/* 247 */           intValue(localObject2)));
/*     */       case 125:
/* 249 */         return this.syms.longType.constType(new Long(
/* 250 */           longValue(localObject1) >>> 
/* 250 */           intValue(localObject2)));
/*     */       case 148:
/* 252 */         if (longValue(localObject1) < longValue(localObject2))
/* 253 */           return this.syms.intType.constType(minusOne);
/* 254 */         if (longValue(localObject1) > longValue(localObject2)) {
/* 255 */           return this.syms.intType.constType(one);
/*     */         }
/* 257 */         return this.syms.intType.constType(zero);
/*     */       case 98:
/* 259 */         return this.syms.floatType.constType(new Float(
/* 260 */           floatValue(localObject1) + 
/* 260 */           floatValue(localObject2)));
/*     */       case 102:
/* 262 */         return this.syms.floatType.constType(new Float(
/* 263 */           floatValue(localObject1) - 
/* 263 */           floatValue(localObject2)));
/*     */       case 106:
/* 265 */         return this.syms.floatType.constType(new Float(
/* 266 */           floatValue(localObject1) * 
/* 266 */           floatValue(localObject2)));
/*     */       case 110:
/* 268 */         return this.syms.floatType.constType(new Float(
/* 269 */           floatValue(localObject1) / 
/* 269 */           floatValue(localObject2)));
/*     */       case 114:
/* 271 */         return this.syms.floatType.constType(new Float(
/* 272 */           floatValue(localObject1) % 
/* 272 */           floatValue(localObject2)));
/*     */       case 149:
/*     */       case 150:
/* 274 */         if (floatValue(localObject1) < floatValue(localObject2))
/* 275 */           return this.syms.intType.constType(minusOne);
/* 276 */         if (floatValue(localObject1) > floatValue(localObject2))
/* 277 */           return this.syms.intType.constType(one);
/* 278 */         if (floatValue(localObject1) == floatValue(localObject2))
/* 279 */           return this.syms.intType.constType(zero);
/* 280 */         if (paramInt == 150) {
/* 281 */           return this.syms.intType.constType(one);
/*     */         }
/* 283 */         return this.syms.intType.constType(minusOne);
/*     */       case 99:
/* 285 */         return this.syms.doubleType.constType(new Double(
/* 286 */           doubleValue(localObject1) + 
/* 286 */           doubleValue(localObject2)));
/*     */       case 103:
/* 288 */         return this.syms.doubleType.constType(new Double(
/* 289 */           doubleValue(localObject1) - 
/* 289 */           doubleValue(localObject2)));
/*     */       case 107:
/* 291 */         return this.syms.doubleType.constType(new Double(
/* 292 */           doubleValue(localObject1) * 
/* 292 */           doubleValue(localObject2)));
/*     */       case 111:
/* 294 */         return this.syms.doubleType.constType(new Double(
/* 295 */           doubleValue(localObject1) / 
/* 295 */           doubleValue(localObject2)));
/*     */       case 115:
/* 297 */         return this.syms.doubleType.constType(new Double(
/* 298 */           doubleValue(localObject1) % 
/* 298 */           doubleValue(localObject2)));
/*     */       case 151:
/*     */       case 152:
/* 300 */         if (doubleValue(localObject1) < doubleValue(localObject2))
/* 301 */           return this.syms.intType.constType(minusOne);
/* 302 */         if (doubleValue(localObject1) > doubleValue(localObject2))
/* 303 */           return this.syms.intType.constType(one);
/* 304 */         if (doubleValue(localObject1) == doubleValue(localObject2))
/* 305 */           return this.syms.intType.constType(zero);
/* 306 */         if (paramInt == 152) {
/* 307 */           return this.syms.intType.constType(one);
/*     */         }
/* 309 */         return this.syms.intType.constType(minusOne);
/*     */       case 165:
/* 311 */         return this.syms.booleanType.constType(b2i(localObject1.equals(localObject2)));
/*     */       case 166:
/* 313 */         return this.syms.booleanType.constType(b2i(!localObject1.equals(localObject2)));
/*     */       case 256:
/* 315 */         return this.syms.stringType.constType(paramType1
/* 316 */           .stringValue() + paramType2.stringValue());
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
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
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 167:
/*     */       case 168:
/*     */       case 169:
/*     */       case 170:
/*     */       case 171:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
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
/*     */       case 191:
/*     */       case 192:
/*     */       case 193:
/*     */       case 194:
/*     */       case 195:
/*     */       case 196:
/*     */       case 197:
/*     */       case 198:
/*     */       case 199:
/*     */       case 200:
/*     */       case 201:
/*     */       case 202:
/*     */       case 203:
/*     */       case 204:
/*     */       case 205:
/*     */       case 206:
/*     */       case 207:
/*     */       case 208:
/*     */       case 209:
/*     */       case 210:
/*     */       case 211:
/*     */       case 212:
/*     */       case 213:
/*     */       case 214:
/*     */       case 215:
/*     */       case 216:
/*     */       case 217:
/*     */       case 218:
/*     */       case 219:
/*     */       case 220:
/*     */       case 221:
/*     */       case 222:
/*     */       case 223:
/*     */       case 224:
/*     */       case 225:
/*     */       case 226:
/*     */       case 227:
/*     */       case 228:
/*     */       case 229:
/*     */       case 230:
/*     */       case 231:
/*     */       case 232:
/*     */       case 233:
/*     */       case 234:
/*     */       case 235:
/*     */       case 236:
/*     */       case 237:
/*     */       case 238:
/*     */       case 239:
/*     */       case 240:
/*     */       case 241:
/*     */       case 242:
/*     */       case 243:
/*     */       case 244:
/*     */       case 245:
/*     */       case 246:
/*     */       case 247:
/*     */       case 248:
/*     */       case 249:
/*     */       case 250:
/*     */       case 251:
/*     */       case 252:
/*     */       case 253:
/*     */       case 254:
/*     */       case 255:
/*     */       case 257:
/*     */       case 260:
/*     */       case 261:
/*     */       case 262:
/*     */       case 263:
/*     */       case 264:
/*     */       case 265:
/*     */       case 266:
/*     */       case 267:
/*     */       case 268:
/* 318 */       case 269: } return null;
/*     */     }
/*     */     catch (ArithmeticException localArithmeticException) {
/*     */     }
/* 322 */     return null;
/*     */   }
/*     */ 
/*     */   strictfp Type coerce(Type paramType1, Type paramType2)
/*     */   {
/* 334 */     if (paramType1.tsym.type == paramType2.tsym.type)
/* 335 */       return paramType1;
/* 336 */     if (paramType1.isNumeric()) {
/* 337 */       Object localObject = paramType1.constValue();
/* 338 */       switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType2.getTag().ordinal()]) {
/*     */       case 1:
/* 340 */         return this.syms.byteType.constType(Integer.valueOf(0 + (byte)intValue(localObject)));
/*     */       case 2:
/* 342 */         return this.syms.charType.constType(Integer.valueOf('\000' + (char)intValue(localObject)));
/*     */       case 3:
/* 344 */         return this.syms.shortType.constType(Integer.valueOf(0 + (short)intValue(localObject)));
/*     */       case 4:
/* 346 */         return this.syms.intType.constType(Integer.valueOf(intValue(localObject)));
/*     */       case 5:
/* 348 */         return this.syms.longType.constType(Long.valueOf(longValue(localObject)));
/*     */       case 6:
/* 350 */         return this.syms.floatType.constType(Float.valueOf(floatValue(localObject)));
/*     */       case 7:
/* 352 */         return this.syms.doubleType.constType(Double.valueOf(doubleValue(localObject)));
/*     */       }
/*     */     }
/* 355 */     return paramType2;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.ConstFold
 * JD-Core Version:    0.6.2
 */