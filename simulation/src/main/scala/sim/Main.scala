package sim

import umontreal.ssj.probdist.PoissonDist
import umontreal.ssj.rng.RandomStream
import umontreal.ssj.stat.Tally
import umontreal.ssj.rng.MRG32k3a
import umontreal.ssj.util.Chrono

class Collision(k:Int,m:Int,maxCount:Int):
  val lambda = m*m/(2.0*k)
  val used = Array.ofDim[Boolean](k)
  val counts = Array.ofDim[Int](maxCount + 1)
  val poisson = new PoissonDist(lambda)

  def simulate (stream:RandomStream):Int=

    var i = 0
    while(i<k){
        used(i) = false
        i+=1
    }
        var c = 0

    var j = 0
    while(j<m){
        val loc = stream.nextInt(0,k-1)
        if (used(loc)){
            c += 1 
        }else{
            used(loc) = true
        }
        j=j+1
    }
    c

  def simulateRuns(n:Int,stream:RandomStream,statC:Tally ):Unit=
    if ()
    statC.init()

    var j = 0
    while(j<maxCount){
        counts(j)=0
        j+=1
    }

    var c = 0
    var i = 0
    while(i<n){
        c = simulate(stream)
        statC.add(c)
        if(c>maxCount){
            c=maxCount
        }
        counts(c) += 1
        i+=1
    }



@main def hello: Unit = 
    val k = 10000
    val m = 500
    val maxCount = 30
    val n = 10000000

    val col = new Collision(k,m,maxCount)
    val statC = new Tally("Statistic on collision")
    val timer = new Chrono()

    col.simulateRuns(n, new MRG32k3a(), statC)

    println(s"Total CPU time ${timer.format()}\n")

    statC.setConfidenceIntervalStudent()

    println(statC.report(0.95,3))

    println(s"Theoretical mean: lambda = ${col.lambda}\n")

    println("""|Counters:
               |c   count   Poisson expect""".stripMargin)

    var c = 0
    while(c<maxCount){
        println(f"$c%2d ${col.counts(c)}%10d ${n * col.poisson.prob(c)}%12.1f%n")
        c+=1
    }
