# transactions-watcher

Watches a specified directory and uploads any CSV files to the fosubo endpoint.

## Installation

0. Install [leiningen](http://leiningen.org) if you don't have it already
1. Clone the repo

then:

```
$ lein uberjar
$ java -jar target/uberjar/transactions-watcher.jar [args]
```

OR:

```
$ lein run -- [args]
```

## Usage

```
Usage: transactions-watcher [options]

Options:
  -d, --watch-directory DIRECTORY  .                                                 Directory to watch for uploaded files
  -u, --upload-url URL             http://localhost:3000/api/v1/upload_transactions  Endpoint URL to POST to
  -t, --token TOKEN                                                                  Auth token for the fosubo API (required)
  -c, --company-id COMPANY_ID                                                        The id of the company in the fosubo database (required)
  -h, --help
```

## Examples

```
java -jar transactions-watcher.jar -c 100 -t 'foobarbazt0k$n' -d '/Users/foo/Desktop' -u 'http://localhost:3000/api/v1/upload_transactions'
```
## License

Copyright © 2015 Sam Davies

Distributed under the MIT license.
