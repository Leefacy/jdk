/*    */ package sun.applet;
/*    */ 
/*    */ public class AppletThreadGroup extends ThreadGroup
/*    */ {
/*    */   public AppletThreadGroup(String paramString)
/*    */   {
/* 43 */     this(Thread.currentThread().getThreadGroup(), paramString);
/*    */   }
/*    */ 
/*    */   public AppletThreadGroup(ThreadGroup paramThreadGroup, String paramString)
/*    */   {
/* 61 */     super(paramThreadGroup, paramString);
/* 62 */     setMaxPriority(4);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.AppletThreadGroup
 * JD-Core Version:    0.6.2
 */