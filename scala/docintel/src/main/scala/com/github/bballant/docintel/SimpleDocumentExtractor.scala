package com.github.bballant.docintel

import java.io.{ File, FileReader, StringReader }

import breeze.data.Example
import breeze.linalg._

object Util {
  val StopWords = Set(
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with")
}

object StringTokenizer {
  import Util._

  def getText(file: File): String = {
    val source = scala.io.Source.fromFile(file)
    val out = source.mkString
    source.close()
    out
  }

  def tokenize(text: String): Seq[String] = {
    text.
      toLowerCase.
      replaceAll("[^a-zA-Z0-9\\s]", "").
      split("\\s+").
      toSeq
  }

  def notStopWord(token: String) = !StopWords.contains(token)

  def stem(term: String) = PorterStemmer.stem(term)
  
}

import org.apache.lucene.analysis._
import org.apache.lucene.analysis.core.{ LowerCaseFilter, StopFilter }
import org.apache.lucene.analysis.en.PorterStemFilter
import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.util.CharArraySet
import org.apache.lucene.util.Version
import java.io.Reader

import scala.collection.JavaConverters._

object LuceneTokenizer /*extends Analyzer*/ {
  import Util._

  val LuceneVersion = Version.LUCENE_41

  def tokenStream(reader: Reader): TokenStream ={
    val tokenizer: Tokenizer = new StandardTokenizer(LuceneVersion, reader)
    val lowerCaseFilter: LowerCaseFilter = new LowerCaseFilter(LuceneVersion, tokenizer)
    val stopWords = new CharArraySet(LuceneVersion, StopWords.asJava,  true)
    val stopFilter: TokenFilter = new StopFilter(LuceneVersion, lowerCaseFilter, stopWords)
    val stemFilter: TokenFilter = new PorterStemFilter(stopFilter)
    stemFilter
  }

  def tokenize(reader: Reader): Seq[String] = {

    val ts: TokenStream = tokenStream(reader)

    var cattr: CharTermAttribute = ts.addAttribute(classOf[CharTermAttribute])
    ts.reset()
    val tokens: List[String] =
      if (ts.incrementToken) {
        Iterator.
          continually(cattr.toString).
          takeWhile(_ => ts.incrementToken).
          toList
      } else List.empty[String]
    ts.end()
    ts.close()
    
    tokens.
      map(_.replaceAll("[^a-zA-Z0-9\\s]", "")).
      filter(!_.isEmpty)
  }
}

object SimpleDocumentExtractor {

  def main(args: Array[String]) {

    val path: String = args.headOption.getOrElse {  
      throw new Exception("SimpleDocumentExtractor expects a file path")
    }

    // A sequence of a tuple representing (is positive, filename, token counts)
    val data: Seq[(Boolean, String, Map[String, Int])] =
      for (dir <- (new File(path)).listFiles; file <- dir.listFiles) yield {

        val tokens: Seq[String] =
          LuceneTokenizer.tokenize(new FileReader(file))

        // val tokens: Seq[String] = {
        //   import StringTokenizer._
        //   tokenize(getText(file)).
        //   filter(notStopWord).
        //   map(stem)
        // }
          
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
