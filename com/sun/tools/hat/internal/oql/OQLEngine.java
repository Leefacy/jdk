/*     */ package com.sun.tools.hat.internal.oql;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class OQLEngine
/*     */ {
/*     */   private Object engine;
/*     */   private Method evalMethod;
/*     */   private Method invokeMethod;
/*     */   private Snapshot snapshot;
/* 309 */   private static boolean debug = false;
/*     */   private static boolean oqlSupported;
/*     */ 
/*     */   public static boolean isOQLSupported()
/*     */   {
/*  64 */     return oqlSupported;
/*     */   }
/*     */ 
/*     */   public OQLEngine(Snapshot paramSnapshot) {
/*  68 */     if (!isOQLSupported()) {
/*  69 */       throw new UnsupportedOperationException("OQL not supported");
/*     */     }
/*  71 */     init(paramSnapshot);
/*     */   }
/*     */ 
/*     */   public synchronized void executeQuery(String paramString, ObjectVisitor paramObjectVisitor)
/*     */     throws OQLException
/*     */   {
/*  84 */     debugPrint("query : " + paramString);
/*  85 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/*  86 */     if (localStringTokenizer.hasMoreTokens()) {
/*  87 */       str1 = localStringTokenizer.nextToken();
/*  88 */       if (!str1.equals("select"))
/*     */       {
/*     */         try
/*     */         {
/*  92 */           Object localObject1 = evalScript(paramString);
/*  93 */           paramObjectVisitor.visit(localObject1);
/*     */         } catch (Exception localException) {
/*  95 */           throw new OQLException(localException);
/*     */         }
/*  97 */         return;
/*     */       }
/*     */     } else {
/* 100 */       throw new OQLException("query syntax error: no 'select' clause");
/*     */     }
/*     */ 
/* 103 */     String str1 = "";
/* 104 */     int i = 0;
/* 105 */     while (localStringTokenizer.hasMoreTokens()) {
/* 106 */       localObject2 = localStringTokenizer.nextToken();
/* 107 */       if (((String)localObject2).equals("from")) {
/* 108 */         i = 1;
/* 109 */         break;
/*     */       }
/* 111 */       str1 = str1 + " " + (String)localObject2;
/*     */     }
/*     */ 
/* 114 */     if (str1.equals("")) {
/* 115 */       throw new OQLException("query syntax error: 'select' expression can not be empty");
/*     */     }
/*     */ 
/* 118 */     Object localObject2 = null;
/* 119 */     boolean bool = false;
/* 120 */     String str2 = null;
/* 121 */     String str3 = null;
/*     */ 
/* 123 */     if (i != 0)
/*     */     {
/*     */       String str4;
/* 124 */       if (localStringTokenizer.hasMoreTokens()) {
/* 125 */         str4 = localStringTokenizer.nextToken();
/* 126 */         if (str4.equals("instanceof")) {
/* 127 */           bool = true;
/* 128 */           if (!localStringTokenizer.hasMoreTokens()) {
/* 129 */             throw new OQLException("no class name after 'instanceof'");
/*     */           }
/* 131 */           localObject2 = localStringTokenizer.nextToken();
/*     */         } else {
/* 133 */           localObject2 = str4;
/*     */         }
/*     */       } else {
/* 136 */         throw new OQLException("query syntax error: class name must follow 'from'");
/*     */       }
/*     */ 
/* 139 */       if (localStringTokenizer.hasMoreTokens()) {
/* 140 */         str3 = localStringTokenizer.nextToken();
/* 141 */         if (str3.equals("where")) {
/* 142 */           throw new OQLException("query syntax error: identifier should follow class name");
/*     */         }
/* 144 */         if (localStringTokenizer.hasMoreTokens()) {
/* 145 */           str4 = localStringTokenizer.nextToken();
/* 146 */           if (!str4.equals("where")) {
/* 147 */             throw new OQLException("query syntax error: 'where' clause expected after 'from' clause");
/*     */           }
/*     */ 
/* 150 */           str2 = "";
/* 151 */           while (localStringTokenizer.hasMoreTokens()) {
/* 152 */             str2 = str2 + " " + localStringTokenizer.nextToken();
/*     */           }
/* 154 */           if (str2.equals(""))
/* 155 */             throw new OQLException("query syntax error: 'where' clause cannot have empty expression");
/*     */         }
/*     */       }
/*     */       else {
/* 159 */         throw new OQLException("query syntax error: identifier should follow class name");
/*     */       }
/*     */     }
/*     */ 
/* 163 */     executeQuery(new OQLQuery(str1, bool, (String)localObject2, str3, str2), paramObjectVisitor);
/*     */   }
/*     */ 
/*     */   private void executeQuery(OQLQuery paramOQLQuery, ObjectVisitor paramObjectVisitor)
/*     */     throws OQLException
/*     */   {
/* 169 */     JavaClass localJavaClass = null;
/* 170 */     if (paramOQLQuery.className != null) {
/* 171 */       localJavaClass = this.snapshot.findClass(paramOQLQuery.className);
/* 172 */       if (localJavaClass == null) {
/* 173 */         throw new OQLException(paramOQLQuery.className + " is not found!");
/*     */       }
/*     */     }
/*     */ 
/* 177 */     StringBuffer localStringBuffer = new StringBuffer();
/* 178 */     localStringBuffer.append("function __select__(");
/* 179 */     if (paramOQLQuery.identifier != null) {
/* 180 */       localStringBuffer.append(paramOQLQuery.identifier);
/*     */     }
/* 182 */     localStringBuffer.append(") { return ");
/* 183 */     localStringBuffer.append(paramOQLQuery.selectExpr.replace('\n', ' '));
/* 184 */     localStringBuffer.append("; }");
/*     */ 
/* 186 */     String str1 = localStringBuffer.toString();
/* 187 */     debugPrint(str1);
/* 188 */     String str2 = null;
/* 189 */     if (paramOQLQuery.whereExpr != null) {
/* 190 */       localStringBuffer = new StringBuffer();
/* 191 */       localStringBuffer.append("function __where__(");
/* 192 */       localStringBuffer.append(paramOQLQuery.identifier);
/* 193 */       localStringBuffer.append(") { return ");
/* 194 */       localStringBuffer.append(paramOQLQuery.whereExpr.replace('\n', ' '));
/* 195 */       localStringBuffer.append("; }");
/* 196 */       str2 = localStringBuffer.toString();
/*     */     }
/* 198 */     debugPrint(str2);
/*     */     try
/*     */     {
/* 202 */       this.evalMethod.invoke(this.engine, new Object[] { str1 });
/* 203 */       if (str2 != null)
/* 204 */         this.evalMethod.invoke(this.engine, new Object[] { str2 });
/*     */       Object localObject1;
/* 207 */       if (paramOQLQuery.className != null) {
/* 208 */         localObject1 = localJavaClass.getInstances(paramOQLQuery.isInstanceOf);
/* 209 */         while (((Enumeration)localObject1).hasMoreElements()) {
/* 210 */           JavaHeapObject localJavaHeapObject = (JavaHeapObject)((Enumeration)localObject1).nextElement();
/* 211 */           Object[] arrayOfObject = { wrapJavaObject(localJavaHeapObject) };
/* 212 */           boolean bool = str2 == null;
/*     */           Object localObject2;
/* 213 */           if (!bool) {
/* 214 */             localObject2 = call("__where__", arrayOfObject);
/* 215 */             if ((localObject2 instanceof Boolean))
/* 216 */               bool = ((Boolean)localObject2).booleanValue();
/* 217 */             else if ((localObject2 instanceof Number))
/* 218 */               bool = ((Number)localObject2).intValue() != 0;
/*     */             else {
/* 220 */               bool = localObject2 != null;
/*     */             }
/*     */           }
/*     */ 
/* 224 */           if (bool) {
/* 225 */             localObject2 = call("__select__", arrayOfObject);
/* 226 */             if (paramObjectVisitor.visit(localObject2)) return; 
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 231 */         localObject1 = call("__select__", new Object[0]);
/* 232 */         paramObjectVisitor.visit(localObject1);
/*     */       }
/*     */     } catch (Exception localException) {
/* 235 */       throw new OQLException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object evalScript(String paramString) throws Exception {
/* 240 */     return this.evalMethod.invoke(this.engine, new Object[] { paramString });
/*     */   }
/*     */ 
/*     */   public Object wrapJavaObject(JavaHeapObject paramJavaHeapObject) throws Exception {
/* 244 */     return call("wrapJavaObject", new Object[] { paramJavaHeapObject });
/*     */   }
/*     */ 
/*     */   public Object toHtml(Object paramObject) throws Exception {
/* 248 */     return call("toHtml", new Object[] { paramObject });
/*     */   }
/*     */ 
/*     */   public Object call(String paramString, Object[] paramArrayOfObject) throws Exception {
/* 252 */     return this.invokeMethod.invoke(this.engine, new Object[] { paramString, paramArrayOfObject });
/*     */   }
/*     */ 
/*     */   private static void debugPrint(String paramString) {
/* 256 */     if (debug) System.out.println(paramString); 
/*     */   }
/*     */ 
/*     */   private void init(Snapshot paramSnapshot) throws RuntimeException {
/* 260 */     this.snapshot = paramSnapshot;
/*     */     try
/*     */     {
/* 263 */       Class localClass1 = Class.forName("javax.script.ScriptEngineManager");
/* 264 */       Object localObject = localClass1.newInstance();
/*     */ 
/* 267 */       Method localMethod1 = localClass1.getMethod("getEngineByName", new Class[] { String.class });
/*     */ 
/* 269 */       this.engine = localMethod1.invoke(localObject, new Object[] { "js" });
/*     */ 
/* 272 */       InputStream localInputStream = getInitStream();
/* 273 */       Class localClass2 = Class.forName("javax.script.ScriptEngine");
/* 274 */       this.evalMethod = localClass2.getMethod("eval", new Class[] { Reader.class });
/*     */ 
/* 276 */       this.evalMethod.invoke(this.engine, new Object[] { new InputStreamReader(localInputStream) });
/*     */ 
/* 280 */       Class localClass3 = Class.forName("javax.script.Invocable");
/*     */ 
/* 282 */       this.evalMethod = localClass2.getMethod("eval", new Class[] { String.class });
/*     */ 
/* 284 */       this.invokeMethod = localClass3.getMethod("invokeFunction", new Class[] { String.class, [Ljava.lang.Object.class });
/*     */ 
/* 288 */       Method localMethod2 = localClass2.getMethod("put", new Class[] { String.class, Object.class });
/*     */ 
/* 292 */       localMethod2.invoke(this.engine, new Object[] { "heap", 
/* 293 */         call("wrapHeapSnapshot", new Object[] { paramSnapshot }) });
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 296 */       if (debug) localException.printStackTrace();
/* 297 */       throw new RuntimeException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private InputStream getInitStream() {
/* 302 */     return getClass().getResourceAsStream("/com/sun/tools/hat/resources/hat.js");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  49 */       Class localClass = Class.forName("javax.script.ScriptEngineManager");
/*  50 */       Object localObject1 = localClass.newInstance();
/*     */ 
/*  53 */       Method localMethod = localClass.getMethod("getEngineByName", new Class[] { String.class });
/*     */ 
/*  55 */       Object localObject2 = localMethod.invoke(localObject1, new Object[] { "js" });
/*  56 */       oqlSupported = localObject2 != null;
/*     */     } catch (Exception localException) {
/*  58 */       oqlSupported = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.oql.OQLEngine
 * JD-Core Version:    0.6.2
 */