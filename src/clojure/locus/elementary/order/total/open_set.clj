(ns locus.elementary.order.total.open-set
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.function.core.object :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.order.total.object :refer :all]
            [locus.elementary.order.total.open-interval :refer :all])
  (:import (locus.elementary.order.total.object TotallyOrderedSet)))

; The base of the order topology of a finite total order consists of open intervals,
; open rays, and the entire set X. Then given these generators we can form any
; open set from a union of them. This total order open set class is designed to handle
; the data of these unions. With this we construct the order topology of a total order.
(deftype TotalOrderOpenSet [order components]
  clojure.lang.IFn
  (invoke [this arg]
    (and
      ((underlying-set order) arg)
      (every?
        (fn [component]
          (component arg))
        components)))
  (applyTo [this args]
    (clojure.lang.AFn/applyToHelper this args)))

(defmethod union* TotalOrderOpenSet
  [^TotalOrderOpenSet a, ^TotalOrderOpenSet b]

  (TotalOrderOpenSet. (.order a) (union (.components a) (.components b))))

(defn open-set-of-total-order?
  [order open]

  (and
    (= (type open) TotalOrderOpenSet)
    (= (.space open) order)))
