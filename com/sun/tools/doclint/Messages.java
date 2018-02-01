/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ 
/*     */ public class Messages
/*     */ {
/*     */   private final Options options;
/*     */   private final Stats stats;
/*     */   ResourceBundle bundle;
/*     */   Env env;
/*     */ 
/*     */   Messages(Env paramEnv)
/*     */   {
/*  88 */     this.env = paramEnv;
/*  89 */     String str = getClass().getPackage().getName() + ".resources.doclint";
/*  90 */     this.bundle = ResourceBundle.getBundle(str, Locale.ENGLISH);
/*     */ 
/*  92 */     this.stats = new Stats(this.bundle);
/*  93 */     this.options = new Options(this.stats);
/*     */   }
/*     */ 
/*     */   void error(Group paramGroup, DocTree paramDocTree, String paramString, Object[] paramArrayOfObject) {
/*  97 */     report(paramGroup, Diagnostic.Kind.ERROR, paramDocTree, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   void warning(Group paramGroup, DocTree paramDocTree, String paramString, Object[] paramArrayOfObject) {
/* 101 */     report(paramGroup, Diagnostic.Kind.WARNING, paramDocTree, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   void setOptions(String paramString) {
/* 105 */     this.options.setOptions(paramString);
/*     */   }
/*     */ 
/*     */   void setStatsEnabled(boolean paramBoolean) {
/* 109 */     this.stats.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */   void reportStats(PrintWriter paramPrintWriter) {
/* 113 */     this.stats.report(paramPrintWriter);
/*     */   }
/*     */ 
/*     */   protected void report(Group paramGroup, Diagnostic.Kind paramKind, DocTree paramDocTree, String paramString, Object[] paramArrayOfObject) {
/* 117 */     if (this.options.isEnabled(paramGroup, this.env.currAccess)) {
/* 118 */       String str = paramString == null ? (String)paramArrayOfObject[0] : localize(paramString, paramArrayOfObject);
/* 119 */       this.env.trees.printMessage(paramKind, str, paramDocTree, this.env.currDocComment, this.env.currPath
/* 120 */         .getCompilationUnit());
/*     */ 
/* 122 */       this.stats.record(paramGroup, paramKind, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void report(Group paramGroup, Diagnostic.Kind paramKind, Tree paramTree, String paramString, Object[] paramArrayOfObject) {
/* 127 */     if (this.options.isEnabled(paramGroup, this.env.currAccess)) {
/* 128 */       String str = localize(paramString, paramArrayOfObject);
/* 129 */       this.env.trees.printMessage(paramKind, str, paramTree, this.env.currPath.getCompilationUnit());
/*     */ 
/* 131 */       this.stats.record(paramGroup, paramKind, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   String localize(String paramString, Object[] paramArrayOfObject) {
/* 136 */     String str = this.bundle.getString(paramString);
/* 137 */     if (str == null) {
/* 138 */       StringBuilder localStringBuilder = new StringBuilder();
/* 139 */       localStringBuilder.append("message file broken: code=").append(paramString);
/* 140 */       if (paramArrayOfObject.length > 0) {
/* 141 */         localStringBuilder.append(" arguments={0}");
/* 142 */         for (int i = 1; i < paramArrayOfObject.length; i++) {
/* 143 */           localStringBuilder.append(", {").append(i).append("}");
/*     */         }
/*     */       }
/* 146 */       str = localStringBuilder.toString();
/*     */     }
/* 148 */     return MessageFormat.format(str, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static enum Group
/*     */   {
/*  65 */     ACCESSIBILITY, 
/*  66 */     HTML, 
/*  67 */     MISSING, 
/*  68 */     SYNTAX, 
/*  69 */     REFERENCE;
/*     */ 
/*  71 */     String optName() { return StringUtils.toLowerCase(name()); } 
/*  72 */     String notOptName() { return "-" + optName(); }
/*     */ 
/*     */     static boolean accepts(String paramString) {
/*  75 */       for (Group localGroup : values())
/*  76 */         if (paramString.equals(localGroup.optName())) return true;
/*  77 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Options
/*     */   {
/* 157 */     Map<String, Env.AccessKind> map = new HashMap();
/*     */     private final Messages.Stats stats;
/*     */     private static final String ALL = "all";
/*     */ 
/*     */     static boolean isValidOptions(String paramString)
/*     */     {
/* 161 */       for (String str : paramString.split(",")) {
/* 162 */         if (!isValidOption(StringUtils.toLowerCase(str.trim())))
/* 163 */           return false;
/*     */       }
/* 165 */       return true;
/*     */     }
/*     */ 
/*     */     private static boolean isValidOption(String paramString) {
/* 169 */       if ((paramString.equals("none")) || (paramString.equals("stats"))) {
/* 170 */         return true;
/*     */       }
/* 172 */       int i = paramString.startsWith("-") ? 1 : 0;
/* 173 */       int j = paramString.indexOf("/");
/* 174 */       String str = paramString.substring(i, j != -1 ? j : paramString.length());
/*     */ 
/* 176 */       return ((i == 0) && (str.equals("all"))) || ((Messages.Group.accepts(str)) && ((j == -1) || 
/* 176 */         (Env.AccessKind.accepts(paramString
/* 176 */         .substring(j + 1)))));
/*     */     }
/*     */ 
/*     */     Options(Messages.Stats paramStats)
/*     */     {
/* 180 */       this.stats = paramStats;
/*     */     }
/*     */ 
/*     */     boolean isEnabled(Messages.Group paramGroup, Env.AccessKind paramAccessKind)
/*     */     {
/* 185 */       if (this.map.isEmpty()) {
/* 186 */         this.map.put("all", Env.AccessKind.PROTECTED);
/*     */       }
/* 188 */       Env.AccessKind localAccessKind = (Env.AccessKind)this.map.get(paramGroup.optName());
/* 189 */       if ((localAccessKind != null) && (paramAccessKind.compareTo(localAccessKind) >= 0)) {
/* 190 */         return true;
/*     */       }
/* 192 */       localAccessKind = (Env.AccessKind)this.map.get("all");
/* 193 */       if ((localAccessKind != null) && (paramAccessKind.compareTo(localAccessKind) >= 0)) {
/* 194 */         localAccessKind = (Env.AccessKind)this.map.get(paramGroup.notOptName());
/* 195 */         if ((localAccessKind == null) || (paramAccessKind.compareTo(localAccessKind) > 0)) {
/* 196 */           return true;
/*     */         }
/*     */       }
/* 199 */       return false;
/*     */     }
/*     */ 
/*     */     void setOptions(String paramString) {
/* 203 */       if (paramString == null)
/* 204 */         setOption("all", Env.AccessKind.PRIVATE);
/*     */       else
/* 206 */         for (String str : paramString.split(","))
/* 207 */           setOption(StringUtils.toLowerCase(str.trim()));
/*     */     }
/*     */ 
/*     */     private void setOption(String paramString) throws IllegalArgumentException
/*     */     {
/* 212 */       if (paramString.equals("stats")) {
/* 213 */         this.stats.setEnabled(true);
/* 214 */         return;
/*     */       }
/*     */ 
/* 217 */       int i = paramString.indexOf("/");
/* 218 */       if (i > 0) {
/* 219 */         Env.AccessKind localAccessKind = Env.AccessKind.valueOf(StringUtils.toUpperCase(paramString.substring(i + 1)));
/* 220 */         setOption(paramString.substring(0, i), localAccessKind);
/*     */       } else {
/* 222 */         setOption(paramString, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void setOption(String paramString, Env.AccessKind paramAccessKind) {
/* 227 */       this.map.put(paramString, paramString
/* 228 */         .startsWith("-") ? 
/* 228 */         Env.AccessKind.PUBLIC : paramAccessKind != null ? paramAccessKind : 
/* 228 */         Env.AccessKind.PRIVATE);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Stats
/*     */   {
/*     */     public static final String OPT = "stats";
/*     */     public static final String NO_CODE = "";
/*     */     final ResourceBundle bundle;
/*     */     int[] groupCounts;
/*     */     int[] dkindCounts;
/*     */     Map<String, Integer> codeCounts;
/*     */ 
/*     */     Stats(ResourceBundle paramResourceBundle)
/*     */     {
/* 252 */       this.bundle = paramResourceBundle;
/*     */     }
/*     */ 
/*     */     void setEnabled(boolean paramBoolean) {
/* 256 */       if (paramBoolean) {
/* 257 */         this.groupCounts = new int[Messages.Group.values().length];
/* 258 */         this.dkindCounts = new int[Diagnostic.Kind.values().length];
/* 259 */         this.codeCounts = new HashMap();
/*     */       } else {
/* 261 */         this.groupCounts = null;
/* 262 */         this.dkindCounts = null;
/* 263 */         this.codeCounts = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     void record(Messages.Group paramGroup, Diagnostic.Kind paramKind, String paramString) {
/* 268 */       if (this.codeCounts == null) {
/* 269 */         return;
/*     */       }
/* 271 */       this.groupCounts[paramGroup.ordinal()] += 1;
/* 272 */       this.dkindCounts[paramKind.ordinal()] += 1;
/* 273 */       if (paramString == null) {
/* 274 */         paramString = "";
/*     */       }
/* 276 */       Integer localInteger = (Integer)this.codeCounts.get(paramString);
/* 277 */       this.codeCounts.put(paramString, Integer.valueOf(localInteger == null ? 1 : localInteger.intValue() + 1));
/*     */     }
/*     */ 
/*     */     void report(PrintWriter paramPrintWriter) {
/* 281 */       if (this.codeCounts == null) {
/* 282 */         return;
/*     */       }
/* 284 */       paramPrintWriter.println("By group...");
/* 285 */       Table localTable = new Table(null);
/* 286 */       for (Object localObject3 : Messages.Group.values()) {
/* 287 */         localTable.put(localObject3.optName(), this.groupCounts[localObject3.ordinal()]);
/*     */       }
/* 289 */       localTable.print(paramPrintWriter);
/* 290 */       paramPrintWriter.println();
/* 291 */       paramPrintWriter.println("By diagnostic kind...");
/* 292 */       ??? = new Table(null);
/*     */       String str1;
/* 293 */       for (str1 : Diagnostic.Kind.values()) {
/* 294 */         ((Table)???).put(StringUtils.toLowerCase(str1.toString()), this.dkindCounts[str1.ordinal()]);
/*     */       }
/* 296 */       ((Table)???).print(paramPrintWriter);
/* 297 */       paramPrintWriter.println();
/* 298 */       paramPrintWriter.println("By message kind...");
/* 299 */       ??? = new Table(null);
/* 300 */       for (Map.Entry localEntry : this.codeCounts.entrySet()) { str1 = (String)localEntry.getKey();
/*     */         String str2;
/*     */         try {
/* 304 */           str2 = str1.equals("") ? "OTHER" : this.bundle.getString(str1);
/*     */         } catch (MissingResourceException localMissingResourceException) {
/* 306 */           str2 = str1;
/*     */         }
/* 308 */         ((Table)???).put(str2, ((Integer)localEntry.getValue()).intValue());
/*     */       }
/* 310 */       ((Table)???).print(paramPrintWriter);
/*     */     }
/*     */ 
/*     */     private static class Table
/*     */     {
/* 318 */       private static final Comparator<Integer> DECREASING = new Comparator()
/*     */       {
/*     */         public int compare(Integer paramAnonymousInteger1, Integer paramAnonymousInteger2) {
/* 321 */           return paramAnonymousInteger2.compareTo(paramAnonymousInteger1);
/*     */         }
/* 318 */       };
/*     */ 
/* 324 */       private final TreeMap<Integer, Set<String>> map = new TreeMap(DECREASING);
/*     */ 
/*     */       void put(String paramString, int paramInt) {
/* 327 */         if (paramInt == 0) {
/* 328 */           return;
/*     */         }
/* 330 */         Object localObject = (Set)this.map.get(Integer.valueOf(paramInt));
/* 331 */         if (localObject == null) {
/* 332 */           this.map.put(Integer.valueOf(paramInt), localObject = new TreeSet());
/*     */         }
/* 334 */         ((Set)localObject).add(paramString);
/*     */       }
/*     */ 
/*     */       void print(PrintWriter paramPrintWriter) {
/* 338 */         for (Map.Entry localEntry : this.map.entrySet()) {
/* 339 */           i = ((Integer)localEntry.getKey()).intValue();
/* 340 */           Set localSet = (Set)localEntry.getValue();
/* 341 */           for (String str : localSet)
/* 342 */             paramPrintWriter.println(String.format("%6d: %s", new Object[] { Integer.valueOf(i), str }));
/*     */         }
/*     */         int i;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.Messages
 * JD-Core Version:    0.6.2
 */