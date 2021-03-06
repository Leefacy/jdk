/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Holder
/*     */   implements AuxGen
/*     */ {
/*     */   protected Hashtable symbolTable;
/*     */   protected SymtabEntry entry;
/*     */   protected GenFileStream stream;
/*     */   protected String holderClass;
/*     */   protected String helperClass;
/*     */   protected String holderType;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry)
/*     */   {
/*  78 */     this.symbolTable = paramHashtable;
/*  79 */     this.entry = paramSymtabEntry;
/*  80 */     init();
/*     */ 
/*  82 */     openStream();
/*  83 */     if (this.stream == null)
/*  84 */       return;
/*  85 */     writeHeading();
/*  86 */     writeBody();
/*  87 */     writeClosing();
/*  88 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*  96 */     this.holderClass = (this.entry.name() + "Holder");
/*  97 */     this.helperClass = Util.helperName(this.entry, true);
/*  98 */     if ((this.entry instanceof ValueBoxEntry))
/*     */     {
/* 100 */       ValueBoxEntry localValueBoxEntry = (ValueBoxEntry)this.entry;
/* 101 */       TypedefEntry localTypedefEntry = ((InterfaceState)localValueBoxEntry.state().elementAt(0)).entry;
/* 102 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/* 103 */       this.holderType = Util.javaName(localSymtabEntry);
/*     */     }
/*     */     else {
/* 106 */       this.holderType = Util.javaName(this.entry);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 114 */     this.stream = Util.stream(this.entry, "Holder.java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 123 */     Util.writePackage(this.stream, this.entry, (short)3);
/* 124 */     Util.writeProlog(this.stream, this.stream.name());
/* 125 */     if (this.entry.comment() != null)
/* 126 */       this.entry.comment().generate("", this.stream);
/* 127 */     this.stream.println("public final class " + this.holderClass + " implements org.omg.CORBA.portable.Streamable");
/* 128 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 136 */     if ((this.entry instanceof ValueBoxEntry))
/* 137 */       this.stream.println("  public " + this.holderType + " value;");
/*     */     else
/* 139 */       Util.writeInitializer("  public ", "value", "", this.entry, this.stream);
/* 140 */     this.stream.println();
/* 141 */     writeCtors();
/* 142 */     writeRead();
/* 143 */     writeWrite();
/* 144 */     writeType();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 152 */     this.stream.println('}');
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 160 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 168 */     this.stream.println("  public " + this.holderClass + " ()");
/* 169 */     this.stream.println("  {");
/* 170 */     this.stream.println("  }");
/* 171 */     this.stream.println();
/* 172 */     this.stream.println("  public " + this.holderClass + " (" + this.holderType + " initialValue)");
/* 173 */     this.stream.println("  {");
/* 174 */     this.stream.println("    value = initialValue;");
/* 175 */     this.stream.println("  }");
/* 176 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeRead()
/*     */   {
/* 184 */     this.stream.println("  public void _read (org.omg.CORBA.portable.InputStream i)");
/* 185 */     this.stream.println("  {");
/* 186 */     if ((this.entry instanceof ValueBoxEntry))
/*     */     {
/* 188 */       TypedefEntry localTypedefEntry = ((InterfaceState)((ValueBoxEntry)this.entry).state().elementAt(0)).entry;
/* 189 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/* 190 */       if ((localSymtabEntry instanceof StringEntry)) {
/* 191 */         this.stream.println("    value = i.read_string ();");
/*     */       }
/* 193 */       else if ((localSymtabEntry instanceof PrimitiveEntry)) {
/* 194 */         this.stream.println("    value = " + this.helperClass + ".read (i).value;");
/*     */       }
/*     */       else
/* 197 */         this.stream.println("    value = " + this.helperClass + ".read (i);");
/*     */     }
/*     */     else {
/* 200 */       this.stream.println("    value = " + this.helperClass + ".read (i);");
/* 201 */     }this.stream.println("  }");
/* 202 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeWrite()
/*     */   {
/* 210 */     this.stream.println("  public void _write (org.omg.CORBA.portable.OutputStream o)");
/* 211 */     this.stream.println("  {");
/* 212 */     if ((this.entry instanceof ValueBoxEntry))
/*     */     {
/* 214 */       TypedefEntry localTypedefEntry = ((InterfaceState)((ValueBoxEntry)this.entry).state().elementAt(0)).entry;
/* 215 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/* 216 */       if ((localSymtabEntry instanceof StringEntry)) {
/* 217 */         this.stream.println("    o.write_string (value);");
/*     */       }
/* 219 */       else if ((localSymtabEntry instanceof PrimitiveEntry))
/*     */       {
/* 221 */         String str = this.entry.name();
/* 222 */         this.stream.println("    " + str + " vb = new " + str + " (value);");
/* 223 */         this.stream.println("    " + this.helperClass + ".write (o, vb);");
/*     */       }
/*     */       else
/*     */       {
/* 227 */         this.stream.println("    " + this.helperClass + ".write (o, value);");
/*     */       }
/*     */     } else {
/* 230 */       this.stream.println("    " + this.helperClass + ".write (o, value);");
/* 231 */     }this.stream.println("  }");
/* 232 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeType()
/*     */   {
/* 240 */     this.stream.println("  public org.omg.CORBA.TypeCode _type ()");
/* 241 */     this.stream.println("  {");
/* 242 */     this.stream.println("    return " + this.helperClass + ".type ();");
/* 243 */     this.stream.println("  }");
/* 244 */     this.stream.println();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Holder
 * JD-Core Version:    0.6.2
 */