package com.sun.jarsigner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import jdk.Exported;

@Exported
public abstract class ContentSigner
{
  public abstract byte[] generateSignedData(ContentSignerParameters paramContentSignerParameters, boolean paramBoolean1, boolean paramBoolean2)
    throws NoSuchAlgorithmException, CertificateException, IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jarsigner.ContentSigner
 * JD-Core Version:    0.6.2
 */