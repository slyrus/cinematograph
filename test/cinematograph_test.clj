
(ns cinematograph-test
  (:use [cinematograph.core]
        [shortcut.graph
         :only (NodeSet
                make-graph
                nodes add-node add-nodes
                edges add-edge add-edges
                neighbors
                connected-components connected-component partition-graph)
         :as graph]
        [clojure.java.io :as io]
        [clojure.contrib.str-utils :as c.c.str-utils]))

(map :name (graph/neighbors *actor-film-graph* (get-film-node "Fast Times at Ridgemont High")))
(map :name (graph/neighbors *actor-film-graph* (get-actor-node "Judge Reinhold")))
(map :name (graph/neighbors *actor-film-graph* (get-film-node "Time Bandits")))

(graph/find-node *actor-film-graph*
                 (get-actor-node "Aishwarya Rai")
                 (get-actor-node "Naomi Watts"))

(map :name (graph/find-node *actor-film-graph*
                 (get-actor-node "Bipasha Basu")
                 (get-actor-node "Peter Falk")))

(map :name (graph/find-node *actor-film-graph*
                 (get-actor-node "Doris Day")
                 (get-actor-node "Faye Wong")))

(map :name (graph/find-node *actor-film-graph*
                 (get-actor-node "Gong Li")
                 (get-actor-node "Charlie Chaplin")))

(map :name (graph/find-node *actor-film-graph*
                            (get-film-node "Dazed and Confused")
                            (get-film-node "Fast Times at Ridgemont High")))

