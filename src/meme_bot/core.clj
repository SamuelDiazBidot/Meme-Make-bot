(ns meme-bot.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [discord.bot :as bot]
            [clojure.string :as str]))

(defn requestMeme [template text0 text1]
  "Makes a POST request to the imgflip api"
  (client/post "https://api.imgflip.com/caption_image"
    {:form-params {:template_id template :username "ginosashi" :password "password" :text0 text0 :text1 text1}}))

  (def templates 
    { "Woman Yelling At Cat" 188390779
      "Two Buttons" 87743020
      "Mocking Spongebob" 102156234
      "Drake Hotline Bling" 181913649
      "Change My Mind"  129242436
      "Surprised Pikachu" 155067746
      "Tuxedo Winnie The Pooh" 178591752
    })

(defn getURL [response]
  (let [success (-> response (:body) (json/read-str :key-fn keyword) (:success))]
    (if success
      (-> response (:body) (json/read-str :key-fn keyword) (get-in [:data :url]) (bot/say))
      (bot/say "Something went wrong :("))))

(bot/defcommand makeMeme [client message]
  "How to use: !makeMeme -Meme Template -text1 -text2"
  (if-let [content (not-empty (:content message))]
    (let [tokens (str/split content #"(\s)?-")]
      (if-let [templateId (get templates (get tokens 1))]
        (let [text0 (get tokens 2)
              text1 (get tokens 3)
              response (requestMeme templateId text0 text1)]
          (getURL response))
        (bot/say "Invalid template name")))
    (bot/say "How to use: !makeMeme -MemeTemplate -text1 -text2")))

(bot/defcommand listMemes [client message]
  "Sends a list of the available memes"
  (let [memeList (clojure.string/join "\n" (keys templates))]
    (bot/say (str "**Available memes:**\n" memeList))))

(defn -main [& args]
  (bot/start))