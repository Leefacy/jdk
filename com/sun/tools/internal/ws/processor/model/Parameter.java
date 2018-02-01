/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jws.WebParam.Mode;
/*     */ 
/*     */ public class Parameter extends ModelObject
/*     */ {
/*     */   private final String entityName;
/*     */   private String name;
/*     */   private JavaParameter javaParameter;
/*     */   private AbstractType type;
/*     */   private Block block;
/*     */   private Parameter link;
/*     */   private boolean embedded;
/*     */   private String typeName;
/*     */   private String customName;
/*     */   private WebParam.Mode mode;
/*     */   private int parameterOrderPosition;
/* 158 */   private List<String> annotations = new ArrayList();
/*     */ 
/*     */   public Parameter(String name, Entity entity)
/*     */   {
/*  44 */     super(entity);
/*  45 */     this.name = name;
/*  46 */     if ((entity instanceof Message))
/*  47 */       this.entityName = ((Message)entity).getName();
/*  48 */     else if ((entity instanceof MessagePart))
/*  49 */       this.entityName = ((MessagePart)entity).getName();
/*     */     else
/*  51 */       this.entityName = name;
/*     */   }
/*     */ 
/*     */   public String getEntityName()
/*     */   {
/*  58 */     return this.entityName;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  62 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String s) {
/*  66 */     this.name = s;
/*     */   }
/*     */ 
/*     */   public JavaParameter getJavaParameter() {
/*  70 */     return this.javaParameter;
/*     */   }
/*     */ 
/*     */   public void setJavaParameter(JavaParameter p) {
/*  74 */     this.javaParameter = p;
/*     */   }
/*     */ 
/*     */   public AbstractType getType() {
/*  78 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(AbstractType t) {
/*  82 */     this.type = t;
/*     */   }
/*     */ 
/*     */   public String getTypeName() {
/*  86 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public void setTypeName(String t) {
/*  90 */     this.typeName = t;
/*     */   }
/*     */ 
/*     */   public Block getBlock() {
/*  94 */     return this.block;
/*     */   }
/*     */ 
/*     */   public void setBlock(Block d) {
/*  98 */     this.block = d;
/*     */   }
/*     */ 
/*     */   public Parameter getLinkedParameter() {
/* 102 */     return this.link;
/*     */   }
/*     */ 
/*     */   public void setLinkedParameter(Parameter p) {
/* 106 */     this.link = p;
/*     */   }
/*     */ 
/*     */   public boolean isEmbedded() {
/* 110 */     return this.embedded;
/*     */   }
/*     */ 
/*     */   public void setEmbedded(boolean b) {
/* 114 */     this.embedded = b;
/*     */   }
/*     */ 
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/* 118 */     visitor.visit(this);
/*     */   }
/*     */ 
/*     */   public int getParameterIndex()
/*     */   {
/* 132 */     return this.parameterOrderPosition;
/*     */   }
/*     */ 
/*     */   public void setParameterIndex(int parameterOrderPosition) {
/* 136 */     this.parameterOrderPosition = parameterOrderPosition;
/*     */   }
/*     */ 
/*     */   public boolean isReturn() {
/* 140 */     return this.parameterOrderPosition == -1;
/*     */   }
/*     */ 
/*     */   public String getCustomName()
/*     */   {
/* 149 */     return this.customName;
/*     */   }
/*     */ 
/*     */   public void setCustomName(String customName)
/*     */   {
/* 155 */     this.customName = customName;
/*     */   }
/*     */ 
/*     */   public List<String> getAnnotations()
/*     */   {
/* 164 */     return this.annotations;
/*     */   }
/*     */ 
/*     */   public void setAnnotations(List<String> annotations)
/*     */   {
/* 172 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */   public void setMode(WebParam.Mode mode) {
/* 176 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */   public boolean isIN() {
/* 180 */     return this.mode == WebParam.Mode.IN;
/*     */   }
/*     */ 
/*     */   public boolean isOUT() {
/* 184 */     return this.mode == WebParam.Mode.OUT;
/*     */   }
/*     */ 
/*     */   public boolean isINOUT() {
/* 188 */     return this.mode == WebParam.Mode.INOUT;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Parameter
 * JD-Core Version:    0.6.2
 */