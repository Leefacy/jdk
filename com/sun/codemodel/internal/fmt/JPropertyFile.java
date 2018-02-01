/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class JPropertyFile extends JResourceFile
/*    */ {
/* 43 */   private final Properties data = new Properties();
/*    */ 
/*    */   public JPropertyFile(String name)
/*    */   {
/* 40 */     super(name);
/*    */   }
/*    */ 
/*    */   public void add(String key, String value)
/*    */   {
/* 51 */     this.data.put(key, value);
/*    */   }
/*    */ 
/*    */   public void build(OutputStream out)
/*    */     throws IOException
/*    */   {
/* 59 */     this.data.store(out, null);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.fmt.JPropertyFile
 * JD-Core Version:    0.6.2
 */