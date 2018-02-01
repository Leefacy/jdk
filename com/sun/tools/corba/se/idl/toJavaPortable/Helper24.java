/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.RepositoryID;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Helper24 extends Helper
/*     */ {
/*     */   protected void writeHeading()
/*     */   {
/*  77 */     Util.writePackage(this.stream, this.entry, (short)2);
/*  78 */     Util.writeProlog(this.stream, this.stream.name());
/*     */ 
/*  81 */     if (this.entry.comment() != null) {
/*  82 */       this.entry.comment().generate("", this.stream);
/*     */     }
/*  84 */     if ((this.entry instanceof ValueBoxEntry)) {
/*  85 */       this.stream.print("public final class " + this.helperClass);
/*  86 */       this.stream.println(" implements org.omg.CORBA.portable.BoxedValueHelper");
/*     */     }
/*     */     else {
/*  89 */       this.stream.println("abstract public class " + this.helperClass);
/*  90 */     }this.stream.println('{');
/*     */   }
/*     */ 
/*     */   protected void writeInstVars()
/*     */   {
/* 100 */     this.stream.println("  private static String  _id = \"" + Util.stripLeadingUnderscoresFromID(this.entry.repositoryID().ID()) + "\";");
/* 101 */     if ((this.entry instanceof ValueEntry))
/*     */     {
/* 103 */       this.stream.println();
/* 104 */       if ((this.entry instanceof ValueBoxEntry)) {
/* 105 */         this.stream.println("  private static " + this.helperClass + " _instance = new " + this.helperClass + " ();");
/* 106 */         this.stream.println();
/*     */       }
/*     */     }
/* 109 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeValueHelperInterface()
/*     */   {
/* 120 */     if ((this.entry instanceof ValueBoxEntry))
/* 121 */       writeGetID();
/* 122 */     else if ((this.entry instanceof ValueEntry))
/* 123 */       writeHelperFactories();
/*     */   }
/*     */ 
/*     */   protected void writeHelperFactories()
/*     */   {
/* 132 */     Vector localVector = ((ValueEntry)this.entry).initializers();
/* 133 */     if (localVector != null)
/*     */     {
/* 135 */       this.stream.println();
/* 136 */       for (int i = 0; i < localVector.size(); i++)
/*     */       {
/* 138 */         MethodEntry localMethodEntry = (MethodEntry)localVector.elementAt(i);
/* 139 */         localMethodEntry.valueMethod(true);
/* 140 */         ((MethodGen24)localMethodEntry.generator()).helperFactoryMethod(this.symbolTable, localMethodEntry, this.entry, this.stream);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 151 */     if ((this.entry instanceof ValueBoxEntry)) {
/* 152 */       this.stream.println("  public " + this.helperClass + "()");
/* 153 */       this.stream.println("  {");
/* 154 */       this.stream.println("  }");
/* 155 */       this.stream.println();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Helper24
 * JD-Core Version:    0.6.2
 */