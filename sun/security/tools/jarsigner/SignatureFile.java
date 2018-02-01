/*      */ package sun.security.tools.jarsigner;
/*      */ 
/*      */ import com.sun.jarsigner.ContentSigner;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.Signature;
/*      */ import java.security.SignatureException;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.Base64;
/*      */ import java.util.Base64.Encoder;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.zip.ZipFile;
/*      */ import sun.security.util.ManifestDigester;
/*      */ import sun.security.util.ManifestDigester.Entry;
/*      */ import sun.security.x509.AlgorithmId;
/*      */ import sun.security.x509.X500Name;
/*      */ import sun.security.x509.X509CertInfo;
/*      */ 
/*      */ class SignatureFile
/*      */ {
/*      */   Manifest sf;
/*      */   String baseName;
/*      */ 
/*      */   public SignatureFile(MessageDigest[] paramArrayOfMessageDigest, Manifest paramManifest, ManifestDigester paramManifestDigester, String paramString, boolean paramBoolean)
/*      */   {
/* 2168 */     this.baseName = paramString;
/*      */ 
/* 2170 */     String str1 = System.getProperty("java.version");
/* 2171 */     String str2 = System.getProperty("java.vendor");
/*      */ 
/* 2173 */     this.sf = new Manifest();
/* 2174 */     Attributes localAttributes1 = this.sf.getMainAttributes();
/*      */ 
/* 2176 */     localAttributes1.putValue(Attributes.Name.SIGNATURE_VERSION.toString(), "1.0");
/* 2177 */     localAttributes1.putValue("Created-By", str1 + " (" + str2 + ")");
/*      */ 
/* 2179 */     if (paramBoolean)
/*      */     {
/* 2181 */       for (int i = 0; i < paramArrayOfMessageDigest.length; i++) {
/* 2182 */         localAttributes1.putValue(paramArrayOfMessageDigest[i].getAlgorithm() + "-Digest-Manifest", 
/* 2183 */           Base64.getEncoder().encodeToString(paramManifestDigester.manifestDigest(paramArrayOfMessageDigest[i])));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2189 */     ManifestDigester.Entry localEntry = paramManifestDigester
/* 2189 */       .get("Manifest-Main-Attributes", false);
/*      */ 
/* 2190 */     if (localEntry != null) {
/* 2191 */       for (int j = 0; j < paramArrayOfMessageDigest.length; j++)
/* 2192 */         localAttributes1.putValue(paramArrayOfMessageDigest[j].getAlgorithm() + "-Digest-" + "Manifest-Main-Attributes", 
/* 2194 */           Base64.getEncoder().encodeToString(localEntry.digest(paramArrayOfMessageDigest[j])));
/*      */     }
/*      */     else {
/* 2197 */       throw new IllegalStateException("ManifestDigester failed to create Manifest-Main-Attribute entry");
/*      */     }
/*      */ 
/* 2204 */     Map localMap = this.sf.getEntries();
/*      */ 
/* 2206 */     Iterator localIterator = paramManifest
/* 2206 */       .getEntries().entrySet().iterator();
/* 2207 */     while (localIterator.hasNext()) {
/* 2208 */       Map.Entry localEntry1 = (Map.Entry)localIterator.next();
/* 2209 */       String str3 = (String)localEntry1.getKey();
/* 2210 */       localEntry = paramManifestDigester.get(str3, false);
/* 2211 */       if (localEntry != null) {
/* 2212 */         Attributes localAttributes2 = new Attributes();
/* 2213 */         for (int k = 0; k < paramArrayOfMessageDigest.length; k++) {
/* 2214 */           localAttributes2.putValue(paramArrayOfMessageDigest[k].getAlgorithm() + "-Digest", 
/* 2215 */             Base64.getEncoder().encodeToString(localEntry.digest(paramArrayOfMessageDigest[k])));
/*      */         }
/* 2217 */         localMap.put(str3, localAttributes2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 2231 */     this.sf.write(paramOutputStream);
/*      */   }
/*      */ 
/*      */   public String getMetaName()
/*      */   {
/* 2239 */     return "META-INF/" + this.baseName + ".SF";
/*      */   }
/*      */ 
/*      */   public String getBaseName()
/*      */   {
/* 2247 */     return this.baseName;
/*      */   }
/*      */ 
/*      */   public Block generateBlock(PrivateKey paramPrivateKey, String paramString1, X509Certificate[] paramArrayOfX509Certificate, boolean paramBoolean, String paramString2, X509Certificate paramX509Certificate, String paramString3, ContentSigner paramContentSigner, String[] paramArrayOfString, ZipFile paramZipFile)
/*      */     throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException, CertificateException
/*      */   {
/* 2276 */     return new Block(this, paramPrivateKey, paramString1, paramArrayOfX509Certificate, paramBoolean, paramString2, paramX509Certificate, paramString3, paramContentSigner, paramArrayOfString, paramZipFile);
/*      */   }
/*      */ 
/*      */   public static class Block
/*      */   {
/*      */     private byte[] block;
/*      */     private String blockFileName;
/*      */ 
/*      */     Block(SignatureFile paramSignatureFile, PrivateKey paramPrivateKey, String paramString1, X509Certificate[] paramArrayOfX509Certificate, boolean paramBoolean, String paramString2, X509Certificate paramX509Certificate, String paramString3, ContentSigner paramContentSigner, String[] paramArrayOfString, ZipFile paramZipFile)
/*      */       throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException, CertificateException
/*      */     {
/* 2296 */       Principal localPrincipal = paramArrayOfX509Certificate[0].getIssuerDN();
/* 2297 */       if (!(localPrincipal instanceof X500Name))
/*      */       {
/* 2303 */         localObject = new X509CertInfo(paramArrayOfX509Certificate[0]
/* 2303 */           .getTBSCertificate());
/*      */ 
/* 2305 */         localPrincipal = (Principal)((X509CertInfo)localObject)
/* 2305 */           .get("issuer.dname");
/*      */       }
/*      */ 
/* 2308 */       Object localObject = paramArrayOfX509Certificate[0].getSerialNumber();
/*      */ 
/* 2311 */       String str2 = paramPrivateKey.getAlgorithm();
/*      */       String str1;
/* 2316 */       if (paramString1 == null)
/*      */       {
/* 2318 */         if (str2.equalsIgnoreCase("DSA"))
/* 2319 */           str1 = "SHA1withDSA";
/* 2320 */         else if (str2.equalsIgnoreCase("RSA"))
/* 2321 */           str1 = "SHA256withRSA";
/* 2322 */         else if (str2.equalsIgnoreCase("EC"))
/* 2323 */           str1 = "SHA256withECDSA";
/*      */         else
/* 2325 */           throw new RuntimeException("private key is not a DSA or RSA key");
/*      */       }
/*      */       else {
/* 2328 */         str1 = paramString1;
/*      */       }
/*      */ 
/* 2332 */       String str3 = str1.toUpperCase(Locale.ENGLISH);
/* 2333 */       if (((str3.endsWith("WITHRSA")) && 
/* 2334 */         (!str2
/* 2334 */         .equalsIgnoreCase("RSA"))) || 
/* 2335 */         ((str3
/* 2335 */         .endsWith("WITHECDSA")) && 
/* 2336 */         (!str2
/* 2336 */         .equalsIgnoreCase("EC"))) || (
/* 2337 */         (str3
/* 2337 */         .endsWith("WITHDSA")) && 
/* 2338 */         (!str2
/* 2338 */         .equalsIgnoreCase("DSA"))))
/*      */       {
/* 2339 */         throw new SignatureException("private key algorithm is not compatible with signature algorithm");
/*      */       }
/*      */ 
/* 2343 */       this.blockFileName = ("META-INF/" + paramSignatureFile.getBaseName() + "." + str2);
/*      */ 
/* 2345 */       AlgorithmId localAlgorithmId1 = AlgorithmId.get(str1);
/* 2346 */       AlgorithmId localAlgorithmId2 = AlgorithmId.get(str2);
/*      */ 
/* 2348 */       Signature localSignature = Signature.getInstance(str1);
/* 2349 */       localSignature.initSign(paramPrivateKey);
/*      */ 
/* 2351 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 2352 */       paramSignatureFile.write(localByteArrayOutputStream);
/*      */ 
/* 2354 */       byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
/*      */ 
/* 2356 */       localSignature.update(arrayOfByte1);
/* 2357 */       byte[] arrayOfByte2 = localSignature.sign();
/*      */ 
/* 2360 */       if (paramContentSigner == null) {
/* 2361 */         paramContentSigner = new TimestampedSigner();
/*      */       }
/* 2363 */       URI localURI = null;
/*      */       try {
/* 2365 */         if (paramString2 != null)
/* 2366 */           localURI = new URI(paramString2);
/*      */       }
/*      */       catch (URISyntaxException localURISyntaxException) {
/* 2369 */         throw new IOException(localURISyntaxException);
/*      */       }
/*      */ 
/* 2373 */       JarSignerParameters localJarSignerParameters = new JarSignerParameters(paramArrayOfString, localURI, paramX509Certificate, paramString3, arrayOfByte2, str1, paramArrayOfX509Certificate, arrayOfByte1, paramZipFile);
/*      */ 
/* 2378 */       this.block = paramContentSigner.generateSignedData(localJarSignerParameters, paramBoolean, (paramString2 != null) || (paramX509Certificate != null));
/*      */     }
/*      */ 
/*      */     public String getMetaName()
/*      */     {
/* 2387 */       return this.blockFileName;
/*      */     }
/*      */ 
/*      */     public void write(OutputStream paramOutputStream)
/*      */       throws IOException
/*      */     {
/* 2399 */       paramOutputStream.write(this.block);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.security.tools.jarsigner.SignatureFile
 * JD-Core Version:    0.6.2
 */