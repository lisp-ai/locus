(ns locus.grothendieck.sheaf.core.object
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.category.core.object :refer :all]
            [locus.elementary.hom.functional.sethom :refer :all]
            [locus.elementary.topoi.copresheaf.object :refer :all]
            [locus.grothendieck.topology.core.object :refer :all]
            [locus.grothendieck.topology.core.morphism :refer :all]
            [locus.grothendieck.site.core.object :refer :all])
  (:import (locus.elementary.function.core.object SetFunction)))

; Let C be a Grothendeick site on a category. Then Sh(X) is its Grothendeick
; topos of sheaves on a site. The objects of this topos are implemented in the
; sheaf class as described below.
(deftype Sheaf [site object-function morphism-function]
  AbstractMorphism
  (source-object [this] site)
  (target-object [this] sets)

  StructuredDifunction
  (first-function [this] morphism-function)
  (second-function [this] object-function))

; A sheaf is an element of a Grothendieck topos of sheaves, however, given a sheaf
; X we can convert into an object of an elementary topos of copresheaves. We do this
; first by getting the elementary category of its site and then taking its dual
; so that we can get past the contravariance in the definition of sheaves.
(defmethod to-copresheaf Sheaf
  [^Sheaf x]

  (->Copresheaf
    (dual (to-category (.site x)))
    (.-object_function x)
    (.-morphism_function x)))

; The most basic example of a sheaf is the sheaf of all functions between two
; sets with no extra conditions on them.
(defn sheaf-of-all-functions
  [source target]

  (Sheaf.
    (discrete-site source)
    (fn [obj]
      (set-hom obj target))
    (fn [[restriction-domain original-domain]]
      (SetFunction.
        (set-hom original-domain target)
        (set-hom restriction-domain target)
        (fn [func]
          (restrict-function func restriction-domain))))))

