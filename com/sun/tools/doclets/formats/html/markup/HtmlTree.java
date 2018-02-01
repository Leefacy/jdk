/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class HtmlTree extends Content
/*     */ {
/*     */   private HtmlTag htmlTag;
/*  49 */   private Map<HtmlAttr, String> attrs = Collections.emptyMap();
/*  50 */   private List<Content> content = Collections.emptyList();
/*  51 */   public static final Content EMPTY = new StringContent("");
/*     */ 
/* 175 */   public static final BitSet NONENCODING_CHARS = new BitSet(256);
/*     */ 
/*     */   public HtmlTree(HtmlTag paramHtmlTag)
/*     */   {
/*  59 */     this.htmlTag = ((HtmlTag)nullCheck(paramHtmlTag));
/*     */   }
/*     */ 
/*     */   public HtmlTree(HtmlTag paramHtmlTag, Content[] paramArrayOfContent)
/*     */   {
/*  69 */     this(paramHtmlTag);
/*  70 */     for (Content localContent : paramArrayOfContent)
/*  71 */       addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void addAttr(HtmlAttr paramHtmlAttr, String paramString)
/*     */   {
/*  81 */     if (this.attrs.isEmpty())
/*  82 */       this.attrs = new LinkedHashMap(3);
/*  83 */     this.attrs.put(nullCheck(paramHtmlAttr), escapeHtmlChars(paramString));
/*     */   }
/*     */ 
/*     */   public void setTitle(Content paramContent) {
/*  87 */     addAttr(HtmlAttr.TITLE, stripHtml(paramContent));
/*     */   }
/*     */ 
/*     */   public void addStyle(HtmlStyle paramHtmlStyle)
/*     */   {
/*  96 */     addAttr(HtmlAttr.CLASS, paramHtmlStyle.toString());
/*     */   }
/*     */ 
/*     */   public void addContent(Content paramContent)
/*     */   {
/* 105 */     if ((paramContent instanceof ContentBuilder)) {
/* 106 */       for (Content localContent : ((ContentBuilder)paramContent).contents) {
/* 107 */         addContent(localContent);
/*     */       }
/*     */     }
/* 110 */     else if ((paramContent == EMPTY) || (paramContent.isValid())) {
/* 111 */       if (this.content.isEmpty())
/* 112 */         this.content = new ArrayList();
/* 113 */       this.content.add(paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addContent(String paramString)
/*     */   {
/* 125 */     if (!this.content.isEmpty()) {
/* 126 */       Content localContent = (Content)this.content.get(this.content.size() - 1);
/* 127 */       if ((localContent instanceof StringContent))
/* 128 */         localContent.addContent(paramString);
/*     */       else
/* 130 */         addContent(new StringContent(paramString));
/*     */     }
/*     */     else {
/* 133 */       addContent(new StringContent(paramString));
/*     */     }
/*     */   }
/*     */ 
/* 137 */   public int charCount() { int i = 0;
/* 138 */     for (Content localContent : this.content)
/* 139 */       i += localContent.charCount();
/* 140 */     return i;
/*     */   }
/*     */ 
/*     */   private static String escapeHtmlChars(String paramString)
/*     */   {
/* 151 */     for (int i = 0; i < paramString.length(); i++) {
/* 152 */       char c = paramString.charAt(i);
/* 153 */       switch (c) { case '&':
/*     */       case '<':
/*     */       case '>':
/* 156 */         StringBuilder localStringBuilder = new StringBuilder(paramString.substring(0, i));
/* 157 */         for (; i < paramString.length(); i++) {
/* 158 */           c = paramString.charAt(i);
/* 159 */           switch (c) { case '<':
/* 160 */             localStringBuilder.append("&lt;"); break;
/*     */           case '>':
/* 161 */             localStringBuilder.append("&gt;"); break;
/*     */           case '&':
/* 162 */             localStringBuilder.append("&amp;"); break;
/*     */           default:
/* 163 */             localStringBuilder.append(c);
/*     */           }
/*     */         }
/* 166 */         return localStringBuilder.toString();
/*     */       }
/*     */     }
/* 169 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static String encodeURL(String paramString)
/*     */   {
/* 199 */     byte[] arrayOfByte = paramString.getBytes(Charset.forName("UTF-8"));
/* 200 */     StringBuilder localStringBuilder = new StringBuilder();
/* 201 */     for (int i = 0; i < arrayOfByte.length; i++) {
/* 202 */       int j = arrayOfByte[i];
/* 203 */       if (NONENCODING_CHARS.get(j & 0xFF))
/* 204 */         localStringBuilder.append((char)j);
/*     */       else {
/* 206 */         localStringBuilder.append(String.format("%%%02X", new Object[] { Integer.valueOf(j & 0xFF) }));
/*     */       }
/*     */     }
/* 209 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static HtmlTree A(String paramString, Content paramContent)
/*     */   {
/* 220 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.A, new Content[] { (Content)nullCheck(paramContent) });
/* 221 */     localHtmlTree.addAttr(HtmlAttr.HREF, encodeURL(paramString));
/* 222 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree A_NAME(String paramString, Content paramContent)
/*     */   {
/* 233 */     HtmlTree localHtmlTree = A_NAME(paramString);
/* 234 */     localHtmlTree.addContent((Content)nullCheck(paramContent));
/* 235 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree A_NAME(String paramString)
/*     */   {
/* 245 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.A);
/* 246 */     localHtmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString));
/* 247 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree CAPTION(Content paramContent)
/*     */   {
/* 257 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.CAPTION, new Content[] { (Content)nullCheck(paramContent) });
/* 258 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree CODE(Content paramContent)
/*     */   {
/* 268 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.CODE, new Content[] { (Content)nullCheck(paramContent) });
/* 269 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree DD(Content paramContent)
/*     */   {
/* 279 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DD, new Content[] { (Content)nullCheck(paramContent) });
/* 280 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree DL(Content paramContent)
/*     */   {
/* 290 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DL, new Content[] { (Content)nullCheck(paramContent) });
/* 291 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree DIV(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 303 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DIV, new Content[] { (Content)nullCheck(paramContent) });
/* 304 */     if (paramHtmlStyle != null)
/* 305 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 306 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree DIV(Content paramContent)
/*     */   {
/* 316 */     return DIV(null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree DT(Content paramContent)
/*     */   {
/* 326 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.DT, new Content[] { (Content)nullCheck(paramContent) });
/* 327 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree FRAME(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 340 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.FRAME);
/* 341 */     localHtmlTree.addAttr(HtmlAttr.SRC, (String)nullCheck(paramString1));
/* 342 */     localHtmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString2));
/* 343 */     localHtmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString3));
/* 344 */     if (paramString4 != null)
/* 345 */       localHtmlTree.addAttr(HtmlAttr.SCROLLING, paramString4);
/* 346 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree FRAME(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 358 */     return FRAME(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   public static HtmlTree FRAMESET(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 371 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.FRAMESET);
/* 372 */     if (paramString1 != null)
/* 373 */       localHtmlTree.addAttr(HtmlAttr.COLS, paramString1);
/* 374 */     if (paramString2 != null)
/* 375 */       localHtmlTree.addAttr(HtmlAttr.ROWS, paramString2);
/* 376 */     localHtmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString3));
/* 377 */     localHtmlTree.addAttr(HtmlAttr.ONLOAD, (String)nullCheck(paramString4));
/* 378 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, boolean paramBoolean, HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 393 */     HtmlTree localHtmlTree = new HtmlTree(paramHtmlTag, new Content[] { (Content)nullCheck(paramContent) });
/* 394 */     if (paramBoolean)
/* 395 */       localHtmlTree.setTitle(paramContent);
/* 396 */     if (paramHtmlStyle != null)
/* 397 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 398 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 411 */     return HEADING(paramHtmlTag, false, paramHtmlStyle, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, boolean paramBoolean, Content paramContent)
/*     */   {
/* 424 */     return HEADING(paramHtmlTag, paramBoolean, null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree HEADING(HtmlTag paramHtmlTag, Content paramContent)
/*     */   {
/* 435 */     return HEADING(paramHtmlTag, false, null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree HTML(String paramString, Content paramContent1, Content paramContent2)
/*     */   {
/* 448 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.HTML, new Content[] { (Content)nullCheck(paramContent1), (Content)nullCheck(paramContent2) });
/* 449 */     localHtmlTree.addAttr(HtmlAttr.LANG, (String)nullCheck(paramString));
/* 450 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree LI(Content paramContent)
/*     */   {
/* 460 */     return LI(null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree LI(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 471 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LI, new Content[] { (Content)nullCheck(paramContent) });
/* 472 */     if (paramHtmlStyle != null)
/* 473 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 474 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree LINK(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 487 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.LINK);
/* 488 */     localHtmlTree.addAttr(HtmlAttr.REL, (String)nullCheck(paramString1));
/* 489 */     localHtmlTree.addAttr(HtmlAttr.TYPE, (String)nullCheck(paramString2));
/* 490 */     localHtmlTree.addAttr(HtmlAttr.HREF, (String)nullCheck(paramString3));
/* 491 */     localHtmlTree.addAttr(HtmlAttr.TITLE, (String)nullCheck(paramString4));
/* 492 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree META(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 504 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.META);
/* 505 */     String str = paramString2 + "; charset=" + paramString3;
/* 506 */     localHtmlTree.addAttr(HtmlAttr.HTTP_EQUIV, (String)nullCheck(paramString1));
/* 507 */     localHtmlTree.addAttr(HtmlAttr.CONTENT, str);
/* 508 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree META(String paramString1, String paramString2)
/*     */   {
/* 519 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.META);
/* 520 */     localHtmlTree.addAttr(HtmlAttr.NAME, (String)nullCheck(paramString1));
/* 521 */     localHtmlTree.addAttr(HtmlAttr.CONTENT, (String)nullCheck(paramString2));
/* 522 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree NOSCRIPT(Content paramContent)
/*     */   {
/* 532 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.NOSCRIPT, new Content[] { (Content)nullCheck(paramContent) });
/* 533 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree P(Content paramContent)
/*     */   {
/* 543 */     return P(null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree P(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 554 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.P, new Content[] { (Content)nullCheck(paramContent) });
/* 555 */     if (paramHtmlStyle != null)
/* 556 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 557 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree SCRIPT(String paramString1, String paramString2)
/*     */   {
/* 568 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 569 */     localHtmlTree.addAttr(HtmlAttr.TYPE, (String)nullCheck(paramString1));
/* 570 */     localHtmlTree.addAttr(HtmlAttr.SRC, (String)nullCheck(paramString2));
/* 571 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree SMALL(Content paramContent)
/*     */   {
/* 581 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SMALL, new Content[] { (Content)nullCheck(paramContent) });
/* 582 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree SPAN(Content paramContent)
/*     */   {
/* 592 */     return SPAN(null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree SPAN(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 603 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SPAN, new Content[] { (Content)nullCheck(paramContent) });
/* 604 */     if (paramHtmlStyle != null)
/* 605 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 606 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree SPAN(String paramString, HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 619 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.SPAN, new Content[] { (Content)nullCheck(paramContent) });
/* 620 */     localHtmlTree.addAttr(HtmlAttr.ID, (String)nullCheck(paramString));
/* 621 */     if (paramHtmlStyle != null)
/* 622 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 623 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree TABLE(HtmlStyle paramHtmlStyle, int paramInt1, int paramInt2, int paramInt3, String paramString, Content paramContent)
/*     */   {
/* 640 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TABLE, new Content[] { (Content)nullCheck(paramContent) });
/* 641 */     if (paramHtmlStyle != null)
/* 642 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 643 */     localHtmlTree.addAttr(HtmlAttr.BORDER, Integer.toString(paramInt1));
/* 644 */     localHtmlTree.addAttr(HtmlAttr.CELLPADDING, Integer.toString(paramInt2));
/* 645 */     localHtmlTree.addAttr(HtmlAttr.CELLSPACING, Integer.toString(paramInt3));
/* 646 */     localHtmlTree.addAttr(HtmlAttr.SUMMARY, (String)nullCheck(paramString));
/* 647 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree TD(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 658 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TD, new Content[] { (Content)nullCheck(paramContent) });
/* 659 */     if (paramHtmlStyle != null)
/* 660 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 661 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree TD(Content paramContent)
/*     */   {
/* 671 */     return TD(null, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree TH(HtmlStyle paramHtmlStyle, String paramString, Content paramContent)
/*     */   {
/* 683 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TH, new Content[] { (Content)nullCheck(paramContent) });
/* 684 */     if (paramHtmlStyle != null)
/* 685 */       localHtmlTree.addStyle(paramHtmlStyle);
/* 686 */     localHtmlTree.addAttr(HtmlAttr.SCOPE, (String)nullCheck(paramString));
/* 687 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree TH(String paramString, Content paramContent)
/*     */   {
/* 698 */     return TH(null, paramString, paramContent);
/*     */   }
/*     */ 
/*     */   public static HtmlTree TITLE(Content paramContent)
/*     */   {
/* 708 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TITLE, new Content[] { (Content)nullCheck(paramContent) });
/* 709 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree TR(Content paramContent)
/*     */   {
/* 719 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.TR, new Content[] { (Content)nullCheck(paramContent) });
/* 720 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public static HtmlTree UL(HtmlStyle paramHtmlStyle, Content paramContent)
/*     */   {
/* 731 */     HtmlTree localHtmlTree = new HtmlTree(HtmlTag.UL, new Content[] { (Content)nullCheck(paramContent) });
/* 732 */     localHtmlTree.addStyle((HtmlStyle)nullCheck(paramHtmlStyle));
/* 733 */     return localHtmlTree;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 740 */     return (!hasContent()) && (!hasAttrs());
/*     */   }
/*     */ 
/*     */   public boolean hasContent()
/*     */   {
/* 749 */     return !this.content.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean hasAttrs()
/*     */   {
/* 758 */     return !this.attrs.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean hasAttr(HtmlAttr paramHtmlAttr)
/*     */   {
/* 768 */     return this.attrs.containsKey(paramHtmlAttr);
/*     */   }
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 779 */     switch (1.$SwitchMap$com$sun$tools$doclets$formats$html$markup$HtmlTag[this.htmlTag.ordinal()]) {
/*     */     case 1:
/* 781 */       return (hasAttr(HtmlAttr.NAME)) || ((hasAttr(HtmlAttr.HREF)) && (hasContent()));
/*     */     case 2:
/* 783 */       return (!hasContent()) && ((!hasAttrs()) || (hasAttr(HtmlAttr.CLEAR)));
/*     */     case 3:
/* 785 */       return (hasAttr(HtmlAttr.SRC)) && (!hasContent());
/*     */     case 4:
/* 787 */       return !hasContent();
/*     */     case 5:
/* 789 */       return (hasAttr(HtmlAttr.SRC)) && (hasAttr(HtmlAttr.ALT)) && (!hasContent());
/*     */     case 6:
/* 791 */       return (hasAttr(HtmlAttr.HREF)) && (!hasContent());
/*     */     case 7:
/* 793 */       return (hasAttr(HtmlAttr.CONTENT)) && (!hasContent());
/*     */     case 8:
/* 796 */       return ((hasAttr(HtmlAttr.TYPE)) && (hasAttr(HtmlAttr.SRC)) && (!hasContent())) || (
/* 796 */         (hasAttr(HtmlAttr.TYPE)) && 
/* 796 */         (hasContent()));
/*     */     }
/* 798 */     return hasContent();
/*     */   }
/*     */ 
/*     */   public boolean isInline()
/*     */   {
/* 808 */     return this.htmlTag.blockType == HtmlTag.BlockType.INLINE;
/*     */   }
/*     */ 
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 816 */     if ((!isInline()) && (!paramBoolean))
/* 817 */       paramWriter.write(DocletConstants.NL);
/* 818 */     String str1 = this.htmlTag.toString();
/* 819 */     paramWriter.write("<");
/* 820 */     paramWriter.write(str1);
/* 821 */     Iterator localIterator1 = this.attrs.keySet().iterator();
/*     */ 
/* 824 */     while (localIterator1.hasNext()) {
/* 825 */       HtmlAttr localHtmlAttr = (HtmlAttr)localIterator1.next();
/* 826 */       String str2 = (String)this.attrs.get(localHtmlAttr);
/* 827 */       paramWriter.write(" ");
/* 828 */       paramWriter.write(localHtmlAttr.toString());
/* 829 */       if (!str2.isEmpty()) {
/* 830 */         paramWriter.write("=\"");
/* 831 */         paramWriter.write(str2);
/* 832 */         paramWriter.write("\"");
/*     */       }
/*     */     }
/* 835 */     paramWriter.write(">");
/* 836 */     boolean bool = false;
/* 837 */     for (Content localContent : this.content)
/* 838 */       bool = localContent.write(paramWriter, bool);
/* 839 */     if (this.htmlTag.endTagRequired()) {
/* 840 */       paramWriter.write("</");
/* 841 */       paramWriter.write(str1);
/* 842 */       paramWriter.write(">");
/*     */     }
/* 844 */     if (!isInline()) {
/* 845 */       paramWriter.write(DocletConstants.NL);
/* 846 */       return true;
/*     */     }
/* 848 */     return false;
/*     */   }
/*     */ 
/*     */   private static String stripHtml(Content paramContent)
/*     */   {
/* 861 */     String str = paramContent.toString();
/*     */ 
/* 863 */     str = str.replaceAll("\\<.*?>", " ");
/*     */ 
/* 865 */     str = str.replaceAll("\\b\\s{2,}\\b", " ");
/*     */ 
/* 867 */     return str.trim();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 179 */     for (int i = 97; i <= 122; i++) {
/* 180 */       NONENCODING_CHARS.set(i);
/*     */     }
/* 182 */     for (i = 65; i <= 90; i++) {
/* 183 */       NONENCODING_CHARS.set(i);
/*     */     }
/*     */ 
/* 186 */     for (i = 48; i <= 57; i++) {
/* 187 */       NONENCODING_CHARS.set(i);
/*     */     }
/*     */ 
/* 190 */     String str = ":/?#[]@!$&'()*+,;=";
/*     */ 
/* 192 */     str = str + "-._~";
/* 193 */     for (int j = 0; j < str.length(); j++)
/* 194 */       NONENCODING_CHARS.set(str.charAt(j));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.HtmlTree
 * JD-Core Version:    0.6.2
 */