package sim

import umontreal.ssj.probdist.PoissonDist
import umontreal.ssj.rng.RandomStream
import umontreal.ssj.stat.Tally
import umontreal.ssj.rng.MRG32k3a
import umontreal.ssj.util.Chrono
import umontreal.ssj.probdist.NormalDist
import cern.jet.random.Normal

class EstimateurMC(normal1:NormalDist,normal2:NormalDist,normal3:NormalDist,
x:Double,w:Double,t:Double,kappa:Double):
    
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

class EstimateurCMC(normal2:NormalDist,
                    normal3:NormalDist,
                    normal3p:NormalDist,mu1:Double, 
                    sigma1:Double,x:Double,w:Double,t:Double,kappa:Double,
                    vaInd:Boolean):

    var normal01 = new NormalDist(0,1)
    

    def simulateRuns(n:Int,streamX1:RandomStream,
                         streamX2:RandomStream,
                         statJ:Tally ):Unit=

        var i = 0
        while(i<n){
            var u2 = streamX1.nextDouble()
            var u3  = u2
            if(vaInd){
                 u3 = streamX2.nextDouble()
            }
            

            var y2 = normal2.inverseF(u2)
            var y3 = normal3.inverseF(u3)
            var y3p = normal3p.inverseF(u3)

            var w1x = kappa/x * math.sqrt(y2*y2/math.pow(w,4) + y3*y3/math.pow(t,4))

            var w1xp = kappa/x * math.sqrt(y2*y2/math.pow(w,4) + y3p*y3p/math.pow(t,4))
        
            var estX1 = 1 - normal01.cdf((w1x-mu1)/sigma1)

            var estX2 = 1 - normal01.cdf((w1xp-mu1)/sigma1)

            var c = estX2 - estX1

            statJ.add(c)
        
            i+=1
        }

@main def question2b: Unit = 
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

    val delta = 1.0
    val sigma3p = sigma3 + delta

    val normal1 = new NormalDist(mu1,sigma1*sigma1)
    val normal2 = new NormalDist(mu2,sigma2*sigma2)
    val normal3 = new NormalDist(mu3,sigma3*sigma3)
    val normal3p = new NormalDist(mu3,sigma3p*sigma3p)

    // 1. Differences finies avec MC et des variables aleatoires independantes
    //  (IRN), avec δ = 1; 

    // val estX1 = new EstimateurI(normal1,normal2,normal3,x,w,t,kappa)
    // val statX1 = new Tally("Statistic Diff finies MC v. a. ind.")
    // val timer = new Chrono()
    // estX1.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), new MRG32k3a(), statX1)
    // println(s"Total CPU time ${timer.format()}\n")
    // statX1.setConfidenceIntervalStudent()
    // println(statX1.report(0.95,3))
    
    // 2. Diff ́erences finies avec CMC et des variables al ́eatoires ind ́ependantes 
    // (IRN), avec δ = 1; 

    // val est = new EstimateurCMC(normal2,normal3,normal3p,mu1,sigma1,x,w,t,kappa,true)
   
    // val stat = new Tally("Statistic Diff finies CMC v. a. ind.")

    // val timer = new Chrono()
    // est.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), stat)
    // println(s"Total CPU time ${timer.format()}\n")
    // stat.setConfidenceIntervalStudent()
    // println(stat.report(0.95,3))
    
    // 3. Diff ́erences finies avec CMC et des variables al ́eatoires communes (CRN), 
    val est3 = new EstimateurCMC(normal2,normal3,normal3p,mu1,sigma1,x,w,t,kappa,false)
   
    val stat3 = new Tally("Statistic Diff finies CMC v. a. communes")

    val timer3 = new Chrono()
    est3.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), stat3)
    println(s"Total CPU time ${timer3.format()}\n")
    stat3.setConfidenceIntervalStudent()
    println(stat3.report(0.95,3))
    
    // 4. D ́eriv ́ee stochastique avec CMC;


