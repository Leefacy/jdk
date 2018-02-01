/*     */ package com.sun.tools.internal.xjc.reader;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class TypeUtil
/*     */ {
/* 261 */   private static final Comparator<JType> typeComparator = new Comparator() {
/*     */     public int compare(JType t1, JType t2) {
/* 263 */       return t1.fullName().compareTo(t2.fullName());
/*     */     }
/* 261 */   };
/*     */ 
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, Collection<? extends JType> types)
/*     */   {
/*  61 */     return getCommonBaseType(codeModel, (JType[])types.toArray(new JType[types.size()]));
/*     */   }
/*     */ 
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, JType[] t)
/*     */   {
/*  76 */     Set uniqueTypes = new TreeSet(typeComparator);
/*  77 */     for (JType type : t) {
/*  78 */       uniqueTypes.add(type);
/*     */     }
/*     */ 
/*  83 */     if (uniqueTypes.size() == 1) {
/*  84 */       return (JType)uniqueTypes.iterator().next();
/*     */     }
/*     */ 
/*  87 */     assert (!uniqueTypes.isEmpty());
/*     */ 
/*  90 */     uniqueTypes.remove(codeModel.NULL);
/*     */ 
/*  93 */     Set s = null;
/*     */ 
/*  95 */     for (JType type : uniqueTypes) {
/*  96 */       JClass cls = type.boxify();
/*     */ 
/*  98 */       if (s == null)
/*  99 */         s = getAssignableTypes(cls);
/*     */       else {
/* 101 */         s.retainAll(getAssignableTypes(cls));
/*     */       }
/*     */     }
/*     */ 
/* 105 */     s.add(codeModel.ref(Object.class));
/*     */ 
/* 111 */     JClass[] raw = (JClass[])s.toArray(new JClass[s.size()]);
/* 112 */     s.clear();
/*     */ 
/* 114 */     for (int i = 0; i < raw.length; i++)
/*     */     {
/* 116 */       for (int j = 0; (j < raw.length) && (
/* 117 */         (i == j) || 
/* 120 */         (!raw[i].isAssignableFrom(raw[j]))); j++);
/* 124 */       if (j == raw.length)
/*     */       {
/* 126 */         s.add(raw[i]);
/*     */       }
/*     */     }
/* 129 */     assert (!s.isEmpty());
/*     */ 
/* 132 */     JClass result = pickOne(s);
/*     */ 
/* 140 */     if (result.isParameterized()) {
/* 141 */       return result;
/*     */     }
/*     */ 
/* 144 */     List parameters = new ArrayList(uniqueTypes.size());
/* 145 */     int paramLen = -1;
/*     */ 
/* 147 */     for (JType type : uniqueTypes) {
/* 148 */       JClass cls = type.boxify();
/* 149 */       bp = cls.getBaseClass(result);
/*     */ 
/* 153 */       if (bp.equals(result)) {
/* 154 */         return result;
/*     */       }
/* 156 */       assert (bp.isParameterized());
/* 157 */       List tp = bp.getTypeParameters();
/* 158 */       parameters.add(tp);
/*     */ 
/* 160 */       assert ((paramLen == -1) || (paramLen == tp.size()));
/*     */ 
/* 163 */       paramLen = tp.size();
/*     */     }
/*     */     JClass bp;
/* 166 */     Object paramResult = new ArrayList();
/* 167 */     List argList = new ArrayList(parameters.size());
/*     */ 
/* 169 */     for (int i = 0; i < paramLen; i++) {
/* 170 */       argList.clear();
/* 171 */       for (List list : parameters) {
/* 172 */         argList.add(list.get(i));
/*     */       }
/*     */ 
/* 175 */       JClass bound = (JClass)getCommonBaseType(codeModel, argList);
/* 176 */       boolean allSame = true;
/* 177 */       for (JClass a : argList)
/* 178 */         allSame &= a.equals(bound);
/* 179 */       if (!allSame) {
/* 180 */         bound = bound.wildcard();
/*     */       }
/* 182 */       ((List)paramResult).add(bound);
/*     */     }
/*     */ 
/* 185 */     return result.narrow((List)paramResult);
/*     */   }
/*     */ 
/*     */   private static JClass pickOne(Set<JClass> s)
/*     */   {
/* 194 */     for (JClass c : s) {
/* 195 */       if ((c instanceof JDefinedClass)) {
/* 196 */         return c;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 201 */     return (JClass)s.iterator().next();
/*     */   }
/*     */ 
/*     */   private static Set<JClass> getAssignableTypes(JClass t) {
/* 205 */     Set r = new TreeSet(typeComparator);
/* 206 */     getAssignableTypes(t, r);
/* 207 */     return r;
/*     */   }
/*     */ 
/*     */   private static void getAssignableTypes(JClass t, Set<JClass> s)
/*     */   {
/* 219 */     if (!s.add(t)) {
/* 220 */       return;
/*     */     }
/*     */ 
/* 223 */     s.add(t.erasure());
/*     */ 
/* 227 */     JClass _super = t._extends();
/* 228 */     if (_super != null) {
/* 229 */       getAssignableTypes(_super, s);
/*     */     }
/*     */ 
/* 232 */     Iterator itr = t._implements();
/* 233 */     while (itr.hasNext())
/* 234 */       getAssignableTypes((JClass)itr.next(), s);
/*     */   }
/*     */ 
/*     */   public static JType getType(JCodeModel codeModel, String typeName, ErrorReceiver errorHandler, Locator errorSource)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       return codeModel.parseType(typeName);
/*     */     }
/*     */     catch (ClassNotFoundException ee)
/*     */     {
/* 249 */       errorHandler.warning(new SAXParseException(Messages.ERR_CLASS_NOT_FOUND
/* 250 */         .format(new Object[] { typeName }), 
/* 250 */         errorSource));
/*     */     }
/*     */ 
/* 254 */     return codeModel.directClass(typeName);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.TypeUtil
 * JD-Core Version:    0.6.2
 */