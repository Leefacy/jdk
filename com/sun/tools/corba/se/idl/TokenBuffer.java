/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ class TokenBuffer
/*    */ {
/* 42 */   private final int DEFAULT_SIZE = 10;
/*    */ 
/* 44 */   private int _size = 0;
/* 45 */   private Token[] _buffer = null;
/* 46 */   private int _currPos = -1;
/*    */ 
/*    */   TokenBuffer()
/*    */   {
/* 50 */     this._size = 10;
/* 51 */     this._buffer = new Token[this._size];
/* 52 */     this._currPos = -1;
/*    */   }
/*    */ 
/*    */   TokenBuffer(int paramInt) throws Exception
/*    */   {
/* 57 */     this._size = paramInt;
/* 58 */     this._buffer = new Token[this._size];
/* 59 */     this._currPos = -1;
/*    */   }
/*    */ 
/*    */   void insert(Token paramToken)
/*    */   {
/* 66 */     this._currPos = (++this._currPos % this._size);
/* 67 */     this._buffer[this._currPos] = paramToken;
/*    */   }
/*    */ 
/*    */   Token lookBack(int paramInt)
/*    */   {
/* 74 */     return this._buffer[(this._currPos - paramInt + this._size)];
/*    */   }
/*    */ 
/*    */   Token current()
/*    */   {
/* 81 */     return this._buffer[this._currPos];
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.TokenBuffer
 * JD-Core Version:    0.6.2
 */