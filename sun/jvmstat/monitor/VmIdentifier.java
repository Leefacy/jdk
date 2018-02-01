/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ 
/*     */ public class VmIdentifier
/*     */ {
/*     */   private URI uri;
/*     */ 
/*     */   private URI canonicalize(String paramString)
/*     */     throws URISyntaxException
/*     */   {
/* 137 */     if (paramString == null) {
/* 138 */       paramString = "local://0@localhost";
/* 139 */       return new URI(paramString);
/*     */     }
/*     */ 
/* 142 */     URI localURI = new URI(paramString);
/*     */ 
/* 144 */     if (localURI.isAbsolute()) {
/* 145 */       if (localURI.isOpaque())
/*     */       {
/* 151 */         localURI = new URI(localURI.getScheme(), "//" + localURI.getSchemeSpecificPart(), localURI
/* 151 */           .getFragment());
/*     */       }
/*     */ 
/*     */     }
/* 159 */     else if (!paramString.startsWith("//")) {
/* 160 */       if (localURI.getFragment() == null) {
/* 161 */         localURI = new URI("//" + localURI.getSchemeSpecificPart());
/*     */       }
/*     */       else {
/* 164 */         localURI = new URI("//" + localURI.getSchemeSpecificPart() + "#" + localURI
/* 164 */           .getFragment());
/*     */       }
/*     */     }
/*     */ 
/* 168 */     return localURI;
/*     */   }
/*     */ 
/*     */   private void validate()
/*     */     throws URISyntaxException
/*     */   {
/* 177 */     String str = getScheme();
/* 178 */     if ((str != null) && (str.compareTo("file") == 0)) {
/* 179 */       return;
/*     */     }
/* 181 */     if (getLocalVmId() == -1)
/* 182 */       throw new URISyntaxException(this.uri.toString(), "Local vmid required");
/*     */   }
/*     */ 
/*     */   public VmIdentifier(String paramString)
/*     */     throws URISyntaxException
/*     */   {
/*     */     URI localURI;
/*     */     try
/*     */     {
/* 198 */       localURI = canonicalize(paramString);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/* 204 */       if (paramString.startsWith("//")) {
/* 205 */         throw localURISyntaxException;
/*     */       }
/* 207 */       localURI = canonicalize("//" + paramString);
/*     */     }
/*     */ 
/* 210 */     this.uri = localURI;
/*     */ 
/* 213 */     validate();
/*     */   }
/*     */ 
/*     */   public VmIdentifier(URI paramURI)
/*     */     throws URISyntaxException
/*     */   {
/* 225 */     this.uri = paramURI;
/* 226 */     validate();
/*     */   }
/*     */ 
/*     */   public HostIdentifier getHostIdentifier()
/*     */     throws URISyntaxException
/*     */   {
/* 258 */     StringBuffer localStringBuffer = new StringBuffer();
/* 259 */     if (getScheme() != null) {
/* 260 */       localStringBuffer.append(getScheme()).append(":");
/*     */     }
/* 262 */     localStringBuffer.append("//").append(getHost());
/* 263 */     if (getPort() != -1) {
/* 264 */       localStringBuffer.append(":").append(getPort());
/*     */     }
/* 266 */     if (getPath() != null) {
/* 267 */       localStringBuffer.append(getPath());
/*     */     }
/* 269 */     return new HostIdentifier(localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 279 */     return this.uri.getScheme();
/*     */   }
/*     */ 
/*     */   public String getSchemeSpecificPart()
/*     */   {
/* 289 */     return this.uri.getSchemeSpecificPart();
/*     */   }
/*     */ 
/*     */   public String getUserInfo()
/*     */   {
/* 299 */     return this.uri.getUserInfo();
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 309 */     return this.uri.getHost();
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 319 */     return this.uri.getPort();
/*     */   }
/*     */ 
/*     */   public String getAuthority()
/*     */   {
/* 329 */     return this.uri.getAuthority();
/*     */   }
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 339 */     return this.uri.getPath();
/*     */   }
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 349 */     return this.uri.getQuery();
/*     */   }
/*     */ 
/*     */   public String getFragment()
/*     */   {
/* 359 */     return this.uri.getFragment();
/*     */   }
/*     */ 
/*     */   public int getLocalVmId()
/*     */   {
/* 370 */     int i = -1;
/*     */     try {
/* 372 */       if (this.uri.getUserInfo() == null)
/* 373 */         i = Integer.parseInt(this.uri.getAuthority());
/*     */       else
/* 375 */         i = Integer.parseInt(this.uri.getUserInfo());
/*     */     } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 378 */     return i;
/*     */   }
/*     */ 
/*     */   public String getMode()
/*     */   {
/* 388 */     String str = getQuery();
/* 389 */     if (str != null) {
/* 390 */       String[] arrayOfString = str.split("\\+");
/* 391 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 392 */         if (arrayOfString[i].startsWith("mode=")) {
/* 393 */           int j = arrayOfString[i].indexOf('=');
/* 394 */           return arrayOfString[i].substring(j + 1);
/*     */         }
/*     */       }
/*     */     }
/* 398 */     return "r";
/*     */   }
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 408 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 419 */     return this.uri.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 433 */     if (paramObject == this) {
/* 434 */       return true;
/*     */     }
/* 436 */     if (!(paramObject instanceof VmIdentifier)) {
/* 437 */       return false;
/*     */     }
/* 439 */     return this.uri.equals(((VmIdentifier)paramObject).uri);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 451 */     return this.uri.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.VmIdentifier
 * JD-Core Version:    0.6.2
 */