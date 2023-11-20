package sim

import umontreal.ssj.probdist.PoissonDist
import umontreal.ssj.rng.RandomStream
import umontreal.ssj.stat.Tally
import umontreal.ssj.rng.MRG32k3a
import umontreal.ssj.util.Chrono
import umontreal.ssj.probdist.NormalDist
import cern.jet.random.Normal

class EstimateurMC(normal1:NormalDist,
    normal2:NormalDist,
    normal3:NormalDist,
    normal3p:NormalDist,
    x:Double,w:Double,t:Double,kappa:Double):
    
    def simulateRuns(n:Int,streamX1:RandomStream,
                         streamX2:RandomStream,
                         streamX3:RandomStream,statI:Tally ):Unit=

        var i = 0
        while(i<n){
            val u1 = streamX1.nextDouble()
            val u2 = streamX2.nextDouble()
            val u3 = streamX3.nextDouble()

            val y1 = normal1.inverseF(u1)
            val y2 = normal2.inverseF(u2)
            val y3 = normal3.inverseF(u3)
            val y3p = normal3p.inverseF(u3)
        
            val dep1 = kappa/y1 * math.sqrt(y2*y2/math.pow(w,4) + y3*y3/math.pow(t,4))
            val dep2 = kappa/y1 * math.sqrt(y2*y2/math.pow(w,4) + y3p*y3p/math.pow(t,4))

            val diff = dep2 - dep1

            val est1 = 0
            if(dep1 <= x){
                val est1 = 1
            }
            
            val est2 = 0
            if(dep2 <= x){
                val est2 = 1
            }

            val der = est2 - est1

            statI.add(der)
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

    val mu1 = 29000000.0
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

    val estMC = new EstimateurI(normal1,normal2,normal3,x,w,t,kappa)
    val statMC = new Tally("Statistic Diff finies MC v. a. ind.")
    val timerMC = new Chrono()
    estMC.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), new MRG32k3a(), statMC)
    println(s"Total CPU time ${timerMC.format()}\n")
    statMC.setConfidenceIntervalStudent()
    println(statMC.report(0.95,3))
    
    // 2. Diff ́erences finies avec CMC et des variables al ́eatoires ind ́ependantes 
    // (IRN), avec δ = 1; 

    val estCMCind = new EstimateurCMC(normal2,normal3,normal3p,mu1,sigma1,x,w,t,kappa,true)
   
    val statCMCind = new Tally("Statistic Diff finies CMC v. a. ind.")

    val timerCMCind = new Chrono()
    estCMCind.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), statCMCind)
    println(s"Total CPU time ${timerCMCind.format()}\n")
    statCMCind.setConfidenceIntervalStudent()
    println(statCMCind.report(0.95,3))
    
    // // 3. Diff ́erences finies avec CMC et des variables al ́eatoires communes (CRN), 
    val estCMCdep = new EstimateurCMC(normal2,normal3,normal3p,mu1,sigma1,x,w,t,kappa,false)
   
    val statCMCdep = new Tally("Statistic Diff finies CMC v. a. communes")

    val timerCMCdep = new Chrono()
    estCMCdep.simulateRuns(n, new MRG32k3a(),  new MRG32k3a(), statCMCdep)
    println(s"Total CPU time ${timerCMCdep.format()}\n")
    statCMCdep.setConfidenceIntervalStudent()
    println(statCMCdep.report(0.95,3))
    
    // 4. D ́eriv ́ee stochastique avec CMC;


