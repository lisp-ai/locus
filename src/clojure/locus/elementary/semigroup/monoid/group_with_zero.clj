(ns locus.elementary.semigroup.monoid.group-with-zero
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.semigroup.core.object :refer :all]
            [locus.elementary.semigroup.monoid.object :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.group.core.object :refer :all]
            [locus.elementary.lattice.core.object :refer :all])
  (:import (locus.elementary.semigroup.monoid.object Monoid)
           (locus.elementary.group.core.object Group)
           (locus.elementary.semigroup.core.object Semigroup)))

; Let F be a field or a semifield. Then the multiplicative semigroup (F,*) of F
; is a group with zero, which means that it has an inverse element, the reciprocal,
; for each element that isn't zero. In order to support this fundamental class of
; inverse semigroups for use in field theory, we provide a special class of
; groups with zero.

; Groups with zero include the multiplicative groups of fields
(deftype GroupWithZero [elems op id inv zero]
  ConcreteObject
  (underlying-set [this] elems)

  ; Semigroups are semigroupoids and so they are structured quivers
  StructuredDiset
  (first-set [this] elems)
  (second-set [this] #{0})

  StructuredQuiver
  (underlying-quiver [this] (singular-quiver elems 0))
  (source-fn [this] (constantly 0))
  (target-fn [this] (constantly 0))
  (transition [this obj] (list 0 0))

  ; Every semigroup is a function
  ConcreteMorphism
  (inputs [this] (complete-relation elems))
  (outputs [this] elems)

  clojure.lang.IFn
  (invoke [this obj] (op obj))
  (applyTo [this args] (clojure.lang.AFn/applyToHelper this args)))

(derive GroupWithZero :locus.elementary.function.core.protocols/monoid)

(defmethod group-with-zero? GroupWithZero
  [group] true)

(defmethod identity-elements GroupWithZero
  [^GroupWithZero group] #{(.id group)})

(defmethod zero-elements GroupWithZero
  [^GroupWithZero group] #{(.zero group)})

(defmethod adjoin-zero :locus.elementary.function.core.protocols/group
  [group]

  (GroupWithZero.
    (cartesian-coproduct #{0} (underlying-set group))
    (fn [[[i v] [j w]]]
      (cond
        (zero? i) (list 0 0)
        (zero? j) (list 0 0)
        :else (list 1 (group (list v w)))))
    (list 1 (identity-element group))
    (fn [[i v]]
      (if (zero? i)
        (list 0 0)
        (list 1 (invert-element group v))))
    (list 0 0)))

(defmethod group-with-zero :default
  [coll func one inv zero]

  (GroupWithZero. coll func one inv zero))

(defmethod group-of-units GroupWithZero
  [^GroupWithZero group]

  (Group.
    (difference (underlying-set group) (zero-elements group))
    (.op group)
    (.id group)
    (.inv group)))

(defmethod sub GroupWithZero
  [^GroupWithZero semigroup]

  (let [group (group-of-units semigroup)
        zero (zero-element semigroup)]
    (->Lattice
      (set
       (map
         (fn [[subzero subgroup]]
           (union subzero subgroup))
         (cartesian-product
           (set (list #{} #{zero}))
           (all-subgroups group))))
      (comp (partial subsemigroup-closure semigroup) union)
      intersection)))