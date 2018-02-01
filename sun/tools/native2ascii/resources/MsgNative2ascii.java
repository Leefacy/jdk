/*    */ package sun.tools.native2ascii.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class MsgNative2ascii extends ListResourceBundle
/*    */ {
/*    */   public Object[][] getContents()
/*    */   {
/* 33 */     Object[][] arrayOfObject; = { { "err.bad.arg", "-encoding requires argument" }, { "err.cannot.read", "{0} could not be read." }, { "err.cannot.write", "{0} could not be written." }, { "usage", "Usage: native2ascii [-reverse] [-encoding encoding] [inputfile [outputfile]]" } };
/*    */ 
/* 41 */     return arrayOfObject;;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.native2ascii.resources.MsgNative2ascii
 * JD-Core Version:    0.6.2
 */