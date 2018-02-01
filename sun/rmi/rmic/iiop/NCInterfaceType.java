/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ public class NCInterfaceType extends InterfaceType
/*     */ {
/*     */   public static NCInterfaceType forNCInterface(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*     */   {
/*  61 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*  63 */     int i = 0;
/*     */     try
/*     */     {
/*  67 */       sun.tools.java.Type localType = paramClassDefinition.getType();
/*  68 */       Type localType1 = getType(localType, paramContextStack);
/*     */ 
/*  70 */       if (localType1 != null)
/*     */       {
/*  72 */         if (!(localType1 instanceof NCInterfaceType)) return null;
/*     */ 
/*  76 */         return (NCInterfaceType)localType1;
/*     */       }
/*     */ 
/*  79 */       NCInterfaceType localNCInterfaceType = new NCInterfaceType(paramContextStack, paramClassDefinition);
/*  80 */       putType(localType, localNCInterfaceType, paramContextStack);
/*  81 */       paramContextStack.push(localNCInterfaceType);
/*  82 */       i = 1;
/*     */ 
/*  84 */       if (localNCInterfaceType.initialize(paramContextStack)) {
/*  85 */         paramContextStack.pop(true);
/*  86 */         return localNCInterfaceType;
/*     */       }
/*  88 */       removeType(localType, paramContextStack);
/*  89 */       paramContextStack.pop(false);
/*  90 */       return null;
/*     */     }
/*     */     catch (CompilerError localCompilerError) {
/*  93 */       if (i != 0) paramContextStack.pop(false); 
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription()
/*     */   {
/* 102 */     return "Non-conforming interface";
/*     */   }
/*     */ 
/*     */   private NCInterfaceType(ContextStack paramContextStack, ClassDefinition paramClassDefinition)
/*     */   {
/* 114 */     super(paramContextStack, paramClassDefinition, 167788544);
/*     */   }
/*     */ 
/*     */   private boolean initialize(ContextStack paramContextStack)
/*     */   {
/* 126 */     if (paramContextStack.getEnv().getParseNonConforming())
/*     */     {
/* 128 */       Vector localVector1 = new Vector();
/* 129 */       Vector localVector2 = new Vector();
/* 130 */       Vector localVector3 = new Vector();
/*     */       try
/*     */       {
/* 135 */         addNonRemoteInterfaces(localVector1, paramContextStack);
/*     */ 
/* 139 */         if (addAllMethods(getClassDefinition(), localVector2, false, false, paramContextStack) != null)
/*     */         {
/* 143 */           if (addConformingConstants(localVector3, false, paramContextStack))
/*     */           {
/* 147 */             if (!initialize(localVector1, localVector2, localVector3, paramContextStack, false)) {
/* 148 */               return false;
/*     */             }
/*     */           }
/*     */         }
/* 152 */         return true;
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound) {
/* 155 */         classNotFound(paramContextStack, localClassNotFound);
/*     */ 
/* 157 */         return false;
/*     */       }
/*     */     }
/* 159 */     return initialize(null, null, null, paramContextStack, false);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.NCInterfaceType
 * JD-Core Version:    0.6.2
 */