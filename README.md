# R-NSGA-II

R-NSGA-II or Reference Point Based NSGA-II proposed by Kalyanmoy Deb et al.

## Status
[![Build Status](https://travis-ci.org/thiagodnf/r-nsga-ii.svg?branch=master)](https://travis-ci.org/thiagodnf/r-nsga-ii)
[![codecov](https://codecov.io/gh/thiagodnf/r-nsga-ii/branch/master/graph/badge.svg)](https://codecov.io/gh/thiagodnf/r-nsga-ii)

## Based on:
This implemention is based on the following paper:

> Deb, Kalyanmoy, and J. Sundar. "Reference point based multi-objective optimization using evolutionary algorithms." In *Proceedings of the 8th annual conference on Genetic and evolutionary computation*, pp. 635-642. ACM, 2006.

## Simulation Results

The following results were found based on the following parameter settings.

**For Binary Problem**

 - populationSize = 100;
 - maxEvaluations = 100000 * populationSize;
 - SinglePointCrossover(0.9)
 - BitFlipMutation(0.001)
 
 **For Double Problem**

 - populationSize = 100;
 - maxEvaluations = 100000 * populationSize;
 - SBXCrossover(0.9, 10.0)
 - PolynomialMutation(0.01, 20.0)
 
**Results**
		
| Problem  | Encoding | 0.01 | 0.001 | 0.0001 |
| ------------- | --- | ------------- |----------- | --------- |
| Knapsack  | Binary Solution | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/kp/r-nsga-ii-0.01.png">  | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/kp/r-nsga-ii-0.001.png"> | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/kp/r-nsga-ii-1.0E-5.png"> |
| ZDT 1  | Double Solution | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt1/r-nsga-ii-0.01.png">  | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt1/r-nsga-ii-0.001.png"> | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt1/r-nsga-ii-1.0E-5.png"> |
| ZDT 2  | Double Solution | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt2/r-nsga-ii-0.01.png">  | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt2/r-nsga-ii-0.001.png"> | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt2/r-nsga-ii-1.0E-5.png"> |
| ZDT 3  | Double Solution | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt3/r-nsga-ii-0.01.png">  | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt3/r-nsga-ii-0.001.png"> | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/zdt3/r-nsga-ii-1.0E-5.png"> |
| DTLZ 2  | Double Solution | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/dtlz2/r-nsga-ii-0.01.png">  | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/dtlz2/r-nsga-ii-0.001.png"> | <img width="180px" src="https://raw.githubusercontent.com/thiagodnf/r-nsga-ii/master/src/main/resources/dtlz2/r-nsga-ii-0.0001.png"> |
