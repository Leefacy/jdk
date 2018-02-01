/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ValueFactory
/*     */   implements AuxGen
/*     */ {
/*     */   protected Hashtable symbolTable;
/*     */   protected SymtabEntry entry;
/*     */   protected GenFileStream stream;
/*     */   protected String factoryClass;
/*     */   protected String factoryType;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry)
/*     */   {
/*  76 */     this.symbolTable = paramHashtable;
/*  77 */     this.entry = paramSymtabEntry;
/*  78 */     init();
/*  79 */     if (hasFactoryMethods()) {
/*  80 */       openStream();
/*  81 */       if (this.stream == null)
/*  82 */         return;
/*  83 */       writeHeading();
/*  84 */       writeBody();
/*  85 */       writeClosing();
/*  86 */       closeStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*  95 */     this.factoryClass = (this.entry.name() + "ValueFactory");
/*  96 */     this.factoryType = Util.javaName(this.entry);
/*     */   }
/*     */ 
/*     */   protected boolean hasFactoryMethods()
/*     */   {
/* 104 */     Vector localVector = ((ValueEntry)this.entry).initializers();
/* 105 */     if ((localVector != null) && (localVector.size() > 0)) {
/* 106 */       return true;
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 116 */     this.stream = Util.stream(this.entry, "ValueFactory.java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 125 */     Util.writePackage(this.stream, this.entry, (short)0);
/* 126 */     Util.writeProlog(this.stream, this.stream.name());
/* 127 */     if (this.entry.comment() != null)
/* 128 */       this.entry.comment().generate("", this.stream);
/* 129 */     this.stream.println("public interface " + this.factoryClass + " extends org.omg.CORBA.portable.ValueFactory");
/* 130 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 138 */     Vector localVector = ((ValueEntry)this.entry).initializers();
/* 139 */     if (localVector != null)
/*     */     {
/* 141 */       for (int i = 0; i < localVector.size(); i++)
/*     */       {
/* 143 */         MethodEntry localMethodEntry = (MethodEntry)localVector.elementAt(i);
/* 144 */         localMethodEntry.valueMethod(true);
/* 145 */         ((MethodGen)localMethodEntry.generator()).interfaceMethod(this.symbolTable, localMethodEntry, this.stream);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 155 */     this.stream.println('}');
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 163 */     this.stream.close();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ValueFactory
 * JD-Core Version:    0.6.2
 */