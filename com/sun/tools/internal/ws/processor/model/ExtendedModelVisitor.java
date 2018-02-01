/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ExtendedModelVisitor
/*     */ {
/*     */   public void visit(Model model)
/*     */     throws Exception
/*     */   {
/*  41 */     preVisit(model);
/*  42 */     for (Service service : model.getServices()) {
/*  43 */       preVisit(service);
/*  44 */       for (Port port : service.getPorts()) {
/*  45 */         preVisit(port);
/*  46 */         if (shouldVisit(port)) {
/*  47 */           for (Operation operation : port.getOperations()) {
/*  48 */             preVisit(operation);
/*  49 */             Request request = operation.getRequest();
/*  50 */             if (request != null) {
/*  51 */               preVisit(request);
/*  52 */               Iterator iter4 = request.getHeaderBlocks();
/*  53 */               while (iter4.hasNext())
/*     */               {
/*  55 */                 Block block = (Block)iter4.next();
/*  56 */                 visitHeaderBlock(block);
/*     */               }
/*  58 */               Iterator iter4 = request.getBodyBlocks();
/*  59 */               while (iter4.hasNext())
/*     */               {
/*  61 */                 Block block = (Block)iter4.next();
/*  62 */                 visitBodyBlock(block);
/*     */               }
/*  64 */               Iterator iter4 = request.getParameters();
/*  65 */               while (iter4.hasNext())
/*     */               {
/*  67 */                 Parameter parameter = (Parameter)iter4.next();
/*  68 */                 visit(parameter);
/*     */               }
/*  70 */               postVisit(request);
/*     */             }
/*     */ 
/*  73 */             Response response = operation.getResponse();
/*  74 */             if (response != null) {
/*  75 */               preVisit(response);
/*  76 */               Iterator iter4 = response.getHeaderBlocks();
/*  77 */               while (iter4.hasNext())
/*     */               {
/*  79 */                 Block block = (Block)iter4.next();
/*  80 */                 visitHeaderBlock(block);
/*     */               }
/*  82 */               Iterator iter4 = response.getBodyBlocks();
/*  83 */               while (iter4.hasNext())
/*     */               {
/*  85 */                 Block block = (Block)iter4.next();
/*  86 */                 visitBodyBlock(block);
/*     */               }
/*  88 */               Iterator iter4 = response.getParameters();
/*  89 */               while (iter4.hasNext())
/*     */               {
/*  91 */                 Parameter parameter = (Parameter)iter4.next();
/*  92 */                 visit(parameter);
/*     */               }
/*  94 */               postVisit(response);
/*     */             }
/*     */ 
/*  97 */             Iterator iter4 = operation.getFaults();
/*  98 */             while (iter4.hasNext())
/*     */             {
/* 100 */               Fault fault = (Fault)iter4.next();
/* 101 */               preVisit(fault);
/* 102 */               visitFaultBlock(fault.getBlock());
/* 103 */               postVisit(fault);
/*     */             }
/* 105 */             postVisit(operation);
/*     */           }
/*     */         }
/* 108 */         postVisit(port);
/*     */       }
/* 110 */       postVisit(service);
/*     */     }
/* 112 */     postVisit(model);
/*     */   }
/*     */ 
/*     */   protected boolean shouldVisit(Port port) {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   protected void preVisit(Model model)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Model model)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Service service)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Service service)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Port port)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Port port)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Operation operation)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Operation operation)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Request request)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Request request)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Response response)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Response response)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void preVisit(Fault fault)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void postVisit(Fault fault)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void visitBodyBlock(Block block)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void visitHeaderBlock(Block block)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void visitFaultBlock(Block block)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void visit(Parameter parameter)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.ExtendedModelVisitor
 * JD-Core Version:    0.6.2
 */