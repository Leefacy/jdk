/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AttributeException;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class BasicWriter
/*     */ {
/* 121 */   private String[] spaces = new String[80];
/*     */   private LineWriter lineWriter;
/*     */   private PrintWriter out;
/*     */   protected Messages messages;
/*     */ 
/*     */   protected BasicWriter(Context paramContext)
/*     */   {
/*  45 */     this.lineWriter = LineWriter.instance(paramContext);
/*  46 */     this.out = ((PrintWriter)paramContext.get(PrintWriter.class));
/*  47 */     this.messages = ((Messages)paramContext.get(Messages.class));
/*  48 */     if (this.messages == null)
/*  49 */       throw new AssertionError();
/*     */   }
/*     */ 
/*     */   protected void print(String paramString) {
/*  53 */     this.lineWriter.print(paramString);
/*     */   }
/*     */ 
/*     */   protected void print(Object paramObject) {
/*  57 */     this.lineWriter.print(paramObject == null ? null : paramObject.toString());
/*     */   }
/*     */ 
/*     */   protected void println() {
/*  61 */     this.lineWriter.println();
/*     */   }
/*     */ 
/*     */   protected void println(String paramString) {
/*  65 */     this.lineWriter.print(paramString);
/*  66 */     this.lineWriter.println();
/*     */   }
/*     */ 
/*     */   protected void println(Object paramObject) {
/*  70 */     this.lineWriter.print(paramObject == null ? null : paramObject.toString());
/*  71 */     this.lineWriter.println();
/*     */   }
/*     */ 
/*     */   protected void indent(int paramInt) {
/*  75 */     this.lineWriter.indent(paramInt);
/*     */   }
/*     */ 
/*     */   protected void tab() {
/*  79 */     this.lineWriter.tab();
/*     */   }
/*     */ 
/*     */   protected void setPendingNewline(boolean paramBoolean) {
/*  83 */     this.lineWriter.pendingNewline = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected String report(AttributeException paramAttributeException) {
/*  87 */     this.out.println("Error: " + paramAttributeException.getMessage());
/*  88 */     return "???";
/*     */   }
/*     */ 
/*     */   protected String report(ConstantPoolException paramConstantPoolException) {
/*  92 */     this.out.println("Error: " + paramConstantPoolException.getMessage());
/*  93 */     return "???";
/*     */   }
/*     */ 
/*     */   protected String report(DescriptorException paramDescriptorException) {
/*  97 */     this.out.println("Error: " + paramDescriptorException.getMessage());
/*  98 */     return "???";
/*     */   }
/*     */ 
/*     */   protected String report(String paramString) {
/* 102 */     this.out.println("Error: " + paramString);
/* 103 */     return "???";
/*     */   }
/*     */ 
/*     */   protected String space(int paramInt) {
/* 107 */     if ((paramInt < this.spaces.length) && (this.spaces[paramInt] != null)) {
/* 108 */       return this.spaces[paramInt];
/*     */     }
/* 110 */     StringBuilder localStringBuilder = new StringBuilder();
/* 111 */     for (int i = 0; i < paramInt; i++) {
/* 112 */       localStringBuilder.append(" ");
/*     */     }
/* 114 */     String str = localStringBuilder.toString();
/* 115 */     if (paramInt < this.spaces.length) {
/* 116 */       this.spaces[paramInt] = str;
/*     */     }
/* 118 */     return str; } 
/*     */   private static class LineWriter { private final PrintWriter out;
/*     */     private final StringBuilder buffer;
/*     */     private int indentCount;
/*     */     private final int indentWidth;
/*     */     private final int tabColumn;
/*     */     private boolean pendingNewline;
/*     */     private int pendingSpaces;
/*     */ 
/* 129 */     static LineWriter instance(Context paramContext) { LineWriter localLineWriter = (LineWriter)paramContext.get(LineWriter.class);
/* 130 */       if (localLineWriter == null)
/* 131 */         localLineWriter = new LineWriter(paramContext);
/* 132 */       return localLineWriter; }
/*     */ 
/*     */     protected LineWriter(Context paramContext)
/*     */     {
/* 136 */       paramContext.put(LineWriter.class, this);
/* 137 */       Options localOptions = Options.instance(paramContext);
/* 138 */       this.indentWidth = localOptions.indentWidth;
/* 139 */       this.tabColumn = localOptions.tabColumn;
/* 140 */       this.out = ((PrintWriter)paramContext.get(PrintWriter.class));
/* 141 */       this.buffer = new StringBuilder();
/*     */     }
/*     */ 
/*     */     protected void print(String paramString) {
/* 145 */       if (this.pendingNewline) {
/* 146 */         println();
/* 147 */         this.pendingNewline = false;
/*     */       }
/* 149 */       if (paramString == null)
/* 150 */         paramString = "null";
/* 151 */       for (int i = 0; i < paramString.length(); i++) {
/* 152 */         char c = paramString.charAt(i);
/* 153 */         switch (c) {
/*     */         case ' ':
/* 155 */           this.pendingSpaces += 1;
/* 156 */           break;
/*     */         case '\n':
/* 159 */           println();
/* 160 */           break;
/*     */         default:
/* 163 */           if (this.buffer.length() == 0)
/* 164 */             indent();
/* 165 */           if (this.pendingSpaces > 0) {
/* 166 */             for (int j = 0; j < this.pendingSpaces; j++)
/* 167 */               this.buffer.append(' ');
/* 168 */             this.pendingSpaces = 0;
/*     */           }
/* 170 */           this.buffer.append(c);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void println()
/*     */     {
/* 178 */       this.pendingSpaces = 0;
/* 179 */       this.out.println(this.buffer);
/* 180 */       this.buffer.setLength(0);
/*     */     }
/*     */ 
/*     */     protected void indent(int paramInt) {
/* 184 */       this.indentCount += paramInt;
/*     */     }
/*     */ 
/*     */     protected void tab() {
/* 188 */       int i = this.indentCount * this.indentWidth + this.tabColumn;
/* 189 */       this.pendingSpaces += (i <= this.buffer.length() ? 1 : i - this.buffer.length());
/*     */     }
/*     */ 
/*     */     private void indent() {
/* 193 */       this.pendingSpaces += this.indentCount * this.indentWidth;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.BasicWriter
 * JD-Core Version:    0.6.2
 */