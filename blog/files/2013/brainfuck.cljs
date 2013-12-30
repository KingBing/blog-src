(ns cljs-brainfuck.core
  (:require [cljs.nodejs :as nodejs]
            [clojure.string :as str]))

(defn read-input [cell cells]
  (let [sync-prompt (nodejs/require "sync-prompt")
        p (.prompt sync-prompt)]
    (reset! cells (assoc @cells @cell
                         (.charCodeAt (.trim (.toString p)) 0)))))

(defn bf-loop [direction pointer commands]
  (let [val (if (= direction :forward) 1 -1)]
    (loop [count 1]
      (when-not (= count 0)
        (reset! pointer (+ @pointer val))
        (case (nth commands @pointer)
          \[ (recur (+ count val))
          \] (recur (- count val))
          (recur count))))))

(defn exec-command [cell cells commands pointer]
  (case (nth commands @pointer)
    \> (swap! cell inc)
    \< (swap! cell dec)
    \+ (reset! cells (assoc @cells @cell (inc (get @cells @cell))))
    \- (reset! cells (assoc @cells @cell (dec (get @cells @cell))))
    \. (print (char (get @cells @cell)))
    \, (read-input cell cells)
    \[ (if     (= (get @cells @cell) 0) (bf-loop :forward  pointer commands))
    \] (if-not (= (get @cells @cell) 0) (bf-loop :backward pointer commands))
    ()))

(defn interpret [commands]
  (let [cell  (atom 0)
        cells (atom (vec (repeat 30000 0)))]
    (loop [pointer (atom 0)]
      (exec-command cell cells commands pointer)
      (swap! pointer inc)
      (if-not (= @pointer (count commands)) (recur pointer)))))

(defn -main [& args]
  (let [arg1 (nth args 0)
        fs (nodejs/require "fs")]
    (if arg1
      (.readFile fs arg1 "utf8" (fn [err data]
                                  (if err (println err)
                                      (interpret (str/trim-newline data)))))
      (println "Error: Please specify filename."))))

(set! *main-cli-fn* -main)
