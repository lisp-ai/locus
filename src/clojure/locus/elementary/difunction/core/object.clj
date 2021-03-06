(ns locus.elementary.difunction.core.object
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.logic.order.seq :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.incidence.system.setpart :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.diset.core.object :refer :all])
  (:import (locus.elementary.diset.core.object Diset)))

; Objects in the topos Sets^{2+2}
; A difunction is equivalent to a pair of functions. It can be defined as a copresheaf
; over the index category 2+2, and therefore it is an object of a topos. We apply
; all the common topos theoretic constructions like subobjects, quotients, products,
; coproducts, etc to difunctions by doubling up their counterparts in functions.

(deftype Difunction [f g]
  StructuredDifunction
  (first-function [this] f)
  (second-function [this] g)

  AbstractMorphism
  (source-object [this]
    (Diset. (inputs f) (inputs g)))
  (target-object [this]
    (Diset. (outputs f) (outputs g))))

; Difunction applications
(defn first-apply
  [func x]

  ((first-function func) x))

(defn second-apply
  [func x]

  ((second-function func) x))

; Difunction builders
(defn difunction
  [morphism]

  (Difunction.
    (first-function morphism)
    (second-function morphism)))

(defn equal-difunction
  [func]

  (Difunction. func func))

(defn identity-difunction
  [pair]

  (Difunction.
    (identity-function (first-set pair))
    (identity-function (second-set pair))))

(defmethod identity-morphism Diset
  [pair] (identity-difunction pair))

; Inclusion and projection pairs used for building difunctions
(defn inclusion-difunction
  [pair new-in new-out]

  (Difunction.
    (inclusion-function new-in (first-set pair))
    (inclusion-function new-out (second-set pair))))

(defn projection-difunction
  [pair in-partition out-partition]

  (Difunction.
    (projection-function in-partition)
    (projection-function out-partition)))

; Composition of difunctions
(defmethod compose* Difunction
  [a b]

  (Difunction.
    (compose-functions (first-function a) (first-function b))
    (compose-functions (second-function a) (second-function b))))

; Product and coproduct of the component functions of a difunction
(defn function-component-coproduct
  [difunction]

  (coproduct (first-function difunction) (second-function difunction)))

(defn function-component-product
  [difunction]

  (product (first-function difunction) (second-function difunction)))

; Epi mono factorisation
(defn function-kernel-pair
  [morphism]

  (list (function-kernel (first-function morphism))
        (function-kernel (second-function morphism))))

(defn function-image-pair
  [morphism]

  (list (function-image (first-function morphism))
        (function-image (second-function morphism))))

(defn difunction-kernel-image-factorisation
  [morphism]

  (list (function-kernel-pair morphism) (function-image-pair morphism)))

; Products and coproducts in the topos of difunctions
(defn difunction-product
  [& pairs]

  (Difunction.
    (apply function-product (map first-function pairs))
    (apply function-product (map second-function pairs))))

(defn difunction-coproduct
  [& pairs]

  (Difunction.
    (apply function-coproduct (map first-function pairs))
    (apply function-coproduct (map second-function pairs))))

(defmethod product Difunction
  [& args]

  (apply difunction-product args))

(defmethod coproduct Difunction
  [& args]

  (apply difunction-coproduct args))

