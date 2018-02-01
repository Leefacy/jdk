/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MethodSet
/*     */ {
/*     */   private final Map lookupMap;
/*     */   private int count;
/*     */   private boolean frozen;
/*     */ 
/*     */   public MethodSet()
/*     */   {
/*  65 */     this.frozen = false;
/*  66 */     this.lookupMap = new HashMap();
/*  67 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  74 */     return this.count;
/*     */   }
/*     */ 
/*     */   public void add(MemberDefinition paramMemberDefinition)
/*     */   {
/*  83 */     if (this.frozen) {
/*  84 */       throw new CompilerError("add()");
/*     */     }
/*     */ 
/*  89 */     Identifier localIdentifier = paramMemberDefinition.getName();
/*     */ 
/*  92 */     Object localObject = (List)this.lookupMap.get(localIdentifier);
/*     */ 
/*  94 */     if (localObject == null)
/*     */     {
/*  97 */       localObject = new ArrayList();
/*  98 */       this.lookupMap.put(localIdentifier, localObject);
/*     */     }
/*     */ 
/* 103 */     int i = ((List)localObject).size();
/* 104 */     for (int j = 0; j < i; j++)
/*     */     {
/* 106 */       if (((MemberDefinition)((List)localObject).get(j))
/* 106 */         .getType().equalArguments(paramMemberDefinition.getType())) {
/* 107 */         throw new CompilerError("duplicate addition");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 112 */     ((List)localObject).add(paramMemberDefinition);
/* 113 */     this.count += 1;
/*     */   }
/*     */ 
/*     */   public void replace(MemberDefinition paramMemberDefinition)
/*     */   {
/* 122 */     if (this.frozen) {
/* 123 */       throw new CompilerError("replace()");
/*     */     }
/*     */ 
/* 128 */     Identifier localIdentifier = paramMemberDefinition.getName();
/*     */ 
/* 131 */     Object localObject = (List)this.lookupMap.get(localIdentifier);
/*     */ 
/* 133 */     if (localObject == null)
/*     */     {
/* 136 */       localObject = new ArrayList();
/* 137 */       this.lookupMap.put(localIdentifier, localObject);
/*     */     }
/*     */ 
/* 142 */     int i = ((List)localObject).size();
/* 143 */     for (int j = 0; j < i; j++)
/*     */     {
/* 145 */       if (((MemberDefinition)((List)localObject).get(j))
/* 145 */         .getType().equalArguments(paramMemberDefinition.getType())) {
/* 146 */         ((List)localObject).set(j, paramMemberDefinition);
/* 147 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 152 */     ((List)localObject).add(paramMemberDefinition);
/* 153 */     this.count += 1;
/*     */   }
/*     */ 
/*     */   public MemberDefinition lookupSig(Identifier paramIdentifier, Type paramType)
/*     */   {
/* 163 */     Iterator localIterator = lookupName(paramIdentifier);
/*     */ 
/* 166 */     while (localIterator.hasNext()) {
/* 167 */       MemberDefinition localMemberDefinition = (MemberDefinition)localIterator.next();
/* 168 */       if (localMemberDefinition.getType().equalArguments(paramType)) {
/* 169 */         return localMemberDefinition;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator lookupName(Identifier paramIdentifier)
/*     */   {
/* 184 */     List localList = (List)this.lookupMap.get(paramIdentifier);
/* 185 */     if (localList == null)
/*     */     {
/* 188 */       return Collections.emptyIterator();
/*     */     }
/* 190 */     return localList.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 239 */     return new Iterator()
/*     */     {
/* 202 */       Iterator hashIter = MethodSet.this.lookupMap.values().iterator();
/* 203 */       Iterator listIter = Collections.emptyIterator();
/*     */ 
/*     */       public boolean hasNext() {
/* 206 */         if (this.listIter.hasNext()) {
/* 207 */           return true;
/*     */         }
/* 209 */         if (this.hashIter.hasNext()) {
/* 210 */           this.listIter = ((List)this.hashIter.next())
/* 211 */             .iterator();
/*     */ 
/* 214 */           if (this.listIter.hasNext()) {
/* 215 */             return true;
/*     */           }
/* 217 */           throw new CompilerError("iterator() in MethodSet");
/*     */         }
/*     */ 
/* 224 */         return false;
/*     */       }
/*     */ 
/*     */       public Object next() {
/* 228 */         return this.listIter.next();
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 232 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void freeze()
/*     */   {
/* 249 */     this.frozen = true;
/*     */   }
/*     */ 
/*     */   public boolean isFrozen()
/*     */   {
/* 256 */     return this.frozen;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 263 */     int i = size();
/* 264 */     StringBuffer localStringBuffer = new StringBuffer();
/* 265 */     Iterator localIterator = iterator();
/* 266 */     localStringBuffer.append("{");
/*     */ 
/* 268 */     while (localIterator.hasNext()) {
/* 269 */       localStringBuffer.append(localIterator.next().toString());
/* 270 */       i--;
/* 271 */       if (i > 0) {
/* 272 */         localStringBuffer.append(", ");
/*     */       }
/*     */     }
/* 275 */     localStringBuffer.append("}");
/* 276 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.MethodSet
 * JD-Core Version:    0.6.2
 */