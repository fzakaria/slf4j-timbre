# slf4j-timbre

[SLF4J](http://www.slf4j.org/) binding for Clojure's [Timbre](https://github.com/ptaoussanis/timbre) logging library

## Usage

[![Clojars Project](http://clojars.org/com.fzakaria/slf4j-timbre/latest-version.svg)](http://clojars.org/com.fzakaria/slf4j-timbre)

You might depend on a Java project which is configured to use SLF4J / JCL / LOG4j / JUL. Rather than having to manage a
separate logging configuration, SLF4J provides the means to have SLF4J delegate to another implementation
(out of the box is [Logback](http://logback.qos.ch/))

You'll also want to make sure all other logging APIs are wrapped by SLF4J.

In your **project.clj** include the following:

    [org.slf4j/log4j-over-slf4j "1.7.12"]
    [org.slf4j/jul-to-slf4j "1.7.12"]
    [org.slf4j/jcl-over-slf4j "1.7.12"]


## License

Copyright © 2015 Farid Zakaria

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
