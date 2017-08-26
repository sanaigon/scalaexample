package com.example.jkpark.extend.typeparameter

// 효츌적이지 않은 Queue의 구조

class SlowAppedQueue[T](elems: List[T]) {
  def head = elems.head
  def tail = elems.tail

  // enqueue method는 Queue에 들어있는 원소의 개수에 비례
  def enqueue(x: T) = new SlowAppedQueue(elems :: List(x))
}

class SlowHeadQueue[T](smele: List[T]) {
  // head와 tail은 원소의 개수에 비례하는 시간이 소요된다.
  def head = smele.last
  def tail = new SlowHeadQueue(smele.init)
  // enqueuqe는 상수시간으로 동작한다.
  def enqueue(x: T) = new SlowHeadQueue(x :: smele)
}

/**
  * 아래는 T Type에 대해 반공변을 정의했다.
  * AnyRef의 출력 채널은 String의 서브타입이다.
  * 직관적이지 않지만 이게 더 타당하다.
  * OutputChannel[String]으로 할 수 있는일이 무엇인가 생각해보자
  * 이 tarit은 write 연산으로 String을 쓰는 것이다.
  * 같은 연산을 OutputChannel[AnyRef]에서 쓸 수 있다.
  *
  * 따라서 OutputChannel[String]이 필요한 곳이라면 OutputChannel[AnyRef] 바꿔 넣어도 문제가 없다.
  * OutputChannel[AnyRef]가 필요한 곳에 OutputChannel[String]을 넣는것은 안전하지 않다.
  * OutputChannel[String]은 오직 String만 쓸 수 있기 때문이다.
  *
  * 리스코프 치환 원칙
  * U 타입이 필요한 모든 경우를 T 타입의 값으로 대치할 수 있다면
  * T 타입을 U 타입의 서브타입으로 가정해도 안전하다.
  *
  * T가 U의 모든 연산을 지원하고 모든 T의 연산이 그에 대응하는 U의 연산에 비해 요구하는 것은 더 적고
  * 제공하는 것은 더 많은 경우 리스코프치환 원칙이 성립한다.
  *
  * OutputChannel[AnyRef]와 OutputChannel[String]은 같은 write 연산을 제공한다.
  * OutputChannel[AnyRef]에 있는 연산이 OutputChannel[String]에 비해 더 적은 것을 요구하기 때문에 이 원칙이 성립한다.
  *
  * 공변성과 반공변성이 같이 존재하는 경우
  * A => B인 함수 리터럴은 내부적으로 Function1[A,B]로 변환한다.
  * Function1은 인자 타입 S에 대해서는 반공변성을 띄고 있고 반환 타입 T는 공변성을 띈다.
  * 이는 S는 함수가 요구하는 것이고 결과는 함수가 제공하는 것이기 때문이다.
  *
  * Function1[-S, +T] {
  *   def apply(x: S): T
  * }
  *
  * 제공은
  */
trait OutputChannel[-T] {
  def write(x: T)
}


sealed class Fruit{
  override def toString: String = "Fruit"
}
class Orange extends Fruit{
  override def toString: String = "Orange"
}

class Apple extends Fruit{
  override def toString: String = "Apple"
}


// 생성자 앞에 private를 붙이는 경우 생성자를 숨기게된다.
class Queue[+T] (
  private val leading: List[T], // 앞 부분 부터 원소를 저장
  private val trailing: List[T] // 원소를 큐의 뒷 부분 부터 저장
){

  /*
  아래와 같이 보조 생성자로 Queue의 객체를 만들어 줄 수는 있지만.
  제일 깔끔한 방법은 Companion object를 이용한 팩토리 메소드를 만드는 것이다.
  def this() = this(Nil, Nil)
  def this(elems: T*) = this(elems.toList, Nil)
  */
  /**
    * leading이 비어있다면
    * trailing을 뒤집은 상태로 새로운 큐를 생성한다.
    *
    * 만약 비어있지 않다면 현재의 leading을 제공하기 위해 this를 반환한다.
    * @return
    */
  private def mirror =
    if (leading.isEmpty)
      new Queue(trailing.reverse, Nil)
    else
      this

  /**
    * head는 mirror를 수행한 후 나오는 Queue의 head값이다.
    * @return
    */
  def head = mirror.leading.head

  /**
    * tail 값은 mirror를 수행한 q에 대해
    * q의 leading의 tail 부분과 q의 trailing 부분을 합성해 만들어낸 queue이다.
    * @return
    */
  def tail = {
    val q = mirror
    new Queue(q.leading.tail, q.trailing)
  }

  /**
    * enqueue는 leading 부분은 그대로 둔 상태에서 trailing 부분에 추가한다.
    * @param x 추가할 원소
    * @return leading 과 x를 추가한 trailing 부분를 합성한 새로운 queue
    * 아래는 T를 U의 하위 바운드로 지정했다.
    * 즉 U는 T의 슈퍼 타입이여야만 한다.
    * enqueue에서 사용하는 Type은 T가 아닌 U를 사용한다.
    * 그래서 반환값 또한 Queue[U] Type이 된다.
    *
    * Fruit의 하위인 Apple, Orange가 있다고 치자.
    * 아래처럼 하위바운드가 걸린 경우 Orange를 Queue[Apple]에 추가 할 수 있다.
    * 결과는 Queue의
   */
  def enqueue[U >: T](x: U): Queue[U] =
    new Queue(leading, x :: trailing)
}

object Queue {
  def apply[T](xs: T*): Queue[T] = new Queue[T](xs.toList, Nil)
}

object TypeParameterExample {
  def main(args: Array[String]): Unit = {
    val data: Queue[Apple] = Queue(new Apple)
    val orangeObj = new Orange
    val retVal: Queue[Fruit] = data.enqueue(orangeObj)
    //println(data.tail)
    println(retVal.tail.head)
  }
}