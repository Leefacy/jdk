/*      */ package sun.security.tools.jarsigner;
/*      */ 
/*      */ import com.sun.jarsigner.ContentSignerParameters;
/*      */ import java.net.URI;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.zip.ZipFile;
/*      */ 
/*      */ class JarSignerParameters
/*      */   implements ContentSignerParameters
/*      */ {
/*      */   private String[] args;
/*      */   private URI tsa;
/*      */   private X509Certificate tsaCertificate;
/*      */   private byte[] signature;
/*      */   private String signatureAlgorithm;
/*      */   private X509Certificate[] signerCertificateChain;
/*      */   private byte[] content;
/*      */   private ZipFile source;
/*      */   private String tSAPolicyID;
/*      */ 
/*      */   JarSignerParameters(String[] paramArrayOfString, URI paramURI, X509Certificate paramX509Certificate, String paramString1, byte[] paramArrayOfByte1, String paramString2, X509Certificate[] paramArrayOfX509Certificate, byte[] paramArrayOfByte2, ZipFile paramZipFile)
/*      */   {
/* 2429 */     if ((paramArrayOfByte1 == null) || (paramString2 == null) || (paramArrayOfX509Certificate == null))
/*      */     {
/* 2431 */       throw new NullPointerException();
/*      */     }
/* 2433 */     this.args = paramArrayOfString;
/* 2434 */     this.tsa = paramURI;
/* 2435 */     this.tsaCertificate = paramX509Certificate;
/* 2436 */     this.tSAPolicyID = paramString1;
/* 2437 */     this.signature = paramArrayOfByte1;
/* 2438 */     this.signatureAlgorithm = paramString2;
/* 2439 */     this.signerCertificateChain = paramArrayOfX509Certificate;
/* 2440 */     this.content = paramArrayOfByte2;
/* 2441 */     this.source = paramZipFile;
/*      */   }
/*      */ 
/*      */   public String[] getCommandLine()
/*      */   {
/* 2450 */     return this.args;
/*      */   }
/*      */ 
/*      */   public URI getTimestampingAuthority()
/*      */   {
/* 2459 */     return this.tsa;
/*      */   }
/*      */ 
/*      */   public X509Certificate getTimestampingAuthorityCertificate()
/*      */   {
/* 2468 */     return this.tsaCertificate;
/*      */   }
/*      */ 
/*      */   public String getTSAPolicyID() {
/* 2472 */     return this.tSAPolicyID;
/*      */   }
/*      */ 
/*      */   public byte[] getSignature()
/*      */   {
/* 2481 */     return this.signature;
/*      */   }
/*      */ 
/*      */   public String getSignatureAlgorithm()
/*      */   {
/* 2490 */     return this.signatureAlgorithm;
/*      */   }
/*      */ 
/*      */   public X509Certificate[] getSignerCertificateChain()
/*      */   {
/* 2499 */     return this.signerCertificateChain;
/*      */   }
/*      */ 
/*      */   public byte[] getContent()
/*      */   {
/* 2508 */     return this.content;
/*      */   }
/*      */ 
/*      */   public ZipFile getSource()
/*      */   {
/* 2517 */     return this.source;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.security.tools.jarsigner.JarSignerParameters
 * JD-Core Version:    0.6.2
 */