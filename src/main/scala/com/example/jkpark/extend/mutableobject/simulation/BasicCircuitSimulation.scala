package com.example.jkpark.extend.mutableobject.simulation

abstract class BasicCircuitSimulation extends Simulation {
  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGateDeplay: Int

  class Wire {
    // 선의 현재 신호
    private var sigVal = false
    // 선과 엮인 모든 액션 프로시저를 표현
    private var actions: List[Action] = List()

    // 현재 선의 신호를 반환한다.
    def getSignal = sigVal

    // 선의 신호를 sig로 설정한다.
    def setSignal(s: Boolean) = {
      if (s != sigVal) {
        sigVal = s
        // 모든 원소에 대해 "_ ()' 함수를 적용한다.
        // "_ ()" 은 ' f=> f ()' 를 축약한 형태이다.
        // 즉 어떤 함수를 받아서 빈 파라미터 목록으로 그 함수를 호출한다.
        actions foreach (_ ())
      }
    }

    // 선의 액션에 구체적인 프로시저 p를 추가한다.
    def addAction(a: Action) = {
      actions = a :: actions
      a()
    }
  }


  def inverter( input: Wire, output: Wire) =  {
    def invertAction() = {
      val inputSig = input.getSignal

      // 지연시간 이후에 출력 값이 변하는 효과를 나타나도록 한다.
      afterDelay(InverterDelay) {
        output setSignal !inputSig
      }
    }

    input addAction invertAction
  }

  def andGate( a1: Wire, a2: Wire, output: Wire ) = {
    def andAction() = {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (a1Sig & a2Sig)
      }
    }

    a1 addAction andAction
    a2 addAction andAction
  }

  def orGate( o1: Wire, o2: Wire, output: Wire) = {
    def orAction() = {
      val o1Sig = o1.getSignal
      val o2Sig = o2.getSignal
      afterDelay(OrGateDeplay) {
        output setSignal (o1Sig | o2Sig)
      }
    }

    o1 addAction orAction
    o2 addAction orAction
  }

  def probe(name: String, wire: Wire) = {
    def probeAction() = {
      println(name + " " + currentTime + " new-value =" + wire.getSignal )
    }
    wire addAction probeAction
  }

}
