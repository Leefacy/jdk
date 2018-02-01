/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URL;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ 
/*    */ public class OptionLister
/*    */ {
/*    */   private static final boolean debug = false;
/*    */   private List<URL> sources;
/*    */ 
/*    */   public OptionLister(List<URL> paramList)
/*    */   {
/* 43 */     this.sources = paramList;
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream) {
/* 47 */     Comparator local1 = new Comparator() {
/*    */       public int compare(OptionFormat paramAnonymousOptionFormat1, OptionFormat paramAnonymousOptionFormat2) {
/* 49 */         OptionFormat localOptionFormat1 = paramAnonymousOptionFormat1;
/* 50 */         OptionFormat localOptionFormat2 = paramAnonymousOptionFormat2;
/* 51 */         return localOptionFormat1.getName().compareTo(localOptionFormat2.getName());
/*    */       }
/*    */     };
/* 55 */     TreeSet localTreeSet = new TreeSet(local1);
/*    */ 
/* 57 */     for (Iterator localIterator = this.sources.iterator(); localIterator.hasNext(); ) { localObject = (URL)localIterator.next();
/*    */       try
/*    */       {
/* 60 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(((URL)localObject)
/* 60 */           .openStream()));
/* 61 */         Set localSet = new Parser(localBufferedReader).parseOptions();
/* 62 */         localTreeSet.addAll(localSet);
/*    */       }
/*    */       catch (IOException localIOException)
/*    */       {
/*    */       }
/*    */       catch (ParserException localParserException)
/*    */       {
/* 70 */         System.err.println(localObject + ": " + localParserException.getMessage());
/* 71 */         System.err.println("Parsing of " + localObject + " aborted");
/*    */       }
/*    */     }
/* 75 */     Object localObject;
/* 75 */     for (localIterator = localTreeSet.iterator(); localIterator.hasNext(); ) { localObject = (OptionFormat)localIterator.next();
/* 76 */       if (((OptionFormat)localObject).getName().compareTo("timestamp") != 0)
/*    */       {
/* 80 */         paramPrintStream.println("-" + ((OptionFormat)localObject).getName());
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.OptionLister
 * JD-Core Version:    0.6.2
 */