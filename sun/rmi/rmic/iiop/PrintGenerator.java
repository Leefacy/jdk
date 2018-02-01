/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import sun.rmi.rmic.Generator;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class PrintGenerator
/*     */   implements Generator, Constants
/*     */ {
/*     */   private static final int JAVA = 0;
/*     */   private static final int IDL = 1;
/*     */   private static final int BOTH = 2;
/*     */   private int whatToPrint;
/*  55 */   private boolean global = false;
/*  56 */   private boolean qualified = false;
/*  57 */   private boolean trace = false;
/*  58 */   private boolean valueMethods = false;
/*     */   private IndentingWriter out;
/*     */ 
/*     */   public PrintGenerator()
/*     */   {
/*  66 */     OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(System.out);
/*  67 */     this.out = new IndentingWriter(localOutputStreamWriter);
/*     */   }
/*     */ 
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain)
/*     */   {
/*  77 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  78 */       if (paramArrayOfString[i] != null) {
/*  79 */         String str = paramArrayOfString[i].toLowerCase();
/*  80 */         if (str.equals("-xprint")) {
/*  81 */           this.whatToPrint = 0;
/*  82 */           paramArrayOfString[i] = null;
/*  83 */           if (i + 1 < paramArrayOfString.length)
/*  84 */             if (paramArrayOfString[(i + 1)].equalsIgnoreCase("idl")) {
/*  85 */               paramArrayOfString[(++i)] = null;
/*  86 */               this.whatToPrint = 1;
/*  87 */             } else if (paramArrayOfString[(i + 1)].equalsIgnoreCase("both")) {
/*  88 */               paramArrayOfString[(++i)] = null;
/*  89 */               this.whatToPrint = 2;
/*     */             }
/*     */         }
/*  92 */         else if (str.equals("-xglobal")) {
/*  93 */           this.global = true;
/*  94 */           paramArrayOfString[i] = null;
/*  95 */         } else if (str.equals("-xqualified")) {
/*  96 */           this.qualified = true;
/*  97 */           paramArrayOfString[i] = null;
/*  98 */         } else if (str.equals("-xtrace")) {
/*  99 */           this.trace = true;
/* 100 */           paramArrayOfString[i] = null;
/* 101 */         } else if (str.equals("-xvaluemethods")) {
/* 102 */           this.valueMethods = true;
/* 103 */           paramArrayOfString[i] = null;
/*     */         }
/*     */       }
/*     */     }
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   public void generate(sun.rmi.rmic.BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile)
/*     */   {
/* 123 */     BatchEnvironment localBatchEnvironment = (BatchEnvironment)paramBatchEnvironment;
/* 124 */     ContextStack localContextStack = new ContextStack(localBatchEnvironment);
/* 125 */     localContextStack.setTrace(this.trace);
/*     */ 
/* 127 */     if (this.valueMethods) {
/* 128 */       localBatchEnvironment.setParseNonConforming(true);
/*     */     }
/*     */ 
/* 133 */     CompoundType localCompoundType = CompoundType.forCompound(paramClassDefinition, localContextStack);
/*     */ 
/* 135 */     if (localCompoundType != null)
/*     */     {
/*     */       try
/*     */       {
/* 141 */         Type[] arrayOfType = localCompoundType.collectMatching(33554432);
/*     */ 
/* 143 */         for (int i = 0; i < arrayOfType.length; i++)
/*     */         {
/* 145 */           this.out.pln("\n-----------------------------------------------------------\n");
/*     */ 
/* 147 */           Type localType = arrayOfType[i];
/*     */ 
/* 149 */           switch (this.whatToPrint) { case 0:
/* 150 */             localType.println(this.out, this.qualified, false, false);
/* 151 */             break;
/*     */           case 1:
/* 153 */             localType.println(this.out, this.qualified, true, this.global);
/* 154 */             break;
/*     */           case 2:
/* 156 */             localType.println(this.out, this.qualified, false, false);
/* 157 */             localType.println(this.out, this.qualified, true, this.global);
/* 158 */             break;
/*     */           default:
/* 160 */             throw new CompilerError("Unknown type!");
/*     */           }
/*     */         }
/*     */ 
/* 164 */         this.out.flush();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 167 */         throw new CompilerError("PrintGenerator caught " + localIOException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.PrintGenerator
 * JD-Core Version:    0.6.2
 */