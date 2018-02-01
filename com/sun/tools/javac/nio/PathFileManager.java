package com.sun.tools.javac.nio;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;

public abstract interface PathFileManager extends JavaFileManager
{
  public abstract FileSystem getDefaultFileSystem();

  public abstract void setDefaultFileSystem(FileSystem paramFileSystem);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjectsFromPaths(Iterable<? extends Path> paramIterable);

  public abstract Iterable<? extends JavaFileObject> getJavaFileObjects(Path[] paramArrayOfPath);

  public abstract Path getPath(FileObject paramFileObject);

  public abstract Iterable<? extends Path> getLocation(JavaFileManager.Location paramLocation);

  public abstract void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends Path> paramIterable)
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.nio.PathFileManager
 * JD-Core Version:    0.6.2
 */