Instructions 
1. Install Java
2. Install sbt : https://www.scala-sbt.org/1.x/docs/Setup.html
3. `sbt`
4. simulation/runMain sim.question1b
5. simulation/runMain sim.question2a
6. simulation/runMain sim.question2b

```
sbt:devoir7> simulation/compile
[info] compiling 4 Scala sources to /Users/cintiasoares/Documents/BaccInfo/Aut2023/IFT-3655 MODELES STOCHASTIQUES/Devoirs/Devoir7/simulation/target/scala-3.3.1/classes ...
[success] Total time: 2 s, completed Nov. 19, 2023, 11:13:12 p.m.
sbt:devoir7> simulation/runMain sim.question1b
[info] running sim.question1b
Total CPU time 0:0:0.4

REPORT on Tally stat. collector ==> Statistic
    num. obs.      min          max        average     variance    standard dev
      1000       3.5E-3        0.150       9.4E-3       9.7E-5       9.8E-3
  95.0% conf. interval for the mean (Student approx.): (    8.8E-3,     0.010 )

[success] Total time: 0 s, completed Nov. 19, 2023, 11:14:25 p.m.
sbt:devoir7>
```

```
sbt:devoir7> simulation/runMain sim.question2a
[info] running sim.question2a
Total CPU time 0:0:0.0

REPORT on Tally stat. collector ==> Statistic MC
    num. obs.      min          max        average     variance    standard dev
      1000        1.000        1.000        1.000        0.000        0.000
  95.0% conf. interval for the mean (Student approx.): (     1.000,     1.000 )

Total CPU time 0:0:0.0

REPORT on Tally stat. collector ==> Statistic CMC
    num. obs.      min          max        average     variance    standard dev
      1000        0.000        0.078       1.4E-4       8.7E-6       3.0E-3
  95.0% conf. interval for the mean (Student approx.): (   -4.5E-5,    3.2E-4 )

[success] Total time: 0 s, completed Nov. 19, 2023, 11:15:09 p.m.
sbt:devoir7>
```

```
sbt:devoir7> simulation/runMain sim.question2b
[info] running sim.question2b
Total CPU time 0:0:0.0

REPORT on Tally stat. collector ==> Statistic Diff finies CMC v. a. communes
    num. obs.      min          max        average     variance    standard dev
      1000       -0.052        0.081       4.4E-4       4.3E-5       6.6E-3
  95.0% conf. interval for the mean (Student approx.): (    3.6E-5,    8.5E-4 )

[success] Total time: 0 s, completed Nov. 19, 2023, 11:16:09 p.m.
sbt:devoir7>
```