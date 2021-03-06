/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ public class JFieldVar extends JVar
/*    */   implements JDocCommentable
/*    */ {
/* 39 */   private JDocComment jdoc = null;
/*    */   private final JDefinedClass owner;
/*    */ 
/*    */   JFieldVar(JDefinedClass owner, JMods mods, JType type, String name, JExpression init)
/*    */   {
/* 57 */     super(mods, type, name, init);
/* 58 */     this.owner = owner;
/*    */   }
/*    */ 
/*    */   public void name(String name)
/*    */   {
/* 64 */     if (this.owner.fields.containsKey(name))
/* 65 */       throw new IllegalArgumentException("name " + name + " is already in use");
/* 66 */     String oldName = name();
/* 67 */     super.name(name);
/* 68 */     this.owner.fields.remove(oldName);
/* 69 */     this.owner.fields.put(name, this);
/*    */   }
/*    */ 
/*    */   public JDocComment javadoc()
/*    */   {
/* 79 */     if (this.jdoc == null)
/* 80 */       this.jdoc = new JDocComment(this.owner.owner());
/* 81 */     return this.jdoc;
/*    */   }
/*    */ 
/*    */   public void declare(JFormatter f) {
/* 85 */     if (this.jdoc != null)
/* 86 */       f.g(this.jdoc);
/* 87 */     super.declare(f);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JFieldVar
 * JD-Core Version:    0.6.2
 */