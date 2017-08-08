package com.example.jkpark.extend.mutableobject


class BankAccount {


  // 비공개 메소드
  private var bal: Int = 0

  // 공개 맴버
  def balance: Int = bal

  // 공개 메소드
  def deposit(amount: Int) = {
    require(amount > 0)
    bal += amount
  }

  def withdraw(amount: Int): Boolean = {
    if(amount > bal) false
    else {
      bal -= amount
      true
    }
  }
}

/**
  * var를 포함하더라도 순수 함수일 수 있는 예
  */
class Keyed {
  // 어떤 시간이 오래 걸리는 작업
  def computeKey: Int = ???
}

// Keyed 클래스를 사용하는 것보다 더 효율적이다.
class MemoKeyed extends Keyed {

  // var 변수를 이용해 캐싱된 키 값을 반환 할 수 있다.
  // 비록 맴버 변수로 var를 가지고 있어도 이 클래스는 순수 함수형이다.
  private var keyCache: Option[Int] = None

  override def computeKey: Int = {
    if(!keyCache.isDefined)
      keyCache = Some(super.computeKey)
    keyCache.get
  }
}

/**
  * 스칼라에서는 별도의 getter와 setter를 정의할 필요가 없다.
  * 스칼라에서는 자동으로 정의해준다.
  * var x의 getter는 x 이고, setter는 x_= 이다.
  * var x라는 필드가 존재하게 될 경우 각 필드에는 private[this]가 붙는다.
  */
class Time {
  var hour = 12
  var minute = 0
}

/**
  * 위의 클래스는 아래 처럼 확장된다.
  */

class TimeX {
  private[this] var h = 12
  private[this] var m = 0

  def hour: Int = h
  def hour_=(x: Int) = { h = x }

  def minute: Int = m
  def minute_=(x: Int) = { h = x }
}


// 연관된 필드 없이 게터나 세터 정의하기
class Thermometer{
  var celsius: Float = _

}

object MutableObject {

  val account = new BankAccount

  def main(args: Array[String]): Unit = {

    account deposit 100
    println(account withdraw 80)
    println(account withdraw 80)
  }

}
