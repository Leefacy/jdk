/*    */ package sun.applet;
/*    */ 
/*    */ import java.awt.Button;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Panel;
/*    */ import java.awt.TextArea;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ 
/*    */ final class TextFrame extends Frame
/*    */ {
/* 86 */   private static AppletMessageHandler amh = new AppletMessageHandler("textframe");
/*    */ 
/*    */   TextFrame(int paramInt1, int paramInt2, String paramString1, String paramString2)
/*    */   {
/* 52 */     setTitle(paramString1);
/* 53 */     TextArea localTextArea = new TextArea(20, 60);
/* 54 */     localTextArea.setText(paramString2);
/* 55 */     localTextArea.setEditable(false);
/*    */ 
/* 57 */     add("Center", localTextArea);
/*    */ 
/* 59 */     Panel localPanel = new Panel();
/* 60 */     add("South", localPanel);
/* 61 */     Button localButton = new Button(amh.getMessage("button.dismiss", "Dismiss"));
/* 62 */     localPanel.add(localButton);
/*    */ 
/* 70 */     localButton.addActionListener(new ActionListener()
/*    */     {
/*    */       public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*    */       {
/* 67 */         TextFrame.this.dispose();
/*    */       }
/*    */     });
/* 72 */     pack();
/* 73 */     move(paramInt1, paramInt2);
/* 74 */     setVisible(true);
/*    */ 
/* 76 */     WindowAdapter local1 = new WindowAdapter()
/*    */     {
/*    */       public void windowClosing(WindowEvent paramAnonymousWindowEvent)
/*    */       {
/* 80 */         TextFrame.this.dispose();
/*    */       }
/*    */     };
/* 84 */     addWindowListener(local1);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.applet.TextFrame
 * JD-Core Version:    0.6.2
 */