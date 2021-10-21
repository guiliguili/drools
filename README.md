An open source rule engine, [DMN engine](https://drools.org/learn/dmn.html) and complex event processing (CEP) engine for Java™ and the JVM Platform.

Drools is a business rule management system with a forward-chaining and backward-chaining inference based rules engine, allowing fast and reliable evaluation of business rules and complex event processing. A rule engine is also a fundamental building block to create an expert system which, in artificial intelligence, is a computer system that emulates the decision-making ability of a human expert.

Be sure to check out the Drools' project [website](https://drools.org) and [documentation](https://drools.org/learn/documentation.html)!

Deploying SAP Commerce Cloud patched version
==========================
This branch contains a patched version of Drools required by SAP Commerce.

The following module have been patched :
* [drools-core](./drools-core)
* [drools-core-reflective](./drools-core-reflective)
* [drools-compiler](./drools-compiler)
 
In order to be able to deploy it to your Maven remote repository :
* Deploy the Drools/jBMP BOMs using the patched Drools version for SAP Commerce : https://github.com/guiliguili/droolsjbpm-build-bootstrap/tree/feature/BIT-5925
* Update the `distributionManagement` management sections to use your Maven remote repository in [pom.xml](./pom.xml)
* Deploy the drools root module by running `mvn clean deploy -N`
* Deploy each module above  by running `mvn clean deploy`

Developing Drools and jBPM
==========================

**If you want to build or contribute to a kiegroup project, [read this document](https://github.com/kiegroup/droolsjbpm-build-bootstrap/blob/master/README.md).**

**It will save you and us a lot of time by setting up your development environment correctly.**
It solves all known pitfalls that can disrupt your development.
It also describes all guidelines, tips and tricks.
If you want your pull requests (or patches) to be merged into master, please respect those guidelines.
