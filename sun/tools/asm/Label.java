/*     */ package sun.tools.asm;
/*     */ 
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ public final class Label extends Instruction
/*     */ {
/*  41 */   static int labelCount = 0;
/*     */   int ID;
/*     */   int depth;
/*     */   MemberDefinition[] locals;
/*     */ 
/*     */   public Label()
/*     */   {
/*  50 */     super(0L, -1, null);
/*  51 */     this.ID = (++labelCount);
/*     */   }
/*     */ 
/*     */   Label getDestination()
/*     */   {
/*  60 */     Label localLabel = this;
/*  61 */     if ((this.next != null) && (this.next != this) && (this.depth == 0)) {
/*  62 */       this.depth = 1;
/*     */ 
/*  64 */       switch (this.next.opc) {
/*     */       case -1:
/*  66 */         localLabel = ((Label)this.next).getDestination();
/*  67 */         break;
/*     */       case 167:
/*  70 */         localLabel = ((Label)this.next.value).getDestination();
/*  71 */         break;
/*     */       case 18:
/*     */       case 19:
/*  75 */         if ((this.next.value instanceof Integer)) {
/*  76 */           Instruction localInstruction = this.next.next;
/*  77 */           if (localInstruction.opc == -1) {
/*  78 */             localInstruction = ((Label)localInstruction).getDestination().next;
/*     */           }
/*     */ 
/*  81 */           if (localInstruction.opc == 153) {
/*  82 */             if (((Integer)this.next.value).intValue() == 0) {
/*  83 */               localLabel = (Label)localInstruction.value;
/*     */             } else {
/*  85 */               localLabel = new Label();
/*  86 */               localLabel.next = localInstruction.next;
/*  87 */               localInstruction.next = localLabel;
/*     */             }
/*  89 */             localLabel = localLabel.getDestination();
/*     */           }
/*  92 */           else if (localInstruction.opc == 154) {
/*  93 */             if (((Integer)this.next.value).intValue() == 0) {
/*  94 */               localLabel = new Label();
/*  95 */               localLabel.next = localInstruction.next;
/*  96 */               localInstruction.next = localLabel;
/*     */             } else {
/*  98 */               localLabel = (Label)localInstruction.value;
/*     */             }
/* 100 */             localLabel = localLabel.getDestination(); } 
/* 101 */         }break;
/*     */       }
/*     */ 
/* 106 */       this.depth = 0;
/*     */     }
/* 108 */     return localLabel;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 112 */     String str = "$" + this.ID + ":";
/* 113 */     if (this.value != null)
/* 114 */       str = str + " stack=" + this.value;
/* 115 */     return str;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.Label
 * JD-Core Version:    0.6.2
 */