(ns locus.elementary.lattice.core.morphism
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.diamond.core.object :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.quiver.core.morphism :refer :all]
            [locus.elementary.quiver.core.thin-morphism :refer :all]
            [locus.elementary.quiver.unital.object :refer :all]
            [locus.elementary.quiver.unital.morphism :refer :all]
            [locus.elementary.lattice.core.object :refer :all])
  (:import (locus.elementary.lattice.core.object Lattice)
           (locus.elementary.diamond.core.object Diamond)))

; The category of lattices is distinguished from the category of categories,
; by its very special type of functors which are the lattice morphisms. These
; morphisms of lattices also need to preserve products and coproducts.

(deftype LatticeMorphism
  [source target func]

  AbstractMorphism
  (source-object [this] source)
  (target-object [this] target)

  StructuredDifunction
  (first-function [this]
    (->SetFunction
      (objects source)
      (objects target)
      func))
  (second-function [this]
    (->SetFunction
      (morphisms source)
      (morphisms target)
      (fn [pair]
        (map func pair))))

  ; Lattice homomorphisms are morphisms of quivers
  StructuredMorphismOfQuivers
  (underlying-morphism-of-quivers [this]
    (->MorphismOfQuivers
      (underlying-quiver source)
      (underlying-quiver target)
      (first-function this)
      (second-function this)))

  StructuredMorphismOfUnitalQuivers
  (underlying-morphism-of-unital-quivers [this]
    (->MorphismOfUnitalQuivers
      (underlying-unital-quiver source)
      (underlying-unital-quiver target)
      (first-function this)
      (second-function this)))

  ; Functional aspects of lattice homomorphisms
  ConcreteMorphism
  (inputs [this] (underlying-set source))
  (outputs [this] (underlying-set target))

  clojure.lang.IFn
  (invoke [this arg]
    (func arg))
  (applyTo [this args]
    (clojure.lang.AFn/applyToHelper this args)))

; The hierarchy of lattice morphisms
(derive LatticeMorphism :locus.elementary.function.core.protocols/functor)

; Composition and identities in the category of lattices
(defmethod compose* LatticeMorphism
  [a b]

  (LatticeMorphism.
    (source-object b)
    (target-object a)
    (comp (.func a) (.func b))))

(defmethod identity-morphism Lattice
  [lattice]

  (LatticeMorphism. lattice lattice identity))

; Morphisms of the component functions of a lattice
(defn morphism-of-join-functions
  [morphism]

  (let [sf (underlying-function morphism)]
    (Diamond.
     (join-function (source-object morphism))
     (join-function (target-object morphism))
     (function-product sf sf)
     sf)))

(defn morphism-of-meet-functions
  [morphism]

  (let [sf (underlying-function morphism)]
    (Diamond.
      (meet-function (source-object morphism))
      (meet-function (target-object morphism))
      (function-product sf sf)
      sf)))