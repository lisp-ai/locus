(ns locus.elementary.semigroup.monoid.fp-monoid
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.lattice.core.object :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.quiver.unital.object :refer :all]
            [locus.elementary.semigroup.core.object :refer :all]))

; Finitely presented monoids
; A finitely presented monoid is the combination of a set of generators, and
; a set of relations of the terms over those generators. In order to retrieve the generating
; set of an FPMonoid use the morphic-gens function. The morphic gens function is
; generalized to get the morphic generating set of any category.

(deftype FPMonoid [gens relations])

(derive FPMonoid :locus.elementary.function.core.protocols/monoid)

(defmethod morphic-gens FPMonoid
  [^FPMonoid monoid]

  (.gens monoid))

; Get the commutativity relations of a presentation
(defn commutativity-relations
  [terms]

  (map
    (fn [pair]
      (let [[a b] (seq pair)]
        [[a b] [b a]]))
    (selections (set terms) 2)))

(defn pairwise-commutativity-relations
  [a b]

  (map
    (fn [[i j]]
      [[i j] [j i]])
    (cartesian-product (set a) (set b))))

; The free product of finitely presented monoids
(defn free-product-presentation
  [a b]

  (FPMonoid.
    (union (set (morphic-gens a)) (set (morphic-gens b)))
    (concat (.relations a) (.relations b))))

(defn direct-product-presentation
  [a b]

  (FPMonoid.
    (union (set (morphic-gens a)) (set (morphic-gens b)))
    (concat
      (.relations a)
      (.relations b)
      (pairwise-commutativity-relations (set (morphic-gens a)) (set (morphic-gens b))))))

; Add a new generator to the FP monoid
(defn add-generator
  [^FPMonoid monoid, x]

  (FPMonoid.
    (conj (.gens monoid) x)
    (.relations monoid)))

(defn add-relation
  [^FPMonoid monoid, r]

  (FPMonoid.
    (.gens monoid)
    (conj (.relations monoid) r)))

(defn delete-generator
  [^FPMonoid monoid, x]

  (FPMonoid.
    (disj (.gens monoid) x)
    (filter
      (fn [[a b]]
        (and
          (not (contains? (set a) x))
          (not (contains? (set b) x))))
      (.relations monoid))))

(defn delete-relation
  [^FPMonoid monoid, r]

  (FPMonoid.
    (.gens monoid)
    (filter
      (fn [relation]
        (not= relation r))
      (.relations monoid))))

; Get the idempotence relations of a presentation
(defn idempotence-relations
  [terms]

  (map
    (fn [i]
      [[i i] [i]])
    terms))

; The bicyclic monoid is a great example of a monoid with a simple presentation
(def bicyclic-monoid
  (FPMonoid.
    [0 1]
    [[[0 1] []]]))

(def four-spiral-monoid
  (FPMonoid.
    [0 1 2 3]
    [[[0 0] [0]]
     [[1 1] [1]]
     [[2 2] [2]]
     [[3 3] [3]]
     [[0 1] [1]]
     [[1 0] [0]]
     [[1 2] [1]]
     [[2 1] [2]]
     [[2 3] [3]]
     [[3 2] [2]]
     [[3 0] [3]]]))

