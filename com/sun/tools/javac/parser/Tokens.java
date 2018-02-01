/*     */ package com.sun.tools.javac.parser;
/*     */ 
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class Tokens
/*     */ {
/*     */   private final Names names;
/*     */   private final TokenKind[] key;
/*  59 */   private int maxKey = 0;
/*     */ 
/*  63 */   private Name[] tokenName = new Name[TokenKind.values().length];
/*     */ 
/*  65 */   public static final Context.Key<Tokens> tokensKey = new Context.Key();
/*     */ 
/* 479 */   public static final Token DUMMY = new Token(TokenKind.ERROR, 0, 0, null);
/*     */ 
/*     */   public static Tokens instance(Context paramContext)
/*     */   {
/*  69 */     Tokens localTokens = (Tokens)paramContext.get(tokensKey);
/*  70 */     if (localTokens == null)
/*  71 */       localTokens = new Tokens(paramContext);
/*  72 */     return localTokens;
/*     */   }
/*     */ 
/*     */   protected Tokens(Context paramContext) {
/*  76 */     paramContext.put(tokensKey, this);
/*  77 */     this.names = Names.instance(paramContext);
/*     */     TokenKind localTokenKind;
/*  78 */     for (localTokenKind : TokenKind.values()) {
/*  79 */       if (localTokenKind.name != null)
/*  80 */         enterKeyword(localTokenKind.name, localTokenKind);
/*     */       else {
/*  82 */         this.tokenName[localTokenKind.ordinal()] = null;
/*     */       }
/*     */     }
/*  85 */     this.key = new TokenKind[this.maxKey + 1];
/*  86 */     for (int i = 0; i <= this.maxKey; i++) this.key[i] = TokenKind.IDENTIFIER;
/*  87 */     for (localTokenKind : TokenKind.values())
/*  88 */       if (localTokenKind.name != null)
/*  89 */         this.key[this.tokenName[localTokenKind.ordinal()].getIndex()] = localTokenKind;
/*     */   }
/*     */ 
/*     */   private void enterKeyword(String paramString, TokenKind paramTokenKind)
/*     */   {
/*  94 */     Name localName = this.names.fromString(paramString);
/*  95 */     this.tokenName[paramTokenKind.ordinal()] = localName;
/*  96 */     if (localName.getIndex() > this.maxKey) this.maxKey = localName.getIndex();
/*     */   }
/*     */ 
/*     */   TokenKind lookupKind(Name paramName)
/*     */   {
/* 105 */     return paramName.getIndex() > this.maxKey ? TokenKind.IDENTIFIER : this.key[paramName.getIndex()];
/*     */   }
/*     */ 
/*     */   TokenKind lookupKind(String paramString) {
/* 109 */     return lookupKind(this.names.fromString(paramString));
/*     */   }
/*     */ 
/*     */   public static abstract interface Comment
/*     */   {
/*     */     public abstract String getText();
/*     */ 
/*     */     public abstract int getSourcePos(int paramInt);
/*     */ 
/*     */     public abstract CommentStyle getStyle();
/*     */ 
/*     */     public abstract boolean isDeprecated();
/*     */ 
/*     */     public static enum CommentStyle
/*     */     {
/* 297 */       LINE, 
/* 298 */       BLOCK, 
/* 299 */       JAVADOC;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NamedToken extends Tokens.Token
/*     */   {
/*     */     public final Name name;
/*     */ 
/*     */     public NamedToken(Tokens.TokenKind paramTokenKind, int paramInt1, int paramInt2, Name paramName, List<Tokens.Comment> paramList)
/*     */     {
/* 421 */       super(paramInt1, paramInt2, paramList);
/* 422 */       this.name = paramName;
/*     */     }
/*     */ 
/*     */     protected void checkKind() {
/* 426 */       if (this.kind.tag != Tokens.Token.Tag.NAMED)
/* 427 */         throw new AssertionError("Bad token kind - expected " + Tokens.Token.Tag.NAMED);
/*     */     }
/*     */ 
/*     */     public Name name()
/*     */     {
/* 433 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NumericToken extends Tokens.StringToken
/*     */   {
/*     */     public final int radix;
/*     */ 
/*     */     public NumericToken(Tokens.TokenKind paramTokenKind, int paramInt1, int paramInt2, String paramString, int paramInt3, List<Tokens.Comment> paramList)
/*     */     {
/* 463 */       super(paramInt1, paramInt2, paramString, paramList);
/* 464 */       this.radix = paramInt3;
/*     */     }
/*     */ 
/*     */     protected void checkKind() {
/* 468 */       if (this.kind.tag != Tokens.Token.Tag.NUMERIC)
/* 469 */         throw new AssertionError("Bad token kind - expected " + Tokens.Token.Tag.NUMERIC);
/*     */     }
/*     */ 
/*     */     public int radix()
/*     */     {
/* 475 */       return this.radix;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StringToken extends Tokens.Token
/*     */   {
/*     */     public final String stringVal;
/*     */ 
/*     */     public StringToken(Tokens.TokenKind paramTokenKind, int paramInt1, int paramInt2, String paramString, List<Tokens.Comment> paramList)
/*     */     {
/* 442 */       super(paramInt1, paramInt2, paramList);
/* 443 */       this.stringVal = paramString;
/*     */     }
/*     */ 
/*     */     protected void checkKind() {
/* 447 */       if (this.kind.tag != Tokens.Token.Tag.STRING)
/* 448 */         throw new AssertionError("Bad token kind - expected " + Tokens.Token.Tag.STRING);
/*     */     }
/*     */ 
/*     */     public String stringVal()
/*     */     {
/* 454 */       return this.stringVal;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Token
/*     */   {
/*     */     public final Tokens.TokenKind kind;
/*     */     public final int pos;
/*     */     public final int endPos;
/*     */     public final List<Tokens.Comment> comments;
/*     */ 
/*     */     Token(Tokens.TokenKind paramTokenKind, int paramInt1, int paramInt2, List<Tokens.Comment> paramList)
/*     */     {
/* 335 */       this.kind = paramTokenKind;
/* 336 */       this.pos = paramInt1;
/* 337 */       this.endPos = paramInt2;
/* 338 */       this.comments = paramList;
/* 339 */       checkKind();
/*     */     }
/*     */ 
/*     */     Token[] split(Tokens paramTokens) {
/* 343 */       if ((this.kind.name.length() < 2) || (this.kind.tag != Tag.DEFAULT)) {
/* 344 */         throw new AssertionError("Cant split" + this.kind);
/*     */       }
/*     */ 
/* 347 */       Tokens.TokenKind localTokenKind1 = paramTokens.lookupKind(this.kind.name.substring(0, 1));
/* 348 */       Tokens.TokenKind localTokenKind2 = paramTokens.lookupKind(this.kind.name.substring(1));
/*     */ 
/* 350 */       if ((localTokenKind1 == null) || (localTokenKind2 == null)) {
/* 351 */         throw new AssertionError("Cant split - bad subtokens");
/*     */       }
/*     */ 
/* 355 */       return new Token[] { new Token(localTokenKind1, this.pos, this.pos + localTokenKind1.name
/* 354 */         .length(), this.comments), new Token(localTokenKind2, this.pos + localTokenKind1.name
/* 355 */         .length(), this.endPos, null) };
/*     */     }
/*     */ 
/*     */     protected void checkKind()
/*     */     {
/* 360 */       if (this.kind.tag != Tag.DEFAULT)
/* 361 */         throw new AssertionError("Bad token kind - expected " + Tag.STRING);
/*     */     }
/*     */ 
/*     */     public Name name()
/*     */     {
/* 366 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String stringVal() {
/* 370 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public int radix() {
/* 374 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public Tokens.Comment comment(Tokens.Comment.CommentStyle paramCommentStyle)
/*     */     {
/* 382 */       List localList = getComments(Tokens.Comment.CommentStyle.JAVADOC);
/* 383 */       return localList.isEmpty() ? null : (Tokens.Comment)localList.head;
/*     */     }
/*     */ 
/*     */     public boolean deprecatedFlag()
/*     */     {
/* 393 */       for (Tokens.Comment localComment : getComments(Tokens.Comment.CommentStyle.JAVADOC)) {
/* 394 */         if (localComment.isDeprecated()) {
/* 395 */           return true;
/*     */         }
/*     */       }
/* 398 */       return false;
/*     */     }
/*     */ 
/*     */     private List<Tokens.Comment> getComments(Tokens.Comment.CommentStyle paramCommentStyle) {
/* 402 */       if (this.comments == null) {
/* 403 */         return List.nil();
/*     */       }
/* 405 */       ListBuffer localListBuffer = new ListBuffer();
/* 406 */       for (Tokens.Comment localComment : this.comments) {
/* 407 */         if (localComment.getStyle() == paramCommentStyle) {
/* 408 */           localListBuffer.add(localComment);
/*     */         }
/*     */       }
/* 411 */       return localListBuffer.toList();
/*     */     }
/*     */ 
/*     */     static enum Tag
/*     */     {
/* 316 */       DEFAULT, 
/* 317 */       NAMED, 
/* 318 */       STRING, 
/* 319 */       NUMERIC;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum TokenKind
/*     */     implements Formattable, Filter<TokenKind>
/*     */   {
/* 117 */     EOF, 
/* 118 */     ERROR, 
/* 119 */     IDENTIFIER(Tokens.Token.Tag.NAMED), 
/* 120 */     ABSTRACT("abstract"), 
/* 121 */     ASSERT("assert", Tokens.Token.Tag.NAMED), 
/* 122 */     BOOLEAN("boolean", Tokens.Token.Tag.NAMED), 
/* 123 */     BREAK("break"), 
/* 124 */     BYTE("byte", Tokens.Token.Tag.NAMED), 
/* 125 */     CASE("case"), 
/* 126 */     CATCH("catch"), 
/* 127 */     CHAR("char", Tokens.Token.Tag.NAMED), 
/* 128 */     CLASS("class"), 
/* 129 */     CONST("const"), 
/* 130 */     CONTINUE("continue"), 
/* 131 */     DEFAULT("default"), 
/* 132 */     DO("do"), 
/* 133 */     DOUBLE("double", Tokens.Token.Tag.NAMED), 
/* 134 */     ELSE("else"), 
/* 135 */     ENUM("enum", Tokens.Token.Tag.NAMED), 
/* 136 */     EXTENDS("extends"), 
/* 137 */     FINAL("final"), 
/* 138 */     FINALLY("finally"), 
/* 139 */     FLOAT("float", Tokens.Token.Tag.NAMED), 
/* 140 */     FOR("for"), 
/* 141 */     GOTO("goto"), 
/* 142 */     IF("if"), 
/* 143 */     IMPLEMENTS("implements"), 
/* 144 */     IMPORT("import"), 
/* 145 */     INSTANCEOF("instanceof"), 
/* 146 */     INT("int", Tokens.Token.Tag.NAMED), 
/* 147 */     INTERFACE("interface"), 
/* 148 */     LONG("long", Tokens.Token.Tag.NAMED), 
/* 149 */     NATIVE("native"), 
/* 150 */     NEW("new"), 
/* 151 */     PACKAGE("package"), 
/* 152 */     PRIVATE("private"), 
/* 153 */     PROTECTED("protected"), 
/* 154 */     PUBLIC("public"), 
/* 155 */     RETURN("return"), 
/* 156 */     SHORT("short", Tokens.Token.Tag.NAMED), 
/* 157 */     STATIC("static"), 
/* 158 */     STRICTFP("strictfp"), 
/* 159 */     SUPER("super", Tokens.Token.Tag.NAMED), 
/* 160 */     SWITCH("switch"), 
/* 161 */     SYNCHRONIZED("synchronized"), 
/* 162 */     THIS("this", Tokens.Token.Tag.NAMED), 
/* 163 */     THROW("throw"), 
/* 164 */     THROWS("throws"), 
/* 165 */     TRANSIENT("transient"), 
/* 166 */     TRY("try"), 
/* 167 */     VOID("void", Tokens.Token.Tag.NAMED), 
/* 168 */     VOLATILE("volatile"), 
/* 169 */     WHILE("while"), 
/* 170 */     INTLITERAL(Tokens.Token.Tag.NUMERIC), 
/* 171 */     LONGLITERAL(Tokens.Token.Tag.NUMERIC), 
/* 172 */     FLOATLITERAL(Tokens.Token.Tag.NUMERIC), 
/* 173 */     DOUBLELITERAL(Tokens.Token.Tag.NUMERIC), 
/* 174 */     CHARLITERAL(Tokens.Token.Tag.NUMERIC), 
/* 175 */     STRINGLITERAL(Tokens.Token.Tag.STRING), 
/* 176 */     TRUE("true", Tokens.Token.Tag.NAMED), 
/* 177 */     FALSE("false", Tokens.Token.Tag.NAMED), 
/* 178 */     NULL("null", Tokens.Token.Tag.NAMED), 
/* 179 */     UNDERSCORE("_", Tokens.Token.Tag.NAMED), 
/* 180 */     ARROW("->"), 
/* 181 */     COLCOL("::"), 
/* 182 */     LPAREN("("), 
/* 183 */     RPAREN(")"), 
/* 184 */     LBRACE("{"), 
/* 185 */     RBRACE("}"), 
/* 186 */     LBRACKET("["), 
/* 187 */     RBRACKET("]"), 
/* 188 */     SEMI(";"), 
/* 189 */     COMMA(","), 
/* 190 */     DOT("."), 
/* 191 */     ELLIPSIS("..."), 
/* 192 */     EQ("="), 
/* 193 */     GT(">"), 
/* 194 */     LT("<"), 
/* 195 */     BANG("!"), 
/* 196 */     TILDE("~"), 
/* 197 */     QUES("?"), 
/* 198 */     COLON(":"), 
/* 199 */     EQEQ("=="), 
/* 200 */     LTEQ("<="), 
/* 201 */     GTEQ(">="), 
/* 202 */     BANGEQ("!="), 
/* 203 */     AMPAMP("&&"), 
/* 204 */     BARBAR("||"), 
/* 205 */     PLUSPLUS("++"), 
/* 206 */     SUBSUB("--"), 
/* 207 */     PLUS("+"), 
/* 208 */     SUB("-"), 
/* 209 */     STAR("*"), 
/* 210 */     SLASH("/"), 
/* 211 */     AMP("&"), 
/* 212 */     BAR("|"), 
/* 213 */     CARET("^"), 
/* 214 */     PERCENT("%"), 
/* 215 */     LTLT("<<"), 
/* 216 */     GTGT(">>"), 
/* 217 */     GTGTGT(">>>"), 
/* 218 */     PLUSEQ("+="), 
/* 219 */     SUBEQ("-="), 
/* 220 */     STAREQ("*="), 
/* 221 */     SLASHEQ("/="), 
/* 222 */     AMPEQ("&="), 
/* 223 */     BAREQ("|="), 
/* 224 */     CARETEQ("^="), 
/* 225 */     PERCENTEQ("%="), 
/* 226 */     LTLTEQ("<<="), 
/* 227 */     GTGTEQ(">>="), 
/* 228 */     GTGTGTEQ(">>>="), 
/* 229 */     MONKEYS_AT("@"), 
/* 230 */     CUSTOM;
/*     */ 
/*     */     public final String name;
/*     */     public final Tokens.Token.Tag tag;
/*     */ 
/* 236 */     private TokenKind() { this(null, Tokens.Token.Tag.DEFAULT); }
/*     */ 
/*     */     private TokenKind(String paramString)
/*     */     {
/* 240 */       this(paramString, Tokens.Token.Tag.DEFAULT);
/*     */     }
/*     */ 
/*     */     private TokenKind(Tokens.Token.Tag paramTag) {
/* 244 */       this(null, paramTag);
/*     */     }
/*     */ 
/*     */     private TokenKind(String paramString, Tokens.Token.Tag paramTag) {
/* 248 */       this.name = paramString;
/* 249 */       this.tag = paramTag;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 253 */       switch (Tokens.1.$SwitchMap$com$sun$tools$javac$parser$Tokens$TokenKind[ordinal()]) {
/*     */       case 1:
/* 255 */         return "token.identifier";
/*     */       case 2:
/* 257 */         return "token.character";
/*     */       case 3:
/* 259 */         return "token.string";
/*     */       case 4:
/* 261 */         return "token.integer";
/*     */       case 5:
/* 263 */         return "token.long-integer";
/*     */       case 6:
/* 265 */         return "token.float";
/*     */       case 7:
/* 267 */         return "token.double";
/*     */       case 8:
/* 269 */         return "token.bad-symbol";
/*     */       case 9:
/* 271 */         return "token.end-of-input";
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/* 274 */         return "'" + this.name + "'";
/*     */       }
/* 276 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String getKind()
/*     */     {
/* 281 */       return "Token";
/*     */     }
/*     */ 
/*     */     public String toString(Locale paramLocale, Messages paramMessages) {
/* 285 */       return this.name != null ? toString() : paramMessages.getLocalizedString(paramLocale, "compiler.misc." + toString(), new Object[0]);
/*     */     }
/*     */ 
/*     */     public boolean accepts(TokenKind paramTokenKind)
/*     */     {
/* 290 */       return this == paramTokenKind;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.Tokens
 * JD-Core Version:    0.6.2
 */