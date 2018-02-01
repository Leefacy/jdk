/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import sun.tools.tree.BooleanExpression;
/*     */ import sun.tools.tree.DoubleExpression;
/*     */ import sun.tools.tree.Expression;
/*     */ import sun.tools.tree.FloatExpression;
/*     */ import sun.tools.tree.IntExpression;
/*     */ import sun.tools.tree.LocalMember;
/*     */ import sun.tools.tree.LongExpression;
/*     */ import sun.tools.tree.Node;
/*     */ import sun.tools.tree.StringExpression;
/*     */ 
/*     */ public final class BinaryMember extends MemberDefinition
/*     */ {
/*     */   Expression value;
/*     */   BinaryAttribute atts;
/* 146 */   private boolean isConstantCache = false;
/* 147 */   private boolean isConstantCached = false;
/*     */ 
/*     */   public BinaryMember(ClassDefinition paramClassDefinition, int paramInt, Type paramType, Identifier paramIdentifier, BinaryAttribute paramBinaryAttribute)
/*     */   {
/*  52 */     super(0L, paramClassDefinition, paramInt, paramType, paramIdentifier, null, null);
/*  53 */     this.atts = paramBinaryAttribute;
/*     */ 
/*  56 */     if (getAttribute(idDeprecated) != null) {
/*  57 */       this.modifiers |= 262144;
/*     */     }
/*     */ 
/*  61 */     if (getAttribute(idSynthetic) != null)
/*  62 */       this.modifiers |= 524288;
/*     */   }
/*     */ 
/*     */   public BinaryMember(ClassDefinition paramClassDefinition)
/*     */   {
/*  70 */     super(paramClassDefinition);
/*     */   }
/*     */ 
/*     */   public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean)
/*     */   {
/*  79 */     return (isConstructor()) && (getClassDefinition().getSuperClass() == null);
/*     */   }
/*     */ 
/*     */   public Vector getArguments()
/*     */   {
/*  86 */     if ((isConstructor()) && (getClassDefinition().getSuperClass() == null)) {
/*  87 */       Vector localVector = new Vector();
/*  88 */       localVector.addElement(new LocalMember(0L, getClassDefinition(), 0, 
/*  89 */         getClassDefinition().getType(), idThis));
/*  90 */       return localVector;
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public ClassDeclaration[] getExceptions(Environment paramEnvironment)
/*     */   {
/*  99 */     if ((!isMethod()) || (this.exp != null)) {
/* 100 */       return this.exp;
/*     */     }
/* 102 */     byte[] arrayOfByte = getAttribute(idExceptions);
/* 103 */     if (arrayOfByte == null) {
/* 104 */       return new ClassDeclaration[0];
/*     */     }
/*     */     try
/*     */     {
/* 108 */       BinaryConstantPool localBinaryConstantPool = ((BinaryClass)getClassDefinition()).getConstants();
/* 109 */       DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
/*     */ 
/* 111 */       int i = localDataInputStream.readUnsignedShort();
/* 112 */       this.exp = new ClassDeclaration[i];
/* 113 */       for (int j = 0; j < i; j++)
/*     */       {
/* 115 */         this.exp[j] = localBinaryConstantPool.getDeclaration(paramEnvironment, localDataInputStream.readUnsignedShort());
/*     */       }
/* 117 */       return this.exp;
/*     */     } catch (IOException localIOException) {
/* 119 */       throw new CompilerError(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getDocumentation()
/*     */   {
/* 127 */     if (this.documentation != null) {
/* 128 */       return this.documentation;
/*     */     }
/* 130 */     byte[] arrayOfByte = getAttribute(idDocumentation);
/* 131 */     if (arrayOfByte == null)
/* 132 */       return null;
/*     */     try
/*     */     {
/* 135 */       return this.documentation = new DataInputStream(new ByteArrayInputStream(arrayOfByte)).readUTF();
/*     */     } catch (IOException localIOException) {
/* 137 */       throw new CompilerError(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isConstant()
/*     */   {
/* 149 */     if (!this.isConstantCached) {
/* 150 */       this.isConstantCache = ((isFinal()) && 
/* 151 */         (isVariable()) && 
/* 152 */         (getAttribute(idConstantValue) != null));
/*     */ 
/* 153 */       this.isConstantCached = true;
/*     */     }
/* 155 */     return this.isConstantCache;
/*     */   }
/*     */ 
/*     */   public Node getValue(Environment paramEnvironment)
/*     */   {
/* 162 */     if (isMethod()) {
/* 163 */       return null;
/*     */     }
/* 165 */     if (!isFinal()) {
/* 166 */       return null;
/*     */     }
/* 168 */     if (getValue() != null) {
/* 169 */       return (Expression)getValue();
/*     */     }
/* 171 */     byte[] arrayOfByte = getAttribute(idConstantValue);
/* 172 */     if (arrayOfByte == null) {
/* 173 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 177 */       BinaryConstantPool localBinaryConstantPool = ((BinaryClass)getClassDefinition()).getConstants();
/*     */ 
/* 179 */       Object localObject = localBinaryConstantPool.getValue(new DataInputStream(new ByteArrayInputStream(arrayOfByte)).readUnsignedShort());
/* 180 */       switch (getType().getTypeCode()) {
/*     */       case 0:
/* 182 */         setValue(new BooleanExpression(0L, ((Number)localObject).intValue() != 0));
/* 183 */         break;
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 188 */         setValue(new IntExpression(0L, ((Number)localObject).intValue()));
/* 189 */         break;
/*     */       case 5:
/* 191 */         setValue(new LongExpression(0L, ((Number)localObject).longValue()));
/* 192 */         break;
/*     */       case 6:
/* 194 */         setValue(new FloatExpression(0L, ((Number)localObject).floatValue()));
/* 195 */         break;
/*     */       case 7:
/* 197 */         setValue(new DoubleExpression(0L, ((Number)localObject).doubleValue()));
/* 198 */         break;
/*     */       case 10:
/* 200 */         setValue(new StringExpression(0L, (String)localBinaryConstantPool.getValue(((Number)localObject).intValue())));
/*     */       case 8:
/*     */       case 9:
/* 203 */       }return (Expression)getValue();
/*     */     } catch (IOException localIOException) {
/* 205 */       throw new CompilerError(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getAttribute(Identifier paramIdentifier)
/*     */   {
/* 213 */     for (BinaryAttribute localBinaryAttribute = this.atts; localBinaryAttribute != null; localBinaryAttribute = localBinaryAttribute.next) {
/* 214 */       if (localBinaryAttribute.name.equals(paramIdentifier)) {
/* 215 */         return localBinaryAttribute.data;
/*     */       }
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean deleteAttribute(Identifier paramIdentifier) {
/* 222 */     Object localObject = null; BinaryAttribute localBinaryAttribute = null;
/*     */ 
/* 224 */     boolean bool = false;
/*     */ 
/* 226 */     while (this.atts.name.equals(paramIdentifier)) {
/* 227 */       this.atts = this.atts.next;
/* 228 */       bool = true;
/*     */     }
/* 230 */     for (localObject = this.atts; localObject != null; localObject = localBinaryAttribute) {
/* 231 */       localBinaryAttribute = ((BinaryAttribute)localObject).next;
/* 232 */       if ((localBinaryAttribute != null) && 
/* 233 */         (localBinaryAttribute.name.equals(paramIdentifier))) {
/* 234 */         ((BinaryAttribute)localObject).next = localBinaryAttribute.next;
/* 235 */         localBinaryAttribute = localBinaryAttribute.next;
/* 236 */         bool = true;
/*     */       }
/*     */     }
/*     */ 
/* 240 */     for (localObject = this.atts; localObject != null; localObject = ((BinaryAttribute)localObject).next) {
/* 241 */       if (((BinaryAttribute)localObject).name.equals(paramIdentifier)) {
/* 242 */         throw new InternalError("Found attribute " + paramIdentifier);
/*     */       }
/*     */     }
/*     */ 
/* 246 */     return bool;
/*     */   }
/*     */ 
/*     */   public void addAttribute(Identifier paramIdentifier, byte[] paramArrayOfByte, Environment paramEnvironment)
/*     */   {
/* 255 */     this.atts = new BinaryAttribute(paramIdentifier, paramArrayOfByte, this.atts);
/*     */ 
/* 257 */     ((BinaryClass)this.clazz).cpool.indexString(paramIdentifier.toString(), paramEnvironment);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryMember
 * JD-Core Version:    0.6.2
 */