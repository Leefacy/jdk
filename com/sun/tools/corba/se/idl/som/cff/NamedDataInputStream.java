/*     */ package com.sun.tools.corba.se.idl.som.cff;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ class NamedDataInputStream extends DataInputStream
/*     */ {
/*     */   public String fullyQualifiedFileName;
/*     */   public boolean inZipFile;
/*     */ 
/*     */   protected NamedDataInputStream(InputStream paramInputStream, String paramString, boolean paramBoolean)
/*     */   {
/* 405 */     super(paramInputStream);
/* 406 */     this.fullyQualifiedFileName = paramString;
/* 407 */     this.inZipFile = paramBoolean;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.som.cff.NamedDataInputStream
 * JD-Core Version:    0.6.2
 */