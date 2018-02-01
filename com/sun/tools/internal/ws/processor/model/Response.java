/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Response extends Message
/*    */ {
/* 73 */   private Map _faultBlocks = new HashMap();
/*    */ 
/*    */   public Response(com.sun.tools.internal.ws.wsdl.document.Message entity, ErrorReceiver receiver)
/*    */   {
/* 42 */     super(entity, receiver);
/*    */   }
/*    */ 
/*    */   public void addFaultBlock(Block b) {
/* 46 */     if (this._faultBlocks.containsKey(b.getName())) {
/* 47 */       throw new ModelException("model.uniqueness", new Object[0]);
/*    */     }
/* 49 */     this._faultBlocks.put(b.getName(), b);
/*    */   }
/*    */ 
/*    */   public Iterator getFaultBlocks() {
/* 53 */     return this._faultBlocks.values().iterator();
/*    */   }
/*    */ 
/*    */   public int getFaultBlockCount() {
/* 57 */     return this._faultBlocks.size();
/*    */   }
/*    */ 
/*    */   public Map getFaultBlocksMap()
/*    */   {
/* 62 */     return this._faultBlocks;
/*    */   }
/*    */ 
/*    */   public void setFaultBlocksMap(Map m) {
/* 66 */     this._faultBlocks = m;
/*    */   }
/*    */ 
/*    */   public void accept(ModelVisitor visitor) throws Exception {
/* 70 */     visitor.visit(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Response
 * JD-Core Version:    0.6.2
 */