/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class ScannerInputReader extends FilterReader
/*     */   implements Constants
/*     */ {
/*     */   Environment env;
/*     */   long pos;
/*     */   private long chpos;
/*  64 */   private int pushBack = -1;
/*     */   private static final int BUFFERLEN = 10240;
/*  91 */   private final char[] buffer = new char[10240];
/*     */   private int currentIndex;
/*     */   private int numChars;
/*     */ 
/*     */   public ScannerInputReader(Environment paramEnvironment, InputStream paramInputStream)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  72 */     super(paramEnvironment.getCharacterEncoding() != null ? new InputStreamReader(paramInputStream, paramEnvironment
/*  73 */       .getCharacterEncoding()) : new InputStreamReader(paramInputStream));
/*     */ 
/*  77 */     this.currentIndex = 0;
/*  78 */     this.numChars = 0;
/*     */ 
/*  80 */     this.env = paramEnvironment;
/*  81 */     this.chpos = 4294967296L;
/*     */   }
/*     */ 
/*     */   private int getNextChar()
/*     */     throws IOException
/*     */   {
/* 108 */     if (this.currentIndex >= this.numChars) {
/* 109 */       this.numChars = this.in.read(this.buffer);
/* 110 */       if (this.numChars == -1)
/*     */       {
/* 112 */         return -1;
/*     */       }
/*     */ 
/* 116 */       this.currentIndex = 0;
/*     */     }
/*     */ 
/* 119 */     return this.buffer[(this.currentIndex++)];
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 125 */     throw new CompilerError("ScannerInputReader is not a fully implemented reader.");
/*     */   }
/*     */ 
/*     */   public int read() throws IOException
/*     */   {
/* 130 */     this.pos = this.chpos;
/* 131 */     this.chpos += 1L;
/*     */ 
/* 133 */     int i = this.pushBack;
/* 134 */     if (i == -1)
/*     */     {
/*     */       try
/*     */       {
/* 140 */         if (this.currentIndex >= this.numChars) {
/* 141 */           this.numChars = this.in.read(this.buffer);
/* 142 */           if (this.numChars == -1)
/*     */           {
/* 144 */             i = -1;
/*     */           }
/*     */           else
/*     */           {
/* 149 */             this.currentIndex = 0;
/*     */           }
/*     */         } else { i = this.buffer[(this.currentIndex++)]; }
/*     */       }
/*     */       catch (CharConversionException localCharConversionException) {
/* 154 */         this.env.error(this.pos, "invalid.encoding.char");
/*     */ 
/* 156 */         return -1;
/*     */       }
/*     */     }
/* 159 */     else this.pushBack = -1;
/*     */ 
/* 163 */     switch (i)
/*     */     {
/*     */     case -2:
/* 167 */       return 92;
/*     */     case 92:
/* 170 */       if ((i = getNextChar()) != 117) {
/* 171 */         this.pushBack = (i == 92 ? -2 : i);
/* 172 */         return 92;
/*     */       }
/*     */ 
/* 175 */       this.chpos += 1L;
/* 176 */       while ((i = getNextChar()) == 117) {
/* 177 */         this.chpos += 1L;
/*     */       }
/*     */ 
/* 181 */       int j = 0;
/* 182 */       for (int k = 0; k < 4; i = getNextChar()) {
/* 183 */         switch (i) { case 48:
/*     */         case 49:
/*     */         case 50:
/*     */         case 51:
/*     */         case 52:
/*     */         case 53:
/*     */         case 54:
/*     */         case 55:
/*     */         case 56:
/*     */         case 57:
/* 186 */           j = (j << 4) + i - 48;
/* 187 */           break;
/*     */         case 97:
/*     */         case 98:
/*     */         case 99:
/*     */         case 100:
/*     */         case 101:
/*     */         case 102:
/* 190 */           j = (j << 4) + 10 + i - 97;
/* 191 */           break;
/*     */         case 65:
/*     */         case 66:
/*     */         case 67:
/*     */         case 68:
/*     */         case 69:
/*     */         case 70:
/* 194 */           j = (j << 4) + 10 + i - 65;
/* 195 */           break;
/*     */         case 58:
/*     */         case 59:
/*     */         case 60:
/*     */         case 61:
/*     */         case 62:
/*     */         case 63:
/*     */         case 64:
/*     */         case 71:
/*     */         case 72:
/*     */         case 73:
/*     */         case 74:
/*     */         case 75:
/*     */         case 76:
/*     */         case 77:
/*     */         case 78:
/*     */         case 79:
/*     */         case 80:
/*     */         case 81:
/*     */         case 82:
/*     */         case 83:
/*     */         case 84:
/*     */         case 85:
/*     */         case 86:
/*     */         case 87:
/*     */         case 88:
/*     */         case 89:
/*     */         case 90:
/*     */         case 91:
/*     */         case 92:
/*     */         case 93:
/*     */         case 94:
/*     */         case 95:
/*     */         case 96:
/*     */         default:
/* 198 */           this.env.error(this.pos, "invalid.escape.char");
/* 199 */           this.pushBack = i;
/* 200 */           return j;
/*     */         }
/* 182 */         k++; this.chpos += 1L;
/*     */       }
/*     */ 
/* 203 */       this.pushBack = i;
/*     */ 
/* 209 */       switch (j) {
/*     */       case 10:
/* 211 */         this.chpos += 4294967296L;
/* 212 */         return 10;
/*     */       case 13:
/* 214 */         if ((i = getNextChar()) != 10)
/* 215 */           this.pushBack = i;
/*     */         else {
/* 217 */           this.chpos += 1L;
/*     */         }
/* 219 */         this.chpos += 4294967296L;
/* 220 */         return 10;
/*     */       }
/* 222 */       return j;
/*     */     case 10:
/* 226 */       this.chpos += 4294967296L;
/* 227 */       return 10;
/*     */     case 13:
/* 230 */       if ((i = getNextChar()) != 10)
/* 231 */         this.pushBack = i;
/*     */       else {
/* 233 */         this.chpos += 1L;
/*     */       }
/* 235 */       this.chpos += 4294967296L;
/* 236 */       return 10;
/*     */     }
/*     */ 
/* 239 */     return i;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ScannerInputReader
 * JD-Core Version:    0.6.2
 */