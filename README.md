# Vim like extension for Liquid Text Editor
This extension adds vim like features for [Liquid Text Editor](https://github.com/mogenslund/liquid)

## Status
The extension is in experimental state, for exploration of the possibilities.

Feel free to add more features and create pull requests. Take a look at the code base. It is not that big, as much can be build on top of existing functionality in [Liquid](https://github.com/mogenslund/liquid).

In this experimental phase I aim for functionality matching vim. Making pretty, adjusting details and doing autocompletion, I know will be possible later. So right now, either a command works or nothing happens.

## Video
[![IMAGE ALT TEXT](http://img.youtube.com/vi/pJ0SkNeFMok/0.jpg)](http://www.youtube.com/watch?v=pJ0SkNeFMok "Walk Through Video")

## Installation
For a general introduction to installing [Liquid](https://github.com/mogenslund/liquid) extensions take a look at [Basic Setup](http://salza.dk/setupbasic.html).

It is possible to make an "easier" installation than descibed below, but I prefer to encourage the most general and flexible way, so you will be rewarded understanding the setup. Feel free the adjust the suggested folders and to skip cloning projects that are already cloned.

The procedure will work on Linux and probably Mac. I will assume you already have installed

 * Java
 * Git
 * Clojure 1.9 with clj command. See [clojure.org](https://clojure.org/guides/getting_started)

### Clone liquid and liquid-vim
Execute the commands below in `~/proj`:

    git clone https://github.com/mogenslund/liquid.git
    git clone https://github.com/mogenslund/liquid-vim.git

Now Liquid and Liquid-vim extensions are installed.

### Load Liquid and Liquid-vim into your own setup
Following the convensions in [Basic Setup](http://salza.dk/setupbasic.html) I assume you have have folder `~/liq` as your own entry point for starting Liquid.

Add releative links to liquid and liquid-vim in the `~/liq/deps.edn` file, so it looks like

```clojure
{:deps {org.clojure/data.json {:mvn/version "0.2.6"}
        org.clojure/core.async {:mvn/version "0.3.465"}}
 :paths ["src"
         "../proj/liquid/src"
         "../proj/liquid/resources"
         "../proj/liquid-vim/src"]}
```

With some extra dependencies for illustration. (Of cause if you have other extensions they should stay in the file.)

### Adjust local core.clj to target liquid-vim
Again I will assume, like in [Basic Setup](http://salza.dk/setupbasic.html), that you have a file `~/liq/src/liana/core.clj`.

Adjust this file to use the new extension. It might look like:

```clojure
(ns liana.core
  (:require [dk.salza.liquid-vim.core :as vimcore])
  (:gen-class))


(defn -main
  [& args]
  (apply vimcore/-main args))
```

### Starting with local core
In `~/liq/src` execute:

    clj -m liquid.core

## Docker
There is a [Dockerfile](Dockerfile) for running the demo project. It can be used as just demo, but also for experimenting.

### Start the container
To build the container execute

    docker build -t liquid-vim .

To boot the container afterwards execute

    docker run -i -t --rm=true liquid-vim /bin/bash

To start Liquid with the vim extension run the following inside the container

    clj -m liana.core