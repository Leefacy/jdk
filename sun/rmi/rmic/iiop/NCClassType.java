/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class NCClassType extends ClassType
/*     */ {
/*     */   public static NCClassType forNCClass(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*     */   {
/*  65 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  67 */     int i = 0;
/*     */     try
/*     */     {
/*  71 */       sun.tools.java.Type localType = paramClassDefinition.getType();
/*  72 */       Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  74 */       if (localType1 != null)
/*     */       {
/*  76 */         if (!(localType1 instanceof NCClassType)) return null;
/*     */ 
/*  80 */         return (NCClassType)localType1;
/*     */       }
/*     */ 
/*  84 */       NCClassType localNCClassType = new NCClassType(paramContextStack, paramClassDefinition);
/*  85 */       putType(localType, localNCClassType, paramContextStack);
/*  86 */       paramContextStack.push(localNCClassType);
/*  87 */       i = 1;
/*     */ 
/*  89 */       if (localNCClassType.initialize(paramContextStack)) {
/*  90 */         paramContextStack.pop(true);
/*  91 */         return localNCClassType;
/*     */       }
/*  93 */       removeType(localType, paramContextStack);
/*  94 */       paramContextStack.pop(false);
/*  95 */       return null;
/*     */     }
/*     */     catch (CompilerError localCompilerError) {
/*  98 */       if (i != 0) paramContextStack.pop(false); 
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 107 */     return addExceptionDescription("Non-conforming class");
/*     */   }
/*     */ 
/*     */   private NCClassType(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 119 */     super(paramContextStack, paramClassDefinition, 100794368);
/*     */   }
/*     */ 
/*     */   private boolean initialize(ContextStack paramContextStack)
/*     */   {
/* 130 */     if (!initParents(paramContextStack)) {
/* 131 */       return false;
/*     */     }
/*     */ 
/* 134 */     if (paramContextStack.getEnv().getParseNonConforming())
/*     */     {
/* 136 */       Vector localVector1 = new Vector();
/* 137 */       Vector localVector2 = new Vector();
/* 138 */       Vector localVector3 = new Vector();
/*     */       try
/*     */       {
/* 144 */         if (addAllMethods(getClassDefinition(), localVector2, false, false, paramContextStack) != null)
/*     */         {
/* 148 */           if (updateParentClassMethods(getClassDefinition(), localVector2, false, paramContextStack) != null)
/*     */           {
/* 152 */             if (addConformingConstants(localVector3, false, paramContextStack))
/*     */             {
/* 156 */               if (!initialize(localVector1, localVector2, localVector3, paramContextStack, false)) {
/* 157 */                 return false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 162 */         return true;
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound) {
/* 165 */         classNotFound(paramContextStack, localClassNotFound);
/*     */ 
/* 167 */         return false;
/*     */       }
/*     */     }
/* 169 */     return initialize(null, null, null, paramContextStack, false);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.NCClassType
 * JD-Core Version:    0.6.2
 */