package com.example.jkpark.extend


abstract class Element {
  def contents: Array[String]
  /**
    * 다음과 같이 method로 선언 할 경우 매번 수행된다.
    * def height: Int = contents.length
    * def length: Int = if(height == 0) 0 else contents(0).length
    */

  // 아래 처럼 필드로 구현하게 되면 메모리를 차지한다는 단점이 존재한다.
  val height: Int = contents.length
  val width: Int = if(height == 0) 0 else contents(0).length


  def hidden(): Boolean = 1 < 2
}

// 비공개가 아닌 맴버를 모두 물려받는다.
// ArrayElement를 Element의 서브타입으로 만든다.
class ArrayElement(override val contents: Array[String]) extends Element {
  // 기존의  파라미터가 없는 메소드를 필드로 오버라이드 할 수 있다.
  //override def contents: Array[String] = conts

  // final을 붙여주게 되면 더이상의 override를 막는다.
  // 클래스 전체의 상속을 막기 위해서는 class 앞에 final을 붙인다.
  // final class ArrayElement(override val contents: Array[String]) extends Element {
  final override def hidden(): Boolean = 2 > 1
  def above(that: Element): Element = new ArrayElement(this.contents ++ that.contents)
  def beside(that: Element): Element = {
    new ArrayElement(
      for(
        // zip 연산의 대상인 두 배열중 하나가 길면 짧은 쪽 기준으로만 맞춘다.
        (line1, line2) <- this.contents zip that.contents
      ) yield line1 + line2
    )
  }
}

/**
  *
  * 아래와 같이 ArrayElement를 상속받은 이유는 contents의 정의를 재사용하기 위해서인다.
class LineElement(s: String) extends ArrayElement(Array(s)) {
  override val height: Int = 1
  override val width: Int = s.length

  override def hidden(): Boolean = 5 < 1
}
*/

class LineElement(s: String) extends Element{
  override def contents: Array[String] = Array(s)
  override val height: Int = 1
  override val width: Int = s.length
}


/**
  * Created by gon on 2017-07-11.
  */
object extend extends App{

  override def main(args: Array[String]): Unit = {

    val ae = new ArrayElement(Array("hello", "world"))
    println(ae.width)

    // 서브타입 관계는 슈퍼 클래스의 값을 필요로 하는 곳이라면 어디에서나 서브클래스의 값을 쓸 수 있다.
    val e: Element = ae

  }

}
