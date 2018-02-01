/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class InlineNewInstanceExpression extends Expression
/*     */ {
/*     */   MemberDefinition field;
/*     */   Statement body;
/*     */ 
/*     */   InlineNewInstanceExpression(long paramLong, Type paramType, MemberDefinition paramMemberDefinition, Statement paramStatement)
/*     */   {
/*  48 */     super(151, paramLong, paramType);
/*  49 */     this.field = paramMemberDefinition;
/*  50 */     this.body = paramStatement;
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*     */   {
/*  56 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  59 */     if (this.body != null) {
/*  60 */       LocalMember localLocalMember = (LocalMember)this.field.getArguments().elementAt(0);
/*  61 */       Context localContext = new Context(paramContext, this);
/*  62 */       localContext.declare(paramEnvironment, localLocalMember);
/*  63 */       this.body = this.body.inline(paramEnvironment, localContext);
/*     */     }
/*  65 */     if ((this.body != null) && (this.body.op == 149)) {
/*  66 */       this.body = null;
/*     */     }
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   public Expression copyInline(Context paramContext)
/*     */   {
/*  75 */     InlineNewInstanceExpression localInlineNewInstanceExpression = (InlineNewInstanceExpression)clone();
/*  76 */     localInlineNewInstanceExpression.body = this.body.copyInline(paramContext, true);
/*  77 */     return localInlineNewInstanceExpression;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/*  84 */     codeCommon(paramEnvironment, paramContext, paramAssembler, false);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  87 */     codeCommon(paramEnvironment, paramContext, paramAssembler, true);
/*     */   }
/*     */ 
/*     */   private void codeCommon(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/*  91 */     paramAssembler.add(this.where, 187, this.field.getClassDeclaration());
/*  92 */     if (this.body != null) {
/*  93 */       LocalMember localLocalMember = (LocalMember)this.field.getArguments().elementAt(0);
/*  94 */       CodeContext localCodeContext = new CodeContext(paramContext, this);
/*  95 */       localCodeContext.declare(paramEnvironment, localLocalMember);
/*  96 */       paramAssembler.add(this.where, 58, new Integer(localLocalMember.number));
/*  97 */       this.body.code(paramEnvironment, localCodeContext, paramAssembler);
/*  98 */       paramAssembler.add(localCodeContext.breakLabel);
/*  99 */       if (paramBoolean)
/* 100 */         paramAssembler.add(this.where, 25, new Integer(localLocalMember.number));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 109 */     LocalMember localLocalMember = (LocalMember)this.field.getArguments().elementAt(0);
/* 110 */     paramPrintStream.println("(" + opNames[this.op] + "#" + localLocalMember.hashCode() + "=" + this.field.hashCode());
/* 111 */     if (this.body != null)
/* 112 */       this.body.print(paramPrintStream, 1);
/*     */     else {
/* 114 */       paramPrintStream.print("<empty>");
/*     */     }
/* 116 */     paramPrintStream.print(")");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.InlineNewInstanceExpression
 * JD-Core Version:    0.6.2
 */