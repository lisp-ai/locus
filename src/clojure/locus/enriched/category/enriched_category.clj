(ns locus.enriched.category.enriched-category
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.logic.order.seq :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.lattice.core.object :refer :all]
            [locus.elementary.category.core.object :refer :all]
            [locus.elementary.category.core.morphism :refer :all]
            [locus.elementary.category.core.natural-transformation :refer :all]
            [locus.elementary.relational.relation.set-relation :refer :all]
            [locus.elementary.order.core.poset :refer :all]
            [locus.linear.semimodule.object :refer :all]
            [locus.linear.semimodule.morphism :refer :all]))

; Enriched category
; This is an ordinary category with a special bivariate hom function. The overriden
; hom function of an enriched category produces another structure such as a
; category on a given hom class of objects.
(defrecord EnrichedCategory [morphisms objects source target func id hom]
  ; Categories are structured quivers
  StructuredDiset
  (first-set [this] morphisms)
  (second-set [this] objects)

  StructuredQuiver
  (underlying-quiver [this] (->Quiver morphisms objects source target))
  (source-fn [this] source)
  (target-fn [this] target)
  (transition [this e] (list (source e) (target e)))

  ; Categories are structured functions
  ConcreteMorphism
  (inputs [this] (composability-relation this))
  (outputs [this] morphisms)

  clojure.lang.IFn
  (invoke [this arg] (func arg))
  (applyTo [this args] (clojure.lang.AFn/applyToHelper this args)))

(derive EnrichedCategory :locus.elementary.function.core.protocols/category)

; Construct the category of categories as a 2-category in the standard fashion.
(def cat
  (EnrichedCategory.
    functor?
    category?
    source-object
    target-object
    (fn [[a b]] (compose a b))
    identity-morphism
    (fn [a b]
      (functor-category a b))))

; Rel is a 2-category with ordered hom classes
(def rel
  (EnrichedCategory.
    set-relation?
    universal?
    source-object
    target-object
    (fn [[r s]] (compose r s))
    identity-morphism
    (fn [a b]
      (->Poset
        (relational-hom-class a b)
        (fn [[rel1 rel2]]
          (included-set-relation? a b))))))

; Get the module category of a ring
(defn module-category
  [ring]

  (EnrichedCategory.
    (module-ring-classifier ring)
    (module-homomorphism-ring-classifier ring)
    source-object
    target-object
    (fn [[r s]] (compose r s))
    identity-morphism
    (fn [a b]
      (additive-hom-group a b))))

