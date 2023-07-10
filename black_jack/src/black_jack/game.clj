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

;(defn opcao-A [posicao]
;  (printf "Deseja que o valor do %s° A seja: 1 ou 11?" posicao)
;  (read-line))


(defn valor-A [cards]
  "Definir o valor do A"
  (map #(if (<= (reduce + cards) 21) (if (= 1 %) 11 #_(opcao-A (.indexOf cards 1)) %) %) cards))


(defn carta-pontos
  [cartas]
  "Pontos das cartas"
  (reduce + cartas))

; Representar jogador
(defn jogador
  [& args]
  (let [cartas-jogador [(nova-carta) (nova-carta)]
        pontos-cartas (carta-pontos (valor-A (map parse-JQK cartas-jogador)))]
    (println "Digite seu nome: ")
    {:player-name (if (empty? args) (read-line) args)
     :cards       cartas-jogador
     :points      pontos-cartas}))

(defn mais-cartas [jogador]
  "Pegar mais cartas para o jogador"
  (let [jogador-cards (assoc jogador :cards (conj (:cards jogador) (parse-JQK (nova-carta))))]
    (assoc
      jogador-cards
      :points (carta-pontos (:cards jogador-cards)))))

(defn decisao-jogador? [player]
  (println (:player-name player) ": mais carta?")
  (= (read-line) "s"))

(defn decisao-dealer? [player-points dealer]
  (let [dealer-points (:points dealer)]
    (if (> player-points 21) false (<= dealer-points player-points))))
(defn jogo [player funcao-continue?]
  "Inicia o jogo e as funções de cada uma"
  (if (funcao-continue? player)
    (let [cartas-jogador (mais-cartas player)]
      (card/print-player cartas-jogador)
      (recur cartas-jogador funcao-continue?))
    player))


(def jogador1 (jogador))
(card/print-player jogador1)
(def dealer (jogador "Dealer"))
(card/print-masked-player dealer)
(def jogador-final (jogo jogador1 decisao-jogador?))
(jogo dealer (partial decisao-dealer? (:points jogador-final)))
