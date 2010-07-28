
(ns cinematograph-test
  (:use [cinematograph.core])
  (:require [shortcut.graph
             :only (NodeSet
                    make-graph
                    nodes add-node add-nodes
                    edges add-edge add-edges
                    neighbors
                    connected-components connected-component partition-graph)
             :as graph]
            [clojure.java.io :as io]
            [clojure.contrib.str-utils :as c.c.str-utils]))

(map :name (neighbors (get-film-node "Fast Times at Ridgemont High")))
(map :name (neighbors (get-actor-node "Judge Reinhold")))
(map :name (neighbors (get-film-node "Time Bandits")))

(find-path
 (get-actor-node "Aishwarya Rai")
 (get-actor-node "Naomi Watts"))

(map :name (find-path
            (get-actor-node "Maggie Gyllenhaal")
            (get-actor-node "Judy Garland")))

(map :name (find-path
            (get-film-node "Dazed and Confused")
            (get-film-node "Fast Times at Ridgemont High")))

(map :name (find-path
            (get-actor-node "Lana Turner")
            (get-actor-node "Alec Guinness")))

;; Ok, let's find every movie in which an actor who was on Fast Times at Rigdemont High appeared:

(map :name
     (reduce into (map neighbors
                       (neighbors (get-film-node "Fast Times at Ridgemont High")))))
