/*      */ package com.sun.xml.internal.dtdparser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ 
/*      */ public class DTDParser
/*      */ {
/*      */   public static final String TYPE_CDATA = "CDATA";
/*      */   public static final String TYPE_ID = "ID";
/*      */   public static final String TYPE_IDREF = "IDREF";
/*      */   public static final String TYPE_IDREFS = "IDREFS";
/*      */   public static final String TYPE_ENTITY = "ENTITY";
/*      */   public static final String TYPE_ENTITIES = "ENTITIES";
/*      */   public static final String TYPE_NMTOKEN = "NMTOKEN";
/*      */   public static final String TYPE_NMTOKENS = "NMTOKENS";
/*      */   public static final String TYPE_NOTATION = "NOTATION";
/*      */   public static final String TYPE_ENUMERATION = "ENUMERATION";
/*      */   private InputEntity in;
/*      */   private StringBuffer strTmp;
/*      */   private char[] nameTmp;
/*      */   private NameCache nameCache;
/*   82 */   private char[] charTmp = new char[2];
/*      */   private boolean doLexicalPE;
/*   89 */   protected final Set declaredElements = new HashSet();
/*   90 */   private SimpleHashtable params = new SimpleHashtable(7);
/*      */ 
/*   93 */   Hashtable notations = new Hashtable(7);
/*   94 */   SimpleHashtable entities = new SimpleHashtable(17);
/*      */ 
/*   96 */   private SimpleHashtable ids = new SimpleHashtable();
/*      */   private DTDEventListener dtdHandler;
/*      */   private EntityResolver resolver;
/*      */   private Locale locale;
/*      */   static final String strANY = "ANY";
/*      */   static final String strEMPTY = "EMPTY";
/*      */   private static final String XmlLang = "xml:lang";
/* 2341 */   static final Catalog messages = new Catalog();
/*      */ 
/*      */   public void setLocale(Locale l)
/*      */     throws SAXException
/*      */   {
/*  117 */     if ((l != null) && (!messages.isLocaleSupported(l.toString()))) {
/*  118 */       throw new SAXException(messages.getMessage(this.locale, "P-078", new Object[] { l }));
/*      */     }
/*      */ 
/*  121 */     this.locale = l;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  128 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public Locale chooseLocale(String[] languages)
/*      */     throws SAXException
/*      */   {
/*  148 */     Locale l = messages.chooseLocale(languages);
/*      */ 
/*  150 */     if (l != null) {
/*  151 */       setLocale(l);
/*      */     }
/*  153 */     return l;
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(EntityResolver r)
/*      */   {
/*  161 */     this.resolver = r;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/*  169 */     return this.resolver;
/*      */   }
/*      */ 
/*      */   public void setDtdHandler(DTDEventListener handler)
/*      */   {
/*  176 */     this.dtdHandler = handler;
/*  177 */     if (handler != null)
/*  178 */       handler.setDocumentLocator(new Locator() {
/*      */         public String getPublicId() {
/*  180 */           return DTDParser.this.getPublicId();
/*      */         }
/*      */ 
/*      */         public String getSystemId() {
/*  184 */           return DTDParser.this.getSystemId();
/*      */         }
/*      */ 
/*      */         public int getLineNumber() {
/*  188 */           return DTDParser.this.getLineNumber();
/*      */         }
/*      */ 
/*      */         public int getColumnNumber() {
/*  192 */           return DTDParser.this.getColumnNumber();
/*      */         }
/*      */       });
/*      */   }
/*      */ 
/*      */   public DTDEventListener getDtdHandler()
/*      */   {
/*  201 */     return this.dtdHandler;
/*      */   }
/*      */ 
/*      */   public void parse(InputSource in)
/*      */     throws IOException, SAXException
/*      */   {
/*  209 */     init();
/*  210 */     parseInternal(in);
/*      */   }
/*      */ 
/*      */   public void parse(String uri)
/*      */     throws IOException, SAXException
/*      */   {
/*  220 */     init();
/*      */ 
/*  222 */     InputSource in = this.resolver.resolveEntity(null, uri);
/*      */ 
/*  225 */     if (in == null) {
/*  226 */       in = Resolver.createInputSource(new URL(uri), false);
/*      */     }
/*  231 */     else if (in.getSystemId() == null) {
/*  232 */       warning("P-065", null);
/*  233 */       in.setSystemId(uri);
/*      */     }
/*      */ 
/*  236 */     parseInternal(in);
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*  241 */     this.in = null;
/*      */ 
/*  244 */     this.strTmp = new StringBuffer();
/*  245 */     this.nameTmp = new char[20];
/*  246 */     this.nameCache = new NameCache();
/*      */ 
/*  251 */     this.doLexicalPE = false;
/*      */ 
/*  253 */     this.entities.clear();
/*  254 */     this.notations.clear();
/*  255 */     this.params.clear();
/*      */ 
/*  257 */     this.declaredElements.clear();
/*      */ 
/*  260 */     builtin("amp", "&#38;");
/*  261 */     builtin("lt", "&#60;");
/*  262 */     builtin("gt", ">");
/*  263 */     builtin("quot", "\"");
/*  264 */     builtin("apos", "'");
/*      */ 
/*  266 */     if (this.locale == null)
/*  267 */       this.locale = Locale.getDefault();
/*  268 */     if (this.resolver == null)
/*  269 */       this.resolver = new Resolver();
/*  270 */     if (this.dtdHandler == null)
/*  271 */       this.dtdHandler = new DTDHandlerBase();
/*      */   }
/*      */ 
/*      */   private void builtin(String entityName, String entityValue)
/*      */   {
/*  276 */     InternalEntity entity = new InternalEntity(entityName, entityValue.toCharArray());
/*  277 */     this.entities.put(entityName, entity);
/*      */   }
/*      */ 
/*      */   private void parseInternal(InputSource input)
/*      */     throws IOException, SAXException
/*      */   {
/*  297 */     if (input == null)
/*  298 */       fatal("P-000");
/*      */     try
/*      */     {
/*  301 */       this.in = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/*  302 */       this.in.init(input, null, null, false);
/*      */ 
/*  304 */       this.dtdHandler.startDTD(this.in);
/*      */ 
/*  311 */       ExternalEntity externalSubset = new ExternalEntity(this.in);
/*  312 */       externalParameterEntity(externalSubset);
/*      */ 
/*  314 */       if (!this.in.isEOF()) {
/*  315 */         fatal("P-001", new Object[] { 
/*  316 */           Integer.toHexString(getc()) });
/*      */       }
/*  318 */       afterRoot();
/*  319 */       this.dtdHandler.endDTD();
/*      */     }
/*      */     catch (EndOfInputException e) {
/*  322 */       if (!this.in.isDocument()) {
/*  323 */         String name = this.in.getName();
/*      */         do
/*  325 */           this.in = this.in.pop();
/*  326 */         while (this.in.isInternal());
/*  327 */         fatal("P-002", new Object[] { name });
/*      */       } else {
/*  329 */         fatal("P-003", null);
/*      */       }
/*      */     }
/*      */     catch (RuntimeException e)
/*      */     {
/*  334 */       System.err.print("Internal DTD parser error: ");
/*  335 */       e.printStackTrace();
/*      */ 
/*  339 */       throw new SAXParseException(e.getMessage() != null ? e
/*  337 */         .getMessage() : e.getClass().getName(), 
/*  338 */         getPublicId(), getSystemId(), 
/*  339 */         getLineNumber(), getColumnNumber());
/*      */     }
/*      */     finally
/*      */     {
/*  343 */       this.strTmp = null;
/*  344 */       this.nameTmp = null;
/*  345 */       this.nameCache = null;
/*      */ 
/*  348 */       if (this.in != null) {
/*  349 */         this.in.close();
/*  350 */         this.in = null;
/*      */       }
/*      */ 
/*  356 */       this.params.clear();
/*  357 */       this.entities.clear();
/*  358 */       this.notations.clear();
/*  359 */       this.declaredElements.clear();
/*      */ 
/*  361 */       this.ids.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   void afterRoot()
/*      */     throws SAXException
/*      */   {
/*  370 */     Enumeration e = this.ids.keys();
/*  371 */     while (e.hasMoreElements())
/*      */     {
/*  373 */       String id = (String)e.nextElement();
/*  374 */       Boolean value = (Boolean)this.ids.get(id);
/*  375 */       if (Boolean.FALSE == value)
/*  376 */         error("V-024", new Object[] { id });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void whitespace(String roleId)
/*      */     throws IOException, SAXException
/*      */   {
/*  386 */     if (!maybeWhitespace())
/*  387 */       fatal("P-004", new Object[] { messages
/*  388 */         .getMessage(this.locale, roleId) });
/*      */   }
/*      */ 
/*      */   private boolean maybeWhitespace()
/*      */     throws IOException, SAXException
/*      */   {
/*  396 */     if (!this.doLexicalPE) {
/*  397 */       return this.in.maybeWhitespace();
/*      */     }
/*      */ 
/*  412 */     char c = getc();
/*  413 */     boolean saw = false;
/*      */ 
/*  415 */     while ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r')) {
/*  416 */       saw = true;
/*      */ 
/*  421 */       if ((this.in.isEOF()) && (!this.in.isInternal()))
/*  422 */         return saw;
/*  423 */       c = getc();
/*      */     }
/*  425 */     ungetc();
/*  426 */     return saw;
/*      */   }
/*      */ 
/*      */   private String maybeGetName()
/*      */     throws IOException, SAXException
/*      */   {
/*  432 */     NameCacheEntry entry = maybeGetNameCacheEntry();
/*  433 */     return entry == null ? null : entry.name;
/*      */   }
/*      */ 
/*      */   private NameCacheEntry maybeGetNameCacheEntry()
/*      */     throws IOException, SAXException
/*      */   {
/*  440 */     char c = getc();
/*      */ 
/*  442 */     if ((!XmlChars.isLetter(c)) && (c != ':') && (c != '_')) {
/*  443 */       ungetc();
/*  444 */       return null;
/*      */     }
/*  446 */     return nameCharString(c);
/*      */   }
/*      */ 
/*      */   private String getNmtoken()
/*      */     throws IOException, SAXException
/*      */   {
/*  454 */     char c = getc();
/*  455 */     if (!XmlChars.isNameChar(c))
/*  456 */       fatal("P-006", new Object[] { new Character(c) });
/*  457 */     return nameCharString(c).name;
/*      */   }
/*      */ 
/*      */   private NameCacheEntry nameCharString(char c)
/*      */     throws IOException, SAXException
/*      */   {
/*  468 */     int i = 1;
/*      */ 
/*  470 */     this.nameTmp[0] = c;
/*      */ 
/*  472 */     while ((c = this.in.getNameChar()) != 0)
/*      */     {
/*  474 */       if (i >= this.nameTmp.length) {
/*  475 */         char[] tmp = new char[this.nameTmp.length + 10];
/*  476 */         System.arraycopy(this.nameTmp, 0, tmp, 0, this.nameTmp.length);
/*  477 */         this.nameTmp = tmp;
/*      */       }
/*  479 */       this.nameTmp[(i++)] = c;
/*      */     }
/*  481 */     return this.nameCache.lookupEntry(this.nameTmp, i);
/*      */   }
/*      */ 
/*      */   private void parseLiteral(boolean isEntityValue)
/*      */     throws IOException, SAXException
/*      */   {
/*  502 */     char quote = getc();
/*      */ 
/*  504 */     InputEntity source = this.in;
/*      */ 
/*  506 */     if ((quote != '\'') && (quote != '"')) {
/*  507 */       fatal("P-007");
/*      */     }
/*      */ 
/*  515 */     this.strTmp = new StringBuffer();
/*      */     while (true)
/*      */     {
/*  520 */       if ((this.in != source) && (this.in.isEOF()))
/*      */       {
/*  523 */         this.in = this.in.pop();
/*      */       }
/*      */       else
/*      */       {
/*      */         char c;
/*  526 */         if (((c = getc()) == quote) && (this.in == source))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  534 */         if (c == '&') {
/*  535 */           String entityName = maybeGetName();
/*      */ 
/*  537 */           if (entityName != null) {
/*  538 */             nextChar(';', "F-020", entityName);
/*      */ 
/*  542 */             if (isEntityValue) {
/*  543 */               this.strTmp.append('&');
/*  544 */               this.strTmp.append(entityName);
/*  545 */               this.strTmp.append(';');
/*      */             }
/*      */             else {
/*  548 */               expandEntityInLiteral(entityName, this.entities, isEntityValue);
/*      */             }
/*      */ 
/*      */           }
/*  552 */           else if ((c = getc()) == '#') {
/*  553 */             int tmp = parseCharNumber();
/*      */ 
/*  555 */             if (tmp > 65535) {
/*  556 */               tmp = surrogatesToCharTmp(tmp);
/*  557 */               this.strTmp.append(this.charTmp[0]);
/*  558 */               if (tmp == 2)
/*  559 */                 this.strTmp.append(this.charTmp[1]);
/*      */             } else {
/*  561 */               this.strTmp.append((char)tmp);
/*      */             }
/*      */           } else { fatal("P-009"); }
/*      */ 
/*      */ 
/*      */         }
/*  569 */         else if ((c == '%') && (isEntityValue)) {
/*  570 */           String entityName = maybeGetName();
/*      */ 
/*  572 */           if (entityName != null) {
/*  573 */             nextChar(';', "F-021", entityName);
/*  574 */             expandEntityInLiteral(entityName, this.params, isEntityValue);
/*      */           }
/*      */           else {
/*  577 */             fatal("P-011");
/*      */           }
/*      */ 
/*      */         }
/*  581 */         else if (!isEntityValue)
/*      */         {
/*  583 */           if ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r')) {
/*  584 */             this.strTmp.append(' ');
/*      */           }
/*  589 */           else if (c == '<')
/*  590 */             fatal("P-012");
/*      */         }
/*      */         else {
/*  593 */           this.strTmp.append(c);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void expandEntityInLiteral(String name, SimpleHashtable table, boolean isEntityValue)
/*      */     throws IOException, SAXException
/*      */   {
/*  603 */     Object entity = table.get(name);
/*      */ 
/*  605 */     if ((entity instanceof InternalEntity)) {
/*  606 */       InternalEntity value = (InternalEntity)entity;
/*  607 */       pushReader(value.buf, name, !value.isPE);
/*      */     }
/*  609 */     else if ((entity instanceof ExternalEntity)) {
/*  610 */       if (!isEntityValue) {
/*  611 */         fatal("P-013", new Object[] { name });
/*      */       }
/*  613 */       pushReader((ExternalEntity)entity);
/*      */     }
/*  615 */     else if (entity == null)
/*      */     {
/*  621 */       fatal(table == this.params ? "V-022" : "P-014", new Object[] { name });
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getQuotedString(String type, String extra)
/*      */     throws IOException, SAXException
/*      */   {
/*  638 */     char quote = this.in.getc();
/*      */ 
/*  640 */     if ((quote != '\'') && (quote != '"')) {
/*  641 */       fatal("P-015", new Object[] { messages
/*  642 */         .getMessage(this.locale, type, new Object[] { extra }) });
/*      */     }
/*      */ 
/*  647 */     this.strTmp = new StringBuffer();
/*      */     char c;
/*  648 */     while ((c = this.in.getc()) != quote)
/*  649 */       this.strTmp.append(c);
/*  650 */     return this.strTmp.toString();
/*      */   }
/*      */ 
/*      */   private String parsePublicId()
/*      */     throws IOException, SAXException
/*      */   {
/*  658 */     String retval = getQuotedString("F-033", null);
/*  659 */     for (int i = 0; i < retval.length(); i++) {
/*  660 */       char c = retval.charAt(i);
/*  661 */       if ((" \r\n-'()+,./:=?;!*#@$_%0123456789".indexOf(c) == -1) && ((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')))
/*      */       {
/*  664 */         fatal("P-016", new Object[] { new Character(c) });
/*      */       }
/*      */     }
/*  666 */     this.strTmp = new StringBuffer();
/*  667 */     this.strTmp.append(retval);
/*  668 */     return normalize(false);
/*      */   }
/*      */ 
/*      */   private boolean maybeComment(boolean skipStart)
/*      */     throws IOException, SAXException
/*      */   {
/*  680 */     if (!this.in.peek(skipStart ? "!--" : "<!--", null)) {
/*  681 */       return false;
/*      */     }
/*  683 */     boolean savedLexicalPE = this.doLexicalPE;
/*      */ 
/*  686 */     this.doLexicalPE = false;
/*  687 */     boolean saveCommentText = false;
/*  688 */     if (saveCommentText) {
/*  689 */       this.strTmp = new StringBuffer();
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*      */       try
/*      */       {
/*  697 */         int c = getc();
/*  698 */         if (c == 45) {
/*  699 */           c = getc();
/*  700 */           if (c != 45) {
/*  701 */             if (saveCommentText)
/*  702 */               this.strTmp.append('-');
/*  703 */             ungetc();
/*      */           }
/*      */           else {
/*  706 */             nextChar('>', "F-022", null);
/*  707 */             break;
/*      */           }
/*  709 */         } else if (saveCommentText) {
/*  710 */           this.strTmp.append((char)c);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (EndOfInputException e)
/*      */       {
/*  719 */         if (this.in.isInternal()) {
/*  720 */           error("V-021", null);
/*      */         }
/*  722 */         fatal("P-017");
/*      */       }
/*      */     }
/*  725 */     this.doLexicalPE = savedLexicalPE;
/*  726 */     if (saveCommentText)
/*  727 */       this.dtdHandler.comment(this.strTmp.toString());
/*  728 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean maybePI(boolean skipStart)
/*      */     throws IOException, SAXException
/*      */   {
/*  738 */     boolean savedLexicalPE = this.doLexicalPE;
/*      */ 
/*  740 */     if (!this.in.peek(skipStart ? "?" : "<?", null))
/*  741 */       return false;
/*  742 */     this.doLexicalPE = false;
/*      */ 
/*  744 */     String target = maybeGetName();
/*      */ 
/*  746 */     if (target == null) {
/*  747 */       fatal("P-018");
/*      */     }
/*  749 */     if ("xml".equals(target)) {
/*  750 */       fatal("P-019");
/*      */     }
/*  752 */     if ("xml".equalsIgnoreCase(target)) {
/*  753 */       fatal("P-020", new Object[] { target });
/*      */     }
/*      */ 
/*  756 */     if (maybeWhitespace()) {
/*  757 */       this.strTmp = new StringBuffer();
/*      */       try
/*      */       {
/*      */         while (true) {
/*  761 */           char c = this.in.getc();
/*      */ 
/*  763 */           if ((c == '?') && (this.in.peekc('>')))
/*      */             break;
/*  765 */           this.strTmp.append(c);
/*      */         }
/*      */       } catch (EndOfInputException e) {
/*  768 */         fatal("P-021");
/*      */       }
/*  770 */       this.dtdHandler.processingInstruction(target, this.strTmp.toString());
/*      */     } else {
/*  772 */       if (!this.in.peek("?>", null)) {
/*  773 */         fatal("P-022");
/*      */       }
/*  775 */       this.dtdHandler.processingInstruction(target, "");
/*      */     }
/*      */ 
/*  778 */     this.doLexicalPE = savedLexicalPE;
/*  779 */     return true;
/*      */   }
/*      */ 
/*      */   private String maybeReadAttribute(String name, boolean must)
/*      */     throws IOException, SAXException
/*      */   {
/*  797 */     if (!maybeWhitespace()) {
/*  798 */       if (!must) {
/*  799 */         return null;
/*      */       }
/*  801 */       fatal("P-024", new Object[] { name });
/*      */     }
/*      */ 
/*  805 */     if (!peek(name)) {
/*  806 */       if (must) {
/*  807 */         fatal("P-024", new Object[] { name });
/*      */       }
/*      */       else
/*      */       {
/*  812 */         ungetc();
/*  813 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  818 */     maybeWhitespace();
/*  819 */     nextChar('=', "F-023", null);
/*  820 */     maybeWhitespace();
/*      */ 
/*  822 */     return getQuotedString("F-035", name);
/*      */   }
/*      */ 
/*      */   private void readVersion(boolean must, String versionNum)
/*      */     throws IOException, SAXException
/*      */   {
/*  828 */     String value = maybeReadAttribute("version", must);
/*      */ 
/*  832 */     if ((must) && (value == null))
/*  833 */       fatal("P-025", new Object[] { versionNum });
/*  834 */     if (value != null) {
/*  835 */       int length = value.length();
/*  836 */       for (int i = 0; i < length; i++) {
/*  837 */         char c = value.charAt(i);
/*  838 */         if (((c < '0') || (c > '9')) && (c != '_') && (c != '.') && ((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != ':') && (c != '-'))
/*      */         {
/*  844 */           fatal("P-026", new Object[] { value });
/*      */         }
/*      */       }
/*      */     }
/*  847 */     if ((value != null) && (!value.equals(versionNum)))
/*  848 */       error("P-027", new Object[] { versionNum, value });
/*      */   }
/*      */ 
/*      */   private String getMarkupDeclname(String roleId, boolean qname)
/*      */     throws IOException, SAXException
/*      */   {
/*  858 */     whitespace(roleId);
/*  859 */     String name = maybeGetName();
/*  860 */     if (name == null) {
/*  861 */       fatal("P-005", new Object[] { messages
/*  862 */         .getMessage(this.locale, roleId) });
/*      */     }
/*  863 */     return name;
/*      */   }
/*      */ 
/*      */   private boolean maybeMarkupDecl()
/*      */     throws IOException, SAXException
/*      */   {
/*  876 */     return (maybeElementDecl()) || 
/*  872 */       (maybeAttlistDecl()) || 
/*  873 */       (maybeEntityDecl()) || 
/*  874 */       (maybeNotationDecl()) || 
/*  875 */       (maybePI(false)) || 
/*  876 */       (maybeComment(false));
/*      */   }
/*      */ 
/*      */   private boolean isXmlLang(String value)
/*      */   {
/*  896 */     if (value.length() < 2)
/*  897 */       return false;
/*  898 */     char c = value.charAt(1);
/*      */     int nextSuffix;
/*  899 */     if (c == '-') {
/*  900 */       c = value.charAt(0);
/*  901 */       if ((c != 'i') && (c != 'I') && (c != 'x') && (c != 'X'))
/*  902 */         return false;
/*  903 */       nextSuffix = 1;
/*      */     }
/*      */     else
/*      */     {
/*      */       int nextSuffix;
/*  904 */       if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
/*      */       {
/*  906 */         c = value.charAt(0);
/*  907 */         if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')))
/*  908 */           return false;
/*  909 */         nextSuffix = 2; } else { return false;
/*      */         break label145;
/*      */         break label145; }  } int nextSuffix;
/*      */     while (true) { if (nextSuffix >= value.length()) break label189;
/*  915 */       c = value.charAt(nextSuffix);
/*  916 */       if (c != '-')
/*      */         break label189;
/*  918 */       label145: nextSuffix++; if (nextSuffix < value.length()) {
/*  919 */         c = value.charAt(nextSuffix);
/*  920 */         if ((c >= 'a') && (c <= 'z')) break; if (c >= 'A') if (c <= 'Z')
/*      */             break;
/*      */       }
/*      */     }
/*  924 */     label189: return (value.length() == nextSuffix) && (c != '-');
/*      */   }
/*      */ 
/*      */   private boolean maybeElementDecl()
/*      */     throws IOException, SAXException
/*      */   {
/* 1063 */     InputEntity start = peekDeclaration("!ELEMENT");
/*      */ 
/* 1065 */     if (start == null) {
/* 1066 */       return false;
/*      */     }
/*      */ 
/* 1070 */     String name = getMarkupDeclname("F-015", true);
/*      */ 
/* 1085 */     if (this.declaredElements.contains(name))
/* 1086 */       error("V-012", new Object[] { name });
/*      */     else {
/* 1088 */       this.declaredElements.add(name);
/*      */     }
/*      */ 
/* 1093 */     whitespace("F-000");
/*      */     short modelType;
/* 1094 */     if (peek("EMPTY"))
/*      */     {
/*      */       short modelType;
/* 1096 */       this.dtdHandler.startContentModel(name, modelType = 0);
/* 1097 */     } else if (peek("ANY"))
/*      */     {
/*      */       short modelType;
/* 1099 */       this.dtdHandler.startContentModel(name, modelType = 1);
/*      */     } else {
/* 1101 */       modelType = getMixedOrChildren(name);
/*      */     }
/*      */ 
/* 1104 */     this.dtdHandler.endContentModel(name, modelType);
/*      */ 
/* 1106 */     maybeWhitespace();
/* 1107 */     char c = getc();
/* 1108 */     if (c != '>')
/* 1109 */       fatal("P-036", new Object[] { name, new Character(c) });
/* 1110 */     if (start != this.in) {
/* 1111 */       error("V-013", null);
/*      */     }
/*      */ 
/* 1115 */     return true;
/*      */   }
/*      */ 
/*      */   private short getMixedOrChildren(String elementName)
/*      */     throws IOException, SAXException
/*      */   {
/* 1132 */     this.strTmp = new StringBuffer();
/*      */ 
/* 1134 */     nextChar('(', "F-028", elementName);
/* 1135 */     InputEntity start = this.in;
/* 1136 */     maybeWhitespace();
/* 1137 */     this.strTmp.append('(');
/*      */     short modelType;
/* 1140 */     if (peek("#PCDATA")) {
/* 1141 */       this.strTmp.append("#PCDATA");
/*      */       short modelType;
/* 1142 */       this.dtdHandler.startContentModel(elementName, modelType = 2);
/* 1143 */       getMixed(elementName, start);
/*      */     } else {
/* 1145 */       this.dtdHandler.startContentModel(elementName, modelType = 3);
/* 1146 */       getcps(elementName, start);
/*      */     }
/*      */ 
/* 1149 */     return modelType;
/*      */   }
/*      */ 
/*      */   private void getcps(String elementName, InputEntity start)
/*      */     throws IOException, SAXException
/*      */   {
/* 1160 */     boolean decided = false;
/* 1161 */     char type = '\000';
/*      */ 
/* 1166 */     this.dtdHandler.startModelGroup();
/*      */     do
/*      */     {
/* 1171 */       String tag = maybeGetName();
/* 1172 */       if (tag != null) {
/* 1173 */         this.strTmp.append(tag);
/*      */ 
/* 1177 */         this.dtdHandler.childElement(tag, getFrequency());
/*      */       }
/* 1179 */       else if (peek("(")) {
/* 1180 */         InputEntity next = this.in;
/* 1181 */         this.strTmp.append('(');
/* 1182 */         maybeWhitespace();
/*      */ 
/* 1186 */         getcps(elementName, next);
/*      */       }
/*      */       else
/*      */       {
/* 1190 */         fatal(type == ',' ? "P-037" : type == 0 ? "P-039" : "P-038", new Object[] { new Character(
/* 1192 */           getc()) });
/*      */       }
/* 1194 */       maybeWhitespace();
/* 1195 */       if (decided) {
/* 1196 */         char c = getc();
/*      */ 
/* 1201 */         if (c == type) {
/* 1202 */           this.strTmp.append(type);
/* 1203 */           maybeWhitespace();
/* 1204 */           reportConnector(type);
/* 1205 */           continue;
/* 1206 */         }if (c == ')') {
/* 1207 */           ungetc();
/* 1208 */           continue;
/*      */         }
/* 1210 */         fatal(type == 0 ? "P-041" : "P-040", new Object[] { new Character(c), new Character(type) });
/*      */       }
/*      */       else
/*      */       {
/* 1217 */         type = getc();
/* 1218 */         switch (type) {
/*      */         case ',':
/*      */         case '|':
/* 1221 */           reportConnector(type);
/* 1222 */           break;
/*      */         default:
/* 1225 */           ungetc();
/* 1226 */           break;
/*      */         }
/*      */ 
/* 1229 */         decided = true;
/*      */ 
/* 1231 */         this.strTmp.append(type);
/*      */       }
/* 1233 */       maybeWhitespace();
/* 1234 */     }while (!peek(")"));
/*      */ 
/* 1236 */     if (this.in != start)
/* 1237 */       error("V-014", new Object[] { elementName });
/* 1238 */     this.strTmp.append(')');
/*      */ 
/* 1240 */     this.dtdHandler.endModelGroup(getFrequency());
/*      */   }
/*      */ 
/*      */   private void reportConnector(char type) throws SAXException
/*      */   {
/* 1245 */     switch (type) {
/*      */     case '|':
/* 1247 */       this.dtdHandler.connector((short)0);
/* 1248 */       return;
/*      */     case ',':
/* 1250 */       this.dtdHandler.connector((short)1);
/* 1251 */       return;
/*      */     }
/* 1253 */     throw new Error();
/*      */   }
/*      */ 
/*      */   private short getFrequency()
/*      */     throws IOException, SAXException
/*      */   {
/* 1260 */     char c = getc();
/*      */ 
/* 1262 */     if (c == '?') {
/* 1263 */       this.strTmp.append(c);
/* 1264 */       return 2;
/*      */     }
/* 1266 */     if (c == '+') {
/* 1267 */       this.strTmp.append(c);
/* 1268 */       return 1;
/*      */     }
/* 1270 */     if (c == '*') {
/* 1271 */       this.strTmp.append(c);
/* 1272 */       return 0;
/*      */     }
/*      */ 
/* 1275 */     ungetc();
/* 1276 */     return 3;
/*      */   }
/*      */ 
/*      */   private void getMixed(String elementName, InputEntity start)
/*      */     throws IOException, SAXException
/*      */   {
/* 1287 */     maybeWhitespace();
/* 1288 */     if ((peek(")*")) || (peek(")"))) {
/* 1289 */       if (this.in != start)
/* 1290 */         error("V-014", new Object[] { elementName });
/* 1291 */       this.strTmp.append(')');
/*      */ 
/* 1293 */       return;
/*      */     }
/*      */ 
/* 1296 */     ArrayList l = new ArrayList();
/*      */ 
/* 1300 */     while (peek("|"))
/*      */     {
/* 1303 */       this.strTmp.append('|');
/* 1304 */       maybeWhitespace();
/*      */ 
/* 1306 */       this.doLexicalPE = true;
/* 1307 */       String name = maybeGetName();
/* 1308 */       if (name == null)
/* 1309 */         fatal("P-042", new Object[] { elementName, 
/* 1310 */           Integer.toHexString(getc()) });
/* 1311 */       if (l.contains(name)) {
/* 1312 */         error("V-015", new Object[] { name });
/*      */       } else {
/* 1314 */         l.add(name);
/* 1315 */         this.dtdHandler.mixedElement(name);
/*      */       }
/* 1317 */       this.strTmp.append(name);
/* 1318 */       maybeWhitespace();
/*      */     }
/*      */ 
/* 1321 */     if (!peek(")*"))
/* 1322 */       fatal("P-043", new Object[] { elementName, new Character(
/* 1323 */         getc()) });
/* 1324 */     if (this.in != start)
/* 1325 */       error("V-014", new Object[] { elementName });
/* 1326 */     this.strTmp.append(')');
/*      */   }
/*      */ 
/*      */   private boolean maybeAttlistDecl()
/*      */     throws IOException, SAXException
/*      */   {
/* 1336 */     InputEntity start = peekDeclaration("!ATTLIST");
/*      */ 
/* 1338 */     if (start == null) {
/* 1339 */       return false;
/*      */     }
/* 1341 */     String elementName = getMarkupDeclname("F-016", true);
/*      */ 
/* 1350 */     while (!peek(">"))
/*      */     {
/* 1356 */       maybeWhitespace();
/* 1357 */       char c = getc();
/* 1358 */       if (c == '%') {
/* 1359 */         String entityName = maybeGetName();
/* 1360 */         if (entityName != null) {
/* 1361 */           nextChar(';', "F-021", entityName);
/* 1362 */           whitespace("F-021");
/*      */         }
/*      */         else {
/* 1365 */           fatal("P-011");
/*      */         }
/*      */       } else {
/* 1368 */         ungetc();
/*      */ 
/* 1370 */         String attName = maybeGetName();
/* 1371 */         if (attName == null) {
/* 1372 */           fatal("P-044", new Object[] { new Character(getc()) });
/*      */         }
/* 1374 */         whitespace("F-001");
/*      */ 
/* 1379 */         Vector values = null;
/*      */         String typeName;
/*      */         String typeName;
/* 1385 */         if (peek("CDATA"))
/*      */         {
/* 1387 */           typeName = "CDATA";
/*      */         }
/*      */         else
/*      */         {
/*      */           String typeName;
/* 1394 */           if (peek("IDREFS")) {
/* 1395 */             typeName = "IDREFS";
/*      */           }
/*      */           else
/*      */           {
/*      */             String typeName;
/* 1396 */             if (peek("IDREF")) {
/* 1397 */               typeName = "IDREF";
/*      */             }
/*      */             else
/*      */             {
/*      */               String typeName;
/* 1398 */               if (peek("ID")) {
/* 1399 */                 typeName = "ID";
/*      */               }
/*      */               else
/*      */               {
/*      */                 String typeName;
/* 1405 */                 if (peek("ENTITY")) {
/* 1406 */                   typeName = "ENTITY";
/*      */                 }
/*      */                 else
/*      */                 {
/*      */                   String typeName;
/* 1407 */                   if (peek("ENTITIES")) {
/* 1408 */                     typeName = "ENTITIES";
/*      */                   }
/*      */                   else
/*      */                   {
/*      */                     String typeName;
/* 1409 */                     if (peek("NMTOKENS")) {
/* 1410 */                       typeName = "NMTOKENS";
/*      */                     }
/*      */                     else
/*      */                     {
/*      */                       String typeName;
/* 1411 */                       if (peek("NMTOKEN")) {
/* 1412 */                         typeName = "NMTOKEN";
/*      */                       }
/* 1417 */                       else if (peek("NOTATION")) {
/* 1418 */                         String typeName = "NOTATION";
/* 1419 */                         whitespace("F-002");
/* 1420 */                         nextChar('(', "F-029", null);
/* 1421 */                         maybeWhitespace();
/*      */ 
/* 1423 */                         values = new Vector();
/*      */                         do
/*      */                         {
/*      */                           String name;
/* 1426 */                           if ((name = maybeGetName()) == null) {
/* 1427 */                             fatal("P-068");
/*      */                           }
/* 1429 */                           if (this.notations.get(name) == null)
/* 1430 */                             this.notations.put(name, name);
/* 1431 */                           values.addElement(name);
/* 1432 */                           maybeWhitespace();
/* 1433 */                           if (peek("|"))
/* 1434 */                             maybeWhitespace(); 
/*      */                         }
/* 1435 */                         while (!peek(")"));
/*      */                       }
/* 1441 */                       else if (peek("("))
/*      */                       {
/* 1443 */                         String typeName = "ENUMERATION";
/*      */ 
/* 1445 */                         maybeWhitespace();
/*      */ 
/* 1448 */                         values = new Vector();
/*      */                         do {
/* 1450 */                           String name = getNmtoken();
/*      */ 
/* 1452 */                           values.addElement(name);
/* 1453 */                           maybeWhitespace();
/* 1454 */                           if (peek("|"))
/* 1455 */                             maybeWhitespace(); 
/*      */                         }
/* 1456 */                         while (!peek(")"));
/*      */                       }
/*      */                       else
/*      */                       {
/* 1461 */                         fatal("P-045", new Object[] { attName, new Character(
/* 1462 */                           getc()) });
/* 1463 */                         typeName = null;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1467 */         String defaultValue = null;
/*      */ 
/* 1471 */         whitespace("F-003");
/*      */         short attributeUse;
/*      */         short attributeUse;
/* 1472 */         if (peek("#REQUIRED")) {
/* 1473 */           attributeUse = 3;
/*      */         }
/* 1475 */         else if (peek("#FIXED"))
/*      */         {
/* 1477 */           if (typeName == "ID") {
/* 1478 */             error("V-017", new Object[] { attName });
/*      */           }
/* 1480 */           short attributeUse = 2;
/* 1481 */           whitespace("F-004");
/* 1482 */           parseLiteral(false);
/*      */ 
/* 1488 */           if (typeName == "CDATA")
/* 1489 */             defaultValue = normalize(false);
/*      */           else {
/* 1491 */             defaultValue = this.strTmp.toString();
/*      */           }
/*      */ 
/*      */         }
/* 1496 */         else if (!peek("#IMPLIED")) {
/* 1497 */           short attributeUse = 1;
/*      */ 
/* 1500 */           if (typeName == "ID")
/* 1501 */             error("V-018", new Object[] { attName });
/* 1502 */           parseLiteral(false);
/*      */ 
/* 1507 */           if (typeName == "CDATA")
/* 1508 */             defaultValue = normalize(false);
/*      */           else {
/* 1510 */             defaultValue = this.strTmp.toString();
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1517 */           attributeUse = 0;
/*      */         }
/*      */ 
/* 1520 */         if (("xml:lang".equals(attName)) && (defaultValue != null))
/*      */         {
/* 1522 */           if (!isXmlLang(defaultValue))
/*      */           {
/* 1523 */             error("P-033", new Object[] { defaultValue });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1531 */         String[] v = values != null ? (String[])values.toArray(new String[0]) : null;
/* 1532 */         this.dtdHandler.attributeDecl(elementName, attName, typeName, v, attributeUse, defaultValue);
/* 1533 */         maybeWhitespace();
/*      */       }
/*      */     }
/* 1535 */     if (start != this.in)
/* 1536 */       error("V-013", null);
/* 1537 */     return true;
/*      */   }
/*      */ 
/*      */   private String normalize(boolean invalidIfNeeded)
/*      */   {
/* 1548 */     String s = this.strTmp.toString();
/* 1549 */     String s2 = s.trim();
/* 1550 */     boolean didStrip = false;
/*      */ 
/* 1552 */     if (s != s2) {
/* 1553 */       s = s2;
/* 1554 */       s2 = null;
/* 1555 */       didStrip = true;
/*      */     }
/* 1557 */     this.strTmp = new StringBuffer();
/* 1558 */     for (int i = 0; i < s.length(); i++) {
/* 1559 */       char c = s.charAt(i);
/* 1560 */       if (!XmlChars.isSpace(c)) {
/* 1561 */         this.strTmp.append(c);
/*      */       }
/*      */       else {
/* 1564 */         this.strTmp.append(' ');
/*      */         while (true) { i++; if ((i >= s.length()) || (!XmlChars.isSpace(s.charAt(i)))) break;
/* 1566 */           didStrip = true; }
/* 1567 */         i--;
/*      */       }
/*      */     }
/* 1569 */     if (didStrip) {
/* 1570 */       return this.strTmp.toString();
/*      */     }
/* 1572 */     return s;
/*      */   }
/*      */ 
/*      */   private boolean maybeConditionalSect()
/*      */     throws IOException, SAXException
/*      */   {
/* 1580 */     if (!peek("<![")) {
/* 1581 */       return false;
/*      */     }
/*      */ 
/* 1584 */     InputEntity start = this.in;
/*      */ 
/* 1586 */     maybeWhitespace();
/*      */     String keyword;
/* 1588 */     if ((keyword = maybeGetName()) == null)
/* 1589 */       fatal("P-046");
/* 1590 */     maybeWhitespace();
/* 1591 */     nextChar('[', "F-030", null);
/*      */ 
/* 1595 */     if ("INCLUDE".equals(keyword)) {
/*      */       while (true) {
/* 1597 */         if ((this.in.isEOF()) && (this.in != start)) {
/* 1598 */           this.in = this.in.pop(); } else {
/* 1599 */           if (this.in.isEOF()) {
/* 1600 */             error("V-020", null);
/*      */           }
/* 1602 */           if (peek("]]>")) {
/*      */             break;
/*      */           }
/* 1605 */           this.doLexicalPE = false;
/* 1606 */           if ((!maybeWhitespace()) && 
/* 1608 */             (!maybePEReference()))
/*      */           {
/* 1610 */             this.doLexicalPE = true;
/* 1611 */             if ((!maybeMarkupDecl()) && (!maybeConditionalSect()))
/*      */             {
/* 1614 */               fatal("P-047");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1622 */     if ("IGNORE".equals(keyword)) {
/* 1623 */       int nestlevel = 1;
/*      */ 
/* 1625 */       this.doLexicalPE = false;
/* 1626 */       while (nestlevel > 0) {
/* 1627 */         char c = getc();
/* 1628 */         if (c == '<') {
/* 1629 */           if (peek("!["))
/* 1630 */             nestlevel++;
/* 1631 */         } else if (c == ']')
/* 1632 */           if (peek("]>"))
/* 1633 */             nestlevel--;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1638 */       fatal("P-048", new Object[] { keyword });
/* 1639 */     }return true;
/*      */   }
/*      */ 
/*      */   private int parseCharNumber()
/*      */     throws IOException, SAXException
/*      */   {
/* 1652 */     int retval = 0;
/*      */ 
/* 1655 */     if (getc() != 'x') {
/* 1656 */       ungetc();
/*      */       while (true) {
/* 1658 */         char c = getc();
/* 1659 */         if ((c >= '0') && (c <= '9')) {
/* 1660 */           retval *= 10;
/* 1661 */           retval += c - '0';
/*      */         }
/*      */         else {
/* 1664 */           if (c == ';')
/* 1665 */             return retval;
/* 1666 */           fatal("P-049");
/*      */         }
/*      */       }
/*      */     }
/*      */     while (true) { char c = getc();
/* 1671 */       if ((c >= '0') && (c <= '9')) {
/* 1672 */         retval <<= 4;
/* 1673 */         retval += c - '0';
/*      */       }
/* 1676 */       else if ((c >= 'a') && (c <= 'f')) {
/* 1677 */         retval <<= 4;
/* 1678 */         retval += 10 + (c - 'a');
/*      */       }
/* 1681 */       else if ((c >= 'A') && (c <= 'F')) {
/* 1682 */         retval <<= 4;
/* 1683 */         retval += 10 + (c - 'A');
/*      */       }
/*      */       else {
/* 1686 */         if (c == ';')
/* 1687 */           return retval;
/* 1688 */         fatal("P-050");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int surrogatesToCharTmp(int ucs4)
/*      */     throws SAXException
/*      */   {
/* 1697 */     if (ucs4 <= 65535) {
/* 1698 */       if (XmlChars.isChar(ucs4)) {
/* 1699 */         this.charTmp[0] = ((char)ucs4);
/* 1700 */         return 1;
/*      */       }
/* 1702 */     } else if (ucs4 <= 1114111)
/*      */     {
/* 1704 */       ucs4 -= 65536;
/* 1705 */       this.charTmp[0] = ((char)(0xD800 | ucs4 >> 10 & 0x3FF));
/* 1706 */       this.charTmp[1] = ((char)(0xDC00 | ucs4 & 0x3FF));
/* 1707 */       return 2;
/*      */     }
/* 1709 */     fatal("P-051", new Object[] { Integer.toHexString(ucs4) });
/*      */ 
/* 1711 */     return -1;
/*      */   }
/*      */ 
/*      */   private boolean maybePEReference()
/*      */     throws IOException, SAXException
/*      */   {
/* 1722 */     if (!this.in.peekc('%')) {
/* 1723 */       return false;
/*      */     }
/* 1725 */     String name = maybeGetName();
/*      */ 
/* 1728 */     if (name == null)
/* 1729 */       fatal("P-011");
/* 1730 */     nextChar(';', "F-021", name);
/* 1731 */     Object entity = this.params.get(name);
/*      */ 
/* 1733 */     if ((entity instanceof InternalEntity)) {
/* 1734 */       InternalEntity value = (InternalEntity)entity;
/* 1735 */       pushReader(value.buf, name, false);
/*      */     }
/* 1737 */     else if ((entity instanceof ExternalEntity)) {
/* 1738 */       pushReader((ExternalEntity)entity);
/* 1739 */       externalParameterEntity((ExternalEntity)entity);
/*      */     }
/* 1741 */     else if (entity == null) {
/* 1742 */       error("V-022", new Object[] { name });
/*      */     }
/* 1744 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean maybeEntityDecl()
/*      */     throws IOException, SAXException
/*      */   {
/* 1756 */     InputEntity start = peekDeclaration("!ENTITY");
/*      */ 
/* 1758 */     if (start == null) {
/* 1759 */       return false;
/*      */     }
/*      */ 
/* 1773 */     this.doLexicalPE = false;
/* 1774 */     whitespace("F-005");
/*      */     SimpleHashtable defns;
/*      */     SimpleHashtable defns;
/* 1775 */     if (this.in.peekc('%')) {
/* 1776 */       whitespace("F-006");
/* 1777 */       defns = this.params;
/*      */     } else {
/* 1779 */       defns = this.entities;
/*      */     }
/* 1781 */     ungetc();
/* 1782 */     this.doLexicalPE = true;
/* 1783 */     String entityName = getMarkupDeclname("F-017", false);
/* 1784 */     whitespace("F-007");
/* 1785 */     ExternalEntity externalId = maybeExternalID();
/*      */ 
/* 1794 */     boolean doStore = defns.get(entityName) == null;
/* 1795 */     if ((!doStore) && (defns == this.entities)) {
/* 1796 */       warning("P-054", new Object[] { entityName });
/*      */     }
/*      */ 
/* 1799 */     if (externalId == null)
/*      */     {
/* 1803 */       this.doLexicalPE = false;
/* 1804 */       parseLiteral(true);
/* 1805 */       this.doLexicalPE = true;
/* 1806 */       if (doStore) {
/* 1807 */         char[] value = new char[this.strTmp.length()];
/* 1808 */         if (value.length != 0)
/* 1809 */           this.strTmp.getChars(0, value.length, value, 0);
/* 1810 */         InternalEntity entity = new InternalEntity(entityName, value);
/* 1811 */         entity.isPE = (defns == this.params);
/* 1812 */         entity.isFromInternalSubset = false;
/* 1813 */         defns.put(entityName, entity);
/* 1814 */         if (defns == this.entities) {
/* 1815 */           this.dtdHandler.internalGeneralEntityDecl(entityName, new String(value));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1822 */       if ((defns == this.entities) && (maybeWhitespace()) && 
/* 1823 */         (peek("NDATA")))
/*      */       {
/* 1824 */         externalId.notation = getMarkupDeclname("F-018", false);
/*      */ 
/* 1828 */         if (this.notations.get(externalId.notation) == null)
/* 1829 */           this.notations.put(externalId.notation, Boolean.TRUE);
/*      */       }
/* 1831 */       externalId.name = entityName;
/* 1832 */       externalId.isPE = (defns == this.params);
/* 1833 */       externalId.isFromInternalSubset = false;
/* 1834 */       if (doStore) {
/* 1835 */         defns.put(entityName, externalId);
/* 1836 */         if (externalId.notation != null) {
/* 1837 */           this.dtdHandler.unparsedEntityDecl(entityName, externalId.publicId, externalId.systemId, externalId.notation);
/*      */         }
/* 1840 */         else if (defns == this.entities) {
/* 1841 */           this.dtdHandler.externalGeneralEntityDecl(entityName, externalId.publicId, externalId.systemId);
/*      */         }
/*      */       }
/*      */     }
/* 1845 */     maybeWhitespace();
/* 1846 */     nextChar('>', "F-031", entityName);
/* 1847 */     if (start != this.in)
/* 1848 */       error("V-013", null);
/* 1849 */     return true;
/*      */   }
/*      */ 
/*      */   private ExternalEntity maybeExternalID()
/*      */     throws IOException, SAXException
/*      */   {
/* 1857 */     String temp = null;
/*      */ 
/* 1860 */     if (peek("PUBLIC")) {
/* 1861 */       whitespace("F-009");
/* 1862 */       temp = parsePublicId();
/* 1863 */     } else if (!peek("SYSTEM")) {
/* 1864 */       return null;
/*      */     }
/* 1866 */     ExternalEntity retval = new ExternalEntity(this.in);
/* 1867 */     retval.publicId = temp;
/* 1868 */     whitespace("F-008");
/* 1869 */     retval.systemId = parseSystemId();
/* 1870 */     return retval;
/*      */   }
/*      */ 
/*      */   private String parseSystemId()
/*      */     throws IOException, SAXException
/*      */   {
/* 1876 */     String uri = getQuotedString("F-034", null);
/* 1877 */     int temp = uri.indexOf(':');
/*      */ 
/* 1886 */     if ((temp == -1) || (uri.indexOf('/') < temp))
/*      */     {
/* 1889 */       String baseURI = this.in.getSystemId();
/* 1890 */       if (baseURI == null)
/* 1891 */         fatal("P-055", new Object[] { uri });
/* 1892 */       if (uri.length() == 0)
/* 1893 */         uri = ".";
/* 1894 */       baseURI = baseURI.substring(0, baseURI.lastIndexOf('/') + 1);
/* 1895 */       if (uri.charAt(0) != '/') {
/* 1896 */         uri = baseURI + uri;
/*      */       }
/*      */       else
/*      */       {
/* 1900 */         throw new InternalError();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1907 */     if (uri.indexOf('#') != -1)
/* 1908 */       error("P-056", new Object[] { uri });
/* 1909 */     return uri;
/*      */   }
/*      */ 
/*      */   private void maybeTextDecl()
/*      */     throws IOException, SAXException
/*      */   {
/* 1916 */     if (peek("<?xml")) {
/* 1917 */       readVersion(false, "1.0");
/* 1918 */       readEncoding(true);
/* 1919 */       maybeWhitespace();
/* 1920 */       if (!peek("?>"))
/* 1921 */         fatal("P-057");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void externalParameterEntity(ExternalEntity next)
/*      */     throws IOException, SAXException
/*      */   {
/* 1946 */     InputEntity pe = this.in;
/* 1947 */     maybeTextDecl();
/*      */     do { do { while (true) { if (pe.isEOF())
/*      */             break label87;
/* 1950 */           if (!this.in.isEOF()) break;
/* 1951 */           this.in = this.in.pop();
/*      */         }
/*      */ 
/* 1954 */         this.doLexicalPE = false; }
/* 1955 */       while ((maybeWhitespace()) || 
/* 1957 */         (maybePEReference()));
/*      */ 
/* 1959 */       this.doLexicalPE = true; }
/* 1960 */     while ((maybeMarkupDecl()) || (maybeConditionalSect()));
/*      */ 
/* 1965 */     label87: if (!pe.isEOF())
/* 1966 */       fatal("P-059", new Object[] { this.in.getName() });
/*      */   }
/*      */ 
/*      */   private void readEncoding(boolean must)
/*      */     throws IOException, SAXException
/*      */   {
/* 1973 */     String name = maybeReadAttribute("encoding", must);
/*      */ 
/* 1975 */     if (name == null)
/* 1976 */       return;
/* 1977 */     for (int i = 0; i < name.length(); i++) {
/* 1978 */       char c = name.charAt(i);
/* 1979 */       if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')))
/*      */       {
/* 1982 */         if ((i == 0) || (((c < '0') || (c > '9')) && (c != '-') && (c != '_') && (c != '.')))
/*      */         {
/* 1989 */           fatal("P-060", new Object[] { new Character(c) });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2001 */     String currentEncoding = this.in.getEncoding();
/*      */ 
/* 2003 */     if ((currentEncoding != null) && 
/* 2004 */       (!name
/* 2004 */       .equalsIgnoreCase(currentEncoding)))
/*      */     {
/* 2005 */       warning("P-061", new Object[] { name, currentEncoding });
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean maybeNotationDecl()
/*      */     throws IOException, SAXException
/*      */   {
/* 2014 */     InputEntity start = peekDeclaration("!NOTATION");
/*      */ 
/* 2016 */     if (start == null) {
/* 2017 */       return false;
/*      */     }
/* 2019 */     String name = getMarkupDeclname("F-019", false);
/* 2020 */     ExternalEntity entity = new ExternalEntity(this.in);
/*      */ 
/* 2022 */     whitespace("F-011");
/* 2023 */     if (peek("PUBLIC")) {
/* 2024 */       whitespace("F-009");
/* 2025 */       entity.publicId = parsePublicId();
/* 2026 */       if (maybeWhitespace())
/* 2027 */         if (!peek(">"))
/* 2028 */           entity.systemId = parseSystemId();
/*      */         else
/* 2030 */           ungetc();
/*      */     }
/* 2032 */     else if (peek("SYSTEM")) {
/* 2033 */       whitespace("F-008");
/* 2034 */       entity.systemId = parseSystemId();
/*      */     } else {
/* 2036 */       fatal("P-062");
/* 2037 */     }maybeWhitespace();
/* 2038 */     nextChar('>', "F-032", name);
/* 2039 */     if (start != this.in)
/* 2040 */       error("V-013", null);
/* 2041 */     if ((entity.systemId != null) && (entity.systemId.indexOf('#') != -1)) {
/* 2042 */       error("P-056", new Object[] { entity.systemId });
/*      */     }
/* 2044 */     Object value = this.notations.get(name);
/* 2045 */     if ((value != null) && ((value instanceof ExternalEntity))) {
/* 2046 */       warning("P-063", new Object[] { name });
/*      */     }
/*      */     else {
/* 2049 */       this.notations.put(name, entity);
/* 2050 */       this.dtdHandler.notationDecl(name, entity.publicId, entity.systemId);
/*      */     }
/*      */ 
/* 2053 */     return true;
/*      */   }
/*      */ 
/*      */   private char getc()
/*      */     throws IOException, SAXException
/*      */   {
/* 2065 */     if (!this.doLexicalPE) {
/* 2066 */       char c = this.in.getc();
/* 2067 */       return c;
/*      */     }
/*      */ 
/* 2088 */     while (this.in.isEOF())
/* 2089 */       if ((this.in.isInternal()) || ((this.doLexicalPE) && (!this.in.isDocument())))
/* 2090 */         this.in = this.in.pop();
/*      */       else
/* 2092 */         fatal("P-064", new Object[] { this.in.getName() });
/*      */     char c;
/* 2095 */     if (((c = this.in.getc()) == '%') && (this.doLexicalPE))
/*      */     {
/* 2097 */       String name = maybeGetName();
/*      */ 
/* 2100 */       if (name == null)
/* 2101 */         fatal("P-011");
/* 2102 */       nextChar(';', "F-021", name);
/* 2103 */       Object entity = this.params.get(name);
/*      */ 
/* 2107 */       pushReader(" ".toCharArray(), null, false);
/* 2108 */       if ((entity instanceof InternalEntity))
/* 2109 */         pushReader(((InternalEntity)entity).buf, name, false);
/* 2110 */       else if ((entity instanceof ExternalEntity))
/*      */       {
/* 2113 */         pushReader((ExternalEntity)entity);
/* 2114 */       } else if (entity == null)
/*      */       {
/* 2116 */         fatal("V-022");
/*      */       }
/* 2118 */       else throw new InternalError();
/* 2119 */       pushReader(" ".toCharArray(), null, false);
/* 2120 */       return this.in.getc();
/*      */     }
/* 2122 */     return c;
/*      */   }
/*      */ 
/*      */   private void ungetc()
/*      */   {
/* 2127 */     this.in.ungetc();
/*      */   }
/*      */ 
/*      */   private boolean peek(String s)
/*      */     throws IOException, SAXException
/*      */   {
/* 2133 */     return this.in.peek(s, null);
/*      */   }
/*      */ 
/*      */   private InputEntity peekDeclaration(String s)
/*      */     throws IOException, SAXException
/*      */   {
/* 2144 */     if (!this.in.peekc('<'))
/* 2145 */       return null;
/* 2146 */     InputEntity start = this.in;
/* 2147 */     if (this.in.peek(s, null))
/* 2148 */       return start;
/* 2149 */     this.in.ungetc();
/* 2150 */     return null;
/*      */   }
/*      */ 
/*      */   private void nextChar(char c, String location, String near)
/*      */     throws IOException, SAXException
/*      */   {
/* 2156 */     while ((this.in.isEOF()) && (!this.in.isDocument()))
/* 2157 */       this.in = this.in.pop();
/* 2158 */     if (!this.in.peekc(c))
/* 2159 */       fatal("P-008", new Object[] { new Character(c), messages
/* 2161 */         .getMessage(this.locale, location), 
/* 2161 */         '"' + near + '"' });
/*      */   }
/*      */ 
/*      */   private void pushReader(char[] buf, String name, boolean isGeneral)
/*      */     throws SAXException
/*      */   {
/* 2169 */     InputEntity r = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/* 2170 */     r.init(buf, name, this.in, !isGeneral);
/* 2171 */     this.in = r;
/*      */   }
/*      */ 
/*      */   private boolean pushReader(ExternalEntity next)
/*      */     throws IOException, SAXException
/*      */   {
/* 2177 */     InputEntity r = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/*      */     try
/*      */     {
/* 2180 */       s = next.getInputSource(this.resolver);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*      */       InputSource s;
/* 2182 */       String msg = "unable to open the external entity from :" + next.systemId;
/*      */ 
/* 2184 */       if (next.publicId != null) {
/* 2185 */         msg = msg + " (public id:" + next.publicId + ")";
/*      */       }
/*      */ 
/* 2188 */       SAXParseException spe = new SAXParseException(msg, 
/* 2188 */         getPublicId(), getSystemId(), getLineNumber(), getColumnNumber(), e);
/* 2189 */       this.dtdHandler.fatalError(spe);
/* 2190 */       throw e;
/*      */     }
/*      */     InputSource s;
/* 2193 */     r.init(s, next.name, this.in, next.isPE);
/* 2194 */     this.in = r;
/* 2195 */     return true;
/*      */   }
/*      */ 
/*      */   public String getPublicId()
/*      */   {
/* 2200 */     return this.in == null ? null : this.in.getPublicId();
/*      */   }
/*      */ 
/*      */   public String getSystemId()
/*      */   {
/* 2205 */     return this.in == null ? null : this.in.getSystemId();
/*      */   }
/*      */ 
/*      */   public int getLineNumber()
/*      */   {
/* 2210 */     return this.in == null ? -1 : this.in.getLineNumber();
/*      */   }
/*      */ 
/*      */   public int getColumnNumber()
/*      */   {
/* 2215 */     return this.in == null ? -1 : this.in.getColumnNumber();
/*      */   }
/*      */ 
/*      */   private void warning(String messageId, Object[] parameters)
/*      */     throws SAXException
/*      */   {
/* 2224 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), 
/* 2224 */       getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/* 2226 */     this.dtdHandler.warning(e);
/*      */   }
/*      */ 
/*      */   void error(String messageId, Object[] parameters)
/*      */     throws SAXException
/*      */   {
/* 2233 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), 
/* 2233 */       getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/* 2235 */     this.dtdHandler.error(e);
/*      */   }
/*      */ 
/*      */   private void fatal(String messageId) throws SAXException
/*      */   {
/* 2240 */     fatal(messageId, null);
/*      */   }
/*      */ 
/*      */   private void fatal(String messageId, Object[] parameters)
/*      */     throws SAXException
/*      */   {
/* 2247 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), 
/* 2247 */       getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/* 2249 */     this.dtdHandler.fatalError(e);
/*      */ 
/* 2251 */     throw e;
/*      */   }
/*      */ 
/*      */   static final class Catalog extends MessageCatalog
/*      */   {
/*      */     Catalog()
/*      */     {
/* 2346 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NameCache
/*      */   {
/* 2270 */     DTDParser.NameCacheEntry[] hashtable = new DTDParser.NameCacheEntry[541];
/*      */ 
/*      */     String lookup(char[] value, int len)
/*      */     {
/* 2277 */       return lookupEntry(value, len).name;
/*      */     }
/*      */ 
/*      */     DTDParser.NameCacheEntry lookupEntry(char[] value, int len)
/*      */     {
/* 2287 */       int index = 0;
/*      */ 
/* 2291 */       for (int i = 0; i < len; i++)
/* 2292 */         index = index * 31 + value[i];
/* 2293 */       index &= 2147483647;
/* 2294 */       index %= this.hashtable.length;
/*      */ 
/* 2297 */       for (DTDParser.NameCacheEntry entry = this.hashtable[index]; 
/* 2298 */         entry != null; 
/* 2299 */         entry = entry.next) {
/* 2300 */         if (entry.matches(value, len)) {
/* 2301 */           return entry;
/*      */         }
/*      */       }
/*      */ 
/* 2305 */       entry = new DTDParser.NameCacheEntry();
/* 2306 */       entry.chars = new char[len];
/* 2307 */       System.arraycopy(value, 0, entry.chars, 0, len);
/* 2308 */       entry.name = new String(entry.chars);
/*      */ 
/* 2314 */       entry.name = entry.name.intern();
/* 2315 */       entry.next = this.hashtable[index];
/* 2316 */       this.hashtable[index] = entry;
/* 2317 */       return entry;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NameCacheEntry {
/*      */     String name;
/*      */     char[] chars;
/*      */     NameCacheEntry next;
/*      */ 
/*      */     boolean matches(char[] value, int len) {
/* 2329 */       if (this.chars.length != len)
/* 2330 */         return false;
/* 2331 */       for (int i = 0; i < len; i++)
/* 2332 */         if (value[i] != this.chars[i])
/* 2333 */           return false;
/* 2334 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.dtdparser.DTDParser
 * JD-Core Version:    0.6.2
 */