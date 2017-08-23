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

// 생성자 앞에 private를 붙이는 경우 생성자를 숨기게된다.
class Queue[T] private (
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
    */
  def enqueue(x: T) =
    new Queue(leading, x :: trailing)
}

object Queue {
  def apply[T](xs: T*): Queue[T] = new Queue[T](xs.toList, Nil)
}

object TypeParameterExample {
  def main(args: Array[String]): Unit = {
    val data = Queue(List(1,2), List(3))
    println(data)
  }
}