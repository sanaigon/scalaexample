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
  // 만약 아래와 같은 경우 private[this]를 제거 하면 컴파일 되지 않는다.
  // 어떤 필드가 정의된 바로 그 객체에서만 접근이 가능하다면, 변성에 아무 문제를 일으키지 않는다.
  // 변성 타입이 타입 오류를 발생시키는 경우는 다음과 같다.
  // 객체가 정의된 시점의 타입보다 정적으로 더 약한 타입의 객체에 대한 참조가 발생하게 되는 경우이다.
  // 하지만 아래의 경우는 정의한 객체만 접근이 가능하기 때문에 문제를 일으키지 않는다.
  // 스칼라는 +, - 같은 변성 표기가 있는 타입 파라미터의 변성에 맞는 위치 사용을 검사할 때
  // 객체 비공개 정의는 제외하고 검사한다.
  private[this] var leading: List[T], // 앞 부분 부터 원소를 저장
  private[this] var trailing: List[T] // 원소를 큐의 뒷 부분 부터 저장
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
//  private def mirror =
//    if (leading.isEmpty)
//      new Queue(trailing.reverse, Nil)
//    else
//      this
  private def mirror() =
    if (leading.isEmpty)
      while(!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }

  /**
    * head는 mirror를 수행한 후 나오는 Queue의 head값이다.
    * @return
    */
  //def head = mirror.leading.head
  def head = {
    mirror()
    leading.head
  }

  /**
    * tail 값은 mirror를 수행한 q에 대해
    * q의 leading의 tail 부분과 q의 trailing 부분을 합성해 만들어낸 queue이다.
    * @return
    */
  def tail = {
    mirror()
    new Queue(leading.tail, trailing)
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

  // getTitle은 Book의 슈퍼타입은 Publication을 인자로 받는다.
  // 이렇게 해도 잘 동작하는 이유는??
  // printBookList의 인자 info의 파라미터 Type이 Book이기 때문에
  // printBookList 메소드의 본문은 그 info에 오직 Book만을 넘길 수 있다.
  // 그리고 getTitle의 파라미터 Type은 publication이기 때문에
  // getTitle 함수 본문에서는 publication의 맴버에만 접근 할 수 있다.
  // Book은 publication의 서브 클래스이므로 서브클래스들은 모든 메소들을 가지고 있다.
  // 즉 아래와 같이 Publication => String이 Book => AnyRef의 서브 타입이기에 컴파일이 가능하다.
  // 이는 인자값은 반공변성을 띄고 있고 결과 값은 공변성을 띄도록 Function1이 정의를 했기 때문에 가능하다.
  // 그래서 반공변을 띄도록 Type을 지정하면 자신보다 Super Class들 까지 모두 인자로 받을 수 있도록 설정하는 것이다.
  // 공변성을 띄도록 설정 하는 경우 자신의 Subtype으로만 인자를 받을 수 있도록 지정하는 것이다.
  def getTitle(p: Publication): String = p.title

  def main(args: Array[String]): Unit = {
    val data: Queue[Apple] = Queue(new Apple)
    val orangeObj = new Orange
    val retVal: Queue[Fruit] = data.enqueue(orangeObj)
    //println(data.tail)

    // 함수의 결과 타입은 공변성을 갖고 있기 때문에 아래의 함수는 타입검사를 통과한다.
    Library.printBookList(getTitle)
  }
}