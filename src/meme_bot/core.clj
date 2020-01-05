(ns meme-bot.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [discord.bot :as bot]
            [clojure.string :as str]))

(defn requestMeme [template text0 text1]
  "Makes a POST request to the imgflip api"
  (client/post "https://api.imgflip.com/caption_image"
    {:form-params {:template_id template :username "ginosashi" :password "password" :text0 text0 :text1 text1}}))

(defn matchTemplate [meme]
  (case (str meme)
    "Woman Yelling At Cat" 188390779
    "Two Buttons" 87743020
    "Mocking Spongebob" 102156234
    "Drake Hotline Bling" 181913649
    "Change My Mind"  129242436
    "Surprised Pikachu" 155067746
    "Tuxedo Winnie The Pooh" 178591752))

(bot/defcommand make-meme [client message]
  (if-let [content (not-empty (:content message))]
    (let [tokens (str/split content #"(\s)?-")
          template (get tokens 1)
          text0 (get tokens 2)
          text1 (get tokens 3)
          response (requestMeme (matchTemplate template) text0 text1)]
      ;;(bot/say (json/write-str response)))))
        (bot/say (str template text0 text1)))))

(defn -main [& args]
  (bot/start))