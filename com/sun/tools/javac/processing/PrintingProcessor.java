/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.Parameterizable;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleElementVisitor7;
/*     */ import javax.lang.model.util.SimpleElementVisitor8;
/*     */ 
/*     */ @SupportedAnnotationTypes({"*"})
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_8)
/*     */ public class PrintingProcessor extends AbstractProcessor
/*     */ {
/*     */   PrintWriter writer;
/*     */ 
/*     */   public PrintingProcessor()
/*     */   {
/*  58 */     this.writer = new PrintWriter(System.out);
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer paramWriter) {
/*  62 */     this.writer = new PrintWriter(paramWriter);
/*     */   }
/*     */ 
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment)
/*     */   {
/*  69 */     for (Element localElement : paramRoundEnvironment.getRootElements()) {
/*  70 */       print(localElement);
/*     */     }
/*     */ 
/*  74 */     return true;
/*     */   }
/*     */ 
/*     */   void print(Element paramElement)
/*     */   {
/*  79 */     ((PrintingElementVisitor)new PrintingElementVisitor(this.writer, this.processingEnv.getElementUtils())
/*  79 */       .visit(paramElement))
/*  79 */       .flush();
/*     */   }
/*     */ 
/*     */   public static class PrintingElementVisitor extends SimpleElementVisitor8<PrintingElementVisitor, Boolean>
/*     */   {
/*     */     int indentation;
/*     */     final PrintWriter writer;
/*     */     final Elements elementUtils;
/* 517 */     private static final String[] spaces = { "", "  ", "    ", "      ", "        ", "          ", "            ", "              ", "                ", "                  ", "                    " };
/*     */ 
/*     */     public PrintingElementVisitor(Writer paramWriter, Elements paramElements)
/*     */     {
/*  93 */       this.writer = new PrintWriter(paramWriter);
/*  94 */       this.elementUtils = paramElements;
/*  95 */       this.indentation = 0;
/*     */     }
/*     */ 
/*     */     protected PrintingElementVisitor defaultAction(Element paramElement, Boolean paramBoolean)
/*     */     {
/* 100 */       if ((paramBoolean != null) && (paramBoolean.booleanValue()))
/* 101 */         this.writer.println();
/* 102 */       printDocComment(paramElement);
/* 103 */       printModifiers(paramElement);
/* 104 */       return this;
/*     */     }
/*     */ 
/*     */     public PrintingElementVisitor visitExecutable(ExecutableElement paramExecutableElement, Boolean paramBoolean)
/*     */     {
/* 109 */       ElementKind localElementKind = paramExecutableElement.getKind();
/*     */ 
/* 111 */       if ((localElementKind != ElementKind.STATIC_INIT) && (localElementKind != ElementKind.INSTANCE_INIT))
/*     */       {
/* 113 */         Element localElement = paramExecutableElement.getEnclosingElement();
/*     */ 
/* 116 */         if ((localElementKind == ElementKind.CONSTRUCTOR) && (localElement != null))
/*     */         {
/* 125 */           if (NestingKind.ANONYMOUS == new SimpleElementVisitor7()
/*     */           {
/*     */             public NestingKind visitType(TypeElement paramAnonymousTypeElement, Void paramAnonymousVoid)
/*     */             {
/* 123 */               return paramAnonymousTypeElement.getNestingKind();
/*     */             }
/*     */           }
/* 125 */           .visit(localElement))
/*     */           {
/* 126 */             return this;
/*     */           }
/*     */         }
/* 128 */         defaultAction(paramExecutableElement, Boolean.valueOf(true));
/* 129 */         printFormalTypeParameters(paramExecutableElement, true);
/*     */ 
/* 131 */         switch (PrintingProcessor.1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()])
/*     */         {
/*     */         case 1:
/* 134 */           this.writer.print(paramExecutableElement.getEnclosingElement().getSimpleName());
/* 135 */           break;
/*     */         case 2:
/* 138 */           this.writer.print(paramExecutableElement.getReturnType().toString());
/* 139 */           this.writer.print(" ");
/* 140 */           this.writer.print(paramExecutableElement.getSimpleName().toString());
/*     */         }
/*     */ 
/* 144 */         this.writer.print("(");
/* 145 */         printParameters(paramExecutableElement);
/* 146 */         this.writer.print(")");
/* 147 */         AnnotationValue localAnnotationValue = paramExecutableElement.getDefaultValue();
/* 148 */         if (localAnnotationValue != null) {
/* 149 */           this.writer.print(" default " + localAnnotationValue);
/*     */         }
/* 151 */         printThrows(paramExecutableElement);
/* 152 */         this.writer.println(";");
/*     */       }
/* 154 */       return this;
/*     */     }
/*     */ 
/*     */     public PrintingElementVisitor visitType(TypeElement paramTypeElement, Boolean paramBoolean)
/*     */     {
/* 160 */       ElementKind localElementKind = paramTypeElement.getKind();
/* 161 */       NestingKind localNestingKind = paramTypeElement.getNestingKind();
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 163 */       if (NestingKind.ANONYMOUS == localNestingKind)
/*     */       {
/* 167 */         this.writer.print("new ");
/*     */ 
/* 171 */         localObject1 = paramTypeElement.getInterfaces();
/* 172 */         if (!((List)localObject1).isEmpty())
/* 173 */           this.writer.print(((List)localObject1).get(0));
/*     */         else {
/* 175 */           this.writer.print(paramTypeElement.getSuperclass());
/*     */         }
/* 177 */         this.writer.print("(");
/*     */ 
/* 180 */         if (((List)localObject1).isEmpty())
/*     */         {
/* 186 */           localObject2 = ElementFilter.constructorsIn(paramTypeElement
/* 186 */             .getEnclosedElements());
/*     */ 
/* 188 */           if (!((List)localObject2).isEmpty())
/* 189 */             printParameters((ExecutableElement)((List)localObject2).get(0));
/*     */         }
/* 191 */         this.writer.print(")");
/*     */       } else {
/* 193 */         if (localNestingKind == NestingKind.TOP_LEVEL) {
/* 194 */           localObject1 = this.elementUtils.getPackageOf(paramTypeElement);
/* 195 */           if (!((PackageElement)localObject1).isUnnamed()) {
/* 196 */             this.writer.print("package " + ((PackageElement)localObject1).getQualifiedName() + ";\n");
/*     */           }
/*     */         }
/* 199 */         defaultAction(paramTypeElement, Boolean.valueOf(true));
/*     */ 
/* 201 */         switch (PrintingProcessor.1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()]) {
/*     */         case 3:
/* 203 */           this.writer.print("@interface");
/* 204 */           break;
/*     */         default:
/* 206 */           this.writer.print(StringUtils.toLowerCase(localElementKind.toString()));
/*     */         }
/* 208 */         this.writer.print(" ");
/* 209 */         this.writer.print(paramTypeElement.getSimpleName());
/*     */ 
/* 211 */         printFormalTypeParameters(paramTypeElement, false);
/*     */ 
/* 214 */         if (localElementKind == ElementKind.CLASS) {
/* 215 */           localObject1 = paramTypeElement.getSuperclass();
/* 216 */           if (((TypeMirror)localObject1).getKind() != TypeKind.NONE)
/*     */           {
/* 218 */             localObject2 = (TypeElement)((DeclaredType)localObject1)
/* 218 */               .asElement();
/* 219 */             if (((TypeElement)localObject2).getSuperclass().getKind() != TypeKind.NONE) {
/* 220 */               this.writer.print(" extends " + localObject1);
/*     */             }
/*     */           }
/*     */         }
/* 224 */         printInterfaces(paramTypeElement);
/*     */       }
/* 226 */       this.writer.println(" {");
/* 227 */       this.indentation += 1;
/*     */       Element localElement;
/*     */       Iterator localIterator2;
/* 229 */       if (localElementKind == ElementKind.ENUM)
/*     */       {
/* 231 */         localObject1 = new ArrayList(paramTypeElement
/* 231 */           .getEnclosedElements());
/*     */ 
/* 233 */         localObject2 = new ArrayList();
/* 234 */         for (Iterator localIterator1 = ((List)localObject1).iterator(); localIterator1.hasNext(); ) { localElement = (Element)localIterator1.next();
/* 235 */           if (localElement.getKind() == ElementKind.ENUM_CONSTANT)
/* 236 */             ((List)localObject2).add(localElement);
/*     */         }
/* 238 */         if (!((List)localObject2).isEmpty())
/*     */         {
/* 240 */           for (int i = 0; i < ((List)localObject2).size() - 1; i++) {
/* 241 */             visit((Element)((List)localObject2).get(i), Boolean.valueOf(true));
/* 242 */             this.writer.print(",");
/*     */           }
/* 244 */           visit((Element)((List)localObject2).get(i), Boolean.valueOf(true));
/* 245 */           this.writer.println(";\n");
/*     */ 
/* 247 */           ((List)localObject1).removeAll((Collection)localObject2);
/*     */         }
/*     */ 
/* 250 */         for (localIterator2 = ((List)localObject1).iterator(); localIterator2.hasNext(); ) { localElement = (Element)localIterator2.next();
/* 251 */           visit(localElement); }
/*     */       } else {
/* 253 */         for (localObject1 = paramTypeElement.getEnclosedElements().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Element)((Iterator)localObject1).next();
/* 254 */           visit((Element)localObject2);
/*     */         }
/*     */       }
/* 257 */       this.indentation -= 1;
/* 258 */       indent();
/* 259 */       this.writer.println("}");
/* 260 */       return this;
/*     */     }
/*     */ 
/*     */     public PrintingElementVisitor visitVariable(VariableElement paramVariableElement, Boolean paramBoolean)
/*     */     {
/* 265 */       ElementKind localElementKind = paramVariableElement.getKind();
/* 266 */       defaultAction(paramVariableElement, paramBoolean);
/*     */ 
/* 268 */       if (localElementKind == ElementKind.ENUM_CONSTANT) {
/* 269 */         this.writer.print(paramVariableElement.getSimpleName());
/*     */       } else {
/* 271 */         this.writer.print(paramVariableElement.asType().toString() + " " + paramVariableElement.getSimpleName());
/* 272 */         Object localObject = paramVariableElement.getConstantValue();
/* 273 */         if (localObject != null) {
/* 274 */           this.writer.print(" = ");
/* 275 */           this.writer.print(this.elementUtils.getConstantExpression(localObject));
/*     */         }
/* 277 */         this.writer.println(";");
/*     */       }
/* 279 */       return this;
/*     */     }
/*     */ 
/*     */     public PrintingElementVisitor visitTypeParameter(TypeParameterElement paramTypeParameterElement, Boolean paramBoolean)
/*     */     {
/* 284 */       this.writer.print(paramTypeParameterElement.getSimpleName());
/* 285 */       return this;
/*     */     }
/*     */ 
/*     */     public PrintingElementVisitor visitPackage(PackageElement paramPackageElement, Boolean paramBoolean)
/*     */     {
/* 291 */       defaultAction(paramPackageElement, Boolean.valueOf(false));
/* 292 */       if (!paramPackageElement.isUnnamed())
/* 293 */         this.writer.println("package " + paramPackageElement.getQualifiedName() + ";");
/*     */       else
/* 295 */         this.writer.println("// Unnamed package");
/* 296 */       return this;
/*     */     }
/*     */ 
/*     */     public void flush() {
/* 300 */       this.writer.flush();
/*     */     }
/*     */ 
/*     */     private void printDocComment(Element paramElement) {
/* 304 */       String str = this.elementUtils.getDocComment(paramElement);
/*     */ 
/* 306 */       if (str != null)
/*     */       {
/* 308 */         StringTokenizer localStringTokenizer = new StringTokenizer(str, "\n\r");
/*     */ 
/* 310 */         indent();
/* 311 */         this.writer.println("/**");
/*     */ 
/* 313 */         while (localStringTokenizer.hasMoreTokens()) {
/* 314 */           indent();
/* 315 */           this.writer.print(" *");
/* 316 */           this.writer.println(localStringTokenizer.nextToken());
/*     */         }
/*     */ 
/* 319 */         indent();
/* 320 */         this.writer.println(" */");
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printModifiers(Element paramElement) {
/* 325 */       ElementKind localElementKind = paramElement.getKind();
/* 326 */       if (localElementKind == ElementKind.PARAMETER) {
/* 327 */         printAnnotationsInline(paramElement);
/*     */       } else {
/* 329 */         printAnnotations(paramElement);
/* 330 */         indent();
/*     */       }
/*     */ 
/* 333 */       if (localElementKind == ElementKind.ENUM_CONSTANT) {
/* 334 */         return;
/*     */       }
/* 336 */       LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 337 */       localLinkedHashSet.addAll(paramElement.getModifiers());
/*     */ 
/* 339 */       switch (PrintingProcessor.1.$SwitchMap$javax$lang$model$element$ElementKind[localElementKind.ordinal()]) {
/*     */       case 3:
/*     */       case 4:
/* 342 */         localLinkedHashSet.remove(Modifier.ABSTRACT);
/* 343 */         break;
/*     */       case 5:
/* 346 */         localLinkedHashSet.remove(Modifier.FINAL);
/* 347 */         localLinkedHashSet.remove(Modifier.ABSTRACT);
/* 348 */         break;
/*     */       case 2:
/*     */       case 6:
/* 352 */         localObject = paramElement.getEnclosingElement();
/* 353 */         if ((localObject != null) && 
/* 354 */           (((Element)localObject)
/* 354 */           .getKind().isInterface())) {
/* 355 */           localLinkedHashSet.remove(Modifier.PUBLIC);
/* 356 */           localLinkedHashSet.remove(Modifier.ABSTRACT);
/* 357 */           localLinkedHashSet.remove(Modifier.STATIC);
/* 358 */           localLinkedHashSet.remove(Modifier.FINAL);
/*     */         }
/*     */ 
/*     */         break;
/*     */       }
/*     */ 
/* 364 */       for (Object localObject = localLinkedHashSet.iterator(); ((Iterator)localObject).hasNext(); ) { Modifier localModifier = (Modifier)((Iterator)localObject).next();
/* 365 */         this.writer.print(localModifier.toString() + " ");
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printFormalTypeParameters(Parameterizable paramParameterizable, boolean paramBoolean)
/*     */     {
/* 371 */       List localList = paramParameterizable.getTypeParameters();
/* 372 */       if (localList.size() > 0) {
/* 373 */         this.writer.print("<");
/*     */ 
/* 375 */         int i = 1;
/* 376 */         for (TypeParameterElement localTypeParameterElement : localList) {
/* 377 */           if (i == 0)
/* 378 */             this.writer.print(", ");
/* 379 */           printAnnotationsInline(localTypeParameterElement);
/* 380 */           this.writer.print(localTypeParameterElement.toString());
/* 381 */           i = 0;
/*     */         }
/*     */ 
/* 384 */         this.writer.print(">");
/* 385 */         if (paramBoolean)
/* 386 */           this.writer.print(" ");
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printAnnotationsInline(Element paramElement) {
/* 391 */       List localList = paramElement.getAnnotationMirrors();
/* 392 */       for (AnnotationMirror localAnnotationMirror : localList) {
/* 393 */         this.writer.print(localAnnotationMirror);
/* 394 */         this.writer.print(" ");
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printAnnotations(Element paramElement) {
/* 399 */       List localList = paramElement.getAnnotationMirrors();
/* 400 */       for (AnnotationMirror localAnnotationMirror : localList) {
/* 401 */         indent();
/* 402 */         this.writer.println(localAnnotationMirror);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printParameters(ExecutableElement paramExecutableElement)
/*     */     {
/* 408 */       List localList = paramExecutableElement.getParameters();
/* 409 */       int i = localList.size();
/*     */       Iterator localIterator;
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 411 */       switch (i) {
/*     */       case 0:
/* 413 */         break;
/*     */       case 1:
/* 416 */         for (localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject1 = (VariableElement)localIterator.next();
/* 417 */           printModifiers((Element)localObject1);
/*     */ 
/* 419 */           if (paramExecutableElement.isVarArgs()) {
/* 420 */             localObject2 = ((VariableElement)localObject1).asType();
/* 421 */             if (((TypeMirror)localObject2).getKind() != TypeKind.ARRAY)
/* 422 */               throw new AssertionError("Var-args parameter is not an array type: " + localObject2);
/* 423 */             this.writer.print(((ArrayType)ArrayType.class.cast(localObject2)).getComponentType());
/* 424 */             this.writer.print("...");
/*     */           } else {
/* 426 */             this.writer.print(((VariableElement)localObject1).asType());
/* 427 */           }this.writer.print(" " + ((VariableElement)localObject1).getSimpleName());
/*     */         }
/* 429 */         break;
/*     */       default:
/* 433 */         int j = 1;
/* 434 */         for (localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (VariableElement)((Iterator)localObject1).next();
/* 435 */           if (j == 2) {
/* 436 */             this.indentation += 1;
/*     */           }
/* 438 */           if (j > 1) {
/* 439 */             indent();
/*     */           }
/* 441 */           printModifiers((Element)localObject2);
/*     */ 
/* 443 */           if ((j == i) && (paramExecutableElement.isVarArgs())) {
/* 444 */             TypeMirror localTypeMirror = ((VariableElement)localObject2).asType();
/* 445 */             if (localTypeMirror.getKind() != TypeKind.ARRAY)
/* 446 */               throw new AssertionError("Var-args parameter is not an array type: " + localTypeMirror);
/* 447 */             this.writer.print(((ArrayType)ArrayType.class.cast(localTypeMirror)).getComponentType());
/*     */ 
/* 449 */             this.writer.print("...");
/*     */           } else {
/* 451 */             this.writer.print(((VariableElement)localObject2).asType());
/* 452 */           }this.writer.print(" " + ((VariableElement)localObject2).getSimpleName());
/*     */ 
/* 454 */           if (j < i) {
/* 455 */             this.writer.println(",");
/*     */           }
/* 457 */           j++;
/*     */         }
/*     */ 
/* 460 */         if (localList.size() >= 2)
/* 461 */           this.indentation -= 1;
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printInterfaces(TypeElement paramTypeElement)
/*     */     {
/* 468 */       ElementKind localElementKind = paramTypeElement.getKind();
/*     */       int i;
/* 470 */       if (localElementKind != ElementKind.ANNOTATION_TYPE) {
/* 471 */         List localList = paramTypeElement.getInterfaces();
/* 472 */         if (localList.size() > 0) {
/* 473 */           this.writer.print(localElementKind.isClass() ? " implements" : " extends");
/*     */ 
/* 475 */           i = 1;
/* 476 */           for (TypeMirror localTypeMirror : localList) {
/* 477 */             if (i == 0)
/* 478 */               this.writer.print(",");
/* 479 */             this.writer.print(" ");
/* 480 */             this.writer.print(localTypeMirror.toString());
/* 481 */             i = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void printThrows(ExecutableElement paramExecutableElement) {
/* 488 */       List localList = paramExecutableElement.getThrownTypes();
/* 489 */       int i = localList.size();
/* 490 */       if (i != 0) {
/* 491 */         this.writer.print(" throws");
/*     */ 
/* 493 */         int j = 1;
/* 494 */         for (TypeMirror localTypeMirror : localList) {
/* 495 */           if (j == 1) {
/* 496 */             this.writer.print(" ");
/*     */           }
/* 498 */           if (j == 2) {
/* 499 */             this.indentation += 1;
/*     */           }
/* 501 */           if (j >= 2) {
/* 502 */             indent();
/*     */           }
/* 504 */           this.writer.print(localTypeMirror);
/*     */ 
/* 506 */           if (j != i) {
/* 507 */             this.writer.println(", ");
/*     */           }
/* 509 */           j++;
/*     */         }
/*     */ 
/* 512 */         if (i >= 2)
/* 513 */           this.indentation -= 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void indent()
/*     */     {
/* 532 */       int i = this.indentation;
/* 533 */       if (i < 0)
/* 534 */         return;
/* 535 */       int j = spaces.length - 1;
/*     */ 
/* 537 */       while (i > j) {
/* 538 */         this.writer.print(spaces[j]);
/* 539 */         i -= j;
/*     */       }
/* 541 */       this.writer.print(spaces[i]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.PrintingProcessor
 * JD-Core Version:    0.6.2
 */