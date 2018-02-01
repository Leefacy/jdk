/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.bind.annotation.XmlLocation;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ abstract class AbstractDeclarationImpl
/*    */   implements BIDeclaration
/*    */ {
/*    */ 
/*    */   @XmlLocation
/*    */   Locator loc;
/*    */   protected BindInfo parent;
/* 73 */   private boolean isAcknowledged = false;
/*    */ 
/*    */   @Deprecated
/*    */   protected AbstractDeclarationImpl(Locator loc)
/*    */   {
/* 49 */     this.loc = loc;
/*    */   }
/*    */ 
/*    */   protected AbstractDeclarationImpl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Locator getLocation() {
/* 57 */     return this.loc;
/*    */   }
/*    */   public void setParent(BindInfo p) {
/* 60 */     this.parent = p;
/*    */   }
/*    */   protected final XSComponent getOwner() {
/* 63 */     return this.parent.getOwner();
/*    */   }
/*    */   protected final BGMBuilder getBuilder() {
/* 66 */     return this.parent.getBuilder();
/*    */   }
/*    */   protected final JCodeModel getCodeModel() {
/* 69 */     return (JCodeModel)Ring.get(JCodeModel.class);
/*    */   }
/*    */ 
/*    */   public final boolean isAcknowledged()
/*    */   {
/* 75 */     return this.isAcknowledged;
/*    */   }
/*    */   public void onSetOwner() {
/*    */   }
/*    */ 
/*    */   public Collection<BIDeclaration> getChildren() {
/* 81 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   public void markAsAcknowledged() {
/* 85 */     this.isAcknowledged = true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl
 * JD-Core Version:    0.6.2
 */