package sim

import umontreal.ssj.probdist.PoissonDist
import umontreal.ssj.rng.RandomStream
import umontreal.ssj.stat.Tally
import umontreal.ssj.rng.MRG32k3a
import umontreal.ssj.util.Chrono

class Estimateur(mu1:Double,mu2:Double,b:Int):
  val lambda1 = 1/mu1
  val lambda2 = 1/mu2

  def simulateRuns(n:Int,streamX1:RandomStream,streamX2:RandomStream,statC:Tally ):Unit=

    var i = 0
    while(i<n){
        var u1 = streamX1.nextDouble()
        var u2 = streamX2.nextDouble()

        var x1 = -mu1*math.log(1-u1)
        var x2 = -mu1*math.log(1-u2)
        
        var c = mu2*mu2/(mu1*mu1)*math.exp(-lambda1*b + lambda2*(x1+x2))
        statC.add(c)
        
        i+=1
    }



@main def question1b: Unit = 
    val n = 1000
    val mu1 = 2.0
    val mu2 = 5.0
    val b = 15

    val est = new Estimateur(mu1,mu2,b)

    val statC = new Tally("Statistic")

    val timer = new Chrono()


    est.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), statC)

    println(s"Total CPU time ${timer.format()}\n")

    statC.setConfidenceIntervalStudent()

    println(statC.report(0.95,3))