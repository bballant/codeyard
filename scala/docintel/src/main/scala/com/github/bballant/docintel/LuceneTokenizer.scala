package com.github.bballant.docintel

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
