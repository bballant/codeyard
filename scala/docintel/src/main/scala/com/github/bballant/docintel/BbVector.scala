package com.github.bballant.docintel
import scala.collection.immutable.SortedSet

trait MagnitudeOps[A] {
  def multiply(a: A, b: A): Double
  def add(a: A, b: A): Double
  def normalize(a: A, normFactor: Double): A
}

case class TermMagnitude(term: String, value: Double)

object TermMagnitudeOps extends MagnitudeOps[TermMagnitude] {
  def multiply(a: TermMagnitude, b: TermMagnitude): Double = (a.value * b.value)
  def add(a: TermMagnitude, b: TermMagnitude): Double = (a.value + b.value)
  def normalize(a: TermMagnitude, normFactor: Double): TermMagnitude = a.copy(value = a.value/normFactor)
}

case class MagnitudeVector[T](name: String, attributes: List[T]) {

  def dotProduct(vector: MagnitudeVector[T])(implicit ops: MagnitudeOps[T]): Double = {
    attributes.zip(vector.attributes).map { case(a, b) => ops.multiply(a, b) }.sum
  }

  def normalized(implicit ops: MagnitudeOps[T]): MagnitudeVector[T] = {
    val normFactor = {
      val attrSq = this.attributes.map(attr => ops.multiply(attr, attr)).sum
      math.sqrt(attrSq)
    }
    val attributesNormalized = this.attributes.map(attr => ops.normalize(attr, normFactor))
    this.copy(attributes = attributesNormalized)
  }
}