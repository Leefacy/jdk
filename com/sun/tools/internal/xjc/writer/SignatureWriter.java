/*     */ package com.sun.tools.internal.xjc.writer;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class SignatureWriter
/*     */ {
/*     */   private final Collection<? extends ClassOutline> classes;
/*  73 */   private final Map<JDefinedClass, ClassOutline> classSet = new HashMap();
/*     */   private final Writer out;
/*  76 */   private int indent = 0;
/*     */ 
/*     */   public static void write(Outline model, Writer out)
/*     */     throws IOException
/*     */   {
/*  59 */     new SignatureWriter(model, out).dump();
/*     */   }
/*     */ 
/*     */   private SignatureWriter(Outline model, Writer out) {
/*  63 */     this.out = out;
/*  64 */     this.classes = model.getClasses();
/*     */ 
/*  66 */     for (ClassOutline ci : this.classes)
/*  67 */       this.classSet.put(ci.ref, ci);
/*     */   }
/*     */ 
/*     */   private void printIndent()
/*     */     throws IOException
/*     */   {
/*  78 */     for (int i = 0; i < this.indent; i++)
/*  79 */       this.out.write("  "); 
/*     */   }
/*     */ 
/*  82 */   private void println(String s) throws IOException { printIndent();
/*  83 */     this.out.write(s);
/*  84 */     this.out.write(10);
/*     */   }
/*     */ 
/*     */   private void dump()
/*     */     throws IOException
/*     */   {
/*  90 */     Set packages = new TreeSet(new Comparator() {
/*     */       public int compare(JPackage lhs, JPackage rhs) {
/*  92 */         return lhs.name().compareTo(rhs.name());
/*     */       }
/*     */     });
/*  95 */     for (ClassOutline ci : this.classes) {
/*  96 */       packages.add(ci._package()._package());
/*     */     }
/*  98 */     for (JPackage pkg : packages) {
/*  99 */       dump(pkg);
/*     */     }
/* 101 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   private void dump(JPackage pkg) throws IOException {
/* 105 */     println("package " + pkg.name() + " {");
/* 106 */     this.indent += 1;
/* 107 */     dumpChildren(pkg);
/* 108 */     this.indent -= 1;
/* 109 */     println("}");
/*     */   }
/*     */ 
/*     */   private void dumpChildren(JClassContainer cont) throws IOException {
/* 113 */     Iterator itr = cont.classes();
/* 114 */     while (itr.hasNext()) {
/* 115 */       JDefinedClass cls = (JDefinedClass)itr.next();
/* 116 */       ClassOutline ci = (ClassOutline)this.classSet.get(cls);
/* 117 */       if (ci != null)
/* 118 */         dump(ci);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void dump(ClassOutline ci) throws IOException {
/* 123 */     JDefinedClass cls = ci.implClass;
/*     */ 
/* 125 */     StringBuilder buf = new StringBuilder();
/* 126 */     buf.append("interface ");
/* 127 */     buf.append(cls.name());
/*     */ 
/* 129 */     boolean first = true;
/* 130 */     Iterator itr = cls._implements();
/* 131 */     while (itr.hasNext()) {
/* 132 */       if (first) {
/* 133 */         buf.append(" extends ");
/* 134 */         first = false;
/*     */       } else {
/* 136 */         buf.append(", ");
/*     */       }
/* 138 */       buf.append(printName((JClass)itr.next()));
/*     */     }
/* 140 */     buf.append(" {");
/* 141 */     println(buf.toString());
/* 142 */     this.indent += 1;
/*     */ 
/* 145 */     for (FieldOutline fo : ci.getDeclaredFields()) {
/* 146 */       String type = printName(fo.getRawType());
/* 147 */       println(type + ' ' + fo.getPropertyInfo().getName(true) + ';');
/*     */     }
/*     */ 
/* 150 */     dumpChildren(cls);
/*     */ 
/* 152 */     this.indent -= 1;
/* 153 */     println("}");
/*     */   }
/*     */ 
/*     */   private String printName(JType t)
/*     */   {
/* 158 */     String name = t.fullName();
/* 159 */     if (name.startsWith("java.lang."))
/* 160 */       name = name.substring(10);
/* 161 */     return name;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.writer.SignatureWriter
 * JD-Core Version:    0.6.2
 */