# docker build -t liquid-vim .
# docker run -i -t --rm=true liquid-vim /bin/bash
# clj -m liana.core

FROM ubuntu

MAINTAINER Mogens Lund <salza@salza.dk>

RUN apt-get update

RUN apt-get install -y \
  curl \
  git \
  rlwrap \
  default-jre \
  net-tools

RUN curl -O https://download.clojure.org/install/linux-install.sh
RUN chmod +x linux-install.sh
RUN ./linux-install.sh

RUN useradd -ms /bin/bash liana
WORKDIR /home/liana

RUN mkdir proj
WORKDIR /home/liana/proj
RUN git clone https://github.com/mogenslund/liquid.git
RUN git clone https://github.com/mogenslund/liquid-vim.git


WORKDIR /home/liana
RUN mkdir -p liq/src/liana

RUN echo "(ns liana.core\n  (:require [dk.salza.liquid-vim.core :as vimcore])\n  (:gen-class))\n\n(defn -main\n  [& args]\n  (apply vimcore/-main args))\n" > liq/src/liana/core.clj

RUN echo "{:deps {org.clojure/data.json {:mvn/version \"0.2.6\"}\n        org.clojure/core.async {:mvn/version \"0.3.465\"}}\n :paths [\"src\"\n         \"../proj/liquid/src\"\n         \"../proj/liquid/resources\"\n         \"../proj/liquid-vim/src\"]}" > liq/deps.edn

WORKDIR /home/liana/liq
