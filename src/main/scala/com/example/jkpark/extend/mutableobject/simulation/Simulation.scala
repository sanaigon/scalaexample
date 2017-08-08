package com.example.jkpark.extend.mutableobject.simulation

import scala.annotation.tailrec

abstract class Simulation {

  // Action을 빈 파라미터 목록에서 Unit을 반환하는 프로시저 타입에 대한 별명으로 정의
  type Action = () => Unit

  case class WorkItem(time: Int, action: Action)
  private var curtime = 0
  def currentTime: Int = curtime
  private var agenda: List[WorkItem] = List()

  private def insert(ag: List[WorkItem], item: WorkItem):List[WorkItem] = {
    if(ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  def afterDelay(deplay: Int)(block: => Unit) = {
    val item = WorkItem(currentTime + deplay, () => block)
    agenda = insert(agenda, item)
  }

  private def next() = {
    (agenda: @unchecked) match  {
      case item :: rest =>
        agenda = rest
        curtime = item.time
        item.action()
    }
  }

  def run() = {
    afterDelay(0) {
      println("*** simulation started, time = " +
      curtime + " ***")
    }
    while( !agenda.isEmpty) next()
  }

}
