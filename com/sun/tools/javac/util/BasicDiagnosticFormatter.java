/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.DiagnosticPart;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.PositionKind;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class BasicDiagnosticFormatter extends AbstractDiagnosticFormatter
/*     */ {
/*     */   public BasicDiagnosticFormatter(Options paramOptions, JavacMessages paramJavacMessages)
/*     */   {
/*  76 */     super(paramJavacMessages, new BasicConfiguration(paramOptions));
/*     */   }
/*     */ 
/*     */   public BasicDiagnosticFormatter(JavacMessages paramJavacMessages)
/*     */   {
/*  85 */     super(paramJavacMessages, new BasicConfiguration());
/*     */   }
/*     */ 
/*     */   public String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/*  89 */     if (paramLocale == null)
/*  90 */       paramLocale = this.messages.getCurrentLocale();
/*  91 */     String str = selectFormat(paramJCDiagnostic);
/*  92 */     StringBuilder localStringBuilder = new StringBuilder();
/*  93 */     for (int i = 0; i < str.length(); i++) {
/*  94 */       char c = str.charAt(i);
/*  95 */       int j = 0;
/*  96 */       if ((c == '%') && (i < str.length() - 1)) {
/*  97 */         j = 1;
/*  98 */         c = str.charAt(++i);
/*     */       }
/* 100 */       localStringBuilder.append(j != 0 ? formatMeta(c, paramJCDiagnostic, paramLocale) : String.valueOf(c));
/*     */     }
/* 102 */     if (this.depth == 0) {
/* 103 */       return addSourceLineIfNeeded(paramJCDiagnostic, localStringBuilder.toString());
/*     */     }
/* 105 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 109 */     int i = 0;
/* 110 */     StringBuilder localStringBuilder = new StringBuilder();
/* 111 */     Collection localCollection = formatArguments(paramJCDiagnostic, paramLocale);
/* 112 */     String str1 = localize(paramLocale, paramJCDiagnostic.getCode(), localCollection.toArray());
/* 113 */     String[] arrayOfString = str1.split("\n");
/* 114 */     if (getConfiguration().getVisible().contains(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY)) {
/* 115 */       i += getConfiguration().getIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY);
/* 116 */       localStringBuilder.append(indent(arrayOfString[0], i));
/*     */     }
/* 118 */     if ((arrayOfString.length > 1) && (getConfiguration().getVisible().contains(DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS))) {
/* 119 */       i += getConfiguration().getIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS);
/* 120 */       for (int j = 1; j < arrayOfString.length; j++) {
/* 121 */         localStringBuilder.append("\n" + indent(arrayOfString[j], i));
/*     */       }
/*     */     }
/* 124 */     if ((paramJCDiagnostic.isMultiline()) && (getConfiguration().getVisible().contains(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS))) {
/* 125 */       i += getConfiguration().getIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS);
/* 126 */       for (String str2 : formatSubdiagnostics(paramJCDiagnostic, paramLocale)) {
/* 127 */         localStringBuilder.append("\n" + indent(str2, i));
/*     */       }
/*     */     }
/* 130 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected String addSourceLineIfNeeded(JCDiagnostic paramJCDiagnostic, String paramString) {
/* 134 */     if (!displaySource(paramJCDiagnostic)) {
/* 135 */       return paramString;
/*     */     }
/* 137 */     BasicConfiguration localBasicConfiguration = getConfiguration();
/* 138 */     int i = localBasicConfiguration.getIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE);
/* 139 */     String str = "\n" + formatSourceLine(paramJCDiagnostic, i);
/* 140 */     int j = paramString.indexOf("\n") == -1 ? 1 : 0;
/* 141 */     if ((j != 0) || (getConfiguration().getSourcePosition() == BasicDiagnosticFormatter.BasicConfiguration.SourcePosition.BOTTOM)) {
/* 142 */       return paramString + str;
/*     */     }
/* 144 */     return paramString.replaceFirst("\n", Matcher.quoteReplacement(str) + "\n");
/*     */   }
/*     */ 
/*     */   protected String formatMeta(char paramChar, JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 149 */     switch (paramChar) {
/*     */     case 'b':
/* 151 */       return formatSource(paramJCDiagnostic, false, paramLocale);
/*     */     case 'e':
/* 153 */       return formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.END, paramLocale);
/*     */     case 'f':
/* 155 */       return formatSource(paramJCDiagnostic, true, paramLocale);
/*     */     case 'l':
/* 157 */       return formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.LINE, paramLocale);
/*     */     case 'c':
/* 159 */       return formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.COLUMN, paramLocale);
/*     */     case 'o':
/* 161 */       return formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.OFFSET, paramLocale);
/*     */     case 'p':
/* 163 */       return formatKind(paramJCDiagnostic, paramLocale);
/*     */     case 's':
/* 165 */       return formatPosition(paramJCDiagnostic, DiagnosticFormatter.PositionKind.START, paramLocale);
/*     */     case 't':
/*     */       int i;
/* 168 */       switch (1.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[paramJCDiagnostic.getType().ordinal()]) {
/*     */       case 1:
/* 170 */         i = 0;
/* 171 */         break;
/*     */       case 2:
/* 173 */         i = paramJCDiagnostic.getIntPosition() == -1 ? 1 : 0;
/* 174 */         break;
/*     */       default:
/* 176 */         i = 1;
/*     */       }
/* 178 */       if (i != 0) {
/* 179 */         return formatKind(paramJCDiagnostic, paramLocale);
/*     */       }
/* 181 */       return "";
/*     */     case 'm':
/* 184 */       return formatMessage(paramJCDiagnostic, paramLocale);
/*     */     case 'L':
/* 186 */       return formatLintCategory(paramJCDiagnostic, paramLocale);
/*     */     case '_':
/* 188 */       return " ";
/*     */     case '%':
/* 190 */       return "%";
/*     */     }
/* 192 */     return String.valueOf(paramChar);
/*     */   }
/*     */ 
/*     */   private String selectFormat(JCDiagnostic paramJCDiagnostic)
/*     */   {
/* 197 */     DiagnosticSource localDiagnosticSource = paramJCDiagnostic.getDiagnosticSource();
/* 198 */     String str = getConfiguration().getFormat(BasicDiagnosticFormatter.BasicConfiguration.BasicFormatKind.DEFAULT_NO_POS_FORMAT);
/* 199 */     if ((localDiagnosticSource != null) && (localDiagnosticSource != DiagnosticSource.NO_SOURCE)) {
/* 200 */       if (paramJCDiagnostic.getIntPosition() != -1)
/* 201 */         str = getConfiguration().getFormat(BasicDiagnosticFormatter.BasicConfiguration.BasicFormatKind.DEFAULT_POS_FORMAT);
/* 202 */       else if ((localDiagnosticSource.getFile() != null) && 
/* 203 */         (localDiagnosticSource
/* 203 */         .getFile().getKind() == JavaFileObject.Kind.CLASS)) {
/* 204 */         str = getConfiguration().getFormat(BasicDiagnosticFormatter.BasicConfiguration.BasicFormatKind.DEFAULT_CLASS_FORMAT);
/*     */       }
/*     */     }
/* 207 */     return str;
/*     */   }
/*     */ 
/*     */   public BasicConfiguration getConfiguration()
/*     */   {
/* 213 */     return (BasicConfiguration)super.getConfiguration();
/*     */   }
/*     */ 
/*     */   public static class BasicConfiguration extends AbstractDiagnosticFormatter.SimpleConfiguration {
/*     */     protected Map<DiagnosticFormatter.Configuration.DiagnosticPart, Integer> indentationLevels;
/*     */     protected Map<BasicFormatKind, String> availableFormats;
/*     */     protected SourcePosition sourcePosition;
/*     */ 
/*     */     public BasicConfiguration(Options paramOptions) {
/* 224 */       super(EnumSet.of(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY, DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS, DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS, DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE));
/*     */ 
/* 228 */       initFormat();
/* 229 */       initIndentation();
/* 230 */       if (paramOptions.isSet("oldDiags"))
/* 231 */         initOldFormat();
/* 232 */       String str1 = paramOptions.get("diagsFormat");
/* 233 */       if (str1 != null) {
/* 234 */         if (str1.equals("OLD"))
/* 235 */           initOldFormat();
/*     */         else
/* 237 */           initFormats(str1);
/*     */       }
/* 239 */       String str2 = null;
/* 240 */       if (((str2 = paramOptions.get("sourcePosition")) != null) && 
/* 241 */         (str2
/* 241 */         .equals("bottom")))
/*     */       {
/* 242 */         setSourcePosition(SourcePosition.BOTTOM);
/*     */       }
/* 244 */       else setSourcePosition(SourcePosition.AFTER_SUMMARY);
/* 245 */       String str3 = paramOptions.get("diagsIndentation");
/* 246 */       if (str3 != null) {
/* 247 */         String[] arrayOfString = str3.split("\\|");
/*     */         try {
/* 249 */           switch (arrayOfString.length) {
/*     */           case 5:
/* 251 */             setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.JLS, 
/* 252 */               Integer.parseInt(arrayOfString[4]));
/*     */           case 4:
/* 254 */             setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS, 
/* 255 */               Integer.parseInt(arrayOfString[3]));
/*     */           case 3:
/* 257 */             setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, 
/* 258 */               Integer.parseInt(arrayOfString[2]));
/*     */           case 2:
/* 260 */             setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS, 
/* 261 */               Integer.parseInt(arrayOfString[1]));
/*     */           }
/*     */ 
/* 263 */           setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY, 
/* 264 */             Integer.parseInt(arrayOfString[0]));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException)
/*     */         {
/* 268 */           initIndentation();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public BasicConfiguration() {
/* 274 */       super();
/*     */ 
/* 278 */       initFormat();
/* 279 */       initIndentation();
/*     */     }
/*     */ 
/*     */     private void initFormat() {
/* 283 */       initFormats("%f:%l:%_%p%L%m", "%p%L%m", "%f:%_%p%L%m");
/*     */     }
/*     */ 
/*     */     private void initOldFormat() {
/* 287 */       initFormats("%f:%l:%_%t%L%m", "%p%L%m", "%f:%_%t%L%m");
/*     */     }
/*     */ 
/*     */     private void initFormats(String paramString1, String paramString2, String paramString3) {
/* 291 */       this.availableFormats = new EnumMap(BasicFormatKind.class);
/* 292 */       setFormat(BasicFormatKind.DEFAULT_POS_FORMAT, paramString1);
/* 293 */       setFormat(BasicFormatKind.DEFAULT_NO_POS_FORMAT, paramString2);
/* 294 */       setFormat(BasicFormatKind.DEFAULT_CLASS_FORMAT, paramString3);
/*     */     }
/*     */ 
/*     */     private void initFormats(String paramString)
/*     */     {
/* 299 */       String[] arrayOfString = paramString.split("\\|");
/* 300 */       switch (arrayOfString.length) {
/*     */       case 3:
/* 302 */         setFormat(BasicFormatKind.DEFAULT_CLASS_FORMAT, arrayOfString[2]);
/*     */       case 2:
/* 304 */         setFormat(BasicFormatKind.DEFAULT_NO_POS_FORMAT, arrayOfString[1]);
/*     */       }
/* 306 */       setFormat(BasicFormatKind.DEFAULT_POS_FORMAT, arrayOfString[0]);
/*     */     }
/*     */ 
/*     */     private void initIndentation()
/*     */     {
/* 311 */       this.indentationLevels = new HashMap();
/* 312 */       setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUMMARY, 0);
/* 313 */       setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS, 2);
/* 314 */       setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS, 4);
/* 315 */       setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, 0);
/*     */     }
/*     */ 
/*     */     public int getIndentation(DiagnosticFormatter.Configuration.DiagnosticPart paramDiagnosticPart)
/*     */     {
/* 325 */       return ((Integer)this.indentationLevels.get(paramDiagnosticPart)).intValue();
/*     */     }
/*     */ 
/*     */     public void setIndentation(DiagnosticFormatter.Configuration.DiagnosticPart paramDiagnosticPart, int paramInt)
/*     */     {
/* 336 */       this.indentationLevels.put(paramDiagnosticPart, Integer.valueOf(paramInt));
/*     */     }
/*     */ 
/*     */     public void setSourcePosition(SourcePosition paramSourcePosition)
/*     */     {
/* 345 */       this.sourcePosition = paramSourcePosition;
/*     */     }
/*     */ 
/*     */     public SourcePosition getSourcePosition()
/*     */     {
/* 354 */       return this.sourcePosition;
/*     */     }
/*     */ 
/*     */     public void setFormat(BasicFormatKind paramBasicFormatKind, String paramString)
/*     */     {
/* 381 */       this.availableFormats.put(paramBasicFormatKind, paramString);
/*     */     }
/*     */ 
/*     */     public String getFormat(BasicFormatKind paramBasicFormatKind)
/*     */     {
/* 390 */       return (String)this.availableFormats.get(paramBasicFormatKind);
/*     */     }
/*     */ 
/*     */     public static enum BasicFormatKind
/*     */     {
/* 401 */       DEFAULT_POS_FORMAT, 
/*     */ 
/* 405 */       DEFAULT_NO_POS_FORMAT, 
/*     */ 
/* 409 */       DEFAULT_CLASS_FORMAT;
/*     */     }
/*     */ 
/*     */     public static enum SourcePosition
/*     */     {
/* 366 */       BOTTOM, 
/*     */ 
/* 371 */       AFTER_SUMMARY;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.BasicDiagnosticFormatter
 * JD-Core Version:    0.6.2
 */