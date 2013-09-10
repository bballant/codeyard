package com.github.bballant.docintel

import java.io.{ File, FileReader, StringReader }
import breeze.data.Example
import breeze.linalg._
import java.io.Reader
import scala.collection.JavaConverters._

object SimpleDocumentExtractor {

  def main(args: Array[String]) {
    val path: String = args.headOption.getOrElse {
      throw new Exception("SimpleDocumentExtractor expects a file path")
    }

    val data: Seq[(Boolean, MagnitudeVector[TermMagnitude])] =
      for (dir <- (new File(path)).listFiles; file <- dir.listFiles) yield {

        val tokens = BbStringTokenizer.tokenizeStemFilter(file)

        val tokenAttributes: List[TermMagnitude] =
          tokens.groupBy(token => token).
          mapValues(_.size.toDouble).
          map { case(k, v) => TermMagnitude(k, v) }.
          toList

        (dir.getName == "pos", MagnitudeVector(file.getName, tokenAttributes))
      }

    val tokenIndex: Seq[String] =
      data.flatMap { case(_, MagnitudeVector(_, tokenAttributes)) =>
        tokenAttributes.map(_.term)
      }.
      distinct.
      sorted

    def vectors: Seq[Seq[Double]] =
      data.map { case(_, MagnitudeVector(_, tokenAttributes)) =>
        val tokenAttributeMap = tokenAttributes.map(a => (a.term, a)).toMap
        tokenIndex.map { token => tokenAttributeMap.get(token).map(_.value).getOrElse(0.0) }
      }

    printToFile(new File("out.csv")) { p =>
      p.println(tokenIndex.mkString(","))
      vectors.foreach { vector =>
        p.println(vector.mkString(","))
      }
    }

  }

  def oldmain(args: Array[String]) {

    val path: String = args.headOption.getOrElse {
      throw new Exception("SimpleDocumentExtractor expects a file path")
    }

    // A sequence of a tuple representing (is positive, filename, token counts)
    val data: Seq[(Boolean, String, Map[String, Int])] =
      for (dir <- (new File(path)).listFiles; file <- dir.listFiles) yield {

        // val tokens: Seq[String] =
        //   LuceneTokenizer.tokenize(new FileReader(file))

        // val tokens: Seq[String] = {
        //   import StringTokenizer._
        //   tokenize(getText(file)).
        //   filter(notStopWord).
        //   map(stem)
        // }

        val tokens = BbStringTokenizer.tokenizeStemFilter(file)

        val tokenCount: Map[String, Int] =
          tokens.groupBy(token => token).mapValues(_.size)

        (dir.getName == "pos", file.getName, tokenCount)
      }

    val tokenIndex = data.flatMap { case(_, _, tokenCount) => tokenCount.keys }.distinct

    val vectors = data.map { case(_, _, tokenCount) =>
      tokenIndex.map { token => tokenCount.get(token).getOrElse(0) }
    }

    printToFile(new File("out.csv")) { p =>
      p.println(tokenIndex.mkString(","))
      vectors.foreach { vector =>
        p.println(vector.mkString(","))
      }
    }

  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}
