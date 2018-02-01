/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class LocalVmManager
/*     */ {
/*     */   private String userName;
/*     */   private File tmpdir;
/*     */   private Pattern userPattern;
/*     */   private Matcher userMatcher;
/*     */   private FilenameFilter userFilter;
/*     */   private Pattern filePattern;
/*     */   private Matcher fileMatcher;
/*     */   private FilenameFilter fileFilter;
/*     */   private Pattern tmpFilePattern;
/*     */   private Matcher tmpFileMatcher;
/*     */   private FilenameFilter tmpFileFilter;
/*     */ 
/*     */   public LocalVmManager()
/*     */   {
/*  66 */     this(null);
/*     */   }
/*     */ 
/*     */   public LocalVmManager(String paramString)
/*     */   {
/*  78 */     this.userName = paramString;
/*     */ 
/*  80 */     if (this.userName == null) {
/*  81 */       this.tmpdir = new File(PerfDataFile.getTempDirectory());
/*  82 */       this.userPattern = Pattern.compile("hsperfdata_\\S*");
/*  83 */       this.userMatcher = this.userPattern.matcher("");
/*     */ 
/*  85 */       this.userFilter = new FilenameFilter() {
/*     */         public boolean accept(File paramAnonymousFile, String paramAnonymousString) {
/*  87 */           LocalVmManager.this.userMatcher.reset(paramAnonymousString);
/*  88 */           return LocalVmManager.this.userMatcher.lookingAt();
/*     */         } } ;
/*     */     }
/*     */     else {
/*  92 */       this.tmpdir = new File(PerfDataFile.getTempDirectory(this.userName));
/*     */     }
/*     */ 
/*  95 */     this.filePattern = Pattern.compile("^[0-9]+$");
/*  96 */     this.fileMatcher = this.filePattern.matcher("");
/*     */ 
/*  98 */     this.fileFilter = new FilenameFilter() {
/*     */       public boolean accept(File paramAnonymousFile, String paramAnonymousString) {
/* 100 */         LocalVmManager.this.fileMatcher.reset(paramAnonymousString);
/* 101 */         return LocalVmManager.this.fileMatcher.matches();
/*     */       }
/*     */     };
/* 105 */     this.tmpFilePattern = Pattern.compile("^hsperfdata_[0-9]+(_[1-2]+)?$");
/* 106 */     this.tmpFileMatcher = this.tmpFilePattern.matcher("");
/*     */ 
/* 108 */     this.tmpFileFilter = new FilenameFilter() {
/*     */       public boolean accept(File paramAnonymousFile, String paramAnonymousString) {
/* 110 */         LocalVmManager.this.tmpFileMatcher.reset(paramAnonymousString);
/* 111 */         return LocalVmManager.this.tmpFileMatcher.matches();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public synchronized Set<Integer> activeVms()
/*     */   {
/* 135 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 137 */     if (!this.tmpdir.isDirectory())
/* 138 */       return localHashSet;
/*     */     int i;
/* 141 */     if (this.userName == null)
/*     */     {
/* 146 */       arrayOfFile1 = this.tmpdir.listFiles(this.userFilter);
/*     */ 
/* 148 */       for (i = 0; i < arrayOfFile1.length; i++) {
/* 149 */         if (arrayOfFile1[i].isDirectory())
/*     */         {
/* 154 */           File[] arrayOfFile2 = arrayOfFile1[i].listFiles(this.fileFilter);
/*     */ 
/* 156 */           if (arrayOfFile2 != null) {
/* 157 */             for (int j = 0; j < arrayOfFile2.length; j++) {
/* 158 */               if ((arrayOfFile2[j].isFile()) && (arrayOfFile2[j].canRead())) {
/* 159 */                 localHashSet.add(new Integer(
/* 160 */                   PerfDataFile.getLocalVmId(arrayOfFile2[j])));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 173 */       arrayOfFile1 = this.tmpdir.listFiles(this.fileFilter);
/*     */ 
/* 175 */       if (arrayOfFile1 != null) {
/* 176 */         for (i = 0; i < arrayOfFile1.length; i++) {
/* 177 */           if ((arrayOfFile1[i].isFile()) && (arrayOfFile1[i].canRead())) {
/* 178 */             localHashSet.add(new Integer(
/* 179 */               PerfDataFile.getLocalVmId(arrayOfFile1[i])));
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 186 */     File[] arrayOfFile1 = this.tmpdir.listFiles(this.tmpFileFilter);
/* 187 */     if (arrayOfFile1 != null) {
/* 188 */       for (i = 0; i < arrayOfFile1.length; i++) {
/* 189 */         if ((arrayOfFile1[i].isFile()) && (arrayOfFile1[i].canRead())) {
/* 190 */           localHashSet.add(new Integer(
/* 191 */             PerfDataFile.getLocalVmId(arrayOfFile1[i])));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 196 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.protocol.local.LocalVmManager
 * JD-Core Version:    0.6.2
 */