/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.util.Abort;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class JavadocTool extends JavaCompiler
/*     */ {
/*     */   DocEnv docenv;
/*     */   final Messager messager;
/*     */   final JavadocClassReader javadocReader;
/*     */   final JavadocEnter javadocEnter;
/*     */   final Set<JavaFileObject> uniquefiles;
/* 391 */   static final boolean surrogatesSupported = surrogatesSupported();
/*     */ 
/*     */   protected JavadocTool(Context paramContext)
/*     */   {
/*  78 */     super(paramContext);
/*  79 */     this.messager = Messager.instance0(paramContext);
/*  80 */     this.javadocReader = JavadocClassReader.instance0(paramContext);
/*  81 */     this.javadocEnter = JavadocEnter.instance0(paramContext);
/*  82 */     this.uniquefiles = new HashSet();
/*     */   }
/*     */ 
/*     */   protected boolean keepComments()
/*     */   {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   public static JavadocTool make0(Context paramContext)
/*     */   {
/*  96 */     Messager localMessager = null;
/*     */     try
/*     */     {
/*  99 */       JavadocClassReader.preRegister(paramContext);
/*     */ 
/* 102 */       JavadocEnter.preRegister(paramContext);
/*     */ 
/* 105 */       JavadocMemberEnter.preRegister(paramContext);
/*     */ 
/* 108 */       JavadocTodo.preRegister(paramContext);
/*     */ 
/* 111 */       localMessager = Messager.instance0(paramContext);
/*     */ 
/* 113 */       return new JavadocTool(paramContext);
/*     */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 115 */       localMessager.error(-1, localCompletionFailure.getMessage(), new Object[0]);
/* 116 */     }return null;
/*     */   }
/*     */ 
/*     */   public RootDocImpl getRootDocImpl(String paramString1, String paramString2, ModifierFilter paramModifierFilter, List<String> paramList1, List<String[]> paramList, Iterable<? extends JavaFileObject> paramIterable, boolean paramBoolean1, List<String> paramList2, List<String> paramList3, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*     */     throws IOException
/*     */   {
/* 132 */     this.docenv = DocEnv.instance(this.context);
/* 133 */     this.docenv.showAccess = paramModifierFilter;
/* 134 */     this.docenv.quiet = paramBoolean4;
/* 135 */     this.docenv.breakiterator = paramBoolean1;
/* 136 */     this.docenv.setLocale(paramString1);
/* 137 */     this.docenv.setEncoding(paramString2);
/* 138 */     this.docenv.docClasses = paramBoolean2;
/* 139 */     this.docenv.legacyDoclet = paramBoolean3;
/* 140 */     this.javadocReader.sourceCompleter = (paramBoolean2 ? null : this.thisCompleter);
/*     */ 
/* 142 */     ListBuffer localListBuffer1 = new ListBuffer();
/* 143 */     ListBuffer localListBuffer2 = new ListBuffer();
/* 144 */     ListBuffer localListBuffer3 = new ListBuffer();
/*     */     try
/*     */     {
/* 147 */       Object localObject1 = (this.docenv.fileManager instanceof StandardJavaFileManager) ? (StandardJavaFileManager)this.docenv.fileManager : null;
/*     */       Object localObject3;
/*     */       Object localObject4;
/* 149 */       for (Object localObject2 = paramList1; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/* 150 */         localObject3 = (String)((List)localObject2).head;
/* 151 */         if ((!paramBoolean2) && (localObject1 != null) && (((String)localObject3).endsWith(".java")) && (new File((String)localObject3).exists())) {
/* 152 */           localObject4 = (JavaFileObject)localObject1.getJavaFileObjects(new String[] { localObject3 }).iterator().next();
/* 153 */           parse((JavaFileObject)localObject4, localListBuffer2, true);
/* 154 */         } else if (isValidPackageName((String)localObject3)) {
/* 155 */           localListBuffer1 = localListBuffer1.append(localObject3);
/* 156 */         } else if (((String)localObject3).endsWith(".java")) {
/* 157 */           if (localObject1 == null) {
/* 158 */             throw new IllegalArgumentException();
/*     */           }
/* 160 */           this.docenv.error(null, "main.file_not_found", (String)localObject3);
/*     */         } else {
/* 162 */           this.docenv.error(null, "main.illegal_package_name", (String)localObject3);
/*     */         }
/*     */       }
/* 165 */       for (localObject2 = paramIterable.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (JavaFileObject)((Iterator)localObject2).next();
/* 166 */         parse((JavaFileObject)localObject3, localListBuffer2, true);
/*     */       }
/*     */ 
/* 169 */       if (!paramBoolean2)
/*     */       {
/* 173 */         localObject2 = searchSubPackages(paramList2, localListBuffer1, paramList3);
/*     */ 
/* 176 */         for (localObject3 = localListBuffer1.toList(); ((List)localObject3).nonEmpty(); localObject3 = ((List)localObject3).tail)
/*     */         {
/* 178 */           localObject4 = (String)((List)localObject3).head;
/* 179 */           parsePackageClasses((String)localObject4, (List)((Map)localObject2).get(localObject4), localListBuffer3, paramList3);
/*     */         }
/*     */ 
/* 182 */         if (this.messager.nerrors() != 0) return null;
/*     */ 
/* 185 */         this.docenv.notice("main.Building_tree");
/* 186 */         this.javadocEnter.main(localListBuffer2.toList().appendList(localListBuffer3.toList()));
/*     */       }
/*     */     } catch (Abort localAbort) {
/*     */     }
/* 190 */     if (this.messager.nerrors() != 0) {
/* 191 */       return null;
/*     */     }
/* 193 */     if (paramBoolean2) {
/* 194 */       return new RootDocImpl(this.docenv, paramList1, paramList);
/*     */     }
/* 196 */     return new RootDocImpl(this.docenv, listClasses(localListBuffer2.toList()), localListBuffer1.toList(), paramList);
/*     */   }
/*     */ 
/*     */   boolean isValidPackageName(String paramString)
/*     */   {
/*     */     int i;
/* 202 */     while ((i = paramString.indexOf('.')) != -1) {
/* 203 */       if (!isValidClassName(paramString.substring(0, i))) return false;
/* 204 */       paramString = paramString.substring(i + 1);
/*     */     }
/* 206 */     return isValidClassName(paramString);
/*     */   }
/*     */ 
/*     */   private void parsePackageClasses(String paramString, List<JavaFileObject> paramList, ListBuffer<JCTree.JCCompilationUnit> paramListBuffer, List<String> paramList1)
/*     */     throws IOException
/*     */   {
/* 218 */     if (paramList1.contains(paramString)) {
/* 219 */       return;
/*     */     }
/*     */ 
/* 222 */     this.docenv.notice("main.Loading_source_files_for_package", paramString);
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 224 */     if (paramList == null) {
/* 225 */       localObject1 = this.docenv.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */ 
/* 227 */       localObject2 = new ListBuffer();
/* 228 */       for (JavaFileObject localJavaFileObject : this.docenv.fileManager.list((JavaFileManager.Location)localObject1, paramString, 
/* 229 */         EnumSet.of(JavaFileObject.Kind.SOURCE), 
/* 229 */         false))
/*     */       {
/* 230 */         String str1 = this.docenv.fileManager.inferBinaryName((JavaFileManager.Location)localObject1, localJavaFileObject);
/* 231 */         String str2 = getSimpleName(str1);
/* 232 */         if (isValidClassName(str2)) {
/* 233 */           ((ListBuffer)localObject2).append(localJavaFileObject);
/*     */         }
/*     */       }
/* 236 */       paramList = ((ListBuffer)localObject2).toList();
/*     */     }
/* 238 */     if (paramList.nonEmpty())
/* 239 */       for (localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JavaFileObject)((Iterator)localObject1).next();
/* 240 */         parse((JavaFileObject)localObject2, paramListBuffer, false);
/*     */       }
/*     */     else
/* 243 */       this.messager.warning(Messager.NOPOS, "main.no_source_files_for_package", new Object[] { paramString
/* 244 */         .replace(File.separatorChar, '.') });
/*     */   }
/*     */ 
/*     */   private void parse(JavaFileObject paramJavaFileObject, ListBuffer<JCTree.JCCompilationUnit> paramListBuffer, boolean paramBoolean)
/*     */   {
/* 250 */     if (this.uniquefiles.add(paramJavaFileObject)) {
/* 251 */       if (paramBoolean)
/* 252 */         this.docenv.notice("main.Loading_source_file", paramJavaFileObject.getName());
/* 253 */       paramListBuffer.append(parse(paramJavaFileObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Map<String, List<JavaFileObject>> searchSubPackages(List<String> paramList1, ListBuffer<String> paramListBuffer, List<String> paramList2)
/*     */     throws IOException
/*     */   {
/* 266 */     HashMap localHashMap1 = new HashMap();
/*     */ 
/* 269 */     HashMap localHashMap2 = new HashMap();
/* 270 */     localHashMap2.put("", Boolean.valueOf(true));
/* 271 */     for (Object localObject = paramList2.iterator(); ((Iterator)localObject).hasNext(); ) { String str = (String)((Iterator)localObject).next();
/* 272 */       localHashMap2.put(str, Boolean.valueOf(false));
/*     */     }
/* 274 */     localObject = this.docenv.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */ 
/* 277 */     searchSubPackages(paramList1, localHashMap2, paramListBuffer, localHashMap1, (StandardLocation)localObject, 
/* 281 */       EnumSet.of(JavaFileObject.Kind.SOURCE));
/*     */ 
/* 283 */     return localHashMap1;
/*     */   }
/*     */ 
/*     */   private void searchSubPackages(List<String> paramList, Map<String, Boolean> paramMap, ListBuffer<String> paramListBuffer, Map<String, List<JavaFileObject>> paramMap1, StandardLocation paramStandardLocation, Set<JavaFileObject.Kind> paramSet)
/*     */     throws IOException
/*     */   {
/* 292 */     for (String str1 : paramList)
/* 293 */       if (isIncluded(str1, paramMap))
/*     */       {
/* 296 */         for (JavaFileObject localJavaFileObject : this.docenv.fileManager.list(paramStandardLocation, str1, paramSet, true)) {
/* 297 */           String str2 = this.docenv.fileManager.inferBinaryName(paramStandardLocation, localJavaFileObject);
/* 298 */           String str3 = getPackageName(str2);
/* 299 */           String str4 = getSimpleName(str2);
/* 300 */           if ((isIncluded(str3, paramMap)) && (isValidClassName(str4))) {
/* 301 */             List localList = (List)paramMap1.get(str3);
/* 302 */             localList = localList == null ? List.of(localJavaFileObject) : localList.prepend(localJavaFileObject);
/* 303 */             paramMap1.put(str3, localList);
/* 304 */             if (!paramListBuffer.contains(str3))
/* 305 */               paramListBuffer.add(str3);
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private String getPackageName(String paramString) {
/* 312 */     int i = paramString.lastIndexOf(".");
/* 313 */     return i == -1 ? "" : paramString.substring(0, i);
/*     */   }
/*     */ 
/*     */   private String getSimpleName(String paramString) {
/* 317 */     int i = paramString.lastIndexOf(".");
/* 318 */     return i == -1 ? paramString : paramString.substring(i + 1);
/*     */   }
/*     */ 
/*     */   private boolean isIncluded(String paramString, Map<String, Boolean> paramMap) {
/* 322 */     Boolean localBoolean = (Boolean)paramMap.get(paramString);
/* 323 */     if (localBoolean == null) {
/* 324 */       localBoolean = Boolean.valueOf(isIncluded(getPackageName(paramString), paramMap));
/* 325 */       paramMap.put(paramString, localBoolean);
/*     */     }
/* 327 */     return localBoolean.booleanValue();
/*     */   }
/*     */ 
/*     */   private void searchSubPackage(String paramString, ListBuffer<String> paramListBuffer, List<String> paramList, Collection<File> paramCollection)
/*     */   {
/* 338 */     if (paramList.contains(paramString)) {
/* 339 */       return;
/*     */     }
/* 341 */     String str1 = paramString.replace('.', File.separatorChar);
/* 342 */     int i = 0;
/* 343 */     for (File localFile1 : paramCollection) {
/* 344 */       File localFile2 = new File(localFile1, str1);
/* 345 */       String[] arrayOfString1 = localFile2.list();
/*     */ 
/* 347 */       if (arrayOfString1 != null)
/* 348 */         for (String str2 : arrayOfString1)
/* 349 */           if ((i == 0) && 
/* 350 */             ((isValidJavaSourceFile(str2)) || 
/* 351 */             (isValidJavaClassFile(str2))) && 
/* 352 */             (!paramListBuffer
/* 352 */             .contains(paramString)))
/*     */           {
/* 353 */             paramListBuffer.append(paramString);
/* 354 */             i = 1;
/* 355 */           } else if ((isValidClassName(str2)) && 
/* 356 */             (new File(localFile2, str2)
/* 356 */             .isDirectory())) {
/* 357 */             searchSubPackage(paramString + "." + str2, paramListBuffer, paramList, paramCollection);
/*     */           }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isValidJavaClassFile(String paramString)
/*     */   {
/* 372 */     if (!paramString.endsWith(".class")) return false;
/* 373 */     String str = paramString.substring(0, paramString.length() - ".class".length());
/* 374 */     return isValidClassName(str);
/*     */   }
/*     */ 
/*     */   private static boolean isValidJavaSourceFile(String paramString)
/*     */   {
/* 384 */     if (!paramString.endsWith(".java")) return false;
/* 385 */     String str = paramString.substring(0, paramString.length() - ".java".length());
/* 386 */     return isValidClassName(str);
/*     */   }
/*     */ 
/*     */   private static boolean surrogatesSupported()
/*     */   {
/*     */     try
/*     */     {
/* 394 */       boolean bool = Character.isHighSurrogate('a');
/* 395 */       return true; } catch (NoSuchMethodError localNoSuchMethodError) {
/*     */     }
/* 397 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isValidClassName(String paramString)
/*     */   {
/* 409 */     if (paramString.length() < 1) return false;
/* 410 */     if (paramString.equals("package-info")) return true;
/*     */     int i;
/* 411 */     if (surrogatesSupported) {
/* 412 */       i = paramString.codePointAt(0);
/* 413 */       if (!Character.isJavaIdentifierStart(i))
/* 414 */         return false;
/* 415 */       for (int j = Character.charCount(i); j < paramString.length(); j += Character.charCount(i)) {
/* 416 */         i = paramString.codePointAt(j);
/* 417 */         if (!Character.isJavaIdentifierPart(i))
/* 418 */           return false;
/*     */       }
/*     */     } else {
/* 421 */       if (!Character.isJavaIdentifierStart(paramString.charAt(0)))
/* 422 */         return false;
/* 423 */       for (i = 1; i < paramString.length(); i++)
/* 424 */         if (!Character.isJavaIdentifierPart(paramString.charAt(i)))
/* 425 */           return false;
/*     */     }
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   List<JCTree.JCClassDecl> listClasses(List<JCTree.JCCompilationUnit> paramList)
/*     */   {
/* 434 */     ListBuffer localListBuffer = new ListBuffer();
/* 435 */     for (JCTree.JCCompilationUnit localJCCompilationUnit : paramList) {
/* 436 */       for (JCTree localJCTree : localJCCompilationUnit.defs) {
/* 437 */         if (localJCTree.hasTag(JCTree.Tag.CLASSDEF))
/* 438 */           localListBuffer.append((JCTree.JCClassDecl)localJCTree);
/*     */       }
/*     */     }
/* 441 */     return localListBuffer.toList();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.JavadocTool
 * JD-Core Version:    0.6.2
 */