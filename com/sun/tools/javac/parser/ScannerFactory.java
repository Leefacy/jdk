/*    */ package com.sun.tools.javac.parser;
/*    */ 
/*    */ import com.sun.tools.javac.code.Source;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Key;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import com.sun.tools.javac.util.Names;
/*    */ import java.nio.CharBuffer;
/*    */ 
/*    */ public class ScannerFactory
/*    */ {
/* 46 */   public static final Context.Key<ScannerFactory> scannerFactoryKey = new Context.Key();
/*    */   final Log log;
/*    */   final Names names;
/*    */   final Source source;
/*    */   final Tokens tokens;
/*    */ 
/*    */   public static ScannerFactory instance(Context paramContext)
/*    */   {
/* 51 */     ScannerFactory localScannerFactory = (ScannerFactory)paramContext.get(scannerFactoryKey);
/* 52 */     if (localScannerFactory == null)
/* 53 */       localScannerFactory = new ScannerFactory(paramContext);
/* 54 */     return localScannerFactory;
/*    */   }
/*    */ 
/*    */   protected ScannerFactory(Context paramContext)
/*    */   {
/* 64 */     paramContext.put(scannerFactoryKey, this);
/* 65 */     this.log = Log.instance(paramContext);
/* 66 */     this.names = Names.instance(paramContext);
/* 67 */     this.source = Source.instance(paramContext);
/* 68 */     this.tokens = Tokens.instance(paramContext);
/*    */   }
/*    */ 
/*    */   public Scanner newScanner(CharSequence paramCharSequence, boolean paramBoolean) {
/* 72 */     if ((paramCharSequence instanceof CharBuffer)) {
/* 73 */       localObject = (CharBuffer)paramCharSequence;
/* 74 */       if (paramBoolean) {
/* 75 */         return new Scanner(this, new JavadocTokenizer(this, (CharBuffer)localObject));
/*    */       }
/* 77 */       return new Scanner(this, (CharBuffer)localObject);
/*    */     }
/* 79 */     Object localObject = paramCharSequence.toString().toCharArray();
/* 80 */     return newScanner((char[])localObject, localObject.length, paramBoolean);
/*    */   }
/*    */ 
/*    */   public Scanner newScanner(char[] paramArrayOfChar, int paramInt, boolean paramBoolean)
/*    */   {
/* 85 */     if (paramBoolean) {
/* 86 */       return new Scanner(this, new JavadocTokenizer(this, paramArrayOfChar, paramInt));
/*    */     }
/* 88 */     return new Scanner(this, paramArrayOfChar, paramInt);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.ScannerFactory
 * JD-Core Version:    0.6.2
 */