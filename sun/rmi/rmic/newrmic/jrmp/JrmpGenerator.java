/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.rmi.rmic.newrmic.BatchEnvironment;
/*     */ import sun.rmi.rmic.newrmic.Generator;
/*     */ import sun.rmi.rmic.newrmic.IndentingWriter;
/*     */ import sun.rmi.rmic.newrmic.Main;
/*     */ import sun.rmi.rmic.newrmic.Resources;
/*     */ 
/*     */ public class JrmpGenerator
/*     */   implements Generator
/*     */ {
/*  58 */   private static final Map<String, Constants.StubVersion> versionOptions = new HashMap();
/*     */   private static final Set<String> bootstrapClassNames;
/*  76 */   private Constants.StubVersion version = Constants.StubVersion.V1_2;
/*     */ 
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain)
/*     */   {
/*  89 */     Object localObject = null;
/*  90 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  91 */       String str = paramArrayOfString[i];
/*  92 */       if (versionOptions.containsKey(str)) {
/*  93 */         if ((localObject != null) && (!localObject.equals(str))) {
/*  94 */           paramMain.error("rmic.cannot.use.both", new String[] { localObject, str });
/*  95 */           return false;
/*     */         }
/*  97 */         localObject = str;
/*  98 */         this.version = ((Constants.StubVersion)versionOptions.get(str));
/*  99 */         paramArrayOfString[i] = null;
/*     */       }
/*     */     }
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   public Class<? extends BatchEnvironment> envClass()
/*     */   {
/* 110 */     return BatchEnvironment.class;
/*     */   }
/*     */ 
/*     */   public Set<String> bootstrapClassNames() {
/* 114 */     return Collections.unmodifiableSet(bootstrapClassNames);
/*     */   }
/*     */ 
/*     */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc, File paramFile)
/*     */   {
/* 126 */     RemoteClass localRemoteClass = RemoteClass.forClass(paramBatchEnvironment, paramClassDoc);
/* 127 */     if (localRemoteClass == null) {
/* 128 */       return;
/*     */     }
/*     */ 
/* 131 */     StubSkeletonWriter localStubSkeletonWriter = new StubSkeletonWriter(paramBatchEnvironment, localRemoteClass, this.version);
/*     */ 
/* 134 */     File localFile1 = sourceFileForClass(localStubSkeletonWriter.stubClassName(), paramFile);
/*     */     try {
/* 136 */       IndentingWriter localIndentingWriter1 = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(localFile1)));
/*     */ 
/* 138 */       localStubSkeletonWriter.writeStub(localIndentingWriter1);
/* 139 */       localIndentingWriter1.close();
/* 140 */       if (paramBatchEnvironment.verbose()) {
/* 141 */         paramBatchEnvironment.output(Resources.getText("rmic.wrote", new String[] { localFile1
/* 142 */           .getPath() }));
/*     */       }
/* 144 */       paramBatchEnvironment.addGeneratedFile(localFile1);
/*     */     } catch (IOException localIOException1) {
/* 146 */       paramBatchEnvironment.error("rmic.cant.write", new String[] { localFile1.toString() });
/* 147 */       return;
/*     */     }
/*     */ 
/* 151 */     File localFile2 = sourceFileForClass(localStubSkeletonWriter
/* 151 */       .skeletonClassName(), paramFile);
/* 152 */     if ((this.version == Constants.StubVersion.V1_1) || (this.version == Constants.StubVersion.VCOMPAT))
/*     */     {
/*     */       try
/*     */       {
/* 156 */         IndentingWriter localIndentingWriter2 = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(localFile2)));
/*     */ 
/* 159 */         localStubSkeletonWriter.writeSkeleton(localIndentingWriter2);
/* 160 */         localIndentingWriter2.close();
/* 161 */         if (paramBatchEnvironment.verbose()) {
/* 162 */           paramBatchEnvironment.output(Resources.getText("rmic.wrote", new String[] { localFile2
/* 163 */             .getPath() }));
/*     */         }
/* 165 */         paramBatchEnvironment.addGeneratedFile(localFile2);
/*     */       } catch (IOException localIOException2) {
/* 167 */         paramBatchEnvironment.error("rmic.cant.write", new String[] { localFile2.toString() });
/* 168 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 179 */       File localFile3 = classFileForClass(localStubSkeletonWriter
/* 179 */         .skeletonClassName(), paramFile);
/*     */ 
/* 181 */       localFile2.delete();
/* 182 */       localFile3.delete();
/*     */     }
/*     */   }
/*     */ 
/*     */   private File sourceFileForClass(String paramString, File paramFile)
/*     */   {
/* 193 */     return fileForClass(paramString, paramFile, ".java");
/*     */   }
/*     */ 
/*     */   private File classFileForClass(String paramString, File paramFile)
/*     */   {
/* 202 */     return fileForClass(paramString, paramFile, ".class");
/*     */   }
/*     */ 
/*     */   private File fileForClass(String paramString1, File paramFile, String paramString2) {
/* 206 */     int i = paramString1.lastIndexOf('.');
/* 207 */     String str1 = paramString1.substring(i + 1) + paramString2;
/* 208 */     if (i != -1) {
/* 209 */       String str2 = paramString1.substring(0, i);
/* 210 */       String str3 = str2.replace('.', File.separatorChar);
/* 211 */       File localFile = new File(paramFile, str3);
/*     */ 
/* 218 */       if (!localFile.exists()) {
/* 219 */         localFile.mkdirs();
/*     */       }
/* 221 */       return new File(localFile, str1);
/*     */     }
/* 223 */     return new File(paramFile, str1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  61 */     versionOptions.put("-v1.1", Constants.StubVersion.V1_1);
/*  62 */     versionOptions.put("-vcompat", Constants.StubVersion.VCOMPAT);
/*  63 */     versionOptions.put("-v1.2", Constants.StubVersion.V1_2);
/*     */ 
/*  66 */     bootstrapClassNames = new HashSet();
/*     */ 
/*  69 */     bootstrapClassNames.add("java.lang.Exception");
/*  70 */     bootstrapClassNames.add("java.rmi.Remote");
/*  71 */     bootstrapClassNames.add("java.rmi.RemoteException");
/*  72 */     bootstrapClassNames.add("java.lang.RuntimeException");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.jrmp.JrmpGenerator
 * JD-Core Version:    0.6.2
 */