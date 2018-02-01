/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public abstract class BIConversion extends AbstractDeclarationImpl
/*     */ {
/*  95 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "conversion");
/*     */ 
/*     */   @Deprecated
/*     */   public BIConversion(Locator loc)
/*     */   {
/*  73 */     super(loc);
/*     */   }
/*     */ 
/*     */   protected BIConversion()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract TypeUse getTypeUse(XSSimpleType paramXSSimpleType);
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  92 */     return NAME;
/*     */   }
/*     */ 
/*     */   public static final class Static extends BIConversion
/*     */   {
/*     */     private final TypeUse transducer;
/*     */ 
/*     */     public Static(Locator loc, TypeUse transducer)
/*     */     {
/* 108 */       super();
/* 109 */       this.transducer = transducer;
/*     */     }
/*     */ 
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 113 */       return this.transducer;
/*     */     }
/*     */   }
/*     */ 
/*     */   @XmlRootElement(name="javaType")
/*     */   public static class User extends BIConversion
/*     */   {
/*     */ 
/*     */     @XmlAttribute
/*     */     private String parseMethod;
/*     */ 
/*     */     @XmlAttribute
/*     */     private String printMethod;
/*     */ 
/*     */     @XmlAttribute(name="name")
/* 130 */     private String type = "java.lang.String";
/*     */     private JType inMemoryType;
/*     */     private TypeUse typeUse;
/* 281 */     private static final String[] knownBases = { "Float", "Double", "Byte", "Short", "Int", "Long", "Boolean" };
/*     */ 
/* 310 */     public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "javaType");
/*     */ 
/*     */     public User(Locator loc, String parseMethod, String printMethod, JType inMemoryType)
/*     */     {
/* 140 */       super();
/* 141 */       this.parseMethod = parseMethod;
/* 142 */       this.printMethod = printMethod;
/* 143 */       this.inMemoryType = inMemoryType;
/*     */     }
/*     */ 
/*     */     public User()
/*     */     {
/*     */     }
/*     */ 
/*     */     public TypeUse getTypeUse(XSSimpleType owner)
/*     */     {
/* 155 */       if (this.typeUse != null) {
/* 156 */         return this.typeUse;
/*     */       }
/* 158 */       JCodeModel cm = getCodeModel();
/*     */ 
/* 160 */       if (this.inMemoryType == null) {
/* 161 */         this.inMemoryType = TypeUtil.getType(cm, this.type, (ErrorReceiver)Ring.get(ErrorReceiver.class), getLocation());
/*     */       }
/* 163 */       JDefinedClass adapter = generateAdapter(parseMethodFor(owner), printMethodFor(owner), owner);
/*     */ 
/* 166 */       this.typeUse = TypeUseFactory.adapt(CBuiltinLeafInfo.STRING, new CAdapter(adapter));
/*     */ 
/* 168 */       return this.typeUse;
/*     */     }
/*     */ 
/*     */     private JDefinedClass generateAdapter(String parseMethod, String printMethod, XSSimpleType owner)
/*     */     {
/* 175 */       JDefinedClass adapter = null;
/*     */ 
/* 177 */       int id = 1;
/* 178 */       while (adapter == null) {
/*     */         try {
/* 180 */           JPackage pkg = ((ClassSelector)Ring.get(ClassSelector.class)).getClassScope().getOwnerPackage();
/* 181 */           adapter = pkg._class("Adapter" + id);
/*     */         }
/*     */         catch (JClassAlreadyExistsException e)
/*     */         {
/* 186 */           id++;
/*     */         }
/*     */       }
/*     */ 
/* 190 */       JClass bim = this.inMemoryType.boxify();
/*     */ 
/* 192 */       adapter._extends(getCodeModel().ref(XmlAdapter.class).narrow(String.class).narrow(bim));
/*     */ 
/* 194 */       JMethod unmarshal = adapter.method(1, bim, "unmarshal");
/* 195 */       JVar $value = unmarshal.param(String.class, "value");
/*     */       JExpression inv;
/*     */       JExpression inv;
/* 199 */       if (parseMethod.equals("new"))
/*     */       {
/* 204 */         inv = JExpr._new(bim).arg($value);
/*     */       } else {
/* 206 */         int idx = parseMethod.lastIndexOf('.');
/*     */         JExpression inv;
/* 207 */         if (idx < 0)
/*     */         {
/* 213 */           inv = bim.staticInvoke(parseMethod).arg($value);
/*     */         }
/* 215 */         else inv = JExpr.direct(parseMethod + "(value)");
/*     */       }
/*     */ 
/* 218 */       unmarshal.body()._return(inv);
/*     */ 
/* 221 */       JMethod marshal = adapter.method(1, String.class, "marshal");
/* 222 */       $value = marshal.param(bim, "value");
/*     */ 
/* 224 */       if (printMethod.startsWith("javax.xml.bind.DatatypeConverter."))
/*     */       {
/* 227 */         marshal.body()._if($value.eq(JExpr._null()))._then()._return(JExpr._null());
/*     */       }
/*     */ 
/* 230 */       int idx = printMethod.lastIndexOf('.');
/* 231 */       if (idx < 0)
/*     */       {
/* 236 */         inv = $value.invoke(printMethod);
/*     */ 
/* 239 */         JConditional jcon = marshal.body()._if($value.eq(JExpr._null()));
/* 240 */         jcon._then()._return(JExpr._null());
/*     */       }
/* 243 */       else if (this.printMethod == null)
/*     */       {
/* 245 */         JType t = this.inMemoryType.unboxify();
/* 246 */         inv = JExpr.direct(printMethod + "((" + findBaseConversion(owner).toLowerCase() + ")(" + t.fullName() + ")value)");
/*     */       } else {
/* 248 */         inv = JExpr.direct(printMethod + "(value)");
/*     */       }
/* 250 */       marshal.body()._return(inv);
/*     */ 
/* 252 */       return adapter;
/*     */     }
/*     */ 
/*     */     private String printMethodFor(XSSimpleType owner) {
/* 256 */       if (this.printMethod != null) return this.printMethod;
/*     */ 
/* 258 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 259 */         String method = getConversionMethod("print", owner);
/* 260 */         if (method != null) {
/* 261 */           return method;
/*     */         }
/*     */       }
/* 264 */       return "toString";
/*     */     }
/*     */ 
/*     */     private String parseMethodFor(XSSimpleType owner) {
/* 268 */       if (this.parseMethod != null) return this.parseMethod;
/*     */ 
/* 270 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 271 */         String method = getConversionMethod("parse", owner);
/* 272 */         if (method != null)
/*     */         {
/* 274 */           return '(' + this.inMemoryType.unboxify().fullName() + ')' + method;
/*     */         }
/*     */       }
/*     */ 
/* 278 */       return "new";
/*     */     }
/*     */ 
/*     */     private String getConversionMethod(String methodPrefix, XSSimpleType owner)
/*     */     {
/* 286 */       String bc = findBaseConversion(owner);
/* 287 */       if (bc == null) return null;
/*     */ 
/* 289 */       return DatatypeConverter.class.getName() + '.' + methodPrefix + bc;
/*     */     }
/*     */ 
/*     */     private String findBaseConversion(XSSimpleType owner)
/*     */     {
/* 294 */       for (XSSimpleType st = owner; st != null; st = st.getSimpleBaseType()) {
/* 295 */         if ("http://www.w3.org/2001/XMLSchema".equals(st.getTargetNamespace()))
/*     */         {
/* 298 */           String name = st.getName().intern();
/* 299 */           for (String s : knownBases)
/* 300 */             if (name.equalsIgnoreCase(s))
/* 301 */               return s;
/*     */         }
/*     */       }
/* 304 */       return null;
/*     */     }
/*     */     public QName getName() {
/* 307 */       return NAME;
/*     */     }
/*     */   }
/*     */ 
/*     */   @XmlRootElement(name="javaType", namespace="http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   public static class UserAdapter extends BIConversion
/*     */   {
/*     */ 
/*     */     @XmlAttribute(name="name")
/* 316 */     private String type = null;
/*     */ 
/*     */     @XmlAttribute
/* 319 */     private String adapter = null;
/*     */     private TypeUse typeUse;
/*     */ 
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 325 */       if (this.typeUse != null) {
/* 326 */         return this.typeUse;
/*     */       }
/* 328 */       JCodeModel cm = getCodeModel();
/*     */       JDefinedClass a;
/*     */       try {
/* 332 */         JDefinedClass a = cm._class(this.adapter);
/* 333 */         a.hide();
/* 334 */         a._extends(cm.ref(XmlAdapter.class).narrow(String.class).narrow(cm
/* 335 */           .ref(this.type)));
/*     */       }
/*     */       catch (JClassAlreadyExistsException e) {
/* 337 */         a = e.getExistingClass();
/*     */       }
/*     */ 
/* 342 */       this.typeUse = TypeUseFactory.adapt(CBuiltinLeafInfo.STRING, new CAdapter(a));
/*     */ 
/* 346 */       return this.typeUse;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion
 * JD-Core Version:    0.6.2
 */