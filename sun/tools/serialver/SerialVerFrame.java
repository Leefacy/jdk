/*     */ package sun.tools.serialver;
/*     */ 
/*     */ import java.awt.Event;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuBar;
/*     */ import java.awt.MenuItem;
/*     */ 
/*     */ class SerialVerFrame extends Frame
/*     */ {
/*     */   MenuBar menu_mb;
/*     */   Menu file_m;
/*     */   MenuItem exit_i;
/*     */   private static final long serialVersionUID = -7248105987187532533L;
/*     */ 
/*     */   SerialVerFrame()
/*     */   {
/* 354 */     super(Res.getText("SerialVersionInspector"));
/*     */ 
/* 357 */     this.file_m = new Menu(Res.getText("File"));
/* 358 */     this.file_m.add(this.exit_i = new MenuItem(Res.getText("Exit")));
/*     */ 
/* 361 */     this.menu_mb = new MenuBar();
/* 362 */     this.menu_mb.add(this.file_m);
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event paramEvent)
/*     */   {
/* 373 */     if (paramEvent.id == 201) {
/* 374 */       exit(0);
/*     */     }
/* 376 */     return super.handleEvent(paramEvent);
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject)
/*     */   {
/* 383 */     if (paramEvent.target == this.exit_i) {
/* 384 */       exit(0);
/*     */     }
/* 386 */     return false;
/*     */   }
/*     */ 
/*     */   void exit(int paramInt)
/*     */   {
/* 393 */     System.exit(paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.serialver.SerialVerFrame
 * JD-Core Version:    0.6.2
 */