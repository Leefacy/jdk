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
/*     */ public class DefaultFactory
/*     */   implements AuxGen
/*     */ {
/*     */   protected Hashtable symbolTable;
/*     */   protected SymtabEntry entry;
/*     */   protected GenFileStream stream;
/*     */   protected String factoryClass;
/*     */   protected String factoryInterface;
/*     */   protected String factoryType;
/*     */   protected String implType;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry)
/*     */   {
/*  76 */     this.symbolTable = paramHashtable;
/*  77 */     this.entry = paramSymtabEntry;
/*  78 */     init();
/*  79 */     openStream();
/*  80 */     if (this.stream == null)
/*  81 */       return;
/*  82 */     writeHeading();
/*  83 */     writeBody();
/*  84 */     writeClosing();
/*  85 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*  93 */     this.factoryClass = (this.entry.name() + "DefaultFactory");
/*  94 */     this.factoryInterface = (this.entry.name() + "ValueFactory");
/*  95 */     this.factoryType = Util.javaName(this.entry);
/*  96 */     this.implType = (this.entry.name() + "Impl");
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
/* 116 */     this.stream = Util.stream(this.entry, "DefaultFactory.java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 125 */     Util.writePackage(this.stream, this.entry, (short)0);
/* 126 */     Util.writeProlog(this.stream, this.stream.name());
/* 127 */     if (this.entry.comment() != null)
/* 128 */       this.entry.comment().generate("", this.stream);
/* 129 */     this.stream.print("public class " + this.factoryClass + " implements ");
/* 130 */     if (hasFactoryMethods())
/* 131 */       this.stream.print(this.factoryInterface);
/*     */     else
/* 133 */       this.stream.print("org.omg.CORBA.portable.ValueFactory");
/* 134 */     this.stream.println(" {");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 142 */     writeFactoryMethods();
/* 143 */     this.stream.println();
/* 144 */     writeReadValue();
/*     */   }
/*     */ 
/*     */   protected void writeFactoryMethods()
/*     */   {
/* 152 */     Vector localVector = ((ValueEntry)this.entry).initializers();
/* 153 */     if (localVector != null)
/*     */     {
/* 155 */       for (int i = 0; i < localVector.size(); i++)
/*     */       {
/* 157 */         MethodEntry localMethodEntry = (MethodEntry)localVector.elementAt(i);
/* 158 */         localMethodEntry.valueMethod(true);
/* 159 */         ((MethodGen24)localMethodEntry.generator()).defaultFactoryMethod(this.symbolTable, localMethodEntry, this.stream);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeReadValue()
/*     */   {
/* 169 */     this.stream.println("  public java.io.Serializable read_value (org.omg.CORBA_2_3.portable.InputStream is)");
/* 170 */     this.stream.println("  {");
/* 171 */     this.stream.println("    return is.read_value(new " + this.implType + " ());");
/* 172 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 180 */     this.stream.println('}');
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 188 */     this.stream.close();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.DefaultFactory
 * JD-Core Version:    0.6.2
 */