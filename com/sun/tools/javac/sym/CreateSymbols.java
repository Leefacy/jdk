/*     */ package com.sun.tools.javac.sym;
/*     */ 
/*     */ import com.sun.tools.javac.Main;
/*     */ import com.sun.tools.javac.api.JavacTaskImpl;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.jvm.ClassWriter;
/*     */ import com.sun.tools.javac.jvm.ClassWriter.PoolOverflow;
/*     */ import com.sun.tools.javac.jvm.ClassWriter.StringOverflow;
/*     */ import com.sun.tools.javac.jvm.Pool;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedOptions;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ import javax.tools.ToolProvider;
/*     */ 
/*     */ @SupportedOptions({"com.sun.tools.javac.sym.Jar", "com.sun.tools.javac.sym.Dest", "com.sun.tools.javac.sym.Profiles"})
/*     */ @SupportedAnnotationTypes({"*"})
/*     */ public class CreateSymbols extends AbstractProcessor
/*     */ {
/*     */   static Set<String> getLegacyPackages()
/*     */   {
/*  98 */     ResourceBundle localResourceBundle = ResourceBundle.getBundle("com.sun.tools.javac.resources.legacy");
/*     */ 
/*  99 */     HashSet localHashSet = new HashSet();
/* 100 */     for (Enumeration localEnumeration = localResourceBundle.getKeys(); localEnumeration.hasMoreElements(); )
/* 101 */       localHashSet.add(localEnumeration.nextElement());
/* 102 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*     */     try {
/* 107 */       if (paramRoundEnvironment.processingOver())
/* 108 */         createSymbols();
/*     */     } catch (IOException localIOException) {
/* 110 */       localObject = localIOException.getLocalizedMessage();
/* 111 */       if (localObject == null)
/* 112 */         localObject = localIOException.toString();
/* 113 */       this.processingEnv.getMessager()
/* 114 */         .printMessage(Diagnostic.Kind.ERROR, (CharSequence)localObject);
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 116 */       localThrowable.printStackTrace();
/* 117 */       Object localObject = localThrowable.getCause();
/* 118 */       if (localObject == null)
/* 119 */         localObject = localThrowable;
/* 120 */       String str = ((Throwable)localObject).getLocalizedMessage();
/* 121 */       if (str == null)
/* 122 */         str = ((Throwable)localObject).toString();
/* 123 */       this.processingEnv.getMessager()
/* 124 */         .printMessage(Diagnostic.Kind.ERROR, str);
/*     */     }
/*     */ 
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   void createSymbols() throws IOException {
/* 130 */     Set localSet1 = getLegacyPackages();
/* 131 */     Set localSet2 = getLegacyPackages();
/* 132 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 134 */     Set localSet3 = ((JavacProcessingEnvironment)this.processingEnv)
/* 134 */       .getSpecifiedPackages();
/* 135 */     Map localMap = this.processingEnv.getOptions();
/* 136 */     String str1 = (String)localMap.get("com.sun.tools.javac.sym.Jar");
/* 137 */     if (str1 == null)
/* 138 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Jar=LOCATION_OF_JAR");
/* 139 */     String str2 = (String)localMap.get("com.sun.tools.javac.sym.Dest");
/* 140 */     if (str2 == null)
/* 141 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Dest=LOCATION_OF_JAR");
/* 142 */     String str3 = (String)localMap.get("com.sun.tools.javac.sym.Profiles");
/* 143 */     if (str3 == null)
/* 144 */       throw new RuntimeException("Must use -Acom.sun.tools.javac.sym.Profiles=PROFILES_SPEC");
/* 145 */     Profiles localProfiles = Profiles.read(new File(str3));
/*     */ 
/* 147 */     for (Object localObject1 = localSet3.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Symbol.PackageSymbol)((Iterator)localObject1).next();
/* 148 */       localObject3 = ((Symbol.PackageSymbol)localObject2).getQualifiedName().toString();
/* 149 */       localSet2.remove(localObject3);
/* 150 */       localHashSet.add(localObject3);
/*     */     }
/*     */ 
/* 153 */     localObject1 = ToolProvider.getSystemJavaCompiler();
/* 154 */     Object localObject2 = ((javax.tools.JavaCompiler)localObject1).getStandardFileManager(null, null, null);
/* 155 */     Object localObject3 = StandardLocation.locationFor(str1);
/* 156 */     File localFile = new File(str1);
/* 157 */     ((StandardJavaFileManager)localObject2).setLocation((JavaFileManager.Location)localObject3, List.of(localFile));
/* 158 */     ((StandardJavaFileManager)localObject2).setLocation(StandardLocation.CLASS_PATH, List.nil());
/* 159 */     ((StandardJavaFileManager)localObject2).setLocation(StandardLocation.SOURCE_PATH, List.nil());
/*     */ 
/* 161 */     Object localObject4 = new ArrayList();
/* 162 */     ((ArrayList)localObject4).add(localFile);
/* 163 */     for (Object localObject5 = ((StandardJavaFileManager)localObject2).getLocation(StandardLocation.PLATFORM_CLASS_PATH).iterator(); ((Iterator)localObject5).hasNext(); ) { localObject6 = (File)((Iterator)localObject5).next();
/* 164 */       if (!new File(((File)localObject6).getName()).equals(new File("rt.jar")))
/* 165 */         ((ArrayList)localObject4).add(localObject6);
/*     */     }
/* 167 */     System.err.println("Using boot class path = " + localObject4);
/* 168 */     ((StandardJavaFileManager)localObject2).setLocation(StandardLocation.PLATFORM_CLASS_PATH, (Iterable)localObject4);
/*     */ 
/* 171 */     localObject4 = new File(str2);
/* 172 */     if ((!((File)localObject4).exists()) && 
/* 173 */       (!((File)localObject4).mkdirs()))
/* 174 */       throw new RuntimeException("Could not create " + localObject4);
/* 175 */     ((StandardJavaFileManager)localObject2).setLocation(StandardLocation.CLASS_OUTPUT, List.of(localObject4));
/* 176 */     localObject5 = new HashSet();
/* 177 */     Object localObject6 = new HashSet();
/* 178 */     List localList = List.of("-XDdev");
/*     */ 
/* 182 */     JavacTaskImpl localJavacTaskImpl = (JavacTaskImpl)((javax.tools.JavaCompiler)localObject1)
/* 182 */       .getTask(null, (JavaFileManager)localObject2, null, localList, null, null);
/*     */ 
/* 184 */     com.sun.tools.javac.main.JavaCompiler localJavaCompiler = com.sun.tools.javac.main.JavaCompiler.instance(localJavacTaskImpl
/* 184 */       .getContext());
/* 185 */     ClassWriter localClassWriter = ClassWriter.instance(localJavacTaskImpl.getContext());
/* 186 */     Symtab localSymtab = Symtab.instance(localJavacTaskImpl.getContext());
/* 187 */     Names localNames = Names.instance(localJavacTaskImpl.getContext());
/*     */ 
/* 190 */     Attribute.Compound localCompound = new Attribute.Compound(localSymtab.proprietaryType, 
/* 190 */       List.nil());
/* 191 */     Attribute.Compound[] arrayOfCompound = new Attribute.Compound[localProfiles.getProfileCount() + 1];
/* 192 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localSymtab.profileType.tsym.members().lookup(localNames.value).sym;
/* 193 */     for (int i = 1; i < arrayOfCompound.length; i++) {
/* 194 */       arrayOfCompound[i] = new Attribute.Compound(localSymtab.profileType, 
/* 195 */         List.of(new Pair(localMethodSymbol, new Attribute.Constant(localSymtab.intType, 
/* 196 */         Integer.valueOf(i)))));
/*     */     }
/*     */ 
/* 199 */     Type.moreInfo = true;
/* 200 */     Types localTypes = Types.instance(localJavacTaskImpl.getContext());
/* 201 */     Pool localPool = new Pool(localTypes);
/* 202 */     for (JavaFileObject localJavaFileObject : ((StandardJavaFileManager)localObject2).list((JavaFileManager.Location)localObject3, "", EnumSet.of(JavaFileObject.Kind.CLASS), true)) {
/* 203 */       String str4 = ((StandardJavaFileManager)localObject2).inferBinaryName((JavaFileManager.Location)localObject3, localJavaFileObject);
/* 204 */       int j = str4.lastIndexOf('.');
/* 205 */       String str5 = j == -1 ? "" : str4.substring(0, j);
/* 206 */       int k = 0;
/* 207 */       if (localHashSet.contains(str5)) {
/* 208 */         if (!localSet1.contains(str5))
/* 209 */           ((Set)localObject6).add(str5);
/*     */       }
/* 211 */       else if (localSet2.contains(str5)) {
/* 212 */         k = 1;
/*     */       }
/*     */       else
/*     */       {
/* 216 */         ((Set)localObject5).add(str5);
/* 217 */         continue;
/*     */       }
/* 219 */       Symbol.TypeSymbol localTypeSymbol = (Symbol.TypeSymbol)localJavaCompiler.resolveIdent(str4);
/* 220 */       if (localTypeSymbol.kind != 2) {
/* 221 */         if (str4.indexOf('$') < 0) {
/* 222 */           System.err.println("Ignoring (other) " + str4 + " : " + localTypeSymbol);
/* 223 */           System.err.println("   " + localTypeSymbol.getClass().getSimpleName() + " " + localTypeSymbol.type);
/*     */         }
/*     */       }
/*     */       else {
/* 227 */         localTypeSymbol.complete();
/* 228 */         if (localTypeSymbol.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
/* 229 */           System.err.println("Ignoring (bad) " + localTypeSymbol.getQualifiedName());
/*     */         }
/*     */         else {
/* 232 */           Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localTypeSymbol;
/* 233 */           if (k != 0) {
/* 234 */             localClassSymbol.prependAttributes(List.of(localCompound));
/*     */           }
/* 236 */           int m = localProfiles.getProfile(localClassSymbol.fullname.toString().replace(".", "/"));
/* 237 */           if ((0 < m) && (m < arrayOfCompound.length))
/* 238 */             localClassSymbol.prependAttributes(List.of(arrayOfCompound[m]));
/* 239 */           writeClass(localPool, localClassSymbol, localClassWriter);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeClass(Pool paramPool, Symbol.ClassSymbol paramClassSymbol, ClassWriter paramClassWriter)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 258 */       paramPool.reset();
/* 259 */       paramClassSymbol.pool = paramPool;
/* 260 */       paramClassWriter.writeClass(paramClassSymbol);
/* 261 */       for (Scope.Entry localEntry = paramClassSymbol.members().elems; localEntry != null; localEntry = localEntry.sibling)
/* 262 */         if (localEntry.sym.kind == 2) {
/* 263 */           Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localEntry.sym;
/* 264 */           localClassSymbol.complete();
/* 265 */           writeClass(paramPool, localClassSymbol, paramClassWriter);
/*     */         }
/*     */     }
/*     */     catch (ClassWriter.StringOverflow localStringOverflow) {
/* 269 */       throw new RuntimeException(localStringOverflow);
/*     */     } catch (ClassWriter.PoolOverflow localPoolOverflow) {
/* 271 */       throw new RuntimeException(localPoolOverflow);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SourceVersion getSupportedSourceVersion() {
/* 276 */     return SourceVersion.latest();
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 281 */     String str1 = paramArrayOfString[0];
/* 282 */     String str2 = paramArrayOfString[1];
/* 283 */     paramArrayOfString = new String[] { "-Xbootclasspath:" + str1, "-XDprocess.packages", "-proc:only", "-processor", "com.sun.tools.javac.sym.CreateSymbols", "-Acom.sun.tools.javac.sym.Jar=" + str1, "-Acom.sun.tools.javac.sym.Dest=" + str2, "java.applet", "java.awt", "java.awt.color", "java.awt.datatransfer", "java.awt.dnd", "java.awt.event", "java.awt.font", "java.awt.geom", "java.awt.im", "java.awt.im.spi", "java.awt.image", "java.awt.image.renderable", "java.awt.print", "java.beans", "java.beans.beancontext", "java.io", "java.lang", "java.lang.annotation", "java.lang.instrument", "java.lang.management", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.nio", "java.nio.channels", "java.nio.channels.spi", "java.nio.charset", "java.nio.charset.spi", "java.rmi", "java.rmi.activation", "java.rmi.dgc", "java.rmi.registry", "java.rmi.server", "java.security", "java.security.acl", "java.security.cert", "java.security.interfaces", "java.security.spec", "java.sql", "java.text", "java.text.spi", "java.util", "java.util.concurrent", "java.util.concurrent.atomic", "java.util.concurrent.locks", "java.util.jar", "java.util.logging", "java.util.prefs", "java.util.regex", "java.util.spi", "java.util.zip", "javax.accessibility", "javax.activation", "javax.activity", "javax.annotation", "javax.annotation.processing", "javax.crypto", "javax.crypto.interfaces", "javax.crypto.spec", "javax.imageio", "javax.imageio.event", "javax.imageio.metadata", "javax.imageio.plugins.jpeg", "javax.imageio.plugins.bmp", "javax.imageio.spi", "javax.imageio.stream", "javax.jws", "javax.jws.soap", "javax.lang.model", "javax.lang.model.element", "javax.lang.model.type", "javax.lang.model.util", "javax.management", "javax.management.loading", "javax.management.monitor", "javax.management.relation", "javax.management.openmbean", "javax.management.timer", "javax.management.modelmbean", "javax.management.remote", "javax.management.remote.rmi", "javax.naming", "javax.naming.directory", "javax.naming.event", "javax.naming.ldap", "javax.naming.spi", "javax.net", "javax.net.ssl", "javax.print", "javax.print.attribute", "javax.print.attribute.standard", "javax.print.event", "javax.rmi", "javax.rmi.CORBA", "javax.rmi.ssl", "javax.script", "javax.security.auth", "javax.security.auth.callback", "javax.security.auth.kerberos", "javax.security.auth.login", "javax.security.auth.spi", "javax.security.auth.x500", "javax.security.cert", "javax.security.sasl", "javax.sound.sampled", "javax.sound.sampled.spi", "javax.sound.midi", "javax.sound.midi.spi", "javax.sql", "javax.sql.rowset", "javax.sql.rowset.serial", "javax.sql.rowset.spi", "javax.swing", "javax.swing.border", "javax.swing.colorchooser", "javax.swing.filechooser", "javax.swing.event", "javax.swing.table", "javax.swing.text", "javax.swing.text.html", "javax.swing.text.html.parser", "javax.swing.text.rtf", "javax.swing.tree", "javax.swing.undo", "javax.swing.plaf", "javax.swing.plaf.basic", "javax.swing.plaf.metal", "javax.swing.plaf.multi", "javax.swing.plaf.synth", "javax.tools", "javax.transaction", "javax.transaction.xa", "javax.xml.parsers", "javax.xml.bind", "javax.xml.bind.annotation", "javax.xml.bind.annotation.adapters", "javax.xml.bind.attachment", "javax.xml.bind.helpers", "javax.xml.bind.util", "javax.xml.soap", "javax.xml.ws", "javax.xml.ws.handler", "javax.xml.ws.handler.soap", "javax.xml.ws.http", "javax.xml.ws.soap", "javax.xml.ws.spi", "javax.xml.transform", "javax.xml.transform.sax", "javax.xml.transform.dom", "javax.xml.transform.stax", "javax.xml.transform.stream", "javax.xml", "javax.xml.crypto", "javax.xml.crypto.dom", "javax.xml.crypto.dsig", "javax.xml.crypto.dsig.dom", "javax.xml.crypto.dsig.keyinfo", "javax.xml.crypto.dsig.spec", "javax.xml.datatype", "javax.xml.validation", "javax.xml.namespace", "javax.xml.xpath", "javax.xml.stream", "javax.xml.stream.events", "javax.xml.stream.util", "org.ietf.jgss", "org.omg.CORBA", "org.omg.CORBA.DynAnyPackage", "org.omg.CORBA.ORBPackage", "org.omg.CORBA.TypeCodePackage", "org.omg.stub.java.rmi", "org.omg.CORBA.portable", "org.omg.CORBA_2_3", "org.omg.CORBA_2_3.portable", "org.omg.CosNaming", "org.omg.CosNaming.NamingContextExtPackage", "org.omg.CosNaming.NamingContextPackage", "org.omg.SendingContext", "org.omg.PortableServer", "org.omg.PortableServer.CurrentPackage", "org.omg.PortableServer.POAPackage", "org.omg.PortableServer.POAManagerPackage", "org.omg.PortableServer.ServantLocatorPackage", "org.omg.PortableServer.portable", "org.omg.PortableInterceptor", "org.omg.PortableInterceptor.ORBInitInfoPackage", "org.omg.Messaging", "org.omg.IOP", "org.omg.IOP.CodecFactoryPackage", "org.omg.IOP.CodecPackage", "org.omg.Dynamic", "org.omg.DynamicAny", "org.omg.DynamicAny.DynAnyPackage", "org.omg.DynamicAny.DynAnyFactoryPackage", "org.w3c.dom", "org.w3c.dom.events", "org.w3c.dom.bootstrap", "org.w3c.dom.ls", "org.xml.sax", "org.xml.sax.ext", "org.xml.sax.helpers", "com.sun.java.browser.dom", "org.w3c.dom", "org.w3c.dom.bootstrap", "org.w3c.dom.ls", "org.w3c.dom.ranges", "org.w3c.dom.traversal", "org.w3c.dom.html", "org.w3c.dom.stylesheets", "org.w3c.dom.css", "org.w3c.dom.events", "org.w3c.dom.views", "com.sun.management", "com.sun.security.auth", "com.sun.security.auth.callback", "com.sun.security.auth.login", "com.sun.security.auth.module", "com.sun.security.jgss", "com.sun.net.httpserver", "com.sun.net.httpserver.spi", "javax.smartcardio" };
/*     */ 
/* 516 */     Main.compile(paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.sym.CreateSymbols
 * JD-Core Version:    0.6.2
 */