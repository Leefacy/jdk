/*      */ package com.sun.tools.javadoc;
/*      */ 
/*      */ import com.sun.javadoc.AnnotatedType;
/*      */ import com.sun.javadoc.AnnotationTypeDoc;
/*      */ import com.sun.javadoc.ClassDoc;
/*      */ import com.sun.javadoc.ConstructorDoc;
/*      */ import com.sun.javadoc.FieldDoc;
/*      */ import com.sun.javadoc.MethodDoc;
/*      */ import com.sun.javadoc.PackageDoc;
/*      */ import com.sun.javadoc.ParamTag;
/*      */ import com.sun.javadoc.ParameterizedType;
/*      */ import com.sun.javadoc.SourcePosition;
/*      */ import com.sun.javadoc.TypeVariable;
/*      */ import com.sun.javadoc.WildcardType;
/*      */ import com.sun.source.util.TreePath;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCImport;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Name.Table;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.URI;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Set;
/*      */ import javax.tools.FileObject;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */ 
/*      */ public class ClassDocImpl extends ProgramElementDocImpl
/*      */   implements ClassDoc
/*      */ {
/*      */   public final Type.ClassType type;
/*      */   protected final Symbol.ClassSymbol tsym;
/*   92 */   boolean isIncluded = false;
/*      */   private SerializedForm serializedForm;
/*      */   private String name;
/*      */   private String qualifiedName;
/*      */   private String simpleTypeName;
/*      */ 
/*      */   public ClassDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  100 */     this(paramDocEnv, paramClassSymbol, null);
/*      */   }
/*      */ 
/*      */   public ClassDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath)
/*      */   {
/*  107 */     super(paramDocEnv, paramClassSymbol, paramTreePath);
/*  108 */     this.type = ((Type.ClassType)paramClassSymbol.type);
/*  109 */     this.tsym = paramClassSymbol;
/*      */   }
/*      */ 
/*      */   public com.sun.javadoc.Type getElementType() {
/*  113 */     return null;
/*      */   }
/*      */ 
/*      */   protected long getFlags()
/*      */   {
/*  120 */     return getFlags(this.tsym);
/*      */   }
/*      */ 
/*      */   static long getFlags(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*      */     try
/*      */     {
/*  128 */       return paramClassSymbol.flags();
/*      */     }
/*      */     catch (Symbol.CompletionFailure localCompletionFailure)
/*      */     {
/*      */     }
/*      */ 
/*  134 */     return getFlags(paramClassSymbol);
/*      */   }
/*      */ 
/*      */   static boolean isAnnotationType(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  142 */     return (getFlags(paramClassSymbol) & 0x2000) != 0L;
/*      */   }
/*      */ 
/*      */   protected Symbol.ClassSymbol getContainingClass()
/*      */   {
/*  149 */     return this.tsym.owner.enclClass();
/*      */   }
/*      */ 
/*      */   public boolean isClass()
/*      */   {
/*  157 */     return !Modifier.isInterface(getModifiers());
/*      */   }
/*      */ 
/*      */   public boolean isOrdinaryClass()
/*      */   {
/*  166 */     if ((isEnum()) || (isInterface()) || (isAnnotationType())) {
/*  167 */       return false;
/*      */     }
/*  169 */     for (Object localObject = this.type; ((com.sun.tools.javac.code.Type)localObject).hasTag(TypeTag.CLASS); localObject = this.env.types.supertype((com.sun.tools.javac.code.Type)localObject)) {
/*  170 */       if ((((com.sun.tools.javac.code.Type)localObject).tsym == this.env.syms.errorType.tsym) || (((com.sun.tools.javac.code.Type)localObject).tsym == this.env.syms.exceptionType.tsym))
/*      */       {
/*  172 */         return false;
/*      */       }
/*      */     }
/*  175 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isEnum()
/*      */   {
/*  184 */     return ((getFlags() & 0x4000) != 0L) && (!this.env.legacyDoclet);
/*      */   }
/*      */ 
/*      */   public boolean isInterface()
/*      */   {
/*  195 */     return Modifier.isInterface(getModifiers());
/*      */   }
/*      */ 
/*      */   public boolean isException()
/*      */   {
/*  203 */     if ((isEnum()) || (isInterface()) || (isAnnotationType())) {
/*  204 */       return false;
/*      */     }
/*  206 */     for (Object localObject = this.type; ((com.sun.tools.javac.code.Type)localObject).hasTag(TypeTag.CLASS); localObject = this.env.types.supertype((com.sun.tools.javac.code.Type)localObject)) {
/*  207 */       if (((com.sun.tools.javac.code.Type)localObject).tsym == this.env.syms.exceptionType.tsym) {
/*  208 */         return true;
/*      */       }
/*      */     }
/*  211 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isError()
/*      */   {
/*  219 */     if ((isEnum()) || (isInterface()) || (isAnnotationType())) {
/*  220 */       return false;
/*      */     }
/*  222 */     for (Object localObject = this.type; ((com.sun.tools.javac.code.Type)localObject).hasTag(TypeTag.CLASS); localObject = this.env.types.supertype((com.sun.tools.javac.code.Type)localObject)) {
/*  223 */       if (((com.sun.tools.javac.code.Type)localObject).tsym == this.env.syms.errorType.tsym) {
/*  224 */         return true;
/*      */       }
/*      */     }
/*  227 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isThrowable()
/*      */   {
/*  234 */     if ((isEnum()) || (isInterface()) || (isAnnotationType())) {
/*  235 */       return false;
/*      */     }
/*  237 */     for (Object localObject = this.type; ((com.sun.tools.javac.code.Type)localObject).hasTag(TypeTag.CLASS); localObject = this.env.types.supertype((com.sun.tools.javac.code.Type)localObject)) {
/*  238 */       if (((com.sun.tools.javac.code.Type)localObject).tsym == this.env.syms.throwableType.tsym) {
/*  239 */         return true;
/*      */       }
/*      */     }
/*  242 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAbstract()
/*      */   {
/*  249 */     return Modifier.isAbstract(getModifiers());
/*      */   }
/*      */ 
/*      */   public boolean isSynthetic()
/*      */   {
/*  256 */     return (getFlags() & 0x1000) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isIncluded()
/*      */   {
/*  268 */     if (this.isIncluded) {
/*  269 */       return true;
/*      */     }
/*  271 */     if (this.env.shouldDocument(this.tsym))
/*      */     {
/*  275 */       if (containingPackage().isIncluded()) {
/*  276 */         return this.isIncluded = 1;
/*      */       }
/*  278 */       ClassDoc localClassDoc = containingClass();
/*  279 */       if ((localClassDoc != null) && (localClassDoc.isIncluded())) {
/*  280 */         return this.isIncluded = 1;
/*      */       }
/*      */     }
/*  283 */     return false;
/*      */   }
/*      */ 
/*      */   public PackageDoc containingPackage()
/*      */   {
/*  291 */     PackageDocImpl localPackageDocImpl = this.env.getPackageDoc(this.tsym.packge());
/*  292 */     if (!localPackageDocImpl.setDocPath) {
/*      */       FileObject localFileObject;
/*      */       try {
/*  295 */         StandardLocation localStandardLocation = this.env.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*      */ 
/*  298 */         localFileObject = this.env.fileManager.getFileForInput(localStandardLocation, localPackageDocImpl
/*  299 */           .qualifiedName(), "package.html");
/*      */       } catch (IOException localIOException) {
/*  301 */         localFileObject = null;
/*      */       }
/*      */ 
/*  304 */       if (localFileObject == null)
/*      */       {
/*  307 */         SourcePosition localSourcePosition = position();
/*  308 */         if (((this.env.fileManager instanceof StandardJavaFileManager)) && ((localSourcePosition instanceof SourcePositionImpl)))
/*      */         {
/*  310 */           URI localURI = ((SourcePositionImpl)localSourcePosition).filename.toUri();
/*  311 */           if ("file".equals(localURI.getScheme())) {
/*  312 */             File localFile1 = new File(localURI);
/*  313 */             File localFile2 = localFile1.getParentFile();
/*  314 */             if (localFile2 != null) {
/*  315 */               File localFile3 = new File(localFile2, "package.html");
/*  316 */               if (localFile3.exists()) {
/*  317 */                 StandardJavaFileManager localStandardJavaFileManager = (StandardJavaFileManager)this.env.fileManager;
/*  318 */                 localFileObject = (FileObject)localStandardJavaFileManager.getJavaFileObjects(new File[] { localFile3 }).iterator().next();
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  326 */       localPackageDocImpl.setDocPath(localFileObject);
/*      */     }
/*  328 */     return localPackageDocImpl;
/*      */   }
/*      */ 
/*      */   public String name()
/*      */   {
/*  343 */     if (this.name == null) {
/*  344 */       this.name = getClassName(this.tsym, false);
/*      */     }
/*  346 */     return this.name;
/*      */   }
/*      */ 
/*      */   public String qualifiedName()
/*      */   {
/*  361 */     if (this.qualifiedName == null) {
/*  362 */       this.qualifiedName = getClassName(this.tsym, true);
/*      */     }
/*  364 */     return this.qualifiedName;
/*      */   }
/*      */ 
/*      */   public String typeName()
/*      */   {
/*  375 */     return name();
/*      */   }
/*      */ 
/*      */   public String qualifiedTypeName()
/*      */   {
/*  385 */     return qualifiedName();
/*      */   }
/*      */ 
/*      */   public String simpleTypeName()
/*      */   {
/*  392 */     if (this.simpleTypeName == null) {
/*  393 */       this.simpleTypeName = this.tsym.name.toString();
/*      */     }
/*  395 */     return this.simpleTypeName;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  406 */     return classToString(this.env, this.tsym, true);
/*      */   }
/*      */ 
/*      */   static String getClassName(Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean)
/*      */   {
/*  414 */     if (paramBoolean) {
/*  415 */       return paramClassSymbol.getQualifiedName().toString();
/*      */     }
/*  417 */     String str = "";
/*  418 */     for (; paramClassSymbol != null; paramClassSymbol = paramClassSymbol.owner.enclClass()) {
/*  419 */       str = paramClassSymbol.name + (str.equals("") ? "" : ".") + str;
/*      */     }
/*  421 */     return str;
/*      */   }
/*      */ 
/*      */   static String classToString(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean)
/*      */   {
/*  432 */     StringBuilder localStringBuilder = new StringBuilder();
/*  433 */     if (!paramClassSymbol.isInner()) {
/*  434 */       localStringBuilder.append(getClassName(paramClassSymbol, paramBoolean));
/*      */     }
/*      */     else {
/*  437 */       Symbol.ClassSymbol localClassSymbol = paramClassSymbol.owner.enclClass();
/*  438 */       localStringBuilder.append(classToString(paramDocEnv, localClassSymbol, paramBoolean))
/*  439 */         .append('.')
/*  440 */         .append(paramClassSymbol.name);
/*      */     }
/*      */ 
/*  442 */     localStringBuilder.append(TypeMaker.typeParametersString(paramDocEnv, paramClassSymbol, paramBoolean));
/*  443 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   static boolean isGeneric(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  451 */     return paramClassSymbol.type.allparams().nonEmpty();
/*      */   }
/*      */ 
/*      */   public TypeVariable[] typeParameters()
/*      */   {
/*  459 */     if (this.env.legacyDoclet) {
/*  460 */       return new TypeVariable[0];
/*      */     }
/*  462 */     TypeVariable[] arrayOfTypeVariable = new TypeVariable[this.type.getTypeArguments().length()];
/*  463 */     TypeMaker.getTypes(this.env, this.type.getTypeArguments(), arrayOfTypeVariable);
/*  464 */     return arrayOfTypeVariable;
/*      */   }
/*      */ 
/*      */   public ParamTag[] typeParamTags()
/*      */   {
/*  473 */     return this.env.legacyDoclet ? new ParamTag[0] : 
/*  473 */       comment().typeParamTags();
/*      */   }
/*      */ 
/*      */   public String modifiers()
/*      */   {
/*  482 */     return Modifier.toString(modifierSpecifier());
/*      */   }
/*      */ 
/*      */   public int modifierSpecifier()
/*      */   {
/*  487 */     int i = getModifiers();
/*  488 */     return (isInterface()) || (isAnnotationType()) ? i & 0xFFFFFBFF : i;
/*      */   }
/*      */ 
/*      */   public ClassDoc superclass()
/*      */   {
/*  500 */     if ((isInterface()) || (isAnnotationType())) return null;
/*  501 */     if (this.tsym == this.env.syms.objectType.tsym) return null;
/*  502 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.env.types.supertype(this.type).tsym;
/*  503 */     if ((localClassSymbol == null) || (localClassSymbol == this.tsym)) localClassSymbol = (Symbol.ClassSymbol)this.env.syms.objectType.tsym;
/*  504 */     return this.env.getClassDoc(localClassSymbol);
/*      */   }
/*      */ 
/*      */   public com.sun.javadoc.Type superclassType()
/*      */   {
/*  513 */     if ((isInterface()) || (isAnnotationType()) || (this.tsym == this.env.syms.objectType.tsym))
/*      */     {
/*  515 */       return null;
/*  516 */     }com.sun.tools.javac.code.Type localType = this.env.types.supertype(this.type);
/*  517 */     return TypeMaker.getType(this.env, localType
/*  518 */       .hasTag(TypeTag.NONE) ? 
/*  518 */       this.env.syms.objectType : localType);
/*      */   }
/*      */ 
/*      */   public boolean subclassOf(ClassDoc paramClassDoc)
/*      */   {
/*  528 */     return this.tsym.isSubClass(((ClassDocImpl)paramClassDoc).tsym, this.env.types);
/*      */   }
/*      */ 
/*      */   public ClassDoc[] interfaces()
/*      */   {
/*  539 */     ListBuffer localListBuffer = new ListBuffer();
/*  540 */     for (com.sun.tools.javac.code.Type localType : this.env.types.interfaces(this.type)) {
/*  541 */       localListBuffer.append(this.env.getClassDoc((Symbol.ClassSymbol)localType.tsym));
/*      */     }
/*      */ 
/*  544 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*      */   }
/*      */ 
/*      */   public com.sun.javadoc.Type[] interfaceTypes()
/*      */   {
/*  555 */     return TypeMaker.getTypes(this.env, this.env.types.interfaces(this.type));
/*      */   }
/*      */ 
/*      */   public FieldDoc[] fields(boolean paramBoolean)
/*      */   {
/*  563 */     return fields(paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public FieldDoc[] fields()
/*      */   {
/*  570 */     return fields(true, false);
/*      */   }
/*      */ 
/*      */   public FieldDoc[] enumConstants()
/*      */   {
/*  577 */     return fields(false, true);
/*      */   }
/*      */ 
/*      */   private FieldDoc[] fields(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  586 */     List localList = List.nil();
/*  587 */     for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  588 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 4)) {
/*  589 */         Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/*  590 */         boolean bool = ((localVarSymbol.flags() & 0x4000) != 0L) && (!this.env.legacyDoclet);
/*      */ 
/*  592 */         if ((bool == paramBoolean2) && ((!paramBoolean1) || 
/*  593 */           (this.env
/*  593 */           .shouldDocument(localVarSymbol))))
/*      */         {
/*  594 */           localList = localList.prepend(this.env.getFieldDoc(localVarSymbol));
/*      */         }
/*      */       }
/*      */     }
/*  598 */     return (FieldDoc[])localList.toArray(new FieldDocImpl[localList.length()]);
/*      */   }
/*      */ 
/*      */   public MethodDoc[] methods(boolean paramBoolean)
/*      */   {
/*  610 */     Names localNames = this.tsym.name.table.names;
/*  611 */     List localList = List.nil();
/*  612 */     for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  613 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 16) && (localEntry.sym.name != localNames.init) && (localEntry.sym.name != localNames.clinit))
/*      */       {
/*  617 */         Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.sym;
/*  618 */         if ((!paramBoolean) || (this.env.shouldDocument(localMethodSymbol))) {
/*  619 */           localList = localList.prepend(this.env.getMethodDoc(localMethodSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  624 */     return (MethodDoc[])localList.toArray(new MethodDocImpl[localList.length()]);
/*      */   }
/*      */ 
/*      */   public MethodDoc[] methods()
/*      */   {
/*  634 */     return methods(true);
/*      */   }
/*      */ 
/*      */   public ConstructorDoc[] constructors(boolean paramBoolean)
/*      */   {
/*  645 */     Names localNames = this.tsym.name.table.names;
/*  646 */     List localList = List.nil();
/*  647 */     for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  648 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 16) && (localEntry.sym.name == localNames.init))
/*      */       {
/*  650 */         Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.sym;
/*  651 */         if ((!paramBoolean) || (this.env.shouldDocument(localMethodSymbol))) {
/*  652 */           localList = localList.prepend(this.env.getConstructorDoc(localMethodSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  657 */     return (ConstructorDoc[])localList.toArray(new ConstructorDocImpl[localList.length()]);
/*      */   }
/*      */ 
/*      */   public ConstructorDoc[] constructors()
/*      */   {
/*  667 */     return constructors(true);
/*      */   }
/*      */ 
/*      */   void addAllClasses(ListBuffer<ClassDocImpl> paramListBuffer, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  676 */       if (isSynthetic()) return;
/*      */ 
/*  678 */       if (!JavadocTool.isValidClassName(this.tsym.name.toString())) return;
/*  679 */       if ((paramBoolean) && (!this.env.shouldDocument(this.tsym))) return;
/*  680 */       if (paramListBuffer.contains(this)) return;
/*  681 */       paramListBuffer.append(this);
/*  682 */       List localList = List.nil();
/*  683 */       for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; 
/*  684 */         localEntry = localEntry.sibling) {
/*  685 */         if ((localEntry.sym != null) && (localEntry.sym.kind == 2)) {
/*  686 */           Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localEntry.sym;
/*  687 */           ClassDocImpl localClassDocImpl = this.env.getClassDoc(localClassSymbol);
/*  688 */           if ((!localClassDocImpl.isSynthetic()) && 
/*  689 */             (localClassDocImpl != null)) localList = localList.prepend(localClassDocImpl);
/*      */         }
/*      */       }
/*      */ 
/*  693 */       for (; localList.nonEmpty(); localList = localList.tail)
/*  694 */         ((ClassDocImpl)localList.head).addAllClasses(paramListBuffer, paramBoolean);
/*      */     }
/*      */     catch (Symbol.CompletionFailure localCompletionFailure)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public ClassDoc[] innerClasses(boolean paramBoolean)
/*      */   {
/*  710 */     ListBuffer localListBuffer = new ListBuffer();
/*  711 */     for (Scope.Entry localEntry = this.tsym.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/*  712 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 2)) {
/*  713 */         Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localEntry.sym;
/*  714 */         if (((localClassSymbol.flags_field & 0x1000) == 0L) && (
/*  715 */           (!paramBoolean) || (this.env.isVisible(localClassSymbol)))) {
/*  716 */           localListBuffer.prepend(this.env.getClassDoc(localClassSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  721 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*      */   }
/*      */ 
/*      */   public ClassDoc[] innerClasses()
/*      */   {
/*  732 */     return innerClasses(true);
/*      */   }
/*      */ 
/*      */   public ClassDoc findClass(String paramString)
/*      */   {
/*  745 */     ClassDoc localClassDoc = searchClass(paramString);
/*  746 */     if (localClassDoc == null) {
/*  747 */       ClassDocImpl localClassDocImpl = (ClassDocImpl)containingClass();
/*      */ 
/*  749 */       while ((localClassDocImpl != null) && (localClassDocImpl.containingClass() != null)) {
/*  750 */         localClassDocImpl = (ClassDocImpl)localClassDocImpl.containingClass();
/*      */       }
/*      */ 
/*  753 */       localClassDoc = localClassDocImpl == null ? null : localClassDocImpl
/*  753 */         .searchClass(paramString);
/*      */     }
/*      */ 
/*  755 */     return localClassDoc;
/*      */   }
/*      */ 
/*      */   private ClassDoc searchClass(String paramString) {
/*  759 */     Names localNames = this.tsym.name.table.names;
/*      */ 
/*  762 */     Object localObject1 = this.env.lookupClass(paramString);
/*  763 */     if (localObject1 != null)
/*  764 */       return localObject1;
/*      */     Object localObject4;
/*  770 */     for (localObject4 : innerClasses()) {
/*  771 */       if ((((ClassDoc)localObject4).name().equals(paramString)) || 
/*  776 */         (((ClassDoc)localObject4)
/*  776 */         .name().endsWith("." + paramString))) {
/*  777 */         return localObject4;
/*      */       }
/*  779 */       ClassDoc localClassDoc = ((ClassDocImpl)localObject4).searchClass(paramString);
/*  780 */       if (localClassDoc != null) {
/*  781 */         return localClassDoc;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  787 */     localObject1 = containingPackage().findClass(paramString);
/*  788 */     if (localObject1 != null) {
/*  789 */       return localObject1;
/*      */     }
/*      */ 
/*  793 */     if (this.tsym.completer != null) {
/*  794 */       this.tsym.complete();
/*      */     }
/*      */ 
/*  799 */     if (this.tsym.sourcefile != null)
/*      */     {
/*  803 */       ??? = this.env.enter.getEnv(this.tsym);
/*  804 */       if (??? == null) return null;
/*      */ 
/*  806 */       Object localObject3 = ((Env)???).toplevel.namedImportScope;
/*  807 */       for (Scope.Entry localEntry = ((Scope)localObject3).lookup(localNames.fromString(paramString)); localEntry.scope != null; localEntry = localEntry.next()) {
/*  808 */         if (localEntry.sym.kind == 2) {
/*  809 */           localObject4 = this.env.getClassDoc((Symbol.ClassSymbol)localEntry.sym);
/*  810 */           return localObject4;
/*      */         }
/*      */       }
/*      */ 
/*  814 */       localObject3 = ((Env)???).toplevel.starImportScope;
/*  815 */       for (localEntry = ((Scope)localObject3).lookup(localNames.fromString(paramString)); localEntry.scope != null; localEntry = localEntry.next()) {
/*  816 */         if (localEntry.sym.kind == 2) {
/*  817 */           localObject4 = this.env.getClassDoc((Symbol.ClassSymbol)localEntry.sym);
/*  818 */           return localObject4;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  823 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean hasParameterTypes(Symbol.MethodSymbol paramMethodSymbol, String[] paramArrayOfString)
/*      */   {
/*  829 */     if (paramArrayOfString == null)
/*      */     {
/*  831 */       return true;
/*      */     }
/*      */ 
/*  834 */     int i = 0;
/*  835 */     List localList = paramMethodSymbol.type.getParameterTypes();
/*      */ 
/*  837 */     if (paramArrayOfString.length != localList.length()) {
/*  838 */       return false;
/*      */     }
/*      */ 
/*  841 */     for (com.sun.tools.javac.code.Type localType : localList) {
/*  842 */       String str = paramArrayOfString[(i++)];
/*      */ 
/*  844 */       if (i == paramArrayOfString.length) {
/*  845 */         str = str.replace("...", "[]");
/*      */       }
/*  847 */       if (!hasTypeName(this.env.types.erasure(localType), str)) {
/*  848 */         return false;
/*      */       }
/*      */     }
/*  851 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean hasTypeName(com.sun.tools.javac.code.Type paramType, String paramString)
/*      */   {
/*  860 */     return (paramString
/*  856 */       .equals(TypeMaker.getTypeName(paramType, true))) || 
/*  858 */       (paramString
/*  858 */       .equals(TypeMaker.getTypeName(paramType, false))) || 
/*  860 */       ((qualifiedName() + "." + paramString).equals(TypeMaker.getTypeName(paramType, true)));
/*      */   }
/*      */ 
/*      */   public MethodDocImpl findMethod(String paramString, String[] paramArrayOfString)
/*      */   {
/*  877 */     return searchMethod(paramString, paramArrayOfString, new HashSet());
/*      */   }
/*      */ 
/*      */   private MethodDocImpl searchMethod(String paramString, String[] paramArrayOfString, Set<ClassDocImpl> paramSet)
/*      */   {
/*  884 */     Names localNames = this.tsym.name.table.names;
/*      */ 
/*  886 */     if (localNames.init.contentEquals(paramString)) {
/*  887 */       return null;
/*      */     }
/*      */ 
/*  893 */     if (paramSet.contains(this)) {
/*  894 */       return null;
/*      */     }
/*  896 */     paramSet.add(this);
/*      */ 
/*  916 */     Scope.Entry localEntry = this.tsym.members().lookup(localNames.fromString(paramString));
/*      */ 
/*  922 */     if (paramArrayOfString == null)
/*      */     {
/*  928 */       localObject = null;
/*  929 */       for (; localEntry.scope != null; localEntry = localEntry.next()) {
/*  930 */         if (localEntry.sym.kind == 16)
/*      */         {
/*  932 */           if (localEntry.sym.name.toString().equals(paramString)) {
/*  933 */             localObject = (Symbol.MethodSymbol)localEntry.sym;
/*      */           }
/*      */         }
/*      */       }
/*  937 */       if (localObject != null)
/*  938 */         return this.env.getMethodDoc((Symbol.MethodSymbol)localObject);
/*      */     }
/*      */     else {
/*  941 */       for (; localEntry.scope != null; localEntry = localEntry.next()) {
/*  942 */         if ((localEntry.sym != null) && (localEntry.sym.kind == 16))
/*      */         {
/*  945 */           if (hasParameterTypes((Symbol.MethodSymbol)localEntry.sym, paramArrayOfString)) {
/*  946 */             return this.env.getMethodDoc((Symbol.MethodSymbol)localEntry.sym);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  956 */     ClassDocImpl localClassDocImpl = (ClassDocImpl)superclass();
/*      */     MethodDocImpl localMethodDocImpl;
/*  957 */     if (localClassDocImpl != null) {
/*  958 */       localMethodDocImpl = localClassDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  959 */       if (localMethodDocImpl != null) {
/*  960 */         return localMethodDocImpl;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  965 */     Object localObject = interfaces();
/*  966 */     for (int i = 0; i < localObject.length; i++) {
/*  967 */       localClassDocImpl = (ClassDocImpl)localObject[i];
/*  968 */       localMethodDocImpl = localClassDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  969 */       if (localMethodDocImpl != null) {
/*  970 */         return localMethodDocImpl;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  975 */     localClassDocImpl = (ClassDocImpl)containingClass();
/*  976 */     if (localClassDocImpl != null) {
/*  977 */       localMethodDocImpl = localClassDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  978 */       if (localMethodDocImpl != null) {
/*  979 */         return localMethodDocImpl;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  991 */     return null;
/*      */   }
/*      */ 
/*      */   public ConstructorDoc findConstructor(String paramString, String[] paramArrayOfString)
/*      */   {
/* 1003 */     Names localNames = this.tsym.name.table.names;
/* 1004 */     for (Scope.Entry localEntry = this.tsym.members().lookup(localNames.fromString("<init>")); localEntry.scope != null; localEntry = localEntry.next()) {
/* 1005 */       if ((localEntry.sym.kind == 16) && 
/* 1006 */         (hasParameterTypes((Symbol.MethodSymbol)localEntry.sym, paramArrayOfString))) {
/* 1007 */         return this.env.getConstructorDoc((Symbol.MethodSymbol)localEntry.sym);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1020 */     return null;
/*      */   }
/*      */ 
/*      */   public FieldDoc findField(String paramString)
/*      */   {
/* 1036 */     return searchField(paramString, new HashSet());
/*      */   }
/*      */ 
/*      */   private FieldDocImpl searchField(String paramString, Set<ClassDocImpl> paramSet) {
/* 1040 */     Names localNames = this.tsym.name.table.names;
/* 1041 */     if (paramSet.contains(this)) {
/* 1042 */       return null;
/*      */     }
/* 1044 */     paramSet.add(this);
/*      */ 
/* 1046 */     for (Object localObject1 = this.tsym.members().lookup(localNames.fromString(paramString)); ((Scope.Entry)localObject1).scope != null; localObject1 = ((Scope.Entry)localObject1).next()) {
/* 1047 */       if (((Scope.Entry)localObject1).sym.kind == 4)
/*      */       {
/* 1049 */         return this.env.getFieldDoc((Symbol.VarSymbol)((Scope.Entry)localObject1).sym);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1056 */     localObject1 = (ClassDocImpl)containingClass();
/* 1057 */     if (localObject1 != null) {
/* 1058 */       localObject2 = ((ClassDocImpl)localObject1).searchField(paramString, paramSet);
/* 1059 */       if (localObject2 != null) {
/* 1060 */         return localObject2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1065 */     localObject1 = (ClassDocImpl)superclass();
/* 1066 */     if (localObject1 != null) {
/* 1067 */       localObject2 = ((ClassDocImpl)localObject1).searchField(paramString, paramSet);
/* 1068 */       if (localObject2 != null) {
/* 1069 */         return localObject2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1074 */     Object localObject2 = interfaces();
/* 1075 */     for (int i = 0; i < localObject2.length; i++) {
/* 1076 */       localObject1 = (ClassDocImpl)localObject2[i];
/* 1077 */       FieldDocImpl localFieldDocImpl = ((ClassDocImpl)localObject1).searchField(paramString, paramSet);
/* 1078 */       if (localFieldDocImpl != null) {
/* 1079 */         return localFieldDocImpl;
/*      */       }
/*      */     }
/*      */ 
/* 1083 */     return null;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public ClassDoc[] importedClasses()
/*      */   {
/* 1100 */     if (this.tsym.sourcefile == null) return new ClassDoc[0];
/*      */ 
/* 1102 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1104 */     Env localEnv = this.env.enter.getEnv(this.tsym);
/* 1105 */     if (localEnv == null) return new ClassDocImpl[0];
/*      */ 
/* 1107 */     Name localName = this.tsym.name.table.names.asterisk;
/* 1108 */     for (JCTree localJCTree1 : localEnv.toplevel.defs) {
/* 1109 */       if (localJCTree1.hasTag(JCTree.Tag.IMPORT)) {
/* 1110 */         JCTree localJCTree2 = ((JCTree.JCImport)localJCTree1).qualid;
/* 1111 */         if ((TreeInfo.name(localJCTree2) != localName) && ((localJCTree2.type.tsym.kind & 0x2) != 0))
/*      */         {
/* 1113 */           localListBuffer.append(this.env
/* 1114 */             .getClassDoc((Symbol.ClassSymbol)localJCTree2.type.tsym));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1119 */     return (ClassDoc[])localListBuffer.toArray(new ClassDocImpl[localListBuffer.length()]);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public PackageDoc[] importedPackages()
/*      */   {
/* 1138 */     if (this.tsym.sourcefile == null) return new PackageDoc[0];
/*      */ 
/* 1140 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1143 */     Names localNames = this.tsym.name.table.names;
/* 1144 */     localListBuffer.append(this.env.getPackageDoc(this.env.reader.enterPackage(localNames.java_lang)));
/*      */ 
/* 1146 */     Env localEnv = this.env.enter.getEnv(this.tsym);
/* 1147 */     if (localEnv == null) return new PackageDocImpl[0];
/*      */ 
/* 1149 */     for (JCTree localJCTree1 : localEnv.toplevel.defs) {
/* 1150 */       if (localJCTree1.hasTag(JCTree.Tag.IMPORT)) {
/* 1151 */         JCTree localJCTree2 = ((JCTree.JCImport)localJCTree1).qualid;
/* 1152 */         if (TreeInfo.name(localJCTree2) == localNames.asterisk) {
/* 1153 */           JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)localJCTree2;
/* 1154 */           Symbol.TypeSymbol localTypeSymbol = localJCFieldAccess.selected.type.tsym;
/* 1155 */           PackageDocImpl localPackageDocImpl = this.env.getPackageDoc(localTypeSymbol.packge());
/* 1156 */           if (!localListBuffer.contains(localPackageDocImpl)) {
/* 1157 */             localListBuffer.append(localPackageDocImpl);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1162 */     return (PackageDoc[])localListBuffer.toArray(new PackageDocImpl[localListBuffer.length()]);
/*      */   }
/*      */ 
/*      */   public String dimension()
/*      */   {
/* 1170 */     return "";
/*      */   }
/*      */ 
/*      */   public ClassDoc asClassDoc()
/*      */   {
/* 1177 */     return this;
/*      */   }
/*      */ 
/*      */   public AnnotationTypeDoc asAnnotationTypeDoc()
/*      */   {
/* 1184 */     return null;
/*      */   }
/*      */ 
/*      */   public ParameterizedType asParameterizedType()
/*      */   {
/* 1191 */     return null;
/*      */   }
/*      */ 
/*      */   public TypeVariable asTypeVariable()
/*      */   {
/* 1198 */     return null;
/*      */   }
/*      */ 
/*      */   public WildcardType asWildcardType()
/*      */   {
/* 1205 */     return null;
/*      */   }
/*      */ 
/*      */   public AnnotatedType asAnnotatedType()
/*      */   {
/* 1212 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isPrimitive()
/*      */   {
/* 1219 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isSerializable()
/*      */   {
/*      */     try
/*      */     {
/* 1235 */       return this.env.types.isSubtype(this.type, this.env.syms.serializableType);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*      */     }
/* 1238 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isExternalizable()
/*      */   {
/*      */     try
/*      */     {
/* 1248 */       return this.env.types.isSubtype(this.type, this.env.externalizableSym.type);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*      */     }
/* 1251 */     return false;
/*      */   }
/*      */ 
/*      */   public MethodDoc[] serializationMethods()
/*      */   {
/* 1262 */     if (this.serializedForm == null) {
/* 1263 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */ 
/* 1266 */     return this.serializedForm.methods();
/*      */   }
/*      */ 
/*      */   public FieldDoc[] serializableFields()
/*      */   {
/* 1287 */     if (this.serializedForm == null) {
/* 1288 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */ 
/* 1291 */     return this.serializedForm.fields();
/*      */   }
/*      */ 
/*      */   public boolean definesSerializableFields()
/*      */   {
/* 1302 */     if ((!isSerializable()) || (isExternalizable())) {
/* 1303 */       return false;
/*      */     }
/* 1305 */     if (this.serializedForm == null) {
/* 1306 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */ 
/* 1309 */     return this.serializedForm.definesSerializableFields();
/*      */   }
/*      */ 
/*      */   boolean isRuntimeException()
/*      */   {
/* 1319 */     return this.tsym.isSubClass(this.env.syms.runtimeExceptionType.tsym, this.env.types);
/*      */   }
/*      */ 
/*      */   public SourcePosition position()
/*      */   {
/* 1328 */     if (this.tsym.sourcefile == null) return null;
/* 1329 */     return SourcePositionImpl.make(this.tsym.sourcefile, this.tree == null ? -1 : this.tree.pos, this.lineMap);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ClassDocImpl
 * JD-Core Version:    0.6.2
 */