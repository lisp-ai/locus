(ns locus.linear.module.morphism
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.action.core.protocols :refer :all]
            [locus.elementary.action.global.object :refer :all]
            [locus.elementary.action.global.morphism :refer :all]
            [locus.elementary.semigroup.core.object :refer :all]
            [locus.elementary.semigroup.monoid.object :refer :all]
            [locus.elementary.semigroup.monoid.morphism :refer :all]
            [locus.elementary.semigroup.core.morphism :refer :all]
            [locus.elementary.group.core.object :refer :all]
            [locus.elementary.group.core.morphism :refer :all]
            [locus.ring.core.object :refer :all]
            [locus.ring.core.protocols :refer :all]
            [locus.ring.core.morphism :refer :all]
            [locus.semiring.core.object :refer :all]
            [locus.semiring.core.morphism :refer :all]
            [locus.linear.semimodule.object :refer :all]
            [locus.linear.module.object :refer :all]
            [locus.linear.semimodule.utils :refer :all])
  (:import (locus.linear.module.object Module)))

; Morphisms in categories of semimodules
(deftype ModuleMorphism [source target func]
  AbstractMorphism
  (source-object [this] source)
  (target-object [this] target)

  ConcreteMorphism
  (inputs [this] (underlying-set source))
  (outputs [this] (underlying-set target))

  clojure.lang.IFn
  (invoke [this arg] (func arg))
  (applyTo [this args] (clojure.lang.AFn/applyToHelper this args)))

; Identities and composition in categories of semimodules
(defmethod identity-morphism Module
  [semimodule]

  (ModuleMorphism. semimodule semimodule identity))

(defmethod compose* ModuleMorphism
  [a b]

  (ModuleMorphism.
    (source-object b)
    (target-object a)
    (comp (.func a) (.func b))))

; Hom groups in abelian categories of modules
(defn module-hom-class
  [a b]

  (fn [morphism]
    (and
      (= (type morphism) ModuleMorphism)
      (= (source-object morphism) a)
      (= (target-object morphism) b))))

(defn add-module-morphisms
  [m1 m2]

  (ModuleMorphism.
    (source-object m1)
    (target-object m1)
    (fn [x]
      (let [add (additive-semigroup (target-object m1))]
        (add [(m1 x) (m2 x)])))))

(defn zero-module-morphism
  ([source]
   (zero-module-morphism source source))
  ([source target]
   (let [add (.semigroup target)
         id (first (identity-elements add))]
     (ModuleMorphism.
       source
       target
       (fn [x] id)))))

(defn negate-module-morphism
  [morphism]

  (ModuleMorphism.
    (source-object morphism)
    (target-object morphism)
    (fn [x]
      (let [neg (additive-inverse-function (target-object morphism))]
        (neg (morphism x))))))

(defn additive-hom-group
  [a b]

  (->Group
    (module-hom-class a b)
    (fn [[a b]]
      (add-module-morphisms a b))
    (zero-module-morphism a b)
    (fn [a]
      (negate-module-morphism a))))

; Endomorphism rings of modules
(defmethod endomorphism-algebra Module
  [module]

  (make-ring
    (additive-hom-group module module)
    (->Monoid
      (module-hom-class module module)
      (fn [[a b]]
        (compose a b))
      (identity-morphism module))))

; Let M be an R-module then we say that there exists a function
; s: R -> End(M,+) that characterises the R module as a special type of
; ring homomorphism to an endomorphism algeba.
(defn action-ring-homomorphism
  [^Module module]

  (let [out-module (to-module (additive-semigroup module))]
    (->RingMorphism
     (.ring module)
     (endomorphism-algebra out-module)
     (fn [action]
       (->ModuleMorphism
         out-module
         out-module
         (fn [i]
           (apply-action module action i)))))))

; Determine which category a module homomorphism belongs in
(defn module-homomorphism-ring-classifier
  [ring]

  (->Universal
    (fn [morphism]
      (and
        (= (type morphism) ModuleMorphism)
        (= (.ring (source-object morphism))
           (.ring (target-object morphism))
           ring)))))

; Ontology of module homomorphisms
(defn module-homomorphism?
  [morphism]

  (= (type morphism) ModuleMorphism))