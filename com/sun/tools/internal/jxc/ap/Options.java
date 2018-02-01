/*     */ package com.sun.tools.internal.jxc.ap;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Options
/*     */ {
/*     */   public static final String DISABLE_XML_SECURITY = "-disableXmlSecurity";
/*  45 */   public String classpath = System.getenv("CLASSPATH");
/*     */ 
/*  47 */   public File targetDir = null;
/*     */ 
/*  49 */   public File episodeFile = null;
/*     */ 
/*  51 */   private boolean disableXmlSecurity = false;
/*     */ 
/*  54 */   public String encoding = null;
/*     */ 
/*  56 */   public final List<String> arguments = new ArrayList();
/*     */ 
/*     */   public void parseArguments(String[] args) throws BadCommandLineException {
/*  59 */     for (int i = 0; i < args.length; i++)
/*  60 */       if (args[i].charAt(0) == '-') {
/*  61 */         int j = parseArgument(args, i);
/*  62 */         if (j == 0)
/*     */         {
/*  64 */           throw new BadCommandLineException(Messages.UNRECOGNIZED_PARAMETER
/*  64 */             .format(new Object[] { args[i] }));
/*     */         }
/*  65 */         i += j;
/*     */       } else {
/*  67 */         this.arguments.add(args[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   private int parseArgument(String[] args, int i) throws BadCommandLineException
/*     */   {
/*  73 */     if (args[i].equals("-d")) {
/*  74 */       if (i == args.length - 1)
/*     */       {
/*  76 */         throw new BadCommandLineException(Messages.OPERAND_MISSING
/*  76 */           .format(new Object[] { args[i] }));
/*     */       }
/*  77 */       this.targetDir = new File(args[(++i)]);
/*  78 */       if (!this.targetDir.exists())
/*     */       {
/*  80 */         throw new BadCommandLineException(Messages.NON_EXISTENT_FILE
/*  80 */           .format(new Object[] { this.targetDir }));
/*     */       }
/*  81 */       return 1;
/*     */     }
/*     */ 
/*  84 */     if (args[i].equals("-episode")) {
/*  85 */       if (i == args.length - 1)
/*     */       {
/*  87 */         throw new BadCommandLineException(Messages.OPERAND_MISSING
/*  87 */           .format(new Object[] { args[i] }));
/*     */       }
/*  88 */       this.episodeFile = new File(args[(++i)]);
/*  89 */       return 1;
/*     */     }
/*     */ 
/*  92 */     if (args[i].equals("-disableXmlSecurity")) {
/*  93 */       if (i == args.length - 1)
/*     */       {
/*  95 */         throw new BadCommandLineException(Messages.OPERAND_MISSING
/*  95 */           .format(new Object[] { args[i] }));
/*     */       }
/*  96 */       this.disableXmlSecurity = true;
/*  97 */       return 1;
/*     */     }
/*     */ 
/* 100 */     if (args[i].equals("-encoding")) {
/* 101 */       if (i == args.length - 1)
/*     */       {
/* 103 */         throw new BadCommandLineException(Messages.OPERAND_MISSING
/* 103 */           .format(new Object[] { args[i] }));
/*     */       }
/* 104 */       this.encoding = args[(++i)];
/* 105 */       return 1;
/*     */     }
/*     */ 
/* 108 */     if ((args[i].equals("-cp")) || (args[i].equals("-classpath"))) {
/* 109 */       if (i == args.length - 1)
/*     */       {
/* 111 */         throw new BadCommandLineException(Messages.OPERAND_MISSING
/* 111 */           .format(new Object[] { args[i] }));
/*     */       }
/* 112 */       this.classpath = args[(++i)];
/*     */ 
/* 114 */       return 1;
/*     */     }
/*     */ 
/* 117 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isDisableXmlSecurity()
/*     */   {
/* 125 */     return this.disableXmlSecurity;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.Options
 * JD-Core Version:    0.6.2
 */