package com.example.jkpark.extend.typeparameter

class Publication(val title: String)
class Book(title: String) extends  Publication(title)

object Library {
  val books: Set[Book] =
    Set(
      new Book("Programming in scala"),
      new Book("Walden")
    )

  def printBookList(info: Book => AnyRef) = {
    // 아래의 코드에서 이터레이션하면서 println으로 info()의 결과값을 출력한다.
    // println은 객체의 toString() 메소드를 호출한 결과를 출력한다.
    // 이 과정은 AnyRef가 아니더라도 정상적으로 수행된다.
    // 이 것이 함수 결과 타입의 공변성이 의미하는 바다.
    for( book <- books ) println(info(book))
  }

}

