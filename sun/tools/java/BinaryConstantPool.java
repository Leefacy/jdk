/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class BinaryConstantPool
/*     */   implements Constants
/*     */ {
/*     */   private byte[] types;
/*     */   private Object[] cpool;
/*     */   Hashtable indexHashObject;
/*     */   Hashtable indexHashAscii;
/*     */   Vector MoreStuff;
/*     */ 
/*     */   BinaryConstantPool(DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/*  52 */     this.types = new byte[paramDataInputStream.readUnsignedShort()];
/*  53 */     this.cpool = new Object[this.types.length];
/*  54 */     for (int i = 1; i < this.cpool.length; i++) {
/*  55 */       int j = i;
/*     */ 
/*  57 */       switch (this.types[i] = paramDataInputStream.readByte()) {
/*     */       case 1:
/*  59 */         this.cpool[i] = paramDataInputStream.readUTF();
/*  60 */         break;
/*     */       case 3:
/*  63 */         this.cpool[i] = new Integer(paramDataInputStream.readInt());
/*  64 */         break;
/*     */       case 4:
/*  66 */         this.cpool[i] = new Float(paramDataInputStream.readFloat());
/*  67 */         break;
/*     */       case 5:
/*  69 */         this.cpool[(i++)] = new Long(paramDataInputStream.readLong());
/*  70 */         break;
/*     */       case 6:
/*  72 */         this.cpool[(i++)] = new Double(paramDataInputStream.readDouble());
/*  73 */         break;
/*     */       case 7:
/*     */       case 8:
/*  79 */         this.cpool[i] = new Integer(paramDataInputStream.readUnsignedShort());
/*  80 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*  87 */         this.cpool[i] = new Integer(paramDataInputStream.readUnsignedShort() << 16 | paramDataInputStream.readUnsignedShort());
/*  88 */         break;
/*     */       case 15:
/*  91 */         this.cpool[i] = readBytes(paramDataInputStream, 3);
/*  92 */         break;
/*     */       case 16:
/*  94 */         this.cpool[i] = readBytes(paramDataInputStream, 2);
/*  95 */         break;
/*     */       case 18:
/*  97 */         this.cpool[i] = readBytes(paramDataInputStream, 4);
/*  98 */         break;
/*     */       case 0:
/*     */       case 2:
/*     */       case 13:
/*     */       case 14:
/*     */       case 17:
/*     */       default:
/* 102 */         throw new ClassFormatError("invalid constant type: " + this.types[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] readBytes(DataInputStream paramDataInputStream, int paramInt) throws IOException {
/* 108 */     byte[] arrayOfByte = new byte[paramInt];
/* 109 */     paramDataInputStream.readFully(arrayOfByte);
/* 110 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getInteger(int paramInt)
/*     */   {
/* 117 */     return paramInt == 0 ? 0 : ((Number)this.cpool[paramInt]).intValue();
/*     */   }
/*     */ 
/*     */   public Object getValue(int paramInt)
/*     */   {
/* 124 */     return paramInt == 0 ? null : this.cpool[paramInt];
/*     */   }
/*     */ 
/*     */   public String getString(int paramInt)
/*     */   {
/* 131 */     return paramInt == 0 ? null : (String)this.cpool[paramInt];
/*     */   }
/*     */ 
/*     */   public Identifier getIdentifier(int paramInt)
/*     */   {
/* 138 */     return paramInt == 0 ? null : Identifier.lookup(getString(paramInt));
/*     */   }
/*     */ 
/*     */   public ClassDeclaration getDeclarationFromName(Environment paramEnvironment, int paramInt)
/*     */   {
/* 145 */     return paramInt == 0 ? null : paramEnvironment.getClassDeclaration(Identifier.lookup(getString(paramInt).replace('/', '.')));
/*     */   }
/*     */ 
/*     */   public ClassDeclaration getDeclaration(Environment paramEnvironment, int paramInt)
/*     */   {
/* 152 */     return paramInt == 0 ? null : getDeclarationFromName(paramEnvironment, getInteger(paramInt));
/*     */   }
/*     */ 
/*     */   public Type getType(int paramInt)
/*     */   {
/* 159 */     return Type.tType(getString(paramInt));
/*     */   }
/*     */ 
/*     */   public int getConstantType(int paramInt)
/*     */   {
/* 166 */     return this.types[paramInt];
/*     */   }
/*     */ 
/*     */   public Object getConstant(int paramInt, Environment paramEnvironment)
/*     */   {
/* 173 */     int i = getConstantType(paramInt);
/* 174 */     switch (i) {
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 15:
/*     */     case 16:
/*     */     case 18:
/* 182 */       return getValue(paramInt);
/*     */     case 7:
/* 185 */       return getDeclaration(paramEnvironment, paramInt);
/*     */     case 8:
/* 188 */       return getString(getInteger(paramInt));
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */       try
/*     */       {
/* 194 */         int j = getInteger(paramInt);
/*     */ 
/* 196 */         ClassDefinition localClassDefinition = getDeclaration(paramEnvironment, j >> 16)
/* 196 */           .getClassDefinition(paramEnvironment);
/* 197 */         int k = getInteger(j & 0xFFFF);
/* 198 */         Identifier localIdentifier = getIdentifier(k >> 16);
/* 199 */         Type localType1 = getType(k & 0xFFFF);
/*     */ 
/* 201 */         for (MemberDefinition localMemberDefinition = localClassDefinition.getFirstMatch(localIdentifier); 
/* 202 */           localMemberDefinition != null; 
/* 203 */           localMemberDefinition = localMemberDefinition.getNextMatch()) {
/* 204 */           Type localType2 = localMemberDefinition.getType();
/* 205 */           if (i == 9 ? localType2 == localType1 : localType2
/* 207 */             .equalArguments(localType1))
/*     */           {
/* 208 */             return localMemberDefinition;
/*     */           }
/*     */         }
/*     */       } catch (ClassNotFound localClassNotFound) {  }
/*     */ 
/* 212 */       return null;
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/* 215 */     case 17: } throw new ClassFormatError("invalid constant type: " + i);
/*     */   }
/*     */ 
/*     */   public Vector getDependencies(Environment paramEnvironment)
/*     */   {
/* 226 */     Vector localVector = new Vector();
/* 227 */     for (int i = 1; i < this.cpool.length; i++) {
/* 228 */       switch (this.types[i]) {
/*     */       case 7:
/* 230 */         localVector.addElement(getDeclarationFromName(paramEnvironment, getInteger(i)));
/*     */       }
/*     */     }
/*     */ 
/* 234 */     return localVector;
/*     */   }
/*     */ 
/*     */   public int indexObject(Object paramObject, Environment paramEnvironment)
/*     */   {
/* 244 */     if (this.indexHashObject == null)
/* 245 */       createIndexHash(paramEnvironment);
/* 246 */     Integer localInteger = (Integer)this.indexHashObject.get(paramObject);
/* 247 */     if (localInteger == null)
/*     */     {
/* 249 */       throw new IndexOutOfBoundsException("Cannot find object " + paramObject + " of type " + paramObject
/* 249 */         .getClass() + " in constant pool");
/* 250 */     }return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public int indexString(String paramString, Environment paramEnvironment)
/*     */   {
/* 258 */     if (this.indexHashObject == null)
/* 259 */       createIndexHash(paramEnvironment);
/* 260 */     Integer localInteger = (Integer)this.indexHashAscii.get(paramString);
/* 261 */     if (localInteger == null) {
/* 262 */       if (this.MoreStuff == null) this.MoreStuff = new Vector();
/* 263 */       localInteger = new Integer(this.cpool.length + this.MoreStuff.size());
/* 264 */       this.MoreStuff.addElement(paramString);
/* 265 */       this.indexHashAscii.put(paramString, localInteger);
/*     */     }
/* 267 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public void createIndexHash(Environment paramEnvironment)
/*     */   {
/* 276 */     this.indexHashObject = new Hashtable();
/* 277 */     this.indexHashAscii = new Hashtable();
/* 278 */     for (int i = 1; i < this.cpool.length; i++)
/* 279 */       if (this.types[i] == 1)
/* 280 */         this.indexHashAscii.put(this.cpool[i], new Integer(i));
/*     */       else
/*     */         try {
/* 283 */           this.indexHashObject.put(getConstant(i, paramEnvironment), new Integer(i));
/*     */         }
/*     */         catch (ClassFormatError localClassFormatError)
/*     */         {
/*     */         }
/*     */   }
/*     */ 
/*     */   public void write(DataOutputStream paramDataOutputStream, Environment paramEnvironment)
/*     */     throws IOException
/*     */   {
/* 295 */     int i = this.cpool.length;
/* 296 */     if (this.MoreStuff != null)
/* 297 */       i += this.MoreStuff.size();
/* 298 */     paramDataOutputStream.writeShort(i);
/* 299 */     for (int j = 1; j < this.cpool.length; j++) {
/* 300 */       int k = this.types[j];
/* 301 */       Object localObject = this.cpool[j];
/* 302 */       paramDataOutputStream.writeByte(k);
/* 303 */       switch (k) {
/*     */       case 1:
/* 305 */         paramDataOutputStream.writeUTF((String)localObject);
/* 306 */         break;
/*     */       case 3:
/* 308 */         paramDataOutputStream.writeInt(((Number)localObject).intValue());
/* 309 */         break;
/*     */       case 4:
/* 311 */         paramDataOutputStream.writeFloat(((Number)localObject).floatValue());
/* 312 */         break;
/*     */       case 5:
/* 314 */         paramDataOutputStream.writeLong(((Number)localObject).longValue());
/* 315 */         j++;
/* 316 */         break;
/*     */       case 6:
/* 318 */         paramDataOutputStream.writeDouble(((Number)localObject).doubleValue());
/* 319 */         j++;
/* 320 */         break;
/*     */       case 7:
/*     */       case 8:
/* 323 */         paramDataOutputStream.writeShort(((Number)localObject).intValue());
/* 324 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/* 329 */         int m = ((Number)localObject).intValue();
/* 330 */         paramDataOutputStream.writeShort(m >> 16);
/* 331 */         paramDataOutputStream.writeShort(m & 0xFFFF);
/* 332 */         break;
/*     */       case 15:
/*     */       case 16:
/*     */       case 18:
/* 337 */         paramDataOutputStream.write((byte[])localObject, 0, ((byte[])localObject).length);
/* 338 */         break;
/*     */       case 2:
/*     */       case 13:
/*     */       case 14:
/*     */       case 17:
/*     */       default:
/* 340 */         throw new ClassFormatError("invalid constant type: " + this.types[j]);
/*     */       }
/*     */     }
/*     */ 
/* 344 */     for (j = this.cpool.length; j < i; j++) {
/* 345 */       String str = (String)this.MoreStuff.elementAt(j - this.cpool.length);
/* 346 */       paramDataOutputStream.writeByte(1);
/* 347 */       paramDataOutputStream.writeUTF(str);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryConstantPool
 * JD-Core Version:    0.6.2
 */