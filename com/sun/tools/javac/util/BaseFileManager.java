/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.file.FSInfo;
/*     */ import com.sun.tools.javac.file.Locations;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.main.OptionHelper.GrumpyHelper;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public abstract class BaseFileManager
/*     */ {
/*     */   public Log log;
/*     */   protected Charset charset;
/*     */   protected Options options;
/*     */   protected String classLoaderClass;
/*     */   protected Locations locations;
/* 171 */   private static final Set<Option> javacFileManagerOptions = Option.getJavacFileManagerOptions();
/*     */   private String defaultEncodingName;
/*     */   private final ByteBufferCache byteBufferCache;
/* 359 */   protected final Map<JavaFileObject, ContentCacheEntry> contentCache = new HashMap();
/*     */ 
/*     */   protected BaseFileManager(Charset paramCharset)
/*     */   {
/*  69 */     this.charset = paramCharset;
/*  70 */     this.byteBufferCache = new ByteBufferCache(null);
/*  71 */     this.locations = createLocations();
/*     */   }
/*     */ 
/*     */   public void setContext(Context paramContext)
/*     */   {
/*  78 */     this.log = Log.instance(paramContext);
/*  79 */     this.options = Options.instance(paramContext);
/*  80 */     this.classLoaderClass = this.options.get("procloader");
/*  81 */     this.locations.update(this.log, this.options, Lint.instance(paramContext), FSInfo.instance(paramContext));
/*     */   }
/*     */ 
/*     */   protected Locations createLocations() {
/*  85 */     return new Locations();
/*     */   }
/*     */ 
/*     */   protected Source getSource()
/*     */   {
/* 105 */     String str = this.options.get(Option.SOURCE);
/* 106 */     Source localSource = null;
/* 107 */     if (str != null)
/* 108 */       localSource = Source.lookup(str);
/* 109 */     return localSource != null ? localSource : Source.DEFAULT;
/*     */   }
/*     */ 
/*     */   protected ClassLoader getClassLoader(URL[] paramArrayOfURL) {
/* 113 */     ClassLoader localClassLoader = getClass().getClassLoader();
/*     */ 
/* 119 */     if (this.classLoaderClass != null)
/*     */       try
/*     */       {
/* 122 */         Class localClass = Class.forName(this.classLoaderClass)
/* 122 */           .asSubclass(ClassLoader.class);
/* 123 */         Class[] arrayOfClass = { [Ljava.net.URL.class, ClassLoader.class };
/* 124 */         Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/* 125 */         return (ClassLoader)localConstructor.newInstance(new Object[] { paramArrayOfURL, localClassLoader });
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/* 130 */     return new URLClassLoader(paramArrayOfURL, localClassLoader);
/*     */   }
/*     */ 
/*     */   public boolean handleOption(String paramString, Iterator<String> paramIterator)
/*     */   {
/* 135 */     OptionHelper.GrumpyHelper local1 = new OptionHelper.GrumpyHelper(this.log)
/*     */     {
/*     */       public String get(Option paramAnonymousOption) {
/* 138 */         return BaseFileManager.this.options.get(paramAnonymousOption.getText());
/*     */       }
/*     */ 
/*     */       public void put(String paramAnonymousString1, String paramAnonymousString2)
/*     */       {
/* 143 */         BaseFileManager.this.options.put(paramAnonymousString1, paramAnonymousString2);
/*     */       }
/*     */ 
/*     */       public void remove(String paramAnonymousString)
/*     */       {
/* 148 */         BaseFileManager.this.options.remove(paramAnonymousString);
/*     */       }
/*     */     };
/* 151 */     for (Option localOption : javacFileManagerOptions) {
/* 152 */       if (localOption.matches(paramString)) {
/* 153 */         if (localOption.hasArg()) {
/* 154 */           if ((paramIterator.hasNext()) && 
/* 155 */             (!localOption.process(local1, paramString, (String)paramIterator.next()))) {
/* 156 */             return true;
/*     */           }
/*     */         }
/* 159 */         else if (!localOption.process(local1, paramString)) {
/* 160 */           return true;
/*     */         }
/*     */ 
/* 163 */         throw new IllegalArgumentException(paramString);
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public int isSupportedOption(String paramString)
/*     */   {
/* 174 */     for (Option localOption : javacFileManagerOptions) {
/* 175 */       if (localOption.matches(paramString))
/* 176 */         return localOption.hasArg() ? 1 : 0;
/*     */     }
/* 178 */     return -1;
/*     */   }
/*     */ 
/*     */   public abstract boolean isDefaultBootClassPath();
/*     */ 
/*     */   private String getDefaultEncodingName()
/*     */   {
/* 188 */     if (this.defaultEncodingName == null) {
/* 189 */       this.defaultEncodingName = new OutputStreamWriter(new ByteArrayOutputStream())
/* 190 */         .getEncoding();
/*     */     }
/* 192 */     return this.defaultEncodingName;
/*     */   }
/*     */ 
/*     */   public String getEncodingName() {
/* 196 */     String str = this.options.get(Option.ENCODING);
/* 197 */     if (str == null) {
/* 198 */       return getDefaultEncodingName();
/*     */     }
/* 200 */     return str;
/*     */   }
/*     */ 
/* 204 */   public CharBuffer decode(ByteBuffer paramByteBuffer, boolean paramBoolean) { String str = getEncodingName();
/*     */     CharsetDecoder localCharsetDecoder;
/*     */     try {
/* 207 */       localCharsetDecoder = getDecoder(str, paramBoolean);
/*     */     } catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 209 */       this.log.error("unsupported.encoding", new Object[] { str });
/* 210 */       return (CharBuffer)CharBuffer.allocate(1).flip();
/*     */     } catch (UnsupportedCharsetException localUnsupportedCharsetException) {
/* 212 */       this.log.error("unsupported.encoding", new Object[] { str });
/* 213 */       return (CharBuffer)CharBuffer.allocate(1).flip();
/*     */     }
/*     */ 
/* 219 */     float f = localCharsetDecoder
/* 218 */       .averageCharsPerByte() * 0.8F + localCharsetDecoder
/* 219 */       .maxCharsPerByte() * 0.2F;
/*     */ 
/* 221 */     CharBuffer localCharBuffer = CharBuffer.allocate(10 + 
/* 221 */       (int)(paramByteBuffer
/* 221 */       .remaining() * f));
/*     */     while (true)
/*     */     {
/* 224 */       CoderResult localCoderResult = localCharsetDecoder.decode(paramByteBuffer, localCharBuffer, true);
/* 225 */       localCharBuffer.flip();
/*     */ 
/* 227 */       if (localCoderResult.isUnderflow())
/*     */       {
/* 229 */         if (localCharBuffer.limit() == localCharBuffer.capacity()) {
/* 230 */           localCharBuffer = CharBuffer.allocate(localCharBuffer.capacity() + 1).put(localCharBuffer);
/* 231 */           localCharBuffer.flip();
/*     */         }
/* 233 */         return localCharBuffer;
/* 234 */       }if (localCoderResult.isOverflow())
/*     */       {
/* 237 */         int i = 10 + localCharBuffer
/* 236 */           .capacity() + 
/* 237 */           (int)(paramByteBuffer
/* 237 */           .remaining() * localCharsetDecoder.maxCharsPerByte());
/* 238 */         localCharBuffer = CharBuffer.allocate(i).put(localCharBuffer);
/* 239 */       } else if ((localCoderResult.isMalformed()) || (localCoderResult.isUnmappable()))
/*     */       {
/* 243 */         if (!getSource().allowEncodingErrors())
/* 244 */           this.log.error(new JCDiagnostic.SimpleDiagnosticPosition(localCharBuffer.limit()), "illegal.char.for.encoding", new Object[] { this.charset == null ? str : this.charset
/* 246 */             .name() });
/*     */         else {
/* 248 */           this.log.warning(new JCDiagnostic.SimpleDiagnosticPosition(localCharBuffer.limit()), "illegal.char.for.encoding", new Object[] { this.charset == null ? str : this.charset
/* 250 */             .name() });
/*     */         }
/*     */ 
/* 254 */         paramByteBuffer.position(paramByteBuffer.position() + localCoderResult.length());
/*     */ 
/* 258 */         localCharBuffer.position(localCharBuffer.limit());
/* 259 */         localCharBuffer.limit(localCharBuffer.capacity());
/* 260 */         localCharBuffer.put(65533);
/*     */       } else {
/* 262 */         throw new AssertionError(localCoderResult);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public CharsetDecoder getDecoder(String paramString, boolean paramBoolean)
/*     */   {
/* 270 */     Charset localCharset = this.charset == null ? 
/* 270 */       Charset.forName(paramString) : 
/* 270 */       this.charset;
/*     */ 
/* 272 */     CharsetDecoder localCharsetDecoder = localCharset.newDecoder();
/*     */     CodingErrorAction localCodingErrorAction;
/* 275 */     if (paramBoolean)
/* 276 */       localCodingErrorAction = CodingErrorAction.REPLACE;
/*     */     else {
/* 278 */       localCodingErrorAction = CodingErrorAction.REPORT;
/*     */     }
/*     */ 
/* 282 */     return localCharsetDecoder
/* 281 */       .onMalformedInput(localCodingErrorAction)
/* 282 */       .onUnmappableCharacter(localCodingErrorAction);
/*     */   }
/*     */ 
/*     */   public ByteBuffer makeByteBuffer(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 292 */     int i = paramInputStream.available();
/* 293 */     if (i < 1024) i = 1024;
/* 294 */     ByteBuffer localByteBuffer = this.byteBufferCache.get(i);
/* 295 */     int j = 0;
/* 296 */     while (paramInputStream.available() != 0) {
/* 297 */       if (j >= i)
/*     */       {
/* 301 */         localByteBuffer = ByteBuffer.allocate(i <<= 1)
/* 301 */           .put((ByteBuffer)localByteBuffer
/* 301 */           .flip());
/* 302 */       }int k = paramInputStream.read(localByteBuffer.array(), j, i - j);
/*     */ 
/* 305 */       if (k < 0) break;
/* 306 */       localByteBuffer.position(j += k);
/*     */     }
/* 308 */     return (ByteBuffer)localByteBuffer.flip();
/*     */   }
/*     */ 
/*     */   public void recycleByteBuffer(ByteBuffer paramByteBuffer) {
/* 312 */     this.byteBufferCache.put(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   public CharBuffer getCachedContent(JavaFileObject paramJavaFileObject)
/*     */   {
/* 339 */     ContentCacheEntry localContentCacheEntry = (ContentCacheEntry)this.contentCache.get(paramJavaFileObject);
/* 340 */     if (localContentCacheEntry == null) {
/* 341 */       return null;
/*     */     }
/* 343 */     if (!localContentCacheEntry.isValid(paramJavaFileObject)) {
/* 344 */       this.contentCache.remove(paramJavaFileObject);
/* 345 */       return null;
/*     */     }
/*     */ 
/* 348 */     return localContentCacheEntry.getValue();
/*     */   }
/*     */ 
/*     */   public void cache(JavaFileObject paramJavaFileObject, CharBuffer paramCharBuffer) {
/* 352 */     this.contentCache.put(paramJavaFileObject, new ContentCacheEntry(paramJavaFileObject, paramCharBuffer));
/*     */   }
/*     */ 
/*     */   public void flushCache(JavaFileObject paramJavaFileObject) {
/* 356 */     this.contentCache.remove(paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   public static JavaFileObject.Kind getKind(String paramString)
/*     */   {
/* 382 */     if (paramString.endsWith(JavaFileObject.Kind.CLASS.extension))
/* 383 */       return JavaFileObject.Kind.CLASS;
/* 384 */     if (paramString.endsWith(JavaFileObject.Kind.SOURCE.extension))
/* 385 */       return JavaFileObject.Kind.SOURCE;
/* 386 */     if (paramString.endsWith(JavaFileObject.Kind.HTML.extension)) {
/* 387 */       return JavaFileObject.Kind.HTML;
/*     */     }
/* 389 */     return JavaFileObject.Kind.OTHER;
/*     */   }
/*     */ 
/*     */   protected static <T> T nullCheck(T paramT) {
/* 393 */     paramT.getClass();
/* 394 */     return paramT;
/*     */   }
/*     */ 
/*     */   protected static <T> Collection<T> nullCheck(Collection<T> paramCollection) {
/* 398 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 399 */       localObject.getClass(); }
/* 400 */     return paramCollection;
/*     */   }
/*     */ 
/*     */   private static class ByteBufferCache
/*     */   {
/*     */     private ByteBuffer cached;
/*     */ 
/*     */     ByteBuffer get(int paramInt)
/*     */     {
/* 321 */       if (paramInt < 20480) paramInt = 20480;
/*     */ 
/* 325 */       ByteBuffer localByteBuffer = (this.cached != null) && 
/* 323 */         (this.cached
/* 323 */         .capacity() >= paramInt) ? 
/* 324 */         (ByteBuffer)this.cached
/* 324 */         .clear() : 
/* 325 */         ByteBuffer.allocate(paramInt + paramInt >> 1);
/*     */ 
/* 326 */       this.cached = null;
/* 327 */       return localByteBuffer;
/*     */     }
/*     */     void put(ByteBuffer paramByteBuffer) {
/* 330 */       this.cached = paramByteBuffer;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class ContentCacheEntry
/*     */   {
/*     */     final long timestamp;
/*     */     final SoftReference<CharBuffer> ref;
/*     */ 
/*     */     ContentCacheEntry(JavaFileObject paramJavaFileObject, CharBuffer paramCharBuffer)
/*     */     {
/* 367 */       this.timestamp = paramJavaFileObject.getLastModified();
/* 368 */       this.ref = new SoftReference(paramCharBuffer);
/*     */     }
/*     */ 
/*     */     boolean isValid(JavaFileObject paramJavaFileObject) {
/* 372 */       return this.timestamp == paramJavaFileObject.getLastModified();
/*     */     }
/*     */ 
/*     */     CharBuffer getValue() {
/* 376 */       return (CharBuffer)this.ref.get();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.BaseFileManager
 * JD-Core Version:    0.6.2
 */