# imageproc-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fimageproc-kotlin-blue.svg)](https://github.com/KotlinMania/imageproc-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/imageproc-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/imageproc-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/imageproc-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/imageproc-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`image-rs/imageproc`](https://github.com/image-rs/imageproc) (crate `imageproc`).

**Original Project:** This port is based on [`image-rs/imageproc`](https://github.com/image-rs/imageproc). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `image-rs/imageproc`

> The text below is reproduced from [`https://github.com/image-rs/imageproc`](https://github.com/image-rs/imageproc). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs where appropriate so they continue to resolve from this repository.

# imageproc

[![crates.io](https://img.shields.io/crates/v/imageproc.svg)](https://crates.io/crates/imageproc)
[![doc-badge]][doc-link]
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/image-rs/imageproc/blob/main/LICENSE)
[![Dependency status](https://deps.rs/repo/github/image-rs/imageproc/status.svg)](https://deps.rs/repo/github/image-rs/imageproc)
[![Coverage Status](https://coveralls.io/repos/github/image-rs/imageproc/badge.svg?branch=main)](https://coveralls.io/github/image-rs/imageproc?branch=main)

An image processing library, based on the
[`image`](https://github.com/image-rs/image) library.

[API documentation][doc-link]

[doc-badge]: https://docs.rs/imageproc/badge.svg
[doc-link]: https://docs.rs/imageproc

## Goals

A performant, well-tested, well-documented library with a consistent API, suitable for use as
the basis of computer vision applications or graphics editors.

## Non-goals

Maximum genericity over image storages or formats, or support for higher-dimensional images.

## Color Space

Functions in this library implicitly assume that pixels colors are
stored in a linear color space like `RGB` as opposed to a non-linear color
space such as `sRGB`.

If you are not familiar with gamma correction then [this article] contains
an introduction and examples of color artefacts resulting
from not using linear color spaces.

[this article]: https://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/

## Parallelism

This library provides both single-threaded and multi-threaded variations of several functions
by using [rayon](https://github.com/rayon-rs/rayon).

Depending on image size and the amount of work performed per pixel the parallel versions may not
always be faster - we recommend benchmarking for your specific use-case.

## Crate Features

### Default Features

- `rayon`: enables multi-threaded versions of several functions
- `text`: enables text drawing
- `fft`: enables `phash` and other functions that depend on the `rustfft` crate

### Optional Features

- `display-window`: enables the displaying of images (using `imageproc::window`) with
  [sdl2](https://github.com/Rust-SDL2/rust-sdl2). SDL2 must be installed on the system before
  building with this feature. For example:

  ```shell
  # Ubuntu/Debian
  sudo apt install libsdl2-dev

  # macOS
  brew install sdl2
  ```

  On Windows, install SDL2 and ensure its library directory is available to the linker and its DLL
  directory is included in `PATH`.

## How to contribute

See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:imageproc-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same license terms as the upstream [`image-rs/imageproc`](https://github.com/image-rs/imageproc). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the imageproc authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`image-rs/imageproc`](https://github.com/image-rs/imageproc) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
