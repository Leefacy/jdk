/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class Graph
/*    */   implements Iterable<ConnectedComponent>
/*    */ {
/* 40 */   private final Element source = new SourceNode();
/* 41 */   private final Element sink = new SinkNode();
/*    */ 
/* 46 */   private final List<ConnectedComponent> ccs = new ArrayList();
/*    */ 
/*    */   public Graph(Expression body)
/*    */   {
/* 56 */     Expression whole = new Sequence(new Sequence(this.source, body), this.sink);
/*    */ 
/* 59 */     whole.buildDAG(ElementSet.EMPTY_SET);
/*    */ 
/* 65 */     this.source.assignDfsPostOrder(this.sink);
/* 66 */     this.source.buildStronglyConnectedComponents(this.ccs);
/*    */ 
/* 69 */     Set visited = new HashSet();
/* 70 */     for (ConnectedComponent cc : this.ccs) {
/* 71 */       visited.clear();
/* 72 */       if (this.source.checkCutSet(cc, visited))
/* 73 */         cc.isRequired = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Iterator<ConnectedComponent> iterator()
/*    */   {
/* 82 */     return this.ccs.iterator();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 86 */     return this.ccs.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.Graph
 * JD-Core Version:    0.6.2
 */