/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*    */ import com.sun.tools.javac.jvm.ClassReader;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Factory;
/*    */ import java.util.EnumSet;
/*    */ import javax.tools.JavaFileObject;
/*    */ import javax.tools.JavaFileObject.Kind;
/*    */ 
/*    */ public class JavadocClassReader extends ClassReader
/*    */ {
/*    */   private DocEnv docenv;
/* 62 */   private EnumSet<JavaFileObject.Kind> all = EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.SOURCE, JavaFileObject.Kind.HTML);
/*    */ 
/* 65 */   private EnumSet<JavaFileObject.Kind> noSource = EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.HTML);
/*    */ 
/*    */   public static JavadocClassReader instance0(Context paramContext)
/*    */   {
/* 47 */     Object localObject = (ClassReader)paramContext.get(classReaderKey);
/* 48 */     if (localObject == null)
/* 49 */       localObject = new JavadocClassReader(paramContext);
/* 50 */     return (JavadocClassReader)localObject;
/*    */   }
/*    */ 
/*    */   public static void preRegister(Context paramContext) {
/* 54 */     paramContext.put(classReaderKey, new Context.Factory() {
/*    */       public ClassReader make(Context paramAnonymousContext) {
/* 56 */         return new JavadocClassReader(paramAnonymousContext);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public JavadocClassReader(Context paramContext)
/*    */   {
/* 69 */     super(paramContext, true);
/* 70 */     this.docenv = DocEnv.instance(paramContext);
/* 71 */     this.preferSource = true;
/*    */   }
/*    */ 
/*    */   protected EnumSet<JavaFileObject.Kind> getPackageFileKinds()
/*    */   {
/* 79 */     return this.docenv.docClasses ? this.noSource : this.all;
/*    */   }
/*    */ 
/*    */   protected void extraFileActions(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject)
/*    */   {
/* 87 */     if (paramJavaFileObject.isNameCompatible("package", JavaFileObject.Kind.HTML))
/* 88 */       this.docenv.getPackageDoc(paramPackageSymbol).setDocPath(paramJavaFileObject);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.JavadocClassReader
 * JD-Core Version:    0.6.2
 */