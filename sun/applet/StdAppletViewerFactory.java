/*     */ package sun.applet;
/*     */ 
/*     */ import java.awt.MenuBar;
/*     */ import java.net.URL;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ final class StdAppletViewerFactory
/*     */   implements AppletViewerFactory
/*     */ {
/*     */   public AppletViewer createAppletViewer(int paramInt1, int paramInt2, URL paramURL, Hashtable paramHashtable)
/*     */   {
/*  98 */     return new AppletViewer(paramInt1, paramInt2, paramURL, paramHashtable, System.out, this);
/*     */   }
/*     */ 
/*     */   public MenuBar getBaseMenuBar()
/*     */   {
/* 103 */     return new MenuBar();
/*     */   }
/*     */ 
/*     */   public boolean isStandalone()
/*     */   {
/* 108 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.StdAppletViewerFactory
 * JD-Core Version:    0.6.2
 */