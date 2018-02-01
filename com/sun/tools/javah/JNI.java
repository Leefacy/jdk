/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ public class JNI extends Gen
/*     */ {
/*     */   JNI(Util paramUtil)
/*     */   {
/*  54 */     super(paramUtil);
/*     */   }
/*     */ 
/*     */   public String getIncludes() {
/*  58 */     return "#include <jni.h>";
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream, TypeElement paramTypeElement) throws Util.Exit {
/*     */     try {
/*  63 */       String str1 = this.mangler.mangle(paramTypeElement.getQualifiedName(), 1);
/*  64 */       PrintWriter localPrintWriter = wrapWriter(paramOutputStream);
/*  65 */       localPrintWriter.println(guardBegin(str1));
/*  66 */       localPrintWriter.println(cppGuardBegin());
/*     */ 
/*  69 */       List localList = getAllFields(paramTypeElement);
/*     */ 
/*  71 */       for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (VariableElement)((Iterator)localObject1).next();
/*  72 */         if (((VariableElement)localObject2).getModifiers().contains(Modifier.STATIC))
/*     */         {
/*  74 */           localObject3 = null;
/*  75 */           localObject3 = defineForStatic(paramTypeElement, (VariableElement)localObject2);
/*  76 */           if (localObject3 != null)
/*  77 */             localPrintWriter.println((String)localObject3);
/*     */         }
/*     */       }
/*  83 */       Object localObject3;
/*  82 */       localObject1 = ElementFilter.methodsIn(paramTypeElement.getEnclosedElements());
/*  83 */       for (Object localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ExecutableElement)((Iterator)localObject2).next();
/*  84 */         if (((ExecutableElement)localObject3).getModifiers().contains(Modifier.NATIVE)) {
/*  85 */           TypeMirror localTypeMirror = this.types.erasure(((ExecutableElement)localObject3).getReturnType());
/*  86 */           String str2 = signature((ExecutableElement)localObject3);
/*  87 */           TypeSignature localTypeSignature = new TypeSignature(this.elems);
/*  88 */           Name localName = ((ExecutableElement)localObject3).getSimpleName();
/*  89 */           int i = 0;
/*  90 */           for (Object localObject4 = ((List)localObject1).iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (ExecutableElement)((Iterator)localObject4).next();
/*  91 */             if ((localObject5 != localObject3) && 
/*  92 */               (localName
/*  92 */               .equals(((ExecutableElement)localObject5)
/*  92 */               .getSimpleName())) && 
/*  93 */               (((ExecutableElement)localObject5)
/*  93 */               .getModifiers().contains(Modifier.NATIVE))) {
/*  94 */               i = 1;
/*     */             }
/*     */           }
/*  97 */           localPrintWriter.println("/*");
/*  98 */           localPrintWriter.println(" * Class:     " + str1);
/*  99 */           localPrintWriter.println(" * Method:    " + this.mangler
/* 100 */             .mangle(localName, 2));
/*     */ 
/* 101 */           localPrintWriter.println(" * Signature: " + localTypeSignature.getTypeSignature(str2, localTypeMirror));
/* 102 */           localPrintWriter.println(" */");
/* 103 */           localPrintWriter.println("JNIEXPORT " + jniType(localTypeMirror) + " JNICALL " + this.mangler
/* 105 */             .mangleMethod((ExecutableElement)localObject3, paramTypeElement, i != 0 ? 8 : 7));
/*     */ 
/* 109 */           localPrintWriter.print("  (JNIEnv *, ");
/* 110 */           localObject4 = ((ExecutableElement)localObject3).getParameters();
/* 111 */           Object localObject5 = new ArrayList();
/* 112 */           for (Iterator localIterator = ((List)localObject4).iterator(); localIterator.hasNext(); ) { localObject6 = (VariableElement)localIterator.next();
/* 113 */             ((List)localObject5).add(this.types.erasure(((VariableElement)localObject6).asType()));
/*     */           }
/*     */           Object localObject6;
/* 115 */           if (((ExecutableElement)localObject3).getModifiers().contains(Modifier.STATIC))
/* 116 */             localPrintWriter.print("jclass");
/*     */           else {
/* 118 */             localPrintWriter.print("jobject");
/*     */           }
/* 120 */           for (localIterator = ((List)localObject5).iterator(); localIterator.hasNext(); ) { localObject6 = (TypeMirror)localIterator.next();
/* 121 */             localPrintWriter.print(", ");
/* 122 */             localPrintWriter.print(jniType((TypeMirror)localObject6));
/*     */           }
/* 124 */           localPrintWriter.println(");" + this.lineSep);
/*     */         }
/*     */       }
/* 127 */       localPrintWriter.println(cppGuardEnd());
/* 128 */       localPrintWriter.println(guardEnd(str1));
/*     */     } catch (TypeSignature.SignatureException localSignatureException) {
/* 130 */       this.util.error("jni.sigerror", new Object[] { localSignatureException.getMessage() });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final String jniType(TypeMirror paramTypeMirror) throws Util.Exit
/*     */   {
/* 136 */     TypeElement localTypeElement1 = this.elems.getTypeElement("java.lang.Throwable");
/* 137 */     TypeElement localTypeElement2 = this.elems.getTypeElement("java.lang.Class");
/* 138 */     TypeElement localTypeElement3 = this.elems.getTypeElement("java.lang.String");
/* 139 */     Element localElement = this.types.asElement(paramTypeMirror);
/*     */ 
/* 142 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 9:
/* 144 */       TypeMirror localTypeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 145 */       switch (1.$SwitchMap$javax$lang$model$type$TypeKind[localTypeMirror.getKind().ordinal()]) { case 1:
/* 146 */         return "jbooleanArray";
/*     */       case 2:
/* 147 */         return "jbyteArray";
/*     */       case 3:
/* 148 */         return "jcharArray";
/*     */       case 4:
/* 149 */         return "jshortArray";
/*     */       case 5:
/* 150 */         return "jintArray";
/*     */       case 6:
/* 151 */         return "jlongArray";
/*     */       case 7:
/* 152 */         return "jfloatArray";
/*     */       case 8:
/* 153 */         return "jdoubleArray";
/*     */       case 9:
/*     */       case 10:
/* 155 */         return "jobjectArray"; }
/* 156 */       throw new Error(localTypeMirror.toString());
/*     */     case 11:
/* 160 */       return "void";
/*     */     case 1:
/* 161 */       return "jboolean";
/*     */     case 2:
/* 162 */       return "jbyte";
/*     */     case 3:
/* 163 */       return "jchar";
/*     */     case 4:
/* 164 */       return "jshort";
/*     */     case 5:
/* 165 */       return "jint";
/*     */     case 6:
/* 166 */       return "jlong";
/*     */     case 7:
/* 167 */       return "jfloat";
/*     */     case 8:
/* 168 */       return "jdouble";
/*     */     case 10:
/* 171 */       if (localElement.equals(localTypeElement3))
/* 172 */         return "jstring";
/* 173 */       if (this.types.isAssignable(paramTypeMirror, localTypeElement1.asType()))
/* 174 */         return "jthrowable";
/* 175 */       if (this.types.isAssignable(paramTypeMirror, localTypeElement2.asType())) {
/* 176 */         return "jclass";
/*     */       }
/* 178 */       return "jobject";
/*     */     }
/*     */ 
/* 182 */     this.util.bug("jni.unknown.type");
/* 183 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.JNI
 * JD-Core Version:    0.6.2
 */