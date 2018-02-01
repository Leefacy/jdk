/*     */ package sun.security.tools.jarsigner;
/*     */ 
/*     */ import com.sun.jarsigner.ContentSigner;
/*     */ import com.sun.jarsigner.ContentSignerParameters;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.URIName;
/*     */ 
/*     */ public final class TimestampedSigner extends ContentSigner
/*     */ {
/*     */   private static final String SUBJECT_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.11";
/*  68 */   private static final ObjectIdentifier AD_TIMESTAMPING_Id = localObjectIdentifier;
/*     */ 
/*     */   public byte[] generateSignedData(ContentSignerParameters paramContentSignerParameters, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws NoSuchAlgorithmException, CertificateException, IOException
/*     */   {
/* 106 */     if (paramContentSignerParameters == null) {
/* 107 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 114 */     String str = paramContentSignerParameters.getSignatureAlgorithm();
/*     */ 
/* 116 */     X509Certificate[] arrayOfX509Certificate = paramContentSignerParameters.getSignerCertificateChain();
/* 117 */     byte[] arrayOfByte1 = paramContentSignerParameters.getSignature();
/*     */ 
/* 120 */     byte[] arrayOfByte2 = paramBoolean1 == true ? null : paramContentSignerParameters.getContent();
/*     */ 
/* 122 */     URI localURI = null;
/* 123 */     if (paramBoolean2) {
/* 124 */       localURI = paramContentSignerParameters.getTimestampingAuthority();
/* 125 */       if (localURI == null)
/*     */       {
/* 127 */         localURI = getTimestampingURI(paramContentSignerParameters
/* 128 */           .getTimestampingAuthorityCertificate());
/* 129 */         if (localURI == null) {
/* 130 */           throw new CertificateException("Subject Information Access extension not found");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 135 */     return PKCS7.generateSignedData(arrayOfByte1, arrayOfX509Certificate, arrayOfByte2, paramContentSignerParameters
/* 136 */       .getSignatureAlgorithm(), localURI, paramContentSignerParameters
/* 137 */       .getTSAPolicyID());
/*     */   }
/*     */ 
/*     */   public static URI getTimestampingURI(X509Certificate paramX509Certificate)
/*     */   {
/* 152 */     if (paramX509Certificate == null) {
/* 153 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 158 */       byte[] arrayOfByte = paramX509Certificate
/* 158 */         .getExtensionValue("1.3.6.1.5.5.7.1.11");
/*     */ 
/* 159 */       if (arrayOfByte == null) {
/* 160 */         return null;
/*     */       }
/* 162 */       DerInputStream localDerInputStream = new DerInputStream(arrayOfByte);
/* 163 */       localDerInputStream = new DerInputStream(localDerInputStream.getOctetString());
/* 164 */       DerValue[] arrayOfDerValue = localDerInputStream.getSequence(5);
/*     */ 
/* 168 */       for (int i = 0; i < arrayOfDerValue.length; i++) {
/* 169 */         AccessDescription localAccessDescription = new AccessDescription(arrayOfDerValue[i]);
/*     */ 
/* 171 */         if (localAccessDescription.getAccessMethod()
/* 171 */           .equals(AD_TIMESTAMPING_Id))
/*     */         {
/* 172 */           GeneralName localGeneralName = localAccessDescription.getAccessLocation();
/* 173 */           if (localGeneralName.getType() == 6) {
/* 174 */             URIName localURIName = (URIName)localGeneralName.getName();
/* 175 */             if ((localURIName.getScheme().equalsIgnoreCase("http")) || 
/* 176 */               (localURIName
/* 176 */               .getScheme().equalsIgnoreCase("https")))
/* 177 */               return localURIName.getURI();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  62 */     ObjectIdentifier localObjectIdentifier = null;
/*     */     try {
/*  64 */       localObjectIdentifier = new ObjectIdentifier("1.3.6.1.5.5.7.48.3");
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.security.tools.jarsigner.TimestampedSigner
 * JD-Core Version:    0.6.2
 */