package com.example.jkpark.extend.packageexam


package bobsrockets.navigation {
  package navigation {

    class Navigator {
      //bobsrockets.navigation.StarMap을 쓸 필요가 없다.
      val map = new StarMap
    }

    class StarMap

  }
  // bobsrockets.navigation.tests 패키지 안쪽
  class Ship {
    val nav = new navigation.Navigator
  }

  package fleets {

    class Fleet {
      // bobsrockets.ship을 쓸 필요가 없다.
      def addShip() = { new Ship }
    }
  }
}
/**
  * Created by gon on 2017-07-24.
  */
class PackageExample extends App{

}
