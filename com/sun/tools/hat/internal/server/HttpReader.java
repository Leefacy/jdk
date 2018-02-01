/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.oql.OQLEngine;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.Socket;
/*     */ import java.net.URLDecoder;
/*     */ 
/*     */ public class HttpReader
/*     */   implements Runnable
/*     */ {
/*     */   private Socket socket;
/*     */   private PrintWriter out;
/*     */   private Snapshot snapshot;
/*     */   private OQLEngine engine;
/*     */ 
/*     */   public HttpReader(Socket paramSocket, Snapshot paramSnapshot, OQLEngine paramOQLEngine)
/*     */   {
/*  65 */     this.socket = paramSocket;
/*  66 */     this.snapshot = paramSnapshot;
/*  67 */     this.engine = paramOQLEngine;
/*     */   }
/*     */ 
/*     */   public void run() {
/*  71 */     BufferedInputStream localBufferedInputStream = null;
/*     */     try {
/*  73 */       localBufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
/*  74 */       this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket
/*  76 */         .getOutputStream())));
/*  77 */       this.out.println("HTTP/1.0 200 OK");
/*  78 */       this.out.println("Cache-Control: no-cache");
/*  79 */       this.out.println("Pragma: no-cache");
/*  80 */       this.out.println();
/*  81 */       if ((localBufferedInputStream.read() != 71) || (localBufferedInputStream.read() != 69) || 
/*  82 */         (localBufferedInputStream
/*  82 */         .read() != 84) || (localBufferedInputStream.read() != 32)) {
/*  83 */         outputError("Protocol error");
/*     */       }
/*     */ 
/*  86 */       StringBuilder localStringBuilder = new StringBuilder();
/*     */       int i;
/*  87 */       while (((i = localBufferedInputStream.read()) != -1) && (i != 32)) {
/*  88 */         char c = (char)i;
/*  89 */         localStringBuilder.append(c);
/*     */       }
/*  91 */       String str = localStringBuilder.toString();
/*  92 */       str = URLDecoder.decode(str, "UTF-8");
/*  93 */       Object localObject1 = null;
/*  94 */       if (this.snapshot == null) {
/*  95 */         outputError("The heap snapshot is still being read.");
/*  96 */         return;
/*  97 */       }if (str.equals("/")) {
/*  98 */         localObject1 = new AllClassesQuery(true, this.engine != null);
/*  99 */         ((QueryHandler)localObject1).setUrlStart("");
/* 100 */         ((QueryHandler)localObject1).setQuery("");
/* 101 */       } else if (str.startsWith("/oql/")) {
/* 102 */         if (this.engine != null) {
/* 103 */           localObject1 = new OQLQuery(this.engine);
/* 104 */           ((QueryHandler)localObject1).setUrlStart("");
/* 105 */           ((QueryHandler)localObject1).setQuery(str.substring(5));
/*     */         }
/* 107 */       } else if (str.startsWith("/oqlhelp/")) {
/* 108 */         if (this.engine != null) {
/* 109 */           localObject1 = new OQLHelp();
/* 110 */           ((QueryHandler)localObject1).setUrlStart("");
/* 111 */           ((QueryHandler)localObject1).setQuery("");
/*     */         }
/* 113 */       } else if (str.equals("/allClassesWithPlatform/")) {
/* 114 */         localObject1 = new AllClassesQuery(false, this.engine != null);
/* 115 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 116 */         ((QueryHandler)localObject1).setQuery("");
/* 117 */       } else if (str.equals("/showRoots/")) {
/* 118 */         localObject1 = new AllRootsQuery();
/* 119 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 120 */         ((QueryHandler)localObject1).setQuery("");
/* 121 */       } else if (str.equals("/showInstanceCounts/includePlatform/")) {
/* 122 */         localObject1 = new InstancesCountQuery(false);
/* 123 */         ((QueryHandler)localObject1).setUrlStart("../../");
/* 124 */         ((QueryHandler)localObject1).setQuery("");
/* 125 */       } else if (str.equals("/showInstanceCounts/")) {
/* 126 */         localObject1 = new InstancesCountQuery(true);
/* 127 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 128 */         ((QueryHandler)localObject1).setQuery("");
/* 129 */       } else if (str.startsWith("/instances/")) {
/* 130 */         localObject1 = new InstancesQuery(false);
/* 131 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 132 */         ((QueryHandler)localObject1).setQuery(str.substring(11));
/* 133 */       } else if (str.startsWith("/newInstances/")) {
/* 134 */         localObject1 = new InstancesQuery(false, true);
/* 135 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 136 */         ((QueryHandler)localObject1).setQuery(str.substring(14));
/* 137 */       } else if (str.startsWith("/allInstances/")) {
/* 138 */         localObject1 = new InstancesQuery(true);
/* 139 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 140 */         ((QueryHandler)localObject1).setQuery(str.substring(14));
/* 141 */       } else if (str.startsWith("/allNewInstances/")) {
/* 142 */         localObject1 = new InstancesQuery(true, true);
/* 143 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 144 */         ((QueryHandler)localObject1).setQuery(str.substring(17));
/* 145 */       } else if (str.startsWith("/object/")) {
/* 146 */         localObject1 = new ObjectQuery();
/* 147 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 148 */         ((QueryHandler)localObject1).setQuery(str.substring(8));
/* 149 */       } else if (str.startsWith("/class/")) {
/* 150 */         localObject1 = new ClassQuery();
/* 151 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 152 */         ((QueryHandler)localObject1).setQuery(str.substring(7));
/* 153 */       } else if (str.startsWith("/roots/")) {
/* 154 */         localObject1 = new RootsQuery(false);
/* 155 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 156 */         ((QueryHandler)localObject1).setQuery(str.substring(7));
/* 157 */       } else if (str.startsWith("/allRoots/")) {
/* 158 */         localObject1 = new RootsQuery(true);
/* 159 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 160 */         ((QueryHandler)localObject1).setQuery(str.substring(10));
/* 161 */       } else if (str.startsWith("/reachableFrom/")) {
/* 162 */         localObject1 = new ReachableQuery();
/* 163 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 164 */         ((QueryHandler)localObject1).setQuery(str.substring(15));
/* 165 */       } else if (str.startsWith("/rootStack/")) {
/* 166 */         localObject1 = new RootStackQuery();
/* 167 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 168 */         ((QueryHandler)localObject1).setQuery(str.substring(11));
/* 169 */       } else if (str.startsWith("/histo/")) {
/* 170 */         localObject1 = new HistogramQuery();
/* 171 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 172 */         ((QueryHandler)localObject1).setQuery(str.substring(7));
/* 173 */       } else if (str.startsWith("/refsByType/")) {
/* 174 */         localObject1 = new RefsByTypeQuery();
/* 175 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 176 */         ((QueryHandler)localObject1).setQuery(str.substring(12));
/* 177 */       } else if (str.startsWith("/finalizerSummary/")) {
/* 178 */         localObject1 = new FinalizerSummaryQuery();
/* 179 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 180 */         ((QueryHandler)localObject1).setQuery("");
/* 181 */       } else if (str.startsWith("/finalizerObjects/")) {
/* 182 */         localObject1 = new FinalizerObjectsQuery();
/* 183 */         ((QueryHandler)localObject1).setUrlStart("../");
/* 184 */         ((QueryHandler)localObject1).setQuery("");
/*     */       }
/*     */ 
/* 187 */       if (localObject1 != null) {
/* 188 */         ((QueryHandler)localObject1).setOutput(this.out);
/* 189 */         ((QueryHandler)localObject1).setSnapshot(this.snapshot);
/* 190 */         ((QueryHandler)localObject1).run();
/*     */       } else {
/* 192 */         outputError("Query '" + str + "' not implemented");
/*     */       }
/*     */     } catch (IOException localIOException3) {
/* 195 */       localIOException3.printStackTrace();
/*     */     } finally {
/* 197 */       if (this.out != null)
/* 198 */         this.out.close();
/*     */       try
/*     */       {
/* 201 */         if (localBufferedInputStream != null)
/* 202 */           localBufferedInputStream.close();
/*     */       }
/*     */       catch (IOException localIOException8) {
/*     */       }
/*     */       try {
/* 207 */         this.socket.close();
/*     */       } catch (IOException localIOException9) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void outputError(String paramString) {
/* 214 */     this.out.println();
/* 215 */     this.out.println("<html><body bgcolor=\"#ffffff\">");
/* 216 */     this.out.println(Misc.encodeHtml(paramString));
/* 217 */     this.out.println("</body></html>");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.server.HttpReader
 * JD-Core Version:    0.6.2
 */