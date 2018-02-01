/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class NameContext
/*     */ {
/*     */   private Hashtable table;
/*     */   private boolean allowCollisions;
/*     */ 
/*     */   public static synchronized NameContext forName(String paramString, boolean paramBoolean, BatchEnvironment paramBatchEnvironment)
/*     */   {
/*  56 */     NameContext localNameContext = null;
/*     */ 
/*  60 */     if (paramString == null)
/*     */     {
/*  64 */       paramString = "null";
/*     */     }
/*     */ 
/*  69 */     if (paramBatchEnvironment.nameContexts == null)
/*     */     {
/*  73 */       paramBatchEnvironment.nameContexts = new Hashtable();
/*     */     }
/*     */     else
/*     */     {
/*  80 */       localNameContext = (NameContext)paramBatchEnvironment.nameContexts.get(paramString);
/*     */     }
/*     */ 
/*  85 */     if (localNameContext == null)
/*     */     {
/*  89 */       localNameContext = new NameContext(paramBoolean);
/*     */ 
/*  91 */       paramBatchEnvironment.nameContexts.put(paramString, localNameContext);
/*     */     }
/*     */ 
/*  94 */     return localNameContext;
/*     */   }
/*     */ 
/*     */   public NameContext(boolean paramBoolean)
/*     */   {
/* 103 */     this.allowCollisions = paramBoolean;
/* 104 */     this.table = new Hashtable();
/*     */   }
/*     */ 
/*     */   public void assertPut(String paramString)
/*     */     throws Exception
/*     */   {
/* 114 */     String str = add(paramString);
/*     */ 
/* 116 */     if (str != null)
/* 117 */       throw new Exception(str);
/*     */   }
/*     */ 
/*     */   public void put(String paramString)
/*     */   {
/* 126 */     if (!this.allowCollisions) {
/* 127 */       throw new Error("Must use assertPut(name)");
/*     */     }
/*     */ 
/* 130 */     add(paramString);
/*     */   }
/*     */ 
/*     */   private String add(String paramString)
/*     */   {
/* 142 */     String str = paramString.toLowerCase();
/*     */ 
/* 146 */     Name localName = (Name)this.table.get(str);
/*     */ 
/* 148 */     if (localName != null)
/*     */     {
/* 153 */       if (!paramString.equals(localName.name))
/*     */       {
/* 158 */         if (this.allowCollisions)
/*     */         {
/* 162 */           localName.collisions = true;
/*     */         }
/*     */         else
/*     */         {
/* 168 */           return new String("\"" + paramString + "\" and \"" + localName.name + "\"");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 175 */       this.table.put(str, new Name(paramString, false));
/*     */     }
/*     */ 
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   public String get(String paramString)
/*     */   {
/* 187 */     Name localName = (Name)this.table.get(paramString.toLowerCase());
/* 188 */     String str = paramString;
/*     */ 
/* 192 */     if (localName.collisions)
/*     */     {
/* 196 */       int i = paramString.length();
/* 197 */       int j = 1;
/*     */ 
/* 199 */       for (int k = 0; k < i; k++)
/*     */       {
/* 201 */         if (Character.isUpperCase(paramString.charAt(k))) {
/* 202 */           str = str + "_";
/* 203 */           str = str + k;
/* 204 */           j = 0;
/*     */         }
/*     */       }
/*     */ 
/* 208 */       if (j != 0) {
/* 209 */         str = str + "_";
/*     */       }
/*     */     }
/*     */ 
/* 213 */     return str;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 220 */     this.table.clear();
/*     */   }
/*     */   public class Name {
/*     */     public String name;
/*     */     public boolean collisions;
/*     */ 
/*     */     public Name(String paramBoolean, boolean arg3) {
/* 228 */       this.name = paramBoolean;
/*     */       boolean bool;
/* 229 */       this.collisions = bool;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.NameContext
 * JD-Core Version:    0.6.2
 */