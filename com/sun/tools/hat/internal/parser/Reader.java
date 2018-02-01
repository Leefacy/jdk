/*    */ package com.sun.tools.hat.internal.parser;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.Snapshot;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class Reader
/*    */ {
/*    */   protected PositionDataInputStream in;
/*    */ 
/*    */   protected Reader(PositionDataInputStream paramPositionDataInputStream)
/*    */   {
/* 50 */     this.in = paramPositionDataInputStream;
/*    */   }
/*    */ 
/*    */   public abstract Snapshot read()
/*    */     throws IOException;
/*    */ 
/*    */   public static Snapshot readFile(String paramString, boolean paramBoolean, int paramInt)
/*    */     throws IOException
/*    */   {
/* 68 */     int i = 1;
/* 69 */     int j = paramString.lastIndexOf('#');
/*    */     Object localObject2;
/* 70 */     if (j > -1) {
/* 71 */       localObject1 = paramString.substring(j + 1, paramString.length());
/*    */       try {
/* 73 */         i = Integer.parseInt((String)localObject1, 10);
/*    */       } catch (NumberFormatException localNumberFormatException) {
/* 75 */         localObject2 = "In file name \"" + paramString + "\", a dump number was " + "expected after the :, but \"" + (String)localObject1 + "\" was found instead.";
/*    */ 
/* 79 */         System.err.println((String)localObject2);
/* 80 */         throw new IOException((String)localObject2);
/*    */       }
/* 82 */       paramString = paramString.substring(0, j);
/*    */     }
/* 84 */     Object localObject1 = new PositionDataInputStream(new BufferedInputStream(new FileInputStream(paramString)));
/*    */     try
/*    */     {
/* 87 */       int k = ((PositionDataInputStream)localObject1).readInt();
/* 88 */       if (k == 1245795905) {
/* 89 */         localObject2 = new HprofReader(paramString, (PositionDataInputStream)localObject1, i, paramBoolean, paramInt);
/*    */ 
/* 92 */         return ((Reader)localObject2).read();
/*    */       }
/* 94 */       throw new IOException("Unrecognized magic number: " + k);
/*    */     }
/*    */     finally {
/* 97 */       ((PositionDataInputStream)localObject1).close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.parser.Reader
 * JD-Core Version:    0.6.2
 */