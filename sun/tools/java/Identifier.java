/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class Identifier
/*     */   implements Constants
/*     */ {
/*  61 */   static Hashtable hash = new Hashtable(3001, 0.5F);
/*     */   String name;
/*     */   Object value;
/*  80 */   Type typeObject = null;
/*     */   private int ipos;
/*     */   public static final char INNERCLASS_PREFIX = ' ';
/*     */   private static final String ambigPrefix = "<<ambiguous>>";
/*     */ 
/*     */   private Identifier(String paramString)
/*     */   {
/*  93 */     this.name = paramString;
/*  94 */     this.ipos = paramString.indexOf(' ');
/*     */   }
/*     */ 
/*     */   int getType()
/*     */   {
/* 102 */     return (this.value != null) && ((this.value instanceof Integer)) ? ((Integer)this.value)
/* 102 */       .intValue() : 60;
/*     */   }
/*     */ 
/*     */   void setType(int paramInt)
/*     */   {
/* 109 */     this.value = new Integer(paramInt);
/*     */   }
/*     */ 
/*     */   public static synchronized Identifier lookup(String paramString)
/*     */   {
/* 118 */     Identifier localIdentifier = (Identifier)hash.get(paramString);
/* 119 */     if (localIdentifier == null) {
/* 120 */       hash.put(paramString, localIdentifier = new Identifier(paramString));
/*     */     }
/* 122 */     return localIdentifier;
/*     */   }
/*     */ 
/*     */   public static Identifier lookup(Identifier paramIdentifier1, Identifier paramIdentifier2)
/*     */   {
/* 130 */     if (paramIdentifier1 == idNull) return paramIdentifier2;
/*     */ 
/* 132 */     if (paramIdentifier1.name.charAt(paramIdentifier1.name.length() - 1) == ' ')
/* 133 */       return lookup(paramIdentifier1.name + paramIdentifier2.name);
/* 134 */     Identifier localIdentifier = lookup(paramIdentifier1 + "." + paramIdentifier2);
/* 135 */     if ((!paramIdentifier2.isQualified()) && (!paramIdentifier1.isInner()))
/* 136 */       localIdentifier.value = paramIdentifier1;
/* 137 */     return localIdentifier;
/*     */   }
/*     */ 
/*     */   public static Identifier lookupInner(Identifier paramIdentifier1, Identifier paramIdentifier2)
/*     */   {
/*     */     Identifier localIdentifier;
/* 146 */     if (paramIdentifier1.isInner()) {
/* 147 */       if (paramIdentifier1.name.charAt(paramIdentifier1.name.length() - 1) == ' ')
/* 148 */         localIdentifier = lookup(paramIdentifier1.name + paramIdentifier2);
/*     */       else
/* 150 */         localIdentifier = lookup(paramIdentifier1, paramIdentifier2);
/*     */     }
/* 152 */     else localIdentifier = lookup(paramIdentifier1 + "." + ' ' + paramIdentifier2);
/*     */ 
/* 154 */     localIdentifier.value = paramIdentifier1.value;
/* 155 */     return localIdentifier;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 162 */     return this.name;
/*     */   }
/*     */ 
/*     */   public boolean isQualified()
/*     */   {
/* 169 */     if (this.value == null) {
/* 170 */       int i = this.ipos;
/* 171 */       if (i <= 0)
/* 172 */         i = this.name.length();
/*     */       else
/* 174 */         i--;
/* 175 */       int j = this.name.lastIndexOf('.', i - 1);
/* 176 */       this.value = (j < 0 ? idNull : lookup(this.name.substring(0, j)));
/*     */     }
/* 178 */     return ((this.value instanceof Identifier)) && (this.value != idNull);
/*     */   }
/*     */ 
/*     */   public Identifier getQualifier()
/*     */   {
/* 187 */     return isQualified() ? (Identifier)this.value : idNull;
/*     */   }
/*     */ 
/*     */   public Identifier getName()
/*     */   {
/* 197 */     return isQualified() ? 
/* 197 */       lookup(this.name
/* 197 */       .substring(((Identifier)this.value).name
/* 197 */       .length() + 1)) : this;
/*     */   }
/*     */ 
/*     */   public boolean isInner()
/*     */   {
/* 224 */     return this.ipos > 0;
/*     */   }
/*     */ 
/*     */   public Identifier getFlatName()
/*     */   {
/* 242 */     if (isQualified()) {
/* 243 */       return getName().getFlatName();
/*     */     }
/* 245 */     if ((this.ipos > 0) && (this.name.charAt(this.ipos - 1) == '.')) {
/* 246 */       if (this.ipos + 1 == this.name.length())
/*     */       {
/* 248 */         return lookup(this.name.substring(0, this.ipos - 1));
/*     */       }
/* 250 */       String str1 = this.name.substring(this.ipos + 1);
/* 251 */       String str2 = this.name.substring(0, this.ipos);
/* 252 */       return lookup(str2 + str1);
/*     */     }
/*     */ 
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */   public Identifier getTopName() {
/* 259 */     if (!isInner()) return this;
/* 260 */     return lookup(getQualifier(), getFlatName().getHead());
/*     */   }
/*     */ 
/*     */   public Identifier getHead()
/*     */   {
/* 269 */     Identifier localIdentifier = this;
/* 270 */     while (localIdentifier.isQualified())
/* 271 */       localIdentifier = localIdentifier.getQualifier();
/* 272 */     return localIdentifier;
/*     */   }
/*     */ 
/*     */   public Identifier getTail()
/*     */   {
/* 279 */     Identifier localIdentifier = getHead();
/* 280 */     if (localIdentifier == this) {
/* 281 */       return idNull;
/*     */     }
/* 283 */     return lookup(this.name.substring(localIdentifier.name.length() + 1));
/*     */   }
/*     */ 
/*     */   public boolean hasAmbigPrefix()
/*     */   {
/* 307 */     return this.name.startsWith("<<ambiguous>>");
/*     */   }
/*     */ 
/*     */   public Identifier addAmbigPrefix()
/*     */   {
/* 316 */     return lookup("<<ambiguous>>" + this.name);
/*     */   }
/*     */ 
/*     */   public Identifier removeAmbigPrefix()
/*     */   {
/* 323 */     if (hasAmbigPrefix()) {
/* 324 */       return lookup(this.name.substring("<<ambiguous>>".length()));
/*     */     }
/* 326 */     return this;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Identifier
 * JD-Core Version:    0.6.2
 */