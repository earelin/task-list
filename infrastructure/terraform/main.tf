terraform {
  required_version = ">= 1.11.0"
}

resource "kubernetes_namespace_v1" "app_namespace" {
  metadata {
    name = "task-list-${var.app_enviroment}"
  }
}

module "app" {
  source      = "./modules/app"
  namespace   = local.app_namespace_name
  app_version = var.app_version
}

module "mongodb" {
  source    = "./modules/mongodb"
  namespace = local.app_namespace_name
}

resource "kubernetes_namespace_v1" "common_namespace" {
  metadata {
    name = local.common_namespace_name
  }
}

module "jaeger" {
  source    = "./modules/jaeger"
  namespace = local.common_namespace_name
}

module "prometheus" {
  source    = "./modules/prometheus"
  namespace = local.common_namespace_name
}

resource "kubernetes_storage_class_v1" "local-storage-retain" {
  metadata {
    name = "local-storage-retain-class"
  }
  # reclaim_policy = "Retain"
  storage_provisioner = "k8s.io/minikube-hostpath"
}
