(ns locus.elementary.quiver.unital.morphism
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.logic.order.seq :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.incidence.system.setpart :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.diamond.core.object :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.quiver.core.morphism :refer :all]
            [locus.elementary.quiver.unital.object :refer :all])
  (:import [locus.elementary.quiver.core.object Quiver]
           [locus.elementary.function.core.object SetFunction]
           (locus.elementary.quiver.unital.object UnitalQuiver)))

; The topos of unital quivers is constructed as a presheaf category over the index category
; of unital quivers, which is a category with two objects and seven morphisms: the
; source morphism, the target morphism, the identity morphism, the source identity,
; and the target identity morphisms with the obvious compositions. Then the morphisms in
; this category are the natural transformations of corresponding presheaves. A useful
; instance of morphisms of unital quivers comes from the data of a functor of categories, then
; the morphism of unital quivers describe the fact that the functor preserves identities.

; Generalized morphisms of in the topos of unital quivers
(defprotocol StructuredMorphismOfUnitalQuivers
  "A structured morphism of unital quivers, such as a functor, is any morphism equipped with a
   functor to the topos of unital quivers."

  (underlying-morphism-of-unital-quivers [this]
    "Get the underlying morphism of unital quivers of a morphism."))

; Morphisms in the topos of unital quivers
(deftype MorphismOfUnitalQuivers [source-quiver target-quiver input-function output-function]
  AbstractMorphism
  (source-object [this] source-quiver)
  (target-object [this] target-quiver)

  StructuredDifunction
  (first-function [this] input-function)
  (second-function [this] output-function)

  StructuredMorphismOfQuivers
  (underlying-morphism-of-quivers [this]
    (->MorphismOfQuivers
      source-quiver
      target-quiver
      input-function
      output-function))

  StructuredMorphismOfUnitalQuivers
  (underlying-morphism-of-unital-quivers [this] this))

; Get the morphisms of identity element functions of a morphism of unital quivers
; the order of the functions in the morphism is transposed because the identity
; element function goes backwards between vertices and edges.
(defn morphism-of-identity-element-functions
  [morphism]

  (->Diamond
    (identity-element-function (source-object morphism))
    (identity-element-function (target-object morphism))
    (second-function morphism)
    (first-function morphism)))

(defn morphism-of-source-identity-functions
  [morphism]

  (->Diamond
    (source-identity-function (source-object morphism))
    (source-identity-function (target-object morphism))
    (first-function morphism)
    (first-function morphism)))

(defn morphism-of-target-identity-functions
  [morphism]

  (->Diamond
    (target-identity-function (source-object morphism))
    (target-identity-function (target-object morphism))
    (first-function morphism)
    (first-function morphism)))

; Composition and identities in the topos of unital quivers
(defmethod compose* MorphismOfUnitalQuivers
  [a b]

  (MorphismOfUnitalQuivers.
    (source-object b)
    (target-object a)
    (compose-functions (first-function a) (first-function b))
    (compose-functions (second-function a) (second-function b))))

(defmethod identity-morphism UnitalQuiver
  [quiv]

  (MorphismOfUnitalQuivers.
    quiv
    quiv
    (identity-function (first-set quiv))
    (identity-function (second-set quiv))))

; Ontology of morphisms in the topos of unital quivers
(defn morphism-of-unital-quivers?
  [morphism]

  (= (type morphism) MorphismOfUnitalQuivers))