/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ public abstract interface Constants extends sun.rmi.rmic.Constants
/*     */ {
/*  42 */   public static final Identifier idReplyHandler = Identifier.lookup("org.omg.CORBA.portable.ResponseHandler")
/*  42 */     ;
/*     */ 
/*  44 */   public static final Identifier idStubBase = Identifier.lookup("javax.rmi.CORBA.Stub")
/*  44 */     ;
/*     */ 
/*  46 */   public static final Identifier idTieBase = Identifier.lookup("org.omg.CORBA.portable.ObjectImpl")
/*  46 */     ;
/*     */ 
/*  48 */   public static final Identifier idTieInterface = Identifier.lookup("javax.rmi.CORBA.Tie")
/*  48 */     ;
/*     */ 
/*  50 */   public static final Identifier idPOAServantType = Identifier.lookup("org.omg.PortableServer.Servant")
/*  50 */     ;
/*     */ 
/*  52 */   public static final Identifier idDelegate = Identifier.lookup("org.omg.CORBA.portable.Delegate")
/*  52 */     ;
/*     */ 
/*  54 */   public static final Identifier idOutputStream = Identifier.lookup("org.omg.CORBA.portable.OutputStream")
/*  54 */     ;
/*     */ 
/*  56 */   public static final Identifier idExtOutputStream = Identifier.lookup("org.omg.CORBA_2_3.portable.OutputStream")
/*  56 */     ;
/*     */ 
/*  58 */   public static final Identifier idInputStream = Identifier.lookup("org.omg.CORBA.portable.InputStream")
/*  58 */     ;
/*     */ 
/*  60 */   public static final Identifier idExtInputStream = Identifier.lookup("org.omg.CORBA_2_3.portable.InputStream")
/*  60 */     ;
/*     */ 
/*  62 */   public static final Identifier idSystemException = Identifier.lookup("org.omg.CORBA.SystemException")
/*  62 */     ;
/*     */ 
/*  64 */   public static final Identifier idBadMethodException = Identifier.lookup("org.omg.CORBA.BAD_OPERATION")
/*  64 */     ;
/*     */ 
/*  66 */   public static final Identifier idPortableUnknownException = Identifier.lookup("org.omg.CORBA.portable.UnknownException")
/*  66 */     ;
/*     */ 
/*  68 */   public static final Identifier idApplicationException = Identifier.lookup("org.omg.CORBA.portable.ApplicationException")
/*  68 */     ;
/*     */ 
/*  70 */   public static final Identifier idRemarshalException = Identifier.lookup("org.omg.CORBA.portable.RemarshalException")
/*  70 */     ;
/*     */ 
/*  72 */   public static final Identifier idJavaIoExternalizable = Identifier.lookup("java.io.Externalizable")
/*  72 */     ;
/*     */ 
/*  74 */   public static final Identifier idCorbaObject = Identifier.lookup("org.omg.CORBA.Object")
/*  74 */     ;
/*     */ 
/*  76 */   public static final Identifier idCorbaORB = Identifier.lookup("org.omg.CORBA.ORB")
/*  76 */     ;
/*     */ 
/*  78 */   public static final Identifier idClassDesc = Identifier.lookup("javax.rmi.CORBA.ClassDesc")
/*  78 */     ;
/*     */ 
/*  80 */   public static final Identifier idJavaIoIOException = Identifier.lookup("java.io.IOException")
/*  80 */     ;
/*     */ 
/*  82 */   public static final Identifier idIDLEntity = Identifier.lookup("org.omg.CORBA.portable.IDLEntity")
/*  82 */     ;
/*     */ 
/*  84 */   public static final Identifier idValueBase = Identifier.lookup("org.omg.CORBA.portable.ValueBase")
/*  84 */     ;
/*     */ 
/*  86 */   public static final Identifier idBoxedRMI = Identifier.lookup("org.omg.boxedRMI")
/*  86 */     ;
/*     */ 
/*  88 */   public static final Identifier idBoxedIDL = Identifier.lookup("org.omg.boxedIDL")
/*  88 */     ;
/*     */ 
/*  90 */   public static final Identifier idCorbaUserException = Identifier.lookup("org.omg.CORBA.UserException")
/*  90 */     ;
/*     */ 
/*  96 */   public static final Identifier idBoolean = Identifier.lookup("boolean")
/*  96 */     ;
/*     */ 
/*  98 */   public static final Identifier idByte = Identifier.lookup("byte")
/*  98 */     ;
/*     */ 
/* 100 */   public static final Identifier idChar = Identifier.lookup("char")
/* 100 */     ;
/*     */ 
/* 102 */   public static final Identifier idShort = Identifier.lookup("short")
/* 102 */     ;
/*     */ 
/* 104 */   public static final Identifier idInt = Identifier.lookup("int")
/* 104 */     ;
/*     */ 
/* 106 */   public static final Identifier idLong = Identifier.lookup("long")
/* 106 */     ;
/*     */ 
/* 108 */   public static final Identifier idFloat = Identifier.lookup("float")
/* 108 */     ;
/*     */ 
/* 110 */   public static final Identifier idDouble = Identifier.lookup("double")
/* 110 */     ;
/*     */ 
/* 112 */   public static final Identifier idVoid = Identifier.lookup("void")
/* 112 */     ;
/*     */   public static final int INDENT_STEP = 4;
/*     */   public static final int TAB_SIZE = 2147483647;
/*     */   public static final int STATUS_PENDING = 0;
/*     */   public static final int STATUS_VALID = 1;
/*     */   public static final int STATUS_INVALID = 2;
/*     */   public static final String NAME_SEPARATOR = ".";
/*     */   public static final String SERIAL_VERSION_UID = "serialVersionUID";
/* 132 */   public static final String[] IDL_KEYWORDS = { "abstract", "any", "attribute", "boolean", "case", "char", "const", "context", "custom", "default", "double", "enum", "exception", "factory", "FALSE", "fixed", "float", "in", "inout", "interface", "long", "module", "native", "Object", "octet", "oneway", "out", "private", "public", "raises", "readonly", "sequence", "short", "string", "struct", "supports", "switch", "TRUE", "truncatable", "typedef", "unsigned", "union", "ValueBase", "valuetype", "void", "wchar", "wstring" };
/*     */   public static final String EXCEPTION_SUFFIX = "Exception";
/*     */   public static final String ERROR_SUFFIX = "Error";
/*     */   public static final String EX_SUFFIX = "Ex";
/*     */   public static final String IDL_REPOSITORY_ID_PREFIX = "IDL:";
/*     */   public static final String IDL_REPOSITORY_ID_VERSION = ":1.0";
/* 190 */   public static final String[] IDL_CORBA_MODULE = { "CORBA" };
/* 191 */   public static final String[] IDL_SEQUENCE_MODULE = { "org", "omg", "boxedRMI" };
/* 192 */   public static final String[] IDL_BOXEDIDL_MODULE = { "org", "omg", "boxedIDL" };
/*     */   public static final String IDL_CLASS = "ClassDesc";
/* 195 */   public static final String[] IDL_CLASS_MODULE = { "javax", "rmi", "CORBA" };
/*     */   public static final String IDL_IDLENTITY = "IDLEntity";
/*     */   public static final String IDL_SERIALIZABLE = "Serializable";
/*     */   public static final String IDL_EXTERNALIZABLE = "Externalizable";
/* 200 */   public static final String[] IDL_JAVA_IO_MODULE = { "java", "io" };
/* 201 */   public static final String[] IDL_ORG_OMG_CORBA_MODULE = { "org", "omg", "CORBA" };
/* 202 */   public static final String[] IDL_ORG_OMG_CORBA_PORTABLE_MODULE = { "org", "omg", "CORBA", "portable" };
/*     */   public static final String IDL_JAVA_LANG_OBJECT = "_Object";
/* 205 */   public static final String[] IDL_JAVA_LANG_MODULE = { "java", "lang" };
/*     */   public static final String IDL_JAVA_RMI_REMOTE = "Remote";
/* 208 */   public static final String[] IDL_JAVA_RMI_MODULE = { "java", "rmi" };
/*     */   public static final String IDL_SEQUENCE = "seq";
/*     */   public static final String IDL_CONSTRUCTOR = "create";
/*     */   public static final String IDL_NAME_SEPARATOR = "::";
/*     */   public static final String IDL_BOOLEAN = "boolean";
/*     */   public static final String IDL_BYTE = "octet";
/*     */   public static final String IDL_CHAR = "wchar";
/*     */   public static final String IDL_SHORT = "short";
/*     */   public static final String IDL_INT = "long";
/*     */   public static final String IDL_LONG = "long long";
/*     */   public static final String IDL_FLOAT = "float";
/*     */   public static final String IDL_DOUBLE = "double";
/*     */   public static final String IDL_VOID = "void";
/*     */   public static final String IDL_STRING = "WStringValue";
/*     */   public static final String IDL_CONSTANT_STRING = "wstring";
/*     */   public static final String IDL_CORBA_OBJECT = "Object";
/*     */   public static final String IDL_ANY = "any";
/*     */   public static final String SOURCE_FILE_EXTENSION = ".java";
/*     */   public static final String IDL_FILE_EXTENSION = ".idl";
/*     */   public static final int TYPE_VOID = 1;
/*     */   public static final int TYPE_BOOLEAN = 2;
/*     */   public static final int TYPE_BYTE = 4;
/*     */   public static final int TYPE_CHAR = 8;
/*     */   public static final int TYPE_SHORT = 16;
/*     */   public static final int TYPE_INT = 32;
/*     */   public static final int TYPE_LONG = 64;
/*     */   public static final int TYPE_FLOAT = 128;
/*     */   public static final int TYPE_DOUBLE = 256;
/*     */   public static final int TYPE_STRING = 512;
/*     */   public static final int TYPE_ANY = 1024;
/*     */   public static final int TYPE_CORBA_OBJECT = 2048;
/*     */   public static final int TYPE_REMOTE = 4096;
/*     */   public static final int TYPE_ABSTRACT = 8192;
/*     */   public static final int TYPE_NC_INTERFACE = 16384;
/*     */   public static final int TYPE_VALUE = 32768;
/*     */   public static final int TYPE_IMPLEMENTATION = 65536;
/*     */   public static final int TYPE_NC_CLASS = 131072;
/*     */   public static final int TYPE_ARRAY = 262144;
/*     */   public static final int TYPE_JAVA_RMI_REMOTE = 524288;
/*     */   public static final int TYPE_NONE = 0;
/*     */   public static final int TYPE_ALL = -1;
/*     */   public static final int TYPE_MASK = 16777215;
/*     */   public static final int TM_MASK = -16777216;
/*     */   public static final int TM_PRIMITIVE = 16777216;
/*     */   public static final int TM_COMPOUND = 33554432;
/*     */   public static final int TM_CLASS = 67108864;
/*     */   public static final int TM_INTERFACE = 134217728;
/*     */   public static final int TM_SPECIAL_CLASS = 268435456;
/*     */   public static final int TM_SPECIAL_INTERFACE = 536870912;
/*     */   public static final int TM_NON_CONFORMING = 1073741824;
/*     */   public static final int TM_INNER = -2147483648;
/*     */   public static final int ATTRIBUTE_NONE = 0;
/*     */   public static final int ATTRIBUTE_IS = 1;
/*     */   public static final int ATTRIBUTE_GET = 2;
/*     */   public static final int ATTRIBUTE_IS_RW = 3;
/*     */   public static final int ATTRIBUTE_GET_RW = 4;
/*     */   public static final int ATTRIBUTE_SET = 5;
/* 289 */   public static final String[] ATTRIBUTE_WIRE_PREFIX = { "", "_get_", "_get_", "_get_", "_get_", "_set_" };
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.Constants
 * JD-Core Version:    0.6.2
 */