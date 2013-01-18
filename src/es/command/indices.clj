(ns es.command.indices
  (:require [es.data.indices :as idx]
            [es.util :refer [maybe-get-in]]))

(def cols
  ['status
   'name
   'pri
   'rep
   'size
   'bytes
   'docs])

(defn go [args {:keys [url verbose]}]
  (concat
   (if verbose
     [(map str cols)])
   (for [[nam data] (idx/indices url args)]
     (let [pri (- (-> data :health :number_of_shards)
                  (-> data :health :number_of_replicas))]
       [(maybe-get-in data :health :status)
        (name nam)
        pri
        (maybe-get-in data :health :number_of_replicas)
        (maybe-get-in data :stats :total :store :size)
        (maybe-get-in data :stats :total :store :size_in_bytes)
        (maybe-get-in data :stats :total :docs :count)]))))

