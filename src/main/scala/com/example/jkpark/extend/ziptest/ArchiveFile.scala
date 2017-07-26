package com.example.jkpark.extend.ziptest

import java.io._
import java.nio.file.{FileSystems, Files, Paths}
import java.util.zip.{ZipEntry, ZipOutputStream}
import org.apache.commons.compress.archivers.{ArchiveEntry, ArchiveStreamFactory}
import org.apache.commons.compress.compressors.CompressorStreamFactory
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object Resource {
  def using[A <:{ def close()}, B](resource: A)(f: A =>B): Unit ={
    try{
      f(resource)
    }
    finally{
      resource.close()
    }
  }
}

object ArchiveFile {

  def extractFiles(filePath: String, extractDirPath: Option[String]): Unit = {

    def createUncompressorInputStream(bufferedInputStream: BufferedInputStream): InputStream = {
      Try(new CompressorStreamFactory().createCompressorInputStream(bufferedInputStream)) match {
        case Success(i) => new BufferedInputStream(i)
        case Failure(_) => bufferedInputStream
      }
    }

    val archiveInputStream = {
      val bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath))
      new ArchiveStreamFactory().createArchiveInputStream(createUncompressorInputStream(bufferedInputStream))
    }

    Resource.using(archiveInputStream) { inputStream =>

      def archiveStream: Stream[ArchiveEntry] = {
        inputStream.getNextEntry match {
          case null => Stream.empty
          case entry => entry #:: archiveStream
        }
      }

      archiveStream.withFilter(_.getSize != 0).foreach { entry: ArchiveEntry =>
        val extractFileName = extractDirPath match {
          case Some(targetDirPath) => targetDirPath + File.separator + entry.getName
          case None => Paths.get(filePath).getParent + File.separator + entry.getName
        }

        if (Files.exists(Paths.get(extractFileName)) == false) {
          Files.createDirectories(Paths.get(extractFileName).getParent)
        }

        Resource.using(new BufferedOutputStream(new FileOutputStream(extractFileName))) { outputStream =>
          writeData(inputStream, outputStream)
        }
      }
    }
  }

  def zipFiles(targetFiles: Seq[String], zipFilePath: String): Unit = {

    Resource.using(new ZipOutputStream(new FileOutputStream(zipFilePath))) { zipStream =>
      targetFiles.foreach { filePath =>
        val absPath: String =
          if (Paths.get(filePath).isAbsolute)
            filePath
          else
            FileSystems.getDefault.getPath(filePath).normalize().toAbsolutePath.toString

        zipStream.putNextEntry(new ZipEntry(absPath))

        //압축 파일 데이터 추가
        Resource.using(new BufferedInputStream(new FileInputStream(absPath))) { bufferdInput =>
          writeData(bufferdInput, zipStream)
        }

        zipStream.closeEntry()
      }
    }
  }

  @tailrec
  private def writeData(input: InputStream, output: OutputStream): Unit = {
    val buf = new Array[Byte](8192)
    val numOfRead = input.read(buf)
    if (numOfRead > 0) {
      output.write(buf, 0, numOfRead)
      writeData(input, output)
    }
  }
}
