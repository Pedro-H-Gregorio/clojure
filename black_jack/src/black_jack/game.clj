(ns black-jack.game
  (:require [card-ascii-art.core :as card]))

; Função que gera carta do A - K

(defn nova-carta
  []
  "Gerar carta para o jogador de 0 a 13"
  (inc (rand-int 13)))

(defn parse-JQK [carta]
  "Verifica a pontuação das cartas e troca caso for J, Q e K"
  (if (> carta 10) 10 carta))

(defn valor-A [cards]
  "Definir o valor do A"
  (if (<= (reduce + cards) 21) (map #(if (= 1 %) 11 %) cards) cards))


(defn carta-pontos
  [cartas]
  "Pontos das cartas"
  (reduce + (valor-A cartas)))

; Representar jogador
(defn jogador
  [nome-do-jogador]
  (let [cartas-jogador [(nova-carta) (nova-carta)]
        pontos-cartas (carta-pontos (map parse-JQK cartas-jogador))]
    {:player-name nome-do-jogador
     :cards       cartas-jogador
     :points      pontos-cartas}))

(defn mais-cartas [jogador]
  "Pegar mais cartas para o jogador"
  (assoc
    (update jogador :cards conj (parse-JQK (nova-carta)))
    :points (carta-pontos (:cards jogador))))

(def jogador1 (jogador "Pedro"))
(card/print-player (mais-cartas jogador1))
