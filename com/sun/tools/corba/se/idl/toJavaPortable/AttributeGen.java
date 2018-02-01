/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabFactory;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AttributeGen extends MethodGen
/*     */   implements com.sun.tools.corba.se.idl.AttributeGen
/*     */ {
/* 171 */   private SymtabEntry realType = null;
/*     */ 
/*     */   private boolean unique(InterfaceEntry paramInterfaceEntry, String paramString)
/*     */   {
/*  68 */     Enumeration localEnumeration = paramInterfaceEntry.methods().elements();
/*  69 */     while (localEnumeration.hasMoreElements())
/*     */     {
/*  71 */       localObject = (SymtabEntry)localEnumeration.nextElement();
/*  72 */       if (paramString.equals(((SymtabEntry)localObject).name())) {
/*  73 */         return false;
/*     */       }
/*     */     }
/*     */ 
/*  77 */     Object localObject = paramInterfaceEntry.derivedFrom().elements();
/*  78 */     while (((Enumeration)localObject).hasMoreElements()) {
/*  79 */       if (!unique((InterfaceEntry)((Enumeration)localObject).nextElement(), paramString)) {
/*  80 */         return false;
/*     */       }
/*     */     }
/*     */ 
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, AttributeEntry paramAttributeEntry, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 100 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/* 103 */     super.interfaceMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/*     */ 
/* 106 */     if (!localAttributeEntry.readOnly())
/*     */     {
/* 108 */       setupForSetMethod();
/* 109 */       super.interfaceMethod(paramHashtable, localAttributeEntry, paramPrintWriter);
/* 110 */       clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void stub(String paramString, boolean paramBoolean, Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*     */   {
/* 119 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/* 122 */     super.stub(paramString, paramBoolean, paramHashtable, localAttributeEntry, paramPrintWriter, paramInt);
/*     */ 
/* 125 */     if (!localAttributeEntry.readOnly())
/*     */     {
/* 127 */       setupForSetMethod();
/* 128 */       super.stub(paramString, paramBoolean, paramHashtable, localAttributeEntry, paramPrintWriter, paramInt + 1);
/* 129 */       clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void skeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*     */   {
/* 138 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/* 141 */     super.skeleton(paramHashtable, localAttributeEntry, paramPrintWriter, paramInt);
/*     */ 
/* 144 */     if (!localAttributeEntry.readOnly())
/*     */     {
/* 146 */       setupForSetMethod();
/* 147 */       super.skeleton(paramHashtable, localAttributeEntry, paramPrintWriter, paramInt + 1);
/* 148 */       clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void dispatchSkeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*     */   {
/* 157 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/* 160 */     super.dispatchSkeleton(paramHashtable, localAttributeEntry, paramPrintWriter, paramInt);
/*     */ 
/* 163 */     if (!localAttributeEntry.readOnly())
/*     */     {
/* 165 */       setupForSetMethod();
/* 166 */       super.dispatchSkeleton(paramHashtable, paramMethodEntry, paramPrintWriter, paramInt + 1);
/* 167 */       clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setupForSetMethod()
/*     */   {
/* 178 */     ParameterEntry localParameterEntry = Compile.compiler.factory.parameterEntry();
/* 179 */     localParameterEntry.type(this.m.type());
/* 180 */     localParameterEntry.name("new" + Util.capitalize(this.m.name()));
/* 181 */     this.m.parameters().addElement(localParameterEntry);
/* 182 */     this.realType = this.m.type();
/* 183 */     this.m.type(null);
/*     */   }
/*     */ 
/*     */   protected void clear()
/*     */   {
/* 192 */     this.m.parameters().removeAllElements();
/* 193 */     this.m.type(this.realType);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.AttributeGen
 * JD-Core Version:    0.6.2
 */