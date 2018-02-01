/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ 
/*    */ public class OptionFinder
/*    */ {
/*    */   private static final boolean debug = false;
/*    */   List<URL> optionsSources;
/*    */ 
/*    */   public OptionFinder(List<URL> paramList)
/*    */   {
/* 45 */     this.optionsSources = paramList;
/*    */   }
/*    */ 
/*    */   public OptionFormat getOptionFormat(String paramString, boolean paramBoolean) {
/* 49 */     OptionFormat localOptionFormat1 = getOptionFormat(paramString, this.optionsSources);
/* 50 */     OptionFormat localOptionFormat2 = null;
/* 51 */     if ((localOptionFormat1 != null) && (paramBoolean))
/*    */     {
/* 53 */       localOptionFormat2 = getOptionFormat("timestamp", this.optionsSources);
/* 54 */       if (localOptionFormat2 != null) {
/* 55 */         ColumnFormat localColumnFormat = (ColumnFormat)localOptionFormat2.getSubFormat(0);
/* 56 */         localOptionFormat1.insertSubFormat(0, localColumnFormat);
/*    */       }
/*    */     }
/* 59 */     return localOptionFormat1;
/*    */   }
/*    */ 
/*    */   protected OptionFormat getOptionFormat(String paramString, List<URL> paramList) {
/* 63 */     OptionFormat localOptionFormat = null;
/* 64 */     for (URL localURL : paramList) {
/*    */       try
/*    */       {
/* 67 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURL
/* 67 */           .openStream()));
/* 68 */         localOptionFormat = new Parser(localBufferedReader).parse(paramString);
/* 69 */         if (localOptionFormat != null) {
/* 70 */           break;
/*    */         }
/*    */ 
/*    */       }
/*    */       catch (IOException localIOException)
/*    */       {
/*    */       }
/*    */       catch (ParserException localParserException)
/*    */       {
/* 79 */         System.err.println(localURL + ": " + localParserException.getMessage());
/* 80 */         System.err.println("Parsing of " + localURL + " aborted");
/*    */       }
/*    */     }
/* 83 */     return localOptionFormat;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.OptionFinder
 * JD-Core Version:    0.6.2
 */