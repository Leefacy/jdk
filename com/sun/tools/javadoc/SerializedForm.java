/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ 
/*     */ class SerializedForm
/*     */ {
/*  71 */   ListBuffer<MethodDoc> methods = new ListBuffer();
/*     */ 
/*  78 */   private final ListBuffer<FieldDocImpl> fields = new ListBuffer();
/*     */ 
/*  83 */   private boolean definesSerializableFields = false;
/*     */   private static final String SERIALIZABLE_FIELDS = "serialPersistentFields";
/*     */   private static final String READOBJECT = "readObject";
/*     */   private static final String WRITEOBJECT = "writeObject";
/*     */   private static final String READRESOLVE = "readResolve";
/*     */   private static final String WRITEREPLACE = "writeReplace";
/*     */   private static final String READOBJECTNODATA = "readObjectNoData";
/*     */ 
/*     */   SerializedForm(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, ClassDocImpl paramClassDocImpl)
/*     */   {
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 101 */     if (paramClassDocImpl.isExternalizable())
/*     */     {
/* 105 */       localObject1 = new String[] { "java.io.ObjectInput" };
/* 106 */       localObject2 = new String[] { "java.io.ObjectOutput" };
/* 107 */       MethodDocImpl localMethodDocImpl = paramClassDocImpl.findMethod("readExternal", (String[])localObject1);
/* 108 */       if (localMethodDocImpl != null) {
/* 109 */         this.methods.append(localMethodDocImpl);
/*     */       }
/* 111 */       localMethodDocImpl = paramClassDocImpl.findMethod("writeExternal", (String[])localObject2);
/* 112 */       if (localMethodDocImpl != null) {
/* 113 */         this.methods.append(localMethodDocImpl);
/* 114 */         Tag[] arrayOfTag = localMethodDocImpl.tags("serialData");
/*     */       }
/*     */     }
/* 117 */     else if (paramClassDocImpl.isSerializable())
/*     */     {
/* 119 */       localObject1 = getDefinedSerializableFields(paramClassSymbol);
/* 120 */       if (localObject1 != null)
/*     */       {
/* 126 */         this.definesSerializableFields = true;
/*     */ 
/* 128 */         localObject2 = paramDocEnv.getFieldDoc((Symbol.VarSymbol)localObject1);
/* 129 */         this.fields.append(localObject2);
/* 130 */         mapSerialFieldTagImplsToFieldDocImpls((FieldDocImpl)localObject2, paramDocEnv, paramClassSymbol);
/*     */       }
/*     */       else
/*     */       {
/* 137 */         computeDefaultSerializableFields(paramDocEnv, paramClassSymbol, paramClassDocImpl);
/*     */       }
/*     */ 
/* 143 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readObject");
/* 144 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "writeObject");
/* 145 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readResolve");
/* 146 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "writeReplace");
/* 147 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readObjectNoData");
/*     */     }
/*     */   }
/*     */ 
/*     */   private Symbol.VarSymbol getDefinedSerializableFields(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 157 */     Names localNames = paramClassSymbol.name.table.names;
/*     */ 
/* 162 */     for (Scope.Entry localEntry = paramClassSymbol.members().lookup(localNames.fromString("serialPersistentFields")); localEntry.scope != null; localEntry = localEntry.next()) {
/* 163 */       if (localEntry.sym.kind == 4) {
/* 164 */         Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/* 165 */         if (((localVarSymbol.flags() & 0x8) != 0L) && 
/* 166 */           ((localVarSymbol
/* 166 */           .flags() & 0x2) != 0L)) {
/* 167 */           return localVarSymbol;
/*     */         }
/*     */       }
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   private void computeDefaultSerializableFields(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, ClassDocImpl paramClassDocImpl)
/*     */   {
/* 183 */     for (Scope.Entry localEntry = paramClassSymbol.members().elems; localEntry != null; localEntry = localEntry.sibling)
/* 184 */       if ((localEntry.sym != null) && (localEntry.sym.kind == 4)) {
/* 185 */         Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/* 186 */         if (((localVarSymbol.flags() & 0x8) == 0L) && 
/* 187 */           ((localVarSymbol
/* 187 */           .flags() & 0x80) == 0L))
/*     */         {
/* 189 */           FieldDocImpl localFieldDocImpl = paramDocEnv.getFieldDoc(localVarSymbol);
/*     */ 
/* 192 */           this.fields.prepend(localFieldDocImpl);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void addMethodIfExist(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, String paramString)
/*     */   {
/* 210 */     Names localNames = paramClassSymbol.name.table.names;
/*     */ 
/* 212 */     for (Scope.Entry localEntry = paramClassSymbol.members().lookup(localNames.fromString(paramString)); localEntry.scope != null; localEntry = localEntry.next())
/* 213 */       if (localEntry.sym.kind == 16) {
/* 214 */         Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.sym;
/* 215 */         if ((localMethodSymbol.flags() & 0x8) == 0L)
/*     */         {
/* 222 */           this.methods.append(paramDocEnv.getMethodDoc(localMethodSymbol));
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private void mapSerialFieldTagImplsToFieldDocImpls(FieldDocImpl paramFieldDocImpl, DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 236 */     Names localNames = paramClassSymbol.name.table.names;
/*     */ 
/* 238 */     SerialFieldTag[] arrayOfSerialFieldTag = paramFieldDocImpl.serialFieldTags();
/* 239 */     for (int i = 0; i < arrayOfSerialFieldTag.length; i++)
/* 240 */       if ((arrayOfSerialFieldTag[i].fieldName() != null) && (arrayOfSerialFieldTag[i].fieldType() != null))
/*     */       {
/* 243 */         Name localName = localNames.fromString(arrayOfSerialFieldTag[i].fieldName());
/*     */ 
/* 246 */         for (Scope.Entry localEntry = paramClassSymbol.members().lookup(localName); localEntry.scope != null; localEntry = localEntry.next())
/* 247 */           if (localEntry.sym.kind == 4) {
/* 248 */             Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/* 249 */             FieldDocImpl localFieldDocImpl = paramDocEnv.getFieldDoc(localVarSymbol);
/* 250 */             ((SerialFieldTagImpl)arrayOfSerialFieldTag[i]).mapToFieldDocImpl(localFieldDocImpl);
/* 251 */             break;
/*     */           }
/*     */       }
/*     */   }
/*     */ 
/*     */   FieldDoc[] fields()
/*     */   {
/* 269 */     return (FieldDoc[])this.fields.toArray(new FieldDocImpl[this.fields.length()]);
/*     */   }
/*     */ 
/*     */   MethodDoc[] methods()
/*     */   {
/* 278 */     return (MethodDoc[])this.methods.toArray(new MethodDoc[this.methods.length()]);
/*     */   }
/*     */ 
/*     */   boolean definesSerializableFields()
/*     */   {
/* 288 */     return this.definesSerializableFields;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.SerializedForm
 * JD-Core Version:    0.6.2
 */