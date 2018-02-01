/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class GraphUtils
/*     */ {
/*     */   public static <D, N extends TarjanNode<D>> List<? extends List<? extends N>> tarjan(Iterable<? extends N> paramIterable)
/*     */   {
/* 106 */     ListBuffer localListBuffer1 = new ListBuffer();
/* 107 */     ListBuffer localListBuffer2 = new ListBuffer();
/* 108 */     int i = 0;
/* 109 */     for (TarjanNode localTarjanNode : paramIterable) {
/* 110 */       if (localTarjanNode.index == -1) {
/* 111 */         i += tarjan(localTarjanNode, i, localListBuffer2, localListBuffer1);
/*     */       }
/*     */     }
/* 114 */     return localListBuffer1.toList();
/*     */   }
/*     */ 
/*     */   private static <D, N extends TarjanNode<D>> int tarjan(N paramN, int paramInt, ListBuffer<N> paramListBuffer, ListBuffer<List<N>> paramListBuffer1) {
/* 118 */     paramN.index = paramInt;
/* 119 */     paramN.lowlink = paramInt;
/* 120 */     paramInt++;
/* 121 */     paramListBuffer.prepend(paramN);
/* 122 */     paramN.active = true;
/* 123 */     for (Object localObject1 = paramN.getAllDependencies().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (TarjanNode)((Iterator)localObject1).next();
/*     */ 
/* 125 */       Object localObject3 = localObject2;
/* 126 */       if (localObject3.index == -1) {
/* 127 */         tarjan(localObject3, paramInt, paramListBuffer, paramListBuffer1);
/* 128 */         paramN.lowlink = Math.min(paramN.lowlink, localObject3.lowlink);
/* 129 */       } else if (paramListBuffer.contains(localObject3)) {
/* 130 */         paramN.lowlink = Math.min(paramN.lowlink, localObject3.index);
/*     */       }
/*     */     }
/*     */     Object localObject2;
/* 133 */     if (paramN.lowlink == paramN.index)
/*     */     {
/* 135 */       localObject2 = new ListBuffer();
/*     */       do {
/* 137 */         localObject1 = (TarjanNode)paramListBuffer.remove();
/* 138 */         ((TarjanNode)localObject1).active = false;
/* 139 */         ((ListBuffer)localObject2).add(localObject1);
/* 140 */       }while (localObject1 != paramN);
/* 141 */       paramListBuffer1.add(((ListBuffer)localObject2).toList());
/*     */     }
/* 143 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public static <D> String toDot(Iterable<? extends TarjanNode<D>> paramIterable, String paramString1, String paramString2)
/*     */   {
/* 153 */     StringBuilder localStringBuilder = new StringBuilder();
/* 154 */     localStringBuilder.append(String.format("digraph %s {\n", new Object[] { paramString1 }));
/* 155 */     localStringBuilder.append(String.format("label = \"%s\";\n", new Object[] { paramString2 }));
/*     */ 
/* 157 */     for (Iterator localIterator1 = paramIterable.iterator(); localIterator1.hasNext(); ) { localTarjanNode1 = (TarjanNode)localIterator1.next();
/* 158 */       localStringBuilder.append(String.format("%s [label = \"%s\"];\n", new Object[] { Integer.valueOf(localTarjanNode1.hashCode()), localTarjanNode1.toString() }));
/*     */     }
/* 161 */     TarjanNode localTarjanNode1;
/* 161 */     for (localIterator1 = paramIterable.iterator(); localIterator1.hasNext(); ) { localTarjanNode1 = (TarjanNode)localIterator1.next();
/*     */       DependencyKind localDependencyKind;
/* 162 */       for (localDependencyKind : localTarjanNode1.getSupportedDependencyKinds()) {
/* 163 */         for (TarjanNode localTarjanNode2 : localTarjanNode1.getDependenciesByKind(localDependencyKind)) {
/* 164 */           localStringBuilder.append(String.format("%s -> %s [label = \" %s \" style = %s ];\n", new Object[] { 
/* 165 */             Integer.valueOf(localTarjanNode1
/* 165 */             .hashCode()), Integer.valueOf(localTarjanNode2.hashCode()), localTarjanNode1.getDependencyName(localTarjanNode2, localDependencyKind), localDependencyKind.getDotStyle() }));
/*     */         }
/*     */       }
/*     */     }
/* 169 */     localStringBuilder.append("}\n");
/* 170 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static abstract interface DependencyKind
/*     */   {
/*     */     public abstract String getDotStyle();
/*     */   }
/*     */ 
/*     */   public static abstract class Node<D>
/*     */   {
/*     */     public final D data;
/*     */ 
/*     */     public Node(D paramD)
/*     */     {
/*  55 */       this.data = paramD;
/*     */     }
/*     */ 
/*     */     public abstract GraphUtils.DependencyKind[] getSupportedDependencyKinds();
/*     */ 
/*     */     public abstract Iterable<? extends Node<D>> getAllDependencies();
/*     */ 
/*     */     public abstract String getDependencyName(Node<D> paramNode, GraphUtils.DependencyKind paramDependencyKind);
/*     */ 
/*     */     public String toString()
/*     */     {
/*  75 */       return this.data.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class TarjanNode<D> extends GraphUtils.Node<D> implements Comparable<TarjanNode<D>>
/*     */   {
/*  84 */     int index = -1;
/*     */     int lowlink;
/*     */     boolean active;
/*     */ 
/*     */     public TarjanNode(D paramD)
/*     */     {
/*  89 */       super();
/*     */     }
/*     */ 
/*     */     public abstract Iterable<? extends TarjanNode<D>> getAllDependencies();
/*     */ 
/*     */     public abstract Iterable<? extends TarjanNode<D>> getDependenciesByKind(GraphUtils.DependencyKind paramDependencyKind);
/*     */ 
/*     */     public int compareTo(TarjanNode<D> paramTarjanNode) {
/*  97 */       return this.index == paramTarjanNode.index ? 0 : this.index < paramTarjanNode.index ? -1 : 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.GraphUtils
 * JD-Core Version:    0.6.2
 */