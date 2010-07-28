;;; file: cinematograph/core.clj
;;;
;;; Copyright (c) 2010 Cyrus Harmon (ch-lisp@bobobeach.com) All rights
;;; reserved.
;;;
;;; Redistribution and use in source and binary forms, with or without
;;; modification, are permitted provided that the following conditions
;;; are met:
;;;
;;;   * Redistributions of source code must retain the above copyright
;;;     notice, this list of conditions and the following disclaimer.
;;;
;;;   * Redistributions in binary form must reproduce the above
;;;     copyright notice, this list of conditions and the following
;;;     disclaimer in the documentation and/or other materials
;;;     provided with the distribution.
;;;
;;; THIS SOFTWARE IS PROVIDED BY THE AUTHOR 'AS IS' AND ANY EXPRESSED
;;; OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
;;; WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
;;; ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
;;; DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
;;; DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
;;; GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
;;; INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
;;; WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
;;; NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
;;; SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

(ns cinematograph.core
  (:use [shortcut.graph
         :only (NodeSet
                make-graph
                nodes add-node add-nodes
                edges add-edge add-edges
                neighbors
                connected-components connected-component partition-graph)
         :as graph]
        [clojure.java.io :as io]
        [clojure.contrib.str-utils :as c.c.str-utils]))

(defmacro apply-ctor
  [classname args]
  `(~(symbol (str classname \.)) ~@args))

(def *actor-file* "actor.tsv.gz")
(def *film-file* "film.tsv.gz")

(defrecord Actor [name id film dubbing_performances netflix_id nytimes_id])

(defn multi [str] 
  (set (when (and str (not (empty? str)))
         (c.c.str-utils/re-split #"," str))))

(defn make-actor [& args]
  (let [[name id film dubbing_performances netflix_id nytimes_id] args]
    (new Actor name id (multi film) dubbing_performances netflix_id nytimes_id)))

(defrecord Film [name id initial_release_date directed_by produced_by
                 written_by cinematography edited_by music language
                 rating estimated_budget country starring runtime
                 locations film_collections soundtrack
                 featured_film_locations genre film_series story_by
                 sequel prequel subjects personal_appearances
                 dubbing_performances film_format costume_design_by
                 other_crew trailers distributors other_film_companies
                 production_companies tagline release_date_s
                 netflix_id film_festivals nytimes_id featured_song
                 metacritic_id apple_movietrailer_id rottentomatoes_id
                 executive_produced_by film_casting_director
                 film_production_design_by film_art_direction_by
                 film_set_decoration_by traileraddict_id gross_revenue
                 fandango_id])

(defn make-film [& args]
  (let [[name id initial_release_date directed_by produced_by
         written_by cinematography edited_by music language rating
         estimated_budget country starring runtime locations
         film_collections soundtrack featured_film_locations genre
         film_series story_by sequel prequel subjects
         personal_appearances dubbing_performances film_format
         costume_design_by other_crew trailers distributors
         other_film_companies production_companies tagline
         release_date_s netflix_id film_festivals nytimes_id
         featured_song metacritic_id apple_movietrailer_id
         rottentomatoes_id executive_produced_by film_casting_director
         film_production_design_by film_art_direction_by
         film_set_decoration_by traileraddict_id gross_revenue
         fandango_id] args]
    (new Film name id initial_release_date (multi directed_by)
         (multi produced_by)
         (multi written_by) cinematography edited_by music language rating
         estimated_budget country (multi starring) runtime locations
         film_collections soundtrack (multi featured_film_locations)
         (multi genre)
         film_series story_by sequel prequel subjects
         personal_appearances dubbing_performances film_format
         costume_design_by other_crew trailers distributors
         other_film_companies production_companies tagline
         release_date_s netflix_id film_festivals nytimes_id
         featured_song metacritic_id apple_movietrailer_id
         rottentomatoes_id executive_produced_by film_casting_director
         film_production_design_by film_art_direction_by
         film_set_decoration_by traileraddict_id gross_revenue
         fandango_id)))

(def film-graph (graph/make-graph))

(let [[actor-graph actor-id-map actor-name-map]
      (with-open [r (io/reader
                     (java.util.zip.GZIPInputStream.
                      (.getResourceAsStream (clojure.lang.RT/baseLoader)
                                            *actor-file*)))]
        (doall
         (reduce (fn [[graph-db actor-id-map actor-name-map] actor-args]
                   (let [actor (apply make-actor actor-args)]
                     [(add-node graph-db actor)
                      (conj actor-id-map {(:id actor) actor})
                      (conj actor-name-map {(:name actor) actor})]))
                 [(graph/make-graph) {} {}]
                 (lazy-seq (map #(c.c.str-utils/re-split #"\t" %)
                                (drop 1 (line-seq r)))))))]
  (def *actor-graph* actor-graph)
  (def *actor-id-map* actor-id-map)
  (def *actor-name-map* actor-name-map))

(let [[film-graph film-id-map film-name-map]
      (with-open [r (io/reader
                     (java.util.zip.GZIPInputStream.
                      (.getResourceAsStream (clojure.lang.RT/baseLoader)
                                            *film-file*)))]
        (doall
         (reduce (fn [[graph-db film-id-map film-name-map] film-args]
                   (let [film (apply make-film film-args)]
                     [(add-node graph-db film)
                      (conj film-id-map {(:id film) film})
                      (conj film-name-map {(:name film) film})]))
                 [(graph/make-graph) {} {}]
                 (map #(c.c.str-utils/re-split #"\t" %)
                      (drop 1 (line-seq r))))))]
  (def *film-graph* film-graph)
  (def *film-id-map* film-id-map)
  (def *film-name-map* film-name-map))
