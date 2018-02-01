/*    */ package com.sun.jarsigner;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.zip.ZipFile;
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public abstract interface ContentSignerParameters
/*    */ {
/*    */   public abstract String[] getCommandLine();
/*    */ 
/*    */   public abstract URI getTimestampingAuthority();
/*    */ 
/*    */   public abstract X509Certificate getTimestampingAuthorityCertificate();
/*    */ 
/*    */   public String getTSAPolicyID()
/*    */   {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   public abstract byte[] getSignature();
/*    */ 
/*    */   public abstract String getSignatureAlgorithm();
/*    */ 
/*    */   public abstract X509Certificate[] getSignerCertificateChain();
/*    */ 
/*    */   public abstract byte[] getContent();
/*    */ 
/*    */   public abstract ZipFile getSource();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jarsigner.ContentSignerParameters
 * JD-Core Version:    0.6.2
 */