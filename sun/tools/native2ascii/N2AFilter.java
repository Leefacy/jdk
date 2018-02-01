/*    */ package sun.tools.native2ascii;
/*    */ 
/*    */ import java.io.FilterWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ class N2AFilter extends FilterWriter
/*    */ {
/*    */   public N2AFilter(Writer paramWriter)
/*    */   {
/* 41 */     super(paramWriter);
/*    */   }
/*    */   public void write(char paramChar) throws IOException {
/* 44 */     char[] arrayOfChar = new char[1];
/* 45 */     arrayOfChar[0] = paramChar;
/* 46 */     write(arrayOfChar, 0, 1);
/*    */   }
/*    */ 
/*    */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 51 */     String str1 = System.getProperty("line.separator");
/*    */ 
/* 54 */     for (int i = 0; i < paramInt2; i++)
/* 55 */       if (paramArrayOfChar[i] > '')
/*    */       {
/* 57 */         this.out.write(92);
/* 58 */         this.out.write(117);
/*    */ 
/* 60 */         String str2 = Integer.toHexString(paramArrayOfChar[i]);
/*    */ 
/* 61 */         StringBuffer localStringBuffer = new StringBuffer(str2);
/* 62 */         localStringBuffer.reverse();
/* 63 */         int j = 4 - localStringBuffer.length();
/* 64 */         for (int k = 0; k < j; k++) {
/* 65 */           localStringBuffer.append('0');
/*    */         }
/* 67 */         for (k = 0; k < 4; k++)
/* 68 */           this.out.write(localStringBuffer.charAt(3 - k));
/*    */       }
/*    */       else {
/* 71 */         this.out.write(paramArrayOfChar[i]);
/*    */       }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.native2ascii.N2AFilter
 * JD-Core Version:    0.6.2
 */