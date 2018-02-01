/*    */ package com.sun.istack.internal.tools;
/*    */ 
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class MaskingClassLoader extends ClassLoader
/*    */ {
/*    */   private final String[] masks;
/*    */ 
/*    */   public MaskingClassLoader(String[] masks)
/*    */   {
/* 44 */     this.masks = masks;
/*    */   }
/*    */ 
/*    */   public MaskingClassLoader(Collection<String> masks) {
/* 48 */     this((String[])masks.toArray(new String[masks.size()]));
/*    */   }
/*    */ 
/*    */   public MaskingClassLoader(ClassLoader parent, String[] masks) {
/* 52 */     super(parent);
/* 53 */     this.masks = masks;
/*    */   }
/*    */ 
/*    */   public MaskingClassLoader(ClassLoader parent, Collection<String> masks) {
/* 57 */     this(parent, (String[])masks.toArray(new String[masks.size()]));
/*    */   }
/*    */ 
/*    */   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
/*    */   {
/* 62 */     for (String mask : this.masks) {
/* 63 */       if (name.startsWith(mask)) {
/* 64 */         throw new ClassNotFoundException();
/*    */       }
/*    */     }
/* 67 */     return super.loadClass(name, resolve);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.istack.internal.tools.MaskingClassLoader
 * JD-Core Version:    0.6.2
 */