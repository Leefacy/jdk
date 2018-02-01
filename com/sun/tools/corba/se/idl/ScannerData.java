/*      */ package com.sun.tools.corba.se.idl;
/*      */ 
/*      */ class ScannerData
/*      */ {
/* 1598 */   String indent = "";
/* 1599 */   IncludeEntry fileEntry = null;
/* 1600 */   String filename = "";
/*      */ 
/* 1607 */   char[] fileBytes = null;
/* 1608 */   int fileIndex = 0;
/* 1609 */   int oldIndex = 0;
/*      */   char ch;
/* 1611 */   int line = 1;
/* 1612 */   int oldLine = 1;
/* 1613 */   boolean macrodata = false;
/* 1614 */   boolean includeIsImport = false;
/*      */ 
/*      */   public ScannerData()
/*      */   {
/*      */   }
/*      */ 
/*      */   public ScannerData(ScannerData paramScannerData)
/*      */   {
/* 1585 */     this.indent = paramScannerData.indent;
/* 1586 */     this.fileEntry = paramScannerData.fileEntry;
/* 1587 */     this.filename = paramScannerData.filename;
/* 1588 */     this.fileBytes = paramScannerData.fileBytes;
/* 1589 */     this.fileIndex = paramScannerData.fileIndex;
/* 1590 */     this.oldIndex = paramScannerData.oldIndex;
/* 1591 */     this.ch = paramScannerData.ch;
/* 1592 */     this.line = paramScannerData.line;
/* 1593 */     this.oldLine = paramScannerData.oldLine;
/* 1594 */     this.macrodata = paramScannerData.macrodata;
/* 1595 */     this.includeIsImport = paramScannerData.includeIsImport;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ScannerData
 * JD-Core Version:    0.6.2
 */