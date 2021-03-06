/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import java.net.URL;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public final class AuthInfo
/*    */ {
/*    */   private final String user;
/*    */   private final String password;
/*    */   private final Pattern urlPattern;
/*    */ 
/*    */   public AuthInfo(@NotNull URL url, @NotNull String user, @NotNull String password)
/*    */   {
/* 46 */     String u = url.toExternalForm().replaceFirst("\\?", "\\\\?");
/* 47 */     this.urlPattern = Pattern.compile(u.replace("*", ".*"), 2);
/* 48 */     this.user = user;
/* 49 */     this.password = password;
/*    */   }
/*    */ 
/*    */   public String getUser() {
/* 53 */     return this.user;
/*    */   }
/*    */ 
/*    */   public String getPassword() {
/* 57 */     return this.password;
/*    */   }
/*    */ 
/*    */   public boolean matchingHost(@NotNull URL requestingURL)
/*    */   {
/* 64 */     return this.urlPattern.matcher(requestingURL.toExternalForm()).matches();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wscompile.AuthInfo
 * JD-Core Version:    0.6.2
 */