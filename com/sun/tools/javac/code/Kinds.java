/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ 
/*     */ public class Kinds
/*     */ {
/*     */   public static final int NIL = 0;
/*     */   public static final int PCK = 1;
/*     */   public static final int TYP = 2;
/*     */   public static final int VAR = 4;
/*     */   public static final int VAL = 12;
/*     */   public static final int MTH = 16;
/*     */   public static final int POLY = 32;
/*     */   public static final int ERR = 63;
/*     */   public static final int AllKinds = 63;
/*     */   public static final int ERRONEOUS = 128;
/*     */   public static final int AMBIGUOUS = 129;
/*     */   public static final int HIDDEN = 130;
/*     */   public static final int STATICERR = 131;
/*     */   public static final int MISSING_ENCL = 132;
/*     */   public static final int ABSENT_VAR = 133;
/*     */   public static final int WRONG_MTHS = 134;
/*     */   public static final int WRONG_MTH = 135;
/*     */   public static final int ABSENT_MTH = 136;
/*     */   public static final int ABSENT_TYP = 137;
/*     */   public static final int WRONG_STATICNESS = 138;
/*     */ 
/*     */   public static KindName kindName(int paramInt)
/*     */   {
/* 141 */     switch (paramInt) { case 1:
/* 142 */       return KindName.PACKAGE;
/*     */     case 2:
/* 143 */       return KindName.CLASS;
/*     */     case 4:
/* 144 */       return KindName.VAR;
/*     */     case 12:
/* 145 */       return KindName.VAL;
/*     */     case 16:
/* 146 */       return KindName.METHOD; }
/* 147 */     throw new AssertionError("Unexpected kind: " + paramInt);
/*     */   }
/*     */ 
/*     */   public static KindName kindName(MemberReferenceTree.ReferenceMode paramReferenceMode)
/*     */   {
/* 152 */     switch (paramReferenceMode) { case INVOKE:
/* 153 */       return KindName.METHOD;
/*     */     case NEW:
/* 154 */       return KindName.CONSTRUCTOR; }
/* 155 */     throw new AssertionError("Unexpected mode: " + paramReferenceMode);
/*     */   }
/*     */ 
/*     */   public static KindName kindName(Symbol paramSymbol)
/*     */   {
/* 162 */     switch (1.$SwitchMap$javax$lang$model$element$ElementKind[paramSymbol.getKind().ordinal()]) {
/*     */     case 1:
/* 164 */       return KindName.PACKAGE;
/*     */     case 2:
/* 167 */       return KindName.ENUM;
/*     */     case 3:
/*     */     case 4:
/* 171 */       return KindName.CLASS;
/*     */     case 5:
/* 174 */       return KindName.INTERFACE;
/*     */     case 6:
/* 177 */       return KindName.TYPEVAR;
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 185 */       return KindName.VAR;
/*     */     case 13:
/* 188 */       return KindName.CONSTRUCTOR;
/*     */     case 14:
/* 191 */       return KindName.METHOD;
/*     */     case 15:
/* 193 */       return KindName.STATIC_INIT;
/*     */     case 16:
/* 195 */       return KindName.INSTANCE_INIT;
/*     */     }
/*     */ 
/* 198 */     if (paramSymbol.kind == 12)
/*     */     {
/* 201 */       return KindName.VAL;
/*     */     }
/* 203 */     throw new AssertionError("Unexpected kind: " + paramSymbol.getKind());
/*     */   }
/*     */ 
/*     */   public static EnumSet<KindName> kindNames(int paramInt)
/*     */   {
/* 210 */     EnumSet localEnumSet = EnumSet.noneOf(KindName.class);
/* 211 */     if ((paramInt & 0xC) != 0)
/* 212 */       localEnumSet.add((paramInt & 0xC) == 4 ? KindName.VAR : KindName.VAL);
/* 213 */     if ((paramInt & 0x10) != 0) localEnumSet.add(KindName.METHOD);
/* 214 */     if ((paramInt & 0x2) != 0) localEnumSet.add(KindName.CLASS);
/* 215 */     if ((paramInt & 0x1) != 0) localEnumSet.add(KindName.PACKAGE);
/* 216 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static KindName typeKindName(Type paramType)
/*     */   {
/* 222 */     if ((paramType.hasTag(TypeTag.TYPEVAR)) || (
/* 223 */       (paramType
/* 223 */       .hasTag(TypeTag.CLASS)) && 
/* 223 */       ((paramType.tsym.flags() & 0x1000000) != 0L)))
/* 224 */       return KindName.BOUND;
/* 225 */     if (paramType.hasTag(TypeTag.PACKAGE))
/* 226 */       return KindName.PACKAGE;
/* 227 */     if ((paramType.tsym.flags_field & 0x2000) != 0L)
/* 228 */       return KindName.ANNOTATION;
/* 229 */     if ((paramType.tsym.flags_field & 0x200) != 0L) {
/* 230 */       return KindName.INTERFACE;
/*     */     }
/* 232 */     return KindName.CLASS;
/*     */   }
/*     */ 
/*     */   public static KindName absentKind(int paramInt)
/*     */   {
/* 239 */     switch (paramInt) {
/*     */     case 133:
/* 241 */       return KindName.VAR;
/*     */     case 134:
/*     */     case 135:
/*     */     case 136:
/*     */     case 138:
/* 243 */       return KindName.METHOD;
/*     */     case 137:
/* 245 */       return KindName.CLASS;
/*     */     }
/* 247 */     throw new AssertionError("Unexpected kind: " + paramInt);
/*     */   }
/*     */ 
/*     */   public static enum KindName
/*     */     implements Formattable
/*     */   {
/* 103 */     ANNOTATION("kindname.annotation"), 
/* 104 */     CONSTRUCTOR("kindname.constructor"), 
/* 105 */     INTERFACE("kindname.interface"), 
/* 106 */     ENUM("kindname.enum"), 
/* 107 */     STATIC("kindname.static"), 
/* 108 */     TYPEVAR("kindname.type.variable"), 
/* 109 */     BOUND("kindname.type.variable.bound"), 
/* 110 */     VAR("kindname.variable"), 
/* 111 */     VAL("kindname.value"), 
/* 112 */     METHOD("kindname.method"), 
/* 113 */     CLASS("kindname.class"), 
/* 114 */     STATIC_INIT("kindname.static.init"), 
/* 115 */     INSTANCE_INIT("kindname.instance.init"), 
/* 116 */     PACKAGE("kindname.package");
/*     */ 
/*     */     private final String name;
/*     */ 
/*     */     private KindName(String paramString) {
/* 121 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 125 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String getKind() {
/* 129 */       return "Kindname";
/*     */     }
/*     */ 
/*     */     public String toString(Locale paramLocale, Messages paramMessages) {
/* 133 */       String str = toString();
/* 134 */       return paramMessages.getLocalizedString(paramLocale, "compiler.misc." + str, new Object[0]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Kinds
 * JD-Core Version:    0.6.2
 */