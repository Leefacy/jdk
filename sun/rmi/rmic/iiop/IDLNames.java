/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.util.Hashtable;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.Identifier;
/*      */ 
/*      */ public class IDLNames
/*      */   implements Constants
/*      */ {
/*   55 */   public static final byte[] ASCII_HEX = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */ 
/*   81 */   private static final byte[] IDL_IDENTIFIER_CHARS = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1 };
/*      */ 
/*      */   public static String getMemberOrMethodName(NameContext paramNameContext, String paramString, BatchEnvironment paramBatchEnvironment)
/*      */   {
/*  123 */     String str = (String)paramBatchEnvironment.namesCache.get(paramString);
/*      */ 
/*  125 */     if (str == null)
/*      */     {
/*  132 */       str = paramNameContext.get(paramString);
/*      */ 
/*  136 */       str = convertLeadingUnderscores(str);
/*      */ 
/*  143 */       str = convertIDLKeywords(str);
/*      */ 
/*  147 */       str = convertToISOLatin1(str);
/*      */ 
/*  151 */       paramBatchEnvironment.namesCache.put(paramString, str);
/*      */     }
/*      */ 
/*  154 */     return str;
/*      */   }
/*      */ 
/*      */   public static String convertToISOLatin1(String paramString)
/*      */   {
/*  166 */     String str = replace(paramString, "x\\u", "U");
/*  167 */     str = replace(str, "x\\U", "U");
/*      */ 
/*  172 */     int i = str.length();
/*  173 */     StringBuffer localStringBuffer = null;
/*      */ 
/*  175 */     for (int j = 0; j < i; j++)
/*      */     {
/*  177 */       int k = str.charAt(j);
/*      */ 
/*  179 */       if ((k > 255) || (IDL_IDENTIFIER_CHARS[k] == 0))
/*      */       {
/*  183 */         if (localStringBuffer == null)
/*      */         {
/*  187 */           localStringBuffer = new StringBuffer(str.substring(0, j));
/*      */         }
/*      */ 
/*  192 */         localStringBuffer.append("U");
/*  193 */         localStringBuffer.append((char)ASCII_HEX[((k & 0xF000) >>> 12)]);
/*  194 */         localStringBuffer.append((char)ASCII_HEX[((k & 0xF00) >>> 8)]);
/*  195 */         localStringBuffer.append((char)ASCII_HEX[((k & 0xF0) >>> 4)]);
/*  196 */         localStringBuffer.append((char)ASCII_HEX[(k & 0xF)]);
/*      */       }
/*  199 */       else if (localStringBuffer != null) {
/*  200 */         localStringBuffer.append(k);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  205 */     if (localStringBuffer != null) {
/*  206 */       str = localStringBuffer.toString();
/*      */     }
/*      */ 
/*  209 */     return str;
/*      */   }
/*      */ 
/*      */   public static String convertIDLKeywords(String paramString)
/*      */   {
/*  219 */     for (int i = 0; i < IDL_KEYWORDS.length; i++) {
/*  220 */       if (paramString.equalsIgnoreCase(IDL_KEYWORDS[i])) {
/*  221 */         return "_" + paramString;
/*      */       }
/*      */     }
/*      */ 
/*  225 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String convertLeadingUnderscores(String paramString)
/*      */   {
/*  235 */     if (paramString.startsWith("_")) {
/*  236 */       return "J" + paramString;
/*      */     }
/*      */ 
/*  239 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String getClassOrInterfaceName(Identifier paramIdentifier, BatchEnvironment paramBatchEnvironment)
/*      */     throws Exception
/*      */   {
/*  254 */     String str1 = paramIdentifier.getName().toString();
/*  255 */     String str2 = null;
/*      */ 
/*  257 */     if (paramIdentifier.isQualified()) {
/*  258 */       str2 = paramIdentifier.getQualifier().toString();
/*      */     }
/*      */ 
/*  263 */     String str3 = (String)paramBatchEnvironment.namesCache.get(str1);
/*      */ 
/*  265 */     if (str3 == null)
/*      */     {
/*  269 */       str3 = replace(str1, ". ", "__");
/*      */ 
/*  273 */       str3 = convertToISOLatin1(str3);
/*      */ 
/*  277 */       NameContext localNameContext = NameContext.forName(str2, false, paramBatchEnvironment);
/*  278 */       localNameContext.assertPut(str3);
/*      */ 
/*  282 */       str3 = getTypeOrModuleName(str3);
/*      */ 
/*  286 */       paramBatchEnvironment.namesCache.put(str1, str3);
/*      */     }
/*      */ 
/*  289 */     return str3;
/*      */   }
/*      */ 
/*      */   public static String getExceptionName(String paramString)
/*      */   {
/*  299 */     String str = paramString;
/*      */ 
/*  301 */     if (paramString.endsWith("Exception"))
/*      */     {
/*  306 */       str = stripLeadingUnderscore(paramString.substring(0, paramString.lastIndexOf("Exception")) + "Ex");
/*      */     }
/*  308 */     else str = paramString + "Ex";
/*      */ 
/*  311 */     return str;
/*      */   }
/*      */ 
/*      */   public static String[] getModuleNames(Identifier paramIdentifier, boolean paramBoolean, BatchEnvironment paramBatchEnvironment)
/*      */     throws Exception
/*      */   {
/*  324 */     Object localObject1 = null;
/*      */     Object localObject2;
/*  326 */     if (paramIdentifier.isQualified())
/*      */     {
/*  330 */       localObject2 = paramIdentifier.getQualifier();
/*      */ 
/*  334 */       paramBatchEnvironment.modulesContext.assertPut(((Identifier)localObject2).toString());
/*      */ 
/*  338 */       int i = 1;
/*  339 */       Object localObject3 = localObject2;
/*  340 */       while (((Identifier)localObject3).isQualified()) {
/*  341 */         localObject3 = ((Identifier)localObject3).getQualifier();
/*  342 */         i++;
/*      */       }
/*      */ 
/*  345 */       localObject1 = new String[i];
/*  346 */       int j = i - 1;
/*  347 */       localObject3 = localObject2;
/*      */ 
/*  351 */       for (int k = 0; k < i; k++)
/*      */       {
/*  353 */         String str1 = ((Identifier)localObject3).getName().toString();
/*      */ 
/*  357 */         String str2 = (String)paramBatchEnvironment.namesCache.get(str1);
/*      */ 
/*  359 */         if (str2 == null)
/*      */         {
/*  363 */           str2 = convertToISOLatin1(str1);
/*      */ 
/*  367 */           str2 = getTypeOrModuleName(str2);
/*      */ 
/*  371 */           paramBatchEnvironment.namesCache.put(str1, str2);
/*      */         }
/*      */ 
/*  374 */         localObject1[(j--)] = str2;
/*  375 */         localObject3 = ((Identifier)localObject3).getQualifier();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  383 */     if (paramBoolean) {
/*  384 */       if (localObject1 == null) {
/*  385 */         localObject1 = IDL_BOXEDIDL_MODULE;
/*      */       } else {
/*  387 */         localObject2 = new String[localObject1.length + IDL_BOXEDIDL_MODULE.length];
/*  388 */         System.arraycopy(IDL_BOXEDIDL_MODULE, 0, localObject2, 0, IDL_BOXEDIDL_MODULE.length);
/*  389 */         System.arraycopy(localObject1, 0, localObject2, IDL_BOXEDIDL_MODULE.length, localObject1.length);
/*  390 */         localObject1 = localObject2;
/*      */       }
/*      */     }
/*      */ 
/*  394 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static String getArrayName(Type paramType, int paramInt)
/*      */   {
/*  404 */     StringBuffer localStringBuffer = new StringBuffer(64);
/*      */ 
/*  408 */     localStringBuffer.append("seq");
/*  409 */     localStringBuffer.append(Integer.toString(paramInt));
/*  410 */     localStringBuffer.append("_");
/*      */ 
/*  415 */     localStringBuffer.append(replace(stripLeadingUnderscore(paramType.getIDLName()), " ", "_"));
/*      */ 
/*  419 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public static String[] getArrayModuleNames(Type paramType)
/*      */   {
/*  428 */     String[] arrayOfString2 = paramType.getIDLModuleNames();
/*  429 */     int i = arrayOfString2.length;
/*      */     String[] arrayOfString1;
/*  433 */     if (i == 0)
/*      */     {
/*  437 */       arrayOfString1 = IDL_SEQUENCE_MODULE;
/*      */     }
/*      */     else
/*      */     {
/*  442 */       arrayOfString1 = new String[i + IDL_SEQUENCE_MODULE.length];
/*  443 */       System.arraycopy(IDL_SEQUENCE_MODULE, 0, arrayOfString1, 0, IDL_SEQUENCE_MODULE.length);
/*  444 */       System.arraycopy(arrayOfString2, 0, arrayOfString1, IDL_SEQUENCE_MODULE.length, i);
/*      */     }
/*      */ 
/*  447 */     return arrayOfString1;
/*      */   }
/*      */ 
/*      */   private static int getInitialAttributeKind(CompoundType.Method paramMethod, BatchEnvironment paramBatchEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/*  453 */     int i = 0;
/*      */ 
/*  457 */     if (!paramMethod.isConstructor())
/*      */     {
/*  463 */       boolean bool1 = true;
/*  464 */       ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*      */ 
/*  466 */       if (arrayOfValueType.length > 0) {
/*  467 */         for (int j = 0; j < arrayOfValueType.length; j++) {
/*  468 */           if ((arrayOfValueType[j].isCheckedException()) && 
/*  469 */             (!arrayOfValueType[j]
/*  469 */             .isRemoteExceptionOrSubclass())) {
/*  470 */             bool1 = false;
/*  471 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  479 */         bool1 = paramMethod.getEnclosing().isType(32768);
/*      */       }
/*      */ 
/*  482 */       if (bool1) {
/*  483 */         String str = paramMethod.getName();
/*  484 */         int k = str.length();
/*  485 */         int m = paramMethod.getArguments().length;
/*  486 */         Type localType = paramMethod.getReturnType();
/*  487 */         boolean bool2 = localType.isType(1);
/*  488 */         boolean bool3 = localType.isType(2);
/*      */ 
/*  493 */         if ((str.startsWith("get")) && (k > 3) && (m == 0) && (!bool2)) {
/*  494 */           i = 2;
/*      */         }
/*  500 */         else if ((str.startsWith("is")) && (k > 2) && (m == 0) && (bool3)) {
/*  501 */           i = 1;
/*      */         }
/*  507 */         else if ((str.startsWith("set")) && (k > 3) && (m == 1) && (bool2)) {
/*  508 */           i = 5;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  515 */     return i;
/*      */   }
/*      */ 
/*      */   private static void setAttributeKinds(CompoundType.Method[] paramArrayOfMethod, int[] paramArrayOfInt, String[] paramArrayOfString)
/*      */   {
/*  522 */     int i = paramArrayOfMethod.length;
/*      */ 
/*  526 */     for (int j = 0; j < i; j++)
/*  527 */       switch (paramArrayOfInt[j]) { case 2:
/*  528 */         paramArrayOfString[j] = paramArrayOfString[j].substring(3); break;
/*      */       case 1:
/*  529 */         paramArrayOfString[j] = paramArrayOfString[j].substring(2); break;
/*      */       case 5:
/*  530 */         paramArrayOfString[j] = paramArrayOfString[j].substring(3);
/*      */       case 3:
/*      */       case 4:
/*      */       }
/*      */     int k;
/*  539 */     for (j = 0; j < i; j++) {
/*  540 */       if (paramArrayOfInt[j] == 1) {
/*  541 */         for (k = 0; k < i; k++) {
/*  542 */           if ((k != j) && ((paramArrayOfInt[k] == 2) || (paramArrayOfInt[k] == 5)))
/*      */           {
/*  544 */             if (paramArrayOfString[j]
/*  544 */               .equals(paramArrayOfString[k]))
/*      */             {
/*  548 */               Type localType1 = paramArrayOfMethod[j].getReturnType();
/*      */               Type localType2;
/*  551 */               if (paramArrayOfInt[k] == 2)
/*  552 */                 localType2 = paramArrayOfMethod[k].getReturnType();
/*      */               else {
/*  554 */                 localType2 = paramArrayOfMethod[k].getArguments()[0];
/*      */               }
/*      */ 
/*  557 */               if (!localType1.equals(localType2))
/*      */               {
/*  561 */                 paramArrayOfInt[j] = 0;
/*  562 */                 paramArrayOfString[j] = paramArrayOfMethod[j].getName();
/*  563 */                 break;
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  575 */     for (j = 0; j < i; j++) {
/*  576 */       if (paramArrayOfInt[j] == 5) {
/*  577 */         k = -1;
/*  578 */         int m = -1;
/*      */ 
/*  581 */         for (int n = 0; n < i; n++) {
/*  582 */           if ((n != j) && (paramArrayOfString[j].equals(paramArrayOfString[n])))
/*      */           {
/*  586 */             Type localType3 = paramArrayOfMethod[n].getReturnType();
/*  587 */             Type localType4 = paramArrayOfMethod[j].getArguments()[0];
/*      */ 
/*  589 */             if (localType3.equals(localType4)) {
/*  590 */               if (paramArrayOfInt[n] == 1) {
/*  591 */                 m = n;
/*      */               }
/*  593 */               else if (paramArrayOfInt[n] == 2) {
/*  594 */                 k = n;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  601 */         if (k > -1) {
/*  602 */           if (m > -1)
/*      */           {
/*  607 */             paramArrayOfInt[m] = 3;
/*      */ 
/*  610 */             paramArrayOfMethod[m].setAttributePairIndex(j);
/*  611 */             paramArrayOfMethod[j].setAttributePairIndex(m);
/*      */ 
/*  615 */             paramArrayOfInt[k] = 0;
/*  616 */             paramArrayOfString[k] = paramArrayOfMethod[k].getName();
/*      */           }
/*      */           else
/*      */           {
/*  621 */             paramArrayOfInt[k] = 4;
/*      */ 
/*  624 */             paramArrayOfMethod[k].setAttributePairIndex(j);
/*  625 */             paramArrayOfMethod[j].setAttributePairIndex(k);
/*      */           }
/*      */         }
/*  628 */         else if (m > -1)
/*      */         {
/*  632 */           paramArrayOfInt[m] = 3;
/*      */ 
/*  635 */           paramArrayOfMethod[m].setAttributePairIndex(j);
/*  636 */           paramArrayOfMethod[j].setAttributePairIndex(m);
/*      */         }
/*      */         else
/*      */         {
/*  640 */           paramArrayOfInt[j] = 0;
/*  641 */           paramArrayOfString[j] = paramArrayOfMethod[j].getName();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  650 */     for (j = 0; j < i; j++)
/*      */     {
/*  652 */       if (paramArrayOfInt[j] != 0)
/*      */       {
/*  654 */         String str = paramArrayOfString[j];
/*      */ 
/*  658 */         if (Character.isUpperCase(str.charAt(0)))
/*      */         {
/*  662 */           if ((str.length() == 1) || (Character.isLowerCase(str.charAt(1))))
/*      */           {
/*  666 */             StringBuffer localStringBuffer = new StringBuffer(str);
/*  667 */             localStringBuffer.setCharAt(0, Character.toLowerCase(str.charAt(0)));
/*  668 */             paramArrayOfString[j] = localStringBuffer.toString();
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  673 */       paramArrayOfMethod[j].setAttributeKind(paramArrayOfInt[j]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void setMethodNames(CompoundType paramCompoundType, CompoundType.Method[] paramArrayOfMethod, BatchEnvironment paramBatchEnvironment)
/*      */     throws Exception
/*      */   {
/*  711 */     int i = paramArrayOfMethod.length;
/*      */ 
/*  713 */     if (i == 0) return;
/*      */ 
/*  717 */     String[] arrayOfString = new String[i];
/*  718 */     for (int j = 0; j < i; j++) {
/*  719 */       arrayOfString[j] = paramArrayOfMethod[j].getName();
/*      */     }
/*      */ 
/*  724 */     CompoundType localCompoundType = paramArrayOfMethod[0].getEnclosing();
/*  725 */     if ((localCompoundType.isType(4096)) || 
/*  726 */       (localCompoundType
/*  726 */       .isType(8192)) || 
/*  727 */       (localCompoundType
/*  727 */       .isType(32768)))
/*      */     {
/*  732 */       localObject = new int[i];
/*      */ 
/*  734 */       for (k = 0; k < i; k++) {
/*  735 */         localObject[k] = getInitialAttributeKind(paramArrayOfMethod[k], paramBatchEnvironment);
/*      */       }
/*      */ 
/*  741 */       setAttributeKinds(paramArrayOfMethod, (int[])localObject, arrayOfString);
/*      */     }
/*      */ 
/*  746 */     Object localObject = new NameContext(true);
/*      */ 
/*  748 */     for (int k = 0; k < i; k++) {
/*  749 */       ((NameContext)localObject).put(arrayOfString[k]);
/*      */     }
/*      */ 
/*  754 */     k = 0;
/*  755 */     for (int m = 0; m < i; m++) {
/*  756 */       if (!paramArrayOfMethod[m].isConstructor()) {
/*  757 */         arrayOfString[m] = getMemberOrMethodName((NameContext)localObject, arrayOfString[m], paramBatchEnvironment);
/*      */       } else {
/*  759 */         arrayOfString[m] = "create";
/*  760 */         k = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  768 */     boolean[] arrayOfBoolean = new boolean[i];
/*  769 */     for (int n = 0; n < i; n++) {
/*  770 */       arrayOfBoolean[n] = ((!paramArrayOfMethod[n].isAttribute()) && 
/*  771 */         (!paramArrayOfMethod[n]
/*  771 */         .isConstructor()) && 
/*  772 */         (doesMethodCollide(arrayOfString[n], paramArrayOfMethod[n], paramArrayOfMethod, arrayOfString, true)) ? 
/*  772 */         1 : false);
/*      */     }
/*  774 */     convertOverloadedMethods(paramArrayOfMethod, arrayOfString, arrayOfBoolean);
/*      */ 
/*  778 */     for (n = 0; n < i; n++) {
/*  779 */       arrayOfBoolean[n] = ((!paramArrayOfMethod[n].isAttribute()) && 
/*  780 */         (paramArrayOfMethod[n]
/*  780 */         .isConstructor()) && 
/*  781 */         (doesConstructorCollide(arrayOfString[n], paramArrayOfMethod[n], paramArrayOfMethod, arrayOfString, true)) ? 
/*  781 */         1 : false);
/*      */     }
/*  783 */     convertOverloadedMethods(paramArrayOfMethod, arrayOfString, arrayOfBoolean);
/*      */     CompoundType.Method localMethod1;
/*  787 */     for (n = 0; n < i; n++)
/*      */     {
/*  789 */       localMethod1 = paramArrayOfMethod[n];
/*      */ 
/*  793 */       if ((localMethod1.isAttribute()) && 
/*  794 */         (doesMethodCollide(arrayOfString[n], localMethod1, paramArrayOfMethod, arrayOfString, true)))
/*      */       {
/*      */         int tmp414_412 = n;
/*      */         String[] tmp414_410 = arrayOfString; tmp414_410[tmp414_412] = (tmp414_410[tmp414_412] + "__");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  805 */     if (k != 0) {
/*  806 */       for (n = 0; n < i; n++) {
/*  807 */         localMethod1 = paramArrayOfMethod[n];
/*      */ 
/*  811 */         if ((localMethod1.isConstructor()) && 
/*  812 */           (doesConstructorCollide(arrayOfString[n], localMethod1, paramArrayOfMethod, arrayOfString, false)))
/*      */         {
/*      */           int tmp490_488 = n;
/*      */           String[] tmp490_486 = arrayOfString; tmp490_486[tmp490_488] = (tmp490_486[tmp490_488] + "__");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  823 */     String str1 = paramCompoundType.getIDLName();
/*  824 */     for (int i1 = 0; i1 < i; i1++) {
/*  825 */       if (arrayOfString[i1].equalsIgnoreCase(str1))
/*      */       {
/*  828 */         if (!paramArrayOfMethod[i1].isAttribute())
/*      */         {
/*      */           int tmp559_557 = i1;
/*      */           String[] tmp559_555 = arrayOfString; tmp559_555[tmp559_557] = (tmp559_555[tmp559_557] + "_");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  837 */     for (i1 = 0; i1 < i; i1++)
/*      */     {
/*  841 */       if (doesMethodCollide(arrayOfString[i1], paramArrayOfMethod[i1], paramArrayOfMethod, arrayOfString, false))
/*      */       {
/*  845 */         throw new Exception(paramArrayOfMethod[i1].toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  853 */     for (i1 = 0; i1 < i; i1++)
/*      */     {
/*  855 */       CompoundType.Method localMethod2 = paramArrayOfMethod[i1];
/*  856 */       String str2 = arrayOfString[i1];
/*      */ 
/*  858 */       if (localMethod2.isAttribute())
/*      */       {
/*  860 */         str2 = ATTRIBUTE_WIRE_PREFIX[localMethod2.getAttributeKind()] + 
/*  860 */           stripLeadingUnderscore(str2);
/*      */ 
/*  861 */         String str3 = arrayOfString[i1];
/*  862 */         localMethod2.setAttributeName(str3);
/*      */       }
/*  864 */       localMethod2.setIDLName(str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String stripLeadingUnderscore(String paramString) {
/*  869 */     if ((paramString != null) && (paramString.length() > 1) && 
/*  870 */       (paramString
/*  870 */       .charAt(0) == 
/*  870 */       '_'))
/*      */     {
/*  872 */       return paramString.substring(1);
/*      */     }
/*  874 */     return paramString;
/*      */   }
/*      */ 
/*      */   private static String stripTrailingUnderscore(String paramString)
/*      */   {
/*  879 */     if ((paramString != null) && (paramString.length() > 1) && 
/*  880 */       (paramString
/*  880 */       .charAt(paramString
/*  880 */       .length() - 1) == '_'))
/*      */     {
/*  882 */       return paramString.substring(0, paramString.length() - 1);
/*      */     }
/*  884 */     return paramString;
/*      */   }
/*      */ 
/*      */   private static void convertOverloadedMethods(CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean[] paramArrayOfBoolean)
/*      */   {
/*  892 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*      */     {
/*  896 */       if (paramArrayOfBoolean[i] != 0)
/*      */       {
/*  900 */         CompoundType.Method localMethod = paramArrayOfMethod[i];
/*  901 */         Type[] arrayOfType = localMethod.getArguments();
/*      */ 
/*  903 */         for (int j = 0; j < arrayOfType.length; j++)
/*      */         {
/*      */           int tmp46_45 = i;
/*      */           String[] tmp46_44 = paramArrayOfString; tmp46_44[tmp46_45] = (tmp46_44[tmp46_45] + "__");
/*      */ 
/*  912 */           String str = arrayOfType[j].getQualifiedIDLName(false);
/*      */ 
/*  918 */           str = replace(str, "::_", "_");
/*      */ 
/*  922 */           str = replace(str, "::", "_");
/*      */ 
/*  926 */           str = replace(str, " ", "_");
/*      */           int tmp113_112 = i;
/*      */           String[] tmp113_111 = paramArrayOfString; tmp113_111[tmp113_112] = (tmp113_111[tmp113_112] + str);
/*      */         }
/*      */ 
/*  933 */         if (arrayOfType.length == 0)
/*      */         {
/*      */           int tmp148_147 = i;
/*      */           String[] tmp148_146 = paramArrayOfString; tmp148_146[tmp148_147] = (tmp148_146[tmp148_147] + "__");
/*      */         }
/*      */ 
/*  939 */         paramArrayOfString[i] = stripLeadingUnderscore(paramArrayOfString[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean doesMethodCollide(String paramString, CompoundType.Method paramMethod, CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean paramBoolean)
/*      */   {
/*  952 */     for (int i = 0; i < paramArrayOfMethod.length; i++)
/*      */     {
/*  954 */       CompoundType.Method localMethod = paramArrayOfMethod[i];
/*      */ 
/*  956 */       if ((paramMethod != localMethod) && 
/*  957 */         (!localMethod
/*  957 */         .isConstructor()) && ((!paramBoolean) || 
/*  958 */         (!localMethod
/*  958 */         .isAttribute())) && 
/*  959 */         (paramString
/*  959 */         .equals(paramArrayOfString[i])))
/*      */       {
/*  963 */         int j = paramMethod.getAttributeKind();
/*  964 */         int k = localMethod.getAttributeKind();
/*      */ 
/*  966 */         if ((j == 0) || (k == 0) || (((j != 5) || (k == 5)) && ((j == 5) || (k != 5)) && ((j != 3) || (k != 2)) && ((j != 2) || (k != 3))))
/*      */         {
/*  979 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  984 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean doesConstructorCollide(String paramString, CompoundType.Method paramMethod, CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean paramBoolean)
/*      */   {
/*  995 */     for (int i = 0; i < paramArrayOfMethod.length; i++)
/*      */     {
/*  997 */       CompoundType.Method localMethod = paramArrayOfMethod[i];
/*      */ 
/*  999 */       if ((paramMethod != localMethod) && 
/* 1000 */         (localMethod
/* 1000 */         .isConstructor() == paramBoolean) && 
/* 1001 */         (paramString
/* 1001 */         .equals(paramArrayOfString[i])))
/*      */       {
/* 1005 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1009 */     return false;
/*      */   }
/*      */ 
/*      */   public static void setMemberNames(CompoundType paramCompoundType, CompoundType.Member[] paramArrayOfMember, CompoundType.Method[] paramArrayOfMethod, BatchEnvironment paramBatchEnvironment)
/*      */     throws Exception
/*      */   {
/* 1027 */     NameContext localNameContext = new NameContext(true);
/*      */ 
/* 1029 */     for (int i = 0; i < paramArrayOfMember.length; i++)
/* 1030 */       localNameContext.put(paramArrayOfMember[i].getName());
/*      */     String str2;
/* 1035 */     for (i = 0; i < paramArrayOfMember.length; i++)
/*      */     {
/* 1037 */       CompoundType.Member localMember = paramArrayOfMember[i];
/* 1038 */       str2 = getMemberOrMethodName(localNameContext, localMember.getName(), paramBatchEnvironment);
/* 1039 */       localMember.setIDLName(str2);
/*      */     }
/*      */ 
/* 1044 */     String str1 = paramCompoundType.getIDLName();
/* 1045 */     for (int j = 0; j < paramArrayOfMember.length; j++) {
/* 1046 */       str2 = paramArrayOfMember[j].getIDLName();
/* 1047 */       if (str2.equalsIgnoreCase(str1))
/*      */       {
/* 1049 */         paramArrayOfMember[j].setIDLName(str2 + "_");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1055 */     for (j = 0; j < paramArrayOfMember.length; j++) {
/* 1056 */       str2 = paramArrayOfMember[j].getIDLName();
/* 1057 */       for (int m = 0; m < paramArrayOfMember.length; m++) {
/* 1058 */         if ((j != m) && (paramArrayOfMember[m].getIDLName().equals(str2)))
/*      */         {
/* 1062 */           throw new Exception(str2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     do
/*      */     {
/* 1072 */       j = 0;
/* 1073 */       for (int k = 0; k < paramArrayOfMember.length; k++) {
/* 1074 */         String str3 = paramArrayOfMember[k].getIDLName();
/* 1075 */         for (int n = 0; n < paramArrayOfMethod.length; n++)
/* 1076 */           if (paramArrayOfMethod[n].getIDLName().equals(str3))
/*      */           {
/* 1080 */             paramArrayOfMember[k].setIDLName(str3 + "_");
/* 1081 */             j = 1;
/* 1082 */             break;
/*      */           }
/*      */       }
/*      */     }
/* 1086 */     while (j != 0);
/*      */   }
/*      */ 
/*      */   public static String getTypeName(int paramInt, boolean paramBoolean)
/*      */   {
/* 1100 */     String str = null;
/*      */ 
/* 1102 */     switch (paramInt) { case 1:
/* 1103 */       str = "void"; break;
/*      */     case 2:
/* 1104 */       str = "boolean"; break;
/*      */     case 4:
/* 1105 */       str = "octet"; break;
/*      */     case 8:
/* 1106 */       str = "wchar"; break;
/*      */     case 16:
/* 1107 */       str = "short"; break;
/*      */     case 32:
/* 1108 */       str = "long"; break;
/*      */     case 64:
/* 1109 */       str = "long long"; break;
/*      */     case 128:
/* 1110 */       str = "float"; break;
/*      */     case 256:
/* 1111 */       str = "double"; break;
/*      */     case 1024:
/* 1112 */       str = "any"; break;
/*      */     case 2048:
/* 1113 */       str = "Object"; break;
/*      */     case 512:
/* 1116 */       if (paramBoolean)
/* 1117 */         str = "wstring";
/*      */       else {
/* 1119 */         str = "WStringValue";
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/* 1126 */     return str;
/*      */   }
/*      */ 
/*      */   public static String getQualifiedName(String[] paramArrayOfString, String paramString)
/*      */   {
/* 1133 */     String str = null;
/* 1134 */     if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)) {
/* 1135 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1136 */         if (i == 0) {
/* 1137 */           str = paramArrayOfString[0];
/*      */         } else {
/* 1139 */           str = str + "::";
/* 1140 */           str = str + paramArrayOfString[i];
/*      */         }
/*      */       }
/* 1143 */       str = str + "::";
/* 1144 */       str = str + paramString;
/*      */     } else {
/* 1146 */       str = paramString;
/*      */     }
/* 1148 */     return str;
/*      */   }
/*      */ 
/*      */   public static String replace(String paramString1, String paramString2, String paramString3)
/*      */   {
/* 1160 */     int i = paramString1.indexOf(paramString2, 0);
/*      */ 
/* 1162 */     if (i >= 0)
/*      */     {
/* 1167 */       StringBuffer localStringBuffer = new StringBuffer(paramString1.length() + 16);
/* 1168 */       int j = paramString2.length();
/* 1169 */       int k = 0;
/*      */ 
/* 1171 */       while (i >= 0) {
/* 1172 */         localStringBuffer.append(paramString1.substring(k, i));
/* 1173 */         localStringBuffer.append(paramString3);
/* 1174 */         k = i + j;
/* 1175 */         i = paramString1.indexOf(paramString2, k);
/*      */       }
/*      */ 
/* 1180 */       if (k < paramString1.length()) {
/* 1181 */         localStringBuffer.append(paramString1.substring(k));
/*      */       }
/*      */ 
/* 1184 */       return localStringBuffer.toString();
/*      */     }
/*      */ 
/* 1190 */     return paramString1;
/*      */   }
/*      */ 
/*      */   public static String getIDLRepositoryID(String paramString)
/*      */   {
/* 1199 */     return "IDL:" + 
/* 1199 */       replace(paramString, "::", "/") + 
/* 1199 */       ":1.0";
/*      */   }
/*      */ 
/*      */   private static String getTypeOrModuleName(String paramString)
/*      */   {
/* 1218 */     String str = convertLeadingUnderscores(paramString);
/*      */ 
/* 1225 */     return convertIDLKeywords(str);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.IDLNames
 * JD-Core Version:    0.6.2
 */