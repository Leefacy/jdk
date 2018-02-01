/*    */ package sun.rmi.rmic.newrmic.jrmp;
/*    */ 
/*    */ final class Constants
/*    */ {
/*    */   static final String REMOTE_OBJECT = "java.rmi.server.RemoteObject";
/*    */   static final String REMOTE_STUB = "java.rmi.server.RemoteStub";
/*    */   static final String REMOTE_REF = "java.rmi.server.RemoteRef";
/*    */   static final String OPERATION = "java.rmi.server.Operation";
/*    */   static final String SKELETON = "java.rmi.server.Skeleton";
/*    */   static final String SKELETON_MISMATCH_EXCEPTION = "java.rmi.server.SkeletonMismatchException";
/*    */   static final String REMOTE_CALL = "java.rmi.server.RemoteCall";
/*    */   static final String MARSHAL_EXCEPTION = "java.rmi.MarshalException";
/*    */   static final String UNMARSHAL_EXCEPTION = "java.rmi.UnmarshalException";
/*    */   static final String UNEXPECTED_EXCEPTION = "java.rmi.UnexpectedException";
/*    */   static final long STUB_SERIAL_VERSION_UID = 2L;
/*    */   static final int INTERFACE_HASH_STUB_VERSION = 1;
/*    */ 
/*    */   private Constants()
/*    */   {
/* 39 */     throw new AssertionError();
/*    */   }
/*    */ 
/*    */   static enum StubVersion
/*    */   {
/* 59 */     V1_1, VCOMPAT, V1_2;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.jrmp.Constants
 * JD-Core Version:    0.6.2
 */