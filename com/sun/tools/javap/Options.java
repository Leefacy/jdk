/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.classfile.AccessFlags;
/*    */ import java.util.EnumSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Options
/*    */ {
/*    */   public boolean help;
/*    */   public boolean verbose;
/*    */   public boolean version;
/*    */   public boolean fullVersion;
/*    */   public boolean showFlags;
/*    */   public boolean showLineAndLocalVariableTables;
/*    */   public int showAccess;
/* 81 */   public Set<String> accessOptions = new HashSet();
/* 82 */   public Set<InstructionDetailWriter.Kind> details = EnumSet.noneOf(InstructionDetailWriter.Kind.class);
/*    */   public boolean showDisassembled;
/*    */   public boolean showDescriptors;
/*    */   public boolean showAllAttrs;
/*    */   public boolean showConstants;
/*    */   public boolean sysInfo;
/*    */   public boolean showInnerClasses;
/* 89 */   public int indentWidth = 2;
/* 90 */   public int tabColumn = 40;
/*    */ 
/*    */   public static Options instance(Context paramContext)
/*    */   {
/* 44 */     Options localOptions = (Options)paramContext.get(Options.class);
/* 45 */     if (localOptions == null)
/* 46 */       localOptions = new Options(paramContext);
/* 47 */     return localOptions;
/*    */   }
/*    */ 
/*    */   protected Options(Context paramContext) {
/* 51 */     paramContext.put(Options.class, this);
/*    */   }
/*    */ 
/*    */   public boolean checkAccess(AccessFlags paramAccessFlags)
/*    */   {
/* 59 */     boolean bool1 = paramAccessFlags.is(1);
/* 60 */     boolean bool2 = paramAccessFlags.is(4);
/* 61 */     boolean bool3 = paramAccessFlags.is(2);
/* 62 */     int i = (!bool1) && (!bool2) && (!bool3) ? 1 : 0;
/*    */ 
/* 64 */     if ((this.showAccess == 1) && ((bool2) || (bool3) || (i != 0)))
/* 65 */       return false;
/* 66 */     if ((this.showAccess == 4) && ((bool3) || (i != 0)))
/* 67 */       return false;
/* 68 */     if ((this.showAccess == 0) && (bool3)) {
/* 69 */       return false;
/*    */     }
/* 71 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.Options
 * JD-Core Version:    0.6.2
 */