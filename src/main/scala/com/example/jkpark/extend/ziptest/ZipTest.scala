package com.example.jkpark.extend.ziptest

import java.io.File

import ArchiveFile._

object ZipTest {


  def main(args: Array[String]): Unit = {

    val messagePath = "/home/shots"
    val paths: Array[String] = new File(messagePath).listFiles().map(file => file.getPath )

    zipFiles(paths, "/root/shots.zip")
    extractFiles("/root/shots.zip", Option("/root"))

  }
}
