/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ColumnFormat extends OptionFormat
/*     */ {
/*     */   private int number;
/*     */   private int width;
/*  39 */   private Alignment align = Alignment.CENTER;
/*  40 */   private Scale scale = Scale.RAW;
/*     */   private String format;
/*     */   private String header;
/*     */   private Expression expression;
/*     */   private Object previousValue;
/*     */ 
/*     */   public ColumnFormat(int paramInt)
/*     */   {
/*  47 */     super("Column" + paramInt);
/*  48 */     this.number = paramInt;
/*     */   }
/*     */ 
/*     */   public void validate()
/*     */     throws ParserException
/*     */   {
/*  59 */     if (this.expression == null)
/*     */     {
/*  61 */       throw new ParserException("Missing data statement in column " + this.number);
/*     */     }
/*  63 */     if (this.header == null)
/*     */     {
/*  67 */       throw new ParserException("Missing header statement in column " + this.number);
/*     */     }
/*  69 */     if (this.format == null)
/*     */     {
/*  72 */       this.format = "0";
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setWidth(int paramInt) {
/*  77 */     this.width = paramInt;
/*     */   }
/*     */ 
/*     */   public void setAlignment(Alignment paramAlignment) {
/*  81 */     this.align = paramAlignment;
/*     */   }
/*     */ 
/*     */   public void setScale(Scale paramScale) {
/*  85 */     this.scale = paramScale;
/*     */   }
/*     */ 
/*     */   public void setFormat(String paramString) {
/*  89 */     this.format = paramString;
/*     */   }
/*     */ 
/*     */   public void setHeader(String paramString) {
/*  93 */     this.header = paramString;
/*     */   }
/*     */ 
/*     */   public String getHeader() {
/*  97 */     return this.header;
/*     */   }
/*     */ 
/*     */   public String getFormat() {
/* 101 */     return this.format;
/*     */   }
/*     */ 
/*     */   public int getWidth() {
/* 105 */     return this.width;
/*     */   }
/*     */ 
/*     */   public Alignment getAlignment() {
/* 109 */     return this.align;
/*     */   }
/*     */ 
/*     */   public Scale getScale() {
/* 113 */     return this.scale;
/*     */   }
/*     */ 
/*     */   public Expression getExpression() {
/* 117 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression paramExpression) {
/* 121 */     this.expression = paramExpression;
/*     */   }
/*     */ 
/*     */   public void setPreviousValue(Object paramObject) {
/* 125 */     this.previousValue = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getPreviousValue() {
/* 129 */     return this.previousValue;
/*     */   }
/*     */ 
/*     */   public void printFormat(int paramInt) {
/* 133 */     String str = "  ";
/*     */ 
/* 135 */     StringBuilder localStringBuilder = new StringBuilder("");
/* 136 */     for (int i = 0; i < paramInt; i++) {
/* 137 */       localStringBuilder.append(str);
/*     */     }
/*     */ 
/* 140 */     System.out.println(localStringBuilder + this.name + " {");
/* 141 */     System.out.println(localStringBuilder + str + "name=" + this.name + ";data=" + this.expression
/* 142 */       .toString() + ";header=" + this.header + ";format=" + this.format + ";width=" + this.width + ";scale=" + this.scale
/* 144 */       .toString() + ";align=" + this.align.toString());
/*     */ 
/* 146 */     for (Iterator localIterator = this.children.iterator(); localIterator.hasNext(); ) {
/* 147 */       OptionFormat localOptionFormat = (OptionFormat)localIterator.next();
/* 148 */       localOptionFormat.printFormat(paramInt + 1);
/*     */     }
/*     */ 
/* 151 */     System.out.println(localStringBuilder + "}");
/*     */   }
/*     */ 
/*     */   public String getValue() {
/* 155 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.ColumnFormat
 * JD-Core Version:    0.6.2
 */