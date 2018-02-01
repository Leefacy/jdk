/*      */ package com.sun.tools.javac.file;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.zip.DataFormatException;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.ZipException;
/*      */ 
/*      */ public class ZipFileIndex
/*      */ {
/*   80 */   private static final String MIN_CHAR = String.valueOf('\000');
/*   81 */   private static final String MAX_CHAR = String.valueOf(65535);
/*      */   public static final long NOT_MODIFIED = -9223372036854775808L;
/*   86 */   private static final boolean NON_BATCH_MODE = System.getProperty("nonBatchMode") != null;
/*      */ 
/*   89 */   private Map<RelativePath.RelativeDirectory, DirectoryEntry> directories = Collections.emptyMap();
/*      */ 
/*   91 */   private Set<RelativePath.RelativeDirectory> allDirs = Collections.emptySet();
/*      */   final File zipFile;
/*      */   private Reference<File> absFileRef;
/*   96 */   long zipFileLastModified = -9223372036854775808L;
/*      */   private RandomAccessFile zipRandomFile;
/*      */   private Entry[] entries;
/*  100 */   private boolean readFromIndex = false;
/*  101 */   private File zipIndexFile = null;
/*  102 */   private boolean triedToReadIndex = false;
/*      */   final RelativePath.RelativeDirectory symbolFilePrefix;
/*      */   private final int symbolFilePrefixLength;
/*  105 */   private boolean hasPopulatedData = false;
/*  106 */   long lastReferenceTimeStamp = -9223372036854775808L;
/*      */   private final boolean usePreindexedCache;
/*      */   private final String preindexedCacheLocation;
/*  111 */   private boolean writeIndex = false;
/*      */ 
/*  113 */   private Map<String, SoftReference<RelativePath.RelativeDirectory>> relativeDirectoryCache = new HashMap();
/*      */   private SoftReference<Inflater> inflaterRef;
/*      */ 
/*      */   public synchronized boolean isOpen()
/*      */   {
/*  118 */     return this.zipRandomFile != null;
/*      */   }
/*      */ 
/*      */   ZipFileIndex(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, boolean paramBoolean1, boolean paramBoolean2, String paramString) throws IOException
/*      */   {
/*  123 */     this.zipFile = paramFile;
/*  124 */     this.symbolFilePrefix = paramRelativeDirectory;
/*  125 */     this.symbolFilePrefixLength = (paramRelativeDirectory == null ? 0 : paramRelativeDirectory
/*  126 */       .getPath().getBytes("UTF-8").length);
/*  127 */     this.writeIndex = paramBoolean1;
/*  128 */     this.usePreindexedCache = paramBoolean2;
/*  129 */     this.preindexedCacheLocation = paramString;
/*      */ 
/*  131 */     if (paramFile != null) {
/*  132 */       this.zipFileLastModified = paramFile.lastModified();
/*      */     }
/*      */ 
/*  136 */     checkIndex();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  141 */     return "ZipFileIndex[" + this.zipFile + "]";
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */     throws Throwable
/*      */   {
/*  147 */     closeFile();
/*  148 */     super.finalize();
/*      */   }
/*      */ 
/*      */   private boolean isUpToDate() {
/*  152 */     if ((this.zipFile != null) && ((!NON_BATCH_MODE) || 
/*  153 */       (this.zipFileLastModified == this.zipFile
/*  153 */       .lastModified())) && (this.hasPopulatedData))
/*      */     {
/*  155 */       return true;
/*      */     }
/*      */ 
/*  158 */     return false;
/*      */   }
/*      */ 
/*      */   private void checkIndex()
/*      */     throws IOException
/*      */   {
/*  166 */     int i = 1;
/*  167 */     if (!isUpToDate()) {
/*  168 */       closeFile();
/*  169 */       i = 0;
/*      */     }
/*      */ 
/*  172 */     if ((this.zipRandomFile != null) || (i != 0)) {
/*  173 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*  174 */       return;
/*      */     }
/*      */ 
/*  177 */     this.hasPopulatedData = true;
/*      */ 
/*  179 */     if (readIndex()) {
/*  180 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*  181 */       return;
/*      */     }
/*      */ 
/*  184 */     this.directories = Collections.emptyMap();
/*  185 */     this.allDirs = Collections.emptySet();
/*      */     try
/*      */     {
/*  188 */       openFile();
/*  189 */       long l = this.zipRandomFile.length();
/*  190 */       ZipDirectory localZipDirectory = new ZipDirectory(this.zipRandomFile, 0L, l, this);
/*  191 */       localZipDirectory.buildIndex();
/*      */     } finally {
/*  193 */       if (this.zipRandomFile != null) {
/*  194 */         closeFile();
/*      */       }
/*      */     }
/*      */ 
/*  198 */     this.lastReferenceTimeStamp = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   private void openFile() throws FileNotFoundException {
/*  202 */     if ((this.zipRandomFile == null) && (this.zipFile != null))
/*  203 */       this.zipRandomFile = new RandomAccessFile(this.zipFile, "r");
/*      */   }
/*      */ 
/*      */   private void cleanupState()
/*      */   {
/*  209 */     this.entries = Entry.EMPTY_ARRAY;
/*  210 */     this.directories = Collections.emptyMap();
/*  211 */     this.zipFileLastModified = -9223372036854775808L;
/*  212 */     this.allDirs = Collections.emptySet();
/*      */   }
/*      */ 
/*      */   public synchronized void close() {
/*  216 */     writeIndex();
/*  217 */     closeFile();
/*      */   }
/*      */ 
/*      */   private void closeFile() {
/*  221 */     if (this.zipRandomFile != null) {
/*      */       try {
/*  223 */         this.zipRandomFile.close();
/*      */       } catch (IOException localIOException) {
/*      */       }
/*  226 */       this.zipRandomFile = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized Entry getZipIndexEntry(RelativePath paramRelativePath)
/*      */   {
/*      */     try
/*      */     {
/*  235 */       checkIndex();
/*  236 */       DirectoryEntry localDirectoryEntry = (DirectoryEntry)this.directories.get(paramRelativePath.dirname());
/*  237 */       String str = paramRelativePath.basename();
/*  238 */       return localDirectoryEntry == null ? null : localDirectoryEntry.getEntry(str);
/*      */     } catch (IOException localIOException) {
/*      */     }
/*  241 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized com.sun.tools.javac.util.List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory)
/*      */   {
/*      */     try
/*      */     {
/*  250 */       checkIndex();
/*      */ 
/*  252 */       DirectoryEntry localDirectoryEntry = (DirectoryEntry)this.directories.get(paramRelativeDirectory);
/*  253 */       com.sun.tools.javac.util.List localList = localDirectoryEntry == null ? null : localDirectoryEntry.getFiles();
/*      */ 
/*  255 */       if (localList == null) {
/*  256 */         return com.sun.tools.javac.util.List.nil();
/*      */       }
/*  258 */       return localList;
/*      */     } catch (IOException localIOException) {
/*      */     }
/*  261 */     return com.sun.tools.javac.util.List.nil();
/*      */   }
/*      */ 
/*      */   public synchronized java.util.List<String> getDirectories(RelativePath.RelativeDirectory paramRelativeDirectory)
/*      */   {
/*      */     try {
/*  267 */       checkIndex();
/*      */ 
/*  269 */       DirectoryEntry localDirectoryEntry = (DirectoryEntry)this.directories.get(paramRelativeDirectory);
/*  270 */       com.sun.tools.javac.util.List localList = localDirectoryEntry == null ? null : localDirectoryEntry.getDirectories();
/*      */ 
/*  272 */       if (localList == null) {
/*  273 */         return com.sun.tools.javac.util.List.nil();
/*      */       }
/*      */ 
/*  276 */       return localList;
/*      */     } catch (IOException localIOException) {
/*      */     }
/*  279 */     return com.sun.tools.javac.util.List.nil();
/*      */   }
/*      */ 
/*      */   public synchronized Set<RelativePath.RelativeDirectory> getAllDirectories()
/*      */   {
/*      */     try {
/*  285 */       checkIndex();
/*  286 */       if (this.allDirs == Collections.EMPTY_SET) {
/*  287 */         this.allDirs = new LinkedHashSet(this.directories.keySet());
/*      */       }
/*      */ 
/*  290 */       return this.allDirs;
/*      */     } catch (IOException localIOException) {
/*      */     }
/*  293 */     return Collections.emptySet();
/*      */   }
/*      */ 
/*      */   public synchronized boolean contains(RelativePath paramRelativePath)
/*      */   {
/*      */     try
/*      */     {
/*  306 */       checkIndex();
/*  307 */       return getZipIndexEntry(paramRelativePath) != null;
/*      */     } catch (IOException localIOException) {
/*      */     }
/*  310 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized boolean isDirectory(RelativePath paramRelativePath)
/*      */     throws IOException
/*      */   {
/*  316 */     if (paramRelativePath.getPath().length() == 0) {
/*  317 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*  318 */       return true;
/*      */     }
/*      */ 
/*  321 */     checkIndex();
/*  322 */     return this.directories.get(paramRelativePath) != null;
/*      */   }
/*      */ 
/*      */   public synchronized long getLastModified(RelativePath.RelativeFile paramRelativeFile) throws IOException {
/*  326 */     Entry localEntry = getZipIndexEntry(paramRelativeFile);
/*  327 */     if (localEntry == null)
/*  328 */       throw new FileNotFoundException();
/*  329 */     return localEntry.getLastModified();
/*      */   }
/*      */ 
/*      */   public synchronized int length(RelativePath.RelativeFile paramRelativeFile) throws IOException {
/*  333 */     Entry localEntry = getZipIndexEntry(paramRelativeFile);
/*  334 */     if (localEntry == null) {
/*  335 */       throw new FileNotFoundException();
/*      */     }
/*  337 */     if (localEntry.isDir) {
/*  338 */       return 0;
/*      */     }
/*      */ 
/*  341 */     byte[] arrayOfByte = getHeader(localEntry);
/*      */ 
/*  343 */     if (get2ByteLittleEndian(arrayOfByte, 8) == 0) {
/*  344 */       return localEntry.compressedSize;
/*      */     }
/*  346 */     return localEntry.size;
/*      */   }
/*      */ 
/*      */   public synchronized byte[] read(RelativePath.RelativeFile paramRelativeFile) throws IOException
/*      */   {
/*  351 */     Entry localEntry = getZipIndexEntry(paramRelativeFile);
/*  352 */     if (localEntry == null)
/*  353 */       throw new FileNotFoundException("Path not found in ZIP: " + paramRelativeFile.path);
/*  354 */     return read(localEntry);
/*      */   }
/*      */ 
/*      */   synchronized byte[] read(Entry paramEntry) throws IOException {
/*  358 */     openFile();
/*  359 */     byte[] arrayOfByte = readBytes(paramEntry);
/*  360 */     closeFile();
/*  361 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public synchronized int read(RelativePath.RelativeFile paramRelativeFile, byte[] paramArrayOfByte) throws IOException {
/*  365 */     Entry localEntry = getZipIndexEntry(paramRelativeFile);
/*  366 */     if (localEntry == null)
/*  367 */       throw new FileNotFoundException();
/*  368 */     return read(localEntry, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   synchronized int read(Entry paramEntry, byte[] paramArrayOfByte) throws IOException
/*      */   {
/*  373 */     int i = readBytes(paramEntry, paramArrayOfByte);
/*  374 */     return i;
/*      */   }
/*      */ 
/*      */   private byte[] readBytes(Entry paramEntry) throws IOException {
/*  378 */     byte[] arrayOfByte1 = getHeader(paramEntry);
/*  379 */     int i = paramEntry.compressedSize;
/*  380 */     byte[] arrayOfByte2 = new byte[i];
/*  381 */     this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  382 */     this.zipRandomFile.readFully(arrayOfByte2, 0, i);
/*      */ 
/*  385 */     if (get2ByteLittleEndian(arrayOfByte1, 8) == 0) {
/*  386 */       return arrayOfByte2;
/*      */     }
/*  388 */     int j = paramEntry.size;
/*  389 */     byte[] arrayOfByte3 = new byte[j];
/*  390 */     if (inflate(arrayOfByte2, arrayOfByte3) != j) {
/*  391 */       throw new ZipException("corrupted zip file");
/*      */     }
/*  393 */     return arrayOfByte3;
/*      */   }
/*      */ 
/*      */   private int readBytes(Entry paramEntry, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  400 */     byte[] arrayOfByte1 = getHeader(paramEntry);
/*      */ 
/*  403 */     if (get2ByteLittleEndian(arrayOfByte1, 8) == 0) {
/*  404 */       this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  405 */       i = 0;
/*  406 */       int j = paramArrayOfByte.length;
/*  407 */       while (i < j) {
/*  408 */         k = this.zipRandomFile.read(paramArrayOfByte, i, j - i);
/*  409 */         if (k == -1)
/*      */           break;
/*  411 */         i += k;
/*      */       }
/*  413 */       return paramEntry.size;
/*      */     }
/*      */ 
/*  416 */     int i = paramEntry.compressedSize;
/*  417 */     byte[] arrayOfByte2 = new byte[i];
/*  418 */     this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  419 */     this.zipRandomFile.readFully(arrayOfByte2, 0, i);
/*      */ 
/*  421 */     int k = inflate(arrayOfByte2, paramArrayOfByte);
/*  422 */     if (k == -1) {
/*  423 */       throw new ZipException("corrupted zip file");
/*      */     }
/*  425 */     return paramEntry.size;
/*      */   }
/*      */ 
/*      */   private byte[] getHeader(Entry paramEntry)
/*      */     throws IOException
/*      */   {
/*  433 */     this.zipRandomFile.seek(paramEntry.offset);
/*  434 */     byte[] arrayOfByte = new byte[30];
/*  435 */     this.zipRandomFile.readFully(arrayOfByte);
/*  436 */     if (get4ByteLittleEndian(arrayOfByte, 0) != 67324752)
/*  437 */       throw new ZipException("corrupted zip file");
/*  438 */     if ((get2ByteLittleEndian(arrayOfByte, 6) & 0x1) != 0)
/*  439 */       throw new ZipException("encrypted zip file");
/*  440 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private int inflate(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */   {
/*  448 */     Inflater localInflater = this.inflaterRef == null ? null : (Inflater)this.inflaterRef.get();
/*      */ 
/*  451 */     if (localInflater == null) {
/*  452 */       this.inflaterRef = new SoftReference(localInflater = new Inflater(true));
/*      */     }
/*  454 */     localInflater.reset();
/*  455 */     localInflater.setInput(paramArrayOfByte1);
/*      */     try {
/*  457 */       return localInflater.inflate(paramArrayOfByte2); } catch (DataFormatException localDataFormatException) {
/*      */     }
/*  459 */     return -1;
/*      */   }
/*      */ 
/*      */   private static int get2ByteLittleEndian(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  468 */     return (paramArrayOfByte[paramInt] & 0xFF) + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8);
/*      */   }
/*      */ 
/*      */   private static int get4ByteLittleEndian(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  475 */     return (paramArrayOfByte[paramInt] & 0xFF) + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8) + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 16) + ((paramArrayOfByte[(paramInt + 3)] & 0xFF) << 24);
/*      */   }
/*      */ 
/*      */   public long getZipFileLastModified()
/*      */     throws IOException
/*      */   {
/*  687 */     synchronized (this) {
/*  688 */       checkIndex();
/*  689 */       return this.zipFileLastModified;
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean readIndex()
/*      */   {
/*  851 */     if ((this.triedToReadIndex) || (!this.usePreindexedCache)) {
/*  852 */       return false;
/*      */     }
/*      */ 
/*  855 */     boolean bool = false;
/*  856 */     synchronized (this) {
/*  857 */       this.triedToReadIndex = true;
/*  858 */       RandomAccessFile localRandomAccessFile = null;
/*      */       try {
/*  860 */         File localFile = getIndexFile();
/*  861 */         localRandomAccessFile = new RandomAccessFile(localFile, "r");
/*      */ 
/*  863 */         long l = localRandomAccessFile.readLong();
/*  864 */         if (this.zipFile.lastModified() != l) {
/*  865 */           bool = false;
/*      */         } else {
/*  867 */           this.directories = new LinkedHashMap();
/*  868 */           int i = localRandomAccessFile.readInt();
/*  869 */           for (int j = 0; j < i; j++) {
/*  870 */             int k = localRandomAccessFile.readInt();
/*  871 */             byte[] arrayOfByte = new byte[k];
/*  872 */             localRandomAccessFile.read(arrayOfByte);
/*      */ 
/*  874 */             RelativePath.RelativeDirectory localRelativeDirectory = getRelativeDirectory(new String(arrayOfByte, "UTF-8"));
/*  875 */             DirectoryEntry localDirectoryEntry = new DirectoryEntry(localRelativeDirectory, this);
/*  876 */             localDirectoryEntry.numEntries = localRandomAccessFile.readInt();
/*  877 */             localDirectoryEntry.writtenOffsetOffset = localRandomAccessFile.readLong();
/*  878 */             this.directories.put(localRelativeDirectory, localDirectoryEntry);
/*      */           }
/*  880 */           bool = true;
/*  881 */           this.zipFileLastModified = l;
/*      */         }
/*      */       } catch (Throwable localThrowable2) {
/*      */       }
/*      */       finally {
/*  886 */         if (localRandomAccessFile != null)
/*      */           try {
/*  888 */             localRandomAccessFile.close();
/*      */           }
/*      */           catch (Throwable localThrowable4)
/*      */           {
/*      */           }
/*      */       }
/*  894 */       if (bool == true) {
/*  895 */         this.readFromIndex = true;
/*      */       }
/*      */     }
/*      */ 
/*  899 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean writeIndex() {
/*  903 */     boolean bool = false;
/*  904 */     if ((this.readFromIndex) || (!this.usePreindexedCache)) {
/*  905 */       return true;
/*      */     }
/*      */ 
/*  908 */     if (!this.writeIndex) {
/*  909 */       return true;
/*      */     }
/*      */ 
/*  912 */     File localFile = getIndexFile();
/*  913 */     if (localFile == null) {
/*  914 */       return false;
/*      */     }
/*      */ 
/*  917 */     RandomAccessFile localRandomAccessFile = null;
/*  918 */     long l1 = 0L;
/*      */     try {
/*  920 */       localRandomAccessFile = new RandomAccessFile(localFile, "rw");
/*      */ 
/*  922 */       localRandomAccessFile.writeLong(this.zipFileLastModified);
/*  923 */       l1 += 8L;
/*      */ 
/*  925 */       ArrayList localArrayList = new ArrayList();
/*  926 */       localHashMap = new HashMap();
/*  927 */       localRandomAccessFile.writeInt(this.directories.keySet().size());
/*  928 */       l1 += 4L;
/*      */ 
/*  930 */       for (localIterator1 = this.directories.keySet().iterator(); localIterator1.hasNext(); ) { localObject1 = (RelativePath.RelativeDirectory)localIterator1.next();
/*  931 */         DirectoryEntry localDirectoryEntry = (DirectoryEntry)this.directories.get(localObject1);
/*      */ 
/*  933 */         localArrayList.add(localDirectoryEntry);
/*      */ 
/*  936 */         byte[] arrayOfByte1 = ((RelativePath.RelativeDirectory)localObject1).getPath().getBytes("UTF-8");
/*  937 */         int i = arrayOfByte1.length;
/*  938 */         localRandomAccessFile.writeInt(i);
/*  939 */         l1 += 4L;
/*      */ 
/*  941 */         localRandomAccessFile.write(arrayOfByte1);
/*  942 */         l1 += i;
/*      */ 
/*  945 */         java.util.List localList1 = localDirectoryEntry.getEntriesAsCollection();
/*  946 */         localRandomAccessFile.writeInt(localList1.size());
/*  947 */         l1 += 4L;
/*      */ 
/*  949 */         localHashMap.put(localObject1, new Long(l1));
/*      */ 
/*  952 */         localDirectoryEntry.writtenOffsetOffset = 0L;
/*  953 */         localRandomAccessFile.writeLong(0L);
/*  954 */         l1 += 8L;
/*      */       }
/*      */ 
/*  957 */       for (localIterator1 = localArrayList.iterator(); localIterator1.hasNext(); ) { localObject1 = (DirectoryEntry)localIterator1.next();
/*      */ 
/*  959 */         long l2 = localRandomAccessFile.getFilePointer();
/*      */ 
/*  961 */         long l3 = ((Long)localHashMap.get(((DirectoryEntry)localObject1).dirName)).longValue();
/*  962 */         localRandomAccessFile.seek(l3);
/*  963 */         localRandomAccessFile.writeLong(l1);
/*      */ 
/*  965 */         localRandomAccessFile.seek(l2);
/*      */ 
/*  968 */         java.util.List localList2 = ((DirectoryEntry)localObject1).getEntriesAsCollection();
/*  969 */         for (Entry localEntry : localList2)
/*      */         {
/*  971 */           byte[] arrayOfByte2 = localEntry.name.getBytes("UTF-8");
/*  972 */           int j = arrayOfByte2.length;
/*  973 */           localRandomAccessFile.writeInt(j);
/*  974 */           l1 += 4L;
/*  975 */           localRandomAccessFile.write(arrayOfByte2);
/*  976 */           l1 += j;
/*      */ 
/*  979 */           localRandomAccessFile.writeByte(localEntry.isDir ? 1 : 0);
/*  980 */           l1 += 1L;
/*      */ 
/*  983 */           localRandomAccessFile.writeInt(localEntry.offset);
/*  984 */           l1 += 4L;
/*      */ 
/*  987 */           localRandomAccessFile.writeInt(localEntry.size);
/*  988 */           l1 += 4L;
/*      */ 
/*  991 */           localRandomAccessFile.writeInt(localEntry.compressedSize);
/*  992 */           l1 += 4L;
/*      */ 
/*  995 */           localRandomAccessFile.writeLong(localEntry.getLastModified());
/*  996 */           l1 += 8L;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable)
/*      */     {
/*      */     }
/*      */     finally
/*      */     {
/*      */       try
/*      */       {
/*      */         HashMap localHashMap;
/*      */         Iterator localIterator1;
/*      */         Object localObject1;
/* 1003 */         if (localRandomAccessFile != null) {
/* 1004 */           localRandomAccessFile.close();
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException3)
/*      */       {
/*      */       }
/*      */     }
/* 1011 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean writeZipIndex() {
/* 1015 */     synchronized (this) {
/* 1016 */       return writeIndex();
/*      */     }
/*      */   }
/*      */ 
/*      */   private File getIndexFile() {
/* 1021 */     if (this.zipIndexFile == null) {
/* 1022 */       if (this.zipFile == null) {
/* 1023 */         return null;
/*      */       }
/*      */ 
/* 1026 */       this.zipIndexFile = new File((this.preindexedCacheLocation == null ? "" : this.preindexedCacheLocation) + this.zipFile
/* 1027 */         .getName() + ".index");
/*      */     }
/*      */ 
/* 1030 */     return this.zipIndexFile;
/*      */   }
/*      */ 
/*      */   public File getZipFile() {
/* 1034 */     return this.zipFile;
/*      */   }
/*      */ 
/*      */   File getAbsoluteFile() {
/* 1038 */     File localFile = this.absFileRef == null ? null : (File)this.absFileRef.get();
/* 1039 */     if (localFile == null) {
/* 1040 */       localFile = this.zipFile.getAbsoluteFile();
/* 1041 */       this.absFileRef = new SoftReference(localFile);
/*      */     }
/* 1043 */     return localFile;
/*      */   }
/*      */ 
/*      */   private RelativePath.RelativeDirectory getRelativeDirectory(String paramString)
/*      */   {
/* 1048 */     SoftReference localSoftReference = (SoftReference)this.relativeDirectoryCache.get(paramString);
/* 1049 */     if (localSoftReference != null) {
/* 1050 */       localRelativeDirectory = (RelativePath.RelativeDirectory)localSoftReference.get();
/* 1051 */       if (localRelativeDirectory != null)
/* 1052 */         return localRelativeDirectory;
/*      */     }
/* 1054 */     RelativePath.RelativeDirectory localRelativeDirectory = new RelativePath.RelativeDirectory(paramString);
/* 1055 */     this.relativeDirectoryCache.put(paramString, new SoftReference(localRelativeDirectory));
/* 1056 */     return localRelativeDirectory;
/*      */   }
/*      */ 
/*      */   static class DirectoryEntry
/*      */   {
/*      */     private boolean filesInited;
/*      */     private boolean directoriesInited;
/*      */     private boolean zipFileEntriesInited;
/*      */     private boolean entriesInited;
/*  703 */     private long writtenOffsetOffset = 0L;
/*      */     private RelativePath.RelativeDirectory dirName;
/*  707 */     private com.sun.tools.javac.util.List<String> zipFileEntriesFiles = com.sun.tools.javac.util.List.nil();
/*  708 */     private com.sun.tools.javac.util.List<String> zipFileEntriesDirectories = com.sun.tools.javac.util.List.nil();
/*  709 */     private com.sun.tools.javac.util.List<ZipFileIndex.Entry> zipFileEntries = com.sun.tools.javac.util.List.nil();
/*      */ 
/*  711 */     private java.util.List<ZipFileIndex.Entry> entries = new ArrayList();
/*      */     private ZipFileIndex zipFileIndex;
/*      */     private int numEntries;
/*      */ 
/*      */     DirectoryEntry(RelativePath.RelativeDirectory paramRelativeDirectory, ZipFileIndex paramZipFileIndex)
/*      */     {
/*  718 */       this.filesInited = false;
/*  719 */       this.directoriesInited = false;
/*  720 */       this.entriesInited = false;
/*      */ 
/*  722 */       this.dirName = paramRelativeDirectory;
/*  723 */       this.zipFileIndex = paramZipFileIndex;
/*      */     }
/*      */ 
/*      */     private com.sun.tools.javac.util.List<String> getFiles() {
/*  727 */       if (!this.filesInited) {
/*  728 */         initEntries();
/*  729 */         for (ZipFileIndex.Entry localEntry : this.entries) {
/*  730 */           if (!localEntry.isDir) {
/*  731 */             this.zipFileEntriesFiles = this.zipFileEntriesFiles.append(localEntry.name);
/*      */           }
/*      */         }
/*  734 */         this.filesInited = true;
/*      */       }
/*  736 */       return this.zipFileEntriesFiles;
/*      */     }
/*      */ 
/*      */     private com.sun.tools.javac.util.List<String> getDirectories() {
/*  740 */       if (!this.directoriesInited) {
/*  741 */         initEntries();
/*  742 */         for (ZipFileIndex.Entry localEntry : this.entries) {
/*  743 */           if (localEntry.isDir) {
/*  744 */             this.zipFileEntriesDirectories = this.zipFileEntriesDirectories.append(localEntry.name);
/*      */           }
/*      */         }
/*  747 */         this.directoriesInited = true;
/*      */       }
/*  749 */       return this.zipFileEntriesDirectories;
/*      */     }
/*      */ 
/*      */     private com.sun.tools.javac.util.List<ZipFileIndex.Entry> getEntries() {
/*  753 */       if (!this.zipFileEntriesInited) {
/*  754 */         initEntries();
/*  755 */         this.zipFileEntries = com.sun.tools.javac.util.List.nil();
/*  756 */         for (ZipFileIndex.Entry localEntry : this.entries) {
/*  757 */           this.zipFileEntries = this.zipFileEntries.append(localEntry);
/*      */         }
/*  759 */         this.zipFileEntriesInited = true;
/*      */       }
/*  761 */       return this.zipFileEntries;
/*      */     }
/*      */ 
/*      */     private ZipFileIndex.Entry getEntry(String paramString) {
/*  765 */       initEntries();
/*  766 */       int i = Collections.binarySearch(this.entries, new ZipFileIndex.Entry(this.dirName, paramString));
/*  767 */       if (i < 0) {
/*  768 */         return null;
/*      */       }
/*      */ 
/*  771 */       return (ZipFileIndex.Entry)this.entries.get(i);
/*      */     }
/*      */ 
/*      */     private void initEntries() {
/*  775 */       if (this.entriesInited)
/*      */         return;
/*      */       int k;
/*  779 */       if (!this.zipFileIndex.readFromIndex) {
/*  780 */         int i = -Arrays.binarySearch(this.zipFileIndex.entries, new ZipFileIndex.Entry(this.dirName, 
/*  781 */           ZipFileIndex.MIN_CHAR)) - 1;
/*      */ 
/*  782 */         int j = -Arrays.binarySearch(this.zipFileIndex.entries, new ZipFileIndex.Entry(this.dirName, 
/*  783 */           ZipFileIndex.MAX_CHAR)) - 1;
/*      */ 
/*  785 */         for (k = i; k < j; k++)
/*  786 */           this.entries.add(this.zipFileIndex.entries[k]);
/*      */       }
/*      */       else {
/*  789 */         File localFile = this.zipFileIndex.getIndexFile();
/*  790 */         if (localFile != null) {
/*  791 */           RandomAccessFile localRandomAccessFile = null;
/*      */           try {
/*  793 */             localRandomAccessFile = new RandomAccessFile(localFile, "r");
/*  794 */             localRandomAccessFile.seek(this.writtenOffsetOffset);
/*      */ 
/*  796 */             for (k = 0; k < this.numEntries; k++)
/*      */             {
/*  798 */               int m = localRandomAccessFile.readInt();
/*  799 */               byte[] arrayOfByte = new byte[m];
/*  800 */               localRandomAccessFile.read(arrayOfByte);
/*  801 */               String str = new String(arrayOfByte, "UTF-8");
/*      */ 
/*  804 */               boolean bool = localRandomAccessFile.readByte() != 0;
/*      */ 
/*  807 */               int n = localRandomAccessFile.readInt();
/*      */ 
/*  810 */               int i1 = localRandomAccessFile.readInt();
/*      */ 
/*  813 */               int i2 = localRandomAccessFile.readInt();
/*      */ 
/*  816 */               long l = localRandomAccessFile.readLong();
/*      */ 
/*  818 */               ZipFileIndex.Entry localEntry = new ZipFileIndex.Entry(this.dirName, str);
/*  819 */               localEntry.isDir = bool;
/*  820 */               localEntry.offset = n;
/*  821 */               localEntry.size = i1;
/*  822 */               localEntry.compressedSize = i2;
/*  823 */               localEntry.javatime = l;
/*  824 */               this.entries.add(localEntry);
/*      */             }
/*      */           } catch (Throwable localThrowable2) {
/*      */           }
/*      */           finally {
/*      */             try {
/*  830 */               if (localRandomAccessFile != null) {
/*  831 */                 localRandomAccessFile.close();
/*      */               }
/*      */             }
/*      */             catch (Throwable localThrowable4)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  840 */       this.entriesInited = true;
/*      */     }
/*      */ 
/*      */     java.util.List<ZipFileIndex.Entry> getEntriesAsCollection() {
/*  844 */       initEntries();
/*      */ 
/*  846 */       return this.entries;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Entry
/*      */     implements Comparable<Entry>
/*      */   {
/* 1060 */     public static final Entry[] EMPTY_ARRAY = new Entry[0];
/*      */     RelativePath.RelativeDirectory dir;
/*      */     boolean isDir;
/*      */     String name;
/*      */     int offset;
/*      */     int size;
/*      */     int compressedSize;
/*      */     long javatime;
/*      */     private int nativetime;
/*      */ 
/*      */     public Entry(RelativePath paramRelativePath)
/*      */     {
/* 1077 */       this(paramRelativePath.dirname(), paramRelativePath.basename());
/*      */     }
/*      */ 
/*      */     public Entry(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/* 1081 */       this.dir = paramRelativeDirectory;
/* 1082 */       this.name = paramString;
/*      */     }
/*      */ 
/*      */     public String getName() {
/* 1086 */       return new RelativePath.RelativeFile(this.dir, this.name).getPath();
/*      */     }
/*      */ 
/*      */     public String getFileName() {
/* 1090 */       return this.name;
/*      */     }
/*      */ 
/*      */     public long getLastModified() {
/* 1094 */       if (this.javatime == 0L) {
/* 1095 */         this.javatime = dosToJavaTime(this.nativetime);
/*      */       }
/* 1097 */       return this.javatime;
/*      */     }
/*      */ 
/*      */     private static long dosToJavaTime(int paramInt)
/*      */     {
/* 1103 */       Calendar localCalendar = Calendar.getInstance();
/* 1104 */       localCalendar.set(1, (paramInt >> 25 & 0x7F) + 1980);
/* 1105 */       localCalendar.set(2, (paramInt >> 21 & 0xF) - 1);
/* 1106 */       localCalendar.set(5, paramInt >> 16 & 0x1F);
/* 1107 */       localCalendar.set(11, paramInt >> 11 & 0x1F);
/* 1108 */       localCalendar.set(12, paramInt >> 5 & 0x3F);
/* 1109 */       localCalendar.set(13, paramInt << 1 & 0x3E);
/* 1110 */       localCalendar.set(14, 0);
/* 1111 */       return localCalendar.getTimeInMillis();
/*      */     }
/*      */ 
/*      */     void setNativeTime(int paramInt) {
/* 1115 */       this.nativetime = paramInt;
/*      */     }
/*      */ 
/*      */     public boolean isDirectory() {
/* 1119 */       return this.isDir;
/*      */     }
/*      */ 
/*      */     public int compareTo(Entry paramEntry) {
/* 1123 */       RelativePath.RelativeDirectory localRelativeDirectory = paramEntry.dir;
/* 1124 */       if (this.dir != localRelativeDirectory) {
/* 1125 */         int i = this.dir.compareTo(localRelativeDirectory);
/* 1126 */         if (i != 0)
/* 1127 */           return i;
/*      */       }
/* 1129 */       return this.name.compareTo(paramEntry.name);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1134 */       if (!(paramObject instanceof Entry))
/* 1135 */         return false;
/* 1136 */       Entry localEntry = (Entry)paramObject;
/* 1137 */       return (this.dir.equals(localEntry.dir)) && (this.name.equals(localEntry.name));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1142 */       int i = 7;
/* 1143 */       i = 97 * i + (this.dir != null ? this.dir.hashCode() : 0);
/* 1144 */       i = 97 * i + (this.name != null ? this.name.hashCode() : 0);
/* 1145 */       return i;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1150 */       return this.dir + ":" + this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ZipDirectory
/*      */   {
/*      */     private RelativePath.RelativeDirectory lastDir;
/*      */     private int lastStart;
/*      */     private int lastLen;
/*      */     byte[] zipDir;
/*  489 */     RandomAccessFile zipRandomFile = null;
/*  490 */     ZipFileIndex zipFileIndex = null;
/*      */ 
/*      */     public ZipDirectory(RandomAccessFile paramLong1, long arg3, long arg5, ZipFileIndex arg7) throws IOException {
/*  493 */       this.zipRandomFile = paramLong1;
/*      */       Object localObject2;
/*  494 */       this.zipFileIndex = localObject2;
/*  495 */       hasValidHeader();
/*      */       Object localObject1;
/*  496 */       findCENRecord(???, localObject1);
/*      */     }
/*      */ 
/*      */     private boolean hasValidHeader()
/*      */       throws IOException
/*      */     {
/*  504 */       long l = this.zipRandomFile.getFilePointer();
/*      */       try {
/*  506 */         if ((this.zipRandomFile.read() == 80) && 
/*  507 */           (this.zipRandomFile.read() == 75) && 
/*  508 */           (this.zipRandomFile.read() == 3) && 
/*  509 */           (this.zipRandomFile.read() == 4)) {
/*  510 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  516 */         this.zipRandomFile.seek(l);
/*      */       }
/*  518 */       throw new ZipFileIndex.ZipFormatException("invalid zip magic");
/*      */     }
/*      */ 
/*      */     private void findCENRecord(long paramLong1, long paramLong2)
/*      */       throws IOException
/*      */     {
/*  527 */       long l1 = paramLong2 - paramLong1;
/*  528 */       int i = 1024;
/*  529 */       byte[] arrayOfByte = new byte[i];
/*  530 */       long l2 = paramLong2 - paramLong1;
/*      */ 
/*  533 */       while (l2 >= 22L) {
/*  534 */         if (l2 < i)
/*  535 */           i = (int)l2;
/*  536 */         long l3 = l2 - i;
/*  537 */         this.zipRandomFile.seek(paramLong1 + l3);
/*  538 */         this.zipRandomFile.readFully(arrayOfByte, 0, i);
/*  539 */         int j = i - 22;
/*  540 */         while ((j >= 0) && ((arrayOfByte[j] != 80) || (arrayOfByte[(j + 1)] != 75) || (arrayOfByte[(j + 2)] != 5) || (arrayOfByte[(j + 3)] != 6) || 
/*  546 */           (l3 + j + 22L + 
/*  546 */           ZipFileIndex.get2ByteLittleEndian(arrayOfByte, j + 20) != 
/*  546 */           l1))) {
/*  547 */           j--;
/*      */         }
/*      */ 
/*  550 */         if (j >= 0) {
/*  551 */           this.zipDir = new byte[ZipFileIndex.get4ByteLittleEndian(arrayOfByte, j + 12)];
/*  552 */           int k = ZipFileIndex.get4ByteLittleEndian(arrayOfByte, j + 16);
/*      */ 
/*  555 */           if ((k < 0) || (ZipFileIndex.get2ByteLittleEndian(arrayOfByte, j + 10) == 65535)) {
/*  556 */             throw new ZipFileIndex.ZipFormatException("detected a zip64 archive");
/*      */           }
/*  558 */           this.zipRandomFile.seek(paramLong1 + k);
/*  559 */           this.zipRandomFile.readFully(this.zipDir, 0, this.zipDir.length);
/*  560 */           return;
/*      */         }
/*  562 */         l2 = l3 + 21L;
/*      */       }
/*      */ 
/*  565 */       throw new ZipException("cannot read zip file");
/*      */     }
/*      */ 
/*      */     private void buildIndex() throws IOException {
/*  569 */       int i = this.zipDir.length;
/*      */ 
/*  572 */       if (i > 0) {
/*  573 */         ZipFileIndex.this.directories = new LinkedHashMap();
/*  574 */         ArrayList localArrayList = new ArrayList();
/*  575 */         for (int j = 0; j < i; ) {
/*  576 */           j = readEntry(j, localArrayList, ZipFileIndex.this.directories);
/*      */         }
/*      */ 
/*  580 */         for (RelativePath.RelativeDirectory localRelativeDirectory1 : ZipFileIndex.this.directories.keySet())
/*      */         {
/*  582 */           RelativePath.RelativeDirectory localRelativeDirectory2 = ZipFileIndex.this.getRelativeDirectory(localRelativeDirectory1.dirname().getPath());
/*  583 */           String str = localRelativeDirectory1.basename();
/*  584 */           ZipFileIndex.Entry localEntry = new ZipFileIndex.Entry(localRelativeDirectory2, str);
/*  585 */           localEntry.isDir = true;
/*  586 */           localArrayList.add(localEntry);
/*      */         }
/*      */ 
/*  589 */         ZipFileIndex.this.entries = ((ZipFileIndex.Entry[])localArrayList.toArray(new ZipFileIndex.Entry[localArrayList.size()]));
/*  590 */         Arrays.sort(ZipFileIndex.this.entries);
/*      */       } else {
/*  592 */         ZipFileIndex.this.cleanupState();
/*      */       }
/*      */     }
/*      */ 
/*      */     private int readEntry(int paramInt, java.util.List<ZipFileIndex.Entry> paramList, Map<RelativePath.RelativeDirectory, ZipFileIndex.DirectoryEntry> paramMap) throws IOException
/*      */     {
/*  598 */       if (ZipFileIndex.get4ByteLittleEndian(this.zipDir, paramInt) != 33639248) {
/*  599 */         throw new ZipException("cannot read zip file entry");
/*      */       }
/*      */ 
/*  602 */       int i = paramInt + 46;
/*  603 */       int j = i;
/*  604 */       int k = j + ZipFileIndex.get2ByteLittleEndian(this.zipDir, paramInt + 28);
/*      */ 
/*  606 */       if ((this.zipFileIndex.symbolFilePrefixLength != 0) && 
/*  607 */         (k - j >= ZipFileIndex.this.symbolFilePrefixLength))
/*      */       {
/*  608 */         i += this.zipFileIndex.symbolFilePrefixLength;
/*  609 */         j += this.zipFileIndex.symbolFilePrefixLength;
/*      */       }
/*      */       int n;
/*  612 */       for (int m = j; m < k; m++) {
/*  613 */         n = this.zipDir[m];
/*  614 */         if (n == 92) {
/*  615 */           this.zipDir[m] = 47;
/*  616 */           j = m + 1;
/*  617 */         } else if (n == 47) {
/*  618 */           j = m + 1;
/*      */         }
/*      */       }
/*      */ 
/*  622 */       RelativePath.RelativeDirectory localRelativeDirectory = null;
/*  623 */       if (j == i) {
/*  624 */         localRelativeDirectory = ZipFileIndex.this.getRelativeDirectory("");
/*  625 */       } else if ((this.lastDir != null) && (this.lastLen == j - i - 1)) {
/*  626 */         n = this.lastLen - 1;
/*  627 */         while (this.zipDir[(this.lastStart + n)] == this.zipDir[(i + n)]) {
/*  628 */           if (n == 0) {
/*  629 */             localRelativeDirectory = this.lastDir;
/*  630 */             break;
/*      */           }
/*  632 */           n--;
/*      */         }
/*      */       }
/*      */       Object localObject;
/*  637 */       if (localRelativeDirectory == null) {
/*  638 */         this.lastStart = i;
/*  639 */         this.lastLen = (j - i - 1);
/*      */ 
/*  641 */         localRelativeDirectory = ZipFileIndex.this.getRelativeDirectory(new String(this.zipDir, i, this.lastLen, "UTF-8"));
/*  642 */         this.lastDir = localRelativeDirectory;
/*      */ 
/*  645 */         localObject = localRelativeDirectory;
/*      */ 
/*  647 */         while (paramMap.get(localObject) == null) {
/*  648 */           paramMap.put(localObject, new ZipFileIndex.DirectoryEntry((RelativePath.RelativeDirectory)localObject, this.zipFileIndex));
/*  649 */           if (((RelativePath.RelativeDirectory)localObject).path.indexOf("/") == ((RelativePath.RelativeDirectory)localObject).path.length() - 1)
/*      */           {
/*      */             break;
/*      */           }
/*  653 */           localObject = ZipFileIndex.this.getRelativeDirectory(((RelativePath.RelativeDirectory)localObject).dirname().getPath());
/*      */         }
/*      */ 
/*      */       }
/*  658 */       else if (paramMap.get(localRelativeDirectory) == null) {
/*  659 */         paramMap.put(localRelativeDirectory, new ZipFileIndex.DirectoryEntry(localRelativeDirectory, this.zipFileIndex));
/*      */       }
/*      */ 
/*  664 */       if (j != k) {
/*  665 */         localObject = new ZipFileIndex.Entry(localRelativeDirectory, new String(this.zipDir, j, k - j, "UTF-8"));
/*      */ 
/*  668 */         ((ZipFileIndex.Entry)localObject).setNativeTime(ZipFileIndex.get4ByteLittleEndian(this.zipDir, paramInt + 12));
/*  669 */         ((ZipFileIndex.Entry)localObject).compressedSize = ZipFileIndex.get4ByteLittleEndian(this.zipDir, paramInt + 20);
/*  670 */         ((ZipFileIndex.Entry)localObject).size = ZipFileIndex.get4ByteLittleEndian(this.zipDir, paramInt + 24);
/*  671 */         ((ZipFileIndex.Entry)localObject).offset = ZipFileIndex.get4ByteLittleEndian(this.zipDir, paramInt + 42);
/*  672 */         paramList.add(localObject);
/*      */       }
/*      */ 
/*  678 */       return paramInt + 46 + 
/*  676 */         ZipFileIndex.get2ByteLittleEndian(this.zipDir, paramInt + 28) + 
/*  677 */         ZipFileIndex.get2ByteLittleEndian(this.zipDir, paramInt + 30) + 
/*  678 */         ZipFileIndex.get2ByteLittleEndian(this.zipDir, paramInt + 32);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class ZipFormatException extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = 8000196834066748623L;
/*      */ 
/*      */     protected ZipFormatException(String paramString)
/*      */     {
/* 1162 */       super();
/*      */     }
/*      */ 
/*      */     protected ZipFormatException(String paramString, Throwable paramThrowable) {
/* 1166 */       super(paramThrowable);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.ZipFileIndex
 * JD-Core Version:    0.6.2
 */