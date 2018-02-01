/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.tools.DocumentationTool.Location;
/*     */ 
/*     */ public class Extern
/*     */ {
/*     */   private Map<String, Item> packageToItemMap;
/*     */   private final Configuration configuration;
/*  69 */   private boolean linkoffline = false;
/*     */ 
/*     */   public Extern(Configuration paramConfiguration)
/*     */   {
/* 123 */     this.configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */   public boolean isExternal(ProgramElementDoc paramProgramElementDoc)
/*     */   {
/* 132 */     if (this.packageToItemMap == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     return this.packageToItemMap.get(paramProgramElementDoc.containingPackage().name()) != null;
/*     */   }
/*     */ 
/*     */   public DocLink getExternalLink(String paramString1, DocPath paramDocPath, String paramString2)
/*     */   {
/* 148 */     return getExternalLink(paramString1, paramDocPath, paramString2, null);
/*     */   }
/*     */ 
/*     */   public DocLink getExternalLink(String paramString1, DocPath paramDocPath, String paramString2, String paramString3)
/*     */   {
/* 153 */     Item localItem = findPackageItem(paramString1);
/* 154 */     if (localItem == null) {
/* 155 */       return null;
/*     */     }
/*     */ 
/* 159 */     DocPath localDocPath = localItem.relative ? paramDocPath
/* 158 */       .resolve(localItem.path)
/* 158 */       .resolve(paramString2) : 
/* 159 */       DocPath.create(localItem.path)
/* 159 */       .resolve(paramString2);
/*     */ 
/* 161 */     return new DocLink(localDocPath, "is-external=true", paramString3);
/*     */   }
/*     */ 
/*     */   public boolean link(String paramString1, String paramString2, DocErrorReporter paramDocErrorReporter, boolean paramBoolean)
/*     */   {
/* 176 */     this.linkoffline = paramBoolean;
/*     */     try {
/* 178 */       paramString1 = adjustEndFileSeparator(paramString1);
/* 179 */       if (isUrl(paramString2))
/* 180 */         readPackageListFromURL(paramString1, toURL(adjustEndFileSeparator(paramString2)));
/*     */       else {
/* 182 */         readPackageListFromFile(paramString1, DocFile.createFileForInput(this.configuration, paramString2));
/*     */       }
/* 184 */       return true;
/*     */     } catch (Fault localFault) {
/* 186 */       paramDocErrorReporter.printWarning(localFault.getMessage());
/* 187 */     }return false;
/*     */   }
/*     */ 
/*     */   private URL toURL(String paramString) throws Extern.Fault
/*     */   {
/*     */     try {
/* 193 */       return new URL(paramString);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 195 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramString), localMalformedURLException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Item findPackageItem(String paramString)
/*     */   {
/* 213 */     if (this.packageToItemMap == null) {
/* 214 */       return null;
/*     */     }
/* 216 */     return (Item)this.packageToItemMap.get(paramString);
/*     */   }
/*     */ 
/*     */   private String adjustEndFileSeparator(String paramString)
/*     */   {
/* 223 */     return paramString + '/';
/*     */   }
/*     */ 
/*     */   private void readPackageListFromURL(String paramString, URL paramURL)
/*     */     throws Extern.Fault
/*     */   {
/*     */     try
/*     */     {
/* 235 */       URL localURL = paramURL.toURI().resolve(DocPaths.PACKAGE_LIST.getPath()).toURL();
/* 236 */       readPackageList(localURL.openStream(), paramString, false);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 238 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramURL.toString()), localURISyntaxException);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 240 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramURL.toString()), localMalformedURLException);
/*     */     } catch (IOException localIOException) {
/* 242 */       throw new Fault(this.configuration.getText("doclet.URL_error", paramURL.toString()), localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readPackageListFromFile(String paramString, DocFile paramDocFile)
/*     */     throws Extern.Fault
/*     */   {
/* 254 */     DocFile localDocFile = paramDocFile.resolve(DocPaths.PACKAGE_LIST);
/* 255 */     if ((!localDocFile.isAbsolute()) && (!this.linkoffline))
/* 256 */       localDocFile = localDocFile.resolveAgainst(DocumentationTool.Location.DOCUMENTATION_OUTPUT);
/*     */     try
/*     */     {
/* 259 */       if ((localDocFile.exists()) && (localDocFile.canRead()))
/*     */       {
/* 262 */         boolean bool = (!DocFile.createFileForInput(this.configuration, paramString)
/* 261 */           .isAbsolute()) && 
/* 262 */           (!isUrl(paramString));
/*     */ 
/* 263 */         readPackageList(localDocFile.openInputStream(), paramString, bool);
/*     */       } else {
/* 265 */         throw new Fault(this.configuration.getText("doclet.File_error", localDocFile.getPath()), null);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 268 */       throw new Fault(this.configuration.getText("doclet.File_error", localDocFile.getPath()), localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readPackageList(InputStream paramInputStream, String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 283 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/* 284 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */     try
/*     */     {
/*     */       int i;
/* 287 */       while ((i = localBufferedReader.read()) >= 0) {
/* 288 */         char c = (char)i;
/* 289 */         if ((c == '\n') || (c == '\r')) {
/* 290 */           if (localStringBuilder.length() > 0) {
/* 291 */             String str1 = localStringBuilder.toString();
/*     */ 
/* 293 */             String str2 = paramString + str1
/* 293 */               .replace('.', '/') + 
/* 293 */               '/';
/* 294 */             new Item(str1, str2, paramBoolean);
/* 295 */             localStringBuilder.setLength(0);
/*     */           }
/*     */         }
/* 298 */         else localStringBuilder.append(c); 
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 302 */       paramInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isUrl(String paramString) {
/*     */     try {
/* 308 */       new URL(paramString);
/*     */ 
/* 310 */       return true;
/*     */     } catch (MalformedURLException localMalformedURLException) {
/*     */     }
/* 313 */     return false;
/*     */   }
/*     */ 
/*     */   private class Fault extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     Fault(String paramException, Exception arg3)
/*     */     {
/* 203 */       super(localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Item
/*     */   {
/*     */     final String packageName;
/*     */     final String path;
/*     */     final boolean relative;
/*     */ 
/*     */     Item(String paramString1, String paramBoolean, boolean arg4)
/*     */     {
/* 103 */       this.packageName = paramString1;
/* 104 */       this.path = paramBoolean;
/*     */       boolean bool;
/* 105 */       this.relative = bool;
/* 106 */       if (Extern.this.packageToItemMap == null) {
/* 107 */         Extern.this.packageToItemMap = new HashMap();
/*     */       }
/* 109 */       if (!Extern.this.packageToItemMap.containsKey(paramString1))
/* 110 */         Extern.this.packageToItemMap.put(paramString1, this);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 118 */       return this.packageName + (this.relative ? " -> " : " => ") + this.path;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.Extern
 * JD-Core Version:    0.6.2
 */