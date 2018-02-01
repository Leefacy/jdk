/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public class PrimitiveType extends Type
/*     */ {
/*     */   public static PrimitiveType forPrimitive(sun.tools.java.Type paramType, ContextStack paramContextStack)
/*     */   {
/*  63 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  67 */     Type localType = getType(paramType, paramContextStack);
/*     */ 
/*  69 */     if (localType != null)
/*     */     {
/*  71 */       if (!(localType instanceof PrimitiveType)) return null;
/*     */ 
/*  75 */       return (PrimitiveType)localType;
/*     */     }
/*     */     int i;
/*  80 */     switch (paramType.getTypeCode()) { case 11:
/*  81 */       i = 1; break;
/*     */     case 0:
/*  82 */       i = 2; break;
/*     */     case 1:
/*  83 */       i = 4; break;
/*     */     case 2:
/*  84 */       i = 8; break;
/*     */     case 3:
/*  85 */       i = 16; break;
/*     */     case 4:
/*  86 */       i = 32; break;
/*     */     case 5:
/*  87 */       i = 64; break;
/*     */     case 6:
/*  88 */       i = 128; break;
/*     */     case 7:
/*  89 */       i = 256; break;
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     default:
/*  90 */       return null;
/*     */     }
/*     */ 
/*  93 */     PrimitiveType localPrimitiveType = new PrimitiveType(paramContextStack, i);
/*     */ 
/*  97 */     putType(paramType, localPrimitiveType, paramContextStack);
/*     */ 
/* 101 */     paramContextStack.push(localPrimitiveType);
/* 102 */     paramContextStack.pop(true);
/*     */ 
/* 104 */     return localPrimitiveType;
/*     */   }
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 112 */     switch (getTypeCode()) { case 1:
/* 113 */       return "V";
/*     */     case 2:
/* 114 */       return "Z";
/*     */     case 4:
/* 115 */       return "B";
/*     */     case 8:
/* 116 */       return "C";
/*     */     case 16:
/* 117 */       return "S";
/*     */     case 32:
/* 118 */       return "I";
/*     */     case 64:
/* 119 */       return "J";
/*     */     case 128:
/* 120 */       return "F";
/*     */     case 256:
/* 121 */       return "D"; }
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 130 */     return "Primitive";
/*     */   }
/*     */ 
/*     */   public String getQualifiedIDLName(boolean paramBoolean)
/*     */   {
/* 140 */     return super.getQualifiedIDLName(false);
/*     */   }
/*     */ 
/*     */   protected Class loadClass()
/*     */   {
/* 151 */     switch (getTypeCode()) { case 1:
/* 152 */       return Null.class;
/*     */     case 2:
/* 153 */       return Boolean.TYPE;
/*     */     case 4:
/* 154 */       return Byte.TYPE;
/*     */     case 8:
/* 155 */       return Character.TYPE;
/*     */     case 16:
/* 156 */       return Short.TYPE;
/*     */     case 32:
/* 157 */       return Integer.TYPE;
/*     */     case 64:
/* 158 */       return Long.TYPE;
/*     */     case 128:
/* 159 */       return Float.TYPE;
/*     */     case 256:
/* 160 */       return Double.TYPE; }
/* 161 */     throw new CompilerError("Not a primitive type");
/*     */   }
/*     */ 
/*     */   private PrimitiveType(ContextStack paramContextStack, int paramInt)
/*     */   {
/* 170 */     super(paramContextStack, paramInt | 0x1000000);
/*     */ 
/* 174 */     String str = IDLNames.getTypeName(paramInt, false);
/* 175 */     Identifier localIdentifier = null;
/*     */ 
/* 177 */     switch (paramInt) { case 1:
/* 178 */       localIdentifier = idVoid; break;
/*     */     case 2:
/* 179 */       localIdentifier = idBoolean; break;
/*     */     case 4:
/* 180 */       localIdentifier = idByte; break;
/*     */     case 8:
/* 181 */       localIdentifier = idChar; break;
/*     */     case 16:
/* 182 */       localIdentifier = idShort; break;
/*     */     case 32:
/* 183 */       localIdentifier = idInt; break;
/*     */     case 64:
/* 184 */       localIdentifier = idLong; break;
/*     */     case 128:
/* 185 */       localIdentifier = idFloat; break;
/*     */     case 256:
/* 186 */       localIdentifier = idDouble; break;
/*     */     default:
/* 187 */       throw new CompilerError("Not a primitive type");
/*     */     }
/*     */ 
/* 190 */     setNames(localIdentifier, null, str);
/* 191 */     setRepositoryID();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.PrimitiveType
 * JD-Core Version:    0.6.2
 */