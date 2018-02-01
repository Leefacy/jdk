/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class MethodGenClone24 extends AttributeGen
/*     */ {
/*     */   protected void abstractMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  81 */     this.symbolTable = paramHashtable;
/*  82 */     this.m = paramMethodEntry;
/*  83 */     this.stream = paramPrintWriter;
/*  84 */     if (paramMethodEntry.comment() != null)
/*  85 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/*  86 */     paramPrintWriter.print("  ");
/*  87 */     paramPrintWriter.print("public abstract ");
/*  88 */     writeMethodSignature();
/*  89 */     paramPrintWriter.println(";");
/*  90 */     paramPrintWriter.println();
/*     */   }
/*     */ 
/*     */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  98 */     this.symbolTable = paramHashtable;
/*  99 */     this.m = paramMethodEntry;
/* 100 */     this.stream = paramPrintWriter;
/* 101 */     if (paramMethodEntry.comment() != null)
/* 102 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/* 103 */     paramPrintWriter.print("  ");
/* 104 */     writeMethodSignature();
/* 105 */     paramPrintWriter.println(";");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.MethodGenClone24
 * JD-Core Version:    0.6.2
 */