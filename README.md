# Locus
An expert system based upon topos theory.

## Topos theoretic knowledge graphs
Developments in elementary copresheaf topos theory suggest a new approach to creating knowledge graphs for systematizing mathematical knowledge. This leads us to two new fundamental primitives.

- Memory locations: congruences in Sets
- Data dependencies: congruences in Sets^(->)

Traditional knowledge graphs are primarily focused on formalising knowledge about subobjects. By extending this with the new theory of congruence lattices of copresheaves, we can create a more effective means of systematizing mathematical knowledge. These same mechanisms can be used to create a mathematical theory of computation.

## Program architecture
We can divide the Locus code base into two components:

* Data visualisation : components for visualising knowlede graphs, copresheaves, and other mathematical objects
* Data processing : mechanisms for raw data processing on algebraic structures, symbolic expressions, and other mathematical objects

## Data visualisation
A Swing based graphical user interface is provided to handle the visualisation copresheaves over finitely generated categories. It consists of the generating system of a category displayed as a labeled directed graph and a system of directed graphs associated to each of those generators.

<img width="700" alt="nje" src="https://i.ibb.co/jVFZmV4/Copresheaf-viewer.png">

It is currently implemented using a combination of Swing widgets and Grapviz routines. Swing is used for the user interface and Graphviz is used to display the directed graphs. 

## Data processing
In addition to our foundational support for topoi, the following more advanced features are implemented:

* support for semigroup theoretic functionality like Green's relations, commuting graphs, subsemigroups, congruences, semigroup homomorphisms, regular semigroups, inverse semigroups, etc
* algorithms for handling finite groups, permutation groups, free groups, etc
* generic lattice theoretic functionality so that any object can be associated to lattices using the sub and con multimethods.
* support for elementary categories, functors, natural transformations, adjunctions, relational functors, arrow categories, subobject classifiers, internal hom, etc.
* elementary topos theory: sets, functions, morphisms of functions, bijections, disets, nsets, quivers, permutable quivers, unital quivers, dependency quivers, compositional quivers, compositional unital quivers, higher arity quivers, MSets, dependency functors, and copresheaves over arbitrary categories
* grothendeick topos theory: sheaves on sites 
* enriched categories such as 2-categories, 2-posets, and categories of modules
* graphs, hypergraphs, incidence structures, and their span copresheaves
* generic arithmetic operations based upon semirings and their specialisations
* a number of basic arithmetical structures like complex numbers, quaternions, matrices, polynomials, rational functions, power series, formal laurent series, elements of semigroup semirings, etc
* support for modules, semimodules, and vector spaces as well as algorithms for treating commutative monoids as semimodules and commutative groups as modules
* basic support for magmas and non-associative algebraic structures
* the hyperarithmetic of additive partitions
* topoi as foundations of computation
* interfaces with apache commons math 
* an upper ontology of mathematical structures

## Documentation 
A user manual is provided in the documentation folder. It describes our original research into the topos theoretic foundations of computation and their implementation.

## License
Apache license version 2.0

Copyright © 2022 John Bernier

## Version
1.0.1 release

## Contributing
Contributions of tests, documentation, code, ideas, etc are welcome.
