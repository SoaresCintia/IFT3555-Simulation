package sim

import umontreal.ssj.probdist.PoissonDist
import umontreal.ssj.rng.RandomStream
import umontreal.ssj.stat.Tally
import umontreal.ssj.rng.MRG32k3a
import umontreal.ssj.util.Chrono
import umontreal.ssj.probdist.NormalDist
import cern.jet.random.Normal

class EstimateurI(normal1:NormalDist,normal2:NormalDist,normal3:NormalDist,
x:Double,w:Double,t:Double,kappa:Double):
    
// changer pour calculer I = I[X<=x]

    def simulateRuns(n:Int,streamX1:RandomStream,
                         streamX2:RandomStream,
                         streamX3:RandomStream,statI:Tally ):Unit=

        var i = 0
        while(i<n){
            var u1 = streamX1.nextDouble()
            var u2 = streamX2.nextDouble()
            var u3 = streamX3.nextDouble()

            var y1 = normal1.inverseF(u1)
            var y2 = normal2.inverseF(u2)
            var y3 = normal3.inverseF(u3)
        
            var dep = kappa/y1 * math.sqrt(y2*y2/math.pow(w,4) + y3*y3/math.pow(t,4))
            if(dep <= x){
                statI.add(1)
            }else{
                statI.add(0)
            }
            
        
            i+=1
        }

class EstimateurJ(normal2:NormalDist,normal3:NormalDist,mu1:Double, sigma1:Double,x:Double,w:Double,t:Double,kappa:Double):

    var normal01 = new NormalDist(0,1)
    

    def simulateRuns(n:Int,streamX1:RandomStream,
                         streamX2:RandomStream,
                         statJ:Tally ):Unit=

        var i = 0
        while(i<n){
            var u2 = streamX1.nextDouble()
            var u3 = streamX2.nextDouble()

            var y2 = normal2.inverseF(u2)
            var y3 = normal3.inverseF(u3)

            var w1x = kappa/x * math.sqrt(y2*y2/math.pow(w,4) + y3*y3/math.pow(t,4))
        
            var c = 1 - normal01.cdf((w1x-mu1)/sigma1)

            statJ.add(c)
        
            i+=1
        }
@main def question2a: Unit = 
    val n = 1000

    val x = 5.0 
    val w = 4.0
    val t = 2.0
    val kappa = 500000.0

    val mu1 = 2900000.0
    val sigma1 = 1450000.0
    val mu2 = 500.0
    val sigma2 = 100.0
    val mu3 = 1000.0
    val sigma3 = 100.0

    val normal1 = new NormalDist(mu1,sigma1*sigma1)
    val normal2 = new NormalDist(mu2,sigma2*sigma2)
    val normal3 = new NormalDist(mu3,sigma3*sigma3)

    val estI = new EstimateurI(normal1,normal2,normal3,x,w,t,kappa)
    val statI = new Tally("Statistic MC")
    val timer = new Chrono()
    estI.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), new MRG32k3a(), statI)
    println(s"Total CPU time ${timer.format()}\n")
    statI.setConfidenceIntervalStudent()
    println(statI.report(0.95,3))

    val estJ = new EstimateurJ(normal2,normal3,mu1,sigma1,x,w,t,kappa)
    val statJ = new Tally("Statistic CMC")
    val timerJ = new Chrono()
    estJ.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), statJ)
    println(s"Total CPU time ${timerJ.format()}\n")
    statJ.setConfidenceIntervalStudent()
    println(statJ.report(0.95,3))
