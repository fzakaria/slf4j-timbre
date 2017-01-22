# slf4j-timbre

[SLF4J](http://www.slf4j.org/) binding for Clojure's [Timbre](https://github.com/ptaoussanis/timbre) logging library

You might depend on a Java project which is configured to use SLF4J / JCL / LOG4j / JUL. Rather than having to manage a separate logging configuration, SLF4J provides the means to have SLF4J delegate to another implementation (the default being [Logback](http://logback.qos.ch/)). This project lets you use Timbre for this instead.

## Usage

Add `slf4j-timbre` to your project dependencies:

[![Clojars Project](http://clojars.org/com.fzakaria/slf4j-timbre/latest-version.svg)](http://clojars.org/com.fzakaria/slf4j-timbre)

You'll also want to make sure all other logging APIs are wrapped by SLF4J by including the following:

```clojure
[org.slf4j/log4j-over-slf4j "1.7.14"]
[org.slf4j/jul-to-slf4j "1.7.14"]
[org.slf4j/jcl-over-slf4j "1.7.14"]
```

**`slf4j-timbre` requires `[org.slf4j/slf4j-api "1.7.14"]` or later so make sure your project or its dependencies are not pulling in an earlier version.**

The project also makes use of features introduced in `[com.taoensso/timbre "4.3.0-RC1"]` so make sure this dependency isn't being shadowed by an earlier version of Timbre in your project.

## License

Copyright © 2017 [Farid Zakaria](https://github.com/fzakaria), [rufoa](https://github.com/rufoa/)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.