/*      */ package com.sun.tools.example.debug.expr;
/*      */ 
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ArrayReference;
/*      */ import com.sun.jdi.ArrayType;
/*      */ import com.sun.jdi.BooleanType;
/*      */ import com.sun.jdi.BooleanValue;
/*      */ import com.sun.jdi.ByteValue;
/*      */ import com.sun.jdi.CharValue;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.ClassType;
/*      */ import com.sun.jdi.DoubleValue;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.FloatValue;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.IntegerValue;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InvalidTypeException;
/*      */ import com.sun.jdi.InvocationException;
/*      */ import com.sun.jdi.LocalVariable;
/*      */ import com.sun.jdi.LongValue;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.PrimitiveType;
/*      */ import com.sun.jdi.PrimitiveValue;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.ShortValue;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.StringReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ abstract class LValue
/*      */ {
/*      */   protected Value jdiValue;
/*      */   static final int STATIC = 0;
/*      */   static final int INSTANCE = 1;
/*  219 */   static List<String> primitiveTypeNames = new ArrayList();
/*      */   static final int SAME = 0;
/*      */   static final int ASSIGNABLE = 1;
/*      */   static final int DIFFERENT = 2;
/*      */ 
/*      */   abstract Value getValue()
/*      */     throws InvocationException, IncompatibleThreadStateException, InvalidTypeException, ClassNotLoadedException, ParseException;
/*      */ 
/*      */   abstract void setValue0(Value paramValue)
/*      */     throws ParseException, InvalidTypeException, ClassNotLoadedException;
/*      */ 
/*      */   abstract void invokeWith(List<Value> paramList)
/*      */     throws ParseException;
/*      */ 
/*      */   void setValue(Value paramValue)
/*      */     throws ParseException
/*      */   {
/*      */     try
/*      */     {
/*   73 */       setValue0(paramValue);
/*      */     } catch (InvalidTypeException localInvalidTypeException) {
/*   75 */       throw new ParseException("Attempt to set value of incorrect type" + localInvalidTypeException);
/*      */     }
/*      */     catch (ClassNotLoadedException localClassNotLoadedException)
/*      */     {
/*   80 */       throw new ParseException("Attempt to set value before " + localClassNotLoadedException
/*   80 */         .className() + " was loaded" + localClassNotLoadedException);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setValue(LValue paramLValue) throws ParseException
/*      */   {
/*   86 */     setValue(paramLValue.interiorGetValue());
/*      */   }
/*      */ 
/*      */   LValue memberLValue(ExpressionParser.GetFrame paramGetFrame, String paramString) throws ParseException
/*      */   {
/*      */     try {
/*   92 */       return memberLValue(paramString, paramGetFrame.get().thread()); } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*      */     }
/*   94 */     throw new ParseException("Thread not suspended");
/*      */   }
/*      */ 
/*      */   LValue memberLValue(String paramString, ThreadReference paramThreadReference)
/*      */     throws ParseException
/*      */   {
/*  100 */     Value localValue = interiorGetValue();
/*  101 */     if (((localValue instanceof ArrayReference)) && 
/*  102 */       ("length"
/*  102 */       .equals(paramString)))
/*      */     {
/*  103 */       return new LValueArrayLength((ArrayReference)localValue);
/*      */     }
/*  105 */     return new LValueInstanceMember(localValue, paramString, paramThreadReference);
/*      */   }
/*      */ 
/*      */   Value getMassagedValue(ExpressionParser.GetFrame paramGetFrame)
/*      */     throws ParseException
/*      */   {
/*  111 */     Value localValue = interiorGetValue();
/*      */ 
/*  115 */     if (((localValue instanceof ObjectReference)) && (!(localValue instanceof StringReference)) && (!(localValue instanceof ArrayReference)))
/*      */     {
/*      */       StackFrame localStackFrame;
/*      */       try
/*      */       {
/*  120 */         localStackFrame = paramGetFrame.get();
/*      */       } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  122 */         throw new ParseException("Thread not suspended");
/*      */       }
/*      */ 
/*  125 */       ThreadReference localThreadReference = localStackFrame.thread();
/*  126 */       LValue localLValue = memberLValue("toString", localThreadReference);
/*  127 */       localLValue.invokeWith(new ArrayList());
/*  128 */       return localLValue.interiorGetValue();
/*      */     }
/*  130 */     return localValue;
/*      */   }
/*      */ 
/*      */   Value interiorGetValue() throws ParseException {
/*      */     Value localValue;
/*      */     try {
/*  136 */       localValue = getValue();
/*      */     }
/*      */     catch (InvocationException localInvocationException) {
/*  139 */       throw new ParseException("Unable to complete expression. Exception " + localInvocationException
/*  139 */         .exception() + " thrown");
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  141 */       throw new ParseException("Unable to complete expression. Thread not suspended for method invoke");
/*      */     }
/*      */     catch (InvalidTypeException localInvalidTypeException) {
/*  144 */       throw new ParseException("Unable to complete expression. Method argument type mismatch");
/*      */     }
/*      */     catch (ClassNotLoadedException localClassNotLoadedException)
/*      */     {
/*  148 */       throw new ParseException("Unable to complete expression. Method argument type " + localClassNotLoadedException
/*  148 */         .className() + " not yet loaded");
/*      */     }
/*      */ 
/*  151 */     return localValue;
/*      */   }
/*      */ 
/*      */   LValue arrayElementLValue(LValue paramLValue) throws ParseException {
/*  155 */     Value localValue = paramLValue.interiorGetValue();
/*      */     int i;
/*  157 */     if (((localValue instanceof IntegerValue)) || ((localValue instanceof ShortValue)) || ((localValue instanceof ByteValue)) || ((localValue instanceof CharValue)))
/*      */     {
/*  161 */       i = ((PrimitiveValue)localValue).intValue();
/*      */     }
/*  163 */     else throw new ParseException("Array index must be a integer type");
/*      */ 
/*  165 */     return new LValueArrayElement(interiorGetValue(), i);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try {
/*  171 */       return interiorGetValue().toString(); } catch (ParseException localParseException) {
/*      */     }
/*  173 */     return "<Parse Exception>";
/*      */   }
/*      */ 
/*      */   static Field fieldByName(ReferenceType paramReferenceType, String paramString, int paramInt)
/*      */   {
/*  187 */     Field localField = paramReferenceType.fieldByName(paramString);
/*  188 */     if (localField != null) {
/*  189 */       boolean bool = localField.isStatic();
/*  190 */       if (((paramInt == 0) && (!bool)) || ((paramInt == 1) && (bool)))
/*      */       {
/*  192 */         localField = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  201 */     return localField;
/*      */   }
/*      */ 
/*      */   static List<Method> methodsByName(ReferenceType paramReferenceType, String paramString, int paramInt)
/*      */   {
/*  206 */     List localList = paramReferenceType.methodsByName(paramString);
/*  207 */     Iterator localIterator = localList.iterator();
/*  208 */     while (localIterator.hasNext()) {
/*  209 */       Method localMethod = (Method)localIterator.next();
/*  210 */       boolean bool = localMethod.isStatic();
/*  211 */       if (((paramInt == 0) && (!bool)) || ((paramInt == 1) && (bool)))
/*      */       {
/*  213 */         localIterator.remove();
/*      */       }
/*      */     }
/*  216 */     return localList;
/*      */   }
/*      */ 
/*      */   static int argumentsMatch(List<Type> paramList, List<Value> paramList1)
/*      */   {
/*  246 */     if (paramList.size() != paramList1.size()) {
/*  247 */       return 2;
/*      */     }
/*      */ 
/*  250 */     Iterator localIterator1 = paramList.iterator();
/*  251 */     Iterator localIterator2 = paramList1.iterator();
/*  252 */     int i = 0;
/*      */ 
/*  257 */     while (localIterator1.hasNext()) {
/*  258 */       Type localType = (Type)localIterator1.next();
/*  259 */       Value localValue = (Value)localIterator2.next();
/*  260 */       if (localValue == null)
/*      */       {
/*  262 */         if (primitiveTypeNames.contains(localType.name())) {
/*  263 */           return 2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  268 */       if (!localValue.type().equals(localType)) {
/*  269 */         if (isAssignableTo(localValue.type(), localType))
/*  270 */           i = 1;
/*      */         else {
/*  272 */           return 2;
/*      */         }
/*      */       }
/*      */     }
/*  276 */     return i;
/*      */   }
/*      */ 
/*      */   static boolean isComponentAssignable(Type paramType1, Type paramType2)
/*      */   {
/*  284 */     if ((paramType1 instanceof PrimitiveType))
/*      */     {
/*  287 */       return paramType1.equals(paramType2);
/*      */     }
/*  289 */     if ((paramType2 instanceof PrimitiveType)) {
/*  290 */       return false;
/*      */     }
/*      */ 
/*  294 */     return isAssignableTo(paramType1, paramType2);
/*      */   }
/*      */ 
/*      */   static boolean isArrayAssignableTo(ArrayType paramArrayType, Type paramType) {
/*  298 */     if ((paramType instanceof ArrayType)) {
/*      */       try {
/*  300 */         Type localType = ((ArrayType)paramType).componentType();
/*  301 */         return isComponentAssignable(paramArrayType.componentType(), localType);
/*      */       }
/*      */       catch (ClassNotLoadedException localClassNotLoadedException)
/*      */       {
/*  305 */         return false;
/*      */       }
/*      */     }
/*  308 */     if ((paramType instanceof InterfaceType))
/*      */     {
/*  310 */       return paramType.name().equals("java.lang.Cloneable");
/*      */     }
/*      */ 
/*  313 */     return paramType.name().equals("java.lang.Object");
/*      */   }
/*      */ 
/*      */   static boolean isAssignableTo(Type paramType1, Type paramType2) {
/*  317 */     if (paramType1.equals(paramType2)) {
/*  318 */       return true;
/*      */     }
/*      */ 
/*  322 */     if ((paramType1 instanceof BooleanType)) {
/*  323 */       if ((paramType2 instanceof BooleanType)) {
/*  324 */         return true;
/*      */       }
/*  326 */       return false;
/*      */     }
/*  328 */     if ((paramType2 instanceof BooleanType)) {
/*  329 */       return false;
/*      */     }
/*      */ 
/*  333 */     if ((paramType1 instanceof PrimitiveType)) {
/*  334 */       if ((paramType2 instanceof PrimitiveType)) {
/*  335 */         return true;
/*      */       }
/*  337 */       return false;
/*      */     }
/*  339 */     if ((paramType2 instanceof PrimitiveType)) {
/*  340 */       return false;
/*      */     }
/*      */ 
/*  344 */     if ((paramType1 instanceof ArrayType))
/*  345 */       return isArrayAssignableTo((ArrayType)paramType1, paramType2);
/*      */     List localList;
/*  348 */     if ((paramType1 instanceof ClassType)) {
/*  349 */       localObject = ((ClassType)paramType1).superclass();
/*  350 */       if ((localObject != null) && (isAssignableTo((Type)localObject, paramType2))) {
/*  351 */         return true;
/*      */       }
/*  353 */       localList = ((ClassType)paramType1).interfaces();
/*      */     }
/*      */     else {
/*  356 */       localList = ((InterfaceType)paramType1).superinterfaces();
/*      */     }
/*  358 */     for (Object localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { InterfaceType localInterfaceType = (InterfaceType)((Iterator)localObject).next();
/*  359 */       if (isAssignableTo(localInterfaceType, paramType2)) {
/*  360 */         return true;
/*      */       }
/*      */     }
/*  363 */     return false;
/*      */   }
/*      */ 
/*      */   static Method resolveOverload(List<Method> paramList, List<Value> paramList1)
/*      */     throws ParseException
/*      */   {
/*  374 */     if (paramList.size() == 1) {
/*  375 */       return (Method)paramList.get(0);
/*      */     }
/*      */ 
/*  387 */     Object localObject = null;
/*  388 */     int i = 0;
/*  389 */     for (Method localMethod : paramList) {
/*      */       List localList;
/*      */       try {
/*  392 */         localList = localMethod.argumentTypes();
/*      */       }
/*      */       catch (ClassNotLoadedException localClassNotLoadedException)
/*      */       {
/*      */       }
/*  397 */       continue;
/*      */ 
/*  399 */       int j = argumentsMatch(localList, paramList1);
/*  400 */       if (j == 0) {
/*  401 */         return localMethod;
/*      */       }
/*  403 */       if (j != 2)
/*      */       {
/*  407 */         localObject = localMethod;
/*  408 */         i++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  414 */     if (localObject != null) {
/*  415 */       if (i == 1) {
/*  416 */         return localObject;
/*      */       }
/*  418 */       throw new ParseException("Arguments match multiple methods");
/*      */     }
/*  420 */     throw new ParseException("Arguments match no method");
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, boolean paramBoolean)
/*      */   {
/*  694 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, byte paramByte) {
/*  698 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramByte));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, char paramChar) {
/*  702 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramChar));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, short paramShort) {
/*  706 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramShort));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, int paramInt) {
/*  710 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramInt));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, long paramLong) {
/*  714 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramLong));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, float paramFloat) {
/*  718 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramFloat));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, double paramDouble) {
/*  722 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramDouble));
/*      */   }
/*      */ 
/*      */   static LValue make(VirtualMachine paramVirtualMachine, String paramString) throws ParseException {
/*  726 */     return new LValueConstant(paramVirtualMachine.mirrorOf(paramString));
/*      */   }
/*      */ 
/*      */   static LValue makeBoolean(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  730 */     return make(paramVirtualMachine, paramToken.image.charAt(0) == 't');
/*      */   }
/*      */ 
/*      */   static LValue makeCharacter(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  734 */     return make(paramVirtualMachine, paramToken.image.charAt(1));
/*      */   }
/*      */ 
/*      */   static LValue makeFloat(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  738 */     return make(paramVirtualMachine, Float.valueOf(paramToken.image).floatValue());
/*      */   }
/*      */ 
/*      */   static LValue makeDouble(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  742 */     return make(paramVirtualMachine, Double.valueOf(paramToken.image).doubleValue());
/*      */   }
/*      */ 
/*      */   static LValue makeInteger(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  746 */     return make(paramVirtualMachine, Integer.parseInt(paramToken.image));
/*      */   }
/*      */ 
/*      */   static LValue makeShort(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  750 */     return make(paramVirtualMachine, Short.parseShort(paramToken.image));
/*      */   }
/*      */ 
/*      */   static LValue makeLong(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  754 */     return make(paramVirtualMachine, Long.parseLong(paramToken.image));
/*      */   }
/*      */ 
/*      */   static LValue makeByte(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  758 */     return make(paramVirtualMachine, Byte.parseByte(paramToken.image));
/*      */   }
/*      */ 
/*      */   static LValue makeString(VirtualMachine paramVirtualMachine, Token paramToken) throws ParseException
/*      */   {
/*  763 */     int i = paramToken.image.length();
/*  764 */     return make(paramVirtualMachine, paramToken.image.substring(1, i - 1));
/*      */   }
/*      */ 
/*      */   static LValue makeNull(VirtualMachine paramVirtualMachine, Token paramToken) throws ParseException
/*      */   {
/*  769 */     return new LValueConstant(null);
/*      */   }
/*      */ 
/*      */   static LValue makeThisObject(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, Token paramToken)
/*      */     throws ParseException
/*      */   {
/*  775 */     if (paramGetFrame == null)
/*  776 */       throw new ParseException("No current thread");
/*      */     try
/*      */     {
/*  779 */       StackFrame localStackFrame = paramGetFrame.get();
/*  780 */       ObjectReference localObjectReference = localStackFrame.thisObject();
/*      */ 
/*  782 */       if (localObjectReference == null) {
/*  783 */         throw new ParseException("No 'this'.  In native or static method");
/*      */       }
/*      */ 
/*  786 */       return new LValueConstant(localObjectReference);
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*      */     }
/*  789 */     throw new ParseException("Thread not suspended");
/*      */   }
/*      */ 
/*      */   static LValue makeNewObject(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, String paramString, List<Value> paramList)
/*      */     throws ParseException
/*      */   {
/*  797 */     List localList = paramVirtualMachine.classesByName(paramString);
/*  798 */     if (localList.size() == 0) {
/*  799 */       throw new ParseException("No class named: " + paramString);
/*      */     }
/*      */ 
/*  802 */     if (localList.size() > 1) {
/*  803 */       throw new ParseException("More than one class named: " + paramString);
/*      */     }
/*      */ 
/*  806 */     ReferenceType localReferenceType = (ReferenceType)localList.get(0);
/*      */ 
/*  809 */     if (!(localReferenceType instanceof ClassType)) {
/*  810 */       throw new ParseException("Cannot create instance of interface " + paramString);
/*      */     }
/*      */ 
/*  814 */     ClassType localClassType = (ClassType)localReferenceType;
/*  815 */     ArrayList localArrayList = new ArrayList(localClassType.methods());
/*  816 */     Iterator localIterator = localArrayList.iterator();
/*  817 */     while (localIterator.hasNext()) {
/*  818 */       localMethod = (Method)localIterator.next();
/*  819 */       if (!localMethod.isConstructor()) {
/*  820 */         localIterator.remove();
/*      */       }
/*      */     }
/*  823 */     Method localMethod = resolveOverload(localArrayList, paramList);
/*      */     ObjectReference localObjectReference;
/*      */     try {
/*  827 */       ThreadReference localThreadReference = paramGetFrame.get().thread();
/*  828 */       localObjectReference = localClassType.newInstance(localThreadReference, localMethod, paramList, 0);
/*      */     }
/*      */     catch (InvocationException localInvocationException) {
/*  831 */       throw new ParseException("Exception in " + paramString + " constructor: " + localInvocationException
/*  831 */         .exception().referenceType().name());
/*      */     } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  833 */       throw new ParseException("Thread not suspended");
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  838 */       throw new ParseException("Unable to create " + paramString + " instance");
/*      */     }
/*  840 */     return new LValueConstant(localObjectReference);
/*      */   }
/*      */ 
/*      */   private static LValue nFields(LValue paramLValue, StringTokenizer paramStringTokenizer, ThreadReference paramThreadReference)
/*      */     throws ParseException
/*      */   {
/*  847 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  848 */       return paramLValue;
/*      */     }
/*  850 */     return nFields(paramLValue.memberLValue(paramStringTokenizer.nextToken(), paramThreadReference), paramStringTokenizer, paramThreadReference);
/*      */   }
/*      */ 
/*      */   static LValue makeName(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, String paramString)
/*      */     throws ParseException
/*      */   {
/*  857 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ".");
/*  858 */     String str = localStringTokenizer.nextToken();
/*      */ 
/*  860 */     if (paramGetFrame != null) try { StackFrame localStackFrame = paramGetFrame.get();
/*  863 */         ThreadReference localThreadReference = localStackFrame.thread();
/*      */         LocalVariable localLocalVariable;
/*      */         try {
/*  866 */           localLocalVariable = localStackFrame.visibleVariableByName(str);
/*      */         } catch (AbsentInformationException localAbsentInformationException) {
/*  868 */           localLocalVariable = null;
/*      */         }
/*  870 */         if (localLocalVariable != null) {
/*  871 */           return nFields(new LValueLocal(localStackFrame, localLocalVariable), localStringTokenizer, localThreadReference);
/*      */         }
/*  873 */         Object localObject1 = localStackFrame.thisObject();
/*      */         Object localObject2;
/*      */         Object localObject3;
/*  874 */         if (localObject1 != null)
/*      */         {
/*  876 */           localObject2 = new LValueConstant((Value)localObject1);
/*      */           try
/*      */           {
/*  879 */             localObject3 = ((LValue)localObject2).memberLValue(str, localThreadReference);
/*      */           } catch (ParseException localParseException) {
/*  881 */             localObject3 = null;
/*      */           }
/*  883 */           if (localObject3 != null) {
/*  884 */             return nFields((LValue)localObject3, localStringTokenizer, localThreadReference);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  889 */         while (localStringTokenizer.hasMoreTokens()) {
/*  890 */           localObject1 = paramVirtualMachine.classesByName(str);
/*  891 */           if (((List)localObject1).size() > 0) {
/*  892 */             if (((List)localObject1).size() > 1) {
/*  893 */               throw new ParseException("More than one class named: " + str);
/*      */             }
/*      */ 
/*  896 */             localObject2 = (ReferenceType)((List)localObject1).get(0);
/*      */ 
/*  898 */             localObject3 = new LValueStaticMember((ReferenceType)localObject2, localStringTokenizer
/*  898 */               .nextToken(), localThreadReference);
/*  899 */             return nFields((LValue)localObject3, localStringTokenizer, localThreadReference);
/*      */           }
/*      */ 
/*  902 */           str = str + '.' + localStringTokenizer.nextToken();
/*      */         }
/*      */       } catch (IncompatibleThreadStateException localIncompatibleThreadStateException) {
/*  905 */         throw new ParseException("Thread not suspended");
/*      */       }
/*      */ 
/*  908 */     throw new ParseException("Name unknown: " + paramString);
/*      */   }
/*      */ 
/*      */   static String stringValue(LValue paramLValue, ExpressionParser.GetFrame paramGetFrame) throws ParseException
/*      */   {
/*  913 */     Value localValue = paramLValue.getMassagedValue(paramGetFrame);
/*  914 */     if (localValue == null) {
/*  915 */       return "null";
/*      */     }
/*  917 */     if ((localValue instanceof StringReference)) {
/*  918 */       return ((StringReference)localValue).value();
/*      */     }
/*  920 */     return localValue.toString();
/*      */   }
/*      */ 
/*      */   static LValue booleanOperation(VirtualMachine paramVirtualMachine, Token paramToken, LValue paramLValue1, LValue paramLValue2)
/*      */     throws ParseException
/*      */   {
/*  926 */     String str = paramToken.image;
/*  927 */     Value localValue1 = paramLValue1.interiorGetValue();
/*  928 */     Value localValue2 = paramLValue2.interiorGetValue();
/*  929 */     if ((!(localValue1 instanceof PrimitiveValue)) || (!(localValue2 instanceof PrimitiveValue)))
/*      */     {
/*  931 */       if (str.equals("=="))
/*  932 */         return make(paramVirtualMachine, localValue1.equals(localValue2));
/*  933 */       if (str.equals("!=")) {
/*  934 */         return make(paramVirtualMachine, !localValue1.equals(localValue2));
/*      */       }
/*  936 */       throw new ParseException("Operands or '" + str + "' must be primitive");
/*      */     }
/*      */ 
/*  941 */     double d1 = ((PrimitiveValue)localValue1).doubleValue();
/*  942 */     double d2 = ((PrimitiveValue)localValue2).doubleValue();
/*      */     boolean bool;
/*  944 */     if (str.equals("<"))
/*  945 */       bool = d1 < d2;
/*  946 */     else if (str.equals(">"))
/*  947 */       bool = d1 > d2;
/*  948 */     else if (str.equals("<="))
/*  949 */       bool = d1 <= d2;
/*  950 */     else if (str.equals(">="))
/*  951 */       bool = d1 >= d2;
/*  952 */     else if (str.equals("=="))
/*  953 */       bool = d1 == d2;
/*  954 */     else if (str.equals("!="))
/*  955 */       bool = d1 != d2;
/*      */     else {
/*  957 */       throw new ParseException("Unknown operation: " + str);
/*      */     }
/*  959 */     return make(paramVirtualMachine, bool);
/*      */   }
/*      */ 
/*      */   static LValue operation(VirtualMachine paramVirtualMachine, Token paramToken, LValue paramLValue1, LValue paramLValue2, ExpressionParser.GetFrame paramGetFrame)
/*      */     throws ParseException
/*      */   {
/*  966 */     String str = paramToken.image;
/*  967 */     Value localValue1 = paramLValue1.interiorGetValue();
/*  968 */     Value localValue2 = paramLValue2.interiorGetValue();
/*  969 */     if (((localValue1 instanceof StringReference)) || ((localValue2 instanceof StringReference)))
/*      */     {
/*  971 */       if (str.equals("+"))
/*      */       {
/*  974 */         return make(paramVirtualMachine, stringValue(paramLValue1, paramGetFrame) + 
/*  975 */           stringValue(paramLValue2, paramGetFrame));
/*      */       }
/*      */     }
/*      */ 
/*  978 */     if (((localValue1 instanceof ObjectReference)) || ((localValue2 instanceof ObjectReference)))
/*      */     {
/*  980 */       if (str.equals("=="))
/*  981 */         return make(paramVirtualMachine, localValue1.equals(localValue2));
/*  982 */       if (str.equals("!=")) {
/*  983 */         return make(paramVirtualMachine, !localValue1.equals(localValue2));
/*      */       }
/*  985 */       throw new ParseException("Invalid operation '" + str + "' on an Object");
/*      */     }
/*      */ 
/*  989 */     if (((localValue1 instanceof BooleanValue)) || ((localValue2 instanceof BooleanValue)))
/*      */     {
/*  991 */       throw new ParseException("Invalid operation '" + str + "' on a Boolean");
/*      */     }
/*      */ 
/*  995 */     PrimitiveValue localPrimitiveValue1 = (PrimitiveValue)localValue1;
/*  996 */     PrimitiveValue localPrimitiveValue2 = (PrimitiveValue)localValue2;
/*  997 */     if (((localPrimitiveValue1 instanceof DoubleValue)) || ((localPrimitiveValue2 instanceof DoubleValue)))
/*      */     {
/*  999 */       double d1 = localPrimitiveValue1.doubleValue();
/* 1000 */       double d2 = localPrimitiveValue2.doubleValue();
/*      */       double d3;
/* 1002 */       if (str.equals("+"))
/* 1003 */         d3 = d1 + d2;
/* 1004 */       else if (str.equals("-"))
/* 1005 */         d3 = d1 - d2;
/* 1006 */       else if (str.equals("*"))
/* 1007 */         d3 = d1 * d2;
/* 1008 */       else if (str.equals("/"))
/* 1009 */         d3 = d1 / d2;
/*      */       else {
/* 1011 */         throw new ParseException("Unknown operation: " + str);
/*      */       }
/* 1013 */       return make(paramVirtualMachine, d3);
/*      */     }
/* 1015 */     if (((localPrimitiveValue1 instanceof FloatValue)) || ((localPrimitiveValue2 instanceof FloatValue)))
/*      */     {
/* 1017 */       float f1 = localPrimitiveValue1.floatValue();
/* 1018 */       float f2 = localPrimitiveValue2.floatValue();
/*      */       float f3;
/* 1020 */       if (str.equals("+"))
/* 1021 */         f3 = f1 + f2;
/* 1022 */       else if (str.equals("-"))
/* 1023 */         f3 = f1 - f2;
/* 1024 */       else if (str.equals("*"))
/* 1025 */         f3 = f1 * f2;
/* 1026 */       else if (str.equals("/"))
/* 1027 */         f3 = f1 / f2;
/*      */       else {
/* 1029 */         throw new ParseException("Unknown operation: " + str);
/*      */       }
/* 1031 */       return make(paramVirtualMachine, f3);
/*      */     }
/* 1033 */     if (((localPrimitiveValue1 instanceof LongValue)) || ((localPrimitiveValue2 instanceof LongValue)))
/*      */     {
/* 1035 */       long l1 = localPrimitiveValue1.longValue();
/* 1036 */       long l2 = localPrimitiveValue2.longValue();
/*      */       long l3;
/* 1038 */       if (str.equals("+"))
/* 1039 */         l3 = l1 + l2;
/* 1040 */       else if (str.equals("-"))
/* 1041 */         l3 = l1 - l2;
/* 1042 */       else if (str.equals("*"))
/* 1043 */         l3 = l1 * l2;
/* 1044 */       else if (str.equals("/"))
/* 1045 */         l3 = l1 / l2;
/*      */       else {
/* 1047 */         throw new ParseException("Unknown operation: " + str);
/*      */       }
/* 1049 */       return make(paramVirtualMachine, l3);
/*      */     }
/* 1051 */     int i = localPrimitiveValue1.intValue();
/* 1052 */     int j = localPrimitiveValue2.intValue();
/*      */     int k;
/* 1054 */     if (str.equals("+"))
/* 1055 */       k = i + j;
/* 1056 */     else if (str.equals("-"))
/* 1057 */       k = i - j;
/* 1058 */     else if (str.equals("*"))
/* 1059 */       k = i * j;
/* 1060 */     else if (str.equals("/"))
/* 1061 */       k = i / j;
/*      */     else {
/* 1063 */       throw new ParseException("Unknown operation: " + str);
/*      */     }
/* 1065 */     return make(paramVirtualMachine, k);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  221 */     primitiveTypeNames.add("boolean");
/*  222 */     primitiveTypeNames.add("byte");
/*  223 */     primitiveTypeNames.add("char");
/*  224 */     primitiveTypeNames.add("short");
/*  225 */     primitiveTypeNames.add("int");
/*  226 */     primitiveTypeNames.add("long");
/*  227 */     primitiveTypeNames.add("float");
/*  228 */     primitiveTypeNames.add("double");
/*      */   }
/*      */ 
/*      */   private static class LValueArrayElement extends LValue
/*      */   {
/*      */     final ArrayReference array;
/*      */     final int index;
/*      */ 
/*      */     LValueArrayElement(Value paramValue, int paramInt)
/*      */       throws ParseException
/*      */     {
/*  638 */       if (!(paramValue instanceof ArrayReference)) {
/*  639 */         throw new ParseException("Must be array type: " + paramValue);
/*      */       }
/*      */ 
/*  642 */       this.array = ((ArrayReference)paramValue);
/*  643 */       this.index = paramInt;
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */     {
/*  648 */       if (this.jdiValue == null) {
/*  649 */         this.jdiValue = this.array.getValue(this.index);
/*      */       }
/*  651 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue)
/*      */       throws InvalidTypeException, ClassNotLoadedException
/*      */     {
/*  657 */       this.array.setValue(this.index, paramValue);
/*  658 */       this.jdiValue = paramValue;
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  663 */       throw new ParseException("Array element is not a method");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LValueArrayLength extends LValue
/*      */   {
/*      */     final ArrayReference arrayRef;
/*      */ 
/*      */     LValueArrayLength(ArrayReference paramArrayReference)
/*      */     {
/*  611 */       this.arrayRef = paramArrayReference;
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */     {
/*  616 */       if (this.jdiValue == null) {
/*  617 */         this.jdiValue = this.arrayRef.virtualMachine().mirrorOf(this.arrayRef.length());
/*      */       }
/*  619 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue) throws ParseException
/*      */     {
/*  624 */       throw new ParseException("Cannot set constant: " + paramValue);
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  629 */       throw new ParseException("Array element is not a method");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LValueConstant extends LValue
/*      */   {
/*      */     final Value value;
/*      */ 
/*      */     LValueConstant(Value paramValue)
/*      */     {
/*  671 */       this.value = paramValue;
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */     {
/*  676 */       if (this.jdiValue == null) {
/*  677 */         this.jdiValue = this.value;
/*      */       }
/*  679 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue) throws ParseException
/*      */     {
/*  684 */       throw new ParseException("Cannot set constant: " + this.value);
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  689 */       throw new ParseException("Constant is not a method");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LValueInstanceMember extends LValue
/*      */   {
/*      */     final ObjectReference obj;
/*      */     final ThreadReference thread;
/*      */     final Field matchingField;
/*      */     final List<Method> overloads;
/*  458 */     Method matchingMethod = null;
/*  459 */     List<Value> methodArguments = null;
/*      */ 
/*      */     LValueInstanceMember(Value paramValue, String paramString, ThreadReference paramThreadReference)
/*      */       throws ParseException
/*      */     {
/*  464 */       if (!(paramValue instanceof ObjectReference)) {
/*  465 */         throw new ParseException("Cannot access field of primitive type: " + paramValue);
/*      */       }
/*      */ 
/*  468 */       this.obj = ((ObjectReference)paramValue);
/*  469 */       this.thread = paramThreadReference;
/*  470 */       ReferenceType localReferenceType = this.obj.referenceType();
/*      */ 
/*  475 */       this.matchingField = LValue.fieldByName(localReferenceType, paramString, 1);
/*      */ 
/*  477 */       this.overloads = LValue.methodsByName(localReferenceType, paramString, 1);
/*      */ 
/*  479 */       if ((this.matchingField == null) && (this.overloads.size() == 0))
/*      */       {
/*  481 */         throw new ParseException("No instance field or method with the name " + paramString + " in " + localReferenceType
/*  481 */           .name());
/*      */       }
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */       throws InvocationException, InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, ParseException
/*      */     {
/*  489 */       if (this.jdiValue != null) {
/*  490 */         return this.jdiValue;
/*      */       }
/*  492 */       if (this.matchingMethod == null) {
/*  493 */         if (this.matchingField == null) {
/*  494 */           throw new ParseException("No such field in " + this.obj.referenceType().name());
/*      */         }
/*  496 */         return this.jdiValue = this.obj.getValue(this.matchingField);
/*      */       }
/*  498 */       return this.jdiValue = this.obj.invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue)
/*      */       throws ParseException, InvalidTypeException, ClassNotLoadedException
/*      */     {
/*  506 */       if (this.matchingMethod != null) {
/*  507 */         throw new ParseException("Cannot assign to a method invocation");
/*      */       }
/*  509 */       this.obj.setValue(this.matchingField, paramValue);
/*  510 */       this.jdiValue = paramValue;
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  515 */       if (this.matchingMethod != null) {
/*  516 */         throw new ParseException("Invalid consecutive invocations");
/*      */       }
/*  518 */       this.methodArguments = paramList;
/*  519 */       this.matchingMethod = LValue.resolveOverload(this.overloads, paramList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LValueLocal extends LValue
/*      */   {
/*      */     final StackFrame frame;
/*      */     final LocalVariable var;
/*      */ 
/*      */     LValueLocal(StackFrame paramStackFrame, LocalVariable paramLocalVariable)
/*      */     {
/*  428 */       this.frame = paramStackFrame;
/*  429 */       this.var = paramLocalVariable;
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */     {
/*  434 */       if (this.jdiValue == null) {
/*  435 */         this.jdiValue = this.frame.getValue(this.var);
/*      */       }
/*  437 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue)
/*      */       throws InvalidTypeException, ClassNotLoadedException
/*      */     {
/*  443 */       this.frame.setValue(this.var, paramValue);
/*  444 */       this.jdiValue = paramValue;
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  449 */       throw new ParseException(this.var.name() + " is not a method");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LValueStaticMember extends LValue
/*      */   {
/*      */     final ReferenceType refType;
/*      */     final ThreadReference thread;
/*      */     final Field matchingField;
/*      */     final List<Method> overloads;
/*  528 */     Method matchingMethod = null;
/*  529 */     List<Value> methodArguments = null;
/*      */ 
/*      */     LValueStaticMember(ReferenceType paramReferenceType, String paramString, ThreadReference paramThreadReference)
/*      */       throws ParseException
/*      */     {
/*  534 */       this.refType = paramReferenceType;
/*  535 */       this.thread = paramThreadReference;
/*      */ 
/*  540 */       this.matchingField = LValue.fieldByName(paramReferenceType, paramString, 0);
/*      */ 
/*  542 */       this.overloads = LValue.methodsByName(paramReferenceType, paramString, 0);
/*      */ 
/*  544 */       if ((this.matchingField == null) && (this.overloads.size() == 0))
/*      */       {
/*  546 */         throw new ParseException("No static field or method with the name " + paramString + " in " + paramReferenceType
/*  546 */           .name());
/*      */       }
/*      */     }
/*      */ 
/*      */     Value getValue()
/*      */       throws InvocationException, InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, ParseException
/*      */     {
/*  554 */       if (this.jdiValue != null) {
/*  555 */         return this.jdiValue;
/*      */       }
/*  557 */       if (this.matchingMethod == null)
/*  558 */         return this.jdiValue = this.refType.getValue(this.matchingField);
/*      */       Object localObject;
/*  559 */       if ((this.refType instanceof ClassType)) {
/*  560 */         localObject = (ClassType)this.refType;
/*  561 */         return this.jdiValue = ((ClassType)localObject).invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*  562 */       }if ((this.refType instanceof InterfaceType)) {
/*  563 */         localObject = (InterfaceType)this.refType;
/*  564 */         return this.jdiValue = ((InterfaceType)localObject).invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*      */       }
/*      */ 
/*  567 */       throw new InvalidTypeException("Cannot invoke static method on " + this.refType
/*  567 */         .name());
/*      */     }
/*      */ 
/*      */     void setValue0(Value paramValue)
/*      */       throws ParseException, InvalidTypeException, ClassNotLoadedException
/*      */     {
/*  575 */       if (this.matchingMethod != null) {
/*  576 */         throw new ParseException("Cannot assign to a method invocation");
/*      */       }
/*  578 */       if (!(this.refType instanceof ClassType)) {
/*  579 */         throw new ParseException("Cannot set interface field: " + this.refType);
/*      */       }
/*      */ 
/*  582 */       ((ClassType)this.refType).setValue(this.matchingField, paramValue);
/*  583 */       this.jdiValue = paramValue;
/*      */     }
/*      */ 
/*      */     void invokeWith(List<Value> paramList) throws ParseException
/*      */     {
/*  588 */       if (this.matchingMethod != null) {
/*  589 */         throw new ParseException("Invalid consecutive invocations");
/*      */       }
/*  591 */       this.methodArguments = paramList;
/*  592 */       this.matchingMethod = LValue.resolveOverload(this.overloads, paramList);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.expr.LValue
 * JD-Core Version:    0.6.2
 */