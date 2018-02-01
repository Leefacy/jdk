/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.ConstEntry;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.ModuleEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ConstGen
/*     */   implements com.sun.tools.corba.se.idl.ConstGen
/*     */ {
/* 185 */   protected Hashtable symbolTable = null;
/* 186 */   protected ConstEntry c = null;
/* 187 */   protected PrintWriter stream = null;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, ConstEntry paramConstEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  72 */     this.symbolTable = paramHashtable;
/*  73 */     this.c = paramConstEntry;
/*  74 */     this.stream = paramPrintWriter;
/*  75 */     init();
/*     */ 
/*  77 */     if ((paramConstEntry.container() instanceof ModuleEntry))
/*  78 */       generateConst();
/*  79 */     else if (this.stream != null)
/*  80 */       writeConstExpr();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void generateConst()
/*     */   {
/*  95 */     openStream();
/*  96 */     if (this.stream == null)
/*  97 */       return;
/*  98 */     writeHeading();
/*  99 */     writeBody();
/* 100 */     writeClosing();
/* 101 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 110 */     this.stream = Util.stream(this.c, ".java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 118 */     Util.writePackage(this.stream, this.c);
/* 119 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 120 */     this.stream.println("public interface " + this.c.name());
/*     */ 
/* 123 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 131 */     writeConstExpr();
/*     */   }
/*     */ 
/*     */   protected void writeConstExpr()
/*     */   {
/* 139 */     if (this.c.comment() != null)
/* 140 */       this.c.comment().generate("  ", this.stream);
/* 141 */     if ((this.c.container() instanceof ModuleEntry))
/*     */     {
/* 143 */       this.stream.print("  public static final " + Util.javaName(this.c.type()) + " value = ");
/*     */     }
/* 145 */     else this.stream.print("  public static final " + Util.javaName(this.c.type()) + ' ' + this.c.name() + " = ");
/*     */ 
/* 147 */     writeConstValue(this.c.type());
/*     */   }
/*     */ 
/*     */   private void writeConstValue(SymtabEntry paramSymtabEntry)
/*     */   {
/* 155 */     if ((paramSymtabEntry instanceof PrimitiveEntry)) {
/* 156 */       this.stream.println('(' + Util.javaName(paramSymtabEntry) + ")(" + Util.parseExpression(this.c.value()) + ");");
/* 157 */     } else if ((paramSymtabEntry instanceof StringEntry)) {
/* 158 */       this.stream.println(Util.parseExpression(this.c.value()) + ';');
/* 159 */     } else if ((paramSymtabEntry instanceof TypedefEntry))
/*     */     {
/* 161 */       while ((paramSymtabEntry instanceof TypedefEntry))
/* 162 */         paramSymtabEntry = paramSymtabEntry.type();
/* 163 */       writeConstValue(paramSymtabEntry);
/*     */     }
/*     */     else {
/* 166 */       this.stream.println(Util.parseExpression(this.c.value()) + ';');
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 174 */     this.stream.println("}");
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 182 */     this.stream.close();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ConstGen
 * JD-Core Version:    0.6.2
 */