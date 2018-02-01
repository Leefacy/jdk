/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.DiagnosticPart;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.PositionKind;
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.file.BaseFileObject;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public final class RawDiagnosticFormatter extends AbstractDiagnosticFormatter
/*     */ {
/*     */   public RawDiagnosticFormatter(Options paramOptions)
/*     */   {
/*  58 */     super(null, new AbstractDiagnosticFormatter.SimpleConfiguration(paramOptions, 
/*  59 */       EnumSet.of(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY, DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS, DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS)));
/*     */   }
/*     */ 
/*     */   public String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/*     */     try
/*     */     {
/*  67 */       StringBuilder localStringBuilder = new StringBuilder();
/*  68 */       if (paramJCDiagnostic.getPosition() != -1L) {
/*  69 */         localStringBuilder.append(formatSource(paramJCDiagnostic, false, null));
/*  70 */         localStringBuilder.append(':');
/*  71 */         localStringBuilder.append(formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.LINE, null));
/*  72 */         localStringBuilder.append(':');
/*  73 */         localStringBuilder.append(formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.COLUMN, null));
/*  74 */         localStringBuilder.append(':');
/*     */       }
/*  76 */       else if ((paramJCDiagnostic.getSource() != null) && (paramJCDiagnostic.getSource().getKind() == JavaFileObject.Kind.CLASS)) {
/*  77 */         localStringBuilder.append(formatSource(paramJCDiagnostic, false, null));
/*  78 */         localStringBuilder.append(":-:-:");
/*     */       }
/*     */       else {
/*  81 */         localStringBuilder.append('-');
/*  82 */       }localStringBuilder.append(' ');
/*  83 */       localStringBuilder.append(formatMessage(paramJCDiagnostic, null));
/*  84 */       if (displaySource(paramJCDiagnostic)) {
/*  85 */         localStringBuilder.append("\n");
/*  86 */         localStringBuilder.append(formatSourceLine(paramJCDiagnostic, 0));
/*     */       }
/*  88 */       return localStringBuilder.toString();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/*  97 */     StringBuilder localStringBuilder = new StringBuilder();
/*  98 */     Collection localCollection = formatArguments(paramJCDiagnostic, paramLocale);
/*  99 */     localStringBuilder.append(localize(null, paramJCDiagnostic.getCode(), localCollection.toArray()));
/* 100 */     if ((paramJCDiagnostic.isMultiline()) && (getConfiguration().getVisible().contains(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS))) {
/* 101 */       List localList = formatSubdiagnostics(paramJCDiagnostic, null);
/* 102 */       if (localList.nonEmpty()) {
/* 103 */         String str1 = "";
/* 104 */         localStringBuilder.append(",{");
/* 105 */         for (String str2 : formatSubdiagnostics(paramJCDiagnostic, null)) {
/* 106 */           localStringBuilder.append(str1);
/* 107 */           localStringBuilder.append("(");
/* 108 */           localStringBuilder.append(str2);
/* 109 */           localStringBuilder.append(")");
/* 110 */           str1 = ",";
/*     */         }
/* 112 */         localStringBuilder.append('}');
/*     */       }
/*     */     }
/* 115 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected String formatArgument(JCDiagnostic paramJCDiagnostic, Object paramObject, Locale paramLocale)
/*     */   {
/*     */     String str;
/* 121 */     if ((paramObject instanceof Formattable)) {
/* 122 */       str = paramObject.toString();
/* 123 */     } else if ((paramObject instanceof JCTree.JCExpression)) {
/* 124 */       JCTree.JCExpression localJCExpression = (JCTree.JCExpression)paramObject;
/* 125 */       str = "@" + localJCExpression.getStartPosition();
/* 126 */     } else if ((paramObject instanceof BaseFileObject)) {
/* 127 */       str = ((BaseFileObject)paramObject).getShortName();
/*     */     } else {
/* 129 */       str = super.formatArgument(paramJCDiagnostic, paramObject, null);
/*     */     }
/* 131 */     return (paramObject instanceof JCDiagnostic) ? "(" + str + ")" : str;
/*     */   }
/*     */ 
/*     */   protected String localize(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 136 */     StringBuilder localStringBuilder = new StringBuilder();
/* 137 */     localStringBuilder.append(paramString);
/* 138 */     String str = ": ";
/* 139 */     for (Object localObject : paramArrayOfObject) {
/* 140 */       localStringBuilder.append(str);
/* 141 */       localStringBuilder.append(localObject);
/* 142 */       str = ", ";
/*     */     }
/* 144 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public boolean isRaw()
/*     */   {
/* 149 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.RawDiagnosticFormatter
 * JD-Core Version:    0.6.2
 */