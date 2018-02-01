/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AliasFileParser
/*     */ {
/*     */   private static final String ALIAS = "alias";
/*     */   private static final boolean DEBUG = false;
/*     */   private URL inputfile;
/*     */   private StreamTokenizer st;
/*     */   private Token currentToken;
/*     */ 
/*     */   AliasFileParser(URL paramURL)
/*     */   {
/*  53 */     this.inputfile = paramURL;
/*     */   }
/*     */ 
/*     */   private void logln(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void nextToken()
/*     */     throws IOException
/*     */   {
/*  77 */     this.st.nextToken();
/*  78 */     this.currentToken = new Token(this.st.ttype, this.st.sval);
/*     */ 
/*  80 */     logln("Read token: type = " + this.currentToken.ttype + " string = " + this.currentToken.sval);
/*     */   }
/*     */ 
/*     */   private void match(int paramInt, String paramString)
/*     */     throws IOException, SyntaxException
/*     */   {
/*  91 */     if ((this.currentToken.ttype == paramInt) && 
/*  92 */       (this.currentToken.sval
/*  92 */       .compareTo(paramString) == 0))
/*     */     {
/*  93 */       logln("matched type: " + paramInt + " and token = " + this.currentToken.sval);
/*     */ 
/*  95 */       nextToken();
/*     */     } else {
/*  97 */       throw new SyntaxException(this.st.lineno());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void match(int paramInt)
/*     */     throws IOException, SyntaxException
/*     */   {
/* 107 */     if (this.currentToken.ttype == paramInt) {
/* 108 */       logln("matched type: " + paramInt + ", token = " + this.currentToken.sval);
/* 109 */       nextToken();
/*     */     } else {
/* 111 */       throw new SyntaxException(this.st.lineno());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void match(String paramString) throws IOException, SyntaxException {
/* 116 */     match(-3, paramString);
/*     */   }
/*     */ 
/*     */   public void parse(Map<String, ArrayList<String>> paramMap)
/*     */     throws SyntaxException, IOException
/*     */   {
/* 124 */     if (this.inputfile == null) {
/* 125 */       return;
/*     */     }
/*     */ 
/* 129 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(this.inputfile
/* 129 */       .openStream()));
/* 130 */     this.st = new StreamTokenizer(localBufferedReader);
/*     */ 
/* 133 */     this.st.slashSlashComments(true);
/* 134 */     this.st.slashStarComments(true);
/* 135 */     this.st.wordChars(95, 95);
/*     */ 
/* 137 */     nextToken();
/*     */ 
/* 139 */     while (this.currentToken.ttype != -1)
/*     */     {
/* 141 */       if ((this.currentToken.ttype != -3) || 
/* 142 */         (this.currentToken.sval
/* 142 */         .compareTo("alias") != 0))
/*     */       {
/* 143 */         nextToken();
/*     */       }
/*     */       else
/*     */       {
/* 147 */         match("alias");
/* 148 */         String str = this.currentToken.sval;
/* 149 */         match(-3);
/*     */ 
/* 151 */         ArrayList localArrayList = new ArrayList();
/*     */         do
/*     */         {
/* 154 */           localArrayList.add(this.currentToken.sval);
/* 155 */           match(-3);
/*     */         }
/* 157 */         while ((this.currentToken.ttype != -1) && 
/* 158 */           (this.currentToken.sval
/* 158 */           .compareTo("alias") != 0));
/*     */ 
/* 160 */         logln("adding map entry for " + str + " values = " + localArrayList);
/*     */ 
/* 162 */         paramMap.put(str, localArrayList);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Token
/*     */   {
/*     */     public String sval;
/*     */     public int ttype;
/*     */ 
/*     */     public Token(int paramString, String arg3)
/*     */     {
/*  62 */       this.ttype = paramString;
/*     */       Object localObject;
/*  63 */       this.sval = localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.AliasFileParser
 * JD-Core Version:    0.6.2
 */