; Subobject classifier
(def truth-diset
  (Diset. #{false true} #{false true}))

(defn subdiset-character
  [pair new-in new-out]

  (Difunction.
    (subset-character new-in (first-set pair))
    (subset-character new-out (second-set pair))))

; Operations for getting subobjects of difunctions
(defn restrict-first-function
  [difunction coll]

  (Difunction.
    (restrict-function (first-function difunction) coll)
    (second-function difunction)))

(defn restrict-second-function
  [difunction coll]

  (Difunction.
    (first-function difunction)
    (restrict-function (second-function difunction) coll)))

(defn restrict-difunction
  [difunction diset]

  (Difunction.
    (restrict-function (first-function difunction) (first-set diset))
    (restrict-function (second-function difunction) (second-set diset))))

(defn reduce-first-function
  [difunction new-in new-out]

  (Difunction.
    (subfunction (first-function difunction) new-in new-out)
    (second-function difunction)))

(defn reduce-second-function
  [difunction new-in new-out]

  (Difunction.
    (first-function difunction)
    (subfunction (second-function difunction) new-in new-out)))

(defn subdifunction?
  [difunction [a c] [b d]]

  (and
    (subfunction? (first-function difunction) a b)
    (subfunction? (second-function difunction) c d)))

(defn subdifunction
  [difunction [a c] [b d]]

  (Difunction.
    (subfunction (first-function difunction) a b)
    (subfunction (second-function difunction) c d)))

; Joining pairs of pairs of sets
(defn join-pair-of-set-pairs
  [& args]

  (list
    (apply join-set-pairs (map first args))
    (apply meet-set-pairs (map second args))))

(defn meet-pair-of-set-pairs
  [& args]

  (list
    (apply meet-set-pairs (map first args))
    (apply meet-set-pairs (map second args))))

; Enumeration of subobjects of difunctions
(defn difunction-subalgebras
  [difunction]

  (set
    (cartesian-product
      (all-subalgebras (first-function difunction))
      (all-subalgebras (second-function difunction)))))

; Get the quotients of difunctions
(defn quotient-first-function
  [difunction in-partition out-partition]

  (Difunction.
    (quotient-function (first-function difunction) in-partition out-partition)
    (second-function difunction)))

(defn quotient-second-function
  [difunction in-partition out-partition]

  (Difunction.
    (first-function difunction)
    (quotient-function (second-function difunction) in-partition out-partition)))

(defn difunction-congruence?
  [difunction [partition1 partition2] [partition3 partition4]]

  (and
    (io-relation? (first-function difunction) partition1 partition3)
    (io-relation? (second-function difunction) partition2 partition4)))

(defn quotient-difunction
  [difunction [partition1 partition2] [partition3 partition4]]

  (Difunction.
    (quotient-function (first-function difunction) partition1 partition3)
    (quotient-function (second-function difunction) partition2 partition4)))

; The congruence lattices of difunctions
(defn difunction-congruences
  [difunction]

  (set
    (cartesian-product
      (all-congruences (first-function difunction))
      (all-congruences (second-function difunction)))))

; Special classes of difunctions
(defn difunction?
  [pair]

  (= (type pair) Difunction))

(defn injective-difunction?
  [pair]

  (and
    (difunction? pair)
    (injective? (first-function pair))
    (injective? (second-function pair))))

(defn surjective-difunction?
  [pair]

  (and
    (difunction? pair)
    (surjective? (first-function pair))
    (surjective? (second-function pair))))

(defn invertible-difunction?
  [pair]

  (and
    (difunction? pair)
    (invertible? (first-function pair))
    (invertible? (second-function pair))))

(defn identity-difunction?
  [pair]

  (and
    (difunction? pair)
    (identity-function? (first-function pair))
    (identity-function? (second-function pair))))

(defn inclusion-difunction?
  [pair]

  (and
    (difunction? pair)
    (inclusion-function? (first-function pair))
    (inclusion-function? (second-function pair))))

(defn endo-difunction?
  [pair]

  (and
    (difunction? pair)
    (= (source-object pair) (target-object pair))))

(defn auto-difunction?
  [pair]

  (and
    (invertible-difunction? pair)
    (= (source-object pair) (target-object pair))))

(defn element-difunction?
  [pair]

  (and
    (difunction? pair)
    (let [src (source-object pair)]
      (and
        (singular-universal? (first-set src))
        (singular-universal? (second-set src))))))

; Equality conditions by properties of the two functions
(defn common-input-difunction?
  [difunction]

  (and
    (difunction? difunction)
    (= (inputs (first-function difunction)) (inputs (second-function difunction)))))

(defn common-output-difunction?
  [difunction]

  (and
    (difunction? difunction)
    (= (outputs (second-function difunction)) (outputs (second-function difunction)))))

(defn parallel-difunction?
  [difunction]

  (and
    (difunction? difunction)
    (= (inputs (first-function difunction)) (inputs (second-function difunction)))
    (= (outputs (first-function difunction)) (outputs (second-function difunction)))))

(defn equal-difunction?
  [difunction]

  (and
    (difunction? difunction)
    (equal-functions? (first-function difunction) (second-function difunction))))

; Ontology of properties of difunctions
(defn !=difunction
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= a b)))

(defn !=first-function
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (first-function a) (first-function b))))

(defn !=second-function
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (second-function a) (second-function b))))

(defn !=first-inputs
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (inputs (first-function a)) (inputs (first-function b)))))

(defn !=second-inputs
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (inputs (second-function a)) (inputs (second-function b)))))

(defn !=first-outputs
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (outputs (first-function a)) (outputs (first-function b)))))

(defn !=second-outputs
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (outputs (second-function a)) (outputs (second-function b)))))

(defn !=source-diset
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (source-object a) (source-object b))))

(defn !=target-diset
  [a b]

  (and
    (difunction? a)
    (difunction? b)
    (not= (target-object a) (target-object b))))

