/*     */ package sun.tools.jstat;
/*     */ 
/*     */ public class Token
/*     */ {
/*     */   public String sval;
/*     */   public double nval;
/*     */   public int ttype;
/*     */ 
/*     */   public Token(int paramInt, String paramString, double paramDouble)
/*     */   {
/*  43 */     this.ttype = paramInt;
/*  44 */     this.sval = paramString;
/*  45 */     this.nval = paramDouble;
/*     */   }
/*     */ 
/*     */   public Token(int paramInt, String paramString) {
/*  49 */     this(paramInt, paramString, 0.0D);
/*     */   }
/*     */ 
/*     */   public Token(int paramInt) {
/*  53 */     this(paramInt, null, 0.0D);
/*     */   }
/*     */ 
/*     */   public String toMessage() {
/*  57 */     switch (this.ttype) {
/*     */     case 10:
/*  59 */       return "\"EOL\"";
/*     */     case -1:
/*  61 */       return "\"EOF\"";
/*     */     case -2:
/*  63 */       return "NUMBER";
/*     */     case -3:
/*  65 */       if (this.sval == null) {
/*  66 */         return "IDENTIFIER";
/*     */       }
/*  68 */       return "IDENTIFIER " + this.sval;
/*     */     }
/*     */ 
/*  71 */     if (this.ttype == 34) {
/*  72 */       String str = "QUOTED STRING";
/*  73 */       if (this.sval != null)
/*  74 */         str = str + " \"" + this.sval + "\"";
/*  75 */       return str;
/*     */     }
/*  77 */     return "CHARACTER '" + (char)this.ttype + "'";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  83 */     StringBuilder localStringBuilder = new StringBuilder();
/*  84 */     switch (this.ttype) {
/*     */     case 10:
/*  86 */       localStringBuilder.append("ttype=TT_EOL");
/*  87 */       break;
/*     */     case -1:
/*  89 */       localStringBuilder.append("ttype=TT_EOF");
/*  90 */       break;
/*     */     case -2:
/*  92 */       localStringBuilder.append("ttype=TT_NUM,").append("nval=" + this.nval);
/*  93 */       break;
/*     */     case -3:
/*  95 */       if (this.sval == null)
/*  96 */         localStringBuilder.append("ttype=TT_WORD:IDENTIFIER");
/*     */       else {
/*  98 */         localStringBuilder.append("ttype=TT_WORD:").append("sval=" + this.sval);
/*     */       }
/* 100 */       break;
/*     */     default:
/* 102 */       if (this.ttype == 34)
/* 103 */         localStringBuilder.append("ttype=TT_STRING:").append("sval=" + this.sval);
/*     */       else {
/* 105 */         localStringBuilder.append("ttype=TT_CHAR:").append((char)this.ttype);
/*     */       }
/*     */       break;
/*     */     }
/* 109 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Token
 * JD-Core Version:    0.6.2
 */