/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSNotation;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class NotationImpl extends DeclarationImpl
/*    */   implements XSNotation
/*    */ {
/*    */   private final String publicId;
/*    */   private final String systemId;
/*    */ 
/*    */   public NotationImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, String _publicId, String _systemId)
/*    */   {
/* 44 */     super(owner, _annon, _loc, _fa, owner.getTargetNamespace(), _name, false);
/*    */ 
/* 46 */     this.publicId = _publicId;
/* 47 */     this.systemId = _systemId;
/*    */   }
/*    */ 
/*    */   public String getPublicId()
/*    */   {
/* 53 */     return this.publicId; } 
/* 54 */   public String getSystemId() { return this.systemId; }
/*    */ 
/*    */   public void visit(XSVisitor visitor) {
/* 57 */     visitor.notation(this);
/*    */   }
/*    */ 
/*    */   public Object apply(XSFunction function) {
/* 61 */     return function.notation(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.NotationImpl
 * JD-Core Version:    0.6.2
 */