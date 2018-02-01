/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.TypeAnnotation;
/*     */ import com.sun.tools.classfile.TypeAnnotation.Position;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TypeAnnotationWriter extends InstructionDetailWriter
/*     */ {
/*     */   private AnnotationWriter annotationWriter;
/*     */   private ClassWriter classWriter;
/*     */   private Map<Integer, List<Note>> pcMap;
/*     */ 
/*     */   static TypeAnnotationWriter instance(Context paramContext)
/*     */   {
/*  62 */     TypeAnnotationWriter localTypeAnnotationWriter = (TypeAnnotationWriter)paramContext.get(TypeAnnotationWriter.class);
/*  63 */     if (localTypeAnnotationWriter == null)
/*  64 */       localTypeAnnotationWriter = new TypeAnnotationWriter(paramContext);
/*  65 */     return localTypeAnnotationWriter;
/*     */   }
/*     */ 
/*     */   protected TypeAnnotationWriter(Context paramContext) {
/*  69 */     super(paramContext);
/*  70 */     paramContext.put(TypeAnnotationWriter.class, this);
/*  71 */     this.annotationWriter = AnnotationWriter.instance(paramContext);
/*  72 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  76 */     Method localMethod = this.classWriter.getMethod();
/*  77 */     this.pcMap = new HashMap();
/*  78 */     check(NoteKind.VISIBLE, (RuntimeVisibleTypeAnnotations_attribute)localMethod.attributes.get("RuntimeVisibleTypeAnnotations"));
/*  79 */     check(NoteKind.INVISIBLE, (RuntimeInvisibleTypeAnnotations_attribute)localMethod.attributes.get("RuntimeInvisibleTypeAnnotations"));
/*     */   }
/*     */ 
/*     */   private void check(NoteKind paramNoteKind, RuntimeTypeAnnotations_attribute paramRuntimeTypeAnnotations_attribute) {
/*  83 */     if (paramRuntimeTypeAnnotations_attribute == null) {
/*  84 */       return;
/*     */     }
/*  86 */     for (TypeAnnotation localTypeAnnotation : paramRuntimeTypeAnnotations_attribute.annotations) {
/*  87 */       TypeAnnotation.Position localPosition = localTypeAnnotation.position;
/*  88 */       Note localNote = null;
/*  89 */       if (localPosition.offset != -1)
/*  90 */         addNote(localPosition.offset, localNote = new Note(paramNoteKind, localTypeAnnotation));
/*  91 */       if (localPosition.lvarOffset != null)
/*  92 */         for (int k = 0; k < localPosition.lvarOffset.length; k++) {
/*  93 */           if (localNote == null)
/*  94 */             localNote = new Note(paramNoteKind, localTypeAnnotation);
/*  95 */           addNote(localPosition.lvarOffset[k], localNote);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addNote(int paramInt, Note paramNote)
/*     */   {
/* 102 */     Object localObject = (List)this.pcMap.get(Integer.valueOf(paramInt));
/* 103 */     if (localObject == null)
/* 104 */       this.pcMap.put(Integer.valueOf(paramInt), localObject = new ArrayList());
/* 105 */     ((List)localObject).add(paramNote);
/*     */   }
/*     */ 
/*     */   void writeDetails(Instruction paramInstruction)
/*     */   {
/* 110 */     String str = space(2);
/* 111 */     int i = paramInstruction.getPC();
/* 112 */     List localList = (List)this.pcMap.get(Integer.valueOf(i));
/* 113 */     if (localList != null)
/* 114 */       for (Note localNote : localList) {
/* 115 */         print(str);
/* 116 */         print("@");
/* 117 */         this.annotationWriter.write(localNote.anno, false, true);
/* 118 */         print(", ");
/* 119 */         println(StringUtils.toLowerCase(localNote.kind.toString()));
/*     */       }
/*     */   }
/*     */ 
/*     */   public static class Note
/*     */   {
/*     */     public final TypeAnnotationWriter.NoteKind kind;
/*     */     public final TypeAnnotation anno;
/*     */ 
/*     */     Note(TypeAnnotationWriter.NoteKind paramNoteKind, TypeAnnotation paramTypeAnnotation)
/*     */     {
/*  54 */       this.kind = paramNoteKind;
/*  55 */       this.anno = paramTypeAnnotation;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum NoteKind
/*     */   {
/*  51 */     VISIBLE, INVISIBLE;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.TypeAnnotationWriter
 * JD-Core Version:    0.6.2
 */