(ns locus.transcendental.series.power-series
  (:refer-clojure :exclude [+ - * /])
  (:require [locus.elementary.logic.base.core :refer :all :exclude [add]]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.semigroup.core.object :refer :all]
            [locus.elementary.semigroup.monoid.object :refer :all]
            [locus.elementary.group.core.object :refer :all]
            [locus.semiring.core.object :refer :all]
            [locus.semiring.set.rset :refer :all]
            [locus.ring.core.object :refer :all]
            [locus.ring.core.arithmetic :refer :all]
            [locus.ring.core.protocols :refer :all]
            [locus.polynomial.core.object :refer :all]))

; Let R be a ring, then R[[X]] is its power series ring. In order to construct this
; power series ring R[[X]] we need to define a special PowerSeries data type to
; hold power series data. The power series ring can be considered to be the ring
; completion of the polynomial ring R[X]. As a consequence, for any given power
; series we can get an initial polynomial part of it using the take polynomial function.

(deftype PowerSeries [ring terms]
  RingedSet
  (ring-of-coefficients [this] ring)
  (terms [this] natural-number?)
  (coefficient [this n] (terms n)))

(defn take-polynomial
  [n series]

  (univariate-polynomial
    (ring-of-coefficients series)
    (map (fn [i] (coefficient series i)) (range n))))

; Generic methods for handling power series
(defmethod negate PowerSeries
  [^PowerSeries series]

  (PowerSeries.
    (.ring series)
    (fn [i]
      (- (coefficient series i)))))

(defmethod add [PowerSeries PowerSeries]
  [^PowerSeries series1, ^PowerSeries series2]

  (PowerSeries.
    (.ring series1)
    (fn [i]
      (+ (coefficient series1 i) (coefficient series2 i)))))

(defmethod multiply [PowerSeries PowerSeries]
  [^PowerSeries series1, ^PowerSeries series2]

  (PowerSeries.
    (.ring series1)
    (fn [n]
      (apply
        +
        (map
          (fn [i]
            (* (coefficient series1 i)
               (coefficient series2 (- n i))))
          (range (inc n)))))))

; Exponential power series
(def exp-power-series
  (PowerSeries.
    qq
    (fn [i]
      (/ (factorial i)))))

(def cosh-power-series
  (PowerSeries.
    qq
    (fn [i]
      (if (odd? i)
        0
        (/ (factorial i))))))

(def sinh-power-series
  (PowerSeries.
    qq
    (fn [i]
      (if (even? i)
        0
        (/ (factorial i))))))

(def cos-power-series
  (PowerSeries.
    qq
    (fn [i]
      (if (odd? i)
        0
        (/ (Math/pow -1 (/ i 2))
           (factorial i))))))

(def sin-power-series
  (PowerSeries.
    qq
    (fn [i]
      (if (even? i)
        0
        (/ (Math/pow -1 (/ (dec i) 2))
           (factorial i))))))





