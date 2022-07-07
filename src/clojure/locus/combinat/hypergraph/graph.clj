(ns locus.combinat.hypergraph.graph
  (:require [locus.elementary.logic.base.core :refer :all]
            [locus.elementary.relation.binary.product :refer :all]
            [locus.elementary.logic.order.seq :refer :all]
            [locus.elementary.incidence.system.family :refer :all]
            [locus.elementary.incidence.system.multifamily :refer :all]
            [locus.elementary.relation.binary.br :refer :all]
            [locus.elementary.relation.binary.sr :refer :all]
            [locus.elementary.function.core.protocols :refer :all]
            [locus.elementary.quiver.core.object :refer :all]
            [locus.elementary.quiver.permutable.object :refer :all]
            [locus.combinat.hypergraph.object :refer :all]))

; Graphs
; The category of graphs can be interpreted presheaf topos theoretically as thin permutable
; quivers. Therefore, we provide the to-permutable-quiver method to convert a given
; graph into an object of the topos of permutable quivers in order to support the topos
; theory of undirected graphs. We also implement graphs in terms of hypergraphs which
; are described by the topos of span copresheaves.

(deftype Graph [vertices edges]
  ConcreteObject
  (underlying-set [this] vertices))

(derive Graph :locus.elementary.function.core.protocols/structured-set)

(defmethod to-permutable-quiver Graph
  [^Graph graph]

  (symmetric-relation->permutable-quiver
    (underlying-set graph)
    (symmetric-binary-relation (.edges graph))))

(defmethod visualize Graph
  [graph] (visualize (edge-set graph)))

(defmethod hypergraph? Graph
  [graph] true)

; Graph order and size metrics
(defn graph-order
  [graph]

  (count (underlying-set graph)))

(defn graph-size
  [graph]

  (count (edge-set graph)))

; Complementation mechanisms in the category of graphs
(defn complement-graph
  [graph]

  (Graph.
    (underlying-set graph)
    (difference
      (complete-dependency-family (underlying-set graph))
      (edge-set graph))))

(defn complement-simple-graph
  [graph]

  (Graph.
    (underlying-set graph)
    (difference (selections (underlying-set graph) 2) (edge-set graph))))

; Construct a graph from a set system
(defn graph
  [family]

  (Graph. (apply union family) family))

; The line graph of a graph has edges for its vertices
(defn line-graph
  [graph]

  (Graph.
    (edge-set graph)
    (set
      (for [[a b] (cartesian-power (edge-set graph) 2)
            :when (not (empty? (intersection a b)))]
        #{a b}))))

; Graphs related to intersection and set systems
(defn johnson-graph
  [coll k]

  (let [elems (selections coll k)]
    (Graph.
      elems
      (set
        (filter
          (fn [pair]
            (= (count (apply intersection pair)) (dec k)))
          (selections elems 2))))))

(defn kneser-graph
  [coll k]

  (let [elems (selections coll k)]
    (Graph.
      elems
      (set
        (filter
          (fn [pair]
            (empty? (apply intersection pair)))
          (selections elems 2))))))




