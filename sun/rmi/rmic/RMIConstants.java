/*    */ package sun.rmi.rmic;
/*    */ 
/*    */ import sun.tools.java.Identifier;
/*    */ 
/*    */ public abstract interface RMIConstants extends Constants
/*    */ {
/* 48 */   public static final Identifier idRemoteObject = Identifier.lookup("java.rmi.server.RemoteObject")
/* 48 */     ;
/*    */ 
/* 50 */   public static final Identifier idRemoteStub = Identifier.lookup("java.rmi.server.RemoteStub")
/* 50 */     ;
/*    */ 
/* 52 */   public static final Identifier idRemoteRef = Identifier.lookup("java.rmi.server.RemoteRef")
/* 52 */     ;
/*    */ 
/* 54 */   public static final Identifier idOperation = Identifier.lookup("java.rmi.server.Operation")
/* 54 */     ;
/*    */ 
/* 56 */   public static final Identifier idSkeleton = Identifier.lookup("java.rmi.server.Skeleton")
/* 56 */     ;
/*    */ 
/* 58 */   public static final Identifier idSkeletonMismatchException = Identifier.lookup("java.rmi.server.SkeletonMismatchException")
/* 58 */     ;
/*    */ 
/* 60 */   public static final Identifier idRemoteCall = Identifier.lookup("java.rmi.server.RemoteCall")
/* 60 */     ;
/*    */ 
/* 62 */   public static final Identifier idMarshalException = Identifier.lookup("java.rmi.MarshalException")
/* 62 */     ;
/*    */ 
/* 64 */   public static final Identifier idUnmarshalException = Identifier.lookup("java.rmi.UnmarshalException")
/* 64 */     ;
/*    */ 
/* 66 */   public static final Identifier idUnexpectedException = Identifier.lookup("java.rmi.UnexpectedException")
/* 66 */     ;
/*    */   public static final int STUB_VERSION_1_1 = 1;
/*    */   public static final int STUB_VERSION_FAT = 2;
/*    */   public static final int STUB_VERSION_1_2 = 3;
/*    */   public static final long STUB_SERIAL_VERSION_UID = 2L;
/*    */   public static final int INTERFACE_HASH_STUB_VERSION = 1;
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.RMIConstants
 * JD-Core Version:    0.6.2
 */