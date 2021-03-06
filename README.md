# clj-templates

A website for finding Leiningen and Boot templates.


[![Build Status](https://travis-ci.org/Dexterminator/clj-templates.svg?branch=master)](https://travis-ci.org/Dexterminator/clj-templates)
## Development

In order to launch the app in dev mode, setting up a server on `localhost:3456`, run:

```
lein repl
=> (go)
```

You should now see:

```
=> :initiated
```

This means that the app is up and running, and that figwheel is listening to changes in your ClojureScript files.

To start a ClojureScript REPL, run:
```
=> (cljs-repl)
```
To go back to the regular Clojure REPL, type:
```
=> :cljs/quit
```

To restart the system and reload changed namespaces (in the regular Clojure REPL), run:
```
=> (reset)
```

Check out the [Integrant](https://github.com/weavejester/integrant) README as well as
[Integrant-REPL](https://github.com/weavejester/integrant-repl) for more information on how to
interact with the system in development mode.

### Build CSS

In order to start the stylus autobuilder, run:
```
make stylusbuild
```


### Run tests:

#### Clojure

Autorun (tests run as changes to files are detected):
```
make clj-test
```

Once:
```
make clj-test-once
```

#### ClojureScript
Autorun (tests run as changes to files are detected):
```
make cljs-test
```
Once: 
```
make cljs-test-once
```

### Build CSS and run tests at the same time

Run:
```
make auto-dev
```

To build css, and run both Clojure and ClojureScript tests at the same time.

## Production build

Run:
```
make
```

See the `Procfile` for the production run command.

## Database migrations and bootstrapping

#### Migrations
The fastest way to migrate the development database is through the REPL:
```
=> (migrate)
```

It is also possible to do it from the command line:
```
make migrate PROFILE=dev
```

The testing database needs to be migrated from the command line:
```
make migrate PROFILE=test
```

#### Rollbacks
Rollbacks of the latest migration are also possible from both the REPL and command line.
```
=> (rollback)
make rollback PROFILE=test
```

#### Bootstrapping
To bootstrap the database with some test data, run:
```
=> (bootstrap)
```

## Local env
It is convenient to keep a local `profiles.clj` in the project with local env information,
such as the development database url. For example:
```
{:profiles/dev  {:env {:database-url "jdbc:postgresql://localhost/clj_templates_dev?user=admin&password=secret"}}
 :profiles/test {:env {:database-url "jdbc:postgresql://localhost/clj_templates_test?user=admin&password=secret"}}}
```
