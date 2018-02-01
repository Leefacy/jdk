/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class ClientCodeWrapper
/*     */ {
/*     */   Map<Class<?>, Boolean> trustedClasses;
/*     */ 
/*     */   public static ClientCodeWrapper instance(Context paramContext)
/*     */   {
/*  97 */     ClientCodeWrapper localClientCodeWrapper = (ClientCodeWrapper)paramContext.get(ClientCodeWrapper.class);
/*  98 */     if (localClientCodeWrapper == null)
/*  99 */       localClientCodeWrapper = new ClientCodeWrapper(paramContext);
/* 100 */     return localClientCodeWrapper;
/*     */   }
/*     */ 
/*     */   protected ClientCodeWrapper(Context paramContext)
/*     */   {
/* 110 */     this.trustedClasses = new HashMap();
/*     */   }
/*     */ 
/*     */   public JavaFileManager wrap(JavaFileManager paramJavaFileManager) {
/* 114 */     if (isTrusted(paramJavaFileManager))
/* 115 */       return paramJavaFileManager;
/* 116 */     return new WrappedJavaFileManager(paramJavaFileManager);
/*     */   }
/*     */ 
/*     */   public FileObject wrap(FileObject paramFileObject) {
/* 120 */     if (isTrusted(paramFileObject))
/* 121 */       return paramFileObject;
/* 122 */     return new WrappedFileObject(paramFileObject);
/*     */   }
/*     */ 
/*     */   FileObject unwrap(FileObject paramFileObject) {
/* 126 */     if ((paramFileObject instanceof WrappedFileObject)) {
/* 127 */       return ((WrappedFileObject)paramFileObject).clientFileObject;
/*     */     }
/* 129 */     return paramFileObject;
/*     */   }
/*     */ 
/*     */   public JavaFileObject wrap(JavaFileObject paramJavaFileObject) {
/* 133 */     if (isTrusted(paramJavaFileObject))
/* 134 */       return paramJavaFileObject;
/* 135 */     return new WrappedJavaFileObject(paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   public Iterable<JavaFileObject> wrapJavaFileObjects(Iterable<? extends JavaFileObject> paramIterable) {
/* 139 */     ArrayList localArrayList = new ArrayList();
/* 140 */     for (JavaFileObject localJavaFileObject : paramIterable)
/* 141 */       localArrayList.add(wrap(localJavaFileObject));
/* 142 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   JavaFileObject unwrap(JavaFileObject paramJavaFileObject) {
/* 146 */     if ((paramJavaFileObject instanceof WrappedJavaFileObject)) {
/* 147 */       return (JavaFileObject)((WrappedJavaFileObject)paramJavaFileObject).clientFileObject;
/*     */     }
/* 149 */     return paramJavaFileObject;
/*     */   }
/*     */ 
/*     */   public <T> DiagnosticListener<T> wrap(DiagnosticListener<T> paramDiagnosticListener) {
/* 153 */     if (isTrusted(paramDiagnosticListener))
/* 154 */       return paramDiagnosticListener;
/* 155 */     return new WrappedDiagnosticListener(paramDiagnosticListener);
/*     */   }
/*     */ 
/*     */   TaskListener wrap(TaskListener paramTaskListener) {
/* 159 */     if (isTrusted(paramTaskListener))
/* 160 */       return paramTaskListener;
/* 161 */     return new WrappedTaskListener(paramTaskListener);
/*     */   }
/*     */ 
/*     */   TaskListener unwrap(TaskListener paramTaskListener) {
/* 165 */     if ((paramTaskListener instanceof WrappedTaskListener)) {
/* 166 */       return ((WrappedTaskListener)paramTaskListener).clientTaskListener;
/*     */     }
/* 168 */     return paramTaskListener;
/*     */   }
/*     */ 
/*     */   Collection<TaskListener> unwrap(Collection<? extends TaskListener> paramCollection) {
/* 172 */     ArrayList localArrayList = new ArrayList(paramCollection.size());
/* 173 */     for (TaskListener localTaskListener : paramCollection)
/* 174 */       localArrayList.add(unwrap(localTaskListener));
/* 175 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private <T> Diagnostic<T> unwrap(Diagnostic<T> paramDiagnostic)
/*     */   {
/* 180 */     if ((paramDiagnostic instanceof JCDiagnostic)) {
/* 181 */       JCDiagnostic localJCDiagnostic = (JCDiagnostic)paramDiagnostic;
/* 182 */       return new DiagnosticSourceUnwrapper(localJCDiagnostic);
/*     */     }
/* 184 */     return paramDiagnostic;
/*     */   }
/*     */ 
/*     */   protected boolean isTrusted(Object paramObject)
/*     */   {
/* 189 */     Class localClass = paramObject.getClass();
/* 190 */     Boolean localBoolean = (Boolean)this.trustedClasses.get(localClass);
/* 191 */     if (localBoolean == null) {
/* 192 */       localBoolean = Boolean.valueOf((localClass.getName().startsWith("com.sun.tools.javac.")) || 
/* 193 */         (localClass
/* 193 */         .isAnnotationPresent(Trusted.class)));
/*     */ 
/* 194 */       this.trustedClasses.put(localClass, localBoolean);
/*     */     }
/* 196 */     return localBoolean.booleanValue();
/*     */   }
/*     */ 
/*     */   private String wrappedToString(Class<?> paramClass, Object paramObject) {
/* 200 */     return paramClass.getSimpleName() + "[" + paramObject + "]";
/*     */   }
/*     */ 
/*     */   public class DiagnosticSourceUnwrapper
/*     */     implements Diagnostic<JavaFileObject>
/*     */   {
/*     */     public final JCDiagnostic d;
/*     */ 
/*     */     DiagnosticSourceUnwrapper(JCDiagnostic arg2)
/*     */     {
/*     */       Object localObject;
/* 613 */       this.d = localObject;
/*     */     }
/*     */ 
/*     */     public Diagnostic.Kind getKind() {
/* 617 */       return this.d.getKind();
/*     */     }
/*     */ 
/*     */     public JavaFileObject getSource() {
/* 621 */       return ClientCodeWrapper.this.unwrap(this.d.getSource());
/*     */     }
/*     */ 
/*     */     public long getPosition() {
/* 625 */       return this.d.getPosition();
/*     */     }
/*     */ 
/*     */     public long getStartPosition() {
/* 629 */       return this.d.getStartPosition();
/*     */     }
/*     */ 
/*     */     public long getEndPosition() {
/* 633 */       return this.d.getEndPosition();
/*     */     }
/*     */ 
/*     */     public long getLineNumber() {
/* 637 */       return this.d.getLineNumber();
/*     */     }
/*     */ 
/*     */     public long getColumnNumber() {
/* 641 */       return this.d.getColumnNumber();
/*     */     }
/*     */ 
/*     */     public String getCode() {
/* 645 */       return this.d.getCode();
/*     */     }
/*     */ 
/*     */     public String getMessage(Locale paramLocale) {
/* 649 */       return this.d.getMessage(paramLocale);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 654 */       return this.d.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({java.lang.annotation.ElementType.TYPE})
/*     */   public static @interface Trusted
/*     */   {
/*     */   }
/*     */ 
/*     */   protected class WrappedDiagnosticListener<T>
/*     */     implements DiagnosticListener<T>
/*     */   {
/*     */     protected DiagnosticListener<T> clientDiagnosticListener;
/*     */ 
/*     */     WrappedDiagnosticListener()
/*     */     {
/*     */       Object localObject;
/* 586 */       localObject.getClass();
/* 587 */       this.clientDiagnosticListener = localObject;
/*     */     }
/*     */ 
/*     */     public void report(Diagnostic<? extends T> paramDiagnostic)
/*     */     {
/*     */       try {
/* 593 */         this.clientDiagnosticListener.report(ClientCodeWrapper.this.unwrap(paramDiagnostic));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 595 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 597 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 599 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 605 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientDiagnosticListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class WrappedFileObject
/*     */     implements FileObject
/*     */   {
/*     */     protected FileObject clientFileObject;
/*     */ 
/*     */     WrappedFileObject(FileObject arg2)
/*     */     {
/*     */       Object localObject;
/* 393 */       localObject.getClass();
/* 394 */       this.clientFileObject = localObject;
/*     */     }
/*     */ 
/*     */     public URI toUri()
/*     */     {
/*     */       try {
/* 400 */         return this.clientFileObject.toUri();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 402 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 404 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 406 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/*     */       try {
/* 413 */         return this.clientFileObject.getName();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 415 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 417 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 419 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws IOException
/*     */     {
/*     */       try {
/* 426 */         return this.clientFileObject.openInputStream();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 428 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 430 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 432 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream() throws IOException
/*     */     {
/*     */       try {
/* 439 */         return this.clientFileObject.openOutputStream();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 441 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 443 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 445 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Reader openReader(boolean paramBoolean) throws IOException
/*     */     {
/*     */       try {
/* 452 */         return this.clientFileObject.openReader(paramBoolean);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 454 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 456 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 458 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public CharSequence getCharContent(boolean paramBoolean) throws IOException
/*     */     {
/*     */       try {
/* 465 */         return this.clientFileObject.getCharContent(paramBoolean);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 467 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 469 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 471 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Writer openWriter() throws IOException
/*     */     {
/*     */       try {
/* 478 */         return this.clientFileObject.openWriter();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 480 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 482 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 484 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public long getLastModified()
/*     */     {
/*     */       try {
/* 491 */         return this.clientFileObject.getLastModified();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 493 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 495 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 497 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/*     */       try {
/* 504 */         return this.clientFileObject.delete();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 506 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 508 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 510 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 516 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class WrappedJavaFileManager
/*     */     implements JavaFileManager
/*     */   {
/*     */     protected JavaFileManager clientJavaFileManager;
/*     */ 
/*     */     WrappedJavaFileManager(JavaFileManager arg2)
/*     */     {
/*     */       Object localObject;
/* 211 */       localObject.getClass();
/* 212 */       this.clientJavaFileManager = localObject;
/*     */     }
/*     */ 
/*     */     public ClassLoader getClassLoader(JavaFileManager.Location paramLocation)
/*     */     {
/*     */       try {
/* 218 */         return this.clientJavaFileManager.getClassLoader(paramLocation);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 220 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 222 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 224 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Iterable<JavaFileObject> list(JavaFileManager.Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean) throws IOException
/*     */     {
/*     */       try {
/* 231 */         return ClientCodeWrapper.this.wrapJavaFileObjects(this.clientJavaFileManager.list(paramLocation, paramString, paramSet, paramBoolean));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 233 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 235 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 237 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String inferBinaryName(JavaFileManager.Location paramLocation, JavaFileObject paramJavaFileObject)
/*     */     {
/*     */       try {
/* 244 */         return this.clientJavaFileManager.inferBinaryName(paramLocation, ClientCodeWrapper.this.unwrap(paramJavaFileObject));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 246 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 248 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 250 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2)
/*     */     {
/*     */       try {
/* 257 */         return this.clientJavaFileManager.isSameFile(ClientCodeWrapper.this.unwrap(paramFileObject1), ClientCodeWrapper.this.unwrap(paramFileObject2));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 259 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 261 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 263 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean handleOption(String paramString, Iterator<String> paramIterator)
/*     */     {
/*     */       try {
/* 270 */         return this.clientJavaFileManager.handleOption(paramString, paramIterator);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 272 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 274 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 276 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasLocation(JavaFileManager.Location paramLocation)
/*     */     {
/*     */       try {
/* 283 */         return this.clientJavaFileManager.hasLocation(paramLocation);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 285 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 287 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 289 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public JavaFileObject getJavaFileForInput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind) throws IOException
/*     */     {
/*     */       try {
/* 296 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getJavaFileForInput(paramLocation, paramString, paramKind));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 298 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 300 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 302 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public JavaFileObject getJavaFileForOutput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject) throws IOException
/*     */     {
/*     */       try {
/* 309 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getJavaFileForOutput(paramLocation, paramString, paramKind, ClientCodeWrapper.this.unwrap(paramFileObject)));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 311 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 313 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 315 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public FileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString1, String paramString2) throws IOException
/*     */     {
/*     */       try {
/* 322 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getFileForInput(paramLocation, paramString1, paramString2));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 324 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 326 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 328 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public FileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject) throws IOException
/*     */     {
/*     */       try {
/* 335 */         return ClientCodeWrapper.this.wrap(this.clientJavaFileManager.getFileForOutput(paramLocation, paramString1, paramString2, ClientCodeWrapper.this.unwrap(paramFileObject)));
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 337 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 339 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 341 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException
/*     */     {
/*     */       try {
/* 348 */         this.clientJavaFileManager.flush();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 350 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 352 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 354 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/*     */       try {
/* 361 */         this.clientJavaFileManager.close();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 363 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 365 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 367 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int isSupportedOption(String paramString)
/*     */     {
/*     */       try {
/* 374 */         return this.clientJavaFileManager.isSupportedOption(paramString);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 376 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 378 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 380 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 386 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientJavaFileManager);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class WrappedJavaFileObject extends ClientCodeWrapper.WrappedFileObject
/*     */     implements JavaFileObject
/*     */   {
/*     */     WrappedJavaFileObject(JavaFileObject arg2)
/*     */     {
/* 522 */       super(localFileObject);
/*     */     }
/*     */ 
/*     */     public JavaFileObject.Kind getKind()
/*     */     {
/*     */       try {
/* 528 */         return ((JavaFileObject)this.clientFileObject).getKind();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 530 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 532 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 534 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */     {
/*     */       try {
/* 541 */         return ((JavaFileObject)this.clientFileObject).isNameCompatible(paramString, paramKind);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 543 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 545 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 547 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public NestingKind getNestingKind()
/*     */     {
/*     */       try {
/* 554 */         return ((JavaFileObject)this.clientFileObject).getNestingKind();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 556 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 558 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 560 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Modifier getAccessLevel()
/*     */     {
/*     */       try {
/* 567 */         return ((JavaFileObject)this.clientFileObject).getAccessLevel();
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 569 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 571 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 573 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 579 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientFileObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class WrappedTaskListener
/*     */     implements TaskListener
/*     */   {
/*     */     protected TaskListener clientTaskListener;
/*     */ 
/*     */     WrappedTaskListener(TaskListener arg2)
/*     */     {
/*     */       Object localObject;
/* 661 */       localObject.getClass();
/* 662 */       this.clientTaskListener = localObject;
/*     */     }
/*     */ 
/*     */     public void started(TaskEvent paramTaskEvent)
/*     */     {
/*     */       try {
/* 668 */         this.clientTaskListener.started(paramTaskEvent);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 670 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 672 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 674 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void finished(TaskEvent paramTaskEvent)
/*     */     {
/*     */       try {
/* 681 */         this.clientTaskListener.finished(paramTaskEvent);
/*     */       } catch (ClientCodeException localClientCodeException) {
/* 683 */         throw localClientCodeException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 685 */         throw new ClientCodeException(localRuntimeException);
/*     */       } catch (Error localError) {
/* 687 */         throw new ClientCodeException(localError);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 693 */       return ClientCodeWrapper.this.wrappedToString(getClass(), this.clientTaskListener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.ClientCodeWrapper
 * JD-Core Version:    0.6.2
 */