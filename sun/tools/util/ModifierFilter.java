/*     */ package sun.tools.util;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ public class ModifierFilter extends Modifier
/*     */ {
/*     */   public static final long PACKAGE = -9223372036854775808L;
/*     */   public static final long ALL_ACCESS = -9223372036854775801L;
/*     */   private long oneOf;
/*     */   private long must;
/*     */   private long cannot;
/*     */   private static final int ACCESS_BITS = 7;
/*     */ 
/*     */   public ModifierFilter(long paramLong)
/*     */   {
/*  81 */     this(paramLong, 0L, 0L);
/*     */   }
/*     */ 
/*     */   public ModifierFilter(long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 106 */     this.oneOf = paramLong1;
/* 107 */     this.must = paramLong2;
/* 108 */     this.cannot = paramLong3;
/*     */   }
/*     */ 
/*     */   public boolean checkModifier(int paramInt)
/*     */   {
/* 120 */     long l = (paramInt & 0x7) == 0 ? paramInt | 0x0 : paramInt;
/*     */ 
/* 123 */     return ((this.oneOf == 0L) || ((this.oneOf & l) != 0L)) && ((this.must & l) == this.must) && ((this.cannot & l) == 0L);
/*     */   }
/*     */ 
/*     */   public boolean checkMember(MemberDefinition paramMemberDefinition)
/*     */   {
/* 139 */     return checkModifier(paramMemberDefinition.getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean checkClass(ClassDefinition paramClassDefinition)
/*     */   {
/* 153 */     return checkModifier(paramClassDefinition.getModifiers());
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.util.ModifierFilter
 * JD-Core Version:    0.6.2
 */