/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ForwardGen;
/*     */ import com.sun.tools.corba.se.idl.ForwardValueGen;
/*     */ import com.sun.tools.corba.se.idl.IncludeGen;
/*     */ import com.sun.tools.corba.se.idl.ParameterGen;
/*     */ import com.sun.tools.corba.se.idl.PragmaGen;
/*     */ 
/*     */ public class GenFactory
/*     */   implements com.sun.tools.corba.se.idl.GenFactory
/*     */ {
/*     */   public com.sun.tools.corba.se.idl.AttributeGen createAttributeGen()
/*     */   {
/*  48 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/*  49 */       return new AttributeGen24();
/*     */     }
/*  51 */     return new AttributeGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.ConstGen createConstGen()
/*     */   {
/*  56 */     return new ConstGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.NativeGen createNativeGen()
/*     */   {
/*  61 */     return new NativeGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.EnumGen createEnumGen()
/*     */   {
/*  66 */     return new EnumGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.ExceptionGen createExceptionGen()
/*     */   {
/*  71 */     return new ExceptionGen();
/*     */   }
/*     */ 
/*     */   public ForwardGen createForwardGen()
/*     */   {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public ForwardValueGen createForwardValueGen()
/*     */   {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public IncludeGen createIncludeGen()
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.InterfaceGen createInterfaceGen()
/*     */   {
/*  91 */     return new InterfaceGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.ValueGen createValueGen()
/*     */   {
/*  96 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/*  97 */       return new ValueGen24();
/*     */     }
/*  99 */     return new ValueGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.ValueBoxGen createValueBoxGen()
/*     */   {
/* 104 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/* 105 */       return new ValueBoxGen24();
/*     */     }
/* 107 */     return new ValueBoxGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.MethodGen createMethodGen()
/*     */   {
/* 112 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/* 113 */       return new MethodGen24();
/*     */     }
/* 115 */     return new MethodGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.ModuleGen createModuleGen()
/*     */   {
/* 120 */     return new ModuleGen();
/*     */   }
/*     */ 
/*     */   public ParameterGen createParameterGen()
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public PragmaGen createPragmaGen()
/*     */   {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.PrimitiveGen createPrimitiveGen()
/*     */   {
/* 135 */     return new PrimitiveGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.SequenceGen createSequenceGen()
/*     */   {
/* 140 */     return new SequenceGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.StringGen createStringGen()
/*     */   {
/* 145 */     return new StringGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.StructGen createStructGen()
/*     */   {
/* 150 */     return new StructGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.TypedefGen createTypedefGen()
/*     */   {
/* 155 */     return new TypedefGen();
/*     */   }
/*     */ 
/*     */   public com.sun.tools.corba.se.idl.UnionGen createUnionGen()
/*     */   {
/* 160 */     return new UnionGen();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.GenFactory
 * JD-Core Version:    0.6.2
 */