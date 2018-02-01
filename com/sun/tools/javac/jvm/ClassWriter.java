/*      */ package com.sun.tools.javac.jvm;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Attribute.Array;
/*      */ import com.sun.tools.javac.code.Attribute.Class;
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.Constant;
/*      */ import com.sun.tools.javac.code.Attribute.Enum;
/*      */ import com.sun.tools.javac.code.Attribute.Error;
/*      */ import com.sun.tools.javac.code.Attribute.RetentionPolicy;
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.Attribute.Visitor;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.DelegatedSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.DynamicMethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.TargetType;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.SignatureGenerator;
/*      */ import com.sun.tools.javac.code.Types.UniqueType;
/*      */ import com.sun.tools.javac.file.BaseFileObject;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.ByteBuffer;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.WriterKind;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.tools.FileObject;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.JavaFileObject.Kind;
/*      */ import javax.tools.StandardLocation;
/*      */ 
/*      */ public class ClassWriter extends ClassFile
/*      */ {
/*   66 */   protected static final Context.Key<ClassWriter> classWriterKey = new Context.Key();
/*      */   private final Options options;
/*      */   private boolean verbose;
/*      */   private boolean scramble;
/*      */   private boolean scrambleAll;
/*      */   private boolean retrofit;
/*      */   private boolean emitSourceFile;
/*      */   private boolean genCrt;
/*      */   boolean debugstackmap;
/*      */   private Target target;
/*      */   private Source source;
/*      */   private Types types;
/*      */   static final int DATA_BUF_SIZE = 65520;
/*      */   static final int POOL_BUF_SIZE = 131056;
/*  120 */   ByteBuffer databuf = new ByteBuffer(65520);
/*      */ 
/*  124 */   ByteBuffer poolbuf = new ByteBuffer(131056);
/*      */   Pool pool;
/*      */   Set<Symbol.ClassSymbol> innerClasses;
/*      */   ListBuffer<Symbol.ClassSymbol> innerClassesQueue;
/*      */   Map<Pool.DynamicMethod, Pool.MethodHandle> bootstrapMethods;
/*      */   private final Log log;
/*      */   private final Names names;
/*      */   private final JavaFileManager fileManager;
/*      */   private final CWSignatureGenerator signatureGen;
/*      */   static final int SAME_FRAME_SIZE = 64;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_EXTENDED = 247;
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */   static final int FULL_FRAME = 255;
/*      */   static final int MAX_LOCAL_LENGTH_DIFF = 4;
/*      */   private final boolean dumpClassModifiers;
/*      */   private final boolean dumpFieldModifiers;
/*      */   private final boolean dumpInnerClassModifiers;
/*      */   private final boolean dumpMethodModifiers;
/*  243 */   private static final String[] flagName = { "PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "SUPER", "VOLATILE", "TRANSIENT", "NATIVE", "INTERFACE", "ABSTRACT", "STRICTFP" };
/*      */ 
/*  887 */   AttributeWriter awriter = new AttributeWriter();
/*      */ 
/*      */   public static ClassWriter instance(Context paramContext)
/*      */   {
/*  166 */     ClassWriter localClassWriter = (ClassWriter)paramContext.get(classWriterKey);
/*  167 */     if (localClassWriter == null)
/*  168 */       localClassWriter = new ClassWriter(paramContext);
/*  169 */     return localClassWriter;
/*      */   }
/*      */ 
/*      */   protected ClassWriter(Context paramContext)
/*      */   {
/*  175 */     paramContext.put(classWriterKey, this);
/*      */ 
/*  177 */     this.log = Log.instance(paramContext);
/*  178 */     this.names = Names.instance(paramContext);
/*  179 */     this.options = Options.instance(paramContext);
/*  180 */     this.target = Target.instance(paramContext);
/*  181 */     this.source = Source.instance(paramContext);
/*  182 */     this.types = Types.instance(paramContext);
/*  183 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*  184 */     this.signatureGen = new CWSignatureGenerator(this.types);
/*      */ 
/*  186 */     this.verbose = this.options.isSet(Option.VERBOSE);
/*  187 */     this.scramble = this.options.isSet("-scramble");
/*  188 */     this.scrambleAll = this.options.isSet("-scrambleAll");
/*  189 */     this.retrofit = this.options.isSet("-retrofit");
/*  190 */     this.genCrt = this.options.isSet(Option.XJCOV);
/*  191 */     this.debugstackmap = this.options.isSet("debugstackmap");
/*      */ 
/*  193 */     this.emitSourceFile = ((this.options.isUnset(Option.G_CUSTOM)) || 
/*  194 */       (this.options
/*  194 */       .isSet(Option.G_CUSTOM, "source")));
/*      */ 
/*  196 */     String str = this.options.get("dumpmodifiers");
/*  197 */     this.dumpClassModifiers = ((str != null) && 
/*  198 */       (str
/*  198 */       .indexOf('c') != 
/*  198 */       -1));
/*  199 */     this.dumpFieldModifiers = ((str != null) && 
/*  200 */       (str
/*  200 */       .indexOf('f') != 
/*  200 */       -1));
/*  201 */     this.dumpInnerClassModifiers = ((str != null) && 
/*  202 */       (str
/*  202 */       .indexOf('i') != 
/*  202 */       -1));
/*  203 */     this.dumpMethodModifiers = ((str != null) && 
/*  204 */       (str
/*  204 */       .indexOf('m') != 
/*  204 */       -1));
/*      */   }
/*      */ 
/*      */   public static String flagNames(long paramLong)
/*      */   {
/*  229 */     StringBuilder localStringBuilder = new StringBuilder();
/*  230 */     int i = 0;
/*  231 */     long l = paramLong & 0xFFF;
/*  232 */     while (l != 0L) {
/*  233 */       if ((l & 1L) != 0L) {
/*  234 */         localStringBuilder.append(" ");
/*  235 */         localStringBuilder.append(flagName[i]);
/*      */       }
/*  237 */       l >>= 1;
/*  238 */       i++;
/*      */     }
/*  240 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   void putChar(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
/*      */   {
/*  256 */     paramByteBuffer.elems[paramInt1] = ((byte)(paramInt2 >> 8 & 0xFF));
/*  257 */     paramByteBuffer.elems[(paramInt1 + 1)] = ((byte)(paramInt2 & 0xFF));
/*      */   }
/*      */ 
/*      */   void putInt(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
/*      */   {
/*  264 */     paramByteBuffer.elems[paramInt1] = ((byte)(paramInt2 >> 24 & 0xFF));
/*  265 */     paramByteBuffer.elems[(paramInt1 + 1)] = ((byte)(paramInt2 >> 16 & 0xFF));
/*  266 */     paramByteBuffer.elems[(paramInt1 + 2)] = ((byte)(paramInt2 >> 8 & 0xFF));
/*  267 */     paramByteBuffer.elems[(paramInt1 + 3)] = ((byte)(paramInt2 & 0xFF));
/*      */   }
/*      */ 
/*      */   Name typeSig(Type paramType)
/*      */   {
/*  340 */     Assert.check(this.signatureGen.isEmpty());
/*      */ 
/*  342 */     this.signatureGen.assembleSig(paramType);
/*  343 */     Name localName = this.signatureGen.toName();
/*  344 */     this.signatureGen.reset();
/*      */ 
/*  346 */     return localName;
/*      */   }
/*      */ 
/*      */   public Name xClassName(Type paramType)
/*      */   {
/*  353 */     if (paramType.hasTag(TypeTag.CLASS))
/*  354 */       return this.names.fromUtf(externalize(paramType.tsym.flatName()));
/*  355 */     if (paramType.hasTag(TypeTag.ARRAY)) {
/*  356 */       return typeSig(this.types.erasure(paramType));
/*      */     }
/*  358 */     throw new AssertionError("xClassName");
/*      */   }
/*      */ 
/*      */   void writePool(Pool paramPool)
/*      */     throws ClassWriter.PoolOverflow, ClassWriter.StringOverflow
/*      */   {
/*  385 */     int i = this.poolbuf.length;
/*  386 */     this.poolbuf.appendChar(0);
/*  387 */     int j = 1;
/*  388 */     while (j < paramPool.pp) {
/*  389 */       Object localObject1 = paramPool.pool[j];
/*  390 */       Assert.checkNonNull(localObject1);
/*  391 */       if (((localObject1 instanceof Pool.Method)) || ((localObject1 instanceof Pool.Variable)))
/*  392 */         localObject1 = ((Symbol.DelegatedSymbol)localObject1).getUnderlyingSymbol();
/*      */       Object localObject2;
/*  394 */       if ((localObject1 instanceof Symbol.MethodSymbol)) {
/*  395 */         localObject2 = (Symbol.MethodSymbol)localObject1;
/*  396 */         if (!((Symbol.MethodSymbol)localObject2).isDynamic()) {
/*  397 */           this.poolbuf.appendByte((((Symbol.MethodSymbol)localObject2).owner.flags() & 0x200) != 0L ? 11 : 10);
/*      */ 
/*  400 */           this.poolbuf.appendChar(paramPool.put(((Symbol.MethodSymbol)localObject2).owner));
/*  401 */           this.poolbuf.appendChar(paramPool.put(nameType((Symbol)localObject2)));
/*      */         }
/*      */         else {
/*  404 */           Symbol.DynamicMethodSymbol localDynamicMethodSymbol = (Symbol.DynamicMethodSymbol)localObject2;
/*  405 */           Pool.MethodHandle localMethodHandle = new Pool.MethodHandle(localDynamicMethodSymbol.bsmKind, localDynamicMethodSymbol.bsm, this.types);
/*  406 */           Pool.DynamicMethod localDynamicMethod = new Pool.DynamicMethod(localDynamicMethodSymbol, this.types);
/*  407 */           this.bootstrapMethods.put(localDynamicMethod, localMethodHandle);
/*      */ 
/*  409 */           paramPool.put(this.names.BootstrapMethods);
/*  410 */           paramPool.put(localMethodHandle);
/*  411 */           for (Object localObject3 : localDynamicMethodSymbol.staticArgs) {
/*  412 */             paramPool.put(localObject3);
/*      */           }
/*  414 */           this.poolbuf.appendByte(18);
/*  415 */           this.poolbuf.appendChar(this.bootstrapMethods.size() - 1);
/*  416 */           this.poolbuf.appendChar(paramPool.put(nameType(localDynamicMethodSymbol)));
/*      */         }
/*  418 */       } else if ((localObject1 instanceof Symbol.VarSymbol)) {
/*  419 */         localObject2 = (Symbol.VarSymbol)localObject1;
/*  420 */         this.poolbuf.appendByte(9);
/*  421 */         this.poolbuf.appendChar(paramPool.put(((Symbol.VarSymbol)localObject2).owner));
/*  422 */         this.poolbuf.appendChar(paramPool.put(nameType((Symbol)localObject2)));
/*  423 */       } else if ((localObject1 instanceof Name)) {
/*  424 */         this.poolbuf.appendByte(1);
/*  425 */         localObject2 = ((Name)localObject1).toUtf();
/*  426 */         this.poolbuf.appendChar(localObject2.length);
/*  427 */         this.poolbuf.appendBytes((byte[])localObject2, 0, localObject2.length);
/*  428 */         if (localObject2.length > 65535)
/*  429 */           throw new StringOverflow(localObject1.toString());
/*  430 */       } else if ((localObject1 instanceof Symbol.ClassSymbol)) {
/*  431 */         localObject2 = (Symbol.ClassSymbol)localObject1;
/*  432 */         if (((Symbol.ClassSymbol)localObject2).owner.kind == 2) paramPool.put(((Symbol.ClassSymbol)localObject2).owner);
/*  433 */         this.poolbuf.appendByte(7);
/*  434 */         if (((Symbol.ClassSymbol)localObject2).type.hasTag(TypeTag.ARRAY)) {
/*  435 */           this.poolbuf.appendChar(paramPool.put(typeSig(((Symbol.ClassSymbol)localObject2).type)));
/*      */         } else {
/*  437 */           this.poolbuf.appendChar(paramPool.put(this.names.fromUtf(externalize(((Symbol.ClassSymbol)localObject2).flatname))));
/*  438 */           enterInner((Symbol.ClassSymbol)localObject2);
/*      */         }
/*  440 */       } else if ((localObject1 instanceof ClassFile.NameAndType)) {
/*  441 */         localObject2 = (ClassFile.NameAndType)localObject1;
/*  442 */         this.poolbuf.appendByte(12);
/*  443 */         this.poolbuf.appendChar(paramPool.put(((ClassFile.NameAndType)localObject2).name));
/*  444 */         this.poolbuf.appendChar(paramPool.put(typeSig(((ClassFile.NameAndType)localObject2).uniqueType.type)));
/*  445 */       } else if ((localObject1 instanceof Integer)) {
/*  446 */         this.poolbuf.appendByte(3);
/*  447 */         this.poolbuf.appendInt(((Integer)localObject1).intValue());
/*  448 */       } else if ((localObject1 instanceof Long)) {
/*  449 */         this.poolbuf.appendByte(5);
/*  450 */         this.poolbuf.appendLong(((Long)localObject1).longValue());
/*  451 */         j++;
/*  452 */       } else if ((localObject1 instanceof Float)) {
/*  453 */         this.poolbuf.appendByte(4);
/*  454 */         this.poolbuf.appendFloat(((Float)localObject1).floatValue());
/*  455 */       } else if ((localObject1 instanceof Double)) {
/*  456 */         this.poolbuf.appendByte(6);
/*  457 */         this.poolbuf.appendDouble(((Double)localObject1).doubleValue());
/*  458 */         j++;
/*  459 */       } else if ((localObject1 instanceof String)) {
/*  460 */         this.poolbuf.appendByte(8);
/*  461 */         this.poolbuf.appendChar(paramPool.put(this.names.fromString((String)localObject1)));
/*  462 */       } else if ((localObject1 instanceof Types.UniqueType)) {
/*  463 */         localObject2 = ((Types.UniqueType)localObject1).type;
/*  464 */         if ((localObject2 instanceof Type.MethodType)) {
/*  465 */           this.poolbuf.appendByte(16);
/*  466 */           this.poolbuf.appendChar(paramPool.put(typeSig((Type.MethodType)localObject2)));
/*      */         } else {
/*  468 */           if (((Type)localObject2).hasTag(TypeTag.CLASS)) enterInner((Symbol.ClassSymbol)((Type)localObject2).tsym);
/*  469 */           this.poolbuf.appendByte(7);
/*  470 */           this.poolbuf.appendChar(paramPool.put(xClassName((Type)localObject2)));
/*      */         }
/*  472 */       } else if ((localObject1 instanceof Pool.MethodHandle)) {
/*  473 */         localObject2 = (Pool.MethodHandle)localObject1;
/*  474 */         this.poolbuf.appendByte(15);
/*  475 */         this.poolbuf.appendByte(((Pool.MethodHandle)localObject2).refKind);
/*  476 */         this.poolbuf.appendChar(paramPool.put(((Pool.MethodHandle)localObject2).refSym));
/*      */       } else {
/*  478 */         Assert.error("writePool " + localObject1);
/*      */       }
/*  480 */       j++;
/*      */     }
/*  482 */     if (paramPool.pp > 65535)
/*  483 */       throw new PoolOverflow();
/*  484 */     putChar(this.poolbuf, i, paramPool.pp);
/*      */   }
/*      */ 
/*      */   Name fieldName(Symbol paramSymbol)
/*      */   {
/*  490 */     if (((this.scramble) && ((paramSymbol.flags() & 0x2) != 0L)) || ((this.scrambleAll) && 
/*  491 */       ((paramSymbol
/*  491 */       .flags() & 0x5) == 0L))) {
/*  492 */       return this.names.fromString("_$" + paramSymbol.name.getIndex());
/*      */     }
/*  494 */     return paramSymbol.name;
/*      */   }
/*      */ 
/*      */   ClassFile.NameAndType nameType(Symbol paramSymbol)
/*      */   {
/*  503 */     return new ClassFile.NameAndType(fieldName(paramSymbol), this.retrofit ? paramSymbol
/*  502 */       .erasure(this.types) : 
/*  502 */       paramSymbol
/*  503 */       .externalType(this.types), 
/*  503 */       this.types);
/*      */   }
/*      */ 
/*      */   int writeAttr(Name paramName)
/*      */   {
/*  518 */     this.databuf.appendChar(this.pool.put(paramName));
/*  519 */     this.databuf.appendInt(0);
/*  520 */     return this.databuf.length;
/*      */   }
/*      */ 
/*      */   void endAttr(int paramInt)
/*      */   {
/*  526 */     putInt(this.databuf, paramInt - 4, this.databuf.length - paramInt);
/*      */   }
/*      */ 
/*      */   int beginAttrs()
/*      */   {
/*  533 */     this.databuf.appendChar(0);
/*  534 */     return this.databuf.length;
/*      */   }
/*      */ 
/*      */   void endAttrs(int paramInt1, int paramInt2)
/*      */   {
/*  540 */     putChar(this.databuf, paramInt1 - 2, paramInt2);
/*      */   }
/*      */ 
/*      */   int writeEnclosingMethodAttribute(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  547 */     if (!this.target.hasEnclosingMethodAttribute())
/*  548 */       return 0;
/*  549 */     return writeEnclosingMethodAttribute(this.names.EnclosingMethod, paramClassSymbol);
/*      */   }
/*      */ 
/*      */   protected int writeEnclosingMethodAttribute(Name paramName, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  556 */     if ((paramClassSymbol.owner.kind != 16) && (paramClassSymbol.name != this.names.empty))
/*      */     {
/*  558 */       return 0;
/*      */     }
/*  560 */     int i = writeAttr(paramName);
/*  561 */     Symbol.ClassSymbol localClassSymbol = paramClassSymbol.owner.enclClass();
/*  562 */     Symbol.MethodSymbol localMethodSymbol = (paramClassSymbol.owner.type == null) || (paramClassSymbol.owner.kind != 16) ? null : (Symbol.MethodSymbol)paramClassSymbol.owner;
/*      */ 
/*  567 */     this.databuf.appendChar(this.pool.put(localClassSymbol));
/*  568 */     this.databuf.appendChar(localMethodSymbol == null ? 0 : this.pool.put(nameType(paramClassSymbol.owner)));
/*  569 */     endAttr(i);
/*  570 */     return 1;
/*      */   }
/*      */ 
/*      */   int writeFlagAttrs(long paramLong)
/*      */   {
/*  576 */     int i = 0;
/*      */     int j;
/*  577 */     if ((paramLong & 0x20000) != 0L) {
/*  578 */       j = writeAttr(this.names.Deprecated);
/*  579 */       endAttr(j);
/*  580 */       i++;
/*      */     }
/*  582 */     if (((paramLong & 0x4000) != 0L) && (!this.target.useEnumFlag())) {
/*  583 */       j = writeAttr(this.names.Enum);
/*  584 */       endAttr(j);
/*  585 */       i++;
/*      */     }
/*  587 */     if (((paramLong & 0x1000) != 0L) && (!this.target.useSyntheticFlag())) {
/*  588 */       j = writeAttr(this.names.Synthetic);
/*  589 */       endAttr(j);
/*  590 */       i++;
/*      */     }
/*  592 */     if (((paramLong & 0x80000000) != 0L) && (!this.target.useBridgeFlag())) {
/*  593 */       j = writeAttr(this.names.Bridge);
/*  594 */       endAttr(j);
/*  595 */       i++;
/*      */     }
/*  597 */     if (((paramLong & 0x0) != 0L) && (!this.target.useVarargsFlag())) {
/*  598 */       j = writeAttr(this.names.Varargs);
/*  599 */       endAttr(j);
/*  600 */       i++;
/*      */     }
/*  602 */     if (((paramLong & 0x2000) != 0L) && (!this.target.useAnnotationFlag())) {
/*  603 */       j = writeAttr(this.names.Annotation);
/*  604 */       endAttr(j);
/*  605 */       i++;
/*      */     }
/*  607 */     return i;
/*      */   }
/*      */ 
/*      */   int writeMemberAttrs(Symbol paramSymbol)
/*      */   {
/*  614 */     int i = writeFlagAttrs(paramSymbol.flags());
/*  615 */     long l = paramSymbol.flags();
/*  616 */     if ((this.source.allowGenerics()) && ((l & 0x80001000) != 4096L) && ((l & 0x20000000) == 0L))
/*      */     {
/*  619 */       if ((!this.types
/*  619 */         .isSameType(paramSymbol.type, paramSymbol
/*  619 */         .erasure(this.types))) || 
/*  620 */         (this.signatureGen
/*  620 */         .hasTypeVar(paramSymbol.type
/*  620 */         .getThrownTypes())))
/*      */       {
/*  623 */         int j = writeAttr(this.names.Signature);
/*  624 */         this.databuf.appendChar(this.pool.put(typeSig(paramSymbol.type)));
/*  625 */         endAttr(j);
/*  626 */         i++;
/*      */       }
/*      */     }
/*  628 */     i += writeJavaAnnotations(paramSymbol.getRawAttributes());
/*  629 */     i += writeTypeAnnotations(paramSymbol.getRawTypeAttributes(), false);
/*  630 */     return i;
/*      */   }
/*      */ 
/*      */   int writeMethodParametersAttr(Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/*  637 */     Type.MethodType localMethodType = paramMethodSymbol.externalType(this.types).asMethodType();
/*  638 */     int i = localMethodType.argtypes.size();
/*  639 */     if ((paramMethodSymbol.params != null) && (i != 0)) {
/*  640 */       int j = writeAttr(this.names.MethodParameters);
/*  641 */       this.databuf.appendByte(i);
/*      */ 
/*  643 */       for (Iterator localIterator = paramMethodSymbol.extraParams.iterator(); localIterator.hasNext(); ) { localVarSymbol = (Symbol.VarSymbol)localIterator.next();
/*      */ 
/*  646 */         k = (int)localVarSymbol
/*  645 */           .flags() & 0x9010 | 
/*  646 */           (int)paramMethodSymbol
/*  646 */           .flags() & 0x1000;
/*  647 */         this.databuf.appendChar(this.pool.put(localVarSymbol.name));
/*  648 */         this.databuf.appendChar(k);
/*      */       }
/*  651 */       Symbol.VarSymbol localVarSymbol;
/*      */       int k;
/*  651 */       for (localIterator = paramMethodSymbol.params.iterator(); localIterator.hasNext(); ) { localVarSymbol = (Symbol.VarSymbol)localIterator.next();
/*      */ 
/*  654 */         k = (int)localVarSymbol
/*  653 */           .flags() & 0x9010 | 
/*  654 */           (int)paramMethodSymbol
/*  654 */           .flags() & 0x1000;
/*  655 */         this.databuf.appendChar(this.pool.put(localVarSymbol.name));
/*  656 */         this.databuf.appendChar(k);
/*      */       }
/*      */ 
/*  659 */       for (localIterator = paramMethodSymbol.capturedLocals.iterator(); localIterator.hasNext(); ) { localVarSymbol = (Symbol.VarSymbol)localIterator.next();
/*      */ 
/*  662 */         k = (int)localVarSymbol
/*  661 */           .flags() & 0x9010 | 
/*  662 */           (int)paramMethodSymbol
/*  662 */           .flags() & 0x1000;
/*  663 */         this.databuf.appendChar(this.pool.put(localVarSymbol.name));
/*  664 */         this.databuf.appendChar(k);
/*      */       }
/*  666 */       endAttr(j);
/*  667 */       return 1;
/*      */     }
/*  669 */     return 0;
/*      */   }
/*      */ 
/*      */   int writeParameterAttrs(Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/*  677 */     int i = 0;
/*  678 */     int j = 0;
/*  679 */     if (paramMethodSymbol.params != null)
/*  680 */       for (Symbol.VarSymbol localVarSymbol : paramMethodSymbol.params)
/*  681 */         for (localIterator2 = localVarSymbol.getRawAttributes().iterator(); localIterator2.hasNext(); ) { localObject = (Attribute.Compound)localIterator2.next();
/*  682 */           switch (1.$SwitchMap$com$sun$tools$javac$code$Attribute$RetentionPolicy[this.types.getRetention(localObject).ordinal()]) { case 1:
/*  683 */             break;
/*      */           case 2:
/*  684 */             j = 1; break;
/*      */           case 3:
/*  685 */             i = 1;
/*      */           }
/*      */         }
/*      */     Iterator localIterator2;
/*      */     Object localObject;
/*  692 */     int k = 0;
/*      */     int m;
/*      */     ListBuffer localListBuffer;
/*      */     Iterator localIterator3;
/*      */     Attribute.Compound localCompound;
/*  693 */     if (i != 0) {
/*  694 */       m = writeAttr(this.names.RuntimeVisibleParameterAnnotations);
/*  695 */       this.databuf.appendByte(paramMethodSymbol.params.length());
/*  696 */       for (localIterator2 = paramMethodSymbol.params.iterator(); localIterator2.hasNext(); ) { localObject = (Symbol.VarSymbol)localIterator2.next();
/*  697 */         localListBuffer = new ListBuffer();
/*  698 */         for (localIterator3 = ((Symbol.VarSymbol)localObject).getRawAttributes().iterator(); localIterator3.hasNext(); ) { localCompound = (Attribute.Compound)localIterator3.next();
/*  699 */           if (this.types.getRetention(localCompound) == Attribute.RetentionPolicy.RUNTIME)
/*  700 */             localListBuffer.append(localCompound); }
/*  701 */         this.databuf.appendChar(localListBuffer.length());
/*  702 */         for (localIterator3 = localListBuffer.iterator(); localIterator3.hasNext(); ) { localCompound = (Attribute.Compound)localIterator3.next();
/*  703 */           writeCompoundAttribute(localCompound); }
/*      */       }
/*  705 */       endAttr(m);
/*  706 */       k++;
/*      */     }
/*  708 */     if (j != 0) {
/*  709 */       m = writeAttr(this.names.RuntimeInvisibleParameterAnnotations);
/*  710 */       this.databuf.appendByte(paramMethodSymbol.params.length());
/*  711 */       for (localIterator2 = paramMethodSymbol.params.iterator(); localIterator2.hasNext(); ) { localObject = (Symbol.VarSymbol)localIterator2.next();
/*  712 */         localListBuffer = new ListBuffer();
/*  713 */         for (localIterator3 = ((Symbol.VarSymbol)localObject).getRawAttributes().iterator(); localIterator3.hasNext(); ) { localCompound = (Attribute.Compound)localIterator3.next();
/*  714 */           if (this.types.getRetention(localCompound) == Attribute.RetentionPolicy.CLASS)
/*  715 */             localListBuffer.append(localCompound); }
/*  716 */         this.databuf.appendChar(localListBuffer.length());
/*  717 */         for (localIterator3 = localListBuffer.iterator(); localIterator3.hasNext(); ) { localCompound = (Attribute.Compound)localIterator3.next();
/*  718 */           writeCompoundAttribute(localCompound); }
/*      */       }
/*  720 */       endAttr(m);
/*  721 */       k++;
/*      */     }
/*  723 */     return k;
/*      */   }
/*      */ 
/*      */   int writeJavaAnnotations(com.sun.tools.javac.util.List<Attribute.Compound> paramList)
/*      */   {
/*  734 */     if (paramList.isEmpty()) return 0;
/*  735 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  736 */     ListBuffer localListBuffer2 = new ListBuffer();
/*  737 */     for (Attribute.Compound localCompound1 : paramList) {
/*  738 */       switch (1.$SwitchMap$com$sun$tools$javac$code$Attribute$RetentionPolicy[this.types.getRetention(localCompound1).ordinal()]) { case 1:
/*  739 */         break;
/*      */       case 2:
/*  740 */         localListBuffer2.append(localCompound1); break;
/*      */       case 3:
/*  741 */         localListBuffer1.append(localCompound1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  746 */     int i = 0;
/*      */     int j;
/*      */     Iterator localIterator2;
/*      */     Attribute.Compound localCompound2;
/*  747 */     if (localListBuffer1.length() != 0) {
/*  748 */       j = writeAttr(this.names.RuntimeVisibleAnnotations);
/*  749 */       this.databuf.appendChar(localListBuffer1.length());
/*  750 */       for (localIterator2 = localListBuffer1.iterator(); localIterator2.hasNext(); ) { localCompound2 = (Attribute.Compound)localIterator2.next();
/*  751 */         writeCompoundAttribute(localCompound2); }
/*  752 */       endAttr(j);
/*  753 */       i++;
/*      */     }
/*  755 */     if (localListBuffer2.length() != 0) {
/*  756 */       j = writeAttr(this.names.RuntimeInvisibleAnnotations);
/*  757 */       this.databuf.appendChar(localListBuffer2.length());
/*  758 */       for (localIterator2 = localListBuffer2.iterator(); localIterator2.hasNext(); ) { localCompound2 = (Attribute.Compound)localIterator2.next();
/*  759 */         writeCompoundAttribute(localCompound2); }
/*  760 */       endAttr(j);
/*  761 */       i++;
/*      */     }
/*  763 */     return i;
/*      */   }
/*      */ 
/*      */   int writeTypeAnnotations(com.sun.tools.javac.util.List<Attribute.TypeCompound> paramList, boolean paramBoolean) {
/*  767 */     if (paramList.isEmpty()) return 0;
/*      */ 
/*  769 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  770 */     ListBuffer localListBuffer2 = new ListBuffer();
/*      */ 
/*  772 */     for (Attribute.TypeCompound localTypeCompound : paramList)
/*  773 */       if (localTypeCompound.hasUnknownPosition()) {
/*  774 */         boolean bool = localTypeCompound.tryFixPosition();
/*      */ 
/*  777 */         if (!bool)
/*      */         {
/*  782 */           localObject = this.log.getWriter(Log.WriterKind.ERROR);
/*  783 */           ((PrintWriter)localObject).println("ClassWriter: Position UNKNOWN in type annotation: " + localTypeCompound);
/*      */         }
/*      */ 
/*      */       }
/*  788 */       else if ((localTypeCompound.position.type.isLocal() == paramBoolean) && 
/*  790 */         (localTypeCompound.position.emitToClassfile()))
/*      */       {
/*  792 */         switch (1.$SwitchMap$com$sun$tools$javac$code$Attribute$RetentionPolicy[this.types.getRetention(localTypeCompound).ordinal()]) { case 1:
/*  793 */           break;
/*      */         case 2:
/*  794 */           localListBuffer2.append(localTypeCompound); break;
/*      */         case 3:
/*  795 */           localListBuffer1.append(localTypeCompound);
/*      */         }
/*      */       }
/*      */     Object localObject;
/*  800 */     int i = 0;
/*      */     int j;
/*      */     Iterator localIterator2;
/*  801 */     if (localListBuffer1.length() != 0) {
/*  802 */       j = writeAttr(this.names.RuntimeVisibleTypeAnnotations);
/*  803 */       this.databuf.appendChar(localListBuffer1.length());
/*  804 */       for (localIterator2 = localListBuffer1.iterator(); localIterator2.hasNext(); ) { localObject = (Attribute.TypeCompound)localIterator2.next();
/*  805 */         writeTypeAnnotation((Attribute.TypeCompound)localObject); }
/*  806 */       endAttr(j);
/*  807 */       i++;
/*      */     }
/*      */ 
/*  810 */     if (localListBuffer2.length() != 0) {
/*  811 */       j = writeAttr(this.names.RuntimeInvisibleTypeAnnotations);
/*  812 */       this.databuf.appendChar(localListBuffer2.length());
/*  813 */       for (localIterator2 = localListBuffer2.iterator(); localIterator2.hasNext(); ) { localObject = (Attribute.TypeCompound)localIterator2.next();
/*  814 */         writeTypeAnnotation((Attribute.TypeCompound)localObject); }
/*  815 */       endAttr(j);
/*  816 */       i++;
/*      */     }
/*      */ 
/*  819 */     return i;
/*      */   }
/*      */ 
/*      */   void writeCompoundAttribute(Attribute.Compound paramCompound)
/*      */   {
/*  891 */     this.databuf.appendChar(this.pool.put(typeSig(paramCompound.type)));
/*  892 */     this.databuf.appendChar(paramCompound.values.length());
/*  893 */     for (Pair localPair : paramCompound.values) {
/*  894 */       this.databuf.appendChar(this.pool.put(((Symbol.MethodSymbol)localPair.fst).name));
/*  895 */       ((Attribute)localPair.snd).accept(this.awriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeTypeAnnotation(Attribute.TypeCompound paramTypeCompound) {
/*  900 */     writePosition(paramTypeCompound.position);
/*  901 */     writeCompoundAttribute(paramTypeCompound);
/*      */   }
/*      */ 
/*      */   void writePosition(TypeAnnotationPosition paramTypeAnnotationPosition) {
/*  905 */     this.databuf.appendByte(paramTypeAnnotationPosition.type.targetTypeValue());
/*  906 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TargetType[paramTypeAnnotationPosition.type.ordinal()])
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  914 */       this.databuf.appendChar(paramTypeAnnotationPosition.offset);
/*  915 */       break;
/*      */     case 5:
/*      */     case 6:
/*  920 */       this.databuf.appendChar(paramTypeAnnotationPosition.lvarOffset.length);
/*  921 */       for (int i = 0; i < paramTypeAnnotationPosition.lvarOffset.length; i++) {
/*  922 */         this.databuf.appendChar(paramTypeAnnotationPosition.lvarOffset[i]);
/*  923 */         this.databuf.appendChar(paramTypeAnnotationPosition.lvarLength[i]);
/*  924 */         this.databuf.appendChar(paramTypeAnnotationPosition.lvarIndex[i]);
/*      */       }
/*  926 */       break;
/*      */     case 7:
/*  929 */       this.databuf.appendChar(paramTypeAnnotationPosition.exception_index);
/*  930 */       break;
/*      */     case 8:
/*  934 */       break;
/*      */     case 9:
/*      */     case 10:
/*  938 */       this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*  939 */       break;
/*      */     case 11:
/*      */     case 12:
/*  943 */       this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*  944 */       this.databuf.appendByte(paramTypeAnnotationPosition.bound_index);
/*  945 */       break;
/*      */     case 13:
/*  948 */       this.databuf.appendChar(paramTypeAnnotationPosition.type_index);
/*  949 */       break;
/*      */     case 14:
/*  952 */       this.databuf.appendChar(paramTypeAnnotationPosition.type_index);
/*  953 */       break;
/*      */     case 15:
/*  956 */       this.databuf.appendByte(paramTypeAnnotationPosition.parameter_index);
/*  957 */       break;
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*  965 */       this.databuf.appendChar(paramTypeAnnotationPosition.offset);
/*  966 */       this.databuf.appendByte(paramTypeAnnotationPosition.type_index);
/*  967 */       break;
/*      */     case 21:
/*      */     case 22:
/*  971 */       break;
/*      */     case 23:
/*  973 */       throw new AssertionError("jvm.ClassWriter: UNKNOWN target type should never occur!");
/*      */     default:
/*  975 */       throw new AssertionError("jvm.ClassWriter: Unknown target type for position: " + paramTypeAnnotationPosition);
/*      */     }
/*      */ 
/*  979 */     this.databuf.appendByte(paramTypeAnnotationPosition.location.size());
/*  980 */     com.sun.tools.javac.util.List localList = TypeAnnotationPosition.getBinaryFromTypePath(paramTypeAnnotationPosition.location);
/*  981 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { int j = ((Integer)localIterator.next()).intValue();
/*  982 */       this.databuf.appendByte((byte)j);
/*      */     }
/*      */   }
/*      */ 
/*      */   void enterInner(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  993 */     if (paramClassSymbol.type.isCompound())
/*  994 */       throw new AssertionError("Unexpected intersection type: " + paramClassSymbol.type);
/*      */     try
/*      */     {
/*  997 */       paramClassSymbol.complete();
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  999 */       System.err.println("error: " + paramClassSymbol + ": " + localCompletionFailure.getMessage());
/* 1000 */       throw localCompletionFailure;
/*      */     }
/* 1002 */     if (!paramClassSymbol.type.hasTag(TypeTag.CLASS)) return;
/* 1003 */     if ((this.pool != null) && 
/* 1004 */       (paramClassSymbol.owner
/* 1004 */       .enclClass() != null) && (
/* 1004 */       (this.innerClasses == null) || 
/* 1005 */       (!this.innerClasses
/* 1005 */       .contains(paramClassSymbol))))
/*      */     {
/* 1007 */       enterInner(paramClassSymbol.owner.enclClass());
/* 1008 */       this.pool.put(paramClassSymbol);
/* 1009 */       if (paramClassSymbol.name != this.names.empty)
/* 1010 */         this.pool.put(paramClassSymbol.name);
/* 1011 */       if (this.innerClasses == null) {
/* 1012 */         this.innerClasses = new HashSet();
/* 1013 */         this.innerClassesQueue = new ListBuffer();
/* 1014 */         this.pool.put(this.names.InnerClasses);
/*      */       }
/* 1016 */       this.innerClasses.add(paramClassSymbol);
/* 1017 */       this.innerClassesQueue.append(paramClassSymbol);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeInnerClasses()
/*      */   {
/* 1024 */     int i = writeAttr(this.names.InnerClasses);
/* 1025 */     this.databuf.appendChar(this.innerClassesQueue.length());
/* 1026 */     for (com.sun.tools.javac.util.List localList = this.innerClassesQueue.toList(); 
/* 1027 */       localList.nonEmpty(); 
/* 1028 */       localList = localList.tail) {
/* 1029 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)localList.head;
/* 1030 */       localClassSymbol.markAbstractIfNeeded(this.types);
/* 1031 */       int j = (char)adjustFlags(localClassSymbol.flags_field);
/* 1032 */       if ((j & 0x200) != 0) j = (char)(j | 0x400);
/* 1033 */       if (localClassSymbol.name.isEmpty()) j = (char)(j & 0xFFFFFFEF);
/* 1034 */       j = (char)(j & 0xFFFFF7FF);
/* 1035 */       if (this.dumpInnerClassModifiers) {
/* 1036 */         PrintWriter localPrintWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1037 */         localPrintWriter.println("INNERCLASS  " + localClassSymbol.name);
/* 1038 */         localPrintWriter.println("---" + flagNames(j));
/*      */       }
/* 1040 */       this.databuf.appendChar(this.pool.get(localClassSymbol));
/* 1041 */       this.databuf.appendChar((localClassSymbol.owner.kind == 2) && 
/* 1042 */         (!localClassSymbol.name
/* 1042 */         .isEmpty()) ? this.pool.get(localClassSymbol.owner) : 0);
/* 1043 */       this.databuf.appendChar(
/* 1044 */         !localClassSymbol.name
/* 1044 */         .isEmpty() ? this.pool.get(localClassSymbol.name) : 0);
/* 1045 */       this.databuf.appendChar(j);
/*      */     }
/* 1047 */     endAttr(i);
/*      */   }
/*      */ 
/*      */   void writeBootstrapMethods()
/*      */   {
/* 1053 */     int i = writeAttr(this.names.BootstrapMethods);
/* 1054 */     this.databuf.appendChar(this.bootstrapMethods.size());
/* 1055 */     for (Map.Entry localEntry : this.bootstrapMethods.entrySet()) {
/* 1056 */       Pool.DynamicMethod localDynamicMethod = (Pool.DynamicMethod)localEntry.getKey();
/* 1057 */       Symbol.DynamicMethodSymbol localDynamicMethodSymbol = (Symbol.DynamicMethodSymbol)localDynamicMethod.baseSymbol();
/*      */ 
/* 1059 */       this.databuf.appendChar(this.pool.get(localEntry.getValue()));
/*      */ 
/* 1061 */       this.databuf.appendChar(localDynamicMethodSymbol.staticArgs.length);
/*      */ 
/* 1063 */       Object[] arrayOfObject1 = localDynamicMethod.uniqueStaticArgs;
/* 1064 */       for (Object localObject : arrayOfObject1) {
/* 1065 */         this.databuf.appendChar(this.pool.get(localObject));
/*      */       }
/*      */     }
/* 1068 */     endAttr(i);
/*      */   }
/*      */ 
/*      */   void writeField(Symbol.VarSymbol paramVarSymbol)
/*      */   {
/* 1074 */     int i = adjustFlags(paramVarSymbol.flags());
/* 1075 */     this.databuf.appendChar(i);
/* 1076 */     if (this.dumpFieldModifiers) {
/* 1077 */       PrintWriter localPrintWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1078 */       localPrintWriter.println("FIELD  " + fieldName(paramVarSymbol));
/* 1079 */       localPrintWriter.println("---" + flagNames(paramVarSymbol.flags()));
/*      */     }
/* 1081 */     this.databuf.appendChar(this.pool.put(fieldName(paramVarSymbol)));
/* 1082 */     this.databuf.appendChar(this.pool.put(typeSig(paramVarSymbol.erasure(this.types))));
/* 1083 */     int j = beginAttrs();
/* 1084 */     int k = 0;
/* 1085 */     if (paramVarSymbol.getConstValue() != null) {
/* 1086 */       int m = writeAttr(this.names.ConstantValue);
/* 1087 */       this.databuf.appendChar(this.pool.put(paramVarSymbol.getConstValue()));
/* 1088 */       endAttr(m);
/* 1089 */       k++;
/*      */     }
/* 1091 */     k += writeMemberAttrs(paramVarSymbol);
/* 1092 */     endAttrs(j, k);
/*      */   }
/*      */ 
/*      */   void writeMethod(Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 1098 */     int i = adjustFlags(paramMethodSymbol.flags());
/* 1099 */     this.databuf.appendChar(i);
/* 1100 */     if (this.dumpMethodModifiers) {
/* 1101 */       PrintWriter localPrintWriter = this.log.getWriter(Log.WriterKind.ERROR);
/* 1102 */       localPrintWriter.println("METHOD  " + fieldName(paramMethodSymbol));
/* 1103 */       localPrintWriter.println("---" + flagNames(paramMethodSymbol.flags()));
/*      */     }
/* 1105 */     this.databuf.appendChar(this.pool.put(fieldName(paramMethodSymbol)));
/* 1106 */     this.databuf.appendChar(this.pool.put(typeSig(paramMethodSymbol.externalType(this.types))));
/* 1107 */     int j = beginAttrs();
/* 1108 */     int k = 0;
/* 1109 */     if (paramMethodSymbol.code != null) {
/* 1110 */       int m = writeAttr(this.names.Code);
/* 1111 */       writeCode(paramMethodSymbol.code);
/* 1112 */       paramMethodSymbol.code = null;
/* 1113 */       endAttr(m);
/* 1114 */       k++;
/*      */     }
/* 1116 */     com.sun.tools.javac.util.List localList1 = paramMethodSymbol.erasure(this.types).getThrownTypes();
/*      */     int n;
/* 1117 */     if (localList1.nonEmpty()) {
/* 1118 */       n = writeAttr(this.names.Exceptions);
/* 1119 */       this.databuf.appendChar(localList1.length());
/* 1120 */       for (com.sun.tools.javac.util.List localList2 = localList1; localList2.nonEmpty(); localList2 = localList2.tail)
/* 1121 */         this.databuf.appendChar(this.pool.put(((Type)localList2.head).tsym));
/* 1122 */       endAttr(n);
/* 1123 */       k++;
/*      */     }
/* 1125 */     if (paramMethodSymbol.defaultValue != null) {
/* 1126 */       n = writeAttr(this.names.AnnotationDefault);
/* 1127 */       paramMethodSymbol.defaultValue.accept(this.awriter);
/* 1128 */       endAttr(n);
/* 1129 */       k++;
/*      */     }
/* 1131 */     if (this.options.isSet(Option.PARAMETERS))
/* 1132 */       k += writeMethodParametersAttr(paramMethodSymbol);
/* 1133 */     k += writeMemberAttrs(paramMethodSymbol);
/* 1134 */     k += writeParameterAttrs(paramMethodSymbol);
/* 1135 */     endAttrs(j, k);
/*      */   }
/*      */ 
/*      */   void writeCode(Code paramCode)
/*      */   {
/* 1141 */     this.databuf.appendChar(paramCode.max_stack);
/* 1142 */     this.databuf.appendChar(paramCode.max_locals);
/* 1143 */     this.databuf.appendInt(paramCode.cp);
/* 1144 */     this.databuf.appendBytes(paramCode.code, 0, paramCode.cp);
/* 1145 */     this.databuf.appendChar(paramCode.catchInfo.length());
/* 1146 */     for (com.sun.tools.javac.util.List localList1 = paramCode.catchInfo.toList(); 
/* 1147 */       localList1.nonEmpty(); 
/* 1148 */       localList1 = localList1.tail) {
/* 1149 */       for (j = 0; j < ((char[])localList1.head).length; j++)
/* 1150 */         this.databuf.appendChar(((char[])localList1.head)[j]);
/*      */     }
/* 1152 */     int i = beginAttrs();
/* 1153 */     int j = 0;
/*      */     int i1;
/* 1155 */     if (paramCode.lineInfo.nonEmpty()) {
/* 1156 */       int k = writeAttr(this.names.LineNumberTable);
/* 1157 */       this.databuf.appendChar(paramCode.lineInfo.length());
/* 1158 */       for (com.sun.tools.javac.util.List localList2 = paramCode.lineInfo.reverse(); 
/* 1159 */         localList2.nonEmpty(); 
/* 1160 */         localList2 = localList2.tail)
/* 1161 */         for (i1 = 0; i1 < ((char[])localList2.head).length; i1++)
/* 1162 */           this.databuf.appendChar(((char[])localList2.head)[i1]);
/* 1163 */       endAttr(k);
/* 1164 */       j++;
/*      */     }
/*      */     int n;
/* 1167 */     if ((this.genCrt) && (paramCode.crt != null)) {
/* 1168 */       CRTable localCRTable = paramCode.crt;
/* 1169 */       n = writeAttr(this.names.CharacterRangeTable);
/* 1170 */       i1 = beginAttrs();
/* 1171 */       int i2 = localCRTable.writeCRT(this.databuf, paramCode.lineMap, this.log);
/* 1172 */       endAttrs(i1, i2);
/* 1173 */       endAttr(n);
/* 1174 */       j++;
/*      */     }
/*      */     int m;
/* 1178 */     if ((paramCode.varDebugInfo) && (paramCode.varBufferSize > 0)) {
/* 1179 */       m = 0;
/* 1180 */       n = writeAttr(this.names.LocalVariableTable);
/* 1181 */       this.databuf.appendChar(paramCode.getLVTSize());
/*      */       Code.LocalVar localLocalVar;
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       Object localObject3;
/*      */       Object localObject4;
/* 1182 */       for (i1 = 0; i1 < paramCode.varBufferSize; i1++) {
/* 1183 */         localLocalVar = paramCode.varBuffer[i1];
/*      */ 
/* 1185 */         for (localObject1 = localLocalVar.aliveRanges.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Code.LocalVar.Range)((Iterator)localObject1).next();
/*      */ 
/* 1187 */           Assert.check((((Code.LocalVar.Range)localObject2).start_pc >= 0) && (((Code.LocalVar.Range)localObject2).start_pc <= paramCode.cp));
/*      */ 
/* 1189 */           this.databuf.appendChar(((Code.LocalVar.Range)localObject2).start_pc);
/* 1190 */           Assert.check((((Code.LocalVar.Range)localObject2).length > 0) && (((Code.LocalVar.Range)localObject2).start_pc + ((Code.LocalVar.Range)localObject2).length <= paramCode.cp));
/*      */ 
/* 1192 */           this.databuf.appendChar(((Code.LocalVar.Range)localObject2).length);
/* 1193 */           localObject3 = localLocalVar.sym;
/* 1194 */           this.databuf.appendChar(this.pool.put(((Symbol.VarSymbol)localObject3).name));
/* 1195 */           localObject4 = ((Symbol.VarSymbol)localObject3).erasure(this.types);
/* 1196 */           this.databuf.appendChar(this.pool.put(typeSig((Type)localObject4)));
/* 1197 */           this.databuf.appendChar(localLocalVar.reg);
/* 1198 */           if (needsLocalVariableTypeEntry(localLocalVar.sym.type)) {
/* 1199 */             m++;
/*      */           }
/*      */         }
/*      */       }
/* 1203 */       endAttr(n);
/* 1204 */       j++;
/*      */ 
/* 1206 */       if (m > 0) {
/* 1207 */         n = writeAttr(this.names.LocalVariableTypeTable);
/* 1208 */         this.databuf.appendChar(m);
/* 1209 */         i1 = 0;
/*      */ 
/* 1211 */         for (int i3 = 0; i3 < paramCode.varBufferSize; i3++) {
/* 1212 */           localObject1 = paramCode.varBuffer[i3];
/* 1213 */           localObject2 = ((Code.LocalVar)localObject1).sym;
/* 1214 */           if (needsLocalVariableTypeEntry(((Symbol.VarSymbol)localObject2).type))
/*      */           {
/* 1216 */             for (localObject3 = ((Code.LocalVar)localObject1).aliveRanges.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Code.LocalVar.Range)((Iterator)localObject3).next();
/*      */ 
/* 1218 */               this.databuf.appendChar(((Code.LocalVar.Range)localObject4).start_pc);
/* 1219 */               this.databuf.appendChar(((Code.LocalVar.Range)localObject4).length);
/* 1220 */               this.databuf.appendChar(this.pool.put(((Symbol.VarSymbol)localObject2).name));
/* 1221 */               this.databuf.appendChar(this.pool.put(typeSig(((Symbol.VarSymbol)localObject2).type)));
/* 1222 */               this.databuf.appendChar(((Code.LocalVar)localObject1).reg);
/* 1223 */               i1++; }
/*      */           }
/*      */         }
/* 1226 */         Assert.check(i1 == m);
/* 1227 */         endAttr(n);
/* 1228 */         j++;
/*      */       }
/*      */     }
/*      */ 
/* 1232 */     if (paramCode.stackMapBufferSize > 0) {
/* 1233 */       if (this.debugstackmap) System.out.println("Stack map for " + paramCode.meth);
/* 1234 */       m = writeAttr(paramCode.stackMap.getAttributeName(this.names));
/* 1235 */       writeStackMap(paramCode);
/* 1236 */       endAttr(m);
/* 1237 */       j++;
/*      */     }
/*      */ 
/* 1240 */     j += writeTypeAnnotations(paramCode.meth.getRawTypeAttributes(), true);
/*      */ 
/* 1242 */     endAttrs(i, j);
/*      */   }
/*      */ 
/*      */   private boolean needsLocalVariableTypeEntry(Type paramType)
/*      */   {
/* 1250 */     return (!this.types.isSameType(paramType, this.types.erasure(paramType))) && 
/* 1250 */       (!paramType
/* 1250 */       .isCompound());
/*      */   }
/*      */ 
/*      */   void writeStackMap(Code paramCode) {
/* 1254 */     int i = paramCode.stackMapBufferSize;
/* 1255 */     if (this.debugstackmap) System.out.println(" nframes = " + i);
/* 1256 */     this.databuf.appendChar(i);
/*      */     int j;
/*      */     Object localObject;
/* 1258 */     switch (paramCode.stackMap) {
/*      */     case CLDC:
/* 1260 */       for (j = 0; j < i; j++) {
/* 1261 */         if (this.debugstackmap) System.out.print("  " + j + ":");
/* 1262 */         localObject = paramCode.stackMapBuffer[j];
/*      */ 
/* 1265 */         if (this.debugstackmap) System.out.print(" pc=" + ((Code.StackMapFrame)localObject).pc);
/* 1266 */         this.databuf.appendChar(((Code.StackMapFrame)localObject).pc);
/*      */ 
/* 1269 */         int k = 0;
/* 1270 */         for (int m = 0; m < ((Code.StackMapFrame)localObject).locals.length; 
/* 1271 */           m += (this.target.generateEmptyAfterBig() ? 1 : Code.width(localObject.locals[m]))) {
/* 1272 */           k++;
/*      */         }
/* 1274 */         if (this.debugstackmap) System.out.print(" nlocals=" + k);
/*      */ 
/* 1276 */         this.databuf.appendChar(k);
/* 1277 */         for (m = 0; m < ((Code.StackMapFrame)localObject).locals.length; 
/* 1278 */           m += (this.target.generateEmptyAfterBig() ? 1 : Code.width(localObject.locals[m]))) {
/* 1279 */           if (this.debugstackmap) System.out.print(" local[" + m + "]=");
/* 1280 */           writeStackMapType(localObject.locals[m]);
/*      */         }
/*      */ 
/* 1284 */         m = 0;
/* 1285 */         for (int n = 0; n < ((Code.StackMapFrame)localObject).stack.length; 
/* 1286 */           n += (this.target.generateEmptyAfterBig() ? 1 : Code.width(localObject.stack[n]))) {
/* 1287 */           m++;
/*      */         }
/* 1289 */         if (this.debugstackmap) System.out.print(" nstack=" + m);
/*      */ 
/* 1291 */         this.databuf.appendChar(m);
/* 1292 */         for (n = 0; n < ((Code.StackMapFrame)localObject).stack.length; 
/* 1293 */           n += (this.target.generateEmptyAfterBig() ? 1 : Code.width(localObject.stack[n]))) {
/* 1294 */           if (this.debugstackmap) System.out.print(" stack[" + n + "]=");
/* 1295 */           writeStackMapType(localObject.stack[n]);
/*      */         }
/* 1297 */         if (this.debugstackmap) System.out.println();
/*      */       }
/* 1299 */       break;
/*      */     case JSR202:
/* 1301 */       Assert.checkNull(paramCode.stackMapBuffer);
/* 1302 */       for (j = 0; j < i; j++) {
/* 1303 */         if (this.debugstackmap) System.out.print("  " + j + ":");
/* 1304 */         localObject = paramCode.stackMapTableBuffer[j];
/* 1305 */         ((StackMapTableFrame)localObject).write(this);
/* 1306 */         if (this.debugstackmap) System.out.println();
/*      */       }
/* 1308 */       break;
/*      */     default:
/* 1311 */       throw new AssertionError("Unexpected stackmap format value");
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeStackMapType(Type paramType)
/*      */   {
/* 1317 */     if (paramType == null) {
/* 1318 */       if (this.debugstackmap) System.out.print("empty");
/* 1319 */       this.databuf.appendByte(0);
/*      */     } else {
/* 1321 */       switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 10:
/* 1327 */         if (this.debugstackmap) System.out.print("int");
/* 1328 */         this.databuf.appendByte(1);
/* 1329 */         break;
/*      */       case 8:
/* 1331 */         if (this.debugstackmap) System.out.print("float");
/* 1332 */         this.databuf.appendByte(2);
/* 1333 */         break;
/*      */       case 9:
/* 1335 */         if (this.debugstackmap) System.out.print("double");
/* 1336 */         this.databuf.appendByte(3);
/* 1337 */         break;
/*      */       case 7:
/* 1339 */         if (this.debugstackmap) System.out.print("long");
/* 1340 */         this.databuf.appendByte(4);
/* 1341 */         break;
/*      */       case 12:
/* 1343 */         if (this.debugstackmap) System.out.print("null");
/* 1344 */         this.databuf.appendByte(5);
/* 1345 */         break;
/*      */       case 11:
/*      */       case 13:
/* 1348 */         if (this.debugstackmap) System.out.print("object(" + paramType + ")");
/* 1349 */         this.databuf.appendByte(7);
/* 1350 */         this.databuf.appendChar(this.pool.put(paramType));
/* 1351 */         break;
/*      */       case 14:
/* 1353 */         if (this.debugstackmap) System.out.print("object(" + this.types.erasure(paramType).tsym + ")");
/* 1354 */         this.databuf.appendByte(7);
/* 1355 */         this.databuf.appendChar(this.pool.put(this.types.erasure(paramType).tsym));
/* 1356 */         break;
/*      */       case 1:
/* 1358 */         if (this.debugstackmap) System.out.print("uninit_this");
/* 1359 */         this.databuf.appendByte(6);
/* 1360 */         break;
/*      */       case 2:
/* 1362 */         UninitializedType localUninitializedType = (UninitializedType)paramType;
/* 1363 */         this.databuf.appendByte(8);
/* 1364 */         if (this.debugstackmap) System.out.print("uninit_object@" + localUninitializedType.offset);
/* 1365 */         this.databuf.appendChar(localUninitializedType.offset);
/*      */ 
/* 1367 */         break;
/*      */       default:
/* 1369 */         throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeFields(Scope.Entry paramEntry)
/*      */   {
/* 1586 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1587 */     for (Scope.Entry localEntry = paramEntry; localEntry != null; localEntry = localEntry.sibling) {
/* 1588 */       if (localEntry.sym.kind == 4) localList = localList.prepend((Symbol.VarSymbol)localEntry.sym);
/*      */     }
/* 1590 */     while (localList.nonEmpty()) {
/* 1591 */       writeField((Symbol.VarSymbol)localList.head);
/* 1592 */       localList = localList.tail;
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeMethods(Scope.Entry paramEntry) {
/* 1597 */     com.sun.tools.javac.util.List localList = com.sun.tools.javac.util.List.nil();
/* 1598 */     for (Scope.Entry localEntry = paramEntry; localEntry != null; localEntry = localEntry.sibling) {
/* 1599 */       if ((localEntry.sym.kind == 16) && ((localEntry.sym.flags() & 0x0) == 0L))
/* 1600 */         localList = localList.prepend((Symbol.MethodSymbol)localEntry.sym);
/*      */     }
/* 1602 */     while (localList.nonEmpty()) {
/* 1603 */       writeMethod((Symbol.MethodSymbol)localList.head);
/* 1604 */       localList = localList.tail;
/*      */     }
/*      */   }
/*      */ 
/*      */   public JavaFileObject writeClass(Symbol.ClassSymbol paramClassSymbol)
/*      */     throws IOException, ClassWriter.PoolOverflow, ClassWriter.StringOverflow
/*      */   {
/* 1615 */     JavaFileObject localJavaFileObject = this.fileManager
/* 1615 */       .getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramClassSymbol.flatname
/* 1616 */       .toString(), JavaFileObject.Kind.CLASS, paramClassSymbol.sourcefile);
/*      */ 
/* 1619 */     OutputStream localOutputStream = localJavaFileObject.openOutputStream();
/*      */     try {
/* 1621 */       writeClassFile(localOutputStream, paramClassSymbol);
/* 1622 */       if (this.verbose)
/* 1623 */         this.log.printVerbose("wrote.file", new Object[] { localJavaFileObject });
/* 1624 */       localOutputStream.close();
/* 1625 */       localOutputStream = null;
/*      */     } finally {
/* 1627 */       if (localOutputStream != null)
/*      */       {
/* 1629 */         localOutputStream.close();
/* 1630 */         localJavaFileObject.delete();
/* 1631 */         localJavaFileObject = null;
/*      */       }
/*      */     }
/* 1634 */     return localJavaFileObject;
/*      */   }
/*      */ 
/*      */   public void writeClassFile(OutputStream paramOutputStream, Symbol.ClassSymbol paramClassSymbol)
/*      */     throws IOException, ClassWriter.PoolOverflow, ClassWriter.StringOverflow
/*      */   {
/* 1641 */     Assert.check((paramClassSymbol.flags() & 0x1000000) == 0L);
/* 1642 */     this.databuf.reset();
/* 1643 */     this.poolbuf.reset();
/* 1644 */     this.signatureGen.reset();
/* 1645 */     this.pool = paramClassSymbol.pool;
/* 1646 */     this.innerClasses = null;
/* 1647 */     this.innerClassesQueue = null;
/* 1648 */     this.bootstrapMethods = new LinkedHashMap();
/*      */ 
/* 1650 */     Type localType = this.types.supertype(paramClassSymbol.type);
/* 1651 */     com.sun.tools.javac.util.List localList1 = this.types.interfaces(paramClassSymbol.type);
/* 1652 */     com.sun.tools.javac.util.List localList2 = paramClassSymbol.type.getTypeArguments();
/*      */ 
/* 1654 */     int i = adjustFlags(paramClassSymbol.flags() & 0xFFFFFFFF);
/* 1655 */     if ((i & 0x4) != 0) i |= 1;
/* 1656 */     i = i & 0x7E11 & 0xFFFFF7FF;
/* 1657 */     if ((i & 0x200) == 0) i |= 32;
/* 1658 */     if ((paramClassSymbol.isInner()) && (paramClassSymbol.name.isEmpty())) i &= -17;
/* 1659 */     if (this.dumpClassModifiers) {
/* 1660 */       localObject1 = this.log.getWriter(Log.WriterKind.ERROR);
/* 1661 */       ((PrintWriter)localObject1).println();
/* 1662 */       ((PrintWriter)localObject1).println("CLASSFILE  " + paramClassSymbol.getQualifiedName());
/* 1663 */       ((PrintWriter)localObject1).println("---" + flagNames(i));
/*      */     }
/* 1665 */     this.databuf.appendChar(i);
/*      */ 
/* 1667 */     this.databuf.appendChar(this.pool.put(paramClassSymbol));
/* 1668 */     this.databuf.appendChar(localType.hasTag(TypeTag.CLASS) ? this.pool.put(localType.tsym) : 0);
/* 1669 */     this.databuf.appendChar(localList1.length());
/* 1670 */     for (Object localObject1 = localList1; ((com.sun.tools.javac.util.List)localObject1).nonEmpty(); localObject1 = ((com.sun.tools.javac.util.List)localObject1).tail)
/* 1671 */       this.databuf.appendChar(this.pool.put(((Type)((com.sun.tools.javac.util.List)localObject1).head).tsym));
/* 1672 */     int j = 0;
/* 1673 */     int k = 0;
/* 1674 */     for (Object localObject2 = paramClassSymbol.members().elems; localObject2 != null; localObject2 = ((Scope.Entry)localObject2).sibling) {
/* 1675 */       switch (((Scope.Entry)localObject2).sym.kind) { case 4:
/* 1676 */         j++; break;
/*      */       case 16:
/* 1677 */         if ((((Scope.Entry)localObject2).sym.flags() & 0x0) == 0L) k++; break;
/*      */       case 2:
/* 1679 */         enterInner((Symbol.ClassSymbol)((Scope.Entry)localObject2).sym); break;
/*      */       default:
/* 1680 */         Assert.error();
/*      */       }
/*      */     }
/*      */ 
/* 1684 */     if (paramClassSymbol.trans_local != null) {
/* 1685 */       for (localObject2 = paramClassSymbol.trans_local.iterator(); ((Iterator)localObject2).hasNext(); ) { Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((Iterator)localObject2).next();
/* 1686 */         enterInner(localClassSymbol);
/*      */       }
/*      */     }
/*      */ 
/* 1690 */     this.databuf.appendChar(j);
/* 1691 */     writeFields(paramClassSymbol.members().elems);
/* 1692 */     this.databuf.appendChar(k);
/* 1693 */     writeMethods(paramClassSymbol.members().elems);
/*      */ 
/* 1695 */     int m = beginAttrs();
/* 1696 */     int n = 0;
/*      */ 
/* 1699 */     int i1 = (localList2
/* 1699 */       .length() != 0) || (localType.allparams().length() != 0) ? 1 : 0;
/* 1700 */     for (com.sun.tools.javac.util.List localList3 = localList1; (i1 == 0) && (localList3.nonEmpty()); localList3 = localList3.tail)
/* 1701 */       i1 = ((Type)localList3.head).allparams().length() != 0 ? 1 : 0;
/*      */     int i2;
/*      */     Object localObject3;
/* 1702 */     if (i1 != 0) {
/* 1703 */       Assert.check(this.source.allowGenerics());
/* 1704 */       i2 = writeAttr(this.names.Signature);
/* 1705 */       if (localList2.length() != 0) this.signatureGen.assembleParamsSig(localList2);
/* 1706 */       this.signatureGen.assembleSig(localType);
/* 1707 */       for (localObject3 = localList1; ((com.sun.tools.javac.util.List)localObject3).nonEmpty(); localObject3 = ((com.sun.tools.javac.util.List)localObject3).tail)
/* 1708 */         this.signatureGen.assembleSig((Type)((com.sun.tools.javac.util.List)localObject3).head);
/* 1709 */       this.databuf.appendChar(this.pool.put(this.signatureGen.toName()));
/* 1710 */       this.signatureGen.reset();
/* 1711 */       endAttr(i2);
/* 1712 */       n++;
/*      */     }
/*      */ 
/* 1715 */     if ((paramClassSymbol.sourcefile != null) && (this.emitSourceFile)) {
/* 1716 */       i2 = writeAttr(this.names.SourceFile);
/*      */ 
/* 1721 */       localObject3 = BaseFileObject.getSimpleName(paramClassSymbol.sourcefile);
/* 1722 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString((String)localObject3)));
/* 1723 */       endAttr(i2);
/* 1724 */       n++;
/*      */     }
/*      */ 
/* 1727 */     if (this.genCrt)
/*      */     {
/* 1729 */       i2 = writeAttr(this.names.SourceID);
/* 1730 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString(Long.toString(getLastModified(paramClassSymbol.sourcefile)))));
/* 1731 */       endAttr(i2);
/* 1732 */       n++;
/*      */ 
/* 1734 */       i2 = writeAttr(this.names.CompilationID);
/* 1735 */       this.databuf.appendChar(paramClassSymbol.pool.put(this.names.fromString(Long.toString(System.currentTimeMillis()))));
/* 1736 */       endAttr(i2);
/* 1737 */       n++;
/*      */     }
/*      */ 
/* 1740 */     n += writeFlagAttrs(paramClassSymbol.flags());
/* 1741 */     n += writeJavaAnnotations(paramClassSymbol.getRawAttributes());
/* 1742 */     n += writeTypeAnnotations(paramClassSymbol.getRawTypeAttributes(), false);
/* 1743 */     n += writeEnclosingMethodAttribute(paramClassSymbol);
/* 1744 */     n += writeExtraClassAttributes(paramClassSymbol);
/*      */ 
/* 1746 */     this.poolbuf.appendInt(-889275714);
/* 1747 */     this.poolbuf.appendChar(this.target.minorVersion);
/* 1748 */     this.poolbuf.appendChar(this.target.majorVersion);
/*      */ 
/* 1750 */     writePool(paramClassSymbol.pool);
/*      */ 
/* 1752 */     if (this.innerClasses != null) {
/* 1753 */       writeInnerClasses();
/* 1754 */       n++;
/*      */     }
/*      */ 
/* 1757 */     if (!this.bootstrapMethods.isEmpty()) {
/* 1758 */       writeBootstrapMethods();
/* 1759 */       n++;
/*      */     }
/*      */ 
/* 1762 */     endAttrs(m, n);
/*      */ 
/* 1764 */     this.poolbuf.appendBytes(this.databuf.elems, 0, this.databuf.length);
/* 1765 */     paramOutputStream.write(this.poolbuf.elems, 0, this.poolbuf.length);
/*      */ 
/* 1767 */     this.pool = (paramClassSymbol.pool = null);
/*      */   }
/*      */ 
/*      */   protected int writeExtraClassAttributes(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1775 */     return 0;
/*      */   }
/*      */ 
/*      */   int adjustFlags(long paramLong) {
/* 1779 */     int i = (int)paramLong;
/* 1780 */     if (((paramLong & 0x1000) != 0L) && (!this.target.useSyntheticFlag()))
/* 1781 */       i &= -4097;
/* 1782 */     if (((paramLong & 0x4000) != 0L) && (!this.target.useEnumFlag()))
/* 1783 */       i &= -16385;
/* 1784 */     if (((paramLong & 0x2000) != 0L) && (!this.target.useAnnotationFlag())) {
/* 1785 */       i &= -8193;
/*      */     }
/* 1787 */     if (((paramLong & 0x80000000) != 0L) && (this.target.useBridgeFlag()))
/* 1788 */       i |= 64;
/* 1789 */     if (((paramLong & 0x0) != 0L) && (this.target.useVarargsFlag()))
/* 1790 */       i |= 128;
/* 1791 */     if ((paramLong & 0x0) != 0L)
/* 1792 */       i &= -1025;
/* 1793 */     return i;
/*      */   }
/*      */ 
/*      */   long getLastModified(FileObject paramFileObject) {
/* 1797 */     long l = 0L;
/*      */     try {
/* 1799 */       l = paramFileObject.getLastModified();
/*      */     } catch (SecurityException localSecurityException) {
/* 1801 */       throw new AssertionError("CRT: couldn't get source file modification date: " + localSecurityException.getMessage());
/*      */     }
/* 1803 */     return l;
/*      */   }
/*      */ 
/*      */   class AttributeWriter
/*      */     implements Attribute.Visitor
/*      */   {
/*      */     AttributeWriter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitConstant(Attribute.Constant paramConstant)
/*      */     {
/*  827 */       Object localObject = paramConstant.value;
/*  828 */       switch (ClassWriter.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramConstant.type.getTag().ordinal()]) {
/*      */       case 3:
/*  830 */         ClassWriter.this.databuf.appendByte(66);
/*  831 */         break;
/*      */       case 4:
/*  833 */         ClassWriter.this.databuf.appendByte(67);
/*  834 */         break;
/*      */       case 5:
/*  836 */         ClassWriter.this.databuf.appendByte(83);
/*  837 */         break;
/*      */       case 6:
/*  839 */         ClassWriter.this.databuf.appendByte(73);
/*  840 */         break;
/*      */       case 7:
/*  842 */         ClassWriter.this.databuf.appendByte(74);
/*  843 */         break;
/*      */       case 8:
/*  845 */         ClassWriter.this.databuf.appendByte(70);
/*  846 */         break;
/*      */       case 9:
/*  848 */         ClassWriter.this.databuf.appendByte(68);
/*  849 */         break;
/*      */       case 10:
/*  851 */         ClassWriter.this.databuf.appendByte(90);
/*  852 */         break;
/*      */       case 11:
/*  854 */         Assert.check(localObject instanceof String);
/*  855 */         ClassWriter.this.databuf.appendByte(115);
/*  856 */         localObject = ClassWriter.this.names.fromString(localObject.toString());
/*  857 */         break;
/*      */       default:
/*  859 */         throw new AssertionError(paramConstant.type);
/*      */       }
/*  861 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(localObject));
/*      */     }
/*      */     public void visitEnum(Attribute.Enum paramEnum) {
/*  864 */       ClassWriter.this.databuf.appendByte(101);
/*  865 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(ClassWriter.this.typeSig(paramEnum.value.type)));
/*  866 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(paramEnum.value.name));
/*      */     }
/*      */     public void visitClass(Attribute.Class paramClass) {
/*  869 */       ClassWriter.this.databuf.appendByte(99);
/*  870 */       ClassWriter.this.databuf.appendChar(ClassWriter.this.pool.put(ClassWriter.this.typeSig(paramClass.classType)));
/*      */     }
/*      */     public void visitCompound(Attribute.Compound paramCompound) {
/*  873 */       ClassWriter.this.databuf.appendByte(64);
/*  874 */       ClassWriter.this.writeCompoundAttribute(paramCompound);
/*      */     }
/*      */     public void visitError(Attribute.Error paramError) {
/*  877 */       throw new AssertionError(paramError);
/*      */     }
/*      */     public void visitArray(Attribute.Array paramArray) {
/*  880 */       ClassWriter.this.databuf.appendByte(91);
/*  881 */       ClassWriter.this.databuf.appendChar(paramArray.values.length);
/*  882 */       for (Attribute localAttribute : paramArray.values)
/*  883 */         localAttribute.accept(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class CWSignatureGenerator extends Types.SignatureGenerator
/*      */   {
/*  278 */     ByteBuffer sigbuf = new ByteBuffer();
/*      */ 
/*      */     CWSignatureGenerator(Types arg2) {
/*  281 */       super();
/*      */     }
/*      */ 
/*      */     public void assembleSig(Type paramType)
/*      */     {
/*  290 */       paramType = paramType.unannotatedType();
/*  291 */       switch (ClassWriter.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()])
/*      */       {
/*      */       case 1:
/*      */       case 2:
/*  296 */         assembleSig(ClassWriter.this.types.erasure(((UninitializedType)paramType).qtype));
/*  297 */         break;
/*      */       default:
/*  299 */         super.assembleSig(paramType);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void append(char paramChar)
/*      */     {
/*  305 */       this.sigbuf.appendByte(paramChar);
/*      */     }
/*      */ 
/*      */     protected void append(byte[] paramArrayOfByte)
/*      */     {
/*  310 */       this.sigbuf.appendBytes(paramArrayOfByte);
/*      */     }
/*      */ 
/*      */     protected void append(Name paramName)
/*      */     {
/*  315 */       this.sigbuf.appendName(paramName);
/*      */     }
/*      */ 
/*      */     protected void classReference(Symbol.ClassSymbol paramClassSymbol)
/*      */     {
/*  320 */       ClassWriter.this.enterInner(paramClassSymbol);
/*      */     }
/*      */ 
/*      */     private void reset() {
/*  324 */       this.sigbuf.reset();
/*      */     }
/*      */ 
/*      */     private Name toName() {
/*  328 */       return this.sigbuf.toName(ClassWriter.this.names);
/*      */     }
/*      */ 
/*      */     private boolean isEmpty() {
/*  332 */       return this.sigbuf.length == 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PoolOverflow extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */   }
/*      */ 
/*      */   static abstract class StackMapTableFrame
/*      */   {
/*      */     abstract int getFrameType();
/*      */ 
/*      */     void write(ClassWriter paramClassWriter)
/*      */     {
/* 1378 */       int i = getFrameType();
/* 1379 */       paramClassWriter.databuf.appendByte(i);
/* 1380 */       if (paramClassWriter.debugstackmap) System.out.print(" frame_type=" + i);
/*      */     }
/*      */ 
/*      */     static StackMapTableFrame getInstance(Code.StackMapFrame paramStackMapFrame, int paramInt, Type[] paramArrayOfType, Types paramTypes)
/*      */     {
/* 1512 */       Type[] arrayOfType1 = paramStackMapFrame.locals;
/* 1513 */       Type[] arrayOfType2 = paramStackMapFrame.stack;
/* 1514 */       int i = paramStackMapFrame.pc - paramInt - 1;
/* 1515 */       if (arrayOfType2.length == 1) {
/* 1516 */         if ((arrayOfType1.length == paramArrayOfType.length) && 
/* 1517 */           (compare(paramArrayOfType, arrayOfType1, paramTypes) == 0))
/*      */         {
/* 1518 */           return new SameLocals1StackItemFrame(i, arrayOfType2[0]);
/*      */         }
/* 1520 */       } else if (arrayOfType2.length == 0) {
/* 1521 */         int j = compare(paramArrayOfType, arrayOfType1, paramTypes);
/* 1522 */         if (j == 0)
/* 1523 */           return new SameFrame(i);
/* 1524 */         if ((-4 < j) && (j < 0))
/*      */         {
/* 1526 */           Type[] arrayOfType3 = new Type[-j];
/* 1527 */           int k = paramArrayOfType.length; for (int m = 0; k < arrayOfType1.length; m++) {
/* 1528 */             arrayOfType3[m] = arrayOfType1[k];
/*      */ 
/* 1527 */             k++;
/*      */           }
/*      */ 
/* 1530 */           return new AppendFrame(251 - j, i, arrayOfType3);
/*      */         }
/*      */ 
/* 1533 */         if ((0 < j) && (j < 4))
/*      */         {
/* 1535 */           return new ChopFrame(251 - j, i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1540 */       return new FullFrame(i, arrayOfType1, arrayOfType2);
/*      */     }
/*      */ 
/*      */     static boolean isInt(Type paramType) {
/* 1544 */       return (paramType.getTag().isStrictSubRangeOf(TypeTag.INT)) || (paramType.hasTag(TypeTag.BOOLEAN));
/*      */     }
/*      */ 
/*      */     static boolean isSameType(Type paramType1, Type paramType2, Types paramTypes) {
/* 1548 */       if (paramType1 == null) return paramType2 == null;
/* 1549 */       if (paramType2 == null) return false;
/*      */ 
/* 1551 */       if ((isInt(paramType1)) && (isInt(paramType2))) return true;
/*      */ 
/* 1553 */       if (paramType1.hasTag(TypeTag.UNINITIALIZED_THIS))
/* 1554 */         return paramType2.hasTag(TypeTag.UNINITIALIZED_THIS);
/* 1555 */       if (paramType1.hasTag(TypeTag.UNINITIALIZED_OBJECT)) {
/* 1556 */         if (paramType2.hasTag(TypeTag.UNINITIALIZED_OBJECT)) {
/* 1557 */           return ((UninitializedType)paramType1).offset == ((UninitializedType)paramType2).offset;
/*      */         }
/* 1559 */         return false;
/*      */       }
/* 1561 */       if ((paramType2.hasTag(TypeTag.UNINITIALIZED_THIS)) || (paramType2.hasTag(TypeTag.UNINITIALIZED_OBJECT))) {
/* 1562 */         return false;
/*      */       }
/*      */ 
/* 1565 */       return paramTypes.isSameType(paramType1, paramType2);
/*      */     }
/*      */ 
/*      */     static int compare(Type[] paramArrayOfType1, Type[] paramArrayOfType2, Types paramTypes) {
/* 1569 */       int i = paramArrayOfType1.length - paramArrayOfType2.length;
/* 1570 */       if ((i > 4) || (i < -4)) {
/* 1571 */         return 2147483647;
/*      */       }
/* 1573 */       int j = i > 0 ? paramArrayOfType2.length : paramArrayOfType1.length;
/* 1574 */       for (int k = 0; k < j; k++) {
/* 1575 */         if (!isSameType(paramArrayOfType1[k], paramArrayOfType2[k], paramTypes)) {
/* 1576 */           return 2147483647;
/*      */         }
/*      */       }
/* 1579 */       return i;
/*      */     }
/*      */ 
/*      */     static class AppendFrame extends ClassWriter.StackMapTableFrame
/*      */     {
/*      */       final int frameType;
/*      */       final int offsetDelta;
/*      */       final Type[] locals;
/*      */ 
/*      */       AppendFrame(int paramInt1, int paramInt2, Type[] paramArrayOfType)
/*      */       {
/* 1454 */         this.frameType = paramInt1;
/* 1455 */         this.offsetDelta = paramInt2;
/* 1456 */         this.locals = paramArrayOfType;
/*      */       }
/* 1458 */       int getFrameType() { return this.frameType; }
/*      */ 
/*      */       void write(ClassWriter paramClassWriter) {
/* 1461 */         super.write(paramClassWriter);
/* 1462 */         paramClassWriter.databuf.appendChar(this.offsetDelta);
/* 1463 */         if (paramClassWriter.debugstackmap) {
/* 1464 */           System.out.print(" offset_delta=" + this.offsetDelta);
/*      */         }
/* 1466 */         for (int i = 0; i < this.locals.length; i++) {
/* 1467 */           if (paramClassWriter.debugstackmap) System.out.print(" locals[" + i + "]=");
/* 1468 */           paramClassWriter.writeStackMapType(this.locals[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class ChopFrame extends ClassWriter.StackMapTableFrame
/*      */     {
/*      */       final int frameType;
/*      */       final int offsetDelta;
/*      */ 
/*      */       ChopFrame(int paramInt1, int paramInt2)
/*      */       {
/* 1435 */         this.frameType = paramInt1;
/* 1436 */         this.offsetDelta = paramInt2;
/*      */       }
/* 1438 */       int getFrameType() { return this.frameType; }
/*      */ 
/*      */       void write(ClassWriter paramClassWriter) {
/* 1441 */         super.write(paramClassWriter);
/* 1442 */         paramClassWriter.databuf.appendChar(this.offsetDelta);
/* 1443 */         if (paramClassWriter.debugstackmap)
/* 1444 */           System.out.print(" offset_delta=" + this.offsetDelta);
/*      */       }
/*      */     }
/*      */ 
/*      */     static class FullFrame extends ClassWriter.StackMapTableFrame
/*      */     {
/*      */       final int offsetDelta;
/*      */       final Type[] locals;
/*      */       final Type[] stack;
/*      */ 
/*      */       FullFrame(int paramInt, Type[] paramArrayOfType1, Type[] paramArrayOfType2)
/*      */       {
/* 1478 */         this.offsetDelta = paramInt;
/* 1479 */         this.locals = paramArrayOfType1;
/* 1480 */         this.stack = paramArrayOfType2;
/*      */       }
/* 1482 */       int getFrameType() { return 255; }
/*      */ 
/*      */       void write(ClassWriter paramClassWriter) {
/* 1485 */         super.write(paramClassWriter);
/* 1486 */         paramClassWriter.databuf.appendChar(this.offsetDelta);
/* 1487 */         paramClassWriter.databuf.appendChar(this.locals.length);
/* 1488 */         if (paramClassWriter.debugstackmap) {
/* 1489 */           System.out.print(" offset_delta=" + this.offsetDelta);
/* 1490 */           System.out.print(" nlocals=" + this.locals.length);
/*      */         }
/* 1492 */         for (int i = 0; i < this.locals.length; i++) {
/* 1493 */           if (paramClassWriter.debugstackmap) System.out.print(" locals[" + i + "]=");
/* 1494 */           paramClassWriter.writeStackMapType(this.locals[i]);
/*      */         }
/*      */ 
/* 1497 */         paramClassWriter.databuf.appendChar(this.stack.length);
/* 1498 */         if (paramClassWriter.debugstackmap) System.out.print(" nstack=" + this.stack.length);
/* 1499 */         for (i = 0; i < this.stack.length; i++) {
/* 1500 */           if (paramClassWriter.debugstackmap) System.out.print(" stack[" + i + "]=");
/* 1501 */           paramClassWriter.writeStackMapType(this.stack[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SameFrame extends ClassWriter.StackMapTableFrame
/*      */     {
/*      */       final int offsetDelta;
/*      */ 
/*      */       SameFrame(int paramInt)
/*      */       {
/* 1386 */         this.offsetDelta = paramInt;
/*      */       }
/*      */       int getFrameType() {
/* 1389 */         return this.offsetDelta < 64 ? this.offsetDelta : 251;
/*      */       }
/*      */ 
/*      */       void write(ClassWriter paramClassWriter) {
/* 1393 */         super.write(paramClassWriter);
/* 1394 */         if (getFrameType() == 251) {
/* 1395 */           paramClassWriter.databuf.appendChar(this.offsetDelta);
/* 1396 */           if (paramClassWriter.debugstackmap)
/* 1397 */             System.out.print(" offset_delta=" + this.offsetDelta); 
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     static class SameLocals1StackItemFrame extends ClassWriter.StackMapTableFrame {
/*      */       final int offsetDelta;
/*      */       final Type stack;
/*      */ 
/* 1407 */       SameLocals1StackItemFrame(int paramInt, Type paramType) { this.offsetDelta = paramInt;
/* 1408 */         this.stack = paramType; }
/*      */ 
/*      */       int getFrameType() {
/* 1411 */         return this.offsetDelta < 64 ? 64 + this.offsetDelta : 247;
/*      */       }
/*      */ 
/*      */       void write(ClassWriter paramClassWriter)
/*      */       {
/* 1417 */         super.write(paramClassWriter);
/* 1418 */         if (getFrameType() == 247) {
/* 1419 */           paramClassWriter.databuf.appendChar(this.offsetDelta);
/* 1420 */           if (paramClassWriter.debugstackmap) {
/* 1421 */             System.out.print(" offset_delta=" + this.offsetDelta);
/*      */           }
/*      */         }
/* 1424 */         if (paramClassWriter.debugstackmap) {
/* 1425 */           System.out.print(" stack[0]=");
/*      */         }
/* 1427 */         paramClassWriter.writeStackMapType(this.stack);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class StringOverflow extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     public final String value;
/*      */ 
/*      */     public StringOverflow(String paramString)
/*      */     {
/*  376 */       this.value = paramString;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.ClassWriter
 * JD-Core Version:    0.6.2
 */