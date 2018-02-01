/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.RuntimeConstants;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.StringExpression;
/*     */ 
/*     */ public final class ConstantPool
/*     */   implements RuntimeConstants
/*     */ {
/*  45 */   Hashtable<Object, ConstantPoolData> hash = new Hashtable(101);
/*     */ 
/*     */   public int index(Object paramObject)
/*     */   {
/*  51 */     return ((ConstantPoolData)this.hash.get(paramObject)).index;
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject)
/*     */   {
/*  58 */     Object localObject = (ConstantPoolData)this.hash.get(paramObject);
/*  59 */     if (localObject == null) {
/*  60 */       if ((paramObject instanceof String))
/*  61 */         localObject = new StringConstantData(this, (String)paramObject);
/*  62 */       else if ((paramObject instanceof StringExpression))
/*  63 */         localObject = new StringExpressionConstantData(this, (StringExpression)paramObject);
/*  64 */       else if ((paramObject instanceof ClassDeclaration))
/*  65 */         localObject = new ClassConstantData(this, (ClassDeclaration)paramObject);
/*  66 */       else if ((paramObject instanceof Type))
/*  67 */         localObject = new ClassConstantData(this, (Type)paramObject);
/*  68 */       else if ((paramObject instanceof MemberDefinition))
/*  69 */         localObject = new FieldConstantData(this, (MemberDefinition)paramObject);
/*  70 */       else if ((paramObject instanceof NameAndTypeData))
/*  71 */         localObject = new NameAndTypeConstantData(this, (NameAndTypeData)paramObject);
/*  72 */       else if ((paramObject instanceof Number)) {
/*  73 */         localObject = new NumberConstantData(this, (Number)paramObject);
/*     */       }
/*  75 */       this.hash.put(paramObject, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/*  83 */     ConstantPoolData[] arrayOfConstantPoolData = new ConstantPoolData[this.hash.size()];
/*  84 */     String[] arrayOfString = new String[arrayOfConstantPoolData.length];
/*  85 */     int i = 1; int j = 0;
/*     */ 
/*  88 */     for (int k = 0; k < 5; k++) {
/*  89 */       int m = j;
/*  90 */       for (Enumeration localEnumeration = this.hash.elements(); localEnumeration.hasMoreElements(); ) {
/*  91 */         ConstantPoolData localConstantPoolData2 = (ConstantPoolData)localEnumeration.nextElement();
/*  92 */         if (localConstantPoolData2.order() == k) {
/*  93 */           arrayOfString[j] = sortKey(localConstantPoolData2);
/*  94 */           arrayOfConstantPoolData[(j++)] = localConstantPoolData2;
/*     */         }
/*     */       }
/*  97 */       xsort(arrayOfConstantPoolData, arrayOfString, m, j - 1);
/*     */     }
/*     */ 
/* 101 */     for (k = 0; k < arrayOfConstantPoolData.length; k++) {
/* 102 */       ConstantPoolData localConstantPoolData1 = arrayOfConstantPoolData[k];
/* 103 */       localConstantPoolData1.index = i;
/* 104 */       i += localConstantPoolData1.width();
/*     */     }
/*     */ 
/* 108 */     paramDataOutputStream.writeShort(i);
/*     */ 
/* 111 */     for (k = 0; k < j; k++)
/* 112 */       arrayOfConstantPoolData[k].write(paramEnvironment, paramDataOutputStream, this);
/*     */   }
/*     */ 
/*     */   private static String sortKey(ConstantPoolData paramConstantPoolData)
/*     */   {
/*     */     Object localObject;
/* 118 */     if ((paramConstantPoolData instanceof NumberConstantData)) {
/* 119 */       localObject = ((NumberConstantData)paramConstantPoolData).num;
/* 120 */       String str = localObject.toString();
/* 121 */       int i = 3;
/* 122 */       if ((localObject instanceof Integer)) i = 0;
/* 123 */       else if ((localObject instanceof Float)) i = 1;
/* 124 */       else if ((localObject instanceof Long)) i = 2;
/* 125 */       return "" + (char)(str.length() + i << 8) + str;
/*     */     }
/* 127 */     if ((paramConstantPoolData instanceof StringExpressionConstantData))
/* 128 */       return (String)((StringExpressionConstantData)paramConstantPoolData).str.getValue();
/* 129 */     if ((paramConstantPoolData instanceof FieldConstantData)) {
/* 130 */       localObject = ((FieldConstantData)paramConstantPoolData).field;
/*     */ 
/* 132 */       return ((MemberDefinition)localObject).getName() + " " + ((MemberDefinition)localObject).getType().getTypeSignature() + " " + ((MemberDefinition)localObject)
/* 132 */         .getClassDeclaration().getName();
/*     */     }
/* 134 */     if ((paramConstantPoolData instanceof NameAndTypeConstantData)) {
/* 135 */       return ((NameAndTypeConstantData)paramConstantPoolData).name + " " + ((NameAndTypeConstantData)paramConstantPoolData).type;
/*     */     }
/* 137 */     if ((paramConstantPoolData instanceof ClassConstantData))
/* 138 */       return ((ClassConstantData)paramConstantPoolData).name;
/* 139 */     return ((StringConstantData)paramConstantPoolData).str;
/*     */   }
/*     */ 
/*     */   private static void xsort(ConstantPoolData[] paramArrayOfConstantPoolData, String[] paramArrayOfString, int paramInt1, int paramInt2)
/*     */   {
/* 148 */     if (paramInt1 >= paramInt2)
/* 149 */       return;
/* 150 */     String str1 = paramArrayOfString[paramInt1];
/* 151 */     int i = paramInt1;
/* 152 */     int j = paramInt2;
/* 153 */     while (i < j) {
/* 154 */       while ((i <= paramInt2) && (paramArrayOfString[i].compareTo(str1) <= 0))
/* 155 */         i++;
/* 156 */       while ((j >= paramInt1) && (paramArrayOfString[j].compareTo(str1) > 0))
/* 157 */         j--;
/* 158 */       if (i < j)
/*     */       {
/* 160 */         ConstantPoolData localConstantPoolData = paramArrayOfConstantPoolData[i];
/* 161 */         localObject = paramArrayOfString[i];
/* 162 */         paramArrayOfConstantPoolData[i] = paramArrayOfConstantPoolData[j]; paramArrayOfConstantPoolData[j] = localConstantPoolData;
/* 163 */         paramArrayOfString[i] = paramArrayOfString[j]; paramArrayOfString[j] = localObject;
/*     */       }
/*     */     }
/* 166 */     int k = j;
/*     */ 
/* 168 */     Object localObject = paramArrayOfConstantPoolData[paramInt1];
/* 169 */     String str2 = paramArrayOfString[paramInt1];
/* 170 */     paramArrayOfConstantPoolData[paramInt1] = paramArrayOfConstantPoolData[k]; paramArrayOfConstantPoolData[k] = localObject;
/* 171 */     paramArrayOfString[paramInt1] = paramArrayOfString[k]; paramArrayOfString[k] = str2;
/* 172 */     xsort(paramArrayOfConstantPoolData, paramArrayOfString, paramInt1, k - 1);
/* 173 */     xsort(paramArrayOfConstantPoolData, paramArrayOfString, k + 1, paramInt2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.ConstantPool
 * JD-Core Version:    0.6.2
 */