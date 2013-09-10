package com.github.bballant.docintel

import java.io.{ File, Reader, BufferedReader }

object BbStringTokenizer {
  import Util._

  def getText(file: File): String = {
    val source = scala.io.Source.fromFile(file)
    val out = source.mkString
    source.close()
    out
  }

  def tokenizeStemFilter(reader: Reader): Seq[String] = {
    val bufferedReader = new BufferedReader(reader)
    val stringBuilder = new StringBuilder

    var line = bufferedReader.readLine
    while(line != null) {
      stringBuilder.append(line)
      line = bufferedReader.readLine
    }

    tokenize(stringBuilder.toString).
      filter(notStopWord).
      map(stem)
  }

  def tokenizeStemFilter(file: File): Seq[String] = {
    tokenize(getText(file)).
      filter(notStopWord).
      map(stem)
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
