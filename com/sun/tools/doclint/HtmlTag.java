/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Name;
/*     */ 
/*     */ public enum HtmlTag
/*     */ {
/*  58 */   A(BlockType.INLINE, EndKind.REQUIRED, new AttrMap[] { 
/*  59 */     attrs(AttrKind.OK, new Attr[] { Attr.HREF, Attr.TARGET, Attr.NAME }) }), 
/*     */ 
/*  61 */   B(BlockType.INLINE, EndKind.REQUIRED, 
/*  62 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/*  62 */     , new AttrMap[0]), 
/*     */ 
/*  64 */   BIG(BlockType.INLINE, EndKind.REQUIRED, 
/*  65 */     EnumSet.of(Flag.EXPECT_CONTENT)
/*  65 */     , new AttrMap[0]), 
/*     */ 
/*  67 */   BLOCKQUOTE(BlockType.BLOCK, EndKind.REQUIRED, 
/*  68 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/*  68 */     , new AttrMap[0]), 
/*     */ 
/*  70 */   BODY(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/*  72 */   BR(BlockType.INLINE, EndKind.NONE, new AttrMap[] { 
/*  73 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.CLEAR }) }), 
/*     */ 
/*  75 */   CAPTION(BlockType.TABLE_ITEM, EndKind.REQUIRED, 
/*  76 */     EnumSet.of(Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT)
/*  76 */     , new AttrMap[0]), 
/*     */ 
/*  78 */   CENTER(BlockType.BLOCK, EndKind.REQUIRED, 
/*  79 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/*  79 */     , new AttrMap[0]), 
/*     */ 
/*  81 */   CITE(BlockType.INLINE, EndKind.REQUIRED, 
/*  82 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/*  82 */     , new AttrMap[0]), 
/*     */ 
/*  84 */   CODE(BlockType.INLINE, EndKind.REQUIRED, 
/*  85 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/*  85 */     , new AttrMap[0]), 
/*     */ 
/*  87 */   DD(BlockType.LIST_ITEM, EndKind.OPTIONAL, 
/*  88 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT)
/*  88 */     , new AttrMap[0]), 
/*     */ 
/*  90 */   DFN(BlockType.INLINE, EndKind.REQUIRED, 
/*  91 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/*  91 */     , new AttrMap[0]), 
/*     */ 
/*  93 */   DIV(BlockType.BLOCK, EndKind.REQUIRED, 
/*  94 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/*  94 */     , new AttrMap[0]), 
/*     */ 
/*  96 */   DL(BlockType.BLOCK, EndKind.REQUIRED, 
/*  97 */     EnumSet.of(Flag.EXPECT_CONTENT)
/*  97 */     , new AttrMap[] { 
/*  98 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.COMPACT }) }), 
/*     */ 
/* 105 */   DT(BlockType.LIST_ITEM, EndKind.OPTIONAL, 
/* 106 */     EnumSet.of(Flag.ACCEPTS_INLINE, Flag.EXPECT_CONTENT)
/* 106 */     , new AttrMap[0]), 
/*     */ 
/* 108 */   EM(BlockType.INLINE, EndKind.REQUIRED, 
/* 109 */     EnumSet.of(Flag.NO_NEST)
/* 109 */     , new AttrMap[0]), 
/*     */ 
/* 111 */   FONT(BlockType.INLINE, EndKind.REQUIRED, 
/* 112 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 112 */     , new AttrMap[] { 
/* 113 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.SIZE, Attr.COLOR, Attr.FACE }) }), 
/*     */ 
/* 115 */   FRAME(BlockType.OTHER, EndKind.NONE, new AttrMap[0]), 
/*     */ 
/* 117 */   FRAMESET(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 119 */   H1(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/* 120 */   H2(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/* 121 */   H3(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/* 122 */   H4(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/* 123 */   H5(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/* 124 */   H6(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 126 */   HEAD(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 128 */   HR(BlockType.BLOCK, EndKind.NONE, new AttrMap[] { 
/* 129 */     attrs(AttrKind.OK, new Attr[] { Attr.WIDTH }) }), 
/*     */ 
/* 131 */   HTML(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 133 */   I(BlockType.INLINE, EndKind.REQUIRED, 
/* 134 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/* 134 */     , new AttrMap[0]), 
/*     */ 
/* 136 */   IMG(BlockType.INLINE, EndKind.NONE, new AttrMap[] { 
/* 137 */     attrs(AttrKind.OK, new Attr[] { Attr.SRC, Attr.ALT, Attr.HEIGHT, Attr.WIDTH }), 
/* 138 */     attrs(AttrKind.OBSOLETE, new Attr[] { Attr.NAME }), 
/* 139 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN, Attr.HSPACE, Attr.VSPACE, Attr.BORDER }) }), 
/*     */ 
/* 141 */   LI(BlockType.LIST_ITEM, EndKind.OPTIONAL, 
/* 142 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/* 142 */     , new AttrMap[] { 
/* 143 */     attrs(AttrKind.OK, new Attr[] { Attr.VALUE }) }), 
/*     */ 
/* 145 */   LINK(BlockType.OTHER, EndKind.NONE, new AttrMap[0]), 
/*     */ 
/* 147 */   MENU(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 154 */   META(BlockType.OTHER, EndKind.NONE, new AttrMap[0]), 
/*     */ 
/* 156 */   NOFRAMES(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 158 */   NOSCRIPT(BlockType.BLOCK, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 160 */   OL(BlockType.BLOCK, EndKind.REQUIRED, 
/* 161 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 161 */     , new AttrMap[] { 
/* 162 */     attrs(AttrKind.OK, new Attr[] { Attr.START, Attr.TYPE }) }), 
/*     */ 
/* 169 */   P(BlockType.BLOCK, EndKind.OPTIONAL, 
/* 170 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 170 */     , new AttrMap[] { 
/* 171 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN }) }), 
/*     */ 
/* 173 */   PRE(BlockType.BLOCK, EndKind.REQUIRED, 
/* 174 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 174 */     , new AttrMap[0]), 
/*     */ 
/* 186 */   SCRIPT(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 188 */   SMALL(BlockType.INLINE, EndKind.REQUIRED, 
/* 189 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 189 */     , new AttrMap[0]), 
/*     */ 
/* 191 */   SPAN(BlockType.INLINE, EndKind.REQUIRED, 
/* 192 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 192 */     , new AttrMap[0]), 
/*     */ 
/* 194 */   STRONG(BlockType.INLINE, EndKind.REQUIRED, 
/* 195 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 195 */     , new AttrMap[0]), 
/*     */ 
/* 197 */   SUB(BlockType.INLINE, EndKind.REQUIRED, 
/* 198 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/* 198 */     , new AttrMap[0]), 
/*     */ 
/* 200 */   SUP(BlockType.INLINE, EndKind.REQUIRED, 
/* 201 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/* 201 */     , new AttrMap[0]), 
/*     */ 
/* 203 */   TABLE(BlockType.BLOCK, EndKind.REQUIRED, 
/* 204 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 204 */     , new AttrMap[] { 
/* 205 */     attrs(AttrKind.OK, new Attr[] { Attr.SUMMARY, Attr.FRAME, Attr.RULES, Attr.BORDER, Attr.CELLPADDING, Attr.CELLSPACING, Attr.WIDTH }), 
/* 207 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.ALIGN, Attr.BGCOLOR }) }), 
/*     */ 
/* 221 */   TBODY(BlockType.TABLE_ITEM, EndKind.REQUIRED, 
/* 222 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 222 */     , new AttrMap[] { 
/* 223 */     attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }) }), 
/*     */ 
/* 230 */   TD(BlockType.TABLE_ITEM, EndKind.OPTIONAL, 
/* 231 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/* 231 */     , new AttrMap[] { 
/* 232 */     attrs(AttrKind.OK, new Attr[] { Attr.COLSPAN, Attr.ROWSPAN, Attr.HEADERS, Attr.SCOPE, Attr.ABBR, Attr.AXIS, Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }), 
/* 234 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.WIDTH, Attr.BGCOLOR, Attr.HEIGHT, Attr.NOWRAP }) }), 
/*     */ 
/* 236 */   TFOOT(BlockType.TABLE_ITEM, EndKind.REQUIRED, new AttrMap[] { 
/* 237 */     attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }) }), 
/*     */ 
/* 244 */   TH(BlockType.TABLE_ITEM, EndKind.OPTIONAL, 
/* 245 */     EnumSet.of(Flag.ACCEPTS_BLOCK, Flag.ACCEPTS_INLINE)
/* 245 */     , new AttrMap[] { 
/* 246 */     attrs(AttrKind.OK, new Attr[] { Attr.COLSPAN, Attr.ROWSPAN, Attr.HEADERS, Attr.SCOPE, Attr.ABBR, Attr.AXIS, Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }), 
/* 248 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.WIDTH, Attr.BGCOLOR, Attr.HEIGHT, Attr.NOWRAP }) }), 
/*     */ 
/* 250 */   THEAD(BlockType.TABLE_ITEM, EndKind.REQUIRED, new AttrMap[] { 
/* 251 */     attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }) }), 
/*     */ 
/* 258 */   TITLE(BlockType.OTHER, EndKind.REQUIRED, new AttrMap[0]), 
/*     */ 
/* 260 */   TR(BlockType.TABLE_ITEM, EndKind.OPTIONAL, new AttrMap[] { 
/* 261 */     attrs(AttrKind.OK, new Attr[] { Attr.ALIGN, Attr.CHAR, Attr.CHAROFF, Attr.VALIGN }), 
/* 262 */     attrs(AttrKind.USE_CSS, new Attr[] { Attr.BGCOLOR }) }), 
/*     */ 
/* 269 */   TT(BlockType.INLINE, EndKind.REQUIRED, 
/* 270 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/* 270 */     , new AttrMap[0]), 
/*     */ 
/* 272 */   U(BlockType.INLINE, EndKind.REQUIRED, 
/* 273 */     EnumSet.of(Flag.EXPECT_CONTENT, Flag.NO_NEST)
/* 273 */     , new AttrMap[0]), 
/*     */ 
/* 275 */   UL(BlockType.BLOCK, EndKind.REQUIRED, 
/* 276 */     EnumSet.of(Flag.EXPECT_CONTENT)
/* 276 */     , new AttrMap[] { 
/* 277 */     attrs(AttrKind.OK, new Attr[] { Attr.COMPACT, Attr.TYPE }) }), 
/*     */ 
/* 284 */   VAR(BlockType.INLINE, EndKind.REQUIRED, new AttrMap[0]);
/*     */ 
/*     */   public final BlockType blockType;
/*     */   public final EndKind endKind;
/*     */   public final Set<Flag> flags;
/*     */   private final Map<Attr, AttrKind> attrs;
/*     */   private static final Map<String, HtmlTag> index;
/*     */ 
/*     */   private HtmlTag(BlockType paramBlockType, EndKind paramEndKind, AttrMap[] paramArrayOfAttrMap)
/*     */   {
/* 390 */     this(paramBlockType, paramEndKind, Collections.emptySet(), paramArrayOfAttrMap);
/*     */   }
/*     */ 
/*     */   private HtmlTag(BlockType paramBlockType, EndKind paramEndKind, Set<Flag> paramSet, AttrMap[] paramArrayOfAttrMap) {
/* 394 */     this.blockType = paramBlockType;
/* 395 */     this.endKind = paramEndKind;
/* 396 */     this.flags = paramSet;
/* 397 */     this.attrs = new EnumMap(Attr.class);
/* 398 */     for (AttrMap localAttrMap : paramArrayOfAttrMap)
/* 399 */       this.attrs.putAll(localAttrMap);
/* 400 */     this.attrs.put(Attr.CLASS, AttrKind.OK);
/* 401 */     this.attrs.put(Attr.ID, AttrKind.OK);
/* 402 */     this.attrs.put(Attr.STYLE, AttrKind.OK);
/*     */   }
/*     */ 
/*     */   public boolean accepts(HtmlTag paramHtmlTag) {
/* 406 */     if ((this.flags.contains(Flag.ACCEPTS_BLOCK)) && (this.flags.contains(Flag.ACCEPTS_INLINE)))
/* 407 */       return (paramHtmlTag.blockType == BlockType.BLOCK) || (paramHtmlTag.blockType == BlockType.INLINE);
/* 408 */     if (this.flags.contains(Flag.ACCEPTS_BLOCK))
/* 409 */       return paramHtmlTag.blockType == BlockType.BLOCK;
/* 410 */     if (this.flags.contains(Flag.ACCEPTS_INLINE)) {
/* 411 */       return paramHtmlTag.blockType == BlockType.INLINE;
/*     */     }
/* 413 */     switch (11.$SwitchMap$com$sun$tools$doclint$HtmlTag$BlockType[this.blockType.ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/* 416 */       return paramHtmlTag.blockType == BlockType.INLINE;
/*     */     case 3:
/* 420 */       return true;
/*     */     }
/*     */ 
/* 424 */     throw new AssertionError(this + ":" + paramHtmlTag);
/*     */   }
/*     */ 
/*     */   public boolean acceptsText()
/*     */   {
/* 431 */     return accepts(B);
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 435 */     return StringUtils.toLowerCase(name());
/*     */   }
/*     */ 
/*     */   public Attr getAttr(Name paramName) {
/* 439 */     return (Attr)Attr.index.get(StringUtils.toLowerCase(paramName.toString()));
/*     */   }
/*     */ 
/*     */   public AttrKind getAttrKind(Name paramName) {
/* 443 */     AttrKind localAttrKind = (AttrKind)this.attrs.get(getAttr(paramName));
/* 444 */     return localAttrKind == null ? AttrKind.INVALID : localAttrKind;
/*     */   }
/*     */ 
/*     */   private static AttrMap attrs(AttrKind paramAttrKind, Attr[] paramArrayOfAttr) {
/* 448 */     AttrMap localAttrMap = new AttrMap();
/* 449 */     for (Attr localAttr : paramArrayOfAttr) localAttrMap.put(localAttr, paramAttrKind);
/* 450 */     return localAttrMap;
/*     */   }
/*     */ 
/*     */   static HtmlTag get(Name paramName)
/*     */   {
/* 461 */     return (HtmlTag)index.get(StringUtils.toLowerCase(paramName.toString()));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 453 */     index = new HashMap();
/*     */ 
/* 455 */     for (HtmlTag localHtmlTag : values())
/* 456 */       index.put(localHtmlTag.getText(), localHtmlTag);
/*     */   }
/*     */ 
/*     */   public static enum Attr
/*     */   {
/* 314 */     ABBR, 
/* 315 */     ALIGN, 
/* 316 */     ALT, 
/* 317 */     AXIS, 
/* 318 */     BGCOLOR, 
/* 319 */     BORDER, 
/* 320 */     CELLSPACING, 
/* 321 */     CELLPADDING, 
/* 322 */     CHAR, 
/* 323 */     CHAROFF, 
/* 324 */     CLEAR, 
/* 325 */     CLASS, 
/* 326 */     COLOR, 
/* 327 */     COLSPAN, 
/* 328 */     COMPACT, 
/* 329 */     FACE, 
/* 330 */     FRAME, 
/* 331 */     HEADERS, 
/* 332 */     HEIGHT, 
/* 333 */     HREF, 
/* 334 */     HSPACE, 
/* 335 */     ID, 
/* 336 */     NAME, 
/* 337 */     NOWRAP, 
/* 338 */     REVERSED, 
/* 339 */     ROWSPAN, 
/* 340 */     RULES, 
/* 341 */     SCOPE, 
/* 342 */     SIZE, 
/* 343 */     SPACE, 
/* 344 */     SRC, 
/* 345 */     START, 
/* 346 */     STYLE, 
/* 347 */     SUMMARY, 
/* 348 */     TARGET, 
/* 349 */     TYPE, 
/* 350 */     VALIGN, 
/* 351 */     VALUE, 
/* 352 */     VSPACE, 
/* 353 */     WIDTH;
/*     */ 
/*     */     static final Map<String, Attr> index;
/*     */ 
/* 356 */     public String getText() { return StringUtils.toLowerCase(name()); }
/*     */ 
/*     */     static {
/* 359 */       index = new HashMap();
/*     */ 
/* 361 */       for (Attr localAttr : values())
/* 362 */         index.put(localAttr.getText(), localAttr);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum AttrKind
/*     */   {
/* 368 */     INVALID, 
/* 369 */     OBSOLETE, 
/* 370 */     USE_CSS, 
/* 371 */     OK;
/*     */   }
/*     */ 
/*     */   private static class AttrMap extends EnumMap<HtmlTag.Attr, HtmlTag.AttrKind>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     AttrMap() {
/* 379 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum BlockType
/*     */   {
/* 290 */     BLOCK, 
/* 291 */     INLINE, 
/* 292 */     LIST_ITEM, 
/* 293 */     TABLE_ITEM, 
/* 294 */     OTHER;
/*     */   }
/*     */ 
/*     */   public static enum EndKind
/*     */   {
/* 301 */     NONE, 
/* 302 */     OPTIONAL, 
/* 303 */     REQUIRED;
/*     */   }
/*     */ 
/*     */   public static enum Flag {
/* 307 */     ACCEPTS_BLOCK, 
/* 308 */     ACCEPTS_INLINE, 
/* 309 */     EXPECT_CONTENT, 
/* 310 */     NO_NEST;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclint.HtmlTag
 * JD-Core Version:    0.6.2
 */