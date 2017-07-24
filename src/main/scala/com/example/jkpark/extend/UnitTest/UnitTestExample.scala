package com.example.jkpark.extend.UnitTest


class ElementSuit extends FunSuite {
  test("elem result should have passed width") {
    val ele = elem('x', 2, 3)
    assert(ele.width ==2 )
  }
}

object UnitTestExample {

  def main(args: Array[String]): Unit = {

  }

}
