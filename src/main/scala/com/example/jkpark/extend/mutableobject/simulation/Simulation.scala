package com.example.jkpark.extend.mutableobject.simulation

import scala.annotation.tailrec

abstract class Simulation {

  // Action을 빈 파라미터 목록에서 Unit을 반환하는 프로시저 타입에 대한 별명으로 정의
  // 이를 type member라 한다.
  type Action = () => Unit

  // WorkItem에 대한 정의
  case class WorkItem(time: Int, action: Action)

  // 현재의 시간을 0으로 설정
  private var curtime = 0
  def currentTime: Int = curtime

  // 아직 실행하지 않은 항목을 갖고 있는 list
  private var agenda: List[WorkItem] = List()

  // 새로운 작업 추가시 시간이 적게 걸리는 시간순으로 정렬한다.
  private def insert(ag: List[WorkItem], item: WorkItem):List[WorkItem] = {
    if(ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  // 두 번째 파라미터는 Call by name으로 인자를 전달하는 시점에 즉시 인자를 계산하지 않는다.
  def afterDelay(deplay: Int)(block: => Unit) = {
    // Delay만큼 시간을 추가한 WorkItem을 만든다.
    val item = WorkItem(currentTime + deplay, () => block)

    // 그리고 agenda에 추가한다.
    agenda = insert(agenda, item)
  }

  private def next() = {
    // 빈 리스트에 대한 조건이 없으므로 MatchError를 반환한다는 경고를 제거하기 위해 @unChecked를 썼다.
    (agenda: @unchecked) match  {
      // 패턴 매치를 통해 head와 tail을 분리한다.
      case item :: rest =>
        agenda = rest
        curtime = item.time
        item.action()
    }
  }

  def run() = {
    afterDelay(0) {
      println("*** simulation started, time = " + curtime + " ***")
    }
    while( !agenda.isEmpty) next()
  }

}
