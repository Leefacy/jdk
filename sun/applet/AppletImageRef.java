/*    */ package sun.applet;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.net.URL;
/*    */ import sun.awt.image.URLImageSource;
/*    */ import sun.misc.Ref;
/*    */ 
/*    */ class AppletImageRef extends Ref
/*    */ {
/*    */   URL url;
/*    */ 
/*    */   AppletImageRef(URL paramURL)
/*    */   {
/* 40 */     this.url = paramURL;
/*    */   }
/*    */ 
/*    */   public void flush() {
/* 44 */     super.flush();
/*    */   }
/*    */ 
/*    */   public Object reconstitute()
/*    */   {
/* 51 */     Image localImage = Toolkit.getDefaultToolkit().createImage(new URLImageSource(this.url));
/* 52 */     return localImage;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.AppletImageRef
 * JD-Core Version:    0.6.2
 */