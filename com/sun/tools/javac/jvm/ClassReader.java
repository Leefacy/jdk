/*      */ package com.sun.tools.javac.jvm;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Attribute.Array;
/*      */ import com.sun.tools.javac.code.Attribute.Class;
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.Constant;
/*      */ import com.sun.tools.javac.code.Attribute.Enum;
/*      */ import com.sun.tools.javac.code.Attribute.Error;
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.Attribute.Visitor;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Scope.ErrorScope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.Completer;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.Type.ForAll;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.Type.WildcardType;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.UniqueType;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Annotate.Worker;
/*      */ import com.sun.tools.javac.file.BaseFileObject;
/*      */ import com.sun.tools.javac.file.BaseFileObject.CannotCreateUriError;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileManager.Location;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.JavaFileObject.Kind;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*   78 */   protected static final Context.Key<ClassReader> classReaderKey = new Context.Key();
/*      */   public static final int INITIAL_BUFFER_SIZE = 65520;
/*      */   Annotate annotate;
/*      */   boolean verbose;
/*      */   boolean checkClassFile;
/*   97 */   public boolean readAllOfClassFile = false;
/*      */   boolean allowGenerics;
/*      */   boolean allowVarargs;
/*      */   boolean allowAnnotations;
/*      */   boolean allowSimplifiedVarargs;
/*      */   boolean lintClassfile;
/*      */   public boolean saveParameterNames;
/*      */   private boolean cacheCompletionFailure;
/*      */   public boolean preferSource;
/*      */   public final Profile profile;
/*      */   final Log log;
/*      */   Symtab syms;
/*      */   Types types;
/*      */   final Names names;
/*      */   final Name completionFailureName;
/*      */   private final JavaFileManager fileManager;
/*      */   JCDiagnostic.Factory diagFactory;
/*  167 */   public SourceCompleter sourceCompleter = null;
/*      */   private Map<Name, Symbol.ClassSymbol> classes;
/*      */   private Map<Name, Symbol.PackageSymbol> packages;
/*      */   protected Scope typevars;
/*  184 */   protected JavaFileObject currentClassFile = null;
/*      */ 
/*  188 */   protected Symbol currentOwner = null;
/*      */ 
/*  192 */   byte[] buf = new byte[65520];
/*      */   protected int bp;
/*      */   Object[] poolObj;
/*      */   int[] poolIdx;
/*      */   int majorVersion;
/*      */   int minorVersion;
/*      */   int[] parameterNameIndices;
/*      */   boolean haveParameterNameIndices;
/*      */   boolean sawMethodParameters;
/*  232 */   Set<Name> warnedAttrs = new HashSet();
/*      */ 
/*  237 */   private final Symbol.Completer thisCompleter = new Symbol.Completer()
/*      */   {
/*      */     public void complete(Symbol paramAnonymousSymbol) throws Symbol.CompletionFailure {
/*  240 */       ClassReader.this.complete(paramAnonymousSymbol); }  } ;
/*      */   byte[] signature;
/*      */   int sigp;
/*      */   int siglimit;
/*      */   boolean sigEnterPhase;
/*      */   byte[] signatureBuffer;
/*      */   int sbp;
/*      */   protected Set<AttributeKind> CLASS_ATTRIBUTE;
/*      */   protected Set<AttributeKind> MEMBER_ATTRIBUTE;
/*      */   protected Set<AttributeKind> CLASS_OR_MEMBER_ATTRIBUTE;
/*      */   protected Map<Name, AttributeReader> attributeReaders;
/*      */   private boolean readingClassAttr;
/*      */   private List<Type> missingTypeVariables;
/*      */   private List<Type> foundTypeVariables;
/*      */   private boolean filling;
/*      */   private Symbol.CompletionFailure cachedCompletionFailure;
/*      */   protected JavaFileManager.Location currentLoc;
/*      */   private boolean verbosePath;
/*      */ 
/*  247 */   public static ClassReader instance(Context paramContext) { ClassReader localClassReader = (ClassReader)paramContext.get(classReaderKey);
/*  248 */     if (localClassReader == null)
/*  249 */       localClassReader = new ClassReader(paramContext, true);
/*  250 */     return localClassReader;
/*      */   }
/*      */ 
/*      */   public void init(Symtab paramSymtab)
/*      */   {
/*  255 */     init(paramSymtab, true);
/*      */   }
/*      */ 
/*      */   private void init(Symtab paramSymtab, boolean paramBoolean)
/*      */   {
/*  262 */     if (this.classes != null) return;
/*      */ 
/*  264 */     if (paramBoolean) {
/*  265 */       Assert.check((this.packages == null) || (this.packages == paramSymtab.packages));
/*  266 */       this.packages = paramSymtab.packages;
/*  267 */       Assert.check((this.classes == null) || (this.classes == paramSymtab.classes));
/*  268 */       this.classes = paramSymtab.classes;
/*      */     } else {
/*  270 */       this.packages = new HashMap();
/*  271 */       this.classes = new HashMap();
/*      */     }
/*      */ 
/*  274 */     this.packages.put(this.names.empty, paramSymtab.rootPackage);
/*  275 */     paramSymtab.rootPackage.completer = this.thisCompleter;
/*  276 */     paramSymtab.unnamedPackage.completer = this.thisCompleter;
/*      */   }
/*      */ 
/*      */   protected ClassReader(Context paramContext, boolean paramBoolean)
/*      */   {
/*  631 */     this.sigEnterPhase = false;
/*      */ 
/*  744 */     this.signatureBuffer = new byte[0];
/*  745 */     this.sbp = 0;
/*      */ 
/*  990 */     this.CLASS_ATTRIBUTE = 
/*  991 */       EnumSet.of(AttributeKind.CLASS);
/*      */ 
/*  992 */     this.MEMBER_ATTRIBUTE = 
/*  993 */       EnumSet.of(AttributeKind.MEMBER);
/*      */ 
/*  994 */     this.CLASS_OR_MEMBER_ATTRIBUTE = 
/*  995 */       EnumSet.of(AttributeKind.CLASS, AttributeKind.MEMBER);
/*      */ 
/*  997 */     this.attributeReaders = new HashMap();
/*      */ 
/* 1389 */     this.readingClassAttr = false;
/* 1390 */     this.missingTypeVariables = List.nil();
/* 1391 */     this.foundTypeVariables = List.nil();
/*      */ 
/* 2484 */     this.filling = false;
/*      */ 
/* 2609 */     this.cachedCompletionFailure = new Symbol.CompletionFailure(null, (JCDiagnostic)null);
/*      */ 
/* 2612 */     this.cachedCompletionFailure.setStackTrace(new StackTraceElement[0]);
/*      */ 
/* 2739 */     this.verbosePath = true;
/*      */ 
/*  283 */     if (paramBoolean) paramContext.put(classReaderKey, this);
/*      */ 
/*  285 */     this.names = Names.instance(paramContext);
/*  286 */     this.syms = Symtab.instance(paramContext);
/*  287 */     this.types = Types.instance(paramContext);
/*  288 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*  289 */     if (this.fileManager == null)
/*  290 */       throw new AssertionError("FileManager initialization error");
/*  291 */     this.diagFactory = JCDiagnostic.Factory.instance(paramContext);
/*      */ 
/*  293 */     init(this.syms, paramBoolean);
/*  294 */     this.log = Log.instance(paramContext);
/*      */ 
/*  296 */     Options localOptions = Options.instance(paramContext);
/*  297 */     this.annotate = Annotate.instance(paramContext);
/*  298 */     this.verbose = localOptions.isSet(Option.VERBOSE);
/*  299 */     this.checkClassFile = localOptions.isSet("-checkclassfile");
/*      */ 
/*  301 */     Source localSource = Source.instance(paramContext);
/*  302 */     this.allowGenerics = localSource.allowGenerics();
/*  303 */     this.allowVarargs = localSource.allowVarargs();
/*  304 */     this.allowAnnotations = localSource.allowAnnotations();
/*  305 */     this.allowSimplifiedVarargs = localSource.allowSimplifiedVarargs();
/*      */ 
/*  307 */     this.saveParameterNames = localOptions.isSet("save-parameter-names");
/*  308 */     this.cacheCompletionFailure = localOptions.isUnset("dev");
/*  309 */     this.preferSource = "source".equals(localOptions.get("-Xprefer"));
/*      */ 
/*  311 */     this.profile = Profile.instance(paramContext);
/*      */ 
/*  313 */     this.completionFailureName = 
/*  314 */       (localOptions
/*  314 */       .isSet("failcomplete") ? 
/*  314 */       this.names
/*  315 */       .fromString(localOptions
/*  315 */       .get("failcomplete")) : 
/*  315 */       null);
/*      */ 
/*  318 */     this.typevars = new Scope(this.syms.noSymbol);
/*      */ 
/*  320 */     this.lintClassfile = Lint.instance(paramContext).isEnabled(Lint.LintCategory.CLASSFILE);
/*      */ 
/*  322 */     initAttributeReaders();
/*      */   }
/*      */ 
/*      */   private void enterMember(Symbol.ClassSymbol paramClassSymbol, Symbol paramSymbol)
/*      */   {
/*  330 */     if (((paramSymbol.flags_field & 0x80001000) != 4096L) || (paramSymbol.name.startsWith(this.names.lambda)))
/*  331 */       paramClassSymbol.members_field.enter(paramSymbol);
/*      */   }
/*      */ 
/*      */   private JCDiagnostic createBadClassFileDiagnostic(JavaFileObject paramJavaFileObject, JCDiagnostic paramJCDiagnostic)
/*      */   {
/*  348 */     String str = paramJavaFileObject.getKind() == JavaFileObject.Kind.SOURCE ? "bad.source.file.header" : "bad.class.file.header";
/*      */ 
/*  350 */     return this.diagFactory.fragment(str, new Object[] { paramJavaFileObject, paramJCDiagnostic });
/*      */   }
/*      */ 
/*      */   public BadClassFile badClassFile(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  357 */     return new BadClassFile(this.currentOwner
/*  355 */       .enclClass(), this.currentClassFile, this.diagFactory
/*  357 */       .fragment(paramString, paramArrayOfObject));
/*      */   }
/*      */ 
/*      */   char nextChar()
/*      */   {
/*  367 */     return (char)(((this.buf[(this.bp++)] & 0xFF) << 8) + (this.buf[(this.bp++)] & 0xFF));
/*      */   }
/*      */ 
/*      */   int nextByte()
/*      */   {
/*  373 */     return this.buf[(this.bp++)] & 0xFF;
/*      */   }
/*      */ 
/*      */   int nextInt()
/*      */   {
/*  379 */     return ((this.buf[(this.bp++)] & 0xFF) << 24) + ((this.buf[(this.bp++)] & 0xFF) << 16) + ((this.buf[(this.bp++)] & 0xFF) << 8) + (this.buf[(this.bp++)] & 0xFF);
/*      */   }
/*      */ 
/*      */   char getChar(int paramInt)
/*      */   {
/*  389 */     return (char)(((this.buf[paramInt] & 0xFF) << 8) + (this.buf[(paramInt + 1)] & 0xFF));
/*      */   }
/*      */ 
/*      */   int getInt(int paramInt)
/*      */   {
/*  396 */     return ((this.buf[paramInt] & 0xFF) << 24) + ((this.buf[(paramInt + 1)] & 0xFF) << 16) + ((this.buf[(paramInt + 2)] & 0xFF) << 8) + (this.buf[(paramInt + 3)] & 0xFF);
/*      */   }
/*      */ 
/*      */   long getLong(int paramInt)
/*      */   {
/*  407 */     DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 8));
/*      */     try
/*      */     {
/*  410 */       return localDataInputStream.readLong();
/*      */     } catch (IOException localIOException) {
/*  412 */       throw new AssertionError(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   float getFloat(int paramInt)
/*      */   {
/*  419 */     DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 4));
/*      */     try
/*      */     {
/*  422 */       return localDataInputStream.readFloat();
/*      */     } catch (IOException localIOException) {
/*  424 */       throw new AssertionError(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   double getDouble(int paramInt)
/*      */   {
/*  431 */     DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(this.buf, paramInt, 8));
/*      */     try
/*      */     {
/*  434 */       return localDataInputStream.readDouble();
/*      */     } catch (IOException localIOException) {
/*  436 */       throw new AssertionError(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   void indexPool()
/*      */   {
/*  448 */     this.poolIdx = new int[nextChar()];
/*  449 */     this.poolObj = new Object[this.poolIdx.length];
/*  450 */     int i = 1;
/*  451 */     while (i < this.poolIdx.length) {
/*  452 */       this.poolIdx[(i++)] = this.bp;
/*  453 */       byte b = this.buf[(this.bp++)];
/*  454 */       switch (b) { case 1:
/*      */       case 2:
/*  456 */         int j = nextChar();
/*  457 */         this.bp += j;
/*  458 */         break;
/*      */       case 7:
/*      */       case 8:
/*      */       case 16:
/*  463 */         this.bp += 2;
/*  464 */         break;
/*      */       case 15:
/*  466 */         this.bp += 3;
/*  467 */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 18:
/*  475 */         this.bp += 4;
/*  476 */         break;
/*      */       case 5:
/*      */       case 6:
/*  479 */         this.bp += 8;
/*  480 */         i++;
/*  481 */         break;
/*      */       case 13:
/*      */       case 14:
/*      */       case 17:
/*      */       default:
/*  483 */         throw badClassFile("bad.const.pool.tag.at", new Object[] { 
/*  484 */           Byte.toString(b), 
/*  485 */           Integer.toString(this.bp - 1) });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Object readPool(int paramInt)
/*      */   {
/*  493 */     Object localObject = this.poolObj[paramInt];
/*  494 */     if (localObject != null) return localObject;
/*      */ 
/*  496 */     int i = this.poolIdx[paramInt];
/*  497 */     if (i == 0) return null;
/*      */ 
/*  499 */     byte b = this.buf[i];
/*      */     Symbol.ClassSymbol localClassSymbol;
/*      */     ClassFile.NameAndType localNameAndType;
/*  500 */     switch (b) {
/*      */     case 1:
/*  502 */       this.poolObj[paramInt] = this.names.fromUtf(this.buf, i + 3, getChar(i + 1));
/*  503 */       break;
/*      */     case 2:
/*  505 */       throw badClassFile("unicode.str.not.supported", new Object[0]);
/*      */     case 7:
/*  507 */       this.poolObj[paramInt] = readClassOrType(getChar(i + 1));
/*  508 */       break;
/*      */     case 8:
/*  511 */       this.poolObj[paramInt] = readName(getChar(i + 1)).toString();
/*  512 */       break;
/*      */     case 9:
/*  514 */       localClassSymbol = readClassSymbol(getChar(i + 1));
/*  515 */       localNameAndType = readNameAndType(getChar(i + 3));
/*  516 */       this.poolObj[paramInt] = new Symbol.VarSymbol(0L, localNameAndType.name, localNameAndType.uniqueType.type, localClassSymbol);
/*  517 */       break;
/*      */     case 10:
/*      */     case 11:
/*  521 */       localClassSymbol = readClassSymbol(getChar(i + 1));
/*  522 */       localNameAndType = readNameAndType(getChar(i + 3));
/*  523 */       this.poolObj[paramInt] = new Symbol.MethodSymbol(0L, localNameAndType.name, localNameAndType.uniqueType.type, localClassSymbol);
/*  524 */       break;
/*      */     case 12:
/*  527 */       this.poolObj[paramInt] = new ClassFile.NameAndType(
/*  528 */         readName(getChar(i + 1)), 
/*  529 */         readType(getChar(i + 3)), 
/*  529 */         this.types);
/*  530 */       break;
/*      */     case 3:
/*  532 */       this.poolObj[paramInt] = Integer.valueOf(getInt(i + 1));
/*  533 */       break;
/*      */     case 4:
/*  535 */       this.poolObj[paramInt] = new Float(getFloat(i + 1));
/*  536 */       break;
/*      */     case 5:
/*  538 */       this.poolObj[paramInt] = new Long(getLong(i + 1));
/*  539 */       break;
/*      */     case 6:
/*  541 */       this.poolObj[paramInt] = new Double(getDouble(i + 1));
/*  542 */       break;
/*      */     case 15:
/*  544 */       skipBytes(4);
/*  545 */       break;
/*      */     case 16:
/*  547 */       skipBytes(3);
/*  548 */       break;
/*      */     case 18:
/*  550 */       skipBytes(5);
/*  551 */       break;
/*      */     case 13:
/*      */     case 14:
/*      */     case 17:
/*      */     default:
/*  553 */       throw badClassFile("bad.const.pool.tag", new Object[] { Byte.toString(b) });
/*      */     }
/*  555 */     return this.poolObj[paramInt];
/*      */   }
/*      */ 
/*      */   Type readType(int paramInt)
/*      */   {
/*  561 */     int i = this.poolIdx[paramInt];
/*  562 */     return sigToType(this.buf, i + 3, getChar(i + 1));
/*      */   }
/*      */ 
/*      */   Object readClassOrType(int paramInt)
/*      */   {
/*  569 */     int i = this.poolIdx[paramInt];
/*  570 */     int j = getChar(i + 1);
/*  571 */     int k = i + 3;
/*  572 */     Assert.check((this.buf[k] == 91) || (this.buf[(k + j - 1)] != 59));
/*      */ 
/*  577 */     return (this.buf[k] == 91) || (this.buf[(k + j - 1)] == 59) ? 
/*  576 */       sigToType(this.buf, k, j) : 
/*  577 */       enterClass(this.names
/*  577 */       .fromUtf(ClassFile.internalize(this.buf, k, j)));
/*      */   }
/*      */ 
/*      */   List<Type> readTypeParams(int paramInt)
/*      */   {
/*  584 */     int i = this.poolIdx[paramInt];
/*  585 */     return sigToTypeParams(this.buf, i + 3, getChar(i + 1));
/*      */   }
/*      */ 
/*      */   Symbol.ClassSymbol readClassSymbol(int paramInt)
/*      */   {
/*  591 */     Object localObject = readPool(paramInt);
/*  592 */     if ((localObject != null) && (!(localObject instanceof Symbol.ClassSymbol))) {
/*  593 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  594 */         .toString(), "CONSTANT_Class_info", 
/*  595 */         Integer.valueOf(paramInt) });
/*      */     }
/*  596 */     return (Symbol.ClassSymbol)localObject;
/*      */   }
/*      */ 
/*      */   Name readName(int paramInt)
/*      */   {
/*  602 */     Object localObject = readPool(paramInt);
/*  603 */     if ((localObject != null) && (!(localObject instanceof Name))) {
/*  604 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  605 */         .toString(), "CONSTANT_Utf8_info or CONSTANT_String_info", 
/*  606 */         Integer.valueOf(paramInt) });
/*      */     }
/*  607 */     return (Name)localObject;
/*      */   }
/*      */ 
/*      */   ClassFile.NameAndType readNameAndType(int paramInt)
/*      */   {
/*  613 */     Object localObject = readPool(paramInt);
/*  614 */     if ((localObject != null) && (!(localObject instanceof ClassFile.NameAndType))) {
/*  615 */       throw badClassFile("bad.const.pool.entry", new Object[] { this.currentClassFile
/*  616 */         .toString(), "CONSTANT_NameAndType_info", 
/*  617 */         Integer.valueOf(paramInt) });
/*      */     }
/*  618 */     return (ClassFile.NameAndType)localObject;
/*      */   }
/*      */ 
/*      */   Type sigToType(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  636 */     this.signature = paramArrayOfByte;
/*  637 */     this.sigp = paramInt1;
/*  638 */     this.siglimit = (paramInt1 + paramInt2);
/*  639 */     return sigToType();
/*      */   }
/*      */ 
/*      */   Type sigToType()
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  645 */     switch ((char)this.signature[this.sigp]) {
/*      */     case 'T':
/*  647 */       this.sigp += 1;
/*  648 */       int i = this.sigp;
/*  649 */       while (this.signature[this.sigp] != 59) this.sigp += 1;
/*  650 */       this.sigp += 1;
/*      */ 
/*  653 */       return this.sigEnterPhase ? Type.noType : 
/*  653 */         findTypeVar(this.names
/*  653 */         .fromUtf(this.signature, i, this.sigp - 1 - i));
/*      */     case '+':
/*  655 */       this.sigp += 1;
/*  656 */       localObject1 = sigToType();
/*  657 */       return new Type.WildcardType((Type)localObject1, BoundKind.EXTENDS, this.syms.boundClass);
/*      */     case '*':
/*  661 */       this.sigp += 1;
/*  662 */       return new Type.WildcardType(this.syms.objectType, BoundKind.UNBOUND, this.syms.boundClass);
/*      */     case '-':
/*  665 */       this.sigp += 1;
/*  666 */       localObject1 = sigToType();
/*  667 */       return new Type.WildcardType((Type)localObject1, BoundKind.SUPER, this.syms.boundClass);
/*      */     case 'B':
/*  671 */       this.sigp += 1;
/*  672 */       return this.syms.byteType;
/*      */     case 'C':
/*  674 */       this.sigp += 1;
/*  675 */       return this.syms.charType;
/*      */     case 'D':
/*  677 */       this.sigp += 1;
/*  678 */       return this.syms.doubleType;
/*      */     case 'F':
/*  680 */       this.sigp += 1;
/*  681 */       return this.syms.floatType;
/*      */     case 'I':
/*  683 */       this.sigp += 1;
/*  684 */       return this.syms.intType;
/*      */     case 'J':
/*  686 */       this.sigp += 1;
/*  687 */       return this.syms.longType;
/*      */     case 'L':
/*  691 */       localObject1 = classSigToType();
/*  692 */       if ((this.sigp < this.siglimit) && (this.signature[this.sigp] == 46)) {
/*  693 */         throw badClassFile("deprecated inner class signature syntax (please recompile from source)", new Object[0]);
/*      */       }
/*      */ 
/*  700 */       return localObject1;
/*      */     case 'S':
/*  703 */       this.sigp += 1;
/*  704 */       return this.syms.shortType;
/*      */     case 'V':
/*  706 */       this.sigp += 1;
/*  707 */       return this.syms.voidType;
/*      */     case 'Z':
/*  709 */       this.sigp += 1;
/*  710 */       return this.syms.booleanType;
/*      */     case '[':
/*  712 */       this.sigp += 1;
/*  713 */       return new Type.ArrayType(sigToType(), this.syms.arrayClass);
/*      */     case '(':
/*  715 */       this.sigp += 1;
/*  716 */       localObject1 = sigToTypes(')');
/*  717 */       Type localType = sigToType();
/*  718 */       List localList = List.nil();
/*  719 */       while (this.signature[this.sigp] == 94) {
/*  720 */         this.sigp += 1;
/*  721 */         localList = localList.prepend(sigToType());
/*      */       }
/*      */ 
/*  724 */       for (localObject2 = localList; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/*  725 */         if (((Type)((List)localObject2).head).hasTag(TypeTag.TYPEVAR)) {
/*  726 */           ((Type)((List)localObject2).head).tsym.flags_field |= 140737488355328L;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  731 */       return new Type.MethodType((List)localObject1, localType, localList
/*  731 */         .reverse(), this.syms.methodClass);
/*      */     case '<':
/*  734 */       this.typevars = this.typevars.dup(this.currentOwner);
/*  735 */       localObject2 = new Type.ForAll(sigToTypeParams(), sigToType());
/*  736 */       this.typevars = this.typevars.leave();
/*  737 */       return localObject2;
/*      */     case ')':
/*      */     case ',':
/*      */     case '.':
/*      */     case '/':
/*      */     case '0':
/*      */     case '1':
/*      */     case '2':
/*      */     case '3':
/*      */     case '4':
/*      */     case '5':
/*      */     case '6':
/*      */     case '7':
/*      */     case '8':
/*      */     case '9':
/*      */     case ':':
/*      */     case ';':
/*      */     case '=':
/*      */     case '>':
/*      */     case '?':
/*      */     case '@':
/*      */     case 'A':
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'U':
/*      */     case 'W':
/*      */     case 'X':
/*  739 */     case 'Y': } throw badClassFile("bad.signature", new Object[] { 
/*  740 */       Convert.utf2string(this.signature, this.sigp, 10) });
/*      */   }
/*      */ 
/*      */   Type classSigToType()
/*      */   {
/*  749 */     if (this.signature[this.sigp] != 76) {
/*  750 */       throw badClassFile("bad.class.signature", new Object[] { 
/*  751 */         Convert.utf2string(this.signature, this.sigp, 10) });
/*      */     }
/*  752 */     this.sigp += 1;
/*  753 */     Object localObject1 = Type.noType;
/*  754 */     int i = this.sbp;
/*      */     while (true)
/*      */     {
/*  757 */       int j = this.signature[(this.sigp++)];
/*      */       Symbol.ClassSymbol localClassSymbol;
/*  758 */       switch (j)
/*      */       {
/*      */       case 59:
/*  761 */         localClassSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i));
/*      */         try
/*      */         {
/*  768 */           return localObject1 == Type.noType ? localClassSymbol
/*  767 */             .erasure(this.types) : 
/*  767 */             new Type.ClassType((Type)localObject1, 
/*  768 */             List.nil(), localClassSymbol);
/*      */         } finally {
/*  770 */           this.sbp = i;
/*      */         }
/*      */ 
/*      */       case 60:
/*  775 */         localClassSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i));
/*      */ 
/*  778 */         localObject1 = new Type.ClassType((Type)localObject1, sigToTypes('>'), localClassSymbol) {
/*  779 */           boolean completed = false;
/*      */ 
/*      */           public Type getEnclosingType() {
/*  782 */             if (!this.completed) {
/*  783 */               this.completed = true;
/*  784 */               this.tsym.complete();
/*  785 */               Type localType = this.tsym.type.getEnclosingType();
/*  786 */               if (localType != Type.noType)
/*      */               {
/*  788 */                 List localList1 = super
/*  788 */                   .getEnclosingType().allparams();
/*      */ 
/*  790 */                 List localList2 = localType
/*  790 */                   .allparams();
/*  791 */                 if (localList2.length() != localList1.length())
/*      */                 {
/*  793 */                   super.setEnclosingType(ClassReader.this.types.erasure(localType));
/*      */                 }
/*  795 */                 else super.setEnclosingType(ClassReader.this.types.subst(localType, localList2, localList1));
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*  800 */                 super.setEnclosingType(Type.noType);
/*      */               }
/*      */             }
/*  803 */             return super.getEnclosingType();
/*      */           }
/*      */ 
/*      */           public void setEnclosingType(Type paramAnonymousType) {
/*  807 */             throw new UnsupportedOperationException();
/*      */           }
/*      */         };
/*  810 */         switch (this.signature[(this.sigp++)]) {
/*      */         case 59:
/*  812 */           if ((this.sigp < this.signature.length) && (this.signature[this.sigp] == 46))
/*      */           {
/*  819 */             this.sigp += this.sbp - i + 3;
/*      */ 
/*  821 */             this.signatureBuffer[(this.sbp++)] = 36;
/*      */           }
/*      */           else {
/*  824 */             this.sbp = i;
/*  825 */             return localObject1;
/*      */           }break;
/*      */         case 46:
/*  828 */           this.signatureBuffer[(this.sbp++)] = 36;
/*  829 */           break;
/*      */         default:
/*  831 */           throw new AssertionError(this.signature[(this.sigp - 1)]);
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 46:
/*  837 */         if (localObject1 != Type.noType) {
/*  838 */           localClassSymbol = enterClass(this.names.fromUtf(this.signatureBuffer, i, this.sbp - i));
/*      */ 
/*  841 */           localObject1 = new Type.ClassType((Type)localObject1, List.nil(), localClassSymbol);
/*      */         }
/*  843 */         this.signatureBuffer[(this.sbp++)] = 36;
/*  844 */         break;
/*      */       case 47:
/*  846 */         this.signatureBuffer[(this.sbp++)] = 46;
/*  847 */         break;
/*      */       default:
/*  849 */         this.signatureBuffer[(this.sbp++)] = j;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   List<Type> sigToTypes(char paramChar)
/*      */   {
/*  859 */     List localList1 = List.of(null);
/*  860 */     List localList2 = localList1;
/*  861 */     while (this.signature[this.sigp] != paramChar)
/*  862 */       localList2 = localList2.setTail(List.of(sigToType()));
/*  863 */     this.sigp += 1;
/*  864 */     return localList1.tail;
/*      */   }
/*      */ 
/*      */   List<Type> sigToTypeParams(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  871 */     this.signature = paramArrayOfByte;
/*  872 */     this.sigp = paramInt1;
/*  873 */     this.siglimit = (paramInt1 + paramInt2);
/*  874 */     return sigToTypeParams();
/*      */   }
/*      */ 
/*      */   List<Type> sigToTypeParams()
/*      */   {
/*  880 */     List localList = List.nil();
/*  881 */     if (this.signature[this.sigp] == 60) {
/*  882 */       this.sigp += 1;
/*  883 */       int i = this.sigp;
/*  884 */       this.sigEnterPhase = true;
/*  885 */       while (this.signature[this.sigp] != 62)
/*  886 */         localList = localList.prepend(sigToTypeParam());
/*  887 */       this.sigEnterPhase = false;
/*  888 */       this.sigp = i;
/*  889 */       while (this.signature[this.sigp] != 62)
/*  890 */         sigToTypeParam();
/*  891 */       this.sigp += 1;
/*      */     }
/*  893 */     return localList.reverse();
/*      */   }
/*      */ 
/*      */   Type sigToTypeParam()
/*      */   {
/*  899 */     int i = this.sigp;
/*  900 */     while (this.signature[this.sigp] != 58) this.sigp += 1;
/*  901 */     Name localName = this.names.fromUtf(this.signature, i, this.sigp - i);
/*      */     Type.TypeVar localTypeVar;
/*  903 */     if (this.sigEnterPhase) {
/*  904 */       localTypeVar = new Type.TypeVar(localName, this.currentOwner, this.syms.botType);
/*  905 */       this.typevars.enter(localTypeVar.tsym);
/*      */     } else {
/*  907 */       localTypeVar = (Type.TypeVar)findTypeVar(localName);
/*      */     }
/*  909 */     List localList = List.nil();
/*  910 */     boolean bool = false;
/*  911 */     if ((this.signature[this.sigp] == 58) && (this.signature[(this.sigp + 1)] == 58)) {
/*  912 */       this.sigp += 1;
/*  913 */       bool = true;
/*      */     }
/*  915 */     while (this.signature[this.sigp] == 58) {
/*  916 */       this.sigp += 1;
/*  917 */       localList = localList.prepend(sigToType());
/*      */     }
/*  919 */     if (!this.sigEnterPhase) {
/*  920 */       this.types.setBounds(localTypeVar, localList.reverse(), bool);
/*      */     }
/*  922 */     return localTypeVar;
/*      */   }
/*      */ 
/*      */   Type findTypeVar(Name paramName)
/*      */   {
/*  928 */     Scope.Entry localEntry = this.typevars.lookup(paramName);
/*  929 */     if (localEntry.scope != null) {
/*  930 */       return localEntry.sym.type;
/*      */     }
/*  932 */     if (this.readingClassAttr)
/*      */     {
/*  943 */       Type.TypeVar localTypeVar = new Type.TypeVar(paramName, this.currentOwner, this.syms.botType);
/*  944 */       this.missingTypeVariables = this.missingTypeVariables.prepend(localTypeVar);
/*      */ 
/*  946 */       return localTypeVar;
/*      */     }
/*  948 */     throw badClassFile("undecl.type.var", new Object[] { paramName }); } 
/*      */   private void initAttributeReaders() { // Byte code:
/*      */     //   0: bipush 22
/*      */     //   2: anewarray 745	com/sun/tools/javac/jvm/ClassReader$AttributeReader
/*      */     //   5: dup
/*      */     //   6: iconst_0
/*      */     //   7: new 733	com/sun/tools/javac/jvm/ClassReader$3
/*      */     //   10: dup
/*      */     //   11: aload_0
/*      */     //   12: aload_0
/*      */     //   13: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   16: getfield 1606	com/sun/tools/javac/util/Names:Code	Lcom/sun/tools/javac/util/Name;
/*      */     //   19: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   22: aload_0
/*      */     //   23: getfield 1583	com/sun/tools/javac/jvm/ClassReader:MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   26: invokespecial 1811	com/sun/tools/javac/jvm/ClassReader$3:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   29: aastore
/*      */     //   30: dup
/*      */     //   31: iconst_1
/*      */     //   32: new 734	com/sun/tools/javac/jvm/ClassReader$4
/*      */     //   35: dup
/*      */     //   36: aload_0
/*      */     //   37: aload_0
/*      */     //   38: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   41: getfield 1607	com/sun/tools/javac/util/Names:ConstantValue	Lcom/sun/tools/javac/util/Name;
/*      */     //   44: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   47: aload_0
/*      */     //   48: getfield 1583	com/sun/tools/javac/jvm/ClassReader:MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   51: invokespecial 1812	com/sun/tools/javac/jvm/ClassReader$4:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   54: aastore
/*      */     //   55: dup
/*      */     //   56: iconst_2
/*      */     //   57: new 735	com/sun/tools/javac/jvm/ClassReader$5
/*      */     //   60: dup
/*      */     //   61: aload_0
/*      */     //   62: aload_0
/*      */     //   63: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   66: getfield 1608	com/sun/tools/javac/util/Names:Deprecated	Lcom/sun/tools/javac/util/Name;
/*      */     //   69: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   72: aload_0
/*      */     //   73: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   76: invokespecial 1813	com/sun/tools/javac/jvm/ClassReader$5:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   79: aastore
/*      */     //   80: dup
/*      */     //   81: iconst_3
/*      */     //   82: new 736	com/sun/tools/javac/jvm/ClassReader$6
/*      */     //   85: dup
/*      */     //   86: aload_0
/*      */     //   87: aload_0
/*      */     //   88: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   91: getfield 1611	com/sun/tools/javac/util/Names:Exceptions	Lcom/sun/tools/javac/util/Name;
/*      */     //   94: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   97: aload_0
/*      */     //   98: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   101: invokespecial 1814	com/sun/tools/javac/jvm/ClassReader$6:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   104: aastore
/*      */     //   105: dup
/*      */     //   106: iconst_4
/*      */     //   107: new 737	com/sun/tools/javac/jvm/ClassReader$7
/*      */     //   110: dup
/*      */     //   111: aload_0
/*      */     //   112: aload_0
/*      */     //   113: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   116: getfield 1612	com/sun/tools/javac/util/Names:InnerClasses	Lcom/sun/tools/javac/util/Name;
/*      */     //   119: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   122: aload_0
/*      */     //   123: getfield 1581	com/sun/tools/javac/jvm/ClassReader:CLASS_ATTRIBUTE	Ljava/util/Set;
/*      */     //   126: invokespecial 1815	com/sun/tools/javac/jvm/ClassReader$7:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   129: aastore
/*      */     //   130: dup
/*      */     //   131: iconst_5
/*      */     //   132: new 738	com/sun/tools/javac/jvm/ClassReader$8
/*      */     //   135: dup
/*      */     //   136: aload_0
/*      */     //   137: aload_0
/*      */     //   138: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   141: getfield 1613	com/sun/tools/javac/util/Names:LocalVariableTable	Lcom/sun/tools/javac/util/Name;
/*      */     //   144: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   147: aload_0
/*      */     //   148: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   151: invokespecial 1816	com/sun/tools/javac/jvm/ClassReader$8:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   154: aastore
/*      */     //   155: dup
/*      */     //   156: bipush 6
/*      */     //   158: new 739	com/sun/tools/javac/jvm/ClassReader$9
/*      */     //   161: dup
/*      */     //   162: aload_0
/*      */     //   163: aload_0
/*      */     //   164: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   167: getfield 1614	com/sun/tools/javac/util/Names:MethodParameters	Lcom/sun/tools/javac/util/Name;
/*      */     //   170: getstatic 1532	com/sun/tools/javac/jvm/ClassFile$Version:V52	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   173: aload_0
/*      */     //   174: getfield 1583	com/sun/tools/javac/jvm/ClassReader:MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   177: invokespecial 1817	com/sun/tools/javac/jvm/ClassReader$9:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   180: aastore
/*      */     //   181: dup
/*      */     //   182: bipush 7
/*      */     //   184: new 716	com/sun/tools/javac/jvm/ClassReader$10
/*      */     //   187: dup
/*      */     //   188: aload_0
/*      */     //   189: aload_0
/*      */     //   190: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   193: getfield 1622	com/sun/tools/javac/util/Names:SourceFile	Lcom/sun/tools/javac/util/Name;
/*      */     //   196: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   199: aload_0
/*      */     //   200: getfield 1581	com/sun/tools/javac/jvm/ClassReader:CLASS_ATTRIBUTE	Ljava/util/Set;
/*      */     //   203: invokespecial 1795	com/sun/tools/javac/jvm/ClassReader$10:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   206: aastore
/*      */     //   207: dup
/*      */     //   208: bipush 8
/*      */     //   210: new 717	com/sun/tools/javac/jvm/ClassReader$11
/*      */     //   213: dup
/*      */     //   214: aload_0
/*      */     //   215: aload_0
/*      */     //   216: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   219: getfield 1623	com/sun/tools/javac/util/Names:Synthetic	Lcom/sun/tools/javac/util/Name;
/*      */     //   222: getstatic 1530	com/sun/tools/javac/jvm/ClassFile$Version:V45_3	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   225: aload_0
/*      */     //   226: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   229: invokespecial 1796	com/sun/tools/javac/jvm/ClassReader$11:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   232: aastore
/*      */     //   233: dup
/*      */     //   234: bipush 9
/*      */     //   236: new 718	com/sun/tools/javac/jvm/ClassReader$12
/*      */     //   239: dup
/*      */     //   240: aload_0
/*      */     //   241: aload_0
/*      */     //   242: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   245: getfield 1609	com/sun/tools/javac/util/Names:EnclosingMethod	Lcom/sun/tools/javac/util/Name;
/*      */     //   248: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   251: aload_0
/*      */     //   252: getfield 1581	com/sun/tools/javac/jvm/ClassReader:CLASS_ATTRIBUTE	Ljava/util/Set;
/*      */     //   255: invokespecial 1797	com/sun/tools/javac/jvm/ClassReader$12:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   258: aastore
/*      */     //   259: dup
/*      */     //   260: bipush 10
/*      */     //   262: new 719	com/sun/tools/javac/jvm/ClassReader$13
/*      */     //   265: dup
/*      */     //   266: aload_0
/*      */     //   267: aload_0
/*      */     //   268: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   271: getfield 1621	com/sun/tools/javac/util/Names:Signature	Lcom/sun/tools/javac/util/Name;
/*      */     //   274: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   277: aload_0
/*      */     //   278: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   281: invokespecial 1798	com/sun/tools/javac/jvm/ClassReader$13:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   284: aastore
/*      */     //   285: dup
/*      */     //   286: bipush 11
/*      */     //   288: new 720	com/sun/tools/javac/jvm/ClassReader$14
/*      */     //   291: dup
/*      */     //   292: aload_0
/*      */     //   293: aload_0
/*      */     //   294: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   297: getfield 1604	com/sun/tools/javac/util/Names:AnnotationDefault	Lcom/sun/tools/javac/util/Name;
/*      */     //   300: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   303: aload_0
/*      */     //   304: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   307: invokespecial 1799	com/sun/tools/javac/jvm/ClassReader$14:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   310: aastore
/*      */     //   311: dup
/*      */     //   312: bipush 12
/*      */     //   314: new 721	com/sun/tools/javac/jvm/ClassReader$15
/*      */     //   317: dup
/*      */     //   318: aload_0
/*      */     //   319: aload_0
/*      */     //   320: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   323: getfield 1615	com/sun/tools/javac/util/Names:RuntimeInvisibleAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   326: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   329: aload_0
/*      */     //   330: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   333: invokespecial 1800	com/sun/tools/javac/jvm/ClassReader$15:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   336: aastore
/*      */     //   337: dup
/*      */     //   338: bipush 13
/*      */     //   340: new 722	com/sun/tools/javac/jvm/ClassReader$16
/*      */     //   343: dup
/*      */     //   344: aload_0
/*      */     //   345: aload_0
/*      */     //   346: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   349: getfield 1616	com/sun/tools/javac/util/Names:RuntimeInvisibleParameterAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   352: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   355: aload_0
/*      */     //   356: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   359: invokespecial 1801	com/sun/tools/javac/jvm/ClassReader$16:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   362: aastore
/*      */     //   363: dup
/*      */     //   364: bipush 14
/*      */     //   366: new 723	com/sun/tools/javac/jvm/ClassReader$17
/*      */     //   369: dup
/*      */     //   370: aload_0
/*      */     //   371: aload_0
/*      */     //   372: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   375: getfield 1618	com/sun/tools/javac/util/Names:RuntimeVisibleAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   378: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   381: aload_0
/*      */     //   382: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   385: invokespecial 1802	com/sun/tools/javac/jvm/ClassReader$17:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   388: aastore
/*      */     //   389: dup
/*      */     //   390: bipush 15
/*      */     //   392: new 724	com/sun/tools/javac/jvm/ClassReader$18
/*      */     //   395: dup
/*      */     //   396: aload_0
/*      */     //   397: aload_0
/*      */     //   398: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   401: getfield 1619	com/sun/tools/javac/util/Names:RuntimeVisibleParameterAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   404: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   407: aload_0
/*      */     //   408: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   411: invokespecial 1803	com/sun/tools/javac/jvm/ClassReader$18:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   414: aastore
/*      */     //   415: dup
/*      */     //   416: bipush 16
/*      */     //   418: new 725	com/sun/tools/javac/jvm/ClassReader$19
/*      */     //   421: dup
/*      */     //   422: aload_0
/*      */     //   423: aload_0
/*      */     //   424: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   427: getfield 1603	com/sun/tools/javac/util/Names:Annotation	Lcom/sun/tools/javac/util/Name;
/*      */     //   430: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   433: aload_0
/*      */     //   434: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   437: invokespecial 1804	com/sun/tools/javac/jvm/ClassReader$19:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   440: aastore
/*      */     //   441: dup
/*      */     //   442: bipush 17
/*      */     //   444: new 727	com/sun/tools/javac/jvm/ClassReader$20
/*      */     //   447: dup
/*      */     //   448: aload_0
/*      */     //   449: aload_0
/*      */     //   450: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   453: getfield 1605	com/sun/tools/javac/util/Names:Bridge	Lcom/sun/tools/javac/util/Name;
/*      */     //   456: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   459: aload_0
/*      */     //   460: getfield 1583	com/sun/tools/javac/jvm/ClassReader:MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   463: invokespecial 1806	com/sun/tools/javac/jvm/ClassReader$20:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   466: aastore
/*      */     //   467: dup
/*      */     //   468: bipush 18
/*      */     //   470: new 728	com/sun/tools/javac/jvm/ClassReader$21
/*      */     //   473: dup
/*      */     //   474: aload_0
/*      */     //   475: aload_0
/*      */     //   476: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   479: getfield 1610	com/sun/tools/javac/util/Names:Enum	Lcom/sun/tools/javac/util/Name;
/*      */     //   482: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   485: aload_0
/*      */     //   486: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   489: invokespecial 1807	com/sun/tools/javac/jvm/ClassReader$21:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   492: aastore
/*      */     //   493: dup
/*      */     //   494: bipush 19
/*      */     //   496: new 729	com/sun/tools/javac/jvm/ClassReader$22
/*      */     //   499: dup
/*      */     //   500: aload_0
/*      */     //   501: aload_0
/*      */     //   502: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   505: getfield 1624	com/sun/tools/javac/util/Names:Varargs	Lcom/sun/tools/javac/util/Name;
/*      */     //   508: getstatic 1531	com/sun/tools/javac/jvm/ClassFile$Version:V49	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   511: aload_0
/*      */     //   512: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   515: invokespecial 1808	com/sun/tools/javac/jvm/ClassReader$22:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   518: aastore
/*      */     //   519: dup
/*      */     //   520: bipush 20
/*      */     //   522: new 730	com/sun/tools/javac/jvm/ClassReader$23
/*      */     //   525: dup
/*      */     //   526: aload_0
/*      */     //   527: aload_0
/*      */     //   528: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   531: getfield 1620	com/sun/tools/javac/util/Names:RuntimeVisibleTypeAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   534: getstatic 1532	com/sun/tools/javac/jvm/ClassFile$Version:V52	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   537: aload_0
/*      */     //   538: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   541: invokespecial 1809	com/sun/tools/javac/jvm/ClassReader$23:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   544: aastore
/*      */     //   545: dup
/*      */     //   546: bipush 21
/*      */     //   548: new 731	com/sun/tools/javac/jvm/ClassReader$24
/*      */     //   551: dup
/*      */     //   552: aload_0
/*      */     //   553: aload_0
/*      */     //   554: getfield 1576	com/sun/tools/javac/jvm/ClassReader:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   557: getfield 1617	com/sun/tools/javac/util/Names:RuntimeInvisibleTypeAnnotations	Lcom/sun/tools/javac/util/Name;
/*      */     //   560: getstatic 1532	com/sun/tools/javac/jvm/ClassFile$Version:V52	Lcom/sun/tools/javac/jvm/ClassFile$Version;
/*      */     //   563: aload_0
/*      */     //   564: getfield 1582	com/sun/tools/javac/jvm/ClassReader:CLASS_OR_MEMBER_ATTRIBUTE	Ljava/util/Set;
/*      */     //   567: invokespecial 1810	com/sun/tools/javac/jvm/ClassReader$24:<init>	(Lcom/sun/tools/javac/jvm/ClassReader;Lcom/sun/tools/javac/util/Name;Lcom/sun/tools/javac/jvm/ClassFile$Version;Ljava/util/Set;)V
/*      */     //   570: aastore
/*      */     //   571: astore_1
/*      */     //   572: aload_1
/*      */     //   573: astore_2
/*      */     //   574: aload_2
/*      */     //   575: arraylength
/*      */     //   576: istore_3
/*      */     //   577: iconst_0
/*      */     //   578: istore 4
/*      */     //   580: iload 4
/*      */     //   582: iload_3
/*      */     //   583: if_icmpge +32 -> 615
/*      */     //   586: aload_2
/*      */     //   587: iload 4
/*      */     //   589: aaload
/*      */     //   590: astore 5
/*      */     //   592: aload_0
/*      */     //   593: getfield 1578	com/sun/tools/javac/jvm/ClassReader:attributeReaders	Ljava/util/Map;
/*      */     //   596: aload 5
/*      */     //   598: getfield 1592	com/sun/tools/javac/jvm/ClassReader$AttributeReader:name	Lcom/sun/tools/javac/util/Name;
/*      */     //   601: aload 5
/*      */     //   603: invokeinterface 1930 3 0
/*      */     //   608: pop
/*      */     //   609: iinc 4 1
/*      */     //   612: goto -32 -> 580
/*      */     //   615: return } 
/* 1256 */   void unrecognized(Name paramName) { if (this.checkClassFile)
/* 1257 */       printCCF("ccf.unrecognized.attribute", paramName);
/*      */   }
/*      */ 
/*      */   protected void readEnclosingMethodAttr(Symbol paramSymbol)
/*      */   {
/* 1266 */     paramSymbol.owner.members().remove(paramSymbol);
/* 1267 */     Symbol.ClassSymbol localClassSymbol1 = (Symbol.ClassSymbol)paramSymbol;
/* 1268 */     Symbol.ClassSymbol localClassSymbol2 = readClassSymbol(nextChar());
/* 1269 */     ClassFile.NameAndType localNameAndType = readNameAndType(nextChar());
/*      */ 
/* 1271 */     if (localClassSymbol2.members_field == null) {
/* 1272 */       throw badClassFile("bad.enclosing.class", new Object[] { localClassSymbol1, localClassSymbol2 });
/*      */     }
/* 1274 */     Symbol.MethodSymbol localMethodSymbol = findMethod(localNameAndType, localClassSymbol2.members_field, localClassSymbol1.flags());
/* 1275 */     if ((localNameAndType != null) && (localMethodSymbol == null)) {
/* 1276 */       throw badClassFile("bad.enclosing.method", new Object[] { localClassSymbol1 });
/*      */     }
/* 1278 */     localClassSymbol1.name = simpleBinaryName(localClassSymbol1.flatname, localClassSymbol2.flatname);
/* 1279 */     localClassSymbol1.owner = (localMethodSymbol != null ? localMethodSymbol : localClassSymbol2);
/* 1280 */     if (localClassSymbol1.name.isEmpty())
/* 1281 */       localClassSymbol1.fullname = this.names.empty;
/*      */     else {
/* 1283 */       localClassSymbol1.fullname = Symbol.ClassSymbol.formFullName(localClassSymbol1.name, localClassSymbol1.owner);
/*      */     }
/* 1285 */     if (localMethodSymbol != null)
/* 1286 */       ((Type.ClassType)paramSymbol.type).setEnclosingType(localMethodSymbol.type);
/* 1287 */     else if ((localClassSymbol1.flags_field & 0x8) == 0L)
/* 1288 */       ((Type.ClassType)paramSymbol.type).setEnclosingType(localClassSymbol2.type);
/*      */     else {
/* 1290 */       ((Type.ClassType)paramSymbol.type).setEnclosingType(Type.noType);
/*      */     }
/* 1292 */     enterTypevars(localClassSymbol1);
/* 1293 */     if (!this.missingTypeVariables.isEmpty()) {
/* 1294 */       ListBuffer localListBuffer = new ListBuffer();
/* 1295 */       for (Type localType : this.missingTypeVariables) {
/* 1296 */         localListBuffer.append(findTypeVar(localType.tsym.name));
/*      */       }
/* 1298 */       this.foundTypeVariables = localListBuffer.toList();
/*      */     } else {
/* 1300 */       this.foundTypeVariables = List.nil();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Name simpleBinaryName(Name paramName1, Name paramName2)
/*      */   {
/* 1306 */     String str = paramName1.toString().substring(paramName2.toString().length());
/* 1307 */     if ((str.length() < 1) || (str.charAt(0) != '$'))
/* 1308 */       throw badClassFile("bad.enclosing.method", new Object[] { paramName1 });
/* 1309 */     int i = 1;
/* 1310 */     while ((i < str.length()) && 
/* 1311 */       (isAsciiDigit(str
/* 1311 */       .charAt(i))))
/*      */     {
/* 1312 */       i++;
/* 1313 */     }return this.names.fromString(str.substring(i));
/*      */   }
/*      */ 
/*      */   private Symbol.MethodSymbol findMethod(ClassFile.NameAndType paramNameAndType, Scope paramScope, long paramLong) {
/* 1317 */     if (paramNameAndType == null) {
/* 1318 */       return null;
/*      */     }
/* 1320 */     Type.MethodType localMethodType = paramNameAndType.uniqueType.type.asMethodType();
/*      */ 
/* 1322 */     for (Scope.Entry localEntry = paramScope.lookup(paramNameAndType.name); localEntry.scope != null; localEntry = localEntry.next()) {
/* 1323 */       if ((localEntry.sym.kind == 16) && (isSameBinaryType(localEntry.sym.type.asMethodType(), localMethodType)))
/* 1324 */         return (Symbol.MethodSymbol)localEntry.sym;
/*      */     }
/* 1326 */     if (paramNameAndType.name != this.names.init)
/*      */     {
/* 1328 */       return null;
/* 1329 */     }if ((paramLong & 0x200) != 0L)
/*      */     {
/* 1331 */       return null;
/* 1332 */     }if (paramNameAndType.uniqueType.type.getParameterTypes().isEmpty())
/*      */     {
/* 1334 */       return null;
/*      */     }
/*      */ 
/* 1338 */     paramNameAndType.setType(new Type.MethodType(paramNameAndType.uniqueType.type.getParameterTypes().tail, paramNameAndType.uniqueType.type
/* 1339 */       .getReturnType(), paramNameAndType.uniqueType.type
/* 1340 */       .getThrownTypes(), this.syms.methodClass));
/*      */ 
/* 1343 */     return findMethod(paramNameAndType, paramScope, paramLong);
/*      */   }
/*      */ 
/*      */   private boolean isSameBinaryType(Type.MethodType paramMethodType1, Type.MethodType paramMethodType2)
/*      */   {
/* 1349 */     List localList1 = this.types.erasure(paramMethodType1.getParameterTypes())
/* 1349 */       .prepend(this.types
/* 1349 */       .erasure(paramMethodType1
/* 1349 */       .getReturnType()));
/* 1350 */     List localList2 = paramMethodType2.getParameterTypes().prepend(paramMethodType2.getReturnType());
/* 1351 */     while ((!localList1.isEmpty()) && (!localList2.isEmpty())) {
/* 1352 */       if (((Type)localList1.head).tsym != ((Type)localList2.head).tsym)
/* 1353 */         return false;
/* 1354 */       localList1 = localList1.tail;
/* 1355 */       localList2 = localList2.tail;
/*      */     }
/* 1357 */     return (localList1.isEmpty()) && (localList2.isEmpty());
/*      */   }
/*      */ 
/*      */   private static boolean isAsciiDigit(char paramChar)
/*      */   {
/* 1365 */     return ('0' <= paramChar) && (paramChar <= '9');
/*      */   }
/*      */ 
/*      */   void readMemberAttrs(Symbol paramSymbol)
/*      */   {
/* 1371 */     readAttrs(paramSymbol, AttributeKind.MEMBER);
/*      */   }
/*      */ 
/*      */   void readAttrs(Symbol paramSymbol, AttributeKind paramAttributeKind) {
/* 1375 */     int i = nextChar();
/* 1376 */     for (int j = 0; j < i; j++) {
/* 1377 */       Name localName = readName(nextChar());
/* 1378 */       int k = nextInt();
/* 1379 */       AttributeReader localAttributeReader = (AttributeReader)this.attributeReaders.get(localName);
/* 1380 */       if ((localAttributeReader != null) && (localAttributeReader.accepts(paramAttributeKind))) {
/* 1381 */         localAttributeReader.read(paramSymbol, k);
/*      */       } else {
/* 1383 */         unrecognized(localName);
/* 1384 */         this.bp += k;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void readClassAttrs(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1396 */     readAttrs(paramClassSymbol, AttributeKind.CLASS);
/*      */   }
/*      */ 
/*      */   Code readCode(Symbol paramSymbol)
/*      */   {
/* 1402 */     nextChar();
/* 1403 */     nextChar();
/* 1404 */     int i = nextInt();
/* 1405 */     this.bp += i;
/* 1406 */     int j = nextChar();
/* 1407 */     this.bp += j * 8;
/* 1408 */     readMemberAttrs(paramSymbol);
/* 1409 */     return null;
/*      */   }
/*      */ 
/*      */   void attachAnnotations(Symbol paramSymbol)
/*      */   {
/* 1419 */     int i = nextChar();
/* 1420 */     if (i != 0) {
/* 1421 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1423 */       for (int j = 0; j < i; j++) {
/* 1424 */         CompoundAnnotationProxy localCompoundAnnotationProxy = readCompoundAnnotation();
/* 1425 */         if (localCompoundAnnotationProxy.type.tsym == this.syms.proprietaryType.tsym)
/* 1426 */           paramSymbol.flags_field |= 274877906944L;
/* 1427 */         else if (localCompoundAnnotationProxy.type.tsym == this.syms.profileType.tsym) {
/* 1428 */           if (this.profile != Profile.DEFAULT) {
/* 1429 */             for (Pair localPair : localCompoundAnnotationProxy.values) {
/* 1430 */               if ((localPair.fst == this.names.value) && ((localPair.snd instanceof Attribute.Constant))) {
/* 1431 */                 Attribute.Constant localConstant = (Attribute.Constant)localPair.snd;
/* 1432 */                 if ((localConstant.type == this.syms.intType) && (((Integer)localConstant.value).intValue() > this.profile.value))
/* 1433 */                   paramSymbol.flags_field |= 35184372088832L;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/* 1439 */           localListBuffer.append(localCompoundAnnotationProxy);
/*      */       }
/* 1441 */       this.annotate.normal(new AnnotationCompleter(paramSymbol, localListBuffer.toList()));
/*      */     }
/*      */   }
/*      */ 
/*      */   void attachParameterAnnotations(Symbol paramSymbol)
/*      */   {
/* 1448 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)paramSymbol;
/* 1449 */     int i = this.buf[(this.bp++)] & 0xFF;
/* 1450 */     List localList = localMethodSymbol.params();
/* 1451 */     int j = 0;
/* 1452 */     while (localList.tail != null) {
/* 1453 */       attachAnnotations((Symbol)localList.head);
/* 1454 */       localList = localList.tail;
/* 1455 */       j++;
/*      */     }
/* 1457 */     if (j != i)
/* 1458 */       throw badClassFile("bad.runtime.invisible.param.annotations", new Object[] { localMethodSymbol });
/*      */   }
/*      */ 
/*      */   void attachTypeAnnotations(Symbol paramSymbol)
/*      */   {
/* 1463 */     int i = nextChar();
/* 1464 */     if (i != 0) {
/* 1465 */       ListBuffer localListBuffer = new ListBuffer();
/* 1466 */       for (int j = 0; j < i; j++)
/* 1467 */         localListBuffer.append(readTypeAnnotation());
/* 1468 */       this.annotate.normal(new TypeAnnotationCompleter(paramSymbol, localListBuffer.toList()));
/*      */     }
/*      */   }
/*      */ 
/*      */   void attachAnnotationDefault(Symbol paramSymbol)
/*      */   {
/* 1475 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)paramSymbol;
/* 1476 */     Attribute localAttribute = readAttributeValue();
/*      */ 
/* 1486 */     localMethodSymbol.defaultValue = localAttribute;
/* 1487 */     this.annotate.normal(new AnnotationDefaultCompleter(localMethodSymbol, localAttribute));
/*      */   }
/*      */ 
/*      */   Type readTypeOrClassSymbol(int paramInt)
/*      */   {
/* 1492 */     if (this.buf[this.poolIdx[paramInt]] == 7)
/* 1493 */       return readClassSymbol(paramInt).type;
/* 1494 */     return readType(paramInt);
/*      */   }
/*      */ 
/*      */   Type readEnumType(int paramInt) {
/* 1498 */     int i = this.poolIdx[paramInt];
/* 1499 */     int j = getChar(i + 1);
/* 1500 */     if (this.buf[(i + j + 2)] != 59)
/* 1501 */       return enterClass(readName(paramInt)).type;
/* 1502 */     return readType(paramInt);
/*      */   }
/*      */ 
/*      */   CompoundAnnotationProxy readCompoundAnnotation() {
/* 1506 */     Type localType = readTypeOrClassSymbol(nextChar());
/* 1507 */     int i = nextChar();
/* 1508 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1510 */     for (int j = 0; j < i; j++) {
/* 1511 */       Name localName = readName(nextChar());
/* 1512 */       Attribute localAttribute = readAttributeValue();
/* 1513 */       localListBuffer.append(new Pair(localName, localAttribute));
/*      */     }
/* 1515 */     return new CompoundAnnotationProxy(localType, localListBuffer.toList());
/*      */   }
/*      */ 
/*      */   TypeAnnotationProxy readTypeAnnotation() {
/* 1519 */     TypeAnnotationPosition localTypeAnnotationPosition = readPosition();
/* 1520 */     CompoundAnnotationProxy localCompoundAnnotationProxy = readCompoundAnnotation();
/*      */ 
/* 1522 */     return new TypeAnnotationProxy(localCompoundAnnotationProxy, localTypeAnnotationPosition);
/*      */   }
/*      */ 
/*      */   TypeAnnotationPosition readPosition() {
/* 1526 */     int i = nextByte();
/*      */ 
/* 1528 */     if (!TargetType.isValidTargetTypeValue(i)) {
/* 1529 */       throw badClassFile("bad.type.annotation.value", new Object[] { String.format("0x%02X", new Object[] { Integer.valueOf(i) }) });
/*      */     }
/* 1531 */     TypeAnnotationPosition localTypeAnnotationPosition = new TypeAnnotationPosition();
/* 1532 */     TargetType localTargetType = TargetType.fromTargetTypeValue(i);
/*      */ 
/* 1534 */     localTypeAnnotationPosition.type = localTargetType;
/*      */ 
/* 1536 */     switch (25.$SwitchMap$com$sun$tools$javac$code$TargetType[localTargetType.ordinal()])
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/* 1544 */       localTypeAnnotationPosition.offset = nextChar();
/* 1545 */       break;
/*      */     case 5:
/*      */     case 6:
/* 1550 */       j = nextChar();
/* 1551 */       localTypeAnnotationPosition.lvarOffset = new int[j];
/* 1552 */       localTypeAnnotationPosition.lvarLength = new int[j];
/* 1553 */       localTypeAnnotationPosition.lvarIndex = new int[j];
/*      */ 
/* 1555 */       for (int k = 0; k < j; k++) {
/* 1556 */         localTypeAnnotationPosition.lvarOffset[k] = nextChar();
/* 1557 */         localTypeAnnotationPosition.lvarLength[k] = nextChar();
/* 1558 */         localTypeAnnotationPosition.lvarIndex[k] = nextChar();
/*      */       }
/* 1560 */       break;
/*      */     case 7:
/* 1563 */       localTypeAnnotationPosition.exception_index = nextChar();
/* 1564 */       break;
/*      */     case 8:
/* 1568 */       break;
/*      */     case 9:
/*      */     case 10:
/* 1572 */       localTypeAnnotationPosition.parameter_index = nextByte();
/* 1573 */       break;
/*      */     case 11:
/*      */     case 12:
/* 1577 */       localTypeAnnotationPosition.parameter_index = nextByte();
/* 1578 */       localTypeAnnotationPosition.bound_index = nextByte();
/* 1579 */       break;
/*      */     case 13:
/* 1582 */       localTypeAnnotationPosition.type_index = nextChar();
/* 1583 */       break;
/*      */     case 14:
/* 1586 */       localTypeAnnotationPosition.type_index = nextChar();
/* 1587 */       break;
/*      */     case 15:
/* 1590 */       localTypeAnnotationPosition.parameter_index = nextByte();
/* 1591 */       break;
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/* 1599 */       localTypeAnnotationPosition.offset = nextChar();
/* 1600 */       localTypeAnnotationPosition.type_index = nextByte();
/* 1601 */       break;
/*      */     case 21:
/*      */     case 22:
/* 1605 */       break;
/*      */     case 23:
/* 1607 */       throw new AssertionError("jvm.ClassReader: UNKNOWN target type should never occur!");
/*      */     default:
/* 1609 */       throw new AssertionError("jvm.ClassReader: Unknown target type for position: " + localTypeAnnotationPosition);
/*      */     }
/*      */ 
/* 1613 */     int j = nextByte();
/* 1614 */     ListBuffer localListBuffer = new ListBuffer();
/* 1615 */     for (int m = 0; m < j * 2; m++)
/* 1616 */       localListBuffer = localListBuffer.append(Integer.valueOf(nextByte()));
/* 1617 */     localTypeAnnotationPosition.location = TypeAnnotationPosition.getTypePathFromBinary(localListBuffer.toList());
/*      */ 
/* 1620 */     return localTypeAnnotationPosition;
/*      */   }
/*      */ 
/*      */   Attribute readAttributeValue() {
/* 1624 */     char c = (char)this.buf[(this.bp++)];
/* 1625 */     switch (c) {
/*      */     case 'B':
/* 1627 */       return new Attribute.Constant(this.syms.byteType, readPool(nextChar()));
/*      */     case 'C':
/* 1629 */       return new Attribute.Constant(this.syms.charType, readPool(nextChar()));
/*      */     case 'D':
/* 1631 */       return new Attribute.Constant(this.syms.doubleType, readPool(nextChar()));
/*      */     case 'F':
/* 1633 */       return new Attribute.Constant(this.syms.floatType, readPool(nextChar()));
/*      */     case 'I':
/* 1635 */       return new Attribute.Constant(this.syms.intType, readPool(nextChar()));
/*      */     case 'J':
/* 1637 */       return new Attribute.Constant(this.syms.longType, readPool(nextChar()));
/*      */     case 'S':
/* 1639 */       return new Attribute.Constant(this.syms.shortType, readPool(nextChar()));
/*      */     case 'Z':
/* 1641 */       return new Attribute.Constant(this.syms.booleanType, readPool(nextChar()));
/*      */     case 's':
/* 1643 */       return new Attribute.Constant(this.syms.stringType, readPool(nextChar()).toString());
/*      */     case 'e':
/* 1645 */       return new EnumAttributeProxy(readEnumType(nextChar()), readName(nextChar()));
/*      */     case 'c':
/* 1647 */       return new Attribute.Class(this.types, readTypeOrClassSymbol(nextChar()));
/*      */     case '[':
/* 1649 */       int i = nextChar();
/* 1650 */       ListBuffer localListBuffer = new ListBuffer();
/* 1651 */       for (int j = 0; j < i; j++)
/* 1652 */         localListBuffer.append(readAttributeValue());
/* 1653 */       return new ArrayAttributeProxy(localListBuffer.toList());
/*      */     case '@':
/* 1656 */       return readCompoundAnnotation();
/*      */     case 'A':
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     case '\\':
/*      */     case ']':
/*      */     case '^':
/*      */     case '_':
/*      */     case '`':
/*      */     case 'a':
/*      */     case 'b':
/*      */     case 'd':
/*      */     case 'f':
/*      */     case 'g':
/*      */     case 'h':
/*      */     case 'i':
/*      */     case 'j':
/*      */     case 'k':
/*      */     case 'l':
/*      */     case 'm':
/*      */     case 'n':
/*      */     case 'o':
/*      */     case 'p':
/*      */     case 'q':
/* 1658 */     case 'r': } throw new AssertionError("unknown annotation tag '" + c + "'");
/*      */   }
/*      */ 
/*      */   Symbol.VarSymbol readField()
/*      */   {
/* 1996 */     long l = adjustFieldFlags(nextChar());
/* 1997 */     Name localName = readName(nextChar());
/* 1998 */     Type localType = readType(nextChar());
/* 1999 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(l, localName, localType, this.currentOwner);
/* 2000 */     readMemberAttrs(localVarSymbol);
/* 2001 */     return localVarSymbol;
/*      */   }
/*      */ 
/*      */   Symbol.MethodSymbol readMethod()
/*      */   {
/* 2007 */     long l = adjustMethodFlags(nextChar());
/* 2008 */     Name localName = readName(nextChar());
/* 2009 */     Object localObject1 = readType(nextChar());
/* 2010 */     if ((this.currentOwner.isInterface()) && ((l & 0x400) == 0L) && 
/* 2011 */       (!localName
/* 2011 */       .equals(this.names.clinit)))
/*      */     {
/* 2012 */       if ((this.majorVersion > Target.JDK1_8.majorVersion) || ((this.majorVersion == Target.JDK1_8.majorVersion) && (this.minorVersion >= Target.JDK1_8.minorVersion)))
/*      */       {
/* 2014 */         if ((l & 0x8) == 0L) {
/* 2015 */           this.currentOwner.flags_field |= 8796093022208L;
/* 2016 */           l |= 8796093023232L;
/*      */         }
/*      */       }
/*      */       else {
/* 2020 */         throw badClassFile((l & 0x8) == 0L ? "invalid.default.interface" : "invalid.static.interface", new Object[] { 
/* 2021 */           Integer.toString(this.majorVersion), 
/* 2022 */           Integer.toString(this.minorVersion) });
/*      */       }
/*      */     }
/*      */ 
/* 2025 */     if ((localName == this.names.init) && (this.currentOwner.hasOuterInstance()))
/*      */     {
/* 2029 */       if (!this.currentOwner.name.isEmpty())
/*      */       {
/* 2032 */         localObject1 = new Type.MethodType(adjustMethodParams(l, ((Type)localObject1).getParameterTypes()), ((Type)localObject1)
/* 2031 */           .getReturnType(), ((Type)localObject1)
/* 2032 */           .getThrownTypes(), this.syms.methodClass);
/*      */       }
/*      */     }
/* 2035 */     Symbol.MethodSymbol localMethodSymbol = new Symbol.MethodSymbol(l, localName, (Type)localObject1, this.currentOwner);
/* 2036 */     if (this.types.isSignaturePolymorphic(localMethodSymbol)) {
/* 2037 */       localMethodSymbol.flags_field |= 70368744177664L;
/*      */     }
/* 2039 */     if (this.saveParameterNames)
/* 2040 */       initParameterNames(localMethodSymbol);
/* 2041 */     Symbol localSymbol = this.currentOwner;
/* 2042 */     this.currentOwner = localMethodSymbol;
/*      */     try {
/* 2044 */       readMemberAttrs(localMethodSymbol);
/*      */     } finally {
/* 2046 */       this.currentOwner = localSymbol;
/*      */     }
/* 2048 */     if (this.saveParameterNames)
/* 2049 */       setParameterNames(localMethodSymbol, (Type)localObject1);
/* 2050 */     return localMethodSymbol;
/*      */   }
/*      */ 
/*      */   private List<Type> adjustMethodParams(long paramLong, List<Type> paramList) {
/* 2054 */     int i = (paramLong & 0x0) != 0L ? 1 : 0;
/* 2055 */     if (i != 0) {
/* 2056 */       Type localType1 = (Type)paramList.last();
/* 2057 */       ListBuffer localListBuffer = new ListBuffer();
/* 2058 */       for (Type localType2 : paramList) {
/* 2059 */         localListBuffer.append(localType2 != localType1 ? localType2 : ((Type.ArrayType)localType2)
/* 2061 */           .makeVarargs());
/*      */       }
/* 2063 */       paramList = localListBuffer.toList();
/*      */     }
/* 2065 */     return paramList.tail;
/*      */   }
/*      */ 
/*      */   void initParameterNames(Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 2082 */     int i = Code.width(paramMethodSymbol.type
/* 2082 */       .getParameterTypes()) + 4;
/* 2083 */     if ((this.parameterNameIndices == null) || (this.parameterNameIndices.length < i))
/*      */     {
/* 2085 */       this.parameterNameIndices = new int[i];
/*      */     }
/* 2087 */     else Arrays.fill(this.parameterNameIndices, 0);
/* 2088 */     this.haveParameterNameIndices = false;
/* 2089 */     this.sawMethodParameters = false;
/*      */   }
/*      */ 
/*      */   void setParameterNames(Symbol.MethodSymbol paramMethodSymbol, Type paramType)
/*      */   {
/* 2107 */     if (!this.haveParameterNameIndices) {
/* 2108 */       return;
/*      */     }
/*      */ 
/* 2111 */     int i = 0;
/* 2112 */     if (!this.sawMethodParameters) {
/* 2113 */       i = (paramMethodSymbol.flags() & 0x8) == 0L ? 1 : 0;
/*      */ 
/* 2120 */       if ((paramMethodSymbol.name == this.names.init) && (this.currentOwner.hasOuterInstance()))
/*      */       {
/* 2124 */         if (!this.currentOwner.name.isEmpty()) {
/* 2125 */           i++;
/*      */         }
/*      */       }
/* 2128 */       if (paramMethodSymbol.type != paramType)
/*      */       {
/* 2139 */         int j = Code.width(paramType.getParameterTypes()) - 
/* 2139 */           Code.width(paramMethodSymbol.type
/* 2139 */           .getParameterTypes());
/* 2140 */         i += j;
/*      */       }
/*      */     }
/* 2143 */     List localList = List.nil();
/* 2144 */     int k = i;
/* 2145 */     for (Type localType : paramMethodSymbol.type.getParameterTypes()) {
/* 2146 */       int m = k < this.parameterNameIndices.length ? this.parameterNameIndices[k] : 0;
/*      */ 
/* 2148 */       Name localName = m == 0 ? this.names.empty : readName(m);
/* 2149 */       localList = localList.prepend(localName);
/* 2150 */       k += Code.width(localType);
/*      */     }
/* 2152 */     paramMethodSymbol.savedParameterNames = localList.reverse();
/*      */   }
/*      */ 
/*      */   void skipBytes(int paramInt)
/*      */   {
/* 2159 */     this.bp += paramInt;
/*      */   }
/*      */ 
/*      */   void skipMember()
/*      */   {
/* 2165 */     this.bp += 6;
/* 2166 */     int i = nextChar();
/* 2167 */     for (int j = 0; j < i; j++) {
/* 2168 */       this.bp += 2;
/* 2169 */       int k = nextInt();
/* 2170 */       this.bp += k;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void enterTypevars(Type paramType)
/*      */   {
/* 2178 */     if ((paramType.getEnclosingType() != null) && (paramType.getEnclosingType().hasTag(TypeTag.CLASS)))
/* 2179 */       enterTypevars(paramType.getEnclosingType());
/* 2180 */     for (List localList = paramType.getTypeArguments(); localList.nonEmpty(); localList = localList.tail)
/* 2181 */       this.typevars.enter(((Type)localList.head).tsym);
/*      */   }
/*      */ 
/*      */   protected void enterTypevars(Symbol paramSymbol) {
/* 2185 */     if (paramSymbol.owner.kind == 16) {
/* 2186 */       enterTypevars(paramSymbol.owner);
/* 2187 */       enterTypevars(paramSymbol.owner.owner);
/*      */     }
/* 2189 */     enterTypevars(paramSymbol.type);
/*      */   }
/*      */ 
/*      */   void readClass(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2196 */     Type.ClassType localClassType = (Type.ClassType)paramClassSymbol.type;
/*      */ 
/* 2199 */     paramClassSymbol.members_field = new Scope(paramClassSymbol);
/*      */ 
/* 2202 */     this.typevars = this.typevars.dup(this.currentOwner);
/* 2203 */     if (localClassType.getEnclosingType().hasTag(TypeTag.CLASS)) {
/* 2204 */       enterTypevars(localClassType.getEnclosingType());
/*      */     }
/*      */ 
/* 2207 */     long l = adjustClassFlags(nextChar());
/* 2208 */     if (paramClassSymbol.owner.kind == 1) paramClassSymbol.flags_field = l;
/*      */ 
/* 2211 */     Symbol.ClassSymbol localClassSymbol = readClassSymbol(nextChar());
/* 2212 */     if (paramClassSymbol != localClassSymbol) {
/* 2213 */       throw badClassFile("class.file.wrong.class", new Object[] { localClassSymbol.flatname });
/*      */     }
/*      */ 
/* 2218 */     int i = this.bp;
/* 2219 */     nextChar();
/* 2220 */     int j = nextChar();
/* 2221 */     this.bp += j * 2;
/* 2222 */     int k = nextChar();
/* 2223 */     for (int m = 0; m < k; m++) skipMember();
/* 2224 */     m = nextChar();
/* 2225 */     for (int n = 0; n < m; n++) skipMember();
/* 2226 */     readClassAttrs(paramClassSymbol);
/*      */ 
/* 2228 */     if (this.readAllOfClassFile) {
/* 2229 */       for (n = 1; n < this.poolObj.length; n++) readPool(n);
/* 2230 */       paramClassSymbol.pool = new Pool(this.poolObj.length, this.poolObj, this.types);
/*      */     }
/*      */ 
/* 2234 */     this.bp = i;
/* 2235 */     n = nextChar();
/* 2236 */     if (localClassType.supertype_field == null)
/* 2237 */       localClassType.supertype_field = (n == 0 ? Type.noType : 
/* 2239 */         readClassSymbol(n)
/* 2239 */         .erasure(this.types));
/* 2240 */     n = nextChar();
/* 2241 */     List localList = List.nil();
/* 2242 */     for (int i1 = 0; i1 < n; i1++) {
/* 2243 */       Type localType = readClassSymbol(nextChar()).erasure(this.types);
/* 2244 */       localList = localList.prepend(localType);
/*      */     }
/* 2246 */     if (localClassType.interfaces_field == null) {
/* 2247 */       localClassType.interfaces_field = localList.reverse();
/*      */     }
/* 2249 */     Assert.check(k == nextChar());
/* 2250 */     for (i1 = 0; i1 < k; i1++) enterMember(paramClassSymbol, readField());
/* 2251 */     Assert.check(m == nextChar());
/* 2252 */     for (i1 = 0; i1 < m; i1++) enterMember(paramClassSymbol, readMethod());
/*      */ 
/* 2254 */     this.typevars = this.typevars.leave();
/*      */   }
/*      */ 
/*      */   void readInnerClasses(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2261 */     int i = nextChar();
/* 2262 */     for (int j = 0; j < i; j++) {
/* 2263 */       nextChar();
/* 2264 */       Symbol.ClassSymbol localClassSymbol1 = readClassSymbol(nextChar());
/* 2265 */       Name localName = readName(nextChar());
/* 2266 */       if (localName == null) localName = this.names.empty;
/* 2267 */       long l = adjustClassFlags(nextChar());
/* 2268 */       if (localClassSymbol1 != null) {
/* 2269 */         if (localName == this.names.empty)
/* 2270 */           localName = this.names.one;
/* 2271 */         Symbol.ClassSymbol localClassSymbol2 = enterClass(localName, localClassSymbol1);
/* 2272 */         if ((l & 0x8) == 0L) {
/* 2273 */           ((Type.ClassType)localClassSymbol2.type).setEnclosingType(localClassSymbol1.type);
/* 2274 */           if (localClassSymbol2.erasure_field != null)
/* 2275 */             ((Type.ClassType)localClassSymbol2.erasure_field).setEnclosingType(this.types.erasure(localClassSymbol1.type));
/*      */         }
/* 2277 */         if (paramClassSymbol == localClassSymbol1) {
/* 2278 */           localClassSymbol2.flags_field = l;
/* 2279 */           enterMember(paramClassSymbol, localClassSymbol2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readClassFile(Symbol.ClassSymbol paramClassSymbol)
/*      */     throws IOException
/*      */   {
/* 2288 */     int i = nextInt();
/* 2289 */     if (i != -889275714) {
/* 2290 */       throw badClassFile("illegal.start.of.class.file", new Object[0]);
/*      */     }
/* 2292 */     this.minorVersion = nextChar();
/* 2293 */     this.majorVersion = nextChar();
/* 2294 */     int j = Target.MAX().majorVersion;
/* 2295 */     int k = Target.MAX().minorVersion;
/* 2296 */     if ((this.majorVersion > j) || 
/* 2298 */       (this.majorVersion * 1000 + this.minorVersion < 
/* 2298 */       Target.MIN().majorVersion * 1000 + Target.MIN().minorVersion))
/*      */     {
/* 2300 */       if (this.majorVersion == j + 1) {
/* 2301 */         this.log.warning("big.major.version", new Object[] { this.currentClassFile, 
/* 2303 */           Integer.valueOf(this.majorVersion), 
/* 2304 */           Integer.valueOf(j) });
/*      */       }
/*      */       else {
/* 2306 */         throw badClassFile("wrong.version", new Object[] { 
/* 2307 */           Integer.toString(this.majorVersion), 
/* 2308 */           Integer.toString(this.minorVersion), 
/* 2309 */           Integer.toString(j), 
/* 2310 */           Integer.toString(k) });
/*      */       }
/*      */     }
/* 2312 */     else if ((this.checkClassFile) && (this.majorVersion == j) && (this.minorVersion > k))
/*      */     {
/* 2316 */       printCCF("found.later.version", 
/* 2317 */         Integer.toString(this.minorVersion));
/*      */     }
/*      */ 
/* 2319 */     indexPool();
/* 2320 */     if (this.signatureBuffer.length < this.bp) {
/* 2321 */       int m = Integer.highestOneBit(this.bp) << 1;
/* 2322 */       this.signatureBuffer = new byte[m];
/*      */     }
/* 2324 */     readClass(paramClassSymbol);
/*      */   }
/*      */ 
/*      */   long adjustFieldFlags(long paramLong)
/*      */   {
/* 2332 */     return paramLong;
/*      */   }
/*      */   long adjustMethodFlags(long paramLong) {
/* 2335 */     if ((paramLong & 0x40) != 0L) {
/* 2336 */       paramLong &= -65L;
/* 2337 */       paramLong |= 2147483648L;
/* 2338 */       if (!this.allowGenerics)
/* 2339 */         paramLong &= -4097L;
/*      */     }
/* 2341 */     if ((paramLong & 0x80) != 0L) {
/* 2342 */       paramLong &= -129L;
/* 2343 */       paramLong |= 17179869184L;
/*      */     }
/* 2345 */     return paramLong;
/*      */   }
/*      */   long adjustClassFlags(long paramLong) {
/* 2348 */     return paramLong & 0xFFFFFFDF;
/*      */   }
/*      */ 
/*      */   public Symbol.ClassSymbol defineClass(Name paramName, Symbol paramSymbol)
/*      */   {
/* 2358 */     Symbol.ClassSymbol localClassSymbol = new Symbol.ClassSymbol(0L, paramName, paramSymbol);
/* 2359 */     if (paramSymbol.kind == 1)
/* 2360 */       Assert.checkNull(this.classes.get(localClassSymbol.flatname), localClassSymbol);
/* 2361 */     localClassSymbol.completer = this.thisCompleter;
/* 2362 */     return localClassSymbol;
/*      */   }
/*      */ 
/*      */   public Symbol.ClassSymbol enterClass(Name paramName, Symbol.TypeSymbol paramTypeSymbol)
/*      */   {
/* 2369 */     Name localName = Symbol.TypeSymbol.formFlatName(paramName, paramTypeSymbol);
/* 2370 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.classes.get(localName);
/* 2371 */     if (localClassSymbol == null) {
/* 2372 */       localClassSymbol = defineClass(paramName, paramTypeSymbol);
/* 2373 */       this.classes.put(localName, localClassSymbol);
/* 2374 */     } else if (((localClassSymbol.name != paramName) || (localClassSymbol.owner != paramTypeSymbol)) && (paramTypeSymbol.kind == 2) && (localClassSymbol.owner.kind == 1))
/*      */     {
/* 2377 */       localClassSymbol.owner.members().remove(localClassSymbol);
/* 2378 */       localClassSymbol.name = paramName;
/* 2379 */       localClassSymbol.owner = paramTypeSymbol;
/* 2380 */       localClassSymbol.fullname = Symbol.ClassSymbol.formFullName(paramName, paramTypeSymbol);
/*      */     }
/* 2382 */     return localClassSymbol;
/*      */   }
/*      */ 
/*      */   public Symbol.ClassSymbol enterClass(Name paramName, JavaFileObject paramJavaFileObject)
/*      */   {
/* 2396 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.classes.get(paramName);
/* 2397 */     if (localClassSymbol != null) {
/* 2398 */       localObject = Log.format("%s: completer = %s; class file = %s; source file = %s", new Object[] { localClassSymbol.fullname, localClassSymbol.completer, localClassSymbol.classfile, localClassSymbol.sourcefile });
/*      */ 
/* 2403 */       throw new AssertionError(localObject);
/*      */     }
/* 2405 */     Object localObject = Convert.packagePart(paramName);
/*      */ 
/* 2408 */     Symbol.PackageSymbol localPackageSymbol = ((Name)localObject).isEmpty() ? this.syms.unnamedPackage : 
/* 2408 */       enterPackage((Name)localObject);
/*      */ 
/* 2409 */     localClassSymbol = defineClass(Convert.shortName(paramName), localPackageSymbol);
/* 2410 */     localClassSymbol.classfile = paramJavaFileObject;
/* 2411 */     this.classes.put(paramName, localClassSymbol);
/* 2412 */     return localClassSymbol;
/*      */   }
/*      */ 
/*      */   public Symbol.ClassSymbol enterClass(Name paramName)
/*      */   {
/* 2419 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.classes.get(paramName);
/* 2420 */     if (localClassSymbol == null) {
/* 2421 */       return enterClass(paramName, (JavaFileObject)null);
/*      */     }
/* 2423 */     return localClassSymbol;
/*      */   }
/*      */ 
/*      */   private void complete(Symbol paramSymbol)
/*      */     throws Symbol.CompletionFailure
/*      */   {
/*      */     Object localObject1;
/* 2430 */     if (paramSymbol.kind == 2) {
/* 2431 */       localObject1 = (Symbol.ClassSymbol)paramSymbol;
/* 2432 */       ((Symbol.ClassSymbol)localObject1).members_field = new Scope.ErrorScope((Symbol)localObject1);
/* 2433 */       this.annotate.enterStart();
/*      */       try {
/* 2435 */         completeOwners(((Symbol.ClassSymbol)localObject1).owner);
/* 2436 */         completeEnclosing((Symbol.ClassSymbol)localObject1);
/*      */ 
/* 2440 */         this.annotate.enterDoneWithoutFlush(); } finally { this.annotate.enterDoneWithoutFlush(); }
/*      */ 
/*      */     }
/* 2443 */     else if (paramSymbol.kind == 1) {
/* 2444 */       localObject1 = (Symbol.PackageSymbol)paramSymbol;
/*      */       try {
/* 2446 */         fillIn((Symbol.PackageSymbol)localObject1);
/*      */       } catch (IOException localIOException) {
/* 2448 */         throw new Symbol.CompletionFailure(paramSymbol, localIOException.getLocalizedMessage()).initCause(localIOException);
/*      */       }
/*      */     }
/* 2451 */     if (!this.filling)
/* 2452 */       this.annotate.flush();
/*      */   }
/*      */ 
/*      */   private void completeOwners(Symbol paramSymbol)
/*      */   {
/* 2457 */     if (paramSymbol.kind != 1) completeOwners(paramSymbol.owner);
/* 2458 */     paramSymbol.complete();
/*      */   }
/*      */ 
/*      */   private void completeEnclosing(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*      */     Symbol localSymbol1;
/* 2468 */     if (paramClassSymbol.owner.kind == 1) {
/* 2469 */       localSymbol1 = paramClassSymbol.owner;
/* 2470 */       for (Name localName : Convert.enclosingCandidates(Convert.shortName(paramClassSymbol.name))) {
/* 2471 */         Symbol localSymbol2 = localSymbol1.members().lookup(localName).sym;
/* 2472 */         if (localSymbol2 == null)
/* 2473 */           localSymbol2 = (Symbol)this.classes.get(Symbol.TypeSymbol.formFlatName(localName, localSymbol1));
/* 2474 */         if (localSymbol2 != null)
/* 2475 */           localSymbol2.complete();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fillIn(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2490 */     if (this.completionFailureName == paramClassSymbol.fullname) {
/* 2491 */       throw new Symbol.CompletionFailure(paramClassSymbol, "user-selected completion failure by class name");
/*      */     }
/* 2493 */     this.currentOwner = paramClassSymbol;
/* 2494 */     this.warnedAttrs.clear();
/* 2495 */     JavaFileObject localJavaFileObject = paramClassSymbol.classfile;
/* 2496 */     if (localJavaFileObject != null) {
/* 2497 */       localObject1 = this.currentClassFile;
/*      */       try {
/* 2499 */         if (this.filling) {
/* 2500 */           Assert.error("Filling " + localJavaFileObject.toUri() + " during " + localObject1);
/*      */         }
/* 2502 */         this.currentClassFile = localJavaFileObject;
/* 2503 */         if (this.verbose) {
/* 2504 */           this.log.printVerbose("loading", new Object[] { this.currentClassFile.toString() });
/*      */         }
/* 2506 */         if (localJavaFileObject.getKind() == JavaFileObject.Kind.CLASS) {
/* 2507 */           this.filling = true;
/*      */           try {
/* 2509 */             this.bp = 0;
/* 2510 */             this.buf = readInputStream(this.buf, localJavaFileObject.openInputStream());
/* 2511 */             readClassFile(paramClassSymbol);
/*      */             Object localObject2;
/* 2512 */             if ((!this.missingTypeVariables.isEmpty()) && (!this.foundTypeVariables.isEmpty())) {
/* 2513 */               localObject2 = this.missingTypeVariables;
/* 2514 */               List localList = this.foundTypeVariables;
/* 2515 */               this.missingTypeVariables = List.nil();
/* 2516 */               this.foundTypeVariables = List.nil();
/* 2517 */               this.filling = false;
/* 2518 */               Type.ClassType localClassType = (Type.ClassType)this.currentOwner.type;
/* 2519 */               localClassType.supertype_field = this.types
/* 2520 */                 .subst(localClassType.supertype_field, (List)localObject2, localList);
/*      */ 
/* 2521 */               localClassType.interfaces_field = this.types
/* 2522 */                 .subst(localClassType.interfaces_field, (List)localObject2, localList);
/*      */             }
/* 2524 */             else if (this.missingTypeVariables.isEmpty() != this.foundTypeVariables
/* 2524 */               .isEmpty()) {
/* 2525 */               localObject2 = ((Type)this.missingTypeVariables.head).tsym.name;
/* 2526 */               throw badClassFile("undecl.type.var", new Object[] { localObject2 });
/*      */             }
/*      */           } finally {
/* 2529 */             this.missingTypeVariables = List.nil();
/* 2530 */             this.foundTypeVariables = List.nil();
/* 2531 */             this.filling = false;
/*      */           }
/*      */         }
/* 2534 */         else if (this.sourceCompleter != null) {
/* 2535 */           this.sourceCompleter.complete(paramClassSymbol);
/*      */         }
/*      */         else {
/* 2538 */           throw new IllegalStateException("Source completer required to read " + localJavaFileObject
/* 2538 */             .toUri());
/*      */         }
/*      */ 
/* 2541 */         return;
/*      */       } catch (IOException localIOException) {
/* 2543 */         throw badClassFile("unable.to.access.file", new Object[] { localIOException.getMessage() });
/*      */       } finally {
/* 2545 */         this.currentClassFile = ((JavaFileObject)localObject1);
/*      */       }
/*      */     }
/*      */ 
/* 2549 */     Object localObject1 = this.diagFactory
/* 2549 */       .fragment("class.file.not.found", new Object[] { paramClassSymbol.flatname });
/*      */ 
/* 2551 */     throw newCompletionFailure(paramClassSymbol, (JCDiagnostic)localObject1);
/*      */   }
/*      */ 
/*      */   private static byte[] readInputStream(byte[] paramArrayOfByte, InputStream paramInputStream) throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 2557 */       paramArrayOfByte = ensureCapacity(paramArrayOfByte, paramInputStream.available());
/* 2558 */       int i = paramInputStream.read(paramArrayOfByte);
/* 2559 */       int j = 0;
/* 2560 */       while (i != -1) {
/* 2561 */         j += i;
/* 2562 */         paramArrayOfByte = ensureCapacity(paramArrayOfByte, j);
/* 2563 */         i = paramInputStream.read(paramArrayOfByte, j, paramArrayOfByte.length - j);
/*      */       }
/* 2565 */       return paramArrayOfByte;
/*      */     } finally {
/*      */       try {
/* 2568 */         paramInputStream.close();
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static byte[] ensureCapacity(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 2584 */     if (paramArrayOfByte.length <= paramInt) {
/* 2585 */       byte[] arrayOfByte = paramArrayOfByte;
/* 2586 */       paramArrayOfByte = new byte[Integer.highestOneBit(paramInt) << 1];
/* 2587 */       System.arraycopy(arrayOfByte, 0, paramArrayOfByte, 0, arrayOfByte.length);
/*      */     }
/* 2589 */     return paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   private Symbol.CompletionFailure newCompletionFailure(Symbol.TypeSymbol paramTypeSymbol, JCDiagnostic paramJCDiagnostic)
/*      */   {
/* 2597 */     if (!this.cacheCompletionFailure)
/*      */     {
/* 2601 */       return new Symbol.CompletionFailure(paramTypeSymbol, paramJCDiagnostic);
/*      */     }
/* 2603 */     Symbol.CompletionFailure localCompletionFailure = this.cachedCompletionFailure;
/* 2604 */     localCompletionFailure.sym = paramTypeSymbol;
/* 2605 */     localCompletionFailure.diag = paramJCDiagnostic;
/* 2606 */     return localCompletionFailure;
/*      */   }
/*      */ 
/*      */   public Symbol.ClassSymbol loadClass(Name paramName)
/*      */     throws Symbol.CompletionFailure
/*      */   {
/* 2619 */     int i = this.classes.get(paramName) == null ? 1 : 0;
/* 2620 */     Symbol.ClassSymbol localClassSymbol = enterClass(paramName);
/* 2621 */     if ((localClassSymbol.members_field == null) && (localClassSymbol.completer != null)) {
/*      */       try {
/* 2623 */         localClassSymbol.complete();
/*      */       } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 2625 */         if (i != 0) this.classes.remove(paramName);
/* 2626 */         throw localCompletionFailure;
/*      */       }
/*      */     }
/* 2629 */     return localClassSymbol;
/*      */   }
/*      */ 
/*      */   public boolean packageExists(Name paramName)
/*      */   {
/* 2639 */     return enterPackage(paramName).exists();
/*      */   }
/*      */ 
/*      */   public Symbol.PackageSymbol enterPackage(Name paramName)
/*      */   {
/* 2645 */     Symbol.PackageSymbol localPackageSymbol = (Symbol.PackageSymbol)this.packages.get(paramName);
/* 2646 */     if (localPackageSymbol == null) {
/* 2647 */       Assert.check(!paramName.isEmpty(), "rootPackage missing!");
/*      */ 
/* 2650 */       localPackageSymbol = new Symbol.PackageSymbol(
/* 2649 */         Convert.shortName(paramName), 
/* 2650 */         enterPackage(Convert.packagePart(paramName)));
/*      */ 
/* 2651 */       localPackageSymbol.completer = this.thisCompleter;
/* 2652 */       this.packages.put(paramName, localPackageSymbol);
/*      */     }
/* 2654 */     return localPackageSymbol;
/*      */   }
/*      */ 
/*      */   public Symbol.PackageSymbol enterPackage(Name paramName, Symbol.PackageSymbol paramPackageSymbol)
/*      */   {
/* 2660 */     return enterPackage(Symbol.TypeSymbol.formFullName(paramName, paramPackageSymbol));
/*      */   }
/*      */ 
/*      */   protected void includeClassFile(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject)
/*      */   {
/* 2669 */     if ((paramPackageSymbol.flags_field & 0x800000) == 0L)
/* 2670 */       for (localObject = paramPackageSymbol; (localObject != null) && (((Symbol)localObject).kind == 1); localObject = ((Symbol)localObject).owner)
/* 2671 */         localObject.flags_field |= 8388608L;
/* 2672 */     Object localObject = paramJavaFileObject.getKind();
/*      */     int i;
/* 2674 */     if (localObject == JavaFileObject.Kind.CLASS)
/* 2675 */       i = 33554432;
/*      */     else
/* 2677 */       i = 67108864;
/* 2678 */     String str = this.fileManager.inferBinaryName(this.currentLoc, paramJavaFileObject);
/* 2679 */     int j = str.lastIndexOf(".");
/* 2680 */     Name localName = this.names.fromString(str.substring(j + 1));
/* 2681 */     int k = localName == this.names.package_info ? 1 : 0;
/*      */ 
/* 2684 */     Symbol.ClassSymbol localClassSymbol = k != 0 ? paramPackageSymbol.package_info : 
/* 2684 */       (Symbol.ClassSymbol)paramPackageSymbol.members_field
/* 2684 */       .lookup(localName).sym;
/*      */ 
/* 2685 */     if (localClassSymbol == null) {
/* 2686 */       localClassSymbol = enterClass(localName, paramPackageSymbol);
/* 2687 */       if (localClassSymbol.classfile == null)
/* 2688 */         localClassSymbol.classfile = paramJavaFileObject;
/* 2689 */       if (k != 0) {
/* 2690 */         paramPackageSymbol.package_info = localClassSymbol;
/*      */       }
/* 2692 */       else if (localClassSymbol.owner == paramPackageSymbol)
/* 2693 */         paramPackageSymbol.members_field.enter(localClassSymbol);
/*      */     }
/* 2695 */     else if ((localClassSymbol.classfile != null) && ((localClassSymbol.flags_field & i) == 0L))
/*      */     {
/* 2700 */       if ((localClassSymbol.flags_field & 0x6000000) != 0L)
/* 2701 */         localClassSymbol.classfile = preferredFileObject(paramJavaFileObject, localClassSymbol.classfile);
/*      */     }
/* 2703 */     localClassSymbol.flags_field |= i;
/*      */   }
/*      */ 
/*      */   protected JavaFileObject preferredFileObject(JavaFileObject paramJavaFileObject1, JavaFileObject paramJavaFileObject2)
/*      */   {
/* 2713 */     if (this.preferSource) {
/* 2714 */       return paramJavaFileObject1.getKind() == JavaFileObject.Kind.SOURCE ? paramJavaFileObject1 : paramJavaFileObject2;
/*      */     }
/* 2716 */     long l1 = paramJavaFileObject1.getLastModified();
/* 2717 */     long l2 = paramJavaFileObject2.getLastModified();
/*      */ 
/* 2720 */     return l1 > l2 ? paramJavaFileObject1 : paramJavaFileObject2;
/*      */   }
/*      */ 
/*      */   protected EnumSet<JavaFileObject.Kind> getPackageFileKinds()
/*      */   {
/* 2728 */     return EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.SOURCE);
/*      */   }
/*      */ 
/*      */   protected void extraFileActions(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void fillIn(Symbol.PackageSymbol paramPackageSymbol)
/*      */     throws IOException
/*      */   {
/* 2744 */     if (paramPackageSymbol.members_field == null) paramPackageSymbol.members_field = new Scope(paramPackageSymbol);
/* 2745 */     String str = paramPackageSymbol.fullname.toString();
/*      */ 
/* 2747 */     EnumSet localEnumSet1 = getPackageFileKinds();
/*      */ 
/* 2749 */     fillIn(paramPackageSymbol, StandardLocation.PLATFORM_CLASS_PATH, this.fileManager
/* 2750 */       .list(StandardLocation.PLATFORM_CLASS_PATH, str, 
/* 2752 */       EnumSet.of(JavaFileObject.Kind.CLASS), 
/* 2752 */       false));
/*      */ 
/* 2755 */     EnumSet localEnumSet2 = EnumSet.copyOf(localEnumSet1);
/* 2756 */     localEnumSet2.remove(JavaFileObject.Kind.SOURCE);
/* 2757 */     int i = !localEnumSet2.isEmpty() ? 1 : 0;
/*      */ 
/* 2759 */     EnumSet localEnumSet3 = EnumSet.copyOf(localEnumSet1);
/* 2760 */     localEnumSet3.remove(JavaFileObject.Kind.CLASS);
/* 2761 */     int j = !localEnumSet3.isEmpty() ? 1 : 0;
/*      */ 
/* 2763 */     boolean bool = this.fileManager.hasLocation(StandardLocation.SOURCE_PATH);
/*      */ 
/* 2765 */     if ((this.verbose) && (this.verbosePath) && 
/* 2766 */       ((this.fileManager instanceof StandardJavaFileManager))) {
/* 2767 */       StandardJavaFileManager localStandardJavaFileManager = (StandardJavaFileManager)this.fileManager;
/*      */       List localList;
/*      */       Iterator localIterator;
/*      */       File localFile;
/* 2768 */       if ((bool) && (j != 0)) {
/* 2769 */         localList = List.nil();
/* 2770 */         for (localIterator = localStandardJavaFileManager.getLocation(StandardLocation.SOURCE_PATH).iterator(); localIterator.hasNext(); ) { localFile = (File)localIterator.next();
/* 2771 */           localList = localList.prepend(localFile);
/*      */         }
/* 2773 */         this.log.printVerbose("sourcepath", new Object[] { localList.reverse().toString() });
/* 2774 */       } else if (j != 0) {
/* 2775 */         localList = List.nil();
/* 2776 */         for (localIterator = localStandardJavaFileManager.getLocation(StandardLocation.CLASS_PATH).iterator(); localIterator.hasNext(); ) { localFile = (File)localIterator.next();
/* 2777 */           localList = localList.prepend(localFile);
/*      */         }
/* 2779 */         this.log.printVerbose("sourcepath", new Object[] { localList.reverse().toString() });
/*      */       }
/* 2781 */       if (i != 0) {
/* 2782 */         localList = List.nil();
/* 2783 */         for (localIterator = localStandardJavaFileManager.getLocation(StandardLocation.PLATFORM_CLASS_PATH).iterator(); localIterator.hasNext(); ) { localFile = (File)localIterator.next();
/* 2784 */           localList = localList.prepend(localFile);
/*      */         }
/* 2786 */         for (localIterator = localStandardJavaFileManager.getLocation(StandardLocation.CLASS_PATH).iterator(); localIterator.hasNext(); ) { localFile = (File)localIterator.next();
/* 2787 */           localList = localList.prepend(localFile);
/*      */         }
/* 2789 */         this.log.printVerbose("classpath", new Object[] { localList.reverse().toString() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2794 */     if ((j != 0) && (!bool)) {
/* 2795 */       fillIn(paramPackageSymbol, StandardLocation.CLASS_PATH, this.fileManager
/* 2796 */         .list(StandardLocation.CLASS_PATH, str, localEnumSet1, false));
/*      */     }
/*      */     else
/*      */     {
/* 2801 */       if (i != 0) {
/* 2802 */         fillIn(paramPackageSymbol, StandardLocation.CLASS_PATH, this.fileManager
/* 2803 */           .list(StandardLocation.CLASS_PATH, str, localEnumSet2, false));
/*      */       }
/*      */ 
/* 2807 */       if (j != 0) {
/* 2808 */         fillIn(paramPackageSymbol, StandardLocation.SOURCE_PATH, this.fileManager
/* 2809 */           .list(StandardLocation.SOURCE_PATH, str, localEnumSet3, false));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2814 */     this.verbosePath = false;
/*      */   }
/*      */ 
/*      */   private void fillIn(Symbol.PackageSymbol paramPackageSymbol, JavaFileManager.Location paramLocation, Iterable<JavaFileObject> paramIterable)
/*      */   {
/* 2821 */     this.currentLoc = paramLocation;
/* 2822 */     for (JavaFileObject localJavaFileObject : paramIterable)
/* 2823 */       switch (localJavaFileObject.getKind())
/*      */       {
/*      */       case CLASS:
/*      */       case SOURCE:
/* 2827 */         String str1 = this.fileManager.inferBinaryName(this.currentLoc, localJavaFileObject);
/* 2828 */         String str2 = str1.substring(str1.lastIndexOf(".") + 1);
/* 2829 */         if ((SourceVersion.isIdentifier(str2)) || 
/* 2830 */           (str2
/* 2830 */           .equals("package-info")))
/*      */         {
/* 2831 */           includeClassFile(paramPackageSymbol, localJavaFileObject); } break;
/*      */       default:
/* 2835 */         extraFileActions(paramPackageSymbol, localJavaFileObject);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void printCCF(String paramString, Object paramObject)
/*      */   {
/* 2845 */     this.log.printLines(paramString, new Object[] { paramObject });
/*      */   }
/*      */ 
/*      */   class AnnotationCompleter extends ClassReader.AnnotationDeproxy
/*      */     implements Annotate.Worker
/*      */   {
/*      */     final Symbol sym;
/*      */     final List<ClassReader.CompoundAnnotationProxy> l;
/*      */     final JavaFileObject classFile;
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1931 */       return " ClassReader annotate " + this.sym.owner + "." + this.sym + " with " + this.l;
/*      */     }
/* 1933 */     AnnotationCompleter(List<ClassReader.CompoundAnnotationProxy> arg2) { super();
/*      */       Object localObject1;
/* 1934 */       this.sym = localObject1;
/*      */       Object localObject2;
/* 1935 */       this.l = localObject2;
/* 1936 */       this.classFile = ClassReader.this.currentClassFile; }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1940 */       JavaFileObject localJavaFileObject = ClassReader.this.currentClassFile;
/*      */       try {
/* 1942 */         ClassReader.this.currentClassFile = this.classFile;
/* 1943 */         List localList = deproxyCompoundList(this.l);
/* 1944 */         if (this.sym.annotationsPendingCompletion())
/* 1945 */           this.sym.setDeclarationAttributes(localList);
/*      */         else {
/* 1947 */           this.sym.appendAttributes(localList);
/*      */         }
/*      */ 
/* 1950 */         ClassReader.this.currentClassFile = localJavaFileObject; } finally { ClassReader.this.currentClassFile = localJavaFileObject; }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   class AnnotationDefaultCompleter extends ClassReader.AnnotationDeproxy
/*      */     implements Annotate.Worker
/*      */   {
/*      */     final Symbol.MethodSymbol sym;
/*      */     final Attribute value;
/* 1901 */     final JavaFileObject classFile = ClassReader.this.currentClassFile;
/*      */ 
/*      */     public String toString() {
/* 1904 */       return " ClassReader store default for " + this.sym.owner + "." + this.sym + " is " + this.value;
/*      */     }
/* 1906 */     AnnotationDefaultCompleter(Symbol.MethodSymbol paramAttribute, Attribute arg3) { super();
/* 1907 */       this.sym = paramAttribute;
/*      */       Object localObject;
/* 1908 */       this.value = localObject; }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1912 */       JavaFileObject localJavaFileObject = ClassReader.this.currentClassFile;
/*      */       try
/*      */       {
/* 1916 */         this.sym.defaultValue = null;
/* 1917 */         ClassReader.this.currentClassFile = this.classFile;
/* 1918 */         this.sym.defaultValue = deproxy(this.sym.type.getReturnType(), this.value);
/*      */ 
/* 1920 */         ClassReader.this.currentClassFile = localJavaFileObject; } finally { ClassReader.this.currentClassFile = localJavaFileObject; }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   class AnnotationDeproxy
/*      */     implements ClassReader.ProxyVisitor
/*      */   {
/* 1740 */     private Symbol.ClassSymbol requestingOwner = ClassReader.this.currentOwner.kind == 16 ? ClassReader.this.currentOwner
/* 1741 */       .enclClass() : (Symbol.ClassSymbol)ClassReader.this.currentOwner;
/*      */     Attribute result;
/*      */     Type type;
/*      */ 
/*      */     AnnotationDeproxy()
/*      */     {
/*      */     }
/*      */ 
/*      */     List<Attribute.Compound> deproxyCompoundList(List<ClassReader.CompoundAnnotationProxy> paramList)
/*      */     {
/* 1745 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1747 */       for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 1748 */         localListBuffer.append(deproxyCompound((ClassReader.CompoundAnnotationProxy)((List)localObject).head));
/*      */       }
/* 1750 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     Attribute.Compound deproxyCompound(ClassReader.CompoundAnnotationProxy paramCompoundAnnotationProxy) {
/* 1754 */       ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1756 */       for (List localList = paramCompoundAnnotationProxy.values; 
/* 1757 */         localList.nonEmpty(); 
/* 1758 */         localList = localList.tail) {
/* 1759 */         Symbol.MethodSymbol localMethodSymbol = findAccessMethod(paramCompoundAnnotationProxy.type, (Name)((Pair)localList.head).fst);
/* 1760 */         localListBuffer.append(new Pair(localMethodSymbol, 
/* 1761 */           deproxy(localMethodSymbol.type
/* 1761 */           .getReturnType(), (Attribute)((Pair)localList.head).snd)));
/*      */       }
/* 1763 */       return new Attribute.Compound(paramCompoundAnnotationProxy.type, localListBuffer.toList());
/*      */     }
/*      */ 
/*      */     Symbol.MethodSymbol findAccessMethod(Type paramType, Name paramName) {
/* 1767 */       Object localObject1 = null;
/*      */       try {
/* 1769 */         for (Scope.Entry localEntry = paramType.tsym.members().lookup(paramName); 
/* 1770 */           localEntry.scope != null; 
/* 1771 */           localEntry = localEntry.next()) {
/* 1772 */           localObject2 = localEntry.sym;
/* 1773 */           if ((((Symbol)localObject2).kind == 16) && (((Symbol)localObject2).type.getParameterTypes().length() == 0))
/* 1774 */             return (Symbol.MethodSymbol)localObject2;
/*      */         }
/*      */       } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1777 */         localObject1 = localCompletionFailure;
/*      */       }
/*      */ 
/* 1780 */       JavaFileObject localJavaFileObject = ClassReader.this.log.useSource(this.requestingOwner.classfile);
/*      */       try {
/* 1782 */         if (ClassReader.this.lintClassfile)
/* 1783 */           if (localObject1 == null) {
/* 1784 */             ClassReader.this.log.warning("annotation.method.not.found", new Object[] { paramType, paramName });
/*      */           }
/*      */           else
/*      */           {
/* 1788 */             ClassReader.this.log.warning("annotation.method.not.found.reason", new Object[] { paramType, paramName, localObject1
/* 1791 */               .getDetailValue() });
/*      */           }
/*      */       }
/*      */       finally {
/* 1795 */         ClassReader.this.log.useSource(localJavaFileObject);
/*      */       }
/*      */ 
/* 1803 */       Object localObject2 = new Type.MethodType(List.nil(), ClassReader.this.syms.botType, 
/* 1803 */         List.nil(), ClassReader.this.syms.methodClass);
/*      */ 
/* 1805 */       return new Symbol.MethodSymbol(1025L, paramName, (Type)localObject2, paramType.tsym);
/*      */     }
/*      */ 
/*      */     Attribute deproxy(Type paramType, Attribute paramAttribute)
/*      */     {
/* 1811 */       Type localType = this.type;
/*      */       try {
/* 1813 */         this.type = paramType;
/* 1814 */         paramAttribute.accept(this);
/* 1815 */         return this.result;
/*      */       } finally {
/* 1817 */         this.type = localType;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitConstant(Attribute.Constant paramConstant)
/*      */     {
/* 1825 */       this.result = paramConstant;
/*      */     }
/*      */ 
/*      */     public void visitClass(Attribute.Class paramClass) {
/* 1829 */       this.result = paramClass;
/*      */     }
/*      */ 
/*      */     public void visitEnum(Attribute.Enum paramEnum) {
/* 1833 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     public void visitCompound(Attribute.Compound paramCompound) {
/* 1837 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     public void visitArray(Attribute.Array paramArray) {
/* 1841 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     public void visitError(Attribute.Error paramError) {
/* 1845 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     public void visitEnumAttributeProxy(ClassReader.EnumAttributeProxy paramEnumAttributeProxy)
/*      */     {
/* 1850 */       Symbol.TypeSymbol localTypeSymbol = paramEnumAttributeProxy.enumType.tsym;
/* 1851 */       Symbol.VarSymbol localVarSymbol = null;
/* 1852 */       Object localObject = null;
/*      */       try {
/* 1854 */         for (Scope.Entry localEntry = localTypeSymbol.members().lookup(paramEnumAttributeProxy.enumerator); 
/* 1855 */           localEntry.scope != null; 
/* 1856 */           localEntry = localEntry.next())
/* 1857 */           if (localEntry.sym.kind == 4) {
/* 1858 */             localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/* 1859 */             break;
/*      */           }
/*      */       }
/*      */       catch (Symbol.CompletionFailure localCompletionFailure)
/*      */       {
/* 1864 */         localObject = localCompletionFailure;
/*      */       }
/* 1866 */       if (localVarSymbol == null) {
/* 1867 */         if (localObject != null)
/* 1868 */           ClassReader.this.log.warning("unknown.enum.constant.reason", new Object[] { ClassReader.this.currentClassFile, localTypeSymbol, paramEnumAttributeProxy.enumerator, localObject
/* 1870 */             .getDiagnostic() });
/*      */         else {
/* 1872 */           ClassReader.this.log.warning("unknown.enum.constant", new Object[] { ClassReader.this.currentClassFile, localTypeSymbol, paramEnumAttributeProxy.enumerator });
/*      */         }
/*      */ 
/* 1875 */         this.result = new Attribute.Enum(localTypeSymbol.type, new Symbol.VarSymbol(0L, paramEnumAttributeProxy.enumerator, ClassReader.this.syms.botType, localTypeSymbol));
/*      */       }
/*      */       else {
/* 1878 */         this.result = new Attribute.Enum(localTypeSymbol.type, localVarSymbol);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitArrayAttributeProxy(ClassReader.ArrayAttributeProxy paramArrayAttributeProxy) {
/* 1883 */       int i = paramArrayAttributeProxy.values.length();
/* 1884 */       Attribute[] arrayOfAttribute = new Attribute[i];
/* 1885 */       Type localType = ClassReader.this.types.elemtype(this.type);
/* 1886 */       int j = 0;
/* 1887 */       for (List localList = paramArrayAttributeProxy.values; localList.nonEmpty(); localList = localList.tail) {
/* 1888 */         arrayOfAttribute[(j++)] = deproxy(localType, (Attribute)localList.head);
/*      */       }
/* 1890 */       this.result = new Attribute.Array(this.type, arrayOfAttribute);
/*      */     }
/*      */ 
/*      */     public void visitCompoundAnnotationProxy(ClassReader.CompoundAnnotationProxy paramCompoundAnnotationProxy) {
/* 1894 */       this.result = deproxyCompound(paramCompoundAnnotationProxy);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ArrayAttributeProxy extends Attribute
/*      */   {
/*      */     List<Attribute> values;
/*      */ 
/*      */     ArrayAttributeProxy(List<Attribute> paramList)
/*      */     {
/* 1686 */       super();
/* 1687 */       this.values = paramList;
/*      */     }
/* 1689 */     public void accept(Attribute.Visitor paramVisitor) { ((ClassReader.ProxyVisitor)paramVisitor).visitArrayAttributeProxy(this); }
/*      */ 
/*      */     public String toString() {
/* 1692 */       return "{" + this.values + "}";
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static enum AttributeKind
/*      */   {
/*  956 */     CLASS, MEMBER; } 
/*      */   protected abstract class AttributeReader { protected final Name name;
/*      */     protected final ClassFile.Version version;
/*      */     protected final Set<ClassReader.AttributeKind> kinds;
/*      */ 
/*  959 */     protected AttributeReader(ClassFile.Version paramSet, Set<ClassReader.AttributeKind> arg3) { this.name = paramSet;
/*      */       Object localObject1;
/*  960 */       this.version = localObject1;
/*      */       Object localObject2;
/*  961 */       this.kinds = localObject2; }
/*      */ 
/*      */     protected boolean accepts(ClassReader.AttributeKind paramAttributeKind)
/*      */     {
/*  965 */       if (this.kinds.contains(paramAttributeKind)) {
/*  966 */         if ((ClassReader.this.majorVersion > this.version.major) || ((ClassReader.this.majorVersion == this.version.major) && (ClassReader.this.minorVersion >= this.version.minor))) {
/*  967 */           return true;
/*      */         }
/*  969 */         if ((ClassReader.this.lintClassfile) && (!ClassReader.this.warnedAttrs.contains(this.name))) {
/*  970 */           JavaFileObject localJavaFileObject = ClassReader.this.log.useSource(ClassReader.this.currentClassFile);
/*      */           try {
/*  972 */             ClassReader.this.log.warning(Lint.LintCategory.CLASSFILE, (JCDiagnostic.DiagnosticPosition)null, "future.attr", new Object[] { this.name, 
/*  973 */               Integer.valueOf(this.version.major), 
/*  973 */               Integer.valueOf(this.version.minor), Integer.valueOf(ClassReader.this.majorVersion), Integer.valueOf(ClassReader.this.minorVersion) });
/*      */ 
/*  975 */             ClassReader.this.log.useSource(localJavaFileObject); } finally { ClassReader.this.log.useSource(localJavaFileObject); }
/*      */ 
/*      */         }
/*      */       }
/*      */ 
/*  980 */       return false;
/*      */     }
/*      */ 
/*      */     protected abstract void read(Symbol paramSymbol, int paramInt);
/*      */   }
/*      */ 
/*      */   public class BadClassFile extends Symbol.CompletionFailure
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     public BadClassFile(Symbol.TypeSymbol paramJavaFileObject, JavaFileObject paramJCDiagnostic, JCDiagnostic arg4)
/*      */     {
/*  343 */       super(ClassReader.this.createBadClassFileDiagnostic(paramJCDiagnostic, localJCDiagnostic));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CompoundAnnotationProxy extends Attribute
/*      */   {
/*      */     final List<Pair<Name, Attribute>> values;
/*      */ 
/*      */     public CompoundAnnotationProxy(Type paramType, List<Pair<Name, Attribute>> paramList)
/*      */     {
/* 1702 */       super();
/* 1703 */       this.values = paramList;
/*      */     }
/* 1705 */     public void accept(Attribute.Visitor paramVisitor) { ((ClassReader.ProxyVisitor)paramVisitor).visitCompoundAnnotationProxy(this); }
/*      */ 
/*      */     public String toString() {
/* 1708 */       StringBuilder localStringBuilder = new StringBuilder();
/* 1709 */       localStringBuilder.append("@");
/* 1710 */       localStringBuilder.append(this.type.tsym.getQualifiedName());
/* 1711 */       localStringBuilder.append("/*proxy*/{");
/* 1712 */       int i = 1;
/* 1713 */       for (List localList = this.values; 
/* 1714 */         localList.nonEmpty(); localList = localList.tail) {
/* 1715 */         Pair localPair = (Pair)localList.head;
/* 1716 */         if (i == 0) localStringBuilder.append(",");
/* 1717 */         i = 0;
/* 1718 */         localStringBuilder.append((CharSequence)localPair.fst);
/* 1719 */         localStringBuilder.append("=");
/* 1720 */         localStringBuilder.append(localPair.snd);
/*      */       }
/* 1722 */       localStringBuilder.append("}");
/* 1723 */       return localStringBuilder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EnumAttributeProxy extends Attribute
/*      */   {
/*      */     Type enumType;
/*      */     Name enumerator;
/*      */ 
/*      */     public EnumAttributeProxy(Type paramType, Name paramName)
/*      */     {
/* 1672 */       super();
/* 1673 */       this.enumType = paramType;
/* 1674 */       this.enumerator = paramName;
/*      */     }
/* 1676 */     public void accept(Attribute.Visitor paramVisitor) { ((ClassReader.ProxyVisitor)paramVisitor).visitEnumAttributeProxy(this); }
/*      */ 
/*      */     public String toString() {
/* 1679 */       return "/*proxy enum*/" + this.enumType + "." + this.enumerator;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface ProxyVisitor extends Attribute.Visitor
/*      */   {
/*      */     public abstract void visitEnumAttributeProxy(ClassReader.EnumAttributeProxy paramEnumAttributeProxy);
/*      */ 
/*      */     public abstract void visitArrayAttributeProxy(ClassReader.ArrayAttributeProxy paramArrayAttributeProxy);
/*      */ 
/*      */     public abstract void visitCompoundAnnotationProxy(ClassReader.CompoundAnnotationProxy paramCompoundAnnotationProxy);
/*      */   }
/*      */ 
/*      */   public static abstract interface SourceCompleter
/*      */   {
/*      */     public abstract void complete(Symbol.ClassSymbol paramClassSymbol)
/*      */       throws Symbol.CompletionFailure;
/*      */   }
/*      */ 
/*      */   private static class SourceFileObject extends BaseFileObject
/*      */   {
/*      */     private Name name;
/*      */     private Name flatname;
/*      */ 
/*      */     public SourceFileObject(Name paramName1, Name paramName2)
/*      */     {
/* 2868 */       super();
/* 2869 */       this.name = paramName1;
/* 2870 */       this.flatname = paramName2;
/*      */     }
/*      */ 
/*      */     public URI toUri()
/*      */     {
/*      */       try {
/* 2876 */         return new URI(null, this.name.toString(), null);
/*      */       } catch (URISyntaxException localURISyntaxException) {
/* 2878 */         throw new BaseFileObject.CannotCreateUriError(this.name.toString(), localURISyntaxException);
/*      */       }
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2884 */       return this.name.toString();
/*      */     }
/*      */ 
/*      */     public String getShortName()
/*      */     {
/* 2889 */       return getName();
/*      */     }
/*      */ 
/*      */     public JavaFileObject.Kind getKind()
/*      */     {
/* 2894 */       return getKind(getName());
/*      */     }
/*      */ 
/*      */     public InputStream openInputStream()
/*      */     {
/* 2899 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public OutputStream openOutputStream()
/*      */     {
/* 2904 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public CharBuffer getCharContent(boolean paramBoolean)
/*      */     {
/* 2909 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public Reader openReader(boolean paramBoolean)
/*      */     {
/* 2914 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public Writer openWriter()
/*      */     {
/* 2919 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public long getLastModified()
/*      */     {
/* 2924 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public boolean delete()
/*      */     {
/* 2929 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     protected String inferBinaryName(Iterable<? extends File> paramIterable)
/*      */     {
/* 2934 */       return this.flatname.toString();
/*      */     }
/*      */ 
/*      */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*      */     {
/* 2939 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 2950 */       if (this == paramObject) {
/* 2951 */         return true;
/*      */       }
/* 2953 */       if (!(paramObject instanceof SourceFileObject)) {
/* 2954 */         return false;
/*      */       }
/* 2956 */       SourceFileObject localSourceFileObject = (SourceFileObject)paramObject;
/* 2957 */       return this.name.equals(localSourceFileObject.name);
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2962 */       return this.name.hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */   class TypeAnnotationCompleter extends ClassReader.AnnotationCompleter
/*      */   {
/*      */     List<ClassReader.TypeAnnotationProxy> proxies;
/*      */ 
/*      */     TypeAnnotationCompleter(List<ClassReader.TypeAnnotationProxy> arg2)
/*      */     {
/* 1961 */       super(localSymbol, List.nil());
/*      */       Object localObject;
/* 1962 */       this.proxies = localObject;
/*      */     }
/*      */ 
/*      */     List<Attribute.TypeCompound> deproxyTypeCompoundList(List<ClassReader.TypeAnnotationProxy> paramList) {
/* 1966 */       ListBuffer localListBuffer = new ListBuffer();
/* 1967 */       for (ClassReader.TypeAnnotationProxy localTypeAnnotationProxy : paramList) {
/* 1968 */         Attribute.Compound localCompound = deproxyCompound(localTypeAnnotationProxy.compound);
/* 1969 */         Attribute.TypeCompound localTypeCompound = new Attribute.TypeCompound(localCompound, localTypeAnnotationProxy.position);
/* 1970 */         localListBuffer.add(localTypeCompound);
/*      */       }
/* 1972 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1977 */       JavaFileObject localJavaFileObject = ClassReader.this.currentClassFile;
/*      */       try {
/* 1979 */         ClassReader.this.currentClassFile = this.classFile;
/* 1980 */         List localList = deproxyTypeCompoundList(this.proxies);
/* 1981 */         this.sym.setTypeAttributes(localList.prependList(this.sym.getRawTypeAttributes()));
/*      */ 
/* 1983 */         ClassReader.this.currentClassFile = localJavaFileObject; } finally { ClassReader.this.currentClassFile = localJavaFileObject; }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TypeAnnotationProxy
/*      */   {
/*      */     final ClassReader.CompoundAnnotationProxy compound;
/*      */     final TypeAnnotationPosition position;
/*      */ 
/*      */     public TypeAnnotationProxy(ClassReader.CompoundAnnotationProxy paramCompoundAnnotationProxy, TypeAnnotationPosition paramTypeAnnotationPosition)
/*      */     {
/* 1734 */       this.compound = paramCompoundAnnotationProxy;
/* 1735 */       this.position = paramTypeAnnotationPosition;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.ClassReader
 * JD-Core Version:    0.6.2
 */