/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ public class Names
/*     */ {
/*  39 */   public static final Context.Key<Names> namesKey = new Context.Key();
/*     */   public final Name asterisk;
/*     */   public final Name comma;
/*     */   public final Name empty;
/*     */   public final Name hyphen;
/*     */   public final Name one;
/*     */   public final Name period;
/*     */   public final Name semicolon;
/*     */   public final Name slash;
/*     */   public final Name slashequals;
/*     */   public final Name _class;
/*     */   public final Name _default;
/*     */   public final Name _super;
/*     */   public final Name _this;
/*     */   public final Name _name;
/*     */   public final Name addSuppressed;
/*     */   public final Name any;
/*     */   public final Name append;
/*     */   public final Name clinit;
/*     */   public final Name clone;
/*     */   public final Name close;
/*     */   public final Name compareTo;
/*     */   public final Name deserializeLambda;
/*     */   public final Name desiredAssertionStatus;
/*     */   public final Name equals;
/*     */   public final Name error;
/*     */   public final Name family;
/*     */   public final Name finalize;
/*     */   public final Name forName;
/*     */   public final Name getClass;
/*     */   public final Name getClassLoader;
/*     */   public final Name getComponentType;
/*     */   public final Name getDeclaringClass;
/*     */   public final Name getMessage;
/*     */   public final Name hasNext;
/*     */   public final Name hashCode;
/*     */   public final Name init;
/*     */   public final Name initCause;
/*     */   public final Name iterator;
/*     */   public final Name length;
/*     */   public final Name next;
/*     */   public final Name ordinal;
/*     */   public final Name serialVersionUID;
/*     */   public final Name toString;
/*     */   public final Name value;
/*     */   public final Name valueOf;
/*     */   public final Name values;
/*     */   public final Name java_io_Serializable;
/*     */   public final Name java_lang_AutoCloseable;
/*     */   public final Name java_lang_Class;
/*     */   public final Name java_lang_Cloneable;
/*     */   public final Name java_lang_Enum;
/*     */   public final Name java_lang_Object;
/*     */   public final Name java_lang_invoke_MethodHandle;
/*     */   public final Name Array;
/*     */   public final Name Bound;
/*     */   public final Name Method;
/*     */   public final Name java_lang;
/*     */   public final Name Annotation;
/*     */   public final Name AnnotationDefault;
/*     */   public final Name BootstrapMethods;
/*     */   public final Name Bridge;
/*     */   public final Name CharacterRangeTable;
/*     */   public final Name Code;
/*     */   public final Name CompilationID;
/*     */   public final Name ConstantValue;
/*     */   public final Name Deprecated;
/*     */   public final Name EnclosingMethod;
/*     */   public final Name Enum;
/*     */   public final Name Exceptions;
/*     */   public final Name InnerClasses;
/*     */   public final Name LineNumberTable;
/*     */   public final Name LocalVariableTable;
/*     */   public final Name LocalVariableTypeTable;
/*     */   public final Name MethodParameters;
/*     */   public final Name RuntimeInvisibleAnnotations;
/*     */   public final Name RuntimeInvisibleParameterAnnotations;
/*     */   public final Name RuntimeInvisibleTypeAnnotations;
/*     */   public final Name RuntimeVisibleAnnotations;
/*     */   public final Name RuntimeVisibleParameterAnnotations;
/*     */   public final Name RuntimeVisibleTypeAnnotations;
/*     */   public final Name Signature;
/*     */   public final Name SourceFile;
/*     */   public final Name SourceID;
/*     */   public final Name StackMap;
/*     */   public final Name StackMapTable;
/*     */   public final Name Synthetic;
/*     */   public final Name Value;
/*     */   public final Name Varargs;
/*     */   public final Name ANNOTATION_TYPE;
/*     */   public final Name CONSTRUCTOR;
/*     */   public final Name FIELD;
/*     */   public final Name LOCAL_VARIABLE;
/*     */   public final Name METHOD;
/*     */   public final Name PACKAGE;
/*     */   public final Name PARAMETER;
/*     */   public final Name TYPE;
/*     */   public final Name TYPE_PARAMETER;
/*     */   public final Name TYPE_USE;
/*     */   public final Name CLASS;
/*     */   public final Name RUNTIME;
/*     */   public final Name SOURCE;
/*     */   public final Name T;
/*     */   public final Name deprecated;
/*     */   public final Name ex;
/*     */   public final Name package_info;
/*     */   public final Name lambda;
/*     */   public final Name metafactory;
/*     */   public final Name altMetafactory;
/*     */   public final Name.Table table;
/*     */ 
/*     */   public static Names instance(Context paramContext)
/*     */   {
/*  42 */     Names localNames = (Names)paramContext.get(namesKey);
/*  43 */     if (localNames == null) {
/*  44 */       localNames = new Names(paramContext);
/*  45 */       paramContext.put(namesKey, localNames);
/*     */     }
/*  47 */     return localNames;
/*     */   }
/*     */ 
/*     */   public Names(Context paramContext)
/*     */   {
/* 183 */     Options localOptions = Options.instance(paramContext);
/* 184 */     this.table = createTable(localOptions);
/*     */ 
/* 187 */     this.asterisk = fromString("*");
/* 188 */     this.comma = fromString(",");
/* 189 */     this.empty = fromString("");
/* 190 */     this.hyphen = fromString("-");
/* 191 */     this.one = fromString("1");
/* 192 */     this.period = fromString(".");
/* 193 */     this.semicolon = fromString(";");
/* 194 */     this.slash = fromString("/");
/* 195 */     this.slashequals = fromString("/=");
/*     */ 
/* 198 */     this._class = fromString("class");
/* 199 */     this._default = fromString("default");
/* 200 */     this._super = fromString("super");
/* 201 */     this._this = fromString("this");
/*     */ 
/* 204 */     this._name = fromString("name");
/* 205 */     this.addSuppressed = fromString("addSuppressed");
/* 206 */     this.any = fromString("<any>");
/* 207 */     this.append = fromString("append");
/* 208 */     this.clinit = fromString("<clinit>");
/* 209 */     this.clone = fromString("clone");
/* 210 */     this.close = fromString("close");
/* 211 */     this.compareTo = fromString("compareTo");
/* 212 */     this.deserializeLambda = fromString("$deserializeLambda$");
/* 213 */     this.desiredAssertionStatus = fromString("desiredAssertionStatus");
/* 214 */     this.equals = fromString("equals");
/* 215 */     this.error = fromString("<error>");
/* 216 */     this.family = fromString("family");
/* 217 */     this.finalize = fromString("finalize");
/* 218 */     this.forName = fromString("forName");
/* 219 */     this.getClass = fromString("getClass");
/* 220 */     this.getClassLoader = fromString("getClassLoader");
/* 221 */     this.getComponentType = fromString("getComponentType");
/* 222 */     this.getDeclaringClass = fromString("getDeclaringClass");
/* 223 */     this.getMessage = fromString("getMessage");
/* 224 */     this.hasNext = fromString("hasNext");
/* 225 */     this.hashCode = fromString("hashCode");
/* 226 */     this.init = fromString("<init>");
/* 227 */     this.initCause = fromString("initCause");
/* 228 */     this.iterator = fromString("iterator");
/* 229 */     this.length = fromString("length");
/* 230 */     this.next = fromString("next");
/* 231 */     this.ordinal = fromString("ordinal");
/* 232 */     this.serialVersionUID = fromString("serialVersionUID");
/* 233 */     this.toString = fromString("toString");
/* 234 */     this.value = fromString("value");
/* 235 */     this.valueOf = fromString("valueOf");
/* 236 */     this.values = fromString("values");
/*     */ 
/* 239 */     this.java_io_Serializable = fromString("java.io.Serializable");
/* 240 */     this.java_lang_AutoCloseable = fromString("java.lang.AutoCloseable");
/* 241 */     this.java_lang_Class = fromString("java.lang.Class");
/* 242 */     this.java_lang_Cloneable = fromString("java.lang.Cloneable");
/* 243 */     this.java_lang_Enum = fromString("java.lang.Enum");
/* 244 */     this.java_lang_Object = fromString("java.lang.Object");
/* 245 */     this.java_lang_invoke_MethodHandle = fromString("java.lang.invoke.MethodHandle");
/*     */ 
/* 248 */     this.Array = fromString("Array");
/* 249 */     this.Bound = fromString("Bound");
/* 250 */     this.Method = fromString("Method");
/*     */ 
/* 253 */     this.java_lang = fromString("java.lang");
/*     */ 
/* 256 */     this.Annotation = fromString("Annotation");
/* 257 */     this.AnnotationDefault = fromString("AnnotationDefault");
/* 258 */     this.BootstrapMethods = fromString("BootstrapMethods");
/* 259 */     this.Bridge = fromString("Bridge");
/* 260 */     this.CharacterRangeTable = fromString("CharacterRangeTable");
/* 261 */     this.Code = fromString("Code");
/* 262 */     this.CompilationID = fromString("CompilationID");
/* 263 */     this.ConstantValue = fromString("ConstantValue");
/* 264 */     this.Deprecated = fromString("Deprecated");
/* 265 */     this.EnclosingMethod = fromString("EnclosingMethod");
/* 266 */     this.Enum = fromString("Enum");
/* 267 */     this.Exceptions = fromString("Exceptions");
/* 268 */     this.InnerClasses = fromString("InnerClasses");
/* 269 */     this.LineNumberTable = fromString("LineNumberTable");
/* 270 */     this.LocalVariableTable = fromString("LocalVariableTable");
/* 271 */     this.LocalVariableTypeTable = fromString("LocalVariableTypeTable");
/* 272 */     this.MethodParameters = fromString("MethodParameters");
/* 273 */     this.RuntimeInvisibleAnnotations = fromString("RuntimeInvisibleAnnotations");
/* 274 */     this.RuntimeInvisibleParameterAnnotations = fromString("RuntimeInvisibleParameterAnnotations");
/* 275 */     this.RuntimeInvisibleTypeAnnotations = fromString("RuntimeInvisibleTypeAnnotations");
/* 276 */     this.RuntimeVisibleAnnotations = fromString("RuntimeVisibleAnnotations");
/* 277 */     this.RuntimeVisibleParameterAnnotations = fromString("RuntimeVisibleParameterAnnotations");
/* 278 */     this.RuntimeVisibleTypeAnnotations = fromString("RuntimeVisibleTypeAnnotations");
/* 279 */     this.Signature = fromString("Signature");
/* 280 */     this.SourceFile = fromString("SourceFile");
/* 281 */     this.SourceID = fromString("SourceID");
/* 282 */     this.StackMap = fromString("StackMap");
/* 283 */     this.StackMapTable = fromString("StackMapTable");
/* 284 */     this.Synthetic = fromString("Synthetic");
/* 285 */     this.Value = fromString("Value");
/* 286 */     this.Varargs = fromString("Varargs");
/*     */ 
/* 289 */     this.ANNOTATION_TYPE = fromString("ANNOTATION_TYPE");
/* 290 */     this.CONSTRUCTOR = fromString("CONSTRUCTOR");
/* 291 */     this.FIELD = fromString("FIELD");
/* 292 */     this.LOCAL_VARIABLE = fromString("LOCAL_VARIABLE");
/* 293 */     this.METHOD = fromString("METHOD");
/* 294 */     this.PACKAGE = fromString("PACKAGE");
/* 295 */     this.PARAMETER = fromString("PARAMETER");
/* 296 */     this.TYPE = fromString("TYPE");
/* 297 */     this.TYPE_PARAMETER = fromString("TYPE_PARAMETER");
/* 298 */     this.TYPE_USE = fromString("TYPE_USE");
/*     */ 
/* 301 */     this.CLASS = fromString("CLASS");
/* 302 */     this.RUNTIME = fromString("RUNTIME");
/* 303 */     this.SOURCE = fromString("SOURCE");
/*     */ 
/* 306 */     this.T = fromString("T");
/* 307 */     this.deprecated = fromString("deprecated");
/* 308 */     this.ex = fromString("ex");
/* 309 */     this.package_info = fromString("package-info");
/*     */ 
/* 312 */     this.lambda = fromString("lambda$");
/* 313 */     this.metafactory = fromString("metafactory");
/* 314 */     this.altMetafactory = fromString("altMetafactory");
/*     */   }
/*     */ 
/*     */   protected Name.Table createTable(Options paramOptions) {
/* 318 */     boolean bool = paramOptions.isSet("useUnsharedTable");
/* 319 */     if (bool) {
/* 320 */       return new UnsharedNameTable(this);
/*     */     }
/* 322 */     return new SharedNameTable(this);
/*     */   }
/*     */ 
/*     */   public void dispose() {
/* 326 */     this.table.dispose();
/*     */   }
/*     */ 
/*     */   public Name fromChars(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 330 */     return this.table.fromChars(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Name fromString(String paramString) {
/* 334 */     return this.table.fromString(paramString);
/*     */   }
/*     */ 
/*     */   public Name fromUtf(byte[] paramArrayOfByte) {
/* 338 */     return this.table.fromUtf(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public Name fromUtf(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 342 */     return this.table.fromUtf(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Names
 * JD-Core Version:    0.6.2
 */