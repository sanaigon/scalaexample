package com.example.jkpark.extend.casepattern



sealed abstract class Expr
case class BindOp(op: String, value1: Expr, value2: Expr) extends Expr
case class UnOp(op: String, value2: Expr) extends Expr
case class Number(value: Int) extends Expr
case class Var(name: String) extends Expr

object CasePattern {

  def main(args: Array[String]): Unit = {

    /***
      * 15.3 패턴 가드
      */
    // 예를 들어 e+e 와 같은 두 피연자가 같으 덧셈 연산을 e*2처럼 곱셈으로 변경하는 규칙을 만들어야 한다고 해보자
    def simplyfiAdd(e: Expr) = e match {
      // 아래의 코드는 오류를 발생시킨다.
      // 스칼라에서는 선형 패턴으로 제한한다.
      // 즉 어떤 패턴 변수가 한 패턴안에 오직 한번만 나와야 한다.
      //case BindOp("+", x , x ) => BindOp("*", x, Number(2))
        // 하지만 만약 패턴가드를 쓴다면 위와 같은 상황을 우회할 수 있다.
      case BindOp("+", x , y ) if x==y => BindOp("*", x, Number(2))
      case _ => e
    }

    /**
      * 15.4 패컨 겹침
      *
      * 패턴 매치는 코드에 있는 순서를 따른다.
      * 그렇기 때문에 case문이 더 구체적인 규칙을 우선적으로 코드에 배치해야 한다.
      */

    /**
      * 봉인된 클래스
      *
      * match 식에서 놓친 패턴 조합이 있는지 찾도록 컴파일러에게 도움을 요청할 수 있다.
      * 이를 위해 컴파일러는 어느 것이 가능한지 알 수 있어야 한다.
      * 만약 Expr을 상속받는 새로운 클래스를 누군가 정의 했을 때 기존의 패턴패치에는 적용이 안될 수 있으므로
      * 예외가 발생할 수 있다.
      * 이 경우 새로운 클래스의 정의를 막으면 이런 일이 발생하지 않는다.
      *
      * 봉인된 클래스(sealed class)가 대안이 될 수 있다.
      * 이 클래스와 같은 파일이 아닌 다른 곳에서 새로운 서브 클래스를 만들 수 없다.
      * 이는 패턴매치에 유용하다.
      * sealed class를 상속받는 클래스를 패턴매치에 사용하면 놓친 부분을 컴파일러가 경고 메시지로 알려주기도 한다.
      *
      * !!!! 패턴 매치를 위한 클래스 계층을 작성한다면 그 계층에 속한 클래스를 봉인하는 것을 고려해야 한다!!!!!
      *
      * 맨 슈퍼 클래스 앞에 sealed 키워드를 붙이면 된다.
      */

    // 매치 가능한 케이스를 몇개 빼먹고 패턴 매치를 시도해보자

    def describe(e: Expr): String = e match {
      case Number(_) => "a Number"
      case Var(_) => "a variable"
    }

    // 위의 컴파일 과정에서 나오는 경고를 무시하고 싶은 경우라면 다음과 같이 @unchecked 애노테이션을 추가 할 수 있다.
    // @unchecked 애노테이션은 패턴매치시 case문이 모든 패턴을 다 다루는지 검사하는 일을 생략한다.
    def describe2(e: Expr): String = (e: @unchecked) match {
      case Number(_) => "a Number"
      case Var(_) => "a variable"
    }

    /**
      * 옵션 타입
      *
      * 선택적인 값을 표현한다.
      * X가 실제 값이라면 Some(X)라는 형태로 값을 표현하고
      * X가 값이 없다면 None이라는 객체가 된다.
      */

    val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")

    val value: Option[String] = capitals get "France"

    // 옵션 값을 분리하는 일반적인 방법은 패턴매치다.
    def show(x: Option[String]) = x match {
      case Some(s) => s
      case None => "?"
    }

    // java에서는 어떤 값이 없음을 표현하기 위해 Null을 사용하지만 Scala에서는 null을 사용하지 않는다.
    // Scala에서의 값 없음을 표현하는 방식은 None이다.

    /**
      *  패턴은 어디에서나
      *
      */
    // val이나 var을 정의 할 때 단순 식별자 대신 패턴을 사용할 수 있다.

    // 아래의 경우는 (Int, String) 형태의 튜플을 나타낸다.
    val myTuple = (123, "abc")

    // 아래의 경우는 number는 Int로 string은 String으로 나타낸다
    val (number, string) = (123, "abc")

    // 이런 구성요소는 케이스 클래스와 같이 사용할 때 유용하다.
    val BindOp(op, left, right) = BindOp("*", Number(5), Number(4))
    println(op)
    println(left)
    println(right)


    val data = new Array(500000)
    var i = 0
    while(true)
      {

        Thread.sleep(500)
      }

  }

}
