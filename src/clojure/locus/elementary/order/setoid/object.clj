(ns locus.elementary.order.setoid.object
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.logic.order.seq :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.incidence.system.setpart :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.quiver.unital.object :refer :all]
            [locus.elementary.quiver.permutable.object :refer :all]
            [locus.elementary.quiver.dependency.object :refer :all])
  (:import (locus.elementary.quiver.core.object Quiver)))

; Thin categories include all preorders such as equivalence relations. As thin categories, equivalence relations
; have all inverses for each morphism. It follows that equivalence relations are also thin groupoids,
; which are also called setoids.
(deftype Setoid [coll rel]
  ConcreteObject
  (underlying-set [this] coll)

  ; Classification as a structured quiver
  StructuredDiset
  (first-set [this] rel)
  (second-set [this] coll)

  StructuredQuiver
  (underlying-quiver [this] (->Quiver rel coll first second))
  (source-fn [this] first)
  (target-fn [this] second)
  (transition [this e] e)

  StructuredUnitalQuiver
  (identity-morphism-of [this x]
    (list x x))
  (underlying-unital-quiver [this]
    (->UnitalQuiver rel coll first second (fn [x] (list x x))))

  StructuredPermutableQuiver
  (invert-morphism [this x] (reverse x))
  (underlying-permutable-quiver [this]
    (->PermutableQuiver rel coll first second reverse))

  StructuredDependencyQuiver
  (underlying-dependency-quiver [this]
    (->DependencyQuiver rel coll first second (fn [x] (list x x)) reverse))

  ; Every thin category is a function
  ConcreteMorphism
  (inputs [this] (composability-relation this))
  (outputs [this] rel)

  clojure.lang.IFn
  (invoke [this [[a b] [c d]]]
    (list c b))
  (applyTo [this args]
    (clojure.lang.AFn/applyToHelper this args)))

; Classification of setoids
(derive Setoid :locus.elementary.function.core.protocols/thin-groupoid)

; Conversion routines
(defmulti to-setoid type)

(defmethod to-setoid Setoid
  [setoid] setoid)

(defmethod to-setoid Quiver
  [quiv]

  (Setoid.
    (objects quiv)
    (underlying-relation quiv)))

; Relational setoids
(defn relational-setoid
  [rel]

  (Setoid. (vertices rel) rel))

(defmethod to-setoid :default
  [rel]

  (relational-setoid rel))

; Underlying relation
(defmethod underlying-relation Setoid
  [^Setoid this] (.rel this))

(defmethod visualize Setoid
  [^Setoid this] (visualize (underlying-relation this)))

(defmethod inverse-function Setoid
  [^Setoid setoid]

  (->SetFunction
    (morphisms setoid)
    (morphisms setoid)
    (fn [elem]
      (reverse elem))))

; These are the equivalent of the product and coproduct of partitions
(defmethod product Setoid
  [& args]

  (Setoid.
    (apply cartesian-product (map underlying-set args))
    (apply product-relation (map underlying-relation args))))

(defmethod coproduct Setoid
  [& args]

  (Setoid.
    (apply cartesian-coproduct (map underlying-set args))
    (apply sum-relation (map underlying-relation args))))

; Setoids are self dual categories
(defmethod dual Setoid
  [setoid] setoid)

; Setoids are essentially equivalent to set partitions
(defn underlying-partition
  [setoid]

  (weakly-connected-components
    (underlying-relation setoid)))

; This is a function for creating setoids
(defn setoid
  [partition]

  (Setoid.
    (apply union partition)
    (equivalence-relation partition)))

; Common classes of setoids
(defn discrete-setoid
  [coll]

  (Setoid.
    coll
    (coreflexive-relation coll)))

(defn indiscrete-setoid
  [coll]

  (Setoid.
    coll
    (complete-relation coll)))