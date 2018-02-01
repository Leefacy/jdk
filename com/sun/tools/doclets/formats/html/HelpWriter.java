/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class HelpWriter extends HtmlDocletWriter
/*     */ {
/*     */   public HelpWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath)
/*     */     throws IOException
/*     */   {
/*  53 */     super(paramConfigurationImpl, paramDocPath);
/*     */   }
/*     */ 
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl)
/*     */   {
/*  65 */     DocPath localDocPath = DocPath.empty;
/*     */     try {
/*  67 */       localDocPath = DocPaths.HELP_DOC;
/*  68 */       HelpWriter localHelpWriter = new HelpWriter(paramConfigurationImpl, localDocPath);
/*  69 */       localHelpWriter.generateHelpFile();
/*  70 */       localHelpWriter.close();
/*     */     } catch (IOException localIOException) {
/*  72 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { localIOException
/*  74 */         .toString(), localDocPath });
/*  75 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateHelpFile()
/*     */     throws IOException
/*     */   {
/*  83 */     String str = this.configuration.getText("doclet.Window_Help_title");
/*  84 */     HtmlTree localHtmlTree = getBody(true, getWindowTitle(str));
/*  85 */     addTop(localHtmlTree);
/*  86 */     addNavLinks(true, localHtmlTree);
/*  87 */     addHelpFileContents(localHtmlTree);
/*  88 */     addNavLinks(false, localHtmlTree);
/*  89 */     addBottom(localHtmlTree);
/*  90 */     printHtmlDocument(null, true, localHtmlTree);
/*     */   }
/*     */ 
/*     */   protected void addHelpFileContents(Content paramContent)
/*     */   {
/* 102 */     HtmlTree localHtmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, 
/* 103 */       getResource("doclet.Help_line_1"));
/*     */ 
/* 104 */     HtmlTree localHtmlTree2 = HtmlTree.DIV(HtmlStyle.header, localHtmlTree1);
/* 105 */     HtmlTree localHtmlTree3 = HtmlTree.DIV(HtmlStyle.subTitle, 
/* 106 */       getResource("doclet.Help_line_2"));
/*     */ 
/* 107 */     localHtmlTree2.addContent(localHtmlTree3);
/* 108 */     paramContent.addContent(localHtmlTree2);
/* 109 */     HtmlTree localHtmlTree4 = new HtmlTree(HtmlTag.UL);
/* 110 */     localHtmlTree4.addStyle(HtmlStyle.blockList);
/* 111 */     if (this.configuration.createoverview) {
/* 112 */       localHtmlTree5 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 113 */         getResource("doclet.Overview"));
/*     */ 
/* 114 */       localHtmlTree6 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree5);
/* 115 */       localContent1 = getResource("doclet.Help_line_3", 
/* 116 */         getHyperLink(DocPaths.OVERVIEW_SUMMARY, this.configuration
/* 117 */         .getText("doclet.Overview")));
/*     */ 
/* 118 */       localHtmlTree7 = HtmlTree.P(localContent1);
/* 119 */       localHtmlTree6.addContent(localHtmlTree7);
/* 120 */       localHtmlTree4.addContent(localHtmlTree6);
/*     */     }
/* 122 */     HtmlTree localHtmlTree5 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 123 */       getResource("doclet.Package"));
/*     */ 
/* 124 */     HtmlTree localHtmlTree6 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree5);
/* 125 */     Content localContent1 = getResource("doclet.Help_line_4");
/* 126 */     HtmlTree localHtmlTree7 = HtmlTree.P(localContent1);
/* 127 */     localHtmlTree6.addContent(localHtmlTree7);
/* 128 */     HtmlTree localHtmlTree8 = new HtmlTree(HtmlTag.UL);
/* 129 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 130 */       getResource("doclet.Interfaces_Italic")));
/*     */ 
/* 131 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 132 */       getResource("doclet.Classes")));
/*     */ 
/* 133 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 134 */       getResource("doclet.Enums")));
/*     */ 
/* 135 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 136 */       getResource("doclet.Exceptions")));
/*     */ 
/* 137 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 138 */       getResource("doclet.Errors")));
/*     */ 
/* 139 */     localHtmlTree8.addContent(HtmlTree.LI(
/* 140 */       getResource("doclet.AnnotationTypes")));
/*     */ 
/* 141 */     localHtmlTree6.addContent(localHtmlTree8);
/* 142 */     localHtmlTree4.addContent(localHtmlTree6);
/* 143 */     HtmlTree localHtmlTree9 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 144 */       getResource("doclet.Help_line_5"));
/*     */ 
/* 145 */     HtmlTree localHtmlTree10 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree9);
/* 146 */     Content localContent2 = getResource("doclet.Help_line_6");
/* 147 */     HtmlTree localHtmlTree11 = HtmlTree.P(localContent2);
/* 148 */     localHtmlTree10.addContent(localHtmlTree11);
/* 149 */     HtmlTree localHtmlTree12 = new HtmlTree(HtmlTag.UL);
/* 150 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 151 */       getResource("doclet.Help_line_7")));
/*     */ 
/* 152 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 153 */       getResource("doclet.Help_line_8")));
/*     */ 
/* 154 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 155 */       getResource("doclet.Help_line_9")));
/*     */ 
/* 156 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 157 */       getResource("doclet.Help_line_10")));
/*     */ 
/* 158 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 159 */       getResource("doclet.Help_line_11")));
/*     */ 
/* 160 */     localHtmlTree12.addContent(HtmlTree.LI(
/* 161 */       getResource("doclet.Help_line_12")));
/*     */ 
/* 162 */     localHtmlTree10.addContent(localHtmlTree12);
/* 163 */     HtmlTree localHtmlTree13 = new HtmlTree(HtmlTag.UL);
/* 164 */     localHtmlTree13.addContent(HtmlTree.LI(
/* 165 */       getResource("doclet.Nested_Class_Summary")));
/*     */ 
/* 166 */     localHtmlTree13.addContent(HtmlTree.LI(
/* 167 */       getResource("doclet.Field_Summary")));
/*     */ 
/* 168 */     localHtmlTree13.addContent(HtmlTree.LI(
/* 169 */       getResource("doclet.Constructor_Summary")));
/*     */ 
/* 170 */     localHtmlTree13.addContent(HtmlTree.LI(
/* 171 */       getResource("doclet.Method_Summary")));
/*     */ 
/* 172 */     localHtmlTree10.addContent(localHtmlTree13);
/* 173 */     HtmlTree localHtmlTree14 = new HtmlTree(HtmlTag.UL);
/* 174 */     localHtmlTree14.addContent(HtmlTree.LI(
/* 175 */       getResource("doclet.Field_Detail")));
/*     */ 
/* 176 */     localHtmlTree14.addContent(HtmlTree.LI(
/* 177 */       getResource("doclet.Constructor_Detail")));
/*     */ 
/* 178 */     localHtmlTree14.addContent(HtmlTree.LI(
/* 179 */       getResource("doclet.Method_Detail")));
/*     */ 
/* 180 */     localHtmlTree10.addContent(localHtmlTree14);
/* 181 */     Content localContent3 = getResource("doclet.Help_line_13");
/* 182 */     HtmlTree localHtmlTree15 = HtmlTree.P(localContent3);
/* 183 */     localHtmlTree10.addContent(localHtmlTree15);
/* 184 */     localHtmlTree4.addContent(localHtmlTree10);
/*     */ 
/* 186 */     HtmlTree localHtmlTree16 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 187 */       getResource("doclet.AnnotationType"));
/*     */ 
/* 188 */     HtmlTree localHtmlTree17 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree16);
/* 189 */     Content localContent4 = getResource("doclet.Help_annotation_type_line_1");
/* 190 */     HtmlTree localHtmlTree18 = HtmlTree.P(localContent4);
/* 191 */     localHtmlTree17.addContent(localHtmlTree18);
/* 192 */     HtmlTree localHtmlTree19 = new HtmlTree(HtmlTag.UL);
/* 193 */     localHtmlTree19.addContent(HtmlTree.LI(
/* 194 */       getResource("doclet.Help_annotation_type_line_2")));
/*     */ 
/* 195 */     localHtmlTree19.addContent(HtmlTree.LI(
/* 196 */       getResource("doclet.Help_annotation_type_line_3")));
/*     */ 
/* 197 */     localHtmlTree19.addContent(HtmlTree.LI(
/* 198 */       getResource("doclet.Annotation_Type_Required_Member_Summary")));
/*     */ 
/* 199 */     localHtmlTree19.addContent(HtmlTree.LI(
/* 200 */       getResource("doclet.Annotation_Type_Optional_Member_Summary")));
/*     */ 
/* 201 */     localHtmlTree19.addContent(HtmlTree.LI(
/* 202 */       getResource("doclet.Annotation_Type_Member_Detail")));
/*     */ 
/* 203 */     localHtmlTree17.addContent(localHtmlTree19);
/* 204 */     localHtmlTree4.addContent(localHtmlTree17);
/*     */ 
/* 206 */     HtmlTree localHtmlTree20 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 207 */       getResource("doclet.Enum"));
/*     */ 
/* 208 */     HtmlTree localHtmlTree21 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree20);
/* 209 */     Content localContent5 = getResource("doclet.Help_enum_line_1");
/* 210 */     HtmlTree localHtmlTree22 = HtmlTree.P(localContent5);
/* 211 */     localHtmlTree21.addContent(localHtmlTree22);
/* 212 */     HtmlTree localHtmlTree23 = new HtmlTree(HtmlTag.UL);
/* 213 */     localHtmlTree23.addContent(HtmlTree.LI(
/* 214 */       getResource("doclet.Help_enum_line_2")));
/*     */ 
/* 215 */     localHtmlTree23.addContent(HtmlTree.LI(
/* 216 */       getResource("doclet.Help_enum_line_3")));
/*     */ 
/* 217 */     localHtmlTree23.addContent(HtmlTree.LI(
/* 218 */       getResource("doclet.Enum_Constant_Summary")));
/*     */ 
/* 219 */     localHtmlTree23.addContent(HtmlTree.LI(
/* 220 */       getResource("doclet.Enum_Constant_Detail")));
/*     */ 
/* 221 */     localHtmlTree21.addContent(localHtmlTree23);
/* 222 */     localHtmlTree4.addContent(localHtmlTree21);
/* 223 */     if (this.configuration.classuse) {
/* 224 */       localObject1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 225 */         getResource("doclet.Help_line_14"));
/*     */ 
/* 226 */       localHtmlTree24 = HtmlTree.LI(HtmlStyle.blockList, (Content)localObject1);
/* 227 */       localObject2 = getResource("doclet.Help_line_15");
/* 228 */       localObject3 = HtmlTree.P((Content)localObject2);
/* 229 */       localHtmlTree24.addContent((Content)localObject3);
/* 230 */       localHtmlTree4.addContent(localHtmlTree24);
/*     */     }
/* 232 */     if (this.configuration.createtree) {
/* 233 */       localObject1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 234 */         getResource("doclet.Help_line_16"));
/*     */ 
/* 235 */       localHtmlTree24 = HtmlTree.LI(HtmlStyle.blockList, (Content)localObject1);
/* 236 */       localObject2 = getResource("doclet.Help_line_17_with_tree_link", 
/* 237 */         getHyperLink(DocPaths.OVERVIEW_TREE, this.configuration
/* 238 */         .getText("doclet.Class_Hierarchy")), 
/* 239 */         HtmlTree.CODE(new StringContent("java.lang.Object")));
/*     */ 
/* 240 */       localObject3 = HtmlTree.P((Content)localObject2);
/* 241 */       localHtmlTree24.addContent((Content)localObject3);
/* 242 */       localHtmlTree25 = new HtmlTree(HtmlTag.UL);
/* 243 */       localHtmlTree25.addContent(HtmlTree.LI(
/* 244 */         getResource("doclet.Help_line_18")));
/*     */ 
/* 245 */       localHtmlTree25.addContent(HtmlTree.LI(
/* 246 */         getResource("doclet.Help_line_19")));
/*     */ 
/* 247 */       localHtmlTree24.addContent(localHtmlTree25);
/* 248 */       localHtmlTree4.addContent(localHtmlTree24);
/*     */     }
/* 250 */     if ((!this.configuration.nodeprecatedlist) && (!this.configuration.nodeprecated))
/*     */     {
/* 252 */       localObject1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 253 */         getResource("doclet.Deprecated_API"));
/*     */ 
/* 254 */       localHtmlTree24 = HtmlTree.LI(HtmlStyle.blockList, (Content)localObject1);
/* 255 */       localObject2 = getResource("doclet.Help_line_20_with_deprecated_api_link", 
/* 256 */         getHyperLink(DocPaths.DEPRECATED_LIST, this.configuration
/* 257 */         .getText("doclet.Deprecated_API")));
/*     */ 
/* 258 */       localObject3 = HtmlTree.P((Content)localObject2);
/* 259 */       localHtmlTree24.addContent((Content)localObject3);
/* 260 */       localHtmlTree4.addContent(localHtmlTree24);
/*     */     }
/* 262 */     if (this.configuration.createindex)
/*     */     {
/* 264 */       if (this.configuration.splitindex) {
/* 265 */         localObject1 = getHyperLink(DocPaths.INDEX_FILES.resolve(DocPaths.indexN(1)), this.configuration
/* 266 */           .getText("doclet.Index"));
/*     */       }
/*     */       else {
/* 268 */         localObject1 = getHyperLink(DocPaths.INDEX_ALL, this.configuration
/* 269 */           .getText("doclet.Index"));
/*     */       }
/*     */ 
/* 271 */       localHtmlTree24 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 272 */         getResource("doclet.Help_line_21"));
/*     */ 
/* 273 */       localObject2 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree24);
/* 274 */       localObject3 = getResource("doclet.Help_line_22", localObject1);
/* 275 */       localHtmlTree25 = HtmlTree.P((Content)localObject3);
/* 276 */       ((Content)localObject2).addContent(localHtmlTree25);
/* 277 */       localHtmlTree4.addContent((Content)localObject2);
/*     */     }
/* 279 */     Object localObject1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 280 */       getResource("doclet.Help_line_23"));
/*     */ 
/* 281 */     HtmlTree localHtmlTree24 = HtmlTree.LI(HtmlStyle.blockList, (Content)localObject1);
/* 282 */     Object localObject2 = getResource("doclet.Help_line_24");
/* 283 */     Object localObject3 = HtmlTree.P((Content)localObject2);
/* 284 */     localHtmlTree24.addContent((Content)localObject3);
/* 285 */     localHtmlTree4.addContent(localHtmlTree24);
/* 286 */     HtmlTree localHtmlTree25 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 287 */       getResource("doclet.Help_line_25"));
/*     */ 
/* 288 */     HtmlTree localHtmlTree26 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree25);
/* 289 */     Content localContent6 = getResource("doclet.Help_line_26");
/* 290 */     HtmlTree localHtmlTree27 = HtmlTree.P(localContent6);
/* 291 */     localHtmlTree26.addContent(localHtmlTree27);
/* 292 */     localHtmlTree4.addContent(localHtmlTree26);
/* 293 */     HtmlTree localHtmlTree28 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 294 */       getResource("doclet.All_Classes"));
/*     */ 
/* 295 */     HtmlTree localHtmlTree29 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree28);
/* 296 */     Content localContent7 = getResource("doclet.Help_line_27", 
/* 297 */       getHyperLink(DocPaths.ALLCLASSES_NOFRAME, this.configuration
/* 298 */       .getText("doclet.All_Classes")));
/*     */ 
/* 299 */     HtmlTree localHtmlTree30 = HtmlTree.P(localContent7);
/* 300 */     localHtmlTree29.addContent(localHtmlTree30);
/* 301 */     localHtmlTree4.addContent(localHtmlTree29);
/* 302 */     HtmlTree localHtmlTree31 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 303 */       getResource("doclet.Serialized_Form"));
/*     */ 
/* 304 */     HtmlTree localHtmlTree32 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree31);
/* 305 */     Content localContent8 = getResource("doclet.Help_line_28");
/* 306 */     HtmlTree localHtmlTree33 = HtmlTree.P(localContent8);
/* 307 */     localHtmlTree32.addContent(localHtmlTree33);
/* 308 */     localHtmlTree4.addContent(localHtmlTree32);
/* 309 */     HtmlTree localHtmlTree34 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 310 */       getResource("doclet.Constants_Summary"));
/*     */ 
/* 311 */     HtmlTree localHtmlTree35 = HtmlTree.LI(HtmlStyle.blockList, localHtmlTree34);
/* 312 */     Content localContent9 = getResource("doclet.Help_line_29", 
/* 313 */       getHyperLink(DocPaths.CONSTANT_VALUES, this.configuration
/* 314 */       .getText("doclet.Constants_Summary")));
/*     */ 
/* 315 */     HtmlTree localHtmlTree36 = HtmlTree.P(localContent9);
/* 316 */     localHtmlTree35.addContent(localHtmlTree36);
/* 317 */     localHtmlTree4.addContent(localHtmlTree35);
/* 318 */     HtmlTree localHtmlTree37 = HtmlTree.DIV(HtmlStyle.contentContainer, localHtmlTree4);
/* 319 */     HtmlTree localHtmlTree38 = HtmlTree.SPAN(HtmlStyle.emphasizedPhrase, getResource("doclet.Help_line_30"));
/* 320 */     localHtmlTree37.addContent(localHtmlTree38);
/* 321 */     paramContent.addContent(localHtmlTree37);
/*     */   }
/*     */ 
/*     */   protected Content getNavLinkHelp()
/*     */   {
/* 331 */     HtmlTree localHtmlTree = HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.helpLabel);
/* 332 */     return localHtmlTree;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.HelpWriter
 * JD-Core Version:    0.6.2
 */