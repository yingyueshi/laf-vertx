# 基于vert.x的web框架

A new vertx web framework with flexible configuration and plugin

## 背景

来源于组件化项目的探索，如何让Web项目具有灵活配置，可扩展的特性。期望实现每个请求的处理链可以进行扩展配置。

调研过程中发现Apache项目的Vert.x项目比较符合需求，它本身是一套高性能的异步框架，基于处理链，但缺乏配置和插件化。

基于此实现了laf-vertx项目，让vert.x框架具有灵活配置、插件化能力。

