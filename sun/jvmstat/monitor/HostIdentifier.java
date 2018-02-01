/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ 
/*     */ public class HostIdentifier
/*     */ {
/*     */   private URI uri;
/*     */ 
/*     */   private URI canonicalize(String paramString)
/*     */     throws URISyntaxException
/*     */   {
/* 110 */     if ((paramString == null) || (paramString.compareTo("localhost") == 0)) {
/* 111 */       paramString = "//localhost";
/* 112 */       return new URI(paramString);
/*     */     }
/*     */ 
/* 115 */     URI localURI1 = new URI(paramString);
/*     */ 
/* 117 */     if (localURI1.isAbsolute()) {
/* 118 */       if (localURI1.isOpaque())
/*     */       {
/* 136 */         str1 = localURI1.getScheme();
/* 137 */         String str2 = localURI1.getSchemeSpecificPart();
/* 138 */         String str3 = localURI1.getFragment();
/* 139 */         URI localURI2 = null;
/*     */ 
/* 141 */         int i = paramString.indexOf(":");
/* 142 */         int j = paramString.lastIndexOf(":");
/* 143 */         if (j != i)
/*     */         {
/* 151 */           if (str3 == null)
/* 152 */             localURI2 = new URI(str1 + "://" + str2);
/*     */           else {
/* 154 */             localURI2 = new URI(str1 + "://" + str2 + "#" + str3);
/*     */           }
/* 156 */           return localURI2;
/*     */         }
/*     */ 
/* 164 */         localURI2 = new URI("//" + paramString);
/* 165 */         return localURI2;
/*     */       }
/* 167 */       return localURI1;
/*     */     }
/*     */ 
/* 178 */     String str1 = localURI1.getSchemeSpecificPart();
/* 179 */     if (str1.startsWith("//")) {
/* 180 */       return localURI1;
/*     */     }
/* 182 */     return new URI("//" + paramString);
/*     */   }
/*     */ 
/*     */   public HostIdentifier(String paramString)
/*     */     throws URISyntaxException
/*     */   {
/* 201 */     this.uri = canonicalize(paramString);
/*     */   }
/*     */ 
/*     */   public HostIdentifier(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */     throws URISyntaxException
/*     */   {
/* 222 */     this.uri = new URI(paramString1, paramString2, paramString3, paramString4, paramString5);
/*     */   }
/*     */ 
/*     */   public HostIdentifier(VmIdentifier paramVmIdentifier)
/*     */   {
/* 240 */     StringBuilder localStringBuilder = new StringBuilder();
/* 241 */     String str1 = paramVmIdentifier.getScheme();
/* 242 */     String str2 = paramVmIdentifier.getHost();
/* 243 */     String str3 = paramVmIdentifier.getAuthority();
/*     */ 
/* 246 */     if ((str1 != null) && (str1.compareTo("file") == 0)) {
/*     */       try {
/* 248 */         this.uri = new URI("file://localhost"); } catch (URISyntaxException localURISyntaxException1) {
/*     */       }
/* 250 */       return;
/*     */     }
/*     */ 
/* 253 */     if ((str2 != null) && (str2.compareTo(str3) == 0))
/*     */     {
/* 258 */       str2 = null;
/*     */     }
/*     */ 
/* 261 */     if (str1 == null) {
/* 262 */       if (str2 == null) {
/* 263 */         str1 = "local";
/*     */       }
/*     */       else
/*     */       {
/* 269 */         str1 = "rmi";
/*     */       }
/*     */     }
/*     */ 
/* 273 */     localStringBuilder.append(str1).append("://");
/*     */ 
/* 275 */     if (str2 == null)
/* 276 */       localStringBuilder.append("localhost");
/*     */     else {
/* 278 */       localStringBuilder.append(str2);
/*     */     }
/*     */ 
/* 281 */     int i = paramVmIdentifier.getPort();
/* 282 */     if (i != -1) {
/* 283 */       localStringBuilder.append(":").append(i);
/*     */     }
/*     */ 
/* 286 */     String str4 = paramVmIdentifier.getPath();
/* 287 */     if ((str4 != null) && (str4.length() != 0)) {
/* 288 */       localStringBuilder.append(str4);
/*     */     }
/*     */ 
/* 291 */     String str5 = paramVmIdentifier.getQuery();
/* 292 */     if (str5 != null) {
/* 293 */       localStringBuilder.append("?").append(str5);
/*     */     }
/*     */ 
/* 296 */     String str6 = paramVmIdentifier.getFragment();
/* 297 */     if (str6 != null) {
/* 298 */       localStringBuilder.append("#").append(str6);
/*     */     }
/*     */     try
/*     */     {
/* 302 */       this.uri = new URI(localStringBuilder.toString());
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException2) {
/* 305 */       throw new RuntimeException("Internal Error", localURISyntaxException2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public VmIdentifier resolve(VmIdentifier paramVmIdentifier)
/*     */     throws URISyntaxException, MonitorException
/*     */   {
/* 338 */     String str1 = paramVmIdentifier.getScheme();
/* 339 */     String str2 = paramVmIdentifier.getHost();
/* 340 */     String str3 = paramVmIdentifier.getAuthority();
/*     */ 
/* 342 */     if ((str1 != null) && (str1.compareTo("file") == 0))
/*     */     {
/* 344 */       return paramVmIdentifier;
/*     */     }
/*     */ 
/* 347 */     if ((str2 != null) && (str2.compareTo(str3) == 0))
/*     */     {
/* 352 */       str2 = null;
/*     */     }
/*     */ 
/* 355 */     if (str1 == null) {
/* 356 */       str1 = getScheme();
/*     */     }
/*     */ 
/* 359 */     Object localObject = null;
/*     */ 
/* 361 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 363 */     localStringBuffer.append(str1).append("://");
/*     */ 
/* 365 */     String str4 = paramVmIdentifier.getUserInfo();
/* 366 */     if (str4 != null)
/* 367 */       localStringBuffer.append(str4);
/*     */     else {
/* 369 */       localStringBuffer.append(paramVmIdentifier.getAuthority());
/*     */     }
/*     */ 
/* 372 */     if (str2 == null) {
/* 373 */       str2 = getHost();
/*     */     }
/* 375 */     localStringBuffer.append("@").append(str2);
/*     */ 
/* 377 */     int i = paramVmIdentifier.getPort();
/* 378 */     if (i == -1) {
/* 379 */       i = getPort();
/*     */     }
/*     */ 
/* 382 */     if (i != -1) {
/* 383 */       localStringBuffer.append(":").append(i);
/*     */     }
/*     */ 
/* 386 */     String str5 = paramVmIdentifier.getPath();
/* 387 */     if ((str5 == null) || (str5.length() == 0)) {
/* 388 */       str5 = getPath();
/*     */     }
/*     */ 
/* 391 */     if ((str5 != null) && (str5.length() > 0)) {
/* 392 */       localStringBuffer.append(str5);
/*     */     }
/*     */ 
/* 395 */     String str6 = paramVmIdentifier.getQuery();
/* 396 */     if (str6 == null) {
/* 397 */       str6 = getQuery();
/*     */     }
/* 399 */     if (str6 != null) {
/* 400 */       localStringBuffer.append("?").append(str6);
/*     */     }
/*     */ 
/* 403 */     String str7 = paramVmIdentifier.getFragment();
/* 404 */     if (str7 == null) {
/* 405 */       str7 = getFragment();
/*     */     }
/* 407 */     if (str7 != null) {
/* 408 */       localStringBuffer.append("#").append(str7);
/*     */     }
/*     */ 
/* 411 */     String str8 = localStringBuffer.toString();
/* 412 */     return new VmIdentifier(str8);
/*     */   }
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 422 */     return this.uri.isAbsolute() ? this.uri.getScheme() : null;
/*     */   }
/*     */ 
/*     */   public String getSchemeSpecificPart()
/*     */   {
/* 432 */     return this.uri.getSchemeSpecificPart();
/*     */   }
/*     */ 
/*     */   public String getUserInfo()
/*     */   {
/* 442 */     return this.uri.getUserInfo();
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 453 */     return this.uri.getHost() == null ? "localhost" : this.uri.getHost();
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 463 */     return this.uri.getPort();
/*     */   }
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 473 */     return this.uri.getPath();
/*     */   }
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 483 */     return this.uri.getQuery();
/*     */   }
/*     */ 
/*     */   public String getFragment()
/*     */   {
/* 493 */     return this.uri.getFragment();
/*     */   }
/*     */ 
/*     */   public String getMode()
/*     */   {
/* 503 */     String str = getQuery();
/* 504 */     if (str != null) {
/* 505 */       String[] arrayOfString = str.split("\\+");
/* 506 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 507 */         if (arrayOfString[i].startsWith("mode=")) {
/* 508 */           int j = arrayOfString[i].indexOf('=');
/* 509 */           return arrayOfString[i].substring(j + 1);
/*     */         }
/*     */       }
/*     */     }
/* 513 */     return "r";
/*     */   }
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 523 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 534 */     return this.uri.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 548 */     if (paramObject == this) {
/* 549 */       return true;
/*     */     }
/* 551 */     if (!(paramObject instanceof HostIdentifier)) {
/* 552 */       return false;
/*     */     }
/* 554 */     return this.uri.equals(((HostIdentifier)paramObject).uri);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 567 */     return this.uri.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.HostIdentifier
 * JD-Core Version:    0.6.2
 */