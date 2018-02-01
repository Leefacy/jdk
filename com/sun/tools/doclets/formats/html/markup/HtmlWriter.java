/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class HtmlWriter
/*     */ {
/*     */   protected String winTitle;
/*     */   protected Configuration configuration;
/*     */   protected boolean memberDetailsListPrinted;
/*     */   protected final String[] profileTableHeader;
/*     */   protected final String[] packageTableHeader;
/*     */   protected final String useTableSummary;
/*     */   protected final String modifierTypeHeader;
/*     */   public final Content overviewLabel;
/*     */   public final Content defaultPackageLabel;
/*     */   public final Content packageLabel;
/*     */   public final Content profileLabel;
/*     */   public final Content useLabel;
/*     */   public final Content prevLabel;
/*     */   public final Content nextLabel;
/*     */   public final Content prevclassLabel;
/*     */   public final Content nextclassLabel;
/*     */   public final Content summaryLabel;
/*     */   public final Content detailLabel;
/*     */   public final Content framesLabel;
/*     */   public final Content noframesLabel;
/*     */   public final Content treeLabel;
/*     */   public final Content classLabel;
/*     */   public final Content deprecatedLabel;
/*     */   public final Content deprecatedPhrase;
/*     */   public final Content allclassesLabel;
/*     */   public final Content allpackagesLabel;
/*     */   public final Content allprofilesLabel;
/*     */   public final Content indexLabel;
/*     */   public final Content helpLabel;
/*     */   public final Content seeLabel;
/*     */   public final Content descriptionLabel;
/*     */   public final Content prevpackageLabel;
/*     */   public final Content nextpackageLabel;
/*     */   public final Content prevprofileLabel;
/*     */   public final Content nextprofileLabel;
/*     */   public final Content packagesLabel;
/*     */   public final Content profilesLabel;
/*     */   public final Content methodDetailsLabel;
/*     */   public final Content annotationTypeDetailsLabel;
/*     */   public final Content fieldDetailsLabel;
/*     */   public final Content propertyDetailsLabel;
/*     */   public final Content constructorDetailsLabel;
/*     */   public final Content enumConstantsDetailsLabel;
/*     */   public final Content specifiedByLabel;
/*     */   public final Content overridesLabel;
/*     */   public final Content descfrmClassLabel;
/*     */   public final Content descfrmInterfaceLabel;
/*     */   private final Writer writer;
/*     */   private Content script;
/*     */ 
/*     */   public HtmlWriter(Configuration paramConfiguration, DocPath paramDocPath)
/*     */     throws IOException, UnsupportedEncodingException
/*     */   {
/* 183 */     this.writer = DocFile.createFileForOutput(paramConfiguration, paramDocPath).openWriter();
/* 184 */     this.configuration = paramConfiguration;
/* 185 */     this.memberDetailsListPrinted = false;
/* 186 */     this.profileTableHeader = new String[] { paramConfiguration
/* 187 */       .getText("doclet.Profile"), 
/* 187 */       paramConfiguration
/* 188 */       .getText("doclet.Description") };
/*     */ 
/* 190 */     this.packageTableHeader = new String[] { paramConfiguration
/* 191 */       .getText("doclet.Package"), 
/* 191 */       paramConfiguration
/* 192 */       .getText("doclet.Description") };
/*     */ 
/* 194 */     this.useTableSummary = paramConfiguration.getText("doclet.Use_Table_Summary", paramConfiguration
/* 195 */       .getText("doclet.packages"));
/*     */ 
/* 196 */     this.modifierTypeHeader = paramConfiguration.getText("doclet.0_and_1", paramConfiguration
/* 197 */       .getText("doclet.Modifier"), 
/* 197 */       paramConfiguration
/* 198 */       .getText("doclet.Type"));
/*     */ 
/* 199 */     this.overviewLabel = getResource("doclet.Overview");
/* 200 */     this.defaultPackageLabel = new StringContent("<Unnamed>");
/* 201 */     this.packageLabel = getResource("doclet.Package");
/* 202 */     this.profileLabel = getResource("doclet.Profile");
/* 203 */     this.useLabel = getResource("doclet.navClassUse");
/* 204 */     this.prevLabel = getResource("doclet.Prev");
/* 205 */     this.nextLabel = getResource("doclet.Next");
/* 206 */     this.prevclassLabel = getNonBreakResource("doclet.Prev_Class");
/* 207 */     this.nextclassLabel = getNonBreakResource("doclet.Next_Class");
/* 208 */     this.summaryLabel = getResource("doclet.Summary");
/* 209 */     this.detailLabel = getResource("doclet.Detail");
/* 210 */     this.framesLabel = getResource("doclet.Frames");
/* 211 */     this.noframesLabel = getNonBreakResource("doclet.No_Frames");
/* 212 */     this.treeLabel = getResource("doclet.Tree");
/* 213 */     this.classLabel = getResource("doclet.Class");
/* 214 */     this.deprecatedLabel = getResource("doclet.navDeprecated");
/* 215 */     this.deprecatedPhrase = getResource("doclet.Deprecated");
/* 216 */     this.allclassesLabel = getNonBreakResource("doclet.All_Classes");
/* 217 */     this.allpackagesLabel = getNonBreakResource("doclet.All_Packages");
/* 218 */     this.allprofilesLabel = getNonBreakResource("doclet.All_Profiles");
/* 219 */     this.indexLabel = getResource("doclet.Index");
/* 220 */     this.helpLabel = getResource("doclet.Help");
/* 221 */     this.seeLabel = getResource("doclet.See");
/* 222 */     this.descriptionLabel = getResource("doclet.Description");
/* 223 */     this.prevpackageLabel = getNonBreakResource("doclet.Prev_Package");
/* 224 */     this.nextpackageLabel = getNonBreakResource("doclet.Next_Package");
/* 225 */     this.prevprofileLabel = getNonBreakResource("doclet.Prev_Profile");
/* 226 */     this.nextprofileLabel = getNonBreakResource("doclet.Next_Profile");
/* 227 */     this.packagesLabel = getResource("doclet.Packages");
/* 228 */     this.profilesLabel = getResource("doclet.Profiles");
/* 229 */     this.methodDetailsLabel = getResource("doclet.Method_Detail");
/* 230 */     this.annotationTypeDetailsLabel = getResource("doclet.Annotation_Type_Member_Detail");
/* 231 */     this.fieldDetailsLabel = getResource("doclet.Field_Detail");
/* 232 */     this.propertyDetailsLabel = getResource("doclet.Property_Detail");
/* 233 */     this.constructorDetailsLabel = getResource("doclet.Constructor_Detail");
/* 234 */     this.enumConstantsDetailsLabel = getResource("doclet.Enum_Constant_Detail");
/* 235 */     this.specifiedByLabel = getResource("doclet.Specified_By");
/* 236 */     this.overridesLabel = getResource("doclet.Overrides");
/* 237 */     this.descfrmClassLabel = getResource("doclet.Description_From_Class");
/* 238 */     this.descfrmInterfaceLabel = getResource("doclet.Description_From_Interface");
/*     */   }
/*     */ 
/*     */   public void write(Content paramContent) throws IOException {
/* 242 */     paramContent.write(this.writer, true);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 246 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString)
/*     */   {
/* 256 */     return this.configuration.getResource(paramString);
/*     */   }
/*     */ 
/*     */   public Content getNonBreakResource(String paramString)
/*     */   {
/* 267 */     String str = this.configuration.getText(paramString);
/* 268 */     Content localContent = this.configuration.newContent();
/* 269 */     int i = 0;
/*     */     int j;
/* 271 */     while ((j = str.indexOf(" ", i)) != -1) {
/* 272 */       localContent.addContent(str.substring(i, j));
/* 273 */       localContent.addContent(RawHtml.nbsp);
/* 274 */       i = j + 1;
/*     */     }
/* 276 */     localContent.addContent(str.substring(i));
/* 277 */     return localContent;
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString, Object paramObject)
/*     */   {
/* 288 */     return this.configuration.getResource(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 300 */     return this.configuration.getResource(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   protected HtmlTree getWinTitleScript()
/*     */   {
/* 309 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 310 */     if ((this.winTitle != null) && (this.winTitle.length() > 0)) {
/* 311 */       localHtmlTree.addAttr(HtmlAttr.TYPE, "text/javascript");
/*     */ 
/* 315 */       String str = "<!--" + DocletConstants.NL + "    try {" + DocletConstants.NL + "        if (location.href.indexOf('is-external=true') == -1) {" + DocletConstants.NL + "            parent.document.title=\"" + 
/* 315 */         escapeJavaScriptChars(this.winTitle) + 
/* 315 */         "\";" + DocletConstants.NL + "        }" + DocletConstants.NL + "    }" + DocletConstants.NL + "    catch(err) {" + DocletConstants.NL + "    }" + DocletConstants.NL + "//-->" + DocletConstants.NL;
/*     */ 
/* 321 */       RawHtml localRawHtml = new RawHtml(str);
/* 322 */       localHtmlTree.addContent(localRawHtml);
/*     */     }
/* 324 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   private static String escapeJavaScriptChars(String paramString)
/*     */   {
/* 334 */     StringBuilder localStringBuilder = new StringBuilder();
/* 335 */     for (int i = 0; i < paramString.length(); i++) {
/* 336 */       char c = paramString.charAt(i);
/* 337 */       switch (c) {
/*     */       case '\b':
/* 339 */         localStringBuilder.append("\\b");
/* 340 */         break;
/*     */       case '\t':
/* 342 */         localStringBuilder.append("\\t");
/* 343 */         break;
/*     */       case '\n':
/* 345 */         localStringBuilder.append("\\n");
/* 346 */         break;
/*     */       case '\f':
/* 348 */         localStringBuilder.append("\\f");
/* 349 */         break;
/*     */       case '\r':
/* 351 */         localStringBuilder.append("\\r");
/* 352 */         break;
/*     */       case '"':
/* 354 */         localStringBuilder.append("\\\"");
/* 355 */         break;
/*     */       case '\'':
/* 357 */         localStringBuilder.append("\\'");
/* 358 */         break;
/*     */       case '\\':
/* 360 */         localStringBuilder.append("\\\\");
/* 361 */         break;
/*     */       default:
/* 363 */         if ((c < ' ') || (c >= ''))
/* 364 */           localStringBuilder.append(String.format("\\u%04X", new Object[] { Integer.valueOf(c) }));
/*     */         else {
/* 366 */           localStringBuilder.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 371 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected Content getFramesetJavaScript()
/*     */   {
/* 380 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 381 */     localHtmlTree.addAttr(HtmlAttr.TYPE, "text/javascript");
/* 382 */     String str = DocletConstants.NL + "    targetPage = \"\" + window.location.search;" + DocletConstants.NL + "    if (targetPage != \"\" && targetPage != \"undefined\")" + DocletConstants.NL + "        targetPage = targetPage.substring(1);" + DocletConstants.NL + "    if (targetPage.indexOf(\":\") != -1 || (targetPage != \"\" && !validURL(targetPage)))" + DocletConstants.NL + "        targetPage = \"undefined\";" + DocletConstants.NL + "    function validURL(url) {" + DocletConstants.NL + "        try {" + DocletConstants.NL + "            url = decodeURIComponent(url);" + DocletConstants.NL + "        }" + DocletConstants.NL + "        catch (error) {" + DocletConstants.NL + "            return false;" + DocletConstants.NL + "        }" + DocletConstants.NL + "        var pos = url.indexOf(\".html\");" + DocletConstants.NL + "        if (pos == -1 || pos != url.length - 5)" + DocletConstants.NL + "            return false;" + DocletConstants.NL + "        var allowNumber = false;" + DocletConstants.NL + "        var allowSep = false;" + DocletConstants.NL + "        var seenDot = false;" + DocletConstants.NL + "        for (var i = 0; i < url.length - 5; i++) {" + DocletConstants.NL + "            var ch = url.charAt(i);" + DocletConstants.NL + "            if ('a' <= ch && ch <= 'z' ||" + DocletConstants.NL + "                    'A' <= ch && ch <= 'Z' ||" + DocletConstants.NL + "                    ch == '$' ||" + DocletConstants.NL + "                    ch == '_' ||" + DocletConstants.NL + "                    ch.charCodeAt(0) > 127) {" + DocletConstants.NL + "                allowNumber = true;" + DocletConstants.NL + "                allowSep = true;" + DocletConstants.NL + "            } else if ('0' <= ch && ch <= '9'" + DocletConstants.NL + "                    || ch == '-') {" + DocletConstants.NL + "                if (!allowNumber)" + DocletConstants.NL + "                     return false;" + DocletConstants.NL + "            } else if (ch == '/' || ch == '.') {" + DocletConstants.NL + "                if (!allowSep)" + DocletConstants.NL + "                    return false;" + DocletConstants.NL + "                allowNumber = false;" + DocletConstants.NL + "                allowSep = false;" + DocletConstants.NL + "                if (ch == '.')" + DocletConstants.NL + "                     seenDot = true;" + DocletConstants.NL + "                if (ch == '/' && seenDot)" + DocletConstants.NL + "                     return false;" + DocletConstants.NL + "            } else {" + DocletConstants.NL + "                return false;" + DocletConstants.NL + "            }" + DocletConstants.NL + "        }" + DocletConstants.NL + "        return true;" + DocletConstants.NL + "    }" + DocletConstants.NL + "    function loadFrames() {" + DocletConstants.NL + "        if (targetPage != \"\" && targetPage != \"undefined\")" + DocletConstants.NL + "             top.classFrame.location = top.targetPage;" + DocletConstants.NL + "    }" + DocletConstants.NL;
/*     */ 
/* 433 */     RawHtml localRawHtml = new RawHtml(str);
/* 434 */     localHtmlTree.addContent(localRawHtml);
/* 435 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public HtmlTree getBody(boolean paramBoolean, String paramString)
/*     */   {
/* 446 */     HtmlTree localHtmlTree1 = new HtmlTree(HtmlTag.BODY);
/*     */ 
/* 448 */     this.winTitle = paramString;
/*     */ 
/* 451 */     if (paramBoolean) {
/* 452 */       this.script = getWinTitleScript();
/* 453 */       localHtmlTree1.addContent(this.script);
/* 454 */       HtmlTree localHtmlTree2 = HtmlTree.NOSCRIPT(
/* 455 */         HtmlTree.DIV(getResource("doclet.No_Script_Message")));
/*     */ 
/* 456 */       localHtmlTree1.addContent(localHtmlTree2);
/*     */     }
/* 458 */     return localHtmlTree1;
/*     */   }
/*     */ 
/*     */   public void generateMethodTypesScript(Map<String, Integer> paramMap, Set<MethodTypes> paramSet)
/*     */   {
/* 469 */     String str = "";
/* 470 */     StringBuilder localStringBuilder = new StringBuilder("var methods = {");
/* 471 */     for (Iterator localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) { localObject = (Map.Entry)localIterator.next();
/* 472 */       localStringBuilder.append(str);
/* 473 */       str = ",";
/* 474 */       localStringBuilder.append("\"")
/* 475 */         .append((String)((Map.Entry)localObject)
/* 475 */         .getKey())
/* 476 */         .append("\":")
/* 477 */         .append(((Map.Entry)localObject)
/* 477 */         .getValue());
/*     */     }
/* 482 */     Object localObject;
/* 479 */     localStringBuilder.append("};").append(DocletConstants.NL);
/* 480 */     str = "";
/* 481 */     localStringBuilder.append("var tabs = {");
/* 482 */     for (localIterator = paramSet.iterator(); localIterator.hasNext(); ) { localObject = (MethodTypes)localIterator.next();
/* 483 */       localStringBuilder.append(str);
/* 484 */       str = ",";
/* 485 */       localStringBuilder.append(((MethodTypes)localObject).value())
/* 486 */         .append(":")
/* 487 */         .append("[")
/* 488 */         .append("\"")
/* 489 */         .append(((MethodTypes)localObject)
/* 489 */         .tabId())
/* 490 */         .append("\"")
/* 491 */         .append(str)
/* 492 */         .append("\"")
/* 493 */         .append(this.configuration
/* 493 */         .getText(((MethodTypes)localObject)
/* 493 */         .resourceKey()))
/* 494 */         .append("\"]");
/*     */     }
/*     */ 
/* 496 */     localStringBuilder.append("};")
/* 497 */       .append(DocletConstants.NL);
/*     */ 
/* 498 */     addStyles(HtmlStyle.altColor, localStringBuilder);
/* 499 */     addStyles(HtmlStyle.rowColor, localStringBuilder);
/* 500 */     addStyles(HtmlStyle.tableTab, localStringBuilder);
/* 501 */     addStyles(HtmlStyle.activeTableTab, localStringBuilder);
/* 502 */     this.script.addContent(new RawHtml(localStringBuilder.toString()));
/*     */   }
/*     */ 
/*     */   public void addStyles(HtmlStyle paramHtmlStyle, StringBuilder paramStringBuilder)
/*     */   {
/* 512 */     paramStringBuilder.append("var ").append(paramHtmlStyle).append(" = \"").append(paramHtmlStyle)
/* 513 */       .append("\";")
/* 513 */       .append(DocletConstants.NL);
/*     */   }
/*     */ 
/*     */   public HtmlTree getTitle()
/*     */   {
/* 522 */     HtmlTree localHtmlTree = HtmlTree.TITLE(new StringContent(this.winTitle));
/* 523 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public String codeText(String paramString) {
/* 527 */     return "<code>" + paramString + "</code>";
/*     */   }
/*     */ 
/*     */   public Content getSpace()
/*     */   {
/* 534 */     return RawHtml.nbsp;
/*     */   }
/*     */ 
/*     */   public String getModifierTypeHeader()
/*     */   {
/* 541 */     return this.modifierTypeHeader;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.HtmlWriter
 * JD-Core Version:    0.6.2
 